# Week 11 Learning Notes: Evidence Before Release

## Purpose

These notes explain test layers, pure testability seams, Room CRUD/migration testing, Activity/Espresso checks, offline performance smoke tests, backend/model regressions, lint, and evidence-based debugging.

Section 12 contains all 14 new/expanded files in full. JVM/backend/Keras/TFLite tests passed; instrumentation sources compiled and require a connected Android device to execute.

## 1. How Week 11 Grows From Week 10

Weeks 01-10 build behavior. Week 11 adds confidence in behavior without changing the product contract.

```text
production feature
  -> smallest stable contract
  -> repeatable test
  -> expected assertion
  -> evidence and residual risk
```

## 2. Test Pyramid

Use the cheapest layer that can falsify the hypothesis:

| Layer | Good for | Avoid using it for |
|---|---|---|
| JVM unit | formatting, JSON, static catalog | Android framework UI |
| Instrumentation | Room, Activity, assets, TFLite | every small calculation |
| Python | API/model/conversion contracts | Android UI |
| Manual | permissions, chooser, notification timing | deterministic logic already automatable |

More tests at lower layers keep feedback fast.

## 3. Arrange, Act, Assert

Every focused test should be readable as:

```text
Arrange known input/state
Act once
Assert observable result
```

One test may contain several assertions about one contract. Avoid tests that depend on order, real network, clock timing, or another test's state.

## 4. Pure Formatter Seam

ResultActivity originally constructed share text and percentage directly. Week 11 extracts `ResultTextFormatter`:

- no Android Context
- deterministic input/output
- preserves rounding and text
- privacy exclusions can be asserted

This is a testability refactor, not a feature change.

## 5. JVM Contract Tests

Three classes produce four tests:

1. percentage rounding
2. share text includes guidance/disclaimer and excludes coordinates
3. all eight API JSON fields parse
4. XML catalog has 10 complete unique entries

These run without emulator using `testDebugUnitTest`.

## 6. Room In-Memory Test

`Room.inMemoryDatabaseBuilder` provides a real Room database that disappears after the test.

The DAO test verifies:

- insert returns IDs
- newest-first ordering
- detail preserves fields including nullable location
- delete count and missing detail

`@Before` creates isolated state; `@After` closes it.

## 7. Migration Testing and Schema Export

Room migration tests need historical JSON schemas. Week 11 enables:

```text
app/schemas/com.leafguard.database.AppDatabase/1.json
app/schemas/com.leafguard.database.AppDatabase/2.json
```

`MigrationTestHelper` creates schema 1, inserts a row, runs `MIGRATION_1_2`, validates schema 2, and asserts old data remains with null location.

Schema files are source-controlled test contracts. Editing them manually to make tests pass is invalid.

## 8. Activity/Espresso Tests

Result test launches with complete Intent extras and checks share/location/save controls. Settings test verifies the reminder switch.

These are intentionally shallow smoke tests. Permission dialogs, chooser target apps, and WorkManager timing remain manual/system-bound behavior.

## 9. Offline Performance Smoke

The device test creates a deterministic 224x224 bitmap, runs one TFLite classification, validates output, and requires completion under 15 seconds.

This catches catastrophic failure/regression. It is not a benchmark because:

- first-run warmup differs
- emulator/device CPUs differ
- one run has noise
- 15 seconds is a broad smoke budget

Record actual elapsed values across multiple device runs for performance analysis.

## 10. Backend and Model Regression Matrix

Week 11 runs existing suites rather than duplicating them:

| Suite | Tests |
|---|---:|
| FastAPI API | 8 |
| Keras contract | 4 |
| TFLite contract/parity | 4 |

Together with four JVM tests, 20 non-device tests pass.

## 11. Debugging and Static Analysis

Use this loop:

1. capture exact failing test/stack trace
2. state one falsifiable hypothesis
3. run the cheapest discriminating check
4. fix the owning boundary
5. rerun focused test
6. rerun complete relevant suite
7. save before/after evidence

