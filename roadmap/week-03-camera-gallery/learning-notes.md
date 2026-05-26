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
