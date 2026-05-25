# Machine Learning Exercises

## Overview

This directory contains machine learning exercises focused on building, training, and deploying a plant disease detection model for the LeafGuard AI application. These exercises cover model selection, data preprocessing, training, TensorFlow Lite conversion, and inference optimization.

## Weekly Mapping

### Week 6: ML Model Integration
- Model selection and architecture
- Dataset preparation and preprocessing
- Model training and evaluation

### Week 9: Model Optimization
- TensorFlow Lite conversion
- Model quantization
- Inference optimization
- Performance benchmarking

## Exercise Categories

### 1. Model Selection and Architecture (Week 6)

**Exercise 1.1: Explore Pre-trained Models**

Compare different model architectures for plant disease detection:

```python
import tensorflow as tf
from tensorflow.keras.applications import (
    MobileNetV2,
    EfficientNetB0,
    ResNet50V2
)

def compare_models():
    """Compare different base models for transfer learning"""

    models_info = []

    # MobileNetV2 - Optimized for mobile
    mobilenet = MobileNetV2(
        input_shape=(224, 224, 3),
        include_top=False,
        weights='imagenet'
    )
    models_info.append({
        'name': 'MobileNetV2',
        'params': mobilenet.count_params(),
        'size_mb': mobilenet.count_params() * 4 / (1024**2),
        'mobile_optimized': True
    })

    # EfficientNetB0 - Balanced efficiency
    efficientnet = EfficientNetB0(
        input_shape=(224, 224, 3),
        include_top=False,
        weights='imagenet'
    )
    models_info.append({
        'name': 'EfficientNetB0',
        'params': efficientnet.count_params(),
        'size_mb': efficientnet.count_params() * 4 / (1024**2),
        'mobile_optimized': True
    })

    # ResNet50V2 - High accuracy but larger
    resnet = ResNet50V2(
        input_shape=(224, 224, 3),
        include_top=False,
        weights='imagenet'
    )
    models_info.append({
        'name': 'ResNet50V2',
        'params': resnet.count_params(),
        'size_mb': resnet.count_params() * 4 / (1024**2),
        'mobile_optimized': False
    })

    return models_info

# Run comparison
models = compare_models()
for model in models:
    print(f"\n{model['name']}:")
    print(f"  Parameters: {model['params']:,}")
    print(f"  Size: {model['size_mb']:.2f} MB")
    print(f"  Mobile Optimized: {model['mobile_optimized']}")
```

**Verification**:
- Run the comparison script
- Analyze parameter counts and model sizes
- Choose appropriate model for mobile deployment (MobileNetV2 or EfficientNetB0)
- Document your choice with reasoning

**Exercise 1.2: Build Custom Model Architecture**

```python
from tensorflow.keras import layers, models

def create_plant_disease_model(num_classes=38, input_shape=(224, 224, 3)):
    """
    Create a custom model for plant disease classification
    using transfer learning with MobileNetV2
    """

    # Load pre-trained base model
    base_model = MobileNetV2(
        input_shape=input_shape,
        include_top=False,
        weights='imagenet'
    )

    # Freeze base model layers
    base_model.trainable = False

    # Build classification head
    inputs = layers.Input(shape=input_shape)

    # Preprocessing
    x = layers.Rescaling(1./127.5, offset=-1)(inputs)

    # Base model
    x = base_model(x, training=False)

    # Classification layers
    x = layers.GlobalAveragePooling2D()(x)
    x = layers.Dropout(0.2)(x)
    x = layers.Dense(128, activation='relu')(x)
    x = layers.Dropout(0.2)(x)
    outputs = layers.Dense(num_classes, activation='softmax')(x)

    # Create model
    model = models.Model(inputs, outputs)

    return model

# Create and inspect model
model = create_plant_disease_model()
model.summary()
```

**Verification**:
- Model summary shows correct architecture
- Total parameters are reasonable for mobile deployment
- Output shape matches number of disease classes
- Save model architecture diagram

### 2. Data Preprocessing and Augmentation (Week 6)

**Exercise 2.1: Dataset Preparation**

