# LeafGuard AI - Comprehensive Viva Questions

## 75+ Viva Questions with Detailed Answers

This document contains potential viva voce questions grouped by topic area with detailed answers to help you prepare.

---

## SECTION 1: ANDROID DEVELOPMENT (15 Questions)

### Q1. What is Android and what are its main components?

**Answer:**
Android is an open-source mobile operating system developed by Google, based on the Linux kernel. The main components are:

1. **Activities:** Represent a single screen with UI (e.g., MainActivity, ResultActivity)
2. **Services:** Background operations without UI (e.g., uploading images)
3. **Broadcast Receivers:** Listen for system-wide broadcasts (e.g., network changes)
4. **Content Providers:** Manage shared data between applications
5. **Intents:** Messaging objects to request actions from other components
6. **Fragments:** Reusable UI portions within activities

In LeafGuard AI, we primarily use Activities and Fragments for UI, Intents for navigation, and implicit intents for camera access.

---

### Q2. Explain the Activity lifecycle in Android.

**Answer:**
The Activity lifecycle consists of callback methods called as the activity transitions through different states:

1. **onCreate():** Activity is created, initialize UI components
2. **onStart():** Activity becomes visible to user
3. **onResume():** Activity is in foreground, user can interact
4. **onPause():** Activity losing focus, save critical data
5. **onStop():** Activity no longer visible, release resources
6. **onDestroy():** Activity is destroyed, final cleanup

**In LeafGuard AI:**
- onCreate(): Initialize ViewModels, set up RecyclerView, bind UI
- onResume(): Refresh scan history from database
- onPause(): Save any user input
- onDestroy(): Cancel ongoing coroutines

**Important:** Configuration changes (screen rotation) destroy and recreate activity, so we use ViewModel to preserve data.

---

### Q3. What is the difference between onCreate() and onStart()?

**Answer:**

**onCreate():**
- Called only once when activity is created
- Used for one-time initialization
- Set up UI, initialize ViewModels, set click listeners
- Example: `binding = ActivityMainBinding.inflate(layoutInflater)`

**onStart():**
- Called every time activity becomes visible
- Can be called multiple times (after onStop())
- Used to register listeners, start animations
- Example: Start listening to database changes

**In LeafGuard AI:** We initialize Room database and Retrofit in onCreate() because they're needed only once, but we refresh the scan history list in onStart() to reflect any changes.

---

### Q4. What is an Intent? Explain explicit and implicit intents with examples from your project.

**Answer:**

**Intent** is a messaging object used to request an action from another component.

**Explicit Intent:** Specifies the exact component to start
```kotlin
// Navigation from Login to Home
val intent = Intent(this, HomeActivity::class.java)
intent.putExtra("USER_ID", userId)
startActivity(intent)
```

**Implicit Intent:** Declares a general action, system chooses appropriate app
```kotlin
// Open camera for image capture
val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)

// Open gallery for image selection
val galleryIntent = Intent(Intent.ACTION_PICK)
galleryIntent.type = "image/*"
startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
```

**In LeafGuard AI:** We use explicit intents for navigation between our screens and implicit intents for camera and gallery access.

---

### Q5. What is the difference between Activity and Fragment?

**Answer:**

| Aspect | Activity | Fragment |
|--------|----------|----------|
| Definition | Full screen UI component | Modular portion of UI within Activity |
| Lifecycle | Independent lifecycle | Dependent on host Activity |
| Context | Has its own context | Uses Activity's context |
| Declaration | In AndroidManifest.xml | Not in manifest |
| Navigation | startActivity() | FragmentTransaction |
| Example | LoginActivity | HomeFragment in MainActivity |

**In LeafGuard AI:**
- MainActivity hosts three fragments: HomeFragment, ScanFragment, HistoryFragment
- Bottom navigation switches between fragments
- Fragments share the MainActivity's ViewModel
- Benefits: Reusable UI, efficient navigation, better tablet support

---

### Q6. What is AndroidManifest.xml and what are its important components?

**Answer:**
AndroidManifest.xml is a configuration file that provides essential information about the app to the Android system.

**Important components:**

```xml
<manifest>
    <!-- App package name -->
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

    <application
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.LeafGuardAI">

        <!-- Declare activities -->
        <activity android:name=".ui.MainActivity"
                  android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>

        <!-- FileProvider for camera images -->
        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="com.leafguard.ai.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths"/>
        </provider>
    </application>
</manifest>
```

**In LeafGuard AI:** We declare all activities, permissions (camera, internet, storage), and FileProvider for secure file sharing.

---

### Q7. Explain View Binding in Android. How is it used in your project?

**Answer:**
View Binding generates binding classes for each XML layout, providing type-safe access to views without findViewById().

**Benefits:**
- Type safety (compile-time checking)
- Null safety (only views in layout are accessible)
- Faster than findViewById()
- No need for synthetic imports

**Implementation in LeafGuard AI:**

```kotlin
// In build.gradle
buildFeatures {
    viewBinding = true
}

// In Activity
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Type-safe view access
        binding.captureButton.setOnClickListener {
            openCamera()
        }

        binding.resultText.text = "Disease: ${scanResult.diseaseName}"
    }
}
```

**Before View Binding (old way):**
```kotlin
val button = findViewById<Button>(R.id.captureButton) // Runtime, nullable
```

**With View Binding:**
```kotlin
binding.captureButton // Compile-time, non-null
```

---

### Q8. What is the purpose of R.java file in Android?

**Answer:**
R.java is an auto-generated file containing unique integer IDs for all resources in the project.

**Resource types:**
- R.layout (XML layouts)
- R.id (view IDs)
- R.drawable (images, icons)
- R.string (text strings)
- R.color (color values)
- R.dimen (dimensions)

**Example in LeafGuard AI:**
```kotlin
setContentView(R.layout.activity_scan)  // R.layout.activity_scan = unique ID
val title = getString(R.string.app_name)  // R.string.app_name = unique ID
binding.imageView.setImageResource(R.drawable.ic_camera)
```

**Important:** Never edit R.java manually. It's regenerated when resources change. If R.java has errors, clean and rebuild the project.

---

### Q9. Explain different types of layouts in Android. Which ones did you use?

**Answer:**

**1. LinearLayout:** Arranges views in single row or column
```xml
<LinearLayout
    android:orientation="vertical">
    <TextView android:text="Username"/>
    <EditText android:id="@+id/usernameInput"/>
</LinearLayout>
```
**Used in:** Login form, Treatment list

**2. RelativeLayout:** Positions views relative to each other or parent
```xml
<RelativeLayout>
    <Button android:id="@+id/button"
            android:layout_alignParentBottom="true"/>
</RelativeLayout>
```
**Used in:** Splash screen, About screen

**3. ConstraintLayout:** Flexible positioning with constraints
```xml
<ConstraintLayout>
    <ImageView
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"/>
</ConstraintLayout>
```
**Used in:** Main activity, Result display, Scan screen

**4. FrameLayout:** Stack views on top of each other
**Used in:** Fragment container, Loading overlays

**5. CardView:** Material Design card with elevation
**Used in:** History items, Disease info cards

**Why ConstraintLayout is preferred:** Flat view hierarchy, better performance, flexible positioning, responsive design.

---

### Q10. What is the difference between margin and padding?

**Answer:**

**Padding:** Space inside the view, between content and border
**Margin:** Space outside the view, between view and its neighbors

```xml
<Button
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Scan"
    android:padding="16dp"        <!-- Space inside button -->
    android:layout_margin="8dp"   <!-- Space outside button -->
    android:background="@color/primary"/>
```

**Visual representation:**
```
┌─────────────────────────────────────────┐
│  Margin (outside, transparent)          │
│  ┌───────────────────────────────────┐  │
│  │ Border                            │  │
│  │  ┌─────────────────────────────┐  │  │
│  │  │ Padding (inside, background)│  │  │
│  │  │  ┌───────────────────────┐  │  │  │
│  │  │  │ Content (text, image) │  │  │  │
│  │  │  └───────────────────────┘  │  │  │
│  │  └─────────────────────────────┘  │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
```

**In LeafGuard AI:** Used padding for button text spacing, margin for spacing between cards in history list.

---

### Q11. What is a RecyclerView and why is it better than ListView?

**Answer:**

**RecyclerView** is an advanced widget for displaying large lists efficiently by recycling views.

**Advantages over ListView:**

1. **ViewHolder Pattern:** Mandatory, improves performance
2. **Layout Flexibility:** Supports Linear, Grid, Staggered layouts
3. **Item Animations:** Built-in add/remove/move animations
4. **Item Decorations:** Dividers, spacing without modifying items
5. **DiffUtil:** Efficient list updates
6. **Better Performance:** Smooth scrolling for large datasets

**Implementation in LeafGuard AI:**

```kotlin
// Adapter
class HistoryAdapter(private val onItemClick: (ScanResult) -> Unit) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var scanResults = listOf<ScanResult>()

    class ViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = scanResults[position]
        holder.binding.apply {
            diseaseNameText.text = result.diseaseName
            confidenceText.text = "${(result.confidence * 100).toInt()}%"
            timestampText.text = formatDate(result.timestamp)
            Glide.with(root.context)
                .load(result.imagePath)
                .into(thumbnailImage)

            root.setOnClickListener { onItemClick(result) }
        }
    }

    override fun getItemCount() = scanResults.size

    fun updateData(newResults: List<ScanResult>) {
        val diffCallback = ScanResultDiffCallback(scanResults, newResults)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        scanResults = newResults
        diffResult.dispatchUpdatesTo(this)
    }
}

// In Activity/Fragment
binding.recyclerView.apply {
    layoutManager = LinearLayoutManager(context)
    adapter = historyAdapter
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
}
```

**Used for:** Scan history list, Treatment recommendations list, Disease info list

---

### Q12. What are runtime permissions? How did you implement them in your project?

**Answer:**
Runtime permissions (introduced in Android 6.0) require user approval at runtime for dangerous permissions like Camera, Location, Storage.

**Implementation in LeafGuard AI:**

```kotlin
class MainActivity : AppCompatActivity() {

    private val CAMERA_PERMISSION_CODE = 100

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            // Should show rationale?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.CAMERA)) {

                // Show explanation dialog
                showPermissionRationaleDialog()
            } else {
                // Request permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }
        } else {
            // Permission already granted
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    openCamera()
                } else {
                    // Permission denied
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.CAMERA)) {
                        // User denied but can ask again
                        Toast.makeText(this,
                            "Camera permission required for scanning",
                            Toast.LENGTH_LONG).show()
                    } else {
                        // User denied with "Don't ask again"
                        showSettingsDialog()
                    }
                }
            }
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("Camera permission is required. Please enable in Settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
```

**Permissions used:**
- `CAMERA`: Capture plant leaf images
- `READ_EXTERNAL_STORAGE`: Select images from gallery
- `INTERNET`: API communication
- `WRITE_EXTERNAL_STORAGE`: Save captured images (API < 29)

---

### Q13. What is the difference between Serializable and Parcelable?

**Answer:**

Both are used to pass objects between activities/fragments, but Parcelable is faster.

| Aspect | Serializable | Parcelable |
|--------|--------------|------------|
| Performance | Slower (uses reflection) | Faster (optimized for Android) |
| Ease of use | Easy (just implement interface) | More code required |
| Memory | More memory usage | Less memory usage |
| Recommended | For simple objects | For Android (always prefer) |

**Example in LeafGuard AI:**

```kotlin
// Parcelable implementation
@Parcelize
data class ScanResult(
    val id: Int,
    val imagePath: String,
    val diseaseName: String,
    val confidence: Float,
    val timestamp: Long
) : Parcelable

// Passing between activities
val intent = Intent(this, ResultActivity::class.java)
intent.putExtra("SCAN_RESULT", scanResult)
startActivity(intent)

// Receiving in ResultActivity
val scanResult = intent.getParcelableExtra<ScanResult>("SCAN_RESULT")
```

**With @Parcelize annotation (Kotlin):** Parcelable implementation is auto-generated, no boilerplate code needed.

---

### Q14. What is the purpose of Gradle in Android development?

**Answer:**
Gradle is a build automation tool used to compile, package, and deploy Android applications.

**Two Gradle files:**

**1. Project-level build.gradle:**
```gradle
buildscript {
    ext.kotlin_version = "1.8.0"
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:8.0.0"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}
```

**2. App-level build.gradle:**
```gradle
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'
    id 'kotlin-parcelize'
}

android {
    namespace 'com.leafguard.ai'
    compileSdk 34

    defaultConfig {
        applicationId "com.leafguard.ai"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'),
                          'proguard-rules.pro'
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX
    implementation 'androidx.core:core-ktx:1.10.1'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'

    // Architecture Components
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.1'

    // Room Database
    implementation 'androidx.room:room-runtime:2.5.2'
    implementation 'androidx.room:room-ktx:2.5.2'
    kapt 'androidx.room:room-compiler:2.5.2'

    // Retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4'

    // Glide
    implementation 'com.github.bumptech.glide:glide:4.14.0'

    // TensorFlow Lite
    implementation 'org.tensorflow:tensorflow-lite:2.11.0'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

**Gradle tasks:**
- `./gradlew build`: Build project
- `./gradlew assembleDebug`: Create debug APK
- `./gradlew assembleRelease`: Create release APK
- `./gradlew clean`: Clean build directory

---

### Q15. Explain the MVVM architecture pattern used in your project.

**Answer:**
MVVM (Model-View-ViewModel) separates UI logic from business logic, making code testable and maintainable.

**Components:**

**1. Model:** Data layer (Room database, API responses)
```kotlin
@Entity(tableName = "scan_results")
data class ScanResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String,
    val diseaseName: String,
    val confidence: Float,
    val timestamp: Long
)
```

**2. View:** UI layer (Activity, Fragment)
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private val viewModel: ScanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        setupClickListeners()
    }

    private fun observeViewModel() {
        viewModel.scanResult.observe(this) { result ->
            displayResult(result)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }
}
```

**3. ViewModel:** Business logic, exposes data via LiveData
```kotlin
class ScanViewModel : ViewModel() {  // general example — LeafGuard AI itself has no ViewModels
    private val repository = ScanRepository()

    private val _scanResult = MutableLiveData<ScanResult>()
    val scanResult: LiveData<ScanResult> = _scanResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun uploadAndDetectDisease(imageFile: File) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = repository.uploadImage(imageFile)
                _scanResult.value = result
                saveToDatabase(result)
            } catch (e: Exception) {
                _error.value = "Detection failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun saveToDatabase(result: ScanResult) {
        withContext(Dispatchers.IO) {
            repository.insertScanResult(result)
        }
    }
}
```

**Benefits in LeafGuard AI:**
- ViewModel survives configuration changes (screen rotation)
- Clear separation of concerns
- Easy to test (can test ViewModel without UI)
- LiveData ensures UI always reflects latest data
- Lifecycle-aware (no memory leaks)

---

## SECTION 2: NETWORKING (10 Questions)

### Q16. What is Retrofit? Why did you use it instead of HttpURLConnection?

**Answer:**
Retrofit is a type-safe HTTP client for Android developed by Square, simplifying REST API consumption.

**Advantages over HttpURLConnection:**

1. **Less Boilerplate:** Annotations instead of manual request building
2. **Type Safety:** Compile-time validation of API endpoints
3. **Automatic Serialization:** JSON ↔ Objects (using Gson)
4. **Coroutines Support:** Easy async operations
5. **Interceptors:** Easy logging, authentication
6. **Error Handling:** Built-in exception handling

