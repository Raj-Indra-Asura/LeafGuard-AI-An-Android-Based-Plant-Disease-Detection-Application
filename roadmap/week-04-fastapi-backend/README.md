# Week 04: Python FastAPI Backend Development

## What you'll learn & why

This week teaches you how to build the FastAPI backend: a Python program that receives a leaf photo from the Android app and sends back a disease diagnosis as JSON. You will learn how an API connects the mobile app to server-side prediction logic. This matters because LeafGuard needs a reliable place to accept uploaded images, run the mock predictor for now, and return clear disease information that Android can display. You will also practice testing the backend before connecting it to the app.

## New words this week

- **Backend** — a program running on a server (here, a Python program) that does work for the app and sends answers back.
- **API (Application Programming Interface)** — a set of URLs and rules that lets the Android app ask the backend for information or actions.
- **HTTP POST** — an HTTP (HyperText Transfer Protocol) request used when the app sends data, such as a leaf image, to the backend.
- **Multipart (multipart/form-data)** — a request format that can carry files and text fields together; LeafGuard uses it to upload the image part named `image`.
- **JSON (JavaScript Object Notation)** — a simple text format for structured data, such as `disease`, `confidence`, `symptoms`, `treatment`, and `prevention`.

See the full [glossary](../../GLOSSARY.md) for more terms.

## Related materials

- Exercises: [../../exercises/backend/](../../exercises/backend/)
- Solutions: [../../solutions/week-04/](../../solutions/week-04/)
- Notebooks: [../../notebooks/week-04/](../../notebooks/week-04/)
- Glossary: [../../GLOSSARY.md](../../GLOSSARY.md)

## Weekly Objective

By the end of Week 04, you will:

1. **Understand REST (REpresentational State Transfer) API principles** and HTTP (HyperText Transfer Protocol) request-response cycles in web applications
2. **Set up a Python FastAPI backend** with proper project structure and virtual environment
3. **Create a `/predict` endpoint** that accepts multipart file uploads with the part name `image`
4. **Return dummy JSON responses** with realistic disease prediction data
5. **Test the API using Postman** to verify endpoint functionality before Android integration
6. **Configure local network setup** allowing your Android phone and laptop to communicate on the same Wi-Fi network

**Measurable Outcomes:**
- Working FastAPI server running on local machine (laptop)
- `/predict` endpoint accepting image uploads via POST request
- Dummy JSON response returned with disease, confidence, symptoms, treatment, and prevention
- Postman collection with successful test requests
- Local IP address configured and documented for Android client
- Git commits showing backend development progress

---

## Why This Week Matters

### Connection to CSE 2206 Mobile Application Development

Week 04 is where your Android app becomes a **network-enabled application**. Without a backend, your app cannot:
- Send images for disease detection
- Receive AI predictions
- Demonstrate HTTP POST requests with multipart data
- Parse JSON responses
- Handle asynchronous network operations

**This week ensures:**
- You understand client-server architecture (core networking concept)
- You can explain RESTful API design during viva
- Your Android app has a real backend to communicate with
- You demonstrate full-stack development capability

### Academic Requirement Alignment

CSE 2206 expects you to demonstrate:
1. **Network Programming:** HTTP requests, REST APIs (Week 04 backend + Week 05 Android client)
2. **JSON Parsing:** Understanding structured data interchange (Week 04 response format)
3. **Asynchronous Operations:** Backend serves requests while Android waits for responses
4. **Client-Server Architecture:** Android app (client) communicates with FastAPI (server)

Week 04 builds the server side. Week 05 builds the client side. Together, they prove you understand network programming.

---

## Syllabus Topics Covered This Week

### Direct Coverage

1. **Network Programming Fundamentals**
   - HTTP protocol (request/response cycle)
   - Client-server architecture
   - RESTful API design principles

2. **Data Interchange Formats**
   - JSON structure and formatting
   - Multipart form-data for file uploads
   - HTTP headers and content types

3. **Backend Development Basics**
   - Python virtual environments
   - Dependency management (requirements.txt)
   - Project structure organization

4. **API Testing**
   - Using Postman for endpoint testing
   - Validating request/response formats
   - Debugging network issues

### Indirect Preparation

- **Week 05 Android Networking:** Setting up the backend now allows you to integrate with Retrofit next week
- **Week 06 ML Model Integration:** Backend structure established this week will be extended with real TensorFlow model
- **Week 09 Offline AI:** Understanding backend structure helps you implement on-device TFLite inference

---

## Prerequisites

### Required Knowledge

1. **Basic Python:**
   - Variables, functions, loops, conditionals
   - Dictionaries and lists
   - Importing libraries
   - Understanding JSON structure

2. **Command Line:**
   - Navigating directories (`cd`, `ls`/`dir`)
   - Running Python scripts
   - Installing packages with pip
   - Understanding virtual environments

