# Week 02 Interactive Notebook

## Building LeafGuard AI's User Interface

> This README acts like a Markdown notebook for CSE 2206. Read one cell at a time, run the code, and write your own notes after each checkpoint.

### How to use this notebook

- Follow the cells in order.
- Run code blocks in Android Studio, Terminal, or a Python shell as indicated.
- Keep LeafGuard AI open in Android Studio while you work.
- Save screenshots for your evidence folder after each big milestone.
- Use Java for Android code in this repository. Do not switch to Kotlin.

### Weekly outcomes

- Trace the Activity lifecycle with log messages.
- Build a Java `MainActivity` with Camera, Gallery, History, and Disease Library actions.
- Navigate between activities using intents.
- Apply Material Design styling, an icon, and a splash theme.

### Repository references

- `android-app/app/src/main/AndroidManifest.xml`
- `roadmap/week-02-android-basics-ui/`
- `solutions/week-02/`

---

## Notebook Cell 1 — Observe the Activity lifecycle

### Explanation

- Lifecycle methods tell you when an Activity is created, started, resumed, paused, stopped, or destroyed.
- Logging lifecycle transitions helps you understand rotation, app switching, and back navigation.

### Code to Read / Run

```java
package com.leafguard;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class LifecycleDemoActivity extends AppCompatActivity {
    private static final String TAG = "LeafGuardLifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }
}
```

### 🔵 Try This

- Run the app, press Home, then reopen it and watch Logcat.
- Rotate the emulator and note which lifecycle methods re-run.

### Expected Output

- Logcat shows lifecycle messages in order.

### ✅ Checkpoint

- Can you explain when `onPause` happens compared with `onStop`?

### ⚠️ Common Mistake

- Do not put expensive work directly in `onResume` unless necessary.

### 📌 Key Point

- Lifecycle awareness is essential before you touch camera, networking, or location.

## Notebook Cell 2 — Create the main layout step by step

### Explanation

- The main screen should clearly present the four core user actions of LeafGuard AI.
- ConstraintLayout keeps the design responsive across different screen sizes.

### Code to Read / Run

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="24dp">

    <TextView
        android:id="@+id/titleText"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="LeafGuard AI"
        android:textAlignment="center"
        android:textSize="28sp"
        android:textStyle="bold"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/cameraButton"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginTop="32dp"
        android:text="Camera"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/titleText" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/galleryButton"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="Gallery"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/cameraButton" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/historyButton"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="History"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/galleryButton" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/libraryButton"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="Disease Library"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/historyButton" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

### 🔵 Try This

- Change button text to sentence case or uppercase and see which feels more professional.
- Add a subtitle below the title describing the app as a plant disease detector.

### Expected Output

- You see four stacked Material buttons centered on screen.

### ✅ Checkpoint

- Why is it better to define these buttons in XML instead of creating them entirely in Java?

### ⚠️ Common Mistake

- Do not hardcode every color inline if you can move them to `colors.xml`.

### 📌 Key Point

- XML describes the visual structure while Java handles behavior.

## Notebook Cell 3 — Add navigation between activities

### Explanation

- Intents are Android messages used to move from one Activity to another.
- Even placeholder screens are useful because they let you test navigation early.

### Code to Read / Run

```java
Intent cameraIntent = new Intent(MainActivity.this, ScanActivity.class);
startActivity(cameraIntent);

Intent galleryIntent = new Intent(MainActivity.this, GalleryActivity.class);
startActivity(galleryIntent);

Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
startActivity(historyIntent);

Intent libraryIntent = new Intent(MainActivity.this, DiseaseLibraryActivity.class);
startActivity(libraryIntent);
```

### 🔵 Try This

- Create empty placeholder activities for all four destinations.
- Add each activity to the manifest and test the navigation flow.

### Expected Output

- Tapping each button opens a different screen without crashing.

### ✅ Checkpoint

- Can you explain what data could later be passed through these intents?

### ⚠️ Common Mistake

- If Android cannot find the target Activity, check the manifest declaration.

### 📌 Key Point

- Navigation testing can happen before the real business logic is finished.

## Notebook Cell 4 — Use Material Design styling

### Explanation

