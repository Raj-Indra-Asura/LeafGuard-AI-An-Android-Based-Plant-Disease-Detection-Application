# Week 08 Interactive Notebook

## Building the Disease Knowledge Base

> Work through this Markdown notebook like a lab manual: read, run, test, and explain each checkpoint in your own words.

### How to use this notebook

- Follow the cells in order.
- Use Java for Android code and Python only for backend/model tooling.
- Save screenshots and logs as evidence for CSE 2206.
- Keep the roadmap folder for this week open while you work.

### Weekly outcomes

- Design an XML file that stores disease explanations for LeafGuard AI.
- Parse the XML with `XmlPullParser` and display the data in a library screen.
- Connect prediction results to detailed disease knowledge.

### Repository references

- `roadmap/week-08-xml-disease-library/`
- `solutions/week-08/`
- `android-app/app/src/main/res/`

---

## Notebook Cell 1 — Design the XML structure

### Explanation

- XML is a good fit for static structured content that ships with the app.
- For LeafGuard AI, each disease entry can store a name, crop, symptoms, treatment, and prevention.

### Code to Read / Run

```xml
<diseases>
    <disease id="tomato_early_blight">
        <name>Tomato Early Blight</name>
        <crop>Tomato</crop>
        <symptoms>Brown spots with concentric rings on older leaves.</symptoms>
        <treatment>Remove infected leaves and apply labeled fungicide.</treatment>
        <prevention>Rotate crops and avoid evening overhead watering.</prevention>
    </disease>
</diseases>
```

### 🔵 Try This

- Explain why each disease entry should have a stable `id` attribute.

### Expected Output

- You have a simple and readable schema for disease content.

### ✅ Checkpoint

- Why is XML acceptable here even though JSON also exists?

### ⚠️ Common Mistake

- Do not invent inconsistent tag names across entries.

### 📌 Key Point

- A consistent data format makes parsing simpler.

## Notebook Cell 2 — Create a complete disease_library.xml with 10 diseases

### Explanation

- Start with a manageable number of diseases before scaling up to all labels.

### Code to Read / Run

```xml
<?xml version="1.0" encoding="utf-8"?>
<diseases>
    <disease id="apple_scab"><name>Apple Scab</name><crop>Apple</crop><symptoms>Olive-brown leaf lesions.</symptoms><treatment>Prune and apply protectant fungicide.</treatment><prevention>Sanitize fallen leaves.</prevention></disease>
    <disease id="apple_black_rot"><name>Apple Black Rot</name><crop>Apple</crop><symptoms>Dark circular spots and fruit rot.</symptoms><treatment>Remove infected fruit and branches.</treatment><prevention>Improve orchard sanitation.</prevention></disease>
    <disease id="corn_gray_leaf_spot"><name>Corn Gray Leaf Spot</name><crop>Corn</crop><symptoms>Rectangular gray lesions between veins.</symptoms><treatment>Use resistant hybrids and fungicide if needed.</treatment><prevention>Rotate crops and manage residue.</prevention></disease>
    <disease id="corn_northern_leaf_blight"><name>Corn Northern Leaf Blight</name><crop>Corn</crop><symptoms>Long cigar-shaped gray-green lesions.</symptoms><treatment>Apply fungicide under high disease pressure.</treatment><prevention>Choose resistant seed.</prevention></disease>
    <disease id="potato_early_blight"><name>Potato Early Blight</name><crop>Potato</crop><symptoms>Target-like spots on older leaves.</symptoms><treatment>Prune affected leaves and use fungicide.</treatment><prevention>Rotate away from solanaceous crops.</prevention></disease>
    <disease id="potato_late_blight"><name>Potato Late Blight</name><crop>Potato</crop><symptoms>Rapid dark lesions with possible white growth.</symptoms><treatment>Remove infected tissue immediately.</treatment><prevention>Avoid wet foliage and monitor humidity.</prevention></disease>
    <disease id="tomato_bacterial_spot"><name>Tomato Bacterial Spot</name><crop>Tomato</crop><symptoms>Small water-soaked spots on leaves.</symptoms><treatment>Remove infected foliage and use copper spray if appropriate.</treatment><prevention>Use clean seed and disinfect tools.</prevention></disease>
    <disease id="tomato_early_blight"><name>Tomato Early Blight</name><crop>Tomato</crop><symptoms>Brown concentric ring lesions on older leaves.</symptoms><treatment>Remove infected leaves and improve airflow.</treatment><prevention>Mulch soil and rotate crops.</prevention></disease>
    <disease id="tomato_late_blight"><name>Tomato Late Blight</name><crop>Tomato</crop><symptoms>Dark water-soaked lesions and fast collapse.</symptoms><treatment>Isolate infected plants and treat quickly.</treatment><prevention>Avoid overhead irrigation in humid periods.</prevention></disease>
    <disease id="tomato_healthy"><name>Tomato Healthy</name><crop>Tomato</crop><symptoms>No disease symptoms visible.</symptoms><treatment>No treatment required.</treatment><prevention>Continue balanced care and monitoring.</prevention></disease>
</diseases>
```

