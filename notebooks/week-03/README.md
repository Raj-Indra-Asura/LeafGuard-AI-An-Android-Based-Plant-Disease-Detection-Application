# Week 03 Notebook: Camera, Gallery, URI, and Preview

This Markdown notebook is an optional companion to the Week 03 roadmap. The Kotlin roadmap remains the primary learning path:

```text
roadmap/week-03-camera-gallery/
```

Week 03 upgrades `ScanActivity` from a placeholder into an image-input screen. It does not add prediction, backend upload, database history, XML lookup, or AI.

---

## Cell 1: Image Input Flow

### Explanation

The app needs a leaf image before any future prediction can happen.

### Flow

```text
Home
  -> ScanActivity
      -> Take Photo -> camera URI -> preview
      -> Choose from Gallery -> selected URI -> preview
```

### Checkpoint

Why is preview enough for Week 03 validation?

---

## Cell 2: Camera Permission

### Explanation

Camera access is sensitive, so Android asks the user at runtime.

### Kotlin Snippet

```kotlin
private fun hasCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}
```

### Checkpoint

What should the app do if permission is denied?

---

## Cell 3: FileProvider

### Explanation

The camera app needs a safe place to write the captured photo. FileProvider gives the camera a `content://` URI instead of exposing a raw file path.

### Manifest Snippet

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

### Path Snippet

```xml
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-files-path name="leaf_images" path="Pictures/" />
</paths>
```

### Checkpoint

Why must the authority in Kotlin match the authority in the manifest?

---

## Cell 4: Gallery Picker

### Explanation

`GetContent` opens a system picker and returns the selected image URI.

### Kotlin Snippet

```kotlin
private val galleryLauncher = registerForActivityResult(
    ActivityResultContracts.GetContent()
) { uri ->
    if (uri != null) {
        updateSelectedImage(uri)
    }
}
```

### Checkpoint

What does `"image/*"` mean when launching the picker?

---

## Cell 5: Camera Capture

### Explanation

`TakePicture` opens the camera and writes the photo to a URI your app prepared first.

### Kotlin Snippet

```kotlin
private val cameraLauncher = registerForActivityResult(
    ActivityResultContracts.TakePicture()
) { success ->
    val cameraUri = pendingCameraUri
    if (success && cameraUri != null) {
        updateSelectedImage(cameraUri)
    }
}
```

### Checkpoint

Why does the app create a URI before opening the camera?

---

## Cell 6: Preview Update

### Explanation

Both camera and gallery finish with the same helper: store the URI and show it in the ImageView.

### Kotlin Snippet

```kotlin
private fun updateSelectedImage(uri: Uri) {
    selectedImageUri = uri
    imagePreview.setImageURI(uri)
    textImageStatus.setText(R.string.image_selected)
}
```

### Checkpoint

Why is one shared preview helper better than duplicating preview code in camera and gallery callbacks?

---

## Final Notebook Check

Before moving to Week 04, answer:

1. Why does image input belong in `ScanActivity`?
2. What does FileProvider protect?
3. What does `TakePicture` return?
4. What does `GetContent` return?
5. Which later week handles backend upload?


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

### Related Roadmap Materials
- 📖 [Week 03 README](../../roadmap/week-03-camera-gallery/README.md) — Week overview & objectives
- 📝 [Week 03 Exercises](../../roadmap/week-03-camera-gallery/exercises.md) — Practice problems
- 💡 [Week 03 Solutions](../../solutions/week-03/README.md) — Reference solutions
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

### Week Progression

| ← Previous | 🏠 | Next → |
|:-----------|:--:|-------:|
| [⬅ Week 02 Notebooks](../week-02/README.md) | [Notebooks Index](../README.md) | [Week 04 Notebooks ➡](../week-04/README.md) |

---