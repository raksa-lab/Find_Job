package com.example.find_job.data.service;

import com.example.find_job.data.models.JobRequest;
import com.example.find_job.data.models.JobResponse;
import com.example.find_job.data.models.LoginRequest;
import com.example.find_job.data.models.LoginResponse;
import com.example.find_job.data.models.RegisterRequest;
import com.example.find_job.data.models.RegisterResponse;
import com.example.find_job.data.models.ApplicationRequest;
import com.example.find_job.data.models.ApplicationResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
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


    @POST("apply/{jobId}/apply")
    Call<Void> applyJob(
            @Path("jobId") String jobId,
            @Body ApplicationRequest body
    );

    @GET("apply/check/{jobId}")
    Call<Map<String, Boolean>> checkApplication(
            @Path("jobId") String jobId
    );

    @GET("apply")
    Call<List<ApplicationResponse>> getMyApplications(
            @Query("status") String status,
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sortBy") String sortBy,
            @Query("order") String order
    );

    @DELETE("apply/{applicationId}")
    Call<Void> withdrawApplication(
            @Path("applicationId") String applicationId
    );

}
