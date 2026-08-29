# Week 05 Learning Notes: Android Networking From the Verified Contract

## Purpose

These notes teach only what is needed to connect the Week 03 Android image URI to the Week 04 FastAPI contract. Read the concepts before the exercises and build task.

Section 12 is the authoritative reconstruction appendix. It contains every changed or new Kotlin-track file in full, with exact logical line counts. The snapshot was compiled independently with `./gradlew assembleDebug`.

## Beginner Bridge: Complete This Before Section 1

Week 05 combines Android, the Week 04 API contract, file access, a third-party networking library, asynchronous execution, and UI state. A beginner can understand each idea separately but still get lost when they first appear together.

Learn them in this order:

```text
verify Week 03 image URI
    -> verify Week 04 API independently
    -> understand Android network permission and addresses
    -> understand Retrofit, OkHttp, and Gson roles
    -> map JSON to Kotlin
    -> turn URI content into upload bytes
    -> construct multipart field image
    -> enqueue without blocking the UI
    -> handle every completion path
    -> pass values to ResultActivity
    -> test success and failure separately
```

Every lesson has a checkpoint. If a checkpoint fails, stop at that boundary. Do not paste the complete `ScanActivity` from Section 12 and hope the pieces become clear later.

### Bridge Lesson A: Prove Both Inputs Before Connecting Them

#### Learn

Week 05 starts with two independent, already-tested systems:

| System | Required proof | If it fails |
|---|---|---|
| Week 03 Android image input | Camera/gallery returns a URI and preview works | Return to Week 03 |
| Week 04 FastAPI | Eight tests pass and `/docs` accepts field `image` | Return to Week 04 |

Networking cannot repair a broken camera URI or a broken backend. Connecting two unverified components creates too many possible causes.

The full flow crosses boundaries:

```text
Android UI
    -> selectedImageUri
    -> ContentResolver
    -> temporary file
    -> OkHttp multipart request
    -> emulator network
    -> FastAPI /predict
    -> JSON response
    -> Gson
    -> PredictionResponse
    -> Intent extras
    -> ResultActivity
```

#### Checkpoint

Before adding Retrofit, show:

1. a selected image preview in Android
2. eight passing backend tests
3. a successful `/predict` call in `/docs`
4. the eight returned JSON keys

#### If stuck

Use the Week 03 and Week 04 validation checklists. Do not troubleshoot Android networking until both earlier boundaries pass.

---

### Bridge Lesson B: What Actually Happens During an Android Network Call

#### Learn

Android is the HTTP client in Week 05. It creates a request and waits for the server's response:

```text
user taps Detect
    -> Android prepares image bytes
    -> Android sends POST /predict
    -> FastAPI validates and responds
    -> Android receives status + JSON
    -> Android updates the UI
```

The request still has the same Week 04 parts:

| HTTP part | Android supplies |
|---|---|
| Base URL | `http://10.0.2.2:8000/` |
| Relative path | `predict` |
| Method | POST |
| Body encoding | multipart form data |
| File field | `image` |
| Filename | temporary cache filename |
| MIME type | selected image type or safe fallback |
| Bytes | copied URI content |

The response supplies:

- an HTTP status
- headers
- JSON success data or error detail

HTTP does not automatically know which Activity should display the result. Your code decides what each response means and how the UI changes.

#### Checkpoint

State which program creates every request item in the table and which program creates the response status and body.

#### If stuck

Place the Week 04 `/docs` request beside the planned Retrofit request. Circle the parts that must remain identical.

---

### Bridge Lesson C: Retrofit, OkHttp, and Gson Have Different Jobs

#### Learn

The three networking dependencies form a stack:

```text
your Activity
    -> Retrofit: turns an annotated interface into a typed API call
    -> OkHttp: creates and transports the HTTP request
    -> Gson converter: converts JSON to Kotlin values
```

| Component | Week 05 responsibility | It does not own |
|---|---|---|
| Retrofit | Method/path annotations and typed call interface | Image selection or result UI |
| OkHttp | Request body, multipart encoding, connection, timeout, logging | JSON property design |
| Gson converter | JSON deserialization into `PredictionResponse` | HTTP transport |
| `ApiService` | One declaration of the API operation | Base client configuration |
| `RetrofitClient` | Base URL, converter, timeouts, logging | Activity state |

Annotated interface skeleton:

```kotlin
interface ApiService {
    @Multipart
    @POST("predict")
    fun uploadImage(
        @Part image: MultipartBody.Part
    ): Call<PredictionResponse>
}
```

Line-by-line:

| Code | Meaning |
|---|---|
| `interface` | Describes operations without manually implementing transport |
| `@Multipart` | Request contains multipart form data |
| `@POST("predict")` | Use POST and append `predict` to the base URL |
| `@Part` | Supplied value belongs in the multipart body |
| `Call<PredictionResponse>` | A request operation that can later return parsed response data |

Retrofit generates the concrete implementation at runtime. Your code calls the interface; it does not write socket code.

#### Checkpoint

Explain why removing the Gson converter, removing `@Multipart`, and changing `@POST("predict")` cause three different problems.

#### If stuck

Make one sentence for each dependency beginning with “This dependency is responsible for ...”.

---

### Bridge Lesson D: Gradle Dependencies and Sync

#### Learn

Android code can import Retrofit classes only after Gradle knows which library artifacts to download.

The Week 05 dependency roles are:

| Dependency | Adds |
|---|---|
| Retrofit | `Retrofit`, `Call`, callback support, HTTP annotations |
| converter-gson | JSON-to-Kotlin converter integration |
| logging-interceptor | Development request/status logging |

Adding a dependency changes the build input. **Gradle sync** resolves it; **assembleDebug** compiles the app with it.

Debug in this order:

```text
dependency line correct
    -> Gradle sync succeeds
    -> imports resolve
    -> compile succeeds
```

Do not add Room, TensorFlow Lite, coroutines networking wrappers, or another HTTP library to solve a Retrofit setup error. Additional dependencies widen the problem.

#### Checkpoint

Explain the difference between:

- declaring a dependency
- syncing Gradle
- importing a class
- compiling the app

#### If stuck

Read the first Gradle error, not the final cascade. Confirm internet access, artifact spelling, and the repository's existing Gradle configuration before changing versions.

---

### Bridge Lesson E: Emulator Addressing and the Base URL

#### Learn

The emulator behaves like a separate device behind a virtual network:

```text
Android emulator
    localhost -> emulator itself
    10.0.2.2  -> special route to development computer
                         |
                         `-> FastAPI on port 8000
