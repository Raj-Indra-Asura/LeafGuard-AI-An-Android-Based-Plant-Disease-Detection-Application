# Week 01 Solution - Project Setup, Architecture, and Foundation

Week 01 establishes the Android + FastAPI foundation for **LeafGuard AI**.

---

## 1. Final Project Structure

```text
LeafGuard-AI/
├── android-app/
│   ├── app/
│   │   ├── build.gradle
│   │   ├── proguard-rules.pro
│   │   └── src/
│   │       ├── main/
│   │       │   ├── AndroidManifest.xml
│   │       │   ├── java/com/leafguard/
│   │       │   │   ├── SplashActivity.java
│   │       │   │   ├── MainActivity.java
│   │       │   │   ├── ScanActivity.java
│   │       │   │   ├── ResultActivity.java
│   │       │   │   ├── HistoryActivity.java
│   │       │   │   ├── DiseaseLibraryActivity.java
│   │       │   │   ├── adapter/
│   │       │   │   │   ├── ScanHistoryAdapter.java
│   │       │   │   │   └── DiseaseAdapter.java
│   │       │   │   ├── data/
│   │       │   │   │   ├── local/
│   │       │   │   │   │   ├── AppDatabase.java
│   │       │   │   │   │   ├── ScanHistory.java
│   │       │   │   │   │   └── ScanHistoryDao.java
│   │       │   │   │   ├── model/
│   │       │   │   │   │   ├── Disease.java
│   │       │   │   │   │   └── PredictionResult.java
│   │       │   │   │   ├── remote/
│   │       │   │   │   │   ├── ApiService.java
│   │       │   │   │   │   ├── RetrofitClient.java
│   │       │   │   │   │   └── PredictionResponse.java
│   │       │   │   │   └── repository/
│   │       │   │   │       ├── ScanRepository.java
│   │       │   │   │       └── DiseaseRepository.java
│   │       │   │   ├── ml/
│   │       │   │   │   └── TFLiteClassifier.java
│   │       │   │   ├── parser/
│   │       │   │   │   └── DiseaseXmlParser.java
│   │       │   │   ├── util/
│   │       │   │   │   ├── FileUtils.java
│   │       │   │   │   ├── ImageUtils.java
│   │       │   │   │   ├── NotificationHelper.java
│   │       │   │   │   ├── ShareUtils.java
│   │       │   │   │   └── LocationHelper.java
│   │       │   │   └── viewmodel/
│   │       │   │       ├── ScanViewModel.java
│   │       │   │       └── HistoryViewModel.java
│   │       │   ├── res/
│   │       │   │   ├── layout/
│   │       │   │   ├── values/
│   │       │   │   ├── drawable/
│   │       │   │   ├── mipmap/
│   │       │   │   └── xml/
│   │       │   └── assets/
│   │       │       ├── disease_library.xml
│   │       │       ├── model.tflite
│   │       │       └── labels.txt
│   │       ├── test/
│   │       └── androidTest/
│   ├── build.gradle
│   └── settings.gradle
├── backend-api/
│   ├── main.py
│   ├── model_loader.py
│   ├── config.py
│   ├── requirements.txt
│   └── models/
│       ├── plantvillage_saved_model/
│       ├── plantvillage_float32.tflite
│       ├── plantvillage_int8.tflite
│       └── labels.txt
├── model/
│   └── model-notes.md
└── README.md
```

---

## 2. Exercise Answer: Data Flow Diagram

Use this architecture/data-flow diagram for the Week 01 submission.

```text
+----------------+
|      User      |
+--------+-------+
         |
         | taps Scan / Camera / Gallery
         v
+----------------+
|  MainActivity  |
+--------+-------+
         |
         | explicit Intent
         v
+----------------+
|  ScanActivity  |
+--------+-------+
         |
         | Uri / Bitmap
         v
+----------------+
| ScanViewModel  |
+--------+-------+
         |
         | delegates business logic
         v
+----------------+
| ScanRepository |
+---+--------+---+
    |        |
    |        |
    |        +------------------------------+
    |                                       |
    v                                       v
+-----------+                       +---------------+
| Room DB   |                       | RetrofitClient|
+-----+-----+                       +-------+-------+
      |                                     |
      | save scan history                   | HTTP POST /predict
      v                                     v
+-----------+                       +---------------+
| History UI |                      | FastAPI API    |
+-----------+                       +-------+--------+
                                            |
                                            | numpy tensor
                                            v
                                     +--------------+
                                     | TF / TFLite  |
                                     | ML Model     |
                                     +------+-------+
                                            |
                                            | JSON prediction
                                            v
                                     +--------------+
                                     | ResultActivity|
                                     +--------------+
```

