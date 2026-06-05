# Week 07 Interactive Notebook

## Saving Scan History with Room Database

> Work through this Markdown notebook like a lab manual: read, run, test, and explain each checkpoint in your own words.

### How to use this notebook

- Follow the cells in order.
- Use Java for Android code and Python only for backend/model tooling.
- Save screenshots and logs as evidence for CSE 2206.
- Keep the roadmap folder for this week open while you work.

### Weekly outcomes

- Understand Room architecture using Entity, DAO, Database, and Repository layers.
- Store past scan results and display them in a RecyclerView-based history screen.
- Use LiveData so the UI updates automatically when history changes.

### Repository references

- `roadmap/week-07-room-sqlite-history/`
- `solutions/week-07/`
- `android-app/app/build.gradle`

---

## Notebook Cell 1 — See the Room architecture as a diagram

### Explanation

- Room is an abstraction layer over SQLite that uses Java classes instead of manual SQL everywhere.
- The data flow is Entity -> DAO -> Database -> Repository -> ViewModel/Activity -> RecyclerView.

### Code to Read / Run

```text
User completes a scan
    |
    v
ScanHistory entity object
    |
    v
ScanHistoryDao.insert(history)
    |
    v
LeafGuardDatabase singleton
    |
    v
Repository method returns LiveData<List<ScanHistory>>
    |
    v
HistoryActivity observes data and updates RecyclerView
```

### 🔵 Try This

- Draw this flow yourself and add where ResultActivity fits in.

### Expected Output

- You can describe which class owns persistence and which class owns UI.

### ✅ Checkpoint

- Why is a repository useful instead of calling DAO methods directly from every screen?

### ⚠️ Common Mistake

- Do not put SQL-like storage logic directly in an Activity.

### 📌 Key Point

- Room gives structure to local persistence, which is important for maintainability.

## Notebook Cell 2 — Create the ScanHistory entity

### Explanation

- An Entity represents one table row in the database.

### Code to Read / Run

```java
@Entity(tableName = "scan_history")
public class ScanHistory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    @ColumnInfo(name = "disease_name")
    private String diseaseName;

    @ColumnInfo(name = "confidence")
    private float confidence;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    public ScanHistory(String imagePath, String diseaseName, float confidence, long timestamp) {
        this.imagePath = imagePath;
        this.diseaseName = diseaseName;
        this.confidence = confidence;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getImagePath() { return imagePath; }
    public String getDiseaseName() { return diseaseName; }
    public float getConfidence() { return confidence; }
    public long getTimestamp() { return timestamp; }
}
```

### 🔵 Try This

- Add more columns later, such as location or prediction source.

### Expected Output

- The table design captures the minimum useful history record.

### ✅ Checkpoint

- Why is `timestamp` stored as a number instead of a formatted string?

### ⚠️ Common Mistake

- Do not forget a primary key; Room requires one.

### 📌 Key Point

- Start with the smallest useful schema and expand only when needed.

## Notebook Cell 3 — Implement the DAO interface

### Explanation

- DAO methods define the database operations in a clean Java interface.

### Code to Read / Run

```java
@Dao
public interface ScanHistoryDao {

    @Insert
    void insert(ScanHistory history);

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    LiveData<List<ScanHistory>> getAllHistory();

    @Query("DELETE FROM scan_history")
    void deleteAll();
}
```

### 🔵 Try This

- Explain what the SQL query does and why descending order is useful.

### Expected Output

- The DAO exposes insert and read operations for the history table.

### ✅ Checkpoint

- Why does `getAllHistory()` return `LiveData<List<ScanHistory>>`?

### ⚠️ Common Mistake

- Do not write long raw SQL strings everywhere when Room annotations can express the query clearly.

### 📌 Key Point

- DAO classes are the contract between Java objects and SQL operations.

## Notebook Cell 4 — Create the database singleton

### Explanation

- A singleton prevents multiple database instances from competing for resources.

### Code to Read / Run

```java
@Database(entities = {ScanHistory.class}, version = 1, exportSchema = false)
public abstract class LeafGuardDatabase extends RoomDatabase {

    public abstract ScanHistoryDao scanHistoryDao();

    private static volatile LeafGuardDatabase INSTANCE;

    public static LeafGuardDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LeafGuardDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            LeafGuardDatabase.class,
                            "leafguard_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
```

