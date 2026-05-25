# Week 06: Real ML Model Integration in Backend

## 1. Overview

This week marks the critical transition from mock predictions to real machine learning model inference in your Flask backend. You will integrate a convolutional neural network (CNN) model trained for plant disease detection, implement proper image preprocessing pipelines, decode model outputs using label mapping, and return confidence scores to your Android client. This transforms LeafGuard AI from a proof-of-concept into a functional disease detection system.

The focus is on mobile engineering principles: understanding how to integrate pre-trained models without needing deep ML expertise, handling real-world constraints like limited model accuracy, implementing fallback strategies when full models are unavailable, and managing the complete inference pipeline from raw image bytes to human-readable predictions.

**Week 06 is about integration, not training.** You will work with existing models, understand their input/output contracts, and build production-grade inference endpoints that your mobile app can reliably consume.

## 2. Learning Objectives

By the end of Week 06, you will be able to:

1. Load and initialize TensorFlow/PyTorch models in a Flask application for inference
2. Implement image preprocessing pipelines (resize, normalize, tensor conversion) matching training parameters
3. Decode model outputs using label mapping files or hardcoded class lists
4. Extract confidence scores from softmax probability distributions
5. Design robust inference endpoints handling various image formats and error conditions
6. Implement fallback strategies using limited class models when full datasets are unavailable
7. Return structured JSON responses containing disease name, confidence, and recommendations
8. Test model integration using tools like Postman before Android integration
9. Understand model limitations and communicate uncertainty to users appropriately
10. Apply CSE 2206 concepts: file I/O for model loading, data structures for label mapping, exception handling for inference errors

## 3. Prerequisites

Before starting Week 06, ensure you have completed:

- **Week 05 fully functional**: Your Android app can capture images, send them to your Flask `/predict` endpoint via Retrofit, and display mock responses in the UI
- **Flask development environment**: Python 3.8+, virtual environment activated, Flask server running on localhost:5000 or deployed URL
- **Model file access**: Downloaded a pre-trained plant disease model (TFLite, SavedModel, or PyTorch) or prepared to use the fallback 6-class model approach
- **Basic Python understanding**: File I/O, dictionaries, lists, function definitions, imports
- **HTTP testing tools**: Postman installed or familiarity with curl for API testing

**Critical checkpoint**: Your Week 05 `/predict` endpoint should successfully receive images from Android and return mock JSON. If networking isn't working, fix Week 05 before proceeding.

## 4. CSE 2206 Connections

Week 06 directly applies concepts from your Mobile Application Development course:

- **File and Storage (Weeks 5-6 syllabus)**: Loading model files (.h5, .pt, .tflite) from disk, reading label mapping text files or JSON
- **Data Structures**: Using Python dictionaries for label mapping (class_id → disease_name), lists for probability arrays, tuples for (confidence, class_name) pairs
- **Error Handling**: Try-catch blocks for model loading failures, invalid image formats, out-of-memory errors during inference
- **API Design**: Structuring JSON responses for mobile consumption, versioning endpoints (/api/v1/predict), status codes (200 OK, 400 Bad Request, 500 Server Error)
- **Performance Optimization**: Lazy loading models once at startup rather than per-request, caching predictions for identical images
- **Testing Strategies**: Unit testing preprocessing functions, integration testing full inference pipeline, validating output JSON schema

The machine learning model is treated as a **black box component** you integrate, similar to using third-party libraries. Your focus is on the engineering aspects: input/output contracts, error handling, and client-server integration.

## 5. Topic Breakdown

### 5.1 Model Fundamentals for Mobile Engineers

**What you need to know**: Neural networks output raw probability scores (logits) or normalized probabilities (softmax) for each class. A 38-class plant disease model will output 38 numbers, each representing the model's confidence that the input image belongs to that class. Your job is to find the highest score and map the class index to a human-readable disease name.

**Input contract**: Models expect images in specific formats. Common requirements:
- Fixed resolution (224x224, 256x256, or 299x299 pixels)
- Normalized pixel values (0-1 range or -1 to +1 range)
- Specific channel order (RGB vs BGR)
- Batch dimension (shape: [1, height, width, channels])

**Output contract**: A 1D array of probabilities, length equal to number of classes. Example for 6-class model:
```
[0.02, 0.85, 0.01, 0.05, 0.03, 0.04]
  ↑     ↑
Class 0  Class 1 (highest confidence)
```

**You do NOT need to understand backpropagation, training loops, or loss functions.** You only need to understand the input/output contract.

### 5.2 Image Preprocessing Pipeline

