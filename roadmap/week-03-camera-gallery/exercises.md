# Week 03 Exercises: Camera, Gallery, URI, and Preview

## How to Use These Exercises

Complete these before the build task. They assume only Week 01 product planning and Week 02 Android navigation-shell knowledge.

Save outputs in:

```text
docs/evidence/week-03/exercises/
```

---

## Exercise 1: Trace the Image Input Flow

### Goal

Understand what happens before writing code.

### Task

Write two flows:

```text
Camera flow:
1. User opens Scan screen.
2. User taps Take Photo.
3. ...

Gallery flow:
1. User opens Scan screen.
2. User taps Choose from Gallery.
3. ...
```

End each flow with the image displayed in the preview.

### Output

File: `docs/evidence/week-03/exercises/exercise-01-image-flow.md`

### Validation

- [ ] Camera flow includes permission check.
- [ ] Camera flow includes FileProvider URI.
- [ ] Gallery flow includes selected URI.
- [ ] Neither flow includes prediction or saving history.

---

## Exercise 2: Permission Explanation

### Goal

Explain camera permission in beginner language.

### Task

Answer:

1. Why does Android ask for camera permission?
2. What should the app do if permission is granted?
3. What should the app do if permission is denied?
4. Why should denial not crash the app?

### Output

File: `docs/evidence/week-03/exercises/exercise-02-permission-explanation.md`

---

## Exercise 3: FileProvider Matching Check

### Goal

Understand that FileProvider authority must match in manifest and code.

### Task

Write this pair and explain why they must match.

Manifest authority:

```xml
android:authorities="${applicationId}.fileprovider"
```

Kotlin authority:

```kotlin
"${BuildConfig.APPLICATION_ID}.fileprovider"
```

### Output

File: `docs/evidence/week-03/exercises/exercise-03-fileprovider-check.md`

### Validation

- [ ] You can explain `applicationId`.
- [ ] You can explain `.fileprovider`.
- [ ] You can explain why raw `file://` paths are avoided.

---

## Exercise 4: URI State Plan

### Goal

Plan how the selected image URI is stored and restored.

### Task

Write a small plan:

```text
Variable name:
When it becomes non-null:
How it updates the ImageView:
How it is saved during screen recreation:
How it is restored:
```

### Output

File: `docs/evidence/week-03/exercises/exercise-04-uri-state-plan.md`

---

## Exercise 5: Preview UI Sketch

### Goal

Design the Scan screen upgrade from Week 02.

### Task

Sketch or list the Scan screen UI:

- title
- ImageView preview area
- image status text
- Take Photo button
- Choose from Gallery button
- note saying detection is a future week

### Output

File: `docs/evidence/week-03/exercises/exercise-05-preview-ui.md` or `.png`

### Validation

- [ ] UI has a clear preview area.
- [ ] UI has both image input actions.
- [ ] UI does not include a working Detect button yet unless it is disabled or clearly future-labeled.

---

## Exercise 6: Edge Case Checklist

### Goal

Prepare for non-happy paths.

### Task

Create a checklist for these cases:

- user denies camera permission
- user cancels camera
- user cancels gallery picker
- camera file URI creation fails
- selected image cannot display
- screen rotates after image selected

For each, write the expected safe behavior.

### Output

File: `docs/evidence/week-03/exercises/exercise-06-edge-cases.md`

---

## Completion Rule

Start the build task only after you can explain camera flow, gallery flow, URI, FileProvider, and preview without mentioning backend, database, or AI.