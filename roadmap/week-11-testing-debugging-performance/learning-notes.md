# Week 11: Learning Notes - Testing, Debugging, and Performance for LeafGuard AI

## Overview

Week 11 is where your LeafGuard AI app stops being "just working on your phone" and starts becoming a reliable Android application that you can confidently submit, demonstrate, and defend in a CSE 2206 viva.

In earlier weeks you focused on building features. This week you focus on proving that those features are correct, finding bugs systematically, and improving speed, memory use, and stability.

A professional Android developer does not guess when something fails. They collect evidence. They read Logcat. They write repeatable tests. They measure performance. They fix the root cause. Then they verify the fix.

That is exactly the mindset you should build this week.

## Learning Goals

By the end of Week 11, you should be able to:

- use Logcat to investigate crashes and warnings
- read Android stack traces line by line
- write JUnit 4 unit tests for pure Java logic
- use Mockito to isolate dependencies during tests
- write basic Espresso UI tests for Activity flows
- use the Android Studio Profiler tools to inspect CPU, memory, network, and energy
- detect and fix memory leaks using LeakCanary and lifecycle cleanup
- measure real performance numbers instead of estimating
- produce a complete test report for the LeafGuard AI app

## Why This Week Matters for LeafGuard AI

LeafGuard AI combines camera input, image processing, ML inference, network communication, and result display.

That means bugs can appear in many places:

- the image Uri may be null
- a background task may update the UI incorrectly
- the model may be slow on large images
- a RecyclerView may leak an Activity context
- a cloud call may fail on a poor connection
- an out-of-memory crash may happen if full-size Bitmaps are loaded carelessly

Testing and debugging help you catch these failures before your teacher or user finds them.

## Table of Contents

