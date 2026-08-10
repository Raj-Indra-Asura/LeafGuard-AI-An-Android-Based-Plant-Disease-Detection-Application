# Week 04 Learning Notes: FastAPI Backend Fundamentals

## Purpose

These notes explain only the concepts needed to build and verify the Week 04 standalone backend. Read them before the exercises and build task.

---

## 1. How Week 04 Grows From Week 03

Week 03 produces an image URI inside Android. Week 04 creates the separate service that will eventually receive image bytes.

```text
Week 03: Android can obtain and preview an image.
Week 04: FastAPI can receive and answer an image upload independently.
Week 05: Android sends the Week 03 image to the Week 04 API.
Week 06: The backend uses a real model instead of a mock.
```

Keeping those slices separate makes failures easier to locate.

---

## 2. Client-Server Architecture

A **client** sends a request. A **server** receives it and sends a response.

```text
API tester                     FastAPI
    |-- request ----------------->|
    |                             | validate and process
    |<-- status + JSON -----------|
```

FastAPI does not know whether the future request comes from Kotlin, Java, `/docs`, curl, or a test. It only knows the API contract.

---

## 3. HTTP Methods and Status Codes

- **GET** asks for information without uploading an image.
- **POST** sends data for processing.

Week 04 uses these common outcomes:

| Status | Meaning |
|---:|---|
| 200 | Request succeeded. |
| 400 | Uploaded content is missing or not a valid image. |
| 413 | Uploaded image is larger than the configured limit. |
| 422 | Required multipart field is missing or malformed. |
| 503 | Real mode was requested, but the real model is unavailable. |

The status code is part of the contract, not only a debugging detail.

---

## 4. FastAPI Routes

A route combines an HTTP method and path:

```python
@app.get("/health")
async def health_check():
    # TODO: Return the current server mode.
    ...
```

The decorator registers the endpoint. The function handles the request. `async` allows FastAPI to wait for input without blocking the whole service.

Week 04 needs:

- `GET /`
- `GET /health`
- `GET /diseases`
- `POST /predict`

---

## 5. Multipart Image Upload

Images are binary data, so `/predict` uses `multipart/form-data`. The file part must be named:

```text
image
```

Minimal route shape:

```python
@app.post("/predict")
async def predict(image: UploadFile = File(...)):
    # TODO: Validate, read, and close the upload safely.
    ...
```

If a client sends `file`, `photo`, or another key, it has broken the agreed contract.

---

## 6. JSON Response Contract

JSON gives the future Android client named values it can parse. Use the response fields already defined by `backend-api/main.py` and documented by FastAPI's `/docs`.

Important ideas:

- `disease` is a display-friendly name.
- `model_label` preserves the model-facing label.
- `confidence` stays between 0.0 and 1.0.
- `uncertain` indicates confidence below the configured threshold.
- guidance fields provide symptoms, treatment, and prevention.

Do not create a different response shape in an exercise or API-testing tool.

---

## 7. Mock Mode

A mock predictor lets you verify upload, validation, JSON, and errors before real model integration.

A good mock is:

- clearly identified by `/health`
- deterministic enough for tests
- shaped like the future real response
- never described as a medically confirmed diagnosis

Mock mode proves the pipeline contract, not AI accuracy.

---

## 8. Input Validation and Cleanup

The backend should:

1. confirm the request contains an image content type
2. read only up to the configured maximum plus one byte
3. reject empty or oversized content
4. decode the bytes as a real image
5. return a safe error without exposing internal details
6. close the uploaded file

Checking only the filename extension is not enough because bytes can be disguised.

---

## 9. Virtual Environment and Dependencies

A virtual environment isolates Python packages for this project.

```text
python -m venv .venv
activate .venv
python -m pip install -r requirements-dev.txt
```

Use the exact commands in `backend-api/README.md` for your operating system. Do not commit `.venv/`.

Before adding or changing a dependency, understand why the backend needs it.

---

## 10. Testing From the Inside Out

Use this order:

1. import the app successfully
2. run automated API tests
3. start Uvicorn locally
4. verify GET endpoints in `/docs`
5. upload one valid sample image
6. test one invalid file
7. optionally test another device on the same trusted local network

