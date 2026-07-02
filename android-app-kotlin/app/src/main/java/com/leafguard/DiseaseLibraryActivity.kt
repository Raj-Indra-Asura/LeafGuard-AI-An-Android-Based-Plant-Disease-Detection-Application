package com.leafguard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leafguard.databinding.ActivityDiseaseLibraryBinding
import java.io.IOException
import java.io.InputStream
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

/**
 * Kotlin twin of DiseaseLibraryActivity.java.
 *
 * Displays the full plant disease reference library by parsing
 * the bundled diseases.xml file with XmlPullParser.
 *
 * Architecture role: read-only reference screen, no database access needed.
 * XML source: assets/diseases.xml (or res/xml/diseases.xml if present)
 */
class DiseaseLibraryActivity : AppCompatActivity() {

    private var binding: ActivityDiseaseLibraryBinding? = null
    private lateinit var adapter: DiseaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDiseaseLibraryBinding.inflate(layoutInflater)
        this.binding = binding
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener { finish() }

        adapter = DiseaseAdapter(mutableListOf())
        binding.recyclerDiseaseLibrary.layoutManager = LinearLayoutManager(this)
        binding.recyclerDiseaseLibrary.adapter = adapter

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
     *   </disease>
     * </diseases>
     */
    private fun loadDiseases() {
        val binding = binding ?: return
        val diseases: List<DiseaseEntry> = try {
            val inputStream = assets.open("diseases.xml")
            parseDiseaseXml(inputStream)
        } catch (e: IOException) {
            // assets/diseases.xml not present or malformed — load built-in fallback data
            getFallbackDiseaseList()
        } catch (e: XmlPullParserException) {
            getFallbackDiseaseList()
        }

        val hasItems = diseases.isNotEmpty()
        adapter.submitList(diseases)
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

        inputStream.close()
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
                "Rotate crops, avoid overhead watering, use resistant varieties."
            ),
            arrayOf(
                "Tomato Late Blight", "Tomato", "Water-soaked lesions with white mold on leaf undersides.",
                "Apply chlorothalonil or mancozeb fungicide immediately.",
                "Plant certified disease-free seeds, ensure good air circulation."
            ),
            arrayOf(
                "Apple Scab", "Apple", "Olive-green spots on leaves, turning dark brown.",
                "Apply fungicide during early bud break.",
                "Rake and destroy fallen leaves, plant resistant cultivars."
            ),
            arrayOf(
                "Potato Early Blight", "Potato", "Small brown spots with yellow halos on lower leaves.",
                "Use approved fungicide; remove heavily infected plants.",
                "Avoid high nitrogen fertilisation, ensure adequate potassium."
            ),
            arrayOf(
                "Corn Northern Leaf Blight", "Corn", "Long grey-green lesions parallel to leaf veins.",
                "Apply foliar fungicides at first sign of infection.",
                "Plant resistant hybrids, avoid continuous corn cultivation."
            )
        )

        return data.map { row ->
            DiseaseEntry(
                name = row[0],
                plant = row[1],
                symptoms = row[2],
                treatment = row[3],
                prevention = row[4]
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
        var prevention: String = ""
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

            fun bind(entry: DiseaseEntry) {
                textName.text = entry.name
                textPlant.text = entry.plant
                textSymptoms.text = entry.symptoms
            }
        }
    }
}
