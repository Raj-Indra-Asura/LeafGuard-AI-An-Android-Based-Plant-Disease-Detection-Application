# Interactive Notebooks Directory

This directory contains Jupyter notebooks and interactive learning materials for the LeafGuard AI project.

## 📓 What Are These Notebooks?

Interactive notebooks provide:
- **Executable code examples** - Run code directly in your browser
- **Visual explanations** - Diagrams, flowcharts, and illustrations
- **Step-by-step walkthroughs** - Detailed explanations with running code
- **Experiment-friendly environment** - Modify and test without breaking your project
- **Multimedia content** - Videos, images, and interactive widgets

## 🎯 How to Use Notebooks

### For Different Learners

**Visual Learners:**
- Diagrams and flowcharts explain concepts
- Watch embedded video tutorials
- See code output immediately

**Hands-on Learners:**
- Modify code cells and run experiments
- Test different approaches safely
- Break things to understand how they work

**Theory Learners:**
- Detailed explanations alongside code
- Concept foundations before implementation
- References to academic sources

## 📂 Directory Structure

```
notebooks/
├── README.md (this file)
├── setup-guide.md (How to install Python + Jupyter and open a notebook)
├── week-01/  01-project-understanding-and-architecture.ipynb
├── week-02/  01-android-ui-fundamentals.ipynb
├── week-03/  01-camera-gallery-image-processing.ipynb
├── ...
└── week-12/  01-final-submission-preparation.ipynb
```

Each `week-NN/` folder also contains a `README.md` that reads like a Markdown
notebook (explanations + code to read) for that week.

## 🚀 Getting Started

### Prerequisites
- Python 3.8+
- Jupyter Notebook or JupyterLab
- Internet connection (for some notebooks)

### Installation

```bash
# Install Jupyter
pip install jupyter notebook

# Or install JupyterLab (recommended)
pip install jupyterlab

# Navigate to notebooks directory
cd notebooks/

# Start Jupyter
jupyter notebook
# OR
jupyter lab
```

### Quick Start Guide
See [setup-guide.md](setup-guide.md) for detailed installation instructions.

## 📚 Week-by-Week Notebooks

> **What is a Jupyter notebook?** It is an interactive document that mixes
> readable text with runnable Python code, cell by cell. You read an explanation,
> then run the code right below it and see the output immediately.
>
> **Kotlin is the default track** for the Android app. The Android-week notebooks
> below are **companions to the Android Studio project** (they explain and simulate
> concepts in Python) — they are *not* standalone Python replacements for the app.
> See **[setup-guide.md](setup-guide.md)** to install Python + Jupyter and open one.

Every week folder contains one real `.ipynb` notebook plus a Markdown `README.md`:

### Foundation Phase (Weeks 1-3)
- **Week 01** — [`01-project-understanding-and-architecture.ipynb`](week-01/01-project-understanding-and-architecture.ipynb): the three-tier architecture (Android client, FastAPI backend, ML model) and how the repo is laid out.
- **Week 02** — [`01-android-ui-fundamentals.ipynb`](week-02/01-android-ui-fundamentals.ipynb): Android UI basics — activities, layouts, and navigation between screens.
- **Week 03** — [`01-camera-gallery-image-processing.ipynb`](week-03/01-camera-gallery-image-processing.ipynb): capturing a photo (camera/gallery), permissions, and basic image handling.

### Backend Integration (Weeks 4-6)
- **Week 04** — [`01-fastapi-backend-fundamentals.ipynb`](week-04/01-fastapi-backend-fundamentals.ipynb): building the FastAPI `POST /predict` endpoint that accepts an `image` upload.
- **Week 05** — [`01-android-networking-retrofit.ipynb`](week-05/01-android-networking-retrofit.ipynb): calling the backend from Android with Retrofit and parsing the JSON reply.
- **Week 06** — [`01-ml-model-integration.ipynb`](week-06/01-ml-model-integration.ipynb): loading a model on the server, preprocessing an image, and returning a `disease` prediction.

### Data & Offline (Weeks 7-9)
- **Week 07** — [`01-room-database-sql-fundamentals.ipynb`](week-07/01-room-database-sql-fundamentals.ipynb): SQL and Room basics for saving scan history on the device.
- **Week 08** — [`01-xml-disease-library-parsing.ipynb`](week-08/01-xml-disease-library-parsing.ipynb): parsing `assets/diseases.xml` into an offline disease library.
- **Week 09** — [`01-tensorflow-lite-on-device-inference.ipynb`](week-09/01-tensorflow-lite-on-device-inference.ipynb): running a TensorFlow Lite model on-device (offline detection).

