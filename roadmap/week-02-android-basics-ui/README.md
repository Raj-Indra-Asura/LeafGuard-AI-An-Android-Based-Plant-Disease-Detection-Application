# Week 02: Android Studio Setup, Project Structure & UI Skeleton

## What You'll Learn & Why

This week you'll set up Android Studio, create the LeafGuard project, and build the app's Activity-based UI skeleton: five top-level tab screens plus three supporting workflow screens. You'll learn how Android organizes code and resources, how Activities represent screens, and how Intents let screens communicate. By the end, you'll have a running dashboard UI on an emulator — the foundation every future week builds upon.

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
3. **Implement 8 Activity classes** with XML layouts demonstrating Android UI principles. An **Activity** = one screen of the app.
4. **Establish navigation flow** between Activities using Intents (messaging objects that start screens and pass data) and understanding the back stack
5. **Understand the Gradle build system** — Gradle compiles your code, downloads libraries, and packages your APK (Android Package, the installable file)
6. **Master Android project structure** including manifests, resources, and source organization
7. **Run and debug on emulator and real device** with proper ADB (Android Debug Bridge — a command-line tool to communicate with devices) configuration

**Measurable Outcomes:**
- Android Studio installed with SDK 24-34 support
- LeafGuard Android project created with correct package structure (`com.leafguard`)
- 8 Activities implemented: **MainActivity**, **ScanActivity**, **AnalyticsActivity**, **DiseaseLibraryActivity**, **SettingsActivity**, **ResultActivity**, **HistoryActivity**, **HistoryDetailActivity**
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

LeafGuard's 8 Activities provide rich demonstration of all these topics. Five are top-level tabs (Home, Scan, Analytics, Library, About), and three are supporting workflow screens (Result, History, History Detail). Week 02 creates the skeleton that Week 03-12 will extend.

> **Key concept:** In Android, an **Activity** represents one screen. LeafGuard uses one Activity per major screen so beginners can learn navigation with explicit Intents before learning Fragments later.

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

