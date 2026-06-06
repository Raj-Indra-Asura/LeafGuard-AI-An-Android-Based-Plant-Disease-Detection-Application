# How to Open the Master Structure Diagram in Astah UML

## File
`docs/uml/LeafGuardAI_master_structure.xml`

This file is in **UML 2.x XMI format** (Eclipse UML2 standard).  
Astah Professional and Astah UML support this format natively via XMI import.

---

## Step-by-Step Import Guide

### Step 1 — Download / clone the file
Make sure you have `docs/uml/LeafGuardAI_master_structure.xml` on your local machine.

### Step 2 — Open Astah
Open **Astah Professional** or **Astah UML**.  
(Astah Community has limited XMI support; Astah Professional is recommended.)

### Step 3 — Import the XMI file
1. Go to **File** → **Import** → **XMI 2.x (Eclipse UML2)**
2. In the file browser, navigate to `docs/uml/`
3. Select `LeafGuardAI_master_structure.xml`
4. Click **Open** (or **Import**)
5. Astah will import all packages, classes, interfaces, attributes, methods, and relationships into a new project

### Step 4 — View the diagram
After import:
1. In the **Structure Tree** (left panel), you will see the `LeafGuardAI_MasterStructure` model node
2. Expand it to see all 7 packages
3. Go to **Diagram** → **New Diagram** → **Class Diagram**
4. Drag all packages/classes from the **Model Browser** onto the canvas
5. Use **View** → **Auto Layout** to arrange them cleanly

### Step 5 — Save as .asta
Once the diagram is arranged:
- Go to **File** → **Save As**
- Save it as `LeafGuardAI_master_structure.asta`
- This becomes your native Astah file you can open directly next time

---

## What Is Inside the Diagram

| Package | Classes / Interfaces | Description |
|---|---|---|
| `com.leafguard` | MainActivity, ResultActivity, HistoryActivity, HistoryDetailActivity, DiseaseLibraryActivity, SettingsActivity | All Android Activity screens |
| `com.leafguard.network` | ApiService *(interface)*, RetrofitClient, PredictionResponse | Retrofit REST networking |
| `com.leafguard.ml` | TFLiteClassifier | On-device ML inference |
| `com.leafguard.database` | AppDatabase, ScanDao *(interface)*, ScanRecord | Room persistence layer |
| `com.leafguard.utils` | NotificationHelper | Utility / notification |
| `backend_api` | LeafGuardAPI, ModelPredictor, load_predictor, PredictionResult, Config | Python/FastAPI server |
| `model` | KerasModel, TFLiteModel, DiseaseLabels, GenerateStubModel | ML model resource files |

---

## Key Relationships Modelled

```
MainActivity
  ├── navigates to → ResultActivity
  ├── navigates to → HistoryActivity
  ├── navigates to → DiseaseLibraryActivity
  ├── navigates to → SettingsActivity
  ├── uses (cloud mode)   → RetrofitClient → ApiService → PredictionResponse
  ├── uses (offline mode) → TFLiteClassifier → TFLiteModel
  └── uses                → NotificationHelper

ResultActivity   → saves scan to   → AppDatabase
HistoryActivity  → reads scans     → AppDatabase → ScanDao (composition) → ScanRecord
HistoryActivity  → navigates to    → HistoryDetailActivity

RetrofitClient   → HTTP POST /predict → LeafGuardAPI (FastAPI)

LeafGuardAPI
  ├── uses     → ModelPredictor
  ├── returns  → PredictionResult
  └── reads    → Config

ModelPredictor
  ├── loads           → KerasModel
  └── reads labels    → DiseaseLabels

TFLiteClassifier
  ├── loads           → TFLiteModel
  └── reads labels    → DiseaseLabels
```

---

## Troubleshooting

| Problem | Fix |
|---|---|
| Diagram canvas is empty after import | Go to **Model Browser**, select all classes, drag them onto a new Class Diagram canvas |
| "XMI import" option is greyed out | You may be using Astah Community; upgrade to Astah Professional or Astah UML |
| Classes appear with no relationships | Astah imports elements into the model but may not auto-draw all edges; add them manually from the model browser |
| File not found error | Make sure you downloaded the `.xml` file (not the GitHub HTML page) — use **Raw** button on GitHub to get the actual file |

---

## Astah Version Compatibility

| Version | XMI 2.x Import | Notes |
|---|---|---|
| Astah Professional | ✅ Full support | Recommended |
| Astah UML | ✅ Full support | Recommended |
| Astah Community (free) | ⚠️ Limited | May not import all element types |
