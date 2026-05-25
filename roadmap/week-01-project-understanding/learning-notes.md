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
   - `MainActivity.java` - Home screen with scan button
   - `ScanActivity.java` - Image capture/selection interface
   - `ResultActivity.java` - Display disease prediction
   - `HistoryActivity.java` - List of past scans
   - `DiseaseLibraryActivity.java` - Browse disease information
   - `SettingsActivity.java` - App preferences

2. **Fragments (Modular UI):**
   - `HomeFragment.java` - Recent scans on home
   - `LibraryFragment.java` - Disease list
   - `ProfileFragment.java` - User preferences

3. **XML Layouts:**
   - `activity_main.xml` - Main screen layout
   - `activity_result.xml` - Result display layout
   - `item_scan_history.xml` - Single history item layout
   - `fragment_library.xml` - Library fragment layout

4. **Adapters (RecyclerView):**
   - `HistoryAdapter.java` - Displays list of scans
   - `DiseaseAdapter.java` - Displays list of diseases

**Data Flow IN:** User interactions (button clicks, image selection)
**Data Flow OUT:** Display commands to UI components

**Example: MainActivity Flow**
```java
// MainActivity.java (Presentation Layer)
public class MainActivity extends AppCompatActivity {
    private Button scanButton;
    private ScanViewModel scanViewModel; // Connection to Business Logic

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Load XML layout

        // Initialize ViewModel (Business Logic)
        scanViewModel = new ViewModelProvider(this).get(ScanViewModel.class);

        // Observe data changes from ViewModel
        scanViewModel.getRecentScans().observe(this, scans -> {
            // Update UI when data changes
            updateRecentScansList(scans);
        });

        // Handle user interaction
        scanButton = findViewById(R.id.btn_scan);
        scanButton.setOnClickListener(v -> {
            // User clicked scan button
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent); // Navigate to ScanActivity
        });
    }
}
```

**Key Principles:**
- Activities should NOT directly access database
- Activities should NOT make network calls
- Activities should only update UI based on ViewModel data
- Activities should be thin (minimal logic)

### Tier 2: Business Logic Layer (Domain Layer)

**Responsibility:** Application logic, data processing, business rules.

**Components in LeafGuard:**

1. **ViewModels:**
   - `ScanViewModel.java` - Manages scan-related operations
   - `HistoryViewModel.java` - Manages history data
   - `AuthViewModel.java` - Manages authentication (if added)

2. **Repositories:**
   - `ScanRepository.java` - Coordinates scan data sources
   - `DiseaseRepository.java` - Manages disease information

3. **UseCases (Optional):**
   - `UploadImageUseCase.java` - Encapsulates image upload logic
   - `SaveScanUseCase.java` - Encapsulates saving scan result

4. **Utility Classes:**
   - `ImageProcessor.java` - Resize, compress, rotate images
   - `ValidationUtils.java` - Validate user input
   - `DateFormatter.java` - Format timestamps

**Data Flow IN:** Requests from Presentation Layer
**Data Flow OUT:** Processed data back to Presentation Layer, requests to Data Layer

**Example: ScanViewModel**
```java
// ScanViewModel.java (Business Logic Layer)
public class ScanViewModel extends ViewModel {
    private final ScanRepository repository; // Connection to Data Layer
    private final MutableLiveData<ScanResult> scanResult = new MutableLiveData<>();

    public ScanViewModel(ScanRepository repository) {
        this.repository = repository;
    }

    // Called by Activity when user selects image
    public void uploadImage(String imagePath) {
        // Business logic: Validate image
        if (!isValidImage(imagePath)) {
            scanResult.setValue(ScanResult.error("Invalid image"));
            return;
        }

        // Business logic: Compress image before upload
        String compressedPath = ImageProcessor.compress(imagePath);

        // Delegate data operation to Repository
        repository.detectDisease(compressedPath, new Callback<DiseaseResult>() {
            @Override
            public void onSuccess(DiseaseResult result) {
                // Business logic: Check confidence threshold
                if (result.getConfidence() < 0.5) {
                    result.setWarning("Low confidence prediction");
                }

                // Business logic: Save to database
                repository.saveScanLocally(result);

                // Update UI via LiveData
                scanResult.setValue(ScanResult.success(result));
            }

            @Override
            public void onError(Exception e) {
                scanResult.setValue(ScanResult.error(e.getMessage()));
            }
        });
    }

    // Expose LiveData for Activity to observe
    public LiveData<ScanResult> getScanResult() {
        return scanResult;
    }
}
```

