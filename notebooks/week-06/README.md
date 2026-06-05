# Week 06 Interactive Notebook

## Adding Real AI to LeafGuard

> This README acts like a Markdown notebook for CSE 2206. Read one cell at a time, run the code, and write your own notes after each checkpoint.

### How to use this notebook

- Follow the cells in order.
- Run code blocks in Android Studio, Terminal, or a Python shell as indicated.
- Keep LeafGuard AI open in Android Studio while you work.
- Save screenshots for your evidence folder after each big milestone.
- Use Java for Android code in this repository. Do not switch to Kotlin.

### Weekly outcomes

- Understand the PlantVillage dataset and class labels used in disease detection.
- Convert a TensorFlow model to TFLite and connect it to FastAPI.
- Test real predictions and set confidence thresholds for safer output.

### Repository references

- `model/model-notes.md`
- `model/labels.txt`
- `roadmap/week-06-cloud-ml-model/`
- `backend-api/model_loader.py`

---

## Notebook Cell 1 — Understand the PlantVillage dataset

### Explanation

- PlantVillage is a widely used academic dataset for plant disease classification.
- It contains labeled plant leaf images across many crops and disease conditions.
- In LeafGuard AI, it provides a practical starting point for the 38-class classification task.

### Step-by-Step

1. Read the dataset description from a trusted source or paper summary.
2. Identify the crop types represented.
3. Compare the dataset classes with the labels you plan to use in the app.

### 🔵 Try This

- Write a two-sentence summary of why a public dataset is useful in student projects.

### Expected Output

- You can explain where the training images come from.

### ✅ Checkpoint

- Why can dataset bias still matter even when the dataset is popular?

### ⚠️ Common Mistake

- Do not assume PlantVillage perfectly represents real field conditions.

### 📌 Key Point

- The model is only as trustworthy as the data and preprocessing behind it.

## Notebook Cell 2 — Download or reuse a pretrained model

### Explanation

- You may train your own model or start from a pretrained plant disease classifier for faster progress.

### Code to Read / Run

```text
Suggested workflow
------------------
1. Locate a trusted TensorFlow/Keras plant disease model or notebook.
2. Confirm the input size, output classes, and license.
3. Save the model file in a documented location.
4. Record every assumption in `model/model-notes.md`.
```

### 🔵 Try This

- Document the source of your model and whether it uses RGB 224x224 inputs.

### Expected Output

- You know the exact file, class order, and preprocessing assumptions of the model you will use.

### ✅ Checkpoint

- What must you know before integrating any model into Android or FastAPI?

### ⚠️ Common Mistake

- Never integrate a model if you do not know its input shape and class mapping.

### 📌 Key Point

- Documentation is part of model integration.

## Notebook Cell 3 — See the TensorFlow to TFLite conversion commands

### Explanation

- TFLite conversion prepares the model for mobile inference and smaller deployment size.

### Code to Read / Run

```python
import tensorflow as tf

keras_model = tf.keras.models.load_model("plant_disease_model.h5")
converter = tf.lite.TFLiteConverter.from_keras_model(keras_model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()

with open("leafguard_model.tflite", "wb") as output_file:
    output_file.write(tflite_model)
```

### 🔵 Try This

- Run the conversion in a Python environment that has TensorFlow installed.

### Expected Output

- A `.tflite` file is generated for later offline inference.

### ✅ Checkpoint

- Why is TFLite useful on mobile even if you also have a cloud model?

### ⚠️ Common Mistake

- Do not assume every TensorFlow operation converts cleanly; read converter errors carefully.

### 📌 Key Point

- Conversion is the bridge between training-time and deployment-time model formats.

## Notebook Cell 4 — Review the 38 class labels

### Explanation

- The order of labels must exactly match the model output indices.

### Code to Read / Run

```text
0  Apple___Apple_scab
1  Apple___Black_rot
2  Apple___Cedar_apple_rust
3  Apple___healthy
4  Blueberry___healthy
5  Cherry___Powdery_mildew
6  Cherry___healthy
7  Corn___Cercospora_leaf_spot Gray_leaf_spot
8  Corn___Common_rust
9  Corn___Northern_Leaf_Blight
10 Corn___healthy
11 Grape___Black_rot
12 Grape___Esca_(Black_Measles)
13 Grape___Leaf_blight_(Isariopsis_Leaf_Spot)
14 Grape___healthy
15 Orange___Haunglongbing_(Citrus_greening)
16 Peach___Bacterial_spot
17 Peach___healthy
18 Pepper,_bell___Bacterial_spot
19 Pepper,_bell___healthy
20 Potato___Early_blight
21 Potato___Late_blight
22 Potato___healthy
23 Raspberry___healthy
24 Soybean___healthy
25 Squash___Powdery_mildew
26 Strawberry___Leaf_scorch
27 Strawberry___healthy
28 Tomato___Bacterial_spot
29 Tomato___Early_blight
30 Tomato___Late_blight
31 Tomato___Leaf_Mold
32 Tomato___Septoria_leaf_spot
33 Tomato___Spider_mites Two-spotted_spider_mite
34 Tomato___Target_Spot
35 Tomato___Tomato_Yellow_Leaf_Curl_Virus
36 Tomato___Tomato_mosaic_virus
37 Tomato___healthy
```

