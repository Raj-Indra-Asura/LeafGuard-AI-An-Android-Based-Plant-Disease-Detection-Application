# Week 04 Build Task: Verify the Standalone FastAPI Backend

## Objective

Build or verify the backend slice that accepts a multipart `image` upload and returns structured mock prediction JSON.

Do not connect Android and do not require real-model inference.

---

## Before You Start

- [ ] Week 03 validation passes.
- [ ] Python is installed.
- [ ] You read `learning-notes.md`.
- [ ] You completed the six exercises.
- [ ] You can identify `backend-api/`.

---

## Target Evidence Folder

```text
docs/evidence/week-04/
```

Save command output as text where possible and screenshots for `/docs`.

---

## Step 1: Inspect the Existing Backend

Open `backend-api/README.md`, then locate:

- app routes in `main.py`
- limits and mode in `config.py`
- predictor boundary in `model_loader.py`
- labels in `labels.py` and `labels-38.txt`
- tests in `test_api.py`

Checkpoint: explain each file in one sentence before editing anything.

---

## Step 2: Create an Isolated Environment

From `backend-api/`, create and activate a virtual environment using the platform-specific instructions in its README.

Install the development requirements:

```text
python -m pip install -r requirements-dev.txt
```

Checkpoint: Python imports FastAPI and the environment folder remains untracked.

---

## Step 3: Verify the Health Routes

Confirm `/` and `/health`:

- use GET
- return status information
- identify mock/real runtime mode
- expose enough configuration to diagnose setup

Checkpoint: both routes return 200 in tests.

---

## Step 4: Verify the Disease List

Confirm `/diseases` returns reviewed entries in a stable JSON structure.

Checkpoint: the count matches the returned list and no Android code is involved.

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

---

## Step 6: Keep Mock and Real Boundaries Honest

For Week 04:

- mock mode may return a model-shaped response
- `/health` must reveal the mode
- the response is not evidence of real AI accuracy
- unavailable real mode should fail clearly

Checkpoint: you can explain why Week 06 owns real cloud inference.

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

---

## Step 8: Run the Development Server

Start Uvicorn with the command documented in `backend-api/README.md`.

Use:

```text
http://localhost:8000/docs
```

Do not expose the development server to the public internet.

---

## Step 9: Perform the Milestone Demo

1. Call `/health`.
2. Call `/diseases`.
3. Open `/predict`.
4. Upload a sample leaf image under `image`.
5. Verify 200 and inspect every response field.
6. Upload an invalid file and verify a safe error.

Use an image from `sample-images/` if needed.

---

## Step 10: Save Evidence

- [ ] `docs-health.png`
- [ ] `docs-diseases.png`
- [ ] `predict-valid.png`
- [ ] `predict-invalid.png`
- [ ] `backend-tests.txt`
- [ ] `api-contract.md`
- [ ] reflection and quiz answers

Do not save private IP addresses, credentials, virtual environments, or uploaded user photos as repository evidence.

---

## Step 11: Validate the Week Boundary

Confirm:

- Android projects are unchanged from Week 03.
- No Retrofit code is required.
- No real-model result is claimed.
- Week 05 can rely on the documented API contract.

---

## Done Means

Week 04 is complete when the server starts, tests pass, `/docs` accepts `image`, valid JSON returns, invalid content fails safely, and the student can explain the boundary in their own words.

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
