# LeafGuard AI - Backend API Setup Guide

## Overview
This guide provides complete instructions for setting up and running the FastAPI backend server for LeafGuard AI. The backend provides REST API endpoints for plant disease detection using a machine learning model, serving as an alternative or complement to on-device inference.

## Prerequisites

- **Python**: Version 3.8 - 3.11 (recommended: 3.10)
- **pip**: Python package manager (comes with Python)
- **Git**: For cloning dependencies
- **8GB+ RAM**: For running ML models
- **CUDA** (optional): For GPU acceleration with TensorFlow/PyTorch

## Python Version Check

```bash
# Check Python version
python --version
# or
python3 --version

# Should output: Python 3.8.x or higher
```

## Project Structure

```
backend-api/
├── main.py                      # FastAPI application entry point
├── requirements.txt             # Python dependencies
├── .env                         # Environment variables (create this)
├── models/
│   ├── model_loader.py          # Load and cache ML model
│   └── disease_classifier.py   # Model inference logic
├── utils/
│   ├── image_processing.py      # Image preprocessing utilities
│   ├── response_formatter.py    # API response formatting
│   └── validators.py            # Input validation
├── config/
│   ├── settings.py              # Configuration management
│   └── labels.py                # Disease labels and metadata
├── api/
│   ├── routes/
│   │   ├── health.py            # Health check endpoint
│   │   ├── predict.py           # Prediction endpoints
│   │   └── info.py              # Model info endpoints
│   └── middleware/
│       └── cors.py              # CORS configuration
├── tests/
│   ├── test_api.py              # API endpoint tests
│   └── test_model.py            # Model inference tests
└── README.md                    # This file
```

## Quick Start

### 1. Create Virtual Environment

**Linux/Mac**:
```bash
# Navigate to backend-api directory
cd backend-api

# Create virtual environment
python3 -m venv venv

# Activate virtual environment
source venv/bin/activate
```

**Windows**:
```bash
# Navigate to backend-api directory
cd backend-api

# Create virtual environment
python -m venv venv

# Activate virtual environment
venv\Scripts\activate
```

You should see `(venv)` prefix in your terminal.

### 2. Install Dependencies

```bash
# Upgrade pip
pip install --upgrade pip

# Install dependencies
pip install -r requirements.txt
```

### 3. Create `requirements.txt`

Create this file with the following content:

```txt
# FastAPI and Server
fastapi==0.109.0
uvicorn[standard]==0.27.0
python-multipart==0.0.6

# Machine Learning
tensorflow==2.15.0
# OR for PyTorch users:
# torch==2.1.2
# torchvision==0.16.2

# Image Processing
Pillow==10.2.0
numpy==1.24.3

# Utilities
python-dotenv==1.0.0
pydantic==2.5.3
pydantic-settings==2.1.0

# Optional: GPU support (TensorFlow)
# tensorflow-gpu==2.15.0

# Development and Testing
pytest==7.4.3
httpx==0.26.0
```

### 4. Download or Place ML Model

Place your TensorFlow Lite model file:
```bash
# Create models directory
mkdir -p models/saved_model

# Copy your model file
# Place: models/saved_model/model.tflite
# Place: models/saved_model/labels.txt
```

### 5. Create Environment Variables

Create `.env` file in `backend-api/` directory:

```bash
# .env file
MODEL_PATH=models/saved_model/model.tflite
LABELS_PATH=models/saved_model/labels.txt
HOST=0.0.0.0
PORT=8000
RELOAD=True
LOG_LEVEL=info
MAX_IMAGE_SIZE=10485760
ALLOWED_ORIGINS=*
```

**Important**: Add `.env` to `.gitignore` to avoid committing sensitive data:
```bash
echo ".env" >> .gitignore
```

## Implementation Guide

### Minimal `main.py` Structure

