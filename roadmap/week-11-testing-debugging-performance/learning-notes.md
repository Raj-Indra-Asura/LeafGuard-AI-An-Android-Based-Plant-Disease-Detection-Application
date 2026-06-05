# Week 11: Learning Notes - Testing, Debugging, and Performance
## Overview
Week 11 is about quality. By now, LeafGuard AI should already have major features. This week teaches you how to prove those features work, how to investigate failures, and how to improve speed and memory behavior.
Testing does not replace coding. Testing makes coding trustworthy. Debugging does not mean randomly changing lines until the crash disappears. Debugging means collecting evidence, reading runtime output, identifying the real root cause, fixing it, and verifying that the problem is actually gone.
These notes focus on Java-based Android development for CSE 2206 students. All examples are written in Java. Use the concepts here together with your own LeafGuard AI source files.
## Learning Outcomes
By the end of Week 11, you should be able to:
- open and use Logcat effectively in Android Studio
- write meaningful log statements with a proper `TAG`
- filter logs by package, tag, and level
- read a stack trace line by line and explain what it means
- write local unit tests with JUnit 4
- mock dependencies with Mockito
- write basic Espresso UI tests in `androidTest`
- use CPU, Memory, Network, and Energy profilers
- detect common Android memory leaks and fix them
- measure and optimize important performance paths in LeafGuard AI
- create a realistic test plan for final submission and viva
## How to Read These Notes
You do not need to memorize everything at once. Use this document in three passes:
1. read the Logcat and stack-trace section first
2. write a few JUnit tests and one Espresso test
3. return to the profiler, leak, and optimization sections while testing the full app
---
## 1. Android Logcat - The Developer's Window
### 1.1 What Logcat Is
Logcat is Android's runtime logging console. It shows messages coming from your app, Android framework classes, system services, and libraries. When your app crashes, Logcat is usually the first place where the real reason appears.
For a beginner, Logcat changes debugging from guessing to evidence-based reasoning. Instead of saying, "The camera feature is not working," you can say, "Logcat showed a `FileNotFoundException` when the selected image Uri was opened."
### 1.2 Why Logcat Matters in LeafGuard AI
LeafGuard AI performs several complex operations:
- camera and gallery input
- bitmap decoding
- image preprocessing
- model inference
- optional cloud requests
- local database storage
- screen navigation and result display

