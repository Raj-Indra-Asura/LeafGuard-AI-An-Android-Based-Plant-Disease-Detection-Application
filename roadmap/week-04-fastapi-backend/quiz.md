# Week 04 Quiz: FastAPI Backend Development

## Instructions

This quiz tests your understanding of Week 04 concepts. Answer all questions without referring to notes first, then check your answers against learning-notes.md.

**Total Questions:** 15 (10 required minimum)

**Time Limit:** 45 minutes (recommended)

**Passing Score:** 12/15 correct (80%)

---

## Section 1: REST API Fundamentals (Questions 1-3)

### Question 1: HTTP Methods

**Which HTTP method should be used for the LeafGuard /predict endpoint and why?**

A) GET - because we are getting a disease prediction
B) POST - because we are sending data (image file) to the server
C) PUT - because we are updating the prediction database
D) DELETE - because we are removing the disease from the image

**Correct Answer:** ___

**Explanation:** _______________________________________________

---

### Question 2: HTTP Status Codes

**Match each status code to its correct meaning:**

| Status Code | Meaning |
|-------------|---------|
| 200 | A. Resource not found |
| 400 | B. Internal server error |
| 404 | C. Success |
| 500 | D. Client error (invalid request) |

**Your Answers:**
- 200: ___
- 400: ___
- 404: ___
- 500: ___

---

### Question 3: RESTful Principles

**Which of the following is TRUE about RESTful APIs? (Select all that apply)**

- [ ] A. Each request must contain all information needed (stateless)
- [ ] B. Server remembers previous requests (stateful sessions)
- [ ] C. Use standard HTTP methods (GET, POST, PUT, DELETE)
- [ ] D. Always use XML for responses (not JSON)
- [ ] E. Resources are accessed via URLs (/api/predict, /api/diseases)

**Correct Options:** _______

---

## Section 2: FastAPI Specifics (Questions 4-6)

### Question 4: Pydantic Models

**What is the purpose of Pydantic models in FastAPI?**

```python
class PredictionResponse(BaseModel):
    disease: str
    confidence: float = Field(..., ge=0.0, le=1.0)
```

A) To create database tables
B) To validate data structure and types automatically
C) To generate HTML templates
D) To connect to MySQL database

**Correct Answer:** ___

**What does `ge=0.0, le=1.0` mean?** _______________________________________________

---

### Question 5: File Upload

**Complete the code for accepting file uploads in FastAPI:**

```python
from fastapi import ________, File

@app.post("/upload")
async def upload_file(file: ________ = File(...)):
    contents = ________ file.read()
    return {"size": len(contents)}
```

**Fill in the blanks:**
1. _______________
2. _______________
3. _______________

---

### Question 6: CORS Middleware

**Why is CORS middleware needed in the LeafGuard backend?**

```python
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)
```

A) To make the API faster
B) To allow Android app to make cross-origin requests
C) To encrypt data
D) To compress responses

**Correct Answer:** ___

**What does `allow_origins=["*"]` mean?** _______________________________________________

---

## Section 3: Network Configuration (Questions 7-9)

### Question 7: Localhost vs Local IP

**Explain the difference between these two URLs:**
- http://127.0.0.1:8000
- http://192.168.1.100:8000

**Your Answer:**

127.0.0.1: _______________________________________________

192.168.1.100: _______________________________________________

**Which one can your Android phone access?** _______________

---

### Question 8: Server Binding

**What is wrong with this command for network access?**

```bash
uvicorn main:app --reload
```

**Problem:** _______________________________________________

**Correct Command:** _______________________________________________

**Explanation:** _______________________________________________

---

### Question 9: Firewall Troubleshooting

**Your phone cannot connect to FastAPI backend at http://192.168.1.100:8000**

**List 4 things to check:**

1. _______________________________________________
2. _______________________________________________
3. _______________________________________________
4. _______________________________________________

---

## Section 4: JSON and Data Formats (Questions 10-11)

### Question 10: Multipart vs JSON

**Why do we use multipart/form-data for image uploads instead of JSON?**

A) JSON cannot contain images
B) Multipart is more secure
C) JSON would require base64 encoding, increasing size by 33%
D) Multipart is faster to parse

**Correct Answer:** ___

**Additional Explanation:** _______________________________________________

---

### Question 11: JSON Structure

**Identify the error in this JSON response:**

```json
{
  "disease": "Tomato Blight",
  "confidence": 1.5,
  "symptoms": "",
  "treatment": "Apply fungicide",
  "timestamp": 2024-01-15
}
```

**Errors Found:**

1. _______________________________________________
2. _______________________________________________
3. _______________________________________________

---

## Section 5: Code Analysis (Questions 12-13)

### Question 12: Error Handling

**What will happen when this code receives a .txt file?**

```python
@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    contents = await file.read()
    image = Image.open(io.BytesIO(contents))
    result = model.predict(image)
    return {"disease": result}
```

A) Returns 200 OK with error in response
B) Server crashes with 500 error
C) Automatically returns 400 error
D) Ignores the file and returns random result

**Correct Answer:** ___

**How should this be fixed?** _______________________________________________

---

### Question 13: Validation Logic

**Complete the validation function:**

```python
def validate_image(file: UploadFile) -> Optional[str]:
    ALLOWED_TYPES = ["image/jpeg", "image/png"]

    # Check content type
    if _______________________________:
        return "Invalid file type"

    # Check file extension
    if file.filename:
        ext = file.filename.split(".")[-1].lower()
        if _______________________________:
            return "Invalid extension"

    return None  # Valid
```

**Fill in the conditions:**
1. _______________________________________________
2. _______________________________________________

