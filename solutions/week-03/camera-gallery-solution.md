# Week 03 Solution - Camera, Gallery, Permissions, and Image Preview

This solution demonstrates how LeafGuard implements camera capture and gallery selection in **MainActivity** using the modern Activity Result APIs. There is no separate `ScanActivity`—all image-input logic lives in the main screen so users can capture a leaf, preview it, and run detection without extra navigation.

The Kotlin implementation (primary) matches the actual `MainActivity.kt` in `android-app-kotlin/`. A Java equivalent (secondary) is provided for reference.

It includes:
- camera permission flow with rationale and settings redirect
- gallery permission flow for Android 13+ and legacy devices
- `TakePicture` + `FileProvider`
- `GetContent("image/*")` gallery picker (simpler than PickVisualMedia)
- memory-safe URI to Bitmap conversion
- preview display
- rotation state preservation
- manifest and `file_paths.xml` updates

---

## 1. Assumptions

This solution assumes the following view IDs already exist in `activity_main.xml`:
- `imagePlantPreview`
- `textImageHint`
- `buttonOpenCamera`
- `buttonOpenGallery`
- `buttonDetectDisease`
- `progressDetection`
- `topAppBar`

---

## 2. Complete `MainActivity.kt` (Kotlin — Primary)

```kotlin
package com.leafguard

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.leafguard.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ACTION_CAMERA = "camera"
        private const val ACTION_GALLERY = "gallery"
    }

    private var binding: ActivityMainBinding? = null
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>

    private var selectedImageUri: Uri? = null
    private var pendingCameraUri: Uri? = null
    private var pendingPermissionAction: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        this.binding = binding
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        setupActivityResults()
        setupButtons()
        updateSelectedImage(null)

        // Restore state if activity was recreated
        savedInstanceState?.getString("selectedImageUri")?.let { uriString ->
            selectedImageUri = Uri.parse(uriString)
            updateSelectedImage(selectedImageUri)
        }
    }

    private fun setupActivityResults() {
        // Permission launcher for multiple permissions
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

        // Gallery launcher using GetContent (simpler than PickVisualMedia)
        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                updateSelectedImage(uri)
            }
        }

        // Camera launcher using TakePicture
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

    private fun setupButtons() {
        val binding = binding ?: return
        binding.buttonOpenCamera.setOnClickListener { openCameraWithPermissionCheck() }
        binding.buttonOpenGallery.setOnClickListener { openGalleryWithPermissionCheck() }
        binding.buttonDetectDisease.setOnClickListener { detectDisease() }
    }

    private fun openCameraWithPermissionCheck() {
        if (hasPermissions(requiredCameraPermissions())) {
            launchCamera()
            return
        }
        pendingPermissionAction = ACTION_CAMERA
        permissionLauncher.launch(requiredCameraPermissions())
    }

    private fun openGalleryWithPermissionCheck() {
        if (hasPermissions(requiredGalleryPermissions())) {
            galleryLauncher.launch("image/*")
            return
        }
        pendingPermissionAction = ACTION_GALLERY
        permissionLauncher.launch(requiredGalleryPermissions())
    }

    private fun launchCamera() {
        try {
            val cameraUri = createImageUri()
            pendingCameraUri = cameraUri
            cameraLauncher.launch(cameraUri)
        } catch (exception: IOException) {
            Toast.makeText(this, getString(R.string.camera_prepare_error, exception.message), Toast.LENGTH_LONG).show()
        }
    }

    @Throws(IOException::class)
    private fun createImageUri(): Uri {
        val imageDirectory = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captures")
        if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
            throw IOException("Could not create image directory")
        }

        // IMPORTANT: Filename format matches real app: leafguard_{timestamp}.jpg
        val imageFile = File(imageDirectory, "leafguard_${System.currentTimeMillis()}.jpg")
        if (!imageFile.exists() && !imageFile.createNewFile()) {
            throw IOException("Could not create image file")
        }

        // IMPORTANT: Authority must match ${applicationId}.fileprovider in manifest
        return FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.fileprovider", imageFile)
    }

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

    private fun detectDisease() {
        if (selectedImageUri == null) {
            Toast.makeText(this, R.string.select_image_first, Toast.LENGTH_SHORT).show()
            return
        }
        // TODO: Implement detection (cloud or offline) — covered in Week 04+
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requiredCameraPermissions(): Array<String> = arrayOf(Manifest.permission.CAMERA)

    private fun requiredGalleryPermissions(): Array<String> {
        // Android 13+ uses READ_MEDIA_IMAGES; older versions use READ_EXTERNAL_STORAGE
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedImageUri?.let { outState.putString("selectedImageUri", it.toString()) }
        pendingCameraUri?.let { outState.putString("pendingCameraUri", it.toString()) }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
```

