# Week 10 Build Task: Add Notifications, Share Intent, and Location Services to LeafGuard AI

## Objective

Integrate three real-world Android platform features into your LeafGuard AI app:

1. **Notifications** to remind users to scan plants regularly
2. **Share Intent** so scan results can be shared with other apps
3. **Location Services** so each scan can optionally store where it happened

By the end of this build task, your app should be able to:

- schedule and display scan reminder notifications
- open the app when the notification is tapped
- share scan results as text
- share scan images securely using FileProvider
- request runtime location permission properly
- fetch device location with `FusedLocationProviderClient`
- save latitude/longitude with each scan in Room

**Estimated Time:** 7-10 hours

---

## Prerequisites

Before starting, ensure:

- [ ] Week 09 Room database and scan history are working
- [ ] Your app already has a result screen after scanning
- [ ] You understand Java Activities, Intents, and Room basics
- [ ] You can build and run the app successfully before making Week 10 changes

---

## Implementation Steps

### Step 1: Add Required Permissions and Provider to `AndroidManifest.xml`

Add notification, location, and FileProvider configuration.

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.leafguardai">

    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

    <!-- Needed only when targeting Android 13+ -->
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.LeafGuardAI">

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>

    </application>
</manifest>
```

### Why this step matters

- Location APIs will fail without manifest permissions
- File sharing will fail without FileProvider
- Android 13+ notification posting may require runtime permission

### Verification

- [ ] Manifest compiles cleanly
- [ ] No duplicated permissions
- [ ] Provider declaration matches your package name through `${applicationId}.provider`

---

### Step 2: Create `res/xml/file_paths.xml`

Create a new XML file in `app/src/main/res/xml/file_paths.xml`.

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <cache-path
        name="shared_images"
        path="images/" />
    <files-path
        name="internal_images"
        path="images/" />
</paths>
```

### Why this step matters

This file tells FileProvider which internal folders may be shared securely with other apps.

### Verification

- [ ] `res/xml/` directory exists
- [ ] `file_paths.xml` has no XML syntax errors
- [ ] The directory where you save share images matches one of these paths

---

### Step 3: Add Dependencies for WorkManager and Location

In `build.gradle (Module: app)`, add:

```gradle
dependencies {
    implementation 'androidx.core:core:1.13.1'
    implementation 'androidx.work:work-runtime:2.9.0'
    implementation 'com.google.android.gms:play-services-location:21.3.0'
}
```

Then sync Gradle.

### Verification

- [ ] Gradle sync succeeds
- [ ] No dependency conflicts
- [ ] `Worker` and `FusedLocationProviderClient` imports resolve correctly

---

### Step 4: Create `NotificationHelper.java`

Create `NotificationHelper.java` in a suitable package such as `helpers`.

```java
package com.example.leafguardai.helpers;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.leafguardai.MainActivity;
import com.example.leafguardai.R;

public class NotificationHelper {

    public static final String CHANNEL_SCAN_REMINDERS = "leafguard_scan_reminders";
    public static final String CHANNEL_DISEASE_ALERTS = "leafguard_disease_alerts";

    private final Context context;

    public NotificationHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    public void createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager == null) {
                return;
            }

            NotificationChannel reminderChannel = new NotificationChannel(
                    CHANNEL_SCAN_REMINDERS,
                    "Scan Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            reminderChannel.setDescription("Reminds users to scan plants regularly.");

            NotificationChannel alertChannel = new NotificationChannel(
                    CHANNEL_DISEASE_ALERTS,
                    "Disease Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            alertChannel.setDescription("Important disease-related alerts.");

            manager.createNotificationChannel(reminderChannel);
            manager.createNotificationChannel(alertChannel);
        }
    }

    public void sendReminderNotification(int notificationId, String title, String message) {
        createChannels();

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("from_notification", true);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, flags);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_SCAN_REMINDERS)
                .setSmallIcon(R.drawable.ic_leaf_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationManagerCompat.from(context).notify(notificationId, builder.build());
    }
}
```

### Why this step matters

This helper centralizes channel creation and notification posting. It keeps Activity code cleaner and makes testing easier.

### Verification

- [ ] File compiles successfully
- [ ] `createChannels()` runs without crash
- [ ] `sendReminderNotification()` posts a notification on supported devices

---

