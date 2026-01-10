package com.example.find_job.ui.application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.Auth.LoginActivity;
import com.example.find_job.R;
import com.example.find_job.adapters.AppliedJobsAdapter;
import com.example.find_job.data.models.AppliedJob;
import com.example.find_job.data.models.ApplicationNotesResponse;
import com.example.find_job.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AppliedJobsFragment extends Fragment {

    private RecyclerView rv;
    private TextView tvEmpty;
    private AppliedJobsViewModel viewModel;
    private AppliedJobsAdapter adapter;

    private final List<AppliedJob> appliedJobs = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();

        SessionManager session = new SessionManager(requireContext());
        if (!session.isLoggedIn()) {
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.applied_jobs, container, false);

        rv = view.findViewById(R.id.rvAppliedJobs);
        tvEmpty = view.findViewById(R.id.tvEmptyState);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel = new ViewModelProvider(this)
                .get(AppliedJobsViewModel.class);

        adapter = new AppliedJobsAdapter(
                requireContext(),
                getViewLifecycleOwner(),
                appliedJobs,
                viewModel,
                this::openNotesDialog
        );

        rv.setAdapter(adapter);
        observeData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.reload();
    }

    // =========================
    // DATA
    // =========================
    private void observeData() {
        viewModel.getAppliedJobs()
                .observe(getViewLifecycleOwner(), list -> {

                    Log.d("APPLIED_JOBS",
                            "Received size = " + (list == null ? 0 : list.size()));

                    if (list == null || list.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                        appliedJobs.clear();
                        adapter.notifyDataSetChanged();
                        return;
                    }

                    tvEmpty.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);

                    appliedJobs.clear();
                    appliedJobs.addAll(list);
                    adapter.notifyDataSetChanged();
                });
    }

    // =========================
    // NOTES POPUP (FIXED)
    // =========================
    private void openNotesDialog(String applicationId) {

        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_application_notes, null);

        EditText etUserNote = dialogView.findViewById(R.id.etUserNote);
        TextView tvAdminNotes = dialogView.findViewById(R.id.tvAdminNotes);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Application Notes")
                .setView(dialogView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Close", null)
                .create();

        dialog.show();

        viewModel.getApplicationNotes(applicationId)
                .observe(getViewLifecycleOwner(), response -> {

                    if (response == null || response.getNotes() == null) {
                        tvAdminNotes.setText("No admin notes");
                        return;
                    }

                    // USER NOTE
                    etUserNote.setText(response.getNotes().getUserNotes());

                    // ADMIN NOTES
                    List<AppliedJob.AdminNote> notes =
                            response.getNotes().getAdminNotes();

                    if (notes == null || notes.isEmpty()) {
                        tvAdminNotes.setText("No admin notes");
                    } else {
                        StringBuilder sb = new StringBuilder();

                        for (AppliedJob.AdminNote note : notes) {
                            if (note == null) continue;
                            if (note.isInternal || !note.notifyUser) continue;

                            sb.append("• ")
                                    .append(note.content)
                                    .append("\n\n");
                        }

                        tvAdminNotes.setText(
                                sb.length() > 0
                                        ? sb.toString()
                                        : "No admin notes"
                        );
                    }

                    boolean canEdit = response.canRespond();
                    etUserNote.setEnabled(canEdit);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setEnabled(canEdit);
                });

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    viewModel.updateUserNote(
                            applicationId,
                            etUserNote.getText().toString()
                    );
                    dialog.dismiss();
                });
    }
}
