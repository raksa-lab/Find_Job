package com.example.find_job.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.AppliedJob;
import com.example.find_job.ui.application.AppliedJobsViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AppliedJobsAdapter
        extends RecyclerView.Adapter<AppliedJobsAdapter.ViewHolder> {

    public interface OnNoteClickListener {
        void onNoteClick(String applicationId);
    }

    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final List<AppliedJob> list;
    private final AppliedJobsViewModel viewModel;
    private final OnNoteClickListener noteClickListener;

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

        // =========================
        // JOB TITLE
        // =========================
        h.tvJobTitle.setText(
                applied.jobTitle != null
                        ? applied.jobTitle
                        : applied.job != null
                        ? applied.job.title
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
                        : applied.job != null
                        ? applied.job.company
                        : "—"
        );

        // =========================
        // LOCATION
        // =========================
        h.tvLocation.setText(
                applied.job != null && applied.job.location != null
                        ? applied.job.location
                        : "—"
        );

        // =========================
        // HIDE UNUSED FIELDS
        // =========================
        h.tvSalary.setVisibility(View.GONE);
        h.tvJobType.setVisibility(View.GONE);
        h.tvRequirementsSmall.setVisibility(View.GONE);

        // =====================================================
        // ✅ ADMIN NOTE (FIXED – USER VISIBLE ONLY)
        // =====================================================
        String adminNote = applied.getLatestAdminNote();

        h.tvAdminNote.setText(
                adminNote != null
                        ? "Admin note: " + adminNote
                        : "Admin note: —"
        );
        h.tvAdminNote.setVisibility(View.VISIBLE);

        // =========================
        // WITHDRAW BUTTON
        // =========================
        boolean canWithdraw =
                applied.status == null
                        || (!"ACCEPTED".equalsIgnoreCase(applied.status)
                        && !"REJECTED".equalsIgnoreCase(applied.status));

        h.btnWithdraw.setVisibility(canWithdraw ? View.VISIBLE : View.GONE);

        h.btnWithdraw.setOnClickListener(v ->
                new AlertDialog.Builder(context)
                        .setTitle("Withdraw Application")
                        .setMessage("Do you want to withdraw this application?")
                        .setPositiveButton("Withdraw", (d, w) ->
                                viewModel.delete(applied.id)
                                        .observe(lifecycleOwner, ok -> {
                                            if (Boolean.TRUE.equals(ok)) {
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
        // OPEN NOTES POPUP
        // =========================
        h.itemView.setOnClickListener(v ->
                noteClickListener.onNoteClick(applied.id)
        );
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvJobTitle, tvStatus, tvCompany, tvLocation;
        TextView tvSalary, tvJobType, tvRequirementsSmall, tvAdminNote;
        MaterialButton btnWithdraw;

        ViewHolder(@NonNull View v) {
            super(v);
            tvJobTitle = v.findViewById(R.id.tvJobTitle);
            tvStatus = v.findViewById(R.id.tvStatus);
            tvCompany = v.findViewById(R.id.tvCompany);
            tvLocation = v.findViewById(R.id.tvLocation);
            tvSalary = v.findViewById(R.id.tvSalary);
            tvJobType = v.findViewById(R.id.tvJobType);
            tvRequirementsSmall = v.findViewById(R.id.tvRequirementsSmall);
            tvAdminNote = v.findViewById(R.id.tvAdminNote);
            btnWithdraw = v.findViewById(R.id.btnWithdraw);
        }
    }
}
