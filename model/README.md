# LeafGuard Model Integration

LeafGuard uses one manually approved 38-class Keras model for online inference and a
TensorFlow Lite conversion of that exact artifact for offline Android inference.

- Contract and provenance notes: [`model-notes.md`](model-notes.md)
- Canonical ordered labels: [`labels-38.txt`](labels-38.txt)
- Complete setup and acceptance guide:
  [`../docs/production-end-to-end-setup.md`](../docs/production-end-to-end-setup.md)
- Convert model: `python model/convert_model.py`
- Inspect tensors: `python model/inspect_model.py`
- Validate TFLite: `python model/validate_tflite.py IMAGE --model MODEL`
- Compare Keras/TFLite: `python model/parity_test.py IMAGES --tflite-model MODEL`

The previous random-weight stub generator was removed so a release cannot accidentally
present meaningless output as disease inference.
