package com.example.find_job.data.api;

import com.example.find_job.data.models.JobRequest;
import com.example.find_job.data.models.JobResponse;
import com.example.find_job.data.models.Summary;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JobApiService {

    // CREATE JOB (ADMIN)
    @POST("jobs")
    Call<JobResponse> addJob(@Body JobRequest jobRequest);

    // DELETE (ARCHIVE) JOB (ADMIN)
    @DELETE("admin/jobs/{id}")
    Call<Void> deleteJob(
            @Path("id") String jobId,
            @Query("deleteApplications") boolean deleteApplications
    );

    // ✅ ADMIN JOB STATS
    @GET("admin/jobs/stats/overview")
    Call<Summary> getAdminJobStats();
}
