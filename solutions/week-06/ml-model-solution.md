# Week 06 Solution - Real PlantVillage Model Integration for Cloud Prediction

This solution upgrades the backend from mock prediction to a real PlantVillage-compatible classifier.

It includes:
- how to obtain a PlantVillage model legally and reproducibly
- a Python verification/download helper
- a SavedModel → TFLite conversion script
- a real `model_loader.py`
- a real `main.py` cloud prediction endpoint
- the full 38-class PlantVillage label list
- `labels.txt` content
- testing instructions with `curl` and Postman

---

## 1. How to acquire the model

There are two clean ways to obtain a usable model.

### Option A - Export your own model from Teachable Machine
1. Go to **https://teachablemachine.withgoogle.com/**.
2. Create an image classification project.
3. Use PlantVillage images grouped into the same 38 classes listed below.
4. Train the model.
5. Export as **TensorFlow SavedModel**.
6. Place the exported folder inside:

```text
backend-api/models/plantvillage_saved_model/
```

### Option B - Train from PlantVillage data
1. Download the PlantVillage dataset from Kaggle:
   - https://www.kaggle.com/datasets/emmarex/plantdisease
2. Train a TensorFlow/Keras model using MobileNetV2 or EfficientNetB0.
3. Save the trained model as a TensorFlow SavedModel.
4. Copy the SavedModel folder to:

```text
backend-api/models/plantvillage_saved_model/
```

### Important note
Kaggle does not provide unauthenticated direct downloads, so the safest reproducible workflow is:
- student downloads/authenticates with Kaggle manually
- backend scripts verify the model structure after it is placed in the repo

---

## 2. Directory layout for Week 06

```text
backend-api/
├── main.py
├── model_loader.py
├── scripts/
│   ├── verify_or_prepare_model.py
│   └── convert_saved_model_to_tflite.py
└── models/
    ├── plantvillage_saved_model/
    ├── plantvillage_float32.tflite
    ├── plantvillage_int8.tflite
    └── labels.txt
```

---

## 3. `scripts/verify_or_prepare_model.py`

```python
from pathlib import Path
import hashlib
import json

import tensorflow as tf

PROJECT_ROOT = Path(__file__).resolve().parents[1]
MODEL_DIR = PROJECT_ROOT / "models" / "plantvillage_saved_model"
LABELS_FILE = PROJECT_ROOT / "models" / "labels.txt"


def sha256_of_file(file_path: Path) -> str:
    digest = hashlib.sha256()
    with file_path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(8192), b""):
            digest.update(chunk)
    return digest.hexdigest()


def inspect_saved_model(model_dir: Path) -> dict:
    model = tf.keras.models.load_model(model_dir)
    summary_lines = []
    model.summary(print_fn=summary_lines.append)
    return {
        "input_shape": model.input_shape,
        "output_shape": model.output_shape,
        "parameter_count": model.count_params(),
        "summary": summary_lines,
    }


def main() -> None:
    if not MODEL_DIR.exists():
        raise FileNotFoundError(
            f"SavedModel not found at {MODEL_DIR}. Export it from Teachable Machine or train from Kaggle PlantVillage first."
        )

    if not LABELS_FILE.exists():
        raise FileNotFoundError(f"labels.txt not found at {LABELS_FILE}")

    info = inspect_saved_model(MODEL_DIR)
    labels = [line.strip() for line in LABELS_FILE.read_text(encoding="utf-8").splitlines() if line.strip()]

    report = {
        "model_dir": str(MODEL_DIR),
        "labels_file": str(LABELS_FILE),
        "label_count": len(labels),
        "input_shape": info["input_shape"],
        "output_shape": info["output_shape"],
        "parameter_count": info["parameter_count"],
        "saved_model_fingerprint": sha256_of_file(MODEL_DIR / "saved_model.pb"),
        "first_five_labels": labels[:5],
        "last_five_labels": labels[-5:],
    }

    print("Model loaded successfully")
    print(json.dumps(report, indent=2, default=str))
    print("
Model summary:")
    for line in info["summary"]:
        print(line)

    if info["output_shape"][-1] != len(labels):
        raise ValueError(
            f"Output classes ({info['output_shape'][-1]}) do not match label count ({len(labels)})"
        )

    print("
Verification passed.")


if __name__ == "__main__":
    main()
```

---

## 4. `scripts/convert_saved_model_to_tflite.py`

