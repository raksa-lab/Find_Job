package com.example.find_job.data.service;

import com.example.find_job.data.models.Job;
import com.example.find_job.data.models.JobRequest;
import com.example.find_job.data.models.JobResponse;
import com.example.find_job.data.models.LoginRequest;
import com.example.find_job.data.models.LoginResponse;
import com.example.find_job.data.models.RegisterRequest;
import com.example.find_job.data.models.RegisterResponse;
import com.example.find_job.data.models.ApplicationRequest;
import com.example.find_job.data.models.ApplicationResponse;
import com.example.find_job.data.models.Summary;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface serviceAPI {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("jobs")
    Call<JobResponse> getAllJobs();

    // ✅ ADMIN ONLY – TOKEN AUTO-INJECTED
    @POST("admin/jobs")
    Call<JobResponse> addJob(@Body JobRequest jobRequest);
    // UPDATE JOB
    @PUT("admin/jobs/{id}")
    Call<Job> updateJob(
            @Path("id") String id,
            @Body JobRequest job
    );

    // DELETE / ARCHIVE JOB
    @DELETE("admin/jobs/{id}")
    Call<Void> deleteJob(
            @Path("id") String id,
            @Query("deleteApplications") boolean deleteApplications
    );

    @GET("admin/jobs/stats/overview")
    Call<Summary> getAdminJobStats();


}
