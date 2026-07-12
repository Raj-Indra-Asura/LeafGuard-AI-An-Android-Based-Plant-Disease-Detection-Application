package com.leafguard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.leafguard.databinding.ActivityDiseaseLibraryBinding
import com.leafguard.ui.setupBottomNav
import java.io.IOException
import java.io.InputStream
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

/**
 * Kotlin twin of DiseaseLibraryActivity.java.
 *
 * Displays the full plant disease reference library by parsing
 * the bundled diseases.xml file with XmlPullParser, with a search box to
 * filter by name/plant and a severity chip per entry.
 *
 * Architecture role: read-only reference screen, no database access needed.
 * XML source: assets/diseases.xml (or res/xml/diseases.xml if present)
 */
class DiseaseLibraryActivity : AppCompatActivity() {

    companion object {
        /**
         * Number of diseases shown on the Home dashboard's "Library" card
         * when assets/diseases.xml is not bundled. Keep this in sync with
         * [getFallbackDiseaseList] below.
         */
        const val FALLBACK_DISEASE_COUNT = 5
    }

    private var binding: ActivityDiseaseLibraryBinding? = null
    private lateinit var adapter: DiseaseAdapter
    private var allDiseases: List<DiseaseEntry> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDiseaseLibraryBinding.inflate(layoutInflater)
        this.binding = binding
        setContentView(binding.root)

        setupBottomNav(binding.bottomNavigation, R.id.nav_library)

