# Week 05 Exercises: Android-to-FastAPI Networking

## How to Use These Exercises

Complete these before the build task. Save exactly six files under:

```text
docs/evidence/week-05/exercises/
```

Use your own words. Do not copy the learning-note explanations.

| Exercise | Output |
|---:|---|
| 1 | `exercise-01-progressive-handoff.md` |
| 2 | `exercise-02-response-model.md` |
| 3 | `exercise-03-multipart-plan.md` |
| 4 | `exercise-04-callback-state.md` |
| 5 | `exercise-05-network-debugging.md` |
| 6 | `exercise-06-demo-plan.md` |

---

## Exercise 1: Trace the Progressive Handoff

### Goal

Explain why Week 05 needs both Week 03 and Week 04.

### Task

Complete this flow:

```text
Week 03 produces:
Week 04 accepts:
Week 04 returns:
Week 05 adds:
Week 06 will replace or validate:
```

Draw arrows from `selectedImageUri` to `ResultActivity`. Label the Android/client and FastAPI/server responsibilities separately.

### Validation

- [ ] Week 03 image capture is not rewritten.
- [ ] Week 04 API shape is not changed.
- [ ] Multipart field `image` appears.
- [ ] Mock mode is not called real inference.

---

## Exercise 2: Build the Exact Response Table

### Goal

Map all Week 04 JSON keys to Kotlin.

### Task

Complete this table from the actual API contract:

| JSON key | Kotlin property | Kotlin type | Result-screen use |
|---|---|---|---|
| `model_label` | | | |
| `disease` | | | |
| `confidence` | | | |
| `uncertain` | | | |
| `guidance_available` | | | |
| `symptoms` | | | |
| `treatment` | | | |
| `prevention` | | | |

Answer:

1. Which fields need `@SerializedName`, and why?
2. Why are `model_label` and `disease` not duplicates?
3. Why can `guidance_available` be false for a valid label?
4. How does 0.87 become 87%?

### Validation

- [ ] Exactly eight fields are present.
- [ ] Types match the backend.
- [ ] Snake_case and camelCase are distinguished.

---

## Exercise 3: Plan URI-to-Multipart Conversion

### Goal

Understand every representation of the image.

### Task

Create this conversion table:

| Stage | Type | Why needed | Cleanup responsibility |
|---|---|---|---|
| Selected image | `Uri` | | |
| Open content | `InputStream` | | |
| Temporary upload | `File` | | |
| HTTP bytes | `RequestBody` | | |
| Named form part | `MultipartBody.Part` | | |

Then write pseudocode that:

1. opens the URI through `ContentResolver`
2. copies bytes to `cacheDir`
3. uses the selected MIME type
4. names the form part `image`
5. deletes the temporary file after success or failure

### Validation

- [ ] The plan does not use `File(uri.path)`.
- [ ] The part name is exactly `image`.
- [ ] Both stream and file cleanup are included.

---

## Exercise 4: Model Callback and UI State

### Goal

Keep Android responsive and recover every terminal path.

### Task

Complete the state table:

| Event | Progress | Detect button | Image buttons | Navigation/message |
|---|---|---|---|---|
| Before image | | | | |
| Image selected | | | | |
| Upload starts | | | | |
| HTTP 200 + body | | | | |
| HTTP 4xx/5xx | | | | |
| `onFailure` | | | | |
| URI copy fails | | | | |

Explain why HTTP 503 belongs to `onResponse`, while connection refusal belongs to `onFailure`.

### Validation

- [ ] `enqueue` is used instead of `execute`.
- [ ] Progress ends on every terminal path.
- [ ] Retry is possible after every failure.

---

## Exercise 5: Diagnose Address and Security Failures

### Goal

Separate URL, permission, cleartext, server, and contract failures.

### Task

Complete this debugging table:

| Symptom | First hypothesis | Discriminating check | Likely fix |
|---|---|---|---|
| Connection refused | | | |
| Cleartext not permitted | | | |
| HTTP 422 | | | |
| HTTP 400 | | | |
| HTTP 503 | | | |
| JSON conversion failure | | | |

Include these facts:

- emulator host is `10.0.2.2`
- Retrofit base URL ends with `/`
- manifest contains `INTERNET`
- cleartext is allowed only for the local emulator host
- `/health` reveals mock/real mode

### Validation

- [ ] HTTP errors are not confused with network failures.
- [ ] No public cleartext allowance is proposed.
- [ ] No private address is committed as evidence.

---

## Exercise 6: Design the Milestone Demo

### Goal

Plan reproducible evidence before implementation.

### Task

Create a numbered demo for:

1. eight backend tests passing
2. `/health` showing mock mode
3. Android image selection
4. visible upload state
5. successful Result screen
6. backend stopped
7. friendly retryable error
8. backend restarted and successful retry

Create an evidence table:

| Evidence file | Exact claim proved | Claim not proved |
|---|---|---|
| `backend-tests.txt` | | |
| `health-mode.png` | | |
| `upload-progress.png` | | |
| `result-mock.png` | | |
| `backend-down.png` | | |
| `android-build.txt` | | |

### Validation

- [ ] Success and failure are both demonstrated.
- [ ] Mock result is labeled honestly.
- [ ] Evidence contains no private IP or personal image.

---

## Completion Rule

Start the build task only when all six files exist and you can explain:

- why Week 05 depends on Weeks 03 and 04
- all eight response fields
- why a URI is copied to cache
- why the multipart field is `image`
- why `enqueue` keeps the UI responsive
- how HTTP errors differ from network failures
- why local HTTP is not a production security design

<!-- NAV_FOOTER_START -->

---

## Week 05 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | **Exercises - current** | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Learning Notes](learning-notes.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Build Task](build-task.md)