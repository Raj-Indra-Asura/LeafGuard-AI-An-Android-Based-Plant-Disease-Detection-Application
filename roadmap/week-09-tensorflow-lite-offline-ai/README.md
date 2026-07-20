# Week 09: TensorFlow Lite Offline AI

## What you'll learn & why

This week you make the app diagnose a leaf **without any internet**, by running a small machine-learning model directly on the phone. That is called **on-device (offline) AI**: instead of sending the photo to a server, the phone does the thinking itself, which is faster and works anywhere. You do this with **TensorFlow Lite (TFLite)**, a lightweight version of Google's TensorFlow made to run on phones. The real class is `TFLiteClassifier` — it resizes the photo to 224×224, turns pixels into red/green/blue numbers from 0 to 1, runs the model, and picks the highest-scoring label (this "pick the biggest" step is called **argmax**). The model file shipped in the repo (`assets/model.tflite`) is only a **text placeholder**, so `TFLiteClassifier` detects it and falls back to a simple green-colour heuristic; part of this week is replacing that placeholder with a real trained model.

## New words this week

- **On-device / offline AI** — the phone runs the prediction itself with no server and no internet, using a model file stored in the app. (See [Glossary](../../GLOSSARY.md).)
- **TensorFlow Lite (TFLite)** — a small, fast version of TensorFlow built to run machine-learning models on phones; the model is a `.tflite` file. (See [Glossary](../../GLOSSARY.md).)
- **Interpreter** — the TFLite object that loads the `.tflite` file and actually runs the model on your input. (See [Glossary](../../GLOSSARY.md).)
- **argmax** — "the position of the biggest number"; after the model outputs one score per class, argmax picks the index of the highest score, which maps to a line in `labels.txt`.

> **The real classifier (match this exactly):** `TFLiteClassifier` (in `android-app-kotlin/app/src/main/java/com/leafguard/ml/`) loads `assets/model.tflite` + `assets/labels.txt`, resizes to **224×224**, converts to **RGB floats 0..1** (divide each channel by 255), runs the model, and takes **argmax** over the 10 outputs. Because the committed `model.tflite` is a **text placeholder**, the classifier catches the load error and uses a **green-channel heuristic fallback** so the app still runs. Replace the placeholder with a converted trained model (see the section below and [`model/model-acquisition-guide.md`](../../model/model-acquisition-guide.md)).

## Where to practice this week

- Machine-learning practice (primary): [`../../exercises/ml/`](../../exercises/ml/)
- Worked answers: [`../../solutions/week-09/`](../../solutions/week-09/)
- Notebook walkthrough: [`../../notebooks/week-09/`](../../notebooks/week-09/)

## Repository State After Week 09

Week 09 keeps the cloud path, history, and disease library, then adds on-device inference. The repository now has both prediction modes: backend cloud mode and Android offline mode.

### Structure to browse after this week

- `android-app-kotlin/app/src/main/assets/model.tflite` is the on-device model asset. In this repository it may be a placeholder until replaced with a real trained model.
- `android-app-kotlin/app/src/main/assets/labels.txt` defines the output label order for the Android classifier.
- `android-app-kotlin/app/src/main/java/com/leafguard/ml/TFLiteClassifier.kt` loads the model, preprocesses the bitmap, runs inference, maps labels, and provides fallback behavior.
- `ScanActivity.kt` chooses between cloud upload and offline prediction, then sends the result to the same result/history flow.
- `ResultActivity.kt`, `HistoryActivity.kt`, and the Room files should work for both cloud and offline predictions.
- `model/model-acquisition-guide.md`, `model/convert_model.py`, `model/validate_tflite.py`, and `model/model_contract.py` support model preparation and validation.
- The Java track should include the same assets and equivalent classifier behavior if both tracks are maintained.

### Files you should create or update this week

