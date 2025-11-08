package com.example.find_job.services;

// --- CORRECTED IMPORTS ---
// Replaced the wildcard import with specific classes to resolve ambiguity.
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener; // Specifically import the Firestore EventListener
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Collections;
import java.util.Map;

// NOTE: The 'java.util.*' wildcard was removed to prevent conflicts.
// Only the needed 'java.util' classes are imported.

public class FirestoreRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Fetch published, active jobs (snapshot listener for realtime)
    // This now works because the compiler knows exactly which EventListener to use.
    public ListenerRegistration listenPublishedJobs(EventListener<QuerySnapshot> listener) {
        return db.collection("jobs")
                .whereEqualTo("isActive", true)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(listener);
    }

    // Create job (admin only - client must ensure role or use cloud function)
    public Task<DocumentReference> createJob(Map<String, Object> jobData) {
        jobData.put("timestamp", FieldValue.serverTimestamp());
        return db.collection("jobs").add(jobData);
    }

    public Task<Void> updateJob(String jobId, Map<String, Object> updates) {
        updates.put("timestamp", FieldValue.serverTimestamp());
        return db.collection("jobs").document(jobId).update(updates);
    }

    public Task<Void> deleteJob(String jobId) {
        return db.collection("jobs").document(jobId).delete();
    }

    // Favorites
    public Task<Void> saveFavorite(String uid, String jobId) {
        return db.collection("users").document(uid).collection("favorites").document(jobId)
                .set(Collections.singletonMap("savedAt", FieldValue.serverTimestamp()));
    }

    public Task<Void> removeFavorite(String uid, String jobId) {
        return db.collection("users").document(uid).collection("favorites").document(jobId).delete();
    }

    // Apply: create application doc
    public Task<DocumentReference> createApplication(Map<String, Object> appData) {
        appData.put("timestamp", FieldValue.serverTimestamp());
        return db.collection("applications").add(appData);
    }

    // Fetch single job
    public Task<DocumentSnapshot> getJob(String jobId) {
        return db.collection("jobs").document(jobId).get();
    }

    // Users read/update
    public Task<DocumentSnapshot> getUser(String uid) {
        return db.collection("users").document(uid).get();
    }

    public Task<Void> updateUser(String uid, Map<String, Object> updates) {
        updates.put("updatedAt", FieldValue.serverTimestamp());
        return db.collection("users").document(uid).set(updates, SetOptions.merge());
    }
}
