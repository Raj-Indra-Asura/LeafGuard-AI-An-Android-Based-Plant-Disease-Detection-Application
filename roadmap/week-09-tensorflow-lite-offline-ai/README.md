# Week 09: TensorFlow Lite Offline Inference

## Week 09 Mindset

Week 08 provides local reviewed guidance but still relies on cloud inference for predictions. Week 09 converts the approved Week 06 Keras model and adds an offline Android branch:

> Convert Keras -> validate TFLite tensors and parity -> bundle model and labels -> run on-device inference -> return the same eight-field result contract.

Offline inference changes where prediction runs. It does not change ResultActivity, Room history, XML guidance, or the meaning of confidence.

---

## Progressive Handoff

| Week | Verified input | New work | Verified output |
|---:|---|---|---|
| 06 | Approved Keras model | Real cloud validation | Eight-field cloud result |
| 07 | Result values | Room | Persistent history |
| 08 | Display disease | XML reference | Local reviewed guidance |
| **09** | **Keras contract + local guidance** | **TFLite conversion and Android interpreter** | **Cloud/offline selectable inference** |
| 10 | Complete core workflows | Sharing/location/notifications/polish | Product utility features |

```text
selected image
  -> Cloud: Retrofit -> FastAPI/Keras
  -> Offline: Bitmap -> TFLite Interpreter
both -> PredictionResponse (8 fields)
     -> Result XML enrichment
     -> Room save
```

---

## Product State After Week 09

**Cumulative product contribution: 80%**

The product can now:

- convert the approved float32 Keras model to TFLite
- validate one input `[1,224,224,3]` and one output `[1,38]`
- preserve raw RGB `[0,255]` preprocessing and embedded scaling
- synchronize exactly 38 canonical Android labels
- memory-map the TFLite asset and use four interpreter threads
- select Cloud or Offline mode on Scan
- run offline inference without FastAPI or Internet
- return the same eight-field `PredictionResponse` from both modes
- reuse Week 08 XML guidance and Week 07 Room persistence unchanged
- close the interpreter after each classification operation

The product still cannot:

- claim parity means accuracy
- guarantee the three sample predictions are correct
- prove physical-device latency, memory, thermal, or airplane-mode behavior without manual tests
- use quantization/GPU delegates; the teaching model remains float32 CPU
- add notifications, location, sharing, analytics, settings-driven thresholds, or UI redesign
- diagnose unsupported classes or replace agricultural verification

---

## Exact Week 09 Repository Delta

| Change | Count | Files |
|---|---:|---|
| New text files | 8 | conversion, validation, parity, focused tests, classifier, Android labels, asset README, TFLite provenance |
| Expanded text files | 6 | model contract/notes, Gradle, Scan Activity/layout, strings |
| Local binary artifact | 1 | `android-app-kotlin/app/src/main/assets/model.tflite` |
| Result/Room/XML/API changes | 0 | Previous contracts are reused |

Exact text sizes:

| File | Logical lines |
|---|---:|
| `model/model_contract.py` | 117 |
| `model/convert_model.py` | 47 |
| `model/validate_tflite.py` | 31 |
| `model/parity_test.py` | 58 |
| `model/test_tflite_contract.py` | 73 |
| `model/model-notes.md` | 55 |
| `release-records/tflite-provenance.txt` | 33 |
| `app/build.gradle` | 60 |
| `ml/TFLiteClassifier.kt` | 149 |
| `ScanActivity.kt` | 321 |
| `activity_scan.xml` | 111 |
| `strings.xml` | 82 |
| `assets/labels.txt` | 38 |
| `assets/README.md` | 7 |
| **Total text** | **1,182** |

