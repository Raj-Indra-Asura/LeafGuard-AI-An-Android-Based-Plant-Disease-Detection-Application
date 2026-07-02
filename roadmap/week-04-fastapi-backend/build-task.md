# Week 04 Build Task: FastAPI Backend with /predict Endpoint

## Task Overview

**Objective:** Build a production-ready FastAPI backend for LeafGuard AI that accepts image uploads via multipart form-data and returns dummy disease predictions in JSON (JavaScript Object Notation) format. The backend must be accessible from your Android emulator via `http://10.0.2.2:8000/` (or a physical phone on the same Wi-Fi using your laptop LAN IP).

**Time Estimate:** 10-15 hours across 7 days

**Prerequisites:**
- Python 3.8+ installed
- Week 03 camera integration completed
- Test leaf images available

---

## Deliverables Checklist

By the end of this build task, you must have:

- [ ] FastAPI project with proper folder structure
- [ ] Virtual environment with requirements.txt
- [ ] Working /predict endpoint accepting image uploads
- [ ] Dummy JSON responses with disease predictions
- [ ] Input validation (file type, size)
- [ ] Error handling (400, 500 status codes)
- [ ] CORS (Cross-Origin Resource Sharing) middleware configured
- [ ] Server running on local network (--host 0.0.0.0)
- [ ] Postman collection with test requests
- [ ] Documentation (README.md, API docs)
- [ ] Network setup documented (IP address, firewall)
- [ ] Git repository with meaningful commits
- [ ] Evidence folder with screenshots

---

## Step-by-Step Implementation

### Step 1: Project Setup (Day 1)

**Create project structure:**

```bash
mkdir -p backend-api
cd backend-api

# Real single-file backend layout
touch main.py config.py model_loader.py requirements.txt README.md .gitignore
```

**Initialize virtual environment:**

```bash
python -m venv venv
# On macOS/Linux, use python3 -m venv venv if python points to Python 2
source venv/bin/activate  # macOS/Linux
# OR
venv\Scripts\activate     # Windows

# If running from the repo root, use: pip install -r backend-api/requirements.txt
pip install fastapi uvicorn python-multipart
pip freeze > requirements.txt
# pip and uvicorn commands are identical on Windows and macOS/Linux
```

**Create .gitignore:**

```
venv/
__pycache__/
*.pyc
*.pyo
.env
uploads/*.jpg
uploads/*.jpeg
uploads/*.png
!uploads/.gitkeep
.DS_Store
.idea/
.vscode/
```

**Git initialization:**

```bash
git init
git add .
git commit -m "Week 04: Initialize FastAPI backend project structure"
```

---

### Step 2: Define the Response Model (Day 2)

**File: `main.py` (near the top)**

```python
from pydantic import BaseModel, Field

class PredictionResult(BaseModel):
    """Response model for successful disease prediction"""
    disease: str = Field(..., min_length=1, max_length=100,
                        description="Name of the detected disease")
    confidence: float = Field(..., ge=0.0, le=1.0,
                             description="Model confidence score (0.0 to 1.0)")
    symptoms: str = Field(..., min_length=10,
                         description="Visible symptoms of the disease")
    treatment: str = Field(..., min_length=10,
                          description="Recommended treatment steps")
    prevention: str = Field(..., min_length=10,
                           description="Prevention strategies")

    class Config:
        json_schema_extra = {
            "example": {
                "disease": "Tomato Early Blight",
                "confidence": 0.92,
                "symptoms": "Dark brown spots with concentric rings on lower leaves",
                "treatment": "Apply fungicide containing chlorothalonil or mancozeb",
                "prevention": "Rotate crops annually, avoid overhead watering"
            }
        }
```

**Test your models:**

```bash
python
>>> from main import PredictionResult
>>> response = PredictionResult(
...     disease="Test Disease",
...     confidence=0.9,
...     symptoms="Test symptoms",
...     treatment="Test treatment",
...     prevention="Test prevention"
... )
>>> print(response.model_dump_json())
```

---

### Step 3: Create Validation Utilities (Day 2)

**Validation helper in `main.py`**

