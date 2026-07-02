# LeafGuard AI - Architecture Diagram Drawing Guide

## Step-by-Step Guide to Creating System Architecture Diagrams

This guide provides detailed instructions on how to draw the LeafGuard AI system architecture diagram showing all components, data flows, and interactions.

---

## DIAGRAM 1: COMPLETE SYSTEM ARCHITECTURE (HIGH-LEVEL)

### Tools Recommended
- **Online:** Draw.io (diagrams.net), Lucidchart, Figma
- **Desktop:** Microsoft Visio, Dia, yEd
- **Code-based:** PlantUML, Mermaid

### Step-by-Step Instructions

#### Step 1: Create Canvas
1. Open your diagramming tool
2. Create new blank diagram
3. Set canvas size to A4 landscape (297mm × 210mm)
4. Enable grid (10mm spacing) for alignment
5. Set default font to Arial or Helvetica, 10pt

#### Step 2: Draw the Android Application Layer (Top)

**Components to include:**

```
┌───────────────────────────────────────────────────────────────┐
│                    ANDROID APPLICATION                         │
│                   (Presentation Layer)                         │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐           │
│  │    Main     │  │   Result    │  │   History   │           │
│  │  Activity   │  │  Activity   │  │  Activity   │           │
│  └─────────────┘  └─────────────┘  └─────────────┘           │
│                                                                │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐           │
│  │HistoryDetail│  │DiseaseLibrary│ │  Settings   │           │
│  │  Activity   │  │  Activity   │  │  Activity   │           │
│  └─────────────┘  └─────────────┘  └─────────────┘           │
│                                                                │
│  ┌─────────────────────────────────────────────┐              │
│  │        RecyclerView Adapters                │              │
│  │  (inner classes of HistoryActivity &        │              │
│  │   DiseaseLibraryActivity)                   │              │
│  └─────────────────────────────────────────────┘              │
└───────────────────────────────────────────────────────────────┘
```

**Drawing Instructions:**
1. Draw large rectangle for Android Application layer
2. Add header with title "ANDROID APPLICATION"
3. Inside, draw 6 smaller rectangles for activities
4. Arrange in 2 rows of 3
5. Label each: MainActivity, ResultActivity, HistoryActivity, HistoryDetailActivity, DiseaseLibraryActivity, SettingsActivity
6. Add one more rectangle at bottom for the RecyclerView adapters (inner classes)
7. Use light blue color for all activity boxes
8. Use dark blue for header

> **Note:** LeafGuard AI has exactly these 6 Activities and uses no Fragments. The list adapters are inner classes of `HistoryActivity` and `DiseaseLibraryActivity`. `MainActivity` handles camera/gallery capture **and** detection directly.

#### Step 3: Draw the Activity Layer (Middle-Upper)

```
┌───────────────────────────────────────────────────────────────┐
│                    ACTIVITY LAYER                              │
│              (UI Controllers — no ViewModels)                  │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐        │
│  │     Main     │  │    Result    │  │   History    │        │
│  │   Activity   │  │   Activity   │  │   Activity   │        │
│  └──────────────┘  └──────────────┘  └──────────────┘        │
│                                                                │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐        │
│  │HistoryDetail │  │DiseaseLibrary│  │   Settings   │        │
│  │   Activity   │  │   Activity   │  │   Activity   │        │
│  └──────────────┘  └──────────────┘  └──────────────┘        │
└───────────────────────────────────────────────────────────────┘
```

**Drawing Instructions:**
1. Draw rectangle below Android layer with small gap
2. Add header "ACTIVITY LAYER"
3. Draw 6 smaller rectangles for the 6 Activities
4. Label: MainActivity, ResultActivity, HistoryActivity, HistoryDetailActivity, DiseaseLibraryActivity, SettingsActivity
5. Note that logic lives directly in the Activities (no ViewModel/LiveData layer)
6. Use light green color for the Activity boxes
7. Add downward arrows from Activities to the Data layer

#### Step 4: Draw the Data Layer (Middle)

