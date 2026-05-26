# Database Development Exercises

## Overview

This directory contains exercises focused on implementing local data persistence using Room Database in the LeafGuard AI Android application. These exercises cover entity definition, DAO (Data Access Object) creation, database queries, type converters, and managing the complete data lifecycle from detection to storage and retrieval.

## Weekly Mapping

### Week 7: Room Database Integration
- Entity design and relationships
- DAO interface implementation
- Database configuration
- Repository pattern
- Data lifecycle management

## Exercise Categories

### 1. Entity Creation (Week 7)

**Exercise 1.1: Define PlantDisease Entity**

```kotlin
package com.leafguard.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plant_diseases")
data class PlantDisease(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "disease_name")
    val diseaseName: String,

    @ColumnInfo(name = "plant_type")
    val plantType: String,

    @ColumnInfo(name = "confidence_score")
    val confidenceScore: Float,

    @ColumnInfo(name = "detection_date")
    val detectionDate: Long,

    @ColumnInfo(name = "image_path")
    val imagePath: String,

    @ColumnInfo(name = "symptoms")
    val symptoms: String?,

    @ColumnInfo(name = "treatment")
    val treatment: String?,

    @ColumnInfo(name = "notes")
    val notes: String? = null,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)
```

**Verification**:
- Build project successfully
- No Room compilation errors
- Check generated Java files in `build/generated/source/kapt/`
- Verify all fields have appropriate types

**Exercise 1.2: Create DetectionHistory Entity with Relationship**

```kotlin
@Entity(tableName = "detection_history")
data class DetectionHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "disease_id")
    val diseaseId: Int,

    @ColumnInfo(name = "detected_at")
    val detectedAt: Long,

    @ColumnInfo(name = "location_lat")
    val locationLat: Double?,

    @ColumnInfo(name = "location_lng")
    val locationLng: Double?,

    @ColumnInfo(name = "weather_condition")
    val weatherCondition: String?
)

// Relation class to fetch disease with history
data class PlantDiseaseWithHistory(
    @Embedded val disease: PlantDisease,

    @Relation(
        parentColumn = "id",
        entityColumn = "disease_id"
    )
    val history: List<DetectionHistory>
)
```

**Verification**:
- Entities compile correctly
- Relationship is properly defined
- Query methods can use the relation class

**Exercise 1.3: Implement Type Converters**

```kotlin
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    // Convert List<String> to JSON string
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let {
            val listType = object : TypeToken<List<String>>() {}.type
            gson.fromJson(it, listType)
        }
    }

    // Convert Date to Long
    @TypeConverter
    fun fromTimestamp(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: java.util.Date?): Long? {
        return date?.time
    }
}

// Updated entity using converters
@Entity(tableName = "plant_diseases")
data class PlantDisease(
    // ... previous fields ...

    @ColumnInfo(name = "alternative_names")
    val alternativeNames: List<String>? = null,

    @ColumnInfo(name = "prevention_tips")
    val preventionTips: List<String>? = null
)
```

**Verification**:
- Type converters compile successfully
- Complex types are stored as JSON
- Data persists and retrieves correctly

### 2. DAO Methods (Week 7)

**Exercise 2.1: Create Basic CRUD Operations**

```kotlin
package com.leafguard.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDiseaseDao {

    // INSERT operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(disease: PlantDisease): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(diseases: List<PlantDisease>): List<Long>

    // UPDATE operations
    @Update
    suspend fun update(disease: PlantDisease): Int

    @Query("UPDATE plant_diseases SET is_favorite = :isFavorite WHERE id = :diseaseId")
    suspend fun updateFavoriteStatus(diseaseId: Int, isFavorite: Boolean): Int

    @Query("UPDATE plant_diseases SET notes = :notes WHERE id = :diseaseId")
    suspend fun updateNotes(diseaseId: Int, notes: String?): Int

    // DELETE operations
    @Delete
    suspend fun delete(disease: PlantDisease): Int

    @Query("DELETE FROM plant_diseases WHERE id = :diseaseId")
    suspend fun deleteById(diseaseId: Int): Int

    @Query("DELETE FROM plant_diseases")
    suspend fun deleteAll(): Int

    // READ operations - Basic queries
    @Query("SELECT * FROM plant_diseases WHERE id = :diseaseId")
    suspend fun getDiseaseById(diseaseId: Int): PlantDisease?

    @Query("SELECT * FROM plant_diseases ORDER BY detection_date DESC")
    fun getAllDiseases(): Flow<List<PlantDisease>>

    @Query("SELECT * FROM plant_diseases ORDER BY detection_date DESC LIMIT :limit")
    fun getRecentDiseases(limit: Int): Flow<List<PlantDisease>>

    @Query("SELECT COUNT(*) FROM plant_diseases")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM plant_diseases WHERE is_favorite = 1")
    suspend fun getFavoriteCount(): Int
}
```

