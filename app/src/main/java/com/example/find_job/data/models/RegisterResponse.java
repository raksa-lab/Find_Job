package com.example.find_job.data.models;

public class RegisterResponse {
    public int status;
    public String error;
    public User data;
    public String uid;
    public boolean isSuccess() {
        return status == 200 && error == null;
    }

    public static class User {

        public String fullName;
        public String email;
    }
}
