# LeafGuard AI — Full Product Presentation Blueprint

> **What this document is.** A slide-by-slide, pipeline-oriented presentation script for the
> entire LeafGuard AI product, written from the *actual code in this repository* (not from
> marketing claims). It is meant to be handed to a human designer or to an AI
> slide-generation agent as the single input needed to build the deck.
>
> **What this document is not.** It is not the academic viva outline. That already exists at
> [`docs/presentation-outline.md`](presentation-outline.md) (15 slides, course-submission
> framing). This blueprint is the *product and architecture* deck: how every component,
> data path, and orchestration layer connects, in sequence.

---

## How to use this document (instructions for the slide-building agent)

1. **One `## SLIDE n` heading = exactly one slide.** Do not merge or split slides.
2. Every slide has four fixed blocks. Use them as follows:
   - **PURPOSE** — the single idea the slide must land. Never printed on the slide.
   - **ON-SLIDE CONTENT** — the literal text to place on the slide. Keep the hierarchy
     (title, bullets, table). Trim wording for space, never change technical facts.
   - **VISUAL / DIAGRAM SPEC** — exactly what to draw. Boxes, arrows, labels, and colours
     are prescribed; follow them so diagrams stay consistent across the deck.
   - **SPEAKER NOTES** — the narration, written to be read aloud in 45–90 seconds.
   - **SOURCE OF TRUTH** — repository file paths that back the slide. Use these if any
     detail must be re-verified. Do not print them on the slide (except Slide 34).
3. **Visual language, used consistently across all diagrams:**
   - Green box = Android app component (`android-app-kotlin/`)
   - Blue box = Backend component (`backend-api/`)
   - Orange box = Model artifact / model pipeline (`model/`)
   - Grey box = Storage (Room DB, SharedPreferences, assets, filesystem)
   - Solid arrow = runtime data flow · Dashed arrow = build-time / offline flow
   - Diamond = decision point · Cylinder = persisted data
4. **Never invent numbers.** Every metric, hash, threshold, shape, and class name in this
   document was read out of the repository. If the deck needs a number that is not here,
   mark it `TBD` rather than estimating.
5. **Tone:** engineering-honest. This product deliberately documents what is *validated*
   and what is *not yet validated* (Slides 29–31). Keep those slides; they are the
   strongest part of the story in a technical review.

**Deck statistics:** 36 slides · 8 sections · target runtime 30–35 minutes
(or 12 minutes using the "core path" subset marked ⭐).

**Section map**

| Section | Slides | Theme |
|---|---|---|
| A. Framing | 1–5 | Problem, product definition, system landscape |
| B. The master pipeline | 6–9 | End-to-end flow and the orchestration layer |
| C. Inference pipelines | 10–18 | Capture → mode decision → cloud path → offline path → convergence |
| D. Post-inference pipelines | 19–24 | Result, persistence, history, analytics, library, settings, notifications |
| E. Supply chain & delivery | 25–27 | Model supply chain, build/CI, data-residency model |
| F. Engineering quality | 28–31 | Concurrency, security, testing, honest limitations |
| G. Project story | 32–34 | Dual-track design, 12-week cumulative build, roadmap |
| H. Close | 35–36 | Demo script, Q&A anchors, appendix |

---

# SECTION A — FRAMING

## SLIDE 1 — Title ⭐

**PURPOSE**
Establish identity: this is a real, buildable, dual-runtime Android + FastAPI + TFLite product,
not a mockup.

**ON-SLIDE CONTENT**

```
LeafGuard AI
An Android-Based Plant Disease Detection Application

A dual-inference product: on-device TensorFlow Lite + cloud FastAPI,
unified behind one prediction contract.

38 disease classes · 8 screens · 2 inference paths · 1 orchestration layer

Android (Kotlin, primary) · Java (parity twin) · FastAPI · TensorFlow / TFLite · Room · Retrofit

Package: com.leafguard   |   Version: 0.2.0-beta (versionCode 2)
minSdk 24 (Android 7.0) · targetSdk / compileSdk 34
```

**VISUAL / DIAGRAM SPEC**
Full-bleed leaf photograph, darkened 40%. Title left-aligned, large. Bottom strip: five
technology wordmarks in a single row — Android, Kotlin, TensorFlow Lite, FastAPI, Room.

**SPEAKER NOTES**
"LeafGuard AI is an Android application that identifies plant diseases from a leaf photo. What
makes it architecturally interesting is that it has *two* complete inference engines — one that
runs entirely on the phone using TensorFlow Lite, and one that runs on a FastAPI server using a
Keras model — and both are hidden behind a single result contract, so the rest of the app cannot
tell which one produced the answer. Today I'll walk the entire product as a set of connected
pipelines, from the camera shutter to the saved history record."

**SOURCE OF TRUTH**
`android-app-kotlin/app/build.gradle` (applicationId, versionName `0.2.0-beta`, versionCode 2,
minSdk 24, compileSdk 34); `android-app-kotlin/app/src/main/AndroidManifest.xml` (8 activities);
`model/labels-38.txt` (38 classes).

---

## SLIDE 2 — How to read this deck

**PURPOSE**
Give the audience the mental model before the detail, so no slide feels disconnected.

**ON-SLIDE CONTENT**

```
THIS DECK FOLLOWS THE DATA, NOT THE FEATURE LIST

We trace one leaf photograph through every layer of the system:

  Capture  →  Mode decision  →  Inference  →  Normalisation  →  Uncertainty gate
          →  Presentation  →  Persistence  →  Aggregation

Eight sections:
  A  Framing                  — what the product is and is not
  B  The master pipeline      — the end-to-end path and its orchestrator
  C  Inference pipelines      — the two engines and how they converge
  D  Post-inference pipelines — result, history, analytics, library, settings
  E  Supply chain & delivery  — how the model and the APK are produced
  F  Engineering quality      — concurrency, security, testing, limits
  G  Project story            — dual track and the 12-week cumulative build
  H  Close                    — live demo script and Q&A anchors
```

**VISUAL / DIAGRAM SPEC**
Horizontal 8-stage chevron/arrow ribbon for the data path (Capture → … → Aggregation), with the
section list beneath as a two-column legend. Reuse this ribbon as a progress indicator in the
top-right corner of every Section C and D slide, with the current stage highlighted green.

**SPEAKER NOTES**
"Rather than listing features, I'll follow a single photograph through the system. Every slide
answers the question: what happens to the data next, and which component owns that step. The
ribbon at the top of each slide shows where we are in that journey."

**SOURCE OF TRUTH**
Structure derived from `ScanActivity.kt` control flow (`detectDisease()` →
`runCloudDetection()` / `runOfflineDetection()` → `openResult()`).

---

## SLIDE 3 — The problem and the user ⭐

**PURPOSE**
Justify both the product *and* the two-engine architecture. The offline path is not a
gimmick — it is a direct response to the deployment environment.

**ON-SLIDE CONTENT**

```
THE PROBLEM

A farmer sees a damaged leaf and needs three answers, quickly:
   1. What is this?          2. What do I do now?          3. How do I stop it recurring?

Today those answers require an expert visit or a lab test — slow, costly, and
unavailable at the moment and place the decision is actually made: standing in the field.

THE CONSTRAINT THAT SHAPES THE ARCHITECTURE

  Fields have poor or no connectivity.
  → A cloud-only app fails exactly where it is needed most.
  → Therefore: on-device inference is a first-class path, not a fallback.

  Phones vary from Android 7.0 upward.
  → minSdk 24. No dependency on recent-only APIs in the critical path.

DESIGN RESPONSE
  Two engines. One contract. The UI never branches on which engine answered.
```

**VISUAL / DIAGRAM SPEC**
Left: three stacked question cards ("What is this?", "What do I do?", "How do I prevent it?").
Right: a signal-bars icon crossed out, with an arrow to a phone icon containing a small chip
symbol, captioned "inference runs here when the network does not".

**SPEAKER NOTES**
"The important design insight is that connectivity is worst exactly where diagnosis is most
valuable. That single constraint is why this app carries a TensorFlow Lite model inside the APK
and treats the server as the *optional* path — in fact the app boots with offline mode selected
by default. The architecture is a response to the field, not to convenience."

**SOURCE OF TRUTH**
`ScanActivity.kt` — `binding.toggleDetectionMode.check(R.id.buttonOfflineMode)` in
`setupModeToggle()`, i.e. offline is the default selection; `cloudMode = false` initial value.
`android-app-kotlin/app/build.gradle` — `minSdk 24`.

---

## SLIDE 4 — What the product is, and what it is not ⭐

**PURPOSE**
Set honest scope early. This protects every later claim and pre-empts the hardest review
question.

**ON-SLIDE CONTENT**

```
WHAT IT IS                                   WHAT IT IS NOT (yet)

✓ A working Android app, two tracks          ✗ A certified diagnostic device
  (Kotlin primary, Java parity twin)         ✗ A field-validated accuracy claim
✓ 8 Activities, 5-tab navigation             ✗ Multi-leaf or whole-plant analysis
✓ On-device TFLite inference, 38 classes     ✗ A production-signed public release
✓ Cloud FastAPI inference, same 38 classes   ✗ A cloud service with authentication
✓ Verified numerical parity between the      ✗ A trainer — the model is sourced,
  two engines (max delta 0.000006)             pinned, hashed and licence-reviewed,
✓ Offline persistence (Room), offline          not trained in this repository
  disease library (XML), local analytics
✓ An explicit uncertainty gate that
  refuses to present low-confidence
  output as a diagnosis

HONEST STATUS: v0.2.0-beta. Debug-signed. Device testing incomplete. See Slide 31.
```

**VISUAL / DIAGRAM SPEC**
Two-column contrast layout. Left column green ticks, right column grey crosses — grey, not red:
these are scope boundaries, not failures.

**SPEAKER NOTES**
"I want to set the boundary before I show the architecture. This is a real, running,
end-to-end system with a genuinely verified model pipeline. It is not a certified diagnostic
tool, and the code says so out loud: when confidence drops below the user's threshold, the app
rewrites its own result to say 'uncertain, retake the image, verify with an expert'. I'll show
you that code path on Slide 17."

**SOURCE OF TRUTH**
`docs/evidence/week-12/model-validation-2026-07-16.md` (parity 30/30, max delta 0.000006,
semantic accuracy 28/30, device tests not run); `release-records/model-provenance.txt`;
`ScanActivity.kt` `openResult()` uncertainty rewrite; `strings.xml` `uncertain_prediction_*`.

---

## SLIDE 5 — System landscape: four subsystems ⭐

**PURPOSE**
Introduce the four top-level parts before any pipeline detail, so every later slide has a home.

**ON-SLIDE CONTENT**

```
FOUR SUBSYSTEMS, ONE PRODUCT

1. ANDROID CLIENT            android-app-kotlin/   (primary, Kotlin 1.9.22)
   8 Activities · Room · Retrofit · TFLite Interpreter · XmlPullParser
   Java twin: android-app/  — same package, same behaviour, same assets

2. BACKEND SERVICE           backend-api/          (FastAPI + Uvicorn, Python 3.11)
   main.py · config.py · model_loader.py · labels.py
   Endpoints: GET / · GET /health · GET /diseases · POST /predict

3. MODEL PIPELINE            model/                (build-time, not shipped code)
   model_contract.py · inspect_model.py · convert_model.py
   validate_tflite.py · parity_test.py · labels-38.txt

4. KNOWLEDGE & DOCS          docs/ · roadmap/ · validation/ · release-records/
   Architecture ground truth · release runbook · provenance · 12-week roadmap

THE JOIN: all four agree on ONE contract —
   float32 input [1, 224, 224, 3], raw RGB 0..255, float32 output [1, 38],
   canonical label order from model/labels-38.txt.
```

**VISUAL / DIAGRAM SPEC**
Four quadrant boxes (green, blue, orange, grey) around a central white hexagon labelled
"THE CONTRACT — [1,224,224,3] float32 → [1,38] float32 · 38 canonical labels". Four arrows
point *inward* from each quadrant to the hexagon. This central-contract motif should recur on
Slides 15 and 25.

**SPEAKER NOTES**
"Four subsystems. What holds them together is not a framework — it is a contract. The Android
classifier, the FastAPI predictor, and the conversion script all independently validate the same
tensor shape, the same pixel range, and the same 38-label ordering, and each one refuses to run
if the artifact it is given does not match. That mutual refusal is the real integration
mechanism in this product."

**SOURCE OF TRUTH**
`model/model_contract.py` (`EXPECTED_INPUT_SHAPE = (1,224,224,3)`, `EXPECTED_CLASS_COUNT = 38`);
`TFLiteClassifier.kt` `initializeModel()` shape checks; `backend-api/model_loader.py`
`load_predictor()` shape checks; `android-app-kotlin/app/build.gradle` task
`validateReleaseModel`.

---

# SECTION B — THE MASTER PIPELINE

## SLIDE 6 — The end-to-end master pipeline ⭐⭐

**PURPOSE**
The single most important slide of the deck. Everything after this is a zoom into one box.

**ON-SLIDE CONTENT**

