# Week 12: Learning Notes - Final Submission

## Week 12 Context

Since you are now working on **Week 12**, the goal is no longer “build one more feature.”
The goal is to transform LeafGuard AI into a **submission-ready academic project**.

This week is about converting your development work into **deliverables** that satisfy CSE 2206 expectations:

1. a working APK
2. a clean GitHub repository
3. a polished final report
4. a clear presentation slide deck
5. a demo video
6. confident viva preparation

Think of Week 12 as the week where you package your learning, not just your code.

---

## Final Deliverables Checklist

By the end of this week, you should have:

- [ ] a tested release APK
- [ ] a signed keystore stored safely
- [ ] a final GitHub repository with documentation
- [ ] a final report in PDF format
- [ ] presentation slides ready for defense
- [ ] a demo video script and recorded video
- [ ] viva preparation answers for major questions
- [ ] screenshots and evidence collected from real app runs

---

## Submission Strategy

Do not leave everything for the last day.
A strong final week follows this order:

1. freeze features
2. clean the project
3. build and test the APK
4. write the report while the implementation is fresh in your mind
5. create slides from the report summary
6. rehearse the demo and viva
7. submit only after one full review pass

### Why feature freeze matters

At the end of a project, new features are dangerous.
Every late feature can introduce bugs, confuse your report, and weaken your confidence during viva.
In Week 12, polish is usually more valuable than one extra unfinished feature.

---

# Part 1 - APK Building Complete Guide

## 1.1 Debug vs Release Builds

You must understand the difference clearly because viva examiners often ask it.

| Topic | Debug Build | Release Build |
|---|---|---|
| Purpose | Development and testing | Final distribution and submission |
| Signing | Usually signed automatically with debug key | Signed with your own keystore |
| Optimization | Minimal | Can be optimized/shrunk |
| Debugging | Easier to inspect | Intended for final use |
| Suitable for teacher submission | Usually no | Yes |

### Simple viva answer

> A debug APK is used while developing the app because it is easy to run and inspect. A release APK is the final signed version prepared for distribution, testing, and project submission.

### What LeafGuard AI should submit

For final evaluation, prepare a **release APK**.
Keep one debug build only for internal testing.

---

## 1.2 Pre-Build Checklist Before Creating the APK

Before you click “Generate Signed APK,” verify the following:

- [ ] app name and icon are correct
- [ ] package name is final
- [ ] camera/gallery/prediction flow works
- [ ] disease library screen works
- [ ] result screen works
- [ ] history screen works if implemented
- [ ] no placeholder text remains on key screens
- [ ] debug-only logs are reduced if necessary
- [ ] no secret keys or private files are committed
- [ ] version name and version code are sensible

### Versioning example

In `build.gradle`, use something like:

```gradle
defaultConfig {
    applicationId "com.leafguardai"
    minSdk 24
    targetSdk 34
    versionCode 1
    versionName "1.0"
}
```

If you create an updated submission after fixing a bug, increase `versionCode`.

---

## 1.3 Step-by-Step: Create a Release APK in Android Studio

### Step 1

Open the project in Android Studio and confirm it builds successfully.
Go to:

```text
Build -> Make Project
```

Fix all errors first.
Do not try to sign a broken project.

### Step 2

Open the signed build wizard:

```text
Build -> Generate Signed Bundle / APK
```

### Step 3

Choose **APK** rather than App Bundle if your course specifically asks for an APK file.
If your teacher accepts both, APK is still simpler for direct installation and demo.

### Step 4

Select or create a keystore.
If you do not already have one, click **Create new...**.

### Step 5

Fill in:

- keystore path
- password
- key alias
- key password
- validity period
- first and last name
- organizational details if required

### Step 6

Choose the **release** build variant.
Enable V1/V2 signing if the wizard offers those options and keep the recommended defaults unless your teacher instructed otherwise.

### Step 7

Click **Finish** and wait for the build to complete.
Android Studio will show a notification with the output location.

---

## 1.4 Creating a Keystore - Detailed Guide with Screenshot Descriptions

A keystore is extremely important.
It is the identity of your release APK.

### What a keystore is

A keystore stores the cryptographic key used to sign your application.
Without a keystore, your release APK is not properly prepared for final distribution.

### Where to store it

Store your keystore in a safe location that you control.
Do **not** upload the keystore to a public repository.
Keep a backup in a secure place such as encrypted personal storage.

### Recommended file naming

```text
leafguard-release-key.jks
```

### Keystore creation form guidance

When the Android Studio dialog opens, fill it like this:

- **Key store path**: choose a safe local path on your own machine
- **Password**: use a strong memorable password
- **Key alias**: `leafguard-release`
- **Key password**: use the same or another strong password
- **Validity (years)**: at least 25 years
- **Certificate info**: your name, university, city, country

### Screenshot description 1

**Screenshot to take:** the “Generate Signed Bundle / APK” dialog.

**What should be visible:**

- APK selected
- “Create new” or chosen keystore path
- release variant option

### Screenshot description 2

**Screenshot to take:** the “Create New Key Store” dialog.

**What should be visible:**

- path field
- alias field
- validity field
- certificate info section

Do **not** show actual passwords in screenshots.

### Screenshot description 3

**Screenshot to take:** the successful build notification showing the APK output folder.

**What should be visible:**

- successful build message
- release APK location

---

## 1.5 Signing Configuration in Gradle (Optional but Good to Understand)

Even if you use the Android Studio wizard, you should understand the logic behind signing.

Example concept only:

