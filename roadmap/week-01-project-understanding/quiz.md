# Week 01 Quiz: Project Understanding & Foundation

## Instructions

Answer all questions to verify your Week 01 understanding. This is self-assessment—be honest. Check your answers at the end.

**Scoring:**
- 10-12 correct: Excellent understanding, ready for Week 02
- 7-9 correct: Good understanding, review missed topics
- 4-6 correct: Needs improvement, re-read learning notes
- 0-3 correct: Must review all Week 01 materials before proceeding

**Time limit:** 20 minutes (if timed)

---

## Section 1: Multiple Choice (1 point each)

### Question 1
What is the primary problem that LeafGuard AI solves?

A) Farmers cannot afford smartphones
B) Plant diseases are rare and hard to find
C) Farmers lack timely access to plant disease experts
D) Machine learning is too complex for agriculture

**Your Answer:** ____

---

### Question 2
Which architecture pattern does LeafGuard AI use?

A) MVC (Model-View-Controller)
B) MVP (Model-View-Presenter)
C) MVVM (Model-View-ViewModel)
D) VIPER (View-Interactor-Presenter-Entity-Router)

**Your Answer:** ____

---

### Question 3
How many main activities does LeafGuard AI have (minimum)?

A) 3 activities
B) 6 activities
C) 10 activities
D) 1 activity with multiple fragments

**Your Answer:** ____

---

### Question 4
What is the purpose of the Repository layer in MVVM?

A) Display UI to users
B) Handle user interactions
C) Abstract data sources and coordinate data access
D) Store local database tables

**Your Answer:** ____

---

### Question 5
Which library does LeafGuard use for REST API communication?

A) Volley
B) OkHttp only
C) Retrofit
D) HttpURLConnection

**Your Answer:** ____

---

### Question 6
What database solution does LeafGuard use for local storage?

A) SQLite directly
B) Room Database
C) Realm
D) SharedPreferences

**Your Answer:** ____

---

### Question 7
What format is used to store disease information locally?

A) JSON files
B) XML files
C) CSV files
D) Binary files

**Your Answer:** ____

---

### Question 8
What is the backend framework used in LeafGuard AI?

A) Django
B) Flask
C) FastAPI
D) Express.js

**Your Answer:** ____

---

### Question 9
Which machine learning framework is used for on-device inference?

A) TensorFlow Full
B) TensorFlow Lite
C) PyTorch Mobile
D) Core ML

**Your Answer:** ____

---

### Question 10
What is the main benefit of using ViewModel over putting logic in Activity?

A) ViewModel has better performance
B) ViewModel survives configuration changes (like screen rotation)
C) ViewModel uses less memory
D) ViewModel is easier to write

**Your Answer:** ____

---

## Section 2: True/False (1 point each)

### Question 11
LeafGuard AI works only in online mode and requires constant internet connection.

**True** or **False**?

**Your Answer:** ____

---

### Question 12
The three tiers in three-tier architecture are: Presentation, Business Logic, and Data.

**True** or **False**?

**Your Answer:** ____

---

### Question 13
In MVVM, Activities should directly access the database using DAO objects.

**True** or **False**?

**Your Answer:** ____

---

### Question 14
Retrofit automatically converts JSON responses to Java objects using Gson or Moshi.

**True** or **False**?

**Your Answer:** ____

---

### Question 15
Room Database provides compile-time verification of SQL queries.

**True** or **False**?

**Your Answer:** ____

---

## Section 3: Fill in the Blank (1 point each)

### Question 16
LeafGuard AI uses _____________ pattern to display lists of scan history efficiently.

**Your Answer:** ____________________

---

### Question 17
The _____________ annotation in Room marks a class as a database table.

**Your Answer:** ____________________

---

### Question 18
In Retrofit, the @POST annotation indicates an HTTP _____________ request.

**Your Answer:** ____________________

---

### Question 19
LeafGuard AI has two AI modes: cloud AI and _____________ AI.

**Your Answer:** ____________________

---

### Question 20
The _____________ holds UI state and survives configuration changes in MVVM.

**Your Answer:** ____________________

---

## Section 4: Short Answer (2 points each)

