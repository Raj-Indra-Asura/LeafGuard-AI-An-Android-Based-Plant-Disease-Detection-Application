package com.leafguard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.leafguard.databinding.ActivityScanBinding;
import com.leafguard.ml.TFLiteClassifier;
import com.leafguard.network.ApiService;
import com.leafguard.network.PredictionResponse;
import com.leafguard.network.RetrofitClient;
import com.leafguard.ui.BottomNav;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Java twin of ScanActivity.kt.
 *
 * Scan tab — image capture/upload + cloud vs offline disease detection. This
 * is the same capture/detect flow that used to live directly on MainActivity;
 * it moved here so Home can become a lightweight dashboard and "Scan" can be
 * its own tab in the bottom navigation bar.
 */
public class ScanActivity extends AppCompatActivity {

    private static final String ACTION_CAMERA = "camera";
    private static final String ACTION_GALLERY = "gallery";

    private ActivityScanBinding binding;
    private ActivityResultLauncher<String[]> permissionLauncher;
    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;

    private Uri selectedImageUri;
    private Uri pendingCameraUri;
    private String pendingPermissionAction;
    private boolean cloudMode = true;
    private final ExecutorService detectionExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // A dashed stroke (see bg_dashed_upload.xml) doesn't render reliably
        // with hardware acceleration, so this View draws in software instead.
        binding.cardUploadArea.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        BottomNav.setup(this, binding.bottomNavigation, R.id.nav_scan);
        setupActivityResults();
        setupModeToggle();
        setupButtons();
        updateSelectedImage(null);
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
        binding.buttonBack.setOnClickListener(view -> finish());
        binding.cardUploadArea.setOnClickListener(view -> showImageSourceChooser());
        binding.buttonDetectDisease.setOnClickListener(view -> detectDisease());
    }

    private void showImageSourceChooser() {
        String[] options = new String[]{
                getString(R.string.choose_image_source_camera),
                getString(R.string.choose_image_source_gallery)
        };
        new AlertDialog.Builder(this)
                .setTitle(R.string.choose_image_source_title)
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        openCameraWithPermissionCheck();
                    } else {
                        openGalleryWithPermissionCheck();
                    }
                })
                .show();
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

        if (imageUri == null) {
            binding.imagePlantPreview.setVisibility(View.GONE);
            binding.layoutUploadPlaceholder.setVisibility(View.VISIBLE);
            binding.layoutDetectionControls.setVisibility(View.GONE);
            return;
        }

        binding.imagePlantPreview.setImageURI(imageUri);
        binding.imagePlantPreview.setVisibility(View.VISIBLE);
        binding.layoutUploadPlaceholder.setVisibility(View.GONE);
        binding.layoutDetectionControls.setVisibility(View.VISIBLE);
    }

    private void detectDisease() {
        if (selectedImageUri == null) {
            Toast.makeText(this, R.string.select_image_first, Toast.LENGTH_SHORT).show();
            return;
        }

        setDetectionInProgress(true);
        if (cloudMode) {
            runCloudDetection();
        } else {
            runOfflineDetection();
        }
    }

    private void runCloudDetection() {
        File uploadFile;
        try {
            uploadFile = copyUriToCacheFile(selectedImageUri);
        } catch (IOException exception) {
            setDetectionInProgress(false);
            Toast.makeText(this, getString(R.string.image_prepare_error, exception.getMessage()), Toast.LENGTH_LONG).show();
            return;
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse(getImageMimeType(selectedImageUri)), uploadFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", uploadFile.getName(), requestBody);
        ApiService apiService = RetrofitClient.getInstance(getBackendBaseUrl()).create(ApiService.class);
        apiService.uploadImage(imagePart).enqueue(new Callback<PredictionResponse>() {
            @Override
            public void onResponse(@NonNull Call<PredictionResponse> call, @NonNull Response<PredictionResponse> response) {
                setDetectionInProgress(false);
                PredictionResponse prediction = response.body();
                if (!response.isSuccessful() || prediction == null) {
                    Toast.makeText(ScanActivity.this, R.string.cloud_prediction_failed, Toast.LENGTH_LONG).show();
                    return;
                }
                openResult(prediction);
            }

            @Override
            public void onFailure(@NonNull Call<PredictionResponse> call, @NonNull Throwable throwable) {
                setDetectionInProgress(false);
                Toast.makeText(ScanActivity.this, getString(R.string.network_error_format, throwable.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void runOfflineDetection() {
        detectionExecutor.execute(() -> {
            try (TFLiteClassifier classifier = new TFLiteClassifier(this)) {
                Bitmap bitmap = loadBitmap(selectedImageUri);
                PredictionResponse prediction = classifier.classify(bitmap);
                runOnUiThread(() -> {
                    setDetectionInProgress(false);
                    openResult(prediction);
                });
            } catch (IOException | RuntimeException exception) {
                runOnUiThread(() -> {
                    setDetectionInProgress(false);
                    Toast.makeText(this, getString(R.string.offline_prediction_failed, exception.getMessage()), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private File copyUriToCacheFile(Uri imageUri) throws IOException {
        File uploadFile = new File(getCacheDir(), "leafguard_upload_" + System.currentTimeMillis() + ".jpg");
        try (InputStream inputStream = getContentResolver().openInputStream(imageUri);
             FileOutputStream outputStream = new FileOutputStream(uploadFile)) {
            if (inputStream == null) {
                throw new IOException("Unable to open selected image. The file may have been moved or deleted.");
            }
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return uploadFile;
    }

    private Bitmap loadBitmap(Uri imageUri) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
            return ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, false);
        }
        return MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
    }

    private String getImageMimeType(Uri imageUri) {
        String mimeType = getContentResolver().getType(imageUri);
        if (mimeType == null || !mimeType.startsWith("image/")) {
            return "image/*";
        }
        return mimeType;
    }

    private String getBackendBaseUrl() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String baseUrl = prefs.getString(SettingsActivity.PREF_BACKEND_URL, SettingsActivity.DEFAULT_BACKEND_URL);
        if (baseUrl == null) {
            baseUrl = "";
        }
        baseUrl = baseUrl.trim().isEmpty() ? SettingsActivity.DEFAULT_BACKEND_URL : baseUrl.trim();
        return baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    private void openResult(PredictionResponse prediction) {
        if (getConfidencePercentage(prediction.getConfidence()) < getConfidenceThreshold()) {
            Toast.makeText(this, R.string.low_confidence_warning, Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(ScanActivity.this, ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_DISEASE_NAME, prediction.getDisease());
        intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, prediction.getConfidence());
        intent.putExtra(ResultActivity.EXTRA_SYMPTOMS, prediction.getSymptoms());
        intent.putExtra(ResultActivity.EXTRA_TREATMENT, prediction.getTreatment());
        intent.putExtra(ResultActivity.EXTRA_PREVENTION, prediction.getPrevention());
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, selectedImageUri.toString());
        startActivity(intent);
    }

    private float getConfidencePercentage(float confidence) {
        return confidence * 100f;
    }

    private int getConfidenceThreshold() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt(SettingsActivity.PREF_CONFIDENCE_THRESHOLD, SettingsActivity.DEFAULT_CONFIDENCE_THRESHOLD);
    }

    private void setDetectionInProgress(boolean inProgress) {
        binding.progressDetection.setVisibility(inProgress ? View.VISIBLE : View.GONE);
        binding.buttonDetectDisease.setEnabled(!inProgress && selectedImageUri != null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detectionExecutor.shutdown();
        binding = null;
    }
}
