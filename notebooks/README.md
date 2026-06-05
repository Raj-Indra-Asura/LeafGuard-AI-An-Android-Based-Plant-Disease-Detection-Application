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
├── setup-guide.md (How to run notebooks)
├── week-01/
│   ├── 01-understanding-mobile-architecture.ipynb
│   ├── 02-android-vs-ios-comparison.ipynb
│   └── resources/
├── week-02/
│   ├── 01-android-activity-lifecycle.ipynb
│   ├── 02-xml-layouts-deep-dive.ipynb
│   └── resources/
└── week-12/
    └── ...
```

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

> **ℹ️ Status Note:** Interactive Jupyter `.ipynb` notebooks have not been created yet. All learning content is available in the detailed `learning-notes.md`, `exercises.md`, and `build-task.md` files in each week's roadmap folder — those files are the primary learning resource and are fully written.
>
> **Recommended alternative for interactive Python experiments:** Use [Google Colab](https://colab.research.google.com/) — free, browser-based, no installation needed. The `model/model-acquisition-guide.md` includes a complete Colab notebook setup for training your TFLite model.

### Foundation Phase (Weeks 1-3)

#### Week 01: Project Understanding
- 📓 Understanding mobile app architecture *(Not yet available — see [roadmap/week-01](../roadmap/week-01-project-understanding/))*
- 📓 Android vs iOS platform comparison *(Not yet available)*
- 📓 CSE 2206 syllabus mapping interactive guide *(Not yet available)*

#### Week 02: Android Basics & UI
- 📓 Android Activity lifecycle interactive visualization *(Not yet available — see [roadmap/week-02](../roadmap/week-02-android-basics-ui/))*
- 📓 XML layouts deep dive with live preview *(Not yet available)*
- 📓 Material Design principles workshop *(Not yet available)*

#### Week 03: Camera & Gallery
- 📓 Android Intents explained with examples *(Not yet available — see [roadmap/week-03](../roadmap/week-03-camera-gallery/))*
- 📓 Runtime permissions system interactive guide *(Not yet available)*
- 📓 Image processing and Bitmap manipulation *(Not yet available)*

### Backend Integration (Weeks 4-6)

#### Week 04: FastAPI Backend
- 📓 REST API fundamentals *(Not yet available — see [roadmap/week-04](../roadmap/week-04-fastapi-backend/))*
- 📓 Building FastAPI endpoints step-by-step *(Not yet available — see [backend-api/main.py](../backend-api/main.py) for working starter code)*
- 📓 Multipart file upload handling *(Not yet available)*

#### Week 05: Android Networking
- 📓 Retrofit architecture explained *(Not yet available — see [roadmap/week-05](../roadmap/week-05-android-networking/))*
- 📓 JSON parsing with Gson *(Not yet available)*
- 📓 Async operations and callbacks *(Not yet available)*

#### Week 06: Cloud ML Model
- 📓 CNN model architecture visualization *(Not yet available — see [roadmap/week-06](../roadmap/week-06-cloud-ml-model/))*
- 📓 Image preprocessing pipeline *(Not yet available — see [model/model-acquisition-guide.md](../model/model-acquisition-guide.md))*
- 📓 Model inference and prediction handling *(Not yet available)*

### Data & Offline (Weeks 7-9)

#### Week 07: Room Database
- 📓 Room architecture components *(Not yet available — see [roadmap/week-07](../roadmap/week-07-room-sqlite-history/))*
- 📓 SQL basics for mobile developers *(Not yet available)*
- 📓 CRUD operations with Room *(Not yet available — see [solutions/week-07](../solutions/week-07/room-database-solution.md))*

#### Week 08: XML Parsing
- 📓 XML structure and parsing methods *(Not yet available — see [roadmap/week-08](../roadmap/week-08-xml-disease-library/))*
- 📓 XmlPullParser step-by-step guide *(Not yet available)*
- 📓 Data mapping and transformation *(Not yet available)*

#### Week 09: TensorFlow Lite
- 📓 TFLite model conversion process *(Not yet available — see [roadmap/week-09](../roadmap/week-09-tensorflow-lite-offline-ai/))*
- 📓 On-device inference optimization *(Not yet available — see [solutions/week-09](../solutions/week-09/tflite-classifier-solution.md))*
- 📓 Cloud vs offline performance comparison *(Not yet available)*

### Polish & Completion (Weeks 10-12)

#### Week 10: Notifications & Share
- 📓 Android notification system explained *(Not yet available — see [roadmap/week-10](../roadmap/week-10-notifications-share-location/))*
- 📓 Intent sharing deep dive *(Not yet available)*
- 📓 Location services integration *(Not yet available)*

#### Week 11: Testing & Debugging
- 📓 Android debugging techniques *(Not yet available — see [roadmap/week-11](../roadmap/week-11-testing-debugging-performance/))*
- 📓 Test case design patterns *(Not yet available)*
- 📓 Performance profiling guide *(Not yet available)*

#### Week 12: Final Submission
- 📓 APK building and signing *(Not yet available — see [roadmap/week-12](../roadmap/week-12-final-submission/))*
- 📓 Documentation best practices *(Not yet available)*
- 📓 Viva preparation interactive guide *(Not yet available)*

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