### Step 5: Create `ReminderWorker.java` and Schedule Notifications with WorkManager

Create `ReminderWorker.java`.

```java
package com.example.leafguardai.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.leafguardai.helpers.NotificationHelper;

public class ReminderWorker extends Worker {

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationHelper helper = new NotificationHelper(getApplicationContext());
        helper.sendReminderNotification(
                5001,
                "LeafGuard Reminder",
                "Time to scan your plants and check for disease symptoms."
        );
        return Result.success();
    }
}
```

Now schedule periodic work, for example in `MainActivity` or settings screen:

```java
private void scheduleDailyReminder() {
    PeriodicWorkRequest reminderWork = new PeriodicWorkRequest.Builder(
            ReminderWorker.class,
            24,
            TimeUnit.HOURS
    ).build();

    WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "leafguard_daily_reminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            reminderWork
    );
}
```

### Optional test shortcut

For testing, you can also trigger a normal immediate notification with a button before relying on periodic background execution.

### Verification

- [ ] Worker class compiles
- [ ] WorkManager request enqueues without crash
- [ ] Unique periodic work avoids duplicate reminder jobs
- [ ] Manual notification test works before periodic testing

---

### Step 6: Implement Share Text Feature in `ResultActivity`

Add a share button in your layout.

```xml
<Button
    android:id="@+id/shareTextButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Share Result" />
```

In `ResultActivity.java`:

```java
private void shareResultText(String diseaseName, double confidence, String treatment, Double latitude, Double longitude) {
    StringBuilder textBuilder = new StringBuilder();
    textBuilder.append("LeafGuard AI Scan Result\n");
    textBuilder.append("Disease: ").append(diseaseName).append("\n");
    textBuilder.append("Confidence: ")
            .append(String.format(java.util.Locale.US, "%.1f%%", confidence * 100))
            .append("\n");

    if (treatment != null && !treatment.isEmpty()) {
        textBuilder.append("Treatment: ").append(treatment).append("\n");
    }

    if (latitude != null && longitude != null) {
        textBuilder.append("Location: ").append(latitude).append(", ").append(longitude).append("\n");
    }

    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "LeafGuard AI Scan Result");
    shareIntent.putExtra(Intent.EXTRA_TEXT, textBuilder.toString());

    if (shareIntent.resolveActivity(getPackageManager()) != null) {
        startActivity(Intent.createChooser(shareIntent, "Share scan result"));
    }
}
```

Wire it to button click:

```java
Button shareTextButton = findViewById(R.id.shareTextButton);
shareTextButton.setOnClickListener(v ->
        shareResultText(diseaseName, confidence, treatment, latitude, longitude)
);
```

### Verification

- [ ] Chooser opens successfully
- [ ] Shared text includes disease and formatted confidence
- [ ] Location line appears only if coordinates exist

---

### Step 7: Implement Share Image Feature with FileProvider

Add another button to layout.

```xml
<Button
    android:id="@+id/shareImageButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Share Image" />
```

In `ResultActivity.java`, add image sharing logic:

```java
private void shareResultImage(Bitmap bitmap, String diseaseName, double confidence) {
    try {
        File imageDir = new File(getCacheDir(), "images");
        if (!imageDir.exists() && !imageDir.mkdirs()) {
            Toast.makeText(this, "Could not create image directory", Toast.LENGTH_SHORT).show();
            return;
        }

        File imageFile = new File(imageDir, "leafguard_shared_result.jpg");
        FileOutputStream outputStream = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        outputStream.flush();
        outputStream.close();

        Uri imageUri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".provider",
                imageFile
        );

        String shareText = "LeafGuard AI Result\nDisease: " + diseaseName
                + "\nConfidence: "
                + String.format(java.util.Locale.US, "%.1f%%", confidence * 100);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Share scan image"));
        }

    } catch (IOException e) {
        Toast.makeText(this, "Image share failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
```

### Important note

Do **not** use:

```java
Uri.fromFile(imageFile)
```

Use `FileProvider.getUriForFile()` instead.

### Verification

- [ ] No FileUriExposedException
- [ ] Receiving apps can preview the shared image
- [ ] FileProvider authority matches manifest
- [ ] Image comes from cache or files path declared in `file_paths.xml`

---

### Step 8: Create `LocationHelper.java`