3. **HTTP Basics:**
   - What GET and POST requests are
   - HTTP status codes (200 OK, 404 Not Found, 500 Error)
   - Request body vs URL parameters

4. **Week 03 Completion:**
   - Camera and gallery integration working
   - You have leaf images to test with
   - You understand what data the app will send to backend

### Required Tools

1. **Python 3.8+:** Download from https://www.python.org/downloads/
2. **Code Editor:** VS Code, PyCharm, or any Python-compatible editor
3. **Postman:** Download from https://www.postman.com/downloads/ (for API testing)
4. **Git:** For version control
5. **Terminal/Command Prompt:** For running Python commands

### Recommended Reading (Before Week 04)

- FastAPI official documentation: https://fastapi.tiangolo.com/
- HTTP methods explained: https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods
- REST API design principles: https://restfulapi.net/
- Multipart form-data: https://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.2

---

## Concepts to Learn

### 1. REST API Architecture

**What it is:** Representational State Transfer (REST) is an architectural style for designing networked applications.

**Key Principles:**

1. **Client-Server Separation:** Android app (client) and FastAPI backend (server) are independent
2. **Stateless:** Each request contains all information needed (no session stored on server)
3. **Resource-Based:** Everything is a resource (disease prediction, scan history)
4. **HTTP Methods:** Use appropriate verbs (GET for retrieving, POST for creating)

**LeafGuard API Design:**
```
POST /predict
- Accepts: Multipart file (image)
- Returns: JSON with disease prediction

GET /health
- Accepts: Nothing
- Returns: JSON with server status

GET /diseases/{name}
- Accepts: Disease name in URL
- Returns: JSON with detailed disease info
```

**Why REST?**
- Standard approach (every Android developer understands)
- Works over HTTP (universal protocol)
- Easy to test (Postman, curl, browser)
- Stateless (scales well, no session management)

### 2. HTTP Request-Response Cycle

**How it works:**

```
ANDROID APP                           FASTAPI BACKEND
     |                                      |
     |-- POST /predict (image bytes) ----->|
     |                                      | (Process request)
     |                                      | (Return dummy data for now)
     |<--- JSON response (prediction) ------|
     |                                      |
```

**Request Components:**
1. **Method:** POST (we are sending data)
2. **URL:** emulator: http://10.0.2.2:8000/predict; computer/browser: http://localhost:8000/predict; physical phone: use your laptop LAN IP
3. **Headers:**
   - Content-Type: multipart/form-data
   - Accept: application/json
4. **Body:** Binary image file

**Response Components:**
1. **Status Code:** 200 OK (or 400 Bad Request, 500 Server Error)
2. **Headers:**
   - Content-Type: application/json
3. **Body:** JSON data
   ```json
   {
     "disease": "Tomato Early Blight",
     "confidence": 0.92,
     "symptoms": "Dark spots with concentric rings...",
     "treatment": "Apply fungicide..."
   }
   ```

### 3. FastAPI Framework Basics

**What is FastAPI?**
- Modern Python web framework for building APIs
- Fast (built on Starlette and Pydantic)
- Automatic documentation (Swagger UI at /docs)
- Type hints for validation

**Why FastAPI for LeafGuard?**
- Easy to learn (minimal boilerplate)
- Built-in file upload handling
- Async support (handles multiple requests)
- Integrates well with TensorFlow/PyTorch

**Basic FastAPI Structure:**
```python
from fastapi import FastAPI

app = FastAPI()

@app.get("/")
def read_root():
    return {"message": "LeafGuard API"}

@app.post("/predict")
async def predict(image: UploadFile = File(...)):
    # Logic here
    return {"disease": "Unknown"}
```

**Decorators Explained:**
- `@app.get("/")`: This function handles GET requests to the root URL
- `@app.post("/predict")`: This function handles POST requests to /predict
- `@app.put("/update")`: For updating resources (not used in LeafGuard)
- `@app.delete("/delete")`: For deleting resources (not used in LeafGuard)

### 4. Multipart Form-Data

**What it is:** A way to upload files over HTTP.

**Why not just JSON?**
- JSON is text-based, images are binary
- Images are large (1-5 MB), inefficient as base64 in JSON
- Multipart allows mixing text fields and file uploads

**Example Request (Raw HTTP):**
```http
POST /predict HTTP/1.1
Host: localhost:8000
Content-Type: multipart/form-data; boundary=----Boundary123

------Boundary123
Content-Disposition: form-data; name="image"; filename="leaf.jpg"
Content-Type: image/jpeg

[binary image data here]
------Boundary123--
```

**In FastAPI:**
```python
from fastapi import UploadFile, File

@app.post("/predict")
async def predict(image: UploadFile = File(...)):
    # FastAPI automatically parses multipart part named "image"
    contents = await image.read()  # Get image bytes
    filename = image.filename      # Get original filename
    return {"received": filename}
```

### 5. JSON Response Structure

**What is JSON?**
JavaScript Object Notation - a lightweight data format for exchanging information.

