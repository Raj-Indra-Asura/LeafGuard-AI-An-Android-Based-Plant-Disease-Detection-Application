# Week 07: Learning Notes - Room Database and Scan History

> **Kotlin-first & accuracy note:** The shipped classes are `ScanRecord` (table `scan_history`, columns `id`, `disease_name`, `confidence`, `symptoms`, `treatment`, `prevention`, `image_uri`, `latitude`, `longitude`, `timestamp`), `ScanDao` (Kotlin `suspend fun`s), and `AppDatabase` (file `leafguard.db`). Kotlin is the primary track (coroutines via `lifecycleScope.launch { }`, plus **kapt** so Room can generate its code); Java examples using `ExecutorService`/`annotationProcessor` are the labelled secondary reference.

## Table of Contents

1. [SQLite and Room Database Fundamentals](#1-sqlite-and-room-database-fundamentals)
2. [Room Architecture Components](#2-room-architecture-components)
3. [Entity Classes Deep Dive](#3-entity-classes-deep-dive)
4. [DAO Patterns and Best Practices](#4-dao-patterns-and-best-practices)
5. [RoomDatabase Configuration](#5-roomdatabase-configuration)
6. [RecyclerView and Adapter Pattern](#6-recyclerview-and-adapter-pattern)
7. [Threading and Database Operations](#7-threading-and-database-operations)
8. [CRUD Operations Implementation](#8-crud-operations-implementation)
9. [Data Lifecycle Management](#9-data-lifecycle-management)
10. [CSE 2206 Exam Preparation](#10-cse-2206-exam-preparation)

---

## 1. SQLite and Room Database Fundamentals

### What is SQLite?

**SQLite** is a lightweight, embedded relational database that runs directly within the Android app process. Unlike server databases (MySQL, PostgreSQL), SQLite stores data in a single file on the device.

#### Key Characteristics

- **Embedded:** No separate server process
- **Self-contained:** Single file contains entire database
- **Serverless:** Direct file access, no network protocols
- **ACID compliant:** Atomic, Consistent, Isolated, Durable transactions
- **Cross-platform:** Works on Android, iOS, desktop

#### SQLite on Android

```
App Process Memory
┌──────────────────────────────┐
│  Your Application Code       │
│  ┌────────────────────┐     │
│  │  SQLite Library    │     │
│  │  (Built into       │     │
│  │   Android OS)      │     │
│  └──────┬─────────────┘     │
│         │                    │
└─────────┼────────────────────┘
          │
          ▼
  /data/data/your.package/databases/
          │
          ▼
  leafguard.db (SQLite file)
```

### What is Room?

**Room** is an **abstraction layer** over SQLite, providing compile-time verification and reducing boilerplate code.

#### Room vs Raw SQLite Comparison

| Feature | Raw SQLite | Room |
|---------|-----------|------|
| Query verification | Runtime (crashes) | Compile-time (build errors) |
| Object mapping | Manual (ContentValues, Cursor) | Automatic (annotations) |
| Boilerplate code | High (100+ lines) | Low (20-30 lines) |
| Type safety | No | Yes |
| Threading | Manual | Built-in support |
| Migration | Manual SQL | Version-based |

#### Example: Inserting a Record

**Raw SQLite:**
```java
// 50+ lines of boilerplate
SQLiteDatabase db = dbHelper.getWritableDatabase();
ContentValues values = new ContentValues();
values.put("disease", "Late Blight");
values.put("confidence", 0.87);
values.put("timestamp", System.currentTimeMillis());
long id = db.insert("scan_history", null, values);
db.close();
```

**Room:**
```java
// 5 lines, type-safe
ScanRecord scan = new ScanRecord("Late Blight", 0.87, System.currentTimeMillis());
database.scanDao().insert(scan);
```

---

## 2. Room Architecture Components

### The Three Pillars of Room

Room database architecture consists of exactly three components:

1. **Entity:** Represents database tables
2. **DAO (Data Access Object):** Defines database operations
3. **Database:** Manages database creation and access

```
┌─────────────────────────────────────┐
│  1. ENTITY                          │
│  @Entity(tableName = "scan_history")│
│  public class ScanRecord {         │
│      @PrimaryKey int id;            │
│      String disease;                │
│      double confidence;             │
│  }                                  │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  2. DAO                             │
│  @Dao                               │
│  public interface ScanDao {  │
│      @Insert void insert(...);      │
│      @Query("...") List getAll();   │
│      @Delete void delete(...);      │
│  }                                  │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  3. DATABASE                        │
│  @Database(entities = {ScanRecord})│
│  public abstract class AppDatabase  │
│      extends RoomDatabase {         │
│      public abstract ScanDao │
│          scanDao();          │
│  }                                  │
└─────────────────────────────────────┘
```

---

## 3. Entity Classes Deep Dive

### Basic Entity Structure

```java
@Entity(tableName = "scan_history")
public class ScanRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "disease_name")
    private String disease;

    private double confidence;
    private String symptoms;
    private String treatment;

    @ColumnInfo(name = "scan_timestamp")
    private long timestamp;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    // Constructor
    public ScanRecord(String disease, double confidence, String symptoms,
                       String treatment, long timestamp, String imagePath) {
        this.disease = disease;
        this.confidence = confidence;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.timestamp = timestamp;
        this.imagePath = imagePath;
    }

    // Getters and setters for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    // ... more getters/setters
}
```

### Entity Annotations Explained

#### @Entity
```java
@Entity(tableName = "scan_history")  // Custom table name
// OR
@Entity  // Table name = class name in lowercase
```

**Purpose:** Marks class as database table

#### @PrimaryKey
```java
@PrimaryKey(autoGenerate = true)
private int id;
```

**Options:**
- `autoGenerate = true`: Auto-increment (1, 2, 3, ...)
- `autoGenerate = false`: Must provide ID manually

**Why auto-increment?** Room manages IDs automatically, prevents duplicates.

#### @ColumnInfo
```java
@ColumnInfo(name = "disease_name")
private String disease;
```

**Purpose:** Customize column name in database (optional)

**When to use:**
- Database naming conventions differ from Java (snake_case vs camelCase)
- Clarity: "disease_name" more explicit than "disease"

#### @Ignore
```java
@Ignore
private Bitmap cachedImage;  // Don't save to database
```

**Purpose:** Exclude fields from database

**Use cases:**
- Temporary runtime data
- Derived/calculated values
- Objects that can't be serialized

### Data Type Mapping

| Java Type | SQLite Type |
|-----------|-------------|
| int, Integer | INTEGER |
| long, Long | INTEGER |
| float, Float | REAL |
| double, Double | REAL |
| String | TEXT |
| boolean, Boolean | INTEGER (0=false, 1=true) |
| byte[] | BLOB |
| Date | INTEGER (requires TypeConverter) |

### Type Converters for Complex Types

```java
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}

@Database(entities = {ScanRecord.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    // ...
}
```

---

## 4. DAO Patterns and Best Practices

### DAO Interface Structure

```java
@Dao
public interface ScanDao {
    // Create
    @Insert
    void insert(ScanRecord scan);

    @Insert
    void insertAll(ScanRecord... scans);

    // Read
    @Query("SELECT * FROM scan_history ORDER BY scan_timestamp DESC")
    List<ScanRecord> getAll();

    @Query("SELECT * FROM scan_history WHERE id = :scanId")
    ScanRecord getById(int scanId);

    @Query("SELECT * FROM scan_history WHERE disease_name LIKE :disease")
    List<ScanRecord> findByDisease(String disease);

    // Update
    @Update
    void update(ScanRecord scan);

    // Delete
    @Delete
    void delete(ScanRecord scan);

    @Query("DELETE FROM scan_history WHERE id = :scanId")
    void deleteById(int scanId);

    @Query("DELETE FROM scan_history")
    void deleteAll();

    // Count
    @Query("SELECT COUNT(*) FROM scan_history")
    int getCount();
}
```

### DAO Annotation Types

#### @Insert
```java
@Insert
void insert(ScanRecord scan);

@Insert
long insertAndReturnId(ScanRecord scan);  // Returns inserted ID

@Insert
List<Long> insertAll(ScanRecord... scans);  // Varargs

@Insert(onConflict = OnConflictStrategy.REPLACE)
void insertOrReplace(ScanRecord scan);  // Replace if exists
```

**OnConflictStrategy options:**
- `REPLACE`: Replace existing record
- `IGNORE`: Keep existing, ignore new
- `ABORT`: Cancel transaction (default)

#### @Update
```java
@Update
void update(ScanRecord scan);

@Update
int updateAndReturnCount(ScanRecord scan);  // Returns affected rows
```

**Note:** Update matches by primary key automatically.

#### @Delete
```java
@Delete
void delete(ScanRecord scan);

@Delete
int deleteAndReturnCount(ScanRecord scan);  // Returns deleted count
```

#### @Query
Most flexible - write custom SQL:

```java
@Query("SELECT * FROM scan_history WHERE confidence > :minConfidence")
List<ScanRecord> getHighConfidenceScans(double minConfidence);

@Query("SELECT disease_name, COUNT(*) as count FROM scan_history " +
       "GROUP BY disease_name ORDER BY count DESC")
List<DiseaseCount> getDiseaseStatistics();
```

**Parameter binding:** Use `:paramName` syntax

### LiveData and Observability (Advanced)

```java
@Dao
public interface ScanDao {
    @Query("SELECT * FROM scan_history ORDER BY scan_timestamp DESC")
    LiveData<List<ScanRecord>> getAllLive();
}

// In Activity
database.scanDao().getAllLive().observe(this, scans -> {
    // Automatically called when database changes
    adapter.setScans(scans);
});
```

**Benefits:**
- Automatic UI updates when data changes
- Lifecycle-aware (stops observing when Activity destroyed)
- No manual refresh needed

---

## 5. RoomDatabase Configuration

### Database Class Structure

```java
@Database(entities = {ScanRecord.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // Abstract method for each DAO
    public abstract ScanDao scanDao();

    // Singleton pattern
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "leafguard.db"
            )
            .fallbackToDestructiveMigration()  // CAUTION: Deletes data on schema change
            .build();
        }
        return instance;
    }
}
```

### Database Annotations

#### @Database
```java
@Database(
    entities = {ScanRecord.class, User.class},  // All entities
    version = 1,  // Schema version
    exportSchema = false  // Don't export schema to file
)
```

**Version management:**
- Increment version when schema changes (add/remove columns)
- Provide Migration if you want to preserve data
- Use `fallbackToDestructiveMigration()` to delete on change (development only)

### Singleton Pattern Explained

**Why singleton?**
1. Database creation is expensive (file I/O, initialization)
2. Multiple instances cause conflicts (locking issues)
3. Memory efficiency (one connection per app)

**Thread-safety:** `synchronized` keyword prevents race conditions

### Database Builder Options

```java
Room.databaseBuilder(context, AppDatabase.class, "database_name")
    .fallbackToDestructiveMigration()  // Delete on schema change
    .allowMainThreadQueries()  // ONLY for testing/debugging
    .addMigrations(MIGRATION_1_2)  // Custom migration
    .addCallback(new DatabaseCallback())  // Lifecycle hooks
    .build();
```

**Production recommendation:** Never use `allowMainThreadQueries()`

---

## 6. RecyclerView and Adapter Pattern

### RecyclerView Architecture

```
Activity
  │
  ├─ RecyclerView (displays items)
  │    │
  │    ├─ LayoutManager (positions items)
  │    │    └─ LinearLayoutManager
  │    │    └─ GridLayoutManager
  │    │    └─ StaggeredGridLayoutManager
  │    │
  │    └─ Adapter (binds data to views)
  │         │
  │         ├─ onCreateViewHolder() - creates ViewHolder
  │         ├─ onBindViewHolder() - binds data to ViewHolder
  │         └─ getItemCount() - returns list size
  │              │
  │              └─ ViewHolder (caches view references)
  │                   └─ itemView.findViewById() - called once only
```

### Adapter Implementation

```java
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<ScanRecord> scans;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ScanRecord scan);
    }

    public HistoryAdapter(List<ScanRecord> scans, OnItemClickListener listener) {
        this.scans = scans;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout ONCE per ViewHolder
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_scan_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Bind data to existing ViewHolder (called many times)
        ScanRecord scan = scans.get(position);
        holder.bind(scan, listener);
    }

    @Override
    public int getItemCount() {
        return scans != null ? scans.size() : 0;
    }

    public void setScans(List<ScanRecord> scans) {
        this.scans = scans;
        notifyDataSetChanged();  // Refresh UI
    }

    // ViewHolder inner class
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView diseaseTextView;
        private TextView confidenceTextView;
        private TextView timestampTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            // findViewById called ONCE per ViewHolder
            diseaseTextView = itemView.findViewById(R.id.diseaseTextView);
            confidenceTextView = itemView.findViewById(R.id.confidenceTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }

        public void bind(ScanRecord scan, OnItemClickListener listener) {
            diseaseTextView.setText(scan.getDisease());
            confidenceTextView.setText(String.format("%.1f%%", scan.getConfidence() * 100));

            // Format timestamp
            Date date = new Date(scan.getTimestamp());
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            timestampTextView.setText(sdf.format(date));

            // Click listener
            itemView.setOnClickListener(v -> listener.onItemClick(scan));
        }
    }
}
```

### ViewHolder Pattern Benefits

**Without ViewHolder (inefficient):**
```java
// Called every time item scrolls into view
TextView tv = view.findViewById(R.id.textView);  // Slow!
```

**With ViewHolder (efficient):**
```java
// findViewById called ONCE when ViewHolder created
// ViewHolder reused for all items
```

**Performance gain:** 10x faster scrolling with large lists

---

## 7. Threading and Database Operations

### Why Threading Matters

**Android Main Thread Rules:**
1. Handles all UI rendering
2. Processes touch events
3. Must complete work in <16ms per frame (60 FPS)

**Database operations are slow:**
- Reading 100 records: ~50ms
- Writing 10 records: ~20ms
- Complex queries: 100+ ms

**Blocking main thread = frozen UI = bad UX**

### ExecutorService for Database Operations

```java
public class DatabaseRepository {
    private AppDatabase database;
    private ExecutorService executor;

    public DatabaseRepository(Context context) {
        database = AppDatabase.getInstance(context);
        executor = Executors.newSingleThreadExecutor();
    }

    // Insert on background thread
    public void insertScan(ScanRecord scan, OnCompleteListener listener) {
        executor.execute(() -> {
            database.scanDao().insert(scan);

            // Return to main thread for UI update
            new Handler(Looper.getMainLooper()).post(() -> {
                listener.onComplete();
            });
        });
    }

    // Query on background thread
    public void getAllScans(OnScansLoadedListener listener) {
        executor.execute(() -> {
            List<ScanRecord> scans = database.scanDao().getAll();

            // Return to main thread
            new Handler(Looper.getMainLooper()).post(() -> {
                listener.onScansLoaded(scans);
            });
        });
    }

    public interface OnCompleteListener {
        void onComplete();
    }

    public interface OnScansLoadedListener {
        void onScansLoaded(List<ScanRecord> scans);
    }
}
```

### Usage in Activity

```java
DatabaseRepository repository = new DatabaseRepository(this);

// Insert
repository.insertScan(scan, () -> {
    Toast.makeText(this, "Scan saved", Toast.LENGTH_SHORT).show();
});

// Query
repository.getAllScans(scans -> {
    adapter.setScans(scans);
});
```

---

## 8. CRUD Operations Implementation

### Create (Insert)

```java
// Single insert
ScanRecord scan = new ScanRecord(
    "Tomato Late Blight",
    0.87,
    "Brown spots on leaves",
    "Apply copper fungicide",
    System.currentTimeMillis(),
    "/path/to/image.jpg"
);
database.scanDao().insert(scan);

// Bulk insert
ScanRecord[] scans = {...};
database.scanDao().insertAll(scans);
```

### Read (Query)

```java
// Get all
List<ScanRecord> allScans = database.scanDao().getAll();

// Get by ID
ScanRecord scan = database.scanDao().getById(5);

// Conditional query
List<ScanRecord> filtered = database.scanDao()
    .findByDisease("Late Blight");
```

### Update

```java
// Modify object
scan.setConfidence(0.95);

// Update in database (matches by primary key)
database.scanDao().update(scan);
```

### Delete

```java
// Delete specific scan
database.scanDao().delete(scan);

// Delete by ID
database.scanDao().deleteById(5);

// Delete all
database.scanDao().deleteAll();
```

---

## 9. Data Lifecycle Management

### When to Save Data

```
User Flow                      Save Point
─────────────────────────────────────────
Camera → Predict → Result  →   ✅ Save after successful prediction
                            →   Display in ResultActivity
                            →   Insert into database

View History               →   ❌ Don't save (reading only)

Delete Scan                →   ✅ Delete from database
                            →   Remove from list UI
```

### Data Persistence Scenarios

#### Scenario 1: App Closed
- Database file remains in `/data/data/package/databases/`
- Data **persists** across app restarts

#### Scenario 2: Device Reboot
- Database file survives reboot
- Data **persists** across device restarts

#### Scenario 3: App Uninstalled
- Android deletes `/data/data/package/` directory
- Data **lost** forever (cannot recover)

#### Scenario 4: App Updated
- Database file preserved
- If schema changes, migration needed (or destructive fallback)

---

## 10. CSE 2206 Exam Preparation

### Key Concepts to Explain

#### 1. Room Architecture

**Q: Explain Room's three components.**

**A:** Room has three components: Entity, DAO, and Database. Entity classes represent database tables with @Entity annotation, defining columns as fields. DAO (Data Access Object) interfaces define database operations using @Insert, @Query, @Update, @Delete annotations. Database class extends RoomDatabase, manages database creation, and provides DAO instances. Room generates implementation code at compile-time, ensuring type safety and reducing boilerplate.

#### 2. ViewHolder Pattern

**Q: Why does RecyclerView use ViewHolder pattern?**

**A:** ViewHolder caches view references to avoid repeated findViewById() calls. When RecyclerView displays 1000 items, without ViewHolder, findViewById() is called 1000+ times (very slow). With ViewHolder, findViewById() is called only ~10 times (number of visible items), then ViewHolders are recycled as items scroll. This improves performance by 10x, enabling smooth 60 FPS scrolling.

#### 3. Threading Requirements

**Q: Why can't database operations run on main thread?**

**A:** The main thread handles UI rendering at 60 FPS (16ms per frame). Database operations take 20-100ms, blocking UI and causing freezing. Android enforces this with exceptions in debug builds. Solution: use ExecutorService or AsyncTask to run database operations on background thread, then post results back to main thread for UI updates.

### Sample Viva Questions & Answers

**Q1: How does Room differ from SQLite?**

**A:** SQLite is the underlying database engine. Room is an abstraction library providing compile-time query verification, automatic object mapping, and reduced boilerplate. With raw SQLite, I'd manually create ContentValues, write SQL strings, parse Cursors—prone to runtime errors. Room uses annotations (@Entity, @Dao) to generate this code automatically with compile-time checking, preventing SQL errors before runtime.

**Q2: What happens if you forget @PrimaryKey?**

**A:** Room requires every entity to have at least one field marked @PrimaryKey. Without it, compilation fails with error: "An entity must have at least 1 primary key." Primary key uniquely identifies each record and is essential for update/delete operations which match by primary key.

**Q3: Explain the singleton pattern in AppDatabase.**

**A:** Singleton ensures only one database instance exists throughout app lifecycle. Database creation is expensive (file I/O, schema setup). Multiple instances cause SQLite locking conflicts. Singleton uses static instance variable, synchronized getInstance() method to prevent race conditions in multi-threaded access. First call creates database, subsequent calls return existing instance.

**Q4: How do you handle database schema changes?**

**A:** Increment database version number in @Database annotation. Provide Migration object mapping old schema to new. Migration contains SQL statements (ALTER TABLE, ADD COLUMN) to modify existing database. During development, use fallbackToDestructiveMigration() which deletes old database and creates new one—acceptable for dev but loses user data in production.

**Q5: Why use RecyclerView instead of ListView?**

**A:** RecyclerView enforces ViewHolder pattern (optional in ListView), ensuring better performance. RecyclerView supports multiple layout managers (Linear, Grid, Staggered) while ListView only supports vertical lists. RecyclerView has item animations built-in. RecyclerView is more flexible for custom layouts and is the modern Android standard recommended by Google.

---

## Summary

### What You Learned This Week

1. **SQLite Fundamentals:** Embedded database, file-based storage, ACID properties
2. **Room Architecture:** Entity, DAO, Database three-component pattern
3. **Entity Design:** Annotations, primary keys, type converters
4. **DAO Patterns:** Insert, Query, Update, Delete operations
5. **RoomDatabase:** Singleton pattern, builder configuration
6. **RecyclerView:** Adapter pattern, ViewHolder optimization
7. **Threading:** ExecutorService for background database operations
8. **CRUD Operations:** Complete database lifecycle management
9. **Data Persistence:** Understanding when data survives vs. lost

### Key Takeaways

- **Always use Room over raw SQLite** (compile-time safety, less code)
- **Never run database operations on main thread** (use ExecutorService)
- **Singleton pattern for database** (one instance per app)
- **ViewHolder pattern mandatory** (10x performance gain)
- **Auto-increment primary keys** (let Room manage IDs)
- **Type converters for complex types** (Date → long conversion)

### Next Steps

- Complete Week 07 exercises to practice these concepts
- Implement build task step-by-step
- Test thoroughly with 20+ scan records
- Move to Week 08: XML disease library parsing

---

**Now you have solid theoretical foundation for Room database. Practice with exercises to build muscle memory!**

---

## 11. SQL Query Optimization and Indexing

Database performance matters when a user has 1000+ scan records. Slow queries cause visible lag in RecyclerView scrolling.

### Understanding Query Execution

SQLite executes queries by scanning tables. Without indexes, every query scans every row:

```
getAllScans() with 1000 records:
  Scan row 1:  timestamp = 2024-06-01 ...
  Scan row 2:  timestamp = 2024-06-02 ...
  ...
  Scan row 1000: timestamp = 2024-06-03 ...
  Sort all 1000 results by timestamp
  Total: O(n log n) operations
```

With 100,000 records this becomes visibly slow (>200ms).

### Adding Indexes with Room

An index is a pre-sorted copy of a column, enabling O(log n) lookups instead of O(n) scans:

```java
// In ScanRecord.java entity
@Entity(
    tableName = "scan_history",
    indices = {
        @Index(value = {"timestamp"}, name = "index_scan_timestamp"),
        @Index(value = {"disease_name"}, name = "index_scan_disease")
    }
)
public class ScanRecord {
    // ... fields
}
```

**When to add indexes**:
- Columns in `ORDER BY` clauses (timestamp)
- Columns in `WHERE` clauses with frequent searches (disease_name)
- Foreign key columns (if you add relationships)

**When NOT to add indexes**:
- Every column (indexes slow down INSERT/UPDATE as they must be maintained)
- Rarely queried columns (overhead not worth it)

### Using EXPLAIN QUERY PLAN

Debug slow queries with SQLite's query planner:

```sql
EXPLAIN QUERY PLAN 
SELECT * FROM scan_history ORDER BY timestamp DESC;

-- Without index: "SCAN TABLE scan_history"  (bad — O(n))
-- With index:    "SEARCH TABLE scan_history USING INDEX index_scan_timestamp"  (good — O(log n))
```

### Efficient Query Patterns

**Pagination with LIMIT and OFFSET** (for infinite scroll):

```java
// DAO method for paginated loading
@Query("SELECT * FROM scan_history ORDER BY timestamp DESC LIMIT :pageSize OFFSET :offset")
List<ScanRecord> getScansPage(int pageSize, int offset);

// Usage in Activity:
// Page 1: getScansPage(20, 0)   → records 1-20
// Page 2: getScansPage(20, 20)  → records 21-40
```

**Counting with COUNT** (faster than fetching all rows):

```java
@Query("SELECT COUNT(*) FROM scan_history")
int getTotalScanCount();

@Query("SELECT COUNT(*) FROM scan_history WHERE disease_name LIKE '%healthy%'")
int getHealthyScanCount();
```

**Aggregation queries** (statistics screen):

```java
@Query("SELECT AVG(confidence) FROM scan_history")
float getAverageConfidence();

@Query("SELECT disease_name, COUNT(*) as scan_count FROM scan_history GROUP BY disease_name ORDER BY scan_count DESC LIMIT 5")
List<DiseaseCountResult> getTopDiseases();
```

For the `getTopDiseases()` result, create a simple POJO:

```java
// DiseaseCountResult.java — no @Entity annotation (not a table, just a result)
public class DiseaseCountResult {
    public String disease_name;  // Must match SELECT column name
    public int scan_count;        // Must match SELECT column alias
}
```

### Search Query with LIKE

Full-text search across disease names:

```java
@Query("SELECT * FROM scan_history WHERE disease_name LIKE '%' || :searchTerm || '%' ORDER BY timestamp DESC")
List<ScanRecord> searchByDisease(String searchTerm);
```

**Warning**: `LIKE '%prefix%'` is slow on large tables (can't use index effectively). For large datasets, consider SQLite's FTS5 (Full Text Search) extension, though Room does not support it directly through annotations.

---

## 12. Room Database Testing with In-Memory Database

Testing Room DAOs is easy because SQLite can run entirely in memory (no file I/O, tests run fast, database is empty at start of each test).

### Setup: In-Memory Database in JUnit Tests

```java
// ScanDaoTest.java (in src/test or src/androidTest)
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScanDaoTest {

    private AppDatabase database;
    private ScanDao dao;

    @Before
    public void setup() {
        // In-memory database: data lost after each test (intentional)
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase.class
        ).allowMainThreadQueries()  // Only for testing! Never in production.
         .build();
        dao = database.scanDao();
    }

    @After
    public void teardown() {
        database.close();
    }

    @Test
    public void testInsertAndRetrieve() {
        // Given
        ScanRecord record = new ScanRecord();
        record.setDiseaseName("Tomato___Early_blight");
        record.setConfidence(0.91f);
        record.setTimestamp(System.currentTimeMillis());

        // When
        long id = dao.insertScan(record);

        // Then
        List<ScanRecord> all = dao.getAllScans();
        assertEquals(1, all.size());
        assertEquals("Tomato___Early_blight", all.get(0).getDiseaseName());
        assertTrue(id > 0);
    }

    @Test
    public void testGetAllScans_orderedByTimestampDesc() {
        // Insert three records with different timestamps
        insertScanAtTime("Disease A", System.currentTimeMillis() - 2000);
        insertScanAtTime("Disease B", System.currentTimeMillis() - 1000);
        insertScanAtTime("Disease C", System.currentTimeMillis());

        List<ScanRecord> result = dao.getAllScans();

        assertEquals(3, result.size());
        assertEquals("Disease C", result.get(0).getDiseaseName());  // Newest first
        assertEquals("Disease A", result.get(2).getDiseaseName());  // Oldest last
    }

    @Test
    public void testDelete() {
        ScanRecord record = new ScanRecord();
        record.setDiseaseName("Test Disease");
        record.setTimestamp(System.currentTimeMillis());
        long id = dao.insertScan(record);

        dao.deleteScanById(id);

        assertNull(dao.getScanById(id));
        assertEquals(0, dao.getAllScans().size());
    }

    @Test
    public void testGetScanById_returnsNullForMissingId() {
        assertNull(dao.getScanById(99999L));
    }

    private void insertScanAtTime(String diseaseName, long timestamp) {
        ScanRecord record = new ScanRecord();
        record.setDiseaseName(diseaseName);
        record.setTimestamp(timestamp);
        record.setConfidence(0.80f);
        dao.insertScan(record);
    }
}
```

**Key points**:
- `inMemoryDatabaseBuilder` creates a fresh, empty database for each test
- `allowMainThreadQueries()` disables the thread check — necessary in tests but forbidden in production
- `@Before` creates the database, `@After` closes it
- Tests are independent — each test starts with an empty database

### Testing with AndroidJUnit4

Run these tests as instrumented tests on an Android device/emulator:

```java
@RunWith(AndroidJUnit4.class)
public class ScanDaoTest {
    // ... same test code
}
```

Or as local unit tests with Robolectric (faster, runs on JVM):

```groovy
// build.gradle
testImplementation "org.robolectric:robolectric:4.11.1"
testImplementation "androidx.room:room-testing:2.6.0"
```

---

## 13. LiveData and Reactive Database Queries

**LiveData** is an observable data holder. When the database changes, all active observers are automatically notified and the UI updates without polling.

### LiveData vs Regular List

**Regular query** (current implementation — requires manual refresh):

```java
// DAO
@Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
List<ScanRecord> getAllScans();

// Activity — must call loadHistory() every time data might change
private void loadHistory() {
    executor.execute(() -> {
        List<ScanRecord> scans = dao.getAllScans();  // One-time read
        runOnUiThread(() -> adapter.submitList(scans));
    });
}
```

**Problem**: If another part of the app inserts a new scan, `HistoryActivity` doesn't know — the user must manually pull-to-refresh or re-enter the activity.

**LiveData query** (reactive — automatically updates):

```java
// DAO
@Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
LiveData<List<ScanRecord>> getAllScansLive();  // Note: LiveData return type

// Activity
private void observeHistory() {
    dao.getAllScansLive().observe(this, scans -> {
        // Called automatically whenever the table changes
        adapter.submitList(scans);
        boolean hasItems = scans != null && !scans.isEmpty();
        recyclerView.setVisibility(hasItems ? View.VISIBLE : View.GONE);
        emptyText.setVisibility(hasItems ? View.GONE : View.VISIBLE);
    });
}
```

**LiveData lifecycle**: Room automatically runs the query on a background thread, posts results to the main thread, and stops observing when the Activity/Fragment is destroyed. No manual thread management needed.

### LiveData Architecture Pattern

```
DAO (LiveData source)
        │
        ▼
Repository (optional business logic layer)
        │
        ▼
ViewModel (survives screen rotation, holds LiveData)
        │
        ▼
Activity/Fragment (observes LiveData, updates UI)
```

### Adding ViewModel for LiveData

```java
// ScanRecordViewModel.java
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

public class ScanRecordViewModel extends ViewModel {

    private final ScanDao scanDao;
    private final LiveData<List<ScanRecord>> allScans;

    public ScanRecordViewModel(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        scanDao = db.scanDao();
        allScans = scanDao.getAllScansLive();
    }

    public LiveData<List<ScanRecord>> getAllScans() {
        return allScans;
    }
}
```

```java
// Updated HistoryActivity.java
private ScanRecordViewModel viewModel;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // ...

    viewModel = new ViewModelProvider(this).get(ScanRecordViewModel.class);
    viewModel.getAllScans().observe(this, scans -> {
        adapter.submitList(scans);
    });
}
// No need for ExecutorService, loadHistory(), or runOnUiThread()!
```

**Key advantage**: If the user saves a scan in `ResultActivity`, the `HistoryActivity` (if open in the back stack) will automatically show the new record.

### LiveData vs Flow (Advanced)

For completeness, Kotlin Coroutines offers **Flow**, which is LiveData's modern replacement:

| Feature | LiveData | Kotlin Flow |
|---------|----------|-------------|
| Language | Java and Kotlin | Kotlin only |
| Threading | Room handles it | Need `flowOn(Dispatchers.IO)` |
| Operators | Limited | Rich (map, filter, combine) |
| Backpressure | No | Yes |
| CSE 2206 focus | ✅ Use this | Advanced—optional |

Since LeafGuard AI uses Java, **LiveData is the appropriate choice** for this course.

---

## 14. Database Migrations

When you update your app and add new columns or tables, users who already have the old database version need their data migrated safely.

### Version Numbers

Every Room database has a version number. Increment it whenever you change the schema:

```java
@Database(
    entities = {ScanRecord.class},
    version = 2,          // ← Incremented from 1
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    // ...
}
```

If you increment the version without providing a migration, Room will throw `IllegalStateException` at runtime.

### Writing a Migration

```java
// Migration from version 1 to version 2
// Added: latitude and longitude columns to scan_history
static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
        // Add new columns with DEFAULT values (required for existing rows)
        database.execSQL(
            "ALTER TABLE scan_history ADD COLUMN latitude REAL NOT NULL DEFAULT 0.0"
        );
        database.execSQL(
            "ALTER TABLE scan_history ADD COLUMN longitude REAL NOT NULL DEFAULT 0.0"
        );
    }
};
```

**Register migration in the database builder**:

```java
// AppDatabase.java
public static AppDatabase getInstance(Context context) {
    if (INSTANCE == null) {
        synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "leafguard.db"
                )
                .addMigrations(MIGRATION_1_2)  // ← Register migration
                .build();
            }
        }
    }
    return INSTANCE;
}
```

### What SQLite ALTER TABLE Can and Cannot Do

| Operation | Supported? | Workaround |
|-----------|-----------|------------|
| Add column | ✅ Yes | — |
| Rename column | ✅ Yes (SQLite 3.25+) | — |
| Drop column | ⚠️ SQLite 3.35+ only | Create new table, copy data, drop old |
| Change column type | ❌ No | Create new table, copy data, drop old |
| Add NOT NULL without DEFAULT | ❌ No | Add with DEFAULT, then update |

**Migration that requires table recreation** (advanced):

```java
static final Migration MIGRATION_2_3 = new Migration(2, 3) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {
        // Step 1: Create new table with desired schema
        database.execSQL(
            "CREATE TABLE scan_history_new (" +
            "  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "  disease_name TEXT NOT NULL DEFAULT ''," +
            "  confidence REAL NOT NULL DEFAULT 0," +
            "  timestamp INTEGER NOT NULL DEFAULT 0," +
            "  image_uri TEXT" +  // ← New non-null column (impossible with ALTER)
            ")"
        );
        // Step 2: Copy data
        database.execSQL(
            "INSERT INTO scan_history_new (id, disease_name, confidence, timestamp) " +
            "SELECT id, disease_name, confidence, timestamp FROM scan_history"
        );
        // Step 3: Drop old table and rename
        database.execSQL("DROP TABLE scan_history");
        database.execSQL("ALTER TABLE scan_history_new RENAME TO scan_history");
    }
};
```

### Fallback: Destructive Migration (Development Only)

During development (not in production — destroys user data!):

```java
INSTANCE = Room.databaseBuilder(...)
        .fallbackToDestructiveMigration()  // Wipe and recreate on version mismatch
        .build();
```

**Never use `fallbackToDestructiveMigration()` in a released app** — it deletes all user data on update.

---

## 15. CSE 2206 Exam Preparation: Advanced Questions

These questions test deeper understanding beyond basic Room API usage.

### Q: Why can't Room queries run on the main thread?

**Answer**: Android's main thread (UI thread) must respond to user interactions within 16ms per frame (60 fps). A database query scanning 1000+ records can take 50-200ms. If the main thread is blocked, the UI freezes — this triggers Android's "Application Not Responding" (ANR) dialog after 5 seconds. Room enforces this rule at compile time when you use `LiveData`, and at runtime when you run queries synchronously (throws `IllegalStateException: Cannot access database on the main thread`).

### Q: What is the difference between @Insert and @Query for inserting data?

```java
// Option 1: @Insert annotation (preferred for simple inserts)
@Insert(onConflict = OnConflictStrategy.REPLACE)
long insertScan(ScanRecord record);

// Option 2: @Query annotation (needed for conditional inserts)
@Query("INSERT INTO scan_history (disease_name, confidence) VALUES (:disease, :confidence)")
void insertRaw(String disease, float confidence);
```

`@Insert` is preferred — Room generates optimal SQL, handles conflicts declaratively, and returns the new ID automatically.

### Q: Explain the Singleton pattern in AppDatabase

If multiple instances of `AppDatabase` were created, each would have its own database connection. SQLite allows only one writer at a time — multiple connections cause lock conflicts and data corruption. The Singleton pattern ensures only one `AppDatabase` object exists per app process, preventing these conflicts. The `synchronized` keyword makes it thread-safe if two threads try to create the instance simultaneously.

### Q: What is OnConflictStrategy.REPLACE vs IGNORE?

| Strategy | Behavior |
|----------|----------|
| `REPLACE` | Delete the conflicting old row, insert the new row. Old ID is lost. |
| `IGNORE` | Keep the old row, silently drop the new insert. |
| `ABORT` (default) | Roll back the transaction, throw an exception. |

For LeafGuard AI scan history, `REPLACE` makes sense: if somehow the same record is saved twice (network retry), the latest version wins.

### Q: How does RecyclerView's ViewHolder improve performance?

Without ViewHolder, `onBindViewHolder()` would call `itemView.findViewById(R.id.textDisease)` every time a list item is scrolled into view. `findViewById` traverses the entire view hierarchy tree to find the view — O(n) where n = number of views in the item layout. With ViewHolder, references are looked up once in `onCreateViewHolder()` and stored as fields. `onBindViewHolder()` then directly sets text on the cached reference — O(1). For a list scrolling at 60fps with 20 visible items, this eliminates 20 × 60 = 1,200 `findViewById` calls per second.

---


<!-- NAV_FOOTER_START -->

---

## 📚 Week 07 — Navigation

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
| [⬅ Week 06: Cloud ML Model](../week-06-cloud-ml-model/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 08: XML Disease Library ➡](../week-08-xml-disease-library/README.md) |

---
