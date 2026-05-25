# LeafGuard AI - Machine Learning Model Guide

## Overview
This comprehensive guide covers everything you need to know about the plant disease detection machine learning model for LeafGuard AI. It includes instructions for finding pre-trained models, training custom models, converting to TensorFlow Lite, and implementing inference in your Android app.

## Table of Contents
1. [Quick Start with Pre-trained Models](#quick-start-with-pre-trained-models)
2. [Model Requirements](#model-requirements)
3. [Training Custom Model](#training-custom-model)
4. [Converting to TensorFlow Lite](#converting-to-tensorflow-lite)
5. [Model Integration](#model-integration)
6. [Preprocessing Requirements](#preprocessing-requirements)
7. [Output Format and Labels](#output-format-and-labels)
8. [Model Limitations](#model-limitations)
9. [Performance Optimization](#performance-optimization)
10. [Troubleshooting](#troubleshooting)

---

## Quick Start with Pre-trained Models

### Option 1: TensorFlow Hub (Recommended for Beginners)

**MobileNetV2 for Image Classification**:

```python
import tensorflow as tf
import tensorflow_hub as hub

# Load pre-trained MobileNetV2
model_url = "https://tfhub.dev/google/tf2-preview/mobilenet_v2/classification/4"
model = tf.keras.Sequential([
    hub.KerasLayer(model_url, input_shape=(224, 224, 3))
])

# Fine-tune on plant disease dataset (see Training section)
```

**Available Models on TensorFlow Hub**:
- MobileNetV2: https://tfhub.dev/google/imagenet/mobilenet_v2_100_224/classification/5
- EfficientNet: https://tfhub.dev/tensorflow/efficientnet/b0/classification/1
- ResNet50: https://tfhub.dev/tensorflow/resnet_50/classification/1

### Option 2: PyTorch Hub

```python
import torch
import torchvision.models as models

# Load pre-trained ResNet18
model = models.resnet18(pretrained=True)

# Modify final layer for plant disease classes
num_diseases = 38  # Example: PlantVillage has 38 classes
model.fc = torch.nn.Linear(model.fc.in_features, num_diseases)

# Fine-tune on plant disease dataset
```

### Option 3: Kaggle Pre-trained Models

**Popular Plant Disease Detection Models on Kaggle**:

1. **PlantVillage Disease Classification** (38 classes):
   - URL: https://www.kaggle.com/models?search=plant+disease
   - Classes: Tomato, Potato, Apple, etc. with various diseases
   - Accuracy: 95-98%

2. **Plant Disease Recognition Dataset**:
   - URL: https://www.kaggle.com/datasets/vipoooool/new-plant-diseases-dataset
   - 87,000+ images across 38 classes
   - Preprocessed and ready for training

3. **Download Pre-trained Model from Kaggle**:
   ```bash
   # Install Kaggle CLI
   pip install kaggle

   # Configure API credentials (get from Kaggle → Account → API)
   mkdir -p ~/.kaggle
   cp kaggle.json ~/.kaggle/
   chmod 600 ~/.kaggle/kaggle.json

   # Download dataset
   kaggle datasets download -d vipoooool/new-plant-diseases-dataset
   unzip new-plant-diseases-dataset.zip
   ```

### Option 4: Pre-trained Models from GitHub

**Recommended Repositories**:

1. **Plant Disease Detection (TensorFlow)**:
   ```bash
   git clone https://github.com/spMohanty/PlantVillage-Dataset.git
   ```

2. **PlantDoc Dataset** (Multiple plant species):
   - URL: https://github.com/pratikkayal/PlantDoc-Dataset
   - 2,598 images across 13 species, 17 classes

### Quick Model Download (Example)

```python
# Download pre-trained model from URL
import urllib.request
import os

model_url = "https://storage.googleapis.com/download.tensorflow.org/models/mobilenet_v1_2018_08_02/mobilenet_v1_1.0_224.tgz"
model_path = "models/pretrained/mobilenet_v1.tgz"

os.makedirs("models/pretrained", exist_ok=True)
urllib.request.urlretrieve(model_url, model_path)

# Extract and convert (see Converting section)
```

---

## Model Requirements

### Input Specifications
- **Input Shape**: (1, 224, 224, 3) or (1, 299, 299, 3)
  - Batch size: 1
  - Height: 224 or 299 pixels
  - Width: 224 or 299 pixels
  - Channels: 3 (RGB)
- **Data Type**: float32
- **Value Range**: [0, 1] (normalized) or [-1, 1] depending on model

### Output Specifications
- **Output Shape**: (1, num_classes)
  - Batch size: 1
  - num_classes: Number of disease classes (e.g., 38)
- **Data Type**: float32
- **Value Range**: [0, 1] (probabilities after softmax)

### Model Size Constraints
- **For Android**: <50MB recommended (TFLite format)
- **Maximum**: 100MB (affects app size and memory)
- **Optimized**: 5-20MB (quantized models)

### Performance Targets
- **Inference Time**: <500ms on mid-range Android devices
- **Accuracy**: >90% on validation set
- **Memory Usage**: <200MB during inference

---

## Training Custom Model

### Step 1: Prepare Dataset

**Dataset Structure**:
```
dataset/
├── train/
│   ├── Tomato_Early_Blight/
│   │   ├── image001.jpg
│   │   ├── image002.jpg
│   │   └── ...
│   ├── Tomato_Late_Blight/
│   │   └── ...
│   ├── Potato_Healthy/
│   │   └── ...
│   └── ...
├── validation/
│   ├── Tomato_Early_Blight/
│   └── ...
└── test/
    ├── Tomato_Early_Blight/
    └── ...
```

**Recommended Dataset Split**:
- Training: 70-80%
- Validation: 10-15%
- Test: 10-15%

### Step 2: Data Augmentation

```python
from tensorflow.keras.preprocessing.image import ImageDataGenerator

# Data augmentation for training
train_datagen = ImageDataGenerator(
    rescale=1./255,
    rotation_range=20,
    width_shift_range=0.2,
    height_shift_range=0.2,
    horizontal_flip=True,
    zoom_range=0.2,
    fill_mode='nearest'
)

# Only rescaling for validation
validation_datagen = ImageDataGenerator(rescale=1./255)

# Load data
train_generator = train_datagen.flow_from_directory(
    'dataset/train',
    target_size=(224, 224),
    batch_size=32,
    class_mode='categorical'
)

validation_generator = validation_datagen.flow_from_directory(
    'dataset/validation',
    target_size=(224, 224),
    batch_size=32,
    class_mode='categorical'
)
```

### Step 3: Build Model Architecture

**Option A: Transfer Learning (Recommended)**:

```python
import tensorflow as tf
from tensorflow.keras import layers, models

# Load pre-trained MobileNetV2
base_model = tf.keras.applications.MobileNetV2(
    input_shape=(224, 224, 3),
    include_top=False,
    weights='imagenet'
)

# Freeze base model layers
base_model.trainable = False

# Build model
model = models.Sequential([
    base_model,
    layers.GlobalAveragePooling2D(),
    layers.Dropout(0.2),
    layers.Dense(128, activation='relu'),
    layers.Dropout(0.2),
    layers.Dense(num_classes, activation='softmax')
])

model.compile(
    optimizer=tf.keras.optimizers.Adam(learning_rate=0.001),
    loss='categorical_crossentropy',
    metrics=['accuracy']
)

model.summary()
```

**Option B: Custom CNN Architecture**:

```python
model = models.Sequential([
    layers.Conv2D(32, (3, 3), activation='relu', input_shape=(224, 224, 3)),
    layers.MaxPooling2D((2, 2)),
    layers.Conv2D(64, (3, 3), activation='relu'),
    layers.MaxPooling2D((2, 2)),
    layers.Conv2D(128, (3, 3), activation='relu'),
    layers.MaxPooling2D((2, 2)),
    layers.Flatten(),
    layers.Dense(128, activation='relu'),
    layers.Dropout(0.5),
    layers.Dense(num_classes, activation='softmax')
])

model.compile(
    optimizer='adam',
    loss='categorical_crossentropy',
    metrics=['accuracy']
)
```

### Step 4: Train Model

```python
# Callbacks
callbacks = [
    tf.keras.callbacks.ModelCheckpoint(
        'models/checkpoints/model_best.h5',
        monitor='val_accuracy',
        save_best_only=True,
        mode='max',
        verbose=1
    ),
    tf.keras.callbacks.EarlyStopping(
        monitor='val_loss',
        patience=5,
        restore_best_weights=True,
        verbose=1
    ),
    tf.keras.callbacks.ReduceLROnPlateau(
        monitor='val_loss',
        factor=0.2,
        patience=3,
        min_lr=0.00001,
        verbose=1
    )
]

# Train
history = model.fit(
    train_generator,
    epochs=25,
    validation_data=validation_generator,
    callbacks=callbacks
)

# Save final model
model.save('models/saved_model/plant_disease_model.h5')
```

### Step 5: Evaluate Model

```python
# Evaluate on test set
test_datagen = ImageDataGenerator(rescale=1./255)
test_generator = test_datagen.flow_from_directory(
    'dataset/test',
    target_size=(224, 224),
    batch_size=32,
    class_mode='categorical',
    shuffle=False
)

test_loss, test_accuracy = model.evaluate(test_generator)
print(f"Test Accuracy: {test_accuracy * 100:.2f}%")

# Confusion matrix
from sklearn.metrics import classification_report, confusion_matrix
import numpy as np

predictions = model.predict(test_generator)
predicted_classes = np.argmax(predictions, axis=1)
true_classes = test_generator.classes

print(classification_report(true_classes, predicted_classes))
```

### Step 6: Fine-Tuning (Optional)

```python
# Unfreeze base model for fine-tuning
base_model.trainable = True

# Freeze early layers, train later layers
for layer in base_model.layers[:-20]:
    layer.trainable = False

# Recompile with lower learning rate
model.compile(
    optimizer=tf.keras.optimizers.Adam(learning_rate=0.0001),
    loss='categorical_crossentropy',
    metrics=['accuracy']
)

# Continue training
history_fine = model.fit(
    train_generator,
    epochs=10,
    validation_data=validation_generator,
    callbacks=callbacks
)
```

---

## Converting to TensorFlow Lite

### Step 1: Convert Keras Model to TFLite

**Basic Conversion**:

```python
import tensorflow as tf

# Load trained model
model = tf.keras.models.load_model('models/saved_model/plant_disease_model.h5')

# Convert to TFLite
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

# Save TFLite model
with open('models/saved_model/model.tflite', 'wb') as f:
    f.write(tflite_model)

print("Model converted to TFLite successfully!")
print(f"Size: {len(tflite_model) / 1024 / 1024:.2f} MB")
```

### Step 2: Optimize Model (Quantization)

**Dynamic Range Quantization** (Recommended):

```python
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_quantized_model = converter.convert()

with open('models/saved_model/model_quantized.tflite', 'wb') as f:
    f.write(tflite_quantized_model)

print(f"Original size: {len(tflite_model) / 1024 / 1024:.2f} MB")
print(f"Quantized size: {len(tflite_quantized_model) / 1024 / 1024:.2f} MB")
```

**Full Integer Quantization** (Smallest size):

```python
def representative_dataset():
    """Generate representative dataset for quantization"""
    for _ in range(100):
        # Use random images or subset of training data
        data = np.random.rand(1, 224, 224, 3).astype(np.float32)
        yield [data]

converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
converter.representative_dataset = representative_dataset
converter.target_spec.supported_ops = [tf.lite.OpsSet.TFLITE_BUILTINS_INT8]
converter.inference_input_type = tf.uint8
converter.inference_output_type = tf.uint8

tflite_int8_model = converter.convert()

with open('models/saved_model/model_int8.tflite', 'wb') as f:
    f.write(tflite_int8_model)
```

### Step 3: Verify Converted Model

```python
# Load TFLite model and allocate tensors
interpreter = tf.lite.Interpreter(model_path='models/saved_model/model.tflite')
interpreter.allocate_tensors()

# Get input and output details
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

print("Input Details:")
print(f"  Shape: {input_details[0]['shape']}")
print(f"  Type: {input_details[0]['dtype']}")

print("Output Details:")
print(f"  Shape: {output_details[0]['shape']}")
print(f"  Type: {output_details[0]['dtype']}")

# Test with sample image
from PIL import Image
import numpy as np

image = Image.open('test_image.jpg')
image = image.resize((224, 224))
image_array = np.array(image, dtype=np.float32) / 255.0
image_array = np.expand_dims(image_array, axis=0)

interpreter.set_tensor(input_details[0]['index'], image_array)
interpreter.invoke()
output_data = interpreter.get_tensor(output_details[0]['index'])

print(f"Predictions: {output_data}")
print(f"Top class: {np.argmax(output_data)}")
```

### Step 4: Create Labels File

```python
# Extract class names from training generator
class_names = list(train_generator.class_indices.keys())

# Save to labels.txt
with open('models/saved_model/labels.txt', 'w') as f:
    for label in class_names:
        f.write(f"{label}\n")

print(f"Saved {len(class_names)} labels to labels.txt")
```

**Labels file format** (`labels.txt`):
```
Tomato_Early_Blight
Tomato_Late_Blight
Tomato_Healthy
Potato_Early_Blight
Potato_Late_Blight
Potato_Healthy
Apple_Black_Rot
Apple_Healthy
...
```

---

## Model Integration

### Android Integration (Java)

**Step 1: Add model to assets**:
```
android-app/app/src/main/assets/
├── model.tflite
└── labels.txt
```

**Step 2: Load model in Android**:

```java
import org.tensorflow.lite.Interpreter;
import android.content.res.AssetFileDescriptor;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class DiseaseClassifier {
    private Interpreter tflite;
    private List<String> labels;

    public DiseaseClassifier(Context context) throws IOException {
        // Load model
        tflite = new Interpreter(loadModelFile(context));

        // Load labels
        labels = loadLabels(context);
    }

    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private List<String> loadLabels(Context context) throws IOException {
        List<String> labels = new ArrayList<>();
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(context.getAssets().open("labels.txt"))
        );
        String line;
        while ((line = reader.readLine()) != null) {
            labels.add(line);
        }
        reader.close();
        return labels;
    }

    public String classify(Bitmap bitmap) {
        // Preprocess image
        ByteBuffer inputBuffer = preprocessImage(bitmap);

        // Run inference
        float[][] output = new float[1][labels.size()];
        tflite.run(inputBuffer, output);

        // Get top prediction
        int maxIndex = 0;
        float maxConfidence = output[0][0];
        for (int i = 1; i < labels.size(); i++) {
            if (output[0][i] > maxConfidence) {
                maxConfidence = output[0][i];
                maxIndex = i;
            }
        }

        return labels.get(maxIndex) + " (" + (maxConfidence * 100) + "%)";
    }

    private ByteBuffer preprocessImage(Bitmap bitmap) {
        // Resize and normalize
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
        buffer.order(ByteOrder.nativeOrder());

        int[] pixels = new int[224 * 224];
        resized.getPixels(pixels, 0, 224, 0, 0, 224, 224);

        for (int pixel : pixels) {
            buffer.putFloat(((pixel >> 16) & 0xFF) / 255.0f); // R
            buffer.putFloat(((pixel >> 8) & 0xFF) / 255.0f);  // G
            buffer.putFloat((pixel & 0xFF) / 255.0f);         // B
        }

        return buffer;
    }
}
```

### Backend Integration (Python/FastAPI)

See [backend-api/README.md](../backend-api/README.md) for complete implementation.

---

## Preprocessing Requirements

### Image Preprocessing Pipeline

```python
def preprocess_image(image_path, target_size=(224, 224)):
    """
    Preprocess image for model inference

    Args:
        image_path: Path to image file
        target_size: Target size (width, height)

    Returns:
        Preprocessed numpy array
    """
    from PIL import Image
    import numpy as np

    # Load image
    image = Image.open(image_path)

    # Convert to RGB (handle grayscale or RGBA)
    if image.mode != 'RGB':
        image = image.convert('RGB')

    # Resize
    image = image.resize(target_size)

    # Convert to numpy array
    image_array = np.array(image, dtype=np.float32)

    # Normalize to [0, 1]
    image_array = image_array / 255.0

    # Add batch dimension
    image_array = np.expand_dims(image_array, axis=0)

    return image_array
```

### Normalization Methods

Different models use different normalization:

**1. Standard Normalization [0, 1]**:
```python
image_array = image_array / 255.0
```

**2. ImageNet Normalization [-1, 1]**:
```python
image_array = (image_array / 127.5) - 1
```

**3. Mean Subtraction and Std Division**:
```python
mean = [0.485, 0.456, 0.406]  # ImageNet mean
std = [0.229, 0.224, 0.225]   # ImageNet std

for i in range(3):
    image_array[:, :, :, i] = (image_array[:, :, :, i] - mean[i]) / std[i]
```

**Check your model's requirements** in the original training code.

### Android Preprocessing (Java)

```java
private ByteBuffer preprocessImage(Bitmap bitmap) {
    // Resize
    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

    // Allocate buffer (4 bytes per float * width * height * channels)
    ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
    buffer.order(ByteOrder.nativeOrder());

    // Get pixels
    int[] pixels = new int[224 * 224];
    resized.getPixels(pixels, 0, 224, 0, 0, 224, 224);

    // Normalize and add to buffer
    for (int pixel : pixels) {
        // Extract RGB values
        int r = (pixel >> 16) & 0xFF;
        int g = (pixel >> 8) & 0xFF;
        int b = pixel & 0xFF;

        // Normalize to [0, 1]
        buffer.putFloat(r / 255.0f);
        buffer.putFloat(g / 255.0f);
        buffer.putFloat(b / 255.0f);
    }

    return buffer;
}
```

---

## Output Format and Labels

### Output Tensor Format

**Standard Classification Output**:
```
Shape: (1, num_classes)
Type: float32
Range: [0, 1] (probabilities after softmax)

Example:
[[0.05, 0.12, 0.78, 0.03, 0.02]]
       ↑           ↑
   Class 0     Class 2 (highest)
```

### Labels Mapping

**labels.txt format**:
```
Tomato___Early_blight
Tomato___Late_blight
Tomato___healthy
Potato___Early_blight
Potato___Late_blight
Potato___healthy
```

**Load labels in Python**:
```python
with open('labels.txt', 'r') as f:
    labels = [line.strip() for line in f.readlines()]
```

**Map predictions to labels**:
```python
import numpy as np

# Get predictions
predictions = model.predict(image_array)

# Get top class
top_index = np.argmax(predictions[0])
top_confidence = predictions[0][top_index]
disease = labels[top_index]

print(f"Disease: {disease}, Confidence: {top_confidence * 100:.2f}%")

# Get top 3 predictions
top_3_indices = np.argsort(predictions[0])[-3:][::-1]
for i in top_3_indices:
    print(f"{labels[i]}: {predictions[0][i] * 100:.2f}%")
```

### Common Disease Classes

**PlantVillage Dataset** (38 classes):
- Apple: scab, black_rot, cedar_apple_rust, healthy
- Cherry: powdery_mildew, healthy
- Corn: cercospora_leaf_spot, common_rust, northern_leaf_blight, healthy
- Grape: black_rot, esca, leaf_blight, healthy
- Peach: bacterial_spot, healthy
- Pepper: bacterial_spot, healthy
- Potato: early_blight, late_blight, healthy
- Strawberry: leaf_scorch, healthy
- Tomato: bacterial_spot, early_blight, late_blight, leaf_mold, septoria_leaf_spot, spider_mites, target_spot, mosaic_virus, yellow_leaf_curl_virus, healthy

### Formatting Labels for Display

```python
def format_disease_name(label):
    """
    Format disease label for display

    Example:
        "Tomato___Early_blight" → "Tomato - Early Blight"
    """
    # Split by '___'
    parts = label.split('___')
    if len(parts) == 2:
        plant = parts[0]
        disease = parts[1].replace('_', ' ').title()
        return f"{plant} - {disease}"
    return label.replace('_', ' ').title()
```

---

## Model Limitations

### 1. Limited to Trained Classes
- Model can only detect diseases it was trained on
- Cannot identify new diseases or plant species
- May misclassify similar-looking diseases

### 2. Image Quality Dependencies
- **Lighting**: Poor lighting reduces accuracy
- **Focus**: Blurry images lead to poor predictions
- **Angle**: Extreme angles may confuse model
- **Background**: Cluttered backgrounds can affect results

### 3. Dataset Bias
- Model performance depends on training data diversity
- May not generalize to different regions/climates
- Seasonal variations may affect accuracy

### 4. False Positives/Negatives
- **False Positive**: Healthy plant predicted as diseased
- **False Negative**: Diseased plant predicted as healthy
- Use confidence threshold (e.g., >70%) to filter uncertain predictions

### 5. Real-time Constraints
- Inference time varies by device
- May be slow on older/low-end devices
- Battery consumption during continuous use

### 6. Model Size Trade-offs
- Smaller models (quantized) = lower accuracy
- Larger models = better accuracy but slower inference
- Balance based on target devices

---

## Performance Optimization

### 1. Model Quantization

```python
# Post-training quantization (reduces size by 4x)
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()
```

**Benefits**:
- 75% size reduction (e.g., 40MB → 10MB)
- Faster inference
- Lower memory usage
- Minimal accuracy loss (<2%)

### 2. Model Pruning

```python
import tensorflow_model_optimization as tfmot

# Apply pruning
pruning_params = {
    'pruning_schedule': tfmot.sparsity.keras.PolynomialDecay(
        initial_sparsity=0.0,
        final_sparsity=0.5,
        begin_step=0,
        end_step=1000
    )
}

model_for_pruning = tfmot.sparsity.keras.prune_low_magnitude(model, **pruning_params)

# Train pruned model
model_for_pruning.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])
model_for_pruning.fit(train_generator, epochs=10)

# Strip pruning
model_for_export = tfmot.sparsity.keras.strip_pruning(model_for_pruning)
```

### 3. Use Smaller Base Models

| Model | Size | Inference Time | Accuracy |
|-------|------|----------------|----------|
| MobileNetV2 | 14MB | 50ms | 90% |
| MobileNetV3-Small | 6MB | 30ms | 87% |
| EfficientNet-B0 | 20MB | 80ms | 93% |
| ResNet50 | 98MB | 200ms | 95% |

**Recommendation**: MobileNetV2 or MobileNetV3 for Android

### 4. GPU Acceleration (Android)

```java
import org.tensorflow.lite.gpu.GpuDelegate;

// Use GPU delegate
GpuDelegate gpuDelegate = new GpuDelegate();
Interpreter.Options options = new Interpreter.Options();
options.addDelegate(gpuDelegate);

Interpreter tflite = new Interpreter(loadModelFile(context), options);
```

### 5. NNAPI Acceleration (Android)

```java
import org.tensorflow.lite.nnapi.NnApiDelegate;

// Use NNAPI delegate
NnApiDelegate nnApiDelegate = new NnApiDelegate();
Interpreter.Options options = new Interpreter.Options();
options.addDelegate(nnApiDelegate);

Interpreter tflite = new Interpreter(loadModelFile(context), options);
```

### 6. Batch Processing (Backend)

```python
# Process multiple images at once
images = [preprocess_image(img) for img in image_paths]
batch = np.vstack(images)

predictions = model.predict(batch)  # Faster than individual predictions
```

---

## Troubleshooting

### 1. Low Accuracy

**Symptoms**: Model predictions are incorrect

**Solutions**:
- Check if test images are similar to training data
- Verify preprocessing matches training (normalization)
- Increase training data (data augmentation)
- Use transfer learning from better base model
- Train for more epochs
- Check for data leakage (test in training set)

### 2. Slow Inference

**Symptoms**: Predictions take >1 second

**Solutions**:
- Use quantized model
- Enable GPU/NNAPI acceleration
- Reduce input image size (224x224 instead of 299x299)
- Use smaller architecture (MobileNet instead of ResNet)
- Close other apps consuming resources

### 3. Out of Memory

**Symptoms**: App crashes during inference

**Solutions**:
- Reduce batch size to 1
- Use quantized model (lower memory footprint)
- Resize images before passing to model
- Release previous Bitmap objects
```java
bitmap.recycle();
```
- Add to AndroidManifest.xml:
```xml
<application android:largeHeap="true">
```

### 4. Model Not Loading

**Symptoms**: FileNotFoundException or model not found

**Solutions**:
- Verify model is in `app/src/main/assets/`
- Check file name matches code
- Ensure file is not compressed (add to build.gradle):
```gradle
aaptOptions {
    noCompress "tflite"
}
```
- Clean and rebuild project

### 5. Incorrect Predictions

**Symptoms**: Always predicts same class or random results

**Solutions**:
- Verify preprocessing (normalization range)
- Check input shape matches model
- Verify labels.txt order matches training
- Test with known good images
- Check if model is corrupted (reconvert)

### 6. TensorFlow Lite Conversion Fails

**Symptoms**: Error during conversion

**Solutions**:
```python
# Enable experimental features
converter.experimental_new_converter = True
converter.target_spec.supported_ops = [
    tf.lite.OpsSet.TFLITE_BUILTINS,
    tf.lite.OpsSet.SELECT_TF_OPS
]

# Or save as SavedModel first, then convert
model.save('saved_model/', save_format='tf')
converter = tf.lite.TFLiteConverter.from_saved_model('saved_model/')
```

### 7. Labels Not Matching Output

**Symptoms**: Wrong disease names shown

**Solutions**:
- Ensure labels.txt order matches model training
```python
# Print class indices during training
print(train_generator.class_indices)

# Save in same order
class_names = sorted(train_generator.class_indices, key=train_generator.class_indices.get)
```

### 8. Model Overfitting

**Symptoms**: High training accuracy, low validation accuracy

**Solutions**:
- Add more dropout layers
```python
layers.Dropout(0.5)
```
- Use data augmentation
- Reduce model complexity
- Early stopping
- Collect more diverse training data

---

## Performance Expectations

### Accuracy Benchmarks

| Dataset | Classes | Expected Accuracy |
|---------|---------|-------------------|
| PlantVillage | 38 | 95-98% |
| PlantDoc | 17 | 85-90% |
| Custom (small) | 5-10 | 80-85% |
| Custom (large) | 20+ | 90-95% |

### Inference Speed Benchmarks

| Device | Model Type | Inference Time |
|--------|------------|----------------|
| High-end (Snapdragon 8 Gen 2) | Float32 | 30-50ms |
| High-end (Snapdragon 8 Gen 2) | Quantized | 20-30ms |
| Mid-range (Snapdragon 7 Gen 1) | Float32 | 80-120ms |
| Mid-range (Snapdragon 7 Gen 1) | Quantized | 50-80ms |
| Low-end (Snapdragon 4 Gen 1) | Float32 | 200-300ms |
| Low-end (Snapdragon 4 Gen 1) | Quantized | 120-180ms |

### Model Size Expectations

| Model Type | Typical Size |
|------------|--------------|
| Float32 | 30-50MB |
| Dynamic Quantization | 8-13MB |
| Full Integer Quantization | 5-10MB |
| Pruned + Quantized | 3-8MB |

---

## Additional Resources

### Datasets
- **PlantVillage**: https://www.kaggle.com/datasets/emmarex/plantdisease
- **PlantDoc**: https://github.com/pratikkayal/PlantDoc-Dataset
- **Plant Pathology 2020**: https://www.kaggle.com/c/plant-pathology-2020-fgvc7
- **IP102 (Pest Detection)**: https://github.com/xpwu95/IP102

### Tutorials
- [TensorFlow Image Classification](https://www.tensorflow.org/tutorials/images/classification)
- [Transfer Learning Guide](https://www.tensorflow.org/tutorials/images/transfer_learning)
- [TFLite Conversion](https://www.tensorflow.org/lite/convert)
- [TFLite Android Guide](https://www.tensorflow.org/lite/guide/android)

### Research Papers
- **PlantVillage Paper**: https://arxiv.org/abs/1511.08060
- **MobileNets**: https://arxiv.org/abs/1704.04861
- **EfficientNet**: https://arxiv.org/abs/1905.11946

### Tools
- **Netron** (Model Visualizer): https://github.com/lutzroeder/netron
- **TensorFlow Model Optimization Toolkit**: https://www.tensorflow.org/model_optimization
- **Android ML Model Benchmark**: https://github.com/tensorflow/tensorflow/tree/master/tensorflow/lite/tools/benchmark

---

## Next Steps

1. **Week 5**: Download or train your first model
2. **Week 6**: Convert model to TensorFlow Lite
3. **Week 7**: Integrate model into Android app
4. **Week 8**: Test with various plant images
5. **Week 11**: Optimize for performance
6. **Week 12**: Evaluate and document results

---

**Remember**: Start with a pre-trained model to ensure your app works, then consider training a custom model for better accuracy on your specific use case!
