/*
 * Exercise 2: ScanActivity - camera & gallery capture (Kotlin twin of ex02_ScanActivity.java)
 * Week 3 - Camera & Gallery
 *
 * Starter skeleton. Drop this into the Kotlin Android project at:
 *   android-app-kotlin/app/src/main/java/com/leafguard/ScanActivity.kt
 * and complete the TODOs (see exercises/android-kotlin/README.md, Ex 2.2-3.3).
 *
 * Goal: let the user capture a photo or pick from the gallery, show it in an
 * ImageView, then enable the "Analyze" button.
 *
 * Key concepts: runtime permissions, FileProvider, ActivityResultLauncher
 * (use the modern API, NOT the deprecated startActivityForResult).
 *
 * Verification:
 *   [ ] Camera permission requested at runtime and denial handled gracefully
 *   [ ] Captured/selected image displays in the ImageView
 *   [ ] Analyze button starts disabled, enabled only after an image is ready
 */
package com.leafguard

import android.net.Uri
import android.os.Bundle

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ScanActivity : AppCompatActivity() {

    private var currentImageUri: Uri? = null

    // TODO 1: register a launcher for taking a picture (TakePicture contract)
    //   and one for picking an image from the gallery (GetContent contract).
    // private val cameraLauncher: ActivityResultLauncher<Uri> = ...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO 2: setContentView(R.layout.activity_scan)

        // TODO 3: read the "source" extra and either launch the camera
        //   (after checking CAMERA permission) or open the gallery picker.

        // TODO 4: keep the Analyze button disabled until onImageReady() runs.
    }

    /** Call this once an image Uri is available from camera or gallery. */
    private fun onImageReady(imageUri: Uri) {
        this.currentImageUri = imageUri
        // TODO 5: imageView.setImageURI(imageUri)
        // TODO 6: btnAnalyze.isEnabled = true
    }

    // TODO 7: build a content:// Uri via FileProvider for the camera output,
    //   then start analysis (call backend in Week 5 or TFLite in Week 9).
}
