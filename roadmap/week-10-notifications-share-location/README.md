# Week 10: Notifications, Share, and Location

## What you'll learn & why

This week you connect LeafGuard AI to three built-in Android features that make an app feel finished. You will show a **notification** (the little message that slides down from the top of the phone) to remind the user to scan their plants. You will add a **share** button so a result can be sent to WhatsApp, Gmail, or any other app. You will optionally tag a scan with the phone's **location** (its GPS latitude and longitude) so the user knows where a diseased leaf was found. These are all things real apps do every day, and each one is a required topic in your CSE 2206 course.

## New words this week

See the shared [glossary](../../GLOSSARY.md) for more. The key terms this week:

- **Notification** — a short message your app posts to the phone's status bar, even when the app is closed.
- **Notification channel** — a named category of notifications (Android 8+) that the user can turn on or off. LeafGuard's channel id is `leafguard_scan_reminders`.
- **PendingIntent** — a "saved action" you hand to the system so it can open your app later when the user taps the notification.
- **Share intent** — an `Intent` with action `ACTION_SEND` that asks Android to show the list of apps you can share to.
- **Location / GPS** — the phone's position on Earth, given as two numbers: latitude and longitude.

---

## Weekly Objective

Implement Android system integrations: notifications, sharing, and location tagging.

**Measurable Outcomes:**
- Notification channel created
- Reminder notification working
- Share intent implemented
- Location permission handling
- Optional: GPS coordinates saved with scans
- Complete system integration demonstration

---

## Why This Week Matters

**CSE 2206 Requirements:**
- **Notifications** - Mandatory syllabus topic
- **App-to-app communication** - Share intent demonstrates this
- **Location** - Maps and location topic (attempt required)

**Viva Question:** "Explain what a NotificationChannel is and why Android 8 introduced it."

---

## Syllabus Topics

1. **Notifications** - NotificationChannel, NotificationCompat, PendingIntent
2. **App-to-app Communication** - Share intent, ACTION_SEND
3. **Location Services** - FusedLocationProviderClient, GPS permissions
4. **System Integration** - Using Android framework features via intents and system services

---

## Key Concepts

### Notification Architecture (API 26+)

Android 8 (API 26) introduced **NotificationChannels**. Every notification must belong to a channel, which the user can manage independently in Settings.

```
App creates NotificationChannel (once, on startup)
       |
       v
NotificationManager registers the channel with the OS
       |
       v
App builds Notification using NotificationCompat.Builder
       |
       v
NotificationManager.notify(id, notification)
       |
       v
Android shows notification in the status bar
       |
       v
User taps → PendingIntent fires → MainActivity opens
```

**Key classes:**
| Class | Role |
|---|---|
| `NotificationChannel` | Defines importance level, sound, vibration |
| `NotificationCompat.Builder` | Builds the visible notification |
| `PendingIntent` | Wraps the Intent that runs when tapped |
| `NotificationManagerCompat` | Posts the notification (compat wrapper) |

**Minimal implementation (Kotlin — primary track):**

In LeafGuard's real code this lives in `NotificationHelper` (a Kotlin `object` in
package `com.leafguard.utils`). The channel id is `leafguard_scan_reminders` and the
notification id is `1001`.

```kotlin
// Create the channel once (safe to call again; Android ignores duplicates)
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val channel = NotificationChannel(
        "leafguard_scan_reminders",       // channel id
        "Scan Reminders",                 // user-visible name
        NotificationManager.IMPORTANCE_DEFAULT
    )
    channel.description = "Reminds you to scan your plants weekly"
    val nm = context.getSystemService(NotificationManager::class.java)
    nm?.createNotificationChannel(channel)
}

// Build and post the notification
val intent = Intent(context, MainActivity::class.java)
val pendingIntent = PendingIntent.getActivity(
    context, 1001, intent,
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
)

val builder = NotificationCompat.Builder(context, "leafguard_scan_reminders")
    .setSmallIcon(android.R.drawable.ic_menu_camera)
    .setContentTitle("Time to check your plants!")
    .setContentText("Open LeafGuard AI and scan a leaf today.")
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    .setContentIntent(pendingIntent)
    .setAutoCancel(true)

NotificationManagerCompat.from(context).notify(1001, builder.build())
```

