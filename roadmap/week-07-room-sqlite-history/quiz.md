# Week 07: Quiz - Room Database and Scan History

## Instructions

- **Total Questions:** 20 (10 conceptual + 10 practical)
- **Time Limit:** 30 minutes
- **Passing Score:** 16/20 (80%)

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
public interface ScanHistoryDao {
    @Insert
    void insert(ScanHistory scan);
}
```

Your answer: _______________

---

### Question 12
**What's wrong with this code?**

```java
List<ScanHistory> scans = database.scanHistoryDao().getAll();
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
public class ScanHistory {
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
ScanHistory getById(int id);
```

Your answer: _______________

---

## Answer Key

1. B  2. C  3. B  4. F  5. C  6. B  7. C  8. T  9. B  10. B  
11. @Dao  12. B  13. Use ExecutorService or synchronized methods  
14. synchronized  15. @PrimaryKey annotation on id  16. F  
17. SimpleDateFormat or DateFormat  18. Refreshes RecyclerView  
19. setNegativeButton  20. Should use :id not ?

---

**Your Score:** _____ / 20

**Status:** [ ] PASS (≥16) | [ ] FAIL (<16)