- `app/src/main/assets/model.tflite`.
- `app/src/main/assets/labels.txt`.
- `ml/TFLiteClassifier.kt`.
- `ScanActivity.kt` for offline mode selection and inference routing.
- `app/build.gradle` for TensorFlow Lite dependency if not already present.
- `model/model-notes.md` with the final on-device model contract.
- `docs/evidence/week-09/` showing airplane-mode prediction and cloud/offline comparison.

### What this repository state can do

- Run a complete prediction flow without internet when a valid TFLite model is present.
- Keep the same result, history, and disease-guidance screens for both cloud and offline predictions.
- Compare backend latency with on-device latency.
- Continue to run with a documented fallback if the checked-in model asset is only a placeholder.

### What this repository state cannot do

- It cannot guarantee medically or agriculturally reliable results without an approved trained model and validation images.
- It cannot yet notify the user or share a result through other apps.
- It cannot attach GPS location to scans unless Week 10 is implemented.
- It is not yet fully tested, hardened, or packaged for submission.

---

## Weekly Objective

Implement on-device AI inference using TensorFlow Lite for offline prediction capability.

**Measurable Outcomes:**
- .tflite model file in assets/
- labels.txt file with class names
- TFLite interpreter initialized
- Offline prediction working
- Cloud vs on-device mode selector
- Latency comparison feature
- Complete offline operation

---

## Why This Week Matters

**Offline AI** enables app to work without internet connection. Demonstrates advanced Android development.

**CSE 2206:** Shows understanding of both cloud and on-device AI architectures.

---

## Syllabus Topics

1. **On-device ML** - TensorFlow Lite integration
2. **File I/O** - Loading model from assets
3. **Performance** - Latency measurement
4. **Mode Selection** - User choice between cloud/offline

---

## Prerequisites

- Week 06 complete (cloud ML working)
- Understanding of tensor inputs/outputs
- .tflite model file ready

---

## Key Concepts

### TFLite Architecture

```
Image → Preprocessing → TFLite Interpreter → Output Tensor → Post-processing → Result
```

### Implementation Steps

1. Convert model to .tflite format
2. Add model and labels to assets/
3. Initialize TFLite Interpreter
4. Preprocess image to match input shape
5. Run inference
6. Extract output and map to labels
7. Display results

---

## Replacing the placeholder `model.tflite`

The repo ships `app/src/main/assets/model.tflite` as a **text placeholder** so the project always builds; `TFLiteClassifier` notices it is not a real model and uses the green-channel heuristic fallback. To get real predictions, swap in a trained model:

1. **Get or make a real model.** Follow [`model/model-acquisition-guide.md`](../../model/model-acquisition-guide.md). To produce a tiny, correctly-shaped `.tflite` for wiring/testing (not for accuracy), run the helper script:
   - macOS/Linux: `python model/generate_stub_model.py`
   - Windows: `python model\generate_stub_model.py`
   This writes a real (but untrained) `model.tflite` with the right 224×224×3 input and 10 outputs.
2. **Confirm the labels match.** The model's output order must line up with `assets/labels.txt` (10 lines, exact order). Do not reorder labels.
3. **Copy the file into assets**, replacing the placeholder:
   - `android-app-kotlin/app/src/main/assets/model.tflite`
   - (and the Java twin `android-app/app/src/main/assets/model.tflite`)
4. **Rebuild and run.** On launch `TFLiteClassifier` maps the file into memory and, if it is valid, uses the real `Interpreter` instead of the heuristic.

**Friendly failure notes:**
- *App still gives odd/greenish results after swapping?* The file is probably still the placeholder or a non-model file — check Logcat for `Unable to load a valid TFLite model asset` (that means the fallback is active).
- *Crash on load with an input-shape error?* Your model is not 224×224×3 input / 10 outputs — re-export it to match `TFLiteClassifier`'s expectations.

**Expected on-screen result:** with a valid trained model, capturing a leaf shows a disease name from `labels.txt` plus a confidence percentage; with the placeholder, you still get a result (from the heuristic) but it is not trustworthy.

---

## Weekly Timeline

