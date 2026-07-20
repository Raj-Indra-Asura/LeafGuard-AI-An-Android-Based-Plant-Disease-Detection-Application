# LeafGuard AI - 12-Week Progress Tracker

## How to Use This Tracker (Beginner Note)

New here? This file is your weekly checklist. Each week, tick `[x]` the boxes as you finish them and write the date in the `[____]` brackets. Work top to bottom — the weeks are in order. You do **not** need to finish everything in one sitting; update it a little each day. It doubles as evidence of steady progress for your instructor.

**Track:** These checkboxes assume the **Kotlin (primary/recommended) track** in `android-app-kotlin/`. If you chose the **Java** track, do the identical steps in `android-app/` instead — the file/class names are the same. A "track" is just the language you build in; if unsure, choose Kotlin.

## Instructions

- Check ✓ each item as you complete it
- Add completion dates in the brackets
- Update weekly as you progress
- This tracker helps you stay on schedule and provides evidence of consistent work

---

## Week 01: Product Idea and Learning Foundation [Start Date: ____ / End Date: ____]

**📈 Cumulative product target: 5%** (see [PRODUCT_PROGRESS_MAP.md](PRODUCT_PROGRESS_MAP.md)) — Cumulative % achieved: [____%]
**Track paths:** No Android track code is written this week. Kotlin (`android-app-kotlin/`) becomes the primary build track from Week 02.

### Learning Phase
- [ ] Read Week 01 README.md [____]
- [ ] Read Week 01 learning-notes.md [____]
- [ ] Skim COURSE_OVERVIEW.md for course context [____]
- [ ] Skim GLOSSARY.md for unfamiliar terms [____]
- [ ] Understand that Week 01 validates the product foundation, not the finished app [____]

### Exercises
- [ ] Exercise 1: Explain the product in five sentences [____]
- [ ] Exercise 2: Write the main user journey [____]
- [ ] Exercise 3: Make feature cards [____]
- [ ] Exercise 4: Draw/list rough screen map [____]
- [ ] Exercise 5: Draw box-level system sketch [____]
- [ ] Exercise 6: Make week growth table [____]

### Build Task
- [ ] Create `docs/evidence/week-01/product-idea.md` [____]
- [ ] Create `docs/evidence/week-01/user-journey.md` [____]
- [ ] Create `docs/evidence/week-01/screen-map.md` [____]
- [ ] Create `docs/evidence/week-01/system-sketch.md` or image [____]
- [ ] Create `docs/evidence/week-01/week-growth-map.md` [____]
- [ ] Create `docs/evidence/week-01/week-01-validation.md` [____]

### Validation
- [ ] Product idea is clear and honest [____]
- [ ] User journey can be explained in under 2 minutes [____]
- [ ] Screen map supports the journey [____]
- [ ] System sketch uses plain boxes, not final class-level architecture [____]
- [ ] Week growth map has a validation demo for each week [____]
- [ ] Milestone demo completed: idea -> journey -> screen map -> sketch -> growth map [____]

### Documentation
- [ ] Complete Week 01 reflection [____]
- [ ] Take quiz [____]
- [ ] Save evidence in docs/evidence/week-01/ [____]
- [ ] Git commit with message format [____]

---

## Week 02: Android UI Navigation Shell [Start Date: ____ / End Date: ____]

**📈 Cumulative product target: 15%** (see [PRODUCT_PROGRESS_MAP.md](PRODUCT_PROGRESS_MAP.md)) — Cumulative % achieved: [____%]
**Track paths:** Kotlin (primary) → `android-app-kotlin/` · Java (secondary/reference) → `android-app/`.

### Setup
- [ ] Install Android Studio (done in pre-Week-01 setup) [____]
- [ ] Open or create the Kotlin Android project in `android-app-kotlin/` [____]
- [ ] Confirm Gradle sync succeeds [____]
- [ ] Confirm emulator or physical device is available [____]
- [ ] Confirm the default app/build runs before adding screens [____]

### Exercises
- [ ] Exercise 1: Connect Week 01 screens to Android screens [____]
- [ ] Exercise 2: Explore Android project folders [____]
- [ ] Exercise 3: Practise string/color resources [____]
- [ ] Exercise 4: Build one simple layout [____]
- [ ] Exercise 5: Practise Intent navigation [____]
- [ ] Exercise 6: Plan Week 02 evidence [____]