Automated tests prove repeatable behavior. `/docs` proves a person can understand and use the contract.

---

## 11. Common Mistakes to Avoid

- Sending multipart key `file` instead of `image`.
- Claiming mock output is real inference.
- Changing Android code during Week 04.
- Hardcoding a private network address into source files.
- Accepting any bytes just because the filename ends in `.png`.
- Exposing raw exception messages to clients.
- Running the development server publicly.
- Skipping invalid-input tests.

---

## 12. End-of-Week-04 File Inventory (Exact Files, Exact Roles, Exact Size)

Week 03 finished the Android image-input slice. Week 04 leaves that slice unchanged and introduces an independently runnable Python backend.

> Week 04 creates or updates **10 core backend files**, uses **3 environment/container support files**, rewrites **0 Android files**, and keeps the Week 03 camera/gallery feature unchanged.

The counts below describe the checked-in Week 04 backend learning state. They are **logical line counts**, so the count does not change when a final newline is added or removed. The Week 04 build uses mock mode; the checked-in Keras model belongs to the Week 06 real-inference boundary and is not required to pass this week.

---

### 12.1 Change Summary: Week 03 → Week 04

| Change type | Count | Files |
|---|---:|---|
| Core backend create/update | 10 | `main.py`, `config.py`, `model_loader.py`, `labels.py`, `labels-38.txt`, three `requirements*.txt` files, `test_api.py`, `README.md` |
| Environment/container support | 3 | `.env.example`, `.dockerignore`, `Dockerfile` |
| Android files changed | 0 | Both Android tracks remain at their Week 03 image-input state |
| Real-model artifact required | 0 | `models/leafguard_model.keras` is deferred to Week 06 |
| Evidence location | 1 directory | `docs/evidence/week-04/` |

**Required Week 04 text files inside `backend-api/`: 13.**

**Total Week 04 Python source and test code: 442 logical lines**:

- `main.py`: 216
- `config.py`: 16
- `model_loader.py`: 87
- `labels.py`: 37
- `test_api.py`: 86

The most important Week 04 boundary is:

```text
Android image URI                            FastAPI upload
exists and previews                          works independently
        |                                           |
        `----------- no connection yet -------------'
                     Week 05 adds it
```

No Retrofit dependency, `INTERNET` permission, base URL, or Android response model is added this week.

---

### 12.2 The Exact Week 04 Tree

The Android tree is the exact Week 03 tree from Section 11.2 of the [Week 03 learning notes](../week-03-camera-gallery/learning-notes.md), unchanged. Week 04 adds this backend learning state beside it:

```text
LeafGuard-AI/
|-- android-app-kotlin/                         UNCHANGED FROM WEEK 03
|-- android-app/                                UNCHANGED FROM WEEK 03
|-- docs/evidence/week-04/                      EVIDENCE OUTPUT
`-- backend-api/
    |-- .dockerignore                           SUPPORT       7 lines
    |-- .env.example                            SUPPORT       7 lines
    |-- Dockerfile                              SUPPORT      23 lines
    |-- README.md                               CORE        920 lines
    |-- config.py                               CORE         16 lines
    |-- labels-38.txt                           CORE         38 lines
    |-- labels.py                               CORE         37 lines
    |-- main.py                                 CORE        216 lines
    |-- model_loader.py                         CORE         87 lines
    |-- requirements-base.txt                   CORE          6 lines
    |-- requirements-dev.txt                    CORE          2 lines
    |-- requirements.txt                        CORE          2 lines
    |-- test_api.py                             CORE         86 lines
    `-- models/
        |-- leafguard_model.keras               PROVIDED/FUTURE: WEEK 06
        `-- temp                                NOT A WEEK 04 REQUIREMENT
```

Status meanings:

| Status | Meaning |
|---|---|
| `CORE` | Read, run, or test this file during Week 04. |
| `SUPPORT` | Helps configuration or packaging but is not the main learning target. |
| `UNCHANGED FROM WEEK 03` | Do not edit it during this week. |
| `PROVIDED/FUTURE` | It may exist in the repository, but Week 04 does not claim real inference from it. |
| `EVIDENCE OUTPUT` | Store proof here; do not put screenshots inside `backend-api/`. |