```
┌───────────────────────────────────────────────────────────────┐
│                       DATA LAYER                               │
│                  (Persistence & Network)                       │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐        │
│  │  Room        │  │  Retrofit    │  │  TFLite      │        │
│  │  ScanDao /   │  │RetrofitClient│  │ Classifier   │        │
│  │  AppDatabase │  │ / ApiService │  │  (offline)   │        │
│  └──────────────┘  └──────────────┘  └──────────────┘        │
│                                                                │
│         ↓                    ↓                     ↓           │
│  scan_history table    POST /predict         model.tflite      │
│  (leafguard.db)      (multipart `image`)     (on-device ML)    │
└───────────────────────────────────────────────────────────────┘
```

**Drawing Instructions:**
1. Draw rectangle below Activity layer
2. Add header "DATA LAYER"
3. Draw 3 rectangles for the data components
4. Label: Room (ScanDao/AppDatabase), Retrofit (RetrofitClient/ApiService), TFLiteClassifier
5. Use light yellow color
6. Add arrows from Activities to the Data layer
7. Add a small note that Room persists to `leafguard.db` and Retrofit calls `POST /predict`

#### Step 5: Draw Data Sources (Split into Local and Remote)

**Left Side - Local Data:**

```
┌──────────────────────────────────┐
│       LOCAL DATA SOURCE          │
├──────────────────────────────────┤
│                                  │
│  ┌────────────────────────────┐  │
│  │     ROOM DATABASE          │  │
│  ├────────────────────────────┤  │
│  │  • AppDatabase.kt          │  │
│  │  • ScanResultDao.kt        │  │
│  │  • UserDao.kt              │  │
│  │  • Entities                │  │
│  └────────────────────────────┘  │
│                                  │
│  ┌────────────────────────────┐  │
│  │   FILE STORAGE             │  │
│  │  • Internal Storage        │  │
│  │  • Captured Images         │  │
│  └────────────────────────────┘  │
│                                  │
│  ┌────────────────────────────┐  │
│  │   XML PARSER               │  │
│  │  • Disease Info XML        │  │
│  │  • Treatment Data XML      │  │
│  └────────────────────────────┘  │
└──────────────────────────────────┘
```

**Right Side - Remote Data:**

```
┌──────────────────────────────────┐
│      REMOTE DATA SOURCE          │
├──────────────────────────────────┤
│                                  │
│  ┌────────────────────────────┐  │
│  │    RETROFIT CLIENT         │  │
│  ├────────────────────────────┤  │
│  │  • ApiService Interface    │  │
│  │  • OkHttpClient           │  │
│  │  • Gson Converter         │  │
│  │  • Interceptors           │  │
│  └────────────────────────────┘  │
│                                  │
│  ┌────────────────────────────┐  │
│  │    API ENDPOINTS           │  │
│  │  • GET  /                  │  │
│  │  • GET  /health            │  │
│  │  • GET  /diseases          │  │
│  │  • POST /predict           │  │
│  └────────────────────────────┘  │
└──────────────────────────────────┘
```

**Drawing Instructions:**
1. Draw two side-by-side rectangles below Repository layer
2. Left rectangle: "LOCAL DATA SOURCE"
3. Right rectangle: "REMOTE DATA SOURCE"
4. In local: add 3 sub-boxes for Room, File Storage, XML Parser
5. In remote: add 2 sub-boxes for Retrofit and API Endpoints
6. Use light purple for local, light orange for remote
7. Add arrows from Repositories to both data sources

#### Step 6: Draw Backend Layer (Bottom)

```
┌───────────────────────────────────────────────────────────────┐
│                    BACKEND LAYER (FASTAPI)                     │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐        │
│  │  Root/Health │  │  Detection   │  │  Disease     │        │
│  │  Endpoints   │  │  API         │  │  Info API    │        │
│  └──────────────┘  └──────────────┘  └──────────────┘        │
│           ↓                ↓                   ↓              │
│  ┌──────────────────────────────────────────────────┐         │
│  │        BUSINESS LOGIC & ML MODEL                 │         │
│  │  • TensorFlow Model Loading                      │         │
│  │  • Image Preprocessing                           │         │
│  │  • Disease Classification                        │         │
│  │  • Confidence Calculation                        │         │
│  └──────────────────────────────────────────────────┘         │
│           ↓                                                   │
│  ┌──────────────────────────────────────────────────┐         │
│  │      IN-MEMORY / STATIC DISEASE DATA             │         │
│  │  • Disease metadata served by /diseases          │         │
│  │  (no server-side relational database)            │         │
│  └──────────────────────────────────────────────────┘         │
└───────────────────────────────────────────────────────────────┘
```

