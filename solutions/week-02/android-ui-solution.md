# Week 02 Solution - Android UI, Resources, and Navigation

This solution provides a working Java UI skeleton for LeafGuard AI.

---

## 1. Screen Flow for Week 02

```text
SplashActivity
    ↓
MainActivity
    ├── Camera / Gallery / Start Scan → ScanActivity
    ├── History → HistoryActivity
    └── Disease Library → DiseaseLibraryActivity
```

---

## 2. `res/layout/activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/leaf_background"
    tools:context=".MainActivity">

    <ImageView
        android:id="@+id/imageLogo"
        android:layout_width="120dp"
        android:layout_height="120dp"
        android:layout_marginTop="32dp"
        android:contentDescription="@string/app_logo"
        android:src="@android:drawable/ic_menu_camera"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/textTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="@string/app_name"
        android:textColor="@color/leaf_text_primary"
        android:textSize="28sp"
        android:textStyle="bold"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/imageLogo" />

    <TextView
        android:id="@+id/textSubtitle"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="24dp"
        android:layout_marginTop="8dp"
        android:layout_marginEnd="24dp"
        android:gravity="center"
        android:text="@string/home_subtitle"
        android:textColor="@color/leaf_text_secondary"
        android:textSize="16sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/textTitle" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/buttonCamera"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="24dp"
        android:layout_marginTop="32dp"
        android:layout_marginEnd="24dp"
        android:text="@string/open_camera"
        app:icon="@android:drawable/ic_menu_camera"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/textSubtitle" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/buttonGallery"
        style="@style/Widget.Material3.Button.OutlinedButton"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="24dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="24dp"
        android:text="@string/open_gallery"
        app:icon="@android:drawable/ic_menu_gallery"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/buttonCamera" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/buttonHistory"
        style="@style/Widget.Material3.Button.OutlinedButton"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="24dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="24dp"
        android:text="@string/view_history"
        app:icon="@android:drawable/ic_menu_recent_history"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/buttonGallery" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/buttonDiseaseLibrary"
        style="@style/Widget.Material3.Button.OutlinedButton"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="24dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="24dp"
        android:text="@string/disease_library"
        app:icon="@android:drawable/ic_menu_info_details"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/buttonHistory" />

    <TextView
        android:id="@+id/textWeekNote"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="24dp"
        android:layout_marginEnd="24dp"
        android:layout_marginBottom="24dp"
        android:gravity="center"
        android:text="@string/week_two_note"
        android:textColor="@color/leaf_text_secondary"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

---

## 3. `res/layout/activity_scan.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/leaf_background"
    tools:context=".ScanActivity">

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/topAppBar"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:backgroundTint="@color/leaf_green_700"
        android:title="@string/scan_leaf"
        android:titleTextColor="@android:color/white"
        app:navigationIcon="@android:drawable/ic_media_previous"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <ImageView
        android:id="@+id/imagePreview"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_marginStart="24dp"
        android:layout_marginTop="24dp"
        android:layout_marginEnd="24dp"
        android:background="@color/leaf_green_100"
        android:contentDescription="@string/plant_image_preview"
        android:padding="16dp"
        android:scaleType="centerCrop"
        android:src="@android:drawable/ic_menu_gallery"
        app:layout_constraintDimensionRatio="1:1"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/topAppBar" />

    <TextView
        android:id="@+id/textImageStatus"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="24dp"
        android:layout_marginTop="12dp"
        android:layout_marginEnd="24dp"
        android:gravity="center"
        android:text="@string/no_image_selected"
        android:textColor="@color/leaf_text_secondary"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/imagePreview" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/buttonCapture"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="24dp"
        android:layout_marginTop="24dp"
        android:layout_marginEnd="12dp"
        android:text="@string/capture_photo"
        app:layout_constraintEnd_toStartOf="@id/buttonPick"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/textImageStatus" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/buttonPick"
        style="@style/Widget.Material3.Button.OutlinedButton"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="12dp"
        android:layout_marginTop="24dp"
        android:layout_marginEnd="24dp"
        android:text="@string/select_from_gallery"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toEndOf="@id/buttonCapture"
        app:layout_constraintTop_toBottomOf="@id/textImageStatus" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/buttonAnalyze"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="24dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="24dp"
        android:enabled="false"
        android:text="@string/analyze_leaf"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/buttonCapture" />

    <com.google.android.material.progressindicator.CircularProgressIndicator
        android:id="@+id/progressIndicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:visibility="gone"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/buttonAnalyze" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

