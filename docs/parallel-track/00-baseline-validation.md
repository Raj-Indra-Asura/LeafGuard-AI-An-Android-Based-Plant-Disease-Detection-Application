# Phase 0 — Baseline Validation of the Java Track

> This document is the **source of truth for behavior**. The Kotlin twin (`android-app-kotlin/`)
> must reproduce every contract listed here exactly. Recorded before any Kotlin file was created.

## 1. Confirmed Inventory of the Java App (`android-app/`)

### Source files — `android-app/app/src/main/java/com/leafguard/`

| File | Role |
|---|---|
| `MainActivity.java` | Home screen: camera/gallery capture, cloud vs offline detection mode toggle, permission flows, launches detection |
| `ResultActivity.java` | Shows a prediction, share intent, save-to-history (Room) |
| `HistoryActivity.java` | Lists saved scans (RecyclerView + inner `HistoryAdapter`), delete, opens detail |
| `HistoryDetailActivity.java` | Full detail of one `ScanRecord` by id, share, delete |
| `DiseaseLibraryActivity.java` | Parses `assets/diseases.xml` with `XmlPullParser` (inner `DiseaseEntry` model + `DiseaseAdapter`), hardcoded 5-entry fallback list |
| `SettingsActivity.java` | Backend URL + confidence threshold in default `SharedPreferences` |
| `database/ScanRecord.java` | Room `@Entity(tableName = "scan_history")` |
| `database/ScanDao.java` | Room `@Dao` |
| `database/AppDatabase.java` | Room `@Database`, singleton, DB name `leafguard.db`, version 1, `fallbackToDestructiveMigration()` |
| `network/ApiService.java` | Retrofit interface |
| `network/PredictionResponse.java` | Gson response POJO |
| `network/RetrofitClient.java` | Retrofit singleton, OkHttp timeouts + logging |
| `ml/TFLiteClassifier.java` | On-device TFLite classifier with heuristic fallback |
| `utils/NotificationHelper.java` | Notification channel + reminder notification |

14 Java files total. No `GalleryActivity`/`ScanActivity` exist. *(Historical note from the baseline audit: at that time no `test/`/`androidTest` sources existed. As of the July 2026 reconstruction, both modules contain `PredictionResponseTest` and `MainActivityTest` — see `docs/JAVA_VS_KOTLIN.md`.)*

### Gradle
- `android-app/build.gradle` — project-level, AGP `8.2.0` via `plugins` block
- `android-app/app/build.gradle` — module-level (dependency set recorded below)
- `android-app/settings.gradle` — `rootProject.name = "LeafGuard AI"`, `include ':app'`
- `android-app/gradle.properties` — `-Xmx2048m`, `android.useAndroidX=true`, `android.enableJetifier=true`
- `android-app/gradlew`, `android-app/gradle/wrapper/` — Gradle wrapper **8.2**

### Resources & assets
- `res/layout/` — `activity_main`, `activity_result`, `activity_history`, `activity_history_detail`, `activity_disease_library`, `activity_settings`, `item_scan_history`, `item_disease_library`
- `res/values/` — `colors.xml`, `strings.xml`, `themes.xml`
- `res/xml/` — `network_security_config.xml`, `file_provider_paths.xml`
- `AndroidManifest.xml` — 6 activities (`MainActivity` exported launcher), FileProvider, `networkSecurityConfig`, cleartext traffic allowed
- `assets/` — `model.tflite`, `labels.txt` (10 labels, "Crop Disease" format), `diseases.xml`

## 2. Build Status of the Baseline

- The Gradle configuration is valid and standard: AGP 8.2.0 + Gradle wrapper 8.2, `compileSdk 34`, Java 11, viewBinding, `noCompress "tflite"`.
- **CI sandbox note:** `./gradlew assembleDebug` in the validation sandbox fails only at dependency
  resolution because `dl.google.com` (Google Maven) is blocked by the sandbox network policy —
  `Plugin ... com.android.application:8.2.0 was not found`. This is an environment network
  limitation, **not** a defect in the Java project. `repo.maven.apache.org` is reachable and the
  same failure mode applies identically to any Android project in this sandbox.
- Baseline soundness was therefore established by full static validation: every source file
  compiles conceptually against the declared dependency set, all layout ids referenced via
  ViewBinding exist in `res/layout/`, all string resources referenced exist in `strings.xml`,
  the manifest registers all 6 activities, and the Room/Retrofit/TFLite contracts are internally
  consistent. Follow-up: run `assembleDebug` for both tracks on a machine with Google Maven access.

## 3. Known-Good Module Dependency Set (`android-app/app/build.gradle`)

- `androidx.appcompat:appcompat:1.6.1`, `com.google.android.material:material:1.11.0`, `androidx.constraintlayout:constraintlayout:2.1.4`, `androidx.recyclerview:recyclerview:1.3.2`, `androidx.preference:preference:1.2.1`
- CameraX `1.3.1` (`camera-core`, `camera-camera2`, `camera-lifecycle`, `camera-view`)
- `retrofit:2.9.0`, `converter-gson:2.9.0`, `okhttp3 logging-interceptor:4.12.0`, `gson:2.10.1`
- TensorFlow Lite `2.14.0` (+ `tensorflow-lite-support:0.4.4`, `tensorflow-lite-gpu:2.14.0`)
- Room `2.6.1` (`room-runtime` + `annotationProcessor room-compiler`)
- `play-services-location:21.1.0`, `androidx.work:work-runtime:2.9.0`
- Tests: `junit:4.13.2`, `mockito-core:5.7.0`, `androidx.test.ext:junit:1.1.5`, `espresso-core:3.5.1`, `androidx.test:runner:1.5.2`, `androidx.test:rules:1.5.0`

