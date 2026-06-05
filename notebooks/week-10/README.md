# Week 10 Interactive Notebook

## Smart Alerts and Sharing Features

> Work through this Markdown notebook like a lab manual: read, run, test, and explain each checkpoint in your own words.

### How to use this notebook

- Follow the cells in order.
- Use Java for Android code and Python only for backend/model tooling.
- Save screenshots and logs as evidence for CSE 2206.
- Keep the roadmap folder for this week open while you work.

### Weekly outcomes

- Create Android notification channels and disease alerts.
- Share scan results as text or image using intents.
- Attach location data to scan history entries.

### Repository references

- `roadmap/week-10-notifications-share-location/`
- `android-app/app/src/main/AndroidManifest.xml`
- `solutions/week-10/`

---

## Notebook Cell 1 — Create the notification channel

### Explanation

- Android 8.0+ requires notification channels for grouped user-configurable notifications.

### Code to Read / Run

```java
public class NotificationHelper {
    public static final String CHANNEL_ID = "leafguard_alerts";

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "LeafGuard Alerts",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Disease detection alerts and reminders");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
```

### 🔵 Try This

- Call `createChannel()` once in `MainActivity` or your Application class.

### Expected Output

- The system registers a LeafGuard Alerts notification channel.

### ✅ Checkpoint

- Why do notifications need a channel on Android 8 and later?

### ⚠️ Common Mistake

- If you skip channel creation, notifications may never appear on newer Android versions.

### 📌 Key Point

- Notifications need both manifest permissions and runtime-ready setup.

## Notebook Cell 2 — Send a disease detected notification

### Explanation

- A notification can remind the user that a scan completed or needs attention.

### Code to Read / Run

```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("LeafGuard AI Alert")
        .setContentText("Possible Tomato Early Blight detected at 91% confidence.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true);

NotificationManagerCompat.from(this).notify(1001, builder.build());
```

### 🔵 Try This

- Trigger the notification after a successful prediction.

### Expected Output

- The emulator or device shows a LeafGuard AI alert notification.

### ✅ Checkpoint

- What information should a disease notification include without overwhelming the user?

### ⚠️ Common Mistake

- Do not forget `POST_NOTIFICATIONS` permission on newer Android versions.

### 📌 Key Point

- Notifications should be helpful, short, and actionable.

## Notebook Cell 3 — Share results as text

### Explanation

- Share intents let Android hand content to other apps such as messaging apps, notes, or email.

### Code to Read / Run

```java
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "LeafGuard AI Scan Result");
    shareIntent.putExtra(Intent.EXTRA_TEXT,
            "Disease: Tomato Early Blight
Confidence: 91%
Treatment: Remove infected leaves and improve airflow.");
    startActivity(Intent.createChooser(shareIntent, "Share scan result"));
```

### 🔵 Try This

- Share one result to a notes app and confirm formatting is readable.

### Expected Output

- Android shows the share sheet and passes the result text to another app.

### ✅ Checkpoint

- Why is a chooser better than launching a fixed target app directly?

### ⚠️ Common Mistake

- Do not lock the user into one app when the platform already provides a share chooser.

### 📌 Key Point

- Sharing is a built-in Android strength; use platform patterns.

## Notebook Cell 4 — Share results as image

### Explanation

- Sometimes the leaf image itself is useful when the farmer or class examiner wants to inspect the evidence.

### Code to Read / Run

```java
Intent shareImageIntent = new Intent(Intent.ACTION_SEND);
shareImageIntent.setType("image/*");
shareImageIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
shareImageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
startActivity(Intent.createChooser(shareImageIntent, "Share leaf image"));
```

### 🔵 Try This

- Use the same FileProvider approach from the camera week so the image Uri is sharable.

### Expected Output

- The user can send the captured image to another app.

### ✅ Checkpoint

- Why is `FLAG_GRANT_READ_URI_PERMISSION` important here?

### ⚠️ Common Mistake

- Do not try to share a private file path directly without a content Uri.

### 📌 Key Point

- Android sharing must respect file-access security.

## Notebook Cell 5 — Add location with FusedLocationProvider

### Explanation

- Location can make scan history more informative by showing where a disease was observed.

### Code to Read / Run

```java
FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
    fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            locationText.setText(latitude + ", " + longitude);
        }
    });
}
```

### 🔵 Try This

- Test location on emulator by setting a custom location from Extended Controls.

### Expected Output

- A latitude and longitude pair becomes available for display or storage.

### ✅ Checkpoint

- Why should LeafGuard AI request location only when the user understands its purpose?

### ⚠️ Common Mistake

- Do not request location without a clear explanation or user value.

### 📌 Key Point

- Location is sensitive data and should be handled transparently.

## Notebook Cell 6 — Attach location to scan history

### Explanation

- Adding location to your Room entity links AI results to field context.

### Code to Read / Run

```java
@ColumnInfo(name = "latitude")
private Double latitude;

@ColumnInfo(name = "longitude")
private Double longitude;
```

### 🔵 Try This

- Add migration planning notes if your schema already existed before this week.

### Expected Output

- History entries can store optional location coordinates.

### ✅ Checkpoint

- Why are `Double` wrapper types better than primitive `double` for optional coordinates?

### ⚠️ Common Mistake

- Do not assume location is always available; GPS may be off or permissions denied.

### 📌 Key Point

- Optional data should be modeled as optional data.

## Notebook Cell 7 — Test notifications on emulator

### Explanation

- Testing should include permission granted, permission denied, and repeated notification scenarios.

### Step-by-Step

1. Run a prediction and trigger a notification.
2. Open notification shade on the emulator.
3. Deny notification permission once and verify graceful fallback.
4. Send multiple notifications and check that IDs behave as expected.

### 🔵 Try This

- Use different notification IDs to compare update versus create-new behavior.

### Expected Output

- Notifications appear when allowed and fail gracefully when blocked.

### ✅ Checkpoint

- Can you explain the full flow from prediction success to visible system notification?

### ⚠️ Common Mistake

- Do not assume notification permission behaves the same on every Android version.

### 📌 Key Point

- Feature polish includes system integration behavior.

## Mini Quiz

- What problem does this week solve inside LeafGuard AI?
- Which Java class or Android component did you touch first?
- Which file path in this repository is most relevant to this week?
- What would break if you skipped the validation step?
- How does this week connect to the three-tier architecture?

## Evidence Checklist

- [ ] Capture a screenshot of the completed screen or terminal output.
- [ ] Save one code snippet that proves the feature is wired correctly.
- [ ] Write two sentences in your progress log about what you learned.
- [ ] Record at least one bug and the exact fix you applied.
- [ ] Commit working changes before moving to the next week.

## Reflection Prompt

- Explain the feature from memory without reading the code.
- State one improvement you would add after submission.
- Identify one risk if this feature were left untested.

## Next Step

- Continue to **Week 11** when this week is stable and documented.
