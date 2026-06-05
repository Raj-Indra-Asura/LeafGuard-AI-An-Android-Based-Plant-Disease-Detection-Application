# Week 09: Validation Checklist - TensorFlow Lite Offline AI

Use this checklist after finishing the Week 09 build task.
Mark each item only after you have real evidence: a screenshot, log entry, code snippet, or successful test.

**Suggested pass target:** 120 / 140 or higher

---

## A. Model Sourcing Verification (15 marks)

| Check | Item | Pass/Fail | Evidence |
|------|------|-----------|----------|
| [ ] | Source of the model is documented (TensorFlow Hub / Colab / Model Maker / other). | _____ | ______________________________ |
| [ ] | `model.tflite` was verified outside Android before integration. | _____ | ______________________________ |
| [ ] | Input shape is recorded in project notes. | _____ | ______________________________ |
| [ ] | Normalization strategy is recorded in project notes. | _____ | ______________________________ |
| [ ] | `labels.txt` exists and order is documented. | _____ | ______________________________ |

### Section notes

_______________________________________________________________
_______________________________________________________________

---

## B. Android Setup and Assets (15 marks)

| Check | Item | Pass/Fail | Evidence |
|------|------|-----------|----------|
| [ ] | TensorFlow Lite dependency added to Gradle. | _____ | ______________________________ |
| [ ] | GPU dependency added if acceleration testing is planned. | _____ | ______________________________ |
| [ ] | Project sync completed successfully. | _____ | ______________________________ |
| [ ] | `model.tflite` is inside `app/src/main/assets/`. | _____ | ______________________________ |
| [ ] | `labels.txt` is inside `app/src/main/assets/`. | _____ | ______________________________ |

### Section notes

_______________________________________________________________
_______________________________________________________________

---

## C. Interpreter Initialization and Safety (15 marks)

| Check | Item | Pass/Fail | Evidence |
|------|------|-----------|----------|
| [ ] | Interpreter initializes without crash on a supported device. | _____ | ______________________________ |
| [ ] | Missing model file is handled gracefully. | _____ | ______________________________ |
| [ ] | Labels load successfully. | _____ | ______________________________ |
| [ ] | Initialization errors are logged or shown clearly. | _____ | ______________________________ |
| [ ] | Resources can be released using `close()`. | _____ | ______________________________ |

### Section notes

_______________________________________________________________
_______________________________________________________________

---

## D. Preprocessing Correctness (20 marks)

| Check | Item | Pass/Fail | Evidence |
|------|------|-----------|----------|
| [ ] | Image is resized to the exact model input size. | _____ | ______________________________ |
| [ ] | Color channel order matches training. | _____ | ______________________________ |
| [ ] | Normalization formula matches training. | _____ | ______________________________ |
| [ ] | Input tensor shape matches model expectation. | _____ | ______________________________ |
| [ ] | Large images are scaled before heavy processing to reduce memory risk. | _____ | ______________________________ |

### Section notes

_______________________________________________________________
_______________________________________________________________

---

## E. Inference and Output Handling (20 marks)

| Check | Item | Pass/Fail | Evidence |
|------|------|-----------|----------|
| [ ] | `Interpreter.run()` executes successfully. | _____ | ______________________________ |
| [ ] | Output tensor size matches label count. | _____ | ______________________________ |
| [ ] | Top-1 prediction is mapped correctly. | _____ | ______________________________ |
| [ ] | Confidence score is displayed or logged. | _____ | ______________________________ |
| [ ] | Top-3 predictions are available for debugging or documentation. | _____ | ______________________________ |

### Section notes

_______________________________________________________________
_______________________________________________________________

---

## F. Low-Confidence Handling (10 marks)

| Check | Item | Pass/Fail | Evidence |
|------|------|-----------|----------|
| [ ] | Confidence threshold value is defined in code or constants. | _____ | ______________________________ |
| [ ] | Low-confidence results show `Uncertain` or similar message. | _____ | ______________________________ |
| [ ] | High-confidence results still show the predicted class. | _____ | ______________________________ |
| [ ] | Low-confidence behavior has been tested using at least one difficult image. | _____ | ______________________________ |
| [ ] | User guidance is shown or documented for retaking a better image. | _____ | ______________________________ |

### Section notes

_______________________________________________________________
_______________________________________________________________

---

## G. Threading and Responsiveness (10 marks)

| Check | Item | Pass/Fail | Evidence |
|------|------|-----------|----------|
| [ ] | Inference runs off the main thread. | _____ | ______________________________ |
| [ ] | UI remains responsive during prediction. | _____ | ______________________________ |
| [ ] | Background result is returned safely to the main thread. | _____ | ______________________________ |
| [ ] | Thread safety of `Interpreter` has been considered. | _____ | ______________________________ |
| [ ] | No ANR or UI freeze observed during normal testing. | _____ | ______________________________ |

### Section notes

_______________________________________________________________
_______________________________________________________________

---

## H. GPU Delegate / NNAPI Benchmarking (10 marks)

| Check | Item | Pass/Fail | Evidence |
|------|------|-----------|----------|
| [ ] | GPU delegate path was attempted or explicitly documented as unavailable. | _____ | ______________________________ |
| [ ] | CPU fallback works if GPU is unavailable. | _____ | ______________________________ |
| [ ] | Benchmark used repeated runs, not a single test. | _____ | ______________________________ |
| [ ] | Latency numbers were recorded for at least CPU mode. | _____ | ______________________________ |
| [ ] | Accelerated-mode findings are documented honestly. | _____ | ______________________________ |

### Section notes

_______________________________________________________________
_______________________________________________________________

---

## I. Offline Validation and Comparison (15 marks)

| Check | Item | Pass/Fail | Evidence |
|------|------|-----------|----------|
| [ ] | Offline mode works in airplane mode. | _____ | ______________________________ |
| [ ] | At least 5 test images were evaluated. | _____ | ______________________________ |
| [ ] | Offline vs cloud comparison recorded predicted labels. | _____ | ______________________________ |
| [ ] | Offline vs cloud comparison recorded latency. | _____ | ______________________________ |
| [ ] | Limitations or mismatches were documented. | _____ | ______________________________ |

### Section notes

_______________________________________________________________
_______________________________________________________________

---

## J. Documentation and Evidence (10 marks)

| Check | Item | Pass/Fail | Evidence |
|------|------|-----------|----------|
| [ ] | Progress log updated with Week 09 work. | _____ | ______________________________ |
| [ ] | Screenshots saved showing offline prediction working. | _____ | ______________________________ |
| [ ] | Logs or notes saved for benchmark results. | _____ | ______________________________ |
| [ ] | Quiz and reflection completed. | _____ | ______________________________ |
| [ ] | Build task marked complete only after real testing. | _____ | ______________________________ |

### Section notes

_______________________________________________________________
_______________________________________________________________

---

## Final Performance Record

| Metric | Value | Notes |
|-------|-------|-------|
| Model size | __________ | |
| Input size | __________ | |
| Normalization type | __________ | |
| CPU average latency | __________ ms | |
| GPU average latency | __________ ms | |
| NNAPI average latency | __________ ms | |
| Confidence threshold | __________ | |
| Number of test images | __________ | |

## Final Pass / Fail Decision

- **Total Score:** __________ / 140
- **Status:** [ ] PASS  [ ] FAIL
- **Can demonstrate offline mode in airplane mode:** [ ] Yes  [ ] No
- **Can explain preprocessing and label order clearly:** [ ] Yes  [ ] No
- **Can justify offline vs cloud architecture choice:** [ ] Yes  [ ] No

## Teacher / Self-Review Notes

_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
