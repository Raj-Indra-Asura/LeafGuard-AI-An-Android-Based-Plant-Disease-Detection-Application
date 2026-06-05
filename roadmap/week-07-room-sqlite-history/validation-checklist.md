# Week 07: Validation Checklist - Room Database and Scan History

## Pass/Fail Validation Criteria

Mark ✅ only when criterion fully met. No partial credit.

---

## Section 1: Room Setup (10 points)

- [ ] Room dependencies added to build.gradle
- [ ] Gradle sync completed successfully
- [ ] Project builds without Room-related errors
- [ ] No @Entity or @Dao annotation errors
- [ ] All three Room components (Entity, DAO, Database) created

---

## Section 2: Entity Class (8 points)

- [ ] ScanHistory entity class exists
- [ ] @Entity annotation present
- [ ] @PrimaryKey with autoGenerate=true
- [ ] All required fields: id, disease, confidence, symptoms, treatment, timestamp, imagePath
- [ ] Constructor implemented
- [ ] All getters implemented
- [ ] All setters implemented
- [ ] No compilation errors

---

## Section 3: DAO Interface (8 points)

- [ ] ScanHistoryDao interface exists
- [ ] @Dao annotation present
- [ ] @Insert method implemented
- [ ] @Query for getAll() implemented with correct SQL
- [ ] @Query for getById() implemented
- [ ] @Delete method implemented
- [ ] All methods have correct return types
- [ ] No SQL syntax errors

---

## Section 4: Database Class (8 points)

- [ ] AppDatabase class exists
- [ ] Extends RoomDatabase
- [ ] @Database annotation with entities and version
- [ ] Abstract scanHistoryDao() method
- [ ] Singleton pattern implemented
- [ ] getInstance() method is synchronized
- [ ] Room.databaseBuilder configured correctly
- [ ] Database name set

---

## Section 5: History List UI (12 points)

- [ ] HistoryListActivity created
- [ ] activity_history_list.xml layout exists
- [ ] RecyclerView in layout
- [ ] Empty state TextView in layout
- [ ] item_scan_history.xml item layout exists
- [ ] Item layout has TextViews for disease, confidence, timestamp
- [ ] ScanHistoryAdapter class created
- [ ] ViewHolder inner class implemented
- [ ] RecyclerView displays scans from database
- [ ] List scrolls smoothly
- [ ] Empty state shows when no scans
- [ ] Back button works

---

## Section 6: Database Operations (10 points)

- [ ] ExecutorService used for all database operations
- [ ] No database operations on main thread
- [ ] Insert operation works (scan saved)
- [ ] Query all operation works (list populated)
- [ ] Query by ID operation works (detail view)
- [ ] Delete operation works (scan removed)
- [ ] Toast confirmation after insert
- [ ] No crashes on empty database
- [ ] Data persists after app restart
- [ ] Timestamps stored correctly

---

## Section 7: Integration with Prediction (8 points)

- [ ] Scan automatically saved after successful prediction
- [ ] All prediction data saved (disease, confidence, symptoms, treatment)
- [ ] Timestamp generated at save time
- [ ] Image path saved
- [ ] Save happens on background thread
- [ ] Toast confirms "Scan saved to history"
- [ ] New scan appears in history list
- [ ] Multiple predictions save correctly

---

## Section 8: Detail View (8 points)

- [ ] HistoryDetailActivity created
- [ ] activity_history_detail.xml exists
- [ ] Layout shows all scan fields
- [ ] Navigation from list to detail works
- [ ] Scan ID passed via Intent
- [ ] Correct scan loaded by ID
- [ ] All fields display correctly
- [ ] Back button returns to list

---

## Section 9: Delete Functionality (10 points)

- [ ] Delete button/option exists
- [ ] Delete triggers confirmation AlertDialog
- [ ] Dialog has title and message
- [ ] Positive button labeled "Delete"
- [ ] Negative button labeled "Cancel"
- [ ] Cancel keeps scan (doesn't delete)
- [ ] Confirm deletes scan from database
- [ ] UI updates after delete
- [ ] Toast shows "Scan deleted"
- [ ] App doesn't crash on delete

---

## Section 10: Data Formatting (6 points)

- [ ] Timestamps displayed in readable format (not raw milliseconds)
- [ ] Confidence displayed as percentage (e.g., "87.5%")
- [ ] Dates formatted clearly (e.g., "May 25, 2024")
- [ ] No null pointer exceptions on display
- [ ] Long text truncates or wraps properly
- [ ] UI doesn't break with special characters

---

## Section 11: Edge Cases (8 points)

- [ ] App handles empty history gracefully
- [ ] App handles 20+ scans (performance test)
- [ ] Delete all scans doesn't crash app
- [ ] Re-opening app after delete shows updated list
- [ ] Rotating device doesn't lose data
- [ ] Killing and restarting app preserves history
- [ ] Null checks prevent crashes
- [ ] No memory leaks (Context references)

---

## Section 12: Code Quality (4 points)

- [ ] No hardcoded strings (use strings.xml)
- [ ] Meaningful variable names
- [ ] Singleton prevents multiple database instances
- [ ] No unused imports
- [ ] Consistent code style

---

## Final Validation Score

**Total Points:** 100

**Your Score:** _____ / 100

**Pass Threshold:** 85/100

**Status:** [ ] PASS | [ ] FAIL

---

## Teacher Demonstration

Must successfully demonstrate:

1. View empty history (shows empty message)
2. Perform 3 predictions (scans auto-save)
3. View history list (shows 3 scans)
4. Open scan detail (all fields display)
5. Delete scan with confirmation
6. Return to list (2 scans remain)
7. Close and reopen app (data persists)

---

**Date Validated:** _________

**Next:** Week 08 - XML Disease Library


<!-- NAV_FOOTER_START -->

---

## 📚 Week 07 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| **5** | **validation-checklist.md** ← *You are here* | **Validation & Verification** |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Build Implementation Guide](build-task.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Validation & Verification** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Knowledge Assessment Quiz →](quiz.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 06: Cloud ML Model](../week-06-cloud-ml-model/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 08: XML Disease Library ➡](../week-08-xml-disease-library/README.md) |

---
