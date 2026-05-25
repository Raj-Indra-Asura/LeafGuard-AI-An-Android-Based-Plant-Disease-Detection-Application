# LeafGuard AI Project Architecture

## System Overview

LeafGuard AI is a **hybrid cloud-offline Android application** that enables plant disease detection through two modes:

1. **Cloud AI Mode**: Android app → HTTP POST → FastAPI Backend → ML Model → JSON Response → Android app
2. **On-Device AI Mode**: Android app → TensorFlow Lite Model → Prediction → Android app

## High-Level Architecture Diagram

```
┌──────────────────────────────────────────────────────────────────┐
│                        LEAFGUARD AI ANDROID APP                  │
│                          (Java, Android SDK)                     │
└──────────────────────────────────────────────────────────────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    │                           │
            ┌───────▼────────┐         ┌────────▼────────┐
            │   CLOUD MODE   │         │  OFFLINE MODE   │
            │                │         │                 │
            │  Retrofit HTTP │         │  TensorFlow     │
            │  POST Image    │         │  Lite Inference │
            └───────┬────────┘         └────────┬────────┘
                    │                           │
         ┌──────────▼──────────┐                │
         │  FASTAPI BACKEND    │                │
         │  (Python, Uvicorn)  │                │
         │                     │                │
         │  /predict endpoint  │                │
         └──────────┬──────────┘                │
                    │                           │
         ┌──────────▼──────────┐      ┌─────────▼────────┐
         │  ML MODEL           │      │  .tflite MODEL   │
         │  (TensorFlow/       │      │  (in assets/)    │
         │   PyTorch)          │      │                  │
         │                     │      │  + labels.txt    │
         └──────────┬──────────┘      └─────────┬────────┘
                    │                           │
         ┌──────────▼──────────┐      ┌─────────▼────────┐
         │  PREDICTION         │      │  PREDICTION      │
         │  {disease, conf, …} │      │  {disease, conf} │
         └──────────┬──────────┘      └─────────┬────────┘
                    │                           │
                    └─────────────┬─────────────┘
                                  │
                    ┌─────────────▼──────────────┐
                    │   RESULT PROCESSING        │
                    │   - Display disease info   │
                    │   - Save to Room DB        │
                    │   - Lookup XML library     │
                    └────────────────────────────┘
```

## Detailed Architecture

### 1. Android App Layer (Client)

```
android-app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/leafguard/
│   │   │   │   ├── activities/
│   │   │   │   │   ├── MainActivity.java
│   │   │   │   │   ├── ResultActivity.java
│   │   │   │   │   ├── HistoryActivity.java
│   │   │   │   │   ├── HistoryDetailActivity.java
│   │   │   │   │   ├── DiseaseLibraryActivity.java
│   │   │   │   │   └── SettingsActivity.java
│   │   │   │   ├── network/
│   │   │   │   │   ├── ApiService.java
│   │   │   │   │   ├── RetrofitClient.java
│   │   │   │   │   └── UploadResponse.java
│   │   │   │   ├── database/
│   │   │   │   │   ├── ScanEntity.java
│   │   │   │   │   ├── ScanDao.java
│   │   │   │   │   └── AppDatabase.java
│   │   │   │   ├── models/
│   │   │   │   │   ├── Disease.java
│   │   │   │   │   └── PredictionResult.java
│   │   │   │   ├── utils/
│   │   │   │   │   ├── ImageUtils.java
│   │   │   │   │   ├── XmlParser.java
│   │   │   │   │   └── NotificationHelper.java
│   │   │   │   ├── ml/
│   │   │   │   │   └── TFLiteInference.java
│   │   │   │   └── adapters/
│   │   │   │       ├── HistoryAdapter.java
│   │   │   │       └── DiseaseAdapter.java
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_result.xml
│   │   │   │   │   ├── activity_history.xml
│   │   │   │   │   └── …
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   └── styles.xml
│   │   │   │   └── drawable/
│   │   │   ├── assets/
│   │   │   │   ├── disease_library.xml
│   │   │   │   ├── plant_disease_model.tflite
│   │   │   │   └── labels.txt
│   │   │   └── AndroidManifest.xml
│   └── build.gradle
└── build.gradle
```

#### Key Android Components

**Activities:**
- `MainActivity`: Home screen with camera/gallery buttons, mode selector
- `ResultActivity`: Display prediction result, save to history
- `HistoryActivity`: List all past scans using RecyclerView
- `HistoryDetailActivity`: Show details of a specific scan
- `DiseaseLibraryActivity`: Browse all diseases from XML
- `SettingsActivity`: App settings, notifications