```python
from fastapi import UploadFile
from typing import Optional

# Constants
ALLOWED_CONTENT_TYPES = ["image/jpeg", "image/png"]
ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png"}
MAX_FILE_SIZE = 10 * 1024 * 1024  # 10 MB

def validate_image(file: UploadFile) -> Optional[str]:
    """
    Validate uploaded image file.

    Args:
        file: FastAPI UploadFile object

    Returns:
        Error message string if validation fails, None if valid
    """

    # Check content type
    if file.content_type not in ALLOWED_CONTENT_TYPES:
        return (
            f"Invalid content type: {file.content_type}. "
            f"Allowed types: {', '.join(ALLOWED_CONTENT_TYPES)}"
        )

    # Check file extension
    if file.filename:
        extension = file.filename.split(".")[-1].lower()
        if extension not in ALLOWED_EXTENSIONS:
            return (
                f"Invalid file extension: .{extension}. "
                f"Allowed extensions: {', '.join(ALLOWED_EXTENSIONS)}"
            )

    return None  # Valid

async def validate_file_size(file: UploadFile) -> tuple[bytes, Optional[str]]:
    """
    Validate file size by reading contents.

    Args:
        file: FastAPI UploadFile object

    Returns:
        Tuple of (file_contents, error_message)
        error_message is None if valid
    """
    contents = await file.read()
    file_size = len(contents)

    if file_size > MAX_FILE_SIZE:
        error = (
            f"File too large: {file_size} bytes. "
            f"Maximum allowed: {MAX_FILE_SIZE} bytes ({MAX_FILE_SIZE // (1024*1024)} MB)"
        )
        return contents, error

    return contents, None
```

---

### Step 4: Implement /predict Endpoint (Day 3)

**File: `main.py` (`/predict` endpoint section)**

```python
from fastapi import FastAPI, File, UploadFile, HTTPException
from pydantic import BaseModel
import random
from datetime import datetime

app = FastAPI(title="LeafGuard AI Backend")

class PredictionResult(BaseModel):
    disease: str
    confidence: float
    symptoms: str
    treatment: str
    prevention: str

# Dummy disease database
DISEASE_DATABASE = [
    {
        "disease": "Tomato Early Blight",
        "symptoms": "Dark brown spots with concentric rings on lower leaves. Yellowing around spots. Older leaves affected first.",
        "treatment": "Apply fungicide containing chlorothalonil or mancozeb every 7-10 days. Remove and destroy infected leaves.",
        "prevention": "Rotate crops every 2-3 years. Avoid overhead watering. Mulch around plants to prevent soil splash."
    },
    {
        "disease": "Potato Late Blight",
        "symptoms": "Water-soaked spots on leaves that quickly turn brown. White fuzzy growth on leaf undersides in humid conditions.",
        "treatment": "Apply copper-based fungicide immediately. Remove and burn infected plants. Do not compost.",
        "prevention": "Plant certified disease-free seed potatoes. Ensure good air circulation. Avoid watering late in the day."
    },
    {
        "disease": "Corn Common Rust",
        "symptoms": "Small, circular to elongated reddish-brown pustules on both leaf surfaces. Pustules rupture releasing rust-colored spores.",
        "treatment": "Usually not economically damaging. Apply fungicide if severe. Remove heavily infected leaves.",
        "prevention": "Plant resistant hybrids. Maintain adequate plant spacing. Monitor regularly for early detection."
    },
    {
        "disease": "Apple Scab",
        "symptoms": "Olive-green to brown spots on leaves and fruit. Velvety texture. Leaves may curl and drop prematurely.",
        "treatment": "Apply fungicide at green tip stage. Continue applications every 7-14 days during wet weather.",
        "prevention": "Rake and destroy fallen leaves. Prune to improve air circulation. Choose resistant varieties."
    },
    {
        "disease": "Grape Black Rot",
        "symptoms": "Circular reddish-brown spots on leaves. Infected berries turn brown, shrivel into hard black mummies.",
        "treatment": "Apply fungicide from bud break through 6 weeks after bloom. Remove mummified berries and infected shoots.",
        "prevention": "Prune for good air circulation. Remove and destroy fallen leaves and mummies. Avoid overhead irrigation."
    }
]

@app.post("/predict", response_model=PredictionResult)
async def predict(image: UploadFile = File(...)):
    """
    Upload a leaf image and receive disease prediction.

    This is a dummy endpoint that returns random predictions.
    Week 09 will integrate the real trained model.

    **Request:**
    - Content-Type: multipart/form-data
    - Body: multipart part named `image` (image/jpeg or image/png)

    **Response:**
    - Disease name, confidence score, symptoms, treatment, prevention
    - Disease name, confidence score, symptoms, treatment, prevention
    """

    # Validate file type and extension
    validation_error = validate_image(image)
    if validation_error:
        raise HTTPException(status_code=400, detail=validation_error)

    # Validate file size
    try:
        contents, size_error = await validate_file_size(image)
        if size_error:
            raise HTTPException(status_code=400, detail=size_error)
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Failed to read uploaded file: {str(e)}"
        )

    # Generate dummy prediction (random disease from database)
    disease_data = random.choice(DISEASE_DATABASE)
    confidence = round(random.uniform(0.75, 0.98), 2)  # Random confidence 75-98%

    # Create response
    response = PredictionResult(
        disease=disease_data["disease"],
        confidence=confidence,
        symptoms=disease_data["symptoms"],
        treatment=disease_data["treatment"],
        prevention=disease_data["prevention"]
    )

    return response
```

