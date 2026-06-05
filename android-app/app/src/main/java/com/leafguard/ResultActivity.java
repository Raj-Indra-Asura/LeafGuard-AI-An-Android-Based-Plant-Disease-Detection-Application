package com.leafguard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leafguard.database.AppDatabase;
import com.leafguard.database.ScanRecord;
import com.leafguard.databinding.ActivityResultBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_DISEASE_NAME = "extra_disease_name";
    public static final String EXTRA_CONFIDENCE = "extra_confidence";
    public static final String EXTRA_SYMPTOMS = "extra_symptoms";
    public static final String EXTRA_TREATMENT = "extra_treatment";
    public static final String EXTRA_PREVENTION = "extra_prevention";
    public static final String EXTRA_IMAGE_URI = "extra_image_uri";

    private ActivityResultBinding binding;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    private String diseaseName;
    private float confidence;
    private String symptoms;
    private String treatment;
    private String prevention;
    private String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.topAppBar);
        binding.topAppBar.setNavigationOnClickListener(view -> finish());

        readIntentExtras();
        renderResult();
        setupButtons();
    }

    private void readIntentExtras() {
        Intent intent = getIntent();
        diseaseName = intent.getStringExtra(EXTRA_DISEASE_NAME);
        confidence = intent.getFloatExtra(EXTRA_CONFIDENCE, 0f);
        symptoms = intent.getStringExtra(EXTRA_SYMPTOMS);
        treatment = intent.getStringExtra(EXTRA_TREATMENT);
        prevention = intent.getStringExtra(EXTRA_PREVENTION);
        imageUri = intent.getStringExtra(EXTRA_IMAGE_URI);

        if (diseaseName == null) {
            diseaseName = getString(R.string.result_unknown_disease);
        }
        if (symptoms == null) {
            symptoms = getString(R.string.placeholder_symptoms);
        }
        if (treatment == null) {
            treatment = getString(R.string.placeholder_treatment);
        }
        if (prevention == null) {
            prevention = getString(R.string.placeholder_prevention);
        }
    }

    private void renderResult() {
        int confidencePercent = Math.round(confidence * 100f);
        binding.textDiseaseName.setText(diseaseName);
        binding.textConfidenceValue.setText(getString(R.string.confidence_format, confidencePercent));
        binding.progressConfidence.setProgress(confidencePercent);
        binding.textSymptoms.setText(symptoms);
        binding.textTreatment.setText(treatment);
        binding.textPrevention.setText(prevention);
    }

    private void setupButtons() {
        binding.buttonShare.setOnClickListener(view -> shareResult());
        binding.buttonSaveToHistory.setOnClickListener(view -> saveToHistory());
        binding.buttonBackHome.setOnClickListener(view -> navigateBackHome());
    }

    private void shareResult() {
        String shareText = getString(
                R.string.share_result_template,
                diseaseName,
                Math.round(confidence * 100f),
                symptoms,
                treatment,
                prevention
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)));
    }

    private void saveToHistory() {
        binding.buttonSaveToHistory.setEnabled(false);
        databaseExecutor.execute(() -> {
            ScanRecord record = new ScanRecord();
            record.setDiseaseName(diseaseName);
            record.setConfidence(confidence);
            record.setSymptoms(symptoms);
            record.setTreatment(treatment);
            record.setPrevention(prevention);
            record.setImageUri(imageUri);
            record.setLatitude(0.0);
            record.setLongitude(0.0);
            record.setTimestamp(System.currentTimeMillis());

            AppDatabase.getInstance(getApplicationContext()).scanDao().insertScan(record);

            runOnUiThread(() -> {
                Toast.makeText(this, R.string.history_saved_message, Toast.LENGTH_SHORT).show();
                binding.buttonSaveToHistory.setText(R.string.saved_to_history_label);
            });
        });
    }

    private void navigateBackHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseExecutor.shutdown();
        binding = null;
    }
}
