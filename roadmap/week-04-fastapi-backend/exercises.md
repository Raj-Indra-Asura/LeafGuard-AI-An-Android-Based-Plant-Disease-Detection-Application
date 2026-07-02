# Week 04 Exercises: Python FastAPI Backend Development

## Exercise Overview

These exercises build your FastAPI backend skills progressively. Start with Exercise 1 and complete them in order. Each exercise includes clear goals, starter guidance, and validation steps.

---

## Exercise 1: FastAPI Hello World

### Goal
Create your first FastAPI application with a health check endpoint and explore auto-generated documentation.

### Learning Objectives
- Set up Python virtual environment
- Install FastAPI and Uvicorn
- Create basic endpoint
- Run server and test with browser
- Explore Swagger UI documentation

### Instructions

**Step 1: Project Setup**
1. Create a folder: `mkdir fastapi-exercises`
2. Navigate into it: `cd fastapi-exercises`
3. Create virtual environment: `python -m venv venv`
4. Activate venv:
   - Mac/Linux: `source venv/bin/activate`
   - Windows: `venv\Scripts\activate`
5. Verify activation (should see `(venv)` prefix in terminal)

**Step 2: Install Dependencies**
```bash
pip install fastapi uvicorn
```

**Step 3: Create main.py**

Create a file named `main.py` with this starting point:

```python
from fastapi import FastAPI

app = FastAPI(title="My First API")

# TODO: Create a GET endpoint at root ("/") that returns a welcome message
# Hint: Use @app.get("/") decorator
# Return format: {"message": "Welcome to my API", "version": "1.0"}
```

**Step 4: Implement the Endpoint**

Complete the TODO by creating a function that returns a dictionary with:
- `message`: A welcome string
- `version`: Your API version number
- `status`: String "online"

**Step 5: Run Server**
```bash
uvicorn main:app --reload
```

**Step 6: Test**
1. Open browser: http://localhost:8000
2. Should see your JSON response
3. Open Swagger docs: http://localhost:8000/docs
4. Try your endpoint in Swagger UI

### Validation Checklist
- [ ] Virtual environment activated
- [ ] FastAPI and uvicorn installed
- [ ] Server starts without errors
- [ ] GET / returns JSON with message, version, status
- [ ] Swagger UI accessible at /docs
- [ ] Can execute request from Swagger UI

### Expected Output

