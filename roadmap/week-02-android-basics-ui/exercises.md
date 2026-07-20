# Week 02 Exercises: Android Project, Layouts, and Navigation

## How to Use These Exercises

Complete these before the build task. They are designed for a student who only knows the Week 01 product idea and screen map.

Save outputs in:

```text
docs/evidence/week-02/exercises/
```

---

## Exercise 1: Connect Week 01 Screens to Android Screens

### Goal

Turn your Week 01 screen map into an Android screen plan.

### Task

Create a table:

| Week 01 Screen Idea | Android Activity Name | Layout File | Week 02 Status | Future Week That Adds Real Behavior |
|---|---|---|---|---:|
| Home | MainActivity | activity_main.xml | Build now | 02 |

Include at least Home, Scan, Result, History, Disease Library, and Settings/About.

### Output

File: `docs/evidence/week-02/exercises/exercise-01-screen-to-activity-map.md`

### Validation

- [ ] Every Week 01 screen idea has a Week 02 Android placeholder.
- [ ] Future behavior is assigned to later weeks.
- [ ] You did not add camera/backend/database behavior to Week 02.

---

## Exercise 2: Explore the Android Project Folders

### Goal

Understand where Android files live.

### Task

Open `android-app-kotlin/` in Android Studio or VS Code and identify:

- Kotlin source folder
- `res/layout/`
- `res/values/`
- `AndroidManifest.xml`
- app-level `build.gradle`

Write one sentence explaining each.

### Output

File: `docs/evidence/week-02/exercises/exercise-02-folder-tour.md`

### Validation

- [ ] You can point to where Activity code lives.
- [ ] You can point to where XML layouts live.
- [ ] You can point to where strings and colors live.
- [ ] You can point to where screens are registered.

---

## Exercise 3: Resource Practice

### Goal

Learn why visible text belongs in `strings.xml`.

### Task

Create or identify three string resources:

```xml
<string name="app_name">LeafGuard AI</string>
<string name="home_title">LeafGuard AI</string>
<string name="open_scan">Open Scan</string>
```

Then use them in a layout with `@string/...`.

### Output

File: `docs/evidence/week-02/exercises/exercise-03-resources.md`

Include:

- the three string names
- where they are used
- one sentence explaining why hardcoded visible strings are avoided

### Validation

- [ ] Text is stored in `strings.xml`.
- [ ] Layout uses `@string/...` references.
- [ ] You can explain the benefit.

---

## Exercise 4: Build One Simple Layout

### Goal

Practise XML layout structure before building the full shell.

### Task

Create a practice Home layout with:

- app title
- short subtitle
- button to open Scan
- button to open History

Use either `LinearLayout` or `ConstraintLayout`.

### Output

File: `docs/evidence/week-02/exercises/exercise-04-layout-practice.md`

Include:

- screenshot of the layout preview or emulator
- short XML snippet
- one sentence explaining the root layout

### Validation

- [ ] Text is visible.
- [ ] Buttons are visible.
- [ ] Layout does not overlap on a phone screen.

---

## Exercise 5: Intent Navigation Practice

### Goal

Understand how a button opens another screen.

### Task

Write a small code snippet that opens `ScanActivity` from `MainActivity`.

Starter:

```kotlin
findViewById<Button>(R.id.buttonOpenScan).setOnClickListener {
    startActivity(Intent(this, ScanActivity::class.java))
}
```

Below the snippet, explain each part in your own words.

### Output

File: `docs/evidence/week-02/exercises/exercise-05-intent-practice.md`

### Validation

- [ ] You explain `findViewById`.
- [ ] You explain `setOnClickListener`.
- [ ] You explain `Intent`.
- [ ] You explain `startActivity`.

---

## Exercise 6: Evidence Plan for the UI Shell

### Goal

Prepare validation evidence before the build task.

### Task

Create a checklist of screenshots or recordings you will capture:

- app launched on emulator/device
- Home screen
- Scan placeholder screen
- Result placeholder screen
- History placeholder screen
- Disease Library placeholder screen
- Settings/About placeholder screen
- Build success output

### Output

File: `docs/evidence/week-02/exercises/exercise-06-evidence-plan.md`

### Validation

- [ ] Evidence checks only Week 02 behavior.
- [ ] It does not require camera, backend, database, or AI.
- [ ] It proves the UI shell is navigable.

---

## Completion Rule

Start the build task only after all six exercises exist and you can explain how Week 02 grows from the Week 01 screen map.

<!-- NAV_FOOTER_START -->

---

## 📚 Week 02 — Navigation

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
| [⬅ Week 01: Project Understanding](../week-01-project-understanding/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 03: Camera & Gallery ➡](../week-03-camera-gallery/README.md) |

---
