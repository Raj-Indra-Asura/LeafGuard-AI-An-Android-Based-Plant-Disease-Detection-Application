# Week 02 Solution: Android UI Navigation Shell

This solution describes the expected Week 02 result. It is a reference after you have attempted the build task yourself.

Kotlin is the primary track. Java remains a secondary comparison track.

---

## 1. Expected Screen Flow

Week 02 should produce this flow:

```text
MainActivity (Home)
|-- opens ScanActivity placeholder
|-- opens ResultActivity placeholder
|-- opens HistoryActivity placeholder
|-- opens DiseaseLibraryActivity placeholder
`-- opens SettingsActivity placeholder
```

Optional:

```text
MainActivity
`-- opens AnalyticsActivity placeholder
```

No real camera, gallery picker, backend call, database write, XML parsing, or AI inference belongs in this solution.

---

## 2. Minimal `strings.xml`

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
    <string name="placeholder_scan">Image input will be added in Week 03.</string>
    <string name="placeholder_result">Real prediction results will be added after networking and model work.</string>
    <string name="placeholder_history">Saved scan history will be added in Week 07.</string>
    <string name="placeholder_library">The XML disease library will be added in Week 08.</string>
    <string name="placeholder_settings">Settings and About information live here.</string>
</resources>
```

Why this is correct:

- visible text is centralized
- placeholders are honest
- future behavior is not faked

---

## 3. Minimal Home Layout

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/home_title"
        android:textSize="28sp" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/home_subtitle" />

    <Button
        android:id="@+id/buttonOpenScan"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
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

Line meaning:

- `LinearLayout` stacks the screen content vertically.
- `TextView` displays text.
- `Button` gives the user an action.
- `@+id/...` creates an ID used by Kotlin.
- `@string/...` pulls text from `strings.xml`.

---

## 4. Minimal Placeholder Activity

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

Line meaning:

- `package com.leafguard` keeps the file in the app namespace.
- `ScanActivity` names the screen.
- `onCreate` runs when Android creates the screen.
- `setContentView` attaches `activity_scan.xml` to the screen.

Use the same pattern for Result, History, Disease Library, and Settings/About placeholders.

---

## 5. Minimal Home Navigation

Example `MainActivity.kt`:

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

Why this is correct:

- buttons are found by ID
- each click opens one destination
- no future behavior is mixed into Week 02

---

## 6. Manifest Entries

```xml
<activity android:name=".ScanActivity" android:exported="false" />
<activity android:name=".ResultActivity" android:exported="false" />
<activity android:name=".HistoryActivity" android:exported="false" />
<activity android:name=".DiseaseLibraryActivity" android:exported="false" />
<activity android:name=".SettingsActivity" android:exported="false" />

<activity android:name=".MainActivity" android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

Why this is correct:

- Android can open each Activity.
- `MainActivity` is the launcher.
- internal screens are not exported.

---

## 7. Validation Result

Week 02 passes when:

- app builds
- app launches
- Home opens each placeholder
- Back navigation does not crash
- evidence is saved
- the student can explain each code piece in beginner language

Week 03 begins only after this shell is stable.