**Implementation in LeafGuard AI:**

```kotlin
// API Service Interface
interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>

    @Multipart
    @POST("detect")
    suspend fun detectDisease(
        @Part image: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Response<DetectionResponse>

    @GET("disease/{name}")
    suspend fun getDiseaseInfo(@Path("name") diseaseName: String): Response<DiseaseInfo>
}

// Retrofit Client Setup
object RetrofitClient {
    private const val BASE_URL = "https://leafguard-api.herokuapp.com/api/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

// Usage in Repository
class ScanRepository {
    suspend fun uploadImage(file: File): ScanResult {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val response = RetrofitClient.apiService.detectDisease(imagePart, "Bearer $token")

        if (response.isSuccessful) {
            return response.body()!!.toScanResult()
        } else {
            throw IOException("Detection failed: ${response.code()}")
        }
    }
}
```

**Why Retrofit?**
- Industry standard
- Reduces network code by 80%
- Excellent error handling
- Works seamlessly with Coroutines
- Easy to test with MockWebServer

---

### Q17. Explain different HTTP methods. Which ones did you use?

**Answer:**

**HTTP Methods used in LeafGuard AI:**

**1. GET - Retrieve data (no body)**
```kotlin
@GET("disease/{name}")
suspend fun getDiseaseInfo(@Path("name") diseaseName: String): Response<DiseaseInfo>

// Usage
val response = apiService.getDiseaseInfo("bacterial_blight")
```
**Used for:** Fetching disease information, user profile

**2. POST - Send data to server (with body)**
```kotlin
@POST("auth/register")
suspend fun register(@Body user: RegisterRequest): Response<RegisterResponse>

// Usage
val request = RegisterRequest(name, email, password)
val response = apiService.register(request)
```
**Used for:** User registration, login, image upload for detection

**3. PUT - Update existing resource**
```kotlin
@PUT("user/profile")
suspend fun updateProfile(@Body profile: UserProfile): Response<UserProfile>
```
**Used for:** Updating user profile information

**4. DELETE - Delete resource**
```kotlin
@DELETE("history/{id}")
suspend fun deleteHistoryItem(@Path("id") id: Int): Response<Unit>
```
**Used for:** Deleting scan history from server (optional feature)

**Not used but important:**
- **PATCH:** Partial update
- **HEAD:** Get headers only
- **OPTIONS:** Get supported methods

**HTTP Status Codes handled:**
- 200 OK: Successful request
- 201 Created: Resource created
- 400 Bad Request: Invalid data
- 401 Unauthorized: Invalid credentials
- 404 Not Found: Resource doesn't exist
- 500 Internal Server Error: Server error

---

### Q18. What is JSON? How do you parse JSON in Android?

**Answer:**
JSON (JavaScript Object Notation) is a lightweight data format for data exchange.

**Example API Response:**
```json
{
  "success": true,
  "data": {
    "disease_name": "Bacterial Blight",
    "confidence": 0.87,
    "symptoms": ["Yellow spots on leaves", "Leaf wilting"],
    "treatment": {
      "chemical": "Streptomycin sulfate",
      "organic": "Neem oil spray",
      "preventive": ["Crop rotation", "Proper drainage"]
    }
  },
  "timestamp": 1635789123
}
```

**Parsing in LeafGuard AI using Gson:**

```kotlin
// Data classes matching JSON structure
data class DetectionResponse(
    val success: Boolean,
    val data: DetectionData,
    val timestamp: Long
)

data class DetectionData(
    @SerializedName("disease_name") val diseaseName: String,
    val confidence: Float,
    val symptoms: List<String>,
    val treatment: Treatment
)

data class Treatment(
    val chemical: String,
    val organic: String,
    val preventive: List<String>
)

// Retrofit automatically parses JSON to objects
val response = apiService.detectDisease(imagePart)
if (response.isSuccessful) {
    val detectionData = response.body()?.data
    println("Disease: ${detectionData?.diseaseName}")
    println("Confidence: ${detectionData?.confidence}")
}
```

**Manual parsing (without Retrofit):**
```kotlin
val jsonString = """{"disease_name":"Bacterial Blight","confidence":0.87}"""
val jsonObject = JSONObject(jsonString)
val diseaseName = jsonObject.getString("disease_name")
val confidence = jsonObject.getDouble("confidence")
```

**@SerializedName annotation:** Maps JSON key to different Kotlin property name
```kotlin
@SerializedName("disease_name") val diseaseName: String
// JSON: "disease_name" → Kotlin: diseaseName
```

---

### Q19. How do you handle network errors in your application?

**Answer:**

**Error handling strategy in LeafGuard AI:**

```kotlin
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

class ScanRepository {
    suspend fun uploadImage(file: File): NetworkResult<ScanResult> {
        return try {
            // Check internet connectivity first
            if (!NetworkUtils.isInternetAvailable()) {
                return NetworkResult.Error("No internet connection")
            }

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val response = withContext(Dispatchers.IO) {
                apiService.detectDisease(imagePart)
            }

            when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body != null) {
                        NetworkResult.Success(body.toScanResult())
                    } else {
                        NetworkResult.Error("Empty response from server")
                    }
                }
                response.code() == 401 -> {
                    NetworkResult.Error("Unauthorized. Please login again.", 401)
                }
                response.code() == 404 -> {
                    NetworkResult.Error("API endpoint not found.", 404)
                }
                response.code() >= 500 -> {
                    NetworkResult.Error("Server error. Please try again later.", response.code())
                }
                else -> {
                    NetworkResult.Error("Request failed: ${response.message()}", response.code())
                }
            }
        } catch (e: SocketTimeoutException) {
            NetworkResult.Error("Connection timeout. Please check your internet.")
        } catch (e: IOException) {
            NetworkResult.Error("Network error: ${e.localizedMessage}")
        } catch (e: HttpException) {
            NetworkResult.Error("HTTP error: ${e.code()}")
        } catch (e: Exception) {
            NetworkResult.Error("Unexpected error: ${e.localizedMessage}")
        }
    }
}

// ViewModel
class ScanViewModel : ViewModel() {  // general example — LeafGuard AI itself has no ViewModels
    private val _scanState = MutableLiveData<NetworkResult<ScanResult>>()
    val scanState: LiveData<NetworkResult<ScanResult>> = _scanState

    fun uploadImage(file: File) {
        viewModelScope.launch {
            _scanState.value = NetworkResult.Loading
            _scanState.value = repository.uploadImage(file)
        }
    }
}

// Activity
viewModel.scanState.observe(this) { result ->
    when (result) {
        is NetworkResult.Loading -> {
            binding.progressBar.isVisible = true
            binding.uploadButton.isEnabled = false
        }
        is NetworkResult.Success -> {
            binding.progressBar.isVisible = false
            binding.uploadButton.isEnabled = true
            displayResult(result.data)
        }
        is NetworkResult.Error -> {
            binding.progressBar.isVisible = false
            binding.uploadButton.isEnabled = true
            showErrorDialog(result.message)

            // Specific handling for 401
            if (result.code == 401) {
                navigateToLogin()
            }
        }
    }
}
```

**Network connectivity check:**
```kotlin
object NetworkUtils {
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
```

**Retry mechanism:**
```kotlin
suspend fun <T> retryIO(
    times: Int = 3,
    initialDelay: Long = 1000,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
            // Wait before retry
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong()
        }
    }
    return block() // Last attempt
}
```

---

### Q20. What is the purpose of OkHttp interceptors? Give an example.

**Answer:**
Interceptors intercept HTTP requests/responses to modify or inspect them.

**Use cases:**
1. **Logging:** Log requests and responses
2. **Authentication:** Add auth tokens to headers
3. **Caching:** Implement custom caching
4. **Error handling:** Centralized error handling
5. **Headers:** Add common headers to all requests

**Implementation in LeafGuard AI:**

```kotlin
// Logging Interceptor
val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }
}

// Authentication Interceptor
class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = tokenProvider()
        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}

// Custom Error Interceptor
class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        when (response.code) {
            401 -> {
                // Token expired, logout user
                EventBus.post(UnauthorizedEvent())
            }
            503 -> {
                // Service unavailable
                throw ServiceUnavailableException("Server is under maintenance")
            }
        }

        return response
    }
}

// OkHttp Client with interceptors
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor(AuthInterceptor { SessionManager.getToken() })
    .addInterceptor(ErrorInterceptor())
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()
```

**Benefits:**
- Centralized request/response handling
- No need to manually add headers in every API call
- Easy debugging with logging
- Clean code separation

---

## SECTION 3: BACKEND / FASTAPI (10 Questions)

### Q21. What is FastAPI and why was it chosen for LeafGuard AI's backend?

**Answer:**
FastAPI is a modern, high-performance Python web framework for building APIs. It is built on top of **Starlette** (for ASGI web serving) and **Pydantic** (for data validation and serialisation).

**Why FastAPI for LeafGuard AI:**

1. **Performance:** Benchmarks comparable to NodeJS and Go — far faster than Flask/Django for IO-bound work
2. **Automatic documentation:** Swagger UI available at `/docs`, ReDoc at `/redoc` — no extra work needed
3. **Type hints:** Python 3.6+ type annotations drive automatic request/response validation
4. **Async support:** `async def` endpoints handle concurrent requests without blocking
5. **Built-in file upload:** `UploadFile` type handles multipart/form-data natively
6. **Easy integration:** Works seamlessly with TensorFlow/Keras model loading

**Basic FastAPI application structure:**

```python
from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI(
    title="LeafGuard AI API",
    description="Plant disease detection via ML model inference",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/health")
async def health():
    return {"status": "ok", "model_loaded": model is not None}

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    # image → preprocess → model.predict → response
    ...
```

**Comparison with alternatives:**
- Flask: No built-in async, no automatic validation, more boilerplate
- Django: Too heavy — includes ORM, admin, template engine (unneeded for a pure API)
- FastAPI: Right-sized, modern, production-ready with minimal setup

---

### Q22. Explain how FastAPI handles file uploads. How does it work in LeafGuard AI?

**Answer:**
FastAPI uses the `UploadFile` and `File` types to receive multipart/form-data requests.

**How UploadFile works:**

```python
from fastapi import UploadFile, File, HTTPException
from PIL import Image
import io

@app.post("/predict")
async def predict_disease(file: UploadFile = File(...)):
    # 1. Validate file type
    if file.content_type not in ["image/jpeg", "image/png", "image/webp"]:
        raise HTTPException(
            status_code=400,
            detail=f"Invalid file type: {file.content_type}. Use JPEG or PNG."
        )

    # 2. Read raw bytes from the upload
    contents = await file.read()

    # 3. Validate file size (max 10 MB)
    if len(contents) > 10 * 1024 * 1024:
        raise HTTPException(status_code=413, detail="File too large. Maximum 10 MB.")

    # 4. Convert bytes to PIL Image for preprocessing
    try:
        image = Image.open(io.BytesIO(contents)).convert("RGB")
    except Exception:
        raise HTTPException(status_code=400, detail="Cannot decode image file.")

    # 5. Preprocess and run inference
    result = run_inference(image)
    return result
```

**Key UploadFile attributes:**
- `file.filename` — original filename sent by the client
- `file.content_type` — MIME type (e.g., `image/jpeg`)
- `await file.read()` — reads the entire file as bytes
- `await file.seek(0)` — resets read pointer to start

**Android side (Retrofit):**

```java
// Create multipart request
RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("image/*"));
MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);

// Send via Retrofit
Call<PredictionResponse> call = apiService.predict(imagePart);
```

**Why multipart/form-data?** Binary files cannot be sent as plain JSON. Multipart form data encodes binary data with boundaries between fields, allowing mixed text and binary content in one request.

---

### Q23. What are Pydantic models in FastAPI? How do you use them for request/response?

**Answer:**
Pydantic is a data validation library that uses Python type annotations. FastAPI uses Pydantic models to define and validate JSON request bodies and response shapes.

**Request model (validates incoming data):**

```python
from pydantic import BaseModel, Field
from typing import Optional, List

class PredictionRequest(BaseModel):
    image_url: Optional[str] = None
    model_version: str = Field(default="v1", description="Model version to use")
    confidence_threshold: float = Field(default=0.5, ge=0.0, le=1.0)
```

**Response model (shapes outgoing data):**

```python
class DiseaseInfo(BaseModel):
    name: str
    confidence: float
    symptoms: List[str]
    treatment: str
    prevention: List[str]
    is_healthy: bool

class PredictionResponse(BaseModel):
    success: bool
    prediction: DiseaseInfo
    processing_time_ms: float
    model_version: str = "v1"

@app.post("/predict", response_model=PredictionResponse)
async def predict(file: UploadFile = File(...)):
    # FastAPI automatically validates the return value matches PredictionResponse
    return PredictionResponse(
        success=True,
        prediction=DiseaseInfo(
            name="Tomato Early Blight",
            confidence=0.91,
            symptoms=["Brown concentric-ring lesions", "Yellow halos", "Leaf drop"],
            treatment="Prune infected leaves. Apply copper-based fungicide.",
            prevention=["Avoid overhead watering", "Rotate crops"],
            is_healthy=False
        ),
        processing_time_ms=45.2
    )
```

**Benefits:**
- FastAPI validates request data automatically — returns 422 if schema mismatch
- Response model acts as documentation AND enforces correct output shape
- Android Retrofit + Gson parses the response into matching Java/Kotlin data classes

---

### Q24. Explain HTTP status codes. Which ones does your API return and when?

**Answer:**
HTTP status codes are 3-digit numbers grouped into categories:

| Range | Category | Meaning |
|-------|----------|---------|
| 2xx | Success | Request completed successfully |
| 4xx | Client Error | Request is wrong (bad input, not found) |
| 5xx | Server Error | Server failed to process a valid request |

**Status codes used in LeafGuard AI backend:**

```python
from fastapi import HTTPException
from starlette.status import (
    HTTP_200_OK, HTTP_400_BAD_REQUEST,
    HTTP_404_NOT_FOUND, HTTP_413_REQUEST_ENTITY_TOO_LARGE,
    HTTP_500_INTERNAL_SERVER_ERROR, HTTP_503_SERVICE_UNAVAILABLE
)

# 200 OK — Successful prediction
@app.post("/predict", status_code=200)
async def predict(file: UploadFile = File(...)):
    ...
    return result  # 200 by default

# 400 Bad Request — Invalid file type or corrupt image
raise HTTPException(status_code=400, detail="Invalid image format")

# 404 Not Found — Disease name not in knowledge base
raise HTTPException(status_code=404, detail=f"Disease '{name}' not found")

# 413 Payload Too Large — Image exceeds 10 MB limit
raise HTTPException(status_code=413, detail="Image too large. Maximum 10 MB.")

# 422 Unprocessable Entity — FastAPI returns this automatically for schema validation failures
# (e.g., missing required field, wrong type)

# 500 Internal Server Error — Unexpected exception during inference
@app.exception_handler(Exception)
async def generic_exception_handler(request, exc):
    return JSONResponse(status_code=500, content={"detail": "Internal server error"})

# 503 Service Unavailable — Model not loaded yet
if model is None:
    raise HTTPException(status_code=503, detail="Model not loaded. Please wait.")
```

**Android handling:**

