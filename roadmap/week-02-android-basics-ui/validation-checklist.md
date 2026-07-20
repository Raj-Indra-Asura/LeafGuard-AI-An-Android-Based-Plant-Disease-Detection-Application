# Week 02 Validation Checklist: Android UI Navigation Shell

## Milestone Demo

> Demo: Launch the app, show the Home screen, navigate to every Week 02 placeholder screen, return safely, and explain which future week will add the real behavior. This proves the cumulative product state is 15%.

You may not move to Week 03 until every required item is yes.

---

## 1. Week 01 Connection

- [ ] I reviewed the Week 01 product idea.
- [ ] I reviewed the Week 01 user journey.
- [ ] I reviewed the Week 01 screen map.
- [ ] My Week 02 screens match the Week 01 screen map or clearly explain any small adjustment.
- [ ] I can explain what Week 02 adds beyond Week 01.

Pass rule: all 5 items yes.

---

## 2. Project Setup

- [ ] `android-app-kotlin/` opens in Android Studio or builds from terminal.
- [ ] App package is `com.leafguard`.
- [ ] Gradle sync succeeds.
- [ ] Debug build succeeds.
- [ ] App launches on emulator or physical device.
- [ ] Evidence of successful run is saved in `docs/evidence/week-02/`.

Pass rule: all 6 items yes.

---

## 3. Resource Files

- [ ] `strings.xml` contains app name and button labels.
- [ ] Placeholder messages are honest about future weeks.
- [ ] Layouts use `@string/...` for visible text where practical.
- [ ] `colors.xml` contains named colors.
- [ ] At least one layout uses a named color.
- [ ] No XML syntax errors exist in resource files.

Pass rule: all 6 items yes.

---

## 4. Screens and Layouts

Required placeholder screens:

- [ ] Home screen exists.
- [ ] Scan placeholder exists.
- [ ] Result placeholder exists.
- [ ] History placeholder exists.
- [ ] Disease Library placeholder exists.
- [ ] Settings/About placeholder exists.

Layout checks:

- [ ] Each screen has a clear title.
- [ ] Each placeholder explains what will be added later.
- [ ] Text and buttons do not overlap on a phone screen.
- [ ] App does not pretend future features already work.

Pass rule: all 10 items yes.

---

## 5. Activity Classes and Manifest

- [ ] Each required screen has an Activity class.
- [ ] Each Activity calls `setContentView` with the correct layout.
- [ ] Each Activity is declared in `AndroidManifest.xml`.
- [ ] `MainActivity` has `MAIN` and `LAUNCHER` intent filter.
- [ ] Internal screens use `android:exported="false"`.
- [ ] Manifest has no merge errors.

Pass rule: all 6 items yes.

---

## 6. Navigation

- [ ] Home button opens Scan placeholder.
- [ ] Home button opens Result placeholder or sample result placeholder.
- [ ] Home button opens History placeholder.
- [ ] Home button opens Disease Library placeholder.
- [ ] Home button opens Settings/About placeholder.
- [ ] Back button returns safely or exits the app without crash.
- [ ] No placeholder screen requires real camera, backend, database, XML, or AI.

Pass rule: all 7 items yes.

---

## 7. Evidence

Save evidence under `docs/evidence/week-02/`.

- [ ] Build success screenshot or terminal output saved.
- [ ] Home screenshot saved.
- [ ] Scan placeholder screenshot saved.
- [ ] Result placeholder screenshot saved.
- [ ] History placeholder screenshot saved.
- [ ] Disease Library placeholder screenshot saved.
- [ ] Settings/About placeholder screenshot saved.
- [ ] Quiz answers saved.
- [ ] Reflection answers saved.

Pass rule: all 9 items yes.

---

## 8. Understanding Check

Can you explain these in your own words?

- [ ] What an Activity is.
- [ ] What an XML layout is.
- [ ] Why strings go in `strings.xml`.
- [ ] Why Activities must be declared in the manifest.
- [ ] How an Intent opens another screen.
- [ ] Why Week 02 does not implement camera/gallery yet.

Pass rule: all 6 items yes.

---

## Completion Criteria

Week 02 is complete only when:

1. The app builds and launches.
2. All required placeholder screens open.
3. The milestone demo works live.
4. Evidence is saved.
5. You can explain the code at a beginner level.

If any item is no, stay in Week 02 and fix it before moving to Week 03.

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
| [⬅ Week 01: Project Understanding](../week-01-project-understanding/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 03: Camera & Gallery ➡](../week-03-camera-gallery/README.md) |

---
