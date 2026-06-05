# Week 12: Learning Notes — Final Submission

> **Week 12 Goal:** Package everything into a professional deliverable — signed APK,
> polished report, compelling demo video, and confident viva preparation.

---

## 1. Release Build vs Debug Build

Android apps have two main build variants:

| Property | Debug | Release |
|----------|-------|---------|
| Signing | Auto debug keystore | Your production keystore |
| Minification | Off | On (ProGuard) |
| Shrinking | Off | On |
| Debugging enabled | Yes | No |
| Log statements | All visible | Stripped by ProGuard |
| APK size | Larger | Smaller (30–50% reduction) |
| Install via ADB | Yes | Yes (signed) |
| Google Play | Not allowed | Required |

**Why release matters for submission:**
Your instructor will install the APK. If you submit a debug APK, it
won't run on a phone that hasn't "trusted" your debug certificate.
A release APK signed with a proper keystore installs on any Android device.

---

## 2. Generating a Signing Keystore

A keystore is a binary file that stores your signing certificate.
You create it ONCE and use it for the lifetime of your app.

### Step-by-step via Android Studio

1. **Build** menu → **Generate Signed Bundle / APK**
2. Select **APK** → **Next**
3. Click **Create new...**

Fill in the keystore dialog:
```
Key store path:  ~/leafguard-release.jks       (save OUTSIDE the repo)
Password:        [strong password — remember it forever]

Key alias:       leafguard-key
Key password:    [same or different password]
Validity:        25 (years)

Certificate:
  First/Last:    [Your Name]
  Organization:  [Your University]
  Country code:  BD  (or your country)
```

4. Back in the wizard:
   - Build Variants: **release**
   - Signature Versions: check **V1 (Jar Signature)** AND **V2 (Full APK Signature)**
5. Click **Finish**

APK saved to: `android-app/app/release/app-release.apk`

### ⚠️ Keystore Rules
- **Never commit the `.jks` file to GitHub** — it's your identity
- **Never lose it** — without the keystore, you cannot update the app
- **Back it up** to USB + Google Drive + email to yourself
- Add `*.jks` to `.gitignore`

---

## 3. ProGuard Rules for LeafGuard AI

ProGuard minifies your code (shortens class names, removes unused code).
Without proper rules, it will break your app.

Key rules needed for LeafGuard:

```proguard
# Keep data model classes (Gson needs field names)
-keepclassmembers class com.leafguard.data.model.** { *; }
-keepclassmembers class com.leafguard.data.remote.** { *; }

# Retrofit 2
-keepattributes Signature, Exceptions, *Annotation*
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**

# Gson
-keep class com.google.gson.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.**

# TensorFlow Lite
-keep class org.tensorflow.lite.** { *; }
-dontwarn org.tensorflow.lite.**

# Keep all Activities (Android requires their names)
-keep public class * extends android.app.Activity

# Keep XML parser (used by XmlPullParser)
-dontwarn org.xmlpull.v1.**
```

**How to test ProGuard worked:**
After generating release APK, install it and test ALL features.
If something breaks in release but works in debug → ProGuard stripped it → add a `-keep` rule.

---

## 4. app/build.gradle Release Configuration

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
            storeFile file("../leafguard-release.jks")
            storePassword "your_keystore_password"
            keyAlias "leafguard-key"
            keyPassword "your_key_password"
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

**versionCode vs versionName:**
- `versionCode`: Integer, incremented on every release (1, 2, 3...)
- `versionName`: Human-readable (1.0.0, 1.0.1, 2.0.0)

---

## 5. Final Project Report Structure

Your CSE 2206 project report should follow this structure:

### Chapter 1: Introduction (4–6 pages)
- 1.1 Problem Statement — plant diseases cause X% crop loss globally
- 1.2 Objectives — what the app achieves
- 1.3 Scope — what's included and excluded
- 1.4 Motivation — why an Android app vs web vs desktop

### Chapter 2: Literature Review (5–8 pages)
- 2.1 Plant Disease Detection Methods (traditional vs AI)
- 2.2 Mobile Application Development Frameworks (Android vs iOS vs cross-platform)
- 2.3 TensorFlow Lite — mobile ML overview
- 2.4 Related Work (2–3 similar apps/papers)