Why the full Week 03 Android tree is not copied a second time: copying it here would create two sources of truth. Section 11.2 of the Week 03 notes remains the exact Android snapshot; this section records only Week 04's delta.

---

### 12.3 New or Rewritten File: `main.py` (216 lines)

This is the central Week 04 file. It creates the FastAPI application, defines the contract, validates uploads, and returns responses.

#### 12.3.1 Where the 216 lines go

| Block | Lines | Job |
|---|---:|---|
| Imports and configuration imports | 1–21 | Bring in FastAPI, Pillow, NumPy, Pydantic, settings, labels, and predictor loading. |
| Logging | 23–24 | Create safe server-side diagnostics. |
| Reviewed disease information | 26–78 | Store guidance for the 10 reviewed classes. |
| Labels and predictor initialization | 80–81 | Load the canonical 38 labels and one predictor instance. |
| `PredictionResult` | 84–92 | Define the successful JSON response contract. |
| FastAPI and CORS setup | 95–107 | Create the app and configure allowed origins. |
| `preprocess_image` | 110–120 | Decode, resize, and convert valid image bytes. |
| `/` and `/health` | 123–135 | Report service status and runtime mode. |
| `/diseases` | 138–151 | Return the 10 reviewed guidance records. |
| `/predict` | 154–216 | Validate, read, preprocess, predict, format, handle errors, and close the upload. |

#### 12.3.2 Imports and configuration

The imports separate into four groups:

| Group | Important names | Why needed |
|---|---|---|
| Python standard library | `io`, `logging`, `Dict` | Read in-memory bytes, write server logs, describe dictionary types. |
| Image and array libraries | `PIL.Image`, `UnidentifiedImageError`, `numpy` | Prove bytes are an image and turn pixels into a model-shaped array. |
| FastAPI and Pydantic | `FastAPI`, `File`, `HTTPException`, `UploadFile`, `BaseModel` | Define routes, receive the file, return safe errors, and document JSON. |
| Local modules | `config`, `labels`, `model_loader` | Keep settings, label rules, and prediction behavior outside the route file. |

The local imports are the first separation-of-concerns lesson of Week 04. `main.py` coordinates the request; it does not hardcode environment parsing or model loading.

#### 12.3.3 The reviewed disease dictionary

`DISEASE_INFO` contains symptoms, treatment, and prevention for 10 reviewed labels. It does **not** claim that every one of the 38 model classes has reviewed guidance.

```python
DISEASE_INFO: Dict[str, Dict[str, str]] = {
    "Tomato___Early_blight": {
        "symptoms": "...",
        "treatment": "...",
        "prevention": "...",
    },
    # Nine more reviewed entries
}
```

Two counts intentionally differ:

| Value | Count | Meaning |
|---|---:|---|
| `CLASS_NAMES` | 38 | Labels the approved model can return. |
| `DISEASE_INFO` | 10 | Labels with reviewed guidance in this project version. |

That is why the response contains `guidance_available`. A valid model label can exist without detailed project guidance.

#### 12.3.4 The response model

```python
class PredictionResult(BaseModel):
    model_label: str
    disease: str
    confidence: float
    uncertain: bool
    guidance_available: bool
    symptoms: str
    treatment: str
    prevention: str
```

| Field | Meaning |
|---|---|
| `model_label` | Exact canonical label, such as `Tomato___Early_blight`. |
| `disease` | Human-readable form, such as `Tomato Early Blight`. |
| `confidence` | Number from 0.0 to 1.0. |
| `uncertain` | `true` when confidence is below `CONFIDENCE_THRESHOLD`. |
| `guidance_available` | Whether the project has reviewed guidance for this exact label. |
| `symptoms`, `treatment`, `prevention` | Guidance text or a safe fallback. |

Pydantic uses this class to validate the returned object and FastAPI uses it to generate `/docs`. The class is therefore executable documentation, not only a Python type.

#### 12.3.5 App creation and CORS

```python
app = FastAPI(
    title="LeafGuard AI Backend",
    description="FastAPI service for plant disease detection using a Keras model or a mock fallback.",
    version="1.0.0",
)
```

