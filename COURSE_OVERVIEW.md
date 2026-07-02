# CSE 2206 - Mobile Application Development Course Overview

## What Is Mobile App Development? (Plain-Language Opener)

Mobile app development means building the programs ("apps") that run on smartphones and tablets. In this course you build a **native Android app** — an app written specifically for Android phones using their official tools — that takes a photo of a plant leaf and reports possible diseases. You learn the whole journey: designing screens, using the camera, saving data, talking to a server over the internet, and packaging the finished app so it can be installed on a real phone.

> **Language track:** This course is **Kotlin-first with a parallel Java track**. Kotlin is Google's recommended language for new Android apps and is the primary track ([`android-app-kotlin/`](android-app-kotlin/)); an identical Java twin ([`android-app/`](android-app/)) is provided for the traditional "Java for Android" framing. If unsure, choose Kotlin.

## Course Context

**Course Code**: CSE 2206
**Course Title**: Mobile Application Development
**Credit Hours**: 3.0
**Prerequisites**: Basic programming knowledge (Java/C++/Python), data structures
**Target Audience**: Computer Science and Engineering students

## What This Course Expects

CSE 2206 is a comprehensive mobile application development course that prepares students to build complete, functional mobile applications. The course focuses on **native Android development** while covering the full spectrum of mobile app engineering: user interface design, data persistence, networking, multimedia, testing, and deployment.

### Core Learning Objectives

By the end of this course, students should be able to:

1. **Develop Native Android Applications**
   - Create multi-screen applications with proper navigation
   - Design user interfaces using XML layouts
   - Handle user input and application state
   - Implement Material Design principles

2. **Integrate System Features**
   - Access device camera and photo gallery
   - Request and handle runtime permissions
   - Store data locally using SQLite/Room database
   - Work with device file systems

3. **Network Communication**
   - Make HTTP requests (GET and POST)
   - Parse JSON responses
   - Upload files (multipart form data)
   - Handle network errors gracefully

4. **Inter-App Communication**
   - Implement Android Intents
   - Share content between applications
   - Launch system apps (camera, gallery, etc.)

5. **Parse and Process Data**
   - Parse XML files stored locally
   - Read and write to local data stores
   - Process and validate user input

6. **Implement Notifications**
   - Create notification channels
   - Schedule notifications
   - Handle notification actions

7. **Work with Location Services** (Optional but recommended)
   - Request location permissions
   - Access device GPS
   - Tag data with location information

8. **Test and Debug Applications**
   - Use Android Studio debugging tools
   - Write and execute test cases
   - Log application behavior
   - Handle edge cases and errors

9. **Deploy Applications**
   - Build release APK files
   - Install apps on physical devices
   - Prepare apps for distribution

## How LeafGuard AI Satisfies Course Requirements

LeafGuard AI is designed as the **perfect CSE 2206 course project** because it integrates every single course topic into one cohesive, practical application.

### Topic Alignment

| CSE 2206 Topic | LeafGuard AI Implementation | Evidence Location |
|----------------|----------------------------|-------------------|
| **Introduction to Mobile Application Development** | Complete native Android app built from scratch | Week 01, Final APK |
| **Platform Comparison (Android/iOS/Windows Phone)** | Report section comparing platforms for plant disease detection apps | Week 01 proposal, Final report |
| **Development Environment Setup** | Android Studio, SDK, Gradle, AVD setup; Python FastAPI backend setup | Week 02 |
| **Java for Android Development** | Android codebase written primarily in Kotlin, with a behavior-identical Java twin | Weeks 02-12 |
| **Designing and Building Applications** | Multi-screen UI: Home, Camera, Result, History, Settings, Disease Library | Weeks 02-03 |
| **XML Parsing** | Parse local `diseases.xml` file (in `assets/`) to display disease information | Week 08 |
| **HTTP POST/GET Requests** | POST image to FastAPI backend; receive JSON prediction response | Week 05 |
| **App-to-App Communication** | Share scan results using Android Share Intent | Week 10 |
| **Notifications** | Reminder notifications using NotificationChannel and PendingIntent | Week 10 |
| **Maps and Location-Based Applications** | Optional: Tag scans with GPS location | Week 10 |
| **Multimedia** | Camera intent for image capture; Gallery picker for image selection | Week 03 |
| **Files and Storage** | Room/SQLite database for scan history; Save images locally | Week 07 |
| **Network Programming** | Retrofit/OkHttp HTTP client; REST API communication | Week 05 |
| **Debugging** | Logging, error handling, edge case testing | Week 11 |
| **Testing** | Test case table with expected/actual/pass-fail results | Week 11 |
| **Deployment** | Build release APK; Install on device; Demo to instructor | Week 12 |

