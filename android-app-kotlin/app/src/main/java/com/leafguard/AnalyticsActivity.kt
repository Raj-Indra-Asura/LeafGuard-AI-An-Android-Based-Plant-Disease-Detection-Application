package com.leafguard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.leafguard.database.AppDatabase
import com.leafguard.databinding.ActivityAnalyticsBinding
import com.leafguard.ui.setupBottomNav
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

/**
 * Analytics tab of the bottom navigation bar.
 *
 * Summarizes the local Room scan history without sending analytics data off-device.
 */
class AnalyticsActivity : AppCompatActivity() {

    private var binding: ActivityAnalyticsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAnalyticsBinding.inflate(layoutInflater)
        this.binding = binding
        setContentView(binding.root)

        setupBottomNav(binding.bottomNavigation, R.id.nav_analytics)
        loadAnalytics()
    }

    override fun onResume() {
        super.onResume()
        loadAnalytics()
    }

    private fun loadAnalytics() {
        lifecycleScope.launch {
            val records = AppDatabase.getInstance(applicationContext).scanDao().getAllScans()
            val binding = binding ?: return@launch
            binding.textAnalyticsTotal.text = records.size.toString()
            binding.textAnalyticsAverage.text = if (records.isEmpty()) {
                getString(R.string.analytics_no_data)
            } else {
                getString(
                    R.string.analytics_confidence_format,
                    (records.map { it.confidence }.average() * 100.0).roundToInt().coerceIn(0, 100)
                )
            }
            binding.textAnalyticsTopDisease.text = records
                .groupingBy { it.diseaseName }
                .eachCount()
                .maxByOrNull { it.value }
                ?.key
                ?: getString(R.string.analytics_no_data)
            binding.textAnalyticsEmpty.visibility = if (records.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
