# Week 09 Build Task: TensorFlow Lite Offline AI Integration

> **Kotlin-first & accuracy note:** The primary track is **Kotlin** (`android-app-kotlin/.../ml/TFLiteClassifier.kt`); the Java version is the labelled secondary reference. The real `TFLiteClassifier` loads `assets/model.tflite` + `assets/labels.txt`, resizes to **224×224**, converts to **RGB floats 0..1**, and takes **argmax** over 10 outputs. The committed `model.tflite` is a **text placeholder**, so the classifier detects it and uses a **green-channel heuristic fallback** — see the README's *Replacing the placeholder `model.tflite`* section and [`model/model-acquisition-guide.md`](../../model/model-acquisition-guide.md).

This build task turns the learning notes into one guided implementation path for LeafGuard AI.
The goal is to add **offline plant disease prediction in Java** while keeping the app safe, explainable, and easy to demonstrate.

## Build Outcome

By the end of this build task, your app should:

- load a TensorFlow Lite model from assets,
- load class labels from `labels.txt`,
- preprocess a bitmap correctly,
- run inference off the main thread using `ExecutorService`,
- display predicted label, confidence, and latency,
- show `Uncertain` for low-confidence cases,
- try GPU delegate with CPU fallback,
- still support comparison with the cloud version.

## Before You Start

Confirm these prerequisites:

- [ ] Week 08 cloud mode still works.
- [ ] You have a `.tflite` model.
- [ ] You have a matching `labels.txt` file.
- [ ] You know the model input size and normalization strategy.
- [ ] Your Android Studio project builds successfully.

---

## Step 1: Acquire or Export a Plant Disease Model (1-2 hours)

**Goal:** Choose a realistic model source and produce a tested `model.tflite` file plus `labels.txt`.

### Why this step matters

If you start coding before validating the model source, you may spend hours debugging an Android problem that is really a model problem. This step reduces that risk early.

### Detailed Sub-Steps

1. Choose one sourcing path: TensorFlow Hub tutorial, Google Colab training, or TensorFlow Lite Model Maker.
2. If you want an official learning route, use `https://www.tensorflow.org/hub/tutorials/cropnet_on_device` and adapt it to PlantVillage classes.
3. If you want a faster student workflow, train or fine-tune a small classifier in Colab using the PlantVillage dataset from Kaggle or TensorFlow Datasets.
4. Export `model.tflite` and generate `labels.txt` in the same class order used during training.
5. Run a Python verification script to print input shape, output shape, and dtype before touching Android code.
6. Record the final model size and the normalization strategy used during training.

### Reference Code

```java
// Java note: model acquisition is done in Python/Colab, but Android uses the exported files.
// Expected final assets:
// model.tflite
// labels.txt
```

### Verification

- [ ] `model.tflite` exists and opens in Python TFLite Interpreter.
- [ ] `labels.txt` exists and class order is documented.
- [ ] Input shape is known.
- [ ] Normalization rule is known.
- [ ] Model size is reasonable for a mobile app.

### Common Risks

- Downloading a model without checking whether it is actually a TFLite image classifier
- Forgetting to preserve label order from training
- Skipping model verification and assuming Android will reveal all problems

---
## Step 2: Add Model and Labels to Android Assets (20 min)

**Goal:** Place the model and labels in the correct folder so the app can package them into the APK.

### Why this step matters

TensorFlow Lite models are normal app assets from Android's point of view. If the files are not in the correct folder, offline mode cannot start.

### Detailed Sub-Steps

1. Create `app/src/main/assets/` if it does not exist.
2. Copy `model.tflite` into the assets folder.
3. Copy `labels.txt` into the same assets folder.
4. Rebuild the app and confirm there are no packaging errors.
5. Log or document the exact asset file names you will use in code.

### Reference Code

```java
// Example asset names used in the Java code below:
private static final String MODEL_PATH = "model.tflite";
private static final String LABELS_PATH = "labels.txt";
```

### Verification

- [ ] Assets folder exists.
- [ ] Model file exists inside assets.
- [ ] Labels file exists inside assets.
- [ ] Rebuild succeeds after asset placement.

