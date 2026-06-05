# Week 06 Quiz: Real ML Model Integration

Test your understanding of machine learning model integration concepts from Week 06. This quiz covers preprocessing, inference, label mapping, confidence scores, error handling, and best practices for integrating pre-trained models into mobile applications.

**Instructions**: Answer all questions. For multiple choice, select the best answer. For code questions, write clean, correct code. For short answer, write 2-4 sentences.

---

## Part 1: Multiple Choice (2 points each)

### Question 1
What is the purpose of adding a batch dimension to a preprocessed image before inference?

A) To compress the image for faster transmission
B) Models expect input shape (batch_size, height, width, channels) even for single images
C) To normalize pixel values to the correct range
D) To convert RGB images to grayscale

**Your answer**: ______

---

### Question 2
Your model was trained with images normalized to [0, 1] range (pixel values divided by 255). You accidentally feed images with pixel values in [0, 255] range. What will most likely happen?

A) The model will crash with a runtime error
B) The model will run but predictions will be poor/random
C) The model will automatically normalize the input
D) The predictions will be identical to correct normalization

**Your answer**: ______

---

### Question 3
A model outputs the following array for a 6-class problem: `[0.05, 0.72, 0.08, 0.10, 0.03, 0.02]`. What is the predicted class index and confidence score?

A) Class 0, confidence 0.05
B) Class 1, confidence 0.72
C) Class 1, confidence 1.00
D) Class 5, confidence 0.02

**Your answer**: ______

---

### Question 4
Why should machine learning models be loaded once at Flask startup rather than inside the request handler?

A) Models cannot be loaded inside functions
B) Loading models takes 5-10 seconds; loading once improves request latency
C) Flask doesn't allow file I/O inside request handlers
D) Multiple model instances improve parallelism

**Your answer**: ______

---

### Question 5
Your model predicts "Tomato Early Blight" with 85% confidence for a corn leaf image. What does this demonstrate?

A) The model is working correctly
B) Confidence score reflects accuracy
C) Models can be confidently wrong on out-of-distribution inputs
D) The corn leaf actually has early blight

**Your answer**: ______

---

### Question 6
What HTTP status code should be returned when a client sends a prediction request without including an image file?

A) 200 OK
B) 400 Bad Request
C) 404 Not Found
D) 500 Internal Server Error

**Your answer**: ______

---

### Question 7
Your model expects RGB images (3 channels), but a user uploads a PNG with alpha channel (RGBA, 4 channels). What should your preprocessing code do?

A) Return an error - RGBA not supported
B) Convert RGBA to RGB by dropping the alpha channel
C) Keep all 4 channels - the model will adapt
D) Convert to grayscale first, then to RGB

**Your answer**: ______

---

### Question 8
What is the main advantage of using a 6-class plant disease model instead of a 38-class model for a learning project?

A) Higher accuracy on all diseases
B) Faster inference time
C) Easier to train with limited resources, demonstrates integration skills
D) Better support for multiple crops

**Your answer**: ______

---

### Question 9
If `predictions = MODEL.predict(img_array)` returns shape `(1, 6)`, how do you extract the predicted class index?

A) `class_idx = predictions[0]`
B) `class_idx = np.argmax(predictions)`
C) `class_idx = np.argmax(predictions[0])`
D) `class_idx = max(predictions)`

**Your answer**: ______

---

### Question 10
What does it mean when a model's confidence is 45% for the top prediction and 38% for the second prediction?

A) The model is highly certain about the top prediction
B) The model is uncertain and both classes are plausible
C) The model is broken and needs retraining
D) The image quality is perfect

**Your answer**: ______

---

## Part 2: True/False (1 point each)

### Question 11
A confidence score of 0.95 (95%) guarantees the prediction is correct.

**Your answer**: ______ (True/False)

---

### Question 12
Image preprocessing steps (resize, normalize) must match the model's training preprocessing exactly.

**Your answer**: ______ (True/False)

---

### Question 13
Loading a model multiple times per second (once per API request) is acceptable for production systems.

**Your answer**: ______ (True/False)

---

### Question 14
Label mapping order must match the model's training class indices, or predictions will be incorrect.

**Your answer**: ______ (True/False)

---