Before feeding an image to the model, you must preprocess it to match training conditions.

**Step 1: Decode image bytes**
```python
from PIL import Image
import io

image = Image.open(io.BytesIO(image_bytes))
```

**Step 2: Resize to model input size**
```python
image = image.resize((224, 224))  # Match model's expected resolution
```

**Step 3: Convert to array and normalize**
```python
import numpy as np

img_array = np.array(image) / 255.0  # Normalize to [0, 1]
```

**Step 4: Add batch dimension**
```python
img_array = np.expand_dims(img_array, axis=0)  # Shape: (1, 224, 224, 3)
```

**Critical**: These parameters must match training. If the model was trained with mean subtraction (ImageNet normalization), you must apply the same transform. Check model documentation or training scripts.

### 5.3 Label Mapping

Models output class indices (0, 1, 2, ...), but users need disease names ("Tomato Early Blight"). You need a mapping:

**Approach 1: Hardcoded dictionary (simple, good for small class counts)**
```python
LABELS = {
    0: "Tomato Healthy",
    1: "Tomato Early Blight",
    2: "Tomato Late Blight",
    3: "Potato Healthy",
    4: "Potato Early Blight",
    5: "Potato Late Blight"
}
```

**Approach 2: Load from file (scalable for large class counts)**
```
# labels.txt (one class per line, line number = class index)
Tomato Healthy
Tomato Early Blight
Tomato Late Blight
...
```

```python
with open('labels.txt', 'r') as f:
    LABELS = [line.strip() for line in f.readlines()]
```

**For Week 06**: Use the approach that matches your model. If using a fallback 6-class model, hardcode the labels for simplicity.

### 5.4 Inference Pipeline

Putting it all together in Flask:

```python
import tensorflow as tf
from flask import Flask, request, jsonify
import numpy as np
from PIL import Image
import io

app = Flask(__name__)

# Load model once at startup
MODEL = tf.keras.models.load_model('plant_disease_model.h5')

LABELS = {
    0: "Tomato Healthy",
    1: "Tomato Early Blight",
    # ... full mapping
}

@app.route('/predict', methods=['POST'])
def predict():
    try:
        # 1. Get image bytes from request
        image_file = request.files['image']
        image_bytes = image_file.read()

        # 2. Preprocess
        image = Image.open(io.BytesIO(image_bytes))
        image = image.resize((224, 224))
        img_array = np.array(image) / 255.0
        img_array = np.expand_dims(img_array, axis=0)

        # 3. Inference
        predictions = MODEL.predict(img_array)

        # 4. Decode output
        class_idx = np.argmax(predictions[0])
        confidence = float(predictions[0][class_idx])
        disease_name = LABELS[class_idx]

        # 5. Return JSON
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

### 5.5 Confidence Scores and Thresholding

Raw confidence scores may not reflect true certainty. A model might output 0.65 (65%) confidence but still be wrong.

**Best practices**:
- Display confidence percentage to users: "Early Blight (78% confidence)"
- Implement threshold warnings: If confidence < 50%, show "Low confidence, consult an expert"
- Show top 3 predictions if top confidence is low: "Might be Early Blight (45%) or Late Blight (38%)"
- NEVER claim 100% certainty, even at 99% confidence

**Example response structure**:
```json
{
  "status": "success",
  "prediction": {
    "disease": "Tomato Early Blight",
    "confidence": 0.78,
    "confidence_level": "high",
    "recommendation": "Remove infected leaves, apply copper fungicide"
  },
  "alternatives": [
    {"disease": "Tomato Late Blight", "confidence": 0.15},
    {"disease": "Tomato Septoria Leaf Spot", "confidence": 0.04}
  ]
}
```

### 5.6 Fallback Strategy: 6-Class Model

**Reality check**: You may not have access to a trained 38-class PlantVillage model. Training requires GPUs, large datasets, and ML expertise beyond this course's scope.

**Fallback solution**: Use a simplified 6-class model covering:
1. Tomato Healthy
2. Tomato Early Blight
3. Tomato Late Blight
4. Potato Healthy
5. Potato Early Blight
6. Potato Late Blight

**Why this works**: These are the most common classes in plant disease datasets, models are easier to train with small datasets, and it demonstrates full integration skills without requiring extensive ML resources.

**Implementation**: If you use the fallback, clearly communicate limitations in your UI: "LeafGuard AI currently supports tomato and potato disease detection. More crops coming soon!"

**Grading note**: Your project is evaluated on mobile engineering skills (API integration, UI/UX, error handling), not ML model accuracy. A working 6-class system is better than a non-functional 38-class attempt.

### 5.7 Testing Before Android Integration

**Use Postman to test your `/predict` endpoint**:

1. Create new POST request to `http://localhost:5000/predict`
2. Set Body type to `form-data`
3. Add key `image`, change type to `File`, upload a test plant leaf image
4. Send request
5. Verify JSON response structure matches expected format
6. Test error cases: invalid file types, missing image key, corrupted images