- **Day 1-2:** Model conversion and setup (4h)
- **Day 3-4:** TFLite interpreter implementation (5h)
- **Day 5:** Integration with app (3h)
- **Day 6:** Mode selector UI (2h)
- **Day 7:** Testing and comparison (2h)

---

## Validation Criteria

- [ ] .tflite model in assets/
- [ ] labels.txt in assets/
- [ ] TFLite interpreter initializes
- [ ] Offline prediction works
- [ ] Results match cloud predictions
- [ ] Mode selector implemented
- [ ] Latency measured and displayed
- [ ] Works without internet

---

**Next:** Open `learning-notes.md` for TFLite concepts.


<!-- NAV_FOOTER_START -->

---

## 📈 Product State After This Week

**Cumulative product completion: 82%** *(official model: [PRODUCT_PROGRESS_MAP.md](../../PRODUCT_PROGRESS_MAP.md))*

- **Your app can now…** diagnose a leaf **with no internet at all** using on-device TensorFlow Lite — the complete core product now works both online and offline.
- **Your app still cannot…** notify the user, share results, or tag scans with location; it is also not yet hardened or packaged. Weeks 10–12 finish the job.
- **Applies equally to both tracks:** Kotlin (`android-app-kotlin/`, primary) and Java (`android-app/`, secondary).

### Cumulative Repository State After Week 09

This snapshot includes all Week 01-08 files and adds the on-device TensorFlow Lite path. Keep the model asset, labels, backend labels, and XML disease names aligned.

```text
LeafGuard-AI/
|-- README.md
|-- START_HERE.md
|-- LEARNING_PATH.md
|-- PRODUCT_PROGRESS_MAP.md
|-- progress-tracker.md
|-- roadmap/week-01-project-understanding/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|-- roadmap/week-02-android-basics-ui/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|-- roadmap/week-03-camera-gallery/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|-- roadmap/week-04-fastapi-backend/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|-- roadmap/week-05-android-networking/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|-- roadmap/week-06-cloud-ml-model/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|-- roadmap/week-07-room-sqlite-history/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|-- roadmap/week-08-xml-disease-library/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|-- roadmap/week-09-tensorflow-lite-offline-ai/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|-- docs/evidence/{week-01/, week-02/, week-03/, week-04/, week-05/, week-06/, week-07/, week-08/, week-09/}
|-- backend-api/{main.py, model_loader.py, config.py, labels.py, labels-38.txt, requirements*.txt, test_api.py, README.md, models/}
|-- model/{README.md, model-notes.md, labels-38.txt, model_contract.py, test_model_contract.py, inspect_model.py, convert_model.py, validate_tflite.py, parity_test.py, model-acquisition-guide.md}
|-- android-app-kotlin/app/src/main/
|   |-- assets/{diseases.xml, labels.txt, model.tflite}
|   |-- java/com/leafguard/ml/TFLiteClassifier.kt
|   |-- java/com/leafguard/{ScanActivity.kt, ResultActivity.kt, HistoryActivity.kt, HistoryDetailActivity.kt, DiseaseLibraryActivity.kt, MainActivity.kt}
|   |-- java/com/leafguard/database/{ScanRecord.kt, ScanDao.kt, AppDatabase.kt}
|   |-- java/com/leafguard/network/{ApiService.kt, RetrofitClient.kt, PredictionResponse.kt}
|   `-- res/layout/{activity_scan.xml, activity_result.xml, activity_history.xml, activity_history_detail.xml, activity_disease_library.xml, item_disease_library.xml, item_scan_history.xml}
`-- android-app/ (Java mirror with TFLiteClassifier.java and matching assets)
```

---

## 📚 Week 09 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| **1** | **README.md** ← *You are here* | **Week Overview & Objectives** |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

*(Start of week)* &nbsp;&nbsp;|&nbsp;&nbsp; **Week Overview & Objectives** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Theory & Learning Notes →](learning-notes.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 08: XML Disease Library](../week-08-xml-disease-library/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 10: Notifications, Share & Location ➡](../week-10-notifications-share-location/README.md) |

---
