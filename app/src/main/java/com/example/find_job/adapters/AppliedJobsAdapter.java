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

        /* =========================
           JOB TITLE
           ========================= */
        h.tvJobTitle.setText(
                job != null && job.title != null
                        ? job.title
                        : applied.jobTitle != null
                        ? applied.jobTitle
                        : "—"
        );

        /* =========================
           STATUS
           ========================= */
        h.tvStatus.setText(
                applied.status != null
                        ? applied.status.toUpperCase()
                        : "UNKNOWN"
        );

        /* =========================
           COMPANY
           ========================= */
        h.tvCompany.setText(
                applied.jobCompany != null
                        ? applied.jobCompany
                        : "—"
        );

        /* =========================
           LOCATION
           ========================= */
        h.tvLocation.setText(
                job != null && job.location != null
                        ? job.location
                        : "—"
        );

        /* =========================
           JOB TYPE (AVAILABLE)
           ========================= */
        if (job != null && job.type != null) {
            h.tvJobType.setText(
                    job.type.replace("-", " ").toUpperCase()
            );
            h.tvJobType.setVisibility(View.VISIBLE);
        } else {
            h.tvJobType.setVisibility(View.GONE);
        }

        /* =========================
           SALARY (NOT IN API)
           ========================= */
        h.tvSalary.setVisibility(View.GONE);

        /* =========================
           REQUIREMENTS (NOT IN API)
           ========================= */
        h.tvRequirementsSmall.setVisibility(View.GONE);

        /* =========================
           USER REMARK / ADMIN NOTE
           ========================= */
        if (applied.coverLetter != null && !applied.coverLetter.isEmpty()) {
            h.tvAdminNote.setText("Your note: " + applied.coverLetter);
        } else if (applied.notes != null && !applied.notes.isEmpty()) {
            h.tvAdminNote.setText("Admin note: " + applied.notes);
        } else {
            h.tvAdminNote.setText("No remark");
        }

        /* =========================
           WITHDRAW BUTTON LOGIC
           ========================= */
        String status = applied.status != null
                ? applied.status.toLowerCase()
                : "";

        boolean canDelete =
                status.equals("applied")
                        || status.equals("pending")
                        || status.equals("submitted");

        h.btnWithdraw.setVisibility(
                canDelete ? View.VISIBLE : View.GONE
        );

        /* =========================
           WITHDRAW ACTION
           ========================= */
        h.btnWithdraw.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Withdraw Application")
                    .setMessage("Do you want to withdraw this application?")
                    .setPositiveButton("Withdraw", (dialog, which) -> {

                        LiveData<Boolean> liveData =
                                viewModel.delete(applied.id);

                        Observer<Boolean> observer = new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean success) {
                                liveData.removeObserver(this);

                                if (Boolean.TRUE.equals(success)) {
                                    int pos = h.getAdapterPosition();
                                    list.remove(pos);
                                    notifyItemRemoved(pos);

                                    Toast.makeText(
                                            context,
                                            "Application withdrawn",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                } else {
                                    Toast.makeText(
                                            context,
                                            "Withdraw failed",
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

    /* =========================
       VIEW HOLDER
       ========================= */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvJobTitle, tvStatus, tvCompany, tvLocation;
        TextView tvSalary, tvJobType, tvRequirementsSmall, tvAdminNote;
        MaterialButton btnWithdraw;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvLocation = itemView.findViewById(R.id.tvLocation);

            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvJobType = itemView.findViewById(R.id.tvJobType);
            tvRequirementsSmall = itemView.findViewById(R.id.tvRequirementsSmall);
            tvAdminNote = itemView.findViewById(R.id.tvAdminNote);

            btnWithdraw = itemView.findViewById(R.id.btnWithdraw);
        }
    }
}
