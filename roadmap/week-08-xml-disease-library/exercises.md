# Week 08: Exercises — XML Disease Library

> **Prerequisites:** Week 07 Room Database complete | Time Budget: ~8 hours
>
> **Accuracy note:** The shipped app reads `app/src/main/assets/diseases.xml` (in `assets/`, filename `diseases.xml`) with `XmlPullParser` inside `DiseaseLibraryActivity`. It has **10** `<disease>` entries using five tags — `<name>`, `<plant>`, `<symptoms>`, `<treatment>`, `<prevention>` — and each `<name>` matches `assets/labels.txt` exactly (spaces, not underscores). A larger schema below is an optional extension. Kotlin is the primary track; Java is the secondary reference.

---

## Exercise 1: Design the XML Schema (30 min)

Before writing any code, design the XML structure for a disease entry.

**Task:** On paper (or in a text file), sketch out the XML structure for a
`<disease>` element that stores: label, commonName, scientificName, symptoms,
treatment, prevention, severity, and affectedCrops.

**Questions to answer before starting:**
1. What should the root element be called?
2. Should `affectedCrops` contain multiple `<crop>` child elements, or a
   comma-separated string?
3. What characters in `symptoms` might need CDATA wrapping?

**Expected outcome:** A written XML schema before touching Android Studio.

---

## Exercise 2: Create diseases.xml (2 hours)

Create `app/src/main/assets/diseases.xml` with ALL 15 diseases from
the solution reference (or at least 10 covering the crops you trained on).

The file must follow this structure:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<diseases>
    <disease>
        <label>Apple___Apple_scab</label>
        <commonName>Apple Scab</commonName>
        <scientificName>Venturia inaequalis</scientificName>
        <affectedCrop>Apple</affectedCrop>
        <symptoms>Olive-green or brown velvety spots on leaves and fruit. 
                  Leaves may curl and fall early. Fruit shows dark, corky spots 
                  that reduce market value.</symptoms>
        <treatment>Apply fungicide (myclobutanil or sulfur) at first sign. 
                   Remove and destroy fallen leaves. Prune to improve air 
                   circulation.</treatment>
        <prevention>Plant scab-resistant apple varieties (e.g., Liberty, Freedom). 
                    Rake and dispose of leaves in autumn. Avoid overhead irrigation.
                    Apply dormant oil spray before budbreak.</prevention>
        <severity>High</severity>
    </disease>

    <disease>
        <label>Apple___healthy</label>
        <commonName>Healthy Apple</commonName>
        <scientificName>N/A</scientificName>
        <affectedCrop>Apple</affectedCrop>
        <symptoms>No disease symptoms. Leaves are uniformly green, smooth, and 
                  properly formed. No spots, lesions, or discoloration.</symptoms>
        <treatment>No treatment required.</treatment>
        <prevention>Maintain good cultural practices: proper spacing, balanced 
                    fertilization, timely pruning, and regular scouting.</prevention>
        <severity>None</severity>
    </disease>

    <disease>
        <label>Tomato___Late_blight</label>
        <commonName>Tomato Late Blight</commonName>
        <scientificName>Phytophthora infestans</scientificName>
        <affectedCrop>Tomato</affectedCrop>
        <symptoms>Large, irregular water-soaked spots on leaves that rapidly turn 
                  brown. White fuzzy mold on leaf undersides in humid conditions. 
                  Brown lesions on stems. Fruit shows firm, brown rot.</symptoms>
        <treatment>Remove infected plant parts immediately. Apply copper-based 
                   fungicide (copper hydroxide) or mancozeb. Avoid wetting foliage 
                   when watering.</treatment>
        <prevention>Use certified disease-free seeds. Plant resistant varieties. 
                    Rotate crops (avoid planting tomato/potato in same spot for 3 
                    years). Ensure good drainage and air circulation.</prevention>
        <severity>High</severity>
    </disease>

    <disease>
        <label>Potato___Early_blight</label>
        <commonName>Potato Early Blight</commonName>
        <scientificName>Alternaria solani</scientificName>
        <affectedCrop>Potato</affectedCrop>
        <symptoms>Circular to angular dark brown spots with concentric rings 
                  (target board pattern) on older leaves. Yellow halo surrounds 
                  spots. Lesions enlarge and coalesce causing leaf death.</symptoms>
        <treatment>Apply chlorothalonil or azoxystrobin fungicide. Remove severely 
                   infected leaves. Maintain adequate plant nutrition especially 
                   potassium.</treatment>
        <prevention>Use certified disease-free seed potatoes. Practice crop 
                    rotation. Avoid overhead irrigation. Maintain adequate plant 
                    spacing. Remove plant debris after harvest.</prevention>
        <severity>Medium</severity>
    </disease>

    <disease>
        <label>Tomato___Bacterial_spot</label>
        <commonName>Tomato Bacterial Spot</commonName>
        <scientificName>Xanthomonas vesicatoria</scientificName>
        <affectedCrop>Tomato</affectedCrop>
        <symptoms>Small, water-soaked circular spots on leaves that turn dark 
                  brown with yellow halos. Spots may join to form blighted areas. 
                  Fruit develops small raised scabby lesions with water-soaked 
                  margins.</symptoms>
        <treatment>Apply copper-based bactericide at early signs. Avoid working 
                   in field when plants are wet. Remove heavily infected plants.</treatment>
        <prevention>Use pathogen-free certified seed. Apply copper sprays 
                    preventively. Practice crop rotation. Avoid overhead 
                    irrigation. Disinfect tools between plants.</prevention>
        <severity>Medium</severity>
    </disease>
