# Week 04 Validation Checklist

## Purpose

Use this checklist to verify your Week 04 FastAPI backend is complete and functional before proceeding to Week 05 Android networking. Every item must be checked for successful completion.

---

## Project Setup & Structure

### Virtual Environment
- [ ] Can I confirm that Virtual environment created (`venv/` folder exists)? — yes/no
- [ ] Can I confirm that Virtual environment can be activated? — yes/no
- [ ] Can I confirm that `(venv)` prefix appears in terminal when activated? — yes/no
- [ ] Can I confirm that FastAPI installed in venv (`pip show fastapi` works)? — yes/no
- [ ] Can I confirm that Uvicorn installed in venv (`pip show uvicorn` works)? — yes/no
- [ ] Can I confirm that python-multipart installed (`pip show python-multipart` works)? — yes/no

### Project Structure
- [ ] Can I confirm that Root folder: `backend-api/` exists? — yes/no
- [ ] Can I confirm that `main.py` exists in root? — yes/no
- [ ] Can I confirm that `config.py` exists for settings and environment variables? — yes/no
- [ ] Can I confirm that `main.py` defines the `/predict` endpoint (no separate route module folder needed)? — yes/no
- [ ] Can I confirm that `model_loader.py` exists for the real model or mock predictor fallback? — yes/no
- [ ] Can I confirm that No upload folder is required unless I intentionally save temporary images? — yes/no
- [ ] Can I confirm that `requirements.txt` exists and contains all dependencies? — yes/no
- [ ] Can I confirm that `.gitignore` exists and excludes venv, uploads, __pycache__? — yes/no

### Dependencies
- [ ] Can I confirm that `requirements.txt` contains fastapi? — yes/no
- [ ] Can I confirm that `requirements.txt` contains uvicorn? — yes/no
- [ ] Can I confirm that `requirements.txt` contains python-multipart? — yes/no
- [ ] Can I confirm that `requirements.txt` contains pydantic? — yes/no
- [ ] Can I confirm that Can install from requirements.txt: `pip install -r requirements.txt` works? — yes/no
- [ ] Can I confirm that `pip freeze` output matches requirements.txt? — yes/no

---

## Code Implementation

### Response Model (in main.py)
- [ ] Can I confirm that PredictionResult class defined? — yes/no
- [ ] Can I confirm that PredictionResult extends BaseModel? — yes/no
- [ ] Can I confirm that Response intentionally does not use a `success` field; it returns `disease`, `confidence`, `symptoms`, `treatment`, and `prevention`? — yes/no
- [ ] Can I confirm that Field: disease (str, required, min_length=1, max_length=100)? — yes/no
- [ ] Can I confirm that Field: confidence (float, required, ge=0.0, le=1.0)? — yes/no
- [ ] Can I confirm that Field: symptoms (str, required)? — yes/no
- [ ] Can I confirm that Field: treatment (str, required)? — yes/no
- [ ] Can I confirm that Field: prevention (str, required)? — yes/no
- [ ] Can I confirm that Response contract does not require a timestamp field? — yes/no
- [ ] Can I confirm that Response contract does not require a file_size field? — yes/no
- [ ] Can I confirm that HTTPException error responses are defined for invalid uploads? — yes/no
- [ ] Can I confirm that Error responses do not need a `success` field? — yes/no
- [ ] Can I confirm that Error responses include a clear error/detail message? — yes/no
- [ ] Can I confirm that Error responses can include optional detail text? — yes/no

### Validation helper (in main.py)
- [ ] Can I confirm that validate_image() function defined? — yes/no
- [ ] Can I confirm that Checks content type (image/jpeg, image/png)? — yes/no
- [ ] Can I confirm that Checks file extension (.jpg, .jpeg, .png)? — yes/no
- [ ] Can I confirm that Returns None if valid, error message if invalid? — yes/no
- [ ] Can I confirm that Constants defined: ALLOWED_CONTENT_TYPES, ALLOWED_EXTENSIONS, MAX_FILE_SIZE? — yes/no
- [ ] Can I confirm that validate_file_size() function defined (optional but recommended)? — yes/no
- [ ] Can I confirm that File size limited to 10 MB or less? — yes/no

