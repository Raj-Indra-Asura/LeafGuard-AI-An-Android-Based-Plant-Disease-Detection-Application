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


---

## Exercise 7: LiveData + Observer Pattern for Real-Time History Updates

**Objective:** Observe Room data from `HistoryActivity` so the RecyclerView updates automatically whenever a scan is inserted or deleted.

### Why This Matters

Polling the database manually is wasteful. `LiveData` gives you an observable stream connected to the Activity lifecycle. When the database changes, Room re-runs the query and pushes the new list to your UI.

### Step 1: Update DAO to Return `LiveData`

```java
@Dao
public interface ScanHistoryDao {

    @Insert
    void insert(ScanHistory scanHistory);

    @Delete
    void delete(ScanHistory scanHistory);

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    LiveData<List<ScanHistory>> getAllLive();
}
```

### Step 2: Add an `updateData()` Method in Adapter

```java
public class ScanHistoryAdapter extends RecyclerView.Adapter<ScanHistoryAdapter.ScanViewHolder> {

    private final List<ScanHistory> scanList = new ArrayList<>();

    public void updateData(List<ScanHistory> newList) {
        scanList.clear();
        if (newList != null) {
            scanList.addAll(newList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scan_history, parent, false);
        return new ScanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanViewHolder holder, int position) {
        ScanHistory scan = scanList.get(position);
        holder.tvDisease.setText(scan.getDisease());
        holder.tvConfidence.setText(String.format(Locale.US, "%.2f%%", scan.getConfidence() * 100));
        holder.tvTimestamp.setText(String.valueOf(scan.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return scanList.size();
    }

    static class ScanViewHolder extends RecyclerView.ViewHolder {
        TextView tvDisease;
        TextView tvConfidence;
        TextView tvTimestamp;

        ScanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDisease = itemView.findViewById(R.id.tvDisease);
            tvConfidence = itemView.findViewById(R.id.tvConfidence);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}
```

### Step 3: Observe LiveData in `HistoryActivity`

```java
public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private ScanHistoryAdapter adapter;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerViewHistory);
        tvEmpty = findViewById(R.id.tvEmptyState);

        adapter = new ScanHistoryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        database = AppDatabase.getInstance(getApplicationContext());

        database.scanHistoryDao().getAllLive().observe(this, scanHistoryList -> {
            adapter.updateData(scanHistoryList);

            boolean isEmpty = scanHistoryList == null || scanHistoryList.isEmpty();
            tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        });
    }
}
```

### Expected Output

- history screen updates automatically after insert or delete
- no manual refresh button is needed
- empty-state message appears when there are no scans

### Verification

- [ ] DAO returns `LiveData<List<ScanHistory>>`
- [ ] Activity observes LiveData with `observe(this, ...)`
- [ ] RecyclerView refreshes after a new scan is saved
- [ ] Empty-state label works correctly

---

## Exercise 8: Delete with Confirmation on Long Press

**Objective:** Let users long-press a history item, confirm the action, and then delete the row from Room.

### Step 1: Create a Long-Click Interface in the Adapter

```java
public class ScanHistoryAdapter extends RecyclerView.Adapter<ScanHistoryAdapter.ScanViewHolder> {

    public interface OnScanLongClickListener {
        void onScanLongClick(ScanHistory scanHistory);
    }

    private final List<ScanHistory> scanList = new ArrayList<>();
    private final OnScanLongClickListener longClickListener;

    public ScanHistoryAdapter(OnScanLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ScanViewHolder holder, int position) {
        ScanHistory scan = scanList.get(position);
        holder.tvDisease.setText(scan.getDisease());
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onScanLongClick(scan);
            return true;
        });
    }

    public void updateData(List<ScanHistory> newList) {
        scanList.clear();
        if (newList != null) {
            scanList.addAll(newList);
        }
        notifyDataSetChanged();
    }

    // onCreateViewHolder(), getItemCount(), and ViewHolder are the same as before.
}
```

### Step 2: Show AlertDialog from `HistoryActivity`

```java
public class HistoryActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private AppDatabase database;
    private ScanHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        database = AppDatabase.getInstance(getApplicationContext());

        adapter = new ScanHistoryAdapter(this::showDeleteDialog);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        database.scanHistoryDao().getAllLive().observe(this, adapter::updateData);
    }

    private void showDeleteDialog(ScanHistory scanHistory) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Scan?")
                .setMessage("Delete the scan result for " + scanHistory.getDisease() + "?")
                .setPositiveButton("Delete", (dialog, which) -> deleteScan(scanHistory))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteScan(ScanHistory scanHistory) {
        executorService.execute(() -> {
            database.scanHistoryDao().delete(scanHistory);
            runOnUiThread(() ->
                    Toast.makeText(this, "Scan deleted", Toast.LENGTH_SHORT).show()
            );
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
```

### Expected Output

