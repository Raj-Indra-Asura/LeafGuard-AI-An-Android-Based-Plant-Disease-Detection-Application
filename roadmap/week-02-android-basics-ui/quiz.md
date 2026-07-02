# Week 02 Quiz: Android Studio Setup & UI Skeleton

## Instructions

- **Total Questions:** 15 (mix of multiple choice, true/false, short answer)
- **Passing Score:** 12/15 (80%)
- **Time Limit:** None (take your time, understand concepts)
- **Open Book:** You may refer to learning materials
- **Purpose:** Verify understanding, not memorization

**Save your answers in `evidence/week-02/quiz-answers.md`**

---

## Section 1: Multiple Choice (1 point each)

### Question 1

What is the purpose of the `onCreate()` method in an Activity?

A) To handle button clicks
B) To initialize the activity, inflate layout, and set up views
C) To destroy the activity when user leaves
D) To save activity state during rotation

**Your Answer:** ___

---

### Question 2

Which XML attribute makes a view's width fill its parent in ConstraintLayout?

A) `android:layout_width="match_parent"`
B) `android:layout_width="wrap_content"`
C) `android:layout_width="0dp"` with start and end constraints
D) `android:layout_width="fill_parent"`

**Your Answer:** ___

---

### Question 3

What is the correct way to declare an Activity in AndroidManifest.xml?

A) `<activity android:name="MainActivity" />`
B) `<activity android:name=".activities.MainActivity" android:exported="true" />`
C) `<component type="activity" class="MainActivity" />`
D) Activities are automatically detected, no declaration needed

**Your Answer:** ___

---

### Question 4

What happens when a user rotates the device screen?

A) Nothing, the activity continues unchanged
B) The activity is paused temporarily
C) The activity is destroyed and recreated with new configuration
D) The activity saves state automatically with no code needed

**Your Answer:** ___

---

### Question 5

Which Gradle file contains app-specific configuration like minSdk and dependencies?

A) `settings.gradle`
B) `build.gradle` (Project level)
C) `build.gradle` (App level)
D) `gradle.properties`

**Your Answer:** ___

---

### Question 6

What is the purpose of externalizing strings to `strings.xml`?

A) To make the app larger
B) To support internationalization and centralized management
C) It is required by Android, hardcoded strings cause errors
D) To improve app performance

**Your Answer:** ___

---

### Question 7

How do you pass a String from MainActivity to ResultActivity?

A) `intent.putExtra("key", "value"); startActivity(intent);`
B) `intent.setString("key", "value");`
C) `ResultActivity.setValue("key", "value");`
D) Strings cannot be passed between activities

**Your Answer:** ___

---

### Question 8

What is the purpose of the `TAG` constant used with Log statements?

A) To tag the activity for deletion
B) To filter Logcat messages by activity or class
C) To set the activity title
D) It is required syntax for Log.d()

**Your Answer:** ___

---

## Section 2: True/False (1 point each)

### Question 9

**Statement:** All activities must have `android:exported="true"` in the manifest.

**Your Answer:** True / False

**Explanation (if False):** ___

---

### Question 10

**Statement:** ConstraintLayout allows for flat view hierarchies, reducing nested layouts.

**Your Answer:** True / False

**Explanation:** ___

---

### Question 11

**Statement:** `minSdk 24` means the app will run on devices with Android 7.0 and higher.

**Your Answer:** True / False

**Explanation:** ___

---

### Question 12

**Statement:** When `onDestroy()` is called, the activity instance can be reused later.

**Your Answer:** True / False

**Explanation (if False):** ___

---

## Section 3: Short Answer (2 points each)

### Question 13

**Question:** Explain the difference between `compileSdk`, `minSdk`, and `targetSdk`. Why are three different SDK versions needed?

**Your Answer (4-6 sentences):**
```








```

---

### Question 14

**Question:** You navigate from MainActivity → MainActivity → ResultActivity. The user presses the Back button twice. Describe the activity stack at each step and which lifecycle methods are called.

**Your Answer:**
```
Initial State: [MainActivity]

After navigating to MainActivity:
Stack: ___
MainActivity lifecycle: ___
MainActivity lifecycle: ___

After navigating to ResultActivity:
Stack: ___
MainActivity lifecycle: ___
ResultActivity lifecycle: ___

After first Back button press:
Stack: ___
ResultActivity lifecycle: ___
MainActivity lifecycle: ___

After second Back button press:
Stack: ___
MainActivity lifecycle: ___
MainActivity lifecycle: ___
```

---

### Question 15

**Question:** Why does LeafGuard use 5 separate activities instead of one activity with multiple screens (views)? What are the advantages and disadvantages of this approach?

**Your Answer (5-7 sentences):**
```










```

---

## Section 4: Code Analysis (2 points)

### Question 16

**Given this code, identify and explain the error:**

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        });

        setContentView(R.layout.activity_main);
    }
}
```

**What is wrong? How to fix it?**

**Your Answer:**
```
Error:


Why this causes a problem:


Fix:
```

---

### Question 17

**Given this layout code, what constraint is missing for proper positioning?**

```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/tvTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Welcome"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

**Your Answer:**
```
Missing constraint:


Why it's needed:


Fixed code:
```

---

## Bonus Question (+2 points)

### Question 18

**Bonus Question:** Explain how ViewBinding (enabled in build.gradle) improves upon `findViewById()`. What are two advantages?

**Your Answer:**
```




```

---

## Answer Key Location

**Do not look at answers until you complete the quiz.**

Answers will be provided separately in `quiz-answer-key.md` for self-grading.

---

## Self-Grading

After completing the quiz:

1. Check your answers against the answer key
2. Calculate your score: ___/17 (or ___/19 with bonus)
3. If score < 12/15 (80%), review weak areas and retake quiz
4. If score ≥ 12/15, you may proceed to Week 03

**Passing Score:** 12/15 (80%)

**Your Score:** ___/17

**Pass/Retake:** ___

---

## Score Interpretation

- **15-17 (88-100%):** Excellent understanding, ready for Week 03
- **12-14 (71-82%):** Good understanding, review flagged topics, proceed to Week 03
- **9-11 (53-65%):** Partial understanding, review learning notes, retake quiz
- **0-8 (<53%):** Insufficient understanding, redo Week 02 exercises and learning notes

---

## Reflection on Quiz

After completing the quiz, answer:

**Which question was hardest?** ___

**Why?** ___

**Which concept needs more review?** ___

**Action plan:** ___

---

**Submit `evidence/week-02/quiz-answers.md` with your answers. Commit with message: "Week 02: Complete quiz"**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 02 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| **6** | **quiz.md** ← *You are here* | **Knowledge Assessment Quiz** |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Validation & Verification](validation-checklist.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Knowledge Assessment Quiz** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Reflection & Consolidation →](reflection.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 01: Project Understanding](../week-01-project-understanding/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 03: Camera & Gallery ➡](../week-03-camera-gallery/README.md) |

---
