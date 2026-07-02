# Week 08 Build Task - Implement the XML Disease Library

> **Accuracy note (read first):** The **shipped** file is `app/src/main/assets/diseases.xml` (folder `assets/`, filename `diseases.xml`), parsed by `DiseaseLibraryActivity` using `XmlPullParser`. It contains **10** `<disease>` entries with five tags each — `<name>`, `<plant>`, `<symptoms>`, `<treatment>`, `<prevention>` — and every `<name>` matches a line in `assets/labels.txt` exactly (spaces, e.g. `Tomato Late Blight`, not `Tomato___Late_blight`). The bigger PlantVillage schema (with `label`/`image`/`severity` attributes) shown below is an **optional extension**. **Kotlin is the primary track**; Java snippets here are the secondary reference (`XmlPullParser` code is nearly identical in both).

## Week 08 Context

This build task is the implementation side of the Week 08 roadmap.
You are no longer just reading about XML parsing.
You are building the actual **LeafGuard AI Disease Library** in Java.

The final result should allow the user to:

1. browse a list of diseases in `DiseaseLibraryActivity`
2. tap a disease to see full details
3. receive disease information automatically in `ResultActivity` after ML prediction
4. keep all disease information available offline from `assets/diseases.xml`

This task directly supports the CSE 2206 requirement for **XML parsing in Android using Java**.

---

## What You Will Build

By the end of this build task, your app should have:

- `diseases.xml` in the `assets/` folder
- `Disease.java` model class
- `DiseaseXmlParser.java` parser using `XmlPullParser`
- `DiseaseRepository.java` singleton with cache and background loading
- `DiseaseAdapter.java` for RecyclerView
- `DiseaseLibraryActivity.java` complete list screen
- `DiseaseDetailActivity.java` complete detail screen
- `ResultActivity` integration that looks up disease details after prediction
- proper fallback handling for missing labels and missing images

---

## Recommended Project Structure

```text
app/src/main/
├── assets/
│   └── diseases.xml
├── java/com/leafguardai/
│   ├── model/
│   │   └── Disease.java
│   ├── parser/
│   │   └── DiseaseXmlParser.java
│   ├── repository/
│   │   └── DiseaseRepository.java
│   └── ui/
│       ├── DiseaseLibraryActivity.java
│       ├── DiseaseDetailActivity.java
│       └── adapter/
│           └── DiseaseAdapter.java
└── res/layout/
    ├── activity_disease_library.xml
    ├── activity_disease_detail.xml
    └── item_disease.xml
```

If your package names differ, adapt the imports accordingly.

---

## Step 1 - Create the XML file in `assets/`

Create this file:

```text
app/src/main/assets/diseases.xml
```

Use the following starter content with **12 complete entries**.
This is large enough to prove your parser works beyond a toy example.
Later you can scale it to all 38 PlantVillage classes without changing the parser structure.

