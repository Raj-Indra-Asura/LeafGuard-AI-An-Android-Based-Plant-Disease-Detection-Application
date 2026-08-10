# Week 08 Exercises: XML Parsing and Local Guidance

## How to Use These Exercises

Complete these before the build task. Save exactly six files under:

```text
docs/evidence/week-08/exercises/
```

| Exercise | Output |
|---:|---|
| 1 | `exercise-01-xml-schema.md` |
| 2 | `exercise-02-parser-state.md` |
| 3 | `exercise-03-repository-cache.md` |
| 4 | `exercise-04-library-flow.md` |
| 5 | `exercise-05-result-enrichment.md` |
| 6 | `exercise-06-boundary-evidence.md` |

---

## Exercise 1: Validate the Catalog Schema

Create a table for `name`, `plant`, `symptoms`, `treatment`, and `prevention`. Record XML tag, Kotlin property, purpose, and one example.

Run counts for `<disease>` and all five child tags. Explain why every count must be 10 and why 10 reviewed entries do not conflict with 38 model labels.

Validation:

- [ ] Exactly 10 entries and 50 required child values are identified.
- [ ] Lookup uses display disease name.
- [ ] XML is described as reference content, not inference.

---

## Exercise 2: Trace Parser Events and Rejections

Draw the pull-parser state from `<disease>` start to immutable object creation.

Predict the result for:

| Input defect | Expected result |
|---|---|
| Missing treatment | |
| Blank plant | |
| Duplicate name with different case | |
| Empty `<diseases>` | |
| Unknown extra tag | |

Explain why normalized names are used for comparison while original capitalization is preserved.

Validation:

- [ ] All required-field failures are rejected.
- [ ] Duplicate normalization uses trim + lowercase.
- [ ] No hardcoded fallback hides malformed XML.

---

## Exercise 3: Design Repository Caching

Draw first access and later access:

```text
Activity -> repository -> cache?
```

Explain application context, singleton double checking, immutable cached list, and exact-name lookup. Predict how many parses occur when list then detail then Result all use the repository.

Validation:

- [ ] First access parses once.
- [ ] Later access reuses cached objects.
- [ ] Activity context is not retained.

---

## Exercise 4: Design List and Detail States

Create a state table for loading, 10 entries, empty list, parser error, row tap, missing detail name, and unmatched detail name.

Draw:

```text
repository -> adapter -> item summary -> name extra -> detail lookup
```

Explain why list shows a symptoms preview while detail shows all five values.

Validation:

- [ ] Loading and error states are distinct.
- [ ] Detail receives only display name.
- [ ] Library works with backend stopped.

---

## Exercise 5: Trace Result Enrichment

Complete:

| Case | Guidance displayed | Source status | Save enabled when |
|---|---|---|---|
| Exact XML match | | | |
| No XML match | | | |
| XML parse/read error | | | |

Explain why lookup uses `disease`, not `model_label`, and why Save waits until lookup finishes. Trace matching local text into the Week 07 Room row.

Validation:

- [ ] Only symptoms/treatment/prevention are replaced.
- [ ] Existing guidance remains on no match/error.
- [ ] No blank guidance is stored.

---

## Exercise 6: Plan Boundary Evidence

Plan evidence for:

1. Android build
2. XML count output
3. 10-entry list without backend
4. complete detail
5. matching local-source Result
6. enriched Room history detail
7. unmatched guidance preservation
8. malformed-catalog safe error

For each, write one proved claim and one unproved claim.

Validation:

- [ ] No model-accuracy claim appears.
- [ ] No search/severity/offline-inference evidence is claimed.
- [ ] Personal images and app data are not committed.

---

## Completion Rule

Start the build task only when all six files exist and you can explain:

- five-field XML schema and 10-entry scope
- pull-parser event flow and rejection rules
- repository singleton/cache
- asynchronous asset loading
- adapter/list/detail responsibilities
- display-name lookup contract
- matching versus unmatched Result behavior
- why XML does not perform inference

<!-- NAV_FOOTER_START -->

---

## Week 08 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | **Exercises - current** | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Learning Notes](learning-notes.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Build Task](build-task.md)