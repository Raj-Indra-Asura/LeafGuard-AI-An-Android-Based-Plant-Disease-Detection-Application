# Week 12: Final Submission Exercises

## Exercise 1: APK Building and Installation

**Objective:** Generate a signed release APK and install it on a physical device.

### Steps:
1. Build → Generate Signed Bundle/APK
2. Select APK (not Bundle)
3. Create new keystore:
   - Key store path: `leafguard-release.jks`
   - Password: (your secure password)
   - Alias: `leafguard-key`
   - Validity: 25 years
4. Build release APK
5. Transfer APK to device
6. Install and test all features

### Verification:
- [ ] APK installs without errors
- [ ] App icon appears correctly
- [ ] All permissions requested at runtime
- [ ] Camera opens and captures
- [ ] Cloud predictions work (requires internet)
- [ ] Offline predictions work (without internet)
- [ ] Database saves and loads history
- [ ] XML disease library loads
- [ ] Share functionality works
- [ ] App doesn't crash during normal use

### Expected Output:
- `app-release.jks` keystore file
- `app-release.apk` file (approximately 10-30MB)
- Successfully installed app on device

---

## Exercise 2: Final Report Writing

**Objective:** Write a comprehensive project report (20-30 pages).

### Report Structure:

#### 1. Title Page
- Project title: "LeafGuard AI: Android Plant Disease Detection"
- Your name, registration number
- Course: CSE 2206 - Mobile Application Development
- Department, university, date

#### 2. Abstract (1 page)
Write 250-300 words covering:
- Problem: Farmers lack quick disease identification
- Solution: Android app with AI-powered detection
- Technologies: Android, FastAPI, TensorFlow Lite, Room DB
- Results: Achieved X% accuracy, Y seconds response time

#### 3. Introduction (2-3 pages)
- Background on plant diseases
- Need for mobile detection
- Project objectives
- Scope and limitations
- Report organization

#### 4. Literature Review (2-3 pages)
Review 5-7 existing solutions:
- Similar mobile apps
- Research papers on disease detection
- Comparison table showing features
- Gap analysis (what they lack)

#### 5. System Design (4-5 pages)
Include:
- System architecture diagram
- Component diagram (Android app, FastAPI, ML model)
- Activity flow diagram
- Database schema (ER diagram)
- API endpoints table
- XML structure

#### 6. Implementation (6-8 pages)
For each major feature:

**Camera Integration:**
- How you implemented camera capture
- Permissions handling
- Code highlights (not full code)

**Cloud Predictions:**
- Retrofit API integration
- JSON parsing
- Error handling

**Offline AI:**
- TensorFlow Lite integration
- Model loading
- Preprocessing steps

**Database:**
- Room entities, DAOs
- RecyclerView for history

**XML Parsing:**
- XmlPullParser implementation
- Disease library structure

**Share & Notifications:**
- Intent implementation
- Notification channels

#### 7. Testing (3-4 pages)
Complete test case table:

| Test ID | Feature | Test Case | Input | Expected Output | Result |
|---------|---------|-----------|-------|-----------------|--------|
| TC01 | Camera | Open camera | Click capture button | Camera opens | Pass |
| TC02 | Cloud | Predict disease | Upload image | Returns disease name | Pass |
| ... | ... | ... | ... | ... | ... |

Include:
- Unit test results
- Integration test results
- Performance metrics
- Edge cases tested

#### 8. Results (2-3 pages)
- Screenshots of all features
- Performance data (response times)
- User feedback (if any)
- Accuracy metrics

#### 9. Conclusion (1-2 pages)
- Summary of achievements
- CSE 2206 syllabus coverage
- Limitations
- Future improvements

#### 10. References (1-2 pages)
- Android documentation
- Research papers
- Libraries used
- Online resources

### Verification:
- [ ] All sections complete
- [ ] 20-30 pages total
- [ ] Figures and diagrams included
- [ ] Code snippets formatted
- [ ] References in proper format
- [ ] No grammatical errors
- [ ] PDF exported

---

## Exercise 3: Presentation Slides Creation

**Objective:** Create 12-15 slides for project presentation.

### Slide Structure:

**Slide 1: Title Slide**
- Project title
- Your name
- Course: CSE 2206

**Slide 2: Problem Statement**
- Plant diseases affect crop yield
- Farmers lack quick identification tools
- Need for accessible mobile solution

