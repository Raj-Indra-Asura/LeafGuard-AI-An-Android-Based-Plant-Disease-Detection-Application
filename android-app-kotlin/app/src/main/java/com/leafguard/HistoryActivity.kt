package com.leafguard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leafguard.database.AppDatabase
import com.leafguard.database.ScanRecord
import com.leafguard.databinding.ActivityHistoryBinding
import java.text.DateFormat
import java.util.Date
import kotlinx.coroutines.launch

/**
 * Kotlin twin of HistoryActivity.java.
 *
 * Same RecyclerView list of saved scans with delete + detail navigation.
 * Room access uses lifecycleScope coroutines with the suspend DAO instead
 * of the Java app's ExecutorService.
 */
class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHistoryBinding.inflate(layoutInflater)
        this.binding = binding
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        binding.topAppBar.setNavigationOnClickListener { finish() }

        adapter = HistoryAdapter(
            mutableListOf(),
            onItemSelected = { record ->
                val intent = Intent(this, HistoryDetailActivity::class.java)
                intent.putExtra(HistoryDetailActivity.EXTRA_SCAN_ID, record.id)
                startActivity(intent)
            },
            onDeleteRequested = ::deleteRecord
        )

        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerHistory.adapter = adapter

        loadHistory()
    }

    private fun loadHistory() {
        lifecycleScope.launch {
            val scans = AppDatabase.getInstance(applicationContext).scanDao().getAllScans()
            renderHistory(scans)
        }
    }

    private fun renderHistory(scans: List<ScanRecord>) {
        val binding = binding ?: return
        adapter.submitList(scans)
        val hasItems = scans.isNotEmpty()
        binding.recyclerHistory.visibility = if (hasItems) View.VISIBLE else View.GONE
        binding.textEmptyState.visibility = if (hasItems) View.GONE else View.VISIBLE
    }

    private fun deleteRecord(record: ScanRecord) {
        lifecycleScope.launch {
            val database = AppDatabase.getInstance(applicationContext)
            database.scanDao().deleteScanById(record.id)
            val scans = database.scanDao().getAllScans()
            Toast.makeText(
                this@HistoryActivity,
                getString(R.string.history_item_deleted, record.diseaseName),
                Toast.LENGTH_SHORT
            ).show()
            renderHistory(scans)
        }
    }

    override fun onResume() {
        super.onResume()
        loadHistory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private class HistoryAdapter(
        private val items: MutableList<ScanRecord>,
        private val onItemSelected: (ScanRecord) -> Unit,
        private val onDeleteRequested: (ScanRecord) -> Unit
    ) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

        fun submitList(newItems: List<ScanRecord>) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_scan_history, parent, false)
            return HistoryViewHolder(view)
        }

        override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
            holder.bind(items[position], onItemSelected, onDeleteRequested)
        }

        override fun getItemCount(): Int = items.size

        class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val textDisease: TextView = itemView.findViewById(R.id.textHistoryDiseaseName)
            private val textConfidence: TextView = itemView.findViewById(R.id.textHistoryConfidence)
            private val textTimestamp: TextView = itemView.findViewById(R.id.textHistoryTimestamp)
            private val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDeleteHistoryItem)

            fun bind(
                record: ScanRecord,
                onItemSelected: (ScanRecord) -> Unit,
                onDeleteRequested: (ScanRecord) -> Unit
            ) {
                textDisease.text = record.diseaseName
                textConfidence.text = itemView.context.getString(
                    R.string.history_confidence_format,
                    record.confidence * 100f
                )
                textTimestamp.text = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                    .format(Date(record.timestamp))
                itemView.setOnClickListener { onItemSelected(record) }
                buttonDelete.setOnClickListener { onDeleteRequested(record) }
            }
        }
    }
}