```java
if (response.isSuccessful()) {
    // 200 range
} else if (response.code() == 400) {
    showError("Invalid image. Please choose a clear plant photo.");
} else if (response.code() >= 500) {
    showError("Server error. Switching to offline mode.");
}
```

---

### Q25. What is CORS and why did you configure it in the backend?

**Answer:**
**CORS = Cross-Origin Resource Sharing** is a browser security mechanism that restricts web pages from making requests to a different domain than the one that served the page.

**Why it matters for LeafGuard AI:**
- The backend runs on `http://192.168.1.x:8000` (local) or a cloud server
- The Android app directly makes HTTP requests — **Android is not a browser**, so CORS does NOT restrict Android apps
- However, if you add a web dashboard or test via browser-based tools (Postman web, Swagger UI), CORS must be configured

**CORS configuration in LeafGuard AI:**

```python
from fastapi.middleware.cors import CORSMiddleware

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],                # Allow all origins (development)
    # Production: allow_origins=["https://leafguard-web.com"]
    allow_credentials=True,
    allow_methods=["GET", "POST", "OPTIONS"],
    allow_headers=["Authorization", "Content-Type"],
)
```

**CORS preflight flow:**
1. Browser sends `OPTIONS` request first (preflight)
2. Server responds with `Access-Control-Allow-Origin` header
3. If allowed, browser sends the actual request

**For Android apps specifically:**
- Android's `HttpURLConnection` and `OkHttp` do NOT enforce CORS
- CORS is only enforced by browser JavaScript
- Even so, configuring CORS is good practice for future web clients

---

### Q26. How does the ML model get loaded and used in FastAPI? Explain the inference pipeline.

**Answer:**
The ML model is loaded once at startup (not per request) and reused for all predictions. This avoids the expensive model-loading cost on every API call.

**Model loading at startup:**

```python
import tensorflow as tf
import numpy as np
from PIL import Image
import io

# Global model reference — loaded once
model = None
class_names = []

@app.on_event("startup")
async def load_model():
    global model, class_names
    try:
        model = tf.keras.models.load_model("model/plant_disease_model.h5")
        with open("model/labels.txt", "r") as f:
            class_names = [line.strip() for line in f.readlines()]
        print(f"Model loaded successfully. Classes: {len(class_names)}")
    except Exception as e:
        print(f"Warning: Model failed to load: {e}")
```

**Complete inference pipeline:**

```python
def preprocess_image(image: Image.Image, target_size=(224, 224)) -> np.ndarray:
    """Step 1: Resize, normalize, and add batch dimension."""
    image = image.resize(target_size)           # Resize to model input size
    img_array = np.array(image, dtype=np.float32)  # Convert to NumPy array
    img_array = img_array / 255.0              # Normalize: [0, 255] → [0.0, 1.0]
    img_array = np.expand_dims(img_array, axis=0)  # Add batch dim: (H,W,3) → (1,H,W,3)
    return img_array

def run_inference(image: Image.Image) -> dict:
    """Step 2: Run model, decode output array into human-readable result."""
    if model is None:
        raise HTTPException(status_code=503, detail="Model not loaded")

    # Preprocess
    input_tensor = preprocess_image(image)

    # Inference
    predictions = model.predict(input_tensor)  # Shape: (1, num_classes)
    predictions = predictions[0]               # Remove batch dim: (num_classes,)

    # Decode
    top_idx = int(np.argmax(predictions))
    confidence = float(predictions[top_idx])
    disease_name = class_names[top_idx]

    return {
        "disease": disease_name,
        "confidence": confidence,
        "all_predictions": {
            class_names[i]: float(predictions[i])
            for i in np.argsort(predictions)[::-1][:5]  # Top 5
        }
    }
```

**Why global model variable?** FastAPI is a long-running process. Loading a 50-100 MB model on every request would take 2-5 seconds each time. Loading once at startup and keeping it in memory reduces inference time to milliseconds.

---

### Q27. Explain async programming in Python. How does FastAPI use it?

**Answer:**
**Async programming** allows a program to start tasks and switch to other work while waiting for slow operations (network, disk I/O), rather than blocking.

**Synchronous (blocking):**
```python
# Waits 2 seconds doing nothing useful
def sync_endpoint():
    data = read_from_database()  # Blocks for 2 seconds
    return process(data)
```

**Asynchronous (non-blocking):**
```python
# While waiting for DB, handles other requests
async def async_endpoint():
    data = await read_from_database()  # Yields control while waiting
    return process(data)
```

**FastAPI and async/await:**

```python
import asyncio
import aiofiles

# Async endpoint — can handle thousands of concurrent requests
@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    contents = await file.read()          # Async file read
    image = Image.open(io.BytesIO(contents))
    
    # CPU-bound operations (model inference) should run in thread pool
    # to avoid blocking the event loop
    import asyncio
    loop = asyncio.get_event_loop()
    result = await loop.run_in_executor(None, run_inference, image)
    
    return result

# Async file saving
@app.post("/save-scan")
async def save_scan(file: UploadFile = File(...)):
    async with aiofiles.open(f"uploads/{file.filename}", "wb") as out_file:
        content = await file.read()
        await out_file.write(content)
    return {"saved": file.filename}
```

**Key concepts:**
- `async def` — defines a coroutine function
- `await` — suspends current coroutine until the awaited task completes
- **Event loop** — runs coroutines and switches between them
- **ASGI** — Asynchronous Server Gateway Interface — what FastAPI runs on (via Uvicorn)

**In LeafGuard AI:** File reads from the Android app are async. Model inference (CPU-bound) runs in a thread executor so it doesn't block other requests.

---

### Q28. How do you validate and handle errors in FastAPI?

**Answer:**
FastAPI provides multiple levels of error handling:

**1. Pydantic automatic validation (422 errors):**
```python
class PredictRequest(BaseModel):
    confidence_threshold: float = Field(ge=0.0, le=1.0)  # Must be 0.0–1.0
    image_size: int = Field(gt=0, le=4096)  # Must be positive, ≤ 4096

# FastAPI automatically returns 422 if validation fails — no manual code needed
```

**2. HTTPException for business logic errors:**
```python
from fastapi import HTTPException

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    # Manual validation
    ALLOWED_TYPES = {"image/jpeg", "image/png", "image/webp"}
    if file.content_type not in ALLOWED_TYPES:
        raise HTTPException(
            status_code=400,
            detail={
                "error": "INVALID_FILE_TYPE",
                "message": f"Expected JPEG/PNG, got {file.content_type}",
                "allowed_types": list(ALLOWED_TYPES)
            }
        )
```

**3. Custom exception handlers (global):**
```python
from fastapi.responses import JSONResponse

@app.exception_handler(ValueError)
async def value_error_handler(request, exc):
    return JSONResponse(
        status_code=400,
        content={"error": "VALIDATION_ERROR", "detail": str(exc)}
    )

@app.exception_handler(Exception)
async def generic_handler(request, exc):
    # Log the full traceback
    import traceback
    traceback.print_exc()
    return JSONResponse(
        status_code=500,
        content={"error": "INTERNAL_ERROR", "detail": "An unexpected error occurred"}
    )
```

**Error response format used in LeafGuard AI:**
```json
{
    "success": false,
    "error": {
        "code": "INVALID_FILE_TYPE",
        "message": "Expected image/jpeg or image/png",
        "timestamp": "2025-01-01T12:00:00Z"
    }
}
```

**Android error handling:**
```java
if (!response.isSuccessful()) {
    String errorBody = response.errorBody().string();
    ErrorResponse error = gson.fromJson(errorBody, ErrorResponse.class);
    showErrorDialog(error.getMessage());
}
```

---

### Q29. What is uvicorn and how do you run a FastAPI application?

**Answer:**
**Uvicorn** is an ASGI (Asynchronous Server Gateway Interface) server implementation for Python. It serves FastAPI applications.

**Running LeafGuard AI backend:**

```bash
# Development mode with auto-reload
uvicorn main:app --reload --host 0.0.0.0 --port 8000

# Production mode with multiple workers
uvicorn main:app --host 0.0.0.0 --port 8000 --workers 4

# With Gunicorn (process manager) + Uvicorn workers
gunicorn main:app -w 4 -k uvicorn.workers.UvicornWorker --bind 0.0.0.0:8000
```

**Command breakdown:**
- `main` — Python file name (main.py)
- `app` — FastAPI application instance variable
- `--reload` — restart on code change (development only)
- `--host 0.0.0.0` — accept connections from all network interfaces (required for Android to connect from same network)
- `--port 8000` — port to listen on
- `--workers 4` — number of parallel worker processes (production)

**Finding your PC's IP for Android connection:**

```bash
# Linux/Mac
ifconfig | grep "inet "

# Windows
ipconfig

# Result example: 192.168.1.105
# Android app connects to: http://192.168.1.105:8000
```

**WSGI vs ASGI:**
- WSGI (Flask, Django): Synchronous, one request at a time per worker
- ASGI (FastAPI, Django Channels): Asynchronous, thousands of concurrent connections per worker

---

### Q30. How would you secure the FastAPI backend? What security measures are important?

**Answer:**
Security is multi-layered. Key measures for LeafGuard AI backend:

**1. Input validation (already done via Pydantic):**
```python
# File type validation
ALLOWED_MIME = {"image/jpeg", "image/png"}
if file.content_type not in ALLOWED_MIME:
    raise HTTPException(400, "Invalid file type")

# File size limit
MAX_FILE_SIZE = 10 * 1024 * 1024  # 10 MB
if len(contents) > MAX_FILE_SIZE:
    raise HTTPException(413, "File too large")
```

**2. Rate limiting:**
```python
from slowapi import Limiter
from slowapi.util import get_remote_address

limiter = Limiter(key_func=get_remote_address)

@app.post("/predict")
@limiter.limit("10/minute")  # Max 10 predictions per minute per IP
async def predict(request: Request, file: UploadFile = File(...)):
    ...
```

**3. API key authentication:**
```python
from fastapi.security import APIKeyHeader

api_key_header = APIKeyHeader(name="X-API-Key")

async def verify_api_key(api_key: str = Security(api_key_header)):
    if api_key != settings.API_KEY:
        raise HTTPException(status_code=403, detail="Invalid API key")
    return api_key

@app.post("/predict", dependencies=[Depends(verify_api_key)])
async def predict(file: UploadFile = File(...)):
    ...
```

**4. Environment variables (no hardcoded secrets):**
```python
# config.py
from pydantic import BaseSettings

class Settings(BaseSettings):
    api_key: str
    model_path: str = "model/plant_disease_model.h5"
    max_file_size_mb: int = 10

    class Config:
        env_file = ".env"

settings = Settings()
```

**5. HTTPS in production:** Deploy behind nginx or use a cloud provider with TLS termination.

**For the CSE 2206 project**, basic API key and input validation are sufficient for academic purposes.

---

## SECTION 4: MACHINE LEARNING / AI (10 Questions)

### Q31. What is a Convolutional Neural Network (CNN)? How does it work for plant disease detection?

**Answer:**
A **Convolutional Neural Network (CNN)** is a class of deep neural network designed to process grid-structured data like images. It automatically learns spatial patterns (edges, textures, shapes) without manual feature engineering.

**How a CNN processes an image:**

```
Input Image (224×224×3 pixels)
        ↓
Convolutional Layer 1 (learns edges, colour gradients)
        ↓
Pooling Layer 1 (reduces spatial size: 224→112)
        ↓
Convolutional Layer 2 (learns textures, spots, patterns)
        ↓
Pooling Layer 2 (112→56)
        ↓
Convolutional Layer 3 (learns disease-specific features)
        ↓
Global Average Pooling (56×56×256 → 256)
        ↓
Dense Layer (256 → 128 neurons)
        ↓
Output Layer (128 → 6 neurons, one per disease class)
        ↓
Softmax → Probability Distribution [0.02, 0.91, 0.03, 0.02, 0.01, 0.01]
```

**Key layers:**
- **Conv2D:** Applies filters (kernels) to detect patterns. A 3×3 kernel slides over the image computing dot products.
- **MaxPooling2D:** Takes the maximum value in each small window — reduces size, keeps strongest features
- **Dense/Fully Connected:** Traditional neural network layer for final classification
- **Softmax:** Converts raw scores to probabilities (sum = 1.0)

**For plant disease:**
- Early layers detect colours (yellowing, browning) and edges (lesion boundaries)
- Middle layers detect textures (concentric rings, powdery spots)
- Deep layers detect disease-specific patterns (blight shapes, mildew coverage)

**In LeafGuard AI:** A pre-trained MobileNetV2 base (trained on ImageNet) with a custom classification head trained on PlantVillage dataset (87,000 plant disease images).

---

### Q32. What is transfer learning? Why was it used in LeafGuard AI?

**Answer:**
**Transfer learning** is reusing a model trained on one large task as the starting point for a different but related task. Instead of training from scratch, you leverage patterns already learned.

**Analogy:** A doctor who specialises in dermatology already knows human anatomy, biology, and medical reasoning — they don't re-learn basic science. They apply existing knowledge to a new specialisation.

**Transfer learning process for LeafGuard AI:**

```python
import tensorflow as tf
from tensorflow.keras.applications import MobileNetV2
from tensorflow.keras import layers, Model

# Step 1: Load pre-trained base model (no classification head)
base_model = MobileNetV2(
    input_shape=(224, 224, 3),
    include_top=False,           # Remove ImageNet 1000-class output
    weights='imagenet'           # Use ImageNet pre-trained weights
)

# Step 2: Freeze base model weights (don't train them initially)
base_model.trainable = False

# Step 3: Add custom classification head for plant diseases
inputs = tf.keras.Input(shape=(224, 224, 3))
x = base_model(inputs, training=False)
x = layers.GlobalAveragePooling2D()(x)
x = layers.Dropout(0.2)(x)
outputs = layers.Dense(38, activation='softmax')(x)  # 38 disease classes

model = Model(inputs, outputs)

# Step 4: Train only the new head (base frozen)
model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])
model.fit(train_dataset, epochs=10, validation_data=val_dataset)

# Step 5: Fine-tuning — unfreeze top layers of base and train with small learning rate
base_model.trainable = True
for layer in base_model.layers[:-20]:
    layer.trainable = False

model.compile(optimizer=tf.keras.optimizers.Adam(1e-5), ...)
model.fit(train_dataset, epochs=5, ...)
```

**Why transfer learning for LeafGuard AI:**
1. **Limited data:** PlantVillage has 87K images — insufficient to train a deep CNN from scratch effectively
2. **Speed:** Training a custom CNN from scratch takes days on GPU; transfer learning converges in hours
3. **Accuracy:** MobileNetV2 already knows edges, textures, shapes — only disease-specific patterns need learning
4. **Mobile efficiency:** MobileNetV2 is designed for mobile inference (lightweight, fast)

---

### Q33. What is the PlantVillage dataset? How is it structured?

**Answer:**
**PlantVillage** is an open-source dataset of 87,000+ labelled plant leaf images covering 38 disease/healthy categories across 14 plant species.

**Dataset structure:**
```
PlantVillage/
├── Apple___Apple_scab/         (2,016 images)
├── Apple___Black_rot/          (621 images)
├── Apple___Cedar_apple_rust/   (275 images)
├── Apple___healthy/            (1,645 images)
├── Corn_(maize)___Cercospora_leaf_spot/  (513 images)
├── Corn_(maize)___Common_rust/  (1,192 images)
├── Tomato___Early_blight/      (1,000 images)
├── Tomato___Late_blight/       (1,909 images)
├── Tomato___healthy/           (1,591 images)
└── ... (38 classes total)
```

