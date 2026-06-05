# Android Development Exercises

## Overview

This directory contains hands-on Android development exercises for the LeafGuard AI project.
These exercises build your practical skills progressively, from basic UI components to advanced
features like offline AI inference. All code examples are in **Java** (the CSE 2206 standard).

## Weekly Mapping

| Week | Topic | Exercises |
|------|-------|-----------|
| 2 | Android Basics & UI | 1.1 – 2.3 |
| 3 | Camera & Gallery | 3.1 – 3.5 |
| 5 | Retrofit Networking | 4.1 – 4.5 |
| 7 | Room Database | 5.1 – 5.6 |
| 9 | TensorFlow Lite | 6.1 – 6.5 |
| 10 | Notifications & Share | 7.1 – 7.4 |
| 11 | Integration & Testing | 8.1 – 8.4 |

---

## 1. UI and Layouts (Week 2)

### Exercise 1.1: Create MainActivity Layout

**Goal:** Build the LeafGuard AI home screen with Material Design buttons.

**Tasks:**
1. Open `res/layout/activity_main.xml`
2. Use `ConstraintLayout` as root
3. Add an `ImageView` for the app logo (top, centered)
4. Add a `TextView` for the app tagline: "Scan. Detect. Protect."
5. Add four `MaterialButton` components:
   - "📷 Take Photo" → opens ScanActivity with camera
   - "🖼️ Choose from Gallery" → opens ScanActivity with gallery
   - "📋 View Scan History" → opens HistoryActivity
   - "📚 Disease Library" → opens DiseaseLibraryActivity
6. Constrain buttons to center vertically with 16dp spacing

**Verification:**
- [ ] Layout renders without overlap in Layout Editor
- [ ] All four buttons visible and readable on a 5-inch screen preview
- [ ] No lint errors about missing constraints

---

### Exercise 1.2: Design ScanActivity Layout

**Goal:** Create the image capture/display screen.

**Tasks:**
1. Create `res/layout/activity_scan.xml`
2. Add an `ImageView` (placeholder: `@drawable/ic_leaf_placeholder`) — takes 60% of screen height
3. Add a `ProgressBar` (initially GONE, shown during analysis)
4. Add two buttons at the bottom: "Camera" and "Gallery"
5. Add an "Analyze Image" button (initially DISABLED, enabled after image selected)

**Java code in ScanActivity.java:**
```java
// Enable button when image is loaded
private void onImageReady(Uri imageUri) {
    this.currentImageUri = imageUri;
    imageView.setImageURI(imageUri);
    btnAnalyze.setEnabled(true);
}
```

**Verification:**
- [ ] ImageView fills most of the screen
- [ ] Analyze button starts disabled
- [ ] Layout looks correct on API 24 and API 33 previews

---

### Exercise 1.3: Design ResultActivity Layout

**Goal:** Display disease detection results clearly.

**Tasks:**
1. Create `res/layout/activity_result.xml`
2. Add: disease name (large `TextView`), confidence percentage, leaf image thumbnail
3. Add expandable section for Symptoms (use `TextView` with toggle)
4. Add expandable section for Treatment
5. Add expandable section for Prevention
6. Add two action buttons: "Save to History" and "Share Result"

**Verification:**
- [ ] All text fields visible with appropriate font sizes
- [ ] Action buttons at the bottom, not overlapping content
- [ ] Scrollable if content is long (`ScrollView` wrapping content)

---

## 2. Intents and Navigation (Week 2)

### Exercise 2.1: Implement Activity Navigation

**Goal:** Wire up all button clicks in MainActivity to navigate correctly.

```java
// In MainActivity.java
btnCamera.setOnClickListener(v -> {
    Intent intent = new Intent(this, ScanActivity.class);
    intent.putExtra("source", "camera");
    startActivity(intent);
});

btnGallery.setOnClickListener(v -> {
    Intent intent = new Intent(this, ScanActivity.class);
    intent.putExtra("source", "gallery");
    startActivity(intent);
});

btnHistory.setOnClickListener(v ->
    startActivity(new Intent(this, HistoryActivity.class)));

btnDiseaseLibrary.setOnClickListener(v ->
    startActivity(new Intent(this, DiseaseLibraryActivity.class)));
```

**Verification:**
- [ ] Each button opens the correct Activity
- [ ] Back button returns to MainActivity from each Activity
- [ ] No crash when rapidly tapping buttons

---

### Exercise 2.2: Pass Data Between Activities

**Goal:** Send disease result from ScanActivity to ResultActivity.