Any of those steps can fail. Logcat gives you the sequence of events, exception messages, and sometimes library-level warnings that explain why.
### 1.3 How to Open Logcat in Android Studio
Use the exact path below:
1. Open your Android project in Android Studio.
2. Run the app on a real device or emulator.
3. From the top menu, click **View**.
4. Choose **Tool Windows**.
5. Click **Logcat**.
6. In the Logcat toolbar, choose the correct device.
7. Choose the correct app process.
8. Trigger an action in the app so new logs appear. If nothing appears, confirm that the app is running, that the correct device is selected, and that the process drop-down is not pointing to another app.
### 1.4 Screenshot Description - Opening Logcat
A good screenshot for your report should include:
- Android Studio editor at the top
- Logcat panel docked at the bottom
- selected emulator or phone in the Logcat toolbar
- your app process selected
- the filter box visible
- at least one line from your app such as `MainActivity`
### 1.5 Structure of a Log Entry
Example:
```text
2025-03-10 14:21:08.532  5412-5412  MainActivity  com.example.leafguardai  D  Detect button clicked
```
Meaning:
- timestamp = `2025-03-10 14:21:08.532`
- process/thread = `5412-5412`
- tag = `MainActivity`
- package = `com.example.leafguardai`
- level = `D` for DEBUG
- message = `Detect button clicked` The tag and message are the parts you control directly. That is why clear logging style matters.
### 1.6 Log Levels
Android gives you several common log levels. Use the correct level for the correct meaning.
| Level | Method | Meaning | Good Use in LeafGuard AI |
|------|------|------|------|
| VERBOSE | `Log.v()` | very detailed tracing | step-by-step preprocessing during development |
| DEBUG | `Log.d()` | developer-focused state | selected Uri, button click, confidence values |
| INFO | `Log.i()` | important normal events | model loaded, prediction finished, history saved |
| WARN | `Log.w()` | suspicious but recoverable | missing optional field, low confidence, fallback used |
| ERROR | `Log.e()` | failure | exception, file read failure, network failure |
| ASSERT | `Log.wtf()` | impossible state | label list empty after successful init |
### 1.7 When to Use Each Level
Use `Log.v()` when you want highly detailed temporary tracing. Use `Log.d()` for regular development-time state. Use `Log.i()` to mark meaningful milestones. Use `Log.w()` when something is unusual but the app can continue. Use `Log.e()` when something failed. Use `Log.wtf()` very rarely for states that should never happen.
### 1.8 Defining a TAG Constant
Always define a tag constant in each major class. That makes filtering much easier.
```java
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: Activity created");
    }
}
```
Use short, class-based tags such as `MainActivity`, `ResultActivity`, `ImageUtils`, `DiseaseClassifier`, and `HistoryRepository`. Avoid vague tags such as `test`, `hello`, or `tag1`.
### 1.9 Writing Log Statements with Java Examples
```java
private static final String TAG = "DiseaseClassifier";

public PredictionResult classify(Bitmap bitmap) {
    Log.i(TAG, "classify: request received");

    if (bitmap == null) {
        Log.e(TAG, "classify: bitmap is null");
        return null;
    }

    Log.d(TAG, "classify: width=" + bitmap.getWidth());
    Log.d(TAG, "classify: height=" + bitmap.getHeight());

    try {
        long start = android.os.SystemClock.elapsedRealtime();
        PredictionResult result = runModel(bitmap);
        long duration = android.os.SystemClock.elapsedRealtime() - start;
        Log.i(TAG, "classify: completed in " + duration + " ms");
        return result;
    } catch (Exception e) {
        Log.e(TAG, "classify: inference failed", e);
        return null;
    }
}
```
Notice what makes these logs useful:
- method name is included in the message
- inputs are logged when helpful
- failure logs include the exception object
- logs describe intent, not just random text
### 1.10 Filtering Logcat Step by Step
A beginner usually opens Logcat, sees thousands of lines, and feels lost. Filtering is the solution. You must know how to filter in three ways:
- by package name
- by tag
- by level
### 1.11 Filter by Package Name
Use this when you want to focus on your app only.
1. Open Logcat.
2. Select your device.
3. Select your app process.
4. Click the search/filter field.
5. Type your package name, such as `com.example.leafguardai`.
6. Press Enter.
7. Trigger an action in the app. Why this helps:
- removes most system noise
- keeps attention on your app
- makes crash investigation faster Screenshot description:
- package name typed in the filter field
- only your app lines visible
- at least one DEBUG or INFO line from your code visible
### 1.12 Filter by Tag
Use this when you want to inspect one class or one feature flow.
1. Add logs using a clear tag such as `MainActivity`.
2. Open Logcat.
3. Click the filter box.
4. Type `MainActivity`.
5. Trigger the feature handled by that Activity.
6. Read only the remaining lines. Best uses:
- Activity lifecycle debugging
- camera button click flow
- result screen state changes
- repository save operations Screenshot description:
- the tag name typed in the filter box
- several lines from the same class visible
- a simple chronological flow easy to read
### 1.13 Filter by Level
Use this when severity matters more than source.
1. Open Logcat.
2. Find the level selector.
3. Choose `Error` for failures, or `Warn` for warnings and errors.
4. Reproduce the issue.
5. Study the remaining lines. When to use which:
- `Error` when the app crashes or something clearly fails
- `Warn` when behavior is suspicious but not fatal
- `Debug` when you are tracing state changes during development
### 1.14 Combined Filters
You can combine filters. For example, choose your app process, filter by tag `DiseaseClassifier`, and show only Error level. That narrows the log to one component and one severity. This is extremely useful when the app has many moving parts.
### 1.15 Color Coding in Android Studio Logcat
Exact colors vary by theme, but Android Studio usually gives visual hints:
- VERBOSE = light gray or low emphasis
- DEBUG = blue or neutral emphasis
- INFO = green or white depending on theme
- WARN = yellow or orange
- ERROR = red
- ASSERT = strong red or bold failure styling

