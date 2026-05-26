# LeafGuard AI - Academic Project Proposal Template

---

## TITLE PAGE

<div align="center">

### [COLLEGE/UNIVERSITY LOGO]

### [COLLEGE/UNIVERSITY NAME]
**Department of Computer Science and Engineering**

---

# PROJECT PROPOSAL

## LeafGuard AI: An Android-Based Plant Disease Detection Application Using Deep Learning

---

**Submitted by:**

**[Student Name]**
Roll Number: [Roll Number]
Class: [Year/Semester]
Branch: Computer Science and Engineering
Course Code: CSE 2206 - Mobile Application Development

---

**Under the Guidance of:**

**[Guide Name]**
[Designation]
Department of Computer Science and Engineering

---

**Academic Year: [YYYY-YYYY]**

**Date of Submission: [DD/MM/YYYY]**

</div>

---

## ABSTRACT

Agriculture is the backbone of the Indian economy, with over 58% of the rural population depending on it for their livelihood. However, plant diseases cause significant crop losses, estimated at 20-40% of global crop production annually. Early and accurate detection of plant diseases is crucial for timely intervention and minimizing economic losses. Traditional disease detection methods rely on visual inspection by agricultural experts, which is time-consuming, expensive, and not always accessible to small-scale farmers.

This project proposes the development of LeafGuard AI, an Android-based mobile application that leverages deep learning and computer vision techniques to detect plant diseases from leaf images captured using a smartphone camera. The application utilizes a TensorFlow Lite model trained on a comprehensive dataset of healthy and diseased plant leaves. Users can capture or upload leaf images, which are analyzed in real-time using a FastAPI backend service that performs disease classification and returns treatment recommendations.

The mobile application is developed using Kotlin for Android, implementing modern Android architecture components including Room for local database management, Retrofit for RESTful API communication, and MVVM (Model-View-ViewModel) design pattern for clean code architecture. The system also incorporates XML parsing for treatment recommendations and maintains a local history of scans for offline access.

This project aims to democratize access to plant disease diagnostics, providing farmers with an affordable, accessible, and accurate tool for protecting their crops. The application addresses the CSE 2206 syllabus requirements comprehensively, demonstrating proficiency in Android development, networking, database management, XML parsing, and machine learning integration.

**Keywords:** Android Development, Plant Disease Detection, Deep Learning, TensorFlow Lite, Mobile Application, Computer Vision, Agricultural Technology, Retrofit, Room Database, FastAPI

---

## 1. INTRODUCTION

### 1.1 Background

Agriculture is fundamental to global food security and economic stability. In India, agriculture contributes approximately 18% to the GDP and provides employment to about 58% of the population. However, agricultural productivity is severely impacted by plant diseases, which are caused by pathogens such as bacteria, fungi, viruses, and environmental stress factors.

Plant diseases lead to substantial crop yield losses, estimated at 20-40% globally each year, translating to economic losses worth billions of dollars. Early detection and diagnosis of plant diseases are critical for implementing effective control measures and preventing widespread crop damage. Traditional methods of disease identification rely heavily on manual inspection by agricultural experts or plant pathologists, which has several limitations:

1. **Limited Accessibility:** Expert consultation is not readily available in remote rural areas
2. **Time-Consuming:** Manual inspection of large agricultural fields is labor-intensive
3. **High Cost:** Hiring experts is expensive for small-scale farmers
4. **Subjectivity:** Visual assessment can vary between experts and may lack consistency
5. **Delayed Response:** By the time diseases are identified, significant damage may have occurred

### 1.2 Motivation

The proliferation of smartphones in India has reached unprecedented levels, with over 750 million smartphone users as of 2023. This widespread adoption, combined with advances in mobile computing power and camera quality, presents a unique opportunity to develop accessible agricultural solutions. Mobile applications can bridge the gap between advanced technology and rural farmers, providing them with powerful diagnostic tools at their fingertips.

Recent breakthroughs in deep learning and computer vision have demonstrated remarkable accuracy in image classification tasks, including plant disease detection. Convolutional Neural Networks (CNNs) can learn complex patterns from large datasets and achieve expert-level accuracy in identifying diseases from leaf images. TensorFlow Lite, Google's lightweight machine learning framework for mobile devices, enables on-device inference, reducing latency and dependency on continuous internet connectivity.