</diseases>
```

**Verification:**
- Open the XML file in a web browser — it should display with proper tree structure
- No "Parse error" messages from the browser
- At least 10 disease entries

---

## Exercise 3: Create the Disease Java Model (45 min)

Create `app/src/main/java/com/leafguard/data/model/Disease.java`:

```java
public class Disease {
    private String label;
    private String commonName;
    private String scientificName;
    private String affectedCrop;
    private String symptoms;
    private String treatment;
    private String prevention;
    private String severity;

    // Default constructor (required for some operations)
    public Disease() {}

    // Full constructor for manual creation
    public Disease(String label, String commonName, String scientificName,
                   String affectedCrop, String symptoms, String treatment,
                   String prevention, String severity) {
        this.label = label;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.affectedCrop = affectedCrop;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.prevention = prevention;
        this.severity = severity;
    }

    // --- Getters ---
    public String getLabel() { return label; }
    public String getCommonName() { return commonName; }
    public String getScientificName() { return scientificName; }
    public String getAffectedCrop() { return affectedCrop; }
    public String getSymptoms() { return symptoms; }
    public String getTreatment() { return treatment; }
    public String getPrevention() { return prevention; }
    public String getSeverity() { return severity; }

    // --- Setters ---
    public void setLabel(String label) { this.label = label; }
    public void setCommonName(String commonName) { this.commonName = commonName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }
    public void setAffectedCrop(String affectedCrop) { this.affectedCrop = affectedCrop; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public void setPrevention(String prevention) { this.prevention = prevention; }
    public void setSeverity(String severity) { this.severity = severity; }

    @Override
    public String toString() {
        return "Disease{label='" + label + "', commonName='" + commonName + "'}";
    }
}
```

**Verification:**
- Class compiles without errors
- Write a quick test in MainActivity: `Disease d = new Disease(); d.setLabel("test"); Log.d("TEST", d.getLabel());`
- Expected logcat output: `TEST: test`

---

## Exercise 4: Implement DiseaseXmlParser (2.5 hours)

Create `app/src/main/java/com/leafguard/data/xml/DiseaseXmlParser.java`.

This is the core parsing logic.

**Step 4a:** Start with a skeleton and test it compiles:

```java
public class DiseaseXmlParser {
    private static final String TAG = "DiseaseXmlParser";
    
