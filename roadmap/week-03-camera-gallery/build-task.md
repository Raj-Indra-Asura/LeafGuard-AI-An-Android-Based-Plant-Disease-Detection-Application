# Week 03 Build Task: Implement Camera & Gallery Image Input

## Objective

Transform LeafGuard's ScanActivity from a skeleton to a fully functional image input system. By completion, users can capture photos with camera or select images from gallery, with proper permission handling and image display.

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

Add to ScanActivity.java:

```java
private ActivityResultLauncher<String> cameraPermissionLauncher;
private ActivityResultLauncher<String> storagePermissionLauncher;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan);

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

```java
private ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher;

private void setupGalleryLauncher() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        pickMediaLauncher = registerForActivityResult(
            new ActivityResultContracts.PickVisualMedia(),
            uri -> {
                if (uri != null) {
                    displayImage(uri);
                }
            }
        );
    }
}

private void openGallery() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        pickMediaLauncher.launch(new PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
            .build());
    } else {
        // Legacy implementation for older devices
        Toast.makeText(this, "Gallery picker not available on this Android version", Toast.LENGTH_SHORT).show();
    }
}
```

### Step 5: Implement Image Display (45 min)

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
