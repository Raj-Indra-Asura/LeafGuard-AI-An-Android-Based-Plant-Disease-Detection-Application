# Week 01 Learning Notes: Thinking Like a Beginner Product Builder

## Purpose

These notes teach the Week 01 ideas from zero. They intentionally avoid finished-code details. You will learn how to move from a base idea to a buildable product plan.

---

## 1. Start With the Product Idea

A product idea is the simplest useful description of the final app.

LeafGuard AI can be described like this:

> A beginner-friendly Android app that lets a user provide a leaf image and receive a plant-disease result with helpful guidance.

This sentence is enough for Week 01. It tells us:

- the platform: Android
- the input: leaf image
- the output: disease result and guidance
- the user value: faster help for plant problems

You do not need to know the final implementation yet.

---

## 2. Define the User Before the Technology

Technology choices should come after the user problem.

Possible users:

- a student demonstrating a mobile-development project
- a gardener checking a plant leaf
- a small farmer who wants quick first-level guidance
- a teacher evaluating Android concepts in CSE 2206

The project should be honest about its limits. It is a learning and demonstration app, not a certified agricultural diagnosis tool.

Good Week 01 statement:

> LeafGuard AI gives a quick educational suggestion from a leaf image and helps the user keep scan records.

Bad Week 01 statement:

> LeafGuard AI always detects every disease accurately.

The second statement promises more than a student project can safely prove.

---

## 3. Understand the Main User Journey

A user journey is the path a user follows to achieve one goal.

The main LeafGuard journey is:

```text
Open app
  -> choose or capture a leaf image
  -> request detection
  -> see disease name and confidence
  -> read symptoms, treatment, and prevention
  -> optionally save or review the scan later
```

This journey is more important than class names in Week 01. If the journey is clear, future code has a direction.

---

## 4. Break the Final Product Into Weekly Slices

A beginner can build a large product only by slicing it into small working increments.

For this project, a good slice has three parts:

1. A concept to learn.
2. A product ability to build.
3. A validation demo to prove it works.

Example:

| Week | Concept | Product Ability | Validation Demo |
|---:|---|---|---|
| 02 | Android screens and layouts | App opens and navigates between screens | Tap through the UI screens |
| 03 | Camera and gallery | User can provide a real image | Capture and choose image |
| 04 | Backend API | Server accepts an uploaded image | Upload image in API docs |

This is how the final product grows. Week 01 prepares this structure.

---

## 5. Screen Map Before Android Code

A screen map is a rough list of screens the user may need. It is not final Android code.

Beginner screen map:

```text
Home screen
  Purpose: welcome user and start scan

Scan screen
  Purpose: choose camera/gallery image and request detection

Result screen
  Purpose: show disease, confidence, and guidance

History screen
  Purpose: show saved scans

Disease library screen
  Purpose: browse disease information

Settings/About screen
  Purpose: show app information or simple settings
```

In Week 02, some of these may become Android Activity classes. In Week 01, they are just product ideas.

---

## 6. Box-Level System Sketch

A system sketch explains the product without technical overload.

Use this Week 01 level:

```text
User
  -> Android App
      -> Image Input
      -> Result Screen
      -> Local History
      -> Disease Library
      -> Backend / AI Service
```

This is enough for Week 01. Later weeks will replace each box with real implementation details.

Do not draw every class yet. That creates false confidence and makes the system harder for a beginner.

---

## 7. What Each Future Technology Means in Plain Language

You will see these names later. Learn only the plain meaning now.

| Technology | Plain Week 01 Meaning | Main Week |
|---|---|---:|
| Kotlin | The programming language for the main Android app. | 02 |
| XML layouts | Files that describe what Android screens look like. | 02 |
| Intent | Android message used to open another screen or system app. | 02-03 |
| Camera/gallery | Ways for the user to provide a leaf image. | 03 |
| FastAPI | A small Python server that can receive an image and return JSON. | 04 |
| Retrofit | Android library that talks to the backend server. | 05 |
| JSON | Text format used for backend responses. | 05 |
| Room | Android library for saving scan history locally. | 07 |
| XML disease library | Local file that stores disease guidance. | 08 |
| TensorFlow Lite | Tool for running a model on the phone offline. | 09 |
| Notification | A phone reminder shown outside the app screen. | 10 |
| JUnit/Espresso | Tools that test code and UI behavior. | 11 |
| APK | Installable Android app file. | 12 |

---

## 8. Evidence Is Part of Learning

Evidence proves that you did the work gradually.

Week 01 evidence should show thinking, not code:

- product idea notes
- user journey
- screen sketch
- system sketch
- week growth map
- quiz score
- reflection answers

Save these in `docs/evidence/week-01/`.

Good evidence answers the question:

> What did I understand this week that I did not understand before?

---

## 9. Common Week 01 Mistakes

### Mistake 1: Trying to understand all final code immediately

Do not do this. You will learn the code as each slice is built.

### Mistake 2: Drawing a very detailed architecture too early

Use a box-level sketch. Detailed architecture belongs later.

### Mistake 3: Treating planning as less important than coding

Planning is the first 5% of the product. Without it, later weeks become disconnected tasks.

### Mistake 4: Copying a senior project structure

You can learn from examples later, but Week 01 should focus on your own product idea and journey.

### Mistake 5: Validating future features

Week 01 validation checks only Week 01 artifacts. It does not check camera, backend, model, or database behavior.

---

## 10. Week 01 Understanding Checklist

Before starting the build task, make sure you can answer:

- What problem does LeafGuard AI try to solve?
- Who is the target user?
- What is the main user journey?
- Which screens might the final app need?
- What will Week 02 add that Week 01 does not have?
- What evidence proves Week 01 is complete?

If you can answer these in your own words, continue to `exercises.md`.

<!-- NAV_FOOTER_START -->

---

## 📚 Week 01 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| **2** | **learning-notes.md** ← *You are here* | **Theory & Learning Notes** |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Week Overview & Objectives](README.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Theory & Learning Notes** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Practice Exercises →](exercises.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| *(Start of roadmap)* | [Learning Path](../../LEARNING_PATH.md) | [Week 02: Android Basics & UI ➡](../week-02-android-basics-ui/README.md) |

---
