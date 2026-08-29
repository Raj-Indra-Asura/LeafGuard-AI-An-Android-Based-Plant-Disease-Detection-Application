# Week 04 Learning Notes: FastAPI Backend Fundamentals

## Purpose

These notes explain only the concepts needed to build and verify the Week 04 standalone backend. Read them before the exercises and build task.

The notes have two jobs:

1. Teach the backend ideas from the Week 03 starting point.
2. Reconstruct the exact checked-in Week 04 implementation without inventing future behavior.

Use the conceptual sections first. Use Section 12 only after you can explain the request-response flow in your own words; exact code counts are evidence of scope, not a substitute for understanding.

## Beginner Bridge: Read This Before Section 1

Week 04 is the first time this roadmap introduces a web API. That is a larger jump than the earlier Android-only weeks because you are learning a second program, a second language, and communication between programs at the same time.

Do not begin with the complete files in Section 12. Learn the ideas in this order:

```text
programs and responsibilities
    -> request and response
    -> URL, method, headers, and body
    -> JSON and multipart data
    -> Python and FastAPI basics
    -> one small GET route
    -> one file-upload route
    -> validation and safe errors
    -> mock prediction
    -> automated and manual testing
    -> complete Week 04 implementation
```

Each lesson below has a **Learn**, **Trace**, **Checkpoint**, and **If stuck** part. Do not continue when a checkpoint is unclear. Return to the smallest unclear lesson instead of copying the full backend.

### Bridge Lesson A: What an API Is

#### Learn

An application programming interface, or **API**, is an agreement for how one program asks another program to do something.

For LeafGuard AI:

- the **client** asks for a prediction
- the **server** receives the image and produces a response
- the **API contract** states exactly how that request and response must look

An API is not the server itself. The API is the set of agreed paths, methods, inputs, outputs, and errors that the server implements.

Beginner analogy:

> A restaurant is the server, a customer is the client, and the menu is the API contract. The customer orders using a known item name; the kitchen returns the promised kind of result.

The Week 04 client is `/docs`, curl, or an automated test. Android becomes a client in Week 05.

#### Trace

```text
client creates request
    -> network delivers request
    -> FastAPI selects a route
    -> route validates input
    -> route performs work
    -> FastAPI creates response
    -> client reads status and body
```

The server does not reach into the Android app to fetch an image. The client must send the image in a request.

#### Checkpoint

Explain these in your own words:

1. What is the difference between a client, a server, and an API?
2. Why can `/docs` test Week 04 without Android?
3. Which side starts the request?
4. What parts of the agreement would break if a client renamed `image` to `photo`?

#### If stuck

Draw two boxes named **API tester** and **FastAPI**. Draw one request arrow toward FastAPI and one response arrow back. Do not continue until both directions make sense.

---

### Bridge Lesson B: One HTTP Request Has Several Parts

#### Learn

**HTTP** is the request-response protocol used here. A request is not only a URL. It has several parts:

| Part | Week 04 example | Question it answers |
|---|---|---|
| Scheme | `http` | Which communication rules are used? |
| Host | `localhost` | Which computer should receive it? |
| Port | `8000` | Which program on that computer? |
| Path | `/predict` | Which server capability? |
| Method | `POST` | What kind of action? |
| Headers | `Content-Type: multipart/form-data; ...` | How should metadata and body be interpreted? |
| Body | image multipart part | What data is being sent? |

Together, the local prediction URL is:

```text
http://localhost:8000/predict
|--|   |-------| |--| |------|
scheme   host    port    path
```

The response also has parts:

| Part | Example | Purpose |
|---|---|---|
| Status code | `200` | Machine-readable outcome category |
| Headers | `Content-Type: application/json` | Description of response data |
| Body | `{ "disease": "...", ... }` | Returned data or safe error detail |

#### Trace

For `GET /health`, there is no upload body:

```text
GET /health
    -> route returns runtime information
    -> HTTP 200
    -> JSON body
```

For `POST /predict`, there is an image body:

```text
POST /predict
Content-Type: multipart/form-data; boundary=<generated value>

<multipart part named image>
    -> route validates and processes bytes
    -> HTTP status
    -> JSON body
```

The boundary is a generated separator between form parts. `/docs`, curl, Retrofit, and OkHttp generate it. You should not type or hardcode it.

#### Checkpoint

Given `http://localhost:8000/health`, identify the scheme, host, port, and path. Then explain why changing only the path from `/health` to `/predict` does not automatically upload a file.

#### If stuck

Use the browser for `/health`, then inspect it in `/docs`. Compare the visible path, method, status, and response body.

---

### Bridge Lesson C: GET, POST, and Status Codes

#### Learn

The HTTP **method** communicates the request's intent:

- `GET` retrieves a representation and should not carry out an image prediction upload.
- `POST` submits data for processing and is used by `/predict`.

The **status code** summarizes the result. The JSON body gives more detail.

| Code | Category | Week 04 interpretation | Client response |
|---:|---|---|---|
| 200 | Success | Request was accepted and completed | Parse success JSON |
| 400 | Bad request content | A part arrived, but its content is empty or invalid | Correct the file |
| 413 | Payload too large | Image exceeds the configured limit | Choose a smaller image |
| 422 | Request shape invalid | Required `image` part is missing or malformed | Correct the multipart contract |
| 500 | Unexpected server failure | An unplanned internal operation failed | Show a safe error; inspect server logs |
| 503 | Service unavailable | Real mode was requested but no usable model exists | Fix server model configuration |

Why `422` differs from `400`:

```text
no part named image
    -> FastAPI cannot bind the route parameter
    -> 422 before route work begins

part named image contains fake bytes
    -> FastAPI binds the parameter
    -> route inspects the content
    -> 400
```

The exact status matters to Week 05 because Android uses it to distinguish request-shape problems from content and server problems.

#### Checkpoint

Predict the status for:

1. a valid PNG under `image`
2. valid PNG bytes under `photo`
3. text bytes pretending to be an image
4. an image larger than the configured limit
5. a valid image while real mode has no model

Check your answers against `exercises.md` only after writing your first prediction.

#### If stuck

Ask two questions in order:

1. Did FastAPI receive the required request shape?
2. If yes, was the received content acceptable?

---

### Bridge Lesson D: JSON Is Structured Data, Not a Screen

#### Learn

**JSON** is a text format for exchanging named values. It has a small set of value types:

| JSON type | Example | Week 04 use |
|---|---|---|
| string | `"Tomato - Healthy"` | labels and guidance text |
| number | `0.86` | confidence |
| boolean | `true` | uncertainty and guidance flags |
| object | `{ "status": "ok" }` | a named response record |
| array | `[{...}, {...}]` | disease list |
| null | `null` | missing value, when a contract permits it |

JSON keys are case-sensitive. `model_label`, `modelLabel`, and `Model_Label` are different keys.

The successful prediction contract has exactly eight keys:

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

The server's Pydantic response model documents and checks this output shape. In Week 05, Gson maps the same JSON shape to Kotlin properties.

The meanings matter:

- `model_label` is the exact canonical class label.
- `disease` is readable display text.
- `confidence` remains a decimal in the range 0.0–1.0.
- `uncertain` is a server decision based on its configured threshold.
- `guidance_available` says whether reviewed guidance exists for that label.

A confidence of `0.86` is stored as `0.86`; turning it into `86%` is a display task for Week 05.

#### Trace

```text
Python PredictionResult
    -> FastAPI serializes values
    -> JSON travels in HTTP response
    -> API tester displays JSON text
```

**Serialization** means converting program values into a transport format such as JSON. **Deserialization** means converting received JSON back into program values.

#### Checkpoint

For each of the eight keys, state its JSON type and meaning. Explain why `model_label` and `disease` are not duplicates.

#### If stuck

Open the response model in `backend-api/main.py` and make a two-column list: **field** and **meaning**. Do not copy the complete endpoint.

---

### Bridge Lesson E: The Minimum Python Needed This Week

#### Learn

You do not need to master Python before Week 04, but you must recognize these pieces:

| Python form | Meaning |
|---|---|
| `from fastapi import FastAPI` | Import a name from an installed package |
| `app = FastAPI(...)` | Create an object and assign it to `app` |
| `def name(...):` | Define a function |
| `async def name(...):` | Define a function that can wait for asynchronous work |
| `return value` | Send a value back to the caller |
| `if condition:` | Run a block only when a condition is true |
| `try` / `except` / `finally` | Handle expected failures and guaranteed cleanup |
| `value: str` | Type hint: `value` should be a string |
| `-> PredictionResult` | Type hint for the returned value |
| `await image.read(...)` | Pause this request while an asynchronous operation completes |

