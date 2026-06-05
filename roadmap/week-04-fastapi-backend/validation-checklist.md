# Week 04 Validation Checklist

## Purpose

Use this checklist to verify your Week 04 FastAPI backend is complete and functional before proceeding to Week 05 Android networking. Every item must be checked for successful completion.

---

## Project Setup & Structure

### Virtual Environment
- [ ] Virtual environment created (`venv/` folder exists)
- [ ] Virtual environment can be activated
- [ ] `(venv)` prefix appears in terminal when activated
- [ ] FastAPI installed in venv (`pip show fastapi` works)
- [ ] Uvicorn installed in venv (`pip show uvicorn` works)
- [ ] python-multipart installed (`pip show python-multipart` works)

### Project Structure
- [ ] Root folder: `leafguard-backend/` exists
- [ ] `main.py` exists in root
- [ ] `models/` folder exists with `__init__.py` and `prediction.py`
- [ ] `routers/` folder exists with `__init__.py` and `predict.py`
- [ ] `utils/` folder exists with `__init__.py` and `validation.py`
- [ ] `uploads/` folder exists for temporary file storage
- [ ] `requirements.txt` exists and contains all dependencies
- [ ] `.gitignore` exists and excludes venv, uploads, __pycache__

### Dependencies
- [ ] `requirements.txt` contains fastapi
- [ ] `requirements.txt` contains uvicorn
- [ ] `requirements.txt` contains python-multipart
- [ ] `requirements.txt` contains pydantic
- [ ] Can install from requirements.txt: `pip install -r requirements.txt` works
- [ ] `pip freeze` output matches requirements.txt

---

## Code Implementation

### Response Models (models/prediction.py)
- [ ] PredictionResponse class defined
- [ ] PredictionResponse extends BaseModel
- [ ] Field: success (bool, default True)
- [ ] Field: disease (str, required, min_length=1, max_length=100)
- [ ] Field: confidence (float, required, ge=0.0, le=1.0)
- [ ] Field: symptoms (str, required)
- [ ] Field: treatment (str, required)
- [ ] Field: prevention (str, required)
- [ ] Field: timestamp (str, required)
- [ ] Field: file_size (Optional[int])
- [ ] ErrorResponse class defined
- [ ] ErrorResponse has success (bool, default False)
- [ ] ErrorResponse has error (str, required)
- [ ] ErrorResponse has detail (Optional[str])

### Validation Utils (utils/validation.py)
- [ ] validate_image() function defined
- [ ] Checks content type (image/jpeg, image/png)
- [ ] Checks file extension (.jpg, .jpeg, .png)
- [ ] Returns None if valid, error message if invalid
- [ ] Constants defined: ALLOWED_CONTENT_TYPES, ALLOWED_EXTENSIONS, MAX_FILE_SIZE
- [ ] validate_file_size() function defined (optional but recommended)
- [ ] File size limited to 10 MB or less

### Predict Endpoint (routers/predict.py)
- [ ] APIRouter created with prefix="/api"
- [ ] DISEASE_DATABASE list defined with at least 3 diseases
- [ ] Each disease has: disease, symptoms, treatment, prevention
- [ ] POST /predict endpoint defined
- [ ] Endpoint decorated with @router.post("/predict", response_model=PredictionResponse)
- [ ] Accepts file: UploadFile = File(...)
- [ ] Calls validate_image() for type validation
- [ ] Raises HTTPException(400) if validation fails
- [ ] Reads file contents with await file.read()
- [ ] Checks file size
- [ ] Selects random disease from database
- [ ] Generates random confidence between 0.75 and 0.98
- [ ] Returns PredictionResponse with all fields
- [ ] Timestamp formatted as ISO 8601
- [ ] File size included in response

### Main Application (main.py)
- [ ] FastAPI app created with title, description, version
- [ ] CORS middleware added
- [ ] CORS allows all origins (allow_origins=["*"])
- [ ] CORS allows all methods and headers
- [ ] Predict router included with app.include_router()
- [ ] GET / endpoint exists (health check)
- [ ] Health check returns status, message, version
- [ ] Optional: GET /health endpoint with detailed info
- [ ] No syntax errors (code runs without crashes)

---

## Functionality Testing

