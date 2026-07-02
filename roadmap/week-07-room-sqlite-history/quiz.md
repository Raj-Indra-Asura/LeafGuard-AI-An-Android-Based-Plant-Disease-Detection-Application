# Week 07: Quiz - Room Database and Scan History

> **Accuracy note:** answers use the real classes `ScanRecord` (table `scan_history`), `ScanDao` (`suspend fun`s), and `AppDatabase` (file `leafguard.db`). Kotlin is the primary track (coroutines + kapt); Java is the secondary reference.

## Instructions

- **Total Questions:** 35 (10 conceptual + 10 practical + 15 advanced)
- **Time Limit:** 50 minutes
- **Passing Score:** 28/35 (80%)

---

## Part A: Conceptual Questions

### Question 1
**What is Room?**
A) A cloud database service
B) An abstraction layer over SQLite
C) A layout component
D) A networking library

Your answer: _____

---

### Question 2
**Which annotation marks a class as a database table?**
A) @Table
B) @Database
C) @Entity
D) @Dao

Your answer: _____

---

### Question 3
**What does @PrimaryKey(autoGenerate = true) do?**
A) Creates foreign key
B) Auto-increments ID starting from 1
C) Generates random IDs
D) Makes field optional

Your answer: _____

---

### Question 4
**True or False: Database operations can run on the main thread in production.**

Your answer: _____

---

### Question 5
**What is the purpose of DAO?**
A) Display data
B) Define UI layout
C) Define database operations
D) Handle network requests

Your answer: _____

---

### Question 6
**Why use ViewHolder pattern in RecyclerView?**
A) Saves memory
B) Caches view references for performance
C) Required by Android
D) Handles clicks

Your answer: _____

---

### Question 7
**What happens to database when app is uninstalled?**
A) Database file preserved
B) Database automatically backed up
C) Database deleted with app
D) Database moved to SD card

Your answer: _____

---

### Question 8
**True or False: Room generates DAO implementation at compile-time.**

Your answer: _____

---

### Question 9
**Which is correct for querying all records?**
A) @Query("SELECT ALL FROM table")
B) @Query("SELECT * FROM table")
C) @GetAll
D) @SelectAll

Your answer: _____

---

### Question 10
**What does fallbackToDestructiveMigration() do?**
A) Backs up database
B) Deletes database on schema change
C) Migrates data safely
D) Prevents crashes

Your answer: _____

---

## Part B: Practical Questions

### Question 11
**Complete the missing annotation:**

```java
___________
public interface ScanDao {
    @Insert
    void insert(ScanRecord scan);
}
```

Your answer: _______________

---

### Question 12
**What's wrong with this code?**

```java
List<ScanRecord> scans = database.scanDao().getAll();
adapter.setScans(scans);
```

A) Nothing wrong
B) Should use ExecutorService
C) Wrong method name
D) Missing return statement

Your answer: _____

---

### Question 13
**How to make database operations thread-safe?**

Your answer (one sentence):
_______________________________________________________________

---

### Question 14
**Complete the singleton pattern:**

```java
public static _________ AppDatabase getInstance(Context context) {
    if (instance == null) {
        instance = Room.databaseBuilder(...).build();
    }
    return instance;
}
```

Your answer: _______________

---

### Question 15
**What's missing in this Entity?**

```java
@Entity
public class ScanRecord {
    private int id;
    private String disease;
    
    // Constructor and methods
}
```

Your answer: _______________

---

### Question 16
**True or False: RecyclerView automatically creates ViewHolders.**

Your answer: _____

---

### Question 17
**How to format timestamp for display?**

```java
long timestamp = scan.getTimestamp();
// What code converts this to "May 25, 2024"?
```

Your answer (code snippet):
_______________________________________________________________

---

### Question 18
**What does notifyDataSetChanged() do?**

Your answer:
_______________________________________________________________

---

### Question 19
**Complete the delete confirmation dialog:**

```java
new AlertDialog.Builder(context)
    .setTitle("Delete?")
    .setPositiveButton("Delete", (d, w) -> {
        // TODO: What goes here?
    })
    ._____________("Cancel", null)
    .show();
```

Your answer: _______________

---

### Question 20
**Identify the error:**

```java
@Query("SELECT * FROM scan_history WHERE id = ?")
ScanRecord getById(int id);
```

Your answer: _______________

---


## Part 2: Advanced Questions