Create a helper to isolate permission-safe location code.

```java
package com.example.leafguardai.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {

    public interface LocationResultListener {
        void onLocationAvailable(Double latitude, Double longitude);
        void onLocationUnavailable(String reason);
    }

    private final Context context;
    private final FusedLocationProviderClient fusedLocationProviderClient;

    public LocationHelper(Context context) {
        this.context = context.getApplicationContext();
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLastLocation(@NonNull LocationResultListener listener) {
        if (!hasLocationPermission()) {
            listener.onLocationUnavailable("Location permission not granted");
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        listener.onLocationAvailable(location.getLatitude(), location.getLongitude());
                    } else {
                        listener.onLocationUnavailable("Location unavailable");
                    }
                })
                .addOnFailureListener(e -> listener.onLocationUnavailable(e.getMessage()));
    }

    public static void requestPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                requestCode
        );
    }
}
```

### Verification

- [ ] Helper compiles cleanly
- [ ] `hasLocationPermission()` returns correct state
- [ ] `requestLastLocation()` handles granted and denied paths

---

### Step 9: Add Complete Runtime Permission Flow in Activity

In `ResultActivity` or whichever screen saves scans:

```java
private static final int REQUEST_LOCATION_PERMISSION = 700;
private LocationHelper locationHelper;
private Double latestLatitude;
private Double latestLongitude;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);

    locationHelper = new LocationHelper(this);
}

private void ensureLocationAndSave() {
    if (locationHelper.hasLocationPermission()) {
        fetchLocationAndSave();
    } else {
        LocationHelper.requestPermission(this, REQUEST_LOCATION_PERMISSION);
    }
}

private void fetchLocationAndSave() {
    locationHelper.requestLastLocation(new LocationHelper.LocationResultListener() {
        @Override
        public void onLocationAvailable(Double latitude, Double longitude) {
            latestLatitude = latitude;
            latestLongitude = longitude;
            saveScanToDatabase();
        }

        @Override
        public void onLocationUnavailable(String reason) {
            latestLatitude = null;
            latestLongitude = null;
            Toast.makeText(ResultActivity.this, reason + ". Saving scan without location.", Toast.LENGTH_SHORT).show();
            saveScanToDatabase();
        }
    });
}

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == REQUEST_LOCATION_PERMISSION) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLocationAndSave();
        } else {
            latestLatitude = null;
            latestLongitude = null;
            Toast.makeText(this, "Location permission denied. Saving scan without location.", Toast.LENGTH_SHORT).show();
            saveScanToDatabase();
        }
    }
}
```

### Verification

- [ ] Permission dialog appears when needed
- [ ] Grant path saves with coordinates when available
- [ ] Deny path still saves scan without crash

---

### Step 10: Update `ScanEntity.java` with Latitude and Longitude

```java
package com.example.leafguardai.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scan_history")
public class ScanEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String diseaseName;
    private double confidence;
    private String imagePath;
    private long scannedAt;
    private Double latitude;
    private Double longitude;

    public ScanEntity(String diseaseName,
                      double confidence,
                      String imagePath,
                      long scannedAt,
                      Double latitude,
                      Double longitude) {
        this.diseaseName = diseaseName;
        this.confidence = confidence;
        this.imagePath = imagePath;
        this.scannedAt = scannedAt;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getImagePath() {
        return imagePath;
    }

    public long getScannedAt() {
        return scannedAt;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
```

### Verification

- [ ] Entity compiles
- [ ] New fields are nullable
- [ ] Existing code updated to use new constructor if necessary

---

### Step 11: Update Room Database Version and Migration

If your Week 09 app already has an installed database, migration is needed.

```java
package com.example.leafguardai.data;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ScanEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScanDao scanDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE scan_history ADD COLUMN latitude REAL");
            database.execSQL("ALTER TABLE scan_history ADD COLUMN longitude REAL");
        }
    };
}
```

When building database:

```java
appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "leafguard.db")
        .addMigrations(AppDatabase.MIGRATION_1_2)
        .build();
```

### Verification

- [ ] Database version increased
- [ ] Migration compiles
- [ ] Old installs can still open database without destructive reset

---

### Step 12: Save Scan Records with Location

Update your save method.

