# Week 08 Learning Notes: Validated Local XML Disease Guidance

## Purpose

These notes explain how to parse a bundled 10-entry XML catalog, cache validated objects, browse summaries/details, and enrich matching Week 07 results before local persistence.

Section 12 is the authoritative reconstruction appendix. It contains all 13 Week 08 new or expanded files in full. The cumulative snapshot was independently compiled with `./gradlew assembleDebug`.

---

## 1. How Week 08 Grows From Week 07

Week 07 persists all eight result values. Three values contain guidance:

```text
symptoms, treatment, prevention
```

Week 08 adds one reviewed local source for those fields:

```text
prediction disease display name
  -> local XML exact-name lookup
  -> match: replace three guidance fields
  -> no match/error: preserve backend guidance
  -> enable Save
  -> Week 07 Room record
```

No prediction is generated from XML. XML is reference content only.

---

## 2. Why XML Is Bundled in `assets/`

`app/src/main/assets/` stores files with their original names and structure. Android opens them with:

```kotlin
context.assets.open("diseases.xml")
```

Benefits:

- available without network
- versioned with app content
- human-readable and reviewable
- one structured source for list, detail, and result guidance

Limitations:

- read-only at runtime
- app update required to change content
- not a database or model
- does not automatically cover every model class

---

## 3. Exact XML Schema

The root is `<diseases>`. It contains exactly 10 `<disease>` elements. Every entry has five required non-empty children:

| Tag | Meaning | Example |
|---|---|---|
| `<name>` | Display-friendly lookup key | `Tomato Early Blight` |
| `<plant>` | Crop name | `Tomato` |
| `<symptoms>` | Reviewed observation text | Brown spots... |
| `<treatment>` | Reviewed response guidance | Remove infected leaves... |
| `<prevention>` | Reviewed prevention guidance | Rotate crops... |

The parser rejects incomplete entries, duplicate normalized names, and an empty catalog.

---

## 4. Why 10 Entries and 38 Labels Are Both Correct

The Keras model has 38 canonical output labels. The local project has reviewed guidance for 10 display names.

| Collection | Count | Purpose |
|---|---:|---|
| `labels-38.txt` | 38 | Decode model output indexes |
| `diseases.xml` | 10 | Reviewed local guidance |

A valid model result may not have local XML guidance. That is not a parser error. It means the result keeps the safe backend guidance and reports the existing guidance state.

---

## 5. Immutable `Disease` Model

```kotlin
data class Disease(
    val name: String,
    val plant: String,
    val symptoms: String,
    val treatment: String,
    val prevention: String
)
```

The object has exactly five values because that is the XML contract. It has no confidence, model index, database ID, severity, location, or image URI.

Immutable values prevent a screen from silently altering shared catalog content.

---

## 6. Pull-Parser State Machine

`XmlPullParser` reads one event at a time:

```text
START_TAG <disease>
  -> reset five temporary fields
START_TAG <name> + TEXT
  -> assign name
...
END_TAG </disease>
  -> validate fields and duplicate name
  -> create immutable Disease
END_DOCUMENT
  -> require at least one entry
```

This is memory-efficient because Android does not build a complete DOM tree.

The parser normalizes names with trim + lowercase only for uniqueness/lookup. It preserves original display capitalization in the `Disease` object.

---

## 7. Repository and Cache

Screens should not each reopen and reparse XML. `DiseaseRepository` owns:

- application context
- asset filename
- cached parsed list
- exact-name lookup
- one singleton instance

```text
first request -> open asset -> parse -> cache
later request -> return cached immutable list
```

The repository does not contain fallback hardcoded diseases. Missing or malformed reviewed content remains visible as an error instead of silently substituting unreviewed data.

---

## 8. Asynchronous Asset I/O

Asset parsing is file I/O. Library, detail, and Result lookup call the repository inside:

```kotlin
withContext(Dispatchers.IO) { ... }
```

`lifecycleScope.launch` returns to the main thread for UI changes. This mirrors Week 07's rule that disk work must not block Android rendering.

---

## 9. Browse and Detail Flow

```text
DiseaseLibraryActivity
  -> repository.getAllDiseases()
  -> DiseaseAdapter
  -> item_disease.xml
  -> row tap passes disease.name
  -> DiseaseDetailActivity
  -> repository.findByName(name)
  -> render five fields
```

The list displays a summary: name, plant, and two-line symptoms preview. Detail displays complete guidance.

Passing only the stable display name avoids duplicating all text in Intent extras and lets detail read the cached source.

