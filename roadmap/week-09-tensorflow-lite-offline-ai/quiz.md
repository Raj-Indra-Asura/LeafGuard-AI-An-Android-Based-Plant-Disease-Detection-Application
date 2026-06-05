# Week 09: Quiz - TensorFlow Lite Offline AI

## Instructions

- Total questions: 30
- Section A: 15 conceptual questions
- Section B: 15 practical questions
- Attempt the quiz before checking the answers section.
- Use it for self-study, peer review, or viva preparation.

---

## Section A - Conceptual Questions

1. **What is TensorFlow Lite primarily designed for?**
   - A. Database management on Android
   - B. On-device machine learning inference
   - C. Cloud-only deep learning training
   - D. Android UI rendering

2. **What is the main difference between training and inference?**
   - A. Training is for Android only, inference is for servers only
   - B. Training learns weights from data, inference uses learned weights to make predictions
   - C. They are the same process with different names
   - D. Inference creates the dataset used for training

3. **Why is TensorFlow Lite useful for LeafGuard AI?**
   - A. It removes the need for Android Studio
   - B. It allows plant disease detection without internet
   - C. It replaces the camera permission system
   - D. It automatically improves all model accuracy

4. **Where should `model.tflite` usually be stored in an Android app?**
   - A. `res/layout/`
   - B. `res/drawable/`
   - C. `assets/`
   - D. `database/`

5. **What is the purpose of `labels.txt`?**
   - A. To store Gradle dependencies
   - B. To map output indices to class names
   - C. To record internet permissions
   - D. To benchmark GPU speed

6. **Why must label order match training order?**
   - A. Because Android requires alphabetical labels
   - B. Because wrong order maps correct output indices to wrong disease names
   - C. Because TensorFlow Lite sorts labels internally
   - D. Because labels change image resolution

7. **Which normalization formula maps pixel values to `[0, 1]`?**
   - A. `pixel * 255.0f`
   - B. `pixel / 255.0f`
   - C. `(pixel / 127.5f) - 1.0f`
   - D. `(pixel - 0.485f) / 0.229f`

8. **Which normalization formula commonly maps pixel values to `[-1, 1]`?**
   - A. `pixel / 255.0f`
   - B. `(pixel / 127.5f) - 1.0f`
   - C. `pixel + 1.0f`
   - D. `pixel * 0.5f`

9. **What is a TensorFlow Lite delegate?**
   - A. A Java interface for RecyclerView
   - B. A hardware-optimized execution path such as GPU or NNAPI
   - C. A text file containing model labels
   - D. A Gradle plugin for Android Studio

10. **What is dynamic range quantization?**
    - A. A way to rotate bitmaps
    - B. A way to reduce model size by quantizing mainly the weights
    - C. A method for improving network speed
    - D. A replacement for labels.txt

11. **Why does full integer quantization need a representative dataset?**
    - A. To calibrate activation ranges during conversion
    - B. To display labels in the UI
    - C. To enable camera permission
    - D. To create XML layouts

12. **What does a confidence threshold help with?**
    - A. It improves Gradle sync speed
    - B. It prevents the app from showing a strong label when the model is unsure
    - C. It removes the need for testing
    - D. It forces the model to be more accurate

13. **Why should TensorFlow Lite inference not run on the main thread?**
    - A. Because Java does not allow loops on the main thread
    - B. Because it can block the UI and harm user experience
    - C. Because labels cannot be loaded on the main thread
    - D. Because the camera API forbids it

14. **Which statement about TensorFlow Lite `Interpreter` thread safety is correct?**
    - A. It is fully thread-safe in all cases
    - B. It should not be used by multiple threads at the same time without protection
    - C. It can only run on the UI thread
    - D. It automatically synchronizes all calls

15. **In a hybrid LeafGuard architecture, when is cloud fallback especially useful?**
    - A. When the offline confidence is low or the model is unavailable
    - B. Only when airplane mode is on
    - C. Only when GPU delegate is active
    - D. Only for loading labels

---

## Section B - Practical Questions

16. **Fill in the dependency:**

```gradle
implementation 'org.tensorflow:______________:2.12.0'
```

17. **Which Java class runs a TensorFlow Lite model?**

