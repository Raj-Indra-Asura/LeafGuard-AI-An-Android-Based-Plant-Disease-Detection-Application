# Week 06 Learning Notes: Real Cloud Model Integration

## Purpose

These notes explain how to replace Week 04's mock execution with an approved Keras artifact without changing the Week 05 mobile contract.

Section 12 is the authoritative reconstruction appendix. It contains all six Week 06 text files in full. The tracked binary is identified by exact source, size, and SHA-256 rather than pasted into Markdown.

---

## 1. How Week 06 Grows From Week 05

Week 05 already proved:

```text
Android image URI
  -> multipart field image
  -> POST /predict
  -> eight-field JSON
  -> ResultActivity
```

Week 06 changes only the server-side prediction implementation:

```text
Week 05: validated image -> mock predictor -> stable JSON
Week 06: validated image -> approved Keras model -> same stable JSON
```

Android does not need new dependencies, fields, URLs, or screens. A model integration that breaks Week 05's contract is not complete.

---

## 2. Model Artifact as a Dependency

A trained model is executable data. Treat it like a third-party library:

- identify its source repository
- pin the source commit
- review the license
- record the exact artifact path
- record byte size
- calculate SHA-256
- keep the large binary out of Git
- validate its interface before inference

The approved artifact is:

| Property | Value |
|---|---|
| Source | `Muhammad-Hassan12/Plant-Disease-Detector` |
| Commit | `f6165bd93524dfb77a9629aae70db845832d1b01` |
| Upstream path | `Models/model_4_mobilenet_finetuned.keras` |
| Local path | `backend-api/models/leafguard_model.keras` |
| Size | 25,143,175 bytes |
| SHA-256 | `08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f` |

A matching filename alone proves nothing. Hash equality proves byte-for-byte identity with the reviewed artifact.

---

## 3. Tensor Input Contract

The Keras model expects one input tensor:

```text
shape: (1, 224, 224, 3)
dtype: float32
color: RGB
caller range: [0, 255]
```

Dimension meaning:

| Position | Meaning |
|---:|---|
| 1 | Batch size: one image |
| 224 | Height |
| 224 | Width |
| 3 | Red, green, blue channels |

The backend performs:

1. decode bytes with Pillow
2. convert to RGB
3. resize to 224 x 224
4. convert to NumPy `float32`
5. add a batch dimension

That creates `(1, 224, 224, 3)`.

---

## 4. Embedded Preprocessing: Do Not Normalize Twice

The approved model contains preprocessing that maps raw pixels:

$$
y = \frac{x}{127.5} - 1
$$

Therefore:

| Caller value | Model value |
|---:|---:|
| 0 | -1 |
| 127.5 | 0 |
| 255 | 1 |

The backend must pass raw `[0,255]` float values. Dividing by 255 before inference would feed approximately `[-1,-0.992]` after the embedded transform, which is not the model's training contract.

Preprocessing can run without an exception and still be wrong. That is why the embedded scaling is validated explicitly.

---

## 5. Output and Label Contract

The model returns:

```text
shape: (1, 38)
dtype: float32
```

One score corresponds to each line in `labels-38.txt`:

```python
best_index = int(np.argmax(scores))
model_label = labels[best_index]
confidence = float(scores[best_index])
```

Order is semantic. Sorting labels alphabetically after model training silently maps every index to the wrong class.

Validation requires:

- exactly 38 non-empty labels
- no duplicates
- canonical and backend files match exactly
- output dimension is 38

---

## 6. Model Loading and Real Mode

The Week 04 backend already loads one predictor during module startup:

```text
USE_MOCK=true
  -> skip model load
  -> deterministic contract practice

USE_MOCK=false
  -> import TensorFlow
  -> find model path
  -> load Keras once
  -> verify input/output shapes
  -> reuse model for requests
```

Loading once avoids disk and graph reconstruction on every request.

Real mode is not proven by setting an environment variable. It is proven by `/health`:

```json
{
  "status": "ok",
  "use_mock": false,
  "model_loaded": true,
  "image_size": 224,
  "class_count": 38
}
```

If the model is absent or invalid, `/predict` returns 503. The backend must not silently describe a mock result as real inference.