```python
import tensorflow as tf
from pathlib import Path
import json

class PlantDiseaseDataset:
    def __init__(self, data_dir, img_size=(224, 224), batch_size=32):
        self.data_dir = Path(data_dir)
        self.img_size = img_size
        self.batch_size = batch_size
        self.class_names = None

    def load_dataset(self, validation_split=0.2):
        """Load and split dataset into train and validation"""

        # Load dataset from directory
        train_ds = tf.keras.preprocessing.image_dataset_from_directory(
            self.data_dir,
            validation_split=validation_split,
            subset="training",
            seed=123,
            image_size=self.img_size,
            batch_size=self.batch_size
        )

        val_ds = tf.keras.preprocessing.image_dataset_from_directory(
            self.data_dir,
            validation_split=validation_split,
            subset="validation",
            seed=123,
            image_size=self.img_size,
            batch_size=self.batch_size
        )

        self.class_names = train_ds.class_names

        return train_ds, val_ds

    def get_class_distribution(self, dataset):
        """Analyze class distribution in dataset"""

        class_counts = {}
        for images, labels in dataset:
            for label in labels.numpy():
                class_name = self.class_names[label]
                class_counts[class_name] = class_counts.get(class_name, 0) + 1

        return class_counts

    def save_class_names(self, output_path="class_names.json"):
        """Save class names for later use"""
        with open(output_path, 'w') as f:
            json.dump(self.class_names, f, indent=2)
        print(f"Class names saved to {output_path}")

# Usage
dataset = PlantDiseaseDataset("data/plant_disease_dataset")
train_ds, val_ds = dataset.load_dataset()

# Analyze distribution
train_dist = dataset.get_class_distribution(train_ds)
print("Training set distribution:")
for class_name, count in sorted(train_dist.items()):
    print(f"  {class_name}: {count}")

# Save class names
dataset.save_class_names()
```

**Verification**:
- Dataset loads successfully
- Class distribution is displayed
- Validation split is appropriate
- Class names are saved correctly

**Exercise 2.2: Data Augmentation**

```python
def create_augmented_dataset(train_ds, val_ds):
    """Apply data augmentation to training set"""

    # Define augmentation layers
    data_augmentation = tf.keras.Sequential([
        layers.RandomFlip("horizontal_and_vertical"),
        layers.RandomRotation(0.2),
        layers.RandomZoom(0.2),
        layers.RandomBrightness(0.2),
        layers.RandomContrast(0.2),
    ])

    # Apply augmentation to training set only
    train_ds = train_ds.map(
        lambda x, y: (data_augmentation(x, training=True), y),
        num_parallel_calls=tf.data.AUTOTUNE
    )

    # Optimize performance
    train_ds = train_ds.prefetch(buffer_size=tf.data.AUTOTUNE)
    val_ds = val_ds.prefetch(buffer_size=tf.data.AUTOTUNE)

    return train_ds, val_ds

# Visualize augmented images
import matplotlib.pyplot as plt

def visualize_augmentation(dataset, augmentation, num_images=9):
    """Visualize augmented images"""

    # Get a batch of images
    for images, labels in dataset.take(1):
        plt.figure(figsize=(10, 10))

        for i in range(num_images):
            # Apply augmentation
            augmented_image = augmentation(images[i:i+1])

            ax = plt.subplot(3, 3, i + 1)
            plt.imshow(augmented_image[0].numpy().astype("uint8"))
            plt.axis("off")

        plt.tight_layout()
        plt.savefig("augmentation_examples.png")
        print("Augmentation visualization saved")
```

**Verification**:
- Augmentation applies correctly
- Visualized images show variety
- Performance optimization is working
- Original validation set is unchanged

### 3. Model Training (Week 6)

**Exercise 3.1: Implement Training Pipeline**

