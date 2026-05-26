# Week 01 Exercises: Project Understanding & Foundation

## Exercise Instructions

Complete all exercises and save your work in `evidence/week-01/exercises/`. Each exercise has clear expected outputs. Do not skip exercises - each builds understanding needed for implementation.

---

## Exercise 1: Data Flow Diagram Creation

### Objective
Understand how data flows through LeafGuard AI by creating a complete flow diagram.

### Task
Draw a flowchart (hand-drawn or digital) showing the complete data flow for this scenario:

**Scenario:** User opens app, taps "Scan", captures leaf photo, receives disease prediction, views result.

### Requirements

**Must include these components:**
1. User (actor)
2. MainActivity
3. Camera Intent
4. ScanActivity
5. ScanViewModel
6. ScanRepository
7. RetrofitClient
8. FastAPI Backend
9. TensorFlow Model
10. Room Database
11. ResultActivity

**Must show these data flows:**
- Image captured (Camera → ScanActivity)
- Image path passed (ScanActivity → ViewModel)
- API request (ViewModel → Repository → Retrofit → Backend)
- ML inference (Backend → TensorFlow Model)
- Prediction returned (Backend → Retrofit → Repository → ViewModel)
- Result saved (ViewModel → Room Database)
- UI updated (ViewModel → ResultActivity)

### Expected Output

**Format:** PNG or PDF image file named `data-flow-diagram.png`

**Quality checklist:**
- [ ] All 11 components are shown
- [ ] Arrows are labeled with data being passed
- [ ] Flow direction is clear (left-to-right or top-to-bottom)
- [ ] Legend explains symbols used
- [ ] Readable when printed on A4 paper

**Example arrow labels:**
- "Bitmap image"
- "HTTP POST /predict"
- "JSON response"
- "ScanEntity object"
- "LiveData update"

### Verification

Test your diagram by tracing the flow and explaining each step aloud. If you cannot explain a transition, your diagram is incomplete.

---

## Exercise 2: Syllabus Mapping Matrix

### Objective
Map every CSE 2206 syllabus topic to specific LeafGuard components, proving complete coverage.

### Task
Create a comprehensive mapping table connecting syllabus topics to implementation details.

### Requirements

**Table format (Markdown or Excel):**

| # | Syllabus Topic | LeafGuard Component | File/Class Name | Specific Feature | How It Demonstrates Understanding |
|---|----------------|---------------------|-----------------|------------------|-----------------------------------|
| 1 | Activities | 6 activities | MainActivity.java, ScanActivity.java, etc. | Screen navigation | Implements onCreate(), onPause(), handles lifecycle |
| 2 | ... | ... | ... | ... | ... |

**Must cover minimum 15 topics:**
1. Activities
2. Intents (Explicit and Implicit)
3. Fragments
4. RecyclerView
5. Retrofit (HTTP client)
6. Room Database
7. XML Parsing
8. JSON Parsing
9. Runtime Permissions
10. Camera Integration
11. AsyncTask / Coroutines
12. Notifications
13. Shared Preferences
14. Material Design
15. MVVM Architecture

**For each topic, specify:**
- Exact file path where it appears
- Line numbers (if known, estimate is okay)
- Brief explanation of implementation
- Why this demonstrates mastery of the topic

### Expected Output

**File:** `syllabus-mapping.md` or `syllabus-mapping.xlsx`

**Example row:**
```
| 5 | Retrofit | Network layer | app/src/main/java/com/example/leafguard/network/ApiService.java | @POST /predict endpoint | Demonstrates understanding of REST API calls, multipart upload, asynchronous callbacks, error handling |
```

### Verification

Can you find every topic in your project? If a topic is missing, note it and plan how to add it.

---

## Exercise 3: Architecture Diagram Drawing

### Objective
Create a professional-quality system architecture diagram showing all LeafGuard components.

### Task
Draw a complete architecture diagram using Draw.io, Lucidchart, or pen-and-paper (then photograph/scan).

### Requirements

