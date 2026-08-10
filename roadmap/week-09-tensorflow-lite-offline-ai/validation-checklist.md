# Week 09 Validation Checklist: TFLite Offline Inference

## Milestone

> Verify model identity and four parity tests, run Offline with backend unavailable, preserve Result/XML/Room behavior, regress Cloud, and show missing assets fail safely.

## 1. Boundary

- [ ] Week 08 XML and Week 07 Room still work.
- [ ] Cloud eight-field contract is unchanged.
- [ ] Offline returns the same eight fields.
- [ ] Week 10 features are absent.
- [ ] Parity is not called accuracy.

## 2. Exact State

- [ ] 8 new and 6 expanded text files documented.
- [ ] Total is 1,182 logical lines.
- [ ] One local TFLite binary is identified.
- [ ] Android build succeeds.
- [ ] Embedded files match Section 12.

## 3. Assets

- [ ] TFLite size is 9,056,916 bytes.
- [ ] SHA-256 is `22ea2d4a47a52b2d9b150e0f74b113def0f12bbdb59209f7e0bce2a9701d41f9`.
- [ ] Labels exactly match canonical order.
- [ ] Labels contain 38 unique values.
- [ ] Asset README records responsibilities.
- [ ] Binary is not duplicated in evidence.

## 4. Tensor/Preprocessing

- [ ] One float32 input `[1,224,224,3]`.
- [ ] One float32 output `[1,38]`.
- [ ] Caller writes raw RGB `[0,255]`.
- [ ] No `/255` double normalization.
- [ ] Direct native-order buffer is 602,112 bytes.
- [ ] Buffer is rewound.
- [ ] Output count matches labels.

## 5. Conversion and Tests

- [ ] Keras contract gates conversion.
- [ ] Embedded scaling gates conversion.
- [ ] Four focused tests run without skips.
- [ ] Labels test passes.
- [ ] Tensor test passes.
- [ ] Raw preprocessing test passes.
- [ ] Three-sample parity test passes.
- [ ] Top-1 indexes match for all three.
- [ ] Maximum observed delta is below 0.000015.

## 6. Classifier

- [ ] Model is memory-mapped.
- [ ] Interpreter uses four threads.
- [ ] Constructor validates tensors and labels.
- [ ] Bitmap scales to 224x224.
- [ ] RGB order is preserved.
- [ ] Argmax is correct.
- [ ] Display label does not reorder labels.
- [ ] Uncertain threshold is 0.50.
- [ ] XML guidance/fallback produces complete text.
- [ ] Response has all eight fields.
- [ ] Interpreter closes through `use`.

## 7. Mode UI and Async State

- [ ] Cloud selected by default.
- [ ] Offline can be selected.
- [ ] Description matches selected mode.
- [ ] Selected image is required.
- [ ] Both branches show progress.
- [ ] Mode/image controls disable while running.
- [ ] Offline runs on `Dispatchers.IO`.
- [ ] Both branches call one `openResult`.
- [ ] Controls restore after success/failure.

## 8. Offline/Cloud Behavior

- [ ] Offline works with backend stopped.
- [ ] Offline works without Internet on a device.
- [ ] Offline Result shows eight fields.
- [ ] Week 08 guidance behavior remains.
- [ ] Week 07 Room save/detail remains.
- [ ] Cloud works after backend restart.
- [ ] Cloud response remains compatible.
- [ ] Repeated offline runs do not crash.

## 9. Failure Behavior

- [ ] Missing model shows safe error.
- [ ] Missing labels shows safe error.
- [ ] Wrong label count is rejected.
- [ ] Wrong tensor shape is rejected.
- [ ] Bitmap failure is handled.
- [ ] No silent switch to Cloud.
- [ ] No fabricated prediction.
- [ ] Retry works after restoration.

## 10. Evidence

- [ ] Identity/hash evidence saved.
- [ ] Four-test output saved.
- [ ] Three-image parity saved.
- [ ] Android build saved.
- [ ] Backend-off Result saved.
- [ ] Offline Room detail saved.
- [ ] Cloud regression saved.
- [ ] Missing-asset failure saved.
- [ ] Limitations note records misclassification observation.
- [ ] Quiz score >=14/18.
- [ ] Reflection and progress tracker complete.

## Completion

All sections pass, device/emulator manual gates are documented, and no unsupported accuracy claim appears.

<!-- NAV_FOOTER_START -->

---

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | **Validation** | [Quiz](quiz.md) | [Reflection](reflection.md)