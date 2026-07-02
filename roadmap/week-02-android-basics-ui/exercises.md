# Week 02 Exercises: Android Studio Setup & UI Skeleton

## Exercise Overview

These exercises reinforce Week 02 concepts through hands-on practice. Complete all exercises to solidify your understanding of Android Studio, Activity lifecycle, XML layouts, and Intent navigation. Each exercise builds on previous knowledge and prepares you for Week 03.

**Completion Criteria:** All exercises must be attempted. Save evidence (screenshots, code snippets, answers) in `evidence/week-02/exercises/`.

---

## Exercise 1: Android Studio Exploration

### Goal
Familiarize yourself with Android Studio IDE features, shortcuts, and tools.

### Tasks

**Part A: Interface Navigation**

1. Open Android Studio and create a test project
2. Identify and document the purpose of these windows/panels:
   - Project window (left sidebar)
   - Editor window (center)
   - Logcat (bottom)
   - Build output (bottom)
   - TODO panel
   - Structure panel
3. Switch between Android view and Project view in Project window
4. Open Layout Editor for an XML file and explore:
   - Design view
   - Code view
   - Split view
   - Component Tree
   - Attributes panel

**Part B: Keyboard Shortcuts**

Practice these shortcuts and document what they do:
- `Ctrl+Space` (Windows/Linux) or `Cmd+Space` (Mac)
- `Ctrl+N` / `Cmd+O`
- `Ctrl+Shift+N` / `Cmd+Shift+O`
- `Ctrl+Alt+L` / `Cmd+Alt+L`
- `Shift+F10` / `Ctrl+R`
- `Alt+Enter` / `Option+Return`
- `Ctrl+F` / `Cmd+F`

**Part C: Tool Windows**

1. Open SDK Manager (Tools → SDK Manager)
2. Screenshot installed SDK platforms
3. Open AVD Manager (Tools → AVD Manager)
4. Screenshot configured virtual devices
5. Open Gradle window and sync project
6. Observe build process in Build output

### Deliverables

Create a document: `exercises/ex1-android-studio-exploration.md`

Include:
- Screenshots of all windows/panels labeled
- Table of keyboard shortcuts and their functions
- Screenshot of SDK Manager showing installed components
- Screenshot of AVD Manager showing emulators
- Notes on what you learned

### Expected Time
30-45 minutes

---

## Exercise 2: Activity Lifecycle Observation

### Goal
Understand the Activity lifecycle by observing method calls in different scenarios.

### Tasks

**Part A: Implement Lifecycle Logging**

1. Open MainActivity in your LeafGuard project
2. Add all 6 lifecycle methods: `onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onDestroy`
3. Add `Log.d(TAG, "MethodName: called")` in each method
4. Run the app and open Logcat

**Part B: Scenario Testing**

Test these scenarios and document the lifecycle methods called:

1. **App Launch:**
   - Launch app from launcher
   - Document: onCreate → onStart → onResume

2. **Home Button Press:**
   - App is running
   - Press Home button
   - Document: onPause → onStop

3. **Return to App:**
   - From home screen, tap app icon
   - Document: onStart → onResume (onCreate NOT called)

4. **Screen Rotation:**
   - App is running in portrait
   - Rotate to landscape
   - Document: onPause → onStop → onDestroy → onCreate → onStart → onResume

5. **Navigate to Another Activity:**
   - From MainActivity, navigate to MainActivity
   - Document: MainActivity onPause → onCreate/onStart/onResume for MainActivity → MainActivity onStop

6. **Back Button:**
   - From MainActivity, press Back
   - Document: MainActivity onPause/onStop/onDestroy → MainActivity onStart/onResume

**Part C: State Preservation**

1. Add a variable in MainActivity: `private int counter = 0;`
2. Add a Button that increments counter and updates a TextView
3. Increment counter to 5
4. Rotate the device
5. Observe counter resets to 0 (state lost)
6. Implement `onSaveInstanceState` and `onRestoreInstanceState` to preserve counter
7. Test rotation again, verify counter is preserved

