# Week 09: Exercises - TensorFlow Lite Offline AI

## Overview

These exercises are designed to turn the theory from the learning notes into practical Android development skills.
They start with setup and grow toward full offline inference, confidence handling, benchmarking, and cloud comparison.

**Recommended total time:** 9-11 hours

**Recommended working order:**

1. Add dependencies
2. Place the model and labels
3. Initialize the interpreter
4. Preprocess a bitmap correctly
5. Run offline inference
6. Compare cloud and offline results
7. Try GPU delegate
8. Add low-confidence handling
9. Test on multiple images and document results

**Important reminder:**
Every code sample in this week is written in **Java**.
If you are using an existing project, adapt package names as needed.

---

## Exercise 1: Add TensorFlow Lite Dependencies and Verify Gradle Setup (20 min)

**Learning Objective:** Add the correct TensorFlow Lite libraries, sync Gradle successfully, and confirm that your Android project is ready for offline AI.

### Prerequisites

- Android Studio project opens successfully.
- You know how to edit `app/build.gradle`.
- Week 03-08 project builds before changes.

### Step-by-Step Instructions

#### Step 1

Open `app/build.gradle` and add the base TensorFlow Lite dependency. If you want to try GPU later, add the GPU delegate dependency now as well so you do not forget it during benchmarking.

#### Step 2

Sync Gradle and wait for dependency resolution to finish. If the sync fails, read the first real error message instead of scrolling to the bottom and guessing.

#### Step 3

Run **Build -> Make Project**. A successful build confirms that the dependency coordinates are valid and that your Java project is still healthy.

#### Step 4

If you plan to use offline-first plus cloud fallback, keep both the networking dependencies from earlier weeks and the new TensorFlow Lite dependency side by side. They solve different problems.

#### Step 5

Take a screenshot of the successful Gradle sync or build output for your evidence folder. This is useful for Week 09 documentation.

### Complete Code - build.gradle snippet

```java
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    implementation 'org.tensorflow:tensorflow-lite:2.12.0'
    implementation 'org.tensorflow:tensorflow-lite-gpu:2.12.0'

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

### Verification Criteria

- [ ] Gradle sync completes without red errors.
- [ ] Project builds successfully after adding TensorFlow Lite.
- [ ] No duplicate dependency conflict is introduced.
- [ ] You can explain why the GPU dependency is optional.
- [ ] You saved evidence of a successful build or sync.

### Common Mistakes

- **Mistake:** adding the dependency in the project-level Gradle file instead of the app-level module file
- **Mistake:** forgetting to click Sync Now after editing Gradle
- **Mistake:** assuming GPU dependency replaces the base TensorFlow Lite dependency
- **Mistake:** moving forward before confirming the project still builds

### Extension Challenge

If the build succeeds, add a short note to your progress log explaining what each TensorFlow Lite dependency does and why offline inference belongs in Week 09.

---
## Exercise 2: Place `model.tflite` and `labels.txt` in the Assets Folder (35 min)

**Learning Objective:** Create the correct assets structure, add the model and labels files, and verify that Android can package them into the app.

### Prerequisites

- You have a downloaded or converted `.tflite` file.
- You know the class names and their order.
- Exercise 1 is complete.

### Step-by-Step Instructions

#### Step 1

Create the folder `app/src/main/assets/` if it does not already exist. Android Studio may not create it by default in every starter project.

#### Step 2

Copy `model.tflite` into the assets folder. Use a meaningful file name if you want, but remember to keep the same name in your Java code.

#### Step 3

Create `labels.txt` with one label per line. The order must match the output class order used during training or export.

#### Step 4

Rebuild the app. A successful build means the asset packaging step did not break.

#### Step 5

Open the APK contents or the Android Studio Project view and visually confirm the files appear under assets.

### Complete Code - labels loading helper

```java
private List<String> loadLabels(AssetManager assetManager) throws IOException {
    List<String> labels = new ArrayList<>();
    BufferedReader reader = new BufferedReader(
            new InputStreamReader(assetManager.open("labels.txt"))
    );

    String line;
    while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (!line.isEmpty()) {
            labels.add(line);
        }
    }

    reader.close();
    return labels;
}
```

### Verification Criteria

- [ ] `model.tflite` is inside `app/src/main/assets/`.
- [ ] `labels.txt` is inside `app/src/main/assets/`.
- [ ] Labels file contains one label per line.
- [ ] You can explain why label order matters.
- [ ] The project still builds after asset placement.

### Common Mistakes

- **Mistake:** placing the model in `res/` instead of `assets/`
- **Mistake:** including commas or numbering inside `labels.txt` when the classifier expects plain lines
- **Mistake:** using a label order different from the training notebook
- **Mistake:** renaming the asset file but forgetting to update Java code

### Extension Challenge

As an extra check, log the label count in `onCreate()` and compare it with the model's output class count from Python verification.

---
## Exercise 3: Initialize the TensorFlow Lite Interpreter (1 hour)

**Learning Objective:** Load the model safely from assets, create the interpreter, and confirm initialization errors are handled gracefully.

### Prerequisites

- Exercises 1 and 2 are complete.
- You understand how `AssetManager` works.
- You can read Logcat output.

### Step-by-Step Instructions

#### Step 1

Create a new Java class called `TFLiteClassifier` or a similar helper class. Keep model logic separate from UI code so the Activity stays readable.

#### Step 2

Implement `loadModelFile()` using `MappedByteBuffer`. This is the standard Android pattern for reading a TFLite asset efficiently.

#### Step 3

Create the `Interpreter` in a constructor or `init()` method inside a try/catch block. Log a clear error if initialization fails.

#### Step 4

Call your classifier initialization from `MainActivity` and show a Toast or log message confirming success.

#### Step 5

If initialization fails, stop and fix it now. There is no point continuing to preprocessing if the model is not loading.

### Complete Code - interpreter initialization class

```java
public class TFLiteClassifier {
    private static final String TAG = "TFLiteClassifier";
    private Interpreter interpreter;

