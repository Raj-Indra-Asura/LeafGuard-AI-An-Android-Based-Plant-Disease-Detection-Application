# Week 03: Image Input With Camera and Gallery

## Week 03 Mindset

Week 02 gave you a runnable Android navigation shell. The Scan screen was only a placeholder. Week 03 turns that placeholder into the first real product feature:

> The user can provide a plant-leaf image by taking a photo or choosing one from the device gallery, and the app shows that image on the Scan screen.

This week still does **not** analyze the image. It does **not** call the backend. It does **not** save history. It does **not** run AI.

Week 03 answers one question only:

> Can the app safely receive an image from the user and preview it?

---

## Product State After Week 03

**Cumulative product contribution: 25%**

By the end of Week 03, the app should:

- open the Scan screen from Home
- request camera permission when needed
- launch the camera app and receive a captured image
- open the gallery picker and receive a selected image
- display the selected/captured image in the Scan screen preview
- handle permission denial and cancellation without crashing
- remember the selected image after simple screen recreation when possible

### What the product can do after Week 03

- Accept real user image input.
- Show the chosen image on screen.
- Prove the first user-content feature works.

### What the product still cannot do

- Upload the image to a backend. That starts in Week 05 after the Week 04 backend exists.
- Predict disease. That starts with cloud prediction in Week 06 and offline prediction in Week 09.
- Save scan history. That starts in Week 07.
- Show disease guidance from XML. That starts in Week 08.

---

## Repository State After Week 03

Week 03 keeps the Week 02 navigation shell and upgrades the Scan screen into the first real user-input feature. The repository should now show where image input lives, but prediction logic is still outside this week.

### Structure to browse after this week

- `android-app-kotlin/app/src/main/java/com/leafguard/ScanActivity.kt` owns camera, gallery, URI, and preview behavior.
- `android-app-kotlin/app/src/main/res/layout/activity_scan.xml` contains the image preview and camera/gallery buttons.
- `android-app-kotlin/app/src/main/AndroidManifest.xml` includes camera permission and the FileProvider declaration when camera capture writes to an app file.
- `android-app-kotlin/app/src/main/res/xml/file_provider_paths.xml` defines which app file paths can be shared with the camera app.
- `android-app-kotlin/app/src/main/res/values/strings.xml` contains user-facing permission, cancel, and image-input text.
- The Java twin under `android-app/` should contain the same camera/gallery behavior in Java.

### Files you should create or update this week

Week 03 adds **1 new file**, rewrites **2 files**, extends **2 files**, and leaves the other **12 Week 02 files untouched**. Total: **17 required Android files**.

| Change | File | Size |
|---|---|---|
| NEW | `res/xml/file_provider_paths.xml` | 6 lines |
| REWRITTEN | `ScanActivity.kt` | 12 → 132 lines |
| REWRITTEN | `res/layout/activity_scan.xml` | 25 → 60 lines |
| EXTENDED | `AndroidManifest.xml` | 37 → 53 lines (camera permission + FileProvider) |
| EXTENDED | `res/values/strings.xml` | 25 → 35 lines (20 → 29 strings) |
| UNCHANGED | `app/build.gradle` | 40 lines — **no new dependency is needed this week** |

Also update outside the app project:

- `docs/evidence/week-03/` screenshots or screen recordings for camera, gallery, denial, and cancellation.

