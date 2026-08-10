# Week 05 Quiz: Retrofit, Multipart, and Result Flow

## Instructions

Answer after the build and validation work. Do not open the answer key first.

Passing score: **14 out of 18**.

---

## Multiple Choice

### 1. What does Week 05 connect?

A) Room to TFLite
B) Week 03 Android image input to the Week 04 backend
C) Notifications to history
D) Two backend models

Answer: ____

### 2. Which emulator URL reaches the development computer?

A) `http://localhost:8000/`
B) `http://127.0.0.1:8000/`
C) `http://10.0.2.2:8000/`
D) `file://10.0.2.2/`

Answer: ____

### 3. What is the required multipart field name?

A) `file`
B) `photo`
C) `image`
D) `uri`

Answer: ____

### 4. Why is a selected URI copied to cache?

A) Gson requires JPEG names
B) Content URIs are not reliable direct filesystem paths
C) FastAPI requires permanent storage
D) Retrofit cannot send bytes

Answer: ____

### 5. What does Gson do?

A) Captures photos
B) Maps JSON to `PredictionResponse`
C) Starts Uvicorn
D) Grants Internet permission

Answer: ____

### 6. Which callback receives HTTP 503?

A) `onFailure` only
B) `onResponse` because the server responded
C) Neither
D) Camera result callback

Answer: ____

### 7. Why use `enqueue`?

A) To sort labels
B) To avoid blocking the Android UI thread
C) To request camera permission
D) To enable cleartext

Answer: ____

### 8. What does Week 05 success prove in mock mode?

A) Real model accuracy
B) Android-FastAPI contract integration
C) Offline inference
D) Database persistence

Answer: ____

---

## True or False

### 9. Retrofit base URLs must end with `/`.

Answer: ____

### 10. Android requires a runtime dialog for `INTERNET` permission.

Answer: ____

### 11. `response.body() != null` is sufficient without checking the HTTP status.

Answer: ____

### 12. The Week 05 response model should contain all eight Week 04 fields.

Answer: ____

### 13. Production should allow cleartext HTTP to every domain.

Answer: ____

---

## Short Answer

### 14. Trace one successful request from `selectedImageUri` to ResultActivity in 5-8 steps.

Answer:

### 15. Explain the difference between `onResponse` and `onFailure`, with one LeafGuard example for each.

Answer:

### 16. Name all eight prediction response fields.

Answer:

### 17. Why are progress and buttons reset in both callback paths?

Answer:

### 18. Name three things Week 05 must not implement or claim yet.

Answer:

---

## Answer Key

1. B
2. C
3. C
4. B
5. B
6. B
7. B
8. B
9. True
10. False
11. False
12. True
13. False

Short-answer requirements:

| Question | Full-credit ideas |
|---:|---|
| 14 | URI, input stream/cache file, RequestBody, multipart `image`, enqueue, FastAPI, Gson, Intent/Result |
| 15 | `onResponse` means HTTP response including errors; `onFailure` means no usable response/conversion failure |
| 16 | `model_label`, `disease`, `confidence`, `uncertain`, `guidance_available`, `symptoms`, `treatment`, `prevention` |
| 17 | End loading state, avoid duplicate requests, permit retry, prevent stranded UI |
| 18 | Any three of real-model accuracy, Room history, offline TFLite, notifications, production deployment, UI redesign |

---

## Remediation Map

| Missed questions | Review |
|---|---|
| 1, 8, 18 | Progressive boundary and honest mock mode |
| 2, 9, 10, 13 | URL, permission, and security |
| 3, 4, 14 | URI and multipart conversion |
| 5, 12, 16 | Eight-field Gson model |
| 6, 7, 11, 15, 17 | Async callbacks and UI recovery |

If your score is below 14, review only the mapped sections, rewrite missed answers in your own words, and retake before Week 06.

<!-- NAV_FOOTER_START -->

---

## Week 05 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | **Quiz - current** | [Reflection](reflection.md)

[Previous: Validation](validation-checklist.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Reflection](reflection.md)