### 🔵 Try This

- Explain why `getApplicationContext()` is used inside the builder.

### Expected Output

- The app has one shared Room database instance.

### ✅ Checkpoint

- What problem could happen if every Activity created its own database instance?

### ⚠️ Common Mistake

- Avoid using an Activity context for long-lived singletons.

### 📌 Key Point

- Singleton scope fits a shared local database.

## Notebook Cell 5 — Add the repository pattern

### Explanation

- The repository hides data-source details from the UI layer and centralizes threading decisions.

### Code to Read / Run

```java
public class ScanHistoryRepository {
    private final ScanHistoryDao scanHistoryDao;
    private final LiveData<List<ScanHistory>> allHistory;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ScanHistoryRepository(Context context) {
        LeafGuardDatabase database = LeafGuardDatabase.getInstance(context);
        scanHistoryDao = database.scanHistoryDao();
        allHistory = scanHistoryDao.getAllHistory();
    }

    public LiveData<List<ScanHistory>> getAllHistory() {
        return allHistory;
    }

    public void insert(ScanHistory history) {
        executorService.execute(() -> scanHistoryDao.insert(history));
    }
}
```

### 🔵 Try This

- Call `insert` after a successful prediction and verify the UI later reflects the change.

### Expected Output

- Database writes happen off the main thread.

### ✅ Checkpoint

- Why should database inserts avoid the UI thread?

### ⚠️ Common Mistake

- Room will complain if you attempt database work on the main thread by default.

### 📌 Key Point

- Repository classes reduce repeated boilerplate across the app.

## Notebook Cell 6 — Build HistoryActivity with RecyclerView

### Explanation

- RecyclerView is efficient for lists and is the standard choice for dynamic item histories.

### Code to Read / Run

```java
public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private ScanHistoryRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter();
        recyclerView.setAdapter(adapter);

        repository = new ScanHistoryRepository(getApplicationContext());
        repository.getAllHistory().observe(this, scanHistories -> adapter.submitList(scanHistories));
    }
}
```

### 🔵 Try This

- Add an empty-state TextView if there are no saved scans yet.

### Expected Output

- HistoryActivity updates automatically when the LiveData changes.

### ✅ Checkpoint

- Why is observing LiveData cleaner than manually refreshing after every insert?

### ⚠️ Common Mistake

- Do not forget to set a `LayoutManager`; RecyclerView needs one.

### 📌 Key Point

- Reactive UI updates reduce manual wiring.

## Notebook Cell 7 — Validate the complete history feature

### Explanation

- A working history flow proves local persistence is integrated with the UI.

### Step-by-Step

1. Perform a scan or insert mock records.
2. Open HistoryActivity.
3. Confirm records are displayed in newest-first order.
4. Close and reopen the app to confirm persistence survives app restarts.

### 🔵 Try This

- Add three test records with different timestamps and verify the order visually.

### Expected Output

- History remains available after app restart.

### ✅ Checkpoint

- Can you explain how Entity, DAO, Database, and Repository collaborate?

### ⚠️ Common Mistake

- Do not claim the feature is done if you only tested in-memory behavior without restarting the app.

### 📌 Key Point

- Persistence means data survives beyond the current session.

## Mini Quiz

- What problem does this week solve inside LeafGuard AI?
- Which Java class or Android component did you touch first?
- Which file path in this repository is most relevant to this week?
- What would break if you skipped the validation step?
- How does this week connect to the three-tier architecture?

## Evidence Checklist

- [ ] Capture a screenshot of the completed screen or terminal output.
- [ ] Save one code snippet that proves the feature is wired correctly.
- [ ] Write two sentences in your progress log about what you learned.
- [ ] Record at least one bug and the exact fix you applied.
- [ ] Commit working changes before moving to the next week.

## Reflection Prompt

- Explain the feature from memory without reading the code.
- State one improvement you would add after submission.
- Identify one risk if this feature were left untested.

## Next Step

- Continue to **Week 08** when this week is stable and documented.
