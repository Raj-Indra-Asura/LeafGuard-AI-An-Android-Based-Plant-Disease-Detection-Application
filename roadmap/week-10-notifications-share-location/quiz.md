# Week 10: Quiz - Notifications, Share Intent, and Location Services

## Instructions

- **Total Questions:** 30 (15 conceptual + 15 practical)
- **Time Limit:** 45 minutes (recommended)
- **Passing Score:** 24/30 (80%)
- **Open Book:** You may reference your code and notes
- **Purpose:** Test understanding before moving to Week 11

**Answer Format:**
- Multiple choice: Write the letter (A, B, C, or D)
- True/False: Write T or F
- Short answer: Write 1-3 sentences

---

## Part A: Conceptual Questions (15 questions)

### Question 1
**What is the primary purpose of a NotificationChannel on Android 8.0+?**

A) To store notification text in a database
B) To group notifications so users can control their behavior and importance
C) To make notifications appear faster
D) To replace PendingIntent

Your answer: _____

---

### Question 2
**True or False: On API 26+, a notification can be posted without assigning it to a channel.**

Your answer: _____

Explanation (if False): _______________________________________________

---

### Question 3
**Which importance level is usually appropriate for a normal daily “scan your plants” reminder?**

A) `IMPORTANCE_NONE`
B) `IMPORTANCE_LOW`
C) `IMPORTANCE_DEFAULT`
D) `IMPORTANCE_MAX`

Your answer: _____

---

### Question 4
**What is the role of `PendingIntent` in a notification?**

A) It saves notification history in Room
B) It tells Android what action to perform later when the user taps the notification
C) It creates the notification channel
D) It requests location permission

Your answer: _____

---

### Question 5
**Why is `FLAG_IMMUTABLE` recommended for many notification tap PendingIntents?**

A) It makes notifications louder
B) It allows other apps to edit the intent
C) It improves security by preventing later modification
D) It is required for Room database access

Your answer: _____

---

### Question 6
**True or False: WorkManager is generally preferred over AlarmManager for routine daily reminder notifications.**

Your answer: _____

Explanation: _______________________________________________

---

### Question 7
**What does `Intent.ACTION_SEND` represent in Android?**

A) A background thread
B) An implicit intent action for sharing content with other apps
C) A direct connection to WhatsApp only
D) A Room database insert action

Your answer: _____

---

### Question 8
**Which MIME type should be used to share a text-only LeafGuard AI result?**

A) `application/json`
B) `image/jpeg`
C) `text/plain`
D) `multipart/form-data`

Your answer: _____

---

### Question 9
**Why is FileProvider required for image sharing on Android 7.0+?**

A) Because raw file URIs are blocked for security reasons
B) Because JPEG files cannot be shared directly
C) Because ACTION_SEND only works with text
D) Because Room requires content URIs

Your answer: _____

---

### Question 10
**True or False: `Uri.fromFile(imageFile)` is the recommended way to share an image with other apps on modern Android.**

Your answer: _____

Explanation: _______________________________________________

---

### Question 11
**What is the main advantage of `FusedLocationProviderClient`?**

A) It stores coordinates in SQLite automatically
B) It combines multiple location sources such as GPS, Wi-Fi, and cell towers
C) It removes the need for runtime permissions
D) It only works on emulators

Your answer: _____

---

### Question 12
**Which permission provides more precise coordinates?**

A) `ACCESS_COARSE_LOCATION`
B) `ACCESS_FINE_LOCATION`
C) `ACCESS_BACKGROUND_LOCATION`
D) `POST_NOTIFICATIONS`

Your answer: _____

---

### Question 13
**True or False: Declaring `ACCESS_FINE_LOCATION` in the manifest is enough; runtime permission is not needed.**

Your answer: _____

Explanation: _______________________________________________

---

### Question 14
**What is the main limitation of `getLastLocation()`?**

A) It always consumes too much battery
B) It may return `null` or stale data
C) It requires background location permission
D) It only returns altitude, not latitude/longitude

Your answer: _____

---

### Question 15
**Why should `latitude` and `longitude` often be stored as `Double` instead of primitive `double` in Room entities?**

A) Because Room does not support primitive doubles
B) Because `Double` allows null when location is unavailable
C) Because `Double` is always faster
D) Because PendingIntent requires boxed types

Your answer: _____

---

## Part B: Practical / Code-Based Questions (15 questions)

### Question 16
**Identify the problem in this code:**

```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "scan_channel")
        .setContentTitle("LeafGuard Reminder")
        .setContentText("Time to scan your plants.");
```

A) Missing `setSmallIcon()`
B) Missing `setContentIntent()`
C) Wrong title
D) No error

Your answer: _____

---

### Question 17
**Complete the missing line for API 26+ channel creation:**

```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    NotificationChannel channel = new NotificationChannel(
            CHANNEL_ID,
            "Scan Reminders",
            ______________________________
    );
}
```

Your answer: _______________________________________________

---

### Question 18
**What is wrong with this PendingIntent code for modern Android?**

```java
PendingIntent pendingIntent = PendingIntent.getActivity(
        this,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
);
```

A) It should use `getBroadcast()` only
B) It should also include immutable/mutable flag as appropriate
C) It cannot use request code 0
D) No error

Your answer: _____

---

### Question 19
**Which line correctly launches a chooser for sharing text?**

A) `startActivity(shareIntent);`
B) `startActivity(Intent.createChooser(shareIntent, "Share result"));`
C) `shareIntent.send();`
D) `Intent.openChooser(shareIntent);`

Your answer: _____

---