**Must include these layers (top to bottom):**

1. **Presentation Layer:**
   - All 6 activities in boxes
   - Fragments (if using)
   - Adapters for RecyclerViews

2. **ViewModel Layer:**
   - ScanViewModel
   - HistoryViewModel
   - Other ViewModels
   - LiveData connections

3. **Repository Layer:**
   - ScanRepository
   - DiseaseRepository
   - Coordination logic

4. **Data Layer (split into Local and Remote):**
   - Local: Room Database, XML Parser, File Storage
   - Remote: Retrofit Client, API Endpoints

5. **Backend Layer:**
   - FastAPI application
   - TensorFlow Model
   - PostgreSQL Database (optional)

**Arrows must show:**
- Data flow direction (solid arrows)
- Control flow (dashed arrows)
- Labels on every arrow (e.g., "Image Upload", "JSON Response")

**Additional elements:**
- Legend explaining symbols and colors
- Title: "LeafGuard AI System Architecture"
- Your name and date
- Layer labels (Presentation, Business Logic, Data, Backend)

### Expected Output

**Files:**
- `architecture-diagram.png` (exported image, 300 DPI)
- `architecture-diagram.drawio` (source file if using Draw.io)
- `architecture-diagram.pdf` (for printing)

**Quality checklist:**
- [ ] All layers clearly separated
- [ ] All components labeled
- [ ] All arrows labeled
- [ ] Legend present
- [ ] Professional color scheme
- [ ] Readable when printed on A4

### Verification

Show your diagram to someone unfamiliar with the project. Can they understand the system flow without your explanation? If not, improve clarity.

---

## Exercise 4: Senior Repository Deep Dive

### Objective
Analyze real-world Android projects to learn best practices and avoid common mistakes.

### Task
Find and thoroughly analyze 5 Android repositories on GitHub. Focus on architecture, code quality, and patterns.

### Requirements

**Repositories to find:**

1. **One plant disease detection app** (search: "android plant disease detection")
2. **One MVVM example** (search: "android mvvm kotlin room retrofit")
3. **One machine learning on Android** (search: "android tensorflow lite image classification")
4. **One camera app** (search: "android camerax example")
5. **One complete CRUD app** (search: "android room database example")

**For each repository, document:**

#### Basic Information
- Repository URL
- Stars, Forks, Last update date
- Primary programming language
- Number of contributors

#### Architecture Analysis
- Architecture pattern (MVVM, MVP, MVC, or none)
- Package/folder structure (screenshot or description)
- How are activities organized?
- How is data layer separated?

#### Technology Stack
- Android SDK version (minSdk, targetSdk)
- Libraries used (list with versions):
  - Networking library (Retrofit, Volley, etc.)
  - Database library (Room, SQLite, etc.)
  - Image loading (Glide, Picasso, Coil)
  - Dependency injection (Hilt, Dagger, manual)
  - Others

#### Code Quality Observations
- Code readability (1-5 stars, with explanation)
- Comments and documentation (1-5 stars)
- Naming conventions (1-5 stars)
- File/class sizes (are they too large?)

#### Strengths (minimum 3 per repository)
What did they do well? Be specific with examples.

**Example:**
"Clean package structure: Organized by feature (`ui/scan/`, `ui/history/`) instead of by type (`activities/`, `fragments/`). This makes navigation easier."

#### Weaknesses (minimum 3 per repository)
What could be improved? Be specific.

**Example:**
"MainActivity.java has 950 lines. Too much responsibility - should be split into smaller classes or use Fragments."

#### Key Learnings
What will you adopt or avoid in LeafGuard? (2-3 points per repository)

**Example:**
- Adopt: Their image compression utility before upload
- Avoid: Hard-coded base URL - use BuildConfig instead

### Expected Output

**File:** `senior-repo-analysis.md`

**Minimum length:** 2000 words (400 words per repository)

