# Week 06 Build Task: Real ML Model Integration

## Objective

Transform your LeafGuard AI Flask backend from returning mock predictions to performing real machine learning inference using a plant disease detection model. By the end of this task, your Android app will receive genuine disease predictions with confidence scores from an actual CNN model.

## Task Overview

You will integrate a pre-trained convolutional neural network into your Flask application, implement image preprocessing to match model requirements, decode model outputs using label mapping, and return structured JSON responses containing disease names, confidence scores, and treatment recommendations. This task focuses on engineering integration, not machine learning expertise.

**Core deliverable**: A working `/predict` endpoint that accepts plant leaf images and returns real AI-powered disease predictions.

## Prerequisites

Before starting this task, ensure you have:

- **Week 05 completed**: Your Android app successfully sends images to Flask and displays mock responses
- **Flask environment ready**: Python 3.8+, virtual environment activated, Flask running
- **Model file acquired**: Either downloaded a pre-trained model or prepared to implement the 6-class fallback
- **Dependencies installed**: TensorFlow (or PyTorch), Pillow, NumPy, Flask
- **Testing tools ready**: Postman installed or curl available

**Checkpoint**: Run your Week 05 Flask server and confirm you can send images from Android. If networking isn't working, fix that first.

## Requirements

### 1. Model Integration (25 points)

**Load and initialize a plant disease detection model:**

- Use a pre-trained CNN model (TensorFlow .h5, SavedModel, or PyTorch .pt)
- Recommended: 6-class model (Tomato/Potato healthy/early blight/late blight)
- Alternative: Larger PlantVillage model if available
- Load model once at Flask startup (global scope), not per-request
- Print model input/output shapes to console on successful loading
- Handle loading errors gracefully with informative messages

**Acceptance criteria**:
- Flask starts without errors and prints "Model loaded successfully"
- Console shows input shape (e.g., `(None, 224, 224, 3)`) and output shape (e.g., `(None, 6)`)
- Model remains in memory for all subsequent requests

**Example code structure**:
```python
import tensorflow as tf

print("Loading plant disease model...")
try:
    MODEL = tf.keras.models.load_model('models/plant_disease_model.h5')
    print(f"Model loaded successfully")
    print(f"Input shape: {MODEL.input_shape}")
    print(f"Output shape: {MODEL.output_shape}")
    print(f"Classes: {MODEL.output_shape[-1]}")
except Exception as e:
    print(f"ERROR: Failed to load model: {e}")
    exit(1)
```

### 2. Image Preprocessing Pipeline (20 points)

**Implement preprocessing function that transforms raw image bytes into model-ready arrays:**

- Decode image bytes to PIL Image object
- Resize to model's expected resolution (verify from model.input_shape)
- Convert RGBA to RGB if needed (handle alpha channel)
- Convert grayscale to RGB if needed
- Normalize pixel values to [0, 1] range (or match model's training normalization)
- Convert to NumPy array
- Add batch dimension: (height, width, 3) → (1, height, width, 3)

**Acceptance criteria**:
- Function handles JPG, PNG, PNG with alpha channel without errors
- Output array shape matches model.input_shape exactly
- Pixel values are in [0, 1] range (verify with print statements during development)
- Preprocessing completes in < 100ms for typical mobile photos

**Example implementation**:
```python
from PIL import Image
import numpy as np
import io

def preprocess_image(image_bytes, target_size=(224, 224)):
    """Preprocess image for model inference."""
    # Open image from bytes
    image = Image.open(io.BytesIO(image_bytes))

    # Convert RGBA to RGB if needed
    if image.mode == 'RGBA':
        image = image.convert('RGB')

    # Convert grayscale to RGB if needed
    if image.mode != 'RGB':
        image = image.convert('RGB')

    # Resize to target dimensions
    image = image.resize(target_size)

    # Convert to array and normalize
    img_array = np.array(image, dtype=np.float32) / 255.0

    # Add batch dimension
    img_array = np.expand_dims(img_array, axis=0)

    return img_array
```

### 3. Label Mapping System (15 points)

**Create mapping from model class indices to human-readable disease names:**

- Define label mapping matching your model's class order
- For 6-class model: hardcode dictionary with 6 entries
- For larger models: load from labels.txt file (one label per line)
- Handle invalid class indices (return "Unknown" or similar)
- Include function to map disease name to treatment recommendation

**Acceptance criteria**:
- All classes have corresponding labels
- Label order matches model training (verify with test images)
- Recommendations are specific and actionable (not generic placeholders)

**Example for 6-class model**:
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
    "Tomato Healthy": "No action needed. Continue regular watering and monitoring for early signs of disease.",
    "Tomato Early Blight": "Remove and destroy infected leaves. Apply copper-based fungicide. Improve air circulation by spacing plants properly.",
    "Tomato Late Blight": "Remove and destroy infected plants immediately. Apply fungicide to nearby healthy plants. Avoid overhead watering.",
    "Potato Healthy": "No action needed. Monitor regularly and maintain proper soil moisture.",
    "Potato Early Blight": "Remove infected foliage. Apply fungicide weekly. Rotate crops next season to prevent recurrence.",
    "Potato Late Blight": "Remove infected plants immediately. Apply preventive fungicide to healthy plants. Destroy all infected plant material."
}

