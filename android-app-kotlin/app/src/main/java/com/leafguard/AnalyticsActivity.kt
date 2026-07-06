package com.leafguard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leafguard.databinding.ActivityAnalyticsBinding
import com.leafguard.ui.setupBottomNav

/**
 * Analytics tab of the bottom navigation bar.
 *
 * This is a placeholder screen for this build — it only hosts the shared
 * bottom navigation bar so students can navigate to and from it.
 *
 * TODO (future week): render scan-trend and disease-frequency charts here,
 * built from the scan_history Room table (see AppDatabase / ScanDao).
 */
class AnalyticsActivity : AppCompatActivity() {

    private var binding: ActivityAnalyticsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAnalyticsBinding.inflate(layoutInflater)
        this.binding = binding
        setContentView(binding.root)

        setupBottomNav(binding.bottomNavigation, R.id.nav_analytics)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
