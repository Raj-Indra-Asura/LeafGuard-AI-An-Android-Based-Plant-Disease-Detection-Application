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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.leafguard.databinding.ActivityMainBinding
import com.leafguard.ml.TFLiteClassifier
import com.leafguard.network.ApiService
import com.leafguard.network.PredictionResponse
import com.leafguard.network.RetrofitClient
import com.leafguard.utils.NotificationHelper
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
 * Kotlin twin of MainActivity.java.
 *
 * Same behavior: image capture/selection with permission checks, cloud vs
 * offline detection toggle, and navigation to History / Disease Library /
 * Settings / Result. Offline detection uses lifecycleScope + Dispatchers.IO
 * coroutines instead of the Java app's single-thread ExecutorService.
 */
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
    private var cloudMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        this.binding = binding
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        NotificationHelper.createChannel(this)

        setupActivityResults()
        setupToolbar()
        setupModeToggle()
        setupButtons()
        updateSelectedImage(null)
        requestNotificationPermissionIfNeeded()
    }

    private fun setupToolbar() {
        binding?.topAppBar?.setTitle(R.string.app_name)
        binding?.topAppBar?.setSubtitle(R.string.main_subtitle)
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
        binding.buttonOpenCamera.setOnClickListener { openCameraWithPermissionCheck() }
        binding.buttonOpenGallery.setOnClickListener { openGalleryWithPermissionCheck() }
        binding.buttonDetectDisease.setOnClickListener { detectDisease() }
        binding.buttonHistory.setOnClickListener { startActivity(Intent(this, HistoryActivity::class.java)) }
        binding.buttonDiseaseLibrary.setOnClickListener { startActivity(Intent(this, DiseaseLibraryActivity::class.java)) }
        binding.buttonSettings.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
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
                    Toast.makeText(this@MainActivity, R.string.cloud_prediction_failed, Toast.LENGTH_LONG).show()
                    return
                }
                openResult(prediction)
            }

            override fun onFailure(call: Call<PredictionResponse>, throwable: Throwable) {
                setDetectionInProgress(false)
                Toast.makeText(this@MainActivity, getString(R.string.network_error_format, throwable.message), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun runOfflineDetection() {
        val imageUri = selectedImageUri ?: return
        lifecycleScope.launch {
            try {
                val prediction = withContext(Dispatchers.IO) {
                    TFLiteClassifier(this@MainActivity).use { classifier ->
                        val bitmap = loadBitmap(imageUri)
                        classifier.classify(bitmap)
                    }
                }
                setDetectionInProgress(false)
                openResult(prediction)
            } catch (exception: IOException) {
                setDetectionInProgress(false)
                Toast.makeText(this@MainActivity, getString(R.string.offline_prediction_failed, exception.message), Toast.LENGTH_LONG).show()
            } catch (exception: RuntimeException) {
                setDetectionInProgress(false)
                Toast.makeText(this@MainActivity, getString(R.string.offline_prediction_failed, exception.message), Toast.LENGTH_LONG).show()
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

        val intent = Intent(this@MainActivity, ResultActivity::class.java)
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

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