```python
from tensorflow.keras.callbacks import (
    EarlyStopping,
    ModelCheckpoint,
    ReduceLROnPlateau,
    TensorBoard
)
from datetime import datetime

class ModelTrainer:
    def __init__(self, model, train_ds, val_ds):
        self.model = model
        self.train_ds = train_ds
        self.val_ds = val_ds
        self.history = None

    def compile_model(self):
        """Compile model with optimizer and loss"""
        self.model.compile(
            optimizer=tf.keras.optimizers.Adam(learning_rate=0.001),
            loss=tf.keras.losses.SparseCategoricalCrossentropy(),
            metrics=['accuracy']
        )

    def setup_callbacks(self, checkpoint_dir="checkpoints"):
        """Setup training callbacks"""

        timestamp = datetime.now().strftime("%Y%m%d-%H%M%S")

        callbacks = [
            # Save best model
            ModelCheckpoint(
                filepath=f"{checkpoint_dir}/model_{timestamp}_{{epoch:02d}}_{{val_accuracy:.2f}}.h5",
                monitor='val_accuracy',
                save_best_only=True,
                verbose=1
            ),

            # Early stopping
            EarlyStopping(
                monitor='val_loss',
                patience=10,
                restore_best_weights=True,
                verbose=1
            ),

            # Reduce learning rate
            ReduceLROnPlateau(
                monitor='val_loss',
                factor=0.2,
                patience=5,
                min_lr=1e-7,
                verbose=1
            ),

            # TensorBoard logging
            TensorBoard(
                log_dir=f"logs/{timestamp}",
                histogram_freq=1
            )
        ]

        return callbacks

    def train(self, epochs=50):
        """Train the model"""

        self.compile_model()
        callbacks = self.setup_callbacks()

        print("Starting training...")
        self.history = self.model.fit(
            self.train_ds,
            validation_data=self.val_ds,
            epochs=epochs,
            callbacks=callbacks
        )

        return self.history

    def plot_training_history(self):
        """Plot training and validation metrics"""

        fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(12, 4))

        # Accuracy plot
        ax1.plot(self.history.history['accuracy'], label='Train Accuracy')
        ax1.plot(self.history.history['val_accuracy'], label='Val Accuracy')
        ax1.set_xlabel('Epoch')
        ax1.set_ylabel('Accuracy')
        ax1.set_title('Model Accuracy')
        ax1.legend()
        ax1.grid(True)

        # Loss plot
        ax2.plot(self.history.history['loss'], label='Train Loss')
        ax2.plot(self.history.history['val_loss'], label='Val Loss')
        ax2.set_xlabel('Epoch')
        ax2.set_ylabel('Loss')
        ax2.set_title('Model Loss')
        ax2.legend()
        ax2.grid(True)

        plt.tight_layout()
        plt.savefig('training_history.png')
        print("Training history plot saved")

# Usage
trainer = ModelTrainer(model, train_ds, val_ds)
history = trainer.train(epochs=50)
trainer.plot_training_history()
```

**Verification**:
- Model trains without errors
- Validation accuracy improves
- Best model is saved
- Training plots are generated
- TensorBoard logs are created

**Exercise 3.2: Model Evaluation**

```python
import numpy as np
from sklearn.metrics import classification_report, confusion_matrix
import seaborn as sns

def evaluate_model(model, test_ds, class_names):
    """Comprehensive model evaluation"""

    # Get predictions
    y_true = []
    y_pred = []

    for images, labels in test_ds:
        predictions = model.predict(images)
        y_true.extend(labels.numpy())
        y_pred.extend(np.argmax(predictions, axis=1))

    # Classification report
    print("\nClassification Report:")
    print(classification_report(
        y_true,
        y_pred,
        target_names=class_names
    ))

    # Confusion matrix
    cm = confusion_matrix(y_true, y_pred)

    plt.figure(figsize=(15, 12))
    sns.heatmap(
        cm,
        annot=True,
        fmt='d',
        cmap='Blues',
        xticklabels=class_names,
        yticklabels=class_names
    )
    plt.title('Confusion Matrix')
    plt.ylabel('True Label')
    plt.xlabel('Predicted Label')
    plt.xticks(rotation=45, ha='right')
    plt.yticks(rotation=0)
    plt.tight_layout()
    plt.savefig('confusion_matrix.png')
    print("Confusion matrix saved")

    # Calculate per-class accuracy
    class_accuracy = cm.diagonal() / cm.sum(axis=1)
    print("\nPer-class Accuracy:")
    for i, acc in enumerate(class_accuracy):
        print(f"  {class_names[i]}: {acc:.2%}")

    # Overall accuracy
    overall_accuracy = np.sum(cm.diagonal()) / np.sum(cm)
    print(f"\nOverall Accuracy: {overall_accuracy:.2%}")

    return {
        'confusion_matrix': cm,
        'class_accuracy': class_accuracy,
        'overall_accuracy': overall_accuracy
    }

# Evaluate
results = evaluate_model(model, val_ds, dataset.class_names)
```

**Verification**:
- Classification report shows good metrics
- Confusion matrix is generated
- Per-class accuracy is calculated
- Overall accuracy meets target (>85%)

### 4. TensorFlow Lite Conversion (Week 9)

**Exercise 4.1: Basic TFLite Conversion**

