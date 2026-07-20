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
