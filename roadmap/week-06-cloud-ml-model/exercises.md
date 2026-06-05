# Week 06 Exercises: Real ML Model Integration

Complete these exercises to build proficiency with machine learning model integration. Each exercise builds on concepts from the learning notes and prepares you for the build task.

## Exercise 1: Model Information Inspector

**Objective**: Learn to extract and verify model metadata before integration.

**Task**: Create a Python script that loads a model and prints detailed information about its structure.

**Steps**:
1. Create a file named `model_inspector.py` in your Flask project directory
2. Import TensorFlow (or PyTorch if using that framework)
3. Load your plant disease model
4. Print the following information:
   - Input shape (expected image dimensions)
   - Output shape (number of classes)
   - Model summary (layer architecture)
   - Total number of parameters
5. Run the script and save the output

**Expected output example**:
```
Model loaded successfully
Input shape: (None, 224, 224, 3)
Output shape: (None, 6)
Number of classes: 6
Total parameters: 1,234,567

Model Summary:
Layer (type)         Output Shape         Param #
==================================================
...
```

**Why this matters**: Understanding model structure prevents runtime errors and helps you write correct preprocessing code.

**Starter code**:
```python
import tensorflow as tf

def inspect_model(model_path):
    print(f"Loading model from {model_path}...")
    model = tf.keras.models.load_model(model_path)

    print("\nModel Information:")
    print(f"Input shape: {model.input_shape}")
    print(f"Output shape: {model.output_shape}")
    print(f"Number of classes: {model.output_shape[-1]}")

    print("\nModel Summary:")
    model.summary()

    return model

if __name__ == "__main__":
    model = inspect_model("plant_disease_model.h5")
```

**Validation**: Your script should run without errors and output should match your model's actual structure. Verify input shape matches what you expect (e.g., 224×224×3).

---

## Exercise 2: Image Preprocessing Function

**Objective**: Implement and test a robust image preprocessing pipeline.

**Task**: Create a function that takes raw image bytes and returns a model-ready array, then test it with various image formats.

**Steps**:
1. Create `preprocess.py` in your Flask project
2. Implement `preprocess_image(image_bytes, target_size=(224, 224))` function
3. Handle RGBA to RGB conversion (4 channels → 3 channels)
4. Implement normalization to [0, 1] range
5. Add batch dimension
6. Test with JPG, PNG, and RGBA PNG images
7. Print preprocessed array properties (shape, min, max)

**Requirements**:
- Function should work with any image format PIL supports
- Output shape must be (1, height, width, 3)
- Pixel values must be in [0, 1] range
- Handle grayscale images by converting to RGB

**Starter code**:
```python
from PIL import Image
import numpy as np
import io

def preprocess_image(image_bytes, target_size=(224, 224)):
    """
    Preprocess image bytes for model inference.

    Args:
        image_bytes: Raw image file bytes
        target_size: Tuple of (height, width) for resizing

    Returns:
        np.array: Preprocessed image array with shape (1, height, width, 3)
    """
    # TODO: Open image from bytes

    # TODO: Convert RGBA to RGB if needed (check image.mode)

    # TODO: Convert grayscale to RGB if needed

    # TODO: Resize to target_size

    # TODO: Convert to numpy array and normalize to [0, 1]

    # TODO: Add batch dimension

    return img_array

# Test function
if __name__ == "__main__":
    # Test with a sample image
    with open("test_leaf.jpg", "rb") as f:
        img_bytes = f.read()

    processed = preprocess_image(img_bytes)

    print(f"Preprocessed shape: {processed.shape}")
    print(f"Value range: [{processed.min():.3f}, {processed.max():.3f}]")
    print(f"Data type: {processed.dtype}")

    # Test assertions
    assert processed.shape[0] == 1, "Missing batch dimension"
    assert processed.shape[-1] == 3, "Should have 3 color channels"
    assert 0 <= processed.min() <= processed.max() <= 1, "Values should be in [0, 1]"

    print("\nAll tests passed!")
```

**Test cases to implement**:
1. JPG image → should process successfully
2. PNG with alpha channel → should convert to RGB
3. Grayscale image → should convert to RGB
4. Very large image (4000×3000) → should resize without memory error
5. Very small image (50×50) → should upscale to target size

**Validation**: Run your function with 5 different test images. All should return shape (1, 224, 224, 3) with values in [0, 1] range.

---

## Exercise 3: Label Mapping Implementation

