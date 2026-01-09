package com.example.find_job.data.models;

import java.util.List;
import java.util.Map;

public class AppliedJob {

    public String id;
    public String status;
    public String stage;

    public String jobId;
    public String jobTitle;
    public String jobCompany;
    public Job job;

    public String userId;
    public String userName;
    public String userEmail;
    public String userPhone;

    public String coverLetter;
    public Object additionalInfo;
    public Notes notes;
    public String resumeUrl;

    public static class Notes {
        public String userNotes;
        public List<String> adminNotes;
    }

    // =========================
    // SAFE NOTE EXTRACTOR
    // =========================
    public String getAdditionalInfoText() {
        if (additionalInfo == null) return null;

        if (additionalInfo instanceof String) {
            String s = (String) additionalInfo;
            return s.trim().isEmpty() ? null : s;
        }

        if (additionalInfo instanceof Map) {
            Object msg = ((Map<?, ?>) additionalInfo).get("message");
            return msg != null ? msg.toString() : null;
        }

        return null;
    }
}

