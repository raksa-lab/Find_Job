package com.example.find_job.data.models;

import java.util.List;
import java.util.Map;

public class AppliedJob {

    // =========================
    // BASIC INFO
    // =========================
    public String id;
    public String status;
    public String stage;

    // =========================
    // JOB INFO
    // =========================
    public String jobId;
    public String jobTitle;
    public String jobCompany;
    public Job job;

    // =========================
    // USER INFO
    // =========================
    public String userId;
    public String userName;
    public String userEmail;
    public String userPhone;

    // =========================
    // CONTENT
    // =========================
    public String coverLetter;
    public Object additionalInfo;
    public Notes notes;
    public String resumeUrl;

    // =========================
    // NOTES MODEL
    // =========================
    public static class Notes {
        public String userNotes;
        public List<AdminNote> adminNotes;
        public Timestamp lastUpdated;
    }

    // =========================
    // ADMIN NOTE MODEL
    // =========================
    public static class AdminNote {
        public String content;
        public String addedBy;
        public String addedByName;
        public boolean isInternal;
        public boolean notifyUser;
        public Timestamp timestamp;

        public boolean isInternal() {
            return isInternal;
        }

        public boolean isNotifyUser() {
            return notifyUser;
        }
    }


    // =========================
    // TIMESTAMP MODEL
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
        public String type;
    }

    // =====================================================
    // ✅ USER-VISIBLE ADMIN NOTE (CRITICAL FIX)
    // Only returns notes meant for the user
    // =====================================================
    public String getLatestAdminNote() {

        if (notes == null || notes.adminNotes == null || notes.adminNotes.isEmpty()) {
            return null;
        }

        // Iterate from newest → oldest
        for (int i = notes.adminNotes.size() - 1; i >= 0; i--) {

            AdminNote note = notes.adminNotes.get(i);
            if (note == null) continue;

            // ✔ USER CAN SEE ONLY THESE
            if (!note.isInternal
                    && note.notifyUser
                    && note.content != null
                    && !note.content.trim().isEmpty()) {

                return note.content;
            }
        }

        return null;
    }

    // =====================================================
    // ✅ SAFE ADDITIONAL INFO (BACKWARD COMPATIBLE)
    // =====================================================
    public String getAdditionalInfoText() {

        if (additionalInfo == null) return null;

        // Case 1: backend returns String
        if (additionalInfo instanceof String) {
            String s = (String) additionalInfo;
            return s.trim().isEmpty() ? null : s;
        }

        // Case 2: backend returns object {}
        if (additionalInfo instanceof Map) {
            Object msg = ((Map<?, ?>) additionalInfo).get("message");
            if (msg != null) {
                String s = msg.toString();
                return s.trim().isEmpty() ? null : s;
            }
        }

        return null;
    }
}
