# Week 03: Image Input With Camera and Gallery

## Week 03 Mindset

Week 02 gave you a runnable Android navigation shell. The Scan screen was only a placeholder. Week 03 turns that placeholder into the first real product feature:

> The user can provide a plant-leaf image by taking a photo or choosing one from the device gallery, and the app shows that image on the Scan screen.

This week still does **not** analyze the image. It does **not** call the backend. It does **not** save history. It does **not** run AI.

Week 03 answers one question only:

> Can the app safely receive an image from the user and preview it?

---

## Product State After Week 03

**Cumulative product contribution: 25%**

By the end of Week 03, the app should:

- open the Scan screen from Home
- request camera permission when needed
- launch the camera app and receive a captured image
- open the gallery picker and receive a selected image
- display the selected/captured image in the Scan screen preview
- handle permission denial and cancellation without crashing
- remember the selected image after simple screen recreation when possible

### What the product can do after Week 03

- Accept real user image input.
- Show the chosen image on screen.
- Prove the first user-content feature works.

### What the product still cannot do

- Upload the image to a backend. That starts in Week 05 after the Week 04 backend exists.
- Predict disease. That starts with cloud prediction in Week 06 and offline prediction in Week 09.
- Save scan history. That starts in Week 07.
- Show disease guidance from XML. That starts in Week 08.

---

## New Words This Week

| Term | Beginner Definition |
|---|---|
| Camera permission | User approval that lets the app ask the device camera to take a photo. |
| Runtime permission | A permission requested while the app is running, not only when installed. |
| Gallery picker | A system screen that lets the user choose an existing image. |
| URI | A reference to content on Android, such as an image selected by the user. |
| FileProvider | Android helper that safely shares an app-owned file with another app, such as the camera. |
| Activity Result API | Modern Android system for launching another app/screen and receiving a result. |
| Preview | Showing the selected or captured image inside the app UI. |
| Cancellation | User backs out of camera/gallery; the app should keep working. |

---

## Weekly Objective

By the end of Week 03, you will be able to:

1. Explain why camera access needs permission.
2. Add the camera permission and FileProvider safely.
3. Use `ActivityResultContracts.TakePicture` for camera capture.
4. Use `ActivityResultContracts.GetContent` for gallery image selection.
5. Store the selected image URI in `ScanActivity`.
6. Display that URI in an `ImageView` preview.
7. Validate only image input behavior.

---

## What You Will Build

You will upgrade `ScanActivity` from a placeholder into an image input screen.

| Week 02 Screen | Week 03 Upgrade | Future Boundary |
|---|---|---|
| `ScanActivity` placeholder | Camera button, gallery button, image preview | Detection waits for later weeks |
| `MainActivity` Home | Still navigates to Scan | No camera logic here |
| `ResultActivity` placeholder | No real change required | Real result waits for networking/model work |

Keep camera/gallery logic in `ScanActivity`, because Week 02 made Scan the screen where image input belongs.

---

## Kotlin First

Use the Kotlin primary track:

```text
android-app-kotlin/
```

The Java app remains a secondary comparison track.

---

## Suggested 7-Day Plan

| Day | Focus | Output |
|---|---|---|
| Day 1 | Understand permissions and URI idea | Notes and exercise 1 |
| Day 2 | Add manifest permission and FileProvider config | Project still builds |
| Day 3 | Build Scan layout with preview and buttons | Scan UI visible |
| Day 4 | Add gallery picker | Gallery image previews |
| Day 5 | Add camera capture | Captured image previews |
| Day 6 | Handle denial/cancel/state | No crash on edge cases |
| Day 7 | Validate and collect evidence | Demo, quiz, reflection |

---

## Milestone Demo

At the end of Week 03, demonstrate this:

1. Launch the app.
2. Open Scan from Home.
3. Tap gallery, choose an image, and show it in the preview.
4. Tap camera, take a photo, and show it in the preview.
5. Deny camera permission or cancel camera/gallery and show the app does not crash.
6. Explain that detection/upload/history are future weeks.

This proves the Week 03 image-input slice is complete.

---

## Week 03 File Order

Complete these files in this order:

| Step | File | Purpose |
|---:|---|---|
| 1 | `README.md` | Understand the Week 03 product slice. |
| 2 | `learning-notes.md` | Learn image input concepts from zero. |
| 3 | `exercises.md` | Practise permission, URI, and preview ideas. |
| 4 | `build-task.md` | Build image input in `ScanActivity`. |
| 5 | `validation-checklist.md` | Prove camera/gallery input works. |
| 6 | `quiz.md` | Check understanding. |
| 7 | `reflection.md` | Explain what you built and why. |

Move to Week 04 only after the image-input demo passes.