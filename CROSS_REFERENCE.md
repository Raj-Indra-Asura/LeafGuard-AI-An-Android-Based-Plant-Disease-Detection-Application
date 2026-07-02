# Material Cross-Reference Guide

This document provides a comprehensive mapping of how different learning materials connect across the LeafGuard AI curriculum.

> **🍴 Track note:** Code references below list the **Kotlin (primary) path first, then the Java twin**. Build in [`android-app-kotlin/`](android-app-kotlin/) unless you have chosen the Java track. A "track" is simply the language you follow the lessons in — if unsure, choose Kotlin.

## 📚 How Materials Interconnect

### Learning Flow for Each Week

```
Theory → Practice → Implementation → Validation
   ↓         ↓            ↓             ↓
README → Exercises → Build Task → Checklist
   ↓         ↓            ↓             ↓
Learning  Solutions  Notebooks   Evidence
 Notes       ↓            ↓             ↓
           Quiz      Reflection    Progress
```

## 🔗 Week-by-Week Material Connections

### Week 01: Project Understanding & Foundation

| Material Type | File/Location | Connects To | Purpose |
|--------------|---------------|-------------|---------|
| Overview | `roadmap/week-01-project-understanding/README.md` | All Week 01 materials | Central hub |
| Theory | `roadmap/week-01-project-understanding/learning-notes.md` | COURSE_OVERVIEW.md, SYLLABUS_MAPPING.md | Deep concepts |
| Practice | `roadmap/week-01-project-understanding/exercises.md` | solutions/week-01/, PROJECT_ARCHITECTURE.md | Hands-on |
| Implementation | `roadmap/week-01-project-understanding/build-task.md` | SENIOR_REPO_ANALYSIS.md, docs/proposal-template.md | Feature build |
| Validation | `roadmap/week-01-project-understanding/validation-checklist.md` | docs/evidence/week-01/ | Verification |
| Assessment | `roadmap/week-01-project-understanding/quiz.md` | learning-notes.md | Knowledge check |
| Reflection | `roadmap/week-01-project-understanding/reflection.md` | progress-tracker.md | Understanding |
| Solutions | `solutions/week-01/` | exercises.md | Reference |
| Notebooks | `notebooks/week-01/` | learning-notes.md, exercises.md | Interactive |

### Week 02: Android Basics & UI Skeleton

| Material Type | File/Location | Connects To | Purpose |
|--------------|---------------|-------------|---------|
| Overview | `roadmap/week-02-android-basics-ui/README.md` | Week 01 completion, android-app-kotlin/ (Java: android-app/) | Central hub |
| Theory | `roadmap/week-02-android-basics-ui/learning-notes.md` | Android developer docs, GLOSSARY.md | Deep concepts |
| Practice | `roadmap/week-02-android-basics-ui/exercises.md` | solutions/week-02/, exercises/android-kotlin/ (Java: exercises/android/) | Hands-on |
| Implementation | `roadmap/week-02-android-basics-ui/build-task.md` | android-app-kotlin/ folder structure (Java: android-app/) | Feature build |
| Validation | `roadmap/week-02-android-basics-ui/validation-checklist.md` | docs/evidence/week-02/ | Verification |
| Solutions | `solutions/week-02/` | exercises.md | Reference |
| Notebooks | `notebooks/week-02/` | Activity lifecycle, XML layouts | Interactive |

### Week 03: Camera & Gallery Integration

| Material Type | File/Location | Connects To | Purpose |
|--------------|---------------|-------------|---------|
| Overview | `roadmap/week-03-camera-gallery/README.md` | Week 02 UI, sample-images/ | Central hub |
| Theory | `roadmap/week-03-camera-gallery/learning-notes.md` | Android Intents, Permissions | Deep concepts |
| Practice | `roadmap/week-03-camera-gallery/exercises.md` | solutions/week-03/ | Hands-on |
| Implementation | `roadmap/week-03-camera-gallery/build-task.md` | android-app-kotlin/app/src/main/java/com/leafguard/MainActivity.kt (Java: android-app/.../MainActivity.java) | Feature build |
| Solutions | `solutions/week-03/` | exercises.md | Reference |
| Notebooks | `notebooks/week-03/` | Runtime permissions, Image handling | Interactive |

