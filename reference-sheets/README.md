# Reference Sheets — Quick Reference Guides

One-page cheat sheets for the things you look up most often while building
LeafGuard AI. For the authoritative inventory of classes, screens, and contracts,
always defer to [`docs/ARCHITECTURE_GROUND_TRUTH.md`](../docs/ARCHITECTURE_GROUND_TRUTH.md).

## Commands Cheat Sheet

### Build & run the apps

```bash
# Kotlin track (primary)
cd android-app-kotlin
./gradlew assembleDebug        # build APK
./gradlew installDebug         # install on device/emulator
./gradlew test                 # run unit tests

# Java track (secondary twin) — same commands from android-app/
cd android-app
./gradlew assembleDebug
```

> ⚠️ Both apps use `applicationId "com.leafguard"` — only one can be installed at
> a time (`adb uninstall com.leafguard` first if switching tracks).

### Backend

```bash
cd backend-api
pip install -r requirements.txt
uvicorn main:app --reload      # serves http://localhost:8000 (emulator: 10.0.2.2:8000)
```

## Key Contracts Cheat Sheet

| Contract | Value |
|---|---|
| Room database | `leafguard.db`, table `scan_history` |
| API endpoint | `POST /predict` (multipart image upload) |
| Default backend URL (emulator) | `http://10.0.2.2:8000` |
| TFLite preprocessing | 224×224, RGB, normalized 0..1 |
| Labels | 10 classes, "Crop Disease" format — keep `assets/labels.txt`, `model/labels.txt`, and `assets/diseases.xml` in sync |
| Notification channel | `leafguard_scan_reminders` |

## Java ↔ Kotlin Idiom Quick Reference

| Java | Kotlin |
|---|---|
| POJO with getters/setters | `data class` |
| `static` utility class | `object` singleton |
| `public static final` constants | `companion object` + `const val` |
| Anonymous `OnClickListener` | lambda `{ ... }` |
| `ExecutorService` + `runOnUiThread` | `suspend fun` DAO + `lifecycleScope.launch { }` |
| `annotationProcessor "androidx.room:room-compiler"` | `kapt "androidx.room:room-compiler"` (**mandatory**) |
| try-with-resources | `.use { }` |
| Bitwise `\|` on Int flags | infix `or` |

Full file-by-file mapping: [`docs/JAVA_VS_KOTLIN.md`](../docs/JAVA_VS_KOTLIN.md).

## Where to look things up

| Need | Go to |
|---|---|
| Term definitions | [`GLOSSARY.md`](../GLOSSARY.md) |
| Real classes/screens/contracts | [`docs/ARCHITECTURE_GROUND_TRUTH.md`](../docs/ARCHITECTURE_GROUND_TRUTH.md) |
| Java↔Kotlin file mapping | [`docs/JAVA_VS_KOTLIN.md`](../docs/JAVA_VS_KOTLIN.md) |
| Environment setup | [`docs/environment-setup.md`](../docs/environment-setup.md) |
| Git workflow | [`docs/git-workflow.md`](../docs/git-workflow.md) |
| Week-by-week navigation | [`LEARNING_PATH.md`](../LEARNING_PATH.md) · [`QUICK_NAV.md`](../QUICK_NAV.md) |
| Viva prep | [`docs/viva-questions.md`](../docs/viva-questions.md) |
