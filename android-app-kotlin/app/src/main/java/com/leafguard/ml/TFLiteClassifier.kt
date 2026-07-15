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
import org.tensorflow.lite.Interpreter
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

/**
 * Kotlin twin of TFLiteClassifier.java.
 *
 * Behavioral contract preserved exactly:
 * - default assets model.tflite / labels.txt, input size 224
 * - memory-mapped model load, 4 interpreter threads
 * - raw RGB float32 input in 0..255 because preprocessing is embedded in the model
 * - labels parsing skips blank lines and lines starting with '#'
 * - argmax over the single output tensor
 * - strict tensor and label validation
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
        private const val GENERIC_SYMPTOMS =
            "Detailed symptoms and treatment guidance are not available in this version."
        private const val GENERIC_TREATMENT =
            "Please verify this result with a local agricultural expert or plant-disease reference."
        private const val GENERIC_PREVENTION =
            "Capture a clear close-up and continue monitoring. This result is not a confirmed diagnosis."
    }

    private val labels = mutableListOf<String>()
    private val guidanceByDisplayName: Map<String, Guidance>
    private var interpreter: Interpreter? = null
    private var outputClasses = 1

    init {
        labels.addAll(loadLabels(context, labelsAssetName))
        if (labels.isEmpty()) {
            throw IOException("The model labels asset is empty.")
        }
        guidanceByDisplayName = loadGuidance(context)
        initializeModel(context, modelAssetName)
    }

    @Throws(IOException::class)
    private fun initializeModel(context: Context, modelAssetName: String) {
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

                val activeInterpreter = interpreter!!
                val inputShape = activeInterpreter.getInputTensor(0).shape()
                val outputShape = activeInterpreter.getOutputTensor(0).shape()
                if (!inputShape.contentEquals(intArrayOf(1, inputSize, inputSize, PIXEL_SIZE))) {
                    throw IOException("Expected TFLite input shape [1, $inputSize, $inputSize, 3].")
                }
                if (outputShape.size != 2 || outputShape[0] != 1) {
                    throw IOException("Expected TFLite output shape [1, class_count].")
                }
                outputClasses = outputShape[1]
                if (outputClasses != labels.size) {
                    throw IOException(
                        "TFLite output count $outputClasses does not match label count ${labels.size}."
                    )
                }
                Log.i(TAG, "Loaded valid TFLite model with $outputClasses labels.")
            }
        } catch (exception: IOException) {
            close()
            throw IOException("Unable to load a compatible TFLite model asset.", exception)
        } catch (exception: IllegalArgumentException) {
            close()
            throw IOException("Unable to load a compatible TFLite model asset.", exception)
        }
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
                inputBuffer.putFloat(Color.red(pixel).toFloat())
                inputBuffer.putFloat(Color.green(pixel).toFloat())
                inputBuffer.putFloat(Color.blue(pixel).toFloat())
            }
        }
        inputBuffer.rewind()
        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle()
        }
        return inputBuffer
    }

    fun classify(bitmap: Bitmap): PredictionResponse {
        val activeInterpreter = checkNotNull(interpreter) { "TFLite interpreter is closed." }
        val inputBuffer = preprocessImage(bitmap)
        val outputBuffer = Array(1) { FloatArray(outputClasses) }
        activeInterpreter.run(inputBuffer, outputBuffer)
        val bestIndex = argmax(outputBuffer[0])
        val confidence = outputBuffer[0][bestIndex]
        val modelLabel = labels[bestIndex]
        val displayName = displayLabel(modelLabel)
        val guidance = guidanceByDisplayName[displayName]

        return PredictionResponse(
            modelLabel = modelLabel,
            disease = displayName,
            confidence = confidence,
            guidanceAvailable = guidance != null,
            symptoms = guidance?.symptoms ?: GENERIC_SYMPTOMS,
            treatment = guidance?.treatment ?: GENERIC_TREATMENT,
            prevention = guidance?.prevention ?: GENERIC_PREVENTION
        )
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

    private fun displayLabel(modelLabel: String): String = when (modelLabel) {
                "Apple___Apple_scab" -> "Apple Scab"
                "Corn___Cercospora_leaf_spot Gray_leaf_spot" -> "Corn Gray Leaf Spot"
                "Corn___Northern_Leaf_Blight" -> "Corn Northern Leaf Blight"
                else -> modelLabel.replace("___", " ").replace('_', ' ')
            }

            private fun loadGuidance(context: Context): Map<String, Guidance> {
                val guidance = mutableMapOf<String, Guidance>()
                try {
                    val parser = android.util.Xml.newPullParser()
                    context.assets.open("diseases.xml").use { input ->
                        parser.setInput(input, "UTF-8")
                        parseGuidance(parser, guidance)
                    }
                } catch (exception: IOException) {
                    Log.w(TAG, "Unable to load optional disease guidance.", exception)
                } catch (exception: XmlPullParserException) {
                    Log.w(TAG, "Unable to load optional disease guidance.", exception)
                }
                return guidance
            }

            private fun parseGuidance(parser: XmlPullParser, output: MutableMap<String, Guidance>) {
                var name: String? = null
                var symptoms: String? = null
                var treatment: String? = null
                var prevention: String? = null
                var event = parser.eventType
                while (event != XmlPullParser.END_DOCUMENT) {
                    if (event == XmlPullParser.START_TAG) {
                        when (parser.name) {
                            "name" -> name = parser.nextText()
                            "symptoms" -> symptoms = parser.nextText()
                            "treatment" -> treatment = parser.nextText()
                            "prevention" -> prevention = parser.nextText()
                        }
                    } else if (event == XmlPullParser.END_TAG && parser.name == "disease" && name != null) {
                        output[name] = Guidance(symptoms.orEmpty(), treatment.orEmpty(), prevention.orEmpty())
                        name = null
                        symptoms = null
                        treatment = null
                        prevention = null
                    }
                    event = parser.next()
                }
            }

            private data class Guidance(
                val symptoms: String,
                val treatment: String,
                val prevention: String
    )

    override fun close() {
        interpreter?.close()
        interpreter = null
    }
}
