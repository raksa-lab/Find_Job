package com.example.find_job.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.api.UserApiService;
import com.example.find_job.data.models.AdminUserStats;
import com.example.find_job.data.models.UploadFilesResponse;
import com.example.find_job.data.models.UploadedFile;
import com.example.find_job.data.models.UserProfile;
import com.example.find_job.data.models.UserProfileResponse;
import com.example.find_job.data.models.UserProfileUI;
import com.example.find_job.data.models.UserStats;
import com.example.find_job.data.service.RetrofitClient;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final UserApiService api;

    // ✅ Context passed ONCE
    public UserRepository(Context context) {
        api = RetrofitClient
                .getClient(context.getApplicationContext())
                .create(UserApiService.class);
    }

    // ===============================
    // FULL PROFILE
    // ===============================
    public LiveData<UserProfileUI> getFullProfile() {

        MutableLiveData<UserProfileUI> liveData = new MutableLiveData<>();
        UserProfileUI ui = new UserProfileUI();

        api.getMyProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(
                    Call<UserProfileResponse> call,
                    Response<UserProfileResponse> response
            ) {
                if (!response.isSuccessful() || response.body() == null) {
                    liveData.setValue(null);
                    return;
                }

                UserProfile u = response.body().user;

                ui.uid = u.uid;
                ui.fullName = u.fullName;
                ui.email = u.email;
                ui.phone = u.phone;
                ui.location = u.location;
                ui.role = u.role;
                ui.bio = u.bio;
                ui.skills = u.skills;
                ui.experience = u.experience;
                ui.education = u.education;

                api.getMyStats().enqueue(new Callback<UserStats>() {
                    @Override
                    public void onResponse(
                            Call<UserStats> call,
                            Response<UserStats> res
                    ) {
                        if (res.body() != null) {
                            ui.applicationsCount = res.body().applications;
                            ui.favoritesCount = res.body().favorites;
                            ui.profileComplete = res.body().profileComplete;
                        }
                        liveData.setValue(ui);
                    }

                    @Override
                    public void onFailure(Call<UserStats> call, Throwable t) {
                        liveData.setValue(ui);
                    }
                });
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    // ===============================
    // UPDATE PROFILE
    // ===============================
    public LiveData<Boolean> updateProfile(
            String fullName,
            String location,
            String bio
    ) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.updateProfile(
                new HashMap<String, Object>() {{
                    put("fullName", fullName);
                    put("location", location);
                    put("bio", bio);
                }}
        ).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(
                    Call<UserProfile> call,
                    Response<UserProfile> response
            ) {
                result.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                result.setValue(false);
            }
        });

        return result;
    }

    // ===============================
    // UPDATE SKILLS
    // ===============================
    public LiveData<Boolean> updateSkills(List<String> skills) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.updateProfile(
                new HashMap<String, Object>() {{
                    put("skills", skills);
                }}
        ).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                result.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                result.setValue(false);
            }
        });

        return result;
    }

    // ===============================
    // BASIC PROFILE (FOR EDIT)
    // ===============================
    public LiveData<UserProfile> getMyProfile() {

        MutableLiveData<UserProfile> liveData = new MutableLiveData<>();

        api.getMyProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(
                    Call<UserProfileResponse> call,
                    Response<UserProfileResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body().user);
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }

    // ===============================
    // UPLOAD RESUME (CV)
    // ===============================
    public LiveData<Boolean> uploadResume(File file, String mimeType) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        RequestBody body =
                RequestBody.create(file, MediaType.parse(mimeType));

        MultipartBody.Part resume =
                MultipartBody.Part.createFormData(
                        "resume",
                        file.getName(),
                        body
                );

        api.uploadResume(resume).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call,
                                   Response<UserProfileResponse> response) {
                result.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                result.setValue(false);
            }
        });

        return result;
    }



    // ===============================
    // DELETE RESUME
    // ===============================
    public LiveData<Boolean> deleteResume() {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.deleteResume().enqueue(new Callback<Void>() {
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
    // ===============================
// ADMIN USER STATS
// ===============================
    // ===============================
// ADMIN USER STATS
// ===============================
    public LiveData<AdminUserStats> fetchAdminUserStats() {

        MutableLiveData<AdminUserStats> liveData = new MutableLiveData<>();

        api.getAdminUserStats().enqueue(new Callback<AdminUserStats>() {
            @Override
            public void onResponse(
                    Call<AdminUserStats> call,
                    Response<AdminUserStats> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AdminUserStats> call, Throwable t) {
                liveData.setValue(null);
            }
        });

        return liveData;
    }



    // ===============================
    // CHECK RESUME EXISTS
    // ===============================
    public LiveData<Boolean> hasResume() {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.getUploadedFiles().enqueue(new Callback<UploadFilesResponse>() {
            @Override
            public void onResponse(
                    Call<UploadFilesResponse> call,
                    Response<UploadFilesResponse> response
            ) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().files != null) {

                    boolean found = false;

                    for (UploadedFile file : response.body().files) {
                        if ("resume".equalsIgnoreCase(file.type)) {
                            found = true;
                            break;
                        }
                    }

                    result.setValue(found);
                } else {
                    result.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<UploadFilesResponse> call, Throwable t) {
                result.setValue(false);
            }
        });

        return result;
    }

}