Python uses indentation to group code. Four spaces normally mean “inside this function, condition, or error block.”

Minimal route skeleton:

```python
@app.get("/example")
async def example():
    # TODO: Return a small JSON-compatible dictionary.
    ...
```

The decorator line beginning with `@` registers the function as a route handler. The decorator is not a comment.

Error-handling mental model:

```text
try:
    operation that may fail
except KnownProblem:
    return or raise the intended safe error
finally:
    release the resource whether success or failure occurred
```

In `/predict`, `finally` closes the uploaded file even if decoding or prediction fails.

#### Trace

Read one route in `main.py` from top to bottom and identify:

1. decorator
2. function name
3. parameters
4. validation conditions
5. returned model
6. cleanup

#### Checkpoint

Explain why indentation matters, what `await` does at a beginner level, and why cleanup belongs in `finally`.

#### If stuck

Do not debug FastAPI and Python syntax simultaneously. First run:

```text
python -m py_compile main.py
```

A syntax or indentation error must be fixed before testing HTTP behavior.

---

### Bridge Lesson F: How FastAPI Connects HTTP to Python

#### Learn

FastAPI uses decorators and type information to connect an HTTP request to a Python function.

```python
@app.post("/predict")
async def predict(image: UploadFile = File(...)):
    # TODO: Validate the supplied upload.
    ...
```

Line-by-line meaning:

| Code | Meaning |
|---|---|
| `@app.post("/predict")` | Register POST requests for `/predict` |
| `async def predict` | Define the route handler |
| `image` | Python parameter and required multipart field name |
| `UploadFile` | Uploaded-file wrapper with metadata and file access |
| `File(...)` | Mark this value as required uploaded form data |
| `...` | Required, not an optional default |

FastAPI performs request binding before it calls the function. That is why a missing `image` part can produce `422` without entering the route body.

Pydantic's `BaseModel` describes structured output:

```python
class ExampleResponse(BaseModel):
    message: str
    ok: bool
```

FastAPI uses that model to:

- document the response in `/docs`
- validate and serialize returned values
- generate an OpenAPI description that other tools can understand

`/docs` is generated interactive documentation, not a separate manually programmed screen.

#### Trace

```text
HTTP POST /predict
    -> FastAPI matches method + path
    -> FastAPI binds multipart image
    -> predict(image=...)
    -> route returns PredictionResult
    -> FastAPI validates and serializes JSON
```

#### Checkpoint

Explain the difference between:

- a route path and a Python function name
- request validation and response validation
- `UploadFile` metadata and the actual uploaded bytes

#### If stuck

Use `/docs` to compare `/health` and `/predict`. Notice which one has a request-body control and which one does not.

---

### Bridge Lesson G: Multipart Uploads From Form to Bytes

#### Learn

JSON is good for text, numbers, booleans, arrays, and objects. Images are binary data, so this route uses **multipart form data**.

A simplified request looks like this:

```text
POST /predict HTTP/1.1
Content-Type: multipart/form-data; boundary=generated-boundary

--generated-boundary
Content-Disposition: form-data; name="image"; filename="leaf.jpg"
Content-Type: image/jpeg

<binary image bytes>
--generated-boundary--
```

This is for understanding only. Let the client library generate the boundary and headers.

Three different facts must not be confused:

| Fact | Example | What it helps with |
|---|---|---|
| Form field name | `image` | FastAPI binds the correct parameter |
| Filename | `leaf.jpg` | Descriptive upload metadata |
| MIME type | `image/jpeg` | Declared content category |

None of these alone proves that the bytes are a real image. A malicious or broken client can call text bytes `leaf.jpg` and declare `image/jpeg`. Pillow decoding is the stronger content check.

`UploadFile` exposes:

- `filename`
- `content_type`
- asynchronous `read(...)`
- asynchronous `close()`

The size-limited read uses `maximum + 1` bytes. If that extra byte exists, the upload is too large. This avoids loading an unlimited request into memory.

#### Trace

```text
multipart request
    -> required field name check
    -> declared MIME type check
    -> bounded byte read
    -> empty/size check
    -> Pillow decode
    -> RGB conversion and resize
```

#### Checkpoint

Explain why the server checks all of these:

1. field name
2. MIME type
3. byte length
4. image decoding

#### If stuck

Imagine a text file renamed to `leaf.png`. List which checks it might pass and which check should reject it.

---

### Bridge Lesson H: Image Preprocessing and the Week 06 Boundary

#### Learn

After validation, Pillow and NumPy convert the image into the shape the predictor interface expects:

```text
uploaded bytes
    -> decoded image
    -> RGB color mode
    -> 224 x 224 pixels
    -> float32 NumPy array
    -> batch shape (1, 224, 224, 3)
```

Shape meaning:

| Position | Value | Meaning |
|---:|---:|---|
| 1 | `1` | one image in this batch |
| 2 | `224` | image height |
| 3 | `224` | image width |
| 4 | `3` | red, green, and blue channels |

The Week 04 implementation deliberately keeps pixel values in the raw `0`–`255` range. The approved later model contains its own rescaling behavior. Adding another divide-by-255 operation would change the input contract and may silently reduce prediction quality.

Critical rule:

> Preprocessing must match the approved model. “Normalization is usually good” is not enough reason to change it.

Week 04 proves the array shape and type for the stable predictor boundary. Week 06 verifies the real model and its exact scaling assumptions.

#### Checkpoint

Explain every dimension in `(1, 224, 224, 3)`, why RGB has three channels, and why you must not invent a second normalization step.

#### If stuck

Read the preprocessing test in `test_api.py`. Identify the expected shape, data type, and representative pixel scale before changing any preprocessing code.

---

### Bridge Lesson I: Mock Mode Is a Controlled Substitute

#### Learn

A **mock** replaces an unfinished or unavailable dependency while preserving its interface.

The Week 04 mock:

- receives the same image-array shape as the future model
- returns one canonical label and confidence
- produces repeatable output for the same input
- lets API tests run without TensorFlow or a model file

The current mock result depends on the image's mean intensity. It is deterministic for the same input; it does not always return one fixed class.

```text
same input array
    -> same mean intensity
    -> same selected mock class
    -> same mock confidence
```

This design can prove that values travel through the pipeline. It cannot prove biological or model correctness.

`/health` exposes whether the service is using mock mode. Never hide an unavailable real model by silently claiming a mock result is real.

#### Checkpoint

Complete both sentences:

1. Mock mode proves ...
2. Mock mode does not prove ...

Then explain why deterministic behavior makes automated tests easier.

#### If stuck

Run the same valid upload twice in mock mode. Compare the response, then state why matching responses still do not demonstrate real disease detection.

---

### Bridge Lesson J: Validation, Errors, and Cleanup Form One Safety Chain

#### Learn

Validation should move from cheap checks to deeper checks:

```text
request shape
    -> declared type
    -> bounded size
    -> decodable bytes
    -> predictor availability
    -> prediction
    -> response construction
```

This order avoids expensive work on obviously bad input.

Expected problems should become specific `HTTPException` responses. Unexpected problems should be logged on the server and converted to a generic safe response. Do not send stack traces, local file paths, environment values, or raw exception details to clients.

The route's error structure has three roles:

| Block | Role |
|---|---|
| `except HTTPException: raise` | Preserve the intentional status and safe detail |
| generic `except Exception` | Log unexpected failure and return generic 500 |
| `finally` | Close the upload on every path |

Server logs are for developers. Response details are for clients. They should not contain the same amount of internal information.

#### Checkpoint

Explain why these are separate:

- logging an internal exception
- returning a safe client error
- closing the upload

#### If stuck

Trace one successful upload and one spoofed-image upload. Confirm that both end at `finally`.

---

### Bridge Lesson K: Virtual Environments and Running the Server

#### Learn

A Python installation can serve many projects. A virtual environment gives this project its own package directory.

```text
system Python
    -> create .venv
    -> activate .venv
    -> install Week 04 packages into .venv
    -> run FastAPI with that environment
```

Use `python -m ...` commands when possible because they use the currently selected Python interpreter.

Beginner verification sequence:

```text
1. confirm Python version
2. create .venv once
3. activate .venv in every new terminal
4. install requirements-dev.txt
5. verify imports
6. run tests
7. start Uvicorn
```

Activation changes which `python` and installed commands the terminal finds. It does not modify the project source.

| Symptom | Likely cause | First check |
|---|---|---|
| `python: command not found` | Platform uses `python3` or Python is missing | Run the platform command from `backend-api/README.md` |
| `No module named fastapi` | Wrong interpreter or requirements not installed | Check active environment, then use `python -m pip` |
| `uvicorn: command not found` | Environment inactive or executable not on path | Use `python -m uvicorn main:app --reload` |
| `Could not import module "main"` | Wrong working directory or import error | Run from `backend-api/`; inspect the first traceback |
| Address already in use | Another process owns port 8000 | Stop it or use a deliberate alternate port |

