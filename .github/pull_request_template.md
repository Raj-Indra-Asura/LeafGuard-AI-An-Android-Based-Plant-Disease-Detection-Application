# Pull Request Template

## PR Information

### Week Context
**Week Number**: <!-- e.g., Week 3, Week 7, etc. -->
**Week Focus**: <!-- e.g., UI Development, Model Integration, Camera Feature -->

### Description
<!-- Provide a clear and concise description of what this PR implements -->


### Related Issue
<!-- Link to the related issue(s) -->
Closes #
Related to #

---

## Implementation Checklist

### Build Status
- [ ] Project builds successfully without errors
- [ ] No new build warnings introduced
- [ ] Gradle sync completes without issues
- [ ] APK can be generated and installed

### Week Validation
**Validation Checklist Reference**: `weeks/week-[X]/validation-checklist.md`

**Items Completed from Validation Checklist**:
- [ ] <!-- Copy relevant checklist items from the week's validation-checklist.md -->
- [ ]
- [ ]
- [ ]

**Items Still Pending** (if any):
- [ ]
- [ ]

### Testing Verification
- [ ] Tested on Android Emulator
  - **Emulator**: <!-- e.g., Pixel 5 API 30 -->
  - **Android Version**: <!-- e.g., Android 11 -->
- [ ] Tested on Physical Device
  - **Device**: <!-- e.g., Samsung Galaxy S21 -->
  - **Android Version**: <!-- e.g., Android 12 -->
- [ ] Feature works as expected
- [ ] No crashes during normal usage
- [ ] Error cases handled gracefully
- [ ] UI renders correctly on different screen sizes

### Code Quality
- [ ] Code follows Kotlin/Android naming conventions
- [ ] Comments added for complex logic
- [ ] No hardcoded strings (all text in `strings.xml`)
- [ ] No hardcoded values (constants properly defined)
- [ ] Proper error handling implemented
- [ ] Resource files properly organized
- [ ] No memory leaks introduced
- [ ] Follows Android lifecycle best practices

### Evidence Collection
- [ ] Screenshots saved in `weeks/week-[X]/evidence/`
- [ ] Code snippets documented (if relevant)
- [ ] Progress log updated: `weeks/week-[X]/progress-log.md`
- [ ] Any challenges/learnings documented

---

## Features Implemented

### Week [X] Features
<!-- List the specific features implemented in this PR -->

**Primary Features**:
-
-
-

**Supporting Changes**:
-
-

### CSE 2206 Concepts Applied
<!-- Connect the implementation to course concepts -->
- **Concept 1**: <!-- e.g., Activity Lifecycle Management -->
  - How applied:
- **Concept 2**: <!-- e.g., Intent Handling -->
  - How applied:
- **Concept 3**: <!-- e.g., Database Integration -->
  - How applied:

---

## Commit Quality

### Commit Messages
- [ ] All commits have meaningful messages
- [ ] Commit messages describe WHAT changed and WHY
- [ ] No "WIP" or "fix" as final commit messages
- [ ] Commits are logically organized
- [ ] No unnecessary merge commits

**Commit Summary**:
<!-- List major commits in this PR -->
1.
2.
3.

### Code Changes
**Files Changed**: <!-- Number of files changed -->
**Lines Added**: <!-- Approximate lines added -->
**Lines Deleted**: <!-- Approximate lines deleted -->

**Key Files Modified**:
-
-
-

---

## Testing Details

### Manual Testing Performed
<!-- Describe the manual testing you performed -->

**Test Scenario 1**:
- Steps:
  1.
  2.
  3.
- Expected Result:
- Actual Result:
- Status: ✅ Pass / ❌ Fail

**Test Scenario 2**:
- Steps:
  1.
  2.
  3.
- Expected Result:
- Actual Result:
- Status: ✅ Pass / ❌ Fail

### Edge Cases Tested
- [ ] <!-- e.g., No internet connection -->
- [ ] <!-- e.g., Invalid image input -->
- [ ] <!-- e.g., Low storage space -->
- [ ] <!-- e.g., Permission denied -->

### Known Issues
<!-- List any known issues or limitations -->
-
-

