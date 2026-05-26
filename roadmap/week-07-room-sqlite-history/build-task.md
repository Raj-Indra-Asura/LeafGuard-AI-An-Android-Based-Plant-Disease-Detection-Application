# Week 07 Build Task: Implement Room Database Scan History

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

## Step 2: Create ScanHistory Entity (30 minutes)

Create `ScanHistory.java`:

```java
@Entity(tableName = "scan_history")
public class ScanHistory {
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
    // TODO: Generate using IDE (Code → Generate → Getters and Setters)
}
```

---

## Step 3: Create ScanHistoryDao (30 minutes)

Create `ScanHistoryDao.java`:

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
    
    @Query("DELETE FROM scan_history")
    void deleteAll();
}
```

---

## Step 4: Create AppDatabase (45 minutes)

Create `AppDatabase.java`:

```java
@Database(entities = {ScanHistory.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    private static AppDatabase instance;
    
    public abstract ScanHistoryDao scanHistoryDao();
    
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "leafguard_database"
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

Create `ScanHistoryAdapter.java`:

```java
public class ScanHistoryAdapter extends RecyclerView.Adapter<ScanHistoryAdapter.ViewHolder> {
    
    private List<ScanHistory> scans;
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onItemClick(ScanHistory scan);
    }
    
    public ScanHistoryAdapter(OnItemClickListener listener) {
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
    
    public void setScans(List<ScanHistory> scans) {
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

## Step 7: Implement HistoryListActivity (2 hours)

Create `HistoryListActivity.java`:

```java
public class HistoryListActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private TextView emptyTextView;
    private ScanHistoryAdapter adapter;
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
        adapter = new ScanHistoryAdapter(this::openDetailActivity);
        recyclerView.setAdapter(adapter);
        
        // Initialize database and executor
        database = AppDatabase.getInstance(this);
        executor = Executors.newSingleThreadExecutor();
        
        // Load scans
        loadScans();
    }
    
    private void loadScans() {
        executor.execute(() -> {
            List<ScanHistory> scans = database.scanHistoryDao().getAll();
            
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
    
    private void openDetailActivity(ScanHistory scan) {
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
        ScanHistory scan = new ScanHistory(
            disease,
            confidence,
            symptoms,
            treatment,
            System.currentTimeMillis(),
            imagePath  // Save captured image path
        );
        
        AppDatabase.getInstance(this).scanHistoryDao().insert(scan);
        
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
            ScanHistory scan = database.scanHistoryDao().getById(scanId);
            
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
        ScanHistory scan = database.scanHistoryDao().getById(scanId);
        database.scanHistoryDao().delete(scan);
        
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
- [ ] HistoryListActivity displays scans
- [ ] Scans saved after prediction
- [ ] Detail activity shows full scan info
- [ ] Delete functionality works
- [ ] No crashes with empty history
- [ ] All database operations on background thread

---

**Test thoroughly before moving to Week 08!**
