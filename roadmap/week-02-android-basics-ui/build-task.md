# Week 02 Build Task: Create LeafGuard Android Project with Navigation

## Objective

By the end of this build task, you will have created a complete Android project structure for LeafGuard with 5 functional activities, proper navigation flow, and a professional UI foundation. This is the skeleton that will house all future features.

## Task Overview

**What You Will Build:**
- Android Studio project: LeafGuard
- 5 activities with XML layouts
- Complete navigation flow with Intent-based routing
- Proper resource management (strings, colors, dimensions)
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
   - Package name: `com.example.leafguard`
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
   com.example.leafguard/
   ├── activities/
   ├── adapters/
   ├── models/
   ├── viewmodels/
   ├── repositories/
   ├── database/
   ├── network/
   └── utils/
   ```

2. Move `MainActivity.java` to `activities/` package
3. Update imports in MainActivity
4. Update AndroidManifest.xml activity name to `.activities.MainActivity`
5. Build and run to verify refactoring works

**Verification:**
- [ ] Packages created
- [ ] MainActivity in activities package
- [ ] App still runs without errors

**Commit:** `Week 02: Organize package structure`

### Step 3: Configure Gradle Dependencies

**Time:** 15 minutes

Edit `app/build.gradle`:

```gradle
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

    <!-- MainActivity -->
    <string name="welcome_title">Welcome to LeafGuard</string>
    <string name="welcome_subtitle">AI-Powered Plant Disease Detection</string>
    <string name="scan_leaf">Scan New Leaf</string>
    <string name="view_history">View Scan History</string>
    <string name="settings">Settings</string>

    <!-- ScanActivity -->
    <string name="select_image_source">Select Image Source</string>
    <string name="camera">Take Photo with Camera</string>
    <string name="gallery">Choose from Gallery</string>
    <string name="image_preview">Image Preview</string>
    <string name="analyze">Analyze Leaf</string>

    <!-- ResultActivity -->
    <string name="scan_result">Scan Result</string>
    <string name="disease_detected">Disease Detected:</string>
    <string name="confidence_score">Confidence:</string>
    <string name="treatment_info">Treatment Information</string>
    <string name="save_to_history">Save to History</string>
    <string name="scan_another">Scan Another Leaf</string>

    <!-- HistoryActivity -->
    <string name="scan_history">Scan History</string>
    <string name="no_history">No scan history yet</string>
    <string name="clear_history">Clear History</string>

    <!-- SettingsActivity -->
    <string name="app_settings">Settings</string>
    <string name="offline_mode">Offline Mode</string>
    <string name="offline_mode_desc">Use on-device AI model</string>
    <string name="clear_cache">Clear Cache</string>
    <string name="about">About</string>
    <string name="version">Version 1.0.0</string>
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

Create 4 new activities:

1. Right-click `activities` package → New → Activity → Empty Activity
2. Create:
   - `ScanActivity`
   - `ResultActivity`
   - `HistoryActivity`
   - `SettingsActivity`
