# Week 03 Solution - Camera, Gallery, Permissions, and Image Preview

This solution provides a full Java implementation for `ScanActivity` using the modern Activity Result APIs.

It includes:
- camera permission flow with rationale and settings redirect
- gallery permission flow for Android 13+ and legacy devices
- `TakePicture` + `FileProvider`
- Photo Picker + legacy `ACTION_PICK`
- memory-safe URI to Bitmap conversion
- preview display
- rotation state preservation
- manifest and `file_paths.xml` updates

---

## 1. Assumptions

This solution assumes the following view IDs already exist in `activity_scan.xml`:
- `imagePreview`
- `textImageStatus`
- `buttonCapture`
- `buttonPick`
- `buttonAnalyze`
- `progressIndicator`
- `topAppBar`

---

## 2. Complete `ScanActivity.java`

```java
package com.leafguard;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.leafguard.databinding.ActivityScanBinding;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ScanActivity extends AppCompatActivity {

    private static final String STATE_CURRENT_IMAGE_URI = "state_current_image_uri";
    private static final String STATE_PENDING_CAMERA_URI = "state_pending_camera_uri";
    private static final int REQUIRED_WIDTH = 1024;
    private static final int REQUIRED_HEIGHT = 1024;

    private ActivityScanBinding binding;
    private Uri currentImageUri;
    private Uri pendingCameraUri;

    private ActivityResultLauncher<String> cameraPermissionLauncher;
    private ActivityResultLauncher<String> galleryPermissionLauncher;
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private ActivityResultLauncher<PickVisualMediaRequest> pickVisualMediaLauncher;
    private ActivityResultLauncher<Intent> legacyGalleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        registerLaunchers();
        setupButtons();

        if (savedInstanceState != null) {
            String savedImage = savedInstanceState.getString(STATE_CURRENT_IMAGE_URI);
            String savedPending = savedInstanceState.getString(STATE_PENDING_CAMERA_URI);
            if (savedImage != null) {
                currentImageUri = Uri.parse(savedImage);
                displayImage(currentImageUri);
            } else {
                showPlaceholder();
            }
            if (savedPending != null) {
                pendingCameraUri = Uri.parse(savedPending);
            }
        } else {
            showPlaceholder();
        }
    }

    private void setupToolbar() {
        binding.topAppBar.setNavigationOnClickListener(view -> finish());
    }

    private void registerLaunchers() {
        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) {
                        launchCamera();
                    } else {
                        handleCameraPermissionDenied();
                    }
                }
        );

        galleryPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) {
                        openLegacyGalleryPicker();
                    } else {
                        handleGalleryPermissionDenied();
                    }
                }
        );

        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && pendingCameraUri != null) {
                        currentImageUri = pendingCameraUri;
                        displayImage(currentImageUri);
                    } else {
                        Toast.makeText(this, R.string.camera_cancelled_message, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        pickVisualMediaLauncher = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        currentImageUri = uri;
                        displayImage(uri);
                    } else {
                        Toast.makeText(this, R.string.gallery_cancelled_message, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        legacyGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedUri = result.getData().getData();
                        if (selectedUri != null) {
                            currentImageUri = selectedUri;
                            displayImage(selectedUri);
                        }
                    } else {
                        Toast.makeText(this, R.string.gallery_cancelled_message, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setupButtons() {
        binding.buttonCapture.setOnClickListener(view -> openCameraWithPermissionCheck());
        binding.buttonPick.setOnClickListener(view -> openGalleryWithVersionCheck());
        binding.buttonAnalyze.setOnClickListener(view -> openResultScreen());
    }

    private void openCameraWithPermissionCheck() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
            return;
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            showCameraRationaleDialog();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openGalleryWithVersionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickVisualMediaLauncher.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            pickVisualMediaLauncher.launch(
                    new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
            );
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            openLegacyGalleryPicker();
            return;
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showGalleryRationaleDialog();
        } else {
            galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void launchCamera() {
        try {
            pendingCameraUri = createImageUri();
            takePictureLauncher.launch(pendingCameraUri);
        } catch (IOException exception) {
            Toast.makeText(this, getString(R.string.camera_prepare_error, exception.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    private Uri createImageUri() throws IOException {
        File imageDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captures");
        if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
            throw new IOException("Could not create image directory");
        }

        File outputFile = new File(imageDirectory, "leaf_" + System.currentTimeMillis() + ".jpg");
        if (!outputFile.exists() && !outputFile.createNewFile()) {
            throw new IOException("Could not create image file");
        }

        return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", outputFile);
    }

    private void openLegacyGalleryPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        legacyGalleryLauncher.launch(intent);
    }

    private void displayImage(@NonNull Uri imageUri) {
        try {
            Bitmap bitmap = loadScaledBitmap(imageUri, REQUIRED_WIDTH, REQUIRED_HEIGHT);
            binding.imagePreview.setImageBitmap(bitmap);
            binding.textImageStatus.setText(getString(R.string.image_selected_message, imageUri.getLastPathSegment()));
            binding.buttonAnalyze.setEnabled(true);
        } catch (IOException exception) {
            showPlaceholder();
            Toast.makeText(this, getString(R.string.image_load_error, exception.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    private void showPlaceholder() {
        binding.imagePreview.setImageResource(android.R.drawable.ic_menu_gallery);
        binding.textImageStatus.setText(R.string.no_image_selected);
        binding.buttonAnalyze.setEnabled(false);
    }

    private Bitmap loadScaledBitmap(@NonNull Uri uri, int reqWidth, int reqHeight) throws IOException {
        ContentResolver resolver = getContentResolver();
        BitmapFactory.Options boundsOptions = new BitmapFactory.Options();
        boundsOptions.inJustDecodeBounds = true;

        try (InputStream boundsStream = resolver.openInputStream(uri)) {
            if (boundsStream == null) {
                throw new IOException("Unable to open image stream.");
            }
            BitmapFactory.decodeStream(boundsStream, null, boundsOptions);
        }

        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inSampleSize = calculateInSampleSize(boundsOptions, reqWidth, reqHeight);
        decodeOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        try (InputStream decodeStream = resolver.openInputStream(uri)) {
            if (decodeStream == null) {
                throw new IOException("Unable to decode image stream.");
            }
            Bitmap bitmap = BitmapFactory.decodeStream(decodeStream, null, decodeOptions);
            if (bitmap == null) {
                throw new IOException("Bitmap decode returned null.");
            }
            return bitmap;
        }
    }

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
        return Math.max(inSampleSize, 1);
    }

    private void openResultScreen() {
        if (currentImageUri == null) {
            Toast.makeText(this, R.string.select_image_first, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_DISEASE_NAME, "Tomato Early Blight");
        intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, 0.89f);
        intent.putExtra(ResultActivity.EXTRA_SYMPTOMS, "Brown concentric rings are visible on older leaves.");
        intent.putExtra(ResultActivity.EXTRA_TREATMENT, "Remove infected leaves and apply a fungicide if needed.");
        intent.putExtra(ResultActivity.EXTRA_PREVENTION, "Avoid overhead watering and keep foliage dry.");
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString());
        startActivity(intent);
    }

    private void showCameraRationaleDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.camera_permission_title)
                .setMessage(R.string.camera_permission_rationale)
                .setPositiveButton(R.string.allow_label, (dialog, which) ->
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA))
                .setNegativeButton(R.string.cancel_label, null)
                .show();
    }

    private void showGalleryRationaleDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.gallery_permission_title)
                .setMessage(R.string.gallery_permission_rationale)
                .setPositiveButton(R.string.allow_label, (dialog, which) ->
                        galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE))
                .setNegativeButton(R.string.cancel_label, null)
                .show();
    }

    private void handleCameraPermissionDenied() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            showSettingsRedirectDialog(getString(R.string.camera_permission_settings_message));
        } else {
            Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleGalleryPermissionDenied() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showSettingsRedirectDialog(getString(R.string.gallery_permission_settings_message));
        } else {
            Toast.makeText(this, R.string.gallery_permission_denied, Toast.LENGTH_SHORT).show();
        }
    }

    private void showSettingsRedirectDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.permission_needed_title)
                .setMessage(message)
                .setPositiveButton(R.string.open_settings, (dialog, which) -> openAppSettings())
                .setNegativeButton(R.string.cancel_label, null)
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentImageUri != null) {
            outState.putString(STATE_CURRENT_IMAGE_URI, currentImageUri.toString());
        }
        if (pendingCameraUri != null) {
            outState.putString(STATE_PENDING_CAMERA_URI, pendingCameraUri.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
```

