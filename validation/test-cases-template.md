# LeafGuard AI - Comprehensive Test Cases

## Test Case Documentation Template

| Test Case ID | Feature Area | Test Case Description | Input/Action | Expected Result | Actual Result | Status | Notes |
|--------------|--------------|----------------------|--------------|-----------------|---------------|--------|-------|
| TC-001 | User Authentication | Verify user registration with valid credentials | Enter email: test@example.com, password: Test@123, confirm password: Test@123, click Register | User account created successfully, redirected to login screen | | | |
| TC-002 | User Authentication | Verify user registration with invalid email | Enter email: invalidemail, password: Test@123, confirm password: Test@123, click Register | Error message displayed: "Invalid email format" | | | |
| TC-003 | User Authentication | Verify user registration with weak password | Enter email: test@example.com, password: 123, confirm password: 123, click Register | Error message displayed: "Password must be at least 8 characters with uppercase, lowercase, and special character" | | | |
| TC-004 | User Authentication | Verify user registration with mismatched passwords | Enter email: test@example.com, password: Test@123, confirm password: Test@456, click Register | Error message displayed: "Passwords do not match" | | | |
| TC-005 | User Authentication | Verify user login with valid credentials | Enter registered email and correct password, click Login | User successfully logged in, redirected to home screen | | | |
| TC-006 | User Authentication | Verify user login with invalid credentials | Enter email: test@example.com, password: WrongPass123, click Login | Error message displayed: "Invalid email or password" | | | |
| TC-007 | User Authentication | Verify logout functionality | Click on profile icon, select Logout option | User successfully logged out, redirected to login screen | | | |
| TC-008 | User Authentication | Verify forgot password functionality | Click "Forgot Password", enter registered email, click Submit | Password reset link sent to email, confirmation message displayed | | | |
| TC-009 | Image Capture | Verify camera permission request | Open app for first time, navigate to scan screen | Camera permission dialog displayed | | | |
| TC-010 | Image Capture | Verify image capture from camera | Grant camera permission, click "Take Photo" button, capture plant leaf image | Image captured successfully and displayed in preview | | | |
| TC-011 | Image Capture | Verify image selection from gallery | Click "Choose from Gallery" button, select image from device gallery | Selected image displayed in preview | | | |
| TC-012 | Image Capture | Verify image capture with denied camera permission | Deny camera permission, click "Take Photo" button | Error message displayed: "Camera permission required to capture images" | | | |
| TC-013 | Image Capture | Verify retake functionality | Capture image, click "Retake" button | Previous image cleared, camera reopened for new capture | | | |
| TC-014 | Disease Detection | Verify disease detection with healthy leaf image | Upload clear image of healthy plant leaf, click "Analyze" | Disease status: "Healthy", confidence score displayed, no treatment recommendations | | | |
| TC-015 | Disease Detection | Verify disease detection with diseased leaf image | Upload clear image of diseased plant leaf (e.g., bacterial blight), click "Analyze" | Disease name identified correctly, confidence score >80%, treatment recommendations displayed | | | |
| TC-016 | Disease Detection | Verify disease detection with low-quality image | Upload blurry or poorly lit leaf image, click "Analyze" | Low confidence score displayed (<60%), suggestion to retake clear image | | | |
| TC-017 | Disease Detection | Verify disease detection with non-leaf image | Upload image of flower, fruit, or non-plant object, click "Analyze" | Error message displayed: "Please upload a valid plant leaf image" | | | |
| TC-018 | Disease Detection | Verify multiple disease detection on same leaf | Upload image with multiple disease symptoms, click "Analyze" | Top 3 probable diseases listed with confidence scores | | | |
| TC-019 | API Communication | Verify successful API request to backend | Upload valid leaf image, click "Analyze" | HTTP 200 response received, JSON data with disease predictions returned | | | |
| TC-020 | API Communication | Verify API request with network error | Disable internet connection, upload image, click "Analyze" | Error message displayed: "Network error. Please check your internet connection" | | | |
| TC-021 | API Communication | Verify API request timeout handling | Configure API with slow response time (>30s), upload image | Timeout error displayed: "Request timeout. Please try again" | | | |
| TC-022 | API Communication | Verify API error response handling | Trigger API server error (500), upload image, click "Analyze" | Error message displayed: "Server error. Please try again later" | | | |
| TC-023 | Result Display | Verify disease name display | Analyze diseased leaf image | Disease name displayed clearly in result screen with proper formatting | | | |
| TC-024 | Result Display | Verify confidence score display | Analyze leaf image | Confidence score displayed as percentage (e.g., 87.5%) with progress bar | | | |
| TC-025 | Result Display | Verify treatment recommendations display | Analyze diseased leaf image | Treatment recommendations displayed with bullet points, clear instructions | | | |
| TC-026 | Result Display | Verify disease symptoms display | Analyze diseased leaf image | List of common symptoms for identified disease displayed | | | |
| TC-027 | History Management | Verify scan history is saved locally | Perform 3 disease scans | All 3 scans saved in local database with timestamp, image thumbnail, results | | | |
| TC-028 | History Management | Verify scan history retrieval | Navigate to History screen | All previous scans displayed in chronological order (newest first) | | | |
| TC-029 | History Management | Verify history item details view | Click on history item | Detailed view opened showing full image, disease name, confidence score, date, treatment | | | |
| TC-030 | History Management | Verify delete single history item | Long press on history item, click Delete | Confirmation dialog displayed, item deleted from history after confirmation | | | |
| TC-031 | History Management | Verify delete all history | Click "Delete All" button in History screen | Confirmation dialog displayed, all history cleared after confirmation | | | |
| TC-032 | History Management | Verify history persistence after app restart | Perform scan, close app completely, reopen app, navigate to History | Previous scan still present in history | | | |
| TC-033 | Local Database | Verify Room database initialization | Launch app for first time | Database created successfully, tables initialized | | | |
| TC-034 | Local Database | Verify data insertion into database | Perform disease scan | Scan result inserted into database with all fields (id, image_path, disease_name, confidence, timestamp) | | | |
| TC-035 | Local Database | Verify data retrieval from database | Navigate to History screen | All records retrieved from database and displayed correctly | | | |
| TC-036 | Local Database | Verify data update in database | Mark history item as favorite | Database record updated successfully | | | |
| TC-037 | Local Database | Verify data deletion from database | Delete history item | Record removed from database, query returns remaining records | | | |
| TC-038 | XML Parsing | Verify treatment recommendations XML parsing | Analyze diseased leaf | Treatment data from XML file parsed correctly and displayed | | | |
| TC-039 | XML Parsing | Verify disease information XML parsing | View disease details | Disease information (symptoms, causes, prevention) parsed from XML and displayed | | | |
| TC-040 | XML Parsing | Verify XML file loading error handling | Corrupt XML file, attempt to load treatment data | Error handled gracefully, default message displayed | | | |
| TC-041 | ML Model | Verify TFLite model loading | Launch app | ML model loaded successfully from assets folder into memory | | | |
| TC-042 | ML Model | Verify image preprocessing for model | Upload image for analysis | Image resized to model input size (224x224), normalized to [0,1] range | | | |
| TC-043 | ML Model | Verify model inference execution | Upload image, click Analyze | Model inference completes in <3 seconds, predictions returned | | | |
| TC-044 | ML Model | Verify model output interpretation | Analyze image | Model output tensor converted to disease labels and confidence scores | | | |
| TC-045 | UI/UX | Verify splash screen display | Launch app | Splash screen displayed for 2-3 seconds with app logo and name | | | |
| TC-046 | UI/UX | Verify responsive layout on different screen sizes | Test app on 5.5", 6.0", 6.5" screen devices | UI elements properly scaled and positioned on all screen sizes | | | |
| TC-047 | UI/UX | Verify navigation between screens | Navigate from Home → Scan → Result → History → Profile | All navigation transitions work smoothly without crashes | | | |
| TC-048 | UI/UX | Verify loading indicators | Upload image and click Analyze | Loading spinner displayed during API request and model inference | | | |
| TC-049 | UI/UX | Verify error message visibility | Trigger various errors (network, API, validation) | All error messages displayed clearly with appropriate icons and colors | | | |
| TC-050 | Performance | Verify app launch time | Launch app from cold start | App launches and displays splash screen within 2 seconds | | | |
| TC-051 | Performance | Verify image analysis response time | Upload image and click Analyze | Results displayed within 5 seconds on stable network | | | |
| TC-052 | Performance | Verify app memory usage | Monitor memory during normal operation | App memory usage remains under 150MB | | | |
| TC-053 | Performance | Verify app battery consumption | Use app continuously for 30 minutes | Battery drain less than 10% with normal screen brightness | | | |
| TC-054 | Security | Verify API key security | Inspect APK for hardcoded secrets | No API keys or sensitive data hardcoded in source code | | | |
| TC-055 | Security | Verify HTTPS communication | Monitor network traffic during API calls | All API requests made over HTTPS protocol | | | |
| TC-056 | Security | Verify user data encryption | Check database file | Sensitive user data encrypted in local database | | | |
| TC-057 | Accessibility | Verify content descriptions for images | Enable TalkBack, navigate app | All images have proper content descriptions for screen readers | | | |
| TC-058 | Accessibility | Verify minimum touch target size | Measure interactive elements | All buttons and clickable elements meet minimum 48dp touch target size | | | |
| TC-059 | Accessibility | Verify color contrast ratios | Check text and background colors | All text meets WCAG AA standard (minimum 4.5:1 contrast ratio) | | | |
| TC-060 | Edge Cases | Verify behavior with extremely large image | Upload 10MB+ image | Image compressed automatically before upload or error displayed with size limit | | | |