### Week 04: FastAPI Backend Development

| Material Type | File/Location | Connects To | Purpose |
|--------------|---------------|-------------|---------|
| Overview | `roadmap/week-04-fastapi-backend/README.md` | backend-api/ folder | Central hub |
| Theory | `roadmap/week-04-fastapi-backend/learning-notes.md` | FastAPI docs, REST principles | Deep concepts |
| Practice | `roadmap/week-04-fastapi-backend/exercises.md` | solutions/week-04/, exercises/backend/ | Hands-on |
| Implementation | `roadmap/week-04-fastapi-backend/build-task.md` | backend-api/main.py | Feature build |
| Solutions | `solutions/week-04/` | exercises.md | Reference |
| Notebooks | `notebooks/week-04/` | REST APIs, File uploads | Interactive |

### Week 05: Android Networking with Retrofit

| Material Type | File/Location | Connects To | Purpose |
|--------------|---------------|-------------|---------|
| Overview | `roadmap/week-05-android-networking/README.md` | Week 03 images, Week 04 backend | Central hub |
| Theory | `roadmap/week-05-android-networking/learning-notes.md` | Retrofit docs, HTTP fundamentals | Deep concepts |
| Practice | `roadmap/week-05-android-networking/exercises.md` | solutions/week-05/ | Hands-on |
| Implementation | `roadmap/week-05-android-networking/build-task.md` | android-app-kotlin/app/src/main/java/com/leafguard/network/ApiService.kt (Java: android-app/.../network/ApiService.java) | Feature build |
| Solutions | `solutions/week-05/` | exercises.md | Reference |
| Notebooks | `notebooks/week-05/` | Retrofit, JSON parsing | Interactive |

### Week 06: Cloud ML Model Integration

| Material Type | File/Location | Connects To | Purpose |
|--------------|---------------|-------------|---------|
| Overview | `roadmap/week-06-cloud-ml-model/README.md` | Week 05 networking, model/ folder | Central hub |
| Theory | `roadmap/week-06-cloud-ml-model/learning-notes.md` | ML concepts, model/ docs | Deep concepts |
| Practice | `roadmap/week-06-cloud-ml-model/exercises.md` | solutions/week-06/, exercises/ml/ | Hands-on |
| Implementation | `roadmap/week-06-cloud-ml-model/build-task.md` | backend-api/main.py, model/ | Feature build |
| Solutions | `solutions/week-06/` | exercises.md | Reference |
| Notebooks | `notebooks/week-06/` | CNN architecture, Preprocessing | Interactive |

### Week 07: Room Database & SQLite History

| Material Type | File/Location | Connects To | Purpose |
|--------------|---------------|-------------|---------|
| Overview | `roadmap/week-07-room-sqlite-history/README.md` | Week 06 predictions | Central hub |
| Theory | `roadmap/week-07-room-sqlite-history/learning-notes.md` | Room docs, SQL basics | Deep concepts |
| Practice | `roadmap/week-07-room-sqlite-history/exercises.md` | solutions/week-07/, exercises/database/ | Hands-on |
| Implementation | `roadmap/week-07-room-sqlite-history/build-task.md` | android-app-kotlin/app/src/main/java/com/leafguard/database/ (Java: android-app/.../database/) | Feature build |
| Solutions | `solutions/week-07/` | exercises.md | Reference |
| Notebooks | `notebooks/week-07/` | Room architecture, CRUD ops | Interactive |

### Week 08: XML Disease Library Parsing

