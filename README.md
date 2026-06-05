# LeafGuard AI - 12-Week Learning Roadmap

> **🎯 New! Unified Learning System:** Navigate seamlessly through all course materials with our [**Interactive Learning Path**](LEARNING_PATH.md) - featuring progressive navigation, exercises, solutions, and interactive notebooks!

## Project Overview

**LeafGuard AI** is a complete Android plant disease detection application built as a structured 12-week learning project for CSE 2206 (Mobile Application Development). This repository provides a comprehensive roadmap that guides you through building a production-quality Android app from scratch, integrating AI/ML capabilities, backend services, and satisfying every requirement of the university course syllabus.

## What You Will Build

A fully functional Android mobile application featuring:

- **Android UI & Navigation**: Multi-screen app with XML layouts and navigation flow
- **Camera & Gallery**: Image capture using camera intent and gallery picker with runtime permissions
- **Cloud AI Mode**: Python FastAPI backend with HTTP POST multipart image upload and JSON response parsing
- **On-Device AI Mode**: TensorFlow Lite offline inference for no-internet scenarios
- **Result Screen**: Disease name, confidence percentage, symptoms, treatment, and prevention advice
- **Scan History**: Room/SQLite local database with Entity, DAO, list view, detail view, and delete functionality
- **XML Disease Library**: Local XML file in assets folder, parsed on-device, mapped to prediction labels
- **Share Functionality**: Android share intent for app-to-app communication
- **Reminder Notifications**: NotificationChannel and PendingIntent implementation
- **Optional Location Tagging**: Attempt scan location feature with clear documentation
- **Testing & Debugging**: Complete test case table, debugging logs, error handling
- **APK Build & Deployment**: Production-ready APK with installation demo
- **Complete Documentation**: Academic proposal, final report, presentation slides, demo video, viva preparation

## Tech Stack

**Android (Java)**
- Android Studio, SDK 21+, Gradle
- XML layouts, Activities, Fragments
- Camera2 API / Camera Intent
- Room Database, SharedPreferences
- Retrofit/OkHttp for networking
- TensorFlow Lite for on-device AI

**Backend (Python)**
- FastAPI web framework
- Uvicorn ASGI server
- TensorFlow/PyTorch for ML inference
- Multipart file upload handling

**Machine Learning**
- Pre-trained plant disease CNN model
- TensorFlow Lite model conversion
- Image preprocessing pipeline
- Label mapping and confidence scores

**Development Tools**
- Git version control
- Postman for API testing
- Android Debug Bridge (ADB)
- Android Emulator / Physical device

## Repository Structure

```
LeafGuard-AI/
├── README.md                          # This file
├── LEARNING_PATH.md                   # 🆕 Unified learning navigator
├── COURSE_OVERVIEW.md                 # CSE 2206 context
├── LEARNING_RULES.md                  # Strict learning rules
├── SYLLABUS_MAPPING.md               # Every syllabus topic mapped to weeks
├── PROJECT_ARCHITECTURE.md            # System architecture diagram
├── SENIOR_REPO_ANALYSIS.md           # How to analyze senior repos
├── GLOSSARY.md                        # Technical terms dictionary
├── progress-tracker.md                # 12-week checkbox tracker
│
├── roadmap/                           # 12 weekly learning packages
│   ├── NAVIGATION_TEMPLATE.md        # 🆕 Navigation guide for weeks
│   ├── week-01-project-understanding/
│   ├── week-02-android-basics-ui/
│   ├── week-03-camera-gallery/
│   ├── week-04-fastapi-backend/
│   ├── week-05-android-networking/
│   ├── week-06-cloud-ml-model/
│   ├── week-07-room-sqlite-history/
│   ├── week-08-xml-disease-library/
│   ├── week-09-tensorflow-lite-offline-ai/
│   ├── week-10-notifications-share-location/
│   ├── week-11-testing-debugging-performance/
│   └── week-12-final-submission/
│
├── exercises/                         # Practice exercises by topic
│   ├── android/
│   ├── backend/
│   ├── ml/
│   ├── database/
│   └── testing/
│
├── solutions/                         # 🆕 Exercise solutions
│   ├── README.md                     # How to use solutions effectively
│   ├── week-01/ through week-12/    # Week-by-week solutions
│
├── notebooks/                         # 🆕 Interactive Jupyter notebooks
│   ├── README.md                     # Getting started with notebooks
│   ├── week-01/ through week-12/    # Interactive learning materials
│
├── android-app/                       # Your Android project goes here
├── backend-api/                       # Your FastAPI backend goes here
├── model/                             # ML models and labels
├── sample-images/                     # Test leaf images
│
├── docs/                              # Documentation templates
│   ├── proposal-template.md
│   ├── final-report-template.md
│   ├── presentation-outline.md
│   ├── architecture-diagram-guide.md
│   ├── viva-questions.md
│   └── evidence/                      # Weekly screenshots & logs
│
├── validation/                        # Validation checklists
├── reflection-journal/                # Weekly reflection prompts
└── final-submission/                  # Submission checklist & scripts
```

