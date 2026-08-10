# Week 04 Exercises: FastAPI Requests, Uploads, and Responses

## How to Use These Exercises

Complete these before the build task. They assume only the Week 03 cumulative product state; no Android networking or real model knowledge is required.

Save outputs in:

```text
docs/evidence/week-04/exercises/
```

Create exactly six exercise files. Each file should contain your own explanation, not copied paragraphs from `learning-notes.md` or `backend-api/README.md`.

| Exercise | Required evidence file | Main CSE 2206 idea |
|---:|---|---|
| 1 | `exercise-01-client-server-boundary.md` | Component separation |
| 2 | `exercise-02-http-table.md` | HTTP request and response |
| 3 | `exercise-03-prediction-contract.md` | API contract design |
| 4 | `exercise-04-validation-cases.md` | Defensive input validation |
| 5 | `exercise-05-docs-test.md` | Manual integration testing |
| 6 | `exercise-06-debug-record.md` | Evidence-based debugging |

Do the exercises in order. Exercises 1–4 can be completed before starting Uvicorn. Exercises 5–6 require the Week 04 development environment.

---

## Exercise 1: Draw the Client-Server Boundary

### Goal
Separate work already completed from work intentionally deferred.

### Task
Draw or write the flow from an API tester to FastAPI and back. Label Android upload as Week 05 and real inference as Week 06.

Use this incomplete structure and replace each question with your own words:

```text
Already complete in Week 03:
	Android can ______________________________________

Week 04 request:
	API tester -> [HTTP method] [path]
						 -> multipart field ___________________
						 -> FastAPI validates _________________
						 -> mock predictor returns ____________

Week 04 response:
	FastAPI -> status ______
					-> JSON containing ______________________

Deferred:
	Week 05 __________________________________________
	Week 06 __________________________________________
```

Add one paragraph explaining why testing FastAPI without Android makes failures easier to locate.

### Output
`exercise-01-client-server-boundary.md`

### Validation
- [ ] Week 03 Android image input is shown as existing.
- [ ] Week 04 is tested without Android.
- [ ] Future work is not claimed as complete.
- [ ] The diagram shows both request and response directions.
- [ ] Mock output is not described as real disease recognition.

---

## Exercise 2: Trace HTTP Requests

### Goal
Distinguish GET from POST.

### Task
For `/health`, `/diseases`, and `/predict`, record the method, input, success status, and response purpose.

Create this table and complete every cell:

| Path | Method | Input | Success status | Response purpose | One failure status |
|---|---|---|---:|---|---:|
| `/` | | | | | |
| `/health` | | | | | |
| `/diseases` | | | | | |
| `/predict` | | | | | |

Then answer:

1. Why are `/` and `/health` allowed to use the same Python function?
2. Why is `/predict` POST instead of GET?
3. Which endpoint tells a client whether mock mode is active?
4. Why is a status code part of the API contract rather than only a server detail?

### Output
`exercise-02-http-table.md`

### Validation

- [ ] All four paths are included.
- [ ] GET is used for information retrieval and POST for upload processing.
- [ ] `/predict` input names multipart and the field `image`.
- [ ] Status descriptions match the Week 04 learning notes.

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

Below that summary, build a field dictionary for all eight successful response fields:

| Field | Data type | Meaning | Example shape or range |
|---|---|---|---|
| `model_label` | | | |
| `disease` | | | |
| `confidence` | | | |
| `uncertain` | | | |
| `guidance_available` | | | |
| `symptoms` | | | |
| `treatment` | | | |
| `prevention` | | | |

Finally, explain why `model_label` and `disease` are both needed, and why `guidance_available` can be false even when the model label is valid.

### Output
`exercise-03-prediction-contract.md`

### Validation
- [ ] The multipart field is exactly `image`.
- [ ] Confidence is interpreted on the 0.0–1.0 scale.
- [ ] The contract matches `backend-api/README.md`.
- [ ] All eight response fields are present.
- [ ] The difference between 38 labels and 10 reviewed guidance entries is explained.

---

## Exercise 4: Classify Validation Cases

### Goal
Connect invalid input to safe HTTP errors.

### Task
Predict the result for a valid image, empty upload, text file, fake image bytes, oversized image, and unavailable real model.

Use this table before running any request:

| Case | Request detail | Predicted status | Why the server should choose it | Code boundary responsible |
|---|---|---:|---|---|
| Valid PNG | `image/png`, decodable bytes | | | |
| Missing field | no multipart `image` part | | | |
| Empty image | `image/png`, zero bytes | | | |
| Text upload | `text/plain` | | | |
| Spoofed image | `image/png`, invalid bytes | | | |
| Oversized upload | limit plus one byte | | | |
| Real mode unavailable | valid image, no loaded model | | | |

After completing the table, compare it with `main.py` and correct any prediction in a separate **Corrections** section. Do not erase the original prediction; the difference is useful learning evidence.

### Output
`exercise-04-validation-cases.md`

### Validation

- [ ] Missing multipart data is distinguished from invalid image data.
- [ ] MIME-type checking and byte decoding are treated as separate checks.
- [ ] Oversized content maps to 413.
- [ ] Unavailable real mode maps to 503 rather than a fake success.
- [ ] The upload is closed on success and failure.

---

## Exercise 5: Use Interactive API Docs

### Goal
Test the backend before another client depends on it.

### Task
Start the server, open `/docs`, call `/health`, call `/diseases`, and upload one sample image to `/predict`.

Use mock mode for this exercise. Follow the exact environment and server commands from `backend-api/README.md`; do not invent a second setup procedure in your evidence.

Record this observation table:

| Action | Expected | Actual status | Important response value | Evidence filename |
|---|---|---:|---|---|
| Open `/docs` | Interactive route list | | | |
| Call `/health` | Runtime mode visible | | | |
| Call `/diseases` | Count and list agree | | | |
| Upload valid image | Eight-field JSON | | | |

For the valid upload, write the exact multipart field name and list the eight keys returned. State explicitly that the response proves the API pipeline, not model accuracy.

### Output
`exercise-05-docs-test.md` plus screenshots.

### Validation
- [ ] Server starts without an import error.
- [ ] GET endpoints return 200.
- [ ] `/predict` receives the file under `image`.
- [ ] Response JSON is readable.
- [ ] `/health` clearly reports mock mode.
- [ ] `/diseases` returns 10 reviewed entries.
- [ ] The valid response contains all eight documented fields.

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

Choose one of these failures:

- missing `image` field
- text upload
- spoofed image bytes
- empty upload
- oversized upload
- real mode without a loaded model

Include the request construction, status code, safe response detail, relevant server-side boundary, and corrected retest. If you intentionally trigger the problem, say so; do not describe it as an accidental backend defect.

Add two final answers:

1. What observation ruled out your first incorrect hypothesis, if you had one?
2. How would the same status help the Week 05 Android client show a useful message?

### Output
`exercise-06-debug-record.md`

### Validation

- [ ] The actual status and detail are recorded exactly.
- [ ] The likely cause is supported by evidence.
- [ ] The fix changes the request or the responsible code boundary, not an unrelated file.
- [ ] The retest result is recorded.
- [ ] No internal traceback or private path is placed in public evidence.

---

## Completion Rule

Start the build task only when:

- all six evidence files exist
- you can explain the API boundary without mentioning Retrofit code
- you can distinguish GET from POST
- you can name the multipart field `image` without checking the answer
- you can name all eight successful response fields
- you can predict 200, 400, 413, 422, and 503 cases
- you can explain why mock mode is useful but is not real inference

The exercises plan and observe the backend. They do not require edits to either Android project.

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
