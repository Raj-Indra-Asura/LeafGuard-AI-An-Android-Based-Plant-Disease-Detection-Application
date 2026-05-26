# Week 05: Learning Notes - Android Networking with Retrofit

## Table of Contents

1. [HTTP Networking Fundamentals](#1-http-networking-fundamentals)
2. [Why Retrofit Over Other Options](#2-why-retrofit-over-other-options)
3. [Retrofit Architecture Deep Dive](#3-retrofit-architecture-deep-dive)
4. [Multipart File Upload Explained](#4-multipart-file-upload-explained)
5. [JSON Parsing with Gson](#5-json-parsing-with-gson)
6. [Asynchronous Programming in Android](#6-asynchronous-programming-in-android)
7. [Error Handling Strategies](#7-error-handling-strategies)
8. [Network Security in Android](#8-network-security-in-android)
9. [Best Practices for Production Apps](#9-best-practices-for-production-apps)
10. [CSE 2206 Exam Preparation](#10-cse-2206-exam-preparation)

---

## 1. HTTP Networking Fundamentals

### What is HTTP?

**HTTP (HyperText Transfer Protocol)** is the foundation of data communication on the web. It's a request-response protocol between clients (your Android app) and servers (your FastAPI backend).

#### Request-Response Cycle

```
Android App                                FastAPI Backend
    │                                            │
    │  1. HTTP POST /predict                    │
    │     Headers:                               │
    │       Content-Type: multipart/form-data   │
    │     Body:                                  │
    │       image: [binary data]                │
    ├───────────────────────────────────────────>│
    │                                            │
    │                                      2. Process
    │                                         Image
    │                                            │
    │  3. HTTP 200 OK Response                  │
    │     Headers:                               │
    │       Content-Type: application/json      │
    │     Body:                                  │
    │       { "disease": "...", ... }           │
    │<───────────────────────────────────────────┤
    │                                            │
    4. Parse JSON
       Display Result
```

### HTTP Methods

| Method | Purpose | Has Body? | Used In LeafGuard |
|--------|---------|-----------|-------------------|
| GET | Retrieve data | No | Not used (yet) |
| POST | Send data to server | Yes | ✅ `/predict` endpoint |
| PUT | Update existing data | Yes | Not used |
| DELETE | Remove data | No | Not used |
| PATCH | Partial update | Yes | Not used |

**Why POST for image upload?**
- POST allows request body (needed for image data)
- GET requests have URL length limits, can't send large files
- POST is semantically correct for creating/processing resources

### HTTP Status Codes

| Code Range | Meaning | Examples |
|------------|---------|----------|
| 2xx | Success | 200 OK, 201 Created |
| 3xx | Redirection | 301 Moved Permanently |
| 4xx | Client Error | 400 Bad Request, 404 Not Found |
| 5xx | Server Error | 500 Internal Server Error |

**In LeafGuard AI:**
- `200 OK`: Image processed successfully, prediction returned
- `400 Bad Request`: Invalid image format or missing file
- `500 Internal Server Error`: Model inference failed

### Content-Type Header

Tells the server what format the request body is in:

- `application/json`: JSON data
- `multipart/form-data`: File uploads with form fields
- `text/plain`: Plain text
- `application/x-www-form-urlencoded`: HTML form data

**LeafGuard uses `multipart/form-data`** for image upload.

---

## 2. Why Retrofit Over Other Options

### Android HTTP Networking Options

#### Option 1: HttpURLConnection (Standard Java)

```java
// ❌ Verbose, error-prone
URL url = new URL("http://192.168.1.10:8000/predict");
HttpURLConnection connection = (HttpURLConnection) url.openConnection();
connection.setRequestMethod("POST");
connection.setDoOutput(true);
// ... 50 more lines of boilerplate code
```

**Problems:**
- Requires manual threading (can't run on main thread)
- Manual JSON parsing
- Complex multipart encoding
- Poor error handling
- Lots of boilerplate code

#### Option 2: OkHttp (Square Library)

```java
// 😐 Better than HttpURLConnection, still verbose
OkHttpClient client = new OkHttpClient();
RequestBody requestBody = new MultipartBody.Builder()
    .setType(MultipartBody.FORM)
    .addFormDataPart("image", "photo.jpg", imageBody)
    .build();
Request request = new Request.Builder()
    .url("http://192.168.1.10:8000/predict")
    .post(requestBody)
    .build();
client.newCall(request).enqueue(callback);
```

**Better, but:**
- Still requires manual JSON parsing
- Must manually create request bodies
- More setup code than needed

#### Option 3: Retrofit (Square Library, Built on OkHttp)

```java
// ✅ Clean, simple, professional
@POST("predict")
Call<PredictionResponse> uploadImage(@Part MultipartBody.Part image);
```

**Advantages:**
- Type-safe API definitions
- Automatic JSON parsing (with Gson)
- Automatic threading (async by default)
- Clean, declarative syntax
- Industry standard in Android development
- Built on OkHttp (same reliability, better API)

### Retrofit's Key Features

1. **Annotations-Based API Definition**
   ```java
   @GET("users/{id}")
   Call<User> getUser(@Path("id") int userId);
   ```

2. **Automatic JSON Conversion**
   ```java
   // Retrofit + Gson automatically converts JSON to objects
   Call<PredictionResponse> call = apiService.uploadImage(imagePart);
   ```

3. **Built-in Async Support**
   ```java
   call.enqueue(callback); // Runs on background thread
   ```

4. **Pluggable Converters**
   - Gson (JSON)
   - Moshi (JSON)
   - Jackson (JSON)
   - Protocol Buffers
   - XML

---

## 3. Retrofit Architecture Deep Dive

### Retrofit Components

```
┌─────────────────────────────────────────────────┐
│           Your Android App Code                 │
└────────────────┬────────────────────────────────┘
                 │
    ┌────────────▼──────────────┐
    │    ApiService Interface   │
    │  (Your API definitions)   │
    └────────────┬──────────────┘
                 │
    ┌────────────▼──────────────┐
    │   Retrofit Instance       │
    │  (Built with Builder)     │
    └────────────┬──────────────┘
                 │
    ┌────────────▼──────────────┐
    │   Converter Factory       │
    │  (GsonConverterFactory)   │
    └────────────┬──────────────┘
                 │
    ┌────────────▼──────────────┐
    │   OkHttp Client           │
    │  (Handles actual HTTP)    │
    └────────────┬──────────────┘
                 │
    ┌────────────▼──────────────┐
    │   Network (Internet)      │
    └───────────────────────────┘
```

### Step-by-Step: How Retrofit Processes a Request

#### Step 1: Define API Interface

```java
public interface ApiService {
    @Multipart
    @POST("predict")
    Call<PredictionResponse> uploadImage(@Part MultipartBody.Part image);
}
```

**What Retrofit sees:**
- Annotation `@POST("predict")` → HTTP method is POST, endpoint is `/predict`
- Annotation `@Multipart` → Content-Type: multipart/form-data
- Parameter `@Part` → This parameter goes in request body as multipart part
- Return type `Call<PredictionResponse>` → Async call returning PredictionResponse object

#### Step 2: Build Retrofit Instance

```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("http://192.168.1.10:8000/")
    .addConverterFactory(GsonConverterFactory.create())
    .build();
```

**What this does:**
- Sets base URL (all endpoint paths will be appended to this)
- Registers Gson converter (JSON ↔ Java objects)
- Creates Retrofit instance with configuration

#### Step 3: Create Service Implementation

```java
ApiService apiService = retrofit.create(ApiService.class);
```

**What happens:**
- Retrofit uses **Java Dynamic Proxy** to create implementation
- It reads annotations at runtime
- Generates HTTP request code automatically
- Returns a proxy object implementing ApiService interface

#### Step 4: Make API Call

```java
Call<PredictionResponse> call = apiService.uploadImage(imagePart);
call.enqueue(new Callback<PredictionResponse>() {
    @Override
    public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
        // Handle success
    }

    @Override
    public void onFailure(Call<PredictionResponse> call, Throwable t) {
        // Handle error
    }
});
```

**Behind the scenes:**
1. Retrofit builds HTTP request from annotations
2. OkHttp sends request on background thread
3. Server responds with JSON
4. Gson parses JSON into PredictionResponse object
5. Callback receives parsed object on main thread

---

## 4. Multipart File Upload Explained

### What is Multipart Form-Data?

**Multipart form-data** is an HTTP content type that allows sending multiple pieces of data (text fields, files) in a single request.

#### Standard Form Data (application/x-www-form-urlencoded)

```
name=John&age=25&city=Delhi
```

**Problem:** Can't send binary data (images, PDFs, etc.)

#### Multipart Form-Data

```
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="description"

Test image
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="image"; filename="photo.jpg"
Content-Type: image/jpeg

[BINARY IMAGE DATA HERE]
------WebKitFormBoundary7MA4YWxkTrZu0gW--
```

**Key components:**
- **Boundary**: Unique string separating parts
- **Content-Disposition**: Metadata for each part
- **filename**: Original file name
- **Content-Type**: MIME type of file
- **Binary data**: The actual file bytes

### Creating Multipart Request in Retrofit

#### Step 1: Create RequestBody from File

```java
File imageFile = new File(imagePath);
RequestBody requestBody = RequestBody.create(
    MediaType.parse("image/*"),
    imageFile
);
```

#### Step 2: Wrap in MultipartBody.Part

```java
MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
    "image",           // Parameter name (matches backend)
    imageFile.getName(), // Filename
    requestBody        // File content
);
```

#### Step 3: Send via Retrofit

```java
Call<PredictionResponse> call = apiService.uploadImage(imagePart);
```

### Backend Receives As

```python
# FastAPI backend
@app.post("/predict")
async def predict(image: UploadFile = File(...)):
    contents = await image.read()  # Binary image data
    filename = image.filename       # "photo.jpg"
    content_type = image.content_type  # "image/jpeg"
```

---

## 5. JSON Parsing with Gson

### What is JSON?

**JSON (JavaScript Object Notation)** is a lightweight data-interchange format. It's human-readable and easy for machines to parse.

#### JSON Example

```json
{
  "disease": "Tomato Late Blight",
  "confidence": 0.87,
  "symptoms": "Brown spots on leaves",
  "treatment": "Apply copper fungicide",
  "prevention": "Remove infected leaves"
}
```

### Manual JSON Parsing (Without Gson)

```java
// ❌ Tedious and error-prone
String jsonString = response.body().string();
JSONObject jsonObject = new JSONObject(jsonString);
String disease = jsonObject.getString("disease");
double confidence = jsonObject.getDouble("confidence");
String symptoms = jsonObject.getString("symptoms");
// ... and so on
```

### Automatic Parsing with Gson

#### Step 1: Create Java Class Matching JSON Structure

```java
public class PredictionResponse {
    private String disease;
    private double confidence;
    private String symptoms;
    private String treatment;
    private String prevention;

    // Getters and setters
    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }
    // ... more getters/setters
}
```

#### Step 2: Gson Automatically Maps JSON

```java
// ✅ Retrofit + Gson handles this automatically
PredictionResponse prediction = response.body();
String disease = prediction.getDisease(); // "Tomato Late Blight"
double confidence = prediction.getConfidence(); // 0.87
```

### Gson Mapping Rules

1. **Field names must match JSON keys** (case-sensitive)
   ```java
   private String disease;  // Matches "disease" in JSON
   ```

2. **Use @SerializedName for different names**
   ```java
   @SerializedName("disease_name")
   private String disease;  // JSON key: "disease_name", Java field: disease
   ```

3. **Nested objects**
   ```json
   {
     "disease": {
       "name": "Late Blight",
       "severity": "High"
     }
   }
   ```

   ```java
   public class Disease {
       private String name;
       private String severity;
   }

   public class PredictionResponse {
       private Disease disease;
   }
   ```

4. **Arrays/Lists**
   ```json
   {
     "symptoms": ["Brown spots", "Wilting", "Yellowing"]
   }
   ```

   ```java
   private List<String> symptoms;
   ```

### Common Gson Errors

1. **Missing Field**: JSON has field but Java class doesn't → Field is ignored
2. **Type Mismatch**: JSON has string, Java expects int → Exception thrown
3. **Null Values**: JSON has null, Java expects primitive → Exception (use wrapper types: Integer, Double instead of int, double)

---

## 6. Asynchronous Programming in Android

### The Main Thread Problem

**Android's Main Thread (UI Thread):**
- Handles all UI updates
- Processes user interactions (clicks, swipes)
- Renders UI at 60 FPS (16ms per frame)

**If you block the main thread:**
- UI freezes
- App becomes unresponsive
- After 5 seconds: "Application Not Responding" (ANR) dialog
- Bad user experience

### Network Calls Take Time

```java
// This would take 1-3 seconds
String response = downloadDataFromServer();
```

**Problem:** If on main thread, UI freezes for 1-3 seconds. Unacceptable.

### Solution: Asynchronous Callbacks

```java
// Request starts immediately, method returns
// Callback executed later when response arrives
call.enqueue(new Callback<PredictionResponse>() {
    @Override
    public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
        // Executed on MAIN THREAD when response arrives
        // Safe to update UI here
    }

    @Override
    public void onFailure(Call<PredictionResponse> call, Throwable t) {
        // Executed on MAIN THREAD when error occurs
        // Safe to show error Toast here
    }
});
```

### How Retrofit Handles Threading

```
Main Thread                     Background Thread
────────────────────────────────────────────────────
1. User clicks button
2. Create Call object
3. call.enqueue(callback)
4. Method returns                → 5. Start HTTP request
   (UI still responsive)         → 6. Wait for response
   (User can scroll, etc.)       → 7. Receive response
                                  → 8. Parse JSON with Gson
9. onResponse() called          ← 9. Return to main thread
10. Update UI
11. User sees result
```

**Key Point:** Steps 5-8 happen on background thread. UI never freezes.

### Callbacks Explained

**Callback** = "Call me back when you're done"

```java
interface Callback<T> {
    void onResponse(Call<T> call, Response<T> response);  // Success callback
    void onFailure(Call<T> call, Throwable t);            // Error callback
}
```

You implement these methods, Retrofit calls them when done.

---

## 7. Error Handling Strategies

### Types of Errors

#### 1. Network Errors (IOException)

**Causes:**
- No internet connection
- Server is down
- Request timeout
- DNS resolution failure

**Detection:**
```java
@Override
public void onFailure(Call<PredictionResponse> call, Throwable t) {
    if (t instanceof IOException) {
        Toast.makeText(this, "Network error. Check your internet.", Toast.LENGTH_LONG).show();
    }
}
```

#### 2. HTTP Errors (Response code 4xx/5xx)

**Causes:**
- 400 Bad Request: Invalid data sent
- 404 Not Found: Endpoint doesn't exist
- 500 Internal Server Error: Backend crashed

**Detection:**
```java
@Override
public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
    if (response.isSuccessful()) {
        // 2xx status code
        PredictionResponse prediction = response.body();
    } else {
        // 4xx or 5xx status code
        int statusCode = response.code();
        if (statusCode == 404) {
            Toast.makeText(this, "Endpoint not found", Toast.LENGTH_LONG).show();
        } else if (statusCode == 500) {
            Toast.makeText(this, "Server error", Toast.LENGTH_LONG).show();
        }
    }
}
```

#### 3. Parsing Errors

**Causes:**
- JSON structure doesn't match Java class
- Backend returned unexpected format
- Corrupted response

**Detection:**
```java
if (response.body() == null) {
    Toast.makeText(this, "Empty response from server", Toast.LENGTH_LONG).show();
    return;
}
```

### Comprehensive Error Handling Template

```java
call.enqueue(new Callback<PredictionResponse>() {
    @Override
    public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
        progressBar.setVisibility(View.GONE);
        uploadButton.setEnabled(true);

        if (response.isSuccessful() && response.body() != null) {
            // SUCCESS: Got valid response
            PredictionResponse prediction = response.body();
            navigateToResult(prediction);
        } else {
            // HTTP ERROR: Got response but status code is not 2xx
            String errorMsg = "Server error: " + response.code();
            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<PredictionResponse> call, Throwable t) {
        progressBar.setVisibility(View.GONE);
        uploadButton.setEnabled(true);

        // NETWORK ERROR: Couldn't reach server
        if (t instanceof IOException) {
            Toast.makeText(MainActivity.this, "Network error. Check connection.", Toast.LENGTH_LONG).show();
        } else if (t instanceof SocketTimeoutException) {
            Toast.makeText(MainActivity.this, "Request timed out.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
});
```

### Timeout Configuration

```java
OkHttpClient client = new OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)  // Connection timeout
    .readTimeout(30, TimeUnit.SECONDS)     // Read timeout
    .writeTimeout(30, TimeUnit.SECONDS)    // Write timeout
    .build();

Retrofit retrofit = new Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)  // Use custom OkHttp client
    .addConverterFactory(GsonConverterFactory.create())
    .build();
```

**Explanation:**
- **connectTimeout**: Time limit to establish connection
- **readTimeout**: Time limit to read response
- **writeTimeout**: Time limit to send request body

---

## 8. Network Security in Android

### Cleartext Traffic Restrictions

**Android 9 (API 28) and above:** HTTP (cleartext) traffic is **blocked by default**.

**Why?**
- HTTPS (encrypted) is more secure
- Google encourages encrypted communication
- Protects users from man-in-the-middle attacks

**Problem for LeafGuard:**
- Your local backend uses HTTP (http://192.168.1.10:8000)
- Not HTTPS (https://...)
- Android blocks it by default

### Solution: Network Security Configuration

#### Step 1: Create `res/xml/network_security_config.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <!-- Allow cleartext traffic to local IP -->
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">192.168.1.10</domain>
    </domain-config>
</network-security-config>
```

#### Step 2: Reference in AndroidManifest.xml

```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ... >
</application>
```

### INTERNET Permission

**Required** for any network access:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

**Without this:** SecurityException thrown, network calls fail.

### Production Best Practices

1. **Use HTTPS in production** (not HTTP)
2. **Certificate pinning** for extra security
3. **Never allow cleartext for production domains**
4. **Only allow cleartext for local development IPs**

---

## 9. Best Practices for Production Apps

### 1. Singleton Retrofit Instance

**❌ Don't create new instance per request:**
```java
// BAD: Creates new Retrofit for each upload
Retrofit retrofit = new Retrofit.Builder()...build();
```

**✅ Use singleton pattern:**
```java
public class RetrofitClient {
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

### 2. Logging Interceptor (Development Only)

**See HTTP requests/responses in Logcat:**

```java
// Add dependency
implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'

// Add to OkHttp client
HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

OkHttpClient client = new OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .build();
```

**Remove in production** (exposes sensitive data in logs).

### 3. Request Cancellation

**Cancel request if Activity is destroyed:**

```java
private Call<PredictionResponse> currentCall;

void uploadImage() {
    currentCall = apiService.uploadImage(imagePart);
    currentCall.enqueue(callback);
}

@Override
protected void onDestroy() {
    super.onDestroy();
    if (currentCall != null && !currentCall.isCanceled()) {
        currentCall.cancel();
    }
}
```

### 4. Retry Logic

**Automatic retry on failure:**

```java
OkHttpClient client = new OkHttpClient.Builder()
    .retryOnConnectionFailure(true)  // Retry on connection failure
    .build();
```

### 5. Base URL Management

**Use BuildConfig for different environments:**

```java
private static final String BASE_URL = BuildConfig.DEBUG
    ? "http://192.168.1.10:8000/"  // Development
    : "https://api.leafguard.com/";  // Production
```

---

## 10. CSE 2206 Exam Preparation

### Key Concepts to Explain

#### 1. Client-Server Architecture

**Definition:** Application architecture where client (Android app) requests services from server (FastAPI backend).

**In LeafGuard:**
- Client: Android app (user interface, camera, display)
- Server: FastAPI backend (ML model, processing logic)
- Communication: HTTP requests/responses

#### 2. RESTful API

**REST = Representational State Transfer**

**Principles:**
- Stateless (each request independent)
- Resource-based (endpoints represent resources)
- Standard HTTP methods (GET, POST, PUT, DELETE)
- JSON for data exchange

**LeafGuard example:**
- Resource: Plant disease prediction
- Endpoint: `/predict`
- Method: POST
- Input: Image file
- Output: JSON with disease name

#### 3. Asynchronous vs Synchronous

| Aspect | Synchronous | Asynchronous |
|--------|-------------|--------------|
| Execution | Blocks thread until complete | Returns immediately |
| UI Impact | Freezes UI | UI stays responsive |
| Android | NetworkOnMainThreadException | Recommended approach |
| Retrofit | call.execute() | call.enqueue() |

#### 4. JSON

**Definition:** Text-based data format for exchanging data between systems.

**Why JSON?**
- Human-readable
- Language-independent
- Lightweight
- Easy to parse

**Alternative:** XML (more verbose)

### Sample Viva Questions & Answers

**Q1: How does your Android app communicate with the backend?**

**A:** My Android app uses Retrofit library to make HTTP POST requests to the FastAPI backend. The app converts the captured image into a MultipartBody.Part and sends it to the `/predict` endpoint. The backend processes the image with a machine learning model and returns a JSON response containing the disease name, confidence score, symptoms, and treatment recommendations. Retrofit automatically parses the JSON into a Java PredictionResponse object using Gson.

---

**Q2: Why can't you make network calls on the main thread?**

**A:** The main thread (UI thread) is responsible for rendering the user interface and handling user interactions. Network calls typically take 1-3 seconds to complete. If we block the main thread during this time, the UI freezes, making the app unresponsive. After 5 seconds, Android shows an "Application Not Responding" (ANR) dialog. To prevent this, we use Retrofit's `enqueue()` method which performs network operations on a background thread and delivers results back to the main thread via callbacks.

---

**Q3: How do you handle network errors in your app?**

**A:** I implement comprehensive error handling in both `onResponse()` and `onFailure()` callbacks. In `onResponse()`, I check if the response is successful (2xx status code) and if the response body is not null. If the status code is 4xx or 5xx, I show an appropriate error message. In `onFailure()`, I check if the error is an IOException (network error) or SocketTimeoutException (timeout), and display user-friendly Toast messages. I also hide the progress bar and re-enable the upload button in both cases to allow the user to retry.

---

**Q4: What is multipart form-data and why do you use it?**

**A:** Multipart form-data is an HTTP content type that allows sending multiple pieces of data, including binary files, in a single request. Traditional form encoding (application/x-www-form-urlencoded) only supports text data. Since LeafGuard AI needs to upload image files from the Android app to the backend, I use multipart form-data. Each part of the request has a boundary separator, content-disposition header, and the actual data. Retrofit makes this easy with the `@Multipart` annotation and `MultipartBody.Part` class.

---

**Q5: Explain the flow when a user uploads an image.**

**A:**
1. User captures an image using camera (from Week 03 implementation)
2. User taps "Detect Disease" button
3. App shows ProgressBar and disables button for feedback
4. App converts the Bitmap to a File, then creates RequestBody and MultipartBody.Part
5. Retrofit sends HTTP POST request to http://[LOCAL_IP]:8000/predict with the image
6. Request runs on background thread (via OkHttp)
7. FastAPI backend receives image, preprocesses it, runs ML model inference
8. Backend returns JSON response with disease, confidence, symptoms, treatment
9. Retrofit receives response on background thread, Gson parses JSON to PredictionResponse object
10. `onResponse()` callback executes on main thread
11. App hides ProgressBar, creates Intent with prediction data
12. App navigates to ResultActivity
13. ResultActivity displays disease name, confidence percentage, and recommendations

---

**Q6: What permissions does your app need for networking?**

**A:** The app requires the INTERNET permission, declared in AndroidManifest.xml with `<uses-permission android:name="android.permission.INTERNET"/>`. Additionally, since Android 9 (API 28) blocks HTTP traffic by default, I created a network security configuration file that allows cleartext traffic to my local backend IP (192.168.1.10). This is referenced in the manifest with `android:networkSecurityConfig="@xml/network_security_config"`. In production, I would use HTTPS instead of HTTP.

---

**Q7: What is the difference between Retrofit and HttpURLConnection?**

**A:** HttpURLConnection is the standard Java networking API. It's low-level and requires significant boilerplate code for tasks like setting headers, handling multipart uploads, parsing JSON, and managing background threads. Retrofit is a type-safe HTTP client library built on top of OkHttp. It provides a clean, annotation-based API for defining endpoints, automatically handles threading via callbacks, integrates with Gson for automatic JSON parsing, and reduces boilerplate code significantly. Retrofit is the industry standard for Android networking because it makes code more maintainable and less error-prone.

---

**Q8: How does Gson convert JSON to Java objects?**

**A:** Gson uses reflection to map JSON keys to Java class fields. When I define a PredictionResponse class with fields like `private String disease`, Gson looks for a "disease" key in the JSON response and assigns its value to the field. If field names don't match JSON keys, I can use the `@SerializedName` annotation. Gson supports primitive types, wrapper types, strings, nested objects, arrays, and lists. It handles null values gracefully when using wrapper types (Integer, Double) instead of primitives (int, double). Retrofit integrates Gson via GsonConverterFactory, so JSON conversion happens automatically without any manual parsing code.

---

### Practice Problems

#### Problem 1: Implement GET Request

**Task:** Add a method to ApiService to get a list of all supported diseases.

**Backend endpoint:**
```
GET /diseases
Response: ["Tomato Late Blight", "Potato Early Blight", ...]
```

**Your code:**
```java
@GET("diseases")
Call<List<String>> getDiseases();
```

#### Problem 2: Add Query Parameter

**Task:** Add a disease search endpoint with query parameter.

**Backend endpoint:**
```
GET /diseases?search=tomato
Response: ["Tomato Late Blight", "Tomato Mosaic Virus", ...]
```

**Your code:**
```java
@GET("diseases")
Call<List<String>> searchDiseases(@Query("search") String searchTerm);
```

#### Problem 3: Handle Nested JSON

**Backend returns:**
```json
{
  "disease": {
    "name": "Tomato Late Blight",
    "scientific_name": "Phytophthora infestans",
    "severity": "High"
  },
  "confidence": 0.87
}
```

**Your data models:**
```java
public class Disease {
    private String name;
    @SerializedName("scientific_name")
    private String scientificName;
    private String severity;
    // Getters and setters
}

public class PredictionResponse {
    private Disease disease;
    private double confidence;
    // Getters and setters
}
```

---

## Summary

### What You Learned This Week

1. **HTTP Fundamentals:** Request-response cycle, methods, status codes, headers
2. **Retrofit Architecture:** How it simplifies Android networking
3. **Multipart Upload:** Sending image files via HTTP
4. **JSON Parsing:** Automatic conversion with Gson
5. **Async Programming:** Callbacks, threading, UI responsiveness
6. **Error Handling:** Network errors, HTTP errors, timeout handling
7. **Network Security:** Cleartext restrictions, permissions
8. **Best Practices:** Singleton pattern, logging, cancellation

### Key Takeaways

- **Always use async networking** (`enqueue()`, never `execute()`)
- **Handle all error cases** (network, HTTP, parsing)
- **Show loading indicators** for user feedback
- **Use HTTPS in production** (HTTP only for local development)
- **Retrofit is the standard** (don't use HttpURLConnection)
- **Singleton for Retrofit** (don't create multiple instances)

### Next Steps

- Complete Week 05 exercises to practice these concepts
- Implement build task step-by-step
- Test thoroughly with different network conditions
- Move to Week 06: Integrate real ML model in backend

---

**Now you have a solid theoretical foundation. Practice with exercises to build muscle memory!**
