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
- [ ] **Package name is com.leafguard**
- [ ] **Minimum SDK set to API 24** (Android 7.0)
- [ ] **Target SDK set to 34** (Android 14)
- [ ] **Compile SDK set to 34**
- [ ] **Project builds without errors**
- [ ] **Package structure created** with:
  - [ ] adapters/
  - [ ] models/
  - [ ] viewmodels/
  - [ ] repositories/
  - [ ] database/
  - [ ] network/
  - [ ] utils/
- [ ] **Activity classes live under com.leafguard**
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
  - [ ] AndroidX AppCompat
  - [ ] AndroidX ConstraintLayout
  - [ ] Material Components
  - [ ] Room
  - [ ] Retrofit / OkHttp
- [ ] **ViewBinding enabled** in buildFeatures
- [ ] **Kotlin/Java target version matches app/build.gradle**
- [ ] **Gradle sync successful** after all changes
- [ ] **No dependency resolution errors**

---

## Resources Created

### strings.xml
- [ ] **strings.xml contains all required strings**
- [ ] **No hardcoded strings in layouts** (all use @string/)
- [ ] **No hardcoded strings in Kotlin/Java code** (except log tags)
- [ ] **Minimum 20 string resources defined**
- [ ] **Strings include**:
  - [ ] app_name
  - [ ] home_dashboard_title, quick_scan_title, start_scanning
  - [ ] nav_home, nav_scan, nav_analytics, nav_library, nav_about
  - [ ] scan_screen_title, tap_to_upload, choose_image_source_camera, choose_image_source_gallery
  - [ ] result_screen_title, confidence_label, save_to_history
  - [ ] history_screen_title, history_empty_state
  - [ ] settings_screen_title, settings_backend_section, settings_confidence_section

