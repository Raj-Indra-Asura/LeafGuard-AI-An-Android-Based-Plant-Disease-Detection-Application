# Week 03 Interactive Notebook

## Connecting LeafGuard to Camera and Gallery

> This README acts like a Markdown notebook for CSE 2206. Read one cell at a time, run the code, and write your own notes after each checkpoint.

### How to use this notebook

- Follow the cells in order.
- Run code blocks in Android Studio, Terminal, or a Python shell as indicated.
- Keep LeafGuard AI open in Android Studio while you work.
- Save screenshots for your evidence folder after each big milestone.
- Kotlin is the primary track for Android code in this repository; a complete Java twin lives in `android-app/`. Pick one track and stay consistent.

### Weekly outcomes

- Understand runtime permission flow for camera and media access.
- Implement camera capture, gallery pick, and image preview in Java.
- Test with emulator image injection and debug common failures.

### Repository references

- `android-app/app/src/main/AndroidManifest.xml`
- `roadmap/week-03-camera-gallery/`
- `solutions/week-03/`

---

## Notebook Cell 1 — Understand the permission flow

### Explanation

- Modern Android requests dangerous permissions at runtime, not only in the manifest.
- LeafGuard AI needs camera access and image reading access when the user actually scans a leaf.

### Code to Read / Run

```text
Permission flow
---------------
User taps Camera
    |
    v
Check CAMERA permission
    |
    +-- granted --> launch camera intent
    |
    +-- denied --> request permission dialog
                       |
                       +-- allow --> launch camera intent
                       |
                       +-- deny --> show message and stay on screen
```

### 🔵 Try This

- Explain why asking for permission on app launch is worse than asking at the moment of need.

### Expected Output

- You understand the difference between manifest declaration and runtime approval.

### ✅ Checkpoint

- Which permission is needed for camera capture?

### ⚠️ Common Mistake

- Do not request unrelated permissions; users notice over-permissioned apps.

### 📌 Key Point

- Permission timing affects trust and user experience.

## Notebook Cell 2 — Declare permissions and FileProvider

### Explanation

- A FileProvider gives the camera app safe access to a file Uri that belongs to your app.

### Code to Read / Run

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission
    android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />

<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_provider_paths" />
</provider>
```

### 🔵 Try This

- Create `res/xml/file_provider_paths.xml` with a cache-path or files-path entry.

### Expected Output

- The manifest is ready for secure photo capture.

### ✅ Checkpoint

- Can you explain why exposing `file://` paths directly is discouraged?

### ⚠️ Common Mistake

- If the provider authority does not match the application ID, camera capture will fail.

### 📌 Key Point

- Camera integration is partly about storage safety, not only about UI buttons.

## Notebook Cell 3 — Implement the complete ScanActivity.java

### Explanation

- This activity handles camera capture, gallery selection, permission requests, and preview updates.

> **Note about the real app:** In the shipped LeafGuard AI project (both `android-app-kotlin/` and `android-app/`), image capture happens inside **`MainActivity`** — there is no separate `ScanActivity` class. `ScanActivity` here (and `exercises/.../ex02_ScanActivity`) is a **standalone practice screen** used to isolate the camera/gallery logic. Once you understand it, the same code lives in `MainActivity` in the real app.

### Code to Read / Run

```java
package com.leafguard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScanActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private ImageView previewImage;
    private MaterialButton openCameraButton;
    private MaterialButton openGalleryButton;
    private Uri currentPhotoUri;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && currentPhotoUri != null) {
                    previewImage.setImageURI(currentPhotoUri);
                }
            });

    private final ActivityResultLauncher<String> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    currentPhotoUri = uri;
                    previewImage.setImageURI(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        previewImage = findViewById(R.id.previewImage);
        openCameraButton = findViewById(R.id.openCameraButton);
        openGalleryButton = findViewById(R.id.openGalleryButton);

        openCameraButton.setOnClickListener(view -> checkCameraPermissionAndLaunch());
        openGalleryButton.setOnClickListener(view -> galleryLauncher.launch("image/*"));
    }

    private void checkCameraPermissionAndLaunch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File photoFile = createImageFile();
            currentPhotoUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    photoFile
            );
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
            cameraLauncher.launch(cameraIntent);
        } catch (IOException exception) {
            Toast.makeText(this, "Failed to create image file", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "LEAF_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileName, ".jpg", storageDir);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
```

### 🔵 Try This

- Wire this activity to a simple `activity_scan.xml` containing one `ImageView` and two buttons.
- After preview works, add a third button later for upload or inference.

### Expected Output

- Tapping Camera opens the device camera app.
- Tapping Gallery opens the file picker and sets the selected image into the preview.

### ✅ Checkpoint

- Can you explain why `currentPhotoUri` must be stored as a field?

### ⚠️ Common Mistake

- If the preview stays empty after capture, confirm you passed `MediaStore.EXTRA_OUTPUT` and that the provider works.

### 📌 Key Point

- A correct Uri flow is more reliable than trying to handle huge Bitmaps directly from camera thumbnails.

