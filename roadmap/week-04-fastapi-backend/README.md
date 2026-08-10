# Week 04: Standalone FastAPI Backend

## Week 04 Mindset

Week 03 gave the Android app a real leaf-image input flow. Week 04 leaves that Android code unchanged and builds the server-side half separately:

> Start a FastAPI server -> verify its health -> upload a leaf image through `/docs` -> receive a structured mock prediction.

The Android app does **not** call the backend this week. A real model is also outside this week's boundary. Week 05 connects Android to this API, and Week 06 replaces the mock prediction with real cloud inference.

---

## Product State After Week 04

**Cumulative product contribution: 35%**

By the end of Week 04, the repository should:

- run a local FastAPI service
- expose `/`, `/health`, `/diseases`, and `/predict`
- accept a multipart image part named `image`
- reject empty, oversized, or invalid uploads safely
- return predictable JSON from the mock prediction path
- prove the backend independently through `/docs` and automated tests

### What the product can do after Week 04

- Accept a leaf image on the server side.
- Return a JSON disease prediction contract.
- Show that the future Android client has a tested API to call.

### What the product still cannot do

- Android cannot upload its selected image until Week 05.
- The backend does not need real model inference until Week 06.
- Scan history, disease-library UI, and offline inference remain future work.

---

## Repository State After Week 04

Week 04 keeps all Week 01–03 work and adds the first independently runnable backend slice.

### Structure to browse after this week

- `backend-api/main.py` owns the FastAPI app and routes.
- `backend-api/config.py` owns environment-based settings and limits.
- `backend-api/model_loader.py` provides the mock/real predictor boundary.
- `backend-api/labels.py` loads and formats labels.
- `backend-api/requirements*.txt` lists runtime and test dependencies.
- `backend-api/test_api.py` verifies the API contract.
- `backend-api/README.md` explains setup and commands.
- `docs/evidence/week-04/` stores proof of this week's work.

### Files you should create or update this week

| Change | File | Week 04 purpose |
|---|---|---|
| CREATE/UPDATE | `backend-api/main.py` | Health, disease-list, and image-upload routes |
| CREATE/UPDATE | `backend-api/config.py` | Mock mode and upload configuration |
| CREATE/UPDATE | `backend-api/model_loader.py` | Mock predictor boundary |
| CREATE/UPDATE | `backend-api/labels.py` | Label loading and display names |
| CREATE/UPDATE | `backend-api/requirements*.txt` | Reproducible dependencies |
| CREATE/UPDATE | `backend-api/test_api.py` | Contract and error-path tests |
| UPDATE | `backend-api/README.md` | Setup, run, and test instructions |

Do not modify either Android app for Week 04.

---

## New Words This Week

| Term | Beginner Definition |
|---|---|
| Backend | A program that receives requests and returns results to a client. |
| API | A documented set of requests and responses programs use to communicate. |
| Endpoint | One API path and method, such as `POST /predict`. |
| HTTP | The request-response protocol used by the client and server. |
| JSON | Structured text used for API responses. |
| Multipart | An HTTP body format that can carry a file under a named field. |
| Status code | A number describing the result, such as 200, 400, 413, or 503. |
| Mock | A controlled substitute used before the real model is integrated. |
| Contract | The agreed request field, response fields, and error behavior. |

---

## Weekly Objective

By the end of Week 04, you will be able to:

1. Explain the Android-client and FastAPI-server boundary.
2. Create and run a Python virtual environment.
3. Explain GET and POST requests.
4. trace a multipart upload named `image`.
5. Read the JSON returned by `/predict`.
6. Test successful and invalid requests.
7. Explain why Android networking and real inference wait.

---

## What You Will Build

| Endpoint | Method | Input | Week 04 result |
|---|---|---|---|
| `/` | GET | none | Basic service health |
| `/health` | GET | none | Runtime/mock status |
| `/diseases` | GET | none | Available reviewed disease information |
| `/predict` | POST | multipart field `image` | Mock prediction JSON or a clear HTTP error |

The successful prediction response must use one stable shape. Follow the current backend contract documented in `backend-api/README.md`; do not invent a second response format in roadmap exercises.

---

## How This Connects to CSE 2206

This week supports CSE 2206 mobile networking concepts without mixing two new systems at once:

- client-server architecture
- HTTP request and response flow
- multipart file transfer
- JSON data interchange
- validation and error handling
- independent component testing

Week 04 builds and tests the server. Week 05 applies the Android networking concepts that call it.

---

## Suggested 7-Day Plan

| Day | Focus | Output |
|---|---|---|
| Day 1 | Client-server and HTTP basics | Request-response diagram |
| Day 2 | Python environment and FastAPI | Server starts locally |
| Day 3 | Health and disease endpoints | GET routes verified |
| Day 4 | Multipart upload contract | `/predict` accepts `image` |
| Day 5 | Validation and errors | Invalid uploads rejected safely |
| Day 6 | Automated tests and `/docs` | Contract tests pass |
| Day 7 | Evidence, quiz, reflection | Week 04 package complete |

---

## Milestone Demo

1. Start the backend.
2. Open `http://localhost:8000/docs`.
3. Check `/health`.
4. Upload a real leaf image to `/predict` using field `image`.
5. Read the returned JSON.
6. Submit an invalid file and explain the error status.
7. Explain why no Android or real-model behavior is required yet.

This proves the server-side product state is 35%.

---

## Week 04 File Order

| Step | File | Purpose |
|---:|---|---|
| 1 | `README.md` | Understand the product slice and boundaries. |
| 2 | `learning-notes.md` | Learn backend concepts from zero. |
| 3 | `exercises.md` | Practise the request, response, and contract. |
| 4 | `build-task.md` | Build and verify the standalone backend. |
| 5 | `validation-checklist.md` | Prove the Week 04 slice works. |
| 6 | `quiz.md` | Check understanding. |
| 7 | `reflection.md` | Explain what you built and why. |

Move to Week 05 only after the milestone demo and validation checklist pass.

<!-- NAV_FOOTER_START -->

---

## 📈 Product State After This Week

**Cumulative product completion: 35%** *(official model: [PRODUCT_PROGRESS_MAP.md](../../PRODUCT_PROGRESS_MAP.md))*

- **Your product can now…** accept an image on the server and return a structured mock disease prediction.
- **Your product still cannot…** send the Week 03 image from Android to the server; Week 05 adds that connection.
- **Android tracks remain unchanged:** Kotlin (`android-app-kotlin/`, primary) and Java (`android-app/`, secondary).

### Cumulative Repository State After Week 04

```text
LeafGuard-AI/
|-- roadmap/week-01-project-understanding/       (foundation)
|-- roadmap/week-02-android-basics-ui/           (Android shell)
|-- roadmap/week-03-camera-gallery/              (image input)
|-- roadmap/week-04-fastapi-backend/             (server learning system)
|-- docs/evidence/week-01/ ... week-04/
|-- android-app-kotlin/                          (unchanged from Week 03)
|-- android-app/                                 (unchanged from Week 03)
`-- backend-api/
    |-- main.py
    |-- config.py
    |-- model_loader.py
    |-- labels.py
    |-- requirements-base.txt
    |-- requirements-dev.txt
    |-- requirements.txt
    |-- test_api.py
    `-- README.md
```

---

## 📚 Week 04 — Navigation

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

### Within-Week Navigation

*(Start of week)* &nbsp;&nbsp;|&nbsp;&nbsp; **Week Overview & Objectives** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Theory & Learning Notes →](learning-notes.md)

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
