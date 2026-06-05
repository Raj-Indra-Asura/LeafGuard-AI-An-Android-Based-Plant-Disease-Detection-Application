# Week 03 Learning Notes: Camera, Gallery & Image Handling

## Overview

Week 03 transforms LeafGuard from a UI skeleton to a functional image input system. This document covers runtime permissions, camera integration, gallery picking, URI/Bitmap handling, and Android storage concepts in practical detail.

---

## 1. Android Permission System Deep Dive

### Permission Categories

**Normal Permissions (Auto-Granted):**
- Declared in manifest, granted at install time
- Examples: INTERNET, ACCESS_NETWORK_STATE, VIBRATE
- No runtime request needed

**Dangerous Permissions (Runtime Request):**
- Access sensitive user data or device features
- Must request at runtime on Android 6.0+
- Examples: CAMERA, READ_EXTERNAL_STORAGE, READ_MEDIA_IMAGES

**Special Permissions:**
- SYSTEM_ALERT_WINDOW, WRITE_SETTINGS
- User grants via Settings, not dialog

### Runtime Permission Workflow

```
1. Declare in AndroidManifest.xml
2. Check if already granted (checkSelfPermission)
3. If not granted:
   - Check if rationale needed (shouldShowRequestPermissionRationale)
   - Request permission (requestPermissions or Activity Result API)
4. Handle result (onRequestPermissionsResult or callback)
5. Proceed or show error message
```

### LeafGuard Permission Requirements

**CAMERA Permission:**
- Required for: Camera Intent
- When: User taps "Take Photo" button
- Rationale: "LeafGuard needs camera access to capture leaf images for disease detection"

**READ_MEDIA_IMAGES (Android 13+):**
- Required for: Gallery picker
- When: User taps "Choose from Gallery"
- Rationale: "LeafGuard needs storage access to select leaf images"

**READ_EXTERNAL_STORAGE (Android 6-12):**
- Legacy permission for gallery access
- Replaced by READ_MEDIA_IMAGES on Android 13+
- Handle both for backward compatibility

### Implementation Pattern

**Modern Approach (Activity Result API):**

```java
// Register launcher in onCreate or field declaration
private ActivityResultLauncher<String> cameraPermissionLauncher = registerForActivityResult(
    new ActivityResultContracts.RequestPermission(),
    isGranted -> {
        if (isGranted) {
            openCamera();
        } else {
            handlePermissionDenied();
        }
    }
);

// Check and request
private void checkCameraPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
        openCamera();
    } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
        showRationaleDialog();
    } else {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
    }
}
```

**Handling Permanent Denial:**

When user checks "Don't ask again" and denies:
```java
if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
    // Permission permanently denied, direct to settings
    new AlertDialog.Builder(this)
        .setTitle("Permission Required")
        .setMessage("Camera permission is required. Please enable it in Settings.")
        .setPositiveButton("Open Settings", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        })
        .setNegativeButton("Cancel", null)
        .show();
}
```

---

## 2. Camera Integration

### Option 1: Camera Intent (Recommended for LeafGuard)

**Advantages:**
- Simple implementation (few lines of code)
- Uses device's default camera app (familiar UX)
- Handles camera hardware automatically
- Works on all Android versions

**Disadvantages:**
- Limited customization
- No preview control
- Different UI per device

**Implementation:**

```java
private Uri capturedImageUri;

// 1. Register launcher
private ActivityResultLauncher<Uri> takePictureLauncher = registerForActivityResult(
    new ActivityResultContracts.TakePicture(),
    success -> {
        if (success) {
            Log.d(TAG, "Image captured: " + capturedImageUri);
            displayImage(capturedImageUri);
        } else {
            Log.w(TAG, "Image capture cancelled or failed");
            Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_SHORT).show();
        }
    }
);

// 2. Create file to save image
private Uri createImageFile() {
    try {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "LEAF_" + timeStamp + ".jpg";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null && !storageDir.exists()) {
            storageDir.mkdirs();
        }

        File imageFile = new File(storageDir, imageFileName);

        return FileProvider.getUriForFile(this,
            "com.example.leafguard.fileprovider",
            imageFile);
    } catch (Exception e) {
        Log.e(TAG, "createImageFile: Error creating file", e);
        return null;
    }
}

// 3. Launch camera
private void openCamera() {
    capturedImageUri = createImageFile();
    if (capturedImageUri != null) {
        takePictureLauncher.launch(capturedImageUri);
    } else {
        Toast.makeText(this, "Failed to create image file", Toast.LENGTH_SHORT).show();
    }
}
```

