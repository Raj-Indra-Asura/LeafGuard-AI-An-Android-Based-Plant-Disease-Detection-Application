# Week 05: Android Networking with Retrofit

## Weekly Objective

By the end of Week 05, you will:

1. **Integrate Retrofit library** into your Android project for HTTP networking
2. **Configure Retrofit with Gson converter** to handle JSON parsing automatically
3. **Create API service interfaces** defining HTTP methods with proper annotations
4. **Implement multipart image upload** from Android app to your FastAPI backend
5. **Handle asynchronous network callbacks** with proper success and error handling
6. **Display loading states** (ProgressBar) while network requests are in progress
7. **Parse JSON responses** from backend and extract prediction data
8. **Navigate to ResultActivity** with prediction data after successful upload
9. **Implement error handling** for network failures, timeouts, and server errors
10. **Test end-to-end communication** between Android app and FastAPI backend

**Measurable Outcomes:**
- Android app successfully uploads images to FastAPI `/predict` endpoint
- JSON response parsed and disease prediction displayed in ResultActivity
- Loading indicator shown during network operations
- Error messages displayed when backend is unavailable or network fails
- No app crashes when network operations fail
- Git commits showing incremental networking implementation

---

## Why This Week Matters

### Connection to CSE 2206 Mobile Application Development

Week 05 is the **critical integration point** where your Android app transforms from a local-only application into a **network-enabled mobile application**. This week demonstrates:

- **HTTP Networking:** Making POST requests with multipart data (core syllabus requirement)
- **JSON Parsing:** Converting server responses into Java objects
- **Asynchronous Programming:** Handling network callbacks on background threads
- **Error Handling:** Managing network failures gracefully
- **User Experience:** Showing progress indicators and error messages

**This is a mandatory CSE 2206 demonstration topic.** Your teacher will expect you to explain:
- How Retrofit simplifies HTTP networking in Android
- The difference between synchronous vs asynchronous network calls
- How to prevent UI freezing during network operations
- How to handle network errors without crashing the app

### Academic Requirement Alignment

CSE 2206 syllabus explicitly requires:

1. **Network Programming (Week 5-6 syllabus):** HTTP POST requests, multipart file upload, JSON parsing
2. **Asynchronous Operations:** Network calls must not block the main UI thread
3. **Third-Party Library Integration:** Using Retrofit demonstrates professional Android development
4. **Client-Server Architecture:** Completing the full request-response cycle from Week 04

**Viva Question Preview:**
- "Explain how your Android app communicates with the backend"
- "What happens if the user has no internet connection?"
- "Why can't you make network calls on the main thread?"
- "How does Retrofit convert JSON to Java objects?"

---

## Syllabus Topics Covered This Week

### Direct Coverage

1. **Android Networking Fundamentals**
   - HTTP protocol in mobile context
   - Network permissions (INTERNET permission)
   - Making POST requests with image data
   - Handling network responses

2. **Asynchronous Programming**
   - Callbacks and listener interfaces
   - Background thread management
   - UI thread updates after network operations
   - Understanding Retrofit's enqueue() method

3. **Third-Party Libraries**
   - Adding Gradle dependencies (Retrofit, Gson, OkHttp)
   - Configuring library initialization
   - Using library APIs effectively
   - Reading library documentation

4. **JSON Data Parsing**
   - Creating data model classes matching JSON structure
   - Automatic deserialization with Gson
   - Accessing nested JSON fields
   - Handling optional/nullable fields

5. **Error Handling**
   - Try-catch for network errors
   - Timeout configuration
   - Server error code handling (404, 500, etc.)
   - User-friendly error messages

6. **User Experience**
   - Showing ProgressBar during uploads
   - Hiding progress after completion
   - Disabling buttons during network operations
   - Toast messages for feedback

### CSE 2206 Concept Connections