```java
// In ScanActivity.java — after receiving prediction result
private void showResult(String diseaseName, double confidence, String imagePath) {
    Intent intent = new Intent(this, ResultActivity.class);
    intent.putExtra("disease_name", diseaseName);
    intent.putExtra("confidence", confidence);
    intent.putExtra("image_path", imagePath);
    startActivity(intent);
}

// In ResultActivity.java — onCreate
String diseaseName = getIntent().getStringExtra("disease_name");
double confidence = getIntent().getDoubleExtra("confidence", 0.0);
String imagePath = getIntent().getStringExtra("image_path");

tvDiseaseName.setText(diseaseName);
tvConfidence.setText(String.format("%.1f%% confidence", confidence * 100));
Glide.with(this).load(imagePath).into(ivLeafImage);
```

**Verification:**
- [ ] Disease name appears correctly in ResultActivity
- [ ] Confidence shows as a percentage (e.g., "87.3% confidence")
- [ ] Image loads from path without crashing

---

### Exercise 2.3: Result Handling (ActivityResultLauncher)

**Goal:** Use modern ActivityResult API for returning data from sub-activities.

```java
// In ScanActivity.java
private final ActivityResultLauncher<Intent> cameraLauncher =
    registerForActivityResult(new ActivityResultContracts.TakePicture(),
        success -> {
            if (success) {
                onImageReady(currentPhotoUri);
            } else {
                Toast.makeText(this, "Camera cancelled", Toast.LENGTH_SHORT).show();
            }
        });
```

**Verification:**
- [ ] Camera cancelled → no crash, toast shown
- [ ] Camera success → image displays in ScanActivity
- [ ] Method uses ActivityResultLauncher (not deprecated startActivityForResult)

---

## 3. Camera and Gallery Integration (Week 3)

### Exercise 3.1: Configure FileProvider

**Goal:** Set up FileProvider so camera can write to app-specific storage.

**Step 1 — Add to `res/xml/file_paths.xml`** (create the file if it doesn't exist):
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-files-path name="my_images" path="Pictures/" />
    <cache-path name="shared_images" path="images/" />
</paths>
```

**Step 2 — Add to `AndroidManifest.xml`** inside `<application>`:
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

**Verification:**
- [ ] Project builds without "Missing provider" warnings
- [ ] Camera capture works without `FileUriExposedException`

---

### Exercise 3.2: Implement Camera Capture

**Goal:** Full camera capture flow using TakePicture contract.

```java
private Uri currentPhotoUri;

private final ActivityResultLauncher<Uri> takePictureLauncher =
    registerForActivityResult(new ActivityResultContracts.TakePicture(),
        success -> {
            if (success) {
                onImageReady(currentPhotoUri);
            }
        });

private void launchCamera() {
    try {
        File photoFile = createImageFile();
        currentPhotoUri = FileProvider.getUriForFile(
            this,
            getPackageName() + ".fileprovider",
            photoFile
        );
        takePictureLauncher.launch(currentPhotoUri);
    } catch (IOException e) {
        Log.e(TAG, "Error creating image file", e);
        Toast.makeText(this, "Error preparing camera", Toast.LENGTH_SHORT).show();
    }
}

private File createImageFile() throws IOException {
    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        .format(new Date());
    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    return File.createTempFile("LEAFGUARD_" + timestamp, ".jpg", storageDir);
}
```

**Verification:**
- [ ] Camera app opens when button tapped
- [ ] Captured photo appears in ImageView
- [ ] File exists in app's external pictures directory

---

### Exercise 3.3: Implement Gallery Picker (Cross-Version)

**Goal:** Open gallery with Photo Picker on Android 13+ and legacy picker on older versions.

```java
// Modern Photo Picker (Android 13+ / API 33+)
private final ActivityResultLauncher<PickVisualMediaRequest> photoPickerLauncher =
    registerForActivityResult(new ActivityResultContracts.PickVisualMedia(),
        uri -> {
            if (uri != null) {
                onImageReady(uri);
            }
        });

// Legacy gallery picker (Android 6–12)
private final ActivityResultLauncher<Intent> legacyGalleryLauncher =
    registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                if (uri != null) {
                    onImageReady(uri);
                }
            }
        });

private void openGallery() {
    if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(this)) {
        photoPickerLauncher.launch(
            new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()
        );
    } else {
        Intent intent = new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        legacyGalleryLauncher.launch(intent);
    }
}
```

**Verification:**
- [ ] On API 33 emulator: Photo Picker appears (system UI)
- [ ] On API 28 emulator: Legacy gallery picker appears
- [ ] Selected image displays in ImageView

---

### Exercise 3.4: URI to Bitmap Conversion (Memory Safe)

**Goal:** Convert gallery/camera URI to Bitmap without OutOfMemoryError.

```java
private static final int TARGET_WIDTH = 224;
private static final int TARGET_HEIGHT = 224;