### Deliverables

Create a document: `exercises/ex2-lifecycle-observation.md`

Include:
- Code snippets of lifecycle methods with Log statements
- Screenshot of Logcat showing each scenario's output
- Table mapping scenarios to lifecycle methods
- Before/after screenshots of state preservation fix
- Explanation of why state is lost on rotation

### Expected Time
45-60 minutes

---

## Exercise 3: ConstraintLayout Mastery

### Goal
Build complex layouts using ConstraintLayout constraints, chains, and guidelines.

### Tasks

**Part A: Recreation Exercise**

Recreate this layout using only ConstraintLayout (no nested layouts):

```
┌─────────────────────────────┐
│                             │
│      [App Logo/Icon]        │  (centered horizontally, 32dp from top)
│                             │
│     LeafGuard AI Title      │  (centered horizontally, 16dp below logo)
│                             │
│  ┌───────────────────────┐  │
│  │    Scan New Leaf      │  │  (button, 0dp width, 16dp margin)
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │    View History       │  │  (button, 16dp below previous)
│  └───────────────────────┘  │
│                             │
│  ┌─────────┐  ┌─────────┐  │
│  │ Library │  │Settings │  │  (two buttons, equal width, chained)
│  └─────────┘  └─────────┘  │
│                             │
└─────────────────────────────┘
```

Requirements:
- Use ConstraintLayout only
- ImageView for logo (use any placeholder image)
- TextView for title
- 4 Material Buttons
- Last two buttons in a horizontal chain with `spread` style
- All constraints properly defined
- Responsive to different screen sizes

**Part B: Guidelines Practice**

Create a new layout file: `activity_profile.xml`

Use vertical and horizontal guidelines to divide screen into sections:
- Vertical guideline at 30% from left
- Vertical guideline at 70% from left
- Horizontal guideline at 40% from top

Place views constrained to guidelines:
- Profile image (top-left section)
- Name TextView (top-right section)
- Bio TextView (middle section, spanning guidelines)
- Edit button (bottom section, centered)

**Part C: Bias and Positioning**

Create a layout demonstrating bias:
- Button positioned at 25% from left (horizontal_bias = 0.25)
- Button positioned at 75% from top (vertical_bias = 0.75)
- Button centered (default bias = 0.5)

### Deliverables

Create:
- `exercises/layout_recreation.xml` - Part A layout
- `exercises/activity_profile.xml` - Part B layout
- `exercises/layout_bias_demo.xml` - Part C layout
- `exercises/ex3-constraintlayout-mastery.md` - Documentation with screenshots

Include:
- Screenshots of all three layouts in Design view
- Screenshots of layouts running on emulator
- XML code snippets showing key constraints
- Notes on challenges faced and solutions

### Expected Time
60-75 minutes

---

## Exercise 4: Intent Navigation Flow

### Goal
Implement complex navigation patterns with data passing between activities.

### Tasks

**Part A: Linear Navigation**

Implement this flow:
```
MainActivity → MainActivity → ResultActivity → HistoryActivity
```

Each activity should:
- Display its name prominently
- Have a "Next" button to go to next activity
- Pass data forward: `activity_count` (increment by 1 each step)
- Display received `activity_count` in a TextView

**Part B: Non-Linear Navigation**

Add these navigation paths:
- MainActivity → HistoryActivity (skip MainActivity)
- ResultActivity → MainActivity (clear back stack, start fresh)
- ResultActivity → MainActivity (go back without destroying current activity)

Implement buttons for each path.

**Part C: Data Passing Practice**

Create MainActivity that passes multiple data types to ResultActivity:
- String: `plant_type` (e.g., "Tomato")
- Float: `confidence` (e.g., 0.87f)
- Boolean: `is_healthy` (e.g., false)
- Int Array: `rgb_values` (e.g., [120, 200, 150])

