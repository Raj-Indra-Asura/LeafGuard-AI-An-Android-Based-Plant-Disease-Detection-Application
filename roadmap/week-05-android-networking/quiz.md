# Week 05: Quiz - Android Networking with Retrofit

## Instructions

- **Total Questions:** 30 (15 conceptual + 15 practical)
- **Time Limit:** 45 minutes (recommended)
- **Passing Score:** 24/30 (80%)
- **Open Book:** You may reference your code and notes
- **Purpose:** Test understanding before moving to Week 06

**Answer Format:**
- Multiple choice: Write the letter (A, B, C, or D)
- True/False: Write T or F
- Short answer: Write 1-3 sentences

---

## Part A: Conceptual Questions (15 questions)

### Question 1
**What is the primary purpose of the Retrofit library in Android?**

A) To compress images before uploading
B) To simplify HTTP networking with type-safe API definitions
C) To store data in local databases
D) To handle camera permissions

Your answer: _____

---

### Question 2
**True or False: Network calls can be made on the main thread in Android without any issues.**

Your answer: _____

Explanation (if False): _______________________________________________

---

### Question 3
**Which HTTP method does LeafGuard AI use to upload images to the backend?**

A) GET
B) POST
C) PUT
D) DELETE

Your answer: _____

---

### Question 4
**What is the role of Gson in Retrofit?**

A) To make HTTP requests
B) To convert JSON responses into Java objects automatically
C) To compress images
D) To handle UI rendering

Your answer: _____

---

### Question 5
**What does the @Multipart annotation indicate in Retrofit?**

A) The request will have multiple response objects
B) The request body uses multipart/form-data encoding
C) Multiple API endpoints will be called
D) The response will be split into multiple parts

Your answer: _____

---

### Question 6
**True or False: The enqueue() method in Retrofit is synchronous and blocks the calling thread.**

Your answer: _____

Explanation: _______________________________________________

---

### Question 7
**Which callback method is called when the HTTP request completes successfully?**

A) onSuccess()
B) onComplete()
C) onResponse()
D) onResult()

Your answer: _____

---

### Question 8
**What does a 404 status code indicate?**

A) Success
B) Server error
C) Endpoint not found
D) Unauthorized access

Your answer: _____

---

### Question 9
**True or False: Android 9+ blocks HTTP (cleartext) traffic by default.**

Your answer: _____

What configuration allows HTTP to local IPs: _______________________________________________

---

### Question 10
**What is the purpose of the singleton pattern in RetrofitClient?**

A) To create multiple Retrofit instances for better performance
B) To ensure only one Retrofit instance is created and reused
C) To allow multiple BASE_URLs
D) To encrypt network traffic

Your answer: _____

---

### Question 11
**Which of the following is NOT a valid HTTP status code range?**

A) 2xx (Success)
B) 3xx (Redirection)
C) 4xx (Client Error)
D) 6xx (Network Error)

Your answer: _____

---

### Question 12
**What happens if response.body() is null and you try to access its methods?**

A) Returns empty string
B) Returns default values
C) NullPointerException (app crashes)
D) Network request retried automatically

Your answer: _____

---

### Question 13
**True or False: The onFailure() callback is only called for network connectivity errors like IOException.**

Your answer: _____

Explanation: _______________________________________________

---

### Question 14
**What does the Content-Type header "multipart/form-data" indicate?**

A) The response is in multiple formats
B) The request body contains both form fields and file data
C) Multiple servers will handle the request
D) The data is compressed

Your answer: _____

---

### Question 15
**Why is a ProgressBar shown during network operations?**

A) To make the app look professional
B) To indicate to the user that work is happening in the background
C) To test UI components
D) Required by Android guidelines

Your answer: _____

---

## Part B: Practical / Code-Based Questions (15 questions)

### Question 16
**Identify the error in this code:**

```java
@POST("predict")
Call<PredictionResponse> uploadImage(MultipartBody.Part image);
```

A) Missing @Multipart annotation
B) Wrong return type
C) Parameter should use @Body instead
D) No error

Your answer: _____

---

### Question 17
**What is wrong with this BASE_URL?**

```java
private static final String BASE_URL = "http://192.168.1.10:8000";
```

A) Should use HTTPS
B) Missing trailing forward slash (/)
C) Port number is wrong
D) No error

Your answer: _____

---

### Question 18
**Complete the missing line:**

```java
RequestBody requestBody = RequestBody.create(
    MediaType.parse("image/*"),
    imageFile
);

MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
    "image",
    imageFile.getName(),
    _______________  // What goes here?
);
```

Your answer: _______________________________________________

---

### Question 19
**Which line should be used for asynchronous network calls?**

A) `call.execute();`
B) `call.enqueue(callback);`
C) `call.start();`
D) `call.run();`

Your answer: _____

---

### Question 20
**True or False: This code correctly checks for successful response:**

```java
if (response.body() != null) {
    PredictionResponse prediction = response.body();
    // use prediction
}
```

Your answer: _____

What's missing: _______________________________________________

---

### Question 21
**Fix this error-handling code:**

```java
@Override
public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
    PredictionResponse prediction = response.body();
    String disease = prediction.getDisease();
    textView.setText(disease);
}
```

What are TWO problems with this code?

