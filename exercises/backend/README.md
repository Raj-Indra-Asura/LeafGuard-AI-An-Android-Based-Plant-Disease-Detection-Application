# Backend Development Exercises

## Overview

This directory contains backend development exercises for the LeafGuard AI project using FastAPI. These exercises focus on building a robust REST API that handles image uploads, performs ML model inference, and returns disease detection results. You'll learn API design, file handling, model integration, error management, and testing.

## Weekly Mapping

### Weeks 4-6: Backend Development with FastAPI

- **Week 4**: FastAPI fundamentals, basic endpoints, request/response handling
- **Week 5**: File upload handling, image processing, validation
- **Week 6**: ML model integration, inference pipeline, optimization

## Exercise Categories

### 1. FastAPI Fundamentals (Week 4)

**Exercise 1.1: Create Basic FastAPI Application**

```python
# main.py
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI(
    title="LeafGuard AI API",
    description="Plant disease detection API",
    version="1.0.0"
)

# Configure CORS for Android client
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Configure appropriately for production
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
async def root():
    return {"message": "LeafGuard AI API is running"}

@app.get("/health")
async def health_check():
    return {"status": "healthy", "version": "1.0.0"}
```

**Verification**:
```bash
# Start the server
uvicorn main:app --reload

# Test endpoints
curl http://localhost:8000/
curl http://localhost:8000/health
```

**Exercise 1.2: Create Disease Information Endpoint**

```python
from typing import List
from pydantic import BaseModel

class DiseaseInfo(BaseModel):
    id: int
    name: str
    plant_type: str
    symptoms: List[str]
    treatment: str
    prevention: List[str]

# Sample disease database
DISEASES = [
    {
        "id": 1,
        "name": "Early Blight",
        "plant_type": "Tomato",
        "symptoms": ["Dark spots on leaves", "Yellow halos", "Leaf drop"],
        "treatment": "Apply fungicide containing chlorothalonil",
        "prevention": ["Crop rotation", "Remove infected debris", "Proper spacing"]
    },
    # Add more diseases...
]

@app.get("/diseases", response_model=List[DiseaseInfo])
async def get_diseases():
    return DISEASES

@app.get("/diseases/{disease_id}", response_model=DiseaseInfo)
async def get_disease(disease_id: int):
    disease = next((d for d in DISEASES if d["id"] == disease_id), None)
    if disease is None:
        raise HTTPException(status_code=404, detail="Disease not found")
    return disease
```

**Verification**:
```bash
curl http://localhost:8000/diseases
curl http://localhost:8000/diseases/1
curl http://localhost:8000/diseases/999  # Should return 404
```

### 2. File Upload Handling (Week 5)

**Exercise 2.1: Implement Image Upload Endpoint**

```python
from fastapi import File, UploadFile, HTTPException
from PIL import Image
import io
import uuid
from pathlib import Path

UPLOAD_DIR = Path("uploads")
UPLOAD_DIR.mkdir(exist_ok=True)

ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png"}
MAX_FILE_SIZE = 10 * 1024 * 1024  # 10MB

@app.post("/upload")
async def upload_image(file: UploadFile = File(...)):
    # Validate file extension
    file_ext = Path(file.filename).suffix.lower()
    if file_ext not in ALLOWED_EXTENSIONS:
        raise HTTPException(
            status_code=400,
            detail=f"Invalid file type. Allowed: {ALLOWED_EXTENSIONS}"
        )

    # Read file content
    contents = await file.read()

    # Validate file size
    if len(contents) > MAX_FILE_SIZE:
        raise HTTPException(
            status_code=400,
            detail=f"File too large. Max size: {MAX_FILE_SIZE} bytes"
        )

    # Validate it's a valid image
    try:
        image = Image.open(io.BytesIO(contents))
        image.verify()
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid image file")

    # Save file with unique name
    file_id = str(uuid.uuid4())
    file_path = UPLOAD_DIR / f"{file_id}{file_ext}"

    with open(file_path, "wb") as f:
        f.write(contents)

    return {
        "file_id": file_id,
        "filename": file.filename,
        "size": len(contents),
        "path": str(file_path)
    }
```

**Verification**:
```bash
# Test with valid image
curl -X POST -F "file=@test_image.jpg" http://localhost:8000/upload

# Test with invalid file type
curl -X POST -F "file=@test.txt" http://localhost:8000/upload

# Test with large file
# (create a large file and test size limit)
```

