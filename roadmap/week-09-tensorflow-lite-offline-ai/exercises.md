# Week 09 Exercises: TFLite Offline Inference

## Use

Save six files under `docs/evidence/week-09/exercises/`:

| Exercise | Output |
|---:|---|
| 1 | `exercise-01-artifact-identity.md` |
| 2 | `exercise-02-tensor-buffer.md` |
| 3 | `exercise-03-conversion-parity.md` |
| 4 | `exercise-04-classifier-flow.md` |
| 5 | `exercise-05-mode-failures.md` |
| 6 | `exercise-06-parity-not-accuracy.md` |

## 1. Artifact Identity

Record Keras source hash, TFLite path/size/hash, labels path/count, conversion precision, and Git status. Explain why conversion output needs its own hash.

Validation: exact 9,056,916 bytes, 64-character hash, 38 labels, no duplicate labels.

## 2. Tensor and Buffer Contract

Label every dimension of `[1,224,224,3]` and `[1,38]`. Calculate:

$$224 \times 224 \times 3 \times 4 = 602{,}112$$

Draw raw RGB float write order. Explain direct buffer, native order, rewind, and double-normalization risk.

Validation: float32 input/output, raw `[0,255]`, RGB, exact byte count.

## 3. Conversion and Parity

Trace Keras validation -> converter -> TFLite bytes -> tensor validation -> three-image parity. Create a table with Keras label/confidence, TFLite label/confidence, delta, and pass/fail.

Validation: same top-1 index, delta <= 0.02, observed maximum below 0.000015.

## 4. Classifier Lifecycle

Trace constructor, labels, memory map, tensor checks, preprocess, run, argmax, display formatting, XML guidance, response construction, and close. Predict failures for wrong labels, tensor shape, closed interpreter, and missing XML guidance.

Validation: all eight response fields and `use`/close are explained.

## 5. Cloud/Offline Modes and Failures

Create a state table for cloud success/failure, offline success, missing model, corrupt labels, bitmap failure, and repeated runs. Record required control/progress recovery.

Validation: no silent mode switching, offline needs no backend, both branches share `openResult`.

## 6. Parity Is Not Accuracy

Explain why all three conversions can pass parity while all three folder labels disagree with predictions. Separate conversion fidelity, sample correctness, broader accuracy, confidence, and diagnosis.

Plan evidence for asset identity, tests, backend-off result, Room save, and safe missing-asset failure.

## Completion Rule

Begin the build only when you can explain artifact identity, tensor/buffer layout, conversion gates, label order, parity threshold, classifier cleanup, shared response contract, and parity-versus-accuracy.

<!-- NAV_FOOTER_START -->

---

[README](README.md) | [Learning Notes](learning-notes.md) | **Exercises** | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)