# LeafGuard AI - Final Submission Checklist

**Complete Deliverables Verification**

**Project Name:** LeafGuard AI - An Android-Based Plant Disease Detection Application
**Course Code:** CSE 2206 - Mobile Application Development
**Student Name:** ___________________________________
**Roll Number:** ___________________________________
**Submission Date:** ___________________________________

---

## Instructions

- Check **[✓]** each item when it is complete and verified
- If an item is incomplete, mark **[✗]** and note the reason in the "Notes" column
- All items marked **CRITICAL** must be completed for submission to be accepted
- Save a signed PDF copy of this checklist for final submission

---

## SECTION 1: SOURCE CODE

### 1.1 Android Application Source Code

| # | Item | Status | Notes |
|---|------|--------|-------|
| 1.1.1 | Complete Kotlin Android project (primary track) with all source files | [ ] | Path: `android-app-kotlin/` |
| 1.1.2 | All Kotlin source files in `app/src/main/java/com/leafguard/` | [ ] | Six activities + database, network, ml, utils packages. (Java secondary twin in `android-app/` — optional to submit) |
| 1.1.3 | All XML layout files in `app/src/main/res/layout/` | [ ] | activity_main.xml, activity_result.xml, etc. |
| 1.1.4 | All resource files (strings.xml, colors.xml, styles.xml) | [ ] | In `app/src/main/res/values/` |
| 1.1.5 | AndroidManifest.xml with all permissions declared | [ ] | CAMERA, INTERNET, STORAGE, LOCATION |
| 1.1.6 | build.gradle (app level) with all dependencies | [ ] | Retrofit, Room, Gson, TFLite |
| 1.1.7 | build.gradle (project level) | [ ] | Root build configuration |
| 1.1.8 | Project builds without errors in Android Studio | [ ] | **CRITICAL** - Test before submission |
| 1.1.9 | .gitignore properly configured (no build artifacts committed) | [ ] | Exclude: `build/`, `.idea/`, `*.apk` (optional) |

**Android Source Code Verification Signature:** ___________________________

---

### 1.2 Backend API Source Code

| # | Item | Status | Notes |
|---|------|--------|-------|
| 1.2.1 | Complete FastAPI backend folder | [ ] | Path: `backend-api/` or `LeafGuardAI-Backend/` |
| 1.2.2 | main.py with FastAPI app and /predict endpoint | [ ] | **CRITICAL** |
| 1.2.3 | model_loader.py (or equivalent) for loading ML model | [ ] | Model initialization logic |
| 1.2.4 | image_processor.py (or equivalent) for preprocessing | [ ] | Resize, normalize, convert to array |
| 1.2.5 | requirements.txt with all Python dependencies | [ ] | fastapi, uvicorn, tensorflow/pytorch, pillow |
| 1.2.6 | config.py or equivalent for configuration | [ ] | Model path, labels, server settings |
| 1.2.7 | README.md in backend folder with setup instructions | [ ] | Installation and running instructions |
| 1.2.8 | Backend runs without errors (`uvicorn main:app --reload`) | [ ] | **CRITICAL** - Test before submission |

**Backend Source Code Verification Signature:** ___________________________

---

## SECTION 2: MACHINE LEARNING MODEL FILES

### 2.1 Cloud Model (Backend)

| # | Item | Status | Notes |
|---|------|--------|-------|
| 2.1.1 | ML model file for backend | [ ] | .h5 (TensorFlow), .pt (PyTorch), or .onnx |
| 2.1.2 | Model file location documented | [ ] | Path: `backend-api/models/` or documented link |
| 2.1.3 | labels.txt or label mapping file for backend | [ ] | Class names matching model output indices |
| 2.1.4 | Model source documented (trained custom or pre-trained) | [ ] | In report or model documentation |

---

### 2.2 On-Device Model (Android)

| # | Item | Status | Notes |
|---|------|--------|-------|
| 2.2.1 | TensorFlow Lite model file (.tflite) | [ ] | **CRITICAL** |
| 2.2.2 | TFLite model in Android assets folder | [ ] | Path: `app/src/main/assets/model.tflite` |
| 2.2.3 | labels.txt in Android assets folder | [ ] | Path: `app/src/main/assets/labels.txt` |
| 2.2.4 | TFLite model tested and working in app | [ ] | **CRITICAL** - Offline mode functional |
| 2.2.5 | Model conversion process documented | [ ] | TensorFlow → TFLite conversion notes |

