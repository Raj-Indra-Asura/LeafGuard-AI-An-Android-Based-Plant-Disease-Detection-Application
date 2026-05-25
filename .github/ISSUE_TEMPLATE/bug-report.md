---
name: Bug Report
about: Report a bug or issue in LeafGuard AI application
title: '[Week X] [Brief Description of Bug]'
labels: ['bug', 'week-X']
assignees: ''
---

## Bug Information

**Week/Feature**: <!-- e.g., Week 7 - Image Preprocessing, Week 9 - Database -->
**Severity**: <!-- 🔴 Critical / 🟠 High / 🟡 Medium / 🟢 Low -->
**Status**: 🔴 Open / 🟡 In Progress / 🟢 Resolved

**Bug Category**:
- [ ] Crash/App Closure
- [ ] Feature Not Working
- [ ] UI/Display Issue
- [ ] Performance Issue
- [ ] Data Loss/Corruption
- [ ] Model/Inference Issue
- [ ] Build/Compilation Error
- [ ] Other: <!-- Specify -->

---

## Description

### Brief Summary
<!-- One-line description of the bug -->


### Expected Behavior
<!-- What should happen? -->


### Actual Behavior
<!-- What actually happens? -->


### Impact
<!-- How does this affect the app functionality? -->
- **User Impact**: <!-- Can users still use the app? Which features are affected? -->
- **Frequency**: <!-- Does this happen always/sometimes/rarely? -->
- **Workaround Available**: Yes / No
  - If yes: <!-- Describe the workaround -->

---

## Steps to Reproduce

### Reproduction Steps
<!-- Provide detailed steps to reproduce the bug -->
1.
2.
3.
4.
5.

### Reproduction Rate
- [ ] Happens every time (100%)
- [ ] Happens frequently (>50%)
- [ ] Happens sometimes (10-50%)
- [ ] Happens rarely (<10%)
- [ ] Cannot reproduce consistently

### Environment When Bug Occurs
- **Activity/Screen**: <!-- e.g., MainActivity, ResultsActivity -->
- **User Action**: <!-- e.g., Clicking capture button, selecting image -->
- **Data State**: <!-- e.g., Empty database, first launch, after login -->

---

## Logs and Error Messages

### Logcat Output
<!-- Paste relevant Logcat error messages -->
```
[Paste Logcat output here]

Key errors to include:
- Error messages (lines starting with E/)
- Exception stack traces
- Relevant warnings (W/)
```

### Build Errors (If Applicable)
<!-- Paste build error messages if it's a compilation issue -->
```
[Paste build error here]
```

### Console Output
<!-- Any other relevant console output -->
```
[Paste console output here]
```

---

## Device Information

### Test Device 1
- **Device Type**: <!-- Emulator / Physical Device -->
- **Device Model**: <!-- e.g., Pixel 5, Samsung Galaxy S21 -->
- **Android Version**: <!-- e.g., Android 12 (API 31) -->
- **Screen Size**: <!-- e.g., 6.0 inches -->
- **Screen Density**: <!-- e.g., 420 dpi, xxhdpi -->
- **Available RAM**: <!-- e.g., 4GB -->
- **Available Storage**: <!-- e.g., 10GB free -->

**Bug Occurs on This Device**: Yes / No

### Test Device 2 (If Tested)
- **Device Type**: <!-- Emulator / Physical Device -->
- **Device Model**: <!-- e.g., Pixel 3a, OnePlus 9 -->
- **Android Version**: <!-- e.g., Android 11 (API 30) -->
- **Screen Size**:
- **Screen Density**:
- **Available RAM**:
- **Available Storage**:

**Bug Occurs on This Device**: Yes / No

### Device-Specific Pattern
<!-- Does the bug only occur on certain devices? -->
- [ ] Occurs on all devices
- [ ] Occurs only on emulator
- [ ] Occurs only on physical devices
- [ ] Occurs only on specific Android version: <!-- Specify -->
- [ ] Occurs only on specific device model: <!-- Specify -->

---

## Code Context

### Relevant Files
<!-- List the files related to this bug -->
- <!-- e.g., app/src/main/java/com/example/leafguard/MainActivity.kt -->
- <!-- e.g., app/src/main/res/layout/activity_main.xml -->
- <!-- e.g., app/src/main/java/com/example/leafguard/ModelProcessor.kt -->

### Code Snippet (If Applicable)
<!-- Share the specific code section where the bug occurs -->
```kotlin
// File: path/to/file.kt
// Line: XX-YY

[Paste relevant code snippet here]
```

### Recent Changes
<!-- List recent code changes that might be related -->
- **Commit**: <!-- Commit hash or message -->
- **Date**: <!-- When was this code changed -->
- **Changed By**: <!-- Who made the change -->

---

## Screenshots/Videos

### Visual Evidence
<!-- Add screenshots or screen recordings showing the bug -->

**Screenshot 1**: <!-- Describe what it shows -->
<!-- Attach or link to screenshot -->


**Screenshot 2**: <!-- Describe what it shows -->
<!-- Attach or link to screenshot -->


**Video**: <!-- If applicable, link to screen recording -->


### UI State
<!-- If it's a UI bug, describe the visual state -->
- What elements are visible?
- What elements are missing?
- What elements are misaligned?
- Any overlapping components?

---

## Technical Details