### Question 15
A model trained only on tomato and potato diseases can accurately detect diseases in corn plants.

**Your answer**: ______ (True/False)

---

## Part 3: Code Completion (5 points each)

### Question 16
Complete the preprocessing function to properly prepare an image for a model expecting 224×224 RGB images normalized to [0, 1]:

```python
from PIL import Image
import numpy as np
import io

def preprocess_image(image_bytes):
    # Open image from bytes
    image = Image.open(io.BytesIO(image_bytes))

    # Convert RGBA to RGB if needed
    if image.mode == 'RGBA':
        # YOUR CODE HERE (1 line)


    # Resize to 224x224
    # YOUR CODE HERE (1 line)


    # Convert to numpy array and normalize to [0, 1]
    # YOUR CODE HERE (1 line)


    # Add batch dimension
    # YOUR CODE HERE (1 line)


    return img_array
```

**Your code**:
```python
# RGBA conversion:


# Resize:


# Normalize:


# Batch dimension:

```

---

### Question 17
Complete the error handling for the `/predict` endpoint:

```python
@app.route('/predict', methods=['POST'])
def predict():
    try:
        # Check if image is in request
        # YOUR CODE HERE (3-4 lines)
        # Return 400 error with message "No image provided" if missing





        image_file = request.files['image']
        image_bytes = image_file.read()

        # Preprocess and inference
        img_array = preprocess_image(image_bytes)
        predictions = MODEL.predict(img_array)

        # Decode output
        class_idx = int(np.argmax(predictions[0]))
        confidence = float(predictions[0][class_idx])
        disease_name = LABELS[class_idx]

        return jsonify({
            "status": "success",
            "prediction": {
                "disease": disease_name,
                "confidence": confidence
            }
        })

    except Exception as e:
        # YOUR CODE HERE (2 lines)
        # Return 500 error with generic message


```

**Your code**:
```python
# Missing image check:





# Exception handling:


```

---

### Question 18
Write code to extract and return the top 3 predictions instead of just the top 1:

```python
# Given:
predictions = MODEL.predict(img_array)  # Shape: (1, 6)

# YOUR CODE HERE (5-8 lines)
# Create a list of top 3 predictions with disease names and confidences
# Expected output format:
# [
#     {"disease": "Tomato Early Blight", "confidence": 0.78},
#     {"disease": "Tomato Late Blight", "confidence": 0.15},
#     {"disease": "Tomato Septoria Leaf Spot", "confidence": 0.04}
# ]

top_predictions = []








```

**Your code**:
```python








```

---

## Part 4: Short Answer (5 points each)

### Question 19
Explain the difference between model confidence and model accuracy. Why might a model have 80% confidence but still be wrong?

**Your answer** (2-4 sentences):

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

### Question 20
Your model takes 3 seconds to process each image, which is too slow for production. List three strategies you could use to improve inference speed, and briefly explain each.

**Your answer** (3-6 sentences covering 3 strategies):

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

### Question 21
Why is it important to display confidence scores to users in an AI-powered mobile app? What UI elements could you use to communicate confidence effectively?

**Your answer** (2-4 sentences):

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

### Question 22
You're using a 6-class model (tomato and potato diseases only) but want to expand to support 10 more crops in the future. What would you need to change in your code? What wouldn't need to change?

**Your answer** (3-5 sentences):

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## Part 5: Debugging Scenario (10 points each)

### Question 23
**Scenario**: Your Flask endpoint runs without errors, but every image you test returns the same prediction: "Tomato Healthy" with 99% confidence, even for diseased leaves and non-plant images.

**What are three possible causes of this problem? For each cause, describe how you would diagnose it.**

**Cause 1**:
___________________________________________________________________
**How to diagnose**:
___________________________________________________________________
___________________________________________________________________

**Cause 2**:
___________________________________________________________________
**How to diagnose**:
___________________________________________________________________
___________________________________________________________________

**Cause 3**:
___________________________________________________________________
**How to diagnose**:
___________________________________________________________________
___________________________________________________________________

---

### Question 24
**Scenario**: Your model works fine in Postman tests, but when testing from your Android app, you always get a 500 error. Flask console shows: `ValueError: cannot reshape array of size 150528 into shape (1,224,224,3)`.

