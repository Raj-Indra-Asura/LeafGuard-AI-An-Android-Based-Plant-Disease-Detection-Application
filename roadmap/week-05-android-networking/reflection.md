# Week 05: Reflection Prompts - Android Networking with Retrofit

## Related materials

- Exercises (primary Kotlin): [../../exercises/android-kotlin/](../../exercises/android-kotlin/)
- Exercises (secondary Java): [../../exercises/android/](../../exercises/android/)
- Solutions: [../../solutions/week-05/](../../solutions/week-05/)
- Notebooks: [../../notebooks/week-05/](../../notebooks/week-05/)
- Glossary: [../../GLOSSARY.md](../../GLOSSARY.md)

---

## Purpose of This Reflection

API means Application Programming Interface, HTTP means HyperText Transfer Protocol, JSON means JavaScript Object Notation, URL means Uniform Resource Locator, and LAN means Local Area Network. Use those full names once in your reflection, then the acronyms are fine.

Reflection transforms hands-on coding into deep learning. By explaining concepts in your own words, you solidify understanding and prepare for viva questions. This reflection should take 30-45 minutes and will be invaluable when preparing for your final presentation.

---

## Section 1: Technical Understanding

### 1.1 HTTP and Networking Fundamentals

**Write 3-4 sentences explaining:**

**Q: What is HTTP and how does it work in the context of your LeafGuard AI app?**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Hints to consider:**
- Request-response cycle
- Client (Android) and server (FastAPI) roles
- POST method for uploading data
- JSON for structured data exchange

---

### 1.2 Why Retrofit?

**Write 3-4 sentences explaining:**

**Q: Why did you use Retrofit instead of Java's HttpURLConnection or OkHttp directly?**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Hints to consider:**
- Boilerplate code reduction
- Type-safe API definitions
- Automatic JSON parsing with Gson
- Built-in threading support
- Industry standard

---

### 1.3 Asynchronous Programming

**Write 4-5 sentences explaining:**

**Q: Why can't network calls run on the main thread? How does Retrofit solve this problem?**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Hints to consider:**
- Main thread handles UI rendering
- Network calls take 1-3 seconds
- UI would freeze (bad UX)
- NetworkOnMainThreadException
- Retrofit's enqueue() runs on background thread
- Callbacks deliver results back to main thread

---

### 1.4 Multipart File Upload

**Write 3-4 sentences explaining:**

**Q: What is multipart form-data and why is it necessary for uploading images?**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Hints to consider:**
- Regular form data can't handle binary files
- Multipart allows mixing text fields and files
- Each part has boundary separator
- Content-Type header specifies format

---

## Section 2: Implementation Reflection

### 2.1 Most Challenging Part

**Q: What was the most difficult aspect of Week 05 for you?**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Q: How did you overcome this challenge?**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

### 2.2 Debugging Experience

**Q: Describe a bug you encountered during Week 05 implementation.**

What was the bug:
_______________________________________________________________
_______________________________________________________________

What error message did you see:
_______________________________________________________________
_______________________________________________________________

How did you debug it (Logcat, Stack Overflow, re-reading docs):
_______________________________________________________________
_______________________________________________________________

How did you fix it:
_______________________________________________________________
_______________________________________________________________

---

### 2.3 Aha! Moment

**Q: What concept "clicked" for you this week? Describe the moment you finally understood something that was confusing earlier.**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Section 3: Architecture and Design

### 3.1 Singleton Pattern

**Write 2-3 sentences explaining:**

**Q: Why did you implement RetrofitClient as a singleton? What problem does this solve?**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Hints to consider:**
- Creating Retrofit instances is expensive
- One instance reused throughout app
- Prevents memory waste
- Centralized configuration

---

### 3.2 Callback Pattern

**Write 3-4 sentences explaining:**

**Q: How do callbacks work in Retrofit? Explain the flow from making a request to receiving a response.**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Hints to consider:**
- Call object created
- enqueue() starts background thread
- Method returns immediately
- onResponse() or onFailure() called later
- Callbacks run on main thread (safe for UI updates)

