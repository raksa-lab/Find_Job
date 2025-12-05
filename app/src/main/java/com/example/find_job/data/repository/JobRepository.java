package com.example.find_job.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.models.Job;
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

    public MutableLiveData<List<Job>> fetchJobs() {

        MutableLiveData<List<Job>> jobList = new MutableLiveData<>();

        api.getAllJobs().enqueue(new Callback<JobResponse>() {
            @Override
            public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {

                if (!response.isSuccessful()) {
                    Log.e("API_DEBUG", "Response not successful: " + response.code());
                    jobList.setValue(new ArrayList<>());
                    return;
                }

                JobResponse body = response.body();

                if (body == null) {
                    Log.e("API_DEBUG", "Body is null");
                    jobList.setValue(new ArrayList<>());
                    return;
                }

                if (body.data == null) {
                    Log.e("API_DEBUG", "DATA is null");
                    jobList.setValue(new ArrayList<>());
                    return;
                }

                Log.d("API_DEBUG", "Jobs received: " + body.data.size());
                jobList.setValue(body.data);
            }

            @Override
            public void onFailure(Call<JobResponse> call, Throwable t) {
                Log.e("API_DEBUG", "API FAILED: " + t.getMessage());
                jobList.setValue(new ArrayList<>());
            }
        });

        return jobList;
    }
}
