# Week 02 Learning Notes: Android UI Shell From Zero

## Purpose

Week 02 teaches the first Android concepts needed to turn the Week 01 screen map into a runnable app shell. These notes explain the ideas before code.

You are learning how to create screens, layouts, resources, and navigation. You are not learning camera, backend, database, XML parsing, or AI yet.

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

## 10. Week 02 Understanding Checklist

Before starting the build task, make sure you can answer:

- What file loads the Home screen layout?
- What folder stores XML layouts?
- What folder stores strings and colors?
- What does an Activity represent?
- What does an Intent do?
- Why must Activities be declared in the manifest?
- What should Week 02 not build yet?

If you can answer these in your own words, continue to `exercises.md`.