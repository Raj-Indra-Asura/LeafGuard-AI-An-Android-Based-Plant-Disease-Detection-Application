# Week 09 Solution - TensorFlow Lite Classifier

## Goal
In Week 09, students move from cloud-only prediction to offline inference using TensorFlow Lite. The classifier must:
- load the `.tflite` model from Android assets
- preprocess a `Bitmap`
- run inference through `Interpreter`
- find the best class with `argmax`
- return a structured prediction result

This solution uses `[0, 1]` normalization.

---

## Full `TFLiteClassifier.java`

```java
package com.leafguard.ml;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.leafguard.network.PredictionResponse;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TFLiteClassifier implements AutoCloseable {

    private static final int PIXEL_SIZE = 3;
    private static final int BYTES_PER_CHANNEL = 4;

    private final int inputSize;
    private final List<String> labels = new ArrayList<>();
    private Interpreter interpreter;
    private int outputClasses = 1;

    public TFLiteClassifier(Context context) throws IOException {
        this(context, "model.tflite", "labels.txt", 224);
    }

    public TFLiteClassifier(Context context, String modelAssetName, String labelsAssetName, int inputSize)
            throws IOException {
        this.inputSize = inputSize;
        loadModel(context, modelAssetName);
        labels.addAll(loadLabels(context, labelsAssetName));
        if (labels.isEmpty()) {
            labels.add("Unknown disease");
        }
        while (labels.size() < outputClasses) {
            labels.add(String.format(Locale.getDefault(), "Class %d", labels.size()));
        }
    }

    private void loadModel(Context context, String modelAssetName) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelAssetName);
        try (FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor())) {
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);

            Interpreter.Options options = new Interpreter.Options();
            options.setNumThreads(4);
            interpreter = new Interpreter(mappedByteBuffer, options);

            int[] outputShape = interpreter.getOutputTensor(0).shape();
            if (outputShape.length > 1) {
                outputClasses = outputShape[1];
            }
        }
    }

    private List<String> loadLabels(Context context, String labelsAssetName) throws IOException {
        List<String> loadedLabels = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open(labelsAssetName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                    loadedLabels.add(trimmed);
                }
            }
        }
        return loadedLabels;
    }

    public ByteBuffer preprocessImage(Bitmap bitmap) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(
                inputSize * inputSize * PIXEL_SIZE * BYTES_PER_CHANNEL
        ).order(ByteOrder.nativeOrder());

        for (int y = 0; y < inputSize; y++) {
            for (int x = 0; x < inputSize; x++) {
                int pixel = scaledBitmap.getPixel(x, y);
                inputBuffer.putFloat(Color.red(pixel) / 255.0f);
                inputBuffer.putFloat(Color.green(pixel) / 255.0f);
                inputBuffer.putFloat(Color.blue(pixel) / 255.0f);
            }
        }
        inputBuffer.rewind();
        return inputBuffer;
    }

    public PredictionResponse classify(Bitmap bitmap) {
        if (interpreter == null) {
            throw new IllegalStateException("Interpreter is not initialized.");
        }

        ByteBuffer inputBuffer = preprocessImage(bitmap);
        float[][] outputBuffer = new float[1][outputClasses];
        interpreter.run(inputBuffer, outputBuffer);

        int bestIndex = argmax(outputBuffer[0]);
        float confidence = outputBuffer[0][bestIndex];

        PredictionResponse response = new PredictionResponse();
        response.setDisease(labels.get(Math.min(bestIndex, labels.size() - 1)));
        response.setConfidence(confidence);
        response.setSymptoms("Inspect the leaf and compare it with the disease library.");
        response.setTreatment("Apply the recommended treatment only after confirming the diagnosis.");
        response.setPrevention("Capture clearer images and keep the offline model updated.");
        return response;
    }

    private int argmax(float[] scores) {
        int bestIndex = 0;
        float bestValue = scores[0];
        for (int index = 1; index < scores.length; index++) {
            if (scores[index] > bestValue) {
                bestValue = scores[index];
                bestIndex = index;
            }
        }
        return bestIndex;
    }

    @Override
    public void close() {
        if (interpreter != null) {
            interpreter.close();
            interpreter = null;
        }
    }
}
```

---

## Method-by-Method Explanation

### Constructor
The default constructor assumes:
- model file is `model.tflite`
- labels file is `labels.txt`
- input size is `224`

That matches the most common MobileNet-style setup.

### `loadModel()`
This method:
1. opens the model from Android assets
2. memory-maps it using `MappedByteBuffer`
3. creates the `Interpreter`
4. reads the model output shape so the output array size is correct

Using a memory-mapped buffer is more efficient than copying the model into normal Java memory.

### `loadLabels()`
This reads label names from `labels.txt` and ignores blank lines and comments starting with `#`.

### `preprocessImage()`
This is the critical ML preparation step.

Steps:
1. resize the `Bitmap` to the expected model input size
2. allocate a direct `ByteBuffer`
3. loop through every pixel
4. extract red, green, and blue values
5. normalize each channel with `pixel / 255.0f`
6. write values into the buffer in RGB order
7. rewind the buffer so TFLite reads from the beginning

### Why `[0, 1]` normalization?
Many beginner-friendly TensorFlow models are trained with image values divided by 255. Always verify this against your own model notes.

### `classify()`
This method:
1. confirms the interpreter exists
2. preprocesses the image
3. creates the output array
4. runs inference
5. computes the best class using `argmax`
6. builds a `PredictionResponse`

### `argmax()`
This finds the index of the largest score in the model output. That index maps directly to a label in `labels.txt`.

### `close()`
Always release the `Interpreter` when done. TFLite models hold native resources and should not be left open forever.

---

## Error Handling Notes

### Missing model file
`openFd(modelAssetName)` throws `IOException`. Catch this where you create the classifier and show a helpful message to the user.

### Wrong labels count
If the labels file has fewer lines than the output layer size, this solution fills the gap with placeholder labels like `Class 7`.

### Uninitialized interpreter
Calling `classify()` before loading the model throws an `IllegalStateException`. That is intentional because silent failures are harder to debug.

---

## Common Mistakes
- Forgetting to add `model.tflite` to `app/src/main/assets/`
- Using a normalization scheme that does not match the training pipeline
- Loading labels in the wrong order
- Allocating the wrong input buffer size
- Forgetting `inputBuffer.rewind()` before inference
- Not closing the interpreter when the Activity is destroyed

---

## Testing Steps
1. Place `model.tflite` and `labels.txt` in `android-app/app/src/main/assets/`.
2. Create a sample `Bitmap` from gallery or camera.
3. Call `classifier.classify(bitmap)`.
4. Log the predicted disease and confidence.
5. Confirm the output class matches your label order.
6. Compare results with a Python test script before trusting the mobile model.

---

## Summary
A good TFLite wrapper keeps four responsibilities separate:
- model loading
- preprocessing
- inference
- cleanup

That structure makes the offline pipeline easier to debug and easier to connect to the rest of the LeafGuard AI app.


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

- 📝 [Back to Week 09 Exercises](../../roadmap/week-09-tensorflow-lite-offline-ai/exercises.md) — Try it yourself first
- 📖 [Week 09 README](../../roadmap/week-09-tensorflow-lite-offline-ai/README.md) — Week overview
- 💡 [Solutions Index for Week 09](README.md) — Other solutions this week
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

---
