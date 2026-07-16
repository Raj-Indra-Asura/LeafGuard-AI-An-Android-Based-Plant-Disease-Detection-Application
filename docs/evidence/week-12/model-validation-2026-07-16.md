# Week 12 Model Validation Evidence - 2026-07-16

## Scope and outcome

This run validated the reproducible Keras-to-FastAPI and Keras-to-TFLite paths in the
dev container. Artifact integrity, conversion parity, real backend inference, both
Android builds, APK contents, and build-time tests passed. The model is not approved as
"perfect": semantic accuracy was 28/30 on the limited real-image set, and no Android
device or emulator was attached for installation, UI, network, or airplane-mode tests.

## Provenance

- Upstream repository: `Muhammad-Hassan12/Plant-Disease-Detector`
- Upstream `main` commit: `f6165bd93524dfb77a9629aae70db845832d1b01`
- Local Keras path: `backend-api/models/leafguard_model.keras`
- Keras byte size: `25143175`
- Keras SHA-256: `08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f`
- Remote `main`, pinned-commit download, and local file: byte-for-byte identical
- Upstream license file: MIT; redistribution still requires preserving its notice

## Contract and conversion

| Check | Result |
|---|---|
| Canonical labels | 38 unique entries |
| Backend/Kotlin/Java label comparison | Byte-for-byte match |
| Label SHA-256 | `23eeb476d27d53fea16ba988aaca02d52673ba780412bfc19f4a8aa72d36b53d` |
| Keras saved version | 3.10.0 |
| Keras input/output | float32 `[None,224,224,3]` / `[None,38]` |
| Embedded scaling probe | `0 -> -1`, `127.5 -> 0`, `255 -> 1` |
| Generated TFLite size | 9,056,916 bytes |
| Generated TFLite SHA-256 | `22ea2d4a47a52b2d9b150e0f74b113def0f12bbdb59209f7e0bce2a9701d41f9` |
| Kotlin/Java TFLite comparison | Byte-for-byte match |
| TFLite input/output | float32 `[1,224,224,3]` / `[1,38]` |

TensorFlow 2.14 could not load this Keras 3 artifact. The verified runtime is Python
3.11 with TensorFlow 2.19.1. The contract checker was updated to validate Keras 3
single-tensor lists and the equivalent `TrueDivide` plus `Subtract` preprocessing graph.

The canonical labels exactly follow the pinned source README's published 38-name list.
The source training notebook and test script instead use lowercase `healthy` and longer
names such as `Corn_(maize)` at the same indices. This is a display-name normalization,
not an output-index difference; it must remain documented because the upstream source is
internally inconsistent.

## Parity and semantic results

Thirty real PlantVillage RGB images were tested: ten Tomato Early Blight, ten Tomato
Late Blight, and ten Tomato Healthy. They were downloaded temporarily from the public
`spMohanty/PlantVillage-Dataset` repository and were not committed.

- Keras/TFLite top-class parity: 30/30
- Maximum observed confidence delta: 0.000006
- Tomato Early Blight accuracy: 8/10
- Tomato Late Blight accuracy: 10/10
- Tomato Healthy accuracy: 10/10
- Overall limited-set top-1 accuracy: 28/30 (93.3%)

Failures:

| Expected | Predicted | Confidence |
|---|---|---:|
| Tomato Early Blight | Tomato Late Blight | 0.790792 |
| Tomato Early Blight | Tomato Septoria Leaf Spot | 0.527168 |

The three committed synthetic illustrations also passed numerical parity, but all were
classified as Blueberry Healthy with 0.8339-0.9813 confidence. They are out-of-
distribution plumbing fixtures, not accuracy evidence, and show that confidence alone
does not reliably reject non-photographic inputs.

## Backend results

- `USE_MOCK=false` loaded the verified Keras model.
- Backend tests: 7 passed, 1 skipped because the real model was available.
- `/health`: `status=ok`, `use_mock=false`, `model_loaded=true`, `image_size=224`,
  `class_count=38`.
- Multipart `/predict` for a real Tomato Late Blight image returned
  `Tomato___Late_blight`, confidence `0.9977`, matching TFLite.
- The production-style Docker image built with `INSTALL_TENSORFLOW=true`. With the
  verified Keras file mounted read-only, container `/health` reported the real 38-class
  model and `/predict` returned `Tomato___Healthy` at confidence `1.0`.

## Android and APK results

Both Kotlin and Java tracks passed `testDebugUnitTest`, `lintDebug`, `assembleDebug`,
`assembleRelease`, the 38-label release model gate, and `assembleDebugAndroidTest`.
Instrumentation APKs compiled but could not run without a device.

| Artifact | Bytes | Signature | Packaged model |
|---|---:|---|---|
| Kotlin debug APK | 47,879,601 | v2 verified | Hash matches |
| Kotlin release APK | 46,134,034 | Unsigned | Hash matches |
| Java debug APK | 47,746,214 | v2 verified | Hash matches |
| Java release APK | 46,017,641 | Unsigned | Hash matches |

A disposable `/tmp` validation key successfully produced a v2-signed Kotlin release
APK. It is not a production key or releasable artifact. V1 was not emitted because the
app's minimum API is 24, where v2 is supported.

## Release sequence status

| Step | Status |
|---|---|
| Download, pin, hash, and license review | Pass |
| Label synchronization and model inspection | Pass |
| Conversion and identical Android artifacts | Pass |
| Real-image Keras/TFLite parity | Pass |
| Real FastAPI health and inference | Pass |
| Android source, tests, lint, and APK builds | Pass |
| Semantic accuracy is perfect | Fail: 28/30 on limited set |
| Android online inference on device | Not run: no device/emulator attached |
| Android offline inference with internet disabled | Not run: no device/emulator attached |
| Install exact signed release APK | Not run: no device and no production keystore |
| Publish verified APK/checksum to GitHub Release | Fail: existing asset is the placeholder build |

The published `v0.2.0-beta` APK was downloaded and inspected. Its SHA-256 is
`020c1c25760cd4737b7952c2e77944538e6e95732e9955bbdc0b5c6f3d401abf`; it contains
a 77-byte `assets/model.tflite` without a `TFL3` header. It is the historical
placeholder/mock APK and must be replaced or clearly withdrawn after a production-signed,
device-tested real-model build is approved. This validation did not modify the release.

## Manual gates before release

1. Run connected Android tests and a real TFLite prediction test on API 24 and a recent API.
2. Compare cloud and offline results in the app using the same documented real images.
3. Disable networking and prove offline inference, history, and result guidance still work.
4. Test camera/gallery permissions, invalid inputs, low confidence, and backend failure.
5. Expand to an independent, leaf-group-separated test set covering all 38 classes.
6. Sign with the private production key, verify, install that exact APK, and record its hash.
7. Publish only after the remaining Week 12 checklist items have evidence.
