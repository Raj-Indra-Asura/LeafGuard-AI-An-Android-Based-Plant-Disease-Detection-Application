# Week 04 Reflection: First Standalone Backend

## Purpose

Connect the FastAPI service to the growing product without claiming future Android networking or real-model work.

Save your own answers in:

```text
docs/evidence/week-04/reflection-answers.md
```

Use your own words. Do not copy answers from the roadmap.

Every technical claim should point to something you actually observed: a response, a test result, a source boundary, or a saved screenshot. Reflection is the final CSE 2206 learning step because it connects implementation facts to design choices.

Before answering, place this progression at the top of your file and complete the last column:

| Week | Input from prior week | New verified ability | Evidence I produced |
|---:|---|---|---|
| 01 | Product idea | Buildable product plan | |
| 02 | Week 01 screen map | Runnable Android navigation shell | |
| 03 | Scan placeholder | Camera/gallery image URI and preview | |
| 04 | Week 03 image concept | Standalone validated FastAPI contract | |

---

## Section 1: From Week 03 to Week 04

### Prompt 1: Product Growth
What server-side ability did Week 04 add?

Name the request path, upload field, and successful response type in your answer.

### Prompt 2: Separate Components
Why was the backend tested independently instead of connecting Android immediately?

Explain which possible cause of failure was removed by keeping Android out of Week 04.

### Prompt 3: Honest Boundary
What did you intentionally defer to Weeks 05 and 06?

Name one specific artifact each future week will add.

---

## Section 2: Backend Understanding

### Prompt 4: Request and Response
Explain one `/predict` request from upload to JSON response.

Include validation, preprocessing, prediction mode, response construction, and file cleanup.

### Prompt 5: Multipart Contract
Why must every client use the exact field name `image`?

State the status expected when the required field is missing.

### Prompt 6: Mock Prediction
Why is a mock useful now, and why must it be clearly identified as a mock?

Name the endpoint and field that reveal the runtime mode.

### Prompt 7: Status Codes
Explain what 200, 400, 413, and 503 communicate.

Also explain how 422 differs from the invalid-image 400 path.

---

## Section 3: Debugging and Evidence

### Prompt 8: Failure Path
Describe one invalid request, its response, and what you learned.

Reference your Exercise 6 debugging record or invalid-request screenshot.

### Prompt 9: Best Evidence
Which screenshot or test result best proves Week 04 is complete?

Explain what it proves and one thing it does not prove.

### Prompt 10: Contract Confidence
How do automated tests reduce the risk for Week 05?

Name at least two contract details protected by the eight tests.

---

## Section 4: Preparing for Week 05

### Prompt 11: Android Connection
What data from Week 03 will the Android app send to this API next week?

Describe the conversion conceptually; do not add Retrofit code to the reflection.

### Prompt 12: Confidence Check

| Topic | Confidence (1–10) | Why |
|---|---:|---|
| Client-server boundary | | |
| GET and POST | | |
| Multipart upload | | |
| JSON response | | |
| Error status | | |
| `/docs` testing | | |

For every score below 7, add one specific practice action before Week 05. For every score of 9 or 10, cite evidence that justifies the score.

---

## Section 5: Evidence-Anchored Retrospective

Complete this final table:

| Question | Your evidence-based answer |
|---|---|
| Which of the eight tests taught you the most? | |
| Which source file now has the clearest responsibility to you? | |
| Which status code did you initially misunderstand? | |
| What exact Week 04 boundary prevented scope creep? | |
| What contract fact must Week 05 preserve unchanged? | |

Then write a three-sentence handoff:

1. **What exists now:** describe the verified standalone backend.
2. **What remains separate:** describe the unchanged Week 03 Android image flow.
3. **What comes next:** describe the Week 05 connection without claiming it already works.

---

## Completion Check

- [ ] I can explain how Week 04 grows from Week 03.
- [ ] I can demonstrate all Week 04 endpoints.
- [ ] I can explain the `image` multipart contract.
- [ ] I tested one success and one failure.
- [ ] I know Android networking is Week 05.
- [ ] I know real cloud inference is Week 06.
- [ ] I saved evidence and updated the progress tracker.
- [ ] Every major claim in my reflection points to observed evidence.
- [ ] My Week 05 handoff preserves the existing multipart and response contract.

<!-- NAV_FOOTER_START -->

---

## 📚 Week 04 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| **7** | **reflection.md** ← *You are here* | **Reflection & Consolidation** |

### Within-Week Navigation

[← Knowledge Assessment Quiz](quiz.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Reflection & Consolidation** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Week 05: Android Networking (Start) →](../week-05-android-networking/README.md)

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