**To be addressed in**: <!-- Future week/PR if applicable -->

---

## Model Integration (If Applicable)

<!-- Fill this section only if PR involves ML model -->

### Model Documentation
- [ ] `model/model-notes.md` is up to date
- [ ] Preprocessing requirements documented
- [ ] Output format documented
- [ ] Model file location specified

### Model Verification
- [ ] Model loads successfully
- [ ] Preprocessing matches documented requirements
- [ ] Inference completes without errors
- [ ] Output format matches documentation
- [ ] Confidence threshold applied correctly

**Model Performance**:
- Average inference time: <!-- e.g., 120ms -->
- Memory usage: <!-- e.g., 45MB -->
- Test device: <!-- e.g., Pixel 5 Emulator -->

---

## UI/UX Changes (If Applicable)

<!-- Fill this section only if PR involves UI changes -->

### Screenshots

**Before**:
<!-- Add screenshot if modifying existing UI -->


**After**:
<!-- Add screenshot showing new UI -->


### UI Elements Added/Modified
-
-
-

### Accessibility Considerations
- [ ] Content descriptions added to images/icons
- [ ] Touch targets are minimum 48dp
- [ ] Color contrast meets accessibility standards
- [ ] Text is scalable

---

## Database Changes (If Applicable)

<!-- Fill this section only if PR involves database changes -->

### Schema Changes
- [ ] New tables created
- [ ] Existing tables modified
- [ ] Migration strategy implemented
- [ ] Database version updated

**Changes**:
-
-

### Data Verification
- [ ] Data saves correctly
- [ ] Data retrieves correctly
- [ ] No data loss on app restart
- [ ] Database queries are efficient

---

## Dependencies

### New Dependencies Added
<!-- List any new dependencies added to build.gradle -->
- [ ] None
- [ ] <!-- e.g., implementation 'androidx.room:room-runtime:2.5.0' -->
- [ ] <!-- e.g., implementation 'org.tensorflow:tensorflow-lite:2.12.0' -->

**Justification for new dependencies**:
-

### Permission Changes
<!-- List any new permissions added to AndroidManifest.xml -->
- [ ] No new permissions
- [ ] <!-- e.g., CAMERA permission -->
- [ ] <!-- e.g., WRITE_EXTERNAL_STORAGE permission -->

**Justification**:
-

---

## Documentation Updates

### Documentation Changed
- [ ] README.md updated (if needed)
- [ ] Week-specific README.md updated
- [ ] Progress log updated
- [ ] Code comments added/updated
- [ ] model/model-notes.md updated (if model-related)

### Links to Updated Documentation
-
-

---

## Reviewer Notes

### Areas Needing Special Attention
<!-- Highlight specific areas you want reviewers to focus on -->
1.
2.
3.

### Questions for Reviewers
1.
2.

### Additional Context
<!-- Any additional context that would help reviewers -->


---

## Pre-Merge Checklist

### Final Verification
- [ ] All validation checklist items completed for this week
- [ ] All tests passing
- [ ] No merge conflicts
- [ ] Branch is up to date with main/master
- [ ] Evidence collected and saved
- [ ] Progress log updated
- [ ] Ready for review

### Post-Merge Tasks
<!-- List any tasks to be done after merging -->
- [ ]
- [ ]
- [ ]

---

## Learning Reflection

### What I Learned
<!-- Reflect on what you learned while implementing these features -->
1.
2.
3.

### Challenges Faced
<!-- Describe challenges and how you overcame them -->
1. **Challenge**:
   - **Solution**:
2. **Challenge**:
   - **Solution**:

### CSE 2206 Concepts Reinforced
<!-- Which course concepts did this implementation reinforce? -->
-
-
-

---

## Additional Notes

<!-- Any other information relevant to this PR -->


---

**Submitted By**: <!-- Your name -->
**Date**: <!-- Submission date -->
**Week Completion**: <!-- e.g., Week 3 Complete, Week 7 In Progress -->
**Course**: CSE 2206 - Mobile Application Development
**Project**: LeafGuard AI - Plant Disease Detection Application