```xml
<?xml version="1.0" encoding="utf-8"?>
<diseaseLibrary version="1.0" source="PlantVillage" totalClasses="38">
    <disease label="Tomato___Late_blight" image="tomato_late_blight" severity="high">
        <commonName>Tomato Late Blight</commonName>
        <scientificName>Phytophthora infestans</scientificName>
        <plant>Tomato</plant>
        <category>Oomycete disease</category>
        <summary>Rapidly spreading blight that damages leaves, stems, and fruit in cool humid conditions.</summary>
        <symptoms>
            <item>Dark water-soaked lesions on leaves</item>
            <item>White growth on leaf undersides in moist weather</item>
            <item>Brown lesions on stems and fruit rot</item>
        </symptoms>
        <treatment>
            <item>Remove badly infected leaves immediately</item>
            <item>Improve spacing and airflow around plants</item>
            <item>Use recommended fungicide support if locally approved</item>
        </treatment>
        <prevention>
            <item>Avoid overhead watering</item>
            <item>Use disease-free seedlings and clean tools</item>
            <item>Rotate tomato planting areas each season</item>
        </prevention>
    </disease>
    <disease label="Tomato___Early_blight" image="tomato_early_blight" severity="medium">
        <commonName>Tomato Early Blight</commonName>
        <scientificName>Alternaria solani</scientificName>
        <plant>Tomato</plant>
        <category>Fungal disease</category>
        <summary>Common tomato disease that begins on older leaves and forms ringed brown lesions.</summary>
        <symptoms>
            <item>Brown spots with concentric rings</item>
            <item>Yellowing around lesions on lower leaves</item>
            <item>Premature leaf drop in severe cases</item>
        </symptoms>
        <treatment>
            <item>Remove affected lower leaves</item>
            <item>Improve field sanitation and airflow</item>
            <item>Apply approved disease management spray if needed</item>
        </treatment>
        <prevention>
            <item>Mulch soil to reduce splash spread</item>
            <item>Rotate crops and avoid continuous tomato planting</item>
            <item>Water at soil level instead of wetting leaves</item>
        </prevention>
    </disease>
    <disease label="Tomato___healthy" image="tomato_healthy" severity="none">
        <commonName>Healthy Tomato Leaf</commonName>
        <scientificName>Not applicable</scientificName>
        <plant>Tomato</plant>
        <category>Healthy</category>
        <summary>Leaf shows no strong visible signs of common tomato disease classes.</summary>
        <symptoms>
            <item>Uniform healthy green color</item>
            <item>No major spotting or mold pattern</item>
            <item>No severe curling or tissue collapse</item>
        </symptoms>
        <treatment>
            <item>No treatment required</item>
            <item>Continue balanced watering and nutrition</item>
            <item>Keep monitoring for new symptoms</item>
        </treatment>
        <prevention>
            <item>Inspect leaves regularly</item>
            <item>Maintain good spacing and airflow</item>
            <item>Sanitize tools before pruning</item>
        </prevention>
    </disease>
    <disease label="Potato___Early_blight" image="potato_early_blight" severity="medium">
        <commonName>Potato Early Blight</commonName>
        <scientificName>Alternaria solani</scientificName>
        <plant>Potato</plant>
        <category>Fungal disease</category>
        <summary>Leaf spot disease that starts on older potato leaves and can reduce plant vigor.</summary>
        <symptoms>
            <item>Brown circular lesions with target-like rings</item>
            <item>Yellow tissue around spots</item>
            <item>Older leaves dry out first</item>
        </symptoms>
        <treatment>
            <item>Remove heavily affected leaves</item>
            <item>Avoid keeping foliage wet for long periods</item>
            <item>Use recommended fungicide support when necessary</item>
        </treatment>
        <prevention>
            <item>Rotate crops away from potato and tomato</item>
            <item>Clear infected plant debris after harvest</item>
            <item>Support airflow between plants</item>
        </prevention>
    </disease>
    <disease label="Potato___Late_blight" image="potato_late_blight" severity="high">
        <commonName>Potato Late Blight</commonName>
        <scientificName>Phytophthora infestans</scientificName>
        <plant>Potato</plant>
        <category>Oomycete disease</category>
        <summary>Aggressive blight that can destroy foliage quickly and may also infect tubers.</summary>
        <symptoms>
            <item>Large dark irregular lesions on leaves</item>
            <item>White growth near lesions in humid weather</item>
            <item>Rapid collapse of foliage during severe infection</item>
        </symptoms>
        <treatment>
            <item>Remove infected plant parts quickly</item>
            <item>Avoid wet foliage and improve ventilation</item>
            <item>Seek approved blight control measures promptly</item>
        </treatment>
        <prevention>
            <item>Use clean seed tubers</item>
            <item>Do not overcrowd plants</item>
            <item>Monitor closely during cool wet periods</item>
        </prevention>
    </disease>
    <disease label="Potato___healthy" image="potato_healthy" severity="none">
        <commonName>Healthy Potato Leaf</commonName>
        <scientificName>Not applicable</scientificName>
        <plant>Potato</plant>
        <category>Healthy</category>
        <summary>The potato leaf appears healthy with no major visible disease symptoms.</summary>
        <symptoms>
            <item>Healthy green leaf surface</item>
            <item>No strong lesion clusters</item>
            <item>No clear blight or mold signs</item>
        </symptoms>
        <treatment>
            <item>No treatment needed</item>
            <item>Continue regular crop care</item>
            <item>Monitor field conditions routinely</item>
        </treatment>
        <prevention>
            <item>Use clean planting material</item>
            <item>Practice crop rotation</item>
            <item>Keep weeds and debris controlled</item>
        </prevention>
    </disease>
    <disease label="Pepper,_bell___Bacterial_spot" image="pepper_bacterial_spot" severity="high">
        <commonName>Bell Pepper Bacterial Spot</commonName>
        <scientificName>Xanthomonas campestris pv. vesicatoria</scientificName>
        <plant>Bell Pepper</plant>
        <category>Bacterial disease</category>
        <summary>Bacterial disease that causes dark spots on leaves and fruit, especially in wet conditions.</summary>
        <symptoms>
            <item>Small dark water-soaked leaf lesions</item>
            <item>Yellow halos may form around spots</item>
            <item>Fruit spots may become rough and cracked</item>
        </symptoms>
        <treatment>
            <item>Remove badly infected leaves and fruit</item>
            <item>Avoid handling wet plants</item>
            <item>Use approved bactericidal management when advised</item>
        </treatment>
        <prevention>
            <item>Start with clean seeds or seedlings</item>
            <item>Disinfect tools and hands</item>
            <item>Reduce splash spread from irrigation and rain</item>
        </prevention>
    </disease>
    <disease label="Pepper,_bell___healthy" image="pepper_healthy" severity="none">
        <commonName>Healthy Bell Pepper Leaf</commonName>
        <scientificName>Not applicable</scientificName>
        <plant>Bell Pepper</plant>
        <category>Healthy</category>
        <summary>The bell pepper leaf appears healthy and free of major disease indicators.</summary>
        <symptoms>
            <item>Green surface with normal texture</item>
            <item>No expanding lesion pattern</item>
            <item>No severe yellow halos or cracking</item>
        </symptoms>
        <treatment>
            <item>No treatment required</item>
            <item>Maintain watering and nutrient balance</item>
            <item>Continue observation</item>
        </treatment>
        <prevention>
            <item>Check leaves weekly</item>
            <item>Avoid unnecessary leaf wetness</item>
            <item>Keep field hygiene strong</item>
        </prevention>
    </disease>
    <disease label="Corn_(maize)___Common_rust_" image="corn_common_rust" severity="medium">
        <commonName>Corn Common Rust</commonName>
        <scientificName>Puccinia sorghi</scientificName>
        <plant>Corn</plant>
        <category>Fungal disease</category>
        <summary>Rust disease that forms powdery pustules on corn leaves and can reduce photosynthesis.</summary>
        <symptoms>
            <item>Cinnamon-brown pustules on both leaf surfaces</item>
            <item>Pustules may rupture and release spores</item>
            <item>Heavy infection may reduce plant vigor</item>
        </symptoms>
        <treatment>
            <item>Remove severely affected leaves when practical</item>
            <item>Monitor spread across the plot</item>
            <item>Use resistant varieties or approved management if needed</item>
        </treatment>
        <prevention>
            <item>Choose resistant seed when available</item>
            <item>Avoid prolonged leaf wetness</item>
            <item>Inspect plants early in the season</item>
        </prevention>
    </disease>
    <disease label="Grape___Black_rot" image="grape_black_rot" severity="high">
        <commonName>Grape Black Rot</commonName>
        <scientificName>Guignardia bidwellii</scientificName>
        <plant>Grape</plant>
        <category>Fungal disease</category>
        <summary>A serious grape disease that causes leaf lesions and black shriveled fruit.</summary>
        <symptoms>
            <item>Brown circular leaf spots with dark borders</item>
            <item>Black lesions may develop on shoots</item>
            <item>Fruit shrivels into hard black mummies</item>
        </symptoms>
        <treatment>
            <item>Remove infected leaves and fruit clusters</item>
            <item>Prune to improve airflow and sunlight</item>
            <item>Apply approved grape disease management when necessary</item>
        </treatment>
        <prevention>
            <item>Clean up mummified fruit after harvest</item>
            <item>Avoid overcrowded vine growth</item>
            <item>Maintain regular vineyard sanitation</item>
        </prevention>
    </disease>
    <disease label="Apple___Apple_scab" image="apple_apple_scab" severity="medium">
        <commonName>Apple Scab</commonName>
        <scientificName>Venturia inaequalis</scientificName>
        <plant>Apple</plant>
        <category>Fungal disease</category>
        <summary>Common apple disease causing olive-green to dark lesions on leaves and fruit.</summary>
        <symptoms>
            <item>Velvety olive-green leaf spots</item>
            <item>Dark scabby fruit lesions</item>
            <item>Early leaf yellowing or drop</item>
        </symptoms>
        <treatment>
            <item>Prune affected foliage where practical</item>
            <item>Improve canopy airflow</item>
            <item>Use approved orchard disease control schedule</item>
        </treatment>
        <prevention>
            <item>Remove fallen infected leaves</item>
            <item>Avoid dense canopy moisture buildup</item>
            <item>Select tolerant varieties when possible</item>
        </prevention>
    </disease>
    <disease label="Orange___Haunglongbing_(Citrus_greening)" image="orange_citrus_greening" severity="high">
        <commonName>Citrus Greening</commonName>
        <scientificName>Candidatus Liberibacter asiaticus</scientificName>
        <plant>Orange</plant>
        <category>Bacterial disease</category>
        <summary>Severe citrus disease linked to leaf mottling, poor fruit quality, and tree decline.</summary>
        <symptoms>
            <item>Blotchy mottled leaf yellowing</item>
            <item>Small misshapen bitter fruit</item>
            <item>Gradual branch decline and poor growth</item>
        </symptoms>
        <treatment>
            <item>Isolate and monitor infected trees carefully</item>
            <item>Consult agricultural extension guidance promptly</item>
            <item>Control insect vectors where recommended</item>
        </treatment>
        <prevention>
            <item>Use disease-free planting material</item>
            <item>Monitor psyllid insect presence</item>
            <item>Remove severely affected trees if advised locally</item>
        </prevention>
    </disease>
</diseaseLibrary>
```