### Question 20
**Complete the missing MIME type for sharing a JPEG image:**

```java
Intent shareIntent = new Intent(Intent.ACTION_SEND);
shareIntent.setType("____________________");
```

Your answer: _______________________________________________

---

### Question 21
**Fix this FileProvider code conceptually:**

```java
Uri uri = Uri.fromFile(imageFile);
shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
```

What should be used instead of `Uri.fromFile(imageFile)`?

Your answer: _______________________________________________

---

### Question 22
**What permission check is missing before this location code runs?**

```java
fusedLocationProviderClient.getLastLocation()
        .addOnSuccessListener(location -> {
            // use location
        });
```

A) CAMERA permission
B) INTERNET permission
C) `ACCESS_FINE_LOCATION` / location permission check
D) POST_NOTIFICATIONS permission

Your answer: _____

---

### Question 23
**True or False: This is a correct permission request line for fine location.**

```java
ActivityCompat.requestPermissions(
        this,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        REQUEST_LOCATION_PERMISSION
);
```

Your answer: _____

---

### Question 24
**Which callback is used to handle the runtime permission result in an Activity?**

A) `onPermissionReady()`
B) `onRequestPermissionsResult()`
C) `onLocationPermissionChanged()`
D) `onGrantResult()`

Your answer: _____

---

### Question 25
**Complete the missing code to read coordinates from a non-null Location object:**

```java
double latitude = location.____________________;
double longitude = location.____________________;
```

Your answer: _______________________________________________

---

### Question 26
**What is the main issue with this Room entity design?**

```java
private double latitude;
private double longitude;
```

A) Room cannot save coordinates
B) It prevents null when location is unavailable
C) It uses too much memory
D) It requires a migration every time

Your answer: _____

---

### Question 27
**Which WorkManager call avoids scheduling duplicate periodic reminder jobs?**

A) `enqueue()`
B) `enqueueRepeating()`
C) `enqueueUniquePeriodicWork()`
D) `startWorker()`

Your answer: _____

---

### Question 28
**What is missing in this image share intent?**

```java
Intent shareIntent = new Intent(Intent.ACTION_SEND);
shareIntent.setType("image/jpeg");
shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
```

A) `Intent.FLAG_GRANT_READ_URI_PERMISSION`
B) INTERNET permission
C) Notification channel ID
D) A database insert

Your answer: _____

---

### Question 29
**Identify one problem with this location logic:**

```java
fusedLocationProviderClient.getLastLocation();
saveScanToDatabase();
```

Your answer: _______________________________________________

---

### Question 30
**What should the app do if location permission is denied while saving a scan result?**

A) Crash so the bug is visible
B) Refuse to save the scan forever
C) Save the scan without location and inform the user gracefully
D) Automatically enable GPS

Your answer: _____

---

## Answers and Scoring

### Part A: Conceptual Answers

1. B
2. F - On API 26+ notifications must belong to a valid channel or they may not appear
3. C
4. B
5. C
6. T - WorkManager is usually preferred for routine scheduled work because it is battery-aware and reliable
7. B
8. C
9. A
10. F - Modern Android requires a secure content URI through FileProvider instead of raw file URIs
11. B
12. B
13. F - Location is a dangerous permission and must also be requested at runtime
14. B
15. B

### Part B: Practical Answers

16. A - A notification needs a valid small icon
17. `NotificationManager.IMPORTANCE_DEFAULT`
18. B - Modern Android expects an explicit immutable/mutable decision, and simple tap actions usually use `FLAG_IMMUTABLE`
19. B
20. `image/jpeg`
21. `FileProvider.getUriForFile(...)`
22. C
23. T
24. B
25. `getLatitude()` and `getLongitude()`
26. B - Primitive doubles cannot represent missing location with null
27. C
28. A
29. The location request is asynchronous, so the app may save before the location result arrives
30. C

---

## Scoring Guide

**Your Score:** _____ / 30

**Percentage:** _____ %

### Score Interpretation

- **27-30 (90-100%):** Excellent! Strong understanding of Week 10 Android platform features.
- **24-26 (80-89%):** Good! Ready for Week 11 with minor review needed.
- **21-23 (70-79%):** Passing, but review weak areas before Week 11.
- **Below 21 (<70%):** Re-read learning notes and retry quiz.

---

## Areas for Review

Based on your incorrect answers, review these topics:

If you missed questions 1-6: Review notification architecture, channels, and scheduling
If you missed questions 7-10: Review share intents, MIME types, and FileProvider
If you missed questions 11-15: Review location services and Room integration
If you missed questions 16-23: Review practical setup and permission flow
If you missed questions 24-30: Review asynchronous thinking, WorkManager, and graceful fallbacks

---

## Retake Policy

- You may retake this quiz after reviewing your mistakes
- Goal is understanding, not perfect score on first attempt
- **Must score 80%+ before starting Week 11**

---

## Certification

- [ ] I scored 80% or higher
- [ ] I reviewed all incorrect answers
- [ ] I understand the concepts behind my mistakes
- [ ] I am ready for Week 11

**Date Completed:** ___________

**Signature:** ___________

---



<!-- NAV_FOOTER_START -->

---

## 📚 Week 10 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| **6** | **quiz.md** ← *You are here* | **Knowledge Assessment Quiz** |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Validation & Verification](validation-checklist.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Knowledge Assessment Quiz** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Reflection & Consolidation →](reflection.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 09: TensorFlow Lite Offline AI](../week-09-tensorflow-lite-offline-ai/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 11: Testing, Debugging & Performance ➡](../week-11-testing-debugging-performance/README.md) |

---
