package com.leafguard.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.leafguard.network.PredictionResponse
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.Locale
import org.tensorflow.lite.Interpreter

/**
 * Kotlin twin of TFLiteClassifier.java.
 *
 * Behavioral contract preserved exactly:
 * - default assets model.tflite / labels.txt, input size 224
 * - memory-mapped model load, 4 interpreter threads
 * - RGB float32 normalization to 0..1 (divide by 255)
 * - labels parsing skips blank lines and lines starting with '#'
 * - argmax over the single output tensor
 * - heuristic green-channel fallback when the model asset is missing/invalid
 */
class TFLiteClassifier @Throws(IOException::class) @JvmOverloads constructor(
    context: Context,
    modelAssetName: String = "model.tflite",
    labelsAssetName: String = "labels.txt",
    private val inputSize: Int = 224
) : AutoCloseable {

    companion object {
        private const val TAG = "TFLiteClassifier"
        private const val PIXEL_SIZE = 3
        private const val BYTES_PER_CHANNEL = 4
        private const val FALLBACK_TOMATO_EARLY_BLIGHT_INDEX = 0
        private const val FALLBACK_TOMATO_LATE_BLIGHT_INDEX = 1
        private const val FALLBACK_TOMATO_HEALTHY_INDEX = 2
    }

    private val labels = mutableListOf<String>()
    private var interpreter: Interpreter? = null
    private var outputClasses = 1
    private var heuristicFallback = false

    init {
        initializeModelOrFallback(context, modelAssetName)
        labels.addAll(loadLabels(context, labelsAssetName))
        if (labels.isEmpty()) {
            labels.add("Unknown disease")
        }
        while (labels.size < outputClasses) {
            labels.add(String.format(Locale.getDefault(), "Class %d", labels.size))
        }
    }

    private fun initializeModelOrFallback(context: Context, modelAssetName: String) {
        try {
            val fileDescriptor = context.assets.openFd(modelAssetName)
            FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
                val fileChannel = inputStream.channel
                val startOffset = fileDescriptor.startOffset
                val declaredLength = fileDescriptor.declaredLength
                val mappedByteBuffer =
                    fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)

                val options = Interpreter.Options().apply { setNumThreads(4) }
                interpreter = Interpreter(mappedByteBuffer, options)

                val outputShape = interpreter!!.getOutputTensor(0).shape()
                if (outputShape.size > 1) {
                    outputClasses = outputShape[1]
                }
            }
        } catch (exception: IOException) {
            fallBackToHeuristic(exception)
        } catch (exception: IllegalArgumentException) {
            fallBackToHeuristic(exception)
        }
    }

    private fun fallBackToHeuristic(exception: Exception) {
        Log.w(TAG, "Unable to load a valid TFLite model asset; using the starter heuristic fallback.", exception)
        heuristicFallback = true
        outputClasses = 3
    }

    @Throws(IOException::class)
    private fun loadLabels(context: Context, labelsAssetName: String): List<String> {
        val loadedLabels = mutableListOf<String>()
        BufferedReader(InputStreamReader(context.assets.open(labelsAssetName))).use { reader ->
            reader.forEachLine { line ->
                val trimmed = line.trim()
                if (trimmed.isNotEmpty() && !trimmed.startsWith("#")) {
                    loadedLabels.add(trimmed)
                }
            }
        }
        return loadedLabels
    }

    fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
        val inputBuffer = ByteBuffer.allocateDirect(
            inputSize * inputSize * PIXEL_SIZE * BYTES_PER_CHANNEL
        ).order(ByteOrder.nativeOrder())

        for (y in 0 until inputSize) {
            for (x in 0 until inputSize) {
                val pixel = scaledBitmap.getPixel(x, y)
                inputBuffer.putFloat(Color.red(pixel) / 255.0f)
                inputBuffer.putFloat(Color.green(pixel) / 255.0f)
                inputBuffer.putFloat(Color.blue(pixel) / 255.0f)
            }
        }
        inputBuffer.rewind()
        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle()
        }
        return inputBuffer
    }

    fun classify(bitmap: Bitmap): PredictionResponse {
        val bestIndex: Int
        val confidence: Float

        val activeInterpreter = interpreter
        if (heuristicFallback || activeInterpreter == null) {
            val averageGreen = averageGreen(bitmap)
            when {
                averageGreen > 0.48f -> {
                    bestIndex = findLabelIndex("Tomato Healthy", FALLBACK_TOMATO_HEALTHY_INDEX)
                    confidence = 0.78f
                }
                averageGreen > 0.32f -> {
                    bestIndex = findLabelIndex("Tomato Early Blight", FALLBACK_TOMATO_EARLY_BLIGHT_INDEX)
                    confidence = 0.72f
                }
                else -> {
                    bestIndex = findLabelIndex("Tomato Late Blight", FALLBACK_TOMATO_LATE_BLIGHT_INDEX)
                    confidence = 0.69f
                }
            }
        } else {
            val inputBuffer = preprocessImage(bitmap)
            val outputBuffer = Array(1) { FloatArray(outputClasses) }
            activeInterpreter.run(inputBuffer, outputBuffer)
            bestIndex = argmax(outputBuffer[0])
            confidence = outputBuffer[0][bestIndex]
        }

        return PredictionResponse(
            disease = labels[minOf(bestIndex, labels.size - 1)],
            confidence = confidence,
            symptoms = "Inspect the leaf closely and compare symptoms with the bundled disease library.",
            treatment = "Apply the treatment plan recommended for the predicted class after manual verification.",
            prevention = "Capture clear images, monitor plants regularly, and replace the starter model with a trained model for production use."
        )
    }

    private fun averageGreen(bitmap: Bitmap): Float {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
        var greenSum = 0L
        val pixelCount = inputSize * inputSize
        for (y in 0 until inputSize) {
            for (x in 0 until inputSize) {
                greenSum += Color.green(scaledBitmap.getPixel(x, y))
            }
        }
        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle()
        }
        return (greenSum / pixelCount.toFloat()) / 255.0f
    }

    private fun findLabelIndex(label: String, fallbackIndex: Int): Int {
        val index = labels.indexOf(label)
        if (index >= 0) {
            return index
        }
        return maxOf(0, minOf(fallbackIndex, labels.size - 1))
    }

    private fun argmax(scores: FloatArray): Int {
        var bestIndex = 0
        var bestValue = scores[0]
        for (index in 1 until scores.size) {
            if (scores[index] > bestValue) {
                bestValue = scores[index]
                bestIndex = index
            }
        }
        return bestIndex
    }

    override fun close() {
        interpreter?.close()
        interpreter = null
    }
}