def get_disease_name(class_idx):
    return DISEASE_LABELS.get(class_idx, "Unknown Disease")

def get_recommendation(disease_name):
    return RECOMMENDATIONS.get(disease_name, "Consult an agricultural expert for proper diagnosis and treatment.")
```

### 4. Inference Endpoint Implementation (25 points)

**Modify `/predict` endpoint to perform real inference:**

- Accept image file via multipart form-data (key: "image")
- Validate request contains image file
- Preprocess image using your preprocessing function
- Run model inference
- Decode output: extract class index and confidence score
- Map class index to disease name
- Return JSON with structured prediction response

**JSON response structure**:
```json
{
  "status": "success",
  "prediction": {
    "disease": "Tomato Early Blight",
    "confidence": 0.78,
    "recommendation": "Remove infected leaves. Apply copper-based fungicide..."
  }
}
```

**Acceptance criteria**:
- Endpoint returns predictions for valid plant images
- Confidence scores are reasonable (0.0 to 1.0 range)
- Response JSON matches specified structure
- Predictions vary for different images (not always same output)

**Example implementation**:
```python
@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Get image from request
        if 'image' not in request.files:
            return jsonify({"status": "error", "message": "No image provided"}), 400

        image_file = request.files['image']
        image_bytes = image_file.read()

        # Preprocess
        img_array = preprocess_image(image_bytes)

        # Inference
        predictions = MODEL.predict(img_array)

        # Decode output
        class_idx = int(np.argmax(predictions[0]))
        confidence = float(predictions[0][class_idx])
        disease_name = get_disease_name(class_idx)

        # Return response
        return jsonify({
            "status": "success",
            "prediction": {
                "disease": disease_name,
                "confidence": confidence,
                "recommendation": get_recommendation(disease_name)
            }
        })

    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500
```

### 5. Error Handling (10 points)

**Implement comprehensive error handling for all failure modes:**

- Missing image in request → 400 Bad Request
- Empty filename → 400 Bad Request
- Invalid image format → 400 Bad Request
- Preprocessing errors → 400 Bad Request
- Model inference errors → 500 Internal Server Error
- Unexpected errors → 500 with generic message, detailed logs to console

**Acceptance criteria**:
- All error cases return appropriate HTTP status codes
- Error messages are descriptive and helpful
- Server doesn't crash on invalid inputs
- Python tracebacks printed to console (not sent to client)

### 6. Testing and Validation (15 points)

**Test endpoint thoroughly before Android integration:**

- Create Postman collection with test requests
- Test with at least 3 different plant leaf images
- Test error cases: no image, invalid format, empty file
- Document test results with screenshots
- Verify predictions are reasonable (not random outputs)

**Test cases required**:

1. **Valid tomato early blight image** → Returns "Tomato Early Blight" or similar
2. **Valid healthy leaf image** → Returns "Healthy" classification
3. **Valid potato disease image** → Returns appropriate potato disease
4. **Request without image** → 400 error
5. **Text file instead of image** → 400 error

**Deliverable**: Screenshots of Postman showing successful predictions and error handling.

### 7. Android Integration Update (10 points)

**Update your Android app to display real predictions:**

- Modify response parsing to extract disease, confidence, and recommendation
- Update UI to show confidence percentage (e.g., "Early Blight (78% confidence)")
- Display recommendation text in a TextView or expandable section
- Handle error responses from backend gracefully
- Test end-to-end: capture photo → send to backend → display real prediction

**Acceptance criteria**:
- Android app displays disease name from real model
- Confidence percentage shown prominently
- Recommendation text visible and readable
- No crashes when backend returns errors

**Example Kotlin update**:
```kotlin
// Data class for response (add confidence field)
data class Prediction(
    val disease: String,
    val confidence: Double,
    val recommendation: String
)

