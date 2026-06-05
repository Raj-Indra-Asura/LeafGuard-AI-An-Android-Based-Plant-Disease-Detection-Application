# Week 8 Reflection Journal

## XML Disease Library

**Date Range**: ________________
**Student Name**: ________________

---

## Part 1: Understanding XML in Android

**In your own words, explain what XML (eXtensible Markup Language) is and how it differs from JSON:**
```
[Explain the syntax, structure, and use cases for XML. Why did we choose XML over JSON
for the disease library? Mention the CSE 2206 requirement for XML parsing.]
```

**Why is the disease library stored in the assets/ folder rather than a database?**
```
[Discuss: static vs dynamic data, offline availability, read-only nature,
file size considerations.]
```

---

## Part 2: XML File Design

**Show your disease_library.xml structure (simplified):**
```xml
<!-- Paste a sample with 2-3 disease entries showing the full tag structure -->
```

**Explain your design decisions:**
- Root element name chosen:
- Child element names chosen:
- Why you used child elements instead of attributes:
- How the label tag connects to the ML model output:

**How many diseases did you add to your XML file?**
```
Number: _____
List them:
```

---

## Part 3: XmlPullParser Implementation

**Explain the XmlPullParser event model (START_TAG, TEXT, END_TAG):**
```
[Walk through the parsing logic step by step. Draw a table or diagram showing
what happens for each event type when parsing a <disease> entry.]
```

**Show your core parsing loop:**
```java
// Paste your DiseaseXmlParser.java parse() method
```

**What state tracking variables did you use and why?**
```
[Explain currentDisease, currentTag, and any other variables]
```

---

## Part 4: The Disease Model Class

**Show your Disease.java class:**
```java
// Paste your Disease.java
```

**Why did you choose these fields?**
```
[Explain each field and how it will be used in the UI]
```

---

## Part 5: Performance — Caching Strategy

**What happens if you parse the XML file every time the user views a disease result?**
```
[Discuss: repeated disk I/O, parsing time, XML parsing overhead]
```

**What caching approach did you implement?**
```java
// Show your caching code (Map<String, Disease> or similar)
```

**Where do you store the cached data to avoid re-parsing?**
```
[Discuss: singleton, Application class, ViewModel, static field]
```

---

## Part 6: Integration with ResultActivity

**Describe the flow when a prediction result is displayed:**
```
1. ML model returns label: "Tomato___Early_blight"
2. [Continue the flow through your code]
3. Disease symptoms appear on screen
```

**Show the method call that connects prediction result to XML data:**
```java
// Show how you look up disease info using the predicted label
```

**What happens if the predicted label is NOT found in the XML?**
```
[Describe your fallback/error handling]
```

---

## Part 7: DiseaseLibraryActivity

**How is the disease library accessed from the main menu?**
```
[Describe the navigation path]
```

**How did you display the list of diseases?**
```
[RecyclerView? ListView? What does the list item look like?]
```

**What happens when a user taps on a disease in the list?**
```
[Describe navigation to detail view or in-place expansion]
```

---

## Part 8: SAX vs DOM vs XmlPullParser Comparison

**Compare the three XML parsing approaches in Android:**

| Approach | Memory Usage | Speed | Code Complexity | Best For |
|----------|-------------|-------|----------------|---------|
| DOM | | | | |
| SAX | | | | |
| XmlPullParser | | | | |

**Why is XmlPullParser the recommended approach for Android?**
```
[Your explanation]
```

---

## Part 9: Error Handling

**What exceptions can XML parsing throw?**
1.
2.

**What happens if disease_library.xml is missing or corrupted?**
```
[Describe your error handling strategy]
```

**Show your try-catch block:**
```java
// Your error handling code
```

---

## Part 10: Self-Assessment

**Rate your understanding (1-5):**

| Concept | Rating (1-5) | Evidence |
|---------|-------------|---------|
| XML syntax | | |
| XmlPullParser events | | |
| Parsing Disease objects | | |
| Assets folder usage | | |
| Caching parsed data | | |
| ResultActivity integration | | |
| DiseaseLibraryActivity | | |

**Total hours spent**: _______ hours

---

## Part 11: CSE 2206 Exam Preparation

**Practice answering this viva question:**
*"Explain how you implemented XML parsing in your project and why you chose XmlPullParser over other approaches."*
```
[Write your answer in 5-8 sentences as if speaking to your examiner]
```

**Practice answering:**
*"What is the purpose of the assets/ folder in Android development?"*
```
[Your answer]
```

---

## Part 12: Challenges and Solutions

**Biggest technical challenge this week:**
```
Problem:
Solution:
What I learned:
```

**Most confusing concept:**
```
[Describe and explain]
```

**Something that surprised you:**
```
[Any unexpected behavior or insight]
```

---

## Part 13: Looking Ahead to Week 9

**Week 9 covers TensorFlow Lite offline AI. Based on what you know, what questions do you have?**
1.
2.
3.

**How does the XML disease library connect to Week 9's offline inference?**
```
[Think about: offline prediction → look up disease info in XML → display without internet]
```

**Your goal for Week 9:**
```
[State one clear objective]
```

---

## Evidence of Completion

- [ ] disease_library.xml created with 6+ diseases
- [ ] Disease.java model class complete
- [ ] DiseaseXmlParser.java working
- [ ] Caching implemented (no re-parsing on every call)
- [ ] ResultActivity shows symptoms/treatment from XML
- [ ] DiseaseLibraryActivity shows full disease list
- [ ] Error handling for missing/corrupted XML

**Links to code:**

**Screenshot of DiseaseLibraryActivity running:**

**Screenshot of ResultActivity with XML data:**

---

## Instructor Feedback

**Comments:**

**Recommendations:**

---

**Reflection completed on**: ________________
**Signature**: ________________