### Build Task
- [ ] Create/update `strings.xml` with Week 02 UI text [____]
- [ ] Create/update `colors.xml` with named colors [____]
- [ ] Build Home screen layout with navigation buttons [____]
- [ ] Create Scan placeholder screen [____]
- [ ] Create Result placeholder screen [____]
- [ ] Create History placeholder screen [____]
- [ ] Create Disease Library placeholder screen [____]
- [ ] Create Settings/About placeholder screen [____]
- [ ] Register Activities in `AndroidManifest.xml` [____]
- [ ] Implement Home navigation using explicit Intents [____]
- [ ] Build and run on emulator/device [____]

### Validation
- [ ] App builds without errors [____]
- [ ] Home screen opens [____]
- [ ] All required placeholder screens open [____]
- [ ] Navigation works from Home and back safely [____]
- [ ] Placeholder text clearly defers future behavior [____]
- [ ] No camera/backend/database/XML/AI behavior is required or claimed [____]

### Documentation
- [ ] Complete Week 02 reflection [____]
- [ ] Take quiz [____]
- [ ] Save screenshots of Home and placeholder screens [____]
- [ ] Save build/run evidence in `docs/evidence/week-02/` [____]
- [ ] Meaningful git commits [____]

---

## Week 03: ScanActivity Image Input [Start Date: ____ / End Date: ____]

**📈 Cumulative product target: 25%** (see [PRODUCT_PROGRESS_MAP.md](PRODUCT_PROGRESS_MAP.md)) — Cumulative % achieved: [____%]
**Track paths:** Kotlin (primary) → `android-app-kotlin/` · Java (secondary/reference) → `android-app/`.

### Exercises
- [ ] Exercise 1: Trace camera and gallery image flows [____]
- [ ] Exercise 2: Explain camera permission [____]
- [ ] Exercise 3: Check FileProvider authority matching [____]
- [ ] Exercise 4: Plan selected image URI state [____]
- [ ] Exercise 5: Sketch Scan preview UI [____]
- [ ] Exercise 6: Prepare edge-case checklist [____]

### Build Task
- [ ] Add CAMERA permission and FileProvider declaration [____]
- [ ] Create `res/xml/file_provider_paths.xml` [____]
- [ ] Add Week 03 strings for image input and messages [____]
- [ ] Upgrade `activity_scan.xml` with preview, status, Take Photo, and Gallery buttons [____]
- [ ] Add `RequestPermission`, `TakePicture`, and `GetContent` launchers in `ScanActivity` [____]
- [ ] Create camera output URI with FileProvider [____]
- [ ] Store selected image URI [____]
- [ ] Display selected/captured image in ImageView [____]
- [ ] Handle permission denial and user cancellation safely [____]

### Validation
- [ ] Home still opens `ScanActivity` [____]
- [ ] Gallery picker opens and selected image previews [____]
- [ ] Camera permission flow works [____]
- [ ] Camera capture previews [____]
- [ ] Permission denial does not crash [____]
- [ ] Camera/gallery cancellation does not crash [____]
- [ ] No backend/database/AI behavior is required or faked [____]

### Documentation
- [ ] Complete Week 03 reflection [____]
- [ ] Take quiz [____]
- [ ] Save screenshots: Scan before image, gallery preview, camera permission, camera preview [____]
- [ ] Save cancellation/denial behavior notes [____]
- [ ] Save evidence in `docs/evidence/week-03/` [____]
- [ ] Meaningful git commits [____]

---

## Week 04: FastAPI Backend [Start Date: ____ / End Date: ____]

**📈 Cumulative product target: 35%** (see [PRODUCT_PROGRESS_MAP.md](PRODUCT_PROGRESS_MAP.md)) — Cumulative % achieved: [____%]
**Track paths:** Kotlin (primary) → `android-app-kotlin/` · Java (secondary) → `android-app/` — do the identical steps in whichever track you chose.

### Setup
- [ ] Install Python 3.8+ [____]
- [ ] Create virtual environment [____]
- [ ] Install FastAPI and Uvicorn [____]
- [ ] Create backend-api/ folder structure [____]

### Exercises
- [ ] Exercise 1: Create basic FastAPI app [____]
- [ ] Exercise 2: Add /predict endpoint [____]
- [ ] Exercise 3: Accept file upload [____]
- [ ] Exercise 4: Return JSON response [____]
- [ ] Exercise 5: Test with Postman [____]
- [ ] Exercise 6: Run on local network [____]