---

### Step 5: Create Main Application (Day 3)

**File: `main.py`**

```python
from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI(
    title="LeafGuard AI Backend",
    description="REST (REpresentational State Transfer) API for plant disease detection using machine learning",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc"
)

# CORS (Cross-Origin Resource Sharing) middleware - allows Android app to make requests
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # In production, specify exact Android app origin
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# The /predict endpoint is defined directly in this main.py file.

@app.get("/", tags=["Health Check"])
def root():
    """
    Health check endpoint.

    Returns API status and version information.
    """
    return {
        "status": "online",
        "message": "LeafGuard AI Backend is running",
        "version": "1.0.0",
        "docs_url": "/docs"
    }

@app.get("/health", tags=["Health Check"])
def health_check():
    """
    Detailed health check.

    Returns server status and configuration info.
    """
    return {
        "status": "healthy",
        "api_version": "1.0.0",
        "endpoints": {
            "predict": "/predict",
            "docs": "/docs",
            "redoc": "/redoc"
        }
    }
```

**Test the server:**

```bash
uvicorn main:app --reload
```

Open browser: http://localhost:8000 and http://localhost:8000/docs

---

### Step 6: Local Network Configuration (Day 4)

**Find your local IP address:**

**Windows:**
```cmd
ipconfig
```

**Mac/Linux:**
```bash
ifconfig
# or
ip addr show
```

For the Android emulator, the base URL is `http://10.0.2.2:8000/`. For a physical phone, note down the laptop IP address (e.g., 192.168.1.100).

**Run server for network access:**

```bash
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

**Test from phone:**
1. Connect phone to same Wi-Fi network
2. Open browser in the Android emulator: http://10.0.2.2:8000
3. For a physical phone, open: http://YOUR_IP:8000
4. Should see health check response

Expected result: a JSON health response appears in the browser.

**Configure firewall (if needed):**
- Windows: Allow Python through Windows Firewall
- Mac: System Preferences → Security → Firewall → Allow Python
- Linux: `sudo ufw allow 8000/tcp`

**Create documentation file: `NETWORK_SETUP.md`**

````markdown
# Local Network Setup for LeafGuard Backend

## Configuration

### Laptop (Server)
- **OS:** [Your Operating System]
- **Wi-Fi Network:** [Network Name]
- **Local IP Address:** 192.168.x.x
- **Server Port:** 8000
- **Emulator Base URL:** http://10.0.2.2:8000
- **Physical Phone Base URL:** http://192.168.x.x:8000

### Android Phone (Client)
- **Device Model:** [Your Phone Model]
- **Android Version:** [Version Number]
- **Wi-Fi Network:** [Same Network Name as Laptop]
- **Test URLs:**
  - Emulator Health Check: http://10.0.2.2:8000/
  - Emulator API Docs: http://10.0.2.2:8000/docs
  - Physical Phone Health Check: http://192.168.x.x:8000/

## Running the Server

```bash
# Activate virtual environment
source venv/bin/activate  # Mac/Linux
venv\Scripts\activate     # Windows

# Run server (accessible from network)
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

## Verification Steps

- [x] Server starts without errors
- [x] Laptop can access http://localhost:8000
- [x] Emulator can access http://10.0.2.2:8000
- [x] Physical phone can access http://192.168.x.x:8000
- [x] Firewall configured (if necessary)
- [x] Both devices on same Wi-Fi network

## Troubleshooting

### Phone Cannot Connect

**Check 1: Same Network**
- Laptop Wi-Fi: [Network Name]
- Phone Wi-Fi: [Must match]
- Phone NOT on mobile data

**Check 2: Firewall**
- Windows: Settings → Firewall → Allow Python
- Mac: System Preferences → Security → Firewall
- Linux: `sudo ufw status` and `sudo ufw allow 8000/tcp`

