# Week 10: Exercises - Notifications, Share Intent, and Location Services

## Overview

These 8 exercises help you practice the three major Android platform features for Week 10:

- Notifications
- Share Intent
- Location Services

Each exercise is designed to be small enough to finish in one sitting, but together they build the complete Week 10 skill set for LeafGuard AI.

**Total Time Investment:** 6-8 hours

**Prerequisites:**
- Week 09 scan history and Room database work completed
- Android Studio working properly
- Java-based Android project configured with AndroidX
- A phone or emulator with Google Play Services for location exercises

**Important:** Write and test each exercise before moving to the next. Week 10 features depend on correct permissions, manifest setup, and structured Android system interaction.

---

## Exercise 1: Create Notification Channel for LeafGuard (15 minutes)

**Objective:** Learn why Android 8.0+ requires channels and create a reusable reminder channel for LeafGuard AI.

### What you will practice

- `NotificationChannel`
- `NotificationManager`
- API 26+ checks
- Channel naming and importance

### Task

Create a notification channel named **Scan Reminders** with ID `leafguard_scan_reminders`.

### Step-by-Step

#### 1. Create constants in a helper class

```java
public class NotificationHelper {
    public static final String CHANNEL_SCAN_REMINDERS = "leafguard_scan_reminders";
    public static final String CHANNEL_NAME_SCAN_REMINDERS = "Scan Reminders";
}
```

#### 2. Add a channel creation method

```java
public static void createReminderChannel(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_SCAN_REMINDERS,
                CHANNEL_NAME_SCAN_REMINDERS,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("Reminds users to scan plants regularly.");

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }
}
```

#### 3. Call the method in `MainActivity.onCreate()`

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    NotificationHelper.createReminderChannel(this);
}
```

### Code Scaffold

```java
// TODO: Replace placeholder channel ID with a stable constant
private static final String CHANNEL_ID = "_____________________";

// TODO: Create channel only on API 26+
if (Build.VERSION.SDK_INT >= ____________) {
    NotificationChannel channel = new NotificationChannel(
            CHANNEL_ID,
            "_____________________",
            NotificationManager._____________________
    );
}
```

### Verification Criteria

- [ ] App builds without errors
- [ ] Channel creation code runs without crash
- [ ] On Android 8.0+ device, system notification settings show **Scan Reminders** channel
- [ ] Channel uses `IMPORTANCE_DEFAULT`

### What to observe

Open:

**Settings → Apps → LeafGuard AI → Notifications**

You should see the channel listed.

### Common mistakes

- Forgetting API level check
- Using inconsistent channel ID later in builder
- Not calling channel creation early enough

---

## Exercise 2: Send Basic Scan Reminder Notification (30 minutes)

**Objective:** Build and send a simple reminder notification using `NotificationCompat.Builder`.

### What you will practice

- `NotificationCompat.Builder`
- Notification icon, title, text
- NotificationManagerCompat
- Notification IDs

### Task

Send a notification that says:

- Title: `LeafGuard Reminder`
- Text: `Time to scan your plants today.`

### Step-by-Step

#### 1. Make sure a small notification icon exists

Use a drawable such as `ic_leaf_notification.xml` or a valid PNG icon in `drawable/`.

#### 2. Build the notification

```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationHelper.CHANNEL_SCAN_REMINDERS)
        .setSmallIcon(R.drawable.ic_leaf_notification)
        .setContentTitle("LeafGuard Reminder")
        .setContentText("Time to scan your plants today.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setCategory(NotificationCompat.CATEGORY_REMINDER)
        .setAutoCancel(true);
```

#### 3. Send it from a button click

```java
Button sendNotificationButton = findViewById(R.id.sendNotificationButton);
sendNotificationButton.setOnClickListener(v -> {
    NotificationManagerCompat.from(this).notify(4001, builder.build());
});
```

### Code Scaffold

```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ____________________)
        .setSmallIcon(____________________)
        .setContentTitle("____________________")
        .setContentText("____________________")
        .setPriority(NotificationCompat.____________________);

