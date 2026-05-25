# LeafGuard AI Demo Video Script

**Complete 19-Step Demonstration Flow**

## Video Overview

- **Duration**: 3-5 minutes (strictly enforced)
- **Format**: MP4, 1080p recommended, max 200MB
- **Audio**: Clear narration with steady pace
- **Recording Method**: Screen recording with voiceover

---

## Pre-Recording Technical Setup Checklist

### Backend Setup
- [ ] FastAPI backend running on laptop (`uvicorn main:app --host 0.0.0.0 --port 8000`)
- [ ] Laptop and phone connected to same Wi-Fi network
- [ ] Verified laptop IP address (e.g., `ipconfig` or `ifconfig`)
- [ ] Backend accessible from phone browser: `http://<laptop-ip>:8000/docs`
- [ ] ML model loaded successfully in backend (check terminal output)

### Phone/Device Setup
- [ ] LeafGuard AI APK installed on device
- [ ] Phone connected to same Wi-Fi as backend laptop
- [ ] Test images ready in gallery (at least 3 different leaf images)
- [ ] Phone battery charged (>80%)
- [ ] Do Not Disturb mode OFF (so notifications can appear)
- [ ] Screen brightness set to high for clear recording
- [ ] Close all other apps to prevent interruptions

### Recording Software Setup
- [ ] Screen recording tool ready (Android Studio Screen Recorder, OBS Studio, or AZ Screen Recorder)
- [ ] Audio input tested (clear voice, no background noise)
- [ ] Recording resolution set to 1080p or 720p minimum
- [ ] Recording frame rate: 30fps recommended
- [ ] Test 10-second recording to verify audio/video quality

### Practice Setup
- [ ] Practiced full demo flow 2-3 times before recording
- [ ] Timed practice run (should be 3-4 minutes for features)
- [ ] Script printed or visible on second screen
- [ ] Backup demo video ready (in case live demo fails during presentation)

---

## Recording Tips

1. **Pacing**: Speak clearly at moderate speed. Not too fast (viewers need to see features), not too slow (risk exceeding 5 minutes).
2. **Annotations**: Use screen recording software to add arrows/highlights pointing to key features.
3. **Silence Removal**: Edit out long pauses or mistakes using video editing software.
4. **Title Cards**: Add title slide at beginning with project name and your name.
5. **Captions**: Consider adding subtitles for clarity (optional but professional).
6. **Transitions**: Use simple fade transitions between major sections (intro → demo → conclusion).
7. **Music**: Avoid background music unless subtle and non-distracting.
8. **Mistakes**: If you make a mistake, pause recording, restart from that step, and edit in post-production.

---

## DEMO VIDEO SCRIPT

---

### SECTION 1: INTRODUCTION (30-45 seconds)

#### [Screen: Title Slide or App Icon]

**NARRATION:**

> "Hello. My name is [Your Full Name], and I'm presenting my CSE 2206 Mobile Application Development project: LeafGuard AI - An Android-Based Plant Disease Detection Application.
>
> **Before I demonstrate the features, I want to emphasize a critical point:** This is not just an AI model. This is not a machine learning research project. This is a comprehensive, production-grade Android mobile application that satisfies every requirement of the CSE 2206 Mobile Application Development syllabus.
>
> This application demonstrates: camera and multimedia input, HTTP networking with POST requests, XML parsing for local data storage, app-to-app communication using share intents, notification channels with PendingIntent, Room database for persistent storage, dual-mode AI inference (cloud and on-device), and complete testing and debugging practices.
>
> The AI functionality serves as a practical use case, but the core achievement is a robust mobile application architecture. Let me demonstrate all features in action."

**TIMING**: 30-45 seconds (speak clearly, emphasize the framing statement)

**SCREEN ACTION**:
- Show title slide with project name
- Optionally show app icon
- Transition to phone screen showing home screen

---

### SECTION 2: 19-STEP FEATURE DEMONSTRATION (3-4 minutes)

---

#### **STEP 1: App Launch and Home Screen**

**SCREEN ACTION:**
- Tap LeafGuard AI app icon on phone home screen
- App opens, showing MainActivity with logo and main buttons