1. [Android Logcat - The Developer's Window](#1-android-logcat---the-developers-window)
2. [JUnit Unit Testing](#2-junit-unit-testing)
3. [Espresso UI Testing](#3-espresso-ui-testing)
4. [Android Studio Profiler](#4-android-studio-profiler)
5. [Memory Leak Detection](#5-memory-leak-detection)
6. [Performance Optimization](#6-performance-optimization)
7. [Testing the Complete LeafGuard AI App](#7-testing-the-complete-leafguard-ai-app)
8. [Week 11 Viva and Submission Readiness](#8-week-11-viva-and-submission-readiness)

---

## 1. Android Logcat - The Developer's Window

### 1.1 What Logcat Is

**Logcat** is Android's centralized logging system. It collects messages from your app, the Android framework, system services, and libraries such as Retrofit, OkHttp, TensorFlow Lite, and Room.

Think of Logcat as a constantly updating timeline of what the device is doing. If your app crashes, shows a warning, throws an exception, or prints your own debugging message, that information usually appears in Logcat.

For beginners, Logcat is the fastest way to move from:

- "Something went wrong"

to:

- "This exact line in MainActivity caused a NullPointerException"

### 1.2 Why Logcat Is Essential

Without Logcat, you are guessing. With Logcat, you can answer questions such as:

- Did the camera Intent launch?
- Was the image path null?
- Did the API request start?
- Did the response fail with HTTP 500?
- Which line crashed?
- Was the warning from your code or from a library?

For CSE 2206 viva preparation, Logcat also gives you concrete evidence. Instead of saying, "I think the app crashed because of the image," you can say, "Logcat showed a FileNotFoundException at ImageUtils.java:48 because the Uri path was invalid."

### 1.3 How to Open Logcat in Android Studio

Follow these exact steps in Android Studio:

1. Open your LeafGuard AI project.
2. Connect a phone or start an emulator.
3. Run the app so Android Studio can attach to a process.
4. In the top menu, click **View**.
5. Move to **Tool Windows**.
6. Click **Logcat**.
7. The Logcat panel usually opens at the bottom of the screen.
8. If you do not see logs immediately, choose the correct device from the device drop-down.
9. Then choose your app process from the application/process drop-down.
10. Trigger an action in the app, such as opening the camera, loading an image, or tapping the prediction button.
11. Watch the logs update in real time.

### 1.4 Screenshot Description - Opening Logcat

If you were taking a screenshot for your report, it should show:

- Android Studio main editor on top
- Logcat panel docked at the bottom
- selected device name in the Logcat toolbar
- your application process selected
- the search/filter text box visible
- one or more lines with your app tag such as `MainActivity`

### 1.5 Anatomy of a Log Line

A typical Logcat entry contains several parts. A simplified example is shown below.

```text
2025-03-10 14:21:08.532  5412-5412  MainActivity  com.example.leafguardai  D  Detect button clicked
```

Read it from left to right:

- `2025-03-10 14:21:08.532` = timestamp
- `5412-5412` = process ID and thread ID
- `MainActivity` = tag
- `com.example.leafguardai` = package or process name
- `D` = log level, which means DEBUG
- `Detect button clicked` = the actual message

When you define your own tags clearly, you can scan Logcat much faster.

### 1.6 Log Levels and When to Use Each One

Android provides six common log levels. Use them intentionally.

| Level | Method | Meaning | When to Use It |
|------|------|------|------|
| VERBOSE | `Log.v()` | Very detailed | Step-by-step tracing during development |
| DEBUG | `Log.d()` | Debug information | Values, branches, control flow |
| INFO | `Log.i()` | Important normal events | App started, model loaded, request finished |
| WARN | `Log.w()` | Suspicious but non-fatal | Missing optional field, fallback used |
| ERROR | `Log.e()` | Something failed | Exception, failed network call, parse error |
| ASSERT | `Log.wtf()` | Severe impossible state | Something that should never happen |

#### VERBOSE

Use VERBOSE for highly detailed output. Examples:

- each preprocessing step in image normalization
- every item read from a large JSON response
- repeated loop diagnostics during development only

Too much VERBOSE logging can make Logcat noisy, so remove or reduce it before submission.

#### DEBUG

Use DEBUG for developer-focused information. Examples:

- button clicked
- image size before resize
- prediction confidence before formatting
- selected Uri string

DEBUG logs are often the most useful during day-to-day debugging.

#### INFO

Use INFO for meaningful milestones. Examples:

- "Model loaded successfully"
- "Prediction completed in 312 ms"
- "History record inserted"

INFO logs are less noisy than DEBUG and communicate useful progress.

#### WARN

Use WARN when something is unusual, but the app can continue. Examples:

- image metadata missing, but default values are used
- network response missing optional text fields
- prediction confidence below your confidence threshold

WARN messages tell you, "Pay attention, but this is not an immediate crash."

#### ERROR

Use ERROR for failures. Examples:

- file open failure
- JSON parsing failure
- Retrofit request failure
- model loading exception

When possible, log both a readable message and the exception object.

#### ASSERT / WTF

`Log.wtf()` means "What a Terrible Failure." Use it only when the state should be impossible, for example:

- your app requires a label list, but the label array is empty after successful initialization
- a required dependency is null after construction

Do not overuse it. For most normal failures, `Log.e()` is sufficient.

### 1.7 Writing Log Statements in Java

First, define a tag constant at the top of your class.

```java
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: Activity created successfully");
    }
}
```

The tag helps you filter quickly. If every class uses a meaningful tag, you can search exactly where an event came from.

#### Complete Example with Multiple Levels

```java
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate: MainActivity launched");
    }

    private void onImageSelected(@Nullable Bitmap bitmap) {
        Log.d(TAG, "onImageSelected: method entered");

        if (bitmap == null) {
            Log.w(TAG, "onImageSelected: bitmap was null");
            return;
        }

        Log.v(TAG, "onImageSelected: width=" + bitmap.getWidth());
        Log.v(TAG, "onImageSelected: height=" + bitmap.getHeight());

        try {
            Log.i(TAG, "onImageSelected: image ready for preprocessing");
        } catch (Exception e) {
            Log.e(TAG, "onImageSelected: unexpected error", e);
        }
    }
}
```

### 1.8 Best Practices for Tags

Good tags are short, consistent, and class-focused.

Recommended examples:

- `MainActivity`
- `ResultActivity`
- `ImageUtils`
- `DiseaseClassifier`
- `HistoryRepository`
- `NetworkClient`

Avoid bad tags such as:

- `TAG123`
- `test`
- `hello`
- `myapp`

A bad tag makes filtering harder.

### 1.9 Filtering Logcat Step by Step

A beginner mistake is to open Logcat and get overwhelmed by thousands of lines. Filtering solves that problem.

You should learn three very common filters:

- by package name
- by tag
- by level

#### Filter by Package Name

Use package filtering when you only want messages related to your app.

Step by step:

1. Open Logcat.
2. In the toolbar, locate the app/process selector.
3. Choose your LeafGuard AI app process.
4. In the search box, type your package name, for example `com.example.leafguardai`.
5. Press Enter.
6. Perform an action in the app.
7. You should now see mostly lines from your own application process.

Why this helps:

- removes noise from the system
- keeps your attention on your code
- makes crash investigation faster

#### Screenshot Description - Package Filter

A useful screenshot would show:

- the package name typed in the Logcat filter box
- only app-specific lines remaining
- one INFO or DEBUG log visible from your app

#### Filter by Tag

Use tag filtering when you want messages from one class only.

Step by step:

1. Add logs using a tag such as `MainActivity`.
2. Open Logcat.
3. Click inside the search/filter field.
4. Type the tag name, for example `MainActivity`.
5. Press Enter.
6. Trigger the feature handled by that class.
7. Only logs from that tag should remain visible.

Why this helps:

- useful when debugging one screen
- easier to follow method order
- useful for onCreate, button clicks, and callback flow

#### Screenshot Description - Tag Filter

The screenshot should show:

- `MainActivity` typed in the filter field
- several lines from the same tag
- one click event, one preprocessing event, and one navigation event if possible

#### Filter by Level

Use level filtering to focus on severity.

Step by step:

1. Open Logcat.
2. Locate the log level selector.
3. Choose `Error` to see only failures, or `Warn` to see warnings and errors.
4. Reproduce the issue.
5. Examine the remaining lines.

When to use it:

- choose ERROR when the app crashes
- choose WARN when the app behaves strangely but does not crash
- choose DEBUG when you need detailed control-flow tracing

#### Combined Filtering

You can combine filters. For example:

- package = your app
- tag = `DiseaseClassifier`
- level = `Error`

This is powerful because it narrows Logcat to the exact component and severity.

### 1.10 Color Coding in Android Studio Logcat

Android Studio typically colors log levels differently. Exact shades depend on your theme, but the idea stays the same.

Common visual pattern:

- VERBOSE = light gray or subdued tone
- DEBUG = blue or neutral highlight
- INFO = green or white depending on theme
- WARN = yellow or orange
- ERROR = red
- ASSERT / WTF = strong red or bold emphasis

Do not rely on color alone. Always read the text, tag, and level. But color helps you scan quickly.

### 1.11 A Sample Stack Trace to Read Line by Line

Below is a realistic Android crash trace. Study it slowly.

```text
E/AndroidRuntime: FATAL EXCEPTION: main
Process: com.example.leafguardai, PID: 5412
java.lang.NullPointerException: Attempt to invoke virtual method 'int android.graphics.Bitmap.getWidth()' on a null object reference
    at com.example.leafguardai.MainActivity.processSelectedImage(MainActivity.java:142)
    at com.example.leafguardai.MainActivity.onActivityResult(MainActivity.java:118)
    at android.app.Activity.dispatchActivityResult(Activity.java:8613)
    at android.app.ActivityThread.deliverResults(ActivityThread.java:5592)
    at android.app.ActivityThread.handleSendResult(ActivityThread.java:5633)
    at android.app.servertransaction.ActivityResultItem.execute(ActivityResultItem.java:54)
    at android.app.servertransaction.ActivityTransactionItem.execute(ActivityTransactionItem.java:45)
    at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:135)
    at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:95)
    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2438)
    at android.os.Handler.dispatchMessage(Handler.java:106)
    at android.os.Looper.loopOnce(Looper.java:201)
    at android.os.Looper.loop(Looper.java:288)
    at android.app.ActivityThread.main(ActivityThread.java:7872)
    at java.lang.reflect.Method.invoke(Native Method)
    at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:548)
    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:936)
```

Now read it line by line.

#### `E/AndroidRuntime: FATAL EXCEPTION: main`

- `E` means ERROR.
- `AndroidRuntime` is the source tag.
- `FATAL EXCEPTION` means the app crashed.
- `main` means the crash occurred on the main UI thread.

This already tells you the crash was serious and user-visible.

#### `Process: com.example.leafguardai, PID: 5412`

- confirms which app process crashed
- useful if multiple apps or test processes are running

#### `java.lang.NullPointerException: ...`

This is the exception type and message. Here, the app tried to call `getWidth()` on a `Bitmap` that was null.

This is the first clue about the root cause.

#### `at com.example.leafguardai.MainActivity.processSelectedImage(MainActivity.java:142)`

This is the most important line. It is the first stack-frame from **your code**.

Interpret it like this:

- class = `MainActivity`
- method = `processSelectedImage`
- file = `MainActivity.java`
- exact line = `142`

This is usually where you should start debugging.

#### `at com.example.leafguardai.MainActivity.onActivityResult(MainActivity.java:118)`

This tells you how the crashing method was reached. `onActivityResult()` called `processSelectedImage()`.

This helps you trace the path of execution.

#### `at android.app.Activity.dispatchActivityResult(...)`

From this point onward, the stack is mostly Android framework code. It is still useful, but usually **not** where you fix the bug.

Important beginner rule:

- first inspect the first line from your own package
- then inspect the line just above it if needed
- do not waste time starting from framework internals

### 1.12 How to Find the Exact Line That Threw the Exception

Use this approach every time:

1. Find the exception type.
2. Move down the trace until you see the first line that belongs to your package.
3. Open that file.
4. Jump to the exact line number.
5. Read 5 to 10 lines above and below it.
6. Ask what variable could be null, out of bounds, closed, or invalid at that point.

In the example above, open `MainActivity.java` and inspect line 142. If line 142 says:

```java
int width = bitmap.getWidth();
```

then the likely issue is obvious: `bitmap` was null.

The real fix is not only, "add a null check." The real fix is to ask:

- Why was bitmap null?
- Did image decoding fail?
- Was the Uri invalid?
- Did the user cancel the picker?

### 1.13 Understanding the `Caused by` Chain

Some stack traces contain more than one exception. The top exception may only describe the outer failure. The real root cause appears later in a `Caused by:` section.

Example:

```text
java.lang.RuntimeException: Failed to decode image for prediction
    at com.example.leafguardai.ImageLoader.loadBitmap(ImageLoader.java:64)
    at com.example.leafguardai.MainActivity.prepareImage(MainActivity.java:103)
Caused by: java.io.FileNotFoundException: open failed: ENOENT (No such file or directory)
    at libcore.io.IoBridge.open(IoBridge.java:574)
    at java.io.FileInputStream.<init>(FileInputStream.java:160)
    at android.content.ContentResolver.openInputStream(ContentResolver.java:1436)
    at com.example.leafguardai.ImageLoader.loadBitmap(ImageLoader.java:52)
```

How to read this:

- outer exception = `RuntimeException`
- outer message = image decoding failed
- `Caused by` = the deeper reason was actually `FileNotFoundException`
- the missing file triggered the decode failure

That means your root cause is not really "decoding logic is broken." Your root cause is "the file path or Uri is invalid."

### 1.14 Your Code vs Library Code

When reading a stack trace, you will often see lines from:

- `android.*`
- `androidx.*`
- `java.*`
- `okhttp3.*`
- `retrofit2.*`
- `org.tensorflow.*`

Those lines matter, but the most actionable line is usually the first one from:

- `com.example.leafguardai.*`
- or whatever package name your project uses

If the top few lines are all library code, keep scanning down until your package appears. That is usually where bad input, wrong configuration, or misuse of the API happened.

### 1.15 Common Android Exceptions You Must Recognize

#### NullPointerException

Meaning: A variable reference was null and you tried to use it.

Common LeafGuard AI causes:

- `Bitmap` not loaded
- `TextView` not found because `findViewById()` used wrong ID
- Intent extra missing
- network response body is null

Typical fix:

- validate before use
- ensure initialization happened
- add defensive null handling
- trace why the value became null

Example:

```java
if (bitmap == null) {
    Log.e(TAG, "Bitmap is null before preprocessing");
    return;
}
```

#### NetworkOnMainThreadException

Meaning: You attempted network access on the main UI thread. Android blocks this to prevent a frozen UI.

Common cause:

- using `HttpURLConnection` or another network API directly inside button click code
- calling `execute()` on a network request from the main thread

Typical fix:

- use Retrofit `enqueue()`
- use WorkManager or another background mechanism
- keep network off the main thread

#### FileNotFoundException

Meaning: The app tried to open a file or stream that does not exist, or is inaccessible.

Common LeafGuard AI causes:

- stale Uri from camera/gallery
- incorrect file path
- missing permission
- deleted cache file

Typical fix:

- verify Uri exists
- log the exact path or Uri
- check permission and file lifetime

#### OutOfMemoryError

Meaning: The app requested more memory than the process could provide.

Common LeafGuard AI causes:

- loading a full-resolution image directly into memory
- holding many Bitmaps without recycling or releasing references
- keeping prediction history thumbnails too large

Typical fix:

- downsample large images
- avoid caching huge Bitmaps unnecessarily
- clear references when the screen closes

### 1.16 Practical Debugging Workflow

Use this workflow every time. Do not skip steps.

#### Step 1: Reproduce

- perform the exact sequence that causes the bug
- note device, OS version, and inputs
- ask whether it happens every time or only sometimes

#### Step 2: Find the Event in Logcat

- clear Logcat if needed
- set filter to your package or tag
- reproduce again
- watch for red ERROR lines, warnings, or your own debug statements

#### Step 3: Read the Stack Trace Carefully

- identify the exception type
- identify the first line from your code
- open the file and line number
- inspect nearby variables and method arguments

#### Step 4: Identify the Root Cause

Avoid fixing only the symptom. Ask why the bad state happened.

Example:

- symptom = `bitmap` is null
- root cause = user cancelled image selection, but your code still called preprocessing

#### Step 5: Fix the Cause

Examples:

- add null guard after image decode
- move network call off the main thread
- release a Cursor in `finally`
- sample down the image before loading it

#### Step 6: Verify the Fix

- rerun the exact failing scenario
- confirm no crash appears
- check Logcat again
- test nearby scenarios to prevent regressions

### 1.17 Sample Logging Strategy for LeafGuard AI

A simple logging strategy is better than random logging.

Suggested places to log:

- Activity `onCreate()`
- camera button click
- gallery result received
- image Uri decoded
- preprocessing start and end
- model inference start and end
- API request start and response
- Room insert success/failure
- navigation to result screen

Suggested level usage:

- INFO for app milestones
- DEBUG for sizes, IDs, confidence values
- WARN for unexpected but recoverable states
- ERROR for failures and exceptions

### 1.18 Example - Logging the Prediction Flow

```java
private static final String TAG = "DiseaseClassifier";

public PredictionResult classify(Bitmap bitmap) {
    Log.i(TAG, "classify: inference requested");

    if (bitmap == null) {
        Log.e(TAG, "classify: bitmap is null");
        return null;
    }

    Log.d(TAG, "classify: input width=" + bitmap.getWidth());
    Log.d(TAG, "classify: input height=" + bitmap.getHeight());

    long start = SystemClock.elapsedRealtime();

    PredictionResult result = runModel(bitmap);

    long duration = SystemClock.elapsedRealtime() - start;
    Log.i(TAG, "classify: inference completed in " + duration + " ms");

    return result;
}
```

### 1.19 Logcat Checklist Before You Submit

Before final submission, make sure you can:

- open Logcat quickly
- filter by package
- filter by tag
- filter by ERROR level
- read the first line from your code in a stack trace
- explain one real bug you fixed using Logcat

---

## 2. JUnit Unit Testing

### 2.1 What Unit Testing Is

A **unit test** checks one small unit of logic in isolation. That unit could be:

- a helper method
- a parser
- a formatter
- a validation method
- a repository method with mocked dependencies

Unit tests are fast, repeatable, and automatic. They help you prove that your business logic works even before you run the full app.

### 2.2 Why Write Tests

Good reasons to write tests in LeafGuard AI:

- confidence formatting should always be correct
- label parsing logic should not break silently
- disease result selection should handle ties and invalid arrays
- data transformation logic should stay stable during refactoring
- regressions are easier to catch before demonstration day

A unit test is like a contract. When the code changes, the test tells you whether the contract still holds.

### 2.3 `test/` vs `androidTest/`

Android projects usually contain two test source sets. They are not the same.

#### `src/test/java`

This folder is for **local unit tests**. These tests run on your computer's JVM, not on a real Android device.

Use it for:

- pure Java logic
- formatter methods
- utility classes
- repository logic with mocks
- parsing and validation code

Advantages:

- runs fast
- easy to repeat frequently
- ideal for logic that does not need Android UI or hardware

#### `src/androidTest/java`

This folder is for **instrumented tests**. These tests run on an emulator or device.

Use it for:

- UI tests with Espresso
- Activity navigation checks
- tests that need Android framework behavior

Simple rule:

- JUnit pure logic -> `test/`
- Espresso UI -> `androidTest/`

### 2.4 JUnit 4 Setup

Common Gradle dependency:

```gradle
testImplementation 'junit:junit:4.13.2'
```

In Java, most JUnit 4 tests use imports like:

```java
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
```

### 2.5 Core JUnit 4 Annotations

#### `@Test`

Marks a method as a test case. If the method completes successfully, the test passes. If an assertion fails or an uncaught exception occurs, the test fails.

#### `@Before`

Runs before each test method. Use it to set up fresh objects.

#### `@After`

Runs after each test method. Use it for cleanup.

#### `@BeforeClass`

Runs once before all tests in the class. Must be `static`. Use it for expensive shared setup.

#### `@AfterClass`

Runs once after all tests in the class. Must be `static`. Use it for shared cleanup.

### 2.6 Example - Annotation Lifecycle

```java
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FormatterLifecycleTest {

    @BeforeClass
    public static void beforeAllTests() {
        System.out.println("Runs once before all tests");
    }

    @Before
    public void setUp() {
        System.out.println("Runs before each test");
    }

    @Test
    public void sampleTestOne() {
        System.out.println("Test one running");
    }

    @Test
    public void sampleTestTwo() {
        System.out.println("Test two running");
    }

    @After
    public void tearDown() {
        System.out.println("Runs after each test");
    }

    @AfterClass
    public static void afterAllTests() {
        System.out.println("Runs once after all tests");
    }
}
```

### 2.7 Assert Methods You Must Know

#### `assertEquals(expected, actual)`

Use when exact equality is expected.

```java
assertEquals("87.3%", result);
```

#### `assertNotNull(object)`

Use when a result must exist.

```java
assertNotNull(predictionResult);
```

#### `assertNull(object)`

Use when a value should intentionally be null.

```java
assertNull(emptySelection);
```

#### `assertTrue(condition)`

Use when a condition should evaluate to true.

```java
assertTrue(confidence > 0.8f);
```

#### `assertFalse(condition)`

Use when a condition should evaluate to false.

```java
assertFalse(labels.isEmpty());
```

#### `assertThrows()`

Use when a method should throw an exception for invalid input.

```java
assertThrows(IllegalArgumentException.class, new Runnable() {
    @Override
    public void run() {
        ConfidenceFormatter.format(-0.5f);
    }
});
```

### 2.8 The 3 A's of Testing

A clean test often follows the **3 A's**:

1. **Arrange** - prepare inputs and objects
2. **Act** - call the method under test
3. **Assert** - verify the result

Example:

```java
@Test
public void formatConfidence_returnsOneDecimalPercentage() {
    // Arrange
    float confidence = 0.8734f;

    // Act
    String result = ConfidenceFormatter.format(confidence);

    // Assert
    assertEquals("87.3%", result);
}
```

This pattern makes tests easy to read, debug, and review.

### 2.9 First Example - Production Helper Class

Here is a complete Java helper class suitable for unit testing.

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

### 2.10 First Example - JUnit Test Class

```java
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ConfidenceFormatterTest {

    @Test
    public void format_returnsPercentageWithOneDecimal() {
        String actual = ConfidenceFormatter.format(0.8734f);
        assertEquals("87.3%", actual);
    }

    @Test
    public void format_returnsZeroPercentForZero() {
        String actual = ConfidenceFormatter.format(0.0f);
        assertEquals("0.0%", actual);
    }

    @Test
    public void format_throwsWhenValueIsNegative() {
        assertThrows(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                ConfidenceFormatter.format(-0.1f);
            }
        });
    }
}
```

This test class is small, fast, and valuable. It proves that your formatting stays correct.

### 2.11 Another Useful Unit Test Target - Disease Result Parsing

LeafGuard AI often needs logic such as:

- reading a probability array
- finding the highest confidence index
- mapping index to label name
- validating array lengths

That is excellent unit-test material.

Example production code:

```java
public class DiseaseResultParser {

    public static String getTopLabel(float[] scores, String[] labels) {
        if (scores == null || labels == null) {
            throw new IllegalArgumentException("Scores and labels must not be null");
        }

        if (scores.length == 0 || labels.length == 0 || scores.length != labels.length) {
            throw new IllegalArgumentException("Scores and labels must be non-empty and equal length");
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

Example test:

```java
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DiseaseResultParserTest {

    @Test
    public void getTopLabel_returnsHighestScoreLabel() {
        float[] scores = {0.10f, 0.72f, 0.18f};
        String[] labels = {"Healthy", "Late Blight", "Powdery Mildew"};

        String result = DiseaseResultParser.getTopLabel(scores, labels);

        assertEquals("Late Blight", result);
    }
}
```

### 2.12 Mockito - Why Mock Dependencies

Real dependencies are often slow, fragile, or hard to control. Examples:

- network APIs depend on internet and server availability
- Room databases involve I/O and Android framework behavior
- sensors and cameras require hardware state

**Mockito** lets you replace a real dependency with a fake object under your control. That means you can test your logic, not the server or hardware.

Common Gradle dependency:

```gradle
testImplementation 'org.mockito:mockito-core:5.12.0'
```

### 2.13 Example - Mocking an API Dependency

Suppose you wrap your Retrofit call in a small interface.

```java
public interface PredictionGateway {
    PredictionResponse fetchLastPrediction();
}
```

Now create logic that depends on this interface.

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

Mockito test:

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

This test runs without internet, without Retrofit, and without Android UI. That is exactly why mocking is powerful.

### 2.14 Example - Mocking a Database Dependency

Suppose you have a DAO-like interface.

```java
import java.util.List;

public interface ScanHistoryDataSource {
    List<String> getAllDiseases();
}
```

Production logic:

```java
import java.util.List;

public class HistoryStatistics {

    private final ScanHistoryDataSource dataSource;

    public HistoryStatistics(ScanHistoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int countHealthyScans() {
        int count = 0;
        List<String> diseases = dataSource.getAllDiseases();

        for (String disease : diseases) {
            if ("Healthy".equals(disease)) {
                count++;
            }
        }

        return count;
    }
}
```

Mockito test:

```java
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HistoryStatisticsTest {

    @Test
    public void countHealthyScans_countsOnlyHealthyRecords() {
        ScanHistoryDataSource dataSource = mock(ScanHistoryDataSource.class);
        when(dataSource.getAllDiseases()).thenReturn(Arrays.asList(
                "Healthy",
                "Late Blight",
                "Healthy",
                "Rust"
        ));

        HistoryStatistics statistics = new HistoryStatistics(dataSource);

        int result = statistics.countHealthyScans();

        assertEquals(2, result);
    }
}
```

### 2.15 What to Unit Test

Strong unit test targets in LeafGuard AI include:

- confidence formatting
- disease label selection
- probability normalization logic
- result summary text generation
- filename generation helpers
- input validation methods
- sorting and filtering of history data
- model output parsing

### 2.16 What NOT to Unit Test

Avoid using local unit tests for things that depend directly on:

- real UI widgets
- camera hardware
- sensors
- actual network requests
- Activity lifecycle behavior
- full Room database behavior unless you set up Android instrumentation or Robolectric

These are better handled with:

- Espresso UI tests
- integration tests
- manual device testing
- mocked abstractions in local tests

### 2.17 Test Coverage

**Test coverage** means the percentage of code executed while your tests run. Higher coverage does **not** automatically mean higher quality, but coverage is still useful.

Coverage can help you see:

- methods never touched by tests
- branches not yet exercised
- logic that needs more verification

### 2.18 How to View Coverage in Android Studio

Step by step:

1. Right-click a test class or the `test` folder.
2. Choose **Run 'Tests in ...' with Coverage**.
3. Wait for execution to finish.
4. Android Studio highlights covered lines.
5. Open the Coverage tool window to view percentages by package, class, and method.

Use coverage as a guide, not a vanity metric. A small set of meaningful tests is better than many shallow tests.

### 2.19 Running Unit Tests

You can run tests in multiple ways.

#### From Android Studio

- right-click a test class
- choose **Run 'ClassNameTest'**
- right-click the whole `test` folder to run all local tests

#### From Gradle

```bash
./gradlew test
```

This is especially useful before pushing code or creating your final submission.

### 2.20 Unit Testing Checklist for Week 11

By the end of this week, you should have at least:

- one test for confidence formatting
- one test for disease result parsing
- one test using Mockito for a fake dependency

That minimum already demonstrates real understanding.

---

## 3. Espresso UI Testing

### 3.1 What Espresso Is

**Espresso** is Android's UI testing framework. It lets you automate user interactions such as:

- tapping buttons
- typing text
- scrolling
- checking whether a view is visible
- verifying that another screen opened

Espresso exists because manual clicking is slow, repetitive, and easy to forget. A UI test gives you a repeatable script.

### 3.2 Why Espresso Matters for LeafGuard AI

LeafGuard AI has critical UI flows such as:

- MainActivity launches
- camera button opens capture flow
- selected image appears
- prediction button triggers work
- result screen opens
- history screen shows saved scans

If one of those flows breaks after a refactor, Espresso can catch it.

### 3.3 `androidTest` vs `test`

Espresso tests belong in:

```text
src/androidTest/java
```

Reason:

- Espresso needs Android framework support
- it interacts with real Views and Activities
- it must run on a device or emulator

Do not place Espresso tests in `src/test/java`. That source set is for local JVM tests only.

### 3.4 Core Espresso Concepts

#### `onView()`

Targets a View in the current UI hierarchy. Example:

```java
onView(withId(R.id.buttonCaptureImage));
```

#### `onData()`

Targets data-backed views such as `ListView` items. You may not need it immediately if your app uses RecyclerView, but it is part of Espresso.

#### `withId()`

Matches a View by resource ID. Example:

```java
withId(R.id.buttonPredict)
```

#### `withText()`

Matches a View by text. Example:

```java
withText("Detect Disease")
```

#### `perform()`

Performs an action on the matched view. Example:

```java
perform(click())
```

#### `check()`

Asserts something about the view. Example:

```java
check(matches(isDisplayed()))
```

### 3.5 Common ViewMatchers

You should know these matchers well.

- `withId()`
- `withText()`
- `isDisplayed()`
- `isEnabled()`
- `isChecked()`

Examples:

```java
onView(withId(R.id.buttonPredict)).check(matches(isDisplayed()));
onView(withText("Healthy")).check(matches(isDisplayed()));
onView(withId(R.id.checkboxTerms)).check(matches(isChecked()));
```

### 3.6 Common ViewActions

Useful actions include:

- `click()`
- `typeText()`
- `scrollTo()`
- `swipeUp()`

Examples:

```java
onView(withId(R.id.editTextName)).perform(typeText("Tomato Leaf"));
onView(withId(R.id.buttonSave)).perform(scrollTo(), click());
onView(withId(R.id.scrollView)).perform(swipeUp());
```

### 3.7 Common ViewAssertions

Useful assertions include:

- `matches()`
- `doesNotExist()`

Examples:

```java
onView(withId(R.id.resultRootLayout)).check(matches(isDisplayed()));
onView(withText("Loading...")).check(doesNotExist());
```

### 3.8 Basic Espresso Dependencies

Typical Gradle entries:

```gradle
androidTestImplementation 'androidx.test.ext:junit:1.1.5'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
androidTestImplementation 'androidx.test.espresso:espresso-intents:3.5.1'
```

Also ensure your default test runner is configured.

```gradle
android {
    defaultConfig {
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }
}
```

### 3.9 Complete Espresso Test Example - Camera Button Tap

The example below assumes:

- `MainActivity` is your launch screen
- the camera button ID is `buttonCaptureImage`
- the result screen is `ResultActivity`

Adjust IDs if your project uses different names.

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
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void cameraButton_isVisibleAndClickable() {
        onView(withId(R.id.buttonCaptureImage))
                .check(matches(isDisplayed()))
                .perform(click());
    }
}
```

This test proves that the button exists and can be pressed. It does **not** prove camera result handling yet. That is okay for a first UI test.

### 3.10 Testing Activity Navigation with Intents

When you want to verify that tapping something opens another Activity, Espresso Intents is helpful.

A classic rule is `IntentsTestRule`. The user request mentions `IntentTestRule`, but in practice many projects use `IntentsTestRule` from Espresso Intents.

Complete example:

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
    public IntentsTestRule<MainActivity> intentsRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void clickingPredictButton_opensResultActivity() {
        onView(withId(R.id.buttonPredict)).perform(click());

        intended(hasComponent(ResultActivity.class.getName()));
    }
}
```

In a real app, you may need to prepare a valid image state first. The idea here is that Espresso can verify navigation, not just visibility.

### 3.11 Full Example - Tap Camera Button and Verify Result Screen Opens

If your MainActivity uses a stubbed image or test mode, you can write a more complete test.

```java
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class PredictionFlowTest {

    @Rule
    public IntentsTestRule<MainActivity> rule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void tapCameraThenPredict_opensResultScreen() {
        onView(withId(R.id.buttonCaptureImage))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.buttonPredict))
                .check(matches(isDisplayed()))
                .perform(click());

        intended(hasComponent(ResultActivity.class.getName()));
    }
}
```

Again, modify IDs and setup as needed for your exact project. The value of this example is the complete structure, imports, rule, action, and assertion.

### 3.12 IdlingResource - Handling Async Operations

Espresso automatically waits for many UI events, but not every asynchronous background task. If your app performs work such as:

- network request
- model inference
- background database insert

then the test may try to check the UI too early.

`IdlingResource` tells Espresso when the app is busy and when it is idle.

Simplified counting example:

```java
import androidx.test.espresso.idling.CountingIdlingResource;

public class EspressoIdlingResource {

    public static final CountingIdlingResource countingIdlingResource =
            new CountingIdlingResource("LeafGuardLoader");

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

Use it in production code around async work:

```java
EspressoIdlingResource.increment();

apiCall.enqueue(new Callback<PredictionResponse>() {
    @Override
    public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
        EspressoIdlingResource.decrement();
    }

    @Override
    public void onFailure(Call<PredictionResponse> call, Throwable t) {
        EspressoIdlingResource.decrement();
    }
});
```

Register it in the test:

```java
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AsyncPredictionTest {

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(EspressoIdlingResource.countingIdlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(EspressoIdlingResource.countingIdlingResource);
    }

    @Test
    public void predictionCompletes_beforeAssertionRuns() {
        // Run UI actions here,
        // then assert after Espresso waits for idle.
    }
}
```

### 3.13 Running Espresso Tests on an Emulator

Step by step:

1. Start an emulator with enough RAM.
2. Wait until the home screen is fully loaded.
3. In Android Studio, right-click your `androidTest` package or a test class.
4. Choose **Run**.
5. Watch the emulator.
6. Espresso will control the UI automatically.
7. Review green or red results in the Run window.

Tips:

- close unnecessary emulator apps
- keep animations disabled if tests are flaky
- use stable IDs in layouts
- avoid relying on hard-coded timing sleeps

### 3.14 Good UI Test Targets in LeafGuard AI

Strong first UI tests include:

- main screen opens successfully
- capture button is visible
- predict button disabled before image selection
- result screen shows after valid prediction flow
- history screen empty state is visible when no records exist

### 3.15 Common Espresso Mistakes

- wrong view ID
- checking before async work finishes
- relying on `Thread.sleep()` instead of IdlingResource
- using text assertions that break when strings are changed
- running tests on a slow emulator and assuming app logic is broken

---

## 4. Android Studio Profiler

### 4.1 Opening the Profiler

Step by step:

1. Run the app on a device or emulator.
2. In Android Studio, click **View**.
3. Move to **Tool Windows**.
4. Click **Profiler**.
5. Select your device.
6. Select the LeafGuard AI process.
7. Choose the profiling area you want to inspect.

The Profiler helps you move from guesses to measurements.

### 4.2 CPU Profiler

The CPU Profiler shows how much processing time your app uses. It is useful when:

- inference feels slow
- scrolling stutters
- app launch is sluggish
- one method blocks the main thread

#### What CPU Profiler Shows

- method execution time
- thread activity
- call stacks
- flame chart or call chart views

#### How to Use It

1. Open CPU Profiler.
2. Start a recording.
3. Perform a slow action, such as running inference on a large image.
4. Stop the recording.
5. Inspect which methods consumed the most time.

#### How to Identify Slow Operations

Look for:

- long blocks on the main thread
- repeated bitmap decode calls
- excessive image resizing loops
- expensive database work on the UI thread

If `preprocessBitmap()` consumes a large share of time, that method becomes your optimization target.

### 4.3 Reading a Flame Chart / Call Chart

A flame chart represents method calls as horizontal blocks. General rules:

- wider block = more time spent
- nested block = method called by another method
- top-level long block = major parent operation

Example interpretation:

- `runPrediction()` is wide
- inside it, `decodeBitmap()` is even wider than `runModel()`
- that means image decoding may be slower than inference itself

This insight is powerful because it tells you where to optimize first.

### 4.4 Memory Profiler

The Memory Profiler helps you inspect RAM usage. Use it when:

- the app crashes with `OutOfMemoryError`
- memory usage keeps growing
- large images are involved
- an Activity seems to stay alive after closing

#### Key Features

- live memory graph
- heap dump capture
- allocation tracking
- object counts by class

#### Heap Dump

A **heap dump** is a snapshot of all objects currently in memory. It helps answer questions like:

- why are 12 large Bitmaps still alive?
- why does MainActivity still exist after rotation?
- what object type is dominating memory?

### 4.5 Taking a Heap Dump

Step by step:

1. Open Memory Profiler.
2. Use the app normally.
3. Trigger the suspected issue.
4. Click the **Dump Java Heap** button.
5. Wait for the heap snapshot to load.
6. Sort objects by retained size or count.
7. Investigate unusually large classes such as `Bitmap` or `MainActivity`.

### 4.6 Allocation Tracking

Allocation tracking shows object creation over time. This is useful when a repeated action creates too many objects, for example:

- decoding a new full-size bitmap on every screen rotation
- creating many temporary arrays during preprocessing
- inflating rows repeatedly without reuse

### 4.7 Network Profiler

The Network Profiler shows real-time network activity. It is useful for cloud prediction features.

You can observe:

- when requests start
- how long responses take
- payload size
- which endpoints are called

For LeafGuard AI, this helps compare:

- offline inference
- cloud inference
- response latency under different network conditions

### 4.8 Energy Profiler

The Energy Profiler helps you estimate battery-heavy behavior. It is relevant when your app:

- keeps background work running too often
- performs repeated network calls
- uses location, sensors, or wake locks

Even if LeafGuard AI is not a background-heavy app, energy still matters. An inefficient app feels unprofessional.

### 4.9 Practical Profiler Workflow

When something feels slow:

1. define the exact action, such as app launch or prediction
2. measure it in Profiler
3. identify the slowest method or biggest allocation
4. optimize one thing at a time
5. measure again
6. compare before and after numbers

Never say, "I optimized performance," without a before-and-after measurement.

---

## 5. Memory Leak Detection

### 5.1 What a Memory Leak Is

A **memory leak** happens when an object is no longer needed, but something still holds a reference to it, so the garbage collector cannot free it.

In Android, this is especially dangerous for:

- `Activity`
- `Fragment`
- `View`
- `Bitmap`
- database resources
- listeners and callbacks

If your app repeatedly leaks old Activities, memory use grows, performance worsens, and crashes become more likely.

### 5.2 Why Android Leaks Are Common

Android screens are created and destroyed often. For example:

- configuration change
- rotation
- back navigation
- opening and closing result screens

If an old Activity is still referenced by a static field, background task, Handler, or long-lived listener, it cannot be collected.

### 5.3 Common Leak Pattern - Static Activity Reference

Bad example:

```java
public class LeakHolder {
    public static MainActivity activity;
}
```

Why it leaks:

- static fields live as long as the app process
- Activity should die when the screen closes
- static reference keeps it alive

Correct idea:

- never store an Activity in a static field
- if you need app-wide context, use `getApplicationContext()` when appropriate

### 5.4 Common Leak Pattern - Anonymous Inner Class Holding Outer Activity

Bad example:

```java
public class MainActivity extends AppCompatActivity {

    private Runnable delayedTask = new Runnable() {
        @Override
        public void run() {
            // This anonymous inner class holds MainActivity implicitly.
        }
    };
}
```

Why it leaks:

- anonymous inner classes keep a hidden reference to the outer Activity
- if the task outlives the Activity, the Activity leaks

Fix options:

- use static inner classes
- cancel tasks in `onDestroy()`
- use lifecycle-aware components where possible

### 5.5 Common Leak Pattern - Non-static Handler in Activity

Bad example:

```java
public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper());

    private void scheduleWork() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // may run after Activity is gone
            }
        }, 10000);
    }
}
```

Why it leaks:

- delayed Runnable may keep Activity alive

Basic fix:

- remove callbacks in `onDestroy()`

```java
@Override
protected void onDestroy() {
    handler.removeCallbacksAndMessages(null);
    super.onDestroy();
}
```

### 5.6 Common Leak Pattern - Unreleased Cursor or Database Resource

Bad example:

```java
Cursor cursor = database.rawQuery("SELECT * FROM scans", null);
while (cursor.moveToNext()) {
    // read rows
}
```

Problem:

- Cursor not closed
- resources stay open longer than necessary

Correct pattern:

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

### 5.7 LeakCanary - What It Is

**LeakCanary** is a popular Android library for detecting memory leaks during development. It watches objects such as Activities and Fragments. If they should be destroyed but remain reachable, LeakCanary reports the leak.

This is much easier than trying to guess leaks manually.

### 5.8 Adding LeakCanary

Typical Gradle dependency:

```gradle
debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.14'
```

Use `debugImplementation` so it is active in debug builds, not in release builds.

### 5.9 Basic LeakCanary Usage

In many modern setups, adding the dependency is enough for Activity leak detection. LeakCanary installs itself automatically in debug builds.

If your app has a custom `Application` class, keep it normal. LeakCanary usually does not need manual setup for standard use.

Example Application class:

```java
import android.app.Application;

public class LeafGuardApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
```

### 5.10 What a LeakCanary Report Means

LeakCanary reports typically show:

- leaking object
- reference chain
- GC root

Interpretation:

- **leaking object** = the object that should have been collected
- **reference chain** = the chain of references keeping it alive
- **GC root** = the top-level reason the chain is reachable

Example reasoning:

- leaking object = `MainActivity`
- reference chain = `SingletonManager -> listener -> MainActivity`
- root = static singleton stored for the whole app

That tells you exactly where to break the chain.

### 5.11 Fixing Leaks with WeakReference

Sometimes you need a reference that should not prevent garbage collection. That is where `WeakReference` can help.

Example:

```java
import java.lang.ref.WeakReference;

public class PredictionCallbackHolder {

    private final WeakReference<MainActivity> activityRef;

    public PredictionCallbackHolder(MainActivity activity) {
        activityRef = new WeakReference<>(activity);
    }

    public void notifyDone() {
        MainActivity activity = activityRef.get();
        if (activity != null && !activity.isFinishing()) {
            activity.showPredictionComplete();
        }
    }
}
```

Important note:

- `WeakReference` is not a magic fix for everything
- use it when a non-owning reference is appropriate
- still prefer lifecycle-aware design and cleanup

### 5.12 Fixing Leaks with Proper Lifecycle Cleanup

Useful cleanup tasks include:

- unregister listeners in `onStop()` or `onDestroy()`
- remove pending callbacks
- clear adapters if needed
- close Cursor and database resources
- cancel in-flight work that is tied to a screen
- null out large bitmap references when the screen no longer needs them

### 5.13 Using Application Context Safely

If you need a long-lived context, use the Application context, not an Activity context.

Good use cases:

- initializing Room database singleton
- creating app-wide repositories
- reading resources that do not depend on a screen theme

Bad use case:

- storing `MainActivity` in a singleton

### 5.14 Leak Detection Checklist

Before final submission, check whether:

- rotating the device repeatedly grows memory abnormally
- opening and closing ResultActivity triggers a leak report
- large Bitmaps stay alive after leaving the screen
- database queries close resources properly

---

## 6. Performance Optimization

### 6.1 Measure Before You Optimize

Performance work should begin with measurement, not assumptions. A feature may feel slow for many reasons:

- large image decode
- main-thread work
- repeated database queries
- network latency
- inefficient RecyclerView updates

Measure first, optimize second, measure again.

### 6.2 Method Timing with `System.currentTimeMillis()`

`System.currentTimeMillis()` returns wall-clock time. It is simple, but can be affected by time changes.

Example:

```java
long start = System.currentTimeMillis();
runPrediction();
long end = System.currentTimeMillis();
Log.d("Perf", "Prediction took " + (end - start) + " ms");
```

Use it for quick rough measurements, but prefer elapsed time APIs for benchmarking.

### 6.3 Method Timing with `SystemClock.elapsedRealtime()`

`SystemClock.elapsedRealtime()` is usually better for timing durations on Android. It measures real elapsed time since boot.

Example:

```java
long start = android.os.SystemClock.elapsedRealtime();
runPrediction();
long duration = android.os.SystemClock.elapsedRealtime() - start;
Log.d("Perf", "Prediction took " + duration + " ms");
```

For Week 11 benchmarks, this is a strong choice.

### 6.4 StrictMode

**StrictMode** helps detect bad behavior during development, especially:

- disk I/O on the main thread
- network on the main thread
- leaked closable objects

Example setup in `onCreate()`:

```java
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        setContentView(R.layout.activity_main);
    }
}
```

Use StrictMode in debug builds, not as a final release-user feature.

### 6.5 Image Loading Optimization

Large images are one of the biggest performance and memory risks in LeafGuard AI. A modern phone camera can produce images far larger than your model input size.

If your model needs something like `224 x 224`, loading a huge full-resolution bitmap first is wasteful.

Use `BitmapFactory.Options` to sample the image down.

```java
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtils {

    public static Bitmap decodeSampledBitmap(String path, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        while ((height / inSampleSize) > reqHeight && (width / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }

        return inSampleSize;
    }
}
```

Benefits:

- lower memory usage
- faster decode time
- less garbage collection pressure

### 6.6 Database Query Optimization

Database work can become slow if queries are careless.

Helpful practices:

- add indexes for frequently searched columns
- avoid loading huge result sets when only a few rows are needed
- query on background threads
- expose data through LiveData or lifecycle-aware patterns

Example Room entity with index:

```java
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "scan_history", indices = {@Index(value = {"timestamp"})})
public class ScanHistoryEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String diseaseName;
    public long timestamp;
}
```

Example DAO query with limit:

```java
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScanHistoryDao {

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC LIMIT 20")
    List<ScanHistoryEntity> getLatestScans();
}
```

Why this matters:

- `LIMIT 20` is faster than loading thousands of rows unnecessarily
- indexes help sorting and searching
- background execution prevents UI jank

### 6.7 RecyclerView Optimization

If your app shows prediction history, RecyclerView performance matters.

Key techniques:

#### ViewHolder Pattern

RecyclerView already uses the ViewHolder pattern to avoid repeated `findViewById()` work. This improves scrolling performance.

#### `setHasFixedSize(true)`

Use it when RecyclerView size does not change because of content changes.

```java
recyclerView.setHasFixedSize(true);
```

This lets RecyclerView optimize layout work.

#### DiffUtil

`DiffUtil` calculates changes between old and new lists efficiently. That means smoother updates and fewer unnecessary redraws.

Benefits:

- better animations
- less work on update
- more efficient history list refresh

### 6.8 APK Size Optimization

A smaller APK installs faster and feels more polished.

Useful techniques:

- enable R8 / ProGuard for release builds
- remove unused resources
- avoid unnecessary large assets
- compress images appropriately

Example release config:

```gradle
buildTypes {
    release {
        minifyEnabled true
        shrinkResources true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

### 6.9 Battery Optimization

Even a student project should avoid unnecessary battery waste.

Good practices:

- avoid keeping wake locks unless absolutely necessary
- do not poll the network repeatedly without reason
- use WorkManager for deferred background tasks
- stop work when the user leaves a screen if the work no longer matters

Example WorkManager idea:

- save history sync
- upload analytics only when charging or connected
- background cleanup instead of immediate heavy work on launch

### 6.10 Performance Targets for LeafGuard AI

Reasonable Week 11 targets:

- app cold launch under 3 seconds
- offline inference under 500 ms on your test device
- cloud request visibly handled with loading state, even if network latency is higher
- history screen opens smoothly without noticeable lag
- no memory leak after repeated screen open/close cycles

### 6.11 Performance Debugging Questions

If the app feels slow, ask:

- Is the work on the main thread?
- Is the image larger than needed?
- Is the same work repeated unnecessarily?
- Is the network the real bottleneck?
- Is scrolling slow because of heavy item binding?
- Is garbage collection caused by too many allocations?

---

## 7. Testing the Complete LeafGuard AI App

### 7.1 Why You Need a Test Plan

A test plan turns random clicking into structured verification. It helps you prove that:

- key features work
- edge cases were considered
- bugs were recorded and fixed
- performance was measured

### 7.2 Test Plan Template

Use a table like this in your report.

| Test ID | Feature | Input / Steps | Expected Output | Actual Output | Status |
|------|------|------|------|------|------|
| TC-01 | App launch | Open app from launcher | Main screen opens within target time | | |
| TC-02 | Camera flow | Tap camera button | Camera opens or permission prompt appears | | |
| TC-03 | Offline inference | Use valid leaf image | Disease result displayed | | |
| TC-04 | Cloud inference | Use valid leaf image with internet | Server response shown | | |
| TC-05 | History | Save scan and open history | Record appears in list | | |

### 7.3 Twenty Specific Test Cases for LeafGuard AI

Below are 20 concrete cases you can actually use.

| ID | Scenario | Input | Expected Output |
|------|------|------|------|
| TC-01 | Launch app | Open from launcher | MainActivity appears in under 3 seconds |
| TC-02 | Camera permission allow | Tap camera and allow permission | Camera opens successfully |
| TC-03 | Camera permission deny | Tap camera and deny permission | Friendly permission message shown |
| TC-04 | Gallery image select | Pick a valid plant image | Preview image appears |
| TC-05 | Predict without image | Tap predict before selecting image | Button disabled or warning shown |
| TC-06 | Healthy leaf offline | Use clear healthy leaf photo | Healthy or expected class shown with confidence |
| TC-07 | Diseased leaf offline | Use clear diseased leaf photo | Correct or plausible disease shown |
| TC-08 | Healthy leaf cloud | Use clear healthy leaf photo with internet | Cloud result displayed successfully |
| TC-09 | Diseased leaf cloud | Use clear diseased leaf photo with internet | Cloud result displayed successfully |
| TC-10 | No internet cloud | Disable internet and try cloud mode | Error shown without crash |
| TC-11 | Slow internet cloud | Use limited network | Loading indicator remains until response |
| TC-12 | Save result | Complete prediction and save history | Record stored successfully |
| TC-13 | Open history | Navigate to history screen | Previous scans are listed |
| TC-14 | Empty history | Clear database and open history | Empty state message shown |
| TC-15 | Rotate result screen | Rotate device on result screen | Result remains valid or state restored |
| TC-16 | Rapid predict taps | Tap predict multiple times quickly | Duplicate work prevented |
| TC-17 | Large image | Use high-resolution image | App does not crash; image is resized |
| TC-18 | Non-plant image | Use desk or wall photo | App handles result gracefully |
| TC-19 | Blurry leaf image | Use blurred plant photo | Low confidence or graceful handling shown |
| TC-20 | Dark image | Use underexposed leaf image | App still responds and explains low confidence if needed |

### 7.4 Edge Cases Specific to Plant Disease Detection

LeafGuard AI is not just a generic CRUD app. The image quality itself affects correctness. Test these edge cases deliberately:

- blurry image
- very dark image
- very bright image
- partially cropped leaf
- background with many distracting objects
- non-plant image
- multiple leaves in one frame
- damaged but unknown disease pattern
- leaf too far from camera
- leaf too close and out of focus

For each edge case, record:

- image condition
- predicted class
- confidence
- whether the UI handled it safely
- whether the app should warn the user to retake the image

### 7.5 Performance Benchmarks to Achieve Before Submission

Suggested benchmark targets:

- offline inference average < 500 ms
- app launch < 3 seconds
- history query < 200 ms for recent items
- no frozen UI during prediction
- no OOM on large gallery images

Cloud inference may be slower because it depends on network. Instead of demanding a fixed universal number, record:

- request start time
- response end time
- average of 10 runs
- best and worst run

### 7.6 Benchmark Recording Template

| Mode | Run 1 | Run 2 | Run 3 | Run 4 | Run 5 | Run 6 | Run 7 | Run 8 | Run 9 | Run 10 | Average |
|------|------|------|------|------|------|------|------|------|------|------|------|
| Offline | | | | | | | | | | | |
| Cloud | | | | | | | | | | | |

### 7.7 Bug Documentation Template

When you find a bug, document it like this:

- **Bug ID:** BUG-01
- **Feature:** Result screen
- **Steps to reproduce:** Open app -> select image -> tap predict
- **Observed behavior:** App crashes before result screen
- **Expected behavior:** Result screen should open
- **Logcat evidence:** include exception line
- **Root cause:** null bitmap passed to preprocessor
- **Fix:** added decode validation and disabled predict when image missing
- **Verification:** repeated test 5 times without crash

### 7.8 CSE 2206 Viva Prep Q&A

#### Q1: Why do we test an Android app?
A: We test to verify correctness, find bugs before users see them, prevent regressions, and prove that the app is reliable under normal and edge-case conditions.

#### Q2: Why is Logcat important?
A: Logcat shows runtime messages, errors, and stack traces. It helps identify the exact line and reason behind failures.

#### Q3: What is the difference between unit testing and UI testing?
A: Unit tests check small logic components in isolation. UI tests verify full user interactions on a device or emulator.

#### Q4: Why does Espresso belong in `androidTest`?
A: Because Espresso needs Android framework components, Views, and Activities, which are only available in instrumented tests.

#### Q5: Why do we use Mockito?
A: Mockito lets us replace real dependencies, such as API or database layers, with controlled fake behavior for reliable tests.

#### Q6: What is a stack trace?
A: A stack trace is the ordered list of method calls that were active when an exception happened. It helps locate the exact file and line that caused the problem.

#### Q7: What is a memory leak?
A: A memory leak happens when unused objects remain referenced, so garbage collection cannot free them. This increases memory use and can lead to crashes.

#### Q8: Why should large Bitmaps be downsampled?
A: Downsampling reduces memory consumption, improves decode speed, and lowers the chance of `OutOfMemoryError`.

#### Q9: What does StrictMode do?
A: StrictMode detects bad development-time behavior such as disk or network work on the main thread and leaked closable resources.

#### Q10: Why should performance be measured instead of guessed?
A: Because the real bottleneck may not be where we assume. Profilers and timing code provide evidence for accurate optimization.

---

## 8. Week 11 Viva and Submission Readiness

### 8.1 Minimum Technical Story You Should Be Able to Tell

Before final submission, you should be ready to explain:

- how you used Logcat to find at least one real bug
- how to read the first meaningful line in a stack trace
- the difference between `test/` and `androidTest/`
- how a JUnit test is structured
- how Espresso verifies a UI flow
- how LeakCanary detects leaks
- what performance numbers you measured for offline and cloud prediction

### 8.2 Minimum Week 11 Deliverables

You should leave Week 11 with:

- at least 3 passing JUnit unit tests
- at least 1 passing Espresso test
- documented Logcat evidence for 3 bugs or issues investigated
- recorded performance measurements
- LeakCanary checked in debug build
- a completed reflection and validation checklist

### 8.3 Final Thought

Testing is not a "last-minute extra." It is part of engineering.

If Week 10 made your app look good, Week 11 makes it trustworthy.

A strong Week 11 submission tells your teacher:

- you can build features
- you can debug professionally
- you can measure quality
- you understand Android development beyond copying code

## Appendix: Quick Debugging Reminders

- Read the exception type before reading the whole stack.
- The first line from your own package is usually the best starting point.
- Reproduce bugs with the same input more than once.
- Use clear TAG names so filtering stays simple.
- Measure offline and cloud prediction separately.
- Downsample images before sending them into the model or UI preview.
- Close Cursor and stream objects promptly.
- Prefer repeatable tests over one-time manual guesses.
- Capture screenshots of Logcat, Profiler, and LeakCanary for evidence.
- Retest after every fix so you prove the bug is actually gone.

## Appendix: Quick Debugging Reminders

- Read the exception type before reading the whole stack.
- The first line from your own package is usually the best starting point.
- Reproduce bugs with the same input more than once.
- Use clear TAG names so filtering stays simple.
- Measure offline and cloud prediction separately.
- Downsample images before sending them into the model or UI preview.
- Close Cursor and stream objects promptly.
- Prefer repeatable tests over one-time manual guesses.
- Capture screenshots of Logcat, Profiler, and LeakCanary for evidence.
- Retest after every fix so you prove the bug is actually gone.

## Appendix: Quick Debugging Reminders

- Read the exception type before reading the whole stack.
- The first line from your own package is usually the best starting point.
- Reproduce bugs with the same input more than once.
- Use clear TAG names so filtering stays simple.
- Measure offline and cloud prediction separately.
- Downsample images before sending them into the model or UI preview.
- Close Cursor and stream objects promptly.
- Prefer repeatable tests over one-time manual guesses.
- Capture screenshots of Logcat, Profiler, and LeakCanary for evidence.
- Retest after every fix so you prove the bug is actually gone.

## Appendix: Quick Debugging Reminders

- Read the exception type before reading the whole stack.
- The first line from your own package is usually the best starting point.
- Reproduce bugs with the same input more than once.
- Use clear TAG names so filtering stays simple.
- Measure offline and cloud prediction separately.
- Downsample images before sending them into the model or UI preview.
- Close Cursor and stream objects promptly.
- Prefer repeatable tests over one-time manual guesses.
- Capture screenshots of Logcat, Profiler, and LeakCanary for evidence.
- Retest after every fix so you prove the bug is actually gone.
