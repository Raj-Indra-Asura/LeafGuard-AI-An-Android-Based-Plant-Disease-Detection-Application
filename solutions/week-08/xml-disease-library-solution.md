# Week 08 Solution - XML Disease Library, Parser, Repository, and RecyclerView

This solution completes the Week 08 XML disease library exercise set for LeafGuard AI.

It includes:
- a full `disease_library.xml` with 15 diseases
- `Disease.java`
- `DiseaseXmlParser.java` with `Map<String, Disease>` caching
- `DiseaseRepository.java` singleton
- `DiseaseLibraryActivity.java`
- `DiseaseAdapter.java`
- integration steps for `ResultActivity`

---

## 1. `app/src/main/assets/disease_library.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<diseases>
    <disease>
        <label>Tomato___Early_blight</label>
        <commonName>Tomato Early Blight</commonName>
        <symptoms>Brown concentric lesions on older leaves, yellow halos, and progressive leaf drop.</symptoms>
        <treatment>Remove infected leaves, apply copper or chlorothalonil fungicide, and improve airflow.</treatment>
        <prevention>Rotate crops, avoid overhead irrigation, and mulch soil to reduce splash spread.</prevention>
    </disease>
    <disease>
        <label>Tomato___Late_blight</label>
        <commonName>Tomato Late Blight</commonName>
        <symptoms>Dark water-soaked patches that expand rapidly with white mold in humid weather.</symptoms>
        <treatment>Remove infected tissue immediately and spray a recommended late blight fungicide.</treatment>
        <prevention>Use healthy seedlings, keep foliage dry, and remove volunteer plants.</prevention>
    </disease>
    <disease>
        <label>Tomato___Leaf_Mold</label>
        <commonName>Tomato Leaf Mold</commonName>
        <symptoms>Yellow spots on upper leaf surface and olive-green mold on the underside.</symptoms>
        <treatment>Prune crowded foliage, improve ventilation, and use an approved fungicide.</treatment>
        <prevention>Reduce humidity in covered growing areas and avoid dense planting.</prevention>
    </disease>
    <disease>
        <label>Tomato___Septoria_leaf_spot</label>
        <commonName>Tomato Septoria Leaf Spot</commonName>
        <symptoms>Numerous small gray spots with dark borders starting on lower leaves.</symptoms>
        <treatment>Remove infected leaves and use a broad-spectrum fungicide if disease pressure increases.</treatment>
        <prevention>Water at the base, clean tools, and rotate away from tomato or potato crops.</prevention>
    </disease>
    <disease>
        <label>Tomato___Spider_mites Two-spotted_spider_mite</label>
        <commonName>Tomato Spider Mites</commonName>
        <symptoms>Fine webbing, pale speckling, leaf bronzing, and dry curled foliage.</symptoms>
        <treatment>Spray water under leaves, release beneficial mites, or apply insecticidal soap.</treatment>
        <prevention>Monitor leaf undersides regularly and avoid hot, dusty plant conditions.</prevention>
    </disease>
    <disease>
        <label>Tomato___Target_Spot</label>
        <commonName>Tomato Target Spot</commonName>
        <symptoms>Brown circular spots with concentric rings and yellowing around lesions.</symptoms>
        <treatment>Prune infected areas and apply a labeled fungicide during wet weather.</treatment>
        <prevention>Use crop rotation, proper spacing, and sanitation after harvest.</prevention>
    </disease>
    <disease>
        <label>Tomato___Tomato_Yellow_Leaf_Curl_Virus</label>
        <commonName>Tomato Yellow Leaf Curl Virus</commonName>
        <symptoms>Upward leaf curling, yellowing, stunted growth, and reduced fruit set.</symptoms>
        <treatment>Remove severely infected plants and control whiteflies aggressively.</treatment>
        <prevention>Use insect netting, reflective mulches, and resistant cultivars where available.</prevention>
    </disease>
    <disease>
        <label>Tomato___Tomato_mosaic_virus</label>
        <commonName>Tomato Mosaic Virus</commonName>
        <symptoms>Mottled leaves, mosaic patterns, leaf distortion, and uneven fruit ripening.</symptoms>
        <treatment>Remove infected plants because viral infections cannot be cured directly.</treatment>
        <prevention>Disinfect hands and tools, use certified seed, and avoid tobacco contamination.</prevention>
    </disease>
    <disease>
        <label>Tomato___healthy</label>
        <commonName>Tomato Healthy</commonName>
        <symptoms>Leaves are uniformly green, firm, and free of spots or curling.</symptoms>
        <treatment>No treatment required beyond normal watering and nutrition.</treatment>
        <prevention>Continue routine scouting and balanced plant care.</prevention>
    </disease>
    <disease>
        <label>Potato___Early_blight</label>
        <commonName>Potato Early Blight</commonName>
        <symptoms>Target-like brown spots on older foliage and yellowing around lesions.</symptoms>
        <treatment>Remove infected leaves and apply fungicide if the disease continues spreading.</treatment>
        <prevention>Rotate crops, maintain spacing, and avoid prolonged leaf wetness.</prevention>
    </disease>
    <disease>
        <label>Potato___Late_blight</label>
        <commonName>Potato Late Blight</commonName>
        <symptoms>Dark blotches expand quickly on leaves and stems, often with white growth.</symptoms>
        <treatment>Destroy infected foliage quickly and protect nearby healthy plants.</treatment>
        <prevention>Use certified seed potatoes and monitor weather-driven outbreak risk.</prevention>
    </disease>
    <disease>
        <label>Potato___healthy</label>
        <commonName>Potato Healthy</commonName>
        <symptoms>Leaf surfaces look green, stable, and free from lesions or decay.</symptoms>
        <treatment>No treatment needed.</treatment>
        <prevention>Keep practicing healthy irrigation and nutrient management.</prevention>
    </disease>
    <disease>
        <label>Apple___Apple_scab</label>
        <commonName>Apple Scab</commonName>
        <symptoms>Olive-brown velvety lesions on leaves and dark rough patches on fruit.</symptoms>
        <treatment>Remove infected leaves and apply protectant fungicide during high-risk periods.</treatment>
        <prevention>Prune for airflow and clean fallen leaf litter thoroughly.</prevention>
    </disease>
    <disease>
        <label>Apple___Black_rot</label>
        <commonName>Apple Black Rot</commonName>
        <symptoms>Purple leaf spots, cankers on branches, and rotting fruit with dark rings.</symptoms>
        <treatment>Prune dead wood and remove infected fruit from the tree and ground.</treatment>
        <prevention>Sanitize orchard debris and avoid leaving mummified fruit behind.</prevention>
    </disease>
    <disease>
        <label>Apple___Cedar_apple_rust</label>
        <commonName>Apple Cedar Rust</commonName>
        <symptoms>Bright orange leaf spots and small tubular growths on the underside of leaves.</symptoms>
        <treatment>Apply fungicide at the correct growth stage and prune nearby host plants if possible.</treatment>
        <prevention>Avoid planting susceptible apples near infected juniper hosts.</prevention>
    </disease>
