# Week 04 Build Task: Verify the Standalone FastAPI Backend

## Objective

Build or verify the backend slice that accepts a multipart `image` upload and returns structured mock prediction JSON.

Do not connect Android and do not require real-model inference.

This task uses the existing checked-in backend as a learning implementation. Inspect first, run the cheapest check, and edit only when observed behavior disagrees with the Week 04 contract. Exact line counts describe the target snapshot; never add blank lines or duplicate code merely to reach a number.

---

## Before You Start

- [ ] Week 03 validation passes.
- [ ] Python is installed.
- [ ] You read `learning-notes.md`.
- [ ] You completed the six exercises.
- [ ] You can identify `backend-api/`.
- [ ] You can explain why the Week 03 URI is not used by this standalone server yet.
- [ ] You know that `requirements-dev.txt`, not the TensorFlow environment, is enough for mock-mode validation.

---

## Target Evidence Folder

```text
docs/evidence/week-04/
```

Save command output as text where possible and screenshots for `/docs`.

Suggested evidence structure:

```text
docs/evidence/week-04/
|-- screenshots/
|   |-- docs-health.png
|   |-- docs-diseases.png
|   |-- predict-valid.png
|   `-- predict-invalid.png
|-- exercises/
|-- api-contract.md
|-- backend-tests.txt
|-- validation.md
|-- quiz-answers.md
`-- reflection-answers.md
```

Do not store `.env`, `.venv`, model binaries, private IP addresses, or personal uploaded photos in this folder.

---

## Target State Before You Build

The checked-in Week 04 target has these exact boundaries:

| Target | Required value |
|---|---:|
| Core backend files | 10 |
| Support files | 3 |
| Python source/test lines | 442 |
| Routes | 4 paths |
| Automated tests | 8 |
| Canonical labels | 38 |
| Reviewed guidance records | 10 |
| Android files changed | 0 |
| Real model required | 0 |

