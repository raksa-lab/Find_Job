package com.example.find_job.data.api;

import com.example.find_job.data.models.JobRequest;
import com.example.find_job.data.models.JobResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JobApiService {

    @POST("jobs")
    Call<JobResponse> addJob(@Body JobRequest jobRequest);
}