Color helps scanning, but do not depend only on color. Always read the level, tag, and message text.
### 1.16 Stack Traces - What They Are
A stack trace is the ordered list of method calls that were active when the exception happened. It tells you the exception type, the exception message, the path the program took, and the file names and line numbers involved. That is why a stack trace is one of the most important debugging tools in Android.
### 1.17 Sample Stack Trace
```text
E/AndroidRuntime: FATAL EXCEPTION: main
Process: com.example.leafguardai, PID: 5412
java.lang.NullPointerException: Attempt to invoke virtual method 'int android.graphics.Bitmap.getWidth()' on a null object reference
    at com.example.leafguardai.MainActivity.processSelectedImage(MainActivity.java:142)
    at com.example.leafguardai.MainActivity.onActivityResult(MainActivity.java:118)
    at android.app.Activity.dispatchActivityResult(Activity.java:8613)
    at android.app.ActivityThread.deliverResults(ActivityThread.java:5592)
    at android.app.ActivityThread.handleSendResult(ActivityThread.java:5633)
    at android.os.Handler.dispatchMessage(Handler.java:106)
    at android.os.Looper.loop(Looper.java:288)
    at android.app.ActivityThread.main(ActivityThread.java:7872)
```
### 1.18 Reading the Stack Trace Line by Line
**Line 1: `E/AndroidRuntime: FATAL EXCEPTION: main`** means Android runtime reported a fatal crash. `main` means it happened on the UI thread, so the user saw the failure.
**Line 2: `Process: com.example.leafguardai, PID: 5412`** confirms which process crashed. This helps when multiple emulators, services, or tests are active.
**Line 3: `java.lang.NullPointerException: ...`** is the exception type and message. Here the app tried to call `getWidth()` on a null `Bitmap` reference.
**Line 4: `at com.example.leafguardai.MainActivity.processSelectedImage(MainActivity.java:142)`** is the most important line. It is the first stack frame from your own code. It gives you the class, method, file, and exact line number to inspect.
**Line 5: `at com.example.leafguardai.MainActivity.onActivityResult(MainActivity.java:118)`** tells you how the crashing method was reached. That helps you reconstruct the execution path.
The remaining lines mostly show framework code. They are useful context, but beginners should start with the first line from their own package.
### 1.19 Finding the Exact Line That Failed
Use this habit every time:
1. identify the exception type
2. scan down until your package appears
3. open the first matching file and line number
4. inspect 5-10 lines above and below it
5. look for null values, bad indexes, missing files, closed resources, or wrong assumptions If line 142 says:
```java
int width = bitmap.getWidth();
```
then you know `bitmap` was null at that moment. But that is only the symptom. The deeper question is why it became null. Maybe the user canceled image selection. Maybe the Uri failed to decode. Maybe the file path was wrong.
### 1.20 Understanding the `Caused by` Chain
Sometimes the first exception is not the real root cause. Look for `Caused by:` lines.
```text
java.lang.RuntimeException: Failed to decode image for prediction
    at com.example.leafguardai.ImageLoader.loadBitmap(ImageLoader.java:64)
    at com.example.leafguardai.MainActivity.prepareImage(MainActivity.java:103)
Caused by: java.io.FileNotFoundException: open failed: ENOENT (No such file or directory)
    at android.content.ContentResolver.openInputStream(ContentResolver.java:1436)
    at com.example.leafguardai.ImageLoader.loadBitmap(ImageLoader.java:52)
```
Interpretation:
- outer exception = decode failed
- inner `Caused by` = actual missing file problem
- real fix = correct the Uri or file handling, not only the decode method wrapper
### 1.21 Your Code vs Library Code
In Android stack traces you will see lines from `android.*`, `androidx.*`, `java.*`, `okhttp3.*`, `retrofit2.*`, and `org.tensorflow.*`. Usually the most actionable line is the first one from your own package. That is where your app passed the wrong value, called the API incorrectly, or failed to validate input.
### 1.22 Common Android Exceptions
#### NullPointerException
Meaning: a reference was null and you tried to use it. Common causes in LeafGuard AI: null Bitmap, missing Intent extra, null response body, wrong `findViewById()` result. Typical fix: validate before use and trace why the value became null.
#### NetworkOnMainThreadException
Meaning: network work was attempted on the main UI thread. Common cause: synchronous network code in a button click handler. Typical fix: move work to Retrofit `enqueue()`, WorkManager, or another background mechanism.
#### FileNotFoundException
Meaning: the app tried to open a file or stream that does not exist or is inaccessible. Common causes: wrong Uri, deleted cache file, missing permission, bad path construction. Typical fix: log the Uri or path, verify it exists, and check permissions.
#### OutOfMemoryError
Meaning: the app tried to use more memory than available. Common causes: loading large Bitmaps at full resolution, keeping many images in memory, leaking Activities or views. Typical fix: sample down images, release references, and profile memory behavior.
### 1.23 Practical Debugging Workflow
A strong debugging workflow is:
1. reproduce the issue consistently
2. filter Logcat to reduce noise
3. read the exception and stack trace carefully
4. identify the first useful line from your code
5. inspect the root cause, not only the symptom
6. apply a focused fix
7. rerun the same scenario to verify the bug is gone
8. test nearby scenarios to avoid regression
### 1.24 Example Debugging Story Pattern
When documenting a bug, write it like this:
- problem: app crashed after gallery image selection
- Logcat: `NullPointerException` in `MainActivity.java:142`
- root cause: image decode returned null because the Uri was invalid
- fix: validate the Uri, check for null Bitmap, and show an error message instead of continuing
- verification: retested with valid image, invalid image, and canceled picker flow
---
## 2. JUnit Unit Testing
### 2.1 What Unit Testing Means
Unit testing means testing a small unit of logic in isolation. A unit could be a formatter, parser, validator, helper method, or business-rule method. The goal is not to prove the whole app works. The goal is to prove one small piece behaves correctly for known inputs.
### 2.2 Why Unit Tests Are Valuable
Unit tests help because they are fast, repeatable, focused, easy to run before every change, and useful for catching regressions. Good unit test targets in LeafGuard AI include confidence formatting, selecting the top disease label from scores, validating image dimensions, generating summary text, and sorting or filtering history data.
### 2.3 `test/` vs `androidTest/`
Use `src/test/java` for local JVM unit tests. These run on your computer, not on a device. They are ideal for pure Java logic. Use `src/androidTest/java` for instrumented Android tests. These run on an emulator or device and are required for Espresso and UI interactions.
### 2.4 Core JUnit 4 Annotations
- `@Test` marks a test method
- `@Before` runs before each test
- `@After` runs after each test
- `@BeforeClass` runs once before all tests in the class and must be static
- `@AfterClass` runs once after all tests in the class and must be static
### 2.5 Assert Methods
The most useful assertion methods for Week 11 are `assertEquals()`, `assertNotNull()`, `assertNull()`, `assertTrue()`, `assertFalse()`, and `assertThrows()`.
```java
assertEquals("87.3%", result);
assertNotNull(predictionResult);
assertNull(optionalText);
assertTrue(confidence > 0.8f);
assertFalse(labels.isEmpty());
```
### 2.6 The 3 A's of Testing
A clean test usually follows the 3 A's:
- Arrange = prepare inputs and objects
- Act = call the method
- Assert = verify the outcome
### 2.7 Complete Example - Helper Method and JUnit Test
```java
import java.util.Locale;

public class ConfidenceFormatter {

    public static String format(float confidence) {
        if (confidence < 0.0f || confidence > 1.0f) {
            throw new IllegalArgumentException("Confidence must be between 0 and 1");
        }
        return String.format(Locale.US, "%.1f%%", confidence * 100.0f);
    }
}
```
```java
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ConfidenceFormatterTest {

    @Test
    public void format_returnsPercentageWithOneDecimal() {
        float confidence = 0.8734f;
        String actual = ConfidenceFormatter.format(confidence);
        assertEquals("87.3%", actual);
    }

    @Test
    public void format_returnsZeroPercent() {
        assertEquals("0.0%", ConfidenceFormatter.format(0.0f));
    }

    @Test
    public void format_throwsForInvalidValue() {
        assertThrows(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                ConfidenceFormatter.format(1.2f);
            }
        });
    }
}
```
### 2.8 Example - Testing Disease Result Parsing
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
```java
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DiseaseResultParserTest {

    @Test
    public void getTopLabel_returnsLabelWithHighestScore() {
        float[] scores = {0.12f, 0.71f, 0.17f};
        String[] labels = {"Healthy", "Leaf Rust", "Powdery Mildew"};
        String result = DiseaseResultParser.getTopLabel(scores, labels);
        assertEquals("Leaf Rust", result);
    }
}
```
### 2.9 Mockito - Why It Exists
Mockito helps you test logic without depending on real external systems. A real API call depends on network, server state, and timing. A real database may involve Android framework behavior or I/O. Mockito replaces those dependencies with controlled fake objects.
### 2.10 Complete Mockito Example - Fake API Response
```java
public interface PredictionGateway {
    PredictionResponse fetchLastPrediction();
}
```
```java
public class PredictionSummaryUseCase {

    private final PredictionGateway gateway;

    public PredictionSummaryUseCase(PredictionGateway gateway) {
        this.gateway = gateway;
    }

    public String buildSummary() {
        PredictionResponse response = gateway.fetchLastPrediction();
        if (response == null) {
            return "No prediction available";
        }
        return response.getDisease() + " (" + response.getConfidence() + ")";
    }
}
```
```java
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PredictionSummaryUseCaseTest {

    @Test
    public void buildSummary_usesFakeApiResponse() {
        PredictionGateway gateway = mock(PredictionGateway.class);
        PredictionResponse fake = new PredictionResponse();
        fake.setDisease("Tomato Early Blight");
        fake.setConfidence(0.92);
        when(gateway.fetchLastPrediction()).thenReturn(fake);
        PredictionSummaryUseCase useCase = new PredictionSummaryUseCase(gateway);
        String result = useCase.buildSummary();
        assertEquals("Tomato Early Blight (0.92)", result);
    }
}
```
### 2.11 What to Unit Test and What Not to Unit Test
Good local unit test targets:
- utility methods
- data transformations
- validation logic
- formatting helpers
- business rules
- parser methods Poor targets for local unit tests:
- UI layout rendering
- camera hardware behavior
- real network requests
- sensors
- Activity lifecycle interactions Those belong to UI tests, integration tests, or manual device tests instead.
### 2.12 Test Coverage
Test coverage means how much of your code was executed while tests ran. Coverage is useful as a guide, not as the only quality metric. A high percentage with weak tests is still weak quality. A moderate percentage with meaningful edge cases is often better.
### 2.13 How to View Coverage in Android Studio
1. Right-click a test class or the `test` folder.
2. Choose **Run with Coverage**.
3. Wait for tests to finish.
4. Open the Coverage tool window.
5. Study which classes and lines were executed.
### 2.14 How to Run Unit Tests
From Android Studio, right-click a class in `src/test/java` and choose **Run**. From Gradle, use:
```bash
./gradlew test
```
A good Week 11 minimum is at least 3 passing unit tests and at least 1 test using Mockito.
---
## 3. Espresso UI Testing
### 3.1 What Espresso Is
Espresso is Android's UI testing framework. It automates user interactions such as tapping buttons, typing text, scrolling, and checking whether a view is visible. It exists so your UI checks can be repeatable instead of manual and inconsistent.
### 3.2 Why Espresso Exists
Manual testing is still important, but it is slow, easy to forget, hard to repeat exactly, and difficult to prove later. Espresso gives you an executable script for UI behavior. That matters in LeafGuard AI because the project includes image capture, prediction, result display, and history navigation.
### 3.3 Espresso Belongs in `androidTest`
Espresso tests must live in `src/androidTest/java` because they run on a real Android environment. They need Activities, Views, and Android framework behavior. They are not plain JVM tests.
### 3.4 Core Espresso Building Blocks
- `onView()` = choose a view
- `onData()` = choose data-backed content such as old `ListView` items
- `withId()` = match a view by resource ID
- `withText()` = match a view by text
- `perform()` = do an action
- `check()` = assert something about the view
### 3.5 ViewMatchers
Common ViewMatchers you should know:
- `withId()`
- `withText()`
- `isDisplayed()`
- `isEnabled()`
- `isChecked()`
```java
onView(withId(R.id.buttonPredict)).check(matches(isDisplayed()));
onView(withText("Detect Disease")).check(matches(isDisplayed()));
onView(withId(R.id.checkboxTerms)).check(matches(isChecked()));
```
### 3.6 ViewActions
Common actions:
- `click()`
- `typeText()`
- `scrollTo()`
- `swipeUp()`
```java
onView(withId(R.id.buttonPredict)).perform(click());
onView(withId(R.id.editTextName)).perform(typeText("Sample Leaf"));
onView(withId(R.id.saveButton)).perform(scrollTo(), click());
```
### 3.7 ViewAssertions
Common assertions:
- `matches()`
- `doesNotExist()`
```java
onView(withId(R.id.resultRootLayout)).check(matches(isDisplayed()));
onView(withText("Loading...")).check(doesNotExist());
```
### 3.8 Espresso Dependencies
```gradle
androidTestImplementation 'androidx.test.ext:junit:1.1.5'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
androidTestImplementation 'androidx.test.espresso:espresso-intents:3.5.1'
```
```gradle
android {
    defaultConfig {
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }
}
```
### 3.9 Complete Espresso Example - Tap Camera Button
```java
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void cameraButton_isVisibleAndCanBeTapped() {
        onView(withId(R.id.buttonCaptureImage)).check(matches(isDisplayed())).perform(click());
    }
}
```
### 3.10 IntentTestRule / Intents-Based Navigation Testing
When you want to verify Activity navigation, Espresso Intents helps. Many projects use `IntentsTestRule`. The important idea is to check that the expected Activity Intent was fired.
```java
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityNavigationTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void tappingPredict_opensResultActivity() {
        onView(withId(R.id.buttonPredict)).perform(click());
        intended(hasComponent(ResultActivity.class.getName()));
    }
}
```
### 3.11 IdlingResource for Async Work
Espresso waits automatically for many UI tasks, but not every background operation. If your app performs network work, model inference, or asynchronous database work, a test may assert too early. `IdlingResource` solves this by telling Espresso when the app is busy and when it is idle.
```java
import androidx.test.espresso.idling.CountingIdlingResource;

public class EspressoIdlingResource {

    public static final CountingIdlingResource countingIdlingResource = new CountingIdlingResource("LeafGuardWork");

    public static void increment() {
        countingIdlingResource.increment();
    }

    public static void decrement() {
        if (!countingIdlingResource.isIdleNow()) {
            countingIdlingResource.decrement();
        }
    }
}
```
### 3.12 Running Espresso Tests on an Emulator
1. Start an emulator.
2. Wait until the home screen is fully ready.
3. Right-click the test class in `androidTest`.
4. Choose **Run**.
5. Watch the emulator as Espresso controls the UI.
6. Check the Run window for green or red results. Best practices:
- use stable view IDs
- avoid unnecessary animation during testing
- do not rely on `Thread.sleep()` unless absolutely unavoidable
- prefer IdlingResource for async operations
### 3.13 Good First UI Tests for LeafGuard AI
Start with simple, valuable tests:
- MainActivity opens
- camera button is displayed
- predict button is disabled before an image is selected
- tapping predict after setup opens result screen
- empty history state displays correctly
---
## 4. Android Studio Profiler
### 4.1 Opening the Profiler
Open it from **View -> Tool Windows -> Profiler**. Then select your device, select the app process, and choose the type of profiling you want to inspect.
Profiler helps when your question is not "Did it fail?" but rather "Why is it slow, memory-heavy, or battery-costly?"
### 4.2 CPU Profiler
CPU Profiler shows where processing time goes. Use it when inference feels slow, app launch seems delayed, or scrolling stutters. Record a profiling session, perform the slow action, and inspect which methods consume the most time.
### 4.3 How to Use CPU Profiler to Find a Slow Method
1. Open CPU Profiler.
2. Start recording.
3. Run the exact slow action, such as prediction on a large image.
4. Stop recording.
5. Look for wide blocks in the call chart or flame chart.
6. Find methods that consume a large share of total time. If `decodeBitmap()` is wider than `runModel()`, your real bottleneck may be image decoding, not inference.
### 4.4 Reading a Flame Chart / Call Chart
General rules:
- wider block = more time spent
- nested block = child method called by parent method
- repeated wide blocks = repeated expensive work A flame chart turns a vague feeling like "prediction is slow" into a measurable answer like "bitmap resizing takes longer than model execution."
### 4.5 Memory Profiler
Memory Profiler helps you understand RAM use. Use it when you suspect leaks, large image issues, or repeated allocation problems. It can show live memory graphs, heap dumps, and allocation tracking.
### 4.6 Heap Dumps
A heap dump is a snapshot of objects currently in memory. Use it to answer questions such as:
- why are several large `Bitmap` objects still alive?
- why does `MainActivity` still exist after it should be destroyed?
- what type of object is using the most memory?
### 4.7 Allocation Tracking
Allocation tracking helps you see what objects are being created over time. That is useful when repeated user actions create too many temporary objects, which can increase garbage collection pressure and slow the app.
### 4.8 Network Profiler
Network Profiler shows network requests and timing in real time. This is especially helpful if your LeafGuard AI app includes a cloud prediction mode. You can compare request duration, frequency, and payload behavior.
### 4.9 Energy Profiler
Energy Profiler shows patterns that may affect battery usage. Even if LeafGuard AI is not a background-heavy app, energy awareness is still useful. Repeated network calls, poorly controlled background work, and unnecessary wake behavior all make an app feel less professional.
---
## 5. Memory Leak Detection
### 5.1 What a Memory Leak Is
A memory leak happens when an object is no longer needed, but another object still holds a reference to it, so the garbage collector cannot free it. On Android, leaking `Activity` or `Bitmap` objects is especially dangerous because they can be large and long-lived.
### 5.2 Common Leak Pattern - Static Activity Reference
```java
public class LeakHolder {
    public static MainActivity activity;
}
```
Why this is bad:
- static fields live for the life of the app process
- Activity objects should die when the screen closes
- the static field prevents garbage collection Correct idea:
- never store an Activity in a static field
- use `getApplicationContext()` only when a long-lived context is truly appropriate
### 5.3 Common Leak Pattern - Anonymous Inner Class
Anonymous inner classes hold an implicit reference to the outer class. If you create one inside an Activity and it outlives the Activity, you may leak the screen. This is common with delayed tasks and callbacks.
### 5.4 Common Leak Pattern - Non-static Handler in Activity
A delayed Runnable can keep the Activity alive longer than intended. Always cancel callbacks in `onDestroy()` if they are no longer needed.
```java
@Override
protected void onDestroy() {
    handler.removeCallbacksAndMessages(null);
    super.onDestroy();
}
```
### 5.5 Common Leak Pattern - Unreleased Cursor or Database Resource
If you open a Cursor, close it. If you open a stream, close it. If you keep database resources open unnecessarily, you waste resources and may create longer-lived objects than expected.
```java
Cursor cursor = database.rawQuery("SELECT * FROM scans", null);
try {
    while (cursor.moveToNext()) {
        // read rows
    }
} finally {
    cursor.close();
}
```
### 5.6 LeakCanary
LeakCanary is a popular Android library for detecting memory leaks during development. It watches Activities, Fragments, and other objects and reports when they should have been collected but are still reachable.
```gradle
debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.14'
```
Use `debugImplementation` so it is active in debug builds only.
### 5.7 Reading a LeakCanary Report
A LeakCanary report usually shows the leaking object, reference chain, and GC root. Interpretation is similar to debugging a stack trace: it tells you what should have been released, what is still holding it, and why the object is reachable.
### 5.8 Fixing Leaks
Common fixes include:
- remove static Activity references
- cancel callbacks and delayed tasks
- unregister listeners
- close Cursors and streams
- use Application context instead of Activity context when a long-lived context is required
- use `WeakReference` when you need a non-owning reference
### 5.9 WeakReference Example
```java
import java.lang.ref.WeakReference;

public class PredictionCallbackHolder {

    private final WeakReference<MainActivity> activityRef;

    public PredictionCallbackHolder(MainActivity activity) {
        this.activityRef = new WeakReference<>(activity);
    }
}
```
`WeakReference` is useful, but do not treat it as a magic fix for every design problem. Good lifecycle cleanup is still the primary solution.
---
## 6. Performance Optimization
### 6.1 Measure Before You Optimize
Never say "This part is probably slow" without measuring it. You need evidence. Profiler data and timing logs reveal the real bottleneck.
### 6.2 Timing Methods
`System.currentTimeMillis()` can measure durations, but it tracks wall-clock time. `SystemClock.elapsedRealtime()` is usually a better choice for timing app operations on Android.
```java
long start = android.os.SystemClock.elapsedRealtime();
runPrediction();
long duration = android.os.SystemClock.elapsedRealtime() - start;
Log.d("Perf", "Prediction took " + duration + " ms");
```
### 6.3 StrictMode
StrictMode helps catch bad development-time behavior, especially disk or network work on the main thread. It can also warn about leaked closable resources.
```java
StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()
        .penaltyLog()
        .build());

StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedClosableObjects()
        .penaltyLog()
        .build());
```
### 6.4 Image Loading Optimization
Large camera images are dangerous for both speed and memory. If your model only needs a small input such as `224 x 224`, loading a huge full-resolution image first is inefficient. Sample the bitmap down before loading it fully.
```java
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtils {

    public static Bitmap decodeSampledBitmap(String path, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = 4;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
}
```
### 6.5 Database Query Optimization
Database performance tips:
- add indexes where appropriate
- limit query size
- avoid querying more rows than needed
- do not block the main thread
- expose data through lifecycle-aware observation when useful
```java
@Query("SELECT * FROM scan_history ORDER BY timestamp DESC LIMIT 20")
List<ScanHistoryEntity> getLatestScans();
```
### 6.6 RecyclerView Optimization
If LeafGuard AI shows history or saved predictions, RecyclerView should be efficient. Useful techniques include the ViewHolder pattern, `setHasFixedSize(true)` when appropriate, and `DiffUtil` to update only changed rows.
### 6.7 APK Size Optimization
To reduce final APK size:
- enable R8 or ProGuard in release builds
- shrink unused resources
- remove unnecessary large assets
- keep only resources you actually use
### 6.8 Battery Optimization
Battery-friendly habits include:
- avoid unnecessary wake locks
- do not repeat background work without reason
- use WorkManager for deferred background tasks
- stop work tied to a screen when the screen is no longer active
### 6.9 Performance Targets for LeafGuard AI
Suggested Week 11 targets:
- app launch under 3 seconds
- offline inference average under 500 ms
- no UI freeze during inference
- history screen opens smoothly
- no OOM on large images used during testing
---
## 7. Testing the Complete LeafGuard AI App
### 7.1 Why a Test Plan Matters
A test plan turns random clicking into disciplined verification. It tells your teacher what you tested, how you tested it, and whether the result passed or failed. It also helps you discover missing cases, especially edge cases.
### 7.2 Test Plan Template
| Test ID | Feature | Input / Steps | Expected Output | Actual Output | Status |
|------|------|------|------|------|------|
| TC-01 | App launch | Open app from launcher | Main screen opens within target time | | |
| TC-02 | Camera flow | Tap camera button | Camera opens or permission prompt appears | | |
| TC-03 | Offline inference | Use valid image | Disease result displayed | | |
| TC-04 | Cloud inference | Use valid image with internet | Server response displayed | | |
| TC-05 | History | Save and open history | Record appears | | |
### 7.3 Twenty Specific LeafGuard AI Test Cases
| ID | Scenario | Input | Expected Output |
|------|------|------|------|
| TC-01 | Launch app | Open from launcher | MainActivity opens within 3 seconds |
| TC-02 | Camera permission allow | Tap camera and allow permission | Camera opens successfully |
| TC-03 | Camera permission deny | Tap camera and deny permission | Friendly message shown |
| TC-04 | Gallery image select | Select a valid plant image | Preview image appears |
| TC-05 | Predict without image | Tap predict with no image | Button disabled or warning shown |
| TC-06 | Healthy leaf offline | Clear healthy leaf image | Healthy or plausible healthy result shown |
| TC-07 | Diseased leaf offline | Clear diseased image | Correct or plausible disease shown |
| TC-08 | Healthy leaf cloud | Clear healthy image with internet | Cloud result appears |
| TC-09 | Diseased leaf cloud | Clear diseased image with internet | Cloud result appears |
| TC-10 | No internet | Disable internet in cloud mode | Error shown without crash |
| TC-11 | Slow network | Limited network connection | Loading state remains until response |
| TC-12 | Save result | Complete prediction and save | History record stored |
| TC-13 | Open history | Navigate to history screen | Existing scans listed |
| TC-14 | Empty history | Clear DB and open history | Empty state message shown |
| TC-15 | Rotate result screen | Rotate during result display | State remains valid or restored |
| TC-16 | Rapid taps | Tap predict quickly many times | Duplicate work prevented |
| TC-17 | Large image | Very high-resolution photo | App resizes image and does not crash |
| TC-18 | Non-plant image | Desk or wall image | App handles output gracefully |
| TC-19 | Blurry image | Blurry plant photo | Low confidence or graceful handling |
| TC-20 | Dark image | Underexposed plant photo | App still responds safely |
### 7.4 Edge Cases Specific to Plant Disease Detection
Important edge cases include:
- blurry images
- dark images
- very bright images
- partially cropped leaves
- multiple leaves in one frame
- non-plant images
- background clutter
- leaves too close or too far from the camera For each edge case, record both technical behavior and user-experience quality. A model may return an imperfect result, but the app should still behave safely, clearly, and predictably.
### 7.5 Performance Benchmarks Before Submission
Suggested targets:
- offline inference average < 500 ms
- app launch < 3 seconds
- recent history query < 200 ms
- no frozen UI during prediction For cloud mode, record 10 runs and calculate the average. Latency is affected by network conditions, so compare best, worst, and average behavior.
### 7.6 Benchmark Table
| Mode | Run 1 | Run 2 | Run 3 | Run 4 | Run 5 | Run 6 | Run 7 | Run 8 | Run 9 | Run 10 | Average |
|------|------|------|------|------|------|------|------|------|------|------|------|
| Offline | | | | | | | | | | | |
| Cloud | | | | | | | | | | | |
### 7.7 CSE 2206 Viva Prep Q&A
**Q: What is the difference between JUnit and Espresso?**
A: JUnit tests small logic units, usually in `src/test/java`. Espresso tests UI interactions on a device or emulator, usually in `src/androidTest/java`.
**Q: Why is Logcat important?**
A: Logcat shows runtime messages, exceptions, and stack traces. It helps locate the exact source file and line number where the failure happened.
**Q: Why is Mockito useful?**
A: Mockito lets you test your logic with fake dependencies, so tests do not depend on real internet, server state, or database timing.
**Q: What is a memory leak?**
A: A memory leak happens when unused objects remain strongly referenced and cannot be garbage collected.
**Q: Why do we benchmark both offline and cloud inference?**
A: Because they have different performance characteristics. Offline depends mostly on device processing, while cloud mode also depends on network latency.
---
## 8. Quick Week 11 Readiness Summary
Before moving to final submission, you should be able to say yes to all of these:
- I can open and filter Logcat confidently.
- I can read a stack trace line by line.
- I wrote at least 3 unit tests.
- I wrote at least 1 Espresso UI test.
- I measured offline and cloud prediction times.
- I checked memory behavior with Profiler and LeakCanary.
- I can explain one real bug I fixed and how I found it.
- I can explain one optimization I made and why it helped. If you can do that, Week 11 has done its job.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 11 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| **2** | **learning-notes.md** ← *You are here* | **Theory & Learning Notes** |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Week Overview & Objectives](README.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Theory & Learning Notes** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Practice Exercises →](exercises.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 10: Notifications, Share & Location](../week-10-notifications-share-location/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 12: Final Submission ➡](../week-12-final-submission/README.md) |

---
