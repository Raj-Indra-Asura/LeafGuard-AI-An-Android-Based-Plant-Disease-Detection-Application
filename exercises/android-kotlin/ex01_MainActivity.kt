/*
 * Exercise 1: MainActivity navigation (Kotlin twin of ex01_MainActivity.java)
 * Week 2 - Android Basics & UI / Intents and Navigation
 *
 * Starter skeleton. Drop this into the Kotlin Android project at:
 *   android-app-kotlin/app/src/main/java/com/leafguard/MainActivity.kt
 * and complete the TODOs. This file intentionally only sketches the structure
 * so you build the feature yourself (see exercises/android-kotlin/README.md, Ex 1.1-2.1).
 *
 * Goal: wire the four home-screen buttons to open the correct Activities.
 *
 * Verification:
 *   [ ] Each button opens the correct Activity
 *   [ ] Back button returns to MainActivity
 *   [ ] No crash when rapidly tapping buttons
 */
package com.leafguard

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO 1: setContentView(R.layout.activity_main)

        // TODO 2: find the four MaterialButtons by id
        //   (take photo, choose from gallery, view history, disease library)
        // val btnCamera = findViewById<MaterialButton>(R.id.btn_camera)

        // TODO 3: set click listeners that start the right Activity.
        //   For camera/gallery, pass the source as an Intent extra, e.g.
        //   intent.putExtra("source", "camera")
        // btnCamera.setOnClickListener { openScan("camera") }
    }

    // TODO 4: implement helper(s) that build the Intent and call startActivity().
    // private fun openScan(source: String) { ... }
}
