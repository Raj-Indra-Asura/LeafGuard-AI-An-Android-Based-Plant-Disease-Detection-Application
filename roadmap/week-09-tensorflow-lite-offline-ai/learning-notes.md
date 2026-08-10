# Week 09 Learning Notes: Verified TFLite Offline Inference

## Purpose

These notes explain how to convert the approved Week 06 Keras artifact, validate TFLite parity, bundle exact Android assets, and add an offline inference branch that preserves the eight-field result contract.

Section 12 is the authoritative reconstruction appendix. It contains all 14 Week 09 new or expanded text files in full. The Android snapshot was compiled independently, and the focused TensorFlow suite passed four tests.

---

## 1. How Week 09 Grows From Week 08

Week 08 adds offline guidance, not offline prediction. Week 09 adds the missing inference engine:

```text
same selected image
  -> cloud branch: upload to Keras backend
  -> offline branch: decode Bitmap and run TFLite

same output:
  PredictionResponse with eight fields
  -> Week 08 Result XML enrichment
  -> Week 07 Room save
```

No Result, Room, XML schema, or API response field needs to change.

---

## 2. Why Convert Keras to TFLite

Keras is the approved training/cloud format. TFLite is designed for mobile deployment:

- compact FlatBuffer model
- Android interpreter runtime
- memory-mapped asset loading
- CPU execution without Python or FastAPI
- explicit tensor interface

Conversion is not retraining. It serializes compatible operations into another runtime format.

---

## 3. Conversion Must Be Gated

`convert_model.py` refuses conversion unless the source Keras model passes:

1. exact 38 unique labels
2. input shape/dtype
3. output count
4. embedded `[0,255] -> [-1,1]` preprocessing

After conversion, the script synchronizes the same model bytes and labels to Android asset locations.

A successful conversion command alone is not enough. Validate the generated artifact independently.

---

## 4. Exact TFLite Artifact Identity

| Property | Value |
|---|---|
| Source | Approved Week 06 Keras artifact |
| Android path | `app/src/main/assets/model.tflite` |
| Size | 9,056,916 bytes |
| SHA-256 | `22ea2d4a47a52b2d9b150e0f74b113def0f12bbdb59209f7e0bce2a9701d41f9` |
| Precision | Float32, no quantization |
| Labels | 38 canonical lines |

The binary is not pasted into Markdown. Hash and size identify it exactly.

---

## 5. Tensor Contract and Embedded Scaling

The TFLite model preserves the Keras interface:

```text
input:  float32 [1,224,224,3]
output: float32 [1,38]
```

Android caller work:

1. decode image URI into `Bitmap`
2. resize to 224 x 224
3. iterate rows and columns
4. write R, G, B as raw float values from 0 to 255
5. rewind the ByteBuffer

Do not divide by 255. The converted graph still performs embedded scaling to `[-1,1]`.

---

## 6. ByteBuffer Size and Layout

Each float uses 4 bytes:

$$
224 \times 224 \times 3 \times 4 = 602{,}112\text{ bytes}
$$

The classifier allocates a direct `ByteBuffer` in native byte order. Pixel order is:

```text
row 0: RGB, RGB, RGB, ...
row 1: RGB, RGB, RGB, ...
```

Wrong channel order, missing rewind, wrong dtype, or double normalization can produce plausible but incorrect predictions without an exception.

---

## 7. Labels and Output Decoding

`labels.txt` must exactly match the canonical Week 06 label order.

```kotlin
bestIndex = argmax(scores)
modelLabel = labels[bestIndex]
```

The classifier validates:

- 38 non-empty labels
- no duplicates
- TFLite output dimension equals label count

Display formatting changes underscores for UI only; it does not reorder model labels.

---

## 8. Classifier Lifecycle

`TFLiteClassifier` performs:

```text
construct
  -> load labels
  -> memory-map model
  -> create Interpreter with 4 threads
  -> validate tensors and label count

classify
  -> preprocess Bitmap
  -> interpreter.run
  -> argmax
  -> map display name and local guidance
  -> build PredictionResponse

close
  -> interpreter.close
```

The caller uses `.use { ... }` so `close()` runs on success or exception.

---

## 9. Shared Eight-Field Response

Offline mode returns exactly:

```text
model_label, disease, confidence, uncertain,
guidance_available, symptoms, treatment, prevention
```

`uncertain` is true below `0.50`. Local XML guidance is used when the display name matches one of the 10 reviewed entries; otherwise safe generic guidance is returned.

ResultActivity performs its existing Week 08 lookup again safely and Room stores the final values.

---

## 10. Cloud/Offline Strategy in Scan

Week 09 adds one simple mode selector:

| Mode | Execution | Backend required |
|---|---|---|
| Cloud | Existing Retrofit upload | Yes |
| Offline | TFLite classifier on `Dispatchers.IO` | No |

Both branches:

- require a selected image
- show one progress state
- disable image/mode controls while running
- restore controls after success/failure
- call the same `openResult(prediction)` method

This is a strategy choice, not two separate result workflows.

---

## 11. Parity, Accuracy, and Failure Boundaries

Parity asks whether conversion changed model behavior. The focused check requires:

- same top-1 index for Keras and TFLite
- confidence delta at most 0.02

The reproduced three-image check passed with maximum delta below `0.000015`.

However, all three predicted `Blueberry___Healthy`, which did not match their tomato sample folders. Therefore conversion fidelity passed while prediction correctness did not.

Safe offline failures include:

- missing/corrupt model asset
- empty/wrong labels
- incompatible tensor shape
- bitmap decode failure
- closed interpreter

