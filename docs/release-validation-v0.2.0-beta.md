# LeafGuard AI v0.2.0-beta Release Validation

> Historical record: this 2026-07-12 validation used placeholder/mock model paths.
> It is superseded for model acceptance by
> [`evidence/week-12/model-validation-2026-07-16.md`](evidence/week-12/model-validation-2026-07-16.md).
> Its APK hash must not be presented as the current real-model release artifact.

Validation date: 2026-07-12

## Release classification

This is an installable **debug-signed prerelease** for Android testing and course demonstration. It is not a production diagnostic release.

The bundled `model.tflite` is still a text placeholder. Offline mode therefore uses the documented three-class tomato leaf-color heuristic. The repository also contains no trained Keras backend model, so the verified backend runs in mock mode. Predictions must be manually verified and must not be represented as validated diagnoses.

## Automated results

| Check | Result |
|---|---|
| Kotlin unit tests | Passed |
| Kotlin Android lint | Passed with no blocking errors |
| Kotlin debug APK | Built |
| Kotlin release APK | Built unsigned |
| Kotlin instrumented-test APK | Compiled |
| Java unit tests | Passed |
| Java Android lint | Passed with no blocking errors |
| Java debug APK | Built |
| Java release APK | Built unsigned |
| Java instrumented-test APK | Compiled |
| Backend source compilation | Passed |
| Backend API tests | 6/6 passed |
| GitHub Actions workflow syntax | Valid YAML |
| Backend Docker image | Built and health smoke test passed |

The backend tests cover both health paths, the 10-entry disease library, a valid prediction request, rejected non-image and spoofed-image requests, and the upload size limit.

## APK identity

- Package: `com.leafguard`
- Version code: `2`
- Version name: `0.2.0-beta`
- Minimum Android: API 24 (Android 7.0)
- Target Android: API 34
- Artifact: `LeafGuard-AI-v0.2.0-beta.apk`
- Size: 38,675,305 bytes
- SHA-256: `020c1c25760cd4737b7952c2e77944538e6e95732e9955bbdc0b5c6f3d401abf`
- Signature: Android debug certificate, APK Signature Scheme v2 verified

## Changes validated

- Correct history-detail confidence sharing and percentage bounds.
- Offline mode selected by default so the app can run without a backend.
- Invalid backend URLs handled without crashing Retrofit.
- Cleartext network access restricted to emulator and localhost development addresses.
- Unused location and broad media/storage permissions removed.
- Gallery selection uses URI-scoped access.
- Cloud upload cache files cleaned after requests and failed copies.
- Disease XML stream always closed.
- Analytics tab now summarizes local Room history.
- Silent destructive Room migration fallback removed.
- Backend `/health` alias, bounded uploads, confidence bounds, file cleanup, and safer CORS credentials behavior added.
- CI, backend tests, Docker deployment, and maintenance guidance added.

## Manual validation still required

No Android emulator or physical phone was attached to the validation environment. The instrumented tests were compiled but not executed. Before promoting beyond beta, install the APK and verify:

1. First launch and notification permission behavior.
2. Camera capture and camera-permission denial.
3. Gallery selection on an API 24 device and a recent Android device.
4. Offline result, sharing, Room save/reload/delete, analytics, and disease search.
5. Cloud mode against an HTTPS deployment on a physical phone.
6. Upgrade installation over the previous APK and history retention.

A production release additionally requires a trained and measured model, real-device test evidence, a private release signing key, and a stable HTTPS backend if Cloud mode is offered.
