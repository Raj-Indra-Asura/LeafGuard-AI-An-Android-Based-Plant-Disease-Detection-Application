# Week 08: Learning Notes - XML Disease Library

> **Accuracy note (read first):** The **shipped** library is `app/src/main/assets/diseases.xml` (in `assets/`, filename `diseases.xml`), parsed by `DiseaseLibraryActivity` with `XmlPullParser`. It has **10** `<disease>` entries using five child tags — `<name>`, `<plant>`, `<symptoms>`, `<treatment>`, `<prevention>` — and each `<name>` matches a line in `assets/labels.txt` exactly (labels use spaces, e.g. `Tomato Late Blight`, **not** underscores). The larger 38-class PlantVillage schema and `label="Tomato___Late_blight"` style shown below are an **optional real-world extension** for learning; the app itself ships the 10-entry, five-tag file. Kotlin is the primary track — Java examples are the secondary reference.

## Week 08 Context

Since you are working on **Week 08**, the focus is building an **offline XML Disease Library** for LeafGuard AI.
This directly supports the CSE 2206 syllabus topic on **XML parsing in Android using Java**.
The disease library lets the app explain a prediction instead of showing only a label.
When the ML model predicts a disease such as `Tomato___Late_blight`, the app should immediately show:

- common name
- scientific name
- symptoms
- treatment guidance
- prevention tips
- severity level
- optional image

This week connects several topics you already studied:

1. **Android assets** from project structure weeks.
2. **Java model classes** from earlier object-oriented programming practice.
3. **RecyclerView** for list-based UI.
4. **Intent data passing** for opening a disease detail screen.
5. **Background processing** so the UI remains smooth.

If you complete this week well, your app becomes much more useful for farmers, students, and demo judges because it explains *why a prediction matters*.

---

## Learning Outcomes

By the end of this week, you should be able to:

- explain what XML is and why it is used in Android projects
- design a structured `diseases.xml` file in the `assets/` folder
- parse XML using `XmlPullParser` in Java
- compare **DOM**, **SAX**, and **XmlPullParser**
- build a `Disease` model class from XML tags
- load disease records once and cache them in memory
- expose disease lookups through a `DiseaseRepository`
- connect disease data to a `RecyclerView`
- integrate the disease lookup into `ResultActivity`
- handle missing labels gracefully
- test XML parsing logic with unit tests
- answer common viva questions about XML parsing

---

## Why XML Still Matters in CSE 2206

Many students ask: *why not just use JSON?*

For this course, XML matters for three reasons:

1. **It is part of the syllabus.** You are expected to understand structured markup, nested tags, and parser flows.
2. **Android uses XML heavily.** Layout files, manifest files, string resources, menus, and themes all use XML.
3. **XML shows hierarchy clearly.** A disease entry can contain nested sections like `<symptoms>`, `<treatment>`, and `<prevention>`.

Even if modern apps often use JSON from APIs, XML is still excellent for learning structured parsing.

---

## The Role of the Disease Library in LeafGuard AI

Your ML model predicts one of the PlantVillage labels.
That label alone is not enough for a good user experience.

For example, imagine the result screen only shows:

```text
Prediction: Tomato___Early_blight
Confidence: 93.4%
```

A farmer may ask:

- What does early blight look like?
- Is it dangerous?
- How can I reduce spread?
- What should I do next?

The XML disease library solves this by acting like a mini offline knowledge base.

### Recommended flow

1. User captures or selects a leaf image.
2. The ML model returns a predicted label.
3. `ResultActivity` receives that label.
4. `DiseaseRepository` looks up the corresponding XML entry.
5. The app displays structured disease information.
6. The user can tap **Open Library** to browse all diseases.

This gives LeafGuard AI two layers of value:

- **prediction layer**: what the model thinks the disease is
- **information layer**: what the user should understand and do next

---

## XML Fundamentals Refresher

### What XML is

XML stands for **eXtensible Markup Language**.
It stores structured data using opening and closing tags.

Example:

```xml
<disease>
    <label>Tomato___Late_blight</label>
    <commonName>Tomato Late Blight</commonName>
</disease>
```

### XML rules you must remember

- Every opening tag must have a closing tag.
- Tags are case-sensitive.
- XML must have exactly one root element.
- Attribute values must be inside quotes.
- Nested elements must be properly ordered.
- Special characters such as `&` should be escaped when needed.

### Good habit for this week

Always validate your XML mentally by checking:

- Is there one root tag?
- Are all diseases inside the root?
- Does every `<disease>` contain the required child tags?
- Do labels exactly match model output labels?

---

## Designing `diseases.xml`

The XML file should live at:

```text
app/src/main/assets/diseases.xml
```

The `assets/` folder is a good choice because:

- the file ships with the APK
- it works offline
- it is easy to open with `getAssets().open()`
- it does not require network access

### Recommended root structure

Use a root element named `<diseaseLibrary>` or `<diseases>`.
Both are acceptable, but stay consistent.

Recommended structure:

```xml
<?xml version="1.0" encoding="utf-8"?>
<diseaseLibrary version="1.0" source="PlantVillage" totalClasses="38">
    <disease label="Tomato___Late_blight" image="tomato_late_blight" severity="high">
        <commonName>Tomato Late Blight</commonName>
        <scientificName>Phytophthora infestans</scientificName>
        <plant>Tomato</plant>
        <category>Oomycete disease</category>
        <summary>Rapidly spreading blight that affects leaves, stems, and fruits.</summary>
        <symptoms>
            <item>Water-soaked dark lesions on leaves</item>
            <item>White fungal-like growth on the underside in humid weather</item>
            <item>Brown stem lesions and fruit rot</item>
        </symptoms>
        <treatment>
            <item>Remove badly infected leaves</item>
            <item>Improve airflow around plants</item>
            <item>Use a recommended fungicide according to agricultural advice</item>
        </treatment>
        <prevention>
            <item>Avoid overhead watering</item>
            <item>Use clean tools and disease-free seedlings</item>
            <item>Rotate crops</item>
        </prevention>
    </disease>
</diseaseLibrary>
```