These show a user-safe error, restore controls, and never silently switch modes or fabricate output.

Avoid adding Week 10 notifications, location, sharing, analytics, settings-driven thresholds, or UI redesign.

---

## 12. End-of-Week-09 File Inventory (Exact Files, Exact Code, Exact Size)

Week 09 starts from the compiled Week 08 XML/Room/network state. It creates 8 text files, expands 6 text files, and supplies one generated local TFLite binary.

### 12.1 Change Summary: Week 08 -> Week 09

| Change | Count | Files |
|---|---:|---|
| New text | 8 | Conversion/validation/parity/test, classifier, labels, asset README, provenance |
| Expanded text | 6 | Shared model contract/notes, Gradle, Scan Activity/layout, strings |
| Local binary | 1 | `model.tflite` |
| Result/Room/XML/API changes | 0 | Existing shared contracts remain unchanged |
| Week 10 changes | 0 | No notification/location/share/polish |

**Complete Week 09 text snapshot: 1,182 logical lines.**

### 12.2 Exact Week 09 Tree

```text
LeafGuard-AI/
|-- model/
|   |-- model_contract.py                    EXPANDED  117 lines
|   |-- model-notes.md                       EXPANDED   55 lines
|   |-- convert_model.py                     NEW        47 lines
|   |-- validate_tflite.py                   NEW        31 lines
|   |-- parity_test.py                       NEW        58 lines
|   `-- test_tflite_contract.py              NEW        73 lines
|-- release-records/
|   `-- tflite-provenance.txt                NEW        33 lines
`-- android-app-kotlin/app/
    |-- build.gradle                         EXPANDED   60 lines
    `-- src/main/
        |-- java/com/leafguard/
        |   |-- ScanActivity.kt              EXPANDED  321 lines
        |   `-- ml/TFLiteClassifier.kt       NEW       149 lines
        |-- res/
        |   |-- layout/activity_scan.xml     EXPANDED  111 lines
        |   `-- values/strings.xml           EXPANDED   82 lines
        `-- assets/
            |-- model.tflite                 LOCAL/BINARY 9,056,916 bytes
            |-- labels.txt                   NEW        38 lines
            `-- README.md                    NEW         7 lines
```

### 12.3 Expanded File: `model/model_contract.py` (85 -> 117 lines)

```python
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
        "Expected embedded preprocessing mapping raw RGB [0, 255] to [-1, 1]. "
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
```

### 12.4 New File: `model/convert_model.py` (47 lines)

```python
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
```

### 12.5 New File: `model/validate_tflite.py` (31 lines)

```python
#!/usr/bin/env python3
import argparse
from pathlib import Path

import numpy as np
import tensorflow as tf

from model_contract import DEFAULT_LABELS, load_labels, preprocess_image, tensor_details


def main() -> None:
    parser = argparse.ArgumentParser(description="Run one image through the LeafGuard TFLite model.")
    parser.add_argument("image", type=Path)
    parser.add_argument("--model", type=Path, required=True)
    parser.add_argument("--labels", type=Path, default=DEFAULT_LABELS)
    args = parser.parse_args()

    labels = load_labels(args.labels)
    interpreter = tf.lite.Interpreter(model_path=str(args.model))
    interpreter.allocate_tensors()
    input_detail, output_detail = tensor_details(interpreter)
    interpreter.set_tensor(input_detail["index"], preprocess_image(args.image))
    interpreter.invoke()
    scores = interpreter.get_tensor(output_detail["index"])[0]
    best_index = int(np.argmax(scores))
    print(f"Prediction: {labels[best_index]}")
    print(f"Confidence: {float(scores[best_index]):.6f}")


if __name__ == "__main__":
    main()
```

### 12.6 New File: `model/parity_test.py` (58 lines)

```python
#!/usr/bin/env python3
import argparse
from pathlib import Path

import numpy as np
import tensorflow as tf

from model_contract import (
    DEFAULT_KERAS_MODEL,
    DEFAULT_LABELS,
    load_labels,
    preprocess_image,
    tensor_details,
    validate_keras_model,
)


def main() -> None:
    parser = argparse.ArgumentParser(description="Compare Keras and TFLite predictions for images.")
    parser.add_argument("images", type=Path, nargs="+")
    parser.add_argument("--keras-model", type=Path, default=DEFAULT_KERAS_MODEL)
    parser.add_argument("--tflite-model", type=Path, required=True)
    parser.add_argument("--labels", type=Path, default=DEFAULT_LABELS)
    parser.add_argument("--max-confidence-delta", type=float, default=0.02)
    args = parser.parse_args()

    labels = load_labels(args.labels)
    keras_model = tf.keras.models.load_model(args.keras_model)
    validate_keras_model(keras_model, labels)
    interpreter = tf.lite.Interpreter(model_path=str(args.tflite_model))
    interpreter.allocate_tensors()
    input_detail, output_detail = tensor_details(interpreter)

    failed = False
    for image_path in args.images:
        image = preprocess_image(image_path)
        keras_scores = np.asarray(keras_model.predict(image, verbose=0))[0]
        interpreter.set_tensor(input_detail["index"], image)
        interpreter.invoke()
        tflite_scores = interpreter.get_tensor(output_detail["index"])[0]
        keras_index = int(np.argmax(keras_scores))
        tflite_index = int(np.argmax(tflite_scores))
        delta = abs(float(keras_scores[keras_index]) - float(tflite_scores[tflite_index]))
        passed = keras_index == tflite_index and delta <= args.max_confidence_delta
        failed = failed or not passed
        print(
            f"{image_path}: {'PASS' if passed else 'FAIL'} "
            f"Keras={labels[keras_index]} ({keras_scores[keras_index]:.6f}) "
            f"TFLite={labels[tflite_index]} ({tflite_scores[tflite_index]:.6f}) "
            f"delta={delta:.6f}"
        )

    if failed:
        raise SystemExit("Keras/TFLite parity failed")


if __name__ == "__main__":
    main()
```

