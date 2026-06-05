# Week 08 Quiz - XML Disease Library

## How to Use This Quiz

This quiz checks both **conceptual understanding** and **practical Java/XML implementation skills** for Week 08.
There are **40 questions total**:

- **20 conceptual questions**
- **20 practical/code questions**

Try answering first without looking at the notes.
Then review the explanations carefully.

---

## Part A - Conceptual Questions (20)

### Q1. What is the main purpose of the Week 08 XML disease library in LeafGuard AI?

A. To replace the ML model completely
B. To store detailed disease information offline
C. To make network requests faster
D. To store user passwords

**Answer:** B

**Explanation:** The XML library supplements the ML prediction by providing offline disease details such as symptoms, treatment, and prevention.

### Q2. Why must the XML `label` match the PlantVillage model output exactly?

A. Because Android Studio requires it
B. Because the repository uses the label as the lookup key
C. Because XML tags are case-insensitive
D. Because RecyclerView cannot display other strings

**Answer:** B

**Explanation:** If the prediction label and XML label differ, `findByLabel()` will not return the correct disease object.

### Q3. Which parser is most appropriate for this Android course task?

A. DOM
B. XmlPullParser
C. CSVReader
D. Gson

**Answer:** B

**Explanation:** XmlPullParser is lightweight, Android-friendly, and ideal for event-based parsing in Java.

### Q4. What does the `assets/` folder provide for this feature?

A. Encrypted storage only
B. A place to store local raw files packaged with the app
C. A database table
D. A replacement for drawables

**Answer:** B

**Explanation:** The XML file is packaged with the app in `assets/` and can be opened offline using `getAssets().open()`.

### Q5. Why is a `Map<String, Disease>` useful in the repository?

A. It stores images directly
B. It allows fast lookup by label
C. It replaces RecyclerView
D. It automatically sorts diseases

**Answer:** B

**Explanation:** A map gives direct access by key, which is ideal when the ML model returns one predicted label.

### Q6. Why should XML parsing usually not happen on the UI thread?

A. Because XML cannot be parsed in Java
B. Because parsing may block rendering and make the app feel slow
C. Because Activities are not allowed to read files
D. Because RecyclerView requires network access

**Answer:** B

**Explanation:** File I/O and parsing can delay the main thread, so background loading is better practice.

### Q7. What is the role of `DiseaseRepository`?

A. Draw list items on screen
B. Abstract XML loading and provide cached access to diseases
C. Replace the XML file with SQLite automatically
D. Store only drawables

**Answer:** B

**Explanation:** The repository separates data access from UI code and manages caching.

### Q8. Which of the following is a good fallback when a disease label is missing from XML?

A. Crash the app immediately
B. Show a friendly message and keep the result screen usable
C. Delete the XML file
D. Return random disease data

**Answer:** B

**Explanation:** Graceful fallback protects user experience and avoids crashes.

### Q9. How are repeated symptoms best represented in XML?

A. As one attribute with commas only
B. As nested `<item>` tags inside `<symptoms>`
C. As drawable files
D. As comments

**Answer:** B

**Explanation:** Nested `<item>` tags map naturally to `List<String>` in Java.

### Q10. What is the main advantage of using a singleton repository here?

A. Unlimited memory
B. A shared cache that avoids repeated parsing during one app session
C. Automatic network sync
D. No need for Java classes

**Answer:** B

**Explanation:** One shared repository instance can load once and serve many screens.

### Q11. What is a strong reason to keep the parser separate from the Activity?

A. Activities cannot contain Java code
B. It improves separation of concerns and testability
C. Android forbids file parsing in Activities
D. It removes the need for layouts

**Answer:** B

**Explanation:** Keeping parsing logic in its own class makes the app easier to maintain and test.

### Q12. Why should healthy classes also appear in the XML disease library?

A. They are needed so the app can explain healthy predictions too
B. Android requires all XML files to contain healthy tags
C. Healthy labels cannot be predicted otherwise
D. To reduce APK size

**Answer:** A

**Explanation:** A healthy prediction still needs meaningful detail text such as monitoring and routine care advice.

### Q13. Which statement best compares DOM and XmlPullParser?

A. DOM uses less memory than XmlPullParser
B. XmlPullParser reads the whole file before parsing
C. DOM builds an in-memory tree while XmlPullParser reads events sequentially
D. They are identical

**Answer:** C

**Explanation:** DOM creates a full document tree, while XmlPullParser moves through the file event by event.

### Q14. Why is `getApplicationContext()` preferred inside a singleton repository?