---

## 7. Stable Mobile Response

Real inference still returns the Week 05-compatible shape:

```json
{
  "model_label": "Tomato___Early_blight",
  "disease": "Tomato Early Blight",
  "confidence": 0.87,
  "uncertain": false,
  "guidance_available": true,
  "symptoms": "...",
  "treatment": "...",
  "prevention": "..."
}
```

The predictor supplies `model_label` and `confidence`. Existing backend code supplies display formatting, thresholding, and reviewed guidance or fallback text.

A valid 38-class model label may have `guidance_available=false` because this project currently has reviewed guidance for only 10 classes.

---

## 8. Confidence Is Not Accuracy

Confidence compares output scores for one input. It does not prove correctness.

A high-confidence prediction can still be wrong because of:

- unfamiliar crop or disease
- background differences
- blur, shadow, or overexposure
- multiple leaves
- visual similarity between classes
- dataset bias

The source author's 98.75% report belongs to an augmented PlantVillage split. It is not independently measured LeafGuard phone-camera accuracy.

Professional wording:

> This result is a model suggestion, not a confirmed diagnosis. Retake unclear images and consult a qualified agricultural source for serious cases.

---

## 9. Contract Validation Before Prediction

`model_contract.py` verifies structure before trust:

| Check | Failure prevented |
|---|---|
| 38 unique labels | Missing or duplicate class names |
| Input shape | Incompatible image dimensions/channels |
| Input dtype | Wrong numeric representation |
| Output count | Label/model mismatch |
| Embedded scaling | Double or absent normalization |

`inspect_model.py` loads the approved artifact and prints observable contract facts.

`test_model_contract.py` proves one success path and deliberately rejects wrong shape and wrong scaling.

---

## 10. Testing Layers

Use this order:

1. artifact size and SHA-256
2. canonical/backend label comparison
3. static Keras contract inspection
4. focused contract tests
5. `/health` in real mode
6. one real `/predict` request
7. Week 05 Android regression demo
8. missing/invalid model failure

A single plausible disease name is weak evidence. Contract checks plus repeatable tests establish that the intended model is running.

---

## 11. Common Mistakes and Week Boundary

Avoid:

- using a model from an unpinned URL
- committing the 25 MB model binary
- skipping license/provenance review
- normalizing pixels twice
- sorting labels
- loading the model per request
- using `USE_MOCK=true` as real-model evidence
- changing the eight-field API
- claiming source-author accuracy as LeafGuard accuracy
- adding TFLite conversion or Android offline code in Week 06

Week 06 is cloud Keras inference. TFLite conversion, parity, Android assets, and offline inference are later boundaries.

---

## 12. End-of-Week-06 File Inventory (Exact Files, Exact Code, Exact Size)

Week 05 ends with a working Android-to-FastAPI mock pipeline. Week 06 changes zero Android files and zero API fields. It adds or rewrites six model-validation text files and validates one provided tracked binary artifact.

### 12.1 Change Summary: Week 05 -> Week 06

| Change | Count | Files |
|---|---:|---|
| New or rewritten text | 6 | Notes, labels, contract, inspector, tests, provenance |
| Local binary | 1 | `backend-api/models/leafguard_model.keras` |
| Backend source changed | 0 | Week 04 already prepared real mode |
| Android changed | 0 | Week 05 contract remains unchanged |
| Offline/TFLite changed | 0 | Deferred |

**Complete Week 06 text snapshot: 311 logical lines.**

### 12.2 Exact Week 06 Tree

