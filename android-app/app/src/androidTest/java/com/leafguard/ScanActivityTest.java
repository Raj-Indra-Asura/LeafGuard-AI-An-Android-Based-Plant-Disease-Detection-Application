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

/**
 * Java twin of ScanActivityTest.kt — UI (instrumented) test for ScanActivity,
 * the "Scan" tab that now hosts the image capture/upload flow that used to
 * live directly on MainActivity.
 *
 * Run it in Android Studio by right-clicking this file and choosing
 * "Run 'ScanActivityTest'" (an emulator must be running), or from a terminal
 * in android-app/ with:
 *   ./gradlew connectedDebugAndroidTest        (macOS/Linux)
 *   gradlew.bat connectedDebugAndroidTest      (Windows)
 */
@RunWith(AndroidJUnit4.class)
public class ScanActivityTest {

    @Rule
    public ActivityScenarioRule<ScanActivity> activityRule =
            new ActivityScenarioRule<>(ScanActivity.class);

    @Test
    public void uploadAreaIsVisibleOnLaunch() {
        onView(withId(R.id.cardUploadArea)).check(matches(isDisplayed()));
    }
}
