# Week 11 Build Task: Testing, Debugging, and Performance Hardening

## Objective

Turn LeafGuard AI into a tested,
measured,
and demonstrably reliable application. By the end of this build task,
you should have unit tests,
UI tests,
performance measurements,
leak detection,
and a final test report.

**Estimated Time:** 6-8 hours

---

## Step 1: Set Up Testing Dependencies

Add or confirm the following in your app-level Gradle file:

```gradle
android {
    defaultConfig {
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests.returnDefaultValues = true
    }
}

dependencies {
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.mockito:mockito-core:5.12.0'

    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation 'androidx.test.espresso:espresso-intents:3.5.1'

    debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.14'
}
```

### Checklist
- [ ] JUnit added
- [ ] Mockito added
- [ ] Espresso added
- [ ] LeakCanary added
- [ ] Gradle sync succeeds
- [ ] App still builds

---

## Step 2: Write Unit Tests for ImageUtils / Formatting Helpers

Good targets include:
- confidence formatting
- image filename creation
- basic validation methods
- summary text formatting

### Example
```java
public class ConfidenceFormatter {
    public static String format(float confidence) {
        if (confidence < 0.0f || confidence > 1.0f) {
            throw new IllegalArgumentException("Confidence must be between 0 and 1");
        }
        return String.format(java.util.Locale.US, "%.1f%%", confidence * 100.0f);
    }
}
```

```java
public class ConfidenceFormatterTest {
    @Test
    public void format_returnsOneDecimalPercentage() {
        assertEquals("87.3%", ConfidenceFormatter.format(0.8734f));
    }
}
```

### Checklist
- [ ] Test class created in `src/test/java`
- [ ] At least 3 assertions written
- [ ] Tests pass

---

## Step 3: Write a Unit Test for DiseaseClassifier Result Parsing

Test the logic that interprets model scores rather than the whole TFLite runtime.

### Suggested Logic
```java
public class DiseaseResultParser {
    public static String getTopLabel(float[] scores, String[] labels) {
        if (scores == null || labels == null || scores.length == 0 || labels.length == 0 || scores.length != labels.length) {
            throw new IllegalArgumentException("Invalid scores or labels");
        }
        int maxIndex = 0;
        for (int i = 1; i < scores.length; i++) {
            if (scores[i] > scores[maxIndex]) {
                maxIndex = i;
            }
        }
        return labels[maxIndex];
    }
}
```

### Checklist
- [ ] Highest-score test written
- [ ] At least one edge-case test written
- [ ] Tests pass

---

## Step 4: Write an Espresso Test for MainActivity

### Example
```java
@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void cameraButton_isVisibleAndCanBeTapped() {
        onView(withId(R.id.buttonCaptureImage))
                .check(matches(isDisplayed()))
                .perform(click());
    }
}
```

### Checklist
- [ ] Test file created in `src/androidTest/java`
- [ ] Emulator/device runs the test
- [ ] At least one assertion passes

---

## Step 5: Add LeakCanary for Memory Leak Detection

1. Confirm LeakCanary is in `debugImplementation`.
2. Run a debug build.
3. Open and close major screens several times.
4. Rotate if relevant.
5. Record whether a leak was reported.

### Checklist
- [ ] LeakCanary active in debug build
- [ ] Screen open/close cycles tested
- [ ] Leak status documented

---

## Step 6: Performance Measurement - 10 Cloud + 10 Offline Predictions

Use the same image and consistent conditions.
Suggested timing approach:

```java
long start = android.os.SystemClock.elapsedRealtime();
// run prediction
long duration = android.os.SystemClock.elapsedRealtime() - start;
```

### Results Table

| Mode | Run 1 | Run 2 | Run 3 | Run 4 | Run 5 | Run 6 | Run 7 | Run 8 | Run 9 | Run 10 | Average |
|------|------|------|------|------|------|------|------|------|------|------|------|
| Offline | | | | | | | | | | | |
| Cloud | | | | | | | | | | | |

### Checklist
- [ ] 10 offline runs recorded
- [ ] 10 cloud runs recorded
- [ ] Average calculated
- [ ] Comparison paragraph written

---

## Step 7: Create a Comprehensive Test Report Document

Include:
1. device/emulator used
2. Android version
3. list of unit tests
4. list of Espresso tests
5. 20 manual test cases
6. 3 bugs found and fixed
7. performance benchmark table
8. memory leak summary
9. final readiness conclusion

### Checklist
- [ ] Test report created
- [ ] Manual cases included
- [ ] Bugs documented
- [ ] Performance results included
- [ ] Leak summary included

---

## Step 8: Fix All Discovered Bugs Before Final Submission

Prioritize fixes in this order:
1. crashes
2. incorrect prediction-flow logic
3. memory leaks
4. performance bottlenecks
5. smaller UI issues

After each fix,
retest the failing scenario and update the report.

### Checklist
- [ ] Crash bugs fixed
- [ ] High-priority issues retested
- [ ] Final demo flow passes

---

## Complete Checklist

### Setup
- [ ] Dependencies configured
- [ ] Gradle sync successful

### Unit Tests
- [ ] Minimum 3 local unit tests passing
- [ ] Mockito used in at least 1 test

### UI Tests
- [ ] Minimum 1 Espresso test passing

### Debugging and Performance
- [ ] 3 bugs documented
- [ ] 10 offline measurements recorded
- [ ] 10 cloud measurements recorded
- [ ] LeakCanary checked

### Documentation
- [ ] Test report completed
- [ ] Reflection completed
- [ ] Validation checklist completed
