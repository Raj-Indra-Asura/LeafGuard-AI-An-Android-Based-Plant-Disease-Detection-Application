# Week 10 Learning Notes: Privacy-Aware Android Utilities

## Purpose

These notes explain sharing through implicit Intent, optional location during Room save, schema migration, notification permission/channel behavior, unique periodic WorkManager scheduling, and one persisted reminder setting.

Section 12 contains all 13 new/expanded files in full. The cumulative snapshot compiled successfully with `assembleDebug`.

## 1. How Week 10 Grows From Week 09

Week 09 already produces a final result through cloud or offline inference. Week 10 reuses that same result:

```text
Result
  -> Share text
  -> Save with optional location
Settings
  -> Enable/disable scan reminder
```

No model output, XML lookup, or prediction branch changes.

## 2. Sharing With an Implicit Intent

`Intent.ACTION_SEND` asks Android to find compatible apps. The app supplies:

- MIME type `text/plain`
- subject
- formatted disease/confidence/guidance text
- caution that output is a model suggestion

`Intent.createChooser` lets the user choose the destination. Week 10 does not share images or coordinates, reducing privacy and FileProvider complexity.

## 3. Optional Location and Permission Timing

Location is requested only when the user checks Include Location and taps Save.

```text
unchecked -> save null location
checked + permission -> query last-known location -> save coordinates or null
checked + denied -> explain -> save null location
```

The result remains useful without location. Permission denial is a normal state, not an error that blocks Room persistence.

Week 10 uses last-known coarse/fine location. It does not start continuous tracking or background location.

## 4. Nullable Location Schema

Week 07's 10 columns remain. Week 10 adds nullable `Double?` values:

| Column | SQL type | Meaning |
|---|---|---|
| `latitude` | `REAL NULL` | Last-known latitude when available/allowed |
| `longitude` | `REAL NULL` | Last-known longitude when available/allowed |

Null means location was not requested, permission was denied, or no provider had a value. `0.0,0.0` is a real coordinate and must not represent missing data.

## 5. Room Migration 1 -> 2

Changing an entity without increasing the database version causes Room's schema integrity check to fail.

Migration SQL:

```sql
ALTER TABLE scan_history ADD COLUMN latitude REAL;
ALTER TABLE scan_history ADD COLUMN longitude REAL;
```

No `NOT NULL` constraint/default is used, so existing rows become null safely. `addMigrations(MIGRATION_1_2)` preserves all earlier history.

Never use destructive migration merely to hide a schema mistake; it deletes student/user data.

## 6. History Location Display

Detail renders either:

- coordinates formatted to five decimal places, or
- `Location was not saved.`

The list remains unchanged because location is detail-level metadata, not the primary scanning summary.

## 7. Notification Channels and Permission

Android 8+ requires a channel before posting. Channel identity is stable:

```text
leafguard_scan_reminders
```

Android 13+ additionally requires runtime `POST_NOTIFICATIONS`. Older versions do not show that runtime prompt.

`showReminder` checks permission again because Worker may run later after permission changes.

## 8. WorkManager Periodic Reminder

`PeriodicWorkRequestBuilder<ScanReminderWorker>(24, HOURS)` requests approximate daily work.

WorkManager is:

- persistent across process restarts
- battery-aware
- not exact to the minute
- appropriate for deferrable reminders

Unique work name plus `ExistingPeriodicWorkPolicy.UPDATE` prevents duplicate schedules when the user toggles repeatedly.

Disabling reminders calls `cancelUniqueWork`.

## 9. Worker and PendingIntent

Worker calls `NotificationHelper.showReminder` and returns `Result.success()`.

The notification's immutable `PendingIntent` opens MainActivity. `FLAG_UPDATE_CURRENT` reuses the request identity safely.

The helper owns channel creation, scheduling, cancellation, and notification construction; Settings owns user preference/permission interaction.

## 10. Persisted Reminder Setting

Settings stores one Boolean in app-private SharedPreferences:

```text
scan_reminders_enabled
```

