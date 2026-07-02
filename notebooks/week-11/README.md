# Week 11 Interactive Notebook

## Making LeafGuard Production-Ready

> Work through this Markdown notebook like a lab manual: read, run, test, and explain each checkpoint in your own words.

### How to use this notebook

- Follow the cells in order.
- Kotlin is the primary track for Android code (`android-app-kotlin/`, with a complete Java twin in `android-app/`); Python is used only for backend/model tooling.
- Save screenshots and logs as evidence for CSE 2206.
- Keep the roadmap folder for this week open while you work.

### Weekly outcomes

- Write JUnit and Espresso tests for core LeafGuard AI behavior.
- Use Android Profiler, LeakCanary, and StrictMode to find quality issues.
- Practice reading stack traces and documenting test results.

### Repository references

- `roadmap/week-11-testing-debugging-performance/`
- `android-app/app/build.gradle`
- `solutions/week-11/`

---

## Notebook Cell 1 — Set up JUnit

### Explanation

- JUnit is the standard Java testing framework for local unit tests.

### Code to Read / Run

```gradle
testImplementation 'junit:junit:4.13.2'
testImplementation 'org.mockito:mockito-core:5.7.0'
```

### 🔵 Try This

- Sync Gradle and create the `src/test/java` package structure if it is missing.

### Expected Output

- Your project can run local Java unit tests.

### ✅ Checkpoint

- Why are local unit tests faster than emulator tests?

### ⚠️ Common Mistake

- Do not place pure Java tests under `androidTest` if they do not need a device.

### 📌 Key Point

- Choose the lightest test type that can validate the behavior.

## Notebook Cell 2 — Write the first unit test for the XML parser

### Explanation

- Static content parsing is a great target for unit testing because it is deterministic.

### Code to Read / Run

```java
public class DiseaseXmlParserTest {

    @Test
    public void parse_returnsDiseases() throws Exception {
        String xml = "<diseases><disease id='tomato'><name>Tomato Early Blight</name><crop>Tomato</crop>"
                + "<symptoms>Brown spots</symptoms><treatment>Remove leaves</treatment>"
                + "<prevention>Rotate crops</prevention></disease></diseases>";

        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        List<DiseaseInfo> diseases = new DiseaseXmlParser().parse(inputStream);

        assertEquals(1, diseases.size());
        assertEquals("Tomato Early Blight", diseases.get(0).getName());
    }
}
```

### 🔵 Try This

- Add a second test for malformed XML or a missing tag.

### Expected Output

- JUnit reports the parser test as passed.

### ✅ Checkpoint

- Why is parser testing easier than testing camera capture?

### ⚠️ Common Mistake

- Do not depend on a real Android resource file if the parser logic can be tested with a simple string.

### 📌 Key Point

- Pure functions and parsers are excellent unit test targets.

## Notebook Cell 3 — Set up Espresso and write a first UI test

### Explanation

- Espresso verifies visible UI behavior on a device or emulator.

### Code to Read / Run

```java
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void cameraButton_isDisplayed() {
        onView(withId(R.id.cameraButton)).check(matches(isDisplayed()));
    }
}
```

### 🔵 Try This

- Add a second UI test that taps History and checks the next screen title.

### Expected Output

- The emulator runs the test and reports a passing assertion.

### ✅ Checkpoint

- When is Espresso more appropriate than JUnit?

### ⚠️ Common Mistake

- Do not write fragile UI tests that depend on unnecessary timing sleeps.

### 📌 Key Point

- UI tests should verify user-visible behavior, not implementation details.

## Notebook Cell 4 — Use Android Profiler

### Explanation

- Profiler tools help you inspect CPU, memory, and network behavior while the app runs.

### Step-by-Step

1. Open Android Studio Profiler while LeafGuard AI runs.
2. Start a scan and observe memory use during image handling.
3. Watch CPU activity during preprocessing or inference.
4. Take one screenshot of the profiler for evidence.

### 🔵 Try This

- Compare memory usage before and after loading a large gallery image.

### Expected Output

- You gain a measurable view of performance rather than guessing.

### ✅ Checkpoint

- Which feature likely causes the largest short-term memory spike?

### ⚠️ Common Mistake

