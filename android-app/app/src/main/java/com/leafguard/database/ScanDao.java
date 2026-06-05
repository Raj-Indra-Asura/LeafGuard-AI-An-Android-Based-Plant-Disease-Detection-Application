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