`apply()` writes asynchronously. The switch restores the prior choice on reopening Settings.

If Android 13 permission is denied:

- switch returns off
- preference becomes false
- unique work is cancelled
- user receives clear feedback

## 11. Boundaries and Common Mistakes

Avoid:

- sharing images/coordinates without explicit need
- requesting location on app startup
- blocking save after permission denial
- using `0.0` as missing location
- changing Room entity without migration
- destructive migration
- scheduling duplicate periodic requests
- claiming WorkManager runs at an exact clock time
- posting before channel creation
- assuming notification permission remains granted forever
- adding background location/maps/analytics/navigation redesign
- changing cloud/offline prediction contracts

Week 11 owns systematic testing, debugging, and performance profiling. Week 12 owns release.

## 12. End-of-Week-10 File Inventory (Exact Files, Exact Code, Exact Size)

Week 10 starts from the compiled Week 09 state. It creates 2 files and expands 11 files.

### 12.1 Change Summary

| Change | Count | Scope |
|---|---:|---|
| New | 2 | notification helper and worker |
| Expanded | 11 | dependency/permissions, Room location migration, Result/history/settings UI and logic |
| Prediction/model/XML changes | 0 | reused unchanged |
| Week 11/12 changes | 0 | deferred |

**Complete changed/new code: 1,088 logical lines.**

### 12.2 Exact Tree

```text
android-app-kotlin/app/
|-- build.gradle                              EXPANDED   61 lines
`-- src/main/
    |-- AndroidManifest.xml                   EXPANDED   64 lines
    |-- java/com/leafguard/
    |   |-- ResultActivity.kt                 EXPANDED  222 lines
    |   |-- HistoryDetailActivity.kt          EXPANDED  119 lines
    |   |-- SettingsActivity.kt               EXPANDED   66 lines
    |   |-- database/
    |   |   |-- ScanRecord.kt                 EXPANDED   33 lines
    |   |   `-- AppDatabase.kt                EXPANDED   45 lines
    |   `-- utils/
    |       |-- NotificationHelper.kt         NEW        82 lines
    |       `-- ScanReminderWorker.kt         NEW        20 lines
    `-- res/
        |-- layout/
        |   |-- activity_result.xml           EXPANDED  136 lines
        |   |-- activity_history_detail.xml   EXPANDED  114 lines
        |   `-- activity_settings.xml         EXPANDED   29 lines
        `-- values/strings.xml                EXPANDED   97 lines
```

### 12.3 Expanded File: `app/build.gradle` (60 -> 61 lines)

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
}
```

### 12.4 Expanded File: `AndroidManifest.xml` (61 -> 64 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-feature
        android:name="android.hardware.camera"
        android:required="false" />

    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <application
        android:allowBackup="true"
        android:icon="@android:drawable/ic_menu_gallery"
        android:label="@string/app_name"
        android:networkSecurityConfig="@xml/network_security_config"
        android:supportsRtl="true"
        android:theme="@style/Theme.LeafGuardAI">

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_provider_paths" />
        </provider>

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity
            android:name=".ScanActivity"
            android:exported="false" />
        <activity
            android:name=".ResultActivity"
            android:exported="false" />
        <activity
            android:name=".HistoryActivity"
            android:exported="false" />
        <activity
            android:name=".HistoryDetailActivity"
            android:exported="false" />
        <activity
            android:name=".DiseaseLibraryActivity"
            android:exported="false" />
        <activity
            android:name=".DiseaseDetailActivity"
            android:exported="false" />
        <activity
            android:name=".SettingsActivity"
            android:exported="false" />
    </application>