## 4. Behavioral Contracts the Kotlin Twin MUST Reproduce

### Screen flow / navigation graph
```
MainActivity (launcher)
 ├─► HistoryActivity ──► HistoryDetailActivity (EXTRA_SCAN_ID: long)
 ├─► DiseaseLibraryActivity
 ├─► SettingsActivity
 └─► ResultActivity (after detection)
       └─► back home: MainActivity with FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP
```

### Intent extras (exact keys)
- `ResultActivity`: `extra_disease_name`, `extra_confidence`, `extra_symptoms`, `extra_treatment`, `extra_prevention`, `extra_image_uri`
- `HistoryDetailActivity`: `extra_scan_id`

### Room schema
- DB name: `leafguard.db`, version 1, `exportSchema = false`, `fallbackToDestructiveMigration()`
- Table `scan_history`, columns: `id` (PK autoGenerate, long), `disease_name` (String), `confidence` (float), `symptoms`, `treatment`, `prevention`, `image_uri` (String), `latitude`, `longitude` (double), `timestamp` (long)
- DAO: `insertScan` (REPLACE, returns long), `getAllScans` (`ORDER BY timestamp DESC`), `deleteScan`, `getRecentScans(limit)` (DESC + LIMIT), `getScanById(id)` (LIMIT 1), `deleteScanById(id)`

### Network contract
- `@Multipart @POST("predict")`, method `uploadImage(@Part MultipartBody.Part)`
- **Multipart form part name as actually implemented: `"image"`** (created in
  `MainActivity.runCloudDetection()` via `MultipartBody.Part.createFormData("image", ...)`;
  the FastAPI backend declares `predict(image: UploadFile = File(...))`). The Kotlin twin
  mirrors `"image"` — behavior parity with the real Java app and backend takes precedence.
- Default base URL `http://10.0.2.2:8000/` (emulator → host); user override via Settings pref `pref_backend_url` (default `http://10.0.2.2:8000`), trailing `/` appended if absent
- OkHttp: 30 s connect/read/write timeouts, `HttpLoggingInterceptor` (BODY when `BuildConfig.DEBUG`, else BASIC), Gson converter

### TFLite / ML contract
- Constructor defaults: `model.tflite`, `labels.txt`, input size **224**
- Memory-mapped model load, `Interpreter.Options` with 4 threads, output classes from output tensor shape `[1]`
- Preprocessing: scale bitmap to 224×224, RGB float32 normalized **/255.0** (0..1), native byte order
- Labels: skip blank lines and lines starting with `#`; if empty add `"Unknown disease"`; pad with `"Class N"` up to `outputClasses`
- Inference: `argmax` over `float[1][outputClasses]`
- **Heuristic fallback** when model asset is missing/invalid: average green channel of 224×224 scaled bitmap; `> 0.48` → "Tomato Healthy" (conf 0.78), `> 0.32` → "Tomato Early Blight" (0.72), else "Tomato Late Blight" (0.69); label lookup by name with index fallback (healthy=2, early=0, late=1); fixed symptoms/treatment/prevention strings
- Implements `AutoCloseable`; `close()` releases the interpreter

### SharedPreferences contract (SettingsActivity)
- `pref_backend_url` (String, default `http://10.0.2.2:8000`), saved on every text change
- `pref_confidence_threshold` (int, default 50, 0–100), saved on SeekBar stop-tracking
- Reset button restores both defaults; app version label from `PackageManager` with `"1.0.0"` fallback

### Notifications
- Channel id `leafguard_scan_reminders`, IMPORTANCE_DEFAULT, created on API ≥ 26
- Reminder notification: id/request code 1001, opens `MainActivity` (`NEW_TASK | CLEAR_TASK`), `FLAG_UPDATE_CURRENT | FLAG_IMMUTABLE`, BigTextStyle, auto-cancel, skipped on API ≥ 33 without `POST_NOTIFICATIONS`

### Permission flows (MainActivity)
- Camera: `CAMERA`; Gallery: `READ_MEDIA_IMAGES` (API ≥ 33) else `READ_EXTERNAL_STORAGE`; notification permission requested at startup on API ≥ 33
- Camera capture via `TakePicture` contract to a FileProvider URI in `<external-files>/Pictures/captures/leafguard_<millis>.jpg`

### Share templates
- `R.string.share_result_template` filled with disease, confidence %, symptoms, treatment, prevention; `ACTION_SEND`, `text/plain`, subject `R.string.share_subject`, chooser `R.string.share_chooser_title`
- Identical template reused by `HistoryDetailActivity` (confidence passed as the already-formatted label text)

### Confidence threshold behavior
- Before opening a result, `MainActivity` warns (`low_confidence_warning` toast) if `confidence*100 < pref_confidence_threshold`; result still opens.

### Assets / labels
- `labels.txt` (both apps and `model/labels.txt`) — fixed 10-class "Crop Disease" list, order is significant and must remain in sync
- `diseases.xml` structure: `<diseases><disease><name/><plant/><symptoms/><treatment/><prevention/></disease>...</diseases>`
