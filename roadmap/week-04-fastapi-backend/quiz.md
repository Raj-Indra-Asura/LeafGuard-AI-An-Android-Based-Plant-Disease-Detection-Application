# Week 04 Quiz: FastAPI Requests, Uploads, and Responses

## Instructions

Answer after completing the learning notes and build task. This quiz checks Week 04 understanding only.

Passing score: 14 out of 18.

Complete the quiz without opening the answer key, source files, or learning notes. The quiz checks whether you can independently explain the Week 04 contract after building and validating it.

| Question group | Questions | Skill checked |
|---|---|---|
| Multiple choice | 1–8 | Recall exact Week 04 scope and API facts |
| True or false | 9–13 | Reject common boundary and validation mistakes |
| Short answer | 14–18 | Trace, justify, and apply the contract |

For short answers, use the exact project terms where they matter: `POST /predict`, multipart `image`, mock mode, JSON, and HTTP status codes. Do not award yourself credit for a vague answer that could describe any backend.

---

## Multiple Choice

### 1. What does Week 04 add?
A) Android Retrofit networking
B) A standalone FastAPI backend
C) Room history
D) Offline TFLite

Answer: ____

### 2. Which method should upload an image?
A) GET
B) POST
C) DELETE
D) PATCH

Answer: ____

### 3. What is the required multipart field name?
A) `file`
B) `photo`
C) `image`
D) `bitmap`

Answer: ____

### 4. What does JSON provide?
A) Structured response data
B) Android layouts
C) Model training
D) Camera permission

Answer: ____

### 5. What does HTTP 413 mean here?
A) Success
B) Missing route
C) Upload too large
D) Model loaded

Answer: ____

### 6. Why use mock mode?
A) To claim real accuracy
B) To test the API contract before real inference
C) To replace validation
D) To connect Room

Answer: ____

### 7. Which page provides interactive FastAPI documentation?
A) `/docs`
B) `/android`
C) `/room`
D) `/camera`

Answer: ____

### 8. Which work belongs to Week 05?
A) Health endpoint
B) Android uploads an image to FastAPI
C) Mock response shape
D) API tests

Answer: ____

---

## True or False

### 9. Week 04 should modify Android networking code.
Answer: ____

### 10. A filename ending in `.png` proves its bytes are a valid image.
Answer: ____

### 11. `/health` should reveal whether the service uses mock mode.
Answer: ____

### 12. Confidence should remain on a 0.0–1.0 scale in the API contract.
Answer: ____

### 13. Invalid requests should be tested as well as successful requests.
Answer: ____

---

## Short Answer

### 14. Explain the `/predict` request-response flow in 4–6 steps.
Answer:

### 15. Why must clients use the exact field name `image`?
Answer:

### 16. Explain the difference between mock prediction and real inference.
Answer:

### 17. Name three invalid-upload cases and their safe outcomes.
Answer:

### 18. Name three things Week 04 must not implement or claim yet.
Answer:

---

## Answer Key

1. B
2. B
3. C
4. A
5. C
6. B
7. A
8. B
9. False
10. False
11. True
12. True
13. True

Short answers should mention the `image` contract, validation, JSON, mock honesty, HTTP errors, Week 05 Android networking, and Week 06 real inference.

### Short-Answer Scoring Guide

Award one point only when the answer includes the required Week 04 ideas:

| Question | Full-credit requirements |
|---:|---|
| 14 | Upload field `image`; bounded read; MIME/decode validation; mock prediction; eight-field JSON; cleanup or safe error |
| 15 | FastAPI binds by parameter name; another field name breaks the request contract and normally produces 422 |
| 16 | Mock is deterministic contract practice; real inference uses a validated Keras model; mock output is not accuracy evidence |
| 17 | Three concrete cases with matching outcomes, such as text/spoofed/empty -> 400, oversized -> 413, missing -> 422, unavailable real mode -> 503 |
| 18 | Three deferred items correctly assigned, including Android networking in Week 05 and real cloud inference in Week 06 |

If an answer contains a correct idea plus a false claim, correct the claim before awarding the point.

## Readiness Rule

If you score below 14, reread `learning-notes.md`, revisit the build task, and retake the quiz before moving to Week 05.

Use the missed-question map instead of rereading everything:

| Missed questions | Review |
|---|---|
| 1, 8, 9, 18 | Week boundary and progressive build sections |
| 2, 3, 4, 7, 14, 15 | Routes, multipart, JSON, and `/docs` sections |
| 5, 10, 13, 17 | Input validation and status-code sections |
| 6, 11, 16 | Mock/real predictor boundary |
| 12 | `PredictionResult` confidence contract |

After reviewing, write a new answer in your own words rather than changing only the selected letter.

<!-- NAV_FOOTER_START -->

---

## 📚 Week 04 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| **6** | **quiz.md** ← *You are here* | **Knowledge Assessment Quiz** |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

### Within-Week Navigation

[← Validation & Verification](validation-checklist.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Knowledge Assessment Quiz** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Reflection & Consolidation →](reflection.md)

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
