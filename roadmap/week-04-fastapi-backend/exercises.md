# Week 04 Exercises: FastAPI Requests, Uploads, and Responses

## How to Use These Exercises

Complete these before the build task. They assume only the Week 03 cumulative product state; no Android networking or real model knowledge is required.

Save outputs in:

```text
docs/evidence/week-04/exercises/
```

---

## Exercise 1: Draw the Client-Server Boundary

### Goal
Separate work already completed from work intentionally deferred.

### Task
Draw or write the flow from an API tester to FastAPI and back. Label Android upload as Week 05 and real inference as Week 06.

### Output
`exercise-01-client-server-boundary.md`

### Validation
- [ ] Week 03 Android image input is shown as existing.
- [ ] Week 04 is tested without Android.
- [ ] Future work is not claimed as complete.

---

## Exercise 2: Trace HTTP Requests

### Goal
Distinguish GET from POST.

### Task
For `/health`, `/diseases`, and `/predict`, record the method, input, success status, and response purpose.

### Output
`exercise-02-http-table.md`

---

## Exercise 3: Inspect the Prediction Contract

### Goal
Understand one stable upload contract.

### Task
Record:

```text
Method:
Path:
Multipart field name:
Accepted input:
Success fields:
Possible error statuses:
```

### Output
`exercise-03-prediction-contract.md`

### Validation
- [ ] The multipart field is exactly `image`.
- [ ] Confidence is interpreted on the 0.0–1.0 scale.
- [ ] The contract matches `backend-api/README.md`.

---

## Exercise 4: Classify Validation Cases

### Goal
Connect invalid input to safe HTTP errors.

### Task
Predict the result for a valid image, empty upload, text file, fake image bytes, oversized image, and unavailable real model.

### Output
`exercise-04-validation-cases.md`

---

## Exercise 5: Use Interactive API Docs

### Goal
Test the backend before another client depends on it.

### Task
Start the server, open `/docs`, call `/health`, call `/diseases`, and upload one sample image to `/predict`.

### Output
`exercise-05-docs-test.md` plus screenshots.

### Validation
- [ ] Server starts without an import error.
- [ ] GET endpoints return 200.
- [ ] `/predict` receives the file under `image`.
- [ ] Response JSON is readable.

---

## Exercise 6: Debug One Failure

### Goal
Practise evidence-based debugging.

### Task
Record one failed request:

```text
Request:
Expected:
Actual status and detail:
Likely cause:
Fix:
Retest result:
```

### Output
`exercise-06-debug-record.md`

---

## Completion Rule

Start the build task only when you can explain the API boundary, GET versus POST, multipart field `image`, JSON response, and at least three error paths.

<!-- NAV_FOOTER_START -->

---

## 📚 Week 04 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| **3** | **exercises.md** ← *You are here* | **Practice Exercises** |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

### Within-Week Navigation

[← Theory & Learning Notes](learning-notes.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Practice Exercises** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Build Implementation Guide →](build-task.md)

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
