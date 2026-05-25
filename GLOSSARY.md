# LeafGuard AI - Technical Glossary

## Purpose

This glossary defines every technical term used throughout the 12-week roadmap. When you encounter an unfamiliar term in any weekly material, refer back to this document.

---

## A

**ADB (Android Debug Bridge)**
Command-line tool for communicating with Android devices. Used for installing APKs, viewing logs, debugging, and running shell commands on devices.

**Activity**
A single screen with a user interface in Android. Each screen in your app (MainActivity, ResultActivity, etc.) is an Activity class that extends `android.app.Activity` or `AppCompatActivity`.

**Android Manifest**
XML file (`AndroidManifest.xml`) that describes essential information about your app: package name, components (activities, services), permissions, hardware requirements.

**Android SDK (Software Development Kit)**
Collection of tools, libraries, and APIs needed to develop Android applications. Includes build tools, platform versions, system images for emulators.

**API (Application Programming Interface)**
Set of rules and protocols for building software. In LeafGuard AI: REST API endpoints that Android app calls to communicate with backend.

**APK (Android Package)**
File format for distributing and installing Android apps. Contains compiled code, resources, assets, and manifest. Your final deliverable will be an APK file.

**ASGI (Asynchronous Server Gateway Interface)**
Python specification for async web servers and applications. Uvicorn is an ASGI server that runs FastAPI.

**Assets Folder**
Directory in Android project (`app/src/main/assets/`) for storing raw files (XML, TFLite models, text files) that are packaged with the app and accessed at runtime.

---

## B

**Backend**
Server-side component that processes requests from client (Android app). In LeafGuard AI: FastAPI Python application that receives images and returns predictions.

**Base URL**
Root address of an API. For local development: `http://192.168.1.5:8000`. Configured in RetrofitClient.

**Bitmap**
Android class representing an image in memory. Created from image files using `BitmapFactory.decodeFile()` or similar methods.

**Build.gradle**
Configuration file for Gradle build system. Contains SDK versions, dependencies, build configurations. Two files: project-level and module-level (app-level).

**ByteBuffer**
Java class for holding a sequence of bytes. Used in TensorFlow Lite to prepare image data for model input.

---

## C

**Callback**
Function passed as an argument to another function, executed after an operation completes. Retrofit uses `onResponse()` and `onFailure()` callbacks for network requests.

**Camera Intent**
Android Intent with action `MediaStore.ACTION_IMAGE_CAPTURE` that launches the device camera app to take a photo.

**CNN (Convolutional Neural Network)**
Type of deep learning model commonly used for image classification. Your plant disease model is likely a CNN (ResNet, MobileNet, etc.).

**Confidence Score**
Probability (0.0 to 1.0 or 0% to 100%) that represents how certain the ML model is about its prediction. Example: 0.92 = 92% confident.

**ConstraintLayout**
Android layout that allows flexible positioning of UI elements using constraints (relationships between views). Modern alternative to RelativeLayout.

**CRUD (Create, Read, Update, Delete)**
Four basic operations for persistent storage. In Room DAO: `@Insert` (Create), `@Query` (Read), `@Update` (Update), `@Delete` (Delete).

---

## D

**DAO (Data Access Object)**
Interface in Room database that defines methods for accessing database. Contains methods annotated with `@Query`, `@Insert`, `@Delete`, etc.

**Dataset**
Collection of training examples (images and labels) used to train a machine learning model. PlantVillage is a popular plant disease dataset.

**Dependency**
External library or module your project relies on. Declared in `build.gradle` with `implementation` keyword. Examples: Retrofit, Room, Gson, TFLite.

**Drawable**
Resource type in Android for images and graphics. Stored in `res/drawable/`. Can be PNG, JPEG, XML vector graphics.

---

## E

**Edge Case**
Unusual or extreme scenario that may cause software to behave unexpectedly. Examples: no internet, permission denied, empty database, corrupted image.

**Emulator (AVD - Android Virtual Device)**
Software that simulates an Android device on your computer. Used for testing apps without a physical device.

**Entity**
Class decorated with `@Entity` annotation in Room database. Represents a table in SQLite database. Each instance is a row; each field is a column.

**Endpoint**
Specific URL path on a server that performs an operation. Example: `POST /predict` is an endpoint that accepts image uploads.