**Class naming convention:** `PlantName___DiseaseOrHealthy`

**Dataset statistics:**
- Total images: ~87,000
- Number of classes: 38
- Image format: JPEG, variable sizes
- Typical split: 80% train, 10% validation, 10% test

**Loading with TensorFlow:**
```python
import tensorflow as tf

BATCH_SIZE = 32
IMG_SIZE = (224, 224)

# Load from directory structure
train_ds = tf.keras.utils.image_dataset_from_directory(
    "PlantVillage/train",
    image_size=IMG_SIZE,
    batch_size=BATCH_SIZE,
    label_mode='categorical'  # One-hot encoded labels
)

val_ds = tf.keras.utils.image_dataset_from_directory(
    "PlantVillage/val",
    image_size=IMG_SIZE,
    batch_size=BATCH_SIZE,
    label_mode='categorical'
)

# Performance optimisation
AUTOTUNE = tf.data.AUTOTUNE
train_ds = train_ds.prefetch(buffer_size=AUTOTUNE)
```

**Class imbalance:** Some classes have far fewer images (e.g., Cedar Apple Rust: 275) vs others (Apple Scab: 2,016). Techniques to handle this: class weights, oversampling, data augmentation.

---

### Q34. What is data augmentation? Why is it used in model training?

**Answer:**
**Data augmentation** is artificially creating new training samples by applying random transformations to existing images. This prevents overfitting and improves model generalisation.

**Common augmentation techniques:**

```python
from tensorflow.keras import layers
import tensorflow as tf

# Data augmentation pipeline
data_augmentation = tf.keras.Sequential([
    layers.RandomFlip("horizontal"),        # Mirror image left-right
    layers.RandomRotation(0.2),             # Rotate ±20%
    layers.RandomZoom(0.1),                 # Zoom in/out ±10%
    layers.RandomBrightness(0.2),           # Vary brightness ±20%
    layers.RandomContrast(0.2),             # Vary contrast ±20%
    layers.RandomTranslation(0.1, 0.1),     # Shift up/down/left/right
])

# Apply augmentation only during training
model = tf.keras.Sequential([
    data_augmentation,          # Augmentation layer
    base_model,                 # Pre-trained feature extractor
    layers.GlobalAveragePooling2D(),
    layers.Dense(38, activation='softmax')
])
```

**Why augmentation for plant disease:**
- A disease looks the same if the leaf is flipped horizontally
- Field photos are taken from varying angles and lighting conditions
- Augmentation teaches the model that disease patterns are rotation/lighting invariant

**What NOT to augment:**
- Don't flip vertically (unnatural for photos of plants)
- Don't rotate 90°+ (leaves look unnatural at extreme angles)
- Don't apply extreme colour shifts (might confuse disease colour signatures)

**Effect on training:**
- Without augmentation: Model memorises training images, fails on new photos (overfitting)
- With augmentation: Model learns disease features that generalise to unseen photos

---

### Q35. What are model accuracy and loss? How do you evaluate a plant disease model?

**Answer:**

**Training metrics:**

| Metric | Formula | What it measures |
|--------|---------|-----------------|
| Accuracy | Correct predictions / Total predictions | Overall correctness |
| Loss (Categorical Cross-Entropy) | −Σ y·log(ŷ) | How far predictions are from true labels |
| Validation Accuracy | Accuracy on unseen data | Generalisation ability |
| Validation Loss | Loss on unseen data | Overfitting indicator |

**Reading training output:**
```
Epoch 10/20
2750/2750 [==============================] - 45s 16ms/step
 - loss: 0.1234
 - accuracy: 0.9580
 - val_loss: 0.2891          ← Validation loss higher than train loss: some overfitting
 - val_accuracy: 0.9120      ← 91.2% correct on unseen images
```

**Confusion matrix for multi-class evaluation:**
```python
from sklearn.metrics import classification_report, confusion_matrix
import seaborn as sns

y_true = []
y_pred = []

for images, labels in test_dataset:
    predictions = model.predict(images)
    y_true.extend(tf.argmax(labels, axis=1).numpy())
    y_pred.extend(tf.argmax(predictions, axis=1).numpy())

print(classification_report(y_true, y_pred, target_names=class_names))
```

**Key classification metrics:**
- **Precision:** Of all predictions for class X, how many were truly class X? (low false positives)
- **Recall:** Of all actual class X instances, how many did we correctly predict? (low false negatives)
- **F1 Score:** Harmonic mean of precision and recall — best overall metric when classes are imbalanced

**For plant disease detection:** High recall is more important than precision — better to flag a potentially diseased plant (false positive) than to miss a real disease (false negative).

---

### Q36. What is TensorFlow Lite (TFLite)? How does it differ from full TensorFlow?

**Answer:**
**TensorFlow Lite** is a lightweight version of TensorFlow optimised for mobile, embedded, and edge devices.

**Key differences:**

| Feature | TensorFlow (Full) | TensorFlow Lite |
|---------|------------------|-----------------|
| Platform | Python, servers, GPUs | Android, iOS, microcontrollers |
| Model format | `.h5`, `SavedModel` | `.tflite` |
| Size | 300-500 MB runtime | 1-2 MB runtime |
| Ops supported | All TF ops | Subset (mobile-optimised) |
| Inference speed | Moderate | Optimised for mobile CPU/GPU/NPU |
| Training | Yes | No (inference only) |
| Quantization | Optional | Strongly recommended |

**TFLite conversion:**
```python
# Convert trained Keras model to TFLite
import tensorflow as tf

# Load saved model
model = tf.keras.models.load_model("plant_disease_model.h5")

# Convert with default settings
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

# Save .tflite file
with open("model/plant_disease.tflite", "wb") as f:
    f.write(tflite_model)
print(f"TFLite model size: {len(tflite_model) / 1024 / 1024:.1f} MB")

# Convert with INT8 quantization (smaller, faster)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_quant_model = converter.convert()
# Typically reduces model from ~20 MB to ~5 MB
```