### Question 21
List the 6 main activities in LeafGuard AI.

**Your Answer:**
1. ____________________
2. ____________________
3. ____________________
4. ____________________
5. ____________________
6. ____________________

---

### Question 22
Explain in 2-3 sentences why LeafGuard uses MVVM architecture instead of putting all logic in Activities.

**Your Answer:**
_________________________________________________________________
_________________________________________________________________
_________________________________________________________________

---

### Question 23
Describe the data flow when a user scans a leaf in cloud AI mode. List at least 6 steps from button click to result display.

**Your Answer:**
1. ____________________
2. ____________________
3. ____________________
4. ____________________
5. ____________________
6. ____________________

---

### Question 24
What are THREE benefits of using Room Database instead of raw SQLite in Android?

**Your Answer:**
1. ____________________
2. ____________________
3. ____________________

---

### Question 25
Name THREE CSE 2206 syllabus topics that LeafGuard AI demonstrates, and briefly explain how.

**Your Answer:**

**Topic 1:** ____________________
**How:** ___________________________________________________________

**Topic 2:** ____________________
**How:** ___________________________________________________________

**Topic 3:** ____________________
**How:** ___________________________________________________________

---

## Section 5: Diagram Interpretation

### Question 26 (3 points)
Study this simplified architecture diagram:

```
┌─────────────────┐
│   MainActivity  │
│   (View)        │
└────────┬────────┘
         │ calls
┌────────▼────────┐
│   ApiService    │
│   (Network)     │
└────────┬────────┘
         │ HTTP
┌────────▼────────┐
│  FastAPI Backend│
│  /predict       │
└────────┬────────┘
         │ ML
┌────────▼────────┐
│ TensorFlow Model│
└─────────────────┘
```

Answer these questions:

**A) Which layer does MainActivity belong to?**
_________________________________________________________________

**B) What does ApiService do in LeafGuard?**
_________________________________________________________________

**C) Why does MainActivity use ApiService instead of making HTTP calls directly?**
_________________________________________________________________
_________________________________________________________________

---

## Section 6: Code Reading

### Question 27 (3 points)
Read this code snippet:

```kotlin
// MainActivity.kt
class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    private fun uploadImage(imagePath: String) {
        lifecycleScope.launch {
            try {
                val response = apiService.predict(createImagePart(imagePath))
                navigateToResult(response.disease, response.confidence)
            } catch (e: Exception) {
                showError("Network error: ${e.message}")
            }
        }
    }

    private fun navigateToResult(disease: String, confidence: Float) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("disease", disease)
        intent.putExtra("confidence", confidence)
        startActivity(intent)
    }
}
```

**A) What is the purpose of lifecycleScope.launch in this code?**
_________________________________________________________________

**B) What happens if the network call fails?**
_________________________________________________________________

**C) How does the result get to ResultActivity?**
_________________________________________________________________

---

## Section 7: Scenario-Based Questions

### Question 28 (3 points)
**Scenario:** A user opens LeafGuard AI, captures a leaf image, and then rotates their phone screen while waiting for the disease prediction result.

**Question:** What happens to the network call and the result? Will the user see the result after rotation, or will it be lost? Explain why.

**Your Answer:**
_________________________________________________________________
_________________________________________________________________
_________________________________________________________________

---

### Question 29 (3 points)
**Scenario:** You are in Week 07 implementing Room database. A classmate suggests: "Why use Room? Let's just use SharedPreferences to save scan history as JSON strings."

**Question:** Explain THREE reasons why this is a bad idea and why Room is better for storing scan history.

**Your Answer:**
1. ____________________________________________________________________
2. ____________________________________________________________________
3. ____________________________________________________________________

---

### Question 30 (3 points)
**Scenario:** During Week 11 testing, you discover that the app crashes when users have no internet connection and try to use cloud AI mode.

**Question:** What architectural component should handle this error? Explain where and how you would implement the error handling.

**Your Answer:**
_________________________________________________________________
_________________________________________________________________
_________________________________________________________________

---

## Answer Key

