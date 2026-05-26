# Week 07: Exercises - Room Database and Scan History

## Overview

These 6 exercises will build your Room database skills progressively. Start with basic entity creation, then advance to complete CRUD operations.

**Time Investment:** 3-4 hours total (30-40 minutes per exercise)

---

## Exercise 1: Add Room Dependencies and Create Entity

**Objective:** Set up Room in your project and create your first Entity class.

### Tasks

1. Add Room dependencies to `build.gradle (Module: app)`:
```gradle
def room_version = "2.5.0"
implementation "androidx.room:room-runtime:$room_version"
annotationProcessor "androidx.room:room-compiler:$room_version"
```

2. Sync Gradle

3. Create `ScanHistory.java` entity class with fields:
   - `id` (int, primary key, auto-generate)
   - `disease` (String)
   - `confidence` (double)
   - `timestamp` (long)

4. Add all necessary annotations (@Entity, @PrimaryKey, etc.)

5. Generate constructor, getters, and setters

### Expected Output

- Project builds successfully
- No annotation errors
- ScanHistory class recognized as Entity

### Verification

- [ ] Room dependencies added
- [ ] Gradle synced successfully
- [ ] ScanHistory.java created with @Entity annotation
- [ ] All fields have getters and setters
- [ ] Project compiles without errors

---

## Exercise 2: Create DAO Interface

**Objective:** Define database operations using DAO interface.

### Tasks

1. Create `ScanHistoryDao.java` interface

2. Add @Dao annotation

3. Implement these methods:
   - `@Insert void insert(ScanHistory scan)`
   - `@Query("SELECT * FROM scan_history") List<ScanHistory> getAll()`
   - `@Query("SELECT * FROM scan_history WHERE id = :id") ScanHistory getById(int id)`
   - `@Delete void delete(ScanHistory scan)`

4. Verify SQL syntax is correct

### Expected Output

- DAO interface compiles
- Room recognizes all annotations
- No SQL syntax errors

### Verification

- [ ] ScanHistoryDao.java created
- [ ] @Dao annotation present
- [ ] All four methods defined
- [ ] SQL queries syntactically correct
- [ ] No compilation errors

---

## Exercise 3: Create AppDatabase Class

**Objective:** Build RoomDatabase class with singleton pattern.

### Tasks

1. Create `AppDatabase.java` class extending RoomDatabase

2. Add @Database annotation with:
   - entities = {ScanHistory.class}
   - version = 1

3. Add abstract method: `public abstract ScanHistoryDao scanHistoryDao()`

4. Implement singleton getInstance() method with:
   - Static instance variable
   - Synchronized method
   - Room.databaseBuilder()
   - Database name: "leafguard_database"

### Expected Output

- AppDatabase compiles
- Singleton pattern implemented correctly
- Database can be instantiated

### Test Code

```java
AppDatabase db = AppDatabase.getInstance(context);
ScanHistoryDao dao = db.scanHistoryDao();
// Should return non-null DAO
```

### Verification

- [ ] AppDatabase.java created
- [ ] Extends RoomDatabase
- [ ] @Database annotation with correct parameters
- [ ] Abstract scanHistoryDao() method
- [ ] Singleton getInstance() implemented
- [ ] Project builds successfully

---

## Exercise 4: Test Database Insert and Query

**Objective:** Perform first database operations.

### Tasks

1. In MainActivity, create test method `testDatabaseInsert()`

2. Get database instance

3. Create ExecutorService for background thread

4. Insert test ScanHistory record:
   - disease: "Test Disease"
   - confidence: 0.85
   - timestamp: System.currentTimeMillis()

5. Query all records and log count

6. Add Toast showing "Inserted 1 record"