**Objective**: Create and test label mapping from class indices to disease names.

**Task**: Implement label mapping for your model's classes and validate it with sample predictions.

**Steps**:
1. Create `labels.py` with label mapping for your model
2. Implement function to map class index to disease name
3. Implement function to map class index to recommendation text
4. Test with simulated prediction arrays
5. Handle invalid class indices gracefully

**Approach 1: Hardcoded dictionary (for 6-class model)**:
```python
DISEASE_LABELS = {
    0: "Tomato Healthy",
    1: "Tomato Early Blight",
    2: "Tomato Late Blight",
    3: "Potato Healthy",
    4: "Potato Early Blight",
    5: "Potato Late Blight"
}

RECOMMENDATIONS = {
    "Tomato Healthy": "No action needed. Continue regular care and monitoring.",
    "Tomato Early Blight": "Remove infected leaves. Apply copper-based fungicide. Improve air circulation.",
    "Tomato Late Blight": "Remove and destroy infected plants. Apply fungicide to nearby plants. Avoid overhead watering.",
    "Potato Healthy": "No action needed. Monitor regularly for signs of disease.",
    "Potato Early Blight": "Remove infected foliage. Apply fungicide. Ensure proper spacing for air flow.",
    "Potato Late Blight": "Remove infected plants immediately. Apply fungicide preventatively to healthy plants."
}

def get_disease_name(class_index):
    """Map class index to disease name."""
    return DISEASE_LABELS.get(class_index, "Unknown Disease")

def get_recommendation(disease_name):
    """Get treatment recommendation for disease."""
    return RECOMMENDATIONS.get(disease_name, "Consult an agricultural expert for proper diagnosis and treatment.")
```

**Approach 2: File-based (for larger class counts)**:
```python
def load_labels_from_file(filepath):
    """Load labels from text file (one label per line)."""
    with open(filepath, 'r') as f:
        labels = [line.strip() for line in f.readlines()]
    return labels

# Usage
DISEASE_LABELS = load_labels_from_file('labels.txt')

def get_disease_name(class_index):
    if 0 <= class_index < len(DISEASE_LABELS):
        return DISEASE_LABELS[class_index]
    return "Unknown Disease"
```

**Test code**:
```python
import numpy as np

def test_label_mapping():
    # Simulate model output for 6 classes
    test_predictions = [
        np.array([0.05, 0.80, 0.05, 0.03, 0.04, 0.03]),  # Should predict class 1
        np.array([0.85, 0.05, 0.02, 0.03, 0.02, 0.03]),  # Should predict class 0
        np.array([0.02, 0.03, 0.05, 0.02, 0.03, 0.85]),  # Should predict class 5
    ]

    expected_diseases = [
        "Tomato Early Blight",
        "Tomato Healthy",
        "Potato Late Blight"
    ]

    for i, pred in enumerate(test_predictions):
        class_idx = np.argmax(pred)
        confidence = pred[class_idx]
        disease = get_disease_name(class_idx)
        recommendation = get_recommendation(disease)

        print(f"\nTest {i+1}:")
        print(f"  Class index: {class_idx}")
        print(f"  Confidence: {confidence:.2%}")
        print(f"  Predicted disease: {disease}")
        print(f"  Recommendation: {recommendation[:60]}...")

        assert disease == expected_diseases[i], f"Expected {expected_diseases[i]}, got {disease}"

    print("\nAll label mapping tests passed!")

if __name__ == "__main__":
    test_label_mapping()
```

**Validation**: All test cases should predict correct disease names. Verify recommendations are specific and actionable.

---

## Exercise 4: Minimal Inference Endpoint

**Objective**: Build a basic Flask endpoint that performs real model inference.

**Task**: Create a minimal working `/predict` endpoint before adding error handling and features.

**Steps**:
1. Create `app_minimal.py` (separate from your main app for testing)
2. Load model at startup
3. Implement `/predict` endpoint that accepts image file
4. Perform inference and return JSON with disease and confidence
5. Test with Postman using 3 different images

