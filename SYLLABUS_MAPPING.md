# CSE 2206 Syllabus Mapping to LeafGuard AI

## Complete Topic Coverage Table

This document provides explicit mapping of **every single CSE 2206 syllabus topic** to specific weeks and features in the LeafGuard AI project.

| # | CSE 2206 Syllabus Topic | LeafGuard AI Feature | Week(s) | Evidence Location | Validation Method |
|---|------------------------|---------------------|---------|-------------------|-------------------|
| 1 | Introduction to mobile application development | Complete native Android app from scratch | 01-12 | Final APK, source code | Working app demo |
| 2 | Platform comparison (Android / iPhone / Windows Phone) | Platform analysis section in proposal and report | 01, 12 | docs/proposal, final report | Written comparison |
| 3 | Development environment setup | Android Studio, SDK, Gradle, emulator setup | 02 | Installation screenshots | Build succeeds |
| 4 | Development environment setup (Backend) | Python, FastAPI, venv, pip setup | 04 | Backend running screenshot | API responds |
| 5 | Java for Android development | Entire Android codebase written in Java | 02-12 | All .java files in app/src/ | Code compiles |
| 6 | Mobile programming language basics | Variables, methods, classes, OOP in Android context | 02-12 | MainActivity.java, utility classes | Feature works |
| 7 | Designing mobile applications | Multi-screen app design, UX flow planning | 02-03 | UI mockups, navigation flow | Screens exist |
| 8 | Building mobile applications | Implementation of all screens and features | 02-12 | Complete app source code | APK builds |
| 9 | User interface layouts | XML layouts for all screens | 02-03 | layout/ folder with XML files | UI renders |
| 10 | Activities and navigation | MainActivity, ResultActivity, HistoryActivity navigation | 02-03 | Activity classes, Intent code | Navigation works |
| 11 | XML parsing | Parse disease_library.xml file on-device | 08 | XmlPullParser implementation, XML file | Data displays |
| 12 | HTTP GET requests | Optional: GET disease info from API | 05, 08 | GET endpoint call (optional) | Response received |
| 13 | HTTP POST requests | POST multipart image upload to FastAPI | 05 | Retrofit POST call with image | Upload succeeds |
| 14 | JSON parsing | Parse prediction response JSON | 05 | Gson/JSON parsing code | Data extracted |
| 15 | RESTful API communication | Android ↔ FastAPI REST API | 05-06 | ApiService interface, endpoints | API communicates |
| 16 | App-to-app communication | Android Share Intent for scan results | 10 | Share button, Intent.ACTION_SEND | Share dialog opens |
| 17 | Android Intents (general) | Camera intent, gallery intent, share intent | 03, 10 | Multiple Intent usages | Intents work |
| 18 | Notifications | Reminder notifications with NotificationChannel | 10 | NotificationManager code | Notification appears |
| 19 | Notification channels | Create notification channel for reminders | 10 | NotificationChannel creation | Channel exists |
| 20 | PendingIntent | Open app from notification | 10 | PendingIntent with activity | Tap opens app |
| 21 | Maps and location-based applications | Optional: GPS location tagging for scans | 10 | LocationManager code attempt | Location captured or documented |
| 22 | Location services | Request location permissions, access GPS | 10 | Location permission request | Permission granted |
| 23 | Multimedia - Camera | Camera intent to capture leaf images | 03 | ACTION_IMAGE_CAPTURE intent | Camera opens |
| 24 | Multimedia - Gallery | Gallery picker to select existing images | 03 | ACTION_PICK intent | Gallery opens |
| 25 | Image handling | Bitmap loading, resizing, display | 03 | BitmapFactory, ImageView code | Image displays |
| 26 | Files and storage - Database | Room/SQLite database for scan history | 07 | Entity, DAO, Database classes | Data persists |
| 27 | Files and storage - File system | Save images to app directory | 03, 07 | File I/O code | Images saved |
| 28 | Files and storage - Assets | disease_library.xml in assets folder | 08 | assets/disease_library.xml | File accessible |
| 29 | SharedPreferences | Optional: Store user settings | 02, 10 | SharedPreferences code | Settings persist |
| 30 | SQLite database | Room abstraction over SQLite | 07 | Room database implementation | Queries work |
| 31 | Database operations (CRUD) | Insert, query, delete scan records | 07 | DAO methods: @Insert, @Query, @Delete | CRUD works |
| 32 | RecyclerView / ListView | Scan history list display | 07 | RecyclerView adapter code | List displays |
| 33 | Network programming basics | HTTP client, request/response cycle | 05 | Retrofit/OkHttp setup | Network call succeeds |
| 34 | Handling network responses | Success and error callbacks | 05 | onResponse, onFailure handling | Errors handled |
| 35 | Asynchronous programming | Network calls on background thread | 05 | Retrofit async calls | UI doesn't freeze |
| 36 | Permissions handling | Runtime permissions for camera, location | 03, 10 | Permission request code | Permissions work |
| 37 | Permission request flow | Check, request, handle grant/deny | 03 | ActivityCompat.requestPermissions | Flow complete |
| 38 | Debugging mobile apps | Logcat logs, breakpoints, error traces | 11 | Log.d/e statements throughout | Logs captured |
| 39 | Testing mobile apps | Test case table with pass/fail results | 11 | Test cases document | Tests documented |
| 40 | Manual testing | Feature testing checklist | 11 | Validation checklists | Features tested |
| 41 | Error handling | Try-catch blocks, null checks, error toasts | 05, 11 | Exception handling code | Errors don't crash |
| 42 | Edge case handling | Network down, permission denied, invalid image | 11 | Edge case test results | Edge cases handled |
| 43 | Application deployment | Build release APK | 12 | Build > Generate Signed Bundle/APK | APK file exists |
| 44 | APK installation | Install APK on physical device or emulator | 12 | adb install command or manual install | App installed |
| 45 | Application lifecycle | onCreate, onResume, onPause, onDestroy | 02-12 | Activity lifecycle methods | State managed |
| 46 | Background tasks | API calls, database operations off main thread | 05, 07 | Background execution code | UI responsive |
| 47 | Resource management | strings.xml, colors.xml, drawable resources | 02-03 | res/ folder organization | Resources used |
| 48 | Gradle build system | build.gradle dependencies, build variants | 02-12 | build.gradle (Module: app) | Project builds |
| 49 | Third-party libraries | Retrofit, Room, Gson, TensorFlow Lite | 05-09 | Gradle dependencies | Libraries integrated |
| 50 | Machine Learning integration (Android) | TensorFlow Lite on-device inference | 09 | TFLite model loading and inference | Offline mode works |
| 51 | Backend API development | FastAPI backend with /predict endpoint | 04-06 | Python FastAPI code | Backend serves requests |
| 52 | Multipart file upload | Handle image upload in FastAPI | 04-05 | FastAPI File parameter | Upload received |
| 53 | Model inference | Load ML model, preprocess, predict, return result | 06 | Model loading and prediction code | Predictions returned |
| 54 | Data serialization | JSON request/response format | 05-06 | JSON schema design | Data transfers correctly |
| 55 | Local XML data storage | Store disease information in XML format | 08 | disease_library.xml structure | XML valid |
| 56 | UI/UX best practices | Material Design components, user-friendly flow | 02-03, 12 | UI screenshots | UI looks professional |
| 57 | Code organization | Packages, separation of concerns | 02-12 | Package structure | Code organized |
| 58 | Version control with Git | GitHub repository, commits, branches | 01-12 | Git commit history | Commits consistent |
| 59 | Documentation | README, code comments, report | 01-12 | README.md, final report | Documentation complete |
| 60 | Project presentation | Demo video, slides, viva | 12 | Presentation slides, video | Demo ready |

