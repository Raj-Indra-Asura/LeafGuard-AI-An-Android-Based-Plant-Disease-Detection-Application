# Week 08: Reflection — XML Disease Library

> Fill in each section honestly. This reflection helps you consolidate
> understanding and prepares you for viva questions on Week 08 topics.

---

## 1. XML Parsing Understanding

**Q: Explain in your own words how XmlPullParser works (4+ sentences).**

> *Sample answer for reference — write your OWN version:*
> XmlPullParser is an event-driven, streaming XML parser. You call `parser.next()` in a loop and it returns integer event codes: START_TAG (opening element), TEXT (text content), END_TAG (closing element), and END_DOCUMENT (end of file). Unlike DOM, it does NOT load the entire XML tree into memory — it reads one event at a time, making it memory-efficient for large files. I use it by checking the current tag name at START_TAG events to know which Disease field to populate, then reading the text content at TEXT events to fill those fields.

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## 2. Why XML Over JSON for the Disease Library?

**Q: Justify the choice of XML over a JSON file or SQLite database (3+ sentences).**

> *Sample answer:* XML was the required format for CSE 2206 to demonstrate XmlPullParser skills. The disease library is static, human-readable, version-controlled data that doesn't change at runtime — it's shipped with the app in the assets folder. XML tags provide self-documenting structure (e.g., `<symptoms>`) that is easy to maintain and extend. JSON would be lighter, but XML was chosen for its educational value and for the course assessment requirement.

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## 3. Repository Pattern Benefit

**Q: What problem does DiseaseRepository solve that wasn't present in earlier weeks? (2+ sentences).**

> *Sample answer:* Without DiseaseRepository, ResultActivity would re-parse the XML file every time a prediction is made — every file read and parse takes ~20ms even for small files, and repeated reads waste I/O. DiseaseRepository parses once, stores diseases in a HashMap, and all subsequent lookups are O(1) in-memory operations. It also provides a single point to add caching logic, error handling, and potential future features like remote disease updates.

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## 4. Biggest Challenge

**Q: What was the most difficult part of implementing the XML disease library this week?**

*Options to consider: getting the XML structure right, debugging parser logic, integrating with ResultActivity, threading, label matching...*

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## 5. Threading Mistake & Fix

**Q: What happens if you call `repo.loadIfNeeded()` on the main thread? Why is this wrong?**

> *Sample answer:* Android throws a `NetworkOnMainThreadException`-equivalent (or StrictMode violation) and the UI freezes while XML is being read and parsed. The main thread should never do file I/O because even small operations can cause "Application Not Responding" (ANR) if they take more than ~5ms on the main thread. The fix is to call `loadIfNeeded()` inside an `Executors.newSingleThreadExecutor()` block and update UI via `runOnUiThread()`.

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## 6. Label Matching Problem

**Q: The model predicts `"Tomato___Late_blight"` but your XML has `<label>Tomato___Late_blight</label>`. What could go wrong?**

> *Things to consider: extra spaces, different capitalization, underscores vs spaces, different dataset naming conventions.*

Your answer:
_______________________________________________________________
_______________________________________________________________

---

## 7. Week 08 Self-Assessment

Rate yourself honestly (1 = not understood, 5 = can teach this):

| Skill | 1–5 |
|-------|-----|
| Understand XmlPullParser event loop | |
| Write Disease.java model class from scratch | |
| Implement full DiseaseXmlParser.java | |
| Implement DiseaseRepository singleton | |
| Integrate disease info into ResultActivity | |
| Build DiseaseLibraryActivity with RecyclerView | |
| Write unit tests for XML parser | |

**Honest total: _____ / 35**

---

## 8. What Would You Improve?

If you had 2 more hours, what would you add or improve?

*Ideas: disease detail screen, crop filter buttons, symptom image gallery, remote XML updates, disease severity statistics dashboard...*

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## 9. Connection to Previous Weeks

**Q: How does this week connect to Week 07 (Room database)?**

> *Sample answer:* Week 07 stores scan history in Room (label, confidence, date, image path). This week adds meaning to that label — when a user views their history, the label `"Tomato___Late_blight"` can now be enriched with symptoms and treatment from the XML repository. Together, they form the complete result experience: past scan record (Room) + disease knowledge base (XML).

Your answer:
_______________________________________________________________
_______________________________________________________________

---

## 10. Key Takeaway

**Write ONE sentence that summarizes your most important learning from Week 08:**

_______________________________________________________________
