package com.example.find_job.ui.job_list;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.adapters.JobAdapter;
import com.example.find_job.data.models.Job;
import com.example.find_job.data.repository.JobRepository;
import com.example.find_job.ui.job_detail.JobDetailActivity;
import com.example.find_job.utils.SessionManager;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class JobListFragment extends Fragment {

    private JobListViewModel viewModel;
    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private ChipGroup chipGroup;
    private EditText searchJob;
    private ImageView btnDelete;

    private SessionManager sessionManager;
    private JobRepository jobRepository;

    private final List<Job> allJobs = new ArrayList<>();

    // ===== ADMIN DELETE MODE =====
    private boolean deleteMode = false;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.activity_job_list, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvAllJobs);
        chipGroup = view.findViewById(R.id.category_chips);
        searchJob = view.findViewById(R.id.search_job);
        btnDelete = view.findViewById(R.id.btnDelete);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sessionManager = new SessionManager(requireContext());
        jobRepository = new JobRepository(requireContext());

        // Hide delete button for normal user
        if (!sessionManager.isAdmin()) {
            btnDelete.setVisibility(View.GONE);
        }

        viewModel = new ViewModelProvider(requireActivity())
                .get(JobListViewModel.class);

        // =============================
        // OBSERVE JOB LIST
        // =============================
        viewModel.getJobs().observe(getViewLifecycleOwner(), jobs -> {
            if (jobs == null) return;

            allJobs.clear();
            allJobs.addAll(jobs);

            filterJobs(
                    searchJob.getText() != null
                            ? searchJob.getText().toString()
                            : ""
            );
        });

        // =============================
        // CHIP FILTER
        // =============================
        chipGroup.setOnCheckedChangeListener(
                (group, checkedId) ->
                        filterJobs(searchJob.getText().toString())
        );

        // =============================
        // SEARCH FILTER
        // =============================
        searchJob.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterJobs(s.toString());
            }

            @Override public void afterTextChanged(Editable s) {}
        });

        setupDeleteButton();
        viewModel.refreshJobs();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshJobs();
    }

    // =================================================
    // SEARCH + CHIP FILTER (COMBINED)
    // =================================================
    private void filterJobs(String keyword) {

        List<Job> filtered = new ArrayList<>();
        List<String> chipKeywords = getKeywordsForChip(
                chipGroup.getCheckedChipId()
        );

        String lower = keyword == null ? "" : keyword.toLowerCase();

        for (Job job : allJobs) {

            boolean matchSearch =
                    lower.isEmpty() ||
                            (job.title != null && job.title.toLowerCase().contains(lower)) ||
                            (job.company != null && job.company.toLowerCase().contains(lower)) ||
                            (job.location != null && job.location.toLowerCase().contains(lower));

            boolean matchChip = chipKeywords.isEmpty();

            if (!chipKeywords.isEmpty() && job.tags != null) {
                for (String tag : job.tags) {
                    for (String key : chipKeywords) {
                        if (tag.equalsIgnoreCase(key)) {
                            matchChip = true;
                            break;
                        }
                    }
                }
            }

            if (matchSearch && matchChip) {
                filtered.add(job);
            }
        }

        jobAdapter = new JobAdapter(
                requireContext(),     // ✅ Context
                filtered,             // ✅ Job list
                this::openJobDetail   // ✅ Card click
        );



        if (deleteMode) {
            jobAdapter.enableSelection(true);
        }

        recyclerView.setAdapter(jobAdapter);
    }

    // =================================================
    // DELETE BUTTON (ADMIN)
    // =================================================
    private void setupDeleteButton() {

        btnDelete.setOnClickListener(v -> {

            if (jobAdapter == null) return;

            // ENTER DELETE MODE
            if (!deleteMode) {
                deleteMode = true;
                jobAdapter.enableSelection(true);
                Toast.makeText(
                        getContext(),
                        "Select jobs to delete",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // CONFIRM DELETE
            List<String> selected = jobAdapter.getSelectedJobIds();

            if (selected.isEmpty()) {
                Toast.makeText(
                        getContext(),
                        "No jobs selected",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Jobs")
                    .setMessage("Selected jobs will be archived.")
                    .setPositiveButton("Delete", (d, w) -> {

                        for (String id : selected) {
                            jobRepository.deleteJob(id);
                        }

                        deleteMode = false;
                        jobAdapter.enableSelection(false);
                        viewModel.refreshJobs();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    // =================================================
    // OPEN DETAIL
    // =================================================
    private void openJobDetail(Job job) {

        if (deleteMode) return;

        Intent i = new Intent(getActivity(), JobDetailActivity.class);
        i.putExtra("jobId", job.id);
        i.putExtra("title", job.title);
        i.putExtra("company", job.company);
        i.putExtra("location", job.location);
        i.putExtra("description", job.description);
        i.putExtra("salary", job.salary);
        i.putExtra("employmentType", job.employmentType);
        i.putExtra("status", job.status);

        if (job.requirements != null) {
            i.putStringArrayListExtra(
                    "requirements",
                    new ArrayList<>(job.requirements)
            );
        }

        startActivity(i);
    }

    // =================================================
    // CHIP KEYWORDS
    // =================================================
    private List<String> getKeywordsForChip(int chipId) {

        List<String> list = new ArrayList<>();

        if (chipId == R.id.chip_design) {
            list.add("design");
            list.add("uiux");
        } else if (chipId == R.id.chip_business) {
            list.add("business");
        } else if (chipId == R.id.chip_marketing) {
            list.add("marketing");
        } else if (chipId == R.id.chip_technology) {
            list.add("software");
            list.add("tech");
            list.add("developer");
        } else if (chipId == R.id.chip_finance) {
            list.add("finance");
        }

        return list;
    }
}
