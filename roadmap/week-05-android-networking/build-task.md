# Week 05 Build Task: Integrate Retrofit Networking into LeafGuard AI

## Objective

Integrate Retrofit HTTP client into your LeafGuard AI Android app to enable image upload to your FastAPI backend and display prediction results. By completion, users can tap a "Detect Disease" button, upload their captured image, and see the disease prediction returned from the backend.

**What You're Building:**
- Retrofit client configuration
- API service interface for `/predict` endpoint
- Image-to-MultipartBody conversion logic
- Network request with loading indicator
- Success handling with navigation to ResultActivity
- Comprehensive error handling with user feedback

**Estimated Time:** 6-8 hours (spread over 3-4 days)

---

## Prerequisites

Before starting, ensure:

- [ ] Week 03 complete: Camera/gallery image capture working
- [ ] Week 04 complete: FastAPI backend running and tested with Postman
- [ ] Both phone and laptop on same Wi-Fi network
- [ ] You know your laptop's local IP address (e.g., 192.168.1.10)
- [ ] Backend accessible from phone browser: `http://YOUR_IP:8000/docs`

**Test backend first:**
```bash
# On laptop, run FastAPI:
cd backend-api
uvicorn main:app --host 0.0.0.0 --port 8000

# On phone browser, visit:
http://192.168.1.10:8000/docs
```

If you can see the API documentation, you're ready to proceed.

---

## Implementation Steps

### Step 1: Add Gradle Dependencies (15 minutes)

#### 1.1 Open `build.gradle (Module: app)`

Navigate to your app-level `build.gradle` file.

#### 1.2 Add Dependencies

Inside the `dependencies` block, add:

```gradle
// Retrofit for HTTP networking
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

// OkHttp logging (for debugging)
implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'
```

#### 1.3 Sync Gradle

Click **"Sync Now"** in the banner that appears. Wait for sync to complete successfully.

**Troubleshooting:**
- If sync fails with version conflict, try Retrofit 2.9.0 and Gson 2.9.0
- Check that you have internet connection
- Try **File → Invalidate Caches / Restart** if persistent issues

#### Verification

- [ ] Gradle sync completed successfully
- [ ] No red errors in build.gradle
- [ ] Build → Make Project succeeds

---

### Step 2: Create Data Models (30 minutes)

#### 2.1 Create PredictionResponse.java

Create new Java class in your package:

**Right-click package → New → Java Class → Name: `PredictionResponse`**

```java
package com.yourname.leafguardai;

public class PredictionResponse {
    private String disease;
    private double confidence;
    private String symptoms;
    private String treatment;
    private String prevention;

    // Constructor
    public PredictionResponse() {}

    // Getters
    public String getDisease() {
        return disease;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getPrevention() {
        return prevention;
    }

    // Setters (optional, Gson uses them internally)
    public void setDisease(String disease) {
        this.disease = disease;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setPrevention(String prevention) {
        this.prevention = prevention;
    }
}
```

**Important:** Field names must match your FastAPI JSON response keys exactly.

#### 2.2 Verify Against Backend Response

Start your FastAPI backend and check the response format with Postman or browser:

```json
{
  "disease": "Tomato Late Blight",
  "confidence": 0.87,
  "symptoms": "Brown spots on leaves...",
  "treatment": "Apply copper fungicide...",
  "prevention": "Remove infected leaves..."
}
```

Make sure your Java class fields match these keys.

#### Verification

- [ ] PredictionResponse.java created
- [ ] Field names match FastAPI response
- [ ] All getters implemented
- [ ] Class compiles without errors

---

### Step 3: Create API Service Interface (30 minutes)

#### 3.1 Create ApiService.java

**Right-click package → New → Java Class → Name: `ApiService`**

Change `class` to `interface`:

```java
package com.yourname.leafguardai;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("predict")
    Call<PredictionResponse> uploadImage(@Part MultipartBody.Part image);
}
```

