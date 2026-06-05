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


## Step 4: Write ViewModel Unit Tests with Mockito

Your ViewModel is where UI state, repository calls, and result formatting usually meet. This makes it one of the most valuable layers to test in Week 11.

### What You Should Test in a ViewModel

Focus on behaviors such as:
- loading state changes correctly
- repository methods are called when expected
- success results are exposed to UI state
- error messages are exposed when failures happen
- helper actions such as clearing messages or saving results work

### Add One More Test Dependency if Missing

```gradle
testImplementation "androidx.arch.core:core-testing:2.2.0"
```

This gives you `InstantTaskExecutorRule`, which makes `LiveData` update immediately during local unit tests.

### Example Supporting Classes

The exact names in your app may differ. The point is to test **behavior**, not Android framework code.

```java
public class PredictionResult {
    private final String diseaseName;
    private final float confidence;

    public PredictionResult(String diseaseName, float confidence) {
        this.diseaseName = diseaseName;
        this.confidence = confidence;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public float getConfidence() {
        return confidence;
    }
}
```

```java
public interface ScanRepository {
    PredictionResult classifyOffline(Bitmap bitmap);
    void saveScan(ScanHistory scanHistory);
    List<ScanHistory> loadHistory();
}
```

```java
public class ScanViewModel extends ViewModel {

    private final ScanRepository repository;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<PredictionResult> predictionResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ScanViewModel(ScanRepository repository) {
        this.repository = repository;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<PredictionResult> getPredictionResult() {
        return predictionResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void classifyOffline(Bitmap bitmap) {
        loading.setValue(true);
        try {
            PredictionResult result = repository.classifyOffline(bitmap);
            predictionResult.setValue(result);
            errorMessage.setValue(null);
        } catch (Exception e) {
            errorMessage.setValue(e.getMessage());
        } finally {
            loading.setValue(false);
        }
    }

    public void saveScan(ScanHistory scanHistory) {
        repository.saveScan(scanHistory);
    }

    public void clearError() {
        errorMessage.setValue(null);
    }
}
```

### Full Java Test Class with 6 Test Methods

```java
@RunWith(MockitoJUnitRunner.class)
public class ScanViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private ScanRepository repository;

    @Mock
    private Bitmap bitmap;

    private ScanViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new ScanViewModel(repository);
    }

    @Test
    public void loading_isFalseByDefault() throws Exception {
        Boolean loading = LiveDataTestUtil.getOrAwaitValue(viewModel.getLoading());
        assertFalse(loading);
    }

    @Test
    public void classifyOffline_success_setsPredictionResult() throws Exception {
        PredictionResult fakeResult = new PredictionResult("Late Blight", 0.91f);
        when(repository.classifyOffline(bitmap)).thenReturn(fakeResult);

        viewModel.classifyOffline(bitmap);

        PredictionResult observed = LiveDataTestUtil.getOrAwaitValue(viewModel.getPredictionResult());
        assertNotNull(observed);
        assertEquals("Late Blight", observed.getDiseaseName());
        assertEquals(0.91f, observed.getConfidence(), 0.001f);
    }

    @Test
    public void classifyOffline_failure_setsErrorMessage() throws Exception {
        when(repository.classifyOffline(bitmap)).thenThrow(new IllegalStateException("Model not loaded"));

        viewModel.classifyOffline(bitmap);

        String error = LiveDataTestUtil.getOrAwaitValue(viewModel.getErrorMessage());
        assertEquals("Model not loaded", error);
    }

    @Test
    public void classifyOffline_callsRepositoryOnce() {
        when(repository.classifyOffline(bitmap)).thenReturn(new PredictionResult("Healthy", 0.99f));

        viewModel.classifyOffline(bitmap);

        verify(repository, times(1)).classifyOffline(bitmap);
    }

    @Test
    public void saveScan_callsRepositorySave() {
        ScanHistory scanHistory = new ScanHistory("Healthy", 0.99, System.currentTimeMillis());

        viewModel.saveScan(scanHistory);

        verify(repository).saveScan(scanHistory);
    }

    @Test
    public void clearError_resetsErrorLiveData() throws Exception {
        when(repository.classifyOffline(bitmap)).thenThrow(new IllegalArgumentException("Bad bitmap"));
        viewModel.classifyOffline(bitmap);

        viewModel.clearError();

        assertNull(LiveDataTestUtil.getOrAwaitValue(viewModel.getErrorMessage()));
    }
}
```

### `LiveDataTestUtil` Helper

```java
public final class LiveDataTestUtil {

    private LiveDataTestUtil() {
    }

    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws Exception {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);

        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T value) {
                data[0] = value;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);

        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw new TimeoutException("LiveData value was never set.");
        }

        return (T) data[0];
    }
}
```

### What These Tests Prove