### 🔵 Try This

- Compare these labels with `model/labels.txt` and note any naming differences.

### Expected Output

- You can map an output index to a human-readable disease class.

### ✅ Checkpoint

- What would happen if label order did not match the model output order?

### ⚠️ Common Mistake

- Never sort labels alphabetically unless the model was trained with that exact order.

### 📌 Key Point

- Index order is a critical part of model correctness.

## Notebook Cell 5 — Integrate the predictor into FastAPI

### Explanation

- The API should preprocess the image, call the predictor, and enrich the result with disease advice.

### Code to Read / Run

```python
def preprocess_image(raw_bytes: bytes) -> np.ndarray:
    image = Image.open(io.BytesIO(raw_bytes)).convert("RGB")
    resized_image = image.resize((224, 224))
    image_array = np.asarray(resized_image, dtype=np.float32) / 255.0
    return np.expand_dims(image_array, axis=0)

disease_name, confidence = predictor.predict(model_input)
metadata = DISEASE_INFO.get(disease_name, fallback_info)
```

### 🔵 Try This

- Check `model/model-notes.md` and write down the required input size and normalization.

### Expected Output

- The backend produces predictions using the real or mock predictor implementation.

### ✅ Checkpoint

- Why must the API preprocessing match training preprocessing?

### ⚠️ Common Mistake

- A wrong normalization rule can destroy prediction quality without causing a visible crash.

### 📌 Key Point

- Preprocessing is part of the model, not a separate optional detail.

## Notebook Cell 6 — Test with real leaf images

### Explanation

- Model evaluation during integration should use images that look different from the training notebook examples when possible.

### Code to Read / Run

```bash
curl -X POST http://127.0.0.1:8000/predict       -F "image=@../sample-images/tomato_early_blight.jpg"

curl -X POST http://127.0.0.1:8000/predict       -F "image=@../sample-images/potato_healthy.jpg"
```

### 🔵 Try This

- Create a small table with image file name, predicted class, confidence, and whether the result seems believable.

### Expected Output

- Different images produce different disease names and confidence values.

### ✅ Checkpoint

- Can you explain why one correct-looking prediction is not enough to validate a model?

### ⚠️ Common Mistake

- Do not judge performance from only one image.

### 📌 Key Point

- Testing multiple images exposes weak spots earlier.

## Notebook Cell 7 — Apply confidence thresholds and fallbacks

### Explanation

- A confidence threshold prevents LeafGuard AI from sounding too certain when the model is unsure.

### Code to Read / Run

```python
CONFIDENCE_THRESHOLD = 0.70

if confidence < CONFIDENCE_THRESHOLD:
    result = {
        "disease": disease_name,
        "confidence": confidence,
        "warning": "Low confidence prediction. Please capture another image in better lighting."
    }
```

### 🔵 Try This

- Set a threshold such as 0.70 or 0.80 and compare how often the warning appears.

### Expected Output

- Low-confidence cases are clearly labeled rather than presented as fully reliable.

### ✅ Checkpoint

- Why is a threshold especially important in agricultural advice tools?

### ⚠️ Common Mistake

- Do not hide low confidence; communicate it honestly to the user.

### 📌 Key Point

- Responsible AI includes uncertainty handling.

## Lab Reflection

- Write down one concept that felt easy.
- Write down one concept that felt confusing.
- Describe one bug you saw and how you fixed it.
- State which file changed the most during this notebook.
- Explain how this week supports the final LeafGuard AI submission.

## Mini Quiz

- What problem does this week solve inside LeafGuard AI?
- Which Java class or Android component did you touch first?
- Which file path in this repository is most relevant to this week?
- What would break if you skipped the validation step?
- How does this week connect to the three-tier architecture?

## Evidence Checklist

- [ ] Capture a screenshot of the completed screen or terminal output.
- [ ] Save one code snippet that proves the feature is wired correctly.
- [ ] Write two sentences in your progress log about what you learned.
- [ ] Record at least one bug and the exact fix you applied.
- [ ] Commit working changes before moving to the next week.

## Next Step

- After this notebook, continue to **[Week 07: Room Database & History](../../roadmap/week-07-room-sqlite-history/README.md)** and connect today's work to the next subsystem.


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

### Related Roadmap Materials
- 📖 [Week 06 README](../../roadmap/week-06-cloud-ml-model/README.md) — Week overview & objectives
- 📝 [Week 06 Exercises](../../roadmap/week-06-cloud-ml-model/exercises.md) — Practice problems
- 💡 [Week 06 Solutions](../../solutions/week-06/README.md) — Reference solutions
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

### Week Progression

| ← Previous | 🏠 | Next → |
|:-----------|:--:|-------:|
| [⬅ Week 05 Notebooks](../week-05/README.md) | [Notebooks Index](../README.md) | [Week 07 Notebooks ➡](../week-07/README.md) |

---