This project is motivated by the convergence of these technologies to create a practical, scalable solution that empowers farmers with real-time disease diagnostics, treatment recommendations, and educational resources—all accessible through a simple mobile application.

### 1.3 Relevance to CSE 2206 Syllabus

This project comprehensively addresses the CSE 2206 Mobile Application Development syllabus by implementing:

- **Android Fundamentals:** Application structure, lifecycle management, resource management
- **User Interface Design:** XML layouts, Material Design components, responsive design
- **Activities and Intents:** Navigation, data passing, activity lifecycle
- **Fragments:** Modular UI components, fragment transactions
- **RecyclerView:** Efficient list display with ViewHolder pattern
- **Networking:** Retrofit for RESTful API communication, HTTP methods
- **JSON Parsing:** API response handling, data serialization
- **XML Parsing:** Treatment recommendations and disease information parsing
- **Database Management:** Room database for local data persistence
- **Runtime Permissions:** Camera and storage permissions
- **Camera Integration:** Image capture and gallery selection
- **Asynchronous Operations:** Kotlin Coroutines for background tasks
- **MVVM Architecture:** ViewModel, LiveData, Repository pattern
- **Machine Learning Integration:** TensorFlow Lite model integration

---

## 2. PROBLEM STATEMENT

**"To develop an Android-based mobile application that utilizes deep learning techniques to accurately detect plant diseases from leaf images, provide actionable treatment recommendations, and maintain a comprehensive history of diagnoses—thereby enabling farmers and agricultural stakeholders to make informed decisions for crop protection and yield optimization."**

### 2.1 Problem Identification

The agricultural sector faces several challenges in plant disease management:

1. **Lack of Immediate Expert Access:** Most farmers, especially in rural areas, do not have immediate access to plant pathologists or agricultural experts who can diagnose diseases accurately.

2. **Knowledge Gap:** Many farmers lack the technical knowledge to identify diseases in their early stages, leading to delayed intervention and increased crop damage.

3. **Cost Barriers:** Hiring experts for regular field inspections is financially burdensome for small and marginal farmers.

4. **Time Sensitivity:** Plant diseases spread rapidly; delayed diagnosis can result in exponential crop losses.

5. **Limited Digital Solutions:** While some web-based solutions exist, they often require continuous internet connectivity and may not be optimized for mobile use in low-bandwidth environments.

6. **Language and Literacy Barriers:** Many existing solutions are not user-friendly for farmers with limited digital literacy or language constraints.

### 2.2 Proposed Solution

LeafGuard AI addresses these challenges by providing:

- **Instant Disease Detection:** Real-time image analysis using on-device machine learning
- **Accessibility:** Mobile-first design accessible to anyone with a smartphone
- **Affordability:** Free or low-cost application with minimal data requirements
- **Educational Value:** Detailed disease information and treatment recommendations
- **Offline Capability:** Local storage of scan history and disease information
- **User-Friendly Interface:** Intuitive design suitable for users with varying digital literacy
- **Actionable Insights:** Clear treatment recommendations with step-by-step guidance

---

## 3. OBJECTIVES

### 3.1 Primary Objectives

1. **Develop a fully functional Android application** for plant disease detection using Kotlin, adhering to modern Android development best practices and architecture patterns.

2. **Integrate a deep learning model** (TensorFlow Lite) capable of accurately classifying plant diseases from leaf images with a minimum accuracy of 85%.

3. **Implement RESTful API communication** using Retrofit to connect the Android application with a FastAPI backend service for disease prediction and data synchronization.

4. **Design and implement a local database** using Room to store scan history, user preferences, and offline disease information for access without internet connectivity.

5. **Parse and display structured information** from XML files containing disease details and treatment recommendations.

### 3.2 Secondary Objectives

6. **Implement user authentication** with registration, login, and session management capabilities.

7. **Develop an intuitive user interface** following Material Design guidelines, ensuring responsiveness across different screen sizes and Android versions.

8. **Integrate camera functionality** with runtime permission handling for capturing leaf images directly from the application.

9. **Provide comprehensive disease information** including symptoms, causes, treatment options, and preventive measures.

10. **Enable scan history management** with features to view, search, and delete previous diagnoses.

### 3.3 Learning Objectives

11. **Demonstrate proficiency** in Android development concepts as per CSE 2206 syllabus.

12. **Apply MVVM architecture pattern** for clean, maintainable, and testable code.

13. **Implement asynchronous programming** using Kotlin Coroutines for background operations.