This structure is better than a flat design because it supports:

- metadata through attributes (`label`, `image`, `severity`)
- nested list items for symptoms, treatment, and prevention
- easy expansion later if you add `cause`, `spread`, or `recommendedSpray`

---

## Required Fields for Each Disease Entry

To keep the file consistent, every `<disease>` should contain the same essential information.

### Minimum fields

- `label` attribute or tag
- `commonName`
- `scientificName`
- `plant`
- `category`
- `summary`
- `symptoms`
- `treatment`
- `prevention`
- `image` attribute
- `severity` attribute

### Why consistency matters

If one disease is missing `prevention`, your parser may still work, but your detail screen may show empty content.
A consistent structure keeps parsing logic simple and reduces bugs.

---

## Complete XML Design Guide for All 38 PlantVillage Classes

The most important rule is this:

> The `label` in XML must exactly match the label produced by your ML model.

If the model outputs `Tomato___Target_Spot` but the XML says `Tomato_Target_Spot`, lookup will fail.

Below is a recommended master catalogue for the full PlantVillage label set.
Use it when designing the final `diseases.xml`.

| # | ML Label | Display Name | Plant | Category | Suggested Drawable |
|---|---|---|---|---|---|
| 1 | `Apple___Apple_scab` | Apple Scab | Apple | fungal | `apple_apple_scab` |
| 2 | `Apple___Black_rot` | Apple Black Rot | Apple | fungal | `apple_black_rot` |
| 3 | `Apple___Cedar_apple_rust` | Apple Cedar Rust | Apple | fungal | `apple_cedar_rust` |
| 4 | `Apple___healthy` | Healthy Apple Leaf | Apple | healthy | `apple_healthy` |
| 5 | `Blueberry___healthy` | Healthy Blueberry Leaf | Blueberry | healthy | `blueberry_healthy` |
| 6 | `Cherry_(including_sour)___Powdery_mildew` | Cherry Powdery Mildew | Cherry | fungal | `cherry_powdery_mildew` |
| 7 | `Cherry_(including_sour)___healthy` | Healthy Cherry Leaf | Cherry | healthy | `cherry_healthy` |
| 8 | `Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot` | Corn Gray Leaf Spot | Corn | fungal | `corn_gray_leaf_spot` |
| 9 | `Corn_(maize)___Common_rust_` | Corn Common Rust | Corn | fungal | `corn_common_rust` |
| 10 | `Corn_(maize)___Northern_Leaf_Blight` | Corn Northern Leaf Blight | Corn | fungal | `corn_northern_leaf_blight` |
| 11 | `Corn_(maize)___healthy` | Healthy Corn Leaf | Corn | healthy | `corn_healthy` |
| 12 | `Grape___Black_rot` | Grape Black Rot | Grape | fungal | `grape_black_rot` |
| 13 | `Grape___Esca_(Black_Measles)` | Grape Esca | Grape | fungal | `grape_esca` |
| 14 | `Grape___Leaf_blight_(Isariopsis_Leaf_Spot)` | Grape Leaf Blight | Grape | fungal | `grape_leaf_blight` |
| 15 | `Grape___healthy` | Healthy Grape Leaf | Grape | healthy | `grape_healthy` |
| 16 | `Orange___Haunglongbing_(Citrus_greening)` | Citrus Greening | Orange | bacterial | `orange_citrus_greening` |
| 17 | `Peach___Bacterial_spot` | Peach Bacterial Spot | Peach | bacterial | `peach_bacterial_spot` |
| 18 | `Peach___healthy` | Healthy Peach Leaf | Peach | healthy | `peach_healthy` |
| 19 | `Pepper,_bell___Bacterial_spot` | Bell Pepper Bacterial Spot | Pepper | bacterial | `pepper_bacterial_spot` |
| 20 | `Pepper,_bell___healthy` | Healthy Bell Pepper Leaf | Pepper | healthy | `pepper_healthy` |
| 21 | `Potato___Early_blight` | Potato Early Blight | Potato | fungal | `potato_early_blight` |
| 22 | `Potato___Late_blight` | Potato Late Blight | Potato | fungal | `potato_late_blight` |
| 23 | `Potato___healthy` | Healthy Potato Leaf | Potato | healthy | `potato_healthy` |
| 24 | `Raspberry___healthy` | Healthy Raspberry Leaf | Raspberry | healthy | `raspberry_healthy` |
| 25 | `Soybean___healthy` | Healthy Soybean Leaf | Soybean | healthy | `soybean_healthy` |
| 26 | `Squash___Powdery_mildew` | Squash Powdery Mildew | Squash | fungal | `squash_powdery_mildew` |
| 27 | `Strawberry___Leaf_scorch` | Strawberry Leaf Scorch | Strawberry | fungal | `strawberry_leaf_scorch` |
| 28 | `Strawberry___healthy` | Healthy Strawberry Leaf | Strawberry | healthy | `strawberry_healthy` |
| 29 | `Tomato___Bacterial_spot` | Tomato Bacterial Spot | Tomato | bacterial | `tomato_bacterial_spot` |
| 30 | `Tomato___Early_blight` | Tomato Early Blight | Tomato | fungal | `tomato_early_blight` |
| 31 | `Tomato___Late_blight` | Tomato Late Blight | Tomato | oomycete | `tomato_late_blight` |
| 32 | `Tomato___Leaf_Mold` | Tomato Leaf Mold | Tomato | fungal | `tomato_leaf_mold` |
| 33 | `Tomato___Septoria_leaf_spot` | Tomato Septoria Leaf Spot | Tomato | fungal | `tomato_septoria_leaf_spot` |
| 34 | `Tomato___Spider_mites Two-spotted_spider_mite` | Tomato Spider Mites | Tomato | pest | `tomato_spider_mites` |
| 35 | `Tomato___Target_Spot` | Tomato Target Spot | Tomato | fungal | `tomato_target_spot` |
| 36 | `Tomato___Tomato_Yellow_Leaf_Curl_Virus` | Tomato Yellow Leaf Curl Virus | Tomato | viral | `tomato_yellow_leaf_curl_virus` |
| 37 | `Tomato___Tomato_mosaic_virus` | Tomato Mosaic Virus | Tomato | viral | `tomato_mosaic_virus` |
| 38 | `Tomato___healthy` | Healthy Tomato Leaf | Tomato | healthy | `tomato_healthy` |


