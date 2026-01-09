package com.example.find_job.data.models;

import com.google.gson.internal.LinkedTreeMap;

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
    // ADMIN → USER REPLY (RAW FROM BACKEND)
    // Can be String | Object | null
    // =========================
    public Object additionalInfo;

    // =========================
    // LEGACY NOTES (OPTIONAL / NOT USED)
    // =========================
    public Notes notes;

    // =========================
    // STATUS HISTORY
    // =========================
    public List<History> history;

    // =====================================================
    // SAFE ACCESSOR FOR ADMIN REPLY
    // =====================================================
    public String getAdditionalInfoText() {

        if (additionalInfo == null) {
            return null;
        }

        // Case 1: backend returns plain string
        if (additionalInfo instanceof String) {
            return (String) additionalInfo;
        }

        // Case 2: backend returns object (e.g. { "text": "..." })
        if (additionalInfo instanceof LinkedTreeMap) {
            LinkedTreeMap<?, ?> map =
                    (LinkedTreeMap<?, ?>) additionalInfo;

            Object text = map.get("text");
            return text != null ? text.toString() : null;
        }

        // Fallback (unexpected type)
        return additionalInfo.toString();
    }

    // =========================
    // NOTES MODEL (LEGACY)
    // =========================
    public static class Notes {
        public String userNotes;
        public List<String> adminNotes;
    }

    // =========================
    // HISTORY MODEL
    // =========================
    public static class History {
        public String notes;
        public String timestamp;
    }

    // =========================
    // JOB INFO
    // =========================
    public Job job;

    public static class Job {
        public String id;
        public String title;
        public String company;
        public String location;
        public String status;
    }
}