**Why TFLite for LeafGuard AI:**
- Enables **offline detection** — works without internet
- Runs on device CPU (or Android's NNAPI for hardware acceleration)
- 5-50ms inference time on modern Android devices
- No server costs for offline mode

---

### Q37. Explain model quantization. Why does it matter for mobile deployment?

**Answer:**
**Quantization** reduces the precision of model weights from 32-bit floats (FP32) to lower-precision formats (INT8, FP16), reducing model size and improving inference speed.

**Types of quantization:**

```python
import tensorflow as tf

converter = tf.lite.TFLiteConverter.from_keras_model(model)

# 1. Default (dynamic range) quantization
# FP32 weights → INT8 at inference time
converter.optimizations = [tf.lite.Optimize.DEFAULT]

# 2. Full integer quantization (requires representative dataset)
def representative_dataset():
    for images, _ in train_dataset.take(100):
        yield [tf.cast(images, tf.float32)]

converter.optimizations = [tf.lite.Optimize.DEFAULT]
converter.representative_dataset = representative_dataset
converter.target_spec.supported_ops = [tf.lite.OpsSet.TFLITE_BUILTINS_INT8]
converter.inference_input_type = tf.int8
converter.inference_output_type = tf.int8

# 3. Float16 quantization (good balance of size and accuracy)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
converter.target_spec.supported_types = [tf.float16]
```

**Impact on model:**

| Type | Size Reduction | Speed Improvement | Accuracy Loss |
|------|---------------|------------------|---------------|
| FP32 (no quantization) | Baseline | Baseline | None |
| FP16 | ~50% smaller | Slight improvement | Negligible |
| INT8 (dynamic) | ~75% smaller | 2-4× faster | Small (< 1%) |
| INT8 (full integer) | ~75% smaller | 2-4× faster, NNAPI | ~1-2% |

**Why it matters for LeafGuard AI:**
- Original model: ~20 MB → INT8 quantized: ~5 MB
- APK size constraint: Google Play recommends < 150 MB total APK
- Inference time: 200ms → 50ms with INT8 + NNAPI
- Battery life: Fewer CPU cycles = less battery drain

---

### Q38. What is overfitting and underfitting? How do you prevent them?

**Answer:**

**Overfitting:** Model memorises training data — excellent training accuracy but poor on new data.
**Underfitting:** Model is too simple — poor accuracy on both training and test data.

**Visualising the difference:**
```
Training Loss ↓ | Validation Loss ↓ → Model improving (good)
Training Loss ↓ | Validation Loss ↑ → Overfitting (gap widening)
Both losses high → Underfitting (model too simple)
```

**Prevention techniques used in LeafGuard AI model:**

```python
from tensorflow.keras import layers, regularizers

model = tf.keras.Sequential([
    # 1. Data augmentation (prevents overfitting)
    layers.RandomFlip("horizontal"),
    layers.RandomRotation(0.15),

    base_model,  # MobileNetV2

    layers.GlobalAveragePooling2D(),

    # 2. Dropout (randomly zeros out neurons during training)
    layers.Dropout(0.3),           # 30% neurons dropped each batch

    # 3. L2 Regularization (penalises large weights)
    layers.Dense(
        128,
        activation='relu',
        kernel_regularizer=regularizers.l2(0.001)
    ),

    layers.Dropout(0.2),

    layers.Dense(38, activation='softmax')
])

# 4. Early stopping (stop training when validation loss stops improving)
early_stop = tf.keras.callbacks.EarlyStopping(
    monitor='val_loss',
    patience=5,          # Stop after 5 epochs without improvement
    restore_best_weights=True
)

# 5. Learning rate scheduling
lr_scheduler = tf.keras.callbacks.ReduceLROnPlateau(
    monitor='val_loss',
    factor=0.5,          # Halve learning rate
    patience=3,
    min_lr=1e-7
)

model.fit(train_ds, validation_data=val_ds,
          callbacks=[early_stop, lr_scheduler])
```

---

### Q39. How does the TFLite interpreter work on Android? Explain the complete offline prediction flow.

**Answer:**
The TFLite interpreter loads a `.tflite` model file, allocates tensors, and runs inference on CPU, GPU, or NNAPI delegate.

**Complete Android TFLite inference flow:**

```java
// TFLiteClassifier.java (from LeafGuard AI android-app/)

public class TFLiteClassifier {
    private Interpreter interpreter;
    private List<String> labels;

    // Step 1: Load model from assets
    public void initialize(Context context) throws IOException {
        MappedByteBuffer modelBuffer = loadModelFile(context, "plant_disease.tflite");
        
        // Configure interpreter options
        Interpreter.Options options = new Interpreter.Options();
        options.setNumThreads(4);              // Use 4 CPU threads
        // options.addDelegate(new GpuDelegate()); // Enable GPU (optional)
        
        interpreter = new Interpreter(modelBuffer, options);
        labels = loadLabels(context, "labels.txt");
    }

    private MappedByteBuffer loadModelFile(Context ctx, String filename) throws IOException {
        AssetFileDescriptor afd = ctx.getAssets().openFd(filename);
        FileInputStream fis = new FileInputStream(afd.getFileDescriptor());
        FileChannel channel = fis.getChannel();
        return channel.map(FileChannel.MapMode.READ_ONLY,
                           afd.getStartOffset(), afd.getDeclaredLength());
    }

    // Step 2: Preprocess Bitmap
    private float[][][][] preprocessBitmap(Bitmap bitmap) {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        float[][][][] input = new float[1][224][224][3];  // [batch][H][W][channels]

        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 224; x++) {
                int pixel = resized.getPixel(x, y);
                input[0][y][x][0] = (pixel >> 16 & 0xFF) / 255.0f;  // R
                input[0][y][x][1] = (pixel >> 8  & 0xFF) / 255.0f;  // G
                input[0][y][x][2] = (pixel        & 0xFF) / 255.0f;  // B
            }
        }
        return input;
    }

    // Step 3: Run inference and decode output
    public ClassificationResult classify(Bitmap bitmap) {
        float[][][][] input = preprocessBitmap(bitmap);
        float[][] output = new float[1][labels.size()];  // [batch][num_classes]

        interpreter.run(input, output);

        // Find highest confidence class
        int topIdx = 0;
        float topConf = 0;
        for (int i = 0; i < output[0].length; i++) {
            if (output[0][i] > topConf) {
                topConf = output[0][i];
                topIdx = i;
            }
        }
        return new ClassificationResult(labels.get(topIdx), topConf);
    }
}
```

**Execution delegates (hardware acceleration):**
- **CPU delegate** (default): Works on all Android devices
- **GPU delegate:** 5-10× faster on devices with Mali/Adreno GPU
- **NNAPI delegate:** Uses Android's Neural Networks API — routes to best available hardware

---

### Q40. What are precision, recall, and F1-score? Interpret them for a plant disease model with 91% accuracy.

**Answer:**

**Scenario:** 100 test images. Model says 91 are correct.

But accuracy alone is misleading with **class imbalance:**
```
Dataset: 80 healthy leaves, 20 diseased leaves
Predict all as healthy → 80% accuracy (useless model!)
```

**Precision:** "When the model says DISEASED, how often is it right?"
```
Precision = True Positives / (True Positives + False Positives)
= 18 / (18 + 2) = 0.90
```

**Recall (Sensitivity):** "Of all actually DISEASED leaves, how many did the model catch?"
```
Recall = True Positives / (True Positives + False Negatives)
= 18 / (18 + 2) = 0.90
```

**F1 Score:** Harmonic mean of precision and recall
```
F1 = 2 × (Precision × Recall) / (Precision + Recall)
   = 2 × (0.90 × 0.90) / (0.90 + 0.90) = 0.90
```

**Confusion matrix for LeafGuard AI model (6 classes):**
```
                Predicted
Actual     Healthy  Blight  Rust  Mildew  Spot  Mosaic
Healthy       48       1     0      0      1      0    ← 2 false positives
Blight         0      14     1      0      0      0
Rust           1       0    10      0      0      0
Mildew         0       0     1      9      0      0
Spot           0       1     0      0     13      0
Mosaic         0       0     0      0      0       8
```

**Interpreting 91% accuracy:**
- Good overall performance
- Check if any specific disease class has low recall (missed diagnoses)
- Low recall on a serious disease (Late Blight) is a critical failure → prioritise improving those classes

---

## SECTION 5: DATABASE / ROOM (8 Questions)

### Q41. What is Room Database? How does it relate to SQLite?

**Answer:**
**Room** is a persistence library from Android Jetpack that provides an abstraction layer over SQLite. It generates boilerplate SQL code and provides compile-time verification of SQL queries.

**Relationship:**
```
Room ─────────────── Abstraction layer over ──────────────► SQLite
                                                              (actual storage)
```

**Without Room (raw SQLite):**
```java
// Manual, error-prone, verbose
SQLiteDatabase db = getWritableDatabase();
ContentValues values = new ContentValues();
values.put("disease_name", "Early Blight");
values.put("confidence", 0.91f);
values.put("timestamp", System.currentTimeMillis());
long rowId = db.insert("scan_records", null, values);

// Query
Cursor cursor = db.rawQuery(
    "SELECT * FROM scan_records ORDER BY timestamp DESC LIMIT 50", null
);
while (cursor.moveToNext()) {
    String name = cursor.getString(cursor.getColumnIndex("disease_name"));
    // ... manual parsing
}
cursor.close();
```

**With Room:**
```java
// Clean, type-safe, compile-time verified
scanDao.insert(new ScanRecord("Early Blight", 0.91f));
List<ScanRecord> records = scanDao.getRecentScans(50);
// No cursors, no column indices, no manual parsing
```

**Room components:**

| Component | Annotation | Role |
|-----------|-----------|------|
| Entity | `@Entity` | Defines database table schema |
| DAO | `@Dao` | Interface declaring database operations |
| Database | `@Database` | Database holder, provides DAOs |

**LeafGuard AI Room setup:**
```java
// AppDatabase.java
@Database(entities = {ScanRecord.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScanDao scanDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "leafguard_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
```

---

### Q42. Explain the @Entity annotation. How did you design the ScanRecord entity?

**Answer:**
`@Entity` maps a Java class to a database table. Each field becomes a column.

```java
// ScanRecord.java — from LeafGuard AI
@Entity(tableName = "scan_records")
public class ScanRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;                       // Auto-incremented primary key

    @ColumnInfo(name = "disease_name")
    private String diseaseName;           // TEXT column

    @ColumnInfo(name = "confidence")
    private float confidence;             // REAL column (0.0–1.0)

    @ColumnInfo(name = "symptoms")
    private String symptoms;              // TEXT column

    @ColumnInfo(name = "treatment")
    private String treatment;             // TEXT column

    @ColumnInfo(name = "image_uri")
    private String imageUri;              // TEXT column (file path or URI string)

    @ColumnInfo(name = "timestamp")
    private long timestamp;               // INTEGER column (Unix milliseconds)

    @ColumnInfo(name = "is_cloud_scan")
    private boolean isCloudScan;          // INTEGER column (0 or 1)

    // Constructor
    public ScanRecord(String diseaseName, float confidence, String symptoms,
                      String treatment, String imageUri, boolean isCloudScan) {
        this.diseaseName = diseaseName;
        this.confidence = confidence;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.imageUri = imageUri;
        this.timestamp = System.currentTimeMillis();
        this.isCloudScan = isCloudScan;
    }

    // Getters and setters (required by Room)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    // ... (all fields)
}
```

**Generated SQL:**
```sql
CREATE TABLE IF NOT EXISTS scan_records (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    disease_name TEXT,
    confidence REAL,
    symptoms TEXT,
    treatment TEXT,
    image_uri TEXT,
    timestamp INTEGER,
    is_cloud_scan INTEGER
);
```

**Important rules:**
- Every `@Entity` must have exactly one `@PrimaryKey`
- Room maps Java types: `String→TEXT`, `int/long→INTEGER`, `float/double→REAL`, `boolean→INTEGER (0/1)`
- Field names default to column names; use `@ColumnInfo(name = "...")` for custom names

---

### Q43. What is a DAO? Explain the CRUD operations in ScanDao.

**Answer:**
**DAO = Data Access Object** — An interface that declares all database operations. Room generates the implementation at compile time.

```java
// ScanDao.java — from LeafGuard AI
@Dao
public interface ScanDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ScanRecord record);

    @Insert
    void insertAll(List<ScanRecord> records);

    // READ
    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC")
    List<ScanRecord> getAllScans();

    @Query("SELECT * FROM scan_records WHERE id = :id")
    ScanRecord getScanById(int id);

    @Query("SELECT * FROM scan_records ORDER BY timestamp DESC LIMIT :limit")
    List<ScanRecord> getRecentScans(int limit);

    @Query("SELECT * FROM scan_records WHERE disease_name LIKE '%' || :query || '%'")
    List<ScanRecord> searchByDiseaseName(String query);

    @Query("SELECT COUNT(*) FROM scan_records")
    int getTotalScanCount();

    // UPDATE
    @Update
    void update(ScanRecord record);

    @Query("UPDATE scan_records SET treatment = :treatment WHERE id = :id")
    void updateTreatment(int id, String treatment);

    // DELETE
    @Delete
    void delete(ScanRecord record);

    @Query("DELETE FROM scan_records WHERE id = :id")
    void deleteById(int id);

    @Query("DELETE FROM scan_records")
    void deleteAllScans();

    @Query("DELETE FROM scan_records WHERE timestamp < :cutoffTime")
    void deleteOlderThan(long cutoffTime);
}
```

**Room compiles DAO at build time** — if your SQL is wrong, the build fails. This prevents runtime SQL errors.

**Usage in Activity:**
```java
// Must run on background thread (Room enforces this)
new Thread(() -> {
    AppDatabase db = AppDatabase.getInstance(this);
    ScanRecord record = new ScanRecord("Early Blight", 0.91f, ...);
    db.scanDao().insert(record);
    
    List<ScanRecord> history = db.scanDao().getAllScans();
    runOnUiThread(() -> adapter.submitList(history));
}).start();
```

---

### Q44. What are database migrations? When are they needed?

**Answer:**
A **migration** updates the database schema when you release a new app version that changes the table structure, without losing existing user data.

**When migrations are needed:**
- Adding a new column to an existing table
- Renaming a table or column
- Adding a new table
- Changing a column's data type
- Removing a column (SQLite limitation: must recreate table)

**Example: Adding `prevention` column in v2:**

```java
// Version 1: Original schema
@Database(entities = {ScanRecord.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase { ... }

// Version 2: Add 'prevention' column to ScanRecord
@Entity(tableName = "scan_records")
public class ScanRecord {
    // ... existing fields ...
    @ColumnInfo(name = "prevention")  // NEW FIELD
    private String prevention;
}

@Database(entities = {ScanRecord.class}, version = 2)  // Increment version
public abstract class AppDatabase extends RoomDatabase {

    // Migration 1 → 2
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                "ALTER TABLE scan_records ADD COLUMN prevention TEXT"
            );
            // SQLite can only ADD columns, not remove or rename
        }
    };

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "leafguard_database"
            )
            .addMigrations(MIGRATION_1_2)  // Register migration
            .build();
        }
        return INSTANCE;
    }
}
```

**Fallback (for development only):**
```java
.fallbackToDestructiveMigration()  // Drops and recreates DB — LOSES ALL DATA
```

**Best practice:** Always write migrations. Never use `fallbackToDestructiveMigration` in production.

---

### Q45. How do you run database operations on a background thread in Android?

**Answer:**
Room enforces that database operations happen on a **background thread** — calling them on the main (UI) thread throws `IllegalStateException`.

**Why?** Database reads/writes can take milliseconds to seconds. Blocking the main thread freezes the UI (ANR — Application Not Responding).

**Method 1: Java Thread (simple, used in LeafGuard AI starter):**
```java
new Thread(() -> {
    ScanRecord record = new ScanRecord("Early Blight", 0.91f, ...);
    AppDatabase.getInstance(this).scanDao().insert(record);

    List<ScanRecord> history = AppDatabase.getInstance(this).scanDao().getAllScans();
    runOnUiThread(() -> {
        adapter.submitList(history);
        progressBar.setVisibility(View.GONE);
    });
}).start();
```

**Method 2: AsyncTask (deprecated but exam-relevant):**
```java
private static class InsertScanTask extends AsyncTask<ScanRecord, Void, Void> {
    private final ScanDao scanDao;

    InsertScanTask(ScanDao dao) { this.scanDao = dao; }

    @Override
    protected Void doInBackground(ScanRecord... records) {
        scanDao.insert(records[0]);
        return null;
    }
}

// Usage
new InsertScanTask(db.scanDao()).execute(newRecord);
```

**Method 3: Kotlin Coroutines (modern, recommended):**
```kotlin
// DAO with suspend functions
@Dao
interface ScanDao {
    @Insert suspend fun insert(record: ScanRecord)
    @Query("SELECT * FROM scan_records") suspend fun getAllScans(): List<ScanRecord>
}

// ViewModel usage
viewModelScope.launch(Dispatchers.IO) {
    dao.insert(record)
    val history = dao.getAllScans()
    withContext(Dispatchers.Main) {
        adapter.submitList(history)
    }
}
```

**For CSE 2206 Java project:** Use `new Thread(...)` with `runOnUiThread(...)` or `Executors.newSingleThreadExecutor()`.

---

### Q46. What is the difference between @Insert, @Update, @Delete, and @Query in Room?

**Answer:**

| Annotation | Purpose | Returns | Notes |
|-----------|---------|---------|-------|
| `@Insert` | Insert one or more entities | `void`, `long` (row ID), or `long[]` | `onConflict` strategy handles duplicates |
| `@Update` | Update entities by primary key | `void` or `int` (rows affected) | Matches by `@PrimaryKey` value |
| `@Delete` | Delete entities by primary key | `void` or `int` (rows deleted) | Matches by `@PrimaryKey` value |
| `@Query` | Any SQL statement | Any type Room can convert | Full SQL flexibility |

**Conflict strategies for @Insert:**
```java
// REPLACE: Delete old row, insert new (useful for "upsert")
@Insert(onConflict = OnConflictStrategy.REPLACE)
void insert(ScanRecord record);

// IGNORE: Skip if conflict (useful for avoiding duplicates)
@Insert(onConflict = OnConflictStrategy.IGNORE)
long insertIfNotExists(ScanRecord record);  // Returns -1 if ignored

// ABORT (default): Throw exception on conflict
@Insert
void insert(ScanRecord record);
```

**@Query examples:**
```java
// Aggregate
@Query("SELECT AVG(confidence) FROM scan_records WHERE disease_name = :name")
float getAverageConfidence(String name);

// JOIN (requires separate entity or POJO)
@Query("SELECT disease_name, COUNT(*) as count FROM scan_records GROUP BY disease_name ORDER BY count DESC")
List<DiseaseCount> getDiseaseFrequency();

// Named parameters
@Query("SELECT * FROM scan_records WHERE timestamp BETWEEN :startTime AND :endTime")
List<ScanRecord> getScansInRange(long startTime, long endTime);
```

---

### Q47. Explain the RecyclerView and Adapter pattern used to display scan history.

**Answer:**
**RecyclerView** is a flexible view for displaying large scrollable lists efficiently. It **recycles** off-screen item views instead of creating new ones.

**Why RecyclerView over ListView?**
- ViewHolder pattern enforced (no accidental performance issues)
- `DiffUtil` for efficient list updates
- Multiple layout managers (linear, grid, staggered)
- Item animations built-in
- Easy item decoration (dividers, padding)

**Implementation in HistoryActivity:**

```java
// 1. Adapter class
public class ScanHistoryAdapter extends RecyclerView.Adapter<ScanHistoryAdapter.ViewHolder> {

    private List<ScanRecord> records = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ScanRecord record);
    }

    public ScanHistoryAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    // ViewHolder — holds references to views in one list item
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDiseaseName, tvConfidence, tvTimestamp;
        ImageView imgThumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            tvDiseaseName = itemView.findViewById(R.id.text_disease_name);
            tvConfidence = itemView.findViewById(R.id.text_confidence);
            tvTimestamp = itemView.findViewById(R.id.text_timestamp);
            imgThumbnail = itemView.findViewById(R.id.image_thumbnail);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Called once per visible item type — inflates the layout
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_scan_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Called when item scrolls into view — binds data to views
        ScanRecord record = records.get(position);
        holder.tvDiseaseName.setText(record.getDiseaseName());
        holder.tvConfidence.setText(
            String.format("%.0f%% confidence", record.getConfidence() * 100)
        );
        holder.tvTimestamp.setText(
            new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
                .format(new Date(record.getTimestamp()))
        );
        holder.itemView.setOnClickListener(v -> listener.onItemClick(record));
    }

    @Override
    public int getItemCount() { return records.size(); }

    public void submitList(List<ScanRecord> newRecords) {
        this.records = newRecords;
        notifyDataSetChanged();
    }
}

// 2. Activity setup
RecyclerView recyclerView = binding.recyclerViewHistory;
recyclerView.setLayoutManager(new LinearLayoutManager(this));
ScanHistoryAdapter adapter = new ScanHistoryAdapter(record -> {
    // Navigate to detail screen on click
    Intent intent = new Intent(this, HistoryDetailActivity.class);
    intent.putExtra("scan_id", record.getId());
    startActivity(intent);
});
recyclerView.setAdapter(adapter);
```

---

### Q48. What is the difference between LiveData and regular List in Room? When would you use LiveData?

**Answer:**

**Regular List (static):**
```java
// Returns a snapshot — does NOT update when database changes
List<ScanRecord> records = db.scanDao().getAllScans();
// If new scan is added, this list is stale until you re-query
```

**LiveData (reactive):**
```java
// DAO returns LiveData
@Query("SELECT * FROM scan_records ORDER BY timestamp DESC")
LiveData<List<ScanRecord>> getAllScansLive();

// Activity observes — automatically updates UI when database changes
db.scanDao().getAllScansLive().observe(this, records -> {
    adapter.submitList(records);  // UI refreshes automatically
});
```

**How LiveData works with Room:**
1. Activity calls `getAllScansLive()` — returns `LiveData<List<ScanRecord>>`
2. Activity calls `.observe(lifecycleOwner, observer)`
3. Room sets up an `InvalidationTracker` on the `scan_records` table
4. When any insert/update/delete happens, Room notifies the LiveData
5. LiveData delivers the updated list to all active observers on the main thread

**Benefits of LiveData:**
- **Lifecycle-aware:** Stops delivering updates when Activity is paused/stopped — no memory leaks
- **No manual refresh:** Database changes automatically update UI
- **Single source of truth:** All UI state comes from database

**In LeafGuard AI:** HistoryActivity uses LiveData so when a new scan is saved in ResultActivity and the user navigates back, the history list immediately shows the new entry.

---

## SECTION 6: XML PARSING (5 Questions)

### Q49. What is XML? How is it used in the LeafGuard AI disease library?

**Answer:**
**XML (eXtensible Markup Language)** is a text-based format for storing and transporting structured data using custom tags.

**XML characteristics:**
- Human-readable and machine-readable
- Hierarchical structure (tree of elements)
- Self-describing (tag names indicate meaning)
- Platform and language independent

**Disease library XML in LeafGuard AI:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<disease_library version="1.2">

    <disease id="TEB001">
        <name>Tomato Early Blight</name>
        <scientific_name>Alternaria solani</scientific_name>
        <plant_type>Tomato</plant_type>
        <severity>Moderate</severity>

        <symptoms>
            <symptom>Dark brown spots with concentric rings (target-board pattern)</symptom>
            <symptom>Yellowing leaves surrounding the lesions</symptom>
            <symptom>Lower and older leaves affected first</symptom>
        </symptoms>

        <causes>
            <cause>Fungal infection by Alternaria solani</cause>
            <cause>Warm, humid conditions (24-29°C)</cause>
            <cause>Overhead irrigation wetting foliage</cause>
        </causes>

        <treatment>
            <step order="1">Remove and destroy infected leaves immediately</step>
            <step order="2">Apply copper-based fungicide (e.g., Bordeaux mixture)</step>
            <step order="3">Improve plant spacing for better air circulation</step>
        </treatment>

        <prevention>
            <tip>Rotate crops every season</tip>
            <tip>Use disease-resistant tomato varieties</tip>
            <tip>Avoid overhead watering; use drip irrigation</tip>
            <tip>Apply preventive fungicide at early season</tip>
        </prevention>

        <image_resource>disease_tomato_early_blight</image_resource>
    </disease>

</disease_library>
```

**Advantages of XML over hardcoded Java:**
- Non-programmers can update disease information
- App can download updated disease library from server
- Localisation: separate XML files per language
- Version-controlled independently from code

---

### Q50. What are the three XML parsing methods in Android? Compare them.

**Answer:**

| Method | Type | Memory | Speed | Ease of Use | Best For |
|--------|------|--------|-------|-------------|----------|
| DOM Parser | Tree-based | High (loads full tree) | Slower | Easiest | Small XML, random access |
| SAX Parser | Event-based | Low (streaming) | Fastest | Harder | Large XML, read-only |
| XmlPullParser | Pull-based | Low (streaming) | Fast | Moderate | Android native, read-only |

**1. DOM Parser (loads entire XML into memory):**
```java
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document doc = builder.parse(inputStream);  // Loads EVERYTHING into RAM

NodeList diseases = doc.getElementsByTagName("disease");
for (int i = 0; i < diseases.getLength(); i++) {
    Element disease = (Element) diseases.item(i);
    String name = disease.getElementsByTagName("name").item(0).getTextContent();
}
```

**2. SAX Parser (event-based callbacks):**
```java
// Complex — must track state manually
SAXParserFactory factory = SAXParserFactory.newInstance();
SAXParser parser = factory.newSAXParser();
parser.parse(inputStream, new DefaultHandler() {
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) {
        if ("disease".equals(qName)) { /* start reading disease */ }
    }
    @Override
    public void characters(char[] ch, int start, int length) {
        /* accumulate text content */ }
});
```

**3. XmlPullParser (recommended for Android — used in LeafGuard AI):**
```java
XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
XmlPullParser parser = factory.newPullParser();
parser.setInput(inputStream, "UTF-8");