**LeafGuard Prediction Response:**
```json
{
  "disease": "Tomato Early Blight",
  "confidence": 0.923,
  "symptoms": "Dark brown spots with concentric rings on lower leaves",
  "treatment": "Apply fungicide containing chlorothalonil or mancozeb",
  "prevention": "Remove infected leaves, improve air circulation"
}
```

**Why this structure?**
- `disease`: String with disease name (Android displays this)
- `confidence`: Float 0.0-1.0 (Android shows as percentage)
- `symptoms`: String (Android displays in result screen)
- `treatment`: String (Android displays as action item)
- `prevention`: String (Android displays as education)

**Error Response:** FastAPI returns an HTTP error such as `{"detail": "Only JPEG and PNG images are supported"}`.

### 6. Local Network Configuration

**The Problem:**
- Your FastAPI server runs on your laptop
- Your Android app runs on your phone
- They need to communicate

**Solution: Same Wi-Fi Network**

**Setup Steps:**
1. Connect laptop to Wi-Fi network (e.g., "Home WiFi")
2. Connect Android phone to same Wi-Fi network
3. For the Android emulator, use `http://10.0.2.2:8000/` because `10.0.2.2` means "my computer" from the emulator. For a physical phone, find your laptop's local IP address:
   - Windows: `ipconfig` → Look for "IPv4 Address" under Wi-Fi adapter
   - Mac/Linux: `ifconfig` → Look for "inet" under en0 or wlan0
   - Example: 192.168.1.100

4. Run FastAPI server: `uvicorn main:app --host 0.0.0.0 --port 8000`
   - `--host 0.0.0.0` means "accept connections from any IP" (not just localhost)
   - `--port 8000` is the port number

5. Test from Android emulator browser: http://10.0.2.2:8000/
6. For a physical phone on the same Wi-Fi, test: http://192.168.1.100:8000/

