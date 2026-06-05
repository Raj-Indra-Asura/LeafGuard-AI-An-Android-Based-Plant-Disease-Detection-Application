# Week 07 Solution - Room Database

## Goal
In Week 07, students add local persistence so the app can save previous plant scans. Room is the recommended Android solution because it gives:
- SQLite storage under the hood
- Java annotations instead of manual SQL table creation
- compile-time query checking
- cleaner architecture for future features like history screens and analytics

---

## 1. `ScanRecord.java` Entity

```java
package com.leafguard.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "scan_history")
public class ScanRecord {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "disease_name")
    private String diseaseName;

    @ColumnInfo(name = "confidence")
    private float confidence;

    @ColumnInfo(name = "symptoms")
    private String symptoms;

    @ColumnInfo(name = "treatment")
    private String treatment;

    @ColumnInfo(name = "prevention")
    private String prevention;

    @ColumnInfo(name = "image_uri")
    private String imageUri;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    public ScanRecord() {
    }

    @Ignore
    public ScanRecord(String diseaseName, float confidence, String symptoms, String treatment,
                      String prevention, String imageUri, double latitude, double longitude,
                      long timestamp) {
        this.diseaseName = diseaseName;
        this.confidence = confidence;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.prevention = prevention;
        this.imageUri = imageUri;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getDiseaseName() { return diseaseName; }
    public void setDiseaseName(String diseaseName) { this.diseaseName = diseaseName; }
    public float getConfidence() { return confidence; }
    public void setConfidence(float confidence) { this.confidence = confidence; }
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public String getPrevention() { return prevention; }
    public void setPrevention(String prevention) { this.prevention = prevention; }
    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
```

### Why these fields?
- `diseaseName`, `confidence`, `symptoms`, `treatment`, and `prevention` preserve the diagnosis.
- `imageUri` lets the app reconnect the result to the original image.
- `latitude` and `longitude` prepare the app for geo-tagged plant history.
- `timestamp` supports sorting and date display.

---

## 2. `ScanDao.java`

```java
package com.leafguard.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertScan(ScanRecord scanRecord);

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    List<ScanRecord> getAllScans();

    @Delete
    void deleteScan(ScanRecord scanRecord);

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC LIMIT :limit")
    List<ScanRecord> getRecentScans(int limit);
}
```

### Query decisions
- `ORDER BY timestamp DESC` makes the newest scans appear first.
- `deleteScan()` is useful for cleanup and testing.
- `getRecentScans(limit)` is handy for dashboards or widgets later.

---

## 3. `AppDatabase.java`

```java
package com.leafguard.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ScanRecord.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "leafguard.db";
    private static volatile AppDatabase instance;

    public abstract ScanDao scanDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
```

### Why singleton database?
A Room database is expensive to build. A singleton ensures the app uses one shared instance safely.

---

## 4. Inserting a Scan from `ResultActivity`

```java
private void saveToHistory() {
    binding.buttonSaveToHistory.setEnabled(false);
    databaseExecutor.execute(() -> {
        ScanRecord record = new ScanRecord();
        record.setDiseaseName(diseaseName);
        record.setConfidence(confidence);
        record.setSymptoms(symptoms);
        record.setTreatment(treatment);
        record.setPrevention(prevention);
        record.setImageUri(imageUri);
        record.setLatitude(0.0);
        record.setLongitude(0.0);
        record.setTimestamp(System.currentTimeMillis());

        AppDatabase.getInstance(getApplicationContext()).scanDao().insertScan(record);

        runOnUiThread(() -> {
            Toast.makeText(this, "Result saved to local history.", Toast.LENGTH_SHORT).show();
            binding.buttonSaveToHistory.setText("Saved to History");
        });
    });
}
```

### Important point
Database work runs on a background thread using `ExecutorService`. Room does not allow long database operations on the UI thread in normal app setups.

---

## 5. Querying Scans in `HistoryActivity`

```java
private void loadHistory() {
    databaseExecutor.execute(() -> {
        List<ScanRecord> scans = AppDatabase.getInstance(getApplicationContext()).scanDao().getAllScans();
        runOnUiThread(() -> renderHistory(scans));
    });
}

private void renderHistory(List<ScanRecord> scans) {
    adapter.submitList(scans);
    boolean hasItems = !scans.isEmpty();
    binding.recyclerHistory.setVisibility(hasItems ? View.VISIBLE : View.GONE);
    binding.textEmptyState.setVisibility(hasItems ? View.GONE : View.VISIBLE);
}
```

This is the minimum structure needed to:
- load saved rows
- update the RecyclerView
- display an empty state when no rows exist

---

## Optional Improvement - Repository Pattern
Students can stop after DAO + database + activity integration, but a repository improves architecture.

### `ScanRepository.java`

```java
package com.leafguard.database;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanRepository {

    private final ScanDao scanDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ScanRepository(Context context) {
        scanDao = AppDatabase.getInstance(context).scanDao();
    }

    public void insert(ScanRecord record) {
        executorService.execute(() -> scanDao.insertScan(record));
    }

    public void delete(ScanRecord record) {
        executorService.execute(() -> scanDao.deleteScan(record));
    }

    public void getAll(RepositoryCallback<List<ScanRecord>> callback) {
        executorService.execute(() -> callback.onComplete(scanDao.getAllScans()));
    }

    public interface RepositoryCallback<T> {
        void onComplete(T result);
    }
}
```

### Why add a repository?
- Activities stay smaller.
- Data access logic lives in one place.
- It becomes easier to add caching, remote sync, or testing later.

---

## Common Mistakes
- Forgetting `annotationProcessor` for Room compiler in `build.gradle`.
- Using unsupported field types without converters.
- Running queries on the main thread.
- Forgetting `version = 1` in the `@Database` annotation.
- Not sorting by timestamp, which makes history feel random.

---

## Summary
A working Room setup for LeafGuard AI needs exactly three core classes:
1. `ScanRecord` entity
2. `ScanDao` interface
3. `AppDatabase` singleton

After that, students can insert records from `ResultActivity` and load them inside `HistoryActivity` to build a functional scan history feature.
