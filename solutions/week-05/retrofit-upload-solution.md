# Week 05 Solution - Retrofit Image Upload

## Exercise Goal
In Week 05, the Android app starts communicating with the FastAPI backend. Students need three connected pieces:
1. a Retrofit interface
2. a Retrofit singleton client
3. upload logic inside `MainActivity`

This solution gives complete working code and explains why each design choice was made.

---

## 1. `ApiService.java`

```java
package com.leafguard.network;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("predict")
    Call<PredictionResponse> uploadImage(@Part MultipartBody.Part image);
}
```

### Why this design?
- `@Multipart` is required for file upload.
- `@POST("predict")` matches the FastAPI endpoint path.
- `MultipartBody.Part` is the standard OkHttp/Retrofit way to send image files.
- `Call<PredictionResponse>` keeps the response strongly typed.

---

## 2. `RetrofitClient.java`

```java
package com.leafguard.network;

import com.leafguard.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {

    private static final String DEFAULT_BASE_URL = "http://10.0.2.2:8000/";

    private static Retrofit retrofit;
    private static String activeBaseUrl = DEFAULT_BASE_URL;

    private RetrofitClient() {
    }

    public static synchronized Retrofit getInstance() {
        return getInstance(DEFAULT_BASE_URL);
    }

    public static synchronized Retrofit getInstance(String baseUrl) {
        if (retrofit == null || !activeBaseUrl.equals(baseUrl)) {
            activeBaseUrl = baseUrl;

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BuildConfig.DEBUG
                    ? HttpLoggingInterceptor.Level.BODY
                    : HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(activeBaseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }
}
```

### Why this design?
- `10.0.2.2` is the special Android emulator alias for the host machine.
- Singleton Retrofit avoids rebuilding the HTTP stack repeatedly.
- Timeouts are important because image uploads are slower than normal API calls.
- Logging interceptor helps students inspect requests during debugging.
- Gson converts JSON directly into `PredictionResponse` objects.

---

## 3. `PredictionResponse.java`

```java
package com.leafguard.network;

import com.google.gson.annotations.SerializedName;

public class PredictionResponse {

    @SerializedName("disease")
    private String disease;

    @SerializedName("confidence")
    private float confidence;

    @SerializedName("symptoms")
    private String symptoms;

    @SerializedName("treatment")
    private String treatment;

    @SerializedName("prevention")
    private String prevention;

    public String getDisease() { return disease; }
    public float getConfidence() { return confidence; }
    public String getSymptoms() { return symptoms; }
    public String getTreatment() { return treatment; }
    public String getPrevention() { return prevention; }
}
```

### Why use a model class?
- It avoids parsing raw JSON manually.
- The same object can be passed to `ResultActivity`.
- It keeps networking code cleaner.

---

## 4. Upload Code in `MainActivity.java`

```java
private void uploadImageToBackend(Uri imageUri) {
    try {
        File imageFile = copyUriToCacheFile(imageUri);
        RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("image/*"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                "image",
                imageFile.getName(),
                requestBody
        );

        binding.progressDetection.setVisibility(View.VISIBLE);
        binding.buttonDetectDisease.setEnabled(false);

        RetrofitClient.getApiService().uploadImage(imagePart).enqueue(new Callback<PredictionResponse>() {
            @Override
            public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                binding.progressDetection.setVisibility(View.GONE);
                binding.buttonDetectDisease.setEnabled(true);

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(MainActivity.this, "Prediction failed: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                PredictionResponse prediction = response.body();
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra(ResultActivity.EXTRA_DISEASE_NAME, prediction.getDisease());
                intent.putExtra(ResultActivity.EXTRA_CONFIDENCE, prediction.getConfidence());
                intent.putExtra(ResultActivity.EXTRA_SYMPTOMS, prediction.getSymptoms());
                intent.putExtra(ResultActivity.EXTRA_TREATMENT, prediction.getTreatment());
                intent.putExtra(ResultActivity.EXTRA_PREVENTION, prediction.getPrevention());
                intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri.toString());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<PredictionResponse> call, Throwable throwable) {
                binding.progressDetection.setVisibility(View.GONE);
                binding.buttonDetectDisease.setEnabled(true);
                Toast.makeText(MainActivity.this, "Network error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    } catch (IOException exception) {
        binding.progressDetection.setVisibility(View.GONE);
        binding.buttonDetectDisease.setEnabled(true);
        Toast.makeText(this, "Image preparation failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
```

### Helper method for copying the `Uri`

```java
private File copyUriToCacheFile(Uri imageUri) throws IOException {
    File outputFile = new File(getCacheDir(), "upload_" + System.currentTimeMillis() + ".jpg");

    try (InputStream inputStream = getContentResolver().openInputStream(imageUri);
         FileOutputStream outputStream = new FileOutputStream(outputFile)) {

        if (inputStream == null) {
            throw new IOException("Unable to open input stream from Uri.");
        }

        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
    }

    return outputFile;
}
```

### Required imports

```java
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
```

---

## Error Handling Strategy

### 1. Request construction errors
If the app cannot read the `Uri`, the `catch (IOException)` block shows a clear message.

### 2. HTTP errors
If FastAPI returns `400`, `422`, or `500`, `response.isSuccessful()` becomes `false`, so the app shows the response code.

### 3. Network failures
If the backend is offline, `onFailure()` is called and shows the exception message.

### 4. UI state recovery
The progress indicator is always hidden and the detect button is always re-enabled after success or failure.

---

## Design Decisions Explained

### Why copy the `Uri` into a cache file?
Android gallery results often provide content URIs, not direct file paths. Retrofit needs a file-like body, so copying the content into cache is the safest approach.

### Why use `enqueue()` instead of `execute()`?
`enqueue()` runs asynchronously and keeps the main thread responsive.

### Why send only the image part?
The backend currently needs just one field: `image`. Keep the contract simple first, then add optional metadata later.

### Why navigate only after checking `response.body()`?
A successful HTTP response with an empty body would still break the next screen. Always validate body content.

---

## Common Mistakes
- Using `localhost` instead of `10.0.2.2` on the emulator.
- Forgetting the `INTERNET` permission in `AndroidManifest.xml`.
- Passing a `Uri` directly to Retrofit without converting it.
- Blocking the main thread with synchronous network calls.
- Not handling `response.body() == null`.

---

## Quick Test Checklist
1. Start FastAPI locally.
2. Open the Android emulator.
3. Select an image.
4. Tap **Detect Disease**.
5. Watch Logcat and the OkHttp logs.
6. Confirm navigation to `ResultActivity`.
7. Verify disease, confidence, and recommendation text display correctly.


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

- 📝 [Back to Week 05 Exercises](../../roadmap/week-05-android-networking/exercises.md) — Try it yourself first
- 📖 [Week 05 README](../../roadmap/week-05-android-networking/README.md) — Week overview
- 💡 [Solutions Index for Week 05](README.md) — Other solutions this week
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

---
