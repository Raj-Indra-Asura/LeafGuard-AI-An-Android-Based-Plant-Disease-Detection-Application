# Week 01 Build Task: Complete Project Foundation Package

## Overview

By the end of Week 01, you must deliver a **complete project foundation package** that proves you understand the entire LeafGuard AI scope, architecture, and implementation plan. This is not just theoretical—every document you create this week will be referenced throughout the next 11 weeks.

**Duration:** 12-15 hours spread across Week 01
**Deadline:** End of Week 01 (before starting Week 02)
**Submission:** All deliverables in Git repository

---

## What You Will Build

You will create **7 major deliverables** that form your project foundation:

1. Complete Project Proposal (8-10 pages)
2. System Architecture Diagram (professional-quality)
3. Syllabus Mapping Document (15+ topics)
4. Senior Repository Analysis Report (5 repos)
5. 12-Week Execution Timeline (detailed plan)
6. Evidence Collection Framework (guide for all 12 weeks)
7. Git Repository Structure (organized for success)

---

## Deliverable 1: Complete Project Proposal

### What to Create

A comprehensive academic proposal document following the template in `/docs/proposal-template.md`.

### Required Sections

**Must include ALL of these sections:**

#### 1. Title Page
- Project title: "LeafGuard AI: An Android-Based Plant Disease Detection Application Using Deep Learning"
- Your name, roll number, class, branch
- Course: CSE 2206 - Mobile Application Development
- Guide name and designation
- Academic year
- Date of submission

#### 2. Abstract (150-200 words)
- Problem statement (plant disease detection challenge)
- Solution approach (Android app with ML)
- Technologies used
- Expected outcomes
- Academic relevance to CSE 2206

#### 3. Introduction (2-3 pages)
- **Background:** Agricultural importance, disease impact statistics
- **Motivation:** Smartphone proliferation, ML advances, farmer needs
- **Relevance to CSE 2206:** List all syllabus topics covered

#### 4. Problem Statement (1 page)
- Clear description of the problem
- Why existing solutions are insufficient
- Who is affected and how
- Specific problem to solve with LeafGuard AI

#### 5. Objectives (1 page)
- **Primary objectives** (3-5 main goals)
- **Secondary objectives** (2-3 additional features)
- **Learning objectives** (what you will learn)
- All objectives must be SMART (Specific, Measurable, Achievable, Relevant, Time-bound)

#### 6. Scope of the Project (1 page)
- **In Scope:** What features WILL be implemented
- **Out of Scope:** What will NOT be implemented (but could be future work)
- **Deliverables:** Software, documentation, diagrams

#### 7. Literature Review (2 pages)
- **Existing commercial apps:** Plantix, Agrio, PlantSnap (analyze 3-5 apps)
- **Research papers:** Deep learning for plant disease detection (cite 3-5 papers)
- **Android development resources:** Official documentation, tutorials
- **Research gap:** What LeafGuard does that others don't

#### 8. Methodology (2 pages)
- **Development approach:** Agile with 1-week sprints
- **System architecture:** Three-tier with MVVM pattern
- **Technology stack:** Android, Retrofit, Room, FastAPI, TensorFlow
- **Implementation plan:** Week-by-week breakdown
- **Testing strategy:** Unit, integration, UI, manual testing

#### 9. Project Timeline (1 page)
- High-level Gantt chart showing 12 weeks
- Major milestones at Weeks 4, 8, 12
- Summary table of weekly deliverables

#### 10. Expected Outcomes (1 page)
- **Technical outcomes:** Working app, accurate model, efficient database
- **Learning outcomes:** Skills gained, concepts mastered
- **Social impact:** How it helps farmers

#### 11. Risk Analysis (1 page)
- **Technical risks:** ML accuracy, API latency, compatibility issues
- **Project management risks:** Scope creep, time overrun
- **Mitigation strategies:** For each risk, how to prevent or handle

#### 12. Budget Estimation (1 page)
- Development costs (mostly free/open-source)
- Infrastructure costs (hosting, domain)
- Documentation costs (printing, binding)
- Total estimated cost: ₹700-₹2500

#### 13. References (1 page)
- Minimum 10 credible sources
- Format: IEEE or APA style
- Include: Android documentation, research papers, library documentation

