# Week 01: Product Idea, Learning Foundation, and First Plan

## Week 01 Mindset

This week starts from only the base idea of the final product:

> LeafGuard AI will become an Android app that helps a user take or choose a plant-leaf image, receive a disease prediction, read useful guidance, and keep a record of scans.

You are not expected to understand the finished codebase. You are not expected to know every Android, backend, database, or machine-learning detail yet. Week 01 is about turning a rough product idea into a clear beginner roadmap that you can build and validate week by week.

The important question this week is not "How does every class work?" The important question is:

> What will we build, why will we build it, and how will each future week add one working piece of the product?

---

## Product State After Week 01

**Cumulative product contribution: 5%**

By the end of Week 01, no Android feature exists yet. The product contribution is a validated foundation:

- a clear problem statement
- a simple user journey
- a first screen map
- a high-level system sketch
- a 12-week growth map
- an evidence folder for Week 01 work

This 5% matters because every later week will use these decisions. Week 02 starts the first real Android slice.

### What the product can do after Week 01

- It can be explained as a project idea.
- It has a planned user journey from image input to result.
- It has a beginner-level map of what will be learned each week.
- It has evidence showing that the student understands the goal.

### What the product cannot do yet

- It cannot run on Android.
- It cannot show UI screens.
- It cannot open the camera or gallery.
- It cannot predict disease.
- It cannot save history.

That is correct for Week 01. Do not pretend the app is built yet.

---

## Repository State After Week 01

Week 01 is the repository setup and orientation state. The current repository may already contain later-week code, but when you study Week 01 you should browse it as a planned project, not as a finished app.

### Structure to recognize

- `README.md`, `START_HERE.md`, `LEARNING_PATH.md`, `PRODUCT_PROGRESS_MAP.md`, and `progress-tracker.md` explain the learning system and the 12-week path.
- `roadmap/week-01-project-understanding/` contains this week's README, learning notes, exercises, build task, validation checklist, quiz, and reflection.
- `docs/evidence/week-01/` is where Week 01 proof belongs.
- `android-app-kotlin/` is the primary future Android track.
- `android-app/` is the secondary Java comparison track.
- `backend-api/` and `model/` are future backend and machine-learning landmarks only.

### Files you should create or update this week

- `docs/evidence/week-01/product-idea.md`
- `docs/evidence/week-01/user-journey.md`
- `docs/evidence/week-01/screen-map.md`
- `docs/evidence/week-01/system-sketch.md` or `docs/evidence/week-01/system-sketch.png`
- `docs/evidence/week-01/week-growth-map.md`
- `docs/evidence/week-01/week-01-validation.md`
- `progress-tracker.md` after validation is complete

### What this repository state can do

- Show the learning roadmap and the boundaries of each week.
- Explain where Android, backend, model, documentation, exercises, notebooks, and evidence will live.
- Store beginner planning evidence before any coding starts.
- Help you answer: "What are we building, and what is intentionally out of scope right now?"

### What this repository state cannot do

- It cannot prove Android runtime behavior.
- It cannot show a working screen, camera flow, backend request, database record, XML parser, or AI prediction.
- It cannot be submitted as a finished app.
- It should not be used to claim later-week features are already understood.

---

## New Words This Week

| Term | Beginner Definition |
|---|---|
| Product idea | A short description of what the final app should help the user do. |
| User journey | The steps a user follows to complete one goal in the app. |
| Feature | One useful ability of the product, such as "choose a leaf image." |
| Screen | One visible page of an app. In Android, a screen may later become an Activity. |
| Milestone | A small checkpoint that proves part of the product is complete. |
| Validation | A yes/no check that proves this week's work is actually finished. |
| Evidence | Screenshots, notes, diagrams, logs, or files that prove what you completed. |
| Scope | The boundary of what the project will and will not include. |

Keep advanced terms such as Retrofit, Room, FastAPI, and TensorFlow Lite as future terms. You only need a plain-language idea of them this week.

---

## Weekly Objective

By the end of Week 01, you will be able to:

1. Explain LeafGuard AI in simple language without reading code.
2. Describe the main user journey: open app -> provide image -> get result -> save or review result.
3. Separate final-product features into weekly learning slices.
4. Draw a beginner-level system sketch using plain boxes, not class-level architecture.
5. Create a small foundation package that proves you are ready for Week 02.

---

## What You Will Produce

Save Week 01 evidence in `docs/evidence/week-01/`.

You will create these artifacts:

1. `product-idea.md` - problem, target user, final app promise, and scope.
2. `user-journey.md` - the main scan journey written as beginner steps.
3. `screen-map.md` - rough list or sketch of screens the app will need.
4. `system-sketch.png` or `system-sketch.md` - high-level boxes: Android app, backend, AI model, local storage, evidence.
5. `week-growth-map.md` - one short row for each week showing what that week adds.
6. `week-01-validation.md` - your completed validation answers.