### Predict Endpoint (in main.py)
- [ ] Can I confirm that `/predict` route is decorated directly with `@app.post("/predict", response_model=PredictionResult)`? — yes/no
- [ ] Can I confirm that DISEASE_DATABASE list defined with at least 3 diseases? — yes/no
- [ ] Can I confirm that Each disease has: disease, symptoms, treatment, prevention? — yes/no
- [ ] Can I confirm that POST /predict endpoint defined? — yes/no
- [ ] Can I confirm that Endpoint decorated with `@app.post("/predict", response_model=PredictionResult)`? — yes/no
- [ ] Can I confirm that Accepts `image: UploadFile = File(...)` so the multipart part is named `image`? — yes/no
- [ ] Can I confirm that Calls validate_image() for type validation? — yes/no
- [ ] Can I confirm that Raises HTTPException(400) if validation fails? — yes/no
- [ ] Can I confirm that Reads image contents with `await image.read()`? — yes/no
- [ ] Can I confirm that Checks file size? — yes/no
- [ ] Can I confirm that Selects random disease from database? — yes/no
- [ ] Can I confirm that Generates random confidence between 0.75 and 0.98? — yes/no
- [ ] Can I confirm that Returns PredictionResult with all response fields? — yes/no
- [ ] Can I confirm that Timestamp formatted as ISO 8601? — yes/no
- [ ] Can I confirm that File size included in response? — yes/no

### Main Application (main.py)
- [ ] Can I confirm that FastAPI app created with title, description, version? — yes/no
- [ ] Can I confirm that CORS middleware added? — yes/no
- [ ] Can I confirm that CORS allows all origins (allow_origins=["*"])? — yes/no
- [ ] Can I confirm that CORS allows all methods and headers? — yes/no
- [ ] Can I confirm that No route module import is needed because `/predict` is defined in `main.py`? — yes/no
- [ ] Can I confirm that GET / endpoint exists (health check)? — yes/no
- [ ] Can I confirm that Health check returns status, message, version? — yes/no
- [ ] Can I confirm that Optional: GET /health endpoint with detailed info? — yes/no
- [ ] Can I confirm that No syntax errors (code runs without crashes)? — yes/no

---

## Functionality Testing

### Server Startup
- [ ] Can I confirm that Server starts with: `uvicorn main:app --reload`? — yes/no
- [ ] Can I confirm that No import errors in terminal? — yes/no
- [ ] Can I confirm that No ModuleNotFoundError? — yes/no
- [ ] Can I confirm that Terminal shows "Uvicorn running on http://127.0.0.1:8000"? — yes/no
- [ ] Can I confirm that Terminal shows "Application startup complete"? — yes/no
- [ ] Can I confirm that Can stop server with Ctrl+C? — yes/no

### Health Check Endpoint
- [ ] Can I confirm that Browser: http://localhost:8000 returns JSON? — yes/no
- [ ] Can I confirm that Response has "status": "online"? — yes/no
- [ ] Can I confirm that Response has "message" field? — yes/no
- [ ] Can I confirm that Response has "version" field? — yes/no
- [ ] Can I confirm that Status code is 200 OK? — yes/no

### Swagger UI Documentation
- [ ] Can I confirm that http://localhost:8000/docs loads? — yes/no
- [ ] Can I confirm that Shows "LeafGuard AI Backend" title? — yes/no
- [ ] Can I confirm that Lists GET / endpoint? — yes/no
- [ ] Can I confirm that Lists POST /predict endpoint? — yes/no
- [ ] Can I confirm that Can expand /predict to see details? — yes/no
- [ ] Can I confirm that Shows request body (file upload)? — yes/no
- [ ] Can I confirm that Shows response schema (PredictionResult)? — yes/no

