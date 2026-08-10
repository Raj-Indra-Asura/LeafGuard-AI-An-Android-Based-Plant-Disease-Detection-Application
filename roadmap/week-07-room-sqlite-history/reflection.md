# Week 07 Reflection: From Transient Result to Local Record

## Purpose

Save your answers in:

```text
docs/evidence/week-07/reflection-answers.md
```

Use your own words and cite observed build, save, restart, list, detail, and delete evidence.

---

## Section 1: Progressive Growth

### Prompt 1: Week 06 Input

Which eight values existed before Room, and why must history preserve all of them?

### Prompt 2: Week 07 Ability

What can the product do after Week 07 that an Intent or Activity field could not guarantee?

### Prompt 3: Honest Boundary

What local-persistence abilities remain outside Week 07, such as cloud sync or schema migration strategy?

---

## Section 2: Room Understanding

### Prompt 4: Schema

Explain why the table has 10 columns and why `id` and `timestamp` have different sources from the eight prediction values.

### Prompt 5: DAO

Explain each of the four DAO methods and the meaning of its return type.

### Prompt 6: Singleton

Explain `@Volatile`, synchronized double checking, and `applicationContext` in your own words.

### Prompt 7: Coroutines

Why are DAO methods suspend functions, and how does `lifecycleScope` connect database work to an Activity lifecycle?

---

## Section 3: UI and Lifecycle

### Prompt 8: Explicit Save

Why does the user choose Save rather than the app automatically storing every prediction?

### Prompt 9: Newest-First List

Why is ordering expressed in SQL instead of sorting in the adapter?

### Prompt 10: Detail by ID

Why does HistoryDetailActivity receive only a primary key and reload the row from Room?

### Prompt 11: `onResume`

What stale-UI problem would occur if History loaded only in `onCreate`?

### Prompt 12: Delete Confirmation

Describe cancel and confirm behavior and the evidence that proved each.

---

## Section 4: Debugging and Evidence

### Prompt 13: One Debugging Cycle

```text
Observed problem:
First hypothesis:
Discriminating check:
Actual cause:
Fix:
Focused retest:
Full milestone retest:
Evidence:
```

### Prompt 14: Strongest Persistence Evidence

Which evidence best proves Room persistence rather than in-memory state? Explain what it still does not prove.

---

## Section 5: Confidence Table

| Topic | Confidence 1-10 | Evidence | Next practice if below 7 |
|---|---:|---|---|
| Entity/schema | | | |
| DAO SQL | | | |
| Database singleton | | | |
| Coroutine database work | | | |
| RecyclerView adapter | | | |
| Empty/list state | | | |
| Detail by ID | | | |
| Delete/lifecycle refresh | | | |

Scores of 9 or 10 require specific evidence.

---

## Section 6: Week 08 Handoff

Complete three sentences:

1. **What exists now:** describe the complete locally saved record flow.
2. **What guidance exists now:** explain that saved guidance came from the backend response.
3. **What Week 08 adds:** describe a local XML disease reference library without changing Room inference records.

---

## Completion Check

- [ ] I explained all 10 columns.
- [ ] I explained Entity, DAO, and database roles.
- [ ] I cited app-restart persistence evidence.
- [ ] I cited newest-first evidence.
- [ ] I explained primary-key detail navigation.
- [ ] I cited delete cancel and confirm evidence.
- [ ] I separated local history from cloud sync.
- [ ] I did not claim Week 08 XML work is complete.
- [ ] I completed the Week 08 handoff.
- [ ] I updated the progress tracker.

<!-- NAV_FOOTER_START -->

---

## Week 07 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | **Reflection - current**

[Previous: Quiz](quiz.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 08](../week-08-xml-disease-library/README.md)