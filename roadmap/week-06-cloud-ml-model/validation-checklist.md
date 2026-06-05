# Week 06 Validation Checklist

Use this checklist to verify you have successfully completed Week 06: Real ML Model Integration. Check off each item as you complete it. All items in Sections 1-6 are required. Section 7 (bonus) is optional but recommended.

## Section 1: Model Integration

### 1.1 Model Loading
- [ ] Model file is accessible (in project directory or documented download location)
- [ ] Model loads successfully when Flask starts (no errors in console)
- [ ] Console prints "Model loaded successfully" or similar confirmation message
- [ ] Model input shape printed to console (e.g., `(None, 224, 224, 3)`)
- [ ] Model output shape printed to console (e.g., `(None, 6)` or `(None, 38)`)
- [ ] Model remains in memory (not reloaded per request)
- [ ] Loading errors caught and handled gracefully with informative messages

### 1.2 Model Documentation
- [ ] Model source documented (where it came from: Kaggle, TensorFlow Hub, Teachable Machine, etc.)
- [ ] Number of classes documented (6, 38, or other)
- [ ] Input image size documented (224x224, 256x256, etc.)
- [ ] Classes/diseases covered listed or referenced in documentation

**Validation Test**: Run Flask server and verify startup logs show model loaded with correct shapes.

---

## Section 2: Image Preprocessing

### 2.1 Preprocessing Implementation
- [ ] Function accepts image bytes as input
- [ ] Decodes bytes to PIL Image object
- [ ] Handles RGBA (4-channel) images by converting to RGB
- [ ] Handles grayscale images by converting to RGB
- [ ] Resizes image to model's expected dimensions
- [ ] Normalizes pixel values to [0, 1] range (or appropriate range for model)
- [ ] Converts to NumPy array with dtype float32
- [ ] Adds batch dimension: (H, W, 3) → (1, H, W, 3)

### 2.2 Preprocessing Validation
- [ ] Test with JPG image → processes successfully
- [ ] Test with PNG image → processes successfully
- [ ] Test with RGBA PNG → converts to RGB, processes successfully
- [ ] Output shape matches model.input_shape exactly
- [ ] Pixel value range verified: min ≈ 0.0, max ≈ 1.0 (print during testing)
- [ ] Preprocessing completes in < 200ms for typical mobile photos

**Validation Test**: Create test script that preprocesses 3 different image formats and prints shapes and value ranges. All should match expected format.

---

## Section 3: Label Mapping

### 3.1 Label Mapping Implementation
- [ ] Label mapping defined (dictionary or loaded from file)
- [ ] All model classes have corresponding labels
- [ ] Label order matches model's training class order
- [ ] Function to map class index to disease name implemented
- [ ] Handles invalid class indices gracefully (returns "Unknown" or similar)

### 3.2 Recommendation System
- [ ] Function to map disease name to recommendation implemented
- [ ] Recommendations are specific and actionable (not generic placeholders)
- [ ] All diseases have corresponding recommendations
- [ ] Healthy classifications have appropriate messages
- [ ] Handles unknown diseases gracefully with fallback message

### 3.3 Label Mapping Validation
- [ ] Test with simulated predictions (NumPy arrays) → correct disease names returned
- [ ] Test with known training images → predictions match expected labels
- [ ] If using file-based labels: file exists and loads successfully

**Validation Test**: Create test script that simulates model outputs and verifies correct disease names and recommendations are returned for each class.

---

## Section 4: Inference Endpoint

### 4.1 Endpoint Implementation
- [ ] `/predict` endpoint accepts POST requests
- [ ] Endpoint accepts image via multipart form-data (key: "image")
- [ ] Image bytes extracted from request successfully
- [ ] Preprocessing function called correctly
- [ ] Model inference performed: `MODEL.predict(img_array)`
- [ ] Output decoded: class index extracted using `np.argmax`
- [ ] Confidence score extracted: `predictions[0][class_idx]`
- [ ] Disease name retrieved via label mapping
- [ ] Recommendation retrieved based on disease name

### 4.2 Response Format
- [ ] Response is valid JSON
- [ ] Response includes `status` field ("success" or "error")
- [ ] Success response includes `prediction` object
- [ ] Prediction object includes `disease` field (string)
- [ ] Prediction object includes `confidence` field (float 0.0-1.0)
- [ ] Prediction object includes `recommendation` field (string)
- [ ] HTTP status code is 200 for successful requests
- [ ] Response structure matches documented format

**Validation Test**: Send valid image via Postman, verify response JSON structure and all required fields present.

---

## Section 5: Error Handling

### 5.1 Client Error Handling (400 Bad Request)
- [ ] Missing image in request → 400 status code with error message
- [ ] Empty filename → 400 status code with error message
- [ ] Empty file (0 bytes) → 400 status code with error message
- [ ] Invalid image format (e.g., .txt file) → 400 status code with error message
- [ ] Preprocessing errors → 400 status code with descriptive message

