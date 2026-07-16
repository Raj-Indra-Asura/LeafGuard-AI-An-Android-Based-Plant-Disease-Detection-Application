# LeafGuard AI Backend Deployment and Maintenance

This guide explains how to keep Cloud mode reachable from an Android phone. It complements the local setup in [complete-setup-and-run-guide.md](complete-setup-and-run-guide.md).

For a production release, follow the gated
[`Production Release Runbook`](PRODUCTION_RELEASE_RUNBOOK.md). It adds required
readiness behavior, HTTPS, rate limiting, non-root container hardening, monitoring,
immutable image deployment, Android signing, and rollback gates that this maintenance
overview does not fully implement.

## 1. Choose the operating mode honestly

The API exposes its current mode at both `GET /` and `GET /health`:

- `model_loaded: true`, `use_mock: false` — a real Keras model is loaded.
- `model_loaded: false`, `use_mock: true` — demo/mock predictions only.

The trained model binary is intentionally ignored by Git. The approved local artifact,
when staged, is `backend-api/models/leafguard_model.keras`; verify its provenance and
hash before every production build. Do not describe mock responses as diagnostic AI.

## 2. Local development

From the repository root:

```bash
cd backend-api
python3 -m venv venv
source venv/bin/activate
pip install -r requirements-base.txt
USE_MOCK=true uvicorn main:app --host 0.0.0.0 --port 8000
```

Verify `http://localhost:8000/health` and submit a sample image to `POST /predict`. The Android emulator can use `http://10.0.2.2:8000`. A release installed on a physical phone should use a deployed HTTPS endpoint instead of a computer's temporary LAN address.

For the approved Keras 3 artifact, use the verified Python 3.11 and TensorFlow 2.19.1
runtime from `requirements.txt`, place the model at
`backend-api/models/leafguard_model.keras`, and set `USE_MOCK=false`.

## 3. Container deployment

A deployment-ready [Dockerfile](../backend-api/Dockerfile) is included.

### Demo/mock image

```bash
docker build -t leafguard-api ./backend-api
docker run --rm -p 8000:8000 -e USE_MOCK=true leafguard-api
```

### Real-model image

```bash
docker build --build-arg INSTALL_TENSORFLOW=true -t leafguard-api ./backend-api
docker run --rm -p 8000:8000 \
  -e USE_MOCK=false \
  -e MODEL_PATH=/models/leafguard_model.keras \
  -v /secure/model/location:/models:ro \
  leafguard-api
```

Keep model binaries and secrets outside Git. Use the hosting provider's encrypted secret and persistent-storage features.

## 4. Deploy to a managed host

Render, Railway, Fly.io, Google Cloud Run, Azure Container Apps, and similar container services can run the included Dockerfile. Provider screens change, but the reliable process is the same:

1. Create a new web/container service from this GitHub repository.
2. Set the Docker build context to `backend-api`.
3. Allow the provider to supply `PORT`; the container reads it automatically.
4. For the current demo backend, set `USE_MOCK=true`.
5. For real inference, provide a validated model, set `MODEL_PATH`, set `USE_MOCK=false`, and allocate enough memory for TensorFlow.
6. Configure the health check path as `/health`.
7. Require HTTPS and note the final base URL, such as `https://leafguard-api.example.com/`.
8. Open that URL in a browser and confirm `status: ok` before configuring the app.

In the Android app, open **About**, replace the backend URL with the deployed HTTPS base URL, then use Cloud mode.

## 5. Environment variables

| Variable | Recommended production value | Purpose |
|---|---|---|
| `PORT` | Supplied by host | Listening port |
| `USE_MOCK` | `false` only when a real model is loaded | Selects demo versus real inference |
| `MODEL_PATH` | Secure mounted model path | Keras model location |
| `IMAGE_SIZE` | Must match training, currently `224` | Input width and height |
| `CONFIDENCE_THRESHOLD` | Validated model threshold | Low-confidence logging threshold |
| `MAX_IMAGE_SIZE_BYTES` | `10485760` or lower | Upload memory/abuse limit |
| `ALLOWED_ORIGINS` | Explicit web origins if a browser client exists | Browser CORS policy |

Native Android requests are not governed by browser CORS. If no browser frontend uses this API, use an explicit internal/admin origin rather than relying on `*`.

## 6. Maintenance schedule

### Every deployment

1. Run backend tests: `python -m unittest test_api.py`.
2. Run both Android unit-test and lint tasks.
3. Confirm `/health` reports the intended model mode.
4. Submit one known image per supported class and record results.
5. Install the APK on at least one API 24 device and one recent Android device.
6. Verify camera, gallery, Offline, Cloud, result sharing, history, analytics, library, and settings.
7. Record the app version, backend commit, model checksum, and test date.

### Weekly

- Check service uptime, error rate, latency, memory, CPU, and storage.
- Review HTTP 4xx/5xx logs without retaining uploaded plant images.
- Confirm TLS certificate validity and `/health` availability.
- Check hosting cost/usage limits and backup status.
- Review provider rate-limit and request-size controls to prevent abuse.

### Monthly

- Review Python and Android dependency updates.
- Re-run the model validation set and compare per-class metrics.
- Rotate operational credentials according to provider policy.
- Test restore/redeploy from a clean environment.

## 7. Monitoring and rollback

Use the hosting provider's health checks against `/health`. Alert when the endpoint fails repeatedly, latency rises, or the process restarts. Keep the previous working container image and APK release available. If a deployment fails validation, roll the backend back first; the app's default Offline mode remains available.

Do not log request bodies or persist uploaded images unless users explicitly consent and a retention policy exists. The Android release build logs only basic request metadata, not HTTP bodies.

## 8. Release policy

Use semantic versions:

- Patch, for example `0.2.1`: bug fixes, no contract change.
- Minor, for example `0.3.0`: backward-compatible features.
- Major, for example `1.0.0`: production model validated and public contract stabilized.

Each GitHub release should state:

- Whether the APK is debug-signed or production-signed.
- Whether Offline and Cloud prediction use a real model or demo fallback.
- Minimum Android version.
- Backend URL/setup requirement.
- Checks performed and known limitations.

Never publish a production APK if the bundled `model.tflite` is still the text placeholder. A beta/demo APK may be published only when that limitation is prominent in the release notes.
