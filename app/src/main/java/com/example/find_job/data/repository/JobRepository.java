package com.example.find_job.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.models.Job;
import com.example.find_job.data.models.JobRequest;
import com.example.find_job.data.models.JobResponse;
import com.example.find_job.data.models.Summary;
import com.example.find_job.data.service.RetrofitClient;
import com.example.find_job.data.service.serviceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobRepository {

    private final serviceAPI api;

    // ✅ CONTEXT REQUIRED
    public JobRepository(Context context) {
        api = RetrofitClient
                .getClient(context)
                .create(serviceAPI.class);
    }

    // ===============================
    // GET ALL JOBS
    // ===============================
    public MutableLiveData<List<Job>> fetchJobs() {

        MutableLiveData<List<Job>> jobList = new MutableLiveData<>();

        api.getAllJobs().enqueue(new Callback<JobResponse>() {
            @Override
            public void onResponse(
                    Call<JobResponse> call,
                    Response<JobResponse> response
            ) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("API_DEBUG", "Failed to fetch jobs");
                    jobList.setValue(new ArrayList<>());
                    return;
                }

                jobList.setValue(response.body().jobs);
            }

            @Override
            public void onFailure(Call<JobResponse> call, Throwable t) {
                Log.e("API_DEBUG", "API FAILED: " + t.getMessage());
                jobList.setValue(new ArrayList<>());
            }
        });

        return jobList;
    }

    // ===============================
    // ADD NEW JOB (ADMIN ONLY)
    // ===============================
    public void addJob(JobRequest jobRequest, AddJobCallback callback) {

        // ✅ TOKEN IS AUTO-ATTACHED BY INTERCEPTOR
        api.addJob(jobRequest).enqueue(new Callback<JobResponse>() {
            @Override
            public void onResponse(
                    Call<JobResponse> call,
                    Response<JobResponse> response
            ) {

                Log.d("ADD_JOB", "HTTP CODE: " + response.code());

                callback.onResult(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<JobResponse> call, Throwable t) {
                Log.e("ADD_JOB", "NETWORK ERROR: " + t.getMessage());
                callback.onResult(false);
            }
        });
    }
    // UPDATE
    public LiveData<Boolean> updateJob(String id, JobRequest job) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.updateJob(id, job).enqueue(new retrofit2.Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, Response<Job> response) {
                result.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Job> call, Throwable t) {
                result.setValue(false);
            }
        });

        return result;
    }

    // DELETE (ARCHIVE)
    // DELETE (ARCHIVE)
    public LiveData<Boolean> deleteJob(String jobId) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        // false = keep applications
        api.deleteJob(jobId, false).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                result.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.setValue(false);
            }
        });

        return result;
    }

    public LiveData<Integer> fetchTotalJobs() {

        MutableLiveData<Integer> total = new MutableLiveData<>();

        api.getAllJobs().enqueue(new Callback<JobResponse>() {
            @Override
            public void onResponse(
                    Call<JobResponse> call,
                    Response<JobResponse> response
            ) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().pagination != null) {

                    total.setValue(response.body().pagination.totalItems);
                } else {
                    total.setValue(0);
                }
            }

            @Override
            public void onFailure(Call<JobResponse> call, Throwable t) {
                total.setValue(0);
            }
        });

        return total;
    }
    public LiveData<Integer> fetchAdminTotalJobs() {

        MutableLiveData<Integer> total = new MutableLiveData<>();

        api.getAdminJobStats().enqueue(new Callback<Summary>() {
            @Override
            public void onResponse(Call<Summary> call, Response<Summary> response) {
                if (response.isSuccessful() && response.body() != null) {
                    total.setValue(response.body().total);
                } else {
                    total.setValue(0);
                }
            }

            @Override
            public void onFailure(Call<Summary> call, Throwable t) {
                total.setValue(0);
            }
        });

        return total;
    }

    // ===============================
    // CALLBACK
    // ===============================
    public interface AddJobCallback {
        void onResult(boolean success);
    }
}
