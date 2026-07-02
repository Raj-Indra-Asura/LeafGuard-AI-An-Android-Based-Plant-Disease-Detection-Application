# LeafGuard AI - Test Case Template

## How to Use This Template

Fill in the "Actual Result" and "Status" (PASS/FAIL) columns as you test.
Add screenshots and notes in the last column.
Reference this in your Week 11 testing work and in your final report.

---

## Section 1: Functional Test Cases

### 1.1 Image Input (Camera)

| ID | Feature | Description | Input / Action | Expected Result | Actual Result | Status | Notes |
|----|---------|-------------|---------------|-----------------|---------------|--------|-------|
| TC-001 | Camera | Launch camera with permission granted | Grant camera permission, tap "📷 Take Photo" | Camera app opens for capture | | | |
| TC-002 | Camera | Capture leaf image | Aim at a leaf, press shutter button | Captured image displays in the MainActivity preview ImageView | | | |
| TC-003 | Camera | Cancel camera without capturing | Open camera, press Back | Returns to MainActivity; no image displayed; Analyze button stays disabled | | | |
| TC-004 | Camera | Camera permission denied first time | Deny camera permission dialog | Permission rationale message or Snackbar shown; camera does NOT open | | | |
| TC-005 | Camera | Camera permission permanently denied | Deny twice (or "Don't ask again") | Snackbar with "Open Settings" action shown | | | |
| TC-006 | Camera | Repeat capture (replace previous photo) | Capture image A, then tap Camera again and capture image B | ImageView shows image B; Analyze button still enabled | | | |

### 1.2 Image Input (Gallery)

| ID | Feature | Description | Input / Action | Expected Result | Actual Result | Status | Notes |
|----|---------|-------------|---------------|-----------------|---------------|--------|-------|
| TC-010 | Gallery | Open gallery and select an image | Tap "🖼️ Choose from Gallery", select any leaf photo | Selected image displays in the MainActivity preview ImageView | | | |
| TC-011 | Gallery | Cancel gallery without selecting | Open gallery picker, press Back | Returns to MainActivity; previously displayed state unchanged | | | |
| TC-012 | Gallery | Select a non-JPEG image (PNG, HEIC) | Choose a PNG file from gallery | Image displays without crash | | | |
| TC-013 | Gallery | Select a very large image (>5MP) | Choose a high-resolution photo | App does NOT crash (OutOfMemoryError absent); image scaled to 224×224 | | | |

### 1.3 Cloud Disease Detection (Online)

| ID | Feature | Description | Input / Action | Expected Result | Actual Result | Status | Notes |
|----|---------|-------------|---------------|-----------------|---------------|--------|-------|
| TC-020 | Cloud API | Detect disease from valid leaf photo | Select a clear tomato leaf photo, tap Analyze (server running) | ResultActivity opens with disease name + confidence ≥50% | | | |
| TC-021 | Cloud API | Detect healthy leaf | Select a known healthy tomato leaf image | Disease shows "Tomato: Healthy" (or similar); confidence ≥60% | | | |
| TC-022 | Cloud API | Handle no internet connection | Disable Wi-Fi/data, tap Analyze | Error dialog shown offering offline fallback; no crash | | | |
| TC-023 | Cloud API | Handle server timeout | Block server (wrong IP), tap Analyze | Timeout error shown after ≤30 sec; user can choose offline | | | |
| TC-024 | Cloud API | Handle HTTP 500 server error | Trigger server error (e.g., corrupt model), tap Analyze | "Server error" message shown; option to retry or go offline | | | |
| TC-025 | Cloud API | Show loading indicator during upload | Select image, tap Analyze | ProgressBar visible; Analyze button disabled during request | | | |

### 1.4 Offline Disease Detection (TFLite)

| ID | Feature | Description | Input / Action | Expected Result | Actual Result | Status | Notes |
|----|---------|-------------|---------------|-----------------|---------------|--------|-------|
| TC-030 | Offline AI | Run offline inference when no internet | Disable internet, select leaf photo, tap Analyze | TFLite model runs; ResultActivity shows disease within 5 sec | | | |
| TC-031 | Offline AI | Low confidence offline result | Use an ambiguous/bad image for offline inference | Low confidence (<50%) warning shown; top-3 alternatives displayed | | | |
| TC-032 | Offline AI | Offline inference does not block UI | Start offline inference | ProgressBar shows; UI remains responsive (no ANR) | | | |
| TC-033 | Offline AI | Model not loaded (tflite file missing) | Remove model from assets, run offline | Graceful error: "Offline model unavailable" toast/dialog | | | |

