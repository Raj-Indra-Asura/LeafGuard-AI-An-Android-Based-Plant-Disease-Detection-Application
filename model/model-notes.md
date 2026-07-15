# LeafGuard 38-Class Model Contract

## Approved artifact

- Intended source: `Muhammad-Hassan12/Plant-Disease-Detector`
- Pinned source commit inspected during integration: `f6165bd93524dfb77a9629aae70db845832d1b01`
- Artifact: `Models/model_4_mobilenet_finetuned.keras`
- Source size: 25,143,175 bytes
- Source repository license claim: MIT
- Local backend path: `backend-api/models/leafguard_model.keras`
- Canonical labels: `model/labels-38.txt`

The owner must review the source and license, download the artifact, record the download
date, and calculate its SHA-256 before distribution. The model binary is intentionally
ignored by Git.

## Tensor contract

- Architecture: fine-tuned MobileNetV2 Keras classifier
- Input: one `float32` tensor shaped `[1, 224, 224, 3]`
- Color: RGB
- Caller preprocessing: decode, convert to RGB, and resize to 224×224
- Caller pixel range: raw float values in `[0, 255]`
- Embedded preprocessing: a Keras `Rescaling` layer maps `[0, 255]` to `[-1, 1]`
- Output: one `float32` tensor shaped `[1, 38]`
- Output mapping: index order in `labels-38.txt`; never sort or rename this file
- Selection: top class is `argmax(output[0])`

`model/inspect_model.py` and `model/convert_model.py` reject incompatible shapes,
dtypes, label counts, and missing embedded rescaling.

## Conversion and validation

```bash
python model/inspect_model.py
python model/convert_model.py
python model/inspect_model.py \
  --tflite-model android-app-kotlin/app/src/main/assets/model.tflite
python model/validate_tflite.py path/to/leaf.jpg \
  --model android-app-kotlin/app/src/main/assets/model.tflite
python model/parity_test.py path/to/leaf1.jpg path/to/leaf2.jpg \
  --tflite-model android-app-kotlin/app/src/main/assets/model.tflite
```

Conversion writes the same TFLite bytes and canonical labels to both Android tracks.

## Product limitations

The published 98.75% score is a source-author report for an augmented PlantVillage
split, not measured LeafGuard performance. Controlled dataset images do not represent
all phone-camera lighting, backgrounds, blur, occlusion, crops, or diseases. LeafGuard
must present low-confidence results as uncertain and must not claim a confirmed
diagnosis. Existing detailed guidance covers only 10 classes and requires human
agricultural review before use.