### Implementation Hint

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
executor.execute(() -> {
    ScanHistory scan = new ScanHistory("Test Disease", 0.85, System.currentTimeMillis());
    db.scanHistoryDao().insert(scan);

    List<ScanHistory> all = db.scanHistoryDao().getAll();
    int count = all.size();

    runOnUiThread(() -> {
        Toast.makeText(this, "Total scans: " + count, Toast.LENGTH_SHORT).show();
    });
});
```

### Expected Output

- Record inserted successfully
- Query returns list with 1 item
- Toast shows correct count
- No crashes

### Verification

- [ ] testDatabaseInsert() method created
- [ ] ExecutorService used for background operation
- [ ] ScanHistory record created and inserted
- [ ] Query returns correct count
- [ ] Toast displays successfully
- [ ] No main thread database access errors

---

## Exercise 5: Build RecyclerView List

**Objective:** Display database records in RecyclerView.

### Tasks

1. Create `activity_history_list.xml` layout with RecyclerView

2. Create `item_scan_history.xml` layout with:
   - TextView for disease name
   - TextView for confidence
   - TextView for timestamp

3. Create `HistoryListActivity.java`

4. Create `ScanHistoryAdapter.java` with ViewHolder

5. In HistoryListActivity:
   - Query all scans from database
   - Set up RecyclerView with LinearLayoutManager
   - Set adapter with data

### Expected Output

- HistoryListActivity displays list of scans
- Each item shows disease, confidence, timestamp
- List scrolls smoothly

### Verification

- [ ] activity_history_list.xml created with RecyclerView
- [ ] item_scan_history.xml created with 3 TextViews
- [ ] HistoryListActivity.java created
- [ ] ScanHistoryAdapter.java created with ViewHolder
- [ ] RecyclerView displays database records
- [ ] List scrolls without lag

---

## Exercise 6: Implement Delete with Confirmation

**Objective:** Add delete functionality with AlertDialog.

### Tasks

1. Add Delete button to item layout OR detail activity

2. Implement item click listener in adapter

3. On delete click, show AlertDialog:
   - Title: "Delete Scan?"
   - Message: "This cannot be undone"
   - Positive button: "Delete"
   - Negative button: "Cancel"

4. On confirmation:
   - Delete from database on background thread
   - Remove from list
   - Show Toast "Scan deleted"

### Implementation Hint

```java
new AlertDialog.Builder(context)
    .setTitle("Delete Scan?")
    .setMessage("This cannot be undone")
    .setPositiveButton("Delete", (dialog, which) -> {
        executor.execute(() -> {
            db.scanHistoryDao().delete(scan);
            runOnUiThread(() -> {
                scans.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Scan deleted", Toast.LENGTH_SHORT).show();
            });
        });
    })
    .setNegativeButton("Cancel", null)
    .show();
```

### Expected Output

- Delete button triggers confirmation dialog
- Cancel keeps the scan
- Delete removes scan from database and UI
- Toast confirms deletion

### Verification

- [ ] Delete button/menu added
- [ ] AlertDialog appears on delete action
- [ ] Cancel button works (dismisses dialog)
- [ ] Delete button removes scan from database
- [ ] UI updates (item removed from list)
- [ ] Toast confirmation shown
- [ ] No crashes

---

## Bonus Exercise: Search by Disease Name

**Objective:** Add search functionality.

### Tasks

1. Add SearchView to HistoryListActivity toolbar

2. Create new DAO method:
```java
@Query("SELECT * FROM scan_history WHERE disease LIKE '%' || :query || '%'")
List<ScanHistory> searchByDisease(String query);
```

3. On search text change:
   - Query database with search term
   - Update RecyclerView with filtered results

4. On search cleared:
   - Show all scans again

### Expected Output

- SearchView appears in toolbar
- Typing filters list in real-time
- Clearing search shows all results

---

## Exercise Completion Checklist

After completing all exercises:

- [ ] Exercise 1: Entity created with Room dependencies
- [ ] Exercise 2: DAO interface with CRUD methods
- [ ] Exercise 3: AppDatabase with singleton
- [ ] Exercise 4: Insert and query tested successfully
- [ ] Exercise 5: RecyclerView displaying scans
- [ ] Exercise 6: Delete functionality working
- [ ] Bonus: Search feature implemented (optional)

---

## Common Issues and Solutions

**Issue 1: "Cannot resolve symbol @Entity"**
- Solution: Check Room dependencies added to build.gradle
- Run Gradle sync again

**Issue 2: "NetworkOnMainThreadException equivalent"**
- Solution: Use ExecutorService for all database operations
- Never call DAO methods directly on main thread

**Issue 3: "RecyclerView shows nothing"**
- Solution: Check adapter is set, LayoutManager is set, data is queried correctly
- Use Log.d() to verify data list size

**Issue 4: "App crashes on delete"**
- Solution: Ensure delete runs on background thread
- Check scan object is not null

---

**Time to implement! These exercises prepare you for the Week 07 build task.**
