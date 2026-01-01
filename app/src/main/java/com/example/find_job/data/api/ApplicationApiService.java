package com.example.find_job.data.api;

import com.example.find_job.data.models.AppliedJobsResponse;
import com.example.find_job.data.models.ApplyRequest;
import com.example.find_job.data.models.ApplicationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApplicationApiService {

    @POST("apply/{jobId}/apply")
    Call<ApplicationResponse> applyJob(
            @Path("jobId") String jobId,
            @Body ApplyRequest body
    );
    @GET("apply")
    Call<AppliedJobsResponse> getMyApplications(
            @Query("status") String status,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @GET("apply/check/{jobId}")
    Call<ApplicationResponse> checkStatus(
            @Path("jobId") String jobId
    );

    @DELETE("apply/{applicationId}")
    Call<Void> withdraw(
            @Path("applicationId") String applicationId
    );
}