---

## F

**FastAPI**
Modern Python web framework for building APIs. Used in LeafGuard AI backend for handling image upload and returning predictions.

**Fragment**
Reusable portion of an Android UI. Like a mini-activity that can be embedded in activities. Not required for LeafGuard AI but good to know.

**Frontend**
Client-side component that users interact with directly. In LeafGuard AI: the Android app.

---

## G

**Gallery Intent**
Android Intent with action `Intent.ACTION_PICK` that opens the device gallery for selecting an existing image.

**GET Request**
HTTP method for retrieving data from a server. Contrasts with POST (sending data). Example: fetching disease info from an API.

**GPS (Global Positioning System)**
Satellite-based system for determining device location. Android accesses GPS through LocationManager.

**Gradle**
Build automation tool for Android projects. Manages dependencies, compiles code, packages APK.

**Gson**
Google library for converting Java objects to JSON and vice versa. Used with Retrofit for parsing API responses.

---

## H

**HTTP (Hypertext Transfer Protocol)**
Protocol for transmitting data over the internet. Methods include GET (retrieve), POST (send), PUT (update), DELETE (remove).

**HTTPS (HTTP Secure)**
Encrypted version of HTTP using SSL/TLS. Production APIs should use HTTPS; local development typically uses HTTP.

---

## I

**ImageView**
Android UI component for displaying images. XML tag: `<ImageView>`. Set image with `setImageBitmap()` or `setImageResource()`.

**Inference**
Process of using a trained ML model to make predictions on new data. Example: passing a leaf image through the model to predict disease.

**Intent**
Messaging object used to request an action from another Android component. Used for starting activities, opening camera, sharing content.

**Interpreter (TensorFlow Lite)**
Class that loads a TFLite model and runs inference. Created with `new Interpreter(modelFile)`, used with `interpreter.run(input, output)`.

---

## J

**Java**
Programming language used for Android development (alongside Kotlin). LeafGuard AI is written in Java to align with CSE 2206 syllabus.

**JSON (JavaScript Object Notation)**
Lightweight data format for exchanging data between client and server. Example: `{"disease": "Early Blight", "confidence": 0.92}`.

---

## K

**Kotlin**
Modern programming language for Android development, interoperable with Java. Not used in LeafGuard AI (Java preferred for course), but good to be aware of.

---

## L

**Label**
Text identifier for a class in machine learning. Example: "Tomato Early Blight", "Potato Late Blight". Stored in `labels.txt` and mapped to model output indices.

**Latency**
Time delay between action and response. In LeafGuard AI: time from image upload to receiving prediction. Measured in milliseconds or seconds.

**Layout XML**
XML file defining the structure and appearance of an Android screen. Stored in `res/layout/`. Example: `activity_main.xml`.

**ListView**
Android UI component for displaying scrollable list of items. Modern alternative is RecyclerView (more efficient).

**Logcat**
Android Studio tool for viewing system logs. Used for debugging. Log messages with `Log.d()`, `Log.e()`, `Log.i()`, etc.

---

## M

**MainActivity**
Typically the entry point activity of an Android app (launched when app icon is tapped). Declared as LAUNCHER in AndroidManifest.

**Manifest (AndroidManifest.xml)**
See "Android Manifest" above.

**Material Design**
Google's design language for Android apps. Provides guidelines for colors, typography, components, animations.

**ML (Machine Learning)**
Field of AI where computers learn patterns from data without explicit programming. Your plant disease model is an ML model.

**Model**
In machine learning: trained neural network that makes predictions. In Android: data class representing business entities.

**Multipart Form Data**
HTTP encoding type for uploading files. Content-Type: `multipart/form-data`. Used when posting images to backend.

**MVVM (Model-View-ViewModel)**
Architecture pattern for organizing Android code. Not required for LeafGuard AI but considered best practice.

---

## N

**Notification**
Message displayed in Android notification panel. Created with `NotificationManager` and `NotificationCompat.Builder`.

**NotificationChannel**
Category for notifications (required on Android 8.0+). Allows users to control notification settings per channel.

**Null**
Absence of a value. Causes `NullPointerException` if not handled. Always check `if (object != null)` before using.

---

## O