### Section 1: Multiple Choice
1. C (Farmers lack timely access to plant disease experts)
2. C (Direct architecture - Activities call services directly)
3. B (6 activities: Main, Result, History, HistoryDetail, Library, Settings)
4. C (Abstract data sources and coordinate data access)
5. C (Retrofit)
6. B (Room Database)
7. B (XML files - diseases.xml)
8. C (FastAPI)
9. B (TensorFlow Lite)
10. B (Service classes provide abstraction and reusability)

### Section 2: True/False
11. False (LeafGuard has both online and offline modes)
12. True
13. False (Activities access database through service classes like ScanDao)
14. True
15. True

### Section 3: Fill in the Blank
16. RecyclerView (or Adapter pattern)
17. @Entity
18. POST
19. offline (or on-device)
20. ApiService (or ScanDao)

### Section 4: Short Answer

**21. Six main activities:**
1. MainActivity
2. ResultActivity
3. HistoryActivity
4. HistoryDetailActivity
5. DiseaseLibraryActivity
6. SettingsActivity

**22. Why direct architecture? (Sample answer)**
LeafGuard uses a direct architecture because it's simpler to understand and debug for a course project. Activities directly call service classes (ApiService, ScanDao, TFLiteClassifier) which keeps the codebase straightforward. This separation still provides reusability and testability while avoiding the complexity of additional abstraction layers.

**23. Data flow in cloud AI mode (Sample answer):**
1. User taps "Scan" button in MainActivity
2. Camera intent launched, user captures image
3. Image returned to MainActivity via ActivityResultContracts
4. MainActivity calls ApiService.predict() with image
5. ApiService uses Retrofit to POST image to FastAPI backend
6. Backend runs TensorFlow model inference
7. Backend returns JSON with "disease" field
8. Retrofit parses JSON to PredictionResponse object
9. MainActivity receives result
10. MainActivity saves result to Room database via ScanDao
11. MainActivity navigates to ResultActivity with Intent extras

**24. Three benefits of Room over SQLite (Sample answer):**
1. Compile-time verification of SQL queries (catches errors before runtime)
2. Less boilerplate code (no need to manually write ContentValues and Cursors)
3. Coroutines integration for automatic background threading

**25. Three CSE 2206 topics (Sample answer):**

**Topic 1:** Activities
**How:** LeafGuard implements 6 activities with proper lifecycle management (onCreate, onPause, onResume) and state saving/restoration.

**Topic 2:** Retrofit (Networking)
**How:** LeafGuard uses Retrofit to make HTTP POST requests to FastAPI backend, demonstrating REST API communication, JSON parsing, and coroutines for async operations.

**Topic 3:** Room Database
**How:** LeafGuard implements Room with @Entity classes (ScanRecord), @Dao interfaces (ScanDao), and @Database class (AppDatabase) for local data persistence.

### Section 5: Diagram Interpretation

**26A) Which layer does MainActivity belong to?**
Presentation Layer (or View layer)

**26B) What does ApiService do in LeafGuard?**
ApiService defines the Retrofit interface for network calls to the FastAPI backend, including the POST /predict endpoint for disease detection.

**26C) Why does MainActivity use ApiService instead of making HTTP calls directly?**
ApiService provides abstraction and reusability. It encapsulates all network logic in one place, making it easier to test, modify, and maintain. Multiple Activities can reuse the same ApiService. If the API changes, only ApiService needs updating, not every Activity.

### Section 6: Code Reading

**27A) Purpose of lifecycleScope.launch:**
lifecycleScope.launch runs the network call on a coroutine that's tied to the Activity's lifecycle. This ensures the operation is automatically cancelled if the Activity is destroyed, preventing memory leaks and crashes.

**27B) What happens if the network call fails?**
The catch block handles the exception and calls showError() to display an error message to the user with the exception details.

**27C) How does the result get to ResultActivity?**
```kotlin
val intent = Intent(this, ResultActivity::class.java)
intent.putExtra("disease", disease)
intent.putExtra("confidence", confidence)
startActivity(intent)
    if (scanResult.isSuccess()) {
        textView.setText(scanResult.getData().getDiseaseName());
    } else {
        textView.setText("Error: " + scanResult.getErrorMessage());
    }
});
```

