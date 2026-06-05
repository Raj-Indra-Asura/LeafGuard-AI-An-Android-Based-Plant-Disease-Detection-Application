#!/usr/bin/env python3
"""
Exercise 2: File Upload and Image Validation
Week 4 — Backend Development

Run with:  python ex02_file_upload.py
Test with: curl -X POST http://localhost:8000/predict -F "file=@/path/to/leaf.jpg"
"""

from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.responses import JSONResponse
from PIL import Image
import io
import time
import uvicorn

app = FastAPI(title="LeafGuard AI API — Exercise 2")

# Configuration
ALLOWED_CONTENT_TYPES = {"image/jpeg", "image/png", "image/webp"}
MAX_FILE_SIZE_MB = 10
MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024
MIN_IMAGE_DIMENSION = 50   # Minimum 50x50 pixels


@app.post("/predict")
async def predict_disease(file: UploadFile = File(...)):
    """
    Upload a plant leaf image to detect disease.

    Steps:
    1. Validate file type (JPEG/PNG only)
    2. Read file bytes
    3. Validate file size (max 10 MB)
    4. Decode image (validate it's a real image)
    5. Validate image dimensions (min 50×50)
    6. Return mock prediction (real ML inference in Week 6)
    """
    start_time = time.time()

    # Step 1: Validate content type
    if file.content_type not in ALLOWED_CONTENT_TYPES:
        raise HTTPException(
            status_code=400,
            detail={
                "error": "INVALID_FILE_TYPE",
                "message": f"Expected {ALLOWED_CONTENT_TYPES}, got {file.content_type}",
                "hint": "Please upload a JPEG or PNG photo of the plant leaf"
            }
        )

    # Step 2: Read bytes
    contents = await file.read()

    # Step 3: Validate size
    if len(contents) > MAX_FILE_SIZE_BYTES:
        raise HTTPException(
            status_code=413,
            detail={
                "error": "FILE_TOO_LARGE",
                "message": f"File {len(contents)/(1024*1024):.1f} MB exceeds {MAX_FILE_SIZE_MB} MB limit",
                "hint": "Compress the image before uploading"
            }
        )

    # Step 4: Decode image
    try:
        image = Image.open(io.BytesIO(contents)).convert("RGB")
    except Exception as e:
        raise HTTPException(
            status_code=400,
            detail={
                "error": "INVALID_IMAGE",
                "message": "Cannot decode image file",
                "hint": "File may be corrupt or not a real image"
            }
        )

    # Step 5: Validate dimensions
    # TODO Exercise 2A: Implement minimum dimension check
    # if image.width < MIN_IMAGE_DIMENSION or image.height < MIN_IMAGE_DIMENSION:
    #     raise HTTPException(status_code=400, detail={"error": "IMAGE_TOO_SMALL", ...})

    # Step 6: Mock prediction (replace with real ML in Week 6)
    processing_ms = (time.time() - start_time) * 1000

    return {
        "success": True,
        "image_info": {
            "filename": file.filename,
            "size_kb": len(contents) / 1024,
            "dimensions": f"{image.width}×{image.height}",
            "format": image.format or "Unknown"
        },
        "prediction": {
            "disease": "MOCK_PREDICTION",
            "confidence": 0.0,
            "note": "Replace this with real ML model inference (Week 6)"
        },
        "processing_time_ms": round(processing_ms, 2)
    }


# TODO Exercise 2B: Add error handling for when the file field is missing entirely
# (FastAPI handles 422 automatically, but test it!)

# TODO Exercise 2C: Add a /batch-predict endpoint that accepts multiple files:
# async def batch_predict(files: List[UploadFile] = File(...)):

# TODO Exercise 2D: Add request logging (print to console):
# log each request: timestamp, filename, file_size, processing_time

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000, reload=True)
