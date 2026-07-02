# LeafGuard AI - Kotlin Android Application Setup Guide

## Overview

This is the **Kotlin + XML parallel track** of the LeafGuard AI Android app — an exact
functional twin of the Java app in [`android-app/`](../android-app/). Same features, same
screens, same behavior, same API contract, same Room database schema, same on-device
TensorFlow Lite model. Only the language layer differs: every `.java` class has a `.kt`
counterpart with the same name in the same `com.leafguard` package.

> **Why two apps?** The CSE 2206 course explicitly requires "Java for Android
> development", so [`android-app/`](../android-app/) remains the primary, course-aligned
> track. This Kotlin track is a parallel enrichment path for learning modern Android
> idioms by direct comparison. See [`docs/parallel-track/README.md`](../docs/parallel-track/README.md)
> and the file-by-file consistency contract in [`docs/JAVA_VS_KOTLIN.md`](../docs/JAVA_VS_KOTLIN.md).

## Prerequisites

- **Android Studio**: Hedgehog (2023.1.1) or later recommended (any release bundling AGP 8.2 support)
- **Java Development Kit (JDK)**: JDK 17 recommended for AGP 8.x (bytecode targets Java 11)
- **Android SDK**: API Level 24 (Android 7.0) minimum, API Level 34 compile target
- **Physical Android Device** or **Emulator** with camera support
- **Minimum 8GB RAM** on development machine

## Project Structure

```
android-app-kotlin/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/leafguard/            # Kotlin sources (.kt) mirroring the Java tree
│   │       │   ├── MainActivity.kt            # Home: capture, mode toggle, detection
│   │       │   ├── ResultActivity.kt          # Prediction result, share, save to history
│   │       │   ├── HistoryActivity.kt         # Saved scans list (RecyclerView)
│   │       │   ├── HistoryDetailActivity.kt   # Single scan detail
│   │       │   ├── DiseaseLibraryActivity.kt  # XmlPullParser disease reference
│   │       │   ├── SettingsActivity.kt        # Backend URL + confidence threshold
│   │       │   ├── database/
│   │       │   │   ├── ScanRecord.kt          # @Entity (table "scan_history")
│   │       │   │   ├── ScanDao.kt             # @Dao with suspend functions
│   │       │   │   └── AppDatabase.kt         # @Database singleton ("leafguard.db")
│   │       │   ├── network/
│   │       │   │   ├── ApiService.kt          # @Multipart @POST("predict")
│   │       │   │   ├── RetrofitClient.kt      # object singleton, 10.0.2.2 base URL
│   │       │   │   └── PredictionResponse.kt  # Gson data class
│   │       │   ├── ml/
│   │       │   │   └── TFLiteClassifier.kt    # 224×224 RGB 0..1, heuristic fallback
│   │       │   └── utils/
│   │       │       └── NotificationHelper.kt  # channel "leafguard_scan_reminders"
│   │       ├── res/                           # XML layouts/values — identical to the Java track
│   │       ├── assets/                        # model.tflite, labels.txt, diseases.xml (identical)
│   │       └── AndroidManifest.xml            # identical activities/permissions
│   ├── build.gradle                           # Module config (Kotlin + kapt, see below)
│   └── proguard-rules.pro
├── gradle/wrapper/                            # Gradle 8.2 wrapper (same as Java track)
├── build.gradle                               # AGP 8.2.0 + Kotlin 1.9.22 plugins
├── settings.gradle                            # rootProject.name = "LeafGuardAI-Kotlin"
└── gradle.properties
```

## Build & Run

```bash
cd android-app-kotlin
./gradlew assembleDebug        # build the APK
./gradlew installDebug         # install on a connected device/emulator
```

Or open `android-app-kotlin/` in Android Studio and press Run.

> ⚠️ Both tracks use `applicationId "com.leafguard"` — install only one app at a time
> on a given device. Installing one replaces the other (after uninstall, since the
> signing debug keys differ per machine, adb may require `adb uninstall com.leafguard` first).

## Key Build Configuration Differences vs the Java Track

