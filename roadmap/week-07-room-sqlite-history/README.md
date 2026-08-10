# Week 07: Save and Review Scan History With Room

## Week 07 Mindset

Week 06 can produce a real cloud-model result, but that result disappears when the screen closes. Week 07 adds an explicit local persistence choice:

> View a prediction -> tap Save to History -> store all eight result values locally -> browse newest-first records -> open details -> delete with confirmation.

This week changes persistence, not inference. Android networking, FastAPI, the Keras model, and the eight-field response contract remain unchanged.

---

## Progressive Handoff

| Week | Verified input | New work | Verified output |
|---:|---|---|---|
| 03 | Camera/gallery URI | Image input | Previewable leaf image |
| 04 | Image concept | FastAPI contract | Stable eight-field API |
| 05 | URI + API | Retrofit | Android result flow |
| 06 | Working pipeline | Real Keras validation | Real cloud result |
| **07** | **Eight result values** | **Room persistence and history UI** | **Saved local scan records** |
| 08 | Saved/result guidance fields | XML disease library | Local reference content |

```text
ResultActivity
  -> explicit Save
  -> ScanRecord entity
  -> ScanDao
  -> leafguard.db
  -> HistoryActivity
  -> HistoryDetailActivity
```

---

## Product State After Week 07

**Cumulative product contribution: 65%**

The product can now:

- save a returned result only when the user requests it
- preserve all eight Week 06 response values
- assign an auto-generated local primary key
- record save time as epoch milliseconds
- display saved scans newest first
- distinguish an empty database from a populated list
- open one record by ID
- delete one record after confirmation
- retain records after Activity recreation and app restart

The product still cannot:

- synchronize history across devices
- enrich records from the Week 08 XML library
- attach later location metadata
- run offline TensorFlow Lite inference
- perform production-grade migrations beyond schema version 1
- treat saved model output as a confirmed diagnosis

---

## Exact Week 07 Repository Delta

The Kotlin track is primary. Week 07 adds **7 files**, expands **7 files**, and leaves Week 06 inference/networking unchanged.

| Change | Count | Files |
|---|---:|---|
| New | 7 | `database/ScanRecord.kt`, `database/ScanDao.kt`, `database/AppDatabase.kt`, `HistoryAdapter.kt`, `HistoryDetailActivity.kt`, `activity_history_detail.xml`, `item_scan_history.xml` |
| Expanded | 7 | `app/build.gradle`, `AndroidManifest.xml`, `ResultActivity.kt`, `HistoryActivity.kt`, `activity_result.xml`, `activity_history.xml`, `strings.xml` |
| Backend/model changes | 0 | Week 06 real inference remains unchanged |
| Later-week additions | 0 | No XML library, location, offline model, notification, analytics, or UI redesign |

Exact cumulative sizes:

| File | Logical lines |
|---|---:|
| `app/build.gradle` | 55 |
| `AndroidManifest.xml` | 58 |
| `database/ScanRecord.kt` | 29 |
| `database/ScanDao.kt` | 21 |
| `database/AppDatabase.kt` | 34 |
| `HistoryAdapter.kt` | 54 |
| `HistoryActivity.kt` | 52 |
| `HistoryDetailActivity.kt` | 112 |
| `ResultActivity.kt` | 98 |
| `activity_history.xml` | 23 |
| `activity_history_detail.xml` | 106 |
| `item_scan_history.xml` | 31 |
| `activity_result.xml` | 122 |
| `strings.xml` | 69 |
| **Total** | **864** |

These are complete end-of-week files, not added-line counts. Full contents appear in [learning-notes.md section 12](learning-notes.md#12-end-of-week-07-file-inventory-exact-files-exact-code-exact-size).

---

## Exact Local Schema

`ScanRecord` maps to table `scan_history` with 10 columns:

| Column | Type | Source |
|---|---|---|
| `id` | `Long`, primary key, auto-generated | Room |
| `model_label` | `String` | Week 06 response |
| `disease` | `String` | Week 06 response |
| `confidence` | `Float` | Week 06 response |
| `uncertain` | `Boolean` | Week 06 response |
| `guidance_available` | `Boolean` | Week 06 response |
| `symptoms` | `String` | Week 06 response |
| `treatment` | `String` | Week 06 response |
| `prevention` | `String` | Week 06 response |
| `timestamp` | `Long` | Android save time |

Room persists booleans in SQLite-compatible form. The Kotlin entity remains the source used by the app.

---

## CSE 2206 Connection

Week 07 applies:

- relational tables, rows, columns, and primary keys
- object-relational mapping with Room
- DAO interfaces and SQL queries
- singleton database construction
- asynchronous disk I/O with coroutines
- RecyclerView adapter/view-holder architecture
- Activity lifecycle refresh with `onResume`
- CRUD operations: create, read, and delete in this slice

The central question is:

> How can Android persist structured prediction data safely without blocking the UI or coupling storage directly to networking?

---

## Milestone Demo

1. Produce a Week 06 result.
2. Tap **Save to History**.
3. Open History and show the new record first.
4. Close and reopen the app; show the record remains.
5. Open the record and compare all eight saved values.
6. Delete it after confirmation.
7. Return to History and show the list refreshes.
8. Delete all records and show the empty state.
9. Explain that the database is local to this app/device.

---

## Seven-File Learning Loop

| Step | File | Purpose | Output |
|---:|---|---|---|
| 1 | `README.md` | Fix scope/schema | Boundary statement |
| 2 | `learning-notes.md` | Learn Room and exact files | Understanding checklist |
| 3 | `exercises.md` | Practise schema/queries/state | Six exercise files |
| 4 | `build-task.md` | Build and verify persistence | Working history flow |
| 5 | `validation-checklist.md` | Prove CRUD and boundaries | Pass/fail evidence |
| 6 | `quiz.md` | Recall exact contracts | At least 14/18 |
| 7 | `reflection.md` | Explain evidence and Week 08 handoff | Reflection answers |

---

## Exact Completion Contract

| Quantity | Required value |
|---|---:|
| New files | 7 |
| Expanded files | 7 |
| Changed/new logical lines | 864 |
| Room entities | 1 |
| Database tables | 1 |
| Table columns | 10 |
| DAO methods | 4 |
| Database version | 1 |
| Android debug build | Successful |
| Backend/model contract changes | 0 |
| Later-week feature claims | 0 |

Do not move to Week 08 until the milestone and validation checklist pass.

<!-- NAV_FOOTER_START -->

---

## Week 07 Navigation

| Step | File | Description |
|---:|---|---|
| **1** | **README.md** - current | Scope and exact schema |
| 2 | [learning-notes.md](learning-notes.md) | Theory and complete source inventory |
| 3 | [exercises.md](exercises.md) | Guided practice |
| 4 | [build-task.md](build-task.md) | Implementation guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation and evidence |
| 6 | [quiz.md](quiz.md) | Knowledge assessment |
| 7 | [reflection.md](reflection.md) | Reflection and handoff |

[Previous: Week 06](../week-06-cloud-ml-model/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 08](../week-08-xml-disease-library/README.md)