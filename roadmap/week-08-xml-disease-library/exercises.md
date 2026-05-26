# Week 08: Exercises - XML Disease Library

## Exercise 1: Create XML File (1 hour)

Create `assets/disease_library.xml` with 3 diseases:

1. Tomato Late Blight
2. Potato Early Blight  
3. Apple Scab

Each must have: label, commonName, symptoms, treatment, prevention.

**Verification:** XML file is well-formed, opens in browser without errors.

---

## Exercise 2: Implement Disease Model (30 min)

Create `Disease.java` with fields matching XML structure.

Add constructor, getters, setters.

**Verification:** Class compiles, can create Disease objects manually.

---

## Exercise 3: Basic XML Parser (2 hours)

Create `DiseaseXmlParser.java` with method:

```java
public List<Disease> parse(InputStream is) throws Exception
```

Use XmlPullParser to extract all diseases.

**Verification:** Parser returns list with correct disease count and data.

---

## Exercise 4: Test Parser (30 min)

In MainActivity, test parser:

```java
InputStream is = getAssets().open("disease_library.xml");
List<Disease> diseases = parser.parse(is);
Log.d(TAG, "Loaded " + diseases.size() + " diseases");
```

**Verification:** Logcat shows correct disease count.

---

## Exercise 5: Disease Lookup by Label (1 hour)

Add method to find disease by label:

```java
public Disease findByLabel(String label, List<Disease> diseases)
```

Test with your model's prediction label.

**Verification:** Returns correct disease object or null.

---

## Exercise 6: Display in List (1 hour)

Create DiseaseLibraryActivity with RecyclerView showing all diseases.

**Verification:** List displays with disease names visible.

---

**Total Time:** 6 hours
