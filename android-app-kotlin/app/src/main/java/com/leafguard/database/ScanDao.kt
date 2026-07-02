package com.leafguard.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Kotlin twin of ScanDao.java.
 *
 * Same SQL, same method names. Methods are `suspend fun` so Room generates
 * coroutine-friendly implementations (callers use Dispatchers.IO via
 * lifecycleScope instead of an ExecutorService).
 */
@Dao
interface ScanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scanRecord: ScanRecord): Long

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC")
    suspend fun getAllScans(): List<ScanRecord>

    @Delete
    suspend fun deleteScan(scanRecord: ScanRecord)

    @Query("SELECT * FROM scan_history ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentScans(limit: Int): List<ScanRecord>

    @Query("SELECT * FROM scan_history WHERE id = :id LIMIT 1")
    suspend fun getScanById(id: Long): ScanRecord?

    @Query("DELETE FROM scan_history WHERE id = :id")
    suspend fun deleteScanById(id: Long)
}
