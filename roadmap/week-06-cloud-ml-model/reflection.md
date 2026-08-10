# Week 06 Reflection: Trustworthy Real Model Integration

## Purpose

Save your answers in:

```text
docs/evidence/week-06/reflection-answers.md
```

Use your own words. Support every technical claim with observed artifact, inspector, test, health, prediction, or failure evidence.

---

## Section 1: Progressive Product Growth

### Prompt 1: Week 05 Input

What request/response pipeline was already verified before the real model was introduced?

### Prompt 2: Week 06 Change

What changed behind `/predict`, and what deliberately remained unchanged for Android?

### Prompt 3: Honest Boundary

What does real Keras execution prove, and why does it not independently prove the source author's reported accuracy?

---

## Section 2: Artifact and Contract Understanding

### Prompt 4: Provenance

Why are source commit, license review, size, and SHA-256 all needed? Which check would detect one changed byte?

### Prompt 5: Tensor Shape

Explain `(1, 224, 224, 3)` and `(1, 38)` in your own words.

### Prompt 6: Label Order

Why would alphabetically sorting a valid 38-label file make predictions semantically wrong without causing a shape error?

### Prompt 7: Preprocessing

Explain the embedded mapping and why the backend must preserve raw `[0,255]` float values.

### Prompt 8: Real-Mode Proof

Why is `USE_MOCK=false` configuration rather than proof? Cite the health and test values that complete the proof.

---

## Section 3: Validation and Debugging

### Prompt 9: Most Useful Test

Which of the four focused tests taught you the most, and what defect would it catch?

### Prompt 10: Failure Path

Use this format:

```text
Failure introduced:
Expected health:
Expected prediction status:
Actual observation:
Server diagnostic:
Client-safe response:
Restoration:
Final retest:
Evidence:
```

### Prompt 11: Best Evidence

Which combination of evidence best proves that the approved artifact ran in real mode? Explain why a disease name alone is weaker.

---

## Section 4: Limitations and Professional Communication

### Prompt 12: Confidence Versus Accuracy

Explain confidence for one output and accuracy over a labeled evaluation set. Why is neither a confirmed diagnosis?

### Prompt 13: Dataset Limits

How might PlantVillage-style images differ from real phone-camera images in background, lighting, blur, framing, and disease distribution?

### Prompt 14: User-Facing Wording

Write a short result disclaimer that is useful without overstating certainty.

---

## Section 5: Confidence Table

| Topic | Confidence 1-10 | Evidence | Next practice if below 7 |
|---|---:|---|---|
| Artifact provenance | | | |
| SHA-256 verification | | | |
| Tensor shapes/dtypes | | | |
| Canonical label order | | | |
| Embedded preprocessing | | | |
| Real-mode health | | | |
| API compatibility | | | |
| Model limitations | | | |

Scores of 9 or 10 require specific evidence.

---

## Section 6: Week 07 Handoff

Complete three sentences:

1. **What exists now:** describe the verified real cloud prediction flow.
2. **What remains transient:** explain why results disappear after the current UI flow.
3. **What Week 07 adds:** describe local Room persistence without changing model inference.

---

## Completion Check

- [ ] I identified the exact approved artifact.
- [ ] I explained tensor and label contracts.
- [ ] I explained double normalization risk.
- [ ] I cited four passing tests without skips.
- [ ] I cited real-mode health and prediction evidence.
- [ ] I documented missing-model failure and restoration.
- [ ] I separated confidence, source metrics, and product accuracy.
- [ ] I did not claim TFLite/offline completion.
- [ ] I completed the Week 07 handoff.
- [ ] I updated the progress tracker.

<!-- NAV_FOOTER_START -->

---

## Week 06 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | **Reflection - current**

[Previous: Quiz](quiz.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 07](../week-07-room-sqlite-history/README.md)