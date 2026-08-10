# Week 07 Learning Notes: Room History From the Week 06 Result

## Purpose

These notes explain how to persist the complete Week 06 result locally with Room, display newest-first history, open one saved record, and delete it safely.

Section 12 is the authoritative reconstruction appendix. It contains all 14 Week 07 new or expanded Kotlin-track files in full. The cumulative snapshot was independently compiled with `./gradlew assembleDebug`.

---

## 1. How Week 07 Grows From Week 06

Week 06 produces eight transient values:

```text
model_label, disease, confidence, uncertain,
guidance_available, symptoms, treatment, prevention
```

Week 07 preserves all eight and adds local identity and time:

```text
Week 06 result
  + generated id
  + save timestamp
  = one ScanRecord row
```

The user explicitly chooses whether to save. Opening History never calls the network or model again.

---

## 2. SQLite and Room

SQLite is Android's embedded relational database. Room is Android Jetpack's typed layer over SQLite.

| SQLite idea | Room representation | LeafGuard example |
|---|---|---|
| Table | `@Entity` data class | `scan_history` |
| Column | Entity property | `confidence` |
| Primary key | `@PrimaryKey` | generated `id` |
| SQL operation | `@Dao` method | `getAllScans()` |
| Database | `RoomDatabase` | `AppDatabase` |

Room checks SQL and object mappings during compilation. It does not remove the need to understand the schema.

---

## 3. Exact 10-Column Schema

| Column | Kotlin type | Rule |
|---|---|---|
| `id` | `Long` | Primary key, auto-generated, default 0 before insert |
| `model_label` | `String` | Exact canonical model label |
| `disease` | `String` | Display label |
| `confidence` | `Float` | Original 0.0-1.0 value |
| `uncertain` | `Boolean` | Preserve server decision |
| `guidance_available` | `Boolean` | Preserve reviewed-guidance state |
| `symptoms` | `String` | Response guidance/fallback |
| `treatment` | `String` | Response guidance/fallback |
| `prevention` | `String` | Response guidance/fallback |
| `timestamp` | `Long` | `System.currentTimeMillis()` at save time |

Do not store only the disease name. A saved history record must be able to reconstruct the complete result without rerunning inference.

---

## 4. Entity: Object-to-Row Mapping

```kotlin
@Entity(tableName = "scan_history")
data class ScanRecord(...)
```

`@ColumnInfo` makes persisted names explicit. `id=0` means "not inserted yet"; Room replaces it with a unique positive key.

The entity is immutable. Saving creates one complete `ScanRecord` and inserts it as one row.

---

## 5. DAO and SQL

The DAO defines four operations:

| Method | SQL/annotation | Job |
|---|---|---|
| `insertScan` | `@Insert` | Create one row and return its ID |
| `getAllScans` | `ORDER BY timestamp DESC` | Read newest first |
| `getScanById` | `WHERE id = :id LIMIT 1` | Read one detail record |
| `deleteScanById` | `DELETE ... WHERE id = :id` | Delete one row and return count |

All methods are `suspend` because disk I/O must not block the main thread.

---

## 6. Database Singleton

Building Room repeatedly wastes resources and can create concurrency problems. `AppDatabase` uses one process-wide instance:

```text
fast path: existing instance
  -> return it

first access:
  -> synchronized block
  -> build with applicationContext
  -> store instance
```

`@Volatile` makes writes visible across threads. The second null check inside `synchronized` prevents two instances during a race.

Database version starts at 1. Week 07 has no migration because this is the initial schema.

---

## 7. Explicit Save Flow

`ResultActivity` already holds all eight values. Week 07 adds a Save button:

```text
tap Save
  -> disable button
  -> create ScanRecord
  -> lifecycleScope.launch
  -> DAO insert
  -> show Saved state
```

Saving is not automatic. Explicit choice avoids creating records for every accidental or low-value result.

The button is disabled after insertion to prevent duplicate taps during the same ResultActivity instance.

---

## 8. History List and Lifecycle

`HistoryActivity` uses RecyclerView:

```text
Room List<ScanRecord>
  -> HistoryAdapter
  -> item_scan_history.xml
  -> disease + confidence + timestamp
```

History loads in `onResume`, not only `onCreate`. When detail deletion finishes and the user returns, `onResume` queries Room again and refreshes the list.

Empty state rule:

