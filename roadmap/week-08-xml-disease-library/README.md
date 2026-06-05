# Week 08: XML Disease Library

## Weekly Objective

By the end of Week 08, you will create and parse an XML-based disease library that provides detailed information for each plant disease the app can detect.

**Measurable Outcomes:**
- disease_library.xml file in assets/ with 6+ diseases
- Disease model class with all attributes
- XML parser implementation using XmlPullParser
- DiseaseLibraryActivity displaying all diseases
- Integration with prediction results for enhanced information
- Search/filter functionality working
- Complete offline operation

---

## Why This Week Matters

**CSE 2206 Syllabus Requirement:** XML Parsing is explicitly required. This week satisfies that requirement while adding valuable functionality.

**Viva Question:** "Show me your XML parsing implementation and explain how it works."

---

## Syllabus Topics

1. **XML Parsing** - XmlPullParser, reading from assets
2. **Data Modeling** - Disease class, object structure
3. **Offline Storage** - Assets folder usage
4. **List Display** - RecyclerView with filtered data

---

## Key Concepts

### XML Structure

```xml
<?xml version="1.0" encoding="UTF-8"?>
<diseases>
    <disease>
        <label>Tomato_Late_Blight</label>
        <commonName>Tomato Late Blight</commonName>
        <symptoms>Water-soaked spots, white mold</symptoms>
        <treatment>Apply copper fungicide</treatment>
        <prevention>Use resistant varieties</prevention>
        <severity>High</severity>
    </disease>
</diseases>
```

### XmlPullParser Architecture

```
assets/disease_library.xml
         |
         v
AssetManager.open("disease_library.xml")  → InputStream
         |
         v
Xml.newPullParser()  → XmlPullParser instance
         |
         v
Loop: parser.next()
   START_TAG "disease"  → create new Disease object
   START_TAG "label"    → read text → disease.setLabel(text)
   START_TAG "symptoms" → read text → disease.setSymptoms(text)
   ...
   END_TAG "disease"    → add Disease to list
         |
         v
Return List<Disease>
```

### XmlPullParser Flow

1. Open XML file from assets
2. Get XmlPullParser instance
3. Loop through events (START_TAG, END_TAG, TEXT)
4. Extract data when inside target tags
5. Build Disease objects
6. Return list

### Complete Parser Implementation

```java
public class DiseaseXmlParser {

    public List<Disease> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        List<Disease> diseases = new ArrayList<>();
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);

        int eventType = parser.getEventType();
        Disease currentDisease = null;
        String currentTag = null;

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
                        String text = parser.getText().trim();
                        switch (currentTag) {
                            case "label":      currentDisease.setLabel(text);      break;
                            case "commonName": currentDisease.setCommonName(text); break;
                            case "symptoms":   currentDisease.setSymptoms(text);   break;
                            case "treatment":  currentDisease.setTreatment(text);  break;
                            case "prevention": currentDisease.setPrevention(text); break;
                            case "severity":   currentDisease.setSeverity(text);   break;
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("disease".equals(parser.getName()) && currentDisease != null) {
                        diseases.add(currentDisease);
                        currentDisease = null;
                    }
                    currentTag = null;
                    break;
            }
            eventType = parser.next();
        }
        return diseases;
    }
}
```

### Lookup Integration with Predictions

After the model returns a label (e.g., `"Tomato_Late_Blight"`), look up the Disease object to show enriched information:

```java
// In ResultActivity
Map<String, Disease> diseaseMap = buildDiseaseMap(parseXml());

Disease info = diseaseMap.get(predictionLabel);
if (info != null) {
    tvSymptoms.setText(info.getSymptoms());
    tvTreatment.setText(info.getTreatment());
    tvPrevention.setText(info.getPrevention());
} else {
    tvSymptoms.setText("Information not found in disease library.");
}
```

---

## Prerequisites

- Week 06 complete (know your ML model labels)
- Understanding of XML structure
- Basic file I/O concepts

---

## Weekly Timeline

- **Day 1-2:** Create XML file with 6+ diseases (4h)
- **Day 3-4:** Implement XML parser (4h)  
- **Day 5:** Integrate with predictions (3h)
- **Day 6:** Build Disease Library UI (3h)
- **Day 7:** Testing and documentation (2h)

---

## Validation Criteria

- [ ] disease_library.xml exists with well-formed XML
- [ ] All 6+ diseases have complete information
- [ ] XML parser successfully extracts all fields
- [ ] No crashes on parse errors
- [ ] Prediction results enhanced with XML data
- [ ] Disease library screen displays all diseases
- [ ] Search functionality works
- [ ] Works offline

---

**Next:** Open `learning-notes.md` for detailed XML parsing concepts (1,822 lines).
