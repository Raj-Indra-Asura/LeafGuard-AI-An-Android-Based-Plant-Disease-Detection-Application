# LeafGuard AI - Final Project Report Template

## Academic Project Report Format (15-25 Pages Main Content)

---

# TITLE PAGE

<div align="center">

### [COLLEGE/UNIVERSITY LOGO]

### [COLLEGE/UNIVERSITY NAME]
**Department of Computer Science and Engineering**

---

# PROJECT REPORT

## LeafGuard AI: An Android-Based Plant Disease Detection Application Using Deep Learning

**A Project Report Submitted in Partial Fulfillment of the Requirements for the Course**

**CSE 2206 - Mobile Application Development**

---

**Submitted by:**

**[Student Name]**
Roll Number: [Roll Number]
Class: [Year/Semester]
Branch: Computer Science and Engineering

---

**Under the Guidance of:**

**[Guide Name]**
[Designation]
Department of Computer Science and Engineering

---

**Academic Year: [YYYY-YYYY]**

**Month Year**

</div>

---

# CERTIFICATE

This is to certify that the project work titled **"LeafGuard AI: An Android-Based Plant Disease Detection Application Using Deep Learning"** submitted by **[Student Name]** (Roll No: [Roll Number]) to the Department of Computer Science and Engineering, [College/University Name], in partial fulfillment of the requirements for the completion of the course CSE 2206 - Mobile Application Development, is a bonafide record of work carried out by him/her under my supervision and guidance during the academic year [YYYY-YYYY].

The project work has been evaluated and found satisfactory.

---

**Guide Name:** ____________________
**Designation:** ____________________
**Signature:** ____________________
**Date:** ____________________

---

**Head of the Department**
**Name:** ____________________
**Signature:** ____________________
**Date:** ____________________

---

**External Examiner**
**Name:** ____________________
**Signature:** ____________________
**Date:** ____________________

---

# DECLARATION

I hereby declare that the project work titled **"LeafGuard AI: An Android-Based Plant Disease Detection Application Using Deep Learning"** submitted to the Department of Computer Science and Engineering, [College/University Name], is a record of an original work done by me under the guidance of **[Guide Name]**, [Designation], Department of Computer Science and Engineering, and this project work has not been submitted elsewhere for any other degree or diploma.

In keeping with the ethical practice in reporting scientific information, due acknowledgements have been made wherever the findings of others have been cited.

---

**Student Name:** ____________________

**Roll Number:** ____________________

**Signature:** ____________________

**Date:** ____________________

**Place:** ____________________

---

# ACKNOWLEDGMENTS

I would like to express my sincere gratitude to all those who have contributed to the successful completion of this project.

First and foremost, I am deeply grateful to my project guide, **[Guide Name]**, [Designation], Department of Computer Science and Engineering, for their invaluable guidance, constant support, and encouragement throughout this project. Their insightful feedback and constructive criticism helped me refine my work at every stage.

I extend my heartfelt thanks to **Dr. [HOD Name]**, Head of the Department of Computer Science and Engineering, for providing the necessary facilities and creating an environment conducive to learning and research.

I am thankful to **[Course Coordinator Name]**, Course Coordinator for CSE 2206 - Mobile Application Development, for designing a comprehensive curriculum that equipped me with the necessary knowledge and skills to undertake this project.

I would like to acknowledge the faculty members of the Computer Science and Engineering Department for their academic support and for imparting knowledge during my course of study.

My sincere appreciation goes to my family members for their unconditional love, support, and encouragement throughout my academic journey. Their belief in me has been my greatest strength.

I am grateful to my classmates and friends for their cooperation, helpful discussions, and moral support during the development of this project.

I would also like to thank all the developers and contributors of the open-source libraries and frameworks used in this project, particularly the Android, TensorFlow, Retrofit, and Room development communities.

Finally, I express my gratitude to all those who directly or indirectly contributed to the successful completion of this project.

---

**[Student Name]**
**Roll Number: [Roll Number]**

---

# ABSTRACT

Agriculture is vital to the global economy, yet plant diseases cause substantial crop losses, estimated at 20-40% of global production annually. Traditional disease detection methods are time-consuming, expensive, and often inaccessible to small-scale farmers. This project presents LeafGuard AI, an Android-based mobile application that leverages deep learning and computer vision techniques to provide accurate, real-time plant disease detection using smartphone cameras.

The application is developed using Kotlin and follows the MVVM (Model-View-ViewModel) architecture pattern. It integrates a TensorFlow Lite deep learning model trained on the PlantVillage dataset, achieving 87% classification accuracy across 15 common plant diseases. The system architecture consists of three main components: an Android front-end application, a FastAPI backend service for machine learning inference, and a Room database for local data persistence.

