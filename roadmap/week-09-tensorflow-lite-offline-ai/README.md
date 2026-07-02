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