**NARRATION:**

> "Step 1: App launch. Here's the home screen with our custom app icon and user-friendly interface. Notice the clean Material Design layout with clear call-to-action buttons."

**TIMING**: 5 seconds

---

#### **STEP 2: Camera Capture → Disease Detection (Cloud Mode)**

**SCREEN ACTION:**
- Ensure "Cloud Mode" is selected (show toggle/radio button)
- Tap "Scan with Camera" button
- Camera intent launches
- Point camera at a leaf (or use pre-prepared leaf photo/printed image)
- Capture photo
- Photo appears in preview

**NARRATION:**

> "Step 2: Camera capture using Android's MediaStore intent. I'll take a photo of this tomato leaf with early blight symptoms. The app requests camera permission at runtime, satisfying Android 6.0+ security requirements."

**TIMING**: 10 seconds

---

#### **STEP 3: Image Upload and Cloud AI Prediction**

**SCREEN ACTION:**
- Tap "Analyze" button
- Loading indicator appears (ProgressBar or dialog)
- Wait for response (should be 2-5 seconds)
- ResultActivity opens showing prediction result

**NARRATION:**

> "Step 3: The image is uploaded via Retrofit HTTP POST to our FastAPI backend running on the local network. The backend preprocesses the image, runs inference using our TensorFlow model, and returns a JSON response containing disease name, confidence score, and detailed information. Here's the result: Tomato Early Blight detected with 87% confidence."

**TIMING**: 10 seconds

---

#### **STEP 4: Result Display with XML-Sourced Disease Details**

**SCREEN ACTION:**
- Show ResultActivity displaying:
  - Disease name
  - Confidence percentage
  - Symptoms section (from XML)
  - Treatment section (from XML)
  - Prevention section (from XML)
- Scroll through the result screen to show all sections

**NARRATION:**

> "Step 4: The result screen displays comprehensive disease information. Notice the symptoms, treatment, and prevention sections. These are not hardcoded - they're parsed from our disease_library.xml file using Android's XmlPullParser, demonstrating XML parsing as required by the syllabus."

**TIMING**: 10 seconds

---

#### **STEP 5: Gallery Selection → Disease Detection (Cloud Mode)**

**SCREEN ACTION:**
- Tap "Back" or navigate back to MainActivity
- Tap "Scan from Gallery" button
- Gallery picker intent opens
- Select a different leaf image from gallery
- Image preview appears

**NARRATION:**

> "Step 5: Now testing gallery image selection using ACTION_PICK intent. I'll select this potato leaf image from my gallery. This demonstrates multimedia handling with URI-to-Bitmap conversion."

**TIMING**: 8 seconds

---

#### **STEP 6: Gallery Image Upload and Prediction**

**SCREEN ACTION:**
- Tap "Analyze" button
- Loading indicator
- Result appears

**NARRATION:**

> "Step 6: Uploading the gallery image to the cloud backend. The multipart/form-data request is handled by Retrofit with OkHttp client. Result: Potato Late Blight detected with 91% confidence."

**TIMING**: 8 seconds

---

#### **STEP 7: Saving to Scan History (Room Database)**

**SCREEN ACTION:**
- On ResultActivity, tap "Save to History" button (or auto-saves)
- Toast message: "Scan saved successfully"

**NARRATION:**

> "Step 7: Saving this scan to our local Room database. The scan record includes image path, disease name, confidence score, and timestamp. Room provides compile-time verification and SQLite abstraction, demonstrating local storage requirements."

**TIMING**: 5 seconds

---

#### **STEP 8: Viewing Scan History List**

**SCREEN ACTION:**
- Navigate back to MainActivity or open navigation drawer
- Tap "History" button/menu item
- HistoryActivity opens showing RecyclerView with scan list
- Show list with at least 3-5 previous scans

**NARRATION:**

> "Step 8: Opening scan history. This RecyclerView displays all saved scans retrieved from the Room database, ordered by timestamp descending. Each item shows a thumbnail, disease name, confidence, and scan date."

**TIMING**: 8 seconds