public Bitmap loadScaledBitmap(Uri imageUri) throws IOException {
    // Step 1: Get image dimensions without loading full bitmap
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    try (InputStream is = getContentResolver().openInputStream(imageUri)) {
        BitmapFactory.decodeStream(is, null, options);
    }

    // Step 2: Calculate safe sample size
    options.inSampleSize = calculateInSampleSize(
        options, TARGET_WIDTH, TARGET_HEIGHT);
    options.inJustDecodeBounds = false;

    // Step 3: Load scaled bitmap
    try (InputStream is = getContentResolver().openInputStream(imageUri)) {
        Bitmap scaled = BitmapFactory.decodeStream(is, null, options);
        // Step 4: Scale to exact target size
        return Bitmap.createScaledBitmap(scaled, TARGET_WIDTH, TARGET_HEIGHT, true);
    }
}

private int calculateInSampleSize(BitmapFactory.Options options,
                                  int reqWidth, int reqHeight) {
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
    return inSampleSize;
}
```

**Verification:**
- [ ] Large image (e.g. 4000×3000) loads without crash
- [ ] Logcat shows no OutOfMemoryError
- [ ] Returned bitmap is exactly 224×224

---

### Exercise 3.5: Handle Permissions for Camera and Gallery

**Goal:** Request CAMERA and storage permissions with proper rationale.

```java
private final ActivityResultLauncher<String> cameraPermissionLauncher =
    registerForActivityResult(new ActivityResultContracts.RequestPermission(),
        granted -> {
            if (granted) {
                launchCamera();
            } else {
                showPermissionDeniedMessage("Camera");
            }
        });

private void checkCameraPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
        launchCamera();
    } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
        // Show explanation dialog first
        new AlertDialog.Builder(this)
            .setTitle("Camera Permission Required")
            .setMessage("LeafGuard AI needs camera access to capture leaf images for disease detection.")
            .setPositiveButton("Grant", (d, w) ->
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA))
            .setNegativeButton("Cancel", null)
            .show();
    } else {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
    }
}

private void showPermissionDeniedMessage(String permission) {
    Snackbar.make(binding.getRoot(),
        permission + " permission denied. Enable in Settings.",
        Snackbar.LENGTH_LONG)
        .setAction("Settings", v -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getPackageName(), null));
            startActivity(intent);
        })
        .show();
}
```

**Verification:**
- [ ] First launch: permission dialog appears
- [ ] Denied: Snackbar with Settings link appears
- [ ] Granted: camera opens immediately

---

## 4. Retrofit Networking (Week 5)

### Exercise 4.1: Add Retrofit Dependencies

**Goal:** Configure Retrofit in build.gradle.

Add to `app/build.gradle` in the `dependencies` block:
```gradle
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'
```

Also add network permission to `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

And for HTTP connections (development only), add to `<application>`:
```xml
android:usesCleartextTraffic="true"
```

**Verification:**
- [ ] Gradle sync succeeds
- [ ] No duplicate dependency warnings
- [ ] App compiles with Retrofit on classpath

---

### Exercise 4.2: Define API Interface

**Goal:** Create the Retrofit API interface for the /predict endpoint.

```java
// ApiService.java
public interface ApiService {

    @Multipart
    @POST("predict")
    Call<PredictionResponse> predict(
        @Part MultipartBody.Part image
    );

    @GET("/")
    Call<HealthResponse> checkHealth();

    @GET("diseases")
    Call<DiseasesResponse> getDiseases();
}
```

Create the response model classes:
```java
// PredictionResponse.java
public class PredictionResponse {
    @SerializedName("disease")
    private String disease;

    @SerializedName("confidence")
    private double confidence;

    @SerializedName("symptoms")
    private String symptoms;

    @SerializedName("treatment")
    private String treatment;

    @SerializedName("prevention")
    private String prevention;

    // Getters
    public String getDisease() { return disease; }
    public double getConfidence() { return confidence; }
    public String getSymptoms() { return symptoms; }
    public String getTreatment() { return treatment; }
    public String getPrevention() { return prevention; }
}
```

**Verification:**
- [ ] Interface compiles with `@Multipart` annotation
- [ ] PredictionResponse fields match FastAPI response JSON

---