2. **Kotlin Basics** (primary track):
   - _Note: Java basics are sufficient if you are intentionally following the secondary `android-app/` twin._
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
    package="com.leafguard">

    <!-- Permissions declared here (Week 03) -->

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.LeafGuard">

        <!-- Launcher Activity (first screen) -->
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Other Activities -->
        <activity
            android:name=".ResultActivity"
            android:exported="false" />

        <activity
            android:name=".HistoryActivity"
            android:exported="false" />

        <activity
            android:name=".HistoryDetailActivity"
            android:exported="false" />

        <activity
            android:name=".DiseaseLibraryActivity"
            android:exported="false" />

        <activity
            android:name=".SettingsActivity"
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
```

---

## 7. The Current App Design: Dashboard Home + Bottom Navigation

**This is the UI you will actually build for LeafGuard.** Sections 1-6 above teach
the underlying Android concepts (Activities, layouts, Intents, the manifest,
Gradle) using small examples. This section describes the *real* screen design
implemented in `android-app-kotlin/` (and mirrored in `android-app/`), and the
exact steps to recreate it. Build this final dashboard path for your submitted
app — it demonstrates the same concepts (Activities, layouts, Intents) but
produces the actual product UI.

### What the finished UI looks like

- **Home** — a dashboard with a green "Quick Scan" banner, two summary cards
  (History / Library), and a "Technical Features" card. This is `MainActivity`.
- **Scan** — a dashed upload box ("Tap to upload image") that opens the
  camera/gallery chooser; once an image is picked, the Cloud/Offline mode
  toggle and "Detect Disease" button appear. This is the new `ScanActivity`
  (the capture logic that used to live on `MainActivity` moved here).
- **Analytics** — an intentionally blank placeholder tab (`AnalyticsActivity`)
  reserved for a future week's charts.
- **Library** — `DiseaseLibraryActivity` with a search box and a severity chip
  ("high"/"medium"/"low") on each disease card.
- **About** — the existing `SettingsActivity`, reused as the fifth tab.

All five screens share a **bottom navigation bar** (`BottomNavigationView`)
with 5 tabs: Home, Scan, Analytics, Library, About.

### Key concept: one Activity per tab (no Fragments yet)

LeafGuard does **not** use the Jetpack Navigation Component or Fragments —
each tab is simply a separate Activity, exactly like the Activities you
already learned about in Section 2. Tapping a bottom navigation item:

1. Starts the target Activity with an explicit `Intent` (the same
   `Intent(this, TargetActivity::class.java)` pattern from Section 4).
2. Calls `finish()` on the current Activity so the back stack never grows
   past one screen.

This is a deliberate simplification: it reuses only the concepts you already
know from this week (Activities + Intents) instead of introducing Fragments
early. The trade-off is that the system **Back** button exits the app rather
than returning to a previous tab — a limitation you'll remove in a later week
when Fragments + the Navigation Component are introduced.

### Step-by-step: recreate this UI

1. **Create a shared menu resource** at `res/menu/bottom_nav_menu.xml` with
   5 `<item>` entries (`nav_home`, `nav_scan`, `nav_analytics`, `nav_library`,
   `nav_about`), each with an `android:icon` and `android:title`.
2. **Add 5 small vector icon drawables** (`res/drawable/ic_nav_*.xml`) — one
   per tab. Simple single-`<path>` vectors are enough; `BottomNavigationView`
   tints them automatically based on selection state.
3. **Write one small navigation helper** instead of duplicating the
   click-handling code on every screen:
   - Kotlin: an extension function `AppCompatActivity.setupBottomNav(...)` in
     `com/leafguard/ui/BottomNav.kt`.
   - Java: a static `BottomNav.setup(...)` method in `com/leafguard/ui/BottomNav.java`.

   Both do the same thing: register `setOnItemSelectedListener` on the
   `BottomNavigationView`, map each tab's id to its target Activity class,
   `startActivity(...)` + `finish()`, then call `setSelectedItemId(...)` so
   the correct tab shows as selected when the screen first opens.
4. **Add a `BottomNavigationView` to the bottom of every tab's layout**,
   constrained to the bottom of the screen, referencing
   `app:menu="@menu/bottom_nav_menu"`.
5. **Build the Home dashboard layout** (`activity_main.xml`): a `ScrollView`
   containing a title row, a `MaterialCardView` "Quick Scan" banner (green
   background, white text, a button), a horizontal row of two
   `MaterialCardView`s (History / Library), and a "Technical Features" card
   with two tappable rows.
6. **Move the capture/detect flow into a new `ScanActivity`** — copy the
   camera/gallery permission logic, the Cloud/Offline
   `MaterialButtonToggleGroup`, and the "Detect Disease" button out of
   `MainActivity` into `ScanActivity`, with a dashed-border upload
   `FrameLayout` (`res/drawable/bg_dashed_upload.xml`, a `<shape>` with a
   dashed `<stroke>`) as the tap target.

   > **Gotcha:** dashed strokes don't reliably render with hardware
   > acceleration. Call
   > `view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)` on the View that
   > uses the dashed background — the same fix applied in `ScanActivity`.
7. **Create the placeholder `AnalyticsActivity`** — just a layout with the
   bottom navigation bar and no other content, plus a `// TODO` comment
   pointing to a future week.
8. **Add a search box and severity chip to the Library screen** — a
   `TextInputEditText` with a `TextWatcher` that filters the in-memory
   disease list by name/plant, and a `Chip` on each `item_disease_library.xml`
   card colored by a `severity` field ("high"/"medium"/"low").
9. **Register the two new Activities** (`ScanActivity`, `AnalyticsActivity`)
   in `AndroidManifest.xml`, alongside the existing five.

### Why this matters for CSE 2206

This section is a direct application of everything in Sections 1-6: you are
still declaring Activities in the manifest, writing ConstraintLayout XML,
using explicit Intents, and relying on Gradle/Material dependencies already
in `build.gradle` — just arranged into a real product UI instead of a
placeholder screen.

---


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

### Day 4: Create the Activity Skeleton