1. _______________________________________________
2. _______________________________________________

---

### Question 22
**What permissions must be in AndroidManifest.xml for networking?**

A) CAMERA and INTERNET
B) INTERNET only
C) INTERNET and READ_EXTERNAL_STORAGE
D) No permissions required

Your answer: _____

---

### Question 23
**Match the Retrofit annotation with its purpose:**

| Annotation | Purpose |
|------------|---------|
| 1. @GET | A. Path parameter |
| 2. @POST | B. Query parameter |
| 3. @Path | C. HTTP GET request |
| 4. @Query | D. HTTP POST request |

Your answer: 1-___, 2-___, 3-___, 4-___

---

### Question 24
**When does the onFailure() callback get called?**

Select all that apply:

A) Network connectivity error (IOException)
B) Timeout exception
C) HTTP 404 error
D) HTTP 500 error
E) Parsing error

Your answer: _______________________________________________

---

### Question 25
**Complete the GsonConverterFactory configuration:**

```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl(BASE_URL)
    .___________________  // What goes here?
    .build();
```

Your answer: _______________________________________________

---

### Question 26
**What does this configuration do?**

```java
OkHttpClient client = new OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .build();
```

A) Limits response size to 30 seconds of data
B) Waits up to 30 seconds to establish connection before timing out
C) Sends request every 30 seconds
D) Keeps connection alive for 30 seconds

Your answer: _____

---

### Question 27
**True or False: After an upload completes (success or failure), the ProgressBar should remain visible.**

Your answer: _____

Correct action: _______________________________________________

---

### Question 28
**How do you format confidence as a percentage in ResultActivity?**

Given: `double confidence = 0.8734;`

A) `String result = confidence + "%";`
B) `String result = String.valueOf(confidence * 100) + "%";`
C) `String result = String.format("%.1f%%", confidence * 100);`
D) `String result = String.format("%.1f", confidence);`

Your answer: _____

---

### Question 29
**Identify all errors in this RetrofitClient:**

```java
public class RetrofitClient {
    private static final String BASE_URL = "http://localhost:8000/";

    public Retrofit getClient() {
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build();
    }
}
```

List at least 3 errors:

1. _______________________________________________
2. _______________________________________________
3. _______________________________________________

---

### Question 30
**What happens when you call this code multiple times?**

```java
public static Retrofit getClient() {
    if (retrofit == null) {
        retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
    return retrofit;
}
```

A) Creates new Retrofit instance each time
B) Returns same instance after first creation
C) Returns null every time
D) Throws exception after first call

Your answer: _____

Why is this beneficial: _______________________________________________

---

## Answers and Scoring

### Part A: Conceptual Answers

1. B
2. F - Network calls block the main thread causing UI freeze and potentially ANR
3. B
4. B
5. B
6. F - enqueue() is asynchronous and does not block
7. C
8. C
9. T - Network security configuration or HTTPS required
10. B
11. D
12. C
13. T - onFailure() is for network-level failures, not HTTP errors
14. B
15. B

### Part B: Practical Answers

16. A - Missing @Multipart annotation
17. B - BASE_URL should end with "/"
18. `requestBody`
19. B
20. F - Should also check response.isSuccessful() first
21. 1) Not checking response.isSuccessful() 2) Not checking if response.body() is null
22. B
23. 1-C, 2-D, 3-A, 4-B
24. A, B, E - (not C or D, those trigger onResponse with error codes)
25. `addConverterFactory(GsonConverterFactory.create())`
26. B
27. F - Should hide ProgressBar (setVisibility(View.GONE))
28. C
29. 1) localhost won't work from phone (use actual IP), 2) Missing Gson converter, 3) Not singleton (method not static), 4) No null check for singleton
30. B - Singleton pattern reuses the same instance, saving resources

---

## Scoring Guide

**Your Score:** _____ / 30

**Percentage:** _____ %

### Score Interpretation

- **27-30 (90-100%):** Excellent! Strong understanding of Retrofit networking.
- **24-26 (80-89%):** Good! Ready for Week 06 with minor review needed.
- **21-23 (70-79%):** Passing, but review weak areas before Week 06.
- **Below 21 (<70%):** Re-read learning notes and retry quiz.

---

## Areas for Review

Based on your incorrect answers, review these topics:

If you missed questions 1-8: Review HTTP fundamentals and Retrofit basics
If you missed questions 9-15: Review Android networking constraints and best practices
If you missed questions 16-23: Review Retrofit annotations and configuration
If you missed questions 24-30: Review error handling and practical implementation

---

## Retake Policy

- You may retake this quiz after reviewing your mistakes
- Goal is understanding, not perfect score on first attempt
- **Must score 80%+ before starting Week 06**

---

## Certification

- [ ] I scored 80% or higher
- [ ] I reviewed all incorrect answers
- [ ] I understand the concepts behind my mistakes
- [ ] I am ready for Week 06

**Date Completed:** ___________

**Signature:** ___________

---

**Next Step:** Complete Week 05 validation checklist and begin Week 06! 🚀


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
| [⬅ Week 04: FastAPI Backend](../week-04-fastapi-backend/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 06: Cloud ML Model ➡](../week-06-cloud-ml-model/README.md) |

---
