# Week 05: Exercises - Android Networking with Retrofit

## Related materials

- Exercises (primary Kotlin): [../../exercises/android-kotlin/](../../exercises/android-kotlin/)
- Exercises (secondary Java): [../../exercises/android/](../../exercises/android/)
- Solutions: [../../solutions/week-05/](../../solutions/week-05/)
- Notebooks: [../../notebooks/week-05/](../../notebooks/week-05/)
- Glossary: [../../GLOSSARY.md](../../GLOSSARY.md)

---

## Overview

These 6 exercises will build your confidence with Retrofit, Gson, and Android networking **before** you integrate them into your main LeafGuard AI app. Each exercise is self-contained and focuses on a specific networking concept.

**Time Investment:** 2-3 hours total (20-30 minutes per exercise)

**Prerequisites:**
- Android Studio installed
- Week 04 FastAPI backend running (or use public APIs for some exercises)
- Basic understanding of HTTP requests

---

## Exercise 1: Add Retrofit Dependencies and Verify Setup

**Objective:** Practice adding Gradle dependencies and verifying successful integration.

### Task

1. Create a new Android project (or use existing Week 03 project)
2. Add Retrofit, Gson, and OkHttp logging dependencies
3. Sync Gradle and verify no errors
4. Add INTERNET permission to manifest

### Step-by-Step

#### 1. Open `build.gradle (Module: app)`

Add these dependencies inside the `dependencies` block:

```gradle
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'
```

#### 2. Click "Sync Now"

Wait for Gradle sync to complete. Check bottom of Android Studio for status.

#### 3. Add INTERNET permission

In `AndroidManifest.xml`, add before `<application>` tag:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

### Expected Output

- Gradle sync completes successfully
- No red errors in build.gradle
- Build → Make Project succeeds
- You see "BUILD SUCCESSFUL" in Build output

### Verification Checklist

- [ ] Dependencies added correctly
- [ ] Gradle synced without errors
- [ ] INTERNET permission present in manifest
- [ ] Project builds successfully

---

## Exercise 2: Create Your First Retrofit API Interface

**Objective:** Learn annotation-based API definition with a simple GET request.

### Task

Create an API interface to fetch data from a public test API.

