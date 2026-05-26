# Week 02 Validation Checklist

## Purpose

This checklist ensures you have completed all Week 02 requirements before proceeding to Week 03. Each item must be verified and checked. Do not skip items or proceed with incomplete work.

---

## Installation and Setup

- [ ] **Android Studio installed** (version 2022.2.1 or later)
- [ ] **SDK Platform 34 installed** (verified in SDK Manager)
- [ ] **SDK Platform 24 installed** (minimum SDK support)
- [ ] **SDK Build Tools installed** (latest version)
- [ ] **Android Emulator installed** and functional
- [ ] **At least one AVD created** (Pixel 5 or similar, API 30+)
- [ ] **Emulator successfully runs** without errors
- [ ] **Physical device connected** (optional but recommended)
- [ ] **USB debugging enabled** on physical device (if using)
- [ ] **Git installed** and configured

---

## Project Creation and Structure

- [ ] **LeafGuard project created** using Empty Activity template
- [ ] **Package name is com.example.leafguard**
- [ ] **Minimum SDK set to API 24** (Android 7.0)
- [ ] **Target SDK set to 34** (Android 14)
- [ ] **Compile SDK set to 34**
- [ ] **Project builds without errors**
- [ ] **Package structure created** with:
  - [ ] activities/
  - [ ] adapters/
  - [ ] models/
  - [ ] viewmodels/
  - [ ] repositories/
  - [ ] database/
  - [ ] network/
  - [ ] utils/
- [ ] **MainActivity moved to activities package**
- [ ] **All imports updated correctly**
- [ ] **Project syncs successfully after reorganization**

---

## Gradle Configuration

- [ ] **app/build.gradle configured** with correct SDK versions
- [ ] **compileSdk = 34**
- [ ] **minSdk = 24**
- [ ] **targetSdk = 34**
- [ ] **versionCode = 1**
- [ ] **versionName = "1.0.0"**
- [ ] **Dependencies added**:
  - [ ] androidx.appcompat:appcompat:1.6.1
  - [ ] androidx.constraintlayout:constraintlayout:2.1.4
  - [ ] com.google.android.material:material:1.10.0
- [ ] **ViewBinding enabled** in buildFeatures
- [ ] **Java version set to 1.8** in compileOptions
- [ ] **Gradle sync successful** after all changes
- [ ] **No dependency resolution errors**

---

## Resources Created

### strings.xml
- [ ] **strings.xml contains all required strings**
- [ ] **No hardcoded strings in layouts** (all use @string/)
- [ ] **No hardcoded strings in Java code** (except log tags)
- [ ] **Minimum 20 string resources defined**
- [ ] **Strings include**:
  - [ ] app_name
  - [ ] welcome_title, welcome_subtitle
  - [ ] scan_leaf, view_history, settings
  - [ ] select_image_source, camera, gallery
  - [ ] scan_result, disease_detected, confidence_score
  - [ ] scan_history, no_history
  - [ ] app_settings, offline_mode, offline_mode_desc