```java
private void saveScanToDatabase() {
    ScanEntity scanEntity = new ScanEntity(
            diseaseName,
            confidence,
            imagePath,
            System.currentTimeMillis(),
            latestLatitude,
            latestLongitude
    );

    Executors.newSingleThreadExecutor().execute(() -> {
        appDatabase.scanDao().insert(scanEntity);
        runOnUiThread(() -> Toast.makeText(this, "Scan saved successfully", Toast.LENGTH_SHORT).show());
    });
}
```

### Verification

- [ ] Record saves successfully
- [ ] Coordinates are stored when available
- [ ] Null coordinates are allowed when unavailable

---

### Step 13: Update UI to Show Location and Share Buttons

Add views to `activity_result.xml`.

```xml
<TextView
    android:id="@+id/locationTextView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Location unavailable" />

<Button
    android:id="@+id/shareTextButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Share Result" />

<Button
    android:id="@+id/shareImageButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Share Image" />
```

Update text in Java:

```java
TextView locationTextView = findViewById(R.id.locationTextView);

if (latestLatitude != null && latestLongitude != null) {
    locationTextView.setText("Location: " + latestLatitude + ", " + latestLongitude);
} else {
    locationTextView.setText("Location unavailable");
}
```

### Verification

- [ ] Buttons appear on result screen
- [ ] Location text updates correctly
- [ ] UI handles null state clearly

---

### Step 14: Integration Testing

Perform the following test sequence:

#### Notification flow

1. Launch app
2. Call `createChannels()`
3. Trigger immediate reminder notification
4. Pull down notification shade
5. Verify title/text/icon
6. Tap notification
7. Confirm app opens and `from_notification` extra is handled

#### Share text flow

1. Open a result screen
2. Tap **Share Result**
3. Confirm chooser appears
4. Select Gmail or Notes
5. Confirm shared text includes disease and confidence

#### Share image flow

1. Open a result with bitmap/image available
2. Tap **Share Image**
3. Confirm chooser appears
4. Select an app that previews images
5. Confirm image is attached and visible

#### Location + Room flow

1. Trigger scan save
2. Grant location permission
3. Fetch location
4. Save record
5. Open history screen
6. Confirm latitude/longitude appear

#### Denied-permission flow

1. Revoke location permission in system settings
2. Save another scan
3. Deny permission request
4. Confirm app shows graceful message
5. Confirm scan still saves without crash

---

## Completion Checklist

### Notifications
- [ ] Notification channels created for reminders and alerts
- [ ] Basic reminder notification posts successfully
- [ ] PendingIntent opens app on tap
- [ ] WorkManager schedules periodic reminder

### Share Feature
- [ ] Text share chooser works
- [ ] Image share works with FileProvider
- [ ] No raw file URI sharing is used
- [ ] Chooser title is user-friendly

### Location
- [ ] Manifest permissions added
- [ ] Runtime permission flow implemented
- [ ] FusedLocationProviderClient returns location or graceful fallback
- [ ] No crash when location is unavailable

### Room Integration
- [ ] `ScanEntity` includes `latitude` and `longitude`
- [ ] Room version updated
- [ ] Migration added if needed
- [ ] Scan records persist with optional coordinates

### UI/UX
- [ ] Result screen shows location state
- [ ] Share buttons visible and functional
- [ ] User messages are understandable
- [ ] App remains usable even when services fail

---

## Final Deliverable

When Week 10 is complete, you should be able to demonstrate:

1. A reminder notification being posted
2. Opening the app from the notification tap
3. Sharing scan results as text
4. Sharing scan images using FileProvider safely
5. Requesting location permission and logging coordinates
6. Saving a scan record with optional latitude/longitude in Room

---

## Teacher Demo Script

Use this during your CSE 2206 demonstration:

1. Show the notification channel in Android settings
2. Trigger and display a reminder notification
3. Tap the notification and show the app opens
4. Open a scan result and share it as text
5. Share the scan image and explain FileProvider
6. Save a scan with location and show stored coordinates in history
7. Revoke permission and show graceful fallback behavior

---

## Week 10 Build Task Complete

Once all steps pass:

- [ ] Mark Week 10 complete in your tracker
- [ ] Save evidence screenshots
- [ ] Commit your code to Git
- [ ] Review viva questions in `learning-notes.md`

**Next:** move toward final polish, testing, and presentation readiness.