```text
LeafGuard-AI/
|-- android-app-kotlin/                         UNCHANGED FROM WEEK 05
|-- android-app/                                UNCHANGED FROM WEEK 05
|-- backend-api/
|   |-- main.py                                 UNCHANGED, REAL-MODE READY
|   |-- config.py                               UNCHANGED, REAL-MODE READY
|   |-- model_loader.py                         UNCHANGED, REAL-MODE READY
|   |-- labels-38.txt                           UNCHANGED FROM WEEK 04
|   |-- requirements.txt                        UNCHANGED, TENSORFLOW PINNED
|   `-- models/
|       `-- leafguard_model.keras               PROVIDED/TRACKED 25,143,175 bytes
|-- model/
|   |-- model-notes.md                          REWRITTEN 54 lines
|   |-- labels-38.txt                           NEW       38 lines
|   |-- model_contract.py                       NEW       85 lines
|   |-- inspect_model.py                        NEW       39 lines
|   `-- test_model_contract.py                  NEW       56 lines
|-- release-records/
|   `-- model-provenance.txt                    NEW       39 lines
`-- docs/evidence/week-06/                      EVIDENCE OUTPUT
```

### 12.3 Rewritten File: `model/model-notes.md` (54 lines)

````markdown
# LeafGuard 38-Class Cloud Model Contract

## Approved Artifact

- Source repository: `Muhammad-Hassan12/Plant-Disease-Detector`
- Pinned source commit: `f6165bd93524dfb77a9629aae70db845832d1b01`
- Source artifact: `Models/model_4_mobilenet_finetuned.keras`
- Local path: `backend-api/models/leafguard_model.keras`
- Exact size: 25,143,175 bytes
- SHA-256: `08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f`
- Source repository license claim: MIT; personal review is required before use or redistribution.

The approved model binary is tracked in this repository. Verify its size and hash before use, and never replace or duplicate it without updating provenance and reviewing the license.

## Tensor Contract

- Framework: TensorFlow/Keras 2.19.1
- Architecture: fine-tuned MobileNetV2 classifier
- Input: one `float32` tensor shaped `[1, 224, 224, 3]`
- Color: RGB
- Caller preprocessing: decode, convert to RGB, resize to 224x224, cast to `float32`
- Caller pixel range: raw `[0, 255]`
- Embedded preprocessing: model maps `[0, 255]` to `[-1, 1]`
- Output: one `float32` tensor shaped `[1, 38]`
- Label mapping: output index uses `model/labels-38.txt` exactly; never sort it
- Selection: `argmax(output[0])`; confidence is the score at that index

Do not divide pixels by 255 in the backend. That would normalize twice and change inference behavior.

## API Compatibility

Week 06 does not change the Week 05 mobile contract:

- Request: `POST /predict`
- Encoding: `multipart/form-data`
- Field: `image`
- Response: `model_label`, `disease`, `confidence`, `uncertain`, `guidance_available`, `symptoms`, `treatment`, `prevention`

Real mode is proven only when `/health` reports `use_mock=false`, `model_loaded=true`, and `class_count=38`.

## Validation

```bash
backend-api/.venv/bin/python model/inspect_model.py
cd model && ../backend-api/.venv/bin/python -m unittest -v test_model_contract
```

Then run one real-mode `/predict` request and preserve the unchanged eight-field JSON.

## Limitations

The source author's published 98.75% score is not independently measured LeafGuard accuracy. Controlled PlantVillage images do not represent every phone-camera background, crop, disease, blur, or lighting condition. Confidence is not certainty. Low-confidence output must remain uncertain, and users should verify serious cases with a qualified agricultural source.

TFLite conversion and offline Android inference are later-week work. Week 06 validates cloud Keras inference only.
````

### 12.4 New File: `model/labels-38.txt` (38 lines)

```text
Apple___Apple_scab
Apple___Black_rot
Apple___Cedar_apple_rust
Apple___Healthy
Blueberry___Healthy
Cherry___Powdery_mildew
Cherry___Healthy
Corn___Cercospora_leaf_spot Gray_leaf_spot
Corn___Common_rust
Corn___Northern_Leaf_Blight
Corn___Healthy
Grape___Black_rot
Grape___Esca_(Black_Measles)
Grape___Leaf_blight_(Isariopsis_Leaf_Spot)
Grape___Healthy
Orange___Haunglongbing_(Citrus_greening)
Peach___Bacterial_spot
Peach___Healthy
Pepper,_bell___Bacterial_spot
Pepper,_bell___Healthy
Potato___Early_blight
Potato___Late_blight
Potato___Healthy
Raspberry___Healthy
Soybean___Healthy
Squash___Powdery_mildew
Strawberry___Leaf_scorch
Strawberry___Healthy
Tomato___Bacterial_spot
Tomato___Early_blight
Tomato___Late_blight
Tomato___Leaf_Mold
Tomato___Septoria_leaf_spot
Tomato___Spider_mites Two-spotted_spider_mite
Tomato___Target_Spot
Tomato___Tomato_Yellow_Leaf_Curl_Virus
Tomato___Tomato_mosaic_virus
Tomato___Healthy
```

### 12.5 New File: `model/model_contract.py` (85 lines)

```python
from pathlib import Path
from typing import Iterable, List, Sequence, Tuple

