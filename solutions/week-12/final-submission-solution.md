# Week 12 Solution — Final Submission

---

## 1. Generate Signed Release APK (Step-by-Step)

### Step 1: Prepare release build variant

In Android Studio:
1. Click **Build** menu → **Generate Signed Bundle / APK**
2. Select **APK** → click **Next**
3. Under "Key store path" click **Create new...**

### Step 2: Create a new keystore

```
Key store path:   /path/to/leafguard-release.jks   (save somewhere SAFE, not in repo)
Password:         [create a strong password]
Alias:            leafguard-key
Key password:     [same or different strong password]
Validity:         25 years
Certificate info: fill your name / organization
```

### Step 3: Generate APK

- Build Variants: **release**
- Signature Versions: check **V1** and **V2**
- Click **Finish**

APK location: `android-app/app/release/app-release.apk`

### Step 4: Test on physical device

```bash
# Install via ADB
adb install android-app/app/release/app-release.apk

# Verify installation
adb shell pm list packages | grep leafguard
```

---

## 2. proguard-rules.pro for LeafGuard AI

```proguard
# Keep all model/data classes used with Gson
-keepclassmembers class com.leafguard.data.model.** { *; }
-keepclassmembers class com.leafguard.data.remote.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**

# Gson
-keep class com.google.gson.** { *; }
-keepattributes *Annotation*
-keepattributes EnclosingMethod

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**

# TensorFlow Lite
-keep class org.tensorflow.lite.** { *; }
-dontwarn org.tensorflow.lite.**

# Disease and ScanHistory data classes (XML + Room)
-keep class com.leafguard.data.model.Disease { *; }
-keep class com.leafguard.data.local.ScanHistory { *; }

# Keep Activity names (required for navigation)
-keep public class * extends android.app.Activity
```

---

## 3. app/build.gradle Release Configuration

```gradle
android {
    compileSdk 34

    defaultConfig {
        applicationId "com.leafguard.ai"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0.0"
    }

    signingConfigs {
        release {
            storeFile file("../leafguard-release.jks")  // path to keystore
            storePassword System.getenv("KEYSTORE_PASS") ?: "your_store_password"
            keyAlias "leafguard-key"
            keyPassword System.getenv("KEY_PASS") ?: "your_key_password"
        }
    }

    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'),
                         'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
        debug {
            applicationIdSuffix ".debug"
            debuggable true
        }
    }
}
```

> ⚠️ Never commit your keystore password to GitHub.
> Use environment variables or Android Studio's `local.properties`.

---

## 4. Final GitHub Repository README Template

```markdown
# LeafGuard AI — Android Plant Disease Detection

![Android](https://img.shields.io/badge/Platform-Android-green)
![Java](https://img.shields.io/badge/Language-Java-orange)
![FastAPI](https://img.shields.io/badge/Backend-FastAPI-blue)
![TFLite](https://img.shields.io/badge/AI-TFLite-red)

A mobile application that detects plant diseases from leaf photographs using
deep learning, built for the CSE 2206 Mobile Application Development course.

## Features
- 📷 **Camera / Gallery** — capture or select leaf images
- ☁️ **Cloud Detection** — FastAPI backend with TensorFlow model
- 📱 **Offline Detection** — TensorFlow Lite on-device inference
- 💾 **Scan History** — Room SQLite database with search
- 📚 **Disease Library** — XML-based disease knowledge base
- 🔔 **Notifications** — alerts after each detection
- 📤 **Share** — share results via WhatsApp, email, etc.

## Architecture
```
Android App (Java)
    ├── FastAPI Backend (Python) → Cloud AI
    ├── TFLite Model (assets/)  → Offline AI
    ├── Room Database           → Scan History
    └── XML Disease Library     → Disease Info
```

## Setup

### Android App
1. Clone this repository
2. Open `android-app/` in Android Studio
3. Set `BASE_URL` in `RetrofitClient.java` to your backend URL
4. Add `plant_disease_model.tflite` to `app/src/main/assets/` (see `model/model-acquisition-guide.md`)
5. Build and run on device / emulator

### Backend API
```bash
cd backend-api
pip install -r requirements.txt
uvicorn main:app --reload
```

## Project Structure
```
├── android-app/         # Android application (Java)
├── backend-api/         # FastAPI prediction API (Python)
├── model/               # Model documentation and labels
├── roadmap/             # 12-week learning roadmap
├── notebooks/           # Step-by-step tutorials
├── solutions/           # Reference solutions
└── docs/                # Report templates and viva prep
```

## Tech Stack
| Layer | Technology |
|-------|-----------|
| Android UI | Java, Material Design 3 |
| Networking | Retrofit 2 + OkHttp |
| Local DB | Room (SQLite) |
| XML Parsing | XmlPullParser |
| Cloud AI | FastAPI + TensorFlow |
| Offline AI | TensorFlow Lite |
| Image Loading | Glide |

## Course
**CSE 2206 — Mobile Application Development**
Student: [Your Name] | Roll: [Your Roll Number]
```

