# LeafGuard AI Production Release Runbook

This is the controlling process for releasing the complete LeafGuard production system:
the Kotlin Android app, offline TFLite model, real FastAPI cloud model, containerized
backend, signed APK, evidence, GitHub Release, monitoring, and rollback plan.

Use this runbook for Week 12 only after completing the Week 11 validation checklist.
The Kotlin app in `android-app-kotlin/` is the only production mobile artifact. Keep the
Java app in `android-app/` compiling as a reference, but do not publish two APKs with the
same application ID.

## 1. How to use this runbook

Each task has an owner:

- **MANUAL** - you must make the decision, enter a secret directly, use a physical
  device, approve legal text, or inspect a provider screen. An agent cannot certify it.
- **AGENT** - an agent can edit files, run commands, calculate hashes, inspect APKs,
  validate code, or prepare release notes.
- **BOTH** - the agent can prepare and test the technical work; you must review or
  perform the final external action.

Never mark an item complete from memory. Save its command output, screenshot, URL, hash,
or signed approval under `docs/evidence/week-12/`.

### 1.1 Prepare the release shell - BOTH

Run commands from the repository root. Set paths once for the current shell:

```bash
export REPO_ROOT="$PWD"
export ANDROID_SDK_ROOT="${ANDROID_SDK_ROOT:-$HOME/android-sdk}"
export PATH="$ANDROID_SDK_ROOT/platform-tools:$ANDROID_SDK_ROOT/build-tools/34.0.0:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$PATH"
export VALIDATION_IMAGE_DIR="/secure/path/to/approved-real-validation-images"
```

Verify the tools before beginning:

```bash
git --version
java -version
python3.11 --version
adb version
apksigner --version
apkanalyzer --version
docker version
gh --version
```

If `python3.11` is unavailable but Conda exists, create the verified runtime with:

```bash
conda create --prefix "$REPO_ROOT/.venv" \
  --channel conda-forge --override-channels python=3.11 --yes
```

Otherwise install Python 3.11 through the operating system or Python distribution you
trust. Android SDK tools may be under a different build-tools version; use the installed
version consistently and record it. `VALIDATION_IMAGE_DIR` is deliberately outside the
repository until image licenses and redistribution are approved.

## 2. Current release blockers

As of the 2026-07-16 validation, do not publish a production release yet:

- The limited real-image accuracy result is 28/30 across only three tomato classes.
  The model has not been independently evaluated across all 38 classes or field images.
- No Android device or emulator has run the real online/offline acceptance suite.
- No private production keystore has signed and installed the final APK.
- The published beta is debug-signed. A new production key cannot update it in place;
  existing beta users must uninstall it or the production app must use a new package ID.
- The app's default backend URL is still the emulator-only `http://10.0.2.2:8000`.
- The public backend has no application-level rate limiting or abuse protection.
- `/health` currently returns HTTP 200 even if the real model is unavailable and exposes
  internal model/label paths. Production readiness must fail closed and expose less detail.
- The backend container currently has no explicit non-root runtime user.
- Privacy policy, data-retention decision, support contact, and production disclaimer
  are not present as approved release documents.
- Only 10 of 38 classes have detailed reviewed in-app guidance; the remaining classes
  intentionally receive generic guidance and require an explicit product decision.
- Current CI builds debug APKs and mock-capable backend tests. It does not validate the
  real Keras model, produce a signed release, deploy the backend, or run device tests.
- The published `v0.2.0-beta` APK contains a 77-byte placeholder model. Do not reuse it.

The detailed baseline is
[`evidence/week-12/model-validation-2026-07-16.md`](evidence/week-12/model-validation-2026-07-16.md).

## 3. The immutable release tuple

Every approved release must record these values together:

