# Week 06: Validate Real Cloud Model Inference

## Week 06 Mindset

Week 05 proved that Android can send a real image to FastAPI and display the unchanged eight-field response. Week 06 replaces the backend's explicit mock execution with one approved, validated Keras model:

> Acquire the pinned model -> verify identity and license -> validate its tensor contract -> run FastAPI with `USE_MOCK=false` -> prove a real-model response without changing Android.

Week 06 is **model integration**, not model training and not offline Android inference.

---

## Progressive Handoff

| Week | Verified input | New work | Verified output |
|---:|---|---|---|
| 01 | Product idea | Product plan | Buildable roadmap |
| 02 | Screen map | Android shell | Runnable navigation |
| 03 | Scan placeholder | Camera/gallery URI | Real image input |
| 04 | Image concept | FastAPI contract and mock | Tested standalone API |
| 05 | URI + API | Retrofit multipart connection | Android-to-backend pipeline |
| **06** | **Working Week 05 pipeline** | **Approved Keras artifact and contract validation** | **Real cloud inference** |
| 07 | Real result flow | Local persistence | Scan history |

```text
Week 05 request and response stay unchanged
                    |
                    v
POST /predict -> Keras model -> same eight-field JSON
                    ^
                    |
      Week 06 validates artifact + contract
```

If Week 05 cannot display a mock response and recover when the backend stops, finish Week 05 first.

---

## Product State After Week 06

**Cumulative product contribution: 55%**

The product can now:

- identify the exact approved model artifact by source commit, size, and SHA-256
- validate 38 canonical labels and their immutable order
- validate Keras input `(None, 224, 224, 3)` and output `(None, 38)`
- validate `float32` input and embedded `[0,255] -> [-1,1]` preprocessing
- start FastAPI with `USE_MOCK=false`
- report `model_loaded=true`, `use_mock=false`, and `class_count=38`
- execute a real Keras prediction through the Week 05 Android-compatible API
- preserve uncertainty and guidance-availability behavior

The product still cannot:

- claim independent 98.75% real-world accuracy
- support crops or diseases outside the 38 canonical classes
- guarantee correct results for arbitrary phone-camera images
- run the model offline on Android; TFLite conversion belongs to a later week
- save history; Week 07 owns persistence
- treat confidence as confirmed diagnosis

---

## Exact Week 06 Repository Delta

The Week 04 backend already contains the real/mock predictor boundary, TensorFlow dependency manifest, preprocessing, and stable response model. Week 06 activates that prepared path by adding a verified artifact and a focused cloud-model validation package.

| Change | Count | Files |
|---|---:|---|
| New or rewritten text files | 6 | `model-notes.md`, `labels-38.txt`, `model_contract.py`, `inspect_model.py`, `test_model_contract.py`, `model-provenance.txt` |
| Provided binary artifact | 1 | `backend-api/models/leafguard_model.keras` (tracked and identity-verified) |
| Backend source changed | 0 | Week 04 source already supports real mode |
| Android files changed | 0 | Week 05 request/response contract remains valid |
| TFLite/offline files changed | 0 | Deferred to offline-inference week |

Exact text-file sizes:

| File | Logical lines |
|---|---:|
| `model/model-notes.md` | 54 |
| `model/labels-38.txt` | 38 |
| `model/model_contract.py` | 85 |
| `model/inspect_model.py` | 39 |
| `model/test_model_contract.py` | 56 |
| `release-records/model-provenance.txt` | 39 |
| **Total** | **311** |

The binary is not pasted into Markdown. Its exact identity is:

| Property | Required value |
|---|---|
| Path | `backend-api/models/leafguard_model.keras` |
| Size | 25,143,175 bytes |
| SHA-256 | `08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f` |
| Source commit | `f6165bd93524dfb77a9629aae70db845832d1b01` |