---

#### **STEP 9: Opening Scan Detail View**

**SCREEN ACTION:**
- Tap on a scan item in the history list
- HistoryDetailActivity opens showing full scan details:
  - Full-size image
  - Disease name and confidence
  - Timestamp
  - Symptoms, treatment, prevention

**NARRATION:**

> "Step 9: Clicking a scan opens the detail view with complete information. This demonstrates Intent data passing between activities and efficient database queries using Room DAO."

**TIMING**: 6 seconds

---

#### **STEP 10: Deleting a Scan**

**SCREEN ACTION:**
- Tap "Delete" button or trash icon
- Confirmation dialog appears: "Are you sure you want to delete this scan?"
- Tap "Yes" or "Delete"
- Toast message: "Scan deleted"
- Navigate back to history list
- Show that deleted scan is no longer in list

**NARRATION:**

> "Step 10: Deleting a scan with confirmation dialog. The Room database Delete operation removes the record, and the RecyclerView updates automatically. This demonstrates database CRUD operations and user-friendly confirmation patterns."

**TIMING**: 8 seconds

---

#### **STEP 11: Share Feature (App-to-App Communication)**

**SCREEN ACTION:**
- Navigate to a scan detail view or result screen
- Tap "Share" button (share icon)
- Android share chooser appears showing available apps (WhatsApp, Email, Messages, etc.)
- Select "Messages" or another app
- Show share screen briefly with pre-filled text: "LeafGuard AI detected: Tomato Early Blight (87% confidence). Symptoms: ..."

**NARRATION:**

> "Step 11: Sharing scan results using Android's implicit intent system. This demonstrates app-to-app communication as required by the syllabus. The share intent includes disease name, confidence, and detailed information."

**TIMING**: 8 seconds

---

#### **STEP 12: Return to App**

**SCREEN ACTION:**
- Press back button or navigate back to LeafGuard AI app

**NARRATION:**

> "Returning to the app after sharing."

**TIMING**: 2 seconds

---

#### **STEP 13: Offline Mode Toggle**

**SCREEN ACTION:**
- Navigate back to MainActivity
- Show mode selector (Toggle, RadioGroup, or Switch)
- Change from "Cloud Mode" to "Offline Mode"
- Mode indicator updates (e.g., "Offline Mode (TensorFlow Lite)" label)

**NARRATION:**

> "Step 13: Switching to offline mode. In this mode, predictions run entirely on-device using a TensorFlow Lite model embedded in the app's assets folder. No internet connection required."

**TIMING**: 6 seconds

---

#### **STEP 14: Offline Prediction with TensorFlow Lite**

**SCREEN ACTION:**
- Tap "Scan with Camera" or "Scan from Gallery"
- Select or capture a leaf image
- Tap "Analyze"
- Notice shorter loading time (no network request)
- Result appears quickly

**NARRATION:**

> "Step 14: Running offline prediction. The TFLite interpreter loads the model from assets, preprocesses the image into a ByteBuffer, runs inference, and returns the result in under one second. No network request, no backend dependency. Result: Tomato Healthy, 93% confidence."

**TIMING**: 10 seconds

---

#### **STEP 15: Cloud vs Offline Comparison**

**SCREEN ACTION:**
- Show result screen from offline mode
- Optionally show a comparison view (if implemented) with side-by-side cloud vs offline results

**NARRATION:**

> "Step 15: Comparing cloud and offline modes. Cloud mode offers more computational power and easier model updates but requires internet. Offline mode provides instant results and privacy but is limited by device resources. This dual-mode architecture demonstrates both HTTP networking and on-device ML inference."

**TIMING**: 8 seconds

---

#### **STEP 16: Reminder Notification**

**SCREEN ACTION:**
- Navigate to Settings or Notifications section (if available)
- Tap "Set Reminder" or "Schedule Notification" button
- Dialog: "Reminder set for 10 seconds from now" (for demo purposes)
- Wait 10 seconds (you can fast-forward this in editing)
- Notification appears in status bar
- Pull down notification shade to show notification

**NARRATION:**