### Polish & Completion (Weeks 10-12)
- **Week 10** — [`01-notifications-share-location.ipynb`](week-10/01-notifications-share-location.ipynb): notifications, sharing a result, and adding location.
- **Week 11** — [`01-testing-debugging-performance.ipynb`](week-11/01-testing-debugging-performance.ipynb): writing tests, debugging, and checking performance.
- **Week 12** — [`01-final-submission-preparation.ipynb`](week-12/01-final-submission-preparation.ipynb): building the APK and preparing the final submission.

## 🎨 Notebook Types

### 1. Concept Notebooks
**Focus:** Theoretical understanding
**Format:** Explanations → Diagrams → Simple examples
**When to use:** Before starting exercises

### 2. Tutorial Notebooks
**Focus:** Step-by-step implementation
**Format:** Problem → Approach → Code → Testing
**When to use:** During build tasks

### 3. Workshop Notebooks
**Focus:** Hands-on experimentation
**Format:** Challenge → Experiment → Solution → Extensions
**When to use:** After completing exercises

### 4. Reference Notebooks
**Focus:** Quick lookup and examples
**Format:** API docs → Code snippets → Common patterns
**When to use:** While coding your project

## 💡 Best Practices

### For Maximum Learning

1. **Run Every Cell**
   - Don't just read - execute the code
   - Observe outputs and behaviors
   - Understand what each cell does

2. **Modify and Experiment**
   - Change parameters and see effects
   - Break code intentionally to understand errors
   - Try alternative approaches

3. **Take Notes**
   - Add markdown cells with your observations
   - Document "aha!" moments
   - Write questions for later research

4. **Save Your Work**
   - Create a copy before experimenting
   - Save modified notebooks separately
   - Document your experiments

5. **Pair with Other Materials**
   - Read learning-notes.md first
   - Use notebooks during exercises
   - Reference while coding build tasks

## 🔧 Technical Requirements

### For Android Notebooks
- Android Studio (for some demonstrations)
- Android SDK tools
- Java/Kotlin kernel (optional)

### For Backend Notebooks
- Python 3.8+
- FastAPI and dependencies
- TensorFlow/PyTorch (for ML notebooks)

### For All Notebooks
- Jupyter Notebook or JupyterLab
- Internet browser (Chrome, Firefox recommended)
- 4GB+ RAM recommended

## 📱 Mobile-Friendly Alternatives

Can't run Jupyter on your device?
- View notebooks on GitHub (static rendering)
- Use Google Colab (cloud-based, free)
- Use Kaggle Kernels (cloud-based, free)
- Use Binder (temporary cloud instances)

## 🤝 Contributing Notebooks

Want to create or improve notebooks?

### Contribution Guidelines
1. Follow existing notebook structure
2. Include clear explanations and comments
3. Test all code cells before submitting
4. Add visualizations where helpful
5. Include exercises at the end

### Notebook Template
See `notebook-template.ipynb` for structure

## 📖 Additional Resources

### Learning Jupyter
- [Official Jupyter Documentation](https://jupyter.org/documentation)
- [Jupyter Notebook Tutorial](https://www.dataquest.io/blog/jupyter-notebook-tutorial/)
- [JupyterLab Documentation](https://jupyterlab.readthedocs.io/)

### Interactive Learning
- [Google Colab](https://colab.research.google.com/)
- [Kaggle Learn](https://www.kaggle.com/learn)
- [Binder](https://mybinder.org/)

## 🔗 Related Directories

- [Solutions](../solutions/) - Exercise solutions and explanations
- [Exercises](../exercises/) - Practice problems by topic
- [Roadmap](../roadmap/) - Week-by-week learning path
- [Code Examples](../roadmap/) - Minimal working snippets

## ⚡ Quick Navigation

- 🏠 [Back to Learning Path](../LEARNING_PATH.md)
- 📚 [Exercises Directory](../exercises/)
- 💡 [Solutions Directory](../solutions/)
- 📖 [Main README](../README.md)

---

## 🚀 Start Learning Interactively!

1. Install Jupyter: `pip install jupyterlab`
2. Navigate to notebooks: `cd notebooks/`
3. Start Jupyter: `jupyter lab`
4. Open Week 01 notebooks and begin!

**Interactive learning makes complex concepts easier to understand. Dive in and experiment! 🎓**

---

**Status:** Notebooks are being developed progressively.
**Last Updated:** 2026-05-27
**Maintained by:** LeafGuard AI Learning System
