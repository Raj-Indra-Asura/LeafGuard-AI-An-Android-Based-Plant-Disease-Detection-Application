# Week 03 Validation Checklist: Image Input

## Milestone Demo

> Demo: Open Scan, select one image from gallery and show it in preview, capture one image from camera and show it in preview, then show that denial or cancellation does not crash the app. This proves the cumulative product state is 25%.

You may not move to Week 04 until every required item is yes.

---

## 1. Week 02 Connection

- [ ] Week 02 navigation shell is still working.
- [ ] Home opens `ScanActivity`.
- [ ] Week 03 work happens in `ScanActivity`, not `MainActivity`.
- [ ] Result, History, Library, and Settings/About placeholders still open.
- [ ] I can explain what Week 03 adds beyond Week 02.

Pass rule: all 5 items yes.

---

## 2. Manifest and FileProvider

- [ ] `CAMERA` permission is declared.
- [ ] Camera hardware is declared with `required="false"` or the limitation is documented.
- [ ] FileProvider is declared inside `<application>`.
- [ ] FileProvider authority uses `${applicationId}.fileprovider`.
- [ ] `res/xml/file_provider_paths.xml` exists.
- [ ] FileProvider paths include an app-specific Pictures path.
- [ ] App builds after manifest changes.

Pass rule: all 7 items yes.

---

## 3. Scan Screen UI

- [ ] Scan screen has a title.
- [ ] Scan screen has an image preview area.
- [ ] Scan screen has image status text.
- [ ] Scan screen has a Take Photo button.
- [ ] Scan screen has a Choose from Gallery button.
- [ ] Screen does not claim detection is already working.
- [ ] Layout does not overlap on a phone screen.

Pass rule: all 7 items yes.

---

## 4. Gallery Selection

- [ ] Gallery/content picker opens from Scan screen.
- [ ] User can choose an image.
- [ ] Selected image URI is received.
- [ ] Selected image displays in the preview.
- [ ] Cancelling gallery does not crash the app.
- [ ] User receives clear feedback or the screen remains stable after cancellation.

Pass rule: all 6 items yes.

---

## 5. Camera Capture

- [ ] App checks camera permission before launching camera.
- [ ] Runtime permission dialog appears when needed.
- [ ] Permission granted path launches camera.
- [ ] Permission denied path shows a helpful message and does not crash.
- [ ] Camera output URI is created with FileProvider.
- [ ] Captured image displays in the preview.
- [ ] Cancelling camera does not crash the app.

Pass rule: all 7 items yes.

---

## 6. URI and State

- [ ] `selectedImageUri` or equivalent variable stores the current image URI.
- [ ] Preview updates through one clear helper function.
- [ ] URI is saved in `onSaveInstanceState` or limitation is documented.
- [ ] URI is restored after simple recreation or limitation is documented.
- [ ] Null URI cases are handled safely.

Pass rule: all 5 items yes.

---

## 7. Future Boundary

- [ ] No backend upload is required in Week 03.
- [ ] No disease prediction is faked in Week 03.
- [ ] No Room history save is required in Week 03.
- [ ] No XML disease lookup is required in Week 03.
- [ ] No TensorFlow Lite inference is required in Week 03.
- [ ] Student can explain which later week adds each deferred behavior.

Pass rule: all 6 items yes.

---

## 8. Evidence

Save evidence under `docs/evidence/week-03/`.

- [ ] Build success evidence saved.
- [ ] Scan screen before image screenshot saved.
- [ ] Gallery picker screenshot or note saved.
- [ ] Gallery preview screenshot saved.
- [ ] Camera permission screenshot or note saved.
- [ ] Camera preview screenshot saved.
- [ ] Cancellation or denial behavior note saved.
- [ ] Quiz answers saved.
- [ ] Reflection answers saved.

Pass rule: all 9 items yes.

---

## 9. Understanding Check

Can you explain these in your own words?

- [ ] Why camera permission is needed.
- [ ] What a URI is.
- [ ] Why FileProvider is needed.
- [ ] What `TakePicture` does.
- [ ] What `GetContent` does.
- [ ] Why image analysis waits for later weeks.

Pass rule: all 6 items yes.

---

## Completion Criteria

Week 03 is complete only when:

1. Camera and gallery image input are demonstrable.
2. Preview updates with selected/captured images.
3. Denial and cancellation do not crash the app.
4. Evidence is saved.
5. Future-week behaviors are not faked.

If any item is no, stay in Week 03 and fix it before moving to Week 04.

<!-- NAV_FOOTER_START -->

---

## 📚 Week 03 — Navigation

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

---

### Within-Week Navigation

[← Build Implementation Guide](build-task.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Validation & Verification** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Knowledge Assessment Quiz →](quiz.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 02: Android Basics & UI](../week-02-android-basics-ui/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 04: FastAPI Backend ➡](../week-04-fastapi-backend/README.md) |

---
