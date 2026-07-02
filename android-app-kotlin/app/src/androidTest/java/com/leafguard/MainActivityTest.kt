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
 * UI (instrumented) test: launches the real MainActivity on an emulator or a
 * connected phone and checks that the two capture buttons are on screen.
 *
 * A UI test runs the actual app and looks at the screen like a user would.
 * Run it in Android Studio by right-clicking this file and choosing
 * "Run 'MainActivityTest'" (an emulator must be running), or from a terminal
 * in android-app-kotlin/ with:
 *   ./gradlew connectedDebugAndroidTest        (macOS/Linux)
 *   gradlew.bat connectedDebugAndroidTest      (Windows)
 */
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
