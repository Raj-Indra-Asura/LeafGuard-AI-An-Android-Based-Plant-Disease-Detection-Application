# Weekly Validation Master Checklist

This document provides a comprehensive validation checklist spanning all 12 weeks of the LeafGuard AI project. Use this to track your progress and ensure you meet all requirements before moving to the next week.

## How to Use This Checklist

- [ ] Review the checklist for your current week BEFORE starting work
- [ ] Complete each item systematically - do not skip ahead
- [ ] Mark items as complete only when you can demonstrate the feature working
- [ ] Save evidence (screenshots, logs, videos) in `docs/evidence/week-XX/`
- [ ] If you cannot complete an item, document why and seek help before proceeding

---

## Week 01: Project Understanding and Proposal

### Understanding
- [ ] Read and understand the CSE 2206 course syllabus completely
- [ ] List all 15+ syllabus topics that must be covered
- [ ] Explain in writing why this is a mobile app project, not just an AI project

### Senior Repo Analysis
- [ ] Found and analyzed at least 3 senior Android project repositories
- [ ] Filled in SENIOR_REPO_ANALYSIS.md comparison table
- [ ] Documented at least 5 patterns to copy and 5 mistakes to avoid

### Architecture
- [ ] Drew system architecture diagram showing: Android app, Retrofit, FastAPI, ML model, Room database
- [ ] Identified all data flows: camera → app → cloud API → response → UI
- [ ] Identified offline mode flow: camera → app → TFLite → response → UI
- [ ] Explained difference between cloud and offline inference modes

### Proposal
- [ ] Completed proposal using docs/proposal-template.md
- [ ] Included problem statement, objectives, methodology, timeline
- [ ] Listed all features explicitly with syllabus topic mapping
- [ ] Identified risks and mitigation strategies

### Evidence Saved
- [ ] Architecture diagram image saved to `docs/evidence/week-01/architecture.png`
- [ ] Senior repo analysis notes saved
- [ ] Proposal PDF saved to `docs/evidence/week-01/proposal.pdf`

---

## Week 02: Android Basics and UI Skeleton

### Environment Setup
- [ ] Android Studio installed (latest stable version)
- [ ] Android SDK installed with API 21+ (Lollipop minimum)
- [ ] Created new Android project with Empty Activity
- [ ] Project builds successfully without errors
- [ ] Can run app on emulator or physical device

### Project Structure Understanding
- [ ] Explained purpose of: app/, gradle/, AndroidManifest.xml, build.gradle files
- [ ] Identified where Java source code goes (app/src/main/java/)
- [ ] Identified where XML layouts go (app/src/main/res/layout/)
- [ ] Identified where assets go (app/src/main/assets/)

### UI Screens Created
- [ ] MainActivity (home screen with options)
- [ ] CaptureActivity or fragment (camera/gallery selection)
- [ ] ResultActivity (shows prediction result)
- [ ] HistoryActivity (shows scan history list)
- [ ] All screens have basic XML layouts with placeholder UI

### Navigation
- [ ] Can navigate from MainActivity to CaptureActivity
- [ ] Can pass data between activities using Intent extras
- [ ] Can return to previous activity using back button
- [ ] Tested navigation flow manually on device

### Gradle and Build
- [ ] Explained what Gradle does
- [ ] Identified dependencies section in build.gradle
- [ ] Successfully ran Build → Rebuild Project
- [ ] Understood difference between debug and release builds

### Evidence Saved
- [ ] Screenshot of all 4 UI screens saved to `docs/evidence/week-02/`
- [ ] Screenshot of successful build log
- [ ] Screenshot of app running on device/emulator

---

## Week 03: Camera and Gallery

### Camera Intent
- [ ] Implemented camera capture using `MediaStore.ACTION_IMAGE_CAPTURE`
- [ ] Can launch camera app from your app
- [ ] Captured image returned successfully to your app
- [ ] Displayed captured image in ImageView