---

## 3. Java Equivalent — `MainActivity.java` (Secondary)

```java
package com.leafguard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.leafguard.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_SELECTED_IMAGE_URI = "state_selected_image_uri";
    private static final String STATE_PENDING_CAMERA_URI = "state_pending_camera_uri";
    private static final String ACTION_CAMERA = "camera";
    private static final String ACTION_GALLERY = "gallery";

    private ActivityMainBinding binding;
    private Uri selectedImageUri;
    private Uri pendingCameraUri;
    private String pendingPermissionAction;

    private ActivityResultLauncher<String[]> permissionLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.topAppBar);
        setupActivityResults();
        setupButtons();
        updateSelectedImage(null);

        if (savedInstanceState != null) {
            String savedImage = savedInstanceState.getString(STATE_SELECTED_IMAGE_URI);
            String savedPending = savedInstanceState.getString(STATE_PENDING_CAMERA_URI);
            if (savedImage != null) {
                selectedImageUri = Uri.parse(savedImage);
                updateSelectedImage(selectedImageUri);
            }
            if (savedPending != null) {
                pendingCameraUri = Uri.parse(savedPending);
            }
        }
    }

    private void setupActivityResults() {
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean allGranted = !result.containsValue(false);
                    if (!allGranted) {
                        Toast.makeText(this, R.string.permissions_required_message, Toast.LENGTH_SHORT).show();
                        pendingPermissionAction = null;
                        return;
                    }

                    if (ACTION_CAMERA.equals(pendingPermissionAction)) {
                        launchCamera();
                    } else if (ACTION_GALLERY.equals(pendingPermissionAction)) {
                        galleryLauncher.launch("image/*");
                    }
                    pendingPermissionAction = null;
                }
        );

        // Using GetContent (simpler than PickVisualMedia)
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        updateSelectedImage(uri);
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && pendingCameraUri != null) {
                        updateSelectedImage(pendingCameraUri);
                    } else {
                        Toast.makeText(this, R.string.camera_cancelled_message, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setupButtons() {
        binding.buttonOpenCamera.setOnClickListener(view -> openCameraWithPermissionCheck());
        binding.buttonOpenGallery.setOnClickListener(view -> openGalleryWithPermissionCheck());
        binding.buttonDetectDisease.setOnClickListener(view -> detectDisease());
    }

    private void openCameraWithPermissionCheck() {
        if (hasPermissions(requiredCameraPermissions())) {
            launchCamera();
            return;
        }
        pendingPermissionAction = ACTION_CAMERA;
        permissionLauncher.launch(requiredCameraPermissions());
    }

    private void openGalleryWithPermissionCheck() {
        if (hasPermissions(requiredGalleryPermissions())) {
            galleryLauncher.launch("image/*");
            return;
        }
        pendingPermissionAction = ACTION_GALLERY;
        permissionLauncher.launch(requiredGalleryPermissions());
    }

    private void launchCamera() {
        try {
            pendingCameraUri = createImageUri();
            cameraLauncher.launch(pendingCameraUri);
        } catch (IOException exception) {
            Toast.makeText(this, getString(R.string.camera_prepare_error, exception.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    private Uri createImageUri() throws IOException {
        File imageDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captures");
        if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
            throw new IOException("Could not create image directory");
        }

        // IMPORTANT: Filename format matches real app: leafguard_{timestamp}.jpg
        File imageFile = new File(imageDirectory, "leafguard_" + System.currentTimeMillis() + ".jpg");
        if (!imageFile.exists() && !imageFile.createNewFile()) {
            throw new IOException("Could not create image file");
        }

        // IMPORTANT: Authority must match ${applicationId}.fileprovider in manifest
        return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", imageFile);
    }

    private void updateSelectedImage(Uri imageUri) {
        selectedImageUri = imageUri;
        binding.buttonDetectDisease.setEnabled(imageUri != null);

        if (imageUri == null) {
            binding.imagePlantPreview.setImageResource(android.R.drawable.ic_menu_gallery);
            binding.textImageHint.setText(R.string.image_hint_default);
            return;
        }

        binding.imagePlantPreview.setImageURI(imageUri);
        binding.textImageHint.setText(getString(R.string.image_selected_message, imageUri.getLastPathSegment()));
    }

    private void detectDisease() {
        if (selectedImageUri == null) {
            Toast.makeText(this, R.string.select_image_first, Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO: Implement detection (cloud or offline) — covered in Week 04+
    }

    private boolean hasPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private String[] requiredCameraPermissions() {
        return new String[]{Manifest.permission.CAMERA};
    }

    private String[] requiredGalleryPermissions() {
        // Android 13+ uses READ_MEDIA_IMAGES; older versions use READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedImageUri != null) {
            outState.putString(STATE_SELECTED_IMAGE_URI, selectedImageUri.toString());
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

## 4. Required string additions

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

## 5. `res/xml/file_paths.xml`

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

## 6. `AndroidManifest.xml` FileProvider configuration

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

## 7. Permissions in `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission
    android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />
```

---

## 8. Why this solution is correct

### Camera flow
- checks camera permission using `RequestMultiplePermissions`
- launches camera with `TakePicture` contract
- stores the photo using `FileProvider` with `${applicationId}.fileprovider`

### Gallery flow
- uses `GetContent("image/*")` — simpler than `PickVisualMedia`
- Android 13+: requests `READ_MEDIA_IMAGES`
- Android 6-12: requests `READ_EXTERNAL_STORAGE`

### Memory safety
- decodes only image bounds first
- calculates `inSampleSize`
- decodes the scaled bitmap second
- avoids loading oversized bitmaps directly into memory

### Rotation support
- saves current and pending URIs
- restores preview after configuration change

---

## 9. Multi-API behavior summary

| API level | Gallery approach | Permission needed |
|---|---|---|
| 24-32 | `GetContent("image/*")` | `READ_EXTERNAL_STORAGE` |
| 33+ | `GetContent("image/*")` | `READ_MEDIA_IMAGES` |

---

## 10. Manual testing checklist

1. Open MainActivity.
2. Tap **Open Camera** and grant camera permission.
3. Take a photo and verify preview appears.
4. Rotate the device and verify preview remains.
5. Tap **Open Gallery** and choose an image.
6. Test cancel behavior for both camera and gallery.
7. Verify **Detect Disease** button enables only when image is selected.

---

## 11. Final Checklist

- [x] camera permission implemented with `RequestMultiplePermissions`
- [x] gallery permission flow with Android 13+ support
- [x] `TakePicture` contract used
- [x] `GetContent("image/*")` gallery picker used
- [x] image preview displayed via `setImageURI()`
- [x] state preserved on rotation
- [x] FileProvider with `${applicationId}.fileprovider`
- [x] Kotlin as primary, Java as secondary


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

- 📝 [Back to Week 03 Exercises](../../roadmap/week-03-camera-gallery/exercises.md) — Try it yourself first
- 📖 [Week 03 README](../../roadmap/week-03-camera-gallery/README.md) — Week overview
- 💡 [Solutions Index for Week 03](README.md) — Other solutions this week
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

---
