package com.leafguard.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.Gson;

import org.junit.Test;

/**
 * Java twin of PredictionResponseTest.kt — unit test for the JSON contract
 * between the app and the FastAPI backend.
 *
 * A unit test is a tiny program that checks one small piece of your code on your
 * computer — no phone or emulator needed. Run it in Android Studio by
 * right-clicking this file and choosing "Run 'PredictionResponseTest'", or from
 * a terminal in android-app/ with:
 *   ./gradlew testDebugUnitTest        (macOS/Linux)
 *   gradlew.bat testDebugUnitTest      (Windows)
 */
public class PredictionResponseTest {

    @Test
    public void parsesDiseaseFieldFromServerJson() {
        // This is exactly the shape of JSON that POST /predict returns.
        // Note the field is "disease" (NOT "disease_name").
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
        // The app must not crash if the server sends only the required fields.
        String json = "{\"disease\": \"Potato Healthy\", \"confidence\": 88.0}";

        PredictionResponse response = new Gson().fromJson(json, PredictionResponse.class);

        assertEquals("Potato Healthy", response.getDisease());
        assertEquals(88.0f, response.getConfidence(), 0.001f);
        assertNull(response.getSymptoms());
    }
}
