#!/usr/bin/env python3
import argparse
from pathlib import Path

import tensorflow as tf

from model_contract import (
    DEFAULT_KERAS_MODEL,
    DEFAULT_LABELS,
    find_embedded_rescaling,
    load_labels,
    tensor_details,
    validate_keras_model,
)


def main() -> None:
    parser = argparse.ArgumentParser(description="Inspect LeafGuard Keras and TFLite model contracts.")
    parser.add_argument("--keras-model", type=Path, default=DEFAULT_KERAS_MODEL)
    parser.add_argument("--tflite-model", type=Path)
    parser.add_argument("--labels", type=Path, default=DEFAULT_LABELS)
    args = parser.parse_args()

    labels = load_labels(args.labels)
    print(f"Labels: {len(labels)} ({args.labels})")
    print("Preprocessing: resize to 224x224 RGB and pass raw float32 values in [0, 255].")

    if args.keras_model.is_file():
        model = tf.keras.models.load_model(args.keras_model)
        input_shape, output_shape = validate_keras_model(model, labels)
        rescaling = find_embedded_rescaling(model)
        print(f"Keras input: shape={input_shape}, dtype={model.inputs[0].dtype}")
        print(f"Keras output: shape={output_shape}, dtype={model.outputs[0].dtype}")
        print(f"Embedded preprocessing: {rescaling.name} maps [0, 255] to [-1, 1]")
        print("Keras contract: valid")
    else:
        print(f"Keras model: not found ({args.keras_model})")

    if args.tflite_model:
        interpreter = tf.lite.Interpreter(model_path=str(args.tflite_model))
        interpreter.allocate_tensors()
        input_detail, output_detail = tensor_details(interpreter)
        print(f"TFLite input: shape={tuple(input_detail['shape'])}, dtype={input_detail['dtype']}")
        print(f"TFLite output: shape={tuple(output_detail['shape'])}, dtype={output_detail['dtype']}")
        print("TFLite contract: valid")


if __name__ == "__main__":
    main()