### Why This Project is Course-Appropriate

**1. Covers Every Syllabus Topic**
- No syllabus topic is left out
- Each feature directly maps to one or more course topics
- Clear evidence trail for each requirement

**2. Appropriate Complexity**
- Not too simple (just a calculator or to-do list)
- Not too complex (not enterprise-level)
- Achievable in 12 weeks with consistent effort

**3. Real-World Application**
- Solves a genuine problem (plant disease identification)
- Modern tech stack (AI/ML integration)
- Demonstrates practical mobile engineering skills

**4. Clear Demonstration Value**
- Easy to demo in 5-10 minutes
- Visible features teachers can verify
- Screenshots and videos provide evidence

**5. Academic Report Friendly**
- Rich technical content for reports
- Architecture diagrams are straightforward
- Comparison opportunities (cloud vs on-device AI)

## Course Assessment Criteria

Based on typical CSE 2206 assessment patterns, your project will likely be evaluated on:

### 1. Functionality (40-50%)
- Does the app work as described?
- Are all required features implemented?
- Does it handle errors gracefully?

**LeafGuard AI addresses this through:**
- Comprehensive validation checklists each week
- Test case tables in Week 11
- Evidence collection (screenshots, videos, logs)

### 2. Code Quality (20-30%)
- Is the code well-structured?
- Are naming conventions followed?
- Is the code readable and maintainable?

**LeafGuard AI addresses this through:**
- Weekly build tasks with best practices
- Code review exercises
- Clean architecture patterns (MVVM suggested but not mandatory)

### 3. Documentation (15-25%)
- Is there a complete project report?
- Are architecture diagrams clear?
- Is the README comprehensive?

**LeafGuard AI addresses this through:**
- Final report template in `docs/`
- Architecture diagram guide
- Weekly reflection journal

### 4. Demonstration (10-15%)
- Can you demo the app effectively?
- Can you explain your implementation?
- Can you answer technical questions?

**LeafGuard AI addresses this through:**
- Demo video script with exact talking points
- 60+ viva questions with suggested answers
- Teacher impression tips in every weekly README

### 5. Syllabus Coverage (Mandatory)
- Does the project satisfy all syllabus topics?
- Is there clear evidence for each topic?

**LeafGuard AI addresses this through:**
- SYLLABUS_MAPPING.md with explicit mappings
- Feature completion matrix
- Evidence folder with weekly screenshots

## Common Pitfalls in CSE 2206 Projects

### What Students Often Get Wrong

1. **Incomplete Syllabus Coverage**
   - Forgetting location/maps topic
   - Skipping XML parsing
   - No notifications implementation
   - Missing HTTP POST (only doing GET)

   **LeafGuard AI prevents this:** Every topic is explicitly assigned to a week with validation checkpoints.

2. **Weak Demonstration**
   - Can't explain how features work
   - No working demo on demo day
   - Backend doesn't work during presentation

   **LeafGuard AI prevents this:** Local backend strategy ensures demo reliability; viva prep in Week 12.

3. **Poor Documentation**
   - Missing architecture diagrams
   - No test cases documented
   - Incomplete final report

   **LeafGuard AI prevents this:** Templates provided; evidence collected weekly; final submission checklist.

4. **Overclaiming AI Capabilities**
   - Saying "95% accurate" without evidence
   - Claiming medical diagnostic capability
   - Not acknowledging limitations

   **LeafGuard AI prevents this:** Explicit instructions to focus on mobile engineering; limitations documented in Week 06.

5. **Code Without Understanding**
   - Copied from GitHub without modification
   - Can't explain key sections during viva
   - Uses libraries or patterns not covered in course

   **LeafGuard AI prevents this:** Learning rules enforce "explain before code"; weekly reflection prompts; minimal starter snippets only.

## Project Scope Boundaries

### What IS Required (Must Have)

- Native Android app in Kotlin (Java twin available)
- Camera and gallery image selection
- HTTP POST to backend API
- JSON parsing
- Local SQLite/Room database
- XML file parsing
- Android Share Intent
- Notifications
- Attempt at location feature (document if failed)
- Test case table
- Debug logs
- APK file
- Final report and presentation

