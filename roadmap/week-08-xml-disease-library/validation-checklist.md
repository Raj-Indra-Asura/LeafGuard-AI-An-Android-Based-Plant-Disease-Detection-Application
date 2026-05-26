# Week 08: Validation Checklist

## XML File (20 points)

- [ ] disease_library.xml exists in assets/
- [ ] Contains 6 or more disease entries
- [ ] XML is well-formed (validated)
- [ ] All diseases have label field
- [ ] All diseases have commonName
- [ ] All diseases have symptoms
- [ ] All diseases have treatment
- [ ] All diseases have prevention
- [ ] Labels match model predictions
- [ ] Information is accurate and complete

## Disease Model (10 points)

- [ ] Disease.java class exists
- [ ] All fields present
- [ ] Constructor implemented
- [ ] All getters implemented
- [ ] All setters implemented

## XML Parser (25 points)

- [ ] DiseaseXmlParser class exists
- [ ] parse() method implemented
- [ ] Uses XmlPullParser
- [ ] Correctly handles START_TAG
- [ ] Correctly handles TEXT
- [ ] Correctly handles END_TAG
- [ ] Returns List<Disease>
- [ ] All XML fields extracted
- [ ] No crashes on valid XML
- [ ] Handles parse errors gracefully

## Integration (20 points)

- [ ] Parser called in app
- [ ] Diseases loaded successfully
- [ ] Prediction results enhanced with XML data
- [ ] Symptoms from XML displayed
- [ ] Treatment from XML displayed
- [ ] Prevention from XML displayed
- [ ] Label matching works correctly
- [ ] Fallback for unknown labels

## Disease Library UI (15 points)

- [ ] DiseaseLibraryActivity exists
- [ ] Displays all diseases
- [ ] RecyclerView working
- [ ] Disease names visible
- [ ] Can tap to see details
- [ ] Back button works

## Error Handling (10 points)

- [ ] Try-catch around parsing
- [ ] Logs errors appropriately
- [ ] App doesn't crash on XML error
- [ ] User sees error message
- [ ] Graceful degradation

---

**Score:** _____ / 100

**Pass:** ≥85

**Status:** [ ] PASS | [ ] FAIL
