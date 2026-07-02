package com.leafguard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.leafguard.database.AppDatabase
import com.leafguard.database.ScanRecord
import com.leafguard.databinding.ActivityHistoryDetailBinding
import java.text.DateFormat
import java.util.Date
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

/**
 * Kotlin twin of HistoryDetailActivity.java.
 *
 * Shows the full details of a single ScanRecord retrieved from the Room database.
 * Launched from HistoryActivity by passing the record's primary key as an intent extra.
 *
 * Architecture:
 *   HistoryActivity → putExtra(EXTRA_SCAN_ID, record.id) → HistoryDetailActivity
 *   → suspend DAO.getScanById(id) via lifecycleScope coroutine → render on UI thread
 */
class HistoryDetailActivity : AppCompatActivity() {

    companion object {
        /** Intent extra key: the long primary key of the ScanRecord to display. */
        const val EXTRA_SCAN_ID = "extra_scan_id"
    }

    private var binding: ActivityHistoryDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        this.binding = binding
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener { finish() }

        val scanId = intent.getLongExtra(EXTRA_SCAN_ID, -1L)
        if (scanId == -1L) {
            Toast.makeText(this, R.string.detail_invalid_id_error, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadRecord(scanId)
        setupButtons(scanId)
    }

    /**
     * Query Room for the ScanRecord off the main thread, then render.
     *
     * Room disallows main-thread queries to prevent ANR errors. The suspend
     * DAO function runs on Room's I/O executor automatically (the Kotlin
     * equivalent of the Java app's single-thread ExecutorService pattern).
     */
    private fun loadRecord(scanId: Long) {
        lifecycleScope.launch {
            val record = AppDatabase
                .getInstance(applicationContext)
                .scanDao()
                .getScanById(scanId)

            if (record == null) {
                Toast.makeText(this@HistoryDetailActivity, R.string.detail_record_not_found, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                renderRecord(record)
            }
        }
    }

    private fun renderRecord(record: ScanRecord) {
        val binding = binding ?: return

        // Disease name and confidence
        binding.textDetailDiseaseName.text = record.diseaseName
        val confidencePercent = (record.confidence * 100f).roundToInt()
        binding.textDetailConfidence.text = getString(R.string.confidence_format, confidencePercent)
        binding.progressDetailConfidence.progress = confidencePercent

        // Formatted timestamp
        val formattedDate = DateFormat
            .getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM)
            .format(Date(record.timestamp))
        binding.textDetailTimestamp.text = formattedDate

        // Symptoms / Treatment / Prevention
        binding.textDetailSymptoms.text =
            record.symptoms ?: getString(R.string.placeholder_symptoms)
        binding.textDetailTreatment.text =
            record.treatment ?: getString(R.string.placeholder_treatment)
        binding.textDetailPrevention.text =
            record.prevention ?: getString(R.string.placeholder_prevention)

        // Show content, hide loading indicator
        binding.scrollDetailContent.visibility = View.VISIBLE
    }

    private fun setupButtons(scanId: Long) {
        val binding = binding ?: return
        binding.buttonDetailShare.setOnClickListener { shareCurrentRecord() }
        binding.buttonDetailDelete.setOnClickListener { deleteRecord(scanId) }
    }

    private fun shareCurrentRecord() {
        val binding = binding ?: return
        val diseaseName = binding.textDetailDiseaseName.text.toString()
        val symptoms = binding.textDetailSymptoms.text.toString()
        val treatment = binding.textDetailTreatment.text.toString()

        val shareText = getString(
            R.string.share_result_template,
            diseaseName,
            binding.textDetailConfidence.text.toString(),
            symptoms,
            treatment,
            binding.textDetailPrevention.text.toString()
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)))
    }

    private fun deleteRecord(scanId: Long) {
        lifecycleScope.launch {
            AppDatabase.getInstance(applicationContext)
                .scanDao()
                .deleteScanById(scanId)

            Toast.makeText(this@HistoryDetailActivity, R.string.detail_deleted_message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