Full text appears in [learning-notes.md section 12](learning-notes.md#12-end-of-week-09-file-inventory-exact-files-exact-code-exact-size).

Binary identity:

| Property | Value |
|---|---|
| Path | `android-app-kotlin/app/src/main/assets/model.tflite` |
| Size | 9,056,916 bytes |
| SHA-256 | `22ea2d4a47a52b2d9b150e0f74b113def0f12bbdb59209f7e0bce2a9701d41f9` |
| Git status | Local/ignored binary; not duplicated in learning notes |

---

## Exact Offline Tensor Contract

| Contract part | Required value |
|---|---|
| Input count | 1 |
| Input shape | `[1,224,224,3]` |
| Input dtype | `float32` |
| Color/range | RGB raw `[0,255]` |
| Embedded scaling | `[0,255] -> [-1,1]` |
| Output count | 1 |
| Output shape | `[1,38]` |
| Output dtype | `float32` |
| Labels | Exact canonical 38-line order |
| Selection | `argmax(output[0])` |
| Uncertain threshold | Confidence below `0.50` |

Android must not divide pixels by 255 because the converted model preserves embedded preprocessing.

---

## Parity Is Not Accuracy

The reproduced three-image check proved:

- identical Keras/TFLite top-1 indexes for all three images
- maximum confidence delta below `0.000015`

It also observed that predicted labels did not match the sample folder names. Therefore:

> Conversion parity passed, but those samples do not establish prediction correctness.

This distinction is required Week 09 evidence.

---

## CSE 2206 Connection

Week 09 applies:

- model format conversion
- binary asset identity and provenance
- tensor shape/dtype validation
- ByteBuffer memory layout and native byte order
- bitmap scaling and RGB extraction
- interpreter lifecycle/resource cleanup
- asynchronous CPU work with coroutines
- strategy selection between cloud and offline implementations
- parity and regression testing

---

## Milestone Demo

1. Verify TFLite size/hash and 38 labels.
2. Run four focused contract tests.
3. Show three-image Keras/TFLite parity.
4. Start app with backend stopped or network unavailable.
5. Select Offline mode and classify an image.
6. Show Result, XML guidance behavior, and Room save.
7. Switch Cloud mode, restart backend, and show the same result contract.
8. Remove/rename a model or label asset temporarily and show safe offline failure.
9. Explain why parity passed but accuracy was not proven.

---

## Seven-File Learning Loop

| Step | File | Purpose | Output |
|---:|---|---|---|
| 1 | `README.md` | Fix artifact and boundaries | Scope statement |
| 2 | `learning-notes.md` | Learn conversion/interpreter and exact files | Understanding checklist |
| 3 | `exercises.md` | Practise tensors, buffers, parity, modes | Six exercise files |
| 4 | `build-task.md` | Convert, validate, integrate, demonstrate | Offline milestone |
| 5 | `validation-checklist.md` | Prove contracts and failures | Pass/fail evidence |
| 6 | `quiz.md` | Recall exact behavior | At least 14/18 |
| 7 | `reflection.md` | Explain evidence and Week 10 handoff | Reflection answers |

---

## Exact Completion Contract

| Quantity | Required value |
|---|---:|
| New text files | 8 |
| Expanded text files | 6 |
| Text logical lines | 1,182 |
| TFLite binaries | 1 verified local artifact |
| Labels | 38 unique canonical lines |
| Focused tests | 4 passing |
| Parity images | 3, identical top-1 indexes |
| Android build | Successful |
| Offline backend dependency | 0 |
| Result/Room/XML schema changes | 0 |
| Unsupported accuracy claims | 0 |

Do not move to Week 10 until the milestone and validation checklist pass.

<!-- NAV_FOOTER_START -->

---

## Week 09 Navigation

| Step | File | Description |
|---:|---|---|
| **1** | **README.md** - current | Scope, artifact, and parity boundary |
| 2 | [learning-notes.md](learning-notes.md) | Theory and complete source inventory |
| 3 | [exercises.md](exercises.md) | Guided practice |
| 4 | [build-task.md](build-task.md) | Implementation guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation and evidence |
| 6 | [quiz.md](quiz.md) | Knowledge assessment |
| 7 | [reflection.md](reflection.md) | Reflection and handoff |

[Previous: Week 08](../week-08-xml-disease-library/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 10](../week-10-notifications-share-location/README.md)