**Check 3: Server Running**
- Terminal shows "Uvicorn running on http://0.0.0.0:8000"
- --host must be 0.0.0.0 (not 127.0.0.1)

**Check 4: Correct IP**
- Verify IP hasn't changed (DHCP may reassign)
- Run ipconfig/ifconfig again to confirm

## Notes

- IP address may change after router restart
- Always use --host 0.0.0.0 for network access
- Test with phone browser before Android integration
- Save this configuration for Week 05 Retrofit setup
````

---

### Step 7: Postman Testing (Day 5)

**Create Postman Collection:**

1. Open Postman
2. Create Collection: "LeafGuard API"
3. Add requests:

**Request 1: Health Check**
- Name: Health Check
- Method: GET
- URL: http://localhost:8000/
- Expected: 200 OK, JSON with status "online"

**Request 2: Predict Disease (Success)**
- Name: Predict Disease - Success
- Method: POST
- URL: http://localhost:8000/predict
- Body: form-data
  - Key: image
  - Type: File
  - Value: [Select leaf.jpg]
- Expected: 200 OK with `disease`, `confidence`, `symptoms`, `treatment`, and `prevention`

**Request 3: Predict Disease (Invalid Type)**
- Name: Predict Disease - Invalid File Type
- Method: POST
- URL: http://localhost:8000/predict
- Body: form-data
  - Key: image
  - Type: File
  - Value: [Select test.txt]
- Expected: 400 Bad Request, error message

**Request 4: Network Test (Phone)**
- Name: Predict from Phone
- Method: POST
- URL: emulator `http://10.0.2.2:8000/predict`; physical phone `http://192.168.x.x:8000/predict` (replace with your IP)
- Body: form-data with image
- Test from Postman mobile app or desktop with environment variable

**Export collection:**
- Right-click collection → Export
- Save as `postman_collection.json`

---

### Step 8: Documentation (Day 6)

**Create comprehensive README.md:**

````markdown
# LeafGuard AI Backend

REST API backend for the LeafGuard plant disease detection Android application.

## Features

- **File Upload:** Accepts leaf images via multipart/form-data
- **Disease Prediction:** Returns dummy predictions (real ML model in Week 09)
- **Input Validation:** File type and size validation
- **Error Handling:** Clear error messages with appropriate HTTP status codes
- **CORS Support:** Allows cross-origin requests from Android app
- **Auto Documentation:** Swagger UI at /docs, ReDoc at /redoc
- **Local Network:** Accessible from Android phone on same Wi-Fi

## Tech Stack

- **Framework:** FastAPI 0.104.1
- **Server:** Uvicorn (ASGI)
- **Validation:** Pydantic 2.5.0
- **Language:** Python 3.8+

## Project Structure

```
backend-api/
├── main.py              # FastAPI app, health endpoint, /predict endpoint
├── config.py            # Settings and environment variables
├── model_loader.py      # Loads the model or mock predictor fallback
└── requirements.txt     # Python dependencies

## Installation

### Prerequisites

- Python 3.8 or higher
- pip (Python package manager)
- Git

### Setup Steps

1. **Clone Repository**
```bash
git clone [repository-url]
cd backend-api
```

2. **Create Virtual Environment**
```bash
python -m venv venv
```

3. **Activate Virtual Environment**
```bash
# Mac/Linux
source venv/bin/activate

# Windows Command Prompt
venv\Scripts\activate

# Windows PowerShell
venv\Scripts\Activate.ps1
```

4. **Install Dependencies**
```bash
pip install -r requirements.txt
```

5. **Run Server**
```bash
# For local development
uvicorn main:app --reload

# For network access (Android testing)
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

6. **Verify Installation**
- Open browser: http://localhost:8000
- Should see: `{"status": "online", ...}`
- Open API docs: http://localhost:8000/docs

## API Endpoints

### GET /

**Health check endpoint**

**Response:**
```json
{
  "status": "online",
  "message": "LeafGuard AI Backend is running",
  "version": "1.0.0",
  "docs_url": "/docs"
}
```

### GET /health

**Detailed health check**

**Response:**
```json
{
  "status": "healthy",
  "api_version": "1.0.0",
  "endpoints": {
    "predict": "/predict",
    "docs": "/docs",
    "redoc": "/redoc"
  }
}
```

### POST /predict

**Upload leaf image and receive disease prediction**

