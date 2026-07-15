#!/usr/bin/env python3
import argparse
import shutil
from pathlib import Path

import tensorflow as tf

from model_contract import (
    ANDROID_ASSETS,
    BACKEND_LABELS,
    DEFAULT_KERAS_MODEL,
    DEFAULT_LABELS,
    find_embedded_rescaling,
    load_labels,
    validate_keras_model,
)


def main() -> None:
    parser = argparse.ArgumentParser(description="Convert the approved LeafGuard Keras model to TFLite.")
    parser.add_argument("--keras-model", type=Path, default=DEFAULT_KERAS_MODEL)
    parser.add_argument("--labels", type=Path, default=DEFAULT_LABELS)
    args = parser.parse_args()

    if not args.keras_model.is_file():
        raise SystemExit(f"Model not found: {args.keras_model}")

    labels = load_labels(args.labels)
    model = tf.keras.models.load_model(args.keras_model)
    validate_keras_model(model, labels)
    find_embedded_rescaling(model)

    converter = tf.lite.TFLiteConverter.from_keras_model(model)
    tflite_model = converter.convert()

    shutil.copyfile(args.labels, BACKEND_LABELS)
    print(f"Synchronized {BACKEND_LABELS}")
    for assets_dir in ANDROID_ASSETS:
        assets_dir.mkdir(parents=True, exist_ok=True)
        (assets_dir / "model.tflite").write_bytes(tflite_model)
        shutil.copyfile(args.labels, assets_dir / "labels.txt")
        print(f"Wrote {assets_dir / 'model.tflite'} ({len(tflite_model)} bytes)")
        print(f"Synchronized {assets_dir / 'labels.txt'}")


if __name__ == "__main__":
    main()
