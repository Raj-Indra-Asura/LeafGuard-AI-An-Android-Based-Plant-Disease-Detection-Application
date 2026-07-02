# Week 05: Validation Checklist - Android Networking with Retrofit

## Related materials

- Exercises (primary Kotlin): [../../exercises/android-kotlin/](../../exercises/android-kotlin/)
- Exercises (secondary Java): [../../exercises/android/](../../exercises/android/)
- Solutions: [../../solutions/week-05/](../../solutions/week-05/)
- Notebooks: [../../notebooks/week-05/](../../notebooks/week-05/)
- Glossary: [../../GLOSSARY.md](../../GLOSSARY.md)

---

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

- [ ] Can I confirm this works? Retrofit 2.9.0 dependency added to `build.gradle (Module: app)` — yes/no. Technical check: **Retrofit 2.9.0 dependency** added to `build.gradle (Module: app)`
- [ ] Can I confirm this works? Gson converter 2.9.0 dependency added to `build.gradle (Module: app)` — yes/no. Technical check: **Gson converter 2.9.0 dependency** added to `build.gradle (Module: app)`
- [ ] Can I confirm this works? OkHttp logging interceptor 4.9.0 dependency added to `build.gradle (Module: app)` — yes/no. Technical check: **OkHttp logging interceptor 4.9.0 dependency** added to `build.gradle (Module: app)`
- [ ] Can I confirm this works? Gradle sync completed successfully without errors — yes/no. Technical check: **Gradle sync completed successfully** without errors
- [ ] Can I confirm this works? Build → Make Project succeeds without compilation errors — yes/no. Technical check: **Build → Make Project succeeds** without compilation errors

**Pass Criteria:** All 5 items checked. Project builds successfully.

---

## Section 2: Data Models and API Interface

### 2.1 PredictionResponse Model

- [ ] Can I confirm this works? PredictionResponse.kt data class exists in Kotlin primary track (or PredictionResponse.java class exists in Java secondary track) with all required fields — yes/no. Technical check: **PredictionResponse.kt / PredictionResponse.java created** with all required fields
- [ ] Can I confirm this works? Field names match backend JSON keys exactly (disease, confidence, symptoms, treatment, prevention) — yes/no. Technical check: **Field names match backend JSON keys exactly** (disease, confidence, symptoms, treatment, prevention)
- [ ] Can I confirm this works? All getter methods implemented and working — yes/no. Technical check: **All getter methods implemented** and working
- [ ] Can I confirm this works? No compilation errors in PredictionResponse class — yes/no. Technical check: **No compilation errors** in PredictionResponse class

### 2.2 API Service Interface

- [ ] Can I confirm this works? ApiService.kt interface exists in Kotlin primary track (or ApiService.java in Java secondary track; not class - must be interface) — yes/no. Technical check: **ApiService.kt / ApiService.java interface created** (not class - must be interface)
- [ ] Can I confirm this works? @Multipart annotation present on uploadImage method — yes/no. Technical check: **@Multipart annotation present** on uploadImage method
- [ ] Can I confirm this works? @POST("predict") annotation present and matches backend endpoint — yes/no. Technical check: **@POST("predict") annotation present** and matches backend endpoint
- [ ] Can I confirm this works? Method signature correct: `Call<PredictionResponse> uploadImage(@Part MultipartBody.Part image)` — yes/no. Technical check: **Method signature correct:** `Call<PredictionResponse> uploadImage(@Part MultipartBody.Part image)`
- [ ] Can I confirm this works? @Part annotation present on image parameter — yes/no. Technical check: **@Part annotation present** on image parameter
- [ ] Can I confirm this works? No compilation errors in ApiService interface — yes/no. Technical check: **No compilation errors** in ApiService interface

**Pass Criteria:** All 10 items checked. Both classes compile without errors.

---

## Section 3: Retrofit Client Configuration

### 3.1 RetrofitClient Singleton

