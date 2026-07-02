/*
 * Exercise 5: On-device TensorFlow Lite inference (Kotlin twin of ex05_LeafClassifier.java)
 * Week 9 - TensorFlow Lite Offline AI
 *
 * Starter skeleton. The finished version of this lives at:
 *   android-app-kotlin/app/src/main/java/com/leafguard/ml/TFLiteClassifier.kt
 * Use that as a reference AFTER attempting this yourself.
 * Complete the TODOs (see exercises/android-kotlin/README.md, Ex 6.1-6.5).
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
package com.leafguard.ml

import android.content.Context
import android.graphics.Bitmap

import java.io.IOException
import java.nio.ByteBuffer

class ex05_LeafClassifier @Throws(IOException::class) constructor(context: Context) : AutoCloseable {

    private val inputSize = 224 // pixels; must match the model input

    init {
        // TODO 1: memory-map model.tflite from assets and build an Interpreter.
        // TODO 2: load labels.txt into a List<String>, skipping '#'/blank lines.
    }

    /** Convert a Bitmap into the model's float input tensor (RGB, 0..1). */
    fun preprocess(bitmap: Bitmap): ByteBuffer? {
        // TODO 3: scale to inputSize x inputSize, then for each pixel put
        //   red/255f, green/255f, blue/255f as floats into a direct ByteBuffer
        //   ordered with ByteOrder.nativeOrder().
        return null
    }

    /** Run inference and return the predicted label. */
    fun classify(bitmap: Bitmap): String {
        // TODO 4: run the interpreter into Array(1) { FloatArray(numClasses) }.
        // TODO 5: find argmax and return labels[index].
        return "TODO"
    }

    override fun close() {
        // TODO 6: close the interpreter to release native resources.
    }
}
