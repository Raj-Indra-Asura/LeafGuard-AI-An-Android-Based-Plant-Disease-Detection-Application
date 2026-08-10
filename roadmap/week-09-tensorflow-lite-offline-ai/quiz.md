# Week 09 Quiz: TFLite Offline Inference

Passing score: **14/18**.

## Multiple Choice

### 1. What does conversion do?
A) Retrains model B) Changes compatible runtime format C) Adds classes D) Creates guidance

### 2. Input shape?
A) `[1,224,224,3]` B) `[224,224]` C) `[1,38]` D) `[256,256,3]`

### 3. Caller pixel range?
A) `[0,1]` B) `[-1,1]` C) raw `[0,255]` D) labels

### 4. Input buffer bytes?
A) 150528 B) 602112 C) 9056916 D) 38

### 5. Output decoding?
A) sort labels B) argmax scores then canonical index C) XML name D) random

### 6. Why `noCompress "tflite"`?
A) enable memory mapping B) improve accuracy C) add labels D) request permission

### 7. Offline branch runs where?
A) FastAPI B) `Dispatchers.IO` with TFLite C) Room D) XML parser only

### 8. What does parity prove?
A) Diagnosis accuracy B) conversion consistency C) all classes supported D) device speed

## True/False

### 9. Cloud and offline return the same eight-field response. ____
### 10. Android should divide pixels by 255 before this model. ____
### 11. Labels may be sorted alphabetically. ____
### 12. Offline inference requires FastAPI. ____
### 13. Interpreter should be closed after use. ____

## Short Answer

### 14. State exact TFLite identity, tensor, and label contracts.
### 15. Trace Bitmap to `PredictionResponse`.
### 16. Explain cloud/offline shared strategy and UI recovery.
### 17. Explain why the three-image result passed parity but not correctness.
### 18. Name three Week 10/later features excluded here.

## Key

1 B, 2 A, 3 C, 4 B, 5 B, 6 A, 7 B, 8 B, 9 True, 10 False, 11 False, 12 False, 13 True.

Short answers require: (14) path/size/hash, float32 shapes, 38 labels; (15) decode/resize/raw RGB buffer/run/argmax/guidance/eight fields; (16) selector, async branches, one result path, terminal recovery; (17) indexes/deltas match but folder labels disagree and three samples are not accuracy; (18) notifications/location/sharing/analytics/settings polish/quantization/GPU.

<!-- NAV_FOOTER_START -->

---

[README](README.md) | [Learning Notes](learning-notes.md) | [Exercises](exercises.md) | [Build Task](build-task.md) | [Validation](validation-checklist.md) | **Quiz** | [Reflection](reflection.md)