```python
def convert_to_tflite(model, output_path="plant_disease_model.tflite"):
    """Convert Keras model to TensorFlow Lite format"""

    # Convert model
    converter = tf.lite.TFLiteConverter.from_keras_model(model)

    # Basic conversion
    tflite_model = converter.convert()

    # Save model
    with open(output_path, 'wb') as f:
        f.write(tflite_model)

    # Get model size
    size_mb = len(tflite_model) / (1024 * 1024)
    print(f"TFLite model saved to {output_path}")
    print(f"Model size: {size_mb:.2f} MB")

    return tflite_model

# Convert model
tflite_model = convert_to_tflite(model)
```

**Verification**:
- Model converts successfully
- File is created with reasonable size
- No conversion errors or warnings

**Exercise 4.2: Quantized Model Conversion**

```python
def convert_to_quantized_tflite(
    model,
    train_ds,
    output_path="plant_disease_model_quantized.tflite"
):
    """Convert model with quantization for smaller size and faster inference"""

    def representative_dataset():
        """Generate representative dataset for quantization"""
        for images, _ in train_ds.take(100):
            yield [tf.cast(images, tf.float32)]

    # Configure converter
    converter = tf.lite.TFLiteConverter.from_keras_model(model)

    # Enable optimizations
    converter.optimizations = [tf.lite.Optimize.DEFAULT]

    # Set representative dataset for full integer quantization
    converter.representative_dataset = representative_dataset

    # Ensure inputs/outputs are float32 for compatibility
    converter.target_spec.supported_ops = [
        tf.lite.OpsSet.TFLITE_BUILTINS_INT8
    ]
    converter.inference_input_type = tf.float32
    converter.inference_output_type = tf.float32

    # Convert
    tflite_quantized_model = converter.convert()

    # Save
    with open(output_path, 'wb') as f:
        f.write(tflite_quantized_model)

    size_mb = len(tflite_quantized_model) / (1024 * 1024)
    print(f"Quantized TFLite model saved to {output_path}")
    print(f"Model size: {size_mb:.2f} MB")

    return tflite_quantized_model

# Convert with quantization
quantized_model = convert_to_quantized_tflite(model, train_ds)
```

**Verification**:
- Quantized model is significantly smaller
- Conversion completes without errors
- Compare size with non-quantized version

**Exercise 4.3: Compare Model Performance**

```python
def compare_model_performance(keras_model, tflite_path, test_ds):
    """Compare performance between Keras and TFLite models"""

    import time

    # Load TFLite model
    interpreter = tf.lite.Interpreter(model_path=tflite_path)
    interpreter.allocate_tensors()

    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()

    # Test both models
    keras_times = []
    tflite_times = []
    keras_correct = 0
    tflite_correct = 0
    total = 0

    for images, labels in test_ds.take(10):
        for i in range(len(images)):
            image = tf.expand_dims(images[i], 0)
            label = labels[i].numpy()

            # Keras prediction
            start = time.time()
            keras_pred = keras_model.predict(image, verbose=0)
            keras_times.append(time.time() - start)
            keras_class = np.argmax(keras_pred)

            # TFLite prediction
            interpreter.set_tensor(input_details[0]['index'], image)
            start = time.time()
            interpreter.invoke()
            tflite_times.append(time.time() - start)
            tflite_pred = interpreter.get_tensor(output_details[0]['index'])
            tflite_class = np.argmax(tflite_pred)

            # Compare accuracy
            if keras_class == label:
                keras_correct += 1
            if tflite_class == label:
                tflite_correct += 1

            total += 1

    # Print results
    print("\nModel Comparison:")
    print(f"\nKeras Model:")
    print(f"  Average inference time: {np.mean(keras_times)*1000:.2f} ms")
    print(f"  Accuracy: {keras_correct/total:.2%}")

    print(f"\nTFLite Model:")
    print(f"  Average inference time: {np.mean(tflite_times)*1000:.2f} ms")
    print(f"  Accuracy: {tflite_correct/total:.2%}")

    print(f"\nSpeedup: {np.mean(keras_times)/np.mean(tflite_times):.2f}x")

# Compare models
compare_model_performance(model, "plant_disease_model.tflite", val_ds)
```

**Verification**:
- TFLite model is faster than Keras
- Accuracy difference is minimal (<2%)
- Inference times are acceptable (<500ms on CPU)

### 5. Inference Optimization (Week 9)

**Exercise 5.1: Optimize Inference Pipeline**

