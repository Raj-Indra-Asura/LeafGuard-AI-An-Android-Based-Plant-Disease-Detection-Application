# Week 10: Validation Checklist - Notifications, Share Intent, and Location Services

## Overview

This checklist provides **pass/fail validation criteria** for Week 10. Each item must pass before you move to Week 11. There are no partial points - features either work or they do not.

**How to use this checklist:**
1. Complete the Week 10 build task first
2. Test each validation point systematically
3. Mark ✅ only when the criterion is fully met
4. If any item fails, fix it before proceeding
5. Save screenshots as evidence for items marked with 📸

---

## Section 1: Manifest, Dependencies, and Setup

### 1.1 Permissions and Provider Setup

- [ ] **ACCESS_COARSE_LOCATION permission** added to `AndroidManifest.xml`
- [ ] **ACCESS_FINE_LOCATION permission** added to `AndroidManifest.xml`
- [ ] **POST_NOTIFICATIONS permission** added if targeting Android 13+
- [ ] **FileProvider declared** in `AndroidManifest.xml`
- [ ] **Provider authority uses `${applicationId}.provider`**
- [ ] **`file_paths.xml` created** in `res/xml/`
- [ ] **Gradle sync completed successfully** after adding required dependencies
- [ ] **Project builds successfully** without compilation errors

**Pass Criteria:** All 8 items checked. App builds and all setup elements are present.

---

## Section 2: Notification Channels and Basic Notification

### 2.1 Notification Channel Creation

- [ ] **NotificationHelper.java created** or equivalent helper class exists
- [ ] **Reminder channel ID defined as a constant**
- [ ] **Reminder channel created only on API 26+**
- [ ] **Reminder channel name is user-friendly** (e.g., “Scan Reminders”)
- [ ] **Reminder channel description added**
- [ ] **Channel importance set appropriately** (`IMPORTANCE_DEFAULT` for normal reminders)
- [ ] **Optional alert/status channels created** if your app uses them

### 2.2 Basic Reminder Notification

- [ ] **Notification builder uses correct channel ID**
- [ ] **Small icon set correctly** with valid drawable resource
- [ ] **Notification title displayed correctly**
- [ ] **Notification text displayed correctly**
- [ ] **Priority set for backward compatibility**
- [ ] **Category set appropriately** (`CATEGORY_REMINDER` or suitable category)
- [ ] **Notification posts successfully** when triggered manually

**Pass Criteria:** All 14 items checked. Notifications work correctly on supported devices.

---

## Section 3: PendingIntent and Tap Action

### 3.1 Notification Interaction

- [ ] **PendingIntent created with `getActivity()`** for app-opening notifications
- [ ] **`FLAG_UPDATE_CURRENT` used** where appropriate
- [ ] **`FLAG_IMMUTABLE` included** for secure tap action handling
- [ ] **Notification uses `setContentIntent()`**
- [ ] **`setAutoCancel(true)` added** so notification dismisses when tapped
- [ ] **Notification tap opens app** without crash
- [ ] **Intent extras from notification are received correctly** (if used)

**Test:** Post notification, tap it, and verify app opens to the correct screen/state.

**Pass Criteria:** All 7 items checked. Tap behavior works correctly.

---

## Section 4: Scheduled Notifications

### 4.1 WorkManager Reminder Scheduling

- [ ] **WorkManager dependency added**
- [ ] **ReminderWorker.java created** or equivalent Worker exists
- [ ] **Worker calls NotificationHelper** to post reminder
- [ ] **PeriodicWorkRequest used** for repeating reminders
- [ ] **Unique work scheduling used** (`enqueueUniquePeriodicWork()`)
- [ ] **No duplicate daily reminders created** when scheduling multiple times
- [ ] **Scheduled reminder architecture can be explained** even if timing tests are approximate

### 4.2 AlarmManager Awareness

- [ ] **You can explain why WorkManager is preferred over AlarmManager** for this use case
- [ ] **You understand AlarmManager as an alternative** for exact timing scenarios

**Pass Criteria:** All 9 items checked. Reminder scheduling is implemented or clearly understood and demonstrated.

---

## Section 5: Share Intent - Text Sharing

### 5.1 Share Text Implementation

- [ ] **Share button added** to result UI
- [ ] **`Intent.ACTION_SEND` used** for text sharing
- [ ] **MIME type set to `text/plain`**
- [ ] **Shared text includes disease name**
- [ ] **Shared text includes formatted confidence percentage**
- [ ] **Optional treatment/location text included** if available
- [ ] **Chooser used** with `Intent.createChooser()`
- [ ] **No crash when chooser opens**
- [ ] **At least one compatible app appears** in chooser during testing

📸 **Screenshot Required:** Chooser dialog for text share.

