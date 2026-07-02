# Week 06 Evidence — Cloud ML Model (mock predictor + contract)

> **Why collect evidence?** Screenshots and notes prove your work for the CSE 2206
> submission and help you debug later. Save everything in this folder.
>
> This week's tasks: [`roadmap/week-06-cloud-ml-model/README.md`](../../../roadmap/week-06-cloud-ml-model/README.md)
> (Kotlin is the primary track — take your screenshots in `android-app-kotlin/` unless
> a step says otherwise.)

## Screenshots to take this week (exactly these, and why)

- [ ] **`prediction-json.png`** — The raw JSON response from `POST /predict` (from Swagger UI or curl) showing `disease`, `confidence`, `symptoms`, `treatment`, `prevention`.
- [ ] **`cloud-vs-labels.png`** — `model/labels.txt` next to a prediction result — proves the label set matches.
- [ ] **`sample-image-test.png`** — A detection run on one of the `sample-images/` starter images.

## Also collect

- [ ] Short notes on any error you hit and how you fixed it (`notes.md`)
- [ ] Your git commit list for the week (`git log --oneline` output pasted into `commits.txt`)
- [ ] The week's completed `validation-checklist.md` (tick the yes/no boxes)

## Folder layout & naming

```
week-06/
├── README.md      (this file)
├── screenshots/   (the images above)
├── notes.md
└── commits.txt
```

Name files `YYYYMMDD_week06_description.png` (example: `20260702_week06_prediction-json.png`).

## Next week

Week 07 saves results on the phone with the Room database.
