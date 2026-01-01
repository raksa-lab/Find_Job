package com.example.find_job;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // ✅ If Firebase is already initialized, do nothing
        if (!FirebaseApp.getApps(this).isEmpty()) {
            return;
        }

        // 🔥 MANUAL Firebase init (bypasses google-services plugin)
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:633505131891:android:27c6b81cc5fa327a052906")
                .setApiKey("AIzaSyDZ2NWIl2Jd8MxuVWmdsBQQlZQv80Tlm7Y")
                .setProjectId("job-portal-2246a")
                .setStorageBucket("job-portal-2246a.firebasestorage.app")
                .build();

        FirebaseApp.initializeApp(this, options);
    }
}