// Update UI after receiving response
private fun displayPrediction(prediction: Prediction) {
    binding.tvDiseaseName.text = prediction.disease
    binding.tvConfidence.text = "Confidence: ${(prediction.confidence * 100).toInt()}%"
    binding.tvRecommendation.text = prediction.recommendation

    // Show confidence bar or indicator
    binding.confidenceProgress.progress = (prediction.confidence * 100).toInt()
}
```

## Deliverables

Submit the following in your project repository:

1. **Updated Flask application** (`app.py` or similar)
   - Model loading code at startup
   - Preprocessing function
   - Label mapping
   - Updated `/predict` endpoint with inference
   - Error handling

2. **Model file or documentation**
   - If model file is small (< 25MB): Include in repo
   - If model file is large: Provide download instructions in README
   - Document model source, architecture, class count, and input size

3. **Label mapping file** (if using file-based approach)
   - `labels.txt` or `labels.json` with all class names

4. **Postman test results**
   - Screenshots of successful predictions (3 different images)
   - Screenshots of error handling (2 error cases)
   - Export of Postman collection JSON (optional but recommended)

5. **Updated Android app**
   - Modified data classes for prediction response
   - Updated UI code displaying confidence and recommendations
   - Screenshots of app showing real predictions

6. **Documentation** (200-300 words in README or separate doc)
   - Model choice explanation (why 6-class or larger model)
   - Preprocessing steps summary
   - Known limitations (e.g., "Currently supports tomato/potato only")
   - Testing summary (what worked, any issues encountered)

## Grading Rubric

| Component | Points | Criteria |
|-----------|--------|----------|
| Model Integration | 25 | Model loads successfully, runs at startup, handles errors |
| Preprocessing | 20 | Correct resizing, normalization, handles multiple formats |
| Label Mapping | 15 | Accurate mapping, actionable recommendations |
| Inference Endpoint | 25 | Real predictions returned, correct JSON structure |
| Error Handling | 10 | All error cases handled, appropriate status codes |
| Testing | 15 | Comprehensive Postman tests, documented results |
| Android Integration | 10 | UI displays confidence and recommendations |
| Documentation | 10 | Clear explanation of choices and limitations |
| **Total** | **130** | **Score out of 130, normalized to 100** |

**Bonus points** (up to +10):
- Implement confidence thresholds with warnings (+3)
- Return top 3 predictions instead of just top 1 (+3)
- Add performance timing/logging (+2)
- Implement prediction caching (+2)

## Implementation Timeline

**Recommended schedule for 6-8 hours of work:**

**Hours 1-2: Model Setup**
- Download or prepare model file
- Install dependencies (TensorFlow, Pillow, NumPy)
- Create model loading script, verify input/output shapes
- Test model loads successfully

**Hours 3-4: Preprocessing and Testing**
- Implement preprocessing function
- Test with various image formats (JPG, PNG, RGBA)
- Validate output shapes and value ranges
- Create label mapping for your model

**Hours 5-6: Endpoint Integration**
- Update `/predict` endpoint to use real model
- Implement error handling
- Test in Postman with multiple images
- Debug any issues (shape mismatches, normalization errors)

**Hours 7-8: Android Integration and Polish**
- Update Android app to display confidence and recommendations
- Test end-to-end from phone to backend
- Take screenshots for documentation
- Write brief documentation explaining choices

**Checkpoint at Hour 4**: You should have working preprocessing and label mapping. If stuck here, ask for help before proceeding to endpoint integration.

## Common Issues and Solutions

### Issue 1: "Model file not found"

**Cause**: Incorrect file path or model not in expected location.

**Solution**:
```python
import os
MODEL_PATH = os.path.join(os.path.dirname(__file__), 'models', 'plant_disease_model.h5')
print(f"Looking for model at: {os.path.abspath(MODEL_PATH)}")
MODEL = tf.keras.models.load_model(MODEL_PATH)
```

### Issue 2: "Input shape mismatch"

**Cause**: Preprocessing output doesn't match model expectations.

**Solution**: Print shapes at each step:
```python
print(f"After resize: {image.size}")
print(f"After array conversion: {img_array.shape}")
print(f"After batch dimension: {img_array.shape}")
print(f"Expected by model: {MODEL.input_shape}")
```

### Issue 3: "Predictions are always the same"

**Cause**: Model not receiving different inputs (preprocessing issue) or model is broken.

**Solution**: Save preprocessed image to verify it looks correct:
```python
from PIL import Image
img_to_save = (img_array[0] * 255).astype(np.uint8)
Image.fromarray(img_to_save).save('preprocessed_debug.png')
```

If image looks correct, model might be broken. Try redownloading.

### Issue 4: "Confidence scores don't make sense"

**Cause**: Label mapping order doesn't match model training.

**Solution**: Test with known images from training dataset. If predictions are wrong, reorder labels.

### Issue 5: "Android receives 500 error"

**Cause**: Unhandled exception in Flask.

**Solution**: Check Flask console logs for Python traceback. Add print statements to find where error occurs:
```python
print("1. Received request")
print("2. Read image bytes")
print("3. Preprocessed image")
print("4. Running inference...")
predictions = MODEL.predict(img_array)
print("5. Inference complete")
```

## Fallback Option

**If you cannot obtain a pre-trained model:**

Use the 6-class fallback strategy:
1. Train a simple model using Teachable Machine (https://teachablemachine.withgoogle.com/)
2. Export as TensorFlow Lite or Keras model
3. Use 6 classes: Tomato Healthy/Early/Late Blight, Potato Healthy/Early/Late Blight
4. Clearly document this is a simplified implementation for learning purposes

**You will NOT be penalized** for using a smaller model if implementation quality is high. Engineering skills (integration, error handling, testing) are more important than model size.

## Success Criteria

Your build task is successful when:

1. ✅ Flask server starts and loads model without errors
2. ✅ Postman tests return predictions with varying disease names and confidence scores
3. ✅ Error cases (no image, invalid format) return appropriate error responses
4. ✅ Android app displays disease name, confidence percentage, and recommendation
5. ✅ End-to-end test works: Capture photo on Android → Real prediction appears in UI
6. ✅ Documentation explains model choice and known limitations

**Final validation test**: Take a photo of a tomato or potato leaf with your Android app, send to backend, see a real disease prediction with confidence score displayed in your UI. If this works, you've successfully completed Week 06.

## Resources

- TensorFlow Model Loading: https://www.tensorflow.org/guide/keras/save_and_serialize
- PIL Image Processing: https://pillow.readthedocs.io/en/stable/
- NumPy Array Operations: https://numpy.org/doc/stable/reference/arrays.ndarray.html
- Flask File Uploads: https://flask.palletsprojects.com/en/2.3.x/patterns/fileuploads/
- Postman Documentation: https://learning.postman.com/docs/getting-started/introduction/

**Pre-trained Model Sources**:
- Kaggle Plant Disease Models: https://www.kaggle.com/search?q=plant+disease+model
- TensorFlow Hub: https://tfhub.dev/s?q=plant
- Teachable Machine (for custom training): https://teachablemachine.withgoogle.com/

## Next Steps

After completing this build task:
1. Complete the validation checklist to ensure all requirements met
2. Write your reflection (Week 06 reflection prompts in README)
3. Begin Week 07: Room/SQLite database for scan history storage

Week 06 establishes your app's core AI functionality. All future weeks build on this foundation by adding features around the prediction capability you've created.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 06 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| **4** | **build-task.md** ← *You are here* | **Build Implementation Guide** |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Practice Exercises](exercises.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Build Implementation Guide** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Validation & Verification →](validation-checklist.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 05: Android Networking](../week-05-android-networking/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 07: Room Database & History ➡](../week-07-room-sqlite-history/README.md) |

---
