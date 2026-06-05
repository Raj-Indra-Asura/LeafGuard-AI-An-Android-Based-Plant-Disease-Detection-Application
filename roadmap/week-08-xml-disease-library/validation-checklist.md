# Week 08 Validation Checklist — XML Disease Library

> Complete all items before marking Week 08 done.

---

## 1. XML File Quality

| # | Check | ✓/✗ |
|---|-------|-----|
| 1.1 | `assets/disease_library.xml` exists | |
| 1.2 | XML opens in browser without parse errors | |
| 1.3 | Root element is `<diseases>` | |
| 1.4 | At least 10 `<disease>` entries present | |
| 1.5 | All 8 required fields present per disease | |
| 1.6 | No empty `<label>` elements | |
| 1.7 | Labels match model output exactly (e.g., `Tomato___Late_blight`) | |
| 1.8 | UTF-8 encoding declared in XML header | |
| 1.9 | Covers at least 4 different crops | |
| 1.10 | Includes both diseased and healthy entries | |

---

## 2. Disease.java Model Class

| # | Check | ✓/✗ |
|---|-------|-----|
| 2.1 | Class has all 8 fields: label, commonName, scientificName, affectedCrop, symptoms, treatment, prevention, severity | |
| 2.2 | Default (no-arg) constructor present | |
| 2.3 | All getters and setters present | |
| 2.4 | Class compiles without errors | |
| 2.5 | Fields use correct Java types (String) | |

---

## 3. DiseaseXmlParser.java

| # | Check | ✓/✗ |
|---|-------|-----|
| 3.1 | Uses `XmlPullParser` (NOT DOM or SAX) | |
| 3.2 | `parse(InputStream)` method signature correct | |
| 3.3 | Parses correct count of diseases (matches XML) | |
| 3.4 | All 8 fields extracted for each disease | |
| 3.5 | Text trimmed (no leading/trailing whitespace) | |
| 3.6 | `currentTag` reset at END_TAG event | |
| 3.7 | Declares `throws XmlPullParserException, IOException` | |
| 3.8 | InputStream is not closed inside parser (caller closes it) | |

---

## 4. DiseaseRepository.java

| # | Check | ✓/✗ |
|---|-------|-----|
| 4.1 | Implements singleton pattern (`getInstance()`) | |
| 4.2 | `loadIfNeeded()` parses XML only once per app session | |
| 4.3 | `loadIfNeeded()` is `synchronized` | |
| 4.4 | Uses `getApplicationContext()` to avoid memory leaks | |
| 4.5 | Diseases stored in `Map<String, Disease>` keyed by label | |
| 4.6 | `findByLabel(String)` returns correct disease | |
| 4.7 | Returns `null` gracefully if label not found | |
| 4.8 | Handles `XmlPullParserException` without crashing | |
| 4.9 | Handles `IOException` without crashing | |
| 4.10 | `getAllDiseases()` returns all entries | |

---

## 5. ResultActivity Integration

| # | Check | ✓/✗ |
|---|-------|-----|
| 5.1 | Disease info loaded on background thread | |
| 5.2 | UI updated via `runOnUiThread()` | |
| 5.3 | Correct disease shown for Tomato Late Blight prediction | |
| 5.4 | Correct disease shown for Apple Scab prediction | |
| 5.5 | "Healthy" prediction shown in green color | |
| 5.6 | High severity shown in red | |
| 5.7 | Unknown label shows raw label text (no crash) | |
| 5.8 | Works when no internet connection | |

---

## 6. DiseaseLibraryActivity

| # | Check | ✓/✗ |
|---|-------|-----|
| 6.1 | Activity listed in AndroidManifest.xml | |
| 6.2 | Opens from MainActivity menu or button | |
| 6.3 | RecyclerView displays all diseases | |
| 6.4 | Diseases show common name, scientific name, crop, severity | |
| 6.5 | Severity color coded (red/amber/green) | |
| 6.6 | SearchView filters by disease name | |
| 6.7 | SearchView filters by crop name | |
| 6.8 | Diseases sorted alphabetically | |
| 6.9 | Loads on background thread (no ANR) | |
| 6.10 | Back button returns to MainActivity | |

---

## 7. Testing

| # | Check | ✓/✗ |
|---|-------|-----|
| 7.1 | Unit test: single disease parsed correctly | |
| 7.2 | Unit test: multiple diseases parsed correctly | |
| 7.3 | Unit test: empty XML returns empty list | |
| 7.4 | Unit test: malformed XML throws exception | |
| 7.5 | Manual: open Disease Library 5× — no crash | |
| 7.6 | Manual: search and clear — list restores fully | |
| 7.7 | Manual: rotate screen in DiseaseLibraryActivity — no crash | |
| 7.8 | Logcat: no `OutOfMemoryError` during XML parsing | |

---

## Scoring

| Section | Items | Passing |
|---------|-------|---------|
| XML File Quality | 10 | |
| Disease Model | 5 | |
| XML Parser | 8 | |
| Repository | 10 | |
| ResultActivity | 8 | |
| Library Activity | 10 | |
| Testing | 8 | |
| **Total** | **59** | |

**Pass threshold: 50/59 (≥85%)**

---

## Common Mistakes to Avoid

1. **Forgetting to close InputStream** — always close in `finally` or use try-with-resources
2. **Parsing on main thread** — always use `Executors` or `AsyncTask`
3. **Label mismatch** — model output uses `___` separators, ensure XML labels match exactly
4. **Not resetting currentTag** — failing to reset at `END_TAG` causes wrong field assignments
5. **OutOfMemoryError** — do NOT load entire file as String; always use streaming XmlPullParser
6. **Multiple parses** — without `isLoaded` flag, XML is parsed on every lookup (slow)
7. **Context leak** — using Activity context in singleton; always use `getApplicationContext()`


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
| **5** | **validation-checklist.md** ← *You are here* | **Validation & Verification** |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Build Implementation Guide](build-task.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Validation & Verification** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Knowledge Assessment Quiz →](quiz.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 07: Room Database & History](../week-07-room-sqlite-history/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 09: TensorFlow Lite Offline AI ➡](../week-09-tensorflow-lite-offline-ai/README.md) |

---
