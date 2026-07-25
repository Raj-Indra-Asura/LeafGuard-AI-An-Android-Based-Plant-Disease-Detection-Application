# Week 02 Build Task: Build the Android UI Navigation Shell

## Objective

Build the first runnable Android slice of LeafGuard AI from the Week 01 screen map.

By the end, the app should launch and navigate between placeholder screens. Each placeholder screen should clearly say which future week will add real behavior.

Estimated time: 8 to 10 hours.

---

## Before You Start

Confirm:

- [ ] Week 01 foundation package is complete.
- [ ] You can explain the product idea and main user journey.
- [ ] You have Android Studio or a working Gradle environment.
- [ ] You know this week does not implement camera, backend, database, XML parsing, or AI.

---

## Target Evidence Folder

Save Week 02 evidence in:

```text
docs/evidence/week-02/
```

Suggested structure:

```text
docs/evidence/week-02/
|-- screenshots/
|-- exercises/
|-- build-notes.md
|-- validation.md
|-- quiz-answers.md
`-- reflection-answers.md
```

---

## Step 1: Open or Create the Kotlin Android Project

Use the Kotlin primary track:

```text
android-app-kotlin/
```

If you are creating the project from scratch in Android Studio:

1. New Project
2. Empty Activity
3. Name: `LeafGuard AI`
4. Package name: `com.leafguard`
5. Language: Kotlin
6. Minimum SDK: API 24 or higher

If the project already exists in the repository, open it and treat this week as a learning rebuild/verification of the UI shell.

### Check

- [ ] Project opens.
- [ ] Gradle sync succeeds.
- [ ] App can build or run the default screen.

---

## Step 2: Understand the Files You Will Touch

For Week 02, focus on these file types:

| File Type | Example | Purpose |
|---|---|---|
| Activity Kotlin file | `MainActivity.kt` | Screen behavior and navigation. |
| Layout XML file | `activity_main.xml` | Screen appearance. |
| Strings file | `strings.xml` | Visible text. |
| Colors file | `colors.xml` | Reusable colors. |
| Manifest | `AndroidManifest.xml` | Declares screens. |

Do not edit networking, database, model, or backend files in Week 02.

---

## Step 3: Create Week 02 String Resources

In `app/src/main/res/values/strings.xml`, create beginner UI text.

Minimal example:

```xml
<resources>
    <string name="app_name">LeafGuard AI</string>
    <string name="home_title">LeafGuard AI</string>
    <string name="home_subtitle">Plant disease detection learning app</string>
    <string name="open_scan">Open Scan</string>
    <string name="open_result">Open Sample Result</string>
    <string name="open_history">Open History</string>
    <string name="open_library">Open Disease Library</string>
    <string name="open_settings">Open Settings</string>
    <string name="scan_title">Scan</string>
    <string name="result_title">Result</string>
    <string name="history_title">History</string>
    <string name="library_title">Disease Library</string>
    <string name="settings_title">Settings and About</string>
    <string name="placeholder_scan">Image input will be added in Week 03.</string>
    <string name="placeholder_result">Real prediction results will be added after networking and model work.</string>
    <string name="placeholder_history">Saved scan history will be added in Week 07.</string>
    <string name="placeholder_library">The XML disease library will be added in Week 08.</string>
    <string name="placeholder_settings">Course project shell. Settings options will grow in later weeks.</string>
</resources>
```

The finished Week 02 `strings.xml` holds 20 strings. The full file, and every other Week 02 file, is listed with exact code and line counts in [`learning-notes.md` section 10](learning-notes.md#10-end-of-week-02-file-inventory-exact-files-exact-code-exact-size).

### Why this code exists

- `string name="..."` gives a reusable name to visible text.
- Layouts can use `@string/home_title` instead of hardcoded text.
- Future edits become easier because text is centralized.

### Check

- [ ] App name exists.
- [ ] Button labels exist.
- [ ] Placeholder messages honestly defer future behavior.

---

## Step 4: Create Simple Colors

In `app/src/main/res/values/colors.xml`, define a small palette.

```xml
<resources>
    <color name="leaf_green">#2E7D32</color>
    <color name="leaf_green_dark">#1B5E20</color>
    <color name="screen_background">#F7FAF4</color>
    <color name="text_primary">#1F2933</color>
    <color name="text_secondary">#52606D</color>
    <color name="white">#FFFFFF</color>
</resources>
```

### Why this code exists

- Colors get names that describe meaning.
- Layouts avoid random hardcoded hex values.
- The app starts to feel like one product, not unrelated screens.

### Check

- [ ] Color XML has no syntax errors.
- [ ] At least one layout uses a named color.

---

## Step 5: Build the Home Layout

Create or update `res/layout/activity_main.xml`.

Starter layout:

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center_horizontal"
    android:padding="24dp"
    android:background="@color/screen_background">

    <TextView
        android:id="@+id/textHomeTitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/home_title"
        android:textColor="@color/leaf_green_dark"
        android:textSize="28sp" />

    <TextView
        android:id="@+id/textHomeSubtitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="@string/home_subtitle"
        android:textColor="@color/text_secondary"
        android:textSize="16sp" />

    <Button
        android:id="@+id/buttonOpenScan"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:text="@string/open_scan" />

    <Button
        android:id="@+id/buttonOpenResult"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/open_result" />

    <Button
        android:id="@+id/buttonOpenHistory"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/open_history" />

    <Button
        android:id="@+id/buttonOpenLibrary"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/open_library" />

    <Button
        android:id="@+id/buttonOpenSettings"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/open_settings" />
</LinearLayout>
```

