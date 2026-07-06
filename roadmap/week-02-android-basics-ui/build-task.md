# Week 02 Build Task: Create LeafGuard Android Project with Navigation

## Objective

By the end of this build task, you will have created a complete Android project structure for LeafGuard with 8 Activity classes: 5 top-level tab screens and 3 supporting workflow screens. This is the skeleton that will house all future features.

## Task Overview

**What You Will Build:**
- Android Studio project: LeafGuard
- 8 activities with XML layouts
- 5-tab bottom navigation (Home, Scan, Analytics, Library, About)
- Complete navigation flow with Intent-based routing
- Proper resource management (strings, colors, drawables)
- Professional UI using Material Design and ConstraintLayout
- Git repository with organized commits

**Expected Time:** 8-12 hours spread over 7 days

---

## Prerequisites

Before starting this build task, ensure:

- [ ] Week 01 completely finished (proposal, architecture docs)
- [ ] Android Studio installed and configured
- [ ] At least one emulator or physical device ready
- [ ] Git installed and configured
- [ ] Understanding of Activity lifecycle
- [ ] Understanding of ConstraintLayout basics
- [ ] Understanding of Intent navigation

---

## Step-by-Step Implementation

### Step 1: Create Android Studio Project

**Time:** 30 minutes

1. Launch Android Studio
2. Click "New Project"
3. Select "Empty Activity" template
4. Configure project:
   - Name: `LeafGuard`
    - Package name: `com.leafguard`
   - Save location: Choose appropriate directory
   - Language: `Java`
   - Minimum SDK: `API 24: Android 7.0 (Nougat)`
5. Click "Finish" and wait for Gradle sync
6. Run the default app on emulator to verify setup

**Verification:**
- [ ] Project builds successfully
- [ ] Default app runs on emulator
- [ ] No build errors in Build Output

**Commit:** `Week 02: Initialize LeafGuard Android project`

### Step 2: Configure Project Structure

**Time:** 30 minutes

1. Create package structure:
    ```
    com.leafguard/
    ├── database/
    ├── ml/
    ├── network/
    ├── ui/
    └── utils/
    ```

2. Keep Activity classes directly under `com.leafguard` for this beginner version
3. Put reusable shared helpers under `com.leafguard.ui` (for example, `BottomNav`)
4. Verify AndroidManifest.xml activity names use `.MainActivity`, `.ScanActivity`, etc.
5. Build and run to verify the structure works

**Verification:**
- [ ] Packages created
- [ ] Activity classes live under `com.leafguard`
- [ ] App still runs without errors

**Commit:** `Week 02: Organize package structure`

### Step 3: Configure Gradle Dependencies

**Time:** 15 minutes

Edit `app/build.gradle`:

```gradle
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

    buildFeatures {
        viewBinding true
    }
}

dependencies {
    // Core AndroidX
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

**Verification:**
- [ ] Gradle sync successful
- [ ] No dependency resolution errors

**Commit:** `Week 02: Configure Gradle dependencies and SDK versions`

### Step 4: Create Resource Files

**Time:** 30 minutes

**A. Create `res/values/strings.xml`:**

```xml
<resources>
    <string name="app_name">LeafGuard AI</string>

    <!-- Home dashboard -->
    <string name="home_dashboard_title">🍃 LeafGuard AI</string>
    <string name="home_dashboard_subtitle">Plant Disease Detection System</string>
    <string name="quick_scan_title">Quick Scan</string>
    <string name="start_scanning">Start Scanning</string>

    <!-- Bottom navigation -->
    <string name="nav_home">Home</string>
    <string name="nav_scan">Scan</string>
    <string name="nav_analytics">Analytics</string>
    <string name="nav_library">Library</string>
    <string name="nav_about">About</string>

    <!-- ScanActivity -->
    <string name="scan_screen_title">Scan Leaf</string>
    <string name="tap_to_upload">Tap to upload image</string>
    <string name="choose_image_source_camera">Take Photo</string>
    <string name="choose_image_source_gallery">Choose from Gallery</string>

    <!-- ResultActivity / History / Settings -->
    <string name="result_screen_title">Detection Result</string>
    <string name="confidence_label">Confidence</string>
    <string name="save_to_history">Save to History</string>
    <string name="history_screen_title">Scan History</string>
    <string name="history_empty_state">No saved scans yet. Save a result to see it appear here.</string>
    <string name="settings_screen_title">Settings</string>
</resources>
```

**B. Create `res/values/colors.xml`:**

```xml
<resources>
    <!-- Primary Colors (Green theme for plant app) -->
    <color name="primary">#4CAF50</color>
    <color name="primary_dark">#388E3C</color>
    <color name="primary_light">#C8E6C9</color>

    <!-- Accent Colors -->
    <color name="accent">#FF9800</color>
    <color name="accent_dark">#F57C00</color>

    <!-- Background Colors -->
    <color name="background">#FAFAFA</color>
    <color name="surface">#FFFFFF</color>
    <color name="card_background">#FFFFFF</color>

    <!-- Text Colors -->
    <color name="text_primary">#212121</color>
    <color name="text_secondary">#757575</color>
    <color name="text_hint">#BDBDBD</color>

    <!-- Status Colors -->
    <color name="success">#4CAF50</color>
    <color name="error">#F44336</color>
    <color name="warning">#FF9800</color>
    <color name="info">#2196F3</color>

    <!-- Standard Colors -->
    <color name="white">#FFFFFF</color>
    <color name="black">#000000</color>