### Chapter 3: Methodology / System Design (8–12 pages)
- 3.1 System Architecture (diagram: Android ↔ FastAPI ↔ TFLite model)
- 3.2 Module Breakdown (6 activities + backend + model)
- 3.3 Database Design (ScanHistory table ER diagram)
- 3.4 XML Disease Library Design
- 3.5 AI/ML Pipeline (image → preprocessing → model → label → XML lookup)
- 3.6 Technology Stack Justification

### Chapter 4: Implementation (12–18 pages)
- 4.1 Project Setup and Environment (Week 01)
- 4.2 UI/UX Design (Week 02)
- 4.3 Camera and Image Capture (Week 03)
- 4.4 UI Polishing (Week 04)
- 4.5 Networking and API Integration (Week 05)
- 4.6 ML Model Integration (Week 06)
- 4.7 Scan History with Room (Week 07)
- 4.8 XML Disease Library (Week 08)
- 4.9 Offline TFLite Detection (Week 09)
- 4.10 Notifications and Sharing (Week 10)
- 4.11 Testing and Debugging (Week 11)

### Chapter 5: Testing (6–10 pages)
- 5.1 Test Plan
- 5.2 Unit Testing Results (JUnit)
- 5.3 UI Testing Results (Espresso)
- 5.4 Manual Test Cases (table: test ID, step, expected, actual, pass/fail)
- 5.5 Performance Analysis (memory, CPU, startup time)
- 5.6 Known Issues and Limitations

### Chapter 6: Conclusion (3–5 pages)
- 6.1 Summary of Achievements
- 6.2 Limitations
- 6.3 Future Work (improvements, additional crops, cloud sync)
- 6.4 Learning Outcomes (how this project taught you mobile dev skills)

### References
- IEEE/APA format, 10–20 references

### Appendix
- A: Full test case table
- B: Screenshots of all screens
- C: API documentation (Swagger UI screenshot)
- D: GitHub repository link

---

## 6. Demo Video Script (5–7 minutes)

### Intro (30 seconds)
> "This is LeafGuard AI, an Android application for detecting plant diseases
> using deep learning. It was built as part of CSE 2206 Mobile Application
> Development at [University]. The app uses a combination of cloud-based
> FastAPI prediction and offline TensorFlow Lite inference."

### Feature 1: Camera Capture (60 seconds)
1. Open app → show SplashActivity and MainActivity
2. Tap "Scan" → ScanActivity opens
3. Tap camera button → take photo of a leaf
4. Show the captured image appears in preview

### Feature 2: Cloud Prediction (60 seconds)
1. With captured image → tap "Detect Online"
2. Show loading indicator
3. ResultActivity appears with: disease name, scientific name, symptoms, treatment
4. Point out severity color (red for High)

### Feature 3: Offline Prediction (45 seconds)
1. Enable Airplane Mode on device
2. Capture another leaf image
3. Tap "Detect Offline"
4. Show TFLite result (same format as cloud)
5. "No internet needed — works in remote farms"

### Feature 4: Scan History (45 seconds)
1. Navigate to HistoryActivity
2. Show list of past scans with date/time
3. Tap a scan → details shown
4. Long-press → delete option

### Feature 5: Disease Library (45 seconds)
1. Navigate to DiseaseLibraryActivity
2. Show list of 15+ diseases
3. Search "tomato" → list filters
4. Tap a disease → show details

### Feature 6: Share & Notify (30 seconds)
1. In ResultActivity → tap Share
2. Show Android share sheet opening
3. Send result via WhatsApp
4. Show notification in status bar

### Tech Stack Summary (45 seconds)
> "The app uses Java on Android 8.0+, Retrofit for API calls, Room SQLite
> for history, XmlPullParser for the disease library, and TFLite for offline
> inference. The backend is FastAPI with TensorFlow serving the PlantVillage
> dataset model with 38 plant disease classes."

### Closing (30 seconds)
> "In 12 weeks, I implemented all 6 activities, REST API integration, offline
> ML inference, a persistent scan history database, XML-based disease knowledge,
> and a notification system. Thank you."

---

## 7. Viva Preparation — 30 Expected Questions