### FileProvider Setup

**Why FileProvider?**
- Direct file:// URIs expose app's file system (security risk)
- Android 7.0+ throws FileUriExposedException
- FileProvider wraps file paths as content:// URIs
- Grants temporary read permission to camera app

**AndroidManifest.xml:**
```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

**res/xml/file_paths.xml:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-files-path name="my_images" path="Pictures/" />
    <external-files-path name="my_cache" path="cache/" />
</paths>
```

**Path Elements:**
- `external-files-path` → getExternalFilesDir()
- `files-path` → getFilesDir()
- `cache-path` → getCacheDir()
- `external-path` → Environment.getExternalStorageDirectory()

### Option 2: CameraX Library (Advanced)

**When to Use:**
- Custom camera UI needed
- Real-time preview required
- Image analysis before capture
- Multiple camera support

**Not needed for LeafGuard Week 03, but good to know for future enhancement.**

---

## 3. Gallery Image Picker

### Option 1: Photo Picker (Android 11+)

**Advantages:**
- Modern, consistent UI across devices
- No READ_EXTERNAL_STORAGE permission needed
- Privacy-focused (no full storage access)
- Google-recommended approach

**Implementation:**
```java
private ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher = registerForActivityResult(
    new ActivityResultContracts.PickVisualMedia(),
    uri -> {
        if (uri != null) {
            Log.d(TAG, "Photo picked: " + uri);
            displayImage(uri);
        } else {
            Log.d(TAG, "No photo selected");
        }
    }
);

private void openGallery() {
    pickMediaLauncher.launch(new PickVisualMediaRequest.Builder()
        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
        .build());
}
```

### Option 2: Legacy Gallery Intent (Android 6-10)

**For older devices:**
```java
private ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            Uri uri = result.getData().getData();
            if (uri != null) {
                displayImage(uri);
            }
        }
    }
);

private void openGalleryLegacy() {
    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    pickImageLauncher.launch(intent);
}
```

### Unified Gallery Approach (Recommended)

```java
private void openGallery() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // Android 11+ : Use Photo Picker
        openGalleryModern();
    } else {
        // Android 6-10: Use Intent with permission check
        checkStoragePermission();
    }
}

private void checkStoragePermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Android 13+: READ_MEDIA_IMAGES
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            openGalleryLegacy();
        } else {
            storagePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        }
    } else {
        // Android 6-12: READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGalleryLegacy();
        } else {
            storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
}
```

---

## 4. URI and Bitmap Handling

### Understanding URIs

**File URI (Deprecated):**
```
file:///storage/emulated/0/Pictures/image.jpg
```
- Direct file system path
- Security risk (exposes file structure)
- Throws FileUriExposedException on Android 7+

**Content URI (Modern):**
```
content://com.android.providers.media.documents/document/image:12345
content://com.example.leafguard.fileprovider/my_images/LEAF_20240115_143022.jpg
```
- Secure reference to content
- Requires ContentResolver to access
- Grants temporary permission
- Standard on Android 10+

### Loading Bitmap from URI

**Basic Loading:**
```java
private Bitmap loadBitmapFromUri(Uri uri) throws IOException {
    InputStream inputStream = getContentResolver().openInputStream(uri);
    if (inputStream == null) {
        throw new IOException("Cannot open input stream for URI: " + uri);
    }

    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
    inputStream.close();

    if (bitmap == null) {
        throw new IOException("Failed to decode bitmap from URI");
    }

    return bitmap;
}
```

**Scaled Loading (Prevent OutOfMemoryError):**

Large images (4000x3000) consume ~46 MB RAM. Scaling to 1024x768 reduces to ~3 MB.