### Exercise 4.3: Build Retrofit Client Singleton

**Goal:** Create a thread-safe Retrofit client.

```java
// RetrofitClient.java
public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8000/";
    // Use 10.0.2.2 for emulator (maps to localhost on host machine)
    // Use your computer's IP for physical device (e.g., "http://192.168.1.100:8000/")

    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS) // longer for image upload
                .addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }
}
```

**Verification:**
- [ ] Singleton returns same Retrofit instance
- [ ] LoggingInterceptor prints requests in Logcat during development
- [ ] Timeouts are set appropriately for image uploads

---

### Exercise 4.4: Implement Image Upload

**Goal:** Convert bitmap to multipart body and call the prediction API.

```java
// In ScanActivity.java or a Repository class
private void uploadImageForPrediction(Bitmap bitmap) {
    // Convert bitmap to byte array
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
    byte[] imageBytes = baos.toByteArray();

    // Create multipart body part
    RequestBody requestFile = RequestBody.create(
        MediaType.parse("image/jpeg"),
        imageBytes
    );
    MultipartBody.Part body = MultipartBody.Part.createFormData(
        "file",      // field name — must match FastAPI parameter name
        "leaf.jpg",  // filename
        requestFile
    );

    // Make the API call
    showLoading(true);
    ApiService api = RetrofitClient.getApiService();
    api.predict(body).enqueue(new Callback<PredictionResponse>() {
        @Override
        public void onResponse(@NonNull Call<PredictionResponse> call,
                               @NonNull Response<PredictionResponse> response) {
            showLoading(false);
            if (response.isSuccessful() && response.body() != null) {
                onPredictionSuccess(response.body());
            } else {
                onPredictionError("Server error: " + response.code());
            }
        }

        @Override
        public void onFailure(@NonNull Call<PredictionResponse> call,
                              @NonNull Throwable t) {
            showLoading(false);
            if (t instanceof java.net.UnknownHostException) {
                // No internet — switch to offline mode
                runOfflineInference(bitmap);
            } else {
                onPredictionError("Network error: " + t.getMessage());
            }
        }
    });
}
```

**Verification:**
- [ ] Start FastAPI server locally, run app on emulator
- [ ] Logcat shows the HTTP POST request and JSON response
- [ ] Disease name appears in ResultActivity after upload

---

### Exercise 4.5: Error Handling and Loading State

**Goal:** Show proper feedback during upload and on errors.

```java
private void showLoading(boolean isLoading) {
    runOnUiThread(() -> {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnAnalyze.setEnabled(!isLoading);
        btnCamera.setEnabled(!isLoading);
        btnGallery.setEnabled(!isLoading);
    });
}

private void onPredictionError(String message) {
    runOnUiThread(() -> {
        new AlertDialog.Builder(this)
            .setTitle("Detection Failed")
            .setMessage(message + "\n\nTry offline detection instead?")
            .setPositiveButton("Try Offline", (d, w) -> runOfflineInference(currentBitmap))
            .setNegativeButton("Cancel", null)
            .show();
    });
}
```

**Verification:**
- [ ] ProgressBar shows while upload in progress
- [ ] Buttons disabled during upload (no double-submit)
- [ ] Error dialog offers offline fallback
- [ ] No NetworkOnMainThreadException crashes

---

## 5. Room Database (Week 7)

> **Note:** All Room code uses Java annotations. Do NOT use Kotlin data classes.

### Exercise 5.1: Create ScanHistory Entity

```java
// ScanHistory.java
@Entity(tableName = "scan_history")
public class ScanHistory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "disease_name")
    private String diseaseName;

    @ColumnInfo(name = "confidence")
    private double confidence;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    // Required no-arg constructor for Room
    public ScanHistory() {}

    // Convenience constructor
    public ScanHistory(String diseaseName, double confidence,
                       long timestamp, String imagePath) {
        this.diseaseName = diseaseName;
        this.confidence = confidence;
        this.timestamp = timestamp;
        this.imagePath = imagePath;
    }

    // Getters and setters (generate with Alt+Insert in Android Studio)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDiseaseName() { return diseaseName; }
    public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
```

**Verification:**
- [ ] `@Entity` annotation present
- [ ] `@PrimaryKey(autoGenerate = true)` on id field
- [ ] Build succeeds (Room validates entity at compile time)

---

### Exercise 5.2: Create DAO Interface