- [ ] Can I confirm this works? RetrofitClient.kt object exists in Kotlin primary track (or RetrofitClient.java class exists in Java secondary track) — yes/no. Technical check: **RetrofitClient.kt object exists in Kotlin primary track (or RetrofitClient.java class exists in Java secondary track)**
- [ ] Can I confirm this works? Singleton pattern implemented (static Retrofit instance, null check) — yes/no. Technical check: **Singleton pattern implemented** (static Retrofit instance, null check)
- [ ] Can I confirm this works? BASE_URL configured for emulator as `http://10.0.2.2:8000/` (or computer LAN IP for physical phone) — yes/no. Technical check: **BASE_URL configured** for emulator as `http://10.0.2.2:8000/` (or computer LAN IP for physical phone)
- [ ] Can I confirm this works? BASE_URL ends with forward slash (e.g., "http://10.0.2.2:8000/") — yes/no. Technical check: **BASE_URL ends with forward slash** (e.g., "http://10.0.2.2:8000/")
- [ ] Can I confirm this works? GsonConverterFactory added to Retrofit builder — yes/no. Technical check: **GsonConverterFactory added** to Retrofit builder
- [ ] Can I confirm this works? Logging interceptor configured (Level.BODY for debugging) — yes/no. Technical check: **Logging interceptor configured** (Level.BODY for debugging)
- [ ] Can I confirm this works? Timeouts configured (30 seconds for connect, read, write) — yes/no. Technical check: **Timeouts configured** (30 seconds for connect, read, write)
- [ ] Can I confirm this works? getApiService() helper method implemented and returns ApiService instance — yes/no. Technical check: **getApiService() helper method** implemented and returns ApiService instance

**Test:** Call `RetrofitClient.getApiService()` - should return non-null ApiService instance.

**Pass Criteria:** All 8 items checked. No crashes when calling getClient() or getApiService().

---

## Section 4: Network Security and Permissions

### 4.1 Network Security Configuration

- [ ] Can I confirm this works? network_security_config.xml created in `res/xml/` directory — yes/no. Technical check: **network_security_config.xml created** in `res/xml/` directory
- [ ] Can I confirm this works? cleartext traffic permitted for `10.0.2.2` in emulator (or your LAN IP for physical phone) — yes/no. Technical check: **cleartext traffic permitted** for `10.0.2.2` in emulator (or your LAN IP for physical phone)
- [ ] Can I confirm this works? Network security domain matches the URL target exactly (`10.0.2.2` for emulator, LAN IP for physical phone) — yes/no. Technical check: **Network security domain matches the URL target** exactly (`10.0.2.2` for emulator, LAN IP for physical phone)
- [ ] Can I confirm this works? network_security_config referenced in AndroidManifest.xml with `android:networkSecurityConfig` attribute — yes/no. Technical check: **network_security_config referenced in AndroidManifest.xml** with `android:networkSecurityConfig` attribute

### 4.2 Permissions

- [ ] Can I confirm this works? INTERNET permission present in AndroidManifest.xml — yes/no. Technical check: **INTERNET permission present** in AndroidManifest.xml
- [ ] Can I confirm this works? Permission declared before `<application>` tag (correct location) — yes/no. Technical check: **Permission declared before `<application>` tag** (correct location)

**Pass Criteria:** All 6 items checked. App has network access permissions.

---

## Section 5: Image Upload Implementation

### 5.1 UI Components

- [ ] Can I confirm this works? ProgressBar added to MainActivity layout — yes/no. Technical check: **ProgressBar added** to MainActivity layout
- [ ] Can I confirm this works? ProgressBar initially invisible (android:visibility="gone") — yes/no. Technical check: **ProgressBar initially invisible** (android:visibility="gone")
- [ ] Can I confirm this works? "Detect Disease" button added to layout — yes/no. Technical check: **"Detect Disease" button added** to layout
- [ ] Can I confirm this works? Button initially disabled until image captured — yes/no. Technical check: **Button initially disabled** until image captured

### 5.2 Image Preparation

- [ ] Can I confirm this works? Captured Bitmap saved to File after camera/gallery selection — yes/no. Technical check: **Captured Bitmap saved to File** after camera/gallery selection
- [ ] Can I confirm this works? File created in cache directory or app-specific directory — yes/no. Technical check: **File created in cache directory** or app-specific directory
- [ ] Can I confirm this works? Image file compression works (JPEG, 80% quality) — yes/no. Technical check: **Image file compression works** (JPEG, 80% quality)
- [ ] Can I confirm this works? Detect button enabled after image saved successfully — yes/no. Technical check: **Detect button enabled** after image saved successfully
- [ ] Can I confirm this works? No crashes when saving image to file — yes/no. Technical check: **No crashes** when saving image to file