| Item | Required value |
|---|---|
| Git commit | Full commit SHA used for the build |
| Git tag | For example `v1.0.0` |
| Android version | `versionCode` and `versionName` |
| Application ID | Final package ID; do not change it after public release |
| Keras model | Byte size and SHA-256 |
| TFLite model | Byte size and SHA-256 |
| Labels | Count and SHA-256 |
| Backend image | Registry name, immutable digest, and deployed revision |
| Backend URL | Final HTTPS base URL |
| APK | Filename, byte size, and SHA-256 |
| Signing identity | Certificate SHA-256 digest, never the private key |
| Test evidence | Device models, Android versions, date, and result report |

If one value changes, rerun every dependent gate. Never rebuild an APK after recording
its hash and still call it the same release artifact.

Copy [`../release-records/production-release-template.md`](../release-records/production-release-template.md)
to `release-records/vX.Y.Z-release-record.md` when the release starts. Fill it
incrementally; do not wait until publication day to reconstruct evidence.

## 4. Phase A - Product and legal decisions

### A1. Define the release claim - MANUAL

Choose one honest classification:

- **Course/demo release** - assistive educational classifier, not production diagnosis.
- **Pilot release** - restricted testers, monitored use, explicit known limitations.
- **Public production release** - requires independent all-class and field validation,
  privacy/legal review, support ownership, monitoring, and incident response.

With the current evidence, use course/demo or controlled pilot language. Do not claim
98.75% LeafGuard accuracy: that number is the source author's dataset result.

### A2. Decide distribution - MANUAL

- Direct/GitHub distribution requires a signed APK.
- Google Play requires an Android App Bundle (`.aab`) for normal publication; also keep
  a signed APK for direct installation testing.
- Decide whether `com.leafguard` is the permanent application ID. Changing it later
  creates a different app and breaks updates.
- Reserve `versionName` (recommended first stable value: `1.0.0`) and a monotonically
  increasing `versionCode` (greater than the current value `2`). Never reuse either
  artifact identity for different bytes.

### A3. Approve legal and user-facing documents - BOTH

Create and personally approve:

- `PRIVACY.md`: camera/gallery access, local Room history, cloud image upload, whether
  uploads are stored, retention period, deletion process, contact, and third parties.
- `TERMS.md` or a short acceptable-use statement.
- `SECURITY.md`: vulnerability-reporting contact and supported versions.
- Model attribution and MIT notice from the source model repository.
- PlantVillage image/data citation and license for any distributed evaluation assets.
- A decision to provide expert-reviewed detailed guidance for all 38 classes or clearly
  retain generic guidance for the unsupported 28 classes.
- In-app and release-note disclaimer: predictions are assistive, not confirmed diagnosis;
  users should consult an agricultural expert before treatment.
- Google Play Data safety and content declarations if using Play.

**Gate A:** stop unless the release classification, package ID, version, distribution
channel, privacy policy, model license, support contact, and disclaimer are approved.

## 5. Phase B - Freeze source and dependencies

### B1. Prepare a release branch - MANUAL

Start from a clean, reviewed `main`:

```bash
git switch main
git pull --ff-only origin main
git status --short
git switch -c release/v1.0.0
```

Do not include `.env`, keystores, passwords, model binaries, generated APKs, or local
provider credentials in Git.

### B2. Update production metadata - AGENT

In `android-app-kotlin/app/build.gradle`:

- increment `versionCode`;
- set the final `versionName`;
- review `minSdk`, `targetSdk`, and `compileSdk` requirements;
- decide whether release shrinking/obfuscation is enabled and test it if changed.

Mirror source behavior in `android-app/` only to keep the reference track synchronized.

### B3. Make the backend URL production-safe - AGENT

The final Kotlin release must default to the deployed HTTPS endpoint, not
`http://10.0.2.2:8000`. Prefer a release-specific `BuildConfig` value or resource over
editing a runtime preference by hand on every device. Keep the local URL only in debug.

Required behavior:

- debug default: `http://10.0.2.2:8000/`;
- release default: `https://YOUR_PRODUCTION_HOST/`;
- release accepts HTTPS only;
- the URL ends with `/` before Retrofit receives it;
- Settings cannot silently reset a production build to the emulator URL.

