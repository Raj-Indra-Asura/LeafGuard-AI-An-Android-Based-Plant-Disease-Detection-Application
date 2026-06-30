/*
 * Exercise 4: Room database for scan history
 * Week 7 - Room / SQLite History
 *
 * Starter skeleton. In the real project these correspond to:
 *   android-app/app/src/main/java/com/leafguard/database/ScanRecord.java  (@Entity)
 *   android-app/app/src/main/java/com/leafguard/database/ScanDao.java     (@Dao)
 *   android-app/app/src/main/java/com/leafguard/database/AppDatabase.java (@Database)
 * Complete the TODOs (see exercises/android/README.md, Ex 5.1-5.6).
 *
 * Goal: persist each scan result (disease, confidence, image path, timestamp)
 * and load it back on the History screen.
 *
 * Key concepts: @Entity / @Dao / @Database, and running queries OFF the main
 * thread (use a background Executor or LiveData) -> otherwise
 * IllegalStateException: Cannot access database on the main thread.
 *
 * Verification:
 *   [ ] Inserting a scan persists across app restarts
 *   [ ] History list loads newest-first
 *   [ ] No main-thread database access
 */
package com.leafguard.database;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

public final class ex04_ScanRepository {

    private ex04_ScanRepository() {
    }

    /** TODO 1: a single saved scan. */
    @Entity(tableName = "scans")
    public static class ScanRecord {
        @PrimaryKey(autoGenerate = true)
        public long id;

        @ColumnInfo(name = "disease")
        public String disease;

        // TODO 2: add confidence (float), imagePath (String), timestamp (long).
    }

    /** TODO 3: data-access object for the scans table. */
    @Dao
    public interface ScanDao {
        @Insert
        long insert(ScanRecord record);

        @Query("SELECT * FROM scans ORDER BY id DESC")
        List<ScanRecord> getAllNewestFirst();

        // TODO 4: add deleteById(long id) and (optionally) deleteAll().
    }

    // TODO 5: create the @Database class exposing scanDao(), and obtain it with
    //   Room.databaseBuilder(...). Call DAO methods on a background Executor.
}