```java
private Bitmap loadScaledBitmap(Uri uri, int maxWidth, int maxHeight) throws IOException {
    // Step 1: Get image dimensions without loading full bitmap
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;

    InputStream inputStream = getContentResolver().openInputStream(uri);
    BitmapFactory.decodeStream(inputStream, null, options);
    inputStream.close();

    int imageWidth = options.outWidth;
    int imageHeight = options.outHeight;
    Log.d(TAG, "Original image size: " + imageWidth + "x" + imageHeight);

    // Step 2: Calculate sample size
    options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
    Log.d(TAG, "Sample size: " + options.inSampleSize);

    // Step 3: Decode with sample size
    options.inJustDecodeBounds = false;
    inputStream = getContentResolver().openInputStream(uri);
    Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
    inputStream.close();

    return bitmap;
}

private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {
        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width
        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
```

**inSampleSize Explanation:**
- `inSampleSize = 1` → Full size
- `inSampleSize = 2` → Width/2, Height/2 (1/4 memory)
- `inSampleSize = 4` → Width/4, Height/4 (1/16 memory)
- Must be power of 2 for efficiency

### Displaying in ImageView

```java
private void displayImage(Uri uri) {
    ImageView ivPreview = findViewById(R.id.ivPreview);

    try {
        // Load scaled bitmap
        Bitmap bitmap = loadScaledBitmap(uri, 1024, 1024);
        ivPreview.setImageBitmap(bitmap);

        // Save URI for later use
        currentImageUri = uri;

        // Enable analyze button
        findViewById(R.id.btnAnalyze).setEnabled(true);
    } catch (IOException e) {
        Log.e(TAG, "displayImage: Failed to load image", e);
        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
    }
}
```

---

## 5. Android Storage Locations

### Internal Storage

**Path:** `/data/data/com.example.leafguard/`

**Access:** `getFilesDir()`, `getCacheDir()`

**Characteristics:**
- Always available
- Private to app (other apps cannot access)
- Deleted when app uninstalled
- No permission needed
- Limited space (varies by device)

**Use Cases:**
- Sensitive data (user preferences, auth tokens)
- Database files
- Temporary processing files

### App-Specific External Storage

**Path:** `/storage/emulated/0/Android/data/com.example.leafguard/`

**Access:** `getExternalFilesDir(Environment.DIRECTORY_PICTURES)`

**Characteristics:**
- Private to app (Android 10+)
- Deleted when app uninstalled
- No permission needed (Android 10+)
- More space than internal
- May be unavailable if external storage unmounted

**Use Cases:**
- Captured photos (LeafGuard camera images)
- Downloaded ML models
- Cache files

**LeafGuard Usage:**
```java
File picturesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
// /storage/emulated/0/Android/data/com.example.leafguard/files/Pictures/
```

### Shared External Storage (MediaStore)

**Path:** `/storage/emulated/0/Pictures/`, `/storage/emulated/0/Downloads/`

**Access:** MediaStore API

**Characteristics:**
- Shared across apps
- Persists after app uninstall
- Requires READ_EXTERNAL_STORAGE permission (Android 6-12)
- Scoped storage restrictions (Android 10+)

**Use Cases:**
- User-saved images (share with gallery apps)
- Downloaded files
- Content meant to be kept permanently

### Scoped Storage (Android 10+)

**Key Changes:**
- Apps have unrestricted access to their own directory
- Limited access to shared storage
- Must use MediaStore or SAF for shared files
- WRITE_EXTERNAL_STORAGE permission deprecated

**For LeafGuard:**
- Camera saves to app-specific directory (no permission)
- Gallery picker uses content URIs (no permission on Android 11+)
- No need for legacy storage permissions

---

## 6. Common Issues and Solutions

### Issue 1: FileUriExposedException

**Symptom:** Crash when launching camera on Android 7+

**Cause:** Using file:// URI instead of content:// URI

**Solution:** Implement FileProvider

### Issue 2: OutOfMemoryError

**Symptom:** App crashes when loading large images

**Cause:** Loading full resolution bitmap (4000x3000 = ~46 MB)

**Solution:** Use inSampleSize to scale down

### Issue 3: Gallery Returns Null URI

**Symptom:** uri is null in result callback