int eventType = parser.getEventType();
while (eventType != XmlPullParser.END_DOCUMENT) {
    if (eventType == XmlPullParser.START_TAG) {
        String tagName = parser.getName();
        if ("disease".equals(tagName)) {
            String id = parser.getAttributeValue(null, "id");
        }
    }
    eventType = parser.next();
}
```

**Why XmlPullParser for LeafGuard AI:** It is built into Android SDK (no extra dependency), low memory usage (streaming), and straightforward for reading forward-only structured data.

---

### Q51. Write the XmlPullParser implementation to parse the disease library.

**Answer:**

```java
// DiseaseXmlParser.java
public class DiseaseXmlParser {

    public static List<DiseaseInfo> parseDiseases(InputStream inputStream)
            throws XmlPullParserException, IOException {

        List<DiseaseInfo> diseases = new ArrayList<>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inputStream, "UTF-8");

        DiseaseInfo current = null;
        List<String> currentSymptoms = null;
        List<String> currentPrevention = null;
        List<String> currentTreatment = null;
        String currentTag = "";

        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    currentTag = parser.getName();
                    if ("disease".equals(currentTag)) {
                        current = new DiseaseInfo();
                        current.setId(parser.getAttributeValue(null, "id"));
                        currentSymptoms = new ArrayList<>();
                        currentPrevention = new ArrayList<>();
                        currentTreatment = new ArrayList<>();
                    }
                    break;

                case XmlPullParser.TEXT:
                    if (current == null) break;
                    String text = parser.getText().trim();
                    if (text.isEmpty()) break;

                    switch (currentTag) {
                        case "name":
                            current.setName(text);
                            break;
                        case "plant_type":
                            current.setPlantType(text);
                            break;
                        case "severity":
                            current.setSeverity(text);
                            break;
                        case "symptom":
                            currentSymptoms.add(text);
                            break;
                        case "tip":
                            currentPrevention.add(text);
                            break;
                        case "step":
                            currentTreatment.add(text);
                            break;
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("disease".equals(parser.getName()) && current != null) {
                        current.setSymptoms(currentSymptoms);
                        current.setPrevention(currentPrevention);
                        current.setTreatment(currentTreatment);
                        diseases.add(current);
                        current = null;
                    }
                    currentTag = "";
                    break;
            }

            eventType = parser.next();
        }

        return diseases;
    }
}

// Usage in Activity
InputStream is = getResources().openRawResource(R.raw.disease_library);
List<DiseaseInfo> diseases = DiseaseXmlParser.parseDiseases(is);
```

---

### Q52. Where is XML used in Android apart from layouts? Give examples from LeafGuard AI.

**Answer:**
XML is used extensively throughout Android development beyond just UI layouts:

**1. Layout files (`res/layout/`):**
```xml
<!-- activity_main.xml — UI component hierarchy -->
<ConstraintLayout>
    <ImageView android:id="@+id/imagePlantPreview" />
    <Button android:id="@+id/buttonDetectDisease" />
</ConstraintLayout>
```

**2. String resources (`res/values/strings.xml`):**
```xml
<!-- Externalise all user-facing text for localisation -->
<resources>
    <string name="app_name">LeafGuard AI</string>
    <string name="select_image_first">Please select an image first</string>
    <string name="cloud_mode_description">Send image to cloud server for ML analysis</string>
</resources>
```

**3. Color resources (`res/values/colors.xml`):**
```xml
<resources>
    <color name="leaf_green">#2E7D32</color>
    <color name="disease_red">#C62828</color>
    <color name="healthy_green">#388E3C</color>
</resources>
```

**4. AndroidManifest.xml — App configuration:**
```xml
<manifest>
    <uses-permission android:name="android.permission.CAMERA" />
    <application android:label="@string/app_name">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

**5. Network security config (`res/xml/network_security_config.xml`):**
```xml
<!-- Allow HTTP connections to local backend during development -->
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">192.168.1.105</domain>
    </domain-config>
</network-security-config>
```

**6. File provider paths (`res/xml/file_provider_paths.xml`):**
```xml
<!-- Define directories accessible via FileProvider for camera images -->
<paths>
    <external-files-path name="images"
        path="Pictures/captures/" />
</paths>
```

**7. Raw data files (`res/raw/disease_library.xml`):**
```xml
<!-- Disease knowledge base parsed at runtime by XmlPullParser -->
<disease_library>
    <disease id="TEB001">...</disease>
</disease_library>
```

---

### Q53. What is the difference between XML in Android resources and XML as a data format?

**Answer:**

| Aspect | Android Resource XML | Data/Configuration XML |
|--------|---------------------|----------------------|
| Location | `res/layout/`, `res/values/` | `res/raw/`, `assets/` |
| Processing | Compiled by AAPT into R.java | Parsed at runtime by your code |
| Format | Android SDK-specific tags | Your custom tags |
| Access | `R.layout.activity_main`, `@string/app_name` | `getResources().openRawResource(R.raw.diseases)` |
| Validation | Android Studio validates schema | You validate in parser |

**Android Resource XML** (processed at compile time):
```xml
<!-- res/layout/activity_main.xml — uses Android SDK tags -->
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <!-- Android knows exactly what tags are valid here -->
</androidx.constraintlayout.widget.ConstraintLayout>
```

**Data XML** (parsed at runtime):
```xml
<!-- res/raw/disease_library.xml — custom tags, you define the schema -->
<disease_library>
    <disease id="TEB001">
        <name>Tomato Early Blight</name>
        <!-- You decide what tags exist and mean -->
    </disease>
</disease_library>
```

**Practical difference in LeafGuard AI:**
- `res/layout/activity_main.xml` → Android compiles to bytecode, accessed via `R.layout.activity_main`
- `res/raw/disease_library.xml` → Kept as raw file, accessed via `InputStream`, parsed with `XmlPullParser`

**When to choose XML vs JSON for app data:**
- XML: When data is hierarchical with attributes, official Android tooling supports it
- JSON: When interacting with REST APIs, lighter syntax, easier to edit manually

---

## SECTION 7: TESTING (7 Questions)

### Q54. What is the difference between unit testing, integration testing, and UI testing?

**Answer:**

| Type | What it tests | Tools | Speed | Scope |
|------|--------------|-------|-------|-------|
| Unit Test | Single function/class in isolation | JUnit, Mockito | Very fast (ms) | Smallest |
| Integration Test | Multiple components working together | JUnit + Room, MockWebServer | Moderate | Medium |
| UI Test | User interactions on real/emulated device | Espresso, UI Automator | Slow (seconds) | Largest |

**Unit Test (logic only — no Android framework):**
```java
// Test disease name formatting without Android
@Test
public void testDiseaseNameFormatting() {
    String raw = "tomato___early_blight";
    String formatted = DiseaseUtils.formatDiseaseName(raw);
    assertEquals("Tomato Early Blight", formatted);
}
```

**Integration Test (tests Room database on device):**
```java
@RunWith(AndroidJUnit4.class)
public class ScanDaoTest {
    private AppDatabase database;
    private ScanDao scanDao;

    @Before
    public void setup() {
        // In-memory database — no actual file, faster, auto-cleared
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().getContext(),
            AppDatabase.class
        ).build();
        scanDao = database.scanDao();
    }

    @Test
    public void insertAndRetrieveScan() {
        ScanRecord record = new ScanRecord("Early Blight", 0.91f, "Symptoms", "Treatment", null, true);
        scanDao.insert(record);
        List<ScanRecord> all = scanDao.getAllScans();
        assertEquals(1, all.size());
        assertEquals("Early Blight", all.get(0).getDiseaseName());
    }

    @After
    public void tearDown() { database.close(); }
}
```

**UI Test (Espresso — tests button clicks and navigation):**
```java
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
        new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testHistoryButtonNavigatesToHistoryActivity() {
        onView(withId(R.id.buttonHistory))
            .perform(click());
        onView(withId(R.id.recyclerViewHistory))
            .check(matches(isDisplayed()));
    }
}
```

---

### Q55. How do you write JUnit tests for Android? Show an example from LeafGuard AI.

**Answer:**
JUnit 4 is the standard testing framework for Android. Tests in `src/test/java/` run on the JVM (fast, no device). Tests in `src/androidTest/java/` run on Android device/emulator.

**Unit test for confidence formatting:**
```java
// src/test/java/com/leafguard/ConfidenceFormatterTest.java
public class ConfidenceFormatterTest {

    @Test
    public void formatHighConfidence() {
        String result = ConfidenceFormatter.format(0.95f);
        assertEquals("95%", result);
    }

    @Test
    public void formatLowConfidence() {
        String result = ConfidenceFormatter.format(0.40f);
        assertEquals("40%", result);
    }

    @Test
    public void formatExactZero() {
        String result = ConfidenceFormatter.format(0.0f);
        assertEquals("0%", result);
    }

    @Test
    public void formatExactOne() {
        String result = ConfidenceFormatter.format(1.0f);
        assertEquals("100%", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatNegative_throwsException() {
        ConfidenceFormatter.format(-0.1f);  // Should throw
    }
}
```

**Unit test for ScanRecord model:**
```java
public class ScanRecordTest {

    @Test
    public void isHealthy_returnsTrueForHealthyLabel() {
        ScanRecord healthy = new ScanRecord("Tomato___healthy", 0.85f, "", "", null, true);
        assertTrue(healthy.isHealthy());
    }

    @Test
    public void isHealthy_returnsFalseForDiseaseLabel() {
        ScanRecord diseased = new ScanRecord("Tomato___Early_Blight", 0.91f, "", "", null, true);
        assertFalse(diseased.isHealthy());
    }

    @Test
    public void constructor_setsTimestampAutomatically() {
        long before = System.currentTimeMillis();
        ScanRecord record = new ScanRecord("Test", 0.5f, "", "", null, false);
        long after = System.currentTimeMillis();
        assertTrue(record.getTimestamp() >= before);
        assertTrue(record.getTimestamp() <= after);
    }
}
```

**Running tests:**
```bash
# Unit tests (fast, JVM)
./gradlew test

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest
```

---

### Q56. What is Mockito? How do you mock dependencies in Android tests?

**Answer:**
**Mockito** is a mocking framework that creates fake (mock) objects to substitute real dependencies in unit tests — allowing you to test a class in isolation.

**Why mocking?**
- Unit tests should not hit real network, real database, or real filesystem
- Control what dependencies return to test different scenarios
- Tests run without Android device (no Room, no OkHttp)

**Mocking the network layer in LeafGuard AI:**
```java
// ScanViewModelTest.java — general example; LeafGuard AI's real unit test is PredictionResponseTest (see android-app/app/src/test/)
@RunWith(MockitoJUnitRunner.class)
public class ScanViewModelTest {

    @Mock
    private ApiService mockApiService;  // Fake network — no real HTTP calls

    @Mock
    private ScanDao mockScanDao;        // Fake database — no real SQLite

    private ScanViewModel viewModel;

    @Before
    public void setup() {
        viewModel = new ScanViewModel(mockApiService, mockScanDao);
    }

    @Test
    public void uploadImage_onSuccess_updatesLiveData() throws Exception {
        // Arrange: Define what the mock returns
        PredictionResponse fakeResponse = new PredictionResponse(
            "Tomato Early Blight", 0.91f, true
        );
        when(mockApiService.detectDisease(any())).thenReturn(
            Response.success(fakeResponse)
        );

        // Act: Trigger the method under test
        viewModel.analyzeImage(mockFile);

        // Assert: Verify expected outcome
        assertNotNull(viewModel.getScanResult().getValue());
        assertEquals("Tomato Early Blight",
            viewModel.getScanResult().getValue().getDiseaseName());

        // Verify: The DAO insert was called once with correct data
        verify(mockScanDao, times(1)).insert(any(ScanRecord.class));
    }

    @Test
    public void uploadImage_onNetworkError_showsError() throws Exception {
        // Arrange: Mock throws IOException
        when(mockApiService.detectDisease(any())).thenThrow(new IOException("No network"));

        // Act
        viewModel.analyzeImage(mockFile);

        // Assert
        assertNotNull(viewModel.getErrorMessage().getValue());
        assertTrue(viewModel.getErrorMessage().getValue().contains("network"));
    }
}
```

**Key Mockito annotations:**
- `@Mock` — creates a mock object
- `when(...).thenReturn(...)` — stub return value
- `when(...).thenThrow(...)` — stub exception
- `verify(mock, times(n)).method(...)` — assert method was called n times
- `any()` — argument matcher (matches any value)

---

### Q57. What is Espresso? Write a test for the disease detection flow.

**Answer:**
**Espresso** is Android's UI testing framework. It interacts with the app as a user would — clicking buttons, typing text, scrolling lists — and asserts what is visible on screen.

**Full detection flow test:**
```java
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DiseaseDetectionFlowTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
        new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void cameraButton_isVisible_onLaunch() {
        onView(withId(R.id.buttonOpenCamera))
            .check(matches(isDisplayed()));
    }

    @Test
    public void detectButton_isDisabled_withoutImage() {
        onView(withId(R.id.buttonDetectDisease))
            .check(matches(not(isEnabled())));
    }

    @Test
    public void historyButton_navigatesTo_historyScreen() {
        onView(withId(R.id.buttonHistory))
            .perform(click());

        // Verify HistoryActivity is now shown
        onView(withId(R.id.recyclerViewHistory))
            .check(matches(isDisplayed()));
    }

    @Test
    public void modeToggle_switchesToOffline_updatesDescription() {
        // Click offline mode button
        onView(withId(R.id.buttonOfflineMode))
            .perform(click());

        // Verify description text changed
        onView(withId(R.id.textModeDescription))
            .check(matches(withText(R.string.offline_mode_description)));
    }

    @Test
    public void resultActivity_showsDiseaseDetails_afterDetection() {
        // Navigate directly to ResultActivity with known extras
        Intent intent = new Intent(
            InstrumentationRegistry.getInstrumentation().getTargetContext(),
            ResultActivity.class
        );
        intent.putExtra(ResultActivity.EXTRA_DISEASE_NAME, "Tomato Early Blight");
        intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, 0.91f);

        try (ActivityScenario<ResultActivity> scenario =
                 ActivityScenario.launch(intent)) {
            onView(withId(R.id.textDiseaseName))
                .check(matches(withText("Tomato Early Blight")));
            onView(withId(R.id.textConfidence))
                .check(matches(isDisplayed()));
        }
    }
}
```

