# Week 07: Room Database and Scan History

## Weekly Objective

By the end of Week 07, you will:

1. **Understand Android Room Architecture Components** and how Room simplifies SQLite database operations
2. **Create Entity classes** representing scan history records with proper annotations
3. **Implement DAO (Data Access Object) interfaces** defining database queries using SQL and Room annotations
4. **Build RoomDatabase class** with proper initialization and singleton pattern
5. **Create HistoryListActivity** displaying all saved scan records in a RecyclerView
6. **Implement ScanHistoryAdapter** for efficient list rendering with ViewHolder pattern
7. **Build HistoryDetailActivity** showing complete information for a selected scan
8. **Add delete functionality** with confirmation dialog and database cascading
9. **Integrate history saving** after each successful disease prediction
10. **Test complete CRUD operations** (Create, Read, Update, Delete) on scan history

**Measurable Outcomes:**
- ScanHistory entity with fields: id, disease, confidence, symptoms, timestamp, imagePath
- ScanHistoryDao with insert, getAll, getById, delete queries
- AppDatabase singleton managing Room database instance
- HistoryListActivity showing scrollable list of past scans
- Clicking a scan opens HistoryDetailActivity with full details
- Delete button removes scan from database and updates UI
- After prediction, scan automatically saved to history
- Git commits showing incremental database implementation

---

## Why This Week Matters

### Connection to CSE 2206 Mobile Application Development

Week 07 implements **local data persistence**, a critical requirement in mobile application development. Without scan history:
- Users cannot review past diagnoses
- No evidence of app usage over time
- Cannot track disease patterns
- Lost opportunity to demonstrate database skills in viva

**This week demonstrates:**
- **Files and Storage (CSE 2206 Topic):** Persistent data storage using SQLite
- **Android Architecture Components:** Room as modern SQLite wrapper
- **RecyclerView:** Efficient scrolling lists for potentially hundreds of records
- **Activity Navigation:** List-to-detail navigation pattern
- **CRUD Operations:** Complete database lifecycle management

### Academic Requirement Alignment

CSE 2206 explicitly requires:

1. **Local Data Storage:** SQLite database implementation (Week 07 core focus)
2. **Data Persistence:** Surviving app restarts and device reboots
3. **List Views:** RecyclerView with adapter pattern
4. **Database Queries:** SQL SELECT, INSERT, DELETE operations
5. **Data Lifecycle Management:** When to save, retrieve, and delete data

**Viva Question Preview:**
- "Show me your scan history feature"
- "Explain how Room differs from raw SQLite"
- "What happens to the database when the app is uninstalled?"
- "How do you prevent database concurrency issues?"

---

## Syllabus Topics Covered This Week

### Direct Coverage

1. **Files and Storage**
   - SQLite database on Android
   - Internal app storage
   - Data persistence across sessions

2. **Android Architecture Components**
   - Room Persistence Library
   - Entity, DAO, Database pattern
   - LiveData and ViewModels (optional advanced)

3. **RecyclerView**
   - ViewHolder pattern
   - Adapter implementation
   - Item click listeners
   - Dynamic list updates

4. **Database Operations**
   - CREATE TABLE (via Entity)
   - INSERT records
   - SELECT queries (all, by ID)
   - DELETE operations
   - Database versioning

5. **Data Models**
   - Entity class design
   - Primary keys and auto-increment
   - Field naming conventions
   - Type converters for Date

6. **Activity Navigation**
   - List-to-detail pattern
   - Passing data between activities
   - Back stack management

### CSE 2206 Concept Connections

| Syllabus Topic | LeafGuard AI Implementation | This Week |
|----------------|----------------------------|-----------|
| Files and Storage | Room SQLite database | ✅ Core focus |
| Data Persistence | Scan history survives app restart | ✅ Core focus |
| List Views | RecyclerView for history list | ✅ Core focus |
| Database Queries | DAO with SQL annotations | ✅ Core focus |
| CRUD Operations | Insert, Read, Delete scan records | ✅ Core focus |

---

## Prerequisites

### Completed Previous Weeks

- **Week 05:** Networking with Retrofit working, predictions received
- **Week 06:** ML model integration complete, real predictions returned

**Critical checkpoint:** Before starting Week 07, you MUST have:
1. Working prediction flow (upload image → get disease result)
2. ResultActivity displaying disease, confidence, symptoms
3. Understanding of Java classes and objects
4. Familiarity with Intents and Activity navigation

### Required Knowledge

1. **Java Fundamentals:**
   - Classes, objects, constructors
   - Getter/setter methods
   - Interfaces
   - Lists and ArrayLists
   - Date and timestamp handling

