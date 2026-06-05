# ML Code Examples

## 1. Image preprocessing concept

```python
from PIL import Image
import numpy as np

CLASS_NAMES = ["Tomato___healthy", "Tomato___Late_blight", "Potato___Early_blight"]

def preprocess_image(path: str):
    image = Image.open(path).convert("RGB").resize((224, 224))
    arr = np.asarray(image).astype("float32") / 255.0
    return np.expand_dims(arr, axis=0)
```

## 2. Mock classifier for early integration

```python
def mock_predict(image_array):
    return {
        "label": "Tomato___healthy",
        "confidence": 0.82,
        "warning": "Mock classifier used. Replace with trained model."
    }
```

## 3. TFLite conversion outline

```python
import tensorflow as tf

model = tf.keras.models.load_model("leafguard_model.keras")
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()

with open("leafguard_model.tflite", "wb") as f:
    f.write(tflite_model)
```

## 4. Android tensor shape rule

Most image classifiers expect:

```text
Input:  [1, 224, 224, 3]
Output: [1, number_of_classes]
```

If your output class order differs from `labels.txt`, your app will show wrong disease names even if the model predicts correctly.
