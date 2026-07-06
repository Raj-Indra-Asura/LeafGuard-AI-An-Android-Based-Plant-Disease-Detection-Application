package com.leafguard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.leafguard.database.AppDatabase;
import com.leafguard.databinding.ActivityMainBinding;
import com.leafguard.ui.BottomNav;
import com.leafguard.utils.NotificationHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Java twin of MainActivity.kt.
 *
 * Home dashboard — the app's launcher screen and the "Home" tab of the
 * bottom navigation bar.
 *
 * Image capture and disease detection now live in ScanActivity (opened via
 * the "Start Scanning" button below). This Activity only shows summary
 * cards and shortcuts to the other tabs, matching the dashboard-style home
 * screen design.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActivityResultLauncher<String[]> permissionLauncher;
    private final ExecutorService historyExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NotificationHelper.createChannel(this);

        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> { /* Home only asks for the notification permission; result is not needed. */ }
        );
        requestNotificationPermissionIfNeeded();

        BottomNav.setup(this, binding.bottomNavigation, R.id.nav_home);
        setupDashboard();
    }

    private void setupDashboard() {
        binding.buttonStartScanning.setOnClickListener(view ->
                startActivity(new Intent(this, ScanActivity.class)));
        binding.cardHistory.setOnClickListener(view ->
                startActivity(new Intent(this, HistoryActivity.class)));
        binding.cardLibrary.setOnClickListener(view ->
                startActivity(new Intent(this, DiseaseLibraryActivity.class)));
        binding.rowCloudArchitecture.setOnClickListener(view ->
                showInfoDialog(R.string.cloud_architecture_row, R.string.cloud_mode_description));
        binding.rowOnDeviceTflite.setOnClickListener(view ->
                showInfoDialog(R.string.ondevice_tflite_row, R.string.offline_mode_description));
        binding.textLibraryCount.setText(
                getString(R.string.library_count_format, DiseaseLibraryActivity.FALLBACK_DISEASE_COUNT));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshHistoryCount();
    }

    /** Reads the saved scan count from Room every time Home becomes visible. */
    private void refreshHistoryCount() {
        historyExecutor.execute(() -> {
            int count = AppDatabase.getInstance(this).scanDao().getAllScans().size();
            runOnUiThread(() -> {
                if (binding != null) {
                    binding.textHistoryCount.setText(getString(R.string.history_count_format, count));
                }
            });
        });
    }

    private void showInfoDialog(int titleRes, int messageRes) {
        new AlertDialog.Builder(this)
                .setTitle(titleRes)
                .setMessage(messageRes)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(new String[]{Manifest.permission.POST_NOTIFICATIONS});
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        historyExecutor.shutdown();
        binding = null;
    }
}

