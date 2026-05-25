# LeafGuard AI - Android Application Setup Guide

## Overview
This guide provides complete instructions for setting up, developing, and running the LeafGuard AI Android application. This app uses camera input or gallery images to detect plant diseases using a TensorFlow Lite machine learning model.

## Prerequisites
- **Android Studio**: Arctic Fox (2020.3.1) or later (recommended: latest stable version)
- **Java Development Kit (JDK)**: JDK 11 or higher
- **Android SDK**: API Level 24 (Android 7.0) minimum, API Level 33+ recommended
- **Physical Android Device** or **Emulator** with camera support
- **Minimum 8GB RAM** on development machine

## Project Structure

When you create the Android project in Week 2, you'll have this structure:

```
android-app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/leafguard/
│   │   │   │   ├── MainActivity.java          # Main entry point
│   │   │   │   ├── CameraActivity.java        # Camera capture (Week 3)
│   │   │   │   ├── ImagePreviewActivity.java  # Image preview (Week 4)
│   │   │   │   ├── ResultActivity.java        # Display results (Week 5)
│   │   │   │   ├── HistoryActivity.java       # Detection history (Week 8)
│   │   │   │   ├── model/
│   │   │   │   │   ├── DiseaseClassifier.java # ML model interface (Week 6)
│   │   │   │   │   └── ModelLoader.java       # TFLite model loader (Week 6)
│   │   │   │   ├── network/
│   │   │   │   │   ├── ApiClient.java         # Retrofit client (Week 9)
│   │   │   │   │   └── ApiService.java        # API endpoints (Week 9)
│   │   │   │   ├── database/
│   │   │   │   │   ├── AppDatabase.java       # Room database (Week 8)
│   │   │   │   │   ├── DetectionDao.java      # Data access object (Week 8)
│   │   │   │   │   └── DetectionEntity.java   # Database entity (Week 8)
│   │   │   │   └── utils/
│   │   │   │       ├── ImageUtils.java        # Image processing utilities
│   │   │   │       └── PermissionUtils.java   # Runtime permissions
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml      # Main screen UI
│   │   │   │   │   ├── activity_camera.xml    # Camera screen UI
│   │   │   │   │   ├── activity_result.xml    # Results screen UI
│   │   │   │   │   └── activity_history.xml   # History screen UI
│   │   │   │   ├── drawable/                  # Icons and images
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml            # String resources
│   │   │   │   │   ├── colors.xml             # Color definitions
│   │   │   │   │   └── themes.xml             # App themes
│   │   │   │   └── xml/
│   │   │   │       └── network_security_config.xml # Network config
│   │   │   ├── assets/
│   │   │   │   ├── model.tflite               # TensorFlow Lite model (Week 6)
│   │   │   │   └── labels.txt                 # Disease labels (Week 6)
│   │   │   └── AndroidManifest.xml            # App manifest
│   │   └── androidTest/                       # Instrumented tests (Week 11)
│   └── build.gradle                           # App-level build config
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle                               # Project-level build config
├── settings.gradle                            # Project settings
└── gradle.properties                          # Gradle properties
```

**Note**: The `app/`, `gradle/`, and build configuration files will be automatically generated when you create the Android project in Week 2. This README serves as your reference guide.

## Week-by-Week Setup Instructions

### Week 2: Create Android Project

1. **Open Android Studio**
2. **Create New Project**:
   - Select "Empty Activity"
   - **Name**: LeafGuard AI
   - **Package name**: `com.leafguard`
   - **Save location**: This `android-app` folder
   - **Language**: Java
   - **Minimum SDK**: API 24 (Android 7.0)
   - Click "Finish"

3. **Initial Project Configuration**:

   Edit `app/build.gradle` (Module-level):
   ```gradle
   plugins {
       id 'com.android.application'
   }

   android {
       namespace 'com.leafguard'
       compileSdk 34

       defaultConfig {
           applicationId "com.leafguard"
           minSdk 24
           targetSdk 34
           versionCode 1
           versionName "1.0"

           testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
       }

       buildTypes {
           release {
               minifyEnabled false
               proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
           }
       }

       compileOptions {
           sourceCompatibility JavaVersion.VERSION_11
           targetCompatibility JavaVersion.VERSION_11
       }

       buildFeatures {
           viewBinding true
       }
   }

   dependencies {
       implementation 'androidx.appcompat:appcompat:1.6.1'
       implementation 'com.google.android.material:material:1.11.0'
       implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

       testImplementation 'junit:junit:4.13.2'
       androidTestImplementation 'androidx.test.ext:junit:1.1.5'
       androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
   }
   ```