```java
______________ interpreter = new Interpreter(modelBuffer);
```

18. **Complete the model loading call:**

```java
AssetFileDescriptor fileDescriptor = assetManager.__________("model.tflite");
```

19. **Complete the preprocessing formula for `[0, 1]` normalization:**

```java
input[0][y][x][0] = ((pixel >> 16) & 0xFF) / __________;
```

20. **Complete the inference call:**

```java
interpreter.__________(input, output);
```

21. **Which helper method is commonly used to find the highest-probability class?**
   - A. `sort()`
   - B. `argmax()`
   - C. `substring()`
   - D. `inflate()`

22. **Write the Java setting that enables NNAPI:**

```java
Interpreter.Options options = new Interpreter.Options();
options.___________________(true);
```

23. **Which Gradle dependency adds GPU delegate support?**
   - A. `org.tensorflow:tensorflow-lite-gpu:2.12.0`
   - B. `org.tensorflow:tensorflow-lite-ui:2.12.0`
   - C. `org.tensorflow:tensorflow-gson:2.12.0`
   - D. `org.tensorflow:tensorflow-lite-room:2.12.0`

24. **Fill in the confidence threshold logic:**

```java
if (confidence < 0.5f) {
    resultTextView.setText("______________");
}
```

25. **Which Java utility is the recommended Week 09 choice for background inference in a Java project?**
   - A. `AsyncTask`
   - B. `ExecutorService`
   - C. `StrictMode`
   - D. `ContentResolver`

26. **Complete the latency measurement:**

```java
long start = System.currentTimeMillis();
// inference
long latency = System.currentTimeMillis() - __________;
```

27. **What is wrong with this code if the model was trained with `[-1, 1]` normalization?**

```java
input[0][y][x][0] = ((pixel >> 16) & 0xFF) / 255.0f;
```

28. **Why is this code risky?**

```java
new Thread(() -> interpreter.run(input, output)).start();
new Thread(() -> interpreter.run(input, output)).start();
```

29. **What should you do if `model.tflite` is missing from assets at runtime?**

30. **Why is benchmarking over 100 runs better than measuring only one inference?**

---

## Answers

1. **B** - TensorFlow Lite is designed for on-device machine learning inference.
2. **B** - Training learns weights; inference uses those learned weights.
3. **B** - It allows plant disease prediction without internet.
4. **C** - `assets/`.
5. **B** - It maps output indices to class names.
6. **B** - Wrong order means wrong labels even if model scores are correct.
7. **B** - `pixel / 255.0f`.
8. **B** - `(pixel / 127.5f) - 1.0f`.
9. **B** - A delegate is a hardware-optimized execution path.
10. **B** - Dynamic range quantization mainly quantizes weights.
11. **A** - It calibrates activation ranges.
12. **B** - It helps avoid misleading low-confidence predictions.
13. **B** - Inference on the main thread can block the UI.
14. **B** - `Interpreter` is not safe for simultaneous unsynchronized multi-threaded use.
15. **A** - Cloud fallback is useful when offline confidence is low or offline mode fails.
16. **tensorflow-lite**
17. **Interpreter**
18. **openFd**
19. **255.0f**
20. **run**
21. **B** - `argmax()`.
22. **setUseNNAPI**
23. **A** - `org.tensorflow:tensorflow-lite-gpu:2.12.0`
24. **Uncertain - try again**
25. **B** - `ExecutorService`
26. **start**
27. The normalization is wrong for that model. The code produces `[0, 1]` input instead of `[-1, 1]`, so predictions may become unreliable.
28. It is risky because `Interpreter` is not thread-safe. Running inference simultaneously on the same interpreter can cause unpredictable behavior or errors.
29. Catch the loading exception, log the error, and show a clear user/developer message such as "Model file not found in assets" instead of crashing silently.
30. Repeated benchmarking reduces warm-up and random variation effects, producing a more reliable average latency value.

---

## Scoring Guide

- **27-30:** Excellent - ready for viva discussion
- **23-26:** Good - revise delegates, preprocessing, and confidence handling
- **18-22:** Fair - review model sourcing, normalization, and threading
- **Below 18:** Re-read learning notes and redo the exercises
