# Week 11 Build Task: Build the Regression Matrix

## 1. Freeze Week 10 Behavior

Record that tests must not change user-visible contracts. Extract only `ResultTextFormatter` while preserving rounding/share output.

## 2. Configure Tests

Apply Section 12 Gradle/AppDatabase changes: runner, JUnit/Gson, AndroidX/Espresso, Room testing, coroutine test, schema directory/export.

## 3. Add JVM Tests

Create three classes/four tests. Run:

```bash
cd android-app-kotlin
./gradlew testDebugUnitTest
```

## 4. Add Room Tests

Create in-memory DAO and migration tests. Never edit exported schemas to force green.

## 5. Add UI and Performance Tests

Create Result, Settings, and offline inference instrumentation tests.

Compile first:

```bash
./gradlew compileDebugAndroidTestKotlin
```

Then connect emulator/device:

```bash
./gradlew connectedDebugAndroidTest
```

## 6. Run Python Regressions

Run 8 backend, 4 Keras, and 4 TFLite tests using prior-week commands.

## 7. Build and Lint

```bash
./gradlew assembleDebug lintDebug
```

## 8. Debug One Failure

Preserve failing name/stack, hypothesis, focused check, fix, focused rerun, complete suite rerun.

## 9. Manual Residual Matrix

Test chooser targets, permission dialogs, notification delivery timing, airplane mode, migration from installed v1, repeated offline latency, and physical-device memory.

## Evidence

Save four JVM green, five device green, 16 Python green, build/lint, migration, performance elapsed values, red-green record, and manual matrix. Do not claim instrumentation passed if only compiled.

## Done

20 non-device tests pass, five device tests pass on a connected target, lint/build pass, and residual risks are documented before Week 12.

<!-- NAV_FOOTER_START -->

---

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | **Build Task** | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)