```

Therefore:

```text
http://localhost:8000/     wrong for host FastAPI from the standard emulator
http://10.0.2.2:8000/     correct Week 05 emulator base URL
```

The Retrofit base URL must end with `/` so it can safely resolve the relative path `predict`:

```text
base:     http://10.0.2.2:8000/
relative: predict
result:   http://10.0.2.2:8000/predict
```

A physical phone is different. It normally uses the development computer's reachable LAN address, requires both devices on a trusted network, may require a firewall rule, and needs an appropriately restricted network-security configuration. A machine-specific LAN address must not become shared repository truth.

Address debugging order:

1. Is FastAPI running?
2. Does `http://localhost:8000/health` work on the development computer?
3. Is Android running in the standard emulator?
4. Does the app use `10.0.2.2`, port `8000`, and a trailing slash?
5. Does Logcat report connection refusal, timeout, or cleartext rejection?
6. Is a local firewall blocking the connection?

These observations are different:

- **connection refused** often means no process is listening at that host/port
- **timeout** often means the route is unreachable or blocked
- **404** means a server answered but the path was wrong
- **cleartext not permitted** means Android security blocked HTTP before the normal request completed

#### Checkpoint

Explain why the browser on your computer uses `localhost` while the emulator app uses `10.0.2.2`.

#### If stuck

Prove `/health` on the host first. Then use Logcat to classify the Android failure instead of changing the backend response code.

---

### Bridge Lesson F: INTERNET Permission and Local Cleartext Security

#### Learn

Android requires this manifest permission for network access:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

It is an install-time permission, so there is no runtime permission dialog. This differs from the Week 03 camera permission.

Week 05 uses local `http://` for a learning server. HTTP is cleartext transport: another party on an untrusted network may be able to observe or modify traffic. Android 9+ blocks cleartext by default for this reason.

The network security configuration makes a narrow development exception:

```text
deny cleartext by default
    -> permit only the emulator-to-host development address
    -> use HTTPS for production
```

Security boundaries:

- `INTERNET` permission allows the app to use networking.
- network security config controls transport policy.
- CORS is a browser cross-origin policy; it is not Android authentication.
- HTTPS protects transport; it does not by itself authenticate app users.

Do not “fix” a cleartext error by enabling cleartext globally.

#### Checkpoint

Explain why Week 03 camera permission needs a runtime request but `INTERNET` does not. Then explain why a local HTTP exception must not become the production policy.

#### If stuck

Search Logcat for `CLEARTEXT`. If present, verify the manifest references the expected XML resource and the exception matches the emulator host.

---

### Bridge Lesson G: JSON-to-Kotlin Mapping

#### Learn

FastAPI serializes Python values into JSON. Gson deserializes that JSON into a Kotlin object.

```text
FastAPI PredictionResult
    -> JSON text over HTTP
    -> GsonConverterFactory
    -> PredictionResponse properties
```

JSON uses snake_case keys such as `model_label`. Kotlin usually uses camelCase properties such as `modelLabel`. `@SerializedName` records the exact mapping.

| JSON | Kotlin | Why |
|---|---|---|
| `model_label` | `modelLabel` | naming styles differ |
| `guidance_available` | `guidanceAvailable` | naming styles differ |
| `confidence` | `confidence` | same name |

The Kotlin type must also fit the JSON type:

| JSON value | Suitable Kotlin type |
|---|---|
| `"Tomato - Healthy"` | `String` |
| `0.86` | `Float` |
| `true` | `Boolean` |

Nullability is written with `?`, such as `String?`. A nullable property requires a safe fallback before display. A non-null property expresses a stronger contract but deserialization still needs testing against the actual server.

There are three response-shape outcomes:

1. **HTTP success and valid body**: use the parsed object.
2. **HTTP success and null/empty body**: treat it as an unexpected server contract failure.
3. **JSON conversion failure**: Retrofit reports failure because Gson could not create the expected object.

Do not force a nullable response with `!!`. A friendly error is better than a crash.

#### Trace

For every Week 04 field, trace:

```text
JSON key -> Kotlin property -> Intent extra -> Result view
```

#### Checkpoint

Complete the eight-row response table in `exercises.md`. Explain why renaming a Kotlin property without an annotation can break mapping.

#### If stuck

Compare one actual `/predict` JSON response with `PredictionResponse.kt`, character by character for key names and value types.

---

### Bridge Lesson H: A URI Is Access, Not a File Path

#### Learn

Week 03 stores a `Uri`. It may use a `content://` scheme controlled by a content provider:

```text
content://provider/item/123
```

That is not guaranteed to be a normal filesystem path. `File(uri.path)` may point to a meaningless or inaccessible path.

Use `ContentResolver`:

```text
Uri
    -> contentResolver.openInputStream(uri)
    -> readable InputStream
    -> copy bytes to cacheDir
    -> temporary File
```

Roles:

| Type | Meaning | Cleanup |
|---|---|---|
| `Uri` | Android reference to content | No deletion by Week 05 |
| `InputStream` | Sequential access to bytes | Close immediately after copying |
| cache `File` | App-owned temporary upload source | Delete after response/failure |
| `RequestBody` | OkHttp view of file bytes + media type | Owned by request |
| `MultipartBody.Part` | Named form part | Owned by request |

Why copy to cache:

- OkHttp can stream from an app-owned file.
- the original provider content remains unchanged.
- cache is temporary and app-private.
- the file can be explicitly deleted after the request ends.

The copy must handle:

- `openInputStream` returning null
- unavailable or revoked content
- read/write `IOException`
- cleanup after partial copies

A buffer copies chunks rather than loading the complete image into one large extra memory allocation. The exact chunk size is an implementation detail; correctness depends on copying until end-of-stream and closing resources.

MIME type comes from `ContentResolver.getType(uri)` when available. If unavailable, the Week 05 implementation uses a safe image fallback consistent with the selected input contract.

#### Checkpoint

Explain why `Uri`, `InputStream`, and `File` are three different representations. State when the stream closes and when the cache file is deleted.

#### If stuck

Draw ownership arrows for the original image and temporary copy. The app may delete only the copy it created for upload.

---

### Bridge Lesson I: Constructing the Multipart Part

#### Learn

Multipart construction happens in layers:

```text
cache File
    -> RequestBody(media type + bytes)
    -> MultipartBody.Part(form name + filename + body)
```

Conceptual skeleton:

```kotlin
val requestBody = uploadFile.asRequestBody(/* TODO: MIME type */)
val imagePart = MultipartBody.Part.createFormData(
    "image",
    uploadFile.name,
    requestBody
)
```

`createFormData` arguments:

| Argument | Week 05 value | Purpose |
|---|---|---|
| form name | `"image"` | Must match FastAPI parameter |
| filename | cache filename | Upload metadata |
| body | `requestBody` | MIME type and bytes |