### Question 21
**Which statement best describes `LiveData` vs `Flow` in Room?**
A) Both work only on the main thread
B) `LiveData` is lifecycle-aware for UI, while `Flow` fits coroutine-based streams
C) `Flow` cannot emit database updates
D) `LiveData` requires Retrofit

Your answer: _____

---

### Question 22
**Why would you use a `TypeConverter` in Room?**
A) To change XML into JSON
B) To store unsupported types such as `ArrayList<String>` in a single column
C) To improve RecyclerView scrolling
D) To create foreign keys automatically

Your answer: _____

---

### Question 23
**Complete the converter method signature:**

```java
public class Converters {
    @TypeConverter
    public String fromDiseaseList(________________ value) {
        // TODO
    }
}
```

Your answer: _______________

---

### Question 24
**When is `@Ignore` useful in an Entity class?**
A) When a field should not be stored in the Room table
B) When you want to hide a class from RecyclerView
C) When you want Room to delete a column
D) When you want a field to become the primary key

Your answer: _____

---

### Question 25
**True or False:** A Room migration should preserve existing data whenever possible instead of deleting the whole database.

Your answer: _____

---

### Question 26
**Write the SQL statement used in a migration to add a new `plant_type` column to `scan_history`.**

Your answer:
_______________________________________________________________

---

### Question 27
**Why are coroutines or `ExecutorService` preferred over `AsyncTask` for Room operations?**
A) Because `AsyncTask` is deprecated and less flexible
B) Because Room works only with coroutines
C) Because Room forbids background threads
D) Because `AsyncTask` automatically creates migrations

Your answer: _____

---

### Question 28
**What does `@ForeignKey` help enforce?**
A) Bitmap compression rules
B) Relationships between parent and child tables
C) RecyclerView item animation
D) Network timeout handling

Your answer: _____

---

### Question 29
**Which statement is true about Database Inspector in Android Studio?**
A) It can inspect Room/SQLite tables while the app is running on a debuggable device
B) It only works for Firebase
C) It edits Java code automatically
D) It replaces DAO interfaces

Your answer: _____

---

### Question 30
**What is the biggest advantage of `Room.inMemoryDatabaseBuilder()` in tests?**
A) It stores data permanently between app launches
B) It creates a fast temporary database isolated for test cases
C) It removes the need for assertions
D) It works only with Espresso tests

Your answer: _____

---

### Question 31
**Short answer:** If a scan can have many tags, why is storing `ArrayList<String>` directly as a field not supported without a converter?

Your answer:
_______________________________________________________________

---

### Question 32
**Identify the missing annotation:**

```java
@Entity(
    foreignKeys = @____________(
        entity = ScanSession.class,
        parentColumns = "session_id",
        childColumns = "session_owner_id",
        onDelete = ForeignKey.CASCADE
    )
)
public class ScanPhoto {
}
```

Your answer: _______________

---

### Question 33
**What is a major risk of calling `fallbackToDestructiveMigration()` in a history app like LeafGuard?**
A) The APK size increases
B) User scan history may be deleted during schema upgrades
C) RecyclerView stops updating
D) The DAO becomes abstract

Your answer: _____

---

### Question 34
**True or False:** `Flow<List<ScanRecord>>` is collected using coroutine APIs such as `collect()`.

Your answer: _____

---

### Question 35
**Why should migration behavior be tested before release?**

Your answer:
_______________________________________________________________

---

## Answer Key

1. B  2. C  3. B  4. F  5. C  6. B  7. C  8. T  9. B  10. B  
11. @Dao  12. B  13. Use ExecutorService or synchronized methods  
14. synchronized  15. @PrimaryKey annotation on id  16. F  
17. SimpleDateFormat or DateFormat  18. Refreshes RecyclerView  
19. setNegativeButton  20. Should use :id not ?
21. B  22. B  23. ArrayList<String>  24. A  25. T
26. ALTER TABLE scan_history ADD COLUMN plant_type TEXT NOT NULL DEFAULT 'Unknown'
27. A  28. B  29. A  30. B  31. Room cannot store complex list objects without conversion
32. ForeignKey  33. B  34. T  35. To confirm schema upgrades preserve data and do not crash

---

**Your Score:** _____ / 35

**Status:** [ ] PASS (≥28) | [ ] FAIL (<28)


<!-- NAV_FOOTER_START -->

---

## 📚 Week 07 — Navigation

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
| [⬅ Week 06: Cloud ML Model](../week-06-cloud-ml-model/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 08: XML Disease Library ➡](../week-08-xml-disease-library/README.md) |

---