### Why this code exists

- `LinearLayout` is simple for beginners and stacks content vertically.
- `TextView` shows readable text.
- `Button` gives a clear action.
- IDs such as `buttonOpenScan` allow Kotlin to attach click behavior.

### Check

- [ ] Home layout renders.
- [ ] Buttons are visible.
- [ ] Text uses string resources.

---

## Step 6: Create Placeholder Layouts

Create simple layouts for each placeholder screen.

Example `activity_scan.xml`:

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp"
    android:background="@color/screen_background">

    <TextView
        android:id="@+id/textScanTitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/scan_title"
        android:textColor="@color/text_primary"
        android:textSize="24sp" />

    <TextView
        android:id="@+id/textScanPlaceholder"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="12dp"
        android:text="@string/placeholder_scan"
        android:textColor="@color/text_secondary"
        android:textSize="16sp" />
</LinearLayout>
```

Create equivalent placeholder layouts for:

- `activity_result.xml`
- `activity_history.xml`
- `activity_disease_library.xml`
- `activity_settings.xml`

Optional:

- `activity_analytics.xml`

### Check

- [ ] Each placeholder screen has a title.
- [ ] Each placeholder screen states its future week or future purpose.
- [ ] No placeholder pretends the future feature already works.

---

## Step 7: Create Activity Classes

Create one Kotlin Activity for each required screen.

Example `ScanActivity.kt`:

```kotlin
package com.leafguard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ScanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
    }
}
```

### Why this code exists

- `package com.leafguard` matches the app package.
- `AppCompatActivity` gives normal Android screen behavior.
- `onCreate` runs when the screen opens.
- `setContentView` connects the Kotlin screen to its XML layout.

Create equivalent simple Activity classes for each placeholder screen.

### Check

- [ ] Every placeholder screen has a Kotlin Activity.
- [ ] Every Activity calls `setContentView`.
- [ ] Project builds after each new screen or after a small group of screens.

---

## Step 8: Register Activities in the Manifest

Add each screen to `AndroidManifest.xml`.

Example:

```xml
<activity
    android:name=".ScanActivity"
    android:exported="false" />
```

The launcher Activity should look like this:

```xml
<activity
    android:name=".MainActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

### Why this code exists

- Android cannot open a screen that is not declared.
- `MainActivity` is exported because the system launcher opens it.
- Internal screens are not exported because only this app should open them.

### Check

- [ ] All Week 02 Activities are declared.
- [ ] Only `MainActivity` has the launcher intent filter.
- [ ] Manifest has no merge errors.

---

## Step 9: Add Home Navigation

Update `MainActivity.kt` so buttons open placeholder screens.

```kotlin
package com.leafguard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.buttonOpenScan).setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        findViewById<Button>(R.id.buttonOpenResult).setOnClickListener {
            startActivity(Intent(this, ResultActivity::class.java))
        }

        findViewById<Button>(R.id.buttonOpenHistory).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        findViewById<Button>(R.id.buttonOpenLibrary).setOnClickListener {
            startActivity(Intent(this, DiseaseLibraryActivity::class.java))
        }

        findViewById<Button>(R.id.buttonOpenSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
```

### Why this code exists

- Each `findViewById` finds one button from `activity_main.xml`.
- Each `setOnClickListener` waits for a tap.
- Each `Intent` names the destination Activity.
- Each `startActivity` opens that destination.

### Check

- [ ] Every Home button opens the intended placeholder screen.
- [ ] Back button returns or exits safely.
- [ ] No button crashes the app.

---

## Step 10: Build, Run, and Capture Evidence

Run one of these:

```bash
cd android-app-kotlin
./gradlew assembleDebug
```

Or press Run in Android Studio.

Capture evidence:

- Home screen screenshot
- each placeholder screen screenshot
- build success screenshot or terminal output
- short note explaining what each placeholder will become in future weeks

---

## Milestone Demo

Demonstrate:

1. App launches.
2. Home screen appears.
3. Scan placeholder opens.
4. Result placeholder opens.
5. History placeholder opens.
6. Disease Library placeholder opens.
7. Settings/About placeholder opens.
8. Back navigation does not crash.
9. You can explain which future week fills each placeholder.

---

## Done Means

Week 02 is done when:

- the app builds
- the app launches
- placeholder screens exist
- Home navigation works
- evidence is saved
- validation checklist passes
- quiz and reflection are complete

Do not move to Week 03 until this is true.

<!-- NAV_FOOTER_START -->

---

## 📚 Week 02 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| **4** | **build-task.md** ← *You are here* | **Build Implementation Guide** |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Practice Exercises](exercises.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Build Implementation Guide** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Validation & Verification →](validation-checklist.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 01: Project Understanding](../week-01-project-understanding/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 03: Camera & Gallery ➡](../week-03-camera-gallery/README.md) |

---
