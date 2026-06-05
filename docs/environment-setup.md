# Environment Setup Guide

This guide walks you through every tool you need to install before you start Week 01 of LeafGuard AI.  
Follow the steps in order. Each section ends with a verification command so you can confirm that the tool is working before moving on.

---

## 1. Install Android Studio

Android Studio is the official IDE for Android development. It bundles the Java compiler, Gradle build system, SDK manager, and emulator.

### Steps

1. Open [https://developer.android.com/studio](https://developer.android.com/studio) in your browser.
2. Click **Download Android Studio** and run the installer for your operating system.
3. During installation, leave all default components selected:
   - Android SDK
   - Android SDK Platform
   - Android Virtual Device
   - Performance (Intel® HAXM or Hyper-V on Windows)
4. Launch Android Studio and click through the Setup Wizard.
5. When the wizard asks you to download SDK components, click **Finish** and wait for the download to complete.

### Verify

Open **Android Studio → SDK Manager** (wrench icon → SDK Manager).  
Confirm that **Android 14 (API 34)** has a checkmark in the **Installed** column.

---

## 2. Set Up the Android SDK

LeafGuard AI targets API 34 and supports down to API 24 (Android 7.0).

### Steps

1. In Android Studio, open **Tools → SDK Manager**.
2. Under the **SDK Platforms** tab, check:
   - Android 14.0 (API 34) — compile target
3. Under the **SDK Tools** tab, check:
   - Android SDK Build-Tools (latest)
   - Android Emulator
   - Android SDK Platform-Tools
4. Click **Apply** and wait for downloads to finish.

### Verify

Open a terminal and run:

```bash
adb version
```

Expected output: `Android Debug Bridge version 1.x.x` (exact version may vary).

---

## 3. Create an Android Virtual Device (Emulator)

You need an emulator to run and test the app without a physical phone.

### Steps

1. In Android Studio, open **Tools → Device Manager**.
2. Click **Create Virtual Device**.
3. Select **Pixel 5** (or Pixel 6) as the hardware profile.
4. Click **Next** and choose a system image:
   - Recommended: **Android 14 (API 34), x86_64**
   - Click the download arrow next to the image if it is not already installed.
5. Click **Finish**.

### Verify

Click the green play button (▶) next to your new virtual device.  
The emulator should boot and show the Android home screen within 1–2 minutes.

> **Tip:** Enable **Hardware acceleration** (Intel HAXM or Windows Hyper-V) for faster emulator startup. The SDK Manager will prompt you to install it during SDK setup.

---

## 4. Install Git

Git tracks your code changes and lets you clone this learning repository.

### Windows

Download the installer from [https://git-scm.com/download/win](https://git-scm.com/download/win) and run it with default options.

### macOS

Run in Terminal:
```bash
xcode-select --install
```
This installs Git as part of the Xcode Command Line Tools.

### Linux (Ubuntu/Debian)

```bash
sudo apt update && sudo apt install git -y
```

### Verify

```bash
git --version
```

Expected output: `git version 2.x.x`.

### Configure Git (required once)

```bash
git config --global user.name "Your Name"
git config --global user.email "you@example.com"
```

---

## 5. Install Python 3.10+

Python is used for the FastAPI backend (Weeks 04–06) and for TFLite model conversion (Week 09).

### Windows

Download from [https://www.python.org/downloads/](https://www.python.org/downloads/).  
During installation, **check the box "Add Python to PATH"**.

### macOS

```bash
brew install python@3.10
```

Or download the macOS installer from [python.org](https://www.python.org/downloads/).

### Linux (Ubuntu/Debian)

```bash
sudo apt update && sudo apt install python3.10 python3.10-venv python3-pip -y
```

### Verify

```bash
python3 --version
pip3 --version
```

Expected: `Python 3.10.x` and `pip 23.x.x` (or later).

---

## 6. Create a Python Virtual Environment

A virtual environment keeps project dependencies separate from your global Python installation.

```bash
# Navigate to the repository root
cd LeafGuard-AI-An-Android-Based-Plant-Disease-Detection-Application

# Create the virtual environment
python3 -m venv .venv

# Activate it
# macOS / Linux:
source .venv/bin/activate
# Windows (Command Prompt):
.venv\Scripts\activate.bat
# Windows (PowerShell):
.venv\Scripts\Activate.ps1
```

### Install backend dependencies

```bash
pip install --upgrade pip
pip install -r backend-api/requirements.txt
```

### Verify

```bash
python -c "import fastapi, numpy, PIL; print('Python environment ready')"
```

Expected output: `Python environment ready`.

---

## 7. Install Postman (optional but recommended)

Postman lets you test the FastAPI `/predict` endpoint directly from a GUI before integrating it with Android.

Download from [https://www.postman.com/downloads/](https://www.postman.com/downloads/).

Alternatively, use `curl` from the terminal — both approaches are shown in the Week 04 notebook.

---

## 8. Configure a Physical Device (optional)

If you prefer testing on a real Android phone instead of an emulator:

1. On your phone: **Settings → About Phone → tap "Build Number" 7 times** to enable Developer Options.
2. Go to **Settings → Developer Options** and enable **USB Debugging**.
3. Connect the phone via USB.
4. In Android Studio, the device should appear in the device dropdown.

### Verify

```bash
adb devices
```

Expected output: your device serial number with `device` status.

---

## 9. Clone the Repository

```bash
git clone https://github.com/Raj-Indra-Asura/LeafGuard-AI-An-Android-Based-Plant-Disease-Detection-Application.git
cd LeafGuard-AI-An-Android-Based-Plant-Disease-Detection-Application
git status
```

Expected: `On branch main, nothing to commit`.

---

## 10. Open the Android Project in Android Studio

1. In Android Studio, choose **File → Open**.
2. Navigate to the cloned repository and select the `android-app/` folder.
3. Click **OK** and wait for Gradle to sync.
4. If Android Studio asks to install a missing Gradle version, click **OK**.

### Verify

Click **Build → Make Project**. The status bar should display `BUILD SUCCESSFUL`.

---

## Quick Environment Checklist

| Tool | Minimum Version | Verify Command |
|------|----------------|----------------|
| Android Studio | Electric Eel 2022.1+ | Help → About |
| Android SDK | API 34 | SDK Manager |
| Java (JDK) | 11 | `java -version` |
| Git | 2.x | `git --version` |
| Python | 3.10 | `python3 --version` |
| pip | 23.x | `pip3 --version` |
| adb | 1.x | `adb version` |

---

## Troubleshooting

### Gradle sync fails with "SDK location not found"

Set `ANDROID_HOME` in your environment:
```bash
# macOS / Linux — add to ~/.bashrc or ~/.zshrc
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$PATH
```

### Emulator is very slow

Enable hardware acceleration:
- **Windows:** Install Intel HAXM from SDK Manager → SDK Tools tab.
- **Windows 11 / Hyper-V:** Enable **Android Emulator Hypervisor Driver for AMD Processors** or use Windows Hypervisor Platform.
- **macOS M1/M2:** The Apple Silicon emulator is fast by default with ARM images.

### `python3` not found on Windows

Use `python` instead of `python3`. Confirm the PATH was set during installation or re-run the Python installer with "Add to PATH" checked.

### Port 8000 already in use (FastAPI)

```bash
# macOS / Linux
lsof -ti:8000 | xargs kill -9
# Windows PowerShell
netstat -ano | findstr :8000
taskkill /PID <PID> /F
```

---

**Once all checks pass, proceed to [Week 01](../roadmap/week-01-project-understanding/README.md).**
