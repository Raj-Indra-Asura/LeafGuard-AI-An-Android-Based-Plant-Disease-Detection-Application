# Week 07 Build Task: Implement Room Database Scan History

> **Kotlin-first & accuracy note:** The real Room classes (Kotlin primary: `android-app-kotlin/.../database/`) are `ScanRecord` (table `scan_history`), `ScanDao` (methods `insertScan`, `getAllScans`, `getScanById`, `getRecentScans`, `deleteScan`, `deleteScanById` — all `suspend fun`), and `AppDatabase` (file `leafguard.db`, version 1). In Kotlin, call DAO methods from `lifecycleScope.launch { ... }` (a **coroutine** does the slow database work without freezing the screen) and enable **kapt** (`kapt` generates Room's code — without it the build fails). The Java track uses `annotationProcessor` + `ExecutorService` and is the labelled secondary reference.

## Objective

Integrate Room database into LeafGuard AI to persist scan history. Users can view past scans, see details, and delete records.

**Estimated Time:** 8-10 hours (spread over 3-4 days)

---

## Step 1: Add Room Dependencies (15 minutes)

Add to `build.gradle (Module: app)`:

```gradle
dependencies {
    def room_version = "2.5.0"
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
    
    // Existing dependencies...
}
```

Sync Gradle. Verify no errors.

---

## Step 2: Create ScanRecord Entity (30 minutes)

Create `ScanRecord.java`:

```java
@Entity(tableName = "scan_history")
public class ScanRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String disease;
    private double confidence;
    private String symptoms;
    private String treatment;
    private long timestamp;
    
    @ColumnInfo(name = "image_path")
    private String imagePath;
    
    // Constructor (without id)
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
    // TODO: Generate using IDE (Code → Generate → Getters and Setters)
}
```

---

## Step 3: Create ScanDao (30 minutes)

Create `ScanDao.java`:

```java
@Dao
public interface ScanDao {
    
    @Insert
    void insert(ScanRecord scan);
    
    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    List<ScanRecord> getAll();
    
    @Query("SELECT * FROM scan_history WHERE id = :scanId")
    ScanRecord getById(int scanId);
    
    @Delete
    void delete(ScanRecord scan);
    
    @Query("DELETE FROM scan_history")
    void deleteAll();
}
```

---

## Step 4: Create AppDatabase (45 minutes)

Create `AppDatabase.java`:

```java
@Database(entities = {ScanRecord.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    private static AppDatabase instance;
    
    public abstract ScanDao scanDao();
    
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "leafguard.db"
            )
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }
}
```

Build project. Verify no errors.

---

## Step 5: Create History List Layout (1 hour)

Create `activity_history_list.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    
    <TextView
        android:id="@+id/emptyTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="No scan history yet"
        android:gravity="center"
        android:padding="32dp"
        android:visibility="gone"/>
    
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/historyRecyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
        
</LinearLayout>
```

Create `item_scan_history.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp">
    
    <TextView
        android:id="@+id/diseaseTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:textStyle="bold"/>
    
    <TextView
        android:id="@+id/confidenceTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="14sp"/>
    
    <TextView
        android:id="@+id/timestampTextView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="12sp"
        android:textColor="#666666"/>
        
</LinearLayout>
```

---

## Step 6: Create RecyclerView Adapter (1.5 hours)

Create `HistoryAdapter.java`:

```java
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    
    private List<ScanRecord> scans;
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onItemClick(ScanRecord scan);
    }
    
    public HistoryAdapter(OnItemClickListener listener) {
        this.scans = new ArrayList<>();
        this.listener = listener;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_scan_history, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(scans.get(position), listener);
    }
    
    @Override
    public int getItemCount() {
        return scans.size();
    }
    
    public void setScans(List<ScanRecord> scans) {
        this.scans = scans;
        notifyDataSetChanged();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        // TODO: Implement ViewHolder with findViewById and bind method
        // Reference Week 07 learning notes for complete implementation
    }
}
```

---

## Step 7: Implement HistoryActivity (2 hours)

Create `HistoryActivity.java`:

```java
public class HistoryActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private TextView emptyTextView;
    private HistoryAdapter adapter;
    private AppDatabase database;
    private ExecutorService executor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);
        
        // Initialize views
        recyclerView = findViewById(R.id.historyRecyclerView);
        emptyTextView = findViewById(R.id.emptyTextView);
        
        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(this::openDetailActivity);
        recyclerView.setAdapter(adapter);
        
        // Initialize database and executor
        database = AppDatabase.getInstance(this);
        executor = Executors.newSingleThreadExecutor();
        
        // Load scans
        loadScans();
    }
    
    private void loadScans() {
        executor.execute(() -> {
            List<ScanRecord> scans = database.scanDao().getAll();
            
            runOnUiThread(() -> {
                if (scans.isEmpty()) {
                    emptyTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setScans(scans);
                }
            });
        });
    }
    
    private void openDetailActivity(ScanRecord scan) {
        Intent intent = new Intent(this, HistoryDetailActivity.class);
        intent.putExtra("scan_id", scan.getId());
        startActivity(intent);
    }
}
```

---

## Step 8: Save Scan After Prediction (1 hour)

In `ResultActivity.java`, after successful prediction:

```java
private void saveScanToHistory() {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(() -> {
        ScanRecord scan = new ScanRecord(
            disease,
            confidence,
            symptoms,
            treatment,
            System.currentTimeMillis(),
            imagePath  // Save captured image path
        );
        
        AppDatabase.getInstance(this).scanDao().insert(scan);
        
        runOnUiThread(() -> {
            Toast.makeText(this, "Scan saved to history", Toast.LENGTH_SHORT).show();
        });
    });
}
```

Call `saveScanToHistory()` after displaying results.

---

## Step 9: Create History Detail Activity (1 hour)

Create `activity_history_detail.xml` and `HistoryDetailActivity.java`:

```java
public class HistoryDetailActivity extends AppCompatActivity {
    
    private AppDatabase database;
    private ExecutorService executor;
    private int scanId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        
        scanId = getIntent().getIntExtra("scan_id", -1);
        database = AppDatabase.getInstance(this);
        executor = Executors.newSingleThreadExecutor();
        
        loadScanDetails();
    }
    
    private void loadScanDetails() {
        executor.execute(() -> {
            ScanRecord scan = database.scanDao().getById(scanId);
            
            runOnUiThread(() -> {
                // Display all fields
                // TODO: Set text to TextViews
            });
        });
    }
}
```

---

## Step 10: Implement Delete Functionality (1 hour)

Add delete button to detail activity:

```java
deleteButton.setOnClickListener(v -> showDeleteConfirmation());

private void showDeleteConfirmation() {
    new AlertDialog.Builder(this)
        .setTitle("Delete Scan?")
        .setMessage("This cannot be undone")
        .setPositiveButton("Delete", (dialog, which) -> deleteScan())
        .setNegativeButton("Cancel", null)
        .show();
}

private void deleteScan() {
    executor.execute(() -> {
        ScanRecord scan = database.scanDao().getById(scanId);
        database.scanDao().delete(scan);
        
        runOnUiThread(() -> {
            Toast.makeText(this, "Scan deleted", Toast.LENGTH_SHORT).show();
            finish();  // Return to history list
        });
    });
}
```

---

## Completion Checklist

- [ ] Room dependencies added
- [ ] Entity, DAO, Database classes created
- [ ] History list layout created
- [ ] RecyclerView adapter implemented
- [ ] HistoryActivity displays scans
- [ ] Scans saved after prediction
- [ ] Detail activity shows full scan info
- [ ] Delete functionality works
- [ ] No crashes with empty history
- [ ] All database operations on background thread

---

**Test thoroughly before moving to Week 08!**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 07 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| **4** | **build-task.md** ← *You are here* | **Build Implementation Guide** |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Practice Exercises](exercises.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Build Implementation Guide** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Validation & Verification →](validation-checklist.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 06: Cloud ML Model](../week-06-cloud-ml-model/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 08: XML Disease Library ➡](../week-08-xml-disease-library/README.md) |

---