**Key Principles:**
- ViewModels survive configuration changes (screen rotation)
- ViewModels should NOT hold references to Activities/Views
- ViewModels coordinate between Repositories and UI
- ViewModels contain business logic, not data access logic

### Tier 3: Data Layer

**Responsibility:** Data storage, retrieval, and network communication.

**Components in LeafGuard:**

1. **Local Data Sources:**
   - `AppDatabase.java` - Room database definition
   - `ScanDao.java` - Database access methods
   - `ScanEntity.java` - Database table schema
   - `XmlParser.java` - Parse disease XML files
   - `FileManager.java` - Save/load images from storage

2. **Remote Data Sources:**
   - `ApiService.java` - Retrofit interface defining API endpoints
   - `RetrofitClient.java` - Retrofit singleton configuration
   - `UploadResponse.java` - API response data class

3. **Data Models:**
   - `Disease.java` - Disease information model
   - `ScanResult.java` - Scan result model
   - `User.java` - User information model (if authentication added)

**Data Flow IN:** Requests from Business Logic Layer
**Data Flow OUT:** Raw data (database records, API responses)

**Example: ScanRepository**
```java
// ScanRepository.java (Data Layer)
public class ScanRepository {
    private final ScanDao scanDao; // Local data source
    private final ApiService apiService; // Remote data source

    public ScanRepository(ScanDao scanDao, ApiService apiService) {
        this.scanDao = scanDao;
        this.apiService = apiService;
    }

    // Coordinates network call (remote) and database save (local)
    public void detectDisease(String imagePath, Callback<DiseaseResult> callback) {
        // Prepare image for upload
        File imageFile = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);

        // Make network call using Retrofit
        apiService.uploadImage(imagePart).enqueue(new retrofit2.Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UploadResponse apiResponse = response.body();

                    // Convert API response to domain model
                    DiseaseResult result = new DiseaseResult(
                        apiResponse.getDisease(),
                        apiResponse.getConfidence(),
                        apiResponse.getSymptoms(),
                        apiResponse.getTreatment()
                    );

                    callback.onSuccess(result);
                } else {
                    callback.onError(new Exception("API call failed"));
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                callback.onError(new Exception(t));
            }
        });
    }

    // Save scan result to local database
    public void saveScanLocally(DiseaseResult result) {
        // Convert domain model to database entity
        ScanEntity entity = new ScanEntity();
        entity.setDiseaseName(result.getDiseaseName());
        entity.setConfidence(result.getConfidence());
        entity.setTimestamp(System.currentTimeMillis());

        // Insert into database
        new Thread(() -> scanDao.insert(entity)).start();
    }

    // Get all scans from local database
    public LiveData<List<ScanEntity>> getAllScans() {
        return scanDao.getAllScans();
    }
}
```

**Key Principles:**
- Repositories are single source of truth
- Repositories decide: network or cache?
- Repositories convert between data models and domain models
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
```java
// MainActivity uses ViewModel
public class MainActivity extends AppCompatActivity {
    private ScanViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewModel.uploadImage(imagePath);
    }
}
```

If you switch from Retrofit to Volley, you ONLY change `ApiService` and `RetrofitClient` in the Data Layer. Activities and ViewModels are unaffected.

---

## MVVM Pattern Explained

### What is MVVM?

**MVVM = Model + View + ViewModel**

**Purpose:** Separate UI (View) from business logic (ViewModel) and data (Model).

### Components Breakdown

#### 1. Model

**What it is:** Your data structures and data sources.

