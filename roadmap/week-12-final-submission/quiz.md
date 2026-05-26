# Week 12: Final Submission - Quiz

**Instructions:** This quiz tests your comprehensive understanding of the entire LeafGuard AI project and CSE 2206 concepts. Answer all questions honestly—this is your final self-assessment.

**Total Questions: 30**
**Passing Score: 24/30 (80%)**

---

## Section A: Android Fundamentals (10 questions)

### Question 1
**What is the correct order of Activity lifecycle methods when an app starts?**

A) `onStart()` → `onCreate()` → `onResume()`
B) `onCreate()` → `onStart()` → `onResume()`
C) `onCreate()` → `onResume()` → `onStart()`
D) `onResume()` → `onCreate()` → `onStart()`

**Your answer:** ____

**Explanation:** _______________________________________

---

### Question 2
**Which intent type did you use for the share functionality?**

A) `Intent.ACTION_VIEW`
B) `Intent.ACTION_SEND`
C) `Intent.ACTION_PICK`
D) `Intent.ACTION_DIAL`

**Your answer:** ____

**Explanation:** _______________________________________

---

### Question 3
**What annotation marks a class as a Room database entity?**

A) `@Database`
B) `@Entity`
C) `@Table`
D) `@Model`

**Your answer:** ____

**Code example from your project:**
```java
[Paste your Entity class snippet here]
```

---

### Question 4
**Which layout did you use for your main UI?**

A) `LinearLayout`
B) `RelativeLayout`
C) `ConstraintLayout`
D) `FrameLayout`

**Your answer:** ____

**Why did you choose this layout?**
```
[Explain your choice]
```

---

### Question 5
**What is the purpose of `@PrimaryKey(autoGenerate = true)` in Room?**

A) It creates a unique identifier automatically
B) It generates random numbers
C) It creates a foreign key
D) It generates database schema

**Your answer:** ____

**Explanation:** _______________________________________

---

### Question 6
**How do you request camera permission at runtime?**

A) In `AndroidManifest.xml` only
B) Using `ActivityCompat.requestPermissions()`
C) Using `startActivityForResult()`
D) Using `PermissionManager.request()`

**Your answer:** ____

**Show your permission request code:**
```java
[Paste your permission request code]
```

---

### Question 7
**What thread should database operations run on?**

A) Main/UI thread
B) Background thread
C) Network thread
D) Any thread

**Your answer:** ____

**How did you implement this?**
```
[Explain your threading approach using ExecutorService]
```

---

### Question 8
**What is the ViewHolder pattern used for in RecyclerView?**

A) To hold the RecyclerView itself
B) To cache view references for reuse
C) To store database data
D) To manage layout inflation

**Your answer:** ____

**Show your ViewHolder code:**
```java
[Paste your ViewHolder class]
```

---

### Question 9
**Which method converts an Activity to bytes for network transmission?**

A) `toString()`
B) `serialize()`
C) No such method exists
D) `toByteArray()`

**Your answer:** ____

**Trick question alert! Explain what's wrong with this question:**
```
[Explain the conceptual error in the question]
```

---

### Question 10
**What is the purpose of Notification Channels in Android 8.0+?**

A) To group notifications by type
B) To enable sound for notifications
C) To allow users to customize notification behavior
D) Both A and C

**Your answer:** ____

**Show your NotificationChannel code:**
```java
[Paste your notification channel creation code]
```

---

## Section B: Room Database (5 questions)

### Question 11
**What are the three main components of Room?**

A) Entity, DAO, Database
B) Table, Query, Connection
C) Model, View, Controller
D) Activity, Fragment, Service

**Your answer:** ____

**Map these to your project:**
```
Entity: [Your entity class name]
DAO: [Your DAO interface name]
Database: [Your database class name]
```

---

### Question 12
**What annotation is used for database queries in DAO?**

A) `@Select`
B) `@Query`
C) `@Fetch`
D) `@Get`

**Your answer:** ____

**Show one of your DAO queries:**
```java
@Query("[Your SQL query here]")
[Method signature]
```

---

### Question 13
**How do you get a Database instance in Room?**

A) `new RoomDatabase()`
B) `Room.createDatabase()`
C) `Room.databaseBuilder()`
D) `RoomDatabase.getInstance()`

**Your answer:** ____