**Explanation:**
- `@Multipart`: Indicates multipart form-data content type
- `@POST("predict")`: HTTP POST to `/predict` endpoint
- `@Part`: Marks parameter as part of multipart request
- `Call<PredictionResponse>`: Async call returning PredictionResponse

#### Verification

- [ ] ApiService.java interface created
- [ ] @Multipart annotation present
- [ ] @POST("predict") matches your backend endpoint
- [ ] Method signature correct
- [ ] No compilation errors

---

### Step 4: Create Retrofit Client Singleton (45 minutes)

#### 4.1 Create RetrofitClient.java

**Right-click package → New → Java Class → Name: `RetrofitClient`**

```java
package com.yourname.leafguardai;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    // IMPORTANT: Replace with YOUR laptop's local IP address
    private static final String BASE_URL = "http://192.168.1.10:8000/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Logging interceptor (for debugging)
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp client with timeouts and logging
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
```

#### 4.2 Update BASE_URL with Your IP

**Find your laptop's IP:**

**Windows:**
```bash
ipconfig
# Look for "IPv4 Address" under your Wi-Fi adapter (e.g., 192.168.1.10)
```

**macOS/Linux:**
```bash
ifconfig | grep "inet "
# Look for 192.168.x.x address (not 127.0.0.1)
```

**Update BASE_URL in RetrofitClient.java:**
```java
private static final String BASE_URL = "http://192.168.1.10:8000/";  // Your IP here
```

#### Verification

- [ ] RetrofitClient.java created
- [ ] BASE_URL updated with your actual IP
- [ ] Singleton pattern implemented (static instance)
- [ ] Logging interceptor configured
- [ ] Timeouts set to 30 seconds
- [ ] GsonConverterFactory added
- [ ] getApiService() helper method created

---

### Step 5: Configure Network Security (30 minutes)

Android 9+ blocks HTTP traffic by default. We need to allow cleartext traffic to our local IP.

#### 5.1 Create res/xml/network_security_config.xml

**Right-click res folder → New → Android Resource Directory → Name: xml**

**Right-click res/xml folder → New → XML Resource File → Name: network_security_config**

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <!-- Allow cleartext traffic to local development IP -->
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">192.168.1.10</domain>
    </domain-config>
</network-security-config>
```

**Replace `192.168.1.10` with YOUR laptop IP.**

#### 5.2 Reference in AndroidManifest.xml

Add to `<application>` tag:

```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ... >
```

#### 5.3 Verify INTERNET Permission

Ensure this is in AndroidManifest.xml (should already be there from Week 04):

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

#### Verification

- [ ] network_security_config.xml created in res/xml/
- [ ] IP address matches your laptop IP
- [ ] Referenced in AndroidManifest.xml
- [ ] INTERNET permission present
- [ ] App builds successfully

---

### Step 6: Update MainActivity with Upload Logic (2 hours)

#### 6.1 Add ProgressBar to Layout

Open `activity_main.xml` (or your scan activity layout):

```xml
<ProgressBar
    android:id="@+id/progressBar"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:visibility="gone"
    android:layout_centerInParent="true"/>
```

#### 6.2 Add "Detect Disease" Button

```xml
<Button
    android:id="@+id/detectButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Detect Disease"
    android:enabled="false"/>
```

Button should be disabled until image is captured.

#### 6.3 Update MainActivity.java

Add these imports:

```java
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
```

Add member variables:

```java
private ProgressBar progressBar;
private Button detectButton;
private File currentImageFile;
```

In `onCreate()`:

```java
progressBar = findViewById(R.id.progressBar);
detectButton = findViewById(R.id.detectButton);

