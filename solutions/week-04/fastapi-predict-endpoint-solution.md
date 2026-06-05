# Week 04 Solution - FastAPI `/predict` Endpoint

## Problem Statement Recap
In Week 04, students build the first working backend endpoint for LeafGuard AI. The goal is to accept an uploaded image, preprocess it, run plant disease prediction, and return structured JSON that the Android app can display.

This solution shows a complete `main.py` implementation with:
- `POST /predict` for image uploads
- `GET /` health check
- `GET /diseases` helper endpoint
- mock mode support when no model file is available
- error handling for invalid images and model failures

---

## Complete `main.py`

```python
import io
import logging
from typing import Dict, List

import numpy as np
from fastapi import FastAPI, File, HTTPException, UploadFile
from fastapi.middleware.cors import CORSMiddleware
from PIL import Image, UnidentifiedImageError
from pydantic import BaseModel

USE_MOCK = True
IMAGE_SIZE = 224
CONFIDENCE_THRESHOLD = 0.50

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("leafguard-api")

DISEASE_INFO: Dict[str, Dict[str, str]] = {
    "Tomato Early Blight": {
        "symptoms": "Small brown spots with ring-like patterns on older leaves.",
        "treatment": "Remove infected leaves and apply a fungicide if needed.",
        "prevention": "Rotate crops and avoid overhead watering.",
    },
    "Tomato Late Blight": {
        "symptoms": "Water-soaked lesions that darken quickly and may show white mold.",
        "treatment": "Isolate affected plants and apply a late blight treatment.",
        "prevention": "Keep foliage dry and use disease-free seedlings.",
    },
    "Tomato Healthy": {
        "symptoms": "Leaves are green and free of disease spots.",
        "treatment": "No treatment required.",
        "prevention": "Continue routine monitoring and balanced care.",
    },
}

CLASS_NAMES: List[str] = list(DISEASE_INFO.keys())


class PredictionResult(BaseModel):
    disease: str
    confidence: float
    symptoms: str
    treatment: str
    prevention: str


app = FastAPI(title="LeafGuard AI Backend", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


def preprocess_image(raw_bytes: bytes) -> np.ndarray:
    try:
        image = Image.open(io.BytesIO(raw_bytes)).convert("RGB")
    except (UnidentifiedImageError, OSError) as exc:
        raise HTTPException(status_code=400, detail="Invalid image file supplied.") from exc

    resized_image = image.resize((IMAGE_SIZE, IMAGE_SIZE))
    image_array = np.asarray(resized_image, dtype=np.float32) / 255.0
    return np.expand_dims(image_array, axis=0)


def run_prediction(image_batch: np.ndarray):
    if USE_MOCK:
        mean_intensity = float(np.mean(image_batch))
        predicted_index = int(round(mean_intensity * (len(CLASS_NAMES) - 1)))
        predicted_index = max(0, min(len(CLASS_NAMES) - 1, predicted_index))
        confidence = round(0.70 + (predicted_index * 0.08), 2)
        return CLASS_NAMES[predicted_index], min(confidence, 0.99)

    raise RuntimeError("Replace this branch with real TensorFlow model inference.")


@app.get("/")
async def health_check():
    return {
        "status": "ok",
        "use_mock": USE_MOCK,
        "class_count": len(CLASS_NAMES),
        "image_size": IMAGE_SIZE,
    }


@app.get("/diseases")
async def list_diseases():
    return {
        "count": len(DISEASE_INFO),
        "diseases": [{"name": name, **details} for name, details in DISEASE_INFO.items()],
    }


@app.post("/predict", response_model=PredictionResult)
async def predict(image: UploadFile = File(...)):
    if not image.content_type or not image.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="Uploaded file must be an image.")

    try:
        raw_bytes = await image.read()
        if not raw_bytes:
            raise HTTPException(status_code=400, detail="Uploaded image is empty.")

        model_input = preprocess_image(raw_bytes)
        disease_name, confidence = run_prediction(model_input)
        disease_data = DISEASE_INFO[disease_name]

        if confidence < CONFIDENCE_THRESHOLD:
            logger.info("Low-confidence result returned: %s (%.2f)", disease_name, confidence)

        return PredictionResult(
            disease=disease_name,
            confidence=confidence,
            symptoms=disease_data["symptoms"],
            treatment=disease_data["treatment"],
            prevention=disease_data["prevention"],
        )
    except HTTPException:
        raise
    except Exception as exc:
        logger.exception("Prediction failed: %s", exc)
        raise HTTPException(status_code=500, detail="Model prediction failed.") from exc
```

