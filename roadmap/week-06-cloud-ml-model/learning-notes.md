# Week 06 Learning Notes: Real ML Model Integration

## Core Concepts

### 1. Machine Learning Model as a Software Component

A trained machine learning model is essentially a function: it takes inputs (images, text, numbers) and produces outputs (predictions, classifications, scores). As a mobile engineer, you don't need to understand how this function was created (training process), but you must understand its **interface contract**.

**Key idea**: Treat the model like a library you import. You learn its API (what inputs it expects, what outputs it returns), not its implementation details (neural network architecture, gradient descent).

**Analogy**: Using Google Maps API in your app. You don't need to know how route optimization algorithms work internally - you just need to know how to send coordinates and parse the returned directions.

### 2. The Input Contract: Image Preprocessing

Models are trained on images processed in specific ways. If you feed images in a different format, the model performs poorly even though it technically runs without errors.

**Critical preprocessing steps**:

1. **Resizing**: Models expect fixed dimensions
   - Common sizes: 224×224 (MobileNet, ResNet), 299×299 (InceptionV3), 256×256 (custom models)
   - Why: Neural networks have fixed input layers. Variable sizes don't work.
   - How: `Image.resize((224, 224))` in PIL

2. **Normalization**: Scaling pixel values to expected range
   - Common ranges: [0, 1] (divide by 255), [-1, 1] (divide by 127.5, subtract 1)
   - Why: Training used normalized data. Raw 0-255 values are outside model's learned range.
   - How: `img_array / 255.0` in NumPy

3. **Channel order**: RGB vs BGR
   - Most models expect RGB (Red, Green, Blue)
   - OpenCV uses BGR by default, PIL uses RGB
   - Why: Models learn color patterns. Swapping channels confuses the model (blue sky becomes red).
   - How: Verify your library's default, convert if needed

4. **Batch dimension**: Adding axis for batch processing
   - Single image shape: (224, 224, 3)
   - Model expects: (batch_size, 224, 224, 3)
   - Why: Models are designed to process multiple images at once for efficiency
   - How: `np.expand_dims(img_array, axis=0)` creates (1, 224, 224, 3)

**Mental model**: Preprocessing is like translating a message before sending it to someone who only speaks a specific language. The message (image) is the same, but the format must match what the receiver (model) understands.

### 3. The Output Contract: Prediction Arrays

Models output arrays of numbers, not human-readable text. For a 6-class plant disease model:

```
Input: Image of tomato leaf with spots
Output: [0.02, 0.78, 0.05, 0.08, 0.04, 0.03]
         ↑     ↑
      Class 0  Class 1 (highest probability)
```

Each number represents the model's confidence that the image belongs to that class. The numbers sum to approximately 1.0 (100%) after softmax activation.

**Decoding output**:
1. Find the highest value: `np.argmax(predictions)` → returns index (e.g., 1)
2. Map index to label: `LABELS[1]` → "Tomato Early Blight"
3. Extract confidence: `predictions[0][1]` → 0.78 (78%)

**Important**: The index with highest confidence is the prediction, not the confidence value itself.

### 4. Label Mapping: Bridging Indices and Human Language

Models work with numbers (class indices), users need words (disease names). Label mapping is the dictionary between these two worlds.

**Approach 1: Hardcoded dictionary**
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

**Approach 2: File-based mapping**
```
# labels.txt
Tomato Healthy        # Line 0
Tomato Early Blight   # Line 1
Tomato Late Blight    # Line 2
...
```

```python
with open('labels.txt', 'r') as f:
    LABELS = [line.strip() for line in f.readlines()]
```

**Critical**: The order must exactly match the training data's class order. If training used alphabetical order but you use a different order, predictions will be mismatched.

**Validation strategy**: Test with known images from the training set. If a known "Tomato Early Blight" image predicts "Potato Healthy", your label mapping is wrong.

### 5. Confidence Scores: What They Mean (and Don't Mean)

Confidence is the model's internal certainty, not ground truth accuracy.

**What confidence tells you**:
- 90% confidence: Model is very sure about this prediction (compared to other classes)
- 40% confidence: Model is uncertain, multiple classes seem plausible

**What confidence does NOT tell you**:
- Whether the prediction is actually correct
- Whether the image is even a plant leaf
- Whether the disease class exists in the real world

**Real scenario**: A model trained only on tomato/potato diseases might give 85% confidence "Tomato Late Blight" for a corn leaf image. The model has never seen corn, so it forces a prediction into the closest known class.

**Engineering response**: Implement multi-level confidence thresholds:
- 70-100%: "Detected [disease]"
- 40-70%: "Possible [disease], consider retaking photo"
- 0-40%: "Unable to determine, consult an expert"

Display confidence percentage to users: "Early Blight (72% confidence)". Transparency builds trust.

### 6. Model Loading: Once at Startup, Not Per Request

Loading a model file involves:
1. Reading file from disk (slow: 1-5 seconds for 50-200MB models)
2. Reconstructing neural network layers in memory
3. Allocating GPU/CPU resources
4. Initializing weights

This is expensive. Doing it for every API request adds 5+ seconds of latency.