> "Step 16: Scheduling a reminder notification. The app creates a NotificationChannel for Android 8.0+, builds a notification with PendingIntent, and displays it after the scheduled time. This demonstrates notification system integration as required by the syllabus."

**TIMING**: 10 seconds (can be condensed in editing)

---

#### **STEP 17: Opening App from Notification**

**SCREEN ACTION:**
- Tap on the notification
- App opens to MainActivity or specific screen
- Show that PendingIntent correctly launches the app

**NARRATION:**

> "Step 17: Tapping the notification opens the app using PendingIntent. The FLAG_IMMUTABLE flag ensures security compliance with Android 12+. This completes the notification workflow."

**TIMING**: 5 seconds

---

#### **STEP 18: Disease Library Screen (Browse XML Data)**

**SCREEN ACTION:**
- Navigate to "Disease Library" from menu or MainActivity
- DiseaseLibraryActivity opens showing list of all diseases
- Scroll through list showing multiple disease names
- Tap on a disease (e.g., "Tomato Septoria Leaf Spot")
- Detail view opens showing symptoms, treatment, prevention from XML

**NARRATION:**

> "Step 18: Browsing the offline disease library. All disease data is stored in disease_library.xml in the assets folder and parsed at startup. This allows users to learn about diseases without making predictions. This further demonstrates XML parsing and offline data access."

**TIMING**: 10 seconds

---

#### **STEP 19: Location Tagging (If Implemented) OR Future Work Acknowledgment**

**SCREEN ACTION:**

**Option A (If Location Feature Implemented):**
- Show a scan record with location data (latitude/longitude or map view)
- Briefly explain: "Location tagged using LocationManager or FusedLocationProviderClient"

**Option B (If Location Feature NOT Fully Implemented):**
- Show code snippet or settings section where location permission is requested
- Show documentation of attempted location feature

**NARRATION:**

**Option A (Implemented):**

> "Step 19: Location tagging. Each scan can optionally record GPS coordinates using Android's FusedLocationProviderClient. This demonstrates location-based services integration, satisfying the maps and location syllabus requirement."

**Option B (Attempted but Incomplete):**

> "Step 19: Location tagging was attempted using Android's location APIs. I implemented permission requests and researched FusedLocationProviderClient integration. While this feature remains incomplete due to time constraints and GPS hardware challenges in testing environments, the attempt demonstrates understanding of location-based services as required by the syllabus. This is documented as future work in my report."

**TIMING**: 8 seconds

---

### SECTION 3: CONCLUSION (30 seconds)

#### [Screen: Show app running or final result screen]

**NARRATION:**

> "To conclude: LeafGuard AI is a complete Android mobile application built with Java, satisfying all CSE 2206 requirements.
>
> **Technologies used:** Android SDK with Java, Retrofit for HTTP networking, Room for local database, TensorFlow Lite for on-device AI, XmlPullParser for XML data, NotificationManager with PendingIntent, implicit intents for sharing, runtime permissions, and Material Design UI components.
>
> **Syllabus coverage:** Camera and multimedia input, HTTP networking, XML parsing, app-to-app communication, notifications, local storage with SQLite, dual-mode architecture, testing with 60+ test cases, debugging with detailed logs, and signed APK deployment.
>
> **Limitations:** The AI model is trained on limited data and serves as a proof-of-concept. For production use, the model would require agricultural expert validation. The app architecture, however, can integrate any plant disease model.
>
> **Future work includes:** Expanding disease database, adding multi-language support, implementing real-time location mapping, cloud database sync, and professional UI/UX enhancements.
>
> Thank you for watching. This project demonstrates not just AI integration, but comprehensive mobile application engineering."

**TIMING**: 30 seconds

**SCREEN ACTION**:
- Show app icon or title slide
- Fade to credits screen with your name, course code, date

---

## Post-Recording Checklist

### Editing
- [ ] Remove long pauses and mistakes
- [ ] Add title slide at beginning (3 seconds)
- [ ] Add credits slide at end (3 seconds)
- [ ] Add annotations/arrows to highlight features (optional)
- [ ] Add fade transitions between major sections
- [ ] Verify audio is clear throughout
- [ ] Verify video quality is sharp and readable