### Quality Requirements

**Writing:**
- Formal academic tone (no first person)
- No spelling or grammar errors
- Clear, concise sentences
- Proper headings and subheadings
- Page numbers on all pages

**Content:**
- Specific to LeafGuard AI (not generic)
- Statistics cited with sources
- Technical terms used correctly
- Realistic and achievable goals

**Format:**
- 8-10 pages minimum (excluding references)
- Font: Times New Roman, 12pt
- Line spacing: 1.5 or Double
- Margins: 1 inch all sides
- Sections numbered (1, 1.1, 1.2, etc.)

### File Outputs

- `docs/proposal.md` - Markdown version for Git
- `docs/proposal.pdf` - PDF for submission
- `docs/proposal.docx` - Word version (keep as backup)

### Time Estimate: 4-5 hours

---

## Deliverable 2: System Architecture Diagram

### What to Create

A professional-quality diagram showing all components of LeafGuard AI and how they connect.

### Required Components

**Must include ALL of these:**

#### Presentation Layer (Top)
- MainActivity
- ScanActivity
- ResultActivity
- HistoryActivity
- DiseaseLibraryActivity
- SettingsActivity
- Fragments (if using)
- RecyclerView Adapters

#### Business Logic Layer (Middle-Upper)
- ScanViewModel
- HistoryViewModel
- AuthViewModel (if adding authentication)
- LiveData/StateFlow indicators
- Business logic annotations

#### Repository Layer (Middle)
- ScanRepository
- DiseaseRepository
- Coordination logic notes

#### Data Layer (Middle-Lower, split into two sides)

**Local Data (Left):**
- Room Database
  - ScanEntity
  - UserEntity
  - DAOs
- XML Parser
  - disease_library.xml
- File Storage
  - Saved images

**Remote Data (Right):**
- Retrofit Client
  - ApiService interface
  - OkHttpClient
  - Gson converter
- API Endpoints
  - POST /predict
  - GET /disease/{name}

#### Backend Layer (Bottom)
- FastAPI Application
  - /predict endpoint
  - /disease endpoint
- TensorFlow Model
  - Model architecture
  - Input/output format
- PostgreSQL Database (optional)

#### External Components (Sides)
- Camera Hardware (left side)
- Gallery/Storage (left side)
- TFLite Model in Assets (right side)

### Visual Requirements

**Arrows and Flow:**
- Solid arrows for data flow (→)
- Dashed arrows for control flow (⇢)
- Label every arrow with what is being passed
  - "Bitmap image"
  - "HTTP POST request"
  - "JSON response"
  - "LiveData update"