OkHttp adds the multipart boundary and required part headers. Do not manually concatenate binary bytes or hardcode a boundary.

The Kotlin parameter name `imagePart` is local code style. The string `"image"` is the network contract. Changing the variable name does not change the request; changing the string does.

#### Checkpoint

Point to the one value FastAPI uses for parameter binding. Explain the separate purposes of filename and MIME type.

#### If stuck

Trigger a deliberate wrong form name only in a local experiment, observe `422`, then restore `"image"`. Record why the server was reachable even though the request failed.

---

### Bridge Lesson J: Asynchronous Calls Protect the Main Thread

#### Learn

Android's **main thread** handles input, drawing, and most view updates. A network call may take seconds or fail after a timeout. Blocking the main thread would freeze the UI and may cause an Application Not Responding error.

Retrofit provides:

- `execute()` for a synchronous blocking call
- `enqueue(...)` for an asynchronous callback call

Week 05 uses `enqueue(...)`.

```text
main thread: tap -> prepare UI -> enqueue -> return to event loop
network work:                    request travels and waits
callback:                                  response or failure -> update UI
```

With standard Retrofit Android configuration, callbacks are delivered on the Android main thread, so the Week 05 callback can update views. Extra CPU-heavy work still must not be performed in the callback.

Exactly one terminal callback occurs for a call:

| Callback | Meaning |
|---|---|
| `onResponse` | An HTTP response arrived, even if status is 400 or 503 |
| `onFailure` | No usable response object was produced, such as connection failure, timeout, cancellation, or conversion failure |

`onResponse` does not mean success. It means the server responded. You must still check `isSuccessful` and body validity.

Activity lifecycle concern:

- a request may outlive a visible screen
- callbacks must not assume a destroyed Activity is still usable
- the exact Week 05 snapshot keeps the flow simple, but later architecture may move requests into lifecycle-aware layers

For this week, avoid repeatedly tapping Detect by disabling controls while one request is active.

#### Checkpoint

Explain why HTTP 503 reaches `onResponse` while a stopped server normally reaches `onFailure`. Explain why `execute()` is forbidden on the UI thread.

#### If stuck

Write two columns named **response arrived** and **no usable response**. Place each error observation into one column before writing callback code.

---

### Bridge Lesson K: Model Upload as a UI State Machine

#### Learn

Do not treat loading as one ProgressBar line. The screen has states:

| State | Image available | Progress | Controls | Next action |
|---|---:|---:|---|---|
| No image | No | Hidden | Selection enabled; Detect disabled | Select image |
| Ready | Yes | Hidden | Detect enabled | Start upload |
| Uploading | Yes | Visible | Image and Detect controls disabled | Wait |
| Success | Yes | Hidden | Controls restored | Open Result |
| HTTP error | Yes | Hidden | Controls restored | Explain and retry |
| Network error | Yes | Hidden | Controls restored | Explain and retry |
| Preparation error | Maybe | Hidden | Controls restored | Reselect or retry |

Every path out of **Uploading** must:

1. remove the temporary file when it exists
2. hide progress
3. restore appropriate controls
4. navigate only for a valid success
5. otherwise show a safe message

This prevents common bugs:

- permanent spinner
- permanently disabled button
- duplicate requests
- leaked temporary files
- opening ResultActivity with missing data

A helper such as `setUploadInProgress(...)` centralizes repeated UI transitions. The exact helper name is less important than applying the same state rules to every terminal path.

#### Checkpoint

Complete the state table in `exercises.md` before implementing callbacks. Verify that every row after Uploading restores the UI.

#### If stuck

Search the callback code for every `return`. Confirm cleanup and UI restoration happen before each terminal return.

---

### Bridge Lesson L: Separate HTTP, Network, Conversion, and Preparation Errors

#### Learn

“Upload failed” is not enough information for debugging. Classify the failure first:

| Observation | Boundary reached | Likely category | First check |
|---|---|---|---|
| URI cannot open | Before HTTP | Content access/preparation | URI access and stream exception |
| Cleartext rejected | Android transport policy | Security configuration | Manifest and XML policy |
| Connection refused | Host/port reached but no listener | Server unavailable/address | Backend process and base URL |
| Timeout | No timely response | Reachability/server delay | Host, firewall, timeout, server log |
| HTTP 400 | FastAPI route examined content | Invalid image | MIME/bytes and server detail |
| HTTP 413 | FastAPI measured upload | Too large | Selected file size |
| HTTP 422 | FastAPI could not bind request shape | Multipart contract | Form name `image` |
| HTTP 503 | FastAPI answered but model unavailable | Server mode | `/health` and configuration |
| Conversion failure | Response arrived but JSON did not map | Contract mismatch | Actual JSON keys/types |
| HTTP 200 with null body | Response violated expected success shape | Contract failure | Server response and converter |

Safe user messages should state what the user can do. Developer details belong in Logcat or server logs and must not reveal secrets or raw image bodies.

Debugging rule:

> Find the last boundary that definitely worked.

Examples:

- An HTTP 422 proves Android reached FastAPI.
- A server access log with POST proves the address and permission worked.
- A `/health` response proves reachability, not multipart correctness.
- A parsed `PredictionResponse` proves JSON mapping, not model accuracy.

#### Checkpoint

For each row, say whether to inspect Android Logcat, FastAPI logs, `/health`, request construction, or JSON mapping first.

#### If stuck

Use `validation-checklist.md` Failure Routing. Change only the owner of the observed failure.

---

### Bridge Lesson M: Pass Data to ResultActivity Safely

#### Learn

Retrofit returns `PredictionResponse` to `ScanActivity`. Android navigation does not automatically share that object. Week 05 puts the eight values into Intent extras.

```text
PredictionResponse
    -> named Intent extras
    -> start ResultActivity
    -> read extras with the same names
    -> apply safe defaults
    -> format for display
```

Intent extra names are another local contract. A value written under one key cannot be read under a different key.

Confidence conversion:

```text
server value: 0.86
display calculation: 0.86 x 100
display value: 86%
```

Keep confidence on the 0.0–1.0 scale until the display layer. Do not multiply it in `PredictionResponse`, because that would change its meaning relative to the API contract.

The screen must represent:

- canonical model label
- readable disease name
- confidence percentage
- uncertainty
- guidance availability
- symptoms
- treatment
- prevention

Safe defaults prevent a direct or malformed ResultActivity launch from crashing. They do not make an invalid network response valid; `ScanActivity` should navigate only after checking response success and body validity.

#### Checkpoint

Trace one field from JSON key to Kotlin property to Intent key to view. Repeat for a snake_case field and a Boolean field.