Useful interpreter checks:

```text
python --version
python -c "import sys; print(sys.executable)"
python -m pip --version
python -c "import fastapi, uvicorn; print('imports OK')"
```

On Windows, `where python` lists matching interpreters. On macOS/Linux, `which python` does the same after activation.

Do not commit `.venv/` or `.env`. The requirements files describe reproducible packages; the local environment directory is machine-specific.

#### Checkpoint

Close and reopen a terminal, then explain why activation must happen again. Confirm that `python` and `python -m pip` point to the same environment.

#### If stuck

Record the exact command, working directory, first error line, and `sys.executable`. Guessing without those four facts often creates more confusion.

---

### Bridge Lesson L: Testing in Layers

#### Learn

Different tests answer different questions:

| Layer | Tool | Question |
|---|---|---|
| Syntax/import | Python command | Can the application load? |
| Automated contract | `unittest` + FastAPI test client | Do repeatable success and failure cases match the contract? |
| Interactive API | `/docs` | Can a person construct and inspect requests? |
| Manual invalid case | `/docs` or curl | Does a visible failure stay safe and understandable? |
| Week boundary | Git/source inspection | Did Week 04 avoid Android and real-model claims? |

Start with the cheapest failing layer. If the app cannot import, opening a browser cannot fix it. If automated tests fail, connect no Android client yet.

When a test fails, read it in this order:

1. failing test name
2. expected value
3. actual value
4. responsible route or helper
5. smallest correction
6. focused retest
7. complete eight-test suite

`/docs` is useful because FastAPI generates it from the same route definitions and models used by the server.

#### Checkpoint

Explain what automated tests prove that one screenshot does not, and what a `/docs` demonstration proves that source inspection alone does not.

#### If stuck

Do not change multiple files after one failing assertion. Locate the smallest owner using the failure-routing table in `validation-checklist.md`.

---

### Week 04 Beginner Readiness Gate

Before Section 12 or `build-task.md`, answer all items without copying:

- [ ] I can draw one request and one response.
- [ ] I can identify method, path, headers, body, status, and response body.
- [ ] I can explain why `/predict` uses POST.
- [ ] I can distinguish 400, 413, 422, 500, and 503.
- [ ] I can name the multipart field `image`.
- [ ] I can explain why MIME type and image decoding are separate checks.
- [ ] I can name and type all eight success fields.
- [ ] I can explain what FastAPI and Pydantic each contribute.
- [ ] I can trace bytes from upload to `(1, 224, 224, 3)`.
- [ ] I can explain why the Week 04 mock is not AI evidence.
- [ ] I can create, activate, and verify the virtual environment.
- [ ] I can explain the purpose of automated tests, `/docs`, logs, and cleanup.

If any item is unclear, repeat its bridge lesson and complete the matching exercise before looking at the complete implementation.

This learning sequence supports CSE 2206 client-server architecture, data interchange, validation, dependency isolation, asynchronous I/O, and component testing. It prepares the exact contract that Android consumes in Week 05.

### Required Input From Earlier Weeks

Week 04 does not restart the project. It assumes these outputs already exist and have been validated:

| Earlier week | Required output | Why Week 04 needs it |
|---:|---|---|
| 01 | Product purpose and main user journey | The API exists to support one known product flow. |
| 02 | Runnable Android navigation shell | The client side has a stable place for later networking work. |
| 03 | Camera/gallery image URI and preview | The learner understands the image that Week 05 will eventually upload. |

If the Week 03 validation checklist is incomplete, finish it first. Week 04 intentionally does not repair camera, gallery, URI, or Android layout problems.

### Exact Output Of This Week

At the end of Week 04, one independent server-side slice must be true:

```text
valid image bytes
    -> multipart field named image
    -> FastAPI validation and preprocessing
    -> explicit mock predictor
    -> eight-field PredictionResult JSON
```

The same route must also reject missing, empty, non-image, spoofed, and oversized input safely. Week 04 therefore validates both successful information flow and failure boundaries.

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

Week 03 has **no required backend code snapshot**. Its validated end state is the Android camera/gallery feature documented in the Week 03 notes. Therefore, the Week 04 backend files below are not expansions of Week 03 Python files; they are the first complete backend learning state.

For every Week 04 backend file, this inventory now records:

1. **Previous state:** absent from the Week 03 validated learning slice.
2. **Week 04 delta:** create the file, or verify the provided file against the contract.
3. **Complete end state:** the full checked-in file content in a fenced block.
4. **Exact size:** logical line count of the checked-in file.

If a learner starts from this repository, the files may already be present. In that case, “create” means reconstruct and understand the Week 04 state, not overwrite working code blindly. Copying every complete block below into the matching path recreates the required text-file snapshot; the Keras binary remains excluded because it belongs to Week 06.

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


#### 12.3.10 Complete end-of-Week-04 `main.py`

<!-- EXACT_WEEK04_FILE_START: backend-api/main.py -->
##### Complete file: `main.py`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/main.py`
**Exact logical size:** 216 lines

