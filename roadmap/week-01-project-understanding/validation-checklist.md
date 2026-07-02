# Week 01 Validation Checklist

## Purpose

Use this checklist to verify complete Week 01 completion before proceeding to Week 02. ALL items must be checked. Do not skip items or proceed if anything is incomplete.

**⚠️ IMPORTANT:** Week 02 assumes Week 01 is 100% complete. Skipping validation will cause problems later.

---

## Section 1: Documentation Completeness

### Project Proposal

- [ ] **Does the proposal document exist** at `docs/proposal.md` or `docs/proposal.pdf`?
- [ ] **Is it minimum 8 pages** (excluding title page and references)?
- [ ] **Is the title page complete** with your name, roll number, course code, guide name, date?
- [ ] **Is the abstract 150-200 words** and does it include problem, solution, technologies, outcomes?
- [ ] **Does the introduction section** explain agricultural problem, smartphone trends, ML advances?
- [ ] **Is the problem statement** specific to plant disease detection challenge?
- [ ] **Are the objectives SMART** (Specific, Measurable, Achievable, Relevant, Time-bound)?
- [ ] **Does the scope section clearly define** what IS and IS NOT included?
- [ ] **Does the literature review analyze** minimum 3 existing apps or research papers?
- [ ] **Does the methodology explain** Agile approach, technology stack, implementation phases?
- [ ] **Does the timeline show all 12 weeks** with deliverables?
- [ ] **Does the risk analysis** identify minimum 5 risks with mitigation strategies?
- [ ] **Does the references section** have minimum 10 credible sources cited properly?
- [ ] **Are there no spelling or grammar errors** throughout the document?
- [ ] **Is formal academic tone** maintained (no first person, no slang)?
- [ ] **Are all statistics cited** with sources?

### Architecture Documentation

- [ ] **Does the architecture diagram exist** at `diagrams/system-architecture.png`?
- [ ] **Is the diagram high resolution** (300 DPI or vector format)?
- [ ] **Does it show three layers clearly:** Presentation, Business Logic, Data?
- [ ] **Are all 6 activities labeled:** MainActivity, ResultActivity, HistoryActivity, HistoryDetailActivity, DiseaseLibraryActivity, SettingsActivity?
- [ ] **Are service classes shown** (ApiService, ScanDao, TFLiteClassifier) with connections to Activities?
- [ ] **Is the Room database shown** with scan_history table?
- [ ] **Is the Retrofit/API connection shown** linking to backend?
- [ ] **Is the FastAPI backend shown** with /predict endpoint?
- [ ] **Is the ML model shown** both in backend (TensorFlow) and app (TFLite)?
- [ ] **Are data flow arrows labeled** (e.g., "Image Upload", "JSON Response", "Save to DB")?
- [ ] **Is a legend present** explaining symbols, colors, arrow types?
- [ ] **Are title, your name, and date** visible on diagram?
- [ ] **Is it readable when printed** on A4 paper?

### Syllabus Mapping

- [ ] **Does the syllabus mapping document exist** at `docs/syllabus-mapping.md`?
- [ ] **Are minimum 15 CSE 2206 topics mapped**?
- [ ] **Are Activities & Intents mapped** to specific LeafGuard activities?
- [ ] **Is RecyclerView mapped** to history list or disease library?
- [ ] **Is Retrofit/Networking mapped** to API service classes?
- [ ] **Is Room database mapped** to scan history storage?
- [ ] **Is XML parsing mapped** to disease information parsing?
- [ ] **Is JSON parsing mapped** to API response handling?
- [ ] **Is camera integration mapped** to image capture feature?
- [ ] **Are runtime permissions mapped** to camera/storage permissions?
- [ ] **Does each mapping include file path** or class name?
- [ ] **Does each mapping explain HOW** topic is demonstrated (not just WHERE)?
- [ ] **Is the table well-formatted** and easy to read?

### Senior Repository Analysis