### Section 7: Scenario-Based Questions

**28. Screen rotation scenario (Sample answer):**
The network call continues in the background because it's initiated in the ViewModel, which survives configuration changes. When the Activity is recreated after rotation, it re-observes the same ViewModel. If the result arrives after rotation, the new Activity instance will receive it through LiveData observation and display it normally. If the result arrived before rotation, the LiveData will immediately deliver the last value to the new Activity. The user will see the result either way—no data is lost. This is the key benefit of MVVM architecture.

**29. Why Room over SharedPreferences for scan history (Sample answer):**
1. **Structured queries:** Room allows SQL queries like "get all scans from last week" or "find scans with confidence >90%". SharedPreferences would require loading all data and filtering in Java, which is inefficient.
2. **Data relationships:** Room supports foreign keys and table relationships (e.g., scans belonging to users). SharedPreferences cannot enforce referential integrity.
3. **Performance:** Room is optimized for large datasets with indexing and lazy loading. SharedPreferences would load entire JSON string into memory every time, causing performance issues with 100+ scans.

**30. Network error handling (Sample answer):**
The error should be handled in the Repository layer. When Retrofit's onFailure callback is triggered, the Repository should check if it's a network error (IOException) and return an appropriate error result to the ViewModel. The ViewModel then updates LiveData with an error state, which the Activity observes and displays to the user (e.g., "No internet connection. Please check your network and try again."). Additionally, the Repository could check network connectivity before making the API call using ConnectivityManager and provide immediate feedback. The Activity should never directly handle network errors—it should only display error messages provided by the ViewModel.

---

## Scoring Guide

**Total Points Possible: 40**

- Section 1 (MC): 10 points
- Section 2 (T/F): 5 points
- Section 3 (Fill): 5 points
- Section 4 (Short): 10 points
- Section 5 (Diagram): 3 points
- Section 6 (Code): 3 points
- Section 7 (Scenario): 9 points (3 points × 3 questions)

**Your Score: _____ / 40**

**Percentage: _____ %**

**Grade:**
- 36-40 (90-100%): Excellent - Proceed to Week 02 with confidence
- 32-35 (80-89%): Good - Review missed concepts, then proceed
- 28-31 (70-79%): Satisfactory - Review Week 01 learning notes before proceeding
- 24-27 (60-69%): Needs Improvement - Re-study architecture and MVVM sections
- Below 24 (<60%): Must Review - Complete learning notes again before Week 02

---

## Post-Quiz Actions

### If you scored 90%+ (Excellent):
1. ✅ You're ready for Week 02
2. Help classmates who scored lower
3. Review any questions you missed to solidify understanding
4. Proceed with confidence

### If you scored 70-89% (Good/Satisfactory):
1. Review topics where you lost points
2. Re-read relevant sections in learning-notes.md
3. Redraw architecture diagram to solidify understanding
4. Retake quiz after review (optional)
5. Proceed to Week 02, but watch for these weak areas

### If you scored below 70% (Needs Improvement):
1. ⚠️ Do NOT proceed to Week 02 yet
2. Re-read all Week 01 learning-notes.md
3. Watch supplementary videos on MVVM, Room, Retrofit
4. Redraw architecture and data flow diagrams
5. Retake this quiz
6. Only proceed when you score 70%+

---

## Reflection Question

**Beyond the score, what does this quiz reveal about your understanding? Where do you need to focus more attention?**

**Your Reflection:**
_________________________________________________________________
_________________________________________________________________
_________________________________________________________________

---

**Quiz completed on:** _______________

**Time taken:** _______________ minutes

**Ready for Week 02?** Yes / No (circle one)

---

**Remember: Understanding beats memorization. If you can explain these concepts to someone else, you truly understand them. Good luck with Week 02!**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 01 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| **6** | **quiz.md** ← *You are here* | **Knowledge Assessment Quiz** |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Validation & Verification](validation-checklist.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Knowledge Assessment Quiz** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Reflection & Consolidation →](reflection.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| *(First week — no previous)* | [Learning Path](../../LEARNING_PATH.md) | [Week 02: Android Basics & UI ➡](../week-02-android-basics-ui/README.md) |

---