---

## 10. Result Guidance Enrichment

The API's `disease` field is display-friendly. It matches XML `<name>` values such as `Apple Scab`.

It does **not** use canonical model text such as `Apple___Apple_scab`.

ResultActivity behavior:

1. render the Week 06 response
2. disable Save while local lookup runs
3. find XML by display disease name on `Dispatchers.IO`
4. if found, replace symptoms/treatment/prevention and show local source
5. if absent or parser fails, retain existing backend guidance
6. enable Save

Disabling Save prevents Week 07 Room from storing pre-enrichment text while a matching local lookup is still running.

---

## 11. Boundaries and Common Mistakes

Avoid:

- claiming 10 XML entries cover all 38 labels
- matching XML display names against `model_label`
- parsing XML separately in every Activity
- hardcoded fallback records that hide catalog defects
- accepting incomplete or duplicate entries
- doing asset I/O on the UI thread
- replacing guidance with blank text when lookup fails
- enabling Save before enrichment finishes
- adding search, severity, location, sharing, analytics, or navigation polish
- treating XML as inference
- adding TFLite/offline inference in Week 08

Week 08 is local reference content and exact-key integration. Week 09 owns offline model inference.

---

## 12. End-of-Week-08 File Inventory (Exact Files, Exact Code, Exact Size)

Week 08 starts from the compiled Week 07 Room/history state. It creates 8 files and expands 5 files. No Gradle dependency changes are required.

### 12.1 Change Summary: Week 07 -> Week 08

| Change | Count | Files |
|---|---:|---|
| New | 8 | Model, parser, repository, adapter, detail Activity/layout, item layout, XML asset |
| Expanded | 5 | Manifest, Library Activity/layout, ResultActivity, strings |
| Gradle/backend/model/Room changes | 0 | Existing contracts are reused |
| Later-week changes | 0 | No search, severity, location, sharing, analytics, UI redesign, or TFLite |

**Total complete changed/new code: 798 logical lines.**

### 12.2 Exact Week 08 Tree

```text
android-app-kotlin/app/src/main/
|-- AndroidManifest.xml                         EXPANDED   61 lines
|-- assets/
|   `-- diseases.xml                            NEW        73 lines
|-- java/com/leafguard/
|   |-- DiseaseAdapter.kt                       NEW        45 lines
|   |-- DiseaseLibraryActivity.kt               EXPANDED   70 lines
|   |-- DiseaseDetailActivity.kt                NEW        67 lines
|   |-- ResultActivity.kt                       EXPANDED  142 lines
|   `-- data/
|       |-- Disease.kt                          NEW         9 lines
|       |-- DiseaseXmlParser.kt                 NEW        76 lines
|       `-- DiseaseRepository.kt                NEW        42 lines
`-- res/
    |-- layout/
    |   |-- activity_disease_library.xml        EXPANDED   41 lines
    |   |-- activity_disease_detail.xml         NEW        63 lines
    |   `-- item_disease.xml                    NEW        33 lines
    `-- values/strings.xml                      EXPANDED   76 lines
```

### 12.3 Expanded File: `AndroidManifest.xml` (58 -> 61 lines)

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
            android:name=".DiseaseDetailActivity"
            android:exported="false" />
        <activity
            android:name=".SettingsActivity"
            android:exported="false" />
    </application>

</manifest>
```

### 12.4 New File: `data/Disease.kt` (9 lines)

```kotlin
package com.leafguard.data

data class Disease(
    val name: String,
    val plant: String,
    val symptoms: String,
    val treatment: String,
    val prevention: String
)
```

### 12.5 New File: `data/DiseaseXmlParser.kt` (76 lines)

```kotlin
package com.leafguard.data

import android.util.Xml
import java.io.InputStream
import java.util.Locale
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

