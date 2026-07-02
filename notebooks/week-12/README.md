# Week 12 Interactive Notebook

## Finishing and Submitting LeafGuard AI

> Work through this Markdown notebook like a lab manual: read, run, test, and explain each checkpoint in your own words.

### How to use this notebook

- Follow the cells in order.
- Kotlin is the primary track for Android code (`android-app-kotlin/`, with a complete Java twin in `android-app/`); Python is used only for backend/model tooling.
- Save screenshots and logs as evidence for CSE 2206.
- Keep the roadmap folder for this week open while you work.

### Weekly outcomes

- Clean the codebase and prepare a signed APK.
- Test the final build on a physical device.
- Organize the report, demo video, GitHub repository, and viva preparation.

### Repository references

- `roadmap/week-12-final-submission/`
- `final-submission/`
- `.github/`

---

## Notebook Cell 1 — Run the pre-submission cleanup checklist

### Explanation

- Submission quality depends on polish: remove dead code, confirm naming consistency, and check that the app still builds.

### Code to Read / Run

```text
Pre-submission cleanup
----------------------
[ ] Remove unused imports and commented-out experiments
[ ] Verify Java class names and file names match
[ ] Confirm all manifest activities are real
[ ] Remove placeholder text such as TODO or Coming Soon from user-facing screens
[ ] Update README and progress logs
[ ] Run tests and record results
```

### 🔵 Try This

- Search the repository for `TODO`, `FIXME`, and `Coming Soon` before you submit.

### Expected Output

- The project feels intentionally finished rather than partially assembled.

### ✅ Checkpoint

- Why is cleanup part of engineering quality instead of optional decoration?

### ⚠️ Common Mistake

- Do not remove comments or files blindly if they still serve documentation value.

### 📌 Key Point

- A clean repository communicates professionalism.

## Notebook Cell 2 — Generate a signed APK step by step

### Explanation

- A release APK must be signed so Android recognizes it as an installable trusted package from you.

### Step-by-Step

1. Open Android Studio and choose Build > Generate Signed Bundle / APK.
2. Select APK.
3. Create a keystore if you do not already have one and store it safely.
4. Choose the `release` build variant.
5. Finish the wizard and wait for build output.

### Code to Read / Run