- long-press shows a confirmation dialog
- tapping **Cancel** closes the dialog only
- tapping **Delete** removes the row from Room
- LiveData refresh makes the RecyclerView update automatically

### Verification

- [ ] Long press is wired in adapter
- [ ] AlertDialog appears with delete and cancel actions
- [ ] Room delete runs on background thread
- [ ] Toast confirms deletion
- [ ] Deleted row disappears from list

---

## Exercise 9: Search and Filter Scan History by Disease Name

**Objective:** Add a search bar so the student can filter history records by disease name.

### Step 1: Add Search Query to DAO

```java
@Dao
public interface ScanHistoryDao {

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    LiveData<List<ScanHistory>> getAllLive();

    @Query("SELECT * FROM scan_history WHERE disease LIKE :searchQuery ORDER BY timestamp DESC")
    List<ScanHistory> searchByDisease(String searchQuery);
}
```

### Why `LIKE` Uses `%`

If the user types `blight`, then the SQL pattern should become:

```text
%blight%
```

That means:
- anything before `blight`
- the word `blight`
- anything after `blight`

So `Late Blight`, `Blight Stage 2`, and `Possible blight disease` all match.

### Step 2: Add SearchView to Toolbar Menu

```xml
<item
    android:id="@+id/action_search"
    android:title="Search"
    android:icon="@android:drawable/ic_menu_search"
    app:showAsAction="ifRoom|collapseActionView"
    app:actionViewClass="androidx.appcompat.widget.SearchView" />
```

### Step 3: Query Room When Text Changes

```java
public class HistoryActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private AppDatabase database;
    private ScanHistoryAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search disease name");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterHistory(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterHistory(newText);
                return true;
            }
        });

        return true;
    }

    private void filterHistory(String query) {
        if (query == null || query.trim().isEmpty()) {
            database.scanHistoryDao().getAllLive().observe(this, adapter::updateData);
            return;
        }

        String searchPattern = "%" + query.trim() + "%";
        executorService.execute(() -> {
            List<ScanHistory> result = database.scanHistoryDao().searchByDisease(searchPattern);
            runOnUiThread(() -> adapter.updateData(result));
        });
    }
}
```

### Better Design Note

For a larger app, you would keep search state in a `ViewModel` and avoid repeatedly adding observers inside `filterHistory()`. For Week 07, the goal is understanding Room queries and UI filtering, so the code above is acceptable as a learning step.

### Verification

- [ ] SearchView appears in toolbar
- [ ] `LIKE` query returns matching disease names
- [ ] Typing filters the list
- [ ] Clearing query shows all results again

---

## Exercise 10: Export Scan History to CSV

**Objective:** Write all scan history rows to a `.csv` file using `FileWriter` and save it in the app's Downloads directory.

### Why Use CSV?

CSV files are easy to:
- open in Excel or Google Sheets
- attach as project evidence
- inspect during viva or report writing

### Step 1: Query All Rows Before Export

```java
@Dao
public interface ScanHistoryDao {

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    List<ScanHistory> getAllForExport();
}
```

### Step 2: Create an Export Helper Method

```java
private final ExecutorService executorService = Executors.newSingleThreadExecutor();

private void exportHistoryToCsv() {
    executorService.execute(() -> {
        List<ScanHistory> scanHistoryList = database.scanHistoryDao().getAllForExport();

        File downloadsDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (downloadsDir == null) {
            runOnUiThread(() -> Toast.makeText(this, "Downloads directory unavailable", Toast.LENGTH_SHORT).show());
            return;
        }

        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }

        File csvFile = new File(downloadsDir, "leafguard_history.csv");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.append("id,disease,confidence,timestamp
");

            for (ScanHistory scan : scanHistoryList) {
                writer.append(String.valueOf(scan.getId())).append(",")
                        .append(escapeCsv(scan.getDisease())).append(",")
                        .append(String.valueOf(scan.getConfidence())).append(",")
                        .append(dateFormat.format(new Date(scan.getTimestamp())))
                        .append("
");
            }

            writer.flush();

            runOnUiThread(() -> Toast.makeText(
                    this,
                    "CSV exported to: " + csvFile.getAbsolutePath(),
                    Toast.LENGTH_LONG
            ).show());
        } catch (IOException e) {
            runOnUiThread(() -> Toast.makeText(this, "CSV export failed", Toast.LENGTH_SHORT).show());
            Log.e("HistoryActivity", "exportHistoryToCsv failed", e);
        }
    });
}

private String escapeCsv(String value) {
    if (value == null) {
        return "";
    }
    return '"' + value.replace(""", """") + '"';
}
```

### Scoped Storage Note

This example writes into the app-specific Downloads directory returned by `getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)`. That is the safest beginner option because it does not require broad storage permission on modern Android.

### Verification