```python
from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from PIL import Image
import numpy as np
import tensorflow as tf
import io
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize FastAPI app
app = FastAPI(
    title="LeafGuard AI API",
    description="Plant Disease Detection API",
    version="1.0.0"
)

# CORS Configuration
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # In production, specify actual origins
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Global variable to store loaded model
model = None
labels = []

@app.on_event("startup")
async def load_model():
    """Load TensorFlow Lite model on startup"""
    global model, labels
    try:
        # Load TFLite model
        model = tf.lite.Interpreter(model_path="models/saved_model/model.tflite")
        model.allocate_tensors()

        # Load labels
        with open("models/saved_model/labels.txt", "r") as f:
            labels = [line.strip() for line in f.readlines()]

        logger.info("Model loaded successfully")
    except Exception as e:
        logger.error(f"Failed to load model: {e}")
        raise

@app.get("/")
async def root():
    """Root endpoint"""
    return {
        "message": "LeafGuard AI API",
        "version": "1.0.0",
        "status": "running"
    }

@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "model_loaded": model is not None
    }

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    """
    Predict plant disease from uploaded image

    Args:
        file: Image file (JPEG, PNG)

    Returns:
        JSON with prediction results
    """
    if model is None:
        raise HTTPException(status_code=503, detail="Model not loaded")

    # Validate file type
    if file.content_type not in ["image/jpeg", "image/png", "image/jpg"]:
        raise HTTPException(status_code=400, detail="Invalid file type. Use JPEG or PNG")

    try:
        # Read image
        contents = await file.read()
        image = Image.open(io.BytesIO(contents))

        # Preprocess image
        image = image.convert("RGB")
        image = image.resize((224, 224))  # Adjust size based on your model
        image_array = np.array(image, dtype=np.float32)
        image_array = image_array / 255.0  # Normalize to [0, 1]
        image_array = np.expand_dims(image_array, axis=0)  # Add batch dimension

        # Get input and output details
        input_details = model.get_input_details()
        output_details = model.get_output_details()

        # Set input tensor
        model.set_tensor(input_details[0]['index'], image_array)

        # Run inference
        model.invoke()

        # Get output tensor
        output_data = model.get_tensor(output_details[0]['index'])
        predictions = output_data[0]

        # Get top prediction
        top_index = np.argmax(predictions)
        confidence = float(predictions[top_index])
        disease = labels[top_index]

        # Get top 3 predictions
        top_3_indices = np.argsort(predictions)[-3:][::-1]
        top_3_predictions = [
            {
                "disease": labels[i],
                "confidence": float(predictions[i])
            }
            for i in top_3_indices
        ]

        return {
            "success": True,
            "prediction": {
                "disease": disease,
                "confidence": confidence
            },
            "top_3": top_3_predictions
        }

    except Exception as e:
        logger.error(f"Prediction error: {e}")
        raise HTTPException(status_code=500, detail=f"Prediction failed: {str(e)}")

@app.get("/model/info")
async def model_info():
    """Get model information"""
    if model is None:
        raise HTTPException(status_code=503, detail="Model not loaded")

    input_details = model.get_input_details()
    output_details = model.get_output_details()

    return {
        "model_type": "TensorFlow Lite",
        "input_shape": input_details[0]['shape'].tolist(),
        "output_shape": output_details[0]['shape'].tolist(),
        "num_classes": len(labels),
        "labels": labels
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True
    )
```

### `models/model_loader.py` Structure

