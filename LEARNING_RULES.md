# Learning Rules for LeafGuard AI Project

## Core Philosophy

**You are not building an app by copying code. You are becoming a mobile developer by understanding every line you write.**

This roadmap is designed to transform you from someone who can copy-paste Android code into someone who can **explain, modify, and create** Android applications independently.

## The Five Iron Rules

### Rule 1: Never Copy Code Without Understanding

**What This Means:**
- Before you use any code snippet (even from this roadmap), read it line by line
- Understand what each line does
- Know why it's written that way
- Be able to explain it to someone else

**How to Follow This Rule:**
1. Read the code snippet completely
2. Identify any terms or APIs you don't recognize
3. Look up those terms in GLOSSARY.md or official documentation
4. Rewrite the code yourself (don't copy-paste)
5. Add comments explaining what each part does
6. Test it and verify it works

**Examples:**

**BAD:**
```java
// Student copies this without understanding
Bitmap bitmap = BitmapFactory.decodeFile(filePath);
bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
```

**GOOD:**
```java
// Student writes this after understanding
// Load the image file from storage into memory as a Bitmap object
Bitmap bitmap = BitmapFactory.decodeFile(filePath);

// Resize the bitmap to 224x224 pixels because our ML model expects this exact size
// The 'true' parameter means use bilinear filtering for better quality
bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
```

**Why This Matters:**
- During viva, teachers will ask "Why did you use this approach?"
- You'll need to modify code when requirements change
- Understanding prevents bugs and debugging becomes easier

---

### Rule 2: Explain Before You Code

**What This Means:**
- Before writing any feature, explain your approach in plain English
- Write down what the feature does, how it works, and why you're implementing it this way
- Never jump straight to coding

**How to Follow This Rule:**
1. Read the weekly build task requirement
2. Write a 3-5 sentence explanation in your own words
3. Draw a simple flowchart or diagram if helpful
4. List the classes/files you'll need to create or modify
5. Only then start coding

**Template for Explanation:**
```
Feature: [Name of feature]

What it does: [User-facing description]

How it works technically: [Classes, APIs, data flow]

Why this approach: [Justification of technical choices]

Expected outcome: [What success looks like]
```

**Example:**

Before implementing camera capture in Week 03:

```
Feature: Camera Image Capture

What it does: User taps "Take Photo" button, camera opens, user takes picture,
picture appears in preview area.

How it works technically:
- MainActivity has Button with click listener
- Click launches Intent with ACTION_IMAGE_CAPTURE
- Intent opens device camera app
- Camera app returns image URI via onActivityResult()
- We load URI into ImageView using BitmapFactory

Why this approach:
Using Intent instead of Camera2 API because it's simpler, safer (no permission issues),
and sufficient for our use case. Camera2 would give more control but adds complexity.

Expected outcome:
User sees captured image displayed in app after taking photo.
```

**Why This Matters:**
- Prevents "code first, understand later" bad habits
- Makes debugging easier (you know what should happen)
- Your weekly reflections become meaningful
- Teachers are impressed by clear thinking

---

### Rule 3: Commit Every Week with Meaningful Messages

**What This Means:**
- Make at least 5-10 commits per week
- Each commit message clearly describes what changed
- Commits are logical units of work, not random saves

**How to Follow This Rule:**

**Commit Message Format:**
```
week-XX: [verb] [what you changed]

Examples:
week-02: add MainActivity layout with navigation buttons
week-03: implement camera intent and image preview
week-05: add Retrofit dependency and API interface
week-07: create ScanEntity and ScanDao for Room database
week-11: fix crash when network is unavailable
```

**Commit Frequency:**
- After completing each exercise: 1 commit
- During build task: 3-5 commits (incremental progress)
- After validation passes: 1 commit
- When fixing bugs: 1 commit per bug fixed

**Good Commit Practices:**
- Commit working code (code that compiles)
- Don't commit half-finished features (use branches)
- Don't commit generated files (build/, .gradle/, .idea/)
- Don't commit sensitive data (API keys, passwords)

**Example Weekly Commit History:**
```
week-03: add camera permission to AndroidManifest
week-03: implement camera button click listener
week-03: add onActivityResult to handle camera image
week-03: add gallery picker intent
week-03: implement image preview in ImageView
week-03: add image resize function for ML input size
week-03: fix crash when camera is cancelled
week-03: pass all Week 03 validation checks
```

**Why This Matters:**
- Teachers often check commit history for evidence of consistent work
- Good commits help you track your own progress
- Makes it easy to revert mistakes
- Demonstrates professional development practices

---

### Rule 4: Validate Before Moving to Next Week

**What This Means:**
- Every week has a validation checklist with pass/fail criteria
- You must pass ALL items before proceeding to the next week
- No shortcuts, no "I'll fix it later"

**How to Follow This Rule:**

**Each Week's Validation Checklist is Mandatory:**

1. Open `roadmap/week-XX/validation-checklist.md`
2. Go through each item systematically
3. Test each feature/requirement
4. Mark only PASS or FAIL (no "partially working")
5. If any item fails, debug and fix before proceeding
6. Take screenshots of passing tests
7. Save screenshots in `docs/evidence/week-XX/`

**Validation Mindset:**

**BAD Approach:**
- "It mostly works, good enough"
- "I'll fix that bug next week"
- "The teacher won't notice this small issue"

**GOOD Approach:**
- "Does feature X work exactly as specified? Yes or No?"
- "Can I demonstrate this reliably? Yes or No?"
- "Would this pass if teacher tested it right now? Yes or No?"

**Example - Week 03 Camera Validation:**

```
[ ] PASS: Tapping "Take Photo" opens device camera
[ ] PASS: After taking photo, image appears in app
[ ] PASS: Tapping "Choose from Gallery" opens gallery picker
[ ] PASS: After selecting image, it appears in app
[ ] PASS: App requests camera permission on first launch
[ ] PASS: App handles permission denial gracefully
[ ] PASS: Large images are resized to prevent memory crashes
[ ] PASS: App doesn't crash if user cancels camera/gallery
```

**All must be PASS to proceed to Week 04.**

**Why This Matters:**
- Building on broken features leads to compounding problems
- Week 11 (testing) reveals all accumulated issues at once
- Teachers test your app thoroughly during demo
- Validation discipline builds professional quality standards

---

### Rule 5: Connect Every Feature to Syllabus Topics

**What This Means:**
- For every feature you build, explicitly identify which CSE 2206 syllabus topic(s) it satisfies
- Maintain clear evidence trail from syllabus requirement → your implementation
- Be ready to explain this connection during viva

**How to Follow This Rule:**

**In Your Weekly Reflection:**
```
Week XX Reflection

Feature Implemented: [Name]

Syllabus Topics Satisfied:
1. [Topic from CSE 2206] → [How your feature demonstrates this]
2. [Topic from CSE 2206] → [How your feature demonstrates this]

Evidence:
- Screenshot: [file path]
- Code location: [file path:line number]
- Explanation: [brief description]
```

**Example:**

```
Week 05 Reflection

Feature Implemented: Retrofit HTTP POST Image Upload

Syllabus Topics Satisfied:
1. Network Programming → Used Retrofit library to make HTTP requests
2. HTTP POST → Sent multipart form data with image file
3. JSON Parsing → Parsed response using Gson converter
4. Error Handling → Handled network timeout and no-internet scenarios

Evidence:
- Screenshot: docs/evidence/week-05/retrofit-success.png
- Code location: app/src/main/java/com/example/leafguard/network/ApiService.java:15-30
- Explanation: ApiService interface defines @POST endpoint with @Multipart annotation.
  RetrofitClient configures base URL and Gson converter. MainActivity calls
  uploadImage() and parses JSON response in onResponse callback.
```

**Syllabus Mapping Checklist:**

Use `SYLLABUS_MAPPING.md` as your reference. For each week, check:

- [ ] Which syllabus topics are covered this week?
- [ ] Have I implemented features demonstrating those topics?
- [ ] Can I point to specific code/screenshots as evidence?
- [ ] Can I explain the connection clearly in 2-3 sentences?

**Why This Matters:**
- CSE 2206 grading often includes "syllabus coverage" criteria
- Teachers specifically look for complete topic coverage
- Viva questions often ask "How did you implement [syllabus topic]?"
- Your final report needs this mapping explicitly stated

---

## Additional Learning Guidelines

### Learning Before Implementing

**The Correct Order:**
1. Read weekly README theory section
2. Study learning-notes.md
3. Complete exercises.md (at least 6 exercises)
4. Take quiz.md to test understanding
5. Explain approach for build task in writing
6. Start coding build-task.md
7. Debug and validate
8. Write reflection
9. Save evidence

**Never skip steps 1-5 to jump straight to coding.**

---

### Code Comments Standard

**What to Comment:**
- Why you chose a particular approach
- What a complex section of code does
- Any non-obvious logic or calculations
- Workarounds or known limitations
- Connection to syllabus requirements

**What NOT to Comment:**
- Obvious code (`i++; // increment i`)
- Every single line
- What the language does by default
- Standard Android patterns

**Good Commenting Example:**
```java
// Using Intent instead of Camera2 API because:
// 1. Simpler implementation for CSE 2206 level
// 2. Handles permissions automatically
// 3. Satisfies "multimedia" syllabus requirement
Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
```

---

### Error Handling Discipline

**Every Feature Must Handle:**
1. Null values
2. Empty results
3. Network failures
4. Permission denials
5. User cancellations

**Standard Error Handling Pattern:**
```java
try {
    // Attempt operation
} catch (SpecificException e) {
    // Log error for debugging
    Log.e("TAG", "Error description: " + e.getMessage());

    // Show user-friendly message
    Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();

    // Document in Week 11 debugging log
}
```

---

### Documentation as You Go

**Don't Leave Documentation for Week 12!**

Maintain these documents throughout:

1. **Weekly Reflections** (immediately after completing each week)
2. **Evidence Collection** (screenshots as you build features)
3. **Debug Log** (note all bugs encountered and how you fixed them)
4. **Test Cases** (document expected vs actual results as you test)
5. **Architecture Decisions** (note why you chose certain approaches)

**These accumulated notes become your final report.**

---

### Testing Mindset

**Test Like a Teacher Would Test:**

Teachers will deliberately try to break your app:
- Cancel operations midway
- Deny permissions
- Turn off internet
- Select invalid images
- Rotate screen
- Press back button at wrong times

**You should test these scenarios first** during Week 11.

---

### Asking for Help

**How to Ask Good Questions:**

**BAD Question:**
"My code doesn't work. Help!"

**GOOD Question:**
"In Week 05, I'm implementing Retrofit image upload. When I call api.uploadImage(),
I get error 'java.net.SocketTimeoutException'. I've set timeout to 60 seconds.
My backend is running on 192.168.1.5:8000. Is my base URL format correct?
Should it be 'http://192.168.1.5:8000' or 'http://192.168.1.5:8000/'?"

**Include:**
- What week/feature you're working on
- What you've already tried
- Exact error message
- Relevant code snippet
- Specific question

---

### Time Management

**If You're Falling Behind:**

1. **Prioritize pass/fail features** over polish
2. **Document what doesn't work** rather than skip validation
3. **Adjust optional features** (location, TFLite) to "documented attempts"
4. **Ask for help early**, not the night before submission
5. **Never skip a week** completely - better to have partial implementation than nothing

**Red Flags That You're Behind:**
- Week 6 and you haven't completed Week 3 validation
- Week 10 and your backend doesn't work yet
- Week 11 and you can't demo basic UI navigation
- Week 12 and you haven't written any report sections

**Recovery Strategy:**
- Dedicate full weekend to catching up on one week
- Simplify optional features to "documented future work"
- Focus on demonstrable core features
- Prepare honest viva answers about challenges faced

---

## The Reflection Habit

**Every Week, Write:**

1. **What I learned this week** (concepts, not just features)
2. **What was challenging** (be honest)
3. **How I overcame challenges** (debugging process)
4. **How this connects to CSE 2206 syllabus**
5. **What I'll do differently next week**

**This reflection serves three purposes:**
1. Reinforces learning through articulation
2. Provides content for final report
3. Prepares you for viva questions

---

## Viva Preparation Mindset

**From Week 1, Think:**
"Can I explain this feature to my teacher right now?"

**Practice explaining out loud:**
- Why you chose Java over Kotlin
- How Retrofit works internally
- Why Room is better than raw SQLite
- How TensorFlow Lite differs from cloud inference
- What each syllabus topic means and where you implemented it

**Your viva performance depends on 12 weeks of explaining, not 1 week of cramming.**

---

## Academic Integrity Reminders

### You May Use:
- Official Android documentation
- This roadmap's minimal starter snippets
- Standard libraries (Retrofit, Room, Gson, TFLite)
- Stack Overflow for understanding concepts
- Tutorial videos to learn patterns

### You May NOT:
- Submit someone else's GitHub project
- Copy-paste entire implementations without understanding
- Have someone else write code for you
- Use auto-code generation tools without understanding output
- Plagiarize reports or documentation

### The Test:
**If you can't explain it, you don't own it.**

---

## Success Indicators

**You're Following These Rules Well If:**

- [ ] You can explain any line of code in your project
- [ ] Your commit history shows consistent weekly progress
- [ ] All validation checklists have PASSes (not aspirational)
- [ ] Your reflections are detailed and honest
- [ ] You have screenshots/evidence for every feature
- [ ] You know which syllabus topic each feature satisfies
- [ ] You can demo your app reliably right now

**You're NOT Following These Rules If:**

- [ ] You can't explain why certain code works
- [ ] Large sections of code appeared in one commit
- [ ] Multiple weeks' validation items are incomplete
- [ ] Your reflections are one-sentence summaries
- [ ] You have no evidence screenshots
- [ ] You can't connect features to syllabus topics
- [ ] Your app crashes during self-testing

---

## Final Thoughts

### This is Hard. That's the Point.

CSE 2206 with LeafGuard AI is designed to be **challenging but achievable** with consistent effort.

- You **will** encounter bugs you can't immediately solve
- You **will** feel stuck sometimes
- You **will** need to read documentation multiple times
- You **will** rewrite code that didn't work the first time

**This is all part of becoming a developer.**

### The Reward

By following these learning rules strictly, you will:

1. **Understand mobile development** deeply, not superficially
2. **Build confidence** in your ability to create apps independently
3. **Impress teachers** with clear explanations during viva
4. **Create portfolio work** you can show future employers
5. **Develop professional habits** that serve you throughout your career

### The Choice

You can:
- **Follow these rules** → Struggle initially, grow continuously, succeed confidently
- **Skip these rules** → Code quickly, understand shallowly, fail viva

**Choose wisely. Your future self will thank you.**

---

**Now that you understand the learning rules, read `SYLLABUS_MAPPING.md` next.**
