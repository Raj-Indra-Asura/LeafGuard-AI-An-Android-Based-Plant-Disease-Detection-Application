# Week 10 Reflection: Useful Features Without Privacy Overreach

Save answers in `docs/evidence/week-10/reflection-answers.md` and cite observed behavior.

## Progression

1. How do utilities reuse Week 09 results without changing inference?
2. What user value does each of sharing, location, and reminders add?
3. Which privacy choices intentionally limit scope?

## Sharing

4. Explain implicit Intent, chooser, MIME type, and payload.
5. Why exclude image and location?

## Location and Room

6. Explain opt-in timing and grant/deny/unavailable paths.
7. Why nullable Double instead of 0.0?
8. Explain version 1->2 migration and evidence old rows survived.

## Notifications

9. Explain channel versus runtime permission.
10. Explain unique periodic work, approximate timing, and cancellation.
11. Explain permission recheck inside posting helper.
12. Explain preference state across reopen/denial.

## Debugging

13. Document one permission/migration/worker debugging cycle with hypothesis, discriminating check, fix, focused/full retest, evidence.

## Confidence

| Topic | 1-10 | Evidence | Next practice |
|---|---:|---|---|
| Sharing/privacy | | | |
| Runtime permission | | | |
| Room migration | | | |
| Nullable location | | | |
| Channel/notification | | | |
| WorkManager uniqueness | | | |
| Preferences | | | |

## Week 11 Handoff

State what complete feature set now exists, what remains manually validated, and how Week 11 adds automated tests/debugging/performance evidence without changing behavior.

## Completion

- [ ] I cited share, location grant/deny, migration, reminder enable/disable evidence.
- [ ] I explained privacy boundaries and approximate scheduling.
- [ ] I did not claim model changes.
- [ ] I completed Week 11 handoff and progress tracker.

<!-- NAV_FOOTER_START -->

---

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | **Reflection**