```java
// ScanHistoryDao.java
@Dao
public interface ScanHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ScanHistory scanHistory);

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    LiveData<List<ScanHistory>> getAllScans();

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<ScanHistory>> getRecentScans(int limit);

    @Query("SELECT * FROM scan_history WHERE id = :id")
    ScanHistory getById(int id);

    @Query("SELECT * FROM scan_history WHERE disease_name LIKE :query ORDER BY timestamp DESC")
    LiveData<List<ScanHistory>> searchByDisease(String query);

    @Delete
    void delete(ScanHistory scanHistory);

    @Query("DELETE FROM scan_history")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM scan_history")
    int getCount();
}
```

**Verification:**
- [ ] All query methods compile without errors
- [ ] LiveData used for observable queries (not plain List)
- [ ] `LIKE :query` enables search functionality

---

### Exercise 5.3: Build LeafGuardDatabase Singleton

```java
// LeafGuardDatabase.java
@Database(entities = {ScanHistory.class}, version = 1, exportSchema = false)
public abstract class LeafGuardDatabase extends RoomDatabase {

    public abstract ScanHistoryDao scanHistoryDao();

    private static volatile LeafGuardDatabase INSTANCE;

    public static LeafGuardDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LeafGuardDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        LeafGuardDatabase.class,
                        "leafguard_db"
                    )
                    .fallbackToDestructiveMigration() // for development only
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
```

**Verification:**
- [ ] Singleton uses double-checked locking
- [ ] `@Database` annotation lists all entities
- [ ] `exportSchema = false` suppresses schema export warning in development

---

### Exercise 5.4: Implement Repository Pattern

```java
// ScanHistoryRepository.java
public class ScanHistoryRepository {
    private final ScanHistoryDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ScanHistoryRepository(Application application) {
        LeafGuardDatabase db = LeafGuardDatabase.getInstance(application);
        dao = db.scanHistoryDao();
    }

    public LiveData<List<ScanHistory>> getAllScans() {
        return dao.getAllScans();
    }

    public LiveData<List<ScanHistory>> searchByDisease(String query) {
        return dao.searchByDisease("%" + query + "%");
    }

    public void insert(ScanHistory scan) {
        executor.execute(() -> dao.insert(scan));
    }

    public void delete(ScanHistory scan) {
        executor.execute(() -> dao.delete(scan));
    }

    public void deleteAll() {
        executor.execute(dao::deleteAll);
    }
}
```

**Verification:**
- [ ] All write operations run on background thread (ExecutorService)
- [ ] Read operations return LiveData (Room handles background threading)
- [ ] No NetworkOnMainThreadException or Room main-thread exceptions

---

### Exercise 5.5: Create HistoryViewModel

```java
// HistoryViewModel.java
public class HistoryViewModel extends AndroidViewModel {
    private final ScanHistoryRepository repository;
    private final LiveData<List<ScanHistory>> allScans;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    public HistoryViewModel(Application application) {
        super(application);
        repository = new ScanHistoryRepository(application);
        allScans = repository.getAllScans();
    }

    public LiveData<List<ScanHistory>> getAllScans() {
        return allScans;
    }

    public void insert(ScanHistory scan) {
        repository.insert(scan);
    }

    public void delete(ScanHistory scan) {
        repository.delete(scan);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
```

**Verification:**
- [ ] Extends `AndroidViewModel` (provides Application context)
- [ ] Data survives screen rotation (ViewModel lifecycle)
- [ ] Repository created once in constructor

---

### Exercise 5.6: Wire HistoryActivity with RecyclerView

```java
// HistoryActivity.java (key parts)
public class HistoryActivity extends AppCompatActivity {
    private HistoryViewModel viewModel;
    private ScanHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        adapter = new ScanHistoryAdapter(scan -> {
            // Click: open ResultActivity with this scan's data
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("disease_name", scan.getDiseaseName());
            intent.putExtra("confidence", scan.getConfidence());
            intent.putExtra("image_path", scan.getImagePath());
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        viewModel.getAllScans().observe(this, scans -> {
            adapter.submitList(scans);
            // Show empty state if no scans
            emptyStateView.setVisibility(scans.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }
}
```

**Verification:**
- [ ] History list shows after performing a scan
- [ ] List updates automatically when new scan saved
- [ ] Empty state message shown when no scans exist

---

## 6. TensorFlow Lite Offline Inference (Week 9)

### Exercise 6.1: Add Model to Assets

1. Obtain `plant_disease_model.tflite` (see `model/model-acquisition-guide.md`)
2. Copy file to `app/src/main/assets/`
3. Create `app/src/main/assets/labels.txt` with one class per line (38 classes)