`lintDebug` detects Android static issues but does not replace runtime tests. Build success does not prove behavior. Coverage percentage does not prove test quality.

No connected device was available during this reconstruction, so instrumentation execution must remain an explicit student gate rather than being falsely reported green.

## 12. End-of-Week-11 File Inventory (Exact Files, Exact Code, Exact Size)

Week 11 begins from the compiled Week 10 feature snapshot. It creates 11 files and expands 3 files.

### 12.1 Summary

| Change | Count | Scope |
|---|---:|---|
| New | 11 | formatter, 3 JVM tests, 5 instrumentation tests, 2 schema JSON files |
| Expanded | 3 | Gradle, AppDatabase schema export, Result formatter use |
| User-visible change | 0 | existing output preserved |
| Week 12 release change | 0 | deferred |

**Total: 895 logical lines.**

### 12.2 Tree

```text
android-app-kotlin/app/
|-- build.gradle                                  EXPANDED  82 lines
|-- schemas/com.leafguard.database.AppDatabase/
|   |-- 1.json                                    NEW       88 lines
|   `-- 2.json                                    NEW      100 lines
`-- src/
    |-- main/java/com/leafguard/
    |   |-- ResultActivity.kt                     EXPANDED 221 lines
    |   |-- ResultTextFormatter.kt                NEW       28 lines
    |   `-- database/AppDatabase.kt               EXPANDED  45 lines
    |-- test/java/com/leafguard/
    |   |-- ResultTextFormatterTest.kt            NEW       31 lines
    |   |-- PredictionResponseContractTest.kt     NEW       37 lines
    |   `-- DiseaseCatalogContractTest.kt         NEW       30 lines
    `-- androidTest/java/com/leafguard/
        |-- ScanDaoInstrumentedTest.kt            NEW       71 lines
        |-- AppDatabaseMigrationTest.kt           NEW       67 lines
        |-- ResultActivityInstrumentedTest.kt     NEW       38 lines
        |-- SettingsActivityInstrumentedTest.kt   NEW       22 lines
        `-- OfflineInferencePerformanceTest.kt    NEW       35 lines
```

### 12.3 Expanded File: `app/build.gradle` (61 -> 82 lines)

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
}

android {
    namespace 'com.leafguard'
    compileSdk 34

    defaultConfig {
        applicationId "com.leafguard"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "0.1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig true
    }

    aaptOptions {
        noCompress "tflite"
    }

    sourceSets {
        androidTest.assets.srcDirs += files("$projectDir/schemas")
    }
}

kapt {
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
    implementation 'org.tensorflow:tensorflow-lite:2.14.0'
    implementation 'androidx.work:work-runtime-ktx:2.9.0'

    def room_version = "2.6.1"
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    testImplementation 'junit:junit:4.13.2'
    testImplementation 'com.google.code.gson:gson:2.10.1'

    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation 'androidx.test:runner:1.5.2'
    androidTestImplementation 'androidx.test:rules:1.5.0'
    androidTestImplementation 'androidx.room:room-testing:2.6.1'
    androidTestImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3'
}
```

### 12.4 Expanded File: `database/AppDatabase.kt` (45 lines)

```kotlin
package com.leafguard.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ScanRecord::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao

    companion object {
        private const val DATABASE_NAME = "leafguard.db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE scan_history ADD COLUMN latitude REAL")
                db.execSQL("ALTER TABLE scan_history ADD COLUMN longitude REAL")
            }
        }

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).addMigrations(MIGRATION_1_2)
                    .build()
                    .also { database ->
                    instance = database
                }
            }
        }
    }
}
```

### 12.5 Expanded File: `ResultActivity.kt` (222 -> 221 lines)

```kotlin
package com.leafguard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.leafguard.data.DiseaseRepository
import com.leafguard.database.AppDatabase
import com.leafguard.database.ScanRecord
import java.io.IOException
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserException

class ResultActivity : AppCompatActivity() {

