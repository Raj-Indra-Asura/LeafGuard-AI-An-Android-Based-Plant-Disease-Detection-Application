package com.leafguard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.leafguard.databinding.ActivityScanBinding
import com.leafguard.ml.TFLiteClassifier
import com.leafguard.network.ApiService
import com.leafguard.network.PredictionResponse
import com.leafguard.network.RetrofitClient
import com.leafguard.ui.setupBottomNav
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Scan tab — image capture/upload + cloud vs offline disease detection.
 *
 * This is the same capture/detect flow that used to live directly on
 * MainActivity; it moved here so Home can become a lightweight dashboard and
 * "Scan" can be its own tab in the bottom navigation bar (see the Home
 * screenshot: tapping "Start Scanning" opens this Activity).
 */
class ScanActivity : AppCompatActivity() {

    companion object {
        private const val ACTION_CAMERA = "camera"
        private const val ACTION_GALLERY = "gallery"
    }

    private var binding: ActivityScanBinding? = null
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>

    private var selectedImageUri: Uri? = null
    private var pendingCameraUri: Uri? = null
    private var pendingPermissionAction: String? = null
    private var cloudMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityScanBinding.inflate(layoutInflater)
        this.binding = binding
        setContentView(binding.root)

        // A dashed stroke (see bg_dashed_upload.xml) doesn't render reliably
        // with hardware acceleration, so this View draws in software instead.
        binding.cardUploadArea.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        setupBottomNav(binding.bottomNavigation, R.id.nav_scan)
        setupActivityResults()
        setupModeToggle()
        setupButtons()
        updateSelectedImage(null)
    }

    private fun setupModeToggle() {
        val binding = binding ?: return
        binding.toggleDetectionMode.check(R.id.buttonCloudMode)
        binding.toggleDetectionMode.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) {
                return@addOnButtonCheckedListener
            }
            cloudMode = checkedId == R.id.buttonCloudMode
            updateModeDescription()
        }
        updateModeDescription()
    }

    private fun setupButtons() {
        val binding = binding ?: return
        binding.buttonBack.setOnClickListener { finish() }
        binding.cardUploadArea.setOnClickListener { showImageSourceChooser() }
        binding.buttonDetectDisease.setOnClickListener { detectDisease() }
    }

    private fun showImageSourceChooser() {
        val options = arrayOf(
            getString(R.string.choose_image_source_camera),
            getString(R.string.choose_image_source_gallery)
        )
        AlertDialog.Builder(this)
            .setTitle(R.string.choose_image_source_title)
            .setItems(options) { _, which ->
                if (which == 0) openCameraWithPermissionCheck() else openGalleryWithPermissionCheck()
            }
            .show()
    }

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

        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                updateSelectedImage(uri)
            }
        }

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

        val imageFile = File(imageDirectory, "leafguard_${System.currentTimeMillis()}.jpg")
        if (!imageFile.exists() && !imageFile.createNewFile()) {
            throw IOException("Could not create image file")
        }

        return FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.fileprovider", imageFile)
    }

    private fun updateSelectedImage(imageUri: Uri?) {
        val binding = binding ?: return
        selectedImageUri = imageUri

        if (imageUri == null) {
            binding.imagePlantPreview.visibility = View.GONE
            binding.layoutUploadPlaceholder.visibility = View.VISIBLE
            binding.layoutDetectionControls.visibility = View.GONE
            return
        }

        binding.imagePlantPreview.setImageURI(imageUri)
        binding.imagePlantPreview.visibility = View.VISIBLE
        binding.layoutUploadPlaceholder.visibility = View.GONE
        binding.layoutDetectionControls.visibility = View.VISIBLE
    }

    private fun detectDisease() {
        if (selectedImageUri == null) {
            Toast.makeText(this, R.string.select_image_first, Toast.LENGTH_SHORT).show()
            return
        }

        setDetectionInProgress(true)
        if (cloudMode) {
            runCloudDetection()
        } else {
            runOfflineDetection()
        }
    }

    private fun runCloudDetection() {
        val imageUri = selectedImageUri ?: return
        val uploadFile: File
        try {
            uploadFile = copyUriToCacheFile(imageUri)
        } catch (exception: IOException) {
            setDetectionInProgress(false)
            Toast.makeText(this, getString(R.string.image_prepare_error, exception.message), Toast.LENGTH_LONG).show()
            return
        }

        val requestBody = uploadFile.asRequestBody(getImageMimeType(imageUri).toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", uploadFile.name, requestBody)
        val apiService = RetrofitClient.getInstance(getBackendBaseUrl()).create(ApiService::class.java)
        apiService.uploadImage(imagePart).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                setDetectionInProgress(false)
                val prediction = response.body()
                if (!response.isSuccessful || prediction == null) {
                    Toast.makeText(this@ScanActivity, R.string.cloud_prediction_failed, Toast.LENGTH_LONG).show()
                    return
                }
                openResult(prediction)
            }

            override fun onFailure(call: Call<PredictionResponse>, throwable: Throwable) {
                setDetectionInProgress(false)
                Toast.makeText(this@ScanActivity, getString(R.string.network_error_format, throwable.message), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun runOfflineDetection() {
        val imageUri = selectedImageUri ?: return
        lifecycleScope.launch {
            try {
                val prediction = withContext(Dispatchers.IO) {
                    TFLiteClassifier(this@ScanActivity).use { classifier ->
                        val bitmap = loadBitmap(imageUri)
                        classifier.classify(bitmap)
                    }
                }
                setDetectionInProgress(false)
                openResult(prediction)
            } catch (exception: IOException) {
                setDetectionInProgress(false)
                Toast.makeText(this@ScanActivity, getString(R.string.offline_prediction_failed, exception.message), Toast.LENGTH_LONG).show()
            } catch (exception: RuntimeException) {
                setDetectionInProgress(false)
                Toast.makeText(this@ScanActivity, getString(R.string.offline_prediction_failed, exception.message), Toast.LENGTH_LONG).show()
            }
        }
    }

    @Throws(IOException::class)
    private fun copyUriToCacheFile(imageUri: Uri): File {
        val uploadFile = File(cacheDir, "leafguard_upload_${System.currentTimeMillis()}.jpg")
        contentResolver.openInputStream(imageUri).use { inputStream ->
            if (inputStream == null) {
                throw IOException("Unable to open selected image. The file may have been moved or deleted.")
            }
            FileOutputStream(uploadFile).use { outputStream ->
                inputStream.copyTo(outputStream, bufferSize = 8192)
            }
        }
        return uploadFile
    }

    @Throws(IOException::class)
    private fun loadBitmap(imageUri: Uri): Bitmap {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, imageUri)
            return ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, false)
        }
        @Suppress("DEPRECATION")
        return MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
    }

    private fun getImageMimeType(imageUri: Uri): String {
        val mimeType = contentResolver.getType(imageUri)
        if (mimeType == null || !mimeType.startsWith("image/")) {
            return "image/*"
        }
        return mimeType
    }

    private fun getBackendBaseUrl(): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        var baseUrl = prefs.getString(SettingsActivity.PREF_BACKEND_URL, SettingsActivity.DEFAULT_BACKEND_URL) ?: ""
        baseUrl = if (baseUrl.trim().isEmpty()) SettingsActivity.DEFAULT_BACKEND_URL else baseUrl.trim()
        return if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
    }

    private fun openResult(prediction: PredictionResponse) {
        if (getConfidencePercentage(prediction.confidence) < getConfidenceThreshold()) {
            Toast.makeText(this, R.string.low_confidence_warning, Toast.LENGTH_LONG).show()
        }

        val intent = Intent(this@ScanActivity, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_DISEASE_NAME, prediction.disease)
        intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, prediction.confidence)
        intent.putExtra(ResultActivity.EXTRA_SYMPTOMS, prediction.symptoms)
        intent.putExtra(ResultActivity.EXTRA_TREATMENT, prediction.treatment)
        intent.putExtra(ResultActivity.EXTRA_PREVENTION, prediction.prevention)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, selectedImageUri.toString())
        startActivity(intent)
    }

    private fun getConfidencePercentage(confidence: Float): Float = confidence * 100f

    private fun getConfidenceThreshold(): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        return prefs.getInt(SettingsActivity.PREF_CONFIDENCE_THRESHOLD, SettingsActivity.DEFAULT_CONFIDENCE_THRESHOLD)
    }

    private fun setDetectionInProgress(inProgress: Boolean) {
        val binding = binding ?: return
        binding.progressDetection.visibility = if (inProgress) View.VISIBLE else View.GONE
        binding.buttonDetectDisease.isEnabled = !inProgress && selectedImageUri != null
    }

    private fun updateModeDescription() {
        binding?.textModeDescription?.setText(
            if (cloudMode) R.string.cloud_mode_description else R.string.offline_mode_description
        )
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requiredCameraPermissions(): Array<String> = arrayOf(Manifest.permission.CAMERA)

    private fun requiredGalleryPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
