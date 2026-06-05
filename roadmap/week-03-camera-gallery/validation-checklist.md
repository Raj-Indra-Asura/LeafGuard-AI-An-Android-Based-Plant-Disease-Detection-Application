# Week 03 Validation Checklist

## Purpose
Verify complete Week 03 implementation before proceeding to Week 04. All items must be checked.

---

## Permissions

- [ ] CAMERA permission declared in manifest
- [ ] READ_EXTERNAL_STORAGE declared (maxSdkVersion="32")
- [ ] READ_MEDIA_IMAGES declared for Android 13+
- [ ] Permission launchers registered correctly
- [ ] Permission check before camera/gallery access
- [ ] Permission granted case handled (opens feature)
- [ ] Permission denied case handled (shows message)
- [ ] Permanent denial handled (optional: redirect to settings)

---

## FileProvider

- [ ] FileProvider declared in manifest
- [ ] Authority matches ${applicationId}.fileprovider
- [ ] file_paths.xml created in res/xml/
- [ ] external-files-path configured for Pictures/
- [ ] FileProvider.getUriForFile() used correctly
- [ ] No FileUriExposedException crashes

---

## Camera Implementation

- [ ] TakePicture launcher registered
- [ ] createImageFile() creates unique filename
- [ ] Image saved to getExternalFilesDir(PICTURES)
- [ ] Camera button checks permission first
- [ ] Camera launches successfully
- [ ] Captured image URI saved
- [ ] Image displayed after capture
- [ ] Cancel handled gracefully (no crash)

---

## Gallery Implementation

- [ ] Gallery picker implemented (Photo Picker or Intent)
- [ ] Version check for API 11+ vs legacy
- [ ] PickVisualMedia used on Android 11+
- [ ] Gallery button opens picker
- [ ] Selected image URI received
- [ ] Image displayed after selection
- [ ] Cancel handled gracefully
- [ ] Works on API 24, 30, 33

---

## Image Handling

- [ ] loadBitmapFromUri() implemented
- [ ] calculateInSampleSize() prevents OOM
- [ ] loadScaledBitmap() scales to max 1024x1024
- [ ] ImageView displays loaded bitmap
- [ ] currentImageUri saved for later use
- [ ] IOException handled with try-catch
- [ ] No OutOfMemoryError on large images

---

## UI and State Management

- [ ] ImageView shows captured/selected image
- [ ] Placeholder shown when no image
- [ ] State preserved on screen rotation
- [ ] onSaveInstanceState saves URI
- [ ] onCreate restores URI if available
- [ ] Log statements added for debugging
- [ ] Toast messages shown for user feedback

---

## Testing Completed

- [ ] Camera tested on emulator
- [ ] Camera tested on physical device
- [ ] Gallery tested on emulator
- [ ] Gallery tested on physical device
- [ ] Permission grant tested
- [ ] Permission deny tested
- [ ] Screen rotation tested with image
- [ ] Cancel camera tested
- [ ] Cancel gallery tested
- [ ] Large image (4MB+) tested
- [ ] API 24 emulator tested
- [ ] API 30+ emulator tested

---

## Code Quality

- [ ] No compilation errors
- [ ] No runtime crashes
- [ ] Null checks for URI
- [ ] Try-catch for IOException
- [ ] Log statements present
- [ ] Comments explain complex logic
- [ ] Consistent naming conventions
- [ ] No hardcoded strings

---

## Evidence Collection

- [ ] Screenshot of camera permission dialog
- [ ] Screenshot of captured image displayed
- [ ] Screenshot of gallery picker
- [ ] Screenshot of selected image displayed
- [ ] Video of camera flow (permission → capture → display)
- [ ] Video of gallery flow (open → select → display)
- [ ] Logcat screenshot showing execution
- [ ] Git commits show progressive work

---

## Documentation

- [ ] Learning notes completed
- [ ] Exercises attempted (min 5/7)
- [ ] Build task completed
- [ ] Reflection submitted
- [ ] Quiz completed (score ≥ 8/10)

---

## Understanding Verification

Can you explain without notes:

- [ ] Android permission system (normal vs dangerous)
- [ ] Why FileProvider is required
- [ ] Difference between file URI and content URI
- [ ] How inSampleSize reduces memory usage
- [ ] Activity Result API vs onActivityResult
- [ ] When to use app-specific vs shared storage

---

## Completion Criteria

You may proceed to Week 04 only when:

1. All checklist items verified
2. App captures/selects images without crashes
3. Permissions handled correctly
4. Quiz score ≥ 8/10
5. Evidence package complete
6. Can explain concepts independently

**If any item incomplete, do not proceed. Week 04 requires functional image input.**

Submit: `evidence/week-03/VALIDATION-COMPLETE.txt`
Commit: "Week 03: Validation complete"


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