import numpy as np

ROOT = Path(__file__).resolve().parent.parent
DEFAULT_KERAS_MODEL = ROOT / "backend-api" / "models" / "leafguard_model.keras"
DEFAULT_LABELS = ROOT / "model" / "labels-38.txt"
BACKEND_LABELS = ROOT / "backend-api" / "labels-38.txt"
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
    input_tensor = model.inputs[0]
    dtype_name = getattr(input_tensor.dtype, "name", str(input_tensor.dtype))
    if dtype_name != "float32":
        raise ValueError(f"Expected Keras float32 input, got {input_tensor.dtype}")
    return input_shape, output_shape


def find_embedded_rescaling(model):
    pending = list(model.layers)
    while pending:
        layer = pending.pop(0)
        if layer.__class__.__name__ == "Rescaling":
            config = layer.get_config()
            scale = float(config.get("scale"))
            offset = float(config.get("offset", 0.0))
            if np.isclose(scale, 1.0 / 127.5, rtol=1e-6, atol=1e-8) and np.isclose(
                offset, -1.0, rtol=1e-6, atol=1e-8
            ):
                return layer
        pending.extend(getattr(layer, "layers", []))

    from tensorflow import keras

    for operation in getattr(model, "_operations", []):
        if operation.__class__.__name__ != "Subtract":
            continue
        try:
            probe = keras.Model(inputs=model.inputs, outputs=operation.output)
            valid_scaling = True
            for value, expected in ((0.0, -1.0), (127.5, 0.0), (255.0, 1.0)):
                image = np.full(EXPECTED_INPUT_SHAPE, value, dtype=np.float32)
                output = np.asarray(probe([image], training=False))
                if output.shape != EXPECTED_INPUT_SHAPE or not np.allclose(
                    output, expected, rtol=1e-6, atol=1e-6
                ):
                    valid_scaling = False
                    break
        except (AttributeError, TypeError, ValueError):
            continue
        if valid_scaling:
            return operation

    raise ValueError(
        "Expected embedded preprocessing mapping raw RGB [0, 255] to [-1, 1]."
    )
```

### 12.6 New File: `model/inspect_model.py` (39 lines)

```python
#!/usr/bin/env python3
import argparse
from pathlib import Path

import tensorflow as tf

from model_contract import (
    DEFAULT_KERAS_MODEL,
    DEFAULT_LABELS,
    find_embedded_rescaling,
    load_labels,
    validate_keras_model,
)


def main() -> None:
    parser = argparse.ArgumentParser(description="Inspect the LeafGuard Keras model contract.")
    parser.add_argument("--keras-model", type=Path, default=DEFAULT_KERAS_MODEL)
    parser.add_argument("--labels", type=Path, default=DEFAULT_LABELS)
    args = parser.parse_args()

    labels = load_labels(args.labels)
    print(f"Labels: {len(labels)} ({args.labels})")
    print("Caller preprocessing: resize to 224x224 RGB and keep raw float32 [0, 255].")

    if not args.keras_model.is_file():
        raise FileNotFoundError(f"Keras model not found: {args.keras_model}")

    model = tf.keras.models.load_model(args.keras_model)
    input_shape, output_shape = validate_keras_model(model, labels)
    rescaling = find_embedded_rescaling(model)
    print(f"Keras input: shape={input_shape}, dtype={model.inputs[0].dtype}")
    print(f"Keras output: shape={output_shape}, dtype={model.outputs[0].dtype}")
    print(f"Embedded preprocessing: {rescaling.name} maps [0, 255] to [-1, 1]")
    print("Keras contract: valid")