```python
import tensorflow as tf
import logging
from pathlib import Path

logger = logging.getLogger(__name__)

class ModelLoader:
    def __init__(self, model_path: str, labels_path: str):
        self.model_path = model_path
        self.labels_path = labels_path
        self.interpreter = None
        self.labels = []

    def load(self):
        """Load TFLite model and labels"""
        try:
            # Check if files exist
            if not Path(self.model_path).exists():
                raise FileNotFoundError(f"Model file not found: {self.model_path}")

            if not Path(self.labels_path).exists():
                raise FileNotFoundError(f"Labels file not found: {self.labels_path}")

            # Load model
            self.interpreter = tf.lite.Interpreter(model_path=self.model_path)
            self.interpreter.allocate_tensors()

            # Load labels
            with open(self.labels_path, 'r') as f:
                self.labels = [line.strip() for line in f.readlines()]

            logger.info(f"Model loaded: {len(self.labels)} classes")
            return True

        except Exception as e:
            logger.error(f"Failed to load model: {e}")
            return False

    def get_input_details(self):
        """Get model input details"""
        return self.interpreter.get_input_details()

    def get_output_details(self):
        """Get model output details"""
        return self.interpreter.get_output_details()
```

### `utils/image_processing.py` Structure

```python
from PIL import Image
import numpy as np
from typing import Tuple

def preprocess_image(
    image: Image.Image,
    target_size: Tuple[int, int] = (224, 224),
    normalize: bool = True
) -> np.ndarray:
    """
    Preprocess image for model inference

    Args:
        image: PIL Image
        target_size: Target size (width, height)
        normalize: Whether to normalize to [0, 1]

    Returns:
        Preprocessed numpy array
    """
    # Convert to RGB
    image = image.convert('RGB')

    # Resize
    image = image.resize(target_size)

    # Convert to numpy array
    image_array = np.array(image, dtype=np.float32)

    # Normalize
    if normalize:
        image_array = image_array / 255.0

    # Add batch dimension
    image_array = np.expand_dims(image_array, axis=0)

    return image_array

def validate_image(image: Image.Image, max_size: int = 10485760) -> bool:
    """
    Validate image

    Args:
        image: PIL Image
        max_size: Maximum file size in bytes

    Returns:
        True if valid
    """
    # Check dimensions
    width, height = image.size
    if width < 50 or height < 50:
        return False

    if width > 4096 or height > 4096:
        return False

    return True
```

## Running the Server

### Development Mode

```bash
# Activate virtual environment
source venv/bin/activate  # Linux/Mac
# or
venv\Scripts\activate  # Windows

# Run with auto-reload
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

### Production Mode

```bash
# Run with multiple workers
uvicorn main:app --host 0.0.0.0 --port 8000 --workers 4
```

### Access API Documentation

Once the server is running:
- **Interactive API Docs (Swagger)**: http://localhost:8000/docs
- **Alternative API Docs (ReDoc)**: http://localhost:8000/redoc
- **OpenAPI JSON**: http://localhost:8000/openapi.json

## Testing the API

### Using cURL

```bash
# Health check
curl http://localhost:8000/health

# Predict from image
curl -X POST "http://localhost:8000/predict" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/plant-image.jpg"

# Get model info
curl http://localhost:8000/model/info
```

### Using Python Requests

```python
import requests

# Test prediction
url = "http://localhost:8000/predict"
files = {"file": open("test_image.jpg", "rb")}
response = requests.post(url, files=files)
print(response.json())
```

### Using Postman

1. Create new POST request
2. URL: `http://localhost:8000/predict`
3. Body → form-data
4. Key: `file` (type: File)
5. Value: Select image file
6. Send

## Connecting Android App to Backend

### Same WiFi Network Setup

1. **Find your computer's IP address**:

   **Linux/Mac**:
   ```bash
   ifconfig | grep "inet " | grep -v 127.0.0.1
   # or
   ip addr show | grep "inet " | grep -v 127.0.0.1
   ```

   **Windows**:
   ```bash
   ipconfig
   ```

   Look for IPv4 address (e.g., 192.168.1.105)

2. **Start server on all interfaces**:
   ```bash
   uvicorn main:app --host 0.0.0.0 --port 8000
   ```

3. **Update Android app** to use your computer's IP:
   ```java
   // In Android app's ApiClient.java
   private static final String BASE_URL = "http://192.168.1.105:8000/";
   ```