## Detailed Week-by-Week Mapping

### Week 01: Project Understanding and Proposal
**Syllabus Topics Covered:**
- Introduction to mobile application development
- Platform comparison (Android / iPhone / Windows Phone)
- Version control with Git

**How Covered:**
- Write project proposal explaining LeafGuard AI as Android app
- Research and document Android vs iOS vs Windows Phone
- Set up GitHub repository
- Analyze senior Android project repositories

**Evidence:**
- `docs/proposal.md` completed
- Platform comparison section in proposal
- Git repository initialized with meaningful commits

---

### Week 02: Android Basics and UI Skeleton
**Syllabus Topics Covered:**
- Development environment setup (Android Studio, SDK, Gradle)
- Java for Android development
- Designing mobile applications
- Building mobile applications
- User interface layouts (XML)
- Activities and navigation
- Application lifecycle
- Gradle build system
- Resource management

**How Covered:**
- Install Android Studio, SDK, create new project
- Design app navigation flow and UI mockups
- Create XML layouts for Home, Result, History, Settings screens
- Implement Activities with basic navigation using Intents
- Set up strings.xml, colors.xml resources
- Configure build.gradle with dependencies

**Evidence:**
- Android Studio screenshot showing project structure
- XML layout files in `app/src/main/res/layout/`
- Multiple Activity classes created
- App runs and shows UI screens (screenshot)
- Navigation between screens works (video)