**Show your database instantiation code:**
```java
[Paste your database builder code]
```

---

### Question 14
**What happens if you perform database operations on the main thread?**

A) It works fine
B) App crashes with `NetworkOnMainThreadException`
C) App crashes with `IllegalStateException`
D) Performance issues but no crash

**Your answer:** ____

**How did you avoid this issue?**
```
[Explain your solution using ExecutorService or Kotlin Coroutines]
```

---

### Question 15
**What SQL operation does `@Insert` annotation represent?**

A) SELECT
B) INSERT
C) UPDATE
D) DELETE

**Your answer:** ____

**Show your @Insert method:**
```java
@Insert
[Method signature]
```

---

## Section C: Networking and API (5 questions)

### Question 16
**What library did you use for network requests?**

A) Volley
B) Retrofit
C) OkHttp
D) HttpURLConnection

**Your answer:** ____

**Why did you choose this library?**
```
[Explain advantages of Retrofit]
```

---

### Question 17
**What annotation marks a POST request in Retrofit?**

A) `@POST`
B) `@HttpPost`
C) `@Request`
D) `@Send`

**Your answer:** ____

**Show your POST endpoint:**
```java
@POST("[Your endpoint]")
Call<[YourResponseType]> [methodName]([Parameters]);
```

---

### Question 18
**What format did your API return predictions in?**

A) XML
B) JSON
C) CSV
D) Plain text

**Your answer:** ____

**Show your response model class:**
```java
[Paste your response POJO/data class]
```

---

### Question 19
**How do you handle network timeouts in Retrofit?**

A) Using `OkHttpClient` with timeout settings
B) Using `Thread.sleep()`
C) Using `setTimeout()`
D) Timeouts can't be configured

**Your answer:** ____

**Show your timeout configuration:**
```java
[Paste your OkHttpClient configuration with timeouts]
```

---

### Question 20
**What does `@Multipart` annotation enable?**

A) Multiple API calls at once
B) File upload with form data
C) Multiple response types
D) Concurrent network requests

**Your answer:** ____

**Show your multipart upload code:**
```java
[Paste your image upload method signature]
```

---

## Section D: XML Parsing (3 questions)

### Question 21
**Which XML parsing method did you use?**

A) DOM Parser
B) SAX Parser
C) XmlPullParser
D) JSON Parser (trick question!)

**Your answer:** ____

**Why this choice?**
```
[Explain advantages of XmlPullParser for Android]
```

---

### Question 22
**What event type indicates the start of an XML tag?**

A) `START_TAG`
B) `BEGIN_TAG`
C) `OPEN_TAG`
D) `TAG_START`

**Your answer:** ____

**Show your parsing loop:**
```java
[Paste your XmlPullParser loop structure]
```

---

### Question 23
**What method advances XmlPullParser to the next event?**

A) `next()`
B) `advance()`
C) `moveNext()`
D) `getNext()`

**Your answer:** ____

**Show complete parsing snippet:**
```java
[Paste a section of your XML parsing code]
```

---

## Section E: TensorFlow Lite (4 questions)

### Question 24
**What TensorFlow Lite class runs model inference?**

A) `Classifier`
B) `Interpreter`
C) `Model`
D) `TFLite`

**Your answer:** ____

**Show your Interpreter initialization:**
```java
[Paste your Interpreter creation code]
```

---

### Question 25
**What input format does your model expect?**

A) Float array
B) ByteBuffer
C) Bitmap
D) String

**Your answer:** ____

**Show your preprocessing code:**
```java
[Paste your image preprocessing method]
```

---

### Question 26
**Where did you place the .tflite model file?**

A) `res/raw/`
B) `assets/`
C) `drawable/`
D) `values/`

**Your answer:** ____

**Show your model loading code:**
```java
[Paste your loadModelFile() method]
```

---

### Question 27
**What is model quantization?**

A) Reducing model size by using lower precision
B) Increasing model accuracy
C) Converting model to XML
D) Encrypting model weights

**Your answer:** ____

**Did you use quantization? Explain:**
```
[Explain if/how you used quantization and the trade-offs]
```

---

## Section F: Final Deliverables (3 questions)

### Question 28
**What keystore algorithm did you use for APK signing?**

A) RSA
B) AES
C) DSA
D) SHA-256

**Your answer:** ____

