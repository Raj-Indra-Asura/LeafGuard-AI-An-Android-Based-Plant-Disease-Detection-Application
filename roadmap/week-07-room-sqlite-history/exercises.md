# Week 07 Exercises: Room Schema, History, and Delete

## How to Use These Exercises

Complete these before the build task. Save exactly six files under:

```text
docs/evidence/week-07/exercises/
```

| Exercise | Output |
|---:|---|
| 1 | `exercise-01-schema-map.md` |
| 2 | `exercise-02-dao-queries.md` |
| 3 | `exercise-03-database-coroutines.md` |
| 4 | `exercise-04-history-list.md` |
| 5 | `exercise-05-detail-delete-flow.md` |
| 6 | `exercise-06-persistence-demo.md` |

---

## Exercise 1: Map the 10-Column Schema

### Goal

Turn the Week 06 response into one complete local row.

### Task

Create a table with column name, Kotlin type, source, and reason for storing it. Include:

```text
id, model_label, disease, confidence, uncertain,
guidance_available, symptoms, treatment, prevention, timestamp
```

Answer:

1. Why is disease name not a valid primary key?
2. Why store both model label and display disease?
3. Why store confidence as 0.0-1.0 rather than formatted text?
4. Why is timestamp assigned when saving rather than predicting?

### Validation

- [ ] Exactly 10 columns appear.
- [ ] All eight Week 06 values are preserved.
- [ ] `id` is generated and `timestamp` is local save time.

---

## Exercise 2: Write and Predict DAO Queries

### Goal

Understand CRUD and SQL ordering before annotations.

### Task

For each operation, write the SQL or Room annotation and expected return type:

| Operation | SQL/annotation | Return meaning |
|---|---|---|
| Insert one scan | | |
| List all newest first | | |
| Find by ID | | |
| Delete by ID | | |

Given timestamps 1000, 3000, and 2000, predict returned order.

Explain why `LIMIT 1` is still useful when `id` is unique.

### Validation

- [ ] `ORDER BY timestamp DESC` is used.
- [ ] Detail/delete bind `:id`.
- [ ] Delete result is interpreted as affected row count.

---

## Exercise 3: Explain Singleton and Coroutines

### Goal

Keep disk I/O safe and database construction centralized.

### Task

Draw the double-check singleton flow. Explain:

- `@Volatile`
- `synchronized(this)`
- `applicationContext`
- first and second null checks
- why DAO methods are `suspend`
- why `lifecycleScope.launch` is used by Activities

Predict what could go wrong if every click built a new database or if DAO work blocked the UI thread.

### Validation

- [ ] One process-wide instance is the goal.
- [ ] Activity context is not retained.
- [ ] Disk work is not placed directly on the UI thread.

---

## Exercise 4: Design the History List State

### Goal

Connect Room rows to RecyclerView and empty state.

### Task

Create this state table:

| Database result | RecyclerView | Empty message | Adapter count |
|---|---|---|---:|
| Empty list | | | |
| One row | | | |
| Three rows | | | |

Draw:

```text
getAllScans -> HistoryAdapter -> ViewHolder -> item_scan_history.xml
```

Explain why list rows show a summary rather than every saved field.

### Validation

- [ ] Disease, percentage, and formatted time appear in each row.
- [ ] Empty/list visibility is mutually exclusive.
- [ ] Newest-first order comes from SQL.

---

## Exercise 5: Trace Detail and Delete

### Goal

Use primary-key navigation and safe destructive action.

### Task

Trace:

```text
row tap -> EXTRA_SCAN_ID -> getScanById -> render all values
delete tap -> confirmation -> deleteScanById -> finish -> onResume refresh
```

Predict safe behavior for:

- missing Intent extra
- ID that no longer exists
- user cancels deletion
- DAO reports zero deleted rows

### Validation

- [ ] Only ID is passed to detail.
- [ ] Missing/invalid records close safely.
- [ ] Cancel never changes the database.
- [ ] Return-to-list refresh is explained.

---

## Exercise 6: Plan Persistence Evidence

### Goal

Prove Room persistence rather than only current-screen memory.

### Task

Plan and name evidence for:

1. empty history
2. result before saving
3. successful Save state
4. one history row
5. app restart with row preserved
6. two rows in newest-first order
7. complete detail
8. delete cancellation
9. confirmed deletion
10. empty state restored

For each artifact, state one claim it proves and one claim it does not prove.

### Validation

- [ ] App restart is included.
- [ ] Cancel and confirm are both included.
- [ ] Evidence does not claim cloud synchronization.
- [ ] Personal images/database files are not committed.

---

## Completion Rule

Start the build task only when all six exercise files exist and you can explain:

- the exact 10-column schema
- all four DAO methods
- why Room uses a singleton
- why DAO methods are suspend
- newest-first SQL order
- RecyclerView/adapter/view-holder roles
- why detail receives only an ID
- how `onResume` refreshes after delete
- what app restart proves

<!-- NAV_FOOTER_START -->

---

## Week 07 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | **Exercises - current** | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Learning Notes](learning-notes.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Build Task](build-task.md)