```
ONE PHOTOGRAPH, ELEVEN STAGES

 1  ACQUIRE      ScanActivity — camera (FileProvider URI) or gallery (GetContent)
 2  PREVIEW      Uri → ImageView; detection controls become visible
 3  ROUTE        Mode toggle: Offline (default) or Cloud
 4a OFFLINE      Uri → Bitmap → 224×224 → ByteBuffer(float32, RGB 0..255)
                 → TFLite Interpreter → float[38] → argmax
 4b CLOUD        Uri → cache file → multipart part "image" → POST /predict
                 → PIL resize 224 → np.float32 [1,224,224,3] → Keras → argmax
 5  NORMALISE    model_label ("Tomato___Late_blight") → display name ("Tomato Late Blight")
 6  ENRICH       display name / model label → symptoms + treatment + prevention
 7  UNIFY        Both paths produce the SAME object: PredictionResponse
 8  GATE         confidence × 100 < threshold  →  rewrite as "Uncertain / retake image"
 9  PRESENT      Intent extras → ResultActivity: name, %, bar, guidance, share
10  PERSIST      "Save to history" → Room insert into scan_history (leafguard.db)
11  AGGREGATE    HistoryActivity list · HistoryDetailActivity · AnalyticsActivity summary
```

**VISUAL / DIAGRAM SPEC**
The hero diagram. Draw as a left-to-right swimlane pipeline:
- Lane 1 (green, top): `ScanActivity` — stages 1, 2, 3, 8, 9-handoff.
- Lane 2 (green, middle-upper): `TFLiteClassifier` — stage 4a.
- Lane 3 (blue, middle-lower): `Retrofit → FastAPI → ModelPredictor` — stage 4b.
- Lane 4 (green, lower): `ResultActivity` — stage 9.
- Lane 5 (grey, bottom): `Room / leafguard.db` — stages 10, 11.
At stage 3 place a **diamond** labelled `cloudMode?`; the two branches split, run in parallel
lanes, then **re-merge at a single filled circle labelled `PredictionResponse`** before stage 8.
That merge point must be visually emphatic — it is the architectural thesis of the deck.
Build this slide with animation: reveal stages 1→11 in sequence.

**SPEAKER NOTES**
"This is the whole product on one slide. Follow the photograph. It is acquired in ScanActivity,
routed by a single boolean called `cloudMode`, and then it goes down one of two completely
different technology stacks — Kotlin and a TFLite Interpreter on the left, or HTTP, Python,
Pillow and Keras on the right. Watch what happens at this point [merge circle]: both stacks
produce the identical Kotlin object, `PredictionResponse`. From here to the end of the app —
the uncertainty gate, the result screen, sharing, the database, analytics — nothing knows or
cares which engine ran. That is the design that makes the rest of the system simple."

**SOURCE OF TRUTH**
`ScanActivity.kt` lines for `detectDisease()`, `runCloudDetection()`, `runOfflineDetection()`,
`openResult()`; `TFLiteClassifier.kt` `classify()`; `backend-api/main.py` `predict()`;
`network/PredictionResponse.kt`; `ResultActivity.kt`; `database/ScanDao.kt`.

---

## SLIDE 7 — The orchestration layer, named ⭐

**PURPOSE**
Answer the question the deck exists to answer: *what orchestrates all this?* LeafGuard has no
ViewModel and no dependency-injection framework — orchestration is explicit and lives in four
identifiable places.

**ON-SLIDE CONTENT**

```
THERE IS NO FRAMEWORK DOING THIS FOR US. ORCHESTRATION IS EXPLICIT, IN FOUR PLACES:

1. FLOW ORCHESTRATOR      ScanActivity
   Owns the whole detection transaction: input selection, permissions, mode routing,
   progress state, error handling, uncertainty gating, and hand-off to the result screen.

2. CONTRACT ORCHESTRATOR  PredictionResponse (com.leafguard.network)
   One Kotlin data class serving three roles at once:
     · Gson DTO for the server's JSON        (@SerializedName)
     · Return type of the on-device classifier
     · Transport payload into ResultActivity via Intent extras

3. NAVIGATION ORCHESTRATOR  ui/BottomNav.kt — fun AppCompatActivity.setupBottomNav(...)
   One Kotlin extension function shared by all five tab screens. Tapping a tab starts the
   target Activity and finishes the current one, so the back stack never exceeds one screen.

4. CONFIGURATION ORCHESTRATOR  SettingsActivity + default SharedPreferences
   Two keys steer runtime behaviour app-wide:
     pref_backend_url            (default http://10.0.2.2:8000)
     pref_confidence_threshold   (default 50)

DELIBERATE ABSENCES: no ViewModel, no Fragments, no Navigation Component, no Hilt/Dagger,
no Repository layer. Each is a documented trade-off, not an oversight (see Slide 28).
```

**VISUAL / DIAGRAM SPEC**
A central rounded rectangle labelled `ScanActivity — flow orchestrator`, with three satellites:
`PredictionResponse` (contract), `setupBottomNav()` (navigation), `SharedPreferences`
(configuration). Bottom banner in grey: the "deliberate absences" line.

**SPEAKER NOTES**
"When people ask 'where is the orchestration layer', in many Android apps the answer is a
ViewModel or a dependency graph. Here it is deliberately explicit, and it lives in four named
places. ScanActivity orchestrates the *flow*. PredictionResponse orchestrates the *contract* —
it is simultaneously the JSON model for the server and the return type of the on-device
classifier, which is precisely why the two engines are interchangeable. BottomNav orchestrates
*navigation* as a single shared extension function. And SharedPreferences orchestrates
*configuration* — two keys that change how the whole app behaves at runtime. The absences are
intentional and documented: the code comment in BottomNav.kt even states the trade-off, that
each tab is its own Activity so the system Back button exits rather than returning to a previous
tab, and names Fragments plus the Navigation Component as the future replacement."

**SOURCE OF TRUTH**
`ScanActivity.kt`; `network/PredictionResponse.kt`; `ui/BottomNav.kt` (KDoc explicitly discusses
the Fragment/Navigation-Component trade-off); `SettingsActivity.kt` companion constants
`PREF_BACKEND_URL`, `PREF_CONFIDENCE_THRESHOLD`, `DEFAULT_BACKEND_URL`,
`DEFAULT_CONFIDENCE_THRESHOLD`.

---

## SLIDE 8 — Screen map: 8 Activities, 5 tabs

**PURPOSE**
Give the audience the complete UI surface so later pipeline slides can name screens freely.

**ON-SLIDE CONTENT**

```
EIGHT ACTIVITIES — FIVE ARE TABS, THREE ARE PUSHED SCREENS

TAB SCREENS (bottom navigation, shared via setupBottomNav)
  MainActivity            Home    Dashboard: scan count from Room, library count,
                                  "Start Scanning" CTA, architecture info dialogs
  ScanActivity            Scan    Capture/upload + mode toggle + detect   ← the orchestrator
  AnalyticsActivity       Analytics  Total scans, mean confidence, most frequent disease
  DiseaseLibraryActivity  Library    Searchable offline encyclopedia from assets/diseases.xml
  SettingsActivity        About      Backend URL, confidence threshold, version, reset

PUSHED SCREENS (started with an Intent, dismissed with finish())
  ResultActivity          Diagnosis view + share + save-to-history      ← from ScanActivity
  HistoryActivity         RecyclerView of saved scans, delete inline    ← from Home card
  HistoryDetailActivity   One record by EXTRA_SCAN_ID, share, delete    ← from History row

LAUNCHER: MainActivity (android.intent.action.MAIN / LAUNCHER). All others exported="false".
```

**VISUAL / DIAGRAM SPEC**
Phone-frame grid, 8 frames. Bottom row of five carries a highlighted tab bar; the three pushed
screens sit above with curved arrows from their launching screen. Label each arrow with what it
carries — e.g. `ScanActivity → ResultActivity : 6 Intent extras`,
`HistoryActivity → HistoryDetailActivity : EXTRA_SCAN_ID (Long)`.

**SPEAKER NOTES**
"Eight screens. Five are tabs wired by that shared navigation function; three are pushed
screens reached by an explicit Intent. Note the security default in the manifest: only
MainActivity is exported. No other screen can be launched by another app on the device."

**SOURCE OF TRUTH**
`AndroidManifest.xml` (8 `<activity>` entries, only `MainActivity` `exported="true"` with the
LAUNCHER intent-filter); `res/menu/bottom_nav_menu.xml` (5 items: nav_home, nav_scan,
nav_analytics, nav_library, nav_about); `ui/BottomNav.kt`.

---

## SLIDE 9 — Repository map → subsystem map

**PURPOSE**
Let a reviewer navigate the repo unaided; connect folders to the four subsystems from Slide 5.

**ON-SLIDE CONTENT**

```
WHERE EVERYTHING LIVES

android-app-kotlin/app/src/main/
  java/com/leafguard/
    MainActivity.kt  ScanActivity.kt  ResultActivity.kt  HistoryActivity.kt
    HistoryDetailActivity.kt  DiseaseLibraryActivity.kt  AnalyticsActivity.kt
    SettingsActivity.kt
    ml/TFLiteClassifier.kt              on-device inference (234 lines)
    network/ApiService.kt · PredictionResponse.kt · RetrofitClient.kt
    database/ScanRecord.kt · ScanDao.kt · AppDatabase.kt
    ui/BottomNav.kt                     shared tab navigation
    utils/NotificationHelper.kt         channel leafguard_scan_reminders
  assets/  labels.txt (38) · diseases.xml (10 entries) · model.tflite (generated, git-ignored)
  res/     layout ×10 · values (strings, colors, themes) · xml (network_security_config,
           file_provider_paths) · menu · drawable

android-app/            Java parity twin — identical package, behaviour and assets
backend-api/            main.py · config.py · model_loader.py · labels.py · test_api.py
                        labels-38.txt · Dockerfile · requirements*.txt · models/*.keras (ignored)
model/                  model_contract.py · convert_model.py · inspect_model.py
                        validate_tflite.py · parity_test.py · test_model_contract.py · labels-38.txt
docs/                   ARCHITECTURE_GROUND_TRUTH · PRODUCTION_RELEASE_RUNBOOK · JAVA_VS_KOTLIN
                        RECONSTRUCTION_REPORT · evidence/week-01..12 · uml/
roadmap/                week-01 … week-12, 7 files each
validation/ release-records/ notebooks/ exercises/ solutions/ sample-images/
```

