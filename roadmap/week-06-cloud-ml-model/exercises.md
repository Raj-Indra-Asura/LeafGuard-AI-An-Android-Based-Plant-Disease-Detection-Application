# Week 06 Exercises: Real Cloud Model Contracts

## How to Use These Exercises

Complete these before the build task. Save exactly six files under:

```text
docs/evidence/week-06/exercises/
```

| Exercise | Output |
|---:|---|
| 1 | `exercise-01-artifact-identity.md` |
| 2 | `exercise-02-tensor-contract.md` |
| 3 | `exercise-03-label-order.md` |
| 4 | `exercise-04-preprocessing-proof.md` |
| 5 | `exercise-05-real-mode-flow.md` |
| 6 | `exercise-06-limitations.md` |

Use observed repository facts and your own explanations.

---

## Exercise 1: Build the Artifact Identity Record

### Goal

Prove which binary is approved before loading it.

### Task

Complete:

```text
Source repository:
Pinned commit:
Source path:
Local path:
License claim:
Exact byte size:
SHA-256:
Why Git ignores it:
```

Run `stat`, `sha256sum`, and `git ls-files`. Record expected and actual output.

Explain why filename equality is weaker than SHA-256 equality.

### Validation

- [ ] Commit contains 40 hexadecimal characters.
- [ ] SHA-256 contains 64 hexadecimal characters.
- [ ] Size is 25,143,175 bytes.
- [ ] Binary is not tracked.
- [ ] License review is mentioned.

---

## Exercise 2: Draw the Tensor Contract

### Goal

Explain the model as a software interface.

### Task

Create this table:

| Boundary | Shape | Dtype | Meaning |
|---|---|---|---|
| Uploaded decoded image | | | |
| Batched Keras input | | | |
| Keras output | | | |

Label each input dimension in `(1, 224, 224, 3)`. Explain why an output count other than 38 is incompatible.

### Validation

- [ ] Batch, height, width, and RGB channel are identified.
- [ ] Input and output are `float32`.
- [ ] Output count equals label count.

---

## Exercise 3: Verify Label Order

### Goal

Understand that label order is part of the model.

### Task

1. Compare `model/labels-38.txt` with `backend-api/labels-38.txt`.
2. Record the number of non-empty labels.
3. Record index 0, index 10, index 29, and index 37.
4. Simulate scores where index 29 is largest.
5. Decode the label using the file order.

Explain what would happen if the file were sorted alphabetically.

### Validation

- [ ] Both files match exactly.
- [ ] There are 38 unique labels.
- [ ] Indexing starts at zero.
- [ ] No label is renamed or sorted.

---

## Exercise 4: Prove Preprocessing

### Goal

Prevent silent double normalization.

### Task

Complete:

| Raw caller value | Embedded output |
|---:|---:|
| 0 | |
| 127.5 | |
| 255 | |

Use the equation:

$$
y = \frac{x}{127.5} - 1
$$

Explain why caller-side `/255.0` is wrong for this artifact. Then inspect the model and record the detected rescaling operation.

### Validation

- [ ] Caller range is `[0,255]`.
- [ ] Embedded output range is `[-1,1]`.
- [ ] RGB resize and batch dimension are included.
- [ ] No generic normalization advice overrides model evidence.

---

## Exercise 5: Trace Real Mode

### Goal

Distinguish configuration from proof.

### Task

Draw:

```text
USE_MOCK=false
  -> TensorFlow import
  -> model path
  -> Keras load
  -> shape validation
  -> predictor.model_loaded
  -> /health
  -> /predict
```

Record the expected `/health` values and all eight response keys. Predict results for:

- valid model
- missing model
- incompatible output count
- `USE_MOCK=true`

### Validation

- [ ] Real mode requires `use_mock=false` and `model_loaded=true`.
- [ ] Missing/incompatible model does not produce fake real success.
- [ ] Week 05 JSON remains unchanged.

---

## Exercise 6: Write an Honest Limitations Note

### Goal

Separate model execution, source metrics, and product accuracy.

### Task

Write three columns:

| Proven this week | Not proven | Future evidence needed |
|---|---|---|
| | | |

Address:

- source-author 98.75% score
- one successful prediction
- PlantVillage dataset conditions
- phone-camera backgrounds and lighting
- confidence versus correctness
- 38 classes versus all plant diseases
- professional verification

### Validation

- [ ] No unsupported accuracy claim appears.
- [ ] Real inference is still described as real model execution.
- [ ] Confidence is not called probability of truth.
- [ ] User-facing caution is included.

---

## Completion Rule

Start the build task only when all six files exist and you can state:

- exact artifact size and SHA-256
- exact Keras input/output contract
- why labels must not be sorted
- why pixels stay raw `[0,255]`
- how `/health` proves real mode
- why Android remains unchanged
- what real inference proves and does not prove

<!-- NAV_FOOTER_START -->

---

## Week 06 Navigation

[README](README.md) | [Learning Notes](learning-notes.md) | **Exercises - current** | [Build Task](build-task.md) | [Validation](validation-checklist.md) | [Quiz](quiz.md) | [Reflection](reflection.md)

[Previous: Learning Notes](learning-notes.md) | [Learning Path](../../LEARNING_PATH.md) | [Next: Build Task](build-task.md)
