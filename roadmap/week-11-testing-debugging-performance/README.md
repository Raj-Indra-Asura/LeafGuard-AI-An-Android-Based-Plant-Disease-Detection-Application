# Week 11: Testing, Debugging, and Performance Evidence

## Mindset

Week 10 completes the product feature set. Week 11 does not add user-facing capability. It turns assumptions into repeatable evidence:

> Test pure contracts quickly, test Android/Room behavior on a device, run backend/model regressions, inspect lint, measure offline smoke performance, and record reproducible failures.

## Progressive Handoff

| Earlier result | Week 11 proof |
|---|---|
| Eight-field response | Gson contract unit test |
| XML catalog | 10-entry JVM contract test |
| Share privacy/rounding | Pure formatter tests |
| Room CRUD/order/location | In-memory instrumentation test |
| Room v1->v2 | MigrationTestHelper + exported schemas |
| Result/settings UI | Espresso visibility tests |
| Offline classifier | Device smoke/performance test |
| Backend/Keras/TFLite | Existing Python regression suites |

## Test Pyramid

| Layer | Count | Environment | Status reproduced here |
|---|---:|---|---|
| Kotlin JVM tests | 4 | Host JVM | Passed |
| Backend API tests | 8 | Python | Passed |
| Keras contract tests | 4 | Python/TensorFlow | Passed |
| TFLite contract/parity tests | 4 | Python/TensorFlow | Passed |
| Android instrumentation tests | 5 | Emulator/device | Compiled; execution requires connected device |
| Lint/build tasks | 2+ | Gradle | Passed |

**20 automated non-device tests executed and passed.** Five instrumentation tests remain explicit connected-device gates.

## Exact Delta

| Change | Count | Files |
|---|---:|---|
| New | 11 | formatter, 3 JVM tests, 5 instrumentation tests, 2 Room schema JSON files |
| Expanded | 3 | Gradle test config, AppDatabase schema export, ResultActivity formatter use |
| User-visible behavior changes | 0 | Existing formatting preserved |
| Week 12 release changes | 0 | Deferred |

| File | Lines |
|---|---:|
| `app/build.gradle` | 82 |
| `database/AppDatabase.kt` | 45 |
| `ResultActivity.kt` | 221 |
| `ResultTextFormatter.kt` | 28 |
| `ResultTextFormatterTest.kt` | 31 |
| `PredictionResponseContractTest.kt` | 37 |
| `DiseaseCatalogContractTest.kt` | 30 |
| `ScanDaoInstrumentedTest.kt` | 71 |
| `AppDatabaseMigrationTest.kt` | 67 |
| `ResultActivityInstrumentedTest.kt` | 38 |
| `SettingsActivityInstrumentedTest.kt` | 22 |
| `OfflineInferencePerformanceTest.kt` | 35 |
| Room schema `1.json` | 88 |
| Room schema `2.json` | 100 |
| **Total** | **895** |

Full files appear in [learning-notes.md section 12](learning-notes.md#12-end-of-week-11-file-inventory-exact-files-exact-code-exact-size).

## What Each Test Proves

- Unit test: deterministic code/data contract in isolation.
- Instrumentation: Android framework, Room, assets, Activity views, TFLite runtime.
- Backend/model suites: server and conversion regressions.
- Lint: static Android correctness/quality signals.
- Performance smoke: one broad upper budget, not benchmarking science.

Passing tests do not prove no bugs. They prove the tested contracts under stated environments.

## CSE 2206 Connection

- JUnit assertions and test fixtures
- test doubles versus real framework dependencies
- AndroidJUnit4 and Espresso
- in-memory Room and migration schemas
- regression testing and test pyramid
- stack traces, Logcat, hypotheses, focused retesting
- static analysis and performance measurement
- reproducibility and evidence quality

## Milestone

1. Run four JVM tests.
2. Run eight backend tests.
3. Run four Keras and four TFLite tests.
4. Compile five instrumentation tests.
5. On emulator/device, execute all five instrumentation tests.
6. Show migration preservation and offline smoke elapsed time.
7. Run lint/build.
8. Demonstrate one red->diagnose->fix->green debugging record.
9. Explain residual manual risks.

## Exact Completion

| Quantity | Value |
|---|---:|
| New files | 11 |
| Expanded files | 3 |
| Lines | 895 |
| Non-device tests passed | 20 |
| Instrumentation tests | 5 compiled and device-run required |
| Exported Room schemas | 2 |
| User-visible feature changes | 0 |

<!-- NAV_FOOTER_START -->

---

[Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Week 10](../week-10-notifications-share-location/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 12](../week-12-final-submission/README.md)