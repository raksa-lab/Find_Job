package com.example.find_job.data.models;

public class DeleteUserRequest {
    public String reason;
    public boolean deleteData;

    public DeleteUserRequest(String reason, boolean deleteData) {
        this.reason = reason;
        this.deleteData = deleteData;
    }
}