    public TFLiteClassifier(AssetManager assetManager) throws IOException {
        try {
            interpreter = new Interpreter(loadModelFile(assetManager, "model.tflite"));
            Log.d(TAG, "Interpreter initialized successfully");
        } catch (IOException e) {
            Log.e(TAG, "Model file missing or unreadable", e);
            throw e;
        } catch (Exception e) {
            Log.e(TAG, "Unexpected TensorFlow Lite initialization error", e);
            throw new IOException("Interpreter initialization failed", e);
        }
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }
}
```

### Verification Criteria

- [ ] Interpreter initializes without crashing the app.
- [ ] A missing model file produces a readable error instead of a silent failure.
- [ ] The Activity can create the classifier successfully.
- [ ] You can explain why `MappedByteBuffer` is used.
- [ ] Logcat shows a success or failure message.

### Common Mistakes

- **Mistake:** creating the interpreter on every button click instead of reusing it
- **Mistake:** swallowing exceptions without logging them
- **Mistake:** using the wrong model file name in `openFd()`
- **Mistake:** mixing classifier logic directly into the Activity until debugging becomes difficult

### Extension Challenge

Print the interpreter input and output shapes to Logcat so you begin validating model expectations early.

---
## Exercise 4: Preprocess a `Bitmap` for Model Input (1.5 hours)

**Learning Objective:** Resize the image to the model input shape, extract RGB values, normalize correctly, and build the input tensor expected by the model.

### Prerequisites

- Interpreter initializes successfully.
- You know your model input size.
- You know the required normalization strategy.

### Step-by-Step Instructions

#### Step 1

Choose the correct input size. Many student models use `224 x 224`, but do not guess if your verification script printed something else.

#### Step 2

Resize the bitmap to the input size using `Bitmap.createScaledBitmap()`. This creates a predictable tensor shape for every image.

#### Step 3

Read each pixel, split it into red, green, and blue channels, and normalize using the same rule from training. For this exercise, use `[0, 1]` unless your model card says otherwise.

#### Step 4

Return the preprocessed image as `float[][][][]` for a float model. If your model is quantized, you may later switch to a `ByteBuffer` path.

#### Step 5

Test preprocessing with a known bitmap and log the dimensions or sample pixel values so you know the conversion ran correctly.

### Complete Code - preprocessing method

```java
public float[][][][] preprocessImage(Bitmap bitmap) {
    int inputSize = 224;
    Bitmap resized = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);
    float[][][][] input = new float[1][inputSize][inputSize][3];

    for (int y = 0; y < inputSize; y++) {
        for (int x = 0; x < inputSize; x++) {
            int pixel = resized.getPixel(x, y);
            input[0][y][x][0] = ((pixel >> 16) & 0xFF) / 255.0f;
            input[0][y][x][1] = ((pixel >> 8) & 0xFF) / 255.0f;
            input[0][y][x][2] = (pixel & 0xFF) / 255.0f;
        }
    }

    return input;
}
```

### Verification Criteria

- [ ] Bitmap is resized to the expected width and height.
- [ ] Input tensor shape matches the model's input shape.
- [ ] RGB channel order matches the training pipeline.
- [ ] Normalization uses the correct formula.
- [ ] No crash occurs when preprocessing a normal test image.

### Common Mistakes

- **Mistake:** feeding raw 0-255 integers into a model trained on normalized values
- **Mistake:** swapping width and height indexing
- **Mistake:** accidentally using BGR order when the model expects RGB
- **Mistake:** assuming every model uses `[0, 1]` normalization

### Extension Challenge

Modify the method to support a second normalization option (`[-1, 1]`) behind a boolean flag, then explain when that version would be needed.

---
## Exercise 5: Run Offline Inference and Display the Predicted Label (1 hour)

**Learning Objective:** Use the interpreter to run inference, map the highest output score to a label, and show a readable result in the UI.

### Prerequisites

- Exercises 1-4 are complete.
- You can access a test bitmap from camera or drawable.
- Labels file is correct.

### Step-by-Step Instructions

#### Step 1

Call `preprocessImage()` to build the input tensor.

#### Step 2

Create the output tensor with `float[1][numClasses]`. `numClasses` should match the number of labels you loaded from assets.

#### Step 3

Run `interpreter.run(input, output)` and find the index of the highest value using an `argmax()` helper method.

#### Step 4

Map the winning index to the label list and display both the label and confidence in the Activity.

#### Step 5

Log the full output array in Logcat for one test image. This helps you debug low confidence and label order problems.

### Complete Code - classify method

```java
public String classify(Bitmap bitmap, List<String> labels) {
    float[][][][] input = preprocessImage(bitmap);
    float[][] output = new float[1][labels.size()];

    interpreter.run(input, output);

    int predictedIndex = argmax(output[0]);
    float confidence = output[0][predictedIndex];
    return labels.get(predictedIndex) + " (" + (confidence * 100f) + "%)";
}

