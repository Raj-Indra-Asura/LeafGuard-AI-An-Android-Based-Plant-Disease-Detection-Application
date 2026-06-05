# Week 02 Learning Notes: Android Studio Setup & UI Skeleton

## Overview

This document contains detailed learning notes for Week 02, focusing on Android Studio setup, project structure, Activity lifecycle, XML layouts, and Intent navigation. These notes complement the README and serve as a reference during development.

---

## Section 1: Android Studio Installation and Configuration

### 1.1 System Requirements Understanding

**Minimum vs Recommended:**
- **Minimum:** 8 GB RAM, 4 GB disk space, 1280x800 display
- **Recommended:** 16 GB RAM, 10 GB SSD, 1920x1080 display
- **Why it matters:** Android Studio with emulator is resource-intensive. Insufficient RAM causes lag and build timeouts.

**Virtualization Technology:**
- **Intel:** VT-x must be enabled in BIOS
- **AMD:** AMD-V must be enabled in BIOS
- **Why:** Emulator uses hardware acceleration for performance
- **Check:** Task Manager → Performance → CPU → Virtualization should show "Enabled"

### 1.2 SDK Components Explained

**SDK Platforms:**
- **API 24 (Android 7.0):** Minimum supported version (covers 95%+ devices)
- **API 30 (Android 11):** Introduced scoped storage changes
- **API 33 (Android 13):** Runtime notification permissions
- **API 34 (Android 14):** Latest stable, target SDK for new apps

**Why multiple SDKs:**
- `minSdk 24` - Your app runs on Android 7.0+ devices
- `compileSdk 34` - You build with latest features and APIs
- `targetSdk 34` - You optimize for Android 14 behavior

**SDK Build Tools:**
- Compiles resources (XML to binary)
- Packages APK files
- Signs APKs for installation
- Version usually matches compileSdk

**SDK Platform Tools:**
- **adb (Android Debug Bridge):** Command-line tool for device communication
- **fastboot:** For flashing system images
- **systrace:** Performance analysis

### 1.3 Emulator Configuration

**AVD (Android Virtual Device) Settings:**

**Recommended Configuration for LeafGuard:**
- **Device:** Pixel 5 or similar (1080x2340, 440 dpi)
- **System Image:** API 30 or 33 with Google APIs
- **RAM:** 2048 MB (if you have 16 GB system RAM)
- **Internal Storage:** 2048 MB
- **Graphics:** Hardware - GLES 2.0

**Why This Configuration:**
- Pixel 5 represents common phone dimensions
- API 30/33 have stable camera and gallery behavior
- Google APIs include Play Services (needed for some features)
- Hardware graphics acceleration improves performance

**Emulator Keyboard Shortcuts:**
- `Ctrl+M` - Show/hide menu
- `Ctrl+H` - Home button
- `Ctrl+B` - Back button
- `Ctrl+R` - Rotate device
- `Ctrl+P` - Power button

### 1.4 Physical Device Setup

**Enabling Developer Options:**
1. Settings → About Phone → Build Number (tap 7 times)
2. Developer Options appears in Settings
3. Enable "USB Debugging"
4. Enable "Install via USB"

**Connecting Device:**
1. Connect via USB cable
2. Device prompts "Allow USB debugging?" → Always allow from this computer
3. Android Studio detects device in device selector
4. Green triangle appears (ready to run)

**Troubleshooting Connection:**
- Windows: Install OEM USB drivers (manufacturer website)
- Check cable supports data transfer (not just charging)
- Try different USB port
- Run `adb devices` in terminal to verify connection

---

## Section 2: Android Project Structure Deep Dive

### 2.1 Project View vs Android View

**Android View (Recommended):**
- Simplified logical grouping
- Shows `java/`, `res/`, `manifests/` clearly
- Hides build folders and Gradle cache

**Project View (File System):**
- Shows actual folder structure on disk
- Reveals build outputs, `.gradle/` cache
- Use when troubleshooting build issues

### 2.2 Folder-by-Folder Explanation