### colors.xml
- [ ] **colors.xml contains color scheme**
- [ ] **Primary color defined** (green theme)
- [ ] **Primary dark defined**
- [ ] **Accent color defined**
- [ ] **Background colors defined**
- [ ] **Text colors defined** (primary, secondary, hint)
- [ ] **Status colors defined** (success, error, warning)
- [ ] **All layouts use @color/** references (no hardcoded hex colors)

### themes.xml and Drawables
- [ ] **themes.xml contains Theme.LeafGuardAI**
- [ ] **Theme colors reference colors.xml**
- [ ] **bottom_nav_menu.xml exists**
- [ ] **5 navigation icon drawables exist** (`ic_nav_home`, `ic_nav_scan`, `ic_nav_analytics`, `ic_nav_library`, `ic_nav_about`)
- [ ] **bg_dashed_upload.xml exists** for the Scan upload area
- [ ] **bg_feature_row.xml exists** for the Home technical feature rows

---

## Activities Created

### Activity Classes
- [ ] **MainActivity exists** (Home dashboard)
- [ ] **ScanActivity exists** (camera/gallery upload and detection)
- [ ] **AnalyticsActivity exists** (placeholder tab)
- [ ] **DiseaseLibraryActivity exists** (Library tab)
- [ ] **SettingsActivity exists** (About tab)
- [ ] **ResultActivity exists**
- [ ] **HistoryActivity exists**
- [ ] **HistoryDetailActivity exists**
- [ ] **All activities extend AppCompatActivity**
- [ ] **All activities have onCreate method**
- [ ] **All activities call setContentView** with correct layout
- [ ] **No compilation errors in any activity**

### AndroidManifest.xml
- [ ] **All 8 activities declared** in manifest
- [ ] **MainActivity has intent-filter** with MAIN action and LAUNCHER category
- [ ] **MainActivity has exported="true"**
- [ ] **All other activities have exported="false"**
- [ ] **Activity text labels reference strings.xml where applicable**
- [ ] **App icon configured**
- [ ] **App name references @string/app_name**
- [ ] **No manifest merger errors**

---

## Layouts Created

### Layout Files
- [ ] **activity_main.xml exists** and renders correctly
- [ ] **activity_scan.xml exists** and renders correctly
- [ ] **activity_analytics.xml exists** and renders correctly
- [ ] **activity_disease_library.xml exists** and renders correctly
- [ ] **activity_result.xml exists** and renders correctly
- [ ] **activity_history.xml exists** and renders correctly
- [ ] **activity_history_detail.xml exists** and renders correctly
- [ ] **activity_settings.xml exists** and renders correctly
- [ ] **Tab layouts use ConstraintLayout as root**
- [ ] **Nested LinearLayouts are used only where they simplify readable card content**
- [ ] **All layouts render without errors** in Design view

### MainActivity Layout (Home)
- [ ] **LeafGuard AI title displayed**
- [ ] **Quick Scan card displayed**
- [ ] **Start Scanning button present**
- [ ] **History and Library summary cards present**
- [ ] **Technical Features card present**
- [ ] **Bottom navigation bar present**

### ScanActivity Layout
- [ ] **Title TextView displayed** (Scan Leaf)
- [ ] **Dashed upload area present**
- [ ] **Upload placeholder text present** (Tap to upload image)
- [ ] **Cloud/Offline toggle appears after image selection**
- [ ] **Detect Disease button appears after image selection**
- [ ] **Bottom navigation bar present**

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
- [ ] **Backend URL field** present
- [ ] **Confidence threshold slider** present
- [ ] **Version TextView** present at bottom
- [ ] **Bottom navigation bar present**

---

## Navigation Implementation

### MainActivity Navigation
- [ ] **Start Scanning button navigates** to ScanActivity
- [ ] **History card navigates** to HistoryActivity
- [ ] **Library card navigates** to DiseaseLibraryActivity
- [ ] **Click listeners implemented** for all tappable dashboard elements

### ScanActivity Navigation
- [ ] **Upload area opens camera/gallery chooser**
- [ ] **Camera and gallery permission requests are handled**
- [ ] **Detect Disease navigates** to ResultActivity with prediction extras
- [ ] **Data passed with Intent extras** (disease name, confidence, image URI)

### ResultActivity Navigation
- [ ] **Data received from Intent** correctly
- [ ] **Disease name displayed** from Intent extra
- [ ] **Confidence displayed** from Intent extra
- [ ] **Save button navigates** to HistoryActivity
- [ ] **Scan another button navigates** to MainActivity
- [ ] **Back to Home action returns to MainActivity**

### Back Button Behavior
- [ ] **Back button works** from ScanActivity → previous screen or exits current tab
- [ ] **Back button works** from ResultActivity → ScanActivity or Home flow
- [ ] **Back button works** from HistoryActivity → MainActivity
- [ ] **Back button works** from SettingsActivity → previous screen or exits current tab
- [ ] **Back button from MainActivity closes app**

---

## Lifecycle Implementation

- [ ] **MainActivity initializes UI in onCreate**
- [ ] **MainActivity refreshes history count in onResume**
- [ ] **Activity bindings are cleared in onDestroy where used**
- [ ] **Lifecycle methods call super** before custom code when overridden
- [ ] **Logcat shows no lifecycle-related crashes** when testing
- [ ] **Lifecycle observed correctly** on rotation
- [ ] **Lifecycle observed correctly** on home button press

---

## Data Passing

- [ ] **ScanActivity passes disease name** to ResultActivity
- [ ] **ScanActivity passes confidence** to ResultActivity
- [ ] **ScanActivity passes symptoms/treatment/prevention** to ResultActivity
- [ ] **ScanActivity passes image URI** to ResultActivity
- [ ] **ResultActivity retrieves all Intent extras** correctly
- [ ] **ResultActivity displays disease name** from Intent
- [ ] **ResultActivity displays confidence** formatted as percentage
- [ ] **No crashes when extras are null** (default values handled)

---

## Code Quality

- [ ] **No compilation errors**
- [ ] **No build warnings** (or only acceptable ones)
- [ ] **Consistent naming conventions** (camelCase for variables, PascalCase for classes)
- [ ] **All view IDs use clear descriptive names** (buttonStartScanning, cardUploadArea, bottomNavigation)
- [ ] **Logs or user-visible messages exist for important failures** (permissions, camera preparation, network/offline detection)
- [ ] **Comments added** where logic is complex
- [ ] **No unused imports**
- [ ] **No unused variables**
- [ ] **Proper indentation** maintained

---

## Testing Completed

### Emulator Testing
- [ ] **App launches on emulator** without crashes
- [ ] **MainActivity Home dashboard displays correctly**
- [ ] **All dashboard cards/buttons clickable**
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
- [ ] **MainActivity Home dashboard screenshot** saved
- [ ] **ScanActivity upload screen screenshot** saved
- [ ] **AnalyticsActivity placeholder screenshot** saved
- [ ] **DiseaseLibraryActivity screenshot** saved
- [ ] **ResultActivity screenshot** with data displayed
- [ ] **HistoryActivity screenshot** saved
- [ ] **HistoryDetailActivity screenshot** saved
- [ ] **SettingsActivity screenshot** saved
- [ ] **Project structure screenshot** in Android Studio
- [ ] **Logcat lifecycle events screenshot** saved
- [ ] **AVD Manager screenshot** showing emulator configuration

### Video Documentation
- [ ] **Navigation flow video recorded** (30-60 seconds)
- [ ] **Video shows Home → Scan → ResultActivity**
- [ ] **Video shows bottom navigation across all 5 tabs**
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
- [ ] **Add a reusable drawable resource** and use it in a layout
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

## Dashboard UI + Bottom Navigation (see build-task.md Step 11)

- [ ] `res/menu/bottom_nav_menu.xml` and 5 `ic_nav_*` drawables exist
- [ ] Shared `BottomNav` helper (Kotlin `setupBottomNav`, Java `BottomNav.setup`) exists and is reused by every tab — not copy-pasted per screen
- [ ] Home, Scan, Analytics, Library, and About screens all show the bottom navigation bar with the correct tab highlighted
- [ ] Home shows the Quick Scan banner, History card (live scan count), and Library card
- [ ] "Start Scanning" opens `ScanActivity`; capture, mode toggle, and detection still work exactly as before
- [ ] Analytics tab opens a placeholder screen with just the nav bar (no crash)
- [ ] Disease Library search box filters results live and each card shows a severity chip
- [ ] Re-tapping the currently active tab does not restart the screen or crash

---

## Completion Criteria

**You may proceed to Week 03 ONLY when:**

1. **All items in this checklist are verified** (not just checked, but actually verified)
2. **App runs without crashes** on emulator or device
3. **All 8 activities are functional** with navigation
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


<!-- NAV_FOOTER_START -->

---

## 📚 Week 02 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| 2 | [learning-notes.md](learning-notes.md) | Theory & Learning Notes |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| **5** | **validation-checklist.md** ← *You are here* | **Validation & Verification** |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Build Implementation Guide](build-task.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Validation & Verification** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Knowledge Assessment Quiz →](quiz.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 01: Project Understanding](../week-01-project-understanding/README.md) | [Learning Path](../../LEARNING_PATH.md) | [Week 03: Camera & Gallery ➡](../week-03-camera-gallery/README.md) |

---
