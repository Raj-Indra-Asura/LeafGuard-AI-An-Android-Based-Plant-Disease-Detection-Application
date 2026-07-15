# Model Acquisition

The model binary must be acquired and approved manually. Follow
[`../docs/production-end-to-end-setup.md`](../docs/production-end-to-end-setup.md) to:

1. review the pinned source and MIT license;
2. download `model_4_mobilenet_finetuned.keras`;
3. record provenance and SHA-256;
4. stage it at `backend-api/models/leafguard_model.keras`;
5. inspect and convert the exact file;
6. validate Keras/TFLite parity and real images.

Do not use a random-weight stub or silently substitute another model. The canonical
class order is [`labels-38.txt`](labels-38.txt).
