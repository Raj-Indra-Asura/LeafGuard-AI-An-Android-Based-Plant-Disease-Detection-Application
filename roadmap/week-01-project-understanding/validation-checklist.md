# Week 01 Validation Checklist

## Purpose

Use this checklist to verify complete Week 01 completion before proceeding to Week 02. ALL items must be checked. Do not skip items or proceed if anything is incomplete.

**⚠️ IMPORTANT:** Week 02 assumes Week 01 is 100% complete. Skipping validation will cause problems later.

---

## Section 1: Documentation Completeness

### Project Proposal

- [ ] **Proposal document exists** at `docs/proposal.md` or `docs/proposal.pdf`
- [ ] **Minimum 8 pages** (excluding title page and references)
- [ ] **Title page complete** with your name, roll number, course code, guide name, date
- [ ] **Abstract is 150-200 words** and includes problem, solution, technologies, outcomes
- [ ] **Introduction section** explains agricultural problem, smartphone trends, ML advances
- [ ] **Problem statement** is specific to plant disease detection challenge
- [ ] **Objectives are SMART** (Specific, Measurable, Achievable, Relevant, Time-bound)
- [ ] **Scope section clearly defines** what IS and IS NOT included
- [ ] **Literature review analyzes** minimum 3 existing apps or research papers
- [ ] **Methodology explains** Agile approach, technology stack, implementation phases
- [ ] **Timeline shows all 12 weeks** with deliverables
- [ ] **Risk analysis** identifies minimum 5 risks with mitigation strategies
- [ ] **References section** has minimum 10 credible sources cited properly
- [ ] **No spelling or grammar errors** throughout document
- [ ] **Formal academic tone** maintained (no first person, no slang)
- [ ] **All statistics are cited** with sources

### Architecture Documentation

- [ ] **Architecture diagram exists** at `diagrams/system-architecture.png`
- [ ] **Diagram is high resolution** (300 DPI or vector format)
- [ ] **Shows three layers clearly:** Presentation, Business Logic, Data
- [ ] **All 6+ activities labeled:** MainActivity, ScanActivity, ResultActivity, etc.
- [ ] **ViewModels shown** with connections to Activities
- [ ] **Repositories shown** as intermediaries between ViewModels and data sources
- [ ] **Room database shown** with at least 2 tables (scans, users)
- [ ] **Retrofit/API connection shown** linking to backend
- [ ] **FastAPI backend shown** with at least /predict endpoint
- [ ] **ML model shown** both in backend (TensorFlow) and app (TFLite)
- [ ] **Data flow arrows labeled** (e.g., "Image Upload", "JSON Response", "Save to DB")
- [ ] **Legend present** explaining symbols, colors, arrow types
- [ ] **Title, your name, and date** visible on diagram
- [ ] **Readable when printed** on A4 paper

### Syllabus Mapping

- [ ] **Syllabus mapping document exists** at `docs/syllabus-mapping.md`
- [ ] **Minimum 15 CSE 2206 topics mapped**
- [ ] **Activities & Intents mapped** to specific LeafGuard activities
- [ ] **Fragments mapped** (even if minimal use)
- [ ] **RecyclerView mapped** to history list or disease library
- [ ] **Retrofit/Networking mapped** to API service classes
- [ ] **Room database mapped** to scan history storage
- [ ] **XML parsing mapped** to disease information parsing
- [ ] **JSON parsing mapped** to API response handling
- [ ] **Camera integration mapped** to image capture feature
- [ ] **Runtime permissions mapped** to camera/storage permissions
- [ ] **Each mapping includes file path** or class name
- [ ] **Each mapping explains HOW** topic is demonstrated (not just WHERE)
- [ ] **Table is well-formatted** and easy to read

### Senior Repository Analysis

