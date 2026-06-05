import io
import logging
from typing import Dict, List

import numpy as np
from fastapi import FastAPI, File, HTTPException, UploadFile
from fastapi.middleware.cors import CORSMiddleware
from PIL import Image, UnidentifiedImageError
from pydantic import BaseModel

from config import ALLOWED_ORIGINS, CONFIDENCE_THRESHOLD, IMAGE_SIZE, MODEL_PATH, USE_MOCK
from model_loader import load_predictor

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("leafguard-api")

# Central knowledge base used by both the API response and the /diseases helper endpoint.
DISEASE_INFO: Dict[str, Dict[str, str]] = {
    "Tomato Early Blight": {
        "symptoms": "Small brown spots with concentric rings, yellowing around lesions, and damage starting on older leaves.",
        "treatment": "Remove infected leaves, improve airflow, mulch soil, and apply a labeled fungicide when pressure is high.",
        "prevention": "Rotate crops, avoid wetting foliage in the evening, and disinfect tools between plants."
    },
    "Tomato Late Blight": {
        "symptoms": "Water-soaked patches that quickly darken, white fuzzy growth underneath leaves, and rapid whole-plant collapse.",
        "treatment": "Isolate infected plants, remove severely affected tissue, and apply an appropriate late blight fungicide immediately.",
        "prevention": "Use disease-free seedlings, space plants well, and avoid overhead irrigation during humid weather."
    },
    "Tomato Healthy": {
        "symptoms": "Leaf remains green, evenly colored, and free from lesions, mold, curling, or necrotic patches.",
        "treatment": "No treatment needed. Continue normal watering, feeding, and routine scouting.",
        "prevention": "Maintain balanced nutrition, monitor weekly, and keep weeds and debris away from the crop."
    },
    "Potato Late Blight": {
        "symptoms": "Dark blotches expand rapidly, stems blacken, and white mold may appear at lesion edges in humid conditions.",
        "treatment": "Remove badly infected foliage, avoid moving spores between rows, and apply a recommended protectant fungicide.",
        "prevention": "Plant resistant varieties where possible and destroy volunteer potatoes after harvest."
    },
    "Potato Early Blight": {
        "symptoms": "Dark target-like rings on older leaves followed by yellowing and premature leaf drop.",
        "treatment": "Prune affected leaves, support plant vigor with correct fertilization, and treat with fungicide if needed.",
        "prevention": "Rotate away from solanaceous crops and water at soil level instead of soaking the canopy."
    },
    "Potato Healthy": {
        "symptoms": "Leaves look firm, green, and free of spots, halos, wilting, or unusual discoloration.",
        "treatment": "No treatment required beyond standard crop care.",
        "prevention": "Keep monitoring field hygiene, irrigation balance, and nutrient supply."
    },
    "Corn Gray Leaf Spot": {
        "symptoms": "Rectangular gray or tan lesions running parallel to leaf veins, usually beginning on lower leaves.",
        "treatment": "Scout regularly, remove heavily damaged leaves where practical, and apply fungicide based on local guidance.",
        "prevention": "Rotate fields, manage residue, and choose resistant hybrids when available."
    },
    "Corn Northern Leaf Blight": {
        "symptoms": "Long cigar-shaped gray-green lesions that enlarge and reduce photosynthetic area.",
        "treatment": "Use a registered fungicide if disease pressure is high and preserve plant vigor with good agronomy.",
        "prevention": "Rotate crops, select resistant seed, and avoid continuous corn where blight is common."
    },
    "Corn Healthy": {
        "symptoms": "Leaves are uniformly green with normal vein structure and no blight or spotting patterns.",
        "treatment": "No treatment required.",
        "prevention": "Continue regular monitoring, balanced fertilization, and integrated pest management."
    },
    "Apple Scab": {
        "symptoms": "Olive-brown velvety leaf lesions, fruit spotting, and leaf distortion in wet spring conditions.",
        "treatment": "Prune for airflow, remove fallen leaves, and apply protectant fungicides during susceptible growth stages.",
        "prevention": "Use resistant cultivars, sanitize orchard litter, and monitor wet periods carefully."
    }
}

CLASS_NAMES: List[str] = list(DISEASE_INFO.keys())
predictor = load_predictor(CLASS_NAMES)


class PredictionResult(BaseModel):
    disease: str
    confidence: float
    symptoms: str
    treatment: str
    prevention: str


app = FastAPI(
    title="LeafGuard AI Backend",
    description="FastAPI service for plant disease detection using a Keras model or a mock fallback.",
    version="1.0.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=ALLOWED_ORIGINS or ["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


def preprocess_image(raw_bytes: bytes) -> np.ndarray:
    """Convert raw upload bytes into a normalized model-ready tensor."""
    try:
        image = Image.open(io.BytesIO(raw_bytes)).convert("RGB")
    except (UnidentifiedImageError, OSError) as exc:
        raise HTTPException(status_code=400, detail="Invalid image file supplied.") from exc

    resized_image = image.resize((IMAGE_SIZE, IMAGE_SIZE))
    image_array = np.asarray(resized_image, dtype=np.float32) / 255.0
    return np.expand_dims(image_array, axis=0)


@app.get("/")
async def health_check() -> Dict[str, object]:
    """Simple health endpoint to verify the API is running and to expose runtime mode."""
    return {
        "status": "ok",
        "use_mock": USE_MOCK or predictor.use_mock,
        "model_loaded": predictor.model_loaded,
        "model_path": MODEL_PATH,
        "image_size": IMAGE_SIZE,
        "class_count": len(CLASS_NAMES),
    }


@app.get("/diseases")
async def list_diseases() -> Dict[str, object]:
    """Expose every disease currently supported by the response dictionary."""
    return {
        "count": len(DISEASE_INFO),
        "diseases": [
            {
                "name": disease_name,
                **details,
            }
            for disease_name, details in DISEASE_INFO.items()
        ],
    }


@app.post("/predict", response_model=PredictionResult)
async def predict(image: UploadFile = File(...)) -> PredictionResult:
    """
    Receive a leaf image as multipart/form-data, preprocess it,
    run model inference, and return disease guidance in JSON form.
    """
    if not image.content_type or not image.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="Uploaded file must be an image.")

    try:
        raw_bytes = await image.read()
        if not raw_bytes:
            raise HTTPException(status_code=400, detail="Uploaded image is empty.")

        model_input = preprocess_image(raw_bytes)
        disease_name, confidence = predictor.predict(model_input)
        metadata = DISEASE_INFO.get(
            disease_name,
            {
                "symptoms": "No symptom notes are available for this prediction.",
                "treatment": "Consult a plant pathology reference before taking action.",
                "prevention": "Capture more images and verify the diagnosis manually.",
            },
        )

        if confidence < CONFIDENCE_THRESHOLD:
            logger.info(
                "Low-confidence prediction returned: %s (%.2f < %.2f)",
                disease_name,
                confidence,
                CONFIDENCE_THRESHOLD,
            )

        return PredictionResult(
            disease=disease_name,
            confidence=round(float(confidence), 4),
            symptoms=metadata["symptoms"],
            treatment=metadata["treatment"],
            prevention=metadata["prevention"],
        )
    except HTTPException:
        raise
    except Exception as exc:  # pragma: no cover - runtime guard
        logger.exception("Prediction failed: %s", exc)
        raise HTTPException(status_code=500, detail="Model prediction failed.") from exc
