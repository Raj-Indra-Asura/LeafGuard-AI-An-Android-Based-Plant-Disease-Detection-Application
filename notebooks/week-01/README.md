# Week 01 Interactive Notebook

## Getting Started with LeafGuard AI

> This README acts like a Markdown notebook for CSE 2206. Read one cell at a time, run the code, and write your own notes after each checkpoint.

### How to use this notebook

- Follow the cells in order.
- Run code blocks in Android Studio, Terminal, or a Python shell as indicated.
- Keep LeafGuard AI open in Android Studio while you work.
- Save screenshots for your evidence folder after each big milestone.
- Use Java for Android code in this repository. Do not switch to Kotlin.

### Weekly outcomes

- Install Android Studio and create a Java project with minimum SDK 24.
- Understand the LeafGuard AI three-tier architecture: Android client, FastAPI backend, and ML model.
- Clone the repository and prepare Python tools for later backend weeks.
- Run a starter Hello World screen and verify your environment works.

### Repository references

- `android-app/README.md`
- `backend-api/main.py`
- `roadmap/week-01-project-understanding/`
- `model/model-notes.md`

---

## Notebook Cell 1 — See the full LeafGuard AI architecture

### Explanation

- LeafGuard AI is a three-tier academic project: the presentation layer is the Android app, the service layer is FastAPI, and the intelligence layer is the plant disease model.
- The Android app collects an image, sends it to an API or local TFLite model, then displays disease information and history.
- CSE 2206 often talks about separation of concerns; this architecture is the concrete project example.

### Step-by-Step

1. Open the repository root in a terminal.
2. Identify the `android-app/`, `backend-api/`, and `model/` folders.
3. Sketch the data flow from camera to prediction result to saved history.

### Code to Read / Run

```text
LeafGuard AI request flow
-------------------------
User taps Scan
    |
    v
Android app captures image
    |
    +--> Online mode: Retrofit -> FastAPI -> TensorFlow/Keras model -> JSON response
    |
    +--> Offline mode: TFLite interpreter on device -> prediction array
    |
    v
ResultActivity shows disease, confidence, treatment, prevention
    |
    v
Room database saves scan history
```

### 🔵 Try This

- Draw the same diagram in your notebook without looking back.
- Label which layer owns UI logic, API logic, and model logic.

### Expected Output

- You can point to one folder for each tier.
- You can explain why an Android screen should not contain model training code.

### ✅ Checkpoint

- Say out loud what the three tiers are.
- Explain where JSON is used in the workflow.

### ⚠️ Common Mistake

- Do not confuse FastAPI with the ML model itself; FastAPI is the service wrapper.

### 📌 Key Point

- A clear three-tier design makes LeafGuard AI easier to debug and present in class.

## Notebook Cell 2 — Install Android Studio correctly

### Explanation

- Android Studio bundles the IDE, Gradle support, emulator tools, and SDK manager you need for Java Android development.
- For LeafGuard AI, API 24 is the minimum target so the app still runs on older Android devices.

### Step-by-Step

1. Visit developer.android.com/studio and download the latest stable Android Studio installer.
2. During installation, keep the Android SDK, Android Virtual Device, and performance acceleration options selected.
3. Launch Android Studio and let it download any missing SDK components.
4. Open SDK Manager and confirm Android 14 platform tools are installed.
5. Open Device Manager and prepare at least one Pixel emulator image.

### Code to Read / Run

```text
Installation checklist
----------------------
[ ] Android Studio installed
[ ] Android SDK Platform 34 installed
[ ] Android SDK Build-Tools installed
[ ] Android Emulator installed
[ ] One virtual device created
[ ] JDK 11+ available
```

### 🔵 Try This

- Open Device Manager and create a Pixel 5 emulator with API 34.
- Record the emulator name you created for later testing.

### Expected Output

- Android Studio launches without setup warnings.
- The SDK Manager shows at least one installed SDK platform.

### ✅ Checkpoint

- Can you explain why Android Studio is more than just a code editor?

### ⚠️ Common Mistake

- Do not skip the emulator image download; it is needed for Week 02 and Week 03 testing.

### 📌 Key Point

- Your setup is not complete until the emulator or a physical device is ready to run apps.