**VISUAL / DIAGRAM SPEC**
Indented tree, colour-coded by subsystem (green / blue / orange / grey per Slide 5's key). Fade
the learning-material folders (roadmap, notebooks, exercises, solutions) to 50% opacity — present
but not part of the runtime product.

**SPEAKER NOTES**
"The repository separates the runtime product from the learning material. The four coloured
groups are the shipping system; the faded folders are the twelve-week course scaffolding that
produced it. Two paths deserve attention: `assets/model.tflite` is git-ignored and generated,
never committed — I'll explain that supply chain on Slide 25 — and `android-app/` is a complete
Java twin of the Kotlin app, which I'll cover on Slide 32."

**SOURCE OF TRUTH**
Repository tree; `.gitignore` (ignores `**/assets/model.tflite`, `backend-api/models/*.keras`,
`*.apk`, `*.jks`, `*.keystore`, `.env`); `docs/JAVA_VS_KOTLIN.md`.

---

# SECTION C — INFERENCE PIPELINES

## SLIDE 10 — Pipeline 1: Image acquisition ⭐

**PURPOSE**
Show that "take a photo" is a multi-step, permission-aware, security-relevant pipeline.

**ON-SLIDE CONTENT**

```
PIPELINE 1 — ACQUIRE  (ScanActivity)

Tap upload area → AlertDialog: "Camera" or "Gallery"

CAMERA BRANCH
  1  Check CAMERA permission (ContextCompat.checkSelfPermission)
  2  If missing → RequestMultiplePermissions launcher; remember the pending action
  3  On grant → createImageUri():
        getExternalFilesDir(PICTURES)/captures/leafguard_<epochMillis>.jpg
  4  Wrap the file with FileProvider  →  content:// URI
        authority: ${applicationId}.fileprovider   (com.leafguard.fileprovider)
  5  ActivityResultContracts.TakePicture(uri) → the camera app writes into our file
  6  On success → updateSelectedImage(uri);  on cancel → Toast, no state change

GALLERY BRANCH
  1  ActivityResultContracts.GetContent("image/*")
  2  NO storage permission requested — GetContent grants scoped, temporary URI access
  3  On selection → updateSelectedImage(uri)

RESULT OF THE PIPELINE
  selectedImageUri set · preview visible · placeholder hidden · detect controls revealed
```

**VISUAL / DIAGRAM SPEC**
Flowchart with a decision diamond at "Camera or Gallery". The camera branch shows a permission
diamond with a denial exit to a Toast node. Add a green callout box on the gallery branch:
"No READ_MEDIA_IMAGES / READ_EXTERNAL_STORAGE permission is declared — scoped access only."

**SPEAKER NOTES**
"Acquisition is where most Android bugs and most privacy problems live, so it is worth the
detail. Two things to notice. First, the camera never gets a raw file path — we hand it a
FileProvider content URI, which is the modern, `FileUriExposedException`-safe pattern. Second,
the gallery branch requests *no* storage permission at all. `GetContent` returns a scoped,
temporary URI, so the app never gains the ability to read the user's whole photo library. The
manifest declares only three permissions: INTERNET, CAMERA, POST_NOTIFICATIONS."

**SOURCE OF TRUTH**
`ScanActivity.kt` — `showImageSourceChooser()`, `openCameraWithPermissionCheck()`,
`launchCamera()`, `createImageUri()`, `setupActivityResults()`, `updateSelectedImage()`;
`AndroidManifest.xml` (`<provider>` FileProvider, three `uses-permission`, camera
`required="false"`); `res/xml/file_provider_paths.xml`.

---

## SLIDE 11 — Pipeline 2: The routing decision ⭐

**PURPOSE**
Isolate the single decision that splits the architecture, and show how small it is in code —
which is the point.

**ON-SLIDE CONTENT**

```
PIPELINE 2 — ROUTE  (ScanActivity)

The entire architectural fork is one boolean field:

    private var cloudMode = false            // OFFLINE is the default

    setupModeToggle()  → MaterialButtonToggleGroup, initial check = buttonOfflineMode
                         cloudMode = (checkedId == R.id.buttonCloudMode)

    detectDisease()
        if (selectedImageUri == null)  → Toast "select an image first", return
        setDetectionInProgress(true)   → progress bar on, detect button disabled
        if (cloudMode) runCloudDetection() else runOfflineDetection()

WHAT EACH MODE MEANS TO THE USER (shown live under the toggle)
  Offline : "runs on-device inference from the bundled TensorFlow Lite classifier"
  Cloud   : "sends the image to the FastAPI backend for server-side prediction"

WHY OFFLINE IS THE DEFAULT
  The app must be demonstrable and useful with no server running and no network.
```

**VISUAL / DIAGRAM SPEC**
Large centred diamond labelled `cloudMode`, two thick arrows: left `false → TFLiteClassifier`
(green), right `true → Retrofit/FastAPI` (blue). Beneath, a small code card with the
`if (cloudMode)` line rendered in monospace, sized to draw the eye — "the whole fork is one
line".

**SPEAKER NOTES**
"This is the fork. One boolean. The reason it can be this small is the contract I mentioned:
both branches are guaranteed to produce the same type, so the routing code has no reconciliation
work to do. Also note the guard rails around the fork — the button is disabled and a progress
indicator shown for the entire duration, so a user cannot fire two overlapping detections."

**SOURCE OF TRUTH**
`ScanActivity.kt` — `cloudMode` field, `setupModeToggle()`, `detectDisease()`,
`setDetectionInProgress()`, `updateModeDescription()`; `strings.xml`
`cloud_mode_description`, `offline_mode_description`.

---

## SLIDE 12 — Pipeline 3A: Cloud inference, client side ⭐

**PURPOSE**
Walk the network path precisely, including the two details that most commonly break it.

**ON-SLIDE CONTENT**

```
PIPELINE 3A — CLOUD, CLIENT SIDE  (ScanActivity → Retrofit)

1  RESOLVE BASE URL
     SharedPreferences[pref_backend_url]  (default http://10.0.2.2:8000)
     blank → default;  then baseUrl.toHttpUrlOrNull()
     null → Toast "invalid backend URL", abort — Retrofit is never handed a bad URL

2  MATERIALISE THE IMAGE
     content:// URI → contentResolver.openInputStream → copy 8 KB at a time →
     cacheDir/leafguard_upload_<epochMillis>.jpg
     (a content URI cannot be uploaded directly; OkHttp needs a real File)

3  BUILD THE MULTIPART REQUEST
     mime = contentResolver.getType(uri), non-image → "image/*"
     RequestBody = file.asRequestBody(mime)
     MultipartBody.Part.createFormData("image", file.name, body)
                                        ^^^^^^^ the part name MUST be "image"

4  SEND
     RetrofitClient.getInstance(baseUrl).create(ApiService::class.java)
     @Multipart @POST("predict") fun uploadImage(@Part image: MultipartBody.Part)
             : Call<PredictionResponse>
     .enqueue(...)   → asynchronous, callback on the main thread

5  HANDLE
     onResponse : delete cache file · progress off ·
                  !isSuccessful || body == null → Toast · else openResult(body)
     onFailure  : delete cache file · progress off · Toast with throwable.message

CLIENT CONFIG   OkHttp 30 s connect/read/write · HttpLoggingInterceptor
                (BODY in debug, BASIC in release) · Gson converter · singleton rebuilt
                only when the base URL changes
```

**VISUAL / DIAGRAM SPEC**
Vertical 5-step pipeline. Step 3 gets a red-outlined callout: `part name = "image", not "file"`.
Step 1 gets a callout explaining `10.0.2.2` = the host machine as seen from the Android emulator.
Show cache-file cleanup as a small bin icon on both the success and failure exits.

**SPEAKER NOTES**
"Two details are worth calling out because they are the classic integration failures. First, the
multipart part name must be exactly `image` — the FastAPI signature is
`predict(image: UploadFile = File(...))`, and any other part name is rejected. Second,
`10.0.2.2` is not a typo: it is the special alias the Android emulator uses to reach the
developer's own machine, because `localhost` inside the emulator means the emulator itself.
Also note the resource hygiene — the temporary upload file is deleted on both the success and
the failure path, and an unparseable URL is caught before Retrofit can throw."

**SOURCE OF TRUTH**
`ScanActivity.kt` — `runCloudDetection()`, `copyUriToCacheFile()`, `getImageMimeType()`,
`getBackendBaseUrl()`; `network/ApiService.kt`; `network/RetrofitClient.kt` (30 s timeouts,
`DEFAULT_BASE_URL = "http://10.0.2.2:8000/"`, logging levels); `backend-api/main.py`
`async def predict(image: UploadFile = File(...))`.

---

## SLIDE 13 — Pipeline 3A continued: the backend service ⭐

**PURPOSE**
Show the server as a small, defensive, well-factored four-file service.

**ON-SLIDE CONTENT**

```
PIPELINE 3A — CLOUD, SERVER SIDE  (backend-api/, FastAPI + Uvicorn)

STARTUP (import time, once)
  labels.py    load_labels(labels-38.txt) → 38 names; rejects empty/duplicate files
  model_loader.py  load_predictor(class_names) →
       USE_MOCK=true            → mock predictor
       TensorFlow import failed → predictor with model_loaded=False
       model file missing       → predictor with model_loaded=False
       otherwise                → tf.keras.models.load_model(MODEL_PATH) and VALIDATE:
                                    input  (None, 224, 224, 3)
                                    output (None, 38) == len(labels)
                                  any mismatch → logged, real inference disabled

REQUEST: POST /predict   (multipart, field "image")
  1 content_type must start with "image/"        else 400
  2 read at most MAX_IMAGE_SIZE_BYTES + 1        empty → 400 · oversize → 413 (limit 10 MB)
  3 preprocess: PIL open → convert("RGB") → resize(224, 224) → np.float32 → expand_dims
       values stay RAW 0..255 — the model's own Rescaling layer maps them to [-1, 1]
       undecodable bytes → 400 "Invalid image file supplied."
  4 if not model_loaded and not use_mock         → 503 "Real model is not loaded."
  5 predictor.predict(batch) → (model_label, confidence in [0,1] via argmax + clamp)
  6 guidance_available = model_label in DISEASE_INFO (10 reviewed entries of the 38)
  7 uncertain = confidence < CONFIDENCE_THRESHOLD (default 0.50), logged when true
  8 respond PredictionResult: model_label · disease · confidence (4 dp) · uncertain
       · guidance_available · symptoms · treatment · prevention
  finally: await image.close()  — always

OTHER ENDPOINTS
  GET /  and  GET /health  → status, use_mock, model_loaded, model_path, image_size,
                             class_count, labels_path
  GET /diseases            → the 10 reviewed guidance entries with display names

CONFIG (config.py, .env-driven): MODEL_PATH · LABELS_PATH · IMAGE_SIZE=224
  CONFIDENCE_THRESHOLD=0.50 · MAX_IMAGE_SIZE_BYTES=10 MB · USE_MOCK · PORT=8000
  ALLOWED_ORIGINS ("*" disables allow_credentials — deliberate)
```

**VISUAL / DIAGRAM SPEC**
Two stacked bands. Top band "STARTUP" with a 4-way decision fan into
mock / degraded / degraded / real. Bottom band "REQUEST" as a numbered pipeline with red side
exits labelled 400, 413, 503, 500 at the correct steps. Mark step 3 with an orange star and the
caption "raw 0..255 — parity-critical", foreshadowing Slide 15.

**SPEAKER NOTES**
"The backend is four small Python files and it is aggressively defensive. It validates the
model's tensor shapes against the label file *at startup*, so a mismatched artifact fails on boot
with a clear log rather than silently returning nonsense. It bounds the upload at ten megabytes
by reading one byte past the limit rather than trusting a header. It rejects non-images twice —
by declared content type and again by attempting a real Pillow decode, which defeats a spoofed
extension. And if the model genuinely is not loaded, it returns 503 rather than pretending.
The starred step is the one that matters most for correctness: the server does *not* divide by
255. It passes raw pixel values, because the rescaling is baked into the model graph — which is
exactly what the Android side does too."

**SOURCE OF TRUTH**
`backend-api/main.py` (`preprocess_image`, `predict`, `health_check`, `list_diseases`,
`DISEASE_INFO` with 10 entries); `backend-api/config.py`; `backend-api/model_loader.py`;
`backend-api/labels.py`; `backend-api/test_api.py` (8 tests incl. spoofed image and oversize).

---

## SLIDE 14 — Pipeline 3B: On-device inference ⭐

**PURPOSE**
Walk the offline engine with the same rigour, and highlight that it is strict by design.

**ON-SLIDE CONTENT**

```
PIPELINE 3B — OFFLINE  (ScanActivity → ml/TFLiteClassifier, 234 lines)

INVOCATION
  lifecycleScope.launch { withContext(Dispatchers.IO) {
      TFLiteClassifier(context).use { it.classify(loadBitmap(uri)) } } }
  — constructed per detection, closed by `use { }`, run off the main thread

CONSTRUCTION (fail fast, never guess)
  1 loadLabels("labels.txt")  — trims, skips blanks and '#' comments; empty → IOException
  2 loadGuidance()            — parses assets/diseases.xml into a display-name → guidance map
                                (optional: failure is logged, not fatal)
  3 initializeModel("model.tflite")
      assets.openFd → FileChannel.map(READ_ONLY, offset, length)   ← memory-mapped, not copied
      Interpreter(buffer, Options().setNumThreads(4))
      ASSERT input  shape == [1, 224, 224, 3]     else IOException
      ASSERT output shape == [1, N] and N == labels.size (38)  else IOException
      any failure → close() then throw — no silent degradation

INFERENCE
  preprocessImage(bitmap)
      Bitmap.createScaledBitmap(224, 224, filter=true)
      direct ByteBuffer, nativeOrder, 224·224·3·4 bytes
      per pixel: putFloat(R), putFloat(G), putFloat(B)   ← RAW 0..255, NOT divided by 255
  interpreter.run(inputBuffer, Array(1){ FloatArray(38) })
  argmax → bestIndex · confidence = scores[bestIndex] · modelLabel = labels[bestIndex]

OUTPUT
  displayLabel(modelLabel) → human name · guidance lookup → symptoms/treatment/prevention
  (missing guidance → three generic, explicitly non-diagnostic strings)
  returns PredictionResponse — the same type the network layer returns

FAILURE SURFACE (by design)
  No model.tflite in assets → IOException → Toast "offline prediction failed: <reason>"
  The build gate on Slide 26 exists so this never reaches a release build.
```

**VISUAL / DIAGRAM SPEC**
Three-band diagram: CONSTRUCT (with three assertion gates drawn as narrowing funnels), INFER
(bitmap → 224×224 grid → ByteBuffer → interpreter → 38-bar score chart → argmax arrow), OUTPUT
(PredictionResponse card). Place an orange star on the `putFloat(R/G/B)` line, matching the star
on Slide 13 — visually connecting the two preprocessing sites.

**SPEAKER NOTES**
"The offline classifier is deliberately strict. It memory-maps the model rather than copying it
into heap, runs on four threads, and validates the input and output tensors against the label
file before it will accept the interpreter. If the model is missing or has the wrong number of
classes, it throws — it does not fall back to a heuristic and it does not guess, because a wrong
answer presented confidently is worse than an error message. The starred line is the twin of the
starred line on the previous slide: raw red, green, blue floats from zero to two-fifty-five. If
either side had normalised, the two engines would silently disagree."

**SOURCE OF TRUTH**
`ml/TFLiteClassifier.kt` — constructor, `initializeModel()`, `loadLabels()`, `loadGuidance()`,
`preprocessImage()`, `classify()`, `argmax()`, `displayLabel()`, `close()`; `ScanActivity.kt`
`runOfflineDetection()`, `loadBitmap()`.

---

## SLIDE 15 — The parity contract: why both engines agree ⭐⭐

**PURPOSE**
The technical centrepiece. Prove that "two engines, one answer" is enforced, not hoped for.

**ON-SLIDE CONTENT**

```
THE PARITY CONTRACT — ENFORCED IN FOUR PLACES

                      ANDROID (Kotlin)              BACKEND (Python)
  Resize              Bitmap.createScaledBitmap     PIL Image.resize
                      224 × 224, filtered           (224, 224)
  Colour              RGB via Color.red/green/blue  .convert("RGB")
  Dtype               float32 (ByteBuffer)          np.float32
  Range               RAW 0 .. 255                  RAW 0 .. 255      ← identical, on purpose
  Normalisation       NONE in app code              NONE in server code
                      → performed by the model's embedded Rescaling layer:
                         scale = 1/127.5, offset = -1     (0 → -1, 127.5 → 0, 255 → +1)
  Tensor in           [1, 224, 224, 3] float32      (None, 224, 224, 3) float32
  Tensor out          [1, 38] float32               (None, 38) float32
  Decision            argmax over 38                argmax over 38
  Label source        assets/labels.txt             backend-api/labels-38.txt
                      ← both byte-identical copies of model/labels-38.txt →

WHO ENFORCES IT
  model/model_contract.py   refuses to convert a model whose embedded rescaling does not
                            probe correctly at 0 → -1, 127.5 → 0, 255 → +1
  TFLiteClassifier.kt       refuses an interpreter with the wrong shapes or class count
  model_loader.py           refuses a Keras model with the wrong shapes or class count
  app/build.gradle          release builds fail unless assets/model.tflite starts with the
                            "TFL3" FlatBuffer magic AND labels.txt has exactly 38 unique lines

MEASURED RESULT (30 real PlantVillage images, 2026-07-16)
  Keras vs TFLite top-class agreement ....... 30 / 30
  Maximum confidence delta .................. 0.000006
```

**VISUAL / DIAGRAM SPEC**
Central two-column comparison table with a bright vertical "=" spine between the columns.
Beneath it, four small shield icons labelled with the four enforcers. Bottom-right: the two
measured numbers in large type on a green panel.

**SPEAKER NOTES**
"This is the slide I would defend hardest. Two engines only behave identically if every
preprocessing step matches exactly — and the most common way that breaks is one side dividing by
two-fifty-five and the other not. This project solves it by moving normalisation *inside the
model graph*, so neither side can get it wrong, and then by refusing to convert any model that
doesn't prove the embedded rescaling behaves correctly — the conversion script literally probes
the graph with zero, one-two-seven-point-five and two-fifty-five and checks it gets minus one,
zero and plus one. Four independent components enforce the contract, including the Gradle build
itself. The measured outcome on thirty real leaf images: identical top class every time, with a
maximum confidence difference of six millionths."

**SOURCE OF TRUTH**
`model/model_contract.py` (`find_embedded_rescaling` probe values, `validate_shape`,
`tensor_details`, `EXPECTED_CLASS_COUNT = 38`); `ml/TFLiteClassifier.kt` `initializeModel()`;
`backend-api/model_loader.py`; `android-app-kotlin/app/build.gradle` task `validateReleaseModel`
(TFL3 header + 38 unique labels, wired to `preReleaseBuild`);
`docs/evidence/week-12/model-validation-2026-07-16.md`.

---

## SLIDE 16 — Convergence: one contract object ⭐

**PURPOSE**
Make the merge point concrete, field by field, and explain the label-normalisation join.

**ON-SLIDE CONTENT**

```
CONVERGENCE — data class PredictionResponse (com.leafguard.network)

  field               JSON key (@SerializedName)   filled by cloud     filled by offline
  modelLabel          model_label                  server              labels[argmax]
  disease             disease                      display_label(...)  displayLabel(...)
  confidence          confidence                   rounded, 4 dp       raw float score
  uncertain           uncertain                    server threshold    always false → set by app
  guidanceAvailable   guidance_available           label ∈ DISEASE_INFO  guidance map hit
  symptoms            symptoms                     DISEASE_INFO / generic  diseases.xml / generic
  treatment           treatment                    "                    "
  prevention          prevention                   "                    "

THE LABEL NORMALISATION JOIN (implemented identically on both sides)
    "Tomato___Late_blight"                  → "Tomato Late Blight"
    "Corn___Cercospora_leaf_spot Gray_leaf_spot" → "Corn Gray Leaf Spot"
    default rule: replace "___" with a space, then "_" with a space

WHY IT MATTERS
  The display name is also the LOOKUP KEY into the guidance data. Model labels are machine
  strings; the library and the guidance files are written in human strings. This function is
  the bridge — and it must stay character-for-character identical in Kotlin, in Java, and in
  Python, or guidance silently disappears from results.

COVERAGE, STATED HONESTLY
  38 classes can be PREDICTED.  10 classes have REVIEWED human guidance.
  The other 28 return explicit generic text: "Detailed symptoms and treatment guidance are
  not available in this version" + "verify with a local agricultural expert".
```

**VISUAL / DIAGRAM SPEC**
Two inbound arrows (green "offline", blue "cloud") converging on one large card representing the
`PredictionResponse` object with its eight fields listed. Below, a small mapping table for label
normalisation. Bottom banner: a 38-segment bar with 10 segments filled green and 28 grey,
captioned "predictable vs. curated guidance".

**SPEAKER NOTES**
"Here is the merge in detail. Eight fields, one class, three jobs — it is the Gson model for the
server, the return type of the on-device classifier, and the payload carried into the result
screen. The subtle part is the label join: the model speaks in strings like
`Tomato underscore underscore underscore Late underscore blight`, but the disease library and the
guidance text are written for humans. That normalisation function exists in Kotlin, in Java and
in Python, and it has to agree character-for-character or the guidance lookup silently misses. And
we are explicit about coverage: thirty-eight classes are predictable, ten have expert-reviewed
guidance, and the remaining twenty-eight say so plainly instead of pretending."

**SOURCE OF TRUTH**
`network/PredictionResponse.kt`; `ml/TFLiteClassifier.kt` `displayLabel()` + `GENERIC_*`
constants; `backend-api/labels.py` `display_label()`; `backend-api/main.py` `DISEASE_INFO`
(10 entries) and its generic fallback dict.

---

## SLIDE 17 — The uncertainty gate ⭐

**PURPOSE**
Show the safety mechanism. This is the product's ethical position expressed as code.

**ON-SLIDE CONTENT**

```
THE UNCERTAINTY GATE — a low-confidence result is REWRITTEN, not decorated

Two independent checks, both must pass:

  SERVER SIDE   uncertain = confidence < CONFIDENCE_THRESHOLD          (default 0.50)
                returned as a boolean field, and logged server-side

  CLIENT SIDE   ScanActivity.openResult():
                  uncertain = prediction.uncertain
                              || confidence × 100 < SharedPreferences[pref_confidence_threshold]
                  (user-tunable, default 50, set with a SeekBar in Settings)

WHEN THE GATE TRIPS, the object is overwritten before it ever reaches the UI:
    disease     → "Uncertain / retake image (top match: <name>)"
    symptoms    → "Confidence is below your configured threshold, so this result should
                   not be treated as a diagnosis."
    treatment   → "Retake the image in good light with one leaf filling the frame, then
                   verify with a local agricultural expert."
    prevention  → "Do not take treatment action based only on this uncertain result."
    uncertain   → true
  …plus a long Toast warning. The user still sees the top match — but framed as a hint.

DESIGN PRINCIPLE
  The app degrades to ADVICE, never to a confident wrong diagnosis.
  Because the rewrite happens before persistence, an uncertain scan is stored as uncertain.
```

**VISUAL / DIAGRAM SPEC**
Split panel. Left: a confident result card (disease name, 97%, full guidance). Right: the same
scan below threshold, rendered as the rewritten "Uncertain / retake image" card. A vertical
threshold slider between them showing the 50% default. Add a small note: "threshold is
user-adjustable in Settings, 0–100."

**SPEAKER NOTES**
"This is where the product takes a position. Many classifiers show 'Tomato Late Blight, 31%' and
leave the user to interpret it. LeafGuard does not: below the threshold, the result object itself
is rewritten to say 'uncertain, retake the image, verify with an expert'. Two independent checks
guard it — the server's own threshold and the user's configurable one — and crucially the rewrite
happens *before* the object reaches the result screen, which means an uncertain scan is also
*saved to history* as uncertain. The record can never be re-read later as if it had been
confident."

**SOURCE OF TRUTH**
`ScanActivity.kt` `openResult()`, `getConfidencePercentage()`, `getConfidenceThreshold()`;
`strings.xml` `uncertain_prediction_title/details/action/prevention`,
`low_confidence_warning`; `backend-api/main.py` uncertainty computation and log;
`backend-api/config.py` `CONFIDENCE_THRESHOLD = 0.50`; `SettingsActivity.kt`
`DEFAULT_CONFIDENCE_THRESHOLD = 50`.

---

## SLIDE 18 — Cloud vs offline: an engineering comparison

**PURPOSE**
Justify keeping both paths, with the trade-offs stated plainly.

**ON-SLIDE CONTENT**

```
                          OFFLINE (default)              CLOUD
  Runs on                 The phone, TFLite Interpreter  FastAPI server, Keras
  Needs network           No                             Yes
  Needs a server          No                             Yes (URL set in Settings)
  Model artifact          assets/model.tflite (~9.06 MB) models/leafguard_model.keras (~25.1 MB)
  Threads                 4 (Interpreter Options)        Server-side
  Latency                 Device-bound, no round trip    Network + server bound
  Timeouts                None (local call)              30 s connect/read/write
  Privacy                 Image never leaves the device  Image is uploaded
  Upgradability           Requires a new APK             Swap the server model, no app update
  Guidance source         assets/diseases.xml (10)       DISEASE_INFO in main.py (10)
  Failure mode            Clear IOException + Toast      HTTP 400/413/503/500 + Toast
  Concurrency model       Coroutine + Dispatchers.IO     Retrofit enqueue callback

WHY KEEP BOTH
  Offline guarantees the product works in the field and keeps images private.
  Cloud allows a bigger model and instant model upgrades without shipping an APK.
  Because they share one contract, supporting both costs one boolean and one class.
```

**VISUAL / DIAGRAM SPEC**
Side-by-side comparison table, green header for offline, blue for cloud. Add two badges: a
padlock on the offline column ("image never leaves the device") and a cloud-refresh icon on the
cloud column ("model upgradable without an app release").

**SPEAKER NOTES**
"Neither path dominates. Offline wins on availability and privacy; cloud wins on model size and
upgradability — you can retrain and redeploy the server model without asking a single user to
update their app. Keeping both is normally expensive, but here it costs one boolean and one
shared data class, because the parity contract already guarantees they agree."

**SOURCE OF TRUTH**
`docs/evidence/week-12/model-validation-2026-07-16.md` (TFLite 9,056,916 bytes; Keras
25,143,175 bytes); `RetrofitClient.kt` timeouts; `TFLiteClassifier.kt`
`setNumThreads(4)`; `ScanActivity.kt` both detection methods.

---
# SECTION D — POST-INFERENCE PIPELINES

## SLIDE 19 — Pipeline 4: Result presentation and sharing ⭐

**PURPOSE**
Show the hand-off mechanism (Intent extras) and the outbound integration (share Intent).

**ON-SLIDE CONTENT**

```
PIPELINE 4 — PRESENT  (ScanActivity → ResultActivity)

HAND-OFF: six Intent extras, keys declared as companion constants on ResultActivity
    EXTRA_DISEASE_NAME  EXTRA_CONFIDENCE  EXTRA_SYMPTOMS
    EXTRA_TREATMENT     EXTRA_PREVENTION  EXTRA_IMAGE_URI
  (the object is flattened into primitives — no Parcelable, no shared singleton, no
   global state; the screen is reconstructible from its Intent alone)

RENDER
    confidencePercent = (confidence × 100).roundToInt()
    textDiseaseName       ← disease name (or the rewritten "Uncertain / retake image")
    textConfidenceValue   ← R.string.confidence_format
    progressConfidence    ← the same integer, as a progress bar
    textSymptoms / textTreatment / textPrevention   ← guidance, or the placeholder strings
    Missing extra → localised placeholder, never a blank screen or a crash

THREE ACTIONS
  SHARE            Intent.ACTION_SEND, type text/plain, EXTRA_SUBJECT + a formatted
                   template (name, %, symptoms, treatment, prevention) → createChooser
                   → WhatsApp, SMS, email, any installed handler
  SAVE TO HISTORY  disables the button, writes a ScanRecord via the suspend DAO,
                   Toast + relabels the button "Saved"   (explicit user action, not automatic)
  BACK HOME        MainActivity with FLAG_ACTIVITY_CLEAR_TOP | SINGLE_TOP → no stack growth
```

**VISUAL / DIAGRAM SPEC**
Left: annotated phone mock-up of the result screen (name, % + bar, three guidance cards, three
buttons). Right: three arrows out — one to a share-sheet icon grid, one to a database cylinder,
one back to the Home screen thumbnail. Label the incoming arrow "6 Intent extras".

**SPEAKER NOTES**
"The result screen is intentionally dumb: it receives six primitives in an Intent and renders
them. It holds no reference to the classifier, no network client, and no global state, so it can
be recreated by the system at any time — after a rotation, or after being killed in the
background — purely from its Intent. Sharing uses the standard `ACTION_SEND` chooser, so the
diagnosis can go to WhatsApp or SMS without the app integrating with any of them. And saving is a
deliberate user action: nothing is written to the database unless the user asks for it."

**SOURCE OF TRUTH**
`ResultActivity.kt` — companion `EXTRA_*` keys, `readIntentExtras()`, `renderResult()`,
`shareResult()`, `saveToHistory()`, `navigateBackHome()`; `strings.xml`
`share_result_template`, `share_subject`, `share_chooser_title`, `confidence_format`,
`placeholder_*`.

---

## SLIDE 20 — Pipeline 5: Persistence with Room ⭐

**PURPOSE**
Present the local data layer as a complete, versioned, migration-safe subsystem.

**ON-SLIDE CONTENT**

```
PIPELINE 5 — PERSIST  (com.leafguard.database, Room 2.6.1 + kapt)

ENTITY   @Entity(tableName = "scan_history")   data class ScanRecord
   id            Long   @PrimaryKey(autoGenerate = true)
   disease_name  String?      confidence  Float
   symptoms      String?      treatment   String?      prevention  String?
   image_uri     String?      latitude    Double       longitude   Double
   timestamp     Long   (System.currentTimeMillis())

DAO      @Dao interface ScanDao — every method is a `suspend fun`
   insertScan(record): Long                 @Insert(onConflict = REPLACE)
   getAllScans(): List<ScanRecord>          SELECT * FROM scan_history ORDER BY timestamp DESC
   getRecentScans(limit): List<ScanRecord>  … LIMIT :limit
   getScanById(id): ScanRecord?             … WHERE id = :id LIMIT 1
   deleteScan(record)                       @Delete
   deleteScanById(id)                       DELETE FROM scan_history WHERE id = :id

DATABASE @Database(entities = [ScanRecord::class], version = 1, exportSchema = false)
   File: leafguard.db · thread-safe singleton (@Volatile + synchronized double-checked locking)
   NO fallbackToDestructiveMigration — a schema change without a migration FAILS LOUDLY
   rather than silently deleting a user's saved scans. That was a deliberate hardening fix.

CONSUMERS
   ResultActivity  insert            HistoryActivity      getAllScans, deleteScanById
   MainActivity    getAllScans.size  HistoryDetailActivity getScanById, deleteScanById
   AnalyticsActivity getAllScans (count, mean confidence, modal disease)
```

**VISUAL / DIAGRAM SPEC**
Classic three-layer Room diagram: Activities (green) → `ScanDao` (green, interface icon) →
`AppDatabase` (green) → `leafguard.db` (grey cylinder). Alongside, render the `scan_history`
table as an actual table header with the ten column names. Add a red-outlined callout on
`AppDatabase`: "no destructive migration fallback — user data is never silently dropped".

**SPEAKER NOTES**
"Room gives us a compile-time-verified SQLite layer: the SQL in those annotations is checked when
the app builds, so a typo in a column name is a build error rather than a crash in a user's
field. Every DAO method is a suspend function, which means Room enforces that queries run off the
main thread. And note one line that isn't there: `fallbackToDestructiveMigration`. It was
deliberately removed, so if a future version changes the schema without providing a migration,
the app fails loudly at development time instead of quietly wiping a farmer's scan history."

**SOURCE OF TRUTH**
`database/ScanRecord.kt`, `database/ScanDao.kt`, `database/AppDatabase.kt`;
`docs/release-validation-v0.2.0-beta.md` ("Silent destructive Room migration fallback removed").

---

## SLIDE 21 — Pipeline 6: History and detail

**PURPOSE**
Show list/detail navigation, RecyclerView usage, and the lifecycle-aware refresh.

**ON-SLIDE CONTENT**

```
PIPELINE 6 — HISTORY  (HistoryActivity → HistoryDetailActivity)

HISTORY LIST
  onCreate / onResume → lifecycleScope.launch { dao.getAllScans() } → renderHistory()
      (refreshing in onResume is what keeps the list correct after a delete or a new save)
  RecyclerView + LinearLayoutManager + HistoryAdapter(items, onItemSelected, onDeleteRequested)
  Row (item_scan_history.xml): disease name · confidence % · formatted date/time · delete button
      DateFormat.getDateTimeInstance(MEDIUM, SHORT).format(Date(timestamp))
  Empty state: RecyclerView hidden, textEmptyState shown — never a blank screen
  Delete: deleteScanById → re-query → Toast naming the deleted disease → re-render

DETAIL
  Navigation carries ONE value:  putExtra(EXTRA_SCAN_ID, record.id)   // Long
  HistoryDetailActivity re-queries the database by id rather than passing the whole object
      → the detail screen always shows current data and survives process death
  id == -1L → Toast + finish()      record == null → Toast + finish()
  Renders name, confidence (clamped to 0..100), long-format timestamp, guidance
  Actions: share (same template as ResultActivity) · delete (then finish())

DESIGN NOTE
  Pass identifiers between screens, not objects. The database stays the single source of truth.
```

**VISUAL / DIAGRAM SPEC**
Two phone frames side by side (list → detail) with an arrow labelled `EXTRA_SCAN_ID: Long`. From
the detail frame, draw a dashed arrow back down to the `leafguard.db` cylinder labelled
"re-query by id". Inset: the empty-state variant of the list screen.

**SPEAKER NOTES**
"Standard list-detail, with one decision worth defending: the list passes only the row's primary
key, and the detail screen re-queries the database. That costs one cheap indexed lookup and buys
correctness — the detail view can never display a stale copy of a record, and it restores itself
correctly if Android kills the process while it is in the background."

**SOURCE OF TRUTH**
`HistoryActivity.kt` (`loadHistory`, `renderHistory`, `deleteRecord`, `HistoryAdapter`,
`onResume`); `HistoryDetailActivity.kt` (`EXTRA_SCAN_ID`, `loadRecord`, `renderRecord`,
`shareCurrentRecord`, `deleteRecord`); `res/layout/item_scan_history.xml`.

---

## SLIDE 22 — Pipeline 7: On-device analytics

**PURPOSE**
Show that insight is derived locally — a privacy statement expressed as an architecture.

**ON-SLIDE CONTENT**

```
PIPELINE 7 — AGGREGATE  (AnalyticsActivity)

INPUT   the same dao.getAllScans() list — no separate analytics store, no events table

THREE DERIVED METRICS, computed in memory on each onCreate/onResume
   Total scans        records.size
   Mean confidence    records.map { confidence }.average() × 100, rounded, coerced to 0..100
   Most frequent      records.groupingBy { diseaseName }.eachCount().maxByOrNull { it.value }?.key
   Empty history      → "no data" strings + a visible empty-state message

WHAT IS DELIBERATELY ABSENT
   No analytics SDK. No Firebase. No crash reporter. No event upload. No user identifier.
   The class KDoc states the intent explicitly: summarise local history
   "without sending analytics data off-device".

WHY THIS MATTERS
   Scan history is agricultural data about a specific person's specific field.
   Treating it as local-only is a privacy decision, and it is enforced by the fact that
   the app declares only INTERNET, CAMERA and POST_NOTIFICATIONS.
```

**VISUAL / DIAGRAM SPEC**
Dashboard mock with three stat tiles (Total / Mean confidence / Most frequent). Beside it, a
diagram of the phone with an outbound arrow to a cloud, crossed out in red, captioned "analytics
never leaves the device".

**SPEAKER NOTES**
"Analytics here means something unusual: it is computed from the same local table, in memory,
every time the tab is opened, and it never leaves the phone. There is no analytics SDK in the
dependency list at all. For a product whose data is 'which diseases appeared in this person's
field, and when', that is the right default."

**SOURCE OF TRUTH**
`AnalyticsActivity.kt` (`loadAnalytics()` and its KDoc); `android-app-kotlin/app/build.gradle`
(no analytics/crash-reporting dependency); `AndroidManifest.xml` (three permissions only).

---

## SLIDE 23 — Pipeline 8: The offline disease library

**PURPOSE**
Show the second offline data pipeline — XML parsing — and where the app degrades gracefully.

**ON-SLIDE CONTENT**

```
PIPELINE 8 — REFERENCE  (DiseaseLibraryActivity)

SOURCE   app/src/main/assets/diseases.xml   (10 curated entries, shipped in the APK)
   <diseases><disease>
       <name/> <plant/> <symptoms/> <treatment/> <prevention/> <severity/>
   </disease>…</diseases>

PARSE    XmlPullParserFactory → XmlPullParser, streaming event loop
   START_TAG "disease" → begin a DiseaseEntry
   TEXT                → assign to the field named by the current tag
   END_TAG "disease"   → append the entry
   Chosen over DOM (loads the whole tree into memory) and SAX (callback-driven);
   pull parsing is the Android-recommended, memory-efficient option.

DISPLAY  RecyclerView + DiseaseAdapter, row = item_disease_library.xml
   name · plant · symptoms · severity Chip, tinted by severity
       high → leaf_green_500 · low → leaf_green_100 · otherwise → leaf_green_300

SEARCH   TextWatcher.afterTextChanged → filterDiseases(query)
   case-insensitive substring match on disease name OR plant name
   zero matches → list hidden, "empty library" message shown

RESILIENCE  IOException or XmlPullParserException → getFallbackDiseaseList()
   5 hard-coded entries so the screen is never empty or broken
   (FALLBACK_DISEASE_COUNT = 5 is also what the Home dashboard's Library card displays)

SECOND CONSUMER  TFLiteClassifier parses the SAME file into a display-name → guidance map,
   which is how an offline prediction acquires symptoms, treatment and prevention text.
```

**VISUAL / DIAGRAM SPEC**
Left: an XML snippet card. Centre: a parser icon with the three event types listed. Right: the
rendered library list with a search box and coloured severity chips. Draw a second arrow from the
XML card down to a `TFLiteClassifier` box, labelled "same asset, second consumer" — this reuse is
the point of the slide.

**SPEAKER NOTES**
"The disease library demonstrates XML parsing on Android, but architecturally the interesting
part is that `diseases.xml` has two consumers: the library screen renders it, and the offline
classifier parses the same file into a lookup map so that an on-device prediction can be enriched
with symptoms and treatment without any network call. One asset, two pipelines. And if the asset
is missing or malformed, the screen falls back to five built-in entries rather than failing."

**SOURCE OF TRUTH**
`DiseaseLibraryActivity.kt` (`loadDiseases`, `parseDiseaseXml`, `filterDiseases`,
`getFallbackDiseaseList`, `FALLBACK_DISEASE_COUNT = 5`, `DiseaseAdapter`);
`ml/TFLiteClassifier.kt` `loadGuidance()`/`parseGuidance()`;
`assets/diseases.xml` (10 `<disease>` entries); `MainActivity.kt` `textLibraryCount`.

---

## SLIDE 24 — Pipelines 9 & 10: Configuration and notifications

**PURPOSE**
Cover the control plane and the re-engagement channel; both are small but architecturally real.

**ON-SLIDE CONTENT**

```
PIPELINE 9 — CONFIGURE  (SettingsActivity, the "About" tab)

  Backend URL           EditText + TextWatcher → prefs.putString(pref_backend_url).apply()
                        saved on every keystroke; default http://10.0.2.2:8000
  Confidence threshold  SeekBar; label updates live on progress change,
                        value persisted on onStopTrackingTouch → putInt(pref_confidence_threshold)
  App version           read at runtime from PackageManager, never hard-coded
  Reset to defaults     restores both keys and refreshes the controls

  WHY apply() NOT commit(): apply() writes asynchronously and never blocks the UI thread.
  WHY SharedPreferences: two small scalars; a Room table would be over-engineering.
  REACH: ScanActivity reads BOTH keys on every detection, so a settings change takes
         effect on the very next scan with no restart and no event bus.

PIPELINE 10 — NOTIFY  (utils/NotificationHelper, a Kotlin `object`)

  createChannel(context)   channel id "leafguard_scan_reminders", IMPORTANCE_DEFAULT,
                           created on Android 8.0+ (guarded by SDK_INT >= O)
                           called from MainActivity.onCreate — the channel exists before use
  sendScanReminderNotification(context, title, message)
                           NotificationCompat + BigTextStyle
                           PendingIntent → MainActivity, FLAG_UPDATE_CURRENT | FLAG_IMMUTABLE
                           notification id / request code 1001
                           On Android 13+ (TIRAMISU): returns silently if POST_NOTIFICATIONS
                           has not been granted — no crash, no SecurityException
  Permission request: MainActivity asks for POST_NOTIFICATIONS on first launch, API 33+ only
```

**VISUAL / DIAGRAM SPEC**
Two half-panels. Left: Settings screen mock with arrows from each control to a
`SharedPreferences` grey cylinder, and from that cylinder to `ScanActivity` and
`RetrofitClient`. Right: notification-channel flow — `MainActivity.onCreate` → `createChannel` →
system tray notification → `PendingIntent` arrow back into `MainActivity`.

**SPEAKER NOTES**
"Settings is the control plane. Two keys — the backend URL and the confidence threshold — and
both are read fresh by ScanActivity on every single detection, so a change applies to the next
scan without a restart or any notification mechanism between the screens. Notifications show the
modern Android permission reality: channels are mandatory from Android 8, `FLAG_IMMUTABLE` is
mandatory from Android 12, and runtime permission is mandatory from Android 13 — and the helper
handles all three, returning silently rather than throwing if permission was refused."

**SOURCE OF TRUTH**
`SettingsActivity.kt` (companion constants, `loadCurrentSettings`, `setupListeners`,
`resetToDefaults`); `ScanActivity.kt` `getBackendBaseUrl()`, `getConfidenceThreshold()`;
`utils/NotificationHelper.kt`; `MainActivity.kt` `requestNotificationPermissionIfNeeded()`,
`NotificationHelper.createChannel(this)`.

---

# SECTION E — SUPPLY CHAIN AND DELIVERY

## SLIDE 25 — The model supply chain ⭐⭐

**PURPOSE**
Show that the model is a governed, reproducible artifact — not a mystery file dropped into
`assets/`. This is the most differentiating engineering slide in the deck.

**ON-SLIDE CONTENT**

```
THE MODEL IS NOT COMMITTED. IT IS PRODUCED BY A REPRODUCIBLE, RECORDED PIPELINE.

 1  SOURCE & PIN      Upstream: Muhammad-Hassan12/Plant-Disease-Detector (MIT)
                      Artifact: Models/model_4_mobilenet_finetuned.keras
                      Pinned commit: f6165bd93524dfb77a9629aae70db845832d1b01
                      → downloaded from the PINNED raw URL, not from a moving branch

 2  VERIFY            size 25,143,175 bytes
                      SHA-256 08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f
                      remote main, pinned download and local file: byte-for-byte identical
                      licence reviewed and recorded in release-records/model-provenance.txt

 3  INSPECT           model/inspect_model.py → Keras 3.10.0, float32
                      input [None,224,224,3], output [None,38]

 4  CONTRACT-CHECK    model/model_contract.py
                      validate_keras_model(...)  shapes + dtype vs the 38 canonical labels
                      find_embedded_rescaling(...) probes the graph:
                          0 → -1,  127.5 → 0,  255 → +1
                      FAILS THE BUILD if the embedded preprocessing is not exactly that

 5  CONVERT & SYNC    model/convert_model.py → TFLiteConverter.from_keras_model
                      writes model.tflite into BOTH android-app/ and android-app-kotlin/ assets
                      copies labels-38.txt into backend-api/ and BOTH assets/labels.txt
                      → one command, four synchronised destinations, no manual copying

 6  VALIDATE          model/validate_tflite.py  single-image sanity run
                      model/parity_test.py      Keras vs TFLite, fails above a 0.02 delta
                      model/test_model_contract.py  unit tests for the contract itself

 7  GATE              app/build.gradle :validateReleaseModel (wired to preReleaseBuild)
                      TFL3 FlatBuffer magic present · exactly 38 unique labels
                      → a release APK CANNOT be built with a placeholder or mismatched model

 MEASURED OUTPUT      TFLite 9,056,916 bytes
                      SHA-256 22ea2d4a47a52b2d9b150e0f74b113def0f12bbdb59209f7e0bce2a9701d41f9
                      Kotlin and Java asset copies: byte-for-byte identical
```

**VISUAL / DIAGRAM SPEC**
A horizontal 7-stage supply-chain conveyor, orange, left to right, with a gate/shield icon at
stages 4 and 7. Under stages 2 and "measured output", show the two SHA-256 values in small
monospace. Use **dashed** arrows throughout — this is a build-time pipeline, not runtime — and
end with two dashed arrows landing in the green Android box and the blue backend box.

**SPEAKER NOTES**
"This is the slide I would show a reviewer who asks 'where did your model come from?'. It was not
trained here and the repository says so. It was downloaded from a specific upstream commit — not
a branch, a commit — hashed, licence-reviewed, and recorded in a provenance file with the
downloader's name and date. Then it is inspected, contract-checked, converted, and synchronised
into four destinations by a single script, so the Kotlin app, the Java app and the backend can
never drift apart. And the final gate is in Gradle itself: a release build fails if
`model.tflite` doesn't start with the TensorFlow Lite magic bytes or if the label file isn't
exactly thirty-eight unique lines. The model artifacts are git-ignored on purpose — the
repository ships the *pipeline*, not the binary."

**SOURCE OF TRUTH**
`release-records/model-provenance.txt`; `model/inspect_model.py`; `model/model_contract.py`;
`model/convert_model.py`; `model/validate_tflite.py`; `model/parity_test.py`;
`model/test_model_contract.py`; `android-app-kotlin/app/build.gradle` `validateReleaseModel` +
`afterEvaluate { preReleaseBuild dependsOn }`; `.gitignore`;
`docs/evidence/week-12/model-validation-2026-07-16.md`.

---

## SLIDE 26 — Build, CI and deployment

**PURPOSE**
Show that both tracks and the backend are continuously verified, and how the service ships.

**ON-SLIDE CONTENT**

```
CONTINUOUS INTEGRATION  (.github/workflows/validate.yml, on push and PR to main)

  JOB "android"  — matrix over BOTH tracks: [android-app-kotlin, android-app]
      JDK 17 (Temurin) + Android SDK, Gradle cache
      ./gradlew testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest
      Uploads the Kotlin debug APK as a build artifact (fails if absent)

  JOB "backend"  — Python 3.11, pip cache
      python -m compileall -q .
      python -m unittest -v test_api.py

ANDROID BUILD FACTS
  Gradle wrapper 8.2 · AGP 8.2.0 · Kotlin 1.9.22 · kapt for Room
  compileSdk/targetSdk 34 · minSdk 24 · Java 11 source/target · jvmTarget 11
  viewBinding true · buildConfig true · aaptOptions { noCompress "tflite" }
      ← the model must stay uncompressed so it can be MEMORY-MAPPED from the APK
  release: minifyEnabled false, proguard-android-optimize + proguard-rules.pro
  release additionally gated by :validateReleaseModel (Slide 25)

BACKEND DEPLOYMENT
  Dockerfile: python:3.11-slim · ARG INSTALL_TENSORFLOW=false (TF is opt-in, keeps the
  image small for mock/dev) · HEALTHCHECK polls /health every 30 s ·
  CMD uvicorn main:app --host 0.0.0.0 --port $PORT
  Local dev: uvicorn main:app --reload from inside backend-api/

VERIFIED IN THE 2026-07-16 RUN
  Both tracks: unit tests, lint, debug APK, release APK, 38-label gate, instrumentation APK
  Docker image built with INSTALL_TENSORFLOW=true; container /health reported the real
  38-class model; /predict returned a real classification
```

**VISUAL / DIAGRAM SPEC**
CI pipeline diagram: a `git push` node fanning into three parallel lanes (Kotlin, Java, Backend),
each with its step chips, converging on a green "artifact + green build" node. Separate small
panel on the right for the Docker deployment path (image → container → healthcheck loop).

**SPEAKER NOTES**
"Continuous integration runs a matrix over both application tracks, so the Java twin cannot
silently rot while the Kotlin app moves forward — a break in either fails the build. One Gradle
setting deserves a mention: `noCompress "tflite"`. Without it, the APK would compress the model
and the classifier could not memory-map it directly out of the package, which is what keeps the
model out of the Java heap. The backend ships as a Docker image where TensorFlow is an opt-in
build argument, so a lightweight mock image and a full inference image come from the same
Dockerfile."

**SOURCE OF TRUTH**
`.github/workflows/validate.yml`; `android-app-kotlin/app/build.gradle`;
`android-app-kotlin/build.gradle`; `android-app-kotlin/gradle/wrapper/gradle-wrapper.properties`;
`backend-api/Dockerfile`; `backend-api/requirements*.txt`;
`docs/evidence/week-12/model-validation-2026-07-16.md`; `docs/backend-deployment-and-maintenance.md`.

---

## SLIDE 27 — Where data lives: the residency model

**PURPOSE**
Answer, in one table, "what is stored, where, and does it ever leave the device?"

**ON-SLIDE CONTENT**

```
DATA RESIDENCY — EVERY PIECE OF STATE IN THE PRODUCT

  Data                     Location                                    Leaves device?
  Captured photo           getExternalFilesDir(PICTURES)/captures/     No
                           app-scoped; removed when the app is uninstalled
  Gallery selection        not copied; scoped content:// URI only      No
  Upload temp file         cacheDir/leafguard_upload_*.jpg             Yes — cloud mode only,
                           deleted on BOTH success and failure               and only the image
  Scan history             Room, leafguard.db, table scan_history      No
  Backend URL              default SharedPreferences                   No
  Confidence threshold     default SharedPreferences                   No
  Model                    assets/model.tflite (APK, memory-mapped)    No
  Disease guidance         assets/diseases.xml (APK)                   No
  Labels                   assets/labels.txt (APK)                     No
  Analytics                derived in memory from Room, per screen open  No
  Shared result text       user-initiated ACTION_SEND only             Only if the user shares

  NETWORK EGRESS EXISTS IN EXACTLY ONE PLACE:
      POST /predict, cloud mode, user-initiated, carrying one image.
      Offline mode produces ZERO network traffic.

  TRANSPORT POLICY (res/xml/network_security_config.xml)
      cleartext DENIED by default; permitted ONLY for
      10.0.2.2 (emulator→host), 10.0.3.2 (Genymotion), 127.0.0.1, localhost
      → any real deployment must be HTTPS. Development convenience cannot leak into production.
```

**VISUAL / DIAGRAM SPEC**
A large phone outline containing all the "No" rows as grey chips. One single arrow leaves the
phone, labelled `POST /predict — cloud mode only`, pointing to the blue backend box. Bottom
strip: the four allow-listed cleartext hosts, with a padlock icon and "everything else must be
HTTPS".

**SPEAKER NOTES**
"One arrow leaves the phone, and only if the user chose cloud mode. Everything else — history,
settings, analytics, the model, the guidance library — is local. The network security config is
worth noting: cleartext HTTP is denied by default and allow-listed only for the four addresses
that mean 'my own development machine'. That means the convenience of plain HTTP during
development physically cannot follow the app into production; a real deployment has to be HTTPS."

**SOURCE OF TRUTH**
`ScanActivity.kt` `createImageUri()`, `copyUriToCacheFile()` + `uploadFile.delete()` on both
callbacks; `res/xml/network_security_config.xml`; `res/xml/file_provider_paths.xml`;
`database/AppDatabase.kt`; `SettingsActivity.kt`; `AnalyticsActivity.kt`.

---

# SECTION F — ENGINEERING QUALITY

## SLIDE 28 — Concurrency, lifecycle and memory

**PURPOSE**
Demonstrate that the hard Android problems — main-thread work, leaks, cancellation — are handled
deliberately.

**ON-SLIDE CONTENT**

```
NOTHING SLOW RUNS ON THE MAIN THREAD. NOTHING OUTLIVES ITS SCREEN.

  Work                    Mechanism                                 Cancellation
  TFLite inference        lifecycleScope + withContext(Dispatchers.IO)  Cancelled with the Activity
  Room queries            suspend DAO in lifecycleScope             Cancelled with the Activity
  HTTP upload             Retrofit enqueue (OkHttp thread pool)     Callback checks state
  SharedPreferences write apply() — asynchronous                    n/a
  Bitmap decode           inside the IO coroutine                   Cancelled with the Activity

MEMORY AND RESOURCE DISCIPLINE
  ViewBinding stored in a nullable field, set to null in onDestroy()  → no View leak
  Every UI access re-reads `binding ?: return`                        → safe after destroy
  TFLiteClassifier implements AutoCloseable, always used via `use { }` → interpreter released
  Model memory-mapped (FileChannel.map) rather than read into heap
  Scaled bitmap recycled after preprocessing when it is a new instance
  Upload cache file deleted on both success and failure
  XML input streams closed via `use { }`
  RetrofitClient singleton rebuilt only when the base URL actually changes

LIFECYCLE CORRECTNESS
  MainActivity.onResume  → refresh the Room scan count
  HistoryActivity.onResume → reload the list  (correct after a delete in the detail screen)
  AnalyticsActivity.onResume → recompute metrics
  ResultActivity is fully reconstructible from its Intent extras

KNOWN TRADE-OFF (documented in ui/BottomNav.kt, not hidden)
  Each tab is a separate Activity and tab switching calls finish(), so the system Back button
  exits the app instead of returning to the previous tab. The fix is a single-Activity +
  Fragments + Navigation Component refactor — named in the code as future work.
```

**VISUAL / DIAGRAM SPEC**
Two-lane timeline: "Main thread" (kept clear, shown as a smooth green line) versus "IO / OkHttp
threads" (blocks of work). Show the coroutine cancelling when the Activity is destroyed. Side
panel: a checklist of the resource-discipline bullets with tick icons.

**SPEAKER NOTES**
"Three classes of Android bug are addressed structurally here. Main-thread blocking: inference,
database access and bitmap decoding all run in `Dispatchers.IO` inside `lifecycleScope`, so they
are also automatically cancelled if the user leaves the screen. Leaks: view bindings are nulled
in `onDestroy` and every access re-checks, and the interpreter is closed by a `use` block even if
inference throws. And staleness: each screen refreshes in `onResume`, which is why deleting a
record in the detail screen leaves the list correct when you come back. The one trade-off we
accept is the back-stack behaviour of Activity-based tabs, and the code comment names both the
consequence and the fix."

**SOURCE OF TRUTH**
`ScanActivity.kt` `runOfflineDetection()`; `ml/TFLiteClassifier.kt` `close()` + `AutoCloseable`;
all Activities' `onDestroy { binding = null }`; `MainActivity.kt`/`HistoryActivity.kt`/
`AnalyticsActivity.kt` `onResume()`; `ui/BottomNav.kt` KDoc.

---

## SLIDE 29 — Security and privacy posture

**PURPOSE**
Consolidate the defensive decisions already visible in earlier pipelines into one auditable list.

**ON-SLIDE CONTENT**

```
CLIENT
  Permissions: INTERNET, CAMERA, POST_NOTIFICATIONS — and nothing else
      broad media/storage and location permissions were deliberately REMOVED
      camera declared android:required="false" → installable on camera-less devices
  Only MainActivity is exported; all other Activities exported="false"
  FileProvider (grantUriPermissions, exported="false") instead of file:// URIs
  Gallery access is scoped and temporary (GetContent), never library-wide
  Cleartext HTTP denied except for four documented development hosts
  Backend URL is parsed with toHttpUrlOrNull() before use — malformed input cannot crash Retrofit
  Release logging reduced to BASIC (request line only); BODY logging is debug-only

SERVER
  Content-type must be image/*                          → 400
  Real decode attempted with Pillow (defeats spoofing)  → 400
  Upload bounded at 10 MB by reading limit + 1 byte     → 413
  Empty upload rejected                                 → 400
  Model not loaded and not mocked                       → 503 (never a fabricated answer)
  Unexpected failure logged, generic message returned   → 500 (no stack trace leaked)
  Uploaded file handle always closed in `finally`
  CORS: when ALLOWED_ORIGINS is "*", allow_credentials is forced FALSE

REPOSITORY
  .gitignore excludes *.keystore, *.jks, *.apk, .env, **/.env,
      backend-api/models/*.keras, and both tracks' assets/model.tflite
  Provenance file explicitly instructs: never commit the model binary, keystore,
      signing passwords, or private environment values
```

**VISUAL / DIAGRAM SPEC**
Three vertical columns (Client / Server / Repository) with a shield icon at each head. Colour the
server rejection codes (400, 413, 503, 500) as small red chips so the defensive surface is
scannable.

**SPEAKER NOTES**
"The permission list is the headline: three permissions, and location plus broad media access were
actively removed during hardening rather than left in 'just in case'. On the server, notice that
we validate the *content*, not just the declared type — a `.jpg` extension on a text file fails at
the Pillow decode. And notice the 503: when the real model isn't loaded, the service says so
rather than falling back to something that looks like an answer."

**SOURCE OF TRUTH**
`AndroidManifest.xml`; `res/xml/network_security_config.xml`; `RetrofitClient.kt` logging levels;
`ScanActivity.kt` `getBackendBaseUrl()`; `backend-api/main.py` (all raise paths, `finally`
close, CORS middleware); `backend-api/config.py`; `.gitignore`;
`release-records/model-provenance.txt`; `docs/release-validation-v0.2.0-beta.md`.

---

## SLIDE 30 — Testing and validation evidence

**PURPOSE**
Show what is actually proven, with numbers, separated from what is merely built.

**ON-SLIDE CONTENT**

```
AUTOMATED TESTS THAT EXIST AND RUN

  Android unit (JVM)         PredictionResponseTest — Gson parses the server JSON into
                             PredictionResponse and the disease field survives  (both tracks)
  Android instrumented       MainActivityTest, ScanActivityTest — launch and assert visibility
                             (COMPILED in CI; not executed — no device attached)
  Backend (unittest)         8 tests in backend-api/test_api.py
                               health aliases report runtime mode
                               /diseases keeps exactly 10 reviewed entries
                               /predict accepts a valid image
                               /predict returns 503 without a real model
                               preprocessing keeps RAW RGB values          ← guards Slide 15
                               /predict rejects a non-image
                               /predict rejects a spoofed image
                               /predict rejects an oversized upload
  Model contract             model/test_model_contract.py — labels, shapes, rescaling probe

MEASURED MODEL EVIDENCE  (docs/evidence/week-12/model-validation-2026-07-16.md)
  Keras ↔ TFLite top-class parity ......... 30 / 30 real PlantVillage images
  Max confidence delta .................... 0.000006
  Tomato Late Blight ...................... 10 / 10
  Tomato Healthy .......................... 10 / 10
  Tomato Early Blight ..................... 8  / 10
  Overall limited-set top-1 accuracy ...... 28 / 30 (93.3 %)
  Backend with the real model ............. /health use_mock=false, model_loaded=true,
                                            class_count=38; /predict Tomato___Late_blight 0.9977
  Both tracks .............................. unit tests, lint, debug + release APK, 38-label gate

THE TWO DOCUMENTED FAILURES (kept in the record on purpose)
  Tomato Early Blight → predicted Tomato Late Blight            confidence 0.790792
  Tomato Early Blight → predicted Tomato Septoria Leaf Spot     confidence 0.527168
  Also recorded: three synthetic illustrations were confidently misclassified as
  Blueberry Healthy (0.83–0.98) — confidence alone does not reject out-of-distribution input.
```

**VISUAL / DIAGRAM SPEC**
Left: a test-pyramid graphic (unit → integration → instrumented, with the instrumented tier
hatched and labelled "compiled, not executed"). Right: a results panel with the parity and
accuracy numbers, and a small amber box for the two documented failures.

**SPEAKER NOTES**
"I want to be precise about what is proven. Numerical parity between the two engines is proven,
thirty out of thirty, to six decimal places. Semantic accuracy is *partially* measured — twenty
eight of thirty on three tomato classes — and that is not an accuracy study for thirty-eight
classes, so we don't present it as one. The failure cases stay in the record, including the
finding that three synthetic images were confidently classified as blueberry, which is exactly
why the uncertainty gate exists and exactly why it is not sufficient on its own."

**SOURCE OF TRUTH**
`app/src/test/java/com/leafguard/network/PredictionResponseTest.kt`;
`app/src/androidTest/java/com/leafguard/{MainActivityTest,ScanActivityTest}.kt`;
`backend-api/test_api.py`; `model/test_model_contract.py`;
`docs/evidence/week-12/model-validation-2026-07-16.md`; `docs/release-validation-v0.2.0-beta.md`.

---

## SLIDE 31 — Limitations and the release gate ⭐

**PURPOSE**
State the remaining gap between "works" and "releasable". Presenting this *builds* credibility.

**ON-SLIDE CONTENT**

```
WHAT IS NOT YET DONE — STATED PLAINLY

  ✗ No Android device or emulator was attached during validation
      → instrumented tests compiled but did not run
      → camera, gallery, airplane-mode inference and install-over-upgrade are UNVERIFIED
  ✗ Accuracy measured on only 3 of 38 classes (30 tomato images), not leaf-group separated
  ✗ No production signing key; release APKs are unsigned or debug-signed
  ✗ The published v0.2.0-beta GitHub Release asset is the HISTORICAL PLACEHOLDER build
      (SHA-256 abbreviated for the slide as 020c1c25…1abf — full value in the appendix,
       containing a 77-byte assets/model.tflite with no TFL3 header)
      → it must be replaced or withdrawn; the validation run did not modify it
  ✗ Cloud mode has no authentication, rate limiting, or HTTPS deployment
  ✗ Guidance text exists for 10 of 38 classes

  MANUAL GATES REQUIRED BEFORE ANY REAL RELEASE
   1  Run connected tests and a real TFLite prediction on API 24 and a recent API
   2  Compare cloud and offline results on-device using the same documented images
   3  Prove offline inference, history and guidance with networking disabled
   4  Exercise permission denial, invalid input, low confidence, and backend failure
   5  Expand to an independent, leaf-group-separated test set covering all 38 classes
   6  Sign with a private production key, install THAT exact APK, record its hash
   7  Publish only with the remaining Week 12 checklist evidence attached

  THIS IS THE PRODUCT'S POSITION: a validated pipeline, an unvalidated field deployment.
```

**VISUAL / DIAGRAM SPEC**
A gate/checkpoint graphic: everything left of the gate is green ("pipeline validated"),
everything right is amber ("device + release gates outstanding"), with the seven manual gates as
numbered checkpoints on the right side.

**SPEAKER NOTES**
"This slide is deliberate. A project that cannot say what it hasn't proven can't be trusted about
what it has. The pipeline is validated end-to-end and reproducibly. The *deployment* is not: no
physical device was attached, accuracy was measured on three of thirty-eight classes, there is no
production signing key, and the currently published beta release asset is the old placeholder
build, which the record says must be replaced or withdrawn. Those seven manual gates are the
actual definition of done."

**SOURCE OF TRUTH**
`docs/evidence/week-12/model-validation-2026-07-16.md` ("Release sequence status" and "Manual
gates before release"); `docs/release-validation-v0.2.0-beta.md`;
`docs/PRODUCTION_RELEASE_RUNBOOK.md`; `validation/final-definition-of-done.md`;
`release-records/model-provenance.txt`.

---

# SECTION G — PROJECT STORY

## SLIDE 32 — Dual-track engineering: Kotlin and Java twins

**PURPOSE**
Explain an unusual repository decision and the discipline it demands.

**ON-SLIDE CONTENT**

```
ONE PRODUCT, TWO COMPLETE IMPLEMENTATIONS

  android-app-kotlin/   PRIMARY   Kotlin 1.9.22 · coroutines · kapt · data classes ·
                                  `object` singletons · extension functions
  android-app/          TWIN      Java · ExecutorService instead of coroutines ·
                                  POJOs with getters/setters · static-method utility classes

  IDENTICAL ACROSS BOTH TRACKS
    package com.leafguard · all 8 Activity names · all layouts, strings, colours, drawables
    assets/labels.txt and assets/diseases.xml · generated model.tflite (byte-for-byte)
    Room schema (leafguard.db, table scan_history, same columns) → byte-compatible databases
    Retrofit contract, part name "image", JSON field names
    Notification channel id, notification id 1001

  RULE (docs/JAVA_VS_KOTLIN.md)
    Any behaviour, resource or asset change must be applied to BOTH tracks.
    CI enforces it: the workflow runs a matrix over both, so one rotting is a red build.

  WHY
    The course must be teachable in either language, and the twin acts as an executable
    specification: if the two tracks ever behave differently, one of them is wrong.
```

**VISUAL / DIAGRAM SPEC**
Mirror layout: identical component stacks facing each other, Kotlin left (green), Java right
(grey-green), with an "=" between them and small language-idiom labels on the differing rows
(coroutines ↔ ExecutorService, `object` ↔ static class, data class ↔ POJO).

**SPEAKER NOTES**
"Maintaining two implementations is unusual and it is not free — but it has a real benefit
beyond teaching. The Java twin is an executable specification. If a change makes the two tracks
behave differently, one of them is wrong, and CI catches it because the workflow builds and tests
both on every push."

**SOURCE OF TRUTH**
`docs/JAVA_VS_KOTLIN.md`; `docs/ARCHITECTURE_GROUND_TRUTH.md`; `.github/workflows/validate.yml`
matrix; the class-by-class KDoc headers in the Kotlin files ("Kotlin twin of X.java").

---

## SLIDE 33 — How it was built: 12 cumulative weeks

**PURPOSE**
Show the product as a monotonic growth curve, each week independently demonstrable.

**ON-SLIDE CONTENT**

```
THE PRODUCT WAS GROWN, NOT ASSEMBLED — ONE CODEBASE, TWELVE INCREMENTS

  Wk  Increment                                              Cumulative  Demonstrable when done
  01  Problem, user journey, screen map, system sketch          5 %      Walk the plan
  02  Android project, Home, layouts, Intent navigation        15 %      Navigate the UI shell
  03  Camera, gallery, permission, FileProvider, preview       25 %      Preview a real photo
  04  FastAPI: /health, /diseases, /predict (mock)             35 %      Predict via /docs page
  05  Retrofit multipart, JSON parsing, Result screen          45 %      Photo → server → result
  06  Real model loading and inference in the backend          55 %      Full cloud round trip
  07  Room: entity, DAO, history list, detail, delete          65 %      History survives restart
  08  diseases.xml, XML parsing, guidance wired into results   72 %      Browse the library
  09  TensorFlow Lite on-device inference + offline mode       82 %      Diagnose in airplane mode
  10  Notifications, share intent, location capture            88 %      Share a diagnosis
  11  JUnit + instrumentation tests, debugging, performance    94 %      Green suite, no crashes
  12  Release APK, report, demo video, presentation, viva     100 %      Install and demo

  THE RULES THAT MADE IT WORK
    Monotonic — the percentage never decreases and never skips a week
    No forward dependencies — no week uses something a later week teaches
    Always demonstrable — every week ends with a live milestone demo
    Same codebase — Week 12's app is Week 02's app, grown ten more weeks
    Both tracks — every increment lands in Kotlin AND Java
```

**VISUAL / DIAGRAM SPEC**
A rising step chart, 5% → 100% across twelve steps, with a tiny screenshot or icon at each step
showing what became demonstrable. Annotate the three inflection points: Week 05 (first end-to-end
result), Week 09 (works offline), Week 12 (installable).

**SPEAKER NOTES**
"The repository encodes its own build order, and the rules are strict: monotonic progress, no
forward dependencies, and a live demo at the end of every single week. That is why there was
never an integration big bang — by week five the photograph already reached a server and came
back as a result on screen; every later week thickened that same path."

**SOURCE OF TRUTH**
`PRODUCT_PROGRESS_MAP.md` (the cumulative-% table and its five rules); `roadmap/week-01…week-12/`
(7 files per week); `progress-tracker.md`.

---

## SLIDE 34 — Roadmap: what comes next

**PURPOSE**
Close the engineering story with a credible, ordered forward plan.

**ON-SLIDE CONTENT**

```
NEXT, IN PRIORITY ORDER

  RELEASE-CRITICAL (blocks any real user)
   1  Device validation matrix — API 24 and a current API, camera, gallery, airplane mode
   2  Independent, leaf-group-separated accuracy study across all 38 classes
   3  Production signing key + published, hash-recorded release; withdraw the placeholder asset
   4  HTTPS backend deployment with authentication and rate limiting

  PRODUCT DEPTH
   5  Expand curated guidance from 10 → 38 classes (currently 28 return generic text)
   6  Top-3 predictions with scores instead of a single argmax
   7  Out-of-distribution rejection — "this does not look like a leaf" (see Slide 30's
      blueberry finding; confidence alone is insufficient)
   8  Re-enable location on scans, with explicit consent, for outbreak mapping
   9  Localisation of guidance text into regional languages

  ARCHITECTURE
  10  Single Activity + Fragments + Navigation Component → per-tab back stacks
      (the trade-off is already documented in ui/BottomNav.kt)
  11  Repository layer + ViewModels → survive configuration changes without re-inference
  12  Room Flow-based queries → reactive lists instead of onResume re-queries
  13  Model upgrade channel — download and verify a new .tflite by hash without an app update
```

**VISUAL / DIAGRAM SPEC**
Three horizontal swim lanes (Release-critical / Product depth / Architecture) on a
left-to-right time axis. Colour release-critical red-amber, product depth green, architecture
blue. Anchor items 7 and 10 to the slides that raised them (30 and 28) with small back-references.

**SPEAKER NOTES**
"The roadmap is ordered by what blocks a real user, not by what is most interesting to build.
Device validation and a proper accuracy study come before new features, because without them the
app cannot honestly be put in front of a farmer. And note that several of these items were
identified *by the validation process itself* — out-of-distribution rejection is on this list
because our own evidence file recorded that synthetic images were classified as blueberry with
ninety-eight percent confidence."

**SOURCE OF TRUTH**
`Future_improvement_plan.md` (sections 13–18: feasibility validation, success definitions,
implementation pathways, risk register); `docs/evidence/week-12/model-validation-2026-07-16.md`
(manual gates); `ui/BottomNav.kt` KDoc; `docs/PRODUCTION_RELEASE_RUNBOOK.md`.

---

# SECTION H — CLOSE

## SLIDE 35 — Live demonstration script ⭐

**PURPOSE**
A rehearsed, failure-proof demo that exercises every pipeline in the deck in about four minutes.

**ON-SLIDE CONTENT**

```
DEMO — 8 STEPS, ~4 MINUTES, EVERY PIPELINE TOUCHED

  BEFORE YOU START
    Backend running (uvicorn main:app --reload) and reachable · Settings URL correct
    A known-good tomato leaf image already on the device · Airplane-mode toggle ready
    Screen recording as a fallback if the room's network fails

  1  HOME             point out the live scan count (read from Room) and library count   [P5, P8]
  2  SCAN + OFFLINE   upload → note mode toggle is already on Offline → Detect
                      → result appears with NO network at all                        [P1, P2, P3B]
  3  RESULT           read the disease, the confidence bar, the three guidance cards      [P4]
  4  SHARE            open the share sheet, send the diagnosis to a messaging app          [P4]
  5  SAVE + HISTORY   Save to History → open History → tap the row → detail → delete   [P5, P6]
  6  AIRPLANE MODE    enable it, repeat a scan offline — prove the field claim          [P3B]
  7  CLOUD MODE       disable airplane mode, switch to Cloud, Detect
                      → show the SAME disease from the server; then show the FastAPI
                        /docs page and /health reporting model_loaded and class_count=38  [P3A]
  8  UNCERTAINTY      in Settings raise the threshold to ~95, rescan
                      → the result is rewritten to "Uncertain / retake image"          [P9, gate]

  CLOSING LINE
    "Same photograph, two completely different engines, one identical answer —
     and when the model is not sure, the app says so instead of guessing."

  IF SOMETHING FAILS
    Backend unreachable → stay in Offline mode; the app is designed for exactly this.
    Camera unavailable  → use the gallery path; both are demonstrated the same way.
```

**VISUAL / DIAGRAM SPEC**
Eight numbered thumbnails in a 4×2 grid, each a screenshot with a one-line caption and the
pipeline tag (P1…P10) it demonstrates. Add a small "fallback" footer strip.

**SPEAKER NOTES**
"The demo is ordered so the riskiest dependency — the network — comes last. Steps one to six need
nothing but the phone, so even with no connectivity in the room, three quarters of the product is
demonstrable. Step seven is the money shot: the same leaf, through the server this time, giving
the same answer. Step eight proves the safety behaviour on demand by moving the threshold."

**SOURCE OF TRUTH**
`docs/complete-setup-and-run-guide.md`; `docs/production-end-to-end-setup.md`;
`final-submission/demo-video-script.md`; `ScanActivity.kt`; `SettingsActivity.kt`.

---

## SLIDE 36 — Q&A anchors and appendix

**PURPOSE**
Close the deck and arm the presenter with exact, defensible answers.

**ON-SLIDE CONTENT**

```
THE QUESTIONS THAT WILL BE ASKED — AND THE ONE-LINE ANSWERS

 Q  Why two inference engines?
 A  Connectivity fails where diagnosis matters most. Offline guarantees availability and
    privacy; cloud allows a larger model and upgrades without an app release.

 Q  How do you know they agree?
 A  Preprocessing is identical because normalisation lives inside the model graph; the
    converter refuses any model whose embedded rescaling doesn't probe 0→-1, 127.5→0, 255→+1.
    Measured: 30/30 top-class parity, max delta 0.000006.

 Q  Where is the orchestration layer?
 A  Four explicit places: ScanActivity (flow), PredictionResponse (contract),
    setupBottomNav (navigation), SharedPreferences (configuration). No DI framework.

 Q  Did you train the model?
 A  No, and the repository says so. It is sourced from a pinned upstream commit, hashed,
    licence-reviewed, and recorded in release-records/model-provenance.txt.

 Q  What is your accuracy?
 A  28/30 on a limited 3-class tomato set. That is not a 38-class accuracy study and we
    don't present it as one. See Slide 31.

 Q  What happens when the model is unsure?
 A  Below the threshold, the result object is rewritten to "Uncertain / retake image" before
    it reaches the UI — so even the saved history record is marked uncertain.

 Q  Why no ViewModel / Fragments / DI?
 A  Deliberate, documented trade-offs for a teachable codebase. The back-stack consequence
    is written in ui/BottomNav.kt along with the intended refactor.

 Q  Is it ready for farmers?
 A  Not yet. Seven manual release gates remain, starting with real-device validation.

 APPENDIX — WHERE TO VERIFY EVERY CLAIM IN THIS DECK
   docs/ARCHITECTURE_GROUND_TRUTH.md ......... authoritative class/API inventory
   docs/JAVA_VS_KOTLIN.md .................... file-by-file dual-track mapping
   docs/evidence/week-12/model-validation-*.md  parity, accuracy, APK evidence
   release-records/model-provenance.txt ...... model source, hash, licence, approvals
   docs/PRODUCTION_RELEASE_RUNBOOK.md ........ the release procedure
   validation/feature-completion-matrix.md ... feature-by-feature status
   PRODUCT_PROGRESS_MAP.md ................... the 12-week cumulative model
   backend-api/README.md · android-app-kotlin/README.md · model/README.md
```

**VISUAL / DIAGRAM SPEC**
Two columns: Q&A on the left (question in bold, answer in regular), appendix file index on the
right in monospace. Footer: "LeafGuard AI · com.leafguard · v0.2.0-beta · Thank you".

**SPEAKER NOTES**
"Thank you. To summarise in one sentence: LeafGuard AI is a plant disease detection app whose
architecture is organised around a single enforced contract, which lets two entirely different
inference engines behave identically, and whose validation record is explicit about what has been
proven and what has not."

**SOURCE OF TRUTH**
All files listed in the appendix block; `docs/viva-questions.md`.

---

## Appendix A — Fact sheet for the slide-generation agent

Every value below was read from the repository. Use these verbatim; do not round or paraphrase.

| Fact | Value | Where |
|---|---|---|
| App package / applicationId | `com.leafguard` | `app/build.gradle` |
| Version | `0.2.0-beta`, versionCode `2` | `app/build.gradle` |
| SDK levels | minSdk 24, targetSdk 34, compileSdk 34 | `app/build.gradle` |
| Kotlin / Java level | Kotlin 1.9.22, Java 11, jvmTarget 11 | `build.gradle`, `app/build.gradle` |
| Activities | 8 (5 tabs + 3 pushed) | `AndroidManifest.xml` |
| Permissions | INTERNET, CAMERA, POST_NOTIFICATIONS | `AndroidManifest.xml` |
| FileProvider authority | `${applicationId}.fileprovider` | `AndroidManifest.xml` |
| Model input | float32 `[1, 224, 224, 3]`, raw RGB 0..255 | `TFLiteClassifier.kt`, `model_contract.py` |
| Model output | float32 `[1, 38]`, argmax | `TFLiteClassifier.kt`, `model_contract.py` |
| Embedded rescaling | scale `1/127.5`, offset `-1` (0→-1, 127.5→0, 255→+1) | `model_contract.py` |
| Interpreter threads | 4 | `TFLiteClassifier.kt` |
| Classes | 38 canonical labels | `model/labels-38.txt` |
| Curated guidance entries | 10 | `assets/diseases.xml`, `main.py` `DISEASE_INFO` |
| Library fallback entries | 5 (`FALLBACK_DISEASE_COUNT`) | `DiseaseLibraryActivity.kt` |
| Database | `leafguard.db`, table `scan_history`, version 1 | `AppDatabase.kt`, `ScanRecord.kt` |
| Room version | 2.6.1 (kapt) | `app/build.gradle` |
| Retrofit endpoint | `@Multipart @POST("predict")`, part name `image` | `ApiService.kt`, `main.py` |
| Default backend URL | `http://10.0.2.2:8000` (Settings) / `http://10.0.2.2:8000/` (Retrofit) | `SettingsActivity.kt`, `RetrofitClient.kt` |
| HTTP timeouts | 30 s connect / read / write | `RetrofitClient.kt` |
| Backend endpoints | `GET /`, `GET /health`, `GET /diseases`, `POST /predict` | `main.py` |
| Server confidence threshold | 0.50 | `config.py` |
| Client confidence threshold | 50 (user-adjustable) | `SettingsActivity.kt` |
| Upload limit | 10 MB → HTTP 413 | `config.py`, `main.py` |
| Image size | 224 | `config.py`, `TFLiteClassifier.kt` |
| Notification channel / id | `leafguard_scan_reminders` / 1001 | `NotificationHelper.kt` |
| SharedPreferences keys | `pref_backend_url`, `pref_confidence_threshold` | `SettingsActivity.kt` |
| Cleartext allow-list | 10.0.2.2, 10.0.3.2, 127.0.0.1, localhost | `network_security_config.xml` |
| Keras artifact | 25,143,175 bytes, SHA-256 `08f285aff6d9e1ab88d4d5b2269f1cc977714003755f8553887edbf8691b325f` | `model-provenance.txt` |
| TFLite artifact | 9,056,916 bytes, SHA-256 `22ea2d4a47a52b2d9b150e0f74b113def0f12bbdb59209f7e0bce2a9701d41f9` | model validation evidence |
| Placeholder release APK (to be withdrawn) | SHA-256 `020c1c25760cd4737b7952c2e77944538e6e95732e9955bbdc0b5c6f3d401abf` | model validation evidence |
| Canonical labels file | SHA-256 `23eeb476d27d53fea16ba988aaca02d52673ba780412bfc19f4a8aa72d36b53d` | model validation evidence |
| Parity | 30/30 top class, max delta 0.000006 | model validation evidence |
| Limited-set accuracy | 28/30 (93.3 %) across 3 tomato classes | model validation evidence |
| Upstream model commit | `f6165bd93524dfb77a9629aae70db845832d1b01` (MIT) | `model-provenance.txt` |
| Backend tests | 8 in `test_api.py` | `backend-api/test_api.py` |
| CI | `.github/workflows/validate.yml`, matrix over both tracks + backend | workflow file |

## Appendix B — Diagram inventory (build these assets first)

| # | Diagram | Slides used |
|---|---|---|
| D1 | Four-subsystem hexagon with the central contract | 5, 15, 25 |
| D2 | Master pipeline swimlane with the split/merge point | 6 (hero) |
| D3 | Orchestration layer — centre + three satellites | 7 |
| D4 | Screen map, 8 phone frames with labelled Intent arrows | 8 |
| D5 | Acquisition flowchart with permission branches | 10 |
| D6 | `cloudMode` decision diamond | 11 |
| D7 | Cloud client 5-step pipeline | 12 |
| D8 | Backend startup fan + request pipeline with error exits | 13 |
| D9 | TFLite construct/infer/output bands | 14 |
| D10 | Parity comparison table with the "=" spine | 15 |
| D11 | Convergence card with two inbound arrows | 16 |
| D12 | Confident vs. rewritten "uncertain" result cards | 17 |
| D13 | Room three-layer diagram + `scan_history` table | 20 |
| D14 | Model supply-chain conveyor with two gates | 25 (hero) |
| D15 | CI three-lane pipeline + Docker panel | 26 |
| D16 | Data-residency phone with a single egress arrow | 27 |
| D17 | Main-thread vs IO-thread timeline | 28 |
| D18 | 12-week rising step chart | 33 |
| D19 | Stage ribbon (progress indicator, reused) | all of C and D |

## Appendix C — 12-minute short version

If the slot is short, present only the ⭐ slides in this order and skip the rest:
**1 → 3 → 4 → 5 → 6 → 7 → 10 → 11 → 12 → 13 → 14 → 15 → 16 → 17 → 19 → 20 → 25 → 31 → 35 → 36.**
Slide 6 (master pipeline) and Slide 15 (parity contract) must never be cut — they carry the
architectural thesis of the product.