`app` is the object Uvicorn starts with `uvicorn main:app`. The first `main` means `main.py`; the second `app` means this variable.

CORS controls browser origins. It is useful for browser-based tools, but it is not Android authentication and must not be treated as one. Week 04 keeps local development simple; production restriction belongs to deployment work.

#### 12.3.6 Image preprocessing boundary

```python
def preprocess_image(raw_bytes: bytes) -> np.ndarray:
    try:
        image = Image.open(io.BytesIO(raw_bytes)).convert("RGB")
    except (UnidentifiedImageError, OSError) as exc:
        raise HTTPException(status_code=400, detail="Invalid image file supplied.") from exc

    resized_image = image.resize((IMAGE_SIZE, IMAGE_SIZE))
    image_array = np.asarray(resized_image, dtype=np.float32)
    return np.expand_dims(image_array, axis=0)
```

| Step | Result |
|---|---|
| `BytesIO(raw_bytes)` | Makes uploaded bytes readable like a file. |
| `Image.open(...)` | Verifies the bytes decode as an image; a `.png` filename alone is not trusted. |
| `.convert("RGB")` | Produces exactly three color channels. |
| `.resize((224, 224))` | Matches the configured model input width and height. |
| `np.asarray(..., float32)` | Converts pixels to a numeric tensor. |
| `np.expand_dims(..., axis=0)` | Changes `(224, 224, 3)` into batch shape `(1, 224, 224, 3)`. |

The array remains in the raw `[0, 255]` range because the approved model contains its own rescaling layer. Changing normalization without checking the model would silently damage Week 06 predictions.

#### 12.3.7 GET routes

`GET /` and `GET /health` call the same function. They report:

- `status`
- whether mock mode is active
- whether a real model loaded
- configured model and label paths
- image size
- class count

`GET /diseases` maps the 10 reviewed entries into display names and guidance. It is a library endpoint, not a prediction.

#### 12.3.8 The `/predict` happy path

The successful route runs in this exact order:

```text
1. Confirm Content-Type begins with image/
2. Read at most MAX_IMAGE_SIZE_BYTES + 1
3. Reject empty content
4. Reject content over the limit
5. Decode and resize the image
6. Confirm a predictor is available
7. Run mock or real predictor
8. Find reviewed guidance or safe fallback text
9. Compare confidence with the threshold
10. Return PredictionResult JSON
11. Close UploadFile in finally
```

Reading "limit plus one byte" is deliberate. It answers the yes/no question "is this upload too large?" without loading an unlimited request body into memory.

#### 12.3.9 The `/predict` failure paths

| Condition | Status | Safe response behavior |
|---|---:|---|
| Multipart field is absent or not named `image` | 422 | FastAPI reports the required field. |
| Declared content type is not an image | 400 | Reject before image processing. |
| Upload is empty | 400 | Explain that the image is empty. |
| Bytes pretend to be an image but cannot decode | 400 | Return `Invalid image file supplied.` |
| Upload exceeds the configured limit | 413 | Report the upload limit. |
| Real mode has no loaded model | 503 | Tell the client to check health and server logs. |
| Unexpected internal failure | 500 | Log details on the server but return a generic client message. |

The `finally` block runs for success and failure:

```python
finally:
    await image.close()
```

This is resource cleanup. Removing it can leave temporary upload resources open under repeated requests.

---

### 12.4 New or Rewritten File: `config.py` (16 lines)

`config.py` reads optional environment values once and gives the rest of the backend typed settings.

```python
import os
from pathlib import Path

from dotenv import load_dotenv

BASE_DIR = Path(__file__).resolve().parent
load_dotenv(BASE_DIR / ".env")

MODEL_PATH = os.getenv("MODEL_PATH", str(BASE_DIR / "models" / "leafguard_model.keras"))
LABELS_PATH = os.getenv("LABELS_PATH", str(BASE_DIR / "labels-38.txt"))
IMAGE_SIZE = int(os.getenv("IMAGE_SIZE", "224"))
CONFIDENCE_THRESHOLD = float(os.getenv("CONFIDENCE_THRESHOLD", "0.50"))
MAX_IMAGE_SIZE_BYTES = int(os.getenv("MAX_IMAGE_SIZE_BYTES", str(10 * 1024 * 1024)))
USE_MOCK = os.getenv("USE_MOCK", "false").strip().lower() in {"1", "true", "yes", "on"}
PORT = int(os.getenv("PORT", "8000"))
ALLOWED_ORIGINS = [origin.strip() for origin in os.getenv("ALLOWED_ORIGINS", "*").split(",") if origin.strip()]
```

