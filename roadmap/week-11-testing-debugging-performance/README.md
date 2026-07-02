# Week 11: Testing, Debugging, and Performance

## What you'll learn & why

This week you make sure LeafGuard AI actually works — and prove it. You will run the project's automated **tests** (small programs that check your app for you), read the phone's log (**Logcat**) to hunt down bugs, and measure how fast detection is so you can compare the online (server) path with the offline (on-device) path. Testing, debugging, and performance analysis are all required by your CSE 2206 course, and this week gives you the evidence (screenshots, tables, logs) your report needs.

Good news: the four automated tests already exist in the project and pass. Your job is to *understand* them, *run* them, and add your own manual test table and debugging notes.

## New words this week

See the shared [glossary](../../GLOSSARY.md) for more. The key terms this week:

- **Test** — a small program that automatically checks that part of your app behaves correctly, so you don't have to check it by hand every time.
- **Unit test** — a test that runs on your computer (no phone or emulator needed) and checks one small piece of code, like parsing a server reply. LeafGuard's unit test is `PredictionResponseTest`.
- **UI (instrumented) test** — a test that runs the real app on an emulator or phone and checks the screen, like a user would. LeafGuard's UI test is `MainActivityTest` (it uses **Espresso**, Android's UI-testing tool).
- **Logcat** — the live stream of log messages from your app; the main tool for debugging on Android.
- **Latency** — how long something takes, measured in milliseconds (ms). Lower is faster.

> **Try it now:** the project already contains four passing tests. Open a terminal in
> `android-app-kotlin/` and run `./gradlew testDebugUnitTest` (Windows: `gradlew.bat testDebugUnitTest`).
> You should see `BUILD SUCCESSFUL`. Full walkthroughs are in
> [`../../solutions/week-11/`](../../solutions/week-11/) and the practice files live in
> [`../../exercises/testing/`](../../exercises/testing/).

---

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

### JUnit 4 Unit Test — the real one in LeafGuard

LeafGuard's real unit test is `PredictionResponseTest`. It checks that the app can
correctly read (parse) the JSON reply the FastAPI backend sends from `POST /predict`.
The important field is `disease` (**not** `disease_name`).

**Kotlin (primary track)** —
`android-app-kotlin/app/src/test/java/com/leafguard/network/PredictionResponseTest.kt`:

```kotlin
class PredictionResponseTest {

    @Test
    fun parsesDiseaseFieldFromServerJson() {
        val json = """
            {
              "disease": "Tomato Early Blight",
              "confidence": 92.5,
              "symptoms": "Small brown spots with concentric rings.",
              "treatment": "Remove infected leaves.",
              "prevention": "Rotate crops."
            }
        """.trimIndent()

        val response = Gson().fromJson(json, PredictionResponse::class.java)

        assertEquals("Tomato Early Blight", response.disease)
        assertEquals(92.5f, response.confidence, 0.001f)
    }

    @Test
    fun missingOptionalFieldsAreNullNotCrash() {
        val json = """{"disease": "Potato Healthy", "confidence": 88.0}"""
        val response = Gson().fromJson(json, PredictionResponse::class.java)
        assertEquals("Potato Healthy", response.disease)
        assertEquals(null, response.symptoms)
    }
}
```

**Java (secondary track)** —
`android-app/app/src/test/java/com/leafguard/network/PredictionResponseTest.java` is the
byte-for-byte-behavior twin; it uses `response.getDisease()` getters instead of Kotlin
properties.

Run it in **Android Studio** by right-clicking the file → **Run 'PredictionResponseTest'**,
or from a terminal in `android-app-kotlin/`:

```bash
./gradlew testDebugUnitTest        # macOS/Linux
gradlew.bat testDebugUnitTest      # Windows
```

Expected result: **2 tests passed** and **BUILD SUCCESSFUL**.

### Espresso UI Test — the real one in LeafGuard

LeafGuard's real UI (instrumented) test is `MainActivityTest`. It launches the real
`MainActivity` and checks that the two capture buttons — `buttonOpenCamera` and
`buttonOpenGallery` — are visible on screen.

**Kotlin (primary track)** —
`android-app-kotlin/app/src/androidTest/java/com/leafguard/MainActivityTest.kt`:

```kotlin
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun captureButtonsAreVisibleOnLaunch() {
        onView(withId(R.id.buttonOpenCamera)).check(matches(isDisplayed()))
        onView(withId(R.id.buttonOpenGallery)).check(matches(isDisplayed()))
    }
}
```

**Java (secondary track)** —
`android-app/app/src/androidTest/java/com/leafguard/MainActivityTest.java` is the twin.

Run it in **Android Studio** by right-clicking the file → **Run 'MainActivityTest'**
(an emulator must be running), or from a terminal in `android-app-kotlin/`:

```bash
./gradlew connectedDebugAndroidTest        # macOS/Linux
gradlew.bat connectedDebugAndroidTest      # Windows
```

Expected result: **1 test passed** and **BUILD SUCCESSFUL**. If you see "No connected
devices", start an emulator first (Android Studio → Device Manager → ▶).

### Debugging with Logcat

```kotlin
// Add structured logging in every Activity (Kotlin — primary track)
private val TAG = "LeafGuard.MainActivity"

Log.v(TAG, "Image URI: $uri")                        // Verbose — development only
Log.d(TAG, "Calling /predict endpoint...")           // Debug — trace execution path
Log.i(TAG, "Prediction received: ${result.disease}") // Info — major lifecycle events
Log.w(TAG, "Location null, saving scan without coordinates") // Warn — recoverable
Log.e(TAG, "Failed to connect to API", exception)    // Error — failures
```

**Java (secondary track):**
```java
private static final String TAG = "LeafGuard.MainActivity";
Log.d(TAG, "Calling /predict endpoint...");
Log.i(TAG, "Prediction received: " + result.getDisease());
Log.e(TAG, "Failed to connect to API", exception);
```

**Logcat filter example:** Set tag filter to `LeafGuard` to see only your app's messages.

### Latency Measurement

```kotlin
val startTime = System.currentTimeMillis()

// ... run prediction (cloud or TFLite) ...

val latency = System.currentTimeMillis() - startTime
Log.d(TAG, "Prediction latency: $latency ms")
tvLatency.text = "Latency: $latency ms"
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

## Where to practice and check your work

- Practice files: [`../../exercises/testing/`](../../exercises/testing/)
- Worked solution & line-by-line test walkthrough: [`../../solutions/week-11/`](../../solutions/week-11/)
- Supporting notebooks: [`../../notebooks/week-11/`](../../notebooks/week-11/)

**Next:** `learning-notes.md` for deeper testing patterns and debugging techniques.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 11 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| **1** | **README.md** ← *You are here* | **Week Overview & Objectives** |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

*(Start of week)* &nbsp;&nbsp;|&nbsp;&nbsp; **Week Overview & Objectives** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Theory & Learning Notes →](learning-notes.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 10: Notifications, Share & Location](../week-10-notifications-share-location/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 12: Final Submission ➡](../week-12-final-submission/README.md) |

---
