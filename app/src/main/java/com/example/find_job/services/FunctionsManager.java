package com.example.find_job.services;


import com.google.firebase.functions.FirebaseFunctions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class FunctionsManager {
    private final FirebaseFunctions functions = FirebaseFunctions.getInstance();

    public Task<HttpsCallableResult> setAdminRole(String uid) {
        Map<String,Object> data = new HashMap<>();
        data.put("uid", uid);
        return functions.getHttpsCallable("setAdminRole").call(data);
    }
}
