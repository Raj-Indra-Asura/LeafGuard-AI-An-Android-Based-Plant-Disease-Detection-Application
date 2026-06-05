# Week 01: Project Understanding & Foundation

## Weekly Objective

By the end of Week 01, you will:

1. **Understand the complete LeafGuard AI project scope** including all features, architecture layers, and technical components
2. **Map every CSE 2206 syllabus topic** to specific parts of your project with concrete examples
3. **Analyze a senior Android project repository** to understand real-world code organization and best practices
4. **Create a professional project proposal** that demonstrates academic rigor and technical understanding
5. **Design a complete system architecture diagram** showing all components, data flows, and technologies
6. **Build a 12-week execution timeline** with measurable milestones and evidence collection points

**Measurable Outcomes:**
- Complete project proposal document (8-10 pages minimum)
- Hand-drawn or digital architecture diagram with all layers labeled
- Syllabus mapping document with 15+ direct connections
- Senior repo analysis report (5+ repositories examined)
- Week-by-week timeline with specific deliverables
- Git repository initialized with proper structure

---

## Why This Week Matters

### Connection to CSE 2206 Mobile Application Development

Week 01 is your foundation. Without proper understanding, you will:
- Build features that do not align with syllabus requirements
- Miss opportunities to demonstrate specific Android concepts
- Struggle to explain your architecture during viva
- Fail to collect evidence that proves you understand the course material

**This week ensures:**
- Every line of code you write serves a syllabus purpose
- You can defend every technical decision during evaluation
- Your project covers ALL required topics with proof
- You build a professional-grade Android application, not a toy project

### Academic Requirement Alignment

CSE 2206 expects you to demonstrate:
1. **Android fundamentals:** Activities, Intents, Lifecycle (LeafGuard has 6+ activities)
2. **UI design:** XML layouts, Material Design, RecyclerView (Disease library, History screens)
3. **Networking:** Retrofit, REST API, HTTP methods (Cloud AI backend communication)
4. **Data persistence:** Room database, SQLite, CRUD operations (Scan history storage)
5. **XML parsing:** Reading disease information from XML files
6. **JSON parsing:** API responses from FastAPI backend
7. **Camera integration:** Image capture, runtime permissions
8. **Asynchronous operations:** Background tasks, threading

LeafGuard AI covers every single topic. Week 01 ensures you know where and how.

---

## Syllabus Topics Covered This Week

### Direct Coverage

1. **Project Planning & Documentation**
   - Creating professional proposals
   - Technical writing for academic projects
   - Architecture documentation

2. **System Design Fundamentals**
   - Three-tier architecture (Presentation, Logic, Data layers)
   - MVVM pattern understanding
   - Data flow design

3. **Version Control Basics**
   - Git repository initialization
   - Branching strategy
   - Commit conventions

4. **Requirements Analysis**
   - Functional requirements documentation
   - Non-functional requirements
   - Scope definition

### Indirect Preparation

- Understanding Android project structure (Week 02 preparation)
- Identifying networking requirements (Week 04-05 preparation)
- Planning database schema (Week 07 preparation)
- Recognizing ML integration points (Week 06, 09 preparation)

---

## Prerequisites

### Required Knowledge

1. **Basic Programming:**
   - Understand variables, loops, conditionals
   - Object-oriented programming concepts (classes, objects, methods)
   - Basic data structures (lists, maps)

2. **Operating Systems:**
   - File system navigation (directories, paths)
   - Understanding processes and threads (high-level)

3. **Web Basics:**
   - What HTTP is (request/response model)
   - What JSON looks like
   - Basic understanding of APIs

4. **Mobile Awareness:**
   - You have used Android apps
   - You understand what activities/screens are
   - You know apps store data locally

### Required Tools

1. **Git:** Install from https://git-scm.com/
2. **Text Editor:** VS Code, Notepad++, or any editor for markdown
3. **Drawing Tool:** Draw.io (web-based, free) or pen and paper
4. **Web Browser:** For research and reading documentation
5. **Word Processor:** Microsoft Word or Google Docs for proposal

### Recommended Reading (Before Week 01)

- Android Developer documentation homepage
- FastAPI documentation introduction
- TensorFlow Lite overview page
- Room database introduction

You do NOT need to understand everything. Just get familiar with the terminology.

---

## Concepts to Learn

### 1. Three-Tier Architecture

**What it is:** Separating your application into three layers.

**The layers:**

1. **Presentation Layer (UI):** What the user sees and interacts with
   - Activities, Fragments, XML layouts
   - Buttons, text fields, images
   - LeafGuard: MainActivity, ResultActivity, HistoryActivity

2. **Business Logic Layer:** Where decisions and processing happen
   - ViewModels, Repositories
   - Data validation, API calls, database queries
   - LeafGuard: ScanViewModel, ApiService, ImageProcessor

3. **Data Layer:** Where data is stored and retrieved
   - Room database (local)
   - REST API backend (remote)
   - LeafGuard: ScanEntity, FastAPI /predict endpoint

**Why it matters:**
- Clean separation makes code easier to maintain
- You can test each layer independently
- Professors love seeing proper architecture

**Real example in LeafGuard:**
```
User taps "Scan" button (Presentation Layer)
    ↓
MainActivity calls ScanViewModel.uploadImage() (Business Logic Layer)
    ↓
ScanViewModel calls ApiService.detectDisease() (Data Layer - Network)
    ↓
Result saved to Room database (Data Layer - Local)
    ↓
UI updated with disease info (Presentation Layer)
```

### 2. MVVM (Model-View-ViewModel) Pattern

**What it is:** A design pattern for organizing Android code.

**Components:**

- **Model:** Your data structures (Disease, ScanResult classes)
- **View:** Your UI (Activities, Fragments, XML layouts)
- **ViewModel:** Bridge between View and Model (holds UI state, survives rotation)

**Flow in LeafGuard:**
```
MainActivity (View) observes ScanViewModel (ViewModel)
    ↓
ViewModel holds LiveData<ScanResult>
    ↓
When data changes, UI automatically updates
    ↓
View never directly accesses database or network
```

**Benefits:**
- Lifecycle-aware (ViewModel survives screen rotation)
- Testable (you can test ViewModels without UI)
- Clean separation of concerns

### 3. REST API Architecture

**What it is:** A way for your Android app to communicate with a backend server.

**HTTP Methods:**
- **POST /predict:** Upload leaf image, get disease prediction
- **GET /disease/{name}:** Get detailed disease information
- **POST /auth/login:** User authentication

**Request/Response Cycle:**
```
Android App                    FastAPI Backend
    |                                |
    |--- POST /predict (image) ----->|
    |                                | (ML model processes image)
    |<---- JSON response (disease) --|
    |                                |
```

**JSON Example (Response):**
```json
{
  "disease": "Tomato Early Blight",
  "confidence": 0.92,
  "symptoms": "Dark brown spots with concentric rings...",
  "treatment": "Apply fungicide containing chlorothalonil..."
}
```

### 4. Room Database (Local Storage)

**What it is:** Android's recommended way to store structured data locally.

**Components:**
- **Entity:** A table (ScanResult, User)
- **DAO (Data Access Object):** Methods to access data (insert, query, delete)
- **Database:** Container for all tables

**LeafGuard Schema:**
```
scans table:
- id (primary key)
- image_path (where image is saved)
- disease_name (predicted disease)
- confidence (0.0 to 1.0)
- timestamp (when scan happened)
```

**Why not just files?**
- Easier to query (find all scans from last week)
- Structured and type-safe
- Built-in SQLite optimization

### 5. Machine Learning Integration