**Starter code**:
```python
from flask import Flask, request, jsonify
import tensorflow as tf
import numpy as np
from preprocess import preprocess_image  # From Exercise 2
from labels import get_disease_name, get_recommendation  # From Exercise 3

app = Flask(__name__)

# Load model at startup
print("Loading model...")
MODEL = tf.keras.models.load_model('plant_disease_model.h5')
print("Model loaded successfully")

@app.route('/predict', methods=['POST'])
def predict():
    # Get image from request
    image_file = request.files['image']
    image_bytes = image_file.read()

    # Preprocess
    img_array = preprocess_image(image_bytes)

    # Inference
    predictions = MODEL.predict(img_array)

    # Decode
    class_idx = int(np.argmax(predictions[0]))
    confidence = float(predictions[0][class_idx])
    disease_name = get_disease_name(class_idx)

    # Return JSON
    return jsonify({
        "status": "success",
        "prediction": {
            "disease": disease_name,
            "confidence": confidence,
            "recommendation": get_recommendation(disease_name)
        }
    })

if __name__ == '__main__':
    app.run(debug=True, port=5000)
```

**Testing checklist**:
1. Run `python app_minimal.py`
2. Verify model loads without errors
3. Open Postman and create POST request to `http://localhost:5000/predict`
4. Set Body type to `form-data`
5. Add key `image`, change type to `File`, upload test image
6. Send request
7. Verify response has `status: "success"` and `prediction` object
8. Test with 3 different images, verify predictions change

**Expected response**:
```json
{
  "status": "success",
  "prediction": {
    "disease": "Tomato Early Blight",
    "confidence": 0.7834,
    "recommendation": "Remove infected leaves. Apply copper-based fungicide..."
  }
}
```

**Validation**: Successfully receive predictions for all test images with reasonable confidence scores (> 0.3).

---

## Exercise 5: Error Handling Implementation

**Objective**: Make your inference endpoint robust by handling all error cases.

**Task**: Extend Exercise 4's endpoint with comprehensive error handling.

**Steps**:
1. Copy `app_minimal.py` to `app_robust.py`
2. Add validation for missing image in request
3. Add validation for empty filename
4. Handle preprocessing errors (invalid image format)
5. Handle inference errors (out of memory, model errors)
6. Return appropriate HTTP status codes (400 for client errors, 500 for server errors)
7. Test all error cases in Postman

**Error cases to handle**:

1. **No image in request**:
```python
if 'image' not in request.files:
    return jsonify({"status": "error", "message": "No image provided"}), 400
```

2. **Empty filename**:
```python
image_file = request.files['image']
if image_file.filename == '':
    return jsonify({"status": "error", "message": "Empty filename"}), 400
```

3. **Empty file**:
```python
image_bytes = image_file.read()
if len(image_bytes) == 0:
    return jsonify({"status": "error", "message": "Empty file"}), 400
```

4. **Preprocessing errors**:
```python
try:
    img_array = preprocess_image(image_bytes)
except Exception as e:
    return jsonify({"status": "error", "message": f"Invalid image format: {str(e)}"}), 400
```

5. **Inference errors**:
```python
try:
    predictions = MODEL.predict(img_array)
except Exception as e:
    return jsonify({"status": "error", "message": f"Inference failed: {str(e)}"}), 500
```

6. **Catch-all for unexpected errors**:
```python
except Exception as e:
    print(f"Unexpected error: {e}")
    import traceback
    traceback.print_exc()
    return jsonify({"status": "error", "message": "Internal server error"}), 500
```

**Complete implementation**:
```python
@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Validate request has image
        if 'image' not in request.files:
            return jsonify({"status": "error", "message": "No image provided"}), 400

        image_file = request.files['image']

        # Validate filename not empty
        if image_file.filename == '':
            return jsonify({"status": "error", "message": "Empty filename"}), 400

        # Read image bytes
        image_bytes = image_file.read()

        # Validate file not empty
        if len(image_bytes) == 0:
            return jsonify({"status": "error", "message": "Empty file"}), 400

        # Preprocess (may fail for invalid formats)
        try:
            img_array = preprocess_image(image_bytes)
        except Exception as e:
            return jsonify({"status": "error", "message": f"Invalid image format: {str(e)}"}), 400

        # Inference (may fail for out-of-memory or model errors)
        try:
            predictions = MODEL.predict(img_array)
        except Exception as e:
            return jsonify({"status": "error", "message": f"Inference failed: {str(e)}"}), 500

        # Decode output
        class_idx = int(np.argmax(predictions[0]))
        confidence = float(predictions[0][class_idx])
        disease_name = get_disease_name(class_idx)

        # Return success response
        return jsonify({
            "status": "success",
            "prediction": {
                "disease": disease_name,
                "confidence": confidence,
                "recommendation": get_recommendation(disease_name)
            }
        })

    except Exception as e:
        # Catch-all for unexpected errors
        print(f"Unexpected error: {e}")
        import traceback
        traceback.print_exc()
        return jsonify({"status": "error", "message": "Internal server error"}), 500
```