## Test Execution Guidelines

1. **Test Environment Setup**
   - Test on minimum Android 7.0 (API 24) devices
   - Test on various screen sizes (small, medium, large)
   - Test on both physical devices and emulators
   - Ensure stable internet connection for API tests

2. **Test Data Requirements**
   - Prepare sample images: healthy leaves, diseased leaves (various diseases), non-leaf images
   - Create test user accounts
   - Prepare XML files with treatment data
   - Have TFLite model file ready in assets

3. **Status Definitions**
   - **PASS**: Test case executed successfully, actual result matches expected result
   - **FAIL**: Test case failed, actual result differs from expected result
   - **BLOCKED**: Cannot execute test due to dependencies or environment issues
   - **SKIP**: Test case not applicable or intentionally skipped

4. **Defect Reporting**
   - Log all failures with detailed steps to reproduce
   - Attach screenshots and logcat outputs
   - Assign severity: Critical, High, Medium, Low
   - Link to test case ID

5. **Regression Testing**
   - Execute all PASS test cases after bug fixes
   - Re-test FAIL cases after code changes
   - Perform smoke testing before full regression

## Test Metrics to Track

- **Total Test Cases**: 60
- **Test Cases Executed**: ___
- **Test Cases Passed**: ___
- **Test Cases Failed**: ___
- **Pass Percentage**: (Passed/Executed) × 100 = ___%
- **Defects Found**: ___
- **Critical Defects**: ___
- **Defects Fixed**: ___

## Sign-off

| Role | Name | Signature | Date |
|------|------|-----------|------|
| Test Lead | | | |
| Developer | | | |
| Project Manager | | | |