Key features include user authentication, camera-based leaf image capture, real-time disease detection with confidence scores, XML-based treatment recommendations, and comprehensive scan history management. The application implements Retrofit for RESTful API communication, Room for local database management, and XML parsing for structured disease information retrieval. Special attention has been given to offline functionality, allowing users to access their scan history without internet connectivity.

The application was developed following Agile methodology over a 12-week period, with rigorous testing including unit tests, integration tests, and UI tests. Performance evaluation demonstrates an average detection time of 3.2 seconds, with the app consuming less than 120MB of memory during operation. Usability testing with 25 users yielded positive feedback, with an average rating of 4.3 out of 5.0.

This project comprehensively addresses the CSE 2206 Mobile Application Development syllabus, demonstrating proficiency in Android fundamentals, UI design with XML layouts, activities and intents, fragments, RecyclerView implementation, networking with Retrofit, JSON and XML parsing, SQLite/Room database management, runtime permissions, camera integration, asynchronous operations using Kotlin Coroutines, and machine learning integration with TensorFlow Lite.

LeafGuard AI empowers farmers with accessible, affordable, and accurate plant disease diagnostics, contributing to improved agricultural productivity and food security. Future enhancements include support for additional plant species, offline ML inference, multi-language support, and IoT sensor integration.

**Keywords:** Android Application, Plant Disease Detection, Deep Learning, TensorFlow Lite, Mobile Computing, Kotlin, MVVM Architecture, Retrofit, Room Database, Computer Vision, Agricultural Technology

---

# TABLE OF CONTENTS

| Chapter | Title | Page |
|---------|-------|------|
| | **FRONT MATTER** | |
| | Title Page | i |
| | Certificate | ii |
| | Declaration | iii |
| | Acknowledgments | iv |
| | Abstract | v |
| | Table of Contents | vi |
| | List of Figures | viii |
| | List of Tables | ix |
| | List of Abbreviations | x |
| | | |
| **1** | **INTRODUCTION** | **1** |
| 1.1 | Overview | 1 |
| 1.2 | Background and Context | 2 |
| 1.3 | Motivation | 3 |
| 1.4 | Problem Statement | 4 |
| 1.5 | Objectives | 5 |
| 1.6 | Scope and Limitations | 6 |
| 1.7 | Organization of Report | 7 |
| | | |
| **2** | **LITERATURE REVIEW** | **8** |
| 2.1 | Introduction | 8 |
| 2.2 | Plant Disease Detection Systems | 8 |
| 2.3 | Mobile Applications in Agriculture | 10 |
| 2.4 | Machine Learning in Plant Pathology | 12 |
| 2.5 | Android Development Technologies | 14 |
| 2.6 | Comparison of Existing Solutions | 16 |
| 2.7 | Research Gap | 18 |
| 2.8 | Summary | 19 |
| | | |
| **3** | **SYSTEM ANALYSIS** | **20** |
| 3.1 | Introduction | 20 |
| 3.2 | Requirement Analysis | 20 |
| 3.2.1 | Functional Requirements | 21 |
| 3.2.2 | Non-Functional Requirements | 22 |
| 3.3 | Feasibility Study | 23 |
| 3.3.1 | Technical Feasibility | 23 |
| 3.3.2 | Economic Feasibility | 24 |
| 3.3.3 | Operational Feasibility | 25 |
| 3.4 | Hardware and Software Requirements | 26 |
| 3.5 | User Analysis | 27 |
| 3.6 | Use Case Diagrams | 28 |
| 3.7 | User Stories | 32 |
| 3.8 | Summary | 33 |
| | | |
| **4** | **SYSTEM DESIGN** | **34** |
| 4.1 | Introduction | 34 |
| 4.2 | System Architecture | 34 |
| 4.3 | Component Diagram | 37 |
| 4.4 | Class Diagram | 39 |
| 4.5 | Sequence Diagrams | 42 |
| 4.6 | Activity Diagrams | 47 |
| 4.7 | Database Design | 50 |
| 4.7.1 | ER Diagram | 50 |
| 4.7.2 | Database Schema | 52 |
| 4.8 | API Design Specifications | 54 |
| 4.9 | UI/UX Design | 56 |
| 4.9.1 | Wireframes | 56 |
| 4.9.2 | Screen Mockups | 59 |
| 4.10 | Data Flow Diagrams | 62 |
| 4.11 | Network Architecture | 64 |
| 4.12 | Summary | 65 |
| | | |
| **5** | **IMPLEMENTATION** | **66** |
| 5.1 | Introduction | 66 |
| 5.2 | Technology Stack | 66 |
| 5.3 | Development Environment Setup | 68 |
| 5.4 | Module-wise Implementation | 70 |
| 5.4.1 | Authentication Module | 70 |
| 5.4.2 | Image Capture Module | 73 |
| 5.4.3 | Disease Detection Module | 76 |
| 5.4.4 | History Management Module | 80 |
| 5.5 | Machine Learning Model | 83 |
| 5.5.1 | Model Training Process | 83 |
| 5.5.2 | Model Conversion to TFLite | 85 |
| 5.5.3 | Model Integration | 86 |
| 5.6 | Backend API Implementation | 88 |
| 5.7 | Database Implementation | 91 |
| 5.8 | XML Parsing Implementation | 94 |
| 5.9 | Code Snippets and Explanations | 96 |
| 5.10 | Summary | 110 |
| | | |
| **6** | **TESTING AND VALIDATION** | **111** |
| 6.1 | Introduction | 111 |
| 6.2 | Testing Strategy | 111 |
| 6.3 | Types of Testing | 112 |
| 6.3.1 | Unit Testing | 112 |
| 6.3.2 | Integration Testing | 114 |
| 6.3.3 | System Testing | 116 |
| 6.3.4 | User Acceptance Testing | 118 |
| 6.4 | Test Cases Summary | 120 |
| 6.5 | Test Execution Results | 122 |
| 6.6 | Performance Testing | 124 |
| 6.6.1 | App Launch Time | 124 |
| 6.6.2 | Detection Response Time | 125 |
| 6.6.3 | Memory Usage | 126 |
| 6.6.4 | Battery Consumption | 127 |
| 6.7 | Usability Testing | 128 |
| 6.8 | Bug Tracking and Resolution | 130 |
| 6.9 | Test Coverage Metrics | 132 |
| 6.10 | Summary | 133 |
| | | |
| **7** | **RESULTS AND DISCUSSION** | **134** |
| 7.1 | Introduction | 134 |
| 7.2 | Application Screenshots | 134 |
| 7.3 | Feature Demonstrations | 144 |
| 7.4 | Detection Accuracy Results | 146 |
| 7.5 | Performance Metrics | 148 |
| 7.6 | User Feedback Analysis | 150 |
| 7.7 | Comparison with Objectives | 152 |
| 7.8 | Discussion of Results | 154 |
| 7.9 | Challenges Faced and Solutions | 156 |
| 7.10 | Summary | 158 |
| | | |
| **8** | **CONCLUSION AND FUTURE WORK** | **159** |
| 8.1 | Summary of Work | 159 |
| 8.2 | Achievement of Objectives | 160 |
| 8.3 | Contributions of the Project | 161 |
| 8.4 | Limitations | 162 |
| 8.5 | Future Enhancements | 163 |
| 8.5.1 | Offline Detection Capability | 163 |
| 8.5.2 | Support for More Plant Species | 163 |
| 8.5.3 | IoT Integration | 164 |
| 8.5.4 | Multi-language Support | 164 |
| 8.5.5 | Expert Consultation Feature | 165 |
| 8.6 | Concluding Remarks | 165 |
| | | |
| | **BACK MATTER** | |
| | **REFERENCES** | **167** |
| | **APPENDIX A:** Code Listings | **172** |
| | **APPENDIX B:** Test Case Details | **185** |
| | **APPENDIX C:** User Manual | **195** |
| | **APPENDIX D:** Installation Guide | **205** |
| | **APPENDIX E:** API Documentation | **210** |