    private lateinit var modelLabel: String
    private lateinit var disease: String
    private var confidence: Float = 0f
    private var uncertain: Boolean = true
    private var guidanceAvailable: Boolean = false
    private lateinit var symptoms: String
    private lateinit var treatment: String
    private lateinit var prevention: String
    private var pendingSaveButton: Button? = null

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val saveButton = pendingSaveButton ?: return@registerForActivityResult
        pendingSaveButton = null
        if (result.values.any { granted -> granted }) {
            insertHistory(saveButton, findLastKnownLocation())
        } else {
            Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_SHORT).show()
            insertHistory(saveButton, null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        modelLabel = intent.getStringExtra(EXTRA_MODEL_LABEL) ?: getString(R.string.result_unknown)
        disease = intent.getStringExtra(EXTRA_DISEASE) ?: getString(R.string.result_unknown)
        confidence = intent.getFloatExtra(EXTRA_CONFIDENCE, 0f)
        uncertain = intent.getBooleanExtra(EXTRA_UNCERTAIN, true)
        guidanceAvailable = intent.getBooleanExtra(EXTRA_GUIDANCE_AVAILABLE, false)
        symptoms = intent.getStringExtra(EXTRA_SYMPTOMS) ?: getString(R.string.guidance_unavailable)
        treatment = intent.getStringExtra(EXTRA_TREATMENT) ?: getString(R.string.guidance_unavailable)
        prevention = intent.getStringExtra(EXTRA_PREVENTION) ?: getString(R.string.guidance_unavailable)
        val confidencePercent = ResultTextFormatter.confidencePercent(confidence)

        findViewById<TextView>(R.id.textResultDisease).text = disease
        findViewById<TextView>(R.id.textResultModelLabel).text = getString(
            R.string.model_label_format,
            modelLabel
        )
        findViewById<TextView>(R.id.textResultConfidence).text = getString(
            R.string.confidence_format,
            confidencePercent
        )
        findViewById<ProgressBar>(R.id.progressResultConfidence).progress = confidencePercent
        findViewById<TextView>(R.id.textResultStatus).text = getString(
            if (uncertain) R.string.result_uncertain else R.string.result_confident
        )
        findViewById<TextView>(R.id.textGuidanceStatus).text = getString(
            if (guidanceAvailable) R.string.guidance_available else R.string.guidance_not_reviewed
        )
        findViewById<TextView>(R.id.textResultSymptoms).text = symptoms
        findViewById<TextView>(R.id.textResultTreatment).text = treatment
        findViewById<TextView>(R.id.textResultPrevention).text = prevention
        val saveButton = findViewById<Button>(R.id.buttonSaveHistory)
        saveButton.isEnabled = false
        saveButton.setOnClickListener {
            saveToHistory(it as Button)
        }
        findViewById<Button>(R.id.buttonShareResult).setOnClickListener { shareResult() }
        loadLocalGuidance(saveButton)
    }

    private fun loadLocalGuidance(saveButton: Button) {
        lifecycleScope.launch {
            var localGuidanceFound = false
            try {
                val localDisease = withContext(Dispatchers.IO) {
                    DiseaseRepository.getInstance(applicationContext).findByName(disease)
                }
                if (localDisease != null) {
                    symptoms = localDisease.symptoms
                    treatment = localDisease.treatment
                    prevention = localDisease.prevention
                    guidanceAvailable = true
                    localGuidanceFound = true
                }
            } catch (exception: IOException) {
                localGuidanceFound = false
            } catch (exception: XmlPullParserException) {
                localGuidanceFound = false
            }

            findViewById<TextView>(R.id.textGuidanceStatus).text = getString(
                if (localGuidanceFound) {
                    R.string.guidance_local_library
                } else if (guidanceAvailable) {
                    R.string.guidance_available
                } else {
                    R.string.guidance_not_reviewed
                }
            )
            findViewById<TextView>(R.id.textResultSymptoms).text = symptoms
            findViewById<TextView>(R.id.textResultTreatment).text = treatment
            findViewById<TextView>(R.id.textResultPrevention).text = prevention
            saveButton.isEnabled = true
        }
    }