These are deliberately small. The full proposal, detailed architecture, real code, and final report will grow later.

---

## What Week 01 Must Not Do

Do not do these in Week 01:

- Do not copy the finished app code to "understand" the project.
- Do not memorize class names before you know the product flow.
- Do not make a detailed database schema.
- Do not write Retrofit, Room, TFLite, or FastAPI code.
- Do not claim the app is complete.
- Do not validate future features.

If you see advanced names in the repository, treat them as future landmarks, not Week 01 requirements.

---

## How This Connects to CSE 2206

CSE 2206 is Mobile Application Development. Week 01 connects to the course through planning and explanation:

- You identify the mobile problem the app solves.
- You decide which Android concepts will appear in later weeks.
- You prepare evidence that your learning is gradual, not copied.
- You learn to explain before coding.

The detailed technical concepts are saved for later:

| Future Concept | First Main Week |
|---|---:|
| Android project and UI screens | Week 02 |
| Camera and gallery image input | Week 03 |
| Backend API | Week 04 |
| Android networking | Week 05 |
| Cloud prediction pipeline | Week 06 |
| Local database and history | Week 07 |
| XML disease library | Week 08 |
| Offline TensorFlow Lite prediction | Week 09 |
| Notifications, share, and location | Week 10 |
| Testing and debugging | Week 11 |
| APK and final submission | Week 12 |

---

## Suggested 7-Day Plan

| Day | Focus | Output |
|---|---|---|
| Day 1 | Understand the base idea | Draft `product-idea.md` |
| Day 2 | Identify users and problems | Improve problem and target-user section |
| Day 3 | Write the main user journey | Draft `user-journey.md` |
| Day 4 | Sketch screens | Draft `screen-map.md` |
| Day 5 | Sketch the system at box level | Draft `system-sketch.md` or image |
| Day 6 | Map weekly growth | Draft `week-growth-map.md` |
| Day 7 | Validate and reflect | Complete validation, quiz, and reflection |

---

## Beginner Success Standard

You are ready for Week 02 when you can explain:

1. Who LeafGuard AI helps.
2. What the first useful app flow will be.
3. Why an Android app is a suitable solution.
4. Which feature will be built first in Week 02.
5. What evidence proves Week 01 is complete.

You do not need to explain the final code yet.

---

## Week 01 File Order

Complete these files in this order:

| Step | File | Purpose |
|---:|---|---|
| 1 | `README.md` | Understand the week goal and product slice. |
| 2 | `learning-notes.md` | Learn the Week 01 concepts from zero. |
| 3 | `exercises.md` | Practise small thinking tasks before the build task. |
| 4 | `build-task.md` | Create the Week 01 foundation package. |
| 5 | `validation-checklist.md` | Prove the 5% foundation is complete. |
| 6 | `quiz.md` | Check your understanding. |
| 7 | `reflection.md` | Explain what you learned in your own words. |

After this, update the main progress tracker and move to Week 02.

<!-- NAV_FOOTER_START -->

---

## 📈 Product State After This Week

**Cumulative product completion: 5%** *(official model: [PRODUCT_PROGRESS_MAP.md](../../PRODUCT_PROGRESS_MAP.md))*

- **Your app can now…** exist as a validated learning foundation: the problem, user flow, future slices, and evidence plan are clear.
- **Your app still cannot…** run at all — no Android code exists yet. Week 02 creates the first runnable app shell.
- **Applies equally to both tracks:** Kotlin (`android-app-kotlin/`, primary) and Java (`android-app/`, secondary).

### Cumulative Repository State After Week 01

This is the complete Week 01 repository state to understand or update. Later-week code may already exist in the full repository, but it is not part of the Week 01 handled state yet.

```text
LeafGuard-AI/
|-- README.md
|-- START_HERE.md
|-- LEARNING_PATH.md
|-- PRODUCT_PROGRESS_MAP.md
|-- progress-tracker.md
|-- roadmap/week-01-project-understanding/
|   |-- README.md
|   |-- learning-notes.md
|   |-- exercises.md
|   |-- build-task.md
|   |-- validation-checklist.md
|   |-- quiz.md
|   `-- reflection.md
|-- docs/evidence/week-01/
|   |-- product-idea.md
|   |-- user-journey.md
|   |-- screen-map.md
|   |-- system-sketch.md or system-sketch.png
|   |-- week-growth-map.md
|   `-- week-01-validation.md
|-- android-app-kotlin/  (future primary Android track)
|-- android-app/         (future Java comparison track)
|-- backend-api/         (future backend landmark)
`-- model/               (future ML landmark)
```

---

## 📚 Week 01 — Navigation

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
| *(Start of roadmap)* | [Learning Path](../../LEARNING_PATH.md) | [Week 02: Android Basics & UI ➡](../week-02-android-basics-ui/README.md) |

---
