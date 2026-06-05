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

## Completion Summary

- [ ] All 8 exercises attempted
- [ ] Evidence collected for each major exercise
- [ ] At least 3 unit tests written
- [ ] At least 1 Espresso test run
- [ ] Performance table completed