detectButton.setOnClickListener(v -> uploadImage());
```

#### 6.4 Save Captured Image to File

After camera/gallery result (in your existing `onActivityResult` or activity result callback):

```java
// After you have the Bitmap from camera/gallery
try {
    // Create file in cache directory
    currentImageFile = new File(getCacheDir(), "leaf_" + System.currentTimeMillis() + ".jpg");
    FileOutputStream fos = new FileOutputStream(currentImageFile);
    capturedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
    fos.close();

    // Enable detect button
    detectButton.setEnabled(true);

} catch (IOException e) {
    e.printStackTrace();
    Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
}
```

#### 6.5 Implement uploadImage() Method

```java
private void uploadImage() {
    if (currentImageFile == null || !currentImageFile.exists()) {
        Toast.makeText(this, "Please capture an image first", Toast.LENGTH_SHORT).show();
        return;
    }

    // Show loading state
    progressBar.setVisibility(View.VISIBLE);
    detectButton.setEnabled(false);

    // Create RequestBody from file
    RequestBody requestBody = RequestBody.create(
            MediaType.parse("image/*"),
            currentImageFile
    );

    // Create MultipartBody.Part
    MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
            "image",
            currentImageFile.getName(),
            requestBody
    );

    // Get API service
    ApiService apiService = RetrofitClient.getApiService();

    // Make network call
    Call<PredictionResponse> call = apiService.uploadImage(imagePart);

    call.enqueue(new Callback<PredictionResponse>() {
        @Override
        public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
            // Hide loading
            progressBar.setVisibility(View.GONE);
            detectButton.setEnabled(true);

            if (response.isSuccessful() && response.body() != null) {
                // SUCCESS: Got prediction
                PredictionResponse prediction = response.body();
                navigateToResult(prediction);
            } else {
                // HTTP error
                String errorMsg = "Server error: " + response.code();
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<PredictionResponse> call, Throwable t) {
            // Hide loading
            progressBar.setVisibility(View.GONE);
            detectButton.setEnabled(true);

            // Network error
            String errorMsg;
            if (t instanceof IOException) {
                errorMsg = "Network error. Check connection and ensure backend is running.";
            } else {
                errorMsg = "Error: " + t.getMessage();
            }
            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
        }
    });
}
```

#### 6.6 Implement navigateToResult() Method

```java
private void navigateToResult(PredictionResponse prediction) {
    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
    intent.putExtra("disease", prediction.getDisease());
    intent.putExtra("confidence", prediction.getConfidence());
    intent.putExtra("symptoms", prediction.getSymptoms());
    intent.putExtra("treatment", prediction.getTreatment());
    intent.putExtra("prevention", prediction.getPrevention());
    startActivity(intent);
}
```

#### Verification

- [ ] ProgressBar added to layout
- [ ] Detect button added and initially disabled
- [ ] Image saved to File after capture
- [ ] uploadImage() method implemented
- [ ] RequestBody and MultipartBody.Part created correctly
- [ ] Retrofit call made with enqueue()
- [ ] Loading state shown during upload
- [ ] onResponse() handles success and HTTP errors
- [ ] onFailure() handles network errors
- [ ] navigateToResult() passes data to ResultActivity
- [ ] No compilation errors

---

### Step 7: Update ResultActivity to Display Prediction (45 minutes)

#### 7.1 Update activity_result.xml Layout

```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:id="@+id/diseaseTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Disease Name"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:id="@+id/confidenceTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Confidence: 0%"
        android:textSize="18sp"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Symptoms:"
        android:textStyle="bold"
        android:textSize="16sp"/>

    <TextView
        android:id="@+id/symptomsTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Symptoms text"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Treatment:"
        android:textStyle="bold"
        android:textSize="16sp"/>

    <TextView
        android:id="@+id/treatmentTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Treatment text"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Prevention:"
        android:textStyle="bold"
        android:textSize="16sp"/>

    <TextView
        android:id="@+id/preventionTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Prevention text"/>

</LinearLayout>
```

#### 7.2 Update ResultActivity.java

```java
public class ResultActivity extends AppCompatActivity {

    private TextView diseaseTextView;
    private TextView confidenceTextView;
    private TextView symptomsTextView;
    private TextView treatmentTextView;
    private TextView preventionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize views
        diseaseTextView = findViewById(R.id.diseaseTextView);
        confidenceTextView = findViewById(R.id.confidenceTextView);
        symptomsTextView = findViewById(R.id.symptomsTextView);
        treatmentTextView = findViewById(R.id.treatmentTextView);
        preventionTextView = findViewById(R.id.preventionTextView);

