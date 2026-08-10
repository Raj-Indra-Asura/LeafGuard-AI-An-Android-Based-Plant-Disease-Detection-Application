# Week 09 Reflection: Offline Prediction With Honest Evidence

Save answers in `docs/evidence/week-09/reflection-answers.md`. Cite observed tests, builds, offline/cloud runs, Room history, and failure behavior.

## Progressive Growth

1. What did Week 08 provide locally, and what new offline ability does Week 09 add?
2. What remains identical between cloud and offline results?
3. What does offline availability prove and not prove?

## Model and Android Understanding

4. Explain conversion versus training.
5. Explain artifact identity and why TFLite gets its own hash.
6. Explain tensor shapes, raw RGB range, and embedded scaling.
7. Derive the 602,112-byte input buffer.
8. Explain memory mapping, interpreter threads, argmax, and close.
9. Explain canonical labels versus display names.
10. Explain XML guidance and generic fallback in offline response.

## Strategy and Failures

11. Trace Cloud and Offline from mode selection to one `openResult`.
12. Explain progress/control recovery on every terminal path.
13. Document one missing/corrupt asset debugging cycle:

```text
Symptom:
Hypothesis:
Discriminating check:
Cause:
Fix:
Focused retest:
Full retest:
Evidence:
```

## Parity and Accuracy

14. Explain the reproduced top-1/delta results.
15. Why did parity pass even though folder labels disagreed?
16. What evaluation would be needed before claiming useful accuracy?

## Confidence Table

| Topic | 1-10 | Evidence | Next practice if below 7 |
|---|---:|---|---|
| Conversion | | | |
| Tensor/buffer contract | | | |
| Labels/argmax | | | |
| Classifier lifecycle | | | |
| Cloud/offline strategy | | | |
| Failure recovery | | | |
| Parity interpretation | | | |

## Week 10 Handoff

Complete:

1. **What exists now:** cloud and offline prediction plus guidance/history.
2. **What remains utilitarian:** describe missing sharing/location/notification polish.
3. **What Week 10 adds:** utility features without changing model contracts.

## Completion

- [ ] I cited parity and misclassification evidence.
- [ ] I demonstrated backend-off offline behavior.
- [ ] I demonstrated cloud regression and Room save.
- [ ] I documented resource cleanup/failure recovery.
- [ ] I did not claim parity as accuracy.
- [ ] I completed Week 10 handoff and progress tracker.

<!-- NAV_FOOTER_START -->

---

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | **Reflection**