**ML Model Files Verification Signature:** ___________________________

---

## SECTION 3: DATA FILES

### 3.1 XML Disease Library

| # | Item | Status | Notes |
|---|------|--------|-------|
| 3.1.1 | diseases.xml file created | [ ] | **CRITICAL** |
| 3.1.2 | XML file in Android assets folder | [ ] | Path: `app/src/main/assets/diseases.xml` |
| 3.1.3 | XML contains at least 10 disease entries | [ ] | Each with: name, symptoms, treatment, prevention |
| 3.1.4 | XML is well-formed (validated) | [ ] | No syntax errors, parseable by XmlPullParser |
| 3.1.5 | XML parsing tested in app | [ ] | Disease library screen displays all diseases |

---

### 3.2 Sample Test Images

| # | Item | Status | Notes |
|---|------|--------|-------|
| 3.2.1 | At least 10 sample leaf images included | [ ] | Various diseases and healthy leaves |
| 3.2.2 | Images organized in folder | [ ] | Path: `sample-images/` or `test-images/` |
| 3.2.3 | Image filenames indicate disease type | [ ] | E.g., `tomato_early_blight_01.jpg` |
| 3.2.4 | Images tested with app (successful predictions) | [ ] | Documented in test case table |

**Data Files Verification Signature:** ___________________________

---

## SECTION 4: BUILD ARTIFACTS

### 4.1 Android APK File

| # | Item | Status | Notes |
|---|------|--------|-------|
| 4.1.1 | Kotlin app APK built successfully (primary) | [ ] | **CRITICAL** — build from `android-app-kotlin/`. An APK (Android Package) is the installable app file. Java-track APK from `android-app/` is optional/secondary. |
| 4.1.2 | APK file named clearly | [ ] | E.g., `LeafGuardAI_v1.0.apk` or `app-release.apk` |
| 4.1.3 | APK file size reasonable (<100MB preferred) | [ ] | If >100MB, document why |
| 4.1.4 | APK tested on at least one physical device | [ ] | **CRITICAL** - Installation and functionality |
| 4.1.5 | APK tested on at least one emulator | [ ] | Different Android version than physical device |
| 4.1.6 | APK location documented | [ ] | Path: `final-submission/LeafGuardAI_v1.0.apk` |
| 4.1.7 | APK uploaded to GitHub Releases (optional but recommended) | [ ] | Or included in submission ZIP |
| 4.1.8 | APK signing documented | [ ] | Debug or release signing explained |

**APK Build Verification Signature:** ___________________________

---

## SECTION 5: DOCUMENTATION

### 5.1 README Files

| # | Item | Status | Notes |
|---|------|--------|-------|
| 5.1.1 | Root README.md in repository | [ ] | **CRITICAL** - Professional, complete |
| 5.1.2 | README includes: Project title, description, features | [ ] | Clear overview of app |
| 5.1.3 | README includes: Screenshots of all major screens | [ ] | At least 6-8 screenshots |
| 5.1.4 | README includes: Technology stack with versions | [ ] | Android, Kotlin (primary; Java secondary), Retrofit, Room, TFLite, FastAPI |
| 5.1.5 | README includes: Setup instructions for Android app | [ ] | How to build and run |
| 5.1.6 | README includes: Setup instructions for backend | [ ] | Python environment, dependencies, running |
| 5.1.7 | README includes: Usage instructions | [ ] | How to use the app features |
| 5.1.8 | README includes: Syllabus mapping summary | [ ] | Which features cover which CSE 2206 topics |
| 5.1.9 | README includes: Demo video link | [ ] | YouTube/Drive link working |
| 5.1.10 | README includes: Contributors and license | [ ] | Your name, MIT/Apache license |

---

### 5.2 Project Proposal

| # | Item | Status | Notes |
|---|------|--------|-------|
| 5.2.1 | Project proposal document created | [ ] | **CRITICAL** |
| 5.2.2 | Proposal includes: Problem statement | [ ] | Why plant disease detection matters |
| 5.2.3 | Proposal includes: Objectives (3-5 clear goals) | [ ] | Measurable and specific |
| 5.2.4 | Proposal includes: Methodology and approach | [ ] | Android + Backend + ML approach |
| 5.2.5 | Proposal includes: Timeline (12-week plan) | [ ] | Week-by-week breakdown |
| 5.2.6 | Proposal includes: Feature list with syllabus mapping | [ ] | All 15+ features mapped to topics |
| 5.2.7 | Proposal includes: Risk analysis | [ ] | Challenges and mitigation |
| 5.2.8 | Proposal exported to PDF | [ ] | Path: `docs/proposal.pdf` or `final-submission/proposal.pdf` |
| 5.2.9 | Proposal approved by supervisor (if applicable) | [ ] | Signature or email confirmation |