| Setting | Default | Week 04 meaning |
|---|---|---|
| `MODEL_PATH` | `models/leafguard_model.keras` | Future real-model location. |
| `LABELS_PATH` | `labels-38.txt` | Canonical ordered labels. |
| `IMAGE_SIZE` | `224` | Required square input size. |
| `CONFIDENCE_THRESHOLD` | `0.50` | Below this, `uncertain` becomes true. |
| `MAX_IMAGE_SIZE_BYTES` | 10 MiB | Upload memory/safety limit. |
| `USE_MOCK` | `false` | Set true for the standalone Week 04 mock demonstration. |
| `PORT` | `8000` | Local server port. |
| `ALLOWED_ORIGINS` | `*` | Development CORS setting. |

Why environment variables are used: the same source can run with different local paths or ports without editing and committing machine-specific values.

---

### 12.5 New or Rewritten File: `model_loader.py` (87 lines)

This file creates one stable predictor interface for two implementations:

```text
ModelPredictor.predict(image_batch)
              |
              |-- mock mode --> deterministic practice result
              `-- real mode --> Keras model result
```

#### 12.5.1 Optional TensorFlow import

TensorFlow is intentionally optional for Week 04. If it cannot import, the service can still start and explain through `/health` that real inference is unavailable.

The warning belongs in server logs. It is not sent as a raw exception to clients.

#### 12.5.2 `ModelPredictor`

| Member | Job |
|---|---|
| `class_names` | Preserve the canonical output order. |
| `model` | Hold the loaded Keras model or `None`. |
| `use_mock` | Choose mock behavior explicitly. |
| `model_loaded` | Report real-model availability to `/health`. |
| `predict(...)` | Expose one call regardless of mode. |
| `_mock_predict(...)` | Return a controlled result for contract testing. |

Mock prediction uses image mean intensity to select a bounded label index. This is deterministic enough for tests, but it is **not disease recognition**.

#### 12.5.3 `load_predictor`

The loader makes these decisions in order:

1. If `USE_MOCK` is true, skip model loading.
2. If TensorFlow is unavailable, return an unloaded real predictor.
3. If the model path does not exist, return an unloaded real predictor.
4. Load the Keras model.
5. Require input shape `(None, 224, 224, 3)`.
6. Require output count to match the 38 labels.
7. If validation fails, log the failure and keep real inference disabled.

The shape checks protect against connecting a valid but incompatible model. They prepare Week 06 without claiming Week 06 is complete.

---

### 12.6 New or Rewritten Files: `labels.py` (37 lines) and `labels-38.txt` (38 lines)

`labels-38.txt` is the canonical ordered output list. One non-empty line equals one model class. Order matters because output index `0` maps to line 1, index `1` maps to line 2, and so on.

`load_labels` rejects three invalid states:

| Invalid state | Why rejected |
|---|---|
| File does not exist | The model output cannot be interpreted safely. |
| File contains no usable labels | Every numeric output would be unnamed. |
| File contains duplicates | Two output indexes would appear to mean the same class. |

`display_label` converts model-facing names to readable UI text:

```text
Tomato___Early_blight  ->  Tomato Early Blight
Potato___Healthy       ->  Potato Healthy
```

Special overrides handle labels whose capitalization or wording cannot be improved by simply replacing underscores.

Do not sort `labels-38.txt` alphabetically. Reordering labels without retraining or confirming model output order makes every prediction label unreliable.

---

### 12.7 New or Rewritten Files: Dependency Manifests (10 lines total)

#### `requirements-base.txt` (6 lines)

```text
fastapi==0.109.0
uvicorn[standard]==0.27.0
python-multipart==0.0.6
pillow==10.2.0
numpy==1.26.3
python-dotenv==1.0.0
```

| Package | Week 04 job |
|---|---|
| FastAPI | API routes, validation, and generated docs. |
| Uvicorn | Local ASGI server. |
| python-multipart | Parse the uploaded `image` form part. |
| Pillow | Decode and resize image bytes. |
| NumPy | Build the image tensor and mock result. |
| python-dotenv | Load local `.env` settings. |

#### `requirements-dev.txt` (2 lines)

```text
-r requirements-base.txt
httpx==0.26.0
```

It includes the base packages and adds the HTTP client required by FastAPI's `TestClient`.

#### `requirements.txt` (2 lines)

```text
-r requirements-base.txt
tensorflow==2.19.1
```

This is the heavier real-model environment. Week 04 can use `requirements-dev.txt`; TensorFlow integration is validated in Week 06.

Pinned versions make two learners install the same dependency set. Do not change a version merely because a newer one exists; first verify compatibility and security.

---

### 12.8 New or Rewritten File: `test_api.py` (86 lines)

The test file uses Python's built-in `unittest` runner and FastAPI's in-process `TestClient`. It does not require starting Uvicorn or connecting Android.

#### 12.8.1 Test helper

`make_png()` creates a valid 32 × 32 RGB image in memory. This keeps the success test small and repeatable without depending on a student's personal photo.

#### 12.8.2 The eight contract tests

| Test | Contract proved |
|---|---|
| `test_health_aliases_report_runtime_mode` | `/` and `/health` return 200, `status: ok`, and 38 classes. |
| `test_disease_library_keeps_ten_reviewed_entries` | `/diseases` reports exactly the 10 reviewed records it returns. |
| `test_predict_accepts_valid_image` | Multipart field `image` accepts a real PNG and returns bounded confidence. |
| `test_predict_returns_503_without_real_model` | Real mode fails clearly when no model is available. |
| `test_preprocessing_keeps_raw_rgb_values` | Tensor shape is `(1, 224, 224, 3)` and raw pixel scale is preserved. |
| `test_predict_rejects_non_image` | A text content type receives 400. |
| `test_predict_rejects_spoofed_image` | Fake bytes with `image/png` still receive 400. |
| `test_predict_rejects_oversized_upload` | Content beyond the configured limit receives 413. |

Notice the balance: three tests prove successful information flow and five protect boundaries or failure behavior. A backend is not validated by one successful upload alone.

---

### 12.9 New or Rewritten File: `README.md` (920 lines)

The backend README is the operational source of truth. The Week 04 roadmap explains concepts; the backend README gives exact setup and command variations.

Read it in this order for Week 04:

| README section | What to learn now |
|---|---|
| Overview and prerequisites | What the service does and which Python version is expected. |
| Project structure | Where backend responsibilities live. |
| Quick start | How to create `.venv` and install development requirements. |
| Environment variables | How to enable mock mode without editing Python. |
| Running the server | How `uvicorn main:app --reload` starts the API. |
| API documentation | How to open `/docs`. |
| Testing the API | How to run GET requests and multipart prediction requests. |
| Common issues | How to diagnose ports, imports, model absence, and upload limits. |
| Testing and logging | How repeatable tests differ from server diagnostics. |

Sections about Android connection, production hosting, performance, and real-model deployment are previews. Do not implement them during Week 04.

---

### 12.10 Supporting Files: Environment and Container Boundaries

#### `.env.example` (7 lines)

This file documents safe setting names and non-secret example values. Copy it to `.env` locally if needed; do not commit the resulting `.env`.

```text
MODEL_PATH=models/leafguard_model.keras
IMAGE_SIZE=224
CONFIDENCE_THRESHOLD=0.50
USE_MOCK=false
MAX_IMAGE_SIZE_BYTES=10485760
PORT=8000
ALLOWED_ORIGINS=*
```

For the Week 04 mock demonstration, set `USE_MOCK=true` in the local `.env` or current shell.

#### `.dockerignore` (7 lines)

It excludes Python caches, local environments, `.env`, test cache, and Keras model binaries from the Docker build context. This reduces accidental secret or large-artifact inclusion.

#### `Dockerfile` (23 lines)

The Dockerfile can package the API, install base requirements, optionally install TensorFlow, expose port 8000, and check `/health`.

Docker is **supporting context**, not a Week 04 completion requirement. The required learning path is the local virtual environment, automated tests, Uvicorn, and `/docs`.

---

### 12.11 Files Week 04 Does Not Rewrite

| Area | Week 04 status | Next relevant change |
|---|---|---|
| `ScanActivity` and camera/gallery resources | Unchanged from Week 03 | Week 05 sends the selected image URI. |
| Android manifests and Gradle files | No networking change | Week 05 adds client networking requirements. |
| Result screen | Still outside this server-only slice | Weeks 05–06 display an API result. |
| Room history | Not required | Later database week. |
| Offline TFLite assets and classifier | Not required | Later offline-inference work. |
| `models/leafguard_model.keras` | Not required for mock validation | Week 06 real cloud inference. |

Explicitly forbidden as Week 04 evidence:

- a hardcoded Android server URL
- an Android screenshot presented as proof of upload
- a mock result described as AI accuracy
- a publicly exposed development server
- a committed `.env`, credential, or private network address
- a real-model success claim based only on the existence of a `.keras` file

---

### 12.12 How to Verify Your Week 04 End State

Run these commands from the repository root. Use the activation command for your operating system from `backend-api/README.md`.

```bash
# 1. Enter the standalone backend
cd backend-api