ResultActivity should:
- Receive all data
- Display each value in separate TextViews
- Format confidence as percentage (87%)
- Display "Healthy" or "Diseased" based on boolean
- Display RGB as "RGB(120, 200, 150)"

**Part D: Back Stack Management**

Implement "Start Over" button in ResultActivity:
- Clear entire back stack
- Navigate to MainActivity
- User cannot press Back to return to ResultActivity

Implement "Cancel" button in MainActivity:
- Finish MainActivity
- Return to MainActivity with result code `RESULT_CANCELED`
- MainActivity shows Toast: "Scan cancelled"

### Deliverables

Create:
- Updated Activities with navigation implemented
- `exercises/ex4-intent-navigation.md` - Documentation

Include:
- Navigation diagram showing all paths
- Code snippets of Intent creation with extras
- Code snippets of receiving and displaying data
- Screenshots of data passing (ResultActivity showing all received data)
- Screenshot of Logcat showing Activity stack changes
- Notes on back stack behavior observations

### Expected Time
60-90 minutes

---

## Exercise 5: Resource Management

### Goal
Properly externalize and organize resources following Android best practices.

### Tasks

**Part A: String Externalization**

1. Find all hardcoded strings in your activities and layouts
2. Extract each to `strings.xml` with meaningful keys
3. Replace hardcoded strings with `@string/resource_name`
4. Ensure no hardcoded strings remain (except log tags)

Example:
```xml
<!-- Before -->
<TextView android:text="Scan Leaf" />

<!-- After -->
<TextView android:text="@string/scan_leaf" />

<!-- In strings.xml -->
<string name="scan_leaf">Scan Leaf</string>
```

**Part B: Color Scheme**

Create a cohesive color scheme in `colors.xml`:
- Primary color (for main UI elements)
- Primary dark (for status bar)
- Accent color (for FABs, highlights)
- Background color
- Text primary color
- Text secondary color
- Error color
- Success color

Apply colors consistently across all layouts.

**Part C: Dimension Standardization**

Create `dimens.xml` with standard dimensions:
```xml
<dimen name="margin_small">8dp</dimen>
<dimen name="margin_medium">16dp</dimen>
<dimen name="margin_large">24dp</dimen>
<dimen name="margin_xlarge">32dp</dimen>
<dimen name="text_size_small">12sp</dimen>
<dimen name="text_size_medium">16sp</dimen>
<dimen name="text_size_large">20sp</dimen>
<dimen name="text_size_xlarge">24sp</dimen>
<dimen name="button_height">48dp</dimen>
<dimen name="icon_size">24dp</dimen>
```

Replace all hardcoded dimensions in layouts with references.

**Part D: Drawable Resources**

1. Find and download 5 vector icons (Material Icons or similar):
   - Camera icon
   - Gallery icon
   - History icon
   - Settings icon
   - Info icon
2. Add icons to `res/drawable/`
3. Use icons in layouts with `ImageView` or Button `drawableStart`

**Part E: String Formatting**

Add formatted strings to `strings.xml`:
```xml
<string name="confidence_format">Confidence: %1$.1f%%</string>
<string name="disease_detected">Disease: %1$s</string>
<string name="scan_count">Total scans: %1$d</string>
```

Use in Java:
```java
String formatted = getString(R.string.confidence_format, 87.5f);
```

### Deliverables

Create:
- Updated `strings.xml` with all strings
- Updated `colors.xml` with color scheme
- Updated `dimens.xml` with standard dimensions
- Updated layouts using resources
- `exercises/ex5-resource-management.md` - Documentation

Include:
- Before/after screenshots showing resource usage
- Table of all strings with keys
- Color palette visualization
- Notes on naming conventions used

### Expected Time
45-60 minutes

---

## Exercise 6: Gradle Configuration

### Goal
Understand and configure Gradle build files for LeafGuard project.

### Tasks