</manifest>
```

### 12.5 Expanded File: `database/ScanRecord.kt` (29 -> 33 lines)

```kotlin
package com.leafguard.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_history")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "model_label")
    val modelLabel: String,
    @ColumnInfo(name = "disease")
    val disease: String,
    @ColumnInfo(name = "confidence")
    val confidence: Float,
    @ColumnInfo(name = "uncertain")
    val uncertain: Boolean,
    @ColumnInfo(name = "guidance_available")
    val guidanceAvailable: Boolean,
    @ColumnInfo(name = "symptoms")
    val symptoms: String,
    @ColumnInfo(name = "treatment")
    val treatment: String,
    @ColumnInfo(name = "prevention")
    val prevention: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double? = null,
    @ColumnInfo(name = "longitude")
    val longitude: Double? = null,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)
```

### 12.6 Expanded File: `database/AppDatabase.kt` (34 -> 45 lines)

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
    exportSchema = false
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

### 12.7 New File: `utils/NotificationHelper.kt` (82 lines)

```kotlin
package com.leafguard.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.leafguard.MainActivity
import com.leafguard.R
import java.util.concurrent.TimeUnit

object NotificationHelper {
    const val CHANNEL_ID = "leafguard_scan_reminders"
    const val UNIQUE_WORK_NAME = "leafguard_daily_scan_reminder"

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.getString(R.string.notification_channel_description)
            }
            context.getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
    }

    fun scheduleDailyReminder(context: Context) {
        createChannel(context)
        val request = PeriodicWorkRequestBuilder<ScanReminderWorker>(24, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancelDailyReminder(context: Context) {
        WorkManager.getInstance(context.applicationContext)
            .cancelUniqueWork(UNIQUE_WORK_NAME)
    }

    fun showReminder(context: Context, title: String, message: String) {
        createChannel(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private const val NOTIFICATION_ID = 1001
}
```

### 12.8 New File: `utils/ScanReminderWorker.kt` (20 lines)

```kotlin
package com.leafguard.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.leafguard.R

class ScanReminderWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : Worker(appContext, workerParameters) {
    override fun doWork(): Result {
        NotificationHelper.showReminder(
            applicationContext,
            applicationContext.getString(R.string.notification_reminder_title),
            applicationContext.getString(R.string.notification_reminder_message)
        )
        return Result.success()
    }
}
```

### 12.9 Expanded File: `ResultActivity.kt` (142 -> 222 lines)

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
        val confidencePercent = (confidence * 100f).roundToInt()

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
        val shareText = getString(
            R.string.share_result_template,
            disease,
            (confidence * 100f).roundToInt(),
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

### 12.10 Expanded File: `HistoryDetailActivity.kt` (112 -> 119 lines)

```kotlin
package com.leafguard

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.leafguard.database.AppDatabase
import com.leafguard.database.ScanRecord
import java.text.DateFormat
import java.util.Date
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

class HistoryDetailActivity : AppCompatActivity() {
    private var scanId: Long = INVALID_SCAN_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)

        scanId = intent.getLongExtra(EXTRA_SCAN_ID, INVALID_SCAN_ID)
        if (scanId == INVALID_SCAN_ID) {
            Toast.makeText(this, R.string.history_invalid_id, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<Button>(R.id.buttonDeleteHistory).setOnClickListener {
            confirmDelete()
        }
        loadRecord()
    }

    private fun loadRecord() {
        lifecycleScope.launch {
            val record = AppDatabase.getInstance(applicationContext)
                .scanDao()
                .getScanById(scanId)
            if (record == null) {
                Toast.makeText(
                    this@HistoryDetailActivity,
                    R.string.history_record_missing,
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                renderRecord(record)
            }
        }
    }

    private fun renderRecord(record: ScanRecord) {
        findViewById<TextView>(R.id.textDetailDisease).text = record.disease
        findViewById<TextView>(R.id.textDetailModelLabel).text = getString(
            R.string.model_label_format,
            record.modelLabel
        )
        findViewById<TextView>(R.id.textDetailConfidence).text = getString(
            R.string.confidence_format,
            (record.confidence * 100f).roundToInt()
        )
        findViewById<TextView>(R.id.textDetailUncertain).text = getString(
            if (record.uncertain) R.string.result_uncertain else R.string.result_confident
        )
        findViewById<TextView>(R.id.textDetailGuidanceStatus).text = getString(
            if (record.guidanceAvailable) {
                R.string.guidance_available
            } else {
                R.string.guidance_not_reviewed
            }
        )
        findViewById<TextView>(R.id.textDetailTimestamp).text = DateFormat
            .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)
            .format(Date(record.timestamp))
        findViewById<TextView>(R.id.textDetailLocation).text = if (
            record.latitude != null && record.longitude != null
        ) {
            getString(R.string.history_location_format, record.latitude, record.longitude)
        } else {
            getString(R.string.history_location_unavailable)
        }
        findViewById<TextView>(R.id.textDetailSymptoms).text = record.symptoms
        findViewById<TextView>(R.id.textDetailTreatment).text = record.treatment
        findViewById<TextView>(R.id.textDetailPrevention).text = record.prevention
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_history_title)
            .setMessage(R.string.delete_history_message)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ -> deleteRecord() }
            .show()
    }

    private fun deleteRecord() {
        lifecycleScope.launch {
            val deletedRows = AppDatabase.getInstance(applicationContext)
                .scanDao()
                .deleteScanById(scanId)
            if (deletedRows > 0) {
                Toast.makeText(
                    this@HistoryDetailActivity,
                    R.string.history_deleted,
                    Toast.LENGTH_SHORT
                ).show()
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_SCAN_ID = "extra_scan_id"
        private const val INVALID_SCAN_ID = -1L
    }
}
```

### 12.11 Expanded File: `SettingsActivity.kt` (12 -> 66 lines)

```kotlin
package com.leafguard

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.leafguard.utils.NotificationHelper

class SettingsActivity : AppCompatActivity() {
    private lateinit var switchReminders: SwitchCompat

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            setRemindersEnabled(true)
        } else {
            switchReminders.isChecked = false
            setRemindersEnabled(false)
            Toast.makeText(this, R.string.notification_permission_denied, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        NotificationHelper.createChannel(this)
        switchReminders = findViewById(R.id.switchScanReminders)
        switchReminders.isChecked = preferences().getBoolean(PREF_REMINDERS, false)
        switchReminders.setOnCheckedChangeListener { _, enabled ->
            if (enabled && requiresNotificationPermission()) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                setRemindersEnabled(enabled)
            }
        }
    }

    private fun setRemindersEnabled(enabled: Boolean) {
        preferences().edit().putBoolean(PREF_REMINDERS, enabled).apply()
        if (enabled) {
            NotificationHelper.scheduleDailyReminder(this)
        } else {
            NotificationHelper.cancelDailyReminder(this)
        }
    }

    private fun requiresNotificationPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
    }

    private fun preferences() = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)

    companion object {
        private const val PREFERENCES_NAME = "leafguard_settings"
        private const val PREF_REMINDERS = "scan_reminders_enabled"
    }
}
```

### 12.12 Expanded File: `activity_result.xml` (122 -> 136 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screen_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="24dp">

        <TextView
            android:id="@+id/textResultTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/result_title"
            android:textColor="@color/text_primary"
            android:textSize="24sp" />

        <TextView
            android:id="@+id/textResultDisease"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/result_unknown"
            android:textColor="@color/leaf_green_dark"
            android:textSize="22sp" />

        <TextView
            android:id="@+id/textResultModelLabel"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:text="@string/model_label_placeholder"
            android:textColor="@color/text_secondary" />

        <TextView
            android:id="@+id/textResultConfidence"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="12dp"
            android:text="@string/confidence_placeholder"
            android:textColor="@color/text_primary" />

        <ProgressBar
            android:id="@+id/progressResultConfidence"
            style="?android:attr/progressBarStyleHorizontal"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:max="100"
            android:progress="0" />

        <TextView
            android:id="@+id/textResultStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="@string/result_uncertain"
            android:textColor="@color/text_secondary" />

        <TextView
            android:id="@+id/textGuidanceStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:text="@string/guidance_not_reviewed"
            android:textColor="@color/text_secondary" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:text="@string/symptoms_heading"
            android:textColor="@color/text_primary"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textResultSymptoms"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/guidance_unavailable"
            android:textColor="@color/text_secondary" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/treatment_heading"
            android:textColor="@color/text_primary"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textResultTreatment"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/guidance_unavailable"
            android:textColor="@color/text_secondary" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/prevention_heading"
            android:textColor="@color/text_primary"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textResultPrevention"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/guidance_unavailable"
            android:textColor="@color/text_secondary" />

        <Button
            android:id="@+id/buttonShareResult"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:text="@string/share_result" />

        <CheckBox
            android:id="@+id/checkboxIncludeLocation"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="12dp"
            android:text="@string/include_location" />

        <Button
            android:id="@+id/buttonSaveHistory"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="12dp"
            android:text="@string/save_to_history" />
    </LinearLayout>
</ScrollView>
```

