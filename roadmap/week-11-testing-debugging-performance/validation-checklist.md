# Week 11: [Testing and Debugging Content]

## Test Case Template

| Test ID | Feature | Input | Expected | Actual | Status |
|---------|---------|-------|----------|--------|--------|
| TC001 | Camera | Tap camera button | Camera opens | Camera opens | PASS |
| TC002 | Prediction | Valid image | Result displayed | Result displayed | PASS |
| TC003 | No Internet | Upload in airplane mode | Error message | Error message shown | PASS |
| TC004 | Empty History | Open history | "No scans" message | Message shown | PASS |
| TC005 | Delete Scan | Tap delete, confirm | Scan removed | Scan removed | PASS |

## Debugging Documentation Format

1. **Issue:** Describe the problem
2. **Error Message:** Copy from Logcat
3. **Investigation:** What you checked
4. **Root Cause:** What was wrong
5. **Fix:** How you resolved it
6. **Verification:** How you confirmed fix

## Performance Metrics

- **Cloud Prediction:** Average ___ ms (test 10 times)
- **Offline Prediction:** Average ___ ms (test 10 times)
- **Database Query:** Average ___ ms
- **XML Parsing:** Average ___ ms
- **App Launch Time:** ___ seconds

## Edge Cases to Test

1. No internet connection
2. Invalid image format
3. Empty database
4. Corrupted XML file
5. Very large images
6. Rapid button clicking
7. Device rotation during operation
8. Low memory scenario
9. Permissions denied
10. Backend server down