</diseases>
```

---

## 2. `Disease.java`

```java
package com.leafguard.data.model;

public class Disease {

    private String label;
    private String commonName;
    private String symptoms;
    private String treatment;
    private String prevention;

    public Disease() {
    }

    public Disease(String label, String commonName, String symptoms, String treatment, String prevention) {
        this.label = label;
        this.commonName = commonName;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.prevention = prevention;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getPrevention() {
        return prevention;
    }

    public void setPrevention(String prevention) {
        this.prevention = prevention;
    }
}
```

---

## 3. `DiseaseXmlParser.java`

```java
package com.leafguard.parser;

import android.util.Xml;

import com.leafguard.data.model.Disease;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DiseaseXmlParser {

    private final Map<String, Disease> cache = new LinkedHashMap<>();

    public synchronized List<Disease> parse(InputStream inputStream) throws Exception {
        cache.clear();

        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);

        Disease currentDisease = null;
        String currentTag = null;
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    currentTag = parser.getName();
                    if ("disease".equals(currentTag)) {
                        currentDisease = new Disease();
                    }
                    break;
                case XmlPullParser.TEXT:
                    if (currentDisease != null && currentTag != null) {
                        String value = parser.getText() == null ? "" : parser.getText().trim();
                        if (!value.isEmpty()) {
                            assignField(currentDisease, currentTag, value);
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("disease".equals(parser.getName()) && currentDisease != null) {
                        if (currentDisease.getLabel() != null) {
                            cache.put(normalizeLabel(currentDisease.getLabel()), currentDisease);
                        }
                        currentDisease = null;
                    }
                    currentTag = null;
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }

        return new ArrayList<>(cache.values());
    }

    private void assignField(Disease disease, String tagName, String value) {
        switch (tagName) {
            case "label":
                disease.setLabel(value);
                break;
            case "commonName":
                disease.setCommonName(value);
                break;
            case "symptoms":
                disease.setSymptoms(value);
                break;
            case "treatment":
                disease.setTreatment(value);
                break;
            case "prevention":
                disease.setPrevention(value);
                break;
            default:
                break;
        }
    }

    public synchronized Disease findByLabel(String label) {
        return cache.get(normalizeLabel(label));
    }

    public synchronized Map<String, Disease> getCacheSnapshot() {
        return new LinkedHashMap<>(cache);
    }

    private String normalizeLabel(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.US);
    }
}
```

---

## 4. `DiseaseRepository.java`

```java
package com.leafguard.data.repository;