### Gallery Picker
- [ ] Implemented gallery picker using `Intent.ACTION_PICK` or `Intent.ACTION_GET_CONTENT`
- [ ] Can open gallery from your app
- [ ] Selected image returned successfully to your app
- [ ] Displayed selected image in ImageView

### Runtime Permissions
- [ ] Added CAMERA permission to AndroidManifest.xml
- [ ] Implemented runtime permission request for CAMERA (Android 6.0+)
- [ ] Handled permission granted scenario
- [ ] Handled permission denied scenario with user-friendly message
- [ ] Tested on Android 6.0+ device

### URI and Bitmap Handling
- [ ] Converted URI to Bitmap correctly
- [ ] Explained difference between content:// URI and file:// URI
- [ ] Handled large images without OutOfMemoryError

### Image Preview and Resizing
- [ ] Displayed selected/captured image before sending to prediction
- [ ] Implemented image resizing to reduce file size (e.g., max 1024x1024)
- [ ] Saved resized image to temporary file if needed
- [ ] Tested with very large images (5MB+) without crashes

### Evidence Saved
- [ ] Screenshot showing camera intent in action
- [ ] Screenshot showing gallery picker
- [ ] Screenshot showing permission request dialog
- [ ] Screenshot showing selected image preview
- [ ] Saved code snippet of bitmap resizing logic

---

## Week 04: FastAPI Backend

### Python Environment Setup
- [ ] Python 3.8+ installed
- [ ] Created virtual environment (venv)
- [ ] Installed FastAPI and uvicorn using pip
- [ ] Can activate/deactivate venv correctly

### FastAPI Basic Server
- [ ] Created main.py with FastAPI app instance
- [ ] Implemented root endpoint (/) returning welcome message
- [ ] Ran server using `uvicorn main:app --reload`
- [ ] Accessed root endpoint in browser successfully

### /predict Endpoint
- [ ] Created POST /predict endpoint
- [ ] Accepts a multipart/form-data upload with the part named `image` (not `file`)
- [ ] Validates uploaded file is an image
- [ ] Returns JSON response with dummy prediction data

### Dummy JSON Response
- [ ] Response includes JSON fields: `disease` (not `disease_name`), confidence, symptoms, treatment, prevention
- [ ] Response format matches what Android app expects
- [ ] Tested endpoint using Postman or curl
- [ ] Confirmed endpoint works from browser or API testing tool