The exact file-by-file inventory and code explanation are in [learning-notes.md section 12](learning-notes.md#12-end-of-week-04-file-inventory-exact-files-exact-roles-exact-size).

---

## Step 1: Inspect the Existing Backend

Open `backend-api/README.md`, then locate:

- app routes in `main.py`
- limits and mode in `config.py`
- predictor boundary in `model_loader.py`
- labels in `labels.py` and `labels-38.txt`
- tests in `test_api.py`

Checkpoint: explain each file in one sentence before editing anything.

Use this ownership table in `docs/evidence/week-04/api-contract.md`:

| File | One responsibility | Week 04 action |
|---|---|---|
| `main.py` | HTTP routes, validation, preprocessing, response model | Trace request flow |
| `config.py` | Environment-derived settings | Record relevant defaults |
| `model_loader.py` | Stable mock/real predictor interface | Explain mode selection |
| `labels.py` | Canonical label validation and display formatting | Explain ordering rule |
| `labels-38.txt` | Ordered model output labels | Count; do not sort |
| `test_api.py` | Repeatable API contract checks | Run all eight tests |
| `requirements*.txt` | Development and real-model dependency sets | Install development set only |
| `README.md` | Operational source of truth | Follow exact commands |

Stop if a core file is missing. Use the inventory in the learning notes to identify the missing responsibility before creating or copying anything.

---

## Step 2: Create an Isolated Environment

From `backend-api/`, create and activate a virtual environment using the platform-specific instructions in its README.

Install the development requirements:

```text
python -m pip install -r requirements-dev.txt
```

Checkpoint: Python imports FastAPI and the environment folder remains untracked.

Verify the environment from `backend-api/`:

```bash
python -c "import fastapi, httpx, PIL, numpy; print('Week 04 imports OK')"
git ls-files .env .venv
```

Expected:

- the import command prints `Week 04 imports OK`
- the Git command prints no tracked `.env` or `.venv` path
- TensorFlow is not required for these checks

If imports fail, confirm the virtual environment is active and the install command used `requirements-dev.txt` from the current directory.

---

## Step 3: Verify the Health Routes

Confirm `/` and `/health`:

- use GET
- return status information
- identify mock/real runtime mode
- expose enough configuration to diagnose setup

Checkpoint: both routes return 200 in tests.

Record the seven health keys:

```text
status, use_mock, model_loaded, model_path,
image_size, class_count, labels_path
```

For the Week 04 demo, `status` must be `ok`, `use_mock` must clearly identify mock mode, `image_size` must be 224, and `class_count` must be 38. A health response proves service configuration; it does not prove prediction accuracy.

---

## Step 4: Verify the Disease List

Confirm `/diseases` returns reviewed entries in a stable JSON structure.

Checkpoint: the count matches the returned list and no Android code is involved.

Required observation:

```text
count == len(diseases) == 10
```

Open one returned item and identify `model_label`, `name`, `symptoms`, `treatment`, and `prevention`. Explain why this endpoint contains 10 reviewed records while the prediction label file contains 38 labels.

---

## Step 5: Verify the Upload Contract

Confirm `/predict`:

- uses POST
- requires multipart field `image`
- reads the upload with a size limit
- rejects empty, oversized, or undecodable content
- closes the upload
- returns the documented response model

Do not paste a second full endpoint from the roadmap. Compare the existing implementation with this checklist and make only necessary corrections.

Trace one successful request in this exact order and write the owning line or block in `api-contract.md`:

1. FastAPI requires multipart field `image`.
2. Content type must begin with `image/`.
3. The route reads at most `MAX_IMAGE_SIZE_BYTES + 1`.
4. Empty and oversized bytes are rejected.
5. Pillow decodes, converts to RGB, and resizes the image.
6. NumPy creates `(1, 224, 224, 3)` `float32` input.
7. The predictor returns one canonical label and bounded confidence.
8. Reviewed guidance or safe fallback text is selected.
9. Confidence is compared with the configured threshold.
10. `PredictionResult` returns eight fields.
11. `UploadFile` is closed in `finally`.

Checkpoint: explain which failures occur before decoding, during decoding, before prediction, and during unexpected runtime handling.

---

## Step 6: Keep Mock and Real Boundaries Honest

For Week 04:

- mock mode may return a model-shaped response
- `/health` must reveal the mode
- the response is not evidence of real AI accuracy
- unavailable real mode should fail clearly

Checkpoint: you can explain why Week 06 owns real cloud inference.

Mock mode must be explicit in both configuration and `/health`. The mock implementation may choose a deterministic label and confidence so tests are repeatable, but it must never be used as evidence that a plant disease was recognized.

Real mode without an available model must return 503. Silently switching an intended real request to mock output would hide a deployment failure and violate the contract.

---

## Step 7: Run Automated Tests

Run the existing backend test command documented in `backend-api/README.md`.

Required checks:

- health aliases
- disease list
- valid image upload
- non-image rejection
- spoofed-image rejection
- oversized-upload rejection
- unavailable real-model behavior

Save the passing summary in `docs/evidence/week-04/backend-tests.txt`.

Run from `backend-api/` with mock mode explicit:

```bash
USE_MOCK=true python -m unittest -v test_api
```

Expected summary:

```text
Ran 8 tests
OK
```

The eight tests must collectively prove:

| Area | Test evidence |
|---|---|
| Health | `/` and `/health` return runtime data |
| Disease data | Exactly 10 reviewed entries are returned |
| Valid upload | A real in-memory PNG returns bounded confidence |
| Real-mode failure | An unavailable model returns 503 |
| Preprocessing | Shape and raw RGB scale match the contract |
| MIME rejection | Text content receives 400 |
| Decode rejection | Spoofed image bytes receive 400 |
| Size rejection | Limit plus one byte receives 413 |

If a test fails, save the exact failing test name and assertion. Fix only the owning boundary, rerun that test, then rerun all eight.

---

## Step 8: Run the Development Server

Start Uvicorn with the command documented in `backend-api/README.md`.

Use:

```text
http://localhost:8000/docs
```

Do not expose the development server to the public internet.

For the Ubuntu/macOS shell, the Week 04 server command is:

```bash
USE_MOCK=true uvicorn main:app --reload
```

Run it from `backend-api/`. Confirm startup before opening `/docs`. Use `127.0.0.1` or `localhost`; do not bind the learning server to a public interface.

---

## Step 9: Perform the Milestone Demo

1. Call `/health`.
2. Call `/diseases`.
3. Open `/predict`.
4. Upload a sample leaf image under `image`.
5. Verify 200 and inspect every response field.
6. Upload an invalid file and verify a safe error.

Use an image from `sample-images/` if needed.

Record the valid response without changing its shape:

```text
model_label
disease
confidence
uncertain
guidance_available
symptoms
treatment
prevention
```

Then make one invalid request. The milestone is incomplete if only the happy path is shown.

---

## Step 10: Save Evidence

- [ ] `docs-health.png`
- [ ] `docs-diseases.png`
- [ ] `predict-valid.png`
- [ ] `predict-invalid.png`
- [ ] `backend-tests.txt`
- [ ] `api-contract.md`
- [ ] reflection and quiz answers

The nine required evidence items are:

1. Health response evidence.
2. Disease-list evidence.
3. Valid prediction evidence.
4. Invalid-upload evidence.
5. Automated test summary.
6. API contract note.
7. Quiz answers.
8. Reflection answers.
9. Updated progress tracker or Week 04 progress record.

Screenshots should show the relevant route, status, and response without exposing machine-specific paths or addresses.

Do not save private IP addresses, credentials, virtual environments, or uploaded user photos as repository evidence.

---

## Step 11: Validate the Week Boundary

Confirm:

- Android projects are unchanged from Week 03.
- No Retrofit code is required.
- No real-model result is claimed.
- Week 05 can rely on the documented API contract.

Run these boundary checks from the repository root:

```bash
git diff -- android-app-kotlin android-app
git ls-files backend-api/.env backend-api/.venv
```

Both commands should produce no Week 04 Android change and no tracked local environment path. If the first command shows earlier intentional work, document that it predates Week 04 rather than deleting it.

Write the Week 05 handoff in one sentence:

> Android must read its selected URI, create multipart field `image`, call `POST /predict`, and parse the existing eight-field response without changing the server contract.

---

## Done Means

Week 04 is complete when the server starts, tests pass, `/docs` accepts `image`, valid JSON returns, invalid content fails safely, and the student can explain the boundary in their own words.

Before marking it complete, answer these verification questions without looking at the code:

1. Why is the multipart field named `image`?
2. Why are MIME checking and image decoding both required?
3. Why does the server read the limit plus one byte?
4. Why can 38 valid labels coexist with only 10 guidance records?
5. What does mock mode prove, and what does it not prove?
6. Why is Android unchanged this week?

<!-- NAV_FOOTER_START -->

---

## 📚 Week 04 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| **4** | **build-task.md** ← *You are here* | **Build Implementation Guide** |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

### Within-Week Navigation

[← Practice Exercises](exercises.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Build Implementation Guide** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Validation & Verification →](validation-checklist.md)

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
