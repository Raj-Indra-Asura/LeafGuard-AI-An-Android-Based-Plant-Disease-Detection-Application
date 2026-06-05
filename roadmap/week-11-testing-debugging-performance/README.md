# Week 11: Testing, Debugging, and Performance

## Weekly Objective

Create comprehensive test suite, document debugging process, and measure performance.

**Measurable Outcomes:**
- Test case table with 20+ test cases
- All tests executed with pass/fail results
- Debugging log documenting 5+ issues fixed
- Edge cases tested (no internet, wrong image, etc.)
- Cloud vs offline latency comparison
- Performance metrics documented
- Evidence collected for all tests

---

## Why This Week Matters

**CSE 2206 Requirements:**
- **Testing** - Mandatory demonstration
- **Debugging** - Must show debugging process
- **Performance Analysis** - Expected in reports

**Teacher expects:** Comprehensive test table showing systematic testing approach.

**Viva Question:** "Show me one unit test and explain what it proves."

---

## Syllabus Topics

1. **Testing** - Unit tests (JUnit), integration tests, UI tests (Espresso)
2. **Debugging** - Logcat, breakpoints, error handling
3. **Performance** - Latency measurement, optimization

---

## Key Concepts

### Android Testing Pyramid

```
         /‾‾‾‾‾‾‾‾‾‾‾‾‾\
        /   UI Tests     \     ← Espresso (slow, end-to-end)
       /‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\
      / Integration Tests  \   ← Robolectric (medium)
     /‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\
    /    Unit Tests          \  ← JUnit (fast, isolated)
   /‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾\
```

Run more unit tests and fewer UI tests — unit tests are faster and give more precise failure messages.

### JUnit 4 Unit Test

```java
// app/src/test/java/com/leafguard/DiseaseParserTest.java
import org.junit.Test;
import static org.junit.Assert.*;

public class DiseaseParserTest {

    @Test
    public void parseConfidence_returnsPercentage() {
        float raw = 0.93f;
        int percent = Math.round(raw * 100);
        assertEquals(93, percent);
    }

    @Test
    public void diseaseLabel_isNotEmpty() {
        String label = "Tomato Late Blight";
        assertFalse("Disease label must not be empty", label.isEmpty());
    }
}
```

Run with: **Android Studio → Run → Run Tests** or `./gradlew test`.

### Espresso UI Test

```java
// app/src/androidTest/java/com/leafguard/MainActivityTest.java
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
        new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void scanButton_isDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.btnScan))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void historyButton_navigatesToHistoryActivity() {
        Espresso.onView(ViewMatchers.withId(R.id.btnHistory))
            .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.rvHistory))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
```

Run with: **Android Studio → Run → Run Instrumented Tests** (requires emulator).

### Debugging with Logcat

```java
// Add structured logging in every Activity
private static final String TAG = "LeafGuard.ScanActivity";

// Verbose — development only
Log.v(TAG, "Image URI: " + uri);

// Debug — trace execution path
Log.d(TAG, "Calling /predict endpoint...");

// Info — major lifecycle events
Log.i(TAG, "Prediction received: " + result.getDisease());

// Warn — recoverable issues
Log.w(TAG, "Location null, saving scan without coordinates");

// Error — failures
Log.e(TAG, "Failed to connect to API", exception);
```

**Logcat filter example:** Set tag filter to `LeafGuard` to see only your app's messages.

### Latency Measurement

```java
long startTime = System.currentTimeMillis();

// ... run prediction (cloud or TFLite) ...

long latency = System.currentTimeMillis() - startTime;
Log.d(TAG, "Prediction latency: " + latency + " ms");
tvLatency.setText("Latency: " + latency + " ms");
```

Collect 5+ measurements for each mode (cloud / offline) and average them for the report.

### Test Case Table Template

| # | Test Name | Input | Expected | Actual | Pass/Fail |
|---|-----------|-------|----------|--------|-----------|
| 1 | Scan healthy leaf | Healthy tomato image | Confidence > 80% | | |
| 2 | No internet, offline mode | Diseased leaf + airplane mode | TFLite result shown | | |
| 3 | Back press from ResultActivity | History scan exists | History screen shows entry | | |
| 4 | Gallery picker on API 24 | Old device / emulator API 24 | Image loads, no crash | | |
| 5 | Share button | Valid scan result | Share sheet opens | | |

---

## Test Categories

1. **Functional Tests** - Each feature works as specified
2. **Edge Case Tests** - Boundary conditions (no image, corrupt file, empty history)
3. **Error Handling Tests** - Failures handled gracefully (no crashes, user-friendly messages)
4. **Performance Tests** - Speed measurements (cloud vs offline, image load time)
5. **Integration Tests** - Features work together (scan → save → view in history)

---

## Prerequisites

- All weeks 01–10 features implemented
- Emulator or physical device available
- Understanding of Logcat usage

---

## Weekly Timeline

- **Day 1-2:** Create test case table (4h)
- **Day 3-4:** Execute all tests, document results (6h)
- **Day 5:** Debugging documentation (3h)
- **Day 6:** Performance testing (3h)
- **Day 7:** Evidence collection and report (2h)

---

## Validation Criteria

- [ ] Test case table with 20+ cases
- [ ] All tests executed
- [ ] Results documented (pass/fail)
- [ ] Screenshots of test execution
- [ ] Debugging log with 5+ issues
- [ ] Performance metrics recorded
- [ ] Latency comparison complete
- [ ] Edge cases tested

---

**This week proves your app works reliably.**

**Next:** `learning-notes.md` for deeper testing patterns and debugging techniques.
