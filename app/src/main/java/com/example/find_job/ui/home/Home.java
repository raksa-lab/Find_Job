package com.example.find_job.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.viewpager2.widget.ViewPager2;

import com.example.find_job.MainActivity;
import com.example.find_job.R;
import com.example.find_job.adapters.BlogSliderAdapter;
import com.example.find_job.adapters.JobAdapter;
import com.example.find_job.data.models.BlogItem;
import com.example.find_job.data.models.Job;
import com.example.find_job.ui.job_detail.JobDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private ViewPager2 blogSlider;
    private RecyclerView rvHomeJobs;
    private JobAdapter jobAdapter;
    private HomeViewModel viewModel;

    private final Handler sliderHandler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.home, container, false);

        // INIT VIEWMODEL
        viewModel = new ViewModelProvider(requireActivity())
                .get(HomeViewModel.class);

        TextView tvJobCount = view.findViewById(R.id.tv_job_count);

        // =====================
        // JOB COUNT
        // =====================
        viewModel.getTotalJobs().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                tvJobCount.setText(String.valueOf(total));
            }
        });

        blogSlider = view.findViewById(R.id.blogSlider);
        rvHomeJobs = view.findViewById(R.id.rvHomeJobs);
        rvHomeJobs.setLayoutManager(new LinearLayoutManager(getContext()));

        setupBlogSlider();

        return view;
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.see_all).setOnClickListener(v -> {
            Intent i = new Intent(requireContext(), MainActivity.class);
            i.putExtra("open_tab", "job");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        });

        // =====================
        // OBSERVE JOBS
        // =====================
        viewModel.getJobs().observe(getViewLifecycleOwner(), jobs -> {
            if (jobs == null) return;

            List<Job> limitedJobs = new ArrayList<>();
            int limit = Math.min(jobs.size(), 5);

            for (int i = 0; i < limit; i++) {
                limitedJobs.add(jobs.get(i));
            }

            // ✅ CORRECT CONSTRUCTOR
            jobAdapter = new JobAdapter(
                    requireContext(),
                    limitedJobs,
                    this::openJobDetail
            );

            rvHomeJobs.setAdapter(jobAdapter);
        });
    }

    // ======================
    // BLOG SLIDER
    // ======================
    private void setupBlogSlider() {

        List<BlogItem> blogs = new ArrayList<>();

        blogs.add(new BlogItem(R.drawable.banner1, "", ""));
        blogs.add(new BlogItem(R.drawable.banner2, "", ""));
        blogs.add(new BlogItem(R.drawable.banner3, "", ""));

        BlogSliderAdapter adapter = new BlogSliderAdapter(blogs);
        blogSlider.setAdapter(adapter);

        blogSlider.setPageTransformer((page, position) -> {
            float abs = Math.abs(position);
            page.setScaleX(0.94f + (1 - abs) * 0.06f);
            page.setScaleY(0.94f + (1 - abs) * 0.06f);
            page.setAlpha(0.75f + (1 - abs) * 0.25f);
        });

        sliderHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int next = (blogSlider.getCurrentItem() + 1) % blogs.size();
                blogSlider.setCurrentItem(next, true);
                sliderHandler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    private void openJobDetail(Job job) {
        Intent i = new Intent(getActivity(), JobDetailActivity.class);

        i.putExtra("jobId", job.id);
        i.putExtra("title", job.title);
        i.putExtra("company", job.company);
        i.putExtra("location", job.location);
        i.putExtra("description", job.description);
        i.putExtra("salary", job.salary);
        i.putExtra("employmentType", job.employmentType);

        i.putStringArrayListExtra(
                "requirements",
                job.requirements != null
                        ? new ArrayList<>(job.requirements)
                        : new ArrayList<>()
        );

        startActivity(i);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacksAndMessages(null);
    }
}
