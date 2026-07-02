# Android Development Exercises — Kotlin Track

## Overview

This directory is the **Kotlin twin** of [`exercises/android/`](../android/). It contains the
same hands-on Android development exercises for the LeafGuard AI project, with starter
skeletons written in **Kotlin** for the parallel Kotlin track
([`android-app-kotlin/`](../../android-app-kotlin/)).

> The Java track (`exercises/android/`) remains the CSE 2206 course-aligned standard.
> This Kotlin track is a parallel enrichment path — same pedagogy, same TODOs, same
> verification checklists, expressed in idiomatic Kotlin.

## Weekly Mapping

| Week | Topic | Exercises |
|------|-------|-----------|
| 2 | Android Basics & UI | 1.1 – 2.3 |
| 3 | Camera & Gallery | 3.1 – 3.5 |
| 5 | Retrofit Networking | 4.1 – 4.5 |
| 7 | Room Database | 5.1 – 5.6 |
| 9 | TensorFlow Lite | 6.1 – 6.5 |
| 10 | Notifications & Share | 7.1 – 7.4 |
| 11 | Integration & Testing | 8.1 – 8.4 |

## Starter Code Files

Unlike a runnable Python script, Android code needs the full app project to
compile. These starter `.kt` skeletons give you a structured starting point —
copy each into the matching package under
`android-app-kotlin/app/src/main/java/com/leafguard/` and complete the `// TODO`s.

| File | Week | Maps to (in the Kotlin app) | Java twin |
|------|------|-----------------------------|-----------|
| [`ex01_MainActivity.kt`](ex01_MainActivity.kt) | 2 | `MainActivity.kt` | [`ex01_MainActivity.java`](../android/ex01_MainActivity.java) |
| [`ex02_ScanActivity.kt`](ex02_ScanActivity.kt) | 3 | `ScanActivity.kt` | [`ex02_ScanActivity.java`](../android/ex02_ScanActivity.java) |
| [`ex03_ApiClient.kt`](ex03_ApiClient.kt) | 5 | `network/ApiService.kt`, `network/RetrofitClient.kt` | [`ex03_ApiClient.java`](../android/ex03_ApiClient.java) |
| [`ex04_ScanRepository.kt`](ex04_ScanRepository.kt) | 7 | `database/ScanRecord.kt`, `database/ScanDao.kt`, `database/AppDatabase.kt` | [`ex04_ScanRepository.java`](../android/ex04_ScanRepository.java) |
| [`ex05_LeafClassifier.kt`](ex05_LeafClassifier.kt) | 9 | `ml/TFLiteClassifier.kt` | [`ex05_LeafClassifier.java`](../android/ex05_LeafClassifier.java) |

> These are intentionally incomplete skeletons (skeleton + TODOs), not finished
> solutions. Attempt them first; the completed reference implementations live in
> the Kotlin app's `java/com/leafguard/` packages (Kotlin sources conventionally
> live under the `java/` source root).

## Exercise Task Lists (shared with the Java track)

The full step-by-step exercise descriptions (Ex 1.1 – 8.4), verification
checklists, layouts, and common-mistake notes are **identical for both tracks**
and are maintained in a single place to prevent drift:

➡️ **[`exercises/android/README.md`](../android/README.md)**

Work through that document, but whenever it shows Java code or refers to a
`.java` file, apply the Kotlin equivalent below and target the
`android-app-kotlin/` project instead of `android-app/`.

## Java → Kotlin Idiom Cheat Sheet for These Exercises

| Java (course track) | Kotlin (this track) |
|---|---|
| `findViewById(R.id.x)` + cast | `findViewById<MaterialButton>(R.id.x)` or ViewBinding (`binding.x`) |
| Anonymous `OnClickListener` | Lambda: `button.setOnClickListener { ... }` |
| POJO with getters/setters | `data class` with properties |
| Private constructor + static methods | `object` singleton |
| `static final` constants | `companion object` + `const val` |
| `ExecutorService` background DB access | `suspend fun` DAO + `lifecycleScope.launch { ... }` |
| `runOnUiThread(() -> ...)` | Already on Main dispatcher after `withContext(Dispatchers.IO)` |
| `annotationProcessor "androidx.room:room-compiler"` | `kapt "androidx.room:room-compiler"` (**mandatory** — Room codegen silently fails otherwise) |
| Null checks (`if (x != null)`) | Null safety: `?.`, `?:`, `let` |
| `implements AutoCloseable` | `: AutoCloseable` + `use { ... }` at call sites |
| try-with-resources | `.use { ... }` |

## Verification (same rules as the Java track)

For every exercise:
1. The Kotlin project must build: `cd android-app-kotlin && ./gradlew assembleDebug`
2. Complete the verification checklist embedded in each starter file header.
3. The behavior must be **identical** to the Java version — same screens, same
   flow, same Room schema (`scan_history`), same `/predict` API contract, same
   TFLite preprocessing (224×224, RGB, 0..1).

See [`docs/JAVA_VS_KOTLIN.md`](../../docs/JAVA_VS_KOTLIN.md) for the complete
file-by-file consistency contract between the two tracks.