**In LeafGuard:**
- `Disease.java` - Represents a plant disease
- `ScanResult.java` - Represents a scan result
- `ScanEntity.java` - Database entity
- `Room Database` - Data source
- `Retrofit API` - Data source

**Example:**
```java
// Disease.java (Model)
public class Disease {
    private String name;
    private String scientificName;
    private String symptoms;
    private String treatment;
    private String prevention;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    // ... more getters/setters
}
```

**Responsibilities:**
- Define data structure
- Represent business entities
- No UI code
- No business logic

#### 2. View

**What it is:** Your UI components (Activities, Fragments, XML layouts).

**In LeafGuard:**
- `MainActivity.java` - Observes ViewModel data
- `activity_main.xml` - Defines UI layout
- `ResultActivity.java` - Displays result from ViewModel

**Example:**
```java
// MainActivity.java (View)
public class MainActivity extends AppCompatActivity {
    private ScanViewModel viewModel;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.tv_status);

        // Get ViewModel
        viewModel = new ViewModelProvider(this).get(ScanViewModel.class);

        // Observe data from ViewModel
        viewModel.getScanResult().observe(this, result -> {
            // Update UI when data changes
            if (result.isSuccess()) {
                statusText.setText("Disease: " + result.getData().getDiseaseName());
            } else {
                statusText.setText("Error: " + result.getErrorMessage());
            }
        });

        // User clicks scan button
        findViewById(R.id.btn_scan).setOnClickListener(v -> {
            // Tell ViewModel to start scan
            viewModel.uploadImage("/path/to/image.jpg");
        });
    }
}
```

**Responsibilities:**
- Display UI
- Handle user interactions (clicks, swipes)
- Observe ViewModel data
- Update UI when data changes
- NO business logic
- NO data access

#### 3. ViewModel

**What it is:** Bridge between View and Model, holds UI state.

**In LeafGuard:**
- `ScanViewModel.java` - Manages scan operations
- `HistoryViewModel.java` - Manages history data

**Example:**
```java
// ScanViewModel.java (ViewModel)
public class ScanViewModel extends ViewModel {
    private final ScanRepository repository;

    // LiveData holds UI state
    private MutableLiveData<Resource<DiseaseResult>> scanResult = new MutableLiveData<>();

    public ScanViewModel(ScanRepository repository) {
        this.repository = repository;
    }

    // Called by View (Activity) when user wants to scan
    public void uploadImage(String imagePath) {
        // Set loading state
        scanResult.setValue(Resource.loading(null));

        // Business logic: Validate input
        if (imagePath == null || imagePath.isEmpty()) {
            scanResult.setValue(Resource.error("Invalid image path", null));
            return;
        }

        // Delegate to Repository (Model layer)
        repository.detectDisease(imagePath, new Callback<DiseaseResult>() {
            @Override
            public void onSuccess(DiseaseResult result) {
                // Update LiveData - View automatically receives update
                scanResult.setValue(Resource.success(result));

                // Business logic: Save to database
                repository.saveScanLocally(result);
            }

            @Override
            public void onError(Exception e) {
                scanResult.setValue(Resource.error(e.getMessage(), null));
            }
        });
    }

    // View observes this LiveData
    public LiveData<Resource<DiseaseResult>> getScanResult() {
        return scanResult;
    }

    // Clean up when ViewModel is destroyed
    @Override
    protected void onCleared() {
        super.onCleared();
        // Cancel any ongoing operations
    }
}
```

**Responsibilities:**
- Hold UI state (LiveData)
- Handle business logic
- Coordinate data operations via Repository
- Survive configuration changes (rotation)
- NO reference to View (Activity/Fragment)
- NO Android framework dependencies (except androidx.lifecycle)

### MVVM Data Flow in LeafGuard

**User scans a leaf - complete flow:**

