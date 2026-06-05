package com.leafguard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.leafguard.databinding.ActivityMainBinding;
import com.leafguard.utils.NotificationHelper;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String ACTION_CAMERA = "camera";
    private static final String ACTION_GALLERY = "gallery";

    private ActivityMainBinding binding;
    private ActivityResultLauncher<String[]> permissionLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;

    private Uri selectedImageUri;
    private Uri pendingCameraUri;
    private String pendingPermissionAction;
    private boolean cloudMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.topAppBar);
        NotificationHelper.createChannel(this);

        setupActivityResults();
        setupToolbar();
        setupModeToggle();
        setupButtons();
        updateSelectedImage(null);
        requestNotificationPermissionIfNeeded();
    }

    private void setupToolbar() {
        binding.topAppBar.setTitle(R.string.app_name);
        binding.topAppBar.setSubtitle(R.string.main_subtitle);
    }

    private void setupModeToggle() {
        binding.toggleDetectionMode.check(R.id.buttonCloudMode);
        binding.toggleDetectionMode.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) {
                return;
            }
            cloudMode = checkedId == R.id.buttonCloudMode;
            updateModeDescription();
        });
        updateModeDescription();
    }

    private void setupButtons() {
        binding.buttonOpenCamera.setOnClickListener(view -> openCameraWithPermissionCheck());
        binding.buttonOpenGallery.setOnClickListener(view -> openGalleryWithPermissionCheck());
        binding.buttonDetectDisease.setOnClickListener(view -> launchMockDetection());
        binding.buttonHistory.setOnClickListener(view -> startActivity(new Intent(this, HistoryActivity.class)));
    }

    private void setupActivityResults() {
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean allGranted = true;
                    for (Boolean granted : result.values()) {
                        if (Boolean.FALSE.equals(granted)) {
                            allGranted = false;
                            break;
                        }
                    }

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

        File imageFile = new File(imageDirectory, "leafguard_" + System.currentTimeMillis() + ".jpg");
        if (!imageFile.exists() && !imageFile.createNewFile()) {
            throw new IOException("Could not create image file");
        }

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

    private void launchMockDetection() {
        if (selectedImageUri == null) {
            Toast.makeText(this, R.string.select_image_first, Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressDetection.setVisibility(View.VISIBLE);
        binding.buttonDetectDisease.setEnabled(false);
        binding.buttonDetectDisease.postDelayed(() -> {
            binding.progressDetection.setVisibility(View.GONE);
            binding.buttonDetectDisease.setEnabled(true);

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra(ResultActivity.EXTRA_DISEASE_NAME,
                    cloudMode ? "Tomato Early Blight" : "Tomato Healthy");
            intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, cloudMode ? 0.91f : 0.83f);
            intent.putExtra(ResultActivity.EXTRA_SYMPTOMS,
                    cloudMode
                            ? "Brown lesions with concentric rings on older leaves."
                            : "Leaf appears green, evenly colored, and free of lesions.");
            intent.putExtra(ResultActivity.EXTRA_TREATMENT,
                    cloudMode
                            ? "Prune infected leaves, improve airflow, and apply a recommended fungicide."
                            : "No treatment needed. Keep monitoring plant health.");
            intent.putExtra(ResultActivity.EXTRA_PREVENTION,
                    cloudMode
                            ? "Avoid overhead watering and rotate crops between seasons."
                            : "Continue balanced watering, spacing, and preventive scouting.");
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, selectedImageUri.toString());
            startActivity(intent);
        }, 1200L);
    }

    private void updateModeDescription() {
        binding.textModeDescription.setText(
                cloudMode ? R.string.cloud_mode_description : R.string.offline_mode_description
        );
    }

    private boolean hasPermissions(@NonNull String[] permissions) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        }
        return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(new String[]{Manifest.permission.POST_NOTIFICATIONS});
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
