package com.leafguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leafguard.database.AppDatabase;
import com.leafguard.database.ScanRecord;
import com.leafguard.databinding.ActivityHistoryBinding;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.topAppBar);
        binding.topAppBar.setNavigationOnClickListener(view -> finish());

        adapter = new HistoryAdapter(
                new ArrayList<>(),
                record -> {
                    Intent intent = new Intent(this, HistoryDetailActivity.class);
                    intent.putExtra(HistoryDetailActivity.EXTRA_SCAN_ID, record.getId());
                    startActivity(intent);
                },
                this::deleteRecord
        );

        binding.recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerHistory.setAdapter(adapter);

        loadHistory();
    }

    private void loadHistory() {
        databaseExecutor.execute(() -> {
            List<ScanRecord> scans = AppDatabase.getInstance(getApplicationContext()).scanDao().getAllScans();
            runOnUiThread(() -> renderHistory(scans));
        });
    }

    private void renderHistory(List<ScanRecord> scans) {
        adapter.submitList(scans);
        boolean hasItems = !scans.isEmpty();
        binding.recyclerHistory.setVisibility(hasItems ? View.VISIBLE : View.GONE);
        binding.textEmptyState.setVisibility(hasItems ? View.GONE : View.VISIBLE);
    }

    private void deleteRecord(ScanRecord record) {
        databaseExecutor.execute(() -> {
            AppDatabase database = AppDatabase.getInstance(getApplicationContext());
            database.scanDao().deleteScanById(record.getId());
            List<ScanRecord> scans = database.scanDao().getAllScans();
            runOnUiThread(() -> {
                Toast.makeText(this, getString(R.string.delete_stub_message, record.getDiseaseName()), Toast.LENGTH_SHORT).show();
                renderHistory(scans);
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseExecutor.shutdown();
        binding = null;
    }

    private static class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

        interface OnItemClickListener {
            void onItemSelected(ScanRecord record);
        }

        interface OnDeleteClickListener {
            void onDeleteRequested(ScanRecord record);
        }

        private final List<ScanRecord> items;
        private final OnItemClickListener onItemClickListener;
        private final OnDeleteClickListener onDeleteClickListener;

        HistoryAdapter(List<ScanRecord> items, OnItemClickListener onItemClickListener, OnDeleteClickListener onDeleteClickListener) {
            this.items = items;
            this.onItemClickListener = onItemClickListener;
            this.onDeleteClickListener = onDeleteClickListener;
        }

        void submitList(List<ScanRecord> newItems) {
            items.clear();
            items.addAll(newItems);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_scan_history, parent, false);
            return new HistoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
            holder.bind(items.get(position), onItemClickListener, onDeleteClickListener);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class HistoryViewHolder extends RecyclerView.ViewHolder {

            private final TextView textDisease;
            private final TextView textConfidence;
            private final TextView textTimestamp;
            private final ImageButton buttonDelete;

            HistoryViewHolder(@NonNull View itemView) {
                super(itemView);
                textDisease = itemView.findViewById(R.id.textHistoryDiseaseName);
                textConfidence = itemView.findViewById(R.id.textHistoryConfidence);
                textTimestamp = itemView.findViewById(R.id.textHistoryTimestamp);
                buttonDelete = itemView.findViewById(R.id.buttonDeleteHistoryItem);
            }

            void bind(ScanRecord record, OnItemClickListener itemListener, OnDeleteClickListener deleteListener) {
                textDisease.setText(record.getDiseaseName());
                textConfidence.setText(itemView.getContext().getString(
                        R.string.history_confidence_format,
                        record.getConfidence() * 100f
                ));
                textTimestamp.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                        .format(new Date(record.getTimestamp())));
                itemView.setOnClickListener(view -> itemListener.onItemSelected(record));
                buttonDelete.setOnClickListener(view -> deleteListener.onDeleteRequested(record));
            }
        }
    }
}