```
1. USER INTERACTION
   User taps "Scan" button
   ↓

2. VIEW (MainActivity)
   Button click listener triggered
   Calls: viewModel.uploadImage(imagePath)
   ↓

3. VIEWMODEL (ScanViewModel)
   Validates image path (business logic)
   Sets LiveData to "loading" state
   Calls: repository.detectDisease(imagePath, callback)
   ↓

4. MODEL - REPOSITORY (ScanRepository)
   Decides: Use network or cache?
   Creates Retrofit API call
   Calls: apiService.uploadImage(imagePart)
   ↓

5. MODEL - API SERVICE (Retrofit)
   Makes HTTP POST to FastAPI backend
   Sends: Multipart image data
   Receives: JSON response
   ↓

6. MODEL - REPOSITORY (callback)
   Receives API response
   Converts to domain model (DiseaseResult)
   Saves to Room database
   Calls: callback.onSuccess(result)
   ↓

7. VIEWMODEL (callback)
   Receives result from Repository
   Updates LiveData with success state
   scanResult.setValue(Resource.success(result))
   ↓

8. VIEW (MainActivity - Observer)
   Observes LiveData change
   Updates UI automatically
   Shows disease name, confidence, treatment
```

### Benefits of MVVM in LeafGuard

#### 1. Survives Configuration Changes

**Problem without MVVM:**
```java
// Bad: Network call in Activity
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // User initiates scan
        makeNetworkCall();
    }

    void makeNetworkCall() {
        // Network call takes 3 seconds
        RetrofitClient.getApiService().upload(image).enqueue(new Callback() {
            @Override
            public void onResponse(Response response) {
                // Update UI
                textView.setText(response.getDisease());
            }
        });
    }
}

// User rotates screen after 2 seconds (during network call)
// Activity is destroyed and recreated
// Network call completes but Activity is destroyed
// CRASH: Cannot update destroyed Activity's TextView
```

**Solution with MVVM:**
```java
// Good: Network call in ViewModel
public class ScanViewModel extends ViewModel {
    public void uploadImage(String path) {
        repository.detectDisease(path, result -> {
            scanResult.setValue(result); // ViewModel survives rotation
        });
    }
}

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel is retained across configuration changes
        ScanViewModel viewModel = new ViewModelProvider(this).get(ScanViewModel.class);

        // Observe result - re-observes after rotation
        viewModel.getScanResult().observe(this, result -> {
            textView.setText(result.getDisease());
        });

        // Start scan only on first creation
        if (savedInstanceState == null) {
            viewModel.uploadImage(imagePath);
        }
    }
}

// User rotates screen
// Activity is destroyed and recreated
// ViewModel survives (not destroyed)
// New Activity observes same ViewModel
// UI receives result from ViewModel
// No crash, no duplicate network call
```

#### 2. Testability

**Without MVVM:**
Cannot test business logic without starting an Android Activity (requires Android framework, emulator).

**With MVVM:**
```java
// Unit test for ViewModel - runs on JVM, no Android needed
@Test
public void uploadImage_validImage_returnsSuccess() {
    // Mock repository
    ScanRepository mockRepo = mock(ScanRepository.class);
    when(mockRepo.detectDisease(anyString(), any())).thenAnswer(invocation -> {
        Callback callback = invocation.getArgument(1);
        callback.onSuccess(new DiseaseResult("Tomato Blight", 0.95));
        return null;
    });

    // Create ViewModel with mock repository
    ScanViewModel viewModel = new ScanViewModel(mockRepo);

    // Trigger upload
    viewModel.uploadImage("/path/to/image.jpg");

    // Verify result
    Resource<DiseaseResult> result = viewModel.getScanResult().getValue();
    assertNotNull(result);
    assertEquals("Tomato Blight", result.getData().getDiseaseName());
    assertEquals(0.95, result.getData().getConfidence(), 0.01);
}
```

#### 3. Separation of Concerns

**Without MVVM:**
```java
// Activity does everything - BAD
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // UI code
        setContentView(R.layout.activity_main);

        // Business logic
        if (validateImage(path)) {
            // Network code
            makeApiCall();
        }

        // Database code
        saveToDatabase();
    }
}
```