**Test cases in Postman**:

1. **Valid image** → 200 status, success response
2. **No image in request** → 400 status, error message "No image provided"
3. **Empty file** → 400 status, error message "Empty file"
4. **Text file instead of image** → 400 status, error message "Invalid image format"
5. **Request to non-existent endpoint** → 404 status

**Validation**: All 5 test cases return appropriate status codes and error messages.

---

## Exercise 6: Confidence Threshold Implementation

**Objective**: Implement confidence-based warning system for low-certainty predictions.

**Task**: Modify your endpoint to include confidence level categorization and warnings.

**Steps**:
1. Define confidence thresholds (high: >70%, medium: 40-70%, low: <40%)
2. Add `confidence_level` field to response
3. Add `warning` field for low-confidence predictions
4. Optionally return top 3 predictions when confidence is medium or low
5. Test with images that produce various confidence levels

**Implementation**:
```python
def get_confidence_level(confidence):
    """Categorize confidence score."""
    if confidence >= 0.70:
        return "high"
    elif confidence >= 0.40:
        return "medium"
    else:
        return "low"

def get_confidence_warning(confidence_level):
    """Get warning message for low confidence."""
    if confidence_level == "low":
        return "Low confidence detected. Consider retaking photo in better lighting or consulting an expert."
    elif confidence_level == "medium":
        return "Moderate confidence. Review alternative predictions or retake photo for better results."
    return None

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # ... (validation and preprocessing code from Exercise 5)

        # Inference
        predictions = MODEL.predict(img_array)

        # Decode top prediction
        class_idx = int(np.argmax(predictions[0]))
        confidence = float(predictions[0][class_idx])
        disease_name = get_disease_name(class_idx)

        # Calculate confidence level
        confidence_level = get_confidence_level(confidence)
        warning = get_confidence_warning(confidence_level)

        # Build response
        response = {
            "status": "success",
            "prediction": {
                "disease": disease_name,
                "confidence": confidence,
                "confidence_level": confidence_level,
                "recommendation": get_recommendation(disease_name)
            }
        }

        # Add warning if needed
        if warning:
            response["prediction"]["warning"] = warning

        # Add alternative predictions for medium/low confidence
        if confidence_level in ["medium", "low"]:
            # Get top 3 predictions
            top_k_indices = np.argsort(predictions[0])[-3:][::-1]
            alternatives = []
            for idx in top_k_indices[1:]:  # Skip first (already in main prediction)
                alternatives.append({
                    "disease": get_disease_name(int(idx)),
                    "confidence": float(predictions[0][idx])
                })
            response["alternatives"] = alternatives

        return jsonify(response)

    except Exception as e:
        # ... (error handling from Exercise 5)
```

**Test cases**:

1. **High confidence prediction** (>70%):
```json
{
  "status": "success",
  "prediction": {
    "disease": "Tomato Early Blight",
    "confidence": 0.85,
    "confidence_level": "high",
    "recommendation": "..."
  }
}
```

2. **Medium confidence prediction** (40-70%):
```json
{
  "status": "success",
  "prediction": {
    "disease": "Tomato Early Blight",
    "confidence": 0.55,
    "confidence_level": "medium",
    "warning": "Moderate confidence. Review alternative predictions...",
    "recommendation": "..."
  },
  "alternatives": [
    {"disease": "Tomato Late Blight", "confidence": 0.32},
    {"disease": "Tomato Septoria Leaf Spot", "confidence": 0.08}
  ]
}
```

3. **Low confidence prediction** (<40%):
```json
{
  "status": "success",
  "prediction": {
    "disease": "Tomato Early Blight",
    "confidence": 0.35,
    "confidence_level": "low",
    "warning": "Low confidence detected. Consider retaking photo...",
    "recommendation": "..."
  },
  "alternatives": [
    {"disease": "Tomato Late Blight", "confidence": 0.28},
    {"disease": "Potato Early Blight", "confidence": 0.22}
  ]
}
```

**Validation**: Test with at least one image in each confidence category. Verify warnings and alternatives appear appropriately.

---

## Bonus Exercises

### Bonus Exercise 1: Performance Profiling

