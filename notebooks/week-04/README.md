# Week 04 Interactive Notebook

## Building the LeafGuard Disease Detection API

> This README acts like a Markdown notebook for CSE 2206. Read one cell at a time, run the code, and write your own notes after each checkpoint.

### How to use this notebook

- Follow the cells in order.
- Run code blocks in Android Studio, Terminal, or a Python shell as indicated.
- Keep LeafGuard AI open in Android Studio while you work.
- Save screenshots for your evidence folder after each big milestone.
- Use Java for Android code in this repository. Do not switch to Kotlin.

### Weekly outcomes

- Set up a Python virtual environment and FastAPI project structure.
- Understand the complete `main.py` request flow for `/predict`.
- Test with curl and Postman, then deploy to Render.

### Repository references

- `backend-api/main.py`
- `backend-api/requirements.txt`
- `roadmap/week-04-fastapi-backend/`

---

## Notebook Cell 1 — Create the Python environment

### Explanation

- FastAPI is a Python web framework that makes it easy to build JSON APIs for LeafGuard AI.

### Code to Read / Run

```bash
cd backend-api
python3 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
pip install -r requirements.txt
```

### 🔵 Try This

- Run `pip list` and confirm FastAPI, Pillow, NumPy, and TensorFlow are installed.

### Expected Output

- The environment activates and installs dependencies without conflicts.

### ✅ Checkpoint

- Why should the backend keep its own environment separate from the Android tooling?

### ⚠️ Common Mistake

- Do not run backend commands without activating the virtual environment.

### 📌 Key Point

- A stable environment is the first backend success criterion.

## Notebook Cell 2 — Learn the FastAPI project structure

### Explanation

- Backend files should stay small and focused: config, model loader, and API entry point.

### Code to Read / Run

```text
backend-api/
├── main.py
├── config.py
├── model_loader.py
├── requirements.txt
└── .env.example
```

### 🔵 Try This

- Open each file and write one line describing its role.

### Expected Output

- You understand where runtime settings and model loading belong.

### ✅ Checkpoint

- Which file should hold environment-specific constants?

### ⚠️ Common Mistake

- Do not hardcode secrets or deployment-specific URLs into `main.py`.

### 📌 Key Point

- File organization matters on the backend just as much as it does on Android.

## Notebook Cell 3 — Walk through a complete FastAPI app

### Explanation

- The `/predict` route accepts an uploaded image, preprocesses it, runs prediction, and returns structured disease guidance.

### Code to Read / Run

```python
from fastapi import FastAPI, File, HTTPException, UploadFile
from pydantic import BaseModel
from PIL import Image
import io
import numpy as np

app = FastAPI(title="LeafGuard AI Backend")

DISEASE_INFO = {
    "Tomato Early Blight": {
        "symptoms": "Small brown spots with rings.",
        "treatment": "Remove infected leaves and use labeled fungicide.",
        "prevention": "Rotate crops and avoid wet foliage."
    },
    "Tomato Healthy": {
        "symptoms": "Healthy green leaves.",
        "treatment": "No treatment needed.",
        "prevention": "Continue regular monitoring."
    }
}

class PredictionResult(BaseModel):
    disease: str
    confidence: float
    symptoms: str
    treatment: str
    prevention: str

def preprocess_image(raw_bytes: bytes) -> np.ndarray:
    image = Image.open(io.BytesIO(raw_bytes)).convert("RGB")
    image = image.resize((224, 224))
    array = np.asarray(image, dtype=np.float32) / 255.0
    return np.expand_dims(array, axis=0)

@app.get("/")
async def health_check():
    return {"status": "ok", "message": "LeafGuard API running"}

@app.post("/predict", response_model=PredictionResult)
async def predict(image: UploadFile = File(...)):
    if not image.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="Uploaded file must be an image.")

    raw_bytes = await image.read()
    model_input = preprocess_image(raw_bytes)

    disease = "Tomato Early Blight"
    confidence = 0.93
    info = DISEASE_INFO[disease]

    return PredictionResult(
        disease=disease,
        confidence=confidence,
        symptoms=info["symptoms"],
        treatment=info["treatment"],
        prevention=info["prevention"],
    )
```

### 🔵 Try This

- Explain what happens before and after `preprocess_image`.
- Change the mock disease name and observe the API response.

### Expected Output

- You can trace the request from upload to JSON response.

### ✅ Checkpoint

- Why is `response_model` useful in FastAPI?

### ⚠️ Common Mistake

- Do not return raw NumPy arrays directly to the Android app.

### 📌 Key Point

- The backend should return clean JSON that Android can parse easily.

## Notebook Cell 4 — Run the API locally

### Explanation

- Uvicorn is the ASGI server that hosts the FastAPI app during development.

### Code to Read / Run

```bash
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

### 🔵 Try This

- Open `http://127.0.0.1:8000/docs` in your browser after the server starts.

### Expected Output

- The terminal shows Uvicorn listening on port 8000 and the docs UI loads.

### ✅ Checkpoint

- Can you explain what `--reload` does?

### ⚠️ Common Mistake