if __name__ == "__main__":
    main()
```

### 12.7 New File: `model/test_model_contract.py` (56 lines)

```python
import unittest

import numpy as np

try:
    import tensorflow as tf
except ImportError:
    tf = None

from model_contract import (
    BACKEND_LABELS,
    DEFAULT_KERAS_MODEL,
    DEFAULT_LABELS,
    EXPECTED_CLASS_COUNT,
    EXPECTED_INPUT_SHAPE,
    find_embedded_rescaling,
    load_labels,
    validate_keras_model,
)


class CloudModelContractTest(unittest.TestCase):
    def test_canonical_and_backend_labels_match(self):
        canonical = load_labels(DEFAULT_LABELS)
        backend = load_labels(BACKEND_LABELS)
        self.assertEqual(EXPECTED_CLASS_COUNT, len(canonical))
        self.assertEqual(canonical, backend)

    @unittest.skipIf(tf is None, "TensorFlow is required for Keras contract checks")
    def test_approved_keras_model_contract(self):
        model = tf.keras.models.load_model(DEFAULT_KERAS_MODEL)
        input_shape, output_shape = validate_keras_model(model, load_labels())
        self.assertEqual((None, 224, 224, 3), input_shape)
        self.assertEqual((None, EXPECTED_CLASS_COUNT), output_shape)
        self.assertIsNotNone(find_embedded_rescaling(model))

    @unittest.skipIf(tf is None, "TensorFlow is required for Keras contract checks")
    def test_incorrect_input_shape_is_rejected(self):
        inputs = tf.keras.Input(shape=(256, 256, 3))
        outputs = tf.keras.layers.Dense(EXPECTED_CLASS_COUNT)(
            tf.keras.layers.GlobalAveragePooling2D()(inputs)
        )
        model = tf.keras.Model(inputs, outputs)
        with self.assertRaisesRegex(ValueError, "Expected Keras input shape"):
            validate_keras_model(model, load_labels())

    @unittest.skipIf(tf is None, "TensorFlow is required for preprocessing checks")
    def test_incorrect_embedded_scaling_is_rejected(self):
        inputs = tf.keras.Input(shape=EXPECTED_INPUT_SHAPE[1:])
        model = tf.keras.Model(inputs, inputs / 255.0 - 1.0)
        with self.assertRaisesRegex(ValueError, "Expected embedded preprocessing"):
            find_embedded_rescaling(model)


if __name__ == "__main__":
    unittest.main()
```

### 12.8 New File: `release-records/model-provenance.txt` (39 lines)

```text
LeafGuard AI - Week 06 Cloud Model Provenance
==============================================

Status: Approved for academic cloud-inference validation

Model name: model_4_mobilenet_finetuned.keras
Model role: Canonical Keras model for FastAPI real-mode inference
Source repository: https://github.com/Muhammad-Hassan12/Plant-Disease-Detector
Pinned source commit: f6165bd93524dfb77a9629aae70db845832d1b01
Source artifact path: Models/model_4_mobilenet_finetuned.keras
Pinned raw URL: https://raw.githubusercontent.com/Muhammad-Hassan12/Plant-Disease-Detector/f6165bd93524dfb77a9629aae70db845832d1b01/Models/model_4_mobilenet_finetuned.keras
Source repository license claim: MIT

Local model path: backend-api/models/leafguard_model.keras
Canonical labels: model/labels-38.txt
Expected input: RGB float32 [1, 224, 224, 3], raw [0,255]
Expected output: float32 [1, 38]
Embedded preprocessing: [0,255] to [-1,1]

Downloaded by: Raj-Indra-Asura
Download date: 2026-07-16
Local file size: 25143175 bytes
SHA-256: 08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f

License reviewed personally: Yes
Approved for LeafGuard academic use: Yes
Approved for redistribution/release: Yes

Week 06 validation:
- Model identity matched size and SHA-256.
- Keras input, output, dtype, labels, and embedded rescaling passed inspection.
- FastAPI /health reported use_mock=false, model_loaded=true, class_count=38.
- A real-mode /predict request returned HTTP 200 with the unchanged eight fields.