### How to organise the 38 diseases efficiently

A clean way is to group diseases by plant in the XML file.
That makes the file easier to read and easier to maintain during viva.

Suggested order:

1. Apple
2. Blueberry
3. Cherry
4. Corn
5. Grape
6. Orange
7. Peach
8. Pepper
9. Potato
10. Raspberry
11. Soybean
12. Squash
13. Strawberry
14. Tomato

### Grouping example

```xml
<diseaseLibrary version="1.0" source="PlantVillage" totalClasses="38">
    <!-- Apple -->
    <disease label="Apple___Apple_scab" image="apple_apple_scab" severity="medium">
        ...
    </disease>
    <disease label="Apple___Black_rot" image="apple_black_rot" severity="high">
        ...
    </disease>

    <!-- Tomato -->
    <disease label="Tomato___Bacterial_spot" image="tomato_bacterial_spot" severity="high">
        ...
    </disease>
    <disease label="Tomato___healthy" image="tomato_healthy" severity="none">
        ...
    </disease>
</diseaseLibrary>
```

### What to write for healthy classes

Healthy classes still deserve entries because the result screen needs something useful to show.
For healthy classes:

- `summary` should say the leaf appears healthy.
- `symptoms` can list positive observations such as normal green tissue.
- `treatment` can say no treatment is necessary.
- `prevention` can focus on routine care and monitoring.

### Example healthy entry strategy

```xml
<disease label="Tomato___healthy" image="tomato_healthy" severity="none">
    <commonName>Healthy Tomato Leaf</commonName>
    <scientificName>Not applicable</scientificName>
    <plant>Tomato</plant>
    <category>Healthy</category>
    <summary>The leaf shows no major visible disease symptoms.</summary>
    <symptoms>
        <item>Leaf color is mostly uniform</item>
        <item>No obvious necrotic spotting pattern</item>
        <item>No severe curling, mold, or lesions</item>
    </symptoms>
    <treatment>
        <item>No treatment required</item>
        <item>Continue standard watering and nutrient management</item>
    </treatment>
    <prevention>
        <item>Monitor leaves regularly</item>
        <item>Maintain proper spacing and airflow</item>
        <item>Keep tools clean</item>
    </prevention>
</disease>
```

---

## XML Tag Design Decisions

### Option A: Put everything in child tags

```xml
<disease>
    <label>Tomato___Early_blight</label>
    <image>tomato_early_blight</image>
</disease>
```

### Option B: Put small metadata in attributes

```xml
<disease label="Tomato___Early_blight" image="tomato_early_blight" severity="high">
```

### Recommended approach for LeafGuard AI

Use **attributes** for short metadata and **child tags** for long text.

Good attribute candidates:

- `label`
- `image`
- `severity`

Good child tags:

- `commonName`
- `scientificName`
- `summary`
- `symptoms`
- `treatment`
- `prevention`

This makes parsing cleaner and keeps text readable.

---

## Full Example of a Well-Designed Disease Entry

```xml
<disease label="Pepper,_bell___Bacterial_spot" image="pepper_bacterial_spot" severity="high">
    <commonName>Bell Pepper Bacterial Spot</commonName>
    <scientificName>Xanthomonas campestris pv. vesicatoria</scientificName>
    <plant>Bell Pepper</plant>
    <category>Bacterial disease</category>
    <summary>A bacterial disease that causes dark, water-soaked lesions on leaves and fruit.</summary>
    <symptoms>
        <item>Small dark lesions with yellow halos</item>
        <item>Spots may become rough and cracked on fruit</item>
        <item>Leaf drop in severe infection</item>
    </symptoms>
    <treatment>
        <item>Remove heavily infected leaves or fruits</item>
        <item>Avoid working with plants when leaves are wet</item>
        <item>Use approved copper-based management if recommended locally</item>
    </treatment>
    <prevention>
        <item>Start with disease-free seeds or seedlings</item>
        <item>Sanitize tools and hands</item>
        <item>Use crop rotation and avoid splash irrigation</item>
    </prevention>
</disease>
```

Notice the design choices:

- The `label` matches ML output exactly.
- The `image` refers to a drawable resource.
- Symptoms, treatment, and prevention are lists, not long paragraphs.
- The text is readable on a phone screen.

---

## XmlPullParser Deep Dive

### Why `XmlPullParser` is preferred in Android learning projects

`XmlPullParser` is lightweight and efficient.
It does not load the entire XML tree into memory like DOM.
Instead, it reads the file one event at a time.

That makes it a strong choice when:

- your data file is moderate in size
- you want low memory usage
- you are working on Android devices with limited resources
- you want to demonstrate manual parsing logic for viva

### The parser works as an event stream

When the parser reads XML, it moves through a sequence of events such as:

- `START_DOCUMENT`
- `START_TAG`
- `TEXT`
- `END_TAG`
- `END_DOCUMENT`

### Visual event flow for one small entry

For this XML:

```xml
<disease label="Tomato___Early_blight">
    <commonName>Tomato Early Blight</commonName>
</disease>
```