- If the module cannot be imported, verify you are inside `backend-api/` when running the command.

### 📌 Key Point

- Swagger docs make API exploration quick during development.

## Notebook Cell 5 — Test with curl and Postman

### Explanation

- Testing outside Android isolates backend issues before you add Retrofit.

### Code to Read / Run

```bash
curl http://127.0.0.1:8000/

curl -X POST http://127.0.0.1:8000/predict       -H "accept: application/json"       -H "Content-Type: multipart/form-data"       -F "image=@../sample-images/tomato_leaf.jpg"
```

### 🔵 Try This

- Repeat the same request in Postman using form-data and a file field named `image`.

### Expected Output

- You receive JSON containing disease, confidence, symptoms, treatment, and prevention.

### ✅ Checkpoint

- Which part of the request proves this endpoint accepts a file upload?

### ⚠️ Common Mistake

- If you use the wrong field name, FastAPI returns a validation error.

### 📌 Key Point

- API testing with curl/Postman saves time before mobile integration.

## Notebook Cell 6 — Understand mock mode

### Explanation

- Mock mode lets you keep the API development moving even when the final ML model is not ready.

### Step-by-Step

1. Return a fixed disease and confidence score for early frontend integration.
2. Add an environment flag later to switch between real model inference and mock responses.
3. Use mock mode to test error handling and UI loading states.

### 🔵 Try This

- List two reasons mock mode is useful in a student project.

### Expected Output

- You understand why frontend and backend work can continue in parallel.

### ✅ Checkpoint

- Can you explain when mock mode should be disabled?

### ⚠️ Common Mistake

- Do not forget to document clearly whether your demo is using mock or real predictions.

### 📌 Key Point

- Mocking is a workflow tool, not a final product state.

## Notebook Cell 7 — Deploy to Render.com

### Explanation

- Render offers an easy free-hosting path for small FastAPI services used in coursework demos.

### Step-by-Step

1. Push the repository to GitHub.
2. Create a new Web Service on Render.
3. Select the repository and set the root directory to `backend-api` if needed.
4. Use `pip install -r requirements.txt` as the build command.
5. Use `uvicorn main:app --host 0.0.0.0 --port $PORT` as the start command.
6. Set environment variables if your model path or mode settings require them.

### 🔵 Try This

- After deployment, open the deployed `/docs` page and test `/predict` again.

### Expected Output

- Render builds the app and returns a public URL.

### ✅ Checkpoint

- Why must the start command bind to `0.0.0.0` instead of only `127.0.0.1`?

### ⚠️ Common Mistake

- If deployment fails, read the build logs before changing code blindly.

### 📌 Key Point

- Deployment is part of proving your architecture works beyond localhost.

## Notebook Cell 8 — Use the complete requirements.txt

### Explanation

- Pinning package versions improves reproducibility between your machine and deployment.

### Code to Read / Run

```text
fastapi==0.109.0
uvicorn[standard]==0.27.0
python-multipart==0.0.6
pillow==10.2.0
numpy==1.26.3
tensorflow==2.14.0
python-dotenv==1.0.0
```

### 🔵 Try This

- Compare this file with `backend-api/requirements.txt` in the repository.

### Expected Output

- Your environment installs the same key packages used by the project.

### ✅ Checkpoint

- Which dependency is needed specifically for multipart file upload handling?

### ⚠️ Common Mistake

- Do not leave required backend packages undocumented or unpinned.

### 📌 Key Point

- Requirements files are part of the backend contract.

## Lab Reflection

- Write down one concept that felt easy.
- Write down one concept that felt confusing.
- Describe one bug you saw and how you fixed it.
- State which file changed the most during this notebook.
- Explain how this week supports the final LeafGuard AI submission.

## Mini Quiz

- What problem does this week solve inside LeafGuard AI?
- Which Java class or Android component did you touch first?
- Which file path in this repository is most relevant to this week?
- What would break if you skipped the validation step?
- How does this week connect to the three-tier architecture?

## Evidence Checklist

- [ ] Capture a screenshot of the completed screen or terminal output.
- [ ] Save one code snippet that proves the feature is wired correctly.
- [ ] Write two sentences in your progress log about what you learned.
- [ ] Record at least one bug and the exact fix you applied.
- [ ] Commit working changes before moving to the next week.

## Next Step

- After this notebook, continue to **[Week 05: Android Networking](../../roadmap/week-05-android-networking/README.md)** and connect today's work to the next subsystem.


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

### Related Roadmap Materials
- 📖 [Week 04 README](../../roadmap/week-04-fastapi-backend/README.md) — Week overview & objectives
- 📝 [Week 04 Exercises](../../roadmap/week-04-fastapi-backend/exercises.md) — Practice problems
- 💡 [Week 04 Solutions](../../solutions/week-04/README.md) — Reference solutions
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

### Week Progression

| ← Previous | 🏠 | Next → |
|:-----------|:--:|-------:|
| [⬅ Week 03 Notebooks](../week-03/README.md) | [Notebooks Index](../README.md) | [Week 05 Notebooks ➡](../week-05/README.md) |

---
