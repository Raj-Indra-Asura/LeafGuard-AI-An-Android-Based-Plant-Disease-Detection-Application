# Week 08: Learning Notes - XML Disease Library

## 1. XML Fundamentals

### What is XML?

XML (eXtensible Markup Language) is a text-based format for structured data storage.

**Key characteristics:**
- Human-readable
- Self-describing
- Hierarchical structure
- Platform-independent
- Strict syntax rules

### XML vs JSON

| Feature | XML | JSON |
|---------|-----|------|
| Verbosity | More verbose | Compact |
| Metadata | Attributes supported | No attributes |
| Comments | Supported | Not supported |
| CSE 2206 | Required by syllabus | Optional |

**For LeafGuard:** XML chosen because it's a CSE 2206 requirement.

---

## 2. XML Structure for Disease Library

```xml
<?xml version="1.0" encoding="UTF-8"?>
<diseases>
    <disease id="1">
        <label>Tomato_Late_Blight</label>
        <commonName>Tomato Late Blight</commonName>
        <scientificName>Phytophthora infestans</scientificName>
        <symptoms>Brown spots on leaves</symptoms>
        <treatment>Apply fungicide</treatment>
        <prevention>Crop rotation</prevention>
    </disease>
</diseases>
```

**Design decisions:**
- Root element: `<diseases>`
- Each disease in `<disease>` tag
- `label` matches ML model output
- All information in child tags

---

## 3. XmlPullParser API

### Basic Usage

```java
XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
XmlPullParser parser = factory.newPullParser();

InputStream is = getAssets().open("disease_library.xml");
parser.setInput(is, "UTF-8");

int eventType = parser.getEventType();
while (eventType != XmlPullParser.END_DOCUMENT) {
    switch (eventType) {
        case XmlPullParser.START_TAG:
            String tagName = parser.getName();
            break;
        case XmlPullParser.TEXT:
            String text = parser.getText();
            break;
        case XmlPullParser.END_TAG:
            break;
    }
    eventType = parser.next();
}
```

### Parsing Disease Objects

```java
public List<Disease> parseDiseases(InputStream is) throws Exception {
    List<Disease> diseases = new ArrayList<>();
    Disease current = null;
    String currentTag = null;

    XmlPullParser parser = getParser(is);
    int event = parser.getEventType();

    while (event != XmlPullParser.END_DOCUMENT) {
        if (event == XmlPullParser.START_TAG) {
            currentTag = parser.getName();
            if ("disease".equals(currentTag)) {
                current = new Disease();
            }
        } else if (event == XmlPullParser.TEXT && current != null) {
            String text = parser.getText().trim();
            if ("label".equals(currentTag)) {
                current.setLabel(text);
            } else if ("commonName".equals(currentTag)) {
                current.setCommonName(text);
            }
            // ... more fields
        } else if (event == XmlPullParser.END_TAG) {
            if ("disease".equals(parser.getName()) && current != null) {
                diseases.add(current);
                current = null;
            }
        }
        event = parser.next();
    }

    return diseases;
}
```

---

## 4. Disease Model Class

```java
public class Disease {
    private String label;
    private String commonName;
    private String scientificName;
    private String symptoms;
    private String treatment;
    private String prevention;

    // Constructor, getters, setters
}
```

---

## 5. Assets Folder

**Location:** `app/src/main/assets/`

**Accessing:**
```java
AssetManager am = getAssets();
InputStream is = am.open("disease_library.xml");
```

**Note:** Assets are read-only. Use for static data.

---

## 6. Error Handling

```java
try {
    List<Disease> diseases = parser.parseDiseases(is);
} catch (XmlPullParserException e) {
    // XML format error
    Log.e(TAG, "XML parsing error", e);
} catch (IOException e) {
    // File not found
    Log.e(TAG, "Cannot read file", e);
}
```

---

## CSE 2206 Exam Preparation

**Q: Why use XML for disease library?**

A: XML is a CSE 2206 syllabus requirement for demonstrating XML parsing skills. It provides structured, human-readable storage for disease information. XML supports comments and metadata better than JSON. The disease library is static data stored in assets, making XML appropriate for this use case.

**Q: Explain XmlPullParser vs SAX vs DOM.**

A: XmlPullParser is Android's recommended approach - memory-efficient, pull-based API where developer controls parsing. SAX is event-driven (push-based), calling handlers automatically. DOM loads entire XML into memory as tree structure - simple but memory-intensive. For Android, XmlPullParser is best.

---

**Summary:** XML parsing is straightforward with XmlPullParser. Key is understanding START_TAG, TEXT, END_TAG event flow and building objects incrementally.