- no rows: hide RecyclerView, show `history_empty`
- one or more rows: show RecyclerView, hide empty message

---

## 9. Detail Navigation by Primary Key

The list passes only `record.id`:

```kotlin
intent.putExtra(HistoryDetailActivity.EXTRA_SCAN_ID, record.id)
```

Detail then reads the authoritative row from Room. Passing every value as Intent extras would duplicate persistence data and could display stale content after deletion or update.

Invalid or missing IDs close safely with feedback.

---

## 10. Delete With Confirmation

Deletion is destructive. Week 07 requires:

1. user taps Delete
2. confirmation dialog explains local removal
3. cancel leaves the row unchanged
4. confirm calls DAO by primary key
5. detail closes
6. History refreshes in `onResume`

The DAO returns the number of deleted rows. A positive value proves a row matched.

---

## 11. Boundaries and Common Mistakes

Avoid:

- storing only five of the eight response fields
- using disease name as a primary key
- running DAO work synchronously on the UI thread
- creating a database instance per Activity
- sorting history in Kotlin instead of SQL
- loading history only in `onCreate`
- passing the whole record through many Intent extras
- deleting without confirmation
- using destructive migration for a production schema
- adding location, XML disease enrichment, TFLite, sharing, or analytics in Week 07

Room history is local application data. It is not cloud synchronization, model evidence, or a confirmed diagnosis record.

---

## 12. End-of-Week-07 File Inventory (Exact Files, Exact Code, Exact Size)

Week 06 changes zero Android files, so Week 07 expands the cumulative Week 05 Android networking snapshot. It creates 7 files and expands 7 files.

### 12.1 Change Summary: Week 06 -> Week 07

| Change | Count | Files |
|---|---:|---|
| New | 7 | Entity, DAO, database, adapter, detail Activity, detail layout, item layout |
| Expanded | 7 | Gradle, manifest, Result/History Activities, Result/History layouts, strings |
| Inference/network changes | 0 | Weeks 05-06 remain unchanged |
| Later-week changes | 0 | No XML library, location, offline model, notification, or UI redesign |

**Total complete changed/new code: 864 logical lines.**

### 12.2 Exact Week 07 Tree

```text
android-app-kotlin/app/
|-- build.gradle                                  EXPANDED   55 lines
`-- src/main/
    |-- AndroidManifest.xml                       EXPANDED   58 lines
    |-- java/com/leafguard/
    |   |-- ResultActivity.kt                     EXPANDED   98 lines
    |   |-- HistoryActivity.kt                    EXPANDED   52 lines
    |   |-- HistoryAdapter.kt                     NEW        54 lines
    |   |-- HistoryDetailActivity.kt              NEW       112 lines
    |   `-- database/
    |       |-- ScanRecord.kt                     NEW        29 lines
    |       |-- ScanDao.kt                        NEW        21 lines
    |       `-- AppDatabase.kt                    NEW        34 lines
    `-- res/
        |-- layout/
        |   |-- activity_result.xml               EXPANDED  122 lines
        |   |-- activity_history.xml              EXPANDED   23 lines
        |   |-- activity_history_detail.xml       NEW       106 lines
        |   `-- item_scan_history.xml             NEW        31 lines
        `-- values/strings.xml                    EXPANDED   69 lines
```

### 12.3 Expanded File: `app/build.gradle` (47 -> 55 lines)

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

    def room_version = "2.6.1"
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
}
```

Room runtime supplies database APIs, Room KTX supports coroutine transactions/DAOs, and kapt generates the implementation from annotations.

### 12.4 Expanded File: `AndroidManifest.xml` (55 -> 58 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-feature
        android:name="android.hardware.camera"
        android:required="false" />

    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.INTERNET" />

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
            android:name=".SettingsActivity"
            android:exported="false" />
    </application>

</manifest>
```

Only `HistoryDetailActivity` is new in the manifest. Room requires no permission.