### Why this XML design is good

- `label` matches ML output exactly.
- `image` points to a drawable name.
- `severity` is easy to display in list cards.
- nested `<item>` tags map naturally to Java lists.
- healthy classes are included so the result screen still feels complete.

### Verification after Step 1

- [ ] The file is in the `assets/` folder, not `res/xml/`.
- [ ] Android Studio opens the file without obvious XML errors.
- [ ] The root element closes correctly.
- [ ] At least 10 full disease entries exist.
- [ ] Labels still include punctuation such as commas, parentheses, or underscores when required.

---

## Step 2 - Create the `Disease` model class

Create `Disease.java`.

```java
package com.leafguardai.model;

import java.util.ArrayList;
import java.util.List;

public class Disease {
    private String label;
    private String commonName;
    private String scientificName;
    private String plant;
    private String category;
    private String summary;
    private List<String> symptoms;
    private List<String> treatment;
    private List<String> prevention;
    private String imageName;
    private String severity;

    public Disease() {
        symptoms = new ArrayList<>();
        treatment = new ArrayList<>();
        prevention = new ArrayList<>();
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

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }

    public List<String> getTreatment() {
        return treatment;
    }

    public void setTreatment(List<String> treatment) {
        this.treatment = treatment;
    }

    public List<String> getPrevention() {
        return prevention;
    }

    public void setPrevention(List<String> prevention) {
        this.prevention = prevention;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        return "Disease{" +
                "label='" + label + ''' +
                ", commonName='" + commonName + ''' +
                ", plant='" + plant + ''' +
                ", severity='" + severity + ''' +
                '}';
    }
}
```

### Why this class is enough

- it stores all XML fields
- it avoids Android-specific dependencies
- it supports nested lists cleanly
- it is easy to use in both RecyclerView and detail screens

---

## Step 3 - Create the XML parser

Create `DiseaseXmlParser.java`.
This class converts XML into `Disease` objects.