**Verification**:
```kotlin
// Test in a coroutine scope
class DaoTest {
    suspend fun testBasicOperations(dao: PlantDiseaseDao) {
        // Test insert
        val disease = PlantDisease(
            diseaseName = "Early Blight",
            plantType = "Tomato",
            confidenceScore = 0.95f,
            detectionDate = System.currentTimeMillis(),
            imagePath = "/path/to/image.jpg"
        )
        val id = dao.insert(disease)
        println("Inserted with ID: $id")

        // Test read
        val retrieved = dao.getDiseaseById(id.toInt())
        println("Retrieved: $retrieved")

        // Test update
        retrieved?.let {
            val updated = it.copy(notes = "Test note")
            dao.update(updated)
        }

        // Test delete
        dao.deleteById(id.toInt())
        println("Deleted disease")

        // Verify deletion
        val count = dao.getCount()
        println("Remaining count: $count")
    }
}
```

**Exercise 2.2: Implement Advanced Query Methods**

```kotlin
@Dao
interface PlantDiseaseDao {
    // ... previous methods ...

    // FILTER queries
    @Query("SELECT * FROM plant_diseases WHERE disease_name LIKE '%' || :searchQuery || '%'")
    fun searchByDiseaseName(searchQuery: String): Flow<List<PlantDisease>>

    @Query("SELECT * FROM plant_diseases WHERE plant_type = :plantType ORDER BY detection_date DESC")
    fun getDiseasesByPlantType(plantType: String): Flow<List<PlantDisease>>

    @Query("SELECT * FROM plant_diseases WHERE confidence_score >= :minConfidence ORDER BY confidence_score DESC")
    fun getDiseasesAboveConfidence(minConfidence: Float): Flow<List<PlantDisease>>

    @Query("SELECT * FROM plant_diseases WHERE is_favorite = 1 ORDER BY detection_date DESC")
    fun getFavoriteDiseases(): Flow<List<PlantDisease>>

    // DATE-BASED queries
    @Query("SELECT * FROM plant_diseases WHERE detection_date >= :startDate AND detection_date <= :endDate ORDER BY detection_date DESC")
    fun getDiseasesInDateRange(startDate: Long, endDate: Long): Flow<List<PlantDisease>>

    @Query("SELECT * FROM plant_diseases WHERE detection_date >= :timestamp ORDER BY detection_date DESC")
    fun getDiseasesAfter(timestamp: Long): Flow<List<PlantDisease>>

    // AGGREGATION queries
    @Query("SELECT plant_type, COUNT(*) as count FROM plant_diseases GROUP BY plant_type ORDER BY count DESC")
    fun getPlantTypeDistribution(): Flow<List<PlantTypeCount>>

    @Query("SELECT disease_name, AVG(confidence_score) as avg_confidence FROM plant_diseases GROUP BY disease_name")
    fun getAverageConfidenceByDisease(): Flow<List<DiseaseConfidence>>

    // STATISTICS queries
    @Query("SELECT AVG(confidence_score) FROM plant_diseases")
    suspend fun getAverageConfidence(): Float?

    @Query("SELECT MAX(confidence_score) FROM plant_diseases")
    suspend fun getMaxConfidence(): Float?

    @Query("SELECT MIN(confidence_score) FROM plant_diseases")
    suspend fun getMinConfidence(): Float?

    // DISTINCT queries
    @Query("SELECT DISTINCT plant_type FROM plant_diseases ORDER BY plant_type")
    fun getUniquePlantTypes(): Flow<List<String>>

    @Query("SELECT DISTINCT disease_name FROM plant_diseases ORDER BY disease_name")
    fun getUniqueDiseaseNames(): Flow<List<String>>
}

// Data classes for aggregation results
data class PlantTypeCount(
    @ColumnInfo(name = "plant_type") val plantType: String,
    @ColumnInfo(name = "count") val count: Int
)

data class DiseaseConfidence(
    @ColumnInfo(name = "disease_name") val diseaseName: String,
    @ColumnInfo(name = "avg_confidence") val avgConfidence: Float
)
```

