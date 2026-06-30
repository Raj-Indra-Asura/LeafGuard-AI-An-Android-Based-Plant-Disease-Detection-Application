/*
 * Exercise 5: On-device TensorFlow Lite inference
 * Week 9 - TensorFlow Lite Offline AI
 *
 * Starter skeleton. The finished version of this lives at:
 *   android-app/app/src/main/java/com/leafguard/ml/TFLiteClassifier.java
 * Use that as a reference AFTER attempting this yourself.
 * Complete the TODOs (see exercises/android/README.md, Ex 6.1-6.5).
 *
 * Goal: load model.tflite + labels.txt from assets, preprocess a Bitmap to the
 * model's input tensor, run inference, and map the argmax index to a label.
 *
 * CRITICAL: preprocessing MUST match how the model was trained, and labels.txt
 * MUST stay in sync with diseases.xml and model/labels.txt (10 demo classes:
 * "Tomato Early Blight", ... "Apple Scab"). A mismatch gives wrong predictions.
 *
 * Verification:
 *   [ ] Model and labels load from assets without IOException
 *   [ ] Input buffer is inputSize*inputSize*3 floats, normalized to 0..1, RGB
 *   [ ] argmax index maps to the correct label string
 *   [ ] Skip the blank '#' comment lines when reading labels.txt
 */
package com.leafguard.ml;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class ex05_LeafClassifier implements AutoCloseable {

    private final int inputSize = 224; // pixels; must match the model input

    public ex05_LeafClassifier(Context context) throws IOException {
        // TODO 1: memory-map model.tflite from assets and build an Interpreter.
        // TODO 2: load labels.txt into a List<String>, skipping '#'/blank lines.
    }

    /** Convert a Bitmap into the model's float input tensor (RGB, 0..1). */
    public ByteBuffer preprocess(Bitmap bitmap) {
        // TODO 3: scale to inputSize x inputSize, then for each pixel put
        //   red/255, green/255, blue/255 as floats into a direct ByteBuffer
        //   ordered with ByteOrder.nativeOrder().
        return null;
    }

    /** Run inference and return the predicted label. */
    public String classify(Bitmap bitmap) {
        // TODO 4: run the interpreter into float[1][numClasses].
        // TODO 5: find argmax and return labels.get(index).
        return "TODO";
    }

    @Override
    public void close() {
        // TODO 6: close the interpreter to release native resources.
    }
}
