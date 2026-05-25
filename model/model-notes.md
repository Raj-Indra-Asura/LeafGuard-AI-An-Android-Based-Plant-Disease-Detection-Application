# LeafGuard AI Model Documentation

## Overview
This document provides comprehensive technical documentation for the LeafGuard AI plant disease detection model. Fill in each section as you develop and integrate the model into the Android application.

---

## Model Architecture

### Model Type
- **Framework**: (e.g., TensorFlow Lite, PyTorch Mobile, ONNX)
- **Base Architecture**: (e.g., MobileNetV2, EfficientNet, Custom CNN)
- **Model Version**:
- **Training Framework**: (e.g., TensorFlow, PyTorch, Keras)
- **Conversion Method**: (How was it converted for mobile?)

### Model File Details
- **File Name**:
- **File Size**:
- **File Format**: (e.g., .tflite, .onnx, .pt)
- **Location in Project**: `/app/src/main/assets/` or `/app/src/main/ml/`
- **Quantization**: (None, INT8, FP16, Dynamic Range)

---

## Preprocessing Requirements

### Input Specifications

#### Image Dimensions
- **Expected Input Shape**: `[batch_size, height, width, channels]`
  - Batch Size:
  - Height: (e.g., 224, 299, 512)
  - Width: (e.g., 224, 299, 512)
  - Channels: (e.g., 3 for RGB, 1 for grayscale)

#### Color Format
- **Color Space**: RGB / BGR / Grayscale
- **Channel Order**: (e.g., RGB, BGR, channels-first, channels-last)
- **Important Notes**:
  - Does the model expect RGB or BGR?
  - Android Camera2 API provides images in YUV format - conversion needed
  - Does PIL/OpenCV order match model expectation?

#### Normalization Method
Select and document the normalization used:

**Option 1: [0, 1] Normalization**
```
normalized_pixel = pixel_value / 255.0
```
- Mean: [0.0, 0.0, 0.0]
- Std: [1.0, 1.0, 1.0]

**Option 2: [-1, 1] Normalization**
```
normalized_pixel = (pixel_value / 127.5) - 1.0
```
- Mean: [0.0, 0.0, 0.0]
- Range: [-1.0, 1.0]

**Option 3: ImageNet Normalization**
```
normalized_pixel = (pixel_value / 255.0 - mean) / std
```
- Mean: [0.485, 0.456, 0.406]
- Std: [0.229, 0.224, 0.225]

**Option 4: Custom Normalization**
- Mean: [___, ___, ___]
- Std: [___, ___, ___]
- Formula:

**Your Model Uses**: (Specify which option and why)

#### Preprocessing Pipeline
Document the exact preprocessing steps in order:
1. **Resize**: Method (bilinear, bicubic, nearest), Aspect ratio handling
2. **Crop**: Center crop, Random crop, No crop
3. **Color Conversion**: YUV → RGB, BGR → RGB, etc.
4. **Normalization**: (As specified above)
5. **Tensor Conversion**: numpy array → tensor, data type (float32, uint8)
6. **Additional Steps**: (Any other transformations)

#### Android Implementation Code
```kotlin
// Preprocessing pseudocode for Android
// TODO: Implement in ImageProcessor.kt

fun preprocessImage(bitmap: Bitmap): FloatArray {
    // 1. Resize to [HEIGHT x WIDTH]

    // 2. Convert color format if needed

    // 3. Normalize pixel values

    // 4. Convert to input tensor format

    return processedArray
}
```

---

## Output Format

### Output Specifications

#### Output Shape
- **Shape**: `[batch_size, num_classes]` or other format
- **Batch Size**:
- **Number of Classes**:
- **Data Type**: (float32, float16, int8)

#### Output Activation
- **Activation Function**: Softmax / Sigmoid / None
- **Are logits returned?**: Yes / No
- **Post-processing needed?**: (Do you need to apply softmax yourself?)

#### Class Information
List all classes the model can predict:

| Index | Class Name | Disease Category | Plant Type |
|-------|-----------|------------------|------------|
| 0     | Healthy   | None             | General    |
| 1     |           |                  |            |
| 2     |           |                  |            |
| 3     |           |                  |            |
| ...   |           |                  |            |

**Total Classes**:

#### Confidence Calculation
```
If softmax applied:
    confidence = max(output_array) * 100
    predicted_class = argmax(output_array)

If logits returned:
    softmax_output = softmax(output_array)
    confidence = max(softmax_output) * 100
    predicted_class = argmax(softmax_output)
```

**Your Model**: (Describe exact calculation)

#### Threshold Settings
- **Minimum Confidence for Valid Prediction**: ___%
- **"Unknown" Threshold**: (Below what confidence is result unreliable?)
- **Multi-class Threshold**: (If supporting multiple diseases per leaf)

---

## Model Performance Metrics

### Accuracy Metrics

#### Overall Performance
- **Training Accuracy**: ___%
- **Validation Accuracy**: ___%
- **Test Accuracy**: ___%
- **F1-Score**:
- **Precision**:
- **Recall**:

#### Per-Class Performance
Create a table for each class:

| Class Name | Precision | Recall | F1-Score | Support |
|-----------|-----------|--------|----------|---------|
| Class 0   |           |        |          |         |
| Class 1   |           |        |          |         |
| Class 2   |           |        |          |         |
| ...       |           |        |          |         |

#### Confusion Matrix Template
```
              Predicted
              Class 0  Class 1  Class 2  ...
Actual Class 0 [  TN  ] [  FP  ] [  FP  ]
       Class 1 [  FN  ] [  TP  ] [  FP  ]
       Class 2 [  FN  ] [  FN  ] [  TP  ]
       ...
```

**Add your confusion matrix data here** (Can be image or table)

### Inference Performance

#### Cloud/Server Inference
- **Average Inference Time**: ___ ms
- **Hardware Used**: (GPU type, CPU cores)
- **Batch Size Tested**:
- **Memory Usage**: ___ MB

#### Mobile/Offline Inference (Android)

**Test Device 1**: (e.g., Pixel 6, Android 13)
- **Inference Time**: ___ ms (average over ___ runs)
- **CPU Usage**:
- **Memory Usage**: ___ MB
- **Battery Impact**: (If measured)

**Test Device 2**: (e.g., Samsung Galaxy S21)
- **Inference Time**: ___ ms
- **CPU Usage**:
- **Memory Usage**: ___ MB
- **Battery Impact**:

**Minimum Device Requirements**:
- Android Version:
- RAM:
- Processor:
- Expected Inference Time: ___ ms

---

## Model Limitations

### Supported Classes
List what the model CAN detect:
-
-
-

### Unsupported Scenarios
List what the model CANNOT detect:
- Plant types not in training data
- Diseases not in training data
- Multiple diseases on same leaf (if not trained for this)
-

### Known Edge Cases

#### When Model Fails or Performs Poorly
Document specific failure scenarios:

1. **Poor Lighting Conditions**
   - Very dark images (underexposed)
   - Overexposed images (washed out)
   - Strong shadows
   - **Mitigation**:

2. **Image Quality Issues**
   - Blurry images
   - Low resolution (below ___ x ___ pixels)
   - Heavy compression artifacts
   - **Mitigation**:

3. **Leaf Positioning**
   - Leaf too small in frame (less than ___% of image)
   - Multiple leaves overlapping
   - Leaf partially visible
   - **Mitigation**:

4. **Background Interference**
   - Complex backgrounds
   - Other plants in view
   - Soil or ground visible
   - **Mitigation**:

5. **Disease Stage**
   - Very early stage (symptoms not visible)
   - Very late stage (leaf mostly dead)
   - **Mitigation**:

6. **Image Artifacts**
   - Watermarks or text on image
   - Filters applied to image
   - Camera lens issues
   - **Mitigation**:

### Dataset Limitations
- **Training Dataset Size**: ___ images
- **Images per Class**: (Min: ___, Max: ___, Average: ___)
- **Dataset Bias**: (Geographic regions, specific plant varieties, etc.)
- **Data Collection Method**: (Lab conditions, field conditions, web-scraped)

### Accuracy Expectations by Scenario

| Scenario | Expected Accuracy | Notes |
|----------|------------------|-------|
| Ideal conditions (good lighting, clear leaf, centered) | ___% | |
| Outdoor natural lighting | ___% | |
| Indoor artificial lighting | ___% | |
| Slight blur or motion | ___% | |
| Partially occluded leaf | ___% | |
| Multiple diseases present | ___% | |

---

## Integration Guidelines

### Android Integration Checklist
- [ ] Model file added to `assets/` or `ml/` folder
- [ ] Input preprocessing implemented correctly
- [ ] Output post-processing implemented
- [ ] Confidence thresholding applied
- [ ] Error handling for invalid inputs
- [ ] UI feedback for low confidence predictions
- [ ] Performance testing on target devices
- [ ] Memory leak testing
- [ ] Offline functionality verified

### Code Integration Points

#### Week 4-5: Model Loading
```kotlin
// ModelLoader.kt
// TODO: Implement model loading logic
```

#### Week 6: Preprocessing
```kotlin
// ImageProcessor.kt
// TODO: Implement preprocessing pipeline
```

#### Week 7: Inference
```kotlin
// DiseaseClassifier.kt
// TODO: Implement inference and post-processing
```

### Testing Strategy
1. **Unit Tests**: Test preprocessing functions with known inputs
2. **Integration Tests**: Test full pipeline with sample images
3. **Accuracy Tests**: Validate against test dataset
4. **Performance Tests**: Measure inference time on target devices
5. **Edge Case Tests**: Test failure scenarios documented above

---

## Maintenance and Updates

### Version History
| Version | Date | Changes | Accuracy | Notes |
|---------|------|---------|----------|-------|
| 1.0     |      |         |          |       |
| 1.1     |      |         |          |       |

### Model Update Checklist
When updating the model:
- [ ] Test new model with existing test images
- [ ] Compare accuracy with previous version
- [ ] Verify preprocessing requirements haven't changed
- [ ] Update this documentation
- [ ] Update class labels if changed
- [ ] Re-test on all target devices
- [ ] Update version number in code

### Known Issues
Document any current issues or bugs:
1.
2.
3.

### Future Improvements
List planned enhancements:
1.
2.
3.

---

## References

### Training Resources
- **Training Code Repository**:
- **Dataset Source**:
- **Research Papers**:
- **Model Architecture Reference**:

### Useful Links
- TensorFlow Lite Documentation: https://www.tensorflow.org/lite
- Android ML Kit: https://developers.google.com/ml-kit
- Model Conversion Tools:

---

## Contact and Support

### Model Information
- **Trained By**:
- **Date Trained**:
- **Training Platform**:

### Questions or Issues
If you encounter issues with the model:
1. Check this documentation first
2. Review preprocessing code
3. Verify input/output formats
4. Test with sample images from training set
5. Open an issue using the bug report template

---

**Last Updated**: [Date]
**Maintained By**: [Your Name]
**CSE 2206 Course Project**: LeafGuard AI
