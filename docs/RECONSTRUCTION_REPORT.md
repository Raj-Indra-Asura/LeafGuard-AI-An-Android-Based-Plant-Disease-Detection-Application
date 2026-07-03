# LeafGuard AI — Kotlin-First Reconstruction Report (July 2026)

> **What is this file?** The change log for the July 2026 "Kotlin-first
> reconstruction" of this repository. It is referenced by `VALIDATION-REPORT.md`
> and `DOCUMENTATION-SUMMARY.md`. The companion document
> [`ARCHITECTURE_GROUND_TRUTH.md`](ARCHITECTURE_GROUND_TRUTH.md) is the
> authoritative inventory of what exists in the code *after* this reconstruction;
> this report records *what changed and why*.

---

## 1. What the reconstruction was

The repository was originally built Java-first: `android-app/` (Java) was the
primary track and `android-app-kotlin/` a parallel enrichment twin. In July 2026
the repository was reconstructed as a **Kotlin-first, beginner-friendly,
dual-path learning system**:

- **Kotlin (`android-app-kotlin/`) is now the primary, recommended track** —
  learners build there first, and `exercises/android-kotlin/` is the primary
  exercise set.
- **Java (`android-app/`) is the complete secondary twin** — fully supported,
  behavior-identical, and still covering the CSE 2206 syllabus topic
  "Java for Android development".
- Neither app was rewritten: the two tracks remain functional twins (same
  screens, Room schema `scan_history`, `POST /predict` API contract, TFLite
  preprocessing, assets). Only the *documented priority* and the documentation
  standard changed.

## 2. Changes made during the reconstruction

| Area | Change |
|---|---|
| Track priority | Kotlin declared primary throughout `README.md`, `LEARNING_PATH.md`, `docs/JAVA_VS_KOTLIN.md`, `docs/ARCHITECTURE_GROUND_TRUTH.md`, `VALIDATION-REPORT.md`, `DOCUMENTATION-SUMMARY.md`, `solutions/README.md` |
| Beginner-first standard | Every document defines terms in plain English **before** showing code (see the "plain language" style of `ARCHITECTURE_GROUND_TRUTH.md`) |
| Automated tests | Real unit tests added to both apps under `app/src/test/java/` |
| Repo hygiene | Build artifacts removed from Git; root `.gitignore` added; Windows `gradlew.bat` wrappers added to both Gradle projects |
| Documentation audit | All roadmap/exercise/notebook docs audited against the real code; discrepancies corrected so documents match `ARCHITECTURE_GROUND_TRUTH.md` |

## 3. Post-reconstruction consistency fixes (this report's addendum)

A follow-up audit found stragglers that still declared Java primary (they
predated the reconstruction) and a few link/coverage gaps. Fixed:

1. `android-app-kotlin/README.md` — "Why two apps?" note now states Kotlin is
   the primary track.
2. `docs/parallel-track/README.md` — track table and rationale now list Kotlin
   as primary, Java as the secondary twin.
3. `exercises/android-kotlin/README.md` — intro note updated (Kotlin exercises
   are the primary set; the shared task lists remain single-sourced in
   `exercises/android/README.md` to prevent drift).
4. `SYLLABUS_MAPPING.md` — parallel-track note updated (both tracks satisfy
   the mappings; Java twin covers the "Java for Android" topic).
5. This file (`docs/RECONSTRUCTION_REPORT.md`) was created — it had been cited
   by `VALIDATION-REPORT.md` and `DOCUMENTATION-SUMMARY.md` but was missing.
6. `LEARNING_PATH.md` — "Documentation Templates" links now point to the
   actual files instead of generically to `docs/`; `reference-sheets/` link
   fixed and the directory now exists with quick-reference content.
7. `notebooks/week-08/` — the XML-parsing notebook (an Android week) gained the
   "Parallel Kotlin Track" section it was missing; coverage lists updated to
   weeks 02, 03, 05, 07, 08, 09, 10, 11, 12 (04/06 remain Python-only by design).

## 4. What did *not* change

- The ML assets remain **functional starters**: a stub `model.tflite` with a
  heuristic fallback in `TFLiteClassifier`, plus the backend's mock mode. This
  is deliberate and labeled ("Functional Starter, Not a Final Diagnostic
  Product"); replacement steps are in the Weeks 06/09 material.
- `docs/parallel-track/CHANGES.md` is preserved as the historical deliverable
  record of the original parallel-track creation; statements there describe the
  repository *at that time*.
- All behavioral contracts (screen flow, database schema, API contract, model
  I/O) recorded in `docs/parallel-track/00-baseline-validation.md` are unchanged.