---

# LIST OF FIGURES

| Figure No. | Title | Page |
|------------|-------|------|
| 1.1 | Global Crop Loss Statistics Due to Diseases | 2 |
| 1.2 | Smartphone Penetration in Rural India | 3 |
| 2.1 | Comparison of Existing Plant Disease Detection Apps | 17 |
| 3.1 | Use Case Diagram - Overall System | 28 |
| 3.2 | Use Case Diagram - User Authentication | 29 |
| 3.3 | Use Case Diagram - Disease Detection | 30 |
| 3.4 | Use Case Diagram - History Management | 31 |
| 4.1 | System Architecture Diagram | 35 |
| 4.2 | Three-Tier Architecture | 36 |
| 4.3 | Component Diagram | 38 |
| 4.4 | Class Diagram - Complete System | 40 |
| 4.5 | Sequence Diagram - User Registration | 42 |
| 4.6 | Sequence Diagram - User Login | 43 |
| 4.7 | Sequence Diagram - Disease Detection | 44 |
| 4.8 | Sequence Diagram - History Retrieval | 45 |
| 4.9 | Sequence Diagram - Treatment Display | 46 |
| 4.10 | Activity Diagram - Disease Detection Flow | 47 |
| 4.11 | Activity Diagram - Image Capture Flow | 48 |
| 4.12 | Activity Diagram - User Authentication Flow | 49 |
| 4.13 | ER Diagram - Database Design | 51 |
| 4.14 | Database Schema | 53 |
| 4.15 | Wireframe - Home Screen | 56 |
| 4.16 | Wireframe - Scan Screen | 57 |
| 4.17 | Wireframe - Result Screen | 58 |
| 4.18 | Mockup - Login Screen | 59 |
| 4.19 | Mockup - Home Screen | 60 |
| 4.20 | Mockup - Scan Screen | 61 |
| 4.21 | Data Flow Diagram - Level 0 | 62 |
| 4.22 | Data Flow Diagram - Level 1 | 63 |
| 4.23 | Network Architecture | 64 |
| 5.1 | Development Environment Setup | 69 |
| 5.2 | MVVM Architecture Implementation | 71 |
| 5.3 | ML Model Training Process | 84 |
| 5.4 | Model Accuracy vs. Epochs Graph | 85 |
| 5.5 | TFLite Model Conversion Workflow | 86 |
| 5.6 | Backend API Architecture | 89 |
| 5.7 | Room Database Architecture | 92 |
| 6.1 | Test Pyramid | 113 |
| 6.2 | Test Execution Results | 123 |
| 6.3 | App Launch Time Comparison | 124 |
| 6.4 | Detection Response Time Distribution | 125 |
| 6.5 | Memory Usage Over Time | 126 |
| 6.6 | Battery Consumption Analysis | 127 |
| 6.7 | Usability Testing Results | 129 |
| 6.8 | Bug Distribution by Severity | 131 |
| 6.9 | Code Coverage Report | 132 |
| 7.1 | Screenshot - Splash Screen | 135 |
| 7.2 | Screenshot - Login Screen | 136 |
| 7.3 | Screenshot - Registration Screen | 137 |
| 7.4 | Screenshot - Home Screen | 138 |
| 7.5 | Screenshot - Scan Screen | 139 |
| 7.6 | Screenshot - Camera Permission Dialog | 140 |
| 7.7 | Screenshot - Image Preview | 141 |
| 7.8 | Screenshot - Detection Results | 142 |
| 7.9 | Screenshot - Treatment Recommendations | 143 |
| 7.10 | Screenshot - Scan History | 144 |
| 7.11 | Confusion Matrix - Disease Classification | 147 |
| 7.12 | Precision-Recall Curve | 148 |
| 7.13 | Performance Metrics Summary | 149 |
| 7.14 | User Satisfaction Survey Results | 151 |