The event flow is roughly:

1. `START_DOCUMENT`
2. `START_TAG` -> `disease`
3. `START_TAG` -> `commonName`
4. `TEXT` -> `Tomato Early Blight`
5. `END_TAG` -> `commonName`
6. `END_TAG` -> `disease`
7. `END_DOCUMENT`

### Typical event loop structure

```java
XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
XmlPullParser parser = factory.newPullParser();
parser.setInput(inputStream, null);

int eventType = parser.getEventType();
while (eventType != XmlPullParser.END_DOCUMENT) {
    switch (eventType) {
        case XmlPullParser.START_TAG:
            // Handle opening tag
            break;
        case XmlPullParser.TEXT:
            // Handle text if needed
            break;
        case XmlPullParser.END_TAG:
            // Handle closing tag
            break;
    }
    eventType = parser.next();
}
```

### A more practical parser with helper methods

```java
public class DiseaseXmlParser {

    public List<Disease> parse(InputStream inputStream)
            throws XmlPullParserException, IOException {
        List<Disease> diseases = new ArrayList<>();
        Disease currentDisease = null;

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
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
                    readSimpleTag(parser, tagName, currentDisease);
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

    private void readSimpleTag(XmlPullParser parser, String tagName, Disease disease)
            throws IOException, XmlPullParserException {
        if ("commonName".equals(tagName)) {
            disease.setCommonName(readText(parser));
        } else if ("scientificName".equals(tagName)) {
            disease.setScientificName(readText(parser));
        } else if ("plant".equals(tagName)) {
            disease.setPlant(readText(parser));
        } else if ("category".equals(tagName)) {
            disease.setCategory(readText(parser));
        } else if ("summary".equals(tagName)) {
            disease.setSummary(readText(parser));
        }
    }

    private String readText(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String text = "";
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.getText().trim();
            parser.nextTag();
        }
        return text;
    }
}
```

---

## Handling Nested Tags Correctly

In a disease library, some data is naturally a list.
For example, symptoms may contain multiple `<item>` elements.
That means you must handle nesting carefully.

### Nested XML example

```xml
<symptoms>
    <item>Small brown lesions on lower leaves</item>
    <item>Yellow halo around spots</item>
    <item>Leaves may dry and fall early</item>
</symptoms>
```

### Strategy for nested tags

When you encounter `<symptoms>`, do not simply call `readText()` once.
Instead, loop until the closing `</symptoms>` tag and collect each `<item>`.

```java
private List<String> readItemList(XmlPullParser parser, String parentTag)
        throws IOException, XmlPullParserException {
    List<String> items = new ArrayList<>();

    while (parser.next() != XmlPullParser.END_TAG || !parentTag.equals(parser.getName())) {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            continue;
        }

        if ("item".equals(parser.getName())) {
            items.add(readText(parser));
        } else {
            skip(parser);
        }
    }

    return items;
}
```

### Why this works

- `parser.next()` advances through events.
- The loop continues until the parser reaches the end tag of the parent section.
- Each `<item>` is read and added to a list.
- Unknown nested tags are skipped safely.

### Important warning

If you call `readText()` directly on `<symptoms>`, you may only get whitespace or incomplete content because the actual text is inside child `<item>` tags.

---

## Understanding Whitespace in XML Parsing

Whitespace is a classic source of confusion.
Because XML is formatted across multiple lines, the parser may encounter whitespace text nodes.

Example:

```xml
<disease>
    <commonName>Tomato Late Blight</commonName>
</disease>
```

There is indentation and line spacing between tags.
Some parser steps may read that whitespace as `TEXT`.

### Best practice

Use:

```java
String value = parser.getText().trim();
```

Why?

- `trim()` removes unwanted spaces and line breaks.
- It prevents values such as `"
    Tomato Late Blight
"`.

### When `trim()` is not enough

If a text block contains meaningful spacing, trimming may remove intentional leading/trailing spaces.
That is rarely a problem for disease library data because most entries are plain text.

---

## Reading Attributes

Attributes belong to the current start tag.
If your `<disease>` tag has attributes, read them inside the `START_TAG` event.

Example:

```java
if ("disease".equals(parser.getName())) {
    String label = parser.getAttributeValue(null, "label");
    String image = parser.getAttributeValue(null, "image");
    String severity = parser.getAttributeValue(null, "severity");
}
```

### Why `null` is passed as the namespace

In simple Android XML files without namespaces, use `null` for the namespace argument.
That tells the parser to look for a non-namespaced attribute.

### Good viva answer

If asked *“What is the difference between tags and attributes?”* you can say:

- Tags store structured content and nested elements.
- Attributes store short metadata attached to an element.
- In our disease library, we use attributes for `label`, `image`, and `severity` because they are compact identifiers.

---

## The `skip()` Method for Unknown Tags

A robust parser should survive future XML changes.
If you later add a new tag such as `<recommendedFertilizer>`, old parser code should skip it instead of crashing.

Use a generic skip method:

```java
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
```

### Why this is useful

- It makes your parser future-proof.
- It helps during debugging if a tag is misspelled or unexpected.
- It is a sign of mature parser design in viva.

---

## SAX vs DOM vs XmlPullParser

Students are often asked to compare XML parsing methods.
Memorize the table below.

| Feature | DOM Parser | SAX Parser | XmlPullParser |
|---|---|---|---|
| Parsing style | Loads entire XML tree in memory | Event-driven callback parsing | Event-driven pull parsing |
| Memory use | High | Low | Low |
| Speed on large files | Moderate | Fast | Fast |
| Easy random access | Yes | No | No |
| Android friendliness | Less preferred for large mobile data | Possible but more complex | Very suitable |
| Coding style | Tree navigation | Handler callbacks | Manual event loop |
| Best for this course | Good for theory comparison | Good to understand streaming | Best practical option |
| Best for LeafGuard | Not ideal if file grows | Okay but verbose | Recommended |