2. **Android Basics:**
   - Activities and Intents
   - RecyclerView basics
   - Button click listeners
   - AlertDialog for confirmations

3. **Database Concepts:**
   - What is a database table?
   - Primary keys
   - SQL INSERT, SELECT, DELETE
   - Relationships (one-to-many)

4. **Gradle:**
   - Adding dependencies
   - Syncing project after changes

---

## Learning Resources

### Official Documentation (Read First)

1. **Room Persistence Library:**
   - Official guide: https://developer.android.com/training/data-storage/room
   - Defining entities: https://developer.android.com/training/data-storage/room/defining-data
   - Accessing data with DAO: https://developer.android.com/training/data-storage/room/accessing-data

2. **RecyclerView:**
   - Create dynamic lists: https://developer.android.com/guide/topics/ui/layout/recyclerview

3. **SQLite:**
   - SQLite on Android: https://developer.android.com/training/data-storage/sqlite

### Video Tutorials (Recommended)

1. **Room Database Tutorial** (YouTube)
   - Search: "Android Room Database Tutorial 2024"
   - Focus on Entity, DAO, Database setup
   - Watch complete CRUD implementation

2. **RecyclerView Tutorial** (YouTube)
   - Search: "Android RecyclerView Tutorial"
   - Understand ViewHolder pattern
   - Learn item click handling

### Reading Materials

1. **Room vs Raw SQLite:**
   - Understand Room advantages: compile-time verification, less boilerplate
   - Room provides abstraction over SQLite

2. **Database Design Best Practices:**
   - Choosing appropriate data types
   - When to use auto-increment primary keys
   - Handling timestamps

---

## Conceptual Overview

### What You're Building This Week

```
ResultActivity                         Room Database
     │                                      │
     │  After successful prediction         │
     │  ┌─────────────────────────┐        │
     └─→│ Save scan to history    │───────>│ AppDatabase
        └─────────────────────────┘        │   └─ ScanHistoryDao
                                           │       └─ INSERT
MainActivity                               │
     │                                     │
     │  User taps "View History" button   │
     │  ┌─────────────────────────┐       │
     └─→│ HistoryListActivity     │<──────┤ SELECT * FROM scans
        │                         │       │
        │ [RecyclerView]          │       │
        │   - Scan 1              │       │
        │   - Scan 2              │       │
        │   - Scan 3              │       │
        └─────────────────────────┘       │
              │                            │
              │  Click on Scan 1           │
              ▼                            │
        ┌─────────────────────────┐       │
        │ HistoryDetailActivity   │<──────┤ SELECT * WHERE id=1
        │                         │       │
        │ Disease: Late Blight    │       │
        │ Confidence: 87%         │       │
        │ Date: 2024-05-25        │       │
        │ [Delete Button]         │       │
        └─────────────────────────┘       │
              │                            │
              │  Click Delete              │
              └───────────────────────────>│ DELETE WHERE id=1
```

### Room Database Architecture

```
┌─────────────────────────────────────────┐
│         Your Application Code            │
│  (Activities, Fragments, ViewModels)     │
└───────────────┬─────────────────────────┘
                │
    ┌───────────▼──────────────┐
    │    AppDatabase.class     │
    │  (RoomDatabase subclass) │
    └───────────┬──────────────┘
                │
    ┌───────────▼──────────────┐
    │  ScanHistoryDao.interface│
    │   (Data Access Object)   │
    │  - insert()              │
    │  - getAll()              │
    │  - getById()             │
    │  - delete()              │
    └───────────┬──────────────┘
                │
    ┌───────────▼──────────────┐
    │   ScanHistory.class      │
    │      (Entity)            │
    │  Fields:                 │
    │  - id (Primary Key)      │
    │  - disease               │
    │  - confidence            │
    │  - symptoms              │
    │  - timestamp             │
    │  - imagePath             │
    └───────────┬──────────────┘
                │
    ┌───────────▼──────────────┐
    │    SQLite Database       │
    │  (scan_history table)    │
    └──────────────────────────┘
```

### Key Room Concepts

#### 1. Entity (Database Table)

```java
@Entity(tableName = "scan_history")
public class ScanHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "disease_name")
    private String disease;

    private double confidence;
    private String symptoms;
    private long timestamp; // milliseconds since epoch

    @ColumnInfo(name = "image_path")
    private String imagePath;

    // Constructor, getters, setters
}
```

**Explanation:**
- `@Entity`: Marks class as database table
- `@PrimaryKey(autoGenerate = true)`: Auto-incrementing ID
- `@ColumnInfo`: Custom column name (optional)
- Fields = table columns