```java
package com.leafguardai.parser;

import com.leafguardai.model.Disease;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DiseaseXmlParser {

    public List<Disease> parse(InputStream inputStream)
            throws XmlPullParserException, IOException {
        List<Disease> diseases = new ArrayList<>();
        Disease currentDisease = null;

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inputStream, null);

        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();

            if (eventType == XmlPullParser.START_TAG) {
                if ("disease".equals(tagName)) {
                    currentDisease = new Disease();
                    currentDisease.setLabel(parser.getAttributeValue(null, "label"));
                    currentDisease.setImageName(parser.getAttributeValue(null, "image"));
                    currentDisease.setSeverity(parser.getAttributeValue(null, "severity"));
                } else if (currentDisease != null) {
                    readDiseaseChild(parser, currentDisease, tagName);
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if ("disease".equals(tagName) && currentDisease != null) {
                    diseases.add(currentDisease);
                    currentDisease = null;
                }
            }

            eventType = parser.next();
        }

        return diseases;
    }

    private void readDiseaseChild(XmlPullParser parser, Disease disease, String tagName)
            throws IOException, XmlPullParserException {
        switch (tagName) {
            case "commonName":
                disease.setCommonName(readText(parser));
                break;
            case "scientificName":
                disease.setScientificName(readText(parser));
                break;
            case "plant":
                disease.setPlant(readText(parser));
                break;
            case "category":
                disease.setCategory(readText(parser));
                break;
            case "summary":
                disease.setSummary(readText(parser));
                break;
            case "symptoms":
                disease.setSymptoms(readItemList(parser, "symptoms"));
                break;
            case "treatment":
                disease.setTreatment(readItemList(parser, "treatment"));
                break;
            case "prevention":
                disease.setPrevention(readItemList(parser, "prevention"));
                break;
            default:
                skip(parser);
                break;
        }
    }

    private String readText(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String value = "";
        if (parser.next() == XmlPullParser.TEXT) {
            value = parser.getText().trim();
            parser.nextTag();
        }
        return value;
    }

    private List<String> readItemList(XmlPullParser parser, String parentTag)
            throws IOException, XmlPullParserException {
        List<String> items = new ArrayList<>();

        while (true) {
            int eventType = parser.next();

            if (eventType == XmlPullParser.END_TAG && parentTag.equals(parser.getName())) {
                break;
            }

            if (eventType != XmlPullParser.START_TAG) {
                continue;
            }

            if ("item".equals(parser.getName())) {
                String itemText = readText(parser);
                if (!itemText.isEmpty()) {
                    items.add(itemText);
                }
            } else {
                skip(parser);
            }
        }

        return items;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            return;
        }

        int depth = 1;
        while (depth != 0) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                depth++;
            } else if (eventType == XmlPullParser.END_TAG) {
                depth--;
            }
        }
    }
}
```

### Important parser behavior to understand

- attributes are read from the `<disease>` start tag
- simple text fields use `readText()`
- list sections use `readItemList()`
- unknown tags are ignored with `skip()`

### Verification after Step 3

Add temporary logging wherever you load the parser:

```java
Log.d("DiseaseXmlParser", "Parsed diseases count = " + diseases.size());
```

Make sure the count matches your XML file.

---

## Step 4 - Create the repository with singleton cache and background loading

Create `DiseaseRepository.java`.
This class is the heart of the feature because it keeps Activities simple.

```java
package com.leafguardai.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.leafguardai.model.Disease;
import com.leafguardai.parser.DiseaseXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiseaseRepository {
    private static final String TAG = "DiseaseRepository";
    private static final String FILE_NAME = "diseases.xml";

    private static DiseaseRepository instance;

    private final Context appContext;
    private final DiseaseXmlParser parser;
    private final ExecutorService executorService;
    private final Handler mainHandler;
    private final List<Disease> cachedList;
    private final Map<String, Disease> cachedMap;

    private boolean loaded;
    private boolean loading;

    public interface LoadCallback {
        void onSuccess(List<Disease> diseases);
        void onError(Exception exception);
    }

    private DiseaseRepository(Context context) {
        appContext = context.getApplicationContext();
        parser = new DiseaseXmlParser();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        cachedList = new ArrayList<>();
        cachedMap = new HashMap<>();
    }

    public static synchronized DiseaseRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DiseaseRepository(context);
        }
        return instance;
    }

    public synchronized boolean isLoaded() {
        return loaded;
    }

    public void loadDiseasesAsync(final LoadCallback callback) {
        synchronized (this) {
            if (loaded) {
                callback.onSuccess(new ArrayList<>(cachedList));
                return;
            }

            if (loading) {
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadDiseasesAsync(callback);
                    }
                }, 100);
                return;
            }

            loading = true;
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Disease> diseases = loadSynchronously();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(diseases);
                        }
                    });
                } catch (final Exception exception) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(exception);
                        }
                    });
                }
            }
        });
    }

    public synchronized List<Disease> loadSynchronously()
            throws IOException, XmlPullParserException {
        if (loaded) {
            return new ArrayList<>(cachedList);
        }

        InputStream inputStream = null;
        try {
            inputStream = appContext.getAssets().open(FILE_NAME);
            List<Disease> parsedDiseases = parser.parse(inputStream);

            cachedList.clear();
            cachedMap.clear();

            for (Disease disease : parsedDiseases) {
                cachedList.add(disease);
                if (disease.getLabel() != null) {
                    cachedMap.put(disease.getLabel(), disease);
                }
            }

            loaded = true;
            Log.d(TAG, "Disease library parsed successfully. Count = " + cachedList.size());
            return new ArrayList<>(cachedList);
        } finally {
            loading = false;
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public synchronized List<Disease> getAllDiseases() {
        return Collections.unmodifiableList(new ArrayList<>(cachedList));
    }

    public synchronized Disease findByLabel(String label) {
        if (label == null) {
            return null;
        }
        return cachedMap.get(label);
    }

    public synchronized void clearCacheForTesting() {
        loaded = false;
        loading = false;
        cachedList.clear();
        cachedMap.clear();
    }
}
```

### Why this repository design is strong

- it uses `getApplicationContext()` safely
- it caches list and map forms of the same data
- it supports asynchronous loading for UI screens
- it supports quick lookup by label after prediction
- it has a testing helper for cache reset

### Background thread explanation

`loadDiseasesAsync()` uses `ExecutorService` so parsing happens off the main thread.
When parsing is complete, it posts the result back to the main thread with `Handler`.
That is exactly what you should explain in viva.

---

## Step 5 - Create the list item layout

