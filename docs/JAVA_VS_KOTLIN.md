# Java ↔ Kotlin Consistency Contract

This document is the **guarantee against drift** between the two LeafGuard AI Android
tracks. Every Java file must have a documented Kotlin twin and vice-versa. If you add,
rename, or remove a file in either track, update this table in the same commit.

## 1. App Source Files (path-to-path mapping)

Java root: `android-app/app/src/main/java/com/leafguard/`
Kotlin root: `android-app-kotlin/app/src/main/java/com/leafguard/`

| # | Java file | Kotlin twin | Notes |
|---|---|---|---|
| 1 | `MainActivity.java` | `MainActivity.kt` | ExecutorService → `lifecycleScope` + `Dispatchers.IO` for offline detection |
| 2 | `ResultActivity.java` | `ResultActivity.kt` | Same intent extras keys; DB insert via coroutine |
| 3 | `HistoryActivity.java` | `HistoryActivity.kt` | Inner `HistoryAdapter`/`HistoryViewHolder` twinned; listener interfaces → Kotlin lambdas |
| 4 | `HistoryDetailActivity.java` | `HistoryDetailActivity.kt` | Same `EXTRA_SCAN_ID` key |
| 5 | `DiseaseLibraryActivity.java` | `DiseaseLibraryActivity.kt` | Inner `DiseaseEntry` → `data class`; same XmlPullParser flow + 5-entry fallback |
| 6 | `SettingsActivity.java` | `SettingsActivity.kt` | Same pref keys/defaults (`pref_backend_url`, `pref_confidence_threshold`, `http://10.0.2.2:8000`, 50) |
| 7 | `database/ScanRecord.java` | `database/ScanRecord.kt` | POJO → `data class`; identical `@Entity(tableName = "scan_history")` + columns |
| 8 | `database/ScanDao.java` | `database/ScanDao.kt` | Identical SQL; methods are `suspend fun` |
| 9 | `database/AppDatabase.java` | `database/AppDatabase.kt` | Same DB name `leafguard.db`, version 1, singleton via `companion object` |
| 10 | `network/ApiService.java` | `network/ApiService.kt` | Identical `@Multipart @POST("predict")`, `Call<PredictionResponse>` kept for behavior parity |
| 11 | `network/PredictionResponse.java` | `network/PredictionResponse.kt` | POJO → `data class`; identical `@SerializedName` fields |
| 12 | `network/RetrofitClient.java` | `network/RetrofitClient.kt` | static-utility class → `object`; same base URL, timeouts, logging levels |
| 13 | `ml/TFLiteClassifier.java` | `ml/TFLiteClassifier.kt` | Identical 224 input, 0..1 RGB normalization, label parsing, argmax, heuristic fallback |
| 14 | `utils/NotificationHelper.java` | `utils/NotificationHelper.kt` | static-utility class → `object`; same channel id + notification id 1001 |

**No unmatched files on either side.** (Neither track has `GalleryActivity`/`ScanActivity`, and
neither module currently contains `test/`/`androidTest/` sources — the Java module has none to twin.)

## 2. Project / Build Files

| Java track | Kotlin track | Relationship |
|---|---|---|
| `android-app/build.gradle` | `android-app-kotlin/build.gradle` | Twin + adds `org.jetbrains.kotlin.android` 1.9.22 plugin |
| `android-app/app/build.gradle` | `android-app-kotlin/app/build.gradle` | Same dependency set + Kotlin/coroutines/KTX; Room `annotationProcessor` → `kapt`; adds `kotlinOptions { jvmTarget = "11" }` |
| `android-app/settings.gradle` | `android-app-kotlin/settings.gradle` | Twin; `rootProject.name = "LeafGuardAI-Kotlin"` |
| `android-app/gradle.properties` | `android-app-kotlin/gradle.properties` | Twin + `kotlin.code.style=official`, `kapt.incremental.apt=true` |
| `android-app/gradlew`, `gradle/wrapper/*` | same paths | Identical wrapper (Gradle 8.2) |
| `android-app/app/proguard-rules.pro` | `android-app-kotlin/app/proguard-rules.pro` | Identical copy |
| `android-app/README.md` | `android-app-kotlin/README.md` | Kotlin-adapted mirror |