```python
from pathlib import Path

import numpy as np
import tensorflow as tf

PROJECT_ROOT = Path(__file__).resolve().parents[1]
SAVED_MODEL_DIR = PROJECT_ROOT / "models" / "plantvillage_saved_model"
FLOAT32_OUTPUT = PROJECT_ROOT / "models" / "plantvillage_float32.tflite"
INT8_OUTPUT = PROJECT_ROOT / "models" / "plantvillage_int8.tflite"
IMAGE_SIZE = 224


def representative_dataset():
    for _ in range(100):
        yield [np.random.rand(1, IMAGE_SIZE, IMAGE_SIZE, 3).astype(np.float32)]


def convert_float32() -> None:
    converter = tf.lite.TFLiteConverter.from_saved_model(str(SAVED_MODEL_DIR))
    converter.optimizations = []
    tflite_model = converter.convert()
    FLOAT32_OUTPUT.write_bytes(tflite_model)
    print(f"Wrote {FLOAT32_OUTPUT}")


def convert_int8() -> None:
    converter = tf.lite.TFLiteConverter.from_saved_model(str(SAVED_MODEL_DIR))
    converter.optimizations = [tf.lite.Optimize.DEFAULT]
    converter.representative_dataset = representative_dataset
    converter.target_spec.supported_ops = [tf.lite.OpsSet.TFLITE_BUILTINS_INT8]
    converter.inference_input_type = tf.uint8
    converter.inference_output_type = tf.uint8
    tflite_model = converter.convert()
    INT8_OUTPUT.write_bytes(tflite_model)
    print(f"Wrote {INT8_OUTPUT}")


def main() -> None:
    if not SAVED_MODEL_DIR.exists():
        raise FileNotFoundError(f"SavedModel missing: {SAVED_MODEL_DIR}")

    convert_float32()
    convert_int8()
    print("Conversion finished successfully.")


if __name__ == "__main__":
    main()
```

---

## 5. Full 38-class PlantVillage label order

This order must match the model output layer and `labels.txt` exactly.

```python
DISEASE_CLASSES = [
    "Apple___Apple_scab",
    "Apple___Black_rot",
    "Apple___Cedar_apple_rust",
    "Apple___healthy",
    "Blueberry___healthy",
    "Cherry___Powdery_mildew",
    "Cherry___healthy",
    "Corn___Cercospora_leaf_spot Gray_leaf_spot",
    "Corn___Common_rust",
    "Corn___Northern_Leaf_Blight",
    "Corn___healthy",
    "Grape___Black_rot",
    "Grape___Esca_(Black_Measles)",
    "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)",
    "Grape___healthy",
    "Orange___Haunglongbing_(Citrus_greening)",
    "Peach___Bacterial_spot",
    "Peach___healthy",
    "Pepper,_bell___Bacterial_spot",
    "Pepper,_bell___healthy",
    "Potato___Early_blight",
    "Potato___Late_blight",
    "Potato___healthy",
    "Raspberry___healthy",
    "Soybean___healthy",
    "Squash___Powdery_mildew",
    "Strawberry___Leaf_scorch",
    "Strawberry___healthy",
    "Tomato___Bacterial_spot",
    "Tomato___Early_blight",
    "Tomato___Late_blight",
    "Tomato___Leaf_Mold",
    "Tomato___Septoria_leaf_spot",
    "Tomato___Spider_mites Two-spotted_spider_mite",
    "Tomato___Target_Spot",
    "Tomato___Tomato_Yellow_Leaf_Curl_Virus",
    "Tomato___Tomato_mosaic_virus",
    "Tomato___healthy",
]
```

---

## 6. `backend-api/models/labels.txt`

```txt
Apple___Apple_scab
Apple___Black_rot
Apple___Cedar_apple_rust
Apple___healthy
Blueberry___healthy
Cherry___Powdery_mildew
Cherry___healthy
Corn___Cercospora_leaf_spot Gray_leaf_spot
Corn___Common_rust
Corn___Northern_Leaf_Blight
Corn___healthy
Grape___Black_rot
Grape___Esca_(Black_Measles)
Grape___Leaf_blight_(Isariopsis_Leaf_Spot)
Grape___healthy
Orange___Haunglongbing_(Citrus_greening)
Peach___Bacterial_spot
Peach___healthy
Pepper,_bell___Bacterial_spot
Pepper,_bell___healthy
Potato___Early_blight
Potato___Late_blight
Potato___healthy
Raspberry___healthy
Soybean___healthy
Squash___Powdery_mildew
Strawberry___Leaf_scorch
Strawberry___healthy
Tomato___Bacterial_spot
Tomato___Early_blight
Tomato___Late_blight
Tomato___Leaf_Mold
Tomato___Septoria_leaf_spot
Tomato___Spider_mites Two-spotted_spider_mite
Tomato___Target_Spot
Tomato___Tomato_Yellow_Leaf_Curl_Virus
Tomato___Tomato_mosaic_virus
Tomato___healthy
```

