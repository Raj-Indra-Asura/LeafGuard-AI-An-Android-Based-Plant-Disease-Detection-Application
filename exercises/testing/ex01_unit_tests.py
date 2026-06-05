#!/usr/bin/env python3
"""
Testing Exercises — Unit and Integration Tests for LeafGuard AI Backend
Week 8 — Testing & Debugging

Run with:  python -m pytest ex01_unit_tests.py -v
       or: python ex01_unit_tests.py

Prerequisites: pip install pytest fastapi httpx Pillow numpy
"""

import pytest
import io
import json
import numpy as np
from PIL import Image
from unittest.mock import patch, MagicMock


# ══════════════════════════════════════════════════════════════
# CODE UNDER TEST
# These helpers are extracted from the FastAPI backend exercises.
# In a real project these would be imported: from app import preprocess_image
# ══════════════════════════════════════════════════════════════

def preprocess_image(image: Image.Image, target_size=(224, 224)) -> np.ndarray:
    """Reference implementation (same as exercises/backend/ex03_model_integration.py)."""
    image = image.convert("RGB")
    image = image.resize(target_size, Image.LANCZOS)
    img_array = np.array(image, dtype=np.float32)
    img_array /= 255.0
    return np.expand_dims(img_array, axis=0)


CLASS_NAMES = [
    "Apple___Apple_scab", "Apple___Black_rot", "Apple___Cedar_apple_rust", "Apple___healthy",
    "Blueberry___healthy", "Cherry_(including_sour)___Powdery_mildew", "Cherry_(including_sour)___healthy",
    "Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot", "Corn_(maize)___Common_rust_",
    "Corn_(maize)___Northern_Leaf_Blight", "Corn_(maize)___healthy", "Grape___Black_rot",
    "Grape___Esca_(Black_Measles)", "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)", "Grape___healthy",
    "Orange___Haunglongbing_(Citrus_greening)", "Peach___Bacterial_spot", "Peach___healthy",
    "Pepper,_bell___Bacterial_spot", "Pepper,_bell___healthy", "Potato___Early_blight",
    "Potato___Late_blight", "Potato___healthy", "Raspberry___healthy", "Soybean___healthy",
    "Squash___Powdery_mildew", "Strawberry___Leaf_scorch", "Strawberry___healthy",
    "Tomato___Bacterial_spot", "Tomato___Early_blight", "Tomato___Late_blight",
    "Tomato___Leaf_Mold", "Tomato___Septoria_leaf_spot",
    "Tomato___Spider_mites Two-spotted_spider_mite", "Tomato___Target_Spot",
    "Tomato___Tomato_Yellow_Leaf_Curl_Virus", "Tomato___Tomato_mosaic_virus", "Tomato___healthy"
]


def decode_prediction(probabilities: np.ndarray, class_names: list) -> dict:
    """Reference decode implementation."""
    top_idx = int(np.argmax(probabilities))
    confidence = float(probabilities[top_idx])
    class_name = class_names[top_idx]
    plant, disease = class_name.split("___") if "___" in class_name else (class_name, "Unknown")
    return {
        "class_name": class_name,
        "confidence": confidence,
        "is_healthy": "healthy" in class_name.lower(),
        "plant_type": plant,
        "disease": disease
    }


def make_jpeg_bytes(width=400, height=300, color=(80, 150, 60)) -> bytes:
    """Helper: create a synthetic JPEG image in memory."""
    img = Image.new("RGB", (width, height), color=color)
    buffer = io.BytesIO()
    img.save(buffer, format="JPEG")
    return buffer.getvalue()


# ══════════════════════════════════════════════════════════════
# UNIT TEST CLASS 1: Image Preprocessing
# ══════════════════════════════════════════════════════════════

class TestImagePreprocessing:
    """
    Unit tests for the image preprocessing pipeline.
    Tests: output shape, dtype, value range, mode conversion.
    """

    def test_output_shape_is_correct(self):
        """Preprocessed image should have shape (1, 224, 224, 3)."""
        image = Image.new("RGB", (512, 400), color=(100, 150, 80))
        result = preprocess_image(image)
        assert result.shape == (1, 224, 224, 3), f"Expected (1,224,224,3), got {result.shape}"

    def test_output_dtype_is_float32(self):
        """Output should be float32 for TFLite compatibility."""
        image = Image.new("RGB", (300, 300), color=(100, 100, 100))
        result = preprocess_image(image)
        assert result.dtype == np.float32, f"Expected float32, got {result.dtype}"

    def test_values_normalised_to_0_1(self):
        """All pixel values should be in [0.0, 1.0] after normalisation."""
        # Max brightness image
        image = Image.new("RGB", (100, 100), color=(255, 255, 255))
        result = preprocess_image(image)
        assert result.max() <= 1.0, f"Max value {result.max()} exceeds 1.0"
        assert result.min() >= 0.0, f"Min value {result.min()} is negative"

    def test_rgba_converted_to_rgb(self):
        """RGBA images (e.g., PNG with transparency) should be converted to RGB."""
        image = Image.new("RGBA", (200, 200), color=(100, 150, 80, 200))
        result = preprocess_image(image)
        assert result.shape == (1, 224, 224, 3), "RGBA should produce 3-channel output"

    def test_non_square_image_resized(self):
        """Rectangular images should be resized to 224×224 without error."""
        image = Image.new("RGB", (800, 300), color=(200, 100, 50))
        result = preprocess_image(image)
        assert result.shape == (1, 224, 224, 3), f"Non-square should resize to 224x224"

    def test_small_image_upscaled(self):
        """Images smaller than 224x224 should be upscaled correctly."""
        image = Image.new("RGB", (50, 50), color=(50, 100, 150))
        result = preprocess_image(image)
        assert result.shape == (1, 224, 224, 3)

    def test_custom_target_size(self):
        """Should respect custom target_size parameter."""
        image = Image.new("RGB", (300, 300), color=(100, 100, 100))
        result = preprocess_image(image, target_size=(128, 128))
        assert result.shape == (1, 128, 128, 3)

    def test_black_image_values_near_zero(self):
        """Black image should produce values near 0."""
        image = Image.new("RGB", (224, 224), color=(0, 0, 0))
        result = preprocess_image(image)
        assert result.max() < 0.01, f"Black image should be near 0, got max {result.max()}"

    def test_white_image_values_near_one(self):
        """White image should produce values near 1."""
        image = Image.new("RGB", (224, 224), color=(255, 255, 255))
        result = preprocess_image(image)
        assert result.min() > 0.99, f"White image should be near 1, got min {result.min()}"