        adapter = DiseaseAdapter(mutableListOf())
        binding.recyclerDiseaseLibrary.layoutManager = LinearLayoutManager(this)
        binding.recyclerDiseaseLibrary.adapter = adapter

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterDiseases(s?.toString().orEmpty())
            }
        })

        loadDiseases()
    }

    /**
     * Parse diseases.xml from assets using XmlPullParser.
     *
     * XmlPullParser is the Android-recommended parser — it is event-driven,
     * memory-efficient (does not load the full DOM like DOM parser), and
     * synchronous (unlike SAX which requires callbacks).
     *
     * Expected XML structure:
     * <diseases>
     *   <disease>
     *     <name>Tomato Early Blight</name>
     *     <plant>Tomato</plant>
     *     <symptoms>Brown lesions with concentric rings...</symptoms>
     *     <treatment>Apply copper-based fungicide...</treatment>
     *     <prevention>Rotate crops, avoid overhead watering</prevention>
     *     <severity>medium</severity>
     *   </disease>
     * </diseases>
     */
    private fun loadDiseases() {
        val diseases: List<DiseaseEntry> = try {
            assets.open("diseases.xml").use(::parseDiseaseXml)
        } catch (e: IOException) {
            // assets/diseases.xml not present or malformed — load built-in fallback data
            getFallbackDiseaseList()
        } catch (e: XmlPullParserException) {
            getFallbackDiseaseList()
        }

        allDiseases = diseases
        filterDiseases(binding?.editTextSearch?.text?.toString().orEmpty())
    }

    /** Filters [allDiseases] by disease name or plant name (case-insensitive) and re-renders the list. */
    private fun filterDiseases(query: String) {
        val binding = binding ?: return
        val filtered = if (query.isBlank()) {
            allDiseases
        } else {
            allDiseases.filter {
                it.name.contains(query, ignoreCase = true) || it.plant.contains(query, ignoreCase = true)
            }
        }

        val hasItems = filtered.isNotEmpty()
        adapter.submitList(filtered)
        binding.recyclerDiseaseLibrary.visibility = if (hasItems) View.VISIBLE else View.GONE
        binding.textEmptyLibrary.visibility = if (hasItems) View.GONE else View.VISIBLE
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parseDiseaseXml(inputStream: InputStream): List<DiseaseEntry> {
        val diseases = mutableListOf<DiseaseEntry>()
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(inputStream, "UTF-8")

        var current: DiseaseEntry? = null
        var tagName: String? = null
        var eventType = parser.eventType

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    tagName = parser.name
                    if ("disease" == tagName) {
                        current = DiseaseEntry()
                    }
                }

                XmlPullParser.TEXT -> {
                    val entry = current
                    if (entry != null && tagName != null) {
                        val text = parser.text.trim()
                        when (tagName) {
                            "name" -> entry.name = text
                            "plant" -> entry.plant = text
                            "symptoms" -> entry.symptoms = text
                            "treatment" -> entry.treatment = text
                            "prevention" -> entry.prevention = text
                            "severity" -> entry.severity = text
                        }
                    }
                }

                XmlPullParser.END_TAG -> {
                    if ("disease" == parser.name && current != null) {
                        diseases.add(current)
                        current = null
                    }
                    tagName = null
                }
            }
            eventType = parser.next()
        }

        return diseases
    }

    /**
     * Hardcoded fallback used when diseases.xml is not yet bundled in assets.
     * Students replace this with a real XML file as part of the build task.
     */
    private fun getFallbackDiseaseList(): List<DiseaseEntry> {
        val data = arrayOf(
            arrayOf(
                "Tomato Early Blight", "Tomato", "Brown lesions with concentric rings on older leaves.",
                "Remove affected leaves; apply copper-based fungicide.",
                "Rotate crops, avoid overhead watering, use resistant varieties.", "medium"
            ),
            arrayOf(
                "Tomato Late Blight", "Tomato", "Water-soaked lesions with white mold on leaf undersides.",
                "Apply chlorothalonil or mancozeb fungicide immediately.",
                "Plant certified disease-free seeds, ensure good air circulation.", "high"
            ),
            arrayOf(
                "Apple Scab", "Apple", "Olive-green spots on leaves, turning dark brown.",
                "Apply fungicide during early bud break.",
                "Rake and destroy fallen leaves, plant resistant cultivars.", "high"
            ),
            arrayOf(
                "Potato Early Blight", "Potato", "Small brown spots with yellow halos on lower leaves.",
                "Use approved fungicide; remove heavily infected plants.",
                "Avoid high nitrogen fertilisation, ensure adequate potassium.", "medium"
            ),
            arrayOf(
                "Corn Northern Leaf Blight", "Corn", "Long grey-green lesions parallel to leaf veins.",
                "Apply foliar fungicides at first sign of infection.",
                "Plant resistant hybrids, avoid continuous corn cultivation.", "medium"
            )
        )

        return data.map { row ->
            DiseaseEntry(
                name = row[0],
                plant = row[1],
                symptoms = row[2],
                treatment = row[3],
                prevention = row[4],
                severity = row[5]
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    // ── Data model ──────────────────────────────────────────────────────

    data class DiseaseEntry(
        var name: String = "",
        var plant: String = "",
        var symptoms: String = "",
        var treatment: String = "",
        var prevention: String = "",
        var severity: String = "medium"
    )

    // ── Adapter ─────────────────────────────────────────────────────────

    private class DiseaseAdapter(
        private val items: MutableList<DiseaseEntry>
    ) : RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder>() {

        fun submitList(newItems: List<DiseaseEntry>) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_disease_library, parent, false)
            return DiseaseViewHolder(view)
        }

        override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        class DiseaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val textName: TextView = itemView.findViewById(R.id.textDiseaseItemName)
            private val textPlant: TextView = itemView.findViewById(R.id.textDiseaseItemPlant)
            private val textSymptoms: TextView = itemView.findViewById(R.id.textDiseaseItemSymptoms)
            private val chipSeverity: Chip = itemView.findViewById(R.id.chipSeverity)

            fun bind(entry: DiseaseEntry) {
                textName.text = entry.name
                textPlant.text = entry.plant
                textSymptoms.text = entry.symptoms
                chipSeverity.text = entry.severity

                val context = itemView.context
                val backgroundRes = when (entry.severity.lowercase()) {
                    "high" -> R.color.leaf_green_500
                    "low" -> R.color.leaf_green_100
                    else -> R.color.leaf_green_300
                }
                chipSeverity.chipBackgroundColor =
                    android.content.res.ColorStateList.valueOf(context.getColor(backgroundRes))
            }
        }
    }
}