A. It is shorter to type
B. It avoids leaking an Activity context
C. It makes RecyclerView faster
D. It converts XML to JSON

**Answer:** B

**Explanation:** A long-lived singleton should not hold an Activity reference because that may cause memory leaks.

### Q15. What should the adapter usually receive from the activity?

A. A list of Disease objects ready for display
B. The entire XML file as a string
C. Only the parser object
D. Only the manifest file

**Answer:** A

**Explanation:** The adapter should work with parsed model objects, not raw XML text.

### Q16. What is the purpose of `skip(parser)` in a robust parser?

A. To skip app installation
B. To skip unknown or unwanted XML sections safely
C. To sort diseases alphabetically
D. To add missing tags

**Answer:** B

**Explanation:** A skip method helps the parser ignore future or unexpected tags without crashing.

### Q17. Which field is best shown to users as the title on the detail screen?

A. Raw XML root name
B. Common name
C. Package name
D. Parser class name

**Answer:** B

**Explanation:** The common name is human-readable, while the raw label is more suitable as an internal key.

### Q18. What is one good reason to store an image name in the XML?

A. So the app can dynamically resolve a matching drawable resource
B. So XML can replace the camera
C. So RecyclerView can parse JSON
D. So the APK becomes a database

**Answer:** A

**Explanation:** The image name lets the app map disease records to drawable resources without hardcoding each one in Java.

### Q19. Why should symptoms, treatment, and prevention usually be stored separately?

A. Because Android only allows three fields
B. Because different UI sections need them independently
C. Because XML cannot hold long text
D. Because maps require it

**Answer:** B

**Explanation:** Separate lists allow cleaner detail screens and clearer information architecture.

### Q20. What is the final goal of Week 08?

A. A fully offline disease information system connected to model predictions
B. A cloud-only API
C. A new programming language
D. A login screen

**Answer:** A

**Explanation:** The week aims to integrate XML-based disease information into the prediction flow and browseable library.


---

## Part B - Practical and Code Questions (20)

### Q21. In `XmlPullParser`, which constant marks the end of the entire XML document?

**Question Type:** Fill in the blank

**Answer:** `XmlPullParser.END_DOCUMENT`

**Explanation:** The event loop normally continues until `eventType == XmlPullParser.END_DOCUMENT`.

**Code-Completion Tip:** Write the answer from memory before checking. These small details matter in viva and exams.

### Q22. Complete the code: `currentDisease.setLabel(parser.getAttributeValue(null, _____ ));`

**Question Type:** Fill in the blank

**Answer:** `"label"`

**Explanation:** The attribute name must match the XML exactly, and `label` is the key used for lookup.

**Code-Completion Tip:** Write the answer from memory before checking. These small details matter in viva and exams.

### Q23. What is the output type of `parse(InputStream inputStream)` in this week?

**Question Type:** Short answer

**Answer:** `List<Disease>`

**Explanation:** The parser should return a collection of disease model objects for UI and repository use.

### Q24. Code review: why is this risky? `DiseaseRepository repository = new DiseaseRepository(this);`

**Question Type:** Short answer

**Answer:** It bypasses the singleton and may store an Activity context.

**Explanation:** Use `getInstance(getApplicationContext())` so the repository is shared and safer.

### Q25. Complete the missing line after creating the adapter: `recyclerView.setLayoutManager(__________);`

**Question Type:** Fill in the blank

**Answer:** `new LinearLayoutManager(this)`

**Explanation:** RecyclerView needs a LayoutManager or it may not display items correctly.

**Code-Completion Tip:** Write the answer from memory before checking. These small details matter in viva and exams.

### Q26. If `parser.getName()` returns `"disease"` during a `START_TAG`, what should usually happen next?

**Question Type:** Short answer

**Answer:** Create a new `Disease` object and read attributes such as label, image, and severity.

**Explanation:** That start tag marks the beginning of a disease record.

### Q27. Which method is commonly used to read the XML file from `assets/`?

**Question Type:** Short answer

**Answer:** `getAssets().open("disease_library.xml")`

**Explanation:** This returns an InputStream that can be passed to the parser.

### Q28. Code completion: `if (disease == null) { summaryTextView.setText(__________); }`

**Question Type:** Fill in the blank

**Answer:** `"Detailed disease information is not available yet."`

**Explanation:** A friendly fallback message is better than leaving the UI blank or crashing.

**Code-Completion Tip:** Write the answer from memory before checking. These small details matter in viva and exams.

### Q29. Why is this better than a loop for prediction lookup? `cachedMap.get(predictedLabel)`

