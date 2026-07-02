package com.leafguard.network

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit test for the JSON contract between the app and the FastAPI backend.
 *
 * A unit test is a tiny program that checks one small piece of your code on your
 * computer — no phone or emulator needed. Run it in Android Studio by
 * right-clicking this file and choosing "Run 'PredictionResponseTest'", or from
 * a terminal in android-app-kotlin/ with:
 *   ./gradlew testDebugUnitTest        (macOS/Linux)
 *   gradlew.bat testDebugUnitTest      (Windows)
 */
class PredictionResponseTest {

    @Test
    fun parsesDiseaseFieldFromServerJson() {
        // This is exactly the shape of JSON that POST /predict returns.
        // Note the field is "disease" (NOT "disease_name").
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
        // The app must not crash if the server sends only the required fields.
        val json = """{"disease": "Potato Healthy", "confidence": 88.0}"""

        val response = Gson().fromJson(json, PredictionResponse::class.java)

        assertEquals("Potato Healthy", response.disease)
        assertEquals(88.0f, response.confidence, 0.001f)
        assertEquals(null, response.symptoms)
    }
}