**Solution**: Load once at Flask startup
```python
# Global scope (runs once when Flask starts)
MODEL = tf.keras.models.load_model('plant_disease_model.h5')
print("Model loaded successfully")

# Request handler (runs for every API call)
@app.route('/predict', methods=['POST'])
def predict():
    predictions = MODEL.predict(img)  # Uses pre-loaded model
```

**Tradeoff**: Higher memory usage (model stays in RAM), but much faster inference (50-200ms vs 5000ms).

**Production consideration**: If memory is extremely limited, implement model unloading/reloading based on request frequency. But for most apps, keeping models loaded is standard.

### 7. Inference Pipeline Architecture

The complete flow from image bytes to prediction:

```
[Android App]
     ↓ (HTTP POST with image file)
[Flask Endpoint] receives request
     ↓
[Extract image bytes] from multipart form data
     ↓
[Decode image] using PIL: bytes → Image object
     ↓
[Preprocess] resize, normalize, batch
     ↓
[Model Inference] feed preprocessed array to model
     ↓
[Decode output] argmax for class, extract confidence
     ↓
[Map to label] class index → disease name
     ↓
[Generate recommendation] based on disease type
     ↓
[Return JSON] structured response with status, prediction, confidence
     ↓
[Android App] parses JSON, updates UI
```

Each step needs error handling:
- Image bytes empty → 400 Bad Request
- Image decode fails (corrupted file) → 400 Bad Request
- Preprocessing fails (unsupported color mode) → 500 Internal Server Error
- Model inference fails (out of memory) → 500 Internal Server Error

### 8. Error Handling Philosophy

Machine learning systems have more failure modes than traditional software:
- Model file missing or corrupted
- Incompatible TensorFlow versions
- Out-of-memory during inference
- Invalid image formats
- Preprocessing edge cases (RGBA vs RGB, grayscale)

**Defensive programming strategy**:

```python
@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Validate request
        if 'image' not in request.files:
            return jsonify({"status": "error", "message": "No image provided"}), 400

        image_file = request.files['image']

        if image_file.filename == '':
            return jsonify({"status": "error", "message": "Empty filename"}), 400

        # Read image
        image_bytes = image_file.read()

        if len(image_bytes) == 0:
            return jsonify({"status": "error", "message": "Empty file"}), 400

        # Preprocess
        try:
            img_array = preprocess_image(image_bytes)
        except Exception as e:
            return jsonify({"status": "error", "message": f"Preprocessing failed: {str(e)}"}), 400

        # Inference
        try:
            predictions = MODEL.predict(img_array)
        except Exception as e:
            return jsonify({"status": "error", "message": f"Inference failed: {str(e)}"}), 500

        # Decode
        class_idx = int(np.argmax(predictions[0]))
        confidence = float(predictions[0][class_idx])
        disease_name = LABELS.get(class_idx, "Unknown")

        # Return success
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

**Key principle**: Fail gracefully with informative error messages. Never crash silently.

### 9. Testing Strategy for ML Endpoints

Traditional software testing: Input A always produces output B.
ML testing: Input A produces output B *most of the time*, but edge cases exist.

**Test categories**:

1. **Happy path tests** (should pass)
   - Valid tomato early blight image → predicts "Tomato Early Blight" with high confidence
   - Valid potato healthy image → predicts "Potato Healthy" with high confidence

2. **Boundary tests** (should handle gracefully)
   - Very small image (10×10 pixels) → resizing should still work
   - Very large image (4000×3000 pixels) → should resize without memory error
   - Grayscale image → should convert to RGB or handle appropriately

3. **Negative tests** (should return errors, not crash)
   - No image in request → 400 error with clear message
   - Text file instead of image → 400 error with "invalid format" message
   - Corrupted image file → 400 error

4. **Edge case tests** (behavior may vary)
   - Non-plant image (cat photo) → returns some prediction (model limitation)
   - Blurry image → lower confidence expected
   - Image with multiple leaves → unpredictable (model trained on single leaf crops)

**Testing tools**:
- Postman for manual testing (upload images, check responses)
- Python unittest for automated testing
- Pytest with parametrize for testing multiple images at once

**Example pytest test**:
```python
import pytest
import requests

def test_valid_prediction():
    with open('test_images/tomato_early_blight.jpg', 'rb') as f:
        response = requests.post('http://localhost:5000/predict', files={'image': f})

    assert response.status_code == 200
    data = response.json()
    assert data['status'] == 'success'
    assert 'prediction' in data
    assert data['prediction']['confidence'] > 0.5  # At least 50% confidence

def test_missing_image():
    response = requests.post('http://localhost:5000/predict')
    assert response.status_code == 400
    assert 'error' in response.json()['status']
