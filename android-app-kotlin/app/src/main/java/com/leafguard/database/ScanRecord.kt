package com.leafguard.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Kotlin twin of ScanRecord.java.
 *
 * Identical Room schema: table name "scan_history" and the exact same
 * column names/types, so both apps produce byte-compatible databases.
 */
@Entity(tableName = "scan_history")
data class ScanRecord(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "disease_name")
    var diseaseName: String? = null,

    @ColumnInfo(name = "confidence")
    var confidence: Float = 0f,

    @ColumnInfo(name = "symptoms")
    var symptoms: String? = null,

    @ColumnInfo(name = "treatment")
    var treatment: String? = null,

    @ColumnInfo(name = "prevention")
    var prevention: String? = null,

    @ColumnInfo(name = "image_uri")
    var imageUri: String? = null,

    @ColumnInfo(name = "latitude")
    var latitude: Double = 0.0,

    @ColumnInfo(name = "longitude")
    var longitude: Double = 0.0,

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0
)
