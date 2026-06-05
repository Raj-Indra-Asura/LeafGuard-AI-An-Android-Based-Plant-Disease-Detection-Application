# Week 11: Quiz - Testing, Debugging, and Performance

## Instructions
- **Total Questions:** 30 (15 conceptual + 15 practical)
- **Recommended Time:** 45-60 minutes
- **Passing Score:** 24/30 (80%)
- **Goal:** Confirm that you understand Logcat,
  JUnit,
  Espresso,
  memory leaks,
  and performance profiling

---

## Part A: Conceptual Questions

### Question 1
**What is the main purpose of Logcat?**
A) To design XML layouts
B) To inspect runtime logs,
warnings,
and crashes
C) To publish APKs
D) To create Room entities

Your answer: _____

### Question 2
**True or False: `src/test/java` is usually the correct place for Espresso UI tests.**

Your answer: _____

### Question 3
**Which log level is best for a suspicious but recoverable situation?**
A) VERBOSE
B) WARN
C) ERROR
D) ASSERT

Your answer: _____

### Question 4
**What is usually the best starting line in a stack trace?**
A) The first line from your own package
B) The last line only
C) Any `android.*` line
D) The process ID line only

Your answer: _____

### Question 5
**True or False: Mockito is used to fake dependencies in tests.**

Your answer: _____

### Question 6
**What does `@Before` do in JUnit 4?**
A) Runs once after all tests
B) Runs before each test
C) Disables a test
D) Measures coverage

Your answer: _____

### Question 7
**Which source set should contain Espresso tests?**
A) `src/main/java`
B) `src/test/java`
C) `src/androidTest/java`
D) `src/debug/java`

Your answer: _____

### Question 8
**True or False: A memory leak means objects are being released correctly.**

Your answer: _____

### Question 9
**Which library is commonly used to detect Android memory leaks during development?**
A) LeakCanary
B) Retrofit
C) Gson
D) Glide

Your answer: _____

### Question 10
**What does test coverage describe?**
A) The number of users who tested the app
B) How much code executed while tests ran
C) APK size after release
D) Internet speed during testing

Your answer: _____

### Question 11
**True or False: `SystemClock.elapsedRealtime()` is usually better than `System.currentTimeMillis()` for elapsed duration measurement on Android.**

Your answer: _____

### Question 12
**Why should large Bitmaps be sampled down before loading?**
A) To reduce memory use and improve speed
B) To enable Retrofit
C) To disable LeakCanary
D) To remove permissions

Your answer: _____

### Question 13
**What is Espresso mainly designed to test?**
A) SQL schema only
B) UI interactions
C) Java compiler warnings
D) APK signing

Your answer: _____

### Question 14
**True or False: StrictMode can warn about disk or network work on the main thread.**

Your answer: _____

### Question 15
**What are the 3 A's of testing?**
A) Arrange,
Act,
Assert
B) Add,
Analyze,
Approve
C) Access,
Allocate,
Apply
D) Alert,
Avoid,
Assign

Your answer: _____

---

## Part B: Practical / Code-Based Questions

### Question 16
**What is wrong with this tag declaration?**
```java
private static final String TAG = MainActivity;
```
A) Nothing
B) It should be a string literal in quotes
C) It must be lowercase
D) It must be final int

Your answer: _____

### Question 17
**Which line correctly logs an exception?**
A) `Log.e(TAG, "Prediction failed", e);`
B) `Log.i(e);`
C) `print(e);`
D) `System.gc();`

Your answer: _____

### Question 18
**True or False: `MainActivity.java:142` in a stack trace tells you the exact source line to inspect first.**

Your answer: _____

### Question 19
**Which assertion correctly checks that `result` equals `"87.3%"`?**
A) `assertTrue(result);`
B) `assertEquals("87.3%", result);`
C) `assertNull(result);`
D) `assertFalse(result);`

Your answer: _____

### Question 20
**What does this annotation mean?**
```java
@BeforeClass
public static void setUpOnce() { }
```
A) Runs before each test
B) Runs once before all tests in the class
C) Runs after every test
D) Marks the class as ignored

Your answer: _____

### Question 21
**Which folder should contain this file?**
```java
@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest { }
```
A) `src/test/java`
B) `src/androidTest/java`
C) `src/main/java`
D) `src/release/java`