**Question Type:** Short answer

**Answer:** Because it performs direct key-based lookup and keeps result integration simple and fast.

**Explanation:** The map is designed for exact label retrieval.

### Q30. Complete the helper method name often used after changing adapter data: `adapter.____________();`

**Question Type:** Fill in the blank

**Answer:** `notifyDataSetChanged`

**Explanation:** After replacing the adapter data list, the adapter must be told to refresh the visible rows.

**Code-Completion Tip:** Write the answer from memory before checking. These small details matter in viva and exams.

### Q31. In a nested list parser, what should be stored when an `<item>` tag is found inside `<symptoms>`?

**Question Type:** Short answer

**Answer:** The trimmed text should be added to a `List<String>` for symptoms.

**Explanation:** Each symptom item becomes one string in the disease object.

### Q32. What resource type is resolved by `getResources().getIdentifier(imageName, "drawable", getPackageName())`?

**Question Type:** Short answer

**Answer:** A drawable resource id.

**Explanation:** The XML stores the drawable name, and Android resolves the actual integer resource id.

### Q33. Code completion: `String predictedLabel = getIntent().getStringExtra(____________);`

**Question Type:** Fill in the blank

**Answer:** `"predicted_label"`

**Explanation:** Use a stable key name when sending prediction data between activities.

**Code-Completion Tip:** Write the answer from memory before checking. These small details matter in viva and exams.

### Q34. What bug appears if the XML contains `Tomato Late Blight` but the model returns `Tomato___Late_blight`?

**Question Type:** Short answer

**Answer:** Lookup fails because the label strings do not match exactly.

**Explanation:** The user may see fallback text even though the disease exists conceptually.

### Q35. Complete the parser loop condition: `while (eventType != __________ )`

**Question Type:** Fill in the blank

**Answer:** `XmlPullParser.END_DOCUMENT`

**Explanation:** This is the normal stopping condition for event-based XML parsing.

**Code-Completion Tip:** Write the answer from memory before checking. These small details matter in viva and exams.

### Q36. Which thread should update RecyclerView after background parsing finishes?

**Question Type:** Short answer

**Answer:** The main/UI thread.

**Explanation:** View updates must happen on the main thread even if parsing occurred in the background.

### Q37. Code review: what is missing here? `recyclerView.setAdapter(adapter);`

**Question Type:** Short answer

**Answer:** A LayoutManager should also be set.

**Explanation:** RecyclerView usually needs a LayoutManager such as `LinearLayoutManager` to arrange rows.

### Q38. Complete the Java declaration: `private final Map<String, Disease> cachedMap = new __________<>();`

**Question Type:** Fill in the blank

**Answer:** `HashMap`

**Explanation:** HashMap is the common Java implementation used for label-to-disease caching.

**Code-Completion Tip:** Write the answer from memory before checking. These small details matter in viva and exams.

### Q39. What should a parser unit test assert first after reading a one-entry XML string?

**Question Type:** Short answer

**Answer:** That the returned list size is 1.

**Explanation:** Checking the count first confirms the parser recognized the expected number of records.

### Q40. Why should `symptoms`, `treatment`, and `prevention` lists be initialized in the constructor?

**Question Type:** Short answer

**Answer:** So they are never null and can safely accept parsed items immediately.

**Explanation:** This avoids null pointer crashes during parsing or display.


---

## Quick Scoring Guide

- **36-40 correct:** Excellent. You are viva-ready for Week 08.
- **30-35 correct:** Good. Review parser flow and repository design one more time.
- **24-29 correct:** Fair. Revisit learning notes and redo the nested-tag exercises.
- **Below 24:** Do not move ahead yet. Practise the build task and explain each class aloud.

---

## Final Reflection

After completing the quiz, answer these three questions in your notebook or progress log:

1. Which type of question felt hardest: XML design, parser code, caching, or UI integration?
2. Which answers did you get wrong because of small syntax details?
3. Can you now explain the full flow from `assets/disease_library.xml` to `ResultActivity` without reading notes?


<!-- NAV_FOOTER_START -->

---

## 📚 Week 08 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| **6** | **quiz.md** ← *You are here* | **Knowledge Assessment Quiz** |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Validation & Verification](validation-checklist.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Knowledge Assessment Quiz** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Reflection & Consolidation →](reflection.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 07: Room Database & History](../week-07-room-sqlite-history/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 09: TensorFlow Lite Offline AI ➡](../week-09-tensorflow-lite-offline-ai/README.md) |

---
