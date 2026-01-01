package com.example.find_job.data.api;

import com.example.find_job.data.models.AdminUserStats;
import com.example.find_job.data.models.UploadFilesResponse;
import com.example.find_job.data.models.UserProfileResponse;
import com.example.find_job.data.models.UserStats;
import com.example.find_job.data.models.UserProfile;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface
UserApiService {

    @GET("users/me")
    Call<UserProfileResponse> getMyProfile();

    @GET("users/me/stats")
    Call<UserStats> getMyStats();

    @PUT("users/me")
    Call<UserProfile> updateProfile(@Body Map<String, Object> body);
    @Multipart
    @POST("upload/resume")
    Call<UserProfileResponse> uploadResume(
            @Part MultipartBody.Part resume
    );

    @DELETE("upload/resume")
    Call<Void> deleteResume();

    @GET("upload/files")
    Call<UploadFilesResponse> getUploadedFiles();
    @GET("admin/users/stats/overview")
    Call<AdminUserStats> getAdminUserStats();


}
