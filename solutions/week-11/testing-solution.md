# Week 11 Solution - Unit Tests, Espresso Tests, DAO Tests, and Test Report

This solution provides a complete Week 11 testing pack for LeafGuard AI.

It includes:
- `ScanViewModelTest.java` with 5 Mockito-based unit tests
- `ScanActivityTest.java` with 3 Espresso scenarios
- `DiseaseXmlParserTest.java`
- `ScanHistoryDaoTest.java`
- a sample report table with 15 passing test cases

---

## 1. `ScanViewModelTest.java`

```java
package com.leafguard.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import android.graphics.Bitmap;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.leafguard.data.model.PredictionResult;
import com.leafguard.data.repository.ScanRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ScanViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ScanRepository repository;
    private ScanViewModel viewModel;
    private Bitmap bitmap;

    @Before
    public void setUp() {
        repository = mock(ScanRepository.class);
        viewModel = new ScanViewModel(repository);
        bitmap = mock(Bitmap.class);
    }

    @Test
    public void initialState_isIdle() {
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
        assertEquals(null, viewModel.getPrediction().getValue());
        assertEquals(null, viewModel.getErrorMessage().getValue());
    }

    @Test
    public void analyzeImage_withNullBitmap_setsError() {
        viewModel.analyzeImage(null, true);

        assertEquals("Please select an image first.", viewModel.getErrorMessage().getValue());
        verify(repository, never()).analyzeImage(any(), eq(true), any());
    }

    @Test
    public void analyzeImage_success_updatesPrediction() {
        PredictionResult result = new PredictionResult("Tomato Early Blight", 0.91f, "spots", "spray", "rotate");

        doAnswer(invocation -> {
            ScanRepository.PredictionCallback callback = invocation.getArgument(2);
            callback.onSuccess(result);
            return null;
        }).when(repository).analyzeImage(any(Bitmap.class), eq(true), any());

        viewModel.analyzeImage(bitmap, true);

        assertNotNull(viewModel.getPrediction().getValue());
        assertEquals("Tomato Early Blight", viewModel.getPrediction().getValue().getDiseaseName());
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void analyzeImage_failure_updatesErrorMessage() {
        doAnswer(invocation -> {
            ScanRepository.PredictionCallback callback = invocation.getArgument(2);
            callback.onError("Network error");
            return null;
        }).when(repository).analyzeImage(any(Bitmap.class), eq(false), any());

        viewModel.analyzeImage(bitmap, false);

        assertEquals("Network error", viewModel.getErrorMessage().getValue());
        assertFalse(Boolean.TRUE.equals(viewModel.getLoading().getValue()));
    }

    @Test
    public void saveResult_callsRepository() {
        PredictionResult result = new PredictionResult("Potato Late Blight", 0.88f, "dark patches", "remove leaves", "inspect often");

        viewModel.saveResult(result, "content://leaf/sample.jpg");

        verify(repository).saveScan(result, "content://leaf/sample.jpg");
    }
}
```

---

## 2. Production assumptions for the ViewModel test

The test above assumes this public API exists:

```java
public class ScanViewModel extends ViewModel {
    public ScanViewModel(ScanRepository repository) { ... }
    public LiveData<Boolean> getLoading() { ... }
    public LiveData<PredictionResult> getPrediction() { ... }
    public LiveData<String> getErrorMessage() { ... }
    public void analyzeImage(Bitmap bitmap, boolean useCloud) { ... }
    public void saveResult(PredictionResult result, String imageUri) { ... }
}
```

---

## 3. `ScanActivityTest.java`

```java
package com.leafguard;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ScanActivityTest {

    @Rule
    public ActivityScenarioRule<ScanActivity> activityRule = new ActivityScenarioRule<>(ScanActivity.class);

    @Test
    public void screen_showsCoreViews() {
        onView(withId(R.id.imagePreview)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonCapture)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonPick)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonAnalyze)).check(matches(isDisplayed()));
    }

    @Test
    public void analyzeButton_isDisabledInitially() {
        onView(withId(R.id.buttonAnalyze)).check(matches(org.hamcrest.Matchers.not(isEnabled())));
        onView(withId(R.id.textImageStatus)).check(matches(withText(R.string.no_image_selected)));
    }

    @Test
    public void captureAndPickButtons_areClickable() {
        onView(withId(R.id.buttonCapture)).perform(click());
        onView(withId(R.id.buttonPick)).perform(click());
        onView(withId(R.id.imagePreview)).check(matches(isDisplayed()));
    }
}
```

---

## 4. `DiseaseXmlParserTest.java`