**Request:**
- **Method:** POST
- **Content-Type:** multipart/form-data
- **Body:**
  - `file`: Image file (JPEG or PNG)
- **Constraints:**
  - File type: image/jpeg or image/png
  - Maximum size: 10 MB

**Response (Success - 200 OK):**
```json
{
  "disease": "Tomato Early Blight",
  "confidence": 0.92,
  "symptoms": "Dark brown spots with concentric rings on lower leaves. Yellowing around spots.",
  "treatment": "Apply fungicide containing chlorothalonil or mancozeb every 7-10 days.",
  "prevention": "Rotate crops every 2-3 years. Avoid overhead watering."
}
```

**Response (Error - 400 Bad Request):**
```json
{
  "detail": "Invalid content type: text/plain. Allowed types: image/jpeg, image/png"
}
```

## Testing

### Using Swagger UI

1. Start server
2. Open http://localhost:8000/docs
3. Click on "POST /predict"
4. Click "Try it out"
5. Upload an image file
6. Click "Execute"
7. View response

### Using Postman

1. Import `postman_collection.json`
2. Select "LeafGuard API" collection
3. Run "Predict Disease - Success" request
4. Verify 200 OK response with disease data

### Using curl

```bash
curl -X POST "http://localhost:8000/predict" \
  -H "accept: application/json" \
  -H "Content-Type: multipart/form-data" \
  -F "image=@/path/to/leaf-image.jpg"
```

### Testing from Android Phone

1. Ensure phone and laptop on same Wi-Fi network
2. Find laptop's local IP: `ipconfig` (Windows) or `ifconfig` (Mac/Linux)
3. Run server: `uvicorn main:app --host 0.0.0.0 --port 8000 --reload`
4. Open phone browser: http://YOUR_IP:8000
5. See NETWORK_SETUP.md for detailed instructions

## Development

### Running in Development Mode

```bash
uvicorn main:app --reload
```

The `--reload` flag auto-restarts server when code changes.

### Adding New Endpoints

1. Add the new `@app.get(...)` or `@app.post(...)` function directly in `main.py`
2. Keep settings in `config.py` and model-loading code in `model_loader.py` when needed
3. Server auto-reloads with new endpoints

### Logging

Add logging for debugging:

```python
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

logger.info("Processing request...")
logger.error("Error occurred: ...")
```

## Future Enhancements (Upcoming Weeks)

- **Week 06:** Integrate real TensorFlow model for disease detection
- **Week 06:** Deploy to cloud (Heroku/AWS/Google Cloud)
- **Week 07:** Add database for logging predictions
- **Week 09:** User authentication (optional)

## Troubleshooting

### "Module not found" Error

**Solution:** Ensure virtual environment is activated and dependencies installed:
```bash
source venv/bin/activate  # or venv\Scripts\activate
pip install -r requirements.txt
```

### "Port 8000 already in use"

**Solution:** Kill existing process or use different port:
```bash
uvicorn main:app --port 8001 --reload
```

### Phone Cannot Connect to Server

**Solution:** See NETWORK_SETUP.md troubleshooting section. Common issues:
- Different Wi-Fi networks
- Firewall blocking connections
- Server not running with --host 0.0.0.0

### "python-multipart" Error

**Solution:** Install missing dependency:
```bash
pip install python-multipart
```

## Contributing

This is an academic project for CSE 2206 Mobile Application Development.

## License

Educational use only.

## Author

[Your Name] - [Roll Number]
CSE 2206 - Mobile Application Development
Week 04: FastAPI Backend Development

## Acknowledgments

- FastAPI documentation: https://fastapi.tiangolo.com
- Android documentation: https://developer.android.com
- LeafGuard project architecture: [Link to PROJECT_ARCHITECTURE.md]
````

---

### Step 9: Testing & Evidence Collection (Day 7)

**Create evidence folder:**

```bash
mkdir -p evidence/week-04/screenshots
mkdir -p evidence/week-04/code
```

**Collect evidence:**

1. **Screenshots:**
   - Postman health check success
   - Postman predict success with response
   - Postman predict error (invalid file)
   - Swagger UI showing endpoints
   - Phone browser accessing server
   - Terminal showing server running

2. **Code snapshots:**
   - Copy main files to evidence/week-04/code/

3. **Exports:**
   - Postman collection JSON
   - requirements.txt
   - README.md

**Git commits:**

```bash
git add .
git commit -m "Week 04: Complete FastAPI backend with /predict endpoint, validation, and documentation"
```

---

## Validation Before Submission