---

### 3.3 Error Handling Strategy

**Write 3-4 sentences explaining:**

**Q: What's the difference between errors handled in onResponse() vs onFailure()? Give examples.**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Hints to consider:**
- onFailure: Couldn't reach server (IOException, timeout)
- onResponse with !isSuccessful(): Reached server but got error status (404, 500)
- Both need error messages shown to user
- Different error messages for each type

---

## Section 4: CSE 2206 Course Connection

### 4.1 Syllabus Topics Covered

**Q: List 5 CSE 2206 syllabus topics that Week 05 directly addresses:**

1. _______________________________________________________________
2. _______________________________________________________________
3. _______________________________________________________________
4. _______________________________________________________________
5. _______________________________________________________________

**Suggested topics:**
- Network programming
- HTTP requests (POST)
- JSON parsing
- Asynchronous operations
- Third-party libraries
- Error handling
- Client-server architecture

---

### 4.2 Viva Preparation

**Q: If your teacher asks "How does your app communicate with the backend?", write your complete answer (5-7 sentences):**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Your answer should cover:**
- Retrofit library
- HTTP POST request
- Multipart file upload
- `/predict` endpoint
- Backend processes and returns JSON
- Gson parses JSON to Java objects
- Result displayed in ResultActivity

---

## Section 5: Comparison and Analysis

### 5.1 Before and After Week 05

**Q: What could your app do before Week 05?**

Your answer:
_______________________________________________________________
_______________________________________________________________

**Q: What can your app do now after Week 05?**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Q: How does this change the app's capabilities?**

Your answer:
_______________________________________________________________
_______________________________________________________________

---

### 5.2 Local vs Cloud Architecture

**Write 3-4 sentences:**

**Q: Your backend currently runs on your laptop (local). What would need to change to deploy it to a cloud server?**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Hints to consider:**
- BASE_URL would change from local IP to public domain
- Would use HTTPS instead of HTTP
- No need for network security config exception
- Backend would always be accessible (not just on Wi-Fi)

---

## Section 6: Code Quality Reflection

### 6.1 Best Practices Applied

**Q: List 3 best practices you followed in your Week 05 code:**

1. _______________________________________________________________
2. _______________________________________________________________
3. _______________________________________________________________

**Examples:**
- Used singleton for Retrofit instance
- Showed loading indicator during network operations
- Handled both success and error cases
- Used strings.xml instead of hardcoded strings
- Implemented proper null checks
- Disabled button during upload to prevent multiple requests

---

### 6.2 Code You're Proud Of

**Q: Copy a code snippet from your Week 05 implementation that you're particularly proud of. Explain why you're proud of this code (2-3 sentences).**

```java
// Paste your code snippet here




```

Why you're proud:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

### 6.3 Code You Would Improve

**Q: If you had more time, what would you improve in your Week 05 code? Why?**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Possible improvements:**
- Better error messages for different error types
- Retry mechanism for failed requests
- Caching predictions to reduce redundant uploads
- Request cancellation when activity is destroyed
- Progress indicator showing upload percentage
- Timeout values optimization

---

## Section 7: Testing and Validation

### 7.1 Testing Process

**Q: Describe how you tested your networking implementation. What scenarios did you test?**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Should mention:**
- Success case (backend running)
- Network error (Wi-Fi off)
- Backend down error
- Multiple consecutive uploads
- Various image sizes

---

### 7.2 Logcat Debugging

**Q: How did you use Logcat to debug networking issues? Give a specific example.**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Section 8: Learning and Growth

### 8.1 New Skills Acquired

**Q: List 5 new skills you gained this week:**

1. _______________________________________________________________
2. _______________________________________________________________
3. _______________________________________________________________
4. _______________________________________________________________
5. _______________________________________________________________

---

### 8.2 Confidence Level

**Before Week 05, rate your confidence with Android networking (1-10):** _____