### Common Risks

- Using `res/raw` in some places and `assets` in others
- Renaming asset files without updating the constants in code

---
## Step 3: Add TensorFlow Lite Dependencies (15 min)

**Goal:** Configure Gradle with the base runtime and optional GPU support.

### Why this step matters

The interpreter and delegates come from external libraries, so the project cannot compile without these dependencies.

### Detailed Sub-Steps

1. Open module-level `app/build.gradle`.
2. Add the base TensorFlow Lite dependency.
3. Add GPU delegate dependency so Week 09 benchmarking is possible.
4. Sync Gradle and then run Make Project.
5. Save a screenshot of a successful build for evidence.

### Reference Code

```java
dependencies {
    implementation 'org.tensorflow:tensorflow-lite:2.12.0'
    implementation 'org.tensorflow:tensorflow-lite-gpu:2.12.0'
}
```

### Verification

- [ ] Gradle sync successful.
- [ ] Project builds successfully.
- [ ] No missing import errors for TensorFlow Lite classes.

### Common Risks

- Adding dependencies in the wrong Gradle file
- Forgetting to sync before troubleshooting unrelated code

---
## Step 4: Create the `TFLiteClassifier` Helper Class (2 hours)

**Goal:** Separate model loading, label loading, preprocessing, and inference logic from the Activity.

### Why this step matters

This keeps your UI code readable and makes testing easier. It also reflects better software engineering for CSE 2206 project quality.

### Detailed Sub-Steps

1. Create a new Java class, ideally inside a package like `ml` or `classifier`.
2. Implement `loadModelFile()` using `MappedByteBuffer`.
3. Implement `loadLabels()` using `BufferedReader`.
4. Create the `Interpreter` in the constructor or an initialization method.
5. Add a `close()` method so resources can be released later.

### Reference Code

```java
public class TFLiteClassifier implements AutoCloseable {
    private final Interpreter interpreter;
    private final List<String> labels;

    public TFLiteClassifier(AssetManager assetManager) throws IOException {
        labels = loadLabels(assetManager, "labels.txt");
        interpreter = new Interpreter(loadModelFile(assetManager, "model.tflite"));
    }

    @Override
    public void close() {
        interpreter.close();
    }
}
```

### Verification

- [ ] Classifier class compiles.
- [ ] Model loads without crash.
- [ ] Labels load without crash.
- [ ] Resources can be closed when needed.

### Common Risks

- Keeping all ML logic inside the Activity
- Ignoring initialization exceptions and leaving the app in an unknown state

---
## Step 5: Implement Correct Image Preprocessing (1.5 hours)

**Goal:** Resize the leaf image and convert it into the numeric tensor expected by the model.

### Why this step matters

Most TensorFlow Lite integration bugs come from preprocessing mismatches, not from the `run()` call itself.

### Detailed Sub-Steps

1. Read the expected input shape from your model verification step.
2. Resize the incoming `Bitmap` to match the model dimensions.
3. Convert each pixel to RGB values.
4. Apply the normalization rule used during training. For many student models this is `[0, 1]`.
5. Return a 4D float array or switch to `ByteBuffer` if your model type requires it.

### Reference Code

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

### Verification

- [ ] Input tensor shape matches the model.
- [ ] Normalization matches training.
- [ ] No crashes occur when processing a typical image.

### Common Risks

- Using raw 0-255 values when the model expects normalized floats
- Assuming RGB order without checking the training notebook

---
## Step 6: Run Inference and Return Structured Results (1 hour)

**Goal:** Execute the model and convert raw output scores into a label plus confidence.

### Why this step matters

A clean result object makes UI updates, threshold handling, and cloud comparison much easier.

### Detailed Sub-Steps

1. Create the input tensor from a bitmap.
2. Create the output array using the number of labels as the class count.
3. Run `interpreter.run(input, output)`.
4. Find the maximum score using `argmax()`.
5. Return label, confidence, and optionally top-3 predictions in a result object.

### Reference Code

