package com.example.find_job.ui.admin;


import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.*;

import com.example.find_job.R;
import com.example.find_job.ui.admin.AdminApplicationsAdapter;
import com.example.find_job.data.models.AdminApplication;

public class AdminApplicationsFragment extends Fragment {

    private RecyclerView rv;
    private TextView tvEmpty;
    private AdminApplicationsViewModel viewModel;

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

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this)
                .get(AdminApplicationsViewModel.class);

        observe();

        return v;
    }

    private void observe() {
        viewModel.getApplications()
                .observe(getViewLifecycleOwner(), list -> {

                    if (list == null || list.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                        return;
                    }

                    tvEmpty.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);

                    rv.setAdapter(new AdminApplicationsAdapter(
                            requireContext(),
                            list,
                            new AdminApplicationsAdapter.OnAction() {

                                @Override
                                public void onApprove(AdminApplication app) {
                                    update(app.id, "accepted");
                                }

                                @Override
                                public void onReject(AdminApplication app) {
                                    update(app.id, "rejected");
                                }

                                @Override
                                public void onDelete(AdminApplication app) {
                                    delete(app.id);
                                }
                            }
                    ));
                });
    }

    private void update(String id, String status) {
        viewModel.updateStatus(id, status)
                .observe(getViewLifecycleOwner(), ok -> {
                    Toast.makeText(
                            getContext(),
                            ok ? "Updated" : "Failed",
                            Toast.LENGTH_SHORT
                    ).show();
                    if (ok) observe();
                });
    }

    private void delete(String id) {
        viewModel.deleteApplication(id)
                .observe(getViewLifecycleOwner(), ok -> {
                    Toast.makeText(
                            getContext(),
                            ok ? "Deleted" : "Delete failed",
                            Toast.LENGTH_SHORT
                    ).show();
                    if (ok) observe();
                });
    }
}
