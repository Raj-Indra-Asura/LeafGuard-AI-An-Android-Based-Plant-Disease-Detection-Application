/*
 * Exercise 4: Room database for scan history (Kotlin twin of ex04_ScanRepository.java)
 * Week 7 - Room / SQLite History
 *
 * Starter skeleton. In the real project these correspond to:
 *   android-app-kotlin/app/src/main/java/com/leafguard/database/ScanRecord.kt  (@Entity)
 *   android-app-kotlin/app/src/main/java/com/leafguard/database/ScanDao.kt     (@Dao)
 *   android-app-kotlin/app/src/main/java/com/leafguard/database/AppDatabase.kt (@Database)
 * Complete the TODOs (see exercises/android-kotlin/README.md, Ex 5.1-5.6).
 *
 * Goal: persist each scan result (disease, confidence, image path, timestamp)
 * and load it back on the History screen.
 *
 * Key concepts: @Entity / @Dao / @Database, and running queries OFF the main
 * thread. In Kotlin use `suspend fun` DAO methods with coroutines
 * (lifecycleScope.launch { ... }) instead of a background Executor ->
 * otherwise IllegalStateException: Cannot access database on the main thread.
 * Remember: the Kotlin module uses `kapt` (not annotationProcessor) for
 * androidx.room:room-compiler — Room codegen silently fails otherwise.
 *
 * Verification:
 *   [ ] Inserting a scan persists across app restarts
 *   [ ] History list loads newest-first
 *   [ ] No main-thread database access
 */
package com.leafguard.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

object ex04_ScanRepository {

    /** TODO 1: a single saved scan. */
    @Entity(tableName = "scans")
    data class ScanRecord(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,

        @ColumnInfo(name = "disease")
        var disease: String? = null

        // TODO 2: add confidence (Float), imagePath (String), timestamp (Long).
    )

    /** TODO 3: data-access object for the scans table. */
    @Dao
    interface ScanDao {
        @Insert
        suspend fun insert(record: ScanRecord): Long

        @Query("SELECT * FROM scans ORDER BY id DESC")
        suspend fun getAllNewestFirst(): List<ScanRecord>

        // TODO 4: add deleteById(id: Long) and (optionally) deleteAll().
    }

    // TODO 5: create the @Database class exposing scanDao(), and obtain it with
    //   Room.databaseBuilder(...). Call suspend DAO methods from a coroutine
    //   (lifecycleScope.launch { ... }).
}