**app/src/main/java/com/example/leafguard/**

This is where all your Java code lives. The package structure should match your domain (reversed domain naming convention: `com.company.appname`).

**Recommended Subpackages for LeafGuard:**
```
com.example.leafguard/
├── activities/          (Activity classes)
├── fragments/           (Fragment classes - Week 08)
├── adapters/            (RecyclerView adapters - Week 07)
├── models/              (Data classes - Week 05)
│   ├── Disease.java
│   ├── ScanResult.java
│   └── User.java
├── viewmodels/          (MVVM ViewModels - Week 05)
│   ├── ScanViewModel.java
│   └── HistoryViewModel.java
├── repositories/        (Data layer - Week 05)
│   └── ScanRepository.java
├── database/            (Room database - Week 07)
│   ├── AppDatabase.java
│   ├── entities/
│   └── dao/
├── network/             (Retrofit API - Week 05)
│   ├── ApiService.java
│   ├── RetrofitClient.java
│   └── responses/
├── utils/               (Helper classes)
│   ├── Constants.java
│   ├── ImageUtils.java
│   └── XmlParser.java
└── Application.java     (Custom application class if needed)
```

**Why This Organization:**
- **Scalability:** Easy to find files as project grows
- **Separation of Concerns:** Each package has a single responsibility
- **Team Collaboration:** Multiple developers can work on different packages
- **Testing:** Easy to locate and test specific layers

**app/src/main/res/**

Resources folder containing all non-code assets.

**res/layout/** - XML layout files
```
layout/
├── activity_main.xml           (MainActivity layout)
├── activity_scan.xml           (ScanActivity layout)
├── activity_result.xml         (ResultActivity layout)
├── activity_history.xml        (HistoryActivity layout)
├── activity_settings.xml       (SettingsActivity layout)
├── fragment_disease_info.xml   (Week 08)
├── item_history.xml            (RecyclerView item - Week 07)
└── dialog_loading.xml          (Custom dialogs)
```

**res/drawable/** - Images, icons, shapes, gradients
```
drawable/
├── ic_camera.xml               (Vector icon for camera)
├── ic_gallery.xml              (Vector icon for gallery)
├── ic_history.xml              (Vector icon for history)
├── ic_settings.xml             (Vector icon for settings)
├── bg_button.xml               (Button background shape)
├── bg_card.xml                 (Card background with rounded corners)
└── splash_background.xml       (Gradient background)
```

**res/values/** - Constants (strings, colors, dimensions, styles)

**strings.xml:**
```xml
<resources>
    <string name="app_name">LeafGuard AI</string>
    <string name="scan_leaf">Scan Leaf</string>
    <string name="view_history">View History</string>
    <string name="settings">Settings</string>
    <string name="camera">Camera</string>
    <string name="gallery">Gallery</string>
    <string name="disease_name">Disease: %1$s</string>
    <string name="confidence">Confidence: %1$.1f%%</string>
</resources>
```

**colors.xml:**
```xml
<resources>
    <color name="primary">#4CAF50</color>        <!-- Green -->
    <color name="primary_dark">#388E3C</color>   <!-- Dark Green -->
    <color name="accent">#FF9800</color>         <!-- Orange -->
    <color name="background">#FAFAFA</color>     <!-- Light Gray -->
    <color name="text_primary">#212121</color>   <!-- Dark Gray -->
    <color name="text_secondary">#757575</color> <!-- Medium Gray -->
    <color name="white">#FFFFFF</color>
    <color name="error">#F44336</color>          <!-- Red -->
</resources>
```

**dimens.xml:**
```xml
<resources>
    <dimen name="padding_small">8dp</dimen>
    <dimen name="padding_medium">16dp</dimen>
    <dimen name="padding_large">24dp</dimen>
    <dimen name="text_size_title">24sp</dimen>
    <dimen name="text_size_body">16sp</dimen>
    <dimen name="text_size_caption">14sp</dimen>
    <dimen name="button_height">48dp</dimen>
    <dimen name="card_corner_radius">8dp</dimen>
</resources>
```

**Why Externalize Resources:**
- **Internationalization:** Easy to translate (create values-es/, values-fr/)
- **Theme Support:** Switch colors for dark mode (values-night/)
- **Maintainability:** Change string once, updates everywhere
- **Different Screen Sizes:** Provide different dimens for tablets (values-sw600dp/)

**res/mipmap/** - App launcher icons

Different densities required:
- `mipmap-mdpi/` - 48x48 px
- `mipmap-hdpi/` - 72x72 px
- `mipmap-xhdpi/` - 96x96 px
- `mipmap-xxhdpi/` - 144x144 px
- `mipmap-xxxhdpi/` - 192x192 px

**AndroidManifest.xml**

The app configuration file. Every component (activity, service, receiver) must be declared here.

**Key Elements:**
```xml
<manifest>
    - package: App's unique identifier
    - xmlns:android: XML namespace for Android attributes

    <uses-permission>: Declare required permissions

    <application>
        - android:icon: App icon reference
        - android:label: App name reference
        - android:theme: App theme reference
        - android:allowBackup: Enable automatic backup

        <activity>: Each activity declaration
            - android:name: Activity class name
            - android:exported: Can other apps launch it?
            - <intent-filter>: How activity responds to intents
                - <action>: Intent action (MAIN, VIEW, etc.)
                - <category>: Intent category (LAUNCHER, DEFAULT)
</application>
</manifest>
```

### 2.3 Gradle Files Explained

**settings.gradle** (Project Root)

Defines which modules are included in the build.

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

rootProject.name = "LeafGuard"
include ':app'
```

**build.gradle (Project Level)**

Applies to all modules, defines build script dependencies.

```gradle
buildscript {
    ext.kotlin_version = "1.8.22"
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:8.1.0"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

**build.gradle (App Level)** - Most Important

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
        versionName "1.0.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        debug {
            minifyEnabled false
            debuggable true
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding true
    }
}

dependencies {
    // Core AndroidX libraries
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    // Material Design
    implementation 'com.google.android.material:material:1.10.0'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

**Understanding Dependency Scopes:**
- `implementation` - Compile-time and runtime dependency (standard)
- `api` - Like implementation but transitive (dependency is exposed to consumers)
- `testImplementation` - Only for unit tests (JUnit)
- `androidTestImplementation` - Only for instrumented tests (UI tests)
- `compileOnly` - Compile-time only, not packaged in APK

---

## Section 3: Activity Lifecycle In-Depth

### 3.1 Lifecycle States

**Created State:**
- Activity is instantiated but not visible
- `onCreate()` called once per instance
- Initialize views, setup click listeners
- Set content view (inflate layout)

**Started State:**
- Activity is visible but not in foreground
- `onStart()` called when becoming visible
- Register broadcast receivers
- Start animations

**Resumed State:**
- Activity is visible and interactive
- `onResume()` called when activity has focus
- Resume paused operations (camera, sensors)
- Start audio/video playback

**Paused State:**
- Activity is partially visible (dialog on top)
- `onPause()` called when losing focus
- Pause ongoing operations
- Save uncommitted changes
- Release resources like camera

**Stopped State:**
- Activity is not visible
- `onStop()` called when completely hidden
- Stop expensive operations (GPS, network polling)
- Save persistent data

**Destroyed State:**
- Activity is finishing or system is killing it
- `onDestroy()` called for final cleanup
- Unregister listeners
- Close database connections

### 3.2 Configuration Changes

**What is a Configuration Change:**
- Screen rotation (portrait ↔ landscape)
- Language change
- Keyboard availability change
- Dark mode toggle

**Default Behavior:**
```
Activity is destroyed:
onPause() → onStop() → onDestroy()

Activity is recreated:
onCreate() → onStart() → onResume()
```

**Problem:** All instance variables are lost, UI resets.

**Solution 1: Save State (Week 02 Approach)**
```java
@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString("disease_name", diseaseName);
    outState.putFloat("confidence", confidence);
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);

    if (savedInstanceState != null) {
        diseaseName = savedInstanceState.getString("disease_name");
        confidence = savedInstanceState.getFloat("confidence");
    }
}
```

**Solution 2: ViewModel (Week 05 Approach - Better)**
```java
public class ResultViewModel extends ViewModel {
    private MutableLiveData<String> diseaseName = new MutableLiveData<>();
    private MutableLiveData<Float> confidence = new MutableLiveData<>();

    // Data survives configuration changes automatically
}
```

### 3.3 Activity Launch Modes

**Standard (Default):**
- New instance created every time
- Multiple instances can exist
- LeafGuard default for all activities

**SingleTop:**
- If activity is at top of stack, reuse it
- Useful for search activities
- Prevents duplicate instances on top

**SingleTask:**
- Only one instance in the system
- Becomes root of new task
- Good for main/home activities

**SingleInstance:**
- Only one instance, isolated task
- Rarely used

**LeafGuard Use Case:**
All activities use standard mode. MainActivity could use `singleTop` to prevent duplicates when launched from notifications.

---

## Section 4: XML Layouts and ConstraintLayout

### 4.1 View Hierarchy Basics

**View:** Base class for all UI components
- TextView, Button, ImageView, EditText all extend View

**ViewGroup:** Container for other views
- LinearLayout, ConstraintLayout, RelativeLayout extend ViewGroup

**Hierarchy Example:**
```
ConstraintLayout (ViewGroup)
├── TextView (View)
├── Button (View)
└── LinearLayout (ViewGroup)
    ├── EditText (View)
    └── Button (View)
```

**Layout Inflation:**
```java
setContentView(R.layout.activity_main);
```
This line:
1. Reads activity_main.xml
2. Parses XML to Java objects
3. Creates View instances
4. Builds view hierarchy
5. Attaches to activity window

### 4.2 ConstraintLayout Constraints Explained

**Constraint Anatomy:**
```xml
app:layout_constraintTop_toBottomOf="@id/tvTitle"
     ↓                  ↓               ↓
  constraint       relationship      target view
```

**Possible Relationships:**
- `toTopOf` - Align with top edge
- `toBottomOf` - Align with bottom edge
- `toStartOf` - Align with start edge (left in LTR)
- `toEndOf` - Align with end edge (right in LTR)
- `toLeftOf` / `toRightOf` - Absolute directions (avoid, use start/end)

**Special Targets:**
- `parent` - The ConstraintLayout itself
- `@id/viewName` - Another view by ID

**Bias (Positioning Within Constraints):**
```xml
app:layout_constraintHorizontal_bias="0.3"
```
- 0.0 = leftmost
- 0.5 = center (default)
- 1.0 = rightmost

**Example: Center Button Horizontally**
```xml
<Button
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    android:layout_marginTop="100dp"/>
```

**Chains:**
Create linked views that distribute space.
```xml
<!-- First view in chain -->
<Button android:id="@+id/btn1"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toStartOf="@id/btn2"
    app:layout_constraintHorizontal_chainStyle="spread"/>

<!-- Second view in chain -->
<Button android:id="@+id/btn2"
    app:layout_constraintStart_toEndOf="@id/btn1"
    app:layout_constraintEnd_toEndOf="parent"/>
```

**Chain Styles:**
- `spread` - Distribute evenly with space between
- `spread_inside` - First and last views at edges, space between others
- `packed` - Views packed together in center

### 4.3 Common Attributes

**Dimension Attributes:**
- `layout_width` / `layout_height`:
  - `match_parent` - Fill parent width/height
  - `wrap_content` - Just big enough for content
  - `0dp` - Match constraints (used with ConstraintLayout)
  - `100dp` - Fixed size in density-independent pixels

**Padding vs Margin:**
- `padding` - Space inside view (pushes content inward)
- `margin` - Space outside view (pushes view away from others)

```xml
<Button
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:padding="16dp"          <!-- 16dp inside button -->
    android:layout_margin="8dp"/>   <!-- 8dp outside button -->
```

**Text Attributes:**
- `text` - Display text
- `textSize` - Font size (use sp for scalability)
- `textColor` - Text color
- `textStyle` - bold, italic, normal
- `gravity` - Text alignment within view (center, start, end)

**Image Attributes:**
- `src` - Image resource
- `scaleType` - How to scale image:
  - `centerCrop` - Scale to fill, crop excess
  - `centerInside` - Scale to fit, preserve aspect ratio
  - `fitXY` - Stretch to fill (distorts)

### 4.4 Material Design Components

**Material Button:**
```xml
<com.google.android.material.button.MaterialButton
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:text="@string/scan_leaf"
    app:cornerRadius="8dp"
    app:strokeColor="@color/primary"
    app:strokeWidth="2dp"
    style="@style/Widget.MaterialComponents.Button.OutlinedButton"/>
```

**Material CardView:**
```xml
<com.google.android.material.card.MaterialCardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardCornerRadius="12dp"
    app:cardElevation="4dp"
    app:contentPadding="16dp">

    <!-- Card content here -->

</com.google.android.material.card.MaterialCardView>
```

**Material TextInputLayout (EditText Wrapper):**
```xml
<com.google.android.material.textfield.TextInputLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="@string/enter_disease_name">

    <com.google.android.material.textfield.TextInputEditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

</com.google.android.material.textfield.TextInputLayout>
```

---

## Section 5: Intent Navigation and Data Passing

### 5.1 Intent Components

**Intent Structure:**
```java
Intent intent = new Intent(Context, Class);
```
- `Context` - Source activity (usually `this` or `ActivityName.this`)
- `Class` - Destination activity class (e.g., `ScanActivity.class`)

**Starting Activity:**
```java
startActivity(intent);
```

**Starting Activity for Result (Deprecated, use Activity Result API):**
```java
startActivityForResult(intent, REQUEST_CODE);
```

### 5.2 Passing Primitive Data

**Sender:**
```java
Intent intent = new Intent(MainActivity.this, ResultActivity.class);
intent.putExtra("disease_name", "Tomato Early Blight");
intent.putExtra("confidence", 0.92f);
intent.putExtra("scan_id", 12345);
intent.putExtra("is_healthy", false);
startActivity(intent);
```

**Receiver:**
```java
String diseaseName = getIntent().getStringExtra("disease_name");
float confidence = getIntent().getFloatExtra("confidence", 0.0f);
int scanId = getIntent().getIntExtra("scan_id", -1);
boolean isHealthy = getIntent().getBooleanExtra("is_healthy", true);
```

**Default Values:**
The second parameter in getXxxExtra() is the default value returned if key does not exist.

### 5.3 Passing Objects

**Option 1: Serializable (Simpler but Slower)**
```java
// Model class
public class Disease implements Serializable {
    private String name;
    private String symptoms;
    private String treatment;

    // Constructor, getters, setters
}

// Sender
Disease disease = new Disease("Tomato Blight", "Brown spots...", "Fungicide...");
intent.putExtra("disease_object", disease);

// Receiver
Disease disease = (Disease) getIntent().getSerializableExtra("disease_object");
```

**Option 2: Parcelable (Faster, More Code)**
```java
public class Disease implements Parcelable {
    private String name;
    private String symptoms;

    // Constructor

    protected Disease(Parcel in) {
        name = in.readString();
        symptoms = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(symptoms);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Disease> CREATOR = new Creator<Disease>() {
        @Override
        public Disease createFromParcel(Parcel in) {
            return new Disease(in);
        }

        @Override
        public Disease[] newArray(int size) {
            return new Disease[size];
        }
    };
}

// Sender
intent.putExtra("disease_object", disease);

// Receiver
Disease disease = getIntent().getParcelableExtra("disease_object");
```

**When to Use:**
- **Serializable:** For prototypes, small objects, simplicity
- **Parcelable:** For production, frequently passed objects, performance-critical

### 5.4 Activity Result API (Modern Approach)

**Replacing startActivityForResult:**

**Old Way (Deprecated):**
```java
// Sender
startActivityForResult(intent, REQUEST_CAMERA);

// Receiver
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
        Uri imageUri = data.getData();
    }
}
```

**New Way (Activity Result API - Week 03):**
```java
// Register launcher
ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            Uri imageUri = data.getData();
        }
    }
);

// Launch activity
cameraLauncher.launch(intent);
```

### 5.5 Back Stack Management

**Default Back Stack:**
```
Launch: [MainActivity]
Start ScanActivity: [MainActivity, ScanActivity]
Start ResultActivity: [MainActivity, ScanActivity, ResultActivity]
Press Back: [MainActivity, ScanActivity]
Press Back: [MainActivity]
Press Back: App closes
```

**Clear Back Stack (Start Fresh):**
```java
Intent intent = new Intent(ResultActivity.this, MainActivity.class);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
startActivity(intent);
```

**Bring Existing Activity to Front:**
```java
intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
```

**LeafGuard Use Case:**
After scan completes, user taps "Scan Another". Clear back stack and return to MainActivity to start fresh.

---

## Section 6: Debugging with Logcat

### 6.1 Log Levels

```java
Log.v(TAG, "Verbose - detailed development logs");
Log.d(TAG, "Debug - debugging information");
Log.i(TAG, "Info - informational messages");
Log.w(TAG, "Warning - potential issues");
Log.e(TAG, "Error - errors occurred");
```

**When to Use Each:**
- `Log.v` - Trace method execution, loop iterations (removed in production)
- `Log.d` - Debug values, state changes (removed in production)
- `Log.i` - App lifecycle events, important milestones
- `Log.w` - Deprecated API usage, recoverable errors
- `Log.e` - Exceptions, critical failures

**Best Practice:**
```java
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initializing MainActivity");
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Layout set successfully");
    }
}
```

### 6.2 Logcat Filters

**Filter by Tag:**
```
tag:MainActivity
```

**Filter by Level:**
```
level:ERROR
```

**Filter by Package:**
```
package:com.example.leafguard
```

**Regex Filter:**
```
regex:.*disease.*
```

**Logcat Colors:**
- Verbose - Gray
- Debug - Blue
- Info - Green
- Warning - Orange
- Error - Red

### 6.3 Common Log Patterns

**Logging Method Entry/Exit:**
```java
public void detectDisease(Bitmap image) {
    Log.d(TAG, "detectDisease: Entry");
    // Process image
    Log.d(TAG, "detectDisease: Exit with result=" + result);
}
```

**Logging Exceptions:**
```java
try {
    processImage();
} catch (IOException e) {
    Log.e(TAG, "processImage: Failed to read image", e);
}
```

**Logging JSON Responses:**
```java
Log.d(TAG, "API Response: " + response.toString());
```

---

## Section 7: Common Errors and Solutions

### 7.1 Build Errors

**Error:** "Manifest merger failed"
**Cause:** Conflicting attributes in merged manifests
**Solution:** Add `tools:replace="android:theme"` in manifest

**Error:** "Program type already present"
**Cause:** Duplicate dependencies
**Solution:** Check build.gradle for duplicate libraries

**Error:** "Gradle sync failed"
**Cause:** Network issues, corrupted cache
**Solution:** File → Invalidate Caches / Restart

### 7.2 Runtime Errors

**Error:** "android.content.ActivityNotFoundException"
**Cause:** Activity not declared in manifest
**Solution:** Add `<activity>` entry in AndroidManifest.xml

**Error:** "java.lang.NullPointerException"
**Cause:** Accessing null view (findViewById returned null)
**Solution:** Check view ID matches XML, ensure setContentView is called first

**Error:** "android.view.InflateException"
**Cause:** XML layout error (wrong tag, missing attribute)
**Solution:** Check XML syntax, validate with layout editor

### 7.3 Emulator Issues

**Issue:** Emulator not starting
**Solution:** Enable virtualization in BIOS, update HAXM

**Issue:** App not installing on emulator
**Solution:** Wipe emulator data, recreate AVD

**Issue:** Emulator extremely slow
**Solution:** Allocate more RAM, use x86 image, enable hardware graphics

---

## Key Takeaways

1. **Android Studio is a powerful IDE** - Learn keyboard shortcuts and features gradually
2. **Project structure follows conventions** - `java/` for code, `res/` for resources
3. **Activity lifecycle is crucial** - Master it to avoid memory leaks and crashes
4. **ConstraintLayout is the modern standard** - Flat hierarchy, flexible positioning
5. **Intents connect activities** - Explicit intents for navigation, extras for data
6. **Gradle manages dependencies** - Understand app-level build.gradle
7. **Logcat is your debugging friend** - Use appropriate log levels
8. **Resources should be externalized** - Strings, colors, dimensions in XML

---

## Next Steps

Week 03 builds on this foundation by adding:
- Camera functionality using Camera2 or CameraX API
- Gallery picker using MediaStore
- Runtime permissions (CAMERA, READ_EXTERNAL_STORAGE)
- Image handling (URIs, Bitmaps, loading into ImageView)
- File storage for captured images

Ensure you have a solid understanding of Week 02 concepts before proceeding. The camera integration will test your knowledge of Activities, Intents, and Manifests.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 02 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| **2** | **learning-notes.md** ← *You are here* | **Theory & Learning Notes** |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Week Overview & Objectives](README.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Theory & Learning Notes** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Practice Exercises →](exercises.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 01: Project Understanding](../week-01-project-understanding/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 03: Camera & Gallery ➡](../week-03-camera-gallery/README.md) |

---
