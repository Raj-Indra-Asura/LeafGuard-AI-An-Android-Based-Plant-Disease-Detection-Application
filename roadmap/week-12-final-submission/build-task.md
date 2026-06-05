# Week 12: Final Submission - Build Task

## Task Overview

**Goal:** Complete all final deliverables for CSE 2206 project submission.

**Deliverables:**
1. ✅ Signed release APK
2. ✅ Final project report (PDF)
3. ✅ Presentation slides (PPT)
4. ✅ Demo video (MP4)
5. ✅ Viva preparation document
6. ✅ Complete submission package

**Estimated Time:** 20-25 hours over 7 days

---

## Part 1: Generate Signed Release APK

### Step 1: Prepare for Release Build

**1.1. Clean and Rebuild Project**

```bash
# In Android Studio terminal
./gradlew clean
./gradlew build
```

**1.2. Update Version Information**

Open `app/build.gradle`:

```gradle
android {
    defaultConfig {
        applicationId "com.yourname.leafguardai"
        minSdkVersion 21
        targetSdkVersion 33
        versionCode 1        // Increment for each release
        versionName "1.0"    // User-visible version
    }
}
```

**1.3. Configure ProGuard (Optional)**

For smaller APK size, enable ProGuard:

```gradle
buildTypes {
    release {
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

### Step 2: Generate Keystore

**Option A: Using Android Studio GUI**

1. Build → Generate Signed Bundle/APK
2. Choose **APK**
3. Click "Create new..." under Key store path
4. Fill in:
   - Key store path: `leafguard-release.jks`
   - Password: (your secure password)
   - Alias: `leafguard-key`
   - Password: (alias password)
   - Validity: 25 years
   - First and Last Name: Your Name
   - Organizational Unit: Student
   - Organization: Your University
   - City/Locality: Your City
   - State/Province: Your State
   - Country Code: Your Country (e.g., BD)

**Option B: Using Command Line**

```bash
keytool -genkey -v -keystore leafguard-release.jks \
  -alias leafguard-key -keyalg RSA -keysize 2048 -validity 10000