**Java (secondary track):**
```java
// Create channel (do this once, e.g. in NotificationHelper)
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    NotificationChannel channel = new NotificationChannel(
        "leafguard_scan_reminders",   // channel id
        "Scan Reminders",             // user-visible name
        NotificationManager.IMPORTANCE_DEFAULT
    );
    channel.setDescription("Reminds you to scan your plants weekly");
    NotificationManager nm = context.getSystemService(NotificationManager.class);
    nm.createNotificationChannel(channel);
}

// Build and post a notification
PendingIntent pi = PendingIntent.getActivity(
    context, 1001,
    new Intent(context, MainActivity.class),
    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
);

NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "leafguard_scan_reminders")
    .setSmallIcon(android.R.drawable.ic_menu_camera)
    .setContentTitle("Time to check your plants!")
    .setContentText("Open LeafGuard AI and scan a leaf today.")
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    .setContentIntent(pi)
    .setAutoCancel(true);

NotificationManagerCompat.from(context).notify(1001, builder.build());
```

---

### Share Intent Architecture

Android's **Share sheet** lets your app send data to any other app that can handle it. No direct integration with the target app is needed.

```
LeafGuard ResultActivity
       |
       | user taps Share
       v
Intent(ACTION_SEND) with text/image MIME type
       |
       v
Android resolves all installed apps that accept this type
       |
       v
Share sheet appears: WhatsApp, Gmail, Messages, Drive...
       |
       v
User picks an app → that app's Activity receives the Intent
```

**Share text result (Kotlin — primary track).** In LeafGuard this lives in `ResultActivity` (and `HistoryDetailActivity`):
```kotlin
private fun shareResult(diseaseName: String, confidence: Float) {
    val shareText = "LeafGuard AI detected: $diseaseName" +
        " (${Math.round(confidence)}% confidence)\n" +
        "Scanned with LeafGuard AI"

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
        putExtra(Intent.EXTRA_SUBJECT, "Plant Disease Scan Result")
    }
    startActivity(Intent.createChooser(shareIntent, "Share result via"))
}
```

**Java (secondary track):**
```java
private void shareResult(String diseaseName, float confidence) {
    String shareText = "LeafGuard AI detected: " + diseaseName
        + " (" + Math.round(confidence * 100) + "% confidence)\n"
        + "Scanned with LeafGuard AI";

    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Plant Disease Scan Result");

    startActivity(Intent.createChooser(shareIntent, "Share result via"));
}
```

**Share image + text:**
```java
private void shareResultWithImage(Uri imageUri, String text) {
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("image/jpeg");
    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    startActivity(Intent.createChooser(shareIntent, "Share via"));
}
```

---

### Location Services Architecture

```
App requests ACCESS_FINE_LOCATION permission
       |
       v
FusedLocationProviderClient.getLastLocation()
       |
       +--> Location available → extract lat/lng → save to DB
       |
       +--> Location null → request fresh location update
              |
              v
         LocationRequest + LocationCallback
              |
              v
         GPS/network returns fix → save lat/lng
```

**Permission declaration (AndroidManifest.xml):**
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

**Get last known location:**
```java
FusedLocationProviderClient fusedLocationClient =
    LocationServices.getFusedLocationProviderClient(this);

if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {

    fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            Log.d(TAG, "Location: " + lat + ", " + lng);
        }
    });
}
```

**Gradle dependency:**
```gradle
implementation 'com.google.android.gms:play-services-location:21.0.1'
```

---

## Prerequisites

- Week 07 complete (Room database to store location with scan records)
- Understanding of Android permissions model
- Week 09 complete (result screen to add Share button to)

---

## Weekly Timeline

- **Day 1-2:** Notification implementation (4h)
- **Day 3-4:** Share functionality (3h)
- **Day 5-6:** Location tagging (4h - attempt, document if difficult)
- **Day 7:** Testing and documentation (2h)

---

## Validation Criteria

- [ ] Notification channel created
- [ ] Notification displays correctly
- [ ] Tapping notification opens app
- [ ] Share button functional
- [ ] Can share to WhatsApp, email, etc.
- [ ] Location permission requested
- [ ] Location saved with scan (or documented as attempted)
- [ ] All features work without crashes

---

**Next:** `learning-notes.md` for 2,335 lines of in-depth coverage.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 10 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| **1** | **README.md** ← *You are here* | **Week Overview & Objectives** |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

*(Start of week)* &nbsp;&nbsp;|&nbsp;&nbsp; **Week Overview & Objectives** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Theory & Learning Notes →](learning-notes.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 09: TensorFlow Lite Offline AI](../week-09-tensorflow-lite-offline-ai/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 11: Testing, Debugging & Performance ➡](../week-11-testing-debugging-performance/README.md) |

---
