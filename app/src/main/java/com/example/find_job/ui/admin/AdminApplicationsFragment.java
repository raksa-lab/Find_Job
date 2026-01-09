package com.example.find_job.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.AdminApplication;

public class AdminApplicationsFragment extends Fragment {

    private RecyclerView rv;
    private TextView tvEmpty;
    private AdminApplicationsViewModel viewModel;
    private AdminApplicationsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View v = inflater.inflate(R.layout.admin_applications, container, false);

        rv = v.findViewById(R.id.rvApplications);
        tvEmpty = v.findViewById(R.id.tvEmptyState);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setHasFixedSize(true);

        adapter = new AdminApplicationsAdapter(
                requireContext(),
                new AdminApplicationsAdapter.OnAction() {
                    @Override
                    public void onApprove(AdminApplication app) {
                        showUpdateDialog(app, "accepted");
                    }

                    @Override
                    public void onReject(AdminApplication app) {
                        showUpdateDialog(app, "rejected");
                    }

                    @Override
                    public void onReply(AdminApplication app) {
                        showReplyDialog(app);
                    }

                    @Override
                    public void onDelete(AdminApplication app) {
                        delete(app.id);
                    }
                }
        );

        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(this)
                .get(AdminApplicationsViewModel.class);

        observeApplications();

        return v;
    }

    // ===============================
    // OBSERVE DATA
    // ===============================
    private void observeApplications() {
        viewModel.getApplications()
                .observe(getViewLifecycleOwner(), list -> {

                    if (list == null || list.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                        return;
                    }

                    tvEmpty.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);
                    adapter.update(list);
                });
    }

    // ===============================
    // ADMIN REPLY
    // ===============================
    private void showReplyDialog(AdminApplication app) {

        EditText etReply = new EditText(requireContext());
        etReply.setHint("Reply to applicant");

        new AlertDialog.Builder(requireContext())
                .setTitle("Reply to Applicant")
                .setView(etReply)
                .setPositiveButton("Send", (d, w) -> {

                    String reply = etReply.getText().toString().trim();

                    if (reply.isEmpty()) {
                        Toast.makeText(
                                requireContext(),
                                "Reply cannot be empty",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    viewModel.replyToUser(app.id, reply)
                            .observe(getViewLifecycleOwner(), ok ->
                                    Toast.makeText(
                                            requireContext(),
                                            ok ? "Reply sent" : "Failed to send reply",
                                            Toast.LENGTH_SHORT
                                    ).show()
                            );
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ===============================
    // UPDATE STATUS
    // ===============================
    private void showUpdateDialog(AdminApplication app, String status) {

        EditText etNote = new EditText(requireContext());
        etNote.setHint("Internal note (optional)");

        new AlertDialog.Builder(requireContext())
                .setTitle("Update Application")
                .setView(etNote)
                .setPositiveButton("Submit", (d, w) ->
                        update(app.id, status, etNote.getText().toString().trim())
                )
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void update(String id, String status, String note) {
        viewModel.updateStatus(id, status, note)
                .observe(getViewLifecycleOwner(), ok ->
                        Toast.makeText(
                                requireContext(),
                                ok ? "Application updated" : "Update failed",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }

    // ===============================
    // DELETE
    // ===============================
    private void delete(String id) {
        viewModel.deleteApplication(id)
                .observe(getViewLifecycleOwner(), ok ->
                        Toast.makeText(
                                requireContext(),
                                ok ? "Application deleted" : "Delete failed",
                                Toast.LENGTH_SHORT
                        ).show()
                );
    }
}