### 12.7 New File: `model/test_tflite_contract.py` (73 lines)

```python
import unittest
from pathlib import Path

import numpy as np
import tensorflow as tf

from model_contract import (
    DEFAULT_KERAS_MODEL,
    DEFAULT_LABELS,
    EXPECTED_CLASS_COUNT,
    EXPECTED_INPUT_SHAPE,
    load_labels,
    preprocess_image,
    tensor_details,
    validate_keras_model,
)

ROOT = Path(__file__).resolve().parent.parent
TFLITE_MODEL = ROOT / "android-app-kotlin" / "app" / "src" / "main" / "assets" / "model.tflite"
ANDROID_LABELS = ROOT / "android-app-kotlin" / "app" / "src" / "main" / "assets" / "labels.txt"
SAMPLES = ROOT / "sample-images"


class TFLiteContractTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.labels = load_labels(DEFAULT_LABELS)
        cls.interpreter = tf.lite.Interpreter(model_path=str(TFLITE_MODEL))
        cls.interpreter.allocate_tensors()
        cls.input_detail, cls.output_detail = tensor_details(cls.interpreter)

    def test_android_labels_match_canonical_order(self):
        self.assertEqual(self.labels, load_labels(ANDROID_LABELS))
        self.assertEqual(EXPECTED_CLASS_COUNT, len(self.labels))

    def test_tflite_tensor_contract(self):
        self.assertEqual(EXPECTED_INPUT_SHAPE, tuple(self.input_detail["shape"]))
        self.assertEqual(np.float32, self.input_detail["dtype"])
        self.assertEqual((1, EXPECTED_CLASS_COUNT), tuple(self.output_detail["shape"]))
        self.assertEqual(np.float32, self.output_detail["dtype"])

    def test_preprocessing_keeps_raw_rgb_values(self):
        image = preprocess_image(SAMPLES / "healthy" / "tomato_healthy_01.png")
        self.assertEqual(EXPECTED_INPUT_SHAPE, image.shape)
        self.assertEqual(np.float32, image.dtype)
        self.assertGreaterEqual(float(image.min()), 0.0)
        self.assertLessEqual(float(image.max()), 255.0)

    def test_three_sample_keras_tflite_parity(self):
        keras_model = tf.keras.models.load_model(DEFAULT_KERAS_MODEL)
        validate_keras_model(keras_model, self.labels)
        samples = [
            SAMPLES / "early_blight" / "tomato_early_blight_01.png",
            SAMPLES / "healthy" / "tomato_healthy_01.png",
            SAMPLES / "late_blight" / "tomato_late_blight_01.png",
        ]
        for image_path in samples:
            image = preprocess_image(image_path)
            keras_scores = np.asarray(keras_model.predict(image, verbose=0))[0]
            self.interpreter.set_tensor(self.input_detail["index"], image)
            self.interpreter.invoke()
            tflite_scores = self.interpreter.get_tensor(self.output_detail["index"])[0]
            keras_index = int(np.argmax(keras_scores))
            tflite_index = int(np.argmax(tflite_scores))
            self.assertEqual(keras_index, tflite_index)
            self.assertLessEqual(
                abs(float(keras_scores[keras_index]) - float(tflite_scores[tflite_index])),
                0.02,
            )


if __name__ == "__main__":
    unittest.main()
```

### 12.8 Expanded File: `model/model-notes.md` (54 -> 55 lines)

````markdown
# LeafGuard Week 09 Offline TFLite Contract

## Source and Conversion

- Source Keras artifact: `backend-api/models/leafguard_model.keras`
- Keras SHA-256: `08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f`
- Conversion script: `model/convert_model.py`
- Android TFLite path: `android-app-kotlin/app/src/main/assets/model.tflite`
- TFLite size: 9,056,916 bytes
- TFLite SHA-256: `22ea2d4a47a52b2d9b150e0f74b113def0f12bbdb59209f7e0bce2a9701d41f9`
- Android labels: `app/src/main/assets/labels.txt`

The TFLite binary is generated from the approved Week 06 Keras artifact and is intentionally not pasted into learning notes. Verify identity after every conversion.

## Tensor Contract

- Input: one `float32` tensor `[1, 224, 224, 3]`
- Color order: RGB
- Android caller range: raw `[0,255]`
- Embedded model preprocessing: `[0,255] -> [-1,1]`
- Output: one `float32` tensor `[1,38]`
- Decode: `argmax(output[0])` using the exact canonical 38-label order
- Uncertain: confidence below `0.50`

Do not divide Android pixels by 255. The converted model preserves embedded preprocessing.

## Compatibility

Both cloud and offline branches return the same eight-field `PredictionResponse`:

`model_label`, `disease`, `confidence`, `uncertain`, `guidance_available`, `symptoms`, `treatment`, `prevention`.

The existing Result enrichment and Room save flow remain unchanged.

## Validation

```bash
python model/validate_tflite.py sample-images/healthy/tomato_healthy_01.png \
	--model android-app-kotlin/app/src/main/assets/model.tflite
python model/parity_test.py sample-images/early_blight/tomato_early_blight_01.png \
	sample-images/healthy/tomato_healthy_01.png \
	sample-images/late_blight/tomato_late_blight_01.png \
	--tflite-model android-app-kotlin/app/src/main/assets/model.tflite
cd model && python -m unittest -v test_tflite_contract
```