**Drawing Instructions:**
1. Draw large rectangle at bottom
2. Add header "BACKEND LAYER (FASTAPI)"
3. Draw 3 boxes for API endpoints at top
4. Draw larger box below for business logic
5. Draw box at bottom for PostgreSQL database
6. Use red/pink colors for backend
7. Add upward arrow from Backend to Retrofit (showing API responses)

#### Step 7: Add External Components (Side Boxes)

**Left Side:**

```
┌─────────────────────┐
│   CAMERA HARDWARE   │
├─────────────────────┤
│  • Device Camera    │
│  • Camera2 API      │
│  • Image Capture    │
└─────────────────────┘
        ↓
   [Captured Image]
        ↓
  Image Capture Module
```

**Right Side:**

```
┌─────────────────────┐
│   ML MODEL FILE     │
├─────────────────────┤
│  • disease_model    │
│    .tflite          │
│  • In Assets Folder │
│  • Loaded at        │
│    Runtime          │
└─────────────────────┘
```

**Drawing Instructions:**
1. Draw rectangle on far left for Camera
2. Draw rectangle on far right for ML Model
3. Add arrows showing Camera → App
4. Add note showing TFLite model in assets
5. Use gray color for hardware/external

#### Step 8: Add Data Flow Arrows

**Add the following arrows with labels:**

1. **Camera → MainActivity:** "Image Captured"
2. **MainActivity → TFLiteClassifier:** "Bitmap (offline inference)"
3. **MainActivity → RetrofitClient:** "Multipart Upload (part `image`)"
4. **RetrofitClient → ApiService:** "Multipart Request"
5. **ApiService → Backend API:** "HTTPS POST /predict"
6. **Backend → ML Model:** "Image Tensor"
7. **ML Model → Backend:** "Predictions Array"
8. **Backend → RetrofitClient:** "JSON Response (field `disease`)"
9. **RetrofitClient → MainActivity:** "PredictionResponse"
10. **MainActivity → ResultActivity:** "Disease Result (Intent extras)"
11. **ResultActivity → UI:** "Display disease & confidence"
12. **ResultActivity → ScanDao/AppDatabase:** "Save Scan (scan_history)"

**Arrow Styling:**
- Use solid arrows (→) for data flow
- Use dashed arrows (⇢) for control flow
- Use different colors: Blue for requests, Green for responses
- Add small text labels above each arrow

#### Step 9: Add Legend

```
┌──────────────────────────────────────────────────┐
│                    LEGEND                         │
├──────────────────────────────────────────────────┤
│  ━━━→  Data Flow (Request)                       │
│  ━━━→  Data Flow (Response)                      │
│  ⇢⇢⇢⇢  Control Flow                              │
│  [Blue]  Presentation Layer                      │
│  [Green] Business Logic Layer                    │
│  [Yellow] Data Layer                             │
│  [Purple] Local Storage                          │
│  [Orange] Network                                │
│  [Red]   Backend                                 │
└──────────────────────────────────────────────────┘
```

**Drawing Instructions:**
1. Add legend box in top-right corner or bottom-right
2. List all arrow types and colors
3. Explain color coding

#### Step 10: Add Title and Annotations

1. Add title at very top: "LeafGuard AI - System Architecture Diagram"
2. Add subtitle: "Three-Tier Android Application with RESTful Backend"
3. Add version: "Version 1.0"
4. Add date: "Date: [Current Date]"
5. Add your name: "Developed by: [Your Name]"
6. Add small notes explaining key interactions:
   - "MVVM Pattern for Clean Architecture"
   - "Repository Pattern for Data Abstraction"
   - "Single Source of Truth with Room"
   - "RESTful API Communication"

---

## DIAGRAM 2: DATA FLOW DIAGRAM (DISEASE DETECTION FLOW)

### Purpose
Show the detailed flow of data during disease detection process.

### Step-by-Step Instructions

#### Step 1: Draw Sequence of Steps