#### 2. DAO (Data Access Object)

```java
@Dao
public interface ScanHistoryDao {
    @Insert
    void insert(ScanHistory scan);

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    List<ScanHistory> getAll();

    @Query("SELECT * FROM scan_history WHERE id = :scanId")
    ScanHistory getById(int scanId);

    @Delete
    void delete(ScanHistory scan);
}
```

**Explanation:**
- `@Dao`: Marks interface for database operations
- `@Insert`, `@Delete`: Built-in operations
- `@Query`: Custom SQL queries
- Room generates implementation automatically

#### 3. RoomDatabase

```java
@Database(entities = {ScanHistory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ScanHistoryDao scanHistoryDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "leafguard_database"
            ).build();
        }
        return instance;
    }
}
```

**Why singleton?** Database should be created once and reused throughout app.

---

## Weekly Timeline

### Day 1: Room Database Setup (3 hours)

**Morning (Theory - 1 hour):**
- Read Room official documentation
- Watch "Room Database Tutorial" video
- Understand Entity, DAO, Database pattern

**Afternoon (Implementation - 1.5 hours):**
- Add Room dependencies to build.gradle
- Create ScanHistory entity class
- Create ScanHistoryDao interface
- Create AppDatabase class

**Evening (Testing - 30 minutes):**
- Sync Gradle
- Verify project builds
- Commit: "week-07: add Room dependencies and database classes"

---

### Day 2: History List UI (3 hours)

**Morning (Theory - 1 hour):**
- Review RecyclerView documentation
- Understand ViewHolder pattern
- Learn about adapters

**Afternoon (Implementation - 1.5 hours):**
- Create HistoryListActivity layout
- Add RecyclerView to layout
- Create list item layout (item_scan_history.xml)
- Create ScanHistoryAdapter class
- Create ViewHolder inner class

**Evening (Testing - 30 minutes):**
- Build and run (empty list for now)
- Verify RecyclerView displays
- Commit: "week-07: create history list UI with RecyclerView"

---

### Day 3: Database Queries and List Display (3 hours)

**Morning (Testing Data - 1 hour):**
- Create method to insert test data
- Add temporary button to insert test scans
- Insert 3-5 test records

**Afternoon (Implementation - 1.5 hours):**
- Query all scans from database in HistoryListActivity
- Bind data to RecyclerView adapter
- Display disease name, confidence, timestamp
- Handle empty state (no scans yet)

**Evening (Testing - 30 minutes):**
- Verify list displays test data
- Test scrolling with multiple items
- Commit: "week-07: implement database query and list display"

---

### Day 4: Detail View and Navigation (3 hours)

**Morning (Theory - 30 minutes):**
- Review Intent extras for passing data
- Understand item click listeners in RecyclerView

**Afternoon (Implementation - 2 hours):**
- Create HistoryDetailActivity layout
- Add TextViews for all scan fields
- Implement item click listener in adapter
- Pass scan ID via Intent to detail activity
- Query scan by ID in detail activity
- Display full scan information

**Evening (Testing - 30 minutes):**
- Test navigation from list to detail
- Verify all fields display correctly
- Commit: "week-07: add detail view and navigation"

---

### Day 5: Delete Functionality (2 hours)

**Morning (Implementation - 1.5 hours):**
- Add Delete button to HistoryDetailActivity layout
- Implement delete confirmation AlertDialog
- Execute delete operation on confirmation
- Return to HistoryListActivity after delete
- Refresh list after delete

**Evening (Testing - 30 minutes):**
- Test delete with confirmation
- Test cancel on delete dialog
- Verify scan removed from list
- Commit: "week-07: implement delete functionality"

---

### Day 6: Integrate History Saving (2 hours)

**Morning (Theory - 30 minutes):**
- Review where to save scan history
- Decide on data to save (all prediction fields)

**Afternoon (Implementation - 1 hour):**
- Add save logic after successful prediction in ResultActivity
- Create ScanHistory object from prediction data
- Insert into database on background thread
- Add Toast confirmation "Scan saved to history"

**Evening (Testing - 30 minutes):**
- Perform complete flow: camera → predict → save
- Open history list, verify new scan appears
- Commit: "week-07: integrate automatic history saving"

---

### Day 7: Polish and Testing (2 hours)

**Morning (Testing - 1.5 hours):**
- Test complete CRUD operations
- Test with 20+ scans (scrolling performance)
- Test edge cases (delete all scans, view empty history)
- Verify timestamps display correctly

**Afternoon (Documentation - 30 minutes):**
- Complete Week 07 validation checklist
- Take screenshots: history list, detail view, delete confirmation
- Update progress-tracker.md
- Fill out reflection.md
- Complete Week 07 quiz

