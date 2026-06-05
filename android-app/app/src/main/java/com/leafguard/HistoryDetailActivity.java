package com.leafguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leafguard.database.AppDatabase;
import com.leafguard.database.ScanRecord;
import com.leafguard.databinding.ActivityHistoryDetailBinding;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HistoryDetailActivity
 *
 * Shows the full details of a single ScanRecord retrieved from the Room database.
 * Launched from HistoryActivity by passing the record's primary key as an intent extra.
 *
 * Architecture:
 *   HistoryActivity → putExtra(EXTRA_SCAN_ID, record.getId()) → HistoryDetailActivity
 *   → Room DAO.getScanById(id) on background thread → render on UI thread
 */
public class HistoryDetailActivity extends AppCompatActivity {

    /** Intent extra key: the long primary key of the ScanRecord to display. */
    public static final String EXTRA_SCAN_ID = "extra_scan_id";

    private ActivityHistoryDetailBinding binding;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.topAppBar);
        binding.topAppBar.setNavigationOnClickListener(view -> finish());

        long scanId = getIntent().getLongExtra(EXTRA_SCAN_ID, -1L);
        if (scanId == -1L) {
            Toast.makeText(this, R.string.detail_invalid_id_error, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadRecord(scanId);
        setupButtons(scanId);
    }

    /**
     * Query Room for the ScanRecord on a background thread, then render on UI thread.
     *
     * Room disallows main-thread queries to prevent ANR errors.
     * We use a single-thread ExecutorService (same pattern as HistoryActivity).
     */
    private void loadRecord(long scanId) {
        databaseExecutor.execute(() -> {
            ScanRecord record = AppDatabase
                    .getInstance(getApplicationContext())
                    .scanDao()
                    .getScanById(scanId);

            runOnUiThread(() -> {
                if (record == null) {
                    Toast.makeText(this, R.string.detail_record_not_found, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    renderRecord(record);
                }
            });
        });
    }

    private void renderRecord(ScanRecord record) {
        // Disease name and confidence
        binding.textDetailDiseaseName.setText(record.getDiseaseName());
        int confidencePercent = Math.round(record.getConfidence() * 100f);
        binding.textDetailConfidence.setText(
                getString(R.string.confidence_format, confidencePercent));
        binding.progressDetailConfidence.setProgress(confidencePercent);

        // Formatted timestamp
        String formattedDate = DateFormat
                .getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM)
                .format(new Date(record.getTimestamp()));
        binding.textDetailTimestamp.setText(formattedDate);

        // Symptoms / Treatment / Prevention
        binding.textDetailSymptoms.setText(
                record.getSymptoms() != null ? record.getSymptoms()
                        : getString(R.string.placeholder_symptoms));
        binding.textDetailTreatment.setText(
                record.getTreatment() != null ? record.getTreatment()
                        : getString(R.string.placeholder_treatment));
        binding.textDetailPrevention.setText(
                record.getPrevention() != null ? record.getPrevention()
                        : getString(R.string.placeholder_prevention));

        // Show content, hide loading indicator
        binding.scrollDetailContent.setVisibility(View.VISIBLE);
    }

    private void setupButtons(long scanId) {
        binding.buttonDetailShare.setOnClickListener(view -> shareCurrentRecord());
        binding.buttonDetailDelete.setOnClickListener(view -> deleteRecord(scanId));
    }

    private void shareCurrentRecord() {
        String diseaseName = binding.textDetailDiseaseName.getText().toString();
        String symptoms    = binding.textDetailSymptoms.getText().toString();
        String treatment   = binding.textDetailTreatment.getText().toString();

        String shareText = getString(
                R.string.share_result_template,
                diseaseName,
                binding.textDetailConfidence.getText().toString(),
                symptoms,
                treatment,
                binding.textDetailPrevention.getText().toString()
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)));
    }

    private void deleteRecord(long scanId) {
        databaseExecutor.execute(() -> {
            AppDatabase.getInstance(getApplicationContext())
                    .scanDao()
                    .deleteScanById(scanId);

            runOnUiThread(() -> {
                Toast.makeText(this, R.string.detail_deleted_message, Toast.LENGTH_SHORT).show();
                finish();
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
