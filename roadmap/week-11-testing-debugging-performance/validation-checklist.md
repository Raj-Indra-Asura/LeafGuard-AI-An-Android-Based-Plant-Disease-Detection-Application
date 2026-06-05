# Week 11: Validation Checklist - Testing, Debugging, and Performance

## Overview

This checklist defines the pass/fail standard for Week 11. A feature is not complete just because it ran once.
You need repeatability,
evidence,
and understanding.

---

## Section 1: JUnit Unit Tests

- [ ] At least **3 local unit tests** exist in `src/test/java`
- [ ] Tests cover real project logic,
      not placeholder code only
- [ ] At least one test covers formatting or helper logic
- [ ] At least one test covers disease-result parsing or similar business logic
- [ ] Tests use clear names
- [ ] Tests follow Arrange,
      Act,
      Assert structure
- [ ] All local unit tests pass
- [ ] `./gradlew test` passes if available

**Pass Criteria:** Minimum 3 passing unit tests.

---

## Section 2: Mockito Usage

- [ ] Mockito dependency added correctly
- [ ] At least 1 dependency mocked successfully
- [ ] Mock-based test does not use real network or database access
- [ ] Mock-based test passes consistently

**Pass Criteria:** Minimum 1 passing Mockito-based test.

---

## Section 3: Espresso UI Tests

- [ ] Espresso dependencies added correctly
- [ ] `testInstrumentationRunner` configured
- [ ] Test file placed in `src/androidTest/java`
- [ ] At least **1 Espresso UI test** exists
- [ ] Test launches MainActivity or another core screen
- [ ] Test performs a real UI action such as `click()`
- [ ] Test contains at least one assertion
- [ ] UI test passes on emulator or device
- [ ] Screenshot of passing UI test saved

**Pass Criteria:** Minimum 1 passing UI test.

---

## Section 4: Logcat Debugging Section

- [ ] I can open Logcat from **View -> Tool Windows -> Logcat**
- [ ] I can filter by package name
- [ ] I can filter by tag
- [ ] I can filter by level
- [ ] I can identify the first useful stack-trace line from my code
- [ ] Bug 1 documented with Logcat evidence
- [ ] Bug 2 documented with Logcat evidence
- [ ] Bug 3 documented with Logcat evidence
- [ ] Each bug record includes symptom,
      root cause,
      fix,
      and verification
- [ ] I can explain what a `Caused by:` chain means

**Pass Criteria:** 3 documented bugs and clear stack-trace understanding.

---

## Section 5: Performance Measurement

- [ ] 10 offline prediction timings recorded
- [ ] 10 cloud prediction timings recorded
- [ ] App launch time measured
- [ ] Timing method documented
- [ ] Offline average calculated
- [ ] Cloud average calculated
- [ ] Fastest and slowest runs recorded
- [ ] Comparison paragraph written
- [ ] Offline result checked against `< 500 ms` target
- [ ] Launch result checked against `< 3 s` target

**Pass Criteria:** Results recorded and interpreted.

---

## Section 6: Profiler Usage

- [ ] CPU Profiler opened at least once
- [ ] Memory Profiler opened at least once
- [ ] Heap dump captured or attempted
- [ ] Network activity observed if cloud mode exists
- [ ] At least one Profiler screenshot saved
- [ ] At least one performance or memory observation written

**Pass Criteria:** Profiler actually used,
not only mentioned.

---

## Section 7: Memory Leak Section

- [ ] LeakCanary added as `debugImplementation`
- [ ] Debug build runs with LeakCanary
- [ ] MainActivity tested for leak behavior
- [ ] ResultActivity tested for leak behavior
- [ ] Repeated navigation or rotation performed if relevant
- [ ] LeakCanary status reviewed
- [ ] LeakCanary shows **no unresolved leaks** in core flow
- [ ] If leaks were found,
      they were documented and fixed
- [ ] Cursor,
      stream,
      or handler cleanup reviewed

**Pass Criteria:** No unresolved core-flow leaks.

---

## Section 8: Code Quality Improvements

- [ ] Meaningful logs added to major classes
- [ ] Null checks improved where needed
- [ ] Error handling improved in at least one major feature
- [ ] Large-image handling improved
- [ ] No new crash introduced by Week 11 work

---

## Section 9: Documentation and Evidence

- [ ] `exercises.md` completed or meaningfully attempted
- [ ] `build-task.md` completed
- [ ] `quiz.md` attempted
- [ ] `reflection.md` completed
- [ ] Screenshot of passing JUnit tests saved
- [ ] Screenshot of passing Espresso test saved
- [ ] Screenshot of Logcat bug evidence saved
- [ ] Screenshot of Profiler or heap dump saved
- [ ] Screenshot or note of LeakCanary result saved
- [ ] Timing benchmark table saved

---

## Section 10: Final Submission Readiness

- [ ] Main app flow still works after Week 11 work
- [ ] Normal demo flow is crash-free
- [ ] At least one edge-case session completed without crash
- [ ] Unit tests green
- [ ] Espresso test green
- [ ] Leak status acceptable
- [ ] Performance results recorded
- [ ] I can explain Logcat confidently
- [ ] I can explain JUnit vs Espresso
- [ ] I can explain what a memory leak is
- [ ] I can explain one optimization and why it helped

---

## Minimum Passing Standard

To pass Week 11,
you must achieve all of the following:

1. [ ] Minimum 3 passing unit tests
2. [ ] Minimum 1 passing Espresso UI test
3. [ ] 3 bugs documented with Logcat evidence
4. [ ] Performance results recorded and analyzed
5. [ ] LeakCanary checked with no unresolved core-flow leaks
6. [ ] Reflection and evidence completed

**Date Completed:** ___________


<!-- NAV_FOOTER_START -->

---

## 📚 Week 11 — Navigation

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
| [⬅ Week 10: Notifications, Share & Location](../week-10-notifications-share-location/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 12: Final Submission ➡](../week-12-final-submission/README.md) |

---
