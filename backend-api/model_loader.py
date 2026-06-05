import logging
from pathlib import Path
from typing import List, Sequence, Tuple

import numpy as np

from config import MODEL_PATH, USE_MOCK

logger = logging.getLogger(__name__)

try:
    import tensorflow as tf
except Exception as exc:  # pragma: no cover - depends on environment
    tf = None
    logger.warning("TensorFlow import failed, mock predictor will be used: %s", exc)


class ModelPredictor:
    """Wrapper that exposes a single predict method for real or mock inference."""

    def __init__(self, class_names: Sequence[str], model=None, use_mock: bool = False):
        self.class_names: List[str] = list(class_names)
        self.model = model
        self.use_mock = use_mock or model is None
        self.model_loaded = model is not None

    def predict(self, image_batch: np.ndarray) -> Tuple[str, float]:
        if self.use_mock or self.model is None:
            return self._mock_predict(image_batch)

        predictions = self.model.predict(image_batch, verbose=0)
        predictions = np.asarray(predictions, dtype=np.float32)
        if predictions.ndim == 1:
            predictions = np.expand_dims(predictions, axis=0)

        scores = predictions[0]
        best_index = int(np.argmax(scores))
        confidence = float(scores[best_index])
        disease_name = self.class_names[best_index] if best_index < len(self.class_names) else f"Class {best_index}"
        return disease_name, confidence

    def _mock_predict(self, image_batch: np.ndarray) -> Tuple[str, float]:
        if not self.class_names:
            return "Unknown disease", 0.50

        mean_intensity = float(np.mean(image_batch))
        scaled_index = int(round(mean_intensity * (len(self.class_names) - 1)))
        best_index = max(0, min(len(self.class_names) - 1, scaled_index))
        confidence = round(0.70 + ((best_index % 3) * 0.08), 2)
        return self.class_names[best_index], min(confidence, 0.99)


def load_predictor(class_names: Sequence[str]) -> ModelPredictor:
    model_path = Path(MODEL_PATH)

    if USE_MOCK:
        logger.info("USE_MOCK enabled. Skipping model load and using mock predictor.")
        return ModelPredictor(class_names=class_names, use_mock=True)

    if tf is None:
        logger.warning("TensorFlow is unavailable. Falling back to mock predictor.")
        return ModelPredictor(class_names=class_names, use_mock=True)

    if not model_path.exists():
        logger.warning("Model file not found at %s. Falling back to mock predictor.", model_path)
        return ModelPredictor(class_names=class_names, use_mock=True)

    try:
        model = tf.keras.models.load_model(model_path)
        logger.info("Loaded Keras model from %s", model_path)
        return ModelPredictor(class_names=class_names, model=model)
    except Exception:
        logger.exception("Failed to load model from %s. Using mock predictor instead.", model_path)
        return ModelPredictor(class_names=class_names, use_mock=True)