---

### 5.3 Architecture Diagram

| # | Item | Status | Notes |
|---|------|--------|-------|
| 5.3.1 | System architecture diagram created | [ ] | **CRITICAL** |
| 5.3.2 | Diagram shows: Android app, Retrofit, FastAPI, ML model, Room DB | [ ] | All major components |
| 5.3.3 | Diagram shows: Data flow arrows with labels | [ ] | Request/response paths |
| 5.3.4 | Diagram shows: Cloud mode and offline mode flows | [ ] | Two inference paths |
| 5.3.5 | Diagram is professional and clear | [ ] | Use draw.io, Lucidchart, or similar |
| 5.3.6 | Diagram exported as image (PNG or PDF) | [ ] | High resolution (300dpi minimum) |
| 5.3.7 | Diagram included in report | [ ] | Referenced in System Design section |
| 5.3.8 | Diagram included in presentation slides | [ ] | System Architecture slide |

**Documentation Verification Signature:** ___________________________

---

### 5.4 Test Cases and Results

| # | Item | Status | Notes |
|---|------|--------|-------|
| 5.4.1 | Test case table created | [ ] | **CRITICAL** |
| 5.4.2 | Test cases cover all features | [ ] | Camera, gallery, upload, prediction, history, etc. |
| 5.4.3 | Test cases include edge cases | [ ] | No internet, invalid image, permission denied |
| 5.4.4 | At least 60 test cases documented | [ ] | As mentioned in syllabus coverage |
| 5.4.5 | Each test case has: ID, Description, Input, Expected, Actual, Status | [ ] | Standard test case format |
| 5.4.6 | Test results include: PASS/FAIL status | [ ] | All or most marked PASS |
| 5.4.7 | Failed test cases explained | [ ] | Reason for failure and resolution |
| 5.4.8 | Test case table included in report | [ ] | In Testing section or Appendix |
| 5.4.9 | Test case table exported separately | [ ] | Excel (.xlsx) or PDF format |
| 5.4.10 | Screenshots of test execution saved | [ ] | Evidence of testing |

---

### 5.5 Final Report PDF

| # | Item | Status | Notes |
|---|------|--------|-------|
| 5.5.1 | Final report document created | [ ] | **CRITICAL** |
| 5.5.2 | Report follows template structure | [ ] | Title, abstract, intro, literature, design, implementation, testing, results, conclusion |
| 5.5.3 | Report length: 15-25 pages | [ ] | Excluding front matter and appendices |
| 5.5.4 | Report includes: Title page with name, course, date | [ ] | Professional formatting |
| 5.5.5 | Report includes: Declaration and signature | [ ] | "This is my original work" statement |
| 5.5.6 | Report includes: Abstract (150-250 words) | [ ] | Summarizes entire project |
| 5.5.7 | Report includes: Table of Contents (auto-generated) | [ ] | Page numbers correct |
| 5.5.8 | Report includes: List of Figures | [ ] | All figures numbered and listed |
| 5.5.9 | Report includes: List of Tables | [ ] | All tables numbered and listed |
| 5.5.10 | Report includes: Introduction section | [ ] | Background, problem, objectives, scope |
| 5.5.11 | Report includes: Literature Review section | [ ] | Related work, existing systems |
| 5.5.12 | Report includes: System Design section | [ ] | Architecture diagram, technology stack, data flow |
| 5.5.13 | Report includes: Implementation section | [ ] | Code snippets, challenges, solutions |
| 5.5.14 | Report includes: Testing section | [ ] | Test cases, results, edge cases |
| 5.5.15 | Report includes: Results section | [ ] | Screenshots, performance metrics |
| 5.5.16 | Report includes: CSE 2206 Syllabus Mapping table | [ ] | **CRITICAL** - All 15+ topics covered |
| 5.5.17 | Report includes: Limitations section | [ ] | Honest about model accuracy, feature gaps |
| 5.5.18 | Report includes: Future Work section | [ ] | Enhancements and scalability |
| 5.5.19 | Report includes: Conclusion section | [ ] | Achievements, learning outcomes |
| 5.5.20 | Report includes: References (at least 15 sources) | [ ] | IEEE or APA format |
| 5.5.21 | Report includes: Appendices | [ ] | Complete test cases, code samples, user manual |
| 5.5.22 | Report formatting: Professional font and spacing | [ ] | Times New Roman 12pt or Arial 11pt, 1.5 spacing |
| 5.5.23 | Report formatting: All figures captioned and referenced | [ ] | "Figure 1: Architecture Diagram" |
| 5.5.24 | Report formatting: All tables captioned and referenced | [ ] | "Table 1: Test Case Results" |
| 5.5.25 | Report formatting: Page numbers on all pages | [ ] | Except title page |
| 5.5.26 | Report exported to PDF format | [ ] | **CRITICAL** |
| 5.5.27 | Report PDF file size reasonable (<20MB) | [ ] | Compress images if needed |
| 5.5.28 | Report PDF named clearly | [ ] | E.g., `LeafGuardAI_FinalReport_YourName.pdf` |
| 5.5.29 | Report spell-checked and grammar-checked | [ ] | **CRITICAL** - Use Grammarly or similar |
| 5.5.30 | Report reviewed by peer or supervisor (optional) | [ ] | Feedback incorporated |

