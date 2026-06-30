"""
generate_stub_model.py — LeafGuard AI stub TFLite model generator

Builds a small MobileNetV2-based model (untrained weights) and converts it
to TFLite format. Output files are placed in android-app/app/src/main/assets/.

Purpose: allows testing the Android TFLite integration pipeline before a real
trained model is available. Predictions from this stub model are meaningless
(random output) — replace the output file with a properly trained model.

Usage:
    python3 model/generate_stub_model.py

Requirements:
    pip install tensorflow pillow numpy
"""

import os
import json
import sys

ASSETS_DIR = os.path.join(
    os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
    "android-app", "app", "src", "main", "assets",
)

# 10 LeafGuard AI demo classes.
#
# These MUST stay in sync with the labels the rest of the project uses:
#   - android-app/app/src/main/assets/labels.txt
#   - android-app/app/src/main/assets/diseases.xml (the <name> values)
#   - model/labels.txt
#   - the TFLiteClassifier heuristic fallback
#
# The naming format is the app's human-readable "Crop Disease" style (e.g.
# "Tomato Early Blight"), NOT the raw PlantVillage "Tomato___Early_blight"
# format. Keeping these aligned means running this script does not desync the
# label list from the disease-library lookup.
LEAFGUARD_LABELS = [
    "Tomato Early Blight",
    "Tomato Late Blight",
    "Tomato Healthy",
    "Potato Early Blight",
    "Potato Late Blight",
    "Potato Healthy",
    "Corn Gray Leaf Spot",
    "Corn Northern Leaf Blight",
    "Corn Healthy",
    "Apple Scab",
]

NUM_CLASSES = len(LEAFGUARD_LABELS)  # 10
INPUT_SIZE = 224  # pixels, matching MobileNetV2 default


def build_and_convert():
    try:
        import tensorflow as tf
    except ImportError:
        print("ERROR: TensorFlow is not installed.")
        print("Run:  pip install tensorflow")
        sys.exit(1)

    print(f"TensorFlow version: {tf.__version__}")
    print(f"Building MobileNetV2 stub model ({INPUT_SIZE}x{INPUT_SIZE}, {NUM_CLASSES} classes)...")

    # Build: MobileNetV2 base (ImageNet weights for realistic architecture)
    # with a custom top for NUM_CLASSES
    base = tf.keras.applications.MobileNetV2(
        input_shape=(INPUT_SIZE, INPUT_SIZE, 3),
        include_top=False,
        weights=None,  # no download needed; stub model only
        pooling="avg",
    )
    inputs = tf.keras.Input(shape=(INPUT_SIZE, INPUT_SIZE, 3))
    x = tf.keras.applications.mobilenet_v2.preprocess_input(inputs)
    x = base(x, training=False)
    outputs = tf.keras.layers.Dense(NUM_CLASSES, activation="softmax")(x)
    model = tf.keras.Model(inputs, outputs)

    print(f"Model parameters: {model.count_params():,}")

    # Convert to TFLite
    converter = tf.lite.TFLiteConverter.from_keras_model(model)
    tflite_model = converter.convert()

    # Write outputs
    os.makedirs(ASSETS_DIR, exist_ok=True)
    tflite_path = os.path.join(ASSETS_DIR, "model.tflite")
    labels_path = os.path.join(ASSETS_DIR, "labels.txt")

    with open(tflite_path, "wb") as f:
        f.write(tflite_model)

    with open(labels_path, "w", encoding="utf-8") as f:
        f.write("\n".join(LEAFGUARD_LABELS) + "\n")

    size_kb = os.path.getsize(tflite_path) // 1024
    print(f"\n✅ TFLite model written:  {tflite_path}  ({size_kb} KB)")
    print(f"✅ labels.txt written:    {labels_path}  ({NUM_CLASSES} classes)")
    print()
    print("⚠️  This is a STUB MODEL with random weights.")
    print("   Replace it with a properly trained model before demo/submission.")
    print("   See model/model-acquisition-guide.md for how to train a real model.")


def verify_model():
    """Run a quick inference sanity check on the generated model."""
    try:
        import numpy as np
        import tensorflow as tf
    except ImportError:
        print("Skipping verification (numpy/tensorflow not available).")
        return

    tflite_path = os.path.join(ASSETS_DIR, "model.tflite")
    if not os.path.exists(tflite_path):
        return

    interpreter = tf.lite.Interpreter(model_path=tflite_path)
    interpreter.allocate_tensors()

    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()

    dummy_image = np.random.rand(1, INPUT_SIZE, INPUT_SIZE, 3).astype(np.float32)
    interpreter.set_tensor(input_details[0]["index"], dummy_image)
    interpreter.invoke()
    output = interpreter.get_tensor(output_details[0]["index"])

    predicted_index = int(np.argmax(output[0]))
    predicted_label = LEAFGUARD_LABELS[predicted_index]
    confidence = float(output[0][predicted_index])

    print(f"✅ Verification OK — random input predicted: '{predicted_label}' ({confidence:.2%})")


if __name__ == "__main__":
    build_and_convert()
    verify_model()
