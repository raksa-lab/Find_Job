package com.example.find_job.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.AppliedJob;
import com.example.find_job.data.models.Job;
import com.example.find_job.ui.application.AppliedJobsViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AppliedJobsAdapter
        extends RecyclerView.Adapter<AppliedJobsAdapter.ViewHolder> {

    private final Context context;
    private final List<AppliedJob> list;
    private final AppliedJobsViewModel viewModel;

    public AppliedJobsAdapter(
            Context context,
            List<AppliedJob> list,
            AppliedJobsViewModel viewModel
    ) {
        this.context = context;
        this.list = list;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_applied_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder h,
            int position
    ) {

        AppliedJob applied = list.get(position);
        Job job = applied.job;

        if (job == null) return;

        h.tvTitle.setText(job.title);
        h.tvCompany.setText(job.company);
        h.tvLocation.setText(job.location);
        h.tvStatus.setText(applied.status.toUpperCase());

        // ===== VISIBILITY LOGIC =====
        String status = applied.status.toLowerCase();

        boolean canDelete =
                status.equals("applied")
                        || status.equals("pending")
                        || status.equals("submitted");

        h.btnWithdraw.setVisibility(
                canDelete ? View.VISIBLE : View.GONE
        );

        // ===== DELETE ACTION =====
        h.btnWithdraw.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Application")
                    .setMessage("This will permanently delete your application. Continue?")
                    .setPositiveButton("Delete", (dialog, which) -> {

                        LiveData<Boolean> liveData =
                                viewModel.delete(applied.id);

                        Observer<Boolean> observer = new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean success) {
                                liveData.removeObserver(this);

                                if (Boolean.TRUE.equals(success)) {
                                    list.remove(h.getAdapterPosition());
                                    notifyItemRemoved(h.getAdapterPosition());

                                    Toast.makeText(
                                            context,
                                            "Application deleted",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                } else {
                                    Toast.makeText(
                                            context,
                                            "Delete failed",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        };

                        liveData.observeForever(observer);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvCompany, tvLocation, tvStatus;
        MaterialButton btnWithdraw;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnWithdraw = itemView.findViewById(R.id.btnWithdraw);
        }
    }
}
