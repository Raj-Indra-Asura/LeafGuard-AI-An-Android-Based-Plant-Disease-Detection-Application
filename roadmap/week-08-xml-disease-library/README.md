# Week 08: Local XML Disease Library

## Week 08 Mindset

Week 07 can persist complete prediction results. Week 08 adds a reviewed local reference catalog that works without calling FastAPI:

> Parse 10 bundled XML entries -> browse summaries -> open complete details -> enrich matching Result guidance before saving history.

This week adds reference content, not new disease classes or inference behavior. The Keras model still has 38 canonical labels, while this project has reviewed local guidance for exactly 10 display names.

---

## Progressive Handoff

| Week | Verified input | New work | Verified output |
|---:|---|---|---|
| 06 | Real cloud result | Model validation | Eight-field result |
| 07 | Eight result values | Room persistence | Local scan history |
| **08** | **Display disease + guidance fields** | **Bundled XML parser/repository/library** | **Offline reference and reviewed enrichment** |
| 09 | Validated cloud contract | TFLite conversion/integration | Offline inference |

```text
assets/diseases.xml
  -> DiseaseXmlParser
  -> DiseaseRepository cache
  -> DiseaseLibraryActivity -> DiseaseDetailActivity
  `-> ResultActivity exact-name lookup -> Room save
```

---

## Product State After Week 08

**Cumulative product contribution: 72%**

The product can now:

- parse exactly 10 reviewed disease entries from a bundled XML asset
- reject empty, incomplete, or duplicate-name catalog data
- cache parsed immutable objects through one repository
- browse disease name, plant, and symptoms preview
- open complete symptoms, treatment, and prevention details
- match a prediction's display-friendly `disease` name to local XML
- replace matching Result guidance before Save is enabled
- preserve backend guidance when no local reviewed entry matches
- save enriched guidance through the unchanged Week 07 Room schema

The product still cannot:

- claim local guidance for all 38 model labels
- search/filter the catalog; that is later UI work
- infer disease from XML
- synchronize content remotely
- edit XML inside the app
- add severity, location, sharing, analytics, or bottom navigation
- run offline TFLite inference; Week 09 owns that boundary

---

## Exact Week 08 Repository Delta

| Change | Count | Files |
|---|---:|---|
| New | 8 | `data/Disease.kt`, `data/DiseaseXmlParser.kt`, `data/DiseaseRepository.kt`, `DiseaseAdapter.kt`, `DiseaseDetailActivity.kt`, `activity_disease_detail.xml`, `item_disease.xml`, `assets/diseases.xml` |
| Expanded | 5 | `AndroidManifest.xml`, `DiseaseLibraryActivity.kt`, `ResultActivity.kt`, `activity_disease_library.xml`, `strings.xml` |
| Gradle changes | 0 | Week 07 already supplies lifecycle and RecyclerView |
| Room/API/model changes | 0 | Existing contracts remain compatible |

Exact cumulative sizes:

| File | Logical lines |
|---|---:|
| `AndroidManifest.xml` | 61 |
| `data/Disease.kt` | 9 |
| `data/DiseaseXmlParser.kt` | 76 |
| `data/DiseaseRepository.kt` | 42 |
| `DiseaseAdapter.kt` | 45 |
| `DiseaseLibraryActivity.kt` | 70 |
| `DiseaseDetailActivity.kt` | 67 |
| `ResultActivity.kt` | 142 |
| `activity_disease_library.xml` | 41 |
| `activity_disease_detail.xml` | 63 |
| `item_disease.xml` | 33 |
| `strings.xml` | 76 |
| `assets/diseases.xml` | 73 |
| **Total** | **798** |

Full contents appear in [learning-notes.md section 12](learning-notes.md#12-end-of-week-08-file-inventory-exact-files-exact-code-exact-size).

---

## Exact XML Contract

Root and repeated structure:

```xml
<diseases>
    <disease>
        <name>...</name>
        <plant>...</plant>
        <symptoms>...</symptoms>
        <treatment>...</treatment>
        <prevention>...</prevention>
    </disease>
</diseases>
```

Required facts:

| Quantity | Value |
|---|---:|
| Reviewed entries | 10 |
| Fields per entry | 5 |
| Duplicate normalized names | 0 |
| Empty required fields | 0 |
| Lookup key | Display-friendly disease name |

The XML `<name>` matches the API `disease` field, such as `Tomato Early Blight`. It does not match canonical `model_label` text such as `Tomato___Early_blight`.

---

## CSE 2206 Connection

Week 08 applies:

- bundled Android assets
- event-driven XML pull parsing
- immutable data models
- repository and cache patterns
- validation and exception handling
- asynchronous file I/O with coroutines
- RecyclerView list/detail navigation
- exact-key integration between local and network data

The central question is:

> How can Android turn bundled structured XML into validated, reusable local objects without coupling parsing directly to every screen?

---

## Milestone Demo

1. Build and launch without requiring the backend.
2. Open Disease Library.
3. Show all 10 entries.
4. Open one detail and show five fields represented.
5. Produce a matching prediction and show local XML guidance source.
6. Save the result and verify enriched guidance in Week 07 history.
7. Produce or simulate an unmatched display name and show safe backend guidance remains.
8. Explain why 10 reviewed entries and 38 model labels are both correct.

---

## Seven-File Learning Loop

| Step | File | Purpose | Output |
|---:|---|---|---|
| 1 | `README.md` | Fix catalog and boundaries | Scope statement |
| 2 | `learning-notes.md` | Learn XML/repository and exact files | Understanding checklist |
| 3 | `exercises.md` | Practise schema/parser/lookup | Six exercise files |
| 4 | `build-task.md` | Build and verify library/enrichment | Working milestone |
| 5 | `validation-checklist.md` | Prove parsing and integration | Pass/fail evidence |
| 6 | `quiz.md` | Recall exact contracts | At least 14/18 |
| 7 | `reflection.md` | Explain evidence and Week 09 handoff | Reflection answers |

---

## Exact Completion Contract

| Quantity | Required value |
|---|---:|
| New files | 8 |
| Expanded files | 5 |
| Complete changed/new lines | 798 |
| XML entries | 10 |
| Required fields per entry | 5 |
| New Gradle dependencies | 0 |
| Matching guidance behavior | Local XML replaces three guidance fields |
| Unmatched behavior | Existing backend guidance remains |
| Week 07 Room schema changes | 0 |
| Week 09 offline inference changes | 0 |

Do not move to Week 09 until the milestone and validation checklist pass.

<!-- NAV_FOOTER_START -->

---

## Week 08 Navigation

| Step | File | Description |
|---:|---|---|
| **1** | **README.md** - current | Scope and exact catalog |
| 2 | [learning-notes.md](learning-notes.md) | Theory and complete source inventory |
| 3 | [exercises.md](exercises.md) | Guided practice |
| 4 | [build-task.md](build-task.md) | Implementation guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation and evidence |
| 6 | [quiz.md](quiz.md) | Knowledge assessment |
| 7 | [reflection.md](reflection.md) | Reflection and handoff |

[Previous: Week 07](../week-07-room-sqlite-history/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 09](../week-09-tensorflow-lite-offline-ai/README.md)