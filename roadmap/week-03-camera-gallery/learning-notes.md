# Week 03 Learning Notes: Image Input From Zero

## Purpose

Week 03 teaches how an Android app receives an image from the user. You will learn only what is needed for camera capture, gallery selection, URI handling, and preview inside `ScanActivity`.

This week does not teach backend upload, image classification, database history, or TensorFlow Lite.

If you only want the answer to "what exactly will exist in my project when Week 03 ends?", jump to [section 11](#11-end-of-week-03-file-inventory-exact-files-exact-code-exact-size). It shows the one new file, the four changed files, the twelve untouched files, every line of code, and every line count.

---

## 1. How Week 03 Grows From Week 02

Week 02 built placeholder screens. The Scan screen said image input would be added later.

Week 03 fills that exact placeholder:

```text
Home screen
  -> Scan screen
      -> Take Photo
      -> Choose from Gallery
      -> Show selected image preview
```

Nothing else should be added yet.

---

## 2. Why Image Input Is a Separate Slice

LeafGuard AI needs an image before it can ever predict disease. But image input is its own Android skill:

- permissions
- file sharing
- launching system apps
- receiving result data
- previewing content safely
- handling cancellation

If you mix image input with backend upload or AI too early, debugging becomes hard. Week 03 keeps the slice narrow.

---

## 3. Permission: Asking Before Camera Use

Android protects sensitive features. Camera access is sensitive because it uses device hardware and can capture private information.

The flow is:

```text
User taps Take Photo
  -> app checks CAMERA permission
  -> if granted, launch camera
  -> if not granted, request permission
  -> if denied, show a helpful message
```

Minimal permission check:

```kotlin
private fun hasCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}
```

Line-by-line meaning:

| Code | Meaning |
|---|---|
| `ContextCompat.checkSelfPermission` | Ask Android whether permission is already granted. |
| `this` | Current Activity context. |
| `Manifest.permission.CAMERA` | The camera permission name. |
| `PackageManager.PERMISSION_GRANTED` | The value Android returns when permission is allowed. |

---

## 4. Activity Result API: Launch and Receive Result

In Week 02, an Intent opened another Activity. In Week 03, launchers open a system UI and return a result.

Two launchers are used:

| Launcher | Purpose |
|---|---|
| `RequestPermission` | Ask for camera permission. |
| `TakePicture` | Open camera and save photo to a URI. |
| `GetContent` | Open gallery/content picker and return a selected URI. |

Launcher rule:

> Register launchers before using them, usually as properties or during Activity setup before the user taps buttons.

---

## 5. URI: A Reference to an Image

A URI is not the image itself. It is a reference to where Android can read or write the image.

Examples:

```text
content://media/external/images/media/123
content://com.leafguard.fileprovider/leaf_images/photo.jpg
```

Beginner mental model:

> A URI is like a safe address for an image.

In Week 03, store the selected URI:

```kotlin
private var selectedImageUri: Uri? = null
```

The `?` means it can be null because the user may not have selected an image yet.

---

## 6. FileProvider: Safe Camera Output

The camera app needs somewhere to save the captured photo. Your app creates a file and gives the camera a safe URI for that file.

Do not use raw `file://` paths. Android 7+ blocks them because they expose private file paths.

Use FileProvider:

```kotlin
FileProvider.getUriForFile(
    this,
    "${BuildConfig.APPLICATION_ID}.fileprovider",
    imageFile
)
```

Line-by-line meaning:

| Code | Meaning |
|---|---|
| `FileProvider.getUriForFile` | Convert your app-owned file into a safe `content://` URI. |
| `this` | Current Activity context. |
| `BuildConfig.APPLICATION_ID` | The app ID, such as `com.leafguard`. |
| `.fileprovider` | Must match the provider authority in the manifest. |
| `imageFile` | The file where the camera should write the photo. |

---

## 7. Gallery Picker: Choose Existing Image

For Week 03, use `ActivityResultContracts.GetContent`. It asks Android to show a picker and returns a URI.

```kotlin
private val galleryLauncher = registerForActivityResult(
    ActivityResultContracts.GetContent()
) { uri ->
    if (uri != null) {
        updateSelectedImage(uri)
    }
}
```

Line-by-line meaning:

| Code | Meaning |
|---|---|
| `GetContent()` | Open a system picker for content. |
| `uri ->` | Callback receives the selected image URI. |
| `if (uri != null)` | User selected an image. If null, user cancelled. |
| `updateSelectedImage(uri)` | Store and show the image. |

Launch it with:

```kotlin
galleryLauncher.launch("image/*")
```

`"image/*"` means show image files only.

---

## 8. Preview: Show the Image in ScanActivity

Use an ImageView in the Scan layout.

```kotlin
private fun updateSelectedImage(uri: Uri) {
    selectedImageUri = uri
    imagePreview.setImageURI(uri)
    textImageStatus.text = getString(R.string.image_selected)
}
```

Line-by-line meaning:

| Code | Meaning |
|---|---|
| `selectedImageUri = uri` | Remember which image is selected. |
| `imagePreview.setImageURI(uri)` | Ask ImageView to display the image. |
| `textImageStatus.text = ...` | Tell the user an image is selected. |

Week 03 does not need model preprocessing. Later weeks can convert the URI to bytes or Bitmap when needed.

---

## 9. State: Keep the URI During Recreation

Android may recreate a screen during rotation or memory pressure. Save the URI string:

```kotlin
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("selected_image_uri", selectedImageUri?.toString())
}
```

Restore it:

```kotlin
savedInstanceState?.getString("selected_image_uri")?.let { uriText ->
    updateSelectedImage(Uri.parse(uriText))
}
```

This is enough for beginner Week 03. More robust persistence can be improved later.

---

## 10. What Week 03 Code Should Not Do

Do not add these yet:

- real disease detection
- fake disease detection
- backend upload
- Retrofit code
- Room history saving
- XML disease lookup
- TensorFlow Lite inference

Good Week 03 behavior:

```text
Image selected. Preview shown. Detection will be added later.
```

Bad Week 03 behavior:

```text
Pretend prediction: Tomato Disease, 90% confidence.
```

Fake predictions create confusion and break the weekly learning path.

---

## 11. End-of-Week-03 File Inventory (Exact Files, Exact Code, Exact Size)

Week 02 finished with 16 required Android files. Week 03 does **not** add many new files. It converts one placeholder into a real feature.

> Week 03 adds exactly **1 new file**, meaningfully rewrites **4 files**, and leaves the other **12 files untouched**.

Everything below describes the Kotlin primary track (`android-app-kotlin/`). See section 11.9 for the Java twin.

---

### 11.1 Change Summary: Week 02 → Week 03

| Change type | Count | Files |
|---|---:|---|
| New file | 1 | `res/xml/file_provider_paths.xml` |
| Rewritten | 2 | `ScanActivity.kt`, `res/layout/activity_scan.xml` |
| Extended | 2 | `AndroidManifest.xml`, `res/values/strings.xml` |
| Unchanged | 12 | 5 Activities, 5 layouts, `colors.xml`, `themes.xml` |
| Build files | 0 changed | Week 03 needs **no new Gradle dependency** |

**Required Android source/resource files at the end of Week 03: 17.**

**Total hand-written Android code at the end of Week 03: about 460 lines**, up from about 355. The whole week is roughly **+105 net lines**, and 120 of the changed lines live in one file.

The single most important fact: **Week 03 adds camera and gallery input using only libraries you already had in Week 02** (`androidx.appcompat` and `androidx.core`). The Activity Result API and `FileProvider` ship inside them. No new dependency line appears in `app/build.gradle`.

---

### 11.2 The Exact Week 03 Tree

```text
android-app-kotlin/
|-- settings.gradle                                   UNCHANGED  17 lines
|-- build.gradle                                      UNCHANGED   4 lines
|-- gradle.properties                                 UNCHANGED   3 lines
`-- app/
    |-- build.gradle                                  UNCHANGED  40 lines  <- no new dependency
    `-- src/main/
        |-- AndroidManifest.xml                       EXTENDED   53 lines  (was 37, +16)
        |-- java/com/leafguard/
        |   |-- MainActivity.kt                       UNCHANGED  34 lines  REAL
        |   |-- ScanActivity.kt                       REWRITTEN 132 lines  REAL  (was 12)
        |   |-- ResultActivity.kt                     UNCHANGED  12 lines  PLACEHOLDER
        |   |-- HistoryActivity.kt                    UNCHANGED  12 lines  PLACEHOLDER
        |   |-- DiseaseLibraryActivity.kt             UNCHANGED  12 lines  PLACEHOLDER
        |   `-- SettingsActivity.kt                   UNCHANGED  12 lines  PLACEHOLDER
        `-- res/
            |-- layout/
            |   |-- activity_main.xml                 UNCHANGED  56 lines  REAL
            |   |-- activity_scan.xml                 REWRITTEN  60 lines  REAL  (was 25)
            |   |-- activity_result.xml               UNCHANGED  25 lines  PLACEHOLDER
            |   |-- activity_history.xml              UNCHANGED  25 lines  PLACEHOLDER
            |   |-- activity_disease_library.xml      UNCHANGED  25 lines  PLACEHOLDER
            |   `-- activity_settings.xml             UNCHANGED  25 lines  PLACEHOLDER
            |-- values/
            |   |-- strings.xml                       EXTENDED   35 lines  (was 25, 29 strings)
            |   |-- colors.xml                        UNCHANGED   9 lines
            |   `-- themes.xml                        UNCHANGED   9 lines
            `-- xml/
                `-- file_provider_paths.xml           NEW         6 lines
```

---

### 11.3 New File: `res/xml/file_provider_paths.xml` (6 lines)

This is the only brand-new file of the week.

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-files-path
        name="leaf_images"
        path="Pictures/" />
</paths>
```

| Code | What it does |
|---|---|
| `<paths>` | Root element of a FileProvider configuration. The manifest points at this file. |
| `<external-files-path>` | Selects the app-private folder returned by `getExternalFilesDir(...)`. Uninstalling the app deletes it, and no storage permission is required. |
| `name="leaf_images"` | The public segment that appears inside the generated URI. The real folder path is hidden from the camera app. |
| `path="Pictures/"` | Only this subfolder may be shared. Nothing else in your app's storage is exposed. |

**Why 6 lines matter this much:** without this file, `FileProvider.getUriForFile(...)` throws `IllegalArgumentException: Failed to find configured root`, and the camera button crashes the app. This file is the security boundary of Week 03: it converts "share one folder of my own photos" into a declared, reviewable rule instead of an unrestricted `file://` path.

---

### 11.4 Rewritten File: `ScanActivity.kt` (12 lines → 132 lines)

Path: `app/src/main/java/com/leafguard/ScanActivity.kt`

This is where 90% of Week 03 lives.

```kotlin
package com.leafguard

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

class ScanActivity : AppCompatActivity() {

    private lateinit var imagePreview: ImageView
    private lateinit var textImageStatus: TextView

    private var selectedImageUri: Uri? = null
    private var pendingCameraUri: Uri? = null

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
        pendingCameraUri = null
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

    private fun openCameraWithPermissionCheck() {
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
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
            pendingCameraUri = null
            Toast.makeText(this, R.string.camera_file_error, Toast.LENGTH_SHORT).show()
        }
    }

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

    private fun updateSelectedImage(uri: Uri) {
        selectedImageUri = uri
        imagePreview.setImageURI(uri)
        textImageStatus.setText(R.string.image_selected)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SELECTED_IMAGE_URI, selectedImageUri?.toString())
    }

    companion object {
        private const val KEY_SELECTED_IMAGE_URI = "selected_image_uri"
    }
}
```

#### 11.4.1 Where the 132 lines go

| Block | Lines | Share | Job |
|---|---:|---:|---|
| Package + 16 imports | 18 | 14% | Names every Android API used below. |
| Class header + 4 fields | 7 | 5% | View references and the two URIs the screen remembers. |
| 3 result launchers | 32 | 24% | Permission, camera, gallery — each with a success and a failure path. |
| `onCreate` | 19 | 14% | Bind views, attach buttons, restore saved state. |
| `openCameraWithPermissionCheck` | 13 | 10% | Permission gate before any camera call. |
| `launchCamera` | 11 | 8% | Prepare the output URI, then open the camera. |
| `createImageUri` | 14 | 11% | Create the folder + file and convert it to a safe `content://` URI. |
| `updateSelectedImage` | 5 | 4% | The single place that changes preview + status. |
| `onSaveInstanceState` | 4 | 3% | Survive rotation. |
| `companion object` | 3 | 2% | The bundle key constant. |
| Blank lines + closing braces | 6 | 5% | Readability. |

Notice the shape: **roughly one third of the file is error/cancel handling.** In Week 03, "handled the sad path" is the feature, not an extra.

#### 11.4.2 Line-by-line meaning

**Imports (17 lines) — why each one is needed**

| Import | Needed for |
|---|---|
| `android.Manifest` | The constant `Manifest.permission.CAMERA`. |
| `android.content.pm.PackageManager` | The constant `PERMISSION_GRANTED`. |
| `android.net.Uri` | The type of every image reference in this screen. |
| `android.os.Bundle` | `onCreate` and `onSaveInstanceState` parameters. |
| `android.os.Environment` | `DIRECTORY_PICTURES`, the standard pictures subfolder name. |
| `android.widget.Button` / `ImageView` / `TextView` | The three view types bound from the layout. |
| `android.widget.Toast` | Short messages for denial, cancellation, and file errors. |
| `androidx.activity.result.contract.ActivityResultContracts` | The three ready-made contracts used by the launchers. |
| `androidx.appcompat.app.AppCompatActivity` | Base class, unchanged from Week 02. |
| `androidx.core.content.ContextCompat` | Version-safe permission check. |
| `androidx.core.content.FileProvider` | Turns an app file into a shareable `content://` URI. |
| `java.io.File` | The capture folder and capture file. |
| `java.io.IOException` | The failure type when the folder or file cannot be prepared. |

**Fields (4 lines)**

| Field | What it holds | Why this type |
|---|---|---|
| `imagePreview: ImageView` | The preview view. | `lateinit` because it is assigned in `onCreate`, after the layout is inflated. |
| `textImageStatus: TextView` | The status line. | Same reason. |
| `selectedImageUri: Uri?` | The image the user has chosen right now. | Nullable, because at first launch nothing is selected. |
| `pendingCameraUri: Uri?` | The file URI handed to the camera app before it opens. | Nullable, because it only exists between "camera launched" and "camera returned". |

**Why two URI fields?** `TakePicture` returns only `true`/`false`, never the image. Your app must remember where it told the camera to write. `selectedImageUri` is "what the user has", `pendingCameraUri` is "what I am waiting for". Merging them would show a stale preview after a cancelled capture.

**Launcher 1: `cameraPermissionLauncher` (9 lines)**

| Code | What it does |
|---|---|
| `registerForActivityResult(...)` | Registers a callback with the Activity **before** it starts. Registering later throws `IllegalStateException`. |
| `ActivityResultContracts.RequestPermission()` | The contract: input is a permission name, output is a `Boolean`. |
| `{ granted -> ... }` | Runs after the user answers the system dialog. |
| `if (granted) launchCamera()` | Continue the flow the user originally asked for. |
| `else Toast(camera_permission_denied)` | Explain and keep going. Gallery still works, so denial is not fatal. |

**Launcher 2: `cameraLauncher` (11 lines)**

| Code | What it does |
|---|---|
| `ActivityResultContracts.TakePicture()` | Contract: input is a `Uri` to write to, output is `Boolean` success. |
| `val cameraUri = pendingCameraUri` | Copy to a local `val` so Kotlin can smart-cast it to non-null. |
| `if (success && cameraUri != null)` | Both must be true: the camera reported success **and** we know the target. |
| `updateSelectedImage(cameraUri)` | Promote the pending capture into the selected image. |
| `else Toast(camera_cancelled)` | User pressed back in the camera app. Normal, not an error. |
| `pendingCameraUri = null` | Clear the pending slot either way so a later cancel cannot resurrect an old file. |

**Launcher 3: `galleryLauncher` (9 lines)**

| Code | What it does |
|---|---|
| `ActivityResultContracts.GetContent()` | Contract: input is a MIME filter string, output is `Uri?`. |
| `if (uri != null)` | The user picked something. |
| `else Toast(gallery_cancelled)` | `null` means the picker was dismissed. |

**Why the gallery needs no permission:** `GetContent()` opens a *system* picker. The user selects the file, and Android grants your app read access to that one item. You never gain access to the whole gallery, so no storage permission is requested. This is the single most useful privacy idea in Week 03.

**`onCreate` (19 lines)**

| Code | What it does |
|---|---|
| `setContentView(R.layout.activity_scan)` | Inflates the rewritten Scan layout. |
| `imagePreview = findViewById(R.id.imagePreview)` | Caches the preview view once instead of searching on every update. |
| `textImageStatus = findViewById(R.id.textImageStatus)` | Same for the status line. |
| `buttonTakePhoto` listener | Never launches the camera directly; always goes through the permission gate. |
| `buttonChooseGallery` listener | Launches directly, because no permission is required. |
| `galleryLauncher.launch("image/*")` | `"image/*"` filters the picker to images of any format. |
| `savedInstanceState?.getString(KEY_...)?.let { ... }` | If Android recreated the screen, rebuild the preview from the saved URI string. |

**`openCameraWithPermissionCheck` (13 lines)**

| Code | What it does |
|---|---|
| `ContextCompat.checkSelfPermission(...)` | Reads the current grant state without showing any dialog. |
| `== PackageManager.PERMISSION_GRANTED` | Comparison against Android's granted constant. |
| `if (granted) launchCamera()` | Already allowed: no dialog, straight to camera. |
| `else cameraPermissionLauncher.launch(...)` | Not allowed yet: ask, and let the callback continue the flow. |

Asking every time would annoy the user; never asking would crash on Android 6.0+. Checking first is the correct middle path.

**`launchCamera` (11 lines)**

| Code | What it does |
|---|---|
| `try { ... }` | File creation can fail on full or unavailable storage. |
| `val imageUri = createImageUri()` | Prepare the destination **before** opening the camera. |
| `pendingCameraUri = imageUri` | Remember it so the result callback knows what was written. |
| `cameraLauncher.launch(imageUri)` | Hand the URI to the camera app. |
| `catch (exception: IOException)` | Turn a storage failure into a message instead of a crash. |
| `pendingCameraUri = null` (in catch) | Never leave a stale pending URI behind after a failure. |

**`createImageUri` (14 lines)**

| Code | What it does |
|---|---|
| `@Throws(IOException::class)` | Documents the failure mode; also required for Java interop. |
| `getExternalFilesDir(Environment.DIRECTORY_PICTURES)` | App-private pictures folder. No storage permission needed; removed on uninstall. |
| `File(..., "captures")` | Week 03 keeps captures in their own subfolder — the same folder `file_provider_paths.xml` exposes. |
| `if (!exists() && !mkdirs()) throw IOException(...)` | Create the folder, and fail loudly if creation is impossible. |
| `"leafguard_${System.currentTimeMillis()}.jpg"` | Millisecond timestamp gives a unique name so captures never overwrite each other. |
| `FileProvider.getUriForFile(this, "...fileprovider", imageFile)` | Converts the private `File` into a `content://` URI the camera app is allowed to write to. |
| `"${BuildConfig.APPLICATION_ID}.fileprovider"` | Must match `${applicationId}.fileprovider` in the manifest exactly, or this throws. |

**`updateSelectedImage` (5 lines)** — the single point of truth

| Code | What it does |
|---|---|
| `selectedImageUri = uri` | Records the current selection for later weeks (upload in Week 05). |
| `imagePreview.setImageURI(uri)` | Asks the ImageView to load and draw the image. |
| `textImageStatus.setText(R.string.image_selected)` | Honest status text: image received, detection still to come. |

Camera success, gallery success, and state restoration all call this one function. Three entry points, one behavior — that is why the preview can never disagree with `selectedImageUri`.

**`onSaveInstanceState` (4 lines) + `companion object` (3 lines)**

| Code | What it does |
|---|---|
| `super.onSaveInstanceState(outState)` | Lets Android save its own view state first. |
| `outState.putString(KEY_..., selectedImageUri?.toString())` | Bundles cannot hold arbitrary objects safely, so the URI is stored as text. `?.` writes `null` when nothing is selected. |
| `private const val KEY_SELECTED_IMAGE_URI` | One named constant used by both save and restore, so a typo cannot silently break restoration. |

Week 03 state handling stops here. Surviving process death and persisting across app restarts is a Week 07 database concern.

---

### 11.5 Rewritten File: `res/layout/activity_scan.xml` (25 lines → 60 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screen_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="24dp">

        <TextView
            android:id="@+id/textScanTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/scan_title"
            android:textColor="@color/text_primary"
            android:textSize="24sp" />

        <TextView
            android:id="@+id/textScanInstruction"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="@string/scan_instruction"
            android:textColor="@color/text_secondary"
            android:textSize="16sp" />

        <ImageView
            android:id="@+id/imagePreview"
            android:layout_width="match_parent"
            android:layout_height="280dp"
            android:layout_marginTop="20dp"
            android:background="#E8F5E9"
            android:contentDescription="@string/scan_preview_description"
            android:scaleType="centerCrop" />

        <TextView
            android:id="@+id/textImageStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="@string/no_image_selected"
            android:textColor="@color/text_secondary" />

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
</ScrollView>
```

What changed from the 25-line Week 02 placeholder:

| Change | Lines | Why |
|---|---:|---|
| `ScrollView` wrapper added | +6 | A 280dp preview plus two buttons overflows small screens in landscape. |
| Instruction `TextView` replaces the placeholder `TextView` | 0 net | The screen no longer defers; it tells the user what to do. |
| `ImageView` added | +9 | The preview surface. |
| Status `TextView` added | +7 | Reports selection, cancellation, and denial. |
| Two `Button`s added | +12 | The two ways to provide an image. |
| **Total** | **+35** | 25 → 60 lines |

| Attribute | What it does |
|---|---|
| `ScrollView` | Allows exactly one direct child and makes it scrollable. |
| Inner `LinearLayout` with `layout_height="wrap_content"` | Grows to its content height; the ScrollView scrolls whatever exceeds the screen. |
| `layout_height="280dp"` on the ImageView | Fixed preview height so the layout does not jump when a new image loads. |
| `background="#E8F5E9"` | A pale panel so the empty preview is visible before any selection. This is the one place a raw hex is tolerated; move it into `colors.xml` when you polish. |
| `contentDescription="@string/scan_preview_description"` | Screen-reader text. Every meaningful `ImageView` needs one. |
| `scaleType="centerCrop"` | Fills the box while preserving aspect ratio, cropping the overflow. |
| `@+id/imagePreview`, `@+id/textImageStatus`, `@+id/buttonTakePhoto`, `@+id/buttonChooseGallery` | The four ids `ScanActivity.kt` looks up. Rename one side only and the app compiles but crashes. |

Note what is **still absent**: no "Detect" button, no progress bar, no confidence text. There is nothing to detect until Weeks 05–06.

---

### 11.6 Extended File: `AndroidManifest.xml` (37 lines → 53 lines)

Two additions: a permission block above `<application>`, and a provider inside it.

```xml
    <uses-feature
        android:name="android.hardware.camera"
        android:required="false" />

    <uses-permission android:name="android.permission.CAMERA" />
```

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

| Code | What it does |
|---|---|
| `<uses-feature android:name="android.hardware.camera" android:required="false"/>` | Declares that the app uses a camera but still installs on devices without one. With `required="true"`, the Play Store would hide the app from those devices. |
| `<uses-permission android:name="android.permission.CAMERA"/>` | Declares the permission. On Android 6.0+ this only makes the runtime request *possible*; `ContextCompat.checkSelfPermission` still decides. |
| `<provider android:name="androidx.core.content.FileProvider">` | Registers the library FileProvider. You write no provider class of your own. |
| `android:authorities="${applicationId}.fileprovider"` | The unique authority. `${applicationId}` is substituted by Gradle, so it stays in sync with `app/build.gradle` and matches `BuildConfig.APPLICATION_ID` in `createImageUri()`. |
| `android:exported="false"` | Other apps cannot browse the provider. |
| `android:grantUriPermissions="true"` | Allows *temporary, per-URI* write access to the camera app for one capture. |
| `<meta-data ... android:resource="@xml/file_provider_paths"/>` | Points at the 6-line file from section 11.3 that lists the shareable folders. |

Three names must agree or the camera button crashes:

```text
app/build.gradle          applicationId "com.leafguard"
AndroidManifest.xml       android:authorities="${applicationId}.fileprovider"
ScanActivity.kt           "${BuildConfig.APPLICATION_ID}.fileprovider"
```

Still absent at the end of Week 03: `INTERNET` permission, `networkSecurityConfig`, `POST_NOTIFICATIONS`.

---

### 11.7 Extended File: `res/values/strings.xml` (25 lines → 35 lines, 20 → 29 strings)

`placeholder_scan` is **deleted** (the Scan screen is no longer a placeholder), `scan_title` changes from `Scan` to `Scan Leaf`, and 10 strings are added:

```xml
    <string name="scan_instruction">Take a photo or choose an image from your device.</string>
    <string name="scan_preview_description">Preview of the selected leaf image</string>
    <string name="take_photo">Take Photo</string>
    <string name="choose_from_gallery">Choose from Gallery</string>
    <string name="no_image_selected">No image selected yet.</string>
    <string name="image_selected">Image selected. Detection will be added later.</string>
    <string name="camera_permission_denied">Camera permission denied. You can still choose from gallery.</string>
    <string name="camera_cancelled">Camera cancelled. No image selected.</string>
    <string name="gallery_cancelled">Gallery closed. No image selected.</string>
    <string name="camera_file_error">Could not prepare a file for the camera.</string>
</resources>
```

| String | Where it appears | Purpose |
|---|---|---|
| `scan_instruction` | Layout, under the title | Tells the user there are two ways to provide an image. |
| `scan_preview_description` | `contentDescription` of the ImageView | Accessibility text for screen readers. |
| `take_photo`, `choose_from_gallery` | The two button labels | The two entry points of the feature. |
| `no_image_selected` | Initial status text | Honest empty state. |
| `image_selected` | Set after any successful selection | Confirms receipt **and** defers detection in the same sentence. |
| `camera_permission_denied` | Toast from the permission launcher | Denial is recoverable: it names the gallery alternative. |
| `camera_cancelled` | Toast from the camera launcher | Cancellation is normal, not an error. |
| `gallery_cancelled` | Toast from the gallery launcher | Same, for the picker. |
| `camera_file_error` | Toast from the `catch` in `launchCamera` | The only genuine error message of the week. |

Four of these 10 strings exist purely for failure and cancellation paths. **Week 03 is 40% about things going wrong.**

---

### 11.8 The 12 Files Week 03 Does Not Touch

| File | Status at end of Week 03 | Next change |
|---|---|---|
| `MainActivity.kt` | Unchanged, 34 lines, real navigation | Week 10 UI polish |
| `activity_main.xml` | Unchanged, 56 lines | Week 10 UI polish |
| `ResultActivity.kt` + `activity_result.xml` | Still placeholder | Weeks 05–06 |
| `HistoryActivity.kt` + `activity_history.xml` | Still placeholder | Week 07 |
| `DiseaseLibraryActivity.kt` + `activity_disease_library.xml` | Still placeholder | Week 08 |
| `SettingsActivity.kt` + `activity_settings.xml` | Still placeholder | Week 10 |
| `colors.xml`, `themes.xml` | Unchanged, 9 lines each | Week 10 |
| `app/build.gradle` | Unchanged, 40 lines, 4 dependencies | Week 05 adds Retrofit |

**Four of the six screens are still placeholders at the end of Week 03.** That is correct. Progress is 25%, not 60%.

Explicitly forbidden at the end of Week 03:

- any Retrofit, OkHttp, or `HttpURLConnection` call
- any `Bitmap` resize, normalization, or tensor preprocessing
- any Room entity, DAO, or database class
- any `assets/labels.txt`, `assets/model.tflite`, or `assets/diseases.xml`
- any hardcoded or randomly generated "prediction" text on the Result screen

If the Scan screen can show a disease name at the end of Week 03, that name is fabricated, and Weeks 05–06 will be debugged against a lie.

---

### 11.9 The Java Twin (`android-app/`)

| Kotlin | Java twin | Difference |
|---|---|---|
| `ScanActivity.kt` (132 lines) | `ScanActivity.java` (about 160 lines) | Java has no `?.`/`let`, needs explicit null checks, `ActivityResultLauncher<...>` field types, and `registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {...})`. |
| `Uri?` | `@Nullable Uri` + `if (uri != null)` | Kotlin's nullable type becomes a manual check. |
| `"${BuildConfig.APPLICATION_ID}.fileprovider"` | `BuildConfig.APPLICATION_ID + ".fileprovider"` | String templates become concatenation. |
| `companion object { const val ... }` | `private static final String ...` | Same constant, different syntax. |

`activity_scan.xml`, `file_provider_paths.xml`, `AndroidManifest.xml`, and `strings.xml` are identical in both tracks.

---

### 11.10 How to Verify Your Week 03 End State

```bash
# 1. ScanActivity should be roughly 130 lines; every other Activity unchanged
wc -l android-app-kotlin/app/src/main/java/com/leafguard/*.kt

# 2. The one new file must exist
ls android-app-kotlin/app/src/main/res/xml/file_provider_paths.xml

# 3. Manifest must declare camera permission and the provider
grep -E "permission.CAMERA|FileProvider|fileprovider" android-app-kotlin/app/src/main/AndroidManifest.xml

# 4. No new dependency may have appeared: still 4 implementation lines
grep -c "implementation" android-app-kotlin/app/build.gradle

# 5. No future-week packages or assets: expect no output
ls android-app-kotlin/app/src/main/java/com/leafguard/ | grep -E "network|database|ml|utils"
ls android-app-kotlin/app/src/main/assets 2>/dev/null || echo "no assets"

# 6. Build
cd android-app-kotlin && ./gradlew assembleDebug
```

Then run the four behavior tests that define Week 03 as done:

| Test | Expected result |
|---|---|
| Pick a gallery image | Preview updates, status shows `image_selected`. |
| Take a photo and accept it | Preview updates, status shows `image_selected`. |
| Press back inside the camera | Toast `camera_cancelled`, previous preview unchanged, no crash. |
| Deny camera permission | Toast `camera_permission_denied`, gallery button still works, no crash. |

Save the command output and the four screenshots in `docs/evidence/week-03/`.

---

## 12. Week 03 Understanding Checklist

Before starting the build task, make sure you can answer:

- Why does the camera need permission?
- What is a URI?
- Why does the camera need FileProvider?
- What does `TakePicture` do?
- What does `GetContent` do?
- Where does image input live after Week 02?
- What must still wait for future weeks?
- Which single file is new in Week 03, and which four files change?

If you can answer these in your own words, continue to `exercises.md`.

<!-- NAV_FOOTER_START -->

---

## 📚 Week 03 — Navigation

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
| [⬅ Week 02: Android Basics & UI](../week-02-android-basics-ui/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 04: FastAPI Backend ➡](../week-04-fastapi-backend/README.md) |

---
