# Week 03: Camera Intent, Gallery Picker & Image Handling

## Weekly Objective

By the end of Week 03, you will:

1. **Implement camera capture** using Android Camera Intent or CameraX library
2. **Implement gallery image picker** using MediaStore or Activity Result API
3. **Master runtime permissions** for camera and storage access on Android 6.0+
4. **Handle URI and Bitmap conversion** for displaying and processing images
5. **Display image preview** in ImageView before analysis
6. **Understand Android storage** including internal, external, and scoped storage
7. **Save captured images** to app-specific directory for persistence

**Measurable Outcomes:**
- Camera button launches camera, captures image, returns URI
- Gallery button opens gallery, user selects image, returns URI
- Permission dialogs shown and handled correctly
- Captured/selected image displayed in ImageView
- Image persisted in app storage
- ScanActivity fully functional with real image input
- No crashes on permission denial or image selection cancellation

---

## Why This Week Matters

### Connection to CSE 2206 Mobile Application Development

Week 03 is where LeafGuard becomes a real image processing app. Without camera and gallery integration, your disease detection feature has no input. This week covers:

**Critical CSE 2206 Topics:**
- **Camera Integration:** Using device hardware via intents
- **Storage Access:** Reading/writing files, understanding Android storage
- **Runtime Permissions:** Marshmallow+ permission model (CRITICAL for modern Android)
- **URI Handling:** ContentResolver, MediaStore, file URIs
- **Bitmap Processing:** Loading, scaling, displaying images
- **Activity Result API:** Modern replacement for onActivityResult

**Real-World Relevance:**
Almost every Android app with user content requires camera or gallery access:
- Social media apps (Instagram, WhatsApp, Snapchat)
- E-commerce apps (product photos)
- Document scanners
- Profile picture uploaders
- AR/ML applications

**Viva Defense Points:**
- Demonstrate understanding of Android permission system
- Explain scoped storage changes (Android 10+)
- Show error handling for permission denial
- Prove image handling works across Android versions 7-14

---

## Syllabus Topics Covered This Week

### Direct Coverage

1. **Camera Integration**
   - Using Intent to launch camera app
   - Capturing and retrieving image
   - Alternative: CameraX library for advanced control

2. **MediaStore and Gallery Access**
   - Accessing user's image library
   - Querying ContentProvider
   - Picking images with ACTION_PICK or ACTION_GET_CONTENT

3. **Runtime Permissions**
   - Requesting CAMERA permission
   - Requesting READ_EXTERNAL_STORAGE permission (Android <13)
   - Requesting READ_MEDIA_IMAGES permission (Android 13+)
   - Handling permission granted/denied states
   - Showing permission rationale to user

4. **File and Storage APIs**
   - Internal vs external storage
   - App-specific directory (no permission needed)
   - MediaStore.Images collection
   - ContentResolver for querying media
   - File URIs vs Content URIs

5. **Bitmap Handling**
   - Loading Bitmap from URI using ContentResolver
   - Scaling Bitmaps to prevent OutOfMemoryError
   - Displaying Bitmap in ImageView
   - Understanding BitmapFactory.Options

6. **Activity Result API**
   - Registering activity result launchers
   - ActivityResultContracts.TakePicture
   - ActivityResultContracts.PickVisualMedia
   - ActivityResultContracts.RequestPermission

### Indirect Preparation

- Image preprocessing for ML model (Week 06, 09)
- Asynchronous operations for image loading (Week 05)
- Caching strategy for images (Week 07)
- File upload to backend via Retrofit (Week 05)

---

## Prerequisites

### Required Knowledge

1. **Week 02 Completion:**
   - Activities and navigation working
   - ScanActivity exists with camera/gallery buttons
   - Understanding of Intent extras
   - Ability to update UI from data

2. **Android Basics:**
   - Understanding of callbacks (onClick, onActivityResult)
   - Basic error handling (try-catch, null checks)
   - Understanding of AndroidManifest.xml

3. **Java/Kotlin Skills:**
   - Lambda expressions (for Activity Result API)
   - Anonymous inner classes (alternative syntax)
   - File I/O basics

### Required Tools

1. **Android Device Recommended:**
   - Physical device with camera
   - USB debugging enabled
   - Emulators have virtual camera but it's limited

2. **Updated Dependencies:**
   - CameraX library (optional, for advanced features)
   - Activity Result API (androidx.activity:activity:1.7.0+)

3. **Test Images:**
   - Sample plant leaf images for gallery testing
   - Downloaded to device/emulator storage

---

## Concepts to Learn

### 1. Android Permission System

**What It Is:** Security model requiring user consent for sensitive operations.

**Permission Types:**

**Normal Permissions (Auto-Granted):**
- INTERNET
- ACCESS_NETWORK_STATE
- No user dialog, just declare in manifest

**Dangerous Permissions (Runtime Request Required):**
- CAMERA
- READ_EXTERNAL_STORAGE (Android 6-12)
- READ_MEDIA_IMAGES (Android 13+)
- User must grant through dialog

**Permission Workflow:**

```
App needs camera
    ↓
Check if permission granted
    ↓
If granted → Use camera
If not granted → Request permission
    ↓
User sees dialog → Allow / Deny
    ↓
If Allow → onPermissionGranted → Use camera
If Deny → onPermissionDenied → Show rationale or disable feature
```

**LeafGuard Implementation:**

```java
// Check permission
if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
    openCamera();
} else {
    requestCameraPermission();
}

// Request permission
ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
    new ActivityResultContracts.RequestPermission(),
    isGranted -> {
        if (isGranted) {
            openCamera();
        } else {
            showPermissionDeniedMessage();
        }
    }
);

requestPermissionLauncher.launch(Manifest.permission.CAMERA);
```

**Permission States:**

1. **Never Asked:** User has never seen permission dialog
2. **Granted:** User allowed permission
3. **Denied Once:** User denied, can ask again
4. **Denied Permanently:** User denied and checked "Don't ask again"

**Handling Permanent Denial:**

```java
if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
    // Show explanation why permission needed
    showRationaleDialog();
} else {
    // User checked "Don't ask again", direct to settings
    openAppSettings();
}
```

### 2. Camera Integration

**Option 1: Camera Intent (Simple, Recommended for LeafGuard)**

```java
// 1. Register launcher
ActivityResultLauncher<Uri> takePictureLauncher = registerForActivityResult(
    new ActivityResultContracts.TakePicture(),
    success -> {
        if (success) {
            // Image saved to imageUri
            displayImage(imageUri);
        }
    }
);

// 2. Create file to save image
Uri imageUri = createImageFile();

// 3. Launch camera
takePictureLauncher.launch(imageUri);
```

**Option 2: CameraX Library (Advanced Control)**

```java
// Provides preview, image capture, image analysis
// More code but better UX
// Use for custom camera UI
```

**Creating Image File:**

```java
private Uri createImageFile() throws IOException {
    // Generate unique filename
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
    String imageFileName = "LEAF_" + timeStamp + ".jpg";

    // Save to app-specific directory (no permission needed)
    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    File imageFile = new File(storageDir, imageFileName);

    // Return URI
    return FileProvider.getUriForFile(this, "com.example.leafguard.fileprovider", imageFile);
}
```

**FileProvider Setup (Required for Camera Intent):**

**AndroidManifest.xml:**
```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="com.example.leafguard.fileprovider"
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
<paths>
    <external-files-path name="my_images" path="Pictures/" />
</paths>
```

### 3. Gallery Image Picker

**Modern Approach: Photo Picker (Android 13+, Backported)**

```java
// Register launcher
ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher = registerForActivityResult(
    new ActivityResultContracts.PickVisualMedia(),
    uri -> {
        if (uri != null) {
            displayImage(uri);
        }
    }
);

// Launch photo picker
pickMediaLauncher.launch(new PickVisualMediaRequest.Builder()
    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
    .build());
```

**Legacy Approach: Intent with ACTION_PICK**

```java
// Register launcher
ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Uri uri = result.getData().getData();
            displayImage(uri);
        }
    }
);

// Launch gallery
Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
pickImageLauncher.launch(intent);
```

**LeafGuard Strategy:**
- Use Photo Picker if available (Android 11+)
- Fall back to ACTION_PICK for older devices

### 4. URI and Bitmap Handling

**Understanding URIs:**

**File URI:** `file:///storage/emulated/0/Pictures/image.jpg`
- Direct file path
- Deprecated for security (exposes file system)
- Replaced by FileProvider

**Content URI:** `content://com.android.providers.media.documents/document/image:12345`
- Secure reference to content
- Requires ContentResolver to access
- Standard on Android 10+

**Loading Bitmap from URI:**

```java
private Bitmap loadBitmapFromUri(Uri uri) throws IOException {
    // Open input stream
    InputStream inputStream = getContentResolver().openInputStream(uri);

    // Decode to Bitmap
    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

    inputStream.close();
    return bitmap;
}
```

**Scaling Bitmap (Prevent OutOfMemoryError):**

```java
private Bitmap loadScaledBitmap(Uri uri, int requiredWidth, int requiredHeight) throws IOException {
    // First decode with inJustDecodeBounds=true to get dimensions
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;

    InputStream inputStream = getContentResolver().openInputStream(uri);
    BitmapFactory.decodeStream(inputStream, null, options);
    inputStream.close();

    // Calculate sample size
    options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);

    // Decode actual bitmap
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

        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2;
        }
    }
    return inSampleSize;
}
```

**Displaying in ImageView:**

```java
ImageView ivPreview = findViewById(R.id.ivPreview);

// Option 1: Direct URI (ImageView loads automatically)
ivPreview.setImageURI(uri);

// Option 2: Load Bitmap first (for processing)
Bitmap bitmap = loadScaledBitmap(uri, 1024, 1024);
ivPreview.setImageBitmap(bitmap);

// Option 3: Using Glide library (Week 05+)
Glide.with(this).load(uri).into(ivPreview);
```

### 5. Android Storage Locations

**Internal Storage:**
- `/data/data/com.example.leafguard/`
- Always available
- Private to app
- Deleted when app uninstalled
- No permission needed

**App-Specific External Storage:**
- `/storage/emulated/0/Android/data/com.example.leafguard/`
- Available on most devices
- Private to app (Android 10+)
- Deleted when app uninstalled
- No permission needed (Android 10+)

**Shared External Storage:**
- `/storage/emulated/0/Pictures/`, `/storage/emulated/0/Downloads/`
- Shared across apps
- Persists after uninstall
- Requires permission (READ_EXTERNAL_STORAGE)
- Scoped storage on Android 10+ (limited access)

**LeafGuard Storage Strategy:**
```
Captured images → App-specific external (getExternalFilesDir(PICTURES))
Uploaded images → Internal cache (getCacheDir()) - temporary
User-saved results → Shared Pictures (MediaStore) - optional feature
```

### 6. Scoped Storage (Android 10+)

**What Changed:**
- Apps cannot directly access shared storage files by path
- Must use MediaStore or Storage Access Framework
- READ_EXTERNAL_STORAGE permission limited to media files
- Better privacy and security

**For LeafGuard:**
- Camera captures → App-specific directory (no change needed)
- Gallery picks → ContentResolver (use URIs, not paths)
- No need for WRITE_EXTERNAL_STORAGE if using app-specific directory

---

## Reading Plan

### Day 1: Permissions Deep Dive

**Reading:**
- Request App Permissions: https://developer.android.com/training/permissions/requesting
- Permissions Best Practices: https://developer.android.com/training/permissions/usage-notes

**Tasks:**
1. Understand permission types (normal vs dangerous)
2. Study permission workflow diagram
3. Learn about shouldShowRequestPermissionRationale
4. Understand permission denial scenarios
5. Add CAMERA permission to manifest

### Day 2: Camera Integration

**Reading:**
- Take Photos: https://developer.android.com/training/camera/photobasics
- FileProvider: https://developer.android.com/reference/androidx/core/content/FileProvider

**Tasks:**
1. Learn TakePicture contract
2. Understand FileProvider setup
3. Create file_paths.xml
4. Implement createImageFile()
5. Test camera on device

### Day 3: Gallery Picker

**Reading:**
- Photo Picker: https://developer.android.com/training/data-storage/shared/photopicker
- Activity Result API: https://developer.android.com/training/basics/intents/result

**Tasks:**
1. Learn PickVisualMedia contract
2. Implement photo picker with fallback
3. Handle URI from gallery
4. Test with multiple image sources

### Day 4: Bitmap and URI Handling

**Reading:**
- Bitmaps: https://developer.android.com/topic/performance/graphics/load-bitmap
- ContentResolver: https://developer.android.com/reference/android/content/ContentResolver

**Tasks:**
1. Implement URI to Bitmap conversion
2. Implement bitmap scaling to prevent OOM
3. Display image in ImageView
4. Handle rotation/EXIF data

### Day 5: Integration and Testing

**Tasks:**
1. Integrate camera + gallery into ScanActivity
2. Test permission flow (grant, deny, permanent deny)
3. Test image display and persistence
4. Test on multiple Android versions (API 24, 30, 33)
5. Handle edge cases (cancel, rotation, app killed)

### Days 6-7: Exercises, Evidence, Documentation

**Tasks:**
1. Complete all exercises
2. Build task implementation
3. Collect evidence (screenshots, videos)
4. Reflection and quiz
5. Validate completion

---

## Week Completion Criteria

You may proceed to Week 04 only when:

**Technical Completion:**
- [ ] Camera button opens camera, captures image
- [ ] Gallery button opens gallery picker, selects image
- [ ] Permissions requested and handled correctly
- [ ] Selected/captured image displayed in ImageView
- [ ] Image persisted and accessible
- [ ] App works on API 24, 30, 33 (test on emulators)

**Understanding:**
- [ ] Can explain Android permission model
- [ ] Can explain difference between file URI and content URI
- [ ] Can implement camera integration without tutorial
- [ ] Can handle permission denial gracefully
- [ ] Can scale Bitmap to prevent OutOfMemoryError

**Documentation:**
- [ ] Learning notes completed
- [ ] Exercises attempted (minimum 6)
- [ ] Build task completed
- [ ] Validation checklist checked
- [ ] Reflection submitted
- [ ] Quiz passed (8/10 minimum)

**Evidence:**
- [ ] Screenshots of permission dialogs
- [ ] Screenshots of camera capture
- [ ] Screenshots of gallery selection
- [ ] Video of complete camera-to-preview flow
- [ ] Git commits show progressive work

---

**Week 03 makes LeafGuard functional for image input. Master camera and gallery, and you're halfway to a complete disease detection app!**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 03 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| **1** | **README.md** ← *You are here* | **Week Overview & Objectives** |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

*(Start of week)* &nbsp;&nbsp;|&nbsp;&nbsp; **Week Overview & Objectives** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Theory & Learning Notes →](learning-notes.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 02: Android Basics & UI](../week-02-android-basics-ui/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 04: FastAPI Backend ➡](../week-04-fastapi-backend/README.md) |

---
