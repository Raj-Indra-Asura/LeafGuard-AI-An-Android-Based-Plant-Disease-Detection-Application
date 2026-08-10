# Week 08 Validation Checklist: XML Disease Library

## Milestone Demo

> Browse all 10 reviewed entries without the backend, open complete detail, show matching local XML guidance on Result and in saved Room history, then show an unmatched name safely preserves backend guidance.

Every item must be yes before Week 09.

Record each result in `docs/evidence/week-08/validation.md`. `NOT TESTED` is not a pass.

---

## 1. Progressive Boundary

- [ ] Week 07 save/history flow still works.
- [ ] Room schema remains unchanged.
- [ ] FastAPI/Keras/Retrofit contracts remain unchanged.
- [ ] XML is reference content, not inference.
- [ ] Ten reviewed entries are distinguished from 38 model labels.
- [ ] Week 09 TFLite work is deferred.

Pass rule: all 6.

---

## 2. Exact Repository State

- [ ] Exactly 8 new files are documented.
- [ ] Exactly 5 expanded files are documented.
- [ ] Complete total is 798 logical lines.
- [ ] No Gradle dependency is added.
- [ ] Complete files match learning-notes Section 12.
- [ ] Android debug build succeeds.
- [ ] No later search/severity/location/share/polish code is required.

Pass rule: all 7.

---

## 3. XML Catalog Integrity

- [ ] Asset path is `app/src/main/assets/diseases.xml`.
- [ ] Root element is `<diseases>`.
- [ ] Exactly 10 `<disease>` entries exist.
- [ ] Exactly 10 names exist.
- [ ] Exactly 10 plants exist.
- [ ] Exactly 10 symptoms exist.
- [ ] Exactly 10 treatments exist.
- [ ] Exactly 10 prevention values exist.
- [ ] Required text values are non-empty.
- [ ] Normalized names are unique.
- [ ] Catalog contains no invented severity/label/location fields.

Pass rule: all 11.

---

## 4. Parser Behavior

- [ ] Parser uses `XmlPullParser` events.
- [ ] New disease resets temporary fields.
- [ ] Text maps to the correct five properties.
- [ ] End disease creates an immutable object.
- [ ] Incomplete entry is rejected.
- [ ] Blank required value is rejected.
- [ ] Duplicate normalized name is rejected.
- [ ] Empty catalog is rejected.
- [ ] Original display capitalization is preserved.
- [ ] No hardcoded fallback hides a failure.

Pass rule: all 10.

---

## 5. Repository and I/O

- [ ] Repository uses application context.
- [ ] Repository is a singleton.
- [ ] Asset filename is defined once.
- [ ] First access parses the XML.
- [ ] Later access returns cached list.
- [ ] `findByName` normalizes lookup.
- [ ] Library parsing runs on `Dispatchers.IO`.
- [ ] Detail lookup runs on `Dispatchers.IO`.
- [ ] Result lookup runs on `Dispatchers.IO`.
- [ ] UI updates return through lifecycle coroutine context.

Pass rule: all 10.

---

## 6. Library List

- [ ] Loading indicator is visible during load.
- [ ] RecyclerView uses `LinearLayoutManager`.
- [ ] Adapter count becomes 10.
- [ ] Each row shows name.
- [ ] Each row shows plant.
- [ ] Each row shows a two-line symptoms preview.
- [ ] Successful list hides empty state.
- [ ] Failure hides list and shows safe empty/error state.
- [ ] Library works with FastAPI stopped.
- [ ] Row tap passes only display disease name.

Pass rule: all 10.

---

## 7. Disease Detail

- [ ] Detail Activity is registered.
- [ ] Missing name shows safe feedback and closes.
- [ ] Unknown name shows safe feedback and closes.
- [ ] Valid name loads cached repository entry.
- [ ] Detail shows disease name.
- [ ] Detail shows plant.
- [ ] Detail shows full symptoms.
- [ ] Detail shows full treatment.
- [ ] Detail shows full prevention.
- [ ] Detail works with backend stopped.

Pass rule: all 10.

---

## 8. Result Enrichment

- [ ] Initial Week 06 result still renders.
- [ ] Save is disabled during XML lookup.
- [ ] Lookup uses API `disease`, not `model_label`.
- [ ] Exact matching name loads local entry.
- [ ] Match replaces symptoms only with XML symptoms.
- [ ] Match replaces treatment only with XML treatment.
- [ ] Match replaces prevention only with XML prevention.
- [ ] Match shows local-library source status.
- [ ] Match sets reviewed guidance available.
- [ ] No match preserves existing backend guidance.
- [ ] Read/parse error preserves existing backend guidance.
- [ ] Save is enabled after every terminal lookup path.
- [ ] Saved matching Room record contains enriched guidance.

Pass rule: all 13.

---

## 9. Failure and Boundary Behavior

- [ ] Malformed catalog never crashes Library.
- [ ] Malformed catalog never blanks Result guidance.
- [ ] No unreviewed fallback list appears.
- [ ] No network request is needed for catalog browsing.
- [ ] XML does not alter confidence/uncertainty/model label.
- [ ] XML does not alter Room table structure.
- [ ] Search and severity are absent from Week 08 target.
- [ ] Offline model inference is absent.

Pass rule: all 8.

---

## 10. Evidence and Understanding

- [ ] Android build output saved.
- [ ] Six XML count outputs saved.
- [ ] 10-entry offline list evidence saved.
- [ ] Complete detail evidence saved.
- [ ] Matching local-source Result saved.
- [ ] Enriched Room detail evidence saved.
- [ ] Unmatched preservation evidence saved.
- [ ] Malformed-catalog behavior saved.
- [ ] Catalog contract note saved.
- [ ] Quiz score is at least 14/18.
- [ ] Reflection cites observed evidence.
- [ ] Progress tracker is updated.
- [ ] No personal image/app database is committed.

Pass rule: all 13.

---

## Failure Routing

| Failure | Return to | Focused recheck |
|---|---|---|
| Wrong counts | XML asset | Six count commands |
| Incomplete accepted | Parser validation | Malformed test copy |
| List fails | Repository/I/O | Backend-off Library |
| Detail wrong | Name extra/lookup | One known entry |
| Match not enriched | `disease` lookup key | Exact XML name |
| Guidance blanked | Error/unmatched fallback | Missing-name case |
| Wrong Room text | Save timing | Match then save/detail |

---

## Completion Criteria

Week 08 is complete only when:

1. Exact 13-file snapshot builds.
2. XML contains 10 complete unique entries.
3. Parser rejects incomplete/duplicate/empty catalogs.
4. Repository parses once and caches.
5. List/detail work without backend.
6. Matching Result uses local reviewed guidance.
7. Unmatched/error Result preserves backend guidance.
8. Room saves final enriched values without schema changes.
9. XML is not described as inference.

<!-- NAV_FOOTER_START -->

---

## Week 08 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | **Validation - current** | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Build Task](build-task.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Quiz](quiz.md)