- [ ] **Analysis document exists** at `docs/senior-repo-analysis.md`
- [ ] **5 repositories analyzed** with URLs provided
- [ ] **Each repository includes basic info** (stars, last update, language)
- [ ] **Architecture pattern identified** for each (MVVM, MVP, MVC, etc.)
- [ ] **Folder structure documented** for each
- [ ] **Libraries and versions listed** for each
- [ ] **Minimum 3 strengths per repository** with specific examples
- [ ] **Minimum 3 weaknesses per repository** with specific examples
- [ ] **Key learnings documented** for each (what to adopt/avoid)
- [ ] **At least one plant disease detection app analyzed**
- [ ] **At least one MVVM example analyzed**
- [ ] **At least one Retrofit+Room example analyzed**
- [ ] **Analysis is specific** (cites actual files, classes, line numbers)
- [ ] **Minimum 2000 words total** (400 per repository)

### Timeline & Planning

- [ ] **12-week timeline exists** at `docs/12-week-timeline.md`
- [ ] **Each week has title** describing focus area
- [ ] **Each week has 3-5 learning objectives**
- [ ] **Each week has 3-5 deliverables**
- [ ] **Each week has evidence collection plan**
- [ ] **Each week has time estimate** (8-15 hours)
- [ ] **Milestones defined** at Weeks 4, 8, 12
- [ ] **Week 01 is Project Understanding** (current week)
- [ ] **Week 02 is Android Basics/UI**
- [ ] **Week 12 is Final Submission**
- [ ] **Timeline is realistic** (matches course semester dates)
- [ ] **Buffer time included** for debugging and delays
- [ ] **Contingency plans documented** for common delays

### Evidence Collection Framework

- [ ] **Evidence collection guide exists** at `evidence/README.md`
- [ ] **Purpose of evidence explained**
- [ ] **Evidence types defined** (screenshots, commits, videos, docs, tests)
- [ ] **Screenshot guidelines specified** (resolution, naming, annotations)
- [ ] **Git commit conventions defined** (format, frequency, categories)
- [ ] **Video guidelines specified** (length, quality, content)
- [ ] **Weekly evidence checklist** for all 12 weeks
- [ ] **Folder structure defined** for organizing evidence
- [ ] **Final submission checklist** documented

---

## Section 2: Visual Assets Quality

### Diagrams

- [ ] **System architecture diagram** is professionally drawn
- [ ] **Data flow diagram** shows complete scan process
- [ ] **All diagrams use consistent styling** (same colors, fonts, arrow styles)
- [ ] **All diagrams are saved in multiple formats** (PNG for display, source file for editing)
- [ ] **All diagrams are backed up** (committed to Git)

### Color Scheme

- [ ] **Consistent color scheme** used across all diagrams
- [ ] **Colors are meaningful** (different colors for different layers)
- [ ] **Colors are accessible** (not relying on color alone for meaning)
- [ ] **High contrast** (readable in black and white if printed)

---

## Section 3: Git & Version Control

### Repository Setup

- [ ] **Git repository initialized** (`git init` executed successfully)
- [ ] **Folder structure created:** docs/, diagrams/, evidence/
- [ ] **.gitignore file exists** with appropriate entries
- [ ] **.gitignore excludes** temporary files, build artifacts, secrets
- [ ] **README.md exists** with project overview
- [ ] **README.md includes:** project description, tech stack, status, features, timeline
- [ ] **All planning documents added** to Git
- [ ] **First commit made** with meaningful message

### Git History

- [ ] **At least 3 commits** in Week 01
- [ ] **Commit messages follow format:** `Week XX: [Category] - Description`
- [ ] **Commit messages are meaningful** (not "update" or "changes")
- [ ] **Can view commit history** with `git log`
- [ ] **`git status` shows clean working tree** (all files committed)

### File Organization