## Notebook Cell 4 — Preview images safely

### Explanation

- Previewing the selected image confirms that capture and gallery pick worked before you move to networking or ML.

### Code to Read / Run

```xml
<ImageView
    android:id="@+id/previewImage"
    android:layout_width="0dp"
    android:layout_height="240dp"
    android:layout_margin="16dp"
    android:scaleType="centerCrop"
    android:contentDescription="Leaf preview" />
```

### 🔵 Try This

- Try a portrait image and a landscape image to see the difference between `fitCenter` and `centerCrop`.

### Expected Output

- The user sees a clear image preview before scanning.

### ✅ Checkpoint

- Why is preview important for user confidence?

### ⚠️ Common Mistake

- Avoid loading unnecessarily huge images on the main thread if you later process very large gallery files.

### 📌 Key Point

- Preview is both a UX feature and a debugging tool.

## Notebook Cell 5 — Test on an emulator

### Explanation

- Emulators can simulate camera input by using webcam passthrough or by choosing gallery images.

### Step-by-Step

1. Open Android Studio Device Manager.
2. Edit your emulator and inspect the camera settings.
3. Use webcam passthrough if available, or rely on gallery selection with sample leaf images.
4. Copy a sample image into the emulator if needed.

### 🔵 Try This

- Use one healthy leaf image and one diseased leaf image from `sample-images/`.

### Expected Output

- You can still complete Week 03 even without a physical device camera.

### ✅ Checkpoint

- Which fallback is easier on emulator: camera or gallery?

### ⚠️ Common Mistake

- Do not assume emulator camera support is perfect on every computer.

### 📌 Key Point

- Gallery fallback makes testing reliable when hardware access is limited.

## Notebook Cell 6 — Debug common camera problems

### Explanation

- Most camera bugs come from permissions, FileProvider configuration, or missing destination files.

### Code to Read / Run

```text
Common failures and fixes
-------------------------
1. SecurityException
   - Usually means permission was denied or provider setup is wrong.
2. ActivityNotFoundException
   - Means no app can handle the capture intent on that device.
3. Empty preview after capture
   - Uri may be null or the wrong file path was supplied.
4. Gallery returns nothing
   - User cancelled the picker or the launcher MIME type is incorrect.
```

### 🔵 Try This

- Intentionally deny the permission once and observe how the app behaves.

### Expected Output

- You can identify the error source faster using Logcat and this checklist.

### ✅ Checkpoint

- If capture fails after pressing allow, what file-creation method would you inspect first?

### ⚠️ Common Mistake

- Do not debug only by guessing; always read Logcat.

### 📌 Key Point

- A reproducible bug is easier to solve than a vague complaint.

## Notebook Cell 7 — Validate the full Week 03 flow

### Explanation

- At the end of this week, ScanActivity should be able to obtain an image from either source and preview it.

### Step-by-Step

1. Launch ScanActivity from MainActivity.
2. Test Camera permission denied.
3. Test Camera permission allowed.
4. Test Gallery selection.
5. Rotate the screen once and see whether the current design still behaves acceptably.

### 🔵 Try This

- Write a short test script in your notes listing every action you performed.

### Expected Output

- All key paths either work or fail gracefully with a toast/message.

### ✅ Checkpoint

- Can you explain the full user flow from button tap to preview image?

### ⚠️ Common Mistake

- Do not move to networking before image selection is reliable.

### 📌 Key Point

- Image acquisition is the foundation for both online and offline diagnosis.

## Lab Reflection

- Write down one concept that felt easy.
- Write down one concept that felt confusing.
- Describe one bug you saw and how you fixed it.
- State which file changed the most during this notebook.
- Explain how this week supports the final LeafGuard AI submission.

## Mini Quiz

- What problem does this week solve inside LeafGuard AI?
- Which Java class or Android component did you touch first?
- Which file path in this repository is most relevant to this week?
- What would break if you skipped the validation step?
- How does this week connect to the three-tier architecture?

## Evidence Checklist

- [ ] Capture a screenshot of the completed screen or terminal output.
- [ ] Save one code snippet that proves the feature is wired correctly.
- [ ] Write two sentences in your progress log about what you learned.
- [ ] Record at least one bug and the exact fix you applied.
- [ ] Commit working changes before moving to the next week.

## Next Step

- After this notebook, continue to **[Week 04: FastAPI Backend](../../roadmap/week-04-fastapi-backend/README.md)** and connect today's work to the next subsystem.


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

### Related Roadmap Materials
- 📖 [Week 03 README](../../roadmap/week-03-camera-gallery/README.md) — Week overview & objectives
- 📝 [Week 03 Exercises](../../roadmap/week-03-camera-gallery/exercises.md) — Practice problems
- 💡 [Week 03 Solutions](../../solutions/week-03/README.md) — Reference solutions
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

### Week Progression

| ← Previous | 🏠 | Next → |
|:-----------|:--:|-------:|
| [⬅ Week 02 Notebooks](../week-02/README.md) | [Notebooks Index](../README.md) | [Week 04 Notebooks ➡](../week-04/README.md) |

---