**With MVVM:**
```java
// Each class has one responsibility - GOOD
public class MainActivity extends AppCompatActivity {
    // Only UI code
}

public class ScanViewModel extends ViewModel {
    // Only business logic
}

public class ScanRepository {
    // Only data access
}
```

---

## Android Components in LeafGuard

### Activities

**Definition:** A screen that users interact with.

**LeafGuard Activities:**

1. **MainActivity** - Home/Dashboard
   - Purpose: Entry point, recent scans, navigation
   - Layout: `activity_main.xml`
   - Key UI: Scan button, Recent scans RecyclerView
   - Navigation: Can go to ScanActivity, HistoryActivity, LibraryActivity

2. **ScanActivity** - Image Capture/Selection
   - Purpose: Capture or select leaf image
   - Layout: `activity_scan.xml`
   - Key UI: Camera preview, Gallery button, Analyze button
   - Navigation: Opens camera intent, returns to MainActivity with result

3. **ResultActivity** - Display Prediction
   - Purpose: Show disease prediction and treatment
   - Layout: `activity_result.xml`
   - Key UI: Disease name, confidence %, symptoms, treatment
   - Navigation: Can go to HistoryActivity, restart scan

4. **HistoryActivity** - Past Scans List
   - Purpose: Display all previous scans
   - Layout: `activity_history.xml` with RecyclerView
   - Key UI: List of scans with date, disease, confidence
   - Navigation: Click item → HistoryDetailActivity

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
```java
public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Called when activity is first created
        // Initialize views, ViewModels, listeners
        setContentView(R.layout.activity_scan);
        setupViewModel();
        checkCameraPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Called when activity becomes visible to user
        // Start camera preview
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Called when activity starts interacting with user
        // Register sensors, start animations
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Called when activity is partially obscured
        // Pause camera preview, save state
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Called when activity is no longer visible
        // Release camera resources
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Called before activity is destroyed
        // Final cleanup
    }
}
```

### Intents

**Definition:** Messaging object to request action from another component.

**Types used in LeafGuard:**

1. **Explicit Intents** (Navigate between app screens)
```java
// From MainActivity to ScanActivity
Intent intent = new Intent(MainActivity.this, ScanActivity.class);
intent.putExtra("mode", "camera"); // Pass data
startActivity(intent);

// In ScanActivity, receive data
String mode = getIntent().getStringExtra("mode");
```

2. **Implicit Intents** (Request action from system)
```java
// Open camera
Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
startActivityForResult(cameraIntent, REQUEST_CAMERA);

// Pick image from gallery
Intent galleryIntent = new Intent(Intent.ACTION_PICK);
galleryIntent.setType("image/*");
startActivityForResult(galleryIntent, REQUEST_GALLERY);

// Receive result
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        // Process photo
    }
}
```

### Fragments

**Definition:** Modular section of activity's UI.

**Why use Fragments in LeafGuard?**
- Reusable UI components
- Better tablet support
- Easier navigation with Navigation Component

**LeafGuard Fragments:**

1. **HomeFragment** - Recent scans section
```java
public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_recent_scans);
        // Setup RecyclerView with adapter
        return view;
    }
}
```

2. **LibraryFragment** - Disease list
3. **ProfileFragment** - User settings

**Fragment vs Activity:**
- Activity: Full screen
- Fragment: Part of screen, can have multiple per activity

### RecyclerView

**Purpose:** Efficiently display large lists.

**LeafGuard Use Cases:**

1. **History List:**
```java
// HistoryAdapter.java
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<ScanEntity> scans;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_scan_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScanEntity scan = scans.get(position);
        holder.diseaseText.setText(scan.getDiseaseName());
        holder.confidenceText.setText(String.format("%.1f%%", scan.getConfidence() * 100));
        holder.dateText.setText(formatDate(scan.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return scans != null ? scans.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView diseaseText, confidenceText, dateText;

        ViewHolder(View view) {
            super(view);
            diseaseText = view.findViewById(R.id.tv_disease);
            confidenceText = view.findViewById(R.id.tv_confidence);
            dateText = view.findViewById(R.id.tv_date);
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
Type-safe HTTP client for Android. Converts HTTP API into Java interface.

**Why use Retrofit in LeafGuard?**
- Easy to use (annotations-based)
- Automatic JSON parsing
- Built-in error handling
- Supports synchronous and asynchronous calls
- Well-maintained by Square

### LeafGuard API Design

**Base URL:** `http://192.168.1.5:8000` (local development) or `https://api.leafguard.com` (production)