**Test API:** JSONPlaceholder (https://jsonplaceholder.typicode.com/)

### Step-by-Step

#### 1. Create Data Model

Create `Post.java`:

```java
public class Post {
    private int userId;
    private int id;
    private String title;
    private String body;

    // Getters
    public int getUserId() { return userId; }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
}
```

#### 2. Create API Interface

Create `JsonPlaceholderApi.java`:

```java
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceholderApi {
    @GET("posts/{id}")
    Call<Post> getPost(@Path("id") int postId);
}
```

#### 3. Create Retrofit Instance

```java
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
```

#### 4. Make API Call in MainActivity

```java
JsonPlaceholderApi api = RetrofitClient.getClient().create(JsonPlaceholderApi.class);
Call<Post> call = api.getPost(1);

call.enqueue(new Callback<Post>() {
    @Override
    public void onResponse(Call<Post> call, Response<Post> response) {
        if (response.isSuccessful() && response.body() != null) {
            Post post = response.body();
            Log.d("Exercise2", "Title: " + post.getTitle());
            Toast.makeText(MainActivity.this, "Title: " + post.getTitle(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<Post> call, Throwable t) {
        Log.e("Exercise2", "Error: " + t.getMessage());
        Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }
});
```

### Expected Output

- Toast message showing: "Title: sunt aut facere repellat provident..."
- Logcat shows the post title
- No errors in Logcat

### Verification Checklist

- [ ] API interface created with @GET annotation
- [ ] Post data model matches JSON structure
- [ ] RetrofitClient singleton implemented
- [ ] API call successful, Toast displayed
- [ ] Logcat shows fetched data

### Learning Points

- `@GET` annotation for GET requests
- `@Path` for URL path parameters
- Asynchronous calls with `enqueue()`
- Callbacks: `onResponse()` and `onFailure()`

---

## Exercise 3: Handle ProgressBar and Button States

**Objective:** Practice showing/hiding loading indicators during network operations.

### Task

Extend Exercise 2 to show ProgressBar while fetching data.

### Step-by-Step

#### 1. Add ProgressBar to Layout

In `activity_main.xml`:

```xml
<ProgressBar
    android:id="@+id/progressBar"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:visibility="gone"
    android:layout_centerInParent="true"/>

<Button
    android:id="@+id/fetchButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Fetch Post"
    android:layout_centerInParent="true"/>
```

#### 2. Update MainActivity

```java
ProgressBar progressBar = findViewById(R.id.progressBar);
Button fetchButton = findViewById(R.id.fetchButton);

fetchButton.setOnClickListener(v -> {
    // Show progress, disable button
    progressBar.setVisibility(View.VISIBLE);
    fetchButton.setEnabled(false);

    JsonPlaceholderApi api = RetrofitClient.getClient().create(JsonPlaceholderApi.class);
    Call<Post> call = api.getPost(1);

    call.enqueue(new Callback<Post>() {
        @Override
        public void onResponse(Call<Post> call, Response<Post> response) {
            // Hide progress, enable button
            progressBar.setVisibility(View.GONE);
            fetchButton.setEnabled(true);

            if (response.isSuccessful() && response.body() != null) {
                Post post = response.body();
                Toast.makeText(MainActivity.this, "Title: " + post.getTitle(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<Post> call, Throwable t) {
            // Hide progress, enable button
            progressBar.setVisibility(View.GONE);
            fetchButton.setEnabled(true);

            Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
        }
    });
});
```

### Expected Output

- Clicking button shows ProgressBar
- Button becomes disabled (grayed out)
- After response, ProgressBar disappears
- Button becomes enabled again
- Toast shows the post title

### Verification Checklist

- [ ] ProgressBar shows when button clicked
- [ ] Button disabled during request
- [ ] ProgressBar hidden after response
- [ ] Button enabled after response
- [ ] Works for both success and failure cases

### Learning Points

- Managing UI state during async operations
- Preventing multiple simultaneous requests
- User feedback best practices

---

## Exercise 4: Implement Error Handling for Different Scenarios

**Objective:** Practice handling various error types gracefully.

### Task

Test your app's error handling with different scenarios.

### Scenarios to Test

#### Scenario 1: Network Error (No Internet)

1. Turn off Wi-Fi and mobile data on your phone/emulator
2. Click fetch button
3. Observe error message

**Expected:** Toast showing "Network error" or IOException message

#### Scenario 2: HTTP Error (Invalid Endpoint)

Modify API interface to use invalid endpoint:

```java
@GET("invalid-endpoint")  // This endpoint doesn't exist
Call<Post> getPost(@Path("id") int postId);
```

Run the app and click fetch button.

**Expected:** `onResponse()` called but `response.isSuccessful()` returns false, status code 404

#### Scenario 3: Parsing Error (Mismatched Data Model)

Modify Post class to have wrong field name:

```java
private String wrongFieldName;  // JSON has "title", not "wrongFieldName"
```

**Expected:** Gson can't map field, field remains null

### Improved Error Handling Code

```java
@Override
public void onResponse(Call<Post> call, Response<Post> response) {
    progressBar.setVisibility(View.GONE);
    fetchButton.setEnabled(true);

    if (response.isSuccessful()) {
        if (response.body() != null) {
            Post post = response.body();
            if (post.getTitle() != null) {
                Toast.makeText(MainActivity.this, "Title: " + post.getTitle(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Parsing error: Title is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Empty response", Toast.LENGTH_SHORT).show();
        }
    } else {
        String errorMsg = "HTTP Error: " + response.code();
        Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
    }
}

@Override
public void onFailure(Call<Post> call, Throwable t) {
    progressBar.setVisibility(View.GONE);
    fetchButton.setEnabled(true);

    String errorMsg;
    if (t instanceof IOException) {
        errorMsg = "Network error. Check your internet connection.";
    } else if (t instanceof SocketTimeoutException) {
        errorMsg = "Request timed out. Try again.";
    } else {
        errorMsg = "Unexpected error: " + t.getMessage();
    }
    Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
}
```

### Verification Checklist

- [ ] Network error handled (no internet scenario)
- [ ] HTTP error handled (404 scenario)
- [ ] Null checks prevent crashes
- [ ] User-friendly error messages shown
- [ ] App never crashes during errors

### Learning Points

- Difference between `onFailure()` and unsuccessful `onResponse()`
- Importance of null checks
- Specific error messages improve UX

---

## Exercise 5: Practice Multipart File Upload (Image Upload Simulation)

**Objective:** Learn to upload files using multipart form-data (preparation for LeafGuard AI).

### Task

Create a simple app that uploads an image file.

**Test API:** Use your FastAPI backend from Week 04, or use https://httpbin.org/post (test endpoint)

### Step-by-Step

#### 1. Add Image to Resources

1. Copy a test image to `res/drawable/` (name it `test_leaf.jpg`)
2. Or use camera from Week 03

#### 2. Create Upload API Interface

**Kotlin (primary):**
```kotlin
interface UploadApi {
    @Multipart
    @POST("predict") // Or use "post" for httpbin.org
    fun uploadImage(@Part image: MultipartBody.Part): Call<UploadResponse>
}
```

**Java (secondary reference):**
```java
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApi {
    @Multipart
    @POST("predict")  // Or use "post" for httpbin.org
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);
}
```

#### 3. Create Response Model

**Kotlin (primary):**
```kotlin
data class UploadResponse(
    val message: String?,
    val filename: String?,
    @SerializedName("disease") val disease: String?,
    val confidence: Double?
)
```

**Java (secondary reference):**
```java
public class UploadResponse {
    private String message;
    private String filename;

    // For your FastAPI backend
    private String disease;
    private double confidence;

    // Getters
    public String getMessage() { return message; }
    public String getFilename() { return filename; }
    public String getDisease() { return disease; }
    public double getConfidence() { return confidence; }
}
```

#### 4. Prepare Image for Upload

```java
// Option 1: From drawable resource
Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_leaf);
File imageFile = new File(getCacheDir(), "test_image.jpg");
FileOutputStream fos = new FileOutputStream(imageFile);
bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
fos.close();

// Option 2: From existing file path
File imageFile = new File(imagePath);

// Create RequestBody
RequestBody requestBody = RequestBody.create(
        MediaType.parse("image/*"),
        imageFile
);

// Create MultipartBody.Part
MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
        "image",              // Parameter name
        imageFile.getName(),  // Filename
        requestBody           // File content
);
```

#### 5. Upload Image

**Kotlin (primary):**
```kotlin
uploadButton.setOnClickListener {
    progressBar.visibility = View.VISIBLE
    uploadButton.isEnabled = false

    val api = RetrofitClient.getClient().create(UploadApi::class.java)
    val call = api.uploadImage(imagePart)

    call.enqueue(object : Callback<UploadResponse> {
        override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
            progressBar.visibility = View.GONE
            uploadButton.isEnabled = true
            if (response.isSuccessful && response.body() != null) {
                Toast.makeText(this@MainActivity, "Upload successful!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "Upload failed: ${response.code()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
            progressBar.visibility = View.GONE
            uploadButton.isEnabled = true
            Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}
```

**Java (secondary reference):**
```java
uploadButton.setOnClickListener(v -> {
    progressBar.setVisibility(View.VISIBLE);
    uploadButton.setEnabled(false);

    // Emulator: use http://10.0.2.2:8000/; physical phone: use your computer LAN IP; or use "https://httpbin.org/"
    UploadApi api = RetrofitClient.getClient().create(UploadApi.class);
    Call<UploadResponse> call = api.uploadImage(imagePart);

    call.enqueue(new Callback<UploadResponse>() {
        @Override
        public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
            progressBar.setVisibility(View.GONE);
            uploadButton.setEnabled(true);

            if (response.isSuccessful() && response.body() != null) {
                UploadResponse uploadResponse = response.body();
                Toast.makeText(MainActivity.this, "Upload successful!", Toast.LENGTH_LONG).show();
                Log.d("Exercise5", "Response: " + uploadResponse.getMessage());
            } else {
                Toast.makeText(MainActivity.this, "Upload failed: " + response.code(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<UploadResponse> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
            uploadButton.setEnabled(true);
            Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
        }
    });
});
```

### Expected Output

- Image file created successfully
- Upload request sent to backend
- Toast shows "Upload successful!"
- Logcat shows response message
- (If using FastAPI) Backend logs show received file

### Verification Checklist

- [ ] Image converted to File successfully
- [ ] RequestBody created correctly
- [ ] MultipartBody.Part created with correct parameters
- [ ] Upload API interface has @Multipart annotation
- [ ] Backend receives file (check backend logs)
- [ ] Response parsed successfully

### Learning Points

- Converting Bitmap to File
- Creating RequestBody and MultipartBody.Part
- Using `@Multipart` and `@Part` annotations
- This is the foundation for LeafGuard AI image upload!

---

## Exercise 6: Complete Integration Test (Mini LeafGuard Simulation)

**Objective:** Combine all concepts into a complete flow simulating LeafGuard AI.

### Task

Build a mini app that:
1. Shows an image (from resources)
2. Has "Detect Disease" button
3. Uploads image to your FastAPI backend
4. Displays prediction result in a TextView

### Step-by-Step

#### 1. Layout Design

```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:src="@drawable/test_leaf"
        android:scaleType="centerCrop"/>

    <Button
        android:id="@+id/detectButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Detect Disease"
        android:layout_marginTop="16dp"/>

    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:layout_marginTop="16dp"
        android:visibility="gone"/>

    <TextView
        android:id="@+id/resultTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="Result will appear here"
        android:textSize="16sp"
        android:padding="16dp"
        android:background="@android:color/darker_gray"/>
</LinearLayout>
```

#### 2. Complete MainActivity Code

**Kotlin (primary core network call):**
```kotlin
private fun uploadAndPredict() {
    progressBar.visibility = View.VISIBLE
    detectButton.isEnabled = false
    resultTextView.text = "Analyzing image..."

    val api = RetrofitClient.getClient().create(UploadApi::class.java)
    val call = api.uploadImage(imagePart)
    call.enqueue(object : Callback<PredictionResponse> {
        override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
            progressBar.visibility = View.GONE
            detectButton.isEnabled = true
            val prediction = response.body()
            resultTextView.text = if (response.isSuccessful && prediction != null) {
                "Disease: ${prediction.disease}\nConfidence: ${prediction.confidence * 100}%\nSymptoms: ${prediction.symptoms}"
            } else {
                "Error: ${response.code()}"
            }
        }

        override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
            progressBar.visibility = View.GONE
            detectButton.isEnabled = true
            resultTextView.text = "Error: ${t.message}"
        }
    })
}
```

**Java (secondary reference):**
```java
public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button detectButton;
    private ProgressBar progressBar;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        detectButton = findViewById(R.id.detectButton);
        progressBar = findViewById(R.id.progressBar);
        resultTextView = findViewById(R.id.resultTextView);

        detectButton.setOnClickListener(v -> uploadAndPredict());
    }

    private void uploadAndPredict() {
        // Show loading state
        progressBar.setVisibility(View.VISIBLE);
        detectButton.setEnabled(false);
        resultTextView.setText("Analyzing image...");

        try {
            // Prepare image
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_leaf);
            File imageFile = new File(getCacheDir(), "leaf.jpg");
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.close();

            // Create multipart body
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.getName(),
                    requestBody
            );

            // Make API call
            UploadApi api = RetrofitClient.getClient().create(UploadApi.class);
            Call<PredictionResponse> call = api.uploadImage(imagePart);

            call.enqueue(new Callback<PredictionResponse>() {
                @Override
                public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    detectButton.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null) {
                        PredictionResponse prediction = response.body();
                        String result = "Disease: " + prediction.getDisease() + "\n" +
                                       "Confidence: " + String.format("%.2f", prediction.getConfidence() * 100) + "%\n" +
                                       "Symptoms: " + prediction.getSymptoms();
                        resultTextView.setText(result);
                    } else {
                        resultTextView.setText("Error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<PredictionResponse> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    detectButton.setEnabled(true);
                    resultTextView.setText("Error: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            detectButton.setEnabled(true);
            resultTextView.setText("Error preparing image: " + e.getMessage());
        }
    }
}
```

### Expected Output

1. App shows test leaf image
2. User taps "Detect Disease"
3. ProgressBar appears
4. Button disabled
5. "Analyzing image..." shown
6. After 1-3 seconds, result appears:
   ```
   Disease: Tomato Late Blight
   Confidence: 87.25%
   Symptoms: Brown spots on leaves
   ```

### Verification Checklist

- [ ] Image displayed correctly
- [ ] Button triggers upload
- [ ] ProgressBar shows during upload
- [ ] Button disabled during upload
- [ ] Result displayed after successful upload
- [ ] Error message shown if backend is down
- [ ] Complete flow works end-to-end

### Learning Points

- Complete request-response flow
- UI state management throughout process
- Error handling at every step
- This is essentially LeafGuard AI networking! Expected result: you should see the disease name and confidence appear in the result TextView.

---

## Bonus Challenge: Add Logging Interceptor

**Objective:** See HTTP requests and responses in Logcat for debugging.

### Task

Add OkHttp logging interceptor to see full network traffic.

### Implementation

**Kotlin (primary):**
```kotlin
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    private val retrofit: Retrofit by lazy {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
```

**Java (secondary reference):**
```java
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8000/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Create logging interceptor
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Create OkHttp client with interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Create Retrofit with custom client
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
```

### Expected Output in Logcat

```
D/OkHttp: --> POST http://10.0.2.2:8000/predict
D/OkHttp: Content-Type: multipart/form-data; boundary=...
D/OkHttp: Content-Length: 52341
D/OkHttp: --> END POST
D/OkHttp: <-- 200 OK http://10.0.2.2:8000/predict (1523ms)
D/OkHttp: Content-Type: application/json
D/OkHttp: {"disease":"Tomato Late Blight","confidence":0.87,...}
D/OkHttp: <-- END HTTP
```

---

## Summary

### What You Practiced

1. **Exercise 1:** Gradle dependency management, INTERNET permission
2. **Exercise 2:** Basic Retrofit API interface, GET requests, callbacks
3. **Exercise 3:** UI state management, ProgressBar, button disabling
4. **Exercise 4:** Comprehensive error handling for different scenarios
5. **Exercise 5:** Multipart file upload, image conversion, @Multipart annotation
6. **Exercise 6:** Complete integration flow (mini LeafGuard AI)

### Skills Gained

- Adding and configuring Retrofit dependencies
- Creating type-safe API interfaces with annotations
- Making asynchronous network calls with callbacks
- Handling success and error cases gracefully
- Managing UI state during network operations
- Uploading files using multipart form-data
- Converting images to RequestBody and MultipartBody.Part
- Building complete request-response flows

### Next Steps

Now that you've practiced these concepts independently, you're ready for the Week 05 build task where you'll integrate all of this into your LeafGuard AI app!

---

**Completion Time:** 2-3 hours (pace yourself, focus on understanding)

**Verification:** Show your teacher working Exercise 6 (mini LeafGuard simulation)


<!-- NAV_FOOTER_START -->

---

## 📚 Week 05 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| **3** | **exercises.md** ← *You are here* | **Practice Exercises** |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Theory & Learning Notes](learning-notes.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Practice Exercises** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Build Implementation Guide →](build-task.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 04: FastAPI Backend](../week-04-fastapi-backend/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 06: Cloud ML Model ➡](../week-06-cloud-ml-model/README.md) |

---