object DiseaseXmlParser {
    @Throws(XmlPullParserException::class)
    fun parse(inputStream: InputStream): List<Disease> {
        val parser = Xml.newPullParser().apply {
            setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            setInput(inputStream, "UTF-8")
        }
        val diseases = mutableListOf<Disease>()
        val normalizedNames = mutableSetOf<String>()

        var name = ""
        var plant = ""
        var symptoms = ""
        var treatment = ""
        var prevention = ""
        var currentTag: String? = null
        var event = parser.eventType

        while (event != XmlPullParser.END_DOCUMENT) {
            when (event) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                    if (currentTag == "disease") {
                        name = ""
                        plant = ""
                        symptoms = ""
                        treatment = ""
                        prevention = ""
                    }
                }
                XmlPullParser.TEXT -> {
                    val value = parser.text.trim()
                    if (value.isNotEmpty()) {
                        when (currentTag) {
                            "name" -> name += value
                            "plant" -> plant += value
                            "symptoms" -> symptoms += value
                            "treatment" -> treatment += value
                            "prevention" -> prevention += value
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "disease") {
                        val values = listOf(name, plant, symptoms, treatment, prevention)
                        if (values.any { it.isBlank() }) {
                            throw XmlPullParserException("Every disease requires five non-empty fields")
                        }
                        val normalizedName = normalizeName(name)
                        if (!normalizedNames.add(normalizedName)) {
                            throw XmlPullParserException("Duplicate disease name: $name")
                        }
                        diseases += Disease(name, plant, symptoms, treatment, prevention)
                    }
                    currentTag = null
                }
            }
            event = parser.next()
        }

        if (diseases.isEmpty()) {
            throw XmlPullParserException("Disease catalog is empty")
        }
        return diseases
    }

    fun normalizeName(value: String): String = value.trim().lowercase(Locale.US)
}
```

### 12.6 New File: `data/DiseaseRepository.kt` (42 lines)

```kotlin
package com.leafguard.data

import android.content.Context

class DiseaseRepository private constructor(context: Context) {
    private val appContext = context.applicationContext

    @Volatile
    private var cachedDiseases: List<Disease>? = null

    fun getAllDiseases(): List<Disease> {
        return cachedDiseases ?: synchronized(this) {
            cachedDiseases ?: appContext.assets.open(ASSET_NAME).use { inputStream ->
                DiseaseXmlParser.parse(inputStream)
            }.also { diseases ->
                cachedDiseases = diseases
            }
        }
    }

    fun findByName(name: String): Disease? {
        val normalizedName = DiseaseXmlParser.normalizeName(name)
        return getAllDiseases().firstOrNull { disease ->
            DiseaseXmlParser.normalizeName(disease.name) == normalizedName
        }
    }

    companion object {
        private const val ASSET_NAME = "diseases.xml"

        @Volatile
        private var instance: DiseaseRepository? = null

        fun getInstance(context: Context): DiseaseRepository {
            return instance ?: synchronized(this) {
                instance ?: DiseaseRepository(context).also { repository ->
                    instance = repository
                }
            }
        }
    }
}
```

### 12.7 New File: `DiseaseAdapter.kt` (45 lines)

```kotlin
package com.leafguard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leafguard.data.Disease

class DiseaseAdapter(
    private val onItemSelected: (Disease) -> Unit
) : RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder>() {
    private val items = mutableListOf<Disease>()

    fun submitList(diseases: List<Disease>) {
        items.clear()
        items.addAll(diseases)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_disease, parent, false)
        return DiseaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        holder.bind(items[position], onItemSelected)
    }

    override fun getItemCount(): Int = items.size

    class DiseaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.textDiseaseName)
        private val textPlant: TextView = itemView.findViewById(R.id.textDiseasePlant)
        private val textSymptoms: TextView = itemView.findViewById(R.id.textDiseaseSymptomsPreview)

        fun bind(disease: Disease, onItemSelected: (Disease) -> Unit) {
            textName.text = disease.name
            textPlant.text = disease.plant
            textSymptoms.text = disease.symptoms
            itemView.setOnClickListener { onItemSelected(disease) }
        }
    }
}
```

### 12.8 Expanded File: `DiseaseLibraryActivity.kt` (12 -> 70 lines)

```kotlin
package com.leafguard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leafguard.data.DiseaseRepository
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserException

class DiseaseLibraryActivity : AppCompatActivity() {
    private lateinit var diseaseAdapter: DiseaseAdapter
    private lateinit var recyclerDiseases: RecyclerView
    private lateinit var textLibraryEmpty: TextView
    private lateinit var progressLibrary: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease_library)

        recyclerDiseases = findViewById(R.id.recyclerDiseases)
        textLibraryEmpty = findViewById(R.id.textLibraryEmpty)
        progressLibrary = findViewById(R.id.progressLibrary)
        diseaseAdapter = DiseaseAdapter { disease ->
            val intent = Intent(this, DiseaseDetailActivity::class.java).apply {
                putExtra(DiseaseDetailActivity.EXTRA_DISEASE_NAME, disease.name)
            }
            startActivity(intent)
        }
        recyclerDiseases.layoutManager = LinearLayoutManager(this)
        recyclerDiseases.adapter = diseaseAdapter
        loadDiseases()
    }

    private fun loadDiseases() {
        lifecycleScope.launch {
            progressLibrary.visibility = View.VISIBLE
            try {
                val diseases = withContext(Dispatchers.IO) {
                    DiseaseRepository.getInstance(applicationContext).getAllDiseases()
                }
                diseaseAdapter.submitList(diseases)
                val hasDiseases = diseases.isNotEmpty()
                recyclerDiseases.visibility = if (hasDiseases) View.VISIBLE else View.GONE
                textLibraryEmpty.visibility = if (hasDiseases) View.GONE else View.VISIBLE
            } catch (exception: IOException) {
                showLibraryError()
            } catch (exception: XmlPullParserException) {
                showLibraryError()
            } finally {
                progressLibrary.visibility = View.GONE
            }
        }
    }

    private fun showLibraryError() {
        recyclerDiseases.visibility = View.GONE
        textLibraryEmpty.visibility = View.VISIBLE
        Toast.makeText(this, R.string.disease_library_error, Toast.LENGTH_LONG).show()
    }
}
```

### 12.9 New File: `DiseaseDetailActivity.kt` (67 lines)

```kotlin
package com.leafguard

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.leafguard.data.Disease
import com.leafguard.data.DiseaseRepository
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserException

class DiseaseDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease_detail)

        val diseaseName = intent.getStringExtra(EXTRA_DISEASE_NAME)
        if (diseaseName.isNullOrBlank()) {
            showErrorAndFinish(R.string.disease_invalid_name)
            return
        }
        loadDisease(diseaseName)
    }

    private fun loadDisease(name: String) {
        lifecycleScope.launch {
            try {
                val disease = withContext(Dispatchers.IO) {
                    DiseaseRepository.getInstance(applicationContext).findByName(name)
                }
                if (disease == null) {
                    showErrorAndFinish(R.string.disease_not_found)
                } else {
                    renderDisease(disease)
                }
            } catch (exception: IOException) {
                showErrorAndFinish(R.string.disease_library_error)
            } catch (exception: XmlPullParserException) {
                showErrorAndFinish(R.string.disease_library_error)
            }
        }
    }

    private fun renderDisease(disease: Disease) {
        findViewById<TextView>(R.id.textDiseaseDetailName).text = disease.name
        findViewById<TextView>(R.id.textDiseaseDetailPlant).text = getString(
            R.string.disease_plant_format,
            disease.plant
        )
        findViewById<TextView>(R.id.textDiseaseDetailSymptoms).text = disease.symptoms
        findViewById<TextView>(R.id.textDiseaseDetailTreatment).text = disease.treatment
        findViewById<TextView>(R.id.textDiseaseDetailPrevention).text = disease.prevention
    }

    private fun showErrorAndFinish(message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        const val EXTRA_DISEASE_NAME = "extra_disease_name"
    }
}
```

### 12.10 Expanded File: `ResultActivity.kt` (98 -> 142 lines)

```kotlin
package com.leafguard

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

### 12.11 Expanded File: `activity_disease_library.xml` (25 -> 41 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screen_background"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/library_title"
        android:textColor="@color/text_primary"
        android:textSize="24sp" />

    <ProgressBar
        android:id="@+id/progressLibrary"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="16dp"
        android:contentDescription="@string/disease_library_loading" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerDiseases"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_marginTop="12dp"
        android:layout_weight="1"
        android:visibility="gone" />

    <TextView
        android:id="@+id/textLibraryEmpty"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:gravity="center"
        android:text="@string/disease_library_empty"
        android:textColor="@color/text_secondary"
        android:visibility="gone" />
</LinearLayout>
```

### 12.12 New File: `activity_disease_detail.xml` (63 lines)

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
            android:id="@+id/textDiseaseDetailName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textColor="@color/leaf_green_dark"
            android:textSize="24sp" />

        <TextView
            android:id="@+id/textDiseaseDetailPlant"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:textColor="@color/text_secondary" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:text="@string/symptoms_heading"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textDiseaseDetailSymptoms"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/treatment_heading"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textDiseaseDetailTreatment"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/prevention_heading"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textDiseaseDetailPrevention"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />
    </LinearLayout>
</ScrollView>
```

### 12.13 New File: `item_disease.xml` (33 lines)

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
        android:id="@+id/textDiseaseName"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="@color/text_primary"
        android:textSize="18sp"
        android:textStyle="bold" />

    <TextView
        android:id="@+id/textDiseasePlant"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="4dp"
        android:textColor="@color/leaf_green_dark" />

    <TextView
        android:id="@+id/textDiseaseSymptomsPreview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="6dp"
        android:ellipsize="end"
        android:maxLines="2"
        android:textColor="@color/text_secondary" />
