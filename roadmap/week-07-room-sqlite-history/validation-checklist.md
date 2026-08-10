# Week 07 Validation Checklist: Room Scan History

## Milestone Demo

> Save a Week 06 result, show it newest-first in History after app restart, open its complete detail, cancel deletion once, confirm deletion once, and show the empty state when no records remain.

Every required item must be yes before Week 08.

Record results in `docs/evidence/week-07/validation.md`:

```text
Item:
Method: build | source inspection | emulator | app restart
Expected:
Actual:
Evidence:
Result: PASS | FAIL | NOT TESTED
```

---

## 1. Progressive Boundary

- [ ] Week 06 result flow still works.
- [ ] All eight response values reach ResultActivity.
- [ ] FastAPI and Keras code have zero Week 07 changes.
- [ ] Retrofit/ScanActivity contract has zero Week 07 changes.
- [ ] Save is explicit rather than automatic.
- [ ] History is described as local device/app data.
- [ ] Week 08 XML library work is deferred.

Pass rule: all 7.

---

## 2. Exact Repository State

- [ ] Exactly 7 new Week 07 files are documented.
- [ ] Exactly 7 expanded Week 07 files are documented.
- [ ] Complete changed/new total is 864 logical lines.
- [ ] `kotlin-kapt` is enabled.
- [ ] Room runtime, KTX, and compiler are version 2.6.1.
- [ ] lifecycle runtime KTX is present.
- [ ] RecyclerView dependency is present.
- [ ] Android debug build succeeds.
- [ ] No later-week dependency is required.

Pass rule: all 9.

---

## 3. Entity and Schema

- [ ] Entity table name is `scan_history`.
- [ ] Schema has exactly 10 columns.
- [ ] `id` is an auto-generated `Long` primary key.
- [ ] `model_label` is preserved.
- [ ] `disease` is preserved.
- [ ] `confidence` remains a `Float` on 0.0-1.0 scale.
- [ ] `uncertain` is preserved.
- [ ] `guidance_available` is preserved.
- [ ] Symptoms, treatment, and prevention are preserved.
- [ ] `timestamp` records save time as `Long`.
- [ ] No location/image/later metadata appears.

Pass rule: all 11.

---

## 4. DAO and Database

- [ ] DAO has exactly four methods.
- [ ] Insert returns generated row ID.
- [ ] List query orders by `timestamp DESC`.
- [ ] Detail query binds primary key and uses `LIMIT 1`.
- [ ] Delete query binds primary key.
- [ ] Delete returns affected row count.
- [ ] All DAO methods are `suspend`.
- [ ] Database version is 1.
- [ ] Database name is `leafguard.db`.
- [ ] `applicationContext` is used.
- [ ] Singleton uses `@Volatile` and synchronized double checking.

Pass rule: all 11.

---

## 5. Save Flow

- [ ] Result screen shows Save to History.
- [ ] Save button is disabled after tap.
- [ ] One complete `ScanRecord` is created.
- [ ] Timestamp is assigned at save.
- [ ] Insert runs through `lifecycleScope.launch`.
- [ ] Success feedback is shown.
- [ ] Button text changes to Saved state.
- [ ] Repeated tap in the same screen does not insert a duplicate.
- [ ] Saving does not rerun networking or inference.

Pass rule: all 9.

---

## 6. History List

- [ ] Empty database shows empty message.
- [ ] Empty database hides RecyclerView.
- [ ] Non-empty database shows RecyclerView.
- [ ] Non-empty database hides empty message.
- [ ] RecyclerView uses `LinearLayoutManager`.
- [ ] Adapter count matches DAO rows.
- [ ] Row displays disease.
- [ ] Row displays formatted percentage.
- [ ] Row displays formatted date/time.
- [ ] Newest saved row appears first.
- [ ] History reloads in `onResume`.

Pass rule: all 11.

---

## 7. Detail and Navigation

- [ ] Row tap passes only `EXTRA_SCAN_ID`.
- [ ] Detail Activity is registered in manifest.
- [ ] Missing ID shows feedback and closes safely.
- [ ] Missing database row shows feedback and closes safely.
- [ ] Detail queries Room by primary key.
- [ ] Detail shows model label and disease.
- [ ] Detail shows confidence and uncertainty.
- [ ] Detail shows guidance availability.
- [ ] Detail shows symptoms, treatment, and prevention.
- [ ] Detail shows formatted save timestamp.

Pass rule: all 10.

---

## 8. Delete Behavior

- [ ] Delete button opens confirmation dialog.
- [ ] Dialog clearly describes local removal.
- [ ] Cancel preserves the row.
- [ ] Confirm calls delete by ID.
- [ ] Positive deleted count shows success.
- [ ] Detail closes after confirmed action.
- [ ] Returning History refreshes automatically.
- [ ] Deleted row no longer appears.
- [ ] Deleting all rows restores empty state.

Pass rule: all 9.

---

## 9. Persistence and Responsiveness

- [ ] Saved row remains after Activity recreation.
- [ ] Saved row remains after app process restart/relaunch.
- [ ] Two different rows retain independent IDs.
- [ ] Full details match their corresponding list item.
- [ ] Save UI remains responsive.
- [ ] History load UI remains responsive.
- [ ] Delete UI remains responsive.
- [ ] No database operation is placed directly as blocking main-thread work.

Pass rule: all 8.

---

## 10. Evidence and Understanding

- [ ] Android build output saved.
- [ ] Ten-column schema note saved.
- [ ] Empty-state evidence saved.
- [ ] Save-success evidence saved.
- [ ] Restart-persistence evidence saved.
- [ ] Newest-first evidence saved.
- [ ] Complete-detail evidence saved.
- [ ] Delete-cancel evidence saved.
- [ ] Delete-confirm evidence saved.
- [ ] Final-empty-state evidence saved.
- [ ] Quiz score is at least 14/18.
- [ ] Reflection uses observed evidence.
- [ ] Progress tracker is updated.
- [ ] No database file or personal image is committed.

Pass rule: all 14.

---

## Failure Routing

| Failure | Return to | Focused recheck |
|---|---|---|
| kapt/Room compile fails | Gradle/entity/DAO | `assembleDebug` |
| Insert fails | Entity and DAO insert | Save one row |
| Wrong list order | DAO SQL | Two timestamps |
| Empty message wrong | History visibility | Empty/non-empty states |
| Detail wrong record | ID extra/query | Two distinct rows |
| Delete not reflected | `onResume` reload | Confirm/delete/back |
| Record lost on restart | Room construction/save | Relaunch app |
| UI freezes | suspend/lifecycle usage | Save/load/delete interaction |

After repair, rerun the focused check and the complete milestone demo.

---

## Completion Criteria

Week 07 is complete only when:

1. Exact 14-file snapshot builds.
2. All eight result values persist in a 10-column row.
3. Save is explicit and asynchronous.
4. History persists after restart and sorts newest first.
5. Empty/list states are correct.
6. Detail loads by generated ID.
7. Delete cancel and confirm both work.
8. Week 06 inference remains unchanged.
9. Evidence and understanding checks pass.

<!-- NAV_FOOTER_START -->

---

## Week 07 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | **Validation - current** | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Build Task](build-task.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Quiz](quiz.md)