</resources>
```

**C. Create `res/values/dimens.xml`:**

```xml
<resources>
    <!-- Margins -->
    <dimen name="margin_tiny">4dp</dimen>
    <dimen name="margin_small">8dp</dimen>
    <dimen name="margin_medium">16dp</dimen>
    <dimen name="margin_large">24dp</dimen>
    <dimen name="margin_xlarge">32dp</dimen>

    <!-- Padding -->
    <dimen name="padding_small">8dp</dimen>
    <dimen name="padding_medium">16dp</dimen>
    <dimen name="padding_large">24dp</dimen>

    <!-- Text Sizes -->
    <dimen name="text_size_caption">12sp</dimen>
    <dimen name="text_size_body">14sp</dimen>
    <dimen name="text_size_body_large">16sp</dimen>
    <dimen name="text_size_title">20sp</dimen>
    <dimen name="text_size_headline">24sp</dimen>
    <dimen name="text_size_display">32sp</dimen>

    <!-- Component Sizes -->
    <dimen name="button_height">48dp</dimen>
    <dimen name="icon_size">24dp</dimen>
    <dimen name="image_preview_height">300dp</dimen>
    <dimen name="card_elevation">4dp</dimen>
    <dimen name="card_corner_radius">8dp</dimen>
</resources>
```

**Verification:**
- [ ] All resource files created
- [ ] No XML syntax errors
- [ ] Project builds successfully

**Commit:** `Week 02: Add string, color, and dimension resources`

### Step 5: Create Remaining Activities

**Time:** 30 minutes

Create the first 5 supporting/top-level activities (in addition to MainActivity). Two more tab activities, `ScanActivity` and `AnalyticsActivity`, are added in Step 11 after the beginner foundation is in place.

1. Right-click the `com.leafguard` package → New → Activity → Empty Activity
2. Create:
   - `ResultActivity`
   - `HistoryActivity`
   - `HistoryDetailActivity`
   - `DiseaseLibraryActivity`
   - `SettingsActivity`
3. Uncheck "Generate Layout File" (we'll create layouts manually)
4. Uncheck "Launcher Activity"

**Verify AndroidManifest.xml includes these 6 foundation activities:**

```xml
<activity
    android:name=".MainActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>

<activity
    android:name=".ResultActivity"
    android:exported="false"
    android:label="@string/result_screen_title" />

<activity
    android:name=".HistoryActivity"
    android:exported="false"
    android:label="@string/history_screen_title" />

<activity
    android:name=".HistoryDetailActivity"
    android:exported="false"
    android:label="@string/history_detail_screen_title" />

<activity
    android:name=".DiseaseLibraryActivity"
    android:exported="false"
    android:label="@string/disease_library_screen_title" />

<activity
    android:name=".SettingsActivity"
    android:exported="false"
    android:label="@string/settings_screen_title" />
```

**Verification:**
- [ ] Foundation activity classes created
- [ ] All activities in AndroidManifest.xml
- [ ] Project builds successfully

**Commit:** `Week 02: Create ResultActivity, HistoryActivity, HistoryDetailActivity, DiseaseLibraryActivity, SettingsActivity`

### Step 6: Design MainActivity Layout

**Time:** 45 minutes

Create `res/layout/activity_main.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background"
    tools:context=".MainActivity">

    <!-- App Title -->
    <TextView
        android:id="@+id/tvAppTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/welcome_title"
        android:textSize="@dimen/text_size_display"
        android:textColor="@color/primary"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="@dimen/margin_xlarge" />

    <!-- Subtitle -->
    <TextView
        android:id="@+id/tvSubtitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/welcome_subtitle"
        android:textSize="@dimen/text_size_body_large"
        android:textColor="@color/text_secondary"
        app:layout_constraintTop_toBottomOf="@id/tvAppTitle"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="@dimen/margin_small" />

    <!-- Scan Button -->
    <com.google.android.material.button.MaterialButton
        android:id="@+id/btnScan"
        android:layout_width="0dp"
        android:layout_height="@dimen/button_height"
        android:text="@string/scan_leaf"
        android:textSize="@dimen/text_size_body_large"
        app:cornerRadius="@dimen/card_corner_radius"
        app:layout_constraintTop_toBottomOf="@id/tvSubtitle"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginStart="@dimen/margin_medium"
        android:layout_marginEnd="@dimen/margin_medium"
        android:layout_marginTop="48dp" />

    <!-- History Button -->
    <com.google.android.material.button.MaterialButton
        android:id="@+id/btnHistory"
        android:layout_width="0dp"
        android:layout_height="@dimen/button_height"
        android:text="@string/view_history"
        android:textSize="@dimen/text_size_body_large"
        app:cornerRadius="@dimen/card_corner_radius"
        style="@style/Widget.MaterialComponents.Button.OutlinedButton"
        app:layout_constraintTop_toBottomOf="@id/btnScan"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginStart="@dimen/margin_medium"
        android:layout_marginEnd="@dimen/margin_medium"
        android:layout_marginTop="@dimen/margin_medium" />

    <!-- Settings Button -->
    <com.google.android.material.button.MaterialButton
        android:id="@+id/btnSettings"
        android:layout_width="0dp"
        android:layout_height="@dimen/button_height"
        android:text="@string/settings"
        android:textSize="@dimen/text_size_body_large"
        app:cornerRadius="@dimen/card_corner_radius"
        style="@style/Widget.MaterialComponents.Button.OutlinedButton"
        app:layout_constraintTop_toBottomOf="@id/btnHistory"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginStart="@dimen/margin_medium"
        android:layout_marginEnd="@dimen/margin_medium"
        android:layout_marginTop="@dimen/margin_medium" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