#### If stuck

Create an eight-row trace table. A missing row reveals where data is being discarded.

---

### Bridge Lesson N: A Safe Step-by-Step Integration Rehearsal

Complete one layer at a time:

1. Run the Week 04 backend tests.
2. Start FastAPI in explicit mock mode.
3. Verify `/health` and `/predict` outside Android.
4. Add only the three networking dependencies and build.
5. Add `PredictionResponse`; build.
6. Add `ApiService` and `RetrofitClient`; build.
7. Add permission and restricted local transport configuration; build.
8. Confirm Week 03 camera/gallery still works.
9. Trace URI-to-cache copy without sending.
10. Construct multipart `image`.
11. Enqueue the call and implement all terminal states.
12. Pass all eight values to ResultActivity.
13. Demo one successful upload.
14. Stop FastAPI and demo one retryable failure.
15. Restart FastAPI and prove recovery.

Why build repeatedly:

> A build after each small layer identifies which layer introduced a compile or resource error.

Do not make all eleven file changes and perform the first build at the end.

---

### Week 05 Beginner Readiness Gate

Before Section 12 or `build-task.md`, verify:

- [ ] Week 03 camera and gallery behavior still passes.
- [ ] Week 04's eight backend tests pass.
- [ ] I can explain Retrofit, OkHttp, and Gson separately.
- [ ] I can explain why the emulator uses `10.0.2.2`.
- [ ] I know why the base URL ends with `/`.
- [ ] I can distinguish INTERNET permission from runtime camera permission.
- [ ] I can explain why local cleartext is narrowly restricted.
- [ ] I can map all eight JSON fields to Kotlin types.
- [ ] I can explain why a content URI is not a filesystem path.
- [ ] I can trace `Uri -> InputStream -> File -> RequestBody -> Part`.
- [ ] I know why the form name is exactly `image`.
- [ ] I can distinguish `onResponse` from `onFailure`.
- [ ] I can restore the UI and delete the cache file on every terminal path.
- [ ] I can classify 400, 413, 422, 503, timeout, refusal, and conversion failure.
- [ ] I can trace all eight values into ResultActivity.
- [ ] I can explain why a mock result proves integration, not model accuracy.

If any item is unclear, repeat the matching bridge lesson and exercise. This sequence connects to CSE 2206 topics in mobile networking, component communication, asynchronous UI work, resource management, Gradle dependency management, JSON data interchange, security policy, and defensive error handling.

---

## 1. How Week 05 Grows From Weeks 03 and 04

Week 05 has two verified inputs:

| Input | Existing fact | Week 05 use |
|---|---|---|
| Week 03 Android | `selectedImageUri` points to a camera or gallery image | Read its bytes for upload |
| Week 04 FastAPI | `POST /predict` accepts multipart field `image` | Define the Retrofit interface |
| Week 04 JSON | Successful response has eight named fields | Define `PredictionResponse` |

```text
selectedImageUri
  -> temporary cache file
  -> RequestBody
  -> MultipartBody.Part named image
  -> Retrofit POST /predict
  -> Week 04 JSON
  -> PredictionResponse
  -> Intent extras
  -> ResultActivity
```

Week 05 does not alter image capture or the backend response shape. It connects them.

---

## 2. Client and Server Responsibilities

| Android client owns | FastAPI server owns |
|---|---|
| User image selection | Upload validation |
| URI byte access | Image decoding and resizing |
| Multipart request construction | Mock or real prediction mode |
| Loading and error feedback | HTTP status codes |
| JSON parsing | Eight-field JSON response |
| Result display | Guidance fallback text |

A network failure does not mean the model is wrong. A 400 response does not mean Android could not reach the server. Separating responsibilities makes debugging testable.

---

## 3. Why Retrofit

Retrofit turns an annotated Kotlin interface into an HTTP client implementation.

```kotlin
interface ApiService {
    @Multipart
    @POST("predict")
    fun uploadImage(@Part image: MultipartBody.Part): Call<PredictionResponse>
}
```

| Part | Meaning |
|---|---|
| `@Multipart` | Encode the request as multipart form data. |
| `@POST("predict")` | Append `predict` to the base URL and use POST. |
| `@Part` | Put the supplied file part in the request body. |
| `Call<PredictionResponse>` | Represent an operation that later returns parsed data. |

Retrofit is built on OkHttp. Gson is the converter that maps JSON keys to Kotlin properties.

---

## 4. Base URL and Emulator Addressing

The Android emulator is a separate virtual device.

| Address | Meaning from the emulator |
|---|---|
| `localhost` / `127.0.0.1` | The emulator itself |
| `10.0.2.2` | The development computer |

The local Week 05 URL is:

```text
http://10.0.2.2:8000/
```

The trailing slash is required by Retrofit. A physical phone needs the development computer's LAN address and a matching network-security rule; never commit a private address as shared project truth.

---

## 5. From Android URI to Upload Bytes

A content URI is not a normal filesystem path. Code such as `File(uri.path)` is unreliable for gallery content.

Week 05 uses this safe sequence:

1. Open the URI with `contentResolver.openInputStream(uri)`.
2. Create a temporary file under `cacheDir`.
3. Copy bytes with an 8192-byte buffer.
4. Wrap the temporary file in an OkHttp `RequestBody`.
5. Delete the temporary file after response or failure.

The original camera/gallery content is not modified.

---

## 6. Multipart Contract

The field name is part of the Week 04 API contract:

```kotlin
val imagePart = MultipartBody.Part.createFormData(
    "image",
    uploadFile.name,
    requestBody
)
```

Changing `image` to `file`, `photo`, or another name normally produces HTTP 422 because FastAPI cannot bind the required parameter.

The client also supplies:

- a filename
- an image MIME type
- binary bytes

OkHttp generates the multipart boundary automatically.

---

## 7. Exact Eight-Field JSON Model

Week 04 returns:

| JSON key | Kotlin property | Type | Meaning |
|---|---|---|---|
| `model_label` | `modelLabel` | `String` | Canonical model-facing label |
| `disease` | `disease` | `String` | Display-friendly name |
| `confidence` | `confidence` | `Float` | Value from 0.0 to 1.0 |
| `uncertain` | `uncertain` | `Boolean` | Below server threshold |
| `guidance_available` | `guidanceAvailable` | `Boolean` | Reviewed guidance exists |
| `symptoms` | `symptoms` | `String` | Guidance or safe fallback |
| `treatment` | `treatment` | `String` | Guidance or safe fallback |
| `prevention` | `prevention` | `String` | Guidance or safe fallback |

`@SerializedName` is required where snake_case JSON maps to camelCase Kotlin.