### Server Startup
- [ ] Server starts with: `uvicorn main:app --reload`
- [ ] No import errors in terminal
- [ ] No ModuleNotFoundError
- [ ] Terminal shows "Uvicorn running on http://127.0.0.1:8000"
- [ ] Terminal shows "Application startup complete"
- [ ] Can stop server with Ctrl+C

### Health Check Endpoint
- [ ] Browser: http://localhost:8000 returns JSON
- [ ] Response has "status": "online"
- [ ] Response has "message" field
- [ ] Response has "version" field
- [ ] Status code is 200 OK

### Swagger UI Documentation
- [ ] http://localhost:8000/docs loads
- [ ] Shows "LeafGuard AI Backend" title
- [ ] Lists GET / endpoint
- [ ] Lists POST /api/predict endpoint
- [ ] Can expand /api/predict to see details
- [ ] Shows request body (file upload)
- [ ] Shows response schema (PredictionResponse)

### File Upload - Valid Image
- [ ] Can upload .jpg file via Swagger UI
- [ ] Can upload .png file via Swagger UI
- [ ] Response status is 200 OK
- [ ] Response has success: true
- [ ] Response has disease name (string)
- [ ] Response has confidence (number between 0.75-0.98)
- [ ] Response has symptoms (string, not empty)
- [ ] Response has treatment (string, not empty)
- [ ] Response has prevention (string, not empty)
- [ ] Response has timestamp (ISO format with Z)
- [ ] Response has file_size (number > 0)
- [ ] Disease varies with multiple uploads (random selection)

### File Upload - Invalid File Type
- [ ] Uploading .txt file returns 400 error
- [ ] Error message mentions invalid content type
- [ ] Uploading .pdf file returns 400 error
- [ ] Uploading .docx file returns 400 error
- [ ] Error detail is user-friendly

### File Upload - Large File (Optional)
- [ ] Files > 10 MB rejected with 400 error
- [ ] Error message mentions file size limit
- [ ] Files <= 10 MB accepted

---

## Network Configuration

