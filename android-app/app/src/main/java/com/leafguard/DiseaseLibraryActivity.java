package com.leafguard;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.leafguard.databinding.ActivityDiseaseLibraryBinding;
import com.leafguard.ui.BottomNav;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * DiseaseLibraryActivity
 *
 * Displays the full plant disease reference library by parsing
 * the bundled diseases.xml file with XmlPullParser, with a search box to
 * filter by name/plant and a severity chip per entry.
 *
 * Architecture role: read-only reference screen, no database access needed.
 * XML source: assets/diseases.xml (or res/xml/diseases.xml if present)
 */
public class DiseaseLibraryActivity extends AppCompatActivity {

    /**
     * Number of diseases shown on the Home dashboard's "Library" card
     * when assets/diseases.xml is not bundled. Keep this in sync with
     * {@link #getFallbackDiseaseList()} below.
     */
    public static final int FALLBACK_DISEASE_COUNT = 5;

    private ActivityDiseaseLibraryBinding binding;
    private DiseaseAdapter adapter;
    private List<DiseaseEntry> allDiseases = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiseaseLibraryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNav.setup(this, binding.bottomNavigation, R.id.nav_library);

        adapter = new DiseaseAdapter(new ArrayList<>());
        binding.recyclerDiseaseLibrary.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerDiseaseLibrary.setAdapter(adapter);

        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterDiseases(s == null ? "" : s.toString());
            }
        });

        loadDiseases();
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
    private void loadDiseases() {
        List<DiseaseEntry> diseases;

        try {
            InputStream inputStream = getAssets().open("diseases.xml");
            diseases = parseDiseaseXml(inputStream);
        } catch (IOException | XmlPullParserException e) {
            // assets/diseases.xml not present or malformed — load built-in fallback data
            diseases = getFallbackDiseaseList();
        }

        allDiseases = diseases;
        filterDiseases(binding.editTextSearch.getText() == null ? "" : binding.editTextSearch.getText().toString());
    }

    /** Filters {@link #allDiseases} by disease name or plant name (case-insensitive) and re-renders the list. */
    private void filterDiseases(String query) {
        List<DiseaseEntry> filtered;
        if (query == null || query.trim().isEmpty()) {
            filtered = allDiseases;
        } else {
            filtered = new ArrayList<>();
            String lowerQuery = query.toLowerCase();
            for (DiseaseEntry entry : allDiseases) {
                if (entry.name.toLowerCase().contains(lowerQuery) || entry.plant.toLowerCase().contains(lowerQuery)) {
                    filtered.add(entry);
                }
            }
        }

        boolean hasItems = !filtered.isEmpty();
        adapter.submitList(filtered);
        binding.recyclerDiseaseLibrary.setVisibility(hasItems ? View.VISIBLE : View.GONE);
        binding.textEmptyLibrary.setVisibility(hasItems ? View.GONE : View.VISIBLE);
    }

    private List<DiseaseEntry> parseDiseaseXml(InputStream inputStream)
            throws XmlPullParserException, IOException {

        List<DiseaseEntry> diseases = new ArrayList<>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inputStream, "UTF-8");

        DiseaseEntry current = null;
        String tagName = null;
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    tagName = parser.getName();
                    if ("disease".equals(tagName)) {
                        current = new DiseaseEntry();
                    }
                    break;

                case XmlPullParser.TEXT:
                    if (current != null && tagName != null) {
                        String text = parser.getText().trim();
                        switch (tagName) {
                            case "name":       current.name       = text; break;
                            case "plant":      current.plant      = text; break;
                            case "symptoms":   current.symptoms   = text; break;
                            case "treatment":  current.treatment  = text; break;
                            case "prevention": current.prevention = text; break;
                            case "severity":   current.severity   = text; break;
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("disease".equals(parser.getName()) && current != null) {
                        diseases.add(current);
                        current = null;
                    }
                    tagName = null;
                    break;
            }
            eventType = parser.next();
        }

        inputStream.close();
        return diseases;
    }

    /**
     * Hardcoded fallback used when diseases.xml is not yet bundled in assets.
     * Students replace this with a real XML file as part of the build task.
     */
    private List<DiseaseEntry> getFallbackDiseaseList() {
        List<DiseaseEntry> list = new ArrayList<>();

        String[][] data = {
            {"Tomato Early Blight",   "Tomato",  "Brown lesions with concentric rings on older leaves.",
             "Remove affected leaves; apply copper-based fungicide.",
             "Rotate crops, avoid overhead watering, use resistant varieties.", "medium"},
            {"Tomato Late Blight",    "Tomato",  "Water-soaked lesions with white mold on leaf undersides.",
             "Apply chlorothalonil or mancozeb fungicide immediately.",
             "Plant certified disease-free seeds, ensure good air circulation.", "high"},
            {"Apple Scab",            "Apple",   "Olive-green spots on leaves, turning dark brown.",
             "Apply fungicide during early bud break.",
             "Rake and destroy fallen leaves, plant resistant cultivars.", "high"},
            {"Potato Early Blight",   "Potato",  "Small brown spots with yellow halos on lower leaves.",
             "Use approved fungicide; remove heavily infected plants.",
             "Avoid high nitrogen fertilisation, ensure adequate potassium.", "medium"},
            {"Corn Northern Leaf Blight", "Corn","Long grey-green lesions parallel to leaf veins.",
             "Apply foliar fungicides at first sign of infection.",
             "Plant resistant hybrids, avoid continuous corn cultivation.", "medium"},
        };

        for (String[] row : data) {
            DiseaseEntry entry = new DiseaseEntry();
            entry.name       = row[0];
            entry.plant      = row[1];
            entry.symptoms   = row[2];
            entry.treatment  = row[3];
            entry.prevention = row[4];
            entry.severity   = row[5];
            list.add(entry);
        }
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    // ── Data model ──────────────────────────────────────────────────────

    static class DiseaseEntry {
        String name       = "";
        String plant      = "";
        String symptoms   = "";
        String treatment  = "";
        String prevention = "";
        String severity   = "medium";
    }

    // ── Adapter ─────────────────────────────────────────────────────────

    private static class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {

        private final List<DiseaseEntry> items;

        DiseaseAdapter(List<DiseaseEntry> items) {
            this.items = items;
        }

        void submitList(List<DiseaseEntry> newItems) {
            items.clear();
            items.addAll(newItems);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_disease_library, parent, false);
            return new DiseaseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class DiseaseViewHolder extends RecyclerView.ViewHolder {

            private final TextView textName;
            private final TextView textPlant;
            private final TextView textSymptoms;
            private final Chip chipSeverity;

            DiseaseViewHolder(@NonNull View itemView) {
                super(itemView);
                textName     = itemView.findViewById(R.id.textDiseaseItemName);
                textPlant    = itemView.findViewById(R.id.textDiseaseItemPlant);
                textSymptoms = itemView.findViewById(R.id.textDiseaseItemSymptoms);
                chipSeverity = itemView.findViewById(R.id.chipSeverity);
            }

            void bind(DiseaseEntry entry) {
                textName.setText(entry.name);
                textPlant.setText(entry.plant);
                textSymptoms.setText(entry.symptoms);
                chipSeverity.setText(entry.severity);

                int backgroundRes;
                switch (entry.severity.toLowerCase()) {
                    case "high":
                        backgroundRes = R.color.leaf_green_500;
                        break;
                    case "low":
                        backgroundRes = R.color.leaf_green_100;
                        break;
                    default:
                        backgroundRes = R.color.leaf_green_300;
                        break;
                }
                chipSeverity.setChipBackgroundColor(
                        ColorStateList.valueOf(itemView.getContext().getColor(backgroundRes)));
            }
        }
    }
}