### 12.13 Expanded File: `activity_history_detail.xml` (106 -> 114 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screen_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="24dp">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/history_detail_title"
            android:textColor="@color/text_primary"
            android:textSize="24sp" />

        <TextView
            android:id="@+id/textDetailDisease"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:textColor="@color/leaf_green_dark"
            android:textSize="22sp" />

        <TextView
            android:id="@+id/textDetailModelLabel"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:textColor="@color/text_secondary" />

        <TextView
            android:id="@+id/textDetailConfidence"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:textColor="@color/text_primary" />

        <TextView
            android:id="@+id/textDetailUncertain"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:textColor="@color/text_secondary" />

        <TextView
            android:id="@+id/textDetailGuidanceStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:textColor="@color/text_secondary" />

        <TextView
            android:id="@+id/textDetailTimestamp"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:textColor="@color/text_secondary" />

        <TextView
            android:id="@+id/textDetailLocation"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:text="@string/history_location_unavailable"
            android:textColor="@color/text_secondary" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:text="@string/symptoms_heading"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textDetailSymptoms"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/treatment_heading"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textDetailTreatment"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/prevention_heading"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textDetailPrevention"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <Button
            android:id="@+id/buttonDeleteHistory"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:text="@string/delete_history" />
    </LinearLayout>
