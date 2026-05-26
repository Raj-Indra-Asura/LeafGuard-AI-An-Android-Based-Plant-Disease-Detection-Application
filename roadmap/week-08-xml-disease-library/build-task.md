# Week 08 Build Task: Implement XML Disease Library

## Step 1: Create XML File (2 hours)

Create `app/src/main/assets/disease_library.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<diseases>
    <disease>
        <label>Tomato_Late_Blight</label>
        <commonName>Tomato Late Blight</commonName>
        <scientificName>Phytophthora infestans</scientificName>
        <symptoms>Water-soaked spots on leaves, white mold growth on leaf undersides, brown lesions on stems</symptoms>
        <treatment>Remove infected plants immediately. Apply copper-based fungicide. Improve air circulation.</treatment>
        <prevention>Use disease-resistant varieties. Avoid overhead watering. Rotate crops annually.</prevention>
    </disease>
    
    <!-- Add 5 more disease entries -->
</diseases>
```

Research and add information for 5 more diseases your model detects.

---

## Step 2: Create Disease Model (30 min)

Create `Disease.java`:

```java
public class Disease {
    private String label;
    private String commonName;
    private String scientificName;
    private String symptoms;
    private String treatment;
    private String prevention;

    // Constructor
    public Disease() {}

    // Getters and setters
    // TODO: Generate using IDE
}
```

---

## Step 3: Implement XML Parser (2 hours)

Create `DiseaseXmlParser.java`:

```java
public class DiseaseXmlParser {
    
    public List<Disease> parse(InputStream is) throws XmlPullParserException, IOException {
        List<Disease> diseases = new ArrayList<>();
        
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(is, "UTF-8");
        
        Disease currentDisease = null;
        String currentTag = null;
        int eventType = parser.getEventType();
        
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                currentTag = parser.getName();
                if ("disease".equals(currentTag)) {
                    currentDisease = new Disease();
                }
            } else if (eventType == XmlPullParser.TEXT && currentDisease != null) {
                String text = parser.getText().trim();
                if (!text.isEmpty()) {
                    switch (currentTag) {
                        case "label":
                            currentDisease.setLabel(text);
                            break;
                        case "commonName":
                            currentDisease.setCommonName(text);
                            break;
                        // TODO: Add cases for other fields
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if ("disease".equals(parser.getName())) {
                    diseases.add(currentDisease);
                    currentDisease = null;
                }
            }
            
            eventType = parser.next();
        }
        
        return diseases;
    }
}
```

---

## Step 4: Test Parser (30 min)

In MainActivity:

```java
try {
    InputStream is = getAssets().open("disease_library.xml");
    DiseaseXmlParser parser = new DiseaseXmlParser();
    List<Disease> diseases = parser.parse(is);
    
    Toast.makeText(this, "Loaded " + diseases.size() + " diseases", Toast.LENGTH_SHORT).show();
    
    for (Disease d : diseases) {
        Log.d("XML_TEST", "Disease: " + d.getCommonName());
    }
} catch (Exception e) {
    Log.e("XML_TEST", "Error parsing XML", e);
}
```

---

## Step 5: Integrate with Prediction (1 hour)

In ResultActivity, after prediction:

```java
private void loadDiseaseDetails(String predictedLabel) {
    try {
        InputStream is = getAssets().open("disease_library.xml");
        DiseaseXmlParser parser = new DiseaseXmlParser();
        List<Disease> diseases = parser.parse(is);
        
        Disease disease = findDiseaseByLabel(predictedLabel, diseases);
        if (disease != null) {
            symptomsTextView.setText(disease.getSymptoms());
            treatmentTextView.setText(disease.getTreatment());
            preventionTextView.setText(disease.getPrevention());
        }
    } catch (Exception e) {
        Log.e(TAG, "Error loading disease details", e);
    }
}

private Disease findDiseaseByLabel(String label, List<Disease> diseases) {
    for (Disease d : diseases) {
        if (d.getLabel().equals(label)) {
            return d;
        }
    }
    return null;
}
```

---

## Step 6: Create Disease Library Activity (2 hours)

Create `DiseaseLibraryActivity.java` and `activity_disease_library.xml`.

Display all diseases in RecyclerView.

---

## Completion Checklist

- [ ] disease_library.xml created with 6+ diseases
- [ ] XML is well-formed
- [ ] Disease model class complete
- [ ] Parser successfully extracts all data
- [ ] Prediction results show XML data
- [ ] Disease library activity displays list
- [ ] No crashes on XML errors
- [ ] Works offline

**Test:** Disconnect internet, perform prediction, verify detailed info displays.
