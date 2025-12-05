package com.example.find_job.data.models;

public class RegisterRequest {

    public String fullName;
    public String email;
    public String password;

    public RegisterRequest(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}
