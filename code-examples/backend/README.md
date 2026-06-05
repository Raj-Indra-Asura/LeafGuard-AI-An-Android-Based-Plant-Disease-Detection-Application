# Backend Code Examples

## 1. Minimal FastAPI prediction API

```python
from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from PIL import Image
import io

app = FastAPI(title="LeafGuard AI API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/health")
def health():
    return {"status": "ok", "service": "leafguard-api"}

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    if not file.content_type or not file.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="Upload must be an image")

    raw = await file.read()
    image = Image.open(io.BytesIO(raw)).convert("RGB")
    width, height = image.size

    # Replace this mock result with real model inference in Week 06.
    return {
        "label": "Tomato___healthy",
        "confidence": 0.87,
        "image_width": width,
        "image_height": height,
        "advice": "Mock result: connect trained model before final submission."
    }
```

## 2. Run locally

```bash
python -m venv .venv
source .venv/bin/activate  # Windows: .venv\\Scripts\\activate
pip install fastapi uvicorn pillow python-multipart
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

## 3. Test with curl

```bash
curl http://127.0.0.1:8000/health
curl -X POST http://127.0.0.1:8000/predict -F "file=@sample_leaf.jpg"
```

## 4. Production checklist

- Validate image type.
- Limit file size.
- Log prediction latency.
- Return stable JSON keys.
- Never expose stack traces to app users.