1. the default UI state is predictable
2. the repository is actually called
3. success data reaches observers
4. errors are turned into UI-friendly messages
5. save actions are delegated correctly

### Checklist
- [ ] `InstantTaskExecutorRule` added
- [ ] Mockito mock for repository created
- [ ] Full ViewModel test class written
- [ ] At least 6 test methods pass
- [ ] Error path and success path both tested

---

## Step 5: Write Espresso UI Tests for the Main Scan Flow

UI tests verify that the user journey still works when buttons, permissions, and screens interact together.

### Goal of the Main Scan Flow Test

You should verify a path similar to this:
1. launch app
2. tap **Camera** button
3. verify permission handling
4. simulate image capture
5. verify preview or result screen updates

### Add Useful UI Test Dependencies

```gradle
androidTestImplementation 'androidx.test:rules:1.5.0'
androidTestImplementation 'androidx.test.uiautomator:uiautomator:2.3.0'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
androidTestImplementation 'androidx.test.espresso:espresso-intents:3.5.1'
```

### Important Note About Permission Dialog Testing

The Android permission dialog belongs to the system UI, not your app. For reliable automation:
- use **`GrantPermissionRule`** for the happy path
- use **UiAutomator** or a fresh-install manual check when you want to see the actual dialog

### Full Espresso Test Class for the Granted-Permission Flow

```java
@RunWith(AndroidJUnit4.class)
public class MainScanFlowTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule cameraPermissionRule =
            GrantPermissionRule.grant(Manifest.permission.CAMERA);

    @Before
    public void initIntents() {
        Intents.init();
    }

    @After
    public void releaseIntents() {
        Intents.release();
    }

    @Test
    public void launchApp_cameraButtonVisible() {
        onView(withId(R.id.btnCamera))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickCameraButton_launchesCameraIntent() {
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent()));

        onView(withId(R.id.btnCamera)).perform(click());

        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));
    }

    @Test
    public void clickCameraButton_thenAnalyzeButtonRemainsEnabledAfterMockCapture() {
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent()));

        onView(withId(R.id.btnCamera)).perform(click());

        onView(withId(R.id.btnAnalyze))
                .check(matches(isDisplayed()));
    }
}
```

### Optional Permission Dialog Test Using UiAutomator

Use this only on a fresh install or after manually revoking camera permission.

```java
@RunWith(AndroidJUnit4.class)
public class CameraPermissionDialogTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void clickCameraButton_showsPermissionDialog_whenPermissionNotGranted() throws Exception {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = ApplicationProvider.getApplicationContext();

        InstrumentationRegistry.getInstrumentation().getUiAutomation()
                .executeShellCommand("pm revoke " + context.getPackageName() + " android.permission.CAMERA");

        onView(withId(R.id.btnCamera)).perform(click());

        device.wait(Until.hasObject(By.textContains("camera")), 3000);
        assertTrue(device.hasObject(By.textContains("camera")));
    }
}
```

### Mocking the Captured Image Result

Real camera tests are fragile because they depend on device camera apps. For course work, it is acceptable to:
- intercept the camera intent with Espresso Intents
- return `RESULT_OK`
- verify your app moves to the next UI state

If your app needs an actual file at the returned output Uri, create a tiny test JPEG in app cache before clicking the camera button. Document that helper in your report.

### Checklist
- [ ] `GrantPermissionRule` configured
- [ ] Camera button visibility test written
- [ ] Camera intent launch verified
- [ ] Fresh-install permission dialog verified manually or with UiAutomator
- [ ] Happy path UI test passes on emulator/device

---

## Step 6: Memory Leak Detection with LeakCanary

Memory leaks happen when objects that should be destroyed remain referenced. In an image-heavy app like LeafGuard, leaks quickly become noticeable.

### Add LeakCanary Dependency

```gradle
debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.14'
```

### How to Run It

1. install a **debug** build
2. open and close MainActivity, ResultActivity, and HistoryActivity several times
3. rotate device if your app supports rotation
4. wait for LeakCanary notification
5. open the trace and inspect the reference chain

### How to Interpret a Leak Trace

A typical trace tells you:
- **leaking object**: the Activity, Fragment, Adapter, or View that should have been destroyed
- **retained size**: roughly how much memory stays alive because of the leak
- **reference chain**: the path of objects still pointing to the leaked object

Example reading approach:
1. find the leaked class name, such as `HistoryActivity`
2. read upward to see what still holds it
3. identify the line of your code or pattern causing the strong reference

### Common Leak 1: Context Leak in a Static Field

**Bad code:**

