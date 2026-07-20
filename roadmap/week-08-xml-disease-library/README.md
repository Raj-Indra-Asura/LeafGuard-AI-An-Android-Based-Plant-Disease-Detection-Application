# Week 08: XML Disease Library

## What you'll learn & why

This week you build an offline encyclopedia of the 10 plant diseases the app knows, so a user can read symptoms, treatment, and prevention even with no internet. The information lives in a plain text file called `assets/diseases.xml`, and you read it with a small tool called `XmlPullParser` inside `DiseaseLibraryActivity`. **XML (eXtensible Markup Language)** is just a way to write structured data using named tags in angle brackets — like labelled boxes inside boxes — so both humans and programs can read it. This matters because bundling reference data as a file (instead of hard-coding it) keeps the app easy to update and works completely offline.

## New words this week

- **XML (eXtensible Markup Language)** — a text format that stores data inside named tags such as `<name>...</name>`; tags can nest to group related information. (See [Glossary](../../GLOSSARY.md).)
- **XmlPullParser** — the small Android tool that walks through an XML file one tag at a time and hands you each piece of text; `DiseaseLibraryActivity` uses it. (See [Glossary](../../GLOSSARY.md).)
- **Assets folder** — `app/src/main/assets/` holds raw files (like `diseases.xml`) packaged inside the app and opened with `assets.open("diseases.xml")`. It is **not** the `res/` folder.

> **The real file (match this exactly):** The committed library is `app/src/main/assets/diseases.xml` (in `assets/`, filename `diseases.xml`). Each `<disease>` uses these tags: `<name>`, `<plant>`, `<symptoms>`, `<treatment>`, `<prevention>`. There are **10** `<disease>` entries and each `<name>` must match a line in `assets/labels.txt` **character-for-character**, because the model's predicted label is used to look up its disease card. The richer schema (with `<commonName>`/`<severity>`) shown later in this week is an *optional extension* — the shipped app parses the five tags above.

## Where to practice this week

- Kotlin Android practice (primary): [`../../exercises/android-kotlin/`](../../exercises/android-kotlin/)
- Java Android practice (secondary): [`../../exercises/android/`](../../exercises/android/)
- Worked answers: [`../../solutions/week-08/`](../../solutions/week-08/)
- Notebook walkthrough: [`../../notebooks/week-08/`](../../notebooks/week-08/)

## Repository State After Week 08

Week 08 keeps the saved scan history and adds an offline disease knowledge base. The repository now contains structured reference data that the app can browse and use to explain predictions.

### Structure to browse after this week

- `android-app-kotlin/app/src/main/assets/diseases.xml` contains the disease library packaged with the app.
- `android-app-kotlin/app/src/main/assets/labels.txt` must stay aligned with the disease names when prediction labels are used for lookup.
- `android-app-kotlin/app/src/main/java/com/leafguard/DiseaseLibraryActivity.kt` parses and displays the disease library.
- `android-app-kotlin/app/src/main/res/layout/activity_disease_library.xml` defines the library screen.
- `android-app-kotlin/app/src/main/res/layout/item_disease_library.xml` defines one disease row or card.
- `ResultActivity.kt` and `HistoryDetailActivity.kt` can use the parsed library to show symptoms, treatment, and prevention.
- The Java twin should include the same `assets/diseases.xml` and equivalent parser behavior.

### Files you should create or update this week

- `app/src/main/assets/diseases.xml`.
- `DiseaseLibraryActivity.kt`.
- `activity_disease_library.xml` and `item_disease_library.xml`.
- `ResultActivity.kt` or `HistoryDetailActivity.kt` if prediction results are enriched from XML.
- `strings.xml` for library labels, empty states, and search text.
- `docs/evidence/week-08/` screenshots showing library browsing, search, and result guidance.

### What this repository state can do

- Bundle disease information inside the app without needing internet.
- Parse XML using Android file I/O and `XmlPullParser`.
- Show a browsable disease library.
- Connect prediction labels to symptoms, treatment, and prevention text when names match.

### What this repository state cannot do

- It cannot run the ML model on the phone yet.
- It cannot diagnose without the backend unless Week 09 offline AI is added.
- It cannot notify, share, or attach location yet.
- It still depends on correct label-to-disease name matching.

---

## Weekly Objective

By the end of Week 08, you will create and parse an XML-based disease library that provides detailed information for each plant disease the app can detect.

**Measurable Outcomes:**
- diseases.xml file in assets/ with 6+ diseases
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

The shipped `assets/diseases.xml` uses these five tags per disease. Each `<name>` matches a line in `labels.txt`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<diseases>
    <disease>
        <name>Tomato Late Blight</name>
        <plant>Tomato</plant>
        <symptoms>Water-soaked spots, white mold</symptoms>
        <treatment>Apply copper fungicide</treatment>
        <prevention>Use resistant varieties</prevention>
    </disease>
</diseases>
```

### XmlPullParser Architecture

`DiseaseLibraryActivity` opens the file from `assets/` and reads it tag-by-tag:

```
assets/diseases.xml
         |
         v
assets.open("diseases.xml")  → InputStream
         |
         v
XmlPullParserFactory.newInstance().newPullParser()  → XmlPullParser instance
         |
         v
Loop: parser.next()
   START_TAG "disease"  → create new DiseaseEntry object
   START_TAG "name"     → read text → entry.name = text
   START_TAG "plant"    → read text → entry.plant = text
   START_TAG "symptoms" → read text → entry.symptoms = text
   ...
   END_TAG "disease"    → add DiseaseEntry to list
         |
         v
Return List<DiseaseEntry>
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

- [ ] diseases.xml exists with well-formed XML
- [ ] All 6+ diseases have complete information
- [ ] XML parser successfully extracts all fields
- [ ] No crashes on parse errors
- [ ] Prediction results enhanced with XML data
- [ ] Disease library screen displays all diseases
- [ ] Search functionality works
- [ ] Works offline

---

**Next:** Open `learning-notes.md` for detailed XML parsing concepts (1,822 lines).


<!-- NAV_FOOTER_START -->

---

## 📈 Product State After This Week

**Cumulative product completion: 72%** *(official model: [PRODUCT_PROGRESS_MAP.md](../../PRODUCT_PROGRESS_MAP.md))*

- **Your app can now…** show symptoms, treatment, and prevention for every predicted disease, and browse a full 10-disease encyclopedia parsed from `assets/diseases.xml`.
- **Your app still cannot…** work without internet — predictions still require the cloud backend. Week 09 brings the AI on-device.
- **Applies equally to both tracks:** Kotlin (`android-app-kotlin/`, primary) and Java (`android-app/`, secondary).

---

## 📚 Week 08 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| **1** | **README.md** ← *You are here* | **Week Overview & Objectives** |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

*(Start of week)* &nbsp;&nbsp;|&nbsp;&nbsp; **Week Overview & Objectives** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Theory & Learning Notes →](learning-notes.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 07: Room Database & History](../week-07-room-sqlite-history/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 09: TensorFlow Lite Offline AI ➡](../week-09-tensorflow-lite-offline-ai/README.md) |

---