### 5.2 Server Error Handling (500 Internal Server Error)
- [ ] Model inference errors caught → 500 status code
- [ ] Unexpected exceptions caught → 500 status code with generic message
- [ ] Python tracebacks printed to server console (not sent to client)
- [ ] Server continues running after error (doesn't crash)

### 5.3 Error Response Format
- [ ] Error responses are valid JSON
- [ ] Error responses include `status: "error"` field
- [ ] Error responses include `message` field with description
- [ ] Error messages are helpful and descriptive
- [ ] Appropriate HTTP status codes used (400 vs 500)

**Validation Test**: Test all error cases in Postman and verify appropriate status codes and error messages.

---

## Section 6: Testing and Documentation

### 6.1 Postman Testing
- [ ] Postman collection created with test requests
- [ ] Test case: Valid tomato/potato disease image → success response
- [ ] Test case: Valid healthy leaf image → success response with "Healthy" classification
- [ ] Test case: Different disease image → success with different disease name
- [ ] Test case: Request without image → 400 error
- [ ] Test case: Invalid file format → 400 error
- [ ] All test results documented (screenshots or exported collection)
- [ ] Predictions vary for different images (not always same output)
- [ ] Confidence scores are in valid range (0.0 to 1.0)

### 6.2 Android Integration
- [ ] Android app updated to parse confidence field
- [ ] Android app updated to parse recommendation field
- [ ] UI displays disease name from real predictions
- [ ] UI displays confidence percentage (e.g., "78%" or "0.78")
- [ ] UI displays recommendation text
- [ ] App handles backend errors gracefully (doesn't crash)
- [ ] End-to-end test successful: Capture photo → See real prediction

### 6.3 Documentation
- [ ] Model choice explained (why 6-class or larger model)
- [ ] Preprocessing steps summarized
- [ ] Known limitations documented (supported classes, accuracy constraints)
- [ ] Model file location documented (in repo or download instructions)
- [ ] Testing summary included (what was tested, any issues found)
- [ ] Screenshots included (Postman tests and Android app with predictions)
- [ ] Documentation is 200-300 words minimum

**Validation Test**: Perform complete end-to-end test: Open Android app, capture leaf photo, see real prediction with confidence and recommendation displayed.

---

## Section 7: Bonus Features (Optional)

### 7.1 Confidence Thresholds (+3 points)
- [ ] Confidence level categorization implemented (high/medium/low)
- [ ] Warning messages for low confidence predictions
- [ ] UI reflects confidence levels appropriately

### 7.2 Alternative Predictions (+3 points)
- [ ] Top 3 predictions returned (not just top 1)
- [ ] Alternative predictions included in response
- [ ] Alternatives shown in UI when confidence is low/medium

### 7.3 Performance Monitoring (+2 points)
- [ ] Inference timing logged to console
- [ ] Preprocessing timing measured
- [ ] Total request time tracked
- [ ] Performance metrics included in response (optional)

### 7.4 Prediction Caching (+2 points)
- [ ] Image hashing implemented (MD5 or similar)
- [ ] Cache dictionary stores previous predictions
- [ ] Cached predictions returned for duplicate images
- [ ] Cache size limited to prevent memory issues

### 7.5 Additional Enhancements
- [ ] Batch prediction endpoint (`/predict_batch`)
- [ ] Model versioning in response metadata
- [ ] Detailed logging of all requests
- [ ] Unit tests for preprocessing and label mapping
- [ ] API documentation (Swagger/OpenAPI)

---

## Final Validation

### Integration Checklist
- [ ] Flask server runs without errors
- [ ] Model loads successfully on startup
- [ ] Postman tests all pass (valid and error cases)
- [ ] Android app receives and displays real predictions
- [ ] Confidence scores displayed in Android UI
- [ ] Recommendations displayed in Android UI
- [ ] End-to-end test passes: Photo capture → Real prediction

### Code Quality Checklist
- [ ] Code is well-commented
- [ ] No hardcoded secrets or credentials
- [ ] No unnecessary print statements in production code
- [ ] Error handling comprehensive
- [ ] Functions have descriptive names
- [ ] Magic numbers replaced with named constants

### Submission Checklist
- [ ] Updated Flask application in repository
- [ ] Model file or download instructions included
- [ ] Label mapping file (if applicable)
- [ ] Postman test screenshots (minimum 5: 3 success, 2 error)
- [ ] Android app screenshots (showing predictions)
- [ ] Documentation (200-300 words)
- [ ] All files committed and pushed to repository

---

## Validation Results

**Date Completed**: ________________

**Total Required Items Completed**: ______ / ______

**Total Bonus Items Completed**: ______ / 10

**Issues Encountered**:
- Issue 1: ___________________________________
  - Resolution: _______________________________
- Issue 2: ___________________________________
  - Resolution: _______________________________
- Issue 3: ___________________________________
  - Resolution: _______________________________

**Performance Metrics**:
- Model loading time: _________ seconds
- Average preprocessing time: _________ ms
- Average inference time: _________ ms
- Total average request time: _________ ms

**Testing Summary**:
- Total test cases executed: _______
- Test cases passed: _______
- Test cases failed: _______
- Known issues remaining: _______

**Confidence in Completion** (1-5, 5 being highest): _______

**Notes**:
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## Instructor Validation (For Grading)

**Model Integration** (25 points): ______ / 25
- Comments: _________________________________________________

**Preprocessing** (20 points): ______ / 20
- Comments: _________________________________________________

**Label Mapping** (15 points): ______ / 15
- Comments: _________________________________________________

**Inference Endpoint** (25 points): ______ / 25
- Comments: _________________________________________________

**Error Handling** (10 points): ______ / 10
- Comments: _________________________________________________

**Testing** (15 points): ______ / 15
- Comments: _________________________________________________

**Android Integration** (10 points): ______ / 10
- Comments: _________________________________________________

**Documentation** (10 points): ______ / 10
- Comments: _________________________________________________

**Bonus** (up to 10 points): ______ / 10
- Comments: _________________________________________________

**Total Score**: ______ / 130 (normalized to ______ / 100)

**Overall Comments**:
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

**Instructor Signature**: _____________________ **Date**: __________


<!-- NAV_FOOTER_START -->

---

## 📚 Week 06 — Navigation

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
| [⬅ Week 05: Android Networking](../week-05-android-networking/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 07: Room Database & History ➡](../week-07-room-sqlite-history/README.md) |

---