4. **Test connection from phone**:
   - Open browser on phone
   - Navigate to: `http://192.168.1.105:8000/health`
   - Should see: `{"status":"healthy","model_loaded":true}`

### Using Android Emulator

The Android emulator cannot access `localhost` or `127.0.0.1` directly.

**Use special alias**: `10.0.2.2`

```java
// In Android app
private static final String BASE_URL = "http://10.0.2.2:8000/";
```

This routes to your host machine's localhost.

### Firewall Configuration

If connection fails, allow port 8000:

**Linux (UFW)**:
```bash
sudo ufw allow 8000
```

**Windows Firewall**:
- Windows Security → Firewall → Advanced Settings
- Inbound Rules → New Rule
- Port → TCP → 8000 → Allow

**Mac**:
- System Preferences → Security & Privacy → Firewall
- Firewall Options → Add application (Python)

## CORS Configuration

For production, specify exact origins instead of `"*"`:

```python
from fastapi.middleware.cors import CORSMiddleware

app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "http://localhost:3000",
        "https://yourdomain.com"
    ],
    allow_credentials=True,
    allow_methods=["GET", "POST"],
    allow_headers=["*"],
)
```

## Environment Configuration

### Using `pydantic-settings` for Config Management

Create `config/settings.py`:

```python
from pydantic_settings import BaseSettings
from typing import List

class Settings(BaseSettings):
    MODEL_PATH: str = "models/saved_model/model.tflite"
    LABELS_PATH: str = "models/saved_model/labels.txt"
    HOST: str = "0.0.0.0"
    PORT: int = 8000
    RELOAD: bool = False
    LOG_LEVEL: str = "info"
    MAX_IMAGE_SIZE: int = 10485760  # 10MB
    ALLOWED_ORIGINS: List[str] = ["*"]

    class Config:
        env_file = ".env"
        case_sensitive = True

settings = Settings()
```

Use in `main.py`:
```python
from config.settings import settings

model = tf.lite.Interpreter(model_path=settings.MODEL_PATH)
```

## Deployment Considerations

### Using Gunicorn (Production WSGI Server)

```bash
# Install gunicorn
pip install gunicorn

# Run with Gunicorn
gunicorn main:app -w 4 -k uvicorn.workers.UvicornWorker --bind 0.0.0.0:8000
```

### Docker Deployment

Create `Dockerfile`:

```dockerfile
FROM python:3.10-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8000

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
```

Build and run:
```bash
docker build -t leafguard-api .
docker run -p 8000:8000 leafguard-api
```

### Cloud Deployment Options

1. **Heroku**: Simple deployment with Procfile
2. **AWS EC2**: Full control, requires setup
3. **Google Cloud Run**: Serverless containers
4. **Azure App Service**: Managed platform
5. **Railway.app**: Easy deployment with GitHub integration

## Common Issues and Solutions

### 1. Port Already in Use

**Error**: `Address already in use`

**Solution**:
```bash
# Find process using port 8000
lsof -i :8000  # Linux/Mac
netstat -ano | findstr :8000  # Windows

# Kill process
kill -9 <PID>  # Linux/Mac
taskkill /PID <PID> /F  # Windows

# Or use different port
uvicorn main:app --port 8001
```

### 2. Module Not Found

**Error**: `ModuleNotFoundError: No module named 'fastapi'`

**Solution**:
```bash
# Ensure virtual environment is activated
source venv/bin/activate

# Reinstall dependencies
pip install -r requirements.txt
```

### 3. Model File Not Found

**Error**: `FileNotFoundError: model.tflite`

**Solution**:
- Check file path in code matches actual location
- Use absolute paths or ensure working directory is correct
- Verify file exists: `ls models/saved_model/model.tflite`

### 4. TensorFlow Not Installing

**Error**: TensorFlow installation fails