**Verification:**
- [ ] Layout renders in Design view
- [ ] No layout warnings
- [ ] All strings referenced correctly

**Commit:** `Week 02: Design MainActivity layout`

### Step 7: Implement MainActivity Navigation

**Time:** 30 minutes

**MainActivity.kt (Kotlin — primary):**

```kotlin
package com.leafguard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.leafguard.R

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Initializing MainActivity")
        setContentView(R.layout.activity_main)

        initializeViews()
    }

    private fun initializeViews() {
        val btnCapture: Button = findViewById(R.id.btnCapture)
        val btnHistory: Button = findViewById(R.id.btnHistory)
        val btnLibrary: Button = findViewById(R.id.btnLibrary)
        val btnSettings: Button = findViewById(R.id.btnSettings)

        btnCapture.setOnClickListener {
            Log.d(TAG, "Capture button clicked — opening camera/gallery")
            // TODO: Week 03+ will add camera intent and call ResultActivity with image
        }

        btnHistory.setOnClickListener {
            Log.d(TAG, "History button clicked")
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        btnLibrary.setOnClickListener {
            Log.d(TAG, "Library button clicked")
            startActivity(Intent(this, DiseaseLibraryActivity::class.java))
        }

        btnSettings.setOnClickListener {
            Log.d(TAG, "Settings button clicked")
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: MainActivity is visible")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: MainActivity in foreground")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: MainActivity losing focus")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: MainActivity not visible")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: MainActivity being destroyed")
    }
}
```

<details>
<summary>Java (secondary)</summary>

```java
package com.leafguard;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.leafguard.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initializing MainActivity");
        setContentView(R.layout.activity_main);

        initializeViews();
    }

    private void initializeViews() {
        Button btnCapture = findViewById(R.id.btnCapture);
        Button btnHistory = findViewById(R.id.btnHistory);
        Button btnLibrary = findViewById(R.id.btnLibrary);
        Button btnSettings = findViewById(R.id.btnSettings);

        btnCapture.setOnClickListener(v -> {
            Log.d(TAG, "Capture button clicked — opening camera/gallery");
            // TODO: Week 03+ will add camera intent and call ResultActivity with image
        });

        btnHistory.setOnClickListener(v -> {
            Log.d(TAG, "History button clicked");
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        btnLibrary.setOnClickListener(v -> {
            Log.d(TAG, "Library button clicked");
            Intent intent = new Intent(MainActivity.this, DiseaseLibraryActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Log.d(TAG, "Settings button clicked");
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: MainActivity is visible");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: MainActivity in foreground");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: MainActivity losing focus");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: MainActivity not visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: MainActivity being destroyed");
    }
}
```
</details>

**Verification:**
- [ ] No compilation errors
- [ ] App builds successfully

**Commit:** `Week 02: Implement MainActivity navigation logic`

### Step 8: Design and Implement Remaining Layouts

**Time:** 2-3 hours

Create layouts for each activity (detailed layouts provided in separate section below).

**For each activity:**
1. Create layout XML file
2. Design using ConstraintLayout
3. Implement activity Kotlin code (or Java for secondary)
4. Add navigation buttons
5. Test navigation flow

**Key Implementation Tips:**

- **MainActivity:** Capture buttons (Camera, Gallery), navigation buttons to other activities
- **ResultActivity:** ImageView, Disease name TextView, Confidence TextView, Share/Save buttons
- **HistoryActivity:** TextView placeholder (RecyclerView in Week 07)
- **HistoryDetailActivity:** Full details of a past scan
- **DiseaseLibraryActivity:** List of diseases loaded from `assets/diseases.xml`
- **SettingsActivity:** Backend URL field, Confidence threshold slider

**Commit after each activity:** `Week 02: Design and implement [ActivityName]`

### Step 9: Implement Complete Navigation Flow

**Time:** 30 minutes

Ensure navigation works:
- MainActivity → (Capture button) → ResultActivity (after image analysis)
- MainActivity → HistoryActivity → HistoryDetailActivity
- MainActivity → DiseaseLibraryActivity
- MainActivity → SettingsActivity
- ResultActivity → MainActivity (Back to Home action)
- All activities support Back button

