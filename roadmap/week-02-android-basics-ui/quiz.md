# Week 02 Quiz: Android UI Navigation Shell

## Instructions

Answer after completing the learning notes and build task. This quiz checks Week 02 understanding only.

Passing score: 14 out of 18.

---

## Multiple Choice

### 1. What does Week 02 build?

A) A complete disease detector
B) A runnable Android UI navigation shell
C) A FastAPI backend
D) A Room database

Answer: ____

### 2. What is an Activity?

A) A Gradle dependency
B) One Android screen
C) A database table
D) A model label

Answer: ____

### 3. What does `setContentView(R.layout.activity_main)` do?

A) Deletes the layout file
B) Connects the Activity to the XML layout
C) Builds the APK
D) Opens the backend

Answer: ____

### 4. Where should user-visible strings usually go?

A) `strings.xml`
B) `colors.xml`
C) `settings.gradle`
D) `labels.txt`

Answer: ____

### 5. What does an explicit Intent do in Week 02?

A) Opens another Activity in the app
B) Trains the AI model
C) Creates a database row
D) Uploads a file

Answer: ____

### 6. Which file declares Activities to Android?

A) `AndroidManifest.xml`
B) `README.md`
C) `colors.xml`
D) `model.tflite`

Answer: ____

### 7. Which behavior belongs in Week 03, not Week 02?

A) Home button opens Scan placeholder
B) Activity loads XML layout
C) Real camera/gallery image input
D) App launches on emulator

Answer: ____

### 8. Why are placeholder screens useful?

A) They pretend the app is finished
B) They prove navigation and screen structure before real features are added
C) They replace future implementation work
D) They make Gradle unnecessary

Answer: ____

---

## True or False

### 9. Week 02 should validate backend upload.

Answer: ____

### 10. A layout file describes what a screen looks like.

Answer: ____

### 11. `MainActivity` should be the only Activity with the launcher intent filter.

Answer: ____

### 12. Hardcoded visible text is preferred because it is easier to translate.

Answer: ____

### 13. Internal screens usually use `android:exported="false"`.

Answer: ____

---

## Short Answer

### 14. In one sentence, explain how Week 02 grows from Week 01.

Answer:

### 15. Name three files or folders used in Week 02 and explain their purpose.

Answer:

### 16. Explain this code in your own words:

```kotlin
startActivity(Intent(this, ScanActivity::class.java))
```

Answer:

### 17. Name three things Week 02 must not implement yet.

Answer:

### 18. What evidence proves Week 02 is complete?

Answer:

---

## Answer Key

Check after answering.

1. B
2. B
3. B
4. A
5. A
6. A
7. C
8. B
9. False
10. True
11. True
12. False
13. True

Short answers may vary. They should mention the Week 01 screen map, Android files, Intent navigation, future-week boundaries, and evidence of a runnable navigation shell.

## Readiness Rule

If you score below 14, reread `learning-notes.md`, revisit the build task, and retake the quiz before moving to Week 03.

<!-- NAV_FOOTER_START -->

---

## 📚 Week 02 — Navigation

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
| [⬅ Week 01: Project Understanding](../week-01-project-understanding/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 03: Camera & Gallery ➡](../week-03-camera-gallery/README.md) |

---