**Solution**:
```bash
# For Mac M1/M2 (Apple Silicon)
pip install tensorflow-macos
pip install tensorflow-metal

# For older Python versions
pip install tensorflow==2.13.0

# For CPU-only
pip install tensorflow-cpu
```

### 5. CORS Error from Android App

**Error**: Blocked by CORS policy

**Solution**:
- Verify CORS middleware is added
- Check `allow_origins` includes your client origin
- For development, use `allow_origins=["*"]`

### 6. Large Image Upload Fails

**Error**: Request entity too large

**Solution**:
```python
# Add size limit to FastAPI
from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse

@app.exception_handler(RequestEntityTooLarge)
async def request_entity_too_large_handler(request, exc):
    return JSONResponse(
        status_code=413,
        content={"error": "Image too large. Max size: 10MB"}
    )

# Limit in upload endpoint
@app.post("/predict")
async def predict(file: UploadFile = File(..., max_length=10485760)):
    ...
```

### 7. Slow Inference

**Issue**: Model takes too long to respond

**Solutions**:
- Use GPU acceleration (TensorFlow GPU)
- Resize images before sending to API
- Use model quantization (reduces size/latency)
- Implement caching for repeated requests
- Consider using ONNX Runtime for faster inference

## Performance Optimization

### 1. Model Loading

Load model once at startup, not per request (already in example above).

### 2. Image Caching

```python
from functools import lru_cache

@lru_cache(maxsize=100)
def get_cached_prediction(image_hash: str):
    # Return cached result
    pass
```

### 3. Async Processing

```python
import asyncio
from concurrent.futures import ThreadPoolExecutor

executor = ThreadPoolExecutor(max_workers=4)

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    loop = asyncio.get_event_loop()
    result = await loop.run_in_executor(executor, run_inference, image)
    return result
```

### 4. Request Validation

```python
from pydantic import BaseModel, validator

class PredictionResponse(BaseModel):
    success: bool
    prediction: dict

    @validator('prediction')
    def validate_prediction(cls, v):
        required_keys = ['disease', 'confidence']
        if not all(key in v for key in required_keys):
            raise ValueError('Invalid prediction format')
        return v
```

## Testing

### Unit Tests with Pytest

Create `tests/test_api.py`:

```python
from fastapi.testclient import TestClient
from main import app

client = TestClient(app)

def test_health_check():
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "healthy"

def test_root():
    response = client.get("/")
    assert response.status_code == 200
    assert "LeafGuard AI API" in response.json()["message"]

def test_predict_with_valid_image():
    with open("test_image.jpg", "rb") as f:
        response = client.post(
            "/predict",
            files={"file": ("test.jpg", f, "image/jpeg")}
        )
    assert response.status_code == 200
    assert "prediction" in response.json()
```

Run tests:
```bash
pytest tests/ -v
```

## Logging

### Configure Logging

```python
import logging
from datetime import datetime

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler(f'logs/api_{datetime.now().strftime("%Y%m%d")}.log'),
        logging.StreamHandler()
    ]
)

logger = logging.getLogger(__name__)

# Use in code
logger.info("Processing image")
logger.error("Failed to load model", exc_info=True)
```

## Monitoring

### Basic Request Logging

```python
import time
from fastapi import Request

@app.middleware("http")
async def log_requests(request: Request, call_next):
    start_time = time.time()
    response = await call_next(request)
    process_time = time.time() - start_time
    logger.info(f"{request.method} {request.url.path} - {response.status_code} - {process_time:.2f}s")
    return response
```

## Next Steps

1. Implement the basic `main.py` with health check and predict endpoints
2. Test locally with sample images
3. Connect Android app to backend
4. Add error handling and validation
5. Implement logging and monitoring
6. Optimize for performance
7. Consider deployment options

---

**Note**: This backend is optional for the LeafGuard AI project. Students can implement on-device inference first (Weeks 5-7) and add backend integration later (Weeks 9-10).