Test scenarios:
1. Launch app → Scan → Result → Scan Another → Result → Back → Back → MainActivity
2. Launch app → History → Back → MainActivity
3. Launch app → Settings → Back → MainActivity
4. Test screen rotation on each activity

**Commit:** `Week 02: Complete navigation flow implementation`

### Step 10: Final Testing and Evidence Collection

**Time:** 1-2 hours

**Testing Checklist:**
- [ ] App launches without crashes
- [ ] All navigation paths work
- [ ] Back button works correctly
- [ ] Screen rotation preserves activity
- [ ] No hardcoded strings (all in strings.xml)
- [ ] No build warnings
- [ ] Logcat shows proper lifecycle events

**Evidence Collection:**
- [ ] Screenshot of each activity
- [ ] Video demonstrating complete navigation flow
- [ ] Screenshot of Logcat showing lifecycle events
- [ ] Screenshot of project structure
- [ ] Generate and save debug APK

**Commit:** `Week 02: Final testing and bug fixes`

---

## Detailed Layouts (Copy-Paste Ready)

> Note: Older drafts of this task placed capture directly in MainActivity. The current app uses the beginner-friendly final structure from Step 11: MainActivity is the Home dashboard, and ScanActivity owns camera/gallery capture and detection.

### MainActivity Layout (activity_main.xml)

MainActivity is now the Home dashboard. It contains the Quick Scan banner, History/Library cards, Technical Features rows, and the shared bottom navigation bar. The capture UI lives in ScanActivity.

### HistoryDetailActivity Layout (activity_history_detail.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="@dimen/padding_medium">

        <ImageView
            android:id="@+id/ivLeafImage"
            android:layout_width="0dp"
            android:layout_height="@dimen/image_preview_height"
            android:scaleType="centerCrop"
            android:contentDescription="@string/scanned_leaf_image"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent" />

        <TextView
            android:id="@+id/tvDiseaseName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="@dimen/text_size_headline"
            android:textStyle="bold"
            app:layout_constraintTop_toBottomOf="@id/ivLeafImage"
            app:layout_constraintStart_toStartOf="parent"
            android:layout_marginTop="@dimen/margin_large" />

        <TextView
            android:id="@+id/tvScanDate"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:layout_constraintTop_toBottomOf="@id/tvDiseaseName"
            app:layout_constraintStart_toStartOf="parent"
            android:layout_marginTop="@dimen/margin_small" />

        <TextView
            android:id="@+id/tvConfidence"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:layout_constraintTop_toBottomOf="@id/tvScanDate"
            app:layout_constraintStart_toStartOf="parent"
            android:layout_marginTop="@dimen/margin_small" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</ScrollView>
```

### DiseaseLibraryActivity Layout (activity_disease_library.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="@dimen/padding_medium">

    <TextView
        android:id="@+id/tvTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/disease_library"
        android:textSize="@dimen/text_size_headline"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="@dimen/margin_large" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvDiseases"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintTop_toBottomOf="@id/tvTitle"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="@dimen/margin_medium" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### ResultActivity Layout (activity_result.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="@dimen/padding_medium">

        <ImageView
            android:id="@+id/ivLeafImage"
            android:layout_width="0dp"
            android:layout_height="@dimen/image_preview_height"
            android:scaleType="centerCrop"
            android:contentDescription="Scanned leaf image"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent" />

        <TextView
            android:id="@+id/tvDiseaseLabel"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/disease_detected"
            android:textSize="@dimen/text_size_body_large"
            android:textStyle="bold"
            app:layout_constraintTop_toBottomOf="@id/ivLeafImage"
            app:layout_constraintStart_toStartOf="parent"
            android:layout_marginTop="@dimen/margin_large" />

        <TextView
            android:id="@+id/tvDiseaseName"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="Tomato Early Blight"
            android:textSize="@dimen/text_size_headline"
            android:textColor="@color/error"
            android:textStyle="bold"
            app:layout_constraintTop_toBottomOf="@id/tvDiseaseLabel"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:layout_marginTop="@dimen/margin_small" />

        <TextView
            android:id="@+id/tvConfidenceLabel"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/confidence_score"
            android:textSize="@dimen/text_size_body_large"
            app:layout_constraintTop_toBottomOf="@id/tvDiseaseName"
            app:layout_constraintStart_toStartOf="parent"
            android:layout_marginTop="@dimen/margin_medium" />

        <TextView
            android:id="@+id/tvConfidence"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="92%"
            android:textSize="@dimen/text_size_title"
            android:textStyle="bold"
            app:layout_constraintTop_toBottomOf="@id/tvConfidenceLabel"
            app:layout_constraintStart_toStartOf="parent"
            android:layout_marginTop="@dimen/margin_small" />

        <com.google.android.material.button.MaterialButton
            android:id="@+id/btnSaveHistory"
            android:layout_width="0dp"
            android:layout_height="@dimen/button_height"
            android:text="@string/save_to_history"
            app:layout_constraintTop_toBottomOf="@id/tvConfidence"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:layout_marginTop="@dimen/margin_large" />

        <com.google.android.material.button.MaterialButton
            android:id="@+id/btnScanAnother"
            android:layout_width="0dp"
            android:layout_height="@dimen/button_height"
            android:text="@string/scan_another"
            style="@style/Widget.MaterialComponents.Button.OutlinedButton"
            app:layout_constraintTop_toBottomOf="@id/btnSaveHistory"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:layout_marginTop="@dimen/margin_medium" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</ScrollView>
```

