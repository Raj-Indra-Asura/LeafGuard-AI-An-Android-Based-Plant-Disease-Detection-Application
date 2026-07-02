# Notebook Setup Guide (Beginner Friendly)

This guide shows you, step by step, how to install Python and Jupyter and open
one of the LeafGuard AI week notebooks.

> **What is a Jupyter notebook?** It is an interactive document that mixes
> readable text with runnable Python code. You run one "cell" at a time and see
> the result right below it. The files end in `.ipynb`.
>
> **Reminder:** These notebooks are **companions** to the Android Studio project
> (Kotlin is the default track, with a Java twin in `android-app/`). They explain
> and simulate ideas in Python — they do not replace the real Android app.

## 1. Install Python 3.10 or newer

1. Go to <https://www.python.org/downloads/> and download the latest Python 3.
2. Run the installer.
   - **Windows:** on the first screen, tick **"Add python.exe to PATH"** before
     clicking Install. This lets you run `python` from any terminal.
   - **macOS/Linux:** Python may already be installed; you can still install the
     latest version from the link above.

## 2. Verify the install

Open a terminal (Command Prompt / PowerShell on Windows, Terminal on macOS/Linux)
and run:

- **Windows:**
  ```bash
  python --version
  ```
- **macOS/Linux:**
  ```bash
  python3 --version
  ```

You should see something like `Python 3.10.x` or higher. If you get "command not
found", re-run the installer and make sure PATH is enabled (Windows).

## 3. Install Jupyter

Run this one command (it downloads and installs the notebook software):

```bash
pip install jupyter
```

(On macOS/Linux, use `pip3 install jupyter` if `pip` is not found.)

## 4. Launch Jupyter

From the terminal, move into the notebooks folder and start Jupyter:

```bash
cd notebooks
jupyter notebook
```

- `cd notebooks` moves you into this folder.
- `jupyter notebook` starts the notebook server and opens your web browser.

**What you should see:** a new browser tab opens showing a file list with the
`week-01` … `week-12` folders. (If it does not open automatically, copy the
`http://localhost:8888/...` link the terminal prints into your browser.)

## 5. Open a week's notebook

1. Click a week folder, e.g. `week-01`.
2. Click the `.ipynb` file, e.g. `01-project-understanding-and-architecture.ipynb`.
3. The notebook opens. Click a cell, then press **Shift + Enter** to run it and
   move to the next cell.

## Common problems and friendly fixes

- **"python is not recognized" (Windows):** PATH was not enabled. Re-run the
  installer, tick **"Add python.exe to PATH"**, and reopen the terminal.
- **"pip is not recognized":** try `python -m pip install jupyter` (Windows) or
  `python3 -m pip install jupyter` (macOS/Linux).
- **"jupyter: command not found":** try `python -m notebook` instead of
  `jupyter notebook`.
- **Browser tab did not open:** copy the `http://localhost:8888/...` link from the
  terminal into your browser manually.
- **Want zero install?** Upload a notebook to [Google Colab](https://colab.research.google.com/)
  and run it in the browser — no local setup needed.

---

Back to the [notebooks README](README.md).
