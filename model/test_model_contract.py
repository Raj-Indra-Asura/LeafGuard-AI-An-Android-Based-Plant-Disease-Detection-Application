import unittest
import xml.etree.ElementTree as ET
from pathlib import Path

from model_contract import ANDROID_ASSETS, BACKEND_LABELS, DEFAULT_LABELS, load_labels


class ModelContractTest(unittest.TestCase):
    def test_canonical_labels_are_unique_and_synchronized(self):
        canonical = load_labels(DEFAULT_LABELS)
        self.assertEqual(canonical, load_labels(BACKEND_LABELS))
        for assets_dir in ANDROID_ASSETS:
            self.assertEqual(canonical, load_labels(assets_dir / "labels.txt"))

    def test_reviewed_guidance_is_limited_to_known_labels(self):
        labels = set(load_labels(DEFAULT_LABELS))
        expected_display_names = {
            "Apple Scab",
            "Corn Gray Leaf Spot",
            "Corn Northern Leaf Blight",
            "Corn Healthy",
            "Potato Early Blight",
            "Potato Late Blight",
            "Potato Healthy",
            "Tomato Early Blight",
            "Tomato Late Blight",
            "Tomato Healthy",
        }
        for assets_dir in ANDROID_ASSETS:
            names = {
                element.text
                for element in ET.parse(assets_dir / "diseases.xml").findall("./disease/name")
            }
            self.assertEqual(expected_display_names, names)
        self.assertTrue(
            {
                "Apple___Apple_scab",
                "Corn___Cercospora_leaf_spot Gray_leaf_spot",
                "Corn___Northern_Leaf_Blight",
                "Corn___Healthy",
                "Potato___Early_blight",
                "Potato___Late_blight",
                "Potato___Healthy",
                "Tomato___Early_blight",
                "Tomato___Late_blight",
                "Tomato___Healthy",
            }.issubset(labels)
        )


if __name__ == "__main__":
    unittest.main()