    public List<Disease> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        List<Disease> diseases = new ArrayList<>();
        
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        
        Disease currentDisease = null;
        String currentTag = "";
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
                    if (currentDisease != null) {
                        String text = parser.getText().trim();
                        if (!text.isEmpty()) {
                            switch (currentTag) {
                                case "label":       currentDisease.setLabel(text); break;
                                case "commonName":  currentDisease.setCommonName(text); break;
                                case "scientificName": currentDisease.setScientificName(text); break;
                                case "affectedCrop": currentDisease.setAffectedCrop(text); break;
                                case "symptoms":    currentDisease.setSymptoms(text); break;
                                case "treatment":   currentDisease.setTreatment(text); break;
                                case "prevention":  currentDisease.setPrevention(text); break;
                                case "severity":    currentDisease.setSeverity(text); break;
                            }
                        }
                    }
                    break;
                    
                case XmlPullParser.END_TAG:
                    if ("disease".equals(parser.getName()) && currentDisease != null) {
                        diseases.add(currentDisease);
                        currentDisease = null;
                        Log.d(TAG, "Parsed disease: " + 
                              (diseases.get(diseases.size()-1).getCommonName()));
                    }
                    currentTag = "";
                    break;
            }
            eventType = parser.next();
        }
        
        Log.d(TAG, "Total diseases parsed: " + diseases.size());
        return diseases;
    }
}
```

**Step 4b:** Test in MainActivity:

```java
try {
    InputStream is = getAssets().open("diseases.xml");
    DiseaseXmlParser xmlParser = new DiseaseXmlParser();
    List<Disease> diseases = xmlParser.parse(is);
    Toast.makeText(this, 
        "Loaded " + diseases.size() + " diseases from XML", 
        Toast.LENGTH_LONG).show();
    
    // Print first disease details
    if (!diseases.isEmpty()) {
        Disease first = diseases.get(0);
        Log.d("XML_TEST", "First disease: " + first.getCommonName());
        Log.d("XML_TEST", "Symptoms: " + first.getSymptoms());
    }
    
} catch (XmlPullParserException e) {
    Log.e("XML_TEST", "XML parsing error: " + e.getMessage(), e);
    Toast.makeText(this, "XML Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
} catch (IOException e) {
    Log.e("XML_TEST", "File not found or IO error: " + e.getMessage(), e);
    Toast.makeText(this, "File Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
}
```

**Verification:**
- Toast shows correct disease count matching your XML
- Logcat shows all disease names

---

## Exercise 5: DiseaseRepository with Singleton + Caching (1.5 hours)

Parsing XML on every lookup is inefficient. Create a `DiseaseRepository.java` that
parses once and caches all diseases in a `Map<String, Disease>` keyed by label.

```java
public class DiseaseRepository {
    private static DiseaseRepository instance;
    private Map<String, Disease> diseaseCache;
    private final Context context;
    private boolean isLoaded = false;

    private DiseaseRepository(Context context) {
        this.context = context.getApplicationContext();
        diseaseCache = new HashMap<>();
    }

    public static synchronized DiseaseRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DiseaseRepository(context);
        }
        return instance;
    }

    /**
     * Must be called on a background thread (takes ~20ms to read file).
     */
    public synchronized void loadIfNeeded() throws XmlPullParserException, IOException {
        if (isLoaded) return; // Already cached
        
        InputStream is = context.getAssets().open("diseases.xml");
        DiseaseXmlParser parser = new DiseaseXmlParser();
        List<Disease> diseases = parser.parse(is);
        
        diseaseCache.clear();
        for (Disease d : diseases) {
            if (d.getLabel() != null) {
                diseaseCache.put(d.getLabel(), d);
            }
        }
        isLoaded = true;
        is.close();
        Log.d("DiseaseRepo", "Loaded " + diseaseCache.size() + " diseases to cache");
    }

    /**
     * Returns null if label not found or cache not loaded.
     */
    public Disease findByLabel(String label) {
        if (label == null) return null;
        return diseaseCache.get(label);
    }

    public List<Disease> getAllDiseases() {
        return new ArrayList<>(diseaseCache.values());
    }

    public int getDiseaseCount() {
        return diseaseCache.size();
    }
    
    /** For testing only — reset the singleton. */
    static void resetForTesting() {
        instance = null;
    }
}
```

**Key design decisions to understand:**
- `synchronized` prevents two threads from parsing simultaneously
- `getApplicationContext()` avoids Activity memory leaks
- `isLoaded` flag prevents parsing the XML file again after first load

**Test the repository in ResultActivity:**

```java
// In ResultActivity.java after getting prediction label
private void loadDiseaseInfo(final String label) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(() -> {
        try {
            DiseaseRepository repo = DiseaseRepository.getInstance(ResultActivity.this);
            repo.loadIfNeeded();
            Disease disease = repo.findByLabel(label);
            
            runOnUiThread(() -> {
                if (disease != null) {
                    tvSymptoms.setText(disease.getSymptoms());
                    tvTreatment.setText(disease.getTreatment());
                    tvPrevention.setText(disease.getPrevention());
                    tvSeverity.setText("Severity: " + disease.getSeverity());
                } else {
                    tvSymptoms.setText("Disease details not available for: " + label);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Failed to load disease info", e);
            runOnUiThread(() -> tvSymptoms.setText("Could not load disease details."));
        }
    });
    executor.shutdown();
}
```

**Verification:**
- After prediction, ResultActivity shows correct symptoms/treatment/prevention
- Second lookup is instant (no XML re-parsing)
- Check logcat: "Loaded N diseases to cache" appears only once per app session

---

## Exercise 6: DiseaseLibraryActivity with RecyclerView (2 hours)

**Step 6a: Create layout `activity_disease_library.xml`:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <SearchView
        android:id="@+id/searchView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:iconifiedByDefault="false"
        android:hint="Search diseases..." />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvDiseases"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:padding="8dp" />

</LinearLayout>
```

**Step 6b: Create item layout `item_disease.xml`:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="4dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="12dp">

        <TextView
            android:id="@+id/tvCommonName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textStyle="bold"
            android:textSize="16sp" />

        <TextView
            android:id="@+id/tvScientificName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textStyle="italic"
            android:textColor="#666666" />

        <TextView
            android:id="@+id/tvCrop"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Crop: " />

        <TextView
            android:id="@+id/tvSeverity"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

    </LinearLayout>
</androidx.cardview.widget.CardView>
```

**Step 6c: Create `DiseaseAdapter.java`:**

```java
public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {
    private List<Disease> diseaseList;
    private List<Disease> filteredList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Disease disease);
    }

    public DiseaseAdapter(List<Disease> diseaseList, OnItemClickListener listener) {
        this.diseaseList = new ArrayList<>(diseaseList);
        this.filteredList = new ArrayList<>(diseaseList);
        this.listener = listener;
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
        Disease disease = filteredList.get(position);
        holder.tvCommonName.setText(disease.getCommonName());
        holder.tvScientificName.setText(disease.getScientificName());
        holder.tvCrop.setText("Crop: " + disease.getAffectedCrop());
        holder.tvSeverity.setText("Severity: " + disease.getSeverity());

        // Color severity badge
        String severity = disease.getSeverity();
        if (severity != null) {
            switch (severity) {
                case "High":
                    holder.tvSeverity.setTextColor(0xFFD32F2F); // red
                    break;
                case "Medium":
                    holder.tvSeverity.setTextColor(0xFFF57F17); // amber
                    break;
                default:
                    holder.tvSeverity.setTextColor(0xFF388E3C); // green
                    break;
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(disease));
    }

    @Override
    public int getItemCount() { return filteredList.size(); }

    /** Filter list based on search query */
    public void filter(String query) {
        filteredList.clear();
        if (query == null || query.isEmpty()) {
            filteredList.addAll(diseaseList);
        } else {
            String lower = query.toLowerCase();
            for (Disease d : diseaseList) {
                if ((d.getCommonName() != null && d.getCommonName().toLowerCase().contains(lower))
                    || (d.getAffectedCrop() != null && d.getAffectedCrop().toLowerCase().contains(lower))) {
                    filteredList.add(d);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class DiseaseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommonName, tvScientificName, tvCrop, tvSeverity;

        DiseaseViewHolder(View itemView) {
            super(itemView);
            tvCommonName = itemView.findViewById(R.id.tvCommonName);
            tvScientificName = itemView.findViewById(R.id.tvScientificName);
            tvCrop = itemView.findViewById(R.id.tvCrop);
            tvSeverity = itemView.findViewById(R.id.tvSeverity);
        }
    }
}
```

**Step 6d: Create `DiseaseLibraryActivity.java`:**

```java
public class DiseaseLibraryActivity extends AppCompatActivity {
    private static final String TAG = "DiseaseLibraryActivity";
    
    private RecyclerView rvDiseases;
    private DiseaseAdapter adapter;
    private ProgressBar progressBar; // optional

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_library);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Disease Library");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvDiseases = findViewById(R.id.rvDiseases);
        rvDiseases.setLayoutManager(new LinearLayoutManager(this));

        SearchView searchView = findViewById(R.id.searchView);

        loadDiseases(searchView);
    }

    private void loadDiseases(SearchView searchView) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                DiseaseRepository repo = DiseaseRepository.getInstance(this);
                repo.loadIfNeeded();
                List<Disease> diseases = repo.getAllDiseases();
                
                // Sort alphabetically by common name
                Collections.sort(diseases, (a, b) -> {
                    if (a.getCommonName() == null) return 1;
                    if (b.getCommonName() == null) return -1;
                    return a.getCommonName().compareTo(b.getCommonName());
                });

                runOnUiThread(() -> {
                    adapter = new DiseaseAdapter(diseases, disease -> {
                        // Navigate to detail screen (optional extension)
                        Toast.makeText(this, 
                            disease.getCommonName() + ": " + disease.getSeverity() + " severity",
                            Toast.LENGTH_SHORT).show();
                    });
                    rvDiseases.setAdapter(adapter);

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) { return false; }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            if (adapter != null) adapter.filter(newText);
                            return true;
                        }
                    });
                });
            } catch (Exception e) {
                Log.e(TAG, "Error loading diseases", e);
                runOnUiThread(() -> 
                    Toast.makeText(this, "Error loading disease library", Toast.LENGTH_SHORT).show());
            }
        });
        executor.shutdown();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
