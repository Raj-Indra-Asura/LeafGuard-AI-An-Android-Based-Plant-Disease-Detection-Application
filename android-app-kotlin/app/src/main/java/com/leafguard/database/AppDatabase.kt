package com.leafguard.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Kotlin twin of AppDatabase.java.
 *
 * Same database name ("leafguard.db"), version, destructive-migration
 * fallback, and double-checked-locking singleton pattern.
 */
@Database(entities = [ScanRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scanDao(): ScanDao

    companion object {
        private const val DATABASE_NAME = "leafguard.db"

        @Volatile
        private var instance: AppDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