**Cause:** User cancelled picker or gallery app closed unexpectedly

**Solution:** Always null-check URI before using

### Issue 4: Permission Request Not Showing

**Symptom:** Permission dialog does not appear

**Cause:** Permission already permanently denied

**Solution:** Check shouldShowRequestPermissionRationale, direct to Settings

### Issue 5: Image Rotation Wrong

**Symptom:** Image displays rotated 90° or upside down

**Cause:** EXIF orientation data not handled

**Solution:** Read EXIF orientation and rotate bitmap accordingly (Week 05 enhancement)

---


---

## 4. Gallery Image Picker (Deep Dive)

The quick gallery examples earlier in this document show the basic idea. This section explains the production-ready version you should understand for CSE 2206: how to branch by Android version, how to safely work with `Uri`, and why the picker result must be copied into a `Bitmap` before you can preview or analyze the image.

### Why LeafGuard Needs Two Gallery Strategies

LeafGuard supports beginner-friendly Android devices from Android 6 through Android 13+. That means one gallery solution is not enough:

- **Android 13+**: Use the **Photo Picker API** through `ActivityResultContracts.PickVisualMedia`
- **Android 6-12**: Use a legacy image-picking `Intent`
- **All versions**: Treat the result as a **`content://` Uri**, not a direct file path

The user journey is:

```text
Tap "Choose from Gallery"
        ↓
Check Android version
        ↓
Launch Photo Picker OR ACTION_PICK intent
        ↓
Receive Uri in callback
        ↓
Open InputStream with ContentResolver
        ↓
Decode Bitmap safely
        ↓
Preview image and prepare it for backend/TFLite use
```

### Photo Picker API (Android 13+)

The Photo Picker is the safest modern choice because the system lets the user choose a photo without granting your app broad storage access.

**Why it is good for LeafGuard:**
- no full gallery permission on Android 13+
- cleaner privacy model for users
- simple callback through Activity Result API
- returns a `Uri` that your app can read directly

### Full Java Example: Photo Picker with `PickVisualMedia`

```java
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageView ivPreview;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<PickVisualMediaRequest> photoPickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.PickVisualMedia(),
                    uri -> {
                        if (uri == null) {
                            Log.d(TAG, "Photo Picker closed without selection");
                            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        handleSelectedImageUri(uri);
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPreview = findViewById(R.id.ivPreview);

        Button btnGallery = findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(v -> openGallery());
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            openPhotoPickerModern();
        } else {
            openLegacyGalleryPicker();
        }
    }

    private void openPhotoPickerModern() {
        PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build();

        photoPickerLauncher.launch(request);
    }

    private void handleSelectedImageUri(Uri uri) {
        selectedImageUri = uri;
        Log.d(TAG, "Selected Uri: " + uri);
        ivPreview.setImageURI(uri);
    }
}
```

### Legacy Intent (`ACTION_PICK`) for Android 6-12

Older Android versions usually rely on a traditional gallery intent. This approach still works, but it is more permission-sensitive on Android 6-12.

**Important:** `ACTION_PICK` returns a `content://` Uri from a media provider. You still should not try to turn it into a raw file path.

### Full Java Example: `ACTION_PICK`

```java
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_STORAGE_PERMISSION = 201;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<Intent> legacyGalleryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() != Activity.RESULT_OK) {
                            Log.d(TAG, "Gallery selection cancelled");
                            return;
                        }

                        Intent data = result.getData();
                        if (data == null || data.getData() == null) {
                            Toast.makeText(this, "No image returned", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        handleSelectedImageUri(data.getData());
                    }
            );

    private void openLegacyGalleryPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION
            );
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        legacyGalleryLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openLegacyGalleryPicker();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleSelectedImageUri(Uri uri) {
        selectedImageUri = uri;
        Log.d(TAG, "Legacy gallery returned: " + uri);
    }
}
```

### Version Branching Logic You Should Actually Use

For this project, keep the branching simple and predictable:

```java
private void openGallery() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        openPhotoPickerModern();
    } else {
        openLegacyGalleryPicker();
    }
}
```

If you want to support the backported system Photo Picker on some Android 11/12 devices later, you can explore `ActivityResultContracts.PickVisualMedia` availability in more detail. For Week 03, the simple branch above is easier for beginners to reason about and test.