Parity proves conversion consistency, not prediction correctness. In the reproduced three-image check, Keras and TFLite matched with maximum confidence delta below `0.000015`, while the predicted labels did not match the sample folder names.

## Limitations

- Offline inference does not require FastAPI or Internet.
- XML guidance is local reference content, not inference.
- Device speed and memory must be tested manually.
- Three-image parity is not an accuracy evaluation.
- Unsupported classes, poor lighting, blur, and dataset shift remain risks.
````

### 12.9 New File: `release-records/tflite-provenance.txt` (33 lines)

```text
LeafGuard AI - Week 09 TFLite Provenance
========================================

Status: Conversion and desktop parity validated; device validation required

Source Keras path: backend-api/models/leafguard_model.keras
Source Keras SHA-256: 08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f
Conversion tool: TensorFlow 2.19.1 TFLiteConverter
Conversion script: model/convert_model.py
Optimization/quantization: none; float32 model

TFLite path: android-app-kotlin/app/src/main/assets/model.tflite
TFLite size: 9056916 bytes
TFLite SHA-256: 22ea2d4a47a52b2d9b150e0f74b113def0f12bbdb59209f7e0bce2a9701d41f9
Labels path: android-app-kotlin/app/src/main/assets/labels.txt
Label count: 38 unique labels in canonical order

Tensor contract:
- Input: RGB float32 [1,224,224,3], raw [0,255]
- Embedded preprocessing: [0,255] to [-1,1]
- Output: float32 [1,38]

Reproduced validation:
- TFLite tensor contract passed.
- Android labels matched canonical labels.
- Three repository samples preserved Keras/TFLite top-1 index.
- Maximum observed confidence delta was below 0.000015.
- Android teaching snapshot compiled successfully.

Limitations:
- The three sample predictions did not match their folder labels; parity is not accuracy.
- Physical-device airplane-mode, latency, memory, and repeated-run tests remain manual gates.
- The binary should not be duplicated in evidence or silently replaced without updating this record.
```

### 12.10 Expanded File: `app/build.gradle` (55 -> 60 lines)

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
}

