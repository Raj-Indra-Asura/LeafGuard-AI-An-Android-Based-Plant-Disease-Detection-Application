# Week 03 Quiz: Camera, Gallery & Image Handling

## Instructions

- **Total Questions:** 12
- **Passing Score:** 10/12 (83%)
- **Open Book:** Yes, refer to materials
- **Save answers in:** `evidence/week-03/quiz-answers.md`

---

## Multiple Choice (1 point each)

### Question 1

Which permission is required for camera access?

A) android.permission.WRITE_EXTERNAL_STORAGE
B) android.permission.CAMERA
C) android.permission.READ_MEDIA_IMAGES
D) No permission needed, camera is always available

**Your Answer:** ___

---

### Question 2

What is the purpose of FileProvider?

A) To provide files over HTTP
B) To wrap file URIs as secure content URIs
C) To cache files for faster access
D) To compress images before sharing

**Your Answer:** ___

---

### Question 3

What does `inSampleSize = 4` do when loading a bitmap?

A) Loads every 4th pixel
B) Reduces width and height to 1/4 (memory = 1/16)
C) Increases image quality by 4x
D) Loads image 4 times faster

**Your Answer:** ___

---

### Question 4

Which is the modern way to handle activity results?

A) onActivityResult()
B) startActivityForResult()
C) ActivityResultLauncher with contracts
D) setResult() and getResult()

**Your Answer:** ___

---

### Question 5

Where should camera-captured images be saved in LeafGuard?

A) /storage/emulated/0/DCIM/
B) getExternalFilesDir(Environment.DIRECTORY_PICTURES)
C) /sdcard/Pictures/
D) Environment.getExternalStorageDirectory()

**Your Answer:** ___

---

### Question 6

What is scoped storage (Android 10+)?

A) Storage limited to 100MB
B) Apps have unrestricted access to shared storage
C) Apps have limited access to shared storage, full access to app-specific directory
D) All storage requires permission

**Your Answer:** ___

---

## True/False (1 point each)

### Question 7

**Statement:** Photo Picker requires READ_MEDIA_IMAGES permission on Android 11+.

**Your Answer:** True / False

**Explanation:** ___

---

### Question 8

**Statement:** File URIs (file://) are deprecated and cause FileUriExposedException on Android 7+.

**Your Answer:** True / False

---

## Short Answer (2 points each)

### Question 9

**Question:** Explain why you must scale large bitmaps before loading. What happens if you don't? Provide example memory calculation.

**Your Answer (5-7 sentences):**
```








```

---

### Question 10

**Question:** Describe the complete flow from user tapping "Camera" button to image displayed in ImageView. Include permission check, file creation, capture, and display steps.

**Your Answer:**
```
Step 1:


Step 2:


Step 3:


Step 4:


Step 5:


Step 6:
```

---

## Code Analysis (2 points each)

### Question 11

**Given this code, identify the error:**

```java
private void openCamera() {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "image.jpg");
    Uri imageUri = Uri.fromFile(imageFile);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    startActivity(intent);
}
```

**What's wrong? How to fix?**

**Your Answer:**
```
Error:


Why this causes a problem:


Fix:
```

---

### Question 12

**What is missing in this permission check?**

```java
private void checkCameraPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
        openCamera();
    } else {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
    }
}
```

**Your Answer:**
```
Missing:


Why it's important:


Improved code:
```

---

## Self-Grading

Check answers, calculate score: ___/14

**Pass (10+/12):** Proceed to Week 04
**Retake (<10/12):** Review weak areas, retake quiz

**Submit `evidence/week-03/quiz-answers.md` and commit: "Week 03: Complete quiz"**


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
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| **6** | **quiz.md** ← *You are here* | **Knowledge Assessment Quiz** |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Validation & Verification](validation-checklist.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Knowledge Assessment Quiz** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Reflection & Consolidation →](reflection.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 02: Android Basics & UI](../week-02-android-basics-ui/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 04: FastAPI Backend ➡](../week-04-fastapi-backend/README.md) |

---
