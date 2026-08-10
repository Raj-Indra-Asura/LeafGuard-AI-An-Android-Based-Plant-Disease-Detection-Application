# Week 08 Reflection: Reviewed Local Guidance

## Purpose

Save answers in:

```text
docs/evidence/week-08/reflection-answers.md
```

Use your own words and cite build, XML count, library, detail, matching, unmatched, and Room evidence.

---

## Section 1: Progressive Growth

### Prompt 1

What did Week 07 already persist, and what local reference ability did Week 08 add?

### Prompt 2

Why are 10 reviewed entries and 38 model labels both correct?

### Prompt 3

What does XML guidance prove, and why is it not offline inference?

---

## Section 2: XML and Architecture

### Prompt 4

Explain all five XML fields and why each is required.

### Prompt 5

Explain the pull-parser state machine and three catalog defects it rejects.

### Prompt 6

Why are `Disease` values immutable?

### Prompt 7

Explain repository singleton, application context, caching, and normalized lookup.

### Prompt 8

Why does asset parsing run on `Dispatchers.IO`?

---

## Section 3: UI and Integration

### Prompt 9

Explain the list -> adapter -> display-name extra -> detail lookup flow.

### Prompt 10

Why does Result lookup use `disease` instead of `model_label`?

### Prompt 11

Explain matching, unmatched, and parser-error guidance behavior.

### Prompt 12

Why must Save wait for lookup, and how did you prove Room stored the enriched text?

---

## Section 4: Debugging and Evidence

### Prompt 13

```text
Observed problem:
First hypothesis:
Discriminating check:
Actual cause:
Fix:
Focused retest:
Milestone retest:
Evidence:
```

### Prompt 14

Which evidence best proves the catalog works offline? What does it not prove?

---

## Section 5: Confidence Table

| Topic | Confidence 1-10 | Evidence | Next practice if below 7 |
|---|---:|---|---|
| XML schema | | | |
| Pull parsing | | | |
| Validation/rejection | | | |
| Repository caching | | | |
| Coroutine asset I/O | | | |
| RecyclerView list/detail | | | |
| Result exact-name lookup | | | |
| Room enrichment | | | |

---

## Section 6: Week 09 Handoff

Complete three sentences:

1. **What exists now:** describe local reviewed reference guidance.
2. **What still requires cloud:** describe current prediction inference.
3. **What Week 09 adds:** describe TFLite/offline inference without confusing it with XML lookup.

---

## Completion Check

- [ ] I explained 10 versus 38.
- [ ] I explained all five XML fields.
- [ ] I cited parser rejection evidence.
- [ ] I explained repository caching.
- [ ] I cited backend-off list/detail evidence.
- [ ] I cited matching and unmatched Result evidence.
- [ ] I cited enriched Room history evidence.
- [ ] I did not describe XML as inference.
- [ ] I completed the Week 09 handoff.
- [ ] I updated the progress tracker.

<!-- NAV_FOOTER_START -->

---

## Week 08 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | **Reflection - current**

[Previous: Quiz](quiz.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 09](../week-09-tensorflow-lite-offline-ai/README.md)