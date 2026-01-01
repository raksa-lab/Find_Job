package com.example.find_job.ui.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.Auth.LoginActivity;
import com.example.find_job.R;
import com.example.find_job.adapters.AppliedJobsAdapter;
import com.example.find_job.utils.SessionManager;

public class AppliedJobsFragment extends Fragment {

    private RecyclerView rv;
    private TextView tvEmpty;
    private AppliedJobsViewModel viewModel;

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

        observeData();

        return view;
    }

    private void observeData() {
        viewModel.getAppliedJobs()
                .observe(getViewLifecycleOwner(), list -> {

                    if (list == null || list.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                        return;
                    }

                    tvEmpty.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);

                    AppliedJobsAdapter adapter =
                            new AppliedJobsAdapter(
                                    requireContext(),
                                    list, viewModel
                            );

                    rv.setAdapter(adapter);
                });
    }
}