**Pass Criteria:** All 9 items checked. Text sharing works correctly.

---

## Section 6: Share Intent - Image Sharing with FileProvider

### 6.1 FileProvider Configuration

- [ ] **Provider entry exists** in manifest
- [ ] **Authority string matches code exactly**
- [ ] **`file_paths.xml` defines the actual share directory**
- [ ] **Image file is saved inside allowed path** (`cache/` or `files/` path declared in XML)

### 6.2 Image Share Flow

- [ ] **Bitmap or result image saved to file successfully**
- [ ] **`FileProvider.getUriForFile()` used**
- [ ] **`Intent.EXTRA_STREAM` used** with `content://` URI
- [ ] **MIME type set correctly** (`image/jpeg` or correct image type)
- [ ] **`FLAG_GRANT_READ_URI_PERMISSION` added**
- [ ] **Chooser opens successfully**
- [ ] **Receiving app can access the image**
- [ ] **No `FileUriExposedException` occurs**
- [ ] **No raw `Uri.fromFile()` sharing used**

📸 **Screenshot Required:** Chooser dialog or receiving app preview with image attached.

**Pass Criteria:** All 13 items checked. Secure image sharing works end-to-end.

---

## Section 7: Location Permission Flow

### 7.1 Permission Handling

- [ ] **Location request code constant defined**
- [ ] **Permission checked before calling location API**
- [ ] **Runtime permission requested** with `ActivityCompat.requestPermissions()`
- [ ] **`onRequestPermissionsResult()` implemented**
- [ ] **Grant path handled correctly**
- [ ] **Deny path handled gracefully**
- [ ] **App does not crash if permission is denied**

**Test:** Revoke permission in settings, reopen app, and trigger location flow again.

**Pass Criteria:** All 7 items checked. Runtime permission flow is correct.

---

## Section 8: Location Retrieval

### 8.1 Fused Location Provider Usage

- [ ] **Google Play Services location dependency added**
- [ ] **FusedLocationProviderClient initialized correctly**
- [ ] **`getLastLocation()` used correctly** after permission check
- [ ] **Null location handled safely**
- [ ] **Success listener logs or stores latitude**
- [ ] **Success listener logs or stores longitude**
- [ ] **Failure listener handled** if location request fails

### 8.2 Understanding Single vs Repeated Updates

- [ ] **You can explain `getLastLocation()`**
- [ ] **You can explain `requestLocationUpdates()`**
- [ ] **You understand when a fresh update may be needed**

📸 **Screenshot Required:** Logcat showing latitude/longitude or a clear null-location fallback message.

**Pass Criteria:** All 10 items checked. Location retrieval logic is implemented safely and understood.

---

## Section 9: Room Database Integration

### 9.1 Entity and Database Changes

- [ ] **`ScanEntity` updated** with nullable `latitude` field
- [ ] **`ScanEntity` updated** with nullable `longitude` field
- [ ] **Fields use `Double` not primitive `double`** for optional values
- [ ] **Room database version updated** if schema changed
- [ ] **Migration added** if upgrading an existing DB version
- [ ] **DAO still works** after schema update

### 9.2 Saving and Displaying Location

- [ ] **Scan save method accepts optional coordinates**
- [ ] **Scan saves correctly when coordinates exist**
- [ ] **Scan still saves when coordinates are null**
- [ ] **UI displays coordinates correctly** if present
- [ ] **UI shows fallback text** (e.g., “Location unavailable”) if absent

**Pass Criteria:** All 11 items checked. Room integration works with optional location data.

---

## Section 10: End-to-End Integration Test

### 10.1 Full Feature Flow

Perform this test sequence and check each step:

1. [ ] **Launch app** - opens without crash
2. [ ] **Notification channels created** during startup or first use
3. [ ] **Trigger manual reminder notification** - appears correctly
4. [ ] **Tap notification** - app opens correctly
5. [ ] **Run a plant scan** - result screen opens
6. [ ] **Tap Share Result** - chooser opens with text-capable apps
7. [ ] **Tap Share Image** - chooser opens with image-capable apps
8. [ ] **Grant location permission** when prompted
9. [ ] **Location fetched or gracefully unavailable**
10. [ ] **Save scan record** - no crash
11. [ ] **Open history/result detail** - coordinates shown if available
12. [ ] **Repeat test with permission denied** - scan still saves without crash

📸 **Screenshot Required:** Evidence of notification, sharing, and stored location behavior.

**Pass Criteria:** All 12 steps complete successfully without crashes.

---

## Section 11: Error Scenario Testing

### 11.1 Notification Errors

- [ ] **Wrong or disabled channel behavior understood/tested**
- [ ] **App does not crash if notification permission is missing on API 33+**
- [ ] **Notification helper safely returns if permission not granted**

