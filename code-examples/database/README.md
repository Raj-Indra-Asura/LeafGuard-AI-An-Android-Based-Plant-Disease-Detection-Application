# Database Code Examples

## 1. Room entity

```java
@Entity(tableName = "detections")
public class DetectionEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String imageUri;
    public String diseaseName;
    public float confidence;
    public String treatment;
    public long createdAt;
}
```

## 2. DAO

```java
@Dao
public interface DetectionDao {
    @Insert
    long insert(DetectionEntity entity);

    @Query("SELECT * FROM detections ORDER BY createdAt DESC")
    List<DetectionEntity> getAll();

    @Query("DELETE FROM detections WHERE id = :id")
    void deleteById(long id);
}
```

## 3. Database

```java
@Database(entities = {DetectionEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DetectionDao detectionDao();
}
```

## 4. Build instance

```java
AppDatabase db = Room.databaseBuilder(
        context.getApplicationContext(),
        AppDatabase.class,
        "leafguard.db"
).build();
```

## Safety rule

Do not run DAO methods on the UI thread. Use an `ExecutorService`, coroutine, RxJava, or another background execution method.
