# Week 07 Build Task: Persist and Browse Scan History

## Objective

Reconstruct or verify the exact Kotlin Week 07 Room/history slice in [learning-notes.md section 12](learning-notes.md#12-end-of-week-07-file-inventory-exact-files-exact-code-exact-size).

Estimated time: 8 to 10 hours.

---

## Before You Start

- [ ] Week 06 real result flow works.
- [ ] All eight response fields reach ResultActivity.
- [ ] Six Week 07 exercises are complete.
- [ ] You can name the 10 database columns.
- [ ] You understand that history is local and user-triggered.

Evidence folder:

```text
docs/evidence/week-07/
|-- exercises/
|-- android-build.txt
|-- schema-note.md
|-- validation.md
|-- quiz-answers.md
|-- reflection-answers.md
`-- screenshots/
```

---

## Step 1: Freeze the Week 06 Input

Record the eight fields that must survive persistence:

```text
model_label, disease, confidence, uncertain,
guidance_available, symptoms, treatment, prevention
```

Checkpoint: no FastAPI, Keras, Retrofit, or ScanActivity change is required.

---

## Step 2: Add Room Tooling

Expand `app/build.gradle` to the exact 55-line target:

- `kotlin-kapt`
- lifecycle runtime KTX
- RecyclerView
- Room runtime 2.6.1
- Room KTX 2.6.1
- Room compiler through kapt

Build:

```bash
cd android-app-kotlin
./gradlew assembleDebug
```

Do not add location, TFLite, WorkManager, or Week 08 XML dependencies.

---

## Step 3: Create the Entity

Create `database/ScanRecord.kt` from Section 12.

Checkpoint:

- table is `scan_history`
- `id` is auto-generated `Long`
- all eight Week 06 values are present
- timestamp is `Long`
- no later latitude/image metadata is added

Build before continuing.

---

## Step 4: Create the DAO

Create `database/ScanDao.kt` with exactly four suspend methods:

1. insert
2. list newest first
3. find one by ID
4. delete one by ID

Checkpoint: Room compiles every SQL query.

---

## Step 5: Create the Database Singleton

Create `database/AppDatabase.kt`.

Required values:

```text
database name: leafguard.db
version: 1
entities: ScanRecord
```

Use `applicationContext`, `@Volatile`, and synchronized double checking.

Build before UI work.

---

## Step 6: Add Explicit Save to Result

Expand ResultActivity and its layout:

- keep all eight existing render values
- add `buttonSaveHistory`
- disable after tap
- create one complete `ScanRecord`
- insert inside `lifecycleScope.launch`
- show success text

Checkpoint: one tap inserts one row; networking is not rerun.

---

## Step 7: Build the History List

Create/expand:

```text
HistoryAdapter.kt
HistoryActivity.kt
activity_history.xml
item_scan_history.xml
```

Required behavior:

- `LinearLayoutManager`
- list loaded in `onResume`
- DAO controls newest-first order
- adapter displays disease, percentage, and date/time
- empty state toggles correctly
- row tap passes only record ID

Build before detail work.

---

## Step 8: Build Detail and Delete

Create:

```text
HistoryDetailActivity.kt
activity_history_detail.xml
```

Register the Activity in the manifest.

Required behavior:

- reject missing ID
- query one row by ID
- display all eight values plus timestamp
- confirm before delete
- cancel preserves row
- confirm deletes by ID and finishes

Checkpoint: return to History triggers `onResume` and refreshes.

---

## Step 9: Verify Persistence

Run this sequence:

1. open empty History
2. produce and save one result
3. open History
4. terminate and relaunch app
5. verify row remains
6. save another result
7. verify newest first
8. open both detail records

App restart is the proof that data is in Room rather than only an Activity field.

---

## Step 10: Verify Delete and Empty State

For one record:

1. tap Delete
2. tap Cancel
3. verify record remains
4. tap Delete again
5. confirm
6. verify record disappears

Delete remaining records and verify the empty message returns.

---

## Step 11: Validate Exact Boundaries

The teaching snapshot must not add:

- changes to API/model contracts
- automatic save of every result
- location columns
- image URI persistence
- XML disease-library enrichment
- TFLite/offline inference
- sharing, analytics, notifications, or bottom navigation
- destructive migrations

The current evolved app may contain later features. They are not Week 07 evidence.

---

## Evidence to Save

1. Successful Android debug build.
2. Ten-column schema note.
3. Empty history.
4. Save success.
5. One-item history.
6. Restart persistence.
7. Newest-first two-item history.
8. Complete detail.
9. Delete-cancel behavior.
10. Delete-confirm behavior.
11. Empty state after all deletes.

Do not commit application database files or personal leaf images.

---

## Done Means

- exact 7-new/7-expanded snapshot is understood
- app builds
- all eight result fields save
- history persists after restart
- list order is newest first
- empty state is correct
- detail loads by primary key
- delete cancellation and confirmation work
- UI remains responsive during DAO work
- Week 06 inference remains unchanged
- evidence is saved

<!-- NAV_FOOTER_START -->

---

## Week 07 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | **Build Task - current** | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Exercises](exercises.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Validation](validation-checklist.md)