### Core Android Questions
1. What is an Activity lifecycle? Describe onCreate → onPause → onStop → onDestroy.
2. What is the difference between `startActivity()` and `startActivityForResult()`?
3. What is AndroidManifest.xml and why is it important?
4. Explain how RecyclerView is more efficient than ListView.
5. What is a ViewHolder pattern and why is it used?
6. Explain ViewModel + LiveData in the context of your app.
7. What is MVVM and how did you implement it?

### Networking Questions
8. Explain Retrofit's `@Multipart` and `@Part` annotations.
9. What is the difference between `enqueue()` and `execute()` in Retrofit?
10. How does your app handle network failures gracefully?
11. What is `UnknownHostException` and when does it occur?
12. What is `BASE_URL` and why must it end with `/`?

### Room Database Questions
13. What is a DAO? What annotations does your DAO use?
14. Why does Room require queries on background threads?
15. What is a Migration and when do you need one?
16. How does Room's LiveData integration work?
17. What is `@Entity` and what does `primaryKeys` mean?

### XML Parsing Questions
18. Compare DOM, SAX, and XmlPullParser. Why did you choose XmlPullParser?
19. What events does XmlPullParser generate?
20. What is `parser.getText()` and when can it return null?
21. Why store the disease library in XML rather than hardcoded Java strings?

### Machine Learning Questions
22. What is TensorFlow Lite and how does it differ from full TensorFlow?
23. What is the input format for your TFLite model (shape, type, normalization)?
24. How do you convert float probabilities to a predicted class label?
25. What is the PlantVillage dataset?
26. How does your app handle the case where the model confidence is below 50%?

### Testing Questions
27. What is the difference between unit tests and UI tests?
28. Why use an in-memory database for Room unit tests?
29. What is Espresso and how does it test UI?
30. Name one bug you found during testing and how you fixed it.

---

## 8. GitHub Repository Cleanup Checklist

Before submission, clean up your repository:

```
[ ] README.md updated with final features list
[ ] All feature branches merged to main
[ ] No uncommitted local changes (git status is clean)
[ ] No sensitive data committed (.jks, API keys, passwords)
[ ] .gitignore covers: *.jks, local.properties, .gradle/, build/, __pycache__/
[ ] Release APK attached to a GitHub Release (tag v1.0.0)
[ ] requirements.txt present for backend
[ ] All solution files reviewed and accurate
```

**Create a release tag:**
```bash
git tag -a v1.0.0 -m "CSE 2206 Final Submission"
git push origin v1.0.0
```
Then on GitHub: Releases → Create a new release → attach the .apk file.

---

## 9. Performance Optimization Before Submission

Quick wins to improve performance in the last sprint:

### Bitmap Memory
```java
// Load scaled bitmap for display (don't load 4000x3000 full resolution)
BitmapFactory.Options opts = new BitmapFactory.Options();
opts.inSampleSize = 4; // Load at 1/4 resolution for display
Bitmap scaled = BitmapFactory.decodeFile(imagePath, opts);
```

### RecyclerView
```java
// Enable fixed size if all items are same height
recyclerView.setHasFixedSize(true);

// Use DiffUtil for efficient list updates
DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffCallback(oldList, newList));
result.dispatchUpdatesTo(adapter);
```

### Startup Speed
- Move XML parsing to app startup (Application class `onCreate()`) rather than first Activity
- Use Glide for all image loading (has built-in disk+memory cache)

### App Size
- Release build with `minifyEnabled true` + `shrinkResources true` reduces APK by 30–50%
- Check APK size via: **Build** → **Analyze APK**

---

## 10. Final Week Timeline

| Day | Task |
|-----|------|
| Day 1 | Generate signed APK, test on physical device |
| Day 2 | Write/finalize Chapters 3 and 4 of report |
| Day 3 | Record demo video, clean up GitHub repo |
| Day 4 | Complete Chapters 5 and 6, add appendices |
| Day 5 | Prepare presentation slides (15 slides) |
| Day 6 | Full viva practice session (answer all 30 questions aloud) |
| Day 7 | Final review, submit, rest |

---

**You have built a complete, production-quality Android application. Week 12 is about
presenting that work with the professionalism it deserves. Good luck! 🌱**