**Two Modes in LeafGuard:**

1. **Cloud AI Mode:**
   - Image sent to FastAPI backend via Retrofit
   - TensorFlow model on server processes image
   - Result sent back as JSON
   - Requires internet connection

2. **On-Device AI Mode:**
   - TensorFlow Lite model stored in app assets
   - Image processed locally on phone
   - No internet required
   - Faster response time

**Model Flow:**
```
Leaf Image (224x224 pixels)
    ↓
Preprocessing (normalize, resize)
    ↓
CNN Model (MobileNet or custom)
    ↓
Output: [0.02, 0.92, 0.01, ...] (probability per disease class)
    ↓
Take argmax → Disease with highest probability
```

### 6. XML Parsing

**What it is:** Reading structured data from XML files.

**LeafGuard Use Case:** Disease library stored in assets/disease_library.xml

**Example XML:**
```xml
<diseases>
    <disease>
        <name>Tomato Early Blight</name>
        <symptoms>Dark spots with concentric rings</symptoms>
        <treatment>Apply fungicide</treatment>
    </disease>
</diseases>
```

**Parsing in Android:**
```java
XmlPullParser parser = ...
while (parser.next() != XmlPullParser.END_TAG) {
    if (parser.getName().equals("name")) {
        String diseaseName = parser.nextText();
    }
}
```

### 7. Project Documentation

**Types of documents you will create:**

1. **Proposal (Week 01):** What you will build and why
2. **Architecture Document (Week 01):** How components connect
3. **API Documentation (Week 04):** Backend endpoints and formats
4. **User Manual (Week 12):** How to use the app
5. **Final Report (Week 12):** Complete project summary

**Academic Writing Tips:**
- Use formal language (avoid "I think", use "This project implements")
- Cite sources (Android documentation, research papers)
- Include diagrams (architecture, sequence, ER diagrams)
- Explain WHY you made decisions, not just WHAT you built

---

## Reading Plan

### Day 1: Project Scope & Architecture

**Read these files completely:**
1. `/PROJECT_ARCHITECTURE.md` (entire file - 750 lines)
2. `/docs/proposal-template.md` (sections 1-6)

**Focus on:**
- Understanding the hybrid cloud-offline architecture
- Identifying all activities and their purposes
- Data flow from camera to result display
- Technology stack justification

**Questions to answer:**
- What are the 6 main activities?
- What is the difference between cloud and offline mode?
- Where is data stored locally?
- What backend framework is used?

### Day 2: CSE 2206 Syllabus Mapping

**Read these resources:**
1. Your official CSE 2206 syllabus PDF
2. Android Developer Architecture Guide: https://developer.android.com/topic/architecture
3. `/PROJECT_ARCHITECTURE.md` sections on Android components

**Create a mapping document:**
```
Syllabus Topic → LeafGuard Component
--------------------------------
Activities & Intents → 6 activities with camera/gallery intents
Fragments → Disease library fragments
RecyclerView → History list display
Retrofit → Cloud API communication
Room → Scan history storage
XML Parsing → Disease info parsing
JSON Parsing → API response parsing
Permissions → Camera, storage, location
```

### Day 3: Senior Repository Analysis

**Find and analyze 3-5 Android projects on GitHub:**

Search terms:
- "android plant disease detection"
- "android machine learning image classification"
- "android retrofit room mvvm"

**For each repository, document:**
1. Repository URL and star count
2. Project structure (how folders are organized)
3. Architecture pattern used (MVVM, MVP, MVC)
4. Libraries used (Retrofit version, Room version, etc.)
5. What they did well (clean code, good documentation)
6. What they did poorly (no comments, messy structure)
7. What you will adopt in LeafGuard
8. What you will avoid

**Create a file:** `/docs/senior-repo-analysis.md`

### Day 4: Architecture Diagram Design

**Read:**
1. `/docs/architecture-diagram-guide.md` (complete file)
2. UML diagram tutorials: https://www.lucidchart.com/pages/uml-diagram
3. Android architecture examples: Search "Android MVVM architecture diagram"

**Practice drawing:**
1. Simple three-tier diagram (hand-drawn is fine)
2. Data flow diagram for scan process
3. Component diagram showing relationships

**Tool setup:**
- Create account on Draw.io or Lucidchart
- Explore templates for architecture diagrams
- Practice creating boxes, arrows, and labels

### Day 5: Project Proposal Writing

**Read:**
1. `/docs/proposal-template.md` (complete)
2. Sample academic proposals online (search "android project proposal pdf")
3. Research paper abstracts related to plant disease detection

**Start writing:**
- Abstract (150-200 words)
- Introduction (2-3 pages)
- Problem Statement (1 page)
- Objectives (1 page)

**Writing checklist:**
- [ ] Formal academic tone
- [ ] No first-person ("I will" → "This project will")
- [ ] Citations for statistics and claims
- [ ] Clear problem and solution
- [ ] Measurable objectives

### Day 6: Timeline & Evidence Planning

**Create two documents:**

1. **12-Week Timeline:**
```
Week 1: Project understanding → Deliverable: Proposal
Week 2: Android UI → Deliverable: Layout mockups
Week 3: Camera → Deliverable: Image capture demo
...
```

2. **Evidence Collection Plan:**
```
What evidence to save each week:
- Screenshots of working features
- Git commit history
- Test results
- Performance metrics
- Bug fix documentation
```

### Day 7: Review & Consolidation

**Review everything you created this week:**
1. Re-read your proposal
2. Verify architecture diagram includes all components
3. Check syllabus mapping is complete
4. Confirm senior repo analysis has actionable insights
5. Validate timeline is realistic

**Prepare for Week 02:**
- Install Android Studio
- Review Java/Kotlin basics
- Read about Android project structure

---

## Hands-On Exercises

### Exercise 1: Repository Structure Exploration

**Goal:** Understand how Android projects are organized.

**Steps:**
1. Go to https://github.com/topics/android-mvvm
2. Pick any repository with 500+ stars
3. Clone it or browse online
4. Create a document answering:
   - Where are activities located? (path)
   - Where are XML layouts? (path)
   - Is there a `data/` or `repository/` folder?
   - What is in the `res/` folder?
   - Where is `AndroidManifest.xml`?
   - What libraries are in `build.gradle`?

**Expected Output:**
```
Repository: [Name and URL]
Structure Analysis:
- Activities: app/src/main/java/com/example/app/ui/
- Layouts: app/src/main/res/layout/
- ViewModels: app/src/main/java/com/example/app/viewmodel/
- Database: app/src/main/java/com/example/app/database/
Libraries:
- Retrofit: 2.9.0
- Room: 2.5.0
- Glide: 4.12.0
```

### Exercise 2: Data Flow Mapping

**Goal:** Understand how data moves through LeafGuard AI.

**Task:** Draw a flowchart on paper or digitally showing:

**Scenario:** User captures a leaf photo and gets a disease prediction.

**Required components in your flowchart:**
1. User
2. Camera
3. MainActivity
4. ScanViewModel
5. ScanRepository
6. RetrofitClient
7. FastAPI Backend
8. ML Model
9. Room Database
10. ResultActivity

**Arrows should show:**
- Image captured (Camera → MainActivity)
- Image sent to ViewModel (MainActivity → ScanViewModel)
- API call made (ScanViewModel → RetrofitClient → Backend)
- Prediction returned (Backend → RetrofitClient → ScanViewModel)
- Result saved locally (ScanViewModel → Room Database)
- UI updated (ScanViewModel → ResultActivity)

**Expected Output:** A clear diagram with labeled arrows showing data/control flow.

### Exercise 3: Syllabus Topic Mapping