| Syllabus Topic | LeafGuard AI Implementation | This Week |
|----------------|----------------------------|-----------|
| Network Programming | HTTP POST image upload | ✅ Core focus |
| JSON Parsing | Gson converts JSON to Java objects | ✅ Core focus |
| Asynchronous Operations | Retrofit callbacks on background thread | ✅ Core focus |
| Error Handling | Try-catch, onFailure callbacks | ✅ Core focus |
| Third-Party Libraries | Retrofit, Gson, OkHttp integration | ✅ Core focus |
| User Interface | ProgressBar, Toast messages | ✅ Applied |
| Intent Navigation | Passing data to ResultActivity | ✅ Applied |

---

## Prerequisites

### Completed Previous Weeks

- **Week 03:** Camera and gallery image capture working, permissions handled
- **Week 04:** FastAPI backend running, `/predict` endpoint tested with Postman

**Critical checkpoint:** Before starting Week 05, you MUST:
1. Have FastAPI backend running and accessible from your phone's browser (http://YOUR_IP:8000/docs)
2. Have images successfully captured in your Android app (from Week 03)
3. Be able to ping your laptop's IP from your phone (both on same Wi-Fi network)

### Required Knowledge

1. **Android Basics:**
   - Activities and Intents
   - UI components (Button, ImageView, ProgressBar, TextView)
   - Event listeners (onClick)
   - Using strings.xml resources

2. **Java/Kotlin Fundamentals:**
   - Classes and objects
   - Methods and callbacks
   - Interfaces
   - Anonymous classes or lambda expressions
   - Null safety basics

3. **Gradle Basics:**
   - How to add dependencies to `build.gradle (Module: app)`
   - Sync project after Gradle changes
   - Understanding library versions

4. **Networking Concepts:**
   - What is HTTP POST?
   - What is JSON?
   - Basic understanding of request headers
   - What is multipart form-data?

---

## Learning Resources

### Official Documentation (Read First)

1. **Retrofit:**
   - Official site: https://square.github.io/retrofit/
   - Getting Started Guide
   - Annotations reference (@POST, @Multipart, @Part)

2. **Gson:**
   - GitHub: https://github.com/google/gson
   - User Guide: https://github.com/google/gson/blob/master/UserGuide.md

3. **Android Developer:**
   - Connecting to the Network: https://developer.android.com/training/basics/network-ops/connecting
   - Permissions: https://developer.android.com/training/permissions/requesting

### Video Tutorials (Recommended)

1. **Retrofit Tutorial for Beginners** (YouTube)
   - Search: "Android Retrofit Tutorial 2024"
   - Focus on POST requests with multipart data
   - Watch videos that explain Gson integration

2. **Asynchronous Programming in Android** (YouTube)
   - Search: "Android Background Tasks Tutorial"
   - Understand why network calls can't be on main thread

### Reading Materials

1. **Retrofit vs OkHttp vs HttpURLConnection:**
   - Understand why Retrofit is preferred for Android
   - Retrofit provides cleaner API and automatic JSON parsing

2. **Understanding Callbacks:**
   - How `onResponse()` and `onFailure()` work
   - Difference between synchronous and asynchronous calls

---

## Conceptual Overview

### What You're Building This Week

```
Android App                     FastAPI Backend
┌─────────────┐                ┌──────────────┐
│             │                │              │
│  [Image]    │                │  /predict    │
│             │   HTTP POST    │              │
│  [Upload]◄──┼────────────────►  endpoint    │
│   Button    │  multipart     │              │
│             │                │  Returns     │
│ ProgressBar │                │  JSON        │
│             │                │              │
│  [Result]   │◄───JSON────────┤              │
│  Activity   │  Response      │              │
└─────────────┘                └──────────────┘
```

### Request-Response Flow

1. **User Action:** User taps "Detect Disease" button after capturing image
2. **UI Feedback:** ProgressBar becomes visible, button disabled
3. **Image Preparation:** Convert Bitmap to File or RequestBody
4. **Network Request:** Retrofit creates HTTP POST with multipart data
5. **Backend Processing:** FastAPI receives image, processes it, returns JSON
6. **Response Handling:**
   - Success: Parse JSON, extract disease name and confidence
   - Failure: Show error Toast message