## Notebook Cell 3 — Create your first Java Android project

### Explanation

- Creating the project yourself helps you learn where Android Studio puts code, resources, and manifests.
- LeafGuard AI should use Java, not Kotlin, because the course repository is organized around Java examples.

### Step-by-Step

1. Choose **New Project** > **Empty Views Activity**.
2. Set the app name to `LeafGuard AI`.
3. Set the package name to `com.leafguard`.
4. Set the language to `Java`.
5. Set minimum SDK to `API 24: Android 7.0 (Nougat)`.
6. Enable View Binding after the project opens if it is not already enabled.

### Code to Read / Run

```gradle
plugins {
    id 'com.android.application'
}

android {
    namespace 'com.leafguard'
    compileSdk 34

    defaultConfig {
        applicationId "com.leafguard"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding true
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }
}
```

### 🔵 Try This

- Match your generated `app/build.gradle` with the block above.
- Highlight where `minSdk 24` is declared.

### Expected Output

- Gradle sync finishes successfully.
- The project builds even before you add extra features.

### ✅ Checkpoint

- Can you explain what `applicationId` and `minSdk` do?

### ⚠️ Common Mistake

- Choosing Kotlin here will make later Java code examples harder to follow.

### 📌 Key Point

- Week 01 is about foundation, so get the project settings correct before adding features.

## Notebook Cell 4 — Understand the Android project structure

### Explanation

- Android apps separate Java code, XML layouts, assets, tests, and build files into different folders.
- Knowing where files belong makes later weeks much faster.

### Step-by-Step

1. Expand the `android-app/` folder.
2. Open `app/src/main/AndroidManifest.xml`.
3. Open the `java/`, `res/layout/`, and `res/values/` folders.

### Code to Read / Run

```text
android-app/
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/leafguard/
│       │   └── MainActivity.java
│       ├── res/layout/
│       │   └── activity_main.xml
│       ├── res/values/
│       │   ├── strings.xml
│       │   ├── colors.xml
│       │   └── themes.xml
│       └── assets/
├── build.gradle
└── settings.gradle
```

### 🔵 Try This

- Write one sentence about what goes inside each folder shown above.

### Expected Output

- You can map Java files to behavior and XML files to UI.

### ✅ Checkpoint

- Where do drawable resources belong?
- Where does the manifest declare app permissions?

### ⚠️ Common Mistake

- Do not place Java classes inside the `res/` folder.

### 📌 Key Point

- The project structure is part of the Android framework contract, not a random folder choice.

## Notebook Cell 5 — Clone this learning repository

### Explanation

- Cloning the repository gives you roadmaps, notebooks, backend code, and solution references in one place.
- Git is also part of professional workflow and viva discussion.

### Step-by-Step

1. Create or choose a working folder on your machine.
2. Use Git to clone the project.
3. Open the folder in Android Studio or your editor.

### Code to Read / Run

```bash
git clone https://github.com/Raj-Indra-Asura/LeafGuard-AI-An-Android-Based-Plant-Disease-Detection-Application.git
cd LeafGuard-AI-An-Android-Based-Plant-Disease-Detection-Application
git status
```

### 🔵 Try This

- Run `git status` and confirm the repository is clean.
- Browse to `roadmap/week-01-project-understanding/` and inspect the learning notes.

### Expected Output

- Git prints the current branch and reports nothing to commit.

### ✅ Checkpoint

- Can you explain the difference between cloning a repository and downloading a ZIP file?

### ⚠️ Common Mistake

- If you clone into a protected folder, Android Studio may not have permission to write Gradle files.

### 📌 Key Point

- Use Git from the beginning so your weekly progress is traceable.

## Notebook Cell 6 — Prepare Python for FastAPI weeks

### Explanation

- Even though the Android app is Java, the backend and model tooling depend on Python.
- Creating a virtual environment isolates project packages from global packages.

### Step-by-Step

1. Open a terminal at the repository root.
2. Create a virtual environment inside `backend-api/` or the repository root.
3. Activate it and install backend dependencies.

### Code to Read / Run

```bash
python3 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
pip install -r backend-api/requirements.txt
python -c "import fastapi, numpy, PIL; print('Python environment ready')"
```

### 🔵 Try This