**Format template:**
```markdown
# Senior Repository Analysis

## Repository 1: [Name]

**URL:** https://github.com/user/repo
**Stars:** 450 | **Forks:** 89 | **Last Update:** 2024-01-10
**Language:** Kotlin

### Project Overview
[1-2 paragraph description]

### Architecture
- Pattern: MVVM with Repository pattern
- Package structure:
  ```
  com.example.app/
  ├── data/
  │   ├── repository/
  │   └── source/
  ├── ui/
  │   ├── scan/
  │   └── history/
  └── utils/
  ```

### Technology Stack
- Android SDK: minSdk 24, targetSdk 33
- Retrofit: 2.9.0
- Room: 2.5.0
- Glide: 4.14.2
- Kotlin Coroutines: 1.6.4

### Code Quality
- Readability: ⭐⭐⭐⭐☆ (4/5) - Clean code, good naming
- Documentation: ⭐⭐⭐☆☆ (3/5) - Some Javadoc, could be better
- Naming: ⭐⭐⭐⭐⭐ (5/5) - Consistent, descriptive

### Strengths
1. [Specific strength with example]
2. [Specific strength with example]
3. [Specific strength with example]

### Weaknesses
1. [Specific weakness with example]
2. [Specific weakness with example]
3. [Specific weakness with example]

### Key Learnings for LeafGuard
- **Adopt:** [What to copy]
- **Avoid:** [What not to copy]
- **Adapt:** [What to modify and use]

---

[Repeat for repositories 2-5]
```

### Verification

Can you explain how each repository's architecture works? Can you identify patterns? If not, re-read the code more carefully.

---

## Exercise 5: Project Proposal Abstract Writing

### Objective
Practice formal academic writing by crafting a professional abstract for LeafGuard AI.

### Task
Write a 200-250 word abstract for your project proposal.

### Requirements

**Must include:**

1. **Problem statement** (2-3 sentences)
   - What problem exists?
   - Why is it important?
   - Who is affected?

2. **Solution approach** (3-4 sentences)
   - What is LeafGuard AI?
   - How does it solve the problem?
   - What technology is used?

3. **Key features** (2-3 sentences)
   - What can users do?
   - What makes it unique?

4. **Expected outcomes** (1-2 sentences)
   - What impact will it have?
   - How does it help farmers?

5. **Academic relevance** (1 sentence)
   - How does it cover CSE 2206 syllabus?

**Writing rules:**
- ❌ Do NOT use first person (I, we, our, my)
- ✅ Use third person or passive voice
- ❌ Do NOT use informal language ("cool", "awesome")
- ✅ Use formal academic language
- ❌ Do NOT make unsupported claims
- ✅ Cite statistics where possible
- ❌ Do NOT use bullet points
- ✅ Write in paragraph form

### Expected Output

**File:** `proposal-abstract.txt` or `proposal-abstract.md`

**Example structure:**
```
Agriculture is fundamental to global food security, yet plant diseases cause 20-40% crop yield losses annually [citation needed]. Farmers in rural areas lack timely access to plant pathologists, leading to delayed diagnosis and exponential crop damage. Traditional visual inspection methods are time-consuming, expensive, and subjective.

This project proposes LeafGuard AI, an Android-based mobile application that leverages deep learning for real-time plant disease detection. The system utilizes a convolutional neural network trained on 54,000 leaf images to classify diseases with 85%+ accuracy. Users capture leaf images using their smartphone camera, which are analyzed either through a cloud-based FastAPI backend or an on-device TensorFlow Lite model for offline operation.

The application implements a hybrid architecture supporting both online and offline modes, stores scan history locally using Room database, and provides comprehensive disease information through XML-based treatment recommendations. Key features include image capture integration, RESTful API communication via Retrofit, local data persistence, and a Material Design user interface.

LeafGuard AI democratizes access to plant disease diagnostics, providing farmers with an affordable, accessible, and accurate tool for crop protection. The project comprehensively addresses the CSE 2206 Mobile Application Development syllabus, demonstrating proficiency in Android development, networking, database management, XML/JSON parsing, and machine learning integration.
```

