# Week 11: Exercises - Testing, Debugging, and Performance

## Overview

These 8 exercises are designed to make you practice Week 11 actively,
not just read about it. Each one maps directly to the testing,
debugging,
or performance skills required for a strong LeafGuard AI submission.

**Recommended total time:** 6-7 hours

---

## Exercise 1: Add Log Statements to Every Major Method in Your App

**Estimated Time:** 30 minutes

### Objective
Add meaningful `Log` statements to the most important parts of your application.

### Suggested Targets
- `onCreate()` in major Activities
- camera/gallery handlers
- image preprocessing
- offline inference start and end
- cloud request start and end
- database save operation
- result display logic

### Instructions
1. Add `private static final String TAG = "ClassName";` in each class.
2. Use `Log.i()` for major lifecycle or completion events.
3. Use `Log.d()` for useful state such as image size or confidence value.
4. Use `Log.w()` for suspicious but recoverable conditions.
5. Use `Log.e()` inside failure paths and catch blocks.
6. Run the app and verify the logs appear in Logcat.

### Deliverables
- screenshot of Logcat showing your tags
- short note describing why your messages are useful

### Verification
- [ ] At least 5 major methods contain logs
- [ ] Logs use meaningful tags and messages
- [ ] You can filter by at least one class tag

---

## Exercise 2: Deliberately Cause a NullPointerException, Read the Stack Trace, and Fix It

**Estimated Time:** 45 minutes

### Objective
Practice reading a crash without panic.

### Instructions
1. Add a temporary,
   safe test bug in a practice branch or clearly temporary section.
2. For example,
   create a null `Bitmap` and call a method on it.
3. Run the app and trigger the crash.
4. Open Logcat and copy the stack trace.
5. Identify:
   - exception type
   - first line from your package
   - file name
   - line number
6. Explain what each part means.
7. Fix the bug properly.
8. Re-run the same scenario to prove the fix works.

### Deliverables
- screenshot of the crash in Logcat
- short explanation of the stack trace
- note showing how the fix was verified

### Verification
- [ ] You identified the exact crash line
- [ ] You explained the exception clearly
- [ ] You fixed and re-tested the issue

---

## Exercise 3: Write a JUnit Test for a Confidence Formatting Utility Method

**Estimated Time:** 45 minutes

### Objective
Create your first real local unit test.

### Instructions
1. Create or reuse a `ConfidenceFormatter` helper.
2. Create `ConfidenceFormatterTest` in `src/test/java`.
3. Write at least 3 tests:
   - normal value like `0.8734f`
   - zero value
   - invalid input or edge value
4. Run the tests in Android Studio.
5. If available,
   run `./gradlew test` in your Android app project.

### Deliverables
- screenshot of green test results
- code snippet of the test class

### Verification
- [ ] Test is in `src/test/java`
- [ ] At least 3 assertions exist
- [ ] Tests pass consistently

---

## Exercise 4: Write a JUnit Test with Mockito for a Fake API Response

**Estimated Time:** 1.5 hours

### Objective
Learn to isolate logic from external dependencies.

### Instructions
1. Add Mockito dependency to Gradle.
2. Create an interface such as `PredictionGateway`.
3. Create a small use-case class that depends on it.
4. Mock the gateway in a test using `mock()` and `when(...).thenReturn(...)`.
5. Return a fake `PredictionResponse`.
6. Assert the summary text or business result exactly.

### Deliverables
- screenshot of passing Mockito test
- code snippet using `mock()` and `when()`
- short explanation of why the test does not need internet

### Verification
- [ ] Mockito is configured
- [ ] Real network is not used
- [ ] Test behavior is controlled by a fake response

---

## Exercise 5: Write an Espresso Test for MainActivity -> Camera Button Tap

**Estimated Time:** 1 hour

### Objective
Automate a simple but important UI interaction.

### Instructions
1. Add Espresso dependencies to `androidTestImplementation`.
2. Confirm the instrumentation runner is configured.
3. Create `MainActivityEspressoTest` in `src/androidTest/java`.
4. Launch `MainActivity`.
5. Check that the camera button is visible.
6. Perform a click on the camera button.
7. If possible,
   extend the test to verify navigation or a result state.

### Deliverables
- screenshot of emulator running the test
- screenshot of passing test result
- code snippet of the Espresso test