**Verification**:
```kotlin
// Test advanced queries
suspend fun testAdvancedQueries(dao: PlantDiseaseDao) {
    // Test search
    dao.searchByDiseaseName("Blight").collect { results ->
        println("Search results: ${results.size}")
    }

    // Test aggregation
    val avgConfidence = dao.getAverageConfidence()
    println("Average confidence: $avgConfidence")

    // Test grouping
    dao.getPlantTypeDistribution().collect { distribution ->
        distribution.forEach { item ->
            println("${item.plantType}: ${item.count} detections")
        }
    }
}
```

**Exercise 2.3: Implement Relationship Queries**

```kotlin
@Dao
interface PlantDiseaseDao {
    // ... previous methods ...

    // RELATIONSHIP queries
    @Transaction
    @Query("SELECT * FROM plant_diseases WHERE id = :diseaseId")
    suspend fun getDiseaseWithHistory(diseaseId: Int): PlantDiseaseWithHistory?

    @Transaction
    @Query("SELECT * FROM plant_diseases ORDER BY detection_date DESC")
    fun getAllDiseasesWithHistory(): Flow<List<PlantDiseaseWithHistory>>

    // Complex JOIN query
    @Query("""
        SELECT pd.*, COUNT(dh.id) as history_count
        FROM plant_diseases pd
        LEFT JOIN detection_history dh ON pd.id = dh.disease_id
        GROUP BY pd.id
        ORDER BY history_count DESC
    """)
    fun getDiseasesWithHistoryCount(): Flow<List<DiseaseWithCount>>
}

data class DiseaseWithCount(
    @Embedded val disease: PlantDisease,
    @ColumnInfo(name = "history_count") val historyCount: Int
)
```

**Verification**:
- Relationship queries return correct nested data
- JOIN operations work as expected
- Transaction annotation ensures atomicity

### 3. Database Configuration (Week 7)

**Exercise 3.1: Create Database Class**

```kotlin
package com.leafguard.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        PlantDisease::class,
        DetectionHistory::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class LeafGuardDatabase : RoomDatabase() {

    abstract fun plantDiseaseDao(): PlantDiseaseDao
    abstract fun detectionHistoryDao(): DetectionHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: LeafGuardDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope? = null
        ): LeafGuardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LeafGuardDatabase::class.java,
                    "leafguard_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration() // Only for development
                    .build()

                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope?
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope?.launch {
                        populateDatabase(database.plantDiseaseDao())
                    }
                }
            }

            suspend fun populateDatabase(dao: PlantDiseaseDao) {
                // Pre-populate with sample data if needed
                println("Database created and ready")
            }
        }

        fun closeDatabase() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}
```

**Verification**:
- Database initializes successfully
- Singleton pattern works correctly
- DAOs are accessible through database instance

**Exercise 3.2: Implement Database Migrations**

```kotlin
// Migration from version 1 to version 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add new column
        database.execSQL(
            "ALTER TABLE plant_diseases ADD COLUMN severity TEXT DEFAULT 'MEDIUM'"
        )

        // Create new index
        database.execSQL(
            "CREATE INDEX index_plant_diseases_severity ON plant_diseases(severity)"
        )
    }
}

// Migration from version 2 to version 3
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create new table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS treatment_logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                disease_id INTEGER NOT NULL,
                treatment_date INTEGER NOT NULL,
                treatment_type TEXT NOT NULL,
                effectiveness TEXT,
                FOREIGN KEY(disease_id) REFERENCES plant_diseases(id) ON DELETE CASCADE
            )
        """)
    }
}

// Updated database builder with migrations
fun getDatabase(context: Context, scope: CoroutineScope? = null): LeafGuardDatabase {
    return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
            context.applicationContext,
            LeafGuardDatabase::class.java,
            "leafguard_database"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .addCallback(DatabaseCallback(scope))
            .build()

        INSTANCE = instance
        instance
    }
}
```