> **Exact contents of every changed file** — full code, per-block line counts, why each line exists, and what must still not exist — are in [`learning-notes.md` section 11](learning-notes.md#11-end-of-week-03-file-inventory-exact-files-exact-code-exact-size).

### What this repository state can do

- Open the Scan screen from the app shell.
- Ask for camera permission only when needed.
- Launch camera capture and gallery selection.
- Store the selected image URI long enough to preview it in the Scan screen.
- Recover from permission denial or user cancellation without crashing.

### What this repository state cannot do

- It cannot analyze the selected image.
- It cannot upload the image to a server.
- It cannot save the scan to history.
- It cannot show treatment advice or run a TensorFlow Lite model.

---

## New Words This Week

| Term | Beginner Definition |
|---|---|
| Camera permission | User approval that lets the app ask the device camera to take a photo. |
| Runtime permission | A permission requested while the app is running, not only when installed. |
| Gallery picker | A system screen that lets the user choose an existing image. |
| URI | A reference to content on Android, such as an image selected by the user. |
| FileProvider | Android helper that safely shares an app-owned file with another app, such as the camera. |
| Activity Result API | Modern Android system for launching another app/screen and receiving a result. |
| Preview | Showing the selected or captured image inside the app UI. |
| Cancellation | User backs out of camera/gallery; the app should keep working. |

---

## Weekly Objective

By the end of Week 03, you will be able to:

1. Explain why camera access needs permission.
2. Add the camera permission and FileProvider safely.
3. Use `ActivityResultContracts.TakePicture` for camera capture.
4. Use `ActivityResultContracts.GetContent` for gallery image selection.
5. Store the selected image URI in `ScanActivity`.
6. Display that URI in an `ImageView` preview.
7. Validate only image input behavior.

---

## What You Will Build

You will upgrade `ScanActivity` from a placeholder into an image input screen.

| Week 02 Screen | Week 03 Upgrade | Future Boundary |
|---|---|---|
| `ScanActivity` placeholder | Camera button, gallery button, image preview | Detection waits for later weeks |
| `MainActivity` Home | Still navigates to Scan | No camera logic here |
| `ResultActivity` placeholder | No real change required | Real result waits for networking/model work |

Keep camera/gallery logic in `ScanActivity`, because Week 02 made Scan the screen where image input belongs.

---

## Kotlin First

Use the Kotlin primary track:

```text
android-app-kotlin/
```

The Java app remains a secondary comparison track.

---

## Suggested 7-Day Plan

| Day | Focus | Output |
|---|---|---|
| Day 1 | Understand permissions and URI idea | Notes and exercise 1 |
| Day 2 | Add manifest permission and FileProvider config | Project still builds |
| Day 3 | Build Scan layout with preview and buttons | Scan UI visible |
| Day 4 | Add gallery picker | Gallery image previews |
| Day 5 | Add camera capture | Captured image previews |
| Day 6 | Handle denial/cancel/state | No crash on edge cases |
| Day 7 | Validate and collect evidence | Demo, quiz, reflection |

---

## Milestone Demo

At the end of Week 03, demonstrate this:

1. Launch the app.
2. Open Scan from Home.
3. Tap gallery, choose an image, and show it in the preview.
4. Tap camera, take a photo, and show it in the preview.
5. Deny camera permission or cancel camera/gallery and show the app does not crash.
6. Explain that detection/upload/history are future weeks.

This proves the Week 03 image-input slice is complete.

---

## Week 03 File Order

Complete these files in this order:

| Step | File | Purpose |
|---:|---|---|
| 1 | `README.md` | Understand the Week 03 product slice. |
| 2 | `learning-notes.md` | Learn image input concepts from zero. |
| 3 | `exercises.md` | Practise permission, URI, and preview ideas. |
| 4 | `build-task.md` | Build image input in `ScanActivity`. |
| 5 | `validation-checklist.md` | Prove camera/gallery input works. |
| 6 | `quiz.md` | Check understanding. |
| 7 | `reflection.md` | Explain what you built and why. |

Move to Week 04 only after the image-input demo passes.

<!-- NAV_FOOTER_START -->

---

## 📈 Product State After This Week

**Cumulative product completion: 25%** *(official model: [PRODUCT_PROGRESS_MAP.md](../../PRODUCT_PROGRESS_MAP.md))*

- **Your app can now…** capture a leaf photo or pick one from gallery and show it in the Scan preview while handling denial/cancel safely.
- **Your app still cannot…** analyze, upload, save, or diagnose the image yet. Week 04 starts the backend prediction service.
- **Applies equally to both tracks:** Kotlin (`android-app-kotlin/`, primary) and Java (`android-app/`, secondary).

### Cumulative Repository State After Week 03

This snapshot includes Weeks 01-02 and adds the image-input files. The Java track should keep the same behavior under `android-app/` with `.java` Activity files.

```text
LeafGuard-AI/
|-- README.md
|-- START_HERE.md
|-- LEARNING_PATH.md
|-- PRODUCT_PROGRESS_MAP.md
|-- progress-tracker.md
|-- roadmap/
|   |-- week-01-project-understanding/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|   |-- week-02-android-basics-ui/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|   `-- week-03-camera-gallery/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|-- docs/evidence/
|   |-- week-01/{planning evidence files}
|   |-- week-02/{launch and navigation evidence}
|   `-- week-03/{README.md, camera proof, gallery proof, permission-denial proof, cancellation proof}
|-- android-app-kotlin/
|   |-- build.gradle, settings.gradle, gradle.properties   (unchanged from Week 02)
|   `-- app/
|       |-- build.gradle                       (40 lines, UNCHANGED - no new dependency)
|       `-- src/main/
|           |-- AndroidManifest.xml            (53 lines, EXTENDED: camera permission + FileProvider)
|           |-- java/com/leafguard/
|           |   |-- MainActivity.kt            (34, unchanged, real)
|           |   |-- ScanActivity.kt            (132, REWRITTEN, real image input)
|           |   |-- ResultActivity.kt          (12, unchanged, placeholder)
|           |   |-- HistoryActivity.kt         (12, unchanged, placeholder)
|           |   |-- DiseaseLibraryActivity.kt  (12, unchanged, placeholder)
|           |   `-- SettingsActivity.kt        (12, unchanged, placeholder)
|           `-- res/
|               |-- layout/
|               |   |-- activity_main.xml      (56, unchanged)
|               |   |-- activity_scan.xml      (60, REWRITTEN: preview + 2 buttons)
|               |   `-- activity_result.xml, activity_history.xml, activity_disease_library.xml, activity_settings.xml (25 each, unchanged placeholders)
|               |-- values/{strings.xml (35, EXTENDED), colors.xml (9), themes.xml (9)}
|               `-- xml/file_provider_paths.xml   (6 lines, NEW)
`-- android-app/ (Java mirror with the same camera/gallery behavior)
```

Still not present after Week 03: `assets/`, `res/menu/`, custom `res/drawable/`, `network_security_config.xml`, and the `network/`, `database/`, `ml/`, `utils/`, `ui/` packages.

---

## 📚 Week 03 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| **1** | **README.md** ← *You are here* | **Week Overview & Objectives** |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

*(Start of week)* &nbsp;&nbsp;|&nbsp;&nbsp; **Week Overview & Objectives** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Theory & Learning Notes →](learning-notes.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 02: Android Basics & UI](../week-02-android-basics-ui/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 04: FastAPI Backend ➡](../week-04-fastapi-backend/README.md) |

---