**Part A: SDK Configuration**

Edit `app/build.gradle`:

1. Set SDK versions:
   - `compileSdk 34`
   - `minSdk 24` (Android 7.0, covers 95%+ devices)
   - `targetSdk 34`

2. Explain the purpose of each:
   - Why is `compileSdk` the highest?
   - What happens if a device runs API 23 with `minSdk 24`?
   - What is the difference between `targetSdk` and `compileSdk`?

**Part B: Dependency Management**

Add these dependencies to `app/build.gradle`:

```gradle
dependencies {
    // Core AndroidX
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    // Material Design
    implementation 'com.google.android.material:material:1.10.0'

    // Lifecycle components (for Week 05)
    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.6.2'
    implementation 'androidx.lifecycle:lifecycle-livedata:2.6.2'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
}
```

Tasks:
1. Add dependencies
2. Sync Gradle
3. Document what each dependency provides
4. Find each library's latest version on Maven Central
5. Explain the difference between `implementation`, `api`, and `testImplementation`

**Part C: Build Types**

Configure build types in `app/build.gradle`:

```gradle
buildTypes {
    debug {
        debuggable true
        applicationIdSuffix ".debug"
        versionNameSuffix "-DEBUG"
    }
    release {
        minifyEnabled true
        shrinkResources true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

Tasks:
1. Understand the difference between debug and release builds
2. Build a debug APK and a release APK
3. Compare file sizes
4. Explain what `minifyEnabled` does
5. Explain what `proguardFiles` are for

**Part D: Version Management**

Configure app versioning:
```gradle
defaultConfig {
    versionCode 1
    versionName "1.0.0"
}
```

Tasks:
1. Explain the difference between `versionCode` and `versionName`
2. Document a version increment strategy:
   - When to increment `versionCode`?
   - When to change `versionName` from "1.0.0" to "1.0.1"?
   - When to change to "1.1.0"?
   - When to change to "2.0.0"?

**Part E: Build Variants**

Configure product flavors for free and paid versions:

```gradle
flavorDimensions "version"
productFlavors {
    free {
        dimension "version"
        applicationIdSuffix ".free"
        versionNameSuffix "-free"
    }
    paid {
        dimension "version"
        applicationIdSuffix ".paid"
        versionNameSuffix "-paid"
    }
}
```

Tasks:
1. Add flavor configuration
2. Sync Gradle
3. Observe Build Variants panel (View → Tool Windows → Build Variants)
4. Build `freeDebug` variant
5. Build `paidRelease` variant
6. Document use cases for flavors

### Deliverables

Create:
- Updated `app/build.gradle` with all configurations
- `exercises/ex6-gradle-configuration.md` - Documentation

Include:
- Annotated `build.gradle` explaining each section
- Screenshot of Build Variants panel
- Screenshots of debug and release APK sizes
- Version increment strategy document
- Notes on dependency version selection

### Expected Time
45-60 minutes

---

## Exercise 7: Manifest Configuration

### Goal
Properly configure AndroidManifest.xml for LeafGuard project.

### Tasks

**Part A: Activity Declarations**

Review your AndroidManifest.xml:

1. Verify all 5 activities are declared
2. Ensure `exported` attribute is set correctly:
   - `exported="true"` only for MainActivity (launcher)
   - `exported="false"` for all other activities
3. Add meaningful labels for each activity:
```xml
<activity
    android:name=".activities.MainActivity"
    android:label="@string/scan_activity_label"
    android:exported="false" />
```

**Part B: App Metadata**

Configure application element:
```xml
<application
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:theme="@style/Theme.LeafGuard"
    android:supportsRtl="true">