</LinearLayout>
```

### 12.14 Expanded File: `strings.xml` (69 -> 76 lines)

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

    <string name="placeholder_settings">Course project shell. Settings options will grow in later weeks.</string>

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

### 12.15 New File: `assets/diseases.xml` (73 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<diseases>
    <disease>
        <name>Tomato Early Blight</name>
        <plant>Tomato</plant>
        <symptoms>Small brown spots with concentric rings, yellowing around lesions, and damage starting on older leaves.</symptoms>
        <treatment>Remove infected leaves, improve airflow, mulch soil, and apply a labeled fungicide when pressure is high.</treatment>
        <prevention>Rotate crops, avoid wetting foliage in the evening, and disinfect tools between plants.</prevention>
    </disease>
    <disease>
        <name>Tomato Late Blight</name>
        <plant>Tomato</plant>
        <symptoms>Water-soaked patches that quickly darken, white fuzzy growth underneath leaves, and rapid whole-plant collapse.</symptoms>
        <treatment>Isolate infected plants, remove severely affected tissue, and apply an appropriate late blight fungicide immediately.</treatment>
        <prevention>Use disease-free seedlings, space plants well, and avoid overhead irrigation during humid weather.</prevention>
    </disease>
    <disease>
        <name>Tomato Healthy</name>
        <plant>Tomato</plant>
        <symptoms>Leaf remains green, evenly colored, and free from lesions, mold, curling, or necrotic patches.</symptoms>
        <treatment>No treatment needed. Continue normal watering, feeding, and routine scouting.</treatment>
        <prevention>Maintain balanced nutrition, monitor weekly, and keep weeds and debris away from the crop.</prevention>
    </disease>
    <disease>
        <name>Potato Early Blight</name>
        <plant>Potato</plant>
        <symptoms>Dark target-like rings on older leaves followed by yellowing and premature leaf drop.</symptoms>
        <treatment>Prune affected leaves, support plant vigor with correct fertilization, and treat with fungicide if needed.</treatment>
        <prevention>Rotate away from solanaceous crops and water at soil level instead of soaking the canopy.</prevention>
    </disease>
    <disease>
        <name>Potato Late Blight</name>
        <plant>Potato</plant>
        <symptoms>Dark blotches expand rapidly, stems blacken, and white mold may appear at lesion edges in humid conditions.</symptoms>
        <treatment>Remove badly infected foliage, avoid moving spores between rows, and apply a recommended protectant fungicide.</treatment>
        <prevention>Plant resistant varieties where possible and destroy volunteer potatoes after harvest.</prevention>
    </disease>
    <disease>
        <name>Potato Healthy</name>
        <plant>Potato</plant>
        <symptoms>Leaves look firm, green, and free of spots, halos, wilting, or unusual discoloration.</symptoms>
        <treatment>No treatment required beyond standard crop care.</treatment>
        <prevention>Keep monitoring field hygiene, irrigation balance, and nutrient supply.</prevention>
    </disease>
    <disease>
        <name>Corn Gray Leaf Spot</name>
        <plant>Corn</plant>
        <symptoms>Rectangular gray or tan lesions running parallel to leaf veins, usually beginning on lower leaves.</symptoms>
        <treatment>Scout regularly, remove heavily damaged leaves where practical, and apply fungicide based on local guidance.</treatment>
        <prevention>Rotate fields, manage residue, and choose resistant hybrids when available.</prevention>
    </disease>
    <disease>
        <name>Corn Northern Leaf Blight</name>
        <plant>Corn</plant>
        <symptoms>Long cigar-shaped gray-green lesions that enlarge and reduce photosynthetic area.</symptoms>
        <treatment>Use a registered fungicide if disease pressure is high and preserve plant vigor with good agronomy.</treatment>
        <prevention>Rotate crops, select resistant seed, and avoid continuous corn where blight is common.</prevention>
    </disease>
    <disease>
        <name>Corn Healthy</name>
        <plant>Corn</plant>
        <symptoms>Leaves are uniformly green with normal vein structure and no blight or spotting patterns.</symptoms>
        <treatment>No treatment required.</treatment>
        <prevention>Continue regular monitoring, balanced fertilization, and integrated pest management.</prevention>
    </disease>
    <disease>
        <name>Apple Scab</name>
        <plant>Apple</plant>
        <symptoms>Olive-brown velvety leaf lesions, fruit spotting, and leaf distortion in wet spring conditions.</symptoms>
        <treatment>Prune for airflow, remove fallen leaves, and apply protectant fungicides during susceptible growth stages.</treatment>
        <prevention>Use resistant cultivars, sanitize orchard litter, and monitor wet periods carefully.</prevention>
    </disease>
</diseases>
```