## 3. Shared (structurally identical copies, not translated)

| Asset | Both tracks | Rule |
|---|---|---|
| `app/src/main/res/**` (8 layouts, values, xml) | identical copies | Layout file names must match so ViewBinding generates the same binding class names |
| `app/src/main/AndroidManifest.xml` | identical copies | Same 6 activities, permissions, FileProvider, `networkSecurityConfig` |
| `app/src/main/assets/model.tflite` | identical copies | Same starter model |
| `app/src/main/assets/labels.txt` | identical copies | 10-class list; order must stay in sync with `model/labels.txt` |
| `app/src/main/assets/diseases.xml` | identical copies | Same disease library data |

## 4. Shared, single-copy (NOT duplicated)

- `backend-api/` — one FastAPI backend serves both apps
- `model/` — one model pipeline (`labels.txt`, `generate_stub_model.py`)
- Notebook Python/ML cells — unchanged; Android weeks gained *additive* Kotlin sections
- Exercise task lists — maintained once in `exercises/android/README.md`; the Kotlin
  README references them (skeleton files are twinned: `exercises/android/exNN_*.java` ↔ `exercises/android-kotlin/exNN_*.kt`, 5 files each)

## 5. Exercise Skeleton Mapping

| Java | Kotlin |
|---|---|
| `exercises/android/ex01_MainActivity.java` | `exercises/android-kotlin/ex01_MainActivity.kt` |
| `exercises/android/ex02_ScanActivity.java` | `exercises/android-kotlin/ex02_ScanActivity.kt` |
| `exercises/android/ex03_ApiClient.java` | `exercises/android-kotlin/ex03_ApiClient.kt` |
| `exercises/android/ex04_ScanRepository.java` | `exercises/android-kotlin/ex04_ScanRepository.kt` |
| `exercises/android/ex05_LeafClassifier.java` | `exercises/android-kotlin/ex05_LeafClassifier.kt` |
| `exercises/android/README.md` | `exercises/android-kotlin/README.md` |

## 6. Idiom-Difference Reference

| Concept | Java track | Kotlin track | Behavior impact |
|---|---|---|---|
| Data holders | POJO + getters/setters | `data class` with properties | None — same fields, same Gson/Room mapping |
| Background DB work | `ExecutorService` + `runOnUiThread` | `suspend fun` DAO + `lifecycleScope.launch { }` | None — same queries off the main thread, results applied on the main thread |
| DAO return style | blocking methods returning `List`/`long` | `suspend fun` returning `List`/`Long` | None — same SQL, same results |
| Room codegen | `annotationProcessor "androidx.room:room-compiler"` | `kapt "androidx.room:room-compiler"` | None at runtime; `kapt` is **mandatory** in Kotlin (annotationProcessor silently produces no code) |
| Singletons | `final` class, private ctor, static methods | `object` declaration | None — same lazy, thread-safe access |
| Constants | `public static final` | `companion object` + `const val` | None — same values, same intent-extra/pref keys |
| Null handling | implicit nullability, `if (x != null)` | explicit `?` types, `?.`, `?:` | None — same defaults substituted for missing extras |
| ViewBinding access | `binding.getRoot()`, `setText(...)` | `binding.root`, property assignment | None — same generated binding classes from the shared XML |
| Resource cleanup | try-with-resources | `.use { }` | None — both call `AutoCloseable.close()` |
| Bit flags | `FLAG_A \| FLAG_B` | `FLAG_A or FLAG_B` | None — same integer value |
| Anonymous callbacks | anonymous inner classes | `object : Callback<..>` / lambdas | None — same Retrofit `enqueue` flow |
| Listener interfaces | nested `interface` in adapter | function types `(ScanRecord) -> Unit` | None — same click/delete behavior |

## 7. Drift Rules

1. Any behavioral change (SQL, endpoint, extras key, preprocessing constant, string
   template, pref key/default) must be applied to **both** tracks in the same PR.
2. `res/**`, `AndroidManifest.xml`, and `assets/**` changes must be copied verbatim to
   the other track.
3. Never rename a class in one track only.
4. `model/labels.txt`, both `assets/labels.txt` copies, and `diseases.xml` must keep
   the same 10 labels in the same order.
