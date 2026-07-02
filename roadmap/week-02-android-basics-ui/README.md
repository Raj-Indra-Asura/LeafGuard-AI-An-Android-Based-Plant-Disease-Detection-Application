# Week 02: Android Studio Setup, Project Structure & UI Skeleton

## What You'll Learn & Why

This week you'll set up Android Studio, create the LeafGuard project, and build six screens (Activities) that form the app's skeleton. You'll learn how Android organizes code and resources, how Activities represent screens, and how Intents let screens communicate. By the end, you'll have a running app on an emulator — the foundation every future week builds upon.

## New Words This Week

> For complete definitions, see the [GLOSSARY](../../GLOSSARY.md).

| Term | Quick Definition |
|------|------------------|
| **Activity** | One screen of your app; Android creates, shows, and destroys Activities as users navigate. |
| **Layout (XML)** | An XML file describing the visual structure of a screen — which buttons, text, and images appear and where. |
| **View / Widget** | A single UI element: `TextView` shows text, `Button` handles taps, `ImageView` displays pictures. |
| **ConstraintLayout** | A flexible layout that positions views relative to each other or the screen edges using constraints. |
| **Intent** | A messaging object used to request an action — typically starting a new Activity or passing data between screens. |
| **Gradle** | Android's build tool; it compiles code, downloads libraries (dependencies), and packages your APK. |
| **Emulator** | A virtual Android device running on your computer for testing without a physical phone. |
| **Material Design** | Google's design system providing ready-made UI components (buttons, cards, themes) for a polished look. |
| **APK** | Android Package — the installable file produced when you build your app. |

---

## Weekly Objective

By the end of Week 02, you will:

1. **Install and configure Android Studio** with all required SDKs, build tools, and emulator (a virtual Android device running on your computer) setup
2. **Create a production-ready Android project** with proper package structure and naming conventions
3. **Implement 6 core Activities** with basic XML layouts demonstrating Android UI principles. An **Activity** = one screen of the app.
4. **Establish navigation flow** between Activities using Intents (messaging objects that start screens and pass data) and understanding the back stack
5. **Understand the Gradle build system** — Gradle compiles your code, downloads libraries, and packages your APK (Android Package, the installable file)
6. **Master Android project structure** including manifests, resources, and source organization
7. **Run and debug on emulator and real device** with proper ADB (Android Debug Bridge — a command-line tool to communicate with devices) configuration

**Measurable Outcomes:**
- Android Studio installed with SDK 24-34 support
- LeafGuard Android project created with correct package structure (`com.leafguard`)
- 6 Activities implemented: **MainActivity**, **ResultActivity**, **HistoryActivity**, **HistoryDetailActivity**, **DiseaseLibraryActivity**, **SettingsActivity**
- Navigation working between all Activities with proper Intent extras
- Application running on emulator or physical device
- First APK generated and installable
- Git repository updated with Android project code

---

## Why This Week Matters

### Connection to CSE 2206 Mobile Application Development

Week 02 is where theory becomes practice. This week establishes the entire UI foundation for LeafGuard AI. Without proper understanding of Android basics, you will:
- Create poorly structured projects that are hard to maintain
- Misunderstand the Activity lifecycle causing memory leaks
- Build layouts that do not adapt to different screen sizes
- Struggle with navigation and data passing between screens
- Face build errors you cannot debug

**This week ensures:**
- You build on solid Android fundamentals, not guesswork
- Your project structure follows industry best practices
- You understand what every folder and file does
- You can debug build issues independently
- Your UI foundation is ready for feature implementation

### Academic Requirement Alignment

CSE 2206 expects demonstration of:
1. **Android Studio proficiency:** Creating projects, understanding IDE features
2. **Activity lifecycle:** onCreate, onStart, onResume, onPause, onStop, onDestroy
3. **XML layouts:** ConstraintLayout, LinearLayout, RelativeLayout
4. **UI components:** TextView, EditText, Button, ImageView, RecyclerView preparation
5. **Intents:** Explicit intents for navigation, passing data with extras
6. **Manifest configuration:** Declaring activities, permissions, app metadata
7. **Resource management:** Drawables, strings, colors, dimensions
8. **Gradle build system:** Dependencies, plugins, build configuration

LeafGuard's 6 Activities provide rich demonstration of all these topics. Week 02 creates the skeleton that Week 03-12 will fill with functionality.

> **Key concept:** In Android, an **Activity** represents one screen. LeafGuard has exactly six Activities — each one is a separate class and XML layout file.

---