7. **UI Update:** Hide ProgressBar, navigate to ResultActivity or show error
8. **Display Result:** Show disease name, confidence, recommendations

### Key Retrofit Concepts

#### 1. API Service Interface

Defines HTTP methods using annotations:

```java
public interface ApiService {
    @Multipart
    @POST("predict")
    Call<PredictionResponse> uploadImage(@Part MultipartBody.Part image);
}
```

**Explanation:**
- `@POST("predict")`: Makes POST request to `/predict` endpoint
- `@Multipart`: Indicates multipart form-data content type
- `@Part`: Marks image parameter as part of multipart request
- `Call<PredictionResponse>`: Return type wrapping async operation

#### 2. Retrofit Client Singleton

Configures Retrofit instance once for entire app:

```java
public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.1.10:8000/";
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

**Why singleton?** Creating Retrofit instances is expensive. Reuse one instance.

#### 3. Data Models

Java classes matching JSON structure:

```java
public class PredictionResponse {
    private String disease;
    private double confidence;
    private String symptoms;
    private String treatment;

    // Getters and setters
}
```

Gson automatically maps JSON keys to class fields.

#### 4. Asynchronous Callbacks

```java
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

**Key Point:** Callbacks run on background thread automatically. UI updates must be on main thread (using `runOnUiThread()` if needed, but Retrofit handles this for you).

---

## Weekly Timeline

### Day 1: Retrofit Setup and Configuration

**Morning (Theory - 1 hour):**
- Read Retrofit official documentation
- Watch "Retrofit Tutorial for Beginners" video (30 minutes)
- Understand HTTP POST and multipart concepts

**Afternoon (Implementation - 2 hours):**
- Add Retrofit, Gson, OkHttp dependencies to `build.gradle`
- Create `ApiService` interface with basic structure
- Create `RetrofitClient` singleton class
- Configure BASE_URL with your laptop's local IP

**Evening (Testing - 30 minutes):**
- Sync Gradle and fix any dependency conflicts
- Verify app still builds without errors
- Commit: "week-05: add Retrofit and Gson dependencies"

---

### Day 2: Data Models and API Interface

**Morning (Theory - 1 hour):**
- Review your FastAPI `/predict` JSON response structure from Week 04
- Understand JSON-to-Java mapping with Gson
- Learn about Gson annotations (@SerializedName if needed)

**Afternoon (Implementation - 2 hours):**
- Create `PredictionResponse.java` data model
- Match fields to FastAPI JSON response
- Add getters and setters
- Complete `ApiService` interface with @Multipart and @Part annotations

**Evening (Review - 30 minutes):**
- Double-check JSON field names match backend response
- Commit: "week-05: create data models and API service interface"

---

### Day 3: Image Upload Implementation

**Morning (Theory - 1 hour):**
- Learn about `RequestBody` and `MultipartBody.Part`
- Understand how to convert File to RequestBody
- Review error handling patterns

**Afternoon (Implementation - 3 hours):**
- Create method to convert image file to MultipartBody.Part
- Implement upload button click listener
- Add ProgressBar to MainActivity layout
- Write upload logic with Retrofit call

**Evening (Debugging - 1 hour):**
- Test with simple image
- Check Logcat for network errors
- Commit: "week-05: implement image upload with Retrofit"

---

### Day 4: Response Handling and Navigation

**Morning (Theory - 1 hour):**
- Review callback patterns
- Understand `onResponse()` vs `onFailure()`
- Learn about HTTP status codes

**Afternoon (Implementation - 2 hours):**
- Implement `onResponse()` callback
- Parse PredictionResponse object
- Create Intent to ResultActivity with data
- Pass disease name, confidence, symptoms, treatment as Intent extras

**Evening (Testing - 1 hour):**
- Test successful upload and navigation
- Verify data displays correctly in ResultActivity
- Commit: "week-05: handle response and navigate to result"

---

