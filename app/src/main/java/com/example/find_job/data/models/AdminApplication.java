package com.example.find_job.data.models;

import java.util.List;

public class AdminApplication {

    // =========================
    // BASIC INFO
    // =========================
    public String id;
    public String stage;
    public String status;
    public String resumeUrl;
    public String userName;
    public String userEmail;
    public String jobTitle;
    public String jobCompany;

    // =========================
    // USER → ADMIN MESSAGE
    // =========================
    public String coverLetter;

    // =========================
    // ADMIN → USER REPLY
    // Can be String | Object | null
    // =========================
    public Object additionalInfo;

    // =========================
    // NOTES (ADMIN + USER)
    // =========================
    public Notes notes;

    // =========================
    // JOB INFO
    // =========================
    public Job job;

    // =========================
    // NOTES MODEL
    // =========================
    public static class Notes {
        public String userNotes;
        public List<AdminNote> adminNotes;
        public Timestamp lastUpdated;
    }

    // =========================
    // ADMIN NOTE
    // =========================
    public static class AdminNote {
        public String content;
        public String addedBy;
        public String addedByName;
        public boolean isInternal;
        public boolean notifyUser;
        public Timestamp timestamp;
    }

    // =========================
    // TIMESTAMP
    // =========================
    public static class Timestamp {
        public long _seconds;
        public int _nanoseconds;
    }

    // =========================
    // JOB MODEL
    // =========================
    public static class Job {
        public String id;
        public String title;
        public String company;
        public String location;
        public String status;
    }

    // =========================
    // SAFE ADMIN REPLY ACCESS
    // =========================
    public String getAdminReply() {
        if (notes == null || notes.adminNotes == null || notes.adminNotes.isEmpty()) {
            return null;
        }
        return notes.adminNotes.get(notes.adminNotes.size() - 1).content;
    }
}
