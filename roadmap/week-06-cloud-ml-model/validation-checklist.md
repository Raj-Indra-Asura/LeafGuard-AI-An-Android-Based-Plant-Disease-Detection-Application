# Week 06 Validation Checklist: Real Cloud Model Inference

## Milestone Demo

> Verify the approved Keras artifact, run the four cloud-model contract tests, start FastAPI with `USE_MOCK=false`, show real-mode health and one HTTP 200 prediction, then prove a missing model returns 503 without changing Android.

Every required item must be yes before Week 07.

Record each observation in `docs/evidence/week-06/validation.md`:

```text
Item:
Method: hash | source inspection | model inspector | test | /health | /predict
Expected:
Actual:
Evidence:
Result: PASS | FAIL | NOT TESTED
```

`NOT TESTED` and skipped TensorFlow tests are not passes.

---

## 1. Progressive Boundary

- [ ] Week 05 Android-to-FastAPI mock pipeline still works.
- [ ] Week 04 eight-field API contract remains unchanged.
- [ ] Android source has zero Week 06 changes.
- [ ] Week 06 is described as cloud Keras inference, not training.
- [ ] TFLite conversion and offline Android inference are deferred.
- [ ] Room/history work is deferred to Week 07.

Pass rule: all 6.

---

## 2. Artifact Identity and Provenance

- [ ] Local path is `backend-api/models/leafguard_model.keras`.
- [ ] Exact size is 25,143,175 bytes.
- [ ] SHA-256 is `08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f`.
- [ ] Source repository is recorded.
- [ ] Pinned source commit is recorded.
- [ ] Upstream artifact path is recorded.
- [ ] License claim was personally reviewed.
- [ ] Binary is tracked at the documented path and matches the approved identity.
- [ ] Size or hash mismatch stops validation.

Pass rule: all 9.

---

## 3. Labels

- [ ] `model/labels-38.txt` exists.
- [ ] It contains exactly 38 non-empty labels.
- [ ] All labels are unique.
- [ ] It exactly matches `backend-api/labels-38.txt`.
- [ ] Label order is preserved.
- [ ] No alphabetical sorting or renaming occurred.
- [ ] Output index 0 maps to line 1.
- [ ] Output index 37 maps to line 38.

Pass rule: all 8.

---

## 4. Tensor and Preprocessing Contract

- [ ] TensorFlow reports version 2.19.1.
- [ ] Keras input shape is `(None, 224, 224, 3)`.
- [ ] Keras input dtype is `float32`.
- [ ] Input color order is RGB.
- [ ] Caller passes raw pixel values in `[0,255]`.
- [ ] Embedded preprocessing maps `[0,255]` to `[-1,1]`.
- [ ] Caller does not divide pixels by 255.
- [ ] Keras output shape is `(None, 38)`.
- [ ] Output dtype is `float32`.
- [ ] `argmax` index is decoded through canonical label order.

Pass rule: all 10.

---

## 5. Exact Validation Package

- [ ] All six Week 06 text files exist.
- [ ] Their total logical size is 311 lines.
- [ ] Complete contents match learning-notes Section 12.
- [ ] `model_contract.py` validates label count and uniqueness.
- [ ] `model_contract.py` validates shape and dtype.
- [ ] `model_contract.py` detects embedded scaling.
- [ ] `inspect_model.py` prints observable contract facts.
- [ ] `test_model_contract.py` contains four focused tests.
- [ ] Provenance records the real validation result.

Pass rule: all 9.

---

## 6. Automated Contract Tests

- [ ] Canonical and backend labels match test passes.
- [ ] Approved Keras model contract test passes.
- [ ] Incorrect input shape rejection test passes.
- [ ] Incorrect scaling rejection test passes.
- [ ] No TensorFlow test is skipped.
- [ ] Summary reports `Ran 4 tests`.
- [ ] Summary reports `OK`.

Pass rule: all 7.

---

## 7. Real-Mode Health

- [ ] Server starts with `USE_MOCK=false`.
- [ ] `/health` returns HTTP 200.
- [ ] `status` is `ok`.
- [ ] `use_mock` is `false`.
- [ ] `model_loaded` is `true`.
- [ ] `image_size` is 224.
- [ ] `class_count` is 38.
- [ ] Reported model and labels paths are expected.

Pass rule: all 8.

---

## 8. Real Prediction and API Compatibility

- [ ] Request remains `POST /predict`.
- [ ] Multipart field remains `image`.
- [ ] Valid image returns HTTP 200 in real mode.
- [ ] Returned `model_label` is canonical.
- [ ] Confidence is bounded from 0.0 to 1.0.
- [ ] `uncertain` follows threshold behavior.
- [ ] `guidance_available` distinguishes reviewed guidance.
- [ ] All eight response fields are present.
- [ ] Week 05 Android parses the response without modification.
- [ ] One prediction is not presented as accuracy validation.

Pass rule: all 10.

---

## 9. Failure Behavior

- [ ] Missing or wrong model path produces `model_loaded=false`.
- [ ] Valid upload in unavailable real mode returns HTTP 503.
- [ ] Client response does not expose a raw traceback.
- [ ] Server logs retain diagnostic detail.
- [ ] Backend does not silently call mock output real inference.
- [ ] Restoring the approved artifact returns health to loaded state.
- [ ] Real prediction succeeds again after restoration.

Pass rule: all 7.

---

## 10. Evidence and Understanding

- [ ] Artifact identity output saved.
- [ ] Provenance/license record saved.
- [ ] Label comparison saved.
- [ ] Inspector output saved.
- [ ] Four-test summary saved.
- [ ] Real-mode health evidence saved.
- [ ] Real prediction JSON saved.
- [ ] Missing-model 503 evidence saved.
- [ ] Week 05 Android regression evidence saved.
- [ ] Honest limitations note saved.
- [ ] Quiz score is at least 14/18.
- [ ] Reflection uses observed evidence.
- [ ] Progress tracker is updated.
- [ ] Evidence does not contain the model binary, secrets, or private settings.

Pass rule: all 14.

---

## Failure Routing

| Failure | Return to | Focused recheck |
|---|---|---|
| Size/hash mismatch | Artifact source and provenance | `stat`, `sha256sum` |
| TensorFlow import fails | Build Step 5 | Version import command |
| Labels differ | Canonical labels | `cmp` and label test |
| Input/output shape fails | Artifact compatibility | Inspector |
| Scaling check fails | Model preprocessing evidence | Scaling rejection test |
| `model_loaded=false` | Model path/load logs | `/health` |
| `/predict` returns 503 | Real-mode availability | Health then upload |
| Android parsing fails | Preserved eight-field contract | Week 05 regression |

After repair, rerun the focused check, all four model tests, real health, and one real prediction.

---

## Completion Criteria

Week 06 is complete only when:

1. Artifact identity and provenance match exactly.
2. All 38 labels match in immutable order.
3. Keras shape, dtype, and embedded scaling are verified.
4. Four focused tests pass without skips.
5. `/health` proves loaded real mode.
6. One real-mode prediction preserves all eight fields.
7. Missing model fails with 503 and no silent mock claim.
8. Week 05 Android remains unchanged and compatible.
9. Evidence states limitations honestly.

<!-- NAV_FOOTER_START -->

---

## Week 06 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | **Validation - current** | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Build Task](build-task.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Quiz](quiz.md)