    private fun saveToHistory(saveButton: Button) {
        saveButton.isEnabled = false
        val includeLocation = findViewById<CheckBox>(R.id.checkboxIncludeLocation).isChecked
        if (!includeLocation) {
            insertHistory(saveButton, null)
            return
        }
        if (hasLocationPermission()) {
            insertHistory(saveButton, findLastKnownLocation())
        } else {
            pendingSaveButton = saveButton
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    private fun insertHistory(saveButton: Button, location: Location?) {
        lifecycleScope.launch {
            val record = ScanRecord(
                modelLabel = modelLabel,
                disease = disease,
                confidence = confidence,
                uncertain = uncertain,
                guidanceAvailable = guidanceAvailable,
                symptoms = symptoms,
                treatment = treatment,
                prevention = prevention,
                latitude = location?.latitude,
                longitude = location?.longitude,
                timestamp = System.currentTimeMillis()
            )
            AppDatabase.getInstance(applicationContext).scanDao().insertScan(record)
            saveButton.setText(R.string.saved_to_history)
            Toast.makeText(
                this@ResultActivity,
                R.string.history_saved,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun shareResult() {
        val shareText = ResultTextFormatter.shareText(
            disease,
            confidence,
            symptoms,
            treatment,
            prevention
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_result_subject))
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_result_chooser)))
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
    }

    private fun findLastKnownLocation(): Location? {
        if (!hasLocationPermission()) return null
        val manager = getSystemService(LocationManager::class.java) ?: return null
        return try {
            manager.getProviders(true)
                .mapNotNull { provider -> manager.getLastKnownLocation(provider) }
                .maxByOrNull { location -> location.time }
        } catch (exception: SecurityException) {
            null
        }
    }

    companion object {
        const val EXTRA_MODEL_LABEL = "extra_model_label"
        const val EXTRA_DISEASE = "extra_disease"
        const val EXTRA_CONFIDENCE = "extra_confidence"
        const val EXTRA_UNCERTAIN = "extra_uncertain"
        const val EXTRA_GUIDANCE_AVAILABLE = "extra_guidance_available"
        const val EXTRA_SYMPTOMS = "extra_symptoms"
        const val EXTRA_TREATMENT = "extra_treatment"
        const val EXTRA_PREVENTION = "extra_prevention"
    }
}
```

### 12.6 New File: `ResultTextFormatter.kt` (28 lines)

```kotlin
package com.leafguard

import kotlin.math.roundToInt

object ResultTextFormatter {
    fun confidencePercent(confidence: Float): Int = (confidence * 100f).roundToInt()

