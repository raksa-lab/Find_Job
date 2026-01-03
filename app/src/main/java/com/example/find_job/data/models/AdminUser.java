package com.example.find_job.data.models;

public class AdminUser {

    private String id;
    private String fullName;
    private String email;
    private String role;

    // ✅ IMPORTANT
    private boolean isDeleted;

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

}