---

## 7. Complete `backend-api/model_loader.py`

```python
from io import BytesIO
from pathlib import Path
from typing import List, Tuple

import numpy as np
from PIL import Image
import tensorflow as tf

MODEL_DIR = Path(__file__).resolve().parent / "models"
SAVED_MODEL_DIR = MODEL_DIR / "plantvillage_saved_model"
LABELS_PATH = MODEL_DIR / "labels.txt"
IMAGE_SIZE = 224

DISEASE_CLASSES: List[str] = [
    "Apple___Apple_scab",
    "Apple___Black_rot",
    "Apple___Cedar_apple_rust",
    "Apple___healthy",
    "Blueberry___healthy",
    "Cherry___Powdery_mildew",
    "Cherry___healthy",
    "Corn___Cercospora_leaf_spot Gray_leaf_spot",
    "Corn___Common_rust",
    "Corn___Northern_Leaf_Blight",
    "Corn___healthy",
    "Grape___Black_rot",
    "Grape___Esca_(Black_Measles)",
    "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)",
    "Grape___healthy",
    "Orange___Haunglongbing_(Citrus_greening)",
    "Peach___Bacterial_spot",
    "Peach___healthy",
    "Pepper,_bell___Bacterial_spot",
    "Pepper,_bell___healthy",
    "Potato___Early_blight",
    "Potato___Late_blight",
    "Potato___healthy",
    "Raspberry___healthy",
    "Soybean___healthy",
    "Squash___Powdery_mildew",
    "Strawberry___Leaf_scorch",
    "Strawberry___healthy",
    "Tomato___Bacterial_spot",
    "Tomato___Early_blight",
    "Tomato___Late_blight",
    "Tomato___Leaf_Mold",
    "Tomato___Septoria_leaf_spot",
    "Tomato___Spider_mites Two-spotted_spider_mite",
    "Tomato___Target_Spot",
    "Tomato___Tomato_Yellow_Leaf_Curl_Virus",
    "Tomato___Tomato_mosaic_virus",
    "Tomato___healthy",
]


def load_labels() -> List[str]:
    if LABELS_PATH.exists():
        labels = [line.strip() for line in LABELS_PATH.read_text(encoding="utf-8").splitlines() if line.strip()]
        if labels:
            return labels
    return DISEASE_CLASSES


class PlantDiseaseModel:
    def __init__(self) -> None:
        if not SAVED_MODEL_DIR.exists():
            raise FileNotFoundError(f"SavedModel directory not found: {SAVED_MODEL_DIR}")
        self.model = tf.keras.models.load_model(SAVED_MODEL_DIR)
        self.labels = load_labels()

    @staticmethod
    def preprocess_image(image_bytes: bytes) -> np.ndarray:
        image = Image.open(BytesIO(image_bytes))
        if image.mode == "RGBA":
            image = image.convert("RGB")
        elif image.mode == "L":
            image = image.convert("RGB")
        else:
            image = image.convert("RGB")

        image = image.resize((IMAGE_SIZE, IMAGE_SIZE), Image.Resampling.BILINEAR)
        image_array = np.asarray(image, dtype=np.float32) / 255.0
        image_array = np.expand_dims(image_array, axis=0)
        return image_array

    def predict(self, image_bytes: bytes) -> Tuple[str, float, np.ndarray]:
        model_input = self.preprocess_image(image_bytes)
        predictions = self.model.predict(model_input, verbose=0)
        predictions = np.asarray(predictions, dtype=np.float32)
        scores = predictions[0]
        best_index = int(np.argmax(scores))
        confidence = float(scores[best_index])
        disease = self.labels[best_index]
        return disease, confidence, scores
```

---

## 8. Complete `backend-api/main.py` for cloud prediction

