# Week 10 Solution - Notifications, Share Intent, and Location Services

This solution completes the Week 10 Android platform features for LeafGuard AI.

It includes:
- `NotificationHelper.java`
- `ShareUtils.java`
- `LocationHelper.java`
- `ResultActivity` integration
- manifest and string updates

---

## 1. `NotificationHelper.java`

```java
package com.leafguard.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.leafguard.MainActivity;
import com.leafguard.R;
import com.leafguard.ResultActivity;

public final class NotificationHelper {

    public static final String CHANNEL_SCAN_RESULTS = "leafguard_scan_results";
    private static final String CHANNEL_NAME = "Scan Results";
    private static final String CHANNEL_DESCRIPTION = "Notifications sent after disease detection completes.";

    private NotificationHelper() {
    }

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_SCAN_RESULTS,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void sendDetectionNotification(Context context, String diseaseName, float confidence, String imageUri) {
        createChannel(context);

        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_DISEASE_NAME, diseaseName);
        intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, confidence);
        if (imageUri != null) {
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2001, intent, flags);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_SCAN_RESULTS)
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setContentTitle(context.getString(R.string.notification_detection_title))
                .setContentText(context.getString(R.string.notification_detection_message, diseaseName, Math.round(confidence * 100f)))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(context.getString(R.string.notification_detection_message, diseaseName, Math.round(confidence * 100f))))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(context).notify(2001, builder.build());
    }

    public static void sendReminderNotification(Context context) {
        createChannel(context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("from_notification", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2002, intent, flags);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_SCAN_RESULTS)
                .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
                .setContentTitle(context.getString(R.string.notification_reminder_title))
                .setContentText(context.getString(R.string.notification_reminder_message))
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(context).notify(2002, builder.build());
    }
}
```

---

## 2. `ShareUtils.java`

```java
package com.leafguard.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.leafguard.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ShareUtils {

    private ShareUtils() {
    }

    public static void shareText(
            Context context,
            String diseaseName,
            float confidence,
            String symptoms,
            String treatment,
            String prevention,
            String locationText
    ) {
        String shareText = context.getString(
                R.string.share_result_template_with_location,
                diseaseName,
                Math.round(confidence * 100f),
                symptoms,
                treatment,
                prevention,
                locationText == null ? context.getString(R.string.location_not_available) : locationText
        );

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_subject));
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_chooser_title)));
    }

    public static void shareImageFromUri(@NonNull Context context, @NonNull Uri sourceUri, @NonNull String caption)
            throws IOException {
        File cacheDir = new File(context.getCacheDir(), "share");
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new IOException("Unable to create share cache directory.");
        }

        File outputFile = new File(cacheDir, "leafguard_share_" + System.currentTimeMillis() + ".jpg");
        try (InputStream inputStream = context.getContentResolver().openInputStream(sourceUri);
             OutputStream outputStream = new FileOutputStream(outputFile)) {
            if (inputStream == null) {
                throw new IOException("Unable to open image stream.");
            }
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        Uri shareUri = androidx.core.content.FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".fileprovider",
                outputFile
        );

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, shareUri);
        intent.putExtra(Intent.EXTRA_TEXT, caption);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_chooser_title)));
    }
}
```

---

## 3. `LocationHelper.java`

```java
package com.leafguard.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

public class LocationHelper {

    public interface Callback {
        void onLocationReady(@Nullable Location location, @NonNull String summary);
    }

    private final Context appContext;
    private final FusedLocationProviderClient fusedLocationProviderClient;

    public LocationHelper(Context context) {
        this.appContext = context.getApplicationContext();
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext);
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void getLastKnownLocation(@NonNull Callback callback) {
        if (!hasLocationPermission()) {
            callback.onLocationReady(null, "Location permission not granted");
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> callback.onLocationReady(location, formatLocation(location)))
                .addOnFailureListener(error -> callback.onLocationReady(null, "Location unavailable"));
    }

    @NonNull
    public static String formatLocation(@Nullable Location location) {
        if (location == null) {
            return "Location unavailable";
        }
        return String.format(Locale.US, "Lat %.5f, Lng %.5f", location.getLatitude(), location.getLongitude());
    }
}
```

---

## 4. `ResultActivity.java` integration