```java
public class AppSession {
    public static Context appContext;
}
```

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppSession.appContext = this;
    }
}
```

**Why it leaks:**
A static field lives for the whole app process. If it stores an Activity context, that Activity cannot be garbage collected.

**Fix:** store application context only when needed.

```java
public class AppSession {
    private static Context appContext;

    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
}
```

### Common Leak 2: Non-Static Inner Class Holding Activity Reference

**Bad code:**

```java
public class HistoryActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());

    private class RefreshRunnable implements Runnable {
        @Override
        public void run() {
            // Uses outer activity implicitly
        }
    }
}
```

**Why it leaks:**
A non-static inner class automatically holds a reference to the outer Activity.

**Fix:** use a static inner class with a `WeakReference`.

```java
public class HistoryActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());

    private static class RefreshRunnable implements Runnable {
        private final WeakReference<HistoryActivity> activityRef;

        RefreshRunnable(HistoryActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            HistoryActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            // Safe work here
        }
    }
}
```

### Common Leak 3: Listener or Observer Not Unregistered

**Bad code:**

```java
public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter("leafguard.UPLOAD_DONE"));
    }
}
```

**Why it leaks:**
If the listener remains registered after the Activity stops, the system still references the Activity.

**Fix:** unregister in the matching lifecycle method.

```java
@Override
protected void onStop() {
    super.onStop();
    unregisterReceiver(receiver);
}
```

### Leak-Prevention Checklist for LeafGuard

- clear adapters or listeners in `onDestroy()` if needed
- shut down executors when screen is destroyed
- avoid static references to Activities or Views
- do not keep giant bitmaps longer than necessary
- unregister receivers, observers, or callbacks you registered manually

### Checklist
- [ ] LeakCanary dependency added
- [ ] Debug build tested across major screens
- [ ] Leak trace interpreted correctly
- [ ] Three common leak patterns understood
- [ ] Any discovered leak documented and fixed

---

## Step 7: Performance Profiling Session

Performance work should be evidence-driven. Use Android Studio profilers instead of guessing.

### A. CPU Profiler: Record a Method Trace

Use CPU Profiler when the app feels slow during:
- image decode
- bitmap resize
- model inference
- RecyclerView rendering

**How to record:**
1. run debug build on emulator or device
2. open **Profiler** in Android Studio
3. select your process
4. click **CPU**
5. choose **Record** → **Method Trace**
6. perform one full scan flow
7. stop recording

**How to interpret it:**
- **Total Time** = time including child method calls
- **Self Time** = time spent only in that specific method
- methods with high total time are orchestration hotspots
- methods with high self time are often direct optimization targets

**What to look for in LeafGuard:**
- repeated bitmap decoding
- resizing the same image multiple times
- expensive JSON parsing on main thread
- database work accidentally happening on UI thread

### B. Memory Profiler: Track Allocations During Image Processing

Image apps often allocate too much memory because large photos are decoded carelessly.

**Profiling routine:**
1. start memory recording
2. open camera or gallery image
3. decode preview image
4. create model-sized bitmap
5. run inference
6. navigate back and repeat once

**Look for:**
- multiple large `Bitmap` instances alive at once
- old `Activity` instances not being destroyed
- many short-lived arrays during repeated compression

### C. Example Before/After Bitmap Optimization Numbers

These are sample evidence numbers you can reproduce or adapt in your report:

| Scenario | Decoded Size | Approx Memory | Time |
|----------|--------------|---------------|------|
| Original camera image | 4000 x 3000 | 45.8 MB | 230 ms |
| Scaled preview | 1024 x 768 | 3.0 MB | 58 ms |
| Model input bitmap | 224 x 224 | 0.19 MB | 8 ms |

### What Changed?

- **Before optimization:** full-size bitmap decoded directly for preview and inference
- **After optimization:** image first sampled down, then resized again for the model

This is exactly the type of evidence examiners like because it shows both engineering reasoning and measurable improvement.

### D. Use System Trace / Systrace for Frame Rendering Analysis

In recent Android Studio versions, **System Trace** replaces the old Systrace workflow for many use cases.

**Use it when:**
- scrolling history list stutters
- opening result screen causes dropped frames
- loading a preview image freezes the UI briefly

**Process:**
1. open CPU Profiler
2. choose **System Trace**
3. interact with your app during image selection and result display
4. inspect the main thread timeline for long blocks
5. inspect frame rendering to see skipped or delayed frames

**Red flags:**
- main thread blocked by bitmap decode
- network request parsing on main thread
- many layout passes caused by repeated view updates

### E. StrictMode in Debug Builds

StrictMode detects bad habits such as disk or network work on the main thread.

```java
public class LeafGuardApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedClosableObjects()
                    .detectActivityLeaks()
                    .penaltyLog()
                    .build());
        }
    }
}
```

### How to Use StrictMode Results

If Logcat reports a violation:
1. note the exact operation
2. identify the method causing it
3. move the work to a background thread if appropriate
4. retest and confirm the violation no longer appears

### Performance Session Deliverables

- one CPU trace screenshot
- one memory profiler screenshot
- before/after bitmap loading numbers
- one paragraph describing the biggest bottleneck found
- one paragraph describing the optimization you applied

### Checklist
- [ ] CPU Method Trace recorded
- [ ] Memory allocations observed during image processing
- [ ] Before/after bitmap numbers documented
- [ ] System Trace used for frame analysis
- [ ] StrictMode enabled in debug builds

---

## Step 8: Create the Final Test Report

Your final report should show that testing was planned, executed, and evidenced.

### Test Case Table Template

| ID | Test Name | Input / Action | Expected Result | Actual Result | Pass/Fail |
|----|-----------|----------------|-----------------|---------------|-----------|
| TC-01 | | | | | |
| TC-02 | | | | | |

### Sample Filled Test Cases for LeafGuard AI

| ID | Test Name | Input / Action | Expected Result | Actual Result | Pass/Fail |
|----|-----------|----------------|-----------------|---------------|-----------|
| TC-01 | Launch main screen | Open app from launcher | Main screen loads without crash | Main screen loaded in 1.2s | Pass |
| TC-02 | Camera permission request | Tap Camera on fresh install | Permission dialog appears | Dialog appeared with Allow/Don't Allow | Pass |
| TC-03 | Capture image flow | Grant permission and take photo | Preview image shown | Preview displayed correctly | Pass |
| TC-04 | Gallery pick flow | Select one leaf image from gallery | Selected image preview shown | Preview displayed | Pass |
| TC-05 | Offline inference | Run TFLite on valid leaf image | Disease and confidence displayed | Result shown in 620 ms | Pass |
| TC-06 | Cloud inference | Send same image to FastAPI backend | Backend prediction displayed | Result shown in 1850 ms | Pass |
| TC-07 | Save history | Tap Save after result | New history row stored in Room | History row visible in list | Pass |
| TC-08 | Delete history item | Long-press a row and confirm delete | Row removed from database and list | Row removed successfully | Pass |
| TC-09 | Search history | Search for "blight" | Matching rows only shown | 2 matching rows shown | Pass |
| TC-10 | Invalid image handling | Use unsupported/corrupt image | Friendly error message appears | Toast shown, app stable | Pass |

### How to Add Screenshots as Evidence

For each important scenario, include:
- one screenshot of the screen before the action
- one screenshot of the result after the action
- one screenshot of Logcat, profiler, or test output when useful

Suggested evidence set:
- JUnit results panel showing green tests
- Espresso results panel
- LeakCanary notification or no-leak confirmation
- profiler screenshots for CPU and Memory
- scan result screen
- Room history list screen

### What to Include in the Testing Chapter of the Final Report

1. **Testing objective**
   - why testing matters for medical/agricultural decision support apps
2. **Environment**
   - device or emulator name
   - Android version
   - RAM and CPU if known
3. **Unit testing summary**
   - helper methods tested
   - ViewModel tests written
4. **UI testing summary**
   - user flows covered by Espresso
5. **Manual testing summary**
   - permission flow, camera, gallery, offline, cloud, history
6. **Performance findings**
   - average offline vs cloud times
   - biggest performance bottleneck
7. **Debugging findings**
   - crashes or bugs discovered
   - how they were fixed
8. **Memory and leak summary**
   - LeakCanary results
   - memory optimization notes
9. **Final readiness statement**
   - whether the app is stable enough for demo and submission

### Suggested Final Conclusion Paragraph

> LeafGuard AI was tested using local unit tests, instrumented UI tests, manual scenario testing, and Android Studio performance tools. The final build completed the full image selection, prediction, and history workflow without crashes in the tested scenarios. Bitmap scaling reduced memory usage significantly, and no critical LeakCanary issues remained in the final debug build.

---

## Complete Checklist

### Setup
- [ ] JUnit added
- [ ] Mockito added
- [ ] Espresso added
- [ ] LeakCanary added
- [ ] core-testing dependency added for LiveData tests
- [ ] Gradle sync successful

### ViewModel Unit Tests
- [ ] 6 ViewModel test methods written
- [ ] success path tested
- [ ] error path tested
- [ ] repository interaction verified with Mockito
- [ ] tests pass consistently

### UI Tests
- [ ] Camera button visibility test added
- [ ] camera intent launch tested
- [ ] granted-permission flow tested
- [ ] permission dialog verified manually or with UiAutomator

### Debugging and Performance
- [ ] LeakCanary run on debug build
- [ ] CPU trace recorded
- [ ] memory profiler session completed
- [ ] before/after bitmap metrics documented
- [ ] StrictMode enabled in debug build

### Documentation
- [ ] Final test case table completed
- [ ] 10 sample or real test scenarios documented
- [ ] screenshots collected as evidence
- [ ] validation checklist completed
