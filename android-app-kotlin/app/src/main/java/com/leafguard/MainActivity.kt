package com.leafguard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.leafguard.database.AppDatabase
import com.leafguard.databinding.ActivityMainBinding
import com.leafguard.ui.setupBottomNav
import com.leafguard.utils.NotificationHelper
import kotlinx.coroutines.launch

/**
 * Home dashboard — the app's launcher screen and the "Home" tab of the
 * bottom navigation bar.
 *
 * Image capture and disease detection now live in ScanActivity (opened via
 * the "Start Scanning" button below). This Activity only shows summary
 * cards and shortcuts to the other tabs, matching the dashboard-style home
 * screen design.
 */
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        this.binding = binding
        setContentView(binding.root)

        NotificationHelper.createChannel(this)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { /* Home only asks for the notification permission; result is not needed. */ }
        requestNotificationPermissionIfNeeded()

        setupBottomNav(binding.bottomNavigation, R.id.nav_home)
        setupDashboard()
    }

    private fun setupDashboard() {
        val binding = binding ?: return
        binding.buttonStartScanning.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }
        binding.cardHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        binding.cardLibrary.setOnClickListener {
            startActivity(Intent(this, DiseaseLibraryActivity::class.java))
        }
        binding.rowCloudArchitecture.setOnClickListener {
            showInfoDialog(R.string.cloud_architecture_row, R.string.cloud_mode_description)
        }
        binding.rowOnDeviceTflite.setOnClickListener {
            showInfoDialog(R.string.ondevice_tflite_row, R.string.offline_mode_description)
        }
        binding.textLibraryCount.text =
            getString(R.string.library_count_format, DiseaseLibraryActivity.FALLBACK_DISEASE_COUNT)
    }

    override fun onResume() {
        super.onResume()
        refreshHistoryCount()
    }

    /** Reads the saved scan count from Room every time Home becomes visible. */
    private fun refreshHistoryCount() {
        lifecycleScope.launch {
            val count = AppDatabase.getInstance(this@MainActivity).scanDao().getAllScans().size
            binding?.textHistoryCount?.text = getString(R.string.history_count_format, count)
        }
    }

    private fun showInfoDialog(titleRes: Int, messageRes: Int) {
        AlertDialog.Builder(this)
            .setTitle(titleRes)
            .setMessage(messageRes)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
