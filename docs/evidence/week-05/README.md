# Week 05 Evidence — Android Networking with Retrofit

> **Why collect evidence?** Screenshots and notes prove your work for the CSE 2206
> submission and help you debug later. Save everything in this folder.
>
> This week's tasks: [`roadmap/week-05-android-networking/README.md`](../../../roadmap/week-05-android-networking/README.md)
> (Kotlin is the primary track — take your screenshots in `android-app-kotlin/` unless
> a step says otherwise.)

## Screenshots to take this week (exactly these, and why)

- [ ] **`cloud-detection-result.png`** — The app's result screen after a Cloud-mode detection — disease name + confidence % — proves the app↔backend connection works.
- [ ] **`logcat-request.png`** — Logcat showing the OkHttp log of the `POST /predict` request (multipart part `image`).
- [ ] **`settings-url.png`** — SettingsActivity showing the backend URL `http://10.0.2.2:8000`.

## Also collect

- [ ] Short notes on any error you hit and how you fixed it (`notes.md`)
- [ ] Your git commit list for the week (`git log --oneline` output pasted into `commits.txt`)
- [ ] The week's completed `validation-checklist.md` (tick the yes/no boxes)

## Folder layout & naming

```
week-05/
├── README.md      (this file)
├── screenshots/   (the images above)
├── notes.md
└── commits.txt
```

Name files `YYYYMMDD_week05_description.png` (example: `20260702_week05_cloud-detection-result.png`).

## Next week

Week 06 looks at what the cloud model does with your image and how the mock predictor works.
