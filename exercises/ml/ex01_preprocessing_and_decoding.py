#!/usr/bin/env python3
"""
ML Exercises — Model Preprocessing and Evaluation
Week 6 & 9 — ML Model Integration and TFLite

Run individual exercises:
  python ex01_preprocessing.py

Prerequisites: pip install Pillow numpy
Optional:      pip install tensorflow matplotlib scikit-learn
"""

import numpy as np
from PIL import Image
import io
import time


# ══════════════════════════════════════════════════════════════
# EXERCISE 1: Image Preprocessing Pipeline
# ══════════════════════════════════════════════════════════════

def exercise_1_preprocess_image(image_bytes: bytes, target_size=(224, 224)) -> np.ndarray:
    """
    Exercise 1: Complete the image preprocessing pipeline.

    Requirements:
    - Convert to RGB (handles RGBA, grayscale)
    - Resize to target_size using LANCZOS resampling
    - Normalise to [0.0, 1.0] range
    - Add batch dimension → shape (1, H, W, 3)
    - Return float32 array

    Test cases to pass:
    - Input: Any PIL Image → Output: (1, 224, 224, 3) float32 in [0, 1]
    - Input: RGBA image → still produces (1, 224, 224, 3)
    - Input: Grayscale image → still produces (1, 224, 224, 3)
    """
    # TODO: Implement this function
    # Step 1: image.convert("RGB")
    # Step 2: image.resize(target_size, Image.LANCZOS)
    # Step 3: np.array(image, dtype=np.float32)
    # Step 4: img_array /= 255.0
    # Step 5: np.expand_dims(img_array, axis=0)
    raise NotImplementedError("Implement exercise_1_preprocess_image()")


def test_exercise_1():
    """Run automated tests for exercise 1."""
    print("Exercise 1: Image Preprocessing")

    # Test 1: Basic RGB image
    rgb_img = Image.new("RGB", (512, 400), color=(100, 150, 80))
    buffer = io.BytesIO()
    rgb_img.save(buffer, format="JPEG")
    result = exercise_1_preprocess_image(buffer.getvalue())

    assert result.shape == (1, 224, 224, 3), f"Expected (1,224,224,3), got {result.shape}"
    assert result.dtype == np.float32, f"Expected float32, got {result.dtype}"
    assert result.min() >= 0.0 and result.max() <= 1.0, "Values should be in [0,1]"
    print("  ✅ Test 1 passed: RGB image preprocessing")

    # Test 2: RGBA image (common from Android screenshots)
    rgba_img = Image.new("RGBA", (300, 300), color=(100, 150, 80, 200))
    buffer = io.BytesIO()
    rgba_img.save(buffer, format="PNG")
    result = exercise_1_preprocess_image(buffer.getvalue())
    assert result.shape == (1, 224, 224, 3), f"RGBA should produce (1,224,224,3), got {result.shape}"
    print("  ✅ Test 2 passed: RGBA image converted to RGB")

    # Test 3: Non-square image
    wide_img = Image.new("RGB", (800, 300), color=(200, 100, 50))
    buffer = io.BytesIO()
    wide_img.save(buffer, format="JPEG")
    result = exercise_1_preprocess_image(buffer.getvalue())
    assert result.shape == (1, 224, 224, 3), f"Non-square should resize to 224x224"
    print("  ✅ Test 3 passed: Non-square image resized to 224×224")

    print("  All Exercise 1 tests passed!\n")


# ══════════════════════════════════════════════════════════════
# EXERCISE 2: Decode Model Output
# ══════════════════════════════════════════════════════════════

CLASS_NAMES = [
    "Apple___Apple_scab", "Apple___Black_rot", "Apple___Cedar_apple_rust", "Apple___healthy",
    "Blueberry___healthy", "Cherry_(including_sour)___Powdery_mildew", "Cherry_(including_sour)___healthy",
    "Corn_(maize)___Cercospora_leaf_spot Gray_leaf_spot", "Corn_(maize)___Common_rust_",
    "Corn_(maize)___Northern_Leaf_Blight", "Corn_(maize)___healthy", "Grape___Black_rot",
    "Grape___Esca_(Black_Measles)", "Grape___Leaf_blight_(Isariopsis_Leaf_Spot)", "Grape___healthy",
    "Orange___Haunglongbing_(Citrus_greening)", "Peach___Bacterial_spot", "Peach___healthy",
    "Pepper,_bell___Bacterial_spot", "Pepper,_bell___healthy", "Potato___Early_blight",
    "Potato___Late_blight", "Potato___healthy", "Raspberry___healthy", "Soybean___healthy",
    "Squash___Powdery_mildew", "Strawberry___Leaf_scorch", "Strawberry___healthy",
    "Tomato___Bacterial_spot", "Tomato___Early_blight", "Tomato___Late_blight",
    "Tomato___Leaf_Mold", "Tomato___Septoria_leaf_spot",
    "Tomato___Spider_mites Two-spotted_spider_mite", "Tomato___Target_Spot",
    "Tomato___Tomato_Yellow_Leaf_Curl_Virus", "Tomato___Tomato_mosaic_virus", "Tomato___healthy"
]


def exercise_2_decode_prediction(probabilities: np.ndarray, class_names: list, threshold=0.5) -> dict:
    """
    Exercise 2: Decode model output probabilities into a structured result.

    Args:
        probabilities: 1D array of shape (num_classes,) with values summing to 1.0
        class_names:   List of class name strings
        threshold:     Confidence threshold. Below this → mark as uncertain.

    Returns dict with:
        - "class_name": str — full class name
        - "display_name": str — human-readable (replace ___ with ": ", _ with " ")
        - "confidence": float — 0.0 to 1.0
        - "is_healthy": bool — True if "healthy" in class_name
        - "is_uncertain": bool — True if confidence < threshold
        - "plant_type": str — part before "___"
        - "disease": str — part after "___" (or "Healthy")
    """
    # TODO: Implement this function
    raise NotImplementedError("Implement exercise_2_decode_prediction()")


def test_exercise_2():
    """Run automated tests for exercise 2."""
    print("Exercise 2: Decode Model Output")

    # Test 1: Confident disease prediction (Tomato Early Blight = index 29)
    probs = np.zeros(38)
    probs[29] = 0.91
    probs[30] = 0.05
    probs[28] = 0.04

    result = exercise_2_decode_prediction(probs, CLASS_NAMES)
    assert result["class_name"] == "Tomato___Early_blight", f"Wrong class: {result['class_name']}"
    assert abs(result["confidence"] - 0.91) < 0.001, "Wrong confidence"
    assert result["is_healthy"] == False, "Early blight is not healthy"
    assert result["is_uncertain"] == False, "0.91 confidence is not uncertain"
    assert result["plant_type"] == "Tomato", f"Wrong plant type: {result['plant_type']}"
    print("  ✅ Test 1 passed: Disease prediction decoded correctly")

    # Test 2: Healthy prediction (Tomato healthy = index 37)
    probs_healthy = np.zeros(38)
    probs_healthy[37] = 0.95
    result_healthy = exercise_2_decode_prediction(probs_healthy, CLASS_NAMES)
    assert result_healthy["is_healthy"] == True, "Should be healthy"
    print("  ✅ Test 2 passed: Healthy prediction detected correctly")

    # Test 3: Uncertain prediction (confidence < threshold)
    probs_uncertain = np.ones(38) / 38  # Equal probabilities (1/38 each)
    result_uncertain = exercise_2_decode_prediction(probs_uncertain, CLASS_NAMES, threshold=0.5)
    assert result_uncertain["is_uncertain"] == True, "Should be uncertain (1/38 < 0.5)"
    print("  ✅ Test 3 passed: Uncertain prediction flagged correctly")

    print("  All Exercise 2 tests passed!\n")


# ══════════════════════════════════════════════════════════════
# EXERCISE 3: Data Augmentation Simulation
# ══════════════════════════════════════════════════════════════

def exercise_3_augment_image(image: Image.Image, flip=True, rotate_degrees=0,
                               brightness_factor=1.0) -> Image.Image:
    """
    Exercise 3: Apply augmentation transformations to a PIL Image.

    Args:
        image:             Input PIL Image
        flip:              If True, flip image horizontally
        rotate_degrees:    Rotation angle in degrees (0 = no rotation)
        brightness_factor: 1.0 = no change, 0.5 = darker, 1.5 = brighter

    Returns: Transformed PIL Image

    Hint: Use PIL.Image methods and PIL.ImageEnhance
    - image.transpose(Image.FLIP_LEFT_RIGHT) for horizontal flip
    - image.rotate(degrees, expand=False) for rotation
    - ImageEnhance.Brightness(image).enhance(factor) for brightness
    """
    # TODO: Implement augmentation
    # from PIL import ImageEnhance
    raise NotImplementedError("Implement exercise_3_augment_image()")


def test_exercise_3():
    """Run automated tests for exercise 3."""
    print("Exercise 3: Data Augmentation")
    from PIL import ImageStat

    original = Image.new("RGB", (224, 224), color=(100, 150, 80))
    # Add a visible asymmetry to test flip
    pixels = np.array(original)
    pixels[:, :50, :] = [200, 50, 50]  # Red stripe on left side
    original = Image.fromarray(pixels.astype(np.uint8))

    # Test flip
    flipped = exercise_3_augment_image(original, flip=True)
    orig_arr = np.array(original)
    flip_arr = np.array(flipped)
    # Left column of original should match right column of flipped
    assert not np.array_equal(orig_arr, flip_arr), "Flipped image should differ from original"
    print("  ✅ Test 1 passed: Horizontal flip works")

    # Test brightness increase
    bright = exercise_3_augment_image(original, flip=False, brightness_factor=1.5)
    bright_arr = np.array(bright)
    orig_mean = np.array(original).mean()
    bright_mean = bright_arr.mean()
    assert bright_mean > orig_mean, f"Brighter image should have higher mean: {orig_mean:.1f} vs {bright_mean:.1f}"
    print("  ✅ Test 2 passed: Brightness increase works")

    print("  All Exercise 3 tests passed!\n")


# ══════════════════════════════════════════════════════════════
# MAIN: Run all exercises
# ══════════════════════════════════════════════════════════════

if __name__ == "__main__":
    print("=" * 60)
    print("  LeafGuard AI — ML Exercises")
    print("  Implement each function above, then run to verify.")
    print("=" * 60)
    print()

    exercises = [
        ("Exercise 1: Image Preprocessing", test_exercise_1),
        ("Exercise 2: Decode Model Output", test_exercise_2),
        ("Exercise 3: Data Augmentation", test_exercise_3),
    ]

    passed = 0
    failed = 0

    for name, test_fn in exercises:
        try:
            test_fn()
            passed += 1
        except NotImplementedError as e:
            print(f"  ⏳ {name}: Not yet implemented")
            failed += 1
        except AssertionError as e:
            print(f"  ❌ {name}: FAILED — {e}")
            failed += 1
        except Exception as e:
            print(f"  ❌ {name}: ERROR — {type(e).__name__}: {e}")
            failed += 1

    print()
    print(f"Results: {passed} passed, {failed} not yet complete")

    if failed == 0:
        print("🎉 All exercises complete! You're ready for Week 9 (TFLite).")
    else:
        print(f"Complete the {failed} remaining exercise(s) above.")
