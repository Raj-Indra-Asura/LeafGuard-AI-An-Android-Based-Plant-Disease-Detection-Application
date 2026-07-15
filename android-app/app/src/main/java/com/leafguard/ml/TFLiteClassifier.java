package com.leafguard.ml;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.leafguard.network.PredictionResponse;

import org.tensorflow.lite.Interpreter;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFLiteClassifier implements AutoCloseable {

    private static final String TAG = "TFLiteClassifier";
    private static final int PIXEL_SIZE = 3;
    private static final int BYTES_PER_CHANNEL = 4;
    private static final String GENERIC_SYMPTOMS =
            "Detailed symptoms and treatment guidance are not available in this version.";
    private static final String GENERIC_TREATMENT =
            "Please verify this result with a local agricultural expert or plant-disease reference.";
    private static final String GENERIC_PREVENTION =
            "Capture a clear close-up and continue monitoring. This result is not a confirmed diagnosis.";

    private final int inputSize;
    private final List<String> labels = new ArrayList<>();
    private final Map<String, Guidance> guidanceByDisplayName = new HashMap<>();
    private Interpreter interpreter;
    private int outputClasses = 1;

    public TFLiteClassifier(Context context) throws IOException {
        this(context, "model.tflite", "labels.txt", 224);
    }

    public TFLiteClassifier(Context context, String modelAssetName, String labelsAssetName, int inputSize)
            throws IOException {
        this.inputSize = inputSize;
        labels.addAll(loadLabels(context, labelsAssetName));
        if (labels.isEmpty()) {
            throw new IOException("The model labels asset is empty.");
        }
        guidanceByDisplayName.putAll(loadGuidance(context));
        initializeModel(context, modelAssetName);
    }

    private void initializeModel(Context context, String modelAssetName) throws IOException {
        try {
            AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelAssetName);
            try (FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor())) {
                FileChannel fileChannel = inputStream.getChannel();
                long startOffset = fileDescriptor.getStartOffset();
                long declaredLength = fileDescriptor.getDeclaredLength();
                MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);

                Interpreter.Options options = new Interpreter.Options();
                options.setNumThreads(4);
                interpreter = new Interpreter(mappedByteBuffer, options);

                int[] inputShape = interpreter.getInputTensor(0).shape();
                int[] outputShape = interpreter.getOutputTensor(0).shape();
                if (inputShape.length != 4 || inputShape[0] != 1
                        || inputShape[1] != inputSize || inputShape[2] != inputSize || inputShape[3] != PIXEL_SIZE) {
                    throw new IOException("Expected TFLite input shape [1, "
                            + inputSize + ", " + inputSize + ", 3].");
                }
                if (outputShape.length != 2 || outputShape[0] != 1) {
                    throw new IOException("Expected TFLite output shape [1, class_count].");
                }
                outputClasses = outputShape[1];
                if (outputClasses != labels.size()) {
                    throw new IOException("TFLite output count " + outputClasses
                            + " does not match label count " + labels.size() + ".");
                }
                Log.i(TAG, "Loaded valid TFLite model with " + outputClasses + " labels.");
            }
        } catch (IOException | IllegalArgumentException exception) {
            close();
            throw new IOException("Unable to load a compatible TFLite model asset.", exception);
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
                inputBuffer.putFloat(Color.red(pixel));
                inputBuffer.putFloat(Color.green(pixel));
                inputBuffer.putFloat(Color.blue(pixel));
            }
        }
        inputBuffer.rewind();
        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle();
        }
        return inputBuffer;
    }

    public PredictionResponse classify(Bitmap bitmap) {
        if (interpreter == null) {
            throw new IllegalStateException("TFLite interpreter is closed.");
        }
        ByteBuffer inputBuffer = preprocessImage(bitmap);
        float[][] outputBuffer = new float[1][outputClasses];
        interpreter.run(inputBuffer, outputBuffer);
        int bestIndex = argmax(outputBuffer[0]);
        float confidence = outputBuffer[0][bestIndex];
        String modelLabel = labels.get(bestIndex);
        String displayName = displayLabel(modelLabel);
        Guidance guidance = guidanceByDisplayName.get(displayName);

        PredictionResponse response = new PredictionResponse();
        response.setModelLabel(modelLabel);
        response.setDisease(displayName);
        response.setConfidence(confidence);
        response.setGuidanceAvailable(guidance != null);
        response.setSymptoms(guidance == null ? GENERIC_SYMPTOMS : guidance.symptoms);
        response.setTreatment(guidance == null ? GENERIC_TREATMENT : guidance.treatment);
        response.setPrevention(guidance == null ? GENERIC_PREVENTION : guidance.prevention);
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

            private String displayLabel(String modelLabel) {
                switch (modelLabel) {
                    case "Apple___Apple_scab":
                        return "Apple Scab";
                    case "Corn___Cercospora_leaf_spot Gray_leaf_spot":
                        return "Corn Gray Leaf Spot";
                    case "Corn___Northern_Leaf_Blight":
                        return "Corn Northern Leaf Blight";
                    default:
                        return modelLabel.replace("___", " ").replace('_', ' ');
                }
            }

            private Map<String, Guidance> loadGuidance(Context context) {
                Map<String, Guidance> guidance = new HashMap<>();
                try {
                    XmlPullParser parser = android.util.Xml.newPullParser();
                    parser.setInput(context.getAssets().open("diseases.xml"), "UTF-8");
                    parseGuidance(parser, guidance);
                } catch (Exception exception) {
                    Log.w(TAG, "Unable to load optional disease guidance.", exception);
                }
                return guidance;
            }

            private void parseGuidance(XmlPullParser parser, Map<String, Guidance> output) throws Exception {
                String name = null;
                String symptoms = null;
                String treatment = null;
                String prevention = null;
                int event = parser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    if (event == XmlPullParser.START_TAG) {
                        String tag = parser.getName();
                        if ("name".equals(tag)) {
                            name = parser.nextText();
                        } else if ("symptoms".equals(tag)) {
                            symptoms = parser.nextText();
                        } else if ("treatment".equals(tag)) {
                            treatment = parser.nextText();
                        } else if ("prevention".equals(tag)) {
                            prevention = parser.nextText();
                        }
                    } else if (event == XmlPullParser.END_TAG && "disease".equals(parser.getName()) && name != null) {
                        output.put(name, new Guidance(symptoms, treatment, prevention));
                        name = symptoms = treatment = prevention = null;
                    }
                    event = parser.next();
                }
            }

            private static final class Guidance {
                final String symptoms;
                final String treatment;
                final String prevention;

                Guidance(String symptoms, String treatment, String prevention) {
                    this.symptoms = symptoms;
                    this.treatment = treatment;
                    this.prevention = prevention;
                }
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