**Slide 3: Solution Overview**
- Android app with AI detection
- Cloud and offline modes
- History tracking
- Disease information library

**Slide 4: System Architecture**
- Architecture diagram showing:
  - Android app
  - FastAPI backend
  - TensorFlow Lite model
  - Room database
  - XML disease library

**Slide 5: Technologies Used**
- Android SDK
- Kotlin/Java
- FastAPI (Python)
- TensorFlow Lite
- Room Database
- Retrofit
- XmlPullParser

**Slide 6: Features - Camera & Prediction**
- Screenshot of camera capture
- Screenshot of prediction result

**Slide 7: Features - History & Library**
- Screenshot of scan history
- Screenshot of disease library

**Slide 8: Implementation Highlights**
- Room database schema
- XML parsing code snippet
- TFLite integration snippet

**Slide 9: Testing Results**
- Test case summary table
- Performance metrics
- Accuracy results

**Slide 10: CSE 2206 Syllabus Coverage**
- Activities and Intents ✓
- Layouts and UI ✓
- Data storage (Room) ✓
- Networking (Retrofit) ✓
- XML parsing ✓
- Permissions ✓

**Slide 11: Challenges & Solutions**
- Challenge 1: Model accuracy → Solution: Data augmentation
- Challenge 2: Slow API → Solution: Offline mode with TFLite
- Challenge 3: Database queries → Solution: Background threading

**Slide 12: Demo**
- "Live Demo" slide
- Video embed (if presenting online)

**Slide 13: Limitations**
- Limited to 6 disease types
- Requires good lighting
- Model size constraints

**Slide 14: Future Improvements**
- More disease types
- Real-time detection
- Multi-language support
- Treatment recommendations

**Slide 15: Thank You**
- Questions?
- Contact info
- GitHub repository link

### Verification:
- [ ] 12-15 slides total
- [ ] Consistent theme/design
- [ ] Screenshots clear and visible
- [ ] No text overload (max 5 bullet points per slide)
- [ ] Diagrams properly labeled
- [ ] Code snippets readable

---

## Exercise 4: Demo Video Recording

**Objective:** Record a 5-10 minute demo video showing all features.

### Video Script:

**Introduction (30 seconds)**
```
"Hello, I'm [Name]. This is LeafGuard AI, an Android application for plant disease detection, developed as part of CSE 2206 Mobile Application Development course."
```

**Problem Statement (30 seconds)**
```
"Plant diseases significantly reduce crop yields. Farmers often lack quick and accurate identification tools. LeafGuard AI solves this by providing instant disease detection using AI, directly on their smartphones."
```

**Feature Demo (6-8 minutes)**

*Camera Capture (1 min):*
- Show app home screen
- Click "Capture Image" button
- Grant camera permission
- Capture a plant leaf photo
- Show captured image preview

*Cloud Prediction (1 min):*
- Click "Detect Disease (Cloud)"
- Show loading indicator
- Display result: Disease name, confidence percentage
- Explain: "This uses our FastAPI backend server"

*Offline Prediction (1 min):*
- Turn off WiFi/mobile data
- Capture new image
- Click "Detect Disease (Offline)"
- Show result
- Explain: "This uses TensorFlow Lite on-device"

*Scan History (1 min):*
- Navigate to History screen
- Show list of past scans
- Click on an entry to see details
- Explain: "Stored using Room database"

*Disease Library (1 min):*
- Navigate to Disease Library
- Show list of diseases
- Click on a disease
- Show detailed information (symptoms, treatment)
- Explain: "Parsed from XML file using XmlPullParser"

*Share Functionality (30s):*
- From a result screen
- Click Share button
- Show share options (WhatsApp, Email, etc.)
- Explain: "Uses Android share Intent"

*Notifications (30s):*
- Show a notification
- Explain: "Notification when scan completes in background"

**Technical Highlights (1 minute)**
```
"Key technologies used: Room database for local storage, Retrofit for API calls, TensorFlow Lite for on-device AI, and XmlPullParser for disease information. All aligning with CSE 2206 syllabus."
```

**Code Walkthrough (1 minute)**
- Briefly show Android Studio
- Highlight key files:
  - MainActivity.java
  - ScanHistory entity
  - disease_library.xml
  - RetrofitClient.java

