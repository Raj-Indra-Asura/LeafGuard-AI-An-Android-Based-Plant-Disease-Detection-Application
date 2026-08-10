# Week 08 Quiz: XML Disease Library

## Instructions

Answer after building and validating Week 08. Passing score: **14 out of 18**.

---

## Multiple Choice

### 1. What does Week 08 add?

A) Offline model inference
B) Bundled reviewed XML reference library
C) New Room schema
D) Cloud deployment

Answer: ____

### 2. How many reviewed XML entries exist?

A) 5
B) 10
C) 38
D) 50

Answer: ____

### 3. How many required fields does each entry have?

A) 3
B) 5
C) 8
D) 10

Answer: ____

### 4. Which prediction field is the XML lookup key?

A) `model_label`
B) `confidence`
C) Display-friendly `disease`
D) `uncertain`

Answer: ____

### 5. Why use a repository cache?

A) To retrain the model
B) To avoid reparsing the same bundled XML for every screen
C) To replace Room
D) To enable Internet

Answer: ____

### 6. Which dispatcher owns asset parsing?

A) `Dispatchers.IO`
B) Main only
C) No coroutine
D) GPU dispatcher

Answer: ____

### 7. What happens when XML has no matching name?

A) Blank all guidance
B) Preserve existing backend guidance
C) Crash
D) Change the model label

Answer: ____

### 8. Why is Save disabled during lookup?

A) Room is unavailable
B) Prevent storing pre-enrichment guidance before lookup completes
C) XML changes confidence
D) Camera permission is needed

Answer: ____

---

## True or False

### 9. Ten reviewed XML entries mean the model has only ten classes.

Answer: ____

### 10. Duplicate names differing only by case should be rejected.

Answer: ____

### 11. Library browsing requires FastAPI to be running.

Answer: ____

### 12. A malformed catalog should silently load hardcoded fallback records.

Answer: ____

### 13. XML guidance performs disease inference.

Answer: ____

---

## Short Answer

### 14. Name all five XML fields and explain their uses.

Answer:

### 15. Trace pull-parser events from `<disease>` start to immutable object creation.

Answer:

### 16. Explain first and later repository access and why application context is used.

Answer:

### 17. Trace a matching Result from display name lookup through Room save.

Answer:

### 18. Name three features or claims outside Week 08.

Answer:

---

## Answer Key

1. B
2. B
3. B
4. C
5. B
6. A
7. B
8. B
9. False
10. True
11. False
12. False
13. False

| Question | Full-credit ideas |
|---:|---|
| 14 | name, plant, symptoms, treatment, prevention and their display/guidance roles |
| 15 | reset fields, map text events, validate five fields/duplicates, create immutable Disease |
| 16 | first opens/parses/caches; later returns cache; app context avoids retaining Activity |
| 17 | API `disease`, IO lookup, replace three guidance fields, source state, enable Save, unchanged Room insert |
| 18 | Any three of search, severity, fallback catalog, location, sharing, analytics, TFLite, model/API/Room changes |

---

## Remediation Map

| Missed questions | Review |
|---|---|
| 1, 9, 13, 18 | Progressive boundary |
| 2, 3, 10, 14, 15 | XML schema/parser |
| 5, 6, 11, 12, 16 | Repository/cache/I/O |
| 4, 7, 8, 17 | Result lookup/enrichment |

Retake after reviewing mapped sections if below 14.

<!-- NAV_FOOTER_START -->

---

## Week 08 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | **Quiz - current** | [Reflection](reflection.md)

[Previous: Validation](validation-checklist.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Reflection](reflection.md)