# Week 01 Build Task: Create the Learning Foundation Package

## Objective

Create a small Week 01 foundation package that proves you understand the base product idea and are ready to build the first Android slice in Week 02.

This is not a full final proposal. It is not a finished architecture document. It is the first 5% of the product: the idea, journey, screen thinking, weekly growth map, and evidence system.

Estimated time: 6 to 8 hours.

---

## Before You Start

Confirm:

- [ ] You read `README.md`.
- [ ] You read `learning-notes.md`.
- [ ] You completed all exercises.
- [ ] You understand that no app code is required in Week 01.

---

## Folder Setup

Create this folder if it does not exist:

```text
docs/evidence/week-01/
```

Inside it, create:

```text
docs/evidence/week-01/
|-- product-idea.md
|-- user-journey.md
|-- screen-map.md
|-- system-sketch.md
|-- week-growth-map.md
`-- week-01-validation.md
```

If you draw by hand, save photos or scans in the same folder and link to them from the markdown file.

---

## Deliverable 1: Product Idea

File: `docs/evidence/week-01/product-idea.md`

Write these sections:

```markdown
# LeafGuard AI Product Idea

## One-Sentence Idea

## Target User

## Problem

## Proposed Mobile Solution

## In Scope for This Course

## Out of Scope for This Course

## Honest Limitation
```

### Requirements

- Keep it to 1 or 2 pages.
- Use plain language.
- Say clearly that this is a learning project, not a certified agricultural diagnosis product.
- Avoid implementation details that belong to future weeks.

### Check

You pass this deliverable if a beginner can read it and understand what the final app should do.

---

## Deliverable 2: User Journey

File: `docs/evidence/week-01/user-journey.md`

Write the main journey as numbered steps.

Use this starter:

```markdown
# Main User Journey

1. User opens LeafGuard AI.
2. User starts a scan.
3. User provides a leaf image.
4. App prepares the image for detection.
5. App shows a disease result and confidence.
6. App shows symptoms, treatment, and prevention guidance.
7. User can save or review the result later.
```

Add short notes explaining why each step matters.

### Check

You pass this deliverable if every future feature can connect back to this journey.

---

## Deliverable 3: Screen Map

File: `docs/evidence/week-01/screen-map.md`

Create a table:

| Screen Idea | User Goal | Main Action | Built In |
|---|---|---|---:|
| Home | Start or navigate | Start scan | Week 02 |

Include at least:

- Home
- Scan
- Result
- History
- Disease Library
- Settings/About

Optional future screen:

- Analytics or summary screen

### Check

You pass this deliverable if every screen has a purpose and none exists only because "the code has it."

---

## Deliverable 4: System Sketch

File: `docs/evidence/week-01/system-sketch.md` or image file.

Draw or write a simple box sketch:

```text
User
  -> Android App
      -> Image Input
      -> Detection Result
      -> Local History
      -> Disease Library
      -> Backend / AI Service
```

Add 5 to 8 bullet points below the sketch explaining each box.

### Rules

- Do not draw detailed classes.
- Do not draw a full database schema.
- Do not include final implementation decisions unless they are already obvious from the roadmap.

### Check

You pass this deliverable if you can explain the sketch in under 2 minutes.

---

## Deliverable 5: Week Growth Map

File: `docs/evidence/week-01/week-growth-map.md`

Create this table for all 12 weeks:

| Week | Learning Focus | Product Increment | Validation Demo |
|---:|---|---|---|
| 01 | Product idea and plan | Learning foundation | Explain foundation package |
| 02 | Android screens | Runnable UI skeleton | Tap through screens |

Use the roadmap week names for Weeks 03 to 12.

### Check

You pass this deliverable if each week adds a specific product ability and has a specific validation demo.

---

## Deliverable 6: Validation Record

File: `docs/evidence/week-01/week-01-validation.md`

Copy the checklist from `validation-checklist.md` and mark each item as yes/no.

For any no, write the fix before moving to Week 02.

---

## Milestone Demo

At the end of Week 01, demonstrate this:

1. Open `product-idea.md` and explain the project in one minute.
2. Open `user-journey.md` and walk through the main user flow.
3. Open `screen-map.md` and show which screen is built first in Week 02.
4. Open `system-sketch.md` and explain the boxes in plain language.
5. Open `week-growth-map.md` and show how the app reaches completion by Week 12.

This proves the Week 01 product foundation is complete.

---

## Commit Guidance

Make small commits such as:

```text
week-01: draft product idea
week-01: add user journey and screen map
week-01: complete foundation validation
```

Do not commit generated junk files or private information.

---

## Done Means

Week 01 is done when:

- all six foundation files exist
- the milestone demo works
- the quiz is complete
- the reflection is complete
- evidence is saved in `docs/evidence/week-01/`
- you can explain the product without looking at finished code

<!-- NAV_FOOTER_START -->

---

## 📚 Week 01 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| **4** | **build-task.md** ← *You are here* | **Build Implementation Guide** |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Practice Exercises](exercises.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Build Implementation Guide** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Validation & Verification →](validation-checklist.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| *(Start of roadmap)* | [Learning Path](../../LEARNING_PATH.md) | [Week 02: Android Basics & UI ➡](../week-02-android-basics-ui/README.md) |

---