```python
class OptimizedInference:
    def __init__(self, model_path, labels_path):
        # Load model
        self.interpreter = tf.lite.Interpreter(
            model_path=model_path,
            num_threads=4  # Use multiple threads
        )
        self.interpreter.allocate_tensors()

        # Get tensor details
        self.input_details = self.interpreter.get_input_details()
        self.output_details = self.interpreter.get_output_details()

        # Load labels
        with open(labels_path, 'r') as f:
            self.labels = json.load(f)

        # Cache input shape
        self.input_shape = self.input_details[0]['shape'][1:3]

    def preprocess_image(self, image_path):
        """Optimized image preprocessing"""

        # Load image
        img = tf.io.read_file(image_path)
        img = tf.image.decode_jpeg(img, channels=3)

        # Resize
        img = tf.image.resize(img, self.input_shape)

        # Normalize
        img = (img / 127.5) - 1.0

        # Add batch dimension
        img = tf.expand_dims(img, 0)

        return img

    def predict(self, image_path, top_k=3):
        """Run optimized inference"""

        # Preprocess
        input_data = self.preprocess_image(image_path)

        # Set input tensor
        self.interpreter.set_tensor(
            self.input_details[0]['index'],
            input_data
        )

        # Run inference
        self.interpreter.invoke()

        # Get output
        output_data = self.interpreter.get_tensor(
            self.output_details[0]['index']
        )[0]

        # Get top k predictions
        top_indices = np.argsort(output_data)[-top_k:][::-1]

        results = []
        for idx in top_indices:
            results.append({
                'label': self.labels[idx],
                'confidence': float(output_data[idx])
            })

        return results

# Usage
inference = OptimizedInference(
    "plant_disease_model.tflite",
    "class_names.json"
)

predictions = inference.predict("test_image.jpg")
for pred in predictions:
    print(f"{pred['label']}: {pred['confidence']:.2%}")
```

**Verification**:
- Inference runs in <300ms on CPU
- Results match expected predictions
- Multi-threading improves performance

**Exercise 5.2: Batch Inference**

```python
def batch_inference(model_path, image_paths, batch_size=8):
    """Process multiple images efficiently"""

    interpreter = tf.lite.Interpreter(model_path=model_path)
    interpreter.allocate_tensors()

    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()

    all_results = []

    for i in range(0, len(image_paths), batch_size):
        batch_paths = image_paths[i:i+batch_size]
        batch_results = []

        for path in batch_paths:
            # Process image
            img = preprocess_image(path)

            interpreter.set_tensor(input_details[0]['index'], img)
            interpreter.invoke()

            output = interpreter.get_tensor(output_details[0]['index'])[0]
            batch_results.append(output)

        all_results.extend(batch_results)

    return all_results
```

**Verification**:
- Batch processing is faster than sequential
- All images are processed correctly
- Memory usage is acceptable

## How to Complete Exercises

1. **Setup environment**: Install TensorFlow, keras, scikit-learn
2. **Prepare dataset**: Download PlantVillage or similar dataset
3. **Work sequentially**: Complete exercises in order
4. **Validate results**: Check model performance meets targets
5. **Optimize**: Apply quantization and optimization techniques
6. **Document**: Record model architecture, hyperparameters, results

## Validation Checklist

- [ ] Model trains successfully
- [ ] Validation accuracy > 85%
- [ ] TFLite model size < 10MB
- [ ] Inference time < 500ms on CPU
- [ ] Quantized model maintains accuracy (< 2% drop)
- [ ] Model works on Android device
- [ ] Predictions are reasonable

## Common Issues and Solutions

**Issue**: Model overfitting
**Solution**: Add more augmentation, increase dropout, use regularization

**Issue**: Low accuracy
**Solution**: Train longer, adjust learning rate, try different architecture

**Issue**: Large model size
**Solution**: Use quantization, prune model, use smaller base architecture

**Issue**: Slow inference
**Solution**: Use quantized model, optimize preprocessing, use GPU delegate

## Resources

- [TensorFlow Model Garden](https://github.com/tensorflow/models)
- [TFLite Guide](https://www.tensorflow.org/lite/guide)
- [PlantVillage Dataset](https://www.kaggle.com/datasets/emmarex/plantdisease)
- [Transfer Learning Tutorial](https://www.tensorflow.org/tutorials/images/transfer_learning)

## Submission Requirements

1. Trained Keras model (.h5)
2. Converted TFLite model (.tflite)
3. Class labels (JSON)
4. Training history plots
5. Evaluation metrics
6. Inference benchmark results
7. Complete reflection journal entry