Do not reduce the model to five fields. Android must preserve the complete Week 04 contract even if the first Result screen emphasizes only some fields.

---

## 8. Asynchronous Request and UI State

`enqueue(...)` starts the network operation without blocking Android's main thread.

```text
tap Detect
  -> show ProgressBar and disable buttons
  -> enqueue request
  -> onResponse OR onFailure
  -> hide ProgressBar and restore buttons
```

| Callback | Meaning |
|---|---|
| `onResponse` | A server response arrived, including 4xx and 5xx responses. |
| `onFailure` | No usable HTTP response arrived, such as connection refusal, timeout, or conversion failure. |

Inside `onResponse`, check both `response.isSuccessful` and `response.body() != null` before opening ResultActivity.

---

## 9. Error Categories

| Observation | Callback | Example | Week 05 behavior |
|---|---|---|---|
| HTTP 200 + body | `onResponse` | Valid mock response | Open ResultActivity |
| HTTP 400/413/422/503 | `onResponse` | Server rejected request | Show status-based message |
| Connection refused | `onFailure` | Backend stopped | Show network message |
| Timeout | `onFailure` | Server unreachable | Show network message |
| URI read failure | Before request | Content unavailable | Restore UI and explain |

Every terminal path must hide progress and permit another attempt.

---

## 10. Android Network Security

The manifest requires:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

`INTERNET` is an install-time permission; Android does not show a runtime dialog.

Android 9+ blocks cleartext HTTP by default. Week 05 permits it only for `10.0.2.2`:

```xml
<base-config cleartextTrafficPermitted="false" />
<domain-config cleartextTrafficPermitted="true">
    <domain includeSubdomains="false">10.0.2.2</domain>
</domain-config>
```

Production must use HTTPS. CORS is not Android authentication and does not replace transport security.

---

## 11. Result Navigation and Honest Mock Mode

After a successful response, `ScanActivity` passes all eight values as Intent extras. `ResultActivity` formats confidence as a percentage and displays uncertainty and guidance availability explicitly.

The screen displays what the backend returned. If `/health` says `use_mock: true`, the result proves:

- Android reached FastAPI
- multipart upload worked
- JSON parsing worked
- navigation and display worked

It does **not** prove that a real model recognized a disease. Week 06 owns that claim.

Common mistakes:

- using `localhost` in the emulator
- sending multipart field `file` instead of `image`
- treating a content URI as a direct file path
- using `execute()` on the UI thread
- checking only `response.body()` and ignoring the status
- leaving the progress indicator visible after failure
- allowing cleartext for every domain
- omitting `model_label`, `uncertain`, or `guidance_available`
- calling mock output real inference

---

## 12. End-of-Week-05 File Inventory (Exact Files, Exact Code, Exact Size)

Week 03 ended with 17 required Android source/resource files. Week 04 changed no Android files. Week 05 creates 4 files and expands 7 files in the Kotlin primary track.

### 12.1 Change Summary: Week 04 -> Week 05

| Change | Count | Files |
|---|---:|---|
| New | 4 | Three `network/*.kt` files and `network_security_config.xml` |
| Expanded | 7 | Gradle, manifest, Scan/Result Activities, Scan/Result layouts, strings |
| Unchanged Android source/resources | 10 | Main, History, Library, Settings, their layouts, colors, themes, FileProvider paths |
| Backend files changed | 0 | Week 04 contract is consumed unchanged |
| Later-week files added | 0 | No Room, TFLite, offline assets, notification, or bottom navigation |

**Required Android source/resource files after Week 05: 21.**

**Total across the 11 changed/new cumulative files: 726 logical lines.**

Logical line count ignores whether the final line ends with a newline. These counts describe the teaching snapshot, not the repository's later fully evolved app.

### 12.2 Exact Week 05 Tree

```text
android-app-kotlin/
|-- settings.gradle                                  UNCHANGED
|-- build.gradle                                     UNCHANGED
|-- gradle.properties                                UNCHANGED
`-- app/
    |-- build.gradle                                 EXPANDED   47 lines
    `-- src/main/
        |-- AndroidManifest.xml                      EXPANDED   55 lines
        |-- java/com/leafguard/
        |   |-- MainActivity.kt                      UNCHANGED
        |   |-- ScanActivity.kt                      EXPANDED  247 lines
        |   |-- ResultActivity.kt                    EXPANDED   56 lines
        |   |-- HistoryActivity.kt                   UNCHANGED
        |   |-- DiseaseLibraryActivity.kt            UNCHANGED
        |   |-- SettingsActivity.kt                  UNCHANGED
        |   `-- network/
        |       |-- ApiService.kt                    NEW        13 lines
        |       |-- PredictionResponse.kt            NEW        22 lines
        |       `-- RetrofitClient.kt                NEW        33 lines
        `-- res/
            |-- layout/
            |   |-- activity_main.xml                UNCHANGED
            |   |-- activity_scan.xml                EXPANDED   76 lines
            |   |-- activity_result.xml              EXPANDED  115 lines
            |   |-- activity_history.xml             UNCHANGED
            |   |-- activity_disease_library.xml     UNCHANGED
            |   `-- activity_settings.xml            UNCHANGED
            |-- values/
            |   |-- strings.xml                      EXPANDED   55 lines
            |   |-- colors.xml                       UNCHANGED
            |   `-- themes.xml                       UNCHANGED
            `-- xml/
                |-- file_provider_paths.xml          UNCHANGED
                `-- network_security_config.xml      NEW         7 lines
```

The Java track under `android-app/` mirrors this behavior with Java classes. Kotlin remains the authoritative learning snapshot.

### 12.3 Expanded File: `app/build.gradle` (40 -> 47 lines)

Week 03 used four Android dependencies. Week 05 adds Retrofit, the Gson converter, and OkHttp logging. `buildConfig` is enabled because the cumulative ScanActivity derives its FileProvider authority from `BuildConfig.APPLICATION_ID`.

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace 'com.leafguard'
    compileSdk 34

    defaultConfig {
        applicationId "com.leafguard"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "0.1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig true
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
}
```

### 12.4 Expanded File: `AndroidManifest.xml` (53 -> 55 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-feature
        android:name="android.hardware.camera"
        android:required="false" />

    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:icon="@android:drawable/ic_menu_gallery"
        android:label="@string/app_name"
        android:networkSecurityConfig="@xml/network_security_config"
        android:supportsRtl="true"
        android:theme="@style/Theme.LeafGuardAI">

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_provider_paths" />
        </provider>

        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity
            android:name=".ScanActivity"
            android:exported="false" />
        <activity
            android:name=".ResultActivity"
            android:exported="false" />
        <activity
            android:name=".HistoryActivity"
            android:exported="false" />
        <activity
            android:name=".DiseaseLibraryActivity"
            android:exported="false" />
        <activity
            android:name=".SettingsActivity"
            android:exported="false" />
    </application>

