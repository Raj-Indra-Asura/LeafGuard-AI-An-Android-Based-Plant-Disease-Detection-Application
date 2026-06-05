#!/usr/bin/env python3
"""
Exercise 3: ML Model Integration
Week 6 — Cloud ML Model

Integrates a real TensorFlow/Keras model into the FastAPI backend.

Prerequisites:
  pip install tensorflow Pillow numpy fastapi uvicorn python-multipart

Run with:  python ex03_model_integration.py
"""

from fastapi import FastAPI, UploadFile, File, HTTPException
from PIL import Image
import numpy as np
import io
import time
import os

app = FastAPI(title="LeafGuard AI API — Exercise 3: ML Integration")

# ──────────────────────────────────────────────
# GLOBAL MODEL STATE
# Loaded once at startup, reused for all requests
# ──────────────────────────────────────────────
model = None
class_names = []

# Path to class names file (relative to repo root, run from exercises/backend/)
LABELS_PATH = os.path.join("..", "..", "model", "labels.txt")
MODEL_PATH = os.path.join("..", "..", "model", "plant_disease.tflite")


def load_class_names(path: str) -> list:
    """Load class names from labels.txt file."""
    try:
        with open(path, "r") as f:
            names = [line.strip() for line in f if line.strip()]
        print(f"Loaded {len(names)} class names from {path}")
        return names
    except FileNotFoundError:
        print(f"labels.txt not found at {path}. Using placeholder names.")
        return [f"Class_{i}" for i in range(38)]


def load_model(path: str):
    """
    Load the TFLite model.
    In production, prefer TFLite over full TF for faster cold start.
    """
    try:
        import tensorflow as tf
        interpreter = tf.lite.Interpreter(model_path=path)
        interpreter.allocate_tensors()
        print(f"TFLite model loaded from {path}")
        return interpreter
    except FileNotFoundError:
        print(f"Model not found at {path}. Using mock model.")
        return None
    except ImportError:
        print("TensorFlow not installed. Using mock model.")
        return None


def preprocess_image(image: Image.Image, target_size=(224, 224)) -> np.ndarray:
    """
    Preprocess PIL Image for CNN inference.

    Returns: (1, 224, 224, 3) float32 tensor, normalised to [0, 1]
    """
    # Ensure RGB (handles RGBA, grayscale, etc.)
    image = image.convert("RGB")

    # Resize to model input size using high-quality resampling
    image = image.resize(target_size, Image.LANCZOS)

    # Convert to float32 array
    img_array = np.array(image, dtype=np.float32)

    # Normalise: [0, 255] → [0.0, 1.0]
    img_array /= 255.0

    # Add batch dimension: (H, W, 3) → (1, H, W, 3)
    img_array = np.expand_dims(img_array, axis=0)

    return img_array


def run_inference(input_tensor: np.ndarray, interpreter, class_names: list) -> dict:
    """
    Run TFLite inference and decode the output.

    Returns dict with top prediction and confidence scores.
    """
    if interpreter is None:
        # Mock inference when model is unavailable
        import random
        idx = 29  # Tomato Early Blight
        return {
            "disease": class_names[idx] if class_names else "MOCK_DISEASE",
            "confidence": 0.91,
            "is_mock": True
        }

    # Get tensor indices
    input_details = interpreter.get_input_details()[0]
    output_details = interpreter.get_output_details()[0]

    # Set input tensor
    interpreter.set_tensor(input_details["index"], input_tensor)

    # Run inference
    interpreter.invoke()

    # Get output
    output = interpreter.get_tensor(output_details["index"])[0]  # shape: (38,)

    # Decode: find highest confidence class
    top_idx = int(np.argmax(output))
    confidence = float(output[top_idx])

    # Top 5 predictions
    top5_idx = np.argsort(output)[::-1][:5]
    top5 = [
        {"rank": i + 1, "class": class_names[idx], "confidence": float(output[idx])}
        for i, idx in enumerate(top5_idx)
    ]

    return {
        "disease": class_names[top_idx],
        "confidence": confidence,
        "is_healthy": "healthy" in class_names[top_idx].lower(),
        "top_5": top5,
        "is_mock": False
    }


@app.on_event("startup")
async def startup_event():
    """Load model and class names when server starts."""
    global model, class_names
    class_names = load_class_names(LABELS_PATH)
    model = load_model(MODEL_PATH)
    status = "loaded" if model else "mock mode (model file not found)"
    print(f"LeafGuard AI backend ready. Model: {status}")


@app.get("/health")
async def health():
    return {
        "status": "healthy",
        "model_loaded": model is not None,
        "num_classes": len(class_names)
    }


@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    """Predict plant disease from uploaded leaf image."""
    start = time.time()

    # Validate
    if file.content_type not in {"image/jpeg", "image/png", "image/webp"}:
        raise HTTPException(400, f"Invalid file type: {file.content_type}")

    contents = await file.read()

    if len(contents) > 10 * 1024 * 1024:
        raise HTTPException(413, "File too large. Maximum 10 MB.")

    # Decode
    try:
        image = Image.open(io.BytesIO(contents)).convert("RGB")
    except Exception:
        raise HTTPException(400, "Cannot decode image file.")

    # Preprocess
    input_tensor = preprocess_image(image)

    # Inference
    result = run_inference(input_tensor, model, class_names)

    processing_ms = (time.time() - start) * 1000

    return {
        "success": True,
        "prediction": result,
        "image_size": f"{image.width}×{image.height}",
        "processing_time_ms": round(processing_ms, 1)
    }


# TODO Exercise 3A: Add confidence threshold parameter to /predict
# If confidence < threshold, return a warning asking for a clearer photo

# TODO Exercise 3B: Add GET /disease/{name} endpoint
# Returns detailed info about a specific disease from a hardcoded dictionary

# TODO Exercise 3C: Add request logging middleware
# Log: timestamp, endpoint, file_size_kb, disease, confidence, time_ms

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000, reload=True)