        // Get data from Intent
        Intent intent = getIntent();
        String disease = intent.getStringExtra("disease");
        double confidence = intent.getDoubleExtra("confidence", 0.0);
        String symptoms = intent.getStringExtra("symptoms");
        String treatment = intent.getStringExtra("treatment");
        String prevention = intent.getStringExtra("prevention");

        // Display data
        diseaseTextView.setText(disease != null ? disease : "Unknown Disease");
        confidenceTextView.setText(String.format("Confidence: %.1f%%", confidence * 100));
        symptomsTextView.setText(symptoms != null ? symptoms : "No symptoms information");
        treatmentTextView.setText(treatment != null ? treatment : "No treatment information");
        preventionTextView.setText(prevention != null ? prevention : "No prevention information");
    }
}
```

#### Verification

- [ ] ResultActivity layout updated with all TextViews
- [ ] ResultActivity.java retrieves Intent extras
- [ ] Confidence formatted as percentage (multiply by 100)
- [ ] Null checks for all fields
- [ ] Data displays correctly when activity opened

---

### Step 8: Test Complete Flow (1 hour)

#### 8.1 Pre-Testing Checklist

- [ ] FastAPI backend running: `uvicorn main:app --host 0.0.0.0 --port 8000`
- [ ] Backend accessible from phone browser
- [ ] Phone and laptop on same Wi-Fi
- [ ] BASE_URL in RetrofitClient matches laptop IP
- [ ] Network security config has correct IP
- [ ] App builds and installs successfully

#### 8.2 Testing Steps

1. **Launch app**
2. **Capture image** with camera or select from gallery
3. **Verify:** Image displays in ImageView
4. **Verify:** "Detect Disease" button becomes enabled
5. **Tap "Detect Disease" button**
6. **Verify:** ProgressBar appears
7. **Verify:** Button becomes disabled (grayed out)
8. **Wait 1-3 seconds**
9. **Verify:** ResultActivity opens
10. **Verify:** Disease name displayed
11. **Verify:** Confidence shows as percentage (e.g., "87.3%")
12. **Verify:** Symptoms, treatment, prevention displayed

#### 8.3 Test Error Handling

**Test 1: Network Error**
- Turn off Wi-Fi on phone
- Try uploading image
- Expected: Toast shows "Network error. Check connection..."
- No crash

**Test 2: Backend Down**
- Stop FastAPI backend on laptop
- Try uploading image
- Expected: Toast shows error message
- No crash

**Test 3: No Image Captured**
- Launch app without capturing image
- Try tapping "Detect Disease" (if enabled)
- Expected: Toast shows "Please capture an image first"

#### Verification

- [ ] Complete flow works: camera → upload → result
- [ ] Loading indicator shows during upload
- [ ] Result displays correctly
- [ ] Network error handled gracefully
- [ ] Backend down scenario handled
- [ ] No crashes in any scenario
- [ ] Logcat shows HTTP requests (from logging interceptor)

---

### Step 9: Debug Using Logcat (30 minutes)

#### 9.1 View Network Logs

Run the app and upload an image. In Logcat, filter by "OkHttp":

```
D/OkHttp: --> POST http://192.168.1.10:8000/predict
D/OkHttp: Content-Type: multipart/form-data; boundary=...
D/OkHttp: Content-Length: 234567
D/OkHttp: --> END POST
D/OkHttp: <-- 200 OK http://192.168.1.10:8000/predict (1523ms)
D/OkHttp: Content-Type: application/json
D/OkHttp: {"disease":"Tomato Late Blight","confidence":0.87,...}
D/OkHttp: <-- END HTTP
```

#### 9.2 Common Issues and Fixes

**Issue: "Network error"**
- Check: Are phone and laptop on same Wi-Fi?
- Check: Is BASE_URL correct?
- Check: Can you access http://YOUR_IP:8000/docs from phone browser?

**Issue: "Server error: 404"**
- Check: Endpoint is `/predict` in both ApiService and FastAPI
- Check: FastAPI route is `@app.post("/predict")`

**Issue: "Server error: 500"**
- Check FastAPI terminal for error logs
- Backend code has a bug, not Android issue

**Issue: App crashes when uploading**
- Check Logcat for stack trace
- Likely: Missing null check or wrong data type in PredictionResponse

#### Verification

- [ ] Logcat shows HTTP request details
- [ ] Logcat shows response JSON
- [ ] Can read and understand network logs
- [ ] Can debug issues using Logcat

---

### Step 10: Final Polish and Commit (30 minutes)

#### 10.1 Remove Logging Interceptor (Production)

For production, comment out logging to avoid exposing sensitive data:

```java
// HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
// interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

