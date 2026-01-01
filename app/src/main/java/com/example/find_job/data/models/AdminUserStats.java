package com.example.find_job.data.models;

import com.google.gson.annotations.SerializedName;

public class AdminUserStats {

    @SerializedName("totalUsers")
    public int totalUsers;

    @SerializedName("activeUsers")
    public int activeUsers;

    @SerializedName("newUsers")
    public int newUsers;

    @SerializedName("bannedUsers")
    public int bannedUsers;

    @SerializedName("deletedUsers")
    public int deletedUsers;
}
