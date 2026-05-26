# Week 09: Learning Notes - TensorFlow Lite

## 1. TensorFlow Lite Overview

**TFLite** is TensorFlow's lightweight solution for mobile and embedded devices.

**Key features:**
- Optimized for mobile CPUs
- Small binary size (~300KB)
- Fast inference (~50-200ms)
- No internet required

---

## 2. Model Conversion

Convert TensorFlow/Keras model to .tflite:

```python
import tensorflow as tf

# Load model
model = tf.keras.models.load_model('model.h5')

# Convert
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

# Save
with open('model.tflite', 'wb') as f:
    f.write(tflite_model)
```

---

## 3. Android Integration

### Add Dependency

```gradle
implementation 'org.tensorflow:tensorflow-lite:2.12.0'
```

### Initialize Interpreter

```java
Interpreter tflite;

try {
    tflite = new Interpreter(loadModelFile());
} catch (Exception e) {
    Log.e(TAG, "Error loading model", e);
}

private MappedByteBuffer loadModelFile() throws IOException {
    AssetFileDescriptor fileDescriptor = getAssets().openFd("model.tflite");
    FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
    FileChannel fileChannel = inputStream.getChannel();
    long startOffset = fileDescriptor.getStartOffset();
    long declaredLength = fileDescriptor.getDeclaredLength();
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
}
```

### Run Inference

```java
// Input: 224x224x3 float array
float[][][][] input = preprocessImage(bitmap);

// Output: 1x6 float array (6 classes)
float[][] output = new float[1][6];

// Run
tflite.run(input, output);

// Get result
int predictedClass = argmax(output[0]);
float confidence = output[0][predictedClass];
```

---

## 4. Input Preprocessing

Must match model training preprocessing exactly:

```java
private float[][][][] preprocessImage(Bitmap bitmap) {
    // Resize to 224x224
    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
    
    float[][][][] input = new float[1][224][224][3];
    
    for (int y = 0; y < 224; y++) {
        for (int x = 0; x < 224; x++) {
            int pixel = resized.getPixel(x, y);
            
            // Normalize to [0, 1]
            input[0][y][x][0] = ((pixel >> 16) & 0xFF) / 255.0f;  // Red
            input[0][y][x][1] = ((pixel >> 8) & 0xFF) / 255.0f;   // Green
            input[0][y][x][2] = (pixel & 0xFF) / 255.0f;          // Blue
        }
    }
    
    return input;
}
```

---

## 5. CSE 2206 Viva Prep

**Q: How does TFLite differ from cloud inference?**

A: TFLite runs ML model directly on device without internet. Cloud inference sends image to server for processing. TFLite advantages: offline operation, privacy (data stays on device), faster for small models. Cloud advantages: can use larger models, easier to update model. LeafGuard implements both for comparison.

**Q: What are TFLite limitations?**

A: TFLite models are quantized/optimized, slightly less accurate than full models. Limited to simpler architectures. Model must fit in device memory (~5-50MB typical). Inference slower than GPU servers but faster than network latency for small models.

---

**Summary:** TFLite enables offline AI on Android. Key challenge is matching preprocessing exactly.