### How to Handle the Returned `Uri`

No matter which picker path you use, treat the returned value as a **pointer to image content**, not as the image itself.

**Correct processing order:**
1. Store the `Uri` in a field like `selectedImageUri`
2. Show a preview if needed
3. Open an `InputStream` using `ContentResolver`
4. Decode a `Bitmap`
5. Resize the bitmap for display or ML input
6. Upload the bitmap bytes to backend or pass the resized bitmap to TFLite

```java
private void handleSelectedImageUri(@NonNull Uri uri) {
    selectedImageUri = uri;

    try {
        Bitmap previewBitmap = loadScaledBitmap(uri, 1024, 1024);
        ivPreview.setImageBitmap(previewBitmap);
        findViewById(R.id.btnAnalyze).setEnabled(true);
    } catch (IOException e) {
        Log.e(TAG, "Failed to load selected image", e);
        Toast.makeText(this, "Could not read selected image", Toast.LENGTH_SHORT).show();
    }
}
```

### Scoped Storage Implications (Android 10+)

From Android 10 onward, apps are not supposed to freely browse shared storage by raw file path. This is called **scoped storage**.

For LeafGuard, that means:
- gallery apps return **safe `content://` references**
- your app reads image bytes through `ContentResolver`
- you should avoid code that tries to convert the Uri into `/sdcard/.../image.jpg`
- app-specific camera photos can still be written inside your own app directories

### `ContentResolver.openInputStream()`

This is the standard way to read bytes behind a `content://` Uri.

```java
private InputStream openImageInputStream(Uri uri) throws IOException {
    InputStream inputStream = getContentResolver().openInputStream(uri);
    if (inputStream == null) {
        throw new IOException("ContentResolver returned null InputStream for: " + uri);
    }
    return inputStream;
}
```

### `content://` vs `file://` URIs

| Type | Example | Meaning | Can you pass it between apps? | Recommended today? |
|------|---------|---------|-------------------------------|--------------------|
| `content://` | `content://media/external/images/media/25` | A secure content reference handled by a provider | Yes, with granted access | **Yes** |
| `file://` | `file:///storage/emulated/0/Pictures/leaf.jpg` | A direct file path | Unsafe on Android 7+ | **No** |

**Remember:**
- `content://` is the modern Android way
- `file://` causes `FileUriExposedException` when shared carelessly
- `FileProvider` converts app files into safe `content://` URIs

---

## 5. URI and Bitmap Handling (Complete Guide)

This is the most important technical bridge between Android UI and machine learning. The picker gives you a `Uri`, but the model needs **pixel data** arranged in a specific width, height, and color format.

### What Is a `Uri`?

A `Uri` is a standardized identifier used by Android to point to content.

Common schemes you may see:

- **`content://`** → content from gallery, MediaStore, or FileProvider
- **`file://`** → direct path to a file on disk (legacy and discouraged)
- **`asset://`** → custom style sometimes used in libraries to refer to packaged assets

Examples:

```text
content://media/external/images/media/103
file:///storage/emulated/0/Pictures/leaf.jpg
asset://model/class_labels.txt
```

In beginner Android projects, `content://` is by far the most important one.

### Why a Raw `Uri` Cannot Be Used Directly in an ML Model

A TFLite model does not understand Android `Uri` objects. It expects numeric pixel values such as:

- width = 224
- height = 224
- 3 channels (RGB)
- normalized float values, or bytes, depending on model design

So the conversion path is:

```text
Uri
  ↓
ContentResolver.openInputStream(uri)
  ↓
BitmapFactory.decodeStream(...)
  ↓
Bitmap
  ↓
Resize / normalize / convert to ByteBuffer
  ↓
TFLite Interpreter input tensor
```

### Converting `Uri -> InputStream -> Bitmap`

```java
private Bitmap uriToBitmap(@NonNull Context context, @NonNull Uri uri) throws IOException {
    ContentResolver resolver = context.getContentResolver();

    try (InputStream inputStream = resolver.openInputStream(uri)) {
        if (inputStream == null) {
            throw new IOException("Cannot open InputStream for Uri: " + uri);
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        if (bitmap == null) {
            throw new IOException("BitmapFactory could not decode image bytes");
        }

        return bitmap;
    }
}
```

