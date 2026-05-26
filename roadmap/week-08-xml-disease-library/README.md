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

## Prerequisites

- Week 06 complete (know your ML model labels)
- Understanding of XML structure
- Basic file I/O concepts

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
    </disease>
</diseases>
```

### XmlPullParser Flow

1. Open XML file from assets
2. Get XmlPullParser instance
3. Loop through events (START_TAG, END_TAG, TEXT)
4. Extract data when inside target tags
5. Build Disease objects
6. Return list

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

**Next:** Open `learning-notes.md` for detailed XML parsing concepts.
