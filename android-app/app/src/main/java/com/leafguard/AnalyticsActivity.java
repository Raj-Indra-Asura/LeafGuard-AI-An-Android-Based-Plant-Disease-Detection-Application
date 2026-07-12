package com.leafguard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.leafguard.database.AppDatabase;
import com.leafguard.database.ScanRecord;
import com.leafguard.databinding.ActivityAnalyticsBinding;
import com.leafguard.ui.BottomNav;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Java twin of AnalyticsActivity.kt.
 *
 * Analytics tab of the bottom navigation bar.
 *
 * Summarizes the local Room scan history without sending analytics data off-device.
 */
public class AnalyticsActivity extends AppCompatActivity {

    private ActivityAnalyticsBinding binding;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnalyticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNav.setup(this, binding.bottomNavigation, R.id.nav_analytics);
        loadAnalytics();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAnalytics();
    }

    private void loadAnalytics() {
        databaseExecutor.execute(() -> {
            List<ScanRecord> records = AppDatabase.getInstance(getApplicationContext()).scanDao().getAllScans();
            int total = records.size();
            float confidenceSum = 0f;
            Map<String, Integer> counts = new HashMap<>();
            for (ScanRecord record : records) {
                confidenceSum += record.getConfidence();
                counts.put(record.getDiseaseName(), counts.getOrDefault(record.getDiseaseName(), 0) + 1);
            }
            String topDisease = null;
            int topCount = 0;
            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                if (entry.getValue() > topCount) {
                    topDisease = entry.getKey();
                    topCount = entry.getValue();
                }
            }
            int average = total == 0 ? 0 : Math.max(0, Math.min(100, Math.round(confidenceSum * 100f / total)));
            String finalTopDisease = topDisease;
            runOnUiThread(() -> {
                if (binding == null) return;
                binding.textAnalyticsTotal.setText(String.valueOf(total));
                binding.textAnalyticsAverage.setText(total == 0
                        ? getString(R.string.analytics_no_data)
                        : getString(R.string.analytics_confidence_format, average));
                binding.textAnalyticsTopDisease.setText(finalTopDisease == null
                        ? getString(R.string.analytics_no_data) : finalTopDisease);
                binding.textAnalyticsEmpty.setVisibility(total == 0 ? android.view.View.VISIBLE : android.view.View.GONE);
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseExecutor.shutdown();
        binding = null;
    }
}
