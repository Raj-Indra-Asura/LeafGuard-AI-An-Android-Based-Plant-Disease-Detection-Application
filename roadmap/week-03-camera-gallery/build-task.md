# Week 03 Build Task: Implement Camera & Gallery Image Input

## Objective

Add camera capture and gallery image selection to LeafGuard's **MainActivity**. By completion, users can capture photos with camera or select images from gallery, with proper permission handling and image preview—all within the main screen. The app uses modern Activity Result APIs (not the deprecated `startActivityForResult`).

> **Note:** LeafGuard keeps camera/gallery logic in `MainActivity` so users can capture a leaf, see a preview, and run detection without extra navigation. There is no separate `ScanActivity`.

## Implementation Steps

### Step 1: Add Permissions to Manifest (15 min)

**AndroidManifest.xml:**
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />

<application ...>
    <provider
        android:name="androidx.core.content.FileProvider"
        android:authorities="${applicationId}.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_paths" />
    </provider>
</application>
```

**res/xml/file_paths.xml:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-files-path name="my_images" path="Pictures/" />
</paths>
```

### Step 2: Implement Permission Handling (45 min)

In LeafGuard, permission handling lives in **MainActivity.kt**. The app uses `registerForActivityResult` with `ActivityResultContracts.RequestMultiplePermissions()` to request CAMERA or storage permissions, then proceeds to launch the camera or gallery on success.

**Kotlin (primary) — MainActivity.kt:**

```kotlin
private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
private var pendingPermissionAction: String? = null

private fun setupActivityResults() {
    permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val allGranted = result.values.all { it }
        if (!allGranted) {
            Toast.makeText(this, R.string.permissions_required_message, Toast.LENGTH_SHORT).show()
            pendingPermissionAction = null
            return@registerForActivityResult
        }
        when (pendingPermissionAction) {
            ACTION_CAMERA -> launchCamera()
            ACTION_GALLERY -> galleryLauncher.launch("image/*")
        }
        pendingPermissionAction = null
    }
    // TODO: Also register galleryLauncher and cameraLauncher here
}

private fun hasPermissions(permissions: Array<String>): Boolean {
    return permissions.all { permission ->
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }
}

private fun openCameraWithPermissionCheck() {
    if (hasPermissions(arrayOf(Manifest.permission.CAMERA))) {
        launchCamera()
        return
    }
    pendingPermissionAction = ACTION_CAMERA
    permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
}
```

**Java (secondary):**

```java
private ActivityResultLauncher<String> cameraPermissionLauncher;
private ActivityResultLauncher<String> storagePermissionLauncher;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setupPermissionLaunchers();
    setupButtons();
}

private void setupPermissionLaunchers() {
    cameraPermissionLauncher = registerForActivityResult(
        new ActivityResultContracts.RequestPermission(),
        isGranted -> {
            if (isGranted) {
                openCamera();
            } else {
                handlePermissionDenied("Camera");
            }
        }
    );

    storagePermissionLauncher = registerForActivityResult(
        new ActivityResultContracts.RequestPermission(),
        isGranted -> {
            if (isGranted) {
                openGallery();
            } else {
                handlePermissionDenied("Storage");
            }
        }
    );
}

private void checkCameraPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
        openCamera();
    } else {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
    }
}

private void handlePermissionDenied(String permissionName) {
    Toast.makeText(this, permissionName + " permission denied", Toast.LENGTH_SHORT).show();
}
```

### Step 3: Implement Camera Capture (60 min)

LeafGuard uses `ActivityResultContracts.TakePicture()` which accepts a Uri where the camera should save the photo. The authority for `FileProvider` must be `${applicationId}.fileprovider` (i.e., `"${BuildConfig.APPLICATION_ID}.fileprovider"` in code).

**Kotlin (primary):**

```kotlin
private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
private var pendingCameraUri: Uri? = null

private fun setupCameraLauncher() {
    cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        val cameraUri = pendingCameraUri
        if (success && cameraUri != null) {
            updateSelectedImage(cameraUri)
        } else {
            Toast.makeText(this, R.string.camera_cancelled_message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Throws(IOException::class)
private fun createImageUri(): Uri {
    val imageDirectory = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captures")
    if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
        throw IOException("Could not create image directory")
    }
    val imageFile = File(imageDirectory, "leafguard_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.fileprovider", imageFile)
}

private fun launchCamera() {
    try {
        pendingCameraUri = createImageUri()
        cameraLauncher.launch(pendingCameraUri!!)
    } catch (exception: IOException) {
        Toast.makeText(this, getString(R.string.camera_prepare_error, exception.message), Toast.LENGTH_LONG).show()
    }
}
```

**Java (secondary):**

```java
private Uri capturedImageUri;
private ActivityResultLauncher<Uri> takePictureLauncher;

private void setupCameraLauncher() {
    takePictureLauncher = registerForActivityResult(
        new ActivityResultContracts.TakePicture(),
        success -> {
            if (success) {
                displayImage(capturedImageUri);
            }
        }
    );
}

private Uri createImageFile() {
    try {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "LEAF_" + timeStamp + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageFileName);
        return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imageFile);
    } catch (Exception e) {
        Log.e(TAG, "Error creating image file", e);
        return null;
    }
}

private void openCamera() {
    capturedImageUri = createImageFile();
    if (capturedImageUri != null) {
        takePictureLauncher.launch(capturedImageUri);
    }
}
```

### Step 4: Implement Gallery Picker (45 min)

LeafGuard uses `ActivityResultContracts.GetContent()` launched with `"image/*"` to open the gallery. This is simpler than `PickVisualMedia` and works on all Android versions (API 19+). On Android 13+ (`TIRAMISU`) no permission is required; on older versions you need `READ_EXTERNAL_STORAGE` or `READ_MEDIA_IMAGES`.

