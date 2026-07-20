# Week 03 Learning Notes: Image Input From Zero

## Purpose

Week 03 teaches how an Android app receives an image from the user. You will learn only what is needed for camera capture, gallery selection, URI handling, and preview inside `ScanActivity`.

This week does not teach backend upload, image classification, database history, or TensorFlow Lite.

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

## 11. Week 03 Understanding Checklist

Before starting the build task, make sure you can answer:

- Why does the camera need permission?
- What is a URI?
- Why does the camera need FileProvider?
- What does `TakePicture` do?
- What does `GetContent` do?
- Where does image input live after Week 02?
- What must still wait for future weeks?

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