### File Upload - Valid Image
- [ ] Can I confirm that Can upload .jpg file via Swagger UI? — yes/no
- [ ] Can I confirm that Can upload .png file via Swagger UI? — yes/no
- [ ] Can I confirm that Response status is 200 OK? — yes/no
- [ ] Can I confirm that the response does not use `success` and instead returns the five contract fields? — yes/no
- [ ] Can I confirm that Response has disease name (string)? — yes/no
- [ ] Can I confirm that Response has confidence (number between 0.75-0.98)? — yes/no
- [ ] Can I confirm that Response has symptoms (string, not empty)? — yes/no
- [ ] Can I confirm that Response has treatment (string, not empty)? — yes/no
- [ ] Can I confirm that Response has prevention (string, not empty)? — yes/no
- [ ] Can I confirm that timestamp is not part of the Week 04 `/predict` response contract? — yes/no
- [ ] Can I confirm that file_size is not part of the Week 04 `/predict` response contract? — yes/no
- [ ] Can I confirm that Disease varies with multiple uploads (random selection)? — yes/no

### File Upload - Invalid File Type
- [ ] Can I confirm that Uploading .txt file returns 400 error? — yes/no
- [ ] Can I confirm that Error message mentions invalid content type? — yes/no
- [ ] Can I confirm that Uploading .pdf file returns 400 error? — yes/no
- [ ] Can I confirm that Uploading .docx file returns 400 error? — yes/no
- [ ] Can I confirm that Error detail is user-friendly? — yes/no

### File Upload - Large File (Optional)
- [ ] Can I confirm that Files > 10 MB rejected with 400 error? — yes/no
- [ ] Can I confirm that Error message mentions file size limit? — yes/no
- [ ] Can I confirm that Files <= 10 MB accepted? — yes/no

---

## Network Configuration

### Local IP Address
- [ ] Can I confirm that Emulator base URL identified as `http://10.0.2.2:8000/`; physical-phone LAN IP identified only if needed? — yes/no
- [ ] Can I confirm that Emulator base URL and any physical-phone LAN IP are documented in NETWORK_SETUP.md or notes? — yes/no
- [ ] Can I confirm that Android URL is not `127.0.0.1`; emulator uses `10.0.2.2` and physical phone uses the laptop LAN IP? — yes/no

### Server Network Access
- [ ] Can I confirm that Server starts with: `uvicorn main:app --host 0.0.0.0 --port 8000 --reload`? — yes/no
- [ ] Can I confirm that Terminal shows "Uvicorn running on http://0.0.0.0:8000"? — yes/no
- [ ] Can I confirm that --host is 0.0.0.0 (not 127.0.0.1)? — yes/no
- [ ] Can I confirm that Port is 8000? — yes/no

### Laptop Access
- [ ] Can I confirm that Can access http://localhost:8000 from laptop browser? — yes/no
- [ ] Can I confirm that Can access http://YOUR_IP:8000 from laptop browser? — yes/no
- [ ] Can I confirm that Both show same health check response? — yes/no

### Firewall Configuration
- [ ] Can I confirm that Firewall checked (Windows Firewall, Mac firewall, ufw on Linux)? — yes/no
- [ ] Can I confirm that Python allowed through firewall? — yes/no
- [ ] Can I confirm that Port 8000 not blocked? — yes/no
- [ ] Can I confirm that No antivirus blocking connections? — yes/no

### Phone Access
- [ ] Can I confirm that Phone connected to SAME Wi-Fi network as laptop? — yes/no
- [ ] Can I confirm that Phone NOT using mobile data? — yes/no
- [ ] Can I confirm that Can access http://YOUR_IP:8000 from phone browser? — yes/no
- [ ] Can I confirm that Health check JSON visible on phone? — yes/no
- [ ] Can I confirm that No "connection refused" error? — yes/no
- [ ] Can I confirm that No timeout error? — yes/no

---

## Postman Testing

### Postman Setup
- [ ] Can I confirm that Postman desktop app installed? — yes/no
- [ ] Can I confirm that Collection created: "LeafGuard API"? — yes/no
- [ ] Can I confirm that Collection has description? — yes/no

### Postman Requests
- [ ] Can I confirm that Request 1: "Health Check" (GET /)? — yes/no
- [ ] Can I confirm that Request 2: "Predict Disease - Success" (POST /predict with image)? — yes/no
- [ ] Can I confirm that Request 3: "Predict Disease - Error" (POST /predict with .txt file)? — yes/no
- [ ] Can I confirm that Optional: Request 4: "Network Test" (POST with IP variable)? — yes/no

