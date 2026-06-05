package com.leafguard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.leafguard.databinding.ActivitySettingsBinding;

/**
 * SettingsActivity
 *
 * Allows users to configure app behaviour:
 *   1. Backend URL    — base URL for the FastAPI server (Cloud mode)
 *   2. Confidence Threshold — minimum confidence (0–100%) to show a prediction
 *   3. App version    — read-only, from BuildConfig
 *
 * All preferences are stored in the default SharedPreferences file, accessible
 * throughout the app via PreferenceManager.getDefaultSharedPreferences(context).
 *
 * SharedPreferences keys are declared as public constants so other classes
 * can read preferences without hard-coding string literals.
 */
public class SettingsActivity extends AppCompatActivity {

    /** SharedPreferences key: FastAPI backend base URL. */
    public static final String PREF_BACKEND_URL      = "pref_backend_url";

    /** SharedPreferences key: minimum confidence threshold (int, 0–100). */
    public static final String PREF_CONFIDENCE_THRESHOLD = "pref_confidence_threshold";

    /** Default FastAPI backend URL (localhost for emulator, 10.0.2.2 = host machine). */
    public static final String DEFAULT_BACKEND_URL   = "http://10.0.2.2:8000";

    /** Default minimum confidence threshold (50%). */
    public static final int    DEFAULT_CONFIDENCE_THRESHOLD = 50;

    private ActivitySettingsBinding binding;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.topAppBar);
        binding.topAppBar.setNavigationOnClickListener(view -> finish());

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        loadCurrentSettings();
        setupListeners();
    }

    /**
     * Read stored preferences and populate all UI controls.
     * Called once in onCreate — any prior user values are reflected immediately.
     */
    private void loadCurrentSettings() {
        // Backend URL
        String savedUrl = prefs.getString(PREF_BACKEND_URL, DEFAULT_BACKEND_URL);
        binding.editTextBackendUrl.setText(savedUrl);

        // Confidence threshold
        int savedThreshold = prefs.getInt(PREF_CONFIDENCE_THRESHOLD, DEFAULT_CONFIDENCE_THRESHOLD);
        binding.seekBarConfidence.setProgress(savedThreshold);
        binding.textConfidenceValue.setText(
                getString(R.string.settings_confidence_value_format, savedThreshold));

        // App version (read-only — BuildConfig values are set at compile time)
        try {
            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            binding.textAppVersion.setText(
                    getString(R.string.settings_version_format, versionName));
        } catch (Exception e) {
            binding.textAppVersion.setText(
                    getString(R.string.settings_version_format, "1.0.0"));
        }
    }

    /**
     * Attach listeners that persist changes to SharedPreferences immediately.
     * Using apply() (asynchronous) instead of commit() (synchronous) avoids
     * blocking the main thread for disk I/O.
     */
    private void setupListeners() {
        // Backend URL: save on every text change
        binding.editTextBackendUrl.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                prefs.edit()
                        .putString(PREF_BACKEND_URL, s.toString().trim())
                        .apply();
            }
        });

        // Confidence threshold: update label in real-time, save on stop-tracking
        binding.seekBarConfidence.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.textConfidenceValue.setText(
                        getString(R.string.settings_confidence_value_format, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { /* no-op */ }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                prefs.edit()
                        .putInt(PREF_CONFIDENCE_THRESHOLD, seekBar.getProgress())
                        .apply();
            }
        });

        // Reset to defaults button
        binding.buttonResetDefaults.setOnClickListener(view -> resetToDefaults());
    }

    private void resetToDefaults() {
        prefs.edit()
                .putString(PREF_BACKEND_URL, DEFAULT_BACKEND_URL)
                .putInt(PREF_CONFIDENCE_THRESHOLD, DEFAULT_CONFIDENCE_THRESHOLD)
                .apply();

        // Refresh UI controls to reflect reset values
        binding.editTextBackendUrl.setText(DEFAULT_BACKEND_URL);
        binding.seekBarConfidence.setProgress(DEFAULT_CONFIDENCE_THRESHOLD);
        binding.textConfidenceValue.setText(
                getString(R.string.settings_confidence_value_format, DEFAULT_CONFIDENCE_THRESHOLD));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
