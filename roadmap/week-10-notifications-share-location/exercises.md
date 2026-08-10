# Week 10 Exercises: Sharing, Location, and Reminders

Save six files under `docs/evidence/week-10/exercises/`.

| # | Output | Focus |
|---:|---|---|
| 1 | `exercise-01-share-privacy.md` | Intent payload/privacy |
| 2 | `exercise-02-location-states.md` | Grant/deny/unavailable |
| 3 | `exercise-03-room-migration.md` | Nullable columns/migration |
| 4 | `exercise-04-notification-work.md` | Channel/permission/unique work |
| 5 | `exercise-05-settings-state.md` | Preference and toggles |
| 6 | `exercise-06-demo-evidence.md` | Full milestone |

## 1. Share Privacy

List included and excluded data. Draw ACTION_SEND -> chooser -> target app. Explain why no share permission is needed and why model suggestion wording matters.

## 2. Location States

Create a table for unchecked, granted with location, granted without location, denied, and revoked. Every state must still permit save. Explain coarse/fine and why `0.0` is not missing.

## 3. Room Migration

Draw schema v1 and v2. Write both ALTER statements. Predict existing/new row values. Explain why destructive migration is unacceptable.

## 4. Notification Work

Trace switch -> permission -> channel -> unique periodic work -> Worker -> notification -> PendingIntent. Explain approximate timing and duplicate prevention.

## 5. Settings State

Create state transitions for enable/grant, enable/deny, reopen, re-enable, and disable. Record preference and unique-work expectations.

## 6. Demo Evidence

Plan share text, no-location save, granted location, denied location, migration preservation, reminder enable/re-enable/disable, and permission-revoked worker behavior. State what each proves and does not prove.

## Completion

Explain implicit Intent, privacy exclusions, permission timing, nullable schema, migration, channels, WorkManager uniqueness, and preferences before building.

<!-- NAV_FOOTER_START -->

---

[README](README.md) | [Learning Notes](learning-notes.md) | **Exercises** | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)