# Week 05 Reflection: First Android-to-Backend Connection

## Purpose

Save your answers in:

```text
docs/evidence/week-05/reflection-answers.md
```

Use your own words. Every technical claim should point to a screenshot, test output, Logcat observation, source boundary, or validation result.

---

## Section 1: Progressive Product Growth

### Prompt 1: Inputs From Earlier Weeks

What exact output from Week 03 and what exact contract from Week 04 were required before Week 05 could work?

### Prompt 2: New Ability

What can the product do after Week 05 that neither earlier week could demonstrate alone?

### Prompt 3: Honest Boundary

Why does a successful mock response prove integration but not disease-recognition accuracy?

---

## Section 2: Android Networking Understanding

### Prompt 4: URI to Multipart

Explain every representation from `selectedImageUri` to multipart field `image`. Why is `File(uri.path)` avoided?

### Prompt 5: Retrofit and Gson

Explain the separate jobs of `ApiService`, `RetrofitClient`, OkHttp, and Gson.

### Prompt 6: Eight-Field Contract

Why must Android retain `model_label`, `uncertain`, and `guidance_available` instead of using only disease, confidence, and guidance text?

### Prompt 7: Async UI

Explain why `enqueue` is used and how the loading state changes from tap to terminal callback.

### Prompt 8: Error Categories

Explain why HTTP 503 reaches `onResponse`, while a stopped backend normally reaches `onFailure`.

### Prompt 9: Local Security

Why is cleartext allowed only for `10.0.2.2`, and what must change for production?

---

## Section 3: Debugging and Evidence

### Prompt 10: One Real Failure

Use this format:

```text
Observed symptom:
First hypothesis:
Discriminating check:
Actual cause:
Fix:
Focused retest:
Full retest:
Evidence file:
```

### Prompt 11: Best Evidence

Which artifact best proves the Week 05 pipeline works? What does it prove, and what does it not prove?

### Prompt 12: Recovery

What happened to progress and buttons when the backend was stopped? Why is retryability part of the feature rather than optional polish?

---

## Section 4: Confidence Table

| Topic | Confidence 1-10 | Evidence | Next practice if below 7 |
|---|---:|---|---|
| Client-server boundary | | | |
| URI-to-cache conversion | | | |
| Multipart contract | | | |
| Eight-field Gson model | | | |
| Retrofit callbacks | | | |
| HTTP vs network errors | | | |
| Local network security | | | |
| Result navigation | | | |

Scores of 9 or 10 require specific evidence.

---

## Section 5: Week 06 Handoff

Complete this three-sentence handoff:

1. **What exists now:** describe the verified Android-to-FastAPI pipeline.
2. **What is still mock:** describe what `/health` reports and why it matters.
3. **What Week 06 adds:** describe real cloud model validation without changing the Android request/response contract.

---

## Completion Check

- [ ] I identified the exact Week 03 and Week 04 inputs.
- [ ] I explained all eight response fields.
- [ ] I distinguished HTTP and network failures.
- [ ] I cited success and failure evidence.
- [ ] I explained local-only cleartext security.
- [ ] I did not claim mock accuracy.
- [ ] I documented one debugging cycle.
- [ ] I completed the Week 06 handoff.
- [ ] I updated the progress tracker.

<!-- NAV_FOOTER_START -->

---

## Week 05 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | **Reflection - current**

[Previous: Quiz](quiz.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 06](../week-06-cloud-ml-model/README.md)