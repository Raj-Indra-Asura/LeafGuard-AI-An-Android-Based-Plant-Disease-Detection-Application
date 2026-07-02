# Week 04 Evidence — FastAPI Backend

> **Why collect evidence?** Screenshots and notes prove your work for the CSE 2206
> submission and help you debug later. Save everything in this folder.
>
> This week's tasks: [`roadmap/week-04-fastapi-backend/README.md`](../../../roadmap/week-04-fastapi-backend/README.md)
> (Kotlin is the primary track — take your screenshots in `android-app-kotlin/` unless
> a step says otherwise.)

## Screenshots to take this week (exactly these, and why)

- [ ] **`uvicorn-running.png`** — Terminal showing `uvicorn main:app --reload` running without errors.
- [ ] **`swagger-ui.png`** — `http://localhost:8000/docs` (the automatic API docs page) showing `POST /predict`.
- [ ] **`curl-predict.png`** — Terminal output of `curl -X POST http://localhost:8000/predict -F "image=@sample-images/healthy/tomato_healthy_01.png"` returning JSON with a `disease` field.

## Also collect

- [ ] Short notes on any error you hit and how you fixed it (`notes.md`)
- [ ] Your git commit list for the week (`git log --oneline` output pasted into `commits.txt`)
- [ ] The week's completed `validation-checklist.md` (tick the yes/no boxes)

## Folder layout & naming

```
week-04/
├── README.md      (this file)
├── screenshots/   (the images above)
├── notes.md
└── commits.txt
```

Name files `YYYYMMDD_week04_description.png` (example: `20260702_week04_uvicorn-running.png`).

## Next week

Week 05 connects the Android app to this backend with Retrofit.