### 1.5 Disease Result Display

| ID | Feature | Description | Input / Action | Expected Result | Actual Result | Status | Notes |
|----|---------|-------------|---------------|-----------------|---------------|--------|-------|
| TC-040 | Result UI | Disease name displayed | Complete any scan | Disease name visible in ResultActivity title/heading | | | |
| TC-041 | Result UI | Confidence percentage displayed | Complete any scan | Confidence shown as "xx.x%" (e.g., "87.3%") | | | |
| TC-042 | Result UI | Symptoms from XML displayed | Complete scan for a disease in XML | Symptoms text visible (not empty, not "null") | | | |
| TC-043 | Result UI | Treatment from XML displayed | Complete scan for a disease in XML | Treatment text visible | | | |
| TC-044 | Result UI | Prevention from XML displayed | Complete scan for a disease in XML | Prevention text visible | | | |
| TC-045 | Result UI | Missing XML entry handled | Get prediction for a disease NOT in XML | Fallback text "Information not available" shown; no crash | | | |
| TC-046 | Result UI | Leaf image shown in result | Complete any scan | Scanned leaf image thumbnail visible in ResultActivity | | | |

### 1.6 Scan History (Room Database)

| ID | Feature | Description | Input / Action | Expected Result | Actual Result | Status | Notes |
|----|---------|-------------|---------------|-----------------|---------------|--------|-------|
| TC-050 | History | Save scan to history | In ResultActivity, tap "Save to History" | Toast "Saved" shown; scan appears in HistoryActivity | | | |
| TC-051 | History | View scan history list | Open HistoryActivity | List shows all saved scans in newest-first order | | | |
| TC-052 | History | Scan history persists after restart | Save a scan, close app completely, reopen, view History | Saved scan still present in list | | | |
| TC-053 | History | Empty history state | Open HistoryActivity with no scans saved | Empty state message "No scans yet" shown; not blank screen | | | |
| TC-054 | History | Tap history item | Tap a scan in the history list | ResultActivity opens with that scan's data | | | |
| TC-055 | History | Delete a scan | Long-press history item, confirm delete | Scan removed from list; Room DB updated | | | |
| TC-056 | History | LiveData updates immediately | Save a scan while HistoryActivity is open | List updates automatically without needing to restart Activity | | | |

### 1.7 XML Disease Library

| ID | Feature | Description | Input / Action | Expected Result | Actual Result | Status | Notes |
|----|---------|-------------|---------------|-----------------|---------------|--------|-------|
| TC-060 | Disease Library | Open disease library | Tap "📚 Disease Library" on main screen | DiseaseLibraryActivity opens with disease list | | | |
| TC-061 | Disease Library | All diseases from XML appear in list | Open DiseaseLibraryActivity | All entries from diseases.xml appear as list items | | | |
| TC-062 | Disease Library | Tap on a disease | Tap a disease in the list | Detailed view shows symptoms, treatment, prevention | | | |
| TC-063 | Disease Library | Search by disease name | Type "tomato" in search bar | List filters to show only tomato diseases | | | |
| TC-064 | Disease Library | XML parse error handled | Corrupt diseases.xml, open library | Error message shown; app does NOT crash | | | |

### 1.8 Notifications

| ID | Feature | Description | Input / Action | Expected Result | Actual Result | Status | Notes |
|----|---------|-------------|---------------|-----------------|---------------|--------|-------|
| TC-070 | Notification | Disease detected notification sent | Complete a successful scan | Notification appears in system tray with disease name | | | |
| TC-071 | Notification | Tap notification opens ResultActivity | Complete scan, then tap notification | App opens to ResultActivity for that scan | | | |
| TC-072 | Notification | Notification permission (Android 13+) | First scan on Android 13+ device | Permission dialog for POST_NOTIFICATIONS shown | | | |

### 1.9 Share Functionality

| ID | Feature | Description | Input / Action | Expected Result | Actual Result | Status | Notes |
|----|---------|-------------|---------------|-----------------|---------------|--------|-------|
| TC-080 | Share | Share scan result as text | In ResultActivity, tap "Share" | Android Share Sheet opens with disease info text | | | |
| TC-081 | Share | Share includes disease name and confidence | Share result | Shared text contains disease name and confidence percentage | | | |
| TC-082 | Share | Share image option | Share with image option selected | Leaf image attached to shared content | | | |

---

## Section 2: Navigation Test Cases