### Build Task
- [ ] Create main.py with FastAPI app [____]
- [ ] Implement /predict POST endpoint [____]
- [ ] Accept multipart file parameter [____]
- [ ] Return dummy JSON prediction [____]
- [ ] Create requirements.txt [____]
- [ ] Test with Postman/browser [____]
- [ ] Run on 0.0.0.0 to accept network requests [____]
- [ ] Find local IP address [____]
- [ ] Test from phone browser [____]

### Validation
- [ ] FastAPI server starts successfully [____]
- [ ] /predict endpoint responds [____]
- [ ] File upload works via Postman [____]
- [ ] JSON response has expected structure [____]
- [ ] Backend accessible from phone (http://IP:8000/docs) [____]

### Documentation
- [ ] Complete Week 04 reflection [____]
- [ ] Take quiz [____]
- [ ] Screenshot: Uvicorn running [____]
- [ ] Screenshot: Postman request/response [____]
- [ ] Save evidence [____]
- [ ] Git commits [____]

---

## Week 05: Android Networking [Start Date: ____ / End Date: ____]

**📈 Cumulative product target: 45%** (see [PRODUCT_PROGRESS_MAP.md](PRODUCT_PROGRESS_MAP.md)) — Cumulative % achieved: [____%]
**Track paths:** Kotlin (primary) → `android-app-kotlin/` · Java (secondary) → `android-app/` — do the identical steps in whichever track you chose.

### Exercises
- [ ] Exercise 1: Add Retrofit dependency [____]
- [ ] Exercise 2: Create API interface [____]
- [ ] Exercise 3: Build multipart request [____]
- [ ] Exercise 4: Parse JSON with Gson [____]
- [ ] Exercise 5: Handle success callback [____]
- [ ] Exercise 6: Handle error callback [____]

### Build Task
- [ ] Add Retrofit and Gson to build.gradle [____]
- [ ] Create ApiService interface [____]
- [ ] Add @POST /predict method [____]
- [ ] Create RetrofitClient singleton [____]
- [ ] Configure base URL (http://LOCAL_IP:8000) [____]
- [ ] Create PredictionResponse data class [____]
- [ ] Implement image upload in MainActivity [____]
- [ ] Show ProgressBar during upload [____]
- [ ] Parse JSON response [____]
- [ ] Handle onResponse (success) [____]
- [ ] Handle onFailure (error) [____]
- [ ] Display result in ResultActivity [____]

### Validation
- [ ] Image uploads successfully [____]
- [ ] Loading indicator shows during upload [____]
- [ ] JSON response parsed correctly [____]
- [ ] Result displays in ResultActivity [____]
- [ ] Error message shown when backend down [____]
- [ ] No crash when network unavailable [____]

### Documentation
- [ ] Complete Week 05 reflection [____]
- [ ] Take quiz [____]
- [ ] Screenshot: successful upload [____]
- [ ] Screenshot: error handling [____]
- [ ] Logcat showing request/response [____]
- [ ] Save evidence [____]
- [ ] Git commits [____]

---

## Week 06: Cloud ML Model Integration [Start Date: ____ / End Date: ____]

**📈 Cumulative product target: 55%** (see [PRODUCT_PROGRESS_MAP.md](PRODUCT_PROGRESS_MAP.md)) — Cumulative % achieved: [____%]
**Track paths:** Kotlin (primary) → `android-app-kotlin/` · Java (secondary) → `android-app/` — do the identical steps in whichever track you chose.

### Exercises
- [ ] Exercise 1: Find plant disease dataset [____]
- [ ] Exercise 2: Load model in FastAPI [____]
- [ ] Exercise 3: Preprocess image [____]
- [ ] Exercise 4: Run inference [____]
- [ ] Exercise 5: Map output to labels [____]
- [ ] Exercise 6: Return structured JSON [____]

### Build Task
- [ ] Obtain or train plant disease model [____]
- [ ] Save model in backend-api/models/ [____]
- [ ] Create labels.txt with disease names [____]
- [ ] Load model in main.py on startup [____]
- [ ] Implement image preprocessing [____]
- [ ] Run model.predict() [____]
- [ ] Extract top prediction [____]
- [ ] Map class index to disease name [____]
- [ ] Return JSON with disease, confidence, symptoms, treatment [____]
- [ ] Test with real leaf images [____]
- [ ] Collect sample images in sample-images/ [____]

### Validation
- [ ] Model loads without errors [____]
- [ ] Prediction returns valid disease name [____]
- [ ] Confidence score is between 0 and 1 [____]
- [ ] JSON includes symptoms and treatment [____]
- [ ] Works with multiple test images [____]
- [ ] Android app displays real predictions [____]

### Documentation
- [ ] Complete Week 06 reflection [____]
- [ ] Take quiz [____]
- [ ] Document model limitations [____]
- [ ] Screenshot: real predictions [____]
- [ ] Save evidence [____]
- [ ] Git commits [____]

---

## Week 07: Room Database and Scan History [Start Date: ____ / End Date: ____]

**📈 Cumulative product target: 65%** (see [PRODUCT_PROGRESS_MAP.md](PRODUCT_PROGRESS_MAP.md)) — Cumulative % achieved: [____%]
**Track paths:** Kotlin (primary) → `android-app-kotlin/` · Java (secondary) → `android-app/` — do the identical steps in whichever track you chose.

### Exercises
- [ ] Exercise 1: Define Entity class [____]
- [ ] Exercise 2: Create DAO interface [____]
- [ ] Exercise 3: Build Database class [____]
- [ ] Exercise 4: Insert scan record [____]
- [ ] Exercise 5: Query all scans [____]
- [ ] Exercise 6: Display in RecyclerView [____]

### Build Task
- [ ] Add Room dependencies to build.gradle [____]
- [ ] Create the Room @Entity `ScanRecord` with fields (table `scan_history`) [____]
- [ ] Create ScanDao with @Insert, @Query, @Delete [____]
- [ ] Create AppDatabase class [____]
- [ ] Save scan after prediction [____]
- [ ] Implement HistoryActivity [____]
- [ ] Create HistoryAdapter for RecyclerView [____]
- [ ] Display list of scans [____]
- [ ] Implement scan detail view [____]
- [ ] Implement delete functionality [____]

### Validation
- [ ] Scan saved to database after prediction [____]
- [ ] HistoryActivity shows list of scans [____]
- [ ] Tapping scan opens detail view [____]
- [ ] Delete removes scan from list [____]
- [ ] Database persists after app restart [____]

### Documentation
- [ ] Complete Week 07 reflection [____]
- [ ] Take quiz [____]
- [ ] Screenshot: history list [____]
- [ ] Screenshot: scan detail [____]
- [ ] Database inspector screenshot [____]
- [ ] Save evidence [____]
- [ ] Git commits [____]

---

## Week 08: XML Disease Library [Start Date: ____ / End Date: ____]

**📈 Cumulative product target: 72%** (see [PRODUCT_PROGRESS_MAP.md](PRODUCT_PROGRESS_MAP.md)) — Cumulative % achieved: [____%]
**Track paths:** Kotlin (primary) → `android-app-kotlin/` · Java (secondary) → `android-app/` — do the identical steps in whichever track you chose.

### Exercises
- [ ] Exercise 1: Design XML structure [____]
- [ ] Exercise 2: Create diseases.xml [____]
- [ ] Exercise 3: Use XmlPullParser [____]
- [ ] Exercise 4: Parse disease nodes [____]
- [ ] Exercise 5: Create Disease model [____]
- [ ] Exercise 6: Display in ListView [____]

### Build Task
- [ ] Create assets/ folder [____]
- [ ] Create diseases.xml [____]
- [ ] Add 6+ disease entries with details [____]
- [ ] Create XmlParser utility class [____]
- [ ] Implement parsing logic [____]
- [ ] Create Disease data model [____]
- [ ] Create DiseaseLibraryActivity [____]
- [ ] Display all diseases in list [____]
- [ ] Implement disease detail view [____]
- [ ] Link predictions to XML entries [____]

### Validation
- [ ] XML file created in assets/ [____]
- [ ] Parsing loads all diseases [____]
- [ ] DiseaseLibraryActivity displays list [____]
- [ ] Tapping disease shows details [____]
- [ ] Predictions linked to XML data [____]

### Documentation
- [ ] Complete Week 08 reflection [____]
- [ ] Take quiz [____]
- [ ] Screenshot: disease library list [____]
- [ ] Screenshot: disease detail [____]
- [ ] Save evidence [____]
- [ ] Git commits [____]

---

## Week 09: TensorFlow Lite Offline AI [Start Date: ____ / End Date: ____]

**📈 Cumulative product target: 82%** (see [PRODUCT_PROGRESS_MAP.md](PRODUCT_PROGRESS_MAP.md)) — Cumulative % achieved: [____%]
**Track paths:** Kotlin (primary) → `android-app-kotlin/` · Java (secondary) → `android-app/` — do the identical steps in whichever track you chose.

### Exercises
- [ ] Exercise 1: Convert model to TFLite [____]
- [ ] Exercise 2: Add TFLite dependency [____]
- [ ] Exercise 3: Load .tflite model [____]
- [ ] Exercise 4: Prepare input tensor [____]
- [ ] Exercise 5: Run inference [____]
- [ ] Exercise 6: Parse output tensor [____]

### Build Task
- [ ] Convert model to .tflite format [____]
- [ ] Copy .tflite and labels.txt to assets/ [____]
- [ ] Add TensorFlow Lite dependency [____]
- [ ] Create the TFLiteClassifier class [____]
- [ ] Load model with Interpreter [____]
- [ ] Implement image preprocessing [____]
- [ ] Run interpreter.run() [____]
- [ ] Parse output and get top prediction [____]
- [ ] Create mode selector (Cloud/Offline) [____]
- [ ] Test offline mode [____]
- [ ] Document fallback if TFLite fails [____]

### Validation
- [ ] .tflite model loads successfully [____]
- [ ] Offline inference returns predictions [____]
- [ ] Mode selector switches between cloud/offline [____]
- [ ] Offline mode works without internet [____]
- [ ] Predictions are reasonably accurate [____]
- [ ] OR fallback documented if TFLite doesn't work [____]

### Documentation
- [ ] Complete Week 09 reflection [____]
- [ ] Take quiz [____]
- [ ] Screenshot: offline mode working [____]
- [ ] Document limitations [____]
- [ ] Save evidence [____]
- [ ] Git commits [____]

---

## Week 10: Notifications, Share, and Location [Start Date: ____ / End Date: ____]

**📈 Cumulative product target: 88%** (see [PRODUCT_PROGRESS_MAP.md](PRODUCT_PROGRESS_MAP.md)) — Cumulative % achieved: [____%]
**Track paths:** Kotlin (primary) → `android-app-kotlin/` · Java (secondary) → `android-app/` — do the identical steps in whichever track you chose.

### Exercises
- [ ] Exercise 1: Create NotificationChannel [____]
- [ ] Exercise 2: Build notification [____]
- [ ] Exercise 3: Add PendingIntent [____]
- [ ] Exercise 4: Implement share intent [____]
- [ ] Exercise 5: Request location permission [____]
- [ ] Exercise 6: Get GPS coordinates [____]

### Build Task
- [ ] Implement share button [____]
- [ ] Create Intent.ACTION_SEND [____]
- [ ] Share scan result as text [____]
- [ ] Create NotificationChannel [____]
- [ ] Build notification with PendingIntent [____]
- [ ] Schedule reminder notification [____]
- [ ] Request location permissions [____]
- [ ] Attempt to get GPS coordinates [____]
- [ ] Tag scan with location (or document failure) [____]
- [ ] Display location in scan detail [____]

### Validation
- [ ] Share button opens Android share dialog [____]
- [ ] Notification appears in notification panel [____]
- [ ] Tapping notification opens app [____]
- [ ] Location permission requested [____]
- [ ] GPS coordinates captured OR attempt documented [____]

### Documentation
- [ ] Complete Week 10 reflection [____]
- [ ] Take quiz [____]
- [ ] Screenshot: share dialog [____]
- [ ] Screenshot: notification [____]
- [ ] Screenshot: location (if working) [____]
- [ ] Save evidence [____]
- [ ] Git commits [____]

---

## Week 11: Testing, Debugging, and Performance [Start Date: ____ / End Date: ____]

**📈 Cumulative product target: 94%** (see [PRODUCT_PROGRESS_MAP.md](PRODUCT_PROGRESS_MAP.md)) — Cumulative % achieved: [____%]
**Track paths:** Kotlin (primary) → `android-app-kotlin/` · Java (secondary) → `android-app/` — do the identical steps in whichever track you chose.

### Exercises
- [ ] Exercise 1: Write test case table [____]
- [ ] Exercise 2: Test all features [____]
- [ ] Exercise 3: Test edge cases [____]
- [ ] Exercise 4: Reproduce and fix bugs [____]
- [ ] Exercise 5: Measure latency [____]
- [ ] Exercise 6: Document debugging process [____]

### Build Task
- [ ] Create comprehensive test case table (30+ cases) [____]
- [ ] Test all features systematically [____]
- [ ] Test edge cases (no internet, invalid image, etc.) [____]
- [ ] Find and fix at least 5 bugs [____]
- [ ] Document bugs and fixes [____]
- [ ] Create comparison screen (cloud vs offline) [____]
- [ ] Measure inference latency for both modes [____]
- [ ] Display latency metrics [____]
- [ ] Ensure all error messages are user-friendly [____]
- [ ] Collect debug logs [____]

### Validation
- [ ] Test case table complete with 30+ cases [____]
- [ ] All major features tested [____]
- [ ] Edge cases handled properly [____]
- [ ] At least 5 bugs documented and fixed [____]
- [ ] Comparison screen shows latency [____]
- [ ] No crashes during testing [____]

### Documentation
- [ ] Complete Week 11 reflection [____]
- [ ] Take quiz [____]
- [ ] Test case table with results [____]
- [ ] Debug log document [____]
- [ ] Screenshot: comparison screen [____]
- [ ] Save evidence [____]
- [ ] Git commits [____]

---

## Week 12: Final Submission [Start Date: ____ / End Date: ____]

**📈 Cumulative product target: 100%** (see [PRODUCT_PROGRESS_MAP.md](PRODUCT_PROGRESS_MAP.md)) — Cumulative % achieved: [____%]
**Track paths:** Kotlin (primary) → `android-app-kotlin/` · Java (secondary) → `android-app/` — do the identical steps in whichever track you chose.

### UI Polish
- [ ] Add app icon [____]
- [ ] Add splash screen (optional) [____]
- [ ] Polish UI colors and spacing [____]
- [ ] Ensure consistent fonts [____]
- [ ] Fix any UI glitches [____]

### Code Organization
- [ ] Organize code into clear packages [____]
- [ ] Remove unused imports [____]
- [ ] Add final code comments [____]
- [ ] Clean up TODOs [____]

### Documentation
- [ ] Write comprehensive README [____]
- [ ] Complete final report (all sections) [____]
- [ ] Create presentation slides (12-15 slides) [____]
- [ ] Write demo video script [____]
- [ ] Practice viva Q&A [____]

### Build and Demo
- [ ] Build release APK [____]
- [ ] Test APK on physical device [____]
- [ ] Record demo video [____]
- [ ] Take final screenshots [____]

### Validation
- [ ] APK builds successfully [____]
- [ ] APK installs on device [____]
- [ ] All features work in release build [____]
- [ ] Final report complete [____]
- [ ] Presentation slides complete [____]
- [ ] Demo video recorded [____]
- [ ] Can answer 60+ viva questions [____]

### Final Checklist
- [ ] All 12 weekly validation checklists passed [____]
- [ ] Complete Week 12 reflection [____]
- [ ] Take quiz [____]
- [ ] Save all final evidence [____]
- [ ] Final git commits [____]
- [ ] Review submission checklist [____]

---

## Overall Progress Summary

### Weeks Completed: ____ / 12

### Features Completed:
- [ ] Android UI with 5+ screens
- [ ] Camera and gallery image selection
- [ ] Permissions handling
- [ ] FastAPI backend with /predict endpoint
- [ ] HTTP POST image upload
- [ ] JSON parsing
- [ ] Real ML model integration
- [ ] Room/SQLite database
- [ ] Scan history with CRUD operations
- [ ] XML disease library
- [ ] XML parsing
- [ ] TensorFlow Lite offline mode (or documented attempt)
- [ ] Share intent
- [ ] Notifications
- [ ] Location tagging (or documented attempt)
- [ ] Test case table
- [ ] Debug logs
- [ ] APK build
- [ ] Final report
- [ ] Presentation slides
- [ ] Demo video

### Evidence Collected:
- [ ] Weekly screenshots (Week 01-12)
- [ ] Weekly reflections (Week 01-12)
- [ ] Test case table with results
- [ ] Debug log document
- [ ] Git commit history (5-10 commits per week)
- [ ] Final APK file
- [ ] Demo video
- [ ] Presentation slides

---

## Notes and Adjustments

**Challenges Faced:**
[Document any major challenges and how you overcame them]

**Features Adjusted:**
[Document any features that were simplified or changed]

**Timeline Adjustments:**
[Document if any weeks took longer or shorter than planned]

**Key Learnings:**
[Document major insights and skills gained]

---

**Keep this file updated weekly. It serves as your progress log and evidence of consistent work.**
