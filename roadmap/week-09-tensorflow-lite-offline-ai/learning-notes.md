# Week 09: Learning Notes - TensorFlow Lite Offline AI

> **Kotlin-first & accuracy note:** The shipped classifier is `TFLiteClassifier` (Kotlin primary: `android-app-kotlin/.../ml/TFLiteClassifier.kt`; Java secondary twin). It loads `assets/model.tflite` + `assets/labels.txt`, resizes to **224×224**, uses **RGB floats 0..1**, and picks the class with **argmax** over 10 outputs. Because the committed `model.tflite` is a **text placeholder**, the classifier catches the load error and falls back to a **green-channel heuristic** so the app still runs. Replace it with a real model via [`model/model-acquisition-guide.md`](../../model/model-acquisition-guide.md) and [`model/generate_stub_model.py`](../../model/generate_stub_model.py).

## Table of Contents

1. [Week Goal and Big Picture](#1-week-goal-and-big-picture)
2. [TensorFlow Lite Fundamentals](#2-tensorflow-lite-fundamentals)
3. [Where to Get a `.tflite` Model (Model Sourcing Guide)](#3-where-to-get-a-tflite-model-model-sourcing-guide)
4. [Understanding Model Files, Labels, and Metadata](#4-understanding-model-files-labels-and-metadata)
5. [Model Conversion Deep Dive](#5-model-conversion-deep-dive)
6. [Android Project Setup for Offline AI](#6-android-project-setup-for-offline-ai)
7. [Input Shapes, Output Shapes, and Tensor Basics](#7-input-shapes-output-shapes-and-tensor-basics)
8. [Normalization Strategies - Why They Must Match Training](#8-normalization-strategies---why-they-must-match-training)
9. [Building the Preprocessing Pipeline in Java](#9-building-the-preprocessing-pipeline-in-java)
10. [Running Inference and Reading Results](#10-running-inference-and-reading-results)
11. [GPU Delegate and NNAPI Acceleration](#11-gpu-delegate-and-nnapi-acceleration)
12. [Handling Inference Errors and Low-Confidence Results](#12-handling-inference-errors-and-low-confidence-results)
13. [Complete `TFLiteClassifier` Implementation](#13-complete-tfliteclassifier-implementation)
14. [Comparing Cloud vs Offline - Architecture Decision](#14-comparing-cloud-vs-offline---architecture-decision)
15. [Benchmarking, Optimization, and Best Practices](#15-benchmarking-optimization-and-best-practices)
16. [CSE 2206 Viva and Exam Preparation](#16-cse-2206-viva-and-exam-preparation)
17. [Summary](#17-summary)
18. [Glossary](#18-glossary)
19. [Quick Revision Checklist](#19-quick-revision-checklist)

---

## 1. Week Goal and Big Picture

Week 09 introduces **offline machine learning on Android** using **TensorFlow Lite (TFLite)**.

In earlier weeks, LeafGuard AI sent images to a backend for prediction.
That cloud approach is useful, but it depends on an internet connection and a running server.
This week adds a second path: **run the model directly on the phone**.

That means your app can:

- classify a plant leaf even with no internet,
- respond faster because there is no network round trip,
- keep the image on the device for better privacy,
- continue working in rural areas with weak connectivity.

For CSE 2206 students, this week connects multiple ideas:

- Android assets management,
- Java file I/O,
- bitmap image processing,
- background threading,
- performance measurement,
- practical mobile AI deployment.

### Why this week matters for LeafGuard AI

A real farmer or field worker may not always have stable internet.
If your app only supports cloud inference, then the app becomes unreliable in exactly the places where it is most useful.
TensorFlow Lite solves that problem by packaging the model inside the Android app.

### What you should be able to do by the end of the week

By the end of Week 09, you should be able to:

1. obtain or create a `.tflite` model,
2. add that model to your Android project,
3. load the model with `Interpreter`,
4. preprocess a `Bitmap` into the input tensor format the model expects,
5. run inference in Java,
6. interpret output probabilities correctly,
7. handle low-confidence predictions safely,
8. benchmark CPU vs GPU delegate performance,
9. compare offline inference with cloud inference.

### LeafGuard architecture this week

```text
Captured Leaf Image
        |
        v
+-------------------+
| Bitmap Processing |
+-------------------+
        |
        v
+-------------------+
| TFLiteClassifier  |
| - load model      |
| - preprocess      |
| - infer           |
| - postprocess     |
+-------------------+
        |
        v
Prediction Result + Confidence + Latency
```

### Key take-away

**Offline AI is not just "add a model and call run()".**
The hardest parts are usually:

- getting a compatible model,
- matching preprocessing exactly,
- dealing with errors and low confidence,
- keeping inference off the main thread,
- optimizing performance for different devices.

---

## 2. TensorFlow Lite Fundamentals

### What is TensorFlow Lite?

**TensorFlow Lite** is a lightweight machine learning runtime designed for:

- Android phones,
- iPhones,
- embedded devices,
- Raspberry Pi,
- edge AI systems.

A normal TensorFlow training model may be too large or too slow for a mobile app.
TFLite converts that model into a smaller, more efficient format suitable for on-device inference.

### Important idea: training vs inference

In ML, there are two different phases:

| Phase | Where it usually happens | Purpose |
|------|---------------------------|---------|
| Training | Laptop, cloud GPU, Colab | Learn weights from data |
| Inference | Android app, server, edge device | Use learned weights to make predictions |

LeafGuard AI this week focuses on **inference**, not training.

### Why TFLite is good for Android

TFLite is useful because it offers:

- small model file sizes,
- lower memory usage,
- hardware acceleration options,
- offline operation,
- direct Java API access.

### TFLite file format

The model usually ends with:

```text
model.tflite
```

This file contains the network structure and weights in a FlatBuffer format.
Android does not read the original `.h5` or `SavedModel` directly during inference.
It reads the converted `.tflite` file.

### Typical inference pipeline

A mobile image classification pipeline looks like this:

1. Load the model file from `assets/`.
2. Load labels from `labels.txt`.
3. Resize image to the correct input size.
4. Convert pixels into the expected tensor format.
5. Normalize pixel values.
6. Run `Interpreter.run()`.
7. Find the highest output probability.
8. Map that index to a label.
9. Show disease name, confidence, and latency.

### Typical performance numbers

These are rough numbers for a medium-size plant disease classifier on phones:

| Hardware path | Typical latency |
|---------------|-----------------|
| CPU only | ~200-500 ms |
| GPU delegate | ~50-150 ms |
| NNAPI delegate | device-dependent, often ~80-200 ms |

These numbers vary based on:

- model size,
- quantization level,
- phone chipset,
- Android version,
- background CPU load.

### Advantages of offline AI

- Works without internet.
- Better privacy because images stay on the device.
- Faster in rural or low-bandwidth environments.
- No server cost for each prediction.
- Easier demo during viva when Wi-Fi is unstable.

### Limitations of offline AI

- Model must fit on the device.
- Very large models may be too slow.
- Updating the model may require an app update.
- Some phones may not support all delegates equally.
- Accuracy may drop if you use aggressive quantization.

### Core TensorFlow Lite classes you will meet

#### `Interpreter`

This is the main runtime class used to run inference.

```java
Interpreter interpreter = new Interpreter(modelBuffer);
```

#### `Interpreter.Options`

Used for performance configuration.

```java
Interpreter.Options options = new Interpreter.Options();
```

You use it to:

- add delegates,
- set number of threads,
- enable NNAPI,
- tune performance.

#### Delegates

A **delegate** hands part of inference execution to optimized hardware-specific code.
Examples:

- GPU delegate,
- NNAPI delegate.

We will study delegates in detail later.

---

## 3. Where to Get a `.tflite` Model (Model Sourcing Guide)

This is one of the most important missing pieces in many student projects.
Students often understand how to call `Interpreter.run()`, but they do not know **where the model actually comes from**.

There are three practical paths.

### Option A: Start from TensorFlow Hub / official TensorFlow tutorials

A good practical path is to begin from the TensorFlow Hub tutorial for plant disease detection:

- TensorFlow Hub tutorial: `https://www.tensorflow.org/hub/tutorials/cropnet_on_device`
- TensorFlow Hub home: `https://tfhub.dev/`
- TensorFlow Datasets PlantVillage page: `https://www.tensorflow.org/datasets/catalog/plant_village`

#### Why this option is good

- Uses official TensorFlow tooling.
- Teaches the full pipeline from data to on-device model.
- Easier to defend in a viva because it is based on TensorFlow documentation.
- Lets you adapt a proven image model to plant disease classification.

#### Important clarification

There is not always a single official one-click download called "PlantVillage.tflite" on TensorFlow Hub.
A more realistic workflow is:

1. use a Hub-compatible image model or the CropNet tutorial,
2. fine-tune it on a plant disease dataset,
3. export to TFLite.

That still counts as a TensorFlow Hub-based model sourcing path.

#### Step-by-step process for Option A

1. Open `https://www.tensorflow.org/hub/tutorials/cropnet_on_device`.
2. Read how TensorFlow Hub feature extractors are used for image classification.
3. Review the PlantVillage dataset info at `https://www.tensorflow.org/datasets/catalog/plant_village`.
4. Run the tutorial in Colab or your local Python environment.
5. Replace dataset-loading steps if needed so they point to the PlantVillage dataset classes you want.
6. Train or fine-tune the model for your chosen disease classes.
7. Export the final model as `.tflite`.
8. Test the `.tflite` file in Python before moving it into Android.

#### What files you should expect after this process

You should end up with something like:

```text
model.tflite
labels.txt
```

Sometimes you may also have:

```text
saved_model/
training_notebook.ipynb
class_names.json
```

### Option B: Train your own model using Google Colab

This is the most common student-friendly option.
Google Colab gives you a Python notebook environment in the browser.
You can use the PlantVillage dataset from Kaggle and train your own classifier.

#### Useful resources

- Google Colab: `https://colab.research.google.com/`
- Kaggle PlantVillage dataset: search for `PlantVillage` on `https://www.kaggle.com/`
- TensorFlow Datasets PlantVillage: `https://www.tensorflow.org/datasets/catalog/plant_village`

#### Why Colab is popular

- No local Python setup needed.
- Free GPU is often available.
- Easy to share notebook screenshots as evidence.
- Perfect for CSE 2206 documentation and viva discussion.

#### Example high-level Colab workflow

1. Upload or mount the dataset.
2. Build an image classifier with Keras.
3. Train on selected classes.
4. Evaluate accuracy on validation data.
5. Save the model as `.h5` or `SavedModel`.
6. Convert to `.tflite`.
7. Download `model.tflite` and `labels.txt`.

#### Example Keras training skeleton

```python
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers

IMG_SIZE = (224, 224)
BATCH_SIZE = 32
NUM_CLASSES = 6

train_ds = tf.keras.utils.image_dataset_from_directory(
    "plantvillage_subset/train",
    image_size=IMG_SIZE,
    batch_size=BATCH_SIZE
)

val_ds = tf.keras.utils.image_dataset_from_directory(
    "plantvillage_subset/val",
    image_size=IMG_SIZE,
    batch_size=BATCH_SIZE
)

model = keras.Sequential([
    layers.Rescaling(1./255, input_shape=(224, 224, 3)),
    layers.Conv2D(16, 3, activation='relu'),
    layers.MaxPooling2D(),
    layers.Conv2D(32, 3, activation='relu'),
    layers.MaxPooling2D(),
    layers.Conv2D(64, 3, activation='relu'),
    layers.MaxPooling2D(),
    layers.Flatten(),
    layers.Dense(128, activation='relu'),
    layers.Dense(NUM_CLASSES, activation='softmax')
])

model.compile(
    optimizer='adam',
    loss='sparse_categorical_crossentropy',
    metrics=['accuracy']
)

model.fit(train_ds, validation_data=val_ds, epochs=10)
model.save('leafguard_model.h5')
```

This model is simple, but the workflow is excellent for learning.

### Option C: Use TensorFlow Lite Model Maker for transfer learning

**TensorFlow Lite Model Maker** helps you train a model with less custom code.
It is designed for scenarios like image classification, where you want to fine-tune an existing base model.

#### Why students like this option

- Less training code to write.
- Easier transfer learning workflow.
- Can export directly to TFLite.
- Good balance between simplicity and realism.

#### Sample Python code for Model Maker

```python
from tflite_model_maker import image_classifier
from tflite_model_maker.image_classifier import DataLoader

train_data = DataLoader.from_folder('plantvillage_subset/train')
validation_data = DataLoader.from_folder('plantvillage_subset/val')

auto_model = image_classifier.create(
    train_data,
    model_spec='efficientnet_lite0',
    validation_data=validation_data,
    epochs=10,
    batch_size=32
)

auto_model.export(
    export_dir='exported_model',
    tflite_filename='model.tflite',
    label_filename='labels.txt'
)
```

#### What this code does

- loads the dataset from folders,
- uses an EfficientNet Lite backbone,
- fine-tunes the classifier,
- exports both the `.tflite` model and the labels file.

### What model format should you expect?

For most image classification models used in Android apps, expect:

- **Input shape:** `1 x 224 x 224 x 3`
- **Input type:** usually `float32` or `uint8`
- **Output shape:** `1 x N` where `N` is number of classes
- **Output meaning:** a probability-like array or raw scores (logits)

Example:

```text
Input:  [1][224][224][3]
Output: [1][6]
```

If your six classes are:

```text
0 Tomato_Healthy
1 Tomato_Early_Blight
2 Tomato_Late_Blight
3 Potato_Healthy
4 Potato_Early_Blight
5 Potato_Late_Blight
```

Then an output like:

```text
[0.03, 0.82, 0.07, 0.01, 0.05, 0.02]
```

means the predicted class is index `1`, i.e. `Tomato_Early_Blight`.

### How to verify a downloaded model before adding it to Android

Never copy an untested model directly into your app.
Always verify it first.

#### Verification checklist in Python

1. Load the model with TFLite Interpreter.
2. Print input tensor details.
3. Print output tensor details.
4. Run one inference with random or test data.
5. Confirm output shape matches expected class count.
6. Confirm no runtime errors occur.

#### Example verification script

```python
import numpy as np
import tensorflow as tf

interpreter = tf.lite.Interpreter(model_path='model.tflite')
interpreter.allocate_tensors()

input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

print('Input details:', input_details)
print('Output details:', output_details)

input_shape = input_details[0]['shape']
input_dtype = input_details[0]['dtype']

sample = np.random.random_sample(input_shape).astype(input_dtype)
interpreter.set_tensor(input_details[0]['index'], sample)
interpreter.invoke()
output = interpreter.get_tensor(output_details[0]['index'])

print('Output shape:', output.shape)
print('Output values:', output)
print('Output sum:', output.sum())
```

#### What to look for in the printed details

You want answers to these questions:

- Is the input shape `224 x 224 x 3` or something else?
- Is the input dtype `float32`, `uint8`, or `int8`?
- Is the output shape equal to the number of labels?
- Does inference succeed without crashing?

### The `labels.txt` file

This small file is critical.
It maps output index numbers to human-readable class names.

#### Example labels file

```text
Tomato_Healthy
Tomato_Early_Blight
Tomato_Late_Blight
Potato_Healthy
Potato_Early_Blight
Potato_Late_Blight
```

#### How to create it

You can create `labels.txt` manually by writing one class name per line.
The order **must exactly match** the order used during training.

#### Why label order matters

Suppose your model output index `2` is actually `Tomato_Late_Blight`.
If your labels file accidentally places `Potato_Healthy` at line 3, your app will show the wrong disease name even though the model predicted correctly.

This is one of the easiest ways to silently break a demo.

#### Best practice

Export labels automatically from the training notebook whenever possible.
If you write the file manually, also save the class order in your notebook or report.

### Model sourcing decision guide

| Situation | Best option |
|-----------|-------------|
| You want the fastest learning path | Option B: Colab |
| You want official TensorFlow-guided workflow | Option A: TensorFlow Hub tutorial |
| You want less custom training code | Option C: Model Maker |
| You already have a Keras model | Convert it to TFLite |
| You need a quick demo model | Use a pre-trained or previously exported TFLite model |

### What to store in your project report

When you source your model, document:

- dataset name,
- number of classes,
- image size,
- training source (Hub / Colab / Model Maker),
- accuracy value,
- normalization strategy,
- output label order,
- final `.tflite` file size.

---

## 4. Understanding Model Files, Labels, and Metadata

### The three files students often confuse

You may encounter several different model-related formats.

| Format | Meaning | Used directly in Android? |
|--------|---------|---------------------------|
| `.h5` | Keras model file | No |
| `SavedModel/` | TensorFlow export directory | No |
| `.tflite` | TensorFlow Lite inference file | Yes |

### What goes into the Android app

In the Android project, you typically place these in `app/src/main/assets/`:

```text
model.tflite
labels.txt
```

### Why `assets/` instead of `res/raw/`?

`assets/` is a common choice because:

- model files can be read as streams or file descriptors,
- file names remain simple,
- it is widely used in TFLite examples.

### Example assets folder structure

```text
app/
  src/
    main/
      assets/
        model.tflite
        labels.txt
```

### Input tensor basics

A tensor is just a multidimensional array.

For image classification, the input tensor often has four dimensions:

```text
[batch][height][width][channels]
```

Example:

```text
[1][224][224][3]
```

This means:

- batch size = 1 image,
- height = 224 pixels,
- width = 224 pixels,
- channels = 3 (RGB).

### Output tensor basics

For classification, the output is often:

```text
[1][numClasses]
```

If the app supports 6 classes, output becomes:

```text
[1][6]
```

### Softmax output vs logits

Your model may output:

1. **softmax probabilities**,
2. **logits** (raw scores before softmax).

If the model uses a final `softmax` layer, then:

- outputs are between 0 and 1,
- values often sum to about 1.0.

If the model outputs logits, then:

- values can be negative or larger than 1,
- you may need to apply softmax yourself.

### Quick softmax refresher

Softmax converts raw scores into probabilities.

```text
probability_i = exp(score_i) / sum(exp(all scores))
```

In practice, many classification models already include softmax in the final layer.
Always confirm this from training code.

### Representative example

Suppose the output is:

```text
[2.1, 4.5, 0.3]
```

This is probably logits.
After softmax, it may become something like:

```text
[0.08, 0.89, 0.03]
```

### Why metadata matters

Before writing Java code, you should know:

- expected input size,
- expected input dtype,
- normalization rule,
- expected color channel order,
- output shape,
- whether output is logits or probabilities.

If any one of these assumptions is wrong, predictions can become useless.

### A model card is valuable

A **model card** or `model-notes.md` should record:

- model source,
- training dataset,
- preprocessing,
- metrics,
- known limitations,
- supported labels.

This is good engineering and good viva preparation.

---

## 5. Model Conversion Deep Dive

Many students have a Keras or TensorFlow model but not a `.tflite` file.
This section explains how conversion works and what optimization choices mean.

### Conversion path 1: Convert from `.h5` Keras model

If you saved your model as:

```text
leafguard_model.h5
```

then use code like this:

```python
import tensorflow as tf

model = tf.keras.models.load_model('leafguard_model.h5')

converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

with open('leafguard_model.tflite', 'wb') as f:
    f.write(tflite_model)
```

### Expanded conversion example with checks

```python
import tensorflow as tf

h5_path = 'leafguard_model.h5'
tflite_path = 'leafguard_model.tflite'

model = tf.keras.models.load_model(h5_path)
model.summary()

converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]

tflite_model = converter.convert()

with open(tflite_path, 'wb') as f:
    f.write(tflite_model)

print('Saved:', tflite_path)
```

### Conversion path 2: Convert from SavedModel format

If you exported a directory like:

```text
saved_model/
```

then use:

```python
import tensorflow as tf

converter = tf.lite.TFLiteConverter.from_saved_model('saved_model')
tflite_model = converter.convert()

with open('leafguard_savedmodel.tflite', 'wb') as f:
    f.write(tflite_model)
```

### Conversion path 3: Convert from concrete functions

This path is less common for beginners, but useful when you have a custom inference function.

```python
import tensorflow as tf

loaded = tf.saved_model.load('saved_model')
concrete_func = loaded.signatures['serving_default']
converter = tf.lite.TFLiteConverter.from_concrete_functions([concrete_func])
tflite_model = converter.convert()
```

### TFLite converter optimization options

TensorFlow Lite supports optimization flags that change model size and inference behavior.

#### 1. `tf.lite.Optimize.DEFAULT`

This is the most common starting point.

```python
converter.optimizations = [tf.lite.Optimize.DEFAULT]
```

**What it means:**

- Let TensorFlow Lite choose practical optimizations.
- Often reduces size significantly.
- Good default choice for many student projects.

#### 2. `tf.lite.Optimize.OPTIMIZE_FOR_SIZE`

```python
converter.optimizations = [tf.lite.Optimize.OPTIMIZE_FOR_SIZE]
```

**What it means:**

- Prioritizes smaller file size.
- Useful when APK size matters.
- May use stronger compression or quantization behavior depending on the graph.

#### 3. `tf.lite.Optimize.OPTIMIZE_FOR_LATENCY`

```python
converter.optimizations = [tf.lite.Optimize.OPTIMIZE_FOR_LATENCY]
```

**What it means:**

- Prioritizes runtime speed.
- Useful for real-time camera-like experiences.
- Final result still depends heavily on device hardware.

### Dynamic range quantization

This is one of the easiest quantization options.

```python
import tensorflow as tf

model = tf.keras.models.load_model('leafguard_model.h5')
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]

tflite_model = converter.convert()

with open('leafguard_dynamic.tflite', 'wb') as f:
    f.write(tflite_model)
```

#### What dynamic range quantization does

- Quantizes mainly the **weights** from float32 to smaller formats such as int8.
- Activations are often still handled in floating point during runtime.
- Usually reduces model size with small accuracy loss.
- No representative dataset is required.

#### When to use it

Use it when:

- you want a quick model size reduction,
- you want easy deployment,
- you do not want calibration complexity.

### Integer quantization (`INT8`)

This is more aggressive.
It quantizes more of the model to integer operations.

#### Why it is attractive

- smaller model,
- often faster inference on supported hardware,
- useful for edge deployment.

#### Pros

- Excellent compression.
- Can be faster on hardware optimized for int8.
- Good for low-resource devices.

#### Cons

- Accuracy may drop more than with float models.
- Calibration setup is more involved.
- Input and output types may change.

### Float16 quantization

This is a middle-ground option.
Weights are stored in float16 instead of float32.

```python
import tensorflow as tf

model = tf.keras.models.load_model('leafguard_model.h5')
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
converter.target_spec.supported_types = [tf.float16]

tflite_model = converter.convert()

with open('leafguard_fp16.tflite', 'wb') as f:
    f.write(tflite_model)
```

#### Why Float16 is useful

- Smaller than float32.
- Usually less accuracy drop than full int8.
- Good compromise for mobile GPUs.

### Full integer quantization

This is the most complete integer conversion path.
It usually requires a **representative dataset**.

#### Why a representative dataset is required

During conversion, TensorFlow Lite must estimate the numeric ranges of activations.
It uses sample input data to understand realistic value ranges.
Without that calibration data, it cannot safely convert activations to int8 with good accuracy.

#### Example representative dataset generator

```python
import tensorflow as tf
import numpy as np

model = tf.keras.models.load_model('leafguard_model.h5')

converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]

def representative_data_gen():
    for _ in range(100):
        sample = np.random.rand(1, 224, 224, 3).astype(np.float32)
        yield [sample]

converter.representative_dataset = representative_data_gen
converter.target_spec.supported_ops = [tf.lite.OpsSet.TFLITE_BUILTINS_INT8]
converter.inference_input_type = tf.int8
converter.inference_output_type = tf.int8

tflite_model = converter.convert()

with open('leafguard_int8.tflite', 'wb') as f:
    f.write(tflite_model)
```

#### Better representative dataset practice

Random data is useful for a demo, but real representative images are better.
For best calibration, use actual leaf images from your dataset after the same resize steps used during training.

### Comparing quantized vs non-quantized model sizes

The exact numbers depend on your architecture, but a rough pattern is:

| Model type | Example file size |
|------------|-------------------|
| Float32 | 16 MB |
| Dynamic range quantized | 4-8 MB |
| Float16 | 8 MB |
| Full int8 | 4 MB |

These are only example values, but the trend is important.
Aggressive quantization usually makes the model much smaller.

### How to choose among conversion options

| Goal | Suggested choice |
|------|------------------|
| Easiest first success | Float32 or `Optimize.DEFAULT` |
| Smaller file size | Dynamic range or float16 |
| Maximum compression | Full int8 |
| Good GPU balance | Float16 |
| Lowest risk of accuracy drop | Float32 |

### Common conversion problems

#### Problem 1: Unsupported ops

Some TensorFlow operations do not convert cleanly to TFLite.
You may see conversion errors about unsupported ops.

**Fixes:**

- simplify the model,
- use supported layers,
- enable select TensorFlow ops if appropriate,
- retrain with a mobile-friendly architecture.

#### Problem 2: Accuracy drop after conversion

Possible causes:

- wrong quantization strategy,
- poor representative dataset,
- mismatch in preprocessing,
- output interpretation error.

#### Problem 3: Android input dtype mismatch

Your Android code may prepare `float[][][][]` input, but your quantized model might expect `byte[]` or `int8` data.
Always inspect model input details first.

### Best beginner recommendation

For most CSE 2206 projects, start with:

1. a float32 or default-optimized model,
2. correct preprocessing,
3. successful Android inference,
4. then experiment with quantization.

That learning order is safer than immediately jumping to full int8 optimization.

---

## 6. Android Project Setup for Offline AI

### Add the TensorFlow Lite dependency

In `app/build.gradle` add:

```gradle
implementation 'org.tensorflow:tensorflow-lite:2.12.0'
```

### Add GPU delegate dependency

If you want GPU acceleration, also add:

```gradle
implementation 'org.tensorflow:tensorflow-lite-gpu:2.12.0'
```

### Optional support libraries

Depending on your project, you may also use support or metadata libraries, but raw `Interpreter` is enough for this course week.

### Add model files to assets

Create:

```text
app/src/main/assets/
```

Then copy:

```text
model.tflite
labels.txt
```

### Loading a model file correctly

The common pattern in Java is to memory-map the model.

```java
private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
    AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
    FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
    FileChannel fileChannel = inputStream.getChannel();
    long startOffset = fileDescriptor.getStartOffset();
    long declaredLength = fileDescriptor.getDeclaredLength();
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
}
```

### Why `MappedByteBuffer` is used

It is efficient for large read-only binary files.
You do not need to manually copy the full model into a Java byte array first.

### Loading labels

```java
private List<String> loadLabels(AssetManager assetManager, String labelsPath) throws IOException {
    List<String> labels = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(labelsPath)));
    String line;
    while ((line = reader.readLine()) != null) {
        if (!line.trim().isEmpty()) {
            labels.add(line.trim());
        }
    }
    reader.close();
    return labels;
}
```

### Project checklist before coding the classifier

- [ ] `model.tflite` exists in assets.
- [ ] `labels.txt` exists in assets.
- [ ] Label count matches output class count.
- [ ] Gradle dependency added.
- [ ] Project sync successful.
- [ ] You know the expected input size.
- [ ] You know the required normalization strategy.

---

## 7. Input Shapes, Output Shapes, and Tensor Basics

### Why shapes matter

TensorFlow Lite is strict.
If the model expects a tensor of shape `1 x 224 x 224 x 3`, then you must provide exactly that shape.
A mismatch can cause:

- runtime exceptions,
- nonsense predictions,
- buffer size errors.

### Example input details lookup in Java

```java
Tensor inputTensor = interpreter.getInputTensor(0);
int[] shape = inputTensor.shape();
DataType dataType = inputTensor.dataType();

Log.d("TFLite", "Input shape: " + Arrays.toString(shape));
Log.d("TFLite", "Input type: " + dataType);
```

### Example output details lookup

```java
Tensor outputTensor = interpreter.getOutputTensor(0);
int[] outputShape = outputTensor.shape();
DataType outputType = outputTensor.dataType();

Log.d("TFLite", "Output shape: " + Arrays.toString(outputShape));
Log.d("TFLite", "Output type: " + outputType);
```

### Most common image input shape

```text
[1, 224, 224, 3]
```

### But do not assume blindly

Some models expect:

- `192 x 192 x 3`,
- `299 x 299 x 3`,
- `256 x 256 x 3`,
- `uint8` instead of `float32`,
- BGR instead of RGB (less common in standard TF pipelines).

### Understanding channel order

If your code reads pixels in RGB order, then:

- channel 0 = red,
- channel 1 = green,
- channel 2 = blue.

That order must match training.

### Output probability arrays

If the model has softmax output, a result might look like:

```text
Tomato_Healthy      0.04
Tomato_Early_Blight 0.78
Tomato_Late_Blight  0.10
Potato_Healthy      0.01
Potato_Early_Blight 0.05
Potato_Late_Blight  0.02
```

### Top-1 prediction

Top-1 means the label with the highest probability.

### Top-3 predictions

Top-3 is often more informative for debugging.
It lets you inspect whether the correct class is at least near the top.

Example:

```text
1. Tomato_Early_Blight 0.78
2. Tomato_Late_Blight 0.10
3. Potato_Early_Blight 0.05
```

### Why top-3 helps students

- It shows whether the model is confused among similar diseases.
- It helps debug mislabeled images.
- It produces richer viva discussion.

---

## 8. Normalization Strategies - Why They Must Match Training

This is the **most common source of wrong predictions**.
A model can be perfectly valid, and your Java code can compile, but the predictions will still be wrong if normalization does not match training.

### What is normalization?

Camera pixels are usually integers from `0` to `255`.
Machine learning models are usually trained on transformed values.
Normalization converts raw pixel values into the numeric range used during training.

### Rule to remember

**Your Android preprocessing must match the training preprocessing exactly.**

Not approximately.
Not "close enough".
Exactly.

### Strategy 1: Normalize to `[0, 1]`

This is very common.

```java
float red = ((pixel >> 16) & 0xFF) / 255.0f;
float green = ((pixel >> 8) & 0xFF) / 255.0f;
float blue = (pixel & 0xFF) / 255.0f;
```

#### When it is commonly used

- simple CNNs built in Keras,
- many educational image classifiers,
- pipelines that include `Rescaling(1./255)`.

### Strategy 2: Normalize to `[-1, 1]`

This is common for models like MobileNet-style pipelines.

```java
float red = (((pixel >> 16) & 0xFF) / 127.5f) - 1.0f;
float green = (((pixel >> 8) & 0xFF) / 127.5f) - 1.0f;
float blue = ((pixel & 0xFF) / 127.5f) - 1.0f;
```

#### Why this works

- `0` becomes `-1.0`
- `127.5` becomes `0.0`
- `255` becomes `1.0`

### Strategy 3: ImageNet mean/std normalization

This is often used in PyTorch-like or transfer learning pipelines.

```java
float red = ((((pixel >> 16) & 0xFF) / 255.0f) - 0.485f) / 0.229f;
float green = ((((pixel >> 8) & 0xFF) / 255.0f) - 0.456f) / 0.224f;
float blue = (((pixel & 0xFF) / 255.0f) - 0.406f) / 0.225f;
```

### How to find what normalization your model used

Check these sources in this order:

1. the training notebook,
2. the model card or model notes,
3. the final model export script,
4. the preprocessing layer inside the network,
5. the official documentation of the base model.

### Clues from training code

#### Example clue for `[0, 1]`

```python
layers.Rescaling(1./255)
```

#### Example clue for `[-1, 1]`

```python
x = (x / 127.5) - 1.0
```

#### Example clue for mean/std normalization

```python
transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
```

### What happens when normalization is wrong?

You may see:

- random-looking predictions,
- low confidence for obvious images,
- high confidence for clearly wrong classes,
- NaN or Inf values in extreme failure cases,
- output that looks nothing like Python test output.

### Example mismatch scenario

Suppose the model was trained using `[-1, 1]`, but your Android app uses `[0, 1]`.
Then every input value is shifted upward.
The network receives a data distribution different from training.
The learned filters no longer interpret colors correctly.
Prediction quality can collapse.

### Code example: preprocessing for `[0, 1]`

```java
private float[][][][] preprocessToZeroOne(Bitmap bitmap) {
    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
    float[][][][] input = new float[1][224][224][3];

    for (int y = 0; y < 224; y++) {
        for (int x = 0; x < 224; x++) {
            int pixel = resized.getPixel(x, y);
            input[0][y][x][0] = ((pixel >> 16) & 0xFF) / 255.0f;
            input[0][y][x][1] = ((pixel >> 8) & 0xFF) / 255.0f;
            input[0][y][x][2] = (pixel & 0xFF) / 255.0f;
        }
    }

    return input;
}
```

### Code example: preprocessing for `[-1, 1]`

```java
private float[][][][] preprocessToMinusOnePlusOne(Bitmap bitmap) {
    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
    float[][][][] input = new float[1][224][224][3];

    for (int y = 0; y < 224; y++) {
        for (int x = 0; x < 224; x++) {
            int pixel = resized.getPixel(x, y);
            input[0][y][x][0] = (((pixel >> 16) & 0xFF) / 127.5f) - 1.0f;
            input[0][y][x][1] = (((pixel >> 8) & 0xFF) / 127.5f) - 1.0f;
            input[0][y][x][2] = ((pixel & 0xFF) / 127.5f) - 1.0f;
        }
    }

    return input;
}
```

### Code example: preprocessing for ImageNet mean/std

```java
private float[][][][] preprocessWithImageNetStats(Bitmap bitmap) {
    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
    float[][][][] input = new float[1][224][224][3];

    for (int y = 0; y < 224; y++) {
        for (int x = 0; x < 224; x++) {
            int pixel = resized.getPixel(x, y);

            float red = ((pixel >> 16) & 0xFF) / 255.0f;
            float green = ((pixel >> 8) & 0xFF) / 255.0f;
            float blue = (pixel & 0xFF) / 255.0f;

            input[0][y][x][0] = (red - 0.485f) / 0.229f;
            input[0][y][x][1] = (green - 0.456f) / 0.224f;
            input[0][y][x][2] = (blue - 0.406f) / 0.225f;
        }
    }

    return input;
}
```

### Which one should LeafGuard use?

Use whichever rule matches the model you actually trained or downloaded.
If your training notebook includes `Rescaling(1./255)`, then your Android app should typically use `[0, 1]` normalization.

### Best debugging method for normalization issues

Take one known image and run it through:

1. Python inference,
2. Android inference.

If the model and image are identical but outputs differ heavily, preprocessing is a likely cause.

---

## 9. Building the Preprocessing Pipeline in Java

### Step 1: Receive a `Bitmap`

Your Activity usually receives a `Bitmap` from:

- camera capture,
- gallery selection,
- file decode.

### Step 2: Resize to model input size

Most models do not accept the original camera size directly.
Resize first.

```java
Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
```

### Step 3: Extract pixel values

```java
int pixel = resized.getPixel(x, y);
```

### Step 4: Separate RGB channels

```java
int red = (pixel >> 16) & 0xFF;
int green = (pixel >> 8) & 0xFF;
int blue = pixel & 0xFF;
```

### Step 5: Normalize

```java
float redNorm = red / 255.0f;
```

### Step 6: Store in input tensor

```java
input[0][y][x][0] = redNorm;
```

### Why resizing before normalization is correct

Resize changes spatial resolution.
Normalization changes numeric scale.
These are different operations.
Doing them in the wrong order may make the code messy or inefficient.

### Full preprocessing method for common float model

```java
private float[][][][] preprocessImage(Bitmap bitmap) {
    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
    float[][][][] input = new float[1][224][224][3];

    for (int y = 0; y < 224; y++) {
        for (int x = 0; x < 224; x++) {
            int pixel = resized.getPixel(x, y);
            input[0][y][x][0] = ((pixel >> 16) & 0xFF) / 255.0f;
            input[0][y][x][1] = ((pixel >> 8) & 0xFF) / 255.0f;
            input[0][y][x][2] = (pixel & 0xFF) / 255.0f;
        }
    }

    return input;
}
```

### Alternative optimization: use a direct `ByteBuffer`

More advanced TFLite code often uses `ByteBuffer` instead of a 4D Java array.
This can reduce overhead and give more control over numeric types.

```java
ByteBuffer inputBuffer = ByteBuffer.allocateDirect(4 * 1 * 224 * 224 * 3);
inputBuffer.order(ByteOrder.nativeOrder());
```

Then you push floats sequentially:

```java
inputBuffer.putFloat(red / 255.0f);
inputBuffer.putFloat(green / 255.0f);
inputBuffer.putFloat(blue / 255.0f);
```

### Should beginners use arrays or `ByteBuffer`?

- Use `float[][][][]` first if it matches your model and you want clarity.
- Use `ByteBuffer` when you need performance tuning, quantized input handling, or more production-like control.

### Handling rotated images

A captured bitmap may appear sideways because of EXIF or camera orientation.
If disease regions are rotated badly, the model may perform worse.
You may need to rotate or correct orientation before preprocessing.

### Cropping vs stretching

When you resize a non-square image to `224 x 224`, the image may stretch.
For simple student projects, this is acceptable.
For higher accuracy, you may prefer center crop then resize.

### Memory warning

Do not preprocess extremely large bitmaps directly without scaling them down.
A huge camera bitmap can trigger memory pressure.
Scale first whenever possible.

---

## 10. Running Inference and Reading Results

### Basic inference code

```java
float[][][][] input = preprocessImage(bitmap);
float[][] output = new float[1][labels.size()];
interpreter.run(input, output);
```

### What `run()` does

It executes one forward pass of the model.
The model reads the input tensor and fills the output tensor.

### Finding the best prediction with argmax

```java
private int argmax(float[] values) {
    int maxIndex = 0;
    float maxValue = values[0];

    for (int i = 1; i < values.length; i++) {
        if (values[i] > maxValue) {
            maxValue = values[i];
            maxIndex = i;
        }
    }

    return maxIndex;
}
```

### Reading top-1 result

```java
int predictedIndex = argmax(output[0]);
String predictedLabel = labels.get(predictedIndex);
float confidence = output[0][predictedIndex];
```

### Reading top-3 results

```java
private List<Result> getTopK(float[] probabilities, int k) {
    List<Result> results = new ArrayList<>();

    for (int i = 0; i < probabilities.length; i++) {
        results.add(new Result(labels.get(i), probabilities[i]));
    }

    Collections.sort(results, (a, b) -> Float.compare(b.getConfidence(), a.getConfidence()));
    return results.subList(0, Math.min(k, results.size()));
}
```

### Why top-3 matters in LeafGuard

Plant diseases can look visually similar.
If the correct class is second highest, that tells you the model is partly understanding the image but is uncertain.
That is useful for debugging and comparison with the cloud model.

### Confidence meaning

If the model output is softmax, the confidence score is the probability of the predicted class.
If the scores sum to approximately `1.0`, that is a strong sign the model already includes softmax.

### If output is logits instead of probabilities

You can apply softmax manually.

```java
private float[] softmax(float[] logits) {
    float max = logits[0];
    for (float value : logits) {
        if (value > max) {
            max = value;
        }
    }

    float sum = 0f;
    float[] expValues = new float[logits.length];
    for (int i = 0; i < logits.length; i++) {
        expValues[i] = (float) Math.exp(logits[i] - max);
        sum += expValues[i];
    }

    float[] probabilities = new float[logits.length];
    for (int i = 0; i < logits.length; i++) {
        probabilities[i] = expValues[i] / sum;
    }

    return probabilities;
}
```

### Measure latency

```java
long startTime = System.currentTimeMillis();
interpreter.run(input, output);
long latencyMs = System.currentTimeMillis() - startTime;
```

### Why latency matters

A model that is 97% accurate but takes 4 seconds may feel slow and frustrating.
A field app should be responsive.
That is why benchmarking is part of this week.

---

## 11. GPU Delegate and NNAPI Acceleration

### What is a delegate in TensorFlow Lite?

A **delegate** is a hardware-specific optimization path.
Instead of running all operations on the generic CPU backend, TFLite can delegate work to specialized execution code.

Think of it like this:

- CPU mode = default general-purpose execution,
- GPU delegate = use graphics hardware for faster tensor operations,
- NNAPI delegate = ask Android's Neural Networks API to use available accelerators.

### CPU inference (default)

This is the baseline and works almost everywhere.
Typical latency for a medium plant disease classifier can be around:

```text
~200-500 ms
```

### GPU Delegate

GPU delegate often reduces latency to something like:

```text
~50-150 ms
```

#### What hardware supports it?

Support depends on:

- device GPU,
- Android version,
- model operators,
- driver quality.

Some devices show a big improvement.
Some show only a small improvement.
Some may fail and need fallback.

### Add GPU dependency to Gradle

```gradle
implementation 'org.tensorflow:tensorflow-lite-gpu:2.12.0'
```

### GPU delegate with CPU fallback

```java
Interpreter tflite;

try {
    Interpreter.Options options = new Interpreter.Options();
    GpuDelegate delegate = new GpuDelegate();
    options.addDelegate(delegate);
    tflite = new Interpreter(loadModelFile(assetManager, "model.tflite"), options);
} catch (Exception e) {
    tflite = new Interpreter(loadModelFile(assetManager, "model.tflite"));
}
```

### Expanded GPU fallback example with cleanup awareness

```java
private Interpreter createInterpreterWithGpuFallback(AssetManager assetManager) throws IOException {
    MappedByteBuffer modelBuffer = loadModelFile(assetManager, "model.tflite");

    try {
        Interpreter.Options options = new Interpreter.Options();
        options.setNumThreads(4);
        GpuDelegate gpuDelegate = new GpuDelegate();
        options.addDelegate(gpuDelegate);
        return new Interpreter(modelBuffer, options);
    } catch (Exception gpuError) {
        Log.w("TFLite", "GPU delegate unavailable, falling back to CPU", gpuError);
        Interpreter.Options cpuOptions = new Interpreter.Options();
        cpuOptions.setNumThreads(4);
        return new Interpreter(modelBuffer, cpuOptions);
    }
}
```

### NNAPI Delegate

**NNAPI** stands for **Neural Networks API**.
It is an Android API that lets ML frameworks access hardware accelerators through a standard interface.

#### Android support

NNAPI is available on Android 8.1+.
Actual performance depends heavily on vendor hardware and drivers.

### NNAPI delegate example

```java
Interpreter.Options options = new Interpreter.Options();
options.setUseNNAPI(true);
options.setNumThreads(4);
Interpreter tflite = new Interpreter(loadModelFile(assetManager, "model.tflite"), options);
```

### When to try NNAPI

Try it when:

- device runs Android 8.1 or newer,
- you want hardware acceleration but GPU delegate is not ideal,
- you want to benchmark multiple options.

### CPU vs GPU vs NNAPI comparison table

| Path | Advantages | Limitations |
|------|------------|-------------|
| CPU | Stable, universal, simple | Slowest in many cases |
| GPU | Often fastest for vision models | Device compatibility varies |
| NNAPI | Can use hardware accelerators | Performance varies by vendor |

### How to benchmark properly

Do not benchmark just one run.
The first run may include warm-up overhead.

#### Recommended benchmark method

1. Warm up the interpreter 5-10 times.
2. Run inference 100 times.
3. Record total time.
4. Divide by 100 to get average latency.

#### Java benchmark example

```java
private long benchmarkAverageLatency(Bitmap bitmap, int iterations) {
    float[][][][] input = preprocessImage(bitmap);
    float[][] output = new float[1][labels.size()];

    for (int i = 0; i < 10; i++) {
        interpreter.run(input, output);
    }

    long start = System.currentTimeMillis();
    for (int i = 0; i < iterations; i++) {
        interpreter.run(input, output);
    }
    long total = System.currentTimeMillis() - start;

    return total / iterations;
}
```

### Benchmark recording table

| Device | CPU avg ms | GPU avg ms | NNAPI avg ms | Notes |
|--------|------------|------------|--------------|-------|
| Emulator | 420 | N/A | N/A | Emulator often does not reflect real performance |
| Mid-range phone | 260 | 95 | 130 | Example only |
| Flagship phone | 140 | 60 | 85 | Example only |

### Important delegate caution

Never assume GPU is always faster on every device.
Benchmark and then decide.

---

## 12. Handling Inference Errors and Low-Confidence Results

A production-ready classifier must handle failures gracefully.
Do not assume the model always loads, the bitmap is always valid, and the prediction is always trustworthy.

### What "confidence" means

If the output is softmax, each value approximates the probability of a class.
All class probabilities should sum to roughly `1.0`.

Example:

```text
[0.10, 0.55, 0.20, 0.05, 0.06, 0.04]
```

The highest probability is `0.55`.
That means the model is 55% confident in that class.

### Confidence threshold

A useful UI rule is:

```text
If maxConfidence < 0.5 -> show "Uncertain - try again"
```

This is safer than always showing a disease name, especially when:

- the image is blurry,
- the leaf is not centered,
- the class is outside the model's training set,
- lighting is poor.

### Confidence threshold example in Java

```java
float threshold = 0.5f;
if (confidence < threshold) {
    resultTextView.setText("Uncertain - try again with a clearer leaf image");
} else {
    resultTextView.setText(predictedLabel + " (" + (confidence * 100f) + "%)");
}
```

### Handling interpreter initialization failure gracefully

Possible reasons:

- `model.tflite` missing,
- corrupt file,
- unsupported operators,
- memory issues,
- delegate creation failure.

#### Example safe initialization

```java
try {
    interpreter = new Interpreter(loadModelFile(assetManager, "model.tflite"));
} catch (IOException e) {
    Log.e("TFLite", "Model file missing or unreadable", e);
    showError("Offline model could not be loaded. Please reinstall the app or contact the developer.");
} catch (Exception e) {
    Log.e("TFLite", "Unexpected TFLite initialization error", e);
    showError("Offline prediction is unavailable on this device.");
}
```

### Handling model file not found in assets

This is one of the most common setup mistakes.

Symptoms:

- `openFd()` throws exception,
- app crashes on startup,
- offline mode never initializes.

#### Better recovery message

```java
showError("Model file not found in assets. Check app/src/main/assets/model.tflite");
```

### Memory issues: `OutOfMemoryError`

A full camera photo can be several thousand pixels wide.
Loading or processing it at full resolution may cause memory pressure.

#### Safer approach

- decode a scaled bitmap,
- resize early,
- recycle intermediate bitmaps if needed.

#### Example defensive approach

```java
try {
    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
} catch (OutOfMemoryError error) {
    Log.e("TFLite", "Bitmap too large for preprocessing", error);
    showError("Image is too large. Please try a smaller image.");
}
```

### NaN / Inf outputs

Sometimes output contains `NaN` or `Infinity`.
This usually points to a preprocessing or tensor mismatch problem.

#### How to detect it

```java
private boolean hasInvalidValues(float[] values) {
    for (float value : values) {
        if (Float.isNaN(value) || Float.isInfinite(value)) {
            return true;
        }
    }
    return false;
}
```

#### How to handle it

```java
if (hasInvalidValues(output[0])) {
    showError("Model output invalid. Check preprocessing and model compatibility.");
    return;
}
```

### Thread safety warning

**TFLite `Interpreter` is NOT thread-safe.**
This means you should not call `run()` from multiple threads at the same time on the same interpreter instance.

#### Safe choices

- use one interpreter per thread, or
- synchronize access to a shared interpreter.

#### Example using synchronized access

```java
synchronized (interpreter) {
    interpreter.run(input, output);
}
```

### Running inference off the main thread

Heavy inference should not run on the UI thread.
If you block the main thread, the app may feel frozen or even trigger ANR errors.

#### `AsyncTask`

Historically used in Android, but now deprecated.
Do not build new production code around it.

#### `ExecutorService`

This is the best Java-friendly recommendation for this project.

```java
ExecutorService executorService = Executors.newSingleThreadExecutor();
executorService.execute(() -> {
    ClassificationResult result = classifier.classify(bitmap);
    runOnUiThread(() -> updateUi(result));
});
```

#### Kotlin coroutines equivalent

Coroutines are excellent in Kotlin, but this repository uses Java.
So for the Java equivalent, use `ExecutorService`, `Handler`, or similar background patterns.

### Error handling checklist

A robust classifier should handle:

- [ ] missing model file,
- [ ] missing labels file,
- [ ] corrupt model,
- [ ] huge image memory issue,
- [ ] invalid output values,
- [ ] low confidence,
- [ ] thread safety,
- [ ] delegate failure fallback.

---

## 13. Complete `TFLiteClassifier` Implementation

The following Java class is a production-quality reference for this week.
It includes:

- model loading with error handling,
- labels loading from assets,
- preprocessing using `[0, 1]` normalization,
- top-1 and top-3 results,
- confidence threshold,
- synchronized inference,
- resource cleanup.

```java
package com.example.leafguardai.ml;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TFLiteClassifier implements AutoCloseable {

    private static final String TAG = "TFLiteClassifier";
    private static final int INPUT_SIZE = 224;
    private static final float CONFIDENCE_THRESHOLD = 0.50f;

    private final Interpreter interpreter;
    private final List<String> labels;

    public TFLiteClassifier(AssetManager assetManager,
                            String modelPath,
                            String labelsPath) throws IOException {
        this.labels = loadLabels(assetManager, labelsPath);
        if (labels.isEmpty()) {
            throw new IOException("Labels file is empty: " + labelsPath);
        }

        try {
            this.interpreter = new Interpreter(loadModelFile(assetManager, modelPath));
        } catch (IOException e) {
            Log.e(TAG, "Failed to load TFLite model", e);
            throw e;
        } catch (Exception e) {
            Log.e(TAG, "Unexpected interpreter creation failure", e);
            throw new IOException("Could not initialize TensorFlow Lite interpreter", e);
        }
    }

    public ClassificationResult classify(Bitmap bitmap) {
        if (bitmap == null) {
            return ClassificationResult.error("Bitmap is null");
        }

        float[][][][] input = preprocessImage(bitmap);
        float[][] output = new float[1][labels.size()];

        try {
            synchronized (interpreter) {
                interpreter.run(input, output);
            }
        } catch (Exception e) {
            Log.e(TAG, "Inference failed", e);
            return ClassificationResult.error("Inference failed: " + e.getMessage());
        }

        float[] probabilities = output[0];
        if (containsInvalidValue(probabilities)) {
            return ClassificationResult.error("Model output contains NaN or Infinity");
        }

        int topIndex = argmax(probabilities);
        float topConfidence = probabilities[topIndex];
        String topLabel = getLabelSafely(topIndex);
        List<Prediction> top3 = getTopK(probabilities, 3);

        boolean uncertain = topConfidence < CONFIDENCE_THRESHOLD;
        String displayLabel = uncertain ? "Uncertain - try again" : topLabel;

        return new ClassificationResult(
                true,
                displayLabel,
                topConfidence,
                uncertain,
                top3,
                null
        );
    }

    private float[][][][] preprocessImage(Bitmap bitmap) {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);
        float[][][][] input = new float[1][INPUT_SIZE][INPUT_SIZE][3];

        for (int y = 0; y < INPUT_SIZE; y++) {
            for (int x = 0; x < INPUT_SIZE; x++) {
                int pixel = resized.getPixel(x, y);
                input[0][y][x][0] = ((pixel >> 16) & 0xFF) / 255.0f;
                input[0][y][x][1] = ((pixel >> 8) & 0xFF) / 255.0f;
                input[0][y][x][2] = (pixel & 0xFF) / 255.0f;
            }
        }

        return input;
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private List<String> loadLabels(AssetManager assetManager, String labelsPath) throws IOException {
        List<String> loadedLabels = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(assetManager.open(labelsPath)));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                loadedLabels.add(line);
            }
        }
        reader.close();
        return loadedLabels;
    }

    private boolean containsInvalidValue(float[] values) {
        for (float value : values) {
            if (Float.isNaN(value) || Float.isInfinite(value)) {
                return true;
            }
        }
        return false;
    }

    private int argmax(float[] values) {
        int maxIndex = 0;
        float maxValue = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] > maxValue) {
                maxValue = values[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private String getLabelSafely(int index) {
        if (index >= 0 && index < labels.size()) {
            return labels.get(index);
        }
        return "Unknown";
    }

    private List<Prediction> getTopK(float[] probabilities, int k) {
        List<Prediction> results = new ArrayList<>();

        for (int i = 0; i < probabilities.length; i++) {
            results.add(new Prediction(getLabelSafely(i), probabilities[i]));
        }

        Collections.sort(results, new Comparator<Prediction>() {
            @Override
            public int compare(Prediction left, Prediction right) {
                return Float.compare(right.getConfidence(), left.getConfidence());
            }
        });

        return new ArrayList<>(results.subList(0, Math.min(k, results.size())));
    }

    @Override
    public void close() {
        if (interpreter != null) {
            interpreter.close();
        }
    }

    public static class Prediction {
        private final String label;
        private final float confidence;

        public Prediction(String label, float confidence) {
            this.label = label;
            this.confidence = confidence;
        }

        public String getLabel() {
            return label;
        }

        public float getConfidence() {
            return confidence;
        }
    }

    public static class ClassificationResult {
        private final boolean success;
        private final String label;
        private final float confidence;
        private final boolean uncertain;
        private final List<Prediction> topPredictions;
        private final String errorMessage;

        public ClassificationResult(boolean success,
                                    String label,
                                    float confidence,
                                    boolean uncertain,
                                    List<Prediction> topPredictions,
                                    String errorMessage) {
            this.success = success;
            this.label = label;
            this.confidence = confidence;
            this.uncertain = uncertain;
            this.topPredictions = topPredictions;
            this.errorMessage = errorMessage;
        }

        public static ClassificationResult error(String errorMessage) {
            return new ClassificationResult(false, null, 0f, false, new ArrayList<Prediction>(), errorMessage);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getLabel() {
            return label;
        }

        public float getConfidence() {
            return confidence;
        }

        public boolean isUncertain() {
            return uncertain;
        }

        public List<Prediction> getTopPredictions() {
            return topPredictions;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
```

### Notes about this implementation

- It assumes `[0, 1]` normalization.
- It assumes one output score per label.
- It assumes float output.
- It uses synchronized inference because the interpreter is not thread-safe.
- It returns an error object instead of crashing the app.

### Suggested Activity usage with `ExecutorService`

```java
ExecutorService executorService = Executors.newSingleThreadExecutor();

executorService.execute(() -> {
    long start = System.currentTimeMillis();
    TFLiteClassifier.ClassificationResult result = classifier.classify(bitmap);
    long latency = System.currentTimeMillis() - start;

    runOnUiThread(() -> {
        if (!result.isSuccess()) {
            resultTextView.setText(result.getErrorMessage());
            return;
        }

        if (result.isUncertain()) {
            resultTextView.setText("Uncertain - try again");
        } else {
            resultTextView.setText(result.getLabel());
        }

        confidenceTextView.setText(String.format(Locale.US, "%.2f%%", result.getConfidence() * 100f));
        latencyTextView.setText(latency + " ms");
    });
});
```

---

## 14. Comparing Cloud vs Offline - Architecture Decision

LeafGuard AI is strongest when you understand **why** you chose a particular architecture.
This matters in viva questions.

### When to use cloud inference

Use cloud inference when:

- the model is too large for the phone,
- you need frequent server-side model updates,
- you want central logging or analytics,
- you need the best possible accuracy from heavier models,
- you want to improve the model without forcing app updates.

### When to use offline inference

Use offline inference when:

- the app must work with no internet,
- user privacy is important,
- latency is critical,
- the model is small enough for mobile,
- you want reduced server cost.

### When cloud may be preferable for accuracy

If the requirement is more than about 95% real-world accuracy and the best model is large, then cloud inference may be the better engineering decision.
That lets you run a heavier model on a backend GPU.

### When offline may be preferable for user experience

If the app is used in farms or fields with poor connectivity, offline inference gives better reliability even if the model is a little smaller.

### Hybrid approach: what LeafGuard can do

A practical hybrid approach is:

1. try offline model first,
2. if confidence is high enough, show offline result immediately,
3. if confidence is low and internet is available, optionally fall back to cloud prediction,
4. compare or confirm results.

### Hybrid architecture diagram

```text
                +------------------+
Captured Image ->| Offline TFLite   |-- high confidence --> Show result
                +------------------+
                         |
                         | low confidence / unavailable
                         v
                +------------------+
                | Cloud API        |---------------> Show fallback result
                +------------------+
```

### Data usage comparison

| Feature | Offline TFLite | Cloud API |
|--------|-----------------|-----------|
| Internet required | No | Yes |
| Image leaves device | No | Yes |
| Server cost | No per-request cost | Yes |
| Model update ease | Harder | Easier |
| Latency | Usually lower for small models | Includes network delay |
| Works in airplane mode | Yes | No |

### Privacy comparison

Offline AI is stronger for privacy because the image stays on the device.
That matters if leaf images are tied to farm location or crop management practices.

### Maintenance comparison

Cloud models are easier to update.
If you discover a new disease class, you can update the backend without publishing a new APK.
Offline models usually require an app update unless you build a model download system.

### Recommended LeafGuard explanation

For a student project, the best explanation is:

- cloud mode demonstrates backend integration,
- offline mode demonstrates on-device AI,
- hybrid mode demonstrates architectural thinking.

### CSE 2206 Viva Q&A

#### Q1. What is TensorFlow Lite?
**Answer:** TensorFlow Lite is TensorFlow's lightweight runtime for mobile and edge devices. It allows a trained model to run directly on Android without needing a server. In LeafGuard AI, it enables offline plant disease prediction.

#### Q2. Why do we need a `labels.txt` file?
**Answer:** The model output is usually just an array of scores. `labels.txt` maps each output index to a class name. The order must exactly match the training class order.

#### Q3. Why must preprocessing match training?
**Answer:** Neural networks learn on a specific input distribution. If Android uses a different resize, color order, or normalization rule, the input values change and prediction accuracy drops sharply. This is why preprocessing mismatch is a common bug.

#### Q4. Why should inference not run on the main thread?
**Answer:** Model inference can take tens or hundreds of milliseconds. Running it on the main thread can freeze the UI and create a poor user experience or ANR risk. In Java, `ExecutorService` is a good way to move inference to a background thread.

#### Q5. Why might you still keep a cloud model if offline exists?
**Answer:** Cloud inference can use larger and more accurate models, can be updated more easily, and can act as a fallback when offline confidence is low. A hybrid design gives both reliability and flexibility.

---

## 15. Benchmarking, Optimization, and Best Practices

### Best practice 1: Benchmark on a real phone

Emulators are useful for UI checks, but ML performance on an emulator does not represent a real device.
Always record results on at least one physical Android phone if possible.

### Best practice 2: Warm up first

The first few inferences may be slower.
Do not use only the first run as your benchmark value.

### Best practice 3: Test several images

One image is not enough.
Use multiple healthy and diseased images.
Record:

- predicted label,
- confidence,
- latency,
- whether the result matches expectation.

### Best practice 4: Keep a test table

| Image name | Expected class | Offline class | Offline confidence | Cloud class | Match? |
|-----------|----------------|---------------|--------------------|------------|--------|
| leaf_01.jpg | Tomato Healthy | Tomato Healthy | 0.91 | Tomato Healthy | Yes |
| leaf_02.jpg | Tomato Late Blight | Tomato Early Blight | 0.44 | Tomato Late Blight | No |

### Best practice 5: Use a confidence threshold

Always prefer "uncertain" over confidently wrong UI behavior.
That is a better user experience and a better engineering decision.

### Best practice 6: Release resources

When the classifier is no longer needed:

```java
classifier.close();
```

Also close delegates if you manage them separately.

### Best practice 7: Log intelligently during development

Useful logs:

- model loaded successfully,
- input/output tensor shapes,
- average latency,
- top-3 probabilities,
- error cause if inference fails.

### Best practice 8: Avoid repeated heavy initialization

Do not recreate the interpreter for every prediction unless necessary.
Initialize once and reuse it safely.

### Best practice 9: Keep thread safety in mind

One interpreter should not be shared unsafely across concurrent threads.
Use synchronization or separate interpreters.

### Best practice 10: Document the whole ML pipeline

A good student project report should clearly state:

- dataset,
- training process,
- conversion process,
- normalization,
- Android integration steps,
- benchmark results,
- limitations.

---

## 16. CSE 2206 Viva and Exam Preparation

### Common viva questions

1. What is the difference between TensorFlow and TensorFlow Lite?
2. Why do we convert a model before using it on Android?
3. Why do we store the model in `assets/`?
4. What is normalization and why is it needed?
5. What is the purpose of `Interpreter.run()`?
6. What is quantization?
7. Why might GPU delegate improve performance?
8. Why do we need a background thread for inference?
9. What does confidence threshold mean?
10. Why might a cloud model still be useful?

### Strong answer strategy

When answering viva questions:

- define the concept simply,
- connect it to LeafGuard AI,
- mention one practical reason or example,
- mention one limitation or design trade-off.

### Example strong viva answer

**Question:** Why did you use TensorFlow Lite instead of only cloud prediction?

**Answer:** We used TensorFlow Lite so that LeafGuard AI can classify plant diseases without internet access. This improves privacy and reduces latency because the image stays on the device. We still kept cloud comparison in mind because larger server-side models can be updated more easily and may achieve better accuracy.

### Revision memory tips

Remember this chain:

```text
Model source -> Convert -> Add to assets -> Load -> Preprocess -> Infer -> Postprocess -> Benchmark
```

If you can explain each arrow in that chain, you understand the week well.

---

## 17. Summary

TensorFlow Lite enables **offline AI on Android**.
For LeafGuard AI, that means the app can classify plant disease images directly on the device.

The most important technical lessons from this week are:

- getting a valid `.tflite` model,
- creating the correct `labels.txt`,
- matching preprocessing exactly,
- understanding normalization,
- handling errors safely,
- keeping inference off the main thread,
- using confidence thresholds,
- benchmarking CPU, GPU, and NNAPI options.

If you remember only one sentence, remember this:

**A TensorFlow Lite model is only as good as the preprocessing that feeds it.**

---

## 18. Glossary

### Assets
Folder in the Android project used to store files like `model.tflite` and `labels.txt`.

### Confidence
The score associated with a predicted class, usually interpreted as probability if output is softmax.

### Delegate
A hardware-optimized execution path such as GPU or NNAPI.

### Inference
Running a trained model on new input to produce a prediction.

### Interpreter
The TensorFlow Lite runtime class used to execute the model.

### Logits
Raw model output scores before softmax.

### Normalization
Transforming raw pixel values into the numeric range used during model training.

### Quantization
Reducing numeric precision to shrink model size and often improve speed.

### Representative dataset
Sample data used to calibrate full integer quantization.

### Softmax
Function that converts scores into probabilities.

### Tensor
A multidimensional array used as model input or output.

---

## 19. Quick Revision Checklist

Before you say you are ready for Week 09 viva, confirm that you can answer all of these:

- [ ] I can explain what TensorFlow Lite is.
- [ ] I can explain the difference between training and inference.
- [ ] I know at least three ways to obtain a `.tflite` model.
- [ ] I understand how to create `labels.txt`.
- [ ] I know why label order matters.
- [ ] I know my model's input shape.
- [ ] I know my model's normalization strategy.
- [ ] I can load the model from Android assets.
- [ ] I can run inference in Java.
- [ ] I can compute top-1 prediction.
- [ ] I can explain top-3 predictions.
- [ ] I can explain what confidence means.
- [ ] I can implement a confidence threshold.
- [ ] I know why `Interpreter` is not thread-safe.
- [ ] I can explain why inference should run off the main thread.
- [ ] I can explain GPU delegate and NNAPI.
- [ ] I can benchmark latency across 100 runs.
- [ ] I can compare offline vs cloud architecture.
- [ ] I can explain when offline is better.
- [ ] I can explain when cloud is better.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 09 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| **2** | **learning-notes.md** ← *You are here* | **Theory & Learning Notes** |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Week Overview & Objectives](README.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Theory & Learning Notes** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Practice Exercises →](exercises.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 08: XML Disease Library](../week-08-xml-disease-library/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 10: Notifications, Share & Location ➡](../week-10-notifications-share-location/README.md) |

---