```
┌────────────┐    ┌────────────┐    ┌────────────┐    ┌────────────┐
│   User     │    │   Camera   │    │   Scan     │    │    Scan    │
│            │    │            │    │  Activity  │    │  ViewModel │
└─────┬──────┘    └─────┬──────┘    └─────┬──────┘    └─────┬──────┘
      │                 │                  │                  │
      │ 1. Click "Take Photo"             │                  │
      │────────────────────────────────────>                  │
      │                 │                  │                  │
      │ 2. Open Camera  │                  │                  │
      │<────────────────│                  │                  │
      │                 │                  │                  │
      │ 3. Capture Image│                  │                  │
      │─────────────────>                  │                  │
      │                 │                  │                  │
      │                 │ 4. Image File    │                  │
      │                 │──────────────────>                  │
      │                 │                  │                  │
      │                 │                  │ 5. Compress &    │
      │                 │                  │    Upload        │
      │                 │                  │──────────────────>
      │                 │                  │                  │

┌─────┴──────┐    ┌─────┴──────┐    ┌─────┴──────┐    ┌─────┴──────┐
│  Retrofit  │    │  FastAPI   │    │  ML Model  │    │   Room DB  │
│  Client    │    │  Backend   │    │  (TFLite)  │    │            │
└─────┬──────┘    └─────┬──────┘    └─────┬──────┘    └─────┬──────┘
      │                  │                  │                  │
      │ 6. POST /predict  │                  │                  │
      │──────────────────>                  │                  │
      │                  │                  │                  │
      │                  │ 7. Preprocess    │                  │
      │                  │──────────────────>                  │
      │                  │                  │                  │
      │                  │ 8. Inference     │                  │
      │                  │ <Result Array>   │                  │
      │                  │<─────────────────│                  │
      │                  │                  │                  │
      │ 9. JSON Response │                  │                  │
      │<─────────────────│                  │                  │
      │                  │                  │                  │
      │                                     │ 10. Save Result  │
      │─────────────────────────────────────────────────────────>
      │                                     │                  │
      │                                     │ 11. Confirmation │
      │<────────────────────────────────────────────────────────
      │                                     │                  │
```

**Drawing Instructions:**
1. Create 8 vertical swim lanes for actors/components
2. Add lifelines (vertical dashed lines)
3. Draw horizontal arrows for interactions
4. Number each step sequentially
5. Add return arrows with dashed lines
6. Label each arrow with action description

---

## DIAGRAM 3: COMPONENT DIAGRAM

### Purpose
Show relationships between major components of the application.

### Drawing Instructions

```
                    ┌─────────────────────────┐
                    │                         │
                    │    MainActivity         │
                    │  (capture + detect)     │
                    └────────┬────────────────┘
                             │
                ┌────────────┼──────────────┐
                │            │              │
        ┌───────▼────────┐  │   ┌──────────▼──────────┐
        │                │  │   │                     │
        │ ResultActivity │  │   │  HistoryActivity →  │
        │ (share + save) │  │   │  HistoryDetail      │
        └───────┬────────┘  │   └──────────┬──────────┘
                │           │              │
                │  ┌────────▼───────────┐  │
                │  │ DiseaseLibrary /   │  │
                │  │ SettingsActivity   │  │
                │  └────────────────────┘  │
                │                          │
            ┌───▼──────────────────────────▼───┐
            │                                  │
            │   Data layer (no ViewModels)     │
            └────┬──────────────┬─────────┬────┘
                 │              │         │
    ┌────────────▼───┐   ┌──────▼───────┐ ┌▼───────────────┐
    │  AppDatabase / │   │RetrofitClient│ │TFLiteClassifier│
    │  ScanDao       │   │ /ApiService  │ │ <<On-device>>  │
    │  <<Local>>     │   │ <<Remote>>   │ │                │
    └────────────────┘   └──────────────┘ └────────────────┘
```

**Drawing Instructions:**
1. Draw boxes for each major component
2. Use stereotypes in guillemets: <<Local>>, <<Remote>>, <<On-device>>
3. Add dependency arrows (dashed with open arrowhead)
4. Group related components
5. Use composition (filled diamond) for strong relationships
6. Use dependency (dashed arrow) for weak relationships

---

## DIAGRAM 4: ROOM DATABASE SCHEMA

### Drawing Instructions

```
┌─────────────────────────────────────┐
│          AppDatabase                │
│  <<RoomDatabase>>                   │
├─────────────────────────────────────┤
│  + scanResultDao(): ScanResultDao   │
│  + userDao(): UserDao               │
└────────────┬────────────────────────┘
             │
    ┌────────┴─────────┐
    │                  │
┌───▼──────────────┐  ┌▼─────────────────┐
│  ScanResult      │  │      User        │
│  <<Entity>>      │  │   <<Entity>>     │
├──────────────────┤  ├──────────────────┤
│ + id: Int (PK)   │  │ + id: Int (PK)   │
│ + imagePath: Str │  │ + name: String   │
│ + diseaseName    │  │ + email: String  │
│ + confidence     │  │ + passwordHash   │
│ + timestamp      │  │ + createdAt      │
│ + userId (FK)    │  └──────────────────┘
└──────────────────┘
        │
        │ 1:N
        │
┌───────▼──────────────┐
│  ScanResultDao       │
│  <<DAO>>             │
├──────────────────────┤
│ + insert(result)     │
│ + getAll(): List     │
│ + getById(id): Res   │
│ + delete(result)     │
│ + deleteAll()        │
└──────────────────────┘
```

**Drawing Instructions:**
1. Draw UML class diagram style boxes
2. Top section: Class name with stereotype
3. Middle section: Attributes with types
4. Bottom section: Methods/Operations
5. Show relationships with lines and cardinality
6. Use different colors for Entity, DAO, Database

---

## DIAGRAM 5: NETWORK ARCHITECTURE

### Drawing Instructions

```
┌──────────────────────────────────────────────────────────────┐
│                    USER'S ANDROID DEVICE                      │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌────────────────────────────────────────────────────────┐  │
│  │          LeafGuard AI Application                      │  │
│  │                                                         │  │
│  │  ┌──────────────┐        ┌─────────────────┐          │  │
│  │  │ UI Layer     │        │ TFLite Model    │          │  │
│  │  └──────┬───────┘        │ (In Assets)     │          │  │
│  │         │                └─────────────────┘          │  │
│  │  ┌──────▼───────┐                                     │  │
│  │  │ ViewModel    │                                     │  │
│  │  └──────┬───────┘                                     │  │
│  │         │                                              │  │
│  │  ┌──────▼───────┐        ┌─────────────────┐         │  │
│  │  │ Repository   │───────→│ Room Database   │         │  │
│  │  └──────┬───────┘        │ (SQLite)        │         │  │
│  │         │                └─────────────────┘         │  │
│  │  ┌──────▼───────┐                                    │  │
│  │  │ Retrofit     │                                    │  │
│  │  │ Client       │                                    │  │
│  │  └──────┬───────┘                                    │  │
│  └─────────┼────────────────────────────────────────────┘  │
└────────────┼─────────────────────────────────────────────┘
             │
             │ HTTPS Request/Response
             │ (JSON Format)
             │
       ┌─────▼──────┐
       │ Internet   │
       │ (WiFi/4G)  │
       └─────┬──────┘
             │
┌────────────▼──────────────────────────────────────────────┐
│                    CLOUD SERVER                           │
│              (AWS / Heroku / DigitalOcean)                │
├───────────────────────────────────────────────────────────┤
│                                                            │
│  ┌──────────────────────────────────────────────────────┐ │
│  │            FastAPI Backend Application               │ │
│  │                                                       │ │
│  │  ┌─────────────────────────────────────────┐         │ │
│  │  │        API Endpoints                    │         │ │
│  │  │  • POST /api              │         │ │
│  │  │  • POST /api                 │         │ │
│  │  │  • POST /predict                     │         │ │
│  │  │  • GET  /api/disease/{name}             │         │ │
│  │  └──────────────────┬──────────────────────┘         │ │
│  │                     │                                 │ │
│  │  ┌──────────────────▼──────────────────────┐         │ │
│  │  │     Business Logic Layer                │         │ │
│  │  │  • Authentication Service               │         │ │
│  │  │  • Image Processing Service             │         │ │
│  │  │  • ML Model Service (TensorFlow)        │         │ │
│  │  └──────────────────┬──────────────────────┘         │ │
│  │                     │                                 │ │
│  │  ┌──────────────────▼──────────────────────┐         │ │
│  │  │        TensorFlow ML Model              │         │ │
│  │  │  • MobileNetV2 Architecture             │         │ │
│  │  │  • 15 Disease Classes                   │         │ │
│  │  │  • Input: 224x224x3 Image               │         │ │
│  │  │  • Output: Probability Array            │         │ │
│  │  └──────────────────────────────────────────┘        │ │
│  └───────────────────────────────────────────────────────┘ │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐ │
│  │         PostgreSQL Database                          │ │
│  │  • Users Table                                       │ │
│  │  • Disease Info Table                                │ │
│  │  • API Logs Table                                    │ │
│  └──────────────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────────────┘
```