# 2. Create and activate a local environment
python -m venv .venv
source .venv/bin/activate

# 3. Install only the Week 04 development environment
python -m pip install -r requirements-dev.txt

# 4. Prove the contract automatically
USE_MOCK=true python -m unittest test_api

# 5. Start the local server in mock mode
USE_MOCK=true uvicorn main:app --reload
```

In a second terminal or through `http://127.0.0.1:8000/docs`, verify:

| Test | Expected result |
|---|---|
| `GET /` | 200 with `status: "ok"`. |
| `GET /health` | 200 and mock mode clearly reported. |
| `GET /diseases` | 200, count 10, and 10 returned entries. |
| Valid image under multipart key `image` | 200 with every `PredictionResult` field. |
| Text file under key `image` | 400. |
| Fake image bytes under key `image` | 400. |
| Request with no `image` field | 422. |
| Upload over the configured limit | 413. |

Also verify the boundaries:

```bash
# Android should contain no Week 04 networking delta.
git diff -- android-app-kotlin android-app

# Local secrets and environments must not be tracked.
git ls-files backend-api/.env backend-api/.venv

# The documented core files must exist.
ls main.py config.py model_loader.py labels.py labels-38.txt \
   requirements-base.txt requirements-dev.txt requirements.txt \
   test_api.py README.md
```

