package com.leafguard

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.leafguard.databinding.ActivitySettingsBinding

/**
 * Kotlin twin of SettingsActivity.java.
 *
 * Allows users to configure app behaviour:
 *   1. Backend URL    — base URL for the FastAPI server (Cloud mode)
 *   2. Confidence Threshold — minimum confidence (0–100%) to show a prediction
 *   3. App version    — read-only, from PackageManager
 *
 * All preferences are stored in the default SharedPreferences file, accessible
 * throughout the app via PreferenceManager.getDefaultSharedPreferences(context).
 *
 * SharedPreferences keys are declared as companion-object constants so other
 * classes can read preferences without hard-coding string literals.
 */
class SettingsActivity : AppCompatActivity() {

    companion object {
        /** SharedPreferences key: FastAPI backend base URL. */
        const val PREF_BACKEND_URL = "pref_backend_url"

        /** SharedPreferences key: minimum confidence threshold (int, 0–100). */
        const val PREF_CONFIDENCE_THRESHOLD = "pref_confidence_threshold"

        /** Default FastAPI backend URL (localhost for emulator, 10.0.2.2 = host machine). */
        const val DEFAULT_BACKEND_URL = "http://10.0.2.2:8000"

        /** Default minimum confidence threshold (50%). */
        const val DEFAULT_CONFIDENCE_THRESHOLD = 50
    }

    private var binding: ActivitySettingsBinding? = null
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        this.binding = binding
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener { finish() }

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        loadCurrentSettings()
        setupListeners()
    }

    /**
     * Read stored preferences and populate all UI controls.
     * Called once in onCreate — any prior user values are reflected immediately.
     */
    private fun loadCurrentSettings() {
        val binding = binding ?: return

        // Backend URL
        val savedUrl = prefs.getString(PREF_BACKEND_URL, DEFAULT_BACKEND_URL)
        binding.editTextBackendUrl.setText(savedUrl)

        // Confidence threshold
        val savedThreshold = prefs.getInt(PREF_CONFIDENCE_THRESHOLD, DEFAULT_CONFIDENCE_THRESHOLD)
        binding.seekBarConfidence.progress = savedThreshold
        binding.textConfidenceValue.text =
            getString(R.string.settings_confidence_value_format, savedThreshold)

        // App version (read-only — resolved at runtime from the package manager)
        try {
            val versionName = packageManager.getPackageInfo(packageName, 0).versionName
            binding.textAppVersion.text =
                getString(R.string.settings_version_format, versionName)
        } catch (e: Exception) {
            binding.textAppVersion.text =
                getString(R.string.settings_version_format, "1.0.0")
        }
    }

    /**
     * Attach listeners that persist changes to SharedPreferences immediately.
     * Using apply() (asynchronous) instead of commit() (synchronous) avoids
     * blocking the main thread for disk I/O.
     */
    private fun setupListeners() {
        val binding = binding ?: return

        // Backend URL: save on every text change
        binding.editTextBackendUrl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                prefs.edit()
                    .putString(PREF_BACKEND_URL, s.toString().trim())
                    .apply()
            }
        })

        // Confidence threshold: update label in real-time, save on stop-tracking
        binding.seekBarConfidence.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                this@SettingsActivity.binding?.textConfidenceValue?.text =
                    getString(R.string.settings_confidence_value_format, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) { /* no-op */ }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                prefs.edit()
                    .putInt(PREF_CONFIDENCE_THRESHOLD, seekBar.progress)
                    .apply()
            }
        })

        // Reset to defaults button
        binding.buttonResetDefaults.setOnClickListener { resetToDefaults() }
    }

    private fun resetToDefaults() {
        prefs.edit()
            .putString(PREF_BACKEND_URL, DEFAULT_BACKEND_URL)
            .putInt(PREF_CONFIDENCE_THRESHOLD, DEFAULT_CONFIDENCE_THRESHOLD)
            .apply()

        // Refresh UI controls to reflect reset values
        val binding = binding ?: return
        binding.editTextBackendUrl.setText(DEFAULT_BACKEND_URL)
        binding.seekBarConfidence.progress = DEFAULT_CONFIDENCE_THRESHOLD
        binding.textConfidenceValue.text =
            getString(R.string.settings_confidence_value_format, DEFAULT_CONFIDENCE_THRESHOLD)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
