# Week 03 Build Task: Add Image Input to ScanActivity

## Objective

Upgrade the Week 02 `ScanActivity` placeholder into a real image-input screen.

By the end, users can:

- open Scan from Home
- take a photo with the camera
- choose an image from the gallery/content picker
- see the selected image in the Scan preview
- cancel or deny permission without crashing the app

Estimated time: 8 to 10 hours.

---

## Before You Start

Confirm:

- [ ] Week 02 navigation shell is complete.
- [ ] `ScanActivity` exists and opens from Home.
- [ ] You understand Activity, XML layout, manifest, and Intent at a beginner level.
- [ ] You know Week 03 does not implement detection, backend upload, database history, XML disease guidance, or AI.

---

## Target Evidence Folder

Save Week 03 evidence in:

```text
docs/evidence/week-03/
```

Suggested structure:

```text
docs/evidence/week-03/
|-- screenshots/
|-- exercises/
|-- build-notes.md
|-- validation.md
|-- quiz-answers.md
`-- reflection-answers.md
```

---

## Step 1: Add Camera Permission and FileProvider

Open `AndroidManifest.xml`.

Add camera permission above `<application>`:

```xml
<uses-feature
    android:name="android.hardware.camera"
    android:required="false" />

<uses-permission android:name="android.permission.CAMERA" />
```

Inside `<application>`, add FileProvider:

```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_provider_paths" />
</provider>
```

### Why this XML exists

| XML | Meaning |
|---|---|
| `uses-feature camera required=false` | The app can run on devices without a camera, but camera features may be unavailable. |
| `uses-permission CAMERA` | The app may request camera access at runtime. |
| `FileProvider` | Gives the camera app a safe `content://` URI for saving a photo. |
| `${applicationId}.fileprovider` | Authority based on the app ID, avoiding hardcoded package mismatch. |
| `grantUriPermissions=true` | Allows temporary URI access for the camera app. |

---

## Step 2: Add FileProvider Paths

Create `res/xml/file_provider_paths.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-files-path
        name="leaf_images"
        path="Pictures/" />
</paths>
```

### Why this XML exists

- `external-files-path` maps to your app-specific external files directory.
- `Pictures/` is where camera photos can be written for this app.
- This does not require giving your app broad access to all user storage.

### Check

- [ ] Project builds after manifest and XML changes.
- [ ] FileProvider authority in manifest ends with `.fileprovider`.

---

## Step 3: Add Week 03 Strings

In `strings.xml`, add:

```xml
<string name="scan_title">Scan Leaf</string>
<string name="scan_instruction">Take a photo or choose an image from your device.</string>
<string name="take_photo">Take Photo</string>
<string name="choose_from_gallery">Choose from Gallery</string>
<string name="no_image_selected">No image selected yet.</string>
<string name="image_selected">Image selected. Detection will be added later.</string>
<string name="camera_permission_denied">Camera permission denied. You can still choose from gallery.</string>
<string name="camera_cancelled">Camera cancelled. No image selected.</string>
<string name="gallery_cancelled">Gallery closed. No image selected.</string>
<string name="camera_file_error">Could not prepare a file for the camera.</string>
```

### Why this exists

- User-visible messages stay centralized.
- Permission denial and cancellation are treated as normal states, not crashes.

---

## Step 4: Upgrade the Scan Layout

Update `activity_scan.xml` so it has a preview and two image input buttons.

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/scan_title"
        android:textSize="24sp" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="@string/scan_instruction" />

    <ImageView
        android:id="@+id/imagePreview"
        android:layout_width="match_parent"
        android:layout_height="280dp"
        android:layout_marginTop="20dp"
        android:background="#E8F5E9"
        android:contentDescription="@string/scan_title"
        android:scaleType="centerCrop" />

    <TextView
        android:id="@+id/textImageStatus"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="@string/no_image_selected" />

    <Button
        android:id="@+id/buttonTakePhoto"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:text="@string/take_photo" />

    <Button
        android:id="@+id/buttonChooseGallery"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/choose_from_gallery" />
</LinearLayout>
```

### Why this XML exists

- `ImageView` displays the chosen image.
- `textImageStatus` tells the user what happened.
- `buttonTakePhoto` starts the camera flow.
- `buttonChooseGallery` starts the gallery flow.
- No Detect button is needed yet. If you keep one, it must be disabled or clearly future-labeled.

---

## Step 5: Add ScanActivity Imports and Fields

Open `ScanActivity.kt`.

Use these imports:

```kotlin
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
```

Add fields inside the class:

```kotlin
private lateinit var imagePreview: ImageView
private lateinit var textImageStatus: TextView
private var selectedImageUri: Uri? = null
private var pendingCameraUri: Uri? = null
```

### Why this code exists

| Code | Meaning |
|---|---|
| `imagePreview` | The ImageView where the selected image appears. |
| `textImageStatus` | Status message below the preview. |
| `selectedImageUri` | The image currently selected by camera or gallery. |
| `pendingCameraUri` | The file URI prepared before launching the camera. |

---

## Step 6: Register Activity Result Launchers

Add these properties inside `ScanActivity`:

```kotlin
private val cameraPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
) { granted ->
    if (granted) {
        launchCamera()
    } else {
        Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show()
    }
}