**Networking (Retrofit):**
- `ApiService`: Interface defining REST endpoints (@POST /predict)
- `RetrofitClient`: Singleton with base URL, Gson converter, OkHttp client
- `UploadResponse`: Data class for JSON response

**Database (Room):**
- `ScanEntity`: Table schema (id, imagePath, diseaseName, confidence, timestamp, location)
- `ScanDao`: Data Access Object (@Insert, @Query, @Delete methods)
- `AppDatabase`: Room database singleton

**ML Inference:**
- `TFLiteInference`: Load .tflite model, preprocess image, run inference, postprocess output

**Utilities:**
- `ImageUtils`: Resize, rotate, compress images
- `XmlParser`: Parse disease_library.xml using XmlPullParser
- `NotificationHelper`: Create notification channels, schedule reminders

---

### 2. Backend API Layer (Server)

```
backend-api/
├── main.py                # FastAPI app, /predict endpoint
├── model_loader.py        # Load ML model on startup
├── image_processor.py     # Image preprocessing pipeline
├── predictor.py           # Inference logic
├── config.py              # Configuration (model path, labels)
├── requirements.txt       # Python dependencies
└── models/
    ├── plant_disease_model.h5 (or .pt)
    └── labels.txt
```

#### Backend Flow

1. **Startup:**
   ```python
   # main.py
   from fastapi import FastAPI, File, UploadFile
   from model_loader import load_model

   app = FastAPI()
   model = load_model("models/plant_disease_model.h5")
   ```

2. **Prediction Endpoint:**
   ```python
   @app.post("/predict")
   async def predict(file: UploadFile = File(...)):
       # 1. Read image bytes
       image_bytes = await file.read()

       # 2. Preprocess image (resize, normalize)
       processed_image = preprocess(image_bytes)

       # 3. Run inference
       prediction = model.predict(processed_image)

       # 4. Get top prediction
       class_idx = np.argmax(prediction)
       confidence = float(prediction[0][class_idx])
       disease_name = labels[class_idx]

       # 5. Return JSON
       return {
           "disease": disease_name,
           "confidence": confidence,
           "symptoms": get_symptoms(disease_name),
           "treatment": get_treatment(disease_name),
           "prevention": get_prevention(disease_name)
       }
   ```

3. **Running Backend Locally:**
   ```bash
   # On laptop (same Wi-Fi as phone)
   uvicorn main:app --host 0.0.0.0 --port 8000

   # Find laptop IP: ipconfig (Windows) or ifconfig (Mac/Linux)
   # Example: 192.168.1.5

   # In Android app, set base URL:
   # http://192.168.1.5:8000
   ```

---

### 3. Data Flow Diagrams

#### Flow 1: Cloud AI Mode (Image Upload & Prediction)

```
[User] → Tap "Scan" → [MainActivity]
                            │
                            ├──> Camera Intent / Gallery Intent
                            │
                        [Image Selected]
                            │
                            ├──> Show image preview
                            │
                        Tap "Analyze"
                            │
                            ├──> Create MultipartBody with image
                            │
                            ├──> RetrofitClient.apiService.uploadImage(multipart)
                            │
                            ├──> HTTP POST → http://192.168.1.5:8000/predict
                            │
                    [FastAPI Backend Receives Request]
                            │
                            ├──> Read image bytes
                            │
                            ├──> Preprocess image (resize to 224x224, normalize)
                            │
                            ├──> model.predict(image)
                            │
                            ├──> Get argmax(output) → class_idx
                            │
                            ├──> Map class_idx → disease_name using labels.txt
                            │
                            ├──> Build JSON response
                            │
                            ├──> HTTP Response 200 OK
                            │
                    [Android Receives JSON Response]
                            │
                            ├──> Parse JSON using Gson
                            │
                            ├──> Extract disease, confidence, symptoms, treatment
                            │
                            ├──> Create ScanEntity object
                            │
                            ├──> Insert into Room database
                            │
                            ├──> Start ResultActivity
                            │
                        [ResultActivity]
                            │
                            ├──> Display disease name
                            ├──> Display confidence %
                            ├──> Display symptoms (from JSON or XML lookup)
                            ├──> Display treatment
                            ├──> Display prevention
                            │
                            └──> Options: Share, View History, Scan Again
```

#### Flow 2: Offline AI Mode (On-Device Inference)

