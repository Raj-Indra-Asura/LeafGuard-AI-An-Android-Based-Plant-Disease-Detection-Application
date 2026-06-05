#!/usr/bin/env python3
"""
Exercise 1: FastAPI Hello World + Health Check
Week 4 — Backend Development

Run with:  python ex01_hello_fastapi.py
Then visit: http://localhost:8000/docs
"""

from fastapi import FastAPI
import uvicorn

app = FastAPI(
    title="LeafGuard AI API — Exercise 1",
    description="Starting point for building the disease detection backend",
    version="0.1.0"
)


@app.get("/")
async def root():
    """Root endpoint — confirms the server is running."""
    return {
        "message": "LeafGuard AI Backend is running!",
        "version": "0.1.0",
        "docs": "/docs"
    }


@app.get("/health")
async def health_check():
    """
    Health check endpoint.
    Android app calls this to verify the server is reachable before uploading images.
    """
    return {
        "status": "healthy",
        "model_loaded": False,   # Will be True once ML model is integrated
        "api_version": "0.1.0"
    }


# TODO Exercise 1A: Add a GET /about endpoint returning:
#   { "project": "LeafGuard AI", "author": "your name", "college": "your college" }

# TODO Exercise 1B: Add a GET /diseases endpoint returning a hardcoded list of 5 disease names:
#   { "diseases": ["Tomato Early Blight", "Tomato Late Blight", ...] }

# TODO Exercise 1C: Add query parameter support to /diseases:
#   GET /diseases?plant=Tomato → returns only tomato diseases
#   Hint: def get_diseases(plant: str = None):

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000, reload=True)