### Simple explanation of the difference

- **DOM** reads everything first, then you move around a tree.
- **SAX** pushes events to callback methods.
- **XmlPullParser** lets your code pull the next event when ready.

### Why `XmlPullParser` fits Week 08 best

- It is included in Android APIs.
- It has low memory usage.
- It demonstrates clear Java control flow for exams.
- It is easier for students to debug than SAX callback chains.

### Viva-ready summary sentence

> For LeafGuard AI, I chose `XmlPullParser` because it is lightweight, Android-friendly, uses low memory, and gives me direct control over tag-by-tag parsing in Java.

---

## Disease Model Design

Your parser should not directly update the UI.
Instead, it should convert XML into a Java object model.

Recommended `Disease` class fields:

```java
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

    // getters and setters
}
```

### Why lists are better than one long string

You could store symptoms as one big paragraph, but a `List<String>` is better because:

- RecyclerView detail screens can show bullet points
- formatting is cleaner
- future search and filtering become easier
- unit testing individual items becomes simpler

---

## Parsing Once and Caching in Memory

### Why caching matters

If you parse the XML file every time `ResultActivity` opens, your app repeats unnecessary work.
That causes:

- slower result screens
- repeated disk access
- wasted battery and CPU cycles
- more chances for UI lag

The disease library is mostly static data.
That makes it perfect for **parse once, reuse many times**.

### Recommended cache structure

Use both:

- a `List<Disease>` for showing all diseases in RecyclerView
- a `Map<String, Disease>` for fast lookup by label

```java
private final List<Disease> diseaseList = new ArrayList<>();
private final Map<String, Disease> diseaseMap = new HashMap<>();
```

### Why a `Map<String, Disease>` is powerful

Prediction lookup becomes simple:

```java
Disease disease = diseaseMap.get(predictedLabel);
```

This is much cleaner than looping through every disease each time.

### Cache loading pattern

1. Parse XML once.
2. Store all objects in the list.
3. Put each disease in the map using the label as key.
4. Reuse the cached data anywhere in the app.

---

## Singleton Parser or Singleton Repository Pattern

A singleton ensures only one shared instance exists during app runtime.
For this week, the singleton should usually be the **repository**, not the parser itself.

Why?

- The parser's job is only to convert XML into objects.
- The repository manages loading, caching, lookup, and exposure to Activities.

### Recommended structure

- `DiseaseXmlParser` -> low-level parsing logic
- `DiseaseRepository` -> singleton cache and public access methods

### Repository singleton example

```java
public class DiseaseRepository {
    private static DiseaseRepository instance;
    private final Context appContext;
    private final Map<String, Disease> diseaseMap = new HashMap<>();
    private final List<Disease> diseaseList = new ArrayList<>();
    private boolean loaded;

    private DiseaseRepository(Context context) {
        this.appContext = context.getApplicationContext();
    }

    public static synchronized DiseaseRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DiseaseRepository(context);
        }
        return instance;
    }
}
```

### Why `getApplicationContext()` is used

Never keep an `Activity` context inside a long-lived singleton because that can cause memory leaks.
Using the application context is safer.

---

## DiseaseRepository: Abstraction Layer for Activities

Activities should not know:

- where the XML file is stored
- how parsing works internally
- whether the data came from cache or from disk

Activities should only know:

- how to request all diseases
- how to request one disease by label

### Good repository interface

```java
public interface LoadCallback {
    void onSuccess(List<Disease> diseases);
    void onError(Exception exception);
}
```

And in the repository:

```java
public void loadDiseases(LoadCallback callback)
public Disease findByLabel(String label)
public List<Disease> getAllDiseases()
```

### Benefits of the repository pattern

- cleaner Activities
- easier testing
- one source of truth
- easy caching
- easy replacement later if XML becomes a Room database or API

This is a very good point to mention in viva because it shows software design thinking, not just coding.

---

## Connecting Disease Data to RecyclerView in `DiseaseLibraryActivity`

### Data flow you should implement

1. `DiseaseLibraryActivity` starts.
2. It requests all diseases from `DiseaseRepository`.
3. The repository returns cached list or parses XML in background.
4. The activity passes the list to `DiseaseAdapter`.
5. The adapter binds each disease to a row view.
6. Tapping a row opens `DiseaseDetailActivity` or detail layout.

### Why RecyclerView is the right UI component

RecyclerView is better than adding many views manually because:

- it handles long lists efficiently
- it reuses row views
- it is scalable for 38 diseases
- it supports filtering later

### Core activity logic

```java
repository.loadDiseases(new DiseaseRepository.LoadCallback() {
    @Override
    public void onSuccess(List<Disease> diseases) {
        adapter.updateData(diseases);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(Exception exception) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(DiseaseLibraryActivity.this,
                "Failed to load disease library",
                Toast.LENGTH_LONG).show();
    }
});
```

### Adapter binding logic

Each row should show at least:

- image thumbnail
- common name
- plant name
- short summary or severity

That gives the user an easy overview before opening full details.

---

## Connecting Disease Lookup to `ResultActivity`

After ML prediction, `ResultActivity` usually receives a label and confidence score through an Intent.

Example extras:

```java
Intent intent = getIntent();
String predictedLabel = intent.getStringExtra("predicted_label");
float confidence = intent.getFloatExtra("confidence", 0f);
```

### Then perform repository lookup

```java
DiseaseRepository repository = DiseaseRepository.getInstance(this);
Disease disease = repository.findByLabel(predictedLabel);
```

### If the cache is not loaded yet

You have two options:

1. call `loadDiseases()` first, then look up the result inside `onSuccess()`
2. preload the repository earlier in `Application`, splash screen, or home screen

### Result screen UI recommendation

On `ResultActivity`, show:

- predicted label or common name
- confidence percentage
- summary
- top symptoms
- treatment button or expandable section
- prevention tips
- button: `Open Full Library`

This is excellent for demonstration because it shows integration between ML output and structured XML data.

---

## Graceful Fallback for Missing Disease Labels

Your app must not crash if the XML file is incomplete or if the model returns a label not present in the file.

### Example fallback strategy

```java
Disease disease = repository.findByLabel(predictedLabel);
if (disease == null) {
    titleTextView.setText(formatLabel(predictedLabel));
    summaryTextView.setText("Detailed library information is not available yet for this prediction.");
    symptomsTextView.setText("Please review the leaf manually and update diseases.xml later.");
}
```

### Why fallback matters

- protects the app during development
- prevents null pointer crashes
- gives the user a useful message
- shows mature error handling in viva

### Helpful fallback text

Use user-friendly text, not technical errors.

Bad:

```text
Null disease object
```

Good:

```text
LeafGuard AI could not find a detailed library entry for this label yet.
```

---

## Adding Disease Images in `drawable`

A good disease library becomes much stronger if each entry has an illustrative image.

### Suggested approach

1. Put one sample image per disease inside `res/drawable/`.
2. Store the drawable name in the XML `image` attribute.
3. Resolve it dynamically in Java.

### Example XML attribute

```xml
<disease label="Potato___Early_blight" image="potato_early_blight" severity="medium">
```

### Resolving drawable by name

```java
int imageResId = context.getResources().getIdentifier(
        disease.getImageName(),
        "drawable",
        context.getPackageName()
);

if (imageResId == 0) {
    imageResId = R.drawable.ic_disease_placeholder;
}
imageView.setImageResource(imageResId);
```

### Image naming rules

Use lowercase letters, numbers, and underscores only.
Android drawable names cannot contain spaces or capital letters.

### Practical naming convention

- `tomato_late_blight`
- `pepper_bacterial_spot`
- `corn_gray_leaf_spot`
- `apple_healthy`

---

## Full Layout XML for the Disease Library List Item

Create `res/layout/item_disease.xml`.
This layout is for one row inside the RecyclerView.

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
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
                android:text="Tomato Late Blight"
                android:textColor="@android:color/black"
                android:textSize="18sp"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/textPlant"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="4dp"
                android:text="Tomato"
                android:textSize="14sp" />

            <TextView
                android:id="@+id/textSummary"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="6dp"
                android:maxLines="2"
                android:ellipsize="end"
                android:text="Rapidly spreading blight that affects leaves and fruit."
                android:textSize="14sp" />

            <TextView
                android:id="@+id/textSeverity"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="6dp"
                android:text="Severity: High"
                android:textStyle="italic" />
        </LinearLayout>
    </LinearLayout>
</com.google.android.material.card.MaterialCardView>
```

### Why this layout works

- The image gives immediate visual identification.
- The common name is most prominent.
- The plant and summary give quick context.
- The card view looks clean for presentation demos.

---

## Full Layout XML for the Detail View

You can either use a separate `DiseaseDetailActivity` or place the detail layout inside `ResultActivity`.
Below is a reusable detail layout example.

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
            android:scaleType="centerCrop"
            android:src="@drawable/ic_disease_placeholder"
            android:contentDescription="Disease detail image" />

        <TextView
            android:id="@+id/textDetailCommonName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Tomato Late Blight"
            android:textSize="24sp"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textScientificName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:text="Phytophthora infestans"
            android:textStyle="italic" />

        <TextView
            android:id="@+id/textSummaryDetail"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="12dp"
            android:text="Summary goes here" />

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
            android:text="• Symptom 1" />

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
            android:text="• Treatment 1" />

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
            android:text="• Prevention 1" />
    </LinearLayout>
</ScrollView>
```

### Why `ScrollView` is necessary

Symptoms, treatment, and prevention can become long.
A scrollable detail screen prevents content from being cut off on small devices.

---

## Threading: Why You Should NOT Parse XML on the UI Thread

Android UI updates run on the main thread.
If you perform slow work there, the app may freeze.

### What can go wrong on the UI thread

- the result screen opens slowly
- scrolling becomes laggy
- Android may show an **Application Not Responding (ANR)** warning
- users think the app is broken

Even though a small XML file may seem fast, good Android practice is to do file reading and parsing in the background.

### Better options for this course

- `AsyncTask` (older but easy to understand; often still seen in academic Android examples)
- `ExecutorService` + `Handler`
- plain `Thread` + `runOnUiThread()`

### `AsyncTask` example for course understanding

```java
private class LoadDiseasesTask extends AsyncTask<Void, Void, List<Disease>> {
    private Exception exception;

    @Override
    protected List<Disease> doInBackground(Void... voids) {
        try {
            return DiseaseRepository.getInstance(getApplicationContext()).loadSynchronously();
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Disease> diseases) {
        if (exception != null) {
            Toast.makeText(DiseaseLibraryActivity.this,
                    "Could not load disease library",
                    Toast.LENGTH_LONG).show();
            return;
        }
        adapter.updateData(diseases);
    }
}
```

### Preferred modern Java approach

```java
ExecutorService executorService = Executors.newSingleThreadExecutor();
Handler mainHandler = new Handler(Looper.getMainLooper());

executorService.execute(new Runnable() {
    @Override
    public void run() {
        try {
            final List<Disease> diseases = repository.loadSynchronously();
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    adapter.updateData(diseases);
                }
            });
        } catch (final Exception e) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DiseaseLibraryActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
});
```

### Viva sentence for threading

> I parse XML in a background thread because disk I/O and parsing should not block the UI thread. After parsing completes, I post the results back to the main thread to update the RecyclerView safely.

---

## Search and Filtering Strategy

A disease library is much better if the user can search.

### Simple search logic

Filter by:

- common name
- plant name
- label

### Example filtering idea in adapter or activity

```java
String query = searchText.toLowerCase(Locale.US).trim();
for (Disease disease : fullList) {
    if (disease.getCommonName().toLowerCase(Locale.US).contains(query)
            || disease.getPlant().toLowerCase(Locale.US).contains(query)
            || disease.getLabel().toLowerCase(Locale.US).contains(query)) {
        filteredList.add(disease);
    }
}
```

This is not the main topic of the week, but it greatly improves the usability of the feature.

---

## Suggested Package Structure

Keep your project organized.

```text
com.leafguardai
├── model
│   └── Disease.java
├── parser
│   └── DiseaseXmlParser.java
├── repository
│   └── DiseaseRepository.java
├── ui
│   ├── DiseaseLibraryActivity.java
│   ├── DiseaseDetailActivity.java
│   └── adapter
│       └── DiseaseAdapter.java
└── util
    └── TextFormatUtils.java
```

This is not compulsory, but a clean structure helps with grading and viva explanation.

---

## Unit Testing the XML Parser

### Why unit test this week's code

XML parsing is logic-heavy and easy to break with:

- missing tags
- spelling errors
- malformed XML
- unexpected whitespace
- labels that do not match

Unit testing helps prove the parser works even before UI integration.

### What to test

1. A valid disease entry parses correctly.
2. Multiple diseases are returned in the correct count.
3. Nested symptom items are read properly.
4. Missing optional tags do not crash the parser.
5. Unknown labels can still be stored.
6. Malformed XML throws an exception or is handled gracefully.

### Example local unit test approach

If you keep a small sample XML file inside `src/test/resources/`, you can test the parser using a `ByteArrayInputStream`.

```java
@Test
public void parse_validXml_returnsDiseaseList() throws Exception {
    String xml = "<?xml version="1.0" encoding="utf-8"?>"
            + "<diseaseLibrary>"
            + "<disease label="Tomato___Early_blight" image="tomato_early_blight" severity="medium">"
            + "<commonName>Tomato Early Blight</commonName>"
            + "<scientificName>Alternaria solani</scientificName>"
            + "<plant>Tomato</plant>"
            + "<category>Fungal disease</category>"
            + "<summary>Common early blight disease.</summary>"
            + "<symptoms><item>Brown spots</item></symptoms>"
            + "<treatment><item>Remove infected leaves</item></treatment>"
            + "<prevention><item>Rotate crops</item></prevention>"
            + "</disease>"
            + "</diseaseLibrary>";

    InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
    DiseaseXmlParser parser = new DiseaseXmlParser();

    List<Disease> diseases = parser.parse(inputStream);

    assertEquals(1, diseases.size());
    assertEquals("Tomato___Early_blight", diseases.get(0).getLabel());
    assertEquals("Tomato Early Blight", diseases.get(0).getCommonName());
}
```

### Testing repository caching

You should also test that repeated calls do not parse again unnecessarily.
A simple repository test can confirm:

- first load fills cache
- second lookup returns from memory
- all disease count stays consistent

---

## Error Handling Checklist

Your XML feature should handle these cases:

- file not found in `assets/`
- malformed XML structure
- missing required tags
- unknown drawable name
- unmatched predicted label
- empty disease list after parsing

### Good defensive habits

- wrap file reading in `try-catch`
- log errors using `Log.e()`
- show a friendly `Toast` or error text
- use placeholder image when drawable is missing
- return empty list instead of crashing where appropriate

### Example logging

```java
Log.e("DiseaseRepository", "Failed to parse diseases.xml", exception);
```

### Example user-facing message

```java
Toast.makeText(context,
        "Disease information could not be loaded. Please try again.",
        Toast.LENGTH_LONG).show();
```

---

## Performance Tips for Week 08

Even though the file is not huge, practice good habits:

- parse once and cache
- reuse the same repository instance
- avoid repeated `getIdentifier()` calls if possible by caching image IDs too
- do list filtering on in-memory data
- do not re-read XML in every adapter bind

### Excellent optimization idea

Add image resource id caching:

```java
private final Map<String, Integer> imageCache = new HashMap<>();
```

Then resolve a drawable only once per image name.
This is optional but demonstrates strong thinking.

---

## Common Student Mistakes in This Week

### Mistake 1: Label mismatch

Problem:

```xml
<label>Tomato Late Blight</label>
```

but the model returns:

```text
Tomato___Late_blight
```

Fix:
Use the exact ML label for lookup and use `commonName` for human-readable display.

### Mistake 2: Parsing on main thread

The app may lag or freeze.
Always prefer background loading.

### Mistake 3: Storing symptoms as one giant paragraph

This makes detail formatting harder.
Use nested `<item>` tags.

### Mistake 4: Using `Activity` context in a singleton

This can leak memory.
Use `getApplicationContext()`.

### Mistake 5: Not handling missing disease entries

A `null` lookup should show fallback UI, not crash.

---

## How This Week Connects to CSE 2206 Concepts

### XML Parsing

This is the obvious syllabus connection.
You must be able to explain:

- XML syntax
- nested structure
- parser event loop
- why `XmlPullParser` is appropriate on Android

### Data Modeling

The `Disease` class is an example of mapping structured text data into Java objects.
This is a core object-oriented concept.

### UI Design

RecyclerView and detail views show how structured data is presented in Android.

### Software Architecture

The repository pattern introduces separation of concerns:

- parser handles parsing
- repository handles loading and caching
- activity handles UI
- adapter handles list binding

That is excellent architecture language for viva.

---

## Sample Viva Questions and Model Answers

### 1. Why did you use XML for the disease library?

Because XML parsing is part of the CSE 2206 syllabus, and Android already uses XML heavily for layouts and resources. It also lets me store structured offline disease information inside the app assets.

