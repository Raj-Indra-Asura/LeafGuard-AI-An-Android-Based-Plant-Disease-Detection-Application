# Notification Code Examples

## 1. Create notification channel

```java
private void createReminderChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel(
                "leafguard_reminders",
                "LeafGuard Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("Plant care and scan reminders");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }
}
```

## 2. Show notification

```java
private void showReminder() {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "leafguard_reminders")
            .setSmallIcon(R.drawable.ic_leaf)
            .setContentTitle("Check your plants")
            .setContentText("Scan suspicious leaves before disease spreads.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true);

    NotificationManagerCompat.from(this).notify(1001, builder.build());
}
```

## Android 13 rule

On Android 13+, request `POST_NOTIFICATIONS` at runtime before showing notifications.