```
[User] → Tap "Scan (Offline Mode)" → [MainActivity]
                                            │
                                            ├──> Camera/Gallery Intent
                                            │
                                        [Image Selected]
                                            │
                                            ├──> Show preview
                                            │
                                        Tap "Analyze (Offline)"
                                            │
                                            ├──> Convert image to Bitmap
                                            │
                                            ├──> Resize to 224x224
                                            │
                                            ├──> Convert to ByteBuffer (float32)
                                            │
                                            ├──> Normalize pixel values (0-1 or -1 to 1)
                                            │
                                            ├──> TFLiteInference.runInference(byteBuffer)
                                            │
                                    [TensorFlow Lite Interpreter]
                                            │
                                            ├──> Load model from assets/
                                            │
                                            ├──> Run interpreter.run(input, output)
                                            │
                                            ├──> Get output float array
                                            │
                                            ├──> argmax(output) → class_idx
                                            │
                                            ├──> Map to disease_name using labels.txt
                                            │
                                            ├──> Get confidence score
                                            │
                                        [Return Prediction]
                                            │
                                            ├──> Lookup disease details in XML library
                                            │
                                            ├──> Create ScanEntity, save to DB
                                            │
                                            ├──> Start ResultActivity
                                            │
                                        [ResultActivity displays result]
```

#### Flow 3: Scan History (Room Database)

```
[User] → Tap "History" → [HistoryActivity]
                                │
                                ├──> ScanDao.getAllScans()
                                │
                            [Room Database Query]
                                │
                                ├──> SELECT * FROM scans ORDER BY timestamp DESC
                                │
                            [Return List<ScanEntity>]
                                │
                                ├──> HistoryAdapter.setData(scanList)
                                │
                                ├──> RecyclerView displays list
                                │
    [User taps on a scan item] ─┘
                                │
                                ├──> Start HistoryDetailActivity
                                │
                                ├──> Pass scan ID via Intent
                                │
                            [HistoryDetailActivity]
                                │
                                ├──> ScanDao.getScanById(id)
                                │
                                ├──> Display: image, disease, confidence, date, location
                                │
                                ├──> Options: Delete, Share, Rescan
```

#### Flow 4: XML Disease Library Parsing

```
[App Startup] → [Parse disease_library.xml once]
                        │
                        ├──> XmlParser.parseDiseaseLibrary(context)
                        │
                    [Read assets/disease_library.xml]
                        │
                        ├──> XmlPullParser loop through <disease> nodes
                        │
                        ├──> For each disease:
                        │       - Extract name, symptoms, treatment, prevention
                        │       - Create Disease object
                        │       - Add to List<Disease>
                        │
                        ├──> Cache in memory (singleton or static list)
                        │
                    [XML Parsed, Data Ready]
                        │
[When prediction received] → Lookup disease by name
                        │
                        ├──> Find matching Disease object in cached list
                        │
                        ├──> Retrieve symptoms, treatment, prevention
                        │
                        └──> Display in ResultActivity
```

---

## Technology Stack Details

### Android App

| Component | Technology | Purpose |
|-----------|-----------|---------|
| Language | Java | CSE 2206 requirement, syllabus-aligned |
| IDE | Android Studio | Official Android development environment |
| Min SDK | API 21 (Lollipop 5.0) | Broad device support |
| Target SDK | API 33+ | Modern Android features |
| UI | XML Layouts | Declarative UI design |
| Navigation | Intents | Activity transitions |
| HTTP Client | Retrofit 2 + OkHttp | REST API communication |
| JSON Parsing | Gson | JSON serialization/deserialization |
| Database | Room | SQLite abstraction, compile-time verification |
| ML Framework | TensorFlow Lite | On-device inference |
| Image Loading | BitmapFactory | Load and process images |
| Permissions | ActivityCompat | Runtime permission handling |
| Notifications | NotificationManager | Reminder notifications |
| Location | LocationManager | Optional GPS tagging |

### Backend API

| Component | Technology | Purpose |
|-----------|-----------|---------|
| Language | Python 3.8+ | ML ecosystem compatibility |
| Framework | FastAPI | Modern, fast API framework |
| Server | Uvicorn | ASGI server for FastAPI |
| ML Framework | TensorFlow or PyTorch | Model inference |
| Image Processing | PIL/Pillow | Image preprocessing |
| Data Validation | Pydantic | Request/response validation |

### Machine Learning

| Component | Details |
|-----------|---------|
| Model Architecture | CNN (ResNet, MobileNet, or custom) |
| Input Size | 224×224×3 (RGB image) |
| Output | Softmax probabilities for N disease classes |
| Training Data | PlantVillage dataset or similar |
| Classes | 6-38 plant disease classes (configurable) |
| Conversion | TensorFlow → TensorFlow Lite (float16 or quantized) |