The existing network security configuration already blocks cleartext except local
development hosts. Do not add the production host to a cleartext exception.

### B4. Lock dependencies - BOTH

- Run dependency and vulnerability review for Gradle, Python, Docker base image, and
  GitHub Actions.
- Keep Python 3.11 and `tensorflow==2.19.1` for this Keras 3 artifact unless a full
  model validation proves another runtime.
- Do not update Android TFLite, TensorFlow, AGP, Gradle, or target SDK during the final
  release build without rerunning conversion, parity, builds, lint, and device tests.
- Record accepted vulnerabilities and mitigations; block known exploitable critical or
  high-severity issues.

Useful agent-run checks include repository secret scanning, dependency reports, Docker
image scanning, Android lint, Python package audit, and workflow permission review.

**Gate B:** source is frozen, metadata is final, no secrets are tracked, the release app
defaults to HTTPS, dependency findings are resolved or formally accepted, and CI is green.

## 6. Phase C - Validate and freeze the model

### C1. Reproduce provenance - AGENT

From the repository root, use the pinned source URL in
`release-records/model-provenance.txt`, then verify:

```bash
stat -c '%n %s bytes' backend-api/models/leafguard_model.keras
sha256sum backend-api/models/leafguard_model.keras
cmp model/labels-38.txt backend-api/labels-38.txt
cmp model/labels-38.txt android-app-kotlin/app/src/main/assets/labels.txt
cmp model/labels-38.txt android-app/app/src/main/assets/labels.txt
```

Expected approved Keras SHA-256:
`08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f`.

### C2. Inspect and convert - AGENT

```bash
python3.11 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
python -m pip install -r backend-api/requirements.txt
python model/inspect_model.py
python model/convert_model.py
python model/inspect_model.py \
  --tflite-model android-app-kotlin/app/src/main/assets/model.tflite
```

If the Conda fallback created `.venv`, activate it with
`source "$REPO_ROOT/.venv/bin/activate"` and continue with the same `python` commands.

Then record both TFLite hashes and prove the two tracks match:

```bash
sha256sum android-app-kotlin/app/src/main/assets/model.tflite
sha256sum android-app/app/src/main/assets/model.tflite
cmp android-app-kotlin/app/src/main/assets/model.tflite \
    android-app/app/src/main/assets/model.tflite
```

### C3. Run parity and accuracy as separate tests - BOTH

Parity proves conversion equivalence. Accuracy measures whether the label is correct.
Never use one as evidence for the other.

```bash
python model/parity_test.py "$VALIDATION_IMAGE_DIR"/*.jpg \
  --tflite-model android-app-kotlin/app/src/main/assets/model.tflite
```

For public production, build an independent, source-documented, leaf-group-separated
evaluation set with real field images covering all 38 classes, plus:

- non-leaf images;
- drawings/screenshots;
- blur, darkness, glare, occlusion, and multiple leaves;
- camera images from low-, mid-, and high-end phones;
- crops and conditions from the intended deployment region.

Record per-class precision, recall, F1, confusion matrix, top-1 accuracy, calibration,
unknown/out-of-distribution behavior, and confidence-threshold selection. Have an
agricultural expert review labels and treatment text. The current 28/30 three-class
result is useful integration evidence but is not a public-production accuracy study.

### C4. Freeze the model contract - BOTH

Record the approved Keras/TFLite/label hashes in:

- `release-records/model-provenance.txt`;
- a dated file under `docs/evidence/week-12/`;
- release notes;
- backend deployment metadata.

**Gate C:** provenance, licensing, label order, inspection, conversion, parity, all-class
evaluation, threshold choice, out-of-distribution behavior, and expert review pass.

## 7. Phase D - Harden and deploy the backend

### D1. Resolve public API security - BOTH

Before exposing `/predict` publicly:

- put the service behind provider/WAF rate limits by IP/device and request size;
- set concurrency, CPU, memory, and request timeout limits;
- use HTTPS only and redirect or reject HTTP;
- restrict ingress to the intended load balancer where supported;
- keep `MAX_IMAGE_SIZE_BYTES` bounded;
- do not use a static secret embedded in the APK as the sole control; APK secrets can
  be extracted;
- decide whether the API is intentionally anonymous or requires real user/device auth;
- add structured request IDs and sanitized error logs;
- make startup or readiness return non-200 when `USE_MOCK=false` and the model is not
  loaded; keep a separate minimal liveness response if the provider requires one;
- do not expose filesystem model paths, label paths, stack traces, or configuration in
  the public health response;
- never log multipart bodies or retain uploaded images without explicit consent;
- configure CORS only for actual browser origins. CORS does not secure Android clients;
- scan the image and dependencies, run as non-root where supported, and use read-only
  filesystems/mounts where practical.

For a course/demo endpoint, provider rate limiting plus strict quotas may be sufficient.
For a commercial service, design proper account/device authentication, revocation, abuse
response, and cost controls before launch.

### D2. Build the real-model image - AGENT

The Keras file is excluded from the Docker context intentionally. Build code and runtime,
then mount or fetch the exact approved model through secure provider storage:

```bash
docker build --build-arg INSTALL_TENSORFLOW=true \
  -t YOUR_REGISTRY/leafguard-api:1.0.0 backend-api

docker run --rm -p 8000:8000 \
  -e USE_MOCK=false \
  -e MODEL_PATH=/models/leafguard_model.keras \
  -e CONFIDENCE_THRESHOLD=0.50 \
  -e MAX_IMAGE_SIZE_BYTES=10485760 \
  -e ALLOWED_ORIGINS=https://YOUR_ADMIN_ORIGIN \
  -v /secure/model/location:/models:ro \
  YOUR_REGISTRY/leafguard-api:1.0.0
```

Verify locally:

```bash
curl --fail http://127.0.0.1:8000/health
curl --fail -F image=@"$VALIDATION_IMAGE_DIR/known-leaf.jpg" \
  http://127.0.0.1:8000/predict
```

### D3. Publish by digest - MANUAL

1. Push the image to a private or controlled registry.
2. Record the immutable `sha256:` image digest.
3. Deploy that digest, not a mutable `latest` tag.
4. Mount the exact approved Keras artifact read-only.
5. Set `USE_MOCK=false`, `MODEL_PATH`, limits, and provider-supplied `PORT`.
6. Allocate enough memory for TensorFlow and configure autoscaling conservatively.
7. Configure `/health` startup/readiness/liveness checks.
8. Attach the custom domain and valid TLS certificate.
9. Enable provider logs, metrics, alerts, budget alerts, quotas, and backups.
10. Keep the previous known-good image digest available for rollback.

### D4. Production backend acceptance - BOTH

Verify from outside the hosting provider:

- `/health` returns 200, `use_mock=false`, `model_loaded=true`, `class_count=38`;
- valid JPEG and PNG prediction requests work;
- invalid MIME type, spoofed image, empty file, and oversized upload are rejected;
- timeout, concurrency, and rate limits behave as designed;
- no uploaded image or sensitive header appears in logs;
- p50/p95/p99 latency and memory usage are within the release budget;
- restart and scale-out preserve behavior;
- rollback to the prior image works;
- TLS and certificate-chain checks pass.

**Gate D:** the immutable backend revision is healthy over HTTPS, protected from basic
abuse, monitored, budgeted, tested, and rollback-ready. Save its URL and digest.

## 8. Phase E - Build and test the Android release candidate

### E1. Run static and local automated checks - AGENT

From the repository root:

```bash
cd android-app-kotlin
./gradlew --no-daemon clean \
  testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest assembleRelease
cd ../android-app
./gradlew --no-daemon clean \
  testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest assembleRelease
cd ..
```

Also run backend and model tests:

```bash
cd model
../.venv/bin/python -m unittest -v test_model_contract.py
cd ../backend-api
../.venv/bin/python -m pip install -r requirements-dev.txt
USE_MOCK=false ../.venv/bin/python -m unittest -v test_api.py
cd ..
```

The current CI does not run the real model because model binaries are ignored. Before
production, add a protected release workflow that downloads the pinned Keras artifact,
verifies its expected SHA-256, converts it, runs parity/backend/release builds, and uploads
only unsigned or safely signed artifacts according to your secret-management design.

### E2. Create the device test matrix - MANUAL

Minimum matrix:

- API 24, the minimum supported Android version;
- one recent target Android/API version;
- one low-memory or low-end physical phone;
- one mid-range physical phone;
- emulator plus physical device;
- Wi-Fi, mobile data, slow/intermittent network, and no network.

Record manufacturer, model, Android version, ABI, RAM class, app version, test date,
and tester.

### E3. Run connected tests - BOTH

With a device/emulator attached:

```bash
adb devices -l
cd android-app-kotlin
./gradlew connectedDebugAndroidTest
```

Add an instrumentation test that opens the packaged real `model.tflite`, classifies a
known bundled test fixture, and asserts the expected tensor/label behavior. Existing UI
tests compiling successfully is not proof that TFLite runs on Android.

### E4. Manual functional acceptance - MANUAL

On every required device, test and capture evidence for:

1. Clean install and first launch.
2. App icon, version, navigation, rotation, background/restore, and process restart.
3. Camera permission allow, deny, deny permanently, and retry.
4. Camera capture and gallery selection for JPEG/PNG and large images.
5. Cloud prediction through the production HTTPS URL.
6. Offline prediction with airplane mode enabled and backend unreachable.
7. Same-image online/offline label and confidence comparison.
8. Low-confidence/uncertain result behavior.
9. A class with detailed guidance and a class without reviewed guidance.
10. History insert, list, detail, delete, persistence, and upgrade retention.
11. Analytics summary, disease search/library, sharing, and notifications.
12. Invalid backend URL, server 4xx/5xx, timeout, and recovery.
13. Accessibility: TalkBack, font scaling, contrast, touch targets, and content labels.
14. No crash, ANR, unresolved LeakCanary finding, or sensitive Logcat output.
15. Offline inference timing, cloud timing, startup timing, CPU, and memory.

Run at least the documented 30-image set in both modes and retain exact image IDs and
results. For public production, use the larger all-class acceptance set from Gate C.

### E5. Upgrade and uninstall behavior - MANUAL

- Install the last public beta, create history, then upgrade to the release candidate.
- The current beta is debug-signed, so a production-key APK with `com.leafguard` cannot
  update it in place. Test and document the required uninstall/reinstall path and local
  data loss, or choose a new permanent production application ID before launch. Never
  sign production with a shared/debug key merely to preserve beta upgrade compatibility.
- For future production-to-production updates, confirm package/signing continuity and
  Room data retention. Add explicit Room migrations and migration tests before changing
  the schema version; never use destructive fallback for user history.
- Confirm uninstall/reinstall behavior matches the privacy policy and Android backup
  setting. The manifest currently permits backup; explicitly decide whether that is
  acceptable for scan history and image URIs.

**Gate E:** automated checks, connected tests, device matrix, offline and cloud flows,
upgrade, accessibility, performance, privacy, and failure recovery all pass.

## 9. Phase F - Create and protect the signing identity

### F1. Create the production keystore - MANUAL

Use Android Studio **Build > Generate Signed Bundle / APK** or `keytool`. Type all
passwords directly into your terminal or Android Studio. Never send them to an agent,
chat, issue, commit, shell history, or screenshot.

Example structure; choose your own alias, identity, and secure password interactively:

```bash
keytool -genkeypair -v \
  -keystore /secure/offline/location/leafguard-release.jks \
  -alias leafguard-production \
  -keyalg RSA -keysize 4096 -validity 10000
```