### `BitmapFactory.Options` and `inSampleSize`

`BitmapFactory.Options` lets you control how a bitmap is decoded.

Most important options for Week 03:

- `inJustDecodeBounds = true` → read width and height only, do not allocate full bitmap memory
- `inSampleSize` → shrink image during decode to reduce RAM usage
- `inPreferredConfig = Bitmap.Config.ARGB_8888` → keep full color quality for model preprocessing

### Why `inSampleSize` Matters

A 4000 x 3000 photo contains 12 million pixels.
If you decode it at ARGB_8888:

```text
12,000,000 pixels × 4 bytes ≈ 48 MB
```

That is far too large to decode casually in a beginner app. Use `inSampleSize` to reduce memory before the image ever reaches your `ImageView` or ML pipeline.

### `calculateInSampleSize()` Algorithm

```java
private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    int height = options.outHeight;
    int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {
        int halfHeight = height / 2;
        int halfWidth = width / 2;

        while ((halfHeight / inSampleSize) >= reqHeight
                && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
```

### How the Algorithm Works

Suppose your image is `4000 x 3000` and your target preview is `1000 x 1000`.

- start with `inSampleSize = 1`
- test whether half size still exceeds requested size
- if yes, double the sample size
- keep doubling while both dimensions stay larger than target

Typical result:

- `1` = full image
- `2` = quarter of memory
- `4` = one-sixteenth of memory
- `8` = one-sixty-fourth of memory

This power-of-two pattern is efficient and widely used in Android image loading.

### Full `loadScaledBitmap()` Implementation

```java
private Bitmap loadScaledBitmap(@NonNull Uri uri, int reqWidth, int reqHeight) throws IOException {
    ContentResolver resolver = getContentResolver();

    BitmapFactory.Options boundsOptions = new BitmapFactory.Options();
    boundsOptions.inJustDecodeBounds = true;

    try (InputStream boundsStream = resolver.openInputStream(uri)) {
        if (boundsStream == null) {
            throw new IOException("Unable to open bounds stream for: " + uri);
        }
        BitmapFactory.decodeStream(boundsStream, null, boundsOptions);
    }

    BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
    decodeOptions.inSampleSize = calculateInSampleSize(boundsOptions, reqWidth, reqHeight);
    decodeOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
    decodeOptions.inJustDecodeBounds = false;

    Bitmap decodedBitmap;
    try (InputStream decodeStream = resolver.openInputStream(uri)) {
        if (decodeStream == null) {
            throw new IOException("Unable to open decode stream for: " + uri);
        }
        decodedBitmap = BitmapFactory.decodeStream(decodeStream, null, decodeOptions);
    }

    if (decodedBitmap == null) {
        throw new IOException("Bitmap decode returned null");
    }

    return Bitmap.createScaledBitmap(decodedBitmap, reqWidth, reqHeight, true);
}
```

### Why LeafGuard Often Scales to `224 x 224`

Many beginner-friendly plant disease models use `224 x 224 x 3` input because:

- it matches common transfer-learning backbones like MobileNet
- it is small enough for mobile inference
- it reduces memory and CPU cost
- it is the size many example datasets use during training

**Important:** you do **not** choose 224 because Android prefers it. You choose it because the **model was trained for that input shape**. If your `model-notes.md` later says `299 x 299` or `256 x 256`, you must obey the model instead.

### Helper for Model Input Bitmap

```java
private Bitmap loadBitmapForModel(@NonNull Uri uri) throws IOException {
    final int MODEL_WIDTH = 224;
    final int MODEL_HEIGHT = 224;
    return loadScaledBitmap(uri, MODEL_WIDTH, MODEL_HEIGHT);
}
```

### Converting Bitmap to Byte Array for Retrofit Multipart Upload

When sending an image to the FastAPI backend, Retrofit usually needs a `RequestBody` or `MultipartBody.Part`. A `Bitmap` must first be compressed into bytes.