```gradle
buildTypes {
    release {
        minifyEnabled false
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

### 🔵 Try This

- Write down where your keystore is stored and why it must stay private.

### Expected Output

- Android Studio generates a release APK path at the end of the wizard.

### ✅ Checkpoint

- What is the difference between a debug APK and a signed release APK?

### ⚠️ Common Mistake

- Do not lose your keystore if you want to update the app later under the same identity.

### 📌 Key Point

- Signing is part of real Android delivery.

## Notebook Cell 3 — Test the APK on a physical device

### Explanation

- Real-device testing catches issues that emulators may hide, such as performance, camera behavior, or permission UX.

### Step-by-Step

1. Enable developer options and USB debugging on your phone.
2. Install the release or debug APK.
3. Test camera capture, gallery pick, prediction, history, and notification flow.
4. Record screenshots and video clips during testing.

### 🔵 Try This

- Compare the speed of offline inference on your physical device and on the emulator.

### Expected Output

- You confirm the app works outside the emulator bubble.

### ✅ Checkpoint

- Why is physical-device testing especially important for camera and notification features?

### ⚠️ Common Mistake

- Do not assume emulator success guarantees real-device success.

### 📌 Key Point

- Final validation should happen on the most realistic environment available.

## Notebook Cell 4 — Use a final report structure with word counts

### Explanation

- A strong report balances technical explanation, evidence, and reflection.

### Code to Read / Run

```text
Suggested final report outline
------------------------------
1. Introduction and problem statement (300-400 words)
2. System architecture and technology stack (400-600 words)
3. Android implementation details (700-900 words)
4. Backend and ML integration (600-800 words)
5. Database, offline mode, and extra features (500-700 words)
6. Testing, debugging, and evaluation (400-600 words)
7. Challenges, limitations, and future work (300-500 words)
8. Conclusion (150-250 words)
```

### 🔵 Try This

- Map screenshots and code snippets to each report section before you start writing.

### Expected Output

- Your report outline covers both implementation and evaluation.

### ✅ Checkpoint

- Why should the report discuss both strengths and limitations?

### ⚠️ Common Mistake

- Do not turn the report into only screenshots; explain decisions and outcomes.

### 📌 Key Point

- A report is an engineering narrative, not just a gallery.

## Notebook Cell 5 — Record a better demo video

### Explanation

- A short clear demo often communicates more effectively than a long unplanned recording.

### Step-by-Step

1. Write a 60-90 second script before recording.
2. Show the main menu first.
3. Demonstrate one online or offline scan end to end.
4. Show the history screen and one extra feature such as notification or disease library.
5. End with the project value statement: why LeafGuard AI helps users.

### 🔵 Try This

- Record one practice take and identify any confusing transitions or missing explanations.

### Expected Output

- Your final video feels deliberate and easy to follow.

### ✅ Checkpoint

- What is the one core workflow that absolutely must appear in the demo?

### ⚠️ Common Mistake

- Do not waste time in the video showing only setup screens or waiting on builds.

### 📌 Key Point

- Demo videos should focus on visible value.

## Notebook Cell 6 — Clean the GitHub repository

### Explanation

- Examiners and teammates should be able to understand the repository quickly.

### Step-by-Step

1. Make sure README files explain the project clearly.
2. Remove accidental large binaries that do not belong in the repository history going forward.
3. Ensure folder names are meaningful and consistent.
4. Confirm roadmap, notebooks, solutions, and submission folders are still organized.

### 🔵 Try This

- Pretend you are a new contributor and ask whether the repo landing page explains what to run first.

### Expected Output

- The repository looks coherent and navigable.

### ✅ Checkpoint

- Why is repository organization part of final project quality?

### ⚠️ Common Mistake

- Do not delete evidence or documentation that supports your submission.

### 📌 Key Point

- Good repository hygiene supports both evaluation and maintenance.

## Notebook Cell 7 — Prepare for viva questions

### Explanation

- A viva usually tests understanding, not just whether the app runs.

### Code to Read / Run

```text
Ten common viva questions
-------------------------
1. Why did you choose Java for the Android side of LeafGuard AI?
2. Explain the three-tier architecture used in the project.
3. How does the camera/gallery flow work?
4. Why did you use FastAPI for the backend?
5. What preprocessing does the ML model require?
6. How does Room help with scan history?
7. What is the difference between online and offline prediction modes?
8. How did you test the application?
9. What were the biggest technical challenges?
10. What would you improve in the next version?
```

### 🔵 Try This

- Answer all ten questions without reading your notes, then refine weak answers.

### Expected Output

- You can explain both code and design decisions confidently.

### ✅ Checkpoint

- Which answer still feels weak and needs more practice?

### ⚠️ Common Mistake

- Do not memorize buzzwords without understanding the underlying flow.

### 📌 Key Point

- Confidence in viva comes from understanding and repetition.

## Notebook Cell 8 — Use a submission day checklist

### Explanation

- Final-day mistakes are often simple: wrong file uploaded, missing APK, or incomplete evidence.

### Code to Read / Run

```text
Submission day checklist
------------------------
[ ] Final APK generated and tested
[ ] Source code pushed to GitHub
[ ] Report exported to PDF if required
[ ] Demo video uploaded and link verified
[ ] Screenshots and evidence organized
[ ] Progress log updated
[ ] Team member contributions clarified if applicable
[ ] Viva notes reviewed
[ ] Backup copy stored safely
```

### 🔵 Try This

- Tick each box only after verifying it physically, not just assuming it is done.

### Expected Output

- You reduce last-minute submission risk dramatically.

### ✅ Checkpoint

- Why is a backup copy essential even if your main machine seems fine?

### ⚠️ Common Mistake

- Do not wait until the deadline minute to upload large files.

### 📌 Key Point

- Submission success depends on logistics as well as code.

## Mini Quiz

- What problem does this week solve inside LeafGuard AI?
- Which Java class or Android component did you touch first?
- Which file path in this repository is most relevant to this week?
- What would break if you skipped the validation step?
- How does this week connect to the three-tier architecture?

## Evidence Checklist

- [ ] Capture a screenshot of the completed screen or terminal output.
- [ ] Save one code snippet that proves the feature is wired correctly.
- [ ] Write two sentences in your progress log about what you learned.
- [ ] Record at least one bug and the exact fix you applied.
- [ ] Commit working changes before moving to the next week.

## Reflection Prompt

- Explain the feature from memory without reading the code.
- State one improvement you would add after submission.
- Identify one risk if this feature were left untested.

## Next Step

- Continue to **[Week 12: Final Submission](../../roadmap/week-12-final-submission/README.md)** when this week is stable and documented.


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

### Related Roadmap Materials
- 📖 [Week 12 README](../../roadmap/week-12-final-submission/README.md) — Week overview & objectives
- 📝 [Week 12 Exercises](../../roadmap/week-12-final-submission/exercises.md) — Practice problems
- 💡 [Week 12 Solutions](../../solutions/week-12/README.md) — Reference solutions
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

### Week Progression

| ← Previous | 🏠 | Next → |
|:-----------|:--:|-------:|
| [⬅ Week 11 Notebooks](../week-11/README.md) | [Notebooks Index](../README.md) | *(Last week)* |

---