### Arrow labels for the exercise

| From | To | Data Passed |
|---|---|---|
| User | MainActivity | button click |
| MainActivity | ScanActivity | explicit Intent |
| ScanActivity | ScanViewModel | image Uri, mode |
| ScanViewModel | ScanRepository | analysis request |
| ScanRepository | RetrofitClient | multipart image upload |
| RetrofitClient | FastAPI Backend | `POST /predict` |
| FastAPI Backend | TensorFlow Model | preprocessed float tensor |
| FastAPI Backend | RetrofitClient | JSON response |
| ScanRepository | Room Database | `ScanHistory` entity |
| ScanViewModel | ResultActivity | disease, confidence, notes |
| Room Database | HistoryActivity | query result list |

---

## 3. Exercise Answer: Syllabus Mapping Matrix

| # | Syllabus Topic | LeafGuard Component | File/Class Name | Specific Feature | How It Demonstrates Understanding |
|---|---|---|---|---|---|
| 1 | Activities | Presentation layer | `MainActivity.java` | app home screen | lifecycle-aware screen entry point |
| 2 | Explicit Intents | Navigation | `MainActivity.java` | open Scan/History/Library screens | moves between activities with extras |
| 3 | Implicit Intents | Platform integration | `ShareUtils.java` | share diagnosis text/image | uses Android intent resolution |
| 4 | RecyclerView | List UI | `HistoryActivity.java` | scan history list | efficient list rendering with adapter |
| 5 | Retrofit | Remote data | `ApiService.java` | upload image to backend | REST communication with multipart body |
| 6 | Room Database | Local persistence | `AppDatabase.java` | save past scans | ORM-style SQLite access |
| 7 | XML Parsing | Structured local content | `DiseaseXmlParser.java` | disease library parsing | `XmlPullParser` for hierarchical data |
| 8 | JSON Parsing | Network response | `PredictionResponse.java` | parse backend response | maps JSON to Java POJO |
| 9 | Runtime Permissions | Device access | `ScanActivity.java` | camera/gallery permission flow | checks and requests dangerous permissions |
| 10 | Camera Integration | Media capture | `ScanActivity.java` | `TakePicture` contract | camera capture via `FileProvider` |
| 11 | Background Work | Async processing | `ScanRepository.java` | DB/network execution | avoids UI-thread blocking |
| 12 | Notifications | User engagement | `NotificationHelper.java` | scan-complete alert | uses channel + `NotificationCompat` |
| 13 | Shared Preferences | Lightweight settings | `MainActivity.java` | remember cloud/offline mode | stores simple user preference |
| 14 | Material Design | UI styling | `themes.xml` | consistent app theme | follows Material 3 components |
| 15 | MVVM | App architecture | `ScanViewModel.java` | UI/business separation | keeps Activities thin |
| 16 | File Handling | Image storage | `FileUtils.java` | temp capture file creation | safe app-scoped file management |
| 17 | Location Services | Geo-tagging | `LocationHelper.java` | save last known location | integrates fused location provider |
| 18 | TensorFlow Lite | Offline AI | `TFLiteClassifier.java` | offline disease detection | on-device ML inference |

---

## 4. Exercise Answer: Architecture Diagram

```text
Presentation Layer
─────────────────────────────────────────────────────────────
SplashActivity | MainActivity | ScanActivity | ResultActivity
HistoryActivity | DiseaseLibraryActivity | RecyclerView Adapters
        |
        v
ViewModel Layer
─────────────────────────────────────────────────────────────
ScanViewModel | HistoryViewModel
        |
        v
Repository Layer
─────────────────────────────────────────────────────────────
ScanRepository | DiseaseRepository
        |
        +---------------------+
        |                     |
        v                     v
Local Data Layer         Remote Data Layer
────────────────────     ────────────────────────────────────
Room DB                  RetrofitClient
XmlPullParser            ApiService
SharedPreferences        PredictionResponse
FileProvider             FastAPI Backend
        |                     |
        +----------+----------+
                   |
                   v
Backend / ML Layer
─────────────────────────────────────────────────────────────
FastAPI main.py | model_loader.py | PlantVillage model
```

