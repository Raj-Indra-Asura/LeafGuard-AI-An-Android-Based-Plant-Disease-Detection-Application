# Week 03 Quiz: Camera, Gallery, URI, and Preview

## Instructions

Answer after completing the learning notes and build task. This quiz checks Week 03 understanding only.

Passing score: 14 out of 18.

---

## Multiple Choice

### 1. What does Week 03 add to the app?

A) Backend prediction
B) Image input and preview in ScanActivity
C) Room database history
D) Final APK signing

Answer: ____

### 2. Which permission is needed before using the camera?

A) `android.permission.CAMERA`
B) `android.permission.INTERNET`
C) `android.permission.ACCESS_FINE_LOCATION`
D) No permission is ever needed

Answer: ____

### 3. What is a URI in Week 03?

A) A Gradle plugin
B) A safe reference/address for an image
C) A database table
D) A disease label

Answer: ____

### 4. Why is FileProvider used for camera capture?

A) To train the model
B) To convert app-owned files into safe `content://` URIs
C) To upload images to FastAPI
D) To save Room records

Answer: ____

### 5. What does `ActivityResultContracts.TakePicture()` do?

A) Opens camera and writes the captured photo to a provided URI
B) Opens the backend server
C) Reads disease labels
D) Creates an Activity layout

Answer: ____

### 6. What does `ActivityResultContracts.GetContent()` do in this week?

A) Opens a picker so the user can choose an image
B) Saves scan history
C) Runs TensorFlow Lite
D) Builds the APK

Answer: ____

### 7. Where should the Week 03 image input logic live after Week 02?

A) `MainActivity`
B) `ScanActivity`
C) `HistoryActivity`
D) `backend-api/main.py`

Answer: ____

### 8. Which behavior must wait for a future week?

A) Showing selected image preview
B) Handling camera cancellation
C) Faking a disease prediction
D) Storing selected URI in a variable

Answer: ____

---

## True or False

### 9. If the user cancels the gallery picker, the app should crash so the bug is visible.

Answer: ____

### 10. The FileProvider authority in code and manifest must match.

Answer: ____

### 11. `selectedImageUri` can be null before the user chooses or captures an image.

Answer: ____

### 12. Week 03 should validate backend image upload.

Answer: ____

### 13. Image preview proves that the app received image input.

Answer: ____

---

## Short Answer

### 14. Explain the camera flow from button tap to preview in 4 to 6 steps.

Answer:

### 15. Explain the gallery flow from button tap to preview in 3 to 5 steps.

Answer:

### 16. Why is FileProvider safer than a raw `file://` path?

Answer:

### 17. Name three edge cases Week 03 should handle.

Answer:

### 18. Name three things Week 03 should not implement yet.

Answer:

---

## Answer Key

Check after answering.

1. B
2. A
3. B
4. B
5. A
6. A
7. B
8. C
9. False
10. True
11. True
12. False
13. True

Short answers may vary. They should mention permission check, FileProvider camera URI, gallery URI, preview update, cancellation/denial handling, and future-week boundaries.

## Readiness Rule

If you score below 14, reread `learning-notes.md`, revisit the build task, and retake the quiz before moving to Week 04.