Add TFLite dependency to `app/build.gradle`:
```gradle
implementation 'org.tensorflow:tensorflow-lite:2.13.0'
implementation 'org.tensorflow:tensorflow-lite-support:0.4.4'
```

Add to `android {}` block in `app/build.gradle`:
```gradle
aaptOptions {
    noCompress "tflite"
}
```

**Verification:**
- [ ] `plant_disease_model.tflite` visible in `assets/` in Project view
- [ ] Build succeeds without asset compression errors

---

### Exercise 6.2: Load TFLite Model

```java
// DiseaseClassifier.java
public class DiseaseClassifier implements Closeable {
    private static final String MODEL_FILE = "plant_disease_model.tflite";
    private static final String LABELS_FILE = "labels.txt";
    private static final int IMAGE_SIZE = 224;
    private static final int NUM_THREADS = 4;

    private final Interpreter interpreter;
    private final List<String> labels;

    public DiseaseClassifier(Context context) throws IOException {
        interpreter = new Interpreter(loadModelFile(context),
            new Interpreter.Options().setNumThreads(NUM_THREADS));
        labels = loadLabels(context);
        Log.i("DiseaseClassifier", "Model loaded. " + labels.size() + " classes.");
    }

    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor afd = context.getAssets().openFd(MODEL_FILE);
        FileInputStream fis = new FileInputStream(afd.getFileDescriptor());
        FileChannel channel = fis.getChannel();
        return channel.map(FileChannel.MapMode.READ_ONLY,
            afd.getStartOffset(), afd.getDeclaredLength());
    }

    private List<String> loadLabels(Context context) throws IOException {
        List<String> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(context.getAssets().open(LABELS_FILE)))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    result.add(line.trim());
                }
            }
        }
        return result;
    }

    @Override
    public void close() {
        if (interpreter != null) interpreter.close();
    }
}
```

**Verification:**
- [ ] No `FileNotFoundException` in Logcat
- [ ] Log shows "Model loaded. 38 classes."

---

### Exercise 6.3: Preprocess Bitmap for Inference

```java
// In DiseaseClassifier.java
private ByteBuffer preprocessBitmap(Bitmap bitmap) {
    // Resize to model input size
    Bitmap resized = Bitmap.createScaledBitmap(bitmap, IMAGE_SIZE, IMAGE_SIZE, true);

    // Allocate buffer: 1 image × 224×224 pixels × 3 channels × 4 bytes (float32)
    ByteBuffer buffer = ByteBuffer.allocateDirect(1 * IMAGE_SIZE * IMAGE_SIZE * 3 * 4);
    buffer.order(ByteOrder.nativeOrder());

    int[] pixels = new int[IMAGE_SIZE * IMAGE_SIZE];
    resized.getPixels(pixels, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE);

    for (int pixel : pixels) {
        // Extract RGB channels and normalize to [0, 1]
        buffer.putFloat(((pixel >> 16) & 0xFF) / 255.0f);  // R
        buffer.putFloat(((pixel >>  8) & 0xFF) / 255.0f);  // G
        buffer.putFloat(( pixel        & 0xFF) / 255.0f);  // B
    }

    return buffer;
}
```

> **Note on normalization:** Use `/255.0f` (range 0–1) if the model was trained with `rescale=1./255`.
> Use `(pixel - 127.5f) / 127.5f` (range -1 to 1) if trained with MobileNet preprocessing.
> Check your model's training code to confirm which normalization to use.

**Verification:**
- [ ] Buffer size = 224 × 224 × 3 × 4 = 602,112 bytes
- [ ] Values are in range [0.0, 1.0] (log a few to check)

---

### Exercise 6.4: Run Inference and Parse Output

```java
// In DiseaseClassifier.java
public static class Prediction {
    public final String label;
    public final float confidence;
    public Prediction(String label, float confidence) {
        this.label = label;
        this.confidence = confidence;
    }
}

public List<Prediction> classify(Bitmap bitmap) {
    ByteBuffer input = preprocessBitmap(bitmap);
    // Output shape: [1][38] — one row, 38 class probabilities
    float[][] output = new float[1][labels.size()];

    interpreter.run(input, output);

    // Build sorted list of predictions
    List<Prediction> predictions = new ArrayList<>();
    for (int i = 0; i < labels.size(); i++) {
        predictions.add(new Prediction(labels.get(i), output[0][i]));
    }
    predictions.sort((a, b) -> Float.compare(b.confidence, a.confidence));
    return predictions;
}
```

**Verification:**
- [ ] Returns a list of 38 predictions
- [ ] Top prediction's confidence is highest
- [ ] Use test image from `sample-images/` and log the top-3 results