</ScrollView>
```

### 12.14 Expanded File: `activity_settings.xml` (25 -> 29 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screen_background"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/settings_title"
        android:textColor="@color/text_primary"
        android:textSize="24sp" />

    <androidx.appcompat.widget.SwitchCompat
        android:id="@+id/switchScanReminders"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:text="@string/settings_scan_reminders" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="@string/settings_scan_reminders_description"
        android:textColor="@color/text_secondary" />
</LinearLayout>
```

### 12.15 Expanded File: `strings.xml` (82 -> 97 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">LeafGuard AI</string>

    <string name="home_title">LeafGuard AI</string>
    <string name="home_subtitle">Plant disease detection learning app</string>

    <string name="open_scan">Open Scan</string>
    <string name="open_result">Open Sample Result</string>
    <string name="open_history">Open History</string>
    <string name="open_library">Open Disease Library</string>
    <string name="open_settings">Open Settings</string>

    <string name="scan_title">Scan Leaf</string>
    <string name="result_title">Prediction Result</string>
    <string name="history_title">History</string>
    <string name="library_title">Disease Library</string>
    <string name="settings_title">Settings and About</string>

    <string name="disease_library_loading">Loading local disease library</string>
    <string name="disease_library_empty">No reviewed disease entries are available.</string>
    <string name="disease_library_error">Could not read the local disease library.</string>
    <string name="disease_invalid_name">Invalid disease selection.</string>
    <string name="disease_not_found">Disease entry not found.</string>
    <string name="disease_plant_format">Plant: %1$s</string>
    <string name="guidance_local_library">Guidance loaded from the reviewed local XML library.</string>

    <string name="scan_instruction">Take a photo or choose an image, then upload it to the Week 04 backend.</string>
    <string name="scan_preview_description">Preview of the selected leaf image</string>
    <string name="take_photo">Take Photo</string>
    <string name="choose_from_gallery">Choose from Gallery</string>
    <string name="no_image_selected">No image selected yet.</string>
    <string name="image_ready_for_upload">Image selected. Ready to detect.</string>
    <string name="camera_permission_denied">Camera permission denied. You can still choose from gallery.</string>
    <string name="camera_cancelled">Camera cancelled. No new image selected.</string>
    <string name="gallery_cancelled">Gallery closed. No new image selected.</string>
    <string name="camera_file_error">Could not prepare a file for the camera.</string>

    <string name="detect_disease">Detect Disease</string>
    <string name="detection_mode_title">Detection mode</string>
    <string name="detection_mode_cloud">Cloud</string>
    <string name="detection_mode_offline">Offline</string>
    <string name="detection_mode_cloud_description">Cloud mode uploads the image to the Week 06 backend.</string>
    <string name="detection_mode_offline_description">Offline mode runs the converted TFLite model on this device.</string>
    <string name="offline_prediction_error">Offline prediction failed. Verify model and label assets.</string>
    <string name="upload_progress_description">Uploading image for prediction</string>
    <string name="select_image_first">Select or capture an image first.</string>
    <string name="image_prepare_error">Could not prepare the selected image for upload.</string>
    <string name="server_error_format">Server rejected the request (HTTP %1$d).</string>
    <string name="network_error">Could not reach the backend. Check the server and emulator URL.</string>

    <string name="result_unknown">Unknown result</string>
    <string name="model_label_placeholder">Model label: unavailable</string>
    <string name="model_label_format">Model label: %1$s</string>
    <string name="confidence_placeholder">Confidence: 0%%</string>
    <string name="confidence_format">Confidence: %1$d%%</string>
    <string name="result_uncertain">Low-confidence result: verify before acting.</string>
    <string name="result_confident">Confidence is above the configured server threshold.</string>
    <string name="guidance_available">Reviewed project guidance is available.</string>
    <string name="guidance_not_reviewed">Detailed project guidance is not reviewed for this label.</string>
    <string name="symptoms_heading">Symptoms</string>
    <string name="treatment_heading">Treatment</string>
    <string name="prevention_heading">Prevention</string>
    <string name="guidance_unavailable">No information available.</string>

    <string name="save_to_history">Save to History</string>
    <string name="saved_to_history">Saved to History</string>
    <string name="history_saved">Result saved locally.</string>
    <string name="history_empty">No saved scans yet.</string>
    <string name="history_confidence_format">Confidence: %1$.1f%%</string>
    <string name="history_detail_title">Saved Scan</string>
    <string name="history_invalid_id">Invalid history record.</string>
    <string name="history_record_missing">This saved scan no longer exists.</string>
    <string name="delete_history">Delete Scan</string>
    <string name="delete_history_title">Delete saved scan?</string>
    <string name="delete_history_message">This removes the scan from this device.</string>
    <string name="delete">Delete</string>
    <string name="cancel">Cancel</string>
    <string name="history_deleted">Saved scan deleted.</string>

    <string name="share_result">Share Result</string>
    <string name="share_result_subject">LeafGuard AI result</string>
    <string name="share_result_chooser">Share result with</string>
    <string name="share_result_template">Disease: %1$s\nConfidence: %2$d%%\n\nSymptoms: %3$s\n\nTreatment: %4$s\n\nPrevention: %5$s\n\nModel suggestion only; verify serious cases with a qualified agricultural source.</string>
    <string name="include_location">Include optional last-known location when saving</string>
    <string name="location_permission_denied">Location permission denied. Saving without location.</string>
    <string name="history_location_format">Location: %1$.5f, %2$.5f</string>
    <string name="history_location_unavailable">Location was not saved.</string>

    <string name="settings_scan_reminders">Daily scan reminder</string>
    <string name="settings_scan_reminders_description">Schedule one local reminder approximately every 24 hours.</string>
    <string name="notification_channel_name">Leaf scan reminders</string>
    <string name="notification_channel_description">Reminders to inspect plants and capture a fresh leaf image.</string>
    <string name="notification_reminder_title">Check your plants</string>
    <string name="notification_reminder_message">Capture a fresh leaf image and compare it with earlier scans.</string>
    <string name="notification_permission_denied">Notifications remain disabled.</string>