Back up the keystore in at least two encrypted locations. Store passwords separately.
Losing the key prevents updates; exposing it permits malicious signed updates.

### F2. Record only the public certificate - AGENT

```bash
keytool -list -v \
  -keystore /secure/offline/location/leafguard-release.jks \
  -alias leafguard-production
```

Record the certificate SHA-256 digest in the release record. Do not record private-key
material or passwords.

### F3. Configure signing safely - BOTH

Either sign through Android Studio or reference credentials from environment variables,
an untracked local properties file, or a CI secret manager. Do not hardcode credentials
in `build.gradle`.

For GitHub Actions, the repository owner must configure protected environment secrets
and required approval. An agent may write the workflow, but only you may upload the
keystore and secret values through GitHub's settings UI. Restrict workflow permissions,
pin trusted actions, prevent untrusted pull requests from accessing signing secrets, and
retain signed artifacts for the minimum necessary period.

## 10. Phase G - Build, sign, and verify the final APK

### G1. Build once from the frozen commit - BOTH

```bash
git status --short
git rev-parse HEAD
cd android-app-kotlin
./gradlew --no-daemon clean assembleRelease
```

Sign the release APK using Android Studio or `apksigner`. Do not pass real passwords on
the command line where they can enter shell history; use Android Studio, protected stdin,
or your secure CI secret integration.

If publishing to Play, also build and sign the release bundle:

```bash
./gradlew --no-daemon bundleRelease
```

### G2. Verify cryptographic and package identity - AGENT

```bash
apksigner verify --verbose --print-certs LeafGuard-AI-v1.0.0.apk
sha256sum LeafGuard-AI-v1.0.0.apk
stat -c '%n %s bytes' LeafGuard-AI-v1.0.0.apk
```

For API 24+, APK Signature Scheme v2 is sufficient. Verify that the certificate digest
matches Gate F, the package/version are correct, and the APK contains the approved model:

```bash
apkanalyzer manifest application-id LeafGuard-AI-v1.0.0.apk
apkanalyzer manifest version-name LeafGuard-AI-v1.0.0.apk
apkanalyzer manifest version-code LeafGuard-AI-v1.0.0.apk
unzip -p LeafGuard-AI-v1.0.0.apk assets/model.tflite | sha256sum
unzip -p LeafGuard-AI-v1.0.0.apk assets/labels.txt | sha256sum
```

The embedded hashes must equal Gate C. Check that no `.env`, keystore, private URL token,
debug certificate, test fixture, or unexpected model is packaged.

### G3. Install the exact hashed artifact - MANUAL

```bash
adb install -r LeafGuard-AI-v1.0.0.apk
```

Repeat the critical smoke test using this exact file, not an Android Studio run build:
launch, camera/gallery, one cloud prediction, airplane-mode offline prediction, history,
share, restart, and version display. Save installation output and screenshots.

**Gate G:** the frozen commit produced one signed APK, all hashes/certificate/package
metadata match, and that exact APK installs and passes the final device smoke test.

## 11. Phase H - CI/CD production pipeline

Keep pull-request validation separate from protected release/deployment jobs.

### H1. Pull-request pipeline - AGENT

Required on every PR:

- Kotlin and Java unit tests, lint, debug build, and instrumentation APK compilation;
- backend compile and tests;
- model contract unit tests that do not require the private binary;
- dependency, secret, workflow, and container scans;
- no signing or deployment secrets.

### H2. Protected release pipeline - BOTH

Trigger only from a reviewed version tag or manual protected environment:

1. Check out the exact tag.
2. Set up JDK 17 and Python 3.11.
3. Download the pinned Keras artifact from approved storage.
4. Verify size and SHA-256 before loading it.
5. Inspect, convert, compare labels/models, and run parity.
6. Run backend real-model tests.
7. Build the real backend image and scan it.
8. Build unsigned Android release APK/AAB and validate embedded hashes.
9. Obtain signing secrets only after environment approval.
10. Sign and verify; emit APK/AAB, checksums, SBOMs, and test report.
11. Never deploy automatically if device/manual gates are incomplete.

