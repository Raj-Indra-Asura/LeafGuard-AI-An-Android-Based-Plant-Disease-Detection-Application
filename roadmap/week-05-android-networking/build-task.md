# Week 05 Build Task: Connect the Android Image URI to FastAPI

## Objective

Reconstruct or verify the exact Kotlin Week 05 slice documented in [learning-notes.md section 12](learning-notes.md#12-end-of-week-05-file-inventory-exact-files-exact-code-exact-size).

By the end, the app uploads the Week 03 image to the unchanged Week 04 backend, parses all eight response fields, displays the result, and recovers safely when the backend is unavailable.

Estimated time: 8 to 10 hours.

---

## Before You Start

- [ ] Week 03 camera/gallery validation passes.
- [ ] Week 04 backend tests pass.
- [ ] Six Week 05 exercises are complete.
- [ ] You can name multipart field `image`.
- [ ] You can name all eight response fields.
- [ ] You understand that mock mode is acceptable and must be disclosed.

Target evidence folder:

```text
docs/evidence/week-05/
|-- exercises/
|-- backend-tests.txt
|-- android-build.txt
|-- api-contract.md
|-- validation.md
|-- quiz-answers.md
|-- reflection-answers.md
`-- screenshots/
```

---

## Step 1: Freeze the Two Input Contracts

Write this in `api-contract.md` before editing Android:

```text
Week 03 input: selectedImageUri from camera or gallery
Week 04 request: POST /predict, multipart field image
Week 04 success: eight-field JSON
Week 05 output: ResultActivity plus retryable errors
```

Run the backend tests:

```bash
cd backend-api
USE_MOCK=true .venv/bin/python -m unittest -v test_api
```

Checkpoint: `Ran 8 tests` and `OK`.

---

## Step 2: Add Only the Three Networking Dependencies

Update `android-app-kotlin/app/build.gradle` from the 40-line Week 03 state to the complete 47-line Week 05 state.

Add:

```groovy
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
```

Also enable `buildConfig` because the cumulative camera code uses `BuildConfig.APPLICATION_ID`.

Checkpoint:

```bash
cd android-app-kotlin
./gradlew assembleDebug
```

Do not add Room, TensorFlow Lite, WorkManager, CameraX, or later UI dependencies.

---

## Step 3: Create the Eight-Field Response Model

Create:

```text
app/src/main/java/com/leafguard/network/PredictionResponse.kt
```

Use the complete 22-line file from the learning notes.

Checkpoint:

- exactly eight properties
- `Float` confidence
- `Boolean` uncertainty and guidance flags
- `@SerializedName` for `model_label` and `guidance_available`
- no invented JSON fields

Build before continuing.

---

## Step 4: Create the Retrofit Contract and Client

Create:

```text
network/ApiService.kt
network/RetrofitClient.kt
```

Required contract:

```kotlin
@Multipart
@POST("predict")
fun uploadImage(@Part image: MultipartBody.Part): Call<PredictionResponse>
```

Required local URL:

```text
http://10.0.2.2:8000/
```

Checkpoint:

- base URL appears in one file only
- URL ends with `/`
- Gson converter is registered
- connect/read/write timeouts are 30 seconds
- logging is `BASIC`, so uploaded bytes are not dumped
- app builds

---

## Step 5: Add Permission and Local Security

Extend the manifest with:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Reference `@xml/network_security_config` on `<application>` and create the exact 7-line file from the learning notes.

Checkpoint:

- `INTERNET` appears before `<application>`
- no runtime permission request exists for Internet
- base cleartext policy is false
- only `10.0.2.2` is allowed for local HTTP
- Week 03 FileProvider remains unchanged

Build before continuing.

---

## Step 6: Expand the Scan and Result Layouts

In `activity_scan.xml`, add:

- disabled `buttonDetectDisease`
- hidden `progressUpload`

Replace the Result placeholder with the complete 115-line result layout.

Checkpoint:

- every visible string uses `strings.xml`
- IDs match the Activity code exactly
- both layouts use `ScrollView`
- result shows model label, confidence, uncertainty, guidance status, symptoms, treatment, and prevention

---

## Step 7: Extend `ScanActivity` From URI to Multipart

Preserve all Week 03 behavior. Add these stages in order:

```text
selectedImageUri
  -> copyUriToCacheFile
  -> asRequestBody(MIME type)
  -> createFormData("image", ...)
  -> RetrofitClient.apiService.uploadImage(...)
```

Use `contentResolver.openInputStream`; do not use `File(uri.path)`.

Checkpoint:

- Detect remains disabled until selection
- cache file is deleted after response or failure
- URI copy failure restores UI
- multipart name is exactly `image`
- no synchronous `execute()` call exists

Build before continuing.

---

## Step 8: Implement Both Callback Paths

In `onResponse`:

1. delete cache file
2. hide progress and restore controls
3. check `isSuccessful`
4. check body for null
5. open ResultActivity only for valid success
6. show HTTP status for rejected responses

In `onFailure`:

1. delete cache file
2. hide progress and restore controls
3. show a safe network message

Checkpoint: stopping FastAPI must not crash or strand the UI.

---

## Step 9: Expand ResultActivity

Read all eight Intent extras using constants. Format confidence with:

```kotlin
val confidencePercent = (confidence * 100f).roundToInt()
```

Display uncertainty and guidance availability instead of silently discarding those flags.

Checkpoint: opening the Result placeholder directly still uses safe defaults and does not crash.

---

## Step 10: Run the Milestone Demo

Start the backend:

```bash
cd backend-api
USE_MOCK=true .venv/bin/uvicorn main:app --reload
```

Build Android:

```bash
cd android-app-kotlin
./gradlew assembleDebug
```

Demo:

1. select a gallery image
2. observe Detect enabled
3. tap Detect
4. observe loading state
5. inspect `POST /predict` in Logcat
6. observe ResultActivity
7. state that the result is mock pipeline evidence
8. stop backend
9. retry and observe friendly error
10. restart backend and retry successfully

---

## Step 11: Verify Future Boundaries

Week 05 must contain none of these in its teaching snapshot:

- Room database classes or dependencies
- TensorFlow Lite classes, dependencies, or assets
- offline/cloud mode toggle
- WorkManager or notifications
- bottom navigation redesign
- real-model accuracy claims

The repository's later evolved app may contain these today. They do not belong in the reconstructed end-of-Week-05 snapshot.

---

## Evidence to Save

1. Eight passing backend tests.
2. Successful Android debug build.
3. `/health` showing mock mode.
4. Image selected and Detect enabled.
5. Progress visible during upload.
6. Result screen.
7. Logcat request/status without body bytes.
8. Backend-unavailable error.
9. Completed API contract note.

Do not save private IPs, `.env`, personal photos, or raw multipart bodies.

---

## Done Means

- the exact 4-new/7-expanded file snapshot is understood
- Android builds
- all eight backend tests pass
- camera and gallery still work
- multipart `image` reaches FastAPI
- all eight response fields parse
- ResultActivity displays the response honestly
- HTTP and network failures recover safely
- evidence is saved
- no Week 06+ behavior is claimed

<!-- NAV_FOOTER_START -->

---

## Week 05 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | **Build Task - current** | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Exercises](exercises.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Validation](validation-checklist.md)