```

### 10. Model Limitations and Honest Communication

All models have limitations. Professional engineering means acknowledging and communicating these clearly.

**Common limitations for plant disease models**:

1. **Class limitation**: Only detects diseases in training set
   - Can't detect new diseases
   - Can't say "I don't know" - always forces prediction into known classes

2. **Visual similarity confusion**: Diseases with similar symptoms get confused
   - Early blight vs Late blight (both have leaf spots)
   - Nutrient deficiency vs disease (yellowing looks similar)

3. **Environmental sensitivity**: Performance degrades with lighting/background changes
   - Shadows can be mistaken for disease spots
   - Bright sunlight causes overexposure
   - Cluttered backgrounds distract the model

4. **Image quality dependence**: Blurry, low-resolution, or cropped images reduce accuracy
   - Model trained on high-quality dataset photos
   - Real-world mobile photos vary widely in quality

5. **No context understanding**: Model sees individual images, not progression
   - Can't tell if disease is getting better or worse
   - Can't factor in weather, location, recent treatments

**How to communicate limitations**:

**Bad** (overconfident): "LeafGuard AI instantly diagnoses any plant disease with 99% accuracy!"

**Good** (honest): "LeafGuard AI detects common tomato and potato diseases. For best results, take clear photos in natural light. This tool aids diagnosis but should not replace professional agricultural advice for serious infestations."

**UI elements for transparency**:
- Display confidence percentage prominently
- Add "What factors affect accuracy?" info button
- Show disclaimer: "This is a diagnostic aid, not medical advice"
- Provide link to agricultural extension services for professional help
- Log predictions for future analysis and model improvement

**Professional principle**: Transparency builds user trust more than exaggerated accuracy claims.

### 11. Fallback Strategy: Working with Limited Resources

**Reality**: Training a full 38-class PlantVillage model requires:
- 54,000+ labeled images
- GPU for training (4-8 hours on modern GPU)
- ML expertise for hyperparameter tuning
- Familiarity with TensorFlow/PyTorch training APIs

This is beyond the scope of a mobile development course.

**Pragmatic solution**: Use a simplified 6-class model
- Tomato Healthy, Tomato Early Blight, Tomato Late Blight
- Potato Healthy, Potato Early Blight, Potato Late Blight

**Why this is acceptable**:
1. **Engineering focus**: Your course evaluates mobile app development skills (UI, API integration, database), not ML model accuracy
2. **Proof of concept**: Demonstrates you understand model integration principles, which apply to models of any size
3. **Real-world parallel**: Many products launch with limited features (MVP - Minimum Viable Product) and expand later

**How to implement fallback**:
```python
# 6-class model labels (hardcoded)
LABELS = {
    0: "Tomato Healthy",
    1: "Tomato Early Blight",
    2: "Tomato Late Blight",
    3: "Potato Healthy",
    4: "Potato Early Blight",
    5: "Potato Late Blight"
}

# In UI, clearly state limitations
disclaimer_text = "Currently supports tomato and potato disease detection. More crops coming soon!"
```

**Where to find 6-class models**:
- Kaggle datasets with tomato/potato subsets
- Train yourself with Teachable Machine (https://teachablemachine.withgoogle.com/) using small datasets
- TensorFlow Lite example models

**Documentation requirement**: In your project README, clearly state:
- Number of classes your model supports
- Which crops/diseases are covered
- That this is a focused implementation for educational purposes
- Future plans for expansion (if any)

**Grading note**: A working 6-class system with excellent error handling, UI/UX, and documentation scores higher than a non-functional 38-class attempt. Engineering quality > model size.

### 12. Performance Considerations

**Inference speed targets**:
- < 500ms: Acceptable for real-time apps
- 500-1000ms: Acceptable with loading spinner
- 1000-2000ms: Borderline, users perceive slowness
- \> 2000ms: Too slow, users abandon

**Factors affecting speed**:

1. **Model size**: Larger models (more parameters) take longer to run
   - MobileNet: ~50-100ms on modern CPU
   - ResNet50: ~200-500ms on modern CPU
   - InceptionV3: ~300-800ms on modern CPU

2. **Input resolution**: Higher resolution = more pixels to process
   - 224×224: Fast baseline
   - 299×299: ~1.8× slower
   - 512×512: ~5× slower

3. **Hardware**: CPU vs GPU vs TPU
   - CPU (laptop): 200-500ms typical
   - GPU (NVIDIA): 20-100ms (10× faster)
   - TPU (Google Cloud): 5-20ms (specialized hardware)

4. **Preprocessing overhead**: Image decoding and resizing
   - PIL/Pillow: Fast enough for production (10-50ms)
   - OpenCV: Slightly faster but heavier dependency

**Optimization strategies**:

1. **Use TensorFlow Lite models** (quantized, compressed)
   - 2-4× smaller file size
   - 2-3× faster inference on CPU
   - Slight accuracy tradeoff (usually < 2%)

2. **Lazy loading**: Only load model when first needed
   - Saves memory if prediction endpoint rarely used
   - Adds latency to first request (acceptable tradeoff)

3. **Batch processing**: If multiple images sent together
   - Process as batch instead of one-by-one
   - Model runs faster on batches (GPU utilization)

4. **Caching**: Store predictions for identical images
   - Use image hash (MD5) as cache key
   - Return cached result instantly
   - Be careful with cache size (memory usage)

**For Week 06**: Focus on correctness first, performance second. If inference takes 1-2 seconds, that's fine for development. Optimization can come later.

### 13. TensorFlow vs PyTorch: What You Need to Know

Both are popular deep learning frameworks. Your choice depends on available pre-trained models.

**TensorFlow (with Keras)**:
- **Pros**: Larger ecosystem, more pre-trained models, TensorFlow Lite for mobile, better deployment tools
- **Cons**: More complex API, steeper learning curve
- **Loading model**: `tf.keras.models.load_model('model.h5')`
- **Inference**: `model.predict(img_array)`

**PyTorch**:
- **Pros**: Pythonic API, popular in research, better debugging
- **Cons**: Fewer production deployment tools, larger model files
- **Loading model**: `torch.load('model.pt')` + `model.eval()`
- **Inference**: `model(img_tensor)`

**For Week 06**: Use whichever framework matches your pre-trained model. Don't try to convert between frameworks (complex and error-prone). If you have a TensorFlow model, use TensorFlow. If PyTorch, use PyTorch.

**Minimal TensorFlow setup**:
```python
import tensorflow as tf
import numpy as np

