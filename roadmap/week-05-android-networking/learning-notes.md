# Week 05 Learning Notes: Android Networking From the Verified Contract

## Purpose

These notes teach only what is needed to connect the Week 03 Android image URI to the Week 04 FastAPI contract. Read the concepts before the exercises and build task.

Section 12 is the authoritative reconstruction appendix. It contains every changed or new Kotlin-track file in full, with exact logical line counts. The snapshot was compiled independently with `./gradlew assembleDebug`.

---

## 1. How Week 05 Grows From Weeks 03 and 04

Week 05 has two verified inputs:

| Input | Existing fact | Week 05 use |
|---|---|---|
| Week 03 Android | `selectedImageUri` points to a camera or gallery image | Read its bytes for upload |
| Week 04 FastAPI | `POST /predict` accepts multipart field `image` | Define the Retrofit interface |
| Week 04 JSON | Successful response has eight named fields | Define `PredictionResponse` |

```text
selectedImageUri
  -> temporary cache file
  -> RequestBody
  -> MultipartBody.Part named image
  -> Retrofit POST /predict
  -> Week 04 JSON
  -> PredictionResponse
  -> Intent extras
  -> ResultActivity
```

Week 05 does not alter image capture or the backend response shape. It connects them.

---

## 2. Client and Server Responsibilities

| Android client owns | FastAPI server owns |
|---|---|
| User image selection | Upload validation |
| URI byte access | Image decoding and resizing |
| Multipart request construction | Mock or real prediction mode |
| Loading and error feedback | HTTP status codes |
| JSON parsing | Eight-field JSON response |
| Result display | Guidance fallback text |

A network failure does not mean the model is wrong. A 400 response does not mean Android could not reach the server. Separating responsibilities makes debugging testable.

---

## 3. Why Retrofit

Retrofit turns an annotated Kotlin interface into an HTTP client implementation.

```kotlin
interface ApiService {
    @Multipart
    @POST("predict")
    fun uploadImage(@Part image: MultipartBody.Part): Call<PredictionResponse>
}
```

| Part | Meaning |
|---|---|
| `@Multipart` | Encode the request as multipart form data. |
| `@POST("predict")` | Append `predict` to the base URL and use POST. |
| `@Part` | Put the supplied file part in the request body. |
| `Call<PredictionResponse>` | Represent an operation that later returns parsed data. |

Retrofit is built on OkHttp. Gson is the converter that maps JSON keys to Kotlin properties.

---

## 4. Base URL and Emulator Addressing

The Android emulator is a separate virtual device.

| Address | Meaning from the emulator |
|---|---|
| `localhost` / `127.0.0.1` | The emulator itself |
| `10.0.2.2` | The development computer |

The local Week 05 URL is:

```text
http://10.0.2.2:8000/
```

The trailing slash is required by Retrofit. A physical phone needs the development computer's LAN address and a matching network-security rule; never commit a private address as shared project truth.

---

## 5. From Android URI to Upload Bytes

A content URI is not a normal filesystem path. Code such as `File(uri.path)` is unreliable for gallery content.

Week 05 uses this safe sequence:

1. Open the URI with `contentResolver.openInputStream(uri)`.
2. Create a temporary file under `cacheDir`.
3. Copy bytes with an 8192-byte buffer.
4. Wrap the temporary file in an OkHttp `RequestBody`.
5. Delete the temporary file after response or failure.

The original camera/gallery content is not modified.

---

## 6. Multipart Contract

The field name is part of the Week 04 API contract:

```kotlin
val imagePart = MultipartBody.Part.createFormData(
    "image",
    uploadFile.name,
    requestBody
)
```

Changing `image` to `file`, `photo`, or another name normally produces HTTP 422 because FastAPI cannot bind the required parameter.

The client also supplies:

- a filename
- an image MIME type
- binary bytes

OkHttp generates the multipart boundary automatically.

---

## 7. Exact Eight-Field JSON Model

Week 04 returns:

| JSON key | Kotlin property | Type | Meaning |
|---|---|---|---|
| `model_label` | `modelLabel` | `String` | Canonical model-facing label |
| `disease` | `disease` | `String` | Display-friendly name |
| `confidence` | `confidence` | `Float` | Value from 0.0 to 1.0 |
| `uncertain` | `uncertain` | `Boolean` | Below server threshold |
| `guidance_available` | `guidanceAvailable` | `Boolean` | Reviewed guidance exists |
| `symptoms` | `symptoms` | `String` | Guidance or safe fallback |
| `treatment` | `treatment` | `String` | Guidance or safe fallback |
| `prevention` | `prevention` | `String` | Guidance or safe fallback |

`@SerializedName` is required where snake_case JSON maps to camelCase Kotlin.

Do not reduce the model to five fields. Android must preserve the complete Week 04 contract even if the first Result screen emphasizes only some fields.

---

## 8. Asynchronous Request and UI State

`enqueue(...)` starts the network operation without blocking Android's main thread.

```text
tap Detect
  -> show ProgressBar and disable buttons
  -> enqueue request
  -> onResponse OR onFailure
  -> hide ProgressBar and restore buttons
```

| Callback | Meaning |
|---|---|
| `onResponse` | A server response arrived, including 4xx and 5xx responses. |
| `onFailure` | No usable HTTP response arrived, such as connection refusal, timeout, or conversion failure. |

Inside `onResponse`, check both `response.isSuccessful` and `response.body() != null` before opening ResultActivity.

---

## 9. Error Categories

| Observation | Callback | Example | Week 05 behavior |
|---|---|---|---|
| HTTP 200 + body | `onResponse` | Valid mock response | Open ResultActivity |
| HTTP 400/413/422/503 | `onResponse` | Server rejected request | Show status-based message |
| Connection refused | `onFailure` | Backend stopped | Show network message |
| Timeout | `onFailure` | Server unreachable | Show network message |
| URI read failure | Before request | Content unavailable | Restore UI and explain |

Every terminal path must hide progress and permit another attempt.

---

## 10. Android Network Security

The manifest requires:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

`INTERNET` is an install-time permission; Android does not show a runtime dialog.

Android 9+ blocks cleartext HTTP by default. Week 05 permits it only for `10.0.2.2`:

```xml
<base-config cleartextTrafficPermitted="false" />
<domain-config cleartextTrafficPermitted="true">
    <domain includeSubdomains="false">10.0.2.2</domain>
</domain-config>
```

Production must use HTTPS. CORS is not Android authentication and does not replace transport security.

---

## 11. Result Navigation and Honest Mock Mode

After a successful response, `ScanActivity` passes all eight values as Intent extras. `ResultActivity` formats confidence as a percentage and displays uncertainty and guidance availability explicitly.

The screen displays what the backend returned. If `/health` says `use_mock: true`, the result proves:

- Android reached FastAPI
- multipart upload worked
- JSON parsing worked
- navigation and display worked

It does **not** prove that a real model recognized a disease. Week 06 owns that claim.

Common mistakes:

- using `localhost` in the emulator
- sending multipart field `file` instead of `image`
- treating a content URI as a direct file path
- using `execute()` on the UI thread
- checking only `response.body()` and ignoring the status
- leaving the progress indicator visible after failure
- allowing cleartext for every domain
- omitting `model_label`, `uncertain`, or `guidance_available`
- calling mock output real inference

---

## 12. End-of-Week-05 File Inventory (Exact Files, Exact Code, Exact Size)

Week 03 ended with 17 required Android source/resource files. Week 04 changed no Android files. Week 05 creates 4 files and expands 7 files in the Kotlin primary track.

### 12.1 Change Summary: Week 04 -> Week 05

