# Week 10: [Placeholder - Complete following Week 09 pattern]

This file follows the same structure as Week 09 but focuses on:
- Notifications (NotificationChannel, NotificationManager)
- Share intent (Intent.ACTION_SEND)
- Location (FusedLocationProviderClient)

## Key Implementation Points

### Notifications
```java
NotificationChannel channel = new NotificationChannel(
    CHANNEL_ID,
    "Scan Reminders",
    NotificationManager.IMPORTANCE_DEFAULT
);
notificationManager.createNotificationChannel(channel);

NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
    .setSmallIcon(R.drawable.ic_notification)
    .setContentTitle("LeafGuard Reminder")
    .setContentText("Time to check your plants!")
    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
```

### Share Intent
```java
Intent shareIntent = new Intent(Intent.ACTION_SEND);
shareIntent.setType("text/plain");
shareIntent.putExtra(Intent.EXTRA_TEXT, "Disease: " + disease + "\nConfidence: " + confidence);
startActivity(Intent.createChooser(shareIntent, "Share result"));
```

### Location
```java
FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);

if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == GRANTED) {
    locationClient.getLastLocation().addOnSuccessListener(location -> {
        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            // Save with scan
        }
    });
}
```

## Validation
- All three features implemented
- Notifications work on API 26+
- Share opens chooser with apps
- Location permission handled gracefully