### HistoryActivity Layout (activity_history.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="@dimen/padding_medium">

    <TextView
        android:id="@+id/tvHistoryTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/scan_history"
        android:textSize="@dimen/text_size_headline"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="@dimen/margin_medium" />

    <TextView
        android:id="@+id/tvNoHistory"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/history_empty_state"
        android:textSize="@dimen/text_size_body_large"
        android:textColor="@color/text_secondary"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

    <!-- RecyclerView will be added in Week 07 -->

</androidx.constraintlayout.widget.ConstraintLayout>
```

### SettingsActivity Layout (activity_settings.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="@dimen/padding_medium">

    <TextView
        android:id="@+id/tvSettingsTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/settings_screen_title"
        android:textSize="@dimen/text_size_headline"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="@dimen/margin_medium" />

    <TextView
        android:id="@+id/tvOfflineMode"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@string/settings_backend_section"
        android:textSize="@dimen/text_size_body_large"
        android:textStyle="bold"
        app:layout_constraintTop_toBottomOf="@id/tvSettingsTitle"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="@dimen/margin_large" />

    <TextView
        android:id="@+id/tvOfflineModeDesc"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@string/settings_backend_url_hint_note"
        android:textSize="@dimen/text_size_body"
        android:textColor="@color/text_secondary"
        app:layout_constraintTop_toBottomOf="@id/tvOfflineMode"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toStartOf="@id/switchOfflineMode"
        android:layout_marginTop="@dimen/margin_tiny"
        android:layout_marginEnd="@dimen/margin_medium" />

    <com.google.android.material.switchmaterial.SwitchMaterial
        android:id="@+id/switchOfflineMode"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintTop_toBottomOf="@id/tvSettingsTitle"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="@dimen/margin_large" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/btnClearCache"
        android:layout_width="0dp"
        android:layout_height="@dimen/button_height"
        android:text="@string/clear_cache"
        style="@style/Widget.MaterialComponents.Button.OutlinedButton"
        app:layout_constraintTop_toBottomOf="@id/tvOfflineModeDesc"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="@dimen/margin_xlarge" />

    <TextView
        android:id="@+id/tvVersion"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/version"
        android:textSize="@dimen/text_size_caption"
        android:textColor="@color/text_secondary"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginBottom="@dimen/margin_medium" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

---

## Activity Implementation Code

### MainActivity capture buttons (Kotlin primary)

In LeafGuard the camera and gallery buttons live in **MainActivity** — there is no separate
capture screen. This week you wire the buttons and navigate to `ResultActivity` with placeholder
data; the real camera/gallery capture arrives in Week 03.

```kotlin
package com.leafguard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate: initializing MainActivity")
        setContentView(R.layout.activity_main)

        // TODO: real capture arrives in Week 03 (camera intent + FileProvider, gallery picker)
        findViewById<android.widget.Button>(R.id.buttonOpenCamera).setOnClickListener {
            Toast.makeText(this, "Camera will be implemented in Week 03", Toast.LENGTH_SHORT).show()
            navigateToResult("camera")
        }
        findViewById<android.widget.Button>(R.id.buttonOpenGallery).setOnClickListener {
            Toast.makeText(this, "Gallery will be implemented in Week 03", Toast.LENGTH_SHORT).show()
            navigateToResult("gallery")
        }
    }

    private fun navigateToResult(source: String) {
        // "source" and the extra keys are local intent extras (not the network JSON field)
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("source", source)
        intent.putExtra(ResultActivity.EXTRA_DISEASE_NAME, "Sample Disease")
        intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, 0.85f)
        startActivity(intent)
    }
}
```

### MainActivity capture buttons — Java (secondary)

```java
package com.leafguard;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initializing MainActivity");
        setContentView(R.layout.activity_main);

        Button btnCamera = findViewById(R.id.buttonOpenCamera);
        Button btnGallery = findViewById(R.id.buttonOpenGallery);

        btnCamera.setOnClickListener(v -> {
            // Week 03: Camera implementation
            Toast.makeText(this, "Camera will be implemented in Week 03", Toast.LENGTH_SHORT).show();
            navigateToResult("camera");
        });

        btnGallery.setOnClickListener(v -> {
            // Week 03: Gallery implementation
            Toast.makeText(this, "Gallery will be implemented in Week 03", Toast.LENGTH_SHORT).show();
            navigateToResult("gallery");
        });
    }

    private void navigateToResult(String source) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("source", source);
        intent.putExtra(ResultActivity.EXTRA_DISEASE_NAME, "Sample Disease");
        intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, 0.85f);
        startActivity(intent);
    }
}
```

> The Java snippets below are **starter examples** for learning Intent extras and Activity structure.
> For the exact current UI, follow Step 11 and the existing source files in `android-app-kotlin/` and `android-app/`.

### ResultActivity.java (starter-only example)

```java
package com.leafguard;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.leafguard.R;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "ResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initializing ResultActivity");
        setContentView(R.layout.activity_result);

        // Retrieve data from Intent
        String source = getIntent().getStringExtra("source");
        String diseaseName = getIntent().getStringExtra("disease_name");
        float confidence = getIntent().getFloatExtra("confidence", 0.0f);

        Log.d(TAG, "onCreate: Received - source=" + source + ", disease=" + diseaseName + ", confidence=" + confidence);

        // Display data
        TextView tvDiseaseName = findViewById(R.id.tvDiseaseName);
        TextView tvConfidence = findViewById(R.id.tvConfidence);

        tvDiseaseName.setText(diseaseName != null ? diseaseName : "Unknown");
        tvConfidence.setText(String.format("%.0f%%", confidence * 100));

        // Setup buttons
        Button btnSaveHistory = findViewById(R.id.btnSaveHistory);
        Button btnScanAnother = findViewById(R.id.btnScanAnother);

        btnSaveHistory.setOnClickListener(v -> {
            Log.d(TAG, "Save to history clicked");
            Toast.makeText(this, "Saved to history (Week 07 feature)", Toast.LENGTH_SHORT).show();
            navigateToHistory();
        });

        btnScanAnother.setOnClickListener(v -> {
            Log.d(TAG, "Scan another clicked");
            navigateToMainActivity();
        });
    }

    private void navigateToHistory() {
        Intent intent = new Intent(ResultActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
```

### HistoryActivity.java (starter-only example)

```java
package com.leafguard;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.leafguard.R;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initializing HistoryActivity");
        setContentView(R.layout.activity_history);

        // RecyclerView implementation in Week 07
    }
}
```

### SettingsActivity.java (starter-only example)

```java
package com.leafguard;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.switchmaterial.SwitchMaterial;

