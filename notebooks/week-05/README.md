# Week 05 Interactive Notebook

## Connecting Android App to FastAPI Backend

> This README acts like a Markdown notebook for CSE 2206. Read one cell at a time, run the code, and write your own notes after each checkpoint.

### How to use this notebook

- Follow the cells in order.
- Run code blocks in Android Studio, Terminal, or a Python shell as indicated.
- Keep LeafGuard AI open in Android Studio while you work.
- Save screenshots for your evidence folder after each big milestone.
- Use Java for Android code in this repository. Do not switch to Kotlin.

### Weekly outcomes

- Add Retrofit and Gson to the Java Android app.
- Upload leaf images as multipart requests to `/predict`.
- Handle loading, success, and failure states cleanly.

### Repository references

- `android-app/app/build.gradle`
- `backend-api/main.py`
- `roadmap/week-05-android-networking/`

---

## Notebook Cell 1 — Add Retrofit dependencies

### Explanation

- Retrofit is a type-safe HTTP client that turns Java interfaces into REST calls.

### Code to Read / Run

```gradle
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
implementation 'com.google.code.gson:gson:2.10.1'
```

### 🔵 Try This

- Sync Gradle and verify there are no dependency download errors.

### Expected Output

- The Android project now has HTTP and JSON parsing support.

### ✅ Checkpoint

- Which dependency converts JSON into Java objects automatically?

### ⚠️ Common Mistake

- Do not forget to keep the INTERNET permission in the manifest.

### 📌 Key Point

- Networking setup starts in Gradle before it starts in Java code.

## Notebook Cell 2 — Create the response model and API interface

### Explanation

- Java model classes mirror the JSON sent by FastAPI.

### Code to Read / Run

```java
public class PredictionResponse {
    private String disease;
    private float confidence;
    private String symptoms;
    private String treatment;
    private String prevention;

    public String getDisease() { return disease; }
    public float getConfidence() { return confidence; }
    public String getSymptoms() { return symptoms; }
    public String getTreatment() { return treatment; }
    public String getPrevention() { return prevention; }
}

public interface LeafGuardApiService {
    @Multipart
    @POST("predict")
    Call<PredictionResponse> predictDisease(@Part MultipartBody.Part image);
}
```

### 🔵 Try This

- Match each Java field to the JSON keys returned by FastAPI.

### Expected Output

- Retrofit knows the endpoint path and expected response type.

### ✅ Checkpoint

- Why should field names match JSON keys unless you use `@SerializedName`?

### ⚠️ Common Mistake

- If a field stays null, check whether the backend key matches the Java property name.

### 📌 Key Point

- A stable API contract reduces integration pain.

## Notebook Cell 3 — Build the Retrofit client

### Explanation

- The base URL depends on where the API runs. Emulator localhost is special.

### Code to Read / Run

```java
public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8000/";
    private static Retrofit retrofit;

    public static LeafGuardApiService getService() {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit.create(LeafGuardApiService.class);
    }
}
```

### 🔵 Try This

- Replace the base URL with your Render URL after cloud deployment.

### Expected Output

- The Android app can now reach a FastAPI server.

### ✅ Checkpoint

- Why does the Android emulator use `10.0.2.2` instead of `localhost`?

### ⚠️ Common Mistake

- Using `http://localhost:8000/` from the emulator will not hit your computer's API server.

### 📌 Key Point

- Networking failures often come from the wrong base URL.

## Notebook Cell 4 — Create the multipart upload request

### Explanation

- Images must be wrapped in a multipart body to match the backend endpoint.

### Code to Read / Run

```java
private MultipartBody.Part createImagePart(File imageFile) {
    RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("image/*"));
    return MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);
}
```

### 🔵 Try This

- Confirm the form-data field name is exactly `image`.

### Expected Output

- The upload payload matches the FastAPI parameter name.

### ✅ Checkpoint

- What would happen if you named the field `photo` instead?

### ⚠️ Common Mistake

