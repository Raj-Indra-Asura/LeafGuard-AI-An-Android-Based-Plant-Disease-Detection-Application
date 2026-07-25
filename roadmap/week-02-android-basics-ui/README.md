# Week 02: Android Project and UI Navigation Shell

## Week 02 Mindset

Week 01 gave you the product idea, user journey, screen map, system sketch, and week growth map. Week 02 turns that plan into the first runnable Android app slice.

This week does **not** build disease detection. It does **not** open the real camera yet. It does **not** save history, call a backend, parse XML, or run AI. Those are future weeks.

Week 02 builds the shell that future features will live inside:

> Open the app -> see the Home screen -> move to placeholder screens -> return safely -> prove the project builds and runs.

That is the correct 15% product state.

---

## Product State After Week 02

**Cumulative product contribution: 15%**

By the end of Week 02, the app should:

- open on an emulator or physical Android device
- show a Home screen based on the Week 01 screen map
- contain placeholder screens for the main future features
- navigate between screens using explicit Intents
- use resource files for visible text and colors
- build successfully from Android Studio or Gradle

### What the product can do after Week 02

- Launch as a real Android app.
- Show a simple UI skeleton.
- Move between planned screens.
- Prove the first Android code slice works.

### What the product still cannot do

- Capture a real photo. That starts in Week 03.
- Upload an image to a backend. That starts in Weeks 04-05.
- Return a real prediction. That starts in Week 06 and improves later.
- Save scan history. That starts in Week 07.
- Show the real XML disease library. That starts in Week 08.
- Work offline with TensorFlow Lite. That starts in Week 09.

---

## Repository State After Week 02

Week 02 keeps the Week 01 planning evidence and adds the first runnable Android app shell. From this point onward, the repository is no longer only a learning plan; it also contains a buildable mobile project.

### Structure to browse after this week

- `android-app-kotlin/` is the primary app project to open in Android Studio.
- `android-app/` is the Java twin that should mirror the same feature set.
- `android-app-kotlin/app/build.gradle`, `settings.gradle`, and root `build.gradle` define the Android build.
- `android-app-kotlin/app/src/main/AndroidManifest.xml` declares the app and its Activities.
- `android-app-kotlin/app/src/main/java/com/leafguard/` contains screen classes such as `MainActivity`, `ScanActivity`, `ResultActivity`, `HistoryActivity`, `DiseaseLibraryActivity`, `SettingsActivity`, and optionally `AnalyticsActivity`.
- `android-app-kotlin/app/src/main/res/layout/` contains the matching XML layouts such as `activity_main.xml`, `activity_scan.xml`, `activity_result.xml`, `activity_history.xml`, `activity_disease_library.xml`, and `activity_settings.xml`.
- `android-app-kotlin/app/src/main/res/values/` contains shared resources such as `strings.xml`, `colors.xml`, and `themes.xml`.

### Files you should create or update this week

Week 02 requires exactly **16 Android source/resource files** (plus the generated Gradle scaffold):

| Group | Count | Files |
|---|---:|---|
| Manifest | 1 | `AndroidManifest.xml` |
| Kotlin Activities | 6 | `MainActivity.kt` (real navigation) + 5 placeholder Activities, 12 lines each |
| XML layouts | 6 | `activity_main.xml` (real UI) + 5 placeholder layouts, 25 lines each |
| Value resources | 3 | `strings.xml` (20 strings), `colors.xml` (6 colors), `themes.xml` (1 style) |

Optional: `AnalyticsActivity.kt` + `activity_analytics.xml` if your Week 01 screen map included an Analytics screen.

Also update outside the app project:

- `docs/evidence/week-02/` screenshots showing launch and navigation.
- `progress-tracker.md` after the Week 02 validation passes.

