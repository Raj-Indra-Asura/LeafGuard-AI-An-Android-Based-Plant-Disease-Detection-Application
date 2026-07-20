# Week 02 Notebook: Android UI Navigation Shell

This Markdown notebook is an optional companion to the Week 02 roadmap. The Kotlin roadmap remains the primary learning path:

```text
roadmap/week-02-android-basics-ui/
```

Use this file to review concepts in small cells. Week 02 builds only a runnable UI shell with placeholder screens.

---

## Cell 1: Connect Week 01 to Week 02

### Explanation

Week 01 created the product idea and screen map. Week 02 turns that screen map into Android screens.

### Try This

Write this mapping in your notebook or evidence file:

| Week 01 Screen Idea | Android Activity | Status in Week 02 |
|---|---|---|
| Home | MainActivity | Real navigation screen |
| Scan | ScanActivity | Placeholder only |
| Result | ResultActivity | Placeholder only |
| History | HistoryActivity | Placeholder only |
| Disease Library | DiseaseLibraryActivity | Placeholder only |
| Settings/About | SettingsActivity | Placeholder only |

### Checkpoint

Can you explain why placeholder screens are useful before camera, backend, database, and AI are implemented?

---

## Cell 2: Activity Loads a Layout

### Explanation

An Activity is one Android screen. The layout file controls what the screen looks like.

### Kotlin Example

```kotlin
class ScanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
    }
}
```

### Line Notes

| Line | Meaning |
|---|---|
| `class ScanActivity` | Names the screen. |
| `AppCompatActivity()` | Gives the screen Android Activity behavior. |
| `onCreate` | Runs when the screen is created. |
| `setContentView` | Connects the screen to XML UI. |

### Checkpoint

What layout file does `R.layout.activity_scan` point to?

---

## Cell 3: Simple Placeholder Layout

### Explanation

A Week 02 placeholder must be honest. It should say what future behavior will be added later.

### XML Example

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Scan" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Image input will be added in Week 03." />
</LinearLayout>
```

### Checkpoint

Why is this better than pretending the camera already works?

---

## Cell 4: Home Navigation With Intent

### Explanation

An Intent opens another screen. In Week 02, the Home screen uses buttons to open placeholder screens.

### Kotlin Example

```kotlin
findViewById<Button>(R.id.buttonOpenScan).setOnClickListener {
    startActivity(Intent(this, ScanActivity::class.java))
}
```

### Line Notes

| Code | Meaning |
|---|---|
| `findViewById<Button>` | Finds a Button from the current layout. |
| `R.id.buttonOpenScan` | The ID created in XML. |
| `setOnClickListener` | Runs code after a tap. |
| `Intent(this, ScanActivity::class.java)` | Names the destination screen. |
| `startActivity` | Opens the destination screen. |

### Checkpoint

What would happen if `ScanActivity` was not declared in the manifest?

---

## Cell 5: Manifest Declaration

### Explanation

Android must know which screens exist.

### XML Example

```xml
<activity
    android:name=".ScanActivity"
    android:exported="false" />
```

### Checkpoint

Why is `MainActivity` exported but internal screens usually are not?

---

## Cell 6: Evidence Checklist

Save evidence under:

```text
docs/evidence/week-02/
```

Evidence should show:

- build success
- Home screen
- Scan placeholder
- Result placeholder
- History placeholder
- Disease Library placeholder
- Settings/About placeholder
- short note: which future week completes each placeholder

---

## Final Notebook Check

Before moving to Week 03, answer:

1. What did Week 02 add to Week 01?
2. What does an Activity do?
3. What does an XML layout do?
4. How does an Intent open another screen?
5. Why should real camera/gallery behavior wait until Week 03?


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

### Related Roadmap Materials
- 📖 [Week 02 README](../../roadmap/week-02-android-basics-ui/README.md) — Week overview & objectives
- 📝 [Week 02 Exercises](../../roadmap/week-02-android-basics-ui/exercises.md) — Practice problems
- 💡 [Week 02 Solutions](../../solutions/week-02/README.md) — Reference solutions
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

### Week Progression

| ← Previous | 🏠 | Next → |
|:-----------|:--:|-------:|
| [⬅ Week 01 Notebooks](../week-01/README.md) | [Notebooks Index](../README.md) | [Week 03 Notebooks ➡](../week-03/README.md) |

---