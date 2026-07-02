package com.leafguard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.leafguard.database.AppDatabase
import com.leafguard.database.ScanRecord
import com.leafguard.databinding.ActivityResultBinding
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

/**
 * Kotlin twin of ResultActivity.java.
 *
 * Same intent extras keys, same rendering, same share template, and the same
 * save-to-history behavior. Room access uses lifecycleScope coroutines with
 * the suspend DAO instead of the Java app's ExecutorService.
 */
class ResultActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DISEASE_NAME = "extra_disease_name"
        const val EXTRA_CONFIDENCE = "extra_confidence"
        const val EXTRA_SYMPTOMS = "extra_symptoms"
        const val EXTRA_TREATMENT = "extra_treatment"
        const val EXTRA_PREVENTION = "extra_prevention"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }

    private var binding: ActivityResultBinding? = null

    private lateinit var diseaseName: String
    private var confidence = 0f
    private lateinit var symptoms: String
    private lateinit var treatment: String
    private lateinit var prevention: String
    private var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityResultBinding.inflate(layoutInflater)
        this.binding = binding
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener { finish() }

        readIntentExtras()
        renderResult()
        setupButtons()
    }

    private fun readIntentExtras() {
        val intent = intent
        diseaseName = intent.getStringExtra(EXTRA_DISEASE_NAME) ?: getString(R.string.result_unknown_disease)
        confidence = intent.getFloatExtra(EXTRA_CONFIDENCE, 0f)
        symptoms = intent.getStringExtra(EXTRA_SYMPTOMS) ?: getString(R.string.placeholder_symptoms)
        treatment = intent.getStringExtra(EXTRA_TREATMENT) ?: getString(R.string.placeholder_treatment)
        prevention = intent.getStringExtra(EXTRA_PREVENTION) ?: getString(R.string.placeholder_prevention)
        imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
    }

    private fun renderResult() {
        val binding = binding ?: return
        val confidencePercent = (confidence * 100f).roundToInt()
        binding.textDiseaseName.text = diseaseName
        binding.textConfidenceValue.text = getString(R.string.confidence_format, confidencePercent)
        binding.progressConfidence.progress = confidencePercent
        binding.textSymptoms.text = symptoms
        binding.textTreatment.text = treatment
        binding.textPrevention.text = prevention
    }

    private fun setupButtons() {
        val binding = binding ?: return
        binding.buttonShare.setOnClickListener { shareResult() }
        binding.buttonSaveToHistory.setOnClickListener { saveToHistory() }
        binding.buttonBackHome.setOnClickListener { navigateBackHome() }
    }

    private fun shareResult() {
        val shareText = getString(
            R.string.share_result_template,
            diseaseName,
            (confidence * 100f).roundToInt(),
            symptoms,
            treatment,
            prevention
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)))
    }

    private fun saveToHistory() {
        binding?.buttonSaveToHistory?.isEnabled = false
        lifecycleScope.launch {
            val record = ScanRecord(
                diseaseName = diseaseName,
                confidence = confidence,
                symptoms = symptoms,
                treatment = treatment,
                prevention = prevention,
                imageUri = imageUri,
                latitude = 0.0,
                longitude = 0.0,
                timestamp = System.currentTimeMillis()
            )

            AppDatabase.getInstance(applicationContext).scanDao().insertScan(record)

            Toast.makeText(this@ResultActivity, R.string.history_saved_message, Toast.LENGTH_SHORT).show()
            binding?.buttonSaveToHistory?.setText(R.string.saved_to_history_label)
        }
    }

    private fun navigateBackHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