| ID | Feature | Description | Input / Action | Expected Result | Actual Result | Status | Notes |
|----|---------|-------------|---------------|-----------------|---------------|--------|-------|
| TC-090 | Navigation | Main → camera capture | Tap Camera button | System camera opens from MainActivity | | | |
| TC-091 | Navigation | Main → HistoryActivity | Tap History button | HistoryActivity opens with scan list | | | |
| TC-092 | Navigation | Main → DiseaseLibraryActivity | Tap Disease Library button | DiseaseLibraryActivity opens | | | |
| TC-093 | Navigation | Back from camera | Press Back in the camera | Returns to MainActivity | | | |
| TC-094 | Navigation | Back from ResultActivity | Press Back in ResultActivity | Returns to MainActivity | | | |
| TC-095 | Navigation | Rapid navigation (no double-open) | Quickly double-tap Camera button | Only one camera instance opens | | | |

---

## Section 3: Edge Cases

| ID | Feature | Description | Input / Action | Expected Result | Actual Result | Status | Notes |
|----|---------|-------------|---------------|-----------------|---------------|--------|-------|
| TC-100 | Edge Case | Rotate screen mid-scan | Start upload, rotate device | Upload completes; result shown correctly (not lost) | | | |
| TC-101 | Edge Case | Analyze with no image selected | Tap Analyze before selecting image | Analyze button is disabled; no crash | | | |
| TC-102 | Edge Case | Very dark image | Use a nearly black image for scan | Low confidence result OR "Retake in better light" guidance | | | |
| TC-103 | Edge Case | Non-plant image | Use a photo of a car or person | Prediction with low confidence; no crash | | | |
| TC-104 | Edge Case | Multiple rapid taps on Analyze | Tap Analyze 5 times quickly | Only one API call made; no duplicate results | | | |
| TC-105 | Edge Case | App in background during scan | Start scan, press Home, return | Scan completes; result displayed when app is foregrounded | | | |

---

## Section 4: Performance Test Cases

| ID | Feature | Description | Target | Actual | Pass? | Notes |
|----|---------|-------------|--------|--------|-------|-------|
| TC-110 | Performance | App launch time (cold start) | < 3 seconds | | | Measure with Logcat timer |
| TC-111 | Performance | Image load time in MainActivity preview | < 1 second | | | From camera return to ImageView display |
| TC-112 | Performance | Cloud prediction response time | < 5 seconds | | | From tap Analyze to ResultActivity open |
| TC-113 | Performance | Offline TFLite inference time | < 3 seconds | | | Measure with `System.currentTimeMillis()` |
| TC-114 | Performance | History list load time (50 items) | < 500ms | | | Time from Activity start to list visible |
| TC-115 | Performance | Memory usage during inference | < 80 MB | | | Android Studio Memory Profiler |

---

## Section 5: Backend API Test Cases

| ID | Endpoint | Description | Input | Expected Response | Actual | Status |
|----|---------|-------------|-------|------------------|--------|--------|
| TC-120 | GET / | Health check | GET http://localhost:8000/ | 200 OK, JSON with status:"ok" | | |
| TC-121 | POST /predict | Valid JPEG image | JPEG leaf image | 200 OK, disease + confidence JSON | | |
| TC-122 | POST /predict | Valid PNG image | PNG leaf image | 200 OK, disease + confidence JSON | | |
| TC-123 | POST /predict | No file attached | Empty multipart | 422 Unprocessable Entity | | |
| TC-124 | POST /predict | Text file (not image) | .txt file | 400 Bad Request or error JSON | | |
| TC-125 | POST /predict | Very large image (10MB) | 10MB JPEG | Processes within 30 sec OR 413 error | | |
| TC-126 | GET /diseases | List all diseases | GET /diseases | 200 OK, list of disease names | | |

---

## How to Document Test Results

1. **Run each test case** from top to bottom
2. **Fill in "Actual Result"** — describe exactly what happened
3. **Mark Status**: PASS, FAIL, SKIP (N/A for your setup), or BLOCKED
4. **Add screenshots** in the Notes column (or link to evidence folder)
5. **For FAIL**: note the bug in your reflection journal and attempt to fix

## Test Evidence Folder

Save screenshots and logs to: `docs/evidence/`

Naming convention: `TC-001-screenshot.png`, `TC-020-logcat.txt`

## Pass Criteria for Submission

- All TC-001 to TC-095 (functional + navigation): **PASS**
- Performance TC-110 to TC-115: at least 4 of 6 **PASS**
- Backend TC-120 to TC-126: all **PASS**
- Edge cases TC-100 to TC-105: at least 4 of 6 **PASS**
