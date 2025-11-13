package com.example.find_job.models;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;
import java.util.List;

public class Job {
    private String id;
    private String title;
    private String company;
    private String description;
    private String location;
    private Double salary;
    private String postedBy;
    private Boolean isActive;
    private List<String> tags;
    @ServerTimestamp private Date timestamp;

    public Job() {}

    // getters & setters (generate in IDE)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    // ... add other getters/setters
}