**Verification**:
- Migrations run without errors
- Existing data is preserved
- New schema is applied correctly
- Test on different database versions

### 4. Repository Pattern (Week 7)

**Exercise 4.1: Create Repository**

```kotlin
package com.leafguard.data.repository

import com.leafguard.data.local.dao.PlantDiseaseDao
import com.leafguard.data.local.entity.PlantDisease
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlantDiseaseRepository(private val dao: PlantDiseaseDao) {

    // Observe all diseases
    val allDiseases: Flow<List<PlantDisease>> = dao.getAllDiseases()

    // Observe favorites
    val favoriteDiseases: Flow<List<PlantDisease>> = dao.getFavoriteDiseases()

    // Insert operations
    suspend fun insert(disease: PlantDisease): Long = withContext(Dispatchers.IO) {
        dao.insert(disease)
    }

    suspend fun insertAll(diseases: List<PlantDisease>): List<Long> =
        withContext(Dispatchers.IO) {
            dao.insertAll(diseases)
        }

    // Update operations
    suspend fun update(disease: PlantDisease): Int = withContext(Dispatchers.IO) {
        dao.update(disease)
    }

    suspend fun toggleFavorite(diseaseId: Int, isFavorite: Boolean): Int =
        withContext(Dispatchers.IO) {
            dao.updateFavoriteStatus(diseaseId, isFavorite)
        }

    suspend fun updateNotes(diseaseId: Int, notes: String?): Int =
        withContext(Dispatchers.IO) {
            dao.updateNotes(diseaseId, notes)
        }

    // Delete operations
    suspend fun delete(disease: PlantDisease): Int = withContext(Dispatchers.IO) {
        dao.delete(disease)
    }

    suspend fun deleteById(diseaseId: Int): Int = withContext(Dispatchers.IO) {
        dao.deleteById(diseaseId)
    }

    suspend fun deleteAll(): Int = withContext(Dispatchers.IO) {
        dao.deleteAll()
    }

    // Query operations
    suspend fun getDiseaseById(diseaseId: Int): PlantDisease? =
        withContext(Dispatchers.IO) {
            dao.getDiseaseById(diseaseId)
        }

    fun searchDiseases(query: String): Flow<List<PlantDisease>> {
        return dao.searchByDiseaseName(query)
    }

    fun getDiseasesByPlantType(plantType: String): Flow<List<PlantDisease>> {
        return dao.getDiseasesByPlantType(plantType)
    }

    fun getDiseasesInDateRange(startDate: Long, endDate: Long): Flow<List<PlantDisease>> {
        return dao.getDiseasesInDateRange(startDate, endDate)
    }

    // Statistics
    suspend fun getStatistics(): DiseaseStatistics = withContext(Dispatchers.IO) {
        DiseaseStatistics(
            totalCount = dao.getCount(),
            favoriteCount = dao.getFavoriteCount(),
            averageConfidence = dao.getAverageConfidence() ?: 0f,
            maxConfidence = dao.getMaxConfidence() ?: 0f,
            minConfidence = dao.getMinConfidence() ?: 0f
        )
    }
}

data class DiseaseStatistics(
    val totalCount: Int,
    val favoriteCount: Int,
    val averageConfidence: Float,
    val maxConfidence: Float,
    val minConfidence: Float
)
```

**Verification**:
```kotlin
// Test repository in ViewModel
class TestViewModel(private val repository: PlantDiseaseRepository) {
    suspend fun testRepository() {
        // Insert
        val disease = PlantDisease(
            diseaseName = "Test Disease",
            plantType = "Test Plant",
            confidenceScore = 0.9f,
            detectionDate = System.currentTimeMillis(),
            imagePath = "/test/path"
        )
        val id = repository.insert(disease)

        // Read
        val retrieved = repository.getDiseaseById(id.toInt())
        println("Retrieved: $retrieved")

        // Update
        repository.toggleFavorite(id.toInt(), true)

        // Statistics
        val stats = repository.getStatistics()
        println("Statistics: $stats")
    }
}
```