### Local IP Address
- [ ] Local IP address identified (192.168.x.x or 10.0.x.x format)
- [ ] IP address documented in NETWORK_SETUP.md or notes
- [ ] IP address is not 127.0.0.1 (that's localhost, not network IP)

### Server Network Access
- [ ] Server starts with: `uvicorn main:app --host 0.0.0.0 --port 8000 --reload`
- [ ] Terminal shows "Uvicorn running on http://0.0.0.0:8000"
- [ ] --host is 0.0.0.0 (not 127.0.0.1)
- [ ] Port is 8000

### Laptop Access
- [ ] Can access http://localhost:8000 from laptop browser
- [ ] Can access http://YOUR_IP:8000 from laptop browser
- [ ] Both show same health check response

### Firewall Configuration
- [ ] Firewall checked (Windows Firewall, Mac firewall, ufw on Linux)
- [ ] Python allowed through firewall
- [ ] Port 8000 not blocked
- [ ] No antivirus blocking connections

### Phone Access
- [ ] Phone connected to SAME Wi-Fi network as laptop
- [ ] Phone NOT using mobile data
- [ ] Can access http://YOUR_IP:8000 from phone browser
- [ ] Health check JSON visible on phone
- [ ] No "connection refused" error
- [ ] No timeout error

---

## Postman Testing

### Postman Setup
- [ ] Postman desktop app installed
- [ ] Collection created: "LeafGuard API"
- [ ] Collection has description

### Postman Requests
- [ ] Request 1: "Health Check" (GET /)
- [ ] Request 2: "Predict Disease - Success" (POST /api/predict with image)
- [ ] Request 3: "Predict Disease - Error" (POST /api/predict with .txt file)
- [ ] Optional: Request 4: "Network Test" (POST with IP variable)

### Test Results
- [ ] Health Check request returns 200 OK
- [ ] Predict Success request returns 200 OK with full response
- [ ] Predict Error request returns 400 Bad Request
- [ ] Error response has clear message
- [ ] All tests repeatable (run multiple times successfully)

### Postman Evidence
- [ ] Collection exported as JSON
- [ ] JSON file saved in evidence folder
- [ ] Screenshots of successful requests saved
- [ ] Screenshot of error handling saved

---

## Documentation

### README.md
- [ ] README.md exists in project root
- [ ] Has project title and description
- [ ] Lists features
- [ ] Lists tech stack
- [ ] Shows project structure (folder tree)
- [ ] Has installation instructions
- [ ] Installation steps are clear (can follow step-by-step)
- [ ] Has "Running the Server" section
- [ ] Documents all API endpoints
- [ ] Shows request/response examples for each endpoint
- [ ] Has testing instructions (Swagger UI, Postman, curl)
- [ ] Has troubleshooting section
- [ ] Has author name and course info

### NETWORK_SETUP.md
- [ ] NETWORK_SETUP.md exists
- [ ] Documents laptop OS and Wi-Fi network
- [ ] Documents local IP address
- [ ] Documents server command (with --host 0.0.0.0)
- [ ] Documents phone device and network
- [ ] Lists verification steps
- [ ] Has troubleshooting section
- [ ] Explains common network issues

### Code Comments
- [ ] main.py has docstrings for functions
- [ ] routers/predict.py has docstring for /predict endpoint
- [ ] models/prediction.py has class docstrings
- [ ] utils/validation.py has function docstrings
- [ ] Complex logic has inline comments

---

## Git & Version Control

### Git Initialization
- [ ] Git repository initialized (`git init` run)
- [ ] .git folder exists
- [ ] .gitignore file exists
- [ ] .gitignore excludes venv/
- [ ] .gitignore excludes __pycache__/
- [ ] .gitignore excludes uploads/*.jpg, *.png
- [ ] .gitignore KEEPS uploads/.gitkeep

### Git Commits
- [ ] At least 3 commits made
- [ ] Commit 1: Initial project structure
- [ ] Commit 2: Add models and validation
- [ ] Commit 3: Complete endpoint and documentation
- [ ] Commit messages follow format: "Week 04: [Category] - Description"
- [ ] Commit messages are meaningful (not "update" or "changes")
- [ ] Can view history with `git log`

### Git Status
- [ ] `git status` shows clean working tree OR only uncommitted evidence files
- [ ] No large files in git (venv, uploads are ignored)
- [ ] All code files are tracked

---

## Evidence Collection

### Evidence Folder Structure
- [ ] evidence/week-04/ folder exists
- [ ] evidence/week-04/screenshots/ folder exists
- [ ] evidence/week-04/code/ folder exists (optional)

### Screenshots Saved
- [ ] postman-health-check.png (200 OK response)
- [ ] postman-predict-success.png (Prediction response with all fields)
- [ ] postman-predict-error.png (400 error with invalid file)
- [ ] swagger-ui.png (API documentation page)
- [ ] phone-browser-test.png (Phone accessing server)
- [ ] terminal-server-running.png (Uvicorn running message)
- [ ] All screenshots are clear and readable
- [ ] All screenshots have timestamp or date visible

### Exported Files
- [ ] postman-collection.json exported and saved
- [ ] requirements.txt in evidence folder
- [ ] README.md in evidence folder
- [ ] Code snapshots saved (optional: main.py, predict.py, prediction.py)

---

## Code Quality

### Python Style
- [ ] Code follows PEP 8 guidelines (proper indentation)
- [ ] Function names are lowercase_with_underscores
- [ ] Class names are CamelCase
- [ ] Constants are UPPERCASE_WITH_UNDERSCORES
- [ ] Proper spacing around operators and after commas
- [ ] Lines not excessively long (< 100 characters preferred)

### Type Hints
- [ ] Function parameters have type hints (def func(name: str))
- [ ] Function return types specified (-> dict, -> PredictionResponse)
- [ ] Pydantic models use proper types (str, int, float, bool)

### Error Handling
- [ ] HTTPException used for validation errors
- [ ] Status codes are appropriate (400 for client errors, 500 for server errors)
- [ ] Error messages are user-friendly
- [ ] Try-except blocks used for file operations
- [ ] No bare except: clauses (specific exceptions caught)

### Constants
- [ ] No magic numbers in code
- [ ] File size limit defined as constant (MAX_FILE_SIZE)
- [ ] Allowed types defined as constant (ALLOWED_CONTENT_TYPES)
- [ ] Constants at top of file or in config section

---

## Understanding & Comprehension

### Conceptual Understanding
- [ ] I can explain what REST API is
- [ ] I can explain the difference between GET and POST
- [ ] I can explain why multipart/form-data is used for file uploads
- [ ] I can explain what JSON is and why it's used
- [ ] I can explain what Pydantic does
- [ ] I can explain what CORS is and why it's needed
- [ ] I can explain the difference between 127.0.0.1 and local IP
- [ ] I can explain why --host 0.0.0.0 is needed

### Practical Knowledge
- [ ] I can create a new FastAPI endpoint from scratch
- [ ] I can define a Pydantic model with validation
- [ ] I can handle file uploads in FastAPI
- [ ] I can raise appropriate HTTP exceptions
- [ ] I can find my laptop's local IP address
- [ ] I can run server accessible from network
- [ ] I can test API with Postman
- [ ] I can read FastAPI auto-generated documentation

### Problem Solving
- [ ] If server crashes, I know how to read error messages
- [ ] If phone can't connect, I know troubleshooting steps
- [ ] If validation fails, I know how to fix it
- [ ] If import error occurs, I know to check virtual environment
- [ ] I can debug using print statements or logging

---

## Performance & Best Practices

### Performance
- [ ] Server responds to requests in < 1 second (dummy data is fast)
- [ ] No memory leaks (server doesn't crash after multiple requests)
- [ ] Large files handled gracefully (rejected before loading into memory)
- [ ] File upload works with 1 MB images
- [ ] File upload works with 5 MB images

### Best Practices
- [ ] Secrets not hardcoded (no API keys in code)
- [ ] No print() statements for production logging (use logging module)
- [ ] Pydantic validation preferred over manual checks
- [ ] Async functions used for file operations (async def, await)
- [ ] CORS configured properly (not just allow_origins=["*"] in production)

---

## Week 05 Readiness

### Prerequisites for Next Week
- [ ] Backend server working and tested
- [ ] Local IP address documented
- [ ] Postman collection works
- [ ] Phone can access server on network
- [ ] Base URL is clear (http://YOUR_IP:8000)
- [ ] /api/predict endpoint tested with images
- [ ] Response format understood (JSON with disease, confidence, etc.)
- [ ] Confident in running server for Android testing

### Handoff to Week 05
- [ ] I know I will use Retrofit in Android to call this API
- [ ] I know Retrofit will send image as multipart/form-data
- [ ] I know Android will parse JSON response using GSON
- [ ] I know I need to keep backend running while testing Android app
- [ ] I saved my local IP for use in Android BaseUrl configuration

---

## Final Checklist

### Before Moving to Week 05
- [ ] All sections above are checked
- [ ] Server runs without errors
- [ ] Phone can access API
- [ ] Postman tests all pass
- [ ] Documentation complete
- [ ] Git commits made
- [ ] Evidence collected and organized
- [ ] I feel confident about FastAPI basics
- [ ] I am ready to integrate with Android

### Self-Assessment (Rate 1-5, 5 being highest)
- **FastAPI understanding:** ___ / 5
- **REST API concepts:** ___ / 5
- **File upload handling:** ___ / 5
- **Network configuration:** ___ / 5
- **Testing proficiency:** ___ / 5
- **Overall confidence:** ___ / 5

**If any score is below 3, review learning-notes.md and repeat exercises before proceeding.**

---

## Verification Signature

**Student Name:** ___________________________

**Completion Date:** ___________________________

**Instructor/Peer Review (Optional):** ___________________________

**Ready for Week 05:** ☐ YES ☐ NO (if NO, list items to complete):

_______________________________________________

_______________________________________________

_______________________________________________

---

**Congratulations! If all items are checked, you have successfully completed Week 04 and are ready for Week 05 Android Networking.**


<!-- NAV_FOOTER_START -->

---

## 📚 Week 04 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| **5** | **validation-checklist.md** ← *You are here* | **Validation & Verification** |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Build Implementation Guide](build-task.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Validation & Verification** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Knowledge Assessment Quiz →](quiz.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 03: Camera & Gallery](../week-03-camera-gallery/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 05: Android Networking ➡](../week-05-android-networking/README.md) |

---
