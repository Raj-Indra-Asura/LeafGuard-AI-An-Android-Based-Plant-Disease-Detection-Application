# Week 06 Validation Checklist

## Week 06 project reality check

> Note: The committed `assets/model.tflite` is a placeholder TEXT file, not a real trained model. Until a real model is provided, the backend uses a **mock predictor** (in `model_loader.py`) and the on-device `TFLiteClassifier` uses a **green-channel heuristic fallback**, so the app still runs end-to-end. The real trained model arrives in **Week 09**. Low or odd confidence values are normal this week because predictions are placeholders.

## Related materials

- Exercises: [backend](../../exercises/backend/) and [ML](../../exercises/ml/)
- Solutions: [Week 06 solutions](../../solutions/week-06/)
- Notebooks: [Week 06 notebooks](../../notebooks/week-06/)
- Glossary: [GLOSSARY.md](../../GLOSSARY.md)

Use this checklist to verify you have successfully completed Week 06: Real ML Model Integration. Check off each item as you complete it. All items in Sections 1-6 are required. Section 7 (bonus) is optional but recommended.

## Section 1: Model Integration

- [ ] I understand predictions are from the mock/placeholder now; the real model comes in Week 09 — yes/no

### 1.1 Model Loading
- [ ] Can I confirm this: model file is accessible (in project directory or documented download location) — yes/no
- [ ] Can I confirm this: model loads successfully when FastAPI starts (no errors in console) — yes/no
- [ ] Can I confirm this: console prints "Model loaded successfully" or similar confirmation message — yes/no
- [ ] Can I confirm this: model input shape printed to console (e.g., `(None, 224, 224, 3)`) — yes/no
- [ ] Can I confirm this: model output shape printed to console (e.g., `(None, 10)` or `(None, 38)`) — yes/no
- [ ] Can I confirm this: model remains in memory (not reloaded per request) — yes/no
- [ ] Can I confirm this: loading errors caught and handled gracefully with informative messages — yes/no

### 1.2 Model Documentation
- [ ] Can I confirm this: model source documented (where it came from: Kaggle, TensorFlow Hub, Teachable Machine, etc.) — yes/no
- [ ] Can I confirm this: number of classes documented (10 now; real model details in Week 09) — yes/no
- [ ] Can I confirm this: input image size documented (224x224, 256x256, etc.) — yes/no
- [ ] Can I confirm this: classes/diseases covered listed or referenced in documentation — yes/no

**Validation Test**: Run the FastAPI server and verify startup logs show the mock predictor or model loaded with the expected 224×224 / 10-label contract.

---

## Section 2: Image Preprocessing

### 2.1 Preprocessing Implementation
- [ ] Can I confirm this: function accepts image bytes as input — yes/no
- [ ] Can I confirm this: decodes bytes to PIL Image object — yes/no
- [ ] Can I confirm this: handles RGBA (4-channel) images by converting to RGB — yes/no
- [ ] Can I confirm this: handles grayscale images by converting to RGB — yes/no
- [ ] Can I confirm this: resizes image to model's expected dimensions — yes/no
- [ ] Can I confirm this: normalizes pixel values to [0, 1] range (or appropriate range for model) — yes/no
- [ ] Can I confirm this: converts to NumPy array with dtype float32 — yes/no
- [ ] Can I confirm this: adds batch dimension: (H, W, 3) → (1, H, W, 3) — yes/no

### 2.2 Preprocessing Validation
- [ ] Can I confirm this: test with JPG image → processes successfully — yes/no
- [ ] Can I confirm this: test with PNG image → processes successfully — yes/no
- [ ] Can I confirm this: test with RGBA PNG → converts to RGB, processes successfully — yes/no
- [ ] Can I confirm this: output shape matches model.input_shape exactly — yes/no
- [ ] Can I confirm this: pixel value range verified: min ≈ 0.0, max ≈ 1.0 (print during testing) — yes/no
- [ ] Can I confirm this: preprocessing completes in < 200ms for typical mobile photos — yes/no

**Validation Test**: Create test script that preprocesses 3 different image formats and prints shapes and value ranges. All should match expected format.

---

## Section 3: Label Mapping

### 3.1 Label Mapping Implementation
- [ ] Can I confirm this: label mapping defined (dictionary or loaded from file) — yes/no
- [ ] Can I confirm this: all model classes have corresponding labels — yes/no
- [ ] Can I confirm this: label order matches model's training class order — yes/no
- [ ] Can I confirm this: function to map class index to disease name implemented — yes/no
- [ ] Can I confirm this: handles invalid class indices gracefully (returns "Unknown" or similar) — yes/no