```

**Verification:**
- Activity opens from MainActivity menu or button
- All diseases listed in RecyclerView
- Search filters correctly as you type
- Tapping a disease shows Toast with name + severity
- Severity shown in correct color (red = high, amber = medium, green = none)

---

## Exercise 7: Handle XML Errors Gracefully (45 min)

What if the XML is malformed? Your app should not crash.

**Task:** Add comprehensive error handling:

```java
// In DiseaseRepository.loadIfNeeded()
public synchronized void loadIfNeeded() {
    if (isLoaded) return;
    
    try {
        InputStream is = context.getAssets().open("diseases.xml");
        DiseaseXmlParser parser = new DiseaseXmlParser();
        List<Disease> diseases = parser.parse(is);
        
        for (Disease d : diseases) {
            if (d.getLabel() != null && !d.getLabel().isEmpty()) {
                diseaseCache.put(d.getLabel(), d);
            } else {
                Log.w("DiseaseRepo", "Skipping disease with null/empty label: " + d);
            }
        }
        isLoaded = true;
        is.close();
        
    } catch (XmlPullParserException e) {
        Log.e("DiseaseRepo", "Malformed XML in diseases.xml: " + e.getMessage());
        // App continues; diseaseCache remains empty; findByLabel returns null
        isLoaded = true; // Don't retry repeatedly
        
    } catch (IOException e) {
        Log.e("DiseaseRepo", "diseases.xml not found in assets: " + e.getMessage());
        // This is a fatal developer error — XML file must be in assets
        isLoaded = true;
    }
}
```

**Task:** Intentionally break your XML (add a missing closing tag) and verify:
- App does NOT crash
- Logcat shows the XmlPullParserException message
- ResultActivity shows "Disease details not available"

---

## Exercise 8: Unit Test the Parser (1 hour)

Create `app/src/test/java/com/leafguard/DiseaseXmlParserTest.java`:

```java
public class DiseaseXmlParserTest {
    private DiseaseXmlParser parser;

