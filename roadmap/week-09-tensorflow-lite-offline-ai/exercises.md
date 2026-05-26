# Week 09: Exercises

## Exercise 1: Add TFLite Dependency (15 min)

Add to build.gradle:
```gradle
implementation 'org.tensorflow:tensorflow-lite:2.12.0'
```

Sync and verify build success.

---

## Exercise 2: Place Model in Assets (30 min)

1. Obtain/convert .tflite model
2. Create assets/ folder if needed
3. Copy model.tflite to assets/
4. Create labels.txt with class names

**Verification:** Files visible in Android Studio assets folder.

---

## Exercise 3: Initialize Interpreter (1 hour)

Create TFLiteClassifier class:

```java
public class TFLiteClassifier {
    private Interpreter tflite;
    
    public void init(AssetManager assetManager) {
        // TODO: Load model and create interpreter
    }
}
```

Test initialization in MainActivity.

---

## Exercise 4: Image Preprocessing (1.5 hours)

Implement preprocessImage() method to convert Bitmap to float array matching model input shape.

Test with sample image, log array dimensions.

---

## Exercise 5: Run Inference (1 hour)

Implement classify() method:

```java
public String classify(Bitmap bitmap) {
    float[][][][] input = preprocessImage(bitmap);
    float[][] output = new float[1][numClasses];
    
    tflite.run(input, output);
    
    int predictedClass = argmax(output[0]);
    return labels.get(predictedClass);
}
```

---

## Exercise 6: Compare with Cloud (1 hour)

Run same image through both cloud and offline models.

Log results and latency for each.

**Verification:** Results are similar, latency values logged.

---

**Total Time:** 5-6 hours