    fun shareText(
        disease: String,
        confidence: Float,
        symptoms: String,
        treatment: String,
        prevention: String
    ): String {
        return """
            Disease: $disease
            Confidence: ${confidencePercent(confidence)}%

            Symptoms: $symptoms

            Treatment: $treatment

            Prevention: $prevention

            Model suggestion only; verify serious cases with a qualified agricultural source.
        """.trimIndent()
    }
}
```

### 12.7 New File: `ResultTextFormatterTest.kt` (31 lines)

```kotlin
package com.leafguard

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultTextFormatterTest {
    @Test
    fun confidencePercentUsesZeroToOneInput() {
        assertEquals(87, ResultTextFormatter.confidencePercent(0.87f))
        assertEquals(88, ResultTextFormatter.confidencePercent(0.875f))
    }

    @Test
    fun shareTextContainsGuidanceAndDisclaimerButNoLocation() {
        val text = ResultTextFormatter.shareText(
            disease = "Tomato Early Blight",
            confidence = 0.92f,
            symptoms = "Brown spots",
            treatment = "Remove affected leaves",
            prevention = "Rotate crops"
        )

        assertTrue(text.contains("Disease: Tomato Early Blight"))
        assertTrue(text.contains("Confidence: 92%"))
        assertTrue(text.contains("Model suggestion only"))
        assertFalse(text.contains("Latitude"))
        assertFalse(text.contains("Longitude"))
    }
}
```

### 12.8 New File: `PredictionResponseContractTest.kt` (37 lines)

```kotlin
package com.leafguard

import com.google.gson.Gson
import com.leafguard.network.PredictionResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PredictionResponseContractTest {
    @Test
    fun parsesAllEightApiFields() {
        val json = """
            {
              "model_label": "Tomato___Early_blight",
              "disease": "Tomato Early Blight",
              "confidence": 0.87,
              "uncertain": false,
              "guidance_available": true,
              "symptoms": "Brown spots",
              "treatment": "Remove leaves",
              "prevention": "Rotate crops"
            }
        """.trimIndent()

        val response = Gson().fromJson(json, PredictionResponse::class.java)

        assertEquals("Tomato___Early_blight", response.modelLabel)
        assertEquals("Tomato Early Blight", response.disease)
        assertEquals(0.87f, response.confidence, 0.0001f)
        assertFalse(response.uncertain)
        assertTrue(response.guidanceAvailable)
        assertEquals("Brown spots", response.symptoms)
        assertEquals("Remove leaves", response.treatment)
        assertEquals("Rotate crops", response.prevention)
    }
}
```

### 12.9 New File: `DiseaseCatalogContractTest.kt` (30 lines)

```kotlin
package com.leafguard

import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DiseaseCatalogContractTest {
    @Test
    fun catalogHasTenCompleteUniqueEntries() {
        val file = File("src/main/assets/diseases.xml")
        val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        val entries = document.getElementsByTagName("disease")
        assertEquals(10, entries.length)

        val requiredTags = listOf("name", "plant", "symptoms", "treatment", "prevention")
        val names = mutableSetOf<String>()
        for (index in 0 until entries.length) {
            val element = entries.item(index) as org.w3c.dom.Element
            for (tag in requiredTags) {
                val value = element.getElementsByTagName(tag).item(0)?.textContent?.trim().orEmpty()
                assertTrue("$tag must not be blank", value.isNotBlank())
            }
            val normalizedName = element.getElementsByTagName("name")
                .item(0).textContent.trim().lowercase()
            assertTrue("duplicate disease name", names.add(normalizedName))
        }
    }
}
```

### 12.10 New File: `ScanDaoInstrumentedTest.kt` (71 lines)

```kotlin
package com.leafguard

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leafguard.database.AppDatabase
import com.leafguard.database.ScanRecord
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScanDaoInstrumentedTest {
    private lateinit var database: AppDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertListDetailAndDeletePreserveAllFields() = runBlocking {
        val olderId = database.scanDao().insertScan(record("Older", 1000L, null, null))
        val newerId = database.scanDao().insertScan(record("Newer", 2000L, 23.7, 90.4))

        val scans = database.scanDao().getAllScans()
        assertEquals(listOf(newerId, olderId), scans.map { it.id })

        val detail = database.scanDao().getScanById(newerId)!!
        assertEquals("Model___Newer", detail.modelLabel)
        assertEquals("Newer", detail.disease)
        assertEquals(0.75f, detail.confidence, 0.0001f)
        assertEquals(23.7, detail.latitude!!, 0.0001)
        assertEquals(90.4, detail.longitude!!, 0.0001)

        assertEquals(1, database.scanDao().deleteScanById(newerId))
        assertNull(database.scanDao().getScanById(newerId))
    }

    private fun record(
        disease: String,
        timestamp: Long,
        latitude: Double?,
        longitude: Double?
    ) = ScanRecord(
        modelLabel = "Model___$disease",
        disease = disease,
        confidence = 0.75f,
        uncertain = false,
        guidanceAvailable = true,
        symptoms = "Symptoms",
        treatment = "Treatment",
        prevention = "Prevention",
        latitude = latitude,
        longitude = longitude,
        timestamp = timestamp
    )
}
```

### 12.11 New File: `AppDatabaseMigrationTest.kt` (67 lines)

```kotlin
package com.leafguard

import android.content.ContentValues
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.leafguard.database.AppDatabase
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {
    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrationOneToTwoPreservesRowAndAddsNullableLocation() {
        helper.createDatabase(TEST_DATABASE, 1).apply {
            insert(
                "scan_history",
                0,
                ContentValues().apply {
                    put("model_label", "Tomato___Early_blight")
                    put("disease", "Tomato Early Blight")
                    put("confidence", 0.87f)
                    put("uncertain", 0)
                    put("guidance_available", 1)
                    put("symptoms", "Symptoms")
                    put("treatment", "Treatment")
                    put("prevention", "Prevention")
                    put("timestamp", 1000L)
                }
            )
            close()
        }

        val migrated = helper.runMigrationsAndValidate(
            TEST_DATABASE,
            2,
            true,
            AppDatabase.MIGRATION_1_2
        )
        migrated.query(
            "SELECT disease, latitude, longitude FROM scan_history"
        ).use { cursor ->
            assertTrue(cursor.moveToFirst())
            assertEquals("Tomato Early Blight", cursor.getString(0))
            assertTrue(cursor.isNull(1))
            assertTrue(cursor.isNull(2))
        }
    }

    companion object {
        private const val TEST_DATABASE = "week11-migration-test"
    }
}
```

### 12.12 New File: `ResultActivityInstrumentedTest.kt` (38 lines)

```kotlin
package com.leafguard

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ResultActivityInstrumentedTest {
    @Test
    fun resultShowsShareLocationAndSaveControls() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = Intent(context, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_MODEL_LABEL, "Tomato___Early_blight")
            putExtra(ResultActivity.EXTRA_DISEASE, "Tomato Early Blight")
            putExtra(ResultActivity.EXTRA_CONFIDENCE, 0.87f)
            putExtra(ResultActivity.EXTRA_UNCERTAIN, false)
            putExtra(ResultActivity.EXTRA_GUIDANCE_AVAILABLE, true)
            putExtra(ResultActivity.EXTRA_SYMPTOMS, "Symptoms")
            putExtra(ResultActivity.EXTRA_TREATMENT, "Treatment")
            putExtra(ResultActivity.EXTRA_PREVENTION, "Prevention")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        ActivityScenario.launch<ResultActivity>(intent).use {
            onView(withId(R.id.buttonShareResult)).check(matches(isDisplayed()))
            onView(withId(R.id.checkboxIncludeLocation)).check(matches(isDisplayed()))
            onView(withId(R.id.buttonSaveHistory)).check(matches(isDisplayed()))
        }
    }
}
```

### 12.13 New File: `SettingsActivityInstrumentedTest.kt` (22 lines)

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
class SettingsActivityInstrumentedTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(SettingsActivity::class.java)

    @Test
    fun reminderSwitchIsVisible() {
        onView(withId(R.id.switchScanReminders)).check(matches(isDisplayed()))
    }
}
```