private int argmax(float[] values) {
    int maxIndex = 0;
    float maxValue = values[0];
    for (int i = 1; i < values.length; i++) {
        if (values[i] > maxValue) {
            maxValue = values[i];
            maxIndex = i;
        }
    }
    return maxIndex;
}
```

### Verification Criteria

- [ ] Inference runs without exception.
- [ ] Predicted index always stays within label list bounds.
- [ ] Confidence is displayed as a percentage or decimal.
- [ ] A test image produces some output instead of an empty screen.
- [ ] You understand the difference between top-1 label and raw probability array.

### Common Mistakes

- **Mistake:** creating the output array with the wrong class count
- **Mistake:** assuming the first output element is always the correct class
- **Mistake:** displaying the wrong label because of index mismatch
- **Mistake:** forgetting to verify whether the model output is already softmax

### Extension Challenge

Add a method that returns the top-3 predictions instead of only the best class, then compare whether that helps you debug confusing images.

---
## Exercise 6: Compare Cloud and Offline Predictions (1 hour)

**Learning Objective:** Run the same image through both the offline model and the cloud API so you can compare latency, reliability, and prediction differences.

### Prerequisites

- Offline inference works.
- Your earlier cloud API path still works.
- You can toggle internet on/off for testing.

### Step-by-Step Instructions

#### Step 1

Prepare one button or one test path that sends the same bitmap to both the offline classifier and the backend API. Keep the image identical so the comparison is fair.

#### Step 2

Measure offline inference latency locally using `System.currentTimeMillis()` or `System.nanoTime()`.

#### Step 3

Measure cloud latency starting before the network request and ending when the response returns.

#### Step 4

Display or log both outputs, both confidence values if available, and both latency values.

#### Step 5

Test one healthy leaf and one diseased leaf so your comparison is not based on a single case.

### Complete Code - comparison helper

```java
private void compareCloudAndOffline(Bitmap bitmap) {
    long offlineStart = System.currentTimeMillis();
    TFLiteClassifier.ClassificationResult offlineResult = classifier.classify(bitmap);
    long offlineLatency = System.currentTimeMillis() - offlineStart;

    Log.d("Week09", "Offline label: " + offlineResult.getLabel());
    Log.d("Week09", "Offline confidence: " + offlineResult.getConfidence());
    Log.d("Week09", "Offline latency: " + offlineLatency + " ms");

    long cloudStart = System.currentTimeMillis();
    uploadImageToCloud(bitmap, response -> {
        long cloudLatency = System.currentTimeMillis() - cloudStart;
        Log.d("Week09", "Cloud label: " + response.getDisease());
        Log.d("Week09", "Cloud confidence: " + response.getConfidence());
        Log.d("Week09", "Cloud latency: " + cloudLatency + " ms");
    });
}
```

### Verification Criteria

- [ ] The same image is used for both prediction paths.
- [ ] Offline latency is recorded successfully.
- [ ] Cloud latency is recorded successfully.
- [ ] You can describe at least one benefit of each approach.
- [ ] At least two different test images were compared.

### Common Mistakes

- **Mistake:** comparing different images and treating the results as meaningful
- **Mistake:** timing only the offline path but not the cloud path
- **Mistake:** ignoring network instability when discussing cloud latency
- **Mistake:** claiming one mode is always better without evidence

### Extension Challenge

Create a simple table in your report with image name, offline result, cloud result, and latency. This becomes excellent viva evidence.

---
## Exercise 7: Try GPU Delegate and Compare Latency with CPU (1 hour)

**Learning Objective:** Enable GPU acceleration with safe fallback to CPU and benchmark the difference over repeated runs.

### Prerequisites

- The base classifier works on CPU.
- GPU dependency is already added.
- You have access to a real Android device if possible.

### Step-by-Step Instructions

#### Step 1

Add a constructor or factory method that first tries to create the interpreter with `GpuDelegate` and falls back to CPU if the device does not support it.

#### Step 2

Warm up the model before benchmarking. The first few runs are often slower and should not be used as the average result.

#### Step 3

Run inference 100 times with CPU mode and record the average latency.

#### Step 4

Repeat the same test with GPU mode on the same image and record the average latency.

#### Step 5

Write down whether GPU is actually faster on your specific device. Do not assume the answer in advance.

### Complete Code - GPU benchmark helper

```java
private long benchmarkAverage(Bitmap bitmap, int iterations) {
    float[][][][] input = classifier.preprocessImage(bitmap);
    float[][] output = new float[1][labels.size()];

    for (int i = 0; i < 10; i++) {
        interpreter.run(input, output);
    }

    long start = System.currentTimeMillis();
    for (int i = 0; i < iterations; i++) {
        interpreter.run(input, output);
    }
    long total = System.currentTimeMillis() - start;
    return total / iterations;
}
```

### Verification Criteria

- [ ] GPU delegate path is attempted safely.
- [ ] CPU fallback works if GPU initialization fails.
- [ ] A warm-up phase is included.
- [ ] Average latency is calculated from repeated runs.
- [ ] Results are documented, not guessed.

### Common Mistakes

- **Mistake:** benchmarking only a single run and calling it average latency
- **Mistake:** forgetting to keep the same image and iteration count between CPU and GPU tests
- **Mistake:** assuming GPU is available on every emulator or phone
- **Mistake:** crashing the app because GPU delegate errors are not caught

### Extension Challenge

If GPU is unavailable, try `setUseNNAPI(true)` and record how NNAPI compares with CPU on the same device.

---
## Exercise 8: Implement Confidence Threshold and Show `Uncertain` for Low Confidence (45 min)

**Learning Objective:** Prevent misleading predictions by applying a confidence threshold and returning a safer UI message when the model is unsure.

### Prerequisites

- Offline inference works.
- You understand how to read the max confidence score.
- UI already displays the prediction result.

### Step-by-Step Instructions

#### Step 1

Choose an initial threshold such as `0.50f`. This is not universal, but it is a reasonable starting point for a student project.

#### Step 2

After inference, compare the highest probability with your threshold. If the confidence is lower, show a friendly message like `Uncertain - try again`.

#### Step 3

Optionally add guidance such as `Use better lighting` or `Center the leaf in the frame`.

#### Step 4

Test with a clear image and then with a blurry or partially cropped image so you can see the threshold behavior change.

#### Step 5

Discuss in your report that confidence thresholds reduce confidently wrong outputs, even though they may increase the number of uncertain cases.

### Complete Code - threshold-based UI update

```java
private void showPrediction(TFLiteClassifier.ClassificationResult result) {
    float threshold = 0.50f;

    if (!result.isSuccess()) {
        resultTextView.setText(result.getErrorMessage());
        return;
    }

    if (result.getConfidence() < threshold) {
        resultTextView.setText("Uncertain - try again with a clearer image");
    } else {
        String text = result.getLabel() + " (" + String.format(Locale.US, "%.2f", result.getConfidence() * 100f) + "%)";
        resultTextView.setText(text);
    }
}
```

### Verification Criteria

- [ ] Low-confidence results display `Uncertain` instead of a disease name.
- [ ] High-confidence results still show the predicted label.
- [ ] The threshold value is easy to modify for later experiments.
- [ ] You tested both a clear image and a poor-quality image.
- [ ] You can explain why a threshold improves reliability.

### Common Mistakes

- **Mistake:** hardcoding a threshold but never testing what it does in practice
- **Mistake:** using the first output element instead of the maximum output value
- **Mistake:** hiding the confidence completely so the user cannot judge certainty
- **Mistake:** forgetting to update the UI for the error case

### Extension Challenge

Try thresholds `0.40`, `0.50`, and `0.60` on the same set of images and write down which threshold feels most reasonable for your model.

---
## Exercise 9: Test 5 Different Images and Document Accuracy Comparison vs Cloud (1.5 hours)

**Learning Objective:** Evaluate the offline model more realistically by testing multiple images, documenting correctness, and comparing the results with cloud predictions.

### Prerequisites

- Exercises 1-8 are complete.
- You have at least 5 test images representing healthy and diseased leaves.
- You can access both offline and cloud modes.

### Step-by-Step Instructions

#### Step 1

Create a test sheet with at least five images. Include the expected class for each image if you know it from the dataset or filename.

#### Step 2

For each image, record the offline label, offline confidence, offline latency, cloud label, and cloud latency.

#### Step 3

Mark whether the offline result appears correct and whether the cloud result appears correct.

#### Step 4

If a result is wrong, write down a likely cause such as poor lighting, partial leaf visibility, label mismatch, or model limitation.

#### Step 5

Summarize your findings in two or three sentences. Mention whether offline performance is acceptable for a classroom demo and where cloud still seems stronger.

### Complete Code - test result model

```java
public class ImageTestResult {
    private final String imageName;
    private final String expectedLabel;
    private final String offlineLabel;
    private final float offlineConfidence;
    private final long offlineLatencyMs;
    private final String cloudLabel;
    private final float cloudConfidence;
    private final long cloudLatencyMs;