# Load model
model = tf.keras.models.load_model('plant_disease_model.h5')

# Inference
img_array = preprocess_image(image_bytes)  # Shape: (1, 224, 224, 3)
predictions = model.predict(img_array)  # Shape: (1, num_classes)
class_idx = np.argmax(predictions[0])
confidence = predictions[0][class_idx]
```

**Minimal PyTorch setup**:
```python
import torch
import torchvision.transforms as transforms

# Load model
model = torch.load('plant_disease_model.pt')
model.eval()  # Set to evaluation mode (disables dropout, etc.)

# Inference
img_tensor = preprocess_image_pytorch(image_bytes)  # Shape: (1, 3, 224, 224)
with torch.no_grad():  # Disable gradient computation (faster)
    outputs = model(img_tensor)
    _, predicted = torch.max(outputs, 1)
    class_idx = predicted.item()
```

**Key differences**:
- TensorFlow uses (batch, height, width, channels) - channels last
- PyTorch uses (batch, channels, height, width) - channels first
- TensorFlow inference: `model.predict()`
- PyTorch inference: `model(input)` inside `torch.no_grad()` block

Check your model's documentation for the correct framework and preprocessing.

### 14. JSON Response Design for Mobile Clients

Your Android app needs structured, predictable JSON responses.

**Minimal response** (good for Week 06):
```json
{
  "status": "success",
  "prediction": {
    "disease": "Tomato Early Blight",
    "confidence": 0.78
  }
}
```

**Enhanced response** (better for production):
```json
{
  "status": "success",
  "prediction": {
    "disease": "Tomato Early Blight",
    "scientific_name": "Alternaria solani",
    "confidence": 0.78,
    "confidence_level": "high",
    "recommendation": "Remove infected leaves, apply copper-based fungicide, ensure proper plant spacing",
    "severity": "moderate"
  },
  "alternatives": [
    {"disease": "Tomato Late Blight", "confidence": 0.15},
    {"disease": "Tomato Septoria Leaf Spot", "confidence": 0.04}
  ],
  "metadata": {
    "model_version": "v1.2",
    "inference_time_ms": 234,
    "timestamp": "2026-05-25T10:30:45Z"
  }
}
```

**Error response**:
```json
{
  "status": "error",
  "error": {
    "code": "INVALID_IMAGE",
    "message": "Unable to process image file",
    "details": "File format not supported. Please upload JPG or PNG."
  }
}
```

**Design principles**:
1. **Consistent structure**: Always include `status` field ("success" or "error")
2. **Nested objects**: Group related fields (prediction.disease, prediction.confidence)
3. **Descriptive keys**: Use full words (confidence, not conf)
4. **Appropriate types**: Numbers as numbers (0.78), not strings ("0.78")
5. **Extensible**: Easy to add fields later without breaking existing clients

**Android parsing** (Retrofit data class):
```kotlin
data class PredictionResponse(
    val status: String,
    val prediction: Prediction?,
    val error: ErrorInfo?
)

data class Prediction(
    val disease: String,
    val confidence: Double,
    val recommendation: String
)
```

### 15. Debugging Checklist for ML Integration

When predictions seem wrong or errors occur, work through this checklist:

**1. Model loads successfully?**
- [ ] Flask prints "Model loaded" message on startup
- [ ] No version compatibility errors
- [ ] Check model file exists at specified path

**2. Input shape correct?**
- [ ] Print `img_array.shape`, verify it matches `model.input_shape`
- [ ] Batch dimension present? Should be (1, H, W, 3), not (H, W, 3)
- [ ] Channel count correct? Should be 3 for RGB

**3. Normalization correct?**
- [ ] Print `img_array.min()` and `img_array.max()`
- [ ] Should be 0.0 and 1.0 (or -1.0 and 1.0 for some models)
- [ ] If range is 0-255, you forgot to normalize

**4. Label mapping correct?**
- [ ] Test with known training set images
- [ ] Prediction should match ground truth for high-confidence cases
- [ ] If always wrong, order might be reversed or shuffled

**5. Output shape correct?**
- [ ] Print `predictions.shape`, should be (1, num_classes)
- [ ] Print `predictions[0]`, should be array of probabilities
- [ ] Values should sum to ~1.0 (softmax output)

**6. Confidence extraction correct?**
- [ ] `np.argmax(predictions[0])` gives class index (integer)
- [ ] `predictions[0][class_idx]` gives confidence (float 0-1)
- [ ] Don't confuse class index with confidence value

**7. Error handling works?**
- [ ] Send request without image → get 400 error
- [ ] Send text file instead of image → get error (not crash)
- [ ] Check Flask console logs for Python tracebacks

**8. Postman test passes?**
- [ ] Valid image returns 200 status
- [ ] JSON structure matches expected format
- [ ] Confidence value is reasonable (not 0 or 1 for all images)

**Use print() debugging liberally during development. Remove debug prints before production.**

## Summary

Week 06 teaches you to integrate machine learning models as software components:

- **Models are functions**: Input (preprocessed image) → Output (probability array)
- **Preprocessing is critical**: Resize, normalize, batch to match training
- **Label mapping bridges**: Class indices → human-readable names
- **Confidence ≠ accuracy**: Display scores, communicate uncertainty
- **Error handling essential**: ML systems have many failure modes
- **Test thoroughly**: Postman before Android integration
- **Be honest about limitations**: Transparency builds trust
- **Fallback strategies**: 6-class model is acceptable for learning purposes
- **Focus on engineering**: API integration, error handling, UI/UX matter more than model accuracy

You're not becoming a data scientist this week - you're learning to integrate AI as a mobile engineer. This skill applies to any pre-trained model: language models, recommendation systems, computer vision, speech recognition. The principles are the same: understand the contract, handle errors, communicate uncertainty, test thoroughly.


---

## Appendix A: CNN Architecture Theory

Understanding *why* convolutional neural networks work helps you diagnose model problems, choose the right architecture, and explain your project in viva questions.

### What is a Convolutional Neural Network?

A **Convolutional Neural Network (CNN)** is a deep learning architecture specifically designed to process grid-structured data (images). It learns to detect features hierarchically — from simple edges in early layers to complex disease patterns in deeper layers.

**Layer-by-layer progression for a leaf disease image:**

```
Input (224×224×3 pixels)
        │
        ▼