**OkHttp**
HTTP client library for Java/Android. Used internally by Retrofit for making network requests.

**onActivityResult()**
Callback method in Activity that receives results from other activities (camera, gallery). Being replaced by Activity Result API but still widely used.

**Offline Mode**
LeafGuard AI feature where prediction runs on-device using TensorFlow Lite instead of uploading to backend. Works without internet.

---

## P

**Package**
Namespace for organizing Java classes. Example: `com.example.leafguard.activities`. Defined at top of Java files with `package` keyword.

**PendingIntent**
Intent that can be executed by another application at a future time. Used with notifications to open app when tapped.

**Permission**
User authorization required to access sensitive device features (camera, location, storage). Declared in AndroidManifest, requested at runtime.

**PIL (Python Imaging Library) / Pillow**
Python library for image processing. Used in FastAPI backend to load and preprocess images.

**PlantVillage**
Public dataset of plant leaf images labeled with diseases. Commonly used for training plant disease models.

**POST Request**
HTTP method for sending data to a server. Used to upload images to FastAPI backend.

**Preprocessing**
Transforming raw data into format required by ML model. For images: resize to 224×224, normalize pixel values to 0-1 or -1 to 1.

**Primary Key**
Unique identifier for database rows. In Room Entity: field annotated with `@PrimaryKey(autoGenerate = true)`.

**ProgressBar**
Android UI component showing loading state. Set visibility to `View.VISIBLE` during operations, `View.GONE` when done.

---

## Q

**Query**
Request to retrieve data from database. In Room: method annotated with `@Query("SELECT * FROM scans")`.

**Quantization**
ML model optimization technique that reduces precision (float32 → int8) to decrease model size and improve inference speed.

---

## R

**RecyclerView**
Efficient Android UI component for displaying large lists. Uses ViewHolder pattern for performance. Requires Adapter class.

**Retrofit**
Type-safe HTTP client library for Android. Simplifies API calls with interface definitions and annotations (@GET, @POST, etc.).

**REST (Representational State Transfer)**
Architecture style for web APIs. Uses standard HTTP methods (GET, POST, PUT, DELETE) and stateless requests.

**Room**
Android library (part of Android Jetpack) that provides abstraction layer over SQLite. Makes database operations easier and type-safe.

**Runtime Permission**
Permission that must be requested while app is running (not just in Manifest). Required for dangerous permissions like CAMERA, LOCATION.

---

## S

**SDK (Software Development Kit)**
See "Android SDK" above.

**Singleton**
Design pattern ensuring a class has only one instance. Used for RetrofitClient and AppDatabase to avoid creating multiple instances.

**SQLite**
Lightweight relational database engine built into Android. Room is an abstraction on top of SQLite.

**String Resource**
Text stored in `res/values/strings.xml` instead of hardcoded in Java. Referenced with `R.string.string_name` or `getString(R.string.string_name)`.

---

## T

**TensorFlow**
Open-source machine learning framework by Google. Used to train models.

**TensorFlow Lite (TFLite)**
Lightweight version of TensorFlow optimized for mobile and embedded devices. Runs on Android without internet.

**Tensor**
Multi-dimensional array. ML models work with tensors. Image input: 4D tensor `[1, 224, 224, 3]` (batch, height, width, channels).

**Toast**
Small popup message displayed briefly at bottom of screen. Created with `Toast.makeText(context, message, Toast.LENGTH_SHORT).show()`.

---

## U

**UI (User Interface)**
Visual elements users interact with. In Android: layouts, buttons, text fields, images defined in XML.

**URI (Uniform Resource Identifier)**
String identifying a resource. Android uses URIs to reference images from camera or gallery.

**Uvicorn**
ASGI server for running FastAPI applications. Command: `uvicorn main:app --host 0.0.0.0 --port 8000`.

---

## V

**Validation**
Process of checking if code meets requirements. In LeafGuard AI: weekly validation checklists with pass/fail criteria.

**ViewHolder**
Design pattern used with RecyclerView. Caches view references to avoid repeated `findViewById()` calls, improving performance.

**Viva**
Oral examination where you explain your project to instructors and answer questions. Part of CSE 2206 assessment.

---

## W

**Wi-Fi**
Wireless networking technology. Android app and FastAPI backend must be on same Wi-Fi network for local testing.

