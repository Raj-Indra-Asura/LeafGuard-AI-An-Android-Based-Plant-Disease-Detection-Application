# Week 05: Validation Checklist - Android Networking with Retrofit

## Overview

This checklist provides **pass/fail validation criteria** for Week 05. Each item must pass before you move to Week 06. There are no partial points - features either work or they don't.

**How to use this checklist:**
1. Complete the Week 05 build task first
2. Test each validation point systematically
3. Mark ✅ only when the criterion is fully met
4. If any item fails, fix it before proceeding
5. Save screenshots as evidence for items marked with 📸

---

## Section 1: Gradle Setup and Dependencies

### 1.1 Dependencies Added

- [ ] **Retrofit 2.9.0 dependency** added to `build.gradle (Module: app)`
- [ ] **Gson converter 2.9.0 dependency** added to `build.gradle (Module: app)`
- [ ] **OkHttp logging interceptor 4.9.0 dependency** added to `build.gradle (Module: app)`
- [ ] **Gradle sync completed successfully** without errors
- [ ] **Build → Make Project succeeds** without compilation errors

**Pass Criteria:** All 5 items checked. Project builds successfully.

---

## Section 2: Data Models and API Interface

### 2.1 PredictionResponse Model

- [ ] **PredictionResponse.java class created** with all required fields
- [ ] **Field names match backend JSON keys exactly** (disease, confidence, symptoms, treatment, prevention)
- [ ] **All getter methods implemented** and working
- [ ] **No compilation errors** in PredictionResponse class

### 2.2 API Service Interface

- [ ] **ApiService.java interface created** (not class - must be interface)
- [ ] **@Multipart annotation present** on uploadImage method
- [ ] **@POST("predict") annotation present** and matches backend endpoint
- [ ] **Method signature correct:** `Call<PredictionResponse> uploadImage(@Part MultipartBody.Part image)`
- [ ] **@Part annotation present** on image parameter
- [ ] **No compilation errors** in ApiService interface

**Pass Criteria:** All 10 items checked. Both classes compile without errors.

---

## Section 3: Retrofit Client Configuration

### 3.1 RetrofitClient Singleton

- [ ] **RetrofitClient.java class created**
- [ ] **Singleton pattern implemented** (static Retrofit instance, null check)
- [ ] **BASE_URL configured** with your actual laptop local IP
- [ ] **BASE_URL ends with forward slash** (e.g., "http://192.168.1.10:8000/")
- [ ] **GsonConverterFactory added** to Retrofit builder
- [ ] **Logging interceptor configured** (Level.BODY for debugging)
- [ ] **Timeouts configured** (30 seconds for connect, read, write)
- [ ] **getApiService() helper method** implemented and returns ApiService instance

**Test:** Call `RetrofitClient.getApiService()` - should return non-null ApiService instance.

**Pass Criteria:** All 8 items checked. No crashes when calling getClient() or getApiService().

---

## Section 4: Network Security and Permissions

### 4.1 Network Security Configuration

- [ ] **network_security_config.xml created** in `res/xml/` directory
- [ ] **cleartext traffic permitted** for your local IP domain
- [ ] **IP address in config matches your laptop IP** exactly
- [ ] **network_security_config referenced in AndroidManifest.xml** with `android:networkSecurityConfig` attribute

### 4.2 Permissions

- [ ] **INTERNET permission present** in AndroidManifest.xml
- [ ] **Permission declared before `<application>` tag** (correct location)

**Pass Criteria:** All 6 items checked. App has network access permissions.

---

## Section 5: Image Upload Implementation

### 5.1 UI Components

- [ ] **ProgressBar added** to MainActivity layout
- [ ] **ProgressBar initially invisible** (android:visibility="gone")
- [ ] **"Detect Disease" button added** to layout
- [ ] **Button initially disabled** until image captured

### 5.2 Image Preparation

- [ ] **Captured Bitmap saved to File** after camera/gallery selection
- [ ] **File created in cache directory** or app-specific directory
- [ ] **Image file compression works** (JPEG, 80% quality)
- [ ] **Detect button enabled** after image saved successfully
- [ ] **No crashes** when saving image to file

### 5.3 Multipart Request Creation

- [ ] **RequestBody created** from image File with MediaType "image/*"
- [ ] **MultipartBody.Part created** with correct parameter name ("image")
- [ ] **Filename included** in MultipartBody.Part
- [ ] **No crashes** when creating multipart request

**Pass Criteria:** All 13 items checked. Image successfully converts to uploadable format.

---

## Section 6: Network Request Execution

### 6.1 Request Initiation

- [ ] **API service instance retrieved** from RetrofitClient
- [ ] **uploadImage() method called** with MultipartBody.Part parameter
- [ ] **enqueue() used** for asynchronous execution (not execute())
- [ ] **Callback implemented** with both onResponse() and onFailure()

### 6.2 Loading State Management

