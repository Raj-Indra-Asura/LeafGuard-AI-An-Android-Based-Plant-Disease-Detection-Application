# Week 10 Validation: Utilities and Privacy

## Boundary

- [ ] Cloud/offline prediction unchanged.
- [ ] Eight-field response unchanged.
- [ ] XML guidance and Room DAO unchanged.
- [ ] Week 11/12 work deferred.

## Exact State

- [ ] 2 new and 11 expanded files.
- [ ] 1,088 logical lines.
- [ ] WorkManager is the only dependency addition.
- [ ] Build succeeds.

## Sharing

- [ ] ACTION_SEND uses text/plain.
- [ ] Chooser opens.
- [ ] Disease/confidence/guidance/disclaimer included.
- [ ] Image/location/model/database excluded.
- [ ] Canceling chooser does not alter app data.

## Location

- [ ] Checkbox is opt-in and unchecked by default.
- [ ] No location prompt until checked Save.
- [ ] Coarse/fine permissions declared/requested.
- [ ] Unchecked saves null.
- [ ] Granted saves last-known coordinates when available.
- [ ] Granted/unavailable saves null.
- [ ] Denied explains and saves null.
- [ ] SecurityException falls back safely.

## Migration

- [ ] Entity columns are nullable Double.
- [ ] Database version is 2.
- [ ] Migration adds two nullable REAL columns.
- [ ] Migration registered.
- [ ] Existing v1 rows survive and show not-saved location.
- [ ] New rows preserve all earlier fields.
- [ ] No destructive migration.

## History Detail

- [ ] Both coordinates display when present.
- [ ] Five decimal formatting used.
- [ ] Null displays explicit not-saved state.
- [ ] Other detail fields remain unchanged.

## Notifications

- [ ] Stable channel ID/name/description.
- [ ] Channel created before posting/scheduling.
- [ ] Android 13 permission requested only on enable.
- [ ] Worker rechecks permission.
- [ ] PendingIntent immutable.
- [ ] Worker returns success.
- [ ] Notification auto-cancels on tap.

## WorkManager/Settings

- [ ] One unique 24-hour periodic request.
- [ ] Re-enable does not duplicate work.
- [ ] Disable cancels unique work.
- [ ] Preference survives Settings reopen.
- [ ] Denial resets switch/preference and cancels.
- [ ] Timing described as approximate.

## Failure/Privacy

- [ ] Location denial never blocks save.
- [ ] Notification denial never crashes.
- [ ] Revoked permission prevents posting safely.
- [ ] No background location tracking.
- [ ] No coordinates shared.
- [ ] No exact alarm claim.

## Evidence

- [ ] Build and exact snapshot saved.
- [ ] Share text saved.
- [ ] Location unchecked/granted/denied evidence saved.
- [ ] Migration preservation saved.
- [ ] Reminder enable/reopen/re-enable/disable saved.
- [ ] Permission denial/revocation saved.
- [ ] Quiz >=14/18, reflection/progress complete.

## Completion

All checks pass and no model/API changes or privacy overreach appear.

<!-- NAV_FOOTER_START -->

---

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | **Validation** | [Quiz](quiz.md) | [Reflection](reflection.md)