## Syllabus Topics Covered This Week

### Direct Coverage

1. **Android Studio IDE**
   - Project creation wizard
   - Layout editor and design view
   - Logcat for debugging
   - AVD Manager for emulators

2. **Activity Lifecycle**
   - Understanding the 6 lifecycle methods
   - State preservation on rotation
   - Proper resource initialization and cleanup
   - Demonstrating lifecycle with Log statements

3. **XML Layout Design**
   - ConstraintLayout for flexible positioning
   - View hierarchy and nesting
   - Attributes: layout_width, layout_height, padding, margin
   - Using layout editor and code view

4. **Intent Navigation**
   - Explicit intents for activity switching
   - Passing data with putExtra and getExtra
   - Understanding the activity back stack
   - Implementing navigation buttons

5. **AndroidManifest.xml**
   - Declaring activities
   - Setting launcher activity
   - Configuring app name and icon
   - Preparing for permissions (Week 03)

6. **Gradle Build System**
   - Understanding build.gradle (Project vs App)
   - Adding dependencies
   - Setting minSdk, targetSdk, compileSdk
   - Build variants (debug vs release)

### Indirect Preparation

- Material Design principles (theming, colors, typography)
- Resource qualifiers for different screen sizes
- String externalization for internationalization
- Drawable resources preparation for icons
- Fragment architecture understanding (Week 08)

---

## Prerequisites

### Required Knowledge

1. **Week 01 Completion:**
   - Project proposal finalized
   - Architecture diagram created
   - Understanding of LeafGuard scope
   - Syllabus mapping completed

2. **Java/Kotlin Basics:**
   - Classes, objects, methods
   - Variables, data types, conditionals
   - Arrays and ArrayList
   - Basic OOP concepts

3. **XML Basics:**
   - Understanding tags, attributes, nesting
   - XML syntax rules
   - No need for advanced XML knowledge

### Required Tools

1. **Android Studio:** Version 2022.2.1 or later (latest stable recommended)
2. **JDK:** Version 11 or later (usually bundled with Android Studio)
3. **System Requirements:**
   - Windows 10/11, macOS 10.14+, or Ubuntu 18.04+
   - 8 GB RAM minimum (16 GB recommended)
   - 10 GB free disk space (SSD recommended)
   - Virtualization enabled in BIOS (for emulator)

4. **Optional but Recommended:**
   - Android physical device with Developer Options enabled
   - USB cable for device connection
   - ADB drivers installed (Windows)

### Recommended Pre-Reading

- Android Developer: Getting Started Guide
- Activity Introduction: https://developer.android.com/guide/components/activities/intro-activities
- Layouts Overview: https://developer.android.com/guide/topics/ui/declaring-layout
- ConstraintLayout Guide: https://developer.android.com/develop/ui/views/layout/constraint-layout

---

## Concepts to Learn

### 1. Android Project Structure

**What it is:** The organization of folders and files in an Android project.

**Key Folders:**

```
LeafGuard/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/leafguard/
│   │   │   │       ├── activities/       (Activity classes)
│   │   │   │       ├── adapters/         (RecyclerView adapters - Week 07)
│   │   │   │       ├── models/           (Data classes - Week 05)
│   │   │   │       ├── viewmodels/       (ViewModels - Week 05)
│   │   │   │       ├── repositories/     (Data layer - Week 05)
│   │   │   │       ├── database/         (Room - Week 07)
│   │   │   │       ├── network/          (Retrofit - Week 05)
│   │   │   │       └── utils/            (Helper classes)
│   │   │   ├── res/
│   │   │   │   ├── layout/               (XML layouts)
│   │   │   │   ├── drawable/             (Icons, images)
│   │   │   │   ├── values/               (strings, colors, dimens)
│   │   │   │   └── mipmap/               (App icons)
│   │   │   └── AndroidManifest.xml
│   │   └── test/ (unit tests)
│   ├── build.gradle (app level)
│   └── proguard-rules.pro
├── build.gradle (project level)
├── gradle.properties
├── settings.gradle
└── .gitignore
```

**Why folders are organized this way:**
- `java/` contains all code separated by responsibility
- `res/` contains all resources (layouts, images, strings)
- `AndroidManifest.xml` declares all app components
- Gradle files manage dependencies and build configuration

**LeafGuard Package Structure (Kotlin-primary, Java secondary):**