import com.leafguard.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initializing SettingsActivity");
        setContentView(R.layout.activity_settings);

        SwitchMaterial switchOfflineMode = findViewById(R.id.switchOfflineMode);
        Button btnClearCache = findViewById(R.id.btnClearCache);

        switchOfflineMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d(TAG, "Offline mode: " + isChecked);
            String message = isChecked ? "Offline mode enabled" : "Online mode enabled";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        btnClearCache.setOnClickListener(v -> {
            Log.d(TAG, "Clear cache clicked");
            Toast.makeText(this, "Cache cleared", Toast.LENGTH_SHORT).show();
        });
    }
}
```

---

## Validation Checklist

Before proceeding to Week 03, verify all items:

**Project Structure:**
- [ ] Package structure organized (`com.leafguard`, database/, network/, ml/, utils/, ui/)
- [ ] All 8 activities created
- [ ] All 8 layout files created and properly named
- [ ] AndroidManifest.xml declares all activities correctly

**Resources:**
- [ ] strings.xml contains all strings (no hardcoded strings in code/layouts)
- [ ] colors.xml contains cohesive color scheme
- [ ] themes.xml contains Theme.LeafGuardAI
- [ ] Drawables for bottom nav icons and custom backgrounds exist
- [ ] Resources properly referenced (@string/, @color/, @drawable/)

**Functionality:**
- [ ] App launches without crashes
- [ ] MainActivity dashboard shows Quick Scan, History, Library, and Technical Features
- [ ] Bottom navigation switches between Home, Scan, Analytics, Library, and About
- [ ] Start Scanning opens ScanActivity
- [ ] ScanActivity camera/gallery chooser responds
- [ ] ScanActivity → ResultActivity navigation works with data passing
- [ ] MainActivity → HistoryActivity navigation works
- [ ] MainActivity → DiseaseLibraryActivity navigation works
- [ ] SettingsActivity works as the About tab
- [ ] ResultActivity → MainActivity navigation works
- [ ] Back button works correctly on all activities
- [ ] Screen rotation preserves activities

**Code Quality:**
- [ ] No compilation errors or warnings
- [ ] Proper lifecycle methods implemented in MainActivity
- [ ] Log statements added for debugging
- [ ] Comments added for clarity
- [ ] Consistent naming conventions

**Evidence:**
- [ ] Screenshots of all 8 activities saved
- [ ] Video of complete navigation flow recorded
- [ ] Logcat screenshot showing lifecycle events saved
- [ ] Debug APK generated and saved
- [ ] Git commits show progressive work (minimum 10 commits)

**Git Repository:**
- [ ] At least 10 meaningful commits
- [ ] Commit messages follow "Week 02: [Description]" format
- [ ] .gitignore configured for Android
- [ ] No build files committed (build/, .gradle/)

---

## Common Issues and Solutions

**Issue:** App crashes on launch
**Solution:** Check Logcat for stack trace, verify AndroidManifest.xml activity declarations

**Issue:** findViewById returns null
**Solution:** Verify view ID matches between Java and XML, ensure setContentView is called first

**Issue:** Navigation not working
**Solution:** Check activity names in Intent match actual class names, verify activities in manifest

**Issue:** Resources not found
**Solution:** Clean and rebuild project (Build → Clean Project → Rebuild Project)

**Issue:** Emulator not starting
**Solution:** Enable virtualization in BIOS, update HAXM, try creating new AVD

---

## Deliverables

Submit to `evidence/week-02/build-task/`:

1. **Complete Android project** (commit to Git)
2. **Screenshots folder** with:
    - MainActivity (Home dashboard)
    - ScanActivity (upload/capture screen)
    - AnalyticsActivity
    - DiseaseLibraryActivity
   - ResultActivity
   - HistoryActivity
    - HistoryDetailActivity
   - SettingsActivity
   - Project structure
   - Logcat lifecycle events
3. **Navigation flow video** (30-60 seconds)
4. **Debug APK file** (`app-debug.apk`)
5. **Build task completion report** (`build-task-report.md`) including:
   - Challenges faced
   - Solutions implemented
   - Time spent on each step
   - Lessons learned

---

## Time Breakdown

| Step | Task | Estimated Time |
|------|------|----------------|
| 1 | Create project | 30 min |
| 2 | Configure structure | 30 min |
| 3 | Configure Gradle | 15 min |
| 4 | Create resources | 30 min |
| 5 | Create activities | 30 min |
| 6 | Design MainActivity | 45 min |
| 7 | Implement MainActivity | 30 min |
| 8 | Design/implement remaining | 2-3 hours |
| 9 | Complete navigation | 30 min |
| 10 | Testing and evidence | 1-2 hours |
| **Total** | | **8-12 hours** |

---

## Step 11: Build the Dashboard Home Screen + Bottom Navigation

**Time:** 3-4 hours

The earlier steps in this file teach Activities, layouts, and navigation in
small beginner-sized pieces. This step combines those pieces into the
**actual current LeafGuard UI**: a dashboard Home screen and a 5-tab bottom
navigation bar (Home / Scan / Analytics / Library / About), matching
`android-app-kotlin/` exactly.
See [README.md, Section 7](README.md#7-the-current-app-design-dashboard-home--bottom-navigation)
for a description of the finished design before starting.

### 11a. Shared bottom navigation bar

1. Create `res/menu/bottom_nav_menu.xml` with 5 `<item>` entries: `nav_home`,
   `nav_scan`, `nav_analytics`, `nav_library`, `nav_about` — each with an
   `android:icon` and `android:title`.
2. Add 5 vector drawables, one per tab (`res/drawable/ic_nav_home.xml`,
   `ic_nav_scan.xml`, `ic_nav_analytics.xml`, `ic_nav_library.xml`,
   `ic_nav_about.xml`). A single `<path>` per vector is enough —
   `BottomNavigationView` tints icons automatically based on selection.
3. Write **one** shared navigation helper instead of repeating the same
   click-handling code on every screen:

   ```kotlin
   // com/leafguard/ui/BottomNav.kt
   fun AppCompatActivity.setupBottomNav(bottomNav: BottomNavigationView, currentItemId: Int) {
       bottomNav.setOnItemSelectedListener { item ->
           if (item.itemId == currentItemId) return@setOnItemSelectedListener true
           val target: Class<out AppCompatActivity> = when (item.itemId) {
               R.id.nav_home -> MainActivity::class.java
               R.id.nav_scan -> ScanActivity::class.java
               R.id.nav_analytics -> AnalyticsActivity::class.java
               R.id.nav_library -> DiseaseLibraryActivity::class.java
               R.id.nav_about -> SettingsActivity::class.java
               else -> return@setOnItemSelectedListener false
           }
           startActivity(Intent(this, target))
           finish()
           true
       }
       bottomNav.selectedItemId = currentItemId
   }
   ```

   <details>
   <summary>Java (secondary)</summary>

   ```java
   // com/leafguard/ui/BottomNav.java
   public final class BottomNav {
       public static void setup(AppCompatActivity activity, BottomNavigationView bottomNav, int currentItemId) {
           bottomNav.setOnItemSelectedListener(item -> {
               int itemId = item.getItemId();
               if (itemId == currentItemId) return true;
               Class<? extends AppCompatActivity> target;
               if (itemId == R.id.nav_home) target = MainActivity.class;
               else if (itemId == R.id.nav_scan) target = ScanActivity.class;
               else if (itemId == R.id.nav_analytics) target = AnalyticsActivity.class;
               else if (itemId == R.id.nav_library) target = DiseaseLibraryActivity.class;
               else if (itemId == R.id.nav_about) target = SettingsActivity.class;
               else return false;
               activity.startActivity(new Intent(activity, target));
               activity.finish();
               return true;
           });
           bottomNav.setSelectedItemId(currentItemId);
       }
   }
   ```
   </details>

4. Add a `BottomNavigationView` (id `bottomNavigation`, `app:menu="@menu/bottom_nav_menu"`)
   constrained to the bottom of every tab screen's layout, and call the
   helper once in each Activity's `onCreate` with that screen's own tab id
   (e.g. `setupBottomNav(binding.bottomNavigation, R.id.nav_home)`).

**Verification:**
- [ ] All 5 icons render in the bottom bar
- [ ] Tapping a tab switches screens and highlights the correct icon
- [ ] Re-tapping the current tab does nothing (no crash, no restart)

**Commit:** `Week 02: Add shared bottom navigation bar`

### 11b. Home dashboard (`MainActivity` + `activity_main.xml`)

1. Replace the placeholder welcome layout with a `ScrollView` containing:
   a title ("🍃 LeafGuard AI") and subtitle, a green `MaterialCardView`
   "Quick Scan" banner with a "Start Scanning" button, a horizontal row of
   two `MaterialCardView`s ("History" / "Library"), and a "Technical
   Features" card with two tappable rows.
2. "Start Scanning" opens `ScanActivity` (built in 11c). The History and
   Library cards open the existing `HistoryActivity` / `DiseaseLibraryActivity`.
3. In `onResume()`, query `AppDatabase.getInstance(this).scanDao().getAllScans()`
   (on a background thread/coroutine) and show `.size` in the History card's
   subtitle — this is the same Room DAO you already use elsewhere in the app.

**Verification:**
- [ ] Home shows the Quick Scan banner + both summary cards on launch
- [ ] History card subtitle updates after saving a new scan

**Commit:** `Week 02: Build Home dashboard screen`

### 11c. Scan tab (new `ScanActivity` + `activity_scan.xml`)

1. Create a new Activity `ScanActivity` and move the camera/gallery
   permission-request code, the `MaterialButtonToggleGroup` (Cloud/Offline),
   and the "Detect Disease" button **out of `MainActivity` and into
   `ScanActivity`** — this is the same capture/detect logic, just relocated.
2. Replace the old side-by-side camera/gallery buttons with a single dashed
   rectangle "tap to upload" area (`res/drawable/bg_dashed_upload.xml`, a
   `<shape>` with a dashed `<stroke>`). Tapping it shows a small dialog with
   "Take Photo" / "Choose from Gallery" options.
3. Keep the mode toggle and "Detect Disease" button hidden (`visibility="gone"`)
   until an image has been picked, then show them.

   > **Gotcha:** dashed strokes don't reliably render with hardware
   > acceleration on some devices. Call
   > `view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)` on the upload area
   > View to fix it.
4. Register `ScanActivity` in `AndroidManifest.xml`.

**Verification:**
- [ ] Tapping the dashed area opens the camera/gallery chooser
- [ ] After picking an image, the mode toggle and Detect button appear
- [ ] Detection still opens `ResultActivity` exactly as before

**Commit:** `Week 02: Move capture/detect flow into ScanActivity`

### 11d. Analytics placeholder (new `AnalyticsActivity`)

1. Create `AnalyticsActivity` with a layout containing **only** the shared
   bottom navigation bar — no other content yet.
2. Add a `// TODO` comment noting that scan-trend charts arrive in a later
   week, built from the same `scan_history` Room table.