```gradle
android {
    signingConfigs {
        release {
            storeFile file("your-keystore-path")
            storePassword "YOUR_PASSWORD"
            keyAlias "leafguard-release"
            keyPassword "YOUR_KEY_PASSWORD"
        }
    }

    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### Very important warning

Do not hardcode real passwords in a public repository.
For academic explanation, it is fine to understand the structure, but for actual submission use local Gradle properties or the Android Studio signing wizard.

---

## 1.6 `proguard-rules.pro` Configuration for LeafGuard AI

Release builds often use R8/ProGuard shrinking.
If you use libraries for ML inference, reflection, Room, or Gson, some rules may be necessary.

Here is a practical starter configuration:

```proguard
# Keep application model classes used directly in app code
-keep class com.leafguardai.model.** { *; }

# Keep Room entities and DAO annotations if Room is used
-keep class androidx.room.** { *; }
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# Keep TensorFlow Lite or ML-related classes if used
-keep class org.tensorflow.lite.** { *; }
-dontwarn org.tensorflow.lite.**

# Keep custom parser and repository classes for clarity during debugging
-keep class com.leafguardai.parser.** { *; }
-keep class com.leafguardai.repository.** { *; }

# Keep activities declared in the manifest
-keep public class * extends androidx.appcompat.app.AppCompatActivity

# If Gson or reflection-based parsing is used elsewhere
-keepattributes Signature
-keepattributes *Annotation*
```

### Why this matters

If the release build removes or renames important classes incorrectly, the app may crash only in release mode.
That is why you must test the signed APK, not only the debug version.

---

## 1.7 Testing the Signed APK on a Real Device

Do not assume the APK works just because Android Studio built it.
Install and test it on a real device.

### Real-device test checklist

- [ ] APK installs successfully
- [ ] app launches from icon
- [ ] camera permission is requested correctly
- [ ] image capture or gallery flow works
- [ ] model prediction runs
- [ ] result screen displays disease details
- [ ] XML disease library loads
- [ ] history screen loads if implemented
- [ ] app does not crash on rotation or background/foreground transitions during normal use

### How to install the APK on a phone

1. transfer the APK via USB, email, messaging, or cloud drive
2. open the APK file on the phone
3. allow installation from your file manager if prompted
4. complete installation
5. run the app immediately and test the core workflow

### Evidence to collect

- screenshot of installed app icon
- screenshot of app running on a real phone
- screenshot of result screen on the real phone
- short video clip of install and launch if possible

---

## 1.8 APK File Size Optimization Tips

A final submission should not be unnecessarily heavy.
Use these practical tips:

- remove unused drawable files
- compress large screenshots stored inside the app if any
- use release build with shrink resources enabled where safe
- avoid duplicate model files
- keep only required language or resource variants if relevant
- do not bundle unnecessary training notebooks or raw datasets into the app module

### Specific advice for LeafGuard AI

The biggest size contributors are often:

- ML model file
- sample disease images
- unused large assets

If your APK becomes large, inspect:

- `assets/`
- `res/drawable/`
- model folder contents

Keep only what the app actually uses.

---

# Part 2 - Final Report Writing Guide

## 2.1 What the Final Report Should Do

Your report is not just a diary.
It is a structured technical document that proves:

- you understand the problem
- you designed a system intentionally
- you implemented the system yourself
- you tested it carefully
- you can reflect on strengths and limitations

A good report helps even if the viva is stressful, because the evaluator can see your reasoning in writing.

---

## 2.2 Suggested Chapter-by-Chapter Structure

### Chapter 1: Title Page and Preliminary Pages

**What to include:** Project title, student name, ID, course code CSE 2206, department, supervisor, submission date, declaration, acknowledgement, and abstract.

**Suggested length:** 300-500 words across all prelim pages excluding title page

### Chapter 2: Introduction

**What to include:** Problem statement, motivation, objective, scope, expected users, and chapter outline.

**Suggested length:** 900-1200 words

### Chapter 3: Literature Review

**What to include:** Existing plant disease detection systems, image classification approaches, mobile agriculture support apps, and research gap.

**Suggested length:** 1200-1600 words

### Chapter 4: Methodology

**What to include:** Dataset, model selection, XML disease library design, app modules, workflow, and development approach.

**Suggested length:** 1200-1600 words

### Chapter 5: System Design

**What to include:** Use case description, architecture, data flow, screen flow, database and XML design.

**Suggested length:** 1200-1600 words

### Chapter 6: Implementation

**What to include:** Important Java classes, RecyclerView, XML parser, Room/SQLite, result integration, and UI details.

**Suggested length:** 1800-2400 words

### Chapter 7: Testing and Evaluation

**What to include:** Test case tables, positive and negative tests, screenshots, performance observations, and limitation notes.

**Suggested length:** 1000-1400 words

### Chapter 8: Conclusion and Future Work

**What to include:** What was achieved, lessons learned, remaining limitations, and future improvement opportunities.

**Suggested length:** 700-1000 words

### Chapter 9: References and Appendix

**What to include:** Citations, screenshots, important code listings, extra tables, and user guide pages.

**Suggested length:** As needed


---

## 2.3 What to Write in the Introduction Chapter

Your introduction should answer five questions clearly:

1. What is the real-world problem?
2. Why does the problem matter?
3. Why is a mobile solution useful?
4. What exactly does LeafGuard AI do?
5. What are the project objectives and scope?

### Suggested introduction structure

#### Paragraph 1 - Background

Explain that plant diseases reduce crop quality and yield.
Farmers and agriculture learners need fast and understandable disease information.
Manual diagnosis may require expert knowledge that is not always available.

#### Paragraph 2 - Motivation for mobile solution

Explain that smartphones are widely available and can support camera-based diagnosis and on-device information access.
This makes Android a practical platform for a lightweight advisory tool.

#### Paragraph 3 - Project overview

Introduce LeafGuard AI as an Android application that captures or selects leaf images, predicts a disease class, and displays details such as symptoms, treatment, prevention, and history.

#### Paragraph 4 - Objectives

Use measurable objectives such as:

- classify plant leaf images into known disease classes
- display result confidence and disease information
- provide an offline XML disease library
- store user scan history
- offer an easy mobile interface

#### Paragraph 5 - Scope and limitation note

State what the app covers and what it does not.
For example, it focuses on leaf-image-based classification for known classes and does not replace expert agricultural diagnosis.

### Sample introduction paragraph

> Agriculture remains a critical sector for food security, yet many plant diseases go untreated or are identified too late because expert support is not always immediately available. With the increasing availability of smartphones, mobile-based image analysis has become a practical approach for assisting early disease recognition. LeafGuard AI is an Android application developed to support plant disease detection from leaf images and to provide readable disease information such as symptoms, treatment guidance, and prevention tips. The application combines mobile UI design, machine learning integration, XML-based structured content, and local storage features within the scope of the CSE 2206 Mobile Application Development course.

---

## 2.4 What to Write in the Literature Review

The literature review should not be a random list of papers.
It should build an argument:

1. researchers already use image classification for plant disease detection
2. mobile and lightweight deployment matter in real use
3. many systems stop at prediction and do not explain the disease well
4. your app addresses usability by combining prediction with an offline disease library and mobile workflow

### Suggested structure

#### Section A - Plant disease detection using images

Summarize that researchers use image datasets and machine learning/deep learning to classify plant diseases from leaf images.

#### Section B - Deep learning and CNN-based classification

Discuss why image-based disease recognition benefits from convolutional neural networks or transfer learning.

#### Section C - Mobile agriculture tools

Explain the importance of delivering detection support through mobile apps rather than only desktop tools or research prototypes.

#### Section D - Research gap

Point out that many demonstrations emphasize classification accuracy but provide less focus on offline explanation, student-friendly mobile design, and integrated result interpretation.

### Suggested papers or paper themes to reference

1. **PlantVillage: A Growing Dataset of Plant Disease Images**
   - Why it is useful: Useful for explaining where the common disease classes and leaf-image benchmarking mindset come from.
   - How to use it in the report: Mention it when describing dataset inspiration and standardized disease classes.

2. **Using Deep Learning for Image-Based Plant Disease Detection**
   - Why it is useful: Supports the argument that CNN-based vision approaches can classify plant diseases from leaf images.
   - How to use it in the report: Use it in the literature review to justify image-classification methodology.

3. **A Review of Deep Learning Applications in Smart Agriculture**
   - Why it is useful: Provides broad background for why AI is increasingly used in crop monitoring and precision agriculture.
   - How to use it in the report: Use it to show that your project fits a recognized research trend.

4. **Transfer Learning for Mobile Plant Disease Classification**
   - Why it is useful: Helps justify lightweight deployment and the mobile-app context for on-device prediction or compact models.
   - How to use it in the report: Use it when discussing why mobile deployment matters.

5. **Survey of Computer Vision Techniques for Plant Disease Recognition**
   - Why it is useful: Gives comparison material for traditional vision methods versus deep learning approaches.
   - How to use it in the report: Use it to build a contrast section in the literature review.

6. **Lightweight CNN or TensorFlow Lite Disease Detection on Edge Devices**
   - Why it is useful: Supports discussion about efficiency, APK size, and deployment constraints on phones.
   - How to use it in the report: Use it when describing implementation trade-offs and future optimization.


### Literature comparison table template

| Paper / System | Main idea | Strength | Limitation | What you learned for LeafGuard AI |
|---|---|---|---|---|
| Paper 1 | CNN disease classification | Good accuracy | Often research-only | Useful for model rationale |
| Paper 2 | Mobile agriculture support | Real-world accessibility | May support fewer classes | Useful for mobile deployment argument |
| Paper 3 | Lightweight inference | Efficient on edge devices | Accuracy trade-off | Useful for APK optimization discussion |

### Warning for report honesty

Do not copy literature-review text directly from papers.
Read, understand, summarize, and compare in your own words.

---

## 2.5 What to Write in the Methodology Chapter

This chapter explains **how** you built the project.
A strong methodology chapter is procedural and clear.

### Suggested subsections

1. development methodology
2. dataset or class-label source
3. model training or integration approach
4. Android module design
5. XML disease library design
6. database/history design
7. testing methodology

### Sample methodology content structure

#### 2.5.1 Development approach

State whether you followed an iterative weekly roadmap.
You can say the project was developed in staged modules covering UI, model integration, XML parsing, data storage, and testing.

#### 2.5.2 Data and labels

Explain that the disease classes are aligned to PlantVillage naming conventions, including healthy and diseased categories.
Mention 38 classes if your project targets the full class structure.

#### 2.5.3 Model integration

Describe how the model or inference module receives an image, preprocesses it, and outputs a predicted label and confidence.
Do not dump huge code; explain the pipeline.

#### 2.5.4 XML disease library methodology

Explain that disease information was stored in `disease_library.xml` inside the app assets and parsed using `XmlPullParser` in Java.
Mention nested tags for symptoms, treatment, and prevention.

#### 2.5.5 Repository pattern

Explain that `DiseaseRepository` abstracts XML access and caches parsed diseases for fast lookup.
This is strong methodology content because it shows design intent.

### Sample methodology paragraph

> The application was implemented using Java in Android Studio following an incremental module-based process. After establishing the user interface and image input workflow, the disease classification component was integrated to produce a predicted PlantVillage label and confidence score. To enrich the result output, an offline XML disease library was designed and stored within the application assets. The XML content was parsed using `XmlPullParser`, converted into `Disease` objects, and cached through a repository layer to avoid repeated parsing. This approach ensured that the app remained functional without network access while keeping the user interface responsive.

---

## 2.6 What to Write in the System Design Chapter

This chapter should show structure, not just description.
Use diagrams, tables, and organized explanations.

### Recommended diagrams to include

1. use case diagram
2. high-level architecture diagram
3. activity/screen navigation diagram
4. data flow diagram
5. database schema diagram if history exists
6. XML structure diagram for disease library

### Architecture diagram description

Your architecture diagram can use three layers:

- **Presentation layer**: MainActivity, ResultActivity, DiseaseLibraryActivity, adapters, layouts
- **Logic / service layer**: classifier controller, repository classes, utility classes
- **Data layer**: model file, XML disease library in assets, Room/SQLite history database

### How to describe the 3-tier architecture in the report

#### Presentation layer

Handles user interaction, navigation, camera/gallery flow, result display, and library browsing.

#### Logic layer

Handles prediction orchestration, XML parsing workflow, repository caching, formatting, and feature coordination.

#### Data layer

Stores static disease content, model files, and scan history records.

### Example architecture explanation sentence

> The system follows a layered architecture to separate concerns. The presentation layer manages screens and user actions, the logic layer coordinates prediction and disease-information retrieval, and the data layer stores static and dynamic content such as the XML disease library and scan history.

### XML design diagram description

If you do not have time for a graphical XML diagram, add a simple tree-style figure in the report:

```text
DiseaseLibrary
 ├── disease (label, image, severity)
 │    ├── commonName
 │    ├── scientificName
 │    ├── plant
 │    ├── category
 │    ├── summary
 │    ├── symptoms
 │    │    ├── item
 │    │    ├── item
 │    ├── treatment
 │    │    ├── item
 │    └── prevention
 │         ├── item
```

That is acceptable and easy to explain.

---

## 2.7 What to Write in the Implementation Chapter

This chapter should explain the most important parts of the codebase.
Do not paste entire files.
Select meaningful code excerpts and explain them.

### Best things to highlight

- activity that starts image capture or gallery selection
- prediction integration method
- result screen binding logic
- `DiseaseXmlParser` event loop
- `DiseaseRepository` cache methods
- RecyclerView adapter for disease library
- Room/SQLite entity and DAO if history is present

### How to describe the code without dumping everything

For each highlighted code snippet, answer:

1. What file is this from?
2. What problem does it solve?
3. Why was this approach chosen?
4. What happens before and after this code runs?

### Sample implementation subsection outline

#### 2.7.1 User input workflow

Explain camera/gallery input and image acquisition.

#### 2.7.2 Prediction pipeline

Explain how the image is passed to the model or API and how the result label/confidence is obtained.

#### 2.7.3 Result integration

Explain how `ResultActivity` shows both prediction output and disease-library data.

#### 2.7.4 XML disease library

Explain the parser, repository, and RecyclerView flow.

#### 2.7.5 Local history module

Explain how scans are stored and later retrieved.

### 3-tier architecture wording for implementation chapter

You can explicitly say:

- **Presentation layer**: activities, XML layouts, adapter classes
- **Business / processing layer**: inference coordination, parser logic, repository pattern
- **Data layer**: assets XML, Room database, model file, preferences

### Sample implementation paragraph

> The XML disease library feature was implemented using three cooperating classes. `DiseaseXmlParser` reads the `disease_library.xml` file from the assets folder using `XmlPullParser` and converts each `<disease>` element into a Java object. `DiseaseRepository` loads the XML data in a background thread, caches the parsed records in memory, and provides both list access and label-based lookup. `DiseaseLibraryActivity` uses a RecyclerView adapter to display the cached records, while `ResultActivity` performs a label lookup after prediction to display symptoms, treatment guidance, and prevention tips.

---

## 2.8 Testing Chapter Guide

The testing chapter should prove that the app was validated intentionally, not casually.

### What to include

- test objective
- testing environment
- device or emulator details
- functional test cases
- negative test cases
- screenshots of important results
- summary of passed and failed tests
- limitation notes

### Sample test environment section

Mention:

- Android Studio version
- emulator configuration or phone model
- Android version tested
- build type tested (debug/release)

### Sample test case table format

| Test ID | Feature | Input / Action | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|
| T01 | App launch | Open app from launcher | Home screen loads | Home screen loads | Pass |
| T02 | Image input | Select image from gallery | Preview appears | Preview appears | Pass |
| T03 | Prediction | Run inference on valid leaf image | Label and confidence appear | Label and confidence appear | Pass |
| T04 | XML lookup | Open result for known label | Disease details appear | Disease details appear | Pass |
| T05 | Missing XML label | Use unknown label in test | Friendly fallback message | Friendly fallback shown | Pass |
| T06 | Disease library | Open library screen | RecyclerView list loads | List loads | Pass |
| T07 | Detail view | Tap one disease item | Detail screen opens | Detail screen opens | Pass |
| T08 | Offline mode | Turn on airplane mode and browse library | Disease info still available | Works offline | Pass |

### Negative tests you should include

- malformed or missing input image
- denied permissions
- missing disease label in XML
- empty search result if search exists
- no network if your project has any online part

### Testing chapter closing paragraph idea

> The testing process showed that the main user workflow of LeafGuard AI functions correctly under normal conditions. The application was able to capture or load images, produce predictions, display disease information through the XML library, and preserve scan history. Release APK testing on a real device further confirmed that the application remained usable outside the development environment.

---

## 2.9 Conclusion and Future Work Guide

### What the conclusion should do

- restate what was built
- summarize major achievements
- connect back to the objectives
- avoid introducing brand-new features here

### Good conclusion points

- Android app for plant disease support was built successfully
- prediction and disease information were integrated
- offline XML disease library improved practical usability
- local history or supporting modules increased completeness
- project met major CSE 2206 learning outcomes

### Future work ideas

- support more real-field images beyond standard datasets
- improve model accuracy with larger and more diverse datasets
- support more crops and disease classes
- add multilingual information for local users
- add cloud synchronization and expert consultation features
- improve recommendation precision with agronomy validation

### Sample conclusion paragraph

> LeafGuard AI demonstrates how Android mobile development can be combined with machine learning and structured local data to create a practical plant disease support tool. The final application allows image-based disease prediction, readable result presentation, an offline XML disease library, and local record management. Although the system has limitations in terms of dataset scope and real-world variability, it successfully meets the core objectives of the project and provides a strong foundation for future improvement.

---

## 2.10 How to Format Code Listings in the Report

Do not paste screenshots of giant files unless your department specifically requires image snippets.
Text-based code listings are usually better for readability.

### Rules for code listings

- use monospaced font
- keep code blocks short and focused
- add a caption such as “Listing 4.2: XML parser start-tag handling”
- explain the purpose below the listing
- highlight only the lines that matter

### Good code listing choices

- a short parser event loop
- repository cache lookup method
- RecyclerView item click handler
- result screen integration snippet

### Bad code listing choices

- full 300-line activity file
- generated boilerplate code with no explanation
- repeated getter/setter blocks

---

## 2.11 Word Count Guidelines Per Chapter

Use the table below as a practical planning tool.

| Chapter | Suggested word range |
|---|---|
| Abstract | 200-300 |
| Introduction | 900-1200 |
| Literature Review | 1200-1600 |
| Methodology | 1200-1600 |
| System Design | 1200-1600 |
| Implementation | 1800-2400 |
| Testing and Evaluation | 1000-1400 |
| Conclusion and Future Work | 700-1000 |

These are guidelines, not laws.
The final total often lands around **20-30 pages** depending on font size, images, and tables.

---

# Part 3 - Presentation / Slides Guide

## 3.1 Presentation Goal

Your presentation should answer one big question:

> Why is LeafGuard AI a meaningful, well-designed mobile application project?

Do not try to show every file.
Show the story, the architecture, the features, and the evidence.

### Golden rule for slides

One slide should communicate one idea clearly.
Too much text weakens the talk.

---

## 3.2 Recommended 15-Slide Structure

### Slide 1 - Title

Project name, student identity, course code, supervisor, and a clean project screenshot.

### Slide 2 - Problem Statement

Why plant disease detection matters and why farmers/students need a mobile solution.

### Slide 3 - Objectives

3 to 5 measurable objectives such as detect disease, show guidance, save history, and work offline.

### Slide 4 - Scope and Users

Who uses LeafGuard AI, what the app covers, and what it does not cover.

### Slide 5 - Existing Works / Literature

2 or 3 concise points comparing your app with earlier research or apps.

### Slide 6 - System Architecture

High-level diagram showing UI, ML inference, XML disease library, and local storage.

### Slide 7 - Dataset and Model

PlantVillage classes, number of classes, model type, and prediction flow.

### Slide 8 - Major Features

Camera/gallery input, result screen, disease library, history, and sharing or notifications if available.

### Slide 9 - XML Disease Library

Explain why XML was used, how parsing works, and how the disease library improves the result screen.

### Slide 10 - Database / History Module

Room or SQLite history storage, scan records, and retrieval flow.

### Slide 11 - UI Screenshots

Home, capture, result, disease library, and history screens with captions.

### Slide 12 - Testing Results

Key test cases, sample outputs, and proof that core flows work.

### Slide 13 - Challenges and Solutions

Mention 3 real implementation challenges and how you solved them.

### Slide 14 - Limitations and Future Work

Be honest and forward-looking.

### Slide 15 - Conclusion and Thank You

Summary, learning outcome, and invitation for viva questions.


---

## 3.3 Slide Design Tips

- use large readable fonts
- keep the same color theme across all slides
- prefer screenshots and diagrams over heavy paragraphs
- use 3 to 5 bullets per slide when possible
- highlight keywords such as **prediction**, **XML disease library**, **offline support**, and **history**

### What not to do

- do not paste full Java files onto slides
- do not use tiny text to “fit everything”
- do not fill all slides with animation effects
- do not rely on internet during the demo if you can avoid it

---

## 3.4 How to Do a Live Demo Safely

A live demo is powerful, but it needs backup planning.

### Safe live demo workflow

1. fully charge the phone or laptop
2. keep the APK already installed
3. prepare 2 or 3 tested images in advance
4. close unnecessary apps and notifications
5. keep airplane mode in mind if you want to prove offline disease library support
6. rehearse the taps in the exact order you will perform them

### Backup plan

Always keep these ready:

- screenshots of each key screen
- a short recorded backup demo video
- one slide that summarizes features if the device fails

### If something goes wrong during live demo

Stay calm and say:

> I also prepared screenshots and a recorded backup demonstration to show the intended workflow clearly.

That sounds professional and prepared.

---

# Part 4 - Demo Video Script

## 4.1 Demo Video Goal

Your demo video should show the core workflow clearly in a short time.
A good range is **5 to 8 minutes** unless your department gave a specific length.

### Suggested video structure

1. intro
2. problem statement
3. architecture summary
4. feature demo
5. technical highlight
6. conclusion

---

## 4.2 Exact Demo Order

Use this sequence to avoid confusion:

1. show the title slide or opening app screen
2. say your name, project title, and course
3. explain the problem in one short sentence
4. open LeafGuard AI home screen
5. show image capture or gallery selection
6. run a prediction
7. show result label and confidence
8. point out disease summary, symptoms, treatment, prevention
9. tap into the disease library
10. show scrolling list in RecyclerView
11. open a disease detail screen
12. show history screen if available
13. mention offline XML support
14. briefly mention architecture and technologies
15. end with conclusion and future work

---

## 4.3 Suggested Voiceover Script

### Opening (20-30 seconds)

> Assalamu Alaikum / Good morning. I am [Your Name], and this is my CSE 2206 project, LeafGuard AI, an Android-based plant disease detection application. The goal of this app is to help users identify likely plant diseases from leaf images and provide understandable disease information directly on a mobile device.

### Problem statement (20-30 seconds)

> Plant diseases can reduce crop health and yield, but early recognition is often difficult without expert support. LeafGuard AI addresses this by combining image-based prediction with an offline disease-information library.

### Feature introduction (20 seconds)

> In this demo, I will show image input, prediction, result explanation, disease-library browsing, and supporting modules such as local history.

### Demo of prediction (45-60 seconds)

> First, I open the image input feature and select a sample leaf image. The app processes the image and returns a predicted disease label with a confidence score. Instead of showing only the raw label, the app also retrieves the matched disease entry and displays a summary, symptoms, treatment guidance, and prevention tips.

### XML disease library explanation (30-45 seconds)

> The disease details are stored in an XML file inside the app assets. This file is parsed using `XmlPullParser` in Java, and the parsed data is cached by a repository layer. As a result, the app can show disease information offline without depending on an internet connection.

### Library and detail demo (30-45 seconds)

> Here I open the disease library screen, which lists disease entries using RecyclerView. When I tap one item, the detail screen shows the full information, including scientific name, symptoms, treatment, and prevention.

### History / additional module demo (20-30 seconds)

> The app can also preserve previous scans locally, allowing the user to review earlier results without repeating the full process.

### Technical highlight (20-30 seconds)

> From a software design perspective, the app uses layered architecture. The user interface is separated from parsing and repository logic, and structured local data is used to support better usability after prediction.

### Conclusion (20-30 seconds)

> In conclusion, LeafGuard AI demonstrates how Android development, machine learning integration, XML parsing, and local data management can be combined to create a practical mobile support tool. Thank you.

---

## 4.4 Technical Tips for Recording the Demo Video

- record in a quiet environment
- keep the phone stable or use screen recording
- increase screen brightness
- disable noisy notifications
- rehearse the exact sequence before the final recording
- speak slowly and clearly
- avoid unnecessary pauses while searching for screens
- keep backup screenshots ready in case of a failed recording

### If using screen recording

- record in portrait if the app is portrait-focused
- use a readable font size on the device
- clean the home screen if it will appear in the recording

### If recording with a second camera

- ensure glare does not hide the screen
- keep the frame steady
- make sure your voice is louder than background noise

---

# Part 5 - Viva Preparation Guide

## 5.1 Viva Preparation Strategy

Viva is not only about memory.
It is about being able to explain your system logically.

### Best viva preparation method

1. practise a 60-second project summary
2. practise a 2-minute architecture explanation
3. practise answers to expected technical questions
4. practise explaining one challenge and one improvement idea
5. rehearse on paper without opening the code

---

## 5.2 Practice Speech: How to Explain the Architecture

Use this as a practice script:

> LeafGuard AI follows a layered Android architecture. The presentation layer contains the activities, XML layouts, and RecyclerView adapter used for user interaction. The logic layer handles prediction coordination, XML parsing, repository caching, and formatting of results. The data layer contains the model resources, the XML disease library in the assets folder, and local history storage. This structure keeps the code organized and helps different parts of the app communicate through clear responsibilities.

Practice saying it naturally.
Do not try to memorize every word exactly.

---

## 5.3 How to Explain Technology Choices

### Why Java?

Because the course implementation path is Java-based and it aligns with the teaching materials and exam expectations.

### Why XML for disease information?

Because XML is a syllabus topic, it supports structured offline content, and Android already uses XML extensively.

### Why RecyclerView?

Because a disease library can contain dozens of records and RecyclerView is efficient for scrollable lists.

### Why repository pattern?

Because it separates data access from UI logic and supports caching.

### Why release APK signing?

Because final submission should represent a real distribution-ready app build.

---

## 5.4 Expected Viva Questions with Model Answers (30)

### Viva Question 1

**Question:** What problem does LeafGuard AI solve?

**Model Answer:** LeafGuard AI helps users identify probable plant diseases from leaf images and immediately provides readable guidance such as symptoms, treatment, prevention, and scan history support.

### Viva Question 2

**Question:** Why did you choose Android for this project?

**Model Answer:** Android is widely available, fits the mobile application course focus, and allows camera integration, offline storage, and on-device user interaction.

### Viva Question 3

**Question:** Why is the project relevant to CSE 2206?

**Model Answer:** It demonstrates Android activities, layouts, permissions, local storage, XML parsing, RecyclerView, testing, and deployment, which align closely with the course syllabus.

### Viva Question 4

**Question:** What dataset or class source inspired your labels?

**Model Answer:** The prediction labels follow the PlantVillage class naming scheme, which gives a structured set of plant and disease categories.

### Viva Question 5

**Question:** Why did you use Java instead of Kotlin?

**Model Answer:** The course roadmap and implementation exercises are designed around Java, so using Java keeps the project aligned with the taught syllabus and viva expectations.

### Viva Question 6

**Question:** How does the prediction flow work at a high level?

**Model Answer:** The user captures or selects a leaf image, the model or inference module predicts a label, the app shows confidence, and then the repository fetches matching disease details for display.

### Viva Question 7

**Question:** What is the role of the XML disease library?

**Model Answer:** It stores structured disease information offline so the app can explain a prediction with symptoms, treatment, and prevention even without internet.

### Viva Question 8

**Question:** Why did you use XmlPullParser?

**Model Answer:** It is lightweight, Android-friendly, memory-efficient, and gives clear control over tag-by-tag parsing in Java.

### Viva Question 9

**Question:** What is the purpose of DiseaseRepository?

**Model Answer:** It abstracts XML access, caches parsed disease objects, and exposes simple lookup methods to Activities.

### Viva Question 10

**Question:** Why is caching important?

**Model Answer:** Caching avoids reparsing the XML file on every screen visit, which improves speed and reduces repeated disk I/O.

### Viva Question 11

**Question:** Why use RecyclerView for the disease library?

**Model Answer:** RecyclerView efficiently displays long lists and reuses row views, making it suitable for dozens of disease records.

### Viva Question 12

**Question:** How do you avoid UI lag while loading disease data?

**Model Answer:** I load XML in a background thread and return results to the main thread only for UI updates.

### Viva Question 13

**Question:** What happens if the predicted disease is missing from the XML file?

**Model Answer:** The app shows graceful fallback text rather than crashing, so the result screen remains usable.

### Viva Question 14

**Question:** How do you store scan history?

**Model Answer:** The app stores scan results locally using Room or SQLite, depending on the module design used in the project.

### Viva Question 15

**Question:** What is your architecture in simple terms?

**Model Answer:** It is a layered architecture with UI screens, repository/business logic, and data sources such as XML assets and local database.

### Viva Question 16

**Question:** Why not put all logic directly inside one activity?

**Model Answer:** That would make the code harder to test, maintain, and explain. Separation of concerns keeps the project cleaner.

### Viva Question 17

**Question:** What Android components did you use?

**Model Answer:** Activities, Intents, RecyclerView, layouts, assets, drawables, optional Room database, and background threading utilities.

### Viva Question 18

**Question:** How did you handle permissions?

**Model Answer:** I requested runtime permissions where needed, especially for camera or storage-related actions, and handled denial gracefully.

### Viva Question 19

**Question:** What testing did you perform?

**Model Answer:** I tested navigation, prediction flow, XML parsing, list display, detail display, error handling, and installation of the APK on a real device.

### Viva Question 20

**Question:** What is the difference between debug and release APK?

**Model Answer:** A debug APK is for development and testing, while a release APK is optimized, signed for distribution, and should represent the final submission build.

### Viva Question 21

**Question:** Why do you need a keystore?

**Model Answer:** A keystore contains the signing key used to create a release APK that Android can identify as your authentic app build.

### Viva Question 22

**Question:** What is ProGuard or R8 used for?

**Model Answer:** It shrinks, optimizes, and can obfuscate release builds to reduce size and make reverse engineering harder.

### Viva Question 23

**Question:** How would you explain your database tables or saved data?

**Model Answer:** I would describe what each entity stores, why the fields are needed, and how the app reads and writes those records during scan history operations.

### Viva Question 24

**Question:** How did you connect the ML output to the rest of the app?

**Model Answer:** The predicted label becomes the bridge key that connects inference results, disease library lookups, and optionally saved history records.

### Viva Question 25

**Question:** What are the limitations of your app?

**Model Answer:** Prediction accuracy depends on image quality and training data, the disease library content still requires careful expansion, and real field conditions may differ from the dataset.

### Viva Question 26

**Question:** What would you improve if you had more time?

**Model Answer:** I would expand the dataset, improve UI polish, add multilingual support, add richer disease images, and strengthen field validation with more real-world samples.

### Viva Question 27

**Question:** Why is offline capability valuable?

**Model Answer:** Users may not always have reliable internet in agricultural settings, so offline disease details and local history increase usefulness.

### Viva Question 28

**Question:** What did you personally learn from this project?

**Model Answer:** I learned how Android modules connect together, how to structure code across layers, and how to turn a technical model output into a complete user-facing solution.

### Viva Question 29

**Question:** How would you defend your technology choices?

**Model Answer:** I would connect each choice to a requirement: Java for course alignment, XML for syllabus and offline structured data, RecyclerView for lists, repository pattern for clean architecture, and release signing for deployment.

### Viva Question 30

**Question:** Why should the evaluator trust that the app is complete?

**Model Answer:** Because I can demonstrate each feature, show the source code organization, present testing evidence, and explain design decisions clearly.


---

## 5.5 How to Handle “What Would You Improve?” Questions

This question is common.
Do not answer “nothing.”
That sounds immature.

Good improvement ideas for LeafGuard AI:

- increase dataset diversity with real field images
- improve disease library completeness for all 38 classes
- add more detailed local-language explanations
- optimize model performance for lower-end phones
- add cloud sync or expert consultation support
- strengthen testing across more phone models

### Good answer structure

1. mention one current limitation honestly
2. explain one practical improvement
3. explain why that improvement matters

Example:

> One limitation is that classification quality depends heavily on image quality and dataset coverage. If I had more time, I would expand training and validation with more real-field images and improve the disease library coverage for all classes. This would improve both prediction reliability and user trust.

---

## 5.6 How to Handle Questions About Weaknesses or Bugs

Never panic and never pretend the app is perfect.
Instead say:

> This is a current limitation, but I handled it by adding fallback logic and documenting it in the report.

That shows honesty and engineering maturity.

### Good examples of honest limitation framing

- prediction accuracy may vary with lighting and leaf angle
- XML content still requires full completion for every PlantVillage class
- some recommendations are general guidance, not expert medical/agronomic advice
- more device testing would strengthen reliability claims

---

# Part 6 - GitHub Repository Finalization

## 6.1 Why Repository Quality Matters

Your repository is part of the submission.
It proves your work is organized and reproducible.
A messy repository weakens confidence even if the app works.

---

## 6.2 Commit Message Conventions

Use clear, professional messages.
Good commit messages explain purpose, not just emotion.

### Strong examples

- `feat: integrate XML disease library into result screen`
- `feat: add RecyclerView-based disease library activity`
- `fix: handle missing disease labels with fallback UI`
- `docs: finalize week 12 submission notes`
- `test: add XML parser unit test`

### Weak examples

- `final`
- `done`
- `updated project`
- `new changes`

### Simple rule

Use this format when possible:

```text
type: short description
```

Where `type` can be:

- `feat`
- `fix`
- `docs`
- `test`
- `refactor`
- `chore`

---

## 6.3 README Must-Haves for Final Submission

Your `README.md` should help a teacher or examiner understand the project quickly.
At minimum include these sections:

1. project title
2. short description
3. key features
4. screenshots
5. technology stack
6. project structure
7. setup instructions
8. APK or build instructions
9. known limitations
10. author / course information

### Recommended README structure

```text
# LeafGuard AI
## Overview
## Features
## Technology Stack
## Project Structure
## How to Run
## APK Build Notes
## Screenshots
## Limitations
## Course Context
```

### Screenshot suggestions for README

- home screen
- image input screen
- result screen
- disease library screen
- history screen

---

## 6.4 What to Clean Up Before Submission

Do a cleanup pass before final push.

### Remove or review these items

- temporary test files
- debug-only screenshots not meant for submission
- commented-out dead code blocks
- duplicate assets
- secret keys or tokens
- very large unused files
- meaningless commit history messages if avoidable for future work

### Check these carefully

- `.gitignore` is sensible
- repository root is understandable
- no accidental personal files are included
- report PDF file naming is clean if stored in repo
- final APK naming is clear if stored in repo

---

## 6.5 Suggested Final Repository Checklist

- [ ] README is complete
- [ ] app builds successfully
- [ ] release APK exists
- [ ] screenshots are organized
- [ ] week roadmap evidence is updated
- [ ] no secrets in repository
- [ ] no obviously unused huge files
- [ ] key modules have readable names
- [ ] final commit messages are meaningful

---

# Part 7 - Final Week Action Plan

## 7.1 Suggested Day-by-Day Plan

### Day 1 - Freeze and clean

- stop adding new features
- review open bugs
- clean project folders
- confirm all core flows work

### Day 2 - Build APK

- generate release APK
- test on real device
- fix release-only issues
- collect screenshots

### Day 3 and Day 4 - Write report

- complete Introduction, Literature Review, and Methodology
- then finish System Design, Implementation, and Testing
- insert figures and screenshots
- review formatting

### Day 5 - Prepare slides and demo

- create 15 slides
- rehearse speaking order
- record demo video

### Day 6 - Viva preparation

- practise 30 viva questions
- practise architecture explanation
- rehearse improvement and limitation answers

### Day 7 - Final review and submission

- check filenames
- verify PDF and APK open correctly
- review README and GitHub repository
- submit calmly

---

## 7.2 Final Submission-Day Checklist

- [ ] release APK copied to accessible folder
- [ ] report PDF exported and readable
- [ ] slides file opens correctly
- [ ] demo video file plays correctly
- [ ] GitHub repository link works
- [ ] key screenshots are backed up
- [ ] keystore is stored safely
- [ ] charger and backup device ready if presenting live

---

## 7.3 Common Final-Week Mistakes to Avoid

- building only debug APK and forgetting release APK
- not testing the signed APK on a real phone
- writing report chapters without screenshots or diagrams
- putting too much text on slides
- recording demo without rehearsal
- answering viva from memory only, without understanding
- leaving README incomplete
- submitting with unclean repository contents

---

## Week 12 Takeaway

Week 12 is where you prove that LeafGuard AI is more than a coding exercise.
It is where you present it as a complete engineering project.

A strong Week 12 submission shows four things clearly:

1. the app works
2. you understand how it works
3. you can communicate it professionally
4. you can defend your design decisions confidently

---

## Final Understanding Check

Before submission, answer these in your own words:

1. What is the difference between debug and release APK?
2. Why is a keystore necessary?
3. What are the most important chapters in your report and why?
4. How would you explain the architecture in under 2 minutes?
5. What are your strongest three features to demo?
6. What limitation will you mention honestly if the examiner asks?
7. What will you improve in future work?
8. Is your GitHub repository clean enough for a teacher to inspect quickly?

If you can answer all of these without hesitation, you are ready for final submission.


<!-- NAV_FOOTER_START -->

---

## 📚 Week 12 — Navigation

### All Files In This Week (Complete In Order)

| Step | File | Description |
|------|------|-------------|
| 1 | [README.md](README.md) | Week Overview & Objectives |
| **2** | **learning-notes.md** ← *You are here* | **Theory & Learning Notes** |
| 3 | [exercises.md](exercises.md) | Practice Exercises |
| 4 | [build-task.md](build-task.md) | Build Implementation Guide |
| 5 | [validation-checklist.md](validation-checklist.md) | Validation & Verification |
| 6 | [quiz.md](quiz.md) | Knowledge Assessment Quiz |
| 7 | [reflection.md](reflection.md) | Reflection & Consolidation |

---

### Within-Week Navigation

[← Week Overview & Objectives](README.md) &nbsp;&nbsp;|&nbsp;&nbsp; **Theory & Learning Notes** *(current)* &nbsp;&nbsp;|&nbsp;&nbsp; [Practice Exercises →](exercises.md)

---

### Week Progression

| ← Previous Week | 🏠 Home | Next Week → |
|:----------------|:-------:|------------:|
| [⬅ Week 11: Testing, Debugging & Performance](../week-11-testing-debugging-performance/README.md) | [Learning Path](../../LEARNING_PATH.md) | *(Last week — course complete!)* |

---