### Verification Checklist

- [ ] Word count: 200-250 words
- [ ] No first-person pronouns
- [ ] Formal academic tone
- [ ] All five required sections present
- [ ] No spelling or grammar errors
- [ ] Statistics cited (or marked [citation needed])
- [ ] Accurately describes LeafGuard AI

---

## Exercise 6: Git Repository Setup & First Commit

### Objective
Initialize version control for LeafGuard AI with proper structure and meaningful first commit.

### Task
Set up a Git repository with organized folder structure and create your first commit.

### Requirements

**Step 1: Create project folder**
```bash
mkdir LeafGuard-AI
cd LeafGuard-AI
```

**Step 2: Initialize Git**
```bash
git init
```

**Step 3: Configure Git (if not already done)**
```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

**Step 4: Create folder structure**
```
LeafGuard-AI/
├── docs/
│   ├── proposal.md (create this)
│   ├── architecture.md (create this)
│   ├── syllabus-mapping.md (from Exercise 2)
│   └── senior-repo-analysis.md (from Exercise 4)
├── diagrams/
│   ├── data-flow-diagram.png (from Exercise 1)
│   ├── architecture-diagram.png (from Exercise 3)
│   └── architecture-diagram.drawio (source)
├── evidence/
│   └── week-01/
│       ├── README.md (create this)
│       ├── exercises/ (folder)
│       └── screenshots/ (folder)
├── .gitignore (create this)
└── README.md (create this)
```

**Step 5: Create .gitignore**
```
# OS files
.DS_Store
Thumbs.db
*.swp

# Editor files
.vscode/
.idea/
*.iml

# Temporary files
*.tmp
*.log
~$*.docx
~$*.xlsx

# Build artifacts (for when Android project is added)
*.apk
*.aab
build/
.gradle/
local.properties

# Large files
*.mov
*.avi
```

**Step 6: Create README.md**
```markdown
# LeafGuard AI

An Android-based plant disease detection application using deep learning.

## Project Overview

LeafGuard AI helps farmers identify plant diseases by analyzing leaf images using a convolutional neural network. The app provides instant disease identification, treatment recommendations, and maintains a local history of scans.

## Technology Stack

- **Android:** Java/Kotlin with MVVM architecture
- **Networking:** Retrofit 2.x for REST API communication
- **Database:** Room for local data persistence
- **Backend:** FastAPI (Python)
- **Machine Learning:** TensorFlow Lite for on-device inference

## Project Status

**Current Phase:** Week 01 - Project Understanding & Planning

- [x] Project scope defined
- [x] Architecture diagram created
- [x] Syllabus mapping completed
- [x] Senior repository analysis done
- [ ] Android project creation (Week 02)
- [ ] UI implementation (Week 02-03)

## Features

### Core Features
- 📸 Camera and gallery image capture
- 🤖 AI-powered disease detection (cloud and offline modes)
- 💾 Local scan history with Room database
- 📚 Disease information library (XML-based)
- 🎨 Material Design user interface

### Technical Features
- Hybrid cloud-offline architecture
- RESTful API integration with Retrofit
- TensorFlow Lite for on-device ML inference
- Room database for local persistence
- XML parsing for disease information
- Runtime permission handling

## Project Structure

```
LeafGuard-AI/
├── docs/           # Project documentation
├── diagrams/       # Architecture diagrams
├── evidence/       # Weekly progress evidence
└── android-app/    # Android Studio project (Week 02)
```

## Academic Information

**Course:** CSE 2206 - Mobile Application Development
**Student:** [Your Name]
**Roll Number:** [Your Roll Number]
**Academic Year:** 2024-2025

## Syllabus Coverage

This project comprehensively covers all CSE 2206 topics:
- Android fundamentals (Activities, Intents, Lifecycle)
- UI design (XML layouts, Material Design, RecyclerView)
- Networking (Retrofit, HTTP, REST APIs)
- Data persistence (Room database, SharedPreferences)
- XML and JSON parsing
- Camera integration and runtime permissions
- Asynchronous operations (Coroutines)
- MVVM architecture pattern

