# Week 09 Build Task: Add Verified Offline Inference

## Objective

Reconstruct the exact snapshot in [learning-notes.md section 12](learning-notes.md#12-end-of-week-09-file-inventory-exact-files-exact-code-exact-size), validate conversion parity, and demonstrate offline Android inference.

## Prerequisites

- [ ] Week 08 list/detail/enrichment works.
- [ ] Week 06 Keras artifact identity is valid.
- [ ] TensorFlow 2.19.1 imports.
- [ ] Six exercises are complete.

## Step 1: Freeze Contracts

Record Keras input/output, raw RGB preprocessing, 38 labels, eight-field response, unchanged Result/XML/Room behavior.

## Step 2: Expand Model Tooling

Create/verify `model_contract.py`, `convert_model.py`, `validate_tflite.py`, `parity_test.py`, and `test_tflite_contract.py` from Section 12.

Run Keras contract tests before conversion.

## Step 3: Convert and Identify

Run:

```bash
backend-api/.venv/bin/python model/convert_model.py
```

Verify model size/hash and exact labels. Stop if identity differs unexpectedly; update provenance only after review.

## Step 4: Run Focused Tests

```bash
cd model
../backend-api/.venv/bin/python -m unittest -v test_tflite_contract
```

Required: four tests, no skips, `OK`.

## Step 5: Add Android Runtime and Assets

Expand Gradle with only `tensorflow-lite:2.14.0` and `noCompress "tflite"`. Add model, labels, and asset README.

Build before classifier work.

## Step 6: Create `TFLiteClassifier`

Use Section 12 exactly. Verify:

- memory mapping
- 4 threads
- strict input/output/label checks
- 602,112-byte raw RGB buffer
- argmax and display formatting
- threshold `0.50`
- local XML guidance/fallback
- eight-field response
- `AutoCloseable`

## Step 7: Add Mode UI and Scan Strategy

Expand Scan layout/strings and ScanActivity. Preserve camera/gallery/cloud code. Add offline branch on `Dispatchers.IO`; both branches use one progress state and `openResult`.

Build:

```bash
cd android-app-kotlin
./gradlew assembleDebug
```

## Step 8: Demonstrate Offline

Stop backend or enable airplane mode on a physical device. Select Offline, choose image, classify, show Result, save to Room, and reopen history detail.

## Step 9: Regress Cloud

Restart backend, select Cloud, and prove Retrofit still produces the same Result/Room flow.

## Step 10: Prove Failure Safety

Temporarily rename model or labels in a test copy. Verify friendly error, progress hidden, controls restored, no fabricated result, and successful retry after restoration.

## Step 11: Preserve Boundaries

Do not add quantization/GPU, settings thresholds, notifications, location, sharing, analytics, or UI redesign.

## Evidence

Save artifact identity, four tests, parity table, Android build, backend-off offline Result, Room detail, cloud regression, missing-asset failure, and honest limitations note.

## Done Means

- exact 14-text-file snapshot is understood
- artifact and labels match
- four tests/parity pass
- Android builds
- offline works without backend
- cloud still works
- both return eight fields
- resources close safely
- failures recover
- parity is not called accuracy

<!-- NAV_FOOTER_START -->

---

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | **Build Task** | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)