### Verification
- [ ] Test runs from `androidTest`
- [ ] At least one `check(matches(...))` exists
- [ ] At least one UI action is performed

---

## Exercise 6: Use Memory Profiler to Take a Heap Dump During App Use

**Estimated Time:** 30 minutes

### Objective
Get comfortable with Android Studio memory investigation tools.

### Instructions
1. Launch the app on a device or emulator.
2. Open **View -> Tool Windows -> Profiler**.
3. Select Memory Profiler.
4. Use the app normally:
   - open main screen
   - choose image
   - run prediction
   - open result screen
   - go back
5. Click **Dump Java Heap**.
6. Inspect large objects like `Bitmap` or unexpected `Activity` instances.

### Deliverables
- screenshot of the heap dump view
- short note about what object types looked largest

### Verification
- [ ] Heap dump captured
- [ ] At least one observation recorded

---

## Exercise 7: Add LeakCanary to Detect Memory Leaks

**Estimated Time:** 45 minutes

### Objective
Use a professional leak-detection tool during development.

### Instructions
1. Add LeakCanary as `debugImplementation`.
2. Build and run the debug app.
3. Open and close MainActivity,
   ResultActivity,
and HistoryActivity.
4. Rotate the device if your app supports it.
5. Watch for LeakCanary notifications or reports.
6. If a leak appears,
   document the leaking object,
   reference chain,
   and likely fix.

### Deliverables
- screenshot of Gradle dependency
- screenshot of LeakCanary output or no-leak note
- one paragraph about a possible leak pattern in your app

### Verification
- [ ] LeakCanary runs in debug build
- [ ] At least 3 screen cycles tested
- [ ] Leak status documented

---

## Exercise 8: Benchmark Offline vs Cloud Inference 10 Times Each and Report Results

**Estimated Time:** 1 hour

### Objective
Collect real performance evidence for discussion and viva defense.

### Instructions
1. Use the same image for all runs.
2. Measure offline inference 10 times.
3. Measure cloud inference 10 times.
4. Record each duration in milliseconds.
5. Calculate:
   - average
   - fastest run
   - slowest run
6. Write a short comparison paragraph.

### Suggested Results Table

| Mode | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | Average |
|------|------|------|------|------|------|------|------|------|------|------|------|
| Offline | | | | | | | | | | | |
| Cloud | | | | | | | | | | | |

### Deliverables
- completed timing table
- one paragraph comparing both modes
- screenshot or log snippet showing timing measurement

### Verification
- [ ] 10 offline runs recorded
- [ ] 10 cloud runs recorded
- [ ] Average calculated for both

---


---

## Detailed Starter Pack for Exercises 3-8

The file already contains eight exercises, so this section expands the most technical ones with starter code, reporting templates, and reflection prompts. Use these pages as your self-contained lab guide while working through Week 11.

### Exercise 3 Support: Confidence Formatter Unit Test Walkthrough

#### Suggested Helper Class

```java
public final class ConfidenceFormatter {

    private ConfidenceFormatter() {
    }

    public static String format(float confidence) {
        if (confidence < 0.0f || confidence > 1.0f) {
            throw new IllegalArgumentException("Confidence must be between 0 and 1");
        }
        return String.format(Locale.US, "%.1f%%", confidence * 100.0f);
    }
}
```

#### Suggested Test Class

```java
public class ConfidenceFormatterTest {

    @Test
    public void format_returnsOneDecimalPercentage() {
        assertEquals("87.3%", ConfidenceFormatter.format(0.8734f));
    }

    @Test
    public void format_returnsZeroPercentForZero() {
        assertEquals("0.0%", ConfidenceFormatter.format(0.0f));
    }

    @Test(expected = IllegalArgumentException.class)
    public void format_throwsForNegativeValue() {
        ConfidenceFormatter.format(-0.1f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void format_throwsForValueAboveOne() {
        ConfidenceFormatter.format(1.5f);
    }
}
```

#### What Each Assertion Teaches You

- first test checks normal formatting logic
- second test checks a lower boundary value
- third and fourth tests confirm that invalid inputs are rejected

#### Reflection Questions

1. Why is it useful to test both valid and invalid input?
2. What bug might happen if the formatter accepted `1.5f` silently?
3. Why does this kind of logic belong in a local unit test rather than an Espresso test?

---

### Exercise 4 Support: Mockito Test for a Fake API Response

#### Simple Interface to Mock

