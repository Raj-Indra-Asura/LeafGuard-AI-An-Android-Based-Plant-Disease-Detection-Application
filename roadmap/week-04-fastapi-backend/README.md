# Week 04: Standalone FastAPI Backend

## Week 04 Mindset

Week 03 gave the Android app a real leaf-image input flow. Week 04 leaves that Android code unchanged and builds the server-side half separately:

> Start a FastAPI server -> verify its health -> upload a leaf image through `/docs` -> receive a structured mock prediction.

The Android app does **not** call the backend this week. A real model is also outside this week's boundary. Week 05 connects Android to this API, and Week 06 replaces the mock prediction with real cloud inference.

---

## How Week 04 Continues the Progressive Build

Each week accepts one verified input from the previous week and produces one verified output for the next week.

| Week | Verified input | New work | Verified output |
|---:|---|---|---|
| 01 | Product idea | User journey and weekly slices | A buildable product plan |
| 02 | Week 01 plan | Android Activities, layouts, and navigation | A runnable six-screen shell |
| 03 | Week 02 Scan placeholder | Camera, gallery, URI, and preview | A real selected-image URI |
| **04** | **Week 03 image concept and stable Android shell** | **Standalone FastAPI upload contract** | **A tested server ready for an Android client** |
| 05 | Week 03 URI + Week 04 API contract | Retrofit and multipart Android upload | Android-to-backend communication |

The Week 03 URI is not sent anywhere during Week 04. It is the conceptual handoff: the learner already understands where an image comes from, and now learns what the future receiver must accept.

```text
Week 03 Android                         Week 04 backend
----------------                       ----------------
selectedImageUri                       POST /predict
camera/gallery preview                 multipart field: image
device-side cancellation               server-side validation
no network code                        no Android code
          \                            /
           `-- Week 05 connects both --'
```

This separation is a CSE 2206 client-server design exercise. If a request fails in Week 04, the failure belongs to the server or request contract; Android networking cannot be the cause yet.

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

### Exact Week 04 Implementation State

Week 04 works with **13 required text files** in `backend-api/`: 10 core files and 3 environment/container support files. It changes **0 Android files** and requires **0 real-model artifacts**.

| Group | Count | Files | Required action |
|---|---:|---|---|
| Python source and tests | 5 | `main.py`, `config.py`, `model_loader.py`, `labels.py`, `test_api.py` | Read, trace, run, and correct only if validation fails |
| Contract/data files | 2 | `labels-38.txt`, `README.md` | Preserve label order; follow operational commands |
| Dependency manifests | 3 | `requirements-base.txt`, `requirements-dev.txt`, `requirements.txt` | Install `requirements-dev.txt` for Week 04 |
| Support files | 3 | `.env.example`, `.dockerignore`, `Dockerfile` | Understand their boundary; Docker is optional this week |
| Android changes | 0 | `android-app-kotlin/`, `android-app/` | Leave at the verified Week 03 state |

Exact checked-in sizes:

| File group | Logical lines |
|---|---:|
| `main.py` | 216 |
| `config.py` | 16 |
| `model_loader.py` | 87 |
| `labels.py` | 37 |
| `test_api.py` | 86 |
| **Python source and tests total** | **442** |
| `labels-38.txt` | 38 |
| `README.md` | 920 |
| Three requirements files | 10 total |
| `.env.example`, `.dockerignore`, `Dockerfile` | 37 total |

Logical line counts describe the checked-in learning snapshot. They are a reconstruction aid, not a reason to add blank lines or reformat working code. The authoritative implementation explanation is in [learning-notes.md section 12](learning-notes.md#12-end-of-week-04-file-inventory-exact-files-exact-roles-exact-size).

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
4. Trace a multipart upload named `image`.
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

### How the Seven Files Form One Learning Loop

The files are not seven descriptions of the same task. Each has one role and produces an input for the next.

| File | Student action | Concrete output |
|---|---|---|
| `README.md` | Identify the product slice and exclusions | Scope statement |
| `learning-notes.md` | Explain the concepts and exact implementation | Understanding checklist |
| `exercises.md` | Practise the contract before changing code | Six evidence files |
| `build-task.md` | Verify the implementation from the inside out | Running API and test output |
| `validation-checklist.md` | Prove every required behavior and boundary | Completed validation record |
| `quiz.md` | Recall and apply the concepts without copying | Score of at least 14/18 |
| `reflection.md` | Explain decisions, failures, and the Week 05 handoff | Reflection answers |

Do not skip directly from the overview to the server command. The progression is deliberately **understand -> practise -> build -> validate -> recall -> reflect**.

---

## Exact Completion Contract

Week 04 is complete only when all of these quantities agree:

| Quantity | Required value |
|---|---:|
| API routes | 4 paths: `/`, `/health`, `/diseases`, `/predict` |
| HTTP methods | 3 GET path operations + 1 POST path operation |
| Multipart upload field | 1, named exactly `image` |
| Successful response fields | 8 |
| Canonical labels | 38 |
| Reviewed guidance entries | 10 |
| Automated contract tests | 8 |
| Required Week 04 evidence items | 9 |
| Android files modified for Week 04 | 0 |
| Real model required for Week 04 | 0 |

The eight successful prediction fields are `model_label`, `disease`, `confidence`, `uncertain`, `guidance_available`, `symptoms`, `treatment`, and `prevention`.

The numbers alone do not prove completion. The tests and milestone demo must show that the code behind them behaves correctly.

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
