# Week 08 Build Task: Parse and Use the Local Disease Library

## Objective

Reconstruct or verify the exact Kotlin Week 08 slice in [learning-notes.md section 12](learning-notes.md#12-end-of-week-08-file-inventory-exact-files-exact-code-exact-size).

Estimated time: 8 to 10 hours.

---

## Before You Start

- [ ] Week 07 save/history/detail/delete milestone passes.
- [ ] The eight response fields remain unchanged.
- [ ] Six Week 08 exercises are complete.
- [ ] You understand that XML contains 10 reviewed entries, not 38 model classes.
- [ ] You can distinguish `disease` from `model_label`.

Evidence folder:

```text
docs/evidence/week-08/
|-- exercises/
|-- android-build.txt
|-- xml-counts.txt
|-- catalog-contract.md
|-- validation.md
|-- quiz-answers.md
|-- reflection-answers.md
`-- screenshots/
```

---

## Step 1: Freeze Earlier Contracts

Record:

```text
Model labels: 38 canonical model_label values
Reviewed XML: 10 display disease names
Room: unchanged 10-column ScanRecord
Result lookup key: API disease field
```

Checkpoint: no Gradle, FastAPI, Keras, Retrofit, or Room schema change is required.

---

## Step 2: Add and Count the XML Asset

Create `app/src/main/assets/diseases.xml` from Section 12.

Run counts for disease and five child tags. Every count must equal 10.

Checkpoint:

- 10 unique display names
- five non-empty fields each
- no label/severity/location fields

---

## Step 3: Add Model and Strict Parser

Create:

```text
data/Disease.kt
data/DiseaseXmlParser.kt
```

Parser must reject:

- missing/blank required fields
- duplicate normalized names
- empty catalog

Build:

```bash
cd android-app-kotlin
./gradlew assembleDebug
```

---

## Step 4: Add Cached Repository

Create `data/DiseaseRepository.kt`.

Checkpoint:

- application context
- singleton instance
- asset opened in one place
- parsed list cached
- lookup normalizes display name
- no hardcoded fallback records

Build before UI work.

---

## Step 5: Build Library List

Create/expand:

```text
DiseaseAdapter.kt
DiseaseLibraryActivity.kt
item_disease.xml
activity_disease_library.xml
```

Required behavior:

- parse on `Dispatchers.IO`
- loading indicator
- RecyclerView with `LinearLayoutManager`
- exactly 10 items
- summary contains name, plant, symptoms preview
- safe empty/error state
- row passes only disease display name

---

## Step 6: Build Detail

Create `DiseaseDetailActivity.kt` and `activity_disease_detail.xml`; register the Activity.

Required behavior:

- reject missing name
- query cached repository
- reject absent entry safely
- display name, plant, symptoms, treatment, prevention
- perform lookup on `Dispatchers.IO`

Build again.

---

## Step 7: Enrich Result Guidance

Expand ResultActivity:

1. render backend result
2. disable Save
3. lookup XML by display `disease`
4. replace three guidance fields only for a match
5. show local-source status for a match
6. retain backend guidance on no match/error
7. re-enable Save

Checkpoint: saving a matching Result stores local XML guidance in the unchanged Week 07 entity.

---

## Step 8: Demonstrate Offline Reference

Stop FastAPI and open Disease Library.

Verify:

- all 10 entries still load
- details still open
- no Internet request occurs

This proves reference availability, not offline prediction.

---

## Step 9: Demonstrate Matching and Unmatched Results

Matching case:

- use a display name present in XML
- verify local source message
- compare three guidance fields with XML
- save and verify Room detail

Unmatched case:

- use a display name absent from XML
- verify existing backend guidance remains
- verify no blank replacement

---

## Step 10: Prove Parser Failure Behavior

In a temporary test copy, remove one required field or duplicate one name. Rebuild/run the focused check.

Expected:

- parser rejects catalog
- Library shows safe error/empty state
- no fallback data pretends success
- Result retains backend guidance

Restore the valid asset and rerun milestone.

---

## Step 11: Preserve Week Boundaries

Week 08 must not require:

- search/filter UI
- severity chips
- hardcoded fallback catalog
- location or image metadata
- sharing, analytics, notifications, bottom navigation
- changes to Room schema
- changes to API/model contracts
- TFLite conversion or offline classifier

---

## Evidence to Save

1. Successful Android build.
2. Six XML count outputs.
3. 10-entry list with backend stopped.
4. Complete detail.
5. Matching local-source Result.
6. Enriched Room history detail.
7. Unmatched guidance preservation.
8. Malformed-catalog rejection.
9. Catalog contract note.

---

## Done Means

- exact 8-new/5-expanded snapshot is understood
- app builds
- XML has 10 complete unique entries
- parser validates and rejects defects
- repository caches parsed objects
- list and detail work without backend
- matching Result uses local guidance
- unmatched/error Result preserves existing guidance
- Save waits for lookup and persists final text
- Week 07 Room and Week 06 inference remain unchanged
- evidence is saved

<!-- NAV_FOOTER_START -->

---

## Week 08 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | **Build Task - current** | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Exercises](exercises.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Validation](validation-checklist.md)