GitHub Secrets cannot sensibly store a 25 MB model. Use authenticated object storage,
a controlled model registry, Git LFS with reviewed redistribution policy, or the pinned
public source plus mandatory hash verification.

### H3. Protected deployment pipeline - BOTH

1. Deploy the backend image by digest to staging.
2. Run health, prediction, abuse, and rollback smoke tests.
3. Require manual approval.
4. Deploy the same digest to production using canary/blue-green rollout.
5. Monitor errors, latency, restarts, and cost.
6. Promote or roll back automatically against defined thresholds.
7. Publish the mobile artifact only after the production backend is stable.

**Gate H:** branch protection, required checks, environment approvals, least-privilege
secrets, immutable artifacts, staging, production promotion, and rollback are tested.

## 12. Phase I - Publish the release

### I1. Prepare release records - AGENT

Create:

- final APK and optional AAB;
- `.sha256` checksum file;
- release notes with release tuple;
- model/backend/device validation summary;
- privacy, terms, security, attribution, and installation links;
- known limitations and rollback/support instructions;
- SBOMs if produced by the pipeline.

Do not replace the stale `v0.2.0-beta` asset with different bytes under the same identity.
Add a warning to that prerelease, then publish a new tag such as `v1.0.0`.

### I2. Create an annotated tag - MANUAL

Only after all gates pass:

```bash
git switch main
git merge --ff-only release/v1.0.0
git tag -a v1.0.0 -m 'LeafGuard AI v1.0.0'
git push origin main
git push origin v1.0.0
```

If `main` cannot fast-forward, stop, review the divergence, merge through the normal PR
process, rerun affected gates, and tag the reviewed release commit.

### I3. Publish GitHub Release or Play release - BOTH

For GitHub, an agent may prepare the draft and commands; you must review and publish:

```bash
gh release create v1.0.0 \
  LeafGuard-AI-v1.0.0.apk \
  LeafGuard-AI-v1.0.0.apk.sha256 \
  --title 'LeafGuard AI v1.0.0' \
  --notes-file release-records/v1.0.0-release-notes.md \
  --draft
```

For Google Play:

- create the app with the final application ID;
- configure Play App Signing and protect the upload key;
- complete store listing, screenshots, category, content rating, privacy URL, Data safety,
  target audience, ads declaration, and app-access declaration;
- upload the signed AAB to internal testing first;
- promote through closed/open testing before production;
- use staged rollout, not immediate 100% publication.

### I4. Verify the published artifact - BOTH

Download from the actual release channel onto a clean machine/device, then repeat:

```bash
sha256sum LeafGuard-AI-v1.0.0.apk
apksigner verify --verbose --print-certs LeafGuard-AI-v1.0.0.apk
adb install -r LeafGuard-AI-v1.0.0.apk
```

The downloaded hash must equal Gate G. Complete one cloud and one offline prediction.

**Gate I:** the public artifact is the exact approved artifact, release notes are honest,
links work, the backend is healthy, and clean installation succeeds.

## 13. Phase J - Rollout, monitoring, and rollback

### J1. Monitor the first release window - MANUAL

For at least the first 24-72 hours, watch:

- backend availability, 4xx/5xx rate, p95 latency, memory, restarts, and cost;
- rate-limit events and abuse patterns;
- Android crash/ANR reports and user support reports;
- cloud/offline disagreement reports and high-confidence wrong predictions;
- certificate expiry and domain health.

Do not collect user images or diagnosis history as telemetry unless the privacy policy,
consent, retention, security, and deletion mechanism explicitly cover it.

### J2. Define rollback triggers before launch - BOTH

Examples:

- real model fails to load or `/health` reports mock mode;
- sustained elevated 5xx/timeout rate;
- crash/ANR regression;
- incorrect model/labels packaged;
- signing or supply-chain compromise;
- serious high-confidence misclassification pattern;
- privacy/security incident or unexpected image retention;
- hosting cost/abuse exceeds limits.

### J3. Roll back safely - MANUAL

- Backend: redeploy the previous immutable image digest and verify `/health` plus a known
  prediction.
- Android: halt staged rollout; publish a higher `versionCode` corrective release. You
  cannot remotely recall an already installed APK.
- Security: revoke affected credentials, preserve incident evidence, notify users where
  required, and rotate secrets.
- Model: restore the previous approved Keras/TFLite pair; never change cloud only and
  still claim online/offline parity.

Record every rollback decision, timestamp, owner, impact, and verification result.

## 14. Final go/no-go checklist

All boxes must be complete for a production release:

### Product, legal, and support

- [ ] Release classification and claims approved
- [ ] Permanent application ID and version approved
- [ ] Privacy, terms, security contact, attribution, and disclaimer published
- [ ] Support owner and incident owner assigned

### Model

- [ ] Keras provenance, license, byte size, and SHA-256 recorded
- [ ] All labels and both Android TFLite files match
- [ ] Inspection, conversion, parity, all-class accuracy, OOD, and threshold gates pass
- [ ] Agricultural expert reviewed labels and treatment guidance

### Backend

- [ ] Real model deployed by immutable container digest with `USE_MOCK=false`
- [ ] HTTPS, rate limits, quotas, timeouts, logging, monitoring, alerts, and budgets pass
- [ ] Health, inference, negative, load, restart, and rollback tests pass
- [ ] Production URL is stable and documented

### Android

- [ ] Release defaults to production HTTPS and contains no secrets
- [ ] Unit tests, lint, release build, model gate, and connected tests pass
- [ ] Minimum/recent API and physical-device matrix passes
- [ ] Cloud, airplane-mode offline, same-image comparison, and failure recovery pass
- [ ] Accessibility, performance, upgrade, backup, and privacy behavior pass

### Signing and artifacts

- [ ] Production keystore is backed up and certificate digest recorded
- [ ] APK/AAB signature, package, version, embedded model, labels, size, and hash pass
- [ ] Exact final APK installs and passes smoke tests
- [ ] No secret, debug certificate, placeholder model, or unapproved asset is packaged

### Pipeline and publication

- [ ] PR, release, deployment, staging, approval, and rollback pipelines pass
- [ ] Release commit and tag identify the immutable release tuple
- [ ] Stale beta warning is visible and a new release identity is used
- [ ] Published artifact re-download matches the approved SHA-256
- [ ] Staged rollout monitoring and rollback owners are active

Any unchecked box is a **NO-GO**. Record the blocker in
`roadmap/week-12-final-submission/progress-log.md`; do not weaken the checklist to make
the release appear complete.

## 15. What an agent can do next

An agent can safely help with:

- production/debug backend URL build configuration;
- version updates and release metadata;
- real-model Android instrumentation tests;
- API rate limiting, request IDs, non-root Docker hardening, and tests;
- privacy/security document drafts for your review;
- all-class evaluation scripts and result tables after you supply approved images;
- protected release/deployment workflow templates without secret values;
- APK/AAB build, package inspection, checksums, SBOMs, and draft release notes;
- stale beta release-note warning and a new draft GitHub Release after all gates pass.

You must personally handle:

- legal/privacy/license approval and diagnostic claims;
- agricultural expert validation and field-image ground truth;
- production domain/provider/billing decisions;
- secret entry, keystore creation, backups, and GitHub/provider secret configuration;
- physical-device, airplane-mode, accessibility, and final installation evidence;
- production deployment approval, Play declarations, staged rollout, and release publish;
- incident response and rollback approval.

After each completed phase, update the Week 12 validation checklist, save evidence under
`docs/evidence/week-12/`, and update the progress log before proceeding.
