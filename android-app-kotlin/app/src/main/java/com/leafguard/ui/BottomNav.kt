package com.leafguard.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leafguard.AnalyticsActivity
import com.leafguard.DiseaseLibraryActivity
import com.leafguard.MainActivity
import com.leafguard.R
import com.leafguard.ScanActivity
import com.leafguard.SettingsActivity

/**
 * Wires up the shared bottom navigation bar (Home / Scan / Analytics / Library / About)
 * that appears on every top-level screen of the app.
 *
 * LeafGuard does not use Fragments or the Jetpack Navigation Component — each tab is
 * a separate Activity. Tapping a tab starts the target Activity and finishes the
 * current one, so the back stack never grows past one screen. The trade-off is that
 * the system Back button exits the app instead of returning to a previous tab. A
 * later week could replace this with a single Activity + Fragments + Navigation
 * Component to give each tab its own back stack.
 */
fun AppCompatActivity.setupBottomNav(bottomNav: BottomNavigationView, currentItemId: Int) {
    bottomNav.setOnItemSelectedListener { item ->
        if (item.itemId == currentItemId) {
            return@setOnItemSelectedListener true
        }
        val target: Class<out AppCompatActivity> = when (item.itemId) {
            R.id.nav_home -> MainActivity::class.java
            R.id.nav_scan -> ScanActivity::class.java
            R.id.nav_analytics -> AnalyticsActivity::class.java
            R.id.nav_library -> DiseaseLibraryActivity::class.java
            R.id.nav_about -> SettingsActivity::class.java
            else -> return@setOnItemSelectedListener false
        }
        startActivity(Intent(this, target))
        finish()
        true
    }
    bottomNav.selectedItemId = currentItemId
}
