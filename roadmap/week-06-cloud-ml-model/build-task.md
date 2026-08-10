# Week 06 Build Task: Validate the Approved Keras Model

## Objective

Acquire and validate the exact Keras artifact, activate FastAPI real mode, preserve the Week 05 API, and document limitations honestly.

Estimated time: 8 to 10 hours, excluding binary download time.

---

## Before You Start

- [ ] Week 05 milestone demo passes.
- [ ] Week 04 backend tests pass.
- [ ] Six Week 06 exercises are complete.
- [ ] Python environment can install TensorFlow 2.19.1.
- [ ] You understand that the approved binary is tracked and must not be silently replaced or duplicated.
- [ ] You can explain raw `[0,255]` caller preprocessing.

Evidence folder:

```text
docs/evidence/week-06/
|-- exercises/
|-- artifact-identity.txt
|-- model-inspection.txt
|-- model-contract-tests.txt
|-- real-health.json
|-- real-prediction.json
|-- failure-mode.txt
|-- validation.md
|-- quiz-answers.md
`-- reflection-answers.md
```

---

## Step 1: Reconfirm the Week 05 Contract

Record:

```text
Request: POST /predict, multipart field image
Response: model_label, disease, confidence, uncertain,
          guidance_available, symptoms, treatment, prevention
Android changes required in Week 06: 0
```

Run the eight backend API regression tests before adding the model.

Checkpoint: `Ran 8 tests` and `OK`.

---

## Step 2: Acquire and Identify the Artifact

Use the pinned source information from `model/model-notes.md`. Place the downloaded file at:

```text
backend-api/models/leafguard_model.keras
```

Verify:

```bash
stat -c '%s' backend-api/models/leafguard_model.keras
sha256sum backend-api/models/leafguard_model.keras
git ls-files backend-api/models/leafguard_model.keras
```

Expected size:

```text
25143175
```

Expected SHA-256:

```text
08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f
```

The Git command must print `backend-api/models/leafguard_model.keras`.

Stop if size or hash differs. Do not rename an unverified artifact into place.

---

## Step 3: Synchronize Canonical Labels

Create or verify `model/labels-38.txt` from learning-notes Section 12. Confirm it matches the backend file:

```bash
cmp model/labels-38.txt backend-api/labels-38.txt
```

Checkpoint:

- 38 non-empty lines
- 38 unique values
- exact byte order matches
- no sorting

---

## Step 4: Add the Contract Validator and Inspector

Create or verify:

```text
model/model_contract.py
model/inspect_model.py
model/test_model_contract.py
```

Use the complete files in learning-notes Section 12.

These checks own:

- label count and uniqueness
- input shape and dtype
- output count
- embedded rescaling
- rejection of incompatible shapes/scaling

They do not own model accuracy.

---

## Step 5: Install Real-Mode Dependencies

From `backend-api/`:

```bash
python -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements.txt
```

`requirements.txt` pins TensorFlow 2.19.1 and includes the Week 04 base requirements.

If a constrained Linux environment terminates while installing the full TensorFlow wheel, `tensorflow-cpu==2.19.1` may be used in that local validation environment because it exposes the same Keras API. Do not silently change the checked-in dependency manifest without compatibility review.

Checkpoint:

```bash
.venv/bin/python -c "import tensorflow as tf; print(tf.__version__)"
```

Expected: `2.19.1`.

---

## Step 6: Inspect the Model Contract

From the repository root:

```bash
backend-api/.venv/bin/python model/inspect_model.py
```

Required output facts:

```text
Labels: 38
Caller preprocessing: raw float32 [0,255]
Keras input: (None, 224, 224, 3), float32
Keras output: (None, 38), float32
Embedded preprocessing: [0,255] to [-1,1]
Keras contract: valid
```

Save the complete output.

---

## Step 7: Run Focused Contract Tests

```bash
cd model
../backend-api/.venv/bin/python -m unittest -v test_model_contract
```

Expected:

```text
Ran 4 tests
OK
```

The suite must prove:

1. canonical/backend labels match
2. approved Keras model contract passes
3. wrong input shape is rejected
4. wrong embedded scaling is rejected

Skipped TensorFlow tests do not complete Week 06. A real TensorFlow environment is required.

---

## Step 8: Activate FastAPI Real Mode

Start from `backend-api/`:

```bash
USE_MOCK=false .venv/bin/uvicorn main:app --reload
```

Open `/health` and verify:

```text
status = ok
use_mock = false
model_loaded = true
image_size = 224
class_count = 38
```

Setting `USE_MOCK=false` without `model_loaded=true` is not success.

---

## Step 9: Run One Real Prediction

Upload a real sample image under multipart key `image` through `/docs` or the unchanged Android app.

Verify:

- HTTP 200
- canonical `model_label`
- confidence from 0.0 to 1.0
- all eight response fields
- uncertainty matches threshold behavior
- guidance availability is honest

Save the response and `/health` together so mode is visible.

Do not infer accuracy from one prediction.

---

## Step 10: Prove Failure Behavior

Temporarily point `MODEL_PATH` to a missing file or move the local artifact outside the expected path. Restart the process in real mode.

Expected:

- `/health` reports `model_loaded=false`
- valid `/predict` returns 503
- server logs explain model loading failure
- client receives no raw traceback
- backend does not silently call this real inference

Restore the verified model and rerun health plus prediction.

---

## Step 11: Preserve Week Boundaries

Confirm:

- Android Kotlin/Java files have no Week 06 delta
- Retrofit response fields remain unchanged
- no TFLite conversion is required
- no Android `model.tflite` is required
- no offline classifier is required
- no Room/history code is required

The current repository may later contain those features. They are not evidence for this reconstructed week.

---

## Evidence to Save

1. Artifact size and SHA-256.
2. Provenance/license record.
3. Label comparison.
4. Model inspector output.
5. Four passing contract tests.
6. Real-mode `/health`.
7. Real-mode prediction JSON.
8. Missing-model 503 behavior.
9. Week 05 Android regression screenshot.
10. Honest limitations note.

Never save the model binary itself in the evidence folder.

---

## Done Means

- exact artifact identity matches
- provenance is complete
- TensorFlow 2.19.1 imports
- all 38 labels match in order
- Keras shape, dtype, and rescaling pass
- four focused tests pass without skips
- real-mode health is true
- one real-mode prediction returns unchanged JSON
- missing model fails clearly
- Android remains compatible and unchanged
- no unsupported accuracy claim is made

<!-- NAV_FOOTER_START -->

---

## Week 06 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | **Build Task - current** | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Exercises](exercises.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Validation](validation-checklist.md)