```java
package com.leafguard.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.leafguard.data.model.Disease;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DiseaseXmlParserTest {

    private DiseaseXmlParser parser;

    @Before
    public void setUp() {
        parser = new DiseaseXmlParser();
    }

    @Test
    public void parse_returnsAllDiseases() throws Exception {
        String xml = "<diseases>"
                + "<disease><label>Tomato___Early_blight</label><commonName>Tomato Early Blight</commonName><symptoms>spots</symptoms><treatment>spray</treatment><prevention>rotate</prevention></disease>"
                + "<disease><label>Apple___Apple_scab</label><commonName>Apple Scab</commonName><symptoms>scab</symptoms><treatment>fungicide</treatment><prevention>prune</prevention></disease>"
                + "</diseases>";

        List<Disease> diseases = parser.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        assertEquals(2, diseases.size());
        assertEquals("Tomato Early Blight", diseases.get(0).getCommonName());
    }

    @Test
    public void findByLabel_returnsCachedDisease() throws Exception {
        String xml = "<diseases><disease><label>Tomato___healthy</label><commonName>Tomato Healthy</commonName><symptoms>none</symptoms><treatment>none</treatment><prevention>monitor</prevention></disease></diseases>";
        parser.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        Disease disease = parser.findByLabel("Tomato___healthy");

        assertNotNull(disease);
        assertEquals("Tomato Healthy", disease.getCommonName());
    }

    @Test
    public void findByLabel_returnsNullForMissingLabel() throws Exception {
        String xml = "<diseases><disease><label>Potato___healthy</label><commonName>Potato Healthy</commonName><symptoms>none</symptoms><treatment>none</treatment><prevention>monitor</prevention></disease></diseases>";
        parser.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        assertNull(parser.findByLabel("Apple___Black_rot"));
    }
}
```

---

## 5. `ScanHistoryDaoTest.java`

```java
package com.leafguard.data.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ScanHistoryDaoTest {

    private AppDatabase database;
    private ScanHistoryDao dao;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = database.scanHistoryDao();
    }

    @After
    public void tearDown() throws IOException {
        database.close();
    }

    @Test
    public void insertAndReadBack_scanHistory() {
        ScanHistory history = new ScanHistory(
                "Tomato Early Blight",
                0.91f,
                "spots",
                "spray",
                "rotate",
                "content://leaf/1.jpg",
                23.7801,
                90.4075,
                System.currentTimeMillis()
        );

        long rowId = dao.insert(history);
        List<ScanHistory> allRows = dao.getAll();

        assertNotNull(rowId);
        assertEquals(1, allRows.size());
        assertEquals("Tomato Early Blight", allRows.get(0).getDiseaseName());
    }

    @Test
    public void delete_removesRow() {
        ScanHistory history = new ScanHistory(
                "Potato Healthy",
                0.98f,
                "none",
                "none",
                "monitor",
                "content://leaf/2.jpg",
                0.0,
                0.0,
                System.currentTimeMillis()
        );

        dao.insert(history);
        ScanHistory firstRow = dao.getAll().get(0);
        dao.delete(firstRow);

        assertEquals(0, dao.getAll().size());
    }
}
```

---

## 6. Gradle test dependencies

```gradle
testImplementation 'junit:junit:4.13.2'
testImplementation 'org.mockito:mockito-core:5.8.0'
testImplementation 'androidx.arch.core:core-testing:2.2.0'
androidTestImplementation 'androidx.test.ext:junit:1.1.5'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
androidTestImplementation 'androidx.test.espresso:espresso-intents:3.5.1'
androidTestImplementation 'androidx.test:runner:1.5.2'
androidTestImplementation 'androidx.test:rules:1.5.0'
androidTestImplementation 'androidx.room:room-testing:2.6.1'
```

---

## 7. Sample test report table

| Test ID | Feature | Scenario | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|
| TC01 | Main screen | App launch | Main buttons visible | Buttons rendered | Pass |
| TC02 | Scan screen | Open ScanActivity | Image preview and buttons visible | Visible | Pass |
| TC03 | Camera flow | Tap capture | Camera contract starts | Started | Pass |
| TC04 | Gallery flow | Tap pick | Picker path starts | Started | Pass |
| TC05 | ScanViewModel | Null bitmap | Error message shown | Correct error shown | Pass |
| TC06 | ScanViewModel | Cloud success | Prediction LiveData updated | Updated | Pass |
| TC07 | ScanViewModel | Offline failure | Error LiveData updated | Updated | Pass |
| TC08 | XML parser | Parse 2 diseases | List size = 2 | Got 2 entries | Pass |
| TC09 | XML parser | Lookup by label | Correct disease returned | Returned | Pass |
| TC10 | XML parser | Missing label | Null returned | Null returned | Pass |
| TC11 | Room DAO | Insert history | Row inserted | Row inserted | Pass |
| TC12 | Room DAO | Query history | Data returned in list | Returned | Pass |
| TC13 | Room DAO | Delete history | Row removed | Removed | Pass |
| TC14 | Espresso | Analyze disabled initially | Button disabled | Disabled | Pass |
| TC15 | Notifications/Share smoke test | Result screen opens | UI stable | Stable | Pass |

---

## 8. Commands to run tests

```bash
# local unit tests
./gradlew test

# instrumentation tests
./gradlew connectedAndroidTest
```

---

## 9. Week 11 checklist

- [x] 5 Mockito unit tests included
- [x] 3 Espresso scenarios included
- [x] XML parser tests included
- [x] Room in-memory DAO test included
- [x] 15-case report table included