## Timeline

- **Week 01:** Project understanding and planning ✅
- **Week 02:** Android basics and UI development
- **Week 03:** Camera and gallery integration
- **Week 04:** FastAPI backend development
- **Week 05:** Networking with Retrofit
- **Week 06:** Cloud ML model integration
- **Week 07:** Room database for history
- **Week 08:** XML disease library parsing
- **Week 09:** TensorFlow Lite offline AI
- **Week 10:** Notifications and sharing
- **Week 11:** Testing and debugging
- **Week 12:** Final submission and presentation

## Documentation

- [Project Proposal](docs/proposal.md)
- [Architecture Documentation](docs/architecture.md)
- [Syllabus Mapping](docs/syllabus-mapping.md)
- [Senior Repository Analysis](docs/senior-repo-analysis.md)

## License

This is an academic project for educational purposes.

## Contact

[Your Name]
Email: [your.email@example.com]
GitHub: [your-github-username]
```

**Step 7: Stage files**
```bash
git add .
```

**Step 8: Create first commit**
```bash
git commit -m "Week 01: Initialize LeafGuard AI project with proposal, architecture docs, and planning materials

- Add project proposal and architecture documentation
- Create system architecture and data flow diagrams
- Complete syllabus mapping showing CSE 2206 coverage
- Analyze 5 senior repositories for best practices
- Set up Git repository with proper structure
- Create evidence collection framework for 12-week timeline"
```

**Step 9: Verify commit**
```bash
git log
git status
```

### Expected Output

**Commands executed successfully:**
```
$ git init
Initialized empty Git repository in /path/to/LeafGuard-AI/.git/

$ git add .
$ git commit -m "Week 01: Initialize LeafGuard AI project..."
[main (root-commit) a1b2c3d] Week 01: Initialize LeafGuard AI project...
 15 files changed, 2847 insertions(+)
 create mode 100644 .gitignore
 create mode 100644 README.md
 create mode 100644 docs/proposal.md
 ...

$ git log
commit a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0
Author: Your Name <your.email@example.com>
Date:   [Current Date]

    Week 01: Initialize LeafGuard AI project with proposal, architecture docs, and planning materials

    - Add project proposal and architecture documentation
    - Create system architecture and data flow diagrams
    ...
```

**Screenshots to save:**
1. Terminal showing `git log` output
2. Folder structure in file explorer
3. Git graph showing initial commit

### Verification Checklist

- [ ] Git repository initialized (`git init` successful)
- [ ] All folders created (docs/, diagrams/, evidence/)
- [ ] .gitignore file excludes temporary files
- [ ] README.md has complete project overview
- [ ] All documents from previous exercises added
- [ ] First commit made with descriptive message
- [ ] `git status` shows "working tree clean"
- [ ] Can view commit history with `git log`

---

## Exercise 7: 12-Week Timeline Creation

### Objective
Create a realistic, detailed timeline for LeafGuard AI development with weekly milestones and buffer time.

### Task
Plan all 12 weeks with specific deliverables, evidence collection points, and realistic time estimates.

### Requirements

**For each week (Week 01 through Week 12), document:**

1. **Week Title:** Descriptive focus area (e.g., "Week 03: Camera & Gallery Integration")

2. **Learning Objectives:** What you will learn (3-5 points)

3. **Deliverables:** What you will build (3-5 specific items)

4. **Evidence to Collect:**
   - Screenshots (what to capture)
   - Git commits (how many, what features)
   - Videos (what demos to record)
   - Documentation (what to write)

5. **Estimated Hours:** Realistic time estimate (8-15 hours per week)

6. **Dependencies:** What must be complete before starting this week

7. **Risks:** Potential challenges and mitigation strategies

**Major Milestones:**
- Milestone 1 (End of Week 4): Backend and basic Android UI complete
- Milestone 2 (End of Week 8): All features implemented
- Milestone 3 (End of Week 12): Final submission ready

### Expected Output

**File:** `12-week-timeline.md`

**Format:**
```markdown
# LeafGuard AI - 12-Week Development Timeline

