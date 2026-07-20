# 📈 Product Progress Map — The Official Cumulative-% Model

This is the **single source of truth** for how complete the LeafGuard AI product is at the end of each week. The app is built **cumulatively**: every week adds a working, demonstrable increment to the **same growing codebase** (Kotlin track: [`android-app-kotlin/`](android-app-kotlin/); Java track: [`android-app/`](android-app/)). By the end of Week 12 you hold a 100%-complete, deployable product.

**How to read this table:**
- **Cumulative %** — how much of the final product exists at the end of that week. It only ever goes up, and reaches exactly 100% in Week 12.
- **Features added this week** — the increment you build that week.
- **Your app can now…** — the demonstrable product state.
- **Your app still cannot…** — what remains, so you always know what's next.
- **Milestone demo** — the one live demo that proves the cumulative state (mirrored in each week's `validation-checklist.md`).

| Week | Features added this week | Cumulative % | Your app can now… | Your app still cannot… | Milestone demo that proves it |
|------|--------------------------|:------------:|-------------------|------------------------|-------------------------------|
| [01](roadmap/week-01-project-understanding/README.md) | Product idea, user journey, screen map, beginner system sketch, 12-week growth map, evidence folder | **5%** | Exist as a validated learning foundation: the problem, user flow, future slices, and evidence plan are clear | Run at all - no Android code exists yet | Walk through the foundation package: product idea -> user journey -> screen map -> system sketch -> growth map |
| [02](roadmap/week-02-android-basics-ui/README.md) | Android project, Home screen, placeholder feature screens, XML layouts, Intent navigation, string/color resources | **15%** | Launch on an emulator/device and navigate through the planned UI shell | Take photos, analyze images, call a backend, save history, or run AI | Launch the app, tap from Home to every placeholder screen, return safely, and explain which future week fills each placeholder |
| [03](roadmap/week-03-camera-gallery/README.md) | ScanActivity image input: camera capture, gallery picker, camera permission, FileProvider, URI preview | **25%** | Capture a leaf photo or pick one from gallery and show it in the Scan preview, handling denial/cancel safely | Analyze, upload, save, or diagnose the image - no backend, database, or AI yet | Open Scan, preview one gallery image, preview one camera photo, and show denial/cancel does not crash |
| [04](roadmap/week-04-fastapi-backend/README.md) | FastAPI backend: health check, `/diseases`, `/predict` multipart upload, mock predictions | **35%** | (Server side) accept an image upload and return a JSON disease prediction — testable from the docs page | Connect app to server — the Android app doesn't talk to it yet | Upload a leaf image via the FastAPI `/docs` page and read the JSON prediction response |
| [05](roadmap/week-05-android-networking/README.md) | Retrofit networking, multipart upload from app, JSON parsing, Result screen, error handling | **45%** | Send a captured photo to your backend and display the disease name + confidence on a Result screen | Give real AI answers (predictions are mock), remember scans, or work offline | Capture a photo in the app → see a prediction from the server on the Result screen; stop the server and show the friendly error |
| [06](roadmap/week-06-cloud-ml-model/README.md) | ML model integration in backend (Keras/TFLite loading, preprocessing, real inference path) | **55%** | Run a real model-driven prediction pipeline in the cloud mode end-to-end | Remember past scans, show disease advice, or work offline | Full cloud round-trip: photo → upload → model inference → result on screen |
| [07](roadmap/week-07-room-sqlite-history/README.md) | Room database: Entity, DAO, save every scan, History list, detail view, delete | **65%** | Save every scan automatically and browse/delete a persistent scan history that survives app restarts | Show treatment/prevention advice or work offline | Do a scan, kill the app, reopen it, and show the scan still in History; delete it live |
| [08](roadmap/week-08-xml-disease-library/README.md) | XML disease library in assets, XML parsing, Disease Library screen, advice wired into results | **72%** | Show symptoms, treatment, and prevention for every predicted disease, plus a browsable disease encyclopedia | Work without internet — cloud mode only | Scan a leaf and show the full advice on the Result screen; open the Disease Library and browse all 10 diseases |
| [09](roadmap/week-09-tensorflow-lite-offline-ai/README.md) | TensorFlow Lite on-device inference, offline mode toggle/fallback | **82%** | Diagnose a leaf **with no internet at all** — the complete core product works offline and online | Notify, share results, or attach location; not yet hardened or packaged | Turn on airplane mode and complete a full scan → result → history flow offline |
| [10](roadmap/week-10-notifications-share-location/README.md) | Notifications, share intent, location capture | **88%** | Notify the user, share a diagnosis to other apps, and tag scans with location | Guarantee stability under stress; not yet a signed, installable release | Complete a scan, share the result to another app, and show the notification + location on the scan |
| [11](roadmap/week-11-testing-debugging-performance/README.md) | JUnit tests, instrumentation tests, debugging passes, performance fixes | **94%** | Prove it works: pass its test suite and survive edge cases (no crashes in the demo flow) | Be installed from a signed release APK with final docs | Run the test suite green, then perform the full crash-free demo flow |
| [12](roadmap/week-12-final-submission/README.md) | Signed release APK, final report, demo video, presentation, viva prep | **100%** | Everything — a complete, signed, installable, documented plant disease detection app | Nothing in scope — the product is done 🎉 | Install the signed APK on a fresh device and demo every feature end-to-end |

---

## Rules of the model

1. **Monotonic:** the cumulative % never decreases and never skips a week.
2. **No forward dependencies:** no week uses code or concepts a previous week didn't build/teach.
3. **Always demonstrable:** every week ends with a working increment you can demo live (the "Milestone demo" column, enforced by each week's `validation-checklist.md`).
4. **Same codebase:** you never restart; Week 12's app is Week 02's app grown ten more weeks.
5. **Both tracks identical:** the % applies equally to the Kotlin (`android-app-kotlin/`) and Java (`android-app/`) tracks — see [`docs/JAVA_VS_KOTLIN.md`](docs/JAVA_VS_KOTLIN.md).

---

## Where this model is used

- Each week's `README.md` ends with a **"📈 Product State After This Week"** section quoting its row.
- [`progress-tracker.md`](progress-tracker.md) records the **cumulative % achieved** per week.
- Each week's `validation-checklist.md` contains the **🎬 Milestone Demo** for that week.
- [`START_HERE.md`](START_HERE.md) is the canonical entry point that leads you to Week 01.
