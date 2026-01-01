package com.example.find_job.data.models;

import com.google.gson.annotations.SerializedName;

public class UserStats {

    @SerializedName("totalApplications")
    public int applications;

    @SerializedName("totalFavorites")
    public int favorites;

    @SerializedName("profileComplete")
    public boolean profileComplete;
}
