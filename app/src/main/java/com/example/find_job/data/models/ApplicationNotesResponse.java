package com.example.find_job.data.models;

import java.util.List;

public class ApplicationNotesResponse {

    private boolean success;
    private Notes notes;
    private String applicationId;
    private boolean canRespond;

    public Notes getNotes() {
        return notes;
    }

    public boolean canRespond() {
        return canRespond;
    }

    public static class Notes {
        private String userNotes;
        private List<String> adminNotes;

        public String getUserNotes() {
            return userNotes == null ? "" : userNotes;
        }

        public List<String> getAdminNotes() {
            return adminNotes;
        }
    }
}