```java
private byte[] bitmapToJpegBytes(@NonNull Bitmap bitmap) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    boolean compressed = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
    if (!compressed) {
        throw new IOException("Bitmap compression failed");
    }

    return outputStream.toByteArray();
}
```

```java
private MultipartBody.Part createImagePart(@NonNull Bitmap bitmap) throws IOException {
    byte[] imageBytes = bitmapToJpegBytes(bitmap);
    RequestBody requestBody = RequestBody.create(
            imageBytes,
            MediaType.parse("image/jpeg")
    );

    return MultipartBody.Part.createFormData("file", "leaf_upload.jpg", requestBody);
}
```

### Memory Management: When Should You Call `bitmap.recycle()`?

`bitmap.recycle()` manually frees the pixel memory of a bitmap. You **should not** call it blindly.

**Safe situations:**
- you created a temporary bitmap only for preprocessing
- you are sure it is no longer displayed in any `ImageView`
- you already created a second resized bitmap and no longer need the first one

**Unsafe situations:**
- the bitmap is still assigned to an `ImageView`
- another method still needs it
- you are not sure whether the UI thread is still using it

### Safe Cleanup Example

```java
private void processImageForUpload(@NonNull Uri uri) {
    Bitmap originalBitmap = null;
    Bitmap modelBitmap = null;

    try {
        originalBitmap = uriToBitmap(this, uri);
        modelBitmap = Bitmap.createScaledBitmap(originalBitmap, 224, 224, true);

        byte[] bytes = bitmapToJpegBytes(modelBitmap);
        Log.d(TAG, "Prepared upload bytes: " + bytes.length);
    } catch (IOException e) {
        Log.e(TAG, "Image processing failed", e);
    } finally {
        if (originalBitmap != null && !originalBitmap.isRecycled()) {
            originalBitmap.recycle();
        }
        if (modelBitmap != null && !modelBitmap.isRecycled()) {
            modelBitmap.recycle();
        }
    }
}
```

**Rule for beginners:** recycle only temporary processing bitmaps. If a bitmap is attached to preview UI, let Android manage it until you replace or clear it.

---

## 6. Android Storage Complete Guide

Storage confuses many Android students because there are several different directories with similar names. For LeafGuard, you only need to master a few safe choices.

### Internal Storage vs External Storage vs App-Specific Storage

| Storage Type | Example Access Method | Who can access it? | Deleted on uninstall? | Good for LeafGuard? |
|--------------|-----------------------|--------------------|-----------------------|---------------------|
| Internal storage | `getFilesDir()` / `getCacheDir()` | Only your app | Yes | Yes |
| App-specific external storage | `getExternalFilesDir()` | Your app (and system tools) | Yes | Yes |
| Shared external storage | `MediaStore`, public Pictures/Downloads | User and other apps | No | Read carefully |

### 1. Internal Storage

Use internal storage when data is private and should stay completely inside the app sandbox.

```java
File filesDir = getFilesDir();
File cacheDir = getCacheDir();
```

**Best uses in LeafGuard:**
- Room database files
- temporary resized bitmap cache
- debug logs or JSON responses that should stay private

### 2. App-Specific External Storage

This is often the best place for camera photos captured by your own app.

```java
File picturesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
File downloadsDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
```

**Why it is a strong choice:**
- more space than internal storage on many devices
- no broad storage permission needed
- files are removed automatically when app is uninstalled
- easy to share safely using `FileProvider`

### 3. App-Specific Cache

Use cache for files you can recreate later.

```java
File cacheDir = getCacheDir();
File externalCacheDir = getExternalCacheDir();
```

**Good LeafGuard cache examples:**
- temporary copy of gallery photo
- compressed upload file
- intermediate 224 x 224 bitmap snapshot

### `getExternalFilesDir()` vs `getCacheDir()`

| Method | Best for | Survives app restarts? | System may delete automatically? |
|--------|----------|------------------------|----------------------------------|
| `getExternalFilesDir()` | captured photos you may need again | Yes | Usually no |
| `getCacheDir()` | temporary files safe to recreate | Maybe | Yes |

### Why LeafGuard Uses App-Specific Storage for Captured Photos