---

### Week 03: Camera and Gallery
**Syllabus Topics Covered:**
- Multimedia - Camera
- Multimedia - Gallery
- Image handling
- Files and storage - File system
- Android Intents (camera, gallery)
- Permissions handling
- Permission request flow

**How Covered:**
- Implement camera intent (ACTION_IMAGE_CAPTURE)
- Implement gallery picker intent (ACTION_PICK)
- Handle image URI and convert to Bitmap
- Resize images for ML model input size
- Request and handle CAMERA, READ_EXTERNAL_STORAGE permissions
- Save captured/selected images to app storage

**Evidence:**
- Camera opens when button tapped (screenshot)
- Gallery opens when button tapped (screenshot)
- Selected image appears in ImageView (screenshot)
- Permission dialog appears (screenshot)
- Logcat shows permission grant/deny (log snippet)

---

### Week 04: FastAPI Backend
**Syllabus Topics Covered:**
- Development environment setup (Python, FastAPI)
- Backend API development
- Multipart file upload
- RESTful API basics

**How Covered:**
- Install Python 3.8+, create virtual environment
- Install FastAPI, Uvicorn with pip
- Create `/predict` POST endpoint accepting multipart image file
- Return dummy JSON prediction response
- Test with Postman or browser
- Run backend on local network IP

**Evidence:**
- `backend-api/main.py` with FastAPI code
- `requirements.txt` with dependencies
- Terminal screenshot showing Uvicorn running
- Postman screenshot showing successful POST request
- JSON response received (screenshot)

---

### Week 05: Android Networking
**Syllabus Topics Covered:**
- HTTP POST requests
- JSON parsing
- RESTful API communication
- Network programming basics
- Handling network responses
- Asynchronous programming
- Error handling

**How Covered:**
- Add Retrofit and Gson dependencies to build.gradle
- Create ApiService interface with @POST endpoint
- Create RetrofitClient with base URL and Gson converter
- Implement image upload with multipart form data
- Parse JSON response and extract prediction data
- Handle success (onResponse) and failure (onFailure) callbacks
- Show loading indicator during network call
- Handle network errors with user-friendly messages

**Evidence:**
- `ApiService.java` and `RetrofitClient.java` files
- Screenshot showing loading state
- Screenshot showing prediction result from backend
- Logcat showing request and response (log snippet)
- Screenshot showing error message when backend is down

---

### Week 06: Cloud ML Model Integration
**Syllabus Topics Covered:**
- Model inference
- Data serialization
- Image preprocessing
- Backend enhancement

**How Covered:**
- Find or train plant disease CNN model
- Load model in FastAPI backend (TensorFlow or PyTorch)
- Implement image preprocessing (resize, normalize)
- Map model output to disease labels
- Return structured JSON with disease name, confidence, symptoms, treatment
- Test with real leaf images

**Evidence:**
- Model file in `model/` folder
- Backend code showing model loading and inference
- Sample images in `sample-images/`
- Screenshot showing real prediction result
- Confidence percentage displayed in app

---

### Week 07: Room Database and Scan History
**Syllabus Topics Covered:**
- Files and storage - Database
- SQLite database (via Room)
- Database operations (CRUD)
- RecyclerView / ListView
- Background tasks

