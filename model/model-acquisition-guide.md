# LeafGuard AI Model Acquisition Guide

> This guide explains how to obtain, validate, convert, optimize, and integrate a plant disease model for the LeafGuard AI Android app.
> It is intentionally long and detailed so students can work through the model pipeline step by step during the 12-week course.

---

## ⚡ Quick Start (5 minutes)

If you just want a working `.tflite` model as fast as possible, run the script below.  
It builds a small MobileNetV2-based stub model converted to TFLite format — enough to test your Android integration before you have a real trained model.

```bash
# From the repository root
pip install tensorflow pillow numpy

python3 model/generate_stub_model.py
```

This creates:
- `android-app/app/src/main/assets/model.tflite` — placeholder TFLite model
- `android-app/app/src/main/assets/labels.txt` — the 10 LeafGuard AI demo class names

> **Important:** The stub model outputs random predictions. Replace it with a real trained model by following Option B (Kaggle PlantVillage) or Option C (Google Colab training) below.

---

## Table of Contents
1. [Why there is no `.tflite` model in this repository](#1-why-there-is-no-tflite-model-in-this-repository)
2. [Option A: Download a pre-trained model from TensorFlow Hub](#2-option-a-download-a-pre-trained-model-from-tensorflow-hub)
3. [Option B: Download and use PlantVillage resources from Kaggle](#3-option-b-download-and-use-plantvillage-resources-from-kaggle)
4. [Option C: Train your own model in Google Colab](#4-option-c-train-your-own-model-in-google-colab)
5. [Model validation before Android integration](#5-model-validation-before-android-integration)
6. [Converting any TensorFlow model to `.tflite`](#6-converting-any-tensorflow-model-to-tflite)
7. [Quantization guide](#7-quantization-guide)
8. [How to add the model to your Android project](#8-how-to-add-the-model-to-your-android-project)
9. [How to create `labels.txt`](#9-how-to-create-labelstxt)
10. [Troubleshooting checklist](#10-troubleshooting-checklist)
11. [Recommended workflow for CSE 2206 students](#11-recommended-workflow-for-cse-2206-students)

## 1. Why there is no `.tflite` model in this repository

There is no ready-made `.tflite` file committed to this repository on purpose.

### Reason 1: Git repositories are not ideal for large model binaries
- TensorFlow Lite models can easily grow from a few megabytes to tens of megabytes.
- Course repositories should remain lightweight and easy to clone.
- Binary model files do not show useful diffs in Git.
- Students may want to experiment with multiple versions of the model.

### Reason 2: Model training depends on data choices
- A plant disease model is only meaningful if you know what dataset was used.
- Different students may choose different crops or different subsets of PlantVillage.
- The label order must match the exact classes used during training.
- Two `.tflite` files with different label orders can look valid but produce wrong results.

### Reason 3: Reproducibility matters more than shipping one opaque file
- In a learning repository, it is better to teach how to obtain or build the model.
- Students should understand preprocessing, label mapping, validation, and conversion.
- This project is a CSE 2206 learning artifact, not just a packaged demo.

### Reason 4: Model licensing and attribution can differ
- TensorFlow Hub models, Kaggle datasets, and community checkpoints can have different licenses.
- A guide is safer than redistributing a file with unclear origin.

### Reason 5: Hardware and optimization choices vary
- Some students will use float32 models.
- Others may prefer dynamic range quantization or full int8 quantization.
- Some will target cloud inference first and offline inference later.

### Bottom line
This repository gives you the app structure, backend structure, labels template, and documentation.
You are expected to either:
1. download a suitable model,
2. train a model yourself,
3. convert it to `.tflite`, and
4. verify the output matches `labels.txt` before using it in Android.

## 2. Option A: Download a pre-trained model from TensorFlow Hub

TensorFlow Hub is the fastest starting point when you want a proven image model architecture.
The important note is that most Hub models are generic image models, not plant-disease-specific models.
So Option A is best used in one of two ways:
- for quick experimentation and learning the pipeline,
- or as a transfer-learning base model for your own plant disease classifier.

### Recommended TensorFlow Hub URLs

#### MobileNetV2 image classification model
- URL: https://tfhub.dev/google/imagenet/mobilenet_v2_100_224/classification/5
- Use case: quick classification experiments and learning the TensorFlow pipeline.

#### MobileNetV2 feature vector model
- URL: https://tfhub.dev/google/tf2-preview/mobilenet_v2/feature_vector/4
- Use case: transfer learning for your own custom plant disease classes.

#### EfficientNet Lite0 feature vector model
- URL: https://tfhub.dev/tensorflow/efficientnet/lite0/feature-vector/2
- Use case: mobile-friendly transfer learning with strong accuracy/size tradeoff.

### When to choose Option A
- You want a reliable base architecture quickly.
- You are still learning transfer learning.
- You do not yet have a trained plant-disease checkpoint.
- You want to build the model pipeline before worrying about data collection.

### Step-by-step: download a Hub model for transfer learning

#### Step 1: create a Python environment
```bash
python -m venv .venv-model
source .venv-model/bin/activate
pip install --upgrade pip
pip install tensorflow==2.14.0 tensorflow-hub==0.16.1 pillow numpy matplotlib
```

#### Step 2: create a script called `download_hub_base.py`
```python
import tensorflow_hub as hub

hub_url = "https://tfhub.dev/google/tf2-preview/mobilenet_v2/feature_vector/4"
layer = hub.KerasLayer(hub_url, input_shape=(224, 224, 3), trainable=False)
print("Loaded TF Hub layer:", layer)
```

#### Step 3: run the script
```bash
python download_hub_base.py
```

If the layer loads successfully, you have confirmed network access and TensorFlow Hub compatibility.

#### Step 4: build a classifier on top of the Hub feature extractor
```python
import tensorflow as tf
import tensorflow_hub as hub

NUM_CLASSES = 10
IMAGE_SIZE = 224

base = hub.KerasLayer(
    "https://tfhub.dev/google/tf2-preview/mobilenet_v2/feature_vector/4",
    input_shape=(IMAGE_SIZE, IMAGE_SIZE, 3),
    trainable=False,
)

model = tf.keras.Sequential([
    tf.keras.layers.Input(shape=(IMAGE_SIZE, IMAGE_SIZE, 3)),
    base,
    tf.keras.layers.Dropout(0.2),
    tf.keras.layers.Dense(NUM_CLASSES, activation="softmax"),
])

model.compile(
    optimizer=tf.keras.optimizers.Adam(1e-3),
    loss="categorical_crossentropy",
    metrics=["accuracy"],
)

model.summary()
```

#### Step 5: train or fine-tune using your plant disease dataset
At this point, TensorFlow Hub gives you the reusable feature extractor.
You still need plant disease images and labels for meaningful predictions.

### Pros of Option A
- Fastest path to a working transfer-learning pipeline.
- Well-tested architecture.
- Easy to convert to `.tflite` later.
- Good teaching value for Week 09 and Week 10.

### Cons of Option A
- The raw Hub classification model is not a plant disease model.
- You still need to train the final classifier head on plant data.
- Label mapping is your responsibility.

## 3. Option B: Download and use PlantVillage resources from Kaggle

Kaggle is one of the most common sources for plant disease datasets and community notebooks.
For LeafGuard AI, the best-known dataset is PlantVillage.

### Main PlantVillage dataset URL
- https://www.kaggle.com/datasets/emmarex/plantdisease

### Why PlantVillage is popular
- It is widely used in academic examples.
- It already contains labeled crop disease folders.
- Many tutorials, notebooks, and reports reference it.
- It is a practical starting point for CSE 2206 student projects.

### Important warning about Kaggle
Kaggle content changes over time.
Notebook outputs, file names, and public model artifacts can move or be renamed.
So always verify folder names and class names after download.

### Step-by-step: download PlantVillage from Kaggle

#### Step 1: install the Kaggle CLI
```bash
pip install kaggle
```

#### Step 2: create your Kaggle API token
1. Sign in to Kaggle.
2. Open **Account** settings.
3. Click **Create New API Token**.
4. Kaggle downloads a file named `kaggle.json`.

#### Step 3: place the token where the CLI expects it
**Linux / macOS:**
```bash
mkdir -p ~/.kaggle
mv kaggle.json ~/.kaggle/kaggle.json
chmod 600 ~/.kaggle/kaggle.json
```

**Windows PowerShell:**
```powershell
mkdir $HOME\.kaggle
move kaggle.json $HOME\.kaggle\kaggle.json
```

#### Step 4: download the dataset
```bash
kaggle datasets download -d emmarex/plantdisease
```

#### Step 5: unzip it
```bash
unzip plantdisease.zip -d plantvillage-data
```

#### Step 6: inspect the class folders
```bash
find plantvillage-data -maxdepth 2 -type d | sort
```

### Suggested subset for beginners
PlantVillage has many classes, but beginners often start with a smaller subset such as:
- Tomato___healthy
- Tomato___Early_blight
- Tomato___Late_blight
- Potato___healthy
- Potato___Early_blight
- Potato___Late_blight
- Corn___healthy
- Corn___Northern_Leaf_Blight
- Corn___Cercospora_leaf_spot Gray_leaf_spot
- Apple___Apple_scab

This 10-class subset aligns well with the backend starter content already added in this repository.

### How to use Kaggle resources without training from scratch
You have three practical Kaggle workflows:

#### Workflow A: download only the dataset
- Best if you want full control over training.
- Use the Google Colab code from Option C.

#### Workflow B: reuse a Kaggle notebook architecture
- Search Kaggle notebooks for PlantVillage MobileNetV2 or EfficientNet examples.
- Recreate the same architecture in Colab or locally.
- Train your own weights from the dataset.

#### Workflow C: use a community checkpoint carefully
- Some notebooks share `.h5`, `.keras`, or SavedModel outputs.
- Verify class order.
- Verify preprocessing.
- Verify whether the last layer uses softmax.
- Validate with test images before converting to `.tflite`.

### Verification questions before trusting a Kaggle checkpoint
- What exact classes were used?
- In what order?
- Was input normalized to `[0,1]`, `[-1,1]`, or ImageNet mean/std?
- What image size was used?
- Does the output already apply softmax?
- How accurate was the model on validation data?

## 4. Option C: Train your own model in Google Colab

This is the most educational path and the best path for a course project.
Google Colab gives you:
- free GPU access in many cases,
- an easy notebook interface,
- simple Google Drive integration,
- a reproducible training workflow.

The following example trains a MobileNetV2-based classifier on PlantVillage-style folder data and converts it to TensorFlow Lite.

### Colab setup instructions
1. Open https://colab.research.google.com/
2. Create a new notebook.
3. Rename it to `leafguard_model_training.ipynb`.
4. Click **Runtime > Change runtime type**.
5. Select **GPU** if available.
6. Mount Google Drive if you want to save checkpoints.

### Complete Colab code - environment setup
```python
# ============================================================
# LeafGuard AI - Plant Disease Model Training in Google Colab
# ============================================================

!pip install -q kaggle tensorflow==2.14.0

import os
import json
import zipfile
import random
import shutil
import pathlib
import numpy as np
import matplotlib.pyplot as plt
import tensorflow as tf

from google.colab import files
from tensorflow.keras import layers, models
from tensorflow.keras.applications import MobileNetV2
from tensorflow.keras.callbacks import EarlyStopping, ModelCheckpoint, ReduceLROnPlateau

print("TensorFlow version:", tf.__version__)
print("GPU available:", tf.config.list_physical_devices('GPU'))
```

### Complete Colab code - upload Kaggle API token
```python
files.upload()  # Upload kaggle.json when prompted

os.makedirs('/root/.kaggle', exist_ok=True)
shutil.move('kaggle.json', '/root/.kaggle/kaggle.json')
os.chmod('/root/.kaggle/kaggle.json', 0o600)
```

### Complete Colab code - download PlantVillage dataset
```python
!kaggle datasets download -d emmarex/plantdisease
!unzip -q plantdisease.zip -d plantvillage_raw
```

### Complete Colab code - inspect folders
```python
base_dir = pathlib.Path('plantvillage_raw')
all_dirs = sorted([str(p) for p in base_dir.glob('*') if p.is_dir()])
print("Found class folders:")
for item in all_dirs[:50]:
    print(item)
```

### Complete Colab code - optional class subset
```python
selected_classes = [
    'Tomato___healthy',
    'Tomato___Early_blight',
    'Tomato___Late_blight',
    'Potato___healthy',
    'Potato___Early_blight',
    'Potato___Late_blight',
    'Corn___healthy',
    'Corn___Northern_Leaf_Blight',
    'Corn___Cercospora_leaf_spot Gray_leaf_spot',
    'Apple___Apple_scab',
]

subset_root = pathlib.Path('plantvillage_subset')
subset_root.mkdir(exist_ok=True)

for class_name in selected_classes:
    source_dir = base_dir / class_name
    target_dir = subset_root / class_name
    if source_dir.exists():
        shutil.copytree(source_dir, target_dir, dirs_exist_ok=True)

print("Subset ready at:", subset_root)
```

### Complete Colab code - create train/validation datasets
```python
IMAGE_SIZE = 224
BATCH_SIZE = 32
SEED = 42
DATA_DIR = 'plantvillage_subset'  # change to plantvillage_raw if using all classes

train_ds = tf.keras.utils.image_dataset_from_directory(
    DATA_DIR,
    validation_split=0.2,
    subset='training',
    seed=SEED,
    image_size=(IMAGE_SIZE, IMAGE_SIZE),
    batch_size=BATCH_SIZE,
)

val_ds = tf.keras.utils.image_dataset_from_directory(
    DATA_DIR,
    validation_split=0.2,
    subset='validation',
    seed=SEED,
    image_size=(IMAGE_SIZE, IMAGE_SIZE),
    batch_size=BATCH_SIZE,
)

class_names = train_ds.class_names
print("Class names:", class_names)
print("Number of classes:", len(class_names))
```

### Complete Colab code - performance optimizations
```python
AUTOTUNE = tf.data.AUTOTUNE
train_ds = train_ds.cache().shuffle(1000).prefetch(buffer_size=AUTOTUNE)
val_ds = val_ds.cache().prefetch(buffer_size=AUTOTUNE)
```

### Complete Colab code - visualize examples
```python
plt.figure(figsize=(12, 12))
for images, labels in train_ds.take(1):
    for i in range(9):
        ax = plt.subplot(3, 3, i + 1)
        plt.imshow(images[i].numpy().astype('uint8'))
        plt.title(class_names[labels[i]])
        plt.axis('off')
plt.show()
```

### Complete Colab code - build MobileNetV2 transfer learning model
```python
num_classes = len(class_names)

data_augmentation = tf.keras.Sequential([
    layers.RandomFlip('horizontal'),
    layers.RandomRotation(0.05),
    layers.RandomZoom(0.10),
])

preprocess_input = tf.keras.applications.mobilenet_v2.preprocess_input
base_model = MobileNetV2(input_shape=(IMAGE_SIZE, IMAGE_SIZE, 3), include_top=False, weights='imagenet')
base_model.trainable = False

inputs = layers.Input(shape=(IMAGE_SIZE, IMAGE_SIZE, 3))
x = data_augmentation(inputs)
x = preprocess_input(x)
x = base_model(x, training=False)
x = layers.GlobalAveragePooling2D()(x)
x = layers.Dropout(0.2)(x)
outputs = layers.Dense(num_classes, activation='softmax')(x)

model = tf.keras.Model(inputs, outputs)

model.compile(
    optimizer=tf.keras.optimizers.Adam(learning_rate=1e-3),
    loss='sparse_categorical_crossentropy',
    metrics=['accuracy'],
)

model.summary()
```

### Complete Colab code - train the classifier head
```python
early_stopping = EarlyStopping(monitor='val_loss', patience=4, restore_best_weights=True)
reduce_lr = ReduceLROnPlateau(monitor='val_loss', factor=0.2, patience=2, verbose=1)
checkpoint = ModelCheckpoint('leafguard_best.keras', monitor='val_accuracy', save_best_only=True)

history = model.fit(
    train_ds,
    validation_data=val_ds,
    epochs=10,
    callbacks=[early_stopping, reduce_lr, checkpoint],
)
```

### Complete Colab code - optional fine-tuning
```python
base_model.trainable = True

for layer in base_model.layers[:-30]:
    layer.trainable = False

model.compile(
    optimizer=tf.keras.optimizers.Adam(learning_rate=1e-5),
    loss='sparse_categorical_crossentropy',
    metrics=['accuracy'],
)

fine_tune_history = model.fit(
    train_ds,
    validation_data=val_ds,
    epochs=5,
    callbacks=[early_stopping, reduce_lr, checkpoint],
)
```

### Complete Colab code - evaluate the model
```python
loss, accuracy = model.evaluate(val_ds)
print('Validation loss:', loss)
print('Validation accuracy:', accuracy)
```

### Complete Colab code - save the trained Keras model
```python
model.save('leafguard_final.keras')
with open('labels.txt', 'w') as f:
    for name in class_names:
        f.write(name + '
')
```

### Complete Colab code - convert to TensorFlow Lite
```python
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

with open('leafguard_model.tflite', 'wb') as f:
    f.write(tflite_model)

print('Saved leafguard_model.tflite')
```

### Complete Colab code - dynamic range quantization
```python
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
quant_tflite_model = converter.convert()

with open('leafguard_model_dynamic.tflite', 'wb') as f:
    f.write(quant_tflite_model)

print('Saved leafguard_model_dynamic.tflite')
```

### Complete Colab code - download the final files to your computer
```python
from google.colab import files
files.download('leafguard_model.tflite')
files.download('labels.txt')
files.download('leafguard_final.keras')
```

### Notes about the Colab pipeline
- `image_dataset_from_directory()` automatically infers class names from folder names.
- The order of `class_names` becomes the label order for Android and backend.
- If you retrain with a different subset, you must regenerate `labels.txt`.
- If you switch preprocessing, document it immediately in `model/model-notes.md`.

## 5. Model validation before Android integration

Never copy a `.tflite` file into Android without testing it first in Python.
This is the fastest way to catch:
- wrong input size,
- wrong normalization,
- wrong label order,
- unexpected output shape,
- conversion mistakes.

### Validation script for a `.tflite` model
```python
import numpy as np
from PIL import Image
import tensorflow as tf

MODEL_PATH = 'leafguard_model.tflite'
LABELS_PATH = 'labels.txt'
IMAGE_PATH = 'sample-leaf.jpg'
IMAGE_SIZE = 224

with open(LABELS_PATH, 'r') as f:
    labels = [line.strip() for line in f if line.strip()]

interpreter = tf.lite.Interpreter(model_path=MODEL_PATH)
interpreter.allocate_tensors()

input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

print('Input details:', input_details)
print('Output details:', output_details)

image = Image.open(IMAGE_PATH).convert('RGB').resize((IMAGE_SIZE, IMAGE_SIZE))
image_array = np.array(image, dtype=np.float32) / 255.0
input_data = np.expand_dims(image_array, axis=0)

interpreter.set_tensor(input_details[0]['index'], input_data)
interpreter.invoke()
output_data = interpreter.get_tensor(output_details[0]['index'])[0]

predicted_index = int(np.argmax(output_data))
confidence = float(output_data[predicted_index])
print('Predicted class index:', predicted_index)
print('Predicted label:', labels[predicted_index])
print('Confidence:', confidence)
```

### What to verify in the validation output
- Does the input shape look like `(1, 224, 224, 3)`?
- Does the output shape match the number of labels?
- Does the predicted class make sense for a known test image?
- Is confidence in the expected range?
- Do repeated runs return stable results?

### Extra validation using several test images
Create a small folder of manually verified test images and run them one by one.
If the model gives obviously wrong outputs for clean images, investigate before using it in the app.

## 6. Converting any TensorFlow model to `.tflite`

The most common conversion path is from a Keras model.

### Basic conversion from `.keras`
```python
import tensorflow as tf

model = tf.keras.models.load_model('leafguard_final.keras')
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

with open('leafguard_model.tflite', 'wb') as f:
    f.write(tflite_model)
```

### Conversion from SavedModel directory
```python
import tensorflow as tf

converter = tf.lite.TFLiteConverter.from_saved_model('saved_model_directory')
tflite_model = converter.convert()

with open('leafguard_model.tflite', 'wb') as f:
    f.write(tflite_model)
```

### Conversion settings you may need
```python
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
converter.target_spec.supported_ops = [tf.lite.OpsSet.TFLITE_BUILTINS]
converter.inference_input_type = tf.float32
converter.inference_output_type = tf.float32
tflite_model = converter.convert()
```

Use float32 first because it is the easiest to debug.
Once the model works, try quantization.

### If conversion fails
Common reasons include:
- unsupported TensorFlow operations,
- custom layers without TFLite support,
- incompatible preprocessing embedded in the graph,
- version mismatch between training and conversion TensorFlow builds.

Suggested fixes:
- simplify the model,
- export from a stable TensorFlow version,
- remove custom ops if possible,
- test conversion on a small proof-of-concept first.

## 7. Quantization guide

Quantization reduces model size and often improves mobile inference speed.
However, it can also reduce accuracy if done carelessly.

### Quantization types to know

#### 1. Float32 (no quantization)
- easiest to debug
- usually largest file size
- best first step for students

#### 2. Dynamic range quantization
- easy to apply
- often much smaller
- usually minimal code changes

#### 3. Float16 quantization
- reduces size
- still works well on many devices
- good intermediate option

#### 4. Full integer quantization (int8)
- strongest optimization
- requires representative dataset for calibration
- needs careful validation

### Dynamic range quantization code
```python
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
quant_model = converter.convert()

with open('leafguard_dynamic.tflite', 'wb') as f:
    f.write(quant_model)
```

### Float16 quantization code
```python
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
converter.target_spec.supported_types = [tf.float16]
quant_model = converter.convert()

with open('leafguard_float16.tflite', 'wb') as f:
    f.write(quant_model)
```

### Full int8 quantization code
```python
import numpy as np


def representative_dataset():
    for images, _ in train_ds.take(100):
        for i in range(images.shape[0]):
            sample = tf.cast(images[i:i+1], tf.float32) / 255.0
            yield [sample]

converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
converter.representative_dataset = representative_dataset
converter.target_spec.supported_ops = [tf.lite.OpsSet.TFLITE_BUILTINS_INT8]
converter.inference_input_type = tf.int8
converter.inference_output_type = tf.int8
int8_model = converter.convert()

with open('leafguard_int8.tflite', 'wb') as f:
    f.write(int8_model)
```

### Quantization rule of thumb for this course
- Start with float32.
- Validate it carefully.
- Try dynamic range quantization next.
- Only attempt int8 when the float32 pipeline is fully understood.

## 8. How to add the model to your Android project

Once the model has been validated, copy it into the Android app.

### Recommended Android asset locations
- Model file: `android-app/app/src/main/assets/model.tflite`
- Labels file: `android-app/app/src/main/assets/labels.txt`

### Why the `assets` folder?
- Android can package raw model files directly.
- TFLite works well with `AssetFileDescriptor` and memory mapping.
- The project already includes `aaptOptions { noCompress "tflite" }` so the model stays efficient to load.

### Copy commands
```bash
mkdir -p android-app/app/src/main/assets
cp leafguard_model.tflite android-app/app/src/main/assets/model.tflite
cp labels.txt android-app/app/src/main/assets/labels.txt
```

### Android checklist after copying the model
- Confirm `model.tflite` exists in `app/src/main/assets/`.
- Confirm `labels.txt` exists in `app/src/main/assets/`.
- Confirm label order matches the model output order.
- Confirm `TFLiteClassifier.java` uses the same input size.
- Confirm normalization in Android matches Python validation.

### Android loading reminder
The starter `TFLiteClassifier.java` added to this repository expects:
- `model.tflite`
- `labels.txt`
- RGB input
- size `224 x 224`
- normalization `pixel / 255.0f`

If your model uses different assumptions, update the classifier code and document the changes in `model/model-notes.md`.

## 9. How to create `labels.txt`

`labels.txt` must contain one class label per line.
The order must exactly match the training order used by the model.

### Example 10-class labels file
```text
Apple___Apple_scab
Corn___Cercospora_leaf_spot Gray_leaf_spot
Corn___Northern_Leaf_Blight
Corn___healthy
Potato___Early_blight
Potato___Late_blight
Potato___healthy
Tomato___Early_blight
Tomato___Late_blight
Tomato___healthy
```

### Best way to generate `labels.txt`
If you use `image_dataset_from_directory()`, TensorFlow gives you `train_ds.class_names`.
Write that list directly to file so the order is guaranteed.

```python
with open('labels.txt', 'w') as f:
    for label in class_names:
        f.write(label + '
')
```

### Common `labels.txt` mistakes
- Sorting labels alphabetically after training when training used a different order.
- Manually editing labels and forgetting to keep the same order.
- Adding comments into the middle of the real label list.
- Using friendly labels in Android but training labels in Python without a mapping layer.

## 10. Troubleshooting checklist

### Issue 1: Model file loads in Python but not Android
- Likely fix: Check that the file is in `app/src/main/assets/` and that `noCompress "tflite"` is enabled.
- Extra note: write the final decision into `model/model-notes.md` so your future self remembers it.

### Issue 2: Predictions are always wrong
- Likely fix: Verify image size, normalization, and label order.
- Extra note: write the final decision into `model/model-notes.md` so your future self remembers it.

### Issue 3: Model predicts the same class for every image
- Likely fix: Check preprocessing, training balance, and whether the final layer uses softmax.
- Extra note: write the final decision into `model/model-notes.md` so your future self remembers it.

### Issue 4: Conversion to `.tflite` fails
- Likely fix: Simplify the model and confirm TensorFlow version compatibility.
- Extra note: write the final decision into `model/model-notes.md` so your future self remembers it.

### Issue 5: Confidence values are tiny or weird
- Likely fix: Inspect whether you are reading logits instead of softmax probabilities.
- Extra note: write the final decision into `model/model-notes.md` so your future self remembers it.

### Issue 6: Android app crashes on model load
- Likely fix: Catch `IOException` and confirm the asset file name matches exactly.
- Extra note: write the final decision into `model/model-notes.md` so your future self remembers it.

### Issue 7: Backend and Android disagree on predictions
- Likely fix: Compare preprocessing pipelines side by side and test the same image in both environments.
- Extra note: write the final decision into `model/model-notes.md` so your future self remembers it.

### Issue 8: Labels look shifted by one class
- Likely fix: Open `labels.txt` and compare it to the exact `class_names` from training.
- Extra note: write the final decision into `model/model-notes.md` so your future self remembers it.

### Issue 9: Quantized model lost too much accuracy
- Likely fix: Fall back to float32 or dynamic range quantization and re-evaluate.
- Extra note: write the final decision into `model/model-notes.md` so your future self remembers it.

### Issue 10: Large model makes app slow
- Likely fix: Try MobileNetV2, EfficientNet Lite, or dynamic range quantization.
- Extra note: write the final decision into `model/model-notes.md` so your future self remembers it.

## 11. Recommended workflow for CSE 2206 students

### Minimum path
1. Use PlantVillage subset.
2. Train a MobileNetV2 transfer-learning model in Colab.
3. Export `.keras`, `.tflite`, and `labels.txt`.
4. Validate with the Python script.
5. Copy the model into `android-app/app/src/main/assets/`.
6. Test it in `TFLiteClassifier.java`.

### Safer path
1. Start with backend mock mode.
2. Train the model separately.
3. Validate in Python.
4. Add the model to Android only after validation succeeds.

### Best report-writing path
1. Record dataset URL.
2. Record selected class list.
3. Record image size.
4. Record preprocessing formula.
5. Record final validation accuracy.
6. Record conversion settings.
7. Record whether you used quantization.
8. Record final `.tflite` size.

## Final reminders
- Do not trust a model you have not validated yourself.
- Do not change label order casually.
- Do not mix preprocessing formulas between Python and Android.
- Always test with known images before demo day.
- Keep `model/model-notes.md` updated every time you change the model pipeline.

## FAQ

### Should I use all PlantVillage classes?
Not necessarily. A smaller, well-understood subset is often better for a semester project.

### Is MobileNetV2 good enough?
Yes. It is a strong baseline for mobile image classification and usually easier to deploy than heavier models.

### Can I use EfficientNet instead?
Yes, especially EfficientNet Lite variants, but always validate conversion and device performance.

### Do I need GPU on Android?
No. Start with CPU inference first. GPU delegation is optional optimization.

### What if my backend model is Keras and Android model is TFLite?
That is normal. Validate both on the same images and compare outputs.

### Can I use a PyTorch model?
Yes, but you will need an export path to TensorFlow Lite or a different mobile runtime.

### How many training images do I need?
More is generally better, but clean labels and balanced classes matter as much as raw count.

### Why are healthy classes important?
They teach the model when not to over-diagnose disease.

### Should I augment the data?
Usually yes, but keep augmentation realistic so leaves still look natural.

### Do I need a test set separate from validation?
For stronger reporting, yes. At minimum, keep a clean validation set and some manual test images.

### Why is label order repeated so often in this guide?
Because wrong label order is one of the easiest ways to silently break a classifier.

### Can I hardcode human-friendly names in Android?
Yes, but use a mapping table and keep the raw model labels intact.