import android.content.Context;

import com.leafguard.data.model.Disease;
import com.leafguard.parser.DiseaseXmlParser;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DiseaseRepository {

    private static final String XML_FILE_NAME = "disease_library.xml";
    private static DiseaseRepository instance;

    private final Context appContext;
    private final DiseaseXmlParser parser;
    private List<Disease> cachedDiseases = Collections.emptyList();

    private DiseaseRepository(Context context) {
        this.appContext = context.getApplicationContext();
        this.parser = new DiseaseXmlParser();
    }

    public static synchronized DiseaseRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DiseaseRepository(context);
        }
        return instance;
    }

    public synchronized List<Disease> getAllDiseases() throws Exception {
        if (!cachedDiseases.isEmpty()) {
            return cachedDiseases;
        }
        try (InputStream inputStream = appContext.getAssets().open(XML_FILE_NAME)) {
            cachedDiseases = parser.parse(inputStream);
            return cachedDiseases;
        }
    }

    public synchronized Disease findByLabel(String label) throws Exception {
        if (cachedDiseases.isEmpty()) {
            getAllDiseases();
        }
        return parser.findByLabel(label);
    }

    public synchronized Map<String, Disease> getCacheSnapshot() {
        return parser.getCacheSnapshot();
    }
}
```

---

## 5. `DiseaseAdapter.java`

```java
package com.leafguard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leafguard.R;
import com.leafguard.data.model.Disease;