| Material Type | File/Location | Connects To | Purpose |
|--------------|---------------|-------------|---------|
| Overview | `roadmap/week-08-xml-disease-library/README.md` | Week 07 database, assets/ | Central hub |
| Theory | `roadmap/week-08-xml-disease-library/learning-notes.md` | XML parsing, XmlPullParser | Deep concepts |
| Practice | `roadmap/week-08-xml-disease-library/exercises.md` | solutions/week-08/ | Hands-on |
| Implementation | `roadmap/week-08-xml-disease-library/build-task.md` | android-app-kotlin/app/src/main/assets/diseases.xml (Java: android-app/app/src/main/assets/diseases.xml) | Feature build |
| Solutions | `solutions/week-08/` | exercises.md | Reference |
| Notebooks | `notebooks/week-08/` | XML structure, Parsing methods | Interactive |

### Week 09: TensorFlow Lite Offline AI

| Material Type | File/Location | Connects To | Purpose |
|--------------|---------------|-------------|---------|
| Overview | `roadmap/week-09-tensorflow-lite-offline-ai/README.md` | Week 06 model, model/ | Central hub |
| Theory | `roadmap/week-09-tensorflow-lite-offline-ai/learning-notes.md` | TFLite docs, On-device ML | Deep concepts |
| Practice | `roadmap/week-09-tensorflow-lite-offline-ai/exercises.md` | solutions/week-09/, exercises/ml/ | Hands-on |
| Implementation | `roadmap/week-09-tensorflow-lite-offline-ai/build-task.md` | android-app-kotlin/app/src/main/assets/model.tflite (Java: android-app/app/src/main/assets/model.tflite) | Feature build |
| Solutions | `solutions/week-09/` | exercises.md | Reference |
| Notebooks | `notebooks/week-09/` | TFLite conversion, Inference | Interactive |

### Week 10: Notifications, Share & Location

| Material Type | File/Location | Connects To | Purpose |
|--------------|---------------|-------------|---------|
| Overview | `roadmap/week-10-notifications-share-location/README.md` | Week 09 completion | Central hub |
| Theory | `roadmap/week-10-notifications-share-location/learning-notes.md` | Notifications, Intents, Location | Deep concepts |
| Practice | `roadmap/week-10-notifications-share-location/exercises.md` | solutions/week-10/ | Hands-on |
| Implementation | `roadmap/week-10-notifications-share-location/build-task.md` | android-app/ features | Feature build |
| Solutions | `solutions/week-10/` | exercises.md | Reference |
| Notebooks | `notebooks/week-10/` | NotificationChannel, Share | Interactive |

### Week 11: Testing, Debugging & Performance

| Material Type | File/Location | Connects To | Purpose |
|--------------|---------------|-------------|---------|
| Overview | `roadmap/week-11-testing-debugging-performance/README.md` | All previous weeks | Central hub |
| Theory | `roadmap/week-11-testing-debugging-performance/learning-notes.md` | Testing principles, Debugging | Deep concepts |
| Practice | `roadmap/week-11-testing-debugging-performance/exercises.md` | solutions/week-11/, exercises/testing/ | Hands-on |
| Implementation | `roadmap/week-11-testing-debugging-performance/build-task.md` | android-app-kotlin/app/src/test/ + app/src/androidTest/ (Java: android-app/app/src/test/ + app/src/androidTest/), validation/ | Feature build |
| Solutions | `solutions/week-11/` | exercises.md | Reference |
| Notebooks | `notebooks/week-11/` | Test design, Performance | Interactive |

### Week 12: Final Submission & Deployment

| Material Type | File/Location | Connects To | Purpose |
|--------------|---------------|-------------|---------|
| Overview | `roadmap/week-12-final-submission/README.md` | All weeks, final-submission/ | Central hub |
| Theory | `roadmap/week-12-final-submission/learning-notes.md` | APK building, Documentation | Deep concepts |
| Practice | `roadmap/week-12-final-submission/exercises.md` | solutions/week-12/ | Hands-on |
| Implementation | `roadmap/week-12-final-submission/build-task.md` | docs/, final-submission/ | Feature build |
| Solutions | `solutions/week-12/` | exercises.md | Reference |
| Notebooks | `notebooks/week-12/` | APK signing, Viva prep | Interactive |