### colors.xml
- [ ] **colors.xml contains color scheme**
- [ ] **Primary color defined** (green theme)
- [ ] **Primary dark defined**
- [ ] **Accent color defined**
- [ ] **Background colors defined**
- [ ] **Text colors defined** (primary, secondary, hint)
- [ ] **Status colors defined** (success, error, warning)
- [ ] **All layouts use @color/** references (no hardcoded hex colors)

### dimens.xml
- [ ] **dimens.xml contains standard dimensions**
- [ ] **Margins defined** (small, medium, large, xlarge)
- [ ] **Padding defined** (small, medium, large)
- [ ] **Text sizes defined** (caption, body, title, headline, display)
- [ ] **Component sizes defined** (button_height, icon_size, etc.)
- [ ] **All layouts use @dimen/** references (no hardcoded dp values)

---

## Activities Created

### Activity Classes
- [ ] **MainActivity.java exists** in activities package
- [ ] **ScanActivity.java exists** in activities package
- [ ] **ResultActivity.java exists** in activities package
- [ ] **HistoryActivity.java exists** in activities package
- [ ] **SettingsActivity.java exists** in activities package
- [ ] **All activities extend AppCompatActivity**
- [ ] **All activities have onCreate method**
- [ ] **All activities call setContentView** with correct layout
- [ ] **No compilation errors in any activity**

### AndroidManifest.xml
- [ ] **All 5 activities declared** in manifest
- [ ] **MainActivity has intent-filter** with MAIN action and LAUNCHER category
- [ ] **MainActivity has exported="true"**
- [ ] **All other activities have exported="false"**
- [ ] **All activity labels reference strings.xml**
- [ ] **App icon configured** (ic_launcher)
- [ ] **App name references @string/app_name**
- [ ] **No manifest merger errors**

---

## Layouts Created

### Layout Files
- [ ] **activity_main.xml exists** and renders correctly
- [ ] **activity_scan.xml exists** and renders correctly
- [ ] **activity_result.xml exists** and renders correctly
- [ ] **activity_history.xml exists** and renders correctly
- [ ] **activity_settings.xml exists** and renders correctly
- [ ] **All layouts use ConstraintLayout** as root
- [ ] **No LinearLayout nesting** (flat hierarchy)
- [ ] **All layouts render without errors** in Design view

### MainActivity Layout
- [ ] **Title TextView displayed** (welcome_title)
- [ ] **Subtitle TextView displayed** (welcome_subtitle)
- [ ] **Scan button present** with proper constraints
- [ ] **History button present** with proper constraints
- [ ] **Settings button present** with proper constraints
- [ ] **All buttons use Material Button**
- [ ] **Buttons have proper width** (0dp with constraints or match_parent)
- [ ] **Proper margins and padding applied**

### ScanActivity Layout
- [ ] **Title TextView displayed** (select_image_source)
- [ ] **ImageView for preview** present
- [ ] **Camera button present**
- [ ] **Gallery button present**
- [ ] **Layout responsive** to different screen sizes

### ResultActivity Layout
- [ ] **ImageView for leaf image** present
- [ ] **Disease name TextView** present
- [ ] **Confidence TextView** present
- [ ] **Save to history button** present
- [ ] **Scan another button** present
- [ ] **Layout uses ScrollView** (handles small screens)

### HistoryActivity Layout
- [ ] **Title TextView displayed**
- [ ] **Placeholder TextView** for no history message
- [ ] **Space reserved** for RecyclerView (Week 07)

### SettingsActivity Layout
- [ ] **Title TextView displayed**
- [ ] **Offline mode switch** present with label
- [ ] **Clear cache button** present
- [ ] **Version TextView** present at bottom

---

## Navigation Implementation

### MainActivity Navigation
- [ ] **Scan button navigates** to ScanActivity
- [ ] **History button navigates** to HistoryActivity
- [ ] **Settings button navigates** to SettingsActivity
- [ ] **Click listeners implemented** for all buttons
- [ ] **Intents created correctly**
- [ ] **startActivity() called**

### ScanActivity Navigation
- [ ] **Camera button shows Toast** (Week 03 feature)
- [ ] **Gallery button shows Toast** (Week 03 feature)
- [ ] **Camera button navigates** to ResultActivity with dummy data
- [ ] **Gallery button navigates** to ResultActivity with dummy data
- [ ] **Data passed with Intent extras** (source, disease_name, confidence)

### ResultActivity Navigation
- [ ] **Data received from Intent** correctly
- [ ] **Disease name displayed** from Intent extra
- [ ] **Confidence displayed** from Intent extra
- [ ] **Save button navigates** to HistoryActivity
- [ ] **Scan another button navigates** to MainActivity
- [ ] **MainActivity launched with FLAG_ACTIVITY_CLEAR_TOP**

### Back Button Behavior
- [ ] **Back button works** from ScanActivity → MainActivity
- [ ] **Back button works** from ResultActivity → ScanActivity
- [ ] **Back button works** from HistoryActivity → MainActivity
- [ ] **Back button works** from SettingsActivity → MainActivity
- [ ] **Back button from MainActivity closes app**

---

## Lifecycle Implementation

- [ ] **MainActivity has onCreate** with Log statement
- [ ] **MainActivity has onStart** with Log statement
- [ ] **MainActivity has onResume** with Log statement
- [ ] **MainActivity has onPause** with Log statement
- [ ] **MainActivity has onStop** with Log statement
- [ ] **MainActivity has onDestroy** with Log statement
- [ ] **Lifecycle methods call super** before custom code
- [ ] **Logcat shows lifecycle events** when testing
- [ ] **Lifecycle observed correctly** on rotation
- [ ] **Lifecycle observed correctly** on home button press

---

## Data Passing

- [ ] **ScanActivity passes "source"** to ResultActivity
- [ ] **ScanActivity passes "disease_name"** to ResultActivity
- [ ] **ScanActivity passes "confidence"** to ResultActivity
- [ ] **ResultActivity retrieves all Intent extras** correctly
- [ ] **ResultActivity displays disease name** from Intent
- [ ] **ResultActivity displays confidence** formatted as percentage
- [ ] **No crashes when extras are null** (default values handled)

---

## Code Quality

- [ ] **No compilation errors**
- [ ] **No build warnings** (or only acceptable ones)
- [ ] **Consistent naming conventions** (camelCase for variables, PascalCase for classes)
- [ ] **All view IDs follow convention** (btnScan, tvTitle, ivPreview)
- [ ] **Log tags defined as constants** (private static final String TAG)
- [ ] **Log statements present** for debugging key events
- [ ] **Comments added** where logic is complex
- [ ] **No unused imports**
- [ ] **No unused variables**
- [ ] **Proper indentation** maintained

---

## Testing Completed

### Emulator Testing
- [ ] **App launches on emulator** without crashes
- [ ] **MainActivity displays correctly**
- [ ] **All buttons clickable**
- [ ] **Navigation to ScanActivity works**
- [ ] **Navigation to HistoryActivity works**
- [ ] **Navigation to SettingsActivity works**
- [ ] **Navigation to ResultActivity works**
- [ ] **Back button works correctly**
- [ ] **Screen rotation tested** (portrait → landscape → portrait)
- [ ] **Activity recreates correctly** on rotation

### Physical Device Testing (Optional but Recommended)
- [ ] **App installs on physical device**
- [ ] **App launches without crashes**
- [ ] **All navigation works**
- [ ] **UI displays correctly** on device screen size
- [ ] **Performance is acceptable** (no lag)

### Edge Case Testing
- [ ] **Rapid button clicks handled** (no duplicate activities)
- [ ] **Back button spamming handled**
- [ ] **Home button press and return tested**
- [ ] **App switcher (Recent apps) tested**
- [ ] **Low memory scenarios considered** (activity recreation)

---

## Evidence Collection

### Screenshots Saved
- [ ] **MainActivity screenshot** saved
- [ ] **ScanActivity screenshot** saved
- [ ] **ResultActivity screenshot** with data displayed
- [ ] **HistoryActivity screenshot** saved
- [ ] **SettingsActivity screenshot** saved
- [ ] **Project structure screenshot** in Android Studio
- [ ] **Logcat lifecycle events screenshot** saved
- [ ] **AVD Manager screenshot** showing emulator configuration

### Video Documentation
- [ ] **Navigation flow video recorded** (30-60 seconds)
- [ ] **Video shows MainActivity → ScanActivity → ResultActivity**
- [ ] **Video shows back button behavior**
- [ ] **Video quality is clear** and viewable

### APK Generation
- [ ] **Debug APK generated** (Build → Build Bundle(s) / APK(s) → Build APK(s))
- [ ] **APK file saved** to evidence/week-02/
- [ ] **APK installs successfully** on emulator or device
- [ ] **APK filename includes date** (e.g., leafguard-debug-2024-01-15.apk)

---

## Git Repository

- [ ] **Git repository initialized** in project root
- [ ] **.gitignore configured** for Android:
  - [ ] *.iml
  - [ ] .gradle/
  - [ ] local.properties
  - [ ] .idea/
  - [ ] build/
  - [ ] captures/
  - [ ] .DS_Store
- [ ] **Minimum 10 commits made** showing progressive work
- [ ] **Commit messages follow convention**: "Week 02: [Description]"
- [ ] **Commits are logical units** (not "fixed stuff" or "update")
- [ ] **No build files committed** (build/, .gradle/ excluded)
- [ ] **No IDE files committed** (.idea/ excluded)
- [ ] **Git log saved** to evidence folder

---

## Documentation

- [ ] **Learning notes completed** (learning-notes.md)
- [ ] **Exercises attempted** (minimum 6 out of 8)
- [ ] **Build task completed** (build-task.md checklist)
- [ ] **Reflection submitted** (reflection.md)
- [ ] **Quiz attempted** (quiz.md)
- [ ] **Quiz score ≥ 8/10** (minimum passing score)
- [ ] **All markdown files formatted correctly**
- [ ] **All code snippets syntax-highlighted**

---

## Understanding Verification

### Can You Explain Without Notes:
- [ ] **Activity lifecycle** from creation to destruction
- [ ] **What onCreate does** and when it is called
- [ ] **What onDestroy does** and when it is called
- [ ] **Configuration change behavior** (rotation)
- [ ] **ConstraintLayout advantages** over LinearLayout
- [ ] **How constraints work** in ConstraintLayout
- [ ] **Intent purpose** and how to create one
- [ ] **How to pass data** with Intent extras
- [ ] **How to retrieve data** from Intent
- [ ] **Difference between compileSdk, minSdk, targetSdk**
- [ ] **Purpose of AndroidManifest.xml**
- [ ] **Why resources should be externalized**
- [ ] **How Gradle manages dependencies**
- [ ] **Purpose of each package** in project structure

### Can You Implement Without Tutorial:
- [ ] **Create a new activity** from scratch
- [ ] **Design a ConstraintLayout** with multiple views
- [ ] **Navigate between activities** with Intent
- [ ] **Pass multiple data types** with Intent extras
- [ ] **Add lifecycle methods** and log events
- [ ] **Externalize hardcoded strings** to strings.xml
- [ ] **Add a new color** to colors.xml and use it
- [ ] **Add a new dimension** to dimens.xml and use it
- [ ] **Read Gradle file** and understand each section
- [ ] **Debug using Logcat** and find specific messages

---

## Final Checks

- [ ] **All validation items above are checked**
- [ ] **No item skipped or ignored**
- [ ] **All build errors resolved**
- [ ] **All runtime crashes fixed**
- [ ] **Evidence package is complete**
- [ ] **Ready to demonstrate** to instructor if asked
- [ ] **Confident to proceed** to Week 03
- [ ] **Week 02 marked complete** in timeline document

---

## Completion Criteria

**You may proceed to Week 03 ONLY when:**

1. **All items in this checklist are verified** (not just checked, but actually verified)
2. **App runs without crashes** on emulator or device
3. **All 5 activities are functional** with navigation
4. **Quiz score is 8/10 or higher**
5. **Build task is 100% complete**
6. **Evidence package is submitted** to evidence/week-02/
7. **You can explain concepts** without looking at notes
8. **You feel confident** with Android Studio, Activities, Layouts, and Intents

**If any item is not complete, do not proceed. Go back and complete it. Week 03 builds on Week 02 foundations. Weak foundations cause failures later.**

---

## Submission

When all items are checked:

1. Create file: `evidence/week-02/VALIDATION-COMPLETE.txt`
2. Add date and your name
3. Commit to Git: `Week 02: Validation checklist complete`
4. Mark Week 02 as complete in your timeline document
5. Proceed to Week 03 with confidence

**Congratulations on completing Week 02! You now have a solid Android foundation.**