## Overview

**Total Duration:** 12 weeks (January 2024 - March 2024)
**Estimated Total Hours:** 120-150 hours
**Methodology:** Agile with 1-week sprints

---

## Week 01: Project Understanding & Planning

**Focus:** Establish project foundation and complete planning

### Learning Objectives
1. Understand three-tier architecture and MVVM pattern
2. Map CSE 2206 syllabus topics to project components
3. Analyze senior Android projects for best practices
4. Learn Git version control fundamentals
5. Practice formal academic writing

### Deliverables
1. ✅ Project proposal document (8-10 pages)
2. ✅ System architecture diagram (high-level and detailed)
3. ✅ Syllabus mapping matrix (15+ topics)
4. ✅ Senior repository analysis (5 repositories)
5. ✅ Git repository initialized with proper structure
6. ✅ 12-week timeline (this document)

### Evidence to Collect
- **Screenshots:**
  - Architecture diagram
  - Folder structure
  - Git log showing first commit
- **Documents:**
  - Proposal PDF
  - Syllabus mapping
  - Analysis report
- **Git:**
  - Initial commit with all planning docs
  - Commit message following convention

### Estimated Hours: 12-15 hours
- Proposal writing: 4-5 hours
- Diagram creation: 2-3 hours
- Repository analysis: 3-4 hours
- Timeline planning: 2-3 hours

### Dependencies
- None (first week)

### Risks & Mitigation
- **Risk:** Scope too large, unrealistic timeline
- **Mitigation:** Reviewed with instructor, added buffer time

---

## Week 02: Android Basics & UI Development

**Focus:** Set up Android Studio, create activities, design layouts

### Learning Objectives
1. Install and configure Android Studio
2. Understand Android project structure (manifests, res/, java/)
3. Create activities and implement lifecycle methods
4. Design XML layouts with Material Design components
5. Implement navigation with Intents

### Deliverables
1. Android Studio project created ("LeafGuard")
2. 6 activities created with basic layouts:
   - MainActivity (home screen)
   - ScanActivity (image capture)
   - ResultActivity (display result)
   - HistoryActivity (scan list)
   - DiseaseLibraryActivity (disease browser)
   - SettingsActivity (preferences)
3. Navigation between activities working
4. Material Design theme applied
5. App icon and launcher activity configured

### Evidence to Collect
- **Screenshots:**
  - Android Studio project structure
  - Each activity's layout in design mode
  - App running on emulator showing all screens
  - Navigation flow (home → scan → result)
- **Videos:**
  - 1-minute walkthrough of empty app
- **Git:**
  - Commits for: project creation, each activity, layouts, navigation
  - Minimum 5 commits this week
- **APK:**
  - Debug APK file (app-debug.apk)

### Estimated Hours: 10-12 hours
- Android Studio setup: 1-2 hours
- Learning Android basics: 2-3 hours
- Creating activities: 2-3 hours
- Designing layouts: 3-4 hours
- Testing and debugging: 2 hours

### Dependencies
- Week 01 complete (architecture understanding)
- Android Studio installed

### Risks & Mitigation
- **Risk:** Android Studio installation issues
- **Mitigation:** Install beforehand, use college lab if needed
- **Risk:** XML layout complexity
- **Mitigation:** Use ConstraintLayout, follow Material Design templates

---

## Week 03: Camera & Gallery Integration

[Continue for Week 03...]

---

## Week 04: FastAPI Backend Development

[Continue for Week 04...]

---

[Continue for all 12 weeks...]

---

## Major Milestones

### Milestone 1: End of Week 4
**Deliverable:** Backend API running, basic Android UI navigable
**Verification:** Can open app, navigate between screens, backend responds to test requests
**Evidence:** Screenshots of working UI, Postman tests of backend