### Test Results
- [ ] Can I confirm that Health Check request returns 200 OK? — yes/no
- [ ] Can I confirm that Predict Success request returns 200 OK with full response? — yes/no
- [ ] Can I confirm that Predict Error request returns 400 Bad Request? — yes/no
- [ ] Can I confirm that Error response has clear message? — yes/no
- [ ] Can I confirm that All tests repeatable (run multiple times successfully)? — yes/no

### Postman Evidence
- [ ] Can I confirm that Collection exported as JSON? — yes/no
- [ ] Can I confirm that JSON file saved in evidence folder? — yes/no
- [ ] Can I confirm that Screenshots of successful requests saved? — yes/no
- [ ] Can I confirm that Screenshot of error handling saved? — yes/no

---

## Documentation

### README.md
- [ ] Can I confirm that README.md exists in project root? — yes/no
- [ ] Can I confirm that Has project title and description? — yes/no
- [ ] Can I confirm that Lists features? — yes/no
- [ ] Can I confirm that Lists tech stack? — yes/no
- [ ] Can I confirm that Shows project structure (folder tree)? — yes/no
- [ ] Can I confirm that Has installation instructions? — yes/no
- [ ] Can I confirm that Installation steps are clear (can follow step-by-step)? — yes/no
- [ ] Can I confirm that Has "Running the Server" section? — yes/no
- [ ] Can I confirm that Documents all API endpoints? — yes/no
- [ ] Can I confirm that Shows request/response examples for each endpoint? — yes/no
- [ ] Can I confirm that Has testing instructions (Swagger UI, Postman, curl)? — yes/no
- [ ] Can I confirm that Has troubleshooting section? — yes/no
- [ ] Can I confirm that Has author name and course info? — yes/no

### NETWORK_SETUP.md
- [ ] Can I confirm that NETWORK_SETUP.md exists? — yes/no
- [ ] Can I confirm that Documents laptop OS and Wi-Fi network? — yes/no
- [ ] Can I confirm that Documents local IP address? — yes/no
- [ ] Can I confirm that Documents server command (with --host 0.0.0.0)? — yes/no
- [ ] Can I confirm that Documents phone device and network? — yes/no
- [ ] Can I confirm that Lists verification steps? — yes/no
- [ ] Can I confirm that Has troubleshooting section? — yes/no
- [ ] Can I confirm that Explains common network issues? — yes/no

### Code Comments
- [ ] Can I confirm that main.py has docstrings for functions? — yes/no
- [ ] Can I confirm that main.py `/predict` endpoint has a docstring? — yes/no
- [ ] Can I confirm that PredictionResult in main.py has clear field names or comments? — yes/no
- [ ] Can I confirm that config.py and model_loader.py have helpful docstrings or clear comments? — yes/no
- [ ] Can I confirm that Complex logic has inline comments? — yes/no

---

## Git & Version Control