**Test cases**:
- Healthy leaf → Should return "Healthy" with high confidence
- Diseased leaf → Should return correct disease name
- Non-leaf image (e.g., cat photo) → Should still return a class but with low confidence (model limitation)
- Invalid file format (e.g., .txt file) → Should return error with 400 status code

**Only proceed to Android integration after Flask endpoint passes all tests.**

### 5.8 Model Limitations and User Communication

**Critical engineering principle**: Be honest about limitations.

**Common model limitations**:
- Only works for trained classes (can't detect new diseases)
- Lighting conditions affect accuracy (shadows, overexposure)
- Image quality matters (blurry photos reduce confidence)
- Similar-looking diseases may be confused
- Background clutter can confuse the model

**How to communicate this in your app**:
- Display confidence scores prominently
- Add disclaimer text: "This is a diagnostic aid, not a replacement for professional advice"
- Provide tips for best results: "Take photos in natural light, fill frame with leaf"
- Show "Learn More" links to trusted agricultural resources
- Log low-confidence predictions for future model improvement

**Mobile engineering is about building trust through transparency.**

## 6. Hands-On Practice

### Practice 1: Model Loading Test

Create a minimal Flask script that loads a model and prints its input/output shapes:

```python
import tensorflow as tf

model = tf.keras.models.load_model('your_model.h5')
print("Input shape:", model.input_shape)
print("Output shape:", model.output_shape)
print("Number of classes:", model.output_shape[-1])
```

Expected output: `Input shape: (None, 224, 224, 3)`, `Output shape: (None, 38)`

If this fails, your model file is corrupted or incompatible. Verify TensorFlow version compatibility.

### Practice 2: Preprocessing Function Unit Test

Test your preprocessing function with a sample image:

```python
def preprocess_image(image_bytes, target_size=(224, 224)):
    image = Image.open(io.BytesIO(image_bytes))
    image = image.resize(target_size)
    img_array = np.array(image) / 255.0
    img_array = np.expand_dims(img_array, axis=0)
    return img_array

# Test
with open('test_leaf.jpg', 'rb') as f:
    img_bytes = f.read()

processed = preprocess_image(img_bytes)
print("Processed shape:", processed.shape)
print("Value range:", processed.min(), processed.max())
```

Expected output: `Processed shape: (1, 224, 224, 3)`, `Value range: 0.0 1.0`

If range is 0-255 instead of 0-1, normalization failed.

### Practice 3: Label Mapping Verification

Test label mapping logic:

```python
LABELS = {
    0: "Tomato Healthy",
    1: "Tomato Early Blight",
    2: "Tomato Late Blight",
    3: "Potato Healthy",
    4: "Potato Early Blight",
    5: "Potato Late Blight"
}

# Simulate model output
predictions = np.array([0.05, 0.75, 0.10, 0.03, 0.04, 0.03])

class_idx = np.argmax(predictions)
confidence = predictions[class_idx]
disease = LABELS[class_idx]

print(f"Prediction: {disease} ({confidence:.2%})")
```

Expected output: `Prediction: Tomato Early Blight (75.00%)`

### Practice 4: Error Handling Test

Test how your endpoint handles invalid inputs:

```python
@app.route('/predict', methods=['POST'])
def predict():
    try:
        if 'image' not in request.files:
            return jsonify({"status": "error", "message": "No image provided"}), 400

        image_file = request.files['image']

        if image_file.filename == '':
            return jsonify({"status": "error", "message": "Empty filename"}), 400

        # ... rest of inference logic

    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500
```

Test with Postman by sending requests without image, with empty files, with non-image files.

### Practice 5: Performance Measurement

Measure inference time to understand latency:

```python
import time

@app.route('/predict', methods=['POST'])
def predict():
    start_time = time.time()

    # ... preprocessing and inference

    inference_time = time.time() - start_time
    print(f"Inference took {inference_time:.3f} seconds")

    return jsonify({
        "status": "success",
        "prediction": {...},
        "inference_time_ms": int(inference_time * 1000)
    })
```

Typical inference times: 100-500ms on CPU, 20-100ms on GPU. If > 2 seconds, investigate preprocessing bottlenecks.

### Practice 6: Postman Collection Setup

Create a Postman collection for testing:

1. Request: "Predict - Valid Image"
   - POST to `/predict`
   - Body: form-data with image file
   - Tests: Check status is "success", confidence > 0

2. Request: "Predict - No Image"
   - POST to `/predict`
   - Body: empty
   - Tests: Check status code is 400

3. Request: "Predict - Invalid Format"
   - POST to `/predict`
   - Body: .txt file instead of image
   - Tests: Check error message contains "invalid" or "format"

Save this collection for regression testing after future changes.

## 7. Common Pitfalls

### Pitfall 1: Mismatched Input Shapes

**Problem**: Model expects 224x224 images, you send 256x256. Result: Runtime error or silent accuracy degradation.

**Solution**: Always check `model.input_shape` and resize images to match exactly. Use assertions during development:

```python
assert img_array.shape == (1, 224, 224, 3), f"Wrong shape: {img_array.shape}"
```

### Pitfall 2: Missing Normalization

**Problem**: You forget to divide by 255.0, feeding integer pixel values (0-255) instead of floats (0-1). Model was trained on normalized images.

**Solution**: Check your training code or model documentation. If using a pre-trained model, normalize to [0, 1] unless specified otherwise. Validate:

```python
print("Min:", img_array.min(), "Max:", img_array.max())  # Should be 0.0 and 1.0
```

### Pitfall 3: Wrong Label Mapping Order

**Problem**: Your label mapping doesn't match the model's class order. Class 0 is "Tomato Healthy" in training but "Potato Healthy" in your mapping.

**Solution**: Verify class order from training code or dataset documentation. Test with known images and validate predictions match expected classes.

### Pitfall 4: Loading Model Per Request

**Problem**: You load the model inside the `/predict` function, causing 5-10 second delays per request.

**Solution**: Load model once at Flask startup (global scope), reuse for all requests:

```python
# BAD: Loads every request
@app.route('/predict', methods=['POST'])
def predict():
    model = tf.keras.models.load_model('model.h5')  # 5 seconds!
    predictions = model.predict(img)

# GOOD: Load once at startup
MODEL = tf.keras.models.load_model('model.h5')

@app.route('/predict', methods=['POST'])
def predict():
    predictions = MODEL.predict(img)  # 100ms
```

### Pitfall 5: Ignoring Confidence Scores

**Problem**: Always returning the top prediction even when confidence is 10%, misleading users.

**Solution**: Implement confidence thresholds:

```python
if confidence < 0.5:
    return jsonify({
        "status": "success",
        "prediction": {
            "disease": disease_name,
            "confidence": confidence,
            "warning": "Low confidence - consider retaking photo or consulting expert"
        }
    })
```

### Pitfall 6: No Error Handling for Missing Dependencies

**Problem**: Flask crashes on startup if TensorFlow isn't installed, giving cryptic errors.

**Solution**: Add graceful error handling:

```python
try:
    import tensorflow as tf
except ImportError:
    print("ERROR: TensorFlow not installed. Run: pip install tensorflow")
    exit(1)

try:
    MODEL = tf.keras.models.load_model('model.h5')
except Exception as e:
    print(f"ERROR: Failed to load model: {e}")
    exit(1)
```

### Pitfall 7: Hardcoded File Paths

**Problem**: Model path is `/Users/yourname/Desktop/model.h5`, breaks when deploying or sharing code.

**Solution**: Use relative paths or environment variables:

```python
import os

MODEL_PATH = os.environ.get('MODEL_PATH', './models/plant_disease_model.h5')
MODEL = tf.keras.models.load_model(MODEL_PATH)
```

### Pitfall 8: Not Testing with Real Mobile Photos

**Problem**: Testing only with clean dataset images, then real mobile photos (shadows, angles, backgrounds) fail.

**Solution**: Test with images captured from your Android app during development. Export photos from emulator/device, test in Postman, iterate on preprocessing.

## 8. Success Criteria

You have successfully completed Week 06 when:

1. **Model loads successfully** at Flask startup without errors
2. **Preprocessing pipeline** correctly resizes, normalizes, and batches images
3. **Inference endpoint** returns predictions with disease name and confidence score
4. **Label mapping** accurately converts class indices to human-readable names
5. **Error handling** catches and returns appropriate status codes for invalid inputs
6. **Postman tests pass** for valid images, missing images, and invalid formats
7. **Performance acceptable**: Inference completes in < 2 seconds on development machine
8. **Confidence scores displayed**: JSON response includes 0-1 confidence value
9. **Android integration working**: LeafGuard AI displays real predictions from model (Week 05 integration + Week 06 backend)
10. **Documentation updated**: Model choice, class count, and limitations documented in README

**Validation test**: Take a photo of a tomato leaf with your Android app, send to backend, receive real prediction with confidence score displayed in UI. This proves full stack integration.

## 9. Build Task

**Objective**: Integrate a real plant disease detection model into your Flask backend and return real predictions to your Android app.

**Requirements**:

1. **Model Integration**
   - Load a pre-trained plant disease model (38-class PlantVillage or 6-class fallback)
   - Model must load at Flask startup, not per-request
   - Print model input/output shapes to console on startup for verification

2. **Preprocessing Pipeline**
   - Implement function to preprocess image bytes to model input format
   - Resize to correct dimensions (typically 224x224 or 256x256)
   - Normalize pixel values to [0, 1] or match training normalization
   - Add batch dimension
   - Handle RGB/RGBA conversion if needed

3. **Label Mapping**
   - Create label mapping from class indices to disease names
   - Use hardcoded dictionary for 6-class model or load from file for larger models
   - Ensure class order matches model training

4. **Inference Endpoint**
   - Modify `/predict` endpoint to use real model instead of mock responses
   - Return JSON with structure: `{"status": "success", "prediction": {"disease": "...", "confidence": 0.xx, "recommendation": "..."}}`
   - Include confidence score as float (0.0 to 1.0)
   - Add basic recommendation text based on disease type

5. **Error Handling**
   - Return 400 status for missing/invalid images
   - Return 500 status for model inference errors
   - Include descriptive error messages in JSON response

6. **Testing**
   - Test endpoint with Postman using at least 3 different plant images
   - Verify predictions are reasonable (not random)
   - Document test results (screenshot of Postman response)

7. **Android Integration**
   - Update Android app to parse and display real predictions
   - Show disease name and confidence percentage in UI
   - Test end-to-end: capture photo → send to backend → display prediction

**Deliverables**:
- Updated `app.py` (Flask backend) with model integration
- `labels.txt` or hardcoded label mapping in code
- Screenshots of Postman tests showing successful predictions
- Updated Android app displaying real predictions
- Brief documentation (200-300 words) explaining model choice, preprocessing steps, and any limitations

**Time estimate**: 6-8 hours (2 hours model setup, 2 hours preprocessing, 2 hours integration, 2 hours testing)

**Fallback option**: If you cannot access a pre-trained model, use the 6-class fallback strategy and clearly document this choice. You will not be penalized for using a smaller model if engineering implementation is solid.

## 10. Knowledge Check

Test your understanding before proceeding:

1. **What is the purpose of image normalization in preprocessing?**
   - To match the training data distribution and ensure pixel values are in the expected range (typically 0-1)

2. **Why do we add a batch dimension to single images?**
   - Models expect inputs with shape (batch_size, height, width, channels). Single images are (height, width, channels), so we expand to (1, height, width, channels)

3. **What does np.argmax() do when applied to model predictions?**
   - Returns the index of the highest value in the array, which corresponds to the predicted class

4. **Why should models be loaded at Flask startup, not per-request?**
   - Loading models is slow (5-10 seconds). Loading once and reusing for all requests keeps inference time low (100-500ms)

5. **What should you return if model confidence is below 30%?**
   - Return the prediction but include a warning message like "Low confidence - consider retaking photo or consulting an expert"

6. **How do you convert a NumPy float32 to a JSON-serializable float?**
   - Use `float(value)` to convert NumPy types to Python native types

7. **What HTTP status code should you return for missing image in request?**
   - 400 Bad Request (client error due to invalid request structure)

8. **Why might a model predict incorrectly even with 80% confidence?**
   - Model confidence reflects internal probability distribution, not ground truth accuracy. Models can be confidently wrong, especially for out-of-distribution inputs

9. **What is the difference between logits and softmax probabilities?**
   - Logits are raw model outputs (can be any value). Softmax probabilities are normalized to sum to 1.0, representing confidence distribution across classes

10. **How would you implement returning top 3 predictions instead of just top 1?**
    - Use `np.argsort(predictions[0])[-3:][::-1]` to get indices of top 3 classes, map to disease names and confidences

## 11. Resources

### Official Documentation
- TensorFlow Keras Model Loading: https://www.tensorflow.org/guide/keras/save_and_serialize
- PyTorch Model Loading: https://pytorch.org/tutorials/beginner/saving_loading_models.html
- Flask Request Handling: https://flask.palletsprojects.com/en/2.3.x/api/#flask.Request
- PIL Image Processing: https://pillow.readthedocs.io/en/stable/reference/Image.html

### Tutorials and Guides
- Image Classification Inference: https://www.tensorflow.org/lite/examples/image_classification/overview
- PlantVillage Dataset: https://github.com/spMohanty/PlantVillage-Dataset (reference for class labels)
- Flask REST API Best Practices: https://flask-restful.readthedocs.io/en/latest/

### Model Sources
- TensorFlow Hub Plant Models: https://tfhub.dev/s?q=plant
- Kaggle Plant Disease Models: https://www.kaggle.com/search?q=plant+disease+model
- Pre-trained TFLite Models: https://www.tensorflow.org/lite/examples/image_classification/overview

### Tools
- Postman: https://www.postman.com/downloads/
- Netron (Model Visualizer): https://netron.app/ (inspect model architecture and I/O shapes)
- ImageMagick: https://imagemagick.org/ (command-line image preprocessing testing)

### Reference Projects
- TensorFlow Lite Plant Disease Detection: https://github.com/tensorflow/examples/tree/master/lite/examples/image_classification
- Flask ML API Template: https://github.com/cosmic-cortex/pytorch-flask-api

### Debugging Resources
- TensorFlow Model Compatibility: Check TensorFlow version used for training vs. inference (e.g., TF 2.10 model might not load in TF 2.15 without conversion)
- Common NumPy Errors: https://numpy.org/doc/stable/user/troubleshooting-importerror.html
- Flask Debugging Mode: Set `FLASK_ENV=development` for detailed error traces

## 12. Additional Challenges

For students who complete the base build task early:

### Challenge 1: Multi-Model Ensemble (Advanced)

Load 2-3 different plant disease models and average their predictions for improved accuracy:

```python
MODEL_1 = load_model('model1.h5')
MODEL_2 = load_model('model2.h5')

pred1 = MODEL_1.predict(img)
pred2 = MODEL_2.predict(img)
avg_pred = (pred1 + pred2) / 2

class_idx = np.argmax(avg_pred[0])
```

This reduces prediction variance and can improve robustness.

### Challenge 2: Top-K Predictions

Return top 3 most likely diseases instead of just top 1:

```python
top_k_indices = np.argsort(predictions[0])[-3:][::-1]

results = []
for idx in top_k_indices:
    results.append({
        "disease": LABELS[idx],
        "confidence": float(predictions[0][idx])
    })

return jsonify({
    "status": "success",
    "top_predictions": results
})
```

Display all 3 in Android UI to show uncertainty.

### Challenge 3: Preprocessing Augmentation

Test how different preprocessing affects predictions:

```python
# Original
img1 = preprocess_image(image_bytes)

# Slight rotation
img2 = preprocess_image(rotate_image(image_bytes, 10))

# Brightness adjustment
img3 = preprocess_image(adjust_brightness(image_bytes, 1.2))

# Average predictions from all three
pred = (MODEL.predict(img1) + MODEL.predict(img2) + MODEL.predict(img3)) / 3
```

This simulates test-time augmentation (TTA) for improved accuracy.

### Challenge 4: Confidence Calibration

Analyze model confidence distribution and implement calibrated thresholds:

```python
# Collect predictions on validation set
confidences = []
correct = []

for image, true_label in validation_set:
    pred = MODEL.predict(image)
    confidence = np.max(pred)
    predicted_label = LABELS[np.argmax(pred)]

    confidences.append(confidence)
    correct.append(predicted_label == true_label)

# Analyze: what confidence threshold gives 90% accuracy?
# Implement dynamic thresholding based on this analysis
```

### Challenge 5: Model Compression

Convert your Keras model to TensorFlow Lite for faster inference:

```python
import tensorflow as tf

converter = tf.lite.TFLiteConverter.from_keras_model(MODEL)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()

with open('model.tflite', 'wb') as f:
    f.write(tflite_model)

# Then load in Flask with TFLite interpreter
interpreter = tf.lite.Interpreter(model_path='model.tflite')
interpreter.allocate_tensors()
```

TFLite models are 2-4x smaller and often faster on CPU.

### Challenge 6: Caching Layer

Implement prediction caching to avoid redundant inference for identical images:

```python
import hashlib

CACHE = {}

@app.route('/predict', methods=['POST'])
def predict():
    image_bytes = request.files['image'].read()

    # Generate hash of image
    image_hash = hashlib.md5(image_bytes).hexdigest()

    # Check cache
    if image_hash in CACHE:
        print("Returning cached prediction")
        return jsonify(CACHE[image_hash])

    # Normal inference
    result = run_inference(image_bytes)

    # Store in cache
    CACHE[image_hash] = result

    return jsonify(result)
```

Be careful with memory usage if cache grows large.

## 13. Week 06 Summary

Week 06 transformed your Flask backend from returning mock data to performing real machine learning inference. You learned how to:

- Load pre-trained models and understand their input/output contracts
- Implement image preprocessing pipelines matching training parameters
- Decode model predictions using label mapping
- Extract and return confidence scores
- Handle errors gracefully at every stage
- Test endpoints thoroughly before Android integration

**Key takeaway**: Machine learning integration is an engineering challenge, not a data science challenge. You don't need to understand training algorithms to build production inference systems. Focus on contracts, preprocessing, error handling, and user trust.

**What's next**: Week 07 introduces Room database for storing scan history locally on the device. You'll learn SQLite fundamentals, implement Entity/DAO/Database architecture, and build a history screen showing past predictions. This enables offline access to previous scans and sets the stage for analytics features in later weeks.

**Integration checkpoint**: By end of Week 06, your full stack should work end-to-end: Android captures image → sends via Retrofit → Flask backend preprocesses and runs inference → returns real prediction → Android displays disease name and confidence. This is your project's core functionality. Weeks 7-10 add features around this foundation.

## 14. Troubleshooting Guide

### Problem: "Model file not found" error on Flask startup

**Cause**: Incorrect file path or model file not in expected location.

**Solutions**:
1. Print the absolute path you're trying to load: `print(os.path.abspath('model.h5'))`
2. Check Flask's working directory: `print(os.getcwd())`
3. Place model file in same directory as `app.py` or use absolute path
4. Use environment variable for flexibility: `MODEL_PATH = os.environ.get('MODEL_PATH', './model.h5')`

### Problem: "Failed to load model" or version compatibility errors

**Cause**: Model was saved with different TensorFlow version than you're using for loading.

**Solutions**:
1. Check TensorFlow version: `python -c "import tensorflow as tf; print(tf.__version__)"`
2. Try loading with `compile=False`: `model = tf.keras.models.load_model('model.h5', compile=False)`
3. If model is from TF 1.x, use migration guide: https://www.tensorflow.org/guide/migrate
4. Re-export model in compatible format if you have access to training environment

### Problem: Predictions are random/nonsensical

**Cause**: Preprocessing doesn't match training, or label mapping is incorrect.

**Solutions**:
1. Verify preprocessing by saving preprocessed image and visually inspecting it:
   ```python
   from PIL import Image
   img_to_save = (img_array[0] * 255).astype(np.uint8)
   Image.fromarray(img_to_save).save('preprocessed.png')
   ```
   Image should look normal, not distorted or wrong colors.

2. Test with known images from training set - predictions should be correct
3. Check normalization range: print `img_array.min()` and `img_array.max()`, should be 0.0 and 1.0
4. Verify label mapping order matches training class indices

### Problem: Slow inference (> 5 seconds per request)

**Cause**: Model loading per-request or inefficient preprocessing.

**Solutions**:
1. Ensure model is loaded globally, not inside endpoint function
2. Profile code to find bottleneck:
   ```python
   import time
   start = time.time()
   # ... operation ...
   print(f"Operation took {time.time() - start:.3f}s")
   ```
3. Check CPU usage - if maxed out, consider GPU inference or smaller model
4. Reduce image size during preprocessing if extremely large

### Problem: "Out of memory" error during inference

**Cause**: Model too large for available RAM, or memory leak from repeated inference.

**Solutions**:
1. Use TensorFlow Lite model (smaller memory footprint)
2. Implement model unloading/reloading if memory constrained
3. Monitor memory usage: `ps aux | grep python` on Linux/macOS
4. Reduce batch size (should be 1 for single image inference)
5. Restart Flask server periodically if memory leaks (not ideal but works for development)

### Problem: Android app receives 500 error from Flask

**Cause**: Unhandled exception in Flask endpoint.

**Solutions**:
1. Check Flask console logs for Python traceback
2. Add comprehensive try-catch in endpoint:
   ```python
   try:
       # ... inference logic ...
   except Exception as e:
       print(f"Error during inference: {e}")
       import traceback
       traceback.print_exc()
       return jsonify({"status": "error", "message": str(e)}), 500
   ```
3. Test endpoint in Postman to isolate Flask vs Android issue
4. Enable Flask debug mode: `app.run(debug=True)` for detailed error pages

### Problem: Model works locally but fails when deployed (Heroku/AWS)

**Cause**: Model file not included in deployment or different Python environment.

**Solutions**:
1. Ensure model file is committed to Git or uploaded separately (large files may need Git LFS)
2. Check deployment logs for missing dependencies: `pip freeze > requirements.txt`
3. Verify TensorFlow installs correctly on deployment platform (some platforms require specific builds)
4. Use smaller TFLite model to reduce deployment size
5. Consider loading model from cloud storage (S3, Google Cloud Storage) instead of bundling

### Problem: Predictions different between Postman and Android

**Cause**: Image encoding differences (JPEG quality, color space, orientation).

**Solutions**:
1. Export exact same image bytes from Android and test in Postman
2. Check image orientation - mobile photos may have EXIF rotation metadata
3. Handle EXIF orientation in preprocessing:
   ```python
   from PIL import ImageOps
   image = Image.open(io.BytesIO(image_bytes))
   image = ImageOps.exif_transpose(image)  # Fix rotation
   ```
4. Log image properties on Flask side to compare: `print(image.size, image.mode)`

### Problem: "Cannot convert NumPy types to JSON" error

**Cause**: NumPy data types (float32, int64) are not JSON serializable.

**Solutions**:
1. Convert to Python native types: `float(numpy_value)`, `int(numpy_value)`
2. Use `.tolist()` for arrays: `predictions[0].tolist()`
3. Ensure all values in response dict are native Python types before `jsonify()`

## 15. Reflection Prompts

After completing Week 06, reflect on these questions (write 2-3 paragraphs in your `reflection.md`):

1. **Integration vs Implementation**: How did integrating a pre-trained model differ from implementing features from scratch (like UI layouts or API calls)? What new types of debugging were required?

2. **Confidence vs Accuracy**: You implemented confidence scores, but realized they don't guarantee accuracy. How would you explain this distinction to a non-technical user? What UI/UX choices help communicate uncertainty?

3. **Model as Black Box**: You treated the ML model as a black box, focusing only on input/output contracts. When might you need to understand model internals? When is black-box integration sufficient?

4. **Fallback Strategies**: If you used a 6-class model instead of 38-class, how did you handle this limitation in your app? How do real-world products handle feature limitations while maintaining user trust?

5. **Testing ML Systems**: How did testing change compared to previous weeks? What challenges arise when the system's output isn't deterministic (same input doesn't always give same output due to preprocessing variations)?

6. **Performance Tradeoffs**: What tradeoffs did you observe between model accuracy, inference speed, and model size? How would you make deployment decisions for a resource-constrained mobile environment?

## 16. Next Steps

### Immediate Next Steps (Week 07 Preview)

Week 07 focuses on local data persistence using Room database:

1. **Room Architecture**: Entity (data class), DAO (queries), Database (manager)
2. **ScanHistory Entity**: Store prediction results (disease, confidence, timestamp, image path)
3. **History List Screen**: Display past scans in RecyclerView with thumbnails
4. **Detail View**: Show full prediction details for selected history item
5. **Delete Functionality**: Remove individual or all history items
6. **LiveData Integration**: Automatic UI updates when database changes

**Why this matters**: Users want to review past diagnoses, track disease progression over time, and access results offline. Local database is essential for production apps.

### Long-Term Roadmap

- **Week 08**: Firebase integration for cloud storage and cross-device sync
- **Week 09**: Treatment recommendations and agricultural resource links
- **Week 10**: Analytics dashboard showing disease trends in user's scans
- **Week 11**: Offline mode and background processing
- **Week 12**: Final polish, testing, and deployment

### Continuous Improvement

- **Monitor model performance**: Collect user feedback on prediction accuracy (simple thumbs up/down)
- **A/B test preprocessing**: Try different normalization strategies and measure impact on user-reported accuracy
- **Optimize inference speed**: Profile and optimize preprocessing pipeline, consider model quantization
- **Expand class support**: As you gain ML expertise, retrain model with more disease classes
- **Implement explainability**: Add Grad-CAM heatmaps showing which leaf regions influenced prediction (advanced ML topic)

### Skill Development

- **Deepen ML understanding**: Take Andrew Ng's Machine Learning course on Coursera (optional but valuable)
- **Master model deployment**: Learn Docker containerization for reproducible ML deployments
- **Explore mobile ML**: TensorFlow Lite on-device inference (Week 11 preview)
- **Study production ML**: Read "Building Machine Learning Powered Applications" by Emmanuel Ameisen

Week 06 established your core AI functionality. Everything from here builds on this foundation.