**Endpoints:**

1. **/predict** - Disease detection
   - Method: POST
   - Body: Multipart form-data with image file
   - Response: JSON with disease, confidence, symptoms, treatment

2. **/disease/{name}** - Get disease details
   - Method: GET
   - Path parameter: disease name
   - Response: JSON with full disease information

### Retrofit Implementation in LeafGuard

**Step 1: Define API Interface**
```java
// ApiService.java
public interface ApiService {

    @Multipart
    @POST("predict")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);

    @GET("disease/{name}")
    Call<DiseaseInfo> getDiseaseInfo(@Path("name") String diseaseName);
}
```

**Step 2: Create Retrofit Client**
```java
// RetrofitClient.java
public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.1.5:8000/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Create OkHttp client with timeouts
            OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor()) // Log requests/responses
                .build();

            // Create Retrofit instance
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()) // JSON parsing
                .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
```

**Step 3: Define Response Models**
```java
// UploadResponse.java
public class UploadResponse {
    private boolean success;

    @SerializedName("disease")
    private String diseaseName;

    private float confidence;
    private String symptoms;
    private String treatment;
    private String prevention;

    // Getters and setters
    public String getDiseaseName() { return diseaseName; }
    // ...
}
```

**Step 4: Make API Call in Repository**
```java
// ScanRepository.java
public void detectDisease(String imagePath, Callback<DiseaseResult> callback) {
    // Prepare image file
    File imageFile = new File(imagePath);
    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestBody);

    // Make API call
    ApiService apiService = RetrofitClient.getApiService();
    Call<UploadResponse> call = apiService.uploadImage(imagePart);

    call.enqueue(new retrofit2.Callback<UploadResponse>() {
        @Override
        public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                UploadResponse apiResponse = response.body();

                // Convert to domain model
                DiseaseResult result = new DiseaseResult(
                    apiResponse.getDiseaseName(),
                    apiResponse.getConfidence(),
                    apiResponse.getSymptoms(),
                    apiResponse.getTreatment(),
                    apiResponse.getPrevention()
                );

                callback.onSuccess(result);
            } else {
                callback.onError(new Exception("API Error: " + response.code()));
            }
        }

        @Override
        public void onFailure(Call<UploadResponse> call, Throwable t) {
            callback.onError(new Exception("Network Error: " + t.getMessage()));
        }
    });
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

    public void close() {
        if (tflite != null) {
            tflite.close();
        }
    }
}
```

**Step 4: Use in ViewModel**
```java
// ScanViewModel.java
public class ScanViewModel extends ViewModel {
    private TFLiteInference tfliteInference;

    public void classifyOffline(Bitmap bitmap) {
        // Run on background thread
        new Thread(() -> {
            DiseaseResult result = tfliteInference.classify(bitmap);

            // Update UI on main thread
            scanResult.postValue(Resource.success(result));
        }).start();
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

**File:** `app/src/main/assets/disease_library.xml`

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
            InputStream inputStream = context.getAssets().open("disease_library.xml");

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
2. **Three-tier architecture:** Presentation → Business Logic → Data
3. **MVVM pattern:** Separates UI, logic, and data for maintainability
4. **6+ Activities:** MainActivity, ScanActivity, ResultActivity, HistoryActivity, etc.
5. **Networking:** Retrofit for REST API communication with FastAPI backend
6. **Database:** Room for local scan history storage
7. **ML Integration:** Cloud AI (TensorFlow on server) and Offline AI (TFLite on device)
8. **XML Parsing:** Disease library information stored in XML

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