Create `res/layout/item_disease.xml`.

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginHorizontal="12dp"
    android:layout_marginTop="8dp"
    android:layout_marginBottom="4dp"
    app:cardCornerRadius="12dp"
    app:cardElevation="4dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="12dp">

        <ImageView
            android:id="@+id/imageDisease"
            android:layout_width="72dp"
            android:layout_height="72dp"
            android:contentDescription="Disease image"
            android:scaleType="centerCrop"
            android:src="@drawable/ic_disease_placeholder" />

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="12dp"
            android:layout_weight="1"
            android:orientation="vertical">

            <TextView
                android:id="@+id/textCommonName"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Disease Name"
                android:textColor="@android:color/black"
                android:textSize="18sp"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/textPlant"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="4dp"
                android:text="Plant Name"
                android:textSize="14sp" />

            <TextView
                android:id="@+id/textSummary"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="6dp"
                android:ellipsize="end"
                android:maxLines="2"
                android:text="Short summary"
                android:textSize="14sp" />

            <TextView
                android:id="@+id/textSeverity"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="6dp"
                android:text="Severity: Medium"
                android:textStyle="italic" />
        </LinearLayout>
    </LinearLayout>
</com.google.android.material.card.MaterialCardView>
```

---

## Step 6 - Create the activity layout for the library screen

Create `res/layout/activity_disease_library.xml`.

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:id="@+id/textLibraryTitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp"
        android:text="Disease Library"
        android:textAlignment="center"
        android:textSize="24sp"
        android:textStyle="bold" />

    <ProgressBar
        android:id="@+id/progressLibrary"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginBottom="8dp" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerDiseases"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:clipToPadding="false"
        android:paddingBottom="12dp" />
</LinearLayout>
```

### Optional improvement

Later, you can add a `SearchView` above the RecyclerView.
For now, the core goal is correct XML-driven display.

---

## Step 7 - Create the detail layout

Create `res/layout/activity_disease_detail.xml`.

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <ImageView
            android:id="@+id/imageDiseaseDetail"
            android:layout_width="match_parent"
            android:layout_height="220dp"
            android:contentDescription="Disease detail image"
            android:scaleType="centerCrop"
            android:src="@drawable/ic_disease_placeholder" />

        <TextView
            android:id="@+id/textDetailCommonName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Disease Name"
            android:textSize="24sp"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textScientificName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:text="Scientific Name"
            android:textStyle="italic" />

        <TextView
            android:id="@+id/textPlantDetail"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="Plant" />

        <TextView
            android:id="@+id/textCategoryDetail"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:text="Category" />

        <TextView
            android:id="@+id/textSeverityDetail"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:text="Severity" />

        <TextView
            android:id="@+id/textSummaryDetail"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="12dp"
            android:text="Summary" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:text="Symptoms"
            android:textSize="18sp"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textSymptoms"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="Symptoms list" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:text="Treatment"
            android:textSize="18sp"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textTreatment"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="Treatment list" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:text="Prevention"
            android:textSize="18sp"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textPrevention"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="Prevention list" />
    </LinearLayout>