    @Before
    public void setUp() {
        parser = new DiseaseXmlParser();
    }

    @Test
    public void testParseSingleDisease() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<diseases>" +
                "<disease>" +
                "<label>Test_Disease</label>" +
                "<commonName>Test Disease</commonName>" +
                "<scientificName>Testus testii</scientificName>" +
                "<affectedCrop>Tomato</affectedCrop>" +
                "<symptoms>Yellow spots</symptoms>" +
                "<treatment>Apply fungicide</treatment>" +
                "<prevention>Crop rotation</prevention>" +
                "<severity>Medium</severity>" +
                "</disease>" +
                "</diseases>";

        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        List<Disease> diseases = parser.parse(is);

        assertEquals(1, diseases.size());
        Disease d = diseases.get(0);
        assertEquals("Test_Disease", d.getLabel());
        assertEquals("Test Disease", d.getCommonName());
        assertEquals("Medium", d.getSeverity());
        assertEquals("Yellow spots", d.getSymptoms());
    }

    @Test
    public void testParseMultipleDiseases() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><diseases>" +
                "<disease><label>D1</label><commonName>Disease One</commonName>" +
                "<scientificName>S1</scientificName><affectedCrop>A1</affectedCrop>" +
                "<symptoms>S1</symptoms><treatment>T1</treatment>" +
                "<prevention>P1</prevention><severity>High</severity></disease>" +
                "<disease><label>D2</label><commonName>Disease Two</commonName>" +
                "<scientificName>S2</scientificName><affectedCrop>A2</affectedCrop>" +
                "<symptoms>S2</symptoms><treatment>T2</treatment>" +
                "<prevention>P2</prevention><severity>Low</severity></disease>" +
                "</diseases>";

        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        List<Disease> diseases = parser.parse(is);

        assertEquals(2, diseases.size());
        assertEquals("D1", diseases.get(0).getLabel());
        assertEquals("D2", diseases.get(1).getLabel());
    }

    @Test
    public void testParseEmptyDiseases() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><diseases></diseases>";
        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        List<Disease> diseases = parser.parse(is);
        assertTrue(diseases.isEmpty());
    }

    @Test(expected = XmlPullParserException.class)
    public void testMalformedXmlThrowsException() throws Exception {
        String badXml = "<?xml version=\"1.0\"?><diseases><disease><label>Test</diseases>";
        InputStream is = new ByteArrayInputStream(badXml.getBytes(StandardCharsets.UTF_8));
        parser.parse(is); // should throw XmlPullParserException
    }
}
```

**Verification:**
- Run with `Run > Run 'DiseaseXmlParserTest'` (right-click the test class)
- All 4 tests pass (green)
- If malformedXmlThrowsException fails, your parser swallows exceptions — fix it

---

## Summary of Week 08 Deliverables

| # | File/Class | Status |
|---|-----------|--------|
| 1 | `assets/diseases.xml` (10+ diseases) | ☐ |
| 2 | `Disease.java` (model class) | ☐ |
| 3 | `DiseaseXmlParser.java` (XmlPullParser) | ☐ |
| 4 | `DiseaseRepository.java` (singleton + cache) | ☐ |
| 5 | `DiseaseAdapter.java` (RecyclerView) | ☐ |
| 6 | `DiseaseLibraryActivity.java` (with search) | ☐ |
| 7 | ResultActivity shows XML-sourced disease info | ☐ |
| 8 | Unit tests (4 passing) | ☐ |

**Total time budget: ~8 hours**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 08 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| **3** | **exercises.md** ← *You are here* | **Practice Exercises** |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Theory & Learning Notes](learning-notes.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Practice Exercises** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Build Implementation Guide →](build-task.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 07: Room Database & History](../week-07-room-sqlite-history/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 09: TensorFlow Lite Offline AI ➡](../week-09-tensorflow-lite-offline-ai/README.md) |

---
