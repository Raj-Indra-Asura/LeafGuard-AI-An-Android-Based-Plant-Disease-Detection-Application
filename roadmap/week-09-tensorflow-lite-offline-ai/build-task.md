# Week 09 Build Task: TensorFlow Lite Integration

## Step 1: Obtain .tflite Model (1 hour)

Options:
1. Convert existing model using TensorFlow Lite converter
2. Use pre-trained plant disease model
3. Train simple model for 6 classes

**Save as:** `model.tflite`

---

## Step 2: Create labels.txt (15 min)

```
Tomato_Healthy
Tomato_Late_Blight
Tomato_Early_Blight
Potato_Healthy
Potato_Late_Blight
Potato_Early_Blight
```

One label per line, matching model output order.

---

## Step 3: Add to Assets (15 min)

1. Create `app/src/main/assets/` if needed
2. Copy model.tflite to assets/
3. Copy labels.txt to assets/

---

## Step 4: Add TFLite Dependency (15 min)

```gradle
implementation 'org.tensorflow:tensorflow-lite:2.12.0'
```

Sync Gradle.

---

## Step 5: Implement TFLiteClassifier (3 hours)

Create class with:
- loadModelFile() method
- loadLabels() method
- preprocessImage() method
- classify() method

Reference learning notes for implementation details.

---

## Step 6: Add Mode Selector (1 hour)

In MainActivity, add radio buttons or toggle:
- Cloud Mode
- Offline Mode

On prediction, use selected mode.

---

## Step 7: Measure Latency (30 min)

```java
long startTime = System.currentTimeMillis();
String result = classifier.classify(bitmap);
long latency = System.currentTimeMillis() - startTime;

Log.d(TAG, "Offline prediction took " + latency + "ms");
```

Display latency in UI.

---

## Step 8: Integration Testing (1 hour)

Test:
- Offline mode works without internet
- Results similar to cloud
- Latency displayed
- Mode switching works

---

## Completion Checklist

- [ ] .tflite model in assets/
- [ ] labels.txt in assets/
- [ ] TFLite dependency added
- [ ] Interpreter initializes
- [ ] Preprocessing matches model
- [ ] Inference returns results
- [ ] Mode selector implemented
- [ ] Latency measured
- [ ] Works offline

---

**Success:** Perform prediction in airplane mode.