- A wrong field name leads to server-side validation errors.

### 📌 Key Point

- Small naming mismatches can break otherwise correct networking code.

## Notebook Cell 5 — Implement the complete upload flow

### Explanation

- This example shows loading state, success handling, and error feedback in one place.

### Code to Read / Run

```java
    public void uploadLeafImage(File imageFile) {
        progressBar.setVisibility(View.VISIBLE);
        resultText.setText("Uploading image...");

        MultipartBody.Part imagePart = createImagePart(imageFile);
        LeafGuardApiService apiService = ApiClient.getService();

        apiService.predictDisease(imagePart).enqueue(new Callback<PredictionResponse>() {
            @Override
            public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    PredictionResponse prediction = response.body();
                    String message = "Disease: " + prediction.getDisease()
                            + "
Confidence: " + Math.round(prediction.getConfidence() * 100) + "%"
                            + "
Treatment: " + prediction.getTreatment();
                    resultText.setText(message);
                } else {
                    resultText.setText("Prediction failed: server returned " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PredictionResponse> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                resultText.setText("Network error: " + throwable.getMessage());
            }
        });
    }
```

### 🔵 Try This

- Temporarily stop the backend and watch the `onFailure` branch.

### Expected Output

- The progress bar appears during upload and disappears after success or failure.

### ✅ Checkpoint

- Why is callback-based handling necessary for network operations on Android?

### ⚠️ Common Mistake

- Do not block the main thread with manual networking code.

### 📌 Key Point

- A good networking flow always communicates loading and failure states.

## Notebook Cell 6 — Handle localhost and emulator testing

### Explanation

- Android emulator networking has one of the most common beginner pitfalls in mobile development.

### Step-by-Step

1. Run the FastAPI server on your computer.
2. Use `http://10.0.2.2:8000/` as the emulator base URL.
3. Keep cleartext traffic allowed in debug if using HTTP locally.
4. Switch to your deployed HTTPS URL later.

### 🔵 Try This

- Open the emulator browser and try loading the API health-check endpoint.

### Expected Output

- If the browser can reach the health endpoint, Retrofit should also be able to reach it.

### ✅ Checkpoint

- Can you explain what `10.0.2.2` represents?

### ⚠️ Common Mistake

- Do not test against the wrong machine IP when emulator networking already provides a shortcut.

### 📌 Key Point

- Verify connectivity with the simplest endpoint before debugging upload code.

## Notebook Cell 7 — Validate the complete networking feature

### Explanation

- This is the week where LeafGuard AI begins to feel like an end-to-end product.

### Step-by-Step

1. Select or capture an image.
2. Send it to the FastAPI backend.
3. Parse the JSON response.
4. Display disease name, confidence, and treatment text.

### 🔵 Try This

- Save screenshots for both a successful response and an intentional failure.

### Expected Output

- Your Android app shows real or mock backend results on screen.

### ✅ Checkpoint

- Can you describe the full upload pipeline from Java File to JSON response?

### ⚠️ Common Mistake

- Do not move on if you only tested successful responses; also test server-down and bad-file cases.

### 📌 Key Point

- Robust apps handle failures as deliberately as successes.

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

- After this notebook, continue to **[Week 06: Cloud ML Model](../../roadmap/week-06-cloud-ml-model/README.md)** and connect today's work to the next subsystem.


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

### Related Roadmap Materials
- 📖 [Week 05 README](../../roadmap/week-05-android-networking/README.md) — Week overview & objectives
- 📝 [Week 05 Exercises](../../roadmap/week-05-android-networking/exercises.md) — Practice problems
- 💡 [Week 05 Solutions](../../solutions/week-05/README.md) — Reference solutions
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

### Week Progression

| ← Previous | 🏠 | Next → |
|:-----------|:--:|-------:|
| [⬅ Week 04 Notebooks](../week-04/README.md) | [Notebooks Index](../README.md) | [Week 06 Notebooks ➡](../week-06/README.md) |

---
