# Feature Completion Matrix

> **What is this file?** A simple, honest table of the features that actually exist in
> the LeafGuard AI app, so you can check each one yourself. Every row below maps to real
> code — see [`docs/ARCHITECTURE_GROUND_TRUTH.md`](../docs/ARCHITECTURE_GROUND_TRUTH.md)
> for the class-by-class details. Both tracks have the same features: Kotlin
> (`android-app-kotlin/`, the primary track) and Java (`android-app/`, the secondary
> track).

**How to use it:** run the app, try each feature, and tick the box only when you saw it
work with your own eyes. Beginner phrasing is used on purpose — each check is a plain
yes/no question.

## Real feature set

| # | Feature | Where it lives (Kotlin primary) | Check (yes/no) | Done |
|---|---|---|---|---|
| 1 | Photo capture from camera | `MainActivity.kt` (camera intent + FileProvider) | When I tap **Camera** and take a photo, the photo appears in the preview — yes/no | [ ] |
| 2 | Photo pick from gallery | `MainActivity.kt` (gallery picker) | When I tap **Gallery** and pick an image, it appears in the preview — yes/no | [ ] |
| 3 | Cloud/Offline mode toggle | `MainActivity.kt` (mode toggle buttons) | I can switch between Cloud and Offline mode and the description text changes — yes/no | [ ] |
| 4 | Cloud detection (`POST /predict`, multipart part `image`) | `network/ApiService.kt`, `network/RetrofitClient.kt` + `backend-api/main.py` | With the backend running, sending a photo shows a disease name and a percentage — yes/no | [ ] |
| 5 | Offline detection (TensorFlow Lite + heuristic fallback) | `ml/TFLiteClassifier.kt` (224×224, RGB 0..1) | In Offline mode (even in airplane mode) I still get a disease result — yes/no | [ ] |
| 6 | Result screen with details | `ResultActivity.kt` | The result shows disease, confidence %, symptoms, treatment, prevention — yes/no | [ ] |
| 7 | Share result | `ResultActivity.kt` (share intent) | Tapping **Share** opens the Android share sheet with the result text — yes/no | [ ] |
| 8 | Save scan to history (Room, table `scan_history`, DB `leafguard.db`) | `database/ScanRecord.kt`, `ScanDao.kt`, `AppDatabase.kt` | After a detection, the scan appears in History — yes/no | [ ] |
| 9 | History list | `HistoryActivity.kt` (RecyclerView) | I see my past scans, newest first — yes/no | [ ] |
| 10 | History detail | `HistoryDetailActivity.kt` (`EXTRA_SCAN_ID`) | Tapping a history item opens its full details — yes/no | [ ] |
| 11 | Delete a history record | `HistoryActivity.kt` + `ScanDao.kt` | I can delete a scan and it disappears from the list — yes/no | [ ] |
| 12 | Offline disease library (XML) | `DiseaseLibraryActivity.kt` parsing `assets/diseases.xml` with `XmlPullParser` | The library lists the 10 diseases with details, even offline — yes/no | [ ] |
| 13 | Notifications (channel `leafguard_scan_reminders`) | `utils/NotificationHelper.kt` | I can trigger the scan-reminder notification and see it in the status bar — yes/no | [ ] |
| 14 | Optional location tagging | `MainActivity.kt` + `latitude`/`longitude` columns in `ScanRecord` | If I allow location, saved scans store coordinates (visible in history detail) — yes/no | [ ] |
| 15 | Settings (backend URL + confidence threshold) | `SettingsActivity.kt` (`pref_backend_url`, `pref_confidence_threshold`) | I can change the server URL and threshold and they persist after restart — yes/no | [ ] |
| 16 | Automated tests | `app/src/test/.../PredictionResponseTest`, `app/src/androidTest/.../MainActivityTest` (both tracks) | `./gradlew testDebugUnitTest` passes (green) — yes/no | [ ] |

## Features this app does NOT have (on purpose)

To keep the course honest: there is **no login/registration, no user accounts or
authentication tokens, no navigation drawer, and no PUT/DELETE API endpoints**. The
backend exposes only `GET /`, `GET /health`, `GET /diseases`, and `POST /predict`.
If any other document claims these exist, that document is wrong — please fix it.

## Syllabus concept coverage (real mapping)

| CSE 2206 concept | Where it is demonstrated |
|---|---|
| Activities & intents | The six activities + intent extras (`EXTRA_SCAN_ID`) |
| Layouts & ViewBinding | `res/layout/*.xml` in both apps |
| Permissions (runtime) | Camera, notifications, location, media in `MainActivity` |
| Networking (Retrofit, multipart) | `ApiService`, `RetrofitClient` |
| JSON parsing (Gson) | `PredictionResponse` (`@SerializedName("disease")`) |
| Local database (Room/SQLite) | `ScanRecord`, `ScanDao`, `AppDatabase` |
| XML parsing | `DiseaseLibraryActivity` + `assets/diseases.xml` |
| Background work | Kotlin coroutines (`lifecycleScope`) / Java `ExecutorService` |
| On-device ML | `TFLiteClassifier` + `assets/model.tflite` (placeholder → Week 09) |
| Notifications | `NotificationHelper` |
| Testing | JUnit unit tests + Espresso UI tests (Week 11) |