Convolutional Layer 1 — learns edges, colour gradients
  [Feature maps: 112×112×32]
        │
        ▼
Pooling Layer 1 — reduces spatial size, keeps strong features
  [Feature maps: 56×56×32]
        │
        ▼
Convolutional Layer 2 — learns textures, simple shapes
  [Feature maps: 56×56×64]
        │
        ▼
Convolutional Layer 3 — learns complex patterns (lesion shapes)
  [Feature maps: 28×28×128]
        │
        ▼
Global Average Pooling — converts feature maps to 1D vector
  [1280 values]
        │
        ▼
Dense Layer — combines all patterns for classification
  [38 values → softmax → probabilities]
```

### The Convolution Operation

A **convolution** applies a small filter (kernel) across the image, computing dot products at each position. This detects local patterns regardless of where they appear in the image.

**Example: 3×3 edge detection kernel**

```
Input patch (3×3):      Kernel (3×3):       Output (single value):
┌───────────────┐    ┌───────────────┐
│  50  60  70   │    │  -1  -1  -1   │     = (50×-1) + (60×-1) + (70×-1)
│  60  70  80   │  × │   0   0   0   │       + (60×0) + (70×0) + (80×0)
│  70  80  90   │    │   1   1   1   │       + (70×1) + (80×1) + (90×1)
└───────────────┘    └───────────────┘     = -180 + 0 + 240 = 60 (horizontal edge)
```

**Key property**: The same kernel is applied across the entire image (weight sharing). This means the model can detect the same disease spot pattern whether it appears in the top-left or bottom-right of the image.

### Why CNNs Excel at Image Classification

| Property | Benefit for Disease Detection |
|----------|------------------------------|
| **Local connectivity** | Learns local symptoms (spots, lesions) |
| **Weight sharing** | Same pattern recognized anywhere in image |
| **Hierarchical features** | Low-level (colour) → mid-level (texture) → high-level (disease shape) |
| **Translation invariance** | Disease spot in corner == spot in center |

### Pooling: Reducing Spatial Resolution

**Max pooling** keeps the strongest activation in each region, reducing computation:

```
Feature map 4×4:          After 2×2 Max Pooling → 2×2:
┌──────────────────┐      ┌──────────┐
│  1   3   2   4   │      │  3   4   │   ← takes max of each 2×2 block
│  5   6   1   2   │  →   │  8   5   │
│  7   8   1   3   │      └──────────┘
│  2   4   4   5   │
└──────────────────┘
```

Pooling reduces overfitting and makes the model robust to small spatial shifts.

### Transfer Learning: Standing on Giants' Shoulders

**Transfer learning** means starting from a model pre-trained on millions of images (ImageNet) and fine-tuning it for your specific task (plant diseases).

**Why it works**: The lower layers (edge detectors, texture detectors) are universal — they work for any image recognition task. Only the upper layers need to be retrained for the specific classes.

**MobileNetV2 architecture** (used in LeafGuard AI):

```
Pre-trained on ImageNet (1.2M images, 1000 classes)
┌─────────────────────────────────────────────────────┐
│  Feature Extractor (frozen or partially trained)     │
│  Layers 1–150: universal image features             │
└─────────────────────────────────────────────────────┘
                          │
                          ▼  Fine-tune with PlantVillage data
┌─────────────────────────────────────────────────────┐
│  Custom Classifier Head                              │
│  GlobalAveragePooling → Dense(256) → Dense(38)      │
│  Trained from scratch on disease images             │
└─────────────────────────────────────────────────────┘
```

**Training code pattern** (Python/TensorFlow):

```python
import tensorflow as tf