### Day 5: Error Handling and Loading States

**Morning (Theory - 1 hour):**
- Study common network errors
- Learn timeout configuration
- Understand user feedback best practices

**Afternoon (Implementation - 3 hours):**
- Implement `onFailure()` callback
- Show Toast messages for different error types
- Handle timeout scenarios
- Configure OkHttp client with custom timeout values
- Show/hide ProgressBar appropriately
- Disable upload button during network operation

**Evening (Testing - 1 hour):**
- Test with backend stopped (server error)
- Test with Wi-Fi off (network error)
- Test with slow network (timeout)
- Commit: "week-05: add error handling and loading states"

---

### Day 6: Edge Cases and Permissions

**Morning (Theory - 1 hour):**
- Review Android permission system
- Learn about INTERNET permission (declared in manifest)
- Understand network security configuration

**Afternoon (Implementation - 2 hours):**
- Add INTERNET permission to AndroidManifest.xml
- Handle case where image is null
- Handle case where user taps upload without selecting image
- Add validation before upload
- Configure network security (allow cleartext traffic for local IP)

**Evening (Testing - 1 hour):**
- Test all edge cases
- Verify no crashes
- Commit: "week-05: handle edge cases and add permissions"

---

### Day 7: Integration Testing and Documentation

**Morning (Testing - 2 hours):**
- Test complete flow: camera → upload → result
- Test with multiple images
- Test error scenarios
- Verify all Toast messages appear correctly

**Afternoon (Documentation - 2 hours):**
- Complete Week 05 validation checklist
- Take screenshots: upload in progress, successful result, error messages
- Update progress-tracker.md
- Fill out reflection.md

**Evening (Final Review - 1 hour):**
- Complete Week 05 quiz
- Review learning notes
- Commit: "week-05: complete integration testing and documentation"

---

## Common Mistakes to Avoid

### 1. ❌ Hardcoding IP Address in Multiple Places

**Problem:** BASE_URL copied in several files, makes updates difficult

**Solution:** Define BASE_URL once in RetrofitClient, reference it everywhere:

```java
public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.1.10:8000/";
    // Use this everywhere
}
```

### 2. ❌ Making Network Calls on Main Thread

**Problem:** App freezes or crashes with NetworkOnMainThreadException

**Solution:** Always use `enqueue()` (asynchronous), never `execute()` (synchronous):

```java
// ✅ Correct - asynchronous
call.enqueue(new Callback<PredictionResponse>() { ... });

// ❌ Wrong - synchronous
call.execute(); // Don't do this!
```

### 3. ❌ Not Handling Null Responses

**Problem:** App crashes when backend returns empty response

**Solution:** Always check if response body is null:

```java
if (response.isSuccessful() && response.body() != null) {
    PredictionResponse prediction = response.body();
    // Safe to use prediction
}
```

### 4. ❌ Forgetting INTERNET Permission

**Problem:** Network requests fail silently

**Solution:** Add to AndroidManifest.xml:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

### 5. ❌ Using `http://` for localhost

**Problem:** Typing `http://localhost:8000` doesn't work from phone

**Solution:** Use your laptop's actual local IP:

```
Windows: ipconfig
macOS/Linux: ifconfig or ip addr
Look for 192.168.x.x address
```

### 6. ❌ Not Configuring Network Security for HTTP

**Problem:** Android 9+ blocks cleartext (HTTP) traffic by default

**Solution:** Add network security config in AndroidManifest.xml:

```xml
<application
    android:networkSecurityConfig="@xml/network_security_config">
```

And create `res/xml/network_security_config.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">192.168.1.10</domain>
    </domain-config>
</network-security-config>
```

### 7. ❌ Not Showing Loading State

**Problem:** User thinks app is frozen during upload

**Solution:** Show ProgressBar and disable button:

```java
// Before upload
progressBar.setVisibility(View.VISIBLE);
uploadButton.setEnabled(false);

// After response (success or failure)
progressBar.setVisibility(View.GONE);
uploadButton.setEnabled(true);
```