4. **Update `AndroidManifest.xml`**:
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <manifest xmlns:android="http://schemas.android.com/apk/res/android">

       <uses-permission android:name="android.permission.INTERNET" />

       <application
           android:allowBackup="true"
           android:icon="@mipmap/ic_launcher"
           android:label="@string/app_name"
           android:roundIcon="@mipmap/ic_launcher_round"
           android:supportsRtl="true"
           android:theme="@style/Theme.LeafGuardAI">
           <activity
               android:name=".MainActivity"
               android:exported="true">
               <intent-filter>
                   <intent-filter>
                       <action android:name="android.intent.action.MAIN" />
                       <category android:name="android.intent.category.LAUNCHER" />
                   </intent-filter>
               </intent-filter>
           </activity>
       </application>

   </manifest>
   ```

5. **Sync Gradle** and verify the project builds successfully.

### Week 3: Camera Integration

Add these dependencies to `app/build.gradle`:

```gradle
dependencies {
    // Previous dependencies...

    // Camera
    implementation 'androidx.camera:camera-core:1.3.1'
    implementation 'androidx.camera:camera-camera2:1.3.1'
    implementation 'androidx.camera:camera-lifecycle:1.3.1'
    implementation 'androidx.camera:camera-view:1.3.1'
}
```

Add permissions to `AndroidManifest.xml`:
```xml
<uses-feature android:name="android.hardware.camera" android:required="true" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES"
    android:minSdkVersion="33" />
```

### Week 4: Gallery Integration

Add Activity for File Chooser to `AndroidManifest.xml`:
```xml
<activity
    android:name=".ImagePreviewActivity"
    android:exported="false" />
```

No additional dependencies needed - use built-in `Intent.ACTION_PICK`.

### Week 5-7: TensorFlow Lite Integration

Add dependencies to `app/build.gradle`:

```gradle
dependencies {
    // Previous dependencies...

    // TensorFlow Lite
    implementation 'org.tensorflow:tensorflow-lite:2.14.0'
    implementation 'org.tensorflow:tensorflow-lite-support:0.4.4'
    implementation 'org.tensorflow:tensorflow-lite-gpu:2.14.0' // Optional: GPU acceleration
}
```

Add to `android` block in `app/build.gradle`:
```gradle
android {
    // ... other config

    aaptOptions {
        noCompress "tflite"
    }
}
```

### Week 8: Local Database (Room)

Add dependencies to `app/build.gradle`:

```gradle
dependencies {
    // Previous dependencies...

    // Room Database
    def room_version = "2.6.1"
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-rxjava3:$room_version" // Optional
}
```

### Week 9-10: Backend API Integration

Add dependencies to `app/build.gradle`:

```gradle
dependencies {
    // Previous dependencies...

    // Retrofit for API calls
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'

    // Gson for JSON parsing
    implementation 'com.google.code.gson:gson:2.10.1'
}
```

Create `res/xml/network_security_config.xml` for local testing:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">192.168.1.0/24</domain>
        <domain includeSubdomains="true">localhost</domain>
        <domain includeSubdomains="true">10.0.2.2</domain>
    </domain-config>
</network-security-config>
```

Update `AndroidManifest.xml`:
```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ...>
```

### Week 11: Testing

Add dependencies to `app/build.gradle`:

```gradle
dependencies {
    // Previous dependencies...

    // Testing
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.mockito:mockito-core:5.7.0'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation 'androidx.test:runner:1.5.2'
    androidTestImplementation 'androidx.test:rules:1.5.0'
}
```

## Building and Running

### Run on Emulator

1. **Create AVD (Android Virtual Device)**:
   - Tools → Device Manager
   - Create Device → Select phone (e.g., Pixel 6)
   - Select system image (API 33 or higher recommended)
   - Enable camera in AVD settings

2. **Run Application**:
   - Select AVD from device dropdown
   - Click Run button (green triangle) or `Shift + F10`

### Run on Physical Device

1. **Enable Developer Options** on your Android device:
   - Settings → About Phone
   - Tap "Build Number" 7 times
   - Go back to Settings → System → Developer Options
   - Enable "USB Debugging"

2. **Connect Device**:
   - Connect via USB cable
   - Accept debugging prompt on phone
   - Select device from Android Studio device dropdown
   - Click Run

### Build APK

```bash
# Debug APK
./gradlew assembleDebug

# Release APK (unsigned)
./gradlew assembleRelease

# Output location:
# app/build/outputs/apk/debug/app-debug.apk
# app/build/outputs/apk/release/app-release-unsigned.apk
```

### Install APK via ADB

```bash
# Install on connected device
adb install app/build/outputs/apk/debug/app-debug.apk

# Install and replace existing
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Testing the App

### Connect Phone to Local Backend

1. **Both devices on same WiFi network**
2. **Find your computer's local IP**:
   ```bash
   # Linux/Mac
   ifconfig | grep "inet " | grep -v 127.0.0.1

   # Windows
   ipconfig
   ```

3. **Update API base URL** in your `ApiClient.java`:
   ```java
   private static final String BASE_URL = "http://192.168.1.XXX:8000/";
   ```

4. **For Android Emulator**: Use `http://10.0.2.2:8000/` (special alias for host machine)

### Verify App Permissions