---

## 5. Exercise Answer: Module Breakdown

| Module | Responsibility | Core Files |
|---|---|---|
| UI | show screens and collect user actions | activities, adapters, layouts |
| ViewModel | maintain UI state and coordinate actions | `ScanViewModel`, `HistoryViewModel` |
| Repository | unify remote + local data sources | `ScanRepository`, `DiseaseRepository` |
| Local storage | save scan history | Room entity, DAO, database |
| Remote API | cloud prediction | Retrofit, DTOs, backend API |
| ML | offline prediction | `TFLiteClassifier`, labels, model |
| XML library | disease reference info | `disease_library.xml`, parser, repository |
| Utilities | share, notifications, location, files | helper classes |

---

## 6. `android-app/settings.gradle`

```gradle
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LeafGuard AI"
include ':app'
```

---

## 7. `android-app/build.gradle`

```gradle
plugins {
    id 'com.android.application' version '8.2.2' apply false
}
```

---

## 8. Complete `android-app/app/build.gradle`

```gradle
plugins {
    id 'com.android.application'
}

android {
    namespace 'com.leafguard'
    compileSdk 34

    defaultConfig {
        applicationId "com.leafguard"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        debug {
            buildConfigField "String", "BASE_URL", '"http://10.0.2.2:8000/"'
        }
        release {
            minifyEnabled true
            shrinkResources true
            buildConfigField "String", "BASE_URL", '"https://api.leafguard.example/"'
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding true
        buildConfig true
    }

    packaging {
        resources {
            excludes += ['/META-INF/{AL2.0,LGPL2.1}']
        }
    }

    aaptOptions {
        noCompress 'tflite'
    }
}

dependencies {
    implementation 'androidx.core:core:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'androidx.cardview:cardview:1.0.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-livedata:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-runtime:2.7.0'
    implementation 'androidx.activity:activity:1.8.2'

    implementation 'com.github.bumptech.glide:glide:4.16.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'

    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:okhttp:4.12.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'

    def roomVersion = '2.6.1'
    implementation "androidx.room:room-runtime:${roomVersion}"
    annotationProcessor "androidx.room:room-compiler:${roomVersion}"
    implementation "androidx.room:room-guava:${roomVersion}"

    implementation 'org.tensorflow:tensorflow-lite:2.14.0'
    implementation 'org.tensorflow:tensorflow-lite-support:0.4.4'
    implementation 'org.tensorflow:tensorflow-lite-metadata:0.4.4'

    implementation 'androidx.exifinterface:exifinterface:1.3.7'
    implementation 'androidx.work:work-runtime:2.9.0'
    implementation 'com.google.android.gms:play-services-location:21.1.0'

    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.mockito:mockito-core:5.8.0'
    testImplementation 'androidx.arch.core:core-testing:2.2.0'

    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation 'androidx.test.espresso:espresso-intents:3.5.1'
    androidTestImplementation 'androidx.test:runner:1.5.2'
    androidTestImplementation 'androidx.test:rules:1.5.0'
}
```

### Dependency explanation

| Dependency | Why it is needed |
|---|---|
| Material | modern buttons, cards, app bar |
| ConstraintLayout | flexible responsive XML layout |
| Lifecycle | MVVM with `ViewModel` and `LiveData` |
| Retrofit + Gson | backend API communication |
| Room | local SQLite wrapper |
| Glide | efficient image rendering |
| TensorFlow Lite | offline plant disease detection |
| ExifInterface | image rotation handling |
| WorkManager | future reminders/background jobs |
| Play Services Location | last known location support |
| Mockito/Espresso | unit and UI tests |

---

