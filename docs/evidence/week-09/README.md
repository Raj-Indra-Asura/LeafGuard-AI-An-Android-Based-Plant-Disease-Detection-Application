# Week 09 Evidence — TensorFlow Lite Offline AI

> **Why collect evidence?** Screenshots and notes prove your work for the CSE 2206
> submission and help you debug later. Save everything in this folder.
>
> This week's tasks: [`roadmap/week-09-tensorflow-lite-offline-ai/README.md`](../../../roadmap/week-09-tensorflow-lite-offline-ai/README.md)
> (Kotlin is the primary track — take your screenshots in `android-app-kotlin/` unless
> a step says otherwise.)

## Screenshots to take this week (exactly these, and why)

- [ ] **`offline-mode-result.png`** — A detection result in Offline mode (airplane mode on) — heuristic fallback or real model.
- [ ] **`model-asset.png`** — `app/src/main/assets/model.tflite` in the Project panel (placeholder, or your converted model).
- [ ] **`logcat-fallback.png`** — Logcat showing the TFLiteClassifier fallback warning (if still using the placeholder) or successful interpreter load (if you replaced it).

## Also collect

- [ ] Short notes on any error you hit and how you fixed it (`notes.md`)
- [ ] Your git commit list for the week (`git log --oneline` output pasted into `commits.txt`)
- [ ] The week's completed `validation-checklist.md` (tick the yes/no boxes)

## Folder layout & naming

```
week-09/
├── README.md      (this file)
├── screenshots/   (the images above)
├── notes.md
└── commits.txt
```

Name files `YYYYMMDD_week09_description.png` (example: `20260702_week09_offline-mode-result.png`).

## Next week

Week 10 adds notifications, sharing, and optional location.