### Local Network Setup
- [ ] Found local IP address of laptop (e.g., 192.168.1.5)
- [ ] Ran FastAPI server accessible on local network (--host 0.0.0.0)
- [ ] Connected phone and laptop to same Wi-Fi network
- [ ] Verified can access backend from phone browser (http://192.168.1.5:8000)

### Evidence Saved
- [ ] Screenshot of FastAPI server running in terminal
- [ ] Screenshot of Postman/curl testing /predict endpoint
- [ ] Screenshot of JSON response
- [ ] Screenshot of accessing backend from phone browser
- [ ] Saved backend code (main.py) to evidence folder

---

## Week 05: Android Networking

### Retrofit Setup
- [ ] Added Retrofit and Gson dependencies to build.gradle
- [ ] Added INTERNET permission to AndroidManifest.xml
- [ ] Created Retrofit instance with base URL
- [ ] Created API interface with @Multipart POST method

### Multipart Image Upload
- [ ] Converted image file to MultipartBody.Part
- [ ] Implemented POST request to /predict endpoint
- [ ] Sent image successfully from Android to FastAPI
- [ ] Received JSON response in Android app

### JSON Parsing
- [ ] Created data class/POJO for prediction response
- [ ] Used Gson to parse JSON response automatically
- [ ] Extracted `disease` (JSON field), confidence, symptoms, treatment, prevention
- [ ] Displayed all fields in ResultActivity UI

### Loading State
- [ ] Showed ProgressBar or loading dialog while uploading
- [ ] Disabled upload button during request
- [ ] Hid loading indicator when response received
- [ ] Tested loading state is visible for slow networks

### Error Handling
- [ ] Handled no internet connection error
- [ ] Handled server unreachable error (wrong IP/port)
- [ ] Handled server error (500 status code)
- [ ] Displayed user-friendly error messages using Toast or Dialog
- [ ] App does not crash when network request fails

### Local IP Configuration
- [ ] Base URL uses local IP address (e.g., http://192.168.1.5:8000)
- [ ] Documented how to change IP address for different networks
- [ ] Tested on same Wi-Fi network successfully
- [ ] Explained why localhost/127.0.0.1 does not work from phone

### Evidence Saved
- [ ] Screenshot of successful image upload and result display
- [ ] Screenshot of loading state
- [ ] Screenshot of error handling (airplane mode test)
- [ ] Logcat output showing HTTP request and response
- [ ] Code snippet of Retrofit API interface

---

## Week 06: Cloud ML Model

### Model Selection
- [ ] Chose plant disease dataset (e.g., PlantVillage)
- [ ] Decided on model: pre-trained or custom trained
- [ ] Documented model input requirements (size, format, normalization)
- [ ] Documented model output format (class indices, probabilities)

### Image Preprocessing in Backend
- [ ] Resized uploaded image to model input size (e.g., 224x224)
- [ ] Converted image to RGB format
- [ ] Normalized pixel values (e.g., 0-1 range or ImageNet normalization)
- [ ] Converted image to numpy array

### Label Mapping
- [ ] Created labels list matching model output classes
- [ ] Mapped class index to disease name
- [ ] Handled class index to label correctly
- [ ] Returned correct disease name in JSON response

### Confidence Score
- [ ] Extracted prediction confidence from model output
- [ ] Converted confidence to percentage (0-100%)
- [ ] Returned confidence in JSON response
- [ ] Displayed confidence in Android app UI

### Real Inference Pipeline
- [ ] Loaded ML model in FastAPI backend (TensorFlow, PyTorch, or ONNX)
- [ ] Performed inference on uploaded image
- [ ] Returned real prediction (not dummy data)
- [ ] Tested with at least 5 different plant images
- [ ] Verified predictions make sense (not random)

### Model Limitations Documentation
- [ ] Documented which plant species/diseases model supports
- [ ] Documented which it does NOT support
- [ ] Documented expected accuracy and known failure cases
- [ ] Added disclaimer: model is for learning purposes, not medical/agricultural advice

### Evidence Saved
- [ ] Screenshot of real prediction results (at least 5 different images)
- [ ] Table comparing predicted vs actual disease for test images
- [ ] Model architecture description or diagram
- [ ] Preprocessing code snippet
- [ ] Model limitations documentation

---

## Week 07: Room Database and Scan History

### Room Setup
- [ ] Added Room dependencies to build.gradle
- [ ] Created Entity class for Scan with fields: id, image_path, disease_name, confidence, timestamp
- [ ] Annotated Entity class correctly (@Entity, @PrimaryKey, @ColumnInfo)

### DAO Interface
- [ ] Created DAO interface with @Dao annotation
- [ ] Implemented @Insert method for adding scan
- [ ] Implemented @Query method for getting all scans (ordered by timestamp DESC)
- [ ] Implemented @Query method for getting single scan by ID
- [ ] Implemented @Delete method for deleting scan

### Database Class
- [ ] Created RoomDatabase subclass
- [ ] Annotated with @Database including entity list and version number
- [ ] Implemented singleton pattern to get database instance
- [ ] Can access DAO from database instance

### Saving Scan After Prediction
- [ ] After receiving prediction, saved scan to Room database
- [ ] Saved image path, disease name, confidence, timestamp
- [ ] Verified data saved correctly using Database Inspector (Android Studio)

### History List Screen
- [ ] Created HistoryActivity or fragment
- [ ] Queried all scans from database
- [ ] Displayed scans in RecyclerView or ListView
- [ ] Each item shows: thumbnail, disease name, confidence, date
- [ ] List updates when new scan added

### Scan Detail View
- [ ] Clicking list item opens detail view
- [ ] Detail view shows: full image, disease name, confidence, timestamp, symptoms, treatment, prevention
- [ ] Can navigate back to list

### Delete Functionality
- [ ] Implemented delete button/option for each scan
- [ ] Confirmation dialog before deleting
- [ ] Successfully deleted scan from database
- [ ] UI updated after deletion (scan removed from list)

### Data Lifecycle
- [ ] Explained when data is inserted (after prediction)
- [ ] Explained when data is queried (opening history screen)
- [ ] Explained when data is deleted (user action)
- [ ] Database persists across app restarts

### Evidence Saved
- [ ] Screenshot of history list with at least 5 scans
- [ ] Screenshot of scan detail view
- [ ] Screenshot of delete confirmation dialog
- [ ] Screenshot of Database Inspector showing scan records
- [ ] Entity class code snippet

---

## Week 08: XML Disease Library

### XML File Design
- [ ] Created diseases.xml in app/src/main/assets/
- [ ] XML structure has root element and disease elements
- [ ] Each disease element has: name, symptoms, treatment, prevention
- [ ] XML is well-formed (validated using XML validator)
- [ ] Included at least 10 diseases

### XML Parsing in Android
- [ ] Used XmlPullParser or DocumentBuilder to parse XML
- [ ] Read XML file from assets folder
- [ ] Extracted disease name, symptoms, treatment, prevention
- [ ] Stored parsed data in List or Map

### Label-to-Disease Mapping
- [ ] Matched model prediction label to XML disease name
- [ ] Retrieved corresponding symptoms, treatment, prevention from XML
- [ ] Returned detailed disease info in prediction result
- [ ] Handled case when label not found in XML (fallback message)

### Offline Disease Library Screen
- [ ] Created activity/fragment to browse all diseases from XML
- [ ] Displayed list of all disease names
- [ ] Clicking disease opens detail view with symptoms, treatment, prevention
- [ ] Works without internet connection

### Testing XML Parsing
- [ ] Tested with at least 10 different diseases
- [ ] Verified all fields (symptoms, treatment, prevention) display correctly
- [ ] Tested with invalid XML (should handle gracefully without crash)
- [ ] Tested with missing disease in XML (should show fallback)

### Evidence Saved
- [ ] Screenshot of disease library list screen
- [ ] Screenshot of disease detail view (from library)
- [ ] Screenshot of prediction result showing XML-sourced details
- [ ] XML file structure snippet
- [ ] XML parsing code snippet

---

## Week 09: TensorFlow Lite Offline AI

### TFLite Model Preparation
- [ ] Converted ML model to TensorFlow Lite format (.tflite)
- [ ] Created labels.txt file with class names (one per line)
- [ ] Placed model.tflite and labels.txt in app/src/main/assets/
- [ ] Verified model file size is reasonable for mobile (<50MB preferred)

### TFLite Dependency
- [ ] Added TensorFlow Lite dependency to build.gradle
- [ ] Added TensorFlow Lite Support library (optional but recommended)
- [ ] Project builds successfully with TFLite dependencies

### Loading TFLite Model
- [ ] Loaded .tflite model from assets in Android app
- [ ] Created Interpreter instance
- [ ] Loaded labels.txt into memory
- [ ] Handled model loading errors gracefully

### Input/Output Tensor Handling
- [ ] Identified input tensor shape (e.g., [1, 224, 224, 3])
- [ ] Preprocessed image to match input tensor shape
- [ ] Normalized pixel values correctly (0-1 or -1 to 1)
- [ ] Created output tensor buffer with correct shape

### Offline Prediction
- [ ] Performed inference using interpreter.run()
- [ ] Extracted prediction results from output tensor
- [ ] Mapped predicted class index to label from labels.txt
- [ ] Calculated confidence score
- [ ] Displayed offline prediction result in UI

### Cloud vs On-Device Mode Selector
- [ ] Added toggle/switch in UI to choose cloud or offline mode
- [ ] When cloud mode selected, uses Retrofit to call FastAPI
- [ ] When offline mode selected, uses TFLite interpreter
- [ ] Mode selection persists during session
- [ ] Clearly labeled which mode is active

### Fallback Strategy
- [ ] If TFLite integration fails, cloud mode still works
- [ ] Documented TFLite as experimental/offline feature
- [ ] App does not crash if TFLite fails to load
- [ ] User informed if offline mode unavailable

### Evidence Saved
- [ ] Screenshot of mode selector UI
- [ ] Screenshot of offline prediction result
- [ ] Screenshot of cloud prediction result (same image for comparison)
- [ ] Logcat showing TFLite inference time
- [ ] Code snippet of TFLite inference

---

## Week 10: Notifications, Share, and Location

### Android Share Intent
- [ ] Implemented share button in ResultActivity
- [ ] Created share intent with prediction text (disease name, confidence)
- [ ] Optionally included image in share intent
- [ ] Tested sharing to messaging apps (WhatsApp, Messages, Email)
- [ ] Share works correctly on Android 10+

### NotificationChannel
- [ ] Created NotificationChannel for reminder notifications (Android 8.0+)
- [ ] Channel has user-visible name and description
- [ ] Channel importance level set correctly
- [ ] Registered channel with NotificationManager

### PendingIntent
- [ ] Created PendingIntent to open app when notification clicked
- [ ] Used correct flags for Android 12+ (PendingIntent.FLAG_IMMUTABLE)
- [ ] PendingIntent opens correct activity

### Reminder Notification
- [ ] Implemented button/option to schedule reminder notification
- [ ] Notification appears after delay (e.g., 10 seconds for testing)
- [ ] Notification shows title, message, icon
- [ ] Clicking notification opens app
- [ ] Tested on Android 8.0+ device

### Location Tagging (Attempted)
- [ ] Attempted to add location permission to AndroidManifest.xml
- [ ] Attempted to request runtime location permission
- [ ] Attempted to get current GPS coordinates using LocationManager or FusedLocationProviderClient
- [ ] Attempted to save location with scan record
- [ ] If failed: documented as future work with explanation of what was tried

### Location Fallback
- [ ] If location feature incomplete, documented attempt in code comments
- [ ] Saved notes on what worked and what didn't
- [ ] Created future work section in report noting location as enhancement
- [ ] Confirmed location attempt satisfies CSE 2206 syllabus requirement

### Evidence Saved
- [ ] Screenshot of share intent chooser
- [ ] Screenshot of shared prediction in another app
- [ ] Screenshot of reminder notification
- [ ] Screenshot of app opening from notification
- [ ] Screenshot of location permission request (if attempted)
- [ ] Code snippet of notification creation

---

## Week 11: Testing, Debugging, and Performance

### Test Case Table
- [ ] Created test cases table with columns: Test Case, Input/Action, Expected Result, Actual Result, Status
- [ ] Included at least 20 test cases covering all features
- [ ] Tested: camera, gallery, permissions, upload, prediction, history, delete, XML, TFLite, share, notification
- [ ] Tested edge cases: no internet, invalid image, empty history, permission denied
- [ ] Marked each test as PASS or FAIL
- [ ] Documented failing test cases with explanation

### Debugging Log
- [ ] Enabled verbose logging throughout app using Log.d(), Log.w(), Log.e()
- [ ] Logged image upload request and response
- [ ] Logged database operations (insert, query, delete)
- [ ] Logged TFLite inference results
- [ ] Logged permission request results
- [ ] Captured Logcat output showing logs for typical user flow

### Edge Case Testing
- [ ] Tested with airplane mode (no internet)
- [ ] Tested with very slow network
- [ ] Tested with invalid image file
- [ ] Tested with very large image (10MB+)
- [ ] Tested with permission denied by user
- [ ] Tested with empty scan history
- [ ] Tested rapid clicking of buttons (prevent duplicate requests)
- [ ] App handles all edge cases without crashing

### No-Internet Error State
- [ ] Cloud mode shows clear error when no internet
- [ ] Error message is user-friendly (not technical stack trace)
- [ ] Suggests switching to offline mode
- [ ] App remains usable after network error

### Cloud vs On-Device Comparison Screen
- [ ] Created comparison screen showing both modes side-by-side
- [ ] Tested same image with both cloud and offline mode
- [ ] Documented prediction differences (if any)
- [ ] Documented user experience differences

### Latency Measurement
- [ ] Measured cloud mode latency (upload + inference + download time)
- [ ] Measured offline mode latency (TFLite inference time)
- [ ] Displayed or logged latency times
- [ ] Created comparison table: cloud vs offline latency
- [ ] Documented latency findings

### Model Accuracy Handling
- [ ] If model accuracy is weak, documented limitations clearly
- [ ] Focused report on mobile engineering strength (not AI accuracy)
- [ ] Prepared explanation: project demonstrates mobile app development, not agricultural science
- [ ] Ready to explain trade-offs during viva

### Evidence Saved
- [ ] Complete test case table (Excel or Markdown)
- [ ] Logcat output file showing detailed logs
- [ ] Screenshots of edge case handling (no internet, permission denied, etc.)
- [ ] Latency comparison table
- [ ] Video recording of full app flow (2-3 minutes)

---

## Week 12: Final Submission

### UI Polish
- [ ] Consistent color scheme across all screens
- [ ] Proper spacing and alignment in all layouts
- [ ] User-friendly button labels and instructions
- [ ] Loading indicators where appropriate
- [ ] Error messages are clear and actionable

### App Icon
- [ ] Created custom app icon (launcher icon)
- [ ] Added icon using Image Asset Studio in Android Studio
- [ ] Icon appears on device home screen
- [ ] Icon is relevant to plant disease detection theme

### APK Build
- [ ] Built release APK (Build → Build Bundle(s) / APK(s) → Build APK(s))
- [ ] APK file created successfully (app/build/outputs/apk/release/)
- [ ] Tested installing APK on another device
- [ ] APK installs and runs correctly
- [ ] Saved APK file with clear name: LeafGuardAI_v1.0.apk

### GitHub README Polish
- [ ] README includes: project title, description, features, screenshots
- [ ] Includes setup instructions for both Android app and backend
- [ ] Includes usage instructions
- [ ] Includes week-by-week learning progress
- [ ] Includes demo video link (if hosted)
- [ ] Includes syllabus mapping summary
- [ ] Professional formatting and clear structure

### Final Report Writing
- [ ] Used docs/final-report-template.md
- [ ] Completed all sections: title, abstract, introduction, methodology, implementation, testing, results, limitations, conclusion, references
- [ ] Included all screenshots and diagrams
- [ ] Included test case table
- [ ] Included syllabus mapping table
- [ ] Included architecture diagram
- [ ] Report is 15-25 pages
- [ ] Exported report as PDF

### Slide Deck
- [ ] Used docs/presentation-outline.md as guide
- [ ] Created 12-15 slides
- [ ] Included: title, agenda, problem, solution, architecture, features, demo, testing, results, limitations, future work, conclusion
- [ ] Each slide has clear visuals (screenshots, diagrams, tables)
- [ ] Slides are not text-heavy
- [ ] Practiced presentation (8-10 minutes)

### Demo Video Script
- [ ] Used final-submission/demo-video-script.md
- [ ] Recorded demo video following 19-step flow
- [ ] Video shows: camera capture, gallery selection, cloud prediction, offline prediction, history, detail view, delete, share, notification
- [ ] Included viva framing paragraph at beginning or end
- [ ] Video is 3-5 minutes long
- [ ] Video has clear audio and screen recording
- [ ] Uploaded video (YouTube, Google Drive, or repository LFS)

### Submission Checklist
- [ ] All items in final-submission/submission-checklist.md completed
- [ ] Android source code pushed to GitHub
- [ ] FastAPI backend source code pushed to GitHub
- [ ] Model file or model source documented
- [ ] TFLite model and labels.txt in repository
- [ ] diseases.xml in repository
- [ ] Sample test images in repository
- [ ] APK file included (in releases or evidence folder)
- [ ] README.md complete
- [ ] Proposal document
- [ ] Architecture diagram
- [ ] Test case table
- [ ] Final report PDF
- [ ] Presentation slides PDF
- [ ] Demo video (link or file)
- [ ] GitHub repository link ready

### Viva Preparation
- [ ] Reviewed docs/viva-questions.md (60+ questions)
- [ ] Practiced explaining architecture diagram
- [ ] Prepared to explain each feature and why it satisfies syllabus topic
- [ ] Prepared viva framing statement (mobile app project, not just AI)
- [ ] Prepared to discuss limitations and future work
- [ ] Prepared to explain code snippets on demand
- [ ] Practiced 5-minute demo of app
- [ ] Ready to discuss testing results

### Final Verification
- [ ] Reviewed validation/final-definition-of-done.md
- [ ] All 15+ CSE 2206 topics covered
- [ ] All required features implemented or attempted
- [ ] All documentation complete
- [ ] All evidence saved and organized
- [ ] Repository is clean and professional
- [ ] Ready to submit

### Evidence Saved
- [ ] Final polished screenshots of all screens
- [ ] APK file saved
- [ ] Demo video file or link
- [ ] Final report PDF
- [ ] Presentation slides PDF
- [ ] Complete evidence folder with all 12 weeks

---

## Final Definition of Done

**You are ready to submit when ALL of the following are true:**

✅ **Code Complete**
- [ ] Android app builds without errors
- [ ] FastAPI backend runs without errors
- [ ] All features implemented or documented as attempted

✅ **Features Working**
- [ ] Camera and gallery image input
- [ ] Cloud prediction with FastAPI
- [ ] Offline prediction with TFLite (or documented attempt)
- [ ] Room database scan history
- [ ] XML disease library
- [ ] Share intent
- [ ] Reminder notification
- [ ] Location tagging (attempted and documented)

✅ **Testing Complete**
- [ ] Test case table with 20+ tests
- [ ] Debugging log captured
- [ ] Edge cases tested
- [ ] Latency comparison documented

✅ **Documentation Complete**
- [ ] README polished
- [ ] Final report PDF
- [ ] Presentation slides
- [ ] Demo video
- [ ] All evidence organized

✅ **Deployment**
- [ ] APK built and tested
- [ ] GitHub repository clean and professional
- [ ] All files committed and pushed

✅ **Viva Ready**
- [ ] Can explain architecture
- [ ] Can explain each feature
- [ ] Can discuss limitations
- [ ] Can run live demo

---

## Weekly Progress Tracking

Use this section to track when you completed each week:

| Week | Topic | Started | Completed | Status |
|------|-------|---------|-----------|--------|
| 01 | Project Understanding | | | ⬜ |
| 02 | Android Basics and UI | | | ⬜ |
| 03 | Camera and Gallery | | | ⬜ |
| 04 | FastAPI Backend | | | ⬜ |
| 05 | Android Networking | | | ⬜ |
| 06 | Cloud ML Model | | | ⬜ |
| 07 | Room Database | | | ⬜ |
| 08 | XML Disease Library | | | ⬜ |
| 09 | TensorFlow Lite | | | ⬜ |
| 10 | Notifications and Share | | | ⬜ |
| 11 | Testing and Debugging | | | ⬜ |
| 12 | Final Submission | | | ⬜ |

---

## Notes

- Mark items with ✅ or [x] when complete
- If you skip an item, document why in your weekly reflection
- If a test fails, fix it before moving to next week
- Save this checklist and update it weekly
- Reference specific validation checklists in each week folder for more detail

**Remember**: This is a learning journey. Complete each step thoroughly before moving forward.