# Load MobileNetV2 WITHOUT the top classifier
base_model = tf.keras.applications.MobileNetV2(
    input_shape=(224, 224, 3),
    include_top=False,      # Remove ImageNet classification head
    weights='imagenet'      # Pre-trained weights
)

# Freeze base model weights (don't train these)
base_model.trainable = False

# Add new classifier head for 38 disease classes
inputs = tf.keras.Input(shape=(224, 224, 3))
x = base_model(inputs, training=False)
x = tf.keras.layers.GlobalAveragePooling2D()(x)
x = tf.keras.layers.Dropout(0.2)(x)
outputs = tf.keras.layers.Dense(38, activation='softmax')(x)

model = tf.keras.Model(inputs, outputs)

# Compile: Adam optimizer, categorical cross-entropy loss
model.compile(
    optimizer=tf.keras.optimizers.Adam(learning_rate=0.001),
    loss='categorical_crossentropy',
    metrics=['accuracy']
)

# Train on PlantVillage dataset
model.fit(
    train_dataset,
    validation_data=val_dataset,
    epochs=30,
    callbacks=[
        tf.keras.callbacks.EarlyStopping(patience=5, restore_best_weights=True),
        tf.keras.callbacks.ModelCheckpoint('best_model.h5', save_best_only=True)
    ]
)
```

**Fine-tuning phase** (optional — can improve accuracy by ~2-5%):

```python
# Unfreeze last 30 layers of base model for fine-tuning
base_model.trainable = True
for layer in base_model.layers[:-30]:
    layer.trainable = False

# Re-compile with lower learning rate (important!)
model.compile(
    optimizer=tf.keras.optimizers.Adam(learning_rate=1e-5),
    loss='categorical_crossentropy',
    metrics=['accuracy']
)

model.fit(train_dataset, epochs=10)
```

### Training Process: Epochs, Batches, Optimizers

**Epoch**: One complete pass through the entire training dataset.
- After each epoch, training accuracy and validation accuracy are reported.
- Typical training: 30–100 epochs.

**Batch**: A subset of training samples processed together before updating weights.
- Batch size 32 = model sees 32 images, computes average loss, then updates weights.
- Smaller batches: noisier updates but better generalization.
- Larger batches: smoother updates but may overfit.

**Training loop visualization**:

```
Epoch 1/30:
  Batch 1 (images 1-32):   forward pass → loss = 3.52 → backprop → update weights
  Batch 2 (images 33-64):  forward pass → loss = 3.48 → backprop → update weights
  ...
  Batch 1687 (last batch): forward pass → loss = 2.21 → backprop → update weights
  
  Epoch summary: train_acc = 0.42, val_acc = 0.38

Epoch 2/30:
  ... (weights carry forward, model is better now)
  Epoch summary: train_acc = 0.61, val_acc = 0.57

...

Epoch 28/30:
  Epoch summary: train_acc = 0.97, val_acc = 0.94  ← Best!
  EarlyStopping patience count: 0
  
Epoch 30/30:
  Epoch summary: train_acc = 0.97, val_acc = 0.93
  EarlyStopping patience count: 2  → Continue...
```

**Adam Optimizer**: Adaptive learning rate optimizer that adjusts learning rates individually for each parameter. Generally the best default choice.

**Loss function: Categorical Cross-Entropy**

For a 38-class classification problem:

```
True label:    [0, 0, 0, ..., 1, ..., 0]   (one-hot, 1 at index 29 = Tomato Early Blight)
Model output:  [0.01, 0.02, 0.01, ..., 0.87, ..., 0.02]  (softmax probabilities)

Cross-entropy loss = -sum(true_label × log(prediction))
                   = -log(0.87)   (only the correct class contributes)
                   = 0.14         (low loss = good prediction)

If model is confused (0.03 for correct class):
Cross-entropy = -log(0.03) = 3.51  (high loss = bad prediction)
```

Loss decreases toward 0 as the model improves. A perfect model has loss = 0.

---

## Appendix B: Evaluation Metrics for Classification Models

Understanding metrics beyond accuracy is critical for honest model evaluation.

### Accuracy vs Precision vs Recall vs F1

**Accuracy**: Percentage of all predictions that are correct.
```
Accuracy = (True Positives + True Negatives) / Total Predictions
```

**Problem with accuracy**: A model that always predicts "Healthy" would achieve ~29% accuracy on PlantVillage (since ~29% of images are healthy). That looks decent but is useless.

**Confusion Matrix** (simplified 3-class example):

```
                Predicted:
                Healthy  Early Blight  Late Blight
Actual:
Healthy           142         3             2
Early Blight        4       138             5
Late Blight         1         6           132
```

**From confusion matrix**:
- **True Positives (TP)**: Diagonal cells — correct predictions for that class
- **False Positives (FP)**: Column sum minus TP — predicted this class, was actually different
- **False Negatives (FN)**: Row sum minus TP — was this class, predicted something else

**Precision**: Of everything the model predicted as "Early Blight", how many actually were?
```
Precision = TP / (TP + FP)  =  138 / (138 + 3 + 6)  =  138 / 147  =  0.939
```

**Recall (Sensitivity)**: Of all actual "Early Blight" cases, how many did the model find?
```
Recall = TP / (TP + FN)  =  138 / (138 + 4 + 5)  =  138 / 147  =  0.939
```

**F1 Score**: Harmonic mean of Precision and Recall (penalizes imbalance between the two):
```
F1 = 2 × (Precision × Recall) / (Precision + Recall)
   = 2 × (0.939 × 0.939) / (0.939 + 0.939)
   = 0.939
