package com.leafguard.ui;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leafguard.AnalyticsActivity;
import com.leafguard.DiseaseLibraryActivity;
import com.leafguard.MainActivity;
import com.leafguard.R;
import com.leafguard.ScanActivity;
import com.leafguard.SettingsActivity;

/**
 * Java twin of BottomNav.kt.
 *
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
public final class BottomNav {

    private BottomNav() {
    }

    public static void setup(@NonNull AppCompatActivity activity, @NonNull BottomNavigationView bottomNav, int currentItemId) {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == currentItemId) {
                return true;
            }

            Class<? extends AppCompatActivity> target;
            if (itemId == R.id.nav_home) {
                target = MainActivity.class;
            } else if (itemId == R.id.nav_scan) {
                target = ScanActivity.class;
            } else if (itemId == R.id.nav_analytics) {
                target = AnalyticsActivity.class;
            } else if (itemId == R.id.nav_library) {
                target = DiseaseLibraryActivity.class;
            } else if (itemId == R.id.nav_about) {
                target = SettingsActivity.class;
            } else {
                return false;
            }

            activity.startActivity(new Intent(activity, target));
            activity.finish();
            return true;
        });
        bottomNav.setSelectedItemId(currentItemId);
    }
}