### 3.2 Recommendation System
- [ ] Can I confirm this: function or lookup to map disease name to symptoms, treatment, and prevention implemented — yes/no
- [ ] Can I confirm this: treatment and prevention guidance is specific and actionable, while predictions are marked as mock/placeholder — yes/no
- [ ] Can I confirm this: all diseases have corresponding symptoms, treatment, and prevention text — yes/no
- [ ] Can I confirm this: healthy classifications have appropriate messages — yes/no
- [ ] Can I confirm this: handles unknown diseases gracefully with fallback message — yes/no

### 3.3 Label Mapping Validation
- [ ] Can I confirm this: test with simulated predictions (NumPy arrays) → correct disease names returned — yes/no
- [ ] Can I confirm this: test with known training images → predictions match expected labels — yes/no
- [ ] Can I confirm this: if using file-based labels: file exists and loads successfully — yes/no

**Validation Test**: Create test script that simulates model outputs and verifies correct disease names plus symptoms, treatment, and prevention are returned for each class.

---

## Section 4: Inference Endpoint

### 4.1 Endpoint Implementation
- [ ] Can I send a POST request to the `/predict` endpoint — yes/no
- [ ] Can I confirm this: endpoint accepts image via multipart form-data (key: "image") — yes/no
- [ ] Can I confirm this: image bytes extracted from request successfully — yes/no
- [ ] Can I confirm this: preprocessing function called correctly — yes/no
- [ ] Can I confirm this: model inference performed: `MODEL.predict(img_array)` — yes/no
- [ ] Can I confirm this: output decoded: class index extracted using `np.argmax` — yes/no
- [ ] Can I confirm this: confidence score extracted: `predictions[0][class_idx]` — yes/no
- [ ] Can I confirm this: disease name retrieved via label mapping — yes/no
- [ ] Can I confirm this: symptoms, treatment, and prevention retrieved based on disease name — yes/no

### 4.2 Response Format
- [ ] Can I confirm this: response is valid JSON — yes/no
- [ ] Can I confirm this: response includes `status` field ("success" or "error") — yes/no
- [ ] Can I confirm this: success response includes `prediction` object — yes/no
- [ ] Can I confirm this: prediction object includes `disease` field (string) — yes/no
- [ ] Can I confirm this: prediction object includes `confidence` field (float 0.0-1.0) — yes/no
- [ ] Can I confirm this: prediction object includes `symptoms`, `treatment`, and `prevention` fields (strings) — yes/no
- [ ] Can I confirm this: HTTP status code is 200 for successful requests — yes/no
- [ ] Can I confirm this: response structure matches documented format — yes/no

**Validation Test**: Send valid image via Postman, verify response JSON structure and all required fields present.

---

## Section 5: Error Handling

### 5.1 Client Error Handling (400 Bad Request)
- [ ] Can I confirm this: missing image in request → 400 status code with error message — yes/no
- [ ] Can I confirm this: empty filename → 400 status code with error message — yes/no
- [ ] Can I confirm this: empty file (0 bytes) → 400 status code with error message — yes/no
- [ ] Can I confirm this: invalid image format (e.g., .txt file) → 400 status code with error message — yes/no
- [ ] Can I confirm this: preprocessing errors → 400 status code with descriptive message — yes/no