14. **Integrate third-party libraries** effectively (Retrofit, Room, Glide, TensorFlow Lite).

15. **Conduct thorough testing** including unit tests, integration tests, and UI tests to ensure application reliability.

---

## 4. SCOPE OF THE PROJECT

### 4.1 In Scope

**4.1.1 Core Features**
- User registration and authentication
- Camera integration for leaf image capture
- Gallery integration for image selection
- Real-time disease detection using ML model
- Display of detection results with confidence scores
- Treatment recommendations for identified diseases
- Scan history with local storage
- Disease information database
- User profile management
- Settings and preferences

**4.1.2 Technical Implementation**
- Android application developed in Kotlin
- FastAPI backend for ML inference
- TensorFlow Lite model integration
- Room database for local persistence
- Retrofit for API communication
- XML parsing for disease data
- JSON parsing for API responses
- MVVM architecture implementation
- Material Design UI components

**4.1.3 Supported Features**
- Minimum Android version: 7.0 (API 24)
- Target Android version: Latest stable release
- Support for 10-15 common plant diseases
- Image preprocessing and optimization
- Offline access to scan history
- Network error handling
- Loading states and progress indicators

### 4.2 Out of Scope

**4.2.1 Features Not Included**
- Multi-language support (Phase 2)
- Voice-based interaction (Future enhancement)
- Real-time video analysis (Future enhancement)
- IoT sensor integration (Future work)
- Social features (community forums, sharing)
- E-commerce integration for purchasing treatments
- Expert consultation booking system
- Crop yield prediction
- Weather integration
- GPS-based disease mapping

**4.2.2 Technical Limitations**
- No iOS application (Android only)
- No web application interface
- No cloud storage for images (local only)
- No push notifications (can be added later)
- No payment gateway integration
- No advanced analytics dashboard
- No AR/VR features

### 4.3 Deliverables

1. **Software Deliverables**
   - Fully functional Android APK (debug and release versions)
   - Source code repository on GitHub
   - FastAPI backend code
   - TensorFlow Lite model file
   - Database schema and sample data
   - XML files for disease information

2. **Documentation Deliverables**
   - Project proposal document (this document)
   - System design document
   - API documentation
   - User manual
   - Installation guide
   - Final project report
   - Test plan and test cases
   - Presentation slides

3. **Diagram Deliverables**
   - System architecture diagram
   - UML diagrams (use case, class, sequence, activity)
   - ER diagram for database
   - Data flow diagrams
   - UI wireframes and mockups

---

## 5. LITERATURE REVIEW

### 5.1 Existing Solutions and Related Work

**5.1.1 Commercial Applications**

1. **Plantix by PEAT**
   - AI-powered crop disease detection
   - Covers 90+ crops and 500+ diseases
   - Limitations: Requires consistent internet, limited offline functionality
   - Reference: Plantix GmbH, "Plantix - your crop doctor," 2023

2. **Agrio - Plant Health & Diseases**
   - Plant disease identification using AI
   - Community-based expert consultations
   - Limitations: Freemium model, limited free features
   - Reference: Saillog Ltd, "Agrio App," 2023

3. **PlantSnap**
   - Plant identification primarily for ornamental plants
   - Disease detection as secondary feature
   - Limitations: Focus on plant identification rather than diseases
   - Reference: PlantSnap Inc., "PlantSnap Plant Identification," 2023

**5.1.2 Research-Based Solutions**

4. **Deep Learning for Plant Disease Detection** (Rahman et al., 2020)
   - CNN-based model achieving 98% accuracy
   - Dataset: PlantVillage with 54,000 images
   - Limitation: Web-based, not mobile-optimized
   - Reference: Rahman, M., et al., "Plant Disease Detection Using Deep Learning," ICIET 2020

5. **Mobile Application for Crop Disease Detection** (Kamilaris & Prenafeta-Boldú, 2018)
   - Survey of deep learning applications in agriculture
   - Identified need for mobile-first solutions
   - Reference: Kamilaris, A., et al., "Deep learning in agriculture: A survey," Computers and Electronics in Agriculture, 2018

6. **TensorFlow Lite for Mobile Deployment** (Ignatov et al., 2019)
   - Demonstrates feasibility of on-device ML inference
   - Performance benchmarking on mobile devices
   - Reference: Ignatov, A., et al., "AI Benchmark: Running Deep Neural Networks on Android Smartphones," ECCV 2018

