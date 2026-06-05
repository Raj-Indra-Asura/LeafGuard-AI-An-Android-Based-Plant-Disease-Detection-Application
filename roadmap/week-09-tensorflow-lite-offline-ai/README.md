# Week 09: TensorFlow Lite Offline AI

## Weekly Objective

Implement on-device AI inference using TensorFlow Lite for offline prediction capability.

**Measurable Outcomes:**
- .tflite model file in assets/
- labels.txt file with class names
- TFLite interpreter initialized
- Offline prediction working
- Cloud vs on-device mode selector
- Latency comparison feature
- Complete offline operation

---

## Why This Week Matters

**Offline AI** enables app to work without internet connection. Demonstrates advanced Android development.

**CSE 2206:** Shows understanding of both cloud and on-device AI architectures.

---

## Syllabus Topics

1. **On-device ML** - TensorFlow Lite integration
2. **File I/O** - Loading model from assets
3. **Performance** - Latency measurement
4. **Mode Selection** - User choice between cloud/offline

---

## Prerequisites

- Week 06 complete (cloud ML working)
- Understanding of tensor inputs/outputs
- .tflite model file ready

---

## Key Concepts

### TFLite Architecture

```
Image → Preprocessing → TFLite Interpreter → Output Tensor → Post-processing → Result
```

### Implementation Steps

1. Convert model to .tflite format
2. Add model and labels to assets/
3. Initialize TFLite Interpreter
4. Preprocess image to match input shape
5. Run inference
6. Extract output and map to labels
7. Display results

---

## Weekly Timeline

- **Day 1-2:** Model conversion and setup (4h)
- **Day 3-4:** TFLite interpreter implementation (5h)
- **Day 5:** Integration with app (3h)
- **Day 6:** Mode selector UI (2h)
- **Day 7:** Testing and comparison (2h)

---

## Validation Criteria

- [ ] .tflite model in assets/
- [ ] labels.txt in assets/
- [ ] TFLite interpreter initializes
- [ ] Offline prediction works
- [ ] Results match cloud predictions
- [ ] Mode selector implemented
- [ ] Latency measured and displayed
- [ ] Works without internet

---

**Next:** Open `learning-notes.md` for TFLite concepts.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 09 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| **1** | **README.md** ← *You are here* | **Week Overview & Objectives** |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

*(Start of week)* &nbsp;&nbsp;|&nbsp;&nbsp; **Week Overview & Objectives** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Theory & Learning Notes →](learning-notes.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 08: XML Disease Library](../week-08-xml-disease-library/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 10: Notifications, Share & Location ➡](../week-10-notifications-share-location/README.md) |

---