**After Week 05, rate your confidence with Android networking (1-10):** _____

**Q: What contributed most to any increase in confidence?**

Your answer:
_______________________________________________________________
_______________________________________________________________

---

### 8.3 Preparing for Week 06

**Q: Week 06 will integrate a real ML model into your FastAPI backend. Based on Week 05 experience, what questions do you have about Week 06?**

Your questions:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Section 9: Real-World Application

### 9.1 Beyond LeafGuard AI

**Q: Besides plant disease detection, name 3 other types of apps that would use similar networking (image upload + server processing + result display):**

1. _______________________________________________________________
2. _______________________________________________________________
3. _______________________________________________________________

**Examples to consider:**
- Face recognition apps
- Document scanner apps
- Translation apps (image-to-text)
- Medical diagnosis apps
- Quality control apps in manufacturing

---

### 9.2 Scaling Considerations

**Write 3-4 sentences:**

**Q: If 10,000 users were using your app simultaneously, what problems might occur with your current implementation? How could you solve them?**

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Hints to consider:**
- Server might crash (need more powerful server)
- Slow response times (need load balancing)
- High bandwidth costs (need image compression)
- Database bottleneck (need optimization)

---

## Section 10: Personal Reflection

### 10.1 Time Management

**Q: How many hours did you spend on Week 05?** _____ hours

**Q: How was this time distributed?**

- Reading documentation: _____ hours
- Watching tutorials: _____ hours
- Writing code: _____ hours
- Debugging: _____ hours
- Testing: _____ hours

**Q: If you could redo Week 05, what would you do differently to use time more effectively?**

Your answer:
_______________________________________________________________
_______________________________________________________________

---

### 10.2 Help and Resources

**Q: What resources did you use this week? (Check all that apply)**

- [ ] Week 05 learning notes
- [ ] Official Retrofit documentation
- [ ] Stack Overflow
- [ ] YouTube tutorials
- [ ] Classmate discussions
- [ ] Teacher/mentor guidance
- [ ] Android Developer documentation
- [ ] Other: _________________

**Q: Which resource was most helpful? Why?**

Your answer:
_______________________________________________________________
_______________________________________________________________

---

### 10.3 Advice for Future Students

**Q: If you were teaching Week 05 to a fellow student, what advice would you give them?**

Your advice:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Section 11: Demonstrate Your Understanding

### 11.1 Teach-Back Exercise

**Q: Explain Retrofit to someone who has never used it, using an analogy or real-world comparison (3-4 sentences):**

Your explanation:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Example approach:** "Retrofit is like a delivery service..."

---

### 11.2 Diagram Your Understanding

**Q: Draw a simple diagram showing the flow from when the user taps "Detect Disease" to when the result is displayed. Use boxes and arrows. Label each step.**

Draw your diagram here (or on paper and attach photo):

```
[Diagram space]








```

---

## Completion Checklist

- [ ] All questions answered in complete sentences
- [ ] Technical concepts explained in your own words (not copied)
- [ ] Specific examples from your code provided
- [ ] Honest assessment of challenges and learning
- [ ] Reviewed answers for clarity and completeness
- [ ] Saved this file with your answers
- [ ] Date completed: ___________

---

## Teacher Review (Optional)

**Teacher Comments:**
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

**Strengths Demonstrated:**
_______________________________________________________________

**Areas for Improvement:**
_______________________________________________________________

---

**Congratulations on completing your Week 05 reflection! This document will be invaluable for your viva preparation and final presentation. 🎓**


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
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| **7** | **reflection.md** ← *You are here* | **Reflection & Consolidation** |

---

### Within-Week Navigation

[← Knowledge Assessment Quiz](quiz.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Reflection & Consolidation** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Week 06: Cloud ML Model (Start) →](../week-06-cloud-ml-model/README.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 04: FastAPI Backend](../week-04-fastapi-backend/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 06: Cloud ML Model ➡](../week-06-cloud-ml-model/README.md) |

---
