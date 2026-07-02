# Week 11 Solution — Line-by-line walkthrough of LeafGuard's real tests

This solution walks through the **four real, passing tests** that already ship with
LeafGuard AI. There are no other test files — earlier drafts of this document described
tests (fictional ViewModel and Activity tests plus DAO/XML tests) for classes that **do not
exist** in the app; those have been replaced here with the genuine tests you can open,
read, and run today.

The Kotlin track (`android-app-kotlin/`) is the **primary** one, so every test is shown in
Kotlin first, with the Java twin (`android-app/`) labelled **"Java (secondary)"**.

The four real tests are:

| # | Test | Kind | Where |
|---|------|------|-------|
| 1 | `PredictionResponseTest` | Unit (runs on your computer) | `app/src/test/java/com/leafguard/network/` |
| 2 | `MainActivityTest` | UI / instrumented (runs on an emulator) | `app/src/androidTest/java/com/leafguard/` |

Each of the two tests exists in **both** tracks (Kotlin + Java), giving four files total.

**Vocabulary refresher** (see [`../../GLOSSARY.md`](../../GLOSSARY.md)):

- **Unit test** — a tiny program that checks one small piece of your code, on your
  computer, with no phone or emulator needed.
- **UI (instrumented) test** — a test that runs the real app on an emulator or phone and
  checks the screen like a user would. It uses **Espresso**, Android's UI-testing tool.
- **JUnit** — the framework that finds and runs methods marked `@Test`.
- **Gson** — Google's library for turning JSON text into Kotlin/Java objects and back.
- **Assertion** — a line such as `assertEquals(expected, actual)` that makes the test fail
  if reality doesn't match what you expected.

---

## 1. Unit test — `PredictionResponseTest` (Kotlin, primary)

**File:** `android-app-kotlin/app/src/test/java/com/leafguard/network/PredictionResponseTest.kt`

This test proves the app can correctly read the JSON that the FastAPI backend returns from
`POST /predict`. The most important detail it locks down: the JSON field is spelled
**`disease`** (not `disease_name`). If someone renamed that field, this test would go red.

```kotlin
package com.leafguard.network

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

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
        assertEquals("Small brown spots with concentric rings.", response.symptoms)
        assertEquals("Remove infected leaves.", response.treatment)
        assertEquals("Rotate crops.", response.prevention)
    }

    @Test
    fun missingOptionalFieldsAreNullNotCrash() {
        val json = """{"disease": "Potato Healthy", "confidence": 88.0}"""

        val response = Gson().fromJson(json, PredictionResponse::class.java)

        assertEquals("Potato Healthy", response.disease)
        assertEquals(88.0f, response.confidence, 0.001f)
        assertEquals(null, response.symptoms)
    }
}
```

### Line-by-line

- **`package com.leafguard.network`** — the test lives in the same package as the class it
  tests (`PredictionResponse`), so it can see it without extra imports.
- **`import com.google.gson.Gson`** — brings in Gson, which does the JSON → object work.
- **`import org.junit.Assert.assertEquals` / `import org.junit.Test`** — JUnit's
  `@Test` marker and the `assertEquals` check.
- **`class PredictionResponseTest`** — a plain class; JUnit will create one instance per
  test method and run every method marked `@Test`.
- **`@Test fun parsesDiseaseFieldFromServerJson()`** — the first test. The name reads like
  a sentence describing what it verifies. Good test names are documentation.
- **The triple-quoted `json` string** — Kotlin's `"""..."""` lets us paste multi-line JSON
  exactly as the server sends it. `.trimIndent()` removes the leading spaces so the string
  is clean JSON. Notice the field name is `"disease"`.
- **`Gson().fromJson(json, PredictionResponse::class.java)`** — creates a Gson instance and
  asks it to build a `PredictionResponse` object out of the JSON text. `PredictionResponse`
  uses `@SerializedName("disease")` internally so the JSON `disease` maps to the Kotlin
  property `disease`.
- **`assertEquals("Tomato Early Blight", response.disease)`** — the first assertion:
  "I expected the disease to be `Tomato Early Blight`; fail if it isn't."
- **`assertEquals(92.5f, response.confidence, 0.001f)`** — comparing decimals (floats) needs
  a *tolerance* (the third argument, `0.001f`) because floating-point maths is never exact.
  This says "close enough to 92.5 within one-thousandth".
- **Second test `missingOptionalFieldsAreNullNotCrash()`** — sends JSON with only the two
  required fields. It proves the app won't crash when `symptoms`, `treatment`, and
  `prevention` are absent; Gson simply leaves them `null`.

### What it proves

