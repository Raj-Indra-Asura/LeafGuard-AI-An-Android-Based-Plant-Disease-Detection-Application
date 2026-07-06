package com.leafguard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.leafguard.databinding.ActivityAnalyticsBinding;
import com.leafguard.ui.BottomNav;

/**
 * Java twin of AnalyticsActivity.kt.
 *
 * Analytics tab of the bottom navigation bar.
 *
 * This is a placeholder screen for this build — it only hosts the shared
 * bottom navigation bar so students can navigate to and from it.
 *
 * TODO (future week): render scan-trend and disease-frequency charts here,
 * built from the scan_history Room table (see AppDatabase / ScanDao).
 */
public class AnalyticsActivity extends AppCompatActivity {

    private ActivityAnalyticsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnalyticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNav.setup(this, binding.bottomNavigation, R.id.nav_analytics);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