**Final Report Verification Signature:** ___________________________

---

### 5.6 Presentation Slides PDF

| # | Item | Status | Notes |
|---|------|--------|-------|
| 5.6.1 | Presentation slide deck created | [ ] | **CRITICAL** |
| 5.6.2 | Slide deck follows recommended structure | [ ] | 12-15 slides |
| 5.6.3 | Slides include: Title slide | [ ] | Project name, your name, course, date |
| 5.6.4 | Slides include: Agenda slide | [ ] | Presentation outline |
| 5.6.5 | Slides include: Problem Statement (1-2 slides) | [ ] | Why this project matters |
| 5.6.6 | Slides include: Objectives slide | [ ] | 3-5 clear goals |
| 5.6.7 | Slides include: System Architecture (1-2 slides) | [ ] | Diagram and technology stack |
| 5.6.8 | Slides include: Key Features (2-3 slides) | [ ] | Screenshots of 8-10 features |
| 5.6.9 | Slides include: Implementation Highlights (1-2 slides) | [ ] | Challenges and solutions |
| 5.6.10 | Slides include: Demo transition slide | [ ] | "Let's see it in action" |
| 5.6.11 | Slides include: Testing and Results (1-2 slides) | [ ] | Test coverage, performance metrics |
| 5.6.12 | Slides include: Syllabus Coverage slide | [ ] | **CRITICAL** - Shows CSE 2206 topics covered |
| 5.6.13 | Slides include: Limitations and Future Work slide | [ ] | Honest and forward-looking |
| 5.6.14 | Slides include: Conclusion slide | [ ] | Key achievements |
| 5.6.15 | Slides include: Thank You / Questions slide | [ ] | Contact information |
| 5.6.16 | Slides formatting: Professional design and colors | [ ] | Consistent theme throughout |
| 5.6.17 | Slides formatting: Clear and readable text | [ ] | Minimum 18pt font, high contrast |
| 5.6.18 | Slides formatting: Visuals on every slide | [ ] | Screenshots, diagrams, icons |
| 5.6.19 | Slides formatting: Not text-heavy | [ ] | Bullet points, not paragraphs |
| 5.6.20 | Slides exported to PDF format | [ ] | **CRITICAL** |
| 5.6.21 | Slides PDF named clearly | [ ] | E.g., `LeafGuardAI_Presentation_YourName.pdf` |
| 5.6.22 | Slides tested on presentation laptop/projector | [ ] | Fonts and formatting preserved |
| 5.6.23 | Presentation rehearsed (8-10 minutes) | [ ] | Timed and practiced |

**Presentation Slides Verification Signature:** ___________________________

---

### 5.7 Demo Video