- [ ] **Does the analysis document exist** at `docs/senior-repo-analysis.md`?
- [ ] **Are 5 repositories analyzed** with URLs provided?
- [ ] **Does each repository include basic info** (stars, last update, language)?
- [ ] **Is the architecture pattern identified** for each (direct, MVVM, MVP, MVC, etc.)?
- [ ] **Is the folder structure documented** for each?
- [ ] **Are libraries and versions listed** for each?
- [ ] **Are minimum 3 strengths per repository** documented with specific examples?
- [ ] **Are minimum 3 weaknesses per repository** documented with specific examples?
- [ ] **Are key learnings documented** for each (what to adopt/avoid)?
- [ ] **Is at least one plant disease detection app analyzed**?
- [ ] **Is at least one Retrofit+Room example analyzed**?
- [ ] **Is the analysis specific** (cites actual files, classes, line numbers)?
- [ ] **Is there minimum 2000 words total** (400 per repository)?

### Timeline & Planning

- [ ] **Does the 12-week timeline exist** at `docs/12-week-timeline.md`?
- [ ] **Does each week have a title** describing focus area?
- [ ] **Does each week have 3-5 learning objectives**?
- [ ] **Does each week have 3-5 deliverables**?
- [ ] **Does each week have an evidence collection plan**?
- [ ] **Does each week have a time estimate** (8-15 hours)?
- [ ] **Are milestones defined** at Weeks 4, 8, 12?
- [ ] **Is Week 01 Project Understanding** (current week)?
- [ ] **Is Week 02 Android Basics/UI**?
- [ ] **Is Week 12 Final Submission**?
- [ ] **Is the timeline realistic** (matches course semester dates)?
- [ ] **Is buffer time included** for debugging and delays?
- [ ] **Are contingency plans documented** for common delays?

### Evidence Collection Framework

- [ ] **Does the evidence collection guide exist** at `evidence/README.md`?
- [ ] **Is the purpose of evidence explained**?
- [ ] **Are evidence types defined** (screenshots, commits, videos, docs, tests)?
- [ ] **Are screenshot guidelines specified** (resolution, naming, annotations)?
- [ ] **Are Git commit conventions defined** (format, frequency, categories)?
- [ ] **Are video guidelines specified** (length, quality, content)?
- [ ] **Is there a weekly evidence checklist** for all 12 weeks?
- [ ] **Is the folder structure defined** for organizing evidence?
- [ ] **Is the final submission checklist** documented?

---

## Section 2: Visual Assets Quality

### Diagrams

- [ ] **Is the system architecture diagram** professionally drawn?
- [ ] **Does the data flow diagram** show the complete scan process?
- [ ] **Do all diagrams use consistent styling** (same colors, fonts, arrow styles)?
- [ ] **Are all diagrams saved in multiple formats** (PNG for display, source file for editing)?
- [ ] **Are all diagrams backed up** (committed to Git)?

### Color Scheme

- [ ] **Is a consistent color scheme** used across all diagrams?
- [ ] **Are colors meaningful** (different colors for different layers)?
- [ ] **Are colors accessible** (not relying on color alone for meaning)?
- [ ] **Is there high contrast** (readable in black and white if printed)?

---

## Section 3: Git & Version Control

### Repository Setup

- [ ] **Is the Git repository initialized** (`git init` executed successfully)?
- [ ] **Is the folder structure created:** docs/, diagrams/, evidence/?
- [ ] **Does the .gitignore file exist** with appropriate entries?
- [ ] **Does .gitignore exclude** temporary files, build artifacts, secrets?
- [ ] **Does README.md exist** with project overview?
- [ ] **Does README.md include:** project description, tech stack, status, features, timeline?
- [ ] **Are all planning documents added** to Git?
- [ ] **Is the first commit made** with a meaningful message?

### Git History

- [ ] **Are there at least 3 commits** in Week 01?
- [ ] **Do commit messages follow format:** `Week XX: [Category] - Description`?
- [ ] **Are commit messages meaningful** (not "update" or "changes")?
- [ ] **Can you view commit history** with `git log`?
- [ ] **Does `git status` show a clean working tree** (all files committed)?

### File Organization