import java.util.ArrayList;
import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {

    public interface OnDiseaseClickListener {
        void onDiseaseClicked(Disease disease);
    }

    private final List<Disease> diseases = new ArrayList<>();
    private final OnDiseaseClickListener listener;

    public DiseaseAdapter(OnDiseaseClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Disease> items) {
        diseases.clear();
        diseases.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_disease, parent, false);
        return new DiseaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        holder.bind(diseases.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return diseases.size();
    }

    static class DiseaseViewHolder extends RecyclerView.ViewHolder {

        private final TextView textCommonName;
        private final TextView textLabel;
        private final TextView textSymptomsPreview;

        DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);
            textCommonName = itemView.findViewById(R.id.textDiseaseCommonName);
            textLabel = itemView.findViewById(R.id.textDiseaseLabel);
            textSymptomsPreview = itemView.findViewById(R.id.textDiseaseSymptomsPreview);
        }

        void bind(Disease disease, OnDiseaseClickListener listener) {
            textCommonName.setText(disease.getCommonName());
            textLabel.setText(disease.getLabel());
            textSymptomsPreview.setText(disease.getSymptoms());
            itemView.setOnClickListener(view -> listener.onDiseaseClicked(disease));
        }
    }
}
```

---

## 6. `DiseaseLibraryActivity.java`

```java
package com.leafguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leafguard.adapter.DiseaseAdapter;
import com.leafguard.data.model.Disease;
import com.leafguard.data.repository.DiseaseRepository;
import com.leafguard.databinding.ActivityDiseaseLibraryBinding;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiseaseLibraryActivity extends AppCompatActivity {

    public static final String EXTRA_DISEASE_LABEL = "extra_disease_label";

    private ActivityDiseaseLibraryBinding binding;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private DiseaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiseaseLibraryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.topAppBar.setNavigationOnClickListener(view -> finish());
        setupRecyclerView();
        loadDiseases();
    }

    private void setupRecyclerView() {
        adapter = new DiseaseAdapter(this::openDiseaseDetail);
        binding.recyclerDiseases.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerDiseases.setAdapter(adapter);
    }

    private void loadDiseases() {
        executorService.execute(() -> {
            try {
                List<Disease> diseases = DiseaseRepository.getInstance(getApplicationContext()).getAllDiseases();
                runOnUiThread(() -> {
                    adapter.submitList(diseases);
                    binding.textEmptyState.setVisibility(diseases.isEmpty() ? android.view.View.VISIBLE : android.view.View.GONE);
                });
            } catch (Exception exception) {
                runOnUiThread(() -> Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private void openDiseaseDetail(Disease disease) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(EXTRA_DISEASE_LABEL, disease.getLabel());
        intent.putExtra(ResultActivity.EXTRA_DISEASE_NAME, disease.getCommonName());
        intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, 1.0f);
        intent.putExtra(ResultActivity.EXTRA_SYMPTOMS, disease.getSymptoms());
        intent.putExtra(ResultActivity.EXTRA_TREATMENT, disease.getTreatment());
        intent.putExtra(ResultActivity.EXTRA_PREVENTION, disease.getPrevention());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
        binding = null;
    }
}
```

---

## 7. Layout note

Use a standard `RecyclerView` screen with a `MaterialToolbar` and a simple `item_disease.xml` row containing common name, label, and symptoms preview. The Java classes above are the main Week 08 deliverables; the layouts can follow the same MaterialCardView pattern already used elsewhere in the project.

---

## 9. Integration with `ResultActivity`

Add this helper to `ResultActivity.java` if you want the result screen to enrich prediction output from XML when only the model label is known.

```java
private void enrichResultFromLibraryIfNeeded() {
    String rawLabel = getIntent().getStringExtra("extra_model_label");
    if (rawLabel == null || !symptoms.equals(getString(R.string.placeholder_symptoms))) {
        return;
    }

    try {
        Disease disease = DiseaseRepository.getInstance(getApplicationContext()).findByLabel(rawLabel);
        if (disease != null) {
            diseaseName = disease.getCommonName();
            symptoms = disease.getSymptoms();
            treatment = disease.getTreatment();
            prevention = disease.getPrevention();
        }
    } catch (Exception ignored) {
    }
}
```

Call it after `readIntentExtras()` and before `renderResult()`.

---

## 10. Week 08 checklist

- [x] XML file with 15 diseases included
- [x] `Disease.java` included
- [x] `DiseaseXmlParser.java` included
- [x] cache implemented with `Map<String, Disease>`
- [x] singleton repository included
- [x] RecyclerView adapter included
- [x] `DiseaseLibraryActivity.java` included
- [x] `ResultActivity` integration included


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

- 📝 [Back to Week 08 Exercises](../../roadmap/week-08-xml-disease-library/exercises.md) — Try it yourself first
- 📖 [Week 08 README](../../roadmap/week-08-xml-disease-library/README.md) — Week overview
- 💡 [Solutions Index for Week 08](README.md) — Other solutions this week
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

---