### Quality Check
- [ ] Total duration: 3-5 minutes (strictly verify)
- [ ] Audio volume consistent throughout
- [ ] No background noise or distractions
- [ ] All 19 steps demonstrated clearly
- [ ] Viva framing statement included in introduction
- [ ] Conclusion includes technologies, syllabus coverage, limitations

### Export Settings
- [ ] Format: MP4 (H.264 codec)
- [ ] Resolution: 1080p (1920x1080) or 720p (1280x720)
- [ ] Frame rate: 30fps
- [ ] Bitrate: 2-5 Mbps (balances quality and file size)
- [ ] Audio: AAC codec, 128-192 kbps
- [ ] Final file size: Under 200MB (preferred) or 500MB (maximum)

### Upload and Distribution
- [ ] Upload to YouTube (Unlisted or Public) - **Recommended**
  - Video URL: `https://youtube.com/...`
- [ ] OR Upload to Google Drive (Anyone with link can view)
  - Share link: `https://drive.google.com/...`
- [ ] OR Upload to Vimeo (Free account)
- [ ] OR Upload to university portal (if file size allowed)
- [ ] Copy video link to `final-submission/demo-video-link.txt`
- [ ] Verify link works in incognito/private browser window

### Backup
- [ ] Save original unedited recording
- [ ] Save final edited video file locally
- [ ] Save video link in multiple locations
- [ ] Upload backup copy to secondary cloud storage

---

## Alternative: Shorter Demo (If Time Constraint)

If you must condense to under 3 minutes, combine these steps:

- **Steps 2-3-4**: Combine into "Camera capture and cloud prediction with XML results" (15 seconds)
- **Steps 5-6**: Combine into "Gallery selection and prediction" (10 seconds)
- **Steps 7-8-9-10**: Combine into "Scan history management: save, view, detail, delete" (20 seconds)
- **Steps 13-14-15**: Combine into "Offline mode with TFLite comparison" (15 seconds)
- **Steps 16-17**: Combine into "Notification workflow" (12 seconds)

This creates a 2.5-minute demo core, allowing 30s intro + 30s conclusion = 3 minutes total.

However, **the 19-step detailed version is strongly recommended** for full syllabus coverage demonstration.

---

## Video Script Summary Table

| Step | Feature | Screen | Duration |
|------|---------|--------|----------|
| Intro | Framing statement | Title slide | 30-45s |
| 1 | App launch | MainActivity | 5s |
| 2 | Camera capture | Camera intent | 10s |
| 3 | Cloud prediction | Loading + Result | 10s |
| 4 | XML disease details | ResultActivity scroll | 10s |
| 5 | Gallery selection | Gallery picker | 8s |
| 6 | Gallery prediction | Loading + Result | 8s |
| 7 | Save to history | Toast message | 5s |
| 8 | View history list | HistoryActivity | 8s |
| 9 | Scan detail view | HistoryDetailActivity | 6s |
| 10 | Delete scan | Confirmation + delete | 8s |
| 11 | Share feature | Share intent chooser | 8s |
| 12 | Return to app | Navigation | 2s |
| 13 | Offline mode toggle | Mode selector | 6s |
| 14 | Offline prediction | TFLite inference | 10s |
| 15 | Mode comparison | Comparison view | 8s |
| 16 | Reminder notification | Notification appears | 10s |
| 17 | Open from notification | PendingIntent | 5s |
| 18 | Disease library | DiseaseLibraryActivity | 10s |
| 19 | Location tagging | Location view/docs | 8s |
| Conclusion | Summary | Final screen | 30s |
| **TOTAL** | | | **3-4 minutes** |

---

## Final Notes

- **This script is production-ready.** Follow it exactly for a professional demo video.
- **Practice makes perfect.** Rehearse 2-3 times before final recording.
- **Backup plan.** If live demo fails during viva, play this video.
- **Confidence.** Speak clearly and confidently - you built this entire application!

**Your demo video is a critical submission component. Make it count!**

Good luck! 🎬