</manifest>
```

### 12.5 New File: `network/ApiService.kt` (13 lines)

```kotlin
package com.leafguard.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("predict")
    fun uploadImage(@Part image: MultipartBody.Part): Call<PredictionResponse>
}
```

### 12.6 New File: `network/PredictionResponse.kt` (22 lines)

```kotlin
package com.leafguard.network

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("model_label")
    val modelLabel: String,
    @SerializedName("disease")
    val disease: String,
    @SerializedName("confidence")
    val confidence: Float,
    @SerializedName("uncertain")
    val uncertain: Boolean,
    @SerializedName("guidance_available")
    val guidanceAvailable: Boolean,
    @SerializedName("symptoms")
    val symptoms: String,
    @SerializedName("treatment")
    val treatment: String,
    @SerializedName("prevention")
    val prevention: String
)
```

### 12.7 New File: `network/RetrofitClient.kt` (33 lines)

```kotlin
package com.leafguard.network

import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    private val apiClient: Retrofit by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        apiClient.create(ApiService::class.java)
    }
}
```

### 12.8 Expanded File: `ScanActivity.kt` (132 -> 247 lines)

The full file preserves all Week 03 permission, camera, gallery, FileProvider, preview, and saved-state code. Week 05 adds cache copying, multipart creation, callbacks, progress state, and result navigation.

```kotlin
package com.leafguard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.leafguard.network.PredictionResponse
import com.leafguard.network.RetrofitClient
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanActivity : AppCompatActivity() {

    private lateinit var imagePreview: ImageView
    private lateinit var textImageStatus: TextView
    private lateinit var buttonDetectDisease: Button
    private lateinit var progressUpload: ProgressBar

    private var selectedImageUri: Uri? = null
    private var pendingCameraUri: Uri? = null

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera()
        } else {
            Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        val cameraUri = pendingCameraUri
        if (success && cameraUri != null) {
            updateSelectedImage(cameraUri)
        } else {
            Toast.makeText(this, R.string.camera_cancelled, Toast.LENGTH_SHORT).show()
        }
        pendingCameraUri = null
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            updateSelectedImage(uri)
        } else {
            Toast.makeText(this, R.string.gallery_cancelled, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        imagePreview = findViewById(R.id.imagePreview)
        textImageStatus = findViewById(R.id.textImageStatus)
        buttonDetectDisease = findViewById(R.id.buttonDetectDisease)
        progressUpload = findViewById(R.id.progressUpload)

        findViewById<Button>(R.id.buttonTakePhoto).setOnClickListener {
            openCameraWithPermissionCheck()
        }
        findViewById<Button>(R.id.buttonChooseGallery).setOnClickListener {
            galleryLauncher.launch("image/*")
        }
        buttonDetectDisease.setOnClickListener {
            uploadSelectedImage()
        }

        savedInstanceState?.getString(KEY_SELECTED_IMAGE_URI)?.let { uriText ->
            updateSelectedImage(Uri.parse(uriText))
        }
    }

    private fun openCameraWithPermissionCheck() {
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            launchCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun launchCamera() {
        try {
            val imageUri = createImageUri()
            pendingCameraUri = imageUri
            cameraLauncher.launch(imageUri)
        } catch (exception: IOException) {
            pendingCameraUri = null
            Toast.makeText(this, R.string.camera_file_error, Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    private fun createImageUri(): Uri {
        val imageDirectory = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captures")
        if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
            throw IOException("Could not create image directory")
        }

        val imageFile = File(imageDirectory, "leafguard_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(
            this,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            imageFile
        )
    }

    private fun updateSelectedImage(uri: Uri) {
        selectedImageUri = uri
        imagePreview.setImageURI(uri)
        textImageStatus.setText(R.string.image_ready_for_upload)
        buttonDetectDisease.isEnabled = true
    }

    private fun uploadSelectedImage() {
        val imageUri = selectedImageUri
        if (imageUri == null) {
            Toast.makeText(this, R.string.select_image_first, Toast.LENGTH_SHORT).show()
            return
        }

        setUploadInProgress(true)
        val uploadFile = try {
            copyUriToCacheFile(imageUri)
        } catch (exception: IOException) {
            setUploadInProgress(false)
            Toast.makeText(this, R.string.image_prepare_error, Toast.LENGTH_LONG).show()
            return
        }

        val mimeType = contentResolver.getType(imageUri) ?: "image/*"
        val requestBody = uploadFile.asRequestBody(mimeType.toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", uploadFile.name, requestBody)

        RetrofitClient.apiService.uploadImage(imagePart).enqueue(
            object : Callback<PredictionResponse> {
                override fun onResponse(
                    call: Call<PredictionResponse>,
                    response: Response<PredictionResponse>
                ) {
                    uploadFile.delete()
                    setUploadInProgress(false)
                    val prediction = response.body()
                    if (!response.isSuccessful || prediction == null) {
                        Toast.makeText(
                            this@ScanActivity,
                            getString(R.string.server_error_format, response.code()),
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                    openResult(prediction)
                }

                override fun onFailure(
                    call: Call<PredictionResponse>,
                    throwable: Throwable
                ) {
                    uploadFile.delete()
                    setUploadInProgress(false)
                    Toast.makeText(
                        this@ScanActivity,
                        R.string.network_error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    @Throws(IOException::class)
    private fun copyUriToCacheFile(uri: Uri): File {
        val uploadFile = File(cacheDir, "leafguard_upload_${System.currentTimeMillis()}.jpg")
        try {
            contentResolver.openInputStream(uri).use { inputStream ->
                if (inputStream == null) {
                    throw IOException("Unable to open selected image")
                }
                FileOutputStream(uploadFile).use { outputStream ->
                    inputStream.copyTo(outputStream, bufferSize = 8192)
                }
            }
        } catch (exception: IOException) {
            uploadFile.delete()
            throw exception
        }
        return uploadFile
    }

    private fun openResult(prediction: PredictionResponse) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_MODEL_LABEL, prediction.modelLabel)
            putExtra(ResultActivity.EXTRA_DISEASE, prediction.disease)
            putExtra(ResultActivity.EXTRA_CONFIDENCE, prediction.confidence)
            putExtra(ResultActivity.EXTRA_UNCERTAIN, prediction.uncertain)
            putExtra(ResultActivity.EXTRA_GUIDANCE_AVAILABLE, prediction.guidanceAvailable)
            putExtra(ResultActivity.EXTRA_SYMPTOMS, prediction.symptoms)
            putExtra(ResultActivity.EXTRA_TREATMENT, prediction.treatment)
            putExtra(ResultActivity.EXTRA_PREVENTION, prediction.prevention)
        }
        startActivity(intent)
    }

    private fun setUploadInProgress(inProgress: Boolean) {
        progressUpload.visibility = if (inProgress) View.VISIBLE else View.GONE
        buttonDetectDisease.isEnabled = !inProgress && selectedImageUri != null
        findViewById<Button>(R.id.buttonTakePhoto).isEnabled = !inProgress
        findViewById<Button>(R.id.buttonChooseGallery).isEnabled = !inProgress
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SELECTED_IMAGE_URI, selectedImageUri?.toString())
    }

    companion object {
        private const val KEY_SELECTED_IMAGE_URI = "selected_image_uri"
    }
}
```

### 12.9 Expanded File: `ResultActivity.kt` (12 -> 56 lines)

```kotlin
package com.leafguard

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val disease = intent.getStringExtra(EXTRA_DISEASE) ?: getString(R.string.result_unknown)
        val modelLabel = intent.getStringExtra(EXTRA_MODEL_LABEL) ?: getString(R.string.result_unknown)
        val confidence = intent.getFloatExtra(EXTRA_CONFIDENCE, 0f)
        val uncertain = intent.getBooleanExtra(EXTRA_UNCERTAIN, true)
        val guidanceAvailable = intent.getBooleanExtra(EXTRA_GUIDANCE_AVAILABLE, false)
        val symptoms = intent.getStringExtra(EXTRA_SYMPTOMS) ?: getString(R.string.guidance_unavailable)
        val treatment = intent.getStringExtra(EXTRA_TREATMENT) ?: getString(R.string.guidance_unavailable)
        val prevention = intent.getStringExtra(EXTRA_PREVENTION) ?: getString(R.string.guidance_unavailable)
        val confidencePercent = (confidence * 100f).roundToInt()

        findViewById<TextView>(R.id.textResultDisease).text = disease
        findViewById<TextView>(R.id.textResultModelLabel).text = getString(
            R.string.model_label_format,
            modelLabel
        )
        findViewById<TextView>(R.id.textResultConfidence).text = getString(
            R.string.confidence_format,
            confidencePercent
        )
        findViewById<ProgressBar>(R.id.progressResultConfidence).progress = confidencePercent
        findViewById<TextView>(R.id.textResultStatus).text = getString(
            if (uncertain) R.string.result_uncertain else R.string.result_confident
        )
        findViewById<TextView>(R.id.textGuidanceStatus).text = getString(
            if (guidanceAvailable) R.string.guidance_available else R.string.guidance_not_reviewed
        )
        findViewById<TextView>(R.id.textResultSymptoms).text = symptoms
        findViewById<TextView>(R.id.textResultTreatment).text = treatment
        findViewById<TextView>(R.id.textResultPrevention).text = prevention
    }

    companion object {
        const val EXTRA_MODEL_LABEL = "extra_model_label"
        const val EXTRA_DISEASE = "extra_disease"
        const val EXTRA_CONFIDENCE = "extra_confidence"
        const val EXTRA_UNCERTAIN = "extra_uncertain"
        const val EXTRA_GUIDANCE_AVAILABLE = "extra_guidance_available"
        const val EXTRA_SYMPTOMS = "extra_symptoms"
        const val EXTRA_TREATMENT = "extra_treatment"
        const val EXTRA_PREVENTION = "extra_prevention"
    }
}
```

### 12.10 Expanded File: `activity_scan.xml` (60 -> 76 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screen_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="24dp">

        <TextView
            android:id="@+id/textScanTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/scan_title"
            android:textColor="@color/text_primary"
            android:textSize="24sp" />

        <TextView
            android:id="@+id/textScanInstruction"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="@string/scan_instruction"
            android:textColor="@color/text_secondary"
            android:textSize="16sp" />

        <ImageView
            android:id="@+id/imagePreview"
            android:layout_width="match_parent"
            android:layout_height="280dp"
            android:layout_marginTop="20dp"
            android:background="#E8F5E9"
            android:contentDescription="@string/scan_preview_description"
            android:scaleType="centerCrop" />

        <TextView
            android:id="@+id/textImageStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="@string/no_image_selected"
            android:textColor="@color/text_secondary" />

        <Button
            android:id="@+id/buttonTakePhoto"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:text="@string/take_photo" />

        <Button
            android:id="@+id/buttonChooseGallery"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/choose_from_gallery" />

        <Button
            android:id="@+id/buttonDetectDisease"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:enabled="false"
            android:text="@string/detect_disease" />

        <ProgressBar
            android:id="@+id/progressUpload"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="12dp"
            android:contentDescription="@string/upload_progress_description"
            android:visibility="gone" />
    </LinearLayout>
</ScrollView>
```

### 12.11 Expanded File: `activity_result.xml` (25 -> 115 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screen_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="24dp">

        <TextView
            android:id="@+id/textResultTitle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/result_title"
            android:textColor="@color/text_primary"
            android:textSize="24sp" />

        <TextView
            android:id="@+id/textResultDisease"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/result_unknown"
            android:textColor="@color/leaf_green_dark"
            android:textSize="22sp" />

        <TextView
            android:id="@+id/textResultModelLabel"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:text="@string/model_label_placeholder"
            android:textColor="@color/text_secondary" />

        <TextView
            android:id="@+id/textResultConfidence"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="12dp"
            android:text="@string/confidence_placeholder"
            android:textColor="@color/text_primary" />

        <ProgressBar
            android:id="@+id/progressResultConfidence"
            style="?android:attr/progressBarStyleHorizontal"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:max="100"
            android:progress="0" />

        <TextView
            android:id="@+id/textResultStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="@string/result_uncertain"
            android:textColor="@color/text_secondary" />

        <TextView
            android:id="@+id/textGuidanceStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:text="@string/guidance_not_reviewed"
            android:textColor="@color/text_secondary" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="20dp"
            android:text="@string/symptoms_heading"
            android:textColor="@color/text_primary"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textResultSymptoms"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/guidance_unavailable"
            android:textColor="@color/text_secondary" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/treatment_heading"
            android:textColor="@color/text_primary"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textResultTreatment"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/guidance_unavailable"
            android:textColor="@color/text_secondary" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="@string/prevention_heading"
            android:textColor="@color/text_primary"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/textResultPrevention"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@string/guidance_unavailable"
            android:textColor="@color/text_secondary" />
    </LinearLayout>
</ScrollView>
```

### 12.12 Expanded File: `strings.xml` (35 -> 55 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">LeafGuard AI</string>

    <string name="home_title">LeafGuard AI</string>
    <string name="home_subtitle">Plant disease detection learning app</string>

    <string name="open_scan">Open Scan</string>
    <string name="open_result">Open Sample Result</string>
    <string name="open_history">Open History</string>
    <string name="open_library">Open Disease Library</string>
    <string name="open_settings">Open Settings</string>

    <string name="scan_title">Scan Leaf</string>
    <string name="result_title">Prediction Result</string>
    <string name="history_title">History</string>
    <string name="library_title">Disease Library</string>
    <string name="settings_title">Settings and About</string>

    <string name="placeholder_history">Saved scan history will be added in Week 07.</string>
    <string name="placeholder_library">The XML disease library will be added in Week 08.</string>
    <string name="placeholder_settings">Course project shell. Settings options will grow in later weeks.</string>

    <string name="scan_instruction">Take a photo or choose an image, then upload it to the Week 04 backend.</string>
    <string name="scan_preview_description">Preview of the selected leaf image</string>
    <string name="take_photo">Take Photo</string>
    <string name="choose_from_gallery">Choose from Gallery</string>
    <string name="no_image_selected">No image selected yet.</string>
    <string name="image_ready_for_upload">Image selected. Ready to detect.</string>
    <string name="camera_permission_denied">Camera permission denied. You can still choose from gallery.</string>
    <string name="camera_cancelled">Camera cancelled. No new image selected.</string>
    <string name="gallery_cancelled">Gallery closed. No new image selected.</string>
    <string name="camera_file_error">Could not prepare a file for the camera.</string>

    <string name="detect_disease">Detect Disease</string>
    <string name="upload_progress_description">Uploading image for prediction</string>
    <string name="select_image_first">Select or capture an image first.</string>
    <string name="image_prepare_error">Could not prepare the selected image for upload.</string>
    <string name="server_error_format">Server rejected the request (HTTP %1$d).</string>
    <string name="network_error">Could not reach the backend. Check the server and emulator URL.</string>

    <string name="result_unknown">Unknown result</string>
    <string name="model_label_placeholder">Model label: unavailable</string>
    <string name="model_label_format">Model label: %1$s</string>
    <string name="confidence_placeholder">Confidence: 0%%</string>
    <string name="confidence_format">Confidence: %1$d%%</string>
    <string name="result_uncertain">Low-confidence result: verify before acting.</string>
    <string name="result_confident">Confidence is above the configured server threshold.</string>
    <string name="guidance_available">Reviewed project guidance is available.</string>
    <string name="guidance_not_reviewed">Detailed project guidance is not reviewed for this label.</string>
    <string name="symptoms_heading">Symptoms</string>
    <string name="treatment_heading">Treatment</string>
    <string name="prevention_heading">Prevention</string>
    <string name="guidance_unavailable">No information available.</string>
</resources>
```

### 12.13 New File: `network_security_config.xml` (7 lines)

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="false" />
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="false">10.0.2.2</domain>
    </domain-config>
</network-security-config>
```

### 12.14 Files Week 05 Does Not Rewrite

| Area | Week 05 status | Later owner |
|---|---|---|
| `MainActivity.kt` and home layout | Unchanged navigation shell | UI polish week |
| History Activity/layout | Placeholder | Week 07 |
| Disease Library Activity/layout | Placeholder | Week 08 |
| Settings Activity/layout | Placeholder | Later UI/settings week |
| `colors.xml`, `themes.xml` | Unchanged | UI polish week |
| `file_provider_paths.xml` | Unchanged Week 03 security boundary | Week 03 |
| Backend source | Unchanged, consumed as contract | Week 06 changes inference |
| Room, TFLite, notifications | Absent | Future weeks |

### 12.15 Verify the Exact Week 05 State

```bash
# Backend contract first
cd backend-api
USE_MOCK=true .venv/bin/python -m unittest -v test_api
USE_MOCK=true .venv/bin/uvicorn main:app --reload

# Android build in another terminal
cd android-app-kotlin
./gradlew assembleDebug
```

Behavior checks:

| Check | Expected |
|---|---|
| Gallery/camera selection | Preview and Detect button enabled |
| Active upload | Progress visible; input buttons disabled |
| Backend mock success | Result screen opens with all contract states represented |
| Backend stopped | Friendly network error; UI restored; no crash |
| Invalid server response | HTTP status message; UI restored |
| Repeated attempt | Another upload can start |

Save evidence under `docs/evidence/week-05/`.

---

## 13. Learning-to-Evidence Map

| Concept | Exercise | Build step | Validation proof |
|---|---|---|---|
| Week 03 + Week 04 handoff | 1 | 1 | Boundary explanation |
| Eight-field response | 2 | 3 | Model inspection and Result screen |
| URI-to-cache conversion | 3 | 7 | Camera and gallery uploads |
| Multipart `image` | 3 | 7 | Backend receives 200 |
| Async callbacks | 4 | 8 | Responsive loading state |
| HTTP vs network error | 5 | 8 | Two distinct failure demos |
| Local security | 5 | 4 | Emulator reaches only allowed host |
| Complete integration | 6 | 10 | Milestone demo and evidence |

---

## 14. Week 05 Understanding Checklist

- [ ] I can explain how Weeks 03 and 04 combine in Week 05.
- [ ] I can explain why the emulator uses `10.0.2.2`.
- [ ] I can explain why Retrofit requires a trailing base-URL slash.
- [ ] I can name the multipart field `image`.
- [ ] I can name all eight JSON response fields.
- [ ] I can explain why a content URI is copied to cache.
- [ ] I can distinguish `onResponse` from `onFailure`.
- [ ] I can explain why `enqueue` does not freeze the UI.
- [ ] I can explain the local cleartext exception and production HTTPS rule.
- [ ] I can demonstrate success and backend-unavailable behavior.
- [ ] I can explain why mock success is not model-accuracy evidence.
- [ ] I can identify all 4 new and 7 expanded files.

<!-- NAV_FOOTER_START -->

---

## Week 05 Navigation

| Step | File | Description |
|---:|---|---|
| 1 | [README.md](README.md) | Week overview |
| **2** | **learning-notes.md** - current | Theory and exact source snapshot |
| 3 | [exercises.md](exercises.md) | Guided practice |
| 4 | [build-task.md](build-task.md) | Implementation guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation and evidence |
| 6 | [quiz.md](quiz.md) | Knowledge assessment |
| 7 | [reflection.md](reflection.md) | Reflection and handoff |

[Previous: Week 04](../week-04-fastapi-backend/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Week 06](../week-06-cloud-ml-model/README.md)