**Common Issues:**
- **Firewall blocking:** Allow Python through Windows Firewall
- **Wrong IP:** Use `10.0.2.2` for the Android emulator; use your laptop LAN IP (192.168.x.x) only for a physical phone
- **Different networks:** Phone on mobile data, laptop on Wi-Fi (won't work)

### 7. Virtual Environment

**What it is:** An isolated Python environment for your project.

**Why use it?**
- Keeps project dependencies separate
- Avoids version conflicts (Project A needs FastAPI 0.95, Project B needs 0.100)
- Easy to replicate on other machines (requirements.txt)

**Setup:**
```bash
# Create virtual environment
python -m venv venv
# On macOS/Linux, use python3 -m venv venv if python points to Python 2

# Activate (Windows)
venv\Scripts\activate

# Activate (Mac/Linux)
source venv/bin/activate

# Install dependencies
pip install fastapi uvicorn python-multipart

# Save dependencies
pip freeze > requirements.txt

# Deactivate when done
deactivate
```

**How to tell it's active:**
- Your terminal shows `(venv)` prefix
- `which python` points to venv folder

---

## Reading Plan

### Day 1: FastAPI Basics & Setup

**Read:**
1. FastAPI First Steps: https://fastapi.tiangolo.com/tutorial/first-steps/
2. Request Body: https://fastapi.tiangolo.com/tutorial/body/
3. Response Model: https://fastapi.tiangolo.com/tutorial/response-model/

**Practice:**
1. Install Python 3.8+ if not already installed
2. Create a project folder: `mkdir backend-api`
3. Create virtual environment: `python -m venv venv`
4. Activate virtual environment
5. Install FastAPI: `pip install fastapi uvicorn`
6. Create `main.py` with hello world endpoint
7. Run server: `uvicorn main:app --reload`
8. Open browser: http://localhost:8000

**Expected Output:**
```json
{"message": "Hello World"}
```

### Day 2: File Upload & Multipart Data

**Read:**
1. Request Files: https://fastapi.tiangolo.com/tutorial/request-files/
2. File Uploads: https://fastapi.tiangolo.com/tutorial/request-forms-and-files/
3. MDN Multipart: https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/POST

**Practice:**
1. Add `python-multipart` dependency: `pip install python-multipart`
2. Create `/upload` endpoint accepting file
3. Read file contents and return filename
4. Test with Postman (File upload)
5. Save uploaded file to disk temporarily

**Expected Behavior:**
- POST request with image file succeeds
- Response shows filename and size
- File appears in uploads/ folder

### Day 3: JSON Response Structure

**Read:**
1. Response Model: https://fastapi.tiangolo.com/tutorial/response-model/
2. Pydantic Models: https://docs.pydantic.dev/latest/
3. JSON basics: https://www.json.org/json-en.html

**Practice:**
1. Define Pydantic model for prediction response
2. Create `/predict` endpoint
3. Return dummy prediction data (hardcoded)
4. Validate response structure in Postman
5. Test with different dummy diseases

**Example Response:**
```json
{
  "disease": "Tomato Leaf Mold",
  "confidence": 0.88,
  "symptoms": "Pale green to yellowish spots on upper leaf surface",
  "treatment": "Increase ventilation, apply fungicides"
}
```

### Day 4: Local Network Setup

**Read:**
1. Network basics: https://developer.mozilla.org/en-US/docs/Learn/Common_questions/What_is_a_URL
2. Uvicorn documentation: https://www.uvicorn.org/
3. HTTP host and port: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Host

**Practice:**
1. Find your laptop's local IP address
2. Run server with `--host 0.0.0.0`
3. Test from phone browser: http://YOUR_IP:8000
4. Document IP address for Android integration
5. Test /predict endpoint from phone using Postman mobile app or browser

**Troubleshooting:**
- If phone cannot reach server, check firewall settings
- Ensure both devices are on same Wi-Fi network
- Try pinging laptop from phone

### Day 5: Postman Testing & Documentation

**Read:**
1. Postman Getting Started: https://learning.postman.com/docs/getting-started/introduction/
2. Postman Collections: https://learning.postman.com/docs/collections/collections-overview/
3. FastAPI Auto Docs: https://fastapi.tiangolo.com/tutorial/first-steps/#interactive-api-docs

**Practice:**
1. Install Postman desktop app
2. Create collection "LeafGuard API"
3. Add request "Health Check" (GET /)
4. Add request "Predict Disease" (POST /predict with file)
5. Test all endpoints
6. Export collection as JSON
7. Explore FastAPI auto-generated docs at http://localhost:8000/docs

**Deliverable:**
- Postman collection with successful test results
- Screenshots of Swagger UI showing endpoints

### Day 6: Error Handling & Validation

**Read:**
1. FastAPI Error Handling: https://fastapi.tiangolo.com/tutorial/handling-errors/
2. HTTP Status Codes: https://developer.mozilla.org/en-US/docs/Web/HTTP/Status

**Practice:**
1. Add file type validation (only JPEG/PNG)
2. Add file size validation (max 10 MB)
3. Return 400 Bad Request for invalid files
4. Return 500 Internal Server Error for exceptions
5. Test error scenarios in Postman

**Error Handling Example:**
```python
from fastapi import HTTPException

if not file.content_type in ["image/jpeg", "image/png"]:
    raise HTTPException(status_code=400, detail="Invalid file type")
```

### Day 7: Code Organization & Documentation

**Read:**
1. FastAPI Bigger Applications: https://fastapi.tiangolo.com/tutorial/bigger-applications/
2. Python project structure: https://docs.python-guide.org/writing/structure/

**Practice:**
1. Keep the backend in the real single-file layout (`main.py`, `config.py`, `model_loader.py`)
2. Add docstrings to functions
3. Create README.md for backend
4. Document API endpoints in README
5. Create requirements.txt
6. Test entire setup: fresh install in new folder
7. Git commit all backend code

**Final Structure:**
```
backend-api/
├── main.py              # FastAPI app, health endpoint, /predict endpoint
├── config.py            # settings and environment variables
├── model_loader.py      # loads the real model or mock predictor fallback
└── requirements.txt     # Python dependencies
```

---

## Build Task

### Week 04 Build Task: Create Working FastAPI Backend with /predict Endpoint

**Objective:** By end of Week 04, you will have a fully functional FastAPI backend running on your local machine, accessible from your Android phone on the same Wi-Fi network, with a /predict endpoint that accepts image uploads and returns dummy disease predictions.

### Deliverable 1: FastAPI Project Structure

**Create this folder structure:**

```
backend-api/
├── main.py              # FastAPI app, health endpoint, /predict endpoint
├── config.py            # settings and environment variables such as USE_MOCK
├── model_loader.py      # loads the model or mock predictor fallback
└── requirements.txt     # Python dependencies
```

**File: `main.py`**

This is your main application file. It creates the FastAPI app and defines the `/predict` endpoint directly in `main.py`.

```python
from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

app = FastAPI(
    title="LeafGuard AI Backend",
    description="Backend API for plant disease detection",
    version="1.0.0"
)

# Allow Android app to make requests (CORS = Cross-Origin Resource Sharing)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # In production, specify Android app's origin
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class PredictionResult(BaseModel):
    disease: str
    confidence: float
    symptoms: str
    treatment: str
    prevention: str

@app.get("/")
def root():
    """Health check endpoint."""
    return {
        "status": "online",
        "message": "LeafGuard AI Backend is running",
        "version": "1.0.0"
    }

@app.post("/predict", response_model=PredictionResult)
async def predict(image: UploadFile = File(...)):
    """Accept the multipart image part named `image` and return a prediction."""
    if image.content_type not in ["image/jpeg", "image/png"]:
        raise HTTPException(status_code=400, detail="Only JPEG and PNG images are supported")
    # Week 04 can return mock data; Week 09 adds the real trained model.
    return PredictionResult(
        disease="Tomato Early Blight",
        confidence=0.92,
        symptoms="Dark spots with concentric rings on lower leaves.",
        treatment="Remove infected leaves and apply an approved fungicide.",
        prevention="Avoid overhead watering and improve air circulation."
    )
```

**What this does:**
- Creates FastAPI app with title and description
- Adds CORS (Cross-Origin Resource Sharing) middleware so Android can call this API
- Provides the health check endpoint at `/`
- Defines the `/predict` endpoint directly in `main.py`

**File: `main.py` (`/predict` section)**

This section contains the `/predict` endpoint logic.

```python
from fastapi import File, UploadFile, HTTPException
from pydantic import BaseModel
import random

class PredictionResult(BaseModel):
    disease: str
    confidence: float
    symptoms: str
    treatment: str
    prevention: str

# Dummy disease data
DUMMY_DISEASES = [
    {
        "disease": "Tomato Early Blight",
        "symptoms": "Dark brown spots with concentric rings on lower leaves. Yellowing around spots.",
        "treatment": "Apply fungicide containing chlorothalonil or mancozeb. Remove infected leaves.",
        "prevention": "Rotate crops, avoid overhead watering, ensure good air circulation."
    },
    {
        "disease": "Potato Late Blight",
        "symptoms": "Water-soaked spots on leaves that turn brown. White fuzzy growth on undersides.",
        "treatment": "Apply copper-based fungicide immediately. Destroy infected plants.",
        "prevention": "Plant resistant varieties, avoid wet foliage, space plants properly."
    },
    {
        "disease": "Corn Common Rust",
        "symptoms": "Small, circular to elongated reddish-brown pustules on leaves.",
        "treatment": "Apply fungicide if disease is severe. Usually not economically damaging.",
        "prevention": "Plant resistant hybrids, monitor regularly."
    }
]

@app.post("/predict", response_model=PredictionResult)
async def predict(image: UploadFile = File(...)):
    """
    Upload a leaf image and get disease prediction.

    This is a dummy endpoint that returns random predictions.
    Week 09 will integrate the real trained model.
    """

    # Validate file
    if image.content_type not in ["image/jpeg", "image/png"]:
        raise HTTPException(status_code=400, detail="Only JPEG and PNG images are supported")

    # Read image (we don't process it yet, just validate it's readable)
    try:
        contents = await image.read()
        file_size = len(contents)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed to read file: {str(e)}")

    # Return dummy prediction (random disease from list)
    disease_data = random.choice(DUMMY_DISEASES)
    confidence = round(random.uniform(0.75, 0.98), 2)  # Random confidence 75-98%

    return PredictionResult(
        disease=disease_data["disease"],
        confidence=confidence,
        symptoms=disease_data["symptoms"],
        treatment=disease_data["treatment"],
        prevention=disease_data["prevention"]
    )
```

**What this does:**
- Defines `/predict` endpoint
- Accepts file upload
- Validates file is a valid image
- Returns random dummy prediction (we'll replace with real ML model in Week 09)
- Includes realistic disease data

**Prediction model in `main.py`**

Pydantic keeps the JSON response structured.

```python
from pydantic import BaseModel

class PredictionResult(BaseModel):
    """Response model for successful prediction"""
    disease: str
    confidence: float
    symptoms: str
    treatment: str
    prevention: str
```

**What this does:**
- Defines structure of JSON responses
- FastAPI uses this to validate responses
- Auto-generates API documentation

**Validation helper in `main.py`**

File validation utilities.

```python
from fastapi import UploadFile

ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png"}
MAX_FILE_SIZE = 10 * 1024 * 1024  # 10 MB

def validate_image(file: UploadFile) -> str:
    """
    Validate uploaded file is a valid image.

    Returns:
        Error message string if invalid, None if valid
    """

    # Check content type
    if file.content_type not in ["image/jpeg", "image/png"]:
        return f"Invalid file type: {file.content_type}. Only JPEG and PNG are supported."

    # Check file extension
    if file.filename:
        extension = file.filename.split(".")[-1].lower()
        if extension not in ALLOWED_EXTENSIONS:
            return f"Invalid file extension: .{extension}. Only {ALLOWED_EXTENSIONS} are supported."

    return None  # Valid
```

**What this does:**
- Checks file is JPEG or PNG
- Validates file extension
- Can be extended to check file size, dimensions, etc.

**File: `requirements.txt`**

```
fastapi==0.104.1
uvicorn==0.24.0
python-multipart==0.0.6
pydantic==2.5.0
```

**What this is:**
- List of Python dependencies
- Allows others to install same versions: `pip install -r requirements.txt`

**File: `.gitignore`**

```
# Python
__pycache__/
*.py[cod]
*$py.class
*.so
.Python
venv/
env/
ENV/

# IDEs
.vscode/
.idea/
*.swp

# Uploads
uploads/*.jpg
uploads/*.jpeg
uploads/*.png

# Keep uploads folder
!uploads/.gitkeep
```

### Deliverable 2: Running Server

**Start the server:**

```bash
# Navigate to backend folder
cd backend-api

# Activate virtual environment (if not already active)
source venv/bin/activate  # Mac/Linux
venv\Scripts\activate     # Windows

# Run server (accessible from other devices)
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

**Expected console output:**
```
INFO:     Uvicorn running on http://0.0.0.0:8000 (Press CTRL+C to quit)
INFO:     Started reloader process [12345] using StatReload
INFO:     Started server process [12346]
INFO:     Waiting for application startup.
INFO:     Application startup complete.
```

**Verify server is running:**
1. Open browser: http://localhost:8000
2. Should see: `{"status": "online", "message": "LeafGuard AI Backend is running", "version": "1.0.0"}`
3. Open Swagger docs: http://localhost:8000/docs
4. Should see interactive API documentation with /predict endpoint

### Deliverable 3: Postman Testing

**Create Postman collection:**

1. Open Postman
2. Create new collection: "LeafGuard API"
3. Add request "Health Check":
   - Method: GET
   - URL: http://localhost:8000/
   - Expected response: 200 OK with status "online"

4. Add request "Predict Disease":
   - Method: POST
   - URL: http://localhost:8000/predict
   - Body: form-data
   - Key: `file`, Type: File
   - Select a test leaf image
   - Expected response: 200 OK with disease prediction JSON

5. Test error handling:
   - Upload a .txt file instead of image
   - Expected response: 400 Bad Request with error message

6. Export collection (File → Export → Collection v2.1)

**Save evidence:**
- Screenshot of successful prediction response
- Screenshot of error handling
- Exported Postman collection JSON

### Deliverable 4: Local Network Configuration

**Find your local IP address:**

**Windows:**
```bash
ipconfig
```
Look for "IPv4 Address" under your Wi-Fi adapter (usually starts with 192.168.x.x)

**Mac/Linux:**
```bash
ifconfig
```
Look for "inet" under en0 or wlan0

**Example output:**
```
IPv4 Address: 192.168.1.100
```

**Test from Android emulator or physical phone:**

1. For the emulator, open Chrome and navigate to: http://10.0.2.2:8000/
2. For a physical phone, first ensure the phone is on the same Wi-Fi network as the laptop
3. On a physical phone, navigate to: http://192.168.1.100:8000/ (replace with your laptop IP)
4. Should see health check response
5. Open docs: http://10.0.2.2:8000/docs on the emulator, or http://192.168.1.100:8000/docs on a physical phone
6. Should see Swagger UI (may need to interact with it)

Expected result: the browser shows the health JSON at `/` and the interactive API documentation at `/docs`.

**Document your setup:**

Create `NETWORK_SETUP.md` in backend folder:

```markdown
# Local Network Setup

## Laptop Configuration
- OS: Windows 11 / macOS / Linux
- Wi-Fi Network: [Your Network Name]
- Local IP Address (physical phone only): 192.168.1.100
- Server Port: 8000
- Computer Base URL: http://localhost:8000

## Android Configuration
- Emulator Base URL: http://10.0.2.2:8000
- Physical Phone Base URL: http://192.168.1.100:8000 (same Wi-Fi only)

## Verification Steps
1. [x] Server runs on laptop: `uvicorn main:app --host 0.0.0.0 --port 8000`
2. [x] Laptop can access: http://localhost:8000
3. [x] Emulator can access: http://10.0.2.2:8000
4. [x] Physical phone can access: http://192.168.1.100:8000 (if testing on a real phone)

## Troubleshooting
- If phone cannot reach server, check firewall (allow Python through firewall)
- Both devices must be on same Wi-Fi (not mobile data)
- Verify IP address hasn't changed (home networks often use DHCP)
```

### Deliverable 5: Backend Documentation

**Create `README.md` in backend folder:**

```markdown
# LeafGuard AI Backend

FastAPI backend for plant disease detection Android application.

## Features
- RESTful API with /predict endpoint
- Multipart file upload handling
- Dummy disease prediction (to be replaced with TensorFlow model in Week 06)
- Input validation and error handling
- Auto-generated API documentation (Swagger UI)

## Tech Stack
- Python 3.8+
- FastAPI 0.104.1
- Uvicorn (ASGI server)
- Pydantic (data validation)

## Installation

### 1. Clone Repository
```bash
git clone [your-repo-url]
cd backend-api
```

### 2. Create Virtual Environment
```bash
python -m venv venv
# On macOS/Linux, use python3 -m venv venv if python points to Python 2
source venv/bin/activate  # Mac/Linux
venv\Scripts\activate     # Windows
```

### 3. Install Dependencies
```bash
pip install -r requirements.txt
```

### 4. Run Server
```bash
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

## API Endpoints

### GET /
Health check endpoint.

**Response:**
```json
{
  "status": "online",
  "message": "LeafGuard AI Backend is running",
  "version": "1.0.0"
}
```

### POST /predict
Upload leaf image and receive disease prediction.

**Request:**
- Method: POST
- Content-Type: multipart/form-data
- Body: multipart part named `image` (image/jpeg or image/png)

**Response (Success):**
```json
{
  "disease": "Tomato Early Blight",
  "confidence": 0.92,
  "symptoms": "Dark brown spots with concentric rings...",
  "treatment": "Apply fungicide containing chlorothalonil...",
  "prevention": "Rotate crops, avoid overhead watering..."
}
```

**Response (Error):** FastAPI returns an HTTP error with a `detail` message, such as `{"detail": "Only JPEG and PNG are supported"}`.

## Testing

### Using Swagger UI
1. Run server from inside `backend-api/` with `uvicorn main:app --reload`
2. Open http://localhost:8000/docs
3. Click on POST /predict
4. Click "Try it out"
5. Upload an image file
6. Click "Execute"

### Using Postman
1. Import `postman_collection.json`
2. Update base URL to your local IP (if testing from phone)
3. Run "Predict Disease" request with image file

### Using curl
```bash
curl -X POST "http://localhost:8000/predict" \
  -H "accept: application/json" \
  -H "Content-Type: multipart/form-data" \
  -F "image=@/path/to/leaf.jpg"
```

## Project Structure
```
backend-api/
├── main.py              # FastAPI app, health endpoint, /predict endpoint
├── config.py            # Settings and environment variables
├── model_loader.py      # Model loader or mock predictor fallback
└── requirements.txt     # Dependencies
```

## Future Enhancements (Week 09)
- Integrate TensorFlow model for real predictions
- Add authentication
- Deploy to cloud (Heroku/AWS)
- Add database for logging predictions

## Author
[Your Name] - CSE 2206 Mobile Application Development
```

### Deliverable 6: Git Commits

**Commit your work:**

```bash
# Initialize git (if not already done)
git init

# Add all files
git add .

# Commit
git commit -m "Week 04: Initialize FastAPI backend with /predict endpoint and dummy responses"

# After testing and documentation
git add .
git commit -m "Week 04: Add Postman collection, network setup docs, and README"
```

---

## Validation Checklist

Use this checklist to verify Week 04 completion.

### Backend Setup
- [ ] Python 3.8+ is installed
- [ ] Virtual environment created and activated
- [ ] FastAPI and dependencies installed
- [ ] Project structure matches specification
- [ ] All files created (`main.py`, `config.py`, `model_loader.py`, `requirements.txt`)
- [ ] .gitignore excludes venv and uploads

### Endpoint Functionality
- [ ] Server starts without errors
- [ ] GET / returns health check response
- [ ] POST /predict accepts image files
- [ ] POST /predict returns dummy disease prediction
- [ ] Response includes all required fields (disease, confidence, symptoms, treatment, prevention)
- [ ] Confidence is between 0.0 and 1.0

### Validation & Error Handling
- [ ] Uploading .txt file returns 400 error
- [ ] Uploading .pdf file returns 400 error
- [ ] Error response includes descriptive message
- [ ] Large files (>10MB) are rejected (optional for Week 04)

### Network Configuration
- [ ] Local IP address identified and documented
- [ ] Server runs with --host 0.0.0.0
- [ ] Server accessible from laptop browser
- [ ] Server accessible from phone browser (same Wi-Fi)
- [ ] Firewall configured to allow Python
- [ ] NETWORK_SETUP.md created with IP and verification steps

### Postman Testing
- [ ] Postman installed
- [ ] Collection created: "LeafGuard API"
- [ ] Health Check request succeeds
- [ ] Predict Disease request succeeds with image file
- [ ] Error handling request succeeds (wrong file type)
- [ ] Collection exported as JSON
- [ ] Screenshots saved of successful requests

### Documentation
- [ ] README.md created with installation steps
- [ ] API endpoints documented with examples
- [ ] requirements.txt includes all dependencies
- [ ] Code includes docstrings
- [ ] NETWORK_SETUP.md documents IP configuration

### Code Quality
- [ ] Code follows PEP 8 style (proper indentation, naming)
- [ ] Functions have type hints
- [ ] No hardcoded IP addresses in code
- [ ] Imports organized (standard lib, third-party, local)
- [ ] No unused imports or variables

### Git & Evidence
- [ ] Git repository initialized in backend folder
- [ ] At least 2-3 meaningful commits
- [ ] Commit messages follow "Week 04: [Category] - Description" format
- [ ] evidence/week-04/ folder created
- [ ] Screenshots of Postman tests saved
- [ ] Screenshot of phone accessing server saved
- [ ] Postman collection JSON saved

### Understanding
- [ ] I can explain REST API principles
- [ ] I can explain difference between GET and POST
- [ ] I can explain why multipart form-data is used for files
- [ ] I can explain JSON structure of prediction response
- [ ] I can explain why --host 0.0.0.0 is needed
- [ ] I can explain purpose of virtual environment
- [ ] I can explain what CORS is and why it's needed

---

## Common Mistakes to Avoid

### Mistake 1: Using 127.0.0.1 or localhost in Android App

**Problem:** Android app cannot reach server at http://127.0.0.1:8000 or http://localhost:8000

**Why:** 127.0.0.1 means "this device" - on Android, it refers to the phone itself, not your laptop.

**Fix:** In the Android emulator use `http://10.0.2.2:8000/`. On a physical phone, use your laptop's LAN IP (e.g., `http://192.168.1.100:8000/`).

### Mistake 2: Forgetting --host 0.0.0.0

**Problem:** Server runs on laptop but phone cannot connect.

**Why:** Default uvicorn binding is 127.0.0.1, which only accepts local connections.

**Fix:** Always run: `uvicorn main:app --host 0.0.0.0 --port 8000`

### Mistake 3: Firewall Blocking Connections

### Mistake 4: 422 Unprocessable Entity

**Problem:** `/predict` returns 422 when uploading with curl, Postman, or Retrofit.

**Fix:** The multipart form part must be named `image`, not `file`.

### Mistake 5: ModuleNotFoundError

**Problem:** FastAPI or python-multipart cannot be imported.

**Fix:** Activate the virtual environment, then run `pip install -r backend-api/requirements.txt`.

### Mistake 6: Address already in use

**Problem:** Uvicorn says port 8000 is already in use.

**Fix:** Stop the other Uvicorn process or run on another port such as `--port 8001`.

### Mistake 7: Firewall Blocking Connections

**Problem:** Phone browser shows "connection refused" or timeout.

**Why:** Windows Firewall or antivirus blocking incoming connections.

**Fix:**
- Windows: Settings → Firewall → Allow an app → Add Python
- Mac: System Preferences → Security → Firewall → Allow Python

### Mistake 4: Phone on Mobile Data, Laptop on Wi-Fi

**Problem:** Phone and laptop cannot communicate.

**Why:** They're on different networks.

**Fix:** Ensure BOTH devices are on the same Wi-Fi network.

### Mistake 5: Not Installing python-multipart

**Problem:** File upload fails with "405 Method Not Allowed" or similar error.

**Why:** FastAPI requires python-multipart library for form-data handling.

**Fix:** `pip install python-multipart`

### Mistake 6: Hardcoding IP Address in Code

**Problem:** Code breaks when IP address changes.

**Why:** Routers often assign new IPs to devices.

**Fix:** Use environment variables or configuration file. Never hardcode IPs in Python code.

### Mistake 7: Not Using Virtual Environment

**Problem:** Dependency conflicts, can't replicate setup on another machine.

**Why:** System-wide packages can conflict between projects.

**Fix:** Always create and activate venv before installing packages.

### Mistake 8: Ignoring Error Responses

**Problem:** Android app crashes when backend returns error.

**Why:** Android code should handle HTTP error responses such as 400 or 500 with a `detail` message.

**Fix:** Always test error scenarios (wrong file type, large files, etc.) and handle them in Android (Week 05).

---

## Next Steps: Week 05 Preview

### What You Will Learn Next Week

Week 05 focuses on Android Networking:
- Setting up Retrofit in Android app
- Creating API service interface
- Implementing multipart request body for image upload
- Parsing JSON responses with GSON
- Handling loading states and errors
- Configuring base URL with local IP
- Testing end-to-end: Android → FastAPI → Response → Android UI

### Preparation for Week 05

- [ ] Keep FastAPI server running (you'll need it for testing)
- [ ] Note down your local IP address (Android app will use it)
- [ ] Have test leaf images ready on your phone
- [ ] Review Week 03 camera code (you'll extend it to upload images)

### Week 05 Deliverable Preview

You will implement:
- RetrofitClient setup in Android
- ApiService interface with @Multipart @POST endpoint
- Upload image captured from camera to FastAPI
- Parse JSON response and display disease name, confidence, symptoms
- Show loading indicator while request is in progress
- Handle errors (network failure, invalid response)

**End-to-end flow:**
1. User captures leaf image (Week 03 code)
2. User taps "Analyze" button
3. Loading indicator shows "Analyzing..."
4. Retrofit uploads image to http://10.0.2.2:8000/predict in the emulator (or your laptop LAN IP on a physical phone)
5. FastAPI returns JSON response
6. Android parses JSON using GSON
7. UI updates with disease name and confidence
8. User sees result in ResultActivity

---

**Congratulations on completing Week 04! You now have a working backend that your Android app can communicate with. Week 05 will connect everything together.**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 04 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| **1** | **README.md** ← *You are here* | **Week Overview & Objectives** |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

*(Start of week)* &nbsp;&nbsp;|&nbsp;&nbsp; **Week Overview & Objectives** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Theory & Learning Notes →](learning-notes.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