### 12.5 New File: `database/ScanRecord.kt` (29 lines)

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
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)
```

### 12.6 New File: `database/ScanDao.kt` (21 lines)

```kotlin
package com.leafguard.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScanDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertScan(record: ScanRecord): Long

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    suspend fun getAllScans(): List<ScanRecord>

    @Query("SELECT * FROM scan_history WHERE id = :id LIMIT 1")
    suspend fun getScanById(id: Long): ScanRecord?

    @Query("DELETE FROM scan_history WHERE id = :id")
    suspend fun deleteScanById(id: Long): Int
}
```

### 12.7 New File: `database/AppDatabase.kt` (34 lines)

```kotlin
package com.leafguard.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ScanRecord::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanDao

    companion object {
        private const val DATABASE_NAME = "leafguard.db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build().also { database ->
                    instance = database
                }
            }
        }
    }
}
```

### 12.8 New File: `HistoryAdapter.kt` (54 lines)

```kotlin
package com.leafguard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leafguard.database.ScanRecord
import java.text.DateFormat
import java.util.Date

class HistoryAdapter(
    private val onItemSelected: (ScanRecord) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val items = mutableListOf<ScanRecord>()

    fun submitList(scans: List<ScanRecord>) {
        items.clear()
        items.addAll(scans)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position], onItemSelected)
    }

    override fun getItemCount(): Int = items.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textDisease: TextView = itemView.findViewById(R.id.textHistoryDisease)
        private val textConfidence: TextView = itemView.findViewById(R.id.textHistoryConfidence)
        private val textTimestamp: TextView = itemView.findViewById(R.id.textHistoryTimestamp)

        fun bind(record: ScanRecord, onItemSelected: (ScanRecord) -> Unit) {
            textDisease.text = record.disease
            textConfidence.text = itemView.context.getString(
                R.string.history_confidence_format,
                record.confidence * 100f
            )
            textTimestamp.text = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.SHORT
            ).format(Date(record.timestamp))
            itemView.setOnClickListener { onItemSelected(record) }
        }
    }
}
```

### 12.9 Expanded File: `HistoryActivity.kt` (12 -> 52 lines)

```kotlin
package com.leafguard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leafguard.database.AppDatabase
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var textHistoryEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerHistory = findViewById(R.id.recyclerHistory)
        textHistoryEmpty = findViewById(R.id.textHistoryEmpty)
        historyAdapter = HistoryAdapter { record ->
            val intent = Intent(this, HistoryDetailActivity::class.java).apply {
                putExtra(HistoryDetailActivity.EXTRA_SCAN_ID, record.id)
            }
            startActivity(intent)
        }

        recyclerHistory.layoutManager = LinearLayoutManager(this)
        recyclerHistory.adapter = historyAdapter
    }

    override fun onResume() {
        super.onResume()
        loadHistory()
    }

    private fun loadHistory() {
        lifecycleScope.launch {
            val scans = AppDatabase.getInstance(applicationContext)
                .scanDao()
                .getAllScans()
            historyAdapter.submitList(scans)
            val hasHistory = scans.isNotEmpty()
            recyclerHistory.visibility = if (hasHistory) View.VISIBLE else View.GONE
            textHistoryEmpty.visibility = if (hasHistory) View.GONE else View.VISIBLE
        }
    }
}
```

### 12.10 New File: `HistoryDetailActivity.kt` (112 lines)

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

### 12.11 Expanded File: `ResultActivity.kt` (56 -> 98 lines)

```kotlin
package com.leafguard

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.leafguard.database.AppDatabase
import com.leafguard.database.ScanRecord
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {

    private lateinit var modelLabel: String
    private lateinit var disease: String
    private var confidence: Float = 0f
    private var uncertain: Boolean = true
    private var guidanceAvailable: Boolean = false
    private lateinit var symptoms: String
    private lateinit var treatment: String
    private lateinit var prevention: String

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
        findViewById<Button>(R.id.buttonSaveHistory).setOnClickListener {
            saveToHistory(it as Button)
        }
    }

    private fun saveToHistory(saveButton: Button) {
        saveButton.isEnabled = false
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

### 12.12 Expanded File: `activity_history.xml` (25 -> 23 lines)

The placeholder becomes a RecyclerView plus an overlapping empty-state message.

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screen_background">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerHistory"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:clipToPadding="false"
        android:padding="16dp" />

    <TextView
        android:id="@+id/textHistoryEmpty"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:padding="24dp"
        android:text="@string/history_empty"
        android:textColor="@color/text_secondary"
        android:textSize="16sp" />
</FrameLayout>
```

### 12.13 New File: `activity_history_detail.xml` (106 lines)

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

### 12.14 New File: `item_scan_history.xml` (31 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="12dp"
    android:background="#E8F5E9"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:id="@+id/textHistoryDisease"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="@color/text_primary"
        android:textSize="18sp"
        android:textStyle="bold" />

    <TextView
        android:id="@+id/textHistoryConfidence"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="4dp"
        android:textColor="@color/text_secondary" />

    <TextView
        android:id="@+id/textHistoryTimestamp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="4dp"
        android:textColor="@color/text_secondary" />
</LinearLayout>
```

### 12.15 Expanded File: `activity_result.xml` (115 -> 122 lines)

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
            android:id="@+id/buttonSaveHistory"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:text="@string/save_to_history" />
    </LinearLayout>
</ScrollView>
```

### 12.16 Expanded File: `strings.xml` (55 -> 69 lines)

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

    <string name="placeholder_library">The XML disease library will be added in Week 08.</string>
    <string name="placeholder_settings">Course project shell. Settings options will grow in later weeks.</string>

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
</resources>
```

### 12.17 Files Week 07 Does Not Rewrite

| Area | Status | Reason |
|---|---|---|
| `ScanActivity` and networking files | Unchanged | Upload contract is complete |
| FastAPI and Keras files | Unchanged | Week 06 inference remains complete |
| Main screen/layout | Unchanged | Navigation already opens History |
| Disease Library Activity/layout | Placeholder | Week 08 |
| Settings Activity/layout | Placeholder | Later week |
| Colors/themes/security XML | Unchanged | No persistence change needed |
| Location, share, analytics | Absent | Later scope |
| TFLite/offline inference | Absent | Later scope |

### 12.18 Verify the Exact End State

```bash
cd android-app-kotlin
./gradlew assembleDebug
```

Manual checks:

| Test | Expected |
|---|---|
| Open empty History | Empty message visible |
| Save one result | Button disables; success shown |
| Open History | One item with disease/confidence/time |
| Restart app | Saved item remains |
| Save second result | Newest item appears first |
| Open detail | All eight result values and time represented |
| Delete, cancel | Record remains |
| Delete, confirm | Record disappears after returning |
| Invalid detail ID | Safe message and finish |

Save evidence under `docs/evidence/week-07/`.

---

## 13. Learning-to-Evidence Map

| Concept | Exercise | Build step | Proof |
|---|---|---|---|
| 10-column schema | 1 | 2 | Entity/source inspection |
| DAO SQL | 2 | 3 | Save/list/detail/delete behavior |
| Singleton database | 3 | 4 | Reused instance/build |
| Coroutine disk I/O | 3 | 5 | Responsive save/load/delete |
| RecyclerView list | 4 | 6 | Newest-first list screenshot |
| Primary-key detail | 5 | 7 | Exact detail record |
| Lifecycle refresh/delete | 5 | 8 | Return-to-list update |
| Persistence after restart | 6 | 9 | Restart demo |

---

## 14. Week 07 Understanding Checklist

- [ ] I can explain why Week 07 begins from all eight Week 06 values.
- [ ] I can name all 10 database columns.
- [ ] I can explain primary-key auto-generation.
- [ ] I can explain Entity, DAO, and RoomDatabase roles.
- [ ] I can explain all four DAO operations.
- [ ] I can explain why DAO methods are suspend functions.
- [ ] I can explain the singleton/double-check pattern.
- [ ] I can explain newest-first SQL ordering.
- [ ] I can explain why History reloads in `onResume`.
- [ ] I can explain why detail receives only an ID.
- [ ] I can demonstrate save, restart persistence, detail, and delete.
- [ ] I can identify all 7 new and 7 expanded files.
- [ ] I can explain why inference does not change this week.
- [ ] I know that Week 08 owns the XML disease library.

<!-- NAV_FOOTER_START -->

---

## Week 07 Navigation

| Step | File | Description |
|---:|---|---|
| 1 | [README.md](README.md) | Week overview |
| **2** | **learning-notes.md** - current | Theory and exact source snapshot |
| 3 | [exercises.md](exercises.md) | Guided practice |
| 4 | [build-task.md](build-task.md) | Implementation guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation and evidence |
| 6 | [quiz.md](quiz.md) | Knowledge assessment |
| 7 | [reflection.md](reflection.md) | Reflection and handoff |

[Previous: Week 06](../week-06-cloud-ml-model/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 08](../week-08-xml-disease-library/README.md)