### 12.14 New File: `OfflineInferencePerformanceTest.kt` (35 lines)

```kotlin
package com.leafguard

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.leafguard.ml.TFLiteClassifier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OfflineInferencePerformanceTest {
    @Test
    fun oneOfflineInferenceCompletesWithinSmokeBudget() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val bitmap = Bitmap.createBitmap(224, 224, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.rgb(30, 180, 60))
        }

        val startedAt = System.nanoTime()
        val prediction = TFLiteClassifier(context).use { classifier ->
            classifier.classify(bitmap)
        }
        val elapsedMilliseconds = (System.nanoTime() - startedAt) / 1_000_000

        assertEquals(38, context.assets.open("labels.txt").bufferedReader().readLines().size)
        assertTrue(prediction.modelLabel.isNotBlank())
        assertTrue(prediction.confidence in 0f..1f)
        assertTrue("Inference took ${elapsedMilliseconds}ms", elapsedMilliseconds < 15_000)
        bitmap.recycle()
    }
}
```

### 12.15 New File: Room Schema `1.json` (88 lines)

```json
{
  "formatVersion": 1,
  "database": {
    "version": 1,
    "identityHash": "2960205c2b08b31011f470fdda2c832d",
    "entities": [
      {
        "tableName": "scan_history",
        "createSql": "CREATE TABLE IF NOT EXISTS `${TABLE_NAME}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `model_label` TEXT NOT NULL, `disease` TEXT NOT NULL, `confidence` REAL NOT NULL, `uncertain` INTEGER NOT NULL, `guidance_available` INTEGER NOT NULL, `symptoms` TEXT NOT NULL, `treatment` TEXT NOT NULL, `prevention` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)",
        "fields": [
          {
            "fieldPath": "id",
            "columnName": "id",
            "affinity": "INTEGER",
            "notNull": true
          },
          {
            "fieldPath": "modelLabel",
            "columnName": "model_label",
            "affinity": "TEXT",
            "notNull": true
          },
          {
            "fieldPath": "disease",
            "columnName": "disease",
            "affinity": "TEXT",
            "notNull": true
          },
          {
            "fieldPath": "confidence",
            "columnName": "confidence",
            "affinity": "REAL",
            "notNull": true
          },
          {
            "fieldPath": "uncertain",
            "columnName": "uncertain",
            "affinity": "INTEGER",
            "notNull": true
          },
          {
            "fieldPath": "guidanceAvailable",
            "columnName": "guidance_available",
            "affinity": "INTEGER",
            "notNull": true
          },
          {
            "fieldPath": "symptoms",
            "columnName": "symptoms",
            "affinity": "TEXT",
            "notNull": true
          },
          {
            "fieldPath": "treatment",
            "columnName": "treatment",
            "affinity": "TEXT",
            "notNull": true
          },
          {
            "fieldPath": "prevention",
            "columnName": "prevention",
            "affinity": "TEXT",
            "notNull": true
          },
          {
            "fieldPath": "timestamp",
            "columnName": "timestamp",
            "affinity": "INTEGER",
            "notNull": true
          }
        ],
        "primaryKey": {
          "autoGenerate": true,
          "columnNames": [
            "id"
          ]
        },
        "indices": [],
        "foreignKeys": []
      }
    ],
    "views": [],
    "setupQueries": [
      "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)",
      "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2960205c2b08b31011f470fdda2c832d')"
    ]
  }
}
```

### 12.16 New File: Room Schema `2.json` (100 lines)

```json
{
  "formatVersion": 1,
  "database": {
    "version": 2,
    "identityHash": "fe37742d25404d13800c2d35a7d76d2f",
    "entities": [
      {
        "tableName": "scan_history",
        "createSql": "CREATE TABLE IF NOT EXISTS `${TABLE_NAME}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `model_label` TEXT NOT NULL, `disease` TEXT NOT NULL, `confidence` REAL NOT NULL, `uncertain` INTEGER NOT NULL, `guidance_available` INTEGER NOT NULL, `symptoms` TEXT NOT NULL, `treatment` TEXT NOT NULL, `prevention` TEXT NOT NULL, `latitude` REAL, `longitude` REAL, `timestamp` INTEGER NOT NULL)",
        "fields": [
          {
            "fieldPath": "id",
            "columnName": "id",
            "affinity": "INTEGER",
            "notNull": true
          },
          {
            "fieldPath": "modelLabel",
            "columnName": "model_label",
            "affinity": "TEXT",
            "notNull": true
          },
          {
            "fieldPath": "disease",
            "columnName": "disease",
            "affinity": "TEXT",
            "notNull": true
          },
          {
            "fieldPath": "confidence",
            "columnName": "confidence",
            "affinity": "REAL",
            "notNull": true
          },
          {
            "fieldPath": "uncertain",
            "columnName": "uncertain",
            "affinity": "INTEGER",
            "notNull": true
          },
          {
            "fieldPath": "guidanceAvailable",
            "columnName": "guidance_available",
            "affinity": "INTEGER",
            "notNull": true
          },
          {
            "fieldPath": "symptoms",
            "columnName": "symptoms",
            "affinity": "TEXT",
            "notNull": true
          },
          {
            "fieldPath": "treatment",
            "columnName": "treatment",
            "affinity": "TEXT",
            "notNull": true
          },
          {
            "fieldPath": "prevention",
            "columnName": "prevention",
            "affinity": "TEXT",
            "notNull": true
          },
          {
            "fieldPath": "latitude",
            "columnName": "latitude",
            "affinity": "REAL",
            "notNull": false
          },
          {
            "fieldPath": "longitude",
            "columnName": "longitude",
            "affinity": "REAL",
            "notNull": false
          },
          {
            "fieldPath": "timestamp",
            "columnName": "timestamp",
            "affinity": "INTEGER",
            "notNull": true
          }
        ],
        "primaryKey": {
          "autoGenerate": true,
          "columnNames": [
            "id"
          ]
        },
        "indices": [],
        "foreignKeys": []
      }
    ],
    "views": [],
    "setupQueries": [
      "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)",
      "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'fe37742d25404d13800c2d35a7d76d2f')"
    ]
  }
}
```


### 12.17 Files Week 11 Does Not Rewrite

| Area | Status | Reason |
|---|---|---|
| Prediction/model/XML/Room behavior | Unchanged | Tests observe contracts |
| Activity layouts/manifest | Unchanged | Existing controls sufficient |
| Backend/model test files | Unchanged | Existing suites reused |
| Permissions/notifications/location | Unchanged | Week 10 behavior tested manually/device-side |
| Signing/version/release | Absent | Week 12 |

### 12.18 Commands and Expected Evidence

```bash
# Kotlin JVM tests
cd android-app-kotlin
./gradlew testDebugUnitTest

