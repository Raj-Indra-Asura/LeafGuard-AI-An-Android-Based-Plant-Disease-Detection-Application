# Week 12 Progress Log

## 2026-07-16 - Real model end-to-end validation

### Completed

- Verified the moving upstream `main` URL still resolves to pinned commit
  `f6165bd93524dfb77a9629aae70db845832d1b01`.
- Recorded the 25,143,175-byte Keras artifact and SHA-256
  `08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f`.
- Confirmed all backend, Kotlin, and Java label files match the source README order.
- Upgraded the real backend runtime to TensorFlow 2.19.1 for Keras 3 compatibility.
- Inspected and converted the exact Keras artifact to identical Android TFLite assets.
- Passed 30/30 Keras/TFLite parity checks on real PlantVillage images.
- Measured 28/30 top-1 accuracy on the limited three-class tomato sample.
- Loaded the real FastAPI model with `USE_MOCK=false` and verified `/health` and `/predict`.
- Built and smoke-tested the real-model Docker image with the Keras artifact mounted
  read-only.
- Passed backend tests and model contract regression tests.
- Passed unit tests, lint, debug/release builds, release-model gates, and instrumentation
  APK compilation in both Android tracks.
- Verified every APK packages the exact converted TFLite hash.
- Audited the published `v0.2.0-beta` asset and confirmed it still contains the 77-byte
  placeholder rather than the verified real TFLite model.

### Remaining release gates

- Run Android online and offline inference on an attached emulator and physical device.
- Disable internet and capture evidence that offline inference remains functional.
- Execute connected instrumentation tests and add a real TFLite prediction assertion.
- Expand independent accuracy testing beyond three tomato classes to all 38 classes.
- Sign with the private production keystore and install that exact release APK.
- Replace or withdraw the stale `v0.2.0-beta` asset only after all manual gates pass,
  then publish the signed APK and checksum.

Full evidence: [`../../docs/evidence/week-12/model-validation-2026-07-16.md`](../../docs/evidence/week-12/model-validation-2026-07-16.md).

## 2026-07-16 - Production release process documented

- Created the controlling production runbook covering model approval, source freeze,
  backend hardening/deployment, Android device acceptance, signing, CI/CD, publication,
  monitoring, rollback, and final go/no-go criteria.
- Separated tasks an agent can execute from manual legal, secret, provider, agricultural,
  physical-device, and publication approvals.
- Recorded current production blockers, including the debug-signed stale beta, incomplete
  all-class evaluation, emulator-only default URL, backend readiness/security gaps, and
  missing production signing/device evidence.
- Added a release-record template binding the immutable artifact tuple, gate evidence,
  device matrix, security/privacy approvals, publication verification, and rollback plan.

Runbook: [`../../docs/PRODUCTION_RELEASE_RUNBOOK.md`](../../docs/PRODUCTION_RELEASE_RUNBOOK.md).
