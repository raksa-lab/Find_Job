package com.example.find_job.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "USER_SESSION";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_UID = "user_uid";
    private static final String KEY_ROLE = "role";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ===============================
    // SAVE SESSION
    // ===============================
    public void saveSession(String token, String uid, String role) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_UID, uid)
                .putString(KEY_ROLE, role)
                .apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public String getUserUid() {
        return prefs.getString(KEY_USER_UID, null);
    }

    public String getRole() {
        return prefs.getString(KEY_ROLE, "");
    }

    public boolean isLoggedIn() {
        return getToken() != null && !getToken().isEmpty();
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(getRole());
    }

    public boolean isJobSeeker() {
        return "employee".equalsIgnoreCase(getRole())
                || "seeker".equalsIgnoreCase(getRole());
    }

    public boolean isEmployer() {
        return "employer".equalsIgnoreCase(getRole());
    }

    public void logout() {
        prefs.edit().clear().apply();
    }
}