| # | Item | Status | Notes |
|---|------|--------|-------|
| 5.7.1 | Demo video recorded | [ ] | **CRITICAL** |
| 5.7.2 | Video follows demo-video-script.md | [ ] | 19-step flow |
| 5.7.3 | Video duration: 3-5 minutes | [ ] | **CRITICAL** - Strictly enforced |
| 5.7.4 | Video includes: Introduction with framing statement | [ ] | "This is a complete Android app, not just AI" |
| 5.7.5 | Video includes: All 19 demonstration steps | [ ] | Camera, gallery, cloud, offline, history, share, notification, XML, location |
| 5.7.6 | Video includes: Conclusion with technologies and syllabus | [ ] | Summary of achievements |
| 5.7.7 | Video quality: 1080p or 720p minimum | [ ] | Clear and sharp |
| 5.7.8 | Video audio: Clear narration throughout | [ ] | No background noise |
| 5.7.9 | Video format: MP4 (H.264) | [ ] | Universal compatibility |
| 5.7.10 | Video file size: Under 200MB (preferred) or 500MB (max) | [ ] | Compressed if needed |
| 5.7.11 | Video uploaded to YouTube (Unlisted or Public) | [ ] | Recommended hosting option |
| 5.7.12 | OR Video uploaded to Google Drive (public link) | [ ] | Alternative hosting |
| 5.7.13 | OR Video uploaded to university portal | [ ] | If required |
| 5.7.14 | Video link working and accessible | [ ] | **CRITICAL** - Tested in incognito browser |
| 5.7.15 | Video link saved in demo-video-link.txt | [ ] | Path: `final-submission/demo-video-link.txt` |
| 5.7.16 | OR Video file included in submission | [ ] | If file-based submission required |
| 5.7.17 | Video edited (removed pauses and mistakes) | [ ] | Professional quality |
| 5.7.18 | Video includes title slide and credits | [ ] | At beginning and end |

**Demo Video Verification Signature:** ___________________________

---

## SECTION 6: GITHUB REPOSITORY

### 6.1 Repository Setup

| # | Item | Status | Notes |
|---|------|--------|-------|
| 6.1.1 | GitHub repository created | [ ] | **CRITICAL** |
| 6.1.2 | Repository name is clear and professional | [ ] | E.g., `LeafGuard-AI-Android-App` |
| 6.1.3 | Repository visibility: Public | [ ] | Or private with evaluator access granted |
| 6.1.4 | Repository README.md complete (see Section 5.1) | [ ] | Professional and comprehensive |
| 6.1.5 | Repository .gitignore properly configured | [ ] | No build artifacts, IDE files |

---

### 6.2 Repository Structure

| # | Item | Status | Notes |
|---|------|--------|-------|
| 6.2.1 | All Android source code committed | [ ] | Complete project folder |
| 6.2.2 | All backend source code committed | [ ] | Complete FastAPI folder |
| 6.2.3 | Model files documented or linked | [ ] | TFLite in repo, cloud model documented |
| 6.2.4 | diseases.xml committed | [ ] | In Android assets |
| 6.2.5 | labels.txt committed | [ ] | In Android assets |
| 6.2.6 | Sample test images committed | [ ] | In `sample-images/` folder |
| 6.2.7 | Documentation files committed | [ ] | Proposal, report, slides (or links) |
| 6.2.8 | Evidence folder with weekly screenshots | [ ] | Path: `docs/evidence/` or `evidence/` |
| 6.2.9 | LICENSE file included | [ ] | MIT or Apache 2.0 recommended |

---

### 6.3 Repository Quality

| # | Item | Status | Notes |
|---|------|--------|-------|
| 6.3.1 | Commit history shows progressive development | [ ] | Not all code in one commit |
| 6.3.2 | Commit messages are clear and descriptive | [ ] | "Add camera capture feature" not "update" |
| 6.3.3 | Code is well-commented | [ ] | Java and Python code documented |
| 6.3.4 | No sensitive data in repository | [ ] | No API keys, passwords, personal info |
| 6.3.5 | Repository has GitHub Release with APK | [ ] | Optional but professional |
| 6.3.6 | Repository pinned on your GitHub profile | [ ] | Optional but recommended |

---

### 6.4 Repository Link

| # | Item | Status | Notes |
|---|------|--------|-------|
| 6.4.1 | Repository URL documented | [ ] | **CRITICAL** |
| 6.4.2 | Repository URL included in final report | [ ] | Footer or references section |
| 6.4.3 | Repository URL included in presentation slides | [ ] | Thank You slide or footer |
| 6.4.4 | Repository URL accessible to evaluators | [ ] | **CRITICAL** - Tested from different account |
| 6.4.5 | Repository URL format correct | [ ] | `https://github.com/username/repo-name` |

**GitHub Repository Verification Signature:** ___________________________

**Repository URL:** ___________________________________________________________

---

## SECTION 7: SUBMISSION PACKAGE

### 7.1 File Organization