```java
public interface PredictionGateway {
    PredictionResponse predict(byte[] imageBytes) throws IOException;
}
```

```java
public class PredictionResponse {
    private final String disease;
    private final float confidence;

    public PredictionResponse(String disease, float confidence) {
        this.disease = disease;
        this.confidence = confidence;
    }

    public String getDisease() {
        return disease;
    }

    public float getConfidence() {
        return confidence;
    }
}
```

#### Small Use-Case Class Under Test

```java
public class PredictionSummaryUseCase {

    private final PredictionGateway gateway;

    public PredictionSummaryUseCase(PredictionGateway gateway) {
        this.gateway = gateway;
    }

    public String buildSummary(byte[] imageBytes) throws IOException {
        PredictionResponse response = gateway.predict(imageBytes);
        return response.getDisease() + " (" + Math.round(response.getConfidence() * 100) + "%)";
    }
}
```

#### Mockito Test Example

```java
@RunWith(MockitoJUnitRunner.class)
public class PredictionSummaryUseCaseTest {

    @Mock
    private PredictionGateway gateway;

    @Test
    public void buildSummary_returnsExpectedTextFromFakeResponse() throws Exception {
        byte[] fakeImage = new byte[]{1, 2, 3};
        when(gateway.predict(fakeImage)).thenReturn(new PredictionResponse("Healthy", 0.97f));

        PredictionSummaryUseCase useCase = new PredictionSummaryUseCase(gateway);
        String summary = useCase.buildSummary(fakeImage);

        assertEquals("Healthy (97%)", summary);
        verify(gateway).predict(fakeImage);
    }

    @Test(expected = IOException.class)
    public void buildSummary_throwsWhenGatewayFails() throws Exception {
        byte[] fakeImage = new byte[]{9, 9, 9};
        when(gateway.predict(fakeImage)).thenThrow(new IOException("Server timeout"));

        PredictionSummaryUseCase useCase = new PredictionSummaryUseCase(gateway);
        useCase.buildSummary(fakeImage);
    }
}
```

#### Why This Is Powerful

You tested your business logic without:
- internet
- FastAPI server
- Retrofit setup
- emulator

That is the value of mocking.

---

### Exercise 5 Support: Espresso Starter for MainActivity

#### Suggested Test Class

```java
@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void cameraButton_isVisible() {
        onView(withId(R.id.btnCamera))
                .check(matches(isDisplayed()));
    }

    @Test
    public void galleryButton_isVisible() {
        onView(withId(R.id.btnGallery))
                .check(matches(isDisplayed()));
    }

    @Test
    public void tappingCameraButton_doesNotCrash() {
        onView(withId(R.id.btnCamera)).perform(click());
    }
}
```

#### Espresso Syntax Cheat Sheet

| Action | Example |
|--------|---------|
| find a view | `onView(withId(R.id.btnCamera))` |
| click it | `.perform(click())` |
| assert visible | `.check(matches(isDisplayed()))` |
| assert text | `.check(matches(withText("Healthy")))` |

#### Common Espresso Errors

- **NoMatchingViewException**
  - means the target view was not found
  - check the view ID and current screen
- **PerformException**
  - often means the view is off screen, disabled, or covered
- **AppNotIdleException**
  - means the app is still doing work and Espresso is waiting

#### Reflection Questions

1. Why is "does not crash" still a valid beginner UI test?
2. What is missing from this test if you want to verify the full scan flow?
3. Why is `ActivityScenarioRule` preferred over very old testing APIs?

---

### Exercise 6 Support: Heap Dump Observation Worksheet

Use this worksheet while inspecting the Memory Profiler.

| Observation Area | What to Check | Your Notes |
|------------------|--------------|------------|
| Number of Activity instances | Do old screens disappear after back press? | |
| Bitmap objects | Are multiple large bitmaps alive at once? | |
| Byte arrays | Do upload/compression operations create large arrays repeatedly? | |
| RecyclerView adapters | Are adapters retained after leaving the screen? | |
| Database objects | Is Room behaving normally without huge growth? | |

#### Interpretation Prompts

- Which object category took the most memory?
- Was that expected?
- Which allocation looks wasteful?
- Can you fix it by downsampling, clearing references, or moving work off the main thread?

#### Example Observation Paragraph

> During the heap dump, the largest objects were camera bitmaps created during image preview. Two bitmap instances existed at the same time because the full-size image was decoded before creating the model-sized copy. After introducing `inSampleSize` and scaling earlier, memory usage dropped noticeably.