---

## 5. Viva Q&A — 20 Most Expected Questions

**Q1: What is the overall architecture of your app?**
> "LeafGuard AI is a 3-tier system. The Android app is the frontend built in Java. The FastAPI Python backend handles cloud-based ML inference. I also implemented on-device offline inference using TensorFlow Lite. The Android app communicates with the backend via Retrofit HTTP calls with multipart image upload."

**Q2: Why did you choose FastAPI for the backend?**
> "FastAPI is a modern Python framework that is very fast because it's built on Starlette and uses async I/O. It also auto-generates API documentation at `/docs`, which made testing easy. It has native support for file uploads via `UploadFile`, which is exactly what I needed for image upload."

**Q3: Explain how MVVM is implemented in your app.**
> "In MVVM, the ViewModel holds the UI state and business logic, separate from the Activity. For example, HistoryViewModel exposes LiveData<List<ScanHistory>> which HistoryActivity observes. When the data changes in Room, LiveData automatically notifies the Activity to update the RecyclerView. This survives screen rotation because ViewModels outlive Activity recreation."

**Q4: Why use Room instead of raw SQLite?**
> "Room provides compile-time SQL validation, which catches errors at build time rather than runtime. It also provides LiveData integration for reactive UI updates, and handles background thread management automatically. Raw SQLite requires a lot of boilerplate code and is error-prone."

**Q5: Explain how TFLite inference works in your app.**
> "I load the `.tflite` model file from the assets folder using a MappedByteBuffer. Then I initialize a TFLite Interpreter. For each image, I resize it to 224×224 pixels, normalize pixel values to [0,1], and pack them into a ByteBuffer. I run `interpreter.run(input, output)` which fills an output array of 38 float values — one per disease class. I pick the index with the highest value as the prediction."

**Q6: What is XmlPullParser and how does it work?**
> "XmlPullParser is Android's recommended XML parsing API. It is event-driven and memory-efficient. You call `parser.next()` in a loop and it returns event types: START_TAG when it encounters `<disease>`, TEXT when it encounters text content, and END_TAG when it encounters `</disease>`. I build Disease objects incrementally as I encounter each tag, then add to my list when I reach END_TAG for disease."

**Q7: Why parse XML instead of JSON for the disease library?**
> "XML was chosen as a CSE 2206 requirement to demonstrate XML parsing knowledge. It's also appropriate here because the disease library is static, structured, hierarchical data that benefits from XML's tag-based format. The data is stored in the assets folder — it's read-only, human-readable, and doesn't require a database connection."

**Q8: How does your app handle offline situations?**
> "When Retrofit's `onFailure` callback fires with `UnknownHostException`, I detect that there's no internet connection and automatically fall back to TFLite on-device inference. The offline inference runs on a background thread so the UI stays responsive. The result is the same — disease name and confidence shown in ResultActivity."

**Q9: Explain the FileProvider and why it's needed.**
> "FileProvider is required from Android 7.0 (API 24) onwards. Direct `file://` URIs cannot be shared between apps (enforced by StrictMode). FileProvider converts a file path to a `content://` URI with temporary permissions granted. I configured it in AndroidManifest.xml with a `file_paths.xml` that specifies which directories are shareable."

**Q10: What is LiveData and why use it with Room?**
> "LiveData is a lifecycle-aware observable data holder. When used with Room, the DAO query returns `LiveData<List<ScanHistory>>`. Room automatically updates this LiveData whenever the database changes. The Activity observes this LiveData, and when new data arrives, the observer callback updates the RecyclerView. Lifecycle-awareness means Room stops delivering updates when the Activity is destroyed, preventing memory leaks."

**Q11: How did you prevent memory leaks in bitmap loading?**
> "I used BitmapFactory.Options with `inSampleSize` to load scaled bitmaps. Instead of loading a 4000×3000 image (46MB) into memory, I calculate a power-of-2 sample size that scales it down to approximately 224×224. This reduced memory usage by up to 95%. I also called `bitmap.recycle()` when the bitmap was no longer needed."