- [ ] **ProgressBar becomes visible** when upload starts
- [ ] **Detect button becomes disabled** when upload starts
- [ ] **ProgressBar hidden** in both onResponse() and onFailure()
- [ ] **Button re-enabled** in both onResponse() and onFailure()

**Test:** Upload image and observe UI changes.

**Pass Criteria:** All 8 items checked. Loading indicator works correctly.

---

## Section 7: Response Handling

### 7.1 Success Path

- [ ] **response.isSuccessful() checked** before accessing body
- [ ] **response.body() null check** performed
- [ ] **PredictionResponse object extracted** from response.body()
- [ ] **All fields accessed without crashes** (disease, confidence, etc.)
- [ ] **Intent created** with prediction data
- [ ] **Data passed as Intent extras** to ResultActivity
- [ ] **ResultActivity started** successfully

### 7.2 Error Handling

- [ ] **HTTP errors handled** (4xx, 5xx status codes) in onResponse()
- [ ] **Network errors handled** (IOException) in onFailure()
- [ ] **Toast messages shown** for errors
- [ ] **Error messages are user-friendly** (not just exception stack traces)
- [ ] **App never crashes** when errors occur

**Test Success Path:** Upload image with backend running - should navigate to ResultActivity.

**Test Error Path:** Stop backend and upload - should show error Toast without crash.

**Pass Criteria:** All 12 items checked. Both success and error paths work correctly.

---

## Section 8: Result Display

### 8.1 ResultActivity Layout

- [ ] **Disease name TextView** present in layout
- [ ] **Confidence TextView** present in layout
- [ ] **Symptoms TextView** present in layout
- [ ] **Treatment TextView** present in layout
- [ ] **Prevention TextView** present in layout

### 8.2 Data Display

- [ ] **Disease name displayed** correctly in ResultActivity
- [ ] **Confidence displayed as percentage** (e.g., "87.3%", not "0.873")
- [ ] **Confidence formatted** with 1 decimal place
- [ ] **Symptoms text displayed** correctly
- [ ] **Treatment text displayed** correctly
- [ ] **Prevention text displayed** correctly
- [ ] **Null values handled** gracefully (no crashes if fields are null)
- [ ] **Default text shown** for null fields (e.g., "No information available")

**Test:** Complete upload and verify all fields display in ResultActivity.

📸 **Screenshot Required:** ResultActivity showing full prediction result.

**Pass Criteria:** All 13 items checked. All prediction data displays correctly.

---

## Section 9: End-to-End Integration Test

### 9.1 Complete Flow

Perform this test sequence and check each step:

1. [ ] **Launch app** - opens MainActivity without crash
2. [ ] **Capture image** with camera - camera intent works
3. [ ] **Image displays** in ImageView after capture
4. [ ] **Detect button enabled** after image captured
5. [ ] **Tap "Detect Disease" button** - no immediate crash
6. [ ] **ProgressBar appears** immediately
7. [ ] **Button disabled** (grayed out) during upload
8. [ ] **Wait 1-5 seconds** - no crash during wait
9. [ ] **ProgressBar disappears** after response received
10. [ ] **ResultActivity opens** automatically
11. [ ] **Disease name shows** in ResultActivity
12. [ ] **Confidence shows as percentage** (e.g., "87%")
13. [ ] **All text fields populated** (symptoms, treatment, prevention)
14. [ ] **Back button works** - can return to MainActivity
15. [ ] **Can perform multiple uploads** consecutively without crash

📸 **Screenshot Required:** Complete sequence from MainActivity → Upload → ResultActivity.

**Pass Criteria:** All 15 steps complete successfully without any crashes.

---

## Section 10: Error Scenario Testing

### 10.1 Network Error (No Internet)

**Setup:** Turn off Wi-Fi and mobile data on phone.

- [ ] **Turn off internet** on phone
- [ ] **Attempt image upload** by tapping button
- [ ] **Error Toast appears** with network error message
- [ ] **Toast message is user-friendly** (mentions network/connection)
- [ ] **App does not crash**
- [ ] **ProgressBar hidden** after error
- [ ] **Button re-enabled** after error
- [ ] **Can retry after turning internet back on**

📸 **Screenshot Required:** Error Toast displayed.

### 10.2 Backend Down

**Setup:** Stop FastAPI server on laptop.

- [ ] **Stop backend server** (Ctrl+C in terminal)
- [ ] **Attempt image upload** from app
- [ ] **Error Toast appears** (may say timeout or connection refused)
- [ ] **App does not crash**
- [ ] **ProgressBar hidden** after error
- [ ] **Button re-enabled** after error
- [ ] **Can retry after restarting backend**

### 10.3 Wrong IP Address

**Setup:** Change BASE_URL to invalid IP (e.g., 192.168.1.99).