At runtime, the app should request:
- Camera permission (Week 3)
- Storage/Media permission (Week 4)

Test that permission dialogs appear and function correctly.

## Common Errors and Solutions

### 1. Gradle Sync Failed

**Error**: "Could not resolve dependencies"

**Solutions**:
- Check internet connection
- Update `repositories` in project-level `build.gradle`:
  ```gradle
  repositories {
      google()
      mavenCentral()
  }
  ```
- File → Invalidate Caches → Invalidate and Restart

### 2. Minimum SDK Version Error

**Error**: "Call requires API level XX"

**Solutions**:
- Wrap code in version check:
  ```java
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      // Code requiring API 33+
  }
  ```
- Or increase `minSdk` in `build.gradle`

### 3. TensorFlow Lite Model Not Found

**Error**: "model.tflite not found"

**Solutions**:
- Ensure model file is in `app/src/main/assets/`
- Check file name matches code
- Clean and rebuild project: Build → Clean Project → Rebuild Project

### 4. Camera Permission Denied

**Error**: Camera doesn't open

**Solutions**:
- Check `AndroidManifest.xml` has camera permissions
- Implement runtime permission request:
  ```java
  if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
          != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[]{Manifest.permission.CAMERA},
          CAMERA_PERMISSION_CODE);
  }
  ```

### 5. Network Request Fails (Cleartext Traffic)

**Error**: "Cleartext HTTP traffic not permitted"

**Solutions**:
- Add `network_security_config.xml` (see Week 9-10 section)
- Or use HTTPS for production

### 6. Out of Memory Error

**Error**: "OutOfMemoryError" when processing images

**Solutions**:
- Scale down images before processing:
  ```java
  Bitmap scaled = Bitmap.createScaledBitmap(original, 224, 224, true);
  ```
- Recycle bitmaps after use:
  ```java
  bitmap.recycle();
  ```
- Add to `AndroidManifest.xml`:
  ```xml
  <application android:largeHeap="true" ...>
  ```

### 7. ViewBinding Not Generated

**Error**: "Cannot resolve symbol ActivityMainBinding"

**Solutions**:
- Ensure `viewBinding` is enabled in `build.gradle`
- Sync Gradle files
- Rebuild project
- Check layout XML file name matches (activity_main.xml → ActivityMainBinding)

### 8. Device Not Detected (ADB)

**Error**: Android Studio doesn't see your phone

**Solutions**:
```bash
# Restart ADB
adb kill-server
adb start-server

# Check devices
adb devices

# Grant permissions on Linux
sudo usermod -aG plugdev $LOGNAME
```

### 9. Emulator Won't Start

**Error**: Emulator initialization failed

**Solutions**:
- Enable hardware virtualization in BIOS (VT-x/AMD-V)
- For Linux:
  ```bash
  sudo apt-get install qemu-kvm
  sudo adduser $USER kvm
  ```
- Choose x86_64 system image (faster than ARM)
- Allocate more RAM to emulator (4GB+)

## Package Structure Best Practices

```
com.leafguard/
├── activities/        # All Activity classes
├── fragments/         # Fragment classes (if using)
├── adapters/          # RecyclerView adapters
├── model/             # ML model and data classes
├── network/           # API clients and services
├── database/          # Room database components
├── utils/             # Helper classes
└── constants/         # Constants and configs
```

## Debugging Tips

### Enable Logging
```java
import android.util.Log;

private static final String TAG = "MainActivity";
Log.d(TAG, "Debug message");
Log.e(TAG, "Error message", exception);
```

### View Logcat
- Android Studio → Logcat tab
- Filter by package name: `com.leafguard`
- Filter by log level: Debug, Info, Warn, Error

### Debug Network Requests
Add OkHttp logging interceptor:
```java
HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
OkHttpClient client = new OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .build();
```

### Inspect Database
- Android Studio → View → Tool Windows → App Inspection
- Select Database Inspector
- View Room database tables in real-time

## Performance Optimization

1. **Image Loading**: Use Glide or Picasso for efficient image loading
2. **Background Tasks**: Use WorkManager for long-running tasks
3. **Memory Leaks**: Use LeakCanary library to detect leaks
4. **ProGuard**: Enable for release builds to shrink APK size

## Resources

- [Android Developers Official Guide](https://developer.android.com/guide)
- [CameraX Documentation](https://developer.android.com/training/camerax)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [TensorFlow Lite Android Guide](https://www.tensorflow.org/lite/guide/android)
- [Retrofit Documentation](https://square.github.io/retrofit/)

## Next Steps

After setting up the Android project:
1. Follow Week 2 syllabus to create basic UI
2. Implement camera functionality in Week 3
3. Add gallery selection in Week 4
4. Integrate TensorFlow Lite model in Weeks 5-7
5. Set up local database in Week 8
6. Connect to backend API in Weeks 9-10

---

**Note**: This is a learning project. Start simple, test frequently, and build incrementally. Don't try to implement everything at once!