**Goal:** Connect every CSE 2206 topic to LeafGuard components.

**Task:** Create a table with three columns:

| Syllabus Topic | LeafGuard Component | Proof Location |
|----------------|---------------------|----------------|
| Activities | MainActivity, ResultActivity, etc. | app/src/main/java/.../activities/ |
| Intents | Camera intent, Gallery intent | MainActivity.java line 45-60 |
| RecyclerView | History list | HistoryActivity.java |
| Retrofit | API service | network/ApiService.java |
| Room | Scan history DB | database/AppDatabase.java |
| XML Parsing | Disease info | utils/XmlParser.java |
| JSON Parsing | API responses | models/UploadResponse.java |
| Permissions | Camera, Storage | AndroidManifest.xml + MainActivity |
| Fragments | Disease library | fragments/LibraryFragment.java |
| Notifications | Scan reminders | utils/NotificationHelper.java |

**Expected Output:** A complete mapping document showing WHERE each syllabus topic appears in your project.

### Exercise 4: Senior Repo Code Reading

**Goal:** Learn from real-world Android code.

**Task:**
1. Find this repository: https://github.com/topics/android-room-database
2. Pick any project with a `database/` folder
3. Read the Entity and DAO files
4. Answer:
   - What tables does the database have?
   - What annotations are used? (@Entity, @PrimaryKey, @ColumnInfo)
   - What CRUD operations are defined in DAO?
   - How is the database created (Database class)?

**Expected Output:**
```
Repository: [Name]
Database Analysis:
Tables:
1. User (@Entity)
   - Fields: id (PK), name, email
2. Post (@Entity)
   - Fields: id (PK), title, content, userId (FK)

DAO Methods:
- @Insert: insertUser(User user)
- @Query("SELECT * FROM user"): getAllUsers()
- @Delete: deleteUser(User user)

Database Class:
- Extends RoomDatabase
- @Database annotation with entities list
- Singleton pattern using synchronized block
```

### Exercise 5: Architecture Diagram Practice

**Goal:** Learn to draw professional architecture diagrams.