**Wrapper**
Code that encapsulates another library or system to simplify its use. Gradle Wrapper ensures consistent Gradle version across machines.

---

## X

**XML (eXtensible Markup Language)**
Markup language for storing and transporting data. Used in Android for layouts, manifest, and disease library file.

**XmlPullParser**
Android API for parsing XML files. Used to read `disease_library.xml` and extract disease information.

---

## Common Acronyms Reference

| Acronym | Full Form | Meaning |
|---------|-----------|---------|
| APK | Android Package | Installable Android app file |
| API | Application Programming Interface | Interface for software to interact |
| AVD | Android Virtual Device | Android emulator |
| CRUD | Create, Read, Update, Delete | Basic database operations |
| DAO | Data Access Object | Database interface in Room |
| GPS | Global Positioning System | Location technology |
| HTTP | Hypertext Transfer Protocol | Web communication protocol |
| IDE | Integrated Development Environment | Software for development (Android Studio) |
| JSON | JavaScript Object Notation | Data format |
| ML | Machine Learning | AI technique |
| REST | Representational State Transfer | API architecture style |
| SDK | Software Development Kit | Development tools |
| SQLite | Structured Query Language Lite | Embedded database |
| TFLite | TensorFlow Lite | Mobile ML framework |
| UI | User Interface | Visual elements |
| URI | Uniform Resource Identifier | Resource address |
| UX | User Experience | How users experience app |
| XML | eXtensible Markup Language | Markup language |

---

## Android Component Lifecycle Terms

**onCreate()**
Method called when Activity is first created. Initialize UI and data here.

**onStart()**
Method called when Activity becomes visible to user.

**onResume()**
Method called when Activity starts interacting with user.

**onPause()**
Method called when Activity is partially obscured (dialog over it).

**onStop()**
Method called when Activity is no longer visible.

**onDestroy()**
Method called before Activity is destroyed. Clean up resources here.

---

## Network Terms

**Base URL**
Root address of API (e.g., `http://192.168.1.5:8000`).

**Endpoint**
Specific path on server (e.g., `/predict`).

**Full URL**
Base URL + Endpoint = `http://192.168.1.5:8000/predict`.

**Request**
Data sent from client to server.

**Response**
Data sent from server to client.

**Status Code**
HTTP response code indicating result (200 = success, 404 = not found, 500 = server error).

**Timeout**
Maximum time to wait for response before giving up.

---

## Machine Learning Terms

**Training**
Process of teaching model to recognize patterns using labeled data. (You won't train from scratch; you'll use a pre-trained model.)

**Inference**
Using trained model to make predictions on new data.

**Input**
Data fed into model (preprocessed image).

**Output**
Prediction from model (array of probabilities for each class).

**Class**
Category the model can predict (e.g., "Tomato Early Blight" is one class).

**Epoch**
One complete pass through training dataset. (Training term; not used during inference.)

**Accuracy**
Percentage of correct predictions. (Reported in model documentation.)

**Loss**
Measure of model error during training. (Training term.)

---

## File System Terms

**Absolute Path**
Complete path from root directory (e.g., `/home/user/project/file.txt`).

**Relative Path**
Path relative to current directory (e.g., `./assets/model.tflite`).

**Assets**
Read-only files packaged with app (`app/src/main/assets/`).

**Internal Storage**
Private storage accessible only to your app.

**External Storage**
Shared storage accessible to other apps (requires permission).

---

## Debugging Terms

**Breakpoint**
Marker that pauses code execution at specific line for inspection.

**Stack Trace**
List of method calls leading to an error. Shows where exception occurred.

**Logcat**
Android Studio panel showing system and app logs.

**Log Levels**
`Log.v()` Verbose, `Log.d()` Debug, `Log.i()` Info, `Log.w()` Warning, `Log.e()` Error.

---

## When to Refer to This Glossary

1. **During weekly readings**: Look up unfamiliar terms immediately
2. **While coding**: Reference when encountering unknown APIs or concepts
3. **During viva prep**: Review to ensure you can define all terms
4. **When writing report**: Use correct terminology consistently

---

**This glossary is your companion for all 12 weeks. Bookmark it and refer to it often.**

**Next: Start Week 01 by opening `roadmap/week-01-project-understanding/README.md`**