- Do not optimize randomly without profiler evidence.

### 📌 Key Point

- Performance work should be evidence-based.

## Notebook Cell 5 — Read and fix a stack trace

### Explanation

- Stack traces show the exception type and the call path that led to the crash.

### Code to Read / Run

```text
java.lang.NullPointerException: Attempt to invoke virtual method
'void android.widget.TextView.setText(java.lang.CharSequence)'
on a null object reference
    at com.leafguard.ResultActivity.showResult(ResultActivity.java:48)
    at com.leafguard.ResultActivity.onCreate(ResultActivity.java:22)
```

### 🔵 Try This

- Open the line number shown in the trace and inspect which view lookup returned null.

### Expected Output

- You trace the crash to a missing or mismatched view ID.

### ✅ Checkpoint

- Why is the top app-specific line often the most useful first clue?

### ⚠️ Common Mistake

- Do not start changing random files before reading the exception class and line number.

### 📌 Key Point

- Debugging gets easier when you read traces systematically.

## Notebook Cell 6 — Add LeakCanary and StrictMode

### Explanation

- LeakCanary detects memory leaks, while StrictMode catches bad main-thread behavior and resource misuse.

### Code to Read / Run

```java
debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.13'

StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .build());
```

### 🔵 Try This

- Enable StrictMode in debug only and watch Logcat during file/network operations.

### Expected Output

- The app surfaces hidden quality issues during development.

### ✅ Checkpoint

- Why should tools like StrictMode usually stay in debug builds only?

### ⚠️ Common Mistake

- Do not ignore repeated StrictMode warnings; they often point to real design problems.

### 📌 Key Point

- Quality tools are part of engineering maturity.

## Notebook Cell 7 — Use a simple test report template

### Explanation

- A test report helps you present evidence clearly during final submission.

### Code to Read / Run

```text
LeafGuard AI Test Report
------------------------
Feature tested: Camera to prediction flow
Device: Pixel 5 emulator API 34
Date: YYYY-MM-DD
Steps:
1. Open app
2. Select image from gallery
3. Upload to backend
4. Verify result and save history
Expected result: prediction details displayed and history updated
Actual result: Passed
Notes: Network latency ~1.2 seconds on local server
```

### 🔵 Try This

- Create at least three reports: one unit test, one UI test, and one manual end-to-end test.

### Expected Output

- You have evidence ready for Week 12 packaging.

### ✅ Checkpoint

- Why is documenting a passed test as important as fixing a failed one?

### ⚠️ Common Mistake

- Do not rely only on memory when presenting test outcomes later.

### 📌 Key Point

- Testing evidence strengthens both technical quality and project presentation.

## Mini Quiz

- What problem does this week solve inside LeafGuard AI?
- Which Java class or Android component did you touch first?
- Which file path in this repository is most relevant to this week?
- What would break if you skipped the validation step?
- How does this week connect to the three-tier architecture?

## Evidence Checklist

- [ ] Capture a screenshot of the completed screen or terminal output.
- [ ] Save one code snippet that proves the feature is wired correctly.
- [ ] Write two sentences in your progress log about what you learned.
- [ ] Record at least one bug and the exact fix you applied.
- [ ] Commit working changes before moving to the next week.

## Reflection Prompt

- Explain the feature from memory without reading the code.
- State one improvement you would add after submission.
- Identify one risk if this feature were left untested.

## Next Step

- Continue to **[Week 12: Final Submission](../../roadmap/week-12-final-submission/README.md)** when this week is stable and documented.


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

### Related Roadmap Materials
- 📖 [Week 11 README](../../roadmap/week-11-testing-debugging-performance/README.md) — Week overview & objectives
- 📝 [Week 11 Exercises](../../roadmap/week-11-testing-debugging-performance/exercises.md) — Practice problems
- 💡 [Week 11 Solutions](../../solutions/week-11/README.md) — Reference solutions
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

### Week Progression

| ← Previous | 🏠 | Next → |
|:-----------|:--:|-------:|
| [⬅ Week 10 Notebooks](../week-10/README.md) | [Notebooks Index](../README.md) | [Week 12 Notebooks ➡](../week-12/README.md) |

---