1. The app and server agree on the field name `disease`.
2. Numbers parse into the right type with the right value.
3. Missing optional fields become `null` instead of crashing the app.

---

## 2. Unit test — `PredictionResponseTest` (Java, secondary)

**File:** `android-app/app/src/test/java/com/leafguard/network/PredictionResponseTest.java`

The Java twin behaves identically. Two differences to notice:

1. The JSON is built with string concatenation and escaped quotes (`\"`) because Java has
   no triple-quoted strings.
2. Values are read through **getter methods** (`response.getDisease()`) instead of Kotlin
   properties (`response.disease`).

```java
package com.leafguard.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.Gson;
import org.junit.Test;

public class PredictionResponseTest {

    @Test
    public void parsesDiseaseFieldFromServerJson() {
        String json = "{"
                + "\"disease\": \"Tomato Early Blight\","
                + "\"confidence\": 92.5,"
                + "\"symptoms\": \"Small brown spots with concentric rings.\","
                + "\"treatment\": \"Remove infected leaves.\","
                + "\"prevention\": \"Rotate crops.\""
                + "}";

        PredictionResponse response = new Gson().fromJson(json, PredictionResponse.class);

        assertEquals("Tomato Early Blight", response.getDisease());
        assertEquals(92.5f, response.getConfidence(), 0.001f);
        assertEquals("Small brown spots with concentric rings.", response.getSymptoms());
        assertEquals("Remove infected leaves.", response.getTreatment());
        assertEquals("Rotate crops.", response.getPrevention());
    }

    @Test
    public void missingOptionalFieldsAreNullNotCrash() {
        String json = "{\"disease\": \"Potato Healthy\", \"confidence\": 88.0}";

        PredictionResponse response = new Gson().fromJson(json, PredictionResponse.class);

        assertEquals("Potato Healthy", response.getDisease());
        assertEquals(88.0f, response.getConfidence(), 0.001f);
        assertNull(response.getSymptoms());
    }
}
```

- **`assertNull(response.getSymptoms())`** — Java uses the dedicated `assertNull` where the
  Kotlin version used `assertEquals(null, ...)`. Both mean the same thing.

---

## 3. UI test — `MainActivityTest` (Kotlin, primary)

**File:** `android-app-kotlin/app/src/androidTest/java/com/leafguard/MainActivityTest.kt`

This test launches the *real* `MainActivity` on an emulator or connected phone and checks
that the two capture buttons — `buttonOpenCamera` and `buttonOpenGallery` — are actually on
screen. It is the smallest possible "does the home screen even open?" safety net.