**Conclusion (30 seconds)**
```
"LeafGuard AI demonstrates practical Android development covering Activities, databases, networking, XML parsing, and AI integration. Thank you for watching."
```

### Recording Setup:
- Screen recording tool: OBS Studio, Camtasia, or Android Studio
- Microphone for clear audio
- Well-lit environment
- Test device or emulator ready

### Verification:
- [ ] Video length 5-10 minutes
- [ ] Audio clear and understandable
- [ ] All features demonstrated
- [ ] Screen clearly visible (1080p minimum)
- [ ] No awkward pauses or errors
- [ ] Exported as MP4 format
- [ ] File size reasonable (<500MB)

---

## Exercise 5: Viva Preparation

**Objective:** Prepare answers for common viva questions.

### Architecture Questions

**Q1: Explain your system architecture.**
**Answer:**
"My system has three main components:
1. Android app (frontend) - handles UI, camera, local storage
2. FastAPI backend (cloud) - receives images, runs ML model, returns predictions
3. TensorFlow Lite (on-device) - enables offline predictions
The app uses Retrofit for API calls, Room for database, and XmlPullParser for disease info."

**Q2: Why did you choose this architecture?**
**Answer:**
"I used a hybrid cloud-offline architecture because:
- Cloud mode: Higher accuracy with larger models
- Offline mode: Works without internet, faster response
- Room database: Persistent local storage as per CSE 2206 requirements
- XML: Easy to update disease information without code changes"

### Technology Questions

**Q3: Why Room database instead of raw SQLite?**
**Answer:**
"Room provides:
- Compile-time SQL query verification
- Less boilerplate code
- LiveData integration for reactive UI
- Better memory management
- Easier to test
While raw SQLite works, Room follows Android best practices taught in CSE 2206."

**Q4: Why XML parsing instead of JSON?**
**Answer:**
"The CSE 2206 syllabus specifically requires XML parsing using XmlPullParser. However, XML is also appropriate here because:
- Disease library is structured hierarchical data
- XML is human-readable for easy editing
- XmlPullParser is memory-efficient for large files"