```
com.leafguard/
├── activities/
│   ├── MainActivity.kt            (Home + capture screen)
│   ├── ResultActivity.kt          (Disease result display)
│   ├── HistoryActivity.kt         (Scan history list)
│   ├── HistoryDetailActivity.kt   (Details of one scan)
│   ├── DiseaseLibraryActivity.kt  (Offline disease encyclopedia)
│   └── SettingsActivity.kt        (App settings)
```

> **Note:** The Kotlin source lives in `android-app-kotlin/`. A secondary Java twin exists in `android-app/` for reference.

### 2. Activity Lifecycle

**What it is:** The sequence of method calls from activity creation to destruction.

**The 6 Lifecycle Methods:**

```
Activity Created
    ↓
onCreate()       → Initialize views, set layout, restore state
    ↓
onStart()        → Activity becoming visible
    ↓
onResume()       → Activity in foreground, user can interact
    ↓
[User interacts with app]
    ↓
onPause()        → Another activity comes to foreground
    ↓
onStop()         → Activity no longer visible
    ↓
onDestroy()      → Activity being destroyed, cleanup resources
    ↓
Activity Destroyed
```

**Common Scenarios:**

**App Launch:**
```
onCreate() → onStart() → onResume()
[App is running]
```

**Screen Rotation:**
```
onPause() → onStop() → onDestroy()
    ↓
onCreate() → onStart() → onResume()
[Activity recreated with new configuration]
```

**Navigate to Another Activity:**
```
Current Activity: onPause() → onStop()
New Activity: onCreate() → onStart() → onResume()
```

**Press Back Button:**
```
onPause() → onStop() → onDestroy()
Previous Activity: onStart() → onResume()
```

**LeafGuard Example (Kotlin — primary):**
```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        val historyButton: Button = findViewById(R.id.btnHistory)

        // Setup click listener — navigate to HistoryActivity
        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called - activity in foreground")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause called - activity losing focus")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop called - activity not visible")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy called - activity being destroyed")
    }
}
```

<details>
<summary>Java (secondary)</summary>

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        Button historyButton = findViewById(R.id.btnHistory);

        // Setup click listener — navigate to HistoryActivity
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume called - activity in foreground");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause called - activity losing focus");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop called - activity not visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy called - activity being destroyed");
    }
}
```
</details>

### 3. XML Layouts and ConstraintLayout

**What it is:** XML files defining the UI structure and appearance.

**ConstraintLayout Advantages:**
- Flat view hierarchy (better performance)
- Flexible positioning using constraints
- Responsive to different screen sizes
- Replaces nested LinearLayouts

**Basic ConstraintLayout Structure:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- Views with constraints go here -->

</androidx.constraintlayout.widget.ConstraintLayout>
```

**Constraint Types:**
- `layout_constraintTop_toTopOf` - Align top edge
- `layout_constraintBottom_toBottomOf` - Align bottom edge
- `layout_constraintStart_toStartOf` - Align left/start edge
- `layout_constraintEnd_toEndOf` - Align right/end edge
- `layout_constraintTop_toBottomOf` - Position below another view
- `layout_constraintStart_toEndOf` - Position to the right of another view

**LeafGuard MainActivity Layout Example:**
```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <TextView
        android:id="@+id/tvTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/app_name"
        android:textSize="24sp"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="32dp"/>

    <Button
        android:id="@+id/btnScan"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@string/scan_leaf"
        app:layout_constraintTop_toBottomOf="@id/tvTitle"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="24dp"/>

    <Button
        android:id="@+id/btnHistory"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@string/view_history"
        app:layout_constraintTop_toBottomOf="@id/btnScan"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="16dp"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 4. Intent Navigation and Data Passing

**What it is:** The mechanism to navigate between activities and pass data.

**Types of Intents:**

**Explicit Intent (direct navigation — Kotlin):**
```kotlin
// Navigate from MainActivity to HistoryActivity
val intent = Intent(this, HistoryActivity::class.java)
startActivity(intent)
```

**Passing Data with Extras (Kotlin):**
```kotlin
// Sender: MainActivity sends data to ResultActivity
val intent = Intent(this, ResultActivity::class.java).apply {
    putExtra("disease_name", "Tomato Early Blight")
    putExtra("confidence", 0.92f)
    putExtra("image_path", "/storage/leaf.jpg")
}
startActivity(intent)
```

```kotlin
// Receiver: ResultActivity retrieves the extras
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_result)

    // Retrieve data
    val diseaseName = intent.getStringExtra("disease_name") ?: "Unknown"
    val confidence = intent.getFloatExtra("confidence", 0.0f)
    val imagePath = intent.getStringExtra("image_path") ?: ""

    // Use data to update UI
    val tvDisease: TextView = findViewById(R.id.tvDiseaseName)
    tvDisease.text = diseaseName
}
```

<details>
<summary>Java (secondary)</summary>

```java
// Navigate from MainActivity to HistoryActivity
Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
startActivity(intent);