# Compile instrumentation without device
./gradlew compileDebugAndroidTestKotlin

# Execute instrumentation with emulator/device
./gradlew connectedDebugAndroidTest

# Static/build checks
./gradlew assembleDebug lintDebug

# Backend
cd ../backend-api
USE_MOCK=true .venv/bin/python -m unittest -v test_api

# Keras
cd ../model
../backend-api/.venv/bin/python -m unittest -v test_model_contract

# TFLite focused suite from Week 09 snapshot/package
../backend-api/.venv/bin/python -m unittest -v test_tflite_contract
```

Reproduced in this environment:

```text
4 Kotlin JVM tests passed
8 backend tests passed
4 Keras contract tests passed
4 TFLite tests passed
5 instrumentation tests compiled
assembleDebug passed
lintDebug passed
connectedDebugAndroidTest not executed: no connected device
```

### 12.19 Evidence Map

| Concept | Exercise | Build step | Evidence |
|---|---|---|---|
| Test pyramid | 1 | 1 | Matrix/results |
| Pure formatter/JSON/XML | 2 | 2 | Four JVM tests |
| Room CRUD | 3 | 3 | DAO instrumentation |
| Room migration | 3 | 4 | v1/v2 schemas + migration test |
| Activity UI | 4 | 5 | Espresso tests |
| Offline performance | 5 | 6 | elapsed smoke result |
| Backend/model regression | 5 | 7 | 16 Python tests |
| Debugging/lint | 6 | 8 | red-green record/lint report |

## 13. Understanding Checklist

- [ ] I can choose unit versus instrumentation versus manual tests.
- [ ] I can write Arrange-Act-Assert tests.
- [ ] I can explain why formatter extraction is not a feature change.
- [ ] I can explain in-memory Room isolation.
- [ ] I can explain schema export and migration testing.
- [ ] I can explain Espresso visibility smoke tests.
- [ ] I can explain why performance smoke is not benchmarking.
- [ ] I can run 20 non-device tests.
- [ ] I can compile and execute five instrumentation tests on a device.
- [ ] I can interpret lint separately from runtime tests.
- [ ] I can document one falsifiable debugging cycle.
- [ ] I can state residual manual risks.
- [ ] I know Week 12 owns signing/release.

<!-- NAV_FOOTER_START -->

---

[README](README.md) | **Learning Notes** | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Week 10](../week-10-notifications-share-location/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 12](../week-12-final-submission/README.md)
