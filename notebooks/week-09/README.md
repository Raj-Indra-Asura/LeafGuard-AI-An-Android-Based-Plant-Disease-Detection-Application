# Week 09 Interactive Notebook

## Making LeafGuard Work Offline

> Work through this Markdown notebook like a lab manual: read, run, test, and explain each checkpoint in your own words.

### How to use this notebook

- Follow the cells in order.
- Use Java for Android code and Python only for backend/model tooling.
- Save screenshots and logs as evidence for CSE 2206.
- Keep the roadmap folder for this week open while you work.

### Weekly outcomes

- Load a `.tflite` model from app assets and run inference on-device.
- Preprocess images to 224x224 with correct normalization.
- Switch between online API mode and offline TFLite mode.

### Repository references

- `roadmap/week-09-tensorflow-lite-offline-ai/`
- `solutions/week-09/`
- `model/model-notes.md`

---

## Notebook Cell 1 — Understand TensorFlow Lite

### Explanation

- TensorFlow Lite is an optimized runtime for mobile and embedded devices.
- It allows LeafGuard AI to keep basic prediction capability even when the network is unavailable.

### 🔵 Try This

- Write one advantage and one limitation of offline inference compared with the cloud API.

### Expected Output

- You can explain why online and offline modes can coexist in one project.

### ✅ Checkpoint

- Why might offline inference be faster but still less flexible than cloud inference?

### ⚠️ Common Mistake

- Do not assume offline mode removes the need for a backend entirely; both can still be useful.

### 📌 Key Point

- Offline AI increases resilience and responsiveness.

## Notebook Cell 2 — Place the model inside assets/

### Explanation

- Android assets let you package raw files such as models and labels with the app.

### Code to Read / Run

```gradle
android {
    aaptOptions {
        noCompress "tflite"
    }
}
```

### 🔵 Try This

- Place `leafguard_model.tflite` and `labels.txt` inside `app/src/main/assets/`.

### Expected Output

- The build system includes the model file without compressing it.

### ✅ Checkpoint

- Why is `noCompress` useful for `.tflite` files?

### ⚠️ Common Mistake

- If the asset path is wrong, the interpreter will fail to load the model.

### 📌 Key Point

- Packaging details matter for model loading.

## Notebook Cell 3 — Initialize the TFLite interpreter

### Explanation

- Interpreter setup should happen once and be reused for multiple inferences.

### Code to Read / Run

```java
public class TFLiteClassifier {
    private final Interpreter interpreter;
    private final List<String> labels;

    public TFLiteClassifier(Context context) throws IOException {
        interpreter = new Interpreter(loadModelFile(context, "leafguard_model.tflite"));
        labels = loadLabels(context, "labels.txt");
    }

    private MappedByteBuffer loadModelFile(Context context, String fileName) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(fileName);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
```

### 🔵 Try This

- Add logging around classifier creation so you can confirm model load success once at startup.

### Expected Output

- The app can create a reusable classifier instance.

### ✅ Checkpoint

- Why is a mapped byte buffer used instead of reading the whole file into a normal string or byte array manually?

### ⚠️ Common Mistake

- Do not recreate the interpreter before every prediction unless you want slower performance.

### 📌 Key Point

- Initialization should be separated from inference.

## Notebook Cell 4 — Preprocess images to 224x224 and normalize

### Explanation

- Most plant disease models expect a fixed input size and floating-point normalized pixel values.

### Code to Read / Run

```java
public ByteBuffer preprocess(Bitmap bitmap) {
    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
    ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
    buffer.order(ByteOrder.nativeOrder());

    int[] pixels = new int[224 * 224];
    resizedBitmap.getPixels(pixels, 0, 224, 0, 0, 224, 224);

    for (int pixel : pixels) {
        float red = ((pixel >> 16) & 0xFF) / 255.0f;
        float green = ((pixel >> 8) & 0xFF) / 255.0f;
        float blue = (pixel & 0xFF) / 255.0f;
        buffer.putFloat(red);
        buffer.putFloat(green);
        buffer.putFloat(blue);
    }
    buffer.rewind();
    return buffer;
}
```