The dependency set is **identical** to `android-app/app/build.gradle`, plus:

```gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'   // Kotlin language support
    id 'kotlin-kapt'                    // Kotlin annotation processing
}

android {
    // ... identical to Java track ...
    kotlinOptions {
        jvmTarget = "11"                // matches sourceCompatibility VERSION_11
    }
}

dependencies {
    // Kotlin additions
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
    implementation 'androidx.activity:activity-ktx:1.8.2'
    implementation 'androidx.room:room-ktx:2.6.1'

    // ⚠️ CRITICAL: Room codegen must use kapt in Kotlin modules.
    // annotationProcessor silently generates NOTHING in a Kotlin module and the
    // app crashes at runtime with "cannot find implementation for AppDatabase".
    kapt "androidx.room:room-compiler:$room_version"
}
```

## Kotlin Idioms Used (behavior unchanged)

| Java pattern | Kotlin equivalent here |
|---|---|
| POJOs (`PredictionResponse`, `ScanRecord`) | `data class` |
| `ExecutorService` + `runOnUiThread` for Room | `suspend fun` DAO + `lifecycleScope.launch { }` |
| `RetrofitClient` / `NotificationHelper` static utility classes | `object` singletons |
| `public static final` constants | `companion object` + `const val` |
| try-with-resources for `TFLiteClassifier` | `.use { }` |
| Anonymous listeners | lambdas / function types |
| Bitwise `\|` on Intent/PendingIntent flags | infix `or` |

Everything user-visible is identical: screen flow, intent extras keys
(`extra_disease_name`, `extra_scan_id`, …), Room table `scan_history` in
`leafguard.db`, the `POST /predict` multipart contract against the FastAPI backend
(default `http://10.0.2.2:8000/`), the 224×224 RGB 0..1 TFLite preprocessing with
heuristic fallback, notification channel `leafguard_scan_reminders`, and the share
text templates.

## Backend & Model

This track shares the single FastAPI backend (`backend-api/`) and model pipeline
(`model/`) with the Java track — do not duplicate them. Start the backend as
described in [`android-app/README.md`](../android-app/README.md) and
[`backend-api/`](../backend-api/); the default emulator base URL `http://10.0.2.2:8000`
is configurable in the Settings screen exactly as in the Java app.

`assets/labels.txt` must stay in sync (same 10 labels, same order) with
`model/labels.txt`, the Java track's `assets/labels.txt`, and `assets/diseases.xml`.

## Week-by-Week Reference

The full week-by-week setup narrative (creating the project, adding camera, Retrofit,
Room, TFLite, etc.) lives in [`android-app/README.md`](../android-app/README.md) and
applies to this track as-is, with three substitutions:

1. Choose **Language: Kotlin** when creating the project.
2. Wherever the guide adds `annotationProcessor "androidx.room:room-compiler"`, use
   `kapt "androidx.room:room-compiler"` (and apply the `kotlin-kapt` plugin).
3. Wherever the guide uses an `ExecutorService` for database work, use
   `lifecycleScope.launch { }` with the `suspend` DAO functions.

Kotlin exercise skeletons: [`exercises/android-kotlin/`](../exercises/android-kotlin/).

## Troubleshooting

| Symptom | Cause | Fix |
|---|---|---|
| `cannot find implementation for AppDatabase` at runtime | Room compiler declared with `annotationProcessor` | Use `kapt "androidx.room:room-compiler"` + `kotlin-kapt` plugin |
| Unresolved reference: `lifecycleScope` | Missing lifecycle KTX | `implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'` |
| `Suspend function should be called only from a coroutine` | Calling suspend DAO outside a coroutine | Wrap in `lifecycleScope.launch { }` |
| Kotlin/Java target mismatch error | `jvmTarget` differs from `compileOptions` | Both are set to 11 in this project — keep them equal |
| App predicts "Tomato Healthy/Early/Late Blight" only | Starter model missing/invalid → heuristic fallback active | Expected with the stub model; replace `assets/model.tflite` with a trained model (same behavior as Java track) |