---

## 4. `MainActivity.java`

```java
package com.leafguard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.leafguard.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_SCAN_SOURCE = "extra_scan_source";
    public static final String SOURCE_CAMERA = "camera";
    public static final String SOURCE_GALLERY = "gallery";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.buttonCamera.setOnClickListener(view -> openScanScreen(SOURCE_CAMERA));
        binding.buttonGallery.setOnClickListener(view -> openScanScreen(SOURCE_GALLERY));
        binding.buttonHistory.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
        binding.buttonDiseaseLibrary.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DiseaseLibraryActivity.class);
            startActivity(intent);
        });
    }

    private void openScanScreen(String source) {
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        intent.putExtra(EXTRA_SCAN_SOURCE, source);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
```

---

## 5. `ScanActivity.java` skeleton

```java
package com.leafguard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leafguard.databinding.ActivityScanBinding;

public class ScanActivity extends AppCompatActivity {

    private ActivityScanBinding binding;
    private Uri selectedImageUri;
    private String launchSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        launchSource = getIntent().getStringExtra(MainActivity.EXTRA_SCAN_SOURCE);
        setupToolbar();
        setupButtons();
        updateUiForImage(null);

        if (MainActivity.SOURCE_CAMERA.equals(launchSource)) {
            Toast.makeText(this, R.string.scan_camera_entry_message, Toast.LENGTH_SHORT).show();
        } else if (MainActivity.SOURCE_GALLERY.equals(launchSource)) {
            Toast.makeText(this, R.string.scan_gallery_entry_message, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupToolbar() {
        binding.topAppBar.setNavigationOnClickListener(view -> finish());
    }

    private void setupButtons() {
        binding.buttonCapture.setOnClickListener(view ->
                Toast.makeText(this, R.string.week_three_camera_todo, Toast.LENGTH_SHORT).show());

        binding.buttonPick.setOnClickListener(view ->
                Toast.makeText(this, R.string.week_three_gallery_todo, Toast.LENGTH_SHORT).show());

        binding.buttonAnalyze.setOnClickListener(view -> openResultScreen());
    }

    private void updateUiForImage(Uri imageUri) {
        selectedImageUri = imageUri;
        binding.buttonAnalyze.setEnabled(imageUri != null);

        if (imageUri == null) {
            binding.imagePreview.setImageResource(android.R.drawable.ic_menu_gallery);
            binding.textImageStatus.setText(R.string.no_image_selected);
        } else {
            binding.imagePreview.setImageURI(imageUri);
            binding.textImageStatus.setText(getString(R.string.image_selected_message, imageUri.toString()));
        }
    }

    private void openResultScreen() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_DISEASE_NAME, "Tomato Healthy");
        intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, 0.93f);
        intent.putExtra(ResultActivity.EXTRA_SYMPTOMS, getString(R.string.placeholder_symptoms));
        intent.putExtra(ResultActivity.EXTRA_TREATMENT, getString(R.string.placeholder_treatment));
        intent.putExtra(ResultActivity.EXTRA_PREVENTION, getString(R.string.placeholder_prevention));
        if (selectedImageUri != null) {
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, selectedImageUri.toString());
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
```

---

## 6. Minimal `HistoryActivity.java`

```java
package com.leafguard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_placeholder);
    }
}
```

---

## 7. Minimal `DiseaseLibraryActivity.java`

```java
package com.leafguard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DiseaseLibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_library_placeholder);
    }
}
```

---

## 8. Placeholder layout for history screen

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/leaf_background">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="@string/history_placeholder"
        android:textColor="@color/leaf_text_primary"
        android:textSize="20sp"
        android:textStyle="bold" />

</FrameLayout>
```

---

## 9. Placeholder layout for disease library screen

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/leaf_background">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="@string/disease_library_placeholder"
        android:textColor="@color/leaf_text_primary"
        android:textSize="20sp"
        android:textStyle="bold" />

</FrameLayout>
```

---