- [ ] **Are all documents in the docs/ folder**?
- [ ] **Are all diagrams in the diagrams/ folder**?
- [ ] **Is evidence organized in evidence/week-01/**?
- [ ] **Are there no files in wrong locations**?
- [ ] **Are there no unnecessary files committed** (temp files, system files)?

---

## Section 4: Understanding & Comprehension

### Conceptual Understanding

- [ ] **Can you explain three-tier architecture** in your own words without notes?
- [ ] **Can you explain the direct architecture pattern** used in LeafGuard?
- [ ] **Can you list all 6 activities** and their purposes?
- [ ] **Can you describe the data flow** from camera to result display?
- [ ] **Can you explain Retrofit's role** in the architecture?
- [ ] **Can you explain Room's role** in the architecture?
- [ ] **Can you explain the difference** between cloud AI and on-device AI modes?
- [ ] **Can you list all technologies** used (Android, Retrofit, Room, FastAPI, TFLite)?
- [ ] **Can you explain why service classes are beneficial** (reusability, testability, separation)?

### Technical Knowledge

- [ ] **Do you understand what an Activity is** and its lifecycle?
- [ ] **Do you understand what an Intent is** (explicit vs implicit)?
- [ ] **Do you understand what RecyclerView is** and why it's used?
- [ ] **Do you understand what Retrofit does** (HTTP client)?
- [ ] **Do you understand what Room does** (database abstraction)?
- [ ] **Do you understand what XML parsing means**?
- [ ] **Do you understand what JSON parsing means**?
- [ ] **Do you understand what TensorFlow Lite is**?
- [ ] **Do you understand REST API basics** (GET, POST, endpoints, JSON)?
- [ ] **Do you understand Git basics** (init, add, commit, log, status)?

### Project-Specific Knowledge

- [ ] **Do you know the exact number of activities** in LeafGuard (6)?
- [ ] **Do you know the database table** (scan_history)?
- [ ] **Do you know the backend framework** (FastAPI)?
- [ ] **Do you know the ML framework** (TensorFlow for backend, TFLite for mobile)?
- [ ] **Do you know the networking library** (Retrofit)?
- [ ] **Do you know the database library** (Room)?
- [ ] **Do you know the architecture pattern** (direct Activity-to-services)?
- [ ] **Do you know the development methodology** (Agile with 1-week sprints)?

---

## Section 5: Quality Assurance

### Writing Quality

- [ ] **Are all documents spell-checked**?
- [ ] **Are all documents grammar-checked**?
- [ ] **Is there no informal language** (cool, awesome, etc.)?
- [ ] **Is formal academic tone** maintained throughout?
- [ ] **Is terminology consistent** (don't switch between "app" and "application" randomly)?
- [ ] **Are there proper headings and subheadings**?
- [ ] **Are sections numbered** where appropriate?
- [ ] **Are tables properly formatted**?
- [ ] **Do lists use consistent formatting** (bullets vs numbers)?

### Technical Accuracy

- [ ] **Are all technical terms used correctly** (Activity, Intent, etc.)?
- [ ] **Are all library names spelled correctly** (Retrofit, TensorFlow Lite, Room)?
- [ ] **Are all version numbers accurate** (if specified)?
- [ ] **Do all file paths use correct format** (forward slashes, proper case)?
- [ ] **Do all code snippets have correct syntax** (if included)?
- [ ] **Are all statistics accurate** (or clearly marked as estimates)?

### Completeness

- [ ] **Is there no placeholder text** (no [TODO], [FILL IN], etc.)?
- [ ] **Are there no broken links** (all URLs tested)?
- [ ] **Are there no missing images** (all diagram references work)?
- [ ] **Are there no incomplete sentences**?
- [ ] **Are there no empty sections**?
- [ ] **Are all required sections present** in all documents?

---

## Section 6: Peer Review (Optional but Recommended)

### Self-Review

- [ ] **Have you re-read the proposal** from start to finish?
- [ ] **Have you checked all diagrams** are included where referenced?
- [ ] **Have you verified all links work**?
- [ ] **Have you printed documents** and checked readability?
- [ ] **Have you reviewed commit history** for logical progression?

### External Review

- [ ] **Have you shown the architecture diagram** to a classmate/senior for feedback?
- [ ] **Have you shown the proposal abstract** to someone for clarity check?
- [ ] **Have you asked a senior** if the timeline is realistic?
- [ ] **Have you incorporated feedback** into documents?
- [ ] **Have you made revisions** based on feedback?

---

## Section 7: Final Preparations

### Week 02 Readiness

- [ ] **Android Studio installed** (ready for Week 02)
- [ ] **Java/Kotlin basics reviewed** (if rusty)
- [ ] **Read Android Activity documentation** (basic familiarity)
- [ ] **Familiar with XML layout syntax** (basic understanding)
- [ ] **Android device or emulator ready** for testing

### Evidence Saved

- [ ] **Week 01 folder complete:** evidence/week-01/
- [ ] **All deliverables saved** in appropriate folders
- [ ] **Screenshots taken** of completed work (Git log, folder structure, etc.)
- [ ] **Diagrams exported** in multiple formats (PNG, PDF, source)
- [ ] **Documents backed up** (local + Git + cloud storage)

---

## Section 8: Self-Assessment

Rate yourself honestly (1-5 scale, 5 being highest):

| Area | Rating | Notes |
|------|--------|-------|
| Project understanding | ___ / 5 | Do I fully understand what I'm building? |
| Documentation quality | ___ / 5 | Are my documents professional and complete? |
| Architecture clarity | ___ / 5 | Can I explain the architecture clearly? |
| Syllabus coverage | ___ / 5 | Have I mapped all required topics? |
| Time management | ___ / 5 | Did I complete Week 01 on schedule? |
| Git proficiency | ___ / 5 | Am I comfortable with basic Git commands? |
| Confidence level | ___ / 5 | Do I feel ready for Week 02? |

**If any score is below 3, revisit that area before proceeding to Week 02.**

---

## Section 9: Instructor Sign-Off (If Applicable)

If your course requires instructor approval:

- [ ] **Proposal submitted** to instructor
- [ ] **Feedback received** from instructor
- [ ] **Feedback incorporated** into documents
- [ ] **Instructor approval obtained** to proceed to Week 02

---

## Section 10: Final Verification

### The Ultimate Test

**Can you answer these questions confidently?**

1. What is LeafGuard AI and what problem does it solve?
   - [ ] Yes, confidently
   - [ ] Somewhat
   - [ ] No

2. How does the three-tier architecture work in LeafGuard?
   - [ ] Yes, confidently
   - [ ] Somewhat
   - [ ] No

3. What is MVVM and why does LeafGuard use it?
   - [ ] Yes, confidently
   - [ ] Somewhat
   - [ ] No

4. How does data flow from camera to result display?
   - [ ] Yes, confidently
   - [ ] Somewhat
   - [ ] No

5. What are ALL the technologies used in LeafGuard?
   - [ ] Yes, can list all
   - [ ] Can list most
   - [ ] Not sure

6. What will you build in each of the next 11 weeks?
   - [ ] Yes, have clear plan
   - [ ] Have general idea
   - [ ] Not sure

**If you answered "Yes, confidently" or "Yes, can list all" to all questions, you are ready for Week 02.**

**If you answered "Somewhat" or "No" to any question, review the relevant learning notes before proceeding.**

---

## Completion Declaration

**I hereby declare that:**

- [ ] I have checked ALL items in this validation checklist
- [ ] ALL items are marked as complete
- [ ] I have reviewed all my Week 01 deliverables
- [ ] I understand the LeafGuard AI project completely
- [ ] I am ready to begin Week 02 Android development
- [ ] I will not skip or rush Week 01 just to "start coding faster"

**Date of Completion:** _______________

**Signature:** _______________

---

## What Happens If Validation Fails?

**If any items are NOT checked:**

1. **Stop.** Do not proceed to Week 02.
2. **Identify which items are incomplete.**
3. **Allocate time to complete them** (1-3 hours typically).
4. **Complete the missing items.**
5. **Re-run this validation checklist.**
6. **Only proceed when ALL items are checked.**

**Remember:** Good planning prevents poor performance. Week 01 time is an investment, not a waste.

---

## Next Steps After Validation

Once ALL items are checked:

1. **Take a screenshot** of this completed checklist
2. **Save to:** `evidence/week-01/screenshots/validation-complete.png`
3. **Commit to Git:** `git commit -m "Week 01: Complete validation checklist - ready for Week 02"`
4. **Take a 1-day break** (let your learning settle)
5. **Review Week 02 roadmap:** `/roadmap/week-02-android-basics-ui/README.md`
6. **Install Android Studio** if not already done
7. **Begin Week 02** on the designated start date

---

**Congratulations on completing Week 01! You have built a solid foundation for the next 11 weeks of development. Your thorough planning will pay dividends when you start coding.**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 01 — Navigation

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
| *(First week — no previous)* | [Learning Path](../../LEARNING_PATH.md) | [Week 02: Android Basics & UI ➡](../week-02-android-basics-ui/README.md) |

---