**Evening (Final Commit):**
- Commit: "week-07: complete Room database implementation and testing"

---

## Common Mistakes to Avoid

### 1. ❌ Running Database Operations on Main Thread

**Problem:** NetworkOnMainThreadException equivalent for database

**Solution:** Use AsyncTask, Executors, or allow main thread queries only for testing:

```java
// ✅ Correct - background thread
ExecutorService executor = Executors.newSingleThreadExecutor();
executor.execute(() -> {
    database.scanHistoryDao().insert(scan);
    runOnUiThread(() -> {
        Toast.makeText(this, "Scan saved", Toast.LENGTH_SHORT).show();
    });
});

// ❌ Wrong - main thread (will crash)
database.scanHistoryDao().insert(scan);
```

### 2. ❌ Forgetting Room Dependencies

**Problem:** @Entity, @Dao annotations not recognized

**Solution:** Add all three Room dependencies:

```gradle
def room_version = "2.5.0"
implementation "androidx.room:room-runtime:$room_version"
annotationProcessor "androidx.room:room-compiler:$room_version"
// Optional: Room testing
androidTestImplementation "androidx.room:room-testing:$room_version"
```

### 3. ❌ Not Using Singleton for Database

**Problem:** Multiple database instances cause conflicts

**Solution:** Always use singleton pattern in AppDatabase:

```java
private static AppDatabase instance;

public static synchronized AppDatabase getInstance(Context context) {
    if (instance == null) {
        instance = Room.databaseBuilder(...).build();
    }
    return instance;
}
```

### 4. ❌ Missing Constructor or Getters/Setters in Entity

**Problem:** Room cannot instantiate Entity or access fields

**Solution:** Provide constructor and all getters/setters:

```java
@Entity
public class ScanHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String disease;

    // Required constructor
    public ScanHistory(String disease) {
        this.disease = disease;
    }

    // Required getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }
}
```

### 5. ❌ Incorrect Date/Time Handling

**Problem:** Trying to store Date objects directly

**Solution:** Store timestamps as long (milliseconds):

```java
// Saving
long timestamp = System.currentTimeMillis();
scan.setTimestamp(timestamp);

// Displaying
Date date = new Date(scan.getTimestamp());
SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
String formattedDate = sdf.format(date);
```

### 6. ❌ Not Handling Empty History State

**Problem:** Blank screen when no scans exist

**Solution:** Show empty state message:

```java
if (scanList.isEmpty()) {
    emptyTextView.setVisibility(View.VISIBLE);
    recyclerView.setVisibility(View.GONE);
} else {
    emptyTextView.setVisibility(View.GONE);
    recyclerView.setVisibility(View.VISIBLE);
}
```

### 7. ❌ Memory Leaks in RecyclerView

**Problem:** Not properly managing Context references

**Solution:** Use ApplicationContext for database, avoid storing Activity references in adapter

---

## Teacher Demonstration Tips

### What to Show Your Teacher

1. **Open app and tap "View History" button**
2. **Show HistoryListActivity** with 5-10 past scans
3. **Point out:**
   - Disease names displayed
   - Confidence percentages shown
   - Timestamps formatted nicely
   - List scrolls smoothly
4. **Tap on a scan** to open detail view
5. **Show HistoryDetailActivity** with complete information
6. **Tap "Delete" button**
7. **Show confirmation dialog** appears
8. **Confirm delete**
9. **Show scan removed** from list
10. **Perform new scan**: camera → predict → result
11. **Show new scan** automatically appears in history

### Key Points to Explain

1. **"I'm using Room, Android's official database library"**
   - Room wraps SQLite with type-safe API
   - Compile-time verification prevents SQL errors
   - Less boilerplate than raw SQLite

2. **"The database has three components: Entity, DAO, Database"**
   - Entity = table structure (ScanHistory class)
   - DAO = query interface (insert, select, delete methods)
   - Database = connection manager (singleton pattern)

3. **"Database operations run on background thread"**
   - Main thread only handles UI
   - Database operations use Executor
   - Results returned via callbacks

4. **"History persists across app restarts"**
   - Data stored in SQLite file
   - Survives app closing and device reboot
   - Only deleted when app is uninstalled

### Viva Question Preparation

**Q: How does Room differ from SQLite?**
A: SQLite is the underlying database engine. Room is a library that provides a clean API on top of SQLite. Room offers compile-time query verification, automatic object mapping, and reduces boilerplate code. With raw SQLite, I'd need to write ContentValues, Cursor parsing, and manual SQL strings. Room generates this code automatically from annotations.

**Q: What happens to the database when the app is uninstalled?**
A: The database file is stored in the app's internal storage (/data/data/package_name/databases/). When the app is uninstalled, Android automatically deletes this directory, removing the database. This is why uninstalling an app loses your data.

**Q: How do you prevent database corruption from concurrent access?**
A: Room's singleton pattern ensures only one database connection exists. Additionally, I run all database operations on a background thread using ExecutorService, which serializes operations preventing race conditions. Room also uses transactions internally to ensure atomic operations.

**Q: Explain the ViewHolder pattern in RecyclerView.**
A: ViewHolder caches view references for each list item. Instead of calling findViewById() every time an item scrolls into view, RecyclerView reuses ViewHolder objects. When an item scrolls off screen, its ViewHolder is recycled and bound to new data. This drastically improves scrolling performance, especially with large lists.

**Q: How do you format the timestamp for display?**
A: I store timestamps as long values (milliseconds since epoch) in the database. When displaying, I convert to Date object and use SimpleDateFormat with pattern "MMM dd, yyyy HH:mm" to format as "May 25, 2024 15:30". This approach keeps database storage efficient while allowing flexible display formatting.

---

## Validation Criteria

Before moving to Week 08, you MUST demonstrate:

### Technical Validation

- [ ] Room dependencies added to build.gradle
- [ ] ScanHistory entity created with all fields
- [ ] ScanHistoryDao interface with CRUD methods
- [ ] AppDatabase class with singleton pattern
- [ ] HistoryListActivity displays scans in RecyclerView
- [ ] ScanHistoryAdapter properly binds data
- [ ] Clicking scan opens HistoryDetailActivity
- [ ] Detail view shows all scan information
- [ ] Delete button with confirmation dialog works
- [ ] Scan removed from database and list after delete
- [ ] New scans automatically saved to history
- [ ] Database operations run on background thread
- [ ] No crashes when database is empty
- [ ] Timestamps display in human-readable format
- [ ] Back button navigation works correctly

### User Experience Validation

- [ ] Empty state message shown when no scans exist
- [ ] List scrolls smoothly with 20+ items
- [ ] Delete confirmation prevents accidental deletion
- [ ] Toast message confirms "Scan saved to history"
- [ ] Dates formatted clearly (not raw timestamps)

### Code Quality Validation

- [ ] Database singleton prevents multiple instances
- [ ] No database operations on main thread
- [ ] Proper null checks in detail activity
- [ ] No memory leaks (Context properly managed)
- [ ] Meaningful variable names
- [ ] No hardcoded strings (use strings.xml)

---

## Success Metrics

You have successfully completed Week 07 when:

1. **CRUD Test:** Can create, read, update (implicit), and delete scan history records
2. **Persistence Test:** Close app, reopen, verify history still present
3. **Integration Test:** Complete flow from prediction to history to detail to delete works
4. **Empty State Test:** Delete all scans, verify empty message displays
5. **Performance Test:** Add 20+ scans, verify smooth scrolling
6. **Explanation Ability:** Can explain Room's three components and why Room vs SQLite

---

## Next Week Preview: Week 08 - XML Disease Library

Now that you can save and retrieve scan history, Week 08 will:

- Create XML file with complete disease information (symptoms, treatments, prevention)
- Parse XML on device without network connection
- Map prediction labels to disease details
- Display detailed disease information from local XML
- Demonstrate XML parsing as CSE 2206 requirement

Week 07 built local data persistence. Week 08 adds offline knowledge base.

---

## Final Notes

### Time Estimate

- **Minimum:** 12-15 hours
- **Expected:** 15-18 hours
- **With debugging:** 18-22 hours

This is a database-heavy week. Budget extra time for understanding Room architecture.

### When to Ask for Help

Get help if:
- Room dependencies cause build errors
- Database operations crash with threading errors
- RecyclerView not displaying data after query
- Cannot understand Entity/DAO/Database relationship

### Collaboration Policy

- **Allowed:** Discussing Room concepts, sharing database design patterns
- **Not Allowed:** Copying someone's entire AppDatabase or DAO implementation

Write your own code. Understand every annotation.

---

**Ready to start? Open `learning-notes.md` to begin your Week 07 journey!**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 07 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| **1** | **README.md** ← *You are here* | **Week Overview & Objectives** |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

*(Start of week)* &nbsp;&nbsp;|&nbsp;&nbsp; **Week Overview & Objectives** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Theory & Learning Notes →](learning-notes.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 06: Cloud ML Model](../week-06-cloud-ml-model/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 08: XML Disease Library ➡](../week-08-xml-disease-library/README.md) |

---