**Espresso key methods:**
- `onView(withId(R.id.xxx))` — find view by ID
- `.perform(click())` — simulate user click
- `.check(matches(isDisplayed()))` — assert view is visible
- `.check(matches(withText("...")))` — assert text content
- `not(isEnabled())` — negation matcher

---

### Q58. What are edge cases? List 10 edge cases you tested in LeafGuard AI.

**Answer:**
Edge cases are unusual or extreme inputs/conditions that lie at the boundary of normal operation. Failing to handle them causes crashes or incorrect behaviour.

**10 edge cases in LeafGuard AI:**

1. **No image selected — tap Detect:** `buttonDetectDisease` must be disabled (enforced by `setEnabled(false)` until image is selected)

2. **Network request timeout (>30s):** OkHttp `connectTimeout(30, TimeUnit.SECONDS)` throws `SocketTimeoutException` → show "Connection timeout" message, offer offline mode

3. **Image file too large (>10MB):** Backend returns HTTP 413 → Android shows "Image too large" error, not a crash

4. **Very low confidence result (<40%):** Show warning "Uncertain result — please take a clearer photo" alongside the prediction

5. **Unknown disease class:** If `class_names[topIdx]` returns an unrecognised label → graceful fallback message

6. **Database full/disk full:** `Room.insert()` throws `SQLiteFullException` → show "Storage full" message, not a crash

7. **App killed during camera capture:** `pendingCameraUri` is null when activity recreates → check for null before processing camera result

8. **Empty disease library XML:** `parseDiseases()` returns empty list → show "Library unavailable" in DiseaseLibraryActivity, not a blank screen

9. **App runs on Android 5 (API 21):** `READ_MEDIA_IMAGES` permission doesn't exist on API < 33 → conditional permission logic using `Build.VERSION.SDK_INT`

10. **Landscape orientation during detection:** Activity is recreated → progress state is lost. Fix: use ViewModel to preserve `cloudMode` and `selectedImageUri` through configuration changes

---

### Q59. What is code coverage? How do you measure it for an Android project?

**Answer:**
**Code coverage** measures what percentage of your source code is executed during test runs. It identifies untested code paths.

**Types of coverage:**

| Type | What it measures | Example |
|------|-----------------|---------|
| Line coverage | % of lines executed | 85% of lines run |
| Branch coverage | % of if/else branches taken | 70% of branches tested |
| Method coverage | % of methods called | 90% of methods invoked |
| Statement coverage | % of statements executed | Similar to line coverage |

**Enabling coverage in Android:**

```groovy
// app/build.gradle
android {
    buildTypes {
        debug {
            testCoverageEnabled true
        }
    }
}
```

```bash
# Generate coverage report
./gradlew createDebugCoverageReport

# Output location:
# app/build/reports/coverage/debug/index.html
```

**Industry benchmarks:**
- Critical business logic: 80%+ coverage
- Android activities: Hard to reach 80% with unit tests alone (need UI tests)
- LeafGuard AI goal: 70%+ for parser, formatter, and business logic classes

**What good coverage does NOT guarantee:**
- Coverage shows you ran code — not that it is correct
- 100% coverage with bad assertions = false confidence
- Focus on meaningful tests, not chasing a number

---

### Q60. What is the difference between black-box testing and white-box testing?

**Answer:**

| Aspect | Black-Box Testing | White-Box Testing |
|--------|------------------|------------------|
| Knowledge of code | None (tester sees only inputs/outputs) | Full (tester sees source code) |
| Test design based on | Requirements, specifications, UI | Code paths, branches, conditions |
| Who performs | QA testers, end users (manual) | Developers |
| Finds | Missing features, UI bugs, wrong outputs | Logic errors, uncovered branches, dead code |
| Examples | Espresso UI tests, manual testing | JUnit unit tests, code review |

**Black-box testing LeafGuard AI (as an end user):**
```
Test: Select a clear tomato photo → tap Detect → verify disease name appears
Pass/Fail based entirely on: Did the result appear? Is it readable?
(No knowledge of how CNN inference works internally)
```

**White-box testing LeafGuard AI (as a developer):**
```java
// Tests specific code path: what happens when confidence < 0.5?
@Test
public void classify_lowConfidence_returnsUncertainFlag() {
    ClassificationResult result = new ClassificationResult("Blight", 0.35f);
    assertTrue(result.isUncertain());  // Tests specific branch in business logic
}
```

**In practice:** Good test suites combine both — Espresso for user-facing black-box validation, JUnit/Mockito for white-box logic testing.

---

## SECTION 8: DEPLOYMENT (5 Questions)

### Q61. What is an APK? How do you build and sign one for release?

**Answer:**
**APK (Android Package Kit)** is the file format for distributing and installing Android applications. It is a ZIP archive containing compiled code, resources, assets, and a manifest.

**APK contents:**
```
my-app.apk (ZIP archive)
├── classes.dex          ← Compiled Java/Kotlin bytecode (Dalvik format)
├── AndroidManifest.xml  ← App metadata (compiled binary format)
├── res/                 ← Compiled resources (layouts, drawables)
├── assets/              ← Raw assets (model files, fonts, XML databases)
├── lib/                 ← Native .so libraries (for each ABI)
└── META-INF/            ← Signature files
    ├── CERT.SF
    └── CERT.RSA
```

**Build process:**

```bash
# 1. Generate release APK via Gradle
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

**Signing the APK (required for Play Store and sideloading):**

```bash
# Step 1: Generate keystore (do once, keep secure)
keytool -genkeypair \
    -alias leafguard_key \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000 \
    -keystore leafguard-release-key.jks

# Step 2: Sign the APK
jarsigner \
    -verbose \
    -keystore leafguard-release-key.jks \
    -signedjar app-release-signed.apk \
    app-release-unsigned.apk \
    leafguard_key

# Step 3: Align the APK (required for Play Store)
zipalign -v 4 app-release-signed.apk app-release-final.apk
```

**Or configure signing in build.gradle:**
```groovy
android {
    signingConfigs {
        release {
            storeFile file("leafguard-release-key.jks")
            storePassword System.getenv("KEYSTORE_PASS")  // From environment
            keyAlias "leafguard_key"
            keyPassword System.getenv("KEY_PASS")
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
        }
    }
}
```

**Debug vs Release APK:**
- Debug: Debuggable, not optimised, signed with debug keystore automatically
- Release: Not debuggable, ProGuard minification, must be signed with your release keystore

---

### Q62. What is ProGuard/R8? Why is it used in release builds?

**Answer:**
**ProGuard/R8** is a code shrinker, obfuscator, and optimiser that runs on release builds. R8 is the modern replacement for ProGuard (built into Android Gradle Plugin).

**Three functions:**

**1. Shrinking — removes unused code:**
```
Before R8: app + all libraries = 25 MB
After R8:  only used code remains = 8 MB
```

**2. Obfuscation — renames classes/methods to short names:**
```java
// Before obfuscation:
public class TFLiteClassifier {
    public ClassificationResult classifyImage(Bitmap bitmap) { ... }
}

// After obfuscation (in decompiled APK):
public class a {
    public b c(d e) { ... }
}
```

**3. Optimisation — removes dead code paths, inlines short methods:**
```java
// Dead code removed:
if (BuildConfig.DEBUG) {  // False in release
    Log.d(TAG, "Debug only");  // Entire block removed
}
```

**Enabling in LeafGuard AI:**
```groovy
buildTypes {
    release {
        minifyEnabled true    // Enables R8 shrinking + obfuscation
        shrinkResources true  // Remove unused resource files too
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'),
                      'proguard-rules.pro'
    }
}
```

**ProGuard rules to keep (proguard-rules.pro):**
```
# Keep Room entities (Room uses reflection)
-keep class com.leafguard.database.** { *; }

# Keep Retrofit interface (uses reflection)
-keep interface com.leafguard.network.ApiService { *; }

# Keep Gson models
-keep class com.leafguard.network.PredictionResponse { *; }

# Keep TFLite classes
-keep class org.tensorflow.lite.** { *; }
```

---

### Q63. What is the difference between minSdkVersion, targetSdkVersion, and compileSdkVersion?

**Answer:**

| Version | Set in | Meaning |
|---------|--------|---------|
| `minSdkVersion` | `defaultConfig` | Minimum Android version app supports. Devices below this cannot install the app. |
| `targetSdkVersion` | `defaultConfig` | Android version you've tested against. Affects system behaviour (permissions, etc.). |
| `compileSdkVersion` | top-level | Android SDK version used to compile the source code. Must be latest. |

**LeafGuard AI configuration:**
```groovy
android {
    compileSdk 34       // Compile with Android 14 APIs — use latest
    defaultConfig {
        minSdk 24       // Android 7.0 — covers 95%+ of active devices
        targetSdk 34    // Tested on Android 14
    }
}
```

**Why minSdkVersion 24?**
- `FileProvider`, `ActivityResultContracts`, `MediaStore.Images.Media.EXTERNAL_CONTENT_URI` work reliably from API 24+
- Below API 24: only ~5% of active Android users (not worth supporting)
- API 21 (Android 5) added many required features, but API 24 is a cleaner baseline

**targetSdkVersion behaviour changes:**
- `targetSdk >= 23`: Runtime permissions required (camera, storage)
- `targetSdk >= 26`: Background service restrictions
- `targetSdk >= 29`: Scoped storage enforced
- `targetSdk >= 31`: Exact alarms require permission
- `targetSdk >= 33`: `READ_MEDIA_IMAGES` instead of `READ_EXTERNAL_STORAGE`

**LeafGuard AI handles this:**
```java
// Version-aware permission request (from MainActivity.java)
private String[] requiredGalleryPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return new String[]{Manifest.permission.READ_MEDIA_IMAGES};
    }
    return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
}
```

---

### Q64. How do you publish an app to the Google Play Store? What are the requirements?

**Answer:**
Google Play is the primary distribution channel for Android apps.

**Requirements before publishing:**

1. **Google Play Developer account:** One-time $25 USD registration fee
2. **Signed APK or AAB:** Must be signed with a release keystore
3. **Target API level:** Must target `targetSdk >= 33` (as of 2024, Play requires latest-1 or later)
4. **Privacy policy URL:** Required if app requests permissions
5. **Content rating:** Complete questionnaire
6. **Screenshots and graphics:** At minimum 2 screenshots + feature graphic (1024×500)

**AAB vs APK for Play Store:**
```bash
# Build AAB (recommended for Play Store)
./gradlew bundleRelease
# Output: app/build/outputs/bundle/release/app-release.aab

# Play Store delivers device-optimised APK splits from AAB
# Users download only resources for their screen density and CPU architecture
# Saves 15-40% download size vs universal APK
```

**Minimum Play Store checklist for LeafGuard AI:**
- [ ] Signed AAB generated
- [ ] App description (10-4000 chars)
- [ ] 2+ screenshots (320dp × 320dp to 3840dp × 3840dp)
- [ ] App icon (512×512 PNG, no alpha)
- [ ] Category: Medical or Health & Fitness
- [ ] Content rating: Everyone
- [ ] Privacy policy URL (required — app uses CAMERA permission)
- [ ] Contact email

---

### Q65. What is version code and version name? How do you manage them in LeafGuard AI?

**Answer:**

| Field | Type | Purpose | Example |
|-------|------|---------|---------|
| `versionCode` | Integer | Internal version number. Must increase with every Play Store upload. | `1`, `2`, `3` |
| `versionName` | String | Human-readable version shown to users. | `"1.0.0"`, `"1.2.3"` |

**Configuration:**
```groovy
// app/build.gradle
defaultConfig {
    versionCode 1            // Increment every release (Play Store requires)
    versionName "1.0.0"      // Semantic versioning: MAJOR.MINOR.PATCH
}
```

**Semantic Versioning (SemVer) for LeafGuard AI:**
```
1.0.0   ← Initial release
1.0.1   ← Bug fix (patch: fixes crash on Android 8)
1.1.0   ← New feature (minor: added offline TFLite mode)
2.0.0   ← Breaking change (major: complete UI redesign)
```

**Checking version in code:**
```java
// Display current version to user in Settings or About screen
try {
    PackageInfo pInfo = getPackageManager()
        .getPackageInfo(getPackageName(), 0);
    String versionName = pInfo.versionName;     // "1.0.0"
    int versionCode = pInfo.versionCode;         // 1
    binding.textVersion.setText(
        getString(R.string.version_format, versionName)
    );
} catch (PackageManager.NameNotFoundException e) {
    Log.e(TAG, "Version check failed", e);
}
```

**Best practices:**
- Never publish same versionCode twice to Play Store (upload will fail)
- Tag Git commits with version: `git tag v1.0.0`
- Keep `versionCode` simple (auto-increment); keep `versionName` user-friendly
- Document changelog in Play Store listing for each new version

---

## SECTION 9: PROJECT-SPECIFIC (10 Questions)

### Q66. What problem does LeafGuard AI solve? Why is this important?

**Answer:**
LeafGuard AI addresses the challenge of **timely and accurate plant disease diagnosis** for farmers, especially in resource-limited settings.

**The problem:**
- Global crop losses due to plant diseases and pests: estimated **40% of potential food production** annually (FAO)
- In developing countries, smallholder farmers lack easy access to agricultural extension officers
- Traditional diagnosis requires a trained agronomist to physically inspect the plant
- By the time a disease is professionally diagnosed, it may have spread to the entire crop

**What LeafGuard AI does:**
- Mobile app that runs on any mid-range Android smartphone
- Farmer takes a photo of a diseased leaf
- ML model identifies the disease within seconds
- App provides treatment advice and prevention tips immediately

**Why mobile-first?**
- Mobile phone penetration in rural India: 85%+ (smartphones 40-50%)
- No internet required for core functionality (TFLite offline mode)
- Works in fields, no need for laboratory equipment

**Academic relevance (CSE 2206):**
- Demonstrates complete software engineering lifecycle: requirement → design → implementation → testing → deployment
- Integrates Android, REST APIs, ML, databases — all CSE 2206 topics
- Hybrid architecture (cloud + on-device AI) is industry-relevant

---

### Q67. Explain the architecture of LeafGuard AI. Draw and describe each component.

**Answer:**
LeafGuard AI follows a **three-tier client-server architecture** with an additional on-device AI tier for offline operation.

```
┌─────────────────────────────────────────────┐
│              ANDROID APP                     │
│  ┌─────────────────────────────────────┐    │
│  │         Presentation Layer           │    │
│  │  MainActivity → ResultActivity      │    │
│  │  HistoryActivity → DiseaseLibrary   │    │
│  └──────────────┬──────────────────────┘    │
│                 │                            │
│  ┌──────────────▼──────────────────────┐    │
│  │          Business Logic              │    │
│  │   TFLiteClassifier (offline AI)     │    │
│  │   RetrofitClient (network)          │    │
│  │   DiseaseXmlParser                  │    │
│  └──────────────┬──────────────────────┘    │
│                 │                            │
│  ┌──────────────▼──────────────────────┐    │
│  │            Data Layer               │    │
│  │   Room Database (scan history)      │    │
│  │   SharedPreferences (settings)      │    │
│  └──────────────┬──────────────────────┘    │
└─────────────────┼───────────────────────────┘
                  │ HTTP/JSON (Retrofit)
                  │ Online mode only