```python
from typing import Dict

from fastapi import FastAPI, File, HTTPException, UploadFile
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

from model_loader import PlantDiseaseModel

app = FastAPI(title="LeafGuard AI Backend", version="2.0.0")
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

model = PlantDiseaseModel()

DISEASE_INFO: Dict[str, Dict[str, str]] = {
    "Tomato___Early_blight": {
        "symptoms": "Brown concentric spots on older leaves.",
        "treatment": "Remove infected leaves and use a labeled fungicide.",
        "prevention": "Avoid overhead irrigation and rotate crops.",
    },
    "Tomato___Late_blight": {
        "symptoms": "Water-soaked lesions that darken rapidly.",
        "treatment": "Remove infected foliage and isolate affected plants.",
        "prevention": "Use resistant varieties and avoid prolonged leaf wetness.",
    },
    "Tomato___healthy": {
        "symptoms": "Leaves are green and free from lesions.",
        "treatment": "No treatment required.",
        "prevention": "Continue balanced irrigation and monitoring.",
    },
    "Potato___Early_blight": {
        "symptoms": "Target-like brown leaf spots on older leaves.",
        "treatment": "Prune infected leaves and use fungicide if required.",
        "prevention": "Rotate crops and keep foliage dry.",
    },
    "Potato___Late_blight": {
        "symptoms": "Dark rapidly expanding lesions with possible white growth.",
        "treatment": "Remove infected tissue quickly and protect nearby plants.",
        "prevention": "Destroy volunteer plants and reduce humidity around leaves.",
    },
}


class PredictionResult(BaseModel):
    disease: str
    confidence: float
    symptoms: str
    treatment: str
    prevention: str


@app.get("/")
def health_check() -> dict:
    return {
        "status": "ok",
        "model_loaded": True,
        "class_count": len(model.labels),
        "image_size": 224,
    }


@app.get("/labels")
def list_labels() -> dict:
    return {"labels": model.labels, "count": len(model.labels)}


@app.post("/predict", response_model=PredictionResult)
async def predict(image: UploadFile = File(...)) -> PredictionResult:
    if not image.content_type or not image.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="Uploaded file must be an image.")

    image_bytes = await image.read()
    if not image_bytes:
        raise HTTPException(status_code=400, detail="Uploaded file is empty.")

    disease, confidence, _ = model.predict(image_bytes)
    metadata = DISEASE_INFO.get(
        disease,
        {
            "symptoms": "See the disease library entry for a detailed description.",
            "treatment": "Consult a plant pathology guide before treatment.",
            "prevention": "Capture another image in good light to verify the result.",
        },
    )

    return PredictionResult(
        disease=disease,
        confidence=round(confidence, 4),
        symptoms=metadata["symptoms"],
        treatment=metadata["treatment"],
        prevention=metadata["prevention"],
    )
```

---

## 9. Optional helper: `backend-api/models/README-model-steps.txt`

```text
1. Put your TensorFlow SavedModel folder in backend-api/models/plantvillage_saved_model/
2. Put labels.txt in backend-api/models/labels.txt
3. Run: python3 scripts/verify_or_prepare_model.py
4. Run: python3 scripts/convert_saved_model_to_tflite.py
5. Start API: uvicorn main:app --reload
6. Test: curl -X POST http://127.0.0.1:8000/predict -F "image=@../sample-images/tomato-leaf.jpg"
```

---

## 10. `curl` test commands

### Health check
```bash
curl http://127.0.0.1:8000/
```

### Labels check
```bash
curl http://127.0.0.1:8000/labels
```

### Prediction test
```bash
curl -X POST http://127.0.0.1:8000/predict   -H "accept: application/json"   -F "image=@sample-images/tomato-leaf.jpg"
```

### Expected JSON shape
```json
{
  "disease": "Tomato___Early_blight",
  "confidence": 0.9123,
  "symptoms": "Brown concentric spots on older leaves.",
  "treatment": "Remove infected leaves and use a labeled fungicide.",
  "prevention": "Avoid overhead irrigation and rotate crops."
}
```

---

## 11. Postman testing steps

1. Create a new **POST** request.
2. URL: `http://127.0.0.1:8000/predict`
3. Open **Body** → **form-data**.
4. Add key `image` and set type to **File**.
5. Select a leaf image.
6. Click **Send**.
7. Verify disease name and confidence are returned.

---

## 12. Why this solution is real and usable

- the backend loads a real TensorFlow SavedModel
- preprocessing converts any input to RGB
- the image is resized to `224x224`
- normalization uses `[0, 1]`
- the output index maps to the 38 PlantVillage labels
- the same label order can be reused later for Android TFLite

---

## 13. Week 06 checklist

- [x] model acquisition guide included
- [x] verification helper script included
- [x] SavedModel to TFLite conversion script included
- [x] complete `model_loader.py` included
- [x] complete `main.py` included
- [x] 38 PlantVillage classes included
- [x] `labels.txt` content included
- [x] curl/Postman tests included


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

- 📝 [Back to Week 06 Exercises](../../roadmap/week-06-cloud-ml-model/exercises.md) — Try it yourself first
- 📖 [Week 06 README](../../roadmap/week-06-cloud-ml-model/README.md) — Week overview
- 💡 [Solutions Index for Week 06](README.md) — Other solutions this week
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

---
