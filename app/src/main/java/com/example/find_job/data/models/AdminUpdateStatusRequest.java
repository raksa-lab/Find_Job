package com.example.find_job.data.models;

public class AdminUpdateStatusRequest {

    public String status;
    public String notes;
    public boolean notifyUser;

    public AdminUpdateStatusRequest(
            String status,
            String notes,
            boolean notifyUser
    ) {
        this.status = status;
        this.notes = notes;
        this.notifyUser = notifyUser;
    }
}
