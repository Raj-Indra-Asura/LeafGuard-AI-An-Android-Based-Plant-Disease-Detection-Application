# LeafGuard AI — Architecture Ground Truth

> **What is this file?** This is the single, authoritative list of everything that
> *really exists* in the LeafGuard AI code. Every other document in this repository
> (roadmap weeks, exercises, notebooks, solutions, validation checklists) must match
> this file. If a document mentions a class, file, or API detail that is not listed
> here, that document is wrong — not the code.

**Kotlin is the primary track** (`android-app-kotlin/`). The Java track
(`android-app/`) is a complete, byte-for-byte-behavior twin kept as a secondary
reference. See [`JAVA_VS_KOTLIN.md`](JAVA_VS_KOTLIN.md) for the full file-by-file mapping.

---

## 1. The six real Activities (screens)

An *Activity* is Android's word for **one screen** of an app. LeafGuard AI has exactly
six, declared in both apps' `AndroidManifest.xml`. Kotlin files live in
`android-app-kotlin/app/src/main/java/com/leafguard/`; Java twins in
`android-app/app/src/main/java/com/leafguard/`.

| Activity (Kotlin `.kt` / Java `.java`) | What it does (plain language) |
|---|---|
| `MainActivity` | The home screen and app launcher. It takes the photo (camera or gallery), toggles between online (server) and offline (on-device) detection, and sends the image for analysis. There is **no** separate `ScanActivity` or `CameraActivity` — capture happens here. |
| `ResultActivity` | Shows the detection result: disease name, confidence percentage, symptoms, treatment, and prevention. Lets you share the result and saves it to history. |
| `HistoryActivity` | Shows a scrollable list of all past scans stored in the on-device database. |
| `HistoryDetailActivity` | Shows the full details of one past scan you tapped in the history list (receives the scan's id via the `EXTRA_SCAN_ID` intent extra). |
| `DiseaseLibraryActivity` | An offline encyclopedia of the 10 diseases, read from `assets/diseases.xml` with `XmlPullParser`. |
| `SettingsActivity` | Lets you change the backend server URL (`pref_backend_url`, default `http://10.0.2.2:8000`) and the confidence threshold (`pref_confidence_threshold`, default 50). |

**Classes that do NOT exist** (never reference them): `ScanActivity`,
`CameraActivity`, `ImagePreviewActivity`, `ScanViewModel`, `DetectionEntity`,
`DetectionDao`, `DiseaseClassifier`, `ModelLoader` (Android; the backend has its own
`backend-api/model_loader.py`, which is real).

## 2. Real supporting classes

| Class | Package | What it does |
|---|---|---|
| `ScanRecord` | `com.leafguard.database` | The Room `@Entity` — one saved scan. Table name **`scan_history`**; columns: `id`, `disease_name`, `confidence`, `symptoms`, `treatment`, `prevention`, `image_uri`, `latitude`, `longitude`, `timestamp`. |
| `ScanDao` | `com.leafguard.database` | The Room DAO (Data Access Object) — the interface with the database queries (`insertScan`, `getAllScans`, `deleteScan`, `getRecentScans`, `getScanById`, `deleteScanById`). Kotlin methods are `suspend fun`. |
| `AppDatabase` | `com.leafguard.database` | The Room database singleton. Database file name: **`leafguard.db`**, version 1. |
| `ApiService` | `com.leafguard.network` | The Retrofit interface: `@Multipart @POST("predict")` with `@Part image` returning `Call<PredictionResponse>`. |
| `PredictionResponse` | `com.leafguard.network` | The parsed JSON reply from the server. Fields: `disease` (via `@SerializedName("disease")`), `confidence`, `symptoms`, `treatment`, `prevention`. |
| `RetrofitClient` | `com.leafguard.network` | Builds the Retrofit instance. Default base URL **`http://10.0.2.2:8000/`** (the emulator's address for "my computer"), 30-second timeouts, logging. Kotlin: an `object` singleton. |
| `TFLiteClassifier` | `com.leafguard.ml` | Offline (on-device) detection. Loads `assets/model.tflite` + `assets/labels.txt`, resizes the photo to **224×224**, converts to **RGB floats 0..1**, runs the model, picks the highest score. If the model asset is missing or invalid (the committed one is a **text placeholder**), it uses a green-channel **heuristic fallback** so the app still works. |
| `NotificationHelper` | `com.leafguard.utils` | Creates the notification channel **`leafguard_scan_reminders`** and posts the scan-reminder notification (id 1001). Kotlin: an `object`. |

## 3. The API contract (authoritative)

The backend is one file: [`backend-api/main.py`](../backend-api/main.py) (FastAPI).
There is **no** `backend-api/predict.py`.

- **Endpoint:** `POST /predict` — defined as `async def predict(image: UploadFile = File(...))`.
- **Multipart part name:** **`image`** (NOT `file`). If the app sends the part as
  `file`, the server rejects it.
- **JSON response field:** **`disease`** (NOT `disease_name`), plus `confidence`,
  `symptoms`, `treatment`, `prevention`.
- **Base URL from the emulator:** `http://10.0.2.2:8000/` (changeable in the app's
  Settings screen). `10.0.2.2` is the special address the Android emulator uses to
  reach the computer it is running on.
- Other real endpoints in `main.py`: `GET /` (welcome), `GET /health`, `GET /diseases`.

Example test command (run while the backend is running):

```bash
curl -X POST "http://localhost:8000/predict" -F "image=@sample-images/healthy/healthy_leaf_01.jpg"
```

## 4. Assets, database, identifiers

| Item | Value |
|---|---|
| App package | `com.leafguard` (both tracks) |
| Database file | `leafguard.db` |
| Room table | `scan_history` |
| Notification channel id | `leafguard_scan_reminders` |
| Model asset | `app/src/main/assets/model.tflite` — **a text placeholder**, kept on purpose so the project builds; the classifier detects it and falls back to the heuristic. Replace with a real converted model in Week 09 (see `model/model-acquisition-guide.md`). |
| Disease library asset | `app/src/main/assets/diseases.xml` — in **`assets/`**, not `res/`; the file is named `diseases.xml`, not `disease_library.xml`. |
| Labels asset | `app/src/main/assets/labels.txt` — 10 labels, same order in `model/labels.txt`, both apps' `assets/labels.txt`, and both `assets/diseases.xml` `<name>` values. |
| TFLite preprocessing | 224×224, RGB, floats scaled 0..1, argmax over 10 outputs, heuristic fallback when the model is a placeholder. |

## 5. The 10 labels (exact order matters)

```
Tomato Early Blight
Tomato Late Blight
Tomato Healthy
Potato Early Blight
Potato Late Blight
Potato Healthy
Corn Gray Leaf Spot
Corn Northern Leaf Blight
Corn Healthy
Apple Scab
```

Index 0 = first line. The model's output position N maps to line N. The exact label
string is also the lookup key into `diseases.xml`, so all copies must match
character-for-character.

## 6. Tests (real, present, passing skeletons)

Both apps contain minimal automated tests (added in the Week 11 repair):

| Test | Kotlin path | Java path |
|---|---|---|
| Unit test (runs on your computer, no phone needed): parses a `PredictionResponse` from JSON with Gson and checks the `disease` field | `android-app-kotlin/app/src/test/java/com/leafguard/network/PredictionResponseTest.kt` | `android-app/app/src/test/java/com/leafguard/network/PredictionResponseTest.java` |
| UI (instrumented) test (runs on an emulator/phone): launches `MainActivity` and checks a view is visible | `android-app-kotlin/app/src/androidTest/java/com/leafguard/MainActivityTest.kt` | `android-app/app/src/androidTest/java/com/leafguard/MainActivityTest.java` |

Test dependencies (JUnit 4, Mockito, Espresso, androidx.test runner/rules) are already
declared in both `app/build.gradle` files.

## 7. Build facts

- Gradle wrapper 8.2 in both apps (`gradlew` for macOS/Linux, `gradlew.bat` for Windows).
- `compileSdk 34`, `minSdk 24`; Kotlin track adds Kotlin 1.9.22 + coroutines + `kapt`.
- Backend: Python 3 + FastAPI; install with `pip install -r backend-api/requirements.txt`,
  run with `uvicorn main:app --reload` from inside `backend-api/`.
