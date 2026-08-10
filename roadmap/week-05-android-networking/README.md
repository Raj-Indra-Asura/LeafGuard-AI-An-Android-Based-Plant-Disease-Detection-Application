# Week 05: Connect Android to FastAPI

## Week 05 Mindset

Week 03 gave Android a real image URI. Week 04 gave FastAPI a tested multipart contract. Week 05 connects those two verified pieces:

> Select or capture an image in Android -> upload it as multipart field `image` -> parse the Week 04 JSON -> display the returned result.

This week does **not** prove real model accuracy. The backend may still run in explicit mock mode. Week 05 proves mobile networking, contract compatibility, loading state, result display, and safe failure behavior.

---

## Progressive Handoff

| Week | Verified input | New work | Verified output |
|---:|---|---|---|
| 01 | Product idea | User journey and weekly slices | Buildable plan |
| 02 | Week 01 plan | Android Activities, layouts, navigation | Runnable screen shell |
| 03 | Scan placeholder | Camera, gallery, URI, preview | Real selected image URI |
| 04 | Image concept | Standalone FastAPI contract | Tested `POST /predict` API |
| **05** | **Week 03 URI + Week 04 contract** | **Retrofit multipart connection** | **Android-to-backend result flow** |
| 06 | Working client-server pipeline | Real cloud model validation | Real cloud inference |

```text
Week 03 Android                    Week 04 FastAPI
selectedImageUri                   POST /predict
        |                          field: image
        |                                |
        `---- Week 05 Retrofit ----------'
                       |
                 ResultActivity
```

If Week 03 image input or Week 04 backend tests do not pass, stop and repair that earlier boundary first.

---

## Product State After Week 05

**Cumulative product contribution: 45%**

The product can now:

- preserve Week 03 camera and gallery input
- convert a selected content URI into temporary upload bytes
- send `POST /predict` using multipart field `image`
- parse all eight Week 04 response fields with Gson
- show loading state while the request is active
- distinguish an HTTP response error from a network failure
- open a real Result screen after a successful response
- recover for another attempt without crashing

The product still cannot:

- claim real AI accuracy while the backend reports mock mode
- save scan history; that belongs to Week 07
- provide the later local disease library
- run offline TensorFlow Lite inference
- use production HTTP security; local cleartext is restricted to the emulator host only

---

## Exact Week 05 Repository Delta

The Kotlin track is primary. Week 05 adds **4 files**, expands **7 files**, preserves the other Week 03 Android files, and does not rewrite the Week 04 backend contract.

| Change | Count | Files |
|---|---:|---|
| New | 4 | `network/ApiService.kt`, `network/PredictionResponse.kt`, `network/RetrofitClient.kt`, `res/xml/network_security_config.xml` |
| Expanded | 7 | `app/build.gradle`, `AndroidManifest.xml`, `ScanActivity.kt`, `ResultActivity.kt`, `activity_scan.xml`, `activity_result.xml`, `strings.xml` |
| Backend changed | 0 | Week 04 API stays the server source of truth |
| Later-week packages added | 0 | No `database/`, `ml/`, `ui/`, notification, or offline assets |

Exact cumulative sizes for the 11 Week 05 target files:

| File | Logical lines |
|---|---:|
| `app/build.gradle` | 47 |
| `AndroidManifest.xml` | 55 |
| `ScanActivity.kt` | 247 |
| `ResultActivity.kt` | 56 |
| `network/ApiService.kt` | 13 |
| `network/PredictionResponse.kt` | 22 |
| `network/RetrofitClient.kt` | 33 |
| `activity_scan.xml` | 76 |
| `activity_result.xml` | 115 |
| `strings.xml` | 55 |
| `network_security_config.xml` | 7 |
| **Total across changed/new files** | **726** |

These are cumulative end-of-week files, not added-line counts. For example, the 247-line `ScanActivity.kt` contains the complete Week 03 camera/gallery behavior plus Week 05 upload behavior.

The full content of every changed or new file appears in [learning-notes.md section 12](learning-notes.md#12-end-of-week-05-file-inventory-exact-files-exact-code-exact-size).

---

## Exact API Contract Android Must Preserve

| Contract part | Required value |
|---|---|
| Method | `POST` |
| Path | `/predict` |
| Encoding | `multipart/form-data` |
| File field | `image` |
| Success | HTTP 200 |
| Response fields | 8 |

The response model contains:

```text
model_label, disease, confidence, uncertain,
guidance_available, symptoms, treatment, prevention
```

Android does not rename, omit, or invent fields. Gson maps this existing Week 04 JSON into `PredictionResponse`.

---

## CSE 2206 Connection

Week 05 applies these mobile-development concepts:

- client-server architecture
- third-party Gradle dependencies
- HTTP POST and multipart file upload
- JSON deserialization
- asynchronous callbacks
- Activity-to-Activity data passing with Intent extras
- runtime error feedback and loading state
- Android network permission and local development security

The central CSE 2206 question is:

> How can an Android client perform slow network work without freezing the UI or crashing when the server is unavailable?

---

## Milestone Demo

1. Start the Week 04 backend with `USE_MOCK=true`.
2. Launch the Kotlin app on an emulator.
3. Open Scan and select or capture an image.
4. Tap **Detect Disease**.
5. Show the progress indicator while uploading.
6. Show the Result screen with all contract data represented.
7. Stop the backend.
8. Retry and show a friendly network error with no crash.
9. Explain that mock mode proves the pipeline, not disease accuracy.

---

## Seven-File Learning Loop

| Step | File | Purpose | Required output |
|---:|---|---|---|
| 1 | `README.md` | Fix the Week 05 scope | Boundary statement |
| 2 | `learning-notes.md` | Learn concepts and exact code | Understanding checklist |
| 3 | `exercises.md` | Practise before building | Six exercise files |
| 4 | `build-task.md` | Reconstruct and run the slice | Compiling app and demo |
| 5 | `validation-checklist.md` | Prove success and failure paths | Pass/fail record |
| 6 | `quiz.md` | Recall the contract independently | At least 14/18 |
| 7 | `reflection.md` | Explain evidence and Week 06 handoff | Reflection answers |

Progress in this order:

```text
understand -> practise -> build -> validate -> recall -> reflect
```

---

## Exact Completion Contract

Week 05 is complete only when:

| Quantity | Required value |
|---|---:|
| New files | 4 |
| Expanded files | 7 |
| Retrofit endpoint methods | 1 |
| Multipart field names | 1: `image` |
| Parsed response fields | 8 |
| Successful end-to-end paths demonstrated | 1 |
| Backend-unavailable paths demonstrated | 1 |
| Android debug build | Successful |
| Week 04 backend tests | 8 passing |
| Real-model accuracy claimed | 0 |

Do not move to Week 06 until the milestone demo and validation checklist pass.

<!-- NAV_FOOTER_START -->

---

## Week 05 Navigation

| Step | File | Description |
|---:|---|---|
| **1** | **README.md** - current | Week overview and exact boundary |
| 2 | [learning-notes.md](learning-notes.md) | Theory and complete source inventory |
| 3 | [exercises.md](exercises.md) | Guided practice |
| 4 | [build-task.md](build-task.md) | Implementation guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation and evidence |
| 6 | [quiz.md](quiz.md) | Knowledge assessment |
| 7 | [reflection.md](reflection.md) | Reflection and handoff |

[Previous: Week 04](../week-04-fastapi-backend/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 06](../week-06-cloud-ml-model/README.md)