### What is NOT Required (Nice to Have)

- Cloud deployment (AWS, GCP, Azure)
- User authentication system
- Real-time backend database
- Advanced ML model training
- iOS version
- Published on Google Play Store
- Complex animations
- Multiple language support

### What is OPTIONAL but Recommended

- TensorFlow Lite on-device AI (Week 09 provides fallback plan)
- Location tagging (Week 10 provides attempt strategy)
- Mode comparison screen (cloud vs offline)
- App icon and splash screen

## Success Metrics

### You Will Pass This Course Project If:

1. All syllabus topics have at least one corresponding feature
2. Your app builds without errors
3. Your app runs on an emulator or device
4. You can demo at least 80% of features
5. Your final report is complete
6. You can explain your code during viva

### You Will Excel (High Marks) If:

1. All features work flawlessly during demo
2. Code is clean and well-commented
3. Report includes detailed architecture and testing sections
4. You demonstrate deep understanding during viva
5. Optional features (TFLite, location) are implemented
6. Evidence is comprehensive (screenshots, videos, logs)

## Timeline Expectations

### Typical Course Duration: 12-15 weeks

- **Weeks 1-4**: Project selection, proposal, basic setup, UI design
- **Weeks 5-8**: Core feature implementation
- **Weeks 9-11**: Advanced features, testing, debugging
- **Week 12+**: Report writing, final demo preparation

### LeafGuard AI Timeline: Exact 12 weeks

This roadmap is specifically designed to fit a standard semester timeline with clear weekly milestones.

## Academic Integrity Reminder

### What is Allowed

- Reading official documentation
- Following tutorials to learn concepts
- Discussing approach with classmates
- Using standard libraries (Retrofit, Room, etc.)
- Getting help from instructors/TAs

### What is NOT Allowed

- Copying code from GitHub without understanding
- Submitting someone else's project as your own
- Having someone else write your code
- Using paid services to complete project
- Plagiarizing reports or documentation

### How LeafGuard AI Supports Integrity

- Provides learning path, not finished code
- Enforces "explain before code" rule
- Weekly reflection enforces understanding
- Minimal starter snippets only (never full solutions)

## Teacher Expectations

Based on CSE 2206 course patterns, your instructor expects:

### During Weekly Check-ins
- Show incremental progress
- Explain what you implemented that week
- Demonstrate working features
- Discuss challenges faced

### During Midterm Demo (Week 6-7)
- Show basic UI and navigation
- Demonstrate camera/gallery functionality
- Show network request working (even with dummy data)
- Present project architecture

### During Final Demo (Week 12)
- Complete app walkthrough (5-10 minutes)
- Show all major features working
- Explain technical decisions
- Handle Q&A about implementation

### In Final Report
- Clear problem statement
- System architecture with diagrams
- Technology stack justification
- Implementation details with code snippets
- Testing results with screenshots
- Conclusion and future work
- References

## Recommended Study Strategy

### Weekly Time Allocation

- **Reading/Theory**: 3-4 hours
- **Hands-on Exercises**: 4-6 hours
- **Build Task Implementation**: 6-8 hours
- **Testing/Debugging**: 2-3 hours
- **Documentation/Reflection**: 2-3 hours

**Total: 17-24 hours per week**

### Daily Schedule Suggestion

**Monday-Wednesday**: Theory, exercises, concept learning
**Thursday-Saturday**: Implementation, build tasks, debugging
**Sunday**: Validation, reflection, evidence collection, planning next week

## Final Advice

### Remember the Course Goal

CSE 2206 is not an AI/ML course. It is a **mobile application development** course. Your app demonstrates mobile engineering skills with AI as one integrated feature among many.

### The Viva Framing

When presenting to your instructor, always frame your project this way:

"I built a complete Android mobile application that satisfies every CSE 2206 syllabus requirement. The app includes camera integration, HTTP networking, XML parsing, local database storage, app-to-app sharing, notifications, testing, and deployment. I integrated AI capabilities in two modes: cloud inference using a REST API and offline inference using TensorFlow Lite."

This framing emphasizes **mobile engineering completeness** while positioning AI as an integrated feature, not the sole focus.

---

**Ready to start?** Read `LEARNING_RULES.md` next, then open `roadmap/week-01-project-understanding/README.md`.
