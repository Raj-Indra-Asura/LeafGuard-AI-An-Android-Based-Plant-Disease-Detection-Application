# Android Code Examples

## 1. MainActivity navigation pattern

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnCamera).setOnClickListener(v ->
                startActivity(new Intent(this, CameraActivity.class)));

        findViewById(R.id.btnHistory).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
    }
}
```

## 2. Safe camera permission request

```java
private final ActivityResultLauncher<String> cameraPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
            if (granted) openCamera();
            else Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
        });

private void requestCameraIfNeeded() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
        openCamera();
    } else {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
    }
}
```

## 3. FileProvider camera URI

```java
private Uri createCameraImageUri() throws IOException {
    File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    if (dir != null && !dir.exists()) dir.mkdirs();
    File file = File.createTempFile("leaf_", ".jpg", dir);
    return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
}
```

## 4. Image scaling before ML

```java
public static Bitmap scaleForModel(Bitmap source) {
    return Bitmap.createScaledBitmap(source, 224, 224, true);
}
```

## 5. Basic result model

```java
public class PredictionResult {
    public final String diseaseName;
    public final float confidence;
    public final String treatment;

    public PredictionResult(String diseaseName, float confidence, String treatment) {
        this.diseaseName = diseaseName;
        this.confidence = confidence;
        this.treatment = treatment;
    }
}
```

## Common mistakes

- Forgetting runtime permissions.
- Using `file://` URI instead of `content://` URI.
- Loading a full-resolution image into memory.
- Calling network/database operations on the main thread.