// Sender (MainActivity.java)
Intent intent = new Intent(MainActivity.this, ResultActivity.class);
intent.putExtra("disease_name", "Tomato Early Blight");
intent.putExtra("confidence", 0.92f);
intent.putExtra("image_path", "/storage/leaf.jpg");
startActivity(intent);

// Receiver (ResultActivity.java)
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);

    // Retrieve data
    String diseaseName = getIntent().getStringExtra("disease_name");
    float confidence = getIntent().getFloatExtra("confidence", 0.0f);
    String imagePath = getIntent().getStringExtra("image_path");

    // Use data to update UI
    TextView tvDisease = findViewById(R.id.tvDiseaseName);
    tvDisease.setText(diseaseName);
}
```
</details>

**Data Types Supported:**
- `putExtra(String key, String value)` - Strings
- `putExtra(String key, int value)` - Integers
- `putExtra(String key, boolean value)` - Booleans
- `putExtra(String key, float value)` - Floats
- `putExtra(String key, Serializable value)` - Serializable objects
- `putExtra(String key, Parcelable value)` - Parcelable objects (more efficient)

**Activity Back Stack:**
```
User opens app: [MainActivity]
User taps History: [MainActivity, HistoryActivity]
User taps a scan: [MainActivity, HistoryActivity, HistoryDetailActivity]
User presses Back: [MainActivity, HistoryActivity]
User presses Back: [MainActivity]
User presses Back: App closes
```

### 5. AndroidManifest.xml

**What it is:** The configuration file declaring all app components and permissions.

**Essential Elements:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.leafguard">

    <!-- Permissions declared here (Week 03) -->

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.LeafGuard">

        <!-- Launcher Activity (first screen) -->
        <activity
            android:name=".activities.MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Other Activities -->
        <activity
            android:name=".activities.ResultActivity"
            android:exported="false" />

        <activity
            android:name=".activities.HistoryActivity"
            android:exported="false" />

        <activity
            android:name=".activities.HistoryDetailActivity"
            android:exported="false" />

        <activity
            android:name=".activities.DiseaseLibraryActivity"
            android:exported="false" />

        <activity
            android:name=".activities.SettingsActivity"
            android:exported="false" />

    </application>

</manifest>
```

**Key Attributes:**
- `android:name` - Full class name (or relative to package)
- `android:exported="true"` - Activity can be launched by other apps (required for launcher)
- `android:exported="false"` - Activity is internal only
- `<intent-filter>` - Declares how activity can be started
- `MAIN` action + `LAUNCHER` category - Marks the app entry point

### 6. Gradle Build System

**What it is:** Android's build automation tool.

**Two Gradle Files:**

**1. Project-level build.gradle:**
```gradle
// Top-level build file
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.0.2'
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
```

**2. App-level build.gradle:**
```gradle
plugins {
    id 'com.android.application'
}

android {
    namespace 'com.example.leafguard'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.leafguard"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    // AndroidX libraries
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
}
```

**Key Configuration:**
- `compileSdk` - SDK version to compile against (latest stable)
- `minSdk` - Minimum Android version supported (API 24 = Android 7.0)
- `targetSdk` - SDK version app is optimized for
- `versionCode` - Internal version number (increment for updates)
- `versionName` - User-facing version string ("1.0", "2.1")
- `dependencies` - Libraries your app uses

**Understanding Dependency Format:**
```
implementation 'group:artifact:version'
                    ↓         ↓        ↓
implementation 'androidx.appcompat:appcompat:1.6.1'
```

---

## Reading Plan

### Day 1: Android Studio Setup

**Tasks:**
1. Download Android Studio from https://developer.android.com/studio
2. Install Android Studio following official installation guide
3. Run setup wizard:
   - Choose "Standard" installation
   - Download SDK Platform 34, 33, 30, 24
   - Download SDK Build Tools
   - Download Android Emulator
   - Download Intel HAXM or Hypervisor (for emulator acceleration)
4. Create a sample "Hello World" app to verify setup
5. Run sample app on emulator to confirm everything works

**Reading:**
- Install Android Studio: https://developer.android.com/studio/install
- Meet Android Studio: https://developer.android.com/studio/intro