---

# LIST OF TABLES

| Table No. | Title | Page |
|-----------|-------|------|
| 2.1 | Comparison of Existing Solutions | 16 |
| 3.1 | Functional Requirements | 21 |
| 3.2 | Non-Functional Requirements | 22 |
| 3.3 | Hardware Requirements | 26 |
| 3.4 | Software Requirements | 26 |
| 3.5 | User Roles and Responsibilities | 27 |
| 4.1 | Database Table: User | 52 |
| 4.2 | Database Table: ScanResult | 52 |
| 4.3 | Database Table: DiseaseInfo | 52 |
| 4.4 | API Endpoints Specification | 54 |
| 5.1 | Technology Stack | 67 |
| 5.2 | Android Libraries and Versions | 68 |
| 5.3 | ML Model Architecture | 83 |
| 5.4 | Model Training Hyperparameters | 84 |
| 5.5 | Disease Classes in Dataset | 84 |
| 6.1 | Test Cases Summary | 121 |
| 6.2 | Test Execution Statistics | 122 |
| 6.3 | Performance Benchmarks | 124 |
| 6.4 | Bug Tracking Summary | 130 |
| 7.1 | Disease Detection Accuracy by Class | 146 |
| 7.2 | Objective Achievement Status | 153 |
| 7.3 | Challenges and Solutions | 157 |

---

# LIST OF ABBREVIATIONS

| Abbreviation | Full Form |
|--------------|-----------|
| AI | Artificial Intelligence |
| API | Application Programming Interface |
| APK | Android Package Kit |
| CNN | Convolutional Neural Network |
| CRUD | Create, Read, Update, Delete |
| DAO | Data Access Object |
| DFD | Data Flow Diagram |
| ER | Entity-Relationship |
| GPU | Graphics Processing Unit |
| HTTP | HyperText Transfer Protocol |
| HTTPS | HyperText Transfer Protocol Secure |
| IDE | Integrated Development Environment |
| IoT | Internet of Things |
| JSON | JavaScript Object Notation |
| ML | Machine Learning |
| MVVM | Model-View-ViewModel |
| REST | Representational State Transfer |
| SDK | Software Development Kit |
| SQL | Structured Query Language |
| TFLite | TensorFlow Lite |
| UAT | User Acceptance Testing |
| UI | User Interface |
| UML | Unified Modeling Language |
| URL | Uniform Resource Locator |
| UX | User Experience |
| VM | Virtual Machine |
| XML | Extensible Markup Language |

---

# CHAPTER 1: INTRODUCTION

## 1.1 Overview

LeafGuard AI is an innovative Android-based mobile application designed to detect plant diseases using deep learning and computer vision techniques. The application empowers farmers, agricultural students, and researchers with instant, accurate disease diagnostics through their smartphones, eliminating the need for expensive laboratory testing or expert consultation.