| Change | Count | Files |
|---|---:|---|
| New | 4 | Three `network/*.kt` files and `network_security_config.xml` |
| Expanded | 7 | Gradle, manifest, Scan/Result Activities, Scan/Result layouts, strings |
| Unchanged Android source/resources | 10 | Main, History, Library, Settings, their layouts, colors, themes, FileProvider paths |
| Backend files changed | 0 | Week 04 contract is consumed unchanged |
| Later-week files added | 0 | No Room, TFLite, offline assets, notification, or bottom navigation |

**Required Android source/resource files after Week 05: 21.**

**Total across the 11 changed/new cumulative files: 726 logical lines.**

Logical line count ignores whether the final line ends with a newline. These counts describe the teaching snapshot, not the repository's later fully evolved app.

### 12.2 Exact Week 05 Tree

```text
android-app-kotlin/
|-- settings.gradle                                  UNCHANGED
|-- build.gradle                                     UNCHANGED
|-- gradle.properties                                UNCHANGED
`-- app/
    |-- build.gradle                                 EXPANDED   47 lines
    `-- src/main/
        |-- AndroidManifest.xml                      EXPANDED   55 lines
        |-- java/com/leafguard/
        |   |-- MainActivity.kt                      UNCHANGED
        |   |-- ScanActivity.kt                      EXPANDED  247 lines
        |   |-- ResultActivity.kt                    EXPANDED   56 lines
        |   |-- HistoryActivity.kt                   UNCHANGED
        |   |-- DiseaseLibraryActivity.kt            UNCHANGED
        |   |-- SettingsActivity.kt                  UNCHANGED
        |   `-- network/
        |       |-- ApiService.kt                    NEW        13 lines
        |       |-- PredictionResponse.kt            NEW        22 lines
        |       `-- RetrofitClient.kt                NEW        33 lines
        `-- res/
            |-- layout/
            |   |-- activity_main.xml                UNCHANGED
            |   |-- activity_scan.xml                EXPANDED   76 lines
            |   |-- activity_result.xml              EXPANDED  115 lines
            |   |-- activity_history.xml             UNCHANGED
            |   |-- activity_disease_library.xml     UNCHANGED
            |   `-- activity_settings.xml            UNCHANGED
            |-- values/
            |   |-- strings.xml                      EXPANDED   55 lines
            |   |-- colors.xml                       UNCHANGED
            |   `-- themes.xml                       UNCHANGED
            `-- xml/
                |-- file_provider_paths.xml          UNCHANGED
                `-- network_security_config.xml      NEW         7 lines
```

The Java track under `android-app/` mirrors this behavior with Java classes. Kotlin remains the authoritative learning snapshot.

### 12.3 Expanded File: `app/build.gradle` (40 -> 47 lines)

Week 03 used four Android dependencies. Week 05 adds Retrofit, the Gson converter, and OkHttp logging. `buildConfig` is enabled because the cumulative ScanActivity derives its FileProvider authority from `BuildConfig.APPLICATION_ID`.

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace 'com.leafguard'
    compileSdk 34

    defaultConfig {
        applicationId "com.leafguard"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "0.1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig true
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
}
```

### 12.4 Expanded File: `AndroidManifest.xml` (53 -> 55 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-feature
        android:name="android.hardware.camera"
        android:required="false" />

    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:icon="@android:drawable/ic_menu_gallery"
        android:label="@string/app_name"
        android:networkSecurityConfig="@xml/network_security_config"
        android:supportsRtl="true"
        android:theme="@style/Theme.LeafGuardAI">

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_provider_paths" />
        </provider>

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity
            android:name=".ScanActivity"
            android:exported="false" />
        <activity
            android:name=".ResultActivity"
            android:exported="false" />
        <activity
            android:name=".HistoryActivity"
            android:exported="false" />
        <activity
            android:name=".DiseaseLibraryActivity"
            android:exported="false" />
        <activity
            android:name=".SettingsActivity"
            android:exported="false" />
    </application>

