# Week 04 Learning Notes: Python FastAPI Backend Development

## Table of Contents
1. [What is a Backend API?](#what-is-a-backend-api)
2. [REST API Fundamentals](#rest-api-fundamentals)
3. [HTTP Protocol Deep Dive](#http-protocol-deep-dive)
4. [FastAPI Framework Explained](#fastapi-framework-explained)
5. [Multipart Form-Data for File Uploads](#multipart-form-data-for-file-uploads)
6. [JSON Response Design](#json-response-design)
7. [Local Network Configuration](#local-network-configuration)
8. [Python Virtual Environments](#python-virtual-environments)
9. [API Testing with Postman](#api-testing-with-postman)
10. [Error Handling and Validation](#error-handling-and-validation)

---

## What is a Backend API?

### The Problem Without a Backend

Imagine your Android app needs to identify plant diseases. You have two options:

**Option 1: Everything on Phone (No Backend)**
- ML model stored in app (50-100 MB)
- All processing happens on phone CPU
- Cannot update model without app update
- Limited to phone's processing power
- Cannot collect usage statistics

**Option 2: Backend API (Client-Server Architecture)**
- Android app (client) sends image to server
- Server has powerful ML model (can be larger, more accurate)
- Server processes image and returns result
- Can update model anytime without user knowing
- Can log requests for analytics
- Can add authentication, user accounts, history sync

LeafGuard uses a **hybrid approach**: Backend API for cloud mode, on-device TFLite for offline mode.

### What is an API?

**API = Application Programming Interface**

It is a way for two applications to communicate. In LeafGuard:
- **Application 1:** Android app (needs disease detection)
- **Application 2:** FastAPI backend (provides disease detection)
- **Interface:** HTTP requests and responses with specific format

**Real-world analogy:**
- You (Android app) go to a restaurant
- Menu (API documentation) tells you what you can order
- You tell waiter (HTTP request) your order: "I want spaghetti"
- Waiter brings food (HTTP response) to your table
- You do not know how kitchen works (server implementation is hidden)

### Why FastAPI?

**Other Python frameworks:**
- Flask: Older, more manual setup, less type safety
- Django: Heavy, meant for full websites with templates
- FastAPI: Modern, fast, built-in validation, auto-documentation

**FastAPI advantages for LeafGuard:**
1. **Type hints:** Catch errors before runtime
2. **Async support:** Handle multiple requests simultaneously
3. **Auto docs:** Swagger UI at /docs endpoint
4. **Fast:** Built on Starlette (async) and Pydantic (validation)
5. **Easy file handling:** Built-in multipart support
6. **JSON-first:** Perfect for mobile apps

---

## REST API Fundamentals

### What is REST?

**REST = Representational State Transfer**

It is an architectural style for designing networked applications. Not a protocol or standard, just a pattern.

**6 REST Principles:**

1. **Client-Server Separation**
   - Client (Android) and server (FastAPI) are independent
   - They can evolve separately
   - Client doesn't know server implementation details

2. **Stateless**
   - Each request contains all information needed
   - Server does not remember previous requests
   - No session stored on server
   - Example: Every request includes auth token, not just first request

3. **Cacheable**
   - Responses can be cached to improve performance
   - Example: Disease info can be cached, predictions should not be

4. **Uniform Interface**
   - Consistent way to interact with resources
   - Use HTTP methods properly (GET, POST, PUT, DELETE)
   - Use proper status codes (200, 404, 500)

5. **Layered System**
   - Client doesn't know if it's talking directly to server or through proxy
   - Can add load balancers, caches without client knowing

6. **Code on Demand (Optional)**
   - Server can send executable code to client
   - Not used in LeafGuard

### Resources and Endpoints

**Resource:** A thing your API manages (disease predictions, scan history, user accounts)

**Endpoint:** A URL that represents a resource

**LeafGuard API Resources:**

```
Resource: Disease Prediction
Endpoint: POST /api/predict
Purpose: Upload image, get disease prediction

Resource: Disease Information
Endpoint: GET /api/diseases/{name}
Purpose: Get detailed info about a specific disease

Resource: Health Check
Endpoint: GET /
Purpose: Check if server is running
```

### HTTP Methods (Verbs)

**GET:** Retrieve data (no changes to server)
- Example: GET /api/diseases/tomato-blight
- Safe: Reading does not modify anything
- Idempotent: Calling 10 times has same effect as calling once

**POST:** Create new resource or submit data
- Example: POST /api/predict (upload image)
- Not safe: Changes server state (creates prediction record)
- Not idempotent: Calling 10 times creates 10 predictions

**PUT:** Update existing resource (replace entirely)
- Example: PUT /api/users/123 (update user profile)
- Not used in Week 04

**DELETE:** Remove resource
- Example: DELETE /api/scans/456 (delete scan history)
- Not used in Week 04

**PATCH:** Partially update resource
- Example: PATCH /api/users/123 (update only email field)
- Not used in Week 04

**For LeafGuard Week 04:**
- We use GET for health check
- We use POST for /predict (sending image data)

### Status Codes

**2xx Success:**
- 200 OK: Request succeeded, response contains data
- 201 Created: Resource created successfully
- 204 No Content: Request succeeded, no response body

**4xx Client Errors:**
- 400 Bad Request: Invalid data sent (wrong file type, missing field)
- 401 Unauthorized: Authentication required
- 403 Forbidden: Authenticated but not allowed
- 404 Not Found: Endpoint doesn't exist
- 422 Unprocessable Entity: Validation failed (Pydantic validation error)

**5xx Server Errors:**
- 500 Internal Server Error: Something went wrong on server
- 503 Service Unavailable: Server is down or overloaded

**LeafGuard Usage:**
- 200: Successful prediction
- 400: Invalid file type (user uploaded .txt instead of .jpg)
- 500: ML model crashed or unexpected exception

---

## HTTP Protocol Deep Dive

### What is HTTP?

**HTTP = HyperText Transfer Protocol**

A protocol for transferring data over the internet. Every web page, API call, file download uses HTTP/HTTPS.

**HTTPS = HTTP + TLS (encryption)**

### Request Structure

Every HTTP request has:

```http
POST /api/predict HTTP/1.1
Host: 192.168.1.100:8000
Content-Type: multipart/form-data; boundary=----Boundary123
Accept: application/json
User-Agent: Retrofit/2.9.0
Content-Length: 2048576

------Boundary123
Content-Disposition: form-data; name="file"; filename="leaf.jpg"
Content-Type: image/jpeg

[binary image data]
------Boundary123--
```

**Components:**

1. **Request Line:** `POST /api/predict HTTP/1.1`
   - Method: POST
   - Path: /api/predict
   - Protocol version: HTTP/1.1

2. **Headers:** Key-value pairs with metadata
   - Host: Which server to contact
   - Content-Type: What kind of data we're sending
   - Accept: What kind of response we want
   - User-Agent: What client is making request

3. **Body:** The actual data (image file in our case)

### Response Structure

```http
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 456
Date: Mon, 15 Jan 2024 10:30:00 GMT

{
  "success": true,
  "disease": "Tomato Early Blight",
  "confidence": 0.92
}
```

**Components:**

1. **Status Line:** `HTTP/1.1 200 OK`
   - Protocol version: HTTP/1.1
   - Status code: 200
   - Status text: OK

2. **Headers:**
   - Content-Type: We're returning JSON
   - Content-Length: Size of response body
   - Date: When response was generated

3. **Body:** JSON data with prediction

### Request-Response Cycle

```
ANDROID APP                         FASTAPI BACKEND
    |                                      |
    |  1. User taps "Analyze" button      |
    |                                      |
    |  2. Retrofit prepares HTTP request  |
    |     - Method: POST                   |
    |     - URL: /api/predict              |
    |     - Body: image file               |
    |                                      |
    |------ 3. Send request over WiFi ---->|
    |                                      |
    |                          4. FastAPI receives request
    |                          5. Validate file type
    |                          6. Process image (dummy for now)
    |                          7. Prepare JSON response
    |                                      |
    |<----- 8. Send response --------------|
    |                                      |
    |  9. Retrofit parses JSON             |
    | 10. Update UI with disease name      |
    |                                      |
```

**Timeline:** Total ~2-5 seconds
- Network latency: 50-200ms
- File upload: 1-3 seconds (depends on image size and WiFi speed)
- Processing: 100ms (dummy), 1-2 seconds (real ML model)
- Response download: 10-50ms (JSON is small)

---

## FastAPI Framework Explained

### Installing FastAPI

```bash
# Create virtual environment
python -m venv venv

# Activate
source venv/bin/activate  # Mac/Linux
venv\Scripts\activate     # Windows

# Install FastAPI
pip install fastapi

# Install ASGI server (Uvicorn)
pip install uvicorn

# Install optional dependencies
pip install python-multipart  # For file uploads
```

### Basic FastAPI Application

```python
from fastapi import FastAPI

app = FastAPI()

@app.get("/")
def root():
    return {"message": "Hello World"}
```

**Run it:**
```bash
uvicorn main:app --reload
```

**What happens:**
1. Uvicorn imports `app` from `main.py`
2. Uvicorn starts server on http://127.0.0.1:8000
3. GET request to / calls `root()` function
4. Function returns dict, FastAPI converts to JSON
5. Response sent with Content-Type: application/json

### Path Parameters

```python
@app.get("/diseases/{disease_name}")
def get_disease(disease_name: str):
    return {"disease": disease_name, "info": "Details here"}
```

**Usage:** GET /diseases/tomato-blight
**Result:** `{"disease": "tomato-blight", "info": "Details here"}`

### Query Parameters

```python
@app.get("/search")
def search_diseases(query: str, limit: int = 10):
    return {"query": query, "limit": limit}
```

**Usage:** GET /search?query=tomato&limit=5
**Result:** `{"query": "tomato", "limit": 5}`

### Request Body (POST)

```python
from pydantic import BaseModel

class ScanRequest(BaseModel):
    image_url: str
    mode: str

@app.post("/scan")
def scan_plant(request: ScanRequest):
    return {"received": request.image_url, "mode": request.mode}
```

**Usage:** POST /scan with body `{"image_url": "http://...", "mode": "cloud"}`

### File Uploads

```python
from fastapi import UploadFile, File

@app.post("/upload")
async def upload_file(file: UploadFile = File(...)):
    contents = await file.read()
    return {
        "filename": file.filename,
        "content_type": file.content_type,
        "size": len(contents)
    }
```

**Key points:**
- `async def`: Allows handling multiple uploads simultaneously
- `await file.read()`: Asynchronous file reading
- `File(...)`: The `...` means this parameter is required

### Response Models

```python
from pydantic import BaseModel

class PredictionResponse(BaseModel):
    disease: str
    confidence: float
    symptoms: str

@app.post("/predict", response_model=PredictionResponse)
def predict(file: UploadFile):
    return {
        "disease": "Tomato Blight",
        "confidence": 0.92,
        "symptoms": "Dark spots on leaves"
    }
```

**Benefits of response_model:**
- FastAPI validates response matches model
- Auto-generates API documentation
- Excludes extra fields not in model
- Catches bugs (if you return wrong structure, FastAPI errors)

### Error Handling

```python
from fastapi import HTTPException

@app.post("/predict")
def predict(file: UploadFile):
    if file.content_type not in ["image/jpeg", "image/png"]:
        raise HTTPException(
            status_code=400,
            detail="Only JPEG and PNG files are supported"
        )
    return {"disease": "Unknown"}
```

**HTTPException:**
- Stops function execution
- Returns error response to client
- Android app receives 400 status with error message

### Middleware (CORS)

```python
from fastapi.middleware.cors import CORSMiddleware

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Allow all origins (Android app)
    allow_methods=["*"],
    allow_headers=["*"],
)
```

**Why CORS?**
- Browsers block cross-origin requests by default
- Android isn't a browser, but Retrofit respects CORS headers
- `allow_origins=["*"]` means "accept requests from any origin"
- In production, specify exact Android app origin

### Automatic Documentation

FastAPI generates interactive API docs automatically.

**Swagger UI:** http://localhost:8000/docs
- Interactive: You can test endpoints in browser
- Shows all endpoints, parameters, responses
- Try out file uploads directly

**ReDoc:** http://localhost:8000/redoc
- Alternative documentation format
- Better for printing or reading

---

## Multipart Form-Data for File Uploads

### Why Not JSON for Images?

**Option 1: Send image as base64 in JSON**
```json
{
  "image": "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAA..."
}
```

**Problems:**
- Base64 increases size by 33% (1 MB image becomes 1.33 MB)
- Inefficient encoding/decoding
- JSON parsers struggle with huge strings

**Option 2: Multipart form-data**
- Send binary image directly
- No encoding overhead
- Standard way to upload files
- Supported by all frameworks

### Multipart Request Structure

```http
POST /api/predict HTTP/1.1
Host: 192.168.1.100:8000
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Length: 2048576

------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="file"; filename="leaf.jpg"
Content-Type: image/jpeg

[binary image data - 2 MB of raw bytes]
------WebKitFormBoundary7MA4YWxkTrZu0gW--
```

**Parts:**
1. **Boundary:** Random string separating parts (like `----WebKitForm...`)
2. **Content-Disposition:** Tells server this is a file field named "file"
3. **filename:** Original filename from user's device
4. **Content-Type:** MIME type of file (image/jpeg, image/png)
5. **Binary data:** Raw image bytes

### Handling in FastAPI

```python
from fastapi import UploadFile, File

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    # Read file contents
    contents = await file.read()

    # Access metadata
    filename = file.filename        # "leaf.jpg"
    content_type = file.content_type  # "image/jpeg"
    size = len(contents)             # 2048576 bytes

    # Save to disk (optional)
    with open(f"uploads/{filename}", "wb") as f:
        f.write(contents)

    return {"filename": filename, "size": size}
```

**Key points:**
- `UploadFile` is a FastAPI class wrapping uploaded file
- `await file.read()` reads entire file into memory
- File is automatically deleted after request completes (unless you save it)

### Multiple File Uploads

```python
from typing import List

@app.post("/upload-multiple")
async def upload_multiple(files: List[UploadFile] = File(...)):
    filenames = [file.filename for file in files]
    return {"uploaded": filenames}
```

### Mixing Files and JSON

```python
from fastapi import Form

@app.post("/predict")
async def predict(
    file: UploadFile = File(...),
    mode: str = Form(...),
    user_id: str = Form(...)
):
    return {
        "file": file.filename,
        "mode": mode,
        "user": user_id
    }
```

**Request would include:**
- `file` part: Image binary data
- `mode` part: "cloud" or "offline" (text)
- `user_id` part: "user123" (text)

---

## JSON Response Design

### Why JSON?

**Alternatives:**
- **XML:** Verbose, harder to parse in Android
- **Plain text:** No structure, hard to extend
- **Binary protocols (Protobuf):** Efficient but complex

**JSON advantages:**
- Human-readable (easy to debug)
- Lightweight (less bandwidth than XML)
- Native JavaScript support (good for web dashboards)
- Easy to parse in Android (GSON, Moshi)
- Flexible (add fields without breaking old clients)

### LeafGuard Response Design

**Success Response:**
```json
{
  "success": true,
  "disease": "Tomato Early Blight",
  "confidence": 0.923,
  "symptoms": "Dark brown spots with concentric rings on lower leaves. Yellowing around spots.",
  "treatment": "Apply fungicide containing chlorothalonil or mancozeb. Remove infected leaves.",
  "prevention": "Rotate crops annually. Avoid overhead watering. Ensure good air circulation.",
  "timestamp": "2024-01-15T10:30:00Z",
  "file_size": 2048576
}
```

**Field explanations:**

- `success` (boolean): Allows Android to quickly check if request succeeded
- `disease` (string): Disease name, displayed prominently in UI
- `confidence` (float): 0.0 to 1.0, Android converts to percentage (92.3%)
- `symptoms` (string): Educational content, displayed in result screen
- `treatment` (string): Actionable advice, helps farmers
- `prevention` (string): Long-term guidance
- `timestamp` (string): ISO 8601 format, Android parses for history
- `file_size` (int): For debugging, shows what was received

**Error Response:**
```json
{
  "success": false,
  "error": "Invalid file format",
  "detail": "Uploaded file must be JPEG or PNG. Received: image/gif"
}
```

**Error fields:**

- `success` (boolean): false indicates error
- `error` (string): High-level error message, displayed to user
- `detail` (string): Technical details, for debugging

### Response Model in FastAPI

```python
from pydantic import BaseModel, Field
from typing import Optional

class PredictionResponse(BaseModel):
    success: bool = True
    disease: str = Field(..., min_length=1, max_length=100)
    confidence: float = Field(..., ge=0.0, le=1.0)
    symptoms: str
    treatment: str
    prevention: str
    timestamp: str
    file_size: Optional[int] = None

class ErrorResponse(BaseModel):
    success: bool = False
    error: str
    detail: Optional[str] = None
```

**Pydantic validation:**
- `Field(...)`: Required field (no default value)
- `min_length=1`: String must have at least 1 character
- `ge=0.0, le=1.0`: Float must be between 0.0 and 1.0 (greater-or-equal, less-or-equal)
- `Optional[int]`: Can be int or None

**Automatic validation:**
If you return `{"disease": "", "confidence": 1.5}`, Pydantic raises error because:
- `disease` is empty (violates min_length=1)
- `confidence` is 1.5 (violates le=1.0)

---

## Local Network Configuration

### Understanding IP Addresses

**127.0.0.1 (localhost):**
- Special IP meaning "this device"
- On your laptop, http://127.0.0.1:8000 works
- On your phone, http://127.0.0.1:8000 refers to the phone itself
- **Never use 127.0.0.1 in Android app**

**192.168.x.x (Local network):**
- Private IP addresses for devices on same Wi-Fi
- Router assigns these addresses (DHCP)
- Example: Laptop is 192.168.1.100, Phone is 192.168.1.105
- Devices on same network can reach each other

**Public IP:**
- IP address visible to entire internet
- Assigned by ISP (Internet Service Provider)
- Used when deploying to cloud (AWS, Heroku)
- Not used in Week 04 (local development only)

### Finding Your Local IP

**Windows:**
```cmd
ipconfig
```
Look for "IPv4 Address" under "Wi-Fi" adapter:
```
Wireless LAN adapter Wi-Fi:
   IPv4 Address. . . . . . . . . . . : 192.168.1.100
```

**Mac:**
```bash
ifconfig en0
```
Look for "inet" line:
```
inet 192.168.1.100 netmask 0xffffff00 broadcast 192.168.1.255
```

**Linux:**
```bash
ip addr show wlan0
# or
ifconfig wlan0
```

**Alternative (works on all platforms):**
- Connect to Wi-Fi
- Open router admin page (usually 192.168.1.1 or 192.168.0.1)
- Look for "Connected Devices" list
- Find your laptop's name and its IP

### Running Server for Network Access

**Wrong (only localhost):**
```bash
uvicorn main:app --reload
# Binds to 127.0.0.1:8000
# Only your laptop can access
```

**Correct (network access):**
```bash
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
# Binds to all network interfaces
# Laptop, phone, other devices can access
```

**What `--host 0.0.0.0` means:**
- "Listen on all available network interfaces"
- Accepts connections from 127.0.0.1 (localhost)
- Accepts connections from 192.168.1.x (local network)
- Does NOT accept connections from internet (router blocks by default)

### Testing from Phone

**Step 1: Connect to same Wi-Fi**
- Laptop on "Home WiFi"
- Phone on "Home WiFi"
- NOT: Laptop on Wi-Fi, phone on mobile data

**Step 2: Start server**
```bash
uvicorn main:app --host 0.0.0.0 --port 8000
```

**Step 3: Open phone browser**
- Navigate to: http://192.168.1.100:8000/
- Replace 192.168.1.100 with YOUR laptop's IP
- Should see: `{"status": "online", "message": "LeafGuard AI Backend is running"}`

**Step 4: Test /docs**
- Navigate to: http://192.168.1.100:8000/docs
- Should see Swagger UI
- Try uploading image directly from phone

### Firewall Configuration

**Windows:**
1. Open Windows Security
2. Firewall & network protection
3. Allow an app through firewall
4. Click "Change settings"
5. Find Python or allow Python through firewall
6. Check both "Private" and "Public" boxes
7. OK

**Mac:**
1. System Preferences → Security & Privacy
2. Firewall tab
3. Click lock to make changes
4. Firewall Options
5. Add Python to allowed applications

**Linux:**
```bash
# Ubuntu/Debian with UFW
sudo ufw allow 8000/tcp

# Check status
sudo ufw status
```

### Common Network Issues

**Issue 1: Connection refused**
- **Cause:** Server not running or firewall blocking
- **Fix:** Verify server is running, check firewall

**Issue 2: Connection timeout**
- **Cause:** Devices on different networks, wrong IP
- **Fix:** Verify both on same Wi-Fi, verify IP address

**Issue 3: IP address changes**
- **Cause:** Router DHCP assigns new IP after restart
- **Fix:** Check IP before each session, or configure static IP in router

**Issue 4: Server crashes when phone connects**
- **Cause:** Bug in code (unhandled exception)
- **Fix:** Check server logs (terminal output), fix bug

---

## Python Virtual Environments

### Why Virtual Environments?

**Scenario without venv:**
- You install FastAPI 0.95.0 for Project A
- Later, Project B needs FastAPI 0.100.0
- You run `pip install fastapi==0.100.0`
- Project A now breaks because it's not compatible with 0.100.0

**Scenario with venv:**
- Project A has its own venv with FastAPI 0.95.0
- Project B has its own venv with FastAPI 0.100.0
- Projects don't interfere with each other

### Creating Virtual Environment

```bash
# Navigate to project folder
cd leafguard-backend

# Create venv
python -m venv venv
# or on some systems:
python3 -m venv venv
```

**What this creates:**
```
leafguard-backend/
└── venv/
    ├── bin/        # (Mac/Linux) Contains python, pip executables
    ├── Scripts/    # (Windows) Contains python.exe, pip.exe
    ├── include/    # C headers
    ├── lib/        # Installed packages go here
    └── pyvenv.cfg  # Configuration
```

### Activating Virtual Environment

**Mac/Linux:**
```bash
source venv/bin/activate
```

**Windows (Command Prompt):**
```cmd
venv\Scripts\activate
```

**Windows (PowerShell):**
```powershell
venv\Scripts\Activate.ps1
```

**How to tell it's active:**
- Your terminal shows `(venv)` prefix:
  ```
  (venv) user@laptop:~/leafguard-backend$
  ```

**What activation does:**
- Changes `python` command to point to venv's Python
- Changes `pip` command to point to venv's pip
- Installed packages go into venv, not system-wide

### Installing Packages

```bash
# Make sure venv is activated (see (venv) prefix)
pip install fastapi
pip install uvicorn
pip install python-multipart

# Verify installation
pip list
```

**Output:**
```
Package           Version
----------------- -------
fastapi           0.104.1
uvicorn           0.24.0
python-multipart  0.0.6
...
```

### Requirements File

**Creating requirements.txt:**
```bash
pip freeze > requirements.txt
```

**What it contains:**
```
fastapi==0.104.1
uvicorn==0.24.0
python-multipart==0.0.6
pydantic==2.5.0
starlette==0.27.0
...
```

**Installing from requirements.txt:**
```bash
# New machine or collaborator
git clone [repo]
cd leafguard-backend
python -m venv venv
source venv/bin/activate  # or venv\Scripts\activate on Windows
pip install -r requirements.txt
```

**Benefits:**
- One command installs all dependencies
- Ensures same versions across machines
- Required for deployment (Heroku, AWS, etc.)

### Deactivating Virtual Environment

```bash
deactivate
```

**What this does:**
- Returns to system Python
- `(venv)` prefix disappears
- `pip install` now installs system-wide (usually not what you want)

### Virtual Environment in .gitignore

**Always exclude venv from Git:**

`.gitignore`:
```
venv/
env/
ENV/
*.pyc
__pycache__/
```

**Why:**
- venv folder is huge (100+ MB)
- Contains OS-specific binaries
- Other developers should create their own venv
- requirements.txt is enough to recreate environment

---

## API Testing with Postman

### What is Postman?

A tool for testing APIs without writing code. Before Retrofit integration, we use Postman to verify backend works.

**Alternatives:**
- curl (command-line tool)
- HTTPie (better command-line tool)
- Insomnia (similar to Postman)
- Browser (only for GET requests)

### Installing Postman

1. Go to https://www.postman.com/downloads/
2. Download for your OS (Windows/Mac/Linux)
3. Install (no account required for basic use)
4. Open app

### Creating a Request

**Step 1: New Request**
- Click "New" → "HTTP Request"
- Or click "+" tab

**Step 2: Configure Request**
- Method: POST
- URL: http://localhost:8000/api/predict
- Headers: (auto-configured for multipart)
- Body: Select "form-data"
  - Key: `file`
  - Type: File (dropdown)
  - Value: Select image file

**Step 3: Send**
- Click "Send" button
- View response in lower panel

**Expected Response:**
```json
{
  "success": true,
  "disease": "Tomato Early Blight",
  "confidence": 0.89,
  "symptoms": "Dark brown spots...",
  "treatment": "Apply fungicide...",
  "prevention": "Rotate crops...",
  "timestamp": "2024-01-15T10:30:00Z",
  "file_size": 2048576
}
```

### Collections

**What is a collection?**
- Group of related requests
- Like a folder for API endpoints

**Creating Collection:**
1. Click "Collections" in left sidebar
2. Click "New Collection"
3. Name: "LeafGuard API"
4. Add description

**Adding Requests to Collection:**
1. Create request (GET / for health check)
2. Click "Save"
3. Select "LeafGuard API" collection
4. Name request "Health Check"
5. Save

**Requests to create:**
- Health Check (GET /)
- Predict Disease (POST /api/predict with image)
- Predict Error (POST /api/predict with .txt file)

### Environment Variables

**Problem:** IP address is hardcoded (http://192.168.1.100:8000)

**Solution:** Use variables

**Creating Environment:**
1. Click gear icon (top right)
2. "Add" environment
3. Name: "Local Development"
4. Add variable:
   - Variable: `base_url`
   - Initial Value: `http://192.168.1.100:8000`
   - Current Value: `http://192.168.1.100:8000`
5. Save

**Using Variable:**
- Change URL from `http://192.168.1.100:8000/api/predict`
- To: `{{base_url}}/api/predict`
- Select "Local Development" environment (dropdown top right)

**Benefits:**
- Change IP once, updates all requests
- Easy to switch between environments (local, staging, production)

### Exporting Collection

**For evidence submission:**
1. Right-click collection "LeafGuard API"
2. Export
3. Select "Collection v2.1" format
4. Save as `leafguard-api-postman.json`
5. Add to evidence folder

---

## Error Handling and Validation

### Why Error Handling Matters

**Bad API (no error handling):**
```python
@app.post("/predict")
async def predict(file: UploadFile):
    contents = await file.read()
    # Crashes if file is not an image
    image = Image.open(io.BytesIO(contents))
    result = model.predict(image)
    return {"disease": result}
```

**What goes wrong:**
- User uploads .txt file → Server crashes with 500 error
- User uploads corrupt image → Server crashes
- User uploads 100 MB file → Server runs out of memory
- Android app has no idea what went wrong

**Good API (with error handling):**
```python
@app.post("/predict")
async def predict(file: UploadFile):
    # Validate file type
    if file.content_type not in ["image/jpeg", "image/png"]:
        raise HTTPException(
            status_code=400,
            detail="Only JPEG and PNG files are supported"
        )

    # Validate file size
    contents = await file.read()
    if len(contents) > 10 * 1024 * 1024:  # 10 MB
        raise HTTPException(
            status_code=400,
            detail="File too large. Maximum size is 10 MB"
        )

    try:
        image = Image.open(io.BytesIO(contents))
        result = model.predict(image)
        return {"disease": result}
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Failed to process image: {str(e)}"
        )
```

**Benefits:**
- Clear error messages
- Appropriate status codes (400 for client errors, 500 for server errors)
- Android can handle errors gracefully (show error dialog, not crash)

### Input Validation

**File Type Validation:**
```python
def validate_image(file: UploadFile) -> str:
    """Returns error message if invalid, None if valid"""

    # Check MIME type
    if file.content_type not in ["image/jpeg", "image/png"]:
        return f"Invalid content type: {file.content_type}"

    # Check file extension
    if file.filename:
        ext = file.filename.split(".")[-1].lower()
        if ext not in ["jpg", "jpeg", "png"]:
            return f"Invalid file extension: .{ext}"

    return None  # Valid
```

**File Size Validation:**
```python
MAX_FILE_SIZE = 10 * 1024 * 1024  # 10 MB

async def validate_file_size(file: UploadFile):
    contents = await file.read()
    if len(contents) > MAX_FILE_SIZE:
        raise HTTPException(
            status_code=400,
            detail=f"File too large: {len(contents)} bytes. Max: {MAX_FILE_SIZE} bytes"
        )
    # Reset file pointer so it can be read again
    await file.seek(0)
    return contents
```

### Exception Handling

**Try-Except Blocks:**
```python
@app.post("/predict")
async def predict(file: UploadFile):
    try:
        # Potentially dangerous code
        contents = await file.read()
        image = process_image(contents)
        result = run_model(image)
        return {"disease": result}

    except FileNotFoundError:
        raise HTTPException(status_code=404, detail="Resource not found")

    except ValueError as e:
        raise HTTPException(status_code=400, detail=f"Invalid input: {str(e)}")

    except Exception as e:
        # Catch-all for unexpected errors
        print(f"Unexpected error: {e}")  # Log for debugging
        raise HTTPException(
            status_code=500,
            detail="Internal server error. Please try again."
        )
```

**Specific exceptions first, generic last:**
- Check for known exceptions (FileNotFoundError, ValueError)
- Catch generic Exception as fallback
- Never return sensitive error details to client (no stack traces)

### Logging Errors

**Python logging module:**
```python
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

@app.post("/predict")
async def predict(file: UploadFile):
    logger.info(f"Received prediction request: {file.filename}")

    try:
        # Process
        result = process_image(file)
        logger.info(f"Prediction successful: {result}")
        return result

    except Exception as e:
        logger.error(f"Prediction failed: {str(e)}", exc_info=True)
        raise HTTPException(status_code=500, detail="Processing failed")
```

**Benefits:**
- Errors logged to console (or file)
- `exc_info=True` includes stack trace
- Easy to debug issues after they happen

### User-Friendly Error Messages

**Bad error message:**
```json
{
  "error": "NoneType object has no attribute 'shape'"
}
```
User has no idea what this means.

**Good error message:**
```json
{
  "success": false,
  "error": "Invalid image file",
  "detail": "The uploaded file could not be processed. Please ensure it's a valid JPEG or PNG image."
}
```

**Guidelines:**
- Use simple language (no technical jargon)
- Explain what went wrong
- Suggest how to fix it
- Include error code for support (optional)

---

**These learning notes cover the theoretical foundations of Week 04. For hands-on practice, proceed to exercises.md and build-task.md. Test your understanding with quiz.md after completion.**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 04 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| **2** | **learning-notes.md** ← *You are here* | **Theory & Learning Notes** |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Week Overview & Objectives](README.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Theory & Learning Notes** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Practice Exercises →](exercises.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
