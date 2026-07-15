# Production End-to-End Setup and Acceptance Guide

This guide builds the Kotlin app (primary track), the Java twin, and the FastAPI service
from the same approved Keras model. A release is not ready until every acceptance item
below is completed on your own machine and device.

## 1. Manually approve and stage the model

1. Review the source repository and its `LICENSE`:
   `https://github.com/Muhammad-Hassan12/Plant-Disease-Detector`
2. Download the exact pinned artifact:
   `https://raw.githubusercontent.com/Muhammad-Hassan12/Plant-Disease-Detector/f6165bd93524dfb77a9629aae70db845832d1b01/Models/model_4_mobilenet_finetuned.keras`
3. Save it as `backend-api/models/leafguard_model.keras`.
4. Confirm `model/labels-38.txt` matches the published class order. Do not sort it.
5. Record the source URL, commit, download date, license decision, file size, and hash:

   ```bash
   sha256sum backend-api/models/leafguard_model.keras
   ```

Do not commit the model unless you intentionally establish a Git LFS and redistribution
policy. The repository ignores generated model artifacts.

## 2. Prepare Python and inspect the contract

Use Python 3.10 or 3.11 because the pinned TensorFlow runtime does not support every
newer Python release.

```bash
python3.11 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
python -m pip install -r backend-api/requirements.txt
python model/inspect_model.py
```

The inspection must report one `[1, 224, 224, 3]` float32 input, one `[1, 38]`
float32 output, 38 labels, and embedded `[0,255]` to `[-1,1]` rescaling.

## 3. Convert the exact Keras model for offline use

```bash
python model/convert_model.py
cmp android-app/app/src/main/assets/model.tflite \
    android-app-kotlin/app/src/main/assets/model.tflite
cmp model/labels-38.txt android-app/app/src/main/assets/labels.txt
cmp model/labels-38.txt android-app-kotlin/app/src/main/assets/labels.txt
```

The conversion utility refuses an incompatible model and writes identical TFLite
artifacts to both app tracks.

## 4. Validate model parity

Collect at least six known test images: tomato early blight, tomato healthy, potato
late blight, corn gray leaf spot, apple scab, and a blurry/non-leaf image.

```bash
python model/validate_tflite.py test-images/tomato-early-blight.jpg \
  --model android-app-kotlin/app/src/main/assets/model.tflite
python model/parity_test.py test-images/*.jpg \
  --tflite-model android-app-kotlin/app/src/main/assets/model.tflite
```

Parity requires the same top class and a confidence difference no greater than 0.02 by
default. Keep a table containing filename, known condition, Keras result, TFLite result,
confidence values, pass/fail, and notes.

## 5. Run and verify the backend

```bash
cd backend-api
USE_MOCK=false python -m uvicorn main:app --host 0.0.0.0 --port 8000
```

In another terminal:

```bash
curl http://127.0.0.1:8000/health
curl -X POST http://127.0.0.1:8000/predict \
  -F image=@../test-images/tomato-early-blight.jpg
```

Required health values:

```json
{"use_mock": false, "model_loaded": true, "class_count": 38}
```

Without a valid real model, `/predict` returns HTTP 503 instead of presenting mock
inference as real. Mock mode is available only when explicitly started with
`USE_MOCK=true`.

For another computer or phone to connect, bind to `0.0.0.0`, allow TCP port 8000 in
the host firewall, and enter `http://HOST_LAN_IP:8000/` in LeafGuard Settings. Use an
HTTPS reverse proxy and restricted `ALLOWED_ORIGINS` for internet deployment.

## 6. Build and test Android

Install Android Studio with Android SDK 34 and JDK 17. The Kotlin app is the primary
release target.

```bash
cd android-app-kotlin
./gradlew testDebugUnitTest lintDebug assembleDebug
./gradlew assembleRelease
```

Repeat in `android-app/` if you intend to distribute the Java twin. Release builds fail
early when the real model is missing or the label asset is not exactly 38 unique labels.

Install the debug APK:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

On an emulator, the default backend URL `http://10.0.2.2:8000/` reaches the host.
On a physical device, configure the host LAN URL in Settings.

For every known image:

1. Select cloud mode and record the result.
2. Select offline mode with the same image and record the result.
3. Confirm top labels agree.
4. Confirm confidence is reasonably close.
5. Confirm one of the 28 non-library classes shows neutral “guidance unavailable” text.
6. Raise the confidence threshold and confirm an uncertain/retake state appears.
7. Remove or corrupt `model.tflite` in a test build and confirm offline inference fails
   clearly rather than returning a heuristic diagnosis.

Also test camera denial, invalid backend URL, backend unavailable, large upload, invalid
image, rotation, process restart, and at least one supported Android 7+ physical device.

## 7. Sign and release the APK manually

Never commit a keystore or passwords. In Android Studio choose **Build → Generate Signed
Bundle / APK**, select APK, create or select your private release keystore, and build the
`release` variant. Back up the keystore securely; losing it prevents compatible updates.

Before publishing:

```bash
apksigner verify --verbose --print-certs app-release.apk
sha256sum app-release.apk
adb install -r app-release.apk
```

Create a GitHub Release from the repository’s **Releases → Draft a new release** page.
Use a version tag matching the app version, attach the signed APK and checksum, and list:

- backend/model compatibility and 38-class label contract;
- model source, pinned commit, license attribution, and SHA-256;
- tested Android devices/versions;
- known PlantVillage/generalization limitations;
- statement that predictions are assistive, not confirmed diagnoses;
- only 10 classes have detailed in-app guidance.

## 8. Release gate

- [ ] Model source and license personally approved
- [ ] Model SHA-256 recorded
- [ ] Inspection and conversion pass
- [ ] Keras/TFLite parity passes on all acceptance images
- [ ] `/health` reports real model loaded, mock false, 38 classes
- [ ] Online and offline tests pass on a physical device
- [ ] Uncertain and unsupported-guidance states verified
- [ ] Agricultural text personally reviewed
- [ ] Signed APK signature and checksum verified
- [ ] APK installed from the exact release artifact
- [ ] Disclaimers and limitations included in release notes

No code change can guarantee diagnostic accuracy. Release approval depends on your
recorded real-image results and manual review.