3. Uncheck "Generate Layout File" (we'll create layouts manually)
4. Uncheck "Launcher Activity"

**Verify AndroidManifest.xml includes all activities:**

```xml
<activity
    android:name=".activities.MainActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>

<activity
    android:name=".activities.ScanActivity"
    android:exported="false"
    android:label="@string/select_image_source" />

<activity
    android:name=".activities.ResultActivity"
    android:exported="false"
    android:label="@string/scan_result" />

<activity
    android:name=".activities.HistoryActivity"
    android:exported="false"
    android:label="@string/scan_history" />

<activity
    android:name=".activities.SettingsActivity"
    android:exported="false"
    android:label="@string/app_settings" />
```

**Verification:**
- [ ] All 5 activity classes created
- [ ] All activities in AndroidManifest.xml
- [ ] Project builds successfully

**Commit:** `Week 02: Create ScanActivity, ResultActivity, HistoryActivity, SettingsActivity`

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
    tools:context=".activities.MainActivity">

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

Update `MainActivity.java`:

```java
package com.example.leafguard.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.leafguard.R;

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
        Button btnScan = findViewById(R.id.btnScan);
        Button btnHistory = findViewById(R.id.btnHistory);
        Button btnSettings = findViewById(R.id.btnSettings);

        btnScan.setOnClickListener(v -> {
            Log.d(TAG, "Scan button clicked");
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            Log.d(TAG, "History button clicked");
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
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
3. Implement activity Java code
4. Add navigation buttons
5. Test navigation flow

**Key Implementation Tips:**

- **ScanActivity:** Two buttons (Camera, Gallery), ImageView placeholder
- **ResultActivity:** ImageView, Disease name TextView, Confidence TextView, Save/Scan Another buttons
- **HistoryActivity:** TextView placeholder (RecyclerView in Week 07)
- **SettingsActivity:** Switch for offline mode, Clear cache button

**Commit after each activity:** `Week 02: Design and implement [ActivityName]`

### Step 9: Implement Complete Navigation Flow

**Time:** 30 minutes

Ensure navigation works:
- MainActivity → ScanActivity → ResultActivity
- MainActivity → HistoryActivity
- MainActivity → SettingsActivity
- ResultActivity → MainActivity (with FLAG_ACTIVITY_CLEAR_TOP)
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

### ScanActivity Layout (activity_scan.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="@dimen/padding_medium">

    <TextView
        android:id="@+id/tvSelectSource"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/select_image_source"
        android:textSize="@dimen/text_size_headline"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="@dimen/margin_xlarge" />

    <ImageView
        android:id="@+id/ivPreview"
        android:layout_width="0dp"
        android:layout_height="@dimen/image_preview_height"
        android:scaleType="centerCrop"
        android:background="@color/primary_light"
        android:contentDescription="@string/image_preview"
        app:layout_constraintTop_toBottomOf="@id/tvSelectSource"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="@dimen/margin_large" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/btnCamera"
        android:layout_width="0dp"
        android:layout_height="@dimen/button_height"
        android:text="@string/camera"
        app:layout_constraintTop_toBottomOf="@id/ivPreview"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="@dimen/margin_large" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/btnGallery"
        android:layout_width="0dp"
        android:layout_height="@dimen/button_height"
        android:text="@string/gallery"
        style="@style/Widget.MaterialComponents.Button.OutlinedButton"
        app:layout_constraintTop_toBottomOf="@id/btnCamera"
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
        android:text="@string/no_history"
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
        android:text="@string/app_settings"
        android:textSize="@dimen/text_size_headline"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="@dimen/margin_medium" />

    <TextView
        android:id="@+id/tvOfflineMode"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@string/offline_mode"
        android:textSize="@dimen/text_size_body_large"
        android:textStyle="bold"
        app:layout_constraintTop_toBottomOf="@id/tvSettingsTitle"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="@dimen/margin_large" />

    <TextView
        android:id="@+id/tvOfflineModeDesc"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@string/offline_mode_desc"
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

### ScanActivity.java

```java
package com.example.leafguard.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.leafguard.R;

public class ScanActivity extends AppCompatActivity {

    private static final String TAG = "ScanActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initializing ScanActivity");
        setContentView(R.layout.activity_scan);

        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnGallery = findViewById(R.id.btnGallery);

        btnCamera.setOnClickListener(v -> {
            Log.d(TAG, "Camera button clicked");
            // Week 03: Camera implementation
            Toast.makeText(this, "Camera will be implemented in Week 03", Toast.LENGTH_SHORT).show();

            // Navigate to ResultActivity with dummy data
            navigateToResult("camera");
        });

        btnGallery.setOnClickListener(v -> {
            Log.d(TAG, "Gallery button clicked");
            // Week 03: Gallery implementation
            Toast.makeText(this, "Gallery will be implemented in Week 03", Toast.LENGTH_SHORT).show();

            // Navigate to ResultActivity with dummy data
            navigateToResult("gallery");
        });
    }

    private void navigateToResult(String source) {
        Intent intent = new Intent(ScanActivity.this, ResultActivity.class);
        intent.putExtra("source", source);
        intent.putExtra("disease_name", "Sample Disease");
        intent.putExtra("confidence", 0.85f);
        startActivity(intent);
    }
}
```

### ResultActivity.java

```java
package com.example.leafguard.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leafguard.R;

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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
```

### HistoryActivity.java

```java
package com.example.leafguard.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.leafguard.R;

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

### SettingsActivity.java

```java
package com.example.leafguard.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.switchmaterial.SwitchMaterial;

import com.example.leafguard.R;

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
- [ ] Package structure organized (activities/, models/, utils/, etc.)
- [ ] All 5 activities created in activities package
- [ ] All 5 layout files created and properly named
- [ ] AndroidManifest.xml declares all activities correctly

**Resources:**
- [ ] strings.xml contains all strings (no hardcoded strings in code/layouts)
- [ ] colors.xml contains cohesive color scheme
- [ ] dimens.xml contains standard dimensions
- [ ] All resources properly referenced (@string/, @color/, @dimen/)

**Functionality:**
- [ ] App launches without crashes
- [ ] MainActivity → ScanActivity navigation works
- [ ] MainActivity → HistoryActivity navigation works
- [ ] MainActivity → SettingsActivity navigation works
- [ ] ScanActivity → ResultActivity navigation works with data passing
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
- [ ] Screenshots of all 5 activities saved
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
   - MainActivity
   - ScanActivity
   - ResultActivity
   - HistoryActivity
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

## Success Criteria

You have successfully completed Week 02 Build Task when:

1. LeafGuard Android project runs without errors
2. All 5 activities are functional with proper layouts
3. Navigation flow works correctly between all activities
4. Resources are properly externalized and organized
5. Code follows Android best practices
6. Git repository shows progressive development
7. Evidence package is complete with screenshots and video
8. You can explain every aspect of your implementation

**Congratulations! You now have a solid Android UI foundation. Week 03 will add camera and gallery functionality to make the scan feature functional.**


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