3. Register it in `AndroidManifest.xml`.

**Verification:**
- [ ] Tapping "Analytics" in the bottom bar opens a blank screen with the nav bar

**Commit:** `Week 02: Add Analytics tab placeholder`

### 11e. Library search + severity chip

1. Add a `TextInputLayout`/`TextInputEditText` search box above the
   `RecyclerView` in `activity_disease_library.xml`.
2. Add a `TextWatcher` that filters the in-memory disease list by name or
   plant (case-insensitive) and re-submits it to the adapter.
3. Add a `severity` field ("high"/"medium"/"low") to the disease data model
   and a `Chip` to `item_disease_library.xml` colored according to severity.
4. Add the bottom navigation bar to this screen too (tab id `nav_library`).

**Verification:**
- [ ] Typing in the search box filters the list live
- [ ] Each disease card shows a colored severity chip

**Commit:** `Week 02: Add search and severity chip to Disease Library`

### 11f. About tab

Add the shared bottom navigation bar to the existing `SettingsActivity`
(tab id `nav_about`) — no other changes needed; Settings already covers the
"About" content (backend URL, confidence threshold, app version).

**Commit:** `Week 02: Wire About tab into SettingsActivity`

---

## Success Criteria

You have successfully completed Week 02 Build Task when:

1. LeafGuard Android project runs without errors
2. All 8 activities are functional with proper layouts
3. Navigation flow works correctly between all activities
4. Resources are properly externalized and organized
5. Code follows Android best practices
6. Git repository shows progressive development
7. Evidence package is complete with screenshots and video
8. You can explain every aspect of your implementation
9. The 5-tab bottom navigation bar (Home / Scan / Analytics / Library / About)
   works and matches the dashboard design described in README.md Section 7

**Congratulations! You now have a solid Android UI foundation. Week 03 will strengthen the camera/gallery flow and permission-handling concepts that are introduced through ScanActivity.**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 02 — Navigation

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
| [⬅ Week 01: Project Understanding](../week-01-project-understanding/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 03: Camera & Gallery ➡](../week-03-camera-gallery/README.md) |

---
