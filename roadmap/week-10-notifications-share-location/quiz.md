# Week 10 Quiz: Sharing, Location, and Reminders

Passing score: **14/18**.

## Multiple Choice

### 1. Sharing uses? A explicit Activity B ACTION_SEND C Room query D Worker
### 2. Location requested when? A startup B every scan C opt-in save D never
### 3. Missing location stored as? A 0.0 B null C empty string D NaN
### 4. Database version after migration? A 1 B 2 C 10 D 38
### 5. Why unique periodic work? A exact time B avoid duplicates C location D sharing
### 6. Android 13 notification requirement? A CAMERA B POST_NOTIFICATIONS C INTERNET D FINE only
### 7. WorkManager timing? A exact B approximate/deferrable C immediate always D UI thread
### 8. Denied location should? A block save B save without location C crash D fabricate coordinates

## True/False

### 9. Share text includes coordinates. ____
### 10. Existing v1 rows should survive migration. ____
### 11. `0.0,0.0` safely means no location. ____
### 12. Notification permission should be rechecked when Worker posts. ____
### 13. Disabling reminders cancels unique work. ____

## Short Answer

### 14. Trace share chooser and privacy exclusions.
### 15. Trace location grant/deny/unavailable save paths.
### 16. Explain migration SQL and existing-row outcome.
### 17. Trace settings toggle to Worker notification and cancellation.
### 18. Name three Week 11/12 or excluded utility features.

## Key

1 B, 2 C, 3 B, 4 B, 5 B, 6 B, 7 B, 8 B, 9 False, 10 True, 11 False, 12 True, 13 True.

Short answers require chooser/text/no image-location; opt-in permission and null fallback; two nullable ALTERs/version2/migration; preference/channel/permission/unique work/Worker/PendingIntent/cancel; and exclusions such as tests/performance/signing/background location/maps/analytics/exact alarms.

<!-- NAV_FOOTER_START -->

---

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | **Quiz** | [Reflection](reflection.md)