## 9. Complete `AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-feature
        android:name="android.hardware.camera"
        android:required="false" />

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
    <uses-permission
        android:name="android.permission.READ_EXTERNAL_STORAGE"
        android:maxSdkVersion="32" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:networkSecurityConfig="@xml/network_security_config"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.LeafGuardAI"
        android:usesCleartextTraffic="true">

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_provider_paths" />
        </provider>

        <activity
            android:name=".DiseaseLibraryActivity"
            android:exported="false" />
        <activity
            android:name=".HistoryActivity"
            android:exported="false" />
        <activity
            android:name=".ResultActivity"
            android:exported="false" />
        <activity
            android:name=".ScanActivity"
            android:exported="false" />
        <activity
            android:name=".MainActivity"
            android:exported="false" />
        <activity
            android:name=".SplashActivity"
            android:exported="true"
            android:theme="@style/Theme.LeafGuardAI.Splash">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

---

## 10. `res/xml/file_provider_paths.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <cache-path
        name="cache_images"
        path="images/" />
    <files-path
        name="internal_images"
        path="images/" />
    <external-files-path
        name="captured_images"
        path="Pictures/" />
</paths>
```

---

## 11. Complete Project `README.md`

```md
# LeafGuard AI

LeafGuard AI is a Java-based Android application for plant disease detection developed for **CSE 2206 - Mobile Application Development**.

## Core Features
- capture a plant image from the camera
- choose a leaf image from gallery
- run cloud prediction through a FastAPI backend
- run offline prediction with TensorFlow Lite
- save scan history with Room database
- browse disease details from an XML library
- share results and trigger notifications

## System Architecture
The app follows **MVVM**.

### Presentation Layer
- `SplashActivity`
- `MainActivity`
- `ScanActivity`
- `ResultActivity`
- `HistoryActivity`
- `DiseaseLibraryActivity`

### ViewModel Layer
- `ScanViewModel`
- `HistoryViewModel`

### Repository Layer
- `ScanRepository`
- `DiseaseRepository`

### Data Layer
- **Remote:** FastAPI backend accessed through Retrofit
- **Local:** Room database, XML asset parser, app-scoped file storage

## Technology Stack
- Android (Java)
- FastAPI (Python)
- Room Database
- Retrofit + Gson
- TensorFlow Lite
- Glide
- XmlPullParser
- Material Design 3

## Package Design
```text
com.leafguard
├── data
├── ml
├── parser
├── util
├── viewmodel
└── adapter
```

## Backend Summary
The backend accepts multipart image uploads, preprocesses them into `224x224x3` tensors, runs disease classification, and returns JSON containing:
- disease name
- confidence
- symptoms
- treatment
- prevention

## Why this architecture?
- MVVM keeps UI classes small
- Retrofit and Room are standard Android libraries taught in mobile development courses
- TFLite enables offline support
- XML parsing satisfies the syllabus requirement for structured local data

## Development Milestones
1. Week 01 - architecture and setup
2. Week 02 - UI and navigation
3. Week 03 - camera/gallery
4. Week 04-06 - backend + cloud ML
5. Week 07-08 - history + disease library
6. Week 09 - offline TFLite
7. Week 10-12 - notifications, testing, release, submission
```

---

## 12. Senior Repository Deep-Dive Summary Answer

For the Week 01 exercise that asks for learning from strong repositories, the most important takeaways for LeafGuard AI are:

1. **Organize by feature or layer** rather than dumping every class in one package.
2. **Keep activities thin** and move business logic into repositories and ViewModels.
3. **Never hard-code backend URLs** inside activities; use `BuildConfig` or configuration classes.
4. **Store image files in app-scoped directories** and expose them through `FileProvider`.
5. **Use one source of truth** for disease labels between backend, TFLite, and XML library.

---

## 13. Short Written Answer for Week 01 Submission

LeafGuard AI uses layered MVVM architecture. Activities handle UI, ViewModels hold state, repositories coordinate local and remote data, Room stores history, Retrofit talks to FastAPI, and TensorFlow/TFLite performs disease prediction. Core dependencies are Material, Lifecycle, Retrofit, Room, Glide, TensorFlow Lite, ExifInterface, WorkManager, and Play Services Location. The system is divided into presentation, ViewModel, repository, local data, remote data, ML, XML library, and utility modules.

---

## 14. Final Checklist

- [x] project structure defined
- [x] Gradle dependencies listed
- [x] complete AndroidManifest included
- [x] FileProvider configured
- [x] README provided
- [x] architecture diagram answered
- [x] dependency list answered
- [x] module breakdown answered
