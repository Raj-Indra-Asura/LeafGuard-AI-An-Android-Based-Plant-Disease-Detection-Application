# Week 07 Quiz: Room and Local Scan History

## Instructions

Answer after building and validating the history feature. Do not read the key first.

Passing score: **14 out of 18**.

---

## Multiple Choice

### 1. What does Week 07 add?

A) Real model training
B) Local Room scan history
C) XML disease library
D) Offline TFLite

Answer: ____

### 2. How many columns are in `scan_history`?

A) 5
B) 8
C) 10
D) 38

Answer: ____

### 3. What is the primary key?

A) Disease name
B) Timestamp
C) Auto-generated `id`
D) Model label

Answer: ____

### 4. Which query returns newest records first?

A) `ORDER BY timestamp ASC`
B) `ORDER BY timestamp DESC`
C) `GROUP BY disease`
D) No ordering

Answer: ____

### 5. Why are DAO methods `suspend`?

A) To animate RecyclerView
B) To support non-blocking database work
C) To generate IDs
D) To request Internet permission

Answer: ____

### 6. Why use `applicationContext` to build Room?

A) It has camera access
B) It avoids retaining an Activity context
C) It sorts SQL
D) It formats timestamps

Answer: ____

### 7. Why does detail receive only the record ID?

A) Intents cannot carry strings
B) Detail reloads authoritative data from Room
C) IDs contain every field
D) RecyclerView requires it

Answer: ____

### 8. Why load history in `onResume`?

A) It runs only once
B) It refreshes after returning from detail/delete
C) It creates the schema
D) It starts FastAPI

Answer: ____

---

## True or False

### 9. Week 07 changes the FastAPI response fields.

Answer: ____

### 10. Saving is explicit rather than automatic.

Answer: ____

### 11. Disease name is guaranteed unique and is a safe primary key.

Answer: ____

### 12. App restart is stronger persistence evidence than Activity recreation alone.

Answer: ____

### 13. Delete should occur immediately without confirmation.

Answer: ____

---

## Short Answer

### 14. Name all 10 columns and identify their sources.

Answer:

### 15. Explain the roles of Entity, DAO, and RoomDatabase in LeafGuard.

Answer:

### 16. Trace Save to History from button tap to generated row ID.

Answer:

### 17. Explain the list -> detail -> delete -> refreshed list lifecycle.

Answer:

### 18. Name three things Week 07 must not implement or claim yet.

Answer:

---

## Answer Key

1. B
2. C
3. C
4. B
5. B
6. B
7. B
8. B
9. False
10. True
11. False
12. True
13. False

Short-answer requirements:

| Question | Full-credit ideas |
|---:|---|
| 14 | `id`, eight Week 06 fields, `timestamp`; generated/local versus response sources |
| 15 | Entity maps object/table, DAO owns typed SQL, RoomDatabase owns configuration/singleton access |
| 16 | Disable button, create complete record, lifecycle coroutine, DAO insert, generated ID, feedback |
| 17 | Adapter passes ID, detail queries row, confirmation/delete by ID, finish, History `onResume` reload |
| 18 | Any three of XML library, location, offline TFLite, notifications, analytics, cloud sync, model changes |

---

## Remediation Map

| Missed questions | Review |
|---|---|
| 1, 9, 18 | Progressive boundary |
| 2, 3, 11, 14 | Entity/schema |
| 4, 5, 15, 16 | DAO/coroutines |
| 6 | Database singleton/context |
| 7, 8, 13, 17 | List/detail/delete lifecycle |
| 10, 12 | Explicit save and persistence proof |

If your score is below 14, review mapped sections and retake before Week 08.

<!-- NAV_FOOTER_START -->

---

## Week 07 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | **Quiz - current** | [Reflection](reflection.md)

[Previous: Validation](validation-checklist.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Reflection](reflection.md)