</manifest>
```

### 12.5 New File: `network/ApiService.kt` (13 lines)

```kotlin
package com.leafguard.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("predict")
    fun uploadImage(@Part image: MultipartBody.Part): Call<PredictionResponse>
}
```

### 12.6 New File: `network/PredictionResponse.kt` (22 lines)

```kotlin
package com.leafguard.network

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("model_label")
    val modelLabel: String,
    @SerializedName("disease")
    val disease: String,
    @SerializedName("confidence")
    val confidence: Float,
    @SerializedName("uncertain")
    val uncertain: Boolean,
    @SerializedName("guidance_available")
    val guidanceAvailable: Boolean,
    @SerializedName("symptoms")
    val symptoms: String,
    @SerializedName("treatment")
    val treatment: String,
    @SerializedName("prevention")
    val prevention: String
)
```

### 12.7 New File: `network/RetrofitClient.kt` (33 lines)

```kotlin
package com.leafguard.network

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    private val apiClient: Retrofit by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        apiClient.create(ApiService::class.java)
    }
}
```

### 12.8 Expanded File: `ScanActivity.kt` (132 -> 247 lines)

The full file preserves all Week 03 permission, camera, gallery, FileProvider, preview, and saved-state code. Week 05 adds cache copying, multipart creation, callbacks, progress state, and result navigation.

```kotlin
package com.leafguard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.leafguard.network.PredictionResponse
import com.leafguard.network.RetrofitClient
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanActivity : AppCompatActivity() {

    private lateinit var imagePreview: ImageView
    private lateinit var textImageStatus: TextView
    private lateinit var buttonDetectDisease: Button
    private lateinit var progressUpload: ProgressBar

    private var selectedImageUri: Uri? = null
    private var pendingCameraUri: Uri? = null

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera()
        } else {
            Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        val cameraUri = pendingCameraUri
        if (success && cameraUri != null) {
            updateSelectedImage(cameraUri)
        } else {
            Toast.makeText(this, R.string.camera_cancelled, Toast.LENGTH_SHORT).show()
        }
        pendingCameraUri = null
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            updateSelectedImage(uri)
        } else {
            Toast.makeText(this, R.string.gallery_cancelled, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        imagePreview = findViewById(R.id.imagePreview)
        textImageStatus = findViewById(R.id.textImageStatus)
        buttonDetectDisease = findViewById(R.id.buttonDetectDisease)
        progressUpload = findViewById(R.id.progressUpload)

        findViewById<Button>(R.id.buttonTakePhoto).setOnClickListener {
            openCameraWithPermissionCheck()
        }
        findViewById<Button>(R.id.buttonChooseGallery).setOnClickListener {
            galleryLauncher.launch("image/*")
        }
        buttonDetectDisease.setOnClickListener {
            uploadSelectedImage()
        }

        savedInstanceState?.getString(KEY_SELECTED_IMAGE_URI)?.let { uriText ->
            updateSelectedImage(Uri.parse(uriText))
        }
    }

    private fun openCameraWithPermissionCheck() {
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            launchCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun launchCamera() {
        try {
            val imageUri = createImageUri()
            pendingCameraUri = imageUri
            cameraLauncher.launch(imageUri)
        } catch (exception: IOException) {
            pendingCameraUri = null
            Toast.makeText(this, R.string.camera_file_error, Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    private fun createImageUri(): Uri {
        val imageDirectory = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captures")
        if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
            throw IOException("Could not create image directory")
        }

        val imageFile = File(imageDirectory, "leafguard_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            this,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            imageFile
        )
    }

    private fun updateSelectedImage(uri: Uri) {
        selectedImageUri = uri
        imagePreview.setImageURI(uri)
        textImageStatus.setText(R.string.image_ready_for_upload)
        buttonDetectDisease.isEnabled = true
    }

    private fun uploadSelectedImage() {
        val imageUri = selectedImageUri
        if (imageUri == null) {
            Toast.makeText(this, R.string.select_image_first, Toast.LENGTH_SHORT).show()
            return
        }

        setUploadInProgress(true)
        val uploadFile = try {
            copyUriToCacheFile(imageUri)
        } catch (exception: IOException) {
            setUploadInProgress(false)
            Toast.makeText(this, R.string.image_prepare_error, Toast.LENGTH_LONG).show()
            return
        }

        val mimeType = contentResolver.getType(imageUri) ?: "image/*"
        val requestBody = uploadFile.asRequestBody(mimeType.toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", uploadFile.name, requestBody)

        RetrofitClient.apiService.uploadImage(imagePart).enqueue(
            object : Callback<PredictionResponse> {
                override fun onResponse(
                    call: Call<PredictionResponse>,
                    response: Response<PredictionResponse>
                ) {
                    uploadFile.delete()
                    setUploadInProgress(false)
                    val prediction = response.body()
                    if (!response.isSuccessful || prediction == null) {
                        Toast.makeText(
                            this@ScanActivity,
                            getString(R.string.server_error_format, response.code()),
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                    openResult(prediction)
                }

                override fun onFailure(
                    call: Call<PredictionResponse>,
                    throwable: Throwable
                ) {
                    uploadFile.delete()
                    setUploadInProgress(false)
                    Toast.makeText(
                        this@ScanActivity,
                        R.string.network_error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    @Throws(IOException::class)
    private fun copyUriToCacheFile(uri: Uri): File {
        val uploadFile = File(cacheDir, "leafguard_upload_${System.currentTimeMillis()}.jpg")
        try {
            contentResolver.openInputStream(uri).use { inputStream ->
                if (inputStream == null) {
                    throw IOException("Unable to open selected image")
                }
                FileOutputStream(uploadFile).use { outputStream ->
                    inputStream.copyTo(outputStream, bufferSize = 8192)
                }
            }
        } catch (exception: IOException) {
            uploadFile.delete()
            throw exception
        }
        return uploadFile
    }

    private fun openResult(prediction: PredictionResponse) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_MODEL_LABEL, prediction.modelLabel)
            putExtra(ResultActivity.EXTRA_DISEASE, prediction.disease)
            putExtra(ResultActivity.EXTRA_CONFIDENCE, prediction.confidence)
            putExtra(ResultActivity.EXTRA_UNCERTAIN, prediction.uncertain)
            putExtra(ResultActivity.EXTRA_GUIDANCE_AVAILABLE, prediction.guidanceAvailable)
            putExtra(ResultActivity.EXTRA_SYMPTOMS, prediction.symptoms)
            putExtra(ResultActivity.EXTRA_TREATMENT, prediction.treatment)
            putExtra(ResultActivity.EXTRA_PREVENTION, prediction.prevention)
        }
        startActivity(intent)
    }

    private fun setUploadInProgress(inProgress: Boolean) {
        progressUpload.visibility = if (inProgress) View.VISIBLE else View.GONE
        buttonDetectDisease.isEnabled = !inProgress && selectedImageUri != null
        findViewById<Button>(R.id.buttonTakePhoto).isEnabled = !inProgress
        findViewById<Button>(R.id.buttonChooseGallery).isEnabled = !inProgress
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SELECTED_IMAGE_URI, selectedImageUri?.toString())
    }

    companion object {
        private const val KEY_SELECTED_IMAGE_URI = "selected_image_uri"
    }
}
```

### 12.9 Expanded File: `ResultActivity.kt` (12 -> 56 lines)

```kotlin
package com.leafguard

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val disease = intent.getStringExtra(EXTRA_DISEASE) ?: getString(R.string.result_unknown)
        val modelLabel = intent.getStringExtra(EXTRA_MODEL_LABEL) ?: getString(R.string.result_unknown)
        val confidence = intent.getFloatExtra(EXTRA_CONFIDENCE, 0f)
        val uncertain = intent.getBooleanExtra(EXTRA_UNCERTAIN, true)
        val guidanceAvailable = intent.getBooleanExtra(EXTRA_GUIDANCE_AVAILABLE, false)
        val symptoms = intent.getStringExtra(EXTRA_SYMPTOMS) ?: getString(R.string.guidance_unavailable)
        val treatment = intent.getStringExtra(EXTRA_TREATMENT) ?: getString(R.string.guidance_unavailable)
        val prevention = intent.getStringExtra(EXTRA_PREVENTION) ?: getString(R.string.guidance_unavailable)
        val confidencePercent = (confidence * 100f).roundToInt()

        findViewById<TextView>(R.id.textResultDisease).text = disease
        findViewById<TextView>(R.id.textResultModelLabel).text = getString(
            R.string.model_label_format,
            modelLabel
        )
        findViewById<TextView>(R.id.textResultConfidence).text = getString(
            R.string.confidence_format,
            confidencePercent
        )
        findViewById<ProgressBar>(R.id.progressResultConfidence).progress = confidencePercent
        findViewById<TextView>(R.id.textResultStatus).text = getString(
            if (uncertain) R.string.result_uncertain else R.string.result_confident
        )
        findViewById<TextView>(R.id.textGuidanceStatus).text = getString(
            if (guidanceAvailable) R.string.guidance_available else R.string.guidance_not_reviewed
        )
        findViewById<TextView>(R.id.textResultSymptoms).text = symptoms
        findViewById<TextView>(R.id.textResultTreatment).text = treatment
        findViewById<TextView>(R.id.textResultPrevention).text = prevention
    }

    companion object {
        const val EXTRA_MODEL_LABEL = "extra_model_label"
        const val EXTRA_DISEASE = "extra_disease"
        const val EXTRA_CONFIDENCE = "extra_confidence"
        const val EXTRA_UNCERTAIN = "extra_uncertain"
        const val EXTRA_GUIDANCE_AVAILABLE = "extra_guidance_available"
        const val EXTRA_SYMPTOMS = "extra_symptoms"
        const val EXTRA_TREATMENT = "extra_treatment"
        const val EXTRA_PREVENTION = "extra_prevention"
    }
}
```

### 12.10 Expanded File: `activity_scan.xml` (60 -> 76 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screen_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="24dp">

        <TextView
            android:id="@+id/textScanTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/scan_title"
            android:textColor="@color/text_primary"
            android:textSize="24sp" />

        <TextView
            android:id="@+id/textScanInstruction"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="@string/scan_instruction"
            android:textColor="@color/text_secondary"
            android:textSize="16sp" />

        <ImageView
            android:id="@+id/imagePreview"
            android:layout_width="match_parent"
            android:layout_height="280dp"
            android:layout_marginTop="20dp"
            android:background="#E8F5E9"
            android:contentDescription="@string/scan_preview_description"
            android:scaleType="centerCrop" />

        <TextView
            android:id="@+id/textImageStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="@string/no_image_selected"
            android:textColor="@color/text_secondary" />

        <Button
            android:id="@+id/buttonTakePhoto"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:text="@string/take_photo" />

        <Button
            android:id="@+id/buttonChooseGallery"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/choose_from_gallery" />

        <Button
            android:id="@+id/buttonDetectDisease"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:enabled="false"
            android:text="@string/detect_disease" />

        <ProgressBar
            android:id="@+id/progressUpload"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="12dp"
            android:contentDescription="@string/upload_progress_description"
            android:visibility="gone" />
    </LinearLayout>
</ScrollView>
```

### 12.11 Expanded File: `activity_result.xml` (25 -> 115 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screen_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="24dp">

        <TextView
            android:id="@+id/textResultTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/result_title"
            android:textColor="@color/text_primary"
            android:textSize="24sp" />

        <TextView
            android:id="@+id/textResultDisease"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/result_unknown"
            android:textColor="@color/leaf_green_dark"
            android:textSize="22sp" />

        <TextView
            android:id="@+id/textResultModelLabel"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:text="@string/model_label_placeholder"
            android:textColor="@color/text_secondary" />

        <TextView
            android:id="@+id/textResultConfidence"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="12dp"
            android:text="@string/confidence_placeholder"
            android:textColor="@color/text_primary" />

        <ProgressBar
            android:id="@+id/progressResultConfidence"
            style="?android:attr/progressBarStyleHorizontal"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:max="100"
            android:progress="0" />

        <TextView
            android:id="@+id/textResultStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="@string/result_uncertain"
            android:textColor="@color/text_secondary" />

        <TextView
            android:id="@+id/textGuidanceStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:text="@string/guidance_not_reviewed"
            android:textColor="@color/text_secondary" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:text="@string/symptoms_heading"
            android:textColor="@color/text_primary"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textResultSymptoms"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/guidance_unavailable"
            android:textColor="@color/text_secondary" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/treatment_heading"
            android:textColor="@color/text_primary"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textResultTreatment"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/guidance_unavailable"
            android:textColor="@color/text_secondary" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/prevention_heading"
            android:textColor="@color/text_primary"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textResultPrevention"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/guidance_unavailable"
            android:textColor="@color/text_secondary" />
    </LinearLayout>
</ScrollView>
```

### 12.12 Expanded File: `strings.xml` (35 -> 55 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">LeafGuard AI</string>

    <string name="home_title">LeafGuard AI</string>
    <string name="home_subtitle">Plant disease detection learning app</string>

    <string name="open_scan">Open Scan</string>
    <string name="open_result">Open Sample Result</string>
    <string name="open_history">Open History</string>
    <string name="open_library">Open Disease Library</string>
    <string name="open_settings">Open Settings</string>

    <string name="scan_title">Scan Leaf</string>
    <string name="result_title">Prediction Result</string>
    <string name="history_title">History</string>
    <string name="library_title">Disease Library</string>
    <string name="settings_title">Settings and About</string>

    <string name="placeholder_history">Saved scan history will be added in Week 07.</string>
    <string name="placeholder_library">The XML disease library will be added in Week 08.</string>
    <string name="placeholder_settings">Course project shell. Settings options will grow in later weeks.</string>

    <string name="scan_instruction">Take a photo or choose an image, then upload it to the Week 04 backend.</string>
    <string name="scan_preview_description">Preview of the selected leaf image</string>
    <string name="take_photo">Take Photo</string>
    <string name="choose_from_gallery">Choose from Gallery</string>
    <string name="no_image_selected">No image selected yet.</string>
    <string name="image_ready_for_upload">Image selected. Ready to detect.</string>
    <string name="camera_permission_denied">Camera permission denied. You can still choose from gallery.</string>
    <string name="camera_cancelled">Camera cancelled. No new image selected.</string>
    <string name="gallery_cancelled">Gallery closed. No new image selected.</string>
    <string name="camera_file_error">Could not prepare a file for the camera.</string>

    <string name="detect_disease">Detect Disease</string>
    <string name="upload_progress_description">Uploading image for prediction</string>
    <string name="select_image_first">Select or capture an image first.</string>
    <string name="image_prepare_error">Could not prepare the selected image for upload.</string>
    <string name="server_error_format">Server rejected the request (HTTP %1$d).</string>
    <string name="network_error">Could not reach the backend. Check the server and emulator URL.</string>

    <string name="result_unknown">Unknown result</string>
    <string name="model_label_placeholder">Model label: unavailable</string>
    <string name="model_label_format">Model label: %1$s</string>
    <string name="confidence_placeholder">Confidence: 0%%</string>
    <string name="confidence_format">Confidence: %1$d%%</string>
    <string name="result_uncertain">Low-confidence result: verify before acting.</string>
    <string name="result_confident">Confidence is above the configured server threshold.</string>
    <string name="guidance_available">Reviewed project guidance is available.</string>
    <string name="guidance_not_reviewed">Detailed project guidance is not reviewed for this label.</string>
    <string name="symptoms_heading">Symptoms</string>
    <string name="treatment_heading">Treatment</string>
    <string name="prevention_heading">Prevention</string>
    <string name="guidance_unavailable">No information available.</string>
</resources>
```

### 12.13 New File: `network_security_config.xml` (7 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="false" />
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="false">10.0.2.2</domain>
    </domain-config>
</network-security-config>
```

### 12.14 Files Week 05 Does Not Rewrite

| Area | Week 05 status | Later owner |
|---|---|---|
| `MainActivity.kt` and home layout | Unchanged navigation shell | UI polish week |
| History Activity/layout | Placeholder | Week 07 |
| Disease Library Activity/layout | Placeholder | Week 08 |
| Settings Activity/layout | Placeholder | Later UI/settings week |
| `colors.xml`, `themes.xml` | Unchanged | UI polish week |
| `file_provider_paths.xml` | Unchanged Week 03 security boundary | Week 03 |
| Backend source | Unchanged, consumed as contract | Week 06 changes inference |
| Room, TFLite, notifications | Absent | Future weeks |

### 12.15 Verify the Exact Week 05 State

```bash
# Backend contract first
cd backend-api
USE_MOCK=true .venv/bin/python -m unittest -v test_api
USE_MOCK=true .venv/bin/uvicorn main:app --reload

# Android build in another terminal
cd android-app-kotlin
./gradlew assembleDebug
```

Behavior checks:

| Check | Expected |
|---|---|
| Gallery/camera selection | Preview and Detect button enabled |
| Active upload | Progress visible; input buttons disabled |
| Backend mock success | Result screen opens with all contract states represented |
| Backend stopped | Friendly network error; UI restored; no crash |
| Invalid server response | HTTP status message; UI restored |
| Repeated attempt | Another upload can start |

Save evidence under `docs/evidence/week-05/`.

---

## 13. Learning-to-Evidence Map

| Concept | Exercise | Build step | Validation proof |
|---|---|---|---|
| Week 03 + Week 04 handoff | 1 | 1 | Boundary explanation |
| Eight-field response | 2 | 3 | Model inspection and Result screen |
| URI-to-cache conversion | 3 | 7 | Camera and gallery uploads |
| Multipart `image` | 3 | 7 | Backend receives 200 |
| Async callbacks | 4 | 8 | Responsive loading state |
| HTTP vs network error | 5 | 8 | Two distinct failure demos |
| Local security | 5 | 4 | Emulator reaches only allowed host |
| Complete integration | 6 | 10 | Milestone demo and evidence |

---

## 14. Week 05 Understanding Checklist

- [ ] I can explain how Weeks 03 and 04 combine in Week 05.
- [ ] I can explain why the emulator uses `10.0.2.2`.
- [ ] I can explain why Retrofit requires a trailing base-URL slash.
- [ ] I can name the multipart field `image`.
- [ ] I can name all eight JSON response fields.
- [ ] I can explain why a content URI is copied to cache.
- [ ] I can distinguish `onResponse` from `onFailure`.
- [ ] I can explain why `enqueue` does not freeze the UI.
- [ ] I can explain the local cleartext exception and production HTTPS rule.
- [ ] I can demonstrate success and backend-unavailable behavior.
- [ ] I can explain why mock success is not model-accuracy evidence.
- [ ] I can identify all 4 new and 7 expanded files.

<!-- NAV_FOOTER_START -->

---

## Week 05 Navigation

| Step | File | Description |
|---:|---|---|
| 1 | [README.md](README.md) | Week overview |
| **2** | **learning-notes.md** - current | Theory and exact source snapshot |
| 3 | [exercises.md](exercises.md) | Guided practice |
| 4 | [build-task.md](build-task.md) | Implementation guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation and evidence |
| 6 | [quiz.md](quiz.md) | Knowledge assessment |
| 7 | [reflection.md](reflection.md) | Reflection and handoff |

[Previous: Week 04](../week-04-fastapi-backend/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 06](../week-06-cloud-ml-model/README.md)