---

## Line-by-Line Explanation

### Imports
- `io` lets us read uploaded bytes in memory.
- `logging` helps us debug the backend.
- `numpy` is used for preprocessing and mock prediction logic.
- `FastAPI`, `File`, `UploadFile`, and `HTTPException` are the core FastAPI tools.
- `CORSMiddleware` allows the Android app to call the backend during development.
- `PIL.Image` opens and resizes images.
- `BaseModel` defines the JSON response schema.

### Constants
- `USE_MOCK` lets you run the API before training a real model.
- `IMAGE_SIZE = 224` matches common CNN input size.
- `CONFIDENCE_THRESHOLD` is useful for logging uncertain predictions.

### `DISEASE_INFO`
This dictionary maps each disease name to the text the app should show for symptoms, treatment, and prevention.

### `PredictionResult`
This Pydantic model ensures FastAPI returns consistent JSON.

### `preprocess_image()`
1. Read uploaded bytes.
2. Convert to RGB.
3. Resize to `224 x 224`.
4. Normalize to `[0, 1]`.
5. Add batch dimension so the final shape becomes `(1, 224, 224, 3)`.

### `run_prediction()`
This is a mock implementation for early testing. It produces deterministic output based on average image brightness. Later, replace it with real TensorFlow inference.

### `GET /`
Use this endpoint to confirm the server is alive.

### `GET /diseases`
This helps the frontend inspect available disease labels and descriptions.

### `POST /predict`
1. Validate content type.
2. Read the file.
3. Reject empty uploads.
4. Preprocess the image.
5. Run prediction.
6. Build the final JSON response.
7. Return `400` for bad images and `500` for internal failures.

---

## Testing the Endpoint with `curl`

### 1. Start the server
```bash
uvicorn main:app --reload
```

### 2. Test health check
```bash
curl http://127.0.0.1:8000/
```

### 3. Test diseases endpoint
```bash
curl http://127.0.0.1:8000/diseases
```

### 4. Test prediction endpoint
```bash
curl -X POST http://127.0.0.1:8000/predict \
  -H "accept: application/json" \
  -F "image=@sample-leaf.jpg"
```

### 5. Test invalid file handling
```bash
curl -X POST http://127.0.0.1:8000/predict \
  -H "accept: application/json" \
  -F "image=@notes.txt"
```

Expected result: FastAPI should return a `400` error because the file is not an image.

---

## Common Mistakes

### 1. Forgetting `python-multipart`
If you see an upload error, install:
```bash
pip install python-multipart
```

### 2. Returning raw NumPy types
FastAPI can fail to serialize `numpy.float32`. Convert confidence values using `float(confidence)`.

### 3. Missing batch dimension
Most models expect `(1, height, width, channels)`, not `(height, width, channels)`.

### 4. Not converting to RGB
Images may arrive in grayscale or RGBA. Use `.convert("RGB")` to keep input shape consistent.

### 5. No error handling for invalid uploads
Without `try/except`, broken files can crash the endpoint instead of returning a helpful `400` response.

---

## Design Decisions
- **Mock mode first** keeps Week 04 achievable before training a real model.
- **Pydantic response model** improves API documentation and consistency.
- **Dictionary-based disease metadata** makes the response reusable by Android and future web clients.
- **Separate preprocessing function** makes testing easier and prepares students for ML integration later.

---

## Next Improvement
After Week 04, replace `run_prediction()` with a real TensorFlow or Keras model loaded from disk, then connect this endpoint to the Android app using Retrofit in Week 05.
