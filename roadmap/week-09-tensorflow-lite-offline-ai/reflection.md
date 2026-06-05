# Week 09: Reflection - TensorFlow Lite Offline AI

Use this reflection after completing the exercises and build task.
Write honest answers. The goal is not just to say what worked, but to explain what you learned and what you would improve.

---

## 1. Model Sourcing Reflection

### Reflection Prompts

- Which model sourcing path did you use: TensorFlow Hub workflow, Google Colab training, or Model Maker? Why did you choose it?
- What did you learn about datasets, class order, and label creation while obtaining the model?
- What evidence will you keep (screenshots, notebook cells, exported files) to prove the model source in your report?

### Your Notes

_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### Evidence to attach

- Screenshot / log / code snippet related to this section:
_______________________________________________________________

### Self-rating (1-5)

Score: _____ / 5

---

## 2. Understanding the Offline AI Pipeline

### Reflection Prompts

- In your own words, explain the full pipeline from captured image to predicted disease label.
- Which step felt most abstract at first: model loading, preprocessing, inference, or post-processing?
- How confident are you that you could explain this pipeline in a viva without looking at notes?

### Your Notes

_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### Evidence to attach

- Screenshot / log / code snippet related to this section:
_______________________________________________________________

### Self-rating (1-5)

Score: _____ / 5

---

## 3. Preprocessing and Normalization

### Reflection Prompts

- What normalization rule does your current model use? How did you confirm it?
- Why is preprocessing mismatch one of the most dangerous silent bugs in mobile ML?
- If your model switched from `[0, 1]` to `[-1, 1]`, what exact Java code would need to change?

### Your Notes

_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### Evidence to attach

- Screenshot / log / code snippet related to this section:
_______________________________________________________________

### Self-rating (1-5)

Score: _____ / 5

---

## 4. Model Conversion and Quantization

### Reflection Prompts

- If your model was converted from `.h5` or `SavedModel`, what steps were required?
- What is the difference between dynamic range quantization, float16 quantization, and full integer quantization?
- Would you use an aggressively quantized model for your final demo? Why or why not?

### Your Notes

_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### Evidence to attach

- Screenshot / log / code snippet related to this section:
_______________________________________________________________

### Self-rating (1-5)

Score: _____ / 5

---

## 5. Java Implementation Quality

### Reflection Prompts

- How did you organize your code so that the Activity did not become overloaded with ML logic?
- Which parts of your `TFLiteClassifier` feel production-ready, and which parts still feel like a student prototype?
- What improvements would you make next if you had more time?

### Your Notes

_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### Evidence to attach

- Screenshot / log / code snippet related to this section:
_______________________________________________________________

### Self-rating (1-5)

Score: _____ / 5

---

## 6. Threading and Responsiveness

### Reflection Prompts

- How did you move inference off the main thread? Why is that necessary in Android?
- What problems could happen if you tried to run long inference directly inside a button click handler?
- How does thread safety affect the way you use the TensorFlow Lite `Interpreter`?

### Your Notes

_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### Evidence to attach

- Screenshot / log / code snippet related to this section:
_______________________________________________________________

### Self-rating (1-5)

Score: _____ / 5

---

## 7. Confidence Threshold and UX

### Reflection Prompts

- What confidence threshold did you choose and why?
- Describe one situation where showing `Uncertain - try again` is better than showing the top label.
- How could you guide the user to capture a better image after a low-confidence result?

### Your Notes

_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### Evidence to attach

- Screenshot / log / code snippet related to this section:
_______________________________________________________________

### Self-rating (1-5)

Score: _____ / 5

---

## 8. GPU Delegate, NNAPI, and Performance

### Reflection Prompts

- Did you test GPU delegate or NNAPI on a real device? What happened?
- How did CPU and accelerated modes compare in latency?
- What did this teach you about benchmarking instead of assuming?

### Your Notes

_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### Evidence to attach

- Screenshot / log / code snippet related to this section:
_______________________________________________________________

### Self-rating (1-5)

Score: _____ / 5

---

## 9. Offline vs Cloud Comparison

### Reflection Prompts

- After this week, when would you prefer offline inference and when would you prefer cloud inference?
- Did the offline and cloud outputs agree on all your test images? If not, what might explain the difference?
- If you were designing LeafGuard for real farmers, would you choose offline-only, cloud-only, or hybrid? Defend your answer.

### Your Notes

_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### Evidence to attach

- Screenshot / log / code snippet related to this section:
_______________________________________________________________

### Self-rating (1-5)

Score: _____ / 5

---

## 10. Week 09 Self-Evaluation and Next Steps

### Reflection Prompts

- What are the three most important technical ideas you learned this week?
- Which validation checklist items are fully complete and which still need evidence?
- What is the next improvement you want to make to LeafGuard AI after finishing Week 09?

### Your Notes

_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________
_______________________________________________________________

### Evidence to attach

- Screenshot / log / code snippet related to this section:
_______________________________________________________________

### Self-rating (1-5)

Score: _____ / 5

---

## Final Reflection Summary

- **Hours spent this week:** __________
- **Most valuable concept learned:** _________________________________
- **Biggest unresolved challenge:** _________________________________
- **Would you feel comfortable demonstrating this in viva? Why?**
_______________________________________________________________
_______________________________________________________________

- **Date:** __________
- **Student signature / initials:** __________


<!-- NAV_FOOTER_START -->

---

## 📚 Week 09 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| **7** | **reflection.md** ← *You are here* | **Reflection & Consolidation** |

---

### Within-Week Navigation

[← Knowledge Assessment Quiz](quiz.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Reflection & Consolidation** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Week 10: Notifications, Share & Location (Start) →](../week-10-notifications-share-location/README.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 08: XML Disease Library](../week-08-xml-disease-library/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 10: Notifications, Share & Location ➡](../week-10-notifications-share-location/README.md) |

---