NotificationManagerCompat.from(this).notify(__________, builder.build());
```

### Verification Criteria

- [ ] Tapping the button posts a notification
- [ ] Notification title and text are correct
- [ ] Notification appears in notification shade
- [ ] App does not crash on Android 8.0+

### Extra challenge

Change the notification text based on the current day, for example:

> “Tuesday reminder: inspect your leaves for disease spots.”

---

## Exercise 3: Add Tap-to-Open PendingIntent to Notification (45 minutes)

**Objective:** Make the notification useful by opening the app when tapped.

### What you will practice

- `PendingIntent.getActivity()`
- `FLAG_UPDATE_CURRENT`
- `FLAG_IMMUTABLE`
- Notification tap actions

### Task

Modify Exercise 2 so that tapping the notification opens `MainActivity` and passes an extra boolean called `from_notification`.

### Step-by-Step

#### 1. Create the intent

```java
Intent openAppIntent = new Intent(this, MainActivity.class);
openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
openAppIntent.putExtra("from_notification", true);
```

#### 2. Create flags safely

```java
int flags = PendingIntent.FLAG_UPDATE_CURRENT;
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    flags |= PendingIntent.FLAG_IMMUTABLE;
}
```

#### 3. Create the PendingIntent

```java
PendingIntent pendingIntent = PendingIntent.getActivity(
        this,
        0,
        openAppIntent,
        flags
);
```

#### 4. Attach it to builder

```java
builder.setContentIntent(pendingIntent);
```

#### 5. Handle the extra in `MainActivity`

```java
boolean fromNotification = getIntent().getBooleanExtra("from_notification", false);
if (fromNotification) {
    Toast.makeText(this, "Opened from notification", Toast.LENGTH_SHORT).show();
}
```

### Code Scaffold

```java
Intent intent = new Intent(this, ____________________);
intent.putExtra("from_notification", ________);

int flags = PendingIntent.FLAG_UPDATE_CURRENT;
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.______) {
    flags |= PendingIntent.____________________;
}

PendingIntent pendingIntent = PendingIntent.getActivity(
        this,
        0,
        intent,
        flags
);
```

### Verification Criteria

- [ ] Notification still posts successfully
- [ ] Tapping it opens `MainActivity`
- [ ] Extra value is received correctly
- [ ] Notification disappears after tap due to `setAutoCancel(true)`

### Debug tip

Log the result in Logcat:

```java
Log.d("Week10", "from_notification = " + fromNotification);
```

---

## Exercise 4: Implement Share Text Intent for Scan Results (30 minutes)

**Objective:** Use Android's implicit intent system to share a textual summary of a scan.

### What you will practice

- `Intent.ACTION_SEND`
- MIME type `text/plain`
- `Intent.EXTRA_TEXT`
- Chooser usage

### Task

Add a button on the result screen that shares disease name and confidence.

### Step-by-Step

#### 1. Add a Share button to layout

```xml
<Button
    android:id="@+id/shareTextButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Share Result" />
```

#### 2. Build share text in Java

```java
String shareText = "LeafGuard AI Scan Result\n"
        + "Disease: " + diseaseName + "\n"
        + "Confidence: " + String.format(Locale.US, "%.1f%%", confidence * 100);
```

#### 3. Create the share intent

```java
Intent shareIntent = new Intent(Intent.ACTION_SEND);
shareIntent.setType("text/plain");
shareIntent.putExtra(Intent.EXTRA_SUBJECT, "LeafGuard AI Scan Result");
shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
```

#### 4. Launch chooser

```java
startActivity(Intent.createChooser(shareIntent, "Share scan result"));
```

### Code Scaffold

```java
Intent shareIntent = new Intent(____________________);
shareIntent.setType("____________________");
shareIntent.putExtra(Intent.EXTRA_TEXT, ____________________);
startActivity(Intent.____________________(shareIntent, "____________________"));
```

### Verification Criteria

- [ ] Chooser opens when button is tapped
- [ ] Gmail/WhatsApp/Messages or similar apps appear
- [ ] Shared text includes disease and confidence
- [ ] App does not crash if chooser is opened repeatedly

### Reflection question

Why is this an implicit intent instead of an explicit intent?

---

## Exercise 5: Share Scan Result as Image Using FileProvider (1 hour)

**Objective:** Safely share an image file using a `content://` URI instead of a raw file path.

### What you will practice

- FileProvider setup
- `file_paths.xml`
- `Intent.EXTRA_STREAM`
- `FLAG_GRANT_READ_URI_PERMISSION`

### Task

Share a captured or processed scan image through the Android chooser.

### Step-by-Step

#### 1. Add FileProvider to manifest

```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

#### 2. Create `res/xml/file_paths.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <cache-path
        name="shared_images"
        path="images/" />
</paths>
```

#### 3. Save bitmap to cache directory

```java
File imageDir = new File(getCacheDir(), "images");
if (!imageDir.exists()) {
    imageDir.mkdirs();
}