### 🔵 Try This

- Check `model/model-notes.md` and confirm that your model really expects `[0,1]` normalization.

### Expected Output

- The bitmap becomes a model-ready input tensor.

### ✅ Checkpoint

- What would go wrong if the model expected `[-1,1]` normalization instead?

### ⚠️ Common Mistake

- Do not guess preprocessing rules; verify them from model documentation or training code.

### 📌 Key Point

- Preprocessing must match training exactly.

## Notebook Cell 5 — Run inference and interpret the output array

### Explanation

- Most classification models return a 2D float array shaped like `[1][numClasses]`.

### Code to Read / Run

```java
public PredictionResult classify(Bitmap bitmap) {
    ByteBuffer inputBuffer = preprocess(bitmap);
    float[][] output = new float[1][38];
    interpreter.run(inputBuffer, output);

    int bestIndex = 0;
    float bestConfidence = output[0][0];
    for (int i = 1; i < output[0].length; i++) {
        if (output[0][i] > bestConfidence) {
            bestConfidence = output[0][i];
            bestIndex = i;
        }
    }
    return new PredictionResult(labels.get(bestIndex), bestConfidence);
}
```

### 🔵 Try This

- Print the top 3 scores instead of only the top 1 result as a debugging exercise.

### Expected Output

- The classifier returns the best label and confidence score.

### ✅ Checkpoint

- Why must the output array length match the label list length?

### ⚠️ Common Mistake

- If the model outputs 38 values but you load only 37 labels, predictions become meaningless.

### 📌 Key Point

- Inference is only useful when output interpretation is correct.

## Notebook Cell 6 — Switch between online and offline mode

### Explanation

- A practical app may use API mode when internet is available and TFLite mode otherwise.

### Code to Read / Run

```java
if (isInternetAvailable()) {
    uploadLeafImage(imageFile);
} else {
    PredictionResult localResult = tfliteClassifier.classify(bitmap);
    showOfflineResult(localResult);
}
```

### 🔵 Try This

- Put the emulator in airplane mode and verify the offline branch works.

### Expected Output

- LeafGuard AI remains usable without a network connection.

### ✅ Checkpoint

- What advantage does a dual-mode architecture give your demo and real-world usage?

### ⚠️ Common Mistake

- Do not leave the user wondering which mode is active; show a label like “Offline Result”.

### 📌 Key Point

- Mode awareness improves trust and debugging.

## Notebook Cell 7 — Optimize performance

### Explanation

- On-device inference should feel responsive, especially on mid-range phones.

### Step-by-Step

1. Reuse the interpreter instead of creating it repeatedly.
2. Resize images before inference instead of feeding oversized bitmaps through repeated conversions.
3. Measure elapsed time for preprocessing and inference separately.
4. Consider lower-precision or optimized models if latency is too high.

### 🔵 Try This

- Log the inference time for five runs and compute the average.

### Expected Output

- You get a repeatable sense of on-device model speed.

### ✅ Checkpoint

- Which is slower on your device: preprocessing or actual inference?

### ⚠️ Common Mistake

- Optimization without measurement is just guessing.

### 📌 Key Point

- Performance is a user experience feature.

## Mini Quiz

- What problem does this week solve inside LeafGuard AI?
- Which Java class or Android component did you touch first?
- Which file path in this repository is most relevant to this week?
- What would break if you skipped the validation step?
- How does this week connect to the three-tier architecture?

## Evidence Checklist

- [ ] Capture a screenshot of the completed screen or terminal output.
- [ ] Save one code snippet that proves the feature is wired correctly.
- [ ] Write two sentences in your progress log about what you learned.
- [ ] Record at least one bug and the exact fix you applied.
- [ ] Commit working changes before moving to the next week.

## Reflection Prompt

- Explain the feature from memory without reading the code.
- State one improvement you would add after submission.
- Identify one risk if this feature were left untested.

## Next Step

- Continue to **Week 10** when this week is stable and documented.