**What is the problem? How would you fix it? Show the specific code change needed.**

**Problem explanation**:
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

**Solution code**:
```python




```

---

## Part 6: Design Question (10 points)

### Question 25
**Scenario**: You're building LeafGuard AI for farmers in rural areas with slow internet connections. Each prediction request currently takes 2-3 seconds due to network latency (500ms) + image upload (1500ms) + inference (500ms).

**Design a solution to improve the user experience. Consider:**
- How could you reduce perceived latency?
- What features could work offline?
- How would you handle cases where network is unavailable?
- What data should be cached locally?

Write a brief technical design (8-12 sentences) explaining your approach.

**Your design**:

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## Answer Key (For Instructor Use)

### Part 1: Multiple Choice
1. B
2. B
3. B
4. B
5. C
6. B
7. B
8. C
9. C
10. B

### Part 2: True/False
11. False (high confidence doesn't guarantee correctness)
12. True (preprocessing must match training)
13. False (load once at startup)
14. True (order matters for mapping)
15. False (model only knows trained classes)

### Part 3: Code Completion

**Question 16**:
```python
image = image.convert('RGB')
image = image.resize((224, 224))
img_array = np.array(image, dtype=np.float32) / 255.0
img_array = np.expand_dims(img_array, axis=0)
```

**Question 17**:
```python
if 'image' not in request.files:
    return jsonify({"status": "error", "message": "No image provided"}), 400

return jsonify({"status": "error", "message": "Internal server error"}), 500
```

**Question 18**:
```python
top_k_indices = np.argsort(predictions[0])[-3:][::-1]
for idx in top_k_indices:
    top_predictions.append({
        "disease": LABELS[int(idx)],
        "confidence": float(predictions[0][idx])
    })
```

### Part 4: Short Answer (Key Points)

**Question 19**: Confidence is model's internal certainty, not ground truth. Models can be wrong because they're trained on limited data, face out-of-distribution inputs, or confuse similar-looking classes.

**Question 20**:
1. Use TensorFlow Lite (smaller, optimized models)
2. Deploy on GPU instead of CPU
3. Implement caching for duplicate images

**Question 21**: Transparency builds trust. Users need to know when predictions are uncertain. Use progress bars, color-coded indicators, or explicit percentage text.

**Question 22**: Need to: retrain/replace model with more classes, update label mapping, update recommendations. Don't need to change: preprocessing logic, endpoint structure, error handling, Android networking code.

### Part 5: Debugging

**Question 23**:
1. Model file corrupted/wrong model loaded - verify model by printing predictions for known image
2. Preprocessing broken (all images become identical) - save preprocessed image and visually inspect
3. Label mapping incorrect - test with training set images with known labels

**Question 24**: Image dimensions don't match expected size. 150528 = 224×224×3, but array isn't shaped correctly. Fix: Ensure reshape or expand_dims adds batch dimension properly.

### Part 6: Design (Key Points)
- On-device inference using TensorFlow Lite
- Cache previous predictions locally
- Show loading indicators during upload
- Offline mode with Room database
- Progressive image loading
- Background processing
- Explain network requirements to users

---

## Scoring

**Total Points: 100**

- Part 1 (Multiple Choice): 20 points
- Part 2 (True/False): 5 points
- Part 3 (Code): 15 points
- Part 4 (Short Answer): 20 points
- Part 5 (Debugging): 20 points
- Part 6 (Design): 10 points
- Bonus (if applicable): +5 points for exceptionally thorough answers

**Grading Scale**:
- 90-100: Excellent understanding
- 80-89: Good understanding
- 70-79: Satisfactory understanding
- 60-69: Needs improvement
- Below 60: Significant gaps, review material

---

**Student Name**: _________________________

**Date Completed**: _________________________

**Score**: ______ / 100

**Instructor Comments**:

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________


<!-- NAV_FOOTER_START -->

---

## 📚 Week 06 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| **6** | **quiz.md** ← *You are here* | **Knowledge Assessment Quiz** |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Validation & Verification](validation-checklist.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Knowledge Assessment Quiz** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Reflection & Consolidation →](reflection.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 05: Android Networking](../week-05-android-networking/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 07: Room Database & History ➡](../week-07-room-sqlite-history/README.md) |

---