**How Covered:**
- Add Room dependencies to build.gradle
- Create ScanEntity with fields: id, imagePath, diseaseName, confidence, timestamp
- Create ScanDao with @Insert, @Query (getAll, getById), @Delete
- Create AppDatabase class
- Implement HistoryActivity with RecyclerView
- Display list of past scans
- Implement delete functionality
- Implement detail view for individual scan

**Evidence:**
- Entity, DAO, Database classes in code
- Screenshot showing history list with multiple scans
- Screenshot showing individual scan details
- Screenshot showing delete confirmation
- Database inspector screenshot (Android Studio)

---

### Week 08: XML Disease Library
**Syllabus Topics Covered:**
- XML parsing
- Files and storage - Assets
- Local XML data storage

**How Covered:**
- Create `disease_library.xml` in `assets/` folder
- Structure XML with disease nodes containing name, symptoms, treatment, prevention
- Implement XmlPullParser to read and parse XML file
- Map disease labels to XML entries
- Create Disease Library screen displaying all diseases
- Show detailed disease information when user taps a disease

**Evidence:**
- `assets/disease_library.xml` file
- XML parsing code in Java
- Screenshot showing disease library list
- Screenshot showing disease detail view
- Logcat showing parsed data (log snippet)

---

### Week 09: TensorFlow Lite Offline AI
**Syllabus Topics Covered:**
- Machine Learning integration (Android)
- Local model inference
- On-device processing
- Third-party libraries (TensorFlow Lite)

**How Covered:**
- Convert TensorFlow/PyTorch model to TFLite format
- Add TensorFlow Lite dependencies to build.gradle
- Copy .tflite model and labels.txt to `assets/`
- Load model using TFLite Interpreter
- Implement on-device inference with image preprocessing
- Create mode selector (Cloud AI vs On-Device AI)
- Implement fallback: if TFLite fails, document and keep cloud mode

**Evidence:**
- `.tflite` file in `assets/`
- `labels.txt` in `assets/`
- TFLite loading and inference code
- Screenshot showing mode selector
- Screenshot showing offline prediction working
- Comparison: cloud result vs offline result

---

### Week 10: Notifications, Share, and Location
**Syllabus Topics Covered:**
- App-to-app communication
- Android Intents (share)
- Notifications
- Notification channels
- PendingIntent
- Maps and location-based applications
- Location services

**How Covered:**
- Implement Share button using Intent.ACTION_SEND
- Share scan result as text or image
- Create NotificationChannel for reminders
- Schedule notification using AlarmManager
- Implement PendingIntent to open app from notification
- Request location permissions
- Attempt to tag scan with GPS coordinates
- If location feature is too difficult, document attempt and limitations

**Evidence:**
- Screenshot showing Android share dialog
- Screenshot showing notification in notification panel
- Screenshot showing app opening from notification tap
- Location permission request screenshot
- GPS coordinates shown in scan detail (or documented failure)

---

### Week 11: Testing, Debugging, and Performance
**Syllabus Topics Covered:**
- Debugging mobile apps
- Testing mobile apps
- Manual testing
- Error handling
- Edge case handling
- Performance measurement

**How Covered:**
- Create comprehensive test case table (30+ test cases)
- Test all features systematically
- Test edge cases: no internet, permission denied, invalid image, empty database
- Reproduce and fix at least 5 bugs
- Document debugging process with logs
- Implement cloud vs on-device comparison screen
- Measure and display inference latency for both modes
- Ensure all error messages are user-friendly

**Evidence:**
- `validation/test-cases-complete.md` with test results
- Debug log document showing bugs found and fixed
- Screenshot showing error handling for each edge case
- Screenshot showing comparison screen with latency metrics
- Logcat showing error traces (before and after fixes)

---

### Week 12: Final Submission
**Syllabus Topics Covered:**
- Application deployment
- APK installation
- UI/UX best practices
- Code organization
- Documentation
- Project presentation

**How Covered:**
- Add app icon and splash screen
- Polish UI with consistent colors and spacing
- Organize code into clean packages
- Write comprehensive README with setup instructions
- Complete final report with all sections
- Create presentation slides (12-15 slides)
- Write demo video script
- Build release APK (signed)
- Install APK on device
- Record demo video
- Prepare viva Q&A

