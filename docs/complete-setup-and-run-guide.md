# LeafGuard AI — Complete Setup & Run Guide (End-to-End)

This is the single document to follow when you want to **set up the whole system from a
fresh machine and run the complete app** — every screen, both detection modes (offline
on-device AI and cloud API), history database, disease library, and settings.

It complements the learning roadmap; it does not replace it. If you are following the
12-week course, keep using [`../roadmap/`](../roadmap/) and use this guide as the
authoritative "how do I run everything right now?" reference.

---

## Table of Contents

1. [What "fully functional" means in this repository](#1-what-fully-functional-means-in-this-repository)
2. [Prerequisites (one-time installs)](#2-prerequisites-one-time-installs)
3. [Clone the repository](#3-clone-the-repository)
4. [Part A — Run the Android app (Kotlin, primary track)](#4-part-a--run-the-android-app-kotlin-primary-track)
5. [Part B — Run the FastAPI backend](#5-part-b--run-the-fastapi-backend)
6. [Part C — Connect the app to the backend (Cloud mode)](#6-part-c--connect-the-app-to-the-backend-cloud-mode)
7. [Part D — Real AI: install a trained TensorFlow Lite model](#7-part-d--real-ai-install-a-trained-tensorflow-lite-model)
8. [Part E — End-to-end verification checklist](#8-part-e--end-to-end-verification-checklist)
9. [Part F — Running the automated tests](#9-part-f--running-the-automated-tests)
10. [Part G — Production release build (signed APK/AAB)](#10-part-g--production-release-build-signed-apkaab)
11. [Troubleshooting](#11-troubleshooting)

---

## 1. What "fully functional" means in this repository

The repository ships a **complete, runnable application** — all screens and features are
implemented in two identical tracks:

| Track | Folder | Status |
|---|---|---|
| **Kotlin (primary)** | [`android-app-kotlin/`](../android-app-kotlin/) | Complete — build this one first |
| Java (secondary twin) | [`android-app/`](../android-app/) | Complete — identical behavior |
| FastAPI backend | [`backend-api/`](../backend-api/) | Complete — runs with or without TensorFlow |

**Every feature works out of the box:**

- 📷 Camera capture and gallery pick (with runtime permission handling)
- 🔀 Detection mode toggle: **Offline (on-device)** vs **Cloud (backend API)**
- 🧠 On-device TensorFlow Lite inference (see the model note below)
- ☁️ Cloud inference via the FastAPI backend (mock predictor if no model file)
- 📄 Result screen with disease, confidence, symptoms, treatment, prevention + share
- 🗂 Scan history saved in a Room (SQLite) database, with detail view and delete
- 📚 Disease library parsed from `assets/diseases.xml` (10 diseases)
- ⚙️ Settings: backend URL and confidence threshold (persisted in SharedPreferences)
- 🔔 Scan-reminder notification channel

**The one deliberate placeholder:** `assets/model.tflite` is a text stub, because a
trained model is a large binary that each learner must obtain or train themselves (see
[`../model/model-acquisition-guide.md`](../model/model-acquisition-guide.md)). The app
detects the invalid stub and automatically switches to a built-in **heuristic
classifier** (leaf-color analysis over 3 tomato classes), so Offline mode still returns
sensible demo predictions and the entire UI flow works without any extra download.
Likewise, the backend runs in **mock mode** when no Keras model file is present. To turn
the demo AI into real AI, follow [Part D](#7-part-d--real-ai-install-a-trained-tensorflow-lite-model).

---

## 2. Prerequisites (one-time installs)

Full step-by-step install instructions with verification commands are in
[`environment-setup.md`](environment-setup.md). Summary:

| Tool | Version | Needed for |
|---|---|---|
| Android Studio | Hedgehog (2023.1.1) or newer | Building/running the app (bundles JDK 17 + Gradle) |
| Android SDK | API 34 installed (min device API 24 / Android 7.0) | Compile target |
| Emulator (AVD) or physical device | Any with a camera | Running the app |
| Git | 2.x | Cloning the repo |
| Python | **3.10 or 3.11** if you want TensorFlow (real model support); 3.12+ works for mock mode only | Backend + model scripts |
| pip | 23.x+ | Backend dependencies |

> ⚠️ You need an **internet connection the first time you build**: Gradle downloads the
> Android Gradle Plugin and libraries from Google Maven and Maven Central (~200 MB).

> ⚠️ **JDK 17 is mandatory.** The Android Gradle Plugin refuses to run on Java 11 or
> older (`Android Gradle plugin requires Java 17 to run. You are currently using Java 11`).
> Verify **before** building:
>
> ```bash
> java -version        # must report 17.x
> ```
>
> On GitHub Codespaces / dev containers with SDKMAN, switch with:
>
> ```bash
> sdk install java 17.0.11-ms   # if 17 is not installed yet
> sdk use java 17.0.11-ms
> export JAVA_HOME="$(dirname "$(dirname "$(readlink -f "$(which java)")")")"
> ```
>
> Android Studio users can skip this — the IDE bundles JDK 17.

---

## 3. Clone the repository

```bash
git clone https://github.com/Raj-Indra-Asura/LeafGuard-AI-An-Android-Based-Plant-Disease-Detection-Application.git
cd LeafGuard-AI-An-Android-Based-Plant-Disease-Detection-Application
```

---

## 4. Part A — Run the Android app (Kotlin, primary track)

> Prefer Java? Every step below is identical for the Java twin — just open
> [`android-app/`](../android-app/) instead. See
> [`JAVA_VS_KOTLIN.md`](JAVA_VS_KOTLIN.md) for the file-by-file mapping.

1. **Open the project**: In Android Studio choose **File → Open** and select the
   **`android-app-kotlin/`** folder (not the repository root!). Click **Trust Project**.
2. **Gradle sync**: wait for the automatic sync to finish (first time: several minutes).
   If prompted to upgrade/install Gradle or SDK components, accept.
3. **Create/select a device**:
   - Emulator: **Device Manager → Create Device** → e.g. Pixel 7, system image API 34.
   - Physical device: enable **Developer Options → USB debugging** and plug it in.
4. **Run**: press the green **▶ Run** button (or `Shift+F10`).
5. The **LeafGuard AI home screen** appears with: image preview area, *Capture Image* /
   *Choose from Gallery* buttons, an *Offline / Cloud* detection-mode toggle, *Detect
   Disease* button, and navigation to History, Disease Library, and Settings.

### First functional test (no backend needed)

1. Leave the mode toggle on **Offline**.
2. Tap **Choose from Gallery** and pick any leaf photo — or **Capture Image** (grant the
   camera permission when asked). Sample images are in
   [`../sample-images/`](../sample-images/).
3. Tap **Detect Disease** → the **Result screen** shows a disease name, confidence,
   symptoms, treatment, and prevention.
4. Tap **Save to history**, go back, open **History** → your scan is listed; tap it for
   the detail view.
5. Open **Disease Library** → 10 diseases are listed from the bundled XML.

If all five steps work, the app is functional end-to-end in offline mode. ✅

### Command-line alternative (no IDE)

```bash
cd android-app-kotlin
./gradlew assembleDebug            # Windows: gradlew.bat assembleDebug
# APK output: app/build/outputs/apk/debug/app-debug.apk
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### Headless setup (Codespaces / server without Android Studio) — verified working

Without Android Studio there is no bundled SDK, so `assembleDebug` fails with
*"SDK location not found"*. Install the command-line SDK once:

```bash
# 1. Ensure JDK 17 is active (see the warning in section 2)
java -version

# 2. Download the command-line tools and install the required packages
mkdir -p ~/android-sdk/cmdline-tools
cd ~/android-sdk/cmdline-tools
wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
unzip -q commandlinetools-linux-*.zip && mv cmdline-tools latest && rm commandlinetools-linux-*.zip

export ANDROID_HOME=~/android-sdk
yes | "$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager" --licenses
"$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager" \
  "platform-tools" "platforms;android-34" "build-tools;34.0.0"

# 3. Point BOTH app tracks at the SDK (local.properties is gitignored — per machine)
cd <repo-root>
echo "sdk.dir=$HOME/android-sdk" > android-app-kotlin/local.properties
echo "sdk.dir=$HOME/android-sdk" > android-app/local.properties

# 4. Build
cd android-app-kotlin && ./gradlew assembleDebug
```

Expect the first build to take several minutes (Gradle + AGP + dependency downloads).
You cannot *run* an emulator inside a typical Codespace — download the built
`app-debug.apk` and install it on a device, or use a local machine for Part A steps 3–5.

---

## 5. Part B — Run the FastAPI backend

The backend is **optional** (Offline mode never touches it) but enables Cloud mode and
mirrors a production client–server deployment. Detailed reference:
[`../backend-api/README.md`](../backend-api/README.md).

```bash
cd backend-api

# 1. Create and activate a virtual environment
python3 -m venv venv
source venv/bin/activate            # Windows: venv\Scripts\activate

# 2. Install dependencies
pip install -r requirements.txt
#    If TensorFlow fails to install (e.g. Python 3.12+), install without it —
#    the server then runs in mock mode automatically:
#    pip install fastapi "uvicorn[standard]" python-multipart pillow numpy python-dotenv

# 3. Start the server
uvicorn main:app --host 0.0.0.0 --port 8000
```

### Verify

```bash
# Health — note "use_mock" tells you whether a real model is loaded
curl http://localhost:8000/
# → {"status":"ok","use_mock":true,"model_loaded":false,...,"class_count":10}

# Prediction round-trip with a bundled sample image (form field name: image)
curl -X POST http://localhost:8000/predict \
  -F "image=@../sample-images/healthy/tomato_healthy_01.png;type=image/png"
# → {"disease":"...","confidence":0.78,"symptoms":"...","treatment":"...","prevention":"..."}

# Interactive API docs
# open http://localhost:8000/docs in a browser
```

To serve **real predictions**, place a trained Keras model at
`backend-api/models/leafguard_model.keras` (or set `MODEL_PATH` in a `.env` file) and
restart — `model_loaded` flips to `true`.

---

## 6. Part C — Connect the app to the backend (Cloud mode)

| Where the app runs | Backend URL to use | Configuration needed |
|---|---|---|
| **Android emulator**, backend on the same computer | `http://10.0.2.2:8000/` | **None — this is the app's default** |
| **Physical phone**, backend on your computer (same Wi-Fi) | `http://<your-computer-IP>:8000/` | Set in the app's **Settings** screen |

1. Make sure the backend is running (Part B) with `--host 0.0.0.0`.
2. Physical device only: find your computer's IP (`ip addr` / `ipconfig`), open the app's
   **Settings** screen, and set the Backend API URL (e.g. `http://192.168.1.105:8000/`).
   Allow port 8000 through your firewall if the phone cannot reach it.
3. On the home screen switch the detection mode toggle to **Cloud**.
4. Pick an image and tap **Detect Disease** → the app POSTs the image to `/predict` and
   shows the JSON response on the Result screen.

> The app allows cleartext (`http://`) traffic via its network security config, so no
> HTTPS certificate is needed for local development. For a real production deployment,
> host the API behind HTTPS and use an `https://` URL in Settings.

---

## 7. Part D — Real AI: install a trained TensorFlow Lite model

Out of the box, Offline mode uses the heuristic fallback (see section 1). To run **real
on-device inference**:

1. **Get a model** — three routes, fully documented in
   [`../model/model-acquisition-guide.md`](../model/model-acquisition-guide.md):
   - **Option A**: adapt a pre-trained MobileNetV2 from TensorFlow Hub
   - **Option B**: use PlantVillage resources from Kaggle
   - **Option C** (recommended): train your own in Google Colab (free GPU) —
     complete notebook steps included
2. **Match the app's contract** (also in [`../model/model-notes.md`](../model/model-notes.md)):
   - Input: `1 × 224 × 224 × 3` float32, RGB, normalized to `0..1`
   - Output: `1 × 10` softmax matching the exact label order of
     [`../model/labels.txt`](../model/labels.txt)
3. **Install it into BOTH app tracks** (they must stay identical):
   ```bash
   cp your_model.tflite android-app-kotlin/app/src/main/assets/model.tflite
   cp your_model.tflite android-app/app/src/main/assets/model.tflite
   ```
4. **Rebuild and run.** The classifier now loads the real interpreter (Logcat no longer
   shows the "heuristic fallback" warning) and predictions cover all 10 classes.

**Pipeline smoke test without training**: `python3 model/generate_stub_model.py`
(requires `pip install tensorflow`) generates a *valid but untrained* model into
`android-app/app/src/main/assets/` — useful to prove the TFLite integration works, but
its predictions are random. Copy it to the Kotlin track's assets too if you use it.

For the backend's real AI, use the same training pipeline but save the **Keras** model
(`.keras`) to `backend-api/models/leafguard_model.keras` (Part B).

---

## 8. Part E — End-to-end verification checklist

Run through this list to confirm the complete system:

- [ ] App builds and launches (`assembleDebug` succeeds, home screen renders)
- [ ] Camera capture works and shows the photo in the preview (permission granted)
- [ ] Gallery pick works and shows the image in the preview
- [ ] **Offline mode**: Detect Disease returns a prediction and opens the Result screen
- [ ] Result screen shows disease, confidence %, symptoms, treatment, prevention
- [ ] Share button opens the Android share sheet with the result text
- [ ] Save to history persists the scan (survives app restart — Room/SQLite)
- [ ] History list shows saved scans; tapping opens the detail view; delete works
- [ ] Disease Library lists 10 diseases parsed from `assets/diseases.xml`
- [ ] Settings: changing the confidence threshold and backend URL persists
- [ ] Backend: `curl http://localhost:8000/` returns `{"status":"ok",...}`
- [ ] Backend: `POST /predict` with a sample image returns a full JSON prediction
- [ ] **Cloud mode**: with the backend running, Detect Disease returns the server's result
- [ ] Low-confidence handling: predictions below the threshold show the "uncertain" notice
- [ ] (With a real model installed) Logcat shows no heuristic-fallback warning

---

## 9. Part F — Running the automated tests

Both tracks contain real tests:

```bash
# JVM unit tests (no device needed) — JSON contract with the backend
cd android-app-kotlin && ./gradlew testDebugUnitTest
cd android-app        && ./gradlew testDebugUnitTest

# Instrumented UI tests (emulator/device must be running)
cd android-app-kotlin && ./gradlew connectedDebugAndroidTest
```

Backend manual test: see the `curl` commands in Part B, or the interactive Swagger UI at
`http://localhost:8000/docs`.

---

## 10. Part G — Production release build (signed APK/AAB)

For a distributable, installable release build (Week 12 material):

1. **Generate a keystore** (one time — keep it safe and out of Git):
   ```bash
   keytool -genkey -v -keystore leafguard-release.jks \
     -keyalg RSA -keysize 2048 -validity 10000 -alias leafguard
   ```
2. **Build a signed artifact** in Android Studio: **Build → Generate Signed App Bundle /
   APK…** → choose your keystore → build type **release**.
   - CLI alternative: add a `signingConfigs` block to `app/build.gradle` (keep passwords
     in `~/.gradle/gradle.properties`, never in the repo) and run `./gradlew assembleRelease`.
3. **Install/verify**:
   ```bash
   adb install app/build/outputs/apk/release/app-release.apk
   ```
4. Production hardening checklist:
   - Install a **real trained model** (Part D) — do not ship the heuristic fallback
   - Point Settings' default URL at your **HTTPS** production API, or ship offline-only
   - Consider enabling `minifyEnabled true` (R8) and re-testing
   - Bump `versionCode`/`versionName` in `app/build.gradle` for each release

---

## 11. Troubleshooting

| Symptom | Cause | Fix |
|---|---|---|
| Build fails: "Android Gradle plugin requires Java 17 to run. You are currently using Java 11" | `JAVA_HOME` points at an old JDK (common on Codespaces) | Switch to JDK 17: `sdk use java 17.0.11-ms` (SDKMAN) or set `JAVA_HOME` to a JDK 17 install; verify with `java -version` |
| Gradle sync fails: "SDK location not found" | `ANDROID_HOME` not set / no `local.properties` | Let Android Studio generate `local.properties`, or follow the *Headless setup* steps in Part A and write `local.properties` yourself |
| Build fails: `resource style/Widget.Material3.OutlinedButton not found` | Wrong Material 3 style name in a layout (correct name is `Widget.Material3.Button.OutlinedButton`) | Already fixed in this repo's layouts; if you reintroduce it, use the full `…Button.OutlinedButton` name |
| Build fails: `Unresolved reference: BuildConfig` / `cannot find symbol BuildConfig` | AGP 8 disables `BuildConfig` generation by default | Already fixed: `buildFeatures { buildConfig true }` is set in both tracks' `app/build.gradle` — keep it if you edit that block |
| Gradle sync fails: cannot resolve `com.android.application` | No access to `dl.google.com` (offline/blocked network) | Build on a network with Google Maven access; corporate proxies must allow it |
| App result always tomato-related, Logcat warns about "heuristic fallback" | `model.tflite` is still the text placeholder | Expected demo behavior; install a real model (Part D) |
| Cloud mode: "Unable to reach the backend" | Backend not running, wrong URL, or firewall | Verify `curl http://localhost:8000/`; emulator must use `10.0.2.2`, phone must use the computer's LAN IP; open port 8000 |
| Backend `use_mock: true` even with TensorFlow installed | No model file at `MODEL_PATH` | Place `models/leafguard_model.keras` or set `MODEL_PATH` in `.env` |
| `pip install -r requirements.txt` fails on TensorFlow | Python 3.12+ (TF 2.14 needs 3.9–3.11) | Use Python 3.10/3.11, or install without TensorFlow (mock mode) |
| Camera button does nothing on emulator | AVD has no camera configured | AVD settings → set front/back camera to *Emulated* or *Webcam0* |
| Port 8000 already in use | Another process bound to it | `lsof -ti:8000 \| xargs kill -9` (or run uvicorn with `--port 8001` and update Settings) |
| Predictions look wrong with a custom model | Preprocessing/label mismatch | Model must take 224×224×3 float32 RGB 0..1 and output 10 classes in `model/labels.txt` order |

More: [`environment-setup.md`](environment-setup.md) →
Troubleshooting, and each component's own README.

---

**You now have the complete system running.** Continue with the
[learning roadmap](../roadmap/week-01-project-understanding/README.md) to understand how
every piece works — and rebuild it yourself.