**Exercise 2.2: Image Preprocessing**

```python
from PIL import Image, ImageOps
import numpy as np

def preprocess_image(image: Image.Image, target_size=(224, 224)):
    """
    Preprocess image for model inference
    """
    # Convert to RGB if needed
    if image.mode != "RGB":
        image = image.convert("RGB")

    # Resize maintaining aspect ratio
    image = ImageOps.fit(image, target_size, Image.LANCZOS)

    # Convert to numpy array
    img_array = np.array(image, dtype=np.float32)

    # Normalize pixel values to [-1, 1]
    img_array = (img_array / 127.5) - 1.0

    # Add batch dimension
    img_array = np.expand_dims(img_array, axis=0)

    return img_array

@app.post("/preprocess")
async def preprocess_endpoint(file: UploadFile = File(...)):
    contents = await file.read()
    image = Image.open(io.BytesIO(contents))

    preprocessed = preprocess_image(image)

    return {
        "original_size": image.size,
        "preprocessed_shape": preprocessed.shape,
        "dtype": str(preprocessed.dtype),
        "min_value": float(preprocessed.min()),
        "max_value": float(preprocessed.max())
    }
```

**Verification**:
```bash
curl -X POST -F "file=@leaf_image.jpg" http://localhost:8000/preprocess
# Verify output shows correct shape and value ranges
```

### 3. ML Model Integration (Week 6)

**Exercise 3.1: Load and Initialize Model**

```python
import tensorflow as tf
from typing import Dict
import json

class DiseaseDetectionModel:
    def __init__(self, model_path: str, labels_path: str):
        # Load TFLite model
        self.interpreter = tf.lite.Interpreter(model_path=model_path)
        self.interpreter.allocate_tensors()

        # Get input and output details
        self.input_details = self.interpreter.get_input_details()
        self.output_details = self.interpreter.get_output_details()

        # Load class labels
        with open(labels_path, 'r') as f:
            self.labels = json.load(f)

        print(f"Model loaded: {model_path}")
        print(f"Input shape: {self.input_details[0]['shape']}")
        print(f"Output shape: {self.output_details[0]['shape']}")
        print(f"Number of classes: {len(self.labels)}")

    def predict(self, image_array: np.ndarray) -> Dict:
        # Set input tensor
        self.interpreter.set_tensor(
            self.input_details[0]['index'],
            image_array
        )

        # Run inference
        self.interpreter.invoke()

        # Get output tensor
        output = self.interpreter.get_tensor(
            self.output_details[0]['index']
        )[0]

        # Get top 3 predictions
        top_indices = np.argsort(output)[-3:][::-1]

        predictions = []
        for idx in top_indices:
            predictions.append({
                "disease": self.labels[idx],
                "confidence": float(output[idx]),
                "confidence_percentage": f"{output[idx] * 100:.2f}%"
            })

        return {
            "predictions": predictions,
            "top_prediction": predictions[0]
        }

# Initialize model at startup
model = None

@app.on_event("startup")
async def load_model():
    global model
    model = DiseaseDetectionModel(
        model_path="models/plant_disease_model.tflite",
        labels_path="models/labels.json"
    )
```

**Verification**:
```bash
# Check startup logs for model loading messages
# Verify input/output shapes match expected values
```

**Exercise 3.2: Create Detection Endpoint**

```python
from datetime import datetime

class DetectionRequest(BaseModel):
    file_id: str

class DetectionResponse(BaseModel):
    file_id: str
    timestamp: str
    predictions: List[Dict]
    top_prediction: Dict
    processing_time_ms: float

@app.post("/detect", response_model=DetectionResponse)
async def detect_disease(file: UploadFile = File(...)):
    start_time = datetime.now()

    try:
        # Read and preprocess image
        contents = await file.read()
        image = Image.open(io.BytesIO(contents))
        preprocessed = preprocess_image(image)

        # Run inference
        if model is None:
            raise HTTPException(
                status_code=503,
                detail="Model not loaded"
            )

        result = model.predict(preprocessed)

        # Calculate processing time
        processing_time = (datetime.now() - start_time).total_seconds() * 1000

        return DetectionResponse(
            file_id=str(uuid.uuid4()),
            timestamp=datetime.now().isoformat(),
            predictions=result["predictions"],
            top_prediction=result["top_prediction"],
            processing_time_ms=processing_time
        )

    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Detection failed: {str(e)}"
        )
```