# ══════════════════════════════════════════════════════════════
# UNIT TEST CLASS 2: Decode Prediction
# ══════════════════════════════════════════════════════════════

class TestDecodePrediction:
    """
    Unit tests for prediction decoding.
    Tests: correct class selection, healthy detection, confidence values.
    """

    def test_returns_highest_confidence_class(self):
        """Should return the class with the highest probability."""
        probs = np.zeros(38)
        probs[29] = 0.91  # Tomato Early Blight
        result = decode_prediction(probs, CLASS_NAMES)
        assert result["class_name"] == "Tomato___Early_blight"

    def test_confidence_value_correct(self):
        """Confidence should match the max probability."""
        probs = np.zeros(38)
        probs[3] = 0.98  # Apple healthy
        result = decode_prediction(probs, CLASS_NAMES)
        assert abs(result["confidence"] - 0.98) < 1e-5

    def test_healthy_flag_true_for_healthy_class(self):
        """is_healthy should be True for classes containing 'healthy'."""
        probs = np.zeros(38)
        probs[37] = 0.95  # Tomato healthy
        result = decode_prediction(probs, CLASS_NAMES)
        assert result["is_healthy"] == True

    def test_healthy_flag_false_for_disease_class(self):
        """is_healthy should be False for disease classes."""
        probs = np.zeros(38)
        probs[29] = 0.88  # Tomato Early Blight
        result = decode_prediction(probs, CLASS_NAMES)
        assert result["is_healthy"] == False

    def test_plant_type_extracted(self):
        """plant_type should be the crop name (part before ___)."""
        probs = np.zeros(38)
        probs[20] = 0.80  # Potato Early Blight
        result = decode_prediction(probs, CLASS_NAMES)
        assert result["plant_type"] == "Potato"

    def test_disease_name_extracted(self):
        """disease should be the condition name (part after ___)."""
        probs = np.zeros(38)
        probs[20] = 0.80  # Potato Early Blight
        result = decode_prediction(probs, CLASS_NAMES)
        assert result["disease"] == "Early_blight"

    def test_works_with_uniform_probabilities(self):
        """Should handle uniform probabilities (returns first max)."""
        probs = np.ones(38) / 38
        result = decode_prediction(probs, CLASS_NAMES)
        assert "class_name" in result
        assert "confidence" in result


# ══════════════════════════════════════════════════════════════
# INTEGRATION TESTS: FastAPI endpoints
# ══════════════════════════════════════════════════════════════

class TestAPIEndpoints:
    """
    Integration tests using FastAPI TestClient.
    These tests make actual HTTP requests to the app (without starting a server).
    """

    @pytest.fixture(autouse=True)
    def setup_client(self):
        """Create a test client for each test."""
        try:
            from fastapi.testclient import TestClient
            from fastapi import FastAPI, UploadFile, File

            # Minimal app for testing
            app = FastAPI()

            @app.get("/health")
            def health():
                return {"status": "healthy"}

            @app.post("/predict")
            async def predict(file: UploadFile = File(...)):
                if file.content_type not in {"image/jpeg", "image/png"}:
                    from fastapi import HTTPException
                    raise HTTPException(400, "Invalid file type")
                contents = await file.read()
                return {"success": True, "size_bytes": len(contents)}

            self.client = TestClient(app)
        except ImportError:
            pytest.skip("FastAPI/httpx not installed")

    def test_health_endpoint_returns_200(self):
        response = self.client.get("/health")
        assert response.status_code == 200

    def test_health_response_has_status_field(self):
        response = self.client.get("/health")
        data = response.json()
        assert "status" in data

    def test_predict_with_valid_jpeg(self):
        jpeg_bytes = make_jpeg_bytes()
        response = self.client.post(
            "/predict",
            files={"file": ("leaf.jpg", jpeg_bytes, "image/jpeg")}
        )
        assert response.status_code == 200

    def test_predict_with_invalid_type_returns_400(self):
        pdf_bytes = b"%PDF-fake"
        response = self.client.post(
            "/predict",
            files={"file": ("doc.pdf", pdf_bytes, "application/pdf")}
        )
        assert response.status_code == 400

    def test_predict_returns_success_field(self):
        jpeg_bytes = make_jpeg_bytes()
        response = self.client.post(
            "/predict",
            files={"file": ("leaf.jpg", jpeg_bytes, "image/jpeg")}
        )
        data = response.json()
        assert "success" in data

    def test_predict_no_file_returns_422(self):
        """Missing required file should return 422 Unprocessable Entity."""
        response = self.client.post("/predict")
        assert response.status_code == 422


# ══════════════════════════════════════════════════════════════
# MAIN
# ══════════════════════════════════════════════════════════════

if __name__ == "__main__":
    import subprocess
    import sys

    print("Running tests with pytest...")
    result = subprocess.run(
        [sys.executable, "-m", "pytest", __file__, "-v", "--tb=short"],
        capture_output=False
    )
    sys.exit(result.returncode)