---

### Exercise 6.5: Integrate Offline Fallback

```java
// In ScanActivity.java
private void runOfflineInference(Bitmap bitmap) {
    showLoading(true);
    tvStatus.setText("Running offline detection...");

    new Thread(() -> {
        try {
            DiseaseClassifier classifier = new DiseaseClassifier(this);
            List<DiseaseClassifier.Prediction> predictions = classifier.classify(bitmap);
            classifier.close();

            DiseaseClassifier.Prediction top = predictions.get(0);
            if (top.confidence < 0.5f) {
                runOnUiThread(() -> {
                    showLoading(false);
                    showLowConfidenceWarning(top.confidence);
                });
            } else {
                runOnUiThread(() -> {
                    showLoading(false);
                    navigateToResult(top.label, top.confidence, currentImagePath);
                });
            }
        } catch (IOException e) {
            runOnUiThread(() -> {
                showLoading(false);
                Toast.makeText(this, "Offline model not available", Toast.LENGTH_LONG).show();
            });
        }
    }).start();
}
```

**Verification:**
- [ ] Disable Wi-Fi/data on emulator
- [ ] Tap Analyze → app uses offline model → result appears
- [ ] Low-confidence (<50%) shows warning dialog

---

## 7. Notifications and Share (Week 10)

### Exercise 7.1: Create Notification Channel

```java
// NotificationHelper.java
public class NotificationHelper {
    public static final String CHANNEL_ID = "leafguard_detections";
    public static final int NOTIFICATION_ID = 1001;

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Disease Detections",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notifications for plant disease detection results");
            NotificationManager manager =
                context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public static void sendDiseaseDetectedNotification(Context context,
                                                        String diseaseName,
                                                        double confidence) {
        createChannel(context);

        Intent intent = new Intent(context, ResultActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
            context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_leaf_notification)
            .setContentTitle("Disease Detected!")
            .setContentText(String.format("%s (%.0f%% confidence)",
                diseaseName, confidence * 100))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }
}
```

Call after a successful detection in ResultActivity:
```java
NotificationHelper.sendDiseaseDetectedNotification(this, diseaseName, confidence);
```

**Verification:**
- [ ] Notification appears in system tray after detection
- [ ] Tapping notification opens ResultActivity

---

### Exercise 7.2: Implement Share Functionality

```java
// In ResultActivity.java
private void shareResult(String diseaseName, double confidence) {
    String shareText = String.format(
        "🌿 LeafGuard AI Detection Result\n" +
        "Disease: %s\n" +
        "Confidence: %.1f%%\n" +
        "Scan time: %s\n\n" +
        "Detected with LeafGuard AI app",
        diseaseName, confidence * 100,
        new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date())
    );

    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
    startActivity(Intent.createChooser(shareIntent, "Share via"));
}
```

**Verification:**
- [ ] Share chooser opens (WhatsApp, Gmail, etc.)
- [ ] Text includes disease name and confidence

---

### Exercise 7.3: Share Result with Image

```java
private void shareResultWithImage(String diseaseName, String imagePath) {
    Uri imageUri = FileProvider.getUriForFile(
        this,
        getPackageName() + ".fileprovider",
        new File(imagePath)
    );

    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("image/jpeg");
    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
    shareIntent.putExtra(Intent.EXTRA_TEXT, "Detected: " + diseaseName);
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    startActivity(Intent.createChooser(shareIntent, "Share leaf image"));
}
```

**Verification:**
- [ ] Image attaches to WhatsApp/email when sharing
- [ ] No `FileUriExposedException`

---

### Exercise 7.4: Add Location Tagging to Scan

```java
// LocationHelper.java
public class LocationHelper {
    public static void getLastLocation(Context context, Consumer<Location> callback) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            callback.accept(null);
            return;
        }
        FusedLocationProviderClient client =
            LocationServices.getFusedLocationProviderClient(context);
        client.getLastLocation()
            .addOnSuccessListener(callback::accept)
            .addOnFailureListener(e -> callback.accept(null));
    }
}

// Usage in ResultActivity when saving to history:
LocationHelper.getLastLocation(this, location -> {
    ScanHistory scan = new ScanHistory(diseaseName, confidence,
        System.currentTimeMillis(), imagePath);
    if (location != null) {
        scan.setLatitude(location.getLatitude());
        scan.setLongitude(location.getLongitude());
    }
    viewModel.insert(scan);
    Toast.makeText(this, "Saved to history", Toast.LENGTH_SHORT).show();
});
```