## 🚀 Quick Start Options

### Option 1: Interactive Learning Path (Recommended)
**Best for:** Visual learners who want seamless navigation

➡️ **[Start with the Unified Learning Path](LEARNING_PATH.md)**

Features:
- Progressive week-by-week navigation
- Direct links to all materials (exercises, solutions, notebooks)
- Quick jump between any week or resource
- Learning progress tracking
- Multiple learning modalities

### Option 2: Traditional Week-by-Week
**Best for:** Sequential learners who prefer structured approach

Start with Week 01 and follow the progression below.

---

## How To Use This Roadmap

### Week-by-Week Workflow

Each week follows this structure:

1. **Read the Weekly README** (`roadmap/week-XX/README.md`)
   - Understand objectives and syllabus topics
   - Check prerequisites
   - Review concepts before coding

2. **Study Learning Notes** (`learning-notes.md`)
   - Deep dive into theoretical concepts
   - Understand the "why" before the "how"

3. **Complete Exercises** (`exercises.md`)
   - Minimum 6 hands-on exercises
   - Clear expected outputs
   - Build confidence incrementally

4. **Build Task** (`build-task.md`)
   - Implement the week's feature
   - Follow step-by-step guide
   - Use minimal starter snippets (no full solutions)

5. **Validate Your Work** (`validation-checklist.md`)
   - Pass/fail validation only
   - Feature either works or it doesn't
   - No gray areas

6. **Take the Quiz** (`quiz.md`)
   - Test your understanding
   - Mix of conceptual and practical questions

7. **Write Reflection** (`reflection.md`)
   - Explain concepts in your own words
   - Document what you learned
   - Note challenges and solutions

8. **Save Evidence** (`docs/evidence/week-XX/`)
   - Screenshots of working features
   - Debug logs
   - Demo video clips

### Daily Routine

**Morning** (30-60 minutes)
- Read theory and concepts
- Watch recommended tutorials
- Take notes in your own words

**Afternoon** (2-3 hours)
- Complete exercises
- Work on build task
- Debug and test

**Evening** (30 minutes)
- Review validation checklist
- Commit your work with meaningful messages
- Update progress tracker

### Git Workflow

Each week:
```bash
# Create feature branch
git checkout -b week-XX-feature-name

# Make incremental commits
git add .
git commit -m "week-XX: implement [specific feature]"

# Push and track progress
git push origin week-XX-feature-name
```

## Setup Instructions

### Prerequisites

- Computer with 8GB+ RAM
- Windows 10/11, macOS, or Linux
- Stable internet connection
- GitHub account

### Day 1 Setup

1. **Clone this repository**
   ```bash
   git clone https://github.com/Raj-Indra-Asura/LeafGuard-AI-An-Android-Based-Plant-Disease-Detection-Application.git
   cd LeafGuard-AI-An-Android-Based-Plant-Disease-Detection-Application
   ```

2. **Read foundational documents**
   - `COURSE_OVERVIEW.md` - Understand CSE 2206 context
   - `LEARNING_RULES.md` - Learn the learning rules
   - `SYLLABUS_MAPPING.md` - See how weeks map to syllabus
   - `PROJECT_ARCHITECTURE.md` - Understand system design

3. **Open Week 01 folder**
   ```bash
   cd roadmap/week-01-project-understanding
   ```

4. **Start with Week 01 README**
   - Read all 16 sections
   - Complete learning notes
   - Begin exercises

5. **Fill out SENIOR_REPO_ANALYSIS.md**
   - Search GitHub for Android plant disease projects
   - Analyze their structure
   - Document findings in the table

### Tools Installation (Week 02)

You will install development tools in Week 02:
- Android Studio
- Java JDK 11+
- Android SDK and emulator
- Python 3.8+
- FastAPI and dependencies

## CSE 2206 Syllabus Coverage

This project satisfies **every single topic** in the CSE 2206 syllabus:

| Syllabus Topic | LeafGuard AI Implementation | Week |
|----------------|----------------------------|------|
| Mobile app development intro | Native Android app | 01 |
| Platform comparison | Android vs iOS report section | 01 |
| Dev environment setup | Android Studio + FastAPI setup | 02 |
| Java for Android | Java code throughout | 02-12 |
| Designing applications | Multi-screen UI design | 02-03 |
| XML parsing | Disease library XML file | 08 |
| HTTP POST/GET | Image upload to FastAPI | 05 |
| App-to-app communication | Share intent | 10 |
| Notifications | Reminder notifications | 10 |
| Maps and location | Optional scan location tag | 10 |
| Multimedia | Camera and gallery | 03 |
| Files and storage | Room database, image files | 07 |
| Network programming | Retrofit HTTP client | 05 |
| Debugging | Logs and error handling | 11 |
| Testing | Test case table | 11 |
| Deployment | APK build | 12 |

See `SYLLABUS_MAPPING.md` for complete detailed mapping.

## Learning Rules

### Strict Rules (Read LEARNING_RULES.md)

1. **Never copy code without understanding**
2. **Explain before you code**
3. **Commit every week with meaningful messages**
4. **Validate before moving to next week**
5. **Connect every feature to syllabus topics**
6. **Save evidence for teacher demonstration**

### Time Commitment

- **Minimum**: 15-20 hours per week
- **Recommended**: 25-30 hours per week
- **Total**: 180-300 hours over 12 weeks

### Success Criteria

You have successfully completed this roadmap when:

- All 12 weekly validation checklists are passed
- Android app APK builds and runs
- Backend API is functional
- Test cases show pass results
- Final report is complete
- Demo video is recorded
- You can explain every feature in your own words during viva

## Weekly Breakdown

### Foundation Phase (Weeks 1-3)
- **Week 01**: Project understanding, proposal, senior repo analysis
- **Week 02**: Android Studio setup, UI skeleton, navigation
- **Week 03**: Camera, gallery, permissions, image handling

### Backend Integration (Weeks 4-6)
- **Week 04**: FastAPI backend, basic prediction endpoint
- **Week 05**: Android networking with Retrofit, HTTP POST
- **Week 06**: Real ML model integration, preprocessing

### Data & Offline Features (Weeks 7-9)
- **Week 07**: Room database, scan history
- **Week 08**: XML disease library, parsing
- **Week 09**: TensorFlow Lite offline AI mode

### Polish & Extras (Weeks 10-12)
- **Week 10**: Share intent, notifications, location
- **Week 11**: Testing, debugging, performance comparison
- **Week 12**: Final polish, APK, documentation, submission

## Teacher Demonstration Strategy

Throughout this project, you'll build evidence for teacher demonstrations:

1. **Weekly demos**: Show each week's completed feature
2. **Screenshot collection**: Save in `docs/evidence/week-XX/`
3. **Talking points**: Use "Teacher impression tips" from weekly READMEs
4. **Viva framing**: Use the exact paragraph from `final-submission/demo-video-script.md`

### Key Viva Statement

"I did not build only an AI model. I built a complete Android mobile application according to the CSE 2206 syllabus. The app includes camera input, HTTP POST communication, XML disease data, local storage, app-to-app sharing, notifications, testing, debugging, and APK deployment. The AI part is implemented in two ways: cloud inference using FastAPI and offline inference using TensorFlow Lite."

## Important Notes

### This is a Functional Starter, Not a Final Diagnostic Product

This repository now includes connected starter flows for Android UI, cloud prediction, offline prediction, XML library, and history. It remains a **learning system**: replace starter/mock assets with trained models and real evidence before final submission. You must:

- Walk every step yourself
- Understand before implementing
- Make mistakes and debug them
- Build muscle memory through repetition
- Explain concepts in your own words

### Manual Tasks Required

After cloning this repository, you must:

1. Review and run the starter Android project in `android-app/` (Week 02)
2. Review and run the starter Python FastAPI project in `backend-api/` (Week 04)
3. Replace the backend mock/fallback with a trained plant disease ML model (Week 06)
4. Replace the starter TensorFlow Lite placeholder with a converted trained model (Week 09)
5. Supplement `sample-images/` with real licensed leaf disease images (Week 06)
6. Fill in `SENIOR_REPO_ANALYSIS.md` table (Week 01)
7. Take weekly screenshots and save in `docs/evidence/`

### Model Limitations

The AI model you integrate will have limitations:

- May not achieve 95%+ accuracy
- Limited to specific plant species
- Requires good lighting conditions
- Focus your demo on **mobile engineering excellence**, not AI perfection

### Local Backend Strategy

The primary demo approach uses **local backend**:

1. Run FastAPI on your laptop
2. Connect phone and laptop to same Wi-Fi
3. Use laptop's local IP (e.g., 192.168.1.x) as Retrofit base URL
4. Demo to teacher with this setup

Cloud deployment is optional and NOT required for course completion.

## Support & Resources

### Within This Repository

- `GLOSSARY.md` - Definitions of every technical term
- `docs/viva-questions.md` - 60+ practice viva questions
- `docs/architecture-diagram-guide.md` - How to draw system architecture
- Each weekly README - "Common mistakes to avoid" section

### External Resources

**Official Documentation** (always consult first)
- Android Developer Guides: https://developer.android.com/guide
- FastAPI Documentation: https://fastapi.tiangolo.com/
- TensorFlow Lite: https://www.tensorflow.org/lite

**Learning Platforms**
- Google Codelabs for Android
- YouTube tutorials (verify credibility)
- Stack Overflow (understand, don't blindly copy)

### Getting Help

1. **Read the weekly README thoroughly**
2. **Check GLOSSARY.md for term definitions**
3. **Review common mistakes section**
4. **Debug using logs and error messages**
5. **Ask specific questions** (not "it doesn't work")

## Contribution & Collaboration

### This is YOUR Learning Journey

- Do NOT copy classmates' implementations
- You may discuss concepts together
- Write your own code
- Understand every line you write

### Academic Integrity

This project must be completed individually. Collaboration on understanding concepts is encouraged, but code must be your own work.

## License

This learning roadmap is provided for educational purposes as part of CSE 2206 Mobile Application Development course.

## Acknowledgments

- CSE 2206 course instructors and syllabus
- Android open-source community
- Plant disease dataset providers
- FastAPI and TensorFlow Lite teams

---

## 🎯 Quick Start Checklist

- [ ] Clone this repository
- [ ] 🆕 **[Explore the Unified Learning Path](LEARNING_PATH.md)** ⭐ Recommended!
- [ ] Or use the **[Quick Navigation Guide](QUICK_NAV.md)** for fast access
- [ ] Read `COURSE_OVERVIEW.md` - Understand CSE 2206 context
- [ ] Read `LEARNING_RULES.md` - Learn effective strategies
- [ ] Read `SYLLABUS_MAPPING.md` - See topic coverage
- [ ] Read `PROJECT_ARCHITECTURE.md` - Understand system design
- [ ] Open `roadmap/week-01-project-understanding/README.md` - Start Week 01
- [ ] Start Week 01 exercises
- [ ] Begin filling out `progress-tracker.md`

### 🚀 Multiple Ways to Start:

**Option 1 (Recommended):** Use the **[Unified Learning Path](LEARNING_PATH.md)** for seamless navigation

**Option 2:** Use the **[Quick Navigation Guide](QUICK_NAV.md)** to jump directly to what you need

**Option 3:** Traditional approach - Open `roadmap/week-01-project-understanding/README.md` now!

---

## 📚 Navigation & Learning Resources

### Core Navigation Documents
- **[LEARNING_PATH.md](LEARNING_PATH.md)** - 🆕 Unified learning system with progressive navigation
- **[QUICK_NAV.md](QUICK_NAV.md)** - 🆕 Quick access to any resource
- **[CROSS_REFERENCE.md](CROSS_REFERENCE.md)** - 🆕 How all materials interconnect
- **[progress-tracker.md](progress-tracker.md)** - Track your 12-week journey

### Supplementary Learning Materials
- **[solutions/](solutions/)** - 🆕 Exercise solutions and reference implementations
- **[notebooks/](notebooks/)** - 🆕 Interactive Jupyter notebooks for hands-on learning
- **[exercises/](exercises/)** - Practice problems organized by topic

### Essential Reference Documents
- **[GLOSSARY.md](GLOSSARY.md)** - Technical terms dictionary
- **[COURSE_OVERVIEW.md](COURSE_OVERVIEW.md)** - CSE 2206 course context
- **[SYLLABUS_MAPPING.md](SYLLABUS_MAPPING.md)** - Complete topic mapping
- **[PROJECT_ARCHITECTURE.md](PROJECT_ARCHITECTURE.md)** - System architecture
- **[LEARNING_RULES.md](LEARNING_RULES.md)** - How to learn effectively