### Functional Requirements

- [ ] Server starts without errors
- [ ] GET / returns health check response
- [ ] POST /predict accepts JPEG images
- [ ] POST /predict accepts PNG images
- [ ] POST /predict rejects TXT files with 400 error
- [ ] Response includes all required fields
- [ ] Confidence is between 0.75 and 0.98
- [ ] Timestamp is in ISO 8601 format
- [ ] File size is included in response
- [ ] Random disease selected from database each time

### Network Requirements

- [ ] Server runs with --host 0.0.0.0
- [ ] Laptop can access via localhost:8000
- [ ] Laptop can access via local IP:8000
- [ ] Phone can access via laptop's IP:8000
- [ ] Firewall configured (if needed)
- [ ] NETWORK_SETUP.md created and accurate

### Code Quality

- [ ] All files have proper docstrings
- [ ] Pydantic models have validation
- [ ] Error handling with try-except
- [ ] CORS middleware configured
- [ ] No hardcoded IP addresses in code
- [ ] Constants defined for limits (file size, allowed types)
- [ ] Code follows PEP 8 style guidelines

### Documentation

- [ ] README.md complete with all sections
- [ ] Installation instructions tested
- [ ] API endpoints documented with examples
- [ ] NETWORK_SETUP.md with troubleshooting
- [ ] Code comments explain complex logic
- [ ] requirements.txt generated and tested

### Testing

- [ ] Postman collection created with 4+ requests
- [ ] All Postman tests pass
- [ ] Swagger UI accessible and functional
- [ ] curl command tested
- [ ] Phone browser test successful
- [ ] Screenshots saved for all test cases

### Git & Evidence

- [ ] Git repository initialized
- [ ] At least 3 meaningful commits
- [ ] .gitignore excludes venv and uploads
- [ ] Evidence folder organized
- [ ] Screenshots clearly labeled
- [ ] Code snapshots saved

---

## Submission Checklist

### Files to Submit

```
backend-api/
├── main.py
├── config.py
├── model_loader.py
├── requirements.txt
├── README.md
├── NETWORK_SETUP.md
├── .gitignore
└── evidence/
    └── week-04/
        ├── screenshots/
        │   ├── postman-health-check.png
        │   ├── postman-predict-success.png
        │   ├── postman-predict-error.png
        │   ├── swagger-ui.png
        │   ├── phone-browser-test.png
        │   └── terminal-server-running.png
        ├── code/
        │   ├── main.py
        │   ├── config.py
        │   └── model_loader.py
        └── postman-collection.json
```

### Verification Steps

1. **Fresh Install Test:**
   ```bash
   # New terminal window
   cd backend-api
   python -m venv test_venv
   source test_venv/bin/activate
   pip install -r requirements.txt
   uvicorn main:app --host 0.0.0.0 --port 8000
   ```
   Should work without errors.

2. **Postman Test:**
   - Import collection
   - Run all requests
   - All should pass

3. **Network Test:**
   - Phone on same Wi-Fi
   - Access http://YOUR_IP:8000
   - Should see health check response

4. **Documentation Test:**
   - Follow README installation steps exactly
   - Should work for someone unfamiliar with project

---

## Common Issues & Solutions

### Issue: Import errors

**Error:** `ModuleNotFoundError: No module named 'fastapi'`

**Solution:**
```bash
# Verify venv is activated (should see (venv) in terminal)
source venv/bin/activate

# Reinstall dependencies
pip install -r requirements.txt
```

### Issue: Server won't start

**Error:** `Address already in use`

**Solution:**
```bash
# Check what's using port 8000
# Mac/Linux:
lsof -i :8000

# Windows:
netstat -ano | findstr :8000

# Kill process or use different port
uvicorn main:app --port 8001
```

### Issue: Phone can't connect

**Checklist:**
1. Both devices on same Wi-Fi?
2. Server running with --host 0.0.0.0?
3. Firewall allowing Python?
4. Correct IP address (run ipconfig again)?
5. Server actually running (check terminal)?

### Issue: Validation not working

**Problem:** All files are accepted, even .txt files

**Solution:** Check if python-multipart is installed:
```bash
pip install python-multipart
# Restart server
```

---

**Congratulations! You now have a fully functional FastAPI backend ready for Android integration in Week 05.**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 04 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| **4** | **build-task.md** ← *You are here* | **Build Implementation Guide** |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Practice Exercises](exercises.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Build Implementation Guide** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Validation & Verification →](validation-checklist.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