**Colors:**
- Presentation Layer: Light Blue (#E3F2FD)
- Business Logic: Light Green (#C8E6C9)
- Data Layer: Light Yellow (#FFF9C4)
- Local Storage: Light Purple (#E1BEE7)
- Network: Light Orange (#FFE0B2)
- Backend: Light Red (#FFCDD2)

**Legend:**
- Include legend box explaining:
  - Solid arrows = Data flow
  - Dashed arrows = Control flow
  - Color coding for layers
  - Symbols used

**Labels:**
- Title at top: "LeafGuard AI - System Architecture"
- Subtitle: "Three-Tier Android Application with RESTful Backend"
- Your name and date
- Version: "v1.0"
- Layer labels on left side

### Tools to Use

**Recommended:**
- Draw.io (https://app.diagrams.net/) - Free, no account needed
- Lucidchart (https://www.lucidchart.com/) - Free tier available
- Microsoft Visio - If you have access
- Hand-drawn + photograph/scan - If digitally challenged (but digitize later)

### File Outputs

- `diagrams/system-architecture.png` - High-res PNG (300 DPI)
- `diagrams/system-architecture.pdf` - For printing
- `diagrams/system-architecture.drawio` - Source file (editable)
- `diagrams/architecture-v1-draft.jpg` - Initial sketch (if hand-drawn first)

### Quality Checklist

- [ ] All layers clearly separated with visual distinction
- [ ] All 6+ activities shown and labeled
- [ ] ViewModels shown with connections to Activities
- [ ] Repository layer shown between ViewModels and Data
- [ ] Room Database shown with table names
- [ ] Retrofit shown with connection to backend
- [ ] Backend shown with endpoints
- [ ] ML model shown both cloud and on-device
- [ ] All arrows labeled
- [ ] Legend present and clear
- [ ] Professional color scheme
- [ ] Readable when printed on A4 paper
- [ ] Title, your name, and date visible

### Time Estimate: 2-3 hours

---

## Deliverable 3: Syllabus Mapping Document

### What to Create

A comprehensive table mapping every CSE 2206 syllabus topic to specific LeafGuard components with evidence locations.

### Required Format

**Create a table with these columns:**

| # | Syllabus Topic | LeafGuard Component | File/Class Path | Specific Implementation | Evidence |
|---|----------------|---------------------|-----------------|------------------------|----------|

### Minimum 15 Topics to Map

**Must include these (at minimum):**

1. **Activities**
   - Component: 6 activities
   - File: `MainActivity.java`, `ScanActivity.java`, etc.
   - Implementation: Activity lifecycle methods (onCreate, onPause, onResume)
   - Evidence: Activity files, lifecycle logging in logcat

2. **Intents**
   - Component: Explicit intents (navigation), Implicit intents (camera, gallery)
   - File: `MainActivity.java` line 45-60
   - Implementation: `Intent intent = new Intent(MainActivity.this, ScanActivity.class);`
   - Evidence: Code snippets showing intent creation and startActivity()

3. **Fragments**
   - Component: HomeFragment, LibraryFragment
   - File: `fragments/HomeFragment.java`
   - Implementation: Fragment lifecycle, FragmentTransaction
   - Evidence: Fragment files, XML layouts

4. **RecyclerView**
   - Component: History list, Disease library list
   - File: `adapters/HistoryAdapter.java`, `HistoryActivity.java`
   - Implementation: Adapter pattern, ViewHolder, onBindViewHolder
   - Evidence: Adapter class, RecyclerView in XML layout

5. **Retrofit (Networking)**
   - Component: API service for disease detection
   - File: `network/ApiService.java`, `network/RetrofitClient.java`
   - Implementation: @POST, @Multipart, Call<Response> with callbacks
   - Evidence: Interface definition, network call in Repository

6. **Room Database**
   - Component: Scan history storage
   - File: `database/AppDatabase.java`, `database/ScanEntity.java`, `database/ScanDao.java`
   - Implementation: @Entity, @Dao, @Database annotations, SQLite backend
   - Evidence: Entity classes, DAO interfaces, database queries

7. **XML Parsing**
   - Component: Disease information library
   - File: `utils/XmlParser.java`, `assets/disease_library.xml`
   - Implementation: XmlPullParser reading XML nodes
   - Evidence: XML file, parsing code, Disease objects created

8. **JSON Parsing**
   - Component: API response handling
   - File: `models/UploadResponse.java`, Gson in Retrofit
   - Implementation: @SerializedName annotations, automatic parsing
   - Evidence: Response model class, Gson converter setup

9. **Runtime Permissions**
   - Component: Camera and storage access
   - File: `MainActivity.java` line 100-130
   - Implementation: ActivityCompat.requestPermissions, onRequestPermissionsResult
   - Evidence: Permission request code, AndroidManifest.xml declarations

10. **Camera Integration**
    - Component: Leaf image capture
    - File: `ScanActivity.java`
    - Implementation: Camera2 API or CameraX, image capture callback
    - Evidence: Camera preview code, captured image handling

11. **Asynchronous Operations**
    - Component: Background network and database operations
    - File: Throughout ViewModels and Repositories
    - Implementation: Kotlin Coroutines or AsyncTask
    - Evidence: Async calls in ViewModel, background threads for DB

12. **Notifications**
    - Component: Scan reminder notifications
    - File: `utils/NotificationHelper.java`
    - Implementation: NotificationManager, NotificationChannel
    - Evidence: Notification creation code, notification showing on device

13. **Shared Preferences**
    - Component: User settings storage
    - File: `SettingsActivity.java`
    - Implementation: SharedPreferences.Editor, commit()
    - Evidence: Settings reading/writing code

14. **Material Design**
    - Component: UI components (buttons, cards, FAB)
    - File: `res/layout/activity_main.xml`, `res/values/styles.xml`
    - Implementation: MaterialComponents theme, Material widgets
    - Evidence: XML layouts using Material components

15. **MVVM Architecture**
    - Component: Overall app structure
    - File: ViewModels, Repositories, Activities observing LiveData
    - Implementation: ViewModel, LiveData, Repository pattern
    - Evidence: ViewModel classes, LiveData observation in Activities

**Add more topics if your syllabus includes:**
- Services
- Broadcast Receivers
- Content Providers
- Google Maps integration
- Firebase integration
- Location services
- Sensors
- Animation
- Custom Views
- etc.

### File Outputs

- `docs/syllabus-mapping.md` - Markdown table
- `docs/syllabus-mapping.xlsx` - Excel version (easier to edit)
- `docs/syllabus-mapping.pdf` - For submission

### Quality Checklist

- [ ] Minimum 15 topics covered
- [ ] Every topic has specific file path
- [ ] Every topic has line numbers or section reference
- [ ] Every topic explains HOW it's implemented, not just WHERE
- [ ] Every topic has evidence location specified
- [ ] Table is well-formatted and readable
- [ ] No topics left unmapped from syllabus

### Time Estimate: 2-3 hours

---

## Deliverable 4: Senior Repository Analysis Report

### What to Create

A detailed analysis of 5 real-world Android repositories, documenting architecture patterns, strengths, weaknesses, and key learnings.

### Required Structure

For EACH of the 5 repositories, document:

#### Basic Information (100 words)
- Repository name and URL
- GitHub stars, forks, last update date
- Primary programming language (Java/Kotlin)
- Brief description (what the app does)
- Number of contributors

#### Architecture Analysis (200 words)
- Architecture pattern identified (MVVM, MVP, MVC, or none)
- Package/folder structure (show tree or describe)
- How are UI components organized?
- How is data layer separated?
- Does it follow Single Responsibility Principle?

#### Technology Stack (150 words)
- Android SDK versions (minSdk, targetSdk)
- Libraries used with versions:
  - Networking (Retrofit, Volley, etc.)
  - Database (Room, SQLite, Realm, etc.)
  - Image loading (Glide, Picasso, Coil, etc.)
  - Dependency injection (Hilt, Dagger, Koin, manual)
  - Others (Coroutines, RxJava, etc.)
- Build system (Gradle, version catalogs)

#### Code Quality Assessment (200 words)
- **Readability:** Rate 1-5 stars, explain
  - Are names descriptive?
  - Are methods reasonably sized?
  - Is code well-organized?
- **Documentation:** Rate 1-5 stars
  - Are there comments?
  - Is there Javadoc?
  - Is README comprehensive?
- **Naming Conventions:** Rate 1-5 stars
  - Consistent naming?
  - Following Android conventions?
- **File Sizes:** Are classes too large? (>500 lines is a red flag)

#### Strengths (Minimum 3, with examples)
Be specific. Don't just say "good code" - explain what's good and show an example.

**Example:**
"✅ **Clean separation of concerns:** Each ViewModel has exactly one corresponding Activity/Fragment. For example, `ScanViewModel` is only used by `ScanActivity`, making dependencies clear and testable."

#### Weaknesses (Minimum 3, with examples)
Be specific about what could be improved.

**Example:**
"❌ **Large Activity classes:** `MainActivity.java` has 950 lines including networking, UI logic, and business logic. Should be refactored using ViewModel and Repository pattern."

#### Key Learnings for LeafGuard (3-5 points)
What will you adopt, avoid, or adapt?

**Format:**
- **Adopt:** [Specific practice to copy]
- **Avoid:** [Specific mistake to prevent]
- **Adapt:** [Specific idea to modify and use]

### 5 Repositories to Find

**Search on GitHub for:**

1. **Plant disease detection app**
   - Search: "android plant disease detection"
   - Pick one with 100+ stars
   - Focus on: ML integration, image processing

2. **MVVM + Retrofit + Room example**
   - Search: "android mvvm kotlin room retrofit"
   - Pick one with 500+ stars
   - Focus on: Architecture, data flow

3. **Machine learning on Android**
   - Search: "android tensorflow lite image classification"
   - Pick one with 200+ stars
   - Focus on: TFLite integration, model loading

4. **Camera integration example**
   - Search: "android camerax example"
   - Pick one with 300+ stars
   - Focus on: Camera2 API or CameraX usage

5. **Complete CRUD app**
   - Search: "android room database example crud"
   - Pick one with 400+ stars
   - Focus on: Database operations, UI patterns

### File Outputs

- `docs/senior-repo-analysis.md` - Markdown document
- `docs/senior-repo-analysis.pdf` - For submission

### Quality Requirements

- **Length:** Minimum 2000 words (400 words per repository)
- **Depth:** Actually read the code, don't just skim README
- **Specificity:** Cite actual files, line numbers, classes
- **Actionable:** Every learning should be applicable to LeafGuard

### Time Estimate: 3-4 hours

---

## Deliverable 5: 12-Week Execution Timeline

### What to Create

A detailed week-by-week plan with deliverables, learning objectives, evidence collection points, and time estimates.

### Required for Each Week

**Week Header:**
- Week number and title (e.g., "Week 03: Camera & Gallery Integration")

**Learning Objectives:**
- 3-5 things you will learn this week
- Be specific (not "learn Android" but "implement Camera2 API for image capture")

**Deliverables:**
- 3-5 concrete outputs you will produce
- Be specific and measurable
- Example: "Working camera capture saving to internal storage"

**Evidence to Collect:**
- Screenshots to take
- Git commits to make (how many, what features)
- Videos to record
- Documentation to write

**Estimated Hours:**
- Realistic time estimate (8-15 hours per week)
- Break down by subtask

**Dependencies:**
- What must be complete before starting this week?

**Risks & Mitigation:**
- What could go wrong?
- How will you handle it?

### Major Milestones

**Define 3 major milestones:**

**Milestone 1 - End of Week 4:**
- Backend API operational
- Basic Android UI navigable
- Can make test API calls

**Milestone 2 - End of Week 8:**
- All features implemented
- App functional end-to-end
- Database, networking, ML working

**Milestone 3 - End of Week 12:**
- Testing complete
- Documentation finalized
- Submission package ready

### Buffer & Contingency

**Include:**
- 2-3 hours buffer per week for debugging
- Contingency plans for common delays:
  - If backend takes longer than Week 4
  - If TFLite is too difficult in Week 9
  - If behind schedule by Week 10

### File Outputs

- `docs/12-week-timeline.md` - Markdown version
- `docs/12-week-timeline.xlsx` - Excel Gantt chart (optional but nice)
- `docs/12-week-timeline.pdf` - For submission

### Time Estimate: 2-3 hours

---

## Deliverable 6: Evidence Collection Framework

### What to Create

A comprehensive guide for what evidence to save each week and how to organize it.

### Required Contents

#### 1. Purpose Statement
Why collect evidence? (for academic evaluation, to prove progressive work)

#### 2. Evidence Types

**Define guidelines for:**

**Screenshots:**
- Resolution: 1080p minimum
- File naming: `week-XX-feature-name-YYYY-MM-DD.png`
- What to capture: UI screens, code, terminal output, test results
- Annotations: Use arrows/labels to highlight key features

**Git Commits:**
- Commit frequency: After every logical unit of work
- Message format: `Week XX: [Category] - Brief description`
- Categories: Init, Feature, Fix, Docs, Test, Refactor, UI
- Example: `Week 03: Feature - Implement camera capture with CameraX`

**Videos:**
- Length: 30-60 seconds per feature demo
- Quality: 720p minimum
- What to show: Feature walkthroughs, error handling, user flows
- Narration: Optional but helpful

**Documentation:**
- Format: Markdown for version control, PDF for submission
- Versioning: Keep old versions with date suffix
- Backup: Export diagrams in multiple formats (PNG, PDF, source)

**Test Results:**
- Unit test output (JUnit XML reports)
- Manual test case spreadsheets
- Performance metrics (app launch time, API response time)

#### 3. Weekly Evidence Checklist

**For each week (Week 01-12), specify:**

**Week 01:**
- [ ] Proposal PDF
- [ ] Architecture diagram PNG + source
- [ ] Syllabus mapping
- [ ] Senior repo analysis
- [ ] Timeline document
- [ ] Git init screenshot

**Week 02:**
- [ ] Android Studio project screenshot
- [ ] Package structure screenshot
- [ ] All activities created
- [ ] Layout XML files
- [ ] First APK build success
- [ ] App running on emulator video

[Continue for all 12 weeks...]

#### 4. Folder Structure

**Define standard structure:**
```
evidence/
├── week-01/
│   ├── README.md (this week's summary)
│   ├── screenshots/
│   ├── documents/
│   ├── diagrams/
│   └── exercises/
├── week-02/
│   ├── README.md
│   ├── screenshots/
│   ├── videos/
│   └── apk/
├── week-03/
│   ├── screenshots/
│   ├── videos/
│   └── code-samples/
...
```

#### 5. Submission Checklist

**Final submission package (Week 12) must include:**
- [ ] Final APK (debug and release)
- [ ] Complete source code (zipped)
- [ ] Final report (PDF, 30-40 pages)
- [ ] All diagrams (high-res)
- [ ] User manual (PDF)
- [ ] Presentation slides (PPT/PDF)
- [ ] Demo video (2-3 minutes)
- [ ] Git repository link
- [ ] All weekly evidence folders

### File Outputs

- `evidence/README.md` - Master evidence guide
- `evidence/week-01/README.md` - Week 01 specific guide
- Template README for each week folder

### Time Estimate: 1-2 hours

---

## Deliverable 7: Git Repository Structure

### What to Create

An organized Git repository with proper folder structure, .gitignore, and meaningful first commit.

### Required Structure

```
LeafGuard-AI/
├── .git/                          # Git metadata (created by git init)
├── .gitignore                     # Ignore patterns
├── README.md                      # Project overview
├── docs/                          # All documentation
│   ├── proposal.md
│   ├── proposal.pdf
│   ├── architecture.md
│   ├── syllabus-mapping.md
│   ├── senior-repo-analysis.md
│   ├── 12-week-timeline.md
│   ├── api-documentation.md       # Template for Week 04
│   └── user-manual.md             # Template for Week 12
├── diagrams/                      # All visual diagrams
│   ├── system-architecture.png
│   ├── system-architecture.drawio
│   ├── data-flow.png
│   ├── er-diagram.png            # Template for Week 07
│   ├── sequence-diagrams/        # Folder for multiple sequence diagrams
│   └── use-case-diagrams/        # Folder for use case diagrams
├── evidence/                      # Weekly progress evidence
│   ├── README.md                  # Evidence collection guide
│   ├── week-01/
│   │   ├── README.md
│   │   ├── screenshots/
│   │   ├── documents/
│   │   ├── diagrams/
│   │   └── exercises/
│   ├── week-02/                   # Create in Week 02
│   ├── week-03/                   # Create in Week 03
│   └── ...
└── android-app/                   # Create in Week 02
    └── (Android Studio project goes here)
```

### .gitignore Contents

```
# Operating System Files
.DS_Store
.DS_Store?
._*
.Spotlight-V100
.Trashes
ehthumbs.db
Thumbs.db
*.swp
*~

# Editor/IDE Files
.vscode/
.idea/
*.iml
.project
.classpath
.settings/
*.sublime-project
*.sublime-workspace

# Temporary Files
*.tmp
*.temp
*.log
*.bak
*.old
~$*.docx
~$*.xlsx
~$*.pptx

# Build Artifacts (for Android project in Week 02)
*.apk
*.aab
*.ap_
*.dex
build/
.gradle/
local.properties
captures/
.externalNativeBuild
.cxx

# Large Media Files (video demos)
*.mov
*.avi
*.mp4
*.mkv

# Compiled Class Files
*.class
bin/
gen/

# Package Files
*.jar
*.war
*.ear
*.zip
*.tar.gz
*.rar

# Android NDK
obj/
jniLibs/

# Keystore Files (NEVER commit these!)
*.jks
*.keystore
keystore.properties

# Firebase/Google Services (if added later)
google-services.json

# Secrets (NEVER commit these!)
.env
secrets.properties
api_keys.txt
