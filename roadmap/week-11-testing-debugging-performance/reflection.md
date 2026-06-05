# Week 11: Reflection - Testing, Debugging, and Performance

## Purpose

This reflection helps you turn hands-on testing into real learning.
A good Week 11 reflection explains not only what worked,
but what failed,
how you investigated it,
and how the app improved because of that process.

---

## Section 1: Testing Philosophy

### 1.1 Why We Test
Write 5-7 sentences explaining why testing is necessary in a mobile app like LeafGuard AI.
Mention ideas such as correctness,
crash prevention,
edge cases,
regression prevention,
and final-submission confidence.

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### 1.2 Manual Testing vs Automated Testing
Compare manual and automated testing in 4-6 sentences.
When is each one useful in your project?

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Section 2: Bugs Found Through Testing

### 2.1 Bug Log Summary

| Bug ID | Feature | Symptom | How Found | Root Cause | Fix |
|------|------|------|------|------|------|
| BUG-01 | | | | | |
| BUG-02 | | | | | |
| BUG-03 | | | | | |

### 2.2 Most Important Bug
Describe the most serious bug you found.
Why was it serious?
How did it affect the user experience?

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### 2.3 Bug Discovery Through Logcat
Describe one bug that you diagnosed using Logcat.
Mention:
- what action triggered it
- what Logcat showed
- exception type
- file and line number
- how you fixed it

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Section 3: Debugging Process Experience

### 3.1 Your Debugging Workflow
Describe your debugging workflow from failure to fix.
Suggested stages: reproduce,
filter Logcat,
read stack trace,
inspect source line,
identify root cause,
fix,
retest.

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### 3.2 Stack Trace Confidence
- **Before Week 11:** ___ / 10
- **After Week 11:** ___ / 10

What changed in your understanding?

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### 3.3 Hardest Debugging Moment
What was the hardest debugging moment this week,
and what finally made it clear?

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Section 4: Unit Testing Experience

### 4.1 What You Tested with JUnit

| Test Class | What It Verifies | Result |
|------|------|------|
| | | |
| | | |
| | | |

### 4.2 Why These Were Good Unit-Test Targets
Explain why your chosen methods were appropriate for local unit tests,
and why they were better targets than UI or camera code.

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### 4.3 Experience with Mockito
If you used Mockito,
explain what you mocked and why.
If you did not complete it,
explain where you would use it in your app.

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Section 5: Espresso UI Testing Experience

### 5.1 UI Flow Tested
Describe the Espresso test you wrote.
What screen did it open,
what action did it perform,
and what did it verify?

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### 5.2 What UI Testing Taught You
What did writing a UI test teach you about stable IDs,
repeatable flows,
and asynchronous behavior?

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Section 6: Performance Results

### 6.1 Raw Results Summary
- **Offline average:** __________ ms
- **Cloud average:** __________ ms
- **Fastest offline:** __________ ms
- **Slowest offline:** __________ ms
- **Fastest cloud:** __________ ms
- **Slowest cloud:** __________ ms
- **App launch time:** __________ seconds

### 6.2 Cloud vs Offline Comparison
Write 6-8 sentences comparing offline and cloud inference.
Discuss speed,
consistency,
and user experience.

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### 6.3 Biggest Performance Bottleneck
What was the slowest part of your app based on testing or profiling?

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Section 7: Memory and Resource Management

### 7.1 Leak Detection Experience
Did LeakCanary report any leaks?
If yes,
what leaked and how did you fix it?
If no,
what did you test to gain confidence?

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### 7.2 Large Image Handling
Explain how you handled or plan to handle large-image memory use.
Why is loading full-resolution Bitmaps risky?

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Section 8: Code Quality Improvements Made

Check improvements you implemented:
- [ ] Added better logging
- [ ] Added null checks
- [ ] Improved exception handling
- [ ] Added unit tests
- [ ] Added UI tests
- [ ] Optimized image loading
- [ ] Improved database queries
- [ ] Fixed memory/resource cleanup
- [ ] Other: _____________________________

### Best Improvement
Which code quality improvement are you most proud of,
and why?

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Section 9: What You Learned About Engineering

### 9.1 Mindset Shift
Before Week 11,
what did "working app" mean to you?
After Week 11,
what does it mean now?

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### 9.2 Skills You Gained
1. _______________________________________________________________
2. _______________________________________________________________
3. _______________________________________________________________
4. _______________________________________________________________
5. _______________________________________________________________

---

## Section 10: Viva Preparation

Write short answers in your own words.

**Q1. What is the difference between `test/` and `androidTest/`?**
_______________________________________________________________
_______________________________________________________________

**Q2. How do you use Logcat to locate a crash?**
_______________________________________________________________
_______________________________________________________________

**Q3. Why is `SystemClock.elapsedRealtime()` useful?**
_______________________________________________________________
_______________________________________________________________

**Q4. What is a memory leak?**
_______________________________________________________________
_______________________________________________________________

**Q5. Why is Espresso useful in a project like LeafGuard AI?**
_______________________________________________________________
_______________________________________________________________

---

## Section 11: Final Reflection

Write a final 8-10 sentence summary covering:
- your testing philosophy
- bugs found
- debugging experience
- performance results
- code quality improvements

Your answer:
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

---

## Completion Checklist
- [ ] I documented at least 3 bugs or issues
- [ ] I recorded performance numbers
- [ ] I reflected honestly on weaknesses and improvements
- [ ] I can explain Week 11 concepts in my own words
