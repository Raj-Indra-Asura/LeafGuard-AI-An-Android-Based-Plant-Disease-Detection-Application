# Week 07 Evidence — Room / SQLite History

> **Why collect evidence?** Screenshots and notes prove your work for the CSE 2206
> submission and help you debug later. Save everything in this folder.
>
> This week's tasks: [`roadmap/week-07-room-sqlite-history/README.md`](../../../roadmap/week-07-room-sqlite-history/README.md)
> (Kotlin is the primary track — take your screenshots in `android-app-kotlin/` unless
> a step says otherwise.)

## Screenshots to take this week (exactly these, and why)

- [ ] **`history-list.png`** — HistoryActivity listing saved scans (newest first) — proves inserts into table `scan_history` work.
- [ ] **`history-detail.png`** — HistoryDetailActivity showing one scan's full details.
- [ ] **`delete-scan.png`** — The history list after deleting a record — proves `deleteScan` works.
- [ ] **`database-inspector.png`** — Android Studio's App Inspection > Database Inspector showing `leafguard.db` and the `scan_history` table.

## Also collect

- [ ] Short notes on any error you hit and how you fixed it (`notes.md`)
- [ ] Your git commit list for the week (`git log --oneline` output pasted into `commits.txt`)
- [ ] The week's completed `validation-checklist.md` (tick the yes/no boxes)

## Folder layout & naming

```
week-07/
├── README.md      (this file)
├── screenshots/   (the images above)
├── notes.md
└── commits.txt
```

Name files `YYYYMMDD_week07_description.png` (example: `20260702_week07_history-list.png`).

## Next week

Week 08 builds the offline disease library from `assets/diseases.xml`.