- [ ] CSV file is created successfully
- [ ] File contains header row
- [ ] All database rows are written
- [ ] Disease names with commas are escaped safely
- [ ] Toast shows saved path

---

## Exercise 11: Add a Room Migration for `plant_type`

**Objective:** Add a new column called `plant_type` without destroying existing user data.

### Step 1: Update the Entity Class

```java
@Entity(tableName = "scan_history")
public class ScanHistory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String disease;
    private double confidence;
    private long timestamp;

    @ColumnInfo(name = "plant_type", defaultValue = "Unknown")
    private String plantType;

    public ScanHistory(String disease, double confidence, long timestamp, String plantType) {
        this.disease = disease;
        this.confidence = confidence;
        this.timestamp = timestamp;
        this.plantType = plantType;
    }

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }
}
```

### Step 2: Increase Database Version

```java
@Database(entities = {ScanHistory.class}, version = 2, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScanHistoryDao scanHistoryDao();
}
```

### Step 3: Create Migration Object

```java
public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL(
                "ALTER TABLE scan_history ADD COLUMN plant_type TEXT NOT NULL DEFAULT 'Unknown'"
        );
    }
};
```

### Step 4: Register Migration in Builder

```java
public static AppDatabase getInstance(Context context) {
    if (instance == null) {
        synchronized (AppDatabase.class) {
            if (instance == null) {
                instance = Room.databaseBuilder(
                                context.getApplicationContext(),
                                AppDatabase.class,
                                "leafguard_database"
                        )
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
    }
    return instance;
}
```

### Why This Is Better Than `fallbackToDestructiveMigration()`

`fallbackToDestructiveMigration()` drops tables and recreates them. That is acceptable in a prototype, but it destroys scan history. A real migration preserves user data.

### Verification

- [ ] Entity includes new `plantType` field
- [ ] Room version changed from 1 to 2
- [ ] Migration object defined
- [ ] Builder registers migration
- [ ] Old users keep previous history rows after upgrade

---

## Exercise 12: Test Room DAO with an In-Memory Database

**Objective:** Write a Room DAO test that runs against a temporary in-memory database and verifies insert/query behavior.

### Why Use an In-Memory Database?

It gives you:
- a clean database for every test
- fast execution
- no pollution of your real app database file

### Full Instrumented Test Example

```java
@RunWith(AndroidJUnit4.class)
public class ScanHistoryDaoTest {

    private AppDatabase database;
    private ScanHistoryDao scanHistoryDao;

    @Before
    public void createDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        scanHistoryDao = database.scanHistoryDao();
    }

    @After
    public void closeDatabase() throws IOException {
        database.close();
    }

    @Test
    public void insertAndReadBackScanHistory() {
        ScanHistory scanHistory = new ScanHistory(
                "Late Blight",
                0.94,
                System.currentTimeMillis(),
                "Tomato"
        );

        scanHistoryDao.insert(scanHistory);
        List<ScanHistory> allRows = scanHistoryDao.getAll();

        assertNotNull(allRows);
        assertEquals(1, allRows.size());
        assertEquals("Late Blight", allRows.get(0).getDisease());
        assertEquals("Tomato", allRows.get(0).getPlantType());
    }

    @Test
    public void searchByDisease_returnsMatchingRowsOnly() {
        scanHistoryDao.insert(new ScanHistory("Late Blight", 0.91, 1000L, "Tomato"));
        scanHistoryDao.insert(new ScanHistory("Powdery Mildew", 0.82, 2000L, "Cucumber"));

        List<ScanHistory> result = scanHistoryDao.searchByDisease("%Blight%");

        assertEquals(1, result.size());
        assertEquals("Late Blight", result.get(0).getDisease());
    }
}
```

### Notes About This Test

- `ApplicationProvider` gives a test-safe Context
- `allowMainThreadQueries()` is fine in tests only
- `@Before` creates a fresh database for each test
- `@After` closes it cleanly

### Verification

- [ ] Test class is under `androidTest/java`
- [ ] `Room.inMemoryDatabaseBuilder` is used
- [ ] Insert test passes
- [ ] Query or search test passes
- [ ] Database closes after test

---

## Exercise Completion Checklist

After completing all exercises:

- [ ] Exercise 1: Entity created with Room dependencies
- [ ] Exercise 2: DAO interface with CRUD methods
- [ ] Exercise 3: AppDatabase with singleton
- [ ] Exercise 4: Insert and query tested successfully
- [ ] Exercise 5: RecyclerView displaying scans
- [ ] Exercise 6: Delete functionality working
- [ ] Exercise 7: LiveData observer updates RecyclerView automatically
- [ ] Exercise 8: Long-press delete with confirmation works
- [ ] Exercise 9: Search/filter by disease name works
- [ ] Exercise 10: CSV export generates a readable file
- [ ] Exercise 11: Migration preserves old data and adds `plant_type`
- [ ] Exercise 12: In-memory DAO tests pass

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