**5.1.3 Android Development Best Practices**

7. **Modern Android Architecture** (Google, 2023)
   - MVVM pattern with LiveData and ViewModel
   - Repository pattern for data abstraction
   - Reference: Google Developers, "Guide to app architecture," 2023

8. **Retrofit for Network Calls** (Square, 2023)
   - Type-safe HTTP client for Android
   - Integration with coroutines for async operations
   - Reference: Square Inc., "Retrofit Documentation," 2023

9. **Room Persistence Library** (Google, 2023)
   - Abstraction layer over SQLite
   - Compile-time verification of SQL queries
   - Reference: Google Developers, "Room Persistence Library," 2023

### 5.2 Research Gap

While several plant disease detection applications exist, most have the following limitations:

1. **Internet Dependency:** Heavy reliance on cloud-based processing
2. **Cost Barriers:** Freemium models limiting access to essential features
3. **Limited Offline Capability:** Minimal functionality without internet
4. **Generic Implementations:** Not tailored to regional crop varieties
5. **Poor User Experience:** Complex interfaces not suitable for farmers
6. **Lack of Educational Content:** Limited information on disease prevention

### 5.3 Proposed Contribution

LeafGuard AI addresses these gaps by:

- Implementing on-device ML inference for reduced latency and internet dependency
- Providing completely free access to core features
- Enabling comprehensive offline functionality with local database
- Offering user-friendly interface designed for users with varying digital literacy
- Including detailed educational content on disease prevention and treatment
- Following modern Android development practices as per academic curriculum

---

## 6. METHODOLOGY

### 6.1 Development Approach

**Agile Methodology with Iterative Development**

The project will follow an Agile development approach with 2-week sprints, allowing for iterative development, continuous testing, and regular feedback incorporation.

**Phases:**
1. **Phase 1:** Requirements Analysis and Design (Weeks 1-2)
2. **Phase 2:** Backend Development and ML Model Integration (Weeks 3-4)
3. **Phase 3:** Android UI Development (Weeks 5-7)
4. **Phase 4:** Feature Implementation (Weeks 8-10)
5. **Phase 5:** Testing and Refinement (Weeks 11-12)
6. **Phase 6:** Documentation and Deployment (Week 12)

### 6.2 System Architecture

**Three-Tier Architecture**

1. **Presentation Layer (Android App)**
   - User interface components
   - User interaction handling
   - Local data display

2. **Business Logic Layer (ViewModels & Repositories)**
   - Data processing
   - Business rule implementation
   - API communication
   - Database operations

3. **Data Layer (Backend + Database)**
   - FastAPI backend for ML inference
   - Room database for local storage
   - XML files for static data

### 6.3 Technology Stack

**6.3.1 Android Application**
- **Language:** Kotlin
- **IDE:** Android Studio (latest stable version)
- **Minimum SDK:** API 24 (Android 7.0)
- **Target SDK:** API 34 (Android 14)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Dependency Injection:** Hilt (optional) or manual DI
- **UI Framework:** Jetpack Compose or XML layouts with Material Design

**6.3.2 Libraries and Frameworks**
- **Networking:** Retrofit 2.9+ with OkHttp
- **Image Loading:** Glide or Coil
- **Database:** Room 2.5+
- **Coroutines:** Kotlin Coroutines for async operations
- **JSON Parsing:** Gson or Moshi
- **XML Parsing:** XmlPullParser
- **ML:** TensorFlow Lite 2.x
- **Testing:** JUnit, Espresso, Mockito

**6.3.3 Backend**
- **Framework:** FastAPI (Python)
- **ML Framework:** TensorFlow/Keras
- **Model Serving:** TensorFlow Serving or custom FastAPI endpoint
- **Database:** PostgreSQL or MySQL (for user management)
- **Deployment:** Heroku, AWS, or DigitalOcean

**6.3.4 Machine Learning**
- **Model Architecture:** Convolutional Neural Network (CNN)
- **Base Model:** MobileNetV2 or EfficientNet (transfer learning)
- **Dataset:** PlantVillage or custom dataset
- **Training Framework:** TensorFlow/Keras
- **Model Format:** TensorFlow Lite (.tflite)

### 6.4 System Design

**6.4.1 Data Flow**
1. User captures/selects leaf image
2. Image preprocessed (resize, normalize)
3. Image sent to backend via Retrofit
4. Backend performs ML inference
5. Results returned as JSON
6. App parses JSON and displays results
7. Results saved to local Room database
8. Treatment recommendations fetched from XML

**6.4.2 Database Design**

**Room Database Tables:**

```
Table: ScanResult
- id (Primary Key, Int, Auto-generated)
- image_path (String)
- disease_name (String)
- confidence_score (Float)
- timestamp (Long)
- treatment_summary (String)
- user_id (Int, Foreign Key)

Table: User
- id (Primary Key, Int, Auto-generated)
- name (String)
- email (String, Unique)
- password_hash (String)
- created_at (Long)

Table: DiseaseInfo
- id (Primary Key, Int, Auto-generated)
- disease_name (String, Unique)
- symptoms (String)
- causes (String)
- treatment (String)
- prevention (String)
```

**6.4.3 API Design**

**Endpoints:**

```
POST /api/auth/register
Request: { "name": "...", "email": "...", "password": "..." }
Response: { "status": "success", "user_id": "..." }

POST /api/auth/login
Request: { "email": "...", "password": "..." }
Response: { "status": "success", "token": "...", "user": {...} }

POST /api/detect
Request: Multipart form-data with image file
Response: {
  "disease_name": "...",
  "confidence": 0.87,
  "top_predictions": [
    {"disease": "...", "confidence": 0.87},
    {"disease": "...", "confidence": 0.10}
  ]
}

GET /api/disease/{disease_name}
Response: {
  "name": "...",
  "symptoms": "...",
  "treatment": "..."
}
```

### 6.5 Implementation Plan

**Week 1-2: Requirements and Design**
- Finalize requirements
- Create wireframes and mockups
- Design database schema
- Create UML diagrams
- Set up development environment

**Week 3-4: Backend and ML**
- Develop FastAPI backend
- Train/fine-tune ML model
- Convert model to TensorFlow Lite
- Test model accuracy
- Deploy backend

**Week 5-7: Android UI**
- Implement splash screen
- Create authentication screens
- Develop home screen
- Implement scan screen with camera
- Create result display screen
- Develop history screen
- Build profile screen

**Week 8-10: Feature Implementation**
- Integrate Retrofit for API calls
- Implement Room database
- Add XML parsing for treatments
- Integrate TFLite model
- Implement image processing
- Add error handling
- Implement loading states

**Week 11-12: Testing and Documentation**
- Write unit tests
- Conduct integration testing
- Perform UI testing
- User acceptance testing
- Fix bugs
- Write documentation
- Prepare presentation
- Final report writing

### 6.6 Testing Strategy

**6.6.1 Unit Testing**
- ViewModel tests
- Repository tests
- Utility function tests
- XML parser tests
- Validation tests

**6.6.2 Integration Testing**
- API communication tests
- Database operations tests
- ML model integration tests

**6.6.3 UI Testing**
- Espresso tests for user flows
- Navigation tests
- Form validation tests

**6.6.4 Performance Testing**
- App launch time
- Image processing time
- API response time
- Memory usage
- Battery consumption

**6.6.5 Usability Testing**
- User interface intuitiveness
- Navigation clarity
- Error message clarity
- Overall user experience

---

## 7. PROJECT TIMELINE

### 7.1 Gantt Chart

| Task | Week 1 | Week 2 | Week 3 | Week 4 | Week 5 | Week 6 | Week 7 | Week 8 | Week 9 | Week 10 | Week 11 | Week 12 |
|------|---------|---------|---------|---------|---------|---------|---------|---------|---------|----------|----------|----------|
| **Phase 1: Analysis & Design** |
| Requirements gathering | ████ | | | | | | | | | | | |
| System design | ████ | ████ | | | | | | | | | | |
| UI/UX design | | ████ | | | | | | | | | | |
| **Phase 2: Backend & ML** |
| Backend development | | | ████ | ████ | | | | | | | | |
| ML model training | | | ████ | ████ | | | | | | | | |
| Backend deployment | | | | ████ | | | | | | | | |
| **Phase 3: Android UI** |
| Authentication screens | | | | | ████ | | | | | | | |
| Home & navigation | | | | | ████ | ████ | | | | | | |
| Scan screen | | | | | | ████ | ████ | | | | | |
| Result & history screens | | | | | | | ████ | | | | | |
| **Phase 4: Features** |
| API integration | | | | | | | | ████ | | | | |
| Database implementation | | | | | | | | ████ | ████ | | | |
| ML integration | | | | | | | | | ████ | ████ | | |
| XML parsing | | | | | | | | | ████ | | | |
| **Phase 5: Testing** |
| Unit testing | | | | | | | | | | ████ | ████ | |
| Integration testing | | | | | | | | | | | ████ | |
| Bug fixing | | | | | | | | | | ████ | ████ | ████ |
| **Phase 6: Documentation** |
| Code documentation | | | | | | | | | | | ████ | ████ |
| Final report | | | | | | | | | | | ████ | ████ |
| Presentation prep | | | | | | | | | | | | ████ |

### 7.2 Milestones

| Milestone | Target Date | Deliverables |
|-----------|-------------|--------------|
| M1: Requirements Complete | End of Week 2 | Requirements document, wireframes, diagrams |
| M2: Backend Ready | End of Week 4 | Functional API, deployed backend, tested ML model |
| M3: UI Complete | End of Week 7 | All screens implemented, navigation working |
| M4: Features Integrated | End of Week 10 | Fully functional app with all features |
| M5: Testing Complete | End of Week 11 | Test reports, bug-free application |
| M6: Project Submission | End of Week 12 | Final report, presentation, demo-ready app |

---

## 8. EXPECTED OUTCOMES

### 8.1 Technical Outcomes

1. **Fully Functional Android Application**
   - Installable APK compatible with Android 7.0+
   - Smooth user experience with intuitive navigation
   - Responsive UI adapting to different screen sizes

2. **Accurate Disease Detection**
   - Minimum 85% classification accuracy
   - Sub-5-second detection time
   - Confidence scores for predictions

3. **Robust Backend Service**
   - Scalable FastAPI backend
   - Reliable ML inference endpoint
   - Proper error handling and logging

4. **Efficient Data Management**
   - Local database with CRUD operations
   - Offline access to scan history
   - Efficient data synchronization

### 8.2 Learning Outcomes

1. **Comprehensive Android Development Skills**
   - Proficiency in Kotlin programming
   - Understanding of Android architecture components
   - Experience with modern development tools

2. **API Integration Experience**
   - RESTful API design and consumption
   - Handling asynchronous network operations
   - Error handling and retry mechanisms

3. **Database Management Skills**
   - Room database implementation
   - SQL query optimization
   - Data modeling and relationships

4. **Machine Learning Integration**
   - TensorFlow Lite model deployment
   - Image preprocessing techniques
   - Inference optimization for mobile

5. **Software Engineering Practices**
   - Version control with Git
   - Code organization and documentation
   - Testing and quality assurance

### 8.3 Social Impact

1. **Empowering Farmers**
   - Accessible disease diagnostics
   - Reduced dependency on experts
   - Cost-effective crop management

2. **Increased Agricultural Productivity**
   - Early disease detection
   - Timely intervention
   - Reduced crop losses

3. **Knowledge Dissemination**
   - Educational content on diseases
   - Best practices for disease prevention
   - Treatment recommendations

---

## 9. RISK ANALYSIS

### 9.1 Technical Risks

| Risk | Probability | Impact | Mitigation Strategy |
|------|-------------|--------|---------------------|
| ML model accuracy below expectations | Medium | High | Use pre-trained models, increase training data, perform extensive validation |
| API latency issues | Medium | Medium | Implement caching, optimize backend, add timeout handling |
| App crashes on low-end devices | Low | Medium | Test on various devices, optimize memory usage, implement error handling |
| Database migration issues | Low | Medium | Plan migrations carefully, maintain backward compatibility, test thoroughly |
| Backend deployment failures | Low | High | Choose reliable hosting, implement CI/CD, have rollback plan |
| TFLite model size too large | Medium | Low | Use quantization, model pruning, compress assets |

### 9.2 Project Management Risks

| Risk | Probability | Impact | Mitigation Strategy |
|------|-------------|--------|---------------------|
| Scope creep | Medium | High | Clearly define scope, prioritize features, use MoSCoW method |
| Time overrun | Medium | High | Create realistic timeline, buffer for delays, regular progress monitoring |
| Dependency on external APIs | Low | Medium | Implement fallback mechanisms, mock services for testing |
| Learning curve for new technologies | Medium | Low | Allocate time for learning, follow tutorials, seek guidance |
| Hardware/software issues | Low | Medium | Keep backups, use version control, have backup development environment |