---

## Section 6: Practical Application (Questions 14-15)

### Question 14: End-to-End Flow

**Arrange these steps in correct order for a successful disease prediction:**

- [ ] A. Android app uploads image to /api/predict
- [ ] B. FastAPI validates file type and size
- [ ] C. User captures leaf photo with camera
- [ ] D. Server returns JSON with disease information
- [ ] E. Server processes image (dummy or real model)
- [ ] F. Android app parses JSON and displays result

**Correct Order:** ___ → ___ → ___ → ___ → ___ → ___

---

### Question 15: Troubleshooting Scenario

**Scenario:** You run `uvicorn main:app --host 0.0.0.0 --port 8000` and see:

```
ERROR: Error loading ASGI app. Could not import module "main".
```

**Identify 3 possible causes and solutions:**

| Possible Cause | Solution |
|----------------|----------|
| 1. | |
| 2. | |
| 3. | |

---

## Bonus Questions (Optional)

### Bonus Question 1: Async vs Sync

**What is the advantage of using `async def` for file upload endpoints?**

**Your Answer:** _______________________________________________

---

### Bonus Question 2: Real-World Scaling

**The LeafGuard backend currently runs on your laptop. What would need to change to support 1000 concurrent users?**

**Your Answer:**

1. _______________________________________________
2. _______________________________________________
3. _______________________________________________

---

## Answer Key

### Section 1
**Q1:** B - POST is used because we are sending data (image file) to the server. GET is only for retrieving data without sending a body.

**Q2:**
- 200: C (Success)
- 400: D (Client error)
- 404: A (Not found)
- 500: B (Server error)

**Q3:** A, C, E are TRUE. RESTful APIs are stateless (A), use standard HTTP methods (C), and resource-based URLs (E).

### Section 2
**Q4:** B - Pydantic validates data structure and types. `ge=0.0, le=1.0` means "greater than or equal to 0.0, less than or equal to 1.0"

**Q5:**
1. UploadFile
2. UploadFile
3. await

**Q6:** B - CORS allows cross-origin requests from Android app. `allow_origins=["*"]` means accept requests from any origin.

### Section 3
**Q7:**
- 127.0.0.1: Localhost, refers to "this device" only
- 192.168.1.100: Local network IP, accessible from other devices on same network
- Phone can access: 192.168.1.100:8000

**Q8:**
- Problem: Binds to 127.0.0.1 only (localhost)
- Correct: `uvicorn main:app --host 0.0.0.0 --port 8000 --reload`
- Explanation: --host 0.0.0.0 accepts connections from any network interface

**Q9:**
1. Both devices on same Wi-Fi network (not mobile data)
2. Server running with --host 0.0.0.0
3. Firewall allows Python / port 8000
4. Correct IP address (verify with ipconfig/ifconfig)

### Section 4
**Q10:** C and A are correct. Base64 increases size by 33%, and multipart sends binary directly.

**Q11:**
1. confidence: 1.5 - exceeds max value of 1.0
2. symptoms: "" - empty string violates min_length constraint
3. timestamp: 2024-01-15 - not ISO format, should be "2024-01-15T00:00:00Z"

### Section 5
**Q12:** B - Server crashes because Image.open() will fail on non-image files. Fix: Add validation before processing.

**Q13:**
1. `file.content_type not in ALLOWED_TYPES`
2. `ext not in ["jpg", "jpeg", "png"]`

### Section 6
**Q14:** C → A → B → E → D → F

**Q15:**
1. Cause: Not in correct directory | Solution: cd to leafguard-backend
2. Cause: main.py doesn't exist | Solution: Verify file exists
3. Cause: Virtual environment not activated | Solution: source venv/bin/activate

### Bonus
**Bonus 1:** Async allows server to handle multiple uploads simultaneously without blocking. While waiting for one file to upload, it can process other requests.

**Bonus 2:**
1. Deploy to cloud (AWS, Heroku, Google Cloud)
2. Use production ASGI server (Gunicorn with Uvicorn workers)
3. Add load balancer to distribute requests
4. Use CDN for static assets
5. Implement caching (Redis)

---

## Scoring

**Total Correct:** ___ / 15

**Percentage:** ____%

**Grade:**
- 14-15 correct (93%+): Excellent - Deep understanding
- 12-13 correct (80-86%): Good - Ready for Week 05
- 10-11 correct (67-73%): Fair - Review weak areas before proceeding
- Below 10 (<67%): Review learning-notes.md and repeat exercises

---

## Areas to Review

Based on your incorrect answers, review these sections in learning-notes.md:

- **Section 1 errors:** Review "REST API Fundamentals" and "HTTP Protocol Deep Dive"
- **Section 2 errors:** Review "FastAPI Framework Explained" and "Pydantic validation"
- **Section 3 errors:** Review "Local Network Configuration"
- **Section 4 errors:** Review "JSON Response Design" and "Multipart Form-Data"
- **Section 5 errors:** Review "Error Handling and Validation"
- **Section 6 errors:** Review "Build Task" and revisit exercises

---

## Reflection

**Questions I got wrong and why:**

1. Question ___: I didn't understand _______________________________________________

2. Question ___: I confused _______________________________________________

**What I will do to improve:**

1. _______________________________________________
2. _______________________________________________

**Concepts I'm most confident about:**

1. _______________________________________________
2. _______________________________________________

---

**Quiz Completed By:** ___________________________

**Date:** ___________________________

**Ready for Week 05:** ☐ YES (12+ correct) ☐ REVIEW NEEDED (<12 correct)


<!-- NAV_FOOTER_START -->

---

## 📚 Week 04 — Navigation

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
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