| # | Item | Status | Notes |
|---|------|--------|-------|
| 7.1.1 | All files organized in clear folder structure | [ ] | Not a flat file list |
| 7.1.2 | Folder names are clear and professional | [ ] | E.g., `Android-App/`, `Backend-API/`, `Documentation/` |
| 7.1.3 | README.txt in root explaining folder structure | [ ] | Optional but helpful |

---

### 7.2 Digital Submission (If Required)

| # | Item | Status | Notes |
|---|------|--------|-------|
| 7.2.1 | Created submission ZIP file | [ ] | E.g., `LeafGuardAI_Submission_YourName.zip` |
| 7.2.2 | ZIP file includes: All source code | [ ] | Android + Backend |
| 7.2.3 | ZIP file includes: APK file | [ ] | LeafGuardAI_v1.0.apk |
| 7.2.4 | ZIP file includes: All documentation PDFs | [ ] | Report, slides, proposal |
| 7.2.5 | ZIP file includes: Demo video link or file | [ ] | demo-video-link.txt or .mp4 |
| 7.2.6 | ZIP file includes: Signed submission checklist PDF | [ ] | This document, signed |
| 7.2.7 | ZIP file includes: README with GitHub link | [ ] | Primary repository reference |
| 7.2.8 | ZIP file size reasonable | [ ] | <500MB preferred, document if larger |
| 7.2.9 | ZIP file tested (extract and verify) | [ ] | **CRITICAL** - All files intact |
| 7.2.10 | ZIP file named according to submission guidelines | [ ] | Follow university naming convention |

---

### 7.3 Physical Submission (If Required)

| # | Item | Status | Notes |
|---|------|--------|-------|
| 7.3.1 | Printed final report (2 copies) | [ ] | 1 for department, 1 for yourself |
| 7.3.2 | Printed presentation slides (handout format) | [ ] | 6 slides per page |
| 7.3.3 | Printed signed submission checklist | [ ] | This document |
| 7.3.4 | USB drive with all files | [ ] | Labeled with your name and course |
| 7.3.5 | OR CD/DVD with all files | [ ] | Burned and labeled |
| 7.3.6 | Report bound professionally | [ ] | Spiral binding or thermal binding |
| 7.3.7 | Cover page on report | [ ] | Transparent cover sheet |

---

### 7.4 Submission Method

| # | Item | Status | Notes |
|---|------|--------|-------|
| 7.4.1 | Uploaded to university portal | [ ] | If online submission required |
| 7.4.2 | Upload confirmation received | [ ] | Screenshot or email confirmation saved |
| 7.4.3 | OR Submitted physically to department office | [ ] | Submission receipt obtained |
| 7.4.4 | OR Emailed to course coordinator | [ ] | Email sent with all attachments |
| 7.4.5 | Submission timestamp noted | [ ] | Before deadline |
| 7.4.6 | Backup copy saved to cloud | [ ] | Google Drive, Dropbox, or similar |
| 7.4.7 | Supervisor/coordinator informed of submission | [ ] | Email or message sent |

**Submission Package Verification Signature:** ___________________________

---

## SECTION 8: VIVA PREPARATION

### 8.1 Demonstration Readiness

| # | Item | Status | Notes |
|---|------|--------|-------|
| 8.1.1 | Phone with APK installed and tested | [ ] | **CRITICAL** |
| 8.1.2 | Phone fully charged | [ ] | >80% battery |
| 8.1.3 | Backend running on laptop (if live demo) | [ ] | Or backup video ready |
| 8.1.4 | Laptop and phone on same Wi-Fi (if live demo) | [ ] | Tested connectivity |
| 8.1.5 | Backup demo video ready to play | [ ] | In case live demo fails |
| 8.1.6 | Demo rehearsed (2-3 minute version) | [ ] | Practiced multiple times |

---

### 8.2 Documentation Readiness

| # | Item | Status | Notes |
|---|------|--------|-------|
| 8.2.1 | Printed copies of report (for viva panel) | [ ] | Extra copies prepared |
| 8.2.2 | Printed copies of slides (for reference) | [ ] | Your personal notes |
| 8.2.3 | Architecture diagram printed/accessible | [ ] | For pointing and explaining |
| 8.2.4 | Test case table printed/accessible | [ ] | To show testing coverage |

---

### 8.3 Question Preparation