**Verification**:
```bash
curl -X POST -F "file=@tomato_leaf.jpg" http://localhost:8000/detect
# Verify predictions are returned with confidence scores
# Check processing time is reasonable
```

### 4. Error Handling (Week 6)

**Exercise 4.1: Implement Comprehensive Error Handling**

```python
from fastapi import Request, status
from fastapi.responses import JSONResponse
from fastapi.exceptions import RequestValidationError

class APIError(Exception):
    def __init__(self, status_code: int, detail: str):
        self.status_code = status_code
        self.detail = detail

@app.exception_handler(APIError)
async def api_error_handler(request: Request, exc: APIError):
    return JSONResponse(
        status_code=exc.status_code,
        content={
            "error": exc.detail,
            "path": str(request.url),
            "timestamp": datetime.now().isoformat()
        }
    )

@app.exception_handler(RequestValidationError)
async def validation_error_handler(request: Request, exc: RequestValidationError):
    return JSONResponse(
        status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
        content={
            "error": "Validation error",
            "details": exc.errors(),
            "path": str(request.url)
        }
    )

@app.exception_handler(Exception)
async def general_error_handler(request: Request, exc: Exception):
    return JSONResponse(
        status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
        content={
            "error": "Internal server error",
            "message": str(exc),
            "path": str(request.url)
        }
    )
```

**Verification**:
```bash
# Test various error scenarios
curl http://localhost:8000/invalid_endpoint  # 404
curl -X POST http://localhost:8000/detect  # 422 (missing file)
# Trigger internal error and verify response format
```

**Exercise 4.2: Input Validation**

```python
from pydantic import BaseModel, validator, Field

class DetectionSettings(BaseModel):
    confidence_threshold: float = Field(0.5, ge=0.0, le=1.0)
    max_predictions: int = Field(3, ge=1, le=10)

    @validator('confidence_threshold')
    def validate_threshold(cls, v):
        if not 0.0 <= v <= 1.0:
            raise ValueError('Threshold must be between 0 and 1')
        return v

@app.post("/detect/advanced")
async def detect_with_settings(
    file: UploadFile = File(...),
    settings: DetectionSettings = None
):
    if settings is None:
        settings = DetectionSettings()

    # Process with custom settings...
    pass
```

**Verification**:
```bash
# Test with valid settings
curl -X POST -F "file=@leaf.jpg" \
  -F "settings={\"confidence_threshold\":0.7,\"max_predictions\":5}" \
  http://localhost:8000/detect/advanced

# Test with invalid settings
curl -X POST -F "file=@leaf.jpg" \
  -F "settings={\"confidence_threshold\":1.5}" \
  http://localhost:8000/detect/advanced
```

### 5. Testing with Postman/cURL (Weeks 4-6)

**Exercise 5.1: Create Postman Collection**

Create a Postman collection with the following requests:

1. **Health Check**
   - Method: GET
   - URL: `http://localhost:8000/health`
   - Expected: 200 OK with health status

2. **Get All Diseases**
   - Method: GET
   - URL: `http://localhost:8000/diseases`
   - Expected: 200 OK with array of diseases

3. **Upload Image**
   - Method: POST
   - URL: `http://localhost:8000/upload`
   - Body: form-data with file
   - Expected: 200 OK with file info

4. **Detect Disease**
   - Method: POST
   - URL: `http://localhost:8000/detect`
   - Body: form-data with image file
   - Expected: 200 OK with predictions

**Verification**: Save collection and verify all requests work

**Exercise 5.2: cURL Test Scripts**

```bash
#!/bin/bash
# test_api.sh

BASE_URL="http://localhost:8000"

echo "Testing Health Check..."
curl -X GET "$BASE_URL/health"
echo -e "\n"

echo "Testing Disease List..."
curl -X GET "$BASE_URL/diseases"
echo -e "\n"

echo "Testing Image Upload..."
curl -X POST -F "file=@test_images/tomato_healthy.jpg" "$BASE_URL/upload"
echo -e "\n"

echo "Testing Disease Detection..."
curl -X POST -F "file=@test_images/tomato_blight.jpg" "$BASE_URL/detect"
echo -e "\n"

echo "Testing Error Handling..."
curl -X POST -F "file=@test.txt" "$BASE_URL/detect"
echo -e "\n"

echo "All tests completed!"
```