### 12.16 Files Week 08 Does Not Rewrite

| Area | Status | Reason |
|---|---|---|
| `app/build.gradle` | Unchanged | Week 07 already has lifecycle/RecyclerView |
| Scan/network files | Unchanged | Week 05 upload contract remains complete |
| FastAPI/Keras files | Unchanged | Week 06 inference remains complete |
| Room entity/DAO/database/history | Unchanged | Week 07 persistence schema remains complete |
| Main/Settings screens | Unchanged | No XML-library ownership |
| Camera/network security resources | Unchanged | No catalog dependency |
| Search/severity/fallback data | Absent | Later UI/content work |
| Location/share/analytics/navigation polish | Absent | Later scope |
| TFLite/offline classifier | Absent | Week 09 |

### 12.17 Verify the Exact End State

```bash
cd android-app-kotlin
./gradlew assembleDebug
```

Static catalog checks:

```bash
grep -c '<disease>' app/src/main/assets/diseases.xml
grep -c '<name>' app/src/main/assets/diseases.xml
grep -c '<plant>' app/src/main/assets/diseases.xml
grep -c '<symptoms>' app/src/main/assets/diseases.xml
grep -c '<treatment>' app/src/main/assets/diseases.xml
grep -c '<prevention>' app/src/main/assets/diseases.xml
```

Every command should print `10`.

Manual behavior:

| Test | Expected |
|---|---|
| Open Library without backend | 10 entries load |
| Tap one entry | Complete five-field detail |
| Matching Result display name | Local XML guidance/source shown |
| Save matching Result | Room stores enriched guidance |
| Unmatched display name | Existing backend guidance remains |
| Missing/malformed asset | Safe error; no hardcoded fallback |
| Reopen list/detail | Repository reuses cached parsed objects |

Save evidence under `docs/evidence/week-08/`.

---

## 13. Learning-to-Evidence Map

| Concept | Exercise | Build step | Proof |
|---|---|---|---|
| Five-field schema | 1 | 2 | XML counts/source review |
| Pull-parser state | 2 | 3 | Valid catalog and rejection cases |
| Repository cache | 3 | 4 | Repeated list/detail lookup |
| RecyclerView summary | 4 | 5 | Ten-item library screenshot |
| Detail by display name | 4 | 6 | Complete detail screenshot |
| Result enrichment | 5 | 7 | Local-source Result evidence |
| Safe unmatched/error behavior | 5 | 7 | Backend guidance preserved |
| Offline reference boundary | 6 | 8 | Backend-off library demo |

---

## 14. Week 08 Understanding Checklist

- [ ] I can explain why 10 reviewed entries and 38 model labels are both correct.
- [ ] I can name all five required XML fields.
- [ ] I can explain assets versus Android resources.
- [ ] I can trace pull-parser events into immutable objects.
- [ ] I can explain incomplete/duplicate/empty rejection.
- [ ] I can explain repository caching and application context.
- [ ] I can explain why parsing runs on `Dispatchers.IO`.
- [ ] I can explain adapter/list/detail roles.
- [ ] I can explain why detail receives display name only.
- [ ] I can explain `disease` versus `model_label` lookup.
- [ ] I can explain matching and unmatched Result behavior.
- [ ] I can explain why Save waits for enrichment.
- [ ] I can identify all 8 new and 5 expanded files.
- [ ] I know XML is reference content, not inference.
- [ ] I know Week 09 owns TFLite/offline inference.

<!-- NAV_FOOTER_START -->

---

## Week 08 Navigation

| Step | File | Description |
|---:|---|---|
| 1 | [README.md](README.md) | Week overview |
| **2** | **learning-notes.md** - current | Theory and exact source snapshot |
| 3 | [exercises.md](exercises.md) | Guided practice |
| 4 | [build-task.md](build-task.md) | Implementation guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation and evidence |
| 6 | [quiz.md](quiz.md) | Knowledge assessment |
| 7 | [reflection.md](reflection.md) | Reflection and handoff |

[Previous: Week 07](../week-07-room-sqlite-history/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 09](../week-09-tensorflow-lite-offline-ai/README.md)