The first two commands should print no Week 04 Android change and no tracked local `.env`/`.venv` path.

Save evidence in `docs/evidence/week-04/`:

1. `/health` response showing mock mode
2. `/diseases` response
3. successful `/predict` response
4. invalid-upload response
5. automated test summary
6. short API contract note

Do not save a private IP address, local environment file, or model binary as evidence.

---

## 13. Week 04 Understanding Checklist

- [ ] I can explain how Week 04 grows from the Week 03 image URI.
- [ ] I can explain client and server roles.
- [ ] I can distinguish GET and POST.
- [ ] I can explain why `/predict` uses multipart field `image`.
- [ ] I can name every successful prediction response field.
- [ ] I can explain why 38 model labels and 10 reviewed guidance entries are both correct.
- [ ] I can explain mock mode without calling it real disease recognition.
- [ ] I can explain 200, 400, 413, 422, 500, and 503.
- [ ] I can explain why image decoding is stronger than checking a filename.
- [ ] I can explain why uploads are read with a size limit and closed in `finally`.
- [ ] I can run the automated tests without Android.
- [ ] I can demonstrate the API through `/docs`.
- [ ] I can identify which files Week 04 changes and which Android files remain untouched.
- [ ] I know that Week 05 connects Android and Week 06 validates real model inference.

<!-- NAV_FOOTER_START -->

---

## 📚 Week 04 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| **2** | **learning-notes.md** ← *You are here* | **Theory & Learning Notes** |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

### Within-Week Navigation

[← Week Overview & Objectives](README.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Theory & Learning Notes** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Practice Exercises →](exercises.md)

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