| # | Item | Status | Notes |
|---|------|--------|-------|
| 8.3.1 | Reviewed docs/viva-questions.md (60+ questions) | [ ] | Practiced answers |
| 8.3.2 | Prepared viva framing statement | [ ] | Memorized opening |
| 8.3.3 | Can explain architecture diagram in detail | [ ] | Data flow, components, interactions |
| 8.3.4 | Can explain each feature and syllabus topic mapping | [ ] | Why feature X covers topic Y |
| 8.3.5 | Can discuss limitations honestly | [ ] | Model accuracy, feature gaps |
| 8.3.6 | Can discuss future work | [ ] | Enhancements and scalability |
| 8.3.7 | Can explain code snippets on demand | [ ] | Retrofit setup, Room DAO, TFLite inference |
| 8.3.8 | Can explain testing methodology and results | [ ] | Test cases, edge cases, debugging |
| 8.3.9 | Prepared answers for difficult questions | [ ] | "Why low accuracy?", "Did you copy?", "Just AI?" |

**Viva Preparation Verification Signature:** ___________________________

---

## SECTION 9: CSE 2206 SYLLABUS COVERAGE VERIFICATION

### 9.1 Mobile Application Development Topics

| # | Syllabus Topic | Implemented Feature(s) | Evidence Location | Status |
|---|----------------|------------------------|-------------------|--------|
| 9.1.1 | Mobile Platform Comparison | Android vs iOS analysis in report | Report Section 2 | [ ] |
| 9.1.2 | Native Android Development | Entire app built in Kotlin (primary track) with Android SDK; complete Java twin as secondary | Source code | [ ] |
| 9.1.3 | Android Studio and Gradle | Project structure, build.gradle files | Repository | [ ] |
| 9.1.4 | Camera and Multimedia | Camera capture and gallery picker intents | MainActivity, Week 3 | [ ] |
| 9.1.5 | HTTP Networking | Retrofit POST requests to FastAPI | Week 5, ApiService.kt (Java twin: ApiService.java) | [ ] |
| 9.1.6 | XML Parsing | diseases.xml with XmlPullParser | Week 8, assets/ | [ ] |
| 9.1.7 | App-to-App Communication | Share intent for prediction results | Week 10, share feature | [ ] |
| 9.1.8 | Notifications | NotificationChannel and PendingIntent | Week 10, NotificationHelper | [ ] |
| 9.1.9 | Local Storage (SQLite/Room) | Room database for scan history | Week 7, ScanDao and AppDatabase | [ ] |
| 9.1.10 | Maps and Location | Location tagging attempted or implemented | Week 10, location attempt | [ ] |
| 9.1.11 | UI Design (XML Layouts) | Material Design layouts for all screens | res/layout/ folder | [ ] |
| 9.1.12 | Runtime Permissions | Camera, storage, location permissions | AndroidManifest, permission handling | [ ] |
| 9.1.13 | Testing and Debugging | 60+ test cases, Logcat debugging | Week 11, test-cases.xlsx | [ ] |
| 9.1.14 | Deployment (APK) | Signed release APK built and tested | LeafGuardAI_v1.0.apk | [ ] |
| 9.1.15 | App Lifecycle Management | Activity lifecycle handling | onCreate, onPause, onResume methods | [ ] |

**All 15+ topics covered:** [ ] **YES** / [ ] **NO** (If NO, document which are missing)

**Syllabus Coverage Verification Signature:** ___________________________

---

## SECTION 10: FINAL VERIFICATION

### 10.1 Completeness Check

| # | Item | Status |
|---|------|--------|
| 10.1.1 | All items in this checklist marked as complete or explained | [ ] |
| 10.1.2 | All CRITICAL items completed | [ ] |
| 10.1.3 | All files exist and are accessible | [ ] |
| 10.1.4 | All documentation reviewed for errors | [ ] |
| 10.1.5 | All links tested and working | [ ] |
| 10.1.6 | Submission meets university requirements | [ ] |
| 10.1.7 | Submission submitted before deadline | [ ] |

---

### 10.2 Quality Check

| # | Item | Status |
|---|------|--------|
| 10.2.1 | Code builds without errors | [ ] |
| 10.2.2 | App runs without crashes | [ ] |
| 10.2.3 | All features demonstrated work correctly | [ ] |
| 10.2.4 | Documentation is professional and error-free | [ ] |
| 10.2.5 | Demo video is clear and comprehensive | [ ] |
| 10.2.6 | Presentation is polished and rehearsed | [ ] |
| 10.2.7 | Repository is clean and organized | [ ] |