```python
import io
import logging
from typing import Dict

import numpy as np
from fastapi import FastAPI, File, HTTPException, UploadFile
from fastapi.middleware.cors import CORSMiddleware
from PIL import Image, UnidentifiedImageError
from pydantic import BaseModel

from config import (
    ALLOWED_ORIGINS,
    CONFIDENCE_THRESHOLD,
    IMAGE_SIZE,
    LABELS_PATH,
    MAX_IMAGE_SIZE_BYTES,
    MODEL_PATH,
    USE_MOCK,
)
from model_loader import load_predictor
from labels import display_label, load_labels

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("leafguard-api")

# Reviewed LeafGuard content remains limited to the original 10 supported classes.
DISEASE_INFO: Dict[str, Dict[str, str]] = {
    "Tomato___Early_blight": {
        "symptoms": "Small brown spots with concentric rings, yellowing around lesions, and damage starting on older leaves.",
        "treatment": "Remove infected leaves, improve airflow, mulch soil, and apply a labeled fungicide when pressure is high.",
        "prevention": "Rotate crops, avoid wetting foliage in the evening, and disinfect tools between plants."
    },
    "Tomato___Late_blight": {
        "symptoms": "Water-soaked patches that quickly darken, white fuzzy growth underneath leaves, and rapid whole-plant collapse.",
        "treatment": "Isolate infected plants, remove severely affected tissue, and apply an appropriate late blight fungicide immediately.",
        "prevention": "Use disease-free seedlings, space plants well, and avoid overhead irrigation during humid weather."
    },
    "Tomato___Healthy": {
        "symptoms": "Leaf remains green, evenly colored, and free from lesions, mold, curling, or necrotic patches.",
        "treatment": "No treatment needed. Continue normal watering, feeding, and routine scouting.",
        "prevention": "Maintain balanced nutrition, monitor weekly, and keep weeds and debris away from the crop."
    },
    "Potato___Late_blight": {
        "symptoms": "Dark blotches expand rapidly, stems blacken, and white mold may appear at lesion edges in humid conditions.",
        "treatment": "Remove badly infected foliage, avoid moving spores between rows, and apply a recommended protectant fungicide.",
        "prevention": "Plant resistant varieties where possible and destroy volunteer potatoes after harvest."
    },
    "Potato___Early_blight": {
        "symptoms": "Dark target-like rings on older leaves followed by yellowing and premature leaf drop.",
        "treatment": "Prune affected leaves, support plant vigor with correct fertilization, and treat with fungicide if needed.",
        "prevention": "Rotate away from solanaceous crops and water at soil level instead of soaking the canopy."
    },
    "Potato___Healthy": {
        "symptoms": "Leaves look firm, green, and free of spots, halos, wilting, or unusual discoloration.",
        "treatment": "No treatment required beyond standard crop care.",
        "prevention": "Keep monitoring field hygiene, irrigation balance, and nutrient supply."
    },
    "Corn___Cercospora_leaf_spot Gray_leaf_spot": {
        "symptoms": "Rectangular gray or tan lesions running parallel to leaf veins, usually beginning on lower leaves.",
        "treatment": "Scout regularly, remove heavily damaged leaves where practical, and apply fungicide based on local guidance.",
        "prevention": "Rotate fields, manage residue, and choose resistant hybrids when available."
    },
    "Corn___Northern_Leaf_Blight": {
        "symptoms": "Long cigar-shaped gray-green lesions that enlarge and reduce photosynthetic area.",
        "treatment": "Use a registered fungicide if disease pressure is high and preserve plant vigor with good agronomy.",
        "prevention": "Rotate crops, select resistant seed, and avoid continuous corn where blight is common."
    },
    "Corn___Healthy": {
        "symptoms": "Leaves are uniformly green with normal vein structure and no blight or spotting patterns.",
        "treatment": "No treatment required.",
        "prevention": "Continue regular monitoring, balanced fertilization, and integrated pest management."
    },
    "Apple___Apple_scab": {
        "symptoms": "Olive-brown velvety leaf lesions, fruit spotting, and leaf distortion in wet spring conditions.",
        "treatment": "Prune for airflow, remove fallen leaves, and apply protectant fungicides during susceptible growth stages.",
        "prevention": "Use resistant cultivars, sanitize orchard litter, and monitor wet periods carefully."
    }
}

CLASS_NAMES = load_labels(LABELS_PATH)
predictor = load_predictor(CLASS_NAMES)


class PredictionResult(BaseModel):
    model_label: str
    disease: str
    confidence: float
    uncertain: bool
    guidance_available: bool
    symptoms: str
    treatment: str
    prevention: str


app = FastAPI(
    title="LeafGuard AI Backend",
    description="FastAPI service for plant disease detection using a Keras model or a mock fallback.",
    version="1.0.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=ALLOWED_ORIGINS or ["*"],
    allow_credentials="*" not in ALLOWED_ORIGINS,
    allow_methods=["*"],
    allow_headers=["*"],
)


def preprocess_image(raw_bytes: bytes) -> np.ndarray:
    """Convert upload bytes into an RGB float32 tensor with values in [0, 255]."""
    try:
        image = Image.open(io.BytesIO(raw_bytes)).convert("RGB")
    except (UnidentifiedImageError, OSError) as exc:
        raise HTTPException(status_code=400, detail="Invalid image file supplied.") from exc

    resized_image = image.resize((IMAGE_SIZE, IMAGE_SIZE))
    # The approved model's embedded Rescaling layer converts raw [0, 255] RGB to [-1, 1].
    image_array = np.asarray(resized_image, dtype=np.float32)
    return np.expand_dims(image_array, axis=0)


@app.get("/")
@app.get("/health")
async def health_check() -> Dict[str, object]:
    """Simple health endpoint to verify the API is running and to expose runtime mode."""
    return {
        "status": "ok",
        "use_mock": USE_MOCK or predictor.use_mock,
        "model_loaded": predictor.model_loaded,
        "model_path": MODEL_PATH,
        "image_size": IMAGE_SIZE,
        "class_count": len(CLASS_NAMES),
        "labels_path": LABELS_PATH,
    }


@app.get("/diseases")
async def list_diseases() -> Dict[str, object]:
    """Expose every disease currently supported by the response dictionary."""
    return {
        "count": len(DISEASE_INFO),
        "diseases": [
            {
                "model_label": disease_name,
                "name": display_label(disease_name),
                **details,
            }
            for disease_name, details in DISEASE_INFO.items()
        ],
    }


@app.post("/predict", response_model=PredictionResult)
async def predict(image: UploadFile = File(...)) -> PredictionResult:
    """
    Receive a leaf image as multipart/form-data, preprocess it,
    run model inference, and return disease guidance in JSON form.
    """
    if not image.content_type or not image.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="Uploaded file must be an image.")

    try:
        raw_bytes = await image.read(MAX_IMAGE_SIZE_BYTES + 1)
        if not raw_bytes:
            raise HTTPException(status_code=400, detail="Uploaded image is empty.")
        if len(raw_bytes) > MAX_IMAGE_SIZE_BYTES:
            raise HTTPException(
                status_code=413,
                detail=f"Image exceeds the {MAX_IMAGE_SIZE_BYTES // (1024 * 1024)} MB upload limit.",
            )

        model_input = preprocess_image(raw_bytes)
        if not predictor.model_loaded and not predictor.use_mock:
            raise HTTPException(
                status_code=503,
                detail="Real model is not loaded. Check /health and server logs.",
            )

        model_label, confidence = predictor.predict(model_input)
        guidance_available = model_label in DISEASE_INFO
        metadata = DISEASE_INFO.get(
            model_label,
            {
                "symptoms": "Detailed symptoms and treatment guidance are not available in this version.",
                "treatment": "Please verify this result with a local agricultural expert or plant-disease reference.",
                "prevention": "Capture a clear close-up and continue monitoring. This result is not a confirmed diagnosis.",
            },
        )

        uncertain = confidence < CONFIDENCE_THRESHOLD
        if uncertain:
            logger.info(
                "Low-confidence prediction returned: %s (%.2f < %.2f)",
                model_label,
                confidence,
                CONFIDENCE_THRESHOLD,
            )

        return PredictionResult(
            model_label=model_label,
            disease=display_label(model_label),
            confidence=round(float(confidence), 4),
            uncertain=uncertain,
            guidance_available=guidance_available,
            symptoms=metadata["symptoms"],
            treatment=metadata["treatment"],
            prevention=metadata["prevention"],
        )
    except HTTPException:
        raise
    except Exception as exc:  # pragma: no cover - runtime guard
        logger.exception("Prediction failed: %s", exc)
        raise HTTPException(status_code=500, detail="Model prediction failed.") from exc
    finally:
        await image.close()
```
<!-- EXACT_WEEK04_FILE_END: backend-api/main.py -->

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


#### 12.4.1 Complete end-of-Week-04 `config.py`

<!-- EXACT_WEEK04_FILE_START: backend-api/config.py -->
##### Complete file: `config.py`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/config.py`
**Exact logical size:** 16 lines

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
<!-- EXACT_WEEK04_FILE_END: backend-api/config.py -->

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


#### 12.5.4 Complete end-of-Week-04 `model_loader.py`

<!-- EXACT_WEEK04_FILE_START: backend-api/model_loader.py -->
##### Complete file: `model_loader.py`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/model_loader.py`
**Exact logical size:** 87 lines

```python
import logging
from pathlib import Path
from typing import List, Sequence, Tuple

import numpy as np

from config import IMAGE_SIZE, MODEL_PATH, USE_MOCK

logger = logging.getLogger(__name__)

try:
    import tensorflow as tf
except Exception as exc:  # pragma: no cover - depends on environment
    tf = None
    logger.warning("TensorFlow import failed, mock predictor will be used: %s", exc)


class ModelPredictor:
    """Wrapper that exposes a single predict method for real or mock inference."""

    def __init__(self, class_names: Sequence[str], model=None, use_mock: bool = False):
        self.class_names: List[str] = list(class_names)
        self.model = model
        self.use_mock = use_mock
        self.model_loaded = model is not None

    def predict(self, image_batch: np.ndarray) -> Tuple[str, float]:
        if self.use_mock:
            return self._mock_predict(image_batch)
        if self.model is None:
            raise RuntimeError("Real model is unavailable. Check /health and server logs.")

        predictions = self.model.predict(image_batch, verbose=0)
        predictions = np.asarray(predictions, dtype=np.float32)
        if predictions.ndim == 1:
            predictions = np.expand_dims(predictions, axis=0)

        scores = predictions[0]
        best_index = int(np.argmax(scores))
        confidence = max(0.0, min(1.0, float(scores[best_index])))
        disease_name = self.class_names[best_index] if best_index < len(self.class_names) else f"Class {best_index}"
        return disease_name, confidence

    def _mock_predict(self, image_batch: np.ndarray) -> Tuple[str, float]:
        if not self.class_names:
            return "Unknown disease", 0.50

        mean_intensity = float(np.mean(image_batch))
        scaled_index = int(round(mean_intensity * (len(self.class_names) - 1)))
        best_index = max(0, min(len(self.class_names) - 1, scaled_index))
        confidence = round(0.70 + ((best_index % 3) * 0.08), 2)
        return self.class_names[best_index], min(confidence, 0.99)


def load_predictor(class_names: Sequence[str]) -> ModelPredictor:
    model_path = Path(MODEL_PATH)

    if USE_MOCK:
        logger.info("USE_MOCK enabled. Skipping model load and using mock predictor.")
        return ModelPredictor(class_names=class_names, use_mock=True)

    if tf is None:
        logger.error("TensorFlow is unavailable. Real inference is disabled.")
        return ModelPredictor(class_names=class_names)

    if not model_path.exists():
        logger.error("Model file not found at %s. Real inference is disabled.", model_path)
        return ModelPredictor(class_names=class_names)

    try:
        model = tf.keras.models.load_model(model_path)
        input_shape = tuple(model.input_shape)
        output_shape = tuple(model.output_shape)
        if len(input_shape) != 4 or input_shape[1:] != (IMAGE_SIZE, IMAGE_SIZE, 3):
            raise ValueError(
                f"Expected model input shape (None, {IMAGE_SIZE}, {IMAGE_SIZE}, 3), "
                f"got {input_shape}"
            )
        if len(output_shape) != 2 or output_shape[-1] != len(class_names):
            raise ValueError(
                f"Model output count {output_shape[-1]} does not match label count {len(class_names)}"
            )
        logger.info("Loaded Keras model from %s", model_path)
        return ModelPredictor(class_names=class_names, model=model)
    except Exception:
        logger.exception("Failed to load or validate model from %s. Real inference is disabled.", model_path)
        return ModelPredictor(class_names=class_names)
```
<!-- EXACT_WEEK04_FILE_END: backend-api/model_loader.py -->

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


