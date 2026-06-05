# Week 10: Learning Notes - Notifications, Share Intent, and Location Services

## Table of Contents

1. [Why Week 10 Matters in LeafGuard AI](#1-why-week-10-matters-in-leafguard-ai)
2. [Android Notification System Architecture](#2-android-notification-system-architecture)
3. [Building Notifications Step-by-Step](#3-building-notifications-step-by-step)
4. [Notification Channels Explained Deeply](#4-notification-channels-explained-deeply)
5. [PendingIntent and Tap Actions](#5-pendingintent-and-tap-actions)
6. [Scheduled Notifications with WorkManager and AlarmManager](#6-scheduled-notifications-with-workmanager-and-alarmmanager)
7. [Android Share Intent System](#7-android-share-intent-system)
8. [Sharing Text Results from LeafGuard AI](#8-sharing-text-results-from-leafguard-ai)
9. [Sharing Images Safely with FileProvider](#9-sharing-images-safely-with-fileprovider)
10. [Location Services Architecture](#10-location-services-architecture)
11. [Location Permissions and Runtime Flow](#11-location-permissions-and-runtime-flow)
12. [Getting a Single Location vs Continuous Updates](#12-getting-a-single-location-vs-continuous-updates)
13. [Main Thread Safety and Async Thinking](#13-main-thread-safety-and-async-thinking)
14. [Saving Location with Scan History in Room](#14-saving-location-with-scan-history-in-room)
15. [Error Handling and Defensive Programming](#15-error-handling-and-defensive-programming)
16. [Best Practices for Production-Ready Features](#16-best-practices-for-production-ready-features)
17. [Week 10 CSE 2206 Viva Preparation Q&A](#17-week-10-cse-2206-viva-preparation-qa)
18. [Key Takeaways](#18-key-takeaways)

---

## 1. Why Week 10 Matters in LeafGuard AI

By Week 10, your LeafGuard AI app should already be able to capture or select plant images, run disease detection, and store useful information. This week adds three features that make the app feel much more like a real Android product:

- **Notifications** remind the user to scan plants regularly.
- **Share Intent** lets the user share scan results with farmers, teachers, friends, or agronomists.
- **Location Services** let the app attach a place to each scan so the user knows *where* a disease was detected.

These are important because they connect core Android platform services to your app logic.

### Real LeafGuard AI use cases

1. A farmer wants a reminder every morning to inspect leaves.
2. A student wants to share scan results to WhatsApp or Gmail.
3. A user wants each scan saved with GPS coordinates for future tracking.
4. A teacher wants to see that you understand Android system components, permissions, and data flow.

### CSE 2206 connection

This week directly strengthens several course ideas:

- **Android system services**
- **Intent-based communication**
- **Permissions and security**
- **Background work and scheduling**
- **Database design and local persistence**
- **User experience improvements**

Week 10 is not just about adding features. It is about understanding how Android apps interact with the operating system in a safe, user-controlled way.

---

## 2. Android Notification System Architecture

### What is a notification?

A notification is a message shown by Android outside your app UI. It can appear in:

- The status bar
- The notification shade
- The lock screen
- Heads-up popups (for higher importance)
- Wearables or companion devices in some cases

In LeafGuard AI, a notification could say:

> “LeafGuard Reminder: Time to scan your tomato plants today.”

### Core architecture

The Android notification system has several pieces working together:

```text
Your App Code
    ↓
NotificationCompat.Builder
    ↓
Notification object
    ↓
NotificationManager / NotificationManagerCompat
    ↓
Android System UI
    ↓
Status bar / shade / heads-up / lock screen
```

### Main classes you must know

| Class | Purpose |
|------|---------|
| `NotificationCompat.Builder` | Builds a notification in a backward-compatible way |
| `NotificationManagerCompat` | Sends notification to system |
| `NotificationChannel` | Defines category/behavior of notifications on API 26+ |
| `PendingIntent` | Action performed when user taps notification |
| `WorkManager` | Schedules reliable background work |
| `AlarmManager` | Time-based alarm scheduling |

### Why NotificationCompat is preferred

Android has many API versions. `NotificationCompat` from AndroidX helps one code path work across old and new devices.

Use:

```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
```

instead of using only platform-specific code.

### Notification lifecycle

1. App creates notification channel (API 26+).
2. App builds notification object.
3. App calls notify(notificationId, notification).
4. System displays notification.
5. User taps, dismisses, or ignores it.
6. A `PendingIntent` may open an Activity or broadcast receiver.

### Notification IDs

Every notification has an integer ID.

```java
notificationManager.notify(1001, builder.build());
```

Why it matters:

- Same ID can **update** existing notification.
- Different ID creates a **new** notification.
- You can cancel later with `cancel(1001)`.

### Categories

Android lets you declare the purpose of a notification using category hints.

Examples:

- `NotificationCompat.CATEGORY_REMINDER`
- `NotificationCompat.CATEGORY_STATUS`
- `NotificationCompat.CATEGORY_MESSAGE`
- `NotificationCompat.CATEGORY_ALARM`

For LeafGuard AI reminders, `CATEGORY_REMINDER` is a sensible choice.

```java
builder.setCategory(NotificationCompat.CATEGORY_REMINDER);
```

### Importance vs priority

This confuses many students.

#### On Android 8.0+ (API 26+)
Behavior is mostly controlled by **channel importance**.

#### On older Android versions
Behavior is influenced by **builder priority**.

So you usually set both:

```java
builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
```

and create a channel using:

```java
NotificationManager.IMPORTANCE_DEFAULT
```

### Notification importance levels

| Level | Meaning | Typical behavior |
|------|---------|------------------|
| `IMPORTANCE_NONE` | Hidden | No visual interruption |
| `IMPORTANCE_MIN` | Very low | Lowest visibility |
| `IMPORTANCE_LOW` | Low importance | Silent, appears in shade |
| `IMPORTANCE_DEFAULT` | Standard | Makes sound, visible in shade |
| `IMPORTANCE_HIGH` | High | May appear as heads-up |
| `IMPORTANCE_MAX` | Very intrusive | Rarely needed |

For LeafGuard AI:

- **Scan reminders** → `IMPORTANCE_DEFAULT`
- **Urgent disease outbreak alert** → maybe `IMPORTANCE_HIGH`
- **Background sync status** → `IMPORTANCE_LOW`

### Notification styles

Basic notifications show small amount of text. Android also supports styles:

- `BigTextStyle`
- `BigPictureStyle`
- `InboxStyle`
- `MessagingStyle`

LeafGuard AI may use:

- `BigTextStyle` for detailed advice
- `BigPictureStyle` when sharing disease snapshot previews

Example:

```java
builder.setStyle(new NotificationCompat.BigTextStyle()
        .bigText("Time to inspect your plants. Early detection helps reduce disease spread and crop loss."));
```

### Notification architecture diagram for LeafGuard AI

```text
ReminderWorker / User Action
            ↓
    NotificationHelper
            ↓
   createChannelIfNeeded()
            ↓
 NotificationCompat.Builder
            ↓
 setContentTitle(), setContentText(), setSmallIcon()
            ↓
    setContentIntent(pendingIntent)
            ↓
 NotificationManagerCompat.notify()
            ↓
     User taps notification
            ↓
        MainActivity opens
```

### Why Android 8.0 changed notification behavior

Before API 26, apps controlled too much. Users could only block the whole app or accept everything. Android introduced channels so users can control categories individually.

That means:

- LeafGuard reminders can be muted without muting all notifications.
- Users gain transparency.
- Apps must respect user choice.

This is important for UX and privacy.

---

## 3. Building Notifications Step-by-Step

This is the practical part most viva questions will focus on.

### Step 1: Add dependency if needed

Most Android projects using AndroidX already have core support. The usual import is:

```java
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
```

### Step 2: Ensure a small icon exists

Every notification must have a valid small icon.

```java
.setSmallIcon(R.drawable.ic_leaf_notification)
```

If you forget the small icon, notifications may fail or appear broken.

### Step 3: Create a channel first on API 26+

```java
private void createReminderChannel(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel(
                "leafguard_scan_reminders",
                "Scan Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("Reminds the user to scan plants regularly.");

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }
}
```

### Step 4: Build the notification

```java
NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "leafguard_scan_reminders")
        .setSmallIcon(R.drawable.ic_leaf_notification)
        .setContentTitle("LeafGuard Reminder")
        .setContentText("Time to scan your plants today.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setCategory(NotificationCompat.CATEGORY_REMINDER)
        .setAutoCancel(true);
```

### Why each method matters

| Method | Why it is required |
|------|---------------------|
| `setSmallIcon()` | Mandatory visual icon |
| `setContentTitle()` | Main title shown to user |
| `setContentText()` | Short summary or message |
| `setPriority()` | Controls behavior on older APIs |
| `setCategory()` | Helps Android classify it |
| `setAutoCancel(true)` | Dismisses notification when tapped |

### Step 5: Send it

```java
NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
notificationManager.notify(2001, builder.build());
```

### Complete basic example

```java
public void sendBasicReminder(Context context) {
    createReminderChannel(context);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "leafguard_scan_reminders")
            .setSmallIcon(R.drawable.ic_leaf_notification)
            .setContentTitle("LeafGuard Reminder")
            .setContentText("Time to scan your plants today.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true);

    NotificationManagerCompat.from(context).notify(2001, builder.build());
}
```

### Step 6: Add a tap action

A notification that does nothing on tap feels incomplete. Usually you open `MainActivity` or `ResultActivity`.

```java
Intent intent = new Intent(context, MainActivity.class);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
```

Then wrap it in a `PendingIntent`.

### Step 7: Add expanded text if needed

```java
builder.setStyle(new NotificationCompat.BigTextStyle()
        .bigText("Time to scan your crops. Early disease detection helps prevent spread and supports better treatment decisions."));
```

### Step 8: Add an action button if desired

Example action buttons:

- “Open App”
- “Scan Now”
- “Dismiss”

Action button example:

```java
builder.addAction(
        R.drawable.ic_open,
        "Open App",
        pendingIntent
);
```

### Step 9: Think about Android 13 notification permission

Even though your minimum target is API 24+, if your app targets API 33+, posting notifications requires runtime permission `POST_NOTIFICATIONS`.

You should mention this in viva even if the week focuses mainly on channels and builder logic.

```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    // Check POST_NOTIFICATIONS before posting.
}
```

### Common mistakes when building notifications

1. Forgetting `setSmallIcon()`
2. Using wrong channel ID
3. Creating builder before channel exists on API 26+
4. Forgetting `PendingIntent`
5. Posting without notification permission on API 33+
6. Using inconsistent notification IDs
7. Expecting `setPriority()` to override user channel settings on API 26+

---

## 4. Notification Channels Explained Deeply

### What is a notification channel?

A notification channel is a **named category** of notifications. Android 8.0+ requires apps to assign notifications to a channel.

Example channel ideas for LeafGuard AI:

- `leafguard_scan_reminders`
- `leafguard_disease_alerts`
- `leafguard_background_status`

### Why channels exist

Channels exist for **user control**.

Users can:

- Disable a channel entirely
- Change sound/vibration
- Decide whether it appears on lock screen
- Make it silent
- Reduce interruptions

This is a very important design philosophy: **the user should control interruptions, not the app**.

### Channel ID vs channel name

| Property | Meaning |
|---------|---------|
| Channel ID | Internal stable string used by code |
| Channel name | User-visible label in system settings |

Example:

```java
String channelId = "leafguard_scan_reminders";
String channelName = "Scan Reminders";
```

### Channel creation example

```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    NotificationChannel channel = new NotificationChannel(
            "leafguard_scan_reminders",
            "Scan Reminders",
            NotificationManager.IMPORTANCE_DEFAULT
    );
    channel.setDescription("Regular reminders to scan crops with LeafGuard AI.");
    channel.enableVibration(true);
    channel.enableLights(true);

    NotificationManager manager = context.getSystemService(NotificationManager.class);
    if (manager != null) {
        manager.createNotificationChannel(channel);
    }
}
```

### When to create channels

Best practice: create channels once during app startup, or before first notification.

A common place is:

- `Application` class
- `MainActivity.onCreate()`
- `NotificationHelper.createChannels()`

### Safe pattern

```java
public static void createChannels(Context context) {
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
        alertChannel.setDescription("Important alerts for urgent plant health issues.");

        NotificationChannel syncChannel = new NotificationChannel(
                CHANNEL_STATUS,
                "Background Status",
                NotificationManager.IMPORTANCE_LOW
        );
        syncChannel.setDescription("Background sync and non-urgent status updates.");

        manager.createNotificationChannel(reminderChannel);
        manager.createNotificationChannel(alertChannel);
        manager.createNotificationChannel(syncChannel);
    }
}
```

### DEFAULT vs HIGH vs LOW

#### `IMPORTANCE_DEFAULT`
Use for normal reminders.

Behavior:

- Visible in shade
- May make sound
- Not too aggressive

Use case:

- Daily scan reminder

#### `IMPORTANCE_HIGH`
Use carefully for urgent alerts.

Behavior:

- More intrusive
- May show as heads-up popup

Use case:

- “Severe disease pattern detected in repeated scans”

Do **not** abuse this or users may disable your app notifications.

#### `IMPORTANCE_LOW`
Use for quiet informational updates.

Behavior:

- Appears silently
- No popup

Use case:

- “Scan history synced successfully”

### Important limitation: channel importance cannot be freely changed later

Once a channel is created, many settings become controlled by the user and cannot be forcefully reset by the app.

This means:

- Choose the right importance from the beginning.
- If you truly need a different behavior, create a new channel ID.

### Example of wrong thinking

> “I created a LOW channel yesterday; now I will change code to HIGH and it should pop up.”

Often it will **not** behave as expected because the channel may already exist with stored settings.

### Channel groups

Android also supports grouping channels, but for this course project, separate channels are enough.

### Debugging channel issues

If notification does not appear on API 26+:

1. Check channel ID in builder matches created channel ID.
2. Check channel is created before notify.
3. Check user has not disabled the channel.
4. Check app notification permission if testing API 33+.
5. Check `setSmallIcon()` exists.

### Sample viva explanation

> A NotificationChannel is a user-visible category of notifications introduced in Android 8.0. It lets users control sound, interruption level, and visibility separately for each category such as reminders or alerts. In LeafGuard AI I would create a reminder channel using IMPORTANCE_DEFAULT and an urgent disease alert channel using IMPORTANCE_HIGH.

---

## 5. PendingIntent and Tap Actions

### What is a PendingIntent?

A `PendingIntent` is a token that lets the Android system perform an action later on behalf of your app.

Think of it like this:

- `Intent` = what action you want
- `PendingIntent` = permission for another component or the system to perform that intent later

### Why notification taps need PendingIntent

When the user taps a notification, the tap happens in the future, often outside your app process. The notification system needs a safe, packaged instruction.

So instead of:

```java
Intent intent = new Intent(context, MainActivity.class);
```

You use:

```java
PendingIntent pendingIntent = PendingIntent.getActivity(...);
```

### Common PendingIntent types

| Method | Use case |
|-------|----------|
| `PendingIntent.getActivity()` | Open an Activity |
| `PendingIntent.getBroadcast()` | Trigger a BroadcastReceiver |
| `PendingIntent.getService()` | Start a Service |

For most notification taps in LeafGuard AI, use `getActivity()`.

### Basic tap action example

```java
Intent openAppIntent = new Intent(context, MainActivity.class);
openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

PendingIntent contentPendingIntent = PendingIntent.getActivity(
        context,
        0,
        openAppIntent,
        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
);
```

Then attach it:

```java
builder.setContentIntent(contentPendingIntent);
```

### Why use `FLAG_UPDATE_CURRENT`

If a matching `PendingIntent` already exists, Android updates the extras with the newest intent data.

Useful when notification content changes.

### Mutability flags on API 31+

Starting with newer Android versions, you must think about whether a `PendingIntent` is mutable or immutable.

#### `FLAG_IMMUTABLE`
Use this when the `PendingIntent` should **not** be modified after creation.

This is preferred for most simple notification tap actions.

#### `FLAG_MUTABLE`
Use this only if the system or another component must modify it later.

For most course project use cases, **use `FLAG_IMMUTABLE`**.

### Why immutability matters

It improves security. If a PendingIntent is mutable without a real reason, another component might alter parts of it in dangerous ways.

### Recommended pattern for API 24+

```java
int flags = PendingIntent.FLAG_UPDATE_CURRENT;
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    flags |= PendingIntent.FLAG_IMMUTABLE;
}

PendingIntent pendingIntent = PendingIntent.getActivity(
        context,
        100,
        openAppIntent,
        flags
);
```

Although `FLAG_IMMUTABLE` becomes especially important in modern APIs, this version-safe pattern works well across a broad range of devices.

### Passing extras to Activity

Suppose user taps reminder and you want app to open scan screen:

```java
Intent openScanIntent = new Intent(context, MainActivity.class);
openScanIntent.putExtra("open_scan_tab", true);
```

Then in `MainActivity`:

```java
boolean openScanTab = getIntent().getBooleanExtra("open_scan_tab", false);
if (openScanTab) {
    // Navigate to scan UI
}
```

### Complete notification with tap action

```java
public void sendReminderNotification(Context context) {
    createReminderChannel(context);

    Intent openAppIntent = new Intent(context, MainActivity.class);
    openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    openAppIntent.putExtra("from_notification", true);

    int flags = PendingIntent.FLAG_UPDATE_CURRENT;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flags |= PendingIntent.FLAG_IMMUTABLE;
    }

    PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            101,
            openAppIntent,
            flags
    );

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_SCAN_REMINDERS)
            .setSmallIcon(R.drawable.ic_leaf_notification)
            .setContentTitle("LeafGuard Reminder")
            .setContentText("Tap to open LeafGuard AI and scan a plant.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

    NotificationManagerCompat.from(context).notify(2002, builder.build());
}
```

### Common viva questions about PendingIntent

**Q: Why can’t you pass a normal Intent directly to the notification?**

Because the system executes the action later, outside the immediate code flow. Android needs a `PendingIntent` token representing a future action.

**Q: Why use FLAG_IMMUTABLE?**

To prevent unwanted modification and improve security.

---

## 6. Scheduled Notifications with WorkManager and AlarmManager

LeafGuard AI reminders are more useful if they appear automatically, not only when the user presses a button.

### Two main approaches

1. **WorkManager** - recommended for most modern background work
2. **AlarmManager** - lower-level exact/alarm-style scheduling

### Why WorkManager is preferred

WorkManager is preferred because it:

- Survives app restarts better
- Works with system battery optimizations
- Provides constraints
- Is easier to manage
- Is recommended by Android Jetpack

For “remind me to scan plants every day”, **WorkManager** is usually the best answer.

### When AlarmManager is still useful

Use AlarmManager when:

- You need exact timing
- You are building alarm-clock-like behavior
- User expects precise moment delivery

For study purposes, know both. For implementation in this course project, prefer WorkManager.

### WorkManager dependency

```gradle
implementation 'androidx.work:work-runtime:2.9.0'
```

### ReminderWorker example

```java
public class ReminderWorker extends Worker {

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext());
        notificationHelper.sendReminderNotification(
                3001,
                "LeafGuard Reminder",
                "Time to scan your plants and check for disease symptoms."
        );
        return Result.success();
    }
}
```

### Scheduling daily reminder with PeriodicWorkRequest

```java
PeriodicWorkRequest reminderWorkRequest = new PeriodicWorkRequest.Builder(
        ReminderWorker.class,
        24,
        TimeUnit.HOURS
).build();

WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "leafguard_daily_reminder",
        ExistingPeriodicWorkPolicy.UPDATE,
        reminderWorkRequest
);
```

### Why `enqueueUniquePeriodicWork()` is useful

It prevents duplicate repeating reminders if the user taps “Enable reminder” multiple times.

### Add input data to worker if needed

```java
Data inputData = new Data.Builder()
        .putString("title", "LeafGuard Reminder")
        .putString("message", "Inspect your plants today.")
        .build();
```

Then set it on request and read it inside worker.

### WorkManager constraints example

```java
Constraints constraints = new Constraints.Builder()
        .setRequiresBatteryNotLow(false)
        .build();
```

For local notifications, network is not required.

### AlarmManager basic example

```java
AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
Intent intent = new Intent(context, ReminderReceiver.class);

int flags = PendingIntent.FLAG_UPDATE_CURRENT;
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    flags |= PendingIntent.FLAG_IMMUTABLE;
}

PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 0, intent, flags);

if (alarmManager != null) {
    long triggerAtMillis = System.currentTimeMillis() + 60 * 60 * 1000;
    alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            AlarmManager.INTERVAL_DAY,
            alarmPendingIntent
    );
}
```

### ReminderReceiver example

```java
public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper helper = new NotificationHelper(context);
        helper.sendReminderNotification(
                3002,
                "LeafGuard Reminder",
                "Scheduled reminder: check your crops today."
        );
    }
}
```

### WorkManager vs AlarmManager comparison

| Feature | WorkManager | AlarmManager |
|--------|-------------|--------------|
| Best for general background tasks | Yes | Sometimes |
| Exact timing | Limited | Better |
| Battery-aware | Yes | Less managed |
| Recommended for periodic app reminders | Yes | Usually no |
| Requires BroadcastReceiver often | No | Yes |

### Best choice for LeafGuard AI

For a daily or periodic scan reminder:

- Use **WorkManager**
- Use **AlarmManager** only if your teacher specifically asks about exact alarms

### Important note about exact alarms on newer Android

Exact alarms have additional restrictions on newer Android versions. This is another reason WorkManager is safer for student projects.

### Suggested reminder feature flow

```text
User enables reminders in app
        ↓
App schedules unique periodic WorkManager task
        ↓
Worker runs periodically in background
        ↓
Worker creates notification channel if needed
        ↓
Worker posts “Time to scan plants” notification
        ↓
User taps notification and opens app
```

---

## 7. Android Share Intent System

### What is the share system?

Android's share system lets one app send content to another app using an `Intent`. Instead of coding direct support for WhatsApp, Gmail, Messenger, Bluetooth, and Drive one by one, you ask Android to show a chooser.

This is a powerful example of Android's component model.

### Basic idea

```text
LeafGuard AI
    ↓
Intent.ACTION_SEND
    ↓
Android Chooser
    ↓
User picks app (WhatsApp / Gmail / Drive / Notes / etc.)
    ↓
Selected app receives shared content
```

### Why this is useful in LeafGuard AI

A user might want to share:

- Disease name
- Confidence score
- Advice text
- A captured leaf image
- Location of the scan

### Main action used

```java
Intent.ACTION_SEND
```

For multiple files, Android also supports `ACTION_SEND_MULTIPLE`, but one result summary and one image are enough for this week.

### Key parts of a share intent

| Part | Purpose |
|------|---------|
| Action | Usually `Intent.ACTION_SEND` |
| MIME type | Describes kind of content |
| Extras | Text, subject, stream URI |
| Chooser | Lets user pick app safely |

### Common MIME types

| MIME Type | Meaning |
|----------|---------|
| `text/plain` | Plain text |
| `image/jpeg` | JPEG image |
| `image/png` | PNG image |
| `*/*` | Generic mixed type, less precise |

### Why chooser is recommended

Always prefer:

```java
startActivity(Intent.createChooser(shareIntent, "Share scan result"));
```

Why?

- Lets user choose app explicitly
- Prevents unintended default app behavior
- Improves UX
- Looks professional

### Share text example

```java
Intent shareIntent = new Intent(Intent.ACTION_SEND);
shareIntent.setType("text/plain");
shareIntent.putExtra(Intent.EXTRA_SUBJECT, "LeafGuard AI Scan Result");
shareIntent.putExtra(Intent.EXTRA_TEXT, "Disease: Tomato Early Blight\nConfidence: 92.4%");
startActivity(Intent.createChooser(shareIntent, "Share scan result"));
```

### Share image example overview

When sharing images, use:

- `Intent.ACTION_SEND`
- MIME type `image/jpeg`
- `Intent.EXTRA_STREAM`
- `FileProvider` generated content URI
- Temporary read permission flag

### Important security rule

Do **not** share a raw file path or `file://` URI on Android 7.0+.

Instead, use `FileProvider` and a `content://` URI.

We will study that in detail later.

### What happens behind the scenes

1. Your app creates implicit share intent.
2. Android looks for apps that can handle that MIME type.
3. Chooser shows compatible apps.
4. User selects one.
5. Selected app receives the content.

### Why this is an implicit intent

Because you do **not** specify exact package or class.

You just say:

- “I want to send text/plain”
- “Which installed apps can handle this?”

Android resolves it.

That is a classic CSE 2206 concept: **implicit intents**.

---

## 8. Sharing Text Results from LeafGuard AI

This is the easier sharing case and should be implemented before image sharing.

### What should be shared?

A good text summary might include:

- Disease name
- Confidence percentage
- Timestamp
- Optional treatment suggestion
- Optional location

Example output:

```text
LeafGuard AI Scan Result
Disease: Tomato Late Blight
Confidence: 91.6%
Recommended action: Remove infected leaves and isolate affected plants.
Location: 23.8103, 90.4125
```

### Java example

```java
public void shareScanText(Context context,
                          String disease,
                          double confidence,
                          String treatment,
                          Double latitude,
                          Double longitude) {

    StringBuilder builder = new StringBuilder();
    builder.append("LeafGuard AI Scan Result\n");
    builder.append("Disease: ").append(disease).append("\n");
    builder.append("Confidence: ").append(String.format(java.util.Locale.US, "%.1f%%", confidence * 100)).append("\n");
    builder.append("Treatment: ").append(treatment).append("\n");

    if (latitude != null && longitude != null) {
        builder.append("Location: ")
                .append(latitude)
                .append(", ")
                .append(longitude)
                .append("\n");
    }

    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "LeafGuard AI Scan Result");
    shareIntent.putExtra(Intent.EXTRA_TEXT, builder.toString());

    context.startActivity(Intent.createChooser(shareIntent, "Share scan result"));
}
```

### Why `text/plain`?

Because the payload is plain human-readable text. Many apps support this:

- Gmail
- WhatsApp
- Messages
- Notes
- Drive
- Bluetooth

### Common formatting tips

1. Keep first line meaningful.
2. Format confidence as percentage, not decimal.
3. Avoid too much raw debugging text.
4. Include line breaks for readability.
5. Handle null values gracefully.

### Null-safe text sharing example

```java
String diseaseText = disease != null ? disease : "Unknown";
String treatmentText = treatment != null ? treatment : "No treatment information available";
```

### Good UX pattern

Add a **Share Result** button on the Result screen.

When tapped:

1. Gather current scan data.
2. Build text summary.
3. Open chooser.

### Error handling

Usually the chooser works if at least one compatible app exists. Still, defensive coding is useful.

```java
if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
    context.startActivity(Intent.createChooser(shareIntent, "Share scan result"));
} else {
    Toast.makeText(context, "No app available to share text.", Toast.LENGTH_SHORT).show();
}
```

### Viva-friendly explanation

> I used an implicit share intent with ACTION_SEND and MIME type text/plain. I put the disease result in EXTRA_TEXT and opened a chooser so the user could send the result to any compatible app like Gmail or WhatsApp.

---

## 9. Sharing Images Safely with FileProvider

This is one of the most important topics in Week 10.

### Why image sharing is harder than text sharing

Text can be put directly in intent extras. An image is a file or binary resource, so the receiving app needs secure temporary access to it.

### Old insecure approach

Earlier Android apps sometimes shared `file://` URIs directly.

Example of what **not** to do:

```java
Uri uri = Uri.fromFile(imageFile);
```

On Android 7.0+ this causes `FileUriExposedException` in many cases.

### Why direct file URIs are not allowed

Because a raw file path exposes internal file structure and violates Android's secure file sharing model.

### Correct solution: FileProvider

`FileProvider` creates a secure `content://` URI that can be temporarily granted to another app.

### How FileProvider works

```text
Image file in your app storage
        ↓
FileProvider converts it to content:// URI
        ↓
Share Intent includes EXTRA_STREAM with URI
        ↓
FLAG_GRANT_READ_URI_PERMISSION gives temporary access
        ↓
Chosen external app can read the image safely
```

### Step 1: Declare provider in manifest

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

### Step 2: Create `res/xml/file_paths.xml`

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

### Why these paths matter

They tell FileProvider which internal directories may be shared.

If your image is saved in:

- `getCacheDir()/images/scan.jpg` → use `cache-path`
- `getFilesDir()/images/scan.jpg` → use `files-path`

### Step 3: Save image to allowed directory

```java
File imageDir = new File(context.getCacheDir(), "images");
if (!imageDir.exists()) {
    imageDir.mkdirs();
}

File imageFile = new File(imageDir, "scan_result.jpg");
```

### Step 4: Get content URI

```java
Uri imageUri = FileProvider.getUriForFile(
        context,
        context.getPackageName() + ".provider",
        imageFile
);
```

### Step 5: Build share intent

```java
Intent shareIntent = new Intent(Intent.ACTION_SEND);
shareIntent.setType("image/jpeg");
shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
startActivity(Intent.createChooser(shareIntent, "Share scan image"));
```

### Sharing image plus text together

You can include both:

```java
shareIntent.putExtra(Intent.EXTRA_TEXT, "LeafGuard AI result: Tomato Early Blight (92.4%)");
```

Some apps show both image and text, though support varies.

### Full utility example

```java
public void shareImageResult(Context context, Bitmap bitmap, String summaryText) {
    try {
        File imageDir = new File(context.getCacheDir(), "images");
        if (!imageDir.exists() && !imageDir.mkdirs()) {
            Toast.makeText(context, "Could not prepare share directory.", Toast.LENGTH_SHORT).show();
            return;
        }

        File imageFile = new File(imageDir, "leafguard_share.jpg");
        FileOutputStream fos = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
        fos.flush();
        fos.close();

        Uri imageUri = FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".provider",
                imageFile
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, summaryText);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(shareIntent, "Share scan result"));
        } else {
            Toast.makeText(context, "No app available to share image.", Toast.LENGTH_SHORT).show();
        }

    } catch (IOException e) {
        Toast.makeText(context, "Image sharing failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
```

### Common FileProvider mistakes

1. Missing provider in manifest
2. Wrong authority string
3. Missing `file_paths.xml`
4. File stored outside declared paths
5. Forgetting `FLAG_GRANT_READ_URI_PERMISSION`
6. Using `Uri.fromFile()` instead of `FileProvider.getUriForFile()`
7. Forgetting to create directory before writing file

### Why FileProvider is a common stumbling block

Because many students understand intents but not secure file exposure. The problem is not with share intent itself; it is with how external apps gain permission to open your internal file.

### Viva explanation

> On Android 7.0 and above I cannot share raw file URIs because of FileUriExposedException and security restrictions. I must use FileProvider to convert the app file into a content URI and then grant temporary read permission through the share intent.

---

## 10. Location Services Architecture

### Why location matters in LeafGuard AI

Location adds context to disease detection.

Examples:

- Which field had infected leaves?
- Which village or plot had repeated disease cases?
- Where was a particular tomato scan captured?

### Android location stack

In modern Android development, Google Play Services often provides the easiest high-level location API through **Fused Location Provider**.

### Main class

```java
FusedLocationProviderClient fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context);
```

### Why “fused”?

Because it combines multiple signals:

- GPS
- Wi-Fi
- Cell towers
- Sensors

This helps balance accuracy, battery, and availability.

### Key architecture pieces

| Class | Purpose |
|------|---------|
| `FusedLocationProviderClient` | Main API for getting device location |
| `LocationRequest` | Describes desired update parameters |
| `LocationCallback` | Receives continuous location updates |
| `Location` | Result object with latitude/longitude/etc. |
| `SettingsClient` | Checks if device settings (like GPS) are suitable |

### Basic data returned by Location

- Latitude
- Longitude
- Accuracy
- Time
- Altitude (sometimes)
- Bearing/speed (sometimes)

### Typical flow

```text
App checks permission
      ↓
App gets FusedLocationProviderClient
      ↓
App requests last known location OR live updates
      ↓
System returns Location object asynchronously
      ↓
App stores latitude/longitude with scan record
```

### Accuracy levels

Location requests can prioritize different goals.

Common choices (modern API wording may vary across library versions):

- High accuracy
- Balanced power accuracy
- Low power
- Passive

In older examples you may see:

```java
LocationRequest.PRIORITY_HIGH_ACCURACY
LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
```

### Which accuracy should LeafGuard AI use?

For attaching approximate farm location to a scan:

- `BALANCED_POWER_ACCURACY` is often enough.
- `HIGH_ACCURACY` can be used when precise field mapping is important.

### Battery trade-off

High accuracy may use GPS more heavily and consume more battery.

This matters in real-world mobile apps.

### Example LocationRequest

```java
LocationRequest locationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(10000)
        .setFastestInterval(5000);
```

For single scan save, continuous updates may be overkill. You often only need one good reading.

---

## 11. Location Permissions and Runtime Flow

Location is sensitive user data, so Android protects it with permissions.

### Main location permissions

| Permission | Meaning |
|-----------|---------|
| `ACCESS_COARSE_LOCATION` | Approximate location |
| `ACCESS_FINE_LOCATION` | Precise location |
| `ACCESS_BACKGROUND_LOCATION` | Location while app is not actively in use |

### Which one does LeafGuard AI need?

Usually:

- `ACCESS_FINE_LOCATION` if precise coordinates are needed for scan history
- `ACCESS_COARSE_LOCATION` if approximate location is enough

If the app only gets location while the user is actively scanning, **background location is usually not needed**.

### Manifest declarations

```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

Only add background location if you truly need it.

### Why background location is more sensitive

It allows tracking even when user is not actively using the app. Android and reviewers treat it more strictly.

For a student app, avoid requesting it unless there is a clear requirement.

### Runtime permission flow

This is extremely important.

#### Step 1: Check permission

```java
boolean fineGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED;
```

#### Step 2: If not granted, request permission

```java
ActivityCompat.requestPermissions(
        this,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
        REQUEST_LOCATION_PERMISSION
);
```

#### Step 3: Handle result

```java
@Override
public void onRequestPermissionsResult(int requestCode,
                                       @NonNull String[] permissions,
                                       @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == REQUEST_LOCATION_PERMISSION) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocationForScan();
        } else {
            Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
        }
    }
}
```

### Full check → request → handle flow

```java
private static final int REQUEST_LOCATION_PERMISSION = 501;

private void ensureLocationPermissionAndFetch() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
        getCurrentLocationForScan();
    } else {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERMISSION
        );
    }
}
```

### Why you cannot skip runtime permission

From Android 6.0+ many dangerous permissions are granted at runtime, not just install time.

Location is one of those dangerous permissions.

### Coarse vs fine location decision

| Scenario | Better choice |
|---------|---------------|
| User wants exact field coordinates | Fine |
| General area/city is enough | Coarse |
| Disease heat-map for learning demo | Coarse or Fine depending requirement |

### If you request both coarse and fine

Android may let the user choose approximate or precise access on newer versions. Your app must be prepared for reduced precision.

### Should LeafGuard AI ask for background location?

Usually no for this week.

Reason:

- App records scan location during active use
- It does not need continuous passive tracking
- Background location increases privacy risk and implementation complexity

### Explain it in viva

> I request location permission at runtime because location is dangerous user data. First I check whether ACCESS_FINE_LOCATION is already granted, and if not I request it. Only after permission is granted do I call the location API.

---

## 12. Getting a Single Location vs Continuous Updates

### Option 1: `getLastLocation()`

This asks for the most recent known location already cached by the system.

```java
fusedLocationProviderClient.getLastLocation()
        .addOnSuccessListener(location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
            }
        });
```

### Advantages of `getLastLocation()`

- Very easy
- Fast
- Battery efficient
- Good for quick attachment to a scan record

### Limitations of `getLastLocation()`

- May return `null`
- May be stale or old
- Not guaranteed to be current

### Good use case in LeafGuard AI

When the user saves a scan result and you just want a quick best-effort location.

### Option 2: `requestLocationUpdates()`

This asks for fresh or repeated location updates.

```java
LocationRequest locationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(10000)
        .setFastestInterval(5000);
```

Then:

```java
LocationCallback locationCallback = new LocationCallback() {
    @Override
    public void onLocationResult(LocationResult locationResult) {
        if (locationResult == null) {
            return;
        }

        Location location = locationResult.getLastLocation();
        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
        }
    }
};
```

And request updates:

```java
fusedLocationProviderClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
);
```

### When to stop updates

If you only need one fresh reading, stop after receiving it.

```java
fusedLocationProviderClient.removeLocationUpdates(locationCallback);
```

### `getLastLocation()` vs `requestLocationUpdates()` comparison

| Question | `getLastLocation()` | `requestLocationUpdates()` |
|---------|----------------------|----------------------------|
| Fast? | Yes | Slower |
| Battery friendly? | Yes | Less |
| May be null? | Yes | Less likely after updates |
| Fresh data? | Not guaranteed | Better |
| Best for one-time scan save? | Often yes | Only if freshness is critical |

### Practical strategy for LeafGuard AI

A strong design is:

1. First try `getLastLocation()`
2. If null or too old, request a fresh update
3. Save location with scan record
4. If still unavailable, save scan without location and show message

### Example hybrid approach

```java
public void fetchLocationForScan() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
        return;
    }

    fusedLocationProviderClient.getLastLocation()
            .addOnSuccessListener(location -> {
                if (location != null) {
                    saveScanWithCoordinates(location.getLatitude(), location.getLongitude());
                } else {
                    requestSingleFreshLocation();
                }
            })
            .addOnFailureListener(e -> requestSingleFreshLocation());
}
```

### Newer API: `getCurrentLocation()`

Depending on library version, you may also see `getCurrentLocation()` which is useful for a single fresh fix. Mentioning it in viva is a plus, but `getLastLocation()` and `requestLocationUpdates()` remain core concepts.

---

## 13. Main Thread Safety and Async Thinking

### Why main thread dangers exist

Android has a main thread (UI thread) responsible for:

- Drawing UI
- Handling touch events
- Updating views
- Running Activity lifecycle callbacks

If you block it for too long, the app feels frozen.

### Is location access synchronous?

No, location results are typically delivered asynchronously through listeners/callbacks.

### Why this matters

Even though requesting location is not like a long manual loop on the main thread, you still must be careful because:

1. Results arrive later, not instantly.
2. UI might have changed by then.
3. Activity may be destroyed.
4. Null results are possible.
5. If you do heavy work inside callbacks, the UI may stutter.

### Dangerous pattern

```java
Location location = null;
fusedLocationProviderClient.getLastLocation();
// Immediately trying to use location here is wrong.
```

This is wrong because the request is asynchronous.

### Correct thinking

Use the result **inside** the success listener or call another method from there.

```java
fusedLocationProviderClient.getLastLocation()
        .addOnSuccessListener(location -> {
            if (location != null) {
                updateUiWithLocation(location);
            }
        });
```

### Heavy work danger

Suppose inside `onSuccess()` you:

- Compress a large bitmap
- Save large files
- Run expensive database transformation
- Perform huge loops

That can still hurt UI responsiveness if done directly on the main thread.

### Better design

- Keep UI callbacks short
- Offload heavy work to background threads, WorkManager, or executors if needed
- Update UI only with small state changes

### Threading example

```java
fusedLocationProviderClient.getLastLocation()
        .addOnSuccessListener(location -> {
            if (location != null) {
                locationTextView.setText(location.getLatitude() + ", " + location.getLongitude());
            } else {
                locationTextView.setText("Location unavailable");
            }
        });
```

This is fine because setting one TextView is light work.

### Activity lifecycle risk

What if user leaves the screen before result arrives?

Potential issues:

- Null view references if cleanup already happened
- Updating wrong screen state
- Leaks if continuous updates not removed

### Defensive habits

1. Check activity is not finishing when needed.
2. Remove location updates in `onPause()` / `onStop()` if they are continuous.
3. Avoid storing Activity in long-lived helper objects.
4. Keep callbacks focused.

### WorkManager and notifications also involve asynchronous thinking

- Worker runs later in background
- Notification tap launches activity later
- Share chooser leaves your app context

Week 10 is heavily about event-driven programming.

---

## 14. Saving Location with Scan History in Room

If your app already uses Room for scan history, Week 10 extends the schema.

### Why save location in database?

Because the scan result should persist even after app closes.

Potential fields:

- disease name
- confidence
- image path
- scan timestamp
- latitude
- longitude

### Updated entity example

```java
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

### Why use `Double` not `double` for coordinates?

Because `Double` can be `null`.

That matters when:

- permission denied
- GPS off
- location unavailable

If you used primitive `double`, you would need fake values like `0.0`, which may be misleading.

### DAO example

```java
@Dao
public interface ScanDao {

    @Insert
    void insert(ScanEntity scanEntity);

    @Query("SELECT * FROM scan_history ORDER BY scannedAt DESC")
    List<ScanEntity> getAllScans();
}
```

### Migration concern

If you add new columns to existing Room database, you must:

- increase database version
- provide migration

Example SQL migration:

```java
static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL("ALTER TABLE scan_history ADD COLUMN latitude REAL");
        database.execSQL("ALTER TABLE scan_history ADD COLUMN longitude REAL");
    }
};
```

### Database class update

```java
@Database(entities = {ScanEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScanDao scanDao();
}
```

Then build with migration:

```java
AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "leafguard.db")
        .addMigrations(MIGRATION_1_2)
        .build();
```

### Saving a scan with optional location

```java
private void saveScan(String diseaseName,
                      double confidence,
                      String imagePath,
                      Double latitude,
                      Double longitude) {
    ScanEntity entity = new ScanEntity(
            diseaseName,
            confidence,
            imagePath,
            System.currentTimeMillis(),
            latitude,
            longitude
    );

    Executors.newSingleThreadExecutor().execute(() -> {
        appDatabase.scanDao().insert(entity);
    });
}
```

### Why database work should not run on main thread

Room blocks main-thread database access by default unless explicitly allowed. That is good because database operations can be slow.

### UI display example

When reading history, show:

- disease name
- date/time
- confidence
- location if available
- “Location unavailable” otherwise

### Example binding logic

```java
if (scan.getLatitude() != null && scan.getLongitude() != null) {
    holder.locationTextView.setText(scan.getLatitude() + ", " + scan.getLongitude());
} else {
    holder.locationTextView.setText("Location unavailable");
}
```

### Architecture idea

```text
Scan completed
    ↓
Try to fetch current or last known location
    ↓
Create ScanEntity with result + optional coordinates
    ↓
Insert into Room database
    ↓
History screen can show where each scan was recorded
```

---

## 15. Error Handling and Defensive Programming

Week 10 features interact with Android services and external apps, so failure handling is essential.

### Notification errors to consider

1. Channel not created
2. Wrong channel ID
3. Small icon missing
4. Notification permission missing on API 33+
5. User disabled notifications for app/channel

### Defensive notification patterns

- Create channels before sending
- Use constants for channel IDs
- Check notification permission on newer APIs
- Use consistent IDs

### Share errors to consider

1. No compatible app installed
2. FileProvider authority mismatch
3. File not found
4. Image not saved successfully
5. Missing read URI permission

### Defensive share patterns

- Use chooser
- Check `resolveActivity()`
- Catch `IOException`
- Ensure file exists before sharing
- Use FileProvider, not raw file URI

### Location errors to consider

1. Permission denied
2. Location services turned off
3. `getLastLocation()` returns null
4. Accuracy is poor
5. Google Play services issues on some devices
6. Callback never returns in weak conditions

### Graceful permission denied behavior

If location is denied, do **not** crash or block entire scan flow unnecessarily.

Instead:

- Save scan without location
- Show short message like “Location not saved because permission was denied.”

### GPS off / settings disabled

You may use `SettingsClient` to check if location settings are enabled.

Conceptually:

```java
LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest);
```

If settings are unsuitable, prompt user to enable GPS.

### Example fallback logic

```java
private void handleLocationResult(@Nullable Location location) {
    if (location != null) {
        saveScanWithCoordinates(location.getLatitude(), location.getLongitude());
    } else {
        Toast.makeText(this, "Location unavailable. Saving scan without coordinates.", Toast.LENGTH_SHORT).show();
        saveScanWithCoordinates(null, null);
    }
}
```

### User-friendly messages

Bad:

> java.lang.IllegalStateException: Failed to resolve provider authority

Better:

> Could not share image. Please try again.

Bad:

> SecurityException: uid 10342 missing permission ACCESS_FINE_LOCATION

Better:

> Location permission is required to save scan coordinates.

### Error scenarios to test manually

1. Share text with no chosen app default
2. Share image after deleting cached file
3. Deny location permission
4. Turn GPS off
5. Run on API 26+ and verify channel settings exist
6. Schedule reminder and confirm notification appears later
7. Tap notification and ensure correct screen opens

### Logging suggestions

Use Logcat tags:

```java
Log.d("LeafGuardNotification", "Reminder notification sent");
Log.e("LeafGuardShare", "Image sharing failed", e);
Log.w("LeafGuardLocation", "Location permission denied by user");
```

### Defensive programming summary

- Never assume permission is granted.
- Never assume location is non-null.
- Never assume a file URI can be shared directly.
- Never assume a notification will work without a channel on API 26+.
- Never assume background timing is exact when using WorkManager.

---

## 16. Best Practices for Production-Ready Features

### Notifications best practices

1. Use meaningful channel names.
2. Avoid spam.
3. Use `IMPORTANCE_HIGH` only when justified.
4. Keep text concise and actionable.
5. Use `setAutoCancel(true)` for user-tapped notifications.
6. Use stable IDs if updating reminders.

### Share feature best practices

1. Start with text sharing first.
2. Use chooser for implicit sharing.
3. Use precise MIME type.
4. Prefer FileProvider for all file sharing.
5. Save temporary share files in cache directory if they are short-lived.

### Location best practices

1. Ask permission only when needed.
2. Explain why location improves the feature.
3. Avoid background location unless necessary.
4. Prefer best-effort one-time fetch for scan saving.
5. Respect denial and continue gracefully.
6. Remove location updates when done.

### Data privacy best practices

Location is sensitive. Good student apps should:

- Request only necessary permissions
- Tell users why location is being collected
- Store only what is useful
- Avoid secret tracking behavior

### Architecture best practices

A clean Week 10 design might include:

- `NotificationHelper`
- `ShareHelper`
- `LocationHelper`
- `ScanEntity` updated with coordinates
- UI logic kept slim in Activity

### Suggested package organization

```text
com.example.leafguardai
├── data
│   ├── ScanEntity.java
│   ├── ScanDao.java
│   └── AppDatabase.java
├── helpers
│   ├── NotificationHelper.java
│   ├── ShareHelper.java
│   └── LocationHelper.java
├── workers
│   └── ReminderWorker.java
└── ui
    ├── MainActivity.java
    └── ResultActivity.java
```

This is not mandatory, but it shows good separation of concerns.

---

## 17. Week 10 CSE 2206 Viva Preparation Q&A

### Q1. What is a notification channel and why is it required?

A notification channel is a user-visible category of notifications introduced in Android 8.0. It is required so users can control sound, importance, and interruption settings per category such as reminders or alerts. If a notification is posted on API 26+ without a valid channel, it may not appear.

### Q2. What is the difference between notification importance and priority?

Importance is mainly for notification channels on Android 8.0+, while priority affects behavior on older Android versions through `NotificationCompat.Builder`. In practice, apps usually set both so behavior is reasonable across device versions.

### Q3. Why do we use PendingIntent in notifications?

Because the notification tap happens later and is executed by the Android system. A PendingIntent safely packages an action for future execution, such as opening `MainActivity`.

### Q4. Why is `FLAG_IMMUTABLE` recommended?

It prevents the PendingIntent from being modified after creation, improving security. Most simple notification tap actions do not need mutation.

### Q5. Why is WorkManager preferred over AlarmManager for scan reminders?

WorkManager is battery-aware, reliable, lifecycle-friendly, and recommended for periodic background work. AlarmManager is lower-level and more suitable when exact alarm timing is required.

### Q6. What does `Intent.ACTION_SEND` do?

It creates an implicit intent asking Android to find apps capable of receiving shared content. The actual receiving app is chosen by the user.

### Q7. Why do we use a chooser with share intents?

A chooser explicitly shows the user available apps and avoids silent use of a default app. It improves user control and experience.

### Q8. What MIME type is used for sharing plain scan text?

`text/plain`.

### Q9. What MIME type is commonly used for JPEG scan image sharing?

`image/jpeg`.

### Q10. Why can’t we share `file://` URIs on Android 7.0+?

Because Android blocks insecure raw file URI exposure between apps. Doing so may throw `FileUriExposedException`.

### Q11. What is FileProvider?

FileProvider is a secure Android component that converts app-private files into temporary `content://` URIs that can be safely shared with other apps.

### Q12. What is FusedLocationProviderClient?

It is Google Play Services’ high-level location API that combines signals from GPS, Wi-Fi, cell towers, and sensors to provide location data efficiently.

### Q13. What is the difference between coarse and fine location?

Coarse location gives approximate position, while fine location gives more precise coordinates. Fine location is used when exact scan location is needed.

### Q14. Why must location permission be requested at runtime?

Because location is a dangerous permission in Android. Declaring it in the manifest is not enough on modern Android versions.

### Q15. What is the difference between `getLastLocation()` and `requestLocationUpdates()`?

`getLastLocation()` returns a cached last-known value and is quick but may be null or stale. `requestLocationUpdates()` actively listens for new readings and is better when fresher data is needed.

### Q16. Why can `getLastLocation()` return null?

Because the system may not have a recent cached location, especially after reboot, with GPS off, or if location was never recently requested by any app.

### Q17. How would you save location in Room?

I would add nullable latitude and longitude fields to the Room entity, update the database version, create a migration if necessary, and insert scan records with coordinates when available.

### Q18. What should the app do if permission is denied?

It should handle denial gracefully, explain the limitation if needed, and continue core functionality where possible, such as saving the scan without location.

### Q19. Why should background location usually be avoided in this project?

Because the app mainly needs location while the user is scanning. Background location is more privacy-sensitive and adds unnecessary complexity.

### Q20. Why is Week 10 important for Android application design?

Because it teaches how an app interacts with system-level features like background work, intents, permissions, secure file sharing, and location-aware data storage. These are core real-world Android development skills.

---

## 18. Key Takeaways

- Notifications must use channels on API 26+.
- `NotificationCompat.Builder` is the standard way to build notifications across versions.
- A valid small icon and correct channel ID are essential.
- Notification taps require a `PendingIntent`, usually with `FLAG_IMMUTABLE`.
- WorkManager is preferred over AlarmManager for routine reminder scheduling.
- Share intents use `Intent.ACTION_SEND` and appropriate MIME types.
- Text sharing is simple with `text/plain`.
- Image sharing requires `FileProvider` and a secure `content://` URI.
- `FusedLocationProviderClient` is the main location API used in many Android apps.
- Location permissions must be handled at runtime.
- `getLastLocation()` is fast but may be null; `requestLocationUpdates()` is more active and current.
- Room entities can store nullable `latitude` and `longitude` fields.
- Good Android apps always handle notification, sharing, and location failure cases gracefully.

---

## Final Week 10 Reminder

Before moving on, make sure you can explain all three of these in your own words:

1. How a notification channel differs from a notification itself
2. Why FileProvider is needed for image sharing
3. Why location permission must be checked before reading coordinates

If you can explain those clearly and implement them in Java, you are in strong shape for Week 10 and for CSE 2206 viva discussion.



<!-- NAV_FOOTER_START -->

---

## 📚 Week 10 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| **2** | **learning-notes.md** ← *You are here* | **Theory & Learning Notes** |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Week Overview & Objectives](README.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Theory & Learning Notes** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Practice Exercises →](exercises.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 09: TensorFlow Lite Offline AI](../week-09-tensorflow-lite-offline-ai/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 11: Testing, Debugging & Performance ➡](../week-11-testing-debugging-performance/README.md) |

---
