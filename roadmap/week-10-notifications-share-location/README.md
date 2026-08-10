# Week 10: Sharing, Optional Location, and Scan Reminders

## Mindset

Week 09 completes cloud/offline prediction, guidance, and history. Week 10 adds three user utilities without changing inference:

> Share a text result, optionally attach last-known location when saving, and schedule/cancel one daily local scan reminder.

Privacy and denial handling are part of the feature: sharing excludes image/location, location is opt-in, denial still saves normally, and notifications remain disabled without permission.

## Progressive Handoff

| Week | Input | Week 10 use |
|---:|---|---|
| 07 | Room history | Add optional nullable location through migration |
| 08 | Final guidance | Include reviewed text in share template |
| 09 | Cloud/offline result | Utility actions reuse either mode |
| **10** | Complete result workflow | Share, location, reminder settings |
| 11 | Complete feature set | Automated testing/debug/performance |

## Product State

**Cumulative completion: 88%**

Now available:

- plain-text `ACTION_SEND` chooser with result disclaimer
- optional location checkbox during Save
- coarse/fine runtime permission request only after opt-in
- save without location after denial/unavailability
- nullable latitude/longitude in Room version 2
- non-destructive migration from schema 1 to 2
- location/not-saved state in history detail
- notification channel
- Android 13 notification permission handling
- one unique approximately daily WorkManager reminder
- persisted reminder switch and cancellation

Still outside Week 10:

- background location tracking or maps
- sharing image files or coordinates
- exact-alarm reminders
- analytics, bottom navigation redesign, broad settings, animations
- Week 11 automated test expansion/performance profiling
- Week 12 signing and release packaging

## Exact Delta

| Change | Count | Files |
|---|---:|---|
| New | 2 | `utils/NotificationHelper.kt`, `utils/ScanReminderWorker.kt` |
| Expanded | 11 | Gradle, manifest, Room entity/database, Result/HistoryDetail/Settings Activities and layouts, strings |
| Model/API/XML changes | 0 | Week 09 and earlier contracts are reused |

| File | Lines |
|---|---:|
| `app/build.gradle` | 61 |
| `AndroidManifest.xml` | 64 |
| `database/ScanRecord.kt` | 33 |
| `database/AppDatabase.kt` | 45 |
| `utils/NotificationHelper.kt` | 82 |
| `utils/ScanReminderWorker.kt` | 20 |
| `ResultActivity.kt` | 222 |
| `HistoryDetailActivity.kt` | 119 |
| `SettingsActivity.kt` | 66 |
| `activity_result.xml` | 136 |
| `activity_history_detail.xml` | 114 |
| `activity_settings.xml` | 29 |
| `strings.xml` | 97 |
| **Total** | **1,088** |

Full files appear in [learning-notes.md section 12](learning-notes.md#12-end-of-week-10-file-inventory-exact-files-exact-code-exact-size).

## Exact Contracts

### Room Migration

Schema 1's 10 columns remain. Schema 2 adds:

```text
latitude REAL NULL
longitude REAL NULL
```

Existing rows migrate with null location. No destructive migration is allowed.

### Permissions

| Permission | Requested when | Denial behavior |
|---|---|---|
| Coarse/fine location | User checks Include Location and saves | Save without location |
| POST_NOTIFICATIONS (Android 13+) | User enables reminders | Toggle off; reminders cancelled |

### Sharing

Share contains disease, confidence, three guidance fields, and caution. It excludes model files, image, database, location, and secrets.

### Reminder

Unique work name prevents duplicate schedules. Interval is approximately 24 hours; WorkManager timing is not exact.

## CSE 2206 Connection

- implicit Intents and chooser
- dangerous runtime permissions
- privacy-minimizing optional data
- Room schema versions/migrations
- SharedPreferences
- notification channels and PendingIntent
- WorkManager and unique periodic work
- backward-compatible Android API behavior

## Milestone Demo

1. Share result through chooser and inspect text.
2. Save without location.
3. Save with location permission granted and show coordinates.
4. Deny location and show save still succeeds without coordinates.
5. Upgrade an existing v1 database and show history remains.
6. Enable reminder and grant notification permission.
7. Show unique work/channel configuration.
8. Disable reminder and prove cancellation.
9. Explain privacy boundaries.

## Seven-File Loop

| Step | File | Output |
|---:|---|---|
| 1 | `README.md` | Scope/contracts |
| 2 | `learning-notes.md` | Theory + exact files |
| 3 | `exercises.md` | Six evidence plans |
| 4 | `build-task.md` | Working utilities |
| 5 | `validation-checklist.md` | Pass/fail proof |
| 6 | `quiz.md` | >=14/18 |
| 7 | `reflection.md` | Week 11 handoff |

## Completion Contract

| Quantity | Value |
|---|---:|
| New files | 2 |
| Expanded files | 11 |
| Logical lines | 1,088 |
| Room version | 2 |
| New nullable columns | 2 |
| New permissions | 3 |
| Unique periodic work | 1 |
| Prediction contract changes | 0 |

<!-- NAV_FOOTER_START -->

---

## Navigation

[Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Week 09](../week-09-tensorflow-lite-offline-ai/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 11](../week-11-testing-debugging-performance/README.md)