```java
public ClassificationResult classify(Bitmap bitmap) {
    float[][][][] input = preprocessImage(bitmap);
    float[][] output = new float[1][labels.size()];

    interpreter.run(input, output);

    int index = argmax(output[0]);
    return new ClassificationResult(labels.get(index), output[0][index]);
}
```

### Verification

- [ ] Inference completes successfully.
- [ ] Predicted label matches one item from `labels.txt`.
- [ ] Confidence value is visible in logs or UI.

### Common Risks

- Creating the output array with the wrong class count
- Forgetting to validate index bounds when mapping labels

---
## Step 7: Move Inference Off the Main Thread Using `ExecutorService` (45 min)

**Goal:** Prevent UI freezes by performing TensorFlow Lite work in the background and returning results safely to the main thread.

### Why this step matters

Inference can take hundreds of milliseconds on CPU, which is enough to make the UI feel blocked if you run it directly in the button click handler.

### Detailed Sub-Steps

1. Create a single-thread `ExecutorService` in your Activity or ViewModel-equivalent class.
2. Inside the executor task, start a timer, call `classifier.classify(bitmap)`, and record the latency.
3. Use `runOnUiThread()` to update the UI after the background work finishes.
4. Disable the prediction button during inference and re-enable it when the result is ready.
5. Shut down the executor when the Activity is destroyed if you created it there.

### Reference Code

```java
private final ExecutorService executorService = Executors.newSingleThreadExecutor();

private void runOfflinePrediction(Bitmap bitmap) {
    predictButton.setEnabled(false);
    progressBar.setVisibility(View.VISIBLE);

    executorService.execute(() -> {
        long start = System.currentTimeMillis();
        TFLiteClassifier.ClassificationResult result = classifier.classify(bitmap);
        long latencyMs = System.currentTimeMillis() - start;

        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            predictButton.setEnabled(true);
            resultTextView.setText(result.getLabel());
            latencyTextView.setText(latencyMs + " ms");
        });
    });
}
```

### Verification

- [ ] No heavy inference work runs in the button click handler on the UI thread.
- [ ] Progress indicator appears during inference.
- [ ] UI updates happen only on the main thread.
- [ ] The app remains responsive while predicting.

### Common Risks

- Calling `setText()` directly from the background thread
- Leaving the button enabled so multiple inferences stack up

---
## Step 8: Implement Confidence Threshold in the UI (30-45 min)

**Goal:** Prevent misleading results when the model is unsure.

### Why this step matters

A production-minded app should not always pretend to know the answer. Low-confidence handling improves trust and creates better discussion for your viva.

### Detailed Sub-Steps

1. Choose an initial threshold such as `0.50f`.
2. After inference, compare the best class confidence with that threshold.
3. If the score is lower, show `Uncertain - try again`.
4. Optionally add advice about lighting, focus, and leaf positioning.
5. Test the behavior using one clear image and one difficult image.

### Reference Code

```java
private void updatePredictionUi(TFLiteClassifier.ClassificationResult result) {
    float threshold = 0.50f;

    if (!result.isSuccess()) {
        resultTextView.setText(result.getErrorMessage());
        return;
    }

    if (result.getConfidence() < threshold) {
        resultTextView.setText("Uncertain - try again with a clearer leaf image");
    } else {
        resultTextView.setText(result.getLabel());
    }
}
```

### Verification

- [ ] Clear images with high confidence still show the predicted class.
- [ ] Low-confidence images show an uncertainty message.
- [ ] Threshold value is easy to tune.

### Common Risks

- Using a threshold without logging confidence during testing
- Displaying `Uncertain` but giving no guidance to the user

---
## Step 9: Add GPU Delegate with CPU Fallback (1 hour)

**Goal:** Improve performance where possible without breaking compatibility on devices that cannot use the GPU path.

### Why this step matters

Delegates can speed up inference, but hardware support varies. Fallback logic makes the app more robust.

### Detailed Sub-Steps

1. Create `Interpreter.Options` and attempt to add a `GpuDelegate`.
2. Wrap GPU initialization in a try/catch block.
3. If GPU creation fails, create a normal CPU interpreter instead.
4. Log which path was used so you can explain benchmark results later.
5. Benchmark both CPU and GPU paths over repeated runs.