**Verification:**
- Android Studio opens without errors
- SDK Manager shows installed SDKs
- AVD Manager shows at least one emulator
- Sample app runs on emulator

### Day 2: Create LeafGuard Project

**Tasks:**
1. Create new Android Studio project:
   - Choose "Empty Activity" template
   - Name: LeafGuard
   - Package: `com.leafguard`
   - Language: **Kotlin** (primary)
   - Minimum SDK: API 24 (Android 7.0)
2. Explore project structure:
   - Open AndroidManifest.xml
   - Open app/build.gradle.kts (or build.gradle)
   - Explore res/ folder
   - Understand kotlin/ (or java/) folder organization
3. Run default app on emulator
4. Initialize Git in project root
5. Create .gitignore for Android
6. Make first commit

**Reading:**
- Create a project: https://developer.android.com/studio/projects/create-project
- Configure your build: https://developer.android.com/build

**Deliverables:**
- LeafGuard project created and building
- Git repository initialized
- Screenshot of project structure
- First commit: "Week 02: Initialize LeafGuard Android project"

### Day 3: Activity Lifecycle Study

**Reading:**
- Activity Lifecycle: https://developer.android.com/guide/components/activities/activity-lifecycle
- Understand Activity Lifecycle: https://developer.android.com/guide/components/activities/intro-activities

**Tasks:**
1. Add lifecycle methods to MainActivity:
   - onCreate, onStart, onResume, onPause, onStop, onDestroy
2. Add Log.d() statements in each method
3. Run app and observe Logcat
4. Perform actions and observe logs:
   - Launch app (see onCreate → onStart → onResume)
   - Press Home (see onPause → onStop)
   - Return to app (see onStart → onResume)
   - Rotate screen (see complete recreation cycle)
   - Press Back (see onPause → onStop → onDestroy)
5. Document observations in notes

**Expected Log Output:**
```
D/MainActivity: onCreate called
D/MainActivity: onStart called
D/MainActivity: onResume called
[User presses Home]
D/MainActivity: onPause called
D/MainActivity: onStop called
[User returns to app]
D/MainActivity: onStart called
D/MainActivity: onResume called
```

### Day 4: Create 6 Activities

**Tasks:**
1. Create activities package: `kotlin/com/leafguard/activities/` (or `java/com/leafguard/activities/`)
2. Move MainActivity to activities package
3. Create 5 new activities:
   - Right-click activities package → New → Activity → Empty Activity
   - Create: **ResultActivity**, **HistoryActivity**, **HistoryDetailActivity**, **DiseaseLibraryActivity**, **SettingsActivity**
4. Verify all activities declared in AndroidManifest.xml
5. Add basic layouts to each activity (just a TextView with activity name)
6. Test building the project

**Deliverables:**
- 6 activities created in activities package
- 6 layout files created (activity_main.xml, activity_result.xml, activity_history.xml, activity_history_detail.xml, activity_disease_library.xml, activity_settings.xml)
- AndroidManifest.xml updated with all activities
- Project builds without errors

### Day 5: Design XML Layouts

**Reading:**
- Layouts: https://developer.android.com/develop/ui/views/layout/declaring-layout
- ConstraintLayout: https://developer.android.com/develop/ui/views/layout/constraint-layout

**Tasks:**
1. Design MainActivity layout:
   - App logo/title at top
   - "Capture from Camera" button (center) — MainActivity handles capture directly
   - "Choose from Gallery" button (below camera)
   - Cloud/Offline toggle switch
   - "View History" button
   - "Disease Library" button
   - "Settings" button
2. Design ResultActivity layout:
   - ImageView for leaf image (top)
   - TextView for disease name
   - TextView for confidence score
   - TextViews for symptoms, treatment, prevention
   - "Share" button
   - "Save to History" button
3. Design HistoryActivity layout:
   - Title "Scan History"
   - RecyclerView placeholder (just TextView for now)
4. Design HistoryDetailActivity layout:
   - Large ImageView for scanned leaf
   - Full disease info
   - Date of scan
   - Action buttons
5. Design DiseaseLibraryActivity layout:
   - Title "Disease Encyclopedia"
   - RecyclerView of 10 diseases
6. Design SettingsActivity layout:
   - Title "Settings"
   - TextField for "Backend URL" (default: `http://10.0.2.2:8000`)
   - Slider for "Confidence Threshold"

**Deliverables:**
- All 6 layouts designed with ConstraintLayout
- All strings externalized to strings.xml
- Layouts responsive to different screen sizes

### Day 6: Implement Navigation