android {
    namespace 'com.leafguard'
    compileSdk 34

    defaultConfig {
        applicationId "com.leafguard"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "0.1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig true
    }

    aaptOptions {
        noCompress "tflite"
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
    implementation 'org.tensorflow:tensorflow-lite:2.14.0'

    def room_version = "2.6.1"
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
}
```

### 12.11 New File: `ml/TFLiteClassifier.kt` (149 lines)

```kotlin
package com.leafguard.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.leafguard.data.DiseaseRepository
import com.leafguard.network.PredictionResponse
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import org.tensorflow.lite.Interpreter

class TFLiteClassifier @Throws(IOException::class) constructor(
    context: Context,
    modelAssetName: String = "model.tflite",
    labelsAssetName: String = "labels.txt"
) : AutoCloseable {
    private val appContext = context.applicationContext
    private val labels = loadLabels(labelsAssetName)
    private var interpreter: Interpreter? = loadInterpreter(modelAssetName)
    private val outputClasses: Int

    init {
        val activeInterpreter = checkNotNull(interpreter)
        val inputShape = activeInterpreter.getInputTensor(0).shape()
        val outputShape = activeInterpreter.getOutputTensor(0).shape()
        if (!inputShape.contentEquals(intArrayOf(1, INPUT_SIZE, INPUT_SIZE, RGB_CHANNELS))) {
            close()
            throw IOException("Expected TFLite input [1, 224, 224, 3]")
        }
        if (outputShape.size != 2 || outputShape[0] != 1) {
            close()
            throw IOException("Expected TFLite output [1, class_count]")
        }
        outputClasses = outputShape[1]
        if (outputClasses != labels.size) {
            close()
            throw IOException("TFLite output count does not match labels")
        }
    }

    fun classify(bitmap: Bitmap): PredictionResponse {
        val activeInterpreter = checkNotNull(interpreter) { "TFLite interpreter is closed" }
        val output = Array(1) { FloatArray(outputClasses) }
        activeInterpreter.run(preprocess(bitmap), output)
        val bestIndex = argmax(output[0])
        val confidence = output[0][bestIndex]
        val modelLabel = labels[bestIndex]
        val displayName = displayLabel(modelLabel)
        val guidance = try {
            DiseaseRepository.getInstance(appContext).findByName(displayName)
        } catch (exception: Exception) {
            null
        }
        return PredictionResponse(
            modelLabel = modelLabel,
            disease = displayName,
            confidence = confidence,
            uncertain = confidence < CONFIDENCE_THRESHOLD,
            guidanceAvailable = guidance != null,
            symptoms = guidance?.symptoms ?: GENERIC_SYMPTOMS,
            treatment = guidance?.treatment ?: GENERIC_TREATMENT,
            prevention = guidance?.prevention ?: GENERIC_PREVENTION
        )
    }

    private fun preprocess(bitmap: Bitmap): ByteBuffer {
        val scaled = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true)
        val buffer = ByteBuffer.allocateDirect(
            INPUT_SIZE * INPUT_SIZE * RGB_CHANNELS * FLOAT_BYTES
        ).order(ByteOrder.nativeOrder())
        for (y in 0 until INPUT_SIZE) {
            for (x in 0 until INPUT_SIZE) {
                val pixel = scaled.getPixel(x, y)
                buffer.putFloat(Color.red(pixel).toFloat())
                buffer.putFloat(Color.green(pixel).toFloat())
                buffer.putFloat(Color.blue(pixel).toFloat())
            }
        }
        buffer.rewind()
        if (scaled !== bitmap) scaled.recycle()
        return buffer
    }

    private fun loadLabels(assetName: String): List<String> {
        val loaded = mutableListOf<String>()
        BufferedReader(InputStreamReader(appContext.assets.open(assetName))).use { reader ->
            reader.forEachLine { line ->
                val value = line.trim()
                if (value.isNotEmpty() && !value.startsWith("#")) loaded += value
            }
        }
        if (loaded.size != EXPECTED_CLASSES || loaded.size != loaded.toSet().size) {
            throw IOException("Expected 38 unique labels")
        }
        return loaded
    }

    private fun loadInterpreter(assetName: String): Interpreter {
        val descriptor = appContext.assets.openFd(assetName)
        FileInputStream(descriptor.fileDescriptor).use { input ->
            val model = input.channel.map(
                FileChannel.MapMode.READ_ONLY,
                descriptor.startOffset,
                descriptor.declaredLength
            )
            return Interpreter(model, Interpreter.Options().apply { setNumThreads(4) })
        }
    }

    private fun argmax(scores: FloatArray): Int {
        var bestIndex = 0
        for (index in 1 until scores.size) {
            if (scores[index] > scores[bestIndex]) bestIndex = index
        }
        return bestIndex
    }

    private fun displayLabel(label: String): String = when (label) {
        "Apple___Apple_scab" -> "Apple Scab"
        "Corn___Cercospora_leaf_spot Gray_leaf_spot" -> "Corn Gray Leaf Spot"
        "Corn___Northern_Leaf_Blight" -> "Corn Northern Leaf Blight"
        "Potato___Early_blight" -> "Potato Early Blight"
        "Potato___Late_blight" -> "Potato Late Blight"
        "Tomato___Early_blight" -> "Tomato Early Blight"
        "Tomato___Late_blight" -> "Tomato Late Blight"
        else -> label.replace("___", " ").replace('_', ' ')
    }

    override fun close() {
        interpreter?.close()
        interpreter = null
    }

    companion object {
        private const val INPUT_SIZE = 224
        private const val RGB_CHANNELS = 3
        private const val FLOAT_BYTES = 4
        private const val EXPECTED_CLASSES = 38
        private const val CONFIDENCE_THRESHOLD = 0.50f
        private const val GENERIC_SYMPTOMS = "Detailed symptoms are not available locally."
        private const val GENERIC_TREATMENT = "Verify this result with a qualified agricultural source."
        private const val GENERIC_PREVENTION = "Retake unclear images and continue monitoring the plant."
    }
}
```

### 12.12 Expanded File: `ScanActivity.kt` (247 -> 321 lines)

```kotlin
package com.leafguard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.leafguard.ml.TFLiteClassifier
import com.leafguard.network.PredictionResponse
import com.leafguard.network.RetrofitClient
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanActivity : AppCompatActivity() {

    private lateinit var imagePreview: ImageView
    private lateinit var textImageStatus: TextView
    private lateinit var buttonDetectDisease: Button
    private lateinit var progressUpload: ProgressBar
    private lateinit var radioDetectionMode: RadioGroup
    private lateinit var textDetectionModeDescription: TextView

    private var selectedImageUri: Uri? = null
    private var pendingCameraUri: Uri? = null
    private var offlineMode = false

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera()
        } else {
            Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        val cameraUri = pendingCameraUri
        if (success && cameraUri != null) {
            updateSelectedImage(cameraUri)
        } else {
            Toast.makeText(this, R.string.camera_cancelled, Toast.LENGTH_SHORT).show()
        }
        pendingCameraUri = null
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            updateSelectedImage(uri)
        } else {
            Toast.makeText(this, R.string.gallery_cancelled, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        imagePreview = findViewById(R.id.imagePreview)
        textImageStatus = findViewById(R.id.textImageStatus)
        buttonDetectDisease = findViewById(R.id.buttonDetectDisease)
        progressUpload = findViewById(R.id.progressUpload)
        radioDetectionMode = findViewById(R.id.radioDetectionMode)
        textDetectionModeDescription = findViewById(R.id.textDetectionModeDescription)

        findViewById<Button>(R.id.buttonTakePhoto).setOnClickListener {
            openCameraWithPermissionCheck()
        }
        findViewById<Button>(R.id.buttonChooseGallery).setOnClickListener {
            galleryLauncher.launch("image/*")
        }
        buttonDetectDisease.setOnClickListener {
            detectSelectedImage()
        }
        radioDetectionMode.setOnCheckedChangeListener { _, checkedId ->
            offlineMode = checkedId == R.id.radioOfflineMode
            textDetectionModeDescription.setText(
                if (offlineMode) {
                    R.string.detection_mode_offline_description
                } else {
                    R.string.detection_mode_cloud_description
                }
            )
        }

        savedInstanceState?.getString(KEY_SELECTED_IMAGE_URI)?.let { uriText ->
            updateSelectedImage(Uri.parse(uriText))
        }
    }

    private fun openCameraWithPermissionCheck() {
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            launchCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun launchCamera() {
        try {
            val imageUri = createImageUri()
            pendingCameraUri = imageUri
            cameraLauncher.launch(imageUri)
        } catch (exception: IOException) {
            pendingCameraUri = null
            Toast.makeText(this, R.string.camera_file_error, Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    private fun createImageUri(): Uri {
        val imageDirectory = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captures")
        if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
            throw IOException("Could not create image directory")
        }

        val imageFile = File(imageDirectory, "leafguard_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            this,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            imageFile
        )
    }

    private fun updateSelectedImage(uri: Uri) {
        selectedImageUri = uri
        imagePreview.setImageURI(uri)
        textImageStatus.setText(R.string.image_ready_for_upload)
        buttonDetectDisease.isEnabled = true
    }

    private fun detectSelectedImage() {
        val imageUri = selectedImageUri
        if (imageUri == null) {
            Toast.makeText(this, R.string.select_image_first, Toast.LENGTH_SHORT).show()
            return
        }

        setUploadInProgress(true)
        if (offlineMode) {
            runOfflineDetection(imageUri)
        } else {
            runCloudDetection(imageUri)
        }
    }

    private fun runCloudDetection(imageUri: Uri) {
        val uploadFile = try {
            copyUriToCacheFile(imageUri)
        } catch (exception: IOException) {
            setUploadInProgress(false)
            Toast.makeText(this, R.string.image_prepare_error, Toast.LENGTH_LONG).show()
            return
        }

        val mimeType = contentResolver.getType(imageUri) ?: "image/*"
        val requestBody = uploadFile.asRequestBody(mimeType.toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", uploadFile.name, requestBody)

        RetrofitClient.apiService.uploadImage(imagePart).enqueue(
            object : Callback<PredictionResponse> {
                override fun onResponse(
                    call: Call<PredictionResponse>,
                    response: Response<PredictionResponse>
                ) {
                    uploadFile.delete()
                    setUploadInProgress(false)
                    val prediction = response.body()
                    if (!response.isSuccessful || prediction == null) {
                        Toast.makeText(
                            this@ScanActivity,
                            getString(R.string.server_error_format, response.code()),
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                    openResult(prediction)
                }

                override fun onFailure(
                    call: Call<PredictionResponse>,
                    throwable: Throwable
                ) {
                    uploadFile.delete()
                    setUploadInProgress(false)
                    Toast.makeText(
                        this@ScanActivity,
                        R.string.network_error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    private fun runOfflineDetection(imageUri: Uri) {
        lifecycleScope.launch {
            try {
                val prediction = withContext(Dispatchers.IO) {
                    TFLiteClassifier(applicationContext).use { classifier ->
                        classifier.classify(loadBitmap(imageUri))
                    }
                }
                setUploadInProgress(false)
                openResult(prediction)
            } catch (exception: IOException) {
                setUploadInProgress(false)
                Toast.makeText(
                    this@ScanActivity,
                    R.string.offline_prediction_error,
                    Toast.LENGTH_LONG
                ).show()
            } catch (exception: RuntimeException) {
                setUploadInProgress(false)
                Toast.makeText(
                    this@ScanActivity,
                    R.string.offline_prediction_error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @Throws(IOException::class)
    private fun copyUriToCacheFile(uri: Uri): File {
        val uploadFile = File(cacheDir, "leafguard_upload_${System.currentTimeMillis()}.jpg")
        try {
            contentResolver.openInputStream(uri).use { inputStream ->
                if (inputStream == null) {
                    throw IOException("Unable to open selected image")
                }
                FileOutputStream(uploadFile).use { outputStream ->
                    inputStream.copyTo(outputStream, bufferSize = 8192)
                }
            }
        } catch (exception: IOException) {
            uploadFile.delete()
            throw exception
        }
        return uploadFile
    }

    @Throws(IOException::class)
    private fun loadBitmap(uri: Uri): Bitmap {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            return ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, false)
        }
        @Suppress("DEPRECATION")
        return MediaStore.Images.Media.getBitmap(contentResolver, uri)
    }

    private fun openResult(prediction: PredictionResponse) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_MODEL_LABEL, prediction.modelLabel)
            putExtra(ResultActivity.EXTRA_DISEASE, prediction.disease)
            putExtra(ResultActivity.EXTRA_CONFIDENCE, prediction.confidence)
            putExtra(ResultActivity.EXTRA_UNCERTAIN, prediction.uncertain)
            putExtra(ResultActivity.EXTRA_GUIDANCE_AVAILABLE, prediction.guidanceAvailable)
            putExtra(ResultActivity.EXTRA_SYMPTOMS, prediction.symptoms)
            putExtra(ResultActivity.EXTRA_TREATMENT, prediction.treatment)
            putExtra(ResultActivity.EXTRA_PREVENTION, prediction.prevention)
        }
        startActivity(intent)
    }

    private fun setUploadInProgress(inProgress: Boolean) {
        progressUpload.visibility = if (inProgress) View.VISIBLE else View.GONE
        buttonDetectDisease.isEnabled = !inProgress && selectedImageUri != null
        findViewById<Button>(R.id.buttonTakePhoto).isEnabled = !inProgress
        findViewById<Button>(R.id.buttonChooseGallery).isEnabled = !inProgress
        for (index in 0 until radioDetectionMode.childCount) {
            radioDetectionMode.getChildAt(index).isEnabled = !inProgress
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SELECTED_IMAGE_URI, selectedImageUri?.toString())
    }

    companion object {
        private const val KEY_SELECTED_IMAGE_URI = "selected_image_uri"
    }
}
```

### 12.13 Expanded File: `activity_scan.xml` (76 -> 111 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screen_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="24dp">

        <TextView
            android:id="@+id/textScanTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/scan_title"
            android:textColor="@color/text_primary"
            android:textSize="24sp" />

        <TextView
            android:id="@+id/textScanInstruction"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="@string/scan_instruction"
            android:textColor="@color/text_secondary"
            android:textSize="16sp" />

        <ImageView
            android:id="@+id/imagePreview"
            android:layout_width="match_parent"
            android:layout_height="280dp"
            android:layout_marginTop="20dp"
            android:background="#E8F5E9"
            android:contentDescription="@string/scan_preview_description"
            android:scaleType="centerCrop" />

        <TextView
            android:id="@+id/textImageStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="@string/no_image_selected"
            android:textColor="@color/text_secondary" />

        <Button
            android:id="@+id/buttonTakePhoto"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:text="@string/take_photo" />

        <Button
            android:id="@+id/buttonChooseGallery"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/choose_from_gallery" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/detection_mode_title"
            android:textColor="@color/text_primary"
            android:textStyle="bold" />

        <RadioGroup
            android:id="@+id/radioDetectionMode"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:checkedButton="@id/radioCloudMode"
            android:orientation="horizontal">

            <RadioButton
                android:id="@+id/radioCloudMode"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/detection_mode_cloud" />

            <RadioButton
                android:id="@+id/radioOfflineMode"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/detection_mode_offline" />
        </RadioGroup>

        <TextView
            android:id="@+id/textDetectionModeDescription"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/detection_mode_cloud_description"
            android:textColor="@color/text_secondary" />

        <Button
            android:id="@+id/buttonDetectDisease"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:enabled="false"
            android:text="@string/detect_disease" />

        <ProgressBar
            android:id="@+id/progressUpload"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="12dp"
            android:contentDescription="@string/upload_progress_description"
            android:visibility="gone" />
    </LinearLayout>
</ScrollView>
```

### 12.14 Expanded File: `strings.xml` (76 -> 82 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">LeafGuard AI</string>

    <string name="home_title">LeafGuard AI</string>
    <string name="home_subtitle">Plant disease detection learning app</string>

    <string name="open_scan">Open Scan</string>
    <string name="open_result">Open Sample Result</string>
    <string name="open_history">Open History</string>
    <string name="open_library">Open Disease Library</string>
    <string name="open_settings">Open Settings</string>

    <string name="scan_title">Scan Leaf</string>
    <string name="result_title">Prediction Result</string>
    <string name="history_title">History</string>
    <string name="library_title">Disease Library</string>
    <string name="settings_title">Settings and About</string>

    <string name="placeholder_settings">Course project shell. Settings options will grow in later weeks.</string>

    <string name="disease_library_loading">Loading local disease library</string>
    <string name="disease_library_empty">No reviewed disease entries are available.</string>
    <string name="disease_library_error">Could not read the local disease library.</string>
    <string name="disease_invalid_name">Invalid disease selection.</string>
    <string name="disease_not_found">Disease entry not found.</string>
    <string name="disease_plant_format">Plant: %1$s</string>
    <string name="guidance_local_library">Guidance loaded from the reviewed local XML library.</string>

    <string name="scan_instruction">Take a photo or choose an image, then upload it to the Week 04 backend.</string>
    <string name="scan_preview_description">Preview of the selected leaf image</string>
    <string name="take_photo">Take Photo</string>
    <string name="choose_from_gallery">Choose from Gallery</string>
    <string name="no_image_selected">No image selected yet.</string>
    <string name="image_ready_for_upload">Image selected. Ready to detect.</string>
    <string name="camera_permission_denied">Camera permission denied. You can still choose from gallery.</string>
    <string name="camera_cancelled">Camera cancelled. No new image selected.</string>
    <string name="gallery_cancelled">Gallery closed. No new image selected.</string>
    <string name="camera_file_error">Could not prepare a file for the camera.</string>

    <string name="detect_disease">Detect Disease</string>
    <string name="detection_mode_title">Detection mode</string>
    <string name="detection_mode_cloud">Cloud</string>
    <string name="detection_mode_offline">Offline</string>
    <string name="detection_mode_cloud_description">Cloud mode uploads the image to the Week 06 backend.</string>
    <string name="detection_mode_offline_description">Offline mode runs the converted TFLite model on this device.</string>
    <string name="offline_prediction_error">Offline prediction failed. Verify model and label assets.</string>
    <string name="upload_progress_description">Uploading image for prediction</string>
    <string name="select_image_first">Select or capture an image first.</string>
    <string name="image_prepare_error">Could not prepare the selected image for upload.</string>
    <string name="server_error_format">Server rejected the request (HTTP %1$d).</string>
    <string name="network_error">Could not reach the backend. Check the server and emulator URL.</string>

    <string name="result_unknown">Unknown result</string>
    <string name="model_label_placeholder">Model label: unavailable</string>
    <string name="model_label_format">Model label: %1$s</string>
    <string name="confidence_placeholder">Confidence: 0%%</string>
    <string name="confidence_format">Confidence: %1$d%%</string>
    <string name="result_uncertain">Low-confidence result: verify before acting.</string>
    <string name="result_confident">Confidence is above the configured server threshold.</string>
    <string name="guidance_available">Reviewed project guidance is available.</string>
    <string name="guidance_not_reviewed">Detailed project guidance is not reviewed for this label.</string>
    <string name="symptoms_heading">Symptoms</string>
    <string name="treatment_heading">Treatment</string>
    <string name="prevention_heading">Prevention</string>
    <string name="guidance_unavailable">No information available.</string>

    <string name="save_to_history">Save to History</string>
    <string name="saved_to_history">Saved to History</string>
    <string name="history_saved">Result saved locally.</string>
    <string name="history_empty">No saved scans yet.</string>
    <string name="history_confidence_format">Confidence: %1$.1f%%</string>
    <string name="history_detail_title">Saved Scan</string>
    <string name="history_invalid_id">Invalid history record.</string>
    <string name="history_record_missing">This saved scan no longer exists.</string>
    <string name="delete_history">Delete Scan</string>
    <string name="delete_history_title">Delete saved scan?</string>
    <string name="delete_history_message">This removes the scan from this device.</string>
    <string name="delete">Delete</string>
    <string name="cancel">Cancel</string>
    <string name="history_deleted">Saved scan deleted.</string>
</resources>
```

### 12.15 New File: `assets/labels.txt` (38 lines)

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

### 12.16 New File: `assets/README.md` (7 lines)

```markdown
# Week 09 Android Model Assets

- `model.tflite`: generated float32 TFLite model, 9,056,916 bytes.
- `labels.txt`: 38 canonical labels in model output order.
- `diseases.xml`: 10 reviewed local guidance entries from Week 08.

Do not sort `labels.txt`, normalize RGB pixels twice, or replace `model.tflite` without rerunning tensor and parity validation.
```


### 12.17 Binary Artifact Record

```text
Path: android-app-kotlin/app/src/main/assets/model.tflite
Size: 9056916 bytes
SHA-256: 22ea2d4a47a52b2d9b150e0f74b113def0f12bbdb59209f7e0bce2a9701d41f9
Git status: local/ignored binary
```

Verify:

```bash
stat -c '%s' android-app-kotlin/app/src/main/assets/model.tflite
sha256sum android-app-kotlin/app/src/main/assets/model.tflite
cmp model/labels-38.txt android-app-kotlin/app/src/main/assets/labels.txt
```

### 12.18 Files Week 09 Does Not Rewrite

| Area | Status | Reason |
|---|---|---|
| `PredictionResponse.kt` | Unchanged | Both modes return same eight fields |
| Result/XML guidance files | Unchanged | Week 08 enrichment is reused |
| Room/history files | Unchanged | Week 07 persistence is reused |
| FastAPI/Keras backend | Unchanged | Cloud mode remains available |
| Camera/gallery code | Preserved inside expanded ScanActivity | Same selected URI |
| Manifest | Unchanged | TFLite needs no permission |
| Notifications/location/share/analytics | Absent | Week 10 |
| Settings-driven thresholds/UI redesign | Absent | Later polish |

### 12.19 Verify the Exact End State

```bash
# Asset identity and labels
stat -c '%s' android-app-kotlin/app/src/main/assets/model.tflite
sha256sum android-app-kotlin/app/src/main/assets/model.tflite
cmp model/labels-38.txt android-app-kotlin/app/src/main/assets/labels.txt

# Focused contract/parity tests
cd model
../backend-api/.venv/bin/python -m unittest -v test_tflite_contract

# Android build
cd ../android-app-kotlin
./gradlew assembleDebug
```

Expected focused result:

```text
Ran 4 tests
OK
```

Manual device/emulator checks:

| Test | Expected |
|---|---|
| Backend stopped + Offline | Result opens without network |
| Cloud with backend running | Existing Retrofit result works |
| Offline matching XML name | Local guidance represented |
| Offline Save | Week 07 Room stores eight final fields |
| Missing model/labels | Safe error; controls restored |
| Repeat offline runs | No interpreter/resource crash |
| Airplane mode on device | Offline result still works |

---

## 13. Learning-to-Evidence Map

| Concept | Exercise | Build step | Proof |
|---|---|---|---|
| Artifact identity | 1 | 2 | Size/hash record |
| Tensor/preprocessing | 2 | 3 | Contract tests |
| Labels/argmax | 2 | 4 | Canonical comparison |
| Conversion | 3 | 2 | Generated artifact record |
| Keras/TFLite parity | 3 | 3 | Three-image output |
| ByteBuffer/classifier | 4 | 5 | Offline emulator result |
| Shared mode strategy | 5 | 6 | Cloud/offline demos |
| Failure/resource cleanup | 5 | 7 | Missing-asset and repeat runs |
| Parity-not-accuracy | 6 | 8 | Honest evidence note |

---

## 14. Week 09 Understanding Checklist

- [ ] I can explain conversion versus retraining.
- [ ] I can state TFLite size and SHA-256.
- [ ] I can state input/output shapes and dtypes.
- [ ] I can explain why RGB values stay in `[0,255]`.
- [ ] I can calculate the 602,112-byte input buffer.
- [ ] I can explain direct native-order ByteBuffer use.
- [ ] I can explain exact 38-label order and argmax.
- [ ] I can trace interpreter construction/classification/close.
- [ ] I can explain cloud/offline shared response strategy.
- [ ] I can run four focused tests.
- [ ] I can explain three-image parity results.
- [ ] I can explain why parity is not accuracy.
- [ ] I can demonstrate offline mode without backend.
- [ ] I can demonstrate safe missing-asset failure.
- [ ] I can identify all 8 new and 6 expanded text files.
- [ ] I know Week 10 owns notifications/location/sharing/polish.

<!-- NAV_FOOTER_START -->

---

## Week 09 Navigation

| Step | File | Description |
|---:|---|---|
| 1 | [README.md](README.md) | Week overview |
| **2** | **learning-notes.md** - current | Theory and exact source snapshot |
| 3 | [exercises.md](exercises.md) | Guided practice |
| 4 | [build-task.md](build-task.md) | Implementation guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation and evidence |
| 6 | [quiz.md](quiz.md) | Knowledge assessment |
| 7 | [reflection.md](reflection.md) | Reflection and handoff |

[Previous: Week 08](../week-08-xml-disease-library/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 10](../week-10-notifications-share-location/README.md)