#### 12.6.1 Complete end-of-Week-04 label files

<!-- EXACT_WEEK04_FILE_START: backend-api/labels.py -->
##### Complete file: `labels.py`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/labels.py`
**Exact logical size:** 37 lines

```python
from pathlib import Path
from typing import List


def load_labels(path: str) -> List[str]:
    label_path = Path(path)
    if not label_path.is_file():
        raise RuntimeError(f"Canonical labels file not found: {label_path}")

    labels = [
        line.strip()
        for line in label_path.read_text(encoding="utf-8").splitlines()
        if line.strip() and not line.lstrip().startswith("#")
    ]
    if not labels:
        raise RuntimeError(f"Canonical labels file is empty: {label_path}")
    if len(labels) != len(set(labels)):
        raise RuntimeError(f"Canonical labels file contains duplicates: {label_path}")
    return labels


def display_label(model_label: str) -> str:
    overrides = {
        "Apple___Apple_scab": "Apple Scab",
        "Corn___Cercospora_leaf_spot Gray_leaf_spot": "Corn Gray Leaf Spot",
        "Corn___Northern_Leaf_Blight": "Corn Northern Leaf Blight",
        "Potato___Early_blight": "Potato Early Blight",
        "Potato___Late_blight": "Potato Late Blight",
        "Tomato___Early_blight": "Tomato Early Blight",
        "Tomato___Late_blight": "Tomato Late Blight",
    }
    if model_label in overrides:
        return overrides[model_label]
    crop, separator, condition = model_label.partition("___")
    if not separator:
        return model_label.replace("_", " ")
    return f"{crop.replace('_', ' ')} {condition.replace('_', ' ')}"
```
<!-- EXACT_WEEK04_FILE_END: backend-api/labels.py -->

<!-- EXACT_WEEK04_FILE_START: backend-api/labels-38.txt -->
##### Complete file: `labels-38.txt`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/labels-38.txt`
**Exact logical size:** 38 lines

```text
Apple___Apple_scab
Apple___Black_rot
Apple___Cedar_apple_rust
Apple___Healthy
Blueberry___Healthy
Cherry___Powdery_mildew
Cherry___Healthy
Corn___Cercospora_leaf_spot Gray_leaf_spot
Corn___Common_rust
Corn___Northern_Leaf_Blight
Corn___Healthy
Grape___Black_rot
Grape___Esca_(Black_Measles)
Grape___Leaf_blight_(Isariopsis_Leaf_Spot)
Grape___Healthy
Orange___Haunglongbing_(Citrus_greening)
Peach___Bacterial_spot
Peach___Healthy
Pepper,_bell___Bacterial_spot
Pepper,_bell___Healthy
Potato___Early_blight
Potato___Late_blight
Potato___Healthy
Raspberry___Healthy
Soybean___Healthy
Squash___Powdery_mildew
Strawberry___Leaf_scorch
Strawberry___Healthy
Tomato___Bacterial_spot
Tomato___Early_blight
Tomato___Late_blight
Tomato___Leaf_Mold
Tomato___Septoria_leaf_spot
Tomato___Spider_mites Two-spotted_spider_mite
Tomato___Target_Spot
Tomato___Tomato_Yellow_Leaf_Curl_Virus
Tomato___Tomato_mosaic_virus
Tomato___Healthy
```
<!-- EXACT_WEEK04_FILE_END: backend-api/labels-38.txt -->

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


#### 12.7.1 Complete end-of-Week-04 dependency manifests

<!-- EXACT_WEEK04_FILE_START: backend-api/requirements-base.txt -->
##### Complete file: `requirements-base.txt`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/requirements-base.txt`
**Exact logical size:** 6 lines

```text
fastapi==0.109.0
uvicorn[standard]==0.27.0
python-multipart==0.0.6
pillow==10.2.0
numpy==1.26.3
python-dotenv==1.0.0
```
<!-- EXACT_WEEK04_FILE_END: backend-api/requirements-base.txt -->

<!-- EXACT_WEEK04_FILE_START: backend-api/requirements-dev.txt -->
##### Complete file: `requirements-dev.txt`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/requirements-dev.txt`
**Exact logical size:** 2 lines

```text
-r requirements-base.txt
httpx==0.26.0
```
<!-- EXACT_WEEK04_FILE_END: backend-api/requirements-dev.txt -->

<!-- EXACT_WEEK04_FILE_START: backend-api/requirements.txt -->
##### Complete file: `requirements.txt`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/requirements.txt`
**Exact logical size:** 2 lines

```text
-r requirements-base.txt
tensorflow==2.19.1
```
<!-- EXACT_WEEK04_FILE_END: backend-api/requirements.txt -->

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


#### 12.8.3 Complete end-of-Week-04 `test_api.py`

<!-- EXACT_WEEK04_FILE_START: backend-api/test_api.py -->
##### Complete file: `test_api.py`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/test_api.py`
**Exact logical size:** 86 lines

```python
import io
import unittest
from unittest.mock import patch

from fastapi.testclient import TestClient
from PIL import Image

import main


class LeafGuardApiTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.client = TestClient(main.app)

    @staticmethod
    def make_png() -> bytes:
        output = io.BytesIO()
        Image.new("RGB", (32, 32), color=(30, 180, 60)).save(output, format="PNG")
        return output.getvalue()

    def test_health_aliases_report_runtime_mode(self):
        for path in ("/", "/health"):
            response = self.client.get(path)
            self.assertEqual(200, response.status_code)
            self.assertEqual("ok", response.json()["status"])
            self.assertEqual(38, response.json()["class_count"])

    def test_disease_library_keeps_ten_reviewed_entries(self):
        response = self.client.get("/diseases")
        self.assertEqual(200, response.status_code)
        self.assertEqual(10, response.json()["count"])
        self.assertEqual(10, len(response.json()["diseases"]))

    def test_predict_accepts_valid_image(self):
        with patch.object(main.predictor, "use_mock", True):
            response = self.client.post(
                "/predict",
                files={"image": ("leaf.png", self.make_png(), "image/png")},
            )
        self.assertEqual(200, response.status_code)
        payload = response.json()
        self.assertIn(payload["model_label"], main.CLASS_NAMES)
        self.assertGreaterEqual(payload["confidence"], 0.0)
        self.assertLessEqual(payload["confidence"], 1.0)

    def test_predict_returns_503_without_real_model(self):
        if main.predictor.model_loaded:
            self.skipTest("Real model is available in this environment")
        with patch.object(main.predictor, "use_mock", False):
            response = self.client.post(
                "/predict",
                files={"image": ("leaf.png", self.make_png(), "image/png")},
            )
        self.assertEqual(503, response.status_code)

    def test_preprocessing_keeps_raw_rgb_values(self):
        tensor = main.preprocess_image(self.make_png())
        self.assertEqual((1, 224, 224, 3), tensor.shape)
        self.assertEqual(180.0, float(tensor[0, 0, 0, 1]))

    def test_predict_rejects_non_image(self):
        response = self.client.post(
            "/predict",
            files={"image": ("notes.txt", b"not an image", "text/plain")},
        )
        self.assertEqual(400, response.status_code)

    def test_predict_rejects_spoofed_image(self):
        response = self.client.post(
            "/predict",
            files={"image": ("fake.png", b"not an image", "image/png")},
        )
        self.assertEqual(400, response.status_code)

    def test_predict_rejects_oversized_upload(self):
        with patch.object(main, "MAX_IMAGE_SIZE_BYTES", 16):
            response = self.client.post(
                "/predict",
                files={"image": ("large.png", b"x" * 17, "image/png")},
            )
        self.assertEqual(413, response.status_code)


if __name__ == "__main__":
    unittest.main()
```
<!-- EXACT_WEEK04_FILE_END: backend-api/test_api.py -->

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