### 8. ❌ Generic Error Messages

**Problem:** "Error occurred" doesn't help user understand what happened

**Solution:** Provide specific messages:

```java
if (t instanceof IOException) {
    Toast.makeText(this, "Network error. Check your internet connection.", Toast.LENGTH_LONG).show();
} else if (t instanceof SocketTimeoutException) {
    Toast.makeText(this, "Request timed out. Server might be down.", Toast.LENGTH_LONG).show();
} else {
    Toast.makeText(this, "Unexpected error: " + t.getMessage(), Toast.LENGTH_LONG).show();
}
```

---

## Teacher Demonstration Tips

### What to Show Your Teacher

1. **Open FastAPI backend terminal** showing server running
2. **Open Android app** on phone or emulator
3. **Capture an image** using camera or gallery
4. **Tap "Detect Disease" button**
5. **Point out:**
   - ProgressBar appears (loading state)
   - Network request happens in background
   - No UI freeze
6. **Show ResultActivity** with disease prediction
7. **Demonstrate error handling:**
   - Stop FastAPI server
   - Try upload again
   - Show error Toast message
   - App doesn't crash

### Key Points to Explain

1. **"I'm using Retrofit, a professional HTTP client library for Android"**
   - Simplifies network code
   - Handles threading automatically
   - Integrates with Gson for JSON parsing

2. **"The app makes an HTTP POST request with multipart data"**
   - Sends image file to backend
   - Backend processes and returns JSON
   - App parses JSON into Java objects

3. **"Network operations happen asynchronously"**
   - Main thread stays responsive
   - User sees progress indicator
   - Callbacks handle response or errors

4. **"I implemented comprehensive error handling"**
   - Network failures show user-friendly messages
   - App doesn't crash when backend is down
   - Timeout configuration prevents infinite waiting

### Viva Question Preparation

**Q: Why can't you make network calls on the main thread?**
A: The main thread handles UI rendering. Blocking it with network operations would freeze the app. Android enforces this with NetworkOnMainThreadException. I use Retrofit's `enqueue()` which runs on a background thread automatically.

**Q: How does Retrofit convert JSON to Java objects?**
A: I configured Retrofit with GsonConverterFactory. Gson reads the JSON response, matches field names to my PredictionResponse class properties, and creates Java objects automatically. This eliminates manual JSON parsing code.

**Q: What happens if the user has no internet?**
A: The `onFailure()` callback is triggered. I check if the error is an IOException (network error) and show a Toast message: "Network error. Check your internet connection." The app doesn't crash.

**Q: How do you send an image in an HTTP request?**
A: I convert the image File to a RequestBody, then wrap it in MultipartBody.Part with the @Part annotation. Retrofit builds a multipart/form-data request automatically. The backend receives it as a file upload.

**Q: What is the difference between synchronous and asynchronous calls?**
A: Synchronous calls block the thread until response is received. Asynchronous calls return immediately and use callbacks to deliver results later. I use `enqueue()` (async) instead of `execute()` (sync) to keep the UI responsive.

---

## Debugging Checklist

If your networking isn't working, check these in order:

### 1. ✅ Backend is Running

```bash
# In terminal, you should see:
INFO:     Uvicorn running on http://0.0.0.0:8000
```

Test in browser: `http://YOUR_IP:8000/docs`

### 2. ✅ Phone and Laptop on Same Wi-Fi

- Check Wi-Fi network name on both devices
- Must be identical network
- Some public Wi-Fi networks block device-to-device communication

### 3. ✅ Correct IP Address in RetrofitClient

```java
// ❌ Wrong
private static final String BASE_URL = "http://localhost:8000/";
private static final String BASE_URL = "http://127.0.0.1:8000/";

// ✅ Correct
private static final String BASE_URL = "http://192.168.1.10:8000/";
```

### 4. ✅ INTERNET Permission in Manifest

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

### 5. ✅ Network Security Config for HTTP

Required for Android 9+. See "Common Mistakes" section above.