**What's the keystore validity you set?**
```
Years: ____
Why this duration: __________
```

---

### Question 29
**How many pages should the final report be?**

A) 10-15 pages
B) 20-30 pages
C) 30-40 pages
D) As many as needed

**Your answer:** ____

**Your actual report page count:** ____

---

### Question 30
**What should be the demo video length?**

A) 2-3 minutes
B) 5-10 minutes
C) 15-20 minutes
D) As long as needed

**Your answer:** ____

**Your actual video length:** ____ minutes

---

## Section G: Comprehensive Understanding (Short Answer)

### Question 31: System Architecture
**Draw and explain your system architecture:**

```
[Draw a simple text-based architecture diagram]

Example:
[User] → [Android App] → [FastAPI Server] → [TensorFlow Model]
             ↓
       [Room Database]
       [TFLite Model]
       [XML Library]

Explanation:
[Explain how components interact]
```

---

### Question 32: Data Flow
**Trace the data flow for a cloud prediction:**

```
Step 1: [User captures image]
Step 2: [________________________]
Step 3: [________________________]
Step 4: [________________________]
Step 5: [________________________]
Step 6: [________________________]
```

---

### Question 33: Error Scenarios
**How does your app handle these errors?**

**No internet connection:**
```
[Explain your error handling]
```

**Camera permission denied:**
```
[Explain your error handling]
```

**Model inference fails:**
```
[Explain your error handling]
```

---

### Question 34: Technology Justification
**Why did you choose these technologies?**

**Room over raw SQLite:**
```
[Justify choice]
```

**Retrofit over HttpURLConnection:**
```
[Justify choice]
```

**TFLite over server-only model:**
```
[Justify choice]
```

---

### Question 35: CSE 2206 Mapping
**Map each CSE 2206 topic to a specific feature in your app:**

| CSE 2206 Topic | Your Implementation | File/Class Name |
|----------------|---------------------|-----------------|
| Activities | [Example: Main screen] | MainActivity.java |
| Intents | | |
| Layouts | | |
| Data Storage | | |
| Networking | | |
| XML Parsing | | |
| Permissions | | |
| Notifications | | |

---

## Answer Key (For Self-Assessment)

### Section A Answers:
1. B - onCreate → onStart → onResume
2. B - Intent.ACTION_SEND
3. B - @Entity
4. C - ConstraintLayout (most likely)
5. A - Creates unique identifier automatically
6. B - ActivityCompat.requestPermissions()
7. B - Background thread
8. B - Cache view references for reuse
9. C - No such method exists (Activities aren't serialized this way)
10. D - Both A and C

### Section B Answers:
11. A - Entity, DAO, Database
12. B - @Query
13. C - Room.databaseBuilder()
14. C - IllegalStateException
15. B - INSERT

### Section C Answers:
16. B - Retrofit
17. A - @POST
18. B - JSON
19. A - OkHttpClient with timeout settings
20. B - File upload with form data

### Section D Answers:
21. C - XmlPullParser
22. A - START_TAG
23. A - next()

### Section E Answers:
24. B - Interpreter
25. B - ByteBuffer
26. B - assets/
27. A - Reducing model size

### Section F Answers:
28. A - RSA
29. B - 20-30 pages
30. B - 5-10 minutes

---

## Scoring

**Your Score: ____/30**

**Grade:**
- 27-30: Excellent (90-100%) - Outstanding understanding
- 24-26: Very Good (80-89%) - Strong understanding
- 21-23: Good (70-79%) - Solid understanding with some gaps
- 18-20: Satisfactory (60-69%) - Basic understanding, needs review
- Below 18: Needs Work (<60%) - Significant review required

---

## Quiz Reflection

**Questions I got wrong:**
1. _______________________________________
2. _______________________________________
3. _______________________________________

**Why I got them wrong:**
```
[Reflect on your misunderstandings]
```

**Topics to review:**
- [ ] _______________________________________
- [ ] _______________________________________
- [ ] _______________________________________

**Action plan:**
```
[How you'll strengthen weak areas before viva]
```

---

## Viva Confidence Rating

After completing this quiz, rate your viva readiness (1-10): ____/10

**If below 8, what will you do to improve?**
```
[Your improvement plan]
```

---

**Congratulations on completing the Week 12 quiz! Use this assessment to identify areas for final review before your viva. Good luck! 🎓**