### 5.3 Multipart Request Creation

- [ ] Can I confirm this works? RequestBody created from image File with MediaType "image/*" — yes/no. Technical check: **RequestBody created** from image File with MediaType "image/*"
- [ ] Can I confirm this works? MultipartBody.Part created with correct parameter name ("image") — yes/no. Technical check: **MultipartBody.Part created** with correct parameter name ("image")
- [ ] Can I confirm this works? Filename included in MultipartBody.Part — yes/no. Technical check: **Filename included** in MultipartBody.Part
- [ ] Can I confirm this works? No crashes when creating multipart request — yes/no. Technical check: **No crashes** when creating multipart request

**Pass Criteria:** All 13 items checked. Image successfully converts to uploadable format.

---

## Section 6: Network Request Execution

### 6.1 Request Initiation

- [ ] Can I confirm this works? API service instance retrieved from RetrofitClient — yes/no. Technical check: **API service instance retrieved** from RetrofitClient
- [ ] Can I confirm this works? uploadImage() method called with MultipartBody.Part parameter — yes/no. Technical check: **uploadImage() method called** with MultipartBody.Part parameter
- [ ] Can I confirm this works? enqueue() used for asynchronous execution (not execute()) — yes/no. Technical check: **enqueue() used** for asynchronous execution (not execute())
- [ ] Can I confirm this works? Callback implemented with both onResponse() and onFailure() — yes/no. Technical check: **Callback implemented** with both onResponse() and onFailure()

### 6.2 Loading State Management

- [ ] Can I confirm this works? ProgressBar becomes visible when upload starts — yes/no. Technical check: **ProgressBar becomes visible** when upload starts
- [ ] Can I confirm this works? Detect button becomes disabled when upload starts — yes/no. Technical check: **Detect button becomes disabled** when upload starts
- [ ] Can I confirm this works? ProgressBar hidden in both onResponse() and onFailure() — yes/no. Technical check: **ProgressBar hidden** in both onResponse() and onFailure()
- [ ] Can I confirm this works? Button re-enabled in both onResponse() and onFailure() — yes/no. Technical check: **Button re-enabled** in both onResponse() and onFailure()

**Test:** Upload image and observe UI changes.

**Pass Criteria:** All 8 items checked. Loading indicator works correctly.

---

## Section 7: Response Handling

### 7.1 Success Path

- [ ] Can I confirm this works? response.isSuccessful() checked before accessing body — yes/no. Technical check: **response.isSuccessful() checked** before accessing body
- [ ] Can I confirm this works? response.body() null check performed — yes/no. Technical check: **response.body() null check** performed
- [ ] Can I confirm this works? PredictionResponse object extracted from response.body() — yes/no. Technical check: **PredictionResponse object extracted** from response.body()
- [ ] Can I confirm this works? All fields accessed without crashes (disease, confidence, etc.) — yes/no. Technical check: **All fields accessed without crashes** (disease, confidence, etc.)
- [ ] Can I confirm this works? Intent created with prediction data — yes/no. Technical check: **Intent created** with prediction data
- [ ] Can I confirm this works? Data passed as Intent extras to ResultActivity — yes/no. Technical check: **Data passed as Intent extras** to ResultActivity
- [ ] Can I confirm this works? ResultActivity started successfully — yes/no. Technical check: **ResultActivity started** successfully

### 7.2 Error Handling

- [ ] Can I confirm this works? HTTP errors handled (4xx, 5xx status codes) in onResponse() — yes/no. Technical check: **HTTP errors handled** (4xx, 5xx status codes) in onResponse()
- [ ] Can I confirm this works? Network errors handled (IOException) in onFailure() — yes/no. Technical check: **Network errors handled** (IOException) in onFailure()
- [ ] Can I confirm this works? Toast messages shown for errors — yes/no. Technical check: **Toast messages shown** for errors
- [ ] Can I confirm this works? Error messages are user-friendly (not just exception stack traces) — yes/no. Technical check: **Error messages are user-friendly** (not just exception stack traces)
- [ ] Can I confirm this works? App never crashes when errors occur — yes/no. Technical check: **App never crashes** when errors occur