- [ ] **All documents in docs/ folder**
- [ ] **All diagrams in diagrams/ folder**
- [ ] **Evidence organized in evidence/week-01/**
- [ ] **No files in wrong locations**
- [ ] **No unnecessary files committed** (temp files, system files)

---

## Section 4: Understanding & Comprehension

### Conceptual Understanding

- [ ] **Can explain three-tier architecture** in own words without notes
- [ ] **Can explain MVVM pattern** and why it's used
- [ ] **Can describe difference** between MVVM and MVC
- [ ] **Can list all 6+ activities** and their purposes
- [ ] **Can describe data flow** from camera to result display
- [ ] **Can explain Retrofit's role** in architecture
- [ ] **Can explain Room's role** in architecture
- [ ] **Can explain difference** between cloud AI and on-device AI modes
- [ ] **Can list all technologies** used (Android, Retrofit, Room, FastAPI, TFLite)
- [ ] **Can explain why MVVM is beneficial** (lifecycle, testability, separation)

### Technical Knowledge

- [ ] **Understand what an Activity is** and its lifecycle
- [ ] **Understand what an Intent is** (explicit vs implicit)
- [ ] **Understand what RecyclerView is** and why it's used
- [ ] **Understand what Retrofit does** (HTTP client)
- [ ] **Understand what Room does** (database abstraction)
- [ ] **Understand what XML parsing means**
- [ ] **Understand what JSON parsing means**
- [ ] **Understand what TensorFlow Lite is**
- [ ] **Understand REST API basics** (GET, POST, endpoints, JSON)
- [ ] **Understand Git basics** (init, add, commit, log, status)

### Project-Specific Knowledge

- [ ] **Know exact number of activities** in LeafGuard (6+)
- [ ] **Know exact number of database tables** (2: scans, users)
- [ ] **Know backend framework** (FastAPI)
- [ ] **Know ML framework** (TensorFlow for backend, TFLite for mobile)
- [ ] **Know networking library** (Retrofit)
- [ ] **Know database library** (Room)
- [ ] **Know architecture pattern** (MVVM)
- [ ] **Know development methodology** (Agile with 1-week sprints)

---

## Section 5: Quality Assurance

### Writing Quality

- [ ] **All documents are spell-checked**
- [ ] **All documents are grammar-checked**
- [ ] **No informal language** (cool, awesome, etc.)
- [ ] **Formal academic tone** maintained throughout
- [ ] **Consistent terminology** (don't switch between "app" and "application" randomly)
- [ ] **Proper headings and subheadings**
- [ ] **Numbered sections** where appropriate
- [ ] **Tables are properly formatted**
- [ ] **Lists use consistent formatting** (bullets vs numbers)

### Technical Accuracy

- [ ] **All technical terms used correctly** (Activity, Intent, Fragment, etc.)
- [ ] **All library names spelled correctly** (Retrofit, TensorFlow Lite, Room)
- [ ] **All version numbers accurate** (if specified)
- [ ] **All file paths use correct format** (forward slashes, proper case)
- [ ] **All code snippets have correct syntax** (if included)
- [ ] **All statistics are accurate** (or clearly marked as estimates)

### Completeness

- [ ] **No placeholder text** (no [TODO], [FILL IN], etc.)
- [ ] **No broken links** (all URLs tested)
- [ ] **No missing images** (all diagram references work)
- [ ] **No incomplete sentences**
- [ ] **No empty sections**
- [ ] **All required sections present** in all documents

---

## Section 6: Peer Review (Optional but Recommended)

### Self-Review

- [ ] **Re-read proposal** from start to finish
- [ ] **Check all diagrams** are included where referenced
- [ ] **Verify all links work**
- [ ] **Print documents** and check readability
- [ ] **Review commit history** for logical progression

### External Review

- [ ] **Shown architecture diagram** to classmate/senior for feedback
- [ ] **Shown proposal abstract** to someone for clarity check
- [ ] **Asked senior** if timeline is realistic
- [ ] **Incorporated feedback** into documents
- [ ] **Made revisions** based on feedback

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