---

### 10.3 Final Sign-Off

**I, ___________________________________ (Student Name), confirm that:**

1. ✅ All deliverables listed in this checklist are complete and verified
2. ✅ All source code is my original work or properly attributed
3. ✅ All documentation is accurate and represents the actual project
4. ✅ The APK has been tested and works as demonstrated
5. ✅ I have prepared for the viva examination
6. ✅ I understand that any false information may result in academic penalties
7. ✅ I am ready to submit my LeafGuard AI project for evaluation

**Student Signature:** ___________________________
**Date:** ___________________________
**Time:** ___________________________

---

### 10.4 Supervisor Sign-Off (If Applicable)

**I, ___________________________________ (Supervisor Name), confirm that:**

1. ✅ I have reviewed this student's project
2. ✅ The project meets the CSE 2206 course requirements
3. ✅ The student has completed all required milestones
4. ✅ The project is ready for final submission and evaluation

**Supervisor Signature:** ___________________________
**Date:** ___________________________

---

### 10.5 Submission Receipt

**Submission Method:** [ ] Online Portal [ ] Physical [ ] Email [ ] Other: ___________

**Submission Date and Time:** ___________________________

**Confirmation Number (if applicable):** ___________________________

**Received By (if physical):** ___________________________

**Receipt/Acknowledgment:** [ ] Obtained [ ] Not Required

---

## SECTION 11: POST-SUBMISSION

### 11.1 Backup and Archive

| # | Item | Status |
|---|------|--------|
| 11.1.1 | Saved backup of all submission files to external drive | [ ] |
| 11.1.2 | Saved backup to cloud storage (Google Drive, Dropbox) | [ ] |
| 11.1.3 | Saved submission confirmation (screenshot, email, receipt) | [ ] |
| 11.1.4 | Kept USB/CD copy for yourself | [ ] |

---

### 11.2 Viva Preparation

| # | Item | Status |
|---|------|--------|
| 11.2.1 | Noted viva date and time | [ ] |
| 11.2.2 | Reviewed viva-questions.md daily | [ ] |
| 11.2.3 | Practiced demo multiple times | [ ] |
| 11.2.4 | Prepared professional attire | [ ] |

---

### 11.3 Celebration

| # | Item | Status |
|---|------|--------|
| 11.3.1 | Celebrated project completion! 🎉 | [ ] |
| 11.3.2 | Updated LinkedIn profile with project | [ ] |
| 11.3.3 | Updated resume with LeafGuard AI project | [ ] |
| 11.3.4 | Thanked mentors, peers, and supporters | [ ] |

---

## APPENDIX: QUICK SUBMISSION CHECKLIST

**Use this for rapid final verification (2 hours before deadline):**

- [ ] **Source Code**: Android + Backend on GitHub ✅
- [ ] **APK**: LeafGuardAI_v1.0.apk tested and working ✅
- [ ] **Models**: TFLite and labels.txt in assets ✅
- [ ] **XML**: diseases.xml in assets ✅
- [ ] **Images**: Sample test images in repository ✅
- [ ] **Report**: Final report PDF (15-25 pages, spell-checked) ✅
- [ ] **Slides**: Presentation PDF (12-15 slides, professional) ✅
- [ ] **Video**: Demo video link working (3-5 minutes) ✅
- [ ] **Proposal**: Project proposal PDF ✅
- [ ] **Diagram**: Architecture diagram in report and slides ✅
- [ ] **Tests**: Test case table (60+ tests) ✅
- [ ] **README**: GitHub README complete with screenshots ✅
- [ ] **GitHub**: Repository public and accessible ✅
- [ ] **Checklist**: This document signed and dated ✅
- [ ] **Submission**: Uploaded before deadline ✅

**All items checked? YOU'RE READY TO SUBMIT! 🚀**

---

## NOTES AND EXCEPTIONS

Use this space to document any items that are incomplete or require explanation:

___________________________________________________________________________

___________________________________________________________________________

___________________________________________________________________________

___________________________________________________________________________

___________________________________________________________________________

___________________________________________________________________________

---

**END OF SUBMISSION CHECKLIST**

**Total Items:** 250+
**Critical Items:** 25+
**Completion Status:** ______%

**Final Reminder:** You've built a comprehensive Android application. Submit with confidence! 💪

---

**Document Version:** 1.0
**Last Updated:** 2025-01-XX
**Prepared By:** LeafGuard AI Learning Roadmap Team