---

## TOOLS AND RESOURCES

### Recommended Diagramming Tools

1. **Draw.io (diagrams.net)** - FREE
   - Website: https://app.diagrams.net/
   - Pros: Free, no account needed, exports to PNG/PDF/SVG
   - Best for: All diagram types
   - Tutorial: Built-in templates available

2. **Lucidchart** - FREE (with limits)
   - Website: https://www.lucidchart.com/
   - Pros: Clean interface, collaboration features
   - Best for: Professional diagrams
   - Tutorial: https://www.lucidchart.com/pages/tutorial/

3. **PlantUML** - FREE (code-based)
   - Website: https://plantuml.com/
   - Pros: Version control friendly, reproducible
   - Best for: UML diagrams from code
   - Tutorial: https://plantuml.com/starting

4. **Microsoft Visio** - PAID
   - Pros: Professional, extensive templates
   - Best for: Enterprise diagrams
   - Alternative: Use college computer lab

5. **Mermaid** - FREE (code-based)
   - Website: https://mermaid.js.org/
   - Pros: Markdown integration, simple syntax
   - Best for: Simple flowcharts, sequence diagrams
   - Tutorial: https://mermaid.js.org/intro/

### Color Schemes

**Professional Palette 1 (Blue Theme):**
- Presentation Layer: #E3F2FD (Light Blue)
- Business Logic: #81C784 (Light Green)
- Data Layer: #FFF9C4 (Light Yellow)
- Network: #FFCC80 (Light Orange)
- Backend: #EF9A9A (Light Red)

**Professional Palette 2 (Corporate):**
- Presentation Layer: #B3E5FC
- Business Logic: #C8E6C9
- Data Layer: #FFF59D
- Network: #FFAB91
- Backend: #F48FB1

### Export Settings

**For Report:**
- Format: PNG or PDF
- Resolution: 300 DPI
- Size: Fit to A4 page
- Background: White

**For Presentation:**
- Format: PNG
- Resolution: 1920×1080 (HD)
- Background: Transparent or White

---

## CHECKLIST FOR COMPLETE DIAGRAMS

- [ ] System Architecture Diagram (High-Level)
- [ ] Data Flow Diagram (Disease Detection)
- [ ] Component Diagram
- [ ] Class Diagram
- [ ] Sequence Diagrams (5-7):
  - [ ] User Registration
  - [ ] User Login
  - [ ] Disease Detection
  - [ ] History Retrieval
  - [ ] Treatment Display
- [ ] Activity Diagrams (3-5):
  - [ ] Disease Detection Flow
  - [ ] Image Capture Flow
  - [ ] User Authentication Flow
- [ ] ER Diagram (Database)
- [ ] Database Schema Diagram
- [ ] Network Architecture Diagram
- [ ] Use Case Diagrams (3-5):
  - [ ] Overall System
  - [ ] User Authentication
  - [ ] Disease Detection
  - [ ] History Management

---

## COMMON MISTAKES TO AVOID

1. **Too Cluttered:** Keep diagrams clean, don't overcrowd
2. **Inconsistent Styling:** Use same colors/fonts throughout
3. **Missing Labels:** Label all arrows and components
4. **Poor Arrow Direction:** Arrows should clearly show data flow
5. **Wrong Relationships:** Use correct UML relationships
6. **No Legend:** Always include legend explaining symbols
7. **Low Resolution:** Export at high resolution for printing
8. **Unclear Hierarchy:** Show clear layers/tiers
9. **Missing Key Components:** Include all major components
10. **No Version/Date:** Add diagram version and date

---

## FINAL TIPS

1. Start with rough sketches on paper
2. Review examples from textbooks/online
3. Get feedback from guide before finalizing
4. Ensure consistency across all diagrams
5. Use professional color schemes
6. Add meaningful notes/annotations
7. Export in multiple formats (PNG, PDF, SVG)
8. Keep editable versions for future updates
9. Maintain aspect ratio when resizing
10. Test readability when printed in black & white

---

**Good luck creating your architecture diagrams! Clear diagrams demonstrate thorough understanding of your system.**