</ScrollView>
```

---

## Step 8 - Create the RecyclerView adapter

Create `DiseaseAdapter.java`.

```java
package com.leafguardai.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leafguardai.R;
import com.leafguardai.model.Disease;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {

    public interface OnDiseaseClickListener {
        void onDiseaseClick(Disease disease);
    }

    private final Context context;
    private final List<Disease> diseases;
    private final OnDiseaseClickListener listener;

    public DiseaseAdapter(Context context, List<Disease> diseases, OnDiseaseClickListener listener) {
        this.context = context;
        this.diseases = new ArrayList<>(diseases);
        this.listener = listener;
    }

    public void updateData(List<Disease> newDiseases) {
        diseases.clear();
        diseases.addAll(newDiseases);
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
        final Disease disease = diseases.get(position);
        holder.textCommonName.setText(disease.getCommonName());
        holder.textPlant.setText("Plant: " + disease.getPlant());
        holder.textSummary.setText(disease.getSummary());
        holder.textSeverity.setText("Severity: " + capitalize(disease.getSeverity()));

        int imageResId = context.getResources().getIdentifier(
                disease.getImageName(),
                "drawable",
                context.getPackageName()
        );

        if (imageResId == 0) {
            imageResId = R.drawable.ic_disease_placeholder;
        }
        holder.imageDisease.setImageResource(imageResId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDiseaseClick(disease);
            }
        });
    }

    @Override
    public int getItemCount() {
        return diseases.size();
    }

    private String capitalize(String value) {
        if (value == null || value.isEmpty()) {
            return "Unknown";
        }
        return value.substring(0, 1).toUpperCase(Locale.US) + value.substring(1);
    }

    static class DiseaseViewHolder extends RecyclerView.ViewHolder {
        ImageView imageDisease;
        TextView textCommonName;
        TextView textPlant;
        TextView textSummary;
        TextView textSeverity;

        DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);
            imageDisease = itemView.findViewById(R.id.imageDisease);
            textCommonName = itemView.findViewById(R.id.textCommonName);
            textPlant = itemView.findViewById(R.id.textPlant);
            textSummary = itemView.findViewById(R.id.textSummary);
            textSeverity = itemView.findViewById(R.id.textSeverity);
        }
    }
}
```

### What this adapter does

- binds each `Disease` object to one card row
- resolves drawable names dynamically
- shows a placeholder if the image resource is missing
- opens a click listener so the activity can navigate to the detail screen

---

## Step 9 - Create `DiseaseLibraryActivity`

Create `DiseaseLibraryActivity.java`.

```java
package com.leafguardai.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leafguardai.R;
import com.leafguardai.model.Disease;
import com.leafguardai.repository.DiseaseRepository;
import com.leafguardai.ui.adapter.DiseaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class DiseaseLibraryActivity extends AppCompatActivity {
    private RecyclerView recyclerDiseases;
    private ProgressBar progressLibrary;
    private DiseaseAdapter adapter;
    private DiseaseRepository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_library);

        recyclerDiseases = findViewById(R.id.recyclerDiseases);
        progressLibrary = findViewById(R.id.progressLibrary);

        repository = DiseaseRepository.getInstance(getApplicationContext());

        adapter = new DiseaseAdapter(this, new ArrayList<Disease>(),
                new DiseaseAdapter.OnDiseaseClickListener() {
                    @Override
                    public void onDiseaseClick(Disease disease) {
                        Intent intent = new Intent(DiseaseLibraryActivity.this, DiseaseDetailActivity.class);
                        intent.putExtra("disease_label", disease.getLabel());
                        startActivity(intent);
                    }
                });

        recyclerDiseases.setLayoutManager(new LinearLayoutManager(this));
        recyclerDiseases.setAdapter(adapter);

        loadDiseases();
    }

    private void loadDiseases() {
        progressLibrary.setVisibility(View.VISIBLE);

        repository.loadDiseasesAsync(new DiseaseRepository.LoadCallback() {
            @Override
            public void onSuccess(List<Disease> diseases) {
                progressLibrary.setVisibility(View.GONE);
                adapter.updateData(diseases);
            }

            @Override
            public void onError(Exception exception) {
                progressLibrary.setVisibility(View.GONE);
                Toast.makeText(DiseaseLibraryActivity.this,
                        "Failed to load disease library: " + exception.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
```

### Why this activity is clean

- it does not parse XML directly
- it gets data through the repository
- it handles loading and error states
- it uses label-based navigation to the detail screen

---

## Step 10 - Create `DiseaseDetailActivity`

This activity shows the full content of one disease entry.

```java
package com.leafguardai.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.leafguardai.R;
import com.leafguardai.model.Disease;
import com.leafguardai.repository.DiseaseRepository;

import java.util.List;

public class DiseaseDetailActivity extends AppCompatActivity {
    private ImageView imageDiseaseDetail;
    private TextView textDetailCommonName;
    private TextView textScientificName;
    private TextView textPlantDetail;
    private TextView textCategoryDetail;
    private TextView textSeverityDetail;
    private TextView textSummaryDetail;
    private TextView textSymptoms;
    private TextView textTreatment;
    private TextView textPrevention;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);

        imageDiseaseDetail = findViewById(R.id.imageDiseaseDetail);
        textDetailCommonName = findViewById(R.id.textDetailCommonName);
        textScientificName = findViewById(R.id.textScientificName);
        textPlantDetail = findViewById(R.id.textPlantDetail);
        textCategoryDetail = findViewById(R.id.textCategoryDetail);
        textSeverityDetail = findViewById(R.id.textSeverityDetail);
        textSummaryDetail = findViewById(R.id.textSummaryDetail);
        textSymptoms = findViewById(R.id.textSymptoms);
        textTreatment = findViewById(R.id.textTreatment);
        textPrevention = findViewById(R.id.textPrevention);

        String diseaseLabel = getIntent().getStringExtra("disease_label");
        DiseaseRepository repository = DiseaseRepository.getInstance(getApplicationContext());
        Disease disease = repository.findByLabel(diseaseLabel);

        if (disease == null) {
            Toast.makeText(this,
                    "Disease details are not available.",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        bindDisease(disease);
    }

    private void bindDisease(Disease disease) {
        textDetailCommonName.setText(disease.getCommonName());
        textScientificName.setText(disease.getScientificName());
        textPlantDetail.setText("Plant: " + disease.getPlant());
        textCategoryDetail.setText("Category: " + disease.getCategory());
        textSeverityDetail.setText("Severity: " + disease.getSeverity());
        textSummaryDetail.setText(disease.getSummary());
        textSymptoms.setText(formatBulletList(disease.getSymptoms()));
        textTreatment.setText(formatBulletList(disease.getTreatment()));
        textPrevention.setText(formatBulletList(disease.getPrevention()));

        int imageResId = getResources().getIdentifier(
                disease.getImageName(),
                "drawable",
                getPackageName()
        );
        if (imageResId == 0) {
            imageResId = R.drawable.ic_disease_placeholder;
        }
        imageDiseaseDetail.setImageResource(imageResId);
    }

    private String formatBulletList(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "No information available.";
        }

        StringBuilder builder = new StringBuilder();
        for (String item : items) {
            builder.append("• ").append(item).append("
");
        }
        return builder.toString().trim();
    }
}
```

### Good design decision

The detail screen performs lookup by label instead of sending every field through the Intent.
That keeps your Intent small and uses the repository as the single source of truth.

---

## Step 11 - Register Activities in `AndroidManifest.xml`

Add the new screens inside your `<application>` tag if they are not already declared.

```xml
<activity android:name=".ui.DiseaseLibraryActivity" />
<activity android:name=".ui.DiseaseDetailActivity" />
```

If your package structure differs, adjust the fully qualified class names accordingly.

---

## Step 12 - Integrate with `ResultActivity`

Now connect the disease library to the ML prediction flow.
The exact structure of your existing `ResultActivity` may differ, so adapt carefully.

### Part A - Make sure prediction data is passed correctly

Wherever the prediction finishes, pass the label and confidence:

```java
Intent intent = new Intent(CurrentActivity.this, ResultActivity.class);
intent.putExtra("predicted_label", predictedLabel);
intent.putExtra("confidence", confidenceScore);
startActivity(intent);
```

### Part B - Add UI fields to the result layout

At minimum, your result screen should include:

- prediction title
- confidence text
- summary text
- symptoms text
- treatment text
- prevention text
- button to open the full library

### Part C - Load repository data and bind the matched disease

```java
public class ResultActivity extends AppCompatActivity {
    private TextView textPredictionTitle;
    private TextView textConfidence;
    private TextView textSummary;
    private TextView textSymptoms;
    private TextView textTreatment;
    private TextView textPrevention;
    private Button buttonOpenLibrary;
    private DiseaseRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textPredictionTitle = findViewById(R.id.textPredictionTitle);
        textConfidence = findViewById(R.id.textConfidence);
        textSummary = findViewById(R.id.textSummary);
        textSymptoms = findViewById(R.id.textSymptoms);
        textTreatment = findViewById(R.id.textTreatment);
        textPrevention = findViewById(R.id.textPrevention);
        buttonOpenLibrary = findViewById(R.id.buttonOpenLibrary);

        repository = DiseaseRepository.getInstance(getApplicationContext());

        final String predictedLabel = getIntent().getStringExtra("predicted_label");
        float confidence = getIntent().getFloatExtra("confidence", 0f);
        textConfidence.setText(String.format(Locale.US, "Confidence: %.2f%%", confidence * 100f));

        repository.loadDiseasesAsync(new DiseaseRepository.LoadCallback() {
            @Override
            public void onSuccess(List<Disease> diseases) {
                bindPrediction(predictedLabel);
            }

            @Override
            public void onError(Exception exception) {
                showFallback(predictedLabel);
            }
        });

        buttonOpenLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, DiseaseLibraryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void bindPrediction(String predictedLabel) {
        Disease disease = repository.findByLabel(predictedLabel);
        if (disease == null) {
            showFallback(predictedLabel);
            return;
        }

        textPredictionTitle.setText(disease.getCommonName());
        textSummary.setText(disease.getSummary());
        textSymptoms.setText(formatBulletList(disease.getSymptoms()));
        textTreatment.setText(formatBulletList(disease.getTreatment()));
        textPrevention.setText(formatBulletList(disease.getPrevention()));
    }

    private void showFallback(String predictedLabel) {
        textPredictionTitle.setText(predictedLabel != null ? predictedLabel : "Unknown prediction");
        textSummary.setText("Detailed disease information is not available yet.");
        textSymptoms.setText("Please inspect the leaf carefully and update diseases.xml later.");
        textTreatment.setText("Use the library screen to browse known diseases.");
        textPrevention.setText("Keep the app data updated as new disease entries are added.");
    }

    private String formatBulletList(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "No information available.";
        }

        StringBuilder builder = new StringBuilder();
        for (String item : items) {
            builder.append("• ").append(item).append("
");
        }
        return builder.toString().trim();
    }
}
```

### Why this integration is important

Without this step, the XML library is isolated from the actual prediction workflow.
This integration turns the disease library into a real product feature instead of a separate demo screen.

---

## Step 13 - Add image resources

Create or collect drawable images with names that match your XML `image` attributes.
Examples:

- `tomato_late_blight.png`
- `tomato_early_blight.png`
- `tomato_healthy.png`
- `potato_early_blight.png`
- `potato_late_blight.png`
- `potato_healthy.png`
- `pepper_bacterial_spot.png`
- `pepper_healthy.png`
- `corn_common_rust.png`
- `grape_black_rot.png`
- `apple_apple_scab.png`
- `orange_citrus_greening.png`

Also create a fallback placeholder such as:

- `ic_disease_placeholder.xml`
- or `ic_disease_placeholder.png`

### Why placeholder support matters

You may not have a custom image for all 38 classes yet.
The placeholder keeps the UI professional and prevents empty image areas.

---

## Step 14 - Proper error handling strategy

Your feature should survive these problems:

1. XML file missing from assets
2. malformed XML structure
3. wrong drawable name
4. missing disease label in XML
5. empty result from parser

### Recommended handling rules

- log the exception in repository code
- show a friendly `Toast` in the library screen if loading fails
- show fallback text in the result screen if a label is missing
- use placeholder image when drawable lookup returns `0`
- never let the app crash from a missing XML entry

### Example repository logging

```java
catch (Exception exception) {
    Log.e(TAG, "Failed to load disease library", exception);
    throw exception;
}
```

### Example UI fallback principle

Bad message:

```text
Null pointer exception in disease repository
```

Good message:

```text
Detailed disease information is not available yet.
```

---

## Step 15 - Optional background loading alternative using `AsyncTask`

If your course materials still use `AsyncTask`, you can understand the same idea through this older API.
It is not the most modern approach, but it is often seen in academic Android examples.

```java
private class LoadLibraryTask extends AsyncTask<Void, Void, List<Disease>> {
    private Exception error;

    @Override
    protected List<Disease> doInBackground(Void... voids) {
        try {
            return DiseaseRepository.getInstance(getApplicationContext()).loadSynchronously();
        } catch (Exception exception) {
            error = exception;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Disease> diseases) {
        if (error != null) {
            Toast.makeText(DiseaseLibraryActivity.this,
                    "Failed to load disease library",
                    Toast.LENGTH_LONG).show();
            return;
        }

        adapter.updateData(diseases);
        progressLibrary.setVisibility(View.GONE);
    }
}
```

### Important note

For a modern Java Android project, `ExecutorService` plus `Handler` is a better pattern.
Still, knowing `AsyncTask` helps for CSE 2206 discussions.

---

## Step 16 - Unit test the parser

Create a parser test if your project already supports tests.
The goal is to verify logic without touching the UI.

```java
@Test
public void parse_validXml_returnsOneDisease() throws Exception {
    String xml = "<?xml version="1.0" encoding="utf-8"?>"
            + "<diseaseLibrary>"
            + "<disease label="Tomato___Late_blight" image="tomato_late_blight" severity="high">"
            + "<commonName>Tomato Late Blight</commonName>"
            + "<scientificName>Phytophthora infestans</scientificName>"
            + "<plant>Tomato</plant>"
            + "<category>Oomycete disease</category>"
            + "<summary>Rapid blight.</summary>"
            + "<symptoms><item>Dark lesions</item></symptoms>"
            + "<treatment><item>Remove infected leaves</item></treatment>"
            + "<prevention><item>Avoid overhead watering</item></prevention>"
            + "</disease>"
            + "</diseaseLibrary>";

    ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    DiseaseXmlParser parser = new DiseaseXmlParser();

    List<Disease> diseases = parser.parse(inputStream);

    assertEquals(1, diseases.size());
    assertEquals("Tomato___Late_blight", diseases.get(0).getLabel());
    assertEquals("Tomato Late Blight", diseases.get(0).getCommonName());
    assertEquals(1, diseases.get(0).getSymptoms().size());
}
```

### What this proves

- the parser recognizes one disease block
- attributes are read correctly
- text tags are read correctly
- nested `<item>` lists are read correctly

---

## Step 17 - Manual testing plan

### Library screen testing

- [ ] Activity opens without crash
- [ ] ProgressBar shows while loading
- [ ] RecyclerView displays all disease entries
- [ ] Images appear or placeholder appears
- [ ] Clicking a row opens detail screen

### Detail screen testing

- [ ] Common name matches the selected item
- [ ] Scientific name appears correctly
- [ ] Symptoms are shown as bullets
- [ ] Treatment and prevention are readable
- [ ] Scrolling works on small screens

### Result integration testing

- [ ] A known prediction label shows matching XML details
- [ ] An unknown label shows fallback text
- [ ] Confidence still displays correctly
- [ ] Open Library button works

### Offline testing

- [ ] Turn on airplane mode
- [ ] Open the library screen
- [ ] Open a result detail screen
- [ ] Confirm everything still works because data is local

---

## Step 18 - Common debugging problems and fixes

### Problem: RecyclerView is blank

**Possible causes:**

- adapter was never updated
- repository load failed
- layout manager was not set
- XML file contains zero valid entries

**Fixes:**

- check Logcat for parse count
- confirm `recyclerView.setLayoutManager(...)` exists
- confirm `adapter.updateData(diseases)` runs on success
- verify XML root and disease tags

### Problem: Detail screen closes immediately

**Possible causes:**

- label extra was missing
- repository cache was not loaded yet
- clicked disease has null label

**Fixes:**

- log the label before starting `DiseaseDetailActivity`
- ensure the library screen loads repository data first
- confirm `intent.putExtra("disease_label", disease.getLabel())`

### Problem: Result screen shows fallback for known diseases

**Possible causes:**

- model output label differs from XML label
- extra key name is wrong
- repository has not loaded yet

**Fixes:**

- log the exact `predictedLabel`
- compare it character by character with XML
- load the repository before calling `findByLabel()`

### Problem: Images do not show

**Possible causes:**

- drawable name in XML does not match resource file name
- file is placed in the wrong resource folder
- `getIdentifier()` returned `0`

**Fixes:**

- check lowercase underscore naming
- verify image extension and placement in `res/drawable`
- test placeholder path first

---

## Step 19 - What to say in viva about this feature

Practice this answer:

> In Week 08, I implemented an offline disease library using XML because XML parsing is a core CSE 2206 topic. I stored a `diseases.xml` file in the assets folder and parsed it in Java using `XmlPullParser`. Each disease entry becomes a `Disease` object. I then used a singleton `DiseaseRepository` to cache the parsed data in memory using both a list and a map. The list is used for RecyclerView display in `DiseaseLibraryActivity`, and the map is used for fast lookup in `ResultActivity` after the ML model predicts a label. I also added graceful fallback handling for missing labels and missing images.

If you can explain that smoothly, you understand the architecture.

---

## Step 20 - Final completion checklist

### Core files

- [ ] `diseases.xml`
- [ ] `Disease.java`
- [ ] `DiseaseXmlParser.java`
- [ ] `DiseaseRepository.java`
- [ ] `DiseaseAdapter.java`
- [ ] `DiseaseLibraryActivity.java`
- [ ] `DiseaseDetailActivity.java`

### UI integration

- [ ] library activity registered in manifest
- [ ] detail activity registered in manifest
- [ ] result screen integrated with disease lookup
- [ ] drawable placeholder added

### Quality checks

- [ ] XML loads in background
- [ ] data is cached
- [ ] known labels resolve correctly
- [ ] missing labels show fallback
- [ ] no crashes when image is missing
- [ ] offline operation confirmed

---

## What You Need To Do Next

1. Type these files into your project carefully.
2. Build after every major file, not only at the end.
3. Test the XML parser before the RecyclerView.
4. Test the library screen before the result integration.
5. Complete the Week 08 validation checklist.
6. Save screenshots and Logcat evidence.

---

## Understanding Check

Before moving on, explain these in your own words:

1. Why is a repository cleaner than parsing XML in an Activity?
2. Why do we keep both a `List<Disease>` and a `Map<String, Disease>`?
3. Why does `ResultActivity` use the label as the lookup key?
4. What problem does background loading solve?
5. What problem does graceful fallback solve?

If you can answer all five clearly, your Week 08 build task is complete.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 08 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| **4** | **build-task.md** ← *You are here* | **Build Implementation Guide** |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Practice Exercises](exercises.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Build Implementation Guide** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Validation & Verification →](validation-checklist.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 07: Room Database & History](../week-07-room-sqlite-history/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 09: TensorFlow Lite Offline AI ➡](../week-09-tensorflow-lite-offline-ai/README.md) |

---