```java
package com.leafguard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.leafguard.databinding.ActivityResultBinding;
import com.leafguard.util.LocationHelper;
import com.leafguard.util.NotificationHelper;
import com.leafguard.util.ShareUtils;

import java.io.IOException;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_DISEASE_NAME = "extra_disease_name";
    public static final String EXTRA_CONFIDENCE = "extra_confidence";
    public static final String EXTRA_IMAGE_URI = "extra_image_uri";

    private ActivityResultBinding binding;
    private String diseaseName;
    private float confidence;
    private String imageUri;
    private String locationSummary = "Location unavailable";
    private LocationHelper locationHelper;
    private ActivityResultLauncher<String[]> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        diseaseName = getIntent().getStringExtra(EXTRA_DISEASE_NAME);
        confidence = getIntent().getFloatExtra(EXTRA_CONFIDENCE, 0f);
        imageUri = getIntent().getStringExtra(EXTRA_IMAGE_URI);

        locationHelper = new LocationHelper(this);
        registerPermissionLauncher();
        requestOptionalPermissions();
        bindData();
        setupButtons();
        NotificationHelper.sendDetectionNotification(this, diseaseName, confidence, imageUri);
    }

    private void registerPermissionLauncher() {
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> fetchLocationIfPossible()
        );
    }

    private void requestOptionalPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(new String[]{Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.ACCESS_COARSE_LOCATION});
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION});
            return;
        }

        fetchLocationIfPossible();
    }

    private void fetchLocationIfPossible() {
        locationHelper.getLastKnownLocation((location, summary) -> {
            locationSummary = summary;
            binding.textLocationValue.setText(summary);
        });
    }

    private void bindData() {
        binding.textDiseaseName.setText(diseaseName == null ? getString(R.string.result_unknown_disease) : diseaseName);
        binding.textConfidenceValue.setText(getString(R.string.confidence_format, Math.round(confidence * 100f)));
        binding.textLocationValue.setText(locationSummary);
    }

    private void setupButtons() {
        binding.buttonShare.setOnClickListener(view -> ShareUtils.shareText(
                this,
                binding.textDiseaseName.getText().toString(),
                confidence,
                binding.textSymptoms.getText().toString(),
                binding.textTreatment.getText().toString(),
                binding.textPrevention.getText().toString(),
                locationSummary
        ));

        binding.buttonShareImage.setOnClickListener(view -> {
            if (imageUri == null) {
                Toast.makeText(this, R.string.image_share_unavailable, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                String caption = getString(R.string.notification_detection_message, diseaseName, Math.round(confidence * 100f));
                ShareUtils.shareImageFromUri(this, Uri.parse(imageUri), caption);
            } catch (IOException exception) {
                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
```

---

## 5. Required `AndroidManifest.xml` updates

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

<application ...>
    <provider
        android:name="androidx.core.content.FileProvider"
        android:authorities="${applicationId}.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_paths" />
    </provider>
</application>
```

---

## 6. String additions

```xml
<string name="notification_detection_title">LeafGuard Detection Complete</string>
<string name="notification_detection_message">%1$s detected with %2$d%% confidence.</string>
<string name="notification_reminder_title">LeafGuard Reminder</string>
<string name="notification_reminder_message">Time to scan your plants today.</string>
<string name="share_result_template_with_location">LeafGuard AI detected: %1$s
Confidence: %2$d%%

Symptoms:
%3$s

Treatment:
%4$s

Prevention:
%5$s

Location:
%6$s</string>
<string name="location_not_available">Location unavailable</string>
<string name="image_share_unavailable">No image is available to share.</string>
```

---

## 7. Layout additions for `ResultActivity`

```xml
<TextView
    android:id="@+id/textLocationLabel"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Location" />

<TextView
    android:id="@+id/textLocationValue"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Location unavailable" />

<com.google.android.material.button.MaterialButton
    android:id="@+id/buttonShareImage"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Share Image" />
```

---

## 8. Week 10 checklist

- [x] notification channel helper included
- [x] result notification included
- [x] share text helper included
- [x] share image helper included
- [x] last known location helper included
- [x] `ResultActivity` integration included
- [x] manifest updates included


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

- 📝 [Back to Week 10 Exercises](../../roadmap/week-10-notifications-share-location/exercises.md) — Try it yourself first
- 📖 [Week 10 README](../../roadmap/week-10-notifications-share-location/README.md) — Week overview
- 💡 [Solutions Index for Week 10](README.md) — Other solutions this week
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

---
