# Week 03 Exercises: Camera, Gallery & Image Handling

## Exercise Overview

Complete these 7 exercises to master camera integration, gallery picking, runtime permissions, and image handling. Each exercise builds practical skills needed for LeafGuard's image input system.

---

## Exercise 1: Permission Flow Implementation

### Goal
Implement complete permission handling for camera access including rationale and settings redirect.

### Tasks

1. Add CAMERA permission to AndroidManifest.xml
2. Implement permission check using ContextCompat.checkSelfPermission
3. Create ActivityResultLauncher for permission request
4. Handle permission granted case (open camera)
5. Handle permission denied case (show explanation)
6. Implement "Don't ask again" handling (redirect to Settings)
7. Create rationale dialog explaining why camera is needed

### Deliverables

- Complete permission handling code
- Screenshot of permission dialog
- Screenshot of rationale dialog
- Screenshot of settings redirect
- Test log showing all scenarios

### Expected Time
60 minutes

---

## Exercise 2: Camera Intent Integration

### Goal
Implement camera capture using TakePicture contract and FileProvider.

### Tasks

1. Add FileProvider to AndroidManifest.xml
2. Create file_paths.xml configuration
3. Implement createImageFile() method
4. Register TakePicture launcher
5. Launch camera with URI
6. Handle capture success (save URI)
7. Handle capture cancel (show message)
8. Test on physical device or emulator

### Deliverables

- Working camera capture
- FileProvider properly configured
- Screenshot of captured image saved
- Code showing URI creation

### Expected Time
75 minutes

---

## Exercise 3: Gallery Picker Implementation

### Goal
Implement gallery image picker with support for both modern Photo Picker and legacy Intent.

### Tasks

1. Implement Photo Picker for Android 11+
2. Implement legacy ACTION_PICK for Android 6-10
3. Add version check (Build.VERSION.SDK_INT)
4. Handle storage permission for legacy devices
5. Register PickVisualMedia launcher
6. Handle selected image URI
7. Test both code paths

### Deliverables

- Unified gallery function working on all API levels
- Code showing version branching
- Screenshots of Photo Picker (API 30+)
- Screenshots of legacy picker (API 24-29)

### Expected Time
60 minutes

---

## Exercise 4: URI to Bitmap Conversion

### Goal
Load bitmap from URI with proper scaling to prevent OutOfMemoryError.

### Tasks

1. Implement loadBitmapFromUri() basic version
2. Implement calculateInSampleSize() method
3. Implement loadScaledBitmap() with inSampleSize
4. Test with large image (4000x3000+)
5. Compare memory usage: full vs scaled
6. Handle IOException gracefully
7. Display bitmap in ImageView

### Deliverables

- Complete bitmap loading code
- Test showing memory savings
- Screenshots of before/after scaling
- Log output showing sample sizes

### Expected Time
90 minutes

---

## Exercise 5: Image Preview and Persistence

### Goal
Display selected/captured image in ImageView and persist URI for later use.

### Tasks

1. Update ScanActivity layout with ImageView
2. Implement displayImage(Uri) method
3. Save currentImageUri as class variable
4. Enable "Analyze" button when image loaded
5. Handle rotation (save URI in onSaveInstanceState)
6. Test image persistence across rotation
7. Show placeholder when no image selected

### Deliverables

- ImageView displaying captured/selected image
- State preserved on rotation
- Screenshot before/after rotation
- Code showing state saving

### Expected Time
45 minutes

---

## Exercise 6: Complete ScanActivity Integration

### Goal
Integrate all components into working ScanActivity with error handling.

### Tasks

1. Combine camera and gallery implementations
2. Add permission checks for both features
3. Implement error handling (null checks, try-catch)
4. Add loading indicators during operations
5. Handle edge cases (cancel, permission denial)
6. Add Log statements for debugging
7. Test complete flow: permission → capture/pick → display

### Deliverables

- Fully functional ScanActivity
- Complete error handling
- Video demonstrating full flow
- Logcat showing complete execution

### Expected Time
120 minutes

---

## Exercise 7: Multi-API Level Testing

### Goal
Test camera and gallery on multiple Android versions to ensure compatibility.

### Tasks

1. Test on API 24 (Android 7.0) emulator
2. Test on API 30 (Android 11) emulator
3. Test on API 33 (Android 13) emulator
4. Test on physical device (note API level)
5. Document differences in behavior
6. Verify permissions work on all levels
7. Create compatibility matrix

### Deliverables

- Compatibility test report
- Screenshots from each API level
- Table showing behavior differences
- Notes on version-specific issues

### Expected Time
90 minutes

---

## Bonus Exercise: EXIF Orientation Handling

### Goal
Handle image rotation based on EXIF metadata.

### Tasks

1. Add ExifInterface dependency
2. Read orientation from EXIF data
3. Rotate bitmap based on orientation
4. Test with images from different devices
5. Compare before/after orientation fix

### Deliverables

- Code handling EXIF rotation
- Screenshots showing correction
- Test with multiple orientations

### Expected Time
60 minutes

---

## Submission

Save deliverables to `evidence/week-03/exercises/` and commit with message: "Week 03: Complete exercises 1-7"


<!-- NAV_FOOTER_START -->

---

## 📚 Week 03 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| **3** | **exercises.md** ← *You are here* | **Practice Exercises** |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Theory & Learning Notes](learning-notes.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Practice Exercises** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Build Implementation Guide →](build-task.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 02: Android Basics & UI](../week-02-android-basics-ui/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 04: FastAPI Backend ➡](../week-04-fastapi-backend/README.md) |

---
