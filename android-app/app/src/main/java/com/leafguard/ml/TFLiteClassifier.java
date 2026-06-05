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
        response.setSymptoms("Inspect the leaf closely and compare symptoms with the weekly disease library.");
        response.setTreatment("Apply the treatment plan recommended for the predicted class after manual verification.");
        response.setPrevention("Capture clear images, monitor plants regularly, and keep the offline model updated.");
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