**Q12: Explain the Retrofit callback pattern.**
> "Retrofit's `enqueue()` method makes an async HTTP call. It accepts a `Callback<T>` with two methods: `onResponse()` called when the server responds (even with error codes), and `onFailure()` called on network failure. Inside `onResponse()` I check `response.isSuccessful()` for HTTP 200-299 status. All UI updates inside callbacks must use `runOnUiThread()` since callbacks run on background threads."

**Q13: What is the Repository pattern?**
> "The Repository is a class that abstracts data sources (Room database, network API) from the ViewModel. ScanHistoryRepository exposes methods like `insert()`, `getAllScans()`, and `delete()`. The ViewModel calls repository methods without knowing whether data comes from the network or local database. This makes testing easier — I can inject a mock repository in unit tests."

**Q14: How does your app handle screen rotation?**
> "Android destroys and recreates Activities on rotation. ViewModels survive this because they are stored by the ViewModelStore which outlives Activity instances. LiveData observations are re-established in the new Activity instance. For the current scan URI, I saved it in `onSaveInstanceState(Bundle)` and restored it in `onCreate()` so the selected image persists through rotation."

**Q15: What testing did you perform?**
> "I wrote JUnit unit tests for DiseaseXmlParser (parse correctness, empty XML), Room DAO tests using in-memory database (insert, delete, ordering), and Espresso UI tests for MainActivity navigation. I also performed manual testing of 15 test cases covering camera, gallery, online/offline detection, history, and notifications. I used Android Studio Profiler to measure performance."

**Q16: What is the confidence threshold you used and why?**
> "I used 0.5 (50%) as the threshold. Below this, I show a warning that the detection is uncertain. I chose 0.5 because it represents a majority vote among the 38 classes. In production, a higher threshold (0.7-0.85) would be safer, but for a learning project 0.5 is reasonable and reduces false negatives."

**Q17: How do you handle images taken in portrait vs landscape?**
> "Camera apps can embed EXIF orientation metadata in JPEG files. I read this using ExifInterface and rotate the bitmap accordingly before displaying or sending it to the API. Without this fix, images often appear rotated 90° in the app."

**Q18: What would you improve if you had more time?**
> "I would improve the model accuracy by training on a larger, more diverse dataset including Indian crop diseases. I'd add a symptom checker feature where users can input symptoms and get disease suggestions. I'd also add offline map support to visualize disease outbreaks by location using the location data already stored with each scan."

**Q19: Explain the PlantVillage dataset.**
> "PlantVillage is a public dataset of 54,000+ leaf images across 38 disease classes from 14 crops. It was collected under controlled conditions with uniform white backgrounds. This means real-world performance may be lower than dataset accuracy since field images have varied backgrounds, lighting, and angles. I used this dataset because it's the most accessible and well-documented plant disease dataset."

**Q20: What are the limitations of your app?**
> "The main limitations are: first, the model was trained on controlled lab images, so accuracy on field photos with complex backgrounds is lower. Second, the app currently covers only 14 crops — many Indian crops like rice, wheat, and sugarcane are not included. Third, the offline model file increases APK size by 20-30 MB. Finally, the backend requires an internet connection and a running server, which isn't suitable for rural areas without connectivity."

---

## 6. Submission Checklist

```
Pre-Submission Verification
============================
[ ] App installs from signed APK without errors
[ ] All 6 activities open and function correctly
[ ] Camera capture works on physical device
[ ] Gallery selection works  
[ ] Online prediction returns disease name (server running)
[ ] Offline prediction works (server stopped)
[ ] Scan history saves and persists after app restart
[ ] Disease library loads all diseases from XML
[ ] Notification appears after scan
[ ] Share text works
[ ] No crashes in any normal use scenario

GitHub Repository
[ ] README.md is complete with setup instructions
[ ] Code is committed and pushed
[ ] Repository is public (or shared with instructor)
[ ] release APK in repository releases or linked in README
[ ] No API keys or passwords committed

Documents
[ ] Final report PDF finalized (all chapters complete)
[ ] Presentation slides ready (15 slides)
[ ] Demo video recorded and accessible
[ ] Viva preparation document reviewed

Backup
[ ] Keystore file backed up securely (NOT in repo)
[ ] Code backed up on USB/cloud
[ ] Report saved in multiple locations
```


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

- 📝 [Back to Week 12 Exercises](../../roadmap/week-12-final-submission/exercises.md) — Try it yourself first
- 📖 [Week 12 README](../../roadmap/week-12-final-submission/README.md) — Week overview
- 💡 [Solutions Index for Week 12](README.md) — Other solutions this week
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

---
