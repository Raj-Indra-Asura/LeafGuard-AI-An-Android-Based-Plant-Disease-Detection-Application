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

## 12. End-of-Week-04 File Inventory

```text
backend-api/
|-- main.py
|-- config.py
|-- model_loader.py
|-- labels.py
|-- labels-38.txt
|-- requirements-base.txt
|-- requirements-dev.txt
|-- requirements.txt
|-- test_api.py
`-- README.md
```

Use the existing backend files as the source of truth. Week 04 learning work should explain and validate this state, not duplicate entire production files inside the roadmap.

---

## 13. Week 04 Understanding Checklist

- [ ] I can explain client and server roles.
- [ ] I can distinguish GET and POST.
- [ ] I can explain multipart field `image`.
- [ ] I can read the prediction JSON.
- [ ] I can explain mock mode.
- [ ] I can describe at least three error statuses.
- [ ] I can test the service without Android.
- [ ] I know what Weeks 05 and 06 add.

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
