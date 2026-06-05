# Week 10: Notifications, Share, and Location

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
User taps → PendingIntent fires → ScanActivity opens
```

**Key classes:**
| Class | Role |
|---|---|
| `NotificationChannel` | Defines importance level, sound, vibration |
| `NotificationCompat.Builder` | Builds the visible notification |
| `PendingIntent` | Wraps the Intent that runs when tapped |
| `NotificationManagerCompat` | Posts the notification (compat wrapper) |

**Minimal implementation:**
```java
// Create channel (do this once in onCreate or Application class)
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    NotificationChannel channel = new NotificationChannel(
        "leafguard_reminders",        // channel ID
        "Scan Reminders",             // user-visible name
        NotificationManager.IMPORTANCE_DEFAULT
    );
    channel.setDescription("Reminds you to scan your plants weekly");
    NotificationManager nm = getSystemService(NotificationManager.class);
    nm.createNotificationChannel(channel);
}

// Build and post a notification
PendingIntent pi = PendingIntent.getActivity(
    this, 0,
    new Intent(this, ScanActivity.class),
    PendingIntent.FLAG_IMMUTABLE
);

NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "leafguard_reminders")
    .setSmallIcon(R.drawable.ic_leaf)
    .setContentTitle("Time to check your plants!")
    .setContentText("Open LeafGuard AI and scan a leaf today.")
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    .setContentIntent(pi)
    .setAutoCancel(true);

NotificationManagerCompat.from(this).notify(1001, builder.build());
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

**Share text result:**
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