```

**When to use each metric for LeafGuard AI**:

| Metric | Use case |
|--------|----------|
| Accuracy | General model performance report |
| Precision | Minimising false alarms (telling healthy plant it's diseased) |
| Recall | Minimising missed detections (missing a disease in an infected plant) |
| F1 | Balanced metric for viva/report |

### Top-1 vs Top-5 Accuracy

**Top-1 accuracy**: The single predicted class is correct. Standard metric.

**Top-5 accuracy**: The correct class appears in the top 5 predictions. Used when there are many similar classes.

```python
# Compute top-5 accuracy
import numpy as np

def top_k_accuracy(predictions, true_label_idx, k=5):
    """
    predictions: (num_classes,) probability array
    true_label_idx: int, index of correct class
    """
    top_k_indices = np.argsort(predictions)[::-1][:k]
    return true_label_idx in top_k_indices

# Example
probs = [0.05, 0.03, 0.08, 0.65, 0.04, 0.15]  # index 3 is highest
print(top_k_accuracy(probs, true_label_idx=5, k=5))  # True (5 is in top-3)
```

For LeafGuard AI, always report Top-1 accuracy since users need a single diagnosis, not a list.

### Overfitting vs Underfitting

```
Training accuracy vs Validation accuracy over epochs:

     100% ─────────────────────────
                    ┌─── train acc
      80% ───────────────────────────────
                         ┌────── val acc
      60%
                              Overfit zone
      40%
      
      0     5     10    15    20   25  epoch

Underfitting (epochs 1-5):  Both train and val accuracy low
Good fit (epoch ~15):        Train and val accuracy both high, close together
Overfitting (epoch 20+):     Train accuracy keeps rising, val accuracy plateaus or drops
```

**Signs of overfitting in LeafGuard AI**:
- Training accuracy 98%, validation accuracy 79%
- Model memorized training images but can't generalize to new photos

**Solutions**:
- **Data augmentation**: Randomly flip, rotate, adjust brightness during training
- **Dropout layers**: Randomly deactivate 20% of neurons during training
- **Early stopping**: Stop training when validation accuracy stops improving
- **L2 regularization**: Penalize large weight values

```python
# Data augmentation example
data_augmentation = tf.keras.Sequential([
    tf.keras.layers.RandomFlip('horizontal'),
    tf.keras.layers.RandomRotation(0.2),
    tf.keras.layers.RandomZoom(0.2),
    tf.keras.layers.RandomContrast(0.2),
])

# Apply at start of model
inputs = tf.keras.Input(shape=(224, 224, 3))
x = data_augmentation(inputs)  # Only active during training
x = base_model(x, training=False)
...
```

### PlantVillage Dataset Statistics

The original PlantVillage dataset used to train models for this project:

| Stat | Value |
|------|-------|
| Total images | 54,309 |
| Number of classes | 38 |
| Images per class (avg) | ~1,429 |
| Most images | Tomato (10 classes, ~18,000 images) |
| Image resolution | Variable (resized to 224×224 for training) |
| Background | White (lab conditions) |
| Collection method | Controlled greenhouse photos |

**Important limitation**: PlantVillage images were taken in controlled lab conditions with white backgrounds. Real-world field photos with soil, other plants, and variable lighting are harder for the model. This is called **domain shift** and is why real apps often perform worse than published accuracy numbers suggest.

---

## Appendix C: All 38 PlantVillage Disease Classes

This is the complete list of classes in the PlantVillage dataset as used by this project.
The index (0-based) must match your model's training class order exactly.

| Index | Label | Crop | Status |
|-------|-------|------|--------|
| 0 | Apple___Apple_scab | Apple | Disease |
| 1 | Apple___Black_rot | Apple | Disease |
| 2 | Apple___Cedar_apple_rust | Apple | Disease |
| 3 | Apple___healthy | Apple | Healthy |
| 4 | Background_without_leaves | — | Non-leaf |
| 5 | Blueberry___healthy | Blueberry | Healthy |
| 6 | Cherry___healthy | Cherry | Healthy |
| 7 | Cherry___Powdery_mildew | Cherry | Disease |
| 8 | Corn___Cercospora_leaf_spot Gray_leaf_spot | Corn | Disease |
| 9 | Corn___Common_rust | Corn | Disease |
| 10 | Corn___healthy | Corn | Healthy |
| 11 | Corn___Northern_Leaf_Blight | Corn | Disease |
| 12 | Grape___Black_rot | Grape | Disease |
| 13 | Grape___Esca_(Black_Measles) | Grape | Disease |
| 14 | Grape___healthy | Grape | Healthy |
| 15 | Grape___Leaf_blight_(Isariopsis_Leaf_Spot) | Grape | Disease |
| 16 | Orange___Haunglongbing_(Citrus_greening) | Orange | Disease |
| 17 | Peach___Bacterial_spot | Peach | Disease |
| 18 | Peach___healthy | Peach | Healthy |
| 19 | Pepper,_bell___Bacterial_spot | Pepper | Disease |
| 20 | Pepper,_bell___healthy | Pepper | Healthy |
| 21 | Potato___Early_blight | Potato | Disease |
| 22 | Potato___healthy | Potato | Healthy |
| 23 | Potato___Late_blight | Potato | Disease |
| 24 | Raspberry___healthy | Raspberry | Healthy |
| 25 | Soybean___healthy | Soybean | Healthy |
| 26 | Squash___Powdery_mildew | Squash | Disease |
| 27 | Strawberry___healthy | Strawberry | Healthy |
| 28 | Strawberry___Leaf_scab | Strawberry | Disease |
| 29 | Tomato___Bacterial_spot | Tomato | Disease |
| 30 | Tomato___Early_blight | Tomato | Disease |
| 31 | Tomato___healthy | Tomato | Healthy |
| 32 | Tomato___Late_blight | Tomato | Disease |
| 33 | Tomato___Leaf_Mold | Tomato | Disease |
| 34 | Tomato___Septoria_leaf_spot | Tomato | Disease |
| 35 | Tomato___Spider_mites Two-spotted_spider_mite | Tomato | Disease |
| 36 | Tomato___Target_Spot | Tomato | Disease |
| 37 | Tomato___Tomato_mosaic_virus | Tomato | Disease |
| 38 | Tomato___Tomato_Yellow_Leaf_Curl_Virus | Tomato | Disease |

**Summary by crop:**
- Apple: 4 classes (3 diseases + 1 healthy)
- Tomato: 10 classes (9 diseases + 1 healthy) ← most common in Indian agriculture
- Potato: 3 classes (2 diseases + 1 healthy) ← second most important
- Corn: 4 classes (3 diseases + 1 healthy)
- Grape: 4 classes (3 diseases + 1 healthy)
- Others: 14 classes

**For beginners**: Start with a 6-class model (Tomato + Potato only) to reduce complexity.

---

## Appendix C: Testing with ngrok (Local Development)

When developing with a physical Android device (not emulator), you need to expose your
local FastAPI server to the internet. **ngrok** creates a public tunnel to your localhost.

### Step 1: Install ngrok
```bash
# macOS
brew install ngrok

