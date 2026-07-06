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

/**
 * UI (instrumented) test for ScanActivity — the "Scan" tab that now hosts the
 * image capture/upload flow that used to live directly on MainActivity.
 *
 * Run it in Android Studio by right-clicking this file and choosing
 * "Run 'ScanActivityTest'" (an emulator must be running), or from a terminal
 * in android-app-kotlin/ with:
 *   ./gradlew connectedDebugAndroidTest        (macOS/Linux)
 *   gradlew.bat connectedDebugAndroidTest      (Windows)
 */
@RunWith(AndroidJUnit4::class)
class ScanActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(ScanActivity::class.java)

    @Test
    fun uploadAreaIsVisibleOnLaunch() {
        onView(withId(R.id.cardUploadArea)).check(matches(isDisplayed()))
    }
}