**Reading:**
- Intents and Intent Filters: https://developer.android.com/guide/components/intents-filters

**Tasks:**
1. MainActivity navigation:
   - "Capture from Camera" / "Choose from Gallery" → capture image, then → ResultActivity (passing image path)
   - "View History" → HistoryActivity
   - "Disease Library" → DiseaseLibraryActivity
   - "Settings" → SettingsActivity
2. ResultActivity navigation:
   - "Share" → share intent to other apps
   - "Save to History" → save scan, then → HistoryActivity
3. HistoryActivity navigation:
   - Tap a scan item → HistoryDetailActivity (pass `EXTRA_SCAN_ID`)
4. Test navigation flow:
   - MainActivity → ResultActivity (after capture)
   - MainActivity → HistoryActivity → HistoryDetailActivity
   - MainActivity → DiseaseLibraryActivity
   - MainActivity → SettingsActivity
5. Verify back button works correctly
6. Add Log statements to verify data passing

**Deliverables:**
- All navigation implemented
- Data passing tested
- Back stack working correctly
- Navigation flow demonstrated in video/screenshots

### Day 7: Testing, Documentation, Evidence

**Tasks:**
1. Test app on emulator:
   - Navigate through all screens
   - Rotate device (test configuration changes)
   - Use back button
   - Close and reopen app
2. Test on real device (if available):
   - Enable USB debugging
   - Connect via USB
   - Run app on device
3. Generate APK:
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Locate APK in app/build/outputs/apk/debug/
4. Document in learning notes:
   - Challenges faced
   - Solutions found
   - Key learnings
5. Create evidence package:
   - Screenshots of all 6 activities
   - Video of navigation flow
   - Git log screenshot
   - APK file saved
6. Complete validation checklist
7. Answer reflection questions

**Deliverables:**
- All testing completed
- Evidence collected
- Learning notes updated
- Validation checklist checked
- Week 02 marked complete in timeline

---

## Additional Resources

### Official Documentation
- Android Developers: https://developer.android.com/
- Android Studio User Guide: https://developer.android.com/studio/intro
- Material Design: https://m3.material.io/

### Video Tutorials
- Android Development for Beginners (Google)
- Android Studio Tutorial (Official)
- ConstraintLayout Deep Dive

### Troubleshooting Resources
- Stack Overflow: https://stackoverflow.com/questions/tagged/android
- Android Issue Tracker: https://issuetracker.google.com/issues?q=componentid:192708
- Reddit r/androiddev: https://www.reddit.com/r/androiddev/

---

## Week Completion Criteria

You may proceed to Week 03 only when:

**Technical Completion:**
- [ ] Android Studio fully installed and configured
- [ ] LeafGuard project created with correct package structure (`com.leafguard`)
- [ ] All 6 activities implemented with layouts
- [ ] Navigation working between all activities
- [ ] App runs on emulator or real device
- [ ] APK generated successfully

**Understanding:**
- [ ] Can explain Activity lifecycle from memory
- [ ] Can create ConstraintLayout without tutorial
- [ ] Can implement Intent navigation independently
- [ ] Can read and modify Gradle files
- [ ] Can debug using Logcat

**Documentation:**
- [ ] Learning notes completed
- [ ] All exercises attempted
- [ ] Build task completed
- [ ] Validation checklist checked
- [ ] Reflection submitted
- [ ] Quiz passed (8/10 minimum)

**Evidence:**
- [ ] Screenshots of all 6 activities saved
- [ ] Navigation video recorded
- [ ] Git commits show progressive work (minimum 5 commits)
- [ ] APK file saved in evidence folder

**Quality:**
- [ ] No build errors
- [ ] No hardcoded strings (all in strings.xml)
- [ ] Consistent naming conventions
- [ ] Clean code with comments

**Next Week Readiness:**
- [ ] Understand how to add buttons and handle clicks
- [ ] Know where to add camera integration code
- [ ] Ready to learn about runtime permissions
- [ ] Prepared to handle image capture and display

---

**Proceed to Week 03: Camera & Gallery Integration only after all criteria are met. Week 02 is your Android foundation - build it solid.**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 02 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| **1** | **README.md** ← *You are here* | **Week Overview & Objectives** |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

*(Start of week)* &nbsp;&nbsp;|&nbsp;&nbsp; **Week Overview & Objectives** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Theory & Learning Notes →](learning-notes.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 01: Project Understanding](../week-01-project-understanding/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 03: Camera & Gallery ➡](../week-03-camera-gallery/README.md) |

---
