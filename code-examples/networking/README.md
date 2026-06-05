# Networking Code Examples

## 1. Retrofit service

```java
public interface LeafGuardApi {
    @Multipart
    @POST("predict")
    Call<PredictionResponse> predict(@Part MultipartBody.Part image);
}
```

## 2. Response model

```java
public class PredictionResponse {
    public String label;
    public float confidence;
    public String advice;
}
```

## 3. Retrofit client

```java
Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/") // emulator to local computer
        .addConverterFactory(GsonConverterFactory.create())
        .build();

LeafGuardApi api = retrofit.create(LeafGuardApi.class);
```

## 4. Multipart request body

```java
RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/jpeg"));
MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
api.predict(body).enqueue(new Callback<PredictionResponse>() {
    @Override
    public void onResponse(Call<PredictionResponse> call, Response<PredictionResponse> response) {
        if (response.isSuccessful() && response.body() != null) {
            // Display prediction
        }
    }

    @Override
    public void onFailure(Call<PredictionResponse> call, Throwable t) {
        // Show network error
    }
});
```

## Testing URLs

- Android emulator → `http://10.0.2.2:8000/`
- Physical phone → `http://YOUR_COMPUTER_IP:8000/`