    public ImageTestResult(String imageName,
                           String expectedLabel,
                           String offlineLabel,
                           float offlineConfidence,
                           long offlineLatencyMs,
                           String cloudLabel,
                           float cloudConfidence,
                           long cloudLatencyMs) {
        this.imageName = imageName;
        this.expectedLabel = expectedLabel;
        this.offlineLabel = offlineLabel;
        this.offlineConfidence = offlineConfidence;
        this.offlineLatencyMs = offlineLatencyMs;
        this.cloudLabel = cloudLabel;
        this.cloudConfidence = cloudConfidence;
        this.cloudLatencyMs = cloudLatencyMs;
    }

    public String toSummaryLine() {
        return imageName + " | expected=" + expectedLabel
                + " | offline=" + offlineLabel + " (" + offlineConfidence + ")"
                + " | cloud=" + cloudLabel + " (" + cloudConfidence + ")"
                + " | offlineMs=" + offlineLatencyMs
                + " | cloudMs=" + cloudLatencyMs;
    }
}
```

### Verification Criteria

- [ ] At least five images were tested.
- [ ] A written record exists for each image.
- [ ] Offline vs cloud comparison includes latency and predicted label.
- [ ] At least one observed weakness or limitation is documented honestly.
- [ ] You can summarize whether offline mode is ready for demo use.

### Common Mistakes

- **Mistake:** testing only one image and treating it as full evaluation
- **Mistake:** recording labels without recording confidence or latency
- **Mistake:** changing lighting or image quality drastically between cloud and offline tests
- **Mistake:** ignoring wrong predictions instead of analyzing them

### Extension Challenge

Create a bar chart or simple table for your report showing average cloud latency vs average offline latency across the five images.

---
## Summary

If you completed all nine exercises, you have practiced the full Week 09 offline AI workflow:

- project setup,
- model placement,
- interpreter creation,
- preprocessing,
- inference,
- cloud comparison,
- GPU benchmarking,
- confidence threshold handling,
- multi-image evaluation.

That is enough to support both a good classroom demo and a strong viva explanation.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 09 — Navigation

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
| [⬅ Week 08: XML Disease Library](../week-08-xml-disease-library/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 10: Notifications, Share & Location ➡](../week-10-notifications-share-location/README.md) |

---
