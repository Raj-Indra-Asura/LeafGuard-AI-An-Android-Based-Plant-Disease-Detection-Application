# Week 01 Learning Notes: Project Understanding & Foundation

## Table of Contents
1. [What is LeafGuard AI?](#what-is-leafguard-ai)
2. [Three-Tier Architecture Deep Dive](#three-tier-architecture-deep-dive)
3. [MVVM Pattern Explained](#mvvm-pattern-explained)
4. [Android Components in LeafGuard](#android-components-in-leafguard)
5. [Networking Architecture](#networking-architecture)
6. [Database Design](#database-design)
7. [Machine Learning Integration](#machine-learning-integration)
8. [XML Parsing Fundamentals](#xml-parsing-fundamentals)
9. [Senior Repository Analysis Insights](#senior-repository-analysis-insights)
10. [Project Documentation Best Practices](#project-documentation-best-practices)

---

## What is LeafGuard AI?

### Problem Context

**Agricultural Challenge:**
Plant diseases cause 20-40% crop yield losses globally. Farmers, especially in rural India, lack access to:
- Plant pathologists for disease identification
- Timely diagnosis leading to early intervention
- Affordable diagnostic solutions
- Educational resources on disease management

**Traditional Approach:**
- Visual inspection by experts
- Time-consuming (days to weeks)
- Expensive (₹500-₹2000 per consultation)
- Limited availability in remote areas
- Subjective (varies between experts)

### Solution: LeafGuard AI

**Core Concept:**
A smartphone-based plant disease detection system that brings expert-level diagnostics to farmers' hands.

**How it works:**
1. Farmer captures or selects leaf image using phone camera
2. App analyzes image using deep learning model
3. Disease identified with confidence score
4. Treatment recommendations provided immediately
5. Scan saved to history for future reference

**Unique Features:**
- **Hybrid AI:** Works both online (cloud) and offline (on-device)
- **Local History:** All scans saved in phone database
- **Educational:** Disease library with symptoms, treatments, prevention
- **Fast:** Results in under 5 seconds
- **Free:** No subscription, no hidden costs

### Technical Architecture Overview

```
┌─────────────────────────────────────────────┐
│           USER'S ANDROID PHONE              │
│                                             │
│  ┌────────────────────────────────────┐    │
│  │   LeafGuard AI Application         │    │
│  │   (6 Activities, MVVM Pattern)     │    │
│  └───────────┬────────────────────────┘    │
│              │                              │
│              ├──→ Room Database (SQLite)    │
│              │    └─ Scan History           │
│              │                              │
│              ├──→ TFLite Model (Assets)     │
│              │    └─ Offline AI             │
│              │                              │
│              └──→ Retrofit Client           │
│                   └─ HTTP to Backend        │
└──────────────────────┬──────────────────────┘
                       │ Internet
              ┌────────▼─────────┐
              │  FASTAPI BACKEND │
              │  ┌─────────────┐ │
              │  │ ML Model    │ │
              │  │ (TensorFlow)│ │
              │  └─────────────┘ │
              └──────────────────┘
```

### Project Scope Summary

**What LeafGuard DOES:**
- ✅ Detect 10-15 plant diseases from leaf images
- ✅ Provide treatment recommendations
- ✅ Save scan history locally
- ✅ Work offline with TensorFlow Lite
- ✅ Work online with cloud AI for better accuracy
- ✅ Display disease information library
- ✅ Handle camera and gallery integration

**What LeafGuard DOES NOT:**
- ❌ User authentication (out of scope for CSE 2206)
- ❌ Multi-language support (future enhancement)
- ❌ Community features (forums, sharing)
- ❌ E-commerce (selling treatments)
- ❌ Weather integration
- ❌ GPS disease mapping
- ❌ Real-time video analysis

---

## Three-Tier Architecture Deep Dive

### What is Three-Tier Architecture?

**Definition:** A software architecture pattern that separates an application into three logical and physical computing tiers.

**Why use it?**
- **Separation of concerns:** Each tier has distinct responsibility
- **Maintainability:** Changes in one tier minimally affect others
- **Scalability:** Each tier can be scaled independently
- **Testability:** Tiers can be tested in isolation
- **Team collaboration:** Different developers can work on different tiers

### Tier 1: Presentation Layer (UI Layer)

**Responsibility:** Everything the user sees and interacts with.

**Components in LeafGuard:**

1. **Activities (Screens):**
   - `MainActivity.kt` - Home screen with scan button + image capture
   - `ResultActivity.kt` - Display disease prediction
   - `HistoryActivity.kt` - List of past scans
   - `HistoryDetailActivity.kt` - Single scan details view
   - `DiseaseLibraryActivity.kt` - Browse disease information
   - `SettingsActivity.kt` - App preferences

2. **XML Layouts:**
   - `activity_main.xml` - Main screen layout
   - `activity_result.xml` - Result display layout
   - `activity_history.xml` - History list layout
   - `item_scan_history.xml` - Single history item layout

3. **Adapters (RecyclerView):**
   - `HistoryAdapter.kt` - Displays list of scans
   - `DiseaseAdapter.kt` - Displays list of diseases

**Data Flow IN:** User interactions (button clicks, image selection)
**Data Flow OUT:** Display commands to UI components

**Example: MainActivity Flow**
```kotlin
// MainActivity.kt (Presentation Layer)
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Load XML layout
        setContentView(binding.root)

        // Handle user interaction
        binding.btnScan.setOnClickListener {
            // User clicked scan button - launch camera directly
            openCamera()
        }
        
        binding.btnHistory.setOnClickListener {
            // Navigate to HistoryActivity
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}
```

**Key Principles:**
- Activities should NOT directly access database
- Activities should NOT make network calls
- Activities should only update UI based on ViewModel data
- Activities should be thin (minimal logic)

### Tier 2: Business Logic Layer (Domain Layer)

**Responsibility:** Application logic, data processing, network/database services.

**Components in LeafGuard:**

1. **Network Services:**
   - `ApiService.kt` - Retrofit interface for API calls
   - `RetrofitClient.kt` - HTTP client configuration
   - `PredictionResponse.kt` - API response model

2. **ML Services:**
   - `TFLiteClassifier.kt` - Offline model inference

3. **Utility Classes:**
   - `NotificationHelper.kt` - Push notification management
   - `XmlParser.kt` - Parse diseases.xml file

**Data Flow IN:** Requests from Activities
**Data Flow OUT:** Processed data back to Activities, network/database operations

**Example: MainActivity calling ApiService**
```kotlin
// MainActivity.kt - Direct API call (no ViewModel layer)
class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Retrofit service
        apiService = RetrofitClient.instance.create(ApiService::class.java)
    }

    // Called when user selects image
    private fun uploadImage(imageUri: Uri) {
        lifecycleScope.launch {
            try {
                // Prepare multipart request
                val file = File(imageUri.path!!)
                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

                // Make API call
                val response = apiService.predict(imagePart)

                if (response.isSuccessful) {
                    val result = response.body()
                    // Navigate to ResultActivity with result
                    val intent = Intent(this@MainActivity, ResultActivity::class.java)
                    intent.putExtra("disease", result?.disease)
                    intent.putExtra("confidence", result?.confidence)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
```

**Key Principles:**
- Activities directly call service layer (no ViewModel intermediary)
- Network calls use Kotlin coroutines for async operations
- Results are passed via Intent extras or saved to database
- Simple architecture suitable for this project's scope

### Tier 3: Data Layer

**Responsibility:** Data storage, retrieval, and network communication.

**Components in LeafGuard:**

1. **Local Data Sources:**
   - `AppDatabase.kt` - Room database definition
   - `ScanDao.java` - Database access methods
   - `ScanEntity.java` - Database table schema
   - `ScanRecord.kt` - Room entity for scan history
   - `ScanDao.kt` - Room DAO for database operations
   - `XmlParser.kt` - Parse diseases.xml file

2. **Remote Data Sources:**
   - `ApiService.kt` - Retrofit interface defining API endpoints
   - `RetrofitClient.kt` - Retrofit singleton configuration
   - `PredictionResponse.kt` - API response data class

3. **Data Models:**
   - `Disease.kt` - Disease information model

**Data Flow IN:** Requests from Activities
**Data Flow OUT:** Raw data (database records, API responses)

**Example: ApiService and Database Access**
```kotlin
// ApiService.kt - Retrofit interface
interface ApiService {
    @Multipart
    @POST("/predict")
    suspend fun predict(
        @Part image: MultipartBody.Part
    ): Response<PredictionResponse>
}

// PredictionResponse.kt - API response model
data class PredictionResponse(
    @SerializedName("disease") val disease: String,
    @SerializedName("confidence") val confidence: Float
)

// ScanRecord.kt - Room entity
@Entity(tableName = "scan_history")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "disease_name") val diseaseName: String,
    @ColumnInfo(name = "confidence") val confidence: Float,
    @ColumnInfo(name = "image_path") val imagePath: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)

// ScanDao.kt - Room DAO
@Dao
interface ScanDao {
    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    suspend fun getAllScans(): List<ScanRecord>

    @Insert
    suspend fun insert(scan: ScanRecord)

    @Delete
    suspend fun delete(scan: ScanRecord)
}
```

**Key Principles:**
- Activities call service layer directly (no Repository intermediary in this app)
- Room handles local SQLite storage
- Retrofit handles network communication
- Coroutines manage async operations
- Repositories handle data synchronization

### Why Three Tiers for LeafGuard?

**Scenario: Changing Network Library**

Without tiers:
```java
// MainActivity directly uses Retrofit - BAD!
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Network call in Activity - very bad!
        RetrofitClient.getApiService().uploadImage(image).enqueue(...);
    }
}
```

If you switch from Retrofit to Volley, you must change EVERY Activity that makes network calls.

With tiers:
```kotlin
// MainActivity uses service layer directly
class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = RetrofitClient.instance.create(ApiService::class.java)
        // Activity calls service directly
    }
}
```

If you switch from Retrofit to Volley, you ONLY change `ApiService` and `RetrofitClient` in the Service Layer. Activities just need different method calls.

---

## Direct Architecture Pattern Explained

### What is the Direct Pattern?

**LeafGuard uses a simpler, direct Activity-to-Service architecture.**

**Purpose:** Activities directly call service layer (ApiService, ScanDao, TFLiteClassifier) without ViewModel intermediary.

### Why Not MVVM for This Project?

1. **Simpler for learning** - Fewer abstractions to understand
2. **Appropriate scope** - Single-user, single-flow app
3. **Course focus** - CSE 2206 emphasizes Android fundamentals, not advanced architecture
4. **Faster development** - Less boilerplate code

### LeafGuard Architecture Components

#### 1. Model (Data Classes)

**What it is:** Your data structures.

**In LeafGuard:**
- `ScanRecord.kt` - Database entity for scan history
- `PredictionResponse.kt` - API response data class
- `Disease.kt` - Disease information from XML

**Example:**
```kotlin
// ScanRecord.kt (Model - Room Entity)
@Entity(tableName = "scan_history")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "disease_name") val diseaseName: String,
    @ColumnInfo(name = "confidence") val confidence: Float,
    @ColumnInfo(name = "image_path") val imagePath: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)
```

**Responsibilities:**
- Define data structure
- Represent business entities
- No UI code
- No business logic

#### 2. View (Activities)

**What it is:** Your UI components (Activities and XML layouts).

**In LeafGuard:**
- `MainActivity.kt` - Handles capture and API calls directly
- `ResultActivity.kt` - Displays result passed via Intent
- `HistoryActivity.kt` - Loads history from database directly

**Example:**
```kotlin
// MainActivity.kt (View - handles everything)
class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize services
        apiService = RetrofitClient.instance.create(ApiService::class.java)
        database = AppDatabase.getInstance(this)

        // User clicks scan button
        binding.btnScan.setOnClickListener {
            openCamera() // Launch camera intent
        }
    }
    
    // Camera result callback
    private fun handleCapturedImage(imageUri: Uri) {
        lifecycleScope.launch {
            val result = apiService.predict(createImagePart(imageUri))
            if (result.isSuccessful) {
                // Navigate to ResultActivity with data
                val intent = Intent(this@MainActivity, ResultActivity::class.java)
                intent.putExtra("disease", result.body()?.disease)
                startActivity(intent)
            }
        }
    }
}
```

**Responsibilities:**
- Display UI
- Handle user interactions (clicks, swipes)
- Call service layer directly
- Update UI when data arrives
- Contains flow logic

#### 3. Service Layer

**What it is:** Network and database access classes.

**In LeafGuard:**
- `ApiService.kt` - Retrofit interface
- `ScanDao.kt` - Room DAO
- `TFLiteClassifier.kt` - ML model wrapper

**Example:**
```kotlin
// ApiService.kt (Service - network)
interface ApiService {
    @Multipart
    @POST("/predict")
    suspend fun predict(
        @Part image: MultipartBody.Part
    ): Response<PredictionResponse>
}

// ScanDao.kt (Service - database)
@Dao
interface ScanDao {
    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    suspend fun getAllScans(): List<ScanRecord>

    @Insert
    suspend fun insert(scan: ScanRecord)
}
```

### Direct Architecture Data Flow in LeafGuard

**User scans a leaf - complete flow:**

```
1. USER INTERACTION
   User taps "Scan" button
   ↓

2. ACTIVITY (MainActivity)
   Button click listener triggered
   Launches camera via ActivityResultContracts.TakePicture
   ↓

3. CAMERA RESULT
   Image captured to FileProvider URI
   MainActivity receives result in callback
   ↓

4. ACTIVITY (MainActivity)
   Creates multipart request from image
   Calls: apiService.predict(imagePart)
   ↓

5. SERVICE (ApiService via Retrofit)
   Makes HTTP POST to backend /predict
   Sends: Multipart image data
   Receives: JSON response {"disease": "...", "confidence": ...}
   ↓

6. ACTIVITY (MainActivity)
   Receives response from coroutine
   Saves result to database via ScanDao
   Creates Intent with result data
   Starts ResultActivity
   ↓

7. ACTIVITY (ResultActivity)
   Receives data from Intent extras
   Displays disease name, confidence, treatment
```

### Trade-offs of Direct Architecture

#### Advantages
- **Simpler code** - Easier to follow and debug
- **Less files** - No ViewModel or Repository classes
- **Faster to implement** - Good for course projects
- **Direct data flow** - Clear cause and effect

#### Disadvantages
- **No screen rotation handling** - Network calls may restart (mitigated with coroutine)
- **Testing harder** - Cannot unit test without Android framework
- **Tight coupling** - Activities depend on services directly

**For LeafGuard's scope, the direct pattern is appropriate.**

---

## Android Components in LeafGuard

### Activities

**Definition:** A screen that users interact with.

**LeafGuard Activities:**

1. **MainActivity** - Home/Dashboard + Image Capture
   - Purpose: Entry point, scan button, camera/gallery access
   - Layout: `activity_main.xml`
   - Key UI: Scan button, Cloud/Offline toggle, navigation buttons
   - Navigation: ResultActivity, HistoryActivity, DiseaseLibraryActivity, SettingsActivity

2. **ResultActivity** - Display Prediction
   - Purpose: Show disease prediction and treatment
   - Layout: `activity_result.xml`
   - Key UI: Disease name, confidence %, symptoms, treatment
   - Navigation: Back to MainActivity, view in History

3. **HistoryActivity** - Past Scans List
   - Purpose: Display all previous scans
   - Layout: `activity_history.xml` with RecyclerView
   - Key UI: List of scans with date, disease, confidence
   - Navigation: Click item → HistoryDetailActivity

4. **HistoryDetailActivity** - Single Scan Details
   - Purpose: Display detailed view of a past scan
   - Layout: `activity_history_detail.xml`
   - Key UI: Full image, disease info, timestamp, delete option
   - Navigation: Back to HistoryActivity

5. **DiseaseLibraryActivity** - Disease Encyclopedia
   - Purpose: Browse all diseases with information
   - Layout: `activity_disease_library.xml`
   - Key UI: SearchView, Disease list, Detail view
   - Navigation: Click disease → show details

6. **SettingsActivity** - App Preferences
   - Purpose: Configure app settings
   - Layout: `activity_settings.xml` (PreferenceScreen)
   - Key UI: Notification toggle, AI mode selection, Clear data
   - Navigation: Back to MainActivity

**Activity Lifecycle in LeafGuard:**
```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Called when activity is first created
        // Initialize views, services, listeners
        setContentView(R.layout.activity_main)
        setupServices()
        checkCameraPermission()
    }

    override fun onStart() {
        super.onStart()
        // Called when activity becomes visible to user
        // Good place to refresh data
    }

    override fun onResume() {
        super.onResume()
        // Called when activity starts interacting with user
        // Register sensors, start animations
    }

    override fun onPause() {
        super.onPause()
        // Called when activity is partially obscured
        // Save state, pause operations
    }

    override fun onStop() {
        super.onStop()
        // Called when activity is no longer visible
        // Release resources
    }

    override fun onDestroy() {
        super.onDestroy()
        // Called before activity is destroyed
        // Final cleanup
    }
}
```

### Intents

**Definition:** Messaging object to request action from another component.

**Types used in LeafGuard:**

1. **Explicit Intents** (Navigate between app screens)
```kotlin
// From MainActivity to ResultActivity
val intent = Intent(this, ResultActivity::class.java)
intent.putExtra("disease", "Tomato Blight") // Pass data
intent.putExtra("confidence", 0.95f)
startActivity(intent)

// In ResultActivity, receive data
val disease = intent.getStringExtra("disease")
val confidence = intent.getFloatExtra("confidence", 0f)
```

2. **Implicit Intents** (Request action from system)
```kotlin
// Modern approach: ActivityResultContracts (recommended)

// Camera capture
private val cameraLauncher = registerForActivityResult(
    ActivityResultContracts.TakePicture()
) { success ->
    if (success) {
        // Image saved to imageUri
        processImage(imageUri)
    }
}

// Gallery pick
private val galleryLauncher = registerForActivityResult(
    ActivityResultContracts.GetContent()
) { uri ->
    uri?.let { processImage(it) }
}

// Usage
fun openCamera() {
    imageUri = createImageFileUri() // FileProvider URI
    cameraLauncher.launch(imageUri)
}

fun openGallery() {
    galleryLauncher.launch("image/*")
}
```

### RecyclerView

**Purpose:** Efficiently display large lists.

**LeafGuard Use Cases:**

1. **History List:**
```kotlin
// HistoryAdapter.kt
class HistoryAdapter(
    private val scans: List<ScanRecord>,
    private val onClick: (ScanRecord) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scan = scans[position]
        holder.bind(scan)
        holder.itemView.setOnClickListener { onClick(scan) }
    }

    override fun getItemCount(): Int = scans.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val diseaseText: TextView = view.findViewById(R.id.tv_disease)
        private val confidenceText: TextView = view.findViewById(R.id.tv_confidence)
        private val dateText: TextView = view.findViewById(R.id.tv_date)

        fun bind(scan: ScanRecord) {
            diseaseText.text = scan.diseaseName
            confidenceText.text = String.format("%.1f%%", scan.confidence * 100)
            dateText.text = formatDate(scan.timestamp)
        }
    }
}
```

2. **Disease Library List:**
Similar pattern for displaying all diseases.

**Why RecyclerView instead of ListView?**
- More flexible (can show grid, list, staggered)
- Better performance (ViewHolder pattern enforced)
- Built-in animations
- Easier to customize

---

## Networking Architecture

### Retrofit Overview

**What is Retrofit?**
Type-safe HTTP client for Android. Converts HTTP API into Kotlin interface.

**Why use Retrofit in LeafGuard?**
- Easy to use (annotations-based)
- Automatic JSON parsing
- Built-in error handling
- Coroutine support (suspend functions)
- Well-maintained by Square

### LeafGuard API Design

**Base URL:** `http://10.0.2.2:8000/` (Android emulator to localhost) or production URL

**Endpoints:**

1. **/predict** - Disease detection
   - Method: POST
   - Body: Multipart form-data with image (field name: "image")
   - Response: JSON with disease name and confidence

### Retrofit Implementation in LeafGuard

**Step 1: Define API Interface**
```kotlin
// ApiService.kt
interface ApiService {
    @Multipart
    @POST("/predict")
    suspend fun predict(
        @Part image: MultipartBody.Part
    ): Response<PredictionResponse>
}

// PredictionResponse.kt
data class PredictionResponse(
    @SerializedName("disease") val disease: String,
    @SerializedName("confidence") val confidence: Float
)
```

**Step 2: Create Retrofit Client**
```kotlin
// RetrofitClient.kt
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"
    
    val instance: Retrofit by lazy {
        // Create OkHttp client with timeouts
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        // Create Retrofit instance
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // JSON parsing
            .build()
    }
}
```

**Step 3: Make API Call in Activity**
```kotlin
// MainActivity.kt - making the API call
class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize API service
        apiService = RetrofitClient.instance.create(ApiService::class.java)
    }

    private fun uploadImage(imageFile: File) {
        lifecycleScope.launch {
            try {
                // Prepare multipart request - field name is "image"
                val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData(
                    "image",  // API expects "image" field name
                    imageFile.name,
                    requestBody
                )

                // Make API call (suspend function, runs in background)
                val response = apiService.predict(imagePart)

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!
                    
                    // Navigate to result screen
                    val intent = Intent(this@MainActivity, ResultActivity::class.java)
                    intent.putExtra("disease", result.disease)
                    intent.putExtra("confidence", result.confidence)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "API Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
```

### Error Handling

**Types of errors:**

1. **Network Error** - No internet connection
2. **Timeout Error** - Server took too long to respond
3. **HTTP Error** - Server returned 4xx or 5xx
4. **Parsing Error** - JSON response format unexpected

**Handling in LeafGuard:**
```java
@Override
public void onFailure(Call<UploadResponse> call, Throwable t) {
    String errorMessage;

    if (t instanceof IOException) {
        // Network error
        errorMessage = "No internet connection. Please check your network.";
    } else if (t instanceof SocketTimeoutException) {
        // Timeout error
        errorMessage = "Request timed out. Server might be busy.";
    } else if (t instanceof JsonSyntaxException) {
        // Parsing error
        errorMessage = "Invalid server response.";
    } else {
        // Unknown error
        errorMessage = "Unknown error occurred: " + t.getMessage();
    }

    callback.onError(new Exception(errorMessage));
}
```

---

## Database Design

### Room Database Overview

**What is Room?**
Android's official database library, built on top of SQLite.

**Benefits:**
- Compile-time verification of SQL queries
- Reduces boilerplate code
- LiveData integration for reactive UI
- Type-safe database access

### LeafGuard Database Schema

**Tables:**

1. **scans** - Stores scan history
2. **users** - Stores user information (if authentication added)
3. **diseases** - Caches disease information (optional)

**ER Diagram:**
```
┌─────────────────┐         ┌─────────────────┐
│     users       │         │     scans       │
├─────────────────┤         ├─────────────────┤
│ id (PK)         │─────┐   │ id (PK)         │
│ name            │     └──→│ user_id (FK)    │
│ email           │         │ image_path      │
│ password_hash   │         │ disease_name    │
│ created_at      │         │ confidence      │
└─────────────────┘         │ mode            │
                            │ timestamp       │
                            │ location_lat    │
                            │ location_lon    │
                            └─────────────────┘
```

### Room Implementation

**Step 1: Define Entity**
```java
// ScanEntity.java
@Entity(tableName = "scans")
public class ScanEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    @ColumnInfo(name = "disease_name")
    private String diseaseName;

    private float confidence;

    private String mode; // "cloud" or "offline"

    private long timestamp;

    @ColumnInfo(name = "location_lat")
    private Double locationLat;

    @ColumnInfo(name = "location_lon")
    private Double locationLon;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    // ... more getters/setters
}
```

**Step 2: Define DAO**
```java
// ScanDao.java
@Dao
public interface ScanDao {

    @Insert
    void insert(ScanEntity scan);

    @Update
    void update(ScanEntity scan);

    @Delete
    void delete(ScanEntity scan);

    @Query("SELECT * FROM scans ORDER BY timestamp DESC")
    LiveData<List<ScanEntity>> getAllScans();

    @Query("SELECT * FROM scans WHERE id = :scanId")
    LiveData<ScanEntity> getScanById(int scanId);

    @Query("SELECT * FROM scans WHERE disease_name = :diseaseName ORDER BY timestamp DESC")
    LiveData<List<ScanEntity>> getScansByDisease(String diseaseName);

    @Query("DELETE FROM scans")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM scans")
    int getCount();
}
```

**Step 3: Define Database**
```java
// AppDatabase.java
@Database(entities = {ScanEntity.class, UserEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ScanDao scanDao();
    public abstract UserDao userDao();

    // Singleton pattern
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "leafguard_database"
            )
            .fallbackToDestructiveMigration() // For development only
            .build();
        }
        return instance;
    }
}
```

**Step 4: Use in Repository**
```java
// ScanRepository.java
public class ScanRepository {
    private final ScanDao scanDao;

    public ScanRepository(ScanDao scanDao) {
        this.scanDao = scanDao;
    }

    // Get all scans (returns LiveData, updates automatically)
    public LiveData<List<ScanEntity>> getAllScans() {
        return scanDao.getAllScans();
    }

    // Insert scan (must run on background thread)
    public void insert(ScanEntity scan) {
        // Room enforces background thread for write operations
        new Thread(() -> scanDao.insert(scan)).start();
    }

    // Delete scan
    public void delete(ScanEntity scan) {
        new Thread(() -> scanDao.delete(scan)).start();
    }
}
```

### Database Migrations (Advanced)

When you change schema in production app, you need migrations:

```java
// Migration from version 1 to 2: Add location columns
static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
        database.execSQL("ALTER TABLE scans ADD COLUMN location_lat REAL");
        database.execSQL("ALTER TABLE scans ADD COLUMN location_lon REAL");
    }
};

// Use in database builder
AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, "leafguard_database")
    .addMigrations(MIGRATION_1_2)
    .build();
```

---

## Machine Learning Integration

### Two AI Modes in LeafGuard

#### Mode 1: Cloud AI (Server-side inference)

**Workflow:**
```
Android App → HTTP POST → FastAPI Backend → TensorFlow Model → JSON Response → Android App
```

**Advantages:**
- Can use large, accurate models (100+ MB)
- Easy to update model (just update server)
- No model size constraints
- Better accuracy with complex models

**Disadvantages:**
- Requires internet connection
- Slower (network latency ~2-5 seconds)
- Server hosting cost
- Privacy concern (image sent to server)

**Implementation:**
Already covered in Retrofit section above.

#### Mode 2: On-Device AI (TensorFlow Lite)

**Workflow:**
```
Android App → Load .tflite from assets → Preprocess image → Run inference → Get result → Display
```

**Advantages:**
- Works offline
- Fast (inference ~200-500ms)
- Privacy-friendly (image stays on device)
- No server cost

**Disadvantages:**
- Model size limited (~10-50 MB for mobile)
- Lower accuracy than large server models
- Harder to update (must update app)
- Device-dependent performance

**Implementation:**

**Step 1: Add TFLite Dependency**
```gradle
// app/build.gradle
dependencies {
    implementation 'org.tensorflow:tensorflow-lite:2.10.0'
    implementation 'org.tensorflow:tensorflow-lite-support:0.4.3'
}
```

**Step 2: Add Model to Assets**
```
app/src/main/assets/
├── plant_disease_model.tflite
└── labels.txt
```

**labels.txt:**
```
Tomato Early Blight
Tomato Late Blight
Tomato Leaf Mold
Potato Early Blight
Potato Late Blight
Pepper Bell Bacterial Spot
...
```

**Step 3: TFLite Inference Class**
```java
// TFLiteInference.java
public class TFLiteInference {
    private Interpreter tflite;
    private List<String> labels;

    private static final int INPUT_SIZE = 224;
    private static final int NUM_CHANNELS = 3;

    public TFLiteInference(Context context) {
        try {
            // Load model from assets
            tflite = new Interpreter(loadModelFile(context));

            // Load labels from assets
            labels = loadLabels(context);
        } catch (Exception e) {
            Log.e("TFLite", "Error loading model", e);
        }
    }

    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("plant_disease_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private List<String> loadLabels(Context context) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("labels.txt")));
        List<String> labels = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            labels.add(line);
        }
        reader.close();
        return labels;
    }

    public DiseaseResult classify(Bitmap bitmap) {
        // Resize bitmap to model input size
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);

        // Convert bitmap to ByteBuffer
        ByteBuffer inputBuffer = convertBitmapToByteBuffer(resizedBitmap);

        // Output buffer
        float[][] output = new float[1][labels.size()];

        // Run inference
        tflite.run(inputBuffer, output);

        // Find class with highest probability
        float maxConfidence = 0;
        int maxIndex = 0;
        for (int i = 0; i < output[0].length; i++) {
            if (output[0][i] > maxConfidence) {
                maxConfidence = output[0][i];
                maxIndex = i;
            }
        }

        String diseaseName = labels.get(maxIndex);

        return new DiseaseResult(diseaseName, maxConfidence);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * NUM_CHANNELS);
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] pixels = new int[INPUT_SIZE * INPUT_SIZE];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int pixel : pixels) {
            // Normalize pixel values to [-1, 1] or [0, 1] depending on model training
            byteBuffer.putFloat(((pixel >> 16) & 0xFF) / 255.0f); // Red
            byteBuffer.putFloat(((pixel >> 8) & 0xFF) / 255.0f);  // Green
            byteBuffer.putFloat((pixel & 0xFF) / 255.0f);         // Blue
        }

        return byteBuffer;
    }

    fun close() {
        tflite?.close()
    }
}
```

**Step 4: Use in MainActivity**
```kotlin
// MainActivity.kt - offline classification
class MainActivity : AppCompatActivity() {
    private lateinit var tfliteClassifier: TFLiteClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tfliteClassifier = TFLiteClassifier(this)
    }

    private fun classifyOffline(bitmap: Bitmap) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = tfliteClassifier.classify(bitmap)
            
            withContext(Dispatchers.Main) {
                // Navigate to ResultActivity with offline result
                val intent = Intent(this@MainActivity, ResultActivity::class.java)
                intent.putExtra("disease", result.diseaseName)
                intent.putExtra("confidence", result.confidence)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tfliteClassifier.close()
    }
}
```

---

## XML Parsing Fundamentals

### Why XML in LeafGuard?

**Purpose:** Store disease information (symptoms, treatments) locally without database overhead.

**Advantages:**
- Easy to edit (can update disease info without code changes)
- Human-readable
- No database schema needed
- Can be bundled in app assets

### Disease Library XML Structure

**File:** `app/src/main/assets/diseases.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<diseases>
    <disease>
        <name>Tomato Early Blight</name>
        <scientificName>Alternaria solani</scientificName>
        <symptoms>
            Dark brown spots with concentric rings on lower leaves.
            Yellow halo around spots. Leaves may drop prematurely.
            Fruit may have dark, leathery spots.
        </symptoms>
        <causes>
            Fungal pathogen. Spreads in warm, humid conditions.
            Overwatering and poor air circulation increase risk.
        </causes>
        <treatment>
            Remove infected leaves immediately.
            Apply fungicide containing chlorothalonil or mancozeb.
            Spray every 7-10 days during humid weather.
            Improve air circulation around plants.
        </treatment>
        <prevention>
            Rotate crops every 2-3 years.
            Use disease-resistant tomato varieties.
            Avoid overhead watering (water at base).
            Mulch around plants to prevent soil splash.
            Space plants adequately for air flow.
        </prevention>
    </disease>

    <disease>
        <name>Tomato Late Blight</name>
        <scientificName>Phytophthora infestans</scientificName>
        <symptoms>
            Water-soaked spots on leaves that turn brown.
            White fuzzy growth on undersides of leaves.
            Rapid leaf death and stem lesions.
            Fruit develops brown, firm rot.
        </symptoms>
        <causes>
            Oomycete pathogen. Thrives in cool, wet weather.
            Can spread rapidly in conducive conditions.
        </causes>
        <treatment>
            Apply copper-based fungicide immediately.
            Remove and destroy all infected plants (do not compost).
            Monitor neighboring plants closely.
        </treatment>
        <prevention>
            Plant late blight-resistant varieties.
            Ensure good air circulation.
            Water in morning so leaves dry quickly.
            Monitor weather for blight-favorable conditions.
        </prevention>
    </disease>

    <!-- More diseases... -->
</diseases>
```

### Parsing XML in Android

**Using XmlPullParser:**

```java
// XmlParser.java
public class XmlParser {

    public static List<Disease> parseDiseaseLibrary(Context context) {
        List<Disease> diseases = new ArrayList<>();

        try {
            // Open XML file from assets
            InputStream inputStream = context.getAssets().open("diseases.xml");

            // Create parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, "UTF-8");

            Disease currentDisease = null;
            String currentTag = null;
            int eventType = parser.getEventType();

            // Parse XML
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();
                        if (currentTag.equals("disease")) {
                            currentDisease = new Disease();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        String text = parser.getText().trim();
                        if (currentDisease != null && !text.isEmpty()) {
                            switch (currentTag) {
                                case "name":
                                    currentDisease.setName(text);
                                    break;
                                case "scientificName":
                                    currentDisease.setScientificName(text);
                                    break;
                                case "symptoms":
                                    currentDisease.setSymptoms(text);
                                    break;
                                case "causes":
                                    currentDisease.setCauses(text);
                                    break;
                                case "treatment":
                                    currentDisease.setTreatment(text);
                                    break;
                                case "prevention":
                                    currentDisease.setPrevention(text);
                                    break;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("disease") && currentDisease != null) {
                            diseases.add(currentDisease);
                            currentDisease = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            inputStream.close();
        } catch (Exception e) {
            Log.e("XmlParser", "Error parsing disease library", e);
        }

        return diseases;
    }

    // Find disease by name
    public static Disease findDiseaseByName(Context context, String name) {
        List<Disease> diseases = parseDiseaseLibrary(context);
        for (Disease disease : diseases) {
            if (disease.getName().equalsIgnoreCase(name)) {
                return disease;
            }
        }
        return null;
    }
}
```

**Using in ViewModel:**
```java
// DiseaseViewModel.java
public class DiseaseViewModel extends ViewModel {
    private MutableLiveData<List<Disease>> diseases = new MutableLiveData<>();

    public void loadDiseases(Context context) {
        // Load in background thread
        new Thread(() -> {
            List<Disease> diseaseList = XmlParser.parseDiseaseLibrary(context);
            diseases.postValue(diseaseList);
        }).start();
    }

    public LiveData<List<Disease>> getDiseases() {
        return diseases;
    }
}
```

---

## Senior Repository Analysis Insights

### Key Learnings from Analyzing Android Projects

**Repository 1: PlantVillage Android App**

**What they did well:**
1. **Clear package structure:** Organized by feature (ui/scan/, data/repository/, ml/inference/)
2. **Comprehensive README:** Installation steps, screenshots, architecture diagram
3. **Error handling:** Try-catch blocks with user-friendly error messages

**What they did poorly:**
1. **No ViewModels:** Activities directly access database (tight coupling)
2. **Hard-coded strings:** URLs and messages in Java code instead of strings.xml
3. **Large activities:** MainActivity has 800+ lines (should be split)

**Adopt in LeafGuard:**
- Feature-based package organization
- Comprehensive README with screenshots
- Detailed error messages

**Avoid in LeafGuard:**
- Direct database access from Activities
- Hard-coded strings
- Monolithic activities

**Repository 2: CNN-PlantDiseaseDetection**

**What they did well:**
1. **TFLite integration:** Clean inference code with proper preprocessing
2. **Model documentation:** Explained model architecture, input/output format
3. **Performance optimizations:** Image compression before upload

**What they did poorly:**
1. **No offline mode:** Completely dependent on network
2. **No history:** Does not save past predictions
3. **Basic UI:** Minimal design, not user-friendly

**Adopt in LeafGuard:**
- TFLite inference approach
- Image compression for faster uploads
- Model documentation

**Avoid in LeafGuard:**
- Network-only approach (implement both cloud and offline)
- Lack of history feature
- Basic UI (use Material Design)

### Common Patterns Observed

**Good Patterns:**
1. **Singleton Pattern:** For Retrofit client and Room database
2. **Repository Pattern:** Abstracting data sources
3. **Observer Pattern:** Using LiveData for reactive UI
4. **Adapter Pattern:** RecyclerView adapters for lists

**Bad Patterns:**
1. **God Object:** One class doing everything
2. **Magic Numbers:** Hard-coded values without constants
3. **Callback Hell:** Nested callbacks making code unreadable
4. **No Error Handling:** Assuming network/database operations always succeed

---

## Project Documentation Best Practices

### Types of Documentation

1. **Code Documentation:**
   - Inline comments explaining WHY, not WHAT
   - Javadoc for public methods
   - README for each module

2. **User Documentation:**
   - User manual with screenshots
   - Installation guide
   - Troubleshooting section

3. **Developer Documentation:**
   - Architecture overview
   - API documentation
   - Setup instructions

4. **Academic Documentation:**
   - Project proposal
   - Final report
   - Presentation slides

### Writing Effective Comments

**Bad Comment (explains WHAT):**
```java
// Set text to disease name
textView.setText(disease.getName());
```

**Good Comment (explains WHY):**
```java
// Display disease name in red if confidence is low to alert user
if (result.getConfidence() < 0.6) {
    textView.setTextColor(Color.RED);
}
textView.setText(disease.getName());
```

**Javadoc Example:**
```java
/**
 * Uploads an image to the backend API for disease detection.
 *
 * This method compresses the image before upload to reduce bandwidth usage.
 * The API call is asynchronous and result is delivered via callback.
 *
 * @param imagePath Absolute path to the image file
 * @param callback Callback to receive result or error
 * @throws IllegalArgumentException if imagePath is null or empty
 */
public void detectDisease(String imagePath, Callback<DiseaseResult> callback) {
    // Implementation
}
```

### README Template

```markdown
# LeafGuard AI

![App Screenshot](screenshots/home_screen.png)

## Overview
LeafGuard AI is an Android application for plant disease detection using deep learning.

## Features
- 📸 Image capture and selection
- 🤖 AI-powered disease detection
- 💾 Local scan history
- 📚 Disease information library
- ✈️ Offline mode with TensorFlow Lite

## Technology Stack
- **Android:** Java, MVVM architecture
- **Networking:** Retrofit 2.9
- **Database:** Room 2.5
- **Backend:** FastAPI
- **ML:** TensorFlow Lite

## Installation
1. Clone repository: `git clone https://github.com/yourusername/leafguard-ai.git`
2. Open in Android Studio
3. Build and run on device/emulator

## Architecture
[Include architecture diagram]

## API Documentation
See [API_DOCS.md](docs/API_DOCS.md)

## License
MIT License

## Contact
[Your Name] - [your.email@example.com]
```

---

## Summary and Next Steps

### Week 01 Key Takeaways

1. **LeafGuard AI is a hybrid cloud-offline plant disease detection app**
2. **Two-tier architecture:** Presentation Layer (Activities) → Service/Data Layer
3. **Direct pattern:** Activities call services directly (ApiService, ScanDao, TFLiteClassifier)
4. **6 Activities:** MainActivity, ResultActivity, HistoryActivity, HistoryDetailActivity, DiseaseLibraryActivity, SettingsActivity
5. **Networking:** Retrofit for REST API communication with FastAPI backend (POST /predict)
6. **Database:** Room for local scan history storage (ScanRecord entity)
7. **ML Integration:** Cloud AI (backend) and Offline AI (TFLite on device)
8. **XML Parsing:** Disease information stored in diseases.xml

### Preparation for Week 02

**What you will do:**
- Install Android Studio
- Create LeafGuard project
- Set up package structure
- Create 6 empty activities
- Design XML layouts for each activity
- Implement basic navigation with Intents

**Prerequisites:**
- Android Studio installed
- Basic Java/Kotlin knowledge refreshed
- Understanding of this week's concepts

**Resources to review:**
- Android Developer Guide: Activities
- Material Design Guidelines
- XML Layouts Tutorial

---

**You have completed Week 01 theoretical foundation. You now understand WHAT you are building, WHY you are building it, and HOW all components connect. Week 02 begins hands-on Android development.**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 01 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| **2** | **learning-notes.md** ← *You are here* | **Theory & Learning Notes** |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Week Overview & Objectives](README.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Theory & Learning Notes** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Practice Exercises →](exercises.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| *(First week — no previous)* | [Learning Path](../../LEARNING_PATH.md) | [Week 02: Android Basics & UI ➡](../week-02-android-basics-ui/README.md) |

---