### 5.2 Server Error Handling (500 Internal Server Error)
- [ ] Can I confirm this: model inference errors caught → 500 status code — yes/no
- [ ] Can I confirm this: unexpected exceptions caught → 500 status code with generic message — yes/no
- [ ] Can I confirm this: python tracebacks printed to server console (not sent to client) — yes/no
- [ ] Can I confirm this: server continues running after error (doesn't crash) — yes/no

### 5.3 Error Response Format
- [ ] Can I confirm this: error responses are valid JSON — yes/no
- [ ] Can I confirm this: error responses include `status: "error"` field — yes/no
- [ ] Can I confirm this: error responses include `message` field with description — yes/no
- [ ] Can I confirm this: error messages are helpful and descriptive — yes/no
- [ ] Can I confirm this: appropriate HTTP status codes used (400 vs 500) — yes/no

**Validation Test**: Test all error cases in Postman and verify appropriate status codes and error messages.

---

## Section 6: Testing and Documentation

### 6.1 Postman Testing
- [ ] Can I confirm this: postman collection created with test requests — yes/no
- [ ] Can I run this test case successfully: Valid tomato/potato disease image → success response — yes/no
- [ ] Can I run this test case successfully: Valid healthy leaf image → success response with "Healthy" classification — yes/no
- [ ] Can I run this test case successfully: Different disease image → success with different disease name — yes/no
- [ ] Can I run this test case successfully: Request without image → 400 error — yes/no
- [ ] Can I run this test case successfully: Invalid file format → 400 error — yes/no
- [ ] Can I confirm this: all test results documented (screenshots or exported collection) — yes/no
- [ ] Can I confirm this: predictions vary for different images (not always same output) — yes/no
- [ ] Can I confirm this: confidence scores are in valid range (0.0 to 1.0) — yes/no

### 6.2 Android Integration
- [ ] Can I confirm this: android app updated to parse confidence field — yes/no
- [ ] Can I confirm this: android app updated to parse symptoms, treatment, and prevention fields — yes/no
- [ ] Can I confirm this: uI displays disease name from prediction responses — yes/no
- [ ] Can I confirm this: uI displays confidence percentage (e.g., "78%" or "0.78") — yes/no
- [ ] Can I confirm this: uI displays treatment and prevention text — yes/no
- [ ] Can I confirm this: app handles backend errors gracefully (doesn't crash) — yes/no
- [ ] Can I confirm this: end-to-end test successful: Capture photo → See prediction response — yes/no

### 6.3 Documentation
- [ ] Can I confirm this: model choice explained (why the current 10-label placeholder/mock contract is used now) — yes/no
- [ ] Can I confirm this: preprocessing steps summarized — yes/no
- [ ] Can I confirm this: known limitations documented (supported classes, accuracy constraints) — yes/no
- [ ] Can I confirm this: model file location documented (in repo or download instructions) — yes/no
- [ ] Can I confirm this: testing summary included (what was tested, any issues found) — yes/no
- [ ] Can I confirm this: screenshots included (Postman tests and Android app with predictions) — yes/no
- [ ] Can I confirm this: documentation is 200-300 words minimum — yes/no

**Validation Test**: Perform complete end-to-end test: Open Android app, capture leaf photo, see prediction response with confidence and treatment and prevention displayed.

---

## Section 7: Bonus Features (Optional)

### 7.1 Confidence Thresholds (+3 points)
- [ ] Can I confirm this: confidence level categorization implemented (high/medium/low) — yes/no
- [ ] Can I confirm this: warning messages for low confidence predictions — yes/no
- [ ] Can I confirm this: uI reflects confidence levels appropriately — yes/no

### 7.2 Alternative Predictions (+3 points)
- [ ] Can I confirm this: top 3 predictions returned (not just top 1) — yes/no
- [ ] Can I confirm this: alternative predictions included in response — yes/no
- [ ] Can I confirm this: alternatives shown in UI when confidence is low/medium — yes/no

### 7.3 Performance Monitoring (+2 points)
- [ ] Can I confirm this: inference timing logged to console — yes/no
- [ ] Can I confirm this: preprocessing timing measured — yes/no
- [ ] Can I confirm this: total request time tracked — yes/no
- [ ] Can I confirm this: performance metrics included in response (optional) — yes/no

### 7.4 Prediction Caching (+2 points)
- [ ] Can I confirm this: image hashing implemented (MD5 or similar) — yes/no
- [ ] Can I confirm this: cache dictionary stores previous predictions — yes/no
- [ ] Can I confirm this: cached predictions returned for duplicate images — yes/no
- [ ] Can I confirm this: cache size limited to prevent memory issues — yes/no

### 7.5 Additional Enhancements
- [ ] Can I confirm this: batch prediction endpoint (`/predict_batch`) — yes/no
- [ ] Can I confirm this: model versioning in response metadata — yes/no
- [ ] Can I confirm this: detailed logging of all requests — yes/no
- [ ] Can I confirm this: unit tests for preprocessing and label mapping — yes/no
- [ ] Can I confirm this: aPI documentation (Swagger/OpenAPI) — yes/no

---

## Final Validation

### Integration Checklist
- [ ] Can I confirm this: fastAPI server runs without errors — yes/no
- [ ] Can I confirm this: model loads successfully on startup — yes/no
- [ ] Can I confirm this: postman tests all pass (valid and error cases) — yes/no
- [ ] Can I confirm this: android app receives and displays prediction responses — yes/no
- [ ] Can I confirm this: confidence scores displayed in Android UI — yes/no
- [ ] Can I confirm this: symptoms, treatment, and prevention displayed in Android UI — yes/no
- [ ] Can I confirm this: end-to-end test passes: Photo capture → Real prediction — yes/no

### Code Quality Checklist
- [ ] Can I confirm this: code is well-commented — yes/no
- [ ] Can I confirm this: no hardcoded secrets or credentials — yes/no
- [ ] Can I confirm this: no unnecessary print statements in production code — yes/no
- [ ] Can I confirm this: error handling comprehensive — yes/no
- [ ] Can I confirm this: functions have descriptive names — yes/no
- [ ] Can I confirm this: magic numbers replaced with named constants — yes/no

### Submission Checklist
- [ ] Can I confirm this: updated FastAPI application in repository — yes/no
- [ ] Can I confirm this: model file or download instructions included — yes/no
- [ ] Can I confirm this: label mapping file (if applicable) — yes/no
- [ ] Can I confirm this: postman test screenshots (minimum 5: 3 success, 2 error) — yes/no
- [ ] Can I confirm this: android app screenshots (showing predictions) — yes/no
- [ ] Can I confirm this: documentation (200-300 words) — yes/no
- [ ] Can I confirm this: all files committed and pushed to repository — yes/no

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