**Tasks:**
1. Locate the app package: `app/src/main/java/com/leafguard/`
2. Create or verify these 8 Activity classes:
    - Top-level tab screens: **MainActivity** (Home), **ScanActivity**, **AnalyticsActivity**, **DiseaseLibraryActivity** (Library), **SettingsActivity** (About)
    - Supporting workflow screens: **ResultActivity**, **HistoryActivity**, **HistoryDetailActivity**
3. Verify all activities are declared in AndroidManifest.xml
4. Add or verify one XML layout for each activity
5. Test building the project

**Deliverables:**
- 8 activities created in `com.leafguard`
- 8 layout files created (`activity_main.xml`, `activity_scan.xml`, `activity_analytics.xml`, `activity_disease_library.xml`, `activity_settings.xml`, `activity_result.xml`, `activity_history.xml`, `activity_history_detail.xml`)
- AndroidManifest.xml updated with all activities
- Project builds without errors

### Day 5: Design XML Layouts

**Reading:**
- Layouts: https://developer.android.com/develop/ui/views/layout/declaring-layout
- ConstraintLayout: https://developer.android.com/develop/ui/views/layout/constraint-layout

**Tasks:**
1. Design MainActivity layout as the Home dashboard:
    - App title and subtitle
    - Green "Quick Scan" card with a "Start Scanning" button
    - Two summary cards: History and Library
    - Technical Features card with two tappable rows
    - Bottom navigation bar
2. Design ScanActivity layout:
    - Back button and title/subtitle
    - Dashed "Tap to upload image" area
    - Cloud/Offline toggle and Detect button (shown after an image is selected)
    - Bottom navigation bar
3. Design AnalyticsActivity layout:
    - Blank placeholder content area
    - Bottom navigation bar
4. Design ResultActivity layout:
   - ImageView for leaf image (top)
   - TextView for disease name
   - TextView for confidence score
   - TextViews for symptoms, treatment, prevention
   - "Share" button
   - "Save to History" button
5. Design HistoryActivity layout:
   - Title "Scan History"
   - RecyclerView placeholder (just TextView for now)
6. Design HistoryDetailActivity layout:
   - Large ImageView for scanned leaf
   - Full disease info
   - Date of scan
   - Action buttons
7. Design DiseaseLibraryActivity layout:
    - Title "Disease Library"
    - Search box
   - RecyclerView of 10 diseases
    - Severity chip on each disease card
    - Bottom navigation bar
8. Design SettingsActivity layout (used as the About tab):
    - Title "Settings"
   - TextField for "Backend URL" (default: `http://10.0.2.2:8000`)
   - Slider for "Confidence Threshold"
    - Bottom navigation bar

**Deliverables:**
- All 8 layouts designed
- All strings externalized to strings.xml
- Layouts responsive to different screen sizes

### Day 6: Implement Navigation

**Reading:**
- Intents and Intent Filters: https://developer.android.com/guide/components/intents-filters

**Tasks:**
1. Shared bottom navigation:
    - Home tab → MainActivity
    - Scan tab → ScanActivity
    - Analytics tab → AnalyticsActivity
    - Library tab → DiseaseLibraryActivity
    - About tab → SettingsActivity
2. Home dashboard navigation:
    - "Start Scanning" → ScanActivity
    - History card → HistoryActivity
    - Library card → DiseaseLibraryActivity
3. ScanActivity navigation:
    - Dashed upload area → camera/gallery chooser
    - Successful detection → ResultActivity (passing image URI and prediction data)
4. ResultActivity navigation:
   - "Share" → share intent to other apps
   - "Save to History" → save scan, then → HistoryActivity
5. HistoryActivity navigation:
   - Tap a scan item → HistoryDetailActivity (pass `EXTRA_SCAN_ID`)
6. Test navigation flow:
    - Home → Scan → Result
    - Home → History → HistoryDetail
    - Home → Library
    - Bottom nav between all 5 tabs
7. Verify back button works correctly
8. Add Log statements to verify data passing

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
    - Screenshots of all 8 activities
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
- [ ] All 8 activities implemented with layouts
- [ ] 5-tab bottom navigation works (Home / Scan / Analytics / Library / About)
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
- [ ] Screenshots of all 8 activities saved
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
- [ ] Know that camera/gallery work belongs in ScanActivity
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
