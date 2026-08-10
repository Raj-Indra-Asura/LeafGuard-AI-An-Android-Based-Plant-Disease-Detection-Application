# Week 04 Validation Checklist: Standalone FastAPI Backend

## Milestone Demo

> Start FastAPI, open `/docs`, verify `/health`, upload a real leaf image to `/predict` under `image`, read the JSON, and show one invalid upload fails safely. This proves the cumulative product state is 35%.

You may not move to Week 05 until every required item is yes.

## How to Use This Checklist

Validate in order after completing the build task. Record each result in `docs/evidence/week-04/validation.md` using:

```text
Item:
Method: automated test | /docs | source inspection | repository check
Expected:
Actual:
Evidence file:
Result: PASS | FAIL | NOT TESTED
```

This checklist contains **57 required items** across nine sections. A checked box means you observed evidence, not merely that the expected code exists. `NOT TESTED` is not a pass.

| Validation layer | Sections | What it proves |
|---|---|---|
| Progressive boundary | 1 and 7 | Week 03 remains intact and future work is honest |
| Environment and source | 2 | The standalone backend is reproducible and safely configured |
| API behavior | 3–5 | Routes, response contract, and failures behave correctly |
| Repeatability | 6 | Automated and manual observations agree |
| Learning record | 8–9 | Evidence exists and the student can explain it |

---

## 1. Week 03 Connection

- [ ] Week 03 camera/gallery flow remains working.
- [ ] Both Android tracks are unchanged for Week 04.
- [ ] I can explain what the backend adds.
- [ ] I can explain why Android integration waits until Week 05.

Pass rule: all 4 items yes.

---

## 2. Environment and Structure

- [ ] A virtual environment is used.
- [ ] Development requirements install successfully.
- [ ] `.venv/` or equivalent is not tracked.
- [ ] Backend responsibilities are separated across the existing files.
- [ ] No credentials or private addresses are committed.

Pass rule: all 5 items yes.

---

## 3. Health and Disease Endpoints

- [ ] `/` returns 200.
- [ ] `/health` returns 200.
- [ ] Health JSON reports runtime mode.
- [ ] `/diseases` returns 200.
- [ ] Disease count matches the returned list.

Pass rule: all 5 items yes.

---

## 4. Prediction Contract

- [ ] `/predict` uses POST.
- [ ] Upload format is multipart.
- [ ] Required field name is exactly `image`.
- [ ] A valid sample image returns 200.
- [ ] Response matches the documented model.
- [ ] Confidence is between 0.0 and 1.0.
- [ ] Mock mode is clearly identified.

Pass rule: all 7 items yes.

---

## 5. Validation and Safety

- [ ] Missing required upload is rejected.
- [ ] Empty upload is rejected.
- [ ] Non-image content is rejected.
- [ ] Spoofed image bytes are rejected.
- [ ] Oversized upload returns 413.
- [ ] Internal exception details are not exposed.
- [ ] Uploaded files are closed.
- [ ] Development server is not publicly exposed.

Pass rule: all 8 items yes.

---

## 6. Automated and Manual Testing

- [ ] Existing backend tests pass.
- [ ] `/docs` loads.
- [ ] All GET routes were tried manually.
- [ ] One successful `/predict` request was tried manually.
- [ ] One invalid `/predict` request was tried manually.
- [ ] Results agree with automated tests.

Pass rule: all 6 items yes.

---

## 7. Future Boundary

- [ ] No Retrofit integration is required.
- [ ] No Android INTERNET permission change is required.
- [ ] No real-model accuracy is claimed.
- [ ] No Room history is required.
- [ ] No offline TFLite inference is required.
- [ ] I can name the week that adds each deferred behavior.

Pass rule: all 6 items yes.

---

## 8. Evidence

Save evidence under `docs/evidence/week-04/`.

- [ ] Health response evidence saved.
- [ ] Disease-list evidence saved.
- [ ] Valid prediction evidence saved.
- [ ] Invalid-upload evidence saved.
- [ ] Test summary saved.
- [ ] API contract note saved.
- [ ] Quiz answers saved.
- [ ] Reflection answers saved.
- [ ] Progress tracker updated.

Pass rule: all 9 items yes.

---

## 9. Understanding Check

- [ ] I can explain client and server.
- [ ] I can distinguish GET and POST.
- [ ] I can explain multipart field `image`.
- [ ] I can explain JSON response fields.
- [ ] I can explain mock mode.
- [ ] I can explain 200, 400, 413, 422, and 503.
- [ ] I can explain how this prepares Week 05.

Pass rule: all 7 items yes.

---

## Completion Criteria

Week 04 is complete only when:

1. The backend runs and its tests pass.
2. Required endpoints are demonstrable.
3. Valid and invalid uploads behave safely.
4. Evidence is saved.
5. Future-week functionality is not falsely claimed.

If any item is no, remain in Week 04 and fix it before connecting Android.

---

## Evidence Coverage Matrix

Use this matrix before signing off the week:

| Evidence | Checklist sections covered | Minimum visible proof |
|---|---|---|
| Health response | 3, 4, 9 | 200, `status`, mock mode, class count |
| Disease-list response | 3, 9 | 200, count 10, 10 returned records |
| Valid prediction | 4, 6 | Multipart `image`, 200, all eight fields |
| Invalid prediction | 5, 6 | Request detail, safe status, safe response detail |
| Automated test summary | 3–6 | `Ran 8 tests` and `OK` |
| API contract note | 4, 9 | Method, path, field name, response fields, statuses |
| Repository boundary check | 1, 2, 7 | No Week 04 Android delta or tracked local environment |
| Quiz and reflection | 8, 9 | Passing score and evidence-based explanation |
| Progress record | 8 | Date, work completed, challenge, next step |

## Failure Routing

Do not restart the whole week when one check fails. Return to the smallest responsible boundary:

| Failed observation | Return to | Recheck |
|---|---|---|
| Environment import fails | Build Step 2 and backend README setup | Import command |
| Health mode is wrong | `config.py`, `.env`, Build Step 3 | `/health` |
| Disease count differs | `DISEASE_INFO`, Build Step 4 | Disease-list test |
| Missing field does not return 422 | `/predict` signature and FastAPI contract | One missing-field request |
| Invalid bytes return success | `preprocess_image`, Build Step 5 | Spoofed-image test |
| Oversized upload is not 413 | Read limit in `/predict` | Oversized test |
| Real mode silently mocks | `model_loader.py`, Build Step 6 | 503 test and `/health` |
| Manual and automated results differ | Exercise 6 debugging record | Focused test, then all eight |

After a repair, rerun the focused failing check first and then the complete eight-test suite. Save the final passing result; do not hide the earlier failure from your learning notes.

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
| **5** | **validation-checklist.md** ← *You are here* | **Validation & Verification** |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

### Within-Week Navigation

[← Build Implementation Guide](build-task.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Validation & Verification** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Knowledge Assessment Quiz →](quiz.md)

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