</resources>
```


### 12.16 Files Week 10 Does Not Rewrite

| Area | Status | Reason |
|---|---|---|
| Scan/TFLite/cloud files | Unchanged | Week 09 inference complete |
| Result response extras | Unchanged | Utilities consume existing values |
| XML catalog/repository | Unchanged | Week 08 guidance complete |
| DAO/history list | Unchanged | Existing queries support nullable columns |
| Camera/network security/model assets | Unchanged | No utility ownership |
| Background location/maps | Absent | Privacy boundary |
| Analytics/navigation redesign | Absent | Later polish |
| Week 11 tests/performance | Deferred | Dedicated next week |
| Signing/release | Deferred | Week 12 |

### 12.17 Verify Exact End State

```bash
cd android-app-kotlin
./gradlew assembleDebug
```

Static checks:

```bash
grep -E 'ACCESS_(COARSE|FINE)_LOCATION|POST_NOTIFICATIONS' app/src/main/AndroidManifest.xml
grep -E 'version = 2|MIGRATION_1_2|ADD COLUMN (latitude|longitude)' \
  app/src/main/java/com/leafguard/database/AppDatabase.kt
grep -E 'enqueueUniquePeriodicWork|cancelUniqueWork' \
  app/src/main/java/com/leafguard/utils/NotificationHelper.kt