```

Tasks:
1. Explain what `android:allowBackup` does
2. Explain what `android:supportsRtl` enables
3. Change app icon (create or download a leaf icon)
4. Verify icon appears in launcher

**Part C: Intent Filter Understanding**

Analyze the launcher activity intent filter:
```xml
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
```

Tasks:
1. Explain what `MAIN` action means
2. Explain what `LAUNCHER` category means
3. What happens if you add this intent filter to ResultActivity?
4. Test: Add launcher intent filter to MainActivity, rebuild, check launcher (you'll see two app icons)
5. Remove the test intent filter

**Part D: Permission Preparation (Week 03 Preview)**

Add permission declarations (you'll implement handlers in Week 03):
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="28" />
<uses-permission android:name="android.permission.INTERNET" />
```

Tasks:
1. Explain why `WRITE_EXTERNAL_STORAGE` has `maxSdkVersion="28"`
2. Explain the difference between declaring permissions in manifest vs requesting at runtime
3. Document which LeafGuard features require each permission

**Part E: Screen Orientation**

Configure screen orientation for MainActivity:
```xml
<activity
    android:name=".activities.MainActivity"
    android:screenOrientation="portrait"
    android:exported="false" />
```

Tasks:
1. Test: Run app, navigate to MainActivity, try to rotate (it won't rotate)
2. Explain why you might lock orientation for camera activities
3. Test ResultActivity without orientation lock, rotate and observe

### Deliverables

Create:
- Updated `AndroidManifest.xml` with proper configuration
- `exercises/ex7-manifest-configuration.md` - Documentation

Include:
- Annotated manifest explaining each element
- Screenshot of new app icon in launcher
- Table mapping activities to their exported status and rationale
- Permission requirements table
- Notes on orientation lock decisions

### Expected Time
30-45 minutes

---

## Exercise 8: Debugging and Logcat Practice

### Goal
Develop debugging skills using Logcat and Android Studio debugging tools.

### Tasks

**Part A: Implement Comprehensive Logging**

Add logging to MainActivity:

```java
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Start");
        setContentView(R.layout.activity_main);

        Button btnScan = findViewById(R.id.btnScan);
        if (btnScan == null) {
            Log.e(TAG, "onCreate: btnScan is null! Check layout file");
            return;
        }
        Log.d(TAG, "onCreate: btnScan initialized successfully");

        btnScan.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Scan button clicked");
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            Log.d(TAG, "onClick: Starting MainActivity");
            startActivity(intent);
        });

        Log.d(TAG, "onCreate: End");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: MainActivity is visible");
    }
}
```

Tasks:
1. Add similar logging to all 5 activities
2. Run app and filter Logcat by your package name
3. Navigate through all activities
4. Save Logcat output to file

**Part B: Intentional Bug Fixing**

Introduce and fix these bugs:

**Bug 1: NullPointerException**
```java
// Introduce bug
TextView tvTitle = findViewById(R.id.tvWrongId); // Wrong ID
tvTitle.setText("LeafGuard"); // Crash!

// Fix
TextView tvTitle = findViewById(R.id.tvTitle);
if (tvTitle != null) {
    tvTitle.setText("LeafGuard");
} else {
    Log.e(TAG, "tvTitle not found in layout");
}
```

**Bug 2: Missing Intent Extra**
```java
// Sender
Intent intent = new Intent(this, ResultActivity.class);
// Forgot to add extras!
startActivity(intent);

// Receiver crashes
String disease = getIntent().getStringExtra("disease_name");
textView.setText(disease.toUpperCase()); // NullPointerException if disease is null

// Fix with default value
String disease = getIntent().getStringExtra("disease_name");
if (disease != null) {
    textView.setText(disease.toUpperCase());
} else {
    Log.w(TAG, "No disease name received, using default");
    textView.setText("Unknown Disease");
}
```

**Bug 3: Activity Not Declared**
```java
// Remove SettingsActivity from manifest
// Try to navigate to it
Intent intent = new Intent(this, SettingsActivity.class);
startActivity(intent); // ActivityNotFoundException

// Observe crash in Logcat
// Fix by adding activity to manifest
```

Tasks:
1. Introduce each bug one at a time
2. Observe crash in Logcat
3. Identify the error line
4. Fix the bug
5. Document the error message and solution

**Part C: Logcat Filtering Practice**

Create Logcat filters:

1. **Filter 1: Only Errors**
   - Show only `Log.e()` messages
   - Level: Error

2. **Filter 2: MainActivity Only**
   - Tag: MainActivity

3. **Filter 3: All LeafGuard Logs**
   - Package: com.example.leafguard

4. **Filter 4: Lifecycle Events**
   - Regex: `.*(onCreate|onStart|onResume|onPause|onStop|onDestroy).*`

Tasks:
1. Create each filter
2. Test by generating different log messages
3. Screenshot each filter's output
4. Document when to use each filter

**Part D: Performance Monitoring**

Add time measurements:
```java
long startTime = System.currentTimeMillis();

// Perform operation
processLargeImage();

long endTime = System.currentTimeMillis();
Log.d(TAG, "processLargeImage: Took " + (endTime - startTime) + "ms");
```

Tasks:
1. Add timing logs to onCreate methods
2. Measure how long layout inflation takes
3. Identify slowest activity
4. Document findings

### Deliverables

Create:
- `exercises/ex8-debugging-logcat.md` - Documentation

Include:
- Code snippets with comprehensive logging
- Screenshots of Logcat showing each bug's error message
- Screenshots of Logcat filters configured
- Table mapping error messages to solutions
- Performance measurement results
- Notes on debugging strategies learned

### Expected Time
60-75 minutes

---

## Bonus Exercise: APK Analysis

### Goal
Understand APK structure and contents.

### Tasks

1. Build a release APK
2. Rename `.apk` to `.zip`
3. Extract contents
4. Explore extracted folders:
   - `META-INF/` - APK signature
   - `res/` - Compiled resources
   - `classes.dex` - Compiled Java code
   - `AndroidManifest.xml` - Binary XML
   - `resources.arsc` - Resource table
5. Install APK Analyzer (bundled with Android Studio)
6. Analyze APK file size breakdown
7. Identify largest contributors to APK size

### Deliverables

Document: `exercises/bonus-apk-analysis.md`

Include:
- Screenshot of APK contents
- Screenshot of APK Analyzer showing size breakdown
- Notes on how to reduce APK size

### Expected Time
20-30 minutes

---

## Exercise Completion Checklist

Mark exercises as complete:

- [ ] Exercise 1: Android Studio Exploration
- [ ] Exercise 2: Activity Lifecycle Observation
- [ ] Exercise 3: ConstraintLayout Mastery
- [ ] Exercise 4: Intent Navigation Flow
- [ ] Exercise 5: Resource Management
- [ ] Exercise 6: Gradle Configuration
- [ ] Exercise 7: Manifest Configuration
- [ ] Exercise 8: Debugging and Logcat Practice
- [ ] Bonus: APK Analysis (optional)

**Minimum Requirement:** Complete exercises 1-7 to proceed to Week 03.

---

## Submission

Save all exercise deliverables to:
```
evidence/week-02/exercises/
├── ex1-android-studio-exploration.md
├── ex2-lifecycle-observation.md
├── ex3-constraintlayout-mastery.md
├── ex4-intent-navigation.md
├── ex5-resource-management.md
├── ex6-gradle-configuration.md
├── ex7-manifest-configuration.md
├── ex8-debugging-logcat.md
├── screenshots/
│   ├── ex1-*.png
│   ├── ex2-*.png
│   └── ...
└── code-snippets/
    ├── MainActivity.java
    ├── layout_recreation.xml
    └── ...
```

Commit to Git with message: `Week 02: Complete exercises 1-8`


<!-- NAV_FOOTER_START -->

---

## 📚 Week 02 — Navigation

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
| [⬅ Week 01: Project Understanding](../week-01-project-understanding/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 03: Camera & Gallery ➡](../week-03-camera-gallery/README.md) |

---
