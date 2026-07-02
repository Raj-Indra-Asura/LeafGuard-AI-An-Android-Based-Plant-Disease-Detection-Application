# Parallel Kotlin Track — Change Summary & Verification Report

Deliverable summary for the "Kotlin + XML parallel track" task. Base commit: `f56ac3f`.

## 1. Files Added (49)

### `android-app-kotlin/` — the Kotlin twin project (40 files)
- **Gradle:** `build.gradle` (AGP 8.2.0 + Kotlin 1.9.22), `settings.gradle` (`LeafGuardAI-Kotlin`), `gradle.properties` (mirror + Kotlin flags), `gradlew`, `gradle/wrapper/*` (Gradle 8.2, identical to Java track), `app/build.gradle` (identical dep set + Kotlin/coroutines/KTX, Room via **kapt**), `app/proguard-rules.pro` (identical copy)
- **Kotlin sources** (14 `.kt` files, 1:1 mirror of the Java tree under `app/src/main/java/com/leafguard/`): `MainActivity`, `ResultActivity`, `HistoryActivity`, `HistoryDetailActivity`, `DiseaseLibraryActivity`, `SettingsActivity`, `database/{ScanRecord,ScanDao,AppDatabase}`, `network/{ApiService,PredictionResponse,RetrofitClient}`, `ml/TFLiteClassifier`, `utils/NotificationHelper`
- **Shared definitions (identical copies):** `app/src/main/res/**` (8 layouts, values, xml — byte-identical), `AndroidManifest.xml` (byte-identical), `assets/{model.tflite,labels.txt,diseases.xml}` (byte-identical)
- `README.md` (Kotlin-adapted mirror of `android-app/README.md`)

### `exercises/android-kotlin/` (6 files)
`ex01_MainActivity.kt`, `ex02_ScanActivity.kt`, `ex03_ApiClient.kt`, `ex04_ScanRepository.kt`, `ex05_LeafClassifier.kt`, `README.md` — same pedagogy/TODOs/verification checklists as the Java twins, pointing at the Kotlin project.

### `docs/` (3 files)
- `docs/parallel-track/00-baseline-validation.md` — Phase 0 baseline record (source of truth for behavior)
- `docs/parallel-track/README.md` — dual-track system guide
- `docs/parallel-track/CHANGES.md` — this file
- `docs/JAVA_VS_KOTLIN.md` — the consistency contract (file-by-file map + idiom reference)

## 2. Files Modified (10, all additive)
- `README.md` — added `android-app-kotlin/` to the structure tree + a "Dual-Track (Java & Kotlin)" section; Java content untouched
- `SYLLABUS_MAPPING.md` — added a note that Java remains the course-aligned track; no mapping removed
- 8 notebooks (weeks 02, 03, 05, 07, 09, 10, 11, 12) — each gained exactly **one appended** "Parallel Kotlin Track" markdown section mirroring the week's Java lesson (explanation → code comparison → try-this → checkpoint → common mistake → key point). Verified programmatically: every pre-existing cell in all 12 notebooks is byte-identical to the base commit; weeks 01/04/06/08 (backend/ML/XML-python weeks) have **zero** changes.

## 3. Java ↔ Kotlin Map
See [`docs/JAVA_VS_KOTLIN.md`](../JAVA_VS_KOTLIN.md) for the complete path-to-path table
(14 app source twins, 7 build-file twins, 5 exercise twins, shared res/manifest/assets, and
single-copy shared backend/model/notebook-Python).

## 4. Verification Results

