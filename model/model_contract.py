from pathlib import Path
from typing import Iterable, List, Sequence, Tuple

import numpy as np
from PIL import Image

ROOT = Path(__file__).resolve().parent.parent
DEFAULT_KERAS_MODEL = ROOT / "backend-api" / "models" / "leafguard_model.keras"
DEFAULT_LABELS = ROOT / "model" / "labels-38.txt"
BACKEND_LABELS = ROOT / "backend-api" / "labels-38.txt"
ANDROID_ASSETS = (
    ROOT / "android-app" / "app" / "src" / "main" / "assets",
    ROOT / "android-app-kotlin" / "app" / "src" / "main" / "assets",
)
EXPECTED_INPUT_SHAPE = (1, 224, 224, 3)
EXPECTED_CLASS_COUNT = 38


def load_labels(path: Path = DEFAULT_LABELS) -> List[str]:
    labels = [
        line.strip()
        for line in path.read_text(encoding="utf-8").splitlines()
        if line.strip() and not line.lstrip().startswith("#")
    ]
    if len(labels) != EXPECTED_CLASS_COUNT:
        raise ValueError(f"Expected {EXPECTED_CLASS_COUNT} labels, found {len(labels)} in {path}")
    if len(labels) != len(set(labels)):
        raise ValueError(f"Duplicate labels found in {path}")
    return labels


def validate_shape(actual: Sequence[int], expected: Sequence[int], name: str) -> None:
    normalized = tuple(1 if dimension is None else int(dimension) for dimension in actual)
    if normalized != tuple(expected):
        raise ValueError(f"Expected {name} shape {tuple(expected)}, got {tuple(actual)}")


def validate_keras_model(model, labels: Iterable[str]) -> Tuple[Tuple[int, ...], Tuple[int, ...]]:
    input_shape = tuple(model.input_shape)
    output_shape = tuple(model.output_shape)
    validate_shape(input_shape, EXPECTED_INPUT_SHAPE, "Keras input")
    if len(output_shape) != 2 or int(output_shape[-1]) != len(list(labels)):
        raise ValueError(
            f"Keras output shape {output_shape} is incompatible with the canonical labels"
        )
    dtype_name = getattr(model.input.dtype, "name", str(model.input.dtype))
    if dtype_name != "float32":
        raise ValueError(f"Expected Keras float32 input, got {model.input.dtype}")
    return input_shape, output_shape


def find_embedded_rescaling(model):
    pending = list(model.layers)
    while pending:
        layer = pending.pop(0)
        if layer.__class__.__name__ == "Rescaling":
            config = layer.get_config()
            scale = float(config.get("scale"))
            offset = float(config.get("offset", 0.0))
            if np.isclose(scale, 1.0 / 127.5) and np.isclose(offset, -1.0):
                return layer
        pending.extend(getattr(layer, "layers", []))
    raise ValueError(
        "Expected an embedded Rescaling layer mapping raw RGB [0, 255] to [-1, 1]. "
        "Refusing conversion because Android/backend preprocessing would not match."
    )


def preprocess_image(path: Path) -> np.ndarray:
    with Image.open(path) as image:
        rgb_image = image.convert("RGB").resize((224, 224))
        return np.expand_dims(np.asarray(rgb_image, dtype=np.float32), axis=0)


def tensor_details(interpreter):
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    if len(input_details) != 1 or len(output_details) != 1:
        raise ValueError("LeafGuard requires exactly one input tensor and one output tensor")
    input_detail = input_details[0]
    output_detail = output_details[0]
    validate_shape(input_detail["shape"], EXPECTED_INPUT_SHAPE, "TFLite input")
    if input_detail["dtype"] != np.float32:
        raise ValueError(f"Expected TFLite float32 input, got {input_detail['dtype']}")
    if tuple(int(value) for value in output_detail["shape"]) != (1, EXPECTED_CLASS_COUNT):
        raise ValueError(
            f"Expected TFLite output shape (1, {EXPECTED_CLASS_COUNT}), "
            f"got {tuple(output_detail['shape'])}"
        )
    if output_detail["dtype"] != np.float32:
        raise ValueError(f"Expected TFLite float32 output, got {output_detail['dtype']}")
    return input_detail, output_detail