### 🔵 Try This

- Move this file into `res/xml/` or `assets/` depending on your parser approach.

### Expected Output

- You now have a real knowledge base file rather than hardcoded scattered strings.

### ✅ Checkpoint

- Why might XML be easier to edit for non-programmers than Java source files?

### ⚠️ Common Mistake

- Avoid malformed XML; one missing closing tag can break the parser.

### 📌 Key Point

- Static knowledge should live in a structured resource file.

## Notebook Cell 3 — Parse the XML with XmlPullParser

### Explanation

- XmlPullParser is efficient and available in Android for streaming XML parsing.

### Code to Read / Run

```java
public class DiseaseXmlParser {

    public List<DiseaseInfo> parse(InputStream inputStream) throws Exception {
        List<DiseaseInfo> diseases = new ArrayList<>();
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, null);

        DiseaseInfo currentDisease = null;
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();

            if (eventType == XmlPullParser.START_TAG) {
                if ("disease".equals(tagName)) {
                    currentDisease = new DiseaseInfo();
                    currentDisease.setId(parser.getAttributeValue(null, "id"));
                } else if (currentDisease != null) {
                    switch (tagName) {
                        case "name": currentDisease.setName(parser.nextText()); break;
                        case "crop": currentDisease.setCrop(parser.nextText()); break;
                        case "symptoms": currentDisease.setSymptoms(parser.nextText()); break;
                        case "treatment": currentDisease.setTreatment(parser.nextText()); break;
                        case "prevention": currentDisease.setPrevention(parser.nextText()); break;
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG && "disease".equals(tagName) && currentDisease != null) {
                diseases.add(currentDisease);
                currentDisease = null;
            }
            eventType = parser.next();
        }
        return diseases;
    }
}
```

### 🔵 Try This

- Step through the parser in the debugger and watch how `currentDisease` changes.

### Expected Output

- The parser returns a `List<DiseaseInfo>` with all 10 diseases.

### ✅ Checkpoint

- Why is `nextText()` safe only when you know the current tag contains text content?

### ⚠️ Common Mistake

- Do not forget to reset `currentDisease` after finishing one entry.

### 📌 Key Point

- Parsing is easier when your XML schema is consistent.

## Notebook Cell 4 — Cache parsed data in memory

### Explanation

- Static XML should not be parsed repeatedly every time the user opens the library.

### Code to Read / Run

```java
public class DiseaseRepository {
    private static List<DiseaseInfo> cachedDiseases;

    public static List<DiseaseInfo> getDiseases(Context context) {
        if (cachedDiseases == null) {
            try (InputStream inputStream = context.getResources().openRawResource(R.raw.disease_library)) {
                cachedDiseases = new DiseaseXmlParser().parse(inputStream);
            } catch (Exception exception) {
                cachedDiseases = new ArrayList<>();
            }
        }
        return cachedDiseases;
    }
}
```

### 🔵 Try This

- Add a log line the first time parsing occurs and confirm it does not repeat every open.

### Expected Output

- The knowledge base loads once and becomes fast to access afterward.

### ✅ Checkpoint

- Why does simple caching improve user experience here?

### ⚠️ Common Mistake

- Do not silently swallow parsing problems in production without at least logging them.

### 📌 Key Point

