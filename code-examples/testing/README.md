# Testing Code Examples

## 1. JUnit test for confidence formatting

```java
public class FormatUtilsTest {
    @Test
    public void confidenceToPercent_formatsCorrectly() {
        assertEquals("87%", FormatUtils.confidenceToPercent(0.87f));
    }
}
```

## 2. Manual test case format

| ID | Feature | Steps | Expected result | Status |
|---|---|---|---|---|
| TC-01 | Camera permission | Tap Take Photo | Permission dialog appears | Pass/Fail |
| TC-02 | Gallery picker | Choose image | Preview is displayed | Pass/Fail |
| TC-03 | Backend prediction | Upload image | JSON result returned | Pass/Fail |
| TC-04 | Offline prediction | Disable internet and scan | TFLite result appears | Pass/Fail |
| TC-05 | History | Save result | Result appears in history | Pass/Fail |

## 3. Performance measurements

Record:

- app launch time,
- image loading time,
- backend response time,
- offline TFLite inference time,
- APK size,
- memory warnings or crashes.