Camera capture in LeafGuard needs a file destination **before** the camera app opens. App-specific storage is the safest answer because:

- no shared-storage permission is required
- the file belongs to LeafGuard only
- `FileProvider` can expose it temporarily to the camera app
- cleanup is easy because you know exactly where your files are stored

### MediaStore API (Read-Only for Our Week 03 Use)

`MediaStore` is Android's organized view of shared media such as photos and videos.

For Week 03, you mostly **read** from shared media through the gallery picker. You do not need to manually scan the whole gallery.

```java
Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
intent.setType("image/*");
legacyGalleryLauncher.launch(intent);
```

That code uses MediaStore indirectly because the gallery returns a `content://` Uri backed by a media provider.

### Why `FileProvider` Is Necessary

The camera app is a different app process. If you want it to write into a file owned by LeafGuard, Android needs a secure share mechanism.

Without `FileProvider`:
- you would try to share a raw `file://` Uri
- Android 7+ would throw `FileUriExposedException`
- the camera app may not have permission to write to your file

With `FileProvider`:
- your private file becomes a temporary `content://` Uri
- the camera app gets controlled access only for that operation
- your app remains compatible with modern Android security rules

### Complete `FileProvider` Manifest Configuration

```xml
<application
    ... >

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

### Complete `file_paths.xml` Example

Create: `app/src/main/res/xml/file_paths.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-files-path
        name="captured_images"
        path="Pictures/" />

    <external-files-path
        name="export_downloads"
        path="Download/" />

    <cache-path
        name="temp_cache"
        path="." />

    <external-cache-path
        name="external_temp_cache"
        path="." />
</paths>
```

### Creating a Temporary Photo File Safely

```java
private File currentPhotoFile;

private Uri createPhotoUri() throws IOException {
    File picturesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    if (picturesDir == null) {
        throw new IOException("External pictures directory is unavailable");
    }

    if (!picturesDir.exists() && !picturesDir.mkdirs()) {
        throw new IOException("Failed to create pictures directory");
    }

    String fileName = "leaf_" + System.currentTimeMillis() + ".jpg";
    currentPhotoFile = new File(picturesDir, fileName);

    return FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".fileprovider",
            currentPhotoFile
    );
}
```

### Cleaning Up Temporary Files After Use

Temporary camera files and exported debug images should not live forever.

**Delete when:**
- user cancels capture
- upload fails and the file is only a temporary copy
- you created a cache file only for resizing or multipart upload

```java
private void deleteCurrentPhotoIfTemporary() {
    if (currentPhotoFile != null && currentPhotoFile.exists()) {
        boolean deleted = currentPhotoFile.delete();
        Log.d(TAG, "Temporary file deleted: " + deleted);
    }
}
```

### Example Cleanup Policy for LeafGuard

A practical beginner workflow is:

1. **Camera capture** → store in app-specific `Pictures/`
2. **Preview + analyze** → keep while result is on screen
3. **If user saves history only** → store metadata in Room, not the whole huge image
4. **If temporary upload copy exists** → delete it after response arrives
5. **If user cancels before analysis** → delete unused temp photo immediately

### Storage Decision Summary

Use this quick decision guide:

- **Need a safe camera output file?** → `getExternalFilesDir(Environment.DIRECTORY_PICTURES)`
- **Need a temporary resized bitmap file?** → `getCacheDir()` or `getExternalCacheDir()`
- **Need to read a gallery image?** → use picker + `ContentResolver.openInputStream()`
- **Need private persistent app data?** → internal storage or Room database

---

## Key Takeaways

1. **Modern Permission Handling:** Use Activity Result API, not deprecated onRequestPermissionsResult
2. **FileProvider is Mandatory:** For camera intents on Android 7+
3. **Always Scale Bitmaps:** Prevent OutOfMemoryError with inSampleSize
4. **Use App-Specific Storage:** No permission needed, automatically cleaned up
5. **Handle Null Cases:** User can cancel camera/gallery, always check for null
6. **Test on Multiple API Levels:** Permission behavior differs across Android versions

---

## Next Steps

Week 04 will send captured images to FastAPI backend for disease detection. The URI handling and bitmap conversion learned this week is crucial for creating multipart request bodies.


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