---

## Communication Protocols

### Android ↔ Backend (HTTP REST API)

**Request (Upload Image):**
```http
POST http://192.168.1.5:8000/predict
Content-Type: multipart/form-data

----boundary
Content-Disposition: form-data; name="file"; filename="leaf.jpg"
Content-Type: image/jpeg

[binary image data]
----boundary--
```

**Response (Prediction Result):**
```json
{
  "disease": "Tomato Early Blight",
  "confidence": 0.9234,
  "symptoms": "Dark spots with concentric rings on leaves...",
  "treatment": "Apply fungicide containing chlorothalonil...",
  "prevention": "Rotate crops, avoid overhead watering..."
}
```

**Error Response:**
```json
{
  "error": "Invalid image format",
  "detail": "Uploaded file must be JPEG or PNG"
}
```

---

## Database Schema

### Room Database: `app_database.db`

**Table: `scans`**

| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER PRIMARY KEY | Auto-incremented scan ID |
| image_path | TEXT | Local file path to saved image |
| disease_name | TEXT | Predicted disease name |
| confidence | REAL | Confidence score (0.0 - 1.0) |
| mode | TEXT | "cloud" or "offline" |
| timestamp | INTEGER | Unix timestamp (milliseconds) |
| location_lat | REAL | GPS latitude (nullable) |
| location_lon | REAL | GPS longitude (nullable) |

**Sample SQL:**
```sql
CREATE TABLE scans (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    image_path TEXT NOT NULL,
    disease_name TEXT NOT NULL,
    confidence REAL NOT NULL,
    mode TEXT NOT NULL,
    timestamp INTEGER NOT NULL,
    location_lat REAL,
    location_lon REAL
);
```

**Room Entity:**
```java
@Entity(tableName = "scans")
public class ScanEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    @ColumnInfo(name = "disease_name")
    private String diseaseName;

    private float confidence;
    private String mode;
    private long timestamp;

    @ColumnInfo(name = "location_lat")
    private Double locationLat;

    @ColumnInfo(name = "location_lon")
    private Double locationLon;

    // Getters and setters...
}
```

---

## XML Data Structure

### `assets/disease_library.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<diseases>
    <disease>
        <name>Tomato Early Blight</name>
        <scientific_name>Alternaria solani</scientific_name>
        <symptoms>
            Dark brown spots with concentric rings on lower leaves.
            Yellow halo around spots. Leaves may drop prematurely.
        </symptoms>
        <treatment>
            Apply fungicide containing chlorothalonil, mancozeb, or copper.
            Remove infected leaves. Improve air circulation.
        </treatment>
        <prevention>
            Rotate crops every 2-3 years. Use disease-resistant varieties.
            Avoid overhead watering. Mulch around plants.
        </prevention>
    </disease>

    <disease>
        <name>Tomato Late Blight</name>
        <scientific_name>Phytophthora infestans</scientific_name>
        <symptoms>
            Water-soaked spots on leaves and stems. White mold on undersides.
            Rapid leaf death. Fruit rot.
        </symptoms>
        <treatment>
            Apply fungicide immediately (copper-based or organic options).
            Remove and destroy infected plants. Do not compost.
        </treatment>
        <prevention>
            Plant resistant varieties. Ensure good air circulation.
            Water in morning. Monitor weather for blight-favorable conditions.
        </prevention>
    </disease>

    <!-- More diseases... -->
</diseases>
```

---

## Deployment Architecture

### Development Setup (Local Testing)

```
┌──────────────────┐         Wi-Fi Network         ┌──────────────────┐
│  Android Device  │ ←─────────────────────────→   │   Developer      │
│  or Emulator     │      192.168.1.x/24            │   Laptop         │
│                  │                                │                  │
│  LeafGuard App   │                                │  FastAPI Backend │
│  (APK installed) │                                │  (uvicorn)       │
│                  │                                │  Port: 8000      │
│  Base URL:       │                                │                  │
│  192.168.1.5:8000│                                │  ML Model Loaded │
└──────────────────┘                                └──────────────────┘
```

**Setup Steps:**
1. Run FastAPI on laptop: `uvicorn main:app --host 0.0.0.0 --port 8000`
2. Find laptop IP: `ipconfig` (Windows) or `ifconfig` (Mac/Linux)
3. Connect phone and laptop to same Wi-Fi
4. In Android app, configure base URL: `http://<laptop-ip>:8000`
5. Test ping from phone browser: `http://192.168.1.5:8000/docs`

### Production Deployment (Optional, Not Required for CSE 2206)