File imageFile = new File(imageDir, "shared_scan.jpg");
FileOutputStream outputStream = new FileOutputStream(imageFile);
bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
outputStream.flush();
outputStream.close();
```

#### 4. Convert file to content URI

```java
Uri contentUri = FileProvider.getUriForFile(
        this,
        getPackageName() + ".provider",
        imageFile
);
```

#### 5. Share it

```java
Intent shareIntent = new Intent(Intent.ACTION_SEND);
shareIntent.setType("image/jpeg");
shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
startActivity(Intent.createChooser(shareIntent, "Share scan image"));
```

### Code Scaffold

```java
Uri uri = FileProvider.getUriForFile(
        this,
        ____________________,
        ____________________
);

Intent shareIntent = new Intent(Intent.ACTION_SEND);
shareIntent.setType("____________________");
shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
shareIntent.addFlags(Intent.____________________);
```

### Verification Criteria

- [ ] No `FileUriExposedException`
- [ ] Chooser opens with image-sharing apps
- [ ] Shared image is visible in receiving app preview
- [ ] Authority string matches manifest declaration
- [ ] File is saved inside allowed path

### Debug checklist

If it fails, check:

- [ ] provider exists in manifest
- [ ] authority matches exactly
- [ ] `file_paths.xml` exists in `res/xml/`
- [ ] file is inside `cache/images/`
- [ ] read URI permission flag is set

---

## Exercise 6: Request Location Permissions Properly (30 minutes)

**Objective:** Implement complete runtime permission handling for location.

### What you will practice

- Manifest location permissions
- `ActivityCompat.checkSelfPermission()`
- `ActivityCompat.requestPermissions()`
- `onRequestPermissionsResult()`

### Task

Request `ACCESS_FINE_LOCATION` before trying to attach coordinates to a scan.

### Step-by-Step

#### 1. Add manifest permissions

```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

#### 2. Define request code

```java
private static final int REQUEST_LOCATION_PERMISSION = 600;
```

#### 3. Check permission before location call

```java
private void checkLocationPermission() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(this, "Location permission already granted", Toast.LENGTH_SHORT).show();
    } else {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERMISSION
        );
    }
}
```

#### 4. Handle the result

```java
@Override
public void onRequestPermissionsResult(int requestCode,
                                       @NonNull String[] permissions,
                                       @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == REQUEST_LOCATION_PERMISSION) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Location denied", Toast.LENGTH_SHORT).show();
        }
    }
}
```

### Code Scaffold

```java
if (ActivityCompat.checkSelfPermission(this, Manifest.permission.____________________)
        == PackageManager.PERMISSION_GRANTED) {
    // TODO: proceed
} else {
    ActivityCompat.requestPermissions(
            this,
            new String[]{Manifest.permission.____________________},
            ____________________
    );
}
```

### Verification Criteria

- [ ] Permission dialog appears on first request
- [ ] Grant path shows success feedback
- [ ] Deny path shows graceful feedback
- [ ] App does not crash in either case

### Extension

Explain to yourself why location is a dangerous permission.

---

## Exercise 7: Get Device Location and Log Latitude/Longitude (45 minutes)

**Objective:** Use `FusedLocationProviderClient` to get the device's current or last known location.

### What you will practice

- Google Play Services location API
- `getLastLocation()`
- Null handling
- Logging coordinates

### Task

After permission is granted, fetch device location and print latitude/longitude to Logcat.

### Step-by-Step

#### 1. Add location dependency

```gradle
implementation 'com.google.android.gms:play-services-location:21.3.0'
```

#### 2. Initialize client

```java
private FusedLocationProviderClient fusedLocationProviderClient;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
}
```

#### 3. Fetch last known location

```java
private void fetchLastLocation() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
        return;
    }

    fusedLocationProviderClient.getLastLocation()
            .addOnSuccessListener(location -> {
                if (location != null) {
                    Log.d("Week10Location", "Lat: " + location.getLatitude());
                    Log.d("Week10Location", "Lon: " + location.getLongitude());
                } else {
                    Log.d("Week10Location", "Location is null");
                }
            })
            .addOnFailureListener(e -> Log.e("Week10Location", "Failed to get location", e));
}
```

### Code Scaffold

```java
fusedLocationProviderClient.getLastLocation()
        .addOnSuccessListener(location -> {
            if (location != null) {
                Log.d("Week10Location", "Lat: " + ____________________);
                Log.d("Week10Location", "Lon: " + ____________________);
            } else {
                Log.d("Week10Location", "____________________");
            }
        });
```

### Verification Criteria

- [ ] App builds after adding dependency
- [ ] Location client initializes without crash
- [ ] Coordinates or null-state message appear in Logcat
- [ ] Permission is checked before location request

### Testing notes

If location is null:

- enable GPS
- test on a real device if emulator is unreliable
- move emulator location using Extended Controls

---

## Exercise 8: Save Location with Scan Result to Room DB (1.5 hours)

**Objective:** Connect everything by storing scan result data with optional latitude and longitude in Room.

### What you will practice

- Updating Room entity
- Database migration thinking
- Saving nullable coordinates
- Displaying location in history/result UI

### Task

Update your `ScanEntity` so a completed scan record stores `latitude` and `longitude`.

### Step-by-Step

#### 1. Update entity fields

```java
@Entity(tableName = "scan_history")
public class ScanEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String diseaseName;
    private double confidence;
    private long scannedAt;
    private Double latitude;
    private Double longitude;

    public ScanEntity(String diseaseName, double confidence, long scannedAt, Double latitude, Double longitude) {
        this.diseaseName = diseaseName;
        this.confidence = confidence;
        this.scannedAt = scannedAt;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
```

#### 2. Increase Room DB version

```java
@Database(entities = {ScanEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScanDao scanDao();
}
```

#### 3. Add migration if you already have existing installs

```java
static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL("ALTER TABLE scan_history ADD COLUMN latitude REAL");
        database.execSQL("ALTER TABLE scan_history ADD COLUMN longitude REAL");
    }
};
```

#### 4. Save scan after location callback

```java
private void saveScanRecord(String diseaseName, double confidence, Double latitude, Double longitude) {
    ScanEntity entity = new ScanEntity(
            diseaseName,
            confidence,
            System.currentTimeMillis(),
            latitude,
            longitude
    );

    Executors.newSingleThreadExecutor().execute(() -> appDatabase.scanDao().insert(entity));
}
```

#### 5. Show location in UI

```java
if (scanEntity.getLatitude() != null && scanEntity.getLongitude() != null) {
    locationTextView.setText(scanEntity.getLatitude() + ", " + scanEntity.getLongitude());
} else {
    locationTextView.setText("Location unavailable");
}
```

### Code Scaffold

```java
private Double ____________________;
private Double ____________________;

ScanEntity entity = new ScanEntity(
        diseaseName,
        confidence,
        System.currentTimeMillis(),
        ____________________,
        ____________________
);
```

### Verification Criteria

- [ ] Room entity compiles with new nullable coordinate fields
- [ ] Database version updated correctly
- [ ] New scan records save with coordinates when available
- [ ] App saves scan even when location is unavailable
- [ ] UI displays stored coordinates or fallback text

### Final thinking question

Why is `Double` a better choice than primitive `double` for latitude and longitude in this case?

---

## Exercise Completion Tracker

- [ ] Exercise 1 complete: Notification channel created
- [ ] Exercise 2 complete: Basic notification sent
- [ ] Exercise 3 complete: Tap action works
- [ ] Exercise 4 complete: Share text chooser works
- [ ] Exercise 5 complete: Image share works with FileProvider
- [ ] Exercise 6 complete: Runtime location permission flow works
- [ ] Exercise 7 complete: Device location logged successfully
- [ ] Exercise 8 complete: Room stores coordinates with scan history

---

## Suggested Order of Work

1. Do Exercises 1-3 together (notifications)
2. Then do Exercise 4 (simple text sharing)
3. Then do Exercise 5 carefully (FileProvider image sharing)
4. Then do Exercises 6-7 together (location permission + fetch)
5. Finish with Exercise 8 (Room integration)

---

## Evidence to Collect for Week 10

Save the following as screenshots or notes:

- [ ] Notification channel visible in settings
- [ ] Notification visible in notification shade
- [ ] Chooser screen for text sharing
- [ ] Chooser screen for image sharing
- [ ] Logcat showing latitude/longitude
- [ ] Result/history screen showing saved coordinates

---

## Before Moving to Build Task

Make sure you can answer these without checking the notes:

1. Why does Android 8.0 require channels?
2. Why is `FLAG_IMMUTABLE` recommended for PendingIntent?
3. Why is `text/plain` used for text sharing?
4. Why do images require FileProvider?
5. Why can `getLastLocation()` return null?

If you can answer those confidently, you are ready for the full Week 10 build task.



<!-- NAV_FOOTER_START -->

---

## 📚 Week 10 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| **3** | **exercises.md** ← *You are here* | **Practice Exercises** |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Theory & Learning Notes](learning-notes.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Practice Exercises** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Build Implementation Guide →](build-task.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 09: TensorFlow Lite Offline AI](../week-09-tensorflow-lite-offline-ai/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 11: Testing, Debugging & Performance ➡](../week-11-testing-debugging-performance/README.md) |

---
