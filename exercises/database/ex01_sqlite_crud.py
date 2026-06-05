#!/usr/bin/env python3
"""
Database Exercises — SQLite CRUD with Python (mirrors Android Room)
Week 7 — Room Database & SQLite History

This file uses Python's built-in sqlite3 module.
SQLite is the same engine Android Room uses under the hood.

Run with:  python ex01_sqlite_crud.py

No external dependencies required.
"""

import sqlite3
import os
from datetime import datetime, timedelta
import random


# ══════════════════════════════════════════════════════════════
# DATABASE SETUP
# Mirrors the LeafGuard AI ScanRecord Room entity
# ══════════════════════════════════════════════════════════════

DB_PATH = "/tmp/leafguard_exercises.db"


def create_connection():
    """Create and return a SQLite connection."""
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row  # Enables column access by name
    return conn


def setup_database(conn):
    """
    Create the scan_records table.

    Mirrors the Android Room @Entity:
      @Entity(tableName = "scan_records")
      public class ScanRecord {
          @PrimaryKey(autoGenerate = true)
          private int id;
          private String imagePath;
          private String diseaseName;
          private double confidence;
          private String timestamp;
          private boolean isHealthy;
      }
    """
    cursor = conn.cursor()
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS scan_records (
            id          INTEGER PRIMARY KEY AUTOINCREMENT,
            image_path  TEXT NOT NULL,
            disease_name TEXT NOT NULL,
            confidence  REAL NOT NULL CHECK(confidence >= 0.0 AND confidence <= 1.0),
            timestamp   TEXT NOT NULL,
            is_healthy  INTEGER NOT NULL DEFAULT 0
        )
    """)
    conn.commit()
    print("Database setup complete. Table 'scan_records' ready.\n")


# ══════════════════════════════════════════════════════════════
# EXERCISE 1: INSERT — Save a scan record
# ══════════════════════════════════════════════════════════════

def exercise_1_insert_scan(conn, image_path: str, disease_name: str,
                             confidence: float, timestamp: str, is_healthy: bool) -> int:
    """
    Exercise 1: Insert a new scan record into the database.

    Returns: The auto-generated id of the new record.

    Android Room equivalent:
      @Insert(onConflict = OnConflictStrategy.REPLACE)
      long insertScan(ScanRecord record);

    Hint:
      cursor = conn.cursor()
      cursor.execute("INSERT INTO ...", (...))
      conn.commit()
      return cursor.lastrowid
    """
    # TODO: Implement insert
    raise NotImplementedError("Implement exercise_1_insert_scan()")


def test_exercise_1(conn):
    print("Exercise 1: INSERT")

    # Clear any previous data
    conn.execute("DELETE FROM scan_records")
    conn.commit()

    now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    new_id = exercise_1_insert_scan(
        conn,
        image_path="/images/scan_001.jpg",
        disease_name="Tomato___Early_blight",
        confidence=0.91,
        timestamp=now,
        is_healthy=False
    )

    assert isinstance(new_id, int), f"Expected int id, got {type(new_id)}"
    assert new_id > 0, "auto-generated id should be > 0"

    # Verify the row was actually inserted
    row = conn.execute("SELECT * FROM scan_records WHERE id = ?", (new_id,)).fetchone()
    assert row is not None, "Row should exist after insert"
    assert row["disease_name"] == "Tomato___Early_blight"
    assert abs(row["confidence"] - 0.91) < 0.001
    assert row["is_healthy"] == 0  # SQLite stores bool as 0/1

    print(f"  ✅ Inserted scan record with id={new_id}")
    print("  All Exercise 1 tests passed!\n")


# ══════════════════════════════════════════════════════════════
# EXERCISE 2: SELECT — Query records
# ══════════════════════════════════════════════════════════════

def exercise_2_get_all_scans(conn) -> list:
    """
    Exercise 2A: Return all scan records, newest first.

    Android Room equivalent:
      @Query("SELECT * FROM scan_records ORDER BY timestamp DESC")
      List<ScanRecord> getAllScans();
    """
    # TODO: Implement SELECT all, ordered by timestamp DESC
    raise NotImplementedError("Implement exercise_2_get_all_scans()")


def exercise_2_get_scan_by_id(conn, scan_id: int):
    """
    Exercise 2B: Return a single scan record by primary key.
    Returns None if not found.

    Android Room equivalent:
      @Query("SELECT * FROM scan_records WHERE id = :id LIMIT 1")
      ScanRecord getScanById(int id);
    """
    # TODO: Implement SELECT by id
    raise NotImplementedError("Implement exercise_2_get_scan_by_id()")


def exercise_2_search_by_disease(conn, query: str) -> list:
    """
    Exercise 2C: Search records where disease_name contains the query (case-insensitive).

    Android Room equivalent:
      @Query("SELECT * FROM scan_records WHERE disease_name LIKE '%' || :query || '%'")
      List<ScanRecord> searchByDisease(String query);

    Hint: Use LIKE '%' || ? || '%' or LIKE ? with '%query%' as the parameter.
    """
    # TODO: Implement LIKE search
    raise NotImplementedError("Implement exercise_2_search_by_disease()")


def test_exercise_2(conn):
    print("Exercise 2: SELECT queries")

    # Seed test data
    conn.execute("DELETE FROM scan_records")
    conn.commit()
    base_time = datetime(2024, 6, 1, 10, 0, 0)
    seeds = [
        ("/img/001.jpg", "Tomato___Early_blight", 0.91, False),
        ("/img/002.jpg", "Tomato___Late_blight", 0.85, False),
        ("/img/003.jpg", "Apple___healthy", 0.98, True),
        ("/img/004.jpg", "Potato___Early_blight", 0.78, False),
        ("/img/005.jpg", "Pepper,_bell___healthy", 0.92, True),
    ]
    ids = []
    for i, (path, disease, conf, healthy) in enumerate(seeds):
        ts = (base_time + timedelta(hours=i)).strftime("%Y-%m-%d %H:%M:%S")
        cur = conn.execute(
            "INSERT INTO scan_records VALUES (NULL,?,?,?,?,?)",
            (path, disease, conf, ts, int(healthy))
        )
        ids.append(cur.lastrowid)
    conn.commit()

    # Test get_all
    all_scans = exercise_2_get_all_scans(conn)
    assert len(all_scans) == 5, f"Expected 5 records, got {len(all_scans)}"
    # Should be newest first
    timestamps = [dict(r)["timestamp"] for r in all_scans]
    assert timestamps == sorted(timestamps, reverse=True), "Should be ordered newest first"
    print("  ✅ get_all_scans: 5 records returned in correct order")

    # Test get_by_id
    found = exercise_2_get_scan_by_id(conn, ids[0])
    assert found is not None, "Should find record by id"
    assert dict(found)["disease_name"] == "Tomato___Early_blight"
    missing = exercise_2_get_scan_by_id(conn, 99999)
    assert missing is None, "Should return None for non-existent id"
    print("  ✅ get_scan_by_id: found and not-found cases work")

    # Test search
    results = exercise_2_search_by_disease(conn, "Tomato")
    assert len(results) == 2, f"Expected 2 Tomato records, got {len(results)}"
    results_healthy = exercise_2_search_by_disease(conn, "healthy")
    assert len(results_healthy) == 2, f"Expected 2 healthy records, got {len(results_healthy)}"
    print("  ✅ search_by_disease: LIKE query works")

    print("  All Exercise 2 tests passed!\n")


# ══════════════════════════════════════════════════════════════
# EXERCISE 3: UPDATE and DELETE
# ══════════════════════════════════════════════════════════════

def exercise_3_update_confidence(conn, scan_id: int, new_confidence: float) -> int:
    """
    Exercise 3A: Update the confidence value for a record.
    Returns the number of rows affected (should be 1 if found, 0 if not).

    Android Room equivalent:
      @Update
      void updateScan(ScanRecord record);
      OR
      @Query("UPDATE scan_records SET confidence = :c WHERE id = :id")
      void updateConfidence(int id, double c);
    """
    # TODO: Implement UPDATE
    raise NotImplementedError("Implement exercise_3_update_confidence()")


def exercise_3_delete_scan(conn, scan_id: int) -> int:
    """
    Exercise 3B: Delete a scan record by id.
    Returns the number of rows deleted.

    Android Room equivalent:
      @Delete
      void deleteScan(ScanRecord record);
      OR
      @Query("DELETE FROM scan_records WHERE id = :id")
      void deleteScanById(int id);
    """
    # TODO: Implement DELETE
    raise NotImplementedError("Implement exercise_3_delete_scan()")


def exercise_3_delete_all(conn) -> int:
    """
    Exercise 3C: Delete ALL scan records.
    Returns the number of rows deleted.

    Android Room equivalent:
      @Query("DELETE FROM scan_records")
      void deleteAllScans();
    """
    # TODO: Implement DELETE ALL
    raise NotImplementedError("Implement exercise_3_delete_all()")


def test_exercise_3(conn):
    print("Exercise 3: UPDATE and DELETE")

    # Seed
    conn.execute("DELETE FROM scan_records")
    conn.commit()
    cur = conn.execute(
        "INSERT INTO scan_records VALUES (NULL, ?, ?, ?, ?, ?)",
        ("/img/test.jpg", "Tomato___Early_blight", 0.75, "2024-06-01 10:00:00", 0)
    )
    test_id = cur.lastrowid
    conn.execute(
        "INSERT INTO scan_records VALUES (NULL, ?, ?, ?, ?, ?)",
        ("/img/test2.jpg", "Apple___healthy", 0.90, "2024-06-01 11:00:00", 1)
    )
    conn.commit()

    # Test update
    affected = exercise_3_update_confidence(conn, test_id, 0.91)
    assert affected == 1, f"Should update 1 row, got {affected}"
    row = conn.execute("SELECT confidence FROM scan_records WHERE id = ?", (test_id,)).fetchone()
    assert abs(row["confidence"] - 0.91) < 0.001, "Confidence should be updated"
    no_affect = exercise_3_update_confidence(conn, 99999, 0.5)
    assert no_affect == 0, "Updating non-existent record should affect 0 rows"
    print("  ✅ update_confidence: update works, non-existent returns 0")

    # Test delete single
    deleted = exercise_3_delete_scan(conn, test_id)
    assert deleted == 1, f"Should delete 1 row, got {deleted}"
    row_after = conn.execute("SELECT * FROM scan_records WHERE id = ?", (test_id,)).fetchone()
    assert row_after is None, "Row should be gone after delete"
    print("  ✅ delete_scan: single record deleted")

    # Test delete all
    total_before = conn.execute("SELECT COUNT(*) FROM scan_records").fetchone()[0]
    deleted_all = exercise_3_delete_all(conn)
    assert deleted_all == total_before, f"Should delete {total_before} rows, got {deleted_all}"
    remaining = conn.execute("SELECT COUNT(*) FROM scan_records").fetchone()[0]
    assert remaining == 0, "Table should be empty after delete_all"
    print("  ✅ delete_all: all records deleted")

    print("  All Exercise 3 tests passed!\n")


# ══════════════════════════════════════════════════════════════
# BONUS EXERCISE 4: Aggregation Queries
# ══════════════════════════════════════════════════════════════

def bonus_exercise_4_statistics(conn) -> dict:
    """
    Bonus Exercise 4: Compute scan statistics.

    Returns a dict with:
      - "total_scans": int
      - "healthy_scans": int
      - "disease_scans": int
      - "avg_confidence": float (round to 4 decimal places)
      - "most_common_disease": str (disease_name with highest count, non-healthy only)

    Android Room equivalent:
      @Query("SELECT COUNT(*) FROM scan_records")
      int getTotalCount();

      @Query("SELECT AVG(confidence) FROM scan_records")
      double getAverageConfidence();
    """
    # TODO: Implement aggregation queries
    raise NotImplementedError("Implement bonus_exercise_4_statistics()")


# ══════════════════════════════════════════════════════════════
# MAIN
# ══════════════════════════════════════════════════════════════

if __name__ == "__main__":
    print("=" * 60)
    print("  LeafGuard AI — Database Exercises")
    print("  Python sqlite3 ↔ Android Room equivalents")
    print("=" * 60)
    print()

    # Clean up any previous run
    if os.path.exists(DB_PATH):
        os.remove(DB_PATH)

    conn = create_connection()
    setup_database(conn)

    exercises = [
        ("Exercise 1: INSERT", test_exercise_1),
        ("Exercise 2: SELECT", test_exercise_2),
        ("Exercise 3: UPDATE & DELETE", test_exercise_3),
    ]

    passed = 0
    failed = 0

    for name, test_fn in exercises:
        try:
            test_fn(conn)
            passed += 1
        except NotImplementedError:
            print(f"  ⏳ {name}: Not yet implemented\n")
            failed += 1
        except AssertionError as e:
            print(f"  ❌ {name}: FAILED — {e}\n")
            failed += 1
        except Exception as e:
            print(f"  ❌ {name}: ERROR — {type(e).__name__}: {e}\n")
            failed += 1

    conn.close()

    print(f"Results: {passed} passed, {failed} not yet complete")
    if failed == 0:
        print("🎉 All exercises complete! You're ready for Android Room.")