**Task:**
1. Open Draw.io (https://app.diagrams.net/)
2. Create new blank diagram
3. Draw LeafGuard AI high-level architecture:
   - One box for Android App (top)
   - One box for FastAPI Backend (bottom)
   - One box for Room Database (left side)
   - One box for TensorFlow Model (right side)
4. Add arrows showing:
   - Android → Backend (HTTP POST)
   - Backend → TensorFlow (inference request)
   - TensorFlow → Backend (prediction)
   - Backend → Android (JSON response)
   - Android → Room (save result)
5. Add labels on arrows: "Image Upload", "Prediction Request", "JSON Response", "Save to DB"

**Expected Output:** A clear, labeled diagram saved as PNG or PDF.

### Exercise 6: Proposal Writing Practice

**Goal:** Practice formal academic writing.

**Task:** Write a 300-word abstract for LeafGuard AI including:
1. Problem statement (what problem does it solve?)
2. Solution approach (Android app with ML)
3. Technologies used (Android, Retrofit, Room, FastAPI, TensorFlow)
4. Expected outcomes (accurate disease detection, farmer empowerment)
5. Academic relevance (covers CSE 2206 syllabus)

**Writing rules:**
- No first person (I, we, our)
- Use passive voice where appropriate
- Cite statistics (e.g., "Plant diseases cause 20-40% crop loss annually")
- Be formal and precise

**Expected Output:** A well-written abstract ready to paste into your proposal.

### Exercise 7: Git Repository Initialization

**Goal:** Set up version control for your project.

**Task:**
1. Create a folder: `LeafGuard-AI/`
2. Initialize Git: `git init`
3. Create initial structure:
   ```
   LeafGuard-AI/
   ├── docs/
   │   ├── proposal.md
   │   ├── architecture.md
   │   └── senior-repo-analysis.md
   ├── diagrams/
   │   └── architecture-v1.png
   ├── evidence/
   │   └── week-01/
   └── README.md
   ```
4. Create `.gitignore`:
   ```
   *.log
   *.tmp
   .DS_Store
   ```
5. Make first commit:
   ```
   git add .
   git commit -m "Week 01: Initialize project with proposal and architecture docs"
   ```

**Expected Output:** A Git repository with organized structure and meaningful first commit.

---

## Build Task

### Week 01 Build Task: Create Complete Project Foundation Package

**Objective:** By end of Week 01, you will have a complete project foundation package that proves you understand the entire project scope.

**Deliverables:**

### 1. Project Proposal Document

**File:** `docs/proposal.md` or `docs/proposal.pdf`

**Requirements:**
- 8-10 pages minimum
- All sections from `/docs/proposal-template.md` filled out:
  - Title page with your details
  - Abstract (150-200 words)
  - Introduction (problem background, motivation)
  - Problem statement (clear and specific)
  - Objectives (primary and secondary)
  - Scope (in-scope, out-of-scope)
  - Literature review (3-5 existing apps/papers analyzed)
  - Methodology (development approach, tech stack)
  - Timeline (12-week Gantt chart)
  - Expected outcomes
  - Risk analysis
  - Budget estimation
  - References (minimum 10 sources)

**Quality Checklist:**
- [ ] No spelling/grammar errors
- [ ] Formal academic tone maintained
- [ ] All statistics cited with sources
- [ ] Diagrams included where appropriate
- [ ] Objectives are SMART (Specific, Measurable, Achievable, Relevant, Time-bound)

### 2. System Architecture Diagram

**File:** `diagrams/system-architecture.png` (or PDF)

**Requirements:**
- Shows all three layers: Presentation, Business Logic, Data
- Includes all 6+ activities
- Shows ViewModels and Repositories
- Displays Room Database with tables
- Shows Retrofit connection to Backend
- Displays FastAPI backend with endpoints
- Shows TensorFlow model (cloud and local)
- Has clear data flow arrows
- Includes legend explaining symbols
- Professional color scheme
- High resolution (300 DPI minimum)

**Must show:**
- Android Application layer (activities, fragments)
- ViewModel layer (AuthViewModel, ScanViewModel, HistoryViewModel)
- Repository layer (data coordination)
- Local data sources (Room, XML parser, file storage)
- Remote data sources (Retrofit, API endpoints)
- Backend layer (FastAPI, ML model, database)
- Data flow arrows with labels

### 3. Syllabus Mapping Document

**File:** `docs/syllabus-mapping.md`

**Requirements:**
- Complete table mapping all CSE 2206 topics to LeafGuard components
- Minimum 15 topics covered
- For each topic:
  - Syllabus topic name
  - LeafGuard component that implements it
  - Specific file/class name
  - Brief explanation of how it's implemented
  - Where to find evidence (line numbers if possible)

**Example entries:**
```markdown
### Activities and Lifecycle

**Syllabus Topic:** Activity lifecycle management, onCreate(), onPause(), onResume()

**LeafGuard Implementation:**
- 6 activities: MainActivity, ScanActivity, ResultActivity, HistoryActivity, DiseaseLibraryActivity, SettingsActivity
- Each activity properly implements lifecycle methods
- State saved/restored on configuration changes

**Evidence Location:**
- `app/src/main/java/com/example/leafguard/activities/MainActivity.java`
- Lines 34-45: onCreate() implementation
- Lines 78-82: onSaveInstanceState() for rotation handling

**How it demonstrates understanding:**
- Proper initialization of views in onCreate()
- Releasing resources in onPause()
- Restoring state in onRestoreInstanceState()
```

### 4. Senior Repository Analysis Report

**File:** `docs/senior-repo-analysis.md`

**Requirements:**
- Analyze 5 Android repositories related to:
  - Plant disease detection
  - Machine learning on Android
  - MVVM architecture
  - Retrofit + Room integration
- For each repository:
  - Name, URL, star count, last update date
  - Project description
  - Architecture pattern identified
  - Folder structure analysis
  - Libraries and versions used
  - Code quality observations
  - What was done well (3+ points)
  - What was done poorly (3+ points)
  - Key learnings for LeafGuard (2+ points)

**Deliverable format:**
```markdown
## Repository 1: PlantDiseaseDetector

**URL:** https://github.com/username/PlantDiseaseDetector
**Stars:** 234 | **Forks:** 45 | **Last Update:** 2023-08-15

### Overview
Android app using CNN model for plant disease detection...

### Architecture
- Pattern: MVP (Model-View-Presenter)
- Structure: Clean separation of concerns, but no ViewModels

### Libraries
- Retrofit: 2.9.0 (for API calls)
- Glide: 4.11.0 (image loading)
- No Room database (uses SharedPreferences)

### Strengths
1. Clean package structure with clear naming
2. Comprehensive error handling with try-catch blocks
3. Good documentation in README

### Weaknesses
1. No lifecycle-aware components (activities do too much)
2. Hard-coded base URL in ApiService
3. No unit tests

### Key Learnings for LeafGuard
1. Adopt their package structure (features-based organization)
2. Avoid their mistake of hard-coded URLs (use BuildConfig)
3. Implement ViewModel to avoid their lifecycle issues
```

### 5. 12-Week Execution Timeline

**File:** `docs/timeline.md`

**Requirements:**
- Week-by-week breakdown from Week 01 to Week 12
- For each week:
  - Week title and focus area
  - Learning objectives (what you will learn)
  - Deliverables (what you will build)
  - Evidence to collect (screenshots, commits, test results)
  - Estimated hours
- Milestones at Weeks 4, 8, 12
- Buffer time for debugging and revisions

**Example week entry:**
```markdown
## Week 03: Camera & Gallery Integration

**Focus:** Implementing image capture and selection features

**Learning Objectives:**
- Understand Android camera architecture (Camera2 API)
- Implement runtime permissions (Marshmallow+)
- Handle storage access framework
- Process and display captured images

**Deliverables:**
1. Working camera capture functionality
2. Gallery image picker implementation
3. Image preview before upload
4. Permission request handling with rationale

**Evidence to Collect:**
- Screenshots of camera UI
- Permission dialog screenshots
- Video demo of capture → preview flow
- Git commits showing implementation
- Logcat showing permission granted/denied

**Estimated Hours:** 12-15 hours

**Risks:**
- Camera API complexity (mitigation: use CameraX library)
- Permission handling edge cases (mitigation: test on API 23-33)
```

### 6. Evidence Collection Plan

**File:** `evidence/week-01/README.md`

**Requirements:**
- Document what evidence to save each week
- Folder structure for organizing evidence
- Guidelines for screenshot quality
- Git commit message conventions
- Video recording tips for demos

**Content:**
```markdown
# Evidence Collection Plan

## Purpose
Maintain organized proof of project progress for academic evaluation.

## Evidence Types

### 1. Screenshots
- Capture at 1080p minimum resolution
- Include timestamp in filename: `scan-activity-2024-01-15.png`
- Annotate key features with arrows/labels
- Store in `evidence/week-XX/screenshots/`

### 2. Git Commits
- Commit message format: `Week XX: [Feature] - Brief description`
- Example: `Week 03: Camera Integration - Implement image capture with permissions`
- Commit after each logical unit of work
- Never commit broken code to main branch

### 3. Video Demonstrations
- Record feature walkthroughs (30-60 seconds each)
- Show error handling (what happens when things go wrong)
- Demonstrate app on real device, not just emulator
- Store in `evidence/week-XX/videos/`

### 4. Documentation
- Save draft documentation in `evidence/week-XX/docs/`
- Keep old versions with date suffix: `proposal-v1-2024-01-10.pdf`
- Export diagrams in multiple formats (PNG, PDF, source file)

### 5. Test Results
- Save JUnit test output
- Screenshot of test coverage reports
- Manual test case results in spreadsheet

## Weekly Evidence Checklist

### Week 01
- [ ] Complete proposal PDF
- [ ] Architecture diagram (PNG + source file)
- [ ] Syllabus mapping document
- [ ] Senior repo analysis
- [ ] Timeline document
- [ ] Git initialization screenshot

### Week 02
- [ ] Android Studio project creation screenshot
- [ ] Package structure screenshot
- [ ] Empty activities created
- [ ] Layout XML files created
- [ ] First APK build success screenshot
...
```

### 7. Git Repository Structure

**Task:** Initialize Git repo with proper organization.

**Required structure:**
```
LeafGuard-AI/
├── .git/
├── .gitignore
├── README.md (project overview)
├── docs/
│   ├── proposal.md
│   ├── architecture.md
│   ├── syllabus-mapping.md
│   ├── senior-repo-analysis.md
│   ├── timeline.md
│   └── api-documentation.md (template for Week 04)
├── diagrams/
│   ├── system-architecture.png
│   ├── data-flow.png
│   ├── er-diagram.png (template for Week 07)
│   └── sequence-diagrams/ (folder)
├── evidence/
│   ├── week-01/
│   │   ├── README.md
│   │   └── screenshots/
│   ├── week-02/ (create next week)
│   └── ...
└── android-app/ (create Week 02)
```

**First commit:**
```bash
git init
git add .
git commit -m "Week 01: Initialize LeafGuard AI project with proposal, architecture docs, and evidence plan"
```

---

## Debugging Practice

### Debugging Case 1: Incomplete Syllabus Mapping

**Symptom:** Your syllabus mapping only has 8 topics, but CSE 2206 covers 15+ topics.

**Root Cause:** You did not read the complete syllabus carefully or review all LeafGuard features.

**Fix:**
1. Open your official CSE 2206 syllabus PDF
2. List ALL topics line by line
3. For each topic, ask: "Where in LeafGuard does this appear?"
4. If you cannot find a match, re-read PROJECT_ARCHITECTURE.md
5. If still no match, LeafGuard might not cover it (check with instructor)

**Prevention:**
- Create checklist of all syllabus topics before starting
- Cross-reference with LeafGuard features table
- Ask seniors or instructors if unsure

### Debugging Case 2: Architecture Diagram Too Complex

**Symptom:** Your architecture diagram has 50+ boxes and is unreadable.

**Root Cause:** Trying to show too much detail in one diagram.

**Fix:**
1. Create separate diagrams:
   - High-level architecture (show only layers)
   - Component diagram (show classes)
   - Data flow diagram (show data movement)
   - Database schema (show tables)
2. Use hierarchy: One overview diagram, then detailed sub-diagrams
3. Follow "one diagram, one message" principle

**Prevention:**
- Start with simple boxes and arrows
- Gradually add detail
- Get feedback before finalizing

### Debugging Case 3: Proposal Too Generic

**Symptom:** Your proposal reads like it could be for any Android app, not specifically LeafGuard.

**Root Cause:** Copying template text without customizing.

**Fix:**
1. Replace every "This app" with "LeafGuard AI"
2. Add specific examples:
   - Instead of: "The app uses machine learning"
   - Write: "LeafGuard AI employs a MobileNetV2-based CNN trained on 54,000 leaf images from the PlantVillage dataset"
3. Mention actual class names, file names, endpoints
4. Include specific metrics: "6 activities, 15 disease classes, 85% accuracy target"

**Prevention:**
- Never copy-paste large blocks without customization
- Add specifics in first draft, not later

### Debugging Case 4: Senior Repo Analysis Too Shallow

**Symptom:** Your analysis says "Good project" without explaining why.

**Root Cause:** Not reading the code, only looking at README.

**Fix:**
1. Actually open Java/Kotlin files in repositories
2. Read at least 3 files per repository:
   - One activity file
   - One ViewModel or Presenter file
   - One database or API file
3. Look for specific patterns:
   - How do they handle errors?
   - How do they structure packages?
   - What libraries do they use?
4. Write specific observations: "MainActivity.java has 800 lines, violating single responsibility principle"

**Prevention:**
- Set aside 30 minutes per repository for deep analysis
- Take notes while reading code
- Focus on architecture and patterns, not just features

### Debugging Case 5: Unrealistic Timeline

**Symptom:** Your timeline allocates 2 hours for "Build entire backend" or 40 hours for "Add button to UI".

**Root Cause:** Not understanding task complexity or effort estimation.

**Fix:**
1. Research how long similar tasks typically take:
   - Google: "How long to implement Retrofit in Android"
   - Ask seniors: "How long did Room database take you?"
2. Break large tasks into subtasks:
   - "Build backend" → "Setup FastAPI" + "Create /predict endpoint" + "Load ML model" + "Test API" + "Deploy"
3. Add 20-30% buffer for debugging and unexpected issues
4. Compare with course duration: If you have 12 weeks, your timeline must fit

**Prevention:**
- Use proven estimation: Simple task (2-4 hrs), Medium (6-10 hrs), Complex (12-20 hrs)
- Review timeline with instructor or experienced developer

---

## Validation Checklist

Use this checklist to verify Week 01 completion. All items must be checked before proceeding to Week 02.

### Documentation Completeness

- [ ] **Proposal document exists** and is 8-10 pages minimum
- [ ] **Abstract is 150-200 words** and summarizes problem, solution, technologies, outcomes
- [ ] **Introduction section explains** agricultural problem, smartphone proliferation, ML advances
- [ ] **Problem statement is clear** and specific to plant disease detection
- [ ] **Objectives are SMART** (Specific, Measurable, Achievable, Relevant, Time-bound)
- [ ] **Scope clearly defines** what is included and excluded
- [ ] **Literature review analyzes** 3-5 existing apps or research papers
- [ ] **Methodology explains** Agile approach, tech stack, development phases
- [ ] **Timeline has 12 weeks** with deliverables for each week
- [ ] **References section has** minimum 10 credible sources (documentation, papers, articles)

### Architecture Understanding

- [ ] **Architecture diagram exists** and is professionally drawn (digital or neat hand-drawn)
- [ ] **Diagram shows three layers:** Presentation, Business Logic, Data
- [ ] **All 6+ activities are labeled** (MainActivity, ScanActivity, ResultActivity, etc.)
- [ ] **ViewModels are shown** with connections to activities
- [ ] **Repositories are shown** as intermediaries between ViewModels and data sources
- [ ] **Room database is shown** with at least 2 tables (scans, users)
- [ ] **Retrofit/API connection is shown** linking to backend
- [ ] **FastAPI backend is shown** with at least /predict endpoint
- [ ] **ML model is shown** both in backend and as TFLite in app
- [ ] **Data flow arrows are labeled** (e.g., "Image Upload", "JSON Response")
- [ ] **Legend exists** explaining symbols and colors

### Syllabus Mapping

- [ ] **Syllabus mapping document exists** with table format
- [ ] **Minimum 15 topics are mapped** from CSE 2206 syllabus
- [ ] **Activities & Intents are mapped** to specific LeafGuard activities
- [ ] **Fragments are mapped** (even if just disease library)
- [ ] **RecyclerView is mapped** to history list or disease library list
- [ ] **Retrofit/Networking is mapped** to API service
- [ ] **Room database is mapped** to scan history storage
- [ ] **XML parsing is mapped** to disease information parsing
- [ ] **JSON parsing is mapped** to API response handling
- [ ] **Camera integration is mapped** to image capture feature
- [ ] **Runtime permissions are mapped** to camera/storage permissions
- [ ] **Each mapping includes file path** where evidence can be found
- [ ] **Each mapping explains HOW** the topic is demonstrated, not just WHERE

### Senior Repository Analysis

- [ ] **Analysis document exists** with 5 repositories reviewed
- [ ] **Each repository includes URL** and basic stats (stars, last update)
- [ ] **Architecture pattern is identified** for each (MVVM, MVP, MVC, or other)
- [ ] **Folder structure is documented** for each
- [ ] **Libraries and versions are listed** for each
- [ ] **Strengths are documented** (minimum 3 per repository)
- [ ] **Weaknesses are documented** (minimum 3 per repository)
- [ ] **Key learnings are documented** (minimum 2 per repository)
- [ ] **At least one plant disease detection app** is analyzed
- [ ] **At least one MVVM example** is analyzed
- [ ] **At least one Retrofit+Room example** is analyzed
- [ ] **Analysis is specific** (mentions actual code, not generic observations)

### Timeline & Planning

- [ ] **12-week timeline exists** with week-by-week breakdown
- [ ] **Each week has clear focus area** (e.g., "Week 03: Camera Integration")
- [ ] **Each week has learning objectives** (what you will learn)
- [ ] **Each week has deliverables** (what you will build)
- [ ] **Each week has evidence plan** (what to save as proof)
- [ ] **Milestones are defined** at Weeks 4, 8, 12
- [ ] **Week 01 is Project Understanding** (current week)
- [ ] **Week 02 is Android Basics/UI** (next week)
- [ ] **Week 12 is Final Submission** (last week)
- [ ] **Timeline is realistic** (total hours per week: 8-15 hours)
- [ ] **Buffer time is included** for debugging and revisions

### Evidence & Version Control

- [ ] **Git repository is initialized** (`git init` executed)
- [ ] **Folder structure is created** with docs/, diagrams/, evidence/ folders
- [ ] **.gitignore file exists** with appropriate entries
- [ ] **README.md exists** with project overview
- [ ] **First commit is made** with meaningful message
- [ ] **Evidence plan document exists** (evidence/week-01/README.md)
- [ ] **Evidence plan specifies** what to save each week
- [ ] **Evidence plan includes** screenshot, video, commit, documentation guidelines
- [ ] **Week-01 evidence folder has** proposal, diagrams, and analysis documents

### Understanding & Comprehension

- [ ] **I can explain three-tier architecture** in my own words
- [ ] **I can explain MVVM pattern** and how it differs from MVC
- [ ] **I can describe data flow** from camera to result display
- [ ] **I can name all 6+ activities** and their purposes
- [ ] **I can explain Retrofit's role** in the architecture
- [ ] **I can explain Room's role** in the architecture
- [ ] **I can explain the difference** between cloud AI and on-device AI modes
- [ ] **I can list all technologies used** (Android, Retrofit, Room, FastAPI, TensorFlow Lite)
- [ ] **I can explain why MVVM is used** (lifecycle awareness, testability, separation of concerns)
- [ ] **I can explain the purpose** of each document I created this week

### Quality Checks

- [ ] **All documents have proper formatting** (headings, lists, paragraphs)
- [ ] **No spelling or grammar errors** in proposal
- [ ] **All diagrams are high resolution** (300 DPI or vectorized)
- [ ] **All file names follow conventions** (lowercase, hyphenated, descriptive)
- [ ] **All code/command examples** in documents are syntax-highlighted or formatted
- [ ] **All tables are properly aligned** and readable
- [ ] **All external links are working** (tested)
- [ ] **All citations have sources** (no unsourced statistics)
- [ ] **Document dates are current** (not template dates)
- [ ] **Your name and details** are in proposal title page

---

## Understanding Questions

Answer these questions to verify your Week 01 understanding. Write answers in `evidence/week-01/understanding-answers.md`.

### Question 1: Architecture Layers
**Question:** Explain the three-tier architecture of LeafGuard AI. For each tier, name two specific components and their responsibilities.

**Expected Answer Components:**
- Presentation Layer: Activities (MainActivity, ResultActivity), Fragments, XML layouts
  - Responsibility: Display UI, handle user interactions
- Business Logic Layer: ViewModels (ScanViewModel), Repositories (ScanRepository)
  - Responsibility: Process data, coordinate between UI and data sources
- Data Layer: Room Database (local), Retrofit API Service (remote)
  - Responsibility: Store and retrieve data

### Question 2: Data Flow
**Question:** Trace the complete data flow when a user captures a leaf photo and receives a disease prediction in cloud AI mode. List all components the data passes through.

**Expected Answer Flow:**
1. User taps "Scan" button in MainActivity
2. Camera intent launched, user captures image
3. Image returned to MainActivity via onActivityResult()
4. MainActivity passes image to ScanViewModel
5. ViewModel calls ScanRepository.uploadImage()
6. Repository uses RetrofitClient to make HTTP POST to /predict
7. FastAPI backend receives image
8. Backend preprocesses image and runs TensorFlow model
9. Model returns prediction array
10. Backend formats JSON response
11. Retrofit receives JSON, parses to UploadResponse object
12. Repository returns result to ViewModel
13. ViewModel updates LiveData
14. MainActivity observes LiveData change, updates UI
15. ViewModel saves result to Room database via ScanDao

### Question 3: MVVM Pattern
**Question:** Why does LeafGuard use MVVM pattern instead of having Activities directly access the database and network? Give three specific benefits.

**Expected Answers:**
1. **Lifecycle awareness:** ViewModel survives configuration changes (screen rotation), Activity does not. This prevents data loss and duplicate network calls.
2. **Testability:** You can unit test ViewModels without needing Android framework. You cannot easily test Activities.
3. **Separation of concerns:** Activities only handle UI, ViewModels handle logic, Repositories handle data. This makes code easier to maintain and debug.

### Question 4: Syllabus Coverage
**Question:** List 10 CSE 2206 syllabus topics and explain which LeafGuard component demonstrates each topic.

**Expected Answers (10 examples):**
1. **Activities:** LeafGuard has 6+ activities (MainActivity, ScanActivity, ResultActivity, etc.)
2. **Intents:** Camera intent for image capture, Gallery intent for image selection
3. **Fragments:** DiseaseLibraryFragment for displaying disease information
4. **RecyclerView:** HistoryActivity displays scan history list using RecyclerView
5. **Retrofit:** ApiService interface and RetrofitClient for REST API communication
6. **Room:** AppDatabase, ScanEntity, ScanDao for local data persistence
7. **XML Parsing:** XmlParser reads disease_library.xml for treatment information
8. **JSON Parsing:** Gson parses API responses into UploadResponse objects
9. **Permissions:** Runtime permissions for Camera and External Storage
10. **AsyncTask/Coroutines:** Background API calls and database operations use Kotlin Coroutines

### Question 5: Repository Pattern
**Question:** What is the purpose of the Repository pattern in LeafGuard? Why not let ViewModel directly access Room database and Retrofit?

**Expected Answer:**
The Repository acts as a **single source of truth** and **data abstraction layer**. Benefits:
1. **Centralized data logic:** Repository decides whether to fetch from network or database
2. **Caching strategy:** Repository can cache API results in Room database automatically
3. **Switching data sources:** If you change from Retrofit to Volley, only Repository changes, not ViewModel
4. **Offline support:** Repository can return cached data when network is unavailable
5. **Testing:** You can mock Repository to test ViewModel without real database or network

### Question 6: Cloud vs Offline AI
**Question:** LeafGuard has two AI modes: cloud and offline. Compare them in terms of accuracy, speed, internet dependency, and model updates.

**Expected Comparison:**

| Aspect | Cloud AI Mode | Offline AI Mode |
|--------|---------------|-----------------|
| Accuracy | Higher (can use larger model) | Lower (model size constrained) |
| Speed | Slower (network latency ~2-5 sec) | Faster (on-device ~500ms) |
| Internet | Required | Not required |
| Model Updates | Easy (update server model) | Hard (must update app to update model) |
| Privacy | Image sent to server | Image stays on device |
| Cost | Server hosting cost | No ongoing cost |

### Question 7: Database Schema
**Question:** Design the Room database schema for LeafGuard. What tables are needed? What are the primary and foreign keys?

**Expected Schema:**

**Table 1: scans**
- id (INTEGER PRIMARY KEY AUTOINCREMENT)
- image_path (TEXT NOT NULL)
- disease_name (TEXT NOT NULL)
- confidence (REAL NOT NULL) // 0.0 to 1.0
- mode (TEXT NOT NULL) // "cloud" or "offline"
- timestamp (INTEGER NOT NULL) // Unix timestamp
- user_id (INTEGER FOREIGN KEY → users.id)

**Table 2: users**
- id (INTEGER PRIMARY KEY AUTOINCREMENT)
- name (TEXT NOT NULL)
- email (TEXT UNIQUE NOT NULL)
- password_hash (TEXT NOT NULL)
- created_at (INTEGER NOT NULL)

**Optional Table 3: diseases**
- id (INTEGER PRIMARY KEY AUTOINCREMENT)
- name (TEXT UNIQUE NOT NULL)
- symptoms (TEXT)
- treatment (TEXT)
- prevention (TEXT)

### Question 8: API Design
**Question:** Design the FastAPI endpoint for disease detection. Specify HTTP method, path, request format, and response format.

**Expected Design:**

**Endpoint:** POST /api/detect

**Request:**
```http
POST /api/detect HTTP/1.1
Content-Type: multipart/form-data

----boundary
Content-Disposition: form-data; name="file"; filename="leaf.jpg"
Content-Type: image/jpeg

[binary image data]
----boundary--
```

**Response (Success):**
```json
{
  "success": true,
  "disease": "Tomato Early Blight",
  "confidence": 0.923,
  "symptoms": "Dark brown spots with concentric rings on lower leaves...",
  "treatment": "Apply fungicide containing chlorothalonil...",
  "prevention": "Rotate crops, avoid overhead watering...",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Response (Error):**
```json
{
  "success": false,
  "error": "Invalid image format",
  "detail": "Uploaded file must be JPEG or PNG"
}
```

### Question 9: Evidence Collection
**Question:** What evidence should you save during Week 03 (Camera Integration) to prove you completed the feature? List 5 types of evidence.

**Expected Evidence:**
1. **Screenshots:**
   - Camera UI with preview
   - Permission request dialog
   - Image selected from gallery
   - Image preview before upload
2. **Git commits:**
   - Commit showing CameraX dependency added
   - Commit showing camera activity implemented
   - Commit showing permission handling
3. **Video demo:**
   - 30-second video showing: tap button → camera opens → capture photo → preview shown
4. **Code documentation:**
   - Inline comments in MainActivity explaining camera logic
   - README updated with camera feature description
5. **Test results:**
   - Manual test cases: camera on API 24, API 30, API 33
   - Screenshot of successful image capture on real device

### Question 10: Risk Mitigation
**Question:** You planned to complete Backend and ML model in Week 04 (one week). During Week 04, you realize ML model training takes longer than expected. What should you do? Provide a concrete action plan.

**Expected Answer:**

**Immediate actions:**
1. **Prioritize core functionality:**
   - Focus on getting /predict endpoint working with a pre-trained model (MobileNetV2 from TensorFlow Hub)
   - Defer custom model training to Week 05
2. **Use mock data for Android development:**
   - Create mock API responses so Android team can continue working
   - Implement proper API integration once backend is ready
3. **Parallel work:**
   - Continue ML training in background (on cloud GPU overnight)
   - Work on other backend endpoints (authentication, disease info) in parallel
4. **Extend timeline:**
   - Shift Week 05 tasks to Week 06 if necessary
   - Use buffer time allocated in timeline
5. **Communicate:**
   - Inform instructor of delay
   - Update timeline document
   - Document lessons learned for future estimation

**Prevention for next time:**
- Research ML training time before estimating
- Add 50% buffer to complex tasks
- Start ML training early (Week 03) in parallel with other work

---

## Common Mistakes to Avoid

### Mistake 1: Skipping Week 01 to "Start Coding Faster"

**Why it's wrong:** Without understanding the full scope, you will build features that do not align with syllabus requirements, waste time on unnecessary features, or miss critical components.

**Consequence:** During viva, professor asks "Where do you demonstrate XML parsing?" and you realize you never planned for it.

**Fix:** Complete Week 01 thoroughly. The planning saves you 10x time later.

### Mistake 2: Copying Proposal Template Without Customization

**Why it's wrong:** Generic proposals show lack of understanding. Professors can tell when you copied without reading.

**Consequence:** Low marks on proposal, loss of trust, harder viva questions.

**Fix:** Replace every generic statement with specific details about LeafGuard. Use actual class names, endpoints, metrics.

### Mistake 3: Architecture Diagram Without Labels

**Why it's wrong:** Unlabeled diagrams are meaningless. Professors should understand your system without you explaining verbally.

**Consequence:** Diagram is ignored, you lose marks for poor documentation.

**Fix:** Label every box, every arrow. Add a legend. Make it self-explanatory.

### Mistake 4: Shallow Senior Repo Analysis

**Why it's wrong:** Writing "Good project" without specifics shows you did not actually read the code.

**Consequence:** You miss learning opportunities, cannot defend your architectural decisions.

**Fix:** Open actual code files. Read at least 100 lines per repository. Note specific patterns.

### Mistake 5: Unrealistic Timeline

**Why it's wrong:** Planning 2 hours for "Build entire backend" or claiming you will finish in 4 weeks when course is 12 weeks.

**Consequence:** You fall behind, panic, rush, produce low-quality work.

**Fix:** Research task complexity. Ask seniors. Add 30% buffer. Align with course duration.

### Mistake 6: Not Saving Evidence

**Why it's wrong:** During final submission, you claim "I implemented this feature" but have no proof.

**Consequence:** Professors may not believe you. You lose marks.

**Fix:** Screenshot every feature. Commit to Git regularly. Save test results. Record videos.

### Mistake 7: Ignoring Syllabus Mapping

**Why it's wrong:** You build a great app that does not cover required syllabus topics.

**Consequence:** Professors dock marks for missing topics. "Where is RecyclerView?" "Where is XML parsing?"

**Fix:** Create syllabus mapping in Week 01. Check it regularly. Ensure all topics are covered.

### Mistake 8: No Version Control

**Why it's wrong:** Working without Git means no backup, no history, no proof of gradual progress.

**Consequence:** If laptop crashes, you lose everything. Cannot prove you did not copy last minute.

**Fix:** Initialize Git in Week 01. Commit after every logical unit of work.

### Mistake 9: Over-Ambitious Scope

**Why it's wrong:** Adding AI chatbot, blockchain, AR features to an already complex app.

**Consequence:** You cannot finish in 12 weeks. Core features are incomplete.

**Fix:** Stick to LeafGuard's defined scope. Mark extra features as "Future Enhancements" in proposal.

### Mistake 10: Not Reading PROJECT_ARCHITECTURE.md

**Why it's wrong:** PROJECT_ARCHITECTURE.md has all answers. Not reading it means you ask questions that are already answered.

**Consequence:** Wasted time, confusion, incorrect understanding.

**Fix:** Read PROJECT_ARCHITECTURE.md completely. Take notes. Refer back often.

---

## Teacher Impression Tips

### Make a Strong First Impression

**What teachers notice in Week 01:**
1. **Proposal quality:** Is it well-written, specific, and professional?
2. **Diagram clarity:** Can they understand your architecture without asking questions?
3. **Depth of analysis:** Did you just copy templates or actually think deeply?
4. **Evidence of research:** Did you cite sources, analyze senior repos, show learning?

**How to impress:**
- Submit proposal on time (or early)
- Use formal academic language
- Include specific metrics and examples
- Show you understand WHY you are building LeafGuard, not just WHAT

### Demonstrate Academic Rigor

**What this means:**
- Citing sources for claims (Android documentation, research papers)
- Using proper terminology (Activity lifecycle, MVVM pattern, RESTful API)
- Showing trade-off analysis (Cloud AI vs Offline AI comparison)
- Acknowledging limitations (model accuracy constraints, device compatibility)

**How to demonstrate:**
- References section with 10+ credible sources
- Footnotes or citations in proposal
- Trade-off tables (e.g., comparing architectures)
- Risk analysis section

### Show You Did the Work Yourself

**Red flags teachers look for (avoid these):**
- Identical proposals to classmates
- Overly generic statements
- No Git commit history
- Perfect documentation but cannot explain verbally
- Code appears suddenly without intermediate commits

**How to prove originality:**
- Personal writing style in proposal
- Hand-drawn diagrams (then digitized) show thinking process
- Regular Git commits throughout 12 weeks
- Ability to explain every decision during viva
- Evidence folder showing progressive work

### Be Specific in Communication

**Bad:** "I will use networking in my app."
**Good:** "I will use Retrofit 2.9 with OkHttp interceptor to make POST requests to FastAPI /predict endpoint, handling timeouts with 30-second read timeout."

**Bad:** "My app stores data."
**Good:** "I use Room 2.5.0 with a scans table (id, image_path, disease_name, confidence, timestamp) and implement DAO with @Insert, @Query, and @Delete annotations."

**Teachers prefer specifics because:**
- Shows deep understanding
- Easier to evaluate
- Demonstrates you actually built it

---

## Git Workflow Checklist

### Week 01 Git Setup

- [ ] **Install Git:** Download from https://git-scm.com/ and install
- [ ] **Configure Git:**
  ```bash
  git config --global user.name "Your Name"
  git config --global user.email "your.email@example.com"
  ```
- [ ] **Create project folder:**
  ```bash
  mkdir LeafGuard-AI
  cd LeafGuard-AI
  ```
- [ ] **Initialize repository:**
  ```bash
  git init
  ```
- [ ] **Create folder structure:**
  ```bash
  mkdir docs diagrams evidence
  mkdir evidence/week-01
  ```
- [ ] **Create .gitignore file:**
  ```
  # OS files
  .DS_Store
  Thumbs.db

  # Editor files
  .vscode/
  .idea/
  *.swp

  # Temporary files
  *.tmp
  *.log
  ~$*.docx

  # Build artifacts (for when you add Android project)
  *.apk
  *.aab
  build/
  .gradle/
  ```
- [ ] **Create README.md:**
  ```markdown
  # LeafGuard AI

  An Android-based plant disease detection application using deep learning.

  ## Project Overview
  LeafGuard AI is a mobile application that helps farmers identify plant diseases by analyzing leaf images. The app uses a CNN model trained on plant disease datasets and provides treatment recommendations.

  ## Technology Stack
  - Android (Java/Kotlin)
  - Retrofit (REST API)
  - Room (Local database)
  - FastAPI (Backend)
  - TensorFlow Lite (ML inference)

  ## Project Status
  Week 01: Project Understanding & Planning (In Progress)

  ## Author
  [Your Name] - [Your Roll Number]
  CSE 2206 - Mobile Application Development
  ```
- [ ] **Stage files:**
  ```bash
  git add .
  ```
- [ ] **Make first commit:**
  ```bash
  git commit -m "Week 01: Initialize LeafGuard AI project with proposal and architecture docs"
  ```
- [ ] **Verify commit:**
  ```bash
  git log
  ```

### Commit Conventions for This Project

**Format:** `Week XX: [Category] - Brief description`

**Categories:**
- `Init` - Initialization, setup
- `Docs` - Documentation changes
- `Feature` - New feature implementation
- `Fix` - Bug fixes
- `Test` - Adding tests
- `Refactor` - Code refactoring
- `UI` - User interface changes

**Examples:**
```
Week 01: Init - Initialize project with proposal and architecture docs
Week 01: Docs - Add syllabus mapping document
Week 01: Docs - Complete senior repository analysis
Week 02: Init - Create Android Studio project structure
Week 02: Feature - Implement MainActivity layout
Week 03: Feature - Add camera capture functionality
Week 03: Fix - Handle camera permission denial gracefully
```

### When to Commit

**Commit frequently, after:**
- Completing a document section (proposal, analysis)
- Creating a diagram
- Finishing a feature (however small)
- Fixing a bug
- Before trying something risky (so you can revert)

**Commit message quality:**
- ✅ "Week 03: Feature - Implement camera capture with CameraX library"
- ✅ "Week 05: Fix - Handle Retrofit timeout with exponential backoff"
- ❌ "Update" (too vague)
- ❌ "Fixed stuff" (not specific)
- ❌ "asdf" (meaningless)

### Git Commands You Will Use

```bash
# Check status
git status

# Stage specific files
git add docs/proposal.md

# Stage all changes
git add .

# Commit with message
git commit -m "Week 01: Docs - Complete project proposal"

# View commit history
git log

# View commit history (compact)
git log --oneline

# Create a branch (for experimenting)
git branch experiment
git checkout experiment

# Return to main branch
git checkout main

# See what changed
git diff
```

---

## Evidence to Save

### Documents
- [ ] **Proposal (PDF and source):** Save both final PDF and Word/Markdown source
- [ ] **Architecture diagram (PNG and source):** Save PNG export and .drawio or .lucid source file
- [ ] **Syllabus mapping (Markdown/PDF):** Keep updated as you learn more
- [ ] **Senior repo analysis (Markdown/PDF):** Include URLs and dates analyzed
- [ ] **Timeline (Markdown/PDF):** Update weekly as you progress

### Git Evidence
- [ ] **Screenshot of git log:** Showing Week 01 commits
- [ ] **Screenshot of folder structure:** Showing organized docs/, diagrams/, evidence/ folders
- [ ] **.gitignore file:** Proof of proper version control setup

### Learning Evidence
- [ ] **Notes from reading:** Annotated sections of PROJECT_ARCHITECTURE.md
- [ ] **Questions asked:** If you asked instructor or seniors, save those Q&A
- [ ] **Draft diagrams:** Even rough sketches show thinking process
- [ ] **Research notes:** URLs of resources you read, with key takeaways

### Verification Evidence
- [ ] **Validation checklist (this file):** With all boxes checked
- [ ] **Understanding questions answered:** Your written answers in evidence/week-01/
- [ ] **Screenshot of completed proposal:** Title page showing your name and date

### Submission Package
Create a folder `evidence/week-01/` with:
```
evidence/week-01/
├── README.md (this evidence plan)
├── screenshots/
│   ├── git-log.png
│   ├── folder-structure.png
│   └── proposal-title-page.png
├── documents/
│   ├── proposal.pdf
│   ├── syllabus-mapping.md
│   ├── senior-repo-analysis.md
│   └── timeline.md
├── diagrams/
│   ├── architecture.png
│   ├── architecture.drawio (source)
│   └── data-flow.png
└── understanding-answers.md
```

---

## Week Completion Criteria

### You can proceed to Week 02 only when:

**Documentation:**
- [ ] Project proposal is complete (8-10 pages) and reviewed by instructor (if possible)
- [ ] System architecture diagram clearly shows all layers and components
- [ ] Syllabus mapping covers minimum 15 CSE 2206 topics
- [ ] Senior repository analysis covers 5 repositories with depth
- [ ] 12-week timeline is realistic and includes buffer time

**Understanding:**
- [ ] You can explain three-tier architecture without looking at notes
- [ ] You can describe MVVM pattern and why it is used
- [ ] You can trace data flow from user action to result display
- [ ] You can name all technologies used and their purposes
- [ ] You can answer all 10 understanding questions

**Git & Evidence:**
- [ ] Git repository is initialized with proper structure
- [ ] At least 3 commits made with meaningful messages
- [ ] Evidence folder for Week 01 contains all required items
- [ ] .gitignore file exists and excludes temporary files

**Quality:**
- [ ] All documents are free of spelling and grammar errors
- [ ] All diagrams are high resolution and clearly labeled
- [ ] All analysis is specific, not generic
- [ ] All file names follow conventions (lowercase, hyphenated)

**Validation:**
- [ ] Validation checklist (above) has all boxes checked
- [ ] You have shown work to instructor or senior for feedback
- [ ] You feel confident about the project scope and plan
- [ ] You are ready to install Android Studio and start coding

### Self-Assessment

Rate yourself (1-5 scale, 5 being highest):

- **Project understanding:** ___ / 5
- **Documentation quality:** ___ / 5
- **Architecture clarity:** ___ / 5
- **Git proficiency:** ___ / 5
- **Confidence level:** ___ / 5

**If any score is below 3, revisit that area before proceeding to Week 02.**

---

## Next Steps: Week 02 Preview

### What You Will Learn Next Week

Week 02 focuses on Android Basics and UI:
- Installing Android Studio
- Creating your first Android project
- Understanding project structure (manifests, java/, res/)
- Creating XML layouts for 6 activities
- Basic UI components (Button, TextView, ImageView, EditText)
- Navigation between activities using Intents
- Running app on emulator and real device

### Preparation for Week 02

- [ ] **Install Android Studio:** Download from https://developer.android.com/studio (large download, ~1-2 GB)
- [ ] **Review Java/Kotlin basics:** If rusty, watch a refresher tutorial
- [ ] **Read Android basics:** https://developer.android.com/guide/components/activities/intro-activities
- [ ] **Prepare device:** If you have an Android phone, enable Developer Options and USB Debugging

### Week 02 Deliverable Preview

You will create:
- Android Studio project named "LeafGuard"
- 6 empty activities with basic layouts
- Navigation between activities
- First APK that runs on emulator
- Evidence: Screenshots of app running, Git commits showing progressive work

---

**Congratulations on completing Week 01! You now have a solid foundation for building LeafGuard AI. With complete planning and understanding, the next 11 weeks will be focused, efficient, and successful.**

**Remember: A project is built twice – once in planning, once in code. Good planning makes coding easy. Poor planning makes coding painful.**

**Proceed to Week 02 when all completion criteria are met. Good luck!**


<!-- NAV_FOOTER_START -->

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
| *(First week — no previous)* | [Learning Path](../../LEARNING_PATH.md) | [Week 02: Android Basics & UI ➡](../week-02-android-basics-ui/README.md) |

---
