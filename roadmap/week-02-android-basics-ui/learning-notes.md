# Week 02 Learning Notes: Android UI Shell From Zero

## Purpose

Week 02 teaches the first Android concepts needed to turn the Week 01 screen map into a runnable app shell. These notes explain the ideas before code.

You are learning how to create screens, layouts, resources, and navigation. You are not learning camera, backend, database, XML parsing, or AI yet.

If you only want the answer to "what exactly will exist in my project when Week 02 ends?", jump to [section 10](#10-end-of-week-02-file-inventory-exact-files-exact-code-exact-size). It lists every file, its exact contents, its exact line count, and whether it is real or a placeholder.

---

## 1. How Week 02 Grows From Week 01

Week 01 produced these planning artifacts:

- product idea
- user journey
- screen map
- system sketch
- week growth map

Week 02 uses them like this:

| Week 01 Artifact | Week 02 Use |
|---|---|
| Product idea | App name, title, short subtitle |
| User journey | Home button choices and placeholder flow |
| Screen map | Activity list and layout files |
| System sketch | Decide which boxes are UI-only this week |
| Week growth map | Know what to defer to future weeks |

The key discipline is to build only the Week 02 slice.

---

## 2. What an Android Project Is

An Android project is a folder that contains everything needed to build the app.

Important parts:

```text
android-app-kotlin/
|-- app/
|   |-- src/main/
|   |   |-- java/com/leafguard/       Kotlin source files live here
|   |   |-- res/layout/               XML screen layouts
|   |   |-- res/values/               strings, colors, themes
|   |   `-- AndroidManifest.xml       app screen declarations
|   `-- build.gradle                  app dependencies and SDK settings
|-- build.gradle                      project-level Gradle settings
`-- settings.gradle                   project name and modules
```

Beginner rule:

> Kotlin files describe behavior. XML layout files describe appearance. Resource files store reusable text, colors, and themes. The manifest tells Android which screens exist.

---

## 3. Activity: One Screen

An Activity is one Android screen.

In Week 02, each Activity has a simple job:

1. Load its XML layout.
2. Set button click listeners.
3. Navigate to another screen if needed.

Minimal Kotlin Activity:

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

Line-by-line meaning:

| Code | Meaning |
|---|---|
| `class MainActivity : AppCompatActivity()` | Create a screen named `MainActivity` using Android's Activity behavior. |
| `onCreate(...)` | This function runs when the screen is first created. |
| `super.onCreate(...)` | Let Android do its required setup before our code. |
| `setContentView(...)` | Attach an XML layout file to this screen. |
| `R.layout.activity_main` | Android's generated name for `res/layout/activity_main.xml`. |

Week 02 mainly uses `onCreate`. You will observe more lifecycle behavior later, but you do not need to master all lifecycle methods now.

---

## 4. XML Layout: What the Screen Looks Like

An XML layout is a text file that describes visible UI.

Minimal layout example:

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:id="@+id/textTitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/app_name" />

    <Button
        android:id="@+id/buttonOpenScan"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/open_scan" />
</LinearLayout>
```

Line-by-line meaning:

| XML | Meaning |
|---|---|
| `LinearLayout` | A simple container that arranges child views in a row or column. |
| `layout_width="match_parent"` | Use the full width of the parent. |
| `layout_height="match_parent"` | Use the full height of the parent. |
| `orientation="vertical"` | Stack child views from top to bottom. |
| `padding="24dp"` | Add inner space around content. |
| `TextView` | A view that displays text. |
| `@+id/textTitle` | Create an ID so Kotlin can find this view if needed. |
| `@string/app_name` | Use text from `strings.xml` instead of hardcoding. |
| `Button` | A tappable view. |

Week 02 can use simple `LinearLayout` or `ConstraintLayout`. Use the simpler layout first; learn constraints gradually.

---

## 5. Resources: Avoid Hardcoding Text

Put user-visible text in `res/values/strings.xml`.

Example:

```xml
<resources>
    <string name="app_name">LeafGuard AI</string>
    <string name="open_scan">Open Scan</string>
</resources>
```

Why this matters:

- text is easier to edit
- translation is easier later
- layouts stay cleaner
- validation can check that UI text is organized

Colors work the same way in `colors.xml`:

```xml
<resources>
    <color name="leaf_green">#2E7D32</color>
    <color name="screen_background">#F7FAF4</color>
</resources>
```

Week 02 validation should check resources. It should not check model labels, API responses, or database tables.

---

## 6. Intent: Opening Another Screen

An Intent is a message that asks Android to do something. In Week 02, you use explicit Intents to open your own screens.

Example:

```kotlin
val intent = Intent(this, ScanActivity::class.java)
startActivity(intent)
```

Line-by-line meaning:

| Code | Meaning |
|---|---|
| `Intent(this, ScanActivity::class.java)` | Create a request to open `ScanActivity`. |
| `this` | The current Activity context. |
| `ScanActivity::class.java` | The destination screen class. |
| `startActivity(intent)` | Ask Android to open the destination screen. |

Button example:

```kotlin
findViewById<Button>(R.id.buttonOpenScan).setOnClickListener {
    startActivity(Intent(this, ScanActivity::class.java))
}
```

Line-by-line meaning:

| Code | Meaning |
|---|---|
| `findViewById<Button>(...)` | Find the Button from the XML layout by ID. |
| `setOnClickListener { ... }` | Run this block when the user taps the button. |
| `startActivity(...)` | Open the next screen. |

Week 02 uses navigation only. Passing real scan data comes later.

---

## 7. Manifest: Register Screens

Android needs to know which Activity classes exist. That happens in `AndroidManifest.xml`.

Example:

```xml
<activity
    android:name=".ScanActivity"
    android:exported="false" />
```

Meaning:

| XML | Meaning |
|---|---|
| `android:name=".ScanActivity"` | Register the `ScanActivity` class in the app package. |
| `android:exported="false"` | Other apps cannot launch this screen directly. |

The launcher screen needs an intent filter:

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

Meaning:

| XML | Meaning |
|---|---|
| `MAIN` | This is the app's starting entry point. |
| `LAUNCHER` | Show this app in the device launcher. |
| `exported="true"` | Required because the launcher is outside your app. |

---

## 8. Gradle: Build Settings and Dependencies

Gradle builds the app.

For Week 02, understand these settings:

| Setting | Meaning |
|---|---|
| `applicationId` | Unique app ID installed on the device. |
| `minSdk` | Oldest Android version supported. |
| `targetSdk` | Android behavior version the app targets. |
| `implementation` | Adds a library dependency. |
| `viewBinding` | Optional helper that makes views easier to access. |

Do not add Retrofit, Room, TensorFlow Lite, or WorkManager just because they exist in the final product. Add dependencies when the relevant week teaches them.

---

## 9. What Week 02 Code Should Look Like

Week 02 code should be simple:

- Activity classes load layouts.
- Home buttons navigate to placeholder screens.
- Placeholder screens explain what will be built later.
- No fake camera implementation.
- No fake backend implementation.
- No fake database implementation.

Good placeholder text:

```text
Scan screen
Image input will be added in Week 03.
```

Bad placeholder behavior:

```text
Pretend detection succeeded and save fake scan history.
```

Fake feature behavior makes future weeks confusing. Keep placeholders honest.

---

## 10. End-of-Week-02 File Inventory (Exact Files, Exact Code, Exact Size)

This section is the authoritative answer to one question:

> When Week 02 is finished, exactly which files exist, exactly what is inside each of them, how big is each one, and which parts are only placeholders?

Everything below describes the **Kotlin primary track** (`android-app-kotlin/`). The Java twin (`android-app/`) mirrors the same file set with `.java` Activity files; see section 10.9.

---

### 10.1 File Count Summary

| Group | Files | Hand-typed? | Total lines |
|---|---:|---|---:|
| A. Gradle build scaffold | 9 | Mostly generated, 4 reviewed/edited | ~61 hand-relevant |
| B. Manifest | 1 | Yes | 37 |
| C. Kotlin Activities | 6 required (+1 optional) | Yes | 34 + (5 x 12) = 94 |
| D. XML layouts | 6 required (+1 optional) | Yes | 56 + (5 x 25) = 181 |
| E. Value resources | 3 | Yes | 43 |
| F. Evidence documents | 4+ | Yes (Markdown, not code) | varies |

**Week 02 required Android source/resource files: 16.**
(1 manifest + 6 Activities + 6 layouts + 3 value files)

**Week 02 required hand-written Android code: about 355 lines** (94 Kotlin + 218 layout/values XML + 37 manifest, rounded because your text may wrap differently).

Optional extras (`AnalyticsActivity.kt` + `activity_analytics.xml`) add 2 files and about 37 lines if your Week 01 screen map included an Analytics screen.

Anything the final product contains that is **not** in this list (Retrofit, Room, TensorFlow Lite, `diseases.xml`, `labels.txt`, `network_security_config.xml`, `file_provider_paths.xml`, RecyclerView item layouts, drawables, bottom-navigation menu) must **not** exist yet at the end of Week 02. See section 10.8.

---

### 10.2 The Exact Week 02 Tree

```text
android-app-kotlin/
|-- settings.gradle                                   [A] 17 lines   edited once
|-- build.gradle                                      [A]  4 lines   edited once
|-- gradle.properties                                 [A]  3 lines   generated
|-- gradlew                                           [A] generated  never edited
|-- gradlew.bat                                       [A] generated  never edited
|-- gradle/wrapper/gradle-wrapper.jar                 [A] binary     never edited
|-- gradle/wrapper/gradle-wrapper.properties          [A] generated  never edited
`-- app/
    |-- build.gradle                                  [A] 40 lines   edited by you
    |-- proguard-rules.pro                            [A] generated  untouched in Week 02
    `-- src/main/
        |-- AndroidManifest.xml                       [B] 37 lines   edited by you
        |-- java/com/leafguard/
        |   |-- MainActivity.kt                       [C] 34 lines   REAL navigation
        |   |-- ScanActivity.kt                       [C] 12 lines   PLACEHOLDER
        |   |-- ResultActivity.kt                     [C] 12 lines   PLACEHOLDER
        |   |-- HistoryActivity.kt                    [C] 12 lines   PLACEHOLDER
        |   |-- DiseaseLibraryActivity.kt             [C] 12 lines   PLACEHOLDER
        |   |-- SettingsActivity.kt                   [C] 12 lines   PLACEHOLDER
        |   `-- AnalyticsActivity.kt                  [C] 12 lines   OPTIONAL PLACEHOLDER
        `-- res/
            |-- layout/
            |   |-- activity_main.xml                 [D] 56 lines   REAL UI
            |   |-- activity_scan.xml                 [D] 25 lines   PLACEHOLDER UI
            |   |-- activity_result.xml               [D] 25 lines   PLACEHOLDER UI
            |   |-- activity_history.xml              [D] 25 lines   PLACEHOLDER UI
            |   |-- activity_disease_library.xml      [D] 25 lines   PLACEHOLDER UI
            |   |-- activity_settings.xml             [D] 25 lines   PLACEHOLDER UI
            |   `-- activity_analytics.xml            [D] 25 lines   OPTIONAL PLACEHOLDER
            `-- values/
                |-- strings.xml                       [E] 25 lines   REAL
                |-- colors.xml                        [E]  9 lines   REAL
                `-- themes.xml                        [E]  9 lines   REAL
```

Legend:

| Marker | Meaning |
|---|---|
| REAL | The file does its final job for this week; behavior is genuine, not simulated. |
| PLACEHOLDER | The file exists, compiles, opens, and honestly says which future week fills it. |
| OPTIONAL | Create it only if your Week 01 screen map listed that screen. |
| generated | Android Studio wrote it; you read it but do not need to change it in Week 02. |

---

### 10.3 Group A: The Gradle Build Scaffold (9 files)

These files answer "how is the app compiled?". Android Studio creates them when you make the project. Week 02 only requires you to **read them and keep them minimal**.

#### A1. `settings.gradle` — 17 lines

```groovy
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LeafGuardAI-Kotlin"
include ':app'
```

| Code | What it does |
|---|---|
| `pluginManagement { repositories { ... } }` | Tells Gradle where to download the Android and Kotlin build plugins. |
| `dependencyResolutionManagement` | Central place that decides where libraries come from. |
| `FAIL_ON_PROJECT_REPOS` | Stops a module from silently adding its own download source. |
| `rootProject.name` | The project name shown in Android Studio. |
| `include ':app'` | Declares that the project contains one module named `app`. |

#### A2. `build.gradle` (project root) — 4 lines

```groovy
plugins {
    id 'com.android.application' version '8.2.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.22' apply false
}
```

| Code | What it does |
|---|---|
| `com.android.application` | Declares the Android Gradle Plugin version used by every module. |
| `org.jetbrains.kotlin.android` | Declares the Kotlin plugin version. |
| `apply false` | Version is declared here, but the plugin is actually switched on inside `app/build.gradle`. |

#### A3. `gradle.properties` — 3 lines

```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
kotlin.code.style=official
```

| Line | What it does |
|---|---|
| `org.gradle.jvmargs` | Memory and text encoding for the build process. |
| `android.useAndroidX=true` | Use modern AndroidX libraries. Required by `appcompat`. |
| `kotlin.code.style=official` | Use standard Kotlin formatting. |

#### A4. `app/build.gradle` — 40 lines (the only build file you really shape in Week 02)

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace 'com.leafguard'
    compileSdk 34

    defaultConfig {
        applicationId "com.leafguard"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "0.1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
}
```

| Code | What it does |
|---|---|
| `namespace 'com.leafguard'` | Package used for the generated `R` class and `BuildConfig`. |
| `compileSdk 34` | Compile against Android 14 APIs. |
| `applicationId "com.leafguard"` | The unique ID of the installed app on a device. |
| `minSdk 24` | Runs on Android 7.0 and newer. |
| `targetSdk 34` | Opt in to Android 14 runtime behavior. |
| `versionCode 1` / `versionName "0.1.0"` | First build of the shell. |
| `buildTypes { release { ... } }` | Release settings; Week 02 keeps shrinking disabled so builds stay simple. |
| `compileOptions` / `kotlinOptions` | Compile Java and Kotlin against Java 11 bytecode. |
| `androidx.core:core-ktx` | Kotlin conveniences for Android APIs. |
| `androidx.appcompat` | Provides `AppCompatActivity`, the base class of every Week 02 screen. |
| `material` | Provides the Material theme parent used in `themes.xml`. |
| `constraintlayout` | Available for layouts, even though Week 02 uses `LinearLayout` for readability. |

**Exactly four dependencies.** If you see Retrofit, Room, or TensorFlow Lite lines here at the end of Week 02, you jumped ahead.

#### A5–A9. `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar`, `gradle/wrapper/gradle-wrapper.properties`, `app/proguard-rules.pro`

These are generated. You must not hand-edit them in Week 02.

| File | What it does | Week 02 status |
|---|---|---|
| `gradlew` / `gradlew.bat` | Scripts that run the correct Gradle version on macOS/Linux and Windows. | untouched |
| `gradle-wrapper.jar` | Small binary that downloads and starts Gradle. | untouched |
| `gradle-wrapper.properties` | Names the Gradle version to download. | untouched |
| `proguard-rules.pro` | Rules for code shrinking in release builds. | empty/comment-only, untouched |

---

### 10.4 Group B: `AndroidManifest.xml` (1 file, 37 lines)

Path: `app/src/main/AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:allowBackup="true"
        android:icon="@android:drawable/ic_menu_gallery"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.LeafGuardAI">

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity
            android:name=".ScanActivity"
            android:exported="false" />
        <activity
            android:name=".ResultActivity"
            android:exported="false" />
        <activity
            android:name=".HistoryActivity"
            android:exported="false" />
        <activity
            android:name=".DiseaseLibraryActivity"
            android:exported="false" />
        <activity
            android:name=".SettingsActivity"
            android:exported="false" />
    </application>

</manifest>
```

| Element | What it does |
|---|---|
| `<manifest>` | Root element describing the whole app to Android. |
| `xmlns:android` | Defines the `android:` attribute namespace. Without it, no attribute is understood. |
| `android:allowBackup="true"` | Allows Android's automatic app-data backup. |
| `android:icon` | Week 02 borrows a built-in system icon so you do not need artwork yet. |
| `android:label="@string/app_name"` | The launcher name, read from `strings.xml`. |
| `android:supportsRtl="true"` | Layouts flip correctly for right-to-left languages. |
| `android:theme="@style/Theme.LeafGuardAI"` | Applies the theme from `themes.xml` to every screen. |
| `<activity android:name=".MainActivity">` | Registers the Home screen class. The leading `.` means "inside `com.leafguard`". |
| `android:exported="true"` on MainActivity | Required, because the device launcher is an outside app that starts it. |
| `<intent-filter>` with `MAIN` + `LAUNCHER` | Marks this Activity as the app's entry point and puts its icon in the launcher. |
| Five `android:exported="false"` Activities | Registered so `startActivity` can open them, but no other app may launch them. |

**Week 02 exclusions in this file:** no `<uses-permission>`, no `<provider>`, no `android:networkSecurityConfig`. Camera permission and FileProvider arrive in Week 03; internet arrives in Week 05.

---

### 10.5 Group C: Kotlin Activities (6 required files, 94 lines total)

#### C1. `MainActivity.kt` — 34 lines — the only file with real behavior

Path: `app/src/main/java/com/leafguard/MainActivity.kt`

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

Size breakdown: 1 package line, 5 imports, 1 class line, 1 `onCreate` header, 2 setup lines, 5 identical 3-line click blocks, closing braces.

| Code | What it does |
|---|---|
| `package com.leafguard` | Places the class in the app package declared by `namespace`. |
| `import android.content.Intent` | Brings in the class used to request another screen. |
| `import android.os.Bundle` | Type of the saved-state parameter of `onCreate`. |
| `import android.widget.Button` | Needed because `findViewById<Button>` names the type. |
| `import androidx.appcompat.app.AppCompatActivity` | The base screen class from the `appcompat` dependency. |
| `class MainActivity : AppCompatActivity()` | Defines the Home screen and inherits Android screen behavior. |
| `override fun onCreate(...)` | Android calls this once when the screen is created. |
| `super.onCreate(savedInstanceState)` | Lets the framework restore its own state first. Skipping this crashes the app. |
| `setContentView(R.layout.activity_main)` | Inflates `activity_main.xml` and shows it. |
| `findViewById<Button>(R.id.buttonOpenScan)` | Finds the Button whose XML id is `buttonOpenScan`. |
| `.setOnClickListener { ... }` | Stores a block of code to run on tap. |
| `Intent(this, ScanActivity::class.java)` | Explicit intent: "open this exact class". |
| `startActivity(...)` | Asks Android to push the new screen on top of the back stack. |

**Why the back button already works:** you never write back-navigation code. Android's back stack pops the top Activity automatically. That is a Week 02 concept worth stating out loud in your reflection.

#### C2–C6. The five placeholder Activities — 12 lines each

Paths: `ScanActivity.kt`, `ResultActivity.kt`, `HistoryActivity.kt`, `DiseaseLibraryActivity.kt`, `SettingsActivity.kt`

Every one of them is the same 12-line shape. Only the class name and the layout name change:

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

| File | Class name | Layout loaded | Placeholder promise |
|---|---|---|---|
| `ScanActivity.kt` | `ScanActivity` | `R.layout.activity_scan` | Camera and gallery arrive in Week 03. |
| `ResultActivity.kt` | `ResultActivity` | `R.layout.activity_result` | Real predictions arrive in Weeks 05–06. |
| `HistoryActivity.kt` | `HistoryActivity` | `R.layout.activity_history` | Saved scans arrive in Week 07. |
| `DiseaseLibraryActivity.kt` | `DiseaseLibraryActivity` | `R.layout.activity_disease_library` | XML disease library arrives in Week 08. |
| `SettingsActivity.kt` | `SettingsActivity` | `R.layout.activity_settings` | Preferences grow from Week 10 onward. |

| Code | What it does |
|---|---|
| `class X : AppCompatActivity()` | Declares one more screen Android can open. |
| `onCreate` + `super.onCreate` | Standard screen creation entry point. |
| `setContentView(R.layout.activity_x)` | Shows the matching placeholder layout. |

**What is deliberately absent from these 12 lines:** no `findViewById`, no click listeners, no fields, no data, no fake results. A Week 02 placeholder Activity has exactly two statements in its body. If yours has more, ask which future week that extra code belongs to.

*(Optional)* `AnalyticsActivity.kt` is the same 12-line template with `R.layout.activity_analytics`. Create it only if your Week 01 screen map listed an Analytics screen.

---

### 10.6 Group D: XML Layouts (6 required files, 181 lines total)

#### D1. `activity_main.xml` — 56 lines — the only layout with real interaction

Path: `app/src/main/res/layout/activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
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

Size breakdown: 1 XML declaration + 6-line root element + 2 TextViews (8 and 9 lines) + 5 Buttons (6, 5, 5, 5, 5 lines) + closing tag.

| Attribute | What it does |
|---|---|
| `LinearLayout` + `orientation="vertical"` | Stack children top to bottom in the order written. |
| `layout_width="match_parent"` | Fill the parent horizontally. |
| `layout_height="match_parent"` (root) | Fill the whole screen vertically. |
| `layout_height="wrap_content"` (child) | Be exactly as tall as the content needs. |
| `padding="24dp"` | Inner breathing room so text never touches screen edges. |
| `background="@color/screen_background"` | Uses the named color instead of a raw hex value. |
| `@+id/textHomeTitle` | `+` creates a new ID entry so Kotlin can call `findViewById`. |
| `@string/home_title` | Reads text from `strings.xml`; no hardcoded English in the layout. |
| `textSize="28sp"` | `sp` scales with the user's font-size setting; use `sp` for text, `dp` for spacing. |
| `layout_marginTop` | Outside spacing that separates one view from the previous one. |
| Five `Button` ids | Each id matches exactly one `findViewById` in `MainActivity.kt`. |

**The contract to remember:** every `@+id/...` here has a matching `R.id....` in `MainActivity.kt`. If you rename one side only, the app compiles but crashes with a null view at runtime.

#### D2–D6. The five placeholder layouts — 25 lines each

Every placeholder layout is the same shape. Only the ids and the two string references change:

```xml
<?xml version="1.0" encoding="utf-8"?>
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

| Layout file | Title string | Placeholder string | Filled in |
|---|---|---|---|
| `activity_scan.xml` | `@string/scan_title` | `@string/placeholder_scan` | Week 03 |
| `activity_result.xml` | `@string/result_title` | `@string/placeholder_result` | Weeks 05–06 |
| `activity_history.xml` | `@string/history_title` | `@string/placeholder_history` | Week 07 |
| `activity_disease_library.xml` | `@string/library_title` | `@string/placeholder_library` | Week 08 |
| `activity_settings.xml` | `@string/settings_title` | `@string/placeholder_settings` | Week 10+ |

| Element | What it does |
|---|---|
| First `TextView` | Screen title so the user knows where they navigated to. |
| Second `TextView` | The honest promise: what this screen will do and in which week. |
| No `Button`, no `ImageView`, no `RecyclerView` | Nothing here can be tapped, because nothing here works yet. |

**Every placeholder layout contains exactly two TextViews and zero interactive views.** That is the mechanical definition of "placeholder" for Week 02.

*(Optional)* `activity_analytics.xml` follows the same template.

---

### 10.7 Group E: Value Resources (3 files, 43 lines total)

#### E1. `res/values/strings.xml` — 25 lines, 20 strings

```xml
<?xml version="1.0" encoding="utf-8"?>
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

| String group | Count | Used by |
|---|---:|---|
| App identity (`app_name`) | 1 | manifest `android:label` |
| Home text | 2 | `activity_main.xml` |
| Button labels | 5 | `activity_main.xml` |
| Screen titles | 5 | the five placeholder layouts |
| Placeholder promises | 5 | the five placeholder layouts |
| **Total** | **20** | |

Rule: 20 strings, 0 hardcoded UI text anywhere in the layouts. Week 02 validation checks exactly this.

#### E2. `res/values/colors.xml` — 9 lines, 6 colors

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="leaf_green">#2E7D32</color>
    <color name="leaf_green_dark">#1B5E20</color>
    <color name="screen_background">#F7FAF4</color>
    <color name="text_primary">#1F2933</color>
    <color name="text_secondary">#52606D</color>
    <color name="white">#FFFFFF</color>
</resources>
```

| Color | Used for |
|---|---|
| `leaf_green` | Theme primary color. |
| `leaf_green_dark` | Home title text and the darker theme variant. |
| `screen_background` | Background of every Week 02 screen. |
| `text_primary` | Screen titles on placeholder screens. |
| `text_secondary` | Subtitles and placeholder messages. |
| `white` | Reserved for text drawn on green surfaces. |

Names describe **meaning**, not the shade. `screen_background` survives a redesign; `light_green_3` does not.

#### E3. `res/values/themes.xml` — 9 lines, 1 style

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.LeafGuardAI" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
        <item name="colorPrimary">@color/leaf_green</item>
        <item name="colorPrimaryDark">@color/leaf_green_dark</item>
        <item name="colorAccent">@color/leaf_green</item>
        <item name="android:windowBackground">@color/screen_background</item>
    </style>
</resources>
```

| Code | What it does |
|---|---|
| `<style name="Theme.LeafGuardAI">` | The theme name referenced by `android:theme` in the manifest. |
| `parent="Theme.MaterialComponents.DayNight.DarkActionBar"` | Inherits Material defaults and light/dark support from the `material` dependency. |
| `colorPrimary` | Action bar and default button tint. |
| `colorPrimaryDark` | Status bar color on older Android versions. |
| `colorAccent` | Highlight color for selected controls. |
| `android:windowBackground` | Default background behind every screen. |

One style, four items. Do not add per-screen styles yet.

---

### 10.8 Files That Must NOT Exist at the End of Week 02

If any of these are present, work has leaked in from a later week:

| File / folder | Belongs to | Why not yet |
|---|---|---|
| `res/xml/file_provider_paths.xml` | Week 03 | Only camera capture needs a FileProvider. |
| `<uses-permission android:name="android.permission.CAMERA" />` | Week 03 | No camera code exists yet. |
| `<uses-permission android:name="android.permission.INTERNET" />`, `res/xml/network_security_config.xml`, `network/` package, Retrofit dependencies | Week 05 | No backend call exists yet. |
| `database/` package, Room dependencies, `item_scan_history.xml` | Week 07 | No history storage exists yet. |
| `assets/diseases.xml`, `item_disease_library.xml` | Week 08 | No disease library parsing exists yet. |
| `assets/labels.txt`, `assets/model.tflite`, `ml/` package, TensorFlow Lite dependencies | Week 09 | No on-device inference exists yet. |
| `utils/NotificationHelper.kt`, notification permission | Week 10 | No notifications exist yet. |
| `ui/BottomNav.kt`, `res/menu/bottom_nav_menu.xml`, `res/drawable/ic_nav_*.xml` | Week 10 polish | Navigation polish comes after all screens are real. |
| `src/test/`, `src/androidTest/` test classes | Week 11 | Automated tests are a dedicated week. |

Placeholder screens are allowed. **Fake behavior is not.** A Result screen that shows "Tomato Blight, 92%" at the end of Week 02 is a bug in your learning process, not a feature.

---

### 10.9 The Java Twin (`android-app/`)

The Java track ends Week 02 with the **same 16 required files** and the same behavior. Only two things differ:

| Kotlin file | Java twin | Line difference |
|---|---|---|
| `MainActivity.kt` (34 lines) | `MainActivity.java` (about 40 lines) | Java needs explicit types and `(Button) findViewById(...)` casts or generic `findViewById`. |
| `ScanActivity.kt` (12 lines) | `ScanActivity.java` (about 14 lines) | Java needs `@Override protected void onCreate(Bundle savedInstanceState)`. |

The XML files (`AndroidManifest.xml`, 6 layouts, 3 value files) are **byte-for-byte the same idea** in both tracks. XML does not care which language reads it.

---

### 10.10 How to Verify Your Week 02 End State

From the repository root:

```bash
# 1. Count required Kotlin Activities: expect 6 (or 7 with Analytics)
find android-app-kotlin/app/src/main/java/com/leafguard -name "*.kt" | wc -l

# 2. Count layouts: expect 6 (or 7 with Analytics)
find android-app-kotlin/app/src/main/res/layout -name "*.xml" | wc -l

# 3. Count value files: expect exactly 3
find android-app-kotlin/app/src/main/res/values -name "*.xml" | wc -l

# 4. Confirm no future-week folders exist yet: expect no output
ls android-app-kotlin/app/src/main/java/com/leafguard/ | grep -E "network|database|ml|utils|ui"

# 5. Confirm no assets exist yet: expect "no assets" or an empty listing
ls android-app-kotlin/app/src/main/assets 2>/dev/null || echo "no assets"

# 6. Build
cd android-app-kotlin && ./gradlew assembleDebug
```

Record the output of these six commands in `docs/evidence/week-02/build-notes.md`. That output *is* your proof that the Week 02 slice is exactly the right size — not too small, not accidentally too large.

---

## 11. Week 02 Understanding Checklist

Before starting the build task, make sure you can answer:

- What file loads the Home screen layout?
- What folder stores XML layouts?
- What folder stores strings and colors?
- What does an Activity represent?
- What does an Intent do?
- Why must Activities be declared in the manifest?
- What should Week 02 not build yet?
- How many Android files exist when Week 02 ends, and which of them are placeholders?

If you can answer these in your own words, continue to `exercises.md`.

<!-- NAV_FOOTER_START -->

---

## 📚 Week 02 — Navigation

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
| [⬅ Week 01: Project Understanding](../week-01-project-understanding/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 03: Camera & Gallery ➡](../week-03-camera-gallery/README.md) |

---
