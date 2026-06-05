# Git and GitHub Workflow Guide

This guide covers everything you need to use Git for the LeafGuard AI project — from your first clone to managing weekly progress commits.  
No prior Git experience is assumed.

---

## What Is Git?

Git is a **version control system**. It records every change you make to your code so you can:
- See what you changed and when.
- Go back to an earlier version if something breaks.
- Work on a new feature without breaking the working code.
- Show your teacher your week-by-week progress.

GitHub is a website that stores your Git repository online so it can be shared and backed up.

---

## Core Concepts

| Term | Plain meaning |
|------|---------------|
| **Repository (repo)** | The folder that Git tracks — all your project files |
| **Commit** | A saved snapshot of your current changes, with a message explaining what changed |
| **Branch** | A separate copy of the code you can edit without touching the main version |
| **Push** | Upload your local commits to GitHub |
| **Pull** | Download commits from GitHub to your local machine |
| **Clone** | Download an entire repository for the first time |
| **Status** | A summary of which files are changed, staged, or untracked |

---

## One-Time Setup

### 1. Configure your identity

Git records your name and email on every commit.

```bash
git config --global user.name "Your Full Name"
git config --global user.email "you@example.com"
```

Verify:
```bash
git config --global --list
```

### 2. Set the default branch name

```bash
git config --global init.defaultBranch main
```

---

## Clone the Repository

```bash
git clone https://github.com/Raj-Indra-Asura/LeafGuard-AI-An-Android-Based-Plant-Disease-Detection-Application.git
cd LeafGuard-AI-An-Android-Based-Plant-Disease-Detection-Application
```

You now have a local copy. The original GitHub repository is called **origin**.

---

## Daily Workflow

These four commands cover almost everything you need:

```bash
git status      # see what has changed
git add .       # stage all changed files for the next commit
git commit -m "Short description of what you did"
git push        # upload commits to GitHub
```

### Step-by-step example: saving Week 02 progress

```bash
# Open your project folder
cd LeafGuard-AI-An-Android-Based-Plant-Disease-Detection-Application

# Check what you have changed
git status

# Stage all changes (or name specific files)
git add .

# Commit with a meaningful message
git commit -m "Week 02: add MainActivity navigation and Activity lifecycle logs"

# Push to GitHub
git push origin main
```

---

## Understanding `git status`

```
On branch main
Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
        modified:   android-app/app/src/main/java/com/leafguard/MainActivity.java

Untracked files:
  (use "git add <file>..." to include in what will be committed)
        android-app/app/src/main/res/layout/activity_scan.xml
```

- **Modified** — file already tracked by Git, but you changed it.
- **Untracked** — new file Git has never seen before.
- **Staged** — file added with `git add`, ready for the next commit.

---

## Writing Good Commit Messages

A commit message is a permanent note to yourself and your teacher.

### Good examples
```
Week 03: implement gallery picker with legacy fallback for API 24–29
Week 04: deploy FastAPI /predict endpoint to Render
Week 07: add Room database history screen with RecyclerView
Fix: prevent crash when camera URI is null on first launch
```

### Bad examples
```
update
done
asdfgh
fix stuff
```

**Rule:** If you cannot explain what you changed in one line, split the work into smaller commits.

---

## Branching Strategy (recommended for LeafGuard AI)

Branches let you work on a feature without breaking working code.

### Create a branch for each week

```bash
# Start Week 03 work on a new branch
git checkout -b week-03-camera-gallery

# ... make changes ...

git add .
git commit -m "Week 03: camera and gallery implementation complete"

# Merge back to main when week is done
git checkout main
git merge week-03-camera-gallery

# Push main to GitHub
git push origin main
```

### View all branches

```bash
git branch          # local branches
git branch -a       # local + remote branches
```

---

## Syncing with GitHub (pull before push)

If you work on more than one machine, always pull before you start working:

```bash
git pull origin main
```

This downloads any new commits from GitHub and merges them into your local copy.  
If there are no conflicts, Git completes the merge automatically.

---

## Undoing Changes

### Discard changes in a file (before committing)

```bash
git checkout -- android-app/app/src/main/java/com/leafguard/MainActivity.java
```

> ⚠️ This permanently discards your changes to that file.

### Unstage a file (after `git add` but before `git commit`)

```bash
git reset HEAD android-app/app/src/main/java/com/leafguard/MainActivity.java
```

### View the history of commits

```bash
git log --oneline
```

Example output:
```
a3f91c2 Week 05: Retrofit call with multipart image upload working
9d4b071 Week 04: FastAPI mock predict endpoint tested with Postman
88eea40 Week 03: camera capture and gallery picker complete
```

### Go back to an earlier commit (read-only)

```bash
git checkout a3f91c2
```

To return to the latest commit:
```bash
git checkout main
```

---

## .gitignore — Files Git Should Ignore

The repository includes a `.gitignore` file that tells Git to skip generated files. You should never commit:

- `android-app/.gradle/`
- `android-app/build/`
- `backend-api/.venv/`
- `backend-api/__pycache__/`
- `model/plant_disease_model.tflite` (binary file, too large)
- `*.keystore` (signing keys — never commit secrets)

If you accidentally stage a file that should be ignored:
```bash
git rm --cached path/to/file
git commit -m "Remove accidentally tracked file"
```

---

## Connecting to GitHub with SSH (optional)

HTTPS cloning asks for your GitHub username and password (or token) on each push.  
SSH is more convenient for frequent use.

### Generate an SSH key

```bash
ssh-keygen -t ed25519 -C "you@example.com"
# Press Enter to accept the default file location
# Set a passphrase (or press Enter for none)
```

### Add the public key to GitHub

```bash
cat ~/.ssh/id_ed25519.pub
```

Copy the output.  
On GitHub: **Settings → SSH and GPG keys → New SSH key** → paste and save.

### Verify

```bash
ssh -T git@github.com
```

Expected: `Hi <username>! You've successfully authenticated`.

---

## Weekly Git Checklist

Use this at the end of each week:

```
[ ] git status shows no unexpected modified files
[ ] All week's changes are committed with a descriptive message
[ ] git push has uploaded commits to GitHub
[ ] GitHub repository shows the new commit in the commit history
[ ] evidence/ screenshots committed alongside code changes
```

---

## Quick Reference Card

```bash
git clone <url>             # Download repo for the first time
git status                  # See what changed
git add .                   # Stage all changes
git add <file>              # Stage one file
git commit -m "message"     # Save a snapshot
git push origin main        # Upload to GitHub
git pull origin main        # Download from GitHub
git log --oneline           # Show commit history
git checkout -b <branch>    # Create and switch to a new branch
git checkout main           # Return to main branch
git merge <branch>          # Merge branch into current branch
git diff                    # Show unstaged line-by-line changes
```

---

**Next:** Once your repo is cloned and Git is working, go to the [Environment Setup Guide](environment-setup.md) to install Android Studio and Python.