### Reference Code

```java
private Interpreter createInterpreterWithFallback(AssetManager assetManager) throws IOException {
    MappedByteBuffer modelBuffer = loadModelFile(assetManager, "model.tflite");

    try {
        Interpreter.Options options = new Interpreter.Options();
        options.addDelegate(new GpuDelegate());
        options.setNumThreads(4);
        Log.d("Week09", "Using GPU delegate");
        return new Interpreter(modelBuffer, options);
    } catch (Exception e) {
        Log.w("Week09", "GPU failed, falling back to CPU", e);
        Interpreter.Options cpuOptions = new Interpreter.Options();
        cpuOptions.setNumThreads(4);
        return new Interpreter(modelBuffer, cpuOptions);
    }
}
```

### Verification

- [ ] GPU path is attempted.
- [ ] CPU fallback works if GPU fails.
- [ ] A log message reveals which path is active.
- [ ] Average latency was measured, not guessed.

### Common Risks

- crashing on devices where the GPU delegate is unavailable
- forgetting to keep the same test conditions for CPU and GPU comparison

---
## Step 10: Integration Testing and Offline Demo Validation (1-1.5 hours)

**Goal:** Confirm that offline mode behaves correctly in realistic use, including airplane mode and comparison against cloud mode.

### Why this step matters

This is the step that proves Week 09 is complete. A model that loads but is never validated is not enough.

### Detailed Sub-Steps

1. Test offline inference with internet enabled so you can compare it with cloud mode first.
2. Turn on airplane mode and verify offline prediction still works.
3. Test at least five images and record the results.
4. Record CPU latency and, if available, GPU latency.
5. Take screenshots of the UI showing label, confidence, and latency.
6. Update your progress log and evidence folder with benchmark notes and test outcomes.

### Reference Code

```java
private void logTestSummary(String imageName,
                            String expectedLabel,
                            TFLiteClassifier.ClassificationResult offlineResult,
                            long offlineLatencyMs) {
    Log.d("Week09Summary", imageName
            + " | expected=" + expectedLabel
            + " | offline=" + offlineResult.getLabel()
            + " | confidence=" + offlineResult.getConfidence()
            + " | latencyMs=" + offlineLatencyMs);
}
```

### Verification

- [ ] Offline mode works in airplane mode.
- [ ] At least five images were tested.
- [ ] Latency measurements are documented.
- [ ] Confidence threshold behavior is demonstrated.
- [ ] Evidence folder contains screenshots or logs.

### Common Risks

- declaring the feature complete after one successful image
- testing only with internet on and never validating real offline behavior

---
## Final Completion Checklist

Mark this build task complete only when all of the following are true:

- [ ] Model acquisition path documented.
- [ ] `model.tflite` verified outside Android.
- [ ] `labels.txt` created and order verified.
- [ ] Model and labels placed in assets.
- [ ] TensorFlow Lite dependencies added.
- [ ] `TFLiteClassifier` implemented.
- [ ] Preprocessing matches training.
- [ ] Inference runs successfully.
- [ ] Result label and confidence are displayed.
- [ ] Inference runs off the main thread using `ExecutorService`.
- [ ] Confidence threshold implemented.
- [ ] GPU delegate attempted with CPU fallback.
- [ ] CPU and GPU or NNAPI benchmarks recorded.
- [ ] Offline mode works in airplane mode.
- [ ] At least 5 images tested and documented.
- [ ] Evidence folder updated.
- [ ] Progress log updated.

## Success Definition

You can confidently say Week 09 is complete when you can demonstrate this sequence live:

1. capture or load a leaf image,
2. run offline prediction without freezing the UI,
3. display label, confidence, and latency,
4. show `Uncertain` on a difficult image,
5. switch off internet and prove offline mode still works,
6. explain why preprocessing and label order matter.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 09 — Navigation

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
| [⬅ Week 08: XML Disease Library](../week-08-xml-disease-library/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 10: Notifications, Share & Location ➡](../week-10-notifications-share-location/README.md) |

---
