# LeafGuard Model Correctness Validation Guide

This guide explains how to test whether LeafGuard's Cloud and Offline predictions are
actually correct after the end-to-end phone test succeeds. It separates deployment
readiness from model correctness because those are different questions.

## 1. Current Audit Summary

The online pipeline is structurally ready when the Azure backend reports:

```json
{"use_mock": false, "model_loaded": true, "class_count": 38}
```

That proves the Android phone can send an image to Azure, the backend can load the Keras
model, and a prediction can return to the Result screen. It does not prove the prediction
is botanically correct.

Current model/data facts:

- The model is a 38-class PlantVillage-style classifier.
- The canonical label order is `model/labels-38.txt`; never sort or rename it.
- The backend Keras model and Android TFLite model use raw RGB float32 input resized to
  `224x224`; embedded preprocessing maps `[0, 255]` to `[-1, 1]`.
- The committed `sample-images/` files are plumbing fixtures, not accuracy evidence.
- Detailed reviewed guidance exists for 10 of the 38 classes. The other 28 classes return
  generic guidance by design.
- The Room database stores scan history. It is not a disease truth database and it cannot
  prove accuracy.

## 2. What Can Be Wrong?

When the phone shows an unexpected disease, classify the problem before changing code:

| Problem type | Meaning | How to test |
|---|---|---|
| Deployment failure | App is not reaching the real backend/model | Check `/health` and Azure logs |
| Contract mismatch | Backend, TFLite, labels, or preprocessing disagree | Run inspection and parity tests |
| Accuracy failure | Model predicts the wrong class on a known real image | Use a labeled validation set |
| Confidence failure | Model is confident but wrong, or uncertain when right | Use confidence calibration bins |
| Data/guidance gap | Prediction is valid but treatment text is generic or missing | Check whether the class is one of the 10 reviewed guidance classes |

## 3. Build a Real Validation Set

Do not use random internet images without knowing the true label. For each image, record:

- filename
- expected class from the exact 38-label list
- image source URL or capture notes
- whether the image is PlantVillage-style, field/mobile-camera, blurry, cropped, or non-leaf
- license/permission notes if it will be included in reports

Recommended folder shape outside the repository:

```text
validation-images/
  Tomato___Early_blight/
    tomato_early_001.jpg
  Tomato___Late_blight/
    tomato_late_001.jpg
  Tomato___Healthy/
    tomato_healthy_001.jpg
```

Minimum for course evidence: 10 images per class for the classes you claim to support.
Better pilot evidence: 20-50 images per class, including phone-captured field images.
Production evidence: independent all-class validation across all 38 classes, with real
field variation and expert-reviewed labels.

## 4. Cloud Correctness Test

First confirm Azure is serving the real model:

```bash
curl https://leafguard-api.whitebeach-add29aa7.eastasia.azurecontainerapps.io/health
```

Required values:

```json
{"use_mock": false, "model_loaded": true, "class_count": 38}
```

Then test one known image manually:

```bash
curl -X POST https://leafguard-api.whitebeach-add29aa7.eastasia.azurecontainerapps.io/predict \
  -F "image=@validation-images/Tomato___Late_blight/tomato_late_001.jpg;type=image/jpeg"
```

Record the returned `model_label`, `disease`, `confidence`, `uncertain`, and
`guidance_available`. The prediction is semantically correct only if `model_label` equals
the known expected class, or if your report explicitly counts it as a failure.

## 5. Offline-vs-Cloud Parity Test

Parity means the Keras backend and the Android TFLite model make the same prediction for
the same image. Parity proves conversion consistency, not biological accuracy.

Use the verified Python 3.11 + TensorFlow environment:

```bash
python3.11 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
python -m pip install -r backend-api/requirements.txt
python model/inspect_model.py \
  --tflite-model android-app-kotlin/app/src/main/assets/model.tflite
python model/parity_test.py validation-images/**/*.jpg \
  --tflite-model android-app-kotlin/app/src/main/assets/model.tflite
```