```kotlin
package com.leafguard

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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

### Line-by-line

- **`androidTest` folder** — files here are *instrumented* tests. They are packaged into a
  tiny test app and installed alongside your app on a device/emulator. That is why they
  need hardware (or an emulator) to run.
- **The Espresso imports** — `onView`, `matches`, `isDisplayed`, `withId` are Espresso's
  building blocks: find a view, then assert something about it.
- **`@RunWith(AndroidJUnit4::class)`** — tells JUnit to use the Android test runner, which
  knows how to talk to a real device.
- **`@get:Rule val activityRule = ActivityScenarioRule(MainActivity::class.java)`** — a
  JUnit *rule* that automatically launches `MainActivity` before each test and closes it
  afterwards, so every test starts from a clean screen. (`@get:Rule` is Kotlin's way of
  putting the annotation on the property's getter, which JUnit requires.)
- **`onView(withId(R.id.buttonOpenCamera))`** — "find the view whose id is
  `buttonOpenCamera`." These ids come from `res/layout/activity_main.xml`.
- **`.check(matches(isDisplayed()))`** — the assertion: "check that this view matches the
  condition *is displayed*." If the button is missing or hidden, the test fails.

### What it proves

The app builds, `MainActivity` launches without crashing, and its two most important
buttons are visible to the user. This catches the biggest, most embarrassing bugs first.

---

## 4. UI test — `MainActivityTest` (Java, secondary)

**File:** `android-app/app/src/androidTest/java/com/leafguard/MainActivityTest.java`

The Java twin is identical apart from Java syntax: `static` imports for the Espresso
helpers, a public `@Rule` field, and `ActivityScenarioRule<MainActivity>` written with
angle brackets instead of Kotlin's `::class.java`.

```java
package com.leafguard;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void captureButtonsAreVisibleOnLaunch() {
        onView(withId(R.id.buttonOpenCamera)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonOpenGallery)).check(matches(isDisplayed()));
    }
}
```

---

## 5. How to run all four tests

### Unit tests (fast, no emulator)

In **Android Studio**: open `PredictionResponseTest`, click the green ▶ arrow next to the
class name, and choose **Run 'PredictionResponseTest'**.

From a **terminal**, in the `android-app-kotlin/` folder (repeat in `android-app/` for the
Java twin):

```bash
./gradlew testDebugUnitTest        # macOS/Linux
gradlew.bat testDebugUnitTest      # Windows
```

**Expected result:** `2 tests passed` and `BUILD SUCCESSFUL`. Android Studio shows two
green ticks; the terminal prints an HTML report path under
`app/build/reports/tests/`.

### UI (instrumented) tests (need an emulator)

1. Start an emulator: Android Studio → **Device Manager** → ▶ next to a virtual device.
2. In **Android Studio**: open `MainActivityTest` and click the green ▶ → **Run
   'MainActivityTest'**.
3. Or from a **terminal** in `android-app-kotlin/`:

```bash
./gradlew connectedDebugAndroidTest        # macOS/Linux
gradlew.bat connectedDebugAndroidTest      # Windows
```

**Expected result:** `1 test passed` and `BUILD SUCCESSFUL`. If you see **"No connected
devices"**, your emulator isn't running — start it and try again.

---

## 6. Friendly failure notes

- **`Unresolved reference: PredictionResponse`** — you're editing the wrong module. The
  Kotlin test must sit under `android-app-kotlin/`, the Java test under `android-app/`.
- **`BUILD SUCCESSFUL` but "0 tests"** — you ran `test` on a module with no tests, or the
  method is missing `@Test`. Double-check the annotation and the file location
  (`src/test` for unit, `src/androidTest` for UI).
- **UI test hangs then fails with a timeout** — the emulator was still booting. Wait for
  the home screen, then re-run.
- **`No connected devices!`** — start an emulator (or plug in a phone with USB debugging on)
  before running `connectedDebugAndroidTest`.
- **Float assertion fails by a tiny amount** — remember the tolerance argument
  (`0.001f`); never compare floats for exact equality.

---

## 7. Test dependencies (already in `app/build.gradle` — do not add)

Both tracks already declare everything these tests need, so you should **not** edit any
Gradle file this week:

```gradle
testImplementation 'junit:junit:4.13.2'
testImplementation 'org.mockito:mockito-core:5.7.0'
androidTestImplementation 'androidx.test.ext:junit:1.1.5'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
androidTestImplementation 'androidx.test:runner:1.5.2'
androidTestImplementation 'androidx.test:rules:1.5.0'
```

---

## 8. Sample test report table for your submission

Combine the automated results above with your own manual tests. Fill in the "Actual" and
"Status" columns as you run each one.

| Test ID | Type | Feature | Scenario | Expected | Actual | Status |
|---|---|---|---|---|---|---|
| TC01 | Unit | JSON contract | Parse full `/predict` reply | `disease` = "Tomato Early Blight" | | |
| TC02 | Unit | JSON contract | Parse reply missing optional fields | `symptoms` is null, no crash | | |
| TC03 | UI | Home screen | Launch `MainActivity` | `buttonOpenCamera` visible | | |
| TC04 | UI | Home screen | Launch `MainActivity` | `buttonOpenGallery` visible | | |
| TC05 | Manual | Camera | Tap **Open Camera** | Camera opens | | |
| TC06 | Manual | Gallery | Tap **Open Gallery** | Picker opens | | |
| TC07 | Manual | Cloud detect | Detect with server running | Result screen shows disease + % | | |
| TC08 | Manual | Offline detect | Airplane mode + offline toggle | TFLite/heuristic result shown | | |
| TC09 | Manual | History | Open History after a scan | Saved scan appears | | |
| TC10 | Manual | Share | Tap Share on a result | Android share sheet opens | | |

---

## 9. Week 11 checklist

- [x] Located the two real unit-test files (`PredictionResponseTest`, Kotlin + Java)
- [x] Located the two real UI-test files (`MainActivityTest`, Kotlin + Java)
- [x] Ran `./gradlew testDebugUnitTest` and saw **2 tests passed / BUILD SUCCESSFUL**
- [x] Ran `./gradlew connectedDebugAndroidTest` on an emulator and saw **BUILD SUCCESSFUL**
- [x] Built a combined automated + manual test report table

<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

- 📝 [Back to Week 11 Exercises](../../roadmap/week-11-testing-debugging-performance/exercises.md) — Try it yourself first
- 📖 [Week 11 README](../../roadmap/week-11-testing-debugging-performance/README.md) — Week overview
- 💡 [Solutions Index for Week 11](README.md) — Other solutions this week
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

---