- Material Design helps the app feel modern and consistent.
- A small number of well-chosen brand colors is better than many unrelated colors.

### Code to Read / Run

```xml
<resources>
    <color name="leaf_primary">#2E7D32</color>
    <color name="leaf_primary_dark">#1B5E20</color>
    <color name="leaf_accent">#81C784</color>
    <color name="white">#FFFFFF</color>
</resources>
```

### 🔵 Try This

- Set the status bar and primary theme colors to a plant-friendly palette.

### Expected Output

- The app feels consistent instead of using default random colors.

### ✅ Checkpoint

- Why should a disease detection app use readable contrast and clear spacing?

### ⚠️ Common Mistake

- Avoid low-contrast text because it hurts accessibility and demo quality.

### 📌 Key Point

- Good UI is part of engineering quality, not just decoration.

## Notebook Cell 5 — Add app icon and splash screen

### Explanation

- A launcher icon and splash screen make the project feel like a complete product instead of a classroom prototype.

### Code to Read / Run

```xml
<style name="Theme.LeafGuardAI.Splash" parent="Theme.SplashScreen">
    <item name="windowSplashScreenBackground">@color/leaf_primary</item>
    <item name="windowSplashScreenAnimatedIcon">@mipmap/ic_launcher</item>
    <item name="postSplashScreenTheme">@style/Theme.LeafGuardAI</item>
</style>
```

### 🔵 Try This

- Generate a simple plant-themed launcher icon with Android Studio Image Asset.

### Expected Output

- The app shows a branded splash theme briefly on startup.

### ✅ Checkpoint

- Can you explain the difference between the launcher icon and the splash theme?

### ⚠️ Common Mistake

- Do not put a very detailed image into the app icon; it becomes unreadable at small size.

### 📌 Key Point

- Small branding details make your Week 12 demo stronger.

## Notebook Cell 6 — Use the complete MainActivity.java

### Explanation

- This class wires the four buttons to their destinations and keeps the code readable.
- The example is intentionally simple and uses Java only.

### Code to Read / Run

```java
package com.leafguard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton cameraButton;
    private MaterialButton galleryButton;
    private MaterialButton historyButton;
    private MaterialButton libraryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraButton = findViewById(R.id.cameraButton);
        galleryButton = findViewById(R.id.galleryButton);
        historyButton = findViewById(R.id.historyButton);
        libraryButton = findViewById(R.id.libraryButton);

        cameraButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
        });

        galleryButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivity(intent);
        });

        historyButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        libraryButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DiseaseLibraryActivity.class);
            startActivity(intent);
        });
    }
}
```

### 🔵 Try This

- Replace lambda expressions with anonymous inner classes if your lecturer wants to compare styles.

### Expected Output

- The main screen launches every placeholder activity correctly.

### ✅ Checkpoint

- Can you explain why button lookup happens in `onCreate`?

### ⚠️ Common Mistake

- If a button is null, check whether the view ID in XML matches the ID used in Java.

### 📌 Key Point

- Keep `MainActivity` focused on navigation instead of mixing in camera or networking code.

## Notebook Cell 7 — Validate the running UI

### Explanation

- Validation means more than a pretty screenshot; every button should do something predictable.

### Step-by-Step

1. Run the app on the emulator.
2. Tap Camera, Gallery, History, and Disease Library.
3. Press Back from each destination and return to the main menu.
4. Rotate the device once and make sure the UI still renders well.

### 🔵 Try This

- Write down one improvement you want to make to spacing, typography, or colors.

### Expected Output

- The app starts on MainActivity and all navigation works.

### ✅ Checkpoint

- Can you explain why testing orientation changes is useful even for a simple screen?

### ⚠️ Common Mistake

- Do not claim the screen is finished if you only tested one button.

### 📌 Key Point

- A stable home screen is the launch pad for all future LeafGuard AI features.

## Lab Reflection

- Write down one concept that felt easy.
- Write down one concept that felt confusing.
- Describe one bug you saw and how you fixed it.
- State which file changed the most during this notebook.
- Explain how this week supports the final LeafGuard AI submission.

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

## Next Step

- After this notebook, continue to **Week 03** and connect today's work to the next subsystem.