**Q5: Why TensorFlow Lite instead of on-server ML only?**
**Answer:**
"TensorFlow Lite enables:
- Offline functionality (no internet required)
- Faster predictions (no network latency)
- Privacy (images don't leave device)
- Reduced server costs
Trade-off: Slightly lower accuracy due to model compression."

### Implementation Questions

**Q6: Explain how you handle camera permissions.**
**Answer:**
```java
// Check permission first
if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    != PackageManager.PERMISSION_GRANTED) {
    // Request permission
    ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
} else {
    // Permission already granted, open camera
    openCamera();
}

// Handle result
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (requestCode == CAMERA_REQUEST_CODE) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
```
This follows Android 6.0+ runtime permissions model."

**Q7: How do you prevent UI thread blocking during database operations?**
**Answer:**
"I use ExecutorService for background operations:
```java
ExecutorService executor = Executors.newSingleThreadExecutor();
executor.execute(() -> {
    // Database operation on background thread
    database.scanHistoryDao().insert(scanHistory);

    // Update UI on main thread
    runOnUiThread(() -> {
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    });
});
```
This prevents ANR (Application Not Responding) errors."

**Q8: How does your XML parsing work?**
**Answer:**
```java
XmlPullParser parser = Xml.newPullParser();
parser.setInput(inputStream, null);

int eventType = parser.getEventType();
Disease currentDisease = null;

while (eventType != XmlPullParser.END_DOCUMENT) {
    String tagName = parser.getName();

    if (eventType == XmlPullParser.START_TAG && tagName.equals("disease")) {
        currentDisease = new Disease();
    } else if (eventType == XmlPullParser.TEXT && currentDisease != null) {
        currentDisease.setSymptoms(parser.getText());
    }

    eventType = parser.next();
}
```
XmlPullParser reads XML sequentially, memory-efficient for large files."

### Challenge Questions

**Q9: What was your biggest challenge?**
**Answer:**
"The biggest challenge was model accuracy. Initially, my TFLite model had only 60% accuracy. I improved it by:
1. Data augmentation (rotation, flip, brightness)
2. Transfer learning from MobileNetV2
3. Proper image preprocessing (224x224, normalization)
4. Increased training epochs
Final accuracy: 85%, acceptable for a prototype."

**Q10: How did you test your app?**
**Answer:**
"Three levels of testing:
1. Unit tests: Tested database DAOs, XML parsing
2. Integration tests: Tested API calls, TFLite inference
3. Manual testing: Tested all features on real device
Created a test case table with 25 test cases covering normal, edge, and error scenarios."

### CSE 2206 Coverage Questions

**Q11: How does your project cover the CSE 2206 syllabus?**
**Answer:**
"My project covers:
- Activities & Intents: MainActivity, ResultActivity, share intent
- Layouts: ConstraintLayout, RecyclerView for history
- Data Storage: Room database (Entity, DAO, Database)
- Networking: Retrofit for REST API calls
- XML Parsing: XmlPullParser for disease library
- Permissions: Runtime camera permission
- Background Tasks: ExecutorService for database
- Notifications: NotificationManager for scan complete alerts"

### Future Improvement Questions

**Q12: What would you improve with more time?**
**Answer:**
"Three main improvements:
1. More diseases: Currently 6, expand to 30+ diseases
2. Real-time detection: Use CameraX with live preview
3. Treatment recommendations: Add remedies for each disease
4. Multi-language: Support local languages for farmers
5. Image quality check: Warn if image is blurry or too dark"

### Limitation Questions

**Q13: What are the limitations of your app?**
**Answer:**
"Honest limitations:
1. Accuracy: 85%, not 100% - can misclassify similar diseases
2. Limited diseases: Only 6 types, real-world has hundreds
3. Lighting dependent: Poor lighting reduces accuracy
4. Model size: 10MB, limits device compatibility
5. Internet required: For cloud mode (offline mode available)
These are acceptable for a prototype project."

---

## Exercise 6: Final Submission Preparation

**Objective:** Organize all deliverables for submission.

### Folder Structure:
```
LeafGuardAI_FinalSubmission/
├── 1_SourceCode/
│   ├── LeafGuardAI_Android/
│   ├── LeafGuardAI_FastAPI/
│   └── README.md (setup instructions)
├── 2_APK/
│   ├── app-release.apk
│   └── installation_guide.txt
├── 3_Report/
│   ├── LeafGuardAI_Final_Report.pdf
│   └── LeafGuardAI_Final_Report.docx (backup)
├── 4_Presentation/
│   ├── LeafGuardAI_Presentation.pptx
│   └── LeafGuardAI_Demo_Video.mp4
├── 5_Documentation/
│   ├── viva_preparation.pdf
│   ├── test_results.xlsx
│   └── screenshots/ (folder with all screenshots)
└── README.txt (submission overview)
```

### Submission Checklist:
- [ ] GitHub repository link accessible
- [ ] Source code compiles without errors
- [ ] APK installs and runs on device
- [ ] Report PDF finalized (20-30 pages)
- [ ] Presentation slides (12-15 slides)
- [ ] Demo video (5-10 minutes, MP4 format)
- [ ] Viva preparation document
- [ ] All screenshots included
- [ ] Test results documented
- [ ] README with setup instructions
- [ ] Backup copies on Google Drive and USB

### Submission Day Tasks:
1. Zip all files: `LeafGuardAI_FinalSubmission.zip`
2. Verify zip file extracts correctly
3. Upload to submission portal
4. Email copy to teacher (if required)
5. Bring USB backup to class
6. Print report (if required)
7. Charge phone for demo

---

## Verification Questions

After completing all exercises, answer these:

1. **Can your APK install on any Android device (version 6.0+)?**
   - Test on at least 2 different devices

2. **Is your report self-contained?**
   - Can someone understand your project without running the code?

3. **Do your slides tell a story?**
   - Problem → Solution → Implementation → Results

4. **Does your demo video show all features?**
   - Camera, cloud, offline, history, library, share

5. **Can you confidently answer any question about your implementation?**
   - Review viva preparation questions

---

**Final Note:** Submission is not just about working code—it's about professional documentation and presentation. Treat this as a real-world project delivery.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 12 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| **3** | **exercises.md** ← *You are here* | **Practice Exercises** |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Theory & Learning Notes](learning-notes.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Practice Exercises** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Build Implementation Guide →](build-task.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 11: Testing, Debugging & Performance](../week-11-testing-debugging-performance/README.md) | [Learning Path](../../LEARNING_PATH.md) | *(Last week — course complete!)* |

---