## 10. `res/values/strings.xml`

```xml
<resources>
    <string name="app_name">LeafGuard AI</string>
    <string name="app_logo">LeafGuard AI logo</string>
    <string name="home_subtitle">AI-assisted plant disease detection for CSE 2206.</string>
    <string name="open_camera">Camera</string>
    <string name="open_gallery">Gallery</string>
    <string name="view_history">History</string>
    <string name="disease_library">Disease Library</string>
    <string name="week_two_note">Week 02 focuses on layouts, resources, activities, and intent navigation.</string>
    <string name="scan_leaf">Scan Leaf</string>
    <string name="plant_image_preview">Plant image preview</string>
    <string name="no_image_selected">No image selected yet.</string>
    <string name="capture_photo">Capture</string>
    <string name="select_from_gallery">Select</string>
    <string name="analyze_leaf">Analyze</string>
    <string name="scan_camera_entry_message">Scan screen opened from Camera button.</string>
    <string name="scan_gallery_entry_message">Scan screen opened from Gallery button.</string>
    <string name="week_three_camera_todo">Week 03 will connect the real camera contract.</string>
    <string name="week_three_gallery_todo">Week 03 will connect the real gallery picker.</string>
    <string name="image_selected_message">Image selected: %1$s</string>
    <string name="history_placeholder">History screen placeholder</string>
    <string name="disease_library_placeholder">Disease library screen placeholder</string>
    <string name="placeholder_symptoms">Symptoms will appear here after prediction.</string>
    <string name="placeholder_treatment">Treatment recommendation will appear here.</string>
    <string name="placeholder_prevention">Prevention recommendation will appear here.</string>
</resources>
```

---

## 11. `res/values/colors.xml`

```xml
<resources>
    <color name="leaf_background">#F4F8F2</color>
    <color name="leaf_green_100">#DDEFD7</color>
    <color name="leaf_green_300">#9BCF8E</color>
    <color name="leaf_green_700">#2E7D32</color>
    <color name="leaf_primary">#388E3C</color>
    <color name="leaf_accent">#FFB300</color>
    <color name="leaf_text_primary">#1F2937</color>
    <color name="leaf_text_secondary">#6B7280</color>
    <color name="white">#FFFFFFFF</color>
</resources>
```

---

## 12. `res/values/themes.xml`

```xml
<resources xmlns:tools="http://schemas.android.com/tools">

    <style name="Theme.LeafGuardAI" parent="Theme.Material3.DayNight.NoActionBar">
        <item name="colorPrimary">@color/leaf_primary</item>
        <item name="colorSecondary">@color/leaf_accent</item>
        <item name="android:statusBarColor">@color/leaf_green_700</item>
        <item name="android:navigationBarColor">@color/leaf_background</item>
        <item name="android:windowLightStatusBar" tools:targetApi="m">false</item>
    </style>

    <style name="Theme.LeafGuardAI.Splash" parent="Theme.Material3.DayNight.NoActionBar">
        <item name="android:windowBackground">@color/leaf_green_700</item>
    </style>

</resources>
```

---

## 13. `AndroidManifest.xml` entries for navigation

```xml
<application ...>
    <activity android:name=".DiseaseLibraryActivity" android:exported="false" />
    <activity android:name=".HistoryActivity" android:exported="false" />
    <activity android:name=".ResultActivity" android:exported="false" />
    <activity android:name=".ScanActivity" android:exported="false" />
    <activity android:name=".MainActivity" android:exported="false" />
    <activity android:name=".SplashActivity" android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
</application>
```

---

## 14. Why this satisfies Week 02

### Android Studio and UI skeleton
The app now has two real XML screens and four navigable activities.
### Lifecycle and intent practice
`MainActivity` starts other screens using explicit intents and passes the scan source as an extra.
### ConstraintLayout practice
Both layouts rely on `ConstraintLayout` and avoid unnecessary nesting.

### Resource management
Strings, colors, and themes are centralized under `res/values/`.

---

## 16. Final Checklist

- [x] `activity_main.xml` complete
- [x] `activity_scan.xml` complete
- [x] `MainActivity.java` complete
- [x] `ScanActivity.java` skeleton included
- [x] strings, colors, and themes provided
- [x] working activity navigation included
