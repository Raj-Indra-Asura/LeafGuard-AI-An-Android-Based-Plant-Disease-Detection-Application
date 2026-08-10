# Week 10 Build Task: Add Privacy-Aware Utilities

## Objective

Reconstruct [learning-notes.md section 12](learning-notes.md#12-end-of-week-10-file-inventory-exact-files-exact-code-exact-size).

## Prerequisites

- Week 09 cloud/offline flow passes.
- Week 07 Room has existing v1 data for migration testing.
- Six exercises complete.

## 1. Freeze Earlier Contracts

Record zero changes to PredictionResponse, model tensors, XML catalog, DAO methods, and inference branches.

## 2. Add WorkManager and Permissions

Add only WorkManager KTX. Add coarse/fine location and POST_NOTIFICATIONS manifest permissions. Build.

## 3. Expand Room Safely

Add nullable latitude/longitude, version 2, MIGRATION_1_2, and `addMigrations`. Build and test upgrading a v1 database with existing rows.

## 4. Add Sharing and Optional Location

Expand Result/layout/strings. Share plain text via chooser. Add opt-in checkbox. Request location only during save; denial/unavailability saves null.

## 5. Display Location

Expand history detail/layout. Show coordinates or explicit not-saved text.

## 6. Add Notification Helper and Worker

Create channel, unique 24-hour periodic request, cancellation, permission guard, immutable PendingIntent, and Worker.

## 7. Replace Settings Placeholder

Add one reminder switch, persisted Boolean, Android 13 permission launcher, schedule/cancel behavior, and denial reset.

## 8. Build

```bash
cd android-app-kotlin
./gradlew assembleDebug
```

## 9. Demonstrate Share/Location

Inspect chooser text. Save unchecked, granted, unavailable, and denied cases. Verify Room detail and existing migrated history.

## 10. Demonstrate Reminders

Enable/grant, reopen Settings, re-enable without duplicates, disable/cancel, and revoke permission before Worker notification.

## 11. Boundaries

Do not add background location, maps, image sharing, exact alarms, analytics, navigation redesign, Week 11 test expansion, or Week 12 release work.

## Evidence

Build, share payload, four location states, v1->v2 preservation, location detail, channel, unique work, persisted toggle, cancellation, denial/revocation behavior.

## Done

Snapshot builds, privacy behavior is correct, migration preserves rows, one reminder schedule exists, cancellation works, and prediction contracts remain unchanged.

<!-- NAV_FOOTER_START -->

---

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | **Build Task** | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)