┌─────────────────▼───────────────────────────┐
│           FASTAPI BACKEND                    │
│   /predict endpoint                          │
│   Image validation → preprocessing →        │
│   TensorFlow/Keras model → JSON response    │
└─────────────────────────────────────────────┘
```

**Data flow (online mode):**
1. User selects image in MainActivity
2. Retrofit sends multipart/form-data POST to `/predict`
3. FastAPI validates → preprocesses → runs `model.predict()`
4. JSON response: `{"disease": "Early Blight", "confidence": 0.91, ...}`
5. ResultActivity displays disease name, confidence, treatment
6. ScanRecord saved to Room database

**Data flow (offline mode):**
1. User selects image in MainActivity
2. TFLiteClassifier loads from `assets/plant_disease.tflite`
3. Bitmap preprocessed → run inference on device CPU
4. Same result display and database save flow

---

### Q68. What design patterns did you use in LeafGuard AI? Justify each choice.

**Answer:**

**1. Singleton Pattern — AppDatabase:**
```java
// Ensures only one database connection exists in the app
public static AppDatabase getInstance(Context context) {
    if (INSTANCE == null) {
        synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(...).build();
            }
        }
    }
    return INSTANCE;
}
```
*Why:* Database connections are expensive to create. Only one connection object should exist to prevent data corruption from concurrent writes.

**2. Singleton Pattern — RetrofitClient:**
```java
// Single Retrofit instance shared across the app
public class RetrofitClient {
    private static Retrofit instance;
    public static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder().baseUrl(BASE_URL)...build();
        }
        return instance;
    }
}
```
*Why:* Retrofit client creation involves configuring OkHttp, converters, timeouts — expensive to repeat.

**3. Adapter Pattern — RecyclerView Adapter:**
The `ScanHistoryAdapter` adapts `List<ScanRecord>` (data) to RecyclerView's view-item interface.
*Why:* RecyclerView works with any data type through an adapter, decoupling the data source from the list UI.

**4. Observer Pattern — LiveData:**
Activities observe LiveData from the database. When data changes, observers are automatically notified.
*Why:* Decouples data layer from UI. UI doesn't need to know when/how data changes — just observe and react.

**5. Strategy Pattern — Detection Mode:**
The `cloudMode` flag selects between two detection strategies (network API vs TFLite) at runtime without changing the calling code structure.
*Why:* The user can switch modes without the code needing to know how each mode works internally.

---

### Q69. What were the main challenges you faced while building LeafGuard AI? How did you solve them?

**Answer:**

**Challenge 1: Camera URI and FileProvider**
- *Problem:* Camera Intent requires a pre-created file URI. Starting from Android 7.0, file:// URIs are blocked — must use FileProvider.
- *Solution:* Used `FileProvider.getUriForFile()` with proper `file_provider_paths.xml` configuration.
- *Learning:* Android security model evolved; modern apps must use content URIs for file sharing.

**Challenge 2: Multipart file upload with Retrofit**
- *Problem:* Sending binary image data as part of an HTTP request requires specific encoding.
- *Solution:* Used `RequestBody.create(file, MediaType.parse("image/*"))` and `MultipartBody.Part.createFormData()`.
- *Learning:* HTTP multipart/form-data is the standard for file uploads; Retrofit abstracts the complexity.

**Challenge 3: Room database on main thread**
- *Problem:* Room throws `IllegalStateException` if database operations run on UI thread.
- *Solution:* Wrapped all database calls in `new Thread(() -> { ... runOnUiThread(() -> { ... }) })`.
- *Learning:* Android strictly enforces background threading for I/O operations to prevent ANR.

**Challenge 4: TFLite model integration**
- *Problem:* The model file needs to be loaded from assets, and input/output tensor formats must exactly match training.
- *Solution:* Read model specifications (224×224 RGB, float32, softmax output) and implemented precise preprocessing.
- *Learning:* Model input/output contract is as important as the model itself.

**Challenge 5: Network security for HTTP on Android 9+**
- *Problem:* Android 9 (Pie) blocks cleartext HTTP by default.
- *Solution:* Added `network_security_config.xml` allowing cleartext to specific development IP.
- *Learning:* Production deployment should use HTTPS; development exemptions are acceptable.

---

### Q70. How does the hybrid (cloud + offline) detection work? What are the trade-offs?

**Answer:**

**Implementation:**
```java
// MainActivity.java — mode selection
private boolean cloudMode = true;

binding.toggleDetectionMode.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
    if (isChecked) cloudMode = (checkedId == R.id.buttonCloudMode);
    updateModeDescription();
});

// Detection dispatch
private void launchDetection() {
    if (cloudMode) {
        uploadToCloudAndDetect();  // Retrofit → FastAPI
    } else {
        detectOffline();            // TFLiteClassifier on device
    }
}
```

**Trade-off comparison:**

| Factor | Cloud Mode (FastAPI) | Offline Mode (TFLite) |
|--------|---------------------|----------------------|
| Model accuracy | Higher (larger model, no size limit) | Good (quantised, smaller) |
| Speed | 1-3 seconds (network + server) | 100-500ms (on-device) |
| Internet required | Yes | No |
| Model updates | Instant (update server) | Requires app update |
| Battery drain | High (mobile data) | Moderate (CPU) |
| Privacy | Image sent to server | Image stays on device |
| Scalability | Server can handle many users | Each device is independent |

**When to use offline mode:**
- Rural areas with poor connectivity
- When privacy is a concern (sensitive agricultural IP)
- Rapid scanning of many plants (no network latency)

**When to use cloud mode:**
- When highest accuracy is needed
- Complex diseases requiring larger model
- Connectivity is reliable

---

### Q71. How would you add user authentication to LeafGuard AI?

**Answer:**
Authentication would enable personal scan history sync across devices and team/farm sharing.

**Backend (FastAPI JWT authentication):**
```python
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from jose import JWTError, jwt
from passlib.context import CryptContext
from datetime import datetime, timedelta

SECRET_KEY = "your-256-bit-secret"
ALGORITHM = "HS256"
TOKEN_EXPIRE_MINUTES = 60 * 24  # 24 hours

pwd_context = CryptContext(schemes=["bcrypt"])
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="auth/login")

@app.post("/auth/login")
async def login(form_data: OAuth2PasswordRequestForm = Depends()):
    user = authenticate_user(form_data.username, form_data.password)
    if not user:
        raise HTTPException(status_code=401, detail="Incorrect credentials")
    access_token = create_access_token({"sub": user.username})
    return {"access_token": access_token, "token_type": "bearer"}

@app.post("/predict")
async def predict(
    file: UploadFile = File(...),
    current_user: User = Depends(get_current_user)  # Requires valid JWT
):
    ...
```

**Android (storing and sending JWT):**
```java
// Store token securely using EncryptedSharedPreferences
EncryptedSharedPreferences prefs = EncryptedSharedPreferences.create(
    "auth_prefs", masterKey, context,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
);
prefs.edit().putString("jwt_token", token).apply();

// Send token with every request (OkHttp interceptor)
class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = prefs.getString("jwt_token", null);
        Request request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer " + token)
            .build();
        return chain.proceed(request);
    }
}
```

---

### Q72. What future enhancements would you make to LeafGuard AI?

**Answer:**
Several high-value enhancements are possible:

**Near-term (technical):**

1. **Multi-language support:** Add Bengali, Hindi, Tamil translations using `res/values-bn/strings.xml`. Disease names in local language increase usability for farmers.

2. **Improved model accuracy:** Fine-tune on local crop varieties. The PlantVillage dataset uses mostly North American crop images; local data would improve accuracy for Indian/Asian crops.

3. **GPS-tagged scan history:** Save `latitude/longitude` with each scan to create disease outbreak heat maps. Farmers in a region could be warned about spreading disease.

4. **Push notifications:** Alert users when a nearby farm reports a new disease outbreak (requires user accounts and location sharing consent).

5. **DiseaseLibraryActivity:** Browse all 38 disease entries offline, with search and filter by plant type.

**Medium-term (architecture):**

6. **Cloud sync:** Room database sync to Firebase Firestore — scan history accessible from multiple devices.

7. **Treatment tracking:** Allow users to mark treatments applied and follow up to verify effectiveness.

8. **Federated learning:** Privacy-preserving model improvement — devices contribute model gradients without sharing raw images.

**Long-term (impact):**

9. **Government API integration:** Connect with India's National Agricultural Portal for official pest advisories.

10. **Expert consultation:** When confidence < 50%, offer "Request expert review" that sends the anonymised image to an agronomist network.

---

### Q73. Explain the folder structure of your project. Why is it organised this way?

**Answer:**
Clean folder structure separates concerns and makes the codebase maintainable as it grows.

**Android app structure:**
```
android-app/
└── app/src/main/
    ├── java/com/leafguard/
    │   ├── MainActivity.java           ← Entry screen
    │   ├── ResultActivity.java         ← Shows detection result
    │   ├── HistoryActivity.java        ← List of past scans
    │   ├── DiseaseLibraryActivity.java ← Offline disease reference
    │   ├── HistoryDetailActivity.java  ← Single scan detail
    │   ├── SettingsActivity.java       ← App preferences
    │   ├── database/
    │   │   ├── AppDatabase.java        ← Room database
    │   │   ├── ScanDao.java            ← SQL queries
    │   │   └── ScanRecord.java         ← Table entity
    │   ├── ml/
    │   │   └── TFLiteClassifier.java   ← On-device inference
    │   ├── network/
    │   │   ├── ApiService.java         ← Retrofit interface
    │   │   ├── RetrofitClient.java     ← OkHttp + Retrofit setup
    │   │   └── PredictionResponse.java ← JSON response model
    │   └── utils/
    │       └── NotificationHelper.java  ← Notification channel setup
    └── res/
        ├── layout/                     ← XML UI layouts
        ├── values/strings.xml          ← All user-facing text
        ├── values/colors.xml           ← Colour palette
        └── xml/                        ← Config XML files
```

**Backend structure:**
```
backend-api/
├── main.py         ← FastAPI app, routes
├── model_loader.py ← Model loading and inference logic
├── config.py       ← Settings from environment variables
└── requirements.txt ← Python dependencies
```

**Why this structure:**
- **Separation by layer** (`database/`, `network/`, `ml/`): changes in one layer don't cascade through unrelated code
- **Clear naming**: Any developer unfamiliar with the project can find relevant code quickly
- **Testability**: Each package/class can be tested independently

---

### Q74. How did you handle image quality and preprocessing challenges?

**Answer:**

**Challenge:** Users capture photos in varied conditions — different lighting, angles, distances, and backgrounds. A model trained on controlled PlantVillage images may fail on real-world photos.

**Preprocessing steps implemented:**

**Backend (Python):**
```python
from PIL import Image, ImageEnhance
import numpy as np

def preprocess_image(image: Image.Image) -> np.ndarray:
    # 1. Convert to RGB (handles RGBA, grayscale, CMYK uploads)
    image = image.convert("RGB")

    # 2. Resize to model input size using high-quality resampling
    image = image.resize((224, 224), Image.LANCZOS)

    # 3. Convert to NumPy float array
    img_array = np.array(image, dtype=np.float32)

    # 4. Normalise to [0, 1]
    img_array = img_array / 255.0

    # 5. Add batch dimension
    img_array = np.expand_dims(img_array, axis=0)

    return img_array
```

**Android (Java — TFLite offline):**
```java
private float[][][][] preprocessBitmap(Bitmap bitmap) {
    // 1. Resize maintaining aspect ratio, then center-crop to 224×224
    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

    float[][][][] input = new float[1][224][224][3];
    for (int y = 0; y < 224; y++) {
        for (int x = 0; x < 224; x++) {
            int pixel = resized.getPixel(x, y);
            // Extract and normalise each channel
            input[0][y][x][0] = ((pixel >> 16) & 0xFF) / 255.0f;  // Red
            input[0][y][x][1] = ((pixel >> 8)  & 0xFF) / 255.0f;  // Green
            input[0][y][x][2] = (pixel & 0xFF)          / 255.0f;  // Blue
        }
    }
    return input;
}
```

**User guidance for better results:**
- Display in-app tip: "Place the diseased leaf on a plain background"
- Add confidence threshold warning: below 50% confidence → "Take a clearer photo"
- Crop guidance overlay on camera screen (future enhancement)

---

### Q75. What did you learn from building this project? How does it relate to your CSE 2206 syllabus?

**Answer:**

**Technical skills gained:**

1. **Android Development:** Full lifecycle of an Android app — UI design, camera integration, background threading, permissions, database persistence

2. **REST API Design:** RESTful principles applied in practice — correct HTTP verbs, status codes, JSON schemas, error handling

3. **Machine Learning Deployment:** Understanding the gap between "training a model" and "deploying it in a production app" — preprocessing contracts, TFLite conversion, quantisation trade-offs

4. **Database Design:** Practical SQLite schema design, migration strategies, the importance of background threading

5. **Software Architecture:** Multi-tier architecture with clear separation of concerns makes the codebase maintainable and testable

**CSE 2206 syllabus connections:**

| Syllabus Topic | Where demonstrated in LeafGuard AI |
|---------------|-------------------------------------|
| Mobile app development basics | MainActivity, Activity lifecycle, AndroidManifest |
| UI/UX design | ConstraintLayout, RecyclerView, Material Design |
| Network programming | Retrofit, OkHttp, REST API consumption |
| Database management | Room, SQLite, DAO pattern, migrations |
| Security | Runtime permissions, FileProvider, signed APK |
| Testing | JUnit unit tests, Espresso UI tests, Mockito mocks |
| Software engineering | Architecture patterns, MVVM, Singleton |
| Emerging technologies | Machine learning on mobile, TFLite, FastAPI |

**Soft skills gained:**
- Breaking a large, complex project into weekly deliverables
- Reading documentation and Stack Overflow effectively
- Debugging systematically (logcat, breakpoints, network inspection)
- Version control with Git (meaningful commits, branches per feature)

**Honest reflection:** The hardest part was not any single technical problem but sustaining motivation and focus through 12 weeks of incremental work. Completing a project of this scope is itself a significant achievement.

---

## PREPARATION TIPS

1. **Understand, Don't Memorize:** Focus on concepts, not rote learning
2. **Practice Explanations:** Explain concepts to friends/mirror
3. **Review Your Own Code:** Be ready to explain any line in your project
4. **Prepare Diagrams:** Draw architecture on whiteboard during viva
5. **Know Trade-offs:** Why you chose X over Y
6. **Be Honest:** If you don't know, say so - don't bluff
7. **Connect to Syllabus:** Relate answers to CSE 2206 topics
8. **Prepare Demo:** Have working app ready to demonstrate
9. **Review Latest Topics:** Jetpack Compose, Flow (bonus points)
10. **Stay Calm:** Take deep breath, think before answering

---

**Good luck with your viva! Your comprehensive project demonstrates excellent understanding of mobile application development.**