### 11.2 Share Errors

- [ ] **App handles no available share app gracefully** (if testable)
- [ ] **App handles image file creation failure gracefully**
- [ ] **App handles FileProvider setup mistakes during debugging without silent failure**

### 11.3 Location Errors

- [ ] **Permission denied path tested**
- [ ] **GPS/location disabled path tested**
- [ ] **Null result from `getLastLocation()` handled safely**
- [ ] **Scan can still be saved without location**

**Pass Criteria:** All 10 error scenario items checked. Failure cases are handled without crashes.

---

## Section 12: Code Quality

### 12.1 Code Organization

- [ ] **Notification logic separated** from UI where practical
- [ ] **Location logic separated** from UI where practical
- [ ] **No hardcoded authority mismatch** between code and manifest
- [ ] **Meaningful constant names used** for channel IDs and request codes
- [ ] **Null checks present** for location-dependent logic
- [ ] **No duplicate reminder scheduling logic**

### 12.2 Secure and Modern Practices

- [ ] **`FLAG_IMMUTABLE` used** where appropriate
- [ ] **No raw file URI sharing** used
- [ ] **Chooser used for implicit sharing**
- [ ] **User-friendly messages shown** for denied/unavailable states

**Pass Criteria:** All 10 items checked. Code follows safe Android practices.

---

## Section 13: Documentation and Evidence

### 13.1 Evidence Collection

- [ ] **Screenshot: notification channel in system settings**
- [ ] **Screenshot: reminder notification visible**
- [ ] **Screenshot: share text chooser**
- [ ] **Screenshot: share image chooser or preview**
- [ ] **Screenshot: Logcat or UI showing location result**
- [ ] **Screenshot: saved scan/history with coordinates or fallback text**
- [ ] **All screenshots saved** in `docs/evidence/week-10/` or your chosen evidence folder

### 13.2 Documentation

- [ ] **Week 10 reflection completed**
- [ ] **Week 10 quiz attempted**
- [ ] **Progress tracker updated** with Week 10 completion
- [ ] **Git commits made** with meaningful messages
- [ ] **Can explain FileProvider and runtime permission flow verbally**

📸 **Evidence Folder:** Must contain at least 6 screenshots.

**Pass Criteria:** All 12 items checked. Evidence is complete and documentation is updated.

---

## Final Validation Summary

### Minimum Passing Requirements

To pass Week 10 validation, you MUST achieve:

1. ✅ **All Section 10 items pass** (end-to-end integration test)
2. ✅ **All Section 11 items pass** (error handling tests)
3. ✅ **At least 90% of all other items pass** (allow minor issues in non-critical areas)
4. ✅ **Zero crashes during normal operation**
5. ✅ **Zero crashes during error scenarios**
6. ✅ **Evidence collected** (minimum 6 screenshots)

### Items Completed

**Total Items:** ~130 validation points

**Your Score:** _____ / 130

**Percentage:** _____ %

**Status:** [ ] PASS (≥90%) | [ ] FAIL (<90%)

---

## If You Fail Validation

If you have not achieved 90% completion:

1. **Review failed items** - note specific validation points not met
2. **Debug systematically** - use Logcat and step-by-step testing
3. **Re-read `build-task.md`** - ensure you followed all steps
4. **Re-read `learning-notes.md`** - especially FileProvider and permission flow sections
5. **Test again** after fixes
6. **Do NOT move to Week 11** until Week 10 validation passes

---

## Teacher Demonstration Checklist

When demonstrating to your teacher, be ready to show:

- [ ] **Notification channel listed** in Android settings
- [ ] **Reminder notification displayed** and tappable
- [ ] **PendingIntent tap action working**
- [ ] **Text share chooser working**
- [ ] **Image share with FileProvider working**
- [ ] **Location permission request flow working**
- [ ] **Stored coordinates in Room** or graceful null fallback
- [ ] **Can explain why WorkManager is preferred** for routine reminders
- [ ] **Can explain why FileProvider is required** for images
- [ ] **Can explain difference between `getLastLocation()` and `requestLocationUpdates()`**

---

## Week 10 Validation Complete!

Once you pass all validation criteria:

- [ ] **Mark Week 10 as complete** in your progress tracker
- [ ] **Commit final version** to Git
- [ ] **Celebrate!** Your app now interacts with Android system services like a real production app 🎉
- [ ] **Ready for Week 11:** testing, optimization, and reliability improvements

**Date Completed:** ___________

**Final Notes/Challenges Faced:**
_______________________________________________________________

---

**Next:** Open `roadmap/week-11-testing-optimization/README.md` when Week 10 validation is fully passed.