---

## 📖 Cross-Cutting Resources

### Always Available References

| Resource | Location | When to Use | Connects To |
|----------|----------|-------------|-------------|
| Course Context | `COURSE_OVERVIEW.md` | Week 01, viva prep | All weeks |
| Learning Strategy | `LEARNING_RULES.md` | Week 01, when stuck | All materials |
| Syllabus Map | `SYLLABUS_MAPPING.md` | Planning, evidence collection | All weeks |
| Architecture | `PROJECT_ARCHITECTURE.md` | Week 01, Weeks 4-6, Week 12 | System design |
| Senior Analysis | `SENIOR_REPO_ANALYSIS.md` | Week 01, when stuck | Code patterns |
| Glossary | `GLOSSARY.md` | When encountering new terms | All materials |
| Progress Tracker | `progress-tracker.md` | Daily/weekly | All weeks |

### Topical Exercise Collections

| Topic | Location | Related Weeks | Purpose |
|-------|----------|---------------|---------|
| Android (Kotlin — primary) | `exercises/android-kotlin/` | Weeks 2-3, 7-8, 10 | Platform skills |
| Android (Java — secondary) | `exercises/android/` | Weeks 2-3, 7-8, 10 | Platform skills |
| Backend | `exercises/backend/` | Weeks 4, 6 | Server development |
| ML/AI | `exercises/ml/` | Weeks 6, 9 | Model integration |
| Database | `exercises/database/` | Week 7 | Data persistence |
| Testing | `exercises/testing/` | Week 11 | Quality assurance |

### Documentation Templates

| Template | Location | Used In | Output |
|----------|----------|---------|--------|
| Proposal | `docs/proposal-template.md` | Week 01 | Project proposal |
| Final Report | `docs/final-report-template.md` | Week 12 | Submission document |
| Presentation | `docs/presentation-outline.md` | Week 12 | Slides structure |
| Architecture | `docs/architecture-diagram-guide.md` | Week 01, 12 | System diagrams |
| Viva Prep | `docs/viva-questions.md` | Week 12 | Defense preparation |

---

## 🎯 Learning Path Recommendations

### For Different Learning Styles

#### Sequential Learners (Linear Path)
1. Read `LEARNING_PATH.md` overview
2. Start Week 01 README
3. Complete in order: learning-notes → exercises → build-task → validation
4. Move to Week 02, repeat

#### Visual Learners (Diagram-First)
1. Start with `PROJECT_ARCHITECTURE.md`
2. Review notebooks for each week's concepts
3. Look at architecture diagrams before reading theory
4. Use visual aids in learning-notes

#### Hands-On Learners (Practice-First)
1. Skim README for objectives
2. Jump to exercises immediately
3. Refer to learning-notes when stuck
4. Check solutions after genuine attempts
5. Complete build-task with context

#### Theory Learners (Concept-First)
1. Read `COURSE_OVERVIEW.md` and `SYLLABUS_MAPPING.md`
2. Study learning-notes thoroughly
3. Watch/read notebooks for deeper understanding
4. Only then attempt exercises
5. Verify against solutions

### By Career Goal

#### Mobile Developer Focus
**Priority:** Weeks 2, 3, 5, 7, 10
**Deep Dive:** Android exercises, UI patterns, database
**Skip:** Backend details (use provided API)
**Extend:** Explore advanced Android topics in notebooks

#### Backend Developer Focus
**Priority:** Weeks 4, 5, 6
**Deep Dive:** Backend exercises, API design, ML serving
**Minimize:** Android UI details
**Extend:** Explore FastAPI advanced features

#### Full-Stack Focus
**Priority:** All weeks equally
**Deep Dive:** Connection points (Weeks 5, 6)
**Balance:** Frontend and backend equally
**Extend:** Integration patterns, system design

#### ML Engineer Focus
**Priority:** Weeks 6, 9
**Deep Dive:** ML exercises, model optimization
**Context:** Weeks 4-5 for serving
**Extend:** Model training, advanced preprocessing

---

## 🔄 Iterative Learning Loops

### Within Each Week
```
1. Overview (README) → 2. Theory (learning-notes)
         ↓                        ↓
8. Reflection ← 7. Quiz ← 3. Practice (exercises)
         ↓                        ↓
6. Validation ← 5. Build ← 4. Reference (solutions/notebooks)
```

### Across Weeks
```
Week N validation → Week N+1 prerequisites check
        ↓                        ↓
Week N evidence → Week N+1 builds on top
        ↓                        ↓
Week N reflection → Week N+1 concepts preview
```

### With External Resources
```
Official Docs → Learning Notes → Your Understanding
      ↓               ↓                  ↓
Stack Overflow → Solutions → Your Code → Reflection
      ↓               ↓                  ↓
GitHub Repos → Senior Analysis → Your Architecture
```

---

## 🆘 When You're Stuck

### Decision Tree for Getting Help

```
Stuck on a concept?
├─ Check GLOSSARY.md for term definitions
├─ Re-read relevant section in learning-notes.md
├─ Look for visual explanation in notebooks/
└─ Review related previous week's materials

Stuck on an exercise?
├─ Check if prerequisite exercises completed
├─ Review learning-notes for related concept
├─ Try for 30+ minutes before checking solutions/
└─ If still stuck, check solutions/ with intention to learn

Stuck on build task?
├─ Verify all exercises completed first
├─ Check if validation from previous week passed
├─ Look at notebooks for step-by-step walkthrough
├─ Review code examples in week folder
└─ Refer to SENIOR_REPO_ANALYSIS.md for patterns

Feature not working?
├─ Check validation-checklist.md for specific criteria
├─ Review common mistakes section in README
├─ Look at debugging section in Week 11 materials
├─ Check evidence/ folder for similar issues
└─ Compare with solutions/ for reference implementation
```

---

## 📊 Evidence Collection Matrix

| Week | Evidence Type | Stored In | Linked From | Used In |
|------|--------------|-----------|-------------|---------|
| 01 | Proposal document | `docs/evidence/week-01/` | validation-checklist | Week 12 report |
| 02 | UI screenshots | `docs/evidence/week-02/` | validation-checklist | Week 12 slides |
| 03 | Camera/permissions demo | `docs/evidence/week-03/` | validation-checklist | Demo video |
| 04 | Backend API logs | `docs/evidence/week-04/` | validation-checklist | Testing docs |
| 05 | Network requests | `docs/evidence/week-05/` | validation-checklist | Testing docs |
| 06 | Prediction results | `docs/evidence/week-06/` | validation-checklist | Final report |
| 07 | Database queries | `docs/evidence/week-07/` | validation-checklist | Testing docs |
| 08 | XML parsing output | `docs/evidence/week-08/` | validation-checklist | Testing docs |
| 09 | TFLite inference | `docs/evidence/week-09/` | validation-checklist | Performance comparison |
| 10 | Notifications/share | `docs/evidence/week-10/` | validation-checklist | Demo video |
| 11 | Test results table | `docs/evidence/week-11/` | validation/ | Final report |
| 12 | APK build logs | `docs/evidence/week-12/` | final-submission/ | Submission package |

---

## ⚡ Quick Reference

### Most Common Paths

**"I want to understand this week's concept"**
→ README → learning-notes → notebooks → exercises

**"I want to build this week's feature"**
→ README → exercises → build-task → validation-checklist

**"I'm stuck on an exercise"**
→ learning-notes → notebooks → solutions → try again

**"I need to verify my work"**
→ validation-checklist → evidence collection → progress-tracker

**"I'm preparing for viva"**
→ reflection.md (all weeks) → docs/viva-questions.md → GLOSSARY.md

---

**This cross-reference guide helps you navigate the rich, interconnected learning materials efficiently. Use it as a map to find the right resource at the right time! 🗺️**