Add timing measurements to identify bottlenecks:

```python
import time

@app.route('/predict', methods=['POST'])
def predict():
    start_time = time.time()

    # ... validation ...
    validation_time = time.time()

    # Preprocess
    img_array = preprocess_image(image_bytes)
    preprocess_time = time.time()

    # Inference
    predictions = MODEL.predict(img_array)
    inference_time = time.time()

    # Decode and build response
    # ...
    response_time = time.time()

    # Log timing breakdown
    print(f"\nTiming breakdown:")
    print(f"  Validation: {(validation_time - start_time)*1000:.1f}ms")
    print(f"  Preprocessing: {(preprocess_time - validation_time)*1000:.1f}ms")
    print(f"  Inference: {(inference_time - preprocess_time)*1000:.1f}ms")
    print(f"  Response building: {(response_time - inference_time)*1000:.1f}ms")
    print(f"  Total: {(response_time - start_time)*1000:.1f}ms")

    # Optionally include in response
    response["metadata"] = {
        "inference_time_ms": int((inference_time - preprocess_time) * 1000),
        "total_time_ms": int((response_time - start_time) * 1000)
    }

    return jsonify(response)
```

Analyze output to identify slow operations. Typical results: validation (5-10ms), preprocessing (20-50ms), inference (100-500ms).

### Bonus Exercise 2: Prediction Caching

Implement caching to avoid redundant inference:

```python
import hashlib

PREDICTION_CACHE = {}

def get_image_hash(image_bytes):
    """Generate MD5 hash of image for caching."""
    return hashlib.md5(image_bytes).hexdigest()

@app.route('/predict', methods=['POST'])
def predict():
    # ... validation ...

    # Check cache
    image_hash = get_image_hash(image_bytes)
    if image_hash in PREDICTION_CACHE:
        print(f"Returning cached prediction for {image_hash}")
        cached_response = PREDICTION_CACHE[image_hash]
        cached_response["cached"] = True
        return jsonify(cached_response)

    # Normal inference
    # ...

    # Store in cache (limit cache size to prevent memory issues)
    if len(PREDICTION_CACHE) < 100:
        PREDICTION_CACHE[image_hash] = response

    return jsonify(response)
```

Test by sending same image twice. Second request should return instantly with `"cached": true`.

### Bonus Exercise 3: Batch Prediction Support

Add endpoint for processing multiple images at once:

```python
@app.route('/predict_batch', methods=['POST'])
def predict_batch():
    try:
        # Get multiple files from request
        files = request.files.getlist('images')

        if not files:
            return jsonify({"status": "error", "message": "No images provided"}), 400

        # Preprocess all images
        img_arrays = []
        for file in files:
            image_bytes = file.read()
            img_array = preprocess_image(image_bytes)
            img_arrays.append(img_array[0])  # Remove batch dimension for stacking

        # Stack into single batch
        batch_array = np.stack(img_arrays, axis=0)  # Shape: (batch_size, H, W, 3)

        # Single batch inference (faster than individual)
        predictions = MODEL.predict(batch_array)

        # Decode all predictions
        results = []
        for i in range(len(predictions)):
            class_idx = int(np.argmax(predictions[i]))
            confidence = float(predictions[i][class_idx])
            results.append({
                "disease": get_disease_name(class_idx),
                "confidence": confidence,
                "confidence_level": get_confidence_level(confidence)
            })

        return jsonify({
            "status": "success",
            "predictions": results
        })

    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500
```

Test in Postman by selecting multiple files for the `images` key.

## Summary

After completing these exercises, you should be able to:

1. Inspect model structure and extract metadata
2. Implement robust image preprocessing pipelines
3. Create and test label mapping systems
4. Build minimal inference endpoints
5. Add comprehensive error handling
6. Implement confidence-based warning systems

These skills prepare you for the Week 06 build task: integrating a real ML model into your full LeafGuard AI backend. The exercises follow the incremental development approach: start simple, add complexity gradually, test thoroughly at each step.

**Next step**: Apply these components to your main Flask application and integrate with your Android app from Week 05.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 06 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| **3** | **exercises.md** ← *You are here* | **Practice Exercises** |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Theory & Learning Notes](learning-notes.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Practice Exercises** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Build Implementation Guide →](build-task.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 05: Android Networking](../week-05-android-networking/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 07: Room Database & History ➡](../week-07-room-sqlite-history/README.md) |

---