### Milestone 2: End of Week 8
**Deliverable:** All features implemented (camera, API, database, XML)
**Verification:** Can scan a leaf, get prediction, save to history, view disease info
**Evidence:** Video demo of complete flow, Git history showing progressive commits

### Milestone 3: End of Week 12
**Deliverable:** Final submission package ready
**Verification:** App runs without crashes, all documentation complete, presentation ready
**Evidence:** Final APK, complete report, presentation slides, demo video

---

## Buffer & Contingency Planning

### Week-by-Week Buffer
- Each week includes 2-3 hours of buffer for debugging and unexpected issues
- If ahead of schedule, use time to improve UI or add polish

### Contingency Plans

**If backend development takes longer than Week 4:**
- Use mock data in Android app
- Continue with database and XML parsing
- Finish backend in Week 5, integrate in Week 5

**If TensorFlow Lite integration is difficult (Week 9):**
- Focus on cloud AI mode first
- TFLite becomes "future enhancement"
- Demonstrate understanding with documentation

**If behind schedule by Week 10:**
- Prioritize core features (scan, predict, history)
- Move nice-to-have features (notifications, location) to "future work"
- Ensure syllabus coverage is complete

---

## Time Allocation Summary

| Week | Focus Area | Hours | Cumulative |
|------|-----------|--------|------------|
| 01 | Project Planning | 12-15 | 15 |
| 02 | Android UI | 10-12 | 27 |
| 03 | Camera Integration | 10-12 | 39 |
| 04 | Backend Development | 12-15 | 54 |
| 05 | Retrofit Networking | 10-12 | 66 |
| 06 | ML Model Integration | 12-15 | 81 |
| 07 | Room Database | 10-12 | 93 |
| 08 | XML Parsing | 8-10 | 103 |
| 09 | TensorFlow Lite | 12-15 | 118 |
| 10 | Additional Features | 8-10 | 128 |
| 11 | Testing & Debugging | 10-12 | 140 |
| 12 | Final Submission | 10-12 | 152 |
| **Total** | | **134-152 hours** | |

**Average per week:** 11-13 hours (manageable alongside other coursework)
```

### Verification

Does your timeline:
- [ ] Cover all 12 weeks with specific details?
- [ ] Include realistic time estimates?
- [ ] Have clear deliverables for each week?
- [ ] Specify evidence to collect?
- [ ] Include buffer time for debugging?
- [ ] Have contingency plans for delays?
- [ ] Align with course semester dates?

---

## Exercise Completion Checklist

Complete all exercises and check off:

- [ ] **Exercise 1:** Data Flow Diagram created and saved
- [ ] **Exercise 2:** Syllabus Mapping Matrix completed (15+ topics)
- [ ] **Exercise 3:** Architecture Diagram drawn professionally
- [ ] **Exercise 4:** Senior Repository Analysis written (5 repos, 2000+ words)
- [ ] **Exercise 5:** Project Proposal Abstract written (200-250 words)
- [ ] **Exercise 6:** Git Repository initialized and first commit made
- [ ] **Exercise 7:** 12-Week Timeline created with all details

### All outputs saved in `evidence/week-01/exercises/`:
- [ ] `data-flow-diagram.png`
- [ ] `syllabus-mapping.md`
- [ ] `architecture-diagram.png` (+ source file)
- [ ] `senior-repo-analysis.md`
- [ ] `proposal-abstract.txt`
- [ ] Git repository with first commit
- [ ] `12-week-timeline.md`

---

## Next Steps

Once all exercises are complete:
1. Review your work for completeness and quality
2. Show architecture diagram to instructor/senior for feedback
3. Verify all files are committed to Git
4. Take screenshots of completed work
5. Proceed to Week 01 Build Task (if not yet done)
6. Prepare for Week 02: Install Android Studio, review Java/Kotlin basics

**Completion target:** End of Week 01 (allow 12-15 hours total for all exercises)

**Remember:** These exercises build foundational understanding. Do not skip or rush. Quality over speed.