- Run the final Python import test and save the output.
- Write down why virtual environments are safer than system-wide installs.

### Expected Output

- The terminal prints `Python environment ready`.

### ✅ Checkpoint

- Can you explain what `pip install -r` does?

### ⚠️ Common Mistake

- Do not forget to activate the environment before running backend scripts.

### 📌 Key Point

- Week 04 becomes much easier if Python is working now.

## Notebook Cell 7 — Install the supporting tools

### Explanation

- LeafGuard AI is a multi-tool project: Git handles version control, Postman or curl tests the API, and Jupyter is optional for experiments.

### Step-by-Step

1. Install Git if it is not already available.
2. Install Postman or plan to use curl.
3. Optionally install JupyterLab for Python notebook experiments.

### Code to Read / Run

```bash
git --version
python3 --version
pip install jupyterlab
jupyter lab --version
```

### 🔵 Try This

- Run each version check and note which tools are already installed.

### Expected Output

- You see version numbers rather than command-not-found errors.

### ✅ Checkpoint

- Which tool is best for API exploration: Android Studio, Git, or Postman?

### ⚠️ Common Mistake

- Jupyter is optional for this repository; Android Studio and Git are not optional.

### 📌 Key Point

- Tooling problems are easiest to solve before the workload gets bigger.

## Notebook Cell 8 — Run a Hello World starter screen

### Explanation

- A successful Hello World run proves your IDE, emulator, Gradle, and Java toolchain are all functioning.
- This is your first validation point in CSE 2206.

### Step-by-Step

1. Replace the generated `MainActivity.java` with a small starter class.
2. Replace the layout with a simple `TextView`.
3. Click Run in Android Studio and choose your emulator.

### Code to Read / Run

```java
package com.leafguard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```

### 🔵 Try This

- Create a `TextView` in `activity_main.xml` that says `Hello LeafGuard AI`.
- Run the app and take a screenshot of the emulator.

### Expected Output

- The emulator opens and shows your Hello LeafGuard AI message.
- Android Studio displays `BUILD SUCCESSFUL`.

### ✅ Checkpoint

- Can you explain what `setContentView` does?
- Can you explain why a successful run is more valuable than just a successful build?

### ⚠️ Common Mistake

- If the app closes immediately, check Logcat for a missing resource or manifest issue.

### 📌 Key Point

- Always verify both build and run behavior.

## Notebook Cell 9 — Checkpoint: explain the three-tier architecture

### Explanation

- This cell is a speaking and writing checkpoint rather than a coding checkpoint.
- You should now be able to explain LeafGuard AI to an examiner in under one minute.

### Step-by-Step

1. Describe the Android app layer in one sentence.
2. Describe the FastAPI layer in one sentence.
3. Describe the ML model layer in one sentence.
4. Explain why the Room database belongs on the app side, not the API side.

### 🔵 Try This

- Record yourself explaining the architecture in 30 to 60 seconds.
- Rewrite the explanation once using simple words and once using technical words.

### Expected Output

- Your explanation mentions UI, API communication, and prediction logic.

### ✅ Checkpoint

- If someone asked where disease treatment advice comes from, could you answer?

### ⚠️ Common Mistake

- Do not answer with only folder names; connect each folder to a responsibility.

### 📌 Key Point

- Understanding architecture early prevents random coding later.

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

- After this notebook, continue to **[Week 02: Android Basics & UI](../../roadmap/week-02-android-basics-ui/README.md)** and connect today's work to the next subsystem.


<!-- NAV_FOOTER_START -->

---

## 🔗 Navigation

### Related Roadmap Materials
- 📖 [Week 01 README](../../roadmap/week-01-project-understanding/README.md) — Week overview & objectives
- 📝 [Week 01 Exercises](../../roadmap/week-01-project-understanding/exercises.md) — Practice problems
- 💡 [Week 01 Solutions](../../solutions/week-01/README.md) — Reference solutions
- 🏠 [Learning Path](../../LEARNING_PATH.md) — Full course overview

### Week Progression

| ← Previous | 🏠 | Next → |
|:-----------|:--:|-------:|
| *(First week)* | [Notebooks Index](../README.md) | [Week 02 Notebooks ➡](../week-02/README.md) |

---