**Evidence:**
- app-release.apk file
- Screenshot showing app icon on device
- Polished UI screenshots for report
- Final report PDF
- Presentation slides PDF/PPTX
- Demo video file
- Complete GitHub repository

---

## Syllabus Coverage Verification Checklist

Use this checklist to verify you have covered every syllabus topic:

### Platform and Environment (3 topics)
- [ ] Introduction to mobile app development → Native Android app
- [ ] Platform comparison → Android vs iOS written analysis
- [ ] Development environment setup → Android Studio + FastAPI installed

### Programming Fundamentals (3 topics)
- [ ] Java for Android → All code in Java
- [ ] Mobile language basics → Variables, methods, classes used
- [ ] Code organization → Clean package structure

### UI and Design (4 topics)
- [ ] Designing applications → UI mockups and flow designed
- [ ] Building applications → Full app implemented
- [ ] User interface layouts → XML layouts created
- [ ] Activities and navigation → Multiple activities with intents

### Data Handling (7 topics)
- [ ] XML parsing → disease_library.xml parsed
- [ ] Files - Database → Room database implemented
- [ ] Files - File system → Images saved to storage
- [ ] Files - Assets → XML file in assets accessed
- [ ] SQLite database → Room over SQLite used
- [ ] Database CRUD → Insert, query, delete implemented
- [ ] RecyclerView/ListView → History list displayed

### Networking (6 topics)
- [ ] HTTP POST → Image uploaded to backend
- [ ] HTTP GET → Optional disease info retrieval
- [ ] JSON parsing → Prediction response parsed
- [ ] RESTful API → Android ↔ FastAPI communication
- [ ] Network programming → Retrofit/OkHttp used
- [ ] Async programming → Network calls on background thread

### System Integration (7 topics)
- [ ] App-to-app communication → Share intent implemented
- [ ] Intents → Camera, gallery, share intents used
- [ ] Notifications → Reminder notification created
- [ ] Notification channels → Channel created
- [ ] PendingIntent → Open app from notification
- [ ] Maps and location → Location tagging attempted
- [ ] Location services → GPS access requested

### Multimedia (3 topics)
- [ ] Camera → Camera intent implemented
- [ ] Gallery → Gallery picker implemented
- [ ] Image handling → Bitmap load, resize, display

### Permissions (2 topics)
- [ ] Permissions handling → Runtime permissions implemented
- [ ] Permission request flow → Check, request, handle flow

### Quality Assurance (4 topics)
- [ ] Debugging → Logcat, error traces used
- [ ] Testing → Test case table created
- [ ] Error handling → Try-catch, null checks added
- [ ] Edge cases → Network down, invalid input handled

### Deployment (2 topics)
- [ ] Application deployment → APK built
- [ ] APK installation → APK installed on device

### Advanced Topics (4 topics)
- [ ] ML integration → TensorFlow Lite implemented
- [ ] Backend API → FastAPI backend created
- [ ] Third-party libraries → Retrofit, Room, TFLite used
- [ ] Gradle build system → Dependencies managed

### Professional Skills (3 topics)
- [ ] Version control → Git commits throughout
- [ ] Documentation → README, report, comments
- [ ] Presentation → Demo video, slides, viva prep

---

## How to Use This Mapping

### During Development
1. Each week, check which syllabus topics are covered
2. Ensure your implementation clearly demonstrates those topics
3. Save evidence (screenshots, code snippets, logs)
4. Document the connection in your weekly reflection

### For Final Report
1. Include this mapping table in your report
2. For each topic, provide:
   - Brief description of the syllabus requirement
   - How your app implements it
   - Evidence (screenshot + code location)

### For Viva Preparation
1. Review each topic mapping
2. Practice explaining: "For [topic], I implemented [feature] using [technology]"
3. Be ready to show code and demo the feature live

### For Evidence Collection
1. Use this table as a checklist
2. Ensure you have at least one screenshot or video for each topic
3. Store in `docs/evidence/` organized by week

---

**This mapping ensures your LeafGuard AI project satisfies 100% of CSE 2206 syllabus requirements.**

**Next: Read `PROJECT_ARCHITECTURE.md` to understand system design.**