Your answer: _____

### Question 22
**Fill in the missing Espresso action.**
```java
onView(withId(R.id.buttonPredict)).perform(__________);
```
A) `assertEquals()`
B) `click()`
C) `matches()`
D) `withText()`

Your answer: _____

### Question 23
**True or False: `matches(isDisplayed())` checks whether a view is visible on screen.**

Your answer: _____

### Question 24
**What is the main problem with this code?**
```java
public class LeakHolder {
    public static MainActivity activity;
}
```
A) It improves performance
B) It can leak the Activity
C) It is required by LeakCanary
D) It is needed for Espresso

Your answer: _____

### Question 25
**Which Profiler is most useful for investigating suspected memory leaks?**
A) CPU Profiler
B) Memory Profiler
C) Network Profiler
D) Energy Profiler

Your answer: _____

### Question 26
**Complete the timing code using the better elapsed-time API.**
```java
long start = ______________________________;
runPrediction();
long duration = ______________________________ - start;
```

Your answer: _______________________________________________

### Question 27
**What does `detectNetwork()` in StrictMode help detect?**
A) Hidden XML layout bugs
B) Network work on the main thread during development
C) RecyclerView jank only
D) APK signing errors

Your answer: _____

### Question 28
**Which code most directly reduces large-image memory use?**
A) `options.inSampleSize = 4;`
B) `Log.d(TAG, "bitmap");`
C) `Thread.sleep(1000);`
D) `assertNotNull(bitmap);`

Your answer: _____

### Question 29
**Why is this query more efficient than loading every row?**
```java
@Query("SELECT * FROM scan_history ORDER BY timestamp DESC LIMIT 20")
List<ScanHistoryEntity> getLatestScans();
```
A) It deletes old rows automatically
B) `LIMIT 20` avoids unnecessary data loading
C) It disables indexing
D) It forces main-thread execution

Your answer: _____

### Question 30
**You measured offline = 320 ms average and cloud = 1480 ms average. What is the best conclusion?**
A) Offline was faster under the tested conditions
B) Cloud is always better in every situation
C) The measurements are useless
D) Offline mode is broken

Your answer: _____

---

## Answers

### Part A
1. **B** - Logcat is used to inspect runtime logs,
warnings,
and crashes.
2. **False** - Espresso belongs in `src/androidTest/java`.
3. **B** - WARN is for suspicious but recoverable states.
4. **A** - The first line from your own package is usually the most useful start.
5. **True** - Mockito creates fake dependencies.
6. **B** - `@Before` runs before each test.
7. **C** - Espresso tests belong in `src/androidTest/java`.
8. **False** - A memory leak means objects are not being released correctly.
9. **A** - LeakCanary detects memory leaks.
10. **B** - Coverage shows how much code ran while tests executed.
11. **True** - `elapsedRealtime()` is better for duration timing.
12. **A** - Sampling down images reduces memory use and improves speed.
13. **B** - Espresso is mainly for UI testing.
14. **True** - StrictMode can catch disk/network misuse on the main thread.
15. **A** - Arrange,
Act,
Assert.

### Part B
16. **B** - `MainActivity` should be a string literal in quotes.
17. **A** - That line logs both a message and the exception object.
18. **True** - The file and line number show where to inspect first.
19. **B** - `assertEquals("87.3%", result);` is correct.
20. **B** - `@BeforeClass` runs once before all tests in the class.
21. **B** - AndroidJUnit4 / Espresso tests go in `src/androidTest/java`.
22. **B** - `click()` is the action passed to `perform()`.
23. **True** - `matches(isDisplayed())` checks visibility.
24. **B** - A static Activity reference can leak the screen.
25. **B** - Memory Profiler is most useful for suspected memory leaks.
26. **`android.os.SystemClock.elapsedRealtime()`** for both blanks.
27. **B** - It detects network operations on the main thread during development.
28. **A** - `inSampleSize` reduces memory use when decoding large images.
29. **B** - `LIMIT 20` avoids loading unnecessary rows.
30. **A** - Offline was faster under the tested conditions.

---

## Scoring Guide
- **27-30:** Excellent
- **24-26:** Good
- **21-23:** Review weak areas
- **Below 21:** Re-read the notes and retry
