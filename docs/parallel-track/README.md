# Parallel Track: Java & Kotlin Dual-Track System

## What is this?

LeafGuard AI ships **two complete, functionally identical Android apps**:

| Track | Location | Language | Status |
|---|---|---|---|
| **Java track** (primary) | [`android-app/`](../../android-app/) | Java + XML | Course-aligned deliverable for CSE 2206 |
| **Kotlin track** (parallel) | [`android-app-kotlin/`](../../android-app-kotlin/) | Kotlin + XML | Enrichment twin, same behavior |

The Kotlin track is a **translation, not a redesign** — same screens, same navigation,
same Room schema, same Retrofit API contract, same TFLite model handling, same
assets. Only the *language layer* differs.

## Why does it exist?

The CSE 2206 syllabus explicitly requires **"Java for Android development"**, so the
Java app is and remains the primary, course-aligned track. Kotlin is, however, the
language Google recommends for modern Android development. Building the exact same
app in both languages:

1. proves the underlying Android concepts (Activities, Room, Retrofit, TFLite,
   permissions, notifications) are language-independent;
2. teaches idiomatic Kotlin (data classes, coroutines, null safety) by direct
   comparison with familiar Java code;
3. produces portfolio evidence of modern-stack competence without compromising the
   course requirement.

## How the two tracks relate

- **Shared (not duplicated conceptually):** XML layouts/resources (structurally
  identical copies so each Gradle project is self-contained), assets
  (`model.tflite`, `labels.txt`, `diseases.xml`), the FastAPI backend
  (`backend-api/`), the ML notebooks' Python content, and the behavioral contracts.
- **Twinned:** every `.java` file has a `.kt` counterpart at the mirrored path with
  the same class name and package (`com.leafguard`). The complete mapping is the
  consistency contract in [`docs/JAVA_VS_KOTLIN.md`](../JAVA_VS_KOTLIN.md).
- **Behavioral source of truth:** [`00-baseline-validation.md`](00-baseline-validation.md)
  records the exact contracts (screen flow, DB schema, API contract, model I/O) that
  the Kotlin twin reproduces.

## How to build each track

### Java track
```bash
cd android-app
./gradlew assembleDebug
```

### Kotlin track
```bash
cd android-app-kotlin
./gradlew assembleDebug
```

Both use Gradle wrapper 8.2 + AGP 8.2.0, `compileSdk 34`, `minSdk 24`, Java 11
bytecode. The Kotlin track additionally applies the Kotlin Android plugin (1.9.22)
and `kotlin-kapt` (Room codegen requires `kapt`, not `annotationProcessor`, in
Kotlin modules).

> ⚠️ Both apps use `applicationId "com.leafguard"`. They are separate Gradle
> projects and build independently, but you can only install **one at a time** on a
> given device/emulator.

## Documents in this folder

| File | Purpose |
|---|---|
| [`00-baseline-validation.md`](00-baseline-validation.md) | Phase 0 record of the Java baseline: inventory, build status, behavioral contracts |
| [`CHANGES.md`](CHANGES.md) | Deliverable summary: files added, Java↔Kotlin map, verification results |
| [`../JAVA_VS_KOTLIN.md`](../JAVA_VS_KOTLIN.md) | The consistency contract: file-by-file mapping + idiom reference |

## Related learning material

- Kotlin exercise skeletons: [`exercises/android-kotlin/`](../../exercises/android-kotlin/)
- Notebook Kotlin sections: weeks 02, 03, 05, 07, 09, 10, 11, 12 each end with a
  "Parallel Kotlin Track" section mirroring the week's Java lesson (backend/ML
  weeks 04 and 06 are Python-only and intentionally untouched).