```

**⚠️ IMPORTANT: Backup your keystore file! Without it, you cannot update your app in the future.**

### Step 3: Build Signed APK

**3.1. Using Android Studio**

1. Build → Generate Signed Bundle/APK
2. Select APK
3. Choose existing keystore: `leafguard-release.jks`
4. Enter keystore password
5. Select key alias: `leafguard-key`
6. Enter key password
7. Select destination folder
8. Select build variant: **release**
9. Signature versions: V1 and V2 (both checked)
10. Click Finish

**3.2. Using Gradle (Alternative)**

Add to `app/build.gradle`:

```gradle
android {
    signingConfigs {
        release {
            storeFile file("../leafguard-release.jks")
            storePassword "your_keystore_password"
            keyAlias "leafguard-key"
            keyPassword "your_key_password"
        }
    }

    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

Then run:

```bash
./gradlew assembleRelease
```

APK location: `app/build/outputs/apk/release/app-release.apk`

### Step 4: Test APK Installation

**4.1. Install on Physical Device**

```bash
adb install app-release.apk
```

**4.2. Test All Features**

- [ ] App installs without errors
- [ ] App icon displays correctly
- [ ] Camera permission requested
- [ ] Camera opens and captures images
- [ ] Cloud prediction works (with internet)
- [ ] Offline prediction works (without internet)
- [ ] Scan history saves and loads
- [ ] Disease library displays information
- [ ] Share functionality works
- [ ] Notifications appear
- [ ] No crashes during normal use

**4.3. Check APK Size**

```bash
ls -lh app-release.apk
```

Target: Under 50MB

---

## Part 2: Write Final Project Report

### Step 1: Create Report Template

Use Microsoft Word or LaTeX. Recommended structure:

**Page Layout:**
- A4 size
- Margins: 1 inch all sides
- Font: Times New Roman, 12pt
- Line spacing: 1.5
- Justified alignment

### Step 2: Write Each Section

**Section 1: Title Page**

```
LeafGuard AI: Android Plant Disease Detection Application

A Project Report
Submitted in partial fulfillment of the requirements for
CSE 2206 - Mobile Application Development

By
[Your Name]
Registration No: [Your Reg No]

Department of Computer Science and Engineering
[Your University Name]
[Month, Year]
```

**Section 2: Abstract (250-300 words)**

Template:
```
This project presents LeafGuard AI, an Android application designed to
detect plant diseases using machine learning. The application addresses
the challenge of [problem].

The system consists of three main components: [components].

Key features include [features].

Technologies used include [technologies].

The application was tested with [test details] and achieved [results].

This project fulfills the CSE 2206 syllabus requirements including
[syllabus topics covered].
```

**Section 3: Introduction (2-3 pages)**

Include:
- Background on plant diseases (statistics, impact)
- Need for mobile solution
- Project objectives (3-5 bullet points)
- Scope: What's included and excluded
- Report organization (overview of chapters)

**Section 4: Literature Review (2-3 pages)**

Review 5-7 existing solutions:

| App/Paper | Year | Technologies | Features | Limitations |
|-----------|------|--------------|----------|-------------|
| Plantix | 2020 | CNN, Android | Disease detection | Requires internet |
| ... | ... | ... | ... | ... |

Write 1 paragraph per solution, then a gap analysis paragraph.

**Section 5: System Design (4-5 pages)**

Include these diagrams:

**5.1. System Architecture Diagram**
```
[User] → [Android App] → [FastAPI Server] → [ML Model]
              ↓
        [Room Database]
        [TFLite Model]
        [XML Disease Library]
```

**5.2. Use Case Diagram**
- Actors: User
- Use cases: Capture Image, Detect Disease, View History, View Disease Info, Share Result

**5.3. Activity Diagram**
- Flow: Launch App → Capture Image → Choose Mode → Get Prediction → Save to History

**5.4. Class Diagram**
- Key classes: MainActivity, ResultActivity, ScanHistory, DiseaseLibrary

**5.5. Database Schema**
```
Table: scan_history
- id (INT, PK, AUTO_INCREMENT)
- image_path (TEXT)
- disease_name (TEXT)
- confidence (REAL)
- timestamp (TEXT)
- mode (TEXT) // 'cloud' or 'offline'
```

**5.6. API Endpoints Table**

| Method | Endpoint | Request | Response |
|--------|----------|---------|----------|
| POST | /predict | multipart/form-data (image) | JSON (disease, confidence) |

**5.7. XML Structure**
```xml
<diseases>
    <disease>
        <label>Tomato_Late_Blight</label>
        <commonName>Tomato Late Blight</commonName>
        <scientificName>Phytophthora infestans</scientificName>
        <symptoms>...</symptoms>
        <causes>...</causes>
        <treatment>...</treatment>
    </disease>
</diseases>
```

**Section 6: Implementation (6-8 pages)**

For each major feature, write 1 page covering:

**6.1. Camera Integration**
- Purpose: Capture plant images
- Design decision: Use Intent vs Camera2 API (explain why Intent)
- Code highlights:
```java
Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
}
```
- Challenges: Handling file paths, permissions

**6.2. Cloud Prediction (Retrofit)**
- Purpose: Send image to server for prediction
- Design decision: REST API with multipart upload
- Code highlights:
```java
@Multipart
@POST("/predict")
Call<PredictionResponse> predictDisease(@Part MultipartBody.Part image);
```
- Challenges: Network errors, timeout handling

**6.3. Offline AI (TensorFlow Lite)**
- Purpose: On-device prediction without internet
- Design decision: Quantized MobileNetV2 for speed
- Code highlights:
```java
Interpreter tflite = new Interpreter(loadModelFile());
ByteBuffer inputBuffer = preprocessImage(bitmap);
float[][] output = new float[1][6];
tflite.run(inputBuffer, output);
```
- Challenges: Model conversion, accuracy trade-off

**6.4. Room Database**
- Purpose: Store scan history
- Design decision: Room over SQLite for simplicity
- Code highlights: Entity, DAO, Database classes
- Challenges: Threading, LiveData integration

**6.5. XML Parsing**
- Purpose: Load disease information
- Design decision: XmlPullParser for memory efficiency
- Code highlights: Parsing loop
- Challenges: Handling malformed XML

**6.6. RecyclerView for History**
- Purpose: Display list of past scans
- Design decision: RecyclerView for performance
- Code highlights: Adapter, ViewHolder
- Challenges: Click handling, data binding

**6.7. Share Functionality**
- Purpose: Share results via other apps
- Design decision: ACTION_SEND intent
- Code highlights:
```java
Intent shareIntent = new Intent(Intent.ACTION_SEND);
shareIntent.setType("text/plain");
shareIntent.putExtra(Intent.EXTRA_TEXT, resultText);
startActivity(Intent.createChooser(shareIntent, "Share via"));
```

**6.8. Notifications**
- Purpose: Notify when scan completes
- Design decision: NotificationCompat for compatibility
- Code highlights: NotificationChannel creation
- Challenges: Android 8.0+ channel requirements

**Section 7: Testing (3-4 pages)**

**7.1. Test Case Table**

| ID | Feature | Test Case | Input | Expected | Actual | Status |
|----|---------|-----------|-------|----------|--------|--------|
| TC01 | Camera | Open camera | Click button | Camera opens | Camera opens | Pass |
| TC02 | Camera | Capture image | Take photo | Image saved | Image saved | Pass |
| TC03 | Cloud | Predict healthy | Upload healthy leaf | "Healthy" | "Healthy" | Pass |
| TC04 | Cloud | Predict disease | Upload diseased leaf | Disease name | Disease name | Pass |
| TC05 | Cloud | No internet | Upload without internet | Error message | Error shown | Pass |
| TC06 | Offline | Predict offline | Upload with TFLite | Disease name | Disease name | Pass |
| TC07 | Database | Save scan | Save result | Saved to DB | Saved | Pass |
| TC08 | Database | Load history | Open history | List shown | List shown | Pass |
| TC09 | XML | Load diseases | App start | Diseases loaded | 6 loaded | Pass |
| TC10 | XML | View disease | Click disease | Details shown | Details shown | Pass |
| TC11 | Share | Share result | Click share | Share dialog | Dialog shown | Pass |
| TC12 | Notification | Scan complete | After prediction | Notification | Shown | Pass |
| TC13 | Permission | Camera denied | Deny permission | Error message | Message shown | Pass |
| TC14 | Edge | Large image | Upload 10MB image | Resized | Resized | Pass |
| TC15 | Edge | Dark image | Upload dark photo | Warning | Warning shown | Pass |

... (Add more test cases to reach 25 total)

**7.2. Performance Testing**

| Metric | Target | Actual |
|--------|--------|--------|
| Cloud prediction time | < 3 seconds | 2.1 seconds |
| Offline prediction time | < 1 second | 0.7 seconds |
| App startup time | < 2 seconds | 1.4 seconds |
| APK size | < 50 MB | 28 MB |
| Memory usage | < 100 MB | 75 MB |

**7.3. Usability Testing**

Describe testing with 3-5 users, feedback received.

**Section 8: Results (2-3 pages)**

**8.1. Screenshots**
- Home screen
- Camera capture
- Prediction result (cloud)
- Prediction result (offline)
- Scan history
- Disease library
- Disease details
- Share dialog
- Notification

**8.2. Performance Metrics**
- Accuracy: 85% on test dataset
- Response time: Average 2.1 seconds (cloud)
- Uptime: 99% during testing week

**8.3. User Feedback**
- Positive: Easy to use, fast predictions
- Negative: Limited diseases, occasional misclassifications

**Section 9: Conclusion (1-2 pages)**

**9.1. Summary**
Briefly restate what you built and key achievements.

**9.2. CSE 2206 Syllabus Coverage**
- Activities and Intents ✓
- Layouts (ConstraintLayout, RecyclerView) ✓
- Data storage (Room database) ✓
- Networking (Retrofit) ✓
- XML parsing (XmlPullParser) ✓
- Permissions (runtime camera) ✓
- Background tasks (ExecutorService) ✓
- Notifications (NotificationManager) ✓

**9.3. Limitations**
- Limited to 6 disease types
- Requires good lighting conditions
- Model accuracy 85% (not 100%)
- TFLite model size constrains features

**9.4. Future Work**
- Expand to 30+ diseases
- Real-time detection using CameraX
- Multi-language support
- Treatment recommendation system
- Integration with agriculture extension services

**Section 10: References**

Format (APA style):

```
[1] Android Developers. (2023). Activities. Retrieved from
    https://developer.android.com/guide/components/activities

[2] TensorFlow Lite. (2023). TensorFlow Lite Guide. Retrieved from
    https://www.tensorflow.org/lite/guide

[3] Room Persistence Library. (2023). Android Developers. Retrieved from
    https://developer.android.com/training/data-storage/room

[4] Smith, J. (2021). Plant Disease Detection using Deep Learning.
    Journal of Agricultural Technology, 15(3), 45-60.

... (Add 10-15 references)
```

### Step 3: Finalize Report

**Checklist:**
- [ ] All sections complete
- [ ] 20-30 pages total
- [ ] All diagrams clearly labeled
- [ ] All code snippets properly formatted
- [ ] All screenshots included
- [ ] References in proper format
- [ ] Table of contents generated
- [ ] Page numbers added
- [ ] Spell-checked
- [ ] Grammar-checked
- [ ] PDF exported

---

## Part 3: Create Presentation Slides

### Step 1: Design Theme

Choose a professional theme:
- Use your university's template if available
- Or use PowerPoint/Google Slides built-in themes
- Consistent colors: 2-3 colors max
- Readable fonts: Calibri, Arial, or Helvetica
- Font sizes: Title 32pt, Content 20-24pt

### Step 2: Create Slides

**Slide 1: Title**
```
LeafGuard AI
Android Plant Disease Detection

[Your Name]
CSE 2206 - Mobile Application Development
[Date]
```

**Slide 2: Agenda**
```
1. Problem Statement
2. Solution Overview
3. System Architecture
4. Key Features
5. Implementation Highlights
6. Testing Results
7. Demo
8. Conclusion
```

**Slide 3: Problem Statement**
```
Plant Disease Challenge

• 40% crop yield loss due to diseases
• Farmers lack quick identification tools
• Laboratory testing is expensive and slow
• Need: Accessible, fast, accurate detection

Image: Diseased plant leaf
```

**Slide 4: Solution - LeafGuard AI**
```
AI-Powered Mobile Detection

✓ Instant disease identification
✓ Cloud and offline modes
✓ History tracking
✓ Disease information library
✓ Easy sharing of results

Image: App icon and phone mockup
```

**Slide 5: System Architecture**
```
[Diagram showing:]
Android App (Frontend)
    ↓
FastAPI Server (Backend)
    ↓
TensorFlow Model (AI)

+ Room Database (Local)
+ TensorFlow Lite (Offline AI)
+ XML Disease Library
```

**Slide 6: Technologies**
```
Tech Stack

Android Development:
• Java/Kotlin
• Android Studio

Backend:
• FastAPI (Python)
• TensorFlow

Android Libraries:
• Room Database
• Retrofit
• TensorFlow Lite
• XmlPullParser
```

**Slide 7: Feature - Camera**
```
Feature 1: Camera Capture

• Runtime permission handling
• Camera intent integration
• Image optimization

[Screenshot: Camera capture screen]
```

**Slide 8: Feature - Cloud Prediction**
```
Feature 2: Cloud Prediction

• Upload image to server
• ML model inference
• Display: Disease name, confidence %
• Response time: ~2 seconds

[Screenshot: Prediction result]
```

**Slide 9: Feature - Offline AI**
```
Feature 3: Offline Detection

• TensorFlow Lite on-device
• Works without internet
• Faster: ~0.7 seconds
• Model size: 10MB

[Screenshot: Offline prediction]
```

**Slide 10: Feature - History & Library**
```
Feature 4 & 5: History and Library

Scan History (Room Database):
• Save all past scans
• RecyclerView display
• Click to view details

Disease Library (XML):
• 6 diseases
• Symptoms, causes, treatment
• XmlPullParser implementation

[Screenshots: History screen, Library screen]
```

**Slide 11: Implementation Highlights**
```
Key Code Implementations

1. Room Database:
   Entity → DAO → Database

2. XML Parsing:
   XmlPullParser for disease info

3. TFLite Integration:
   Interpreter for on-device AI

4. Threading:
   ExecutorService for background tasks
```

**Slide 12: Testing Results**
```
Test Results

✓ 25 test cases executed
✓ 24 passed, 1 edge case known limitation

Performance:
• Cloud: 2.1s average
• Offline: 0.7s average
• Accuracy: 85%

[Test case summary table]
```

**Slide 13: CSE 2206 Syllabus Coverage**
```
Course Requirements Fulfilled

✓ Activities and Intents
✓ Layouts (Constraint, RecyclerView)
✓ Data Storage (Room Database)
✓ Networking (Retrofit)
✓ XML Parsing (XmlPullParser)
✓ Permissions (Runtime)
✓ Background Tasks
✓ Notifications
```

**Slide 14: Challenges & Solutions**
```
Challenges Faced

Challenge 1: Model Accuracy (60%)
→ Solution: Data augmentation, transfer learning
→ Result: Improved to 85%

Challenge 2: Slow API Response
→ Solution: Added offline mode with TFLite
→ Result: 0.7s prediction time

Challenge 3: Database on UI Thread
→ Solution: ExecutorService for background
→ Result: No ANR errors
```

**Slide 15: Demo**
```
Live Demonstration

[Video embed or link]

Demo covers:
1. Camera capture
2. Cloud prediction
3. Offline prediction
4. Scan history
5. Disease library
6. Share functionality
```

**Slide 16: Limitations**
```
Current Limitations

• Limited to 6 disease types (expandable)
• Requires good lighting
• Model accuracy 85% (not 100%)
• Model size constraints (10MB)
• No real-time detection yet
```

**Slide 17: Future Improvements**
```
Future Enhancements

1. More Diseases: Expand to 30+ types
2. Real-time Detection: CameraX integration
3. Multi-language: Support local languages
4. Treatment Recommendations: Actionable advice
5. Community Features: Farmer forum
```

**Slide 18: Conclusion**
```
Project Summary

✓ Fully functional Android app
✓ Cloud + Offline AI detection
✓ Complete CSE 2206 requirements
✓ Tested and documented
✓ Ready for real-world use

Learned: Android development, ML integration,
         database management, XML parsing
```

**Slide 19: Thank You**
```
Thank You!

Questions?

GitHub: github.com/yourname/LeafGuardAI
Email: yourname@university.edu
```

### Step 3: Add Animations (Optional)

- Use "Appear" animation for bullet points
- Keep transitions simple (Fade or Push)
- Don't overuse animations

### Step 4: Finalize

**Checklist:**
- [ ] 12-15 slides total
- [ ] Consistent design
- [ ] All screenshots clear
- [ ] No text overload (max 5 bullets per slide)
- [ ] Spell-checked
- [ ] Saved as .pptx
- [ ] PDF version exported

---

## Part 4: Record Demo Video

### Step 1: Prepare Recording Environment

**Software:**
- Screen recorder: OBS Studio, Camtasia, or Screencast-O-Matic
- Video editor: DaVinci Resolve (free) or OpenShot

**Hardware:**
- Device: Android phone or emulator
- Microphone: Clear audio quality
- Lighting: Well-lit room

**Settings:**
- Resolution: 1920x1080 (1080p)
- Frame rate: 30fps
- Format: MP4

### Step 2: Write and Practice Script

**Script Template (5-10 minutes total):**

```
[0:00-0:30] Introduction
"Hello, I'm [Your Name], a student of CSE 2206 at [University].
This is LeafGuard AI, an Android application that detects plant
diseases using artificial intelligence. Let me show you how it works."

[0:30-1:00] Problem Statement
"Plant diseases cause significant crop losses. Farmers often can't
identify diseases quickly, leading to reduced yields. LeafGuard AI
solves this by providing instant, accurate detection right on their
smartphones."

[1:00-2:00] Camera Feature
"Let's start by capturing an image. I'll click the Capture Image button.
The app requests camera permission—I'll grant it. Now I can take a
photo of this plant leaf. The image is captured successfully."

[2:00-3:00] Cloud Prediction
"Now I'll detect the disease using our cloud-based AI. I click
Detect Disease Cloud. The image is sent to our FastAPI server,
which runs a TensorFlow model. Within 2 seconds, we get the result:
Tomato Late Blight with 92% confidence. The app also shows symptoms."

[3:00-4:00] Offline Prediction
"What if there's no internet? Let me turn off WiFi. I'll capture
another image and click Detect Disease Offline. This uses TensorFlow
Lite, a smaller model running directly on the device. Even faster—
0.7 seconds—we get the result: Tomato Early Blight, 87% confidence."

[4:00-5:00] Scan History
"All predictions are saved locally. Let me open Scan History.
Here you can see all past scans with date, disease name, and confidence.
This is stored using Room database. I can click any entry to see
full details."

[5:00-6:00] Disease Library
"LeafGuard also includes a disease information library. I'll navigate
to Disease Library. This data is loaded from an XML file using
XmlPullParser. I can view symptoms, causes, and treatments for each
disease. For example, Tomato Late Blight—here are the symptoms and
recommended treatments."

[6:00-6:30] Share Functionality
"Users can share results with others. From any result screen, I'll
click the Share button. This uses Android's share intent. I can share
via WhatsApp, email, or any messaging app. Very useful for farmers
consulting with agriculture experts."

[6:30-7:00] Notifications
"The app also sends notifications when scans complete. Here's a
notification showing the disease detected. This uses Android's
NotificationManager with proper channels for Android 8.0+."

[7:00-8:00] Code Walkthrough
"Let me briefly show the code structure. Here's Android Studio.
MainActivity handles camera and UI. ScanHistory is the Room entity—
notice the annotations for Entity, PrimaryKey, and ColumnInfo.
Here's the DAO with database operations. This is the disease library
XML file with structured information. And here's the Retrofit interface
for API calls."

[8:00-9:00] Technical Highlights
"Key technologies used: Room database for persistent storage, Retrofit
for network calls, TensorFlow Lite for on-device AI, and XmlPullParser
for parsing disease information. The app follows Android best practices
with proper threading—database operations run on background threads
using ExecutorService to prevent UI blocking."

[9:00-9:30] Conclusion
"LeafGuard AI demonstrates comprehensive Android development covering
all CSE 2206 syllabus requirements: Activities, layouts, data storage,
networking, XML parsing, and more. The app is fully functional, tested,
and ready for real-world use. Thank you for watching."
```

**Practice Tips:**
- Practice 5+ times before final recording
- Speak clearly and not too fast
- Don't say "um" or pause awkwardly
- If you make a mistake, stop, wait 2 seconds, and restart that section
- You can edit out mistakes later

### Step 3: Record Video

**Recording Steps:**
1. Close all unnecessary apps
2. Put phone in Do Not Disturb mode
3. Open screen recorder
4. Set recording area (full screen or phone only)
5. Start recording
6. Begin speaking your script
7. Perform all app interactions
8. Stop recording

**Tips:**
- Record in multiple takes if needed
- Record phone screen + voiceover separately, then combine
- Use a pop filter for better audio
- Record in a quiet room

### Step 4: Edit Video

**Basic Editing:**
- Cut out mistakes and pauses
- Add text overlays for key points
- Add background music (very low volume, optional)
- Add intro/outro titles
- Add captions (accessibility)

**Video Editing Checklist:**
- [ ] All mistakes removed
- [ ] Consistent audio volume
- [ ] No awkward pauses
- [ ] Clear visuals
- [ ] Exported as MP4 1080p
- [ ] File size under 500MB

---

## Part 5: Viva Preparation Document

Create a PDF with common questions and answers (see Exercise 5 in exercises.md).

---

## Part 6: Organize Final Submission

### Create Submission Package

**Folder Structure:**
```
LeafGuardAI_FinalSubmission_[YourName]/
├── README.txt
├── 1_SourceCode/
│   ├── LeafGuardAI_Android/
│   │   ├── app/
│   │   ├── gradle/
│   │   └── README.md
│   └── LeafGuardAI_FastAPI/
│       ├── main.py
│       ├── model/
│       └── requirements.txt
├── 2_APK/
│   ├── app-release.apk
│   └── installation_guide.txt
├── 3_Report/
│   ├── LeafGuardAI_Final_Report.pdf
│   └── LeafGuardAI_Final_Report.docx
├── 4_Presentation/
│   ├── LeafGuardAI_Presentation.pptx
│   ├── LeafGuardAI_Presentation.pdf
│   └── LeafGuardAI_Demo_Video.mp4
└── 5_Documentation/
    ├── viva_preparation.pdf
    ├── test_results.xlsx
    └── screenshots/
        ├── 01_home_screen.png
        ├── 02_camera_capture.png
        ├── 03_prediction_result.png
        └── ... (all screenshots)
```

### README.txt Content

```
LeafGuard AI - Final Submission
CSE 2206 - Mobile Application Development
Student: [Your Name]
Registration: [Your Registration Number]
Date: [Submission Date]

================================================================================
CONTENTS
================================================================================
1. Source Code (1_SourceCode/)
2. Release APK (2_APK/)
3. Project Report (3_Report/)
4. Presentation & Demo (4_Presentation/)
5. Additional Documentation (5_Documentation/)

================================================================================
INSTALLATION INSTRUCTIONS
================================================================================
1. Transfer app-release.apk to Android device (Android 6.0+)
2. Enable "Install from Unknown Sources" in device settings
3. Open apk file and install
4. Grant camera permission when prompted

================================================================================
SETUP INSTRUCTIONS (For Source Code)
================================================================================
Android App:
1. Install Android Studio (latest version)
2. Open project from 1_SourceCode/LeafGuardAI_Android/
3. Wait for Gradle sync to complete
4. Run on emulator or device

FastAPI Backend:
1. Install Python 3.8+
2. Navigate to 1_SourceCode/LeafGuardAI_FastAPI/
3. Run: pip install -r requirements.txt
4. Run: uvicorn main:app --reload
5. Server runs on http://localhost:8000

================================================================================
GITHUB REPOSITORY
================================================================================
https://github.com/[yourusername]/LeafGuardAI

================================================================================
CONTACT
================================================================================
Email: [youremail@university.edu]
Phone: [Your Phone Number]
```

### Final Checklist

**Before Submission:**
- [ ] All files organized in folder structure
- [ ] README.txt complete
- [ ] Source code compiles without errors
- [ ] APK installs on test device
- [ ] Report is 20-30 pages
- [ ] Presentation is 12-15 slides
- [ ] Demo video is 5-10 minutes
- [ ] All screenshots included
- [ ] GitHub repository accessible
- [ ] Folder zipped: `LeafGuardAI_FinalSubmission_[YourName].zip`
- [ ] Zip file under 500MB
- [ ] Backup copy on Google Drive
- [ ] Backup copy on USB drive
- [ ] Printed report (if required)
- [ ] Phone charged for demo

**Submission Day:**
- [ ] Upload zip to submission portal
- [ ] Email confirmation received
- [ ] Bring USB backup to class
- [ ] Prepare phone for demo
- [ ] Review viva questions
- [ ] Dress professionally

---

## Estimated Timeline

| Day | Task | Hours |
|-----|------|-------|
| Day 1 | APK building and testing | 3h |
| Day 2 | Report writing (sections 1-5) | 6h |
| Day 3 | Report writing (sections 6-10) | 6h |
| Day 4 | Presentation slides creation | 4h |
| Day 5 | Demo video recording and editing | 4h |
| Day 6 | Viva preparation | 3h |
| Day 7 | Final organization and submission | 2h |
| **Total** | | **28h** |

---

## Success Criteria

- ✅ APK installs and runs on any Android 6.0+ device
- ✅ Report is comprehensive (20-30 pages) and professionally formatted
- ✅ Presentation tells a clear story with visuals
- ✅ Demo video shows all features clearly
- ✅ All deliverables submitted on time
- ✅ Ready for viva with confident answers

---

**Final Note:** This is the culmination of 12 weeks of work. Take your time to polish each deliverable. Quality matters more than rushing to submit early.

**Good luck with your final submission! You've got this! 🚀**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 12 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| **4** | **build-task.md** ← *You are here* | **Build Implementation Guide** |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Practice Exercises](exercises.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Build Implementation Guide** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Validation & Verification →](validation-checklist.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 11: Testing, Debugging & Performance](../week-11-testing-debugging-performance/README.md) | [Learning Path](../../LEARNING_PATH.md) | *(Last week — course complete!)* |

---