- Not every optimization is complex; caching a static resource is a practical win.

## Notebook Cell 5 — Build DiseaseLibraryActivity

### Explanation

- The library screen lets users explore disease information even without scanning a new image.

### Code to Read / Run

```java
public class DiseaseLibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_library);

        RecyclerView recyclerView = findViewById(R.id.libraryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<DiseaseInfo> diseases = DiseaseRepository.getDiseases(this);
        recyclerView.setAdapter(new DiseaseLibraryAdapter(diseases));
    }
}
```

### 🔵 Try This

- Add a search bar later if you want an extension challenge.

### Expected Output

- The user can scroll through disease entries and read symptoms/treatment/prevention.

### ✅ Checkpoint

- How does this screen support the educational value of LeafGuard AI?

### ⚠️ Common Mistake

- Do not make the knowledge base screen depend on a live network connection if the data is bundled locally.

### 📌 Key Point

- LeafGuard AI is not just a classifier; it is also an information tool.

## Notebook Cell 6 — Connect disease info to prediction results

### Explanation

- When a prediction is made, the app should show related library information immediately.

### Code to Read / Run

```java
DiseaseInfo info = DiseaseMapper.findByPredictionName(prediction.getDisease(), DiseaseRepository.getDiseases(this));
if (info != null) {
    symptomsText.setText(info.getSymptoms());
    treatmentText.setText(info.getTreatment());
    preventionText.setText(info.getPrevention());
}
```

### 🔵 Try This

- Normalize naming differences such as underscores or capitalization between model labels and XML names.

### Expected Output

- Prediction results now feel richer because the app explains the disease.

### ✅ Checkpoint

- What happens if a prediction name does not match any XML entry?

### ⚠️ Common Mistake

- Do not assume label strings will always match perfectly; build a mapping layer if needed.

### 📌 Key Point

- A mapping layer protects you from naming inconsistencies.

## Notebook Cell 7 — Validate the working disease library

### Explanation

- Week 08 is successful when both browsing and result-driven lookup work.

### Step-by-Step

1. Open DiseaseLibraryActivity from the main menu.
2. Confirm all 10 entries appear.
3. Run a prediction and check whether related info is shown on the result screen.

### 🔵 Try This

- Add one deliberately broken XML tag in a test branch to see how the parser fails, then fix it.

### Expected Output

- Disease details appear from static XML data.

### ✅ Checkpoint

- Can you explain the full flow from XML file to on-screen disease card?

### ⚠️ Common Mistake

- Do not trust the parser until you test with malformed input at least once during learning.

### 📌 Key Point

- Structured static content can be a major app feature.

## Mini Quiz

- What problem does this week solve inside LeafGuard AI?
- Which Java class or Android component did you touch first?
- Which file path in this repository is most relevant to this week?
- What would break if you skipped the validation step?
- How does this week connect to the three-tier architecture?

## Evidence Checklist

- [ ] Capture a screenshot of the completed screen or terminal output.
- [ ] Save one code snippet that proves the feature is wired correctly.
- [ ] Write two sentences in your progress log about what you learned.
- [ ] Record at least one bug and the exact fix you applied.
- [ ] Commit working changes before moving to the next week.

## Reflection Prompt

- Explain the feature from memory without reading the code.
- State one improvement you would add after submission.
- Identify one risk if this feature were left untested.

## Next Step

- Continue to **[Week 09: TensorFlow Lite Offline AI](../../roadmap/week-09-tensorflow-lite-offline-ai/README.md)** when this week is stable and documented.


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

### Related Roadmap Materials
- 📖 [Week 08 README](../../roadmap/week-08-xml-disease-library/README.md) — Week overview & objectives
- 📝 [Week 08 Exercises](../../roadmap/week-08-xml-disease-library/exercises.md) — Practice problems
- 💡 [Week 08 Solutions](../../solutions/week-08/README.md) — Reference solutions
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

### Week Progression

| ← Previous | 🏠 | Next → |
|:-----------|:--:|-------:|
| [⬅ Week 07 Notebooks](../week-07/README.md) | [Notebooks Index](../README.md) | [Week 09 Notebooks ➡](../week-09/README.md) |

---