### 2. Why did you choose `XmlPullParser`?

`XmlPullParser` is lightweight, memory-efficient, Android-friendly, and gives explicit control over event-by-event parsing in Java.

### 3. What is the role of the `assets/` folder?

The `assets/` folder stores raw files packaged with the APK. I used it to keep `diseases.xml` so the app can load disease information offline.

### 4. How do you handle nested symptom lists?

I created nested `<item>` tags inside `<symptoms>` and used a loop to read each item until the closing `</symptoms>` tag.

### 5. Why not parse XML every time the result screen opens?

That would repeat disk I/O and parsing work. Instead, I cache the parsed data once in a singleton `DiseaseRepository`.

### 6. What if the ML label is missing from XML?

I show a graceful fallback message and keep the app running. This prevents crashes and improves user experience.

### 7. Why is a `Map<String, Disease>` useful?

It allows fast lookup by label after prediction, instead of looping through the whole disease list every time.

### 8. Why do you avoid using the UI thread for parsing?

Because file I/O and parsing can block rendering and cause lag or ANR. Background loading keeps the interface responsive.

### 9. How do you connect XML data to the RecyclerView?

The activity requests a list from the repository and passes it to `DiseaseAdapter`, which binds each `Disease` object to a row layout.

### 10. How would you extend this in the future?

I could add search filters, multilingual disease descriptions, local Room storage, image galleries, and links between prediction history and disease details.

---

## Practice Explanation You Should Be Able to Say Out Loud

> In Week 08 of LeafGuard AI, I created an offline XML disease library. I stored `diseases.xml` in the assets folder and parsed it in Java using `XmlPullParser`. Each XML disease entry is converted into a `Disease` object. I then cache the data inside a singleton `DiseaseRepository`, which provides fast list access for the RecyclerView and fast label lookup for the result screen. This design keeps parsing logic separate from UI logic and improves performance by avoiding repeated parsing.

Practice saying that until it feels natural.

---

## Mini Checklist Before You Move On

- [ ] `diseases.xml` exists in `assets/`
- [ ] labels exactly match ML outputs
- [ ] all important diseases have symptoms, treatment, and prevention
- [ ] parser handles nested `<item>` tags
- [ ] repository caches parsed diseases
- [ ] `DiseaseLibraryActivity` displays all records in RecyclerView
- [ ] `ResultActivity` shows disease details after prediction
- [ ] fallback works for missing labels
- [ ] image placeholder works if drawable is missing
- [ ] parser unit test passes

---

## Debugging Guide

### If the app crashes when loading the XML

Check:

1. Is the file path correct?
2. Is the root element closed properly?
3. Does every `<disease>` have matching end tags?
4. Are attributes wrapped in quotes?
5. Are there illegal characters like raw `&`?

### If RecyclerView shows zero items

Check:

1. Did `loadDiseases()` actually finish?
2. Did the parser return an empty list?
3. Did you call `adapter.notifyDataSetChanged()` or use `updateData()`?
4. Is the XML root tag name the same as your parser expects?

### If result lookup returns null

Check:

1. Does `predictedLabel` match the XML `label` exactly?
2. Did the repository load before `findByLabel()` was called?
3. Is the label being trimmed or changed accidentally?

---

## Testing This Week's Code

1. Build the project from Android Studio.
2. Launch the app on an emulator or real device.
3. Open the disease library screen.
4. Confirm all expected diseases are visible.
5. Tap a disease and check detail content.
6. Run a prediction and verify the result screen shows the matched XML details.
7. Temporarily test an unknown label and confirm fallback behavior.
8. Rotate the device if applicable and ensure the app still behaves correctly.

### Expected behavior

- No XML parse crash
- Disease list appears quickly after first load
- Second open is faster because of cache
- Images appear or fallback placeholder appears
- Result screen shows readable disease details

---

## Evidence Collection for Week 08

Do not forget to save proof of your work in the week folder.

Recommended evidence items:

- screenshot of `diseases.xml` in `assets/`
- screenshot of RecyclerView disease list
- screenshot of disease detail view
- screenshot of result screen showing lookup integration
- Logcat showing successful XML load
- code snippet of `XmlPullParser` event loop
- code snippet of repository caching logic

Store these inside:

```text
roadmap/week-08-xml-disease-library/
```

and your usual progress/evidence folders if your course workflow requires it.

---

## Final Understanding Check

Before you say Week 08 is complete, answer these questions in your own words:

1. Why is `XmlPullParser` better than DOM for this Android feature?
2. Why do labels have to match the ML output exactly?
3. Why is a repository pattern cleaner than parsing XML directly inside an activity?
4. Why should XML be parsed in the background?
5. What does caching improve?
6. What happens if a drawable name is wrong?
7. How do nested `<item>` tags become `List<String>` fields?
8. Why is fallback handling important for real users?

If you can answer these without looking, you are ready for implementation.

---

## Week 08 Takeaway

This week is not just about reading XML.
It is about building a clean data pipeline:

```text
assets XML -> XmlPullParser -> Disease objects -> Repository cache -> RecyclerView / ResultActivity UI
```

That pipeline is exactly the kind of structured thinking CSE 2206 wants you to learn.

Now, after reading these notes, your next step should be:

1. open `roadmap/week-08-xml-disease-library/exercises.md`
2. complete the exercises in order
3. implement the full build task
4. check everything against `validation-checklist.md`

When you finish, explain the whole flow in your own words.
That is how you prove understanding instead of just copying code.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 08 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| **2** | **learning-notes.md** ← *You are here* | **Theory & Learning Notes** |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Week Overview & Objectives](README.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Theory & Learning Notes** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Practice Exercises →](exercises.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 07: Room Database & History](../week-07-room-sqlite-history/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 09: TensorFlow Lite Offline AI ➡](../week-09-tensorflow-lite-offline-ai/README.md) |

---
