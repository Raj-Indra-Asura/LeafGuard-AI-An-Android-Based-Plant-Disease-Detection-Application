# Week 06 Quiz: Real Cloud Model Contracts

## Instructions

Answer after the build task and validation checklist. Do not read the key first.

Passing score: **14 out of 18**.

---

## Multiple Choice

### 1. What changes in Week 06?

A) Android multipart contract
B) Mock execution becomes validated Keras execution
C) Room history is added
D) TFLite runs on Android

Answer: ____

### 2. Which property proves byte-for-byte model identity most strongly?

A) Filename
B) Folder name
C) SHA-256
D) Keras extension

Answer: ____

### 3. What input shape does the approved model expect?

A) `(1, 224, 224, 3)`
B) `(224, 224)`
C) `(1, 256, 256, 3)`
D) `(38, 224, 224)`

Answer: ____

### 4. What pixel range should the backend caller supply?

A) `[0,1]`
B) `[-1,1]`
C) Raw `[0,255]` as `float32`
D) Integer labels

Answer: ____

### 5. Why must labels not be sorted?

A) Sorting is slow
B) Output index meaning depends on original order
C) Python cannot sort underscores
D) FastAPI requires random order

Answer: ____

### 6. Which health combination proves real mode is ready?

A) `use_mock=true`, `model_loaded=false`
B) `use_mock=false`, `model_loaded=true`
C) `status=ok` only
D) `class_count=10`

Answer: ____

### 7. What should happen when real mode has no loaded model?

A) Return fake success
B) Return HTTP 503
C) Retrain automatically
D) Change Android fields

Answer: ____

### 8. What does one real prediction prove?

A) Independent 98.75% accuracy
B) Real artifact execution through the existing API
C) Every plant disease is supported
D) Offline parity

Answer: ____

---

## True or False

### 9. Week 06 changes the Week 05 `PredictionResponse` fields.

Answer: ____

### 10. The approved model contains embedded scaling from `[0,255]` to `[-1,1]`.

Answer: ____

### 11. Dividing pixels by 255 before this model is safe generic preprocessing.

Answer: ____

### 12. The Keras binary should be committed so every learner has it.

Answer: ____

### 13. High confidence guarantees a correct diagnosis.

Answer: ____

---

## Short Answer

### 14. Explain the full artifact identity record required before loading the model.

Answer:

### 15. Explain every dimension in `(1, 224, 224, 3)` and why output `(1, 38)` must match labels.

Answer:

### 16. Why would caller-side `/255.0` silently damage this model's input contract?

Answer:

### 17. Name the evidence needed to prove real mode, beyond setting `USE_MOCK=false`.

Answer:

### 18. Name three claims or implementation areas that remain outside Week 06.

Answer:

---

## Answer Key

1. B
2. C
3. A
4. C
5. B
6. B
7. B
8. B
9. False
10. True
11. False
12. False
13. False

Short-answer requirements:

| Question | Full-credit ideas |
|---:|---|
| 14 | Source repository, pinned commit/path, license review, local path, exact size, SHA-256, tracked status |
| 15 | Batch, height, width, RGB channels; 38 scores require the same 38 labels in order |
| 16 | Embedded transform expects raw values; predivision causes double normalization and wrong range |
| 17 | TensorFlow import, valid inspector/tests, `/health` with false/true/38, real HTTP 200 response |
| 18 | Any three of independent accuracy claim, TFLite conversion, offline Android inference, history, training, production deployment |

---

## Remediation Map

| Missed questions | Review |
|---|---|
| 1, 9, 18 | Progressive boundary |
| 2, 12, 14 | Artifact identity and provenance |
| 3, 5, 15 | Tensor/output/label contract |
| 4, 10, 11, 16 | Embedded preprocessing |
| 6, 7, 17 | Real-mode health and failure behavior |
| 8, 13 | Confidence, execution, and accuracy limitations |

If your score is below 14, review mapped sections, rewrite missed answers in your own words, and retake before Week 07.

<!-- NAV_FOOTER_START -->

---

## Week 06 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | **Quiz - current** | [Reflection](reflection.md)

[Previous: Validation](validation-checklist.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Reflection](reflection.md)