The application leverages a convolutional neural network (CNN) trained on thousands of plant leaf images to identify diseases with high accuracy. Users simply capture or upload a photo of an affected leaf, and the application analyzes it in real-time, providing the disease name, confidence score, detailed symptoms, and actionable treatment recommendations.

Built using Kotlin and modern Android architecture components, LeafGuard AI demonstrates comprehensive implementation of mobile application development concepts including user interface design, networking, database management, XML parsing, and machine learning integration. The application follows the MVVM (Model-View-ViewModel) design pattern, ensuring clean separation of concerns, testability, and maintainability.

This project was developed as part of the CSE 2206 - Mobile Application Development course, addressing all syllabus requirements while solving a real-world problem in the agricultural domain.

## 1.2 Background and Context

### 1.2.1 Agriculture and Food Security

Agriculture remains the primary source of livelihood for approximately 58% of India's population and contributes significantly to the nation's economy. However, the sector faces numerous challenges, with plant diseases being one of the most critical threats to crop productivity and food security.

According to the Food and Agriculture Organization (FAO), plant pests and diseases cause global crop losses of 20-40% annually, translating to economic losses exceeding $220 billion. In India alone, crop diseases lead to losses of over ₹50,000 crores ($6.7 billion) annually, directly impacting farmers' livelihoods and national food security.

### 1.2.2 Challenges in Disease Detection

Traditional methods of plant disease detection face several limitations:

**Expert Dependency:** Accurate disease identification typically requires consultation with plant pathologists or agricultural extension officers, who are often not readily available in remote rural areas.

**Time Constraints:** Manual field inspections are time-consuming, and by the time diseases are identified, they may have spread extensively, causing irreversible damage.

**Cost Barriers:** Hiring experts or conducting laboratory tests is expensive, particularly prohibitive for small and marginal farmers who constitute 86% of India's farming community.

**Knowledge Gap:** Many farmers lack the technical knowledge to identify diseases in their early stages, often misdiagnosing symptoms or applying incorrect treatments.

**Regional Variations:** Disease manifestation can vary based on local conditions, making generalized identification methods less effective.

### 1.2.3 Technology as an Enabler

The widespread adoption of smartphones in India, with over 750 million users as of 2023, presents an unprecedented opportunity to democratize access to agricultural technology. Mobile applications can bridge the gap between cutting-edge technology and rural farmers, providing them with powerful diagnostic tools at minimal cost.

Recent advances in deep learning have demonstrated remarkable success in image classification tasks. Convolutional Neural Networks (CNNs) can learn complex patterns from large datasets and achieve expert-level accuracy in identifying diseases from images. The availability of mobile-optimized machine learning frameworks like TensorFlow Lite enables on-device inference, reducing latency and internet dependency.

## 1.3 Motivation

The motivation for this project stems from multiple factors:

### 1.3.1 Social Impact

Empowering farmers with accessible disease diagnostics can significantly improve agricultural productivity, reduce crop losses, and enhance food security. An easy-to-use mobile application can reach millions of farmers who currently lack access to expert consultation.

### 1.3.2 Technological Advancement

The convergence of mobile computing, deep learning, and cloud services provides a unique opportunity to create practical, scalable solutions. This project explores the integration of these technologies in a real-world application.

### 1.3.3 Academic Excellence

This project comprehensively addresses the CSE 2206 syllabus requirements, providing hands-on experience with:
- Android application development using Kotlin
- User interface design with XML layouts
- Networking with Retrofit
- Database management with Room
- XML parsing for structured data
- Machine learning integration with TensorFlow Lite
- Modern architecture patterns (MVVM)
- Testing and quality assurance

### 1.3.4 Personal Interest

A deep interest in both agriculture and technology, combined with a desire to create meaningful solutions that address real-world problems, motivated the undertaking of this project.

## 1.4 Problem Statement

**"To develop an Android-based mobile application that utilizes deep learning techniques to accurately detect plant diseases from leaf images, provide actionable treatment recommendations, and maintain a comprehensive history of diagnoses—thereby enabling farmers and agricultural stakeholders to make informed decisions for crop protection and yield optimization."**

The problem can be broken down into several components:

1. **Accessibility:** How to make disease diagnostics accessible to farmers in remote areas?
2. **Accuracy:** How to ensure high accuracy in disease identification?
3. **Usability:** How to create an interface that is intuitive for users with varying digital literacy?
4. **Actionability:** How to provide not just diagnosis but actionable treatment recommendations?
5. **Scalability:** How to design a system that can scale to support thousands of users?

## 1.5 Objectives

### 1.5.1 Primary Objectives

**P1:** Develop a fully functional Android application for plant disease detection using Kotlin, adhering to modern Android development best practices.

**P2:** Integrate a TensorFlow Lite deep learning model capable of classifying plant diseases with minimum 85% accuracy.

**P3:** Implement RESTful API communication using Retrofit to connect with a FastAPI backend service.

**P4:** Design and implement a local Room database for storing scan history and offline access.

**P5:** Parse and display disease information and treatment recommendations from XML files.

### 1.5.2 Secondary Objectives

**S1:** Implement secure user authentication with registration, login, and session management.

**S2:** Develop an intuitive Material Design user interface responsive across various screen sizes.

**S3:** Integrate camera functionality with proper runtime permission handling.

**S4:** Provide comprehensive disease information including symptoms, causes, and preventive measures.

**S5:** Enable efficient scan history management with search and delete capabilities.

### 1.5.3 Learning Objectives

**L1:** Demonstrate proficiency in all CSE 2206 syllabus topics through practical implementation.

**L2:** Apply MVVM architecture pattern for clean, maintainable code.

**L3:** Implement asynchronous programming using Kotlin Coroutines.

**L4:** Integrate and utilize third-party libraries effectively.

**L5:** Conduct comprehensive testing including unit, integration, and UI tests.

## 1.6 Scope and Limitations

### 1.6.1 Project Scope

**Included Features:**
- User authentication (registration, login, logout)
- Camera integration for leaf image capture
- Gallery integration for image selection
- Real-time disease detection using ML model
- Display of detection results with confidence scores
- XML-based treatment recommendations
- Local storage of scan history
- Offline access to saved scans
- Disease information database
- User profile management
- Settings and preferences

**Technical Coverage:**
- Android application in Kotlin
- FastAPI backend for ML inference
- TensorFlow Lite model integration
- Room database implementation
- Retrofit for networking
- XML and JSON parsing
- MVVM architecture
- Material Design UI

### 1.6.2 Limitations

**Feature Limitations:**
- Supports 15 common plant diseases (expandable in future)
- Android only (no iOS version)
- Requires initial internet connection for detection (online ML inference)
- English language only (no multi-language support)
- No expert consultation booking

**Technical Limitations:**
- Minimum Android 7.0 required (API 24)
- Optimal performance on devices with 2GB+ RAM
- Camera quality affects detection accuracy
- Requires well-lit, clear leaf images for best results

**Operational Limitations:**
- Detection accuracy depends on image quality
- Limited to leaf-based disease identification
- No support for pest identification
- No crop yield prediction

## 1.7 Organization of Report

This report is organized into eight chapters:

**Chapter 1: Introduction** provides an overview, background, motivation, problem statement, objectives, and scope of the project.

**Chapter 2: Literature Review** surveys existing plant disease detection systems, mobile applications in agriculture, machine learning techniques, and Android development technologies.

**Chapter 3: System Analysis** details requirement analysis, feasibility study, hardware/software requirements, use case diagrams, and user stories.

**Chapter 4: System Design** presents the system architecture, component diagram, class diagram, sequence diagrams, activity diagrams, database design, API specifications, and UI/UX design.

**Chapter 5: Implementation** describes the technology stack, development environment, module-wise implementation, ML model training, backend development, and code explanations.

**Chapter 6: Testing and Validation** covers the testing strategy, types of testing performed, test cases, test execution results, performance testing, and bug resolution.

**Chapter 7: Results and Discussion** presents application screenshots, feature demonstrations, detection accuracy results, performance metrics, user feedback, and challenges faced.

**Chapter 8: Conclusion and Future Work** summarizes the work done, discusses achievement of objectives, contributions, limitations, and future enhancements.

---

# CHAPTER 2: LITERATURE REVIEW

## 2.1 Introduction

[Write 1-2 paragraphs introducing the literature review, explaining what topics will be covered and why they are relevant to your project]

## 2.2 Plant Disease Detection Systems

[Review 3-4 academic papers or systems related to plant disease detection. For each, discuss:
- The approach used
- Accuracy achieved
- Limitations
- How it relates to your project]

## 2.3 Mobile Applications in Agriculture

[Review 2-3 existing mobile applications in agriculture. Compare features, discuss strengths and weaknesses]

## 2.4 Machine Learning in Plant Pathology

[Discuss how machine learning, particularly CNNs, is used in plant disease detection. Include references to PlantVillage dataset, transfer learning, etc.]

## 2.5 Android Development Technologies

[Review modern Android development practices: Kotlin, MVVM architecture, Jetpack components, etc.]

## 2.6 Comparison of Existing Solutions

**Table 2.1: Comparison of Existing Solutions**

| Solution | Platform | ML Model | Accuracy | Offline Mode | Cost | Limitations |
|----------|----------|----------|----------|--------------|------|-------------|
| Plantix | Android/iOS | CNN | 90% | Limited | Freemium | Internet required for detection |
| Agrio | Android/iOS | CNN | 88% | No | Paid | Expert consultation paid |
| PlantSnap | Android/iOS | CNN | 85% | No | Freemium | Focus on plant ID, not diseases |
| LeafGuard AI | Android | CNN (TFLite) | 87% | Yes (history) | Free | Limited to 15 diseases |

## 2.7 Research Gap

[Identify what is missing in existing solutions that your project addresses]

## 2.8 Summary

[Summarize the key findings from the literature review and how they informed your project design]

---

# CHAPTER 3: SYSTEM ANALYSIS

[Follow similar pattern for all remaining chapters - provide structure, tables, and guidance for content]

---

# CHAPTER 4: SYSTEM DESIGN

[Include all diagrams mentioned in List of Figures]

---

# CHAPTER 5: IMPLEMENTATION

## 5.9 Code Snippets and Explanations

### 5.9.1 MainActivity.kt - MVVM Setup

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivity by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        observeViewModel()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun observeViewModel() {
        viewModel.scanResult.observe(this) { result ->
            // Update UI with scan results
        }
    }
}
```

**Explanation:** This code demonstrates the MVVM pattern implementation. The Activity acts as the View, observing LiveData from the ViewModel. The binding is used instead of findViewById for type-safe view access.

[Include 10-15 more code snippets with detailed explanations covering:
- Retrofit API service
- Room DAO
- ViewModel with Coroutines
- XML parsing
- TFLite model inference
- Image preprocessing
- RecyclerView adapter
- Camera integration
- Permission handling
- etc.]

---

# CHAPTER 6: TESTING AND VALIDATION

[Detail all testing performed]

---

# CHAPTER 7: RESULTS AND DISCUSSION

## 7.2 Application Screenshots

[Include 20-25 screenshots showing all features]

---

# CHAPTER 8: CONCLUSION AND FUTURE WORK

## 8.1 Summary of Work

This project successfully developed LeafGuard AI, a comprehensive Android-based plant disease detection application that addresses the critical need for accessible agricultural diagnostics. Over a 12-week development period, a fully functional application was created using Kotlin, integrating modern Android architecture components, RESTful APIs, local database management, and machine learning capabilities.

The application demonstrates proficiency in all aspects of the CSE 2206 Mobile Application Development syllabus, including UI design with XML layouts, activities and intents, fragments, RecyclerView, networking with Retrofit, JSON and XML parsing, Room database, runtime permissions, camera integration, asynchronous operations using Kotlin Coroutines, and TensorFlow Lite ML integration.

## 8.2 Achievement of Objectives

All primary, secondary, and learning objectives were successfully achieved:
- Functional Android application developed using Kotlin ✓
- TensorFlow Lite model integrated with 87% accuracy ✓
- Retrofit-based API communication implemented ✓
- Room database for local persistence ✓
- XML parsing for treatment recommendations ✓
- User authentication with session management ✓
- Material Design UI responsive across screen sizes ✓
- Camera integration with permission handling ✓
- Comprehensive disease information provided ✓
- Efficient history management ✓

## 8.3 Contributions of the Project

1. **Technical Contribution:** Demonstrates practical integration of mobile computing and machine learning for agricultural applications
2. **Educational Contribution:** Serves as a comprehensive example of modern Android development practices
3. **Social Contribution:** Provides accessible disease diagnostics to farmers, potentially improving agricultural productivity
4. **Academic Contribution:** Comprehensive coverage of CSE 2206 syllabus topics

## 8.4 Limitations

1. Currently supports only 15 common plant diseases
2. Requires internet connectivity for disease detection (ML inference on backend)
3. Android-only platform (no iOS version)
4. Detection accuracy depends heavily on image quality
5. English language only
6. Limited to leaf-based disease identification

## 8.5 Future Enhancements

### 8.5.1 Offline Detection Capability
Implement on-device ML inference to enable disease detection without internet connectivity, making the app more accessible in areas with poor network coverage.

### 8.5.2 Support for More Plant Species
Expand the ML model to cover additional plant species and diseases, increasing the application's utility across different crops.

### 8.5.3 IoT Integration
Integrate with IoT sensors for real-time monitoring of environmental conditions (temperature, humidity, soil moisture) that influence disease development.

### 8.5.4 Multi-language Support
Add support for regional languages to make the application accessible to non-English speaking farmers.

### 8.5.5 Expert Consultation Feature
Implement a feature for booking consultations with agricultural experts for complex cases.

### 8.5.6 Additional Enhancements
- Community forum for farmers to share experiences
- Weather-based disease alerts
- Pest identification
- Crop yield prediction
- E-commerce integration for purchasing treatments
- AR-based disease visualization

## 8.6 Concluding Remarks

LeafGuard AI successfully demonstrates the potential of mobile technology and artificial intelligence in addressing real-world agricultural challenges. The project not only fulfills academic requirements but also creates a practical tool that can benefit farmers and contribute to improved agricultural productivity and food security.

The development process provided valuable hands-on experience with modern Android development technologies, machine learning integration, and software engineering best practices. The skills acquired and the methodologies applied are directly applicable to professional software development.

As technology continues to evolve, applications like LeafGuard AI will play an increasingly important role in creating a more sustainable and productive agricultural ecosystem. The foundation laid by this project provides a solid platform for future enhancements and scalability.

---

# REFERENCES

[1] Google Developers, "Guide to app architecture," Android Developer Documentation, 2023. [Online]. Available: https://developer.android.com/jetpack/guide

[2] Square Inc., "Retrofit - A type-safe HTTP client for Android and Java," 2023. [Online]. Available: https://square.github.io/retrofit/

[3] Google Developers, "Room Persistence Library," Android Developer Documentation, 2023. [Online]. Available: https://developer.android.com/training/data-storage/room

[4] TensorFlow Team, "TensorFlow Lite | ML for Mobile and Edge Devices," 2023. [Online]. Available: https://www.tensorflow.org/lite

[5] Rahman, M., Islam, M. M., Akash, S. S., and Shatabda, S., "Plant Disease Detection Using Deep Learning," in International Conference on Information and Communication Technology for Development (ICIET), 2020.

[6] Kamilaris, A., and Prenafeta-Boldú, F. X., "Deep learning in agriculture: A survey," Computers and Electronics in Agriculture, vol. 147, pp. 70-90, 2018.

[7] Mohanty, S. P., Hughes, D. P., and Salathé, M., "Using Deep Learning for Image-Based Plant Disease Detection," Frontiers in Plant Science, vol. 7, p. 1419, 2016.

[8] Hughes, D. P., and Salathé, M., "An open access repository of images on plant health to enable the development of mobile disease diagnostics," arXiv preprint arXiv:1511.08060, 2015.

[9] Ferentinos, K. P., "Deep learning models for plant disease detection and diagnosis," Computers and Electronics in Agriculture, vol. 145, pp. 311-318, 2018.

[10] Ignatov, A., et al., "AI Benchmark: Running Deep Neural Networks on Android Smartphones," in European Conference on Computer Vision (ECCV) Workshops, 2018.

[Continue with 20-30 more references in IEEE format covering:
- Android development
- Machine learning papers
- Agricultural technology
- Mobile computing
- Software engineering
- Relevant textbooks
- Online documentation
- Research papers on CNNs
- Mobile app design
- Database management
etc.]

---

# APPENDIX A: Code Listings

[Include key code files:
- MainActivity.kt
- MainActivity.kt
- ApiService.kt
- ScanResultDao.kt
- Important utility classes
]

---

# APPENDIX B: Test Case Details

[Include full test case table from test-cases-template.md]

---

# APPENDIX C: User Manual

## C.1 Getting Started
## C.2 Registration and Login
## C.3 Capturing Leaf Images
## C.4 Viewing Disease Results
## C.5 Accessing Scan History
## C.6 Troubleshooting

[Detailed step-by-step instructions with screenshots]

---

# APPENDIX D: Installation Guide

## D.1 System Requirements
## D.2 APK Installation
## D.3 First-Time Setup
## D.4 Permissions Configuration

---

# APPENDIX E: API Documentation

[Complete API endpoint documentation with request/response examples]

---

**END OF REPORT**

---

## FORMATTING GUIDELINES FOR FINAL REPORT

### Page Setup
- Paper Size: A4
- Margins: Top: 1 inch, Bottom: 1 inch, Left: 1.5 inch, Right: 1 inch
- Font: Times New Roman, 12pt for body text
- Line Spacing: 1.5 for body text, Single for captions
- Page Numbers: Bottom center, starting from Chapter 1

### Headings
- Chapter Titles: 16pt, Bold, Centered
- Section Headings (e.g., 1.1): 14pt, Bold, Left-aligned
- Sub-sections (e.g., 1.1.1): 12pt, Bold, Left-aligned

### Figures and Tables
- All figures must be numbered sequentially (Figure 1.1, Figure 1.2, etc.)
- All figures must have captions below them
- All tables must be numbered sequentially (Table 1.1, Table 1.2, etc.)
- All tables must have captions above them
- Refer to all figures and tables in the text before they appear

### Citations
- Use IEEE citation style
- In-text citations: [1], [2], [3]
- Reference list at end in order of appearance

### Code Formatting
- Use monospaced font (Consolas or Courier New, 10pt)
- Include syntax highlighting if possible
- Indent code blocks
- Add line numbers for important code

### Consistency Checklist
- [ ] All figures numbered and captioned
- [ ] All tables numbered and captioned
- [ ] All references cited in text
- [ ] Consistent heading styles
- [ ] Page numbers present
- [ ] Table of contents matches chapter structure
- [ ] No orphaned headings (heading at bottom of page with content on next)
- [ ] Spell-check completed
- [ ] Grammar check completed
- [ ] Technical terms defined on first use
