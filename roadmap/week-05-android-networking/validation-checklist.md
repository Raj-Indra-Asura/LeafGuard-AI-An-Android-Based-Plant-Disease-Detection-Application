# Week 05 Validation Checklist: Android-to-FastAPI Connection

## Milestone Demo

> Select or capture an image, upload it to the Week 04 backend, display the mock response in ResultActivity, then stop the backend and show a friendly retryable error. This proves the cumulative product state is 45%.

Every required item must be yes before Week 06.

Record results in `docs/evidence/week-05/validation.md`:

```text
Item:
Method: build | source inspection | emulator | backend test | Logcat
Expected:
Actual:
Evidence:
Result: PASS | FAIL | NOT TESTED
```

`NOT TESTED` is not a pass.

---

## 1. Progressive Boundary

- [ ] Week 03 camera capture still previews an image.
- [ ] Week 03 gallery selection still previews an image.
- [ ] Week 04 backend runs without Android.
- [ ] All eight Week 04 backend tests pass.
- [ ] I can explain why Week 05 changes Android but not the backend contract.
- [ ] I do not describe mock output as real disease recognition.

Pass rule: all 6.

---

## 2. Exact Repository State

- [ ] The Kotlin target has exactly 4 new Week 05 files.
- [ ] The Kotlin target has exactly 7 expanded Week 05 files.
- [ ] `app/build.gradle` contains only the three new networking dependencies.
- [ ] `buildConfig` generation is enabled.
- [ ] No Week 05 Room, TFLite, WorkManager, or notification code is required.
- [ ] The complete target matches learning-notes Section 12.
- [ ] `./gradlew assembleDebug` succeeds.

Pass rule: all 7.

---

## 3. API and JSON Contract

- [ ] `ApiService` is an interface.
- [ ] The method uses `@Multipart`.
- [ ] The method uses `@POST("predict")`.
- [ ] The file parameter uses `@Part`.
- [ ] `MultipartBody.Part` is named `image` during construction.
- [ ] `PredictionResponse` contains exactly eight contract properties.
- [ ] `model_label` maps to `modelLabel`.
- [ ] `guidance_available` maps to `guidanceAvailable`.
- [ ] Confidence remains on the 0.0-1.0 scale until display.

Pass rule: all 9.

---

## 4. Retrofit and Network Security

- [ ] Base URL is defined once.
- [ ] Emulator URL is `http://10.0.2.2:8000/`.
- [ ] Base URL ends with `/`.
- [ ] Gson converter is registered.
- [ ] Connect, read, and write timeouts are configured.
- [ ] Logging does not dump uploaded body bytes.
- [ ] Manifest declares `INTERNET` before `<application>`.
- [ ] Network security denies cleartext by default.
- [ ] Cleartext exception is limited to `10.0.2.2`.

Pass rule: all 9.

---

## 5. URI and Multipart Preparation

- [ ] Upload starts only when `selectedImageUri` is non-null.
- [ ] The URI is opened with `ContentResolver`.
- [ ] The code does not rely on `File(uri.path)`.
- [ ] Bytes are copied to an app cache file.
- [ ] MIME type comes from the content resolver with a safe fallback.
- [ ] Filename is included in the multipart part.
- [ ] Cache file is deleted after response.
- [ ] Cache file is deleted after failure.
- [ ] URI-copy failure shows a safe message and restores controls.

Pass rule: all 9.

---

## 6. Asynchronous UI State

- [ ] Detect is disabled before image selection.
- [ ] Detect is enabled after selection.
- [ ] Progress is hidden initially.
- [ ] Progress appears when upload starts.
- [ ] Camera, gallery, and Detect are disabled during upload.
- [ ] `enqueue` is used; `execute` is not used.
- [ ] Progress hides after HTTP response.
- [ ] Progress hides after network failure.
- [ ] Controls permit retry after every terminal path.

Pass rule: all 9.

---

## 7. Response and Result Display

- [ ] `response.isSuccessful` is checked.
- [ ] Response body is checked for null.
- [ ] ResultActivity opens only for valid success.
- [ ] All eight values are passed with named Intent constants.
- [ ] Confidence is displayed as a percentage.
- [ ] Model label and display disease are both represented.
- [ ] Uncertainty is represented.
- [ ] Guidance availability is represented.
- [ ] Symptoms, treatment, and prevention are displayed.
- [ ] Safe defaults prevent a direct ResultActivity launch from crashing.

Pass rule: all 10.

---

## 8. Failure Behavior

- [ ] Backend-stopped request reaches `onFailure`.
- [ ] Backend-stopped request shows a friendly message.
- [ ] HTTP 400/413/422/503 stays in `onResponse`.
- [ ] HTTP rejection includes the status code.
- [ ] Wrong multipart field is understood as a 422 contract error.
- [ ] No raw stack trace is shown to the user.
- [ ] App never crashes during tested failures.
- [ ] Backend restart permits a successful retry.

Pass rule: all 8.

---

## 9. Manual End-to-End Demo

- [ ] Backend `/health` reports mock mode.
- [ ] App launches on the emulator.
- [ ] Scan opens from Home.
- [ ] A real image is selected or captured.
- [ ] Upload progress is visible.
- [ ] Logcat shows `POST /predict` and HTTP 200.
- [ ] ResultActivity displays the returned response.
- [ ] I verbally state that the result is mock pipeline evidence.
- [ ] Backend-down behavior is demonstrated.

Pass rule: all 9.

---

## 10. Evidence and Understanding

- [ ] Backend test output saved.
- [ ] Android build output saved.
- [ ] Health-mode evidence saved.
- [ ] Upload-progress evidence saved.
- [ ] Result-screen evidence saved.
- [ ] Backend-down evidence saved.
- [ ] API contract note saved.
- [ ] Quiz score is at least 14/18.
- [ ] Reflection answers use observed evidence.
- [ ] Progress tracker is updated.
- [ ] Evidence contains no private IP, secret, `.env`, or personal photo.

Pass rule: all 11.

---

## Failure Routing

| Failure | Return to | Focused recheck |
|---|---|---|
| Gradle cannot resolve Retrofit | Build Step 2 | Gradle sync/build |
| `BuildConfig` unresolved | `buildFeatures` | Kotlin compilation |
| Cleartext blocked | Manifest/security config | One emulator request |
| Connection refused | Backend and `10.0.2.2` | `/health`, then upload |
| HTTP 422 | Multipart name | One request using `image` |
| Gson conversion fails | Eight-field model | Valid mock response |
| UI remains busy | Callback terminal paths | Backend-down retry |
| Result crashes | Extras/defaults/IDs | Direct and network launch |

After a repair, run the focused check and then the full milestone demo.

---

## Completion Criteria

Week 05 is complete only when:

1. Backend tests and Android build pass.
2. One valid image travels Android -> FastAPI -> ResultActivity.
3. All eight response fields parse.
4. Loading state is visible and recoverable.
5. HTTP and network failures are distinguished.
6. Backend-unavailable behavior does not crash.
7. Evidence and understanding checks pass.
8. No real-model claim or future-week implementation leaks into the slice.

<!-- NAV_FOOTER_START -->

---

## Week 05 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | **Validation - current** | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Build Task](build-task.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Quiz](quiz.md)