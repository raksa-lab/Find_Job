package com.example.find_job.data.service;

import com.example.find_job.data.models.JobRequest;
import com.example.find_job.data.models.JobResponse;
import com.example.find_job.data.models.LoginRequest;
import com.example.find_job.data.models.LoginResponse;
import com.example.find_job.data.models.RegisterRequest;
import com.example.find_job.data.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface serviceAPI {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
    @GET("api/jobs")
    Call<JobResponse> getAllJobs();
    @POST("api/admin/jobs")
    Call<JobResponse> addJob(
            @Header("Authorization") String authorization,
            @Body JobRequest jobRequest
    );


}