# Windows: Download from https://ngrok.com/download

# Linux
sudo snap install ngrok
```

### Step 2: Start FastAPI and ngrok
```bash
# Terminal 1: Start FastAPI
cd backend-api
uvicorn main:app --reload --port 8000

# Terminal 2: Start ngrok tunnel
ngrok http 8000
```

### Step 3: Get the public URL
ngrok will display something like:
```
Forwarding: https://abc123.ngrok.io -> http://localhost:8000
```

### Step 4: Update Android app
In `RetrofitClient.java`, change `BASE_URL`:
```java
// For emulator:
private static final String BASE_URL = "http://10.0.2.2:8000/";

// For physical device via ngrok (replace with your ngrok URL):
private static final String BASE_URL = "https://abc123.ngrok.io/";
```

> ⚠️ ngrok free tier generates a new URL each session. Update BASE_URL each time.
> For stable testing, use your local Wi-Fi IP (e.g., `http://192.168.1.100:8000/`).

### Finding Your Local IP (for same Wi-Fi network)
```bash
# Linux/Mac
ifconfig | grep "inet " | grep -v 127.0.0.1

# Windows
ipconfig | findstr IPv4
```

Then use: `http://192.168.x.x:8000/` as BASE_URL.

---

## Appendix D: Confidence Threshold Guidelines

Choosing the right confidence threshold prevents misleading users:

| Threshold | Meaning | Recommendation |
|-----------|---------|----------------|
| > 0.90 | Very high confidence | Show result as definitive |
| 0.70 – 0.90 | High confidence | Show result with slight caution |
| 0.50 – 0.70 | Medium confidence | Show result + "Please verify" |
| 0.30 – 0.50 | Low confidence | Show top-3 results, ask user |
| < 0.30 | Very low confidence | Show "Unable to detect clearly" |

**Recommended implementation for LeafGuard AI:**

```python
# In FastAPI backend (main.py)
CONFIDENCE_THRESHOLD = 0.50  # Minimum for a definitive prediction

predicted_class = int(np.argmax(predictions[0]))
confidence = float(predictions[0][predicted_class])

if confidence < CONFIDENCE_THRESHOLD:
    return {
        "disease": "Unknown",
        "confidence": confidence,
        "low_confidence": True,
        "recommendation": "Image quality may be too low. Retake photo in better lighting."
    }
```

**For "Healthy" class:**
- High confidence "healthy" (>0.85) = good news, tell the user
- Low confidence "healthy" (<0.60) = uncertain, suggest re-scanning

**Special case: Background_without_leaves (class 4)**
- If model predicts this class, user likely didn't point at a leaf
- Return: "No plant detected. Please capture a clear leaf image."


<!-- NAV_FOOTER_START -->

---

## 📚 Week 06 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| **2** | **learning-notes.md** ← *You are here* | **Theory & Learning Notes** |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Week Overview & Objectives](README.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Theory & Learning Notes** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Practice Exercises →](exercises.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 05: Android Networking](../week-05-android-networking/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 07: Room Database & History ➡](../week-07-room-sqlite-history/README.md) |

---