**Kotlin (primary) — uses GetContent:**

```kotlin
private lateinit var galleryLauncher: ActivityResultLauncher<String>

private fun setupGalleryLauncher() {
    galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            updateSelectedImage(uri)
        }
    }
}

private fun openGalleryWithPermissionCheck() {
    if (hasPermissions(requiredGalleryPermissions())) {
        galleryLauncher.launch("image/*")
        return
    }
    pendingPermissionAction = ACTION_GALLERY
    permissionLauncher.launch(requiredGalleryPermissions())
}

private fun requiredGalleryPermissions(): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}
```

**Java (secondary) — alternative using PickVisualMedia:**

This implementation supports all Android versions from API 24 (minSdk) through current releases.

- **API 30+ (Android 11+):** Uses the modern `PickVisualMedia` contract.
- **API 24–29 (Android 7–10):** Uses the legacy `ACTION_GET_CONTENT` intent, which is available on all devices in this range.

```java
private ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher;
private ActivityResultLauncher<Intent> legacyPickLauncher;

private void setupGalleryLauncher() {
    // Modern picker — Android 11+ (API 30+)
    pickMediaLauncher = registerForActivityResult(
        new ActivityResultContracts.PickVisualMedia(),
        uri -> {
            if (uri != null) {
                displayImage(uri);
            }
        }
    );

    // Legacy picker — Android 7–10 (API 24–29, i.e. Android 7.0 through 10)
    legacyPickLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                if (uri != null) {
                    displayImage(uri);
                }
            }
        }
    );
}

private void openGallery() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // Android 11+ — photo picker
        pickMediaLauncher.launch(new PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
            .build());
    } else {
        // Android 7–10 (API 24–29) — legacy ACTION_GET_CONTENT
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        legacyPickLauncher.launch(Intent.createChooser(intent, "Select Image"));
    }
}
```

> **Note:** Call `setupGalleryLauncher()` inside `onCreate()` before any button click listener, since `registerForActivityResult` must be called before the activity reaches the STARTED state.

### Step 5: Implement Image Display (45 min)

After capturing or selecting an image, update the preview in `MainActivity`. The simplest approach is `ImageView.setImageURI(uri)`, though for large images you may want to decode a scaled `Bitmap` to save memory.

**Kotlin (primary) — simple approach used in LeafGuard:**

```kotlin
private var selectedImageUri: Uri? = null

private fun updateSelectedImage(imageUri: Uri?) {
    val binding = binding ?: return
    selectedImageUri = imageUri
    binding.buttonDetectDisease.isEnabled = imageUri != null

    if (imageUri == null) {
        binding.imagePlantPreview.setImageResource(android.R.drawable.ic_menu_gallery)
        binding.textImageHint.setText(R.string.image_hint_default)
        return
    }

    binding.imagePlantPreview.setImageURI(imageUri)
    binding.textImageHint.text = getString(R.string.image_selected_message, imageUri.lastPathSegment)
}
```

**Java (secondary) — with scaled bitmap for memory safety:**

```java
private Uri currentImageUri;

private void displayImage(Uri uri) {
    try {
        Bitmap bitmap = loadScaledBitmap(uri, 1024, 1024);
        ImageView ivPreview = findViewById(R.id.ivPreview);
        ivPreview.setImageBitmap(bitmap);
        currentImageUri = uri;
        Log.d(TAG, "Image displayed: " + uri);
    } catch (IOException e) {
        Log.e(TAG, "Failed to load image", e);
        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
    }
}

private Bitmap loadScaledBitmap(Uri uri, int maxWidth, int maxHeight) throws IOException {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    InputStream is = getContentResolver().openInputStream(uri);
    BitmapFactory.decodeStream(is, null, options);
    is.close();

    options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
    options.inJustDecodeBounds = false;

    is = getContentResolver().openInputStream(uri);
    Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
    is.close();
    return bitmap;
}

private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {
        final int halfHeight = height / 2;
        final int halfWidth = width / 2;
        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2;
        }
    }
    return inSampleSize;
}
```

### Step 6: Wire Up Buttons (15 min)

**Kotlin (primary):**

```kotlin
private fun setupButtons() {
    val binding = binding ?: return
    binding.buttonOpenCamera.setOnClickListener { openCameraWithPermissionCheck() }
    binding.buttonOpenGallery.setOnClickListener { openGalleryWithPermissionCheck() }
    binding.buttonDetectDisease.setOnClickListener { detectDisease() }
    // TODO: Add navigation to History, DiseaseLibrary, Settings
}
```

**Java (secondary):**

```java
private void setupButtons() {
    Button btnCamera = findViewById(R.id.btnCamera);
    Button btnGallery = findViewById(R.id.btnGallery);

    btnCamera.setOnClickListener(v -> checkCameraPermission());
    btnGallery.setOnClickListener(v -> openGallery());
}
```

### Step 7: Testing (60 min)

Test scenarios:
1. Camera permission grant → capture → display
2. Camera permission deny → show message
3. Gallery picker → select → display
4. Screen rotation with image loaded
5. Cancel camera/gallery selection

### Validation Checklist

- [ ] Camera button opens camera
- [ ] Image captured and displayed
- [ ] Gallery button opens picker
- [ ] Image selected and displayed
- [ ] Permissions handled correctly
- [ ] No crashes on cancel
- [ ] Works on API 24, 30, 33
- [ ] State preserved on rotation

**Expected Time:** 6-8 hours

**Submit:** Evidence folder with screenshots, video, and code. Commit: "Week 03: Complete camera and gallery implementation"


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