- Backend on AWS EC2, GCP Compute Engine, or Azure VM
- Use HTTPS with valid SSL certificate
- Implement API authentication (JWT tokens)
- Use cloud database (PostgreSQL, MongoDB)
- Scale with load balancer
- Deploy TensorFlow Serving for model inference

**For CSE 2206: Local deployment is sufficient. Cloud deployment is NOT required.**

---

## Security Considerations

### Data Privacy
- No user authentication (out of scope for CSE 2206)
- Images stored locally on device
- No image upload to permanent cloud storage
- Predictions not logged on server

### Network Security
- Local backend uses HTTP (not HTTPS) for simplicity
- No sensitive data transmitted
- No API keys required

### Permissions Justification
- CAMERA: Required for taking leaf photos
- READ_EXTERNAL_STORAGE: Required for gallery image selection
- WRITE_EXTERNAL_STORAGE: Required to save scan images
- ACCESS_FINE_LOCATION: Optional, for GPS tagging
- INTERNET: Required for cloud AI mode

---

## Performance Considerations

### Image Size Optimization
- Resize images to 224×224 before upload (reduces network payload)
- Compress JPEG to 80% quality (balance between size and quality)
- Maximum upload size: 5MB

### Network Optimization
- Set Retrofit timeout: connect=30s, read=60s, write=60s
- Show loading indicator during network call
- Cache predictions locally (Room database)
- Implement retry logic with exponential backoff (optional)

### On-Device Inference Speed
- TFLite model: ~200-500ms inference time on mid-range phone
- Use quantized model (int8) for faster inference
- Preprocessing: ~50-100ms
- Total offline mode: <1 second

### Database Optimization
- Index on timestamp column for faster history queries
- Limit history to last 100 scans (optional pagination)
- Clean up old scans periodically

---

## Error Handling Strategy

### Network Errors
- No internet connection → Show message, enable offline mode
- Backend not reachable → Show retry button
- Timeout → Show message, suggest checking Wi-Fi
- Invalid response → Log error, show generic error message

### ML Model Errors
- Low confidence (<50%) → Warn user "Uncertain prediction"
- Model loading failure → Disable offline mode, log error
- Invalid image → Validate image format before processing

### Database Errors
- Insert failure → Retry once, then warn user
- Query failure → Show empty state with error message
- Database corruption → Attempt recovery, fallback to in-memory

### Permission Errors
- Permission denied → Show rationale, direct to settings
- Camera unavailable → Show message, suggest gallery
- Location unavailable → Document in scan record as "unavailable"

---

## Testing Strategy

### Unit Testing (Optional for CSE 2206)
- ImageUtils resize logic
- XmlParser correctness
- Prediction confidence calculation

### Integration Testing (Manual, Required)
- Camera intent → image preview → upload → result
- Gallery picker → image preview → upload → result
- Scan saved to database → appears in history
- Delete scan → removed from history
- Share scan → Android share dialog appears

### End-to-End Testing (Manual, Required)
- Full workflow: capture → analyze (cloud) → view result → save → view history
- Full workflow: capture → analyze (offline) → view result → save → view history
- Edge cases: no internet, permission denied, invalid image, backend down

### Performance Testing
- Measure cloud mode latency (time from upload to response)
- Measure offline mode latency (time from input to prediction)
- Display comparison in app

---

## Scalability & Future Enhancements

### Not Required, but Good to Document

**Backend Enhancements:**
- User authentication and personal history
- Cloud database for multi-device sync
- Real-time notifications of disease outbreaks
- API versioning (/v1/predict, /v2/predict)

**App Enhancements:**
- Multiple language support (i18n)
- Dark mode UI theme
- Bulk scanning of multiple leaves
- Export history as CSV or PDF
- Integration with agricultural advisory services

**ML Enhancements:**
- Retrain model with more disease classes
- Implement disease progression tracking
- Severity assessment (mild, moderate, severe)
- Multi-plant support (not just tomato/potato)

---

## Conclusion

This architecture achieves:

- ✅ Clean separation of concerns (UI, Network, Database, ML)
- ✅ Scalable structure (easy to add features)
- ✅ CSE 2206 syllabus coverage (every required topic addressed)
- ✅ Demonstrable components (each layer can be tested independently)
- ✅ Professional design patterns (Repository, Singleton, Adapter, etc.)

**Next Steps:**
1. Read this architecture document thoroughly
2. Understand data flow diagrams
3. Sketch the architecture diagram for your report
4. Proceed to Week 01 to begin implementation

---

**Now read `SENIOR_REPO_ANALYSIS.md` to learn how to analyze existing Android projects.**
