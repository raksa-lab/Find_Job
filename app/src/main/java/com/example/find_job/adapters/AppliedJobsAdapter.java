package com.example.find_job.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.AppliedJob;
import com.example.find_job.data.models.Job;
import com.example.find_job.ui.application.AppliedJobsViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AppliedJobsAdapter
        extends RecyclerView.Adapter<AppliedJobsAdapter.ViewHolder> {

    // =========================
    // CALLBACK
    // =========================
    public interface OnNoteClickListener {
        void onNoteClick(String applicationId);
    }

    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final List<AppliedJob> list;
    private final AppliedJobsViewModel viewModel;
    private final OnNoteClickListener noteClickListener;

    // =========================
    // CONSTRUCTOR
    // =========================
    public AppliedJobsAdapter(
            Context context,
            LifecycleOwner lifecycleOwner,
            List<AppliedJob> list,
            AppliedJobsViewModel viewModel,
            OnNoteClickListener noteClickListener
    ) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.list = list;
        this.viewModel = viewModel;
        this.noteClickListener = noteClickListener;
    }

    // =========================
    // VIEW HOLDER
    // =========================
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

    // =========================
    // BIND
    // =========================
    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder h,
            int position
    ) {

        AppliedJob applied = list.get(position);
        Job job = applied.job;

        // =========================
        // JOB TITLE
        // =========================
        h.tvJobTitle.setText(
                job != null && job.title != null
                        ? job.title
                        : applied.jobTitle != null
                        ? applied.jobTitle
                        : "—"
        );

        // =========================
        // STATUS
        // =========================
        h.tvStatus.setText(
                applied.status != null
                        ? applied.status.toUpperCase()
                        : "—"
        );

        // =========================
        // COMPANY
        // =========================
        h.tvCompany.setText(
                applied.jobCompany != null
                        ? applied.jobCompany
                        : "—"
        );

        // =========================
        // LOCATION
        // =========================
        h.tvLocation.setText(
                job != null && job.location != null
                        ? job.location
                        : "—"
        );

        // =========================
        // JOB TYPE
        // =========================
        if (job != null && job.type != null) {
            h.tvJobType.setText(job.type.replace("-", " ").toUpperCase());
            h.tvJobType.setVisibility(View.VISIBLE);
        } else {
            h.tvJobType.setVisibility(View.GONE);
        }

        // =========================
        // HIDE UNUSED FIELDS
        // =========================
        h.tvSalary.setVisibility(View.GONE);
        h.tvRequirementsSmall.setVisibility(View.GONE);

        // =========================
        // USER MESSAGE (SAFE ORDER)
        // =========================
        // =========================
// USER MESSAGE (FINAL SAFE ORDER)
// =========================
        String userMessage = null;

// 1️⃣ NEW: notes.userNotes (preferred)
        if (applied.notes != null
                && applied.notes.userNotes != null
                && !applied.notes.userNotes.trim().isEmpty()) {
            userMessage = applied.notes.userNotes;
        }

// 2️⃣ NEW: additionalInfo (object-safe)
        else if (applied.getAdditionalInfoText() != null) {
            userMessage = applied.getAdditionalInfoText();
        }

// 3️⃣ FALLBACK: cover letter
        else if (applied.coverLetter != null
                && !applied.coverLetter.trim().isEmpty()) {
            userMessage = applied.coverLetter;
        }


        h.tvAdminNote.setText(
                userMessage != null
                        ? "Your note: " + userMessage
                        : "Your note: —"
        );
        h.tvAdminNote.setVisibility(View.VISIBLE);

        // =========================
        // WITHDRAW BUTTON
        // =========================
        boolean canWithdraw =
                !"ACCEPTED".equalsIgnoreCase(applied.status)
                        && !"REJECTED".equalsIgnoreCase(applied.status);

        h.btnWithdraw.setVisibility(
                canWithdraw ? View.VISIBLE : View.GONE
        );

        h.btnWithdraw.setOnClickListener(v ->
                new AlertDialog.Builder(context)
                        .setTitle("Withdraw Application")
                        .setMessage("Do you want to withdraw this application?")
                        .setPositiveButton("Withdraw", (dialog, which) ->
                                viewModel.delete(applied.id)
                                        .observe(lifecycleOwner, success -> {
                                            if (Boolean.TRUE.equals(success)) {
                                                int pos = h.getAdapterPosition();
                                                if (pos != RecyclerView.NO_POSITION) {
                                                    list.remove(pos);
                                                    notifyItemRemoved(pos);
                                                }
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
                                        })
                        )
                        .setNegativeButton("Cancel", null)
                        .show()
        );

        // =========================
        // OPEN NOTES
        // =========================
        h.itemView.setOnClickListener(v ->
                noteClickListener.onNoteClick(applied.id)
        );
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    // =========================
    // HOLDER
    // =========================
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
            tvRequirementsSmall =
                    itemView.findViewById(R.id.tvRequirementsSmall);
            tvAdminNote = itemView.findViewById(R.id.tvAdminNote);

            btnWithdraw = itemView.findViewById(R.id.btnWithdraw);
        }
    }
}