### 6. ✅ Gradle Dependencies Added

```gradle
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'
```

### 7. ✅ Gradle Synced Successfully

After adding dependencies, click "Sync Now" and wait for success.

### 8. ✅ JSON Structure Matches

FastAPI response:
```json
{
  "disease": "Tomato Late Blight",
  "confidence": 0.87,
  "symptoms": "...",
  "treatment": "..."
}
```

Java class:
```java
private String disease;
private double confidence;
private String symptoms;
private String treatment;
```

Field names must match exactly (or use @SerializedName).

---

## Validation Criteria

Before moving to Week 06, you MUST demonstrate:

### Technical Validation

- [ ] Retrofit and Gson dependencies added to build.gradle
- [ ] ApiService interface created with @POST annotation
- [ ] RetrofitClient singleton implemented
- [ ] PredictionResponse data model matches backend JSON
- [ ] Image successfully converts to MultipartBody.Part
- [ ] Upload button triggers network request
- [ ] ProgressBar shows during upload
- [ ] onResponse() callback receives data
- [ ] ResultActivity displays disease name and confidence
- [ ] onFailure() callback handles errors
- [ ] Toast messages shown for network errors
- [ ] INTERNET permission in AndroidManifest.xml
- [ ] Network security config allows HTTP to local IP
- [ ] App doesn't crash when backend is stopped
- [ ] App doesn't crash when network is unavailable

### User Experience Validation

- [ ] Loading indicator visible during upload
- [ ] Upload button disabled during network operation
- [ ] Clear error messages for different failure types
- [ ] Smooth transition to ResultActivity after success
- [ ] No UI freeze during network operations

### Code Quality Validation

- [ ] BASE_URL defined in one location only
- [ ] Proper null checks for response.body()
- [ ] Try-catch blocks where appropriate
- [ ] Meaningful variable names
- [ ] No hardcoded strings (use strings.xml)

---

## Success Metrics

You have successfully completed Week 05 when:

1. **End-to-End Test:** You can capture an image, upload it, and see the dummy prediction in ResultActivity
2. **Error Handling Test:** You can stop the backend, try uploading, and see a user-friendly error message without app crash
3. **Network Security:** You understand why HTTP requires special configuration on Android 9+
4. **Explanation Ability:** You can explain how Retrofit works to your teacher during demonstration
5. **Documentation:** All screenshots saved, validation checklist completed, reflection written

---

## Next Week Preview: Week 06 - Cloud ML Model Integration

Now that your Android app can communicate with your FastAPI backend, Week 06 will:

- Replace dummy predictions with real TensorFlow model inference
- Implement image preprocessing in backend
- Load trained plant disease detection model
- Return actual confidence scores and disease names
- Handle model limitations and fallback strategies

Week 05 built the communication pipeline. Week 06 makes it intelligent.

---

## Final Notes

### Time Estimate

- **Minimum:** 15-20 hours
- **Expected:** 20-25 hours
- **With debugging:** 25-30 hours

This is a networking-heavy week. Budget extra time for debugging connection issues.

### When to Ask for Help

Get help if:
- After 2 hours of debugging, you still can't connect to backend
- Retrofit dependencies cause Gradle sync errors
- You don't understand callbacks after reading documentation
- App crashes and Logcat error is unclear

### Collaboration Policy

- **Allowed:** Discussing Retrofit concepts, debugging connection issues together
- **Not Allowed:** Copying someone else's RetrofitClient or ApiService code

Write your own code. Understand every line.

---

**Ready to start? Open `learning-notes.md` to begin your Week 05 journey!**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 05 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| **1** | **README.md** ← *You are here* | **Week Overview & Objectives** |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

*(Start of week)* &nbsp;&nbsp;|&nbsp;&nbsp; **Week Overview & Objectives** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Theory & Learning Notes →](learning-notes.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 04: FastAPI Backend](../week-04-fastapi-backend/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 06: Cloud ML Model ➡](../week-06-cloud-ml-model/README.md) |

---