The complete six-file text snapshot appears in [learning-notes.md section 12](learning-notes.md#12-end-of-week-06-file-inventory-exact-files-exact-code-exact-size).

---

## Exact Cloud Model Contract

| Contract part | Required value |
|---|---|
| Framework | TensorFlow/Keras 2.19.1 |
| Architecture | Fine-tuned MobileNetV2 classifier |
| Input | One `float32` tensor, `(1, 224, 224, 3)` |
| Color | RGB |
| Caller range | Raw `[0,255]` float values |
| Embedded scaling | `[0,255] -> [-1,1]` |
| Output | One `float32` tensor, `(1, 38)` |
| Label mapping | Exact order in `labels-38.txt` |
| Selection | `argmax(output[0])` |

Do not divide pixels by 255 in the backend. The approved model already rescales internally.

---

## API Compatibility Contract

Week 06 changes inference implementation, not the mobile API:

| API part | Preserved value |
|---|---|
| Method/path | `POST /predict` |
| Multipart field | `image` |
| Success fields | `model_label`, `disease`, `confidence`, `uncertain`, `guidance_available`, `symptoms`, `treatment`, `prevention` |
| Confidence scale | 0.0 to 1.0 |

No Week 05 Retrofit or `PredictionResponse` field needs to change.

---

## CSE 2206 Connection

Week 06 applies:

- binary artifact management
- file paths and environment configuration
- input/output contracts
- defensive validation and exceptions
- array indexing and `argmax`
- dependency management
- integration and regression testing
- trustworthy communication of technical limitations

The central engineering question is:

> How do we prove that a third-party model is the expected artifact and is compatible with an existing mobile API before trusting its predictions?

---

## Milestone Demo

1. Verify artifact size and SHA-256.
2. Run `model/inspect_model.py`.
3. Show 38 labels, valid Keras shapes, and embedded rescaling.
4. Run four focused cloud-model contract tests.
5. Start FastAPI with `USE_MOCK=false`.
6. Show `/health`: `use_mock=false`, `model_loaded=true`, `class_count=38`.
7. Send one real image through Android or `/docs`.
8. Show the unchanged eight-field response.
9. Explain that one prediction and a source-author score are not independent accuracy validation.
10. Temporarily remove or misconfigure the model and show real mode fails clearly rather than silently claiming success.

---

## Seven-File Learning Loop

| Step | File | Purpose | Output |
|---:|---|---|---|
| 1 | `README.md` | Fix scope and artifact identity | Boundary statement |
| 2 | `learning-notes.md` | Learn model contract and exact files | Understanding checklist |
| 3 | `exercises.md` | Practise identity, shapes, labels, inference | Six exercise files |
| 4 | `build-task.md` | Acquire and validate real mode | Real cloud demo |
| 5 | `validation-checklist.md` | Prove contract and failure behavior | Pass/fail record |
| 6 | `quiz.md` | Recall exact facts | At least 14/18 |
| 7 | `reflection.md` | Explain evidence and limitations | Reflection answers |

---

## Exact Completion Contract

| Quantity | Required value |
|---|---:|
| Complete Week 06 text files | 6 |
| Text-file logical lines | 311 |
| Approved Keras artifacts | 1 |
| Canonical labels | 38 |
| Focused cloud-model tests | 4 passing |
| Real-mode health checks | 3 key values correct |
| Real-mode predictions | At least 1 HTTP 200 |
| Android contract changes | 0 |
| TFLite/offline changes | 0 |
| Independent accuracy claims | 0 unsupported claims |

Do not move to Week 07 until the milestone demo and validation checklist pass.

<!-- NAV_FOOTER_START -->

---

## Week 06 Navigation

| Step | File | Description |
|---:|---|---|
| **1** | **README.md** - current | Week scope and exact boundary |
| 2 | [learning-notes.md](learning-notes.md) | Theory and complete source inventory |
| 3 | [exercises.md](exercises.md) | Guided practice |
| 4 | [build-task.md](build-task.md) | Implementation guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation and evidence |
| 6 | [quiz.md](quiz.md) | Knowledge assessment |
| 7 | [reflection.md](reflection.md) | Reflection and handoff |

[Previous: Week 05](../week-05-android-networking/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 07](../week-07-room-sqlite-history/README.md)