Limitations:
- Source-author accuracy is not independent LeafGuard accuracy evidence.
- Real phone-camera and broader class evaluation remain incomplete.
- TFLite conversion, offline inference, and device parity are later-week gates.
- Do not duplicate or replace the tracked model without provenance review; never commit secrets or private environment values.
```

### 12.9 Binary Artifact Record

The Keras file is binary and therefore not reproduced as text.

```text
Path: backend-api/models/leafguard_model.keras
Size: 25143175 bytes
SHA-256: 08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f
Git status: tracked at the exact path above
```

Verify:

```bash
stat -c '%s' backend-api/models/leafguard_model.keras
sha256sum backend-api/models/leafguard_model.keras
git ls-files backend-api/models/leafguard_model.keras
```

The first two outputs must match. The Git command must print the exact model path.

### 12.10 Files Week 06 Does Not Rewrite

| Area | Status | Reason |
|---|---|---|
| Week 05 Android files | Unchanged | API stays compatible |
| `backend-api/main.py` | Unchanged | Already validates upload and response |
| `backend-api/config.py` | Unchanged | Already defines model path and mode |
| `backend-api/model_loader.py` | Unchanged | Already loads and validates Keras shape |
| `backend-api/requirements.txt` | Unchanged | Already pins TensorFlow 2.19.1 |
| TFLite conversion scripts | Deferred | Offline inference is later |
| Android `model.tflite` and classifier | Deferred | Not cloud inference |
| Room/history code | Deferred | Week 07 |

### 12.11 Verify End State

```bash
# Identity
stat -c '%s' backend-api/models/leafguard_model.keras
sha256sum backend-api/models/leafguard_model.keras
cmp model/labels-38.txt backend-api/labels-38.txt

# Contract
backend-api/.venv/bin/python model/inspect_model.py
cd model
../backend-api/.venv/bin/python -m unittest -v test_model_contract

# Real API
cd ../backend-api
USE_MOCK=false .venv/bin/uvicorn main:app --reload
```

Expected focused test result:

```text
Ran 4 tests
OK
```

Expected health values:

```text
use_mock=false
model_loaded=true
class_count=38
image_size=224
```

Then upload one real image through `/docs` or the unchanged Week 05 Android app.

---

## 13. Learning-to-Evidence Map

| Concept | Exercise | Build step | Proof |
|---|---|---|---|
| Artifact identity | 1 | 2 | Size/hash record |
| Tensor contract | 2 | 4 | Inspector output |
| Label order | 3 | 3 | Comparison and test |
| Embedded scaling | 4 | 4 | Inspector and rejection test |
| Real-mode health | 5 | 6 | `/health` screenshot |
| Stable mobile API | 5 | 7 | Eight-field response |
| Limitations | 6 | 8 | Evidence note and reflection |

---

## 14. Week 06 Understanding Checklist

- [ ] I can explain why a model binary needs provenance and a hash.
- [ ] I can state the exact artifact size and local path.
- [ ] I can state the input and output shapes.
- [ ] I can explain why caller pixels remain in `[0,255]`.
- [ ] I can explain embedded `[0,255] -> [-1,1]` scaling.
- [ ] I can explain why label order cannot be sorted.
- [ ] I can distinguish confidence from accuracy.
- [ ] I can prove real mode through `/health`.
- [ ] I can run all four focused contract tests.
- [ ] I can demonstrate one real-mode HTTP 200 prediction.
- [ ] I can explain why Android does not change this week.
- [ ] I can explain why TFLite/offline work is deferred.

<!-- NAV_FOOTER_START -->

---

## Week 06 Navigation

| Step | File | Description |
|---:|---|---|
| 1 | [README.md](README.md) | Week overview |
| **2** | **learning-notes.md** - current | Theory and exact source snapshot |
| 3 | [exercises.md](exercises.md) | Guided practice |
| 4 | [build-task.md](build-task.md) | Implementation guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation and evidence |
| 6 | [quiz.md](quiz.md) | Knowledge assessment |
| 7 | [reflection.md](reflection.md) | Reflection and handoff |

[Previous: Week 05](../week-05-android-networking/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 07](../week-07-room-sqlite-history/README.md)