**Verification:**
- [ ] Location saved with scan when permission granted
- [ ] Scan still saves (with null location) if permission denied

---

## 8. Integration Exercises (Cross-Week, Week 11)

### Exercise 8.1: End-to-End Scan Flow Test

**Goal:** Verify the complete online scan path works without interruption.

**Steps:**
1. Start FastAPI server: `uvicorn main:app --reload`
2. Launch app on emulator (connected to same machine via `10.0.2.2`)
3. Grant camera permission
4. Take a photo of a leaf (or use a sample image from `sample-images/`)
5. Tap "Analyze"
6. Verify: disease name appears in ResultActivity
7. Tap "Save to History"
8. Go to HistoryActivity → verify scan appears

**Checklist:**
- [ ] Permission dialog handled correctly
- [ ] Upload shows progress bar
- [ ] Result displays disease name and confidence
- [ ] History shows the saved scan
- [ ] No crashes or ANR dialogs throughout

---

### Exercise 8.2: Offline Fallback Test

**Goal:** Verify TFLite offline path works when server is unreachable.

**Steps:**
1. Stop the FastAPI server
2. OR set `BASE_URL` to an invalid address
3. Launch app, take a photo, tap Analyze
4. Verify: "Offline detection" runs and shows result
5. Verify: Result still saves to Room DB

**Checklist:**
- [ ] App does NOT crash when network fails
- [ ] Offline detection completes within 5 seconds
- [ ] Result activity shows "Offline" indicator
- [ ] Scan saves to history even offline

---

### Exercise 8.3: Disease Library Integration Test

**Goal:** Verify XML-parsed disease info shows in ResultActivity.

**Steps:**
1. Perform any scan (online or offline)
2. In ResultActivity: verify Symptoms, Treatment, Prevention sections are populated
3. Open Disease Library: verify all diseases from XML are listed
4. Search for a disease: verify filtering works

**Checklist:**
- [ ] Symptoms from XML appear (not just model name)
- [ ] All diseases listed in DiseaseLibraryActivity
- [ ] Search by name filters correctly

---

### Exercise 8.4: Performance Baseline Measurement

**Goal:** Measure and record key performance metrics.

**Using Android Studio Profiler:**
1. Open Profiler: View → Tool Windows → Profiler
2. Click the + button, select your device
3. Run a scan and observe:
   - CPU % during image upload / TFLite inference
   - Memory usage before and after bitmap load
   - Network bytes sent per scan request

**Record your measurements:**

| Metric | Target | Measured |
|--------|--------|----------|
| Time to result (online) | < 5 sec | ___ sec |
| Time to result (offline) | < 3 sec | ___ sec |
| Memory during inference | < 80 MB | ___ MB |
| APK size (debug) | < 50 MB | ___ MB |

---

## How to Complete Exercises

1. **Read carefully** — understand the goal before writing code
2. **Build incrementally** — compile after every ~10 lines of new code
3. **Use Logcat** — add `Log.d(TAG, "...")` at key points
4. **Check the checklist** — each exercise has a `- [ ]` verification checklist
5. **Consult solutions** — if stuck >30 minutes, check `solutions/week-XX/`
6. **Document** — add a 1-line comment above each complex method

## Common Errors Quick Reference

| Error | Cause | Fix |
|-------|-------|-----|
| `FileUriExposedException` | Using `file://` URI with camera | Add FileProvider |
| `NetworkOnMainThreadException` | Retrofit call on main thread | Use `enqueue()` not `execute()` |
| `IllegalStateException: Cannot access database on main thread` | Room query on UI thread | Use LiveData or background thread |
| `OutOfMemoryError` | Loading full-resolution bitmap | Use `inSampleSize` scaling |
| `XmlPullParserException` | Malformed XML | Validate XML in browser first |
| `Resources$NotFoundException` | Missing layout/drawable | Check `res/` folder spelling |
| `ActivityNotFoundException` | Target Activity not in Manifest | Add Activity to AndroidManifest.xml |

## Assessment Criteria

| Category | Weight | Description |
|----------|--------|-------------|
| Functionality | 40% | Does the feature work as specified? |
| Code Quality | 25% | Clean, readable, no duplication |
| Error Handling | 20% | Graceful failure, no crashes |
| Documentation | 15% | Comments on complex logic |

## Resources

- [Android Developer Guide](https://developer.android.com/guide)
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- [TensorFlow Lite Android Quickstart](https://www.tensorflow.org/lite/android/quickstart)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Material Design Components](https://material.io/develop/android)