---

## 3. Required string additions

```xml
<string name="camera_permission_title">Camera permission needed</string>
<string name="camera_permission_rationale">LeafGuard AI needs camera access to capture a plant leaf for disease analysis.</string>
<string name="camera_permission_denied">Camera permission denied.</string>
<string name="camera_permission_settings_message">Camera permission was permanently denied. Open settings and allow it manually.</string>
<string name="gallery_permission_title">Gallery permission needed</string>
<string name="gallery_permission_rationale">LeafGuard AI needs gallery access so you can select a leaf image from storage.</string>
<string name="gallery_permission_denied">Gallery permission denied.</string>
<string name="gallery_permission_settings_message">Gallery permission was permanently denied. Open settings and allow it manually.</string>
<string name="permission_needed_title">Permission required</string>
<string name="allow_label">Allow</string>
<string name="cancel_label">Cancel</string>
<string name="open_settings">Open Settings</string>
<string name="gallery_cancelled_message">No image was selected from gallery.</string>
<string name="image_load_error">Unable to load image: %1$s</string>
<string name="camera_cancelled_message">Camera capture was cancelled.</string>
<string name="camera_prepare_error">Unable to prepare camera file: %1$s</string>
<string name="select_image_first">Select or capture an image before analysis.</string>
```

---

