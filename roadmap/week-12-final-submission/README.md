# Week 12: Final Submission

## What you'll learn & why

This is the finish line. You will package LeafGuard AI into an **APK** — the single
installable file you can hand to a teacher or drop onto any Android phone — and gather all
the other deliverables your CSE 2206 project needs: source code, a written report, slides,
and a demo video. You'll build the APK from the **Kotlin** app (`android-app-kotlin/`,
the primary track), install it on an emulator or phone to confirm it runs, and double-check
that the app uses the real assets `model.tflite` and `diseases.xml`. Getting a clean,
installable build is what turns "code on my laptop" into "an app I can show anyone".

## New words this week

See the shared [glossary](../../GLOSSARY.md) for more. The key terms this week:

- **APK** — Android Package: the single installable file of your app (ends in `.apk`).
- **Debug build vs release build** — a *debug* APK is for testing; a *release* APK is
  optimised and signed for real distribution.
- **`assembleDebug`** — the Gradle command that builds a debug APK.
- **adb** — Android Debug Bridge, the command-line tool that installs an APK onto a device
  (`adb install app.apk`); you can also drag an APK onto a running emulator.

> **Build it (Kotlin, primary):** in Android Studio open `android-app-kotlin/`, then
> **Build > Build Bundle(s) / APK(s) > Build APK(s)**, or run `./gradlew assembleDebug`
> (Windows: `gradlew.bat assembleDebug`). The Java app (`android-app/`) builds the same way
> and is the labelled **secondary** track. Worked steps live in
> [`../../solutions/week-12/`](../../solutions/week-12/) and
> [`../../notebooks/week-12/`](../../notebooks/week-12/).

For a complete production release rather than a course-only APK build, use the
[`Production Release Runbook`](../../docs/PRODUCTION_RELEASE_RUNBOOK.md). It is the
controlling order for model approval, backend hardening/deployment, device acceptance,
private signing, CI/CD, publication, monitoring, and rollback.

## Repository State After Week 12

Week 12 is the final submission state. The repository should now be browsable as a complete course project: source code, backend, model notes, evidence, final documents, and an installable APK all point to the same finished LeafGuard AI product.

### Structure to browse after this week

- `android-app-kotlin/` is the primary Android source used to build the submission APK.
- `android-app/` is the Java comparison track, if maintained for the course.
- `backend-api/` contains the FastAPI backend, model-loading path, dependencies, tests, and deployment notes.
- `model/` contains model contract notes, label files, conversion helpers, validation helpers, and acquisition guidance.
- `docs/` contains architecture, setup, release, validation, report, presentation, viva, and deployment material.
- `docs/evidence/week-01/` through `docs/evidence/week-12/` contain week-by-week proof.
- `final-submission/` contains the final checklist, demo-video script, and submission packaging notes.
- `release-records/` and production runbooks record release decisions when used.

### Files you should create or update this week

- The debug or release APK generated from `android-app-kotlin/`.
- `final-submission/submission-checklist.md`.
- `final-submission/demo-video-script.md`.
- Final report files based on `docs/final-report-template.md`.
- Presentation slides based on `docs/presentation-outline.md`.
- `docs/evidence/week-12/` with APK build, install, and final demo proof.
- Main `README.md` setup/run notes if anything changed during packaging.
- `progress-tracker.md` with Week 12 complete.

### What this repository state can do

- Build an installable Android APK from the primary Kotlin track.
- Demonstrate camera/gallery input, cloud prediction, offline prediction, history, disease library, notifications, sharing, and optional location.
- Explain setup, architecture, model limitations, testing, and release steps from repository documents.
- Support final report writing, presentation, demo video, and viva preparation.

### What this repository state cannot do

- It cannot claim production readiness unless the production release runbook is also completed.
- It cannot hide model limitations, mock-mode behavior, or unsupported crops.
- It cannot replace a live final install test on a fresh emulator or device.
- It cannot be considered complete if evidence, report, slides, APK, and demo video are missing.

---

## Weekly Objective

Complete all final deliverables for CSE 2206 project submission.

**Final Deliverables:**
1. Polished Android app (APK)
2. Source code (GitHub repository)
3. Final project report (PDF)
4. Presentation slides (PPT)
5. Demo video (MP4)
6. Viva preparation document

---

## Why This Week Matters

**This is submission week.** All work culminates in professional deliverables for evaluation.

**Teacher evaluates:** Completeness, documentation quality, demonstration ability.

---

## Submission Checklist

### Code Deliverables
- [ ] Android source code in GitHub
- [ ] FastAPI backend code in GitHub
- [ ] ML model file or source
- [ ] Database schema documented
- [ ] XML disease library complete
- [ ] README with setup instructions
- [ ] All dependencies documented

### APK Deliverable
- [ ] Release APK built and tested
- [ ] APK signed (debug or release)
- [ ] Installation tested on device
- [ ] APK size reasonable (<50MB)

### Documentation Deliverables
- [ ] Final report (PDF, 20-30 pages)
- [ ] Architecture diagram
- [ ] Screenshots of all features
- [ ] Test results table
- [ ] Limitations documented

### Presentation Deliverables
- [ ] Slides (12-15 slides)
- [ ] Demo script written
- [ ] Video recorded (5-10 minutes)
- [ ] Viva questions prepared

---

## Weekly Timeline

- **Day 1:** APK building and testing (3h)
- **Day 2-3:** Final report writing (8h)
- **Day 4:** Presentation slides (4h)
- **Day 5:** Demo video recording (3h)
- **Day 6:** Viva preparation (3h)
- **Day 7:** Final review and submission (2h)

---

## Final Report Outline

1. **Title Page** (1 page)
2. **Abstract** (1 page)
3. **Introduction** (2-3 pages)
4. **Literature Review** (2-3 pages)
5. **System Design** (4-5 pages)
6. **Implementation** (6-8 pages)
7. **Testing** (3-4 pages)
8. **Results** (2-3 pages)
9. **Conclusion** (1-2 pages)
10. **References** (1-2 pages)

---

## Demo Video Script

1. **Introduction** (30s) - Name, project title
2. **Problem Statement** (30s) - Why this app?
3. **Features Demo** (6-8 min):
   - Camera capture
   - Cloud prediction
   - Offline prediction
   - Scan history
   - Disease library
   - Share functionality
   - Notifications
4. **Technical Highlights** (1 min) - XML parsing, Room database, TFLite
5. **Conclusion** (30s) - Summary

---

## Viva Preparation

Practice answering:
1. Architecture questions
2. Technology choices (why XML? why Room?)
3. Challenges faced
4. Future improvements
5. CSE 2206 syllabus coverage

---

**This is it! Final push to completion!**


<!-- NAV_FOOTER_START -->

---

## 📈 Product State After This Week

**Cumulative product completion: 100%** *(official model: [PRODUCT_PROGRESS_MAP.md](../../PRODUCT_PROGRESS_MAP.md))*

- **Your app can now…** **everything** — a complete, signed, installable, documented plant disease detection app with cloud + offline AI, history, disease library, notifications, sharing, and location.
- **Your app still cannot…** nothing in scope — the product is 100% complete. 🎉
- **Applies equally to both tracks:** Kotlin (`android-app-kotlin/`, primary) and Java (`android-app/`, secondary).

---

## 📚 Week 12 — Navigation

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
| [⬅ Week 11: Testing, Debugging & Performance](../week-11-testing-debugging-performance/README.md) | [Learning Path](../../LEARNING_PATH.md) | *(Last week — course complete!)* |

---