### Model-Related (If Applicable)
<!-- Fill this section if bug involves ML model -->
- **Model File**: <!-- e.g., plant_disease_model.tflite -->
- **Model Version**: <!-- e.g., v1.2 -->
- **Inference Time**: <!-- e.g., 150ms -->
- **Input Shape**: <!-- e.g., [1, 224, 224, 3] -->
- **Error in Preprocessing**: Yes / No
- **Error in Inference**: Yes / No
- **Error in Post-processing**: Yes / No

**Model Notes Reference**: See `model/model-notes.md` section <!-- Specify section -->

### Database-Related (If Applicable)
<!-- Fill this section if bug involves database -->
- **Database Version**: <!-- e.g., v3 -->
- **Table Affected**: <!-- e.g., history_table -->
- **Operation**: <!-- e.g., INSERT, SELECT, UPDATE, DELETE -->
- **Data Corruption**: Yes / No
- **Migration Issue**: Yes / No

### Network-Related (If Applicable)
<!-- Fill this section if bug involves network -->
- **Network Available**: Yes / No
- **API Endpoint**: <!-- If applicable -->
- **Response Code**: <!-- e.g., 404, 500 -->
- **Timeout**: Yes / No

### Permission-Related (If Applicable)
<!-- Fill this section if bug involves permissions -->
- **Permission**: <!-- e.g., CAMERA, WRITE_EXTERNAL_STORAGE -->
- **Permission Granted**: Yes / No / Not Requested
- **Permission Dialog Shown**: Yes / No

---

## Debugging Attempted

### What I've Tried
<!-- List the debugging steps you've already attempted -->
1. <!-- e.g., Added Log statements to track variable values -->
2. <!-- e.g., Checked for null pointer exceptions -->
3. <!-- e.g., Verified input data format -->
4. <!-- e.g., Tested with different inputs -->
5. <!-- e.g., Reviewed Week X documentation -->

### Debugging Results
<!-- What did you learn from debugging attempts? -->
-
-
-

### Current Understanding
<!-- What do you think is causing the bug? -->


---

## Week Context

### Validation Checklist
**Reference**: `weeks/week-[X]/validation-checklist.md`

**Which validation item is failing**:
- [ ] <!-- Copy relevant checklist item that's failing -->

### Related Week Tasks
**This bug blocks**:
- [ ] <!-- List tasks that are blocked by this bug -->
- [ ]

**Related Issue**: #<!-- Link to weekly task issue if applicable -->

---

## CSE 2206 Concepts Involved

### Course Concepts Related to Bug
<!-- Which course concepts are involved? -->
- **Concept 1**: <!-- e.g., Activity Lifecycle -->
  - How it relates:
- **Concept 2**: <!-- e.g., Asynchronous Operations -->
  - How it relates:

### Learning Opportunity
<!-- What can be learned from debugging this issue? -->
-
-

---

## Additional Context

### When Did This Start?
- [ ] Always been present
- [ ] After specific commit: <!-- Commit hash -->
- [ ] After Week X completion
- [ ] After adding feature: <!-- Feature name -->
- [ ] After updating dependency: <!-- Dependency name -->

### Related Issues
<!-- Link to any related issues -->
- Related to #
- Possibly caused by #
- Similar to #

### External Resources Consulted
<!-- List resources you've checked -->
- [ ] Android Documentation: <!-- Link -->
- [ ] Stack Overflow: <!-- Link -->
- [ ] Course Materials: <!-- Topic -->
- [ ] Project Documentation: <!-- File -->

---

## Proposed Solution

### Potential Fix
<!-- If you have an idea for how to fix this -->
**Approach**:


**Changes Required**:
1.
2.
3.

**Risks**:
-

**Testing Plan**:
1.
2.
3.

---

## Priority and Assignment

### Priority Justification
<!-- Why is this priority level chosen? -->


### Blocking Issues
**This bug blocks**:
- [ ] Week progression
- [ ] Other features
- [ ] Testing
- [ ] Deployment
- [ ] None (can work around)

### Target Resolution
**Should be fixed by**: <!-- Date or week -->
**Assigned to**: <!-- Name or leave blank -->

---

## Resolution Tracking

### Resolution Status
- [ ] Bug confirmed and reproduced
- [ ] Root cause identified
- [ ] Fix implemented
- [ ] Fix tested on original device
- [ ] Fix tested on multiple devices
- [ ] Regression testing completed
- [ ] Documentation updated
- [ ] Evidence collected

### Resolution Details
<!-- Fill this when bug is resolved -->

**Root Cause**:


**Fix Applied**:


**Commit**: <!-- Commit hash where fix is implemented -->

**Verification**:
- [ ] Bug no longer reproducible
- [ ] No new bugs introduced
- [ ] All validation checklist items pass
- [ ] Performance not negatively impacted

**Resolution Date**: <!-- Date -->

---

## Prevention

### How to Prevent Similar Bugs
<!-- What can be done to prevent similar issues in the future? -->
1.
2.
3.

### Lessons Learned
<!-- What did you learn from this bug? -->
-
-
-

---

## Checklist Before Closing

- [ ] Bug reproduced and confirmed
- [ ] Root cause identified
- [ ] Fix implemented and tested
- [ ] Code committed with clear message
- [ ] Validation checklist updated
- [ ] Progress log updated
- [ ] Evidence collected (screenshots of fix working)
- [ ] No regression in other features
- [ ] Documentation updated if needed

---

**Reported By**: <!-- Your name -->
**Reported On**: <!-- Date -->
**Week**: <!-- Current week number -->
**Course**: CSE 2206 - Mobile Application Development
**Project**: LeafGuard AI - Plant Disease Detection Application

---

## Notes

<!-- Any additional notes, observations, or comments -->