---

### Exercise 7 Support: LeakCanary Investigation Worksheet

#### Leak Report Template

| Item | Details |
|------|---------|
| Screen tested | |
| Action sequence | |
| Leak found? | Yes / No |
| Leaking class | |
| Reference chain summary | |
| Proposed fix | |
| Verified after fix? | |

#### Common Places to Inspect in LeafGuard

- Activity fields storing adapters or listeners
- static helper classes holding `Context`
- long-running executors not shut down
- dialogs shown after Activity is finishing
- image-processing callbacks that outlive the screen

#### Example No-Leak Note

> LeakCanary was run on MainActivity, ResultActivity, and HistoryActivity. No retained Activity leak was reported after repeated open-close cycles and one device rotation test. This suggests the current Activity lifecycle cleanup is acceptable for submission.

---

### Exercise 8 Support: Benchmark Worksheet and Calculation Guide

#### Raw Timing Table

| Run | Offline (ms) | Cloud (ms) |
|-----|--------------|------------|
| 1 | | |
| 2 | | |
| 3 | | |
| 4 | | |
| 5 | | |
| 6 | | |
| 7 | | |
| 8 | | |
| 9 | | |
| 10 | | |

#### Summary Table

| Metric | Offline | Cloud |
|--------|---------|-------|
| Fastest | | |
| Slowest | | |
| Average | | |

#### How to Calculate Average

```text
Average = (run1 + run2 + run3 + ... + run10) / 10
```

#### Example Interpretation Paragraph

> Offline inference was consistently faster than cloud inference because the image stayed on the device and did not need upload or server processing. Cloud inference remained useful as a fallback strategy, but the timing data shows that TFLite is better for low-latency prediction when the model is already bundled with the app.

#### Follow-Up Questions

1. Was the fastest mode also the most stable mode?
2. Did network quality affect cloud timing a lot?
3. If cloud predictions were more accurate, how would you justify the slower speed?

---

## Self-Assessment Rubric for Week 11 Exercises

Use this checklist before claiming Week 11 is complete.

### Testing Skills
- [ ] I can explain the difference between local unit tests and instrumented tests.
- [ ] I wrote at least one assertion that checks expected output exactly.
- [ ] I used a fake or mocked dependency in at least one test.

### Debugging Skills
- [ ] I can read the first important line of a stack trace.
- [ ] I can identify whether a bug is caused by null data, thread misuse, or wrong assumptions.
- [ ] I documented at least one real bug and fix.

### Performance Skills
- [ ] I captured profiler evidence instead of guessing.
- [ ] I can explain why large bitmaps are dangerous.
- [ ] I can compare offline and cloud timings with numbers.

### Report-Writing Skills
- [ ] I have screenshots for major tests.
- [ ] I have a table of results, not just paragraphs.
- [ ] My conclusions are based on evidence from the app.

---

## Suggested Submission Evidence for Exercises 3-8

Collect these items while you work so Week 12 report writing is easier.

| Exercise | Recommended Evidence |
|----------|----------------------|
| Exercise 3 | JUnit results panel + test code snippet |
| Exercise 4 | Mockito test screenshot + explanation paragraph |
| Exercise 5 | Espresso pass result + emulator screenshot |
| Exercise 6 | Heap dump screenshot + short memory note |
| Exercise 7 | LeakCanary report or no-leak statement |
| Exercise 8 | Benchmark table + timing log snippet |

### Minimal Evidence Folder Checklist

- [ ] one screenshot from Android Studio test results
- [ ] one Logcat screenshot showing timing or debugging information
- [ ] one profiler screenshot
- [ ] one screenshot of a working app feature after a test

---

## Completion Summary

- [ ] All 8 exercises attempted
- [ ] Evidence collected for each major exercise
- [ ] At least 3 unit tests written
- [ ] At least 1 Espresso test run
- [ ] Performance table completed


<!-- NAV_FOOTER_START -->

---

## 📚 Week 11 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| **3** | **exercises.md** ← *You are here* | **Practice Exercises** |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Theory & Learning Notes](learning-notes.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Practice Exercises** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Build Implementation Guide →](build-task.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 10: Notifications, Share & Location](../week-10-notifications-share-location/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 12: Final Submission ➡](../week-12-final-submission/README.md) |

---