```

Manual behavior:

| Test | Expected |
|---|---|
| Share | Chooser opens with text/disclaimer; no location/image |
| Save unchecked | Null location; normal save |
| Save checked/granted | Last-known coordinates or null if unavailable |
| Save checked/denied | Explanation and successful null-location save |
| Existing v1 data | Upgrade to v2 preserves rows |
| History detail | Coordinates or explicit not-saved state |
| Reminder enable | Permission if needed; one unique periodic work |
| Re-enable | No duplicate periodic work |
| Reminder disable | Unique work cancelled |
| Worker without permission | No crash/no notification |

---

## 13. Learning-to-Evidence Map

| Concept | Exercise | Build step | Proof |
|---|---|---|---|
| Share Intent/privacy | 1 | 2 | Chooser payload |
| Optional permission | 2 | 4 | Grant/deny saves |
| Nullable schema | 3 | 3 | Entity/generated schema |
| Migration | 3 | 3 | v1 row preserved in v2 |
| Channel/permission | 4 | 5 | Channel/settings behavior |
| Unique periodic work | 4 | 6 | WorkManager inspection |
| Preferences | 5 | 6 | Toggle survives reopen |
| Failure/boundary | 6 | 7 | Denial/revocation/cancel evidence |

## 14. Understanding Checklist

- [ ] I can explain implicit sharing and chooser behavior.
- [ ] I can explain why image/location are excluded from share.
- [ ] I can explain permission request timing and denial fallback.
- [ ] I can explain nullable location versus `0.0`.
- [ ] I can explain Room version 2 and migration SQL.
- [ ] I can explain why existing rows survive.
- [ ] I can explain notification channels and Android 13 permission.
- [ ] I can explain WorkManager's approximate timing.
- [ ] I can explain unique periodic work and cancellation.
- [ ] I can explain SharedPreferences persistence.
- [ ] I can identify all 2 new and 11 expanded files.
- [ ] I can demonstrate sharing, grant/deny location, and reminder enable/disable.
- [ ] I know Week 11 owns testing/debug/performance.
- [ ] I know Week 12 owns release.

<!-- NAV_FOOTER_START -->

---

## Navigation

[README](README.md) | **Learning Notes** | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Week 09](../week-09-tensorflow-lite-offline-ai/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 11](../week-11-testing-debugging-performance/README.md)