#### 12.9.1 Complete end-of-Week-04 `README.md`

<!-- EXACT_WEEK04_FILE_START: backend-api/README.md -->
##### Complete file: `README.md`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/README.md`
**Exact logical size:** 920 lines

````markdown
# LeafGuard AI - Backend API Setup Guide

## Overview
This guide provides complete instructions for setting up and running the FastAPI backend server for LeafGuard AI. The backend provides REST API endpoints for plant disease detection using a machine learning model, serving as an alternative or complement to on-device inference.

> **✅ The real-model integration ships in this folder** (`main.py`, `config.py`,
> `model_loader.py`, `requirements.txt`). Follow the [Quick Start](#quick-start) below.
> Real prediction requires the manually approved Keras artifact. If it is absent or
> invalid, health remains available but prediction returns HTTP 503. Mock predictions are
> enabled only when `USE_MOCK=true`; they must never be used as real inference.
> The "Implementation Guide" sections further down are a learning walkthrough that
> shows *how* such a server is built — treat them as study material, not setup steps.
> For the full app + backend walkthrough, see
> [`docs/complete-setup-and-run-guide.md`](../docs/complete-setup-and-run-guide.md).

## Prerequisites

- **Python**: Version 3.8 - 3.11 (recommended: 3.10)
- **pip**: Python package manager (comes with Python)
- **Git**: For cloning dependencies
- **8GB+ RAM**: For running ML models
- **CUDA** (optional): For GPU acceleration with TensorFlow/PyTorch

## Python Version Check

```bash
# Check Python version
python --version
# or
python3 --version

# Should output: Python 3.8.x or higher
```

## Project Structure (actual files in this folder)

```
backend-api/
├── main.py                      # FastAPI application: /, /diseases, /predict endpoints + disease knowledge base
├── config.py                    # Configuration via environment variables / optional .env file
├── model_loader.py              # Loads the Keras model, or falls back to a mock predictor
├── requirements-base.txt        # API + mock-mode dependencies (Python 3.12 supported)
├── requirements.txt             # Base dependencies + real-model TensorFlow runtime
├── requirements-dev.txt         # Base dependencies + API test client
├── Dockerfile                   # Container deployment definition
├── test_api.py                  # Automated API contract and upload tests
├── .env                         # OPTIONAL — override configuration defaults (create only if needed)
├── models/                      # Place the approved model here
│   └── leafguard_model.keras    #   default MODEL_PATH; absent = prediction HTTP 503
└── README.md                    # This file
```

## Quick Start

### 1. Create Virtual Environment

**Linux/Mac**:
```bash
# Navigate to backend-api directory
cd backend-api

# Create virtual environment
python3 -m venv venv

# Activate virtual environment
source venv/bin/activate
```

**Windows**:
```bash
# Navigate to backend-api directory
cd backend-api

# Create virtual environment
python -m venv venv

# Activate virtual environment
venv\Scripts\activate
```

You should see `(venv)` prefix in your terminal.

### 2. Install Dependencies

```bash
# Upgrade pip
pip install --upgrade pip

# API/mock development mode (works on Python 3.12+)
pip install -r requirements-base.txt

# Real Keras model mode (verified with Python 3.11)
pip install -r requirements.txt
```

### 3. About the requirements files

The files are **already provided**:

```txt
requirements-base.txt  # runnable API with mock fallback
requirements.txt       # base dependencies plus TensorFlow 2.19.1 / Keras 3 support
requirements-dev.txt   # base dependencies plus API test tooling
```

> **Note**: The approved model was saved by Keras 3.10.0 and cannot be loaded by
> `tensorflow==2.14.0`. The real-model workflow is verified with Python 3.11 and the
> pinned `tensorflow==2.19.1`. If TensorFlow fails to install, use `requirements-base.txt`.
> Missing TensorFlow disables real inference. Mock mode must be explicitly enabled with
> `USE_MOCK=true`.

### 4. Place the Approved Trained ML Model

The backend loads a **Keras** model (not `.tflite` — that format is for the Android app's
on-device inference). If you have a trained model:

```bash
# Create the models directory
mkdir -p models

# Place your trained Keras model at the default path:
#   models/leafguard_model.keras
```

If the file is absent, the server logs an error and `/predict` returns HTTP 503.
The model output must match the 38 labels in [`../model/labels-38.txt`](../model/labels-38.txt).
Follow [`../docs/production-end-to-end-setup.md`](../docs/production-end-to-end-setup.md)
for acquisition, conversion, parity testing, Android setup, signing, and release steps.

### 5. (Optional) Create Environment Variables

All settings have working defaults (see `config.py`). To override them, create a `.env`
file in `backend-api/`:

```bash
# .env file — every line is optional
MODEL_PATH=models/leafguard_model.keras   # path to the trained Keras model
IMAGE_SIZE=224                            # input resolution expected by the model
CONFIDENCE_THRESHOLD=0.50                 # below this, a low-confidence log line is emitted
USE_MOCK=false                            # true = force mock predictions even if a model exists
MAX_IMAGE_SIZE_BYTES=10485760             # maximum accepted upload size (10 MiB)
PORT=8000                                 # informational; pass --port to uvicorn to change it
ALLOWED_ORIGINS=*                         # comma-separated CORS origins
```

**Important**: Add `.env` to `.gitignore` to avoid committing sensitive data:
```bash
echo ".env" >> .gitignore
```

## Implementation Guide

> **📚 Learning material — not setup steps.** Everything below shows how a server like
> this is designed. The real, already-working code lives in `main.py`, `config.py`, and
> `model_loader.py` in this folder, and its details differ slightly from these teaching
> sketches (real endpoints: `GET /`, `GET /health`, `GET /diseases`, `POST /predict` with form field
> `image`).

### Minimal `main.py` Structure

```python
from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from PIL import Image
import numpy as np
import tensorflow as tf
import io
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize FastAPI app
app = FastAPI(
    title="LeafGuard AI API",
    description="Plant Disease Detection API",
    version="1.0.0"
)

# CORS Configuration
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # In production, specify actual origins
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Global variable to store loaded model
model = None
labels = []

@app.on_event("startup")
async def load_model():
    """Load TensorFlow Lite model on startup"""
    global model, labels
    try:
        # Load TFLite model
        model = tf.lite.Interpreter(model_path="models/saved_model/model.tflite")
        model.allocate_tensors()

        # Load labels
        with open("models/saved_model/labels.txt", "r") as f:
            labels = [line.strip() for line in f.readlines()]

        logger.info("Model loaded successfully")
    except Exception as e:
        logger.error(f"Failed to load model: {e}")
        raise

@app.get("/")
async def root():
    """Root endpoint"""
    return {
        "message": "LeafGuard AI API",
        "version": "1.0.0",
        "status": "running"
    }

@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "model_loaded": model is not None
    }

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    """
    Predict plant disease from uploaded image

    Args:
        file: Image file (JPEG, PNG)

    Returns:
        JSON with prediction results
    """
    if model is None:
        raise HTTPException(status_code=503, detail="Model not loaded")

    # Validate file type
    if file.content_type not in ["image/jpeg", "image/png", "image/jpg"]:
        raise HTTPException(status_code=400, detail="Invalid file type. Use JPEG or PNG")

    try:
        # Read image
        contents = await file.read()
        image = Image.open(io.BytesIO(contents))

        # Preprocess image
        image = image.convert("RGB")
        image = image.resize((224, 224))  # Adjust size based on your model
        image_array = np.array(image, dtype=np.float32)
        image_array = image_array / 255.0  # Normalize to [0, 1]
        image_array = np.expand_dims(image_array, axis=0)  # Add batch dimension

        # Get input and output details
        input_details = model.get_input_details()
        output_details = model.get_output_details()

        # Set input tensor
        model.set_tensor(input_details[0]['index'], image_array)

        # Run inference
        model.invoke()

        # Get output tensor
        output_data = model.get_tensor(output_details[0]['index'])
        predictions = output_data[0]

        # Get top prediction
        top_index = np.argmax(predictions)
        confidence = float(predictions[top_index])
        disease = labels[top_index]

        # Get top 3 predictions
        top_3_indices = np.argsort(predictions)[-3:][::-1]
        top_3_predictions = [
            {
                "disease": labels[i],
                "confidence": float(predictions[i])
            }
            for i in top_3_indices
        ]

        return {
            "success": True,
            "prediction": {
                "disease": disease,
                "confidence": confidence
            },
            "top_3": top_3_predictions
        }

    except Exception as e:
        logger.error(f"Prediction error: {e}")
        raise HTTPException(status_code=500, detail=f"Prediction failed: {str(e)}")

@app.get("/model/info")
async def model_info():
    """Get model information"""
    if model is None:
        raise HTTPException(status_code=503, detail="Model not loaded")

    input_details = model.get_input_details()
    output_details = model.get_output_details()

    return {
        "model_type": "TensorFlow Lite",
        "input_shape": input_details[0]['shape'].tolist(),
        "output_shape": output_details[0]['shape'].tolist(),
        "num_classes": len(labels),
        "labels": labels
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True
    )
```

### `models/model_loader.py` Structure

```python
import tensorflow as tf
import logging
from pathlib import Path

logger = logging.getLogger(__name__)

class ModelLoader:
    def __init__(self, model_path: str, labels_path: str):
        self.model_path = model_path
        self.labels_path = labels_path
        self.interpreter = None
        self.labels = []

    def load(self):
        """Load TFLite model and labels"""
        try:
            # Check if files exist
            if not Path(self.model_path).exists():
                raise FileNotFoundError(f"Model file not found: {self.model_path}")

            if not Path(self.labels_path).exists():
                raise FileNotFoundError(f"Labels file not found: {self.labels_path}")

            # Load model
            self.interpreter = tf.lite.Interpreter(model_path=self.model_path)
            self.interpreter.allocate_tensors()

            # Load labels
            with open(self.labels_path, 'r') as f:
                self.labels = [line.strip() for line in f.readlines()]

            logger.info(f"Model loaded: {len(self.labels)} classes")
            return True

        except Exception as e:
            logger.error(f"Failed to load model: {e}")
            return False

    def get_input_details(self):
        """Get model input details"""
        return self.interpreter.get_input_details()

    def get_output_details(self):
        """Get model output details"""
        return self.interpreter.get_output_details()
```

### `utils/image_processing.py` Structure

```python
from PIL import Image
import numpy as np
from typing import Tuple

def preprocess_image(
    image: Image.Image,
    target_size: Tuple[int, int] = (224, 224),
    normalize: bool = True
) -> np.ndarray:
    """
    Preprocess image for model inference

    Args:
        image: PIL Image
        target_size: Target size (width, height)
        normalize: Whether to normalize to [0, 1]

    Returns:
        Preprocessed numpy array
    """
    # Convert to RGB
    image = image.convert('RGB')

    # Resize
    image = image.resize(target_size)

    # Convert to numpy array
    image_array = np.array(image, dtype=np.float32)

    # Normalize
    if normalize:
        image_array = image_array / 255.0

    # Add batch dimension
    image_array = np.expand_dims(image_array, axis=0)

    return image_array

def validate_image(image: Image.Image, max_size: int = 10485760) -> bool:
    """
    Validate image

    Args:
        image: PIL Image
        max_size: Maximum file size in bytes

    Returns:
        True if valid
    """
    # Check dimensions
    width, height = image.size
    if width < 50 or height < 50:
        return False

    if width > 4096 or height > 4096:
        return False

    return True
```

## Running the Server

### Development Mode

```bash
# Activate virtual environment
source venv/bin/activate  # Linux/Mac
# or
venv\Scripts\activate  # Windows

# Run with auto-reload
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

### Production Mode

```bash
# Run with multiple workers
uvicorn main:app --host 0.0.0.0 --port 8000 --workers 4
```

### Access API Documentation

Once the server is running:
- **Interactive API Docs (Swagger)**: http://localhost:8000/docs
- **Alternative API Docs (ReDoc)**: http://localhost:8000/redoc
- **OpenAPI JSON**: http://localhost:8000/openapi.json

## Testing the API

The real endpoints are `GET /` and `GET /health` (health aliases), `GET /diseases`, and `POST /predict` with a
multipart form field named **`image`**.

### Using cURL

```bash
# Health check (also reports whether a real model or the mock predictor is active)
curl http://localhost:8000/

# Equivalent deployment health-check alias
curl http://localhost:8000/health

# Predict from image (note the field name: image)
curl -X POST "http://localhost:8000/predict" \
  -F "image=@/path/to/plant-image.jpg"

# List every disease in the knowledge base
curl http://localhost:8000/diseases
```

### Using Python Requests

```python
import requests

# Test prediction
url = "http://localhost:8000/predict"
files = {"image": open("test_image.jpg", "rb")}
response = requests.post(url, files=files)
print(response.json())
```

### Using Postman

1. Create new POST request
2. URL: `http://localhost:8000/predict`
3. Body → form-data
4. Key: `image` (type: File)
5. Value: Select image file
6. Send

## Connecting Android App to Backend

### Same WiFi Network Setup

1. **Find your computer's IP address**:

   **Linux/Mac**:
   ```bash
   ifconfig | grep "inet " | grep -v 127.0.0.1
   # or
   ip addr show | grep "inet " | grep -v 127.0.0.1
   ```

   **Windows**:
   ```bash
   ipconfig
   ```

   Look for IPv4 address (e.g., 192.168.1.105)

2. **Start server on all interfaces**:
   ```bash
   uvicorn main:app --host 0.0.0.0 --port 8000
   ```

3. **Update the Android app** to use your computer's IP — no code change needed:
   open the app's **Settings** screen and set the Backend API URL to
   `http://192.168.1.105:8000/` (the default is `http://10.0.2.2:8000/`, which only
   works from the emulator). The default lives in
   `network/RetrofitClient.kt` / `RetrofitClient.java` if you want to change it permanently.

4. **Test connection from phone**:
   - Open browser on phone
   - Navigate to: `http://192.168.1.105:8000/`
   - Should see: `{"status":"ok","use_mock":...,"model_loaded":...}`

### Using Android Emulator

The Android emulator cannot access `localhost` or `127.0.0.1` directly.

**Use special alias**: `10.0.2.2`

```java
// In Android app — this is already the default in RetrofitClient
private static final String BASE_URL = "http://10.0.2.2:8000/";
```

This routes to your host machine's localhost. The app ships with this default, so the
emulator + local backend combination works with zero configuration.

### Firewall Configuration

If connection fails, allow port 8000:

**Linux (UFW)**:
```bash
sudo ufw allow 8000
```

**Windows Firewall**:
- Windows Security → Firewall → Advanced Settings
- Inbound Rules → New Rule
- Port → TCP → 8000 → Allow

**Mac**:
- System Preferences → Security & Privacy → Firewall
- Firewall Options → Add application (Python)

## CORS Configuration

For production, specify exact origins instead of `"*"`:

```python
from fastapi.middleware.cors import CORSMiddleware

app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "http://localhost:3000",
        "https://yourdomain.com"
    ],
    allow_credentials=True,
    allow_methods=["GET", "POST"],
    allow_headers=["*"],
)
```

## Environment Configuration

### Using `pydantic-settings` for Config Management

Create `config/settings.py`:

```python
from pydantic_settings import BaseSettings
from typing import List

class Settings(BaseSettings):
    MODEL_PATH: str = "models/saved_model/model.tflite"
    LABELS_PATH: str = "models/saved_model/labels.txt"
    HOST: str = "0.0.0.0"
    PORT: int = 8000
    RELOAD: bool = False
    LOG_LEVEL: str = "info"
    MAX_IMAGE_SIZE: int = 10485760  # 10MB
    ALLOWED_ORIGINS: List[str] = ["*"]

    class Config:
        env_file = ".env"
        case_sensitive = True

settings = Settings()
```

Use in `main.py`:
```python
from config.settings import settings

model = tf.lite.Interpreter(model_path=settings.MODEL_PATH)
```

## Deployment Considerations

### Using Gunicorn (Production WSGI Server)

```bash
# Install gunicorn
pip install gunicorn

# Run with Gunicorn
gunicorn main:app -w 4 -k uvicorn.workers.UvicornWorker --bind 0.0.0.0:8000
```

### Docker Deployment

Create `Dockerfile`:

```dockerfile
FROM python:3.10-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8000

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
```

Build and run:
```bash
docker build -t leafguard-api .
docker run -p 8000:8000 leafguard-api
```

### Cloud Deployment Options

1. **Heroku**: Simple deployment with Procfile
2. **AWS EC2**: Full control, requires setup
3. **Google Cloud Run**: Serverless containers
4. **Azure App Service**: Managed platform
5. **Railway.app**: Easy deployment with GitHub integration

## Common Issues and Solutions

### 1. Port Already in Use

**Error**: `Address already in use`

**Solution**:
```bash
# Find process using port 8000
lsof -i :8000  # Linux/Mac
netstat -ano | findstr :8000  # Windows

# Kill process
kill -9 <PID>  # Linux/Mac
taskkill /PID <PID> /F  # Windows

# Or use different port
uvicorn main:app --port 8001
```

### 2. Module Not Found

**Error**: `ModuleNotFoundError: No module named 'fastapi'`

**Solution**:
```bash
# Ensure virtual environment is activated
source venv/bin/activate

# Reinstall dependencies
pip install -r requirements.txt
```

### 3. Model File Not Found

**Error**: `FileNotFoundError: model.tflite`

**Solution**:
- Check file path in code matches actual location
- Use absolute paths or ensure working directory is correct
- Verify file exists: `ls models/saved_model/model.tflite`

### 4. TensorFlow Not Installing

**Error**: TensorFlow installation fails

**Solution**:
```bash
# For Mac M1/M2 (Apple Silicon)
pip install tensorflow-macos
pip install tensorflow-metal

# Use the repository's Keras 3-compatible pin
pip install -r requirements.txt

# For CPU-only
pip install tensorflow-cpu
```

### 5. CORS Error from Android App

**Error**: Blocked by CORS policy

**Solution**:
- Verify CORS middleware is added
- Check `allow_origins` includes your client origin
- For development, use `allow_origins=["*"]`

### 6. Large Image Upload Fails

**Error**: Request entity too large

**Solution**:
```python
# Add size limit to FastAPI
from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse

@app.exception_handler(RequestEntityTooLarge)
async def request_entity_too_large_handler(request, exc):
    return JSONResponse(
        status_code=413,
        content={"error": "Image too large. Max size: 10MB"}
    )

# Limit in upload endpoint
@app.post("/predict")
async def predict(file: UploadFile = File(..., max_length=10485760)):
    ...
```

### 7. Slow Inference

**Issue**: Model takes too long to respond

**Solutions**:
- Use GPU acceleration (TensorFlow GPU)
- Resize images before sending to API
- Use model quantization (reduces size/latency)
- Implement caching for repeated requests
- Consider using ONNX Runtime for faster inference

## Performance Optimization

### 1. Model Loading

Load model once at startup, not per request (already in example above).

### 2. Image Caching

```python
from functools import lru_cache

@lru_cache(maxsize=100)
def get_cached_prediction(image_hash: str):
    # Return cached result
    pass
```

### 3. Async Processing

```python
import asyncio
from concurrent.futures import ThreadPoolExecutor

executor = ThreadPoolExecutor(max_workers=4)

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    loop = asyncio.get_event_loop()
    result = await loop.run_in_executor(executor, run_inference, image)
    return result
```

### 4. Request Validation

```python
from pydantic import BaseModel, validator

class PredictionResponse(BaseModel):
    success: bool
    prediction: dict

    @validator('prediction')
    def validate_prediction(cls, v):
        required_keys = ['disease', 'confidence']
        if not all(key in v for key in required_keys):
            raise ValueError('Invalid prediction format')
        return v
```

## Testing

### Unit Tests with Pytest

Create `tests/test_api.py` (endpoints below match the real server in this folder):

```python
from fastapi.testclient import TestClient
from main import app

client = TestClient(app)

def test_health_check():
    response = client.get("/")
    assert response.status_code == 200
    assert response.json()["status"] == "ok"

def test_diseases_list():
    response = client.get("/diseases")
    assert response.status_code == 200
    assert response.json()["count"] == 10

def test_predict_with_valid_image():
    with open("test_image.jpg", "rb") as f:
        response = client.post(
            "/predict",
            files={"image": ("test.jpg", f, "image/jpeg")}
        )
    assert response.status_code == 200
    assert "disease" in response.json()
```

Run tests:
```bash
pytest tests/ -v
```

## Logging

### Configure Logging

```python
import logging
from datetime import datetime

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler(f'logs/api_{datetime.now().strftime("%Y%m%d")}.log'),
        logging.StreamHandler()
    ]
)

logger = logging.getLogger(__name__)

# Use in code
logger.info("Processing image")
logger.error("Failed to load model", exc_info=True)
```

## Monitoring

### Basic Request Logging

```python
import time
from fastapi import Request

@app.middleware("http")
async def log_requests(request: Request, call_next):
    start_time = time.time()
    response = await call_next(request)
    process_time = time.time() - start_time
    logger.info(f"{request.method} {request.url.path} - {response.status_code} - {process_time:.2f}s")
    return response
```

## Next Steps

1. Implement the basic `main.py` with health check and predict endpoints
2. Test locally with sample images
3. Connect Android app to backend
4. Add error handling and validation
5. Implement logging and monitoring
6. Optimize for performance
7. Consider deployment options

---

**Note**: This backend is optional for the LeafGuard AI project. Students can implement on-device inference first (Weeks 5-7) and add backend integration later (Weeks 9-10).
````
<!-- EXACT_WEEK04_FILE_END: backend-api/README.md -->

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


#### 12.10.1 Complete end-of-Week-04 support files

<!-- EXACT_WEEK04_FILE_START: backend-api/.env.example -->
##### Complete file: `.env.example`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/.env.example`
**Exact logical size:** 7 lines

```dotenv
MODEL_PATH=models/leafguard_model.keras
IMAGE_SIZE=224
CONFIDENCE_THRESHOLD=0.50
USE_MOCK=false
MAX_IMAGE_SIZE_BYTES=10485760
PORT=8000
ALLOWED_ORIGINS=*
```
<!-- EXACT_WEEK04_FILE_END: backend-api/.env.example -->

<!-- EXACT_WEEK04_FILE_START: backend-api/.dockerignore -->
##### Complete file: `.dockerignore`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/.dockerignore`
**Exact logical size:** 7 lines

```text
__pycache__/
*.py[cod]
.env
.venv/
venv/
.pytest_cache/
models/*.keras
```
<!-- EXACT_WEEK04_FILE_END: backend-api/.dockerignore -->

<!-- EXACT_WEEK04_FILE_START: backend-api/Dockerfile -->
##### Complete file: `Dockerfile`

**Previous Week 03 state:** This backend file is not part of the validated Week 03 Android slice.

**Week 04 delta:** Create this file, or verify the provided copy exactly before running the Week 04 checks.

**Final path:** `backend-api/Dockerfile`
**Exact logical size:** 23 lines

```dockerfile
FROM python:3.11-slim

ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1 \
    PORT=8000

WORKDIR /app

ARG INSTALL_TENSORFLOW=false

COPY requirements-base.txt requirements.txt ./
RUN pip install --no-cache-dir --upgrade pip \
    && pip install --no-cache-dir -r requirements-base.txt \
    && if [ "$INSTALL_TENSORFLOW" = "true" ]; then pip install --no-cache-dir -r requirements.txt; fi

COPY . .

EXPOSE 8000

HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
    CMD python -c "import os, urllib.request; urllib.request.urlopen('http://127.0.0.1:' + os.environ.get('PORT', '8000') + '/health')"

CMD ["sh", "-c", "exec uvicorn main:app --host 0.0.0.0 --port \"${PORT}\""]
```
<!-- EXACT_WEEK04_FILE_END: backend-api/Dockerfile -->

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

## 13. From Learning Note to Verified Evidence

Every major idea in this file is used again. This prevents the seven Week 04 files from becoming disconnected reading material.

| Concept learned here | Practised in | Built or inspected in | Proved by |
|---|---|---|---|
| Week 03/04 client-server boundary | Exercise 1 | Build Steps 1 and 11 | No Week 04 Android delta |
| GET, POST, and status codes | Exercises 2 and 4 | Build Steps 3–5 | Endpoint tests and `/docs` evidence |
| Multipart field `image` | Exercise 3 | Build Step 5 | Valid upload 200; missing field 422 |
| Stable JSON response | Exercise 3 | `PredictionResult` in `main.py` | Eight response fields in valid-upload evidence |
| Mock honesty | Exercises 1 and 5 | Build Step 6 | `/health` reports mock mode |
| Decode and size validation | Exercise 4 | `preprocess_image` and `/predict` | 400 and 413 tests |
| Resource cleanup | Exercise 4 | `/predict` `finally` block | Code inspection and automated requests |
| Configuration and dependencies | Exercise 5 | Build Step 2 | Isolated environment and successful import |
| Repeatable testing | Exercise 6 | Build Step 7 | Eight passing contract tests |
| Week 05 handoff | Exercise 1 and reflection | Build Step 11 | API contract note |

The proof order matters:

```text
read contract
    -> predict behavior
    -> inspect implementation
    -> run automated checks
    -> demonstrate manually
    -> record evidence
    -> explain the Week 05 handoff
```

Do not mark a concept complete merely because its code exists. Explain it, trigger it, observe it, and save the result.

---

## 14. Week 04 Understanding Checklist

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
- [ ] I can map each Week 04 concept to an exercise, implementation location, and validation result.

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