### 5. Data Lifecycle Management (Week 7)

**Exercise 5.1: Implement Complete Detection Flow**

```kotlin
class DetectionManager(
    private val repository: PlantDiseaseRepository,
    private val context: Context
) {
    suspend fun saveDetectionResult(
        diseaseName: String,
        plantType: String,
        confidence: Float,
        imageBitmap: Bitmap
    ): Long {
        // Save image to internal storage
        val imagePath = saveImageToStorage(imageBitmap)

        // Create entity
        val disease = PlantDisease(
            diseaseName = diseaseName,
            plantType = plantType,
            confidenceScore = confidence,
            detectionDate = System.currentTimeMillis(),
            imagePath = imagePath
        )

        // Save to database
        return repository.insert(disease)
    }

    private fun saveImageToStorage(bitmap: Bitmap): String {
        val filename = "disease_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, filename)

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }

        return file.absolutePath
    }

    fun loadImage(imagePath: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(imagePath)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteDetection(disease: PlantDisease) {
        // Delete image file
        File(disease.imagePath).delete()

        // Delete from database
        repository.delete(disease)
    }
}
```

**Verification**:
- Images are saved correctly
- Database entries are created
- Complete lifecycle works end-to-end
- Cleanup removes both file and database entry

**Exercise 5.2: Implement Data Export/Import**

```kotlin
class DataManager(
    private val repository: PlantDiseaseRepository,
    private val gson: Gson
) {
    suspend fun exportData(outputFile: File): Boolean {
        return try {
            val diseases = repository.allDiseases.first()
            val json = gson.toJson(diseases)

            outputFile.writeText(json)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun importData(inputFile: File): Boolean {
        return try {
            val json = inputFile.readText()
            val type = object : TypeToken<List<PlantDisease>>() {}.type
            val diseases: List<PlantDisease> = gson.fromJson(json, type)

            repository.insertAll(diseases)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun clearAllData() {
        repository.deleteAll()
    }
}
```

**Verification**:
- Export creates valid JSON file
- Import restores data correctly
- Clear operation removes all entries

## How to Complete Exercises

1. **Setup Room**: Add Room dependencies to build.gradle
2. **Define entities**: Create data classes with Room annotations
3. **Create DAOs**: Implement query methods
4. **Build database**: Configure database class
5. **Implement repository**: Abstract data access
6. **Test thoroughly**: Verify all CRUD operations
7. **Document**: Add comments and update reflection journal

## Testing Checklist

- [ ] Entities compile without errors
- [ ] All DAO methods work correctly
- [ ] Database initializes successfully
- [ ] Migrations preserve data
- [ ] Repository provides clean API
- [ ] Data persists across app restarts
- [ ] Queries return expected results
- [ ] Foreign key constraints work
- [ ] Type converters handle complex types
- [ ] No memory leaks in database operations

## Common Issues and Solutions

**Issue**: Database not persisting data
**Solution**: Ensure operations are on background thread, check database location

**Issue**: Type conversion errors
**Solution**: Implement proper TypeConverters for complex types

**Issue**: Migration failures
**Solution**: Test migrations incrementally, verify SQL syntax

**Issue**: Memory leaks
**Solution**: Properly close database connections, use ViewModel for lifecycle

## Resources

- [Room Documentation](https://developer.android.com/training/data-storage/room)
- [Room Testing Guide](https://developer.android.com/training/data-storage/room/testing-db)
- [Database Migration Guide](https://developer.android.com/training/data-storage/room/migrating-db-versions)
- [Coroutines with Room](https://developer.android.com/kotlin/coroutines/coroutines-adv)

## Submission Requirements

1. Complete entity definitions
2. Fully implemented DAOs
3. Database configuration code
4. Repository implementation
5. Test cases demonstrating all operations
6. Migration scripts (if applicable)
7. Documentation of schema design
8. Reflection journal entry for Week 7