- [ ] **Change BASE_URL** in RetrofitClient
- [ ] **Rebuild and install app**
- [ ] **Attempt upload**
- [ ] **Error Toast appears** (connection timeout or failed to connect)
- [ ] **App does not crash**

### 10.4 No Image Captured

**Setup:** Launch app without capturing image.

- [ ] **Launch app fresh** (without capturing image)
- [ ] **"Detect Disease" button disabled** (or validation prevents upload)
- [ ] **If button enabled, tapping shows error** message
- [ ] **App does not crash**

**Pass Criteria:** All 19 error scenario items checked. App handles all error cases gracefully.

---

## Section 11: Code Quality

### 11.1 Code Organization

- [ ] **No hardcoded strings** in Java code (use strings.xml)
- [ ] **BASE_URL defined once** in RetrofitClient only
- [ ] **Proper null checks** before accessing response.body()
- [ ] **Try-catch blocks** where appropriate (file operations, etc.)
- [ ] **Meaningful variable names** (no `temp`, `x`, `data1`)

### 11.2 Imports and Structure

- [ ] **No unused imports** in any class
- [ ] **No compilation warnings** in Android Studio
- [ ] **Package structure organized** (classes in appropriate packages)

**Pass Criteria:** All 8 items checked. Code follows best practices.

---

## Section 12: Debugging and Logging

### 12.1 Logcat Output

- [ ] **HTTP requests visible** in Logcat (filtered by "OkHttp")
- [ ] **Request method shown** (POST /predict)
- [ ] **Request body logged** (multipart data)
- [ ] **Response status code shown** (200 OK)
- [ ] **Response JSON visible** in Logcat
- [ ] **Can read and understand** network logs

**Test:** Upload image and observe Logcat during request.

📸 **Screenshot Required:** Logcat showing HTTP request and response.

**Pass Criteria:** All 6 items checked. Can debug using network logs.

---

## Section 13: Documentation and Evidence

### 13.1 Evidence Collection

- [ ] **Screenshot: MainActivity with image** captured
- [ ] **Screenshot: ProgressBar visible** during upload
- [ ] **Screenshot: ResultActivity** with prediction displayed
- [ ] **Screenshot: Error Toast** (network error scenario)
- [ ] **Screenshot: Logcat** showing HTTP request/response
- [ ] **All screenshots saved** in `docs/evidence/week-05/`

### 13.2 Documentation

- [ ] **Week 05 reflection completed**
- [ ] **Week 05 quiz attempted**
- [ ] **progress-tracker.md updated** with Week 05 completion
- [ ] **Git commits made** with meaningful messages
- [ ] **Can explain how Retrofit works** in your own words

📸 **Evidence Folder:** Must contain at least 5 screenshots.

**Pass Criteria:** All 11 items checked. Evidence collected and documented.

---

## Final Validation Summary

### Minimum Passing Requirements

To pass Week 05 validation, you MUST achieve:

1. ✅ **All Section 9 items pass** (end-to-end integration test)
2. ✅ **All Section 10 items pass** (error handling tests)
3. ✅ **At least 90% of all other items pass** (allow minor issues in non-critical areas)
4. ✅ **Zero crashes during normal operation**
5. ✅ **Zero crashes during error scenarios**
6. ✅ **Evidence collected** (minimum 5 screenshots)

### Items Completed

**Total Items:** ~130 validation points

**Your Score:** _____ / 130

**Percentage:** _____ %

**Status:** [ ] PASS (≥90%) | [ ] FAIL (<90%)

---

## If You Fail Validation

If you have not achieved 90% completion:

1. **Review failed items** - note specific validation points not met
2. **Debug systematically** - use Logcat and Toast messages
3. **Re-read build-task.md** - ensure you followed all steps
4. **Check common mistakes** section in README.md
5. **Test again** after fixes
6. **Do NOT move to Week 06** until Week 05 validation passes

---

## Teacher Demonstration Checklist

When demonstrating to your teacher, be ready to show:

- [ ] **FastAPI backend running** in terminal
- [ ] **App running on phone** or emulator
- [ ] **Complete upload flow** (camera → upload → result)
- [ ] **Error handling demo** (stop backend, show error, restart, retry)
- [ ] **Logcat showing network requests**
- [ ] **Can explain Retrofit architecture** verbally
- [ ] **Can explain difference between onResponse() and onFailure()**
- [ ] **Can explain why async networking is required**

---

## Week 05 Validation Complete!

Once you pass all validation criteria:

- [ ] **Mark Week 05 as complete** in progress-tracker.md
- [ ] **Commit final version** to Git
- [ ] **Celebrate!** You've built a network-enabled Android app! 🎉
- [ ] **Ready for Week 06:** Real ML model integration in FastAPI backend

**Date Completed:** ___________

**Final Notes/Challenges Faced:**
_______________________________________________________________

---

**Next:** Open `roadmap/week-06-cloud-ml-model/README.md` to begin Week 06!