### Build & structure
- [x] **Original Java `android-app/` byte-for-byte unmodified** — `git diff` against base shows **0** changed files under `android-app/`.
- [x] **Kotlin project compiles** — full Gradle `assembleDebug` could not run in the CI sandbox because `dl.google.com` (Google Maven, host of AGP and androidx artifacts) is blocked by the sandbox network policy (the identical failure occurs for the unmodified Java project, confirming an environment limitation, not a project fault). As the strongest available substitute, **all 14 Kotlin sources were compiled with `kotlinc 1.9` against the real `android-34/android.jar` plus the real Retrofit 2.9.0 / OkHttp 4.12.0 / Gson 2.10.1 / kotlinx-coroutines 1.7.3 jars from Maven Central** (with minimal API-faithful stubs for androidx/material/TFLite/ViewBinding classes): **0 errors**. The 5 Kotlin exercise skeletons also compile. Additionally verified: every `R.string`/`R.id`/`R.layout` reference and every ViewBinding property used in Kotlin resolves against the shared layouts/strings.
- [x] **1:1 structure mirror** — `find`-based comparison confirms every Java file path has exactly one Kotlin twin path and vice-versa; no orphan files.

### Behavioral parity
- [x] Same 6 Activities, same launcher, same navigation graph (manifest is byte-identical).
- [x] Room: identical table `scan_history`, columns, DB name `leafguard.db`, version 1, and **byte-identical `@Query` SQL** (diff-verified), same newest-first/getById/delete/recent-limit semantics.
- [x] Network: identical `@Multipart @POST("predict")` with `Call<PredictionResponse>`, multipart part name `"image"` (as actually implemented by the Java app and expected by the FastAPI backend's `predict(image: UploadFile)` — documented in `00-baseline-validation.md`), same default base URL `http://10.0.2.2:8000/`, same Settings-pref override + trailing-slash handling, same 30 s timeouts and logging levels.
- [x] TFLite: identical input size 224, RGB /255 normalization, 4 threads, label parsing (skip `#`/blank, pad `Class N`), argmax, and the same green-channel heuristic fallback thresholds (0.48/0.32) and confidences (0.78/0.72/0.69).
- [x] Intent extras keys (`extra_disease_name`, `extra_confidence`, `extra_symptoms`, `extra_treatment`, `extra_prevention`, `extra_image_uri`, `extra_scan_id`), share templates (same `R.string.share_result_template` flow), notification channel/id (`leafguard_scan_reminders`/1001), and permission flows (CAMERA, READ_MEDIA_IMAGES/READ_EXTERNAL_STORAGE, POST_NOTIFICATIONS) — grep-verified identical string constants.
- [x] Same assets: `model.tflite`, `labels.txt`, `diseases.xml` are byte-identical copies; labels order in sync with `model/labels.txt`.

### Consistency contract
- [x] `docs/JAVA_VS_KOTLIN.md` maps every Java file to its Kotlin twin; no unmatched files (shared assets/backend/notebook-Python documented as shared).
- [x] Every `exercises/android/exNN_*.java` has a `exercises/android-kotlin/exNN_*.kt` twin (5/5) + README.
- [x] Notebooks: weeks 02/03/05/07/09/10/11/12 each gained one Kotlin section; all Java/Python content preserved cell-for-cell; weeks 04/06 (and 01/08) untouched.

### Regression safety
- [x] `git diff` against base: **0** changes under `backend-api/`, `model/`, and `android-app/`; no `.tflite`/`labels.txt` modified anywhere.
- [x] No stale references introduced: all new docs link to existing paths; Java-track references unchanged.

## 5. Follow-ups Requiring a Real Environment (CI cannot run these)
1. **Gradle builds of both tracks** on a machine with Google Maven access:
   `cd android-app && ./gradlew assembleDebug` and `cd android-app-kotlin && ./gradlew assembleDebug`
   (the Kotlin build also exercises `kapt` Room codegen and ViewBinding generation end-to-end).
2. **Manual device/emulator testing:** camera capture via FileProvider, gallery pick,
   runtime permission dialogs, cloud detection against a running FastAPI backend,
   offline TFLite detection, history save/load/delete, share sheet, notification display.
3. Install note: both APKs use `applicationId "com.leafguard"` — install one at a time.
