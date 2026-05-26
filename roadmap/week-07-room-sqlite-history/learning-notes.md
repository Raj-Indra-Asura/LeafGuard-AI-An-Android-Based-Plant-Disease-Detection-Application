# Week 07: Learning Notes - Room Database and Scan History

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
  leafguard_database.db (SQLite file)
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
ScanHistory scan = new ScanHistory("Late Blight", 0.87, System.currentTimeMillis());
database.scanHistoryDao().insert(scan);
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
│  public class ScanHistory {         │
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
│  public interface ScanHistoryDao {  │
│      @Insert void insert(...);      │
│      @Query("...") List getAll();   │
│      @Delete void delete(...);      │
│  }                                  │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  3. DATABASE                        │
│  @Database(entities = {ScanHistory})│
│  public abstract class AppDatabase  │
│      extends RoomDatabase {         │
│      public abstract ScanHistoryDao │
│          scanHistoryDao();          │
│  }                                  │
└─────────────────────────────────────┘
```

---

## 3. Entity Classes Deep Dive

### Basic Entity Structure

```java
@Entity(tableName = "scan_history")
public class ScanHistory {
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
    public ScanHistory(String disease, double confidence, String symptoms,
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

@Database(entities = {ScanHistory.class}, version = 1)
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
public interface ScanHistoryDao {
    // Create
    @Insert
    void insert(ScanHistory scan);

    @Insert
    void insertAll(ScanHistory... scans);

    // Read
    @Query("SELECT * FROM scan_history ORDER BY scan_timestamp DESC")
    List<ScanHistory> getAll();

    @Query("SELECT * FROM scan_history WHERE id = :scanId")
    ScanHistory getById(int scanId);

    @Query("SELECT * FROM scan_history WHERE disease_name LIKE :disease")
    List<ScanHistory> findByDisease(String disease);

    // Update
    @Update
    void update(ScanHistory scan);

    // Delete
    @Delete
    void delete(ScanHistory scan);

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
void insert(ScanHistory scan);

@Insert
long insertAndReturnId(ScanHistory scan);  // Returns inserted ID

@Insert
List<Long> insertAll(ScanHistory... scans);  // Varargs

@Insert(onConflict = OnConflictStrategy.REPLACE)
void insertOrReplace(ScanHistory scan);  // Replace if exists
```

**OnConflictStrategy options:**
- `REPLACE`: Replace existing record
- `IGNORE`: Keep existing, ignore new
- `ABORT`: Cancel transaction (default)

#### @Update
```java
@Update
void update(ScanHistory scan);

@Update
int updateAndReturnCount(ScanHistory scan);  // Returns affected rows
```

**Note:** Update matches by primary key automatically.

#### @Delete
```java
@Delete
void delete(ScanHistory scan);

@Delete
int deleteAndReturnCount(ScanHistory scan);  // Returns deleted count
```

#### @Query
Most flexible - write custom SQL:

```java
@Query("SELECT * FROM scan_history WHERE confidence > :minConfidence")
List<ScanHistory> getHighConfidenceScans(double minConfidence);

@Query("SELECT disease_name, COUNT(*) as count FROM scan_history " +
       "GROUP BY disease_name ORDER BY count DESC")
List<DiseaseCount> getDiseaseStatistics();
```

**Parameter binding:** Use `:paramName` syntax

### LiveData and Observability (Advanced)

```java
@Dao
public interface ScanHistoryDao {
    @Query("SELECT * FROM scan_history ORDER BY scan_timestamp DESC")
    LiveData<List<ScanHistory>> getAllLive();
}

// In Activity
database.scanHistoryDao().getAllLive().observe(this, scans -> {
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
@Database(entities = {ScanHistory.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // Abstract method for each DAO
    public abstract ScanHistoryDao scanHistoryDao();

    // Singleton pattern
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "leafguard_database"
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
    entities = {ScanHistory.class, User.class},  // All entities
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
public class ScanHistoryAdapter extends RecyclerView.Adapter<ScanHistoryAdapter.ViewHolder> {

    private List<ScanHistory> scans;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ScanHistory scan);
    }

    public ScanHistoryAdapter(List<ScanHistory> scans, OnItemClickListener listener) {
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
        ScanHistory scan = scans.get(position);
        holder.bind(scan, listener);
    }

    @Override
    public int getItemCount() {
        return scans != null ? scans.size() : 0;
    }

    public void setScans(List<ScanHistory> scans) {
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

        public void bind(ScanHistory scan, OnItemClickListener listener) {
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
    public void insertScan(ScanHistory scan, OnCompleteListener listener) {
        executor.execute(() -> {
            database.scanHistoryDao().insert(scan);

            // Return to main thread for UI update
            new Handler(Looper.getMainLooper()).post(() -> {
                listener.onComplete();
            });
        });
    }

    // Query on background thread
    public void getAllScans(OnScansLoadedListener listener) {
        executor.execute(() -> {
            List<ScanHistory> scans = database.scanHistoryDao().getAll();

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
        void onScansLoaded(List<ScanHistory> scans);
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
ScanHistory scan = new ScanHistory(
    "Tomato Late Blight",
    0.87,
    "Brown spots on leaves",
    "Apply copper fungicide",
    System.currentTimeMillis(),
    "/path/to/image.jpg"
);
database.scanHistoryDao().insert(scan);

// Bulk insert
ScanHistory[] scans = {...};
database.scanHistoryDao().insertAll(scans);
```

### Read (Query)

```java
// Get all
List<ScanHistory> allScans = database.scanHistoryDao().getAll();

// Get by ID
ScanHistory scan = database.scanHistoryDao().getById(5);

// Conditional query
List<ScanHistory> filtered = database.scanHistoryDao()
    .findByDisease("Late Blight");
```

### Update

```java
// Modify object
scan.setConfidence(0.95);

// Update in database (matches by primary key)
database.scanHistoryDao().update(scan);
```

### Delete

```java
// Delete specific scan
database.scanHistoryDao().delete(scan);

// Delete by ID
database.scanHistoryDao().deleteById(5);

// Delete all
database.scanHistoryDao().deleteAll();
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