**Browser (http://localhost:8000):**
```json
{
  "message": "Welcome to my API",
  "version": "1.0",
  "status": "online"
}
```

**Terminal:**
```
INFO:     Uvicorn running on http://127.0.0.1:8000 (Press CTRL+C to quit)
INFO:     Started reloader process [12345] using StatReload
INFO:     Started server process [12346]
INFO:     Waiting for application startup.
INFO:     Application startup complete.
```

### Extension Challenge
Add a second endpoint GET /info that returns:
- Server name
- Current timestamp
- Python version (hint: `import sys; sys.version`)

---

## Exercise 2: Path and Query Parameters

### Goal
Learn to accept user input through URL path parameters and query strings.

### Learning Objectives
- Use path parameters to capture URL segments
- Use query parameters for optional filters
- Validate parameter types with type hints
- Return dynamic responses based on parameters

### Instructions

**Create a new file: `exercise2.py`**

Starting code:

```python
from fastapi import FastAPI

app = FastAPI()

# TODO 1: Create endpoint GET /greet/{name}
# Path parameter: name (string)
# Return: {"greeting": "Hello, {name}!"}

# TODO 2: Create endpoint GET /calculate
# Query parameters: a (int), b (int), operation (str, default="add")
# Operations: add, subtract, multiply, divide
# Return: {"a": a, "b": b, "operation": operation, "result": calculated_value}

# TODO 3: Create endpoint GET /diseases/{disease_name}
# Path parameter: disease_name (string)
# Query parameter: details (bool, default=False)
# If details=True: Return name, symptoms, treatment
# If details=False: Return only name
```

### Implementation Hints

**Path Parameter Example:**
```python
@app.get("/user/{user_id}")
def get_user(user_id: int):
    return {"user_id": user_id}
```

**Query Parameter Example:**
```python
@app.get("/search")
def search(query: str, limit: int = 10):
    return {"query": query, "limit": limit}
```

**Combining Both:**
```python
@app.get("/items/{item_id}")
def get_item(item_id: int, format: str = "json"):
    return {"item_id": item_id, "format": format}
```

### Test Cases

**Test 1: Path Parameter**
```
GET /greet/Alice
Expected: {"greeting": "Hello, Alice!"}
```

**Test 2: Query Parameters**
```
GET /calculate?a=10&b=5&operation=multiply
Expected: {"a": 10, "b": 5, "operation": "multiply", "result": 50}
```

**Test 3: Default Query Parameter**
```
GET /calculate?a=8&b=2
Expected: {"a": 8, "b": 2, "operation": "add", "result": 10}
```

**Test 4: Boolean Query Parameter**
```
GET /diseases/tomato-blight?details=true
Expected: Full disease information

GET /diseases/tomato-blight
Expected: Only disease name
```

### Validation Checklist
- [ ] GET /greet/{name} works with any name
- [ ] GET /calculate performs all four operations correctly
- [ ] Default operation is "add" when not specified
- [ ] Division by zero handled gracefully (return error message)
- [ ] GET /diseases/{disease_name} respects details parameter
- [ ] Type validation works (sending string for integer fails appropriately)

---

## Exercise 3: File Upload with Validation

### Goal
Implement file upload endpoint with proper validation and error handling.

### Learning Objectives
- Handle multipart form-data file uploads
- Validate file type and size
- Read file contents
- Return appropriate error messages
- Save files to disk

### Instructions

**Create: `exercise3.py`**

Starting code:

```python
from fastapi import FastAPI, UploadFile, File, HTTPException
import os

app = FastAPI()

UPLOAD_DIR = "uploads"
os.makedirs(UPLOAD_DIR, exist_ok=True)

# TODO 1: Create POST /upload endpoint
# Accept: `image` (UploadFile) for the LeafGuard `/predict` contract
# Validate:
#   - Content type must be image/jpeg or image/png
#   - File size must be <= 5 MB
# Save file to uploads/ directory
# Return: filename, size, content_type, saved_path

# TODO 2: Create POST /analyze-image endpoint (dummy processing)
# Accept: `image` (UploadFile) for the LeafGuard `/predict` contract
# Validate: Same as above
# Simulate processing (you can just return random analysis)
# Return: analysis results (width, height, format, file_size)
```

### Implementation Steps

**Step 1: Basic File Upload**

```python
@app.post("/upload")
async def upload_file(file: UploadFile = File(...)):
    # Read file contents
    contents = await file.read()

    # Validate content type
    if file.content_type not in ["image/jpeg", "image/png"]:
        raise HTTPException(
            status_code=400,
            detail=f"Invalid file type: {file.content_type}"
        )

    # TODO: Add size validation
    # TODO: Save file
    # TODO: Return response
```

**Step 2: Size Validation**

Check if `len(contents)` exceeds 5 MB (5 * 1024 * 1024 bytes). If so, raise HTTPException with status 400.

**Step 3: Save File**

```python
file_path = os.path.join(UPLOAD_DIR, file.filename)
with open(file_path, "wb") as f:
    f.write(contents)
```

**Step 4: Return Response**

Return dictionary with filename, size, content_type, saved_path.

### Test Cases

**Test 1: Valid Upload**
- Upload a small JPEG image
- Should succeed with 200 status
- File should appear in uploads/ directory

**Test 2: Invalid File Type**
- Upload a .txt file
- Should fail with 400 status
- Error message should mention invalid type

**Test 3: File Too Large**
- Create or find a >5 MB image
- Should fail with 400 status
- Error message should mention size limit

**Test 4: Multiple Uploads**
- Upload 3 different images sequentially
- All should succeed
- All files should be saved

### Validation Checklist
- [ ] python-multipart installed (`pip install python-multipart`)
- [ ] uploads/ directory created
- [ ] JPEG images upload successfully
- [ ] PNG images upload successfully
- [ ] TXT files rejected with clear error
- [ ] Large files (>5 MB) rejected
- [ ] Files saved with correct names
- [ ] Response includes all required fields

### Extension Challenge

Add image dimension validation:
- Use PIL (Pillow): `pip install pillow`
- Read image: `from PIL import Image; img = Image.open(io.BytesIO(contents))`
- Check dimensions: `width, height = img.size`
- Reject images smaller than 100x100 or larger than 5000x5000

---

## Exercise 4: Pydantic Response Models

### Goal
Use Pydantic models to structure and validate API responses.

### Learning Objectives
- Define Pydantic BaseModel classes
- Use response_model in endpoint decorators
- Validate response data automatically
- Handle optional fields

### Instructions

**Create: `exercise4.py`**

Starting code:

```python
from fastapi import FastAPI
from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime

app = FastAPI()

# TODO 1: Define DiseaseResponse model with fields:
# - disease: str (required, min_length=1, max_length=100)
# - confidence: float (required, must be between 0.0 and 1.0)
# - symptoms: str (required)
# - treatment: str (required)
# - timestamp: str (required)

class DiseaseResponse(BaseModel):
    # Your model here
    pass

# TODO 2: Define ErrorResponse model with fields:
# - success: bool (default False)
# - error: str (required)
# - detail: Optional[str] (can be None)

# TODO 3: Create POST /predict-dummy endpoint
# response_model=DiseaseResponse
# Return dummy disease prediction data
# Ensure all fields match the model

# TODO 4: Create GET /test-validation endpoint
# Try returning data that violates model constraints
# Observe FastAPI validation error
```

### Implementation Hints

**Defining Model with Validation:**

```python
class PersonModel(BaseModel):
    name: str = Field(..., min_length=1, max_length=50)
    age: int = Field(..., ge=0, le=150)  # greater-equal, less-equal
    email: Optional[str] = None

# Usage in endpoint:
@app.post("/person", response_model=PersonModel)
def create_person():
    return {
        "name": "Alice",
        "age": 30,
        "email": "alice@example.com"
    }
```

**Field Constraints:**
- `min_length`, `max_length`: String length
- `ge`, `gt`: Greater than or equal, greater than (numbers)
- `le`, `lt`: Less than or equal, less than
- `regex`: Regular expression pattern

### Test Cases

**Test 1: Valid Response**
```python
# POST /predict-dummy should return valid DiseaseResponse
{
  "disease": "Tomato Blight",
  "confidence": 0.92,
  "symptoms": "Dark spots on leaves",
  "treatment": "Apply fungicide",
  "timestamp": "2024-01-15T10:00:00Z"
}
```

**Test 2: Invalid Confidence (>1.0)**
```python
# Modify code to return confidence=1.5
# FastAPI should raise validation error
# Error should mention "le=1.0" constraint
```

**Test 3: Missing Required Field**
```python
# Return response without "treatment" field
# Should fail validation
```

**Test 4: Error Detail**
```python
# FastAPI error responses should include a clear detail message
{
  "detail": "File not found"
}
```

### Validation Checklist
- [ ] DiseaseResponse model defined with all fields
- [ ] Confidence constrained to 0.0-1.0
- [ ] Disease name constrained to 1-100 characters
- [ ] ErrorResponse model has optional detail field
- [ ] POST /predict-dummy returns valid response
- [ ] Validation errors raised for invalid data
- [ ] Swagger UI shows model schemas

### Extension Challenge

Create a HistoryScanModel:
- id: int (auto-generated)
- disease: str
- confidence: float
- image_path: str
- created_at: datetime

Create GET /history endpoint that returns `List[HistoryScanModel]`.

---

## Exercise 5: Error Handling and HTTP Status Codes

### Goal
Implement proper error handling with appropriate HTTP status codes.

### Learning Objectives
- Use HTTPException for error responses
- Return correct status codes (400, 404, 500)
- Handle exceptions with try-except
- Provide user-friendly error messages

### Instructions

**Create: `exercise5.py`**

```python
from fastapi import FastAPI, HTTPException

app = FastAPI()

# Dummy disease database
DISEASES = {
    "tomato-blight": {"name": "Tomato Blight", "severity": "high"},
    "corn-rust": {"name": "Corn Rust", "severity": "medium"},
    "potato-scab": {"name": "Potato Scab", "severity": "low"}
}

# TODO 1: GET /disease/{disease_id}
# If disease_id exists in DISEASES, return it
# If not, raise HTTPException with status 404

# TODO 2: POST /divide
# Accept query params: a (int), b (int)
# Return division result
# If b is 0, raise HTTPException with status 400

# TODO 3: GET /risky-operation
# Simulate an operation that might fail
# Use try-except to catch exceptions
# Return 500 status for unexpected errors

# TODO 4: POST /validate-confidence
# Accept JSON body: {"confidence": float}
# If confidence not in 0.0-1.0 range, raise 400 error
# Return success message if valid
```

### Implementation Examples

**404 Not Found:**
```python
@app.get("/item/{item_id}")
def get_item(item_id: int):
    items = {1: "Apple", 2: "Banana"}
    if item_id not in items:
        raise HTTPException(
            status_code=404,
            detail=f"Item {item_id} not found"
        )
    return {"item": items[item_id]}
```

**400 Bad Request:**
```python
@app.post("/register")
def register(username: str):
    if len(username) < 3:
        raise HTTPException(
            status_code=400,
            detail="Username must be at least 3 characters"
        )
    return {"username": username, "registered": True}
```

**500 Internal Server Error:**
```python
@app.get("/process")
def process():
    try:
        result = risky_function()
        return {"result": result}
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Processing failed: {str(e)}"
        )
```

### Test Cases

**Test 1: Disease Found (200)**
```
GET /disease/tomato-blight
Status: 200
Response: {"name": "Tomato Blight", "severity": "high"}
```

**Test 2: Disease Not Found (404)**
```
GET /disease/unknown-disease
Status: 404
Response: {"detail": "Disease unknown-disease not found"}
```

**Test 3: Division by Zero (400)**
```
POST /divide?a=10&b=0
Status: 400
Response: {"detail": "Cannot divide by zero"}
```

**Test 4: Invalid Confidence (400)**
```
POST /validate-confidence
Body: {"confidence": 1.5}
Status: 400
Response: {"detail": "Confidence must be between 0.0 and 1.0"}
```

### Validation Checklist
- [ ] 404 returned for non-existent resources
- [ ] 400 returned for invalid input (validation errors)
- [ ] 500 returned for unexpected server errors
- [ ] Error messages are clear and user-friendly
- [ ] Try-except blocks catch exceptions
- [ ] Swagger UI shows different status codes

---

## Exercise 6: Local Network Testing

### Goal
Configure FastAPI to accept connections from other devices on the same network.

### Learning Objectives
- Understand localhost vs local IP
- Run server with --host 0.0.0.0
- Find local IP address
- Test from phone browser
- Configure firewall if needed

### Instructions

**Step 1: Find Your Local IP**

**Windows:**
```cmd
ipconfig
```
Look for "IPv4 Address" under Wi-Fi adapter.

**Mac/Linux:**
```bash
ifconfig
# or
ip addr show
```
For the emulator, use `http://10.0.2.2:8000/`. For a physical phone, look for a laptop IP starting with 192.168.x.x or 10.0.x.x.

**Step 2: Run Server for Network Access**

```bash
# Wrong (only localhost):
uvicorn main:app --reload

# Correct (network access):
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

**Step 3: Test from Laptop**

Open browser: http://localhost:8000
Should work.

Also try from the emulator: http://10.0.2.2:8000
For a physical phone, also try: http://YOUR_IP:8000 (e.g., http://192.168.1.100:8000)

Expected result: the health JSON appears.

**Step 4: Test from Phone**

1. For emulator testing, open Chrome in the emulator
2. Navigate to: http://10.0.2.2:8000
3. For a physical phone, connect to the SAME Wi-Fi network as laptop and navigate to http://YOUR_IP:8000
4. Should see your API response

**Step 5: Troubleshooting**

If phone cannot connect:

1. **Verify same network:**
   - Check laptop Wi-Fi: "Home WiFi"
   - Check phone Wi-Fi: Must be "Home WiFi", NOT mobile data

2. **Check firewall:**
   - Windows: Allow Python through firewall
   - Mac: System Preferences → Security → Firewall → Allow Python

3. **Verify server is running:**
   - Terminal should show "Uvicorn running on http://0.0.0.0:8000"

4. **Try pinging:**
   - On phone, use app like "Network Analyzer"
   - Ping laptop IP address

### Validation Checklist
- [ ] Found local IP address (documented)
- [ ] Server runs with --host 0.0.0.0
- [ ] Laptop browser can access http://localhost:8000
- [ ] Laptop browser can access http://YOUR_IP:8000
- [ ] Phone and laptop on same Wi-Fi network
- [ ] Phone browser can access http://YOUR_IP:8000
- [ ] Firewall configured (if needed)
- [ ] Screenshot of phone accessing server saved

### Documentation Task

Create `NETWORK_CONFIG.md`:

```markdown
# Network Configuration

## Laptop
- OS: [Your OS]
- Wi-Fi Network: [Network Name]
- Local IP: [Your IP]
- Server Command: uvicorn main:app --host 0.0.0.0 --port 8000

## Phone
- Device: [Phone Model]
- OS: Android [Version]
- Wi-Fi Network: [Same Network]
- Test URL: http://[YOUR_IP]:8000

## Verification
- [x] Server accessible from laptop
- [x] Server accessible from phone
- [x] Swagger UI works on phone

## Issues Encountered
[Document any problems and how you solved them]
```

---

## Exercise 7: Complete LeafGuard /predict Endpoint

### Goal
Build the complete /predict endpoint for LeafGuard with dummy response.

### Learning Objectives
- Combine all previous concepts
- Handle multipart file upload
- Validate image files
- Return structured Pydantic response
- Handle errors properly
- Test with Postman

### Instructions

**Create: `leafguard_api.py`**

This is your complete Week 04 deliverable.

**Requirements:**

1. **Pydantic Models:**
   - PredictionResult (disease, confidence, symptoms, treatment, prevention)

2. **Validation Function:**
   - Check file content type (image/jpeg, image/png only)
   - Check file size (max 10 MB)
   - Return error message if invalid, None if valid

3. **POST /predict Endpoint:**
   - Accept file upload
   - Validate file
   - Generate dummy prediction (random disease from list)
   - Return PredictionResult

4. **GET / Health Check:**
   - Return status, message, version

5. **CORS Middleware:**
   - Allow all origins (for Android app)

6. **Error Handling:**
   - 400 for invalid files
   - 500 for unexpected errors

### Starter Template

```python
from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime
import random

app = FastAPI(
    title="LeafGuard AI Backend",
    description="Plant disease detection API",
    version="1.0.0"
)

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

# Models
class PredictionResult(BaseModel):
    # TODO: Define disease, confidence, symptoms, treatment, prevention
    pass

# Dummy disease data
DISEASES = [
    {
        "disease": "Tomato Early Blight",
        "symptoms": "Dark brown spots with concentric rings on lower leaves",
        "treatment": "Apply fungicide containing chlorothalonil",
        "prevention": "Rotate crops, avoid overhead watering"
    },
    # TODO: Add 2 more diseases
]

# Validation
def validate_image(file: UploadFile) -> Optional[str]:
    """Returns error message if invalid, None if valid"""
    # TODO: Implement validation
    pass

# Endpoints
@app.get("/")
def health_check():
    # TODO: Implement
    pass

@app.post("/predict", response_model=PredictionResult)
async def predict(image: UploadFile = File(...)):
    # TODO: Implement
    # 1. Validate file
    # 2. Read file contents
    # 3. Select random disease from DISEASES list
    # 4. Generate random confidence (0.75-0.98)
    # 5. Return PredictionResult with the five contract fields
    pass
```

### Testing Requirements

**Test with Postman:**

1. **Health Check:**
   - GET http://localhost:8000/
   - Status: 200
   - Response has status, message, version

2. **Valid Prediction:**
   - POST http://localhost:8000/predict
   - Body: form-data, file: [leaf image]
   - Status: 200
   - Response has disease, confidence, symptoms, treatment, and prevention

3. **Invalid File Type:**
   - POST http://localhost:8000/predict
   - Body: .txt file
   - Status: 400
   - Error message mentions invalid type

4. **Large File:**
   - POST with >10 MB image
   - Status: 400
   - Error message mentions size limit

5. **Network Test:**
   - Run server with --host 0.0.0.0
   - Test from phone browser
   - POST request from Postman on phone

### Validation Checklist

**Code Quality:**
- [ ] All models defined with proper types
- [ ] Validation function checks type and size
- [ ] Error handling with try-except
- [ ] CORS middleware configured
- [ ] Code has docstrings
- [ ] No hardcoded values (use constants)

**Functionality:**
- [ ] Health check endpoint works
- [ ] /predict accepts image uploads
- [ ] Random disease returned from list
- [ ] Confidence is between 0.75-0.98
- [ ] Timestamp in ISO format
- [ ] File size included in response
- [ ] Invalid files rejected with 400
- [ ] Unexpected errors return 500

**Testing:**
- [ ] Postman collection created
- [ ] All 5 test cases pass
- [ ] Screenshots saved
- [ ] Works from phone on same network

**Documentation:**
- [ ] README.md with installation steps
- [ ] API documentation (endpoints, examples)
- [ ] NETWORK_CONFIG.md with IP setup
- [ ] requirements.txt generated

### Submission

Create evidence folder:
```
evidence/week-04/
├── screenshots/
│   ├── postman-health-check.png
│   ├── postman-predict-success.png
│   ├── postman-predict-error.png
│   ├── phone-browser-test.png
│   └── swagger-ui.png
├── leafguard_api.py
├── requirements.txt
├── README.md
├── NETWORK_CONFIG.md
└── postman-collection.json
```

---

## Bonus Exercise: Deploy to Local Network Server

### Goal
Run FastAPI backend as a background service accessible anytime on local network.

### Instructions

**Option 1: Run as Service (Linux)**

Create systemd service file: `/etc/systemd/system/leafguard-api.service`

```ini
[Unit]
Description=LeafGuard API
After=network.target

[Service]
User=your-username
WorkingDirectory=/path/to/backend-api
ExecStart=/path/to/venv/bin/uvicorn main:app --host 0.0.0.0 --port 8000
Restart=always

[Install]
WantedBy=multi-user.target
```

Enable and start:
```bash
sudo systemctl enable leafguard-api
sudo systemctl start leafguard-api
sudo systemctl status leafguard-api
```

**Option 2: Run with Screen (Linux/Mac)**

```bash
# Install screen
sudo apt install screen  # Ubuntu
brew install screen      # Mac

# Start screen session
screen -S leafguard-api

# Run server
uvicorn main:app --host 0.0.0.0 --port 8000

# Detach: Press Ctrl+A, then D
# Reattach: screen -r leafguard-api
```

**Option 3: Task Scheduler (Windows)**

1. Create batch file `run_api.bat`:
```batch
cd C:\path\to\backend-api
call venv\Scripts\activate
uvicorn main:app --host 0.0.0.0 --port 8000
```

2. Open Task Scheduler
3. Create Basic Task
4. Trigger: At startup
5. Action: Start a program → Select run_api.bat

---

**Congratulations! You have completed all Week 04 exercises. You now have a solid understanding of FastAPI backend development. Proceed to build-task.md to create the production-ready LeafGuard backend.**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 04 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| **3** | **exercises.md** ← *You are here* | **Practice Exercises** |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Theory & Learning Notes](learning-notes.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Practice Exercises** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Build Implementation Guide →](build-task.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
