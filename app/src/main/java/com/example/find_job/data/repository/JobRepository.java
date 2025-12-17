package com.example.find_job.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.models.Job;
import com.example.find_job.data.models.JobRequest;
import com.example.find_job.data.models.JobResponse;
import com.example.find_job.data.service.RetrofitClient;
import com.example.find_job.data.service.serviceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobRepository {

    private serviceAPI api;

    public JobRepository() {
        api = RetrofitClient.getClient().create(serviceAPI.class);
    }

    // ===============================
    // GET ALL JOBS (Already Working)
    // ===============================
    public MutableLiveData<List<Job>> fetchJobs() {

        MutableLiveData<List<Job>> jobList = new MutableLiveData<>();

        api.getAllJobs().enqueue(new Callback<JobResponse>() {
            @Override
            public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {

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
    // ADD NEW JOB (NEW)
    // ===============================
    public void addJob(JobRequest jobRequest, AddJobCallback callback) {

        // ⚠️ TEMP: hardcoded admin token (replace with real one)
        String adminToken = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjM4MTFiMDdmMjhiODQxZjRiNDllNDgyNTg1ZmQ2NmQ1NWUzOGRiNWQiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiU3lzdGVtIEFkbWluaXN0cmF0b3IiLCJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vam9iLXBvcnRhbC0yMjQ2YSIsImF1ZCI6ImpvYi1wb3J0YWwtMjI0NmEiLCJhdXRoX3RpbWUiOjE3NjU5NTI5MzAsInVzZXJfaWQiOiJidWxQNzJjS0lmU0p3ZVd4djJNNm1NTXREQUQyIiwic3ViIjoiYnVsUDcyY0tJZlNKd2VXeHYyTTZtTU10REFEMiIsImlhdCI6MTc2NTk1MjkzMCwiZXhwIjoxNzY1OTU2NTMwLCJlbWFpbCI6ImFkbWluQGpvYnBvcnRhbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsiYWRtaW5Aam9icG9ydGFsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.ondrQ79MLZppYObPIdtAM0WWucewed4SvPN08nQ8Lja3_73LlPLCozT9ZMM9G8v1MzYKdBMj8DWg6BejOietesTqYytZ74eyvWgIcSfNq6BzyUoqKMjyY12ezfPULrNlCyy0R4kkZMOR_lDwvnArEeyi34mJovQxhYGIZ0Q2jJE4hBn6WAcDzmtsk5-haEhxlb-_xRxy17vLV_BJbj2uG6dukq8ohRSka1DCXRdKaKMdlkGD7UdtxvgMU368xiDX97Hnov2gRKPwNWlxbt1JWJSgFRNpysRaOsXnjwI1n6-Z2K2clZdQXUFJ4wv4xl4OsX9wm6bEUbIof7ep_ePvIw";

        api.addJob(adminToken, jobRequest).enqueue(new Callback<JobResponse>() {
            @Override
            public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {

                Log.e("ADD_JOB", "HTTP CODE: " + response.code());

                if (response.isSuccessful()) {
                    callback.onResult(true);
                } else {
                    try {
                        if (response.errorBody() != null) {
                            Log.e("ADD_JOB", "ERROR BODY: " + response.errorBody().string());
                        }
                    } catch (Exception ignored) {}

                    callback.onResult(false);
                }
            }

            @Override
            public void onFailure(Call<JobResponse> call, Throwable t) {
                Log.e("ADD_JOB", "NETWORK ERROR: " + t.getMessage());
                callback.onResult(false);
            }
        });
    }



    // ===============================
    // Callback Interface
    // ===============================
    public interface AddJobCallback {
        void onResult(boolean success);
    }
}