OkHttpClient client = new OkHttpClient.Builder()
        // .addInterceptor(interceptor)  // Comment out for production
        .connectTimeout(30, TimeUnit.SECONDS)
        ...
```

**For now, keep it enabled for learning.**

#### 10.2 Add Strings to strings.xml

Move hardcoded strings to resources:

```xml
<string name="detect_disease">Detect Disease</string>
<string name="error_no_image">Please capture an image first</string>
<string name="error_network">Network error. Check connection and ensure backend is running.</string>
<string name="confidence_format">Confidence: %.1f%%</string>
```

#### 10.3 Test One More Time

Complete the full flow end-to-end to ensure everything works.

#### 10.4 Git Commit

```bash
git add .
git commit -m "week-05: integrate Retrofit networking for image upload and prediction display"
git push origin your-branch
```

#### Verification

- [ ] Complete flow tested successfully
- [ ] Strings moved to strings.xml where appropriate
- [ ] Code is clean and organized
- [ ] Git commit made with meaningful message

---

## Completion Checklist

### Technical Requirements

- [ ] Retrofit dependencies added successfully
- [ ] PredictionResponse data model created
- [ ] ApiService interface implemented
- [ ] RetrofitClient singleton configured with correct IP
- [ ] Network security config allows cleartext to local IP
- [ ] Image converts to MultipartBody.Part correctly
- [ ] Upload triggers on button click
- [ ] ProgressBar shows/hides appropriately
- [ ] onResponse() handles success path
- [ ] onFailure() handles network errors
- [ ] HTTP error codes handled (4xx, 5xx)
- [ ] Null checks prevent crashes
- [ ] ResultActivity displays all prediction fields
- [ ] Confidence displayed as percentage
- [ ] No hardcoded strings (use strings.xml)

### User Experience

- [ ] Loading indicator visible during upload
- [ ] Button disabled during network operation
- [ ] Clear error messages for different failure types
- [ ] Smooth transition to result screen
- [ ] No UI freezing during upload

### Testing

- [ ] End-to-end flow works (camera → upload → result)
- [ ] Error handling tested (no internet, backend down)
- [ ] App never crashes during testing
- [ ] Works with multiple consecutive uploads
- [ ] Logcat shows network requests/responses

### Documentation

- [ ] Code comments for complex sections
- [ ] Week 05 validation checklist completed
- [ ] Screenshots saved in docs/evidence/week-05/
- [ ] Reflection completed
- [ ] Git commits with meaningful messages

---

## Next Steps

**Week 06:** Replace dummy FastAPI predictions with real TensorFlow model inference!

---

**Congratulations! You've built a fully functional network-enabled Android app! 🎉**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 05 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| **4** | **build-task.md** ← *You are here* | **Build Implementation Guide** |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Practice Exercises](exercises.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Build Implementation Guide** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Validation & Verification →](validation-checklist.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 04: FastAPI Backend](../week-04-fastapi-backend/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 06: Cloud ML Model ➡](../week-06-cloud-ml-model/README.md) |

---