**Verification**:
```bash
chmod +x test_api.sh
./test_api.sh
# Review output, verify all tests pass
```

**Exercise 5.3: Performance Testing**

```python
import time
from locust import HttpUser, task, between

class LeafGuardUser(HttpUser):
    wait_time = between(1, 3)

    @task(3)
    def detect_disease(self):
        with open("test_images/sample.jpg", "rb") as f:
            self.client.post(
                "/detect",
                files={"file": f}
            )

    @task(1)
    def get_diseases(self):
        self.client.get("/diseases")

    @task(1)
    def health_check(self):
        self.client.get("/health")
```

**Verification**:
```bash
locust -f locustfile.py
# Open browser to http://localhost:8089
# Run load test and analyze results
```

## Complete Example: Full API Implementation

```python
# complete_api.py
from fastapi import FastAPI, File, UploadFile, HTTPException, status
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Dict
import tensorflow as tf
from PIL import Image
import numpy as np
import io
import uuid
from datetime import datetime
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize FastAPI
app = FastAPI(
    title="LeafGuard AI API",
    description="Plant disease detection API",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Models
class DetectionResponse(BaseModel):
    detection_id: str
    timestamp: str
    predictions: List[Dict]
    top_prediction: Dict
    processing_time_ms: float

# Global model instance
model_instance = None

@app.on_event("startup")
async def startup_event():
    global model_instance
    logger.info("Loading ML model...")
    # Initialize model here
    logger.info("Model loaded successfully")

@app.get("/")
async def root():
    return {
        "message": "LeafGuard AI API",
        "version": "1.0.0",
        "endpoints": ["/health", "/diseases", "/detect"]
    }

@app.get("/health")
async def health_check():
    return {
        "status": "healthy",
        "model_loaded": model_instance is not None,
        "timestamp": datetime.now().isoformat()
    }

@app.post("/detect", response_model=DetectionResponse)
async def detect_disease(file: UploadFile = File(...)):
    start_time = datetime.now()

    # Implementation here...

    return DetectionResponse(
        detection_id=str(uuid.uuid4()),
        timestamp=datetime.now().isoformat(),
        predictions=[],  # Add actual predictions
        top_prediction={},  # Add top prediction
        processing_time_ms=(datetime.now() - start_time).total_seconds() * 1000
    )

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
```

## How to Complete Exercises

1. **Set up environment**: Create virtual environment and install dependencies
2. **Implement incrementally**: Start with basic endpoints, add features gradually
3. **Test continuously**: Test each endpoint after implementation
4. **Handle errors**: Add proper error handling and validation
5. **Document**: Add docstrings and comments
6. **Performance test**: Ensure API can handle expected load

## Testing Checklist

- [ ] All endpoints return correct status codes
- [ ] Request validation works properly
- [ ] File upload handles various image formats
- [ ] Image size limits are enforced
- [ ] Model inference returns predictions
- [ ] Error responses are properly formatted
- [ ] CORS is configured correctly
- [ ] API documentation is accessible at /docs
- [ ] Performance meets requirements (< 2s per prediction)

## Common Issues and Solutions

**Issue**: Model fails to load
**Solution**: Check file paths, verify model format, ensure TensorFlow is installed

**Issue**: Out of memory errors
**Solution**: Limit concurrent requests, optimize image processing, use batch processing

**Issue**: Slow inference times
**Solution**: Use TFLite instead of full TensorFlow, optimize preprocessing, consider GPU

**Issue**: CORS errors from Android app
**Solution**: Configure CORS middleware with correct origins

## Resources

- [FastAPI Documentation](https://fastapi.tiangolo.com/)
- [TensorFlow Lite Guide](https://www.tensorflow.org/lite)
- [Postman Learning Center](https://learning.postman.com/)
- [cURL Tutorial](https://curl.se/docs/tutorial.html)

## Submission

1. Ensure all endpoints are functional
2. Include Postman collection export
3. Provide test scripts
4. Document API in README
5. Add performance test results
6. Update reflection journal