## 4. `res/xml/file_paths.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-files-path
        name="captured_images"
        path="Pictures/" />
    <cache-path
        name="shared_cache"
        path="images/" />
    <files-path
        name="internal_images"
        path="images/" />
</paths>
```

---

## 5. `AndroidManifest.xml` FileProvider configuration

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

---

## 6. Permissions in `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission
    android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />
```

---

## 7. Why this solution is correct

### Camera flow
- checks camera permission
- explains why permission is needed
- launches camera with `TakePicture`
- stores the photo using `FileProvider`

### Gallery flow
- Android 13+: uses modern photo picker
- Android 11-12: uses photo picker branch
- Android 6-10: falls back to `ACTION_PICK` with storage permission

### Memory safety
- decodes only image bounds first
- calculates `inSampleSize`
- decodes the scaled bitmap second
- avoids loading oversized bitmaps directly into memory

### Rotation support
- saves current and pending URIs
- restores preview after configuration change

---

## 8. Multi-API behavior summary

| API level | Gallery path | Permission needed |
|---|---|---|
| 24-29 | `ACTION_PICK` | `READ_EXTERNAL_STORAGE` |
| 30-32 | `PickVisualMedia` | no gallery permission |
| 33+ | `PickVisualMedia` | no gallery permission |

---

## 9. Manual testing checklist

1. Open scan screen.
2. Tap **Capture** and grant camera permission.
3. Take a photo and verify preview appears.
4. Rotate the device and verify preview remains.
5. Tap **Select** and choose an image from gallery.
6. Test cancel behavior for both camera and gallery.
7. Deny permission and verify rationale/settings flows appear.

---

## 10. Final Checklist

- [x] camera permission implemented with Activity Result API
- [x] gallery permission flow included
- [x] `TakePicture` contract used
- [x] modern and legacy gallery picker included
- [x] URI to bitmap conversion is memory-safe
- [x] image preview displayed
- [x] state preserved on rotation
- [x] FileProvider configuration included
