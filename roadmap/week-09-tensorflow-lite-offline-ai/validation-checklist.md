# Week 09: Validation Checklist

## Model Files (15 points)

- [ ] model.tflite in assets/
- [ ] File size reasonable (<50MB)
- [ ] labels.txt in assets/
- [ ] Label count matches model output
- [ ] Label order correct

## TFLite Setup (20 points)

- [ ] Dependency added to gradle
- [ ] Project builds successfully
- [ ] Interpreter initializes without error
- [ ] Model loads from assets
- [ ] Labels load correctly

## Preprocessing (20 points)

- [ ] Image resized to model input size
- [ ] Pixel normalization correct ([0,1] or [-1,1])
- [ ] Color channels correct order (RGB/BGR)
- [ ] Input tensor shape matches model
- [ ] No preprocessing errors

## Inference (25 points)

- [ ] tflite.run() executes without error
- [ ] Output tensor extracted correctly
- [ ] Argmax finds predicted class
- [ ] Confidence score extracted
- [ ] Labels mapped to output correctly
- [ ] Results displayed in UI

## Mode Selector (10 points)

- [ ] UI element for mode selection
- [ ] Cloud mode option
- [ ] Offline mode option
- [ ] Mode switching works
- [ ] Selected mode applied

## Latency Measurement (5 points)

- [ ] Start time recorded
- [ ] End time recorded
- [ ] Latency calculated
- [ ] Latency displayed in UI

## Offline Operation (5 points)

- [ ] Works in airplane mode
- [ ] No network errors
- [ ] Results still displayed

---

**Score:** _____ / 100

**Pass:** ≥85

**Status:** [ ] PASS | [ ] FAIL