**Test Success Path:** Upload image with backend running - should navigate to ResultActivity.

**Test Error Path:** Stop backend and upload - should show error Toast without crash.

**Pass Criteria:** All 12 items checked. Both success and error paths work correctly.

---

## Section 8: Result Display

### 8.1 ResultActivity Layout

- [ ] Can I confirm this works? Disease name TextView present in layout — yes/no. Technical check: **Disease name TextView** present in layout
- [ ] Can I confirm this works? Confidence TextView present in layout — yes/no. Technical check: **Confidence TextView** present in layout
- [ ] Can I confirm this works? Symptoms TextView present in layout — yes/no. Technical check: **Symptoms TextView** present in layout
- [ ] Can I confirm this works? Treatment TextView present in layout — yes/no. Technical check: **Treatment TextView** present in layout
- [ ] Can I confirm this works? Prevention TextView present in layout — yes/no. Technical check: **Prevention TextView** present in layout

### 8.2 Data Display

- [ ] Can I confirm this works? Disease name displayed correctly in ResultActivity — yes/no. Technical check: **Disease name displayed** correctly in ResultActivity
- [ ] Can I confirm this works? Confidence displayed as percentage (e.g., "87.3%", not "0.873") — yes/no. Technical check: **Confidence displayed as percentage** (e.g., "87.3%", not "0.873")
- [ ] Can I confirm this works? Confidence formatted with 1 decimal place — yes/no. Technical check: **Confidence formatted** with 1 decimal place
- [ ] Can I confirm this works? Symptoms text displayed correctly — yes/no. Technical check: **Symptoms text displayed** correctly
- [ ] Can I confirm this works? Treatment text displayed correctly — yes/no. Technical check: **Treatment text displayed** correctly
- [ ] Can I confirm this works? Prevention text displayed correctly — yes/no. Technical check: **Prevention text displayed** correctly
- [ ] Can I confirm this works? Null values handled gracefully (no crashes if fields are null) — yes/no. Technical check: **Null values handled** gracefully (no crashes if fields are null)
- [ ] Can I confirm this works? Default text shown for null fields (e.g., "No information available") — yes/no. Technical check: **Default text shown** for null fields (e.g., "No information available")

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

- [ ] Can I confirm this works? Turn off internet on phone — yes/no. Technical check: **Turn off internet** on phone
- [ ] Can I confirm this works? Attempt image upload by tapping button — yes/no. Technical check: **Attempt image upload** by tapping button
- [ ] Can I confirm this works? Error Toast appears with network error message — yes/no. Technical check: **Error Toast appears** with network error message
- [ ] Can I confirm this works? Toast message is user-friendly (mentions network/connection) — yes/no. Technical check: **Toast message is user-friendly** (mentions network/connection)
- [ ] Can I confirm this works? App does not crash — yes/no. Technical check: **App does not crash**
- [ ] Can I confirm this works? ProgressBar hidden after error — yes/no. Technical check: **ProgressBar hidden** after error
- [ ] Can I confirm this works? Button re-enabled after error — yes/no. Technical check: **Button re-enabled** after error
- [ ] Can I confirm this works? Can retry after turning internet back on — yes/no. Technical check: **Can retry after turning internet back on**

📸 **Screenshot Required:** Error Toast displayed.

### 10.2 Backend Down

**Setup:** Stop FastAPI server on laptop.

- [ ] Can I confirm this works? Stop backend server (Ctrl+C in terminal) — yes/no. Technical check: **Stop backend server** (Ctrl+C in terminal)
- [ ] Can I confirm this works? Attempt image upload from app — yes/no. Technical check: **Attempt image upload** from app
- [ ] Can I confirm this works? Error Toast appears (may say timeout or connection refused) — yes/no. Technical check: **Error Toast appears** (may say timeout or connection refused)
- [ ] Can I confirm this works? App does not crash — yes/no. Technical check: **App does not crash**
- [ ] Can I confirm this works? ProgressBar hidden after error — yes/no. Technical check: **ProgressBar hidden** after error
- [ ] Can I confirm this works? Button re-enabled after error — yes/no. Technical check: **Button re-enabled** after error
- [ ] Can I confirm this works? Can retry after restarting backend — yes/no. Technical check: **Can retry after restarting backend**

### 10.3 Wrong IP Address

**Setup:** Change BASE_URL to an invalid address (e.g., `http://10.0.2.3:8000/` or `http://192.168.1.99:8000/`).

- [ ] Can I confirm this works? Change BASE_URL in RetrofitClient — yes/no. Technical check: **Change BASE_URL** in RetrofitClient
- [ ] Can I confirm this works? Rebuild and install app — yes/no. Technical check: **Rebuild and install app**
- [ ] Can I confirm this works? Attempt upload — yes/no. Technical check: **Attempt upload**
- [ ] Can I confirm this works? Error Toast appears (connection timeout or failed to connect) — yes/no. Technical check: **Error Toast appears** (connection timeout or failed to connect)
- [ ] Can I confirm this works? App does not crash — yes/no. Technical check: **App does not crash**

### 10.4 No Image Captured

**Setup:** Launch app without capturing image.

- [ ] Can I confirm this works? Launch app fresh (without capturing image) — yes/no. Technical check: **Launch app fresh** (without capturing image)
- [ ] Can I confirm this works? "Detect Disease" button disabled (or validation prevents upload) — yes/no. Technical check: **"Detect Disease" button disabled** (or validation prevents upload)
- [ ] Can I confirm this works? If button enabled, tapping shows error message — yes/no. Technical check: **If button enabled, tapping shows error** message
- [ ] Can I confirm this works? App does not crash — yes/no. Technical check: **App does not crash**

**Pass Criteria:** All 19 error scenario items checked. App handles all error cases gracefully.

---

## Section 11: Code Quality

### 11.1 Code Organization

- [ ] Can I confirm this works? No hardcoded strings in Java code (use strings.xml) — yes/no. Technical check: **No hardcoded strings** in Java code (use strings.xml)
- [ ] Can I confirm this works? BASE_URL defined once in RetrofitClient only — yes/no. Technical check: **BASE_URL defined once** in RetrofitClient only
- [ ] Can I confirm this works? Proper null checks before accessing response.body() — yes/no. Technical check: **Proper null checks** before accessing response.body()
- [ ] Can I confirm this works? Try-catch blocks where appropriate (file operations, etc.) — yes/no. Technical check: **Try-catch blocks** where appropriate (file operations, etc.)
- [ ] Can I confirm this works? Meaningful variable names (no `temp`, `x`, `data1`) — yes/no. Technical check: **Meaningful variable names** (no `temp`, `x`, `data1`)

### 11.2 Imports and Structure

- [ ] Can I confirm this works? No unused imports in any class — yes/no. Technical check: **No unused imports** in any class
- [ ] Can I confirm this works? No compilation warnings in Android Studio — yes/no. Technical check: **No compilation warnings** in Android Studio
- [ ] Can I confirm this works? Package structure organized (classes in appropriate packages) — yes/no. Technical check: **Package structure organized** (classes in appropriate packages)

**Pass Criteria:** All 8 items checked. Code follows best practices.

---

## Section 12: Debugging and Logging

### 12.1 Logcat Output

- [ ] Can I confirm this works? HTTP requests visible in Logcat (filtered by "OkHttp") — yes/no. Technical check: **HTTP requests visible** in Logcat (filtered by "OkHttp")
- [ ] Can I confirm this works? Request method shown (POST /predict) — yes/no. Technical check: **Request method shown** (POST /predict)
- [ ] Can I confirm this works? Request body logged (multipart data) — yes/no. Technical check: **Request body logged** (multipart data)
- [ ] Can I confirm this works? Response status code shown (200 OK) — yes/no. Technical check: **Response status code shown** (200 OK)
- [ ] Can I confirm this works? Response JSON visible in Logcat — yes/no. Technical check: **Response JSON visible** in Logcat
- [ ] Can I confirm this works? Can read and understand network logs — yes/no. Technical check: **Can read and understand** network logs

**Test:** Upload image and observe Logcat during request.

📸 **Screenshot Required:** Logcat showing HTTP request and response.

**Pass Criteria:** All 6 items checked. Can debug using network logs.

---

## Section 13: Documentation and Evidence

### 13.1 Evidence Collection

- [ ] Can I confirm this works? Screenshot: MainActivity with image captured — yes/no. Technical check: **Screenshot: MainActivity with image** captured
- [ ] Can I confirm this works? Screenshot: ProgressBar visible during upload — yes/no. Technical check: **Screenshot: ProgressBar visible** during upload
- [ ] Can I confirm this works? Screenshot: ResultActivity with prediction displayed — yes/no. Technical check: **Screenshot: ResultActivity** with prediction displayed
- [ ] Can I confirm this works? Screenshot: Error Toast (network error scenario) — yes/no. Technical check: **Screenshot: Error Toast** (network error scenario)
- [ ] Can I confirm this works? Screenshot: Logcat showing HTTP request/response — yes/no. Technical check: **Screenshot: Logcat** showing HTTP request/response
- [ ] Can I confirm this works? All screenshots saved in `docs/evidence/week-05/` — yes/no. Technical check: **All screenshots saved** in `docs/evidence/week-05/`

### 13.2 Documentation

- [ ] Can I confirm this works? Week 05 reflection completed — yes/no. Technical check: **Week 05 reflection completed**
- [ ] Can I confirm this works? Week 05 quiz attempted — yes/no. Technical check: **Week 05 quiz attempted**
- [ ] Can I confirm this works? progress-tracker.md updated with Week 05 completion — yes/no. Technical check: **progress-tracker.md updated** with Week 05 completion
- [ ] Can I confirm this works? Git commits made with meaningful messages — yes/no. Technical check: **Git commits made** with meaningful messages
- [ ] Can I confirm this works? Can explain how Retrofit works in your own words — yes/no. Technical check: **Can explain how Retrofit works** in your own words

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

- [ ] Can I confirm this works? FastAPI backend running in terminal — yes/no. Technical check: **FastAPI backend running** in terminal
- [ ] Can I confirm this works? App running on phone or emulator — yes/no. Technical check: **App running on phone** or emulator
- [ ] Can I confirm this works? Complete upload flow (camera → upload → result) — yes/no. Technical check: **Complete upload flow** (camera → upload → result)
- [ ] Can I confirm this works? Error handling demo (stop backend, show error, restart, retry) — yes/no. Technical check: **Error handling demo** (stop backend, show error, restart, retry)
- [ ] Can I confirm this works? Logcat showing network requests — yes/no. Technical check: **Logcat showing network requests**
- [ ] Can I confirm this works? Can explain Retrofit architecture verbally — yes/no. Technical check: **Can explain Retrofit architecture** verbally
- [ ] Can I confirm this works? Can explain difference between onResponse() and onFailure() — yes/no. Technical check: **Can explain difference between onResponse() and onFailure()**
- [ ] Can I confirm this works? Can explain why async networking is required — yes/no. Technical check: **Can explain why async networking is required**

---

## Week 05 Validation Complete!

Once you pass all validation criteria:

- [ ] Can I confirm this works? Mark Week 05 as complete in progress-tracker.md — yes/no. Technical check: **Mark Week 05 as complete** in progress-tracker.md
- [ ] Can I confirm this works? Commit final version to Git — yes/no. Technical check: **Commit final version** to Git
- [ ] Can I confirm this works? Celebrate! You've built a network-enabled Android app! 🎉 — yes/no. Technical check: **Celebrate!** You've built a network-enabled Android app! 🎉
- [ ] Can I confirm this works? Ready for Week 06: Real ML model integration in FastAPI backend — yes/no. Technical check: **Ready for Week 06:** Real ML model integration in FastAPI backend

**Date Completed:** ___________

**Final Notes/Challenges Faced:**
_______________________________________________________________

---

**Next:** Open `roadmap/week-06-cloud-ml-model/README.md` to begin Week 06!


<!-- NAV_FOOTER_START -->

---

## 📚 Week 05 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| **5** | **validation-checklist.md** ← *You are here* | **Validation & Verification** |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Build Implementation Guide](build-task.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Validation & Verification** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Knowledge Assessment Quiz →](quiz.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 04: FastAPI Backend](../week-04-fastapi-backend/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 06: Cloud ML Model ➡](../week-06-cloud-ml-model/README.md) |

---
