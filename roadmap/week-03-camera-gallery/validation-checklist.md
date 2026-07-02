# Week 03 Validation Checklist

## Purpose
Verify complete Week 03 implementation before proceeding to Week 04. All items must be checked. Answer each question honestly with "yes" or "no"—you should be able to answer "yes" to every item before moving on.

---

## Permissions

- [ ] Did you add the CAMERA permission to AndroidManifest.xml?
- [ ] Did you add READ_EXTERNAL_STORAGE with `maxSdkVersion="32"` for older devices?
- [ ] Did you add READ_MEDIA_IMAGES for Android 13+ devices?
- [ ] Are your permission launchers registered in `onCreate()` (before STARTED state)?
- [ ] Does your app check permissions before opening camera or gallery?
- [ ] When permission is granted, does the camera/gallery open correctly?
- [ ] When permission is denied, do you show a helpful message to the user?
- [ ] (Optional) When permanently denied, do you offer to open Settings?

---

## FileProvider

- [ ] Did you add the FileProvider entry inside `<application>` in AndroidManifest.xml?
- [ ] Does your authority match `${applicationId}.fileprovider` (NOT a hardcoded package name)?
- [ ] Did you create `res/xml/file_paths.xml`?
- [ ] Does your file_paths.xml include `<external-files-path>` for `Pictures/`?
- [ ] Do you use `FileProvider.getUriForFile()` when creating the camera image URI?
- [ ] Does your app run without `FileUriExposedException` crashes?

---

## Camera Implementation

- [ ] Did you register a `TakePicture` launcher using `registerForActivityResult`?
- [ ] Does your `createImageUri()` create a unique filename each time?
- [ ] Are images saved to `getExternalFilesDir(DIRECTORY_PICTURES)`?
- [ ] Does tapping the camera button check permission first?
- [ ] Does the camera app launch successfully?
- [ ] Is the captured image URI saved to a class variable?
- [ ] Is the image displayed in the preview after capture?
- [ ] If the user cancels the camera, does the app handle it without crashing?

---

## Gallery Implementation

- [ ] Did you implement a gallery picker (GetContent or PickVisualMedia)?
- [ ] Does your code check Android version for permission requirements?
- [ ] On Android 13+, does the Photo Picker work without requesting READ_EXTERNAL_STORAGE?
- [ ] Does tapping the gallery button open the picker?
- [ ] Is the selected image URI received in your callback?
- [ ] Is the image displayed in the preview after selection?
- [ ] If the user cancels the picker, does the app handle it gracefully?
- [ ] Have you tested on API 24, 30, and 33 emulators?

---

## Image Handling

- [ ] Did you implement a method to load a Bitmap from URI?
- [ ] Did you implement `calculateInSampleSize()` to prevent memory issues?
- [ ] Does your scaled bitmap load stay within ~1024×1024 maximum?
- [ ] Does the ImageView display the loaded bitmap correctly?
- [ ] Is `selectedImageUri` (or `currentImageUri`) saved for later use?
- [ ] Do you wrap bitmap loading in try-catch for IOException?
- [ ] Have you tested with a large image (4MB+) without OutOfMemoryError?

---

## UI and State Management

- [ ] Does the ImageView show the captured or selected image?
- [ ] Is a placeholder image shown when no image is selected?
- [ ] If you rotate the device, does the image preview survive?
- [ ] Did you save the URI in `onSaveInstanceState()`?
- [ ] Did you restore the URI in `onCreate()` if `savedInstanceState` is not null?
- [ ] Did you add Log statements to help with debugging?
- [ ] Do you show Toast messages for user feedback (errors, cancellations)?

---

## Testing Completed

- [ ] Did you test camera capture on the emulator?
- [ ] Did you test camera capture on a physical device (if available)?
- [ ] Did you test gallery selection on the emulator?
- [ ] Did you test gallery selection on a physical device?
- [ ] Did you test the permission-grant flow?
- [ ] Did you test the permission-deny flow?
- [ ] Did you test screen rotation with an image loaded?
- [ ] Did you test canceling the camera?
- [ ] Did you test canceling the gallery picker?
- [ ] Did you test loading a large image (4MB+)?
- [ ] Did you test on an API 24 emulator?
- [ ] Did you test on an API 30+ emulator?

---

## Code Quality

- [ ] Does your project compile without errors?
- [ ] Does your app run without runtime crashes?
- [ ] Do you check for null before using the URI?
- [ ] Do you wrap I/O operations in try-catch?
- [ ] Did you add Log statements for key events?
- [ ] Did you add comments to explain complex logic?
- [ ] Are your variable/method names clear and consistent?
- [ ] Did you avoid hardcoded strings (use `@string/` resources)?

---

## Evidence Collection

- [ ] Did you take a screenshot of the camera permission dialog?
- [ ] Did you take a screenshot showing a captured image displayed?
- [ ] Did you take a screenshot of the gallery picker?
- [ ] Did you take a screenshot showing a selected image displayed?
- [ ] Did you record a video of the camera flow (permission → capture → display)?
- [ ] Did you record a video of the gallery flow (open → select → display)?
- [ ] Did you capture a Logcat screenshot showing successful execution?
- [ ] Do your Git commits show progressive work throughout the week?

---

## Documentation

- [ ] Did you complete the learning notes for this week?
- [ ] Did you attempt at least 5 of the 7 exercises?
- [ ] Did you complete the build task?
- [ ] Did you submit your reflection?
- [ ] Did you complete the quiz (score ≥ 8/10)?

---

## Understanding Verification

Can you explain these concepts without looking at notes?

- [ ] What is the difference between normal and dangerous permissions?
- [ ] Why is FileProvider required for camera intents on Android 7+?
- [ ] What is the difference between a `file://` URI and a `content://` URI?
- [ ] How does `inSampleSize` reduce memory usage when loading bitmaps?
- [ ] Why is Activity Result API preferred over `onActivityResult()`?
- [ ] When should you use app-specific storage vs shared storage?

---

## Completion Criteria

You may proceed to Week 04 only when:

1. All checklist items answered "yes"
2. App captures and selects images without crashes
3. Permissions handled correctly on grant and deny
4. Quiz score ≥ 8/10
5. Evidence package is complete
6. You can explain concepts independently

**If any item is incomplete, do not proceed. Week 04 requires functional image input.**

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