private val cameraLauncher = registerForActivityResult(
    ActivityResultContracts.TakePicture()
) { success ->
    val cameraUri = pendingCameraUri
    if (success && cameraUri != null) {
        updateSelectedImage(cameraUri)
    } else {
        Toast.makeText(this, R.string.camera_cancelled, Toast.LENGTH_SHORT).show()
    }
}

private val galleryLauncher = registerForActivityResult(
    ActivityResultContracts.GetContent()
) { uri ->
    if (uri != null) {
        updateSelectedImage(uri)
    } else {
        Toast.makeText(this, R.string.gallery_cancelled, Toast.LENGTH_SHORT).show()
    }
}
```

### Why this code exists

| Launcher | Meaning |
|---|---|
| `cameraPermissionLauncher` | Requests camera permission and continues only if granted. |
| `cameraLauncher` | Opens camera and receives success/failure. |
| `galleryLauncher` | Opens gallery/content picker and receives an image URI or null. |

---

## Step 7: Wire Buttons in onCreate

Replace or update `onCreate`:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scan)

    imagePreview = findViewById(R.id.imagePreview)
    textImageStatus = findViewById(R.id.textImageStatus)

    findViewById<Button>(R.id.buttonTakePhoto).setOnClickListener {
        openCameraWithPermissionCheck()
    }

    findViewById<Button>(R.id.buttonChooseGallery).setOnClickListener {
        galleryLauncher.launch("image/*")
    }

    savedInstanceState?.getString(KEY_SELECTED_IMAGE_URI)?.let { uriText ->
        updateSelectedImage(Uri.parse(uriText))
    }
}
```

Add this constant:

```kotlin
companion object {
    private const val KEY_SELECTED_IMAGE_URI = "selected_image_uri"
}
```

### Why this code exists

| Code | Meaning |
|---|---|
| `findViewById` | Connect Kotlin fields/buttons to XML views. |
| `buttonTakePhoto` listener | Starts the permission-then-camera flow. |
| `buttonChooseGallery` listener | Opens image picker directly. |
| `savedInstanceState` block | Restores the image URI if Android recreated the Activity. |

---

## Step 8: Add Permission and Camera Helpers

```kotlin
private fun openCameraWithPermissionCheck() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
        PackageManager.PERMISSION_GRANTED
    ) {
        launchCamera()
    } else {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}

private fun launchCamera() {
    try {
        val imageUri = createImageUri()
        pendingCameraUri = imageUri
        cameraLauncher.launch(imageUri)
    } catch (exception: IOException) {
        Toast.makeText(this, R.string.camera_file_error, Toast.LENGTH_SHORT).show()
    }
}
```

### Why this code exists

- `openCameraWithPermissionCheck` prevents camera launch without permission.
- `launchCamera` creates the output URI before opening camera.
- `try/catch` prevents file preparation errors from crashing the app.

---

## Step 9: Create the Camera Output URI

```kotlin
@Throws(IOException::class)
private fun createImageUri(): Uri {
    val imageDirectory = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captures")
    if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
        throw IOException("Could not create image directory")
    }

    val imageFile = File(imageDirectory, "leafguard_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        this,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
}
```

### Why this code exists
| Code | Meaning |
|---|---|
| `getExternalFilesDir(...)` | Gets an app-specific folder for pictures. |
| `captures` | Keeps Week 03 camera images organized. |
| `System.currentTimeMillis()` | Creates a unique filename. |
| `FileProvider.getUriForFile` | Converts the file to a safe URI for the camera app. |

---

## Step 10: Update Preview and Save State

```kotlin
private fun updateSelectedImage(uri: Uri) {
    selectedImageUri = uri
    imagePreview.setImageURI(uri)
    textImageStatus.setText(R.string.image_selected)
}

override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString(KEY_SELECTED_IMAGE_URI, selectedImageUri?.toString())
}
```

### Why this code exists

- `selectedImageUri = uri` remembers the image for future weeks.
- `setImageURI` shows the image.
- `image_selected` tells the user the app received the image.
- `onSaveInstanceState` helps preserve the URI through simple recreation.

---

## Step 11: Build and Test

Run:

```bash
cd android-app-kotlin
./gradlew assembleDebug
```

Then test on emulator or device:

1. Open app.
2. Open Scan.
3. Choose from gallery.
4. Verify preview updates.
5. Take photo.
6. Verify preview updates.
7. Cancel gallery and camera.
8. Deny camera permission.

---

## Evidence to Save

Save under `docs/evidence/week-03/`:

- Scan screen before image
- gallery picker opened
- gallery image preview
- camera permission dialog
- camera image preview
- cancellation/denial behavior note
- build success output
- short demo video if possible

---

## Done Means

Week 03 is done when:

- camera capture works or fails gracefully on the test device
- gallery selection works or fails gracefully
- selected image previews in ScanActivity
- no future-week behavior is faked
- evidence is saved
- validation checklist passes

Do not move to Week 04 until this is true.

<!-- NAV_FOOTER_START -->

---

## 📚 Week 03 — Navigation

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
| [⬅ Week 02: Android Basics & UI](../week-02-android-basics-ui/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 04: FastAPI Backend ➡](../week-04-fastapi-backend/README.md) |

---
