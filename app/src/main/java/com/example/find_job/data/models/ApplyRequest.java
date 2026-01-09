package com.example.find_job.data.models;

import java.util.HashMap;
import java.util.Map;

public class ApplyRequest {

    public String coverLetter;
    public Map<String, String> notes;

    public ApplyRequest(String coverLetter, String userNote) {
        this.coverLetter = coverLetter;

        if (userNote != null && !userNote.trim().isEmpty()) {
            notes = new HashMap<>();
            notes.put("userNotes", userNote);
        }
    }
}