### 9.3 Resource Risks

| Risk | Probability | Impact | Mitigation Strategy |
|------|-------------|--------|---------------------|
| Limited access to testing devices | Medium | Low | Use emulators, borrow devices, prioritize critical device tests |
| Insufficient dataset for ML | Low | High | Use public datasets (PlantVillage), augment data, transfer learning |
| Server hosting costs | Low | Low | Use free tiers (Heroku, AWS Free), optimize resource usage |
| Limited computational resources for training | Medium | Low | Use cloud services (Google Colab), pre-trained models |

### 9.4 Contingency Plans

1. **If ML model accuracy is insufficient:**
   - Use established pre-trained models (MobileNet, EfficientNet)
   - Increase training data through augmentation
   - Adjust model architecture
   - Fine-tune hyperparameters

2. **If backend deployment fails:**
   - Have backup hosting provider
   - Implement local-only mode with pre-loaded model
   - Use mock API for demonstration

3. **If timeline is exceeded:**
   - Prioritize MVP (Minimum Viable Product) features
   - Move non-critical features to future enhancements
   - Seek extension if necessary

4. **If device compatibility issues arise:**
   - Focus on most common Android versions (8.0-14)
   - Implement graceful degradation
   - Provide clear minimum requirements

---

## 10. BUDGET ESTIMATION

### 10.1 Development Costs

| Item | Description | Cost (INR) |
|------|-------------|------------|
| Android Studio | Free (Open source) | ₹0 |
| Kotlin Language | Free (Open source) | ₹0 |
| Libraries & SDKs | Free (Open source) | ₹0 |
| Development Device | Personal Android smartphone | ₹0 (owned) |
| Testing Devices | Emulators + borrowed devices | ₹0 |
| **Total Development** | | **₹0** |

### 10.2 Infrastructure Costs

| Item | Description | Cost (INR) |
|------|-------------|------------|
| Backend Hosting | Heroku free tier / AWS free tier | ₹0 (first year) |
| Database Hosting | PostgreSQL on Heroku free tier | ₹0 |
| Domain Name | Optional (can use provided URL) | ₹0-500/year |
| SSL Certificate | Free (Let's Encrypt) | ₹0 |
| Cloud Storage | For backups (Google Drive) | ₹0 (free tier) |
| **Total Infrastructure** | | **₹0-500** |

### 10.3 ML Model Training Costs

| Item | Description | Cost (INR) |
|------|-------------|------------|
| Training Platform | Google Colab free tier / Kaggle | ₹0 |
| Dataset | PlantVillage (public dataset) | ₹0 |
| GPU Resources | Colab free GPU access | ₹0 |
| **Total ML Training** | | **₹0** |

### 10.4 Documentation & Presentation

| Item | Description | Cost (INR) |
|------|-------------|------------|
| Documentation Tools | Microsoft Word / Google Docs | ₹0 (free) |
| Diagram Tools | Draw.io, Lucidchart free tier | ₹0 |
| Presentation Software | PowerPoint / Google Slides | ₹0 (free) |
| Report Printing | Proposal + Final report (2 copies) | ₹500-1000 |
| Report Binding | Spiral/Hard binding | ₹200-500 |
| **Total Documentation** | | **₹700-1500** |

### 10.5 Miscellaneous

| Item | Description | Cost (INR) |
|------|-------------|------------|
| Internet Charges | For development (3 months) | ₹0 (existing) |
| Electricity | Additional costs | ₹0 (negligible) |
| Reference Materials | Online resources (free) | ₹0 |
| Travel | College visits (if required) | ₹0-500 |
| **Total Miscellaneous** | | **₹0-500** |

### 10.6 Total Project Cost

| Category | Cost (INR) |
|----------|------------|
| Development | ₹0 |
| Infrastructure | ₹0-500 |
| ML Training | ₹0 |
| Documentation | ₹700-1500 |
| Miscellaneous | ₹0-500 |
| **Grand Total** | **₹700-2500** |

**Note:** This is a low-budget project leveraging free and open-source tools. All costs are approximate and may vary based on specific college requirements and personal circumstances.

---

## 11. REFERENCES

[1] Google Developers, "Guide to app architecture," Android Developer Documentation, 2023. [Online]. Available: https://developer.android.com/jetpack/guide

[2] Square Inc., "Retrofit - A type-safe HTTP client for Android and Java," 2023. [Online]. Available: https://square.github.io/retrofit/

[3] Google Developers, "Room Persistence Library," Android Developer Documentation, 2023. [Online]. Available: https://developer.android.com/training/data-storage/room

[4] TensorFlow Team, "TensorFlow Lite | ML for Mobile and Edge Devices," 2023. [Online]. Available: https://www.tensorflow.org/lite

[5] Rahman, M., Islam, M. M., Akash, S. S., and Shatabda, S., "Plant Disease Detection Using Deep Learning," in International Conference on Information and Communication Technology for Development (ICIET), 2020.

[6] Kamilaris, A., and Prenafeta-Boldú, F. X., "Deep learning in agriculture: A survey," Computers and Electronics in Agriculture, vol. 147, pp. 70-90, 2018.

[7] Ignatov, A., et al., "AI Benchmark: Running Deep Neural Networks on Android Smartphones," in European Conference on Computer Vision (ECCV) Workshops, 2018.

[8] Hughes, D. P., and Salathé, M., "An open access repository of images on plant health to enable the development of mobile disease diagnostics," arXiv preprint arXiv:1511.08060, 2015.

[9] Mohanty, S. P., Hughes, D. P., and Salathé, M., "Using Deep Learning for Image-Based Plant Disease Detection," Frontiers in Plant Science, vol. 7, p. 1419, 2016.

[10] Ferentinos, K. P., "Deep learning models for plant disease detection and diagnosis," Computers and Electronics in Agriculture, vol. 145, pp. 311-318, 2018.

[11] Google Developers, "Kotlin Programming Language," 2023. [Online]. Available: https://developer.android.com/kotlin

[12] Plantix GmbH, "Plantix - your crop doctor," 2023. [Online]. Available: https://plantix.net/

[13] Saillog Ltd, "Agrio - Effective Plant Disease Detection," 2023. [Online]. Available: https://agrio.app/

[14] Howard, A. G., et al., "MobileNets: Efficient Convolutional Neural Networks for Mobile Vision Applications," arXiv preprint arXiv:1704.04861, 2017.

[15] Tan, M., and Le, Q. V., "EfficientNet: Rethinking Model Scaling for Convolutional Neural Networks," in International Conference on Machine Learning (ICML), 2019.

---

## APPENDICES

### Appendix A: Glossary of Terms

- **Android SDK:** Software Development Kit for building Android applications
- **API:** Application Programming Interface
- **CNN:** Convolutional Neural Network
- **DAO:** Data Access Object
- **FastAPI:** Modern Python web framework for building APIs
- **JSON:** JavaScript Object Notation, a lightweight data format
- **Kotlin:** Modern programming language for Android development
- **MVVM:** Model-View-ViewModel architecture pattern
- **REST:** Representational State Transfer
- **Room:** Android's abstraction layer over SQLite database
- **Retrofit:** Type-safe HTTP client for Android
- **TFLite:** TensorFlow Lite, ML framework for mobile devices
- **XML:** Extensible Markup Language

### Appendix B: Contact Information

**Student Details:**
- Name: [Your Name]
- Roll Number: [Your Roll Number]
- Email: [your.email@college.edu]
- Phone: [Your Phone Number]

**Guide Details:**
- Name: [Guide Name]
- Designation: [Designation]
- Department: Computer Science and Engineering
- Email: [guide.email@college.edu]

**Institution:**
- [College/University Name]
- [Department]
- [Address]
- [City, State, PIN]
- [Contact Number]
- [Website]

---

## DECLARATION

I hereby declare that this project proposal titled **"LeafGuard AI: An Android-Based Plant Disease Detection Application Using Deep Learning"** is my original work. The information presented has been gathered from authentic sources, and all references have been duly acknowledged. This proposal is submitted for academic purposes as part of the CSE 2206 - Mobile Application Development course.

**Student Name:** ____________________

**Roll Number:** ____________________

**Signature:** ____________________

**Date:** ____________________

---

## GUIDE APPROVAL

This project proposal has been reviewed and is approved for implementation.

**Guide Name:** ____________________

**Designation:** ____________________

**Signature:** ____________________

**Date:** ____________________

---

## HOD APPROVAL

This project proposal is approved and the student may proceed with implementation.

**HOD Name:** ____________________

**Department:** Computer Science and Engineering

**Signature:** ____________________

**Date:** ____________________

---

**End of Proposal**