### Git Initialization
- [ ] Can I confirm that Git repository initialized (`git init` run)? — yes/no
- [ ] Can I confirm that .git folder exists? — yes/no
- [ ] Can I confirm that .gitignore file exists? — yes/no
- [ ] Can I confirm that .gitignore excludes venv/? — yes/no
- [ ] Can I confirm that .gitignore excludes __pycache__/? — yes/no
- [ ] Can I confirm that .gitignore excludes uploads/*.jpg, *.png? — yes/no
- [ ] Can I confirm that .gitignore KEEPS uploads/.gitkeep? — yes/no

### Git Commits
- [ ] Can I confirm that At least 3 commits made? — yes/no
- [ ] Can I confirm that Commit 1: Initial project structure? — yes/no
- [ ] Can I confirm that Commit 2: Add models and validation? — yes/no
- [ ] Can I confirm that Commit 3: Complete endpoint and documentation? — yes/no
- [ ] Can I confirm that Commit messages follow format: "Week 04: [Category] - Description"? — yes/no
- [ ] Can I confirm that Commit messages are meaningful (not "update" or "changes")? — yes/no
- [ ] Can I confirm that Can view history with `git log`? — yes/no

### Git Status
- [ ] Can I confirm that `git status` shows clean working tree OR only uncommitted evidence files? — yes/no
- [ ] Can I confirm that No large files in git (venv, uploads are ignored)? — yes/no
- [ ] Can I confirm that All code files are tracked? — yes/no

---

## Evidence Collection

### Evidence Folder Structure
- [ ] Can I confirm that evidence/week-04/ folder exists? — yes/no
- [ ] Can I confirm that evidence/week-04/screenshots/ folder exists? — yes/no
- [ ] Can I confirm that evidence/week-04/code/ folder exists (optional)? — yes/no

### Screenshots Saved
- [ ] Can I confirm that postman-health-check.png (200 OK response)? — yes/no
- [ ] Can I confirm that postman-predict-success.png (Prediction response with all fields)? — yes/no
- [ ] Can I confirm that postman-predict-error.png (400 error with invalid file)? — yes/no
- [ ] Can I confirm that swagger-ui.png (API documentation page)? — yes/no
- [ ] Can I confirm that phone-browser-test.png (Phone accessing server)? — yes/no
- [ ] Can I confirm that terminal-server-running.png (Uvicorn running message)? — yes/no
- [ ] Can I confirm that All screenshots are clear and readable? — yes/no
- [ ] Can I confirm that All screenshots have timestamp or date visible? — yes/no

### Exported Files
- [ ] Can I confirm that postman-collection.json exported and saved? — yes/no
- [ ] Can I confirm that requirements.txt in evidence folder? — yes/no
- [ ] Can I confirm that README.md in evidence folder? — yes/no
- [ ] Can I confirm that Code snapshots saved (optional: main.py, config.py, model_loader.py)? — yes/no

---

## Code Quality

### Python Style
- [ ] Can I confirm that Code follows PEP 8 guidelines (proper indentation)? — yes/no
- [ ] Can I confirm that Function names are lowercase_with_underscores? — yes/no
- [ ] Can I confirm that Class names are CamelCase? — yes/no
- [ ] Can I confirm that Constants are UPPERCASE_WITH_UNDERSCORES? — yes/no
- [ ] Can I confirm that Proper spacing around operators and after commas? — yes/no
- [ ] Can I confirm that Lines not excessively long (< 100 characters preferred)? — yes/no

### Type Hints
- [ ] Can I confirm that Function parameters have type hints (def func(name: str))? — yes/no
- [ ] Can I confirm that Function return types specified where helpful (for example, `-> dict` or `PredictionResult`)? — yes/no
- [ ] Can I confirm that Pydantic models use proper types (str, int, float, bool)? — yes/no

### Error Handling
- [ ] Can I confirm that HTTPException used for validation errors? — yes/no
- [ ] Can I confirm that Status codes are appropriate (400 for client errors, 500 for server errors)? — yes/no
- [ ] Can I confirm that Error messages are user-friendly? — yes/no
- [ ] Can I confirm that Try-except blocks used for file operations? — yes/no
- [ ] Can I confirm that No bare except: clauses (specific exceptions caught)? — yes/no

### Constants
- [ ] Can I confirm that No magic numbers in code? — yes/no
- [ ] Can I confirm that File size limit defined as constant (MAX_FILE_SIZE)? — yes/no
- [ ] Can I confirm that Allowed types defined as constant (ALLOWED_CONTENT_TYPES)? — yes/no
- [ ] Can I confirm that Constants at top of file or in config section? — yes/no

---

## Understanding & Comprehension

### Conceptual Understanding
- [ ] Can I confirm that I can explain what REST API is? — yes/no
- [ ] Can I confirm that I can explain the difference between GET and POST? — yes/no
- [ ] Can I confirm that I can explain why multipart/form-data is used for file uploads? — yes/no
- [ ] Can I confirm that I can explain what JSON is and why it's used? — yes/no
- [ ] Can I confirm that I can explain what Pydantic does? — yes/no
- [ ] Can I confirm that I can explain what CORS is and why it's needed? — yes/no
- [ ] Can I confirm that I can explain the difference between 127.0.0.1 and local IP? — yes/no
- [ ] Can I confirm that I can explain why --host 0.0.0.0 is needed? — yes/no

### Practical Knowledge
- [ ] Can I confirm that I can create a new FastAPI endpoint from scratch? — yes/no
- [ ] Can I confirm that I can define a Pydantic model with validation? — yes/no
- [ ] Can I confirm that I can handle file uploads in FastAPI? — yes/no
- [ ] Can I confirm that I can raise appropriate HTTP exceptions? — yes/no
- [ ] Can I confirm that I can find my laptop's local IP address? — yes/no
- [ ] Can I confirm that I can run server accessible from network? — yes/no
- [ ] Can I confirm that I can test API with Postman? — yes/no
- [ ] Can I confirm that I can read FastAPI auto-generated documentation? — yes/no

### Problem Solving
- [ ] Can I confirm that If server crashes, I know how to read error messages? — yes/no
- [ ] Can I confirm that If phone can't connect, I know troubleshooting steps? — yes/no
- [ ] Can I confirm that If validation fails, I know how to fix it? — yes/no
- [ ] Can I confirm that If import error occurs, I know to check virtual environment? — yes/no
- [ ] Can I confirm that I can debug using print statements or logging? — yes/no

---

## Performance & Best Practices

### Performance
- [ ] Can I confirm that Server responds to requests in < 1 second (dummy data is fast)? — yes/no
- [ ] Can I confirm that No memory leaks (server doesn't crash after multiple requests)? — yes/no
- [ ] Can I confirm that Large files handled gracefully (rejected before loading into memory)? — yes/no
- [ ] Can I confirm that File upload works with 1 MB images? — yes/no
- [ ] Can I confirm that File upload works with 5 MB images? — yes/no

### Best Practices
- [ ] Can I confirm that Secrets not hardcoded (no API keys in code)? — yes/no
- [ ] Can I confirm that No print() statements for production logging (use logging module)? — yes/no
- [ ] Can I confirm that Pydantic validation preferred over manual checks? — yes/no
- [ ] Can I confirm that Async functions used for file operations (async def, await)? — yes/no
- [ ] Can I confirm that CORS configured properly (not just allow_origins=["*"] in production)? — yes/no

---

## Week 05 Readiness

### Prerequisites for Next Week
- [ ] Can I confirm that Backend server working and tested? — yes/no
- [ ] Can I confirm that Local IP address documented? — yes/no
- [ ] Can I confirm that Postman collection works? — yes/no
- [ ] Can I confirm that Phone can access server on network? — yes/no
- [ ] Can I confirm that Base URL is clear (http://YOUR_IP:8000)? — yes/no
- [ ] Can I confirm that /predict endpoint tested with images? — yes/no
- [ ] Can I confirm that Response format understood (JSON with disease, confidence, etc.)? — yes/no
- [ ] Can I confirm that Confident in running server for Android testing? — yes/no

### Handoff to Week 05
- [ ] Can I confirm that I know I will use Retrofit in Android to call this API? — yes/no
- [ ] Can I confirm that I know Retrofit will send image as multipart/form-data? — yes/no
- [ ] Can I confirm that I know Android will parse JSON response using GSON? — yes/no
- [ ] Can I confirm that I know I need to keep backend running while testing Android app? — yes/no
- [ ] Can I confirm that I saved my local IP for use in Android BaseUrl configuration? — yes/no

---

## Final Checklist

### Before Moving to Week 05
- [ ] Can I confirm that All sections above are checked? — yes/no
- [ ] Can I confirm that Server runs without errors? — yes/no
- [ ] Can I confirm that Phone can access API? — yes/no
- [ ] Can I confirm that Postman tests all pass? — yes/no
- [ ] Can I confirm that Documentation complete? — yes/no
- [ ] Can I confirm that Git commits made? — yes/no
- [ ] Can I confirm that Evidence collected and organized? — yes/no
- [ ] Can I confirm that I feel confident about FastAPI basics? — yes/no
- [ ] Can I confirm that I am ready to integrate with Android? — yes/no

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