Expected parity result: same top class and confidence delta no greater than `0.02`.

## 6. Accuracy Metrics

For a class-level report, calculate at least:

- Top-1 accuracy: predicted class equals expected class.
- Per-class accuracy: accuracy separately for each disease/healthy class.
- Confusion matrix: which wrong classes the model confuses.
- Unsupported guidance count: predictions where `guidance_available=false`.
- Failure examples: include filenames, expected label, predicted label, confidence, and notes.

Use this table format in your evidence:

| File | Expected | Cloud label | Offline label | Cloud confidence | Offline confidence | Correct? | Notes |
|---|---|---|---|---:|---:|---|---|
| tomato_late_001.jpg | Tomato___Late_blight | Tomato___Late_blight | Tomato___Late_blight | 0.9977 | 0.9977 | Pass | clear PlantVillage-style image |

Do not report the source author's `98.75%` as LeafGuard's measured accuracy. It is an
upstream dataset result, not your independent app validation result.

## 7. Confidence Is Not Guaranteed Probability

The model's confidence is a softmax score. It is useful, but it is not automatically a
calibrated probability that the answer is true.

To test confidence quality:

1. Group predictions into confidence bins: `0.50-0.60`, `0.60-0.70`, `0.70-0.80`,
   `0.80-0.90`, `0.90-1.00`.
2. For each bin, calculate actual accuracy.
3. A well-calibrated `0.90-1.00` bin should be correct around 90-100% of the time.
4. If high-confidence failures appear often, keep stronger disclaimers and raise the
   app's uncertainty threshold.

For course work, a simple bin table is enough. For research-level validation, calculate
Expected Calibration Error (ECE) and reliability diagrams.

## 8. Phone-Based Acceptance Test

After command-line Cloud and parity checks pass, test on your Android phone:

1. Put the same known validation images into the phone gallery.
2. Open LeafGuard AI and set the About screen backend URL to the Azure HTTPS base URL.
3. Select Cloud mode, choose each image, and record the Result screen.
4. Switch to Offline mode for the same image and record the Result screen.
5. Confirm Cloud and Offline top labels agree.
6. Save at least one result to history and verify it survives app restart.
7. Confirm the result text is reviewed guidance for the 10 supported classes and generic
   guidance for unsupported classes.

Required screenshots:

- Azure `/health` output.
- Cloud Result screen for a correct prediction.
- Cloud Result screen for at least one failure or low-confidence result, if present.
- Offline Result screen for the same image.
- History detail screen after saving the result.

## 9. Decision Rules

Use honest release language based on evidence:

| Evidence level | Claim you can make |
|---|---|
| Only `/health` and one phone result pass | End-to-end plumbing works |
| Cloud and Offline agree on known images | Keras/TFLite deployment is consistent |
| Labeled test set has measured accuracy | LeafGuard achieved X/Y on this validation set |
| All 38 classes independently validated | Broad 38-class accuracy claim is supported |
| Field/mobile images validated by experts | Real-world pilot claim is more defensible |

If the model predicts the wrong disease on real known-label images, the fix is usually not
an Android/backend code change. The options are:

1. collect better labeled data for the failing classes;
2. retrain or fine-tune the model;
3. calibrate or raise the confidence threshold;
4. narrow the product claim to the classes that pass validation;
5. improve preprocessing only if inspection proves the app/backend input contract is wrong.

## 10. Evidence Checklist

- [ ] Model hash, label hash, backend image tag, and Azure URL recorded.
- [ ] `/health` shows real model loaded.
- [ ] At least one manual `/predict` call saved.
- [ ] Validation images have known labels and source notes.
- [ ] Cloud result table completed.
- [ ] Offline-vs-Cloud parity table completed.
- [ ] Accuracy, per-class accuracy, and failure examples recorded.
- [ ] Confidence-bin table completed.
- [ ] Phone screenshots collected.
- [ ] Progress log updated with what failed and what will be improved.