> **Exact contents of every one of these files** — full code, line counts, what is real, what is a placeholder, and what must not exist yet — are in [`learning-notes.md` section 10](learning-notes.md#10-end-of-week-02-file-inventory-exact-files-exact-code-exact-size).

### What this repository state can do

- Build and launch the Android app on an emulator or device.
- Show a Home screen and planned placeholder screens.
- Navigate between screens using explicit Intents.
- Prove the project structure, Gradle setup, resources, and Activity registration work.

### What this repository state cannot do

- It cannot use real camera or gallery input.
- It cannot send an image to a backend.
- It cannot display a real prediction.
- It cannot save history, parse the disease library, or run offline AI.

---

## New Words This Week

| Term | Beginner Definition |
|---|---|
| Android Studio | The main program used to create, edit, build, and run Android apps. |
| Project | The full folder containing app code, resources, Gradle files, and settings. |
| Activity | One Android screen. For beginners, think of it as one app page. |
| XML layout | A file that describes what a screen looks like. |
| View | One visible UI element, such as text, button, image, or card. |
| Intent | A message that asks Android to open another screen or perform an action. |
| Manifest | The file where Android screens and permissions are declared. |
| Gradle | The build system that compiles code, downloads libraries, and creates the APK. |
| Resource | A reusable app file, such as a string, color, image, or layout. |
| Emulator | A virtual Android phone running on your computer. |

---

## Weekly Objective

By the end of Week 02, you will be able to:

1. Open or create the LeafGuard Android project in the Kotlin track.
2. Explain the main folders: Kotlin source, layouts, values, manifest, Gradle.
3. Create simple placeholder screens from the Week 01 screen map.
4. Use XML layouts to display text and buttons.
5. Use Kotlin click listeners and explicit Intents for navigation.
6. Run the app and validate only the UI shell.

---

## What You Will Build

Build a beginner navigation shell with these screen ideas:

| Week 01 Screen Idea | Week 02 Android Slice | Purpose This Week |
|---|---|---|
| Home | `MainActivity` | App opens here; user can navigate. |
| Scan | `ScanActivity` placeholder | Shows where image input will be added in Week 03. |
| Result | `ResultActivity` placeholder | Shows where prediction results will appear later. |
| History | `HistoryActivity` placeholder | Shows empty history state until Week 07. |
| Disease Library | `DiseaseLibraryActivity` placeholder | Shows library placeholder until Week 08. |
| Settings/About | `SettingsActivity` placeholder | Shows app/course info or simple settings placeholder. |

Optional future placeholder if your screen map included it:

| Optional Screen | Purpose This Week |
|---|---|
| `AnalyticsActivity` | Placeholder only; real data comes after history exists. |

Do not add real camera, database, backend, or model code in Week 02.

---

## Kotlin First

Use the Kotlin app path unless your instructor explicitly requires Java:

```text
android-app-kotlin/
```

The Java twin exists for comparison:

```text
android-app/
```

Week 02 explanations focus on Kotlin because it is the primary track.

---

## Suggested 7-Day Plan

| Day | Focus | Output |
|---|---|---|
| Day 1 | Android Studio and project orientation | Project opens and runs default screen |
| Day 2 | Project folders and resources | `strings.xml`, `colors.xml`, basic theme understood |
| Day 3 | Home screen layout | `MainActivity` displays title and navigation buttons |
| Day 4 | Placeholder screens | Scan, Result, History, Library, Settings/About screens exist |
| Day 5 | Intent navigation | Buttons open the correct screens |
| Day 6 | Build and run checks | App builds, launches, and navigates without crashes |
| Day 7 | Validation and evidence | Screenshots, checklist, quiz, reflection saved |

---

## Milestone Demo

At the end of Week 02, demonstrate this:

1. Launch LeafGuard AI on an emulator or device.
2. Show the Home screen.
3. Tap each navigation button.
4. Confirm each placeholder screen opens.
5. Use the Back button to return safely.
6. Explain which future week will fill each placeholder with real behavior.

This proves the Week 02 UI shell is complete.

---

## Week 02 File Order

Complete these files in this order:

| Step | File | Purpose |
|---:|---|---|
| 1 | `README.md` | Understand the Week 02 product slice. |
| 2 | `learning-notes.md` | Learn Android basics from zero. |
| 3 | `exercises.md` | Practise folders, layout, and navigation. |
| 4 | `build-task.md` | Build the navigation shell. |
| 5 | `validation-checklist.md` | Prove the Week 02 slice works. |
| 6 | `quiz.md` | Check understanding. |
| 7 | `reflection.md` | Explain what you built and why. |

After this, update the progress tracker and move to Week 03 only after validation passes.

<!-- NAV_FOOTER_START -->

---

## 📈 Product State After This Week

**Cumulative product completion: 15%** *(official model: [PRODUCT_PROGRESS_MAP.md](../../PRODUCT_PROGRESS_MAP.md))*

- **Your app can now…** launch on an emulator or device and navigate through the planned UI shell.
- **Your app still cannot…** take photos, analyze images, call a backend, save history, or run AI. Week 03 adds image input.
- **Applies equally to both tracks:** Kotlin (`android-app-kotlin/`, primary) and Java (`android-app/`, secondary).

### Cumulative Repository State After Week 02

This cumulative snapshot includes Week 01 planning plus the first runnable Android shell. Kotlin primary paths are shown; mirror the same structure in `android-app/` when maintaining the Java track.

```text
LeafGuard-AI/
|-- README.md
|-- START_HERE.md
|-- LEARNING_PATH.md
|-- PRODUCT_PROGRESS_MAP.md
|-- progress-tracker.md
|-- roadmap/
|   |-- week-01-project-understanding/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|   `-- week-02-android-basics-ui/{README.md, learning-notes.md, exercises.md, build-task.md, validation-checklist.md, quiz.md, reflection.md}
|-- docs/evidence/
|   |-- week-01/{product-idea.md, user-journey.md, screen-map.md, system-sketch.*, week-growth-map.md, week-01-validation.md}
|   `-- week-02/{README.md, launch screenshots, navigation screenshots, validation notes}
|-- android-app-kotlin/
|   |-- build.gradle                       (4 lines)
|   |-- settings.gradle                    (17 lines)
|   |-- gradle.properties                  (3 lines)
|   |-- gradlew, gradlew.bat, gradle/wrapper/   (generated, never hand-edited)
|   `-- app/
|       |-- build.gradle                   (40 lines, exactly 4 dependencies)
|       |-- proguard-rules.pro             (generated, untouched)
|       `-- src/main/
|           |-- AndroidManifest.xml        (37 lines, 6 Activities declared)
|           |-- java/com/leafguard/{MainActivity.kt (34), ScanActivity.kt (12), ResultActivity.kt (12), HistoryActivity.kt (12), DiseaseLibraryActivity.kt (12), SettingsActivity.kt (12)}
|           `-- res/
|               |-- layout/{activity_main.xml (56), activity_scan.xml (25), activity_result.xml (25), activity_history.xml (25), activity_disease_library.xml (25), activity_settings.xml (25)}
|               `-- values/{strings.xml (25), colors.xml (9), themes.xml (9)}
|
|   (optional, only if your screen map has Analytics: AnalyticsActivity.kt + activity_analytics.xml)
|
`-- android-app/ (Java mirror of the same 16-file Week 02 Android shell)
```

Not yet present after Week 02: `res/xml/`, `assets/`, `res/menu/`, custom `res/drawable/`, and the `network/`, `database/`, `ml/`, `utils/`, `ui/` packages. Each arrives in the week that needs it.

---

## 📚 Week 02 — Navigation

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
| [⬅ Week 01: Project Understanding](../week-01-project-understanding/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 03: Camera & Gallery ➡](../week-03-camera-gallery/README.md) |

---
