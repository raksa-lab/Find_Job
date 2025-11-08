package com.example.find_job.services;

import android.net.Uri;

// --- THIS IS THE FIX ---
// Import the missing 'Task' class.
import com.google.android.gms.tasks.Task;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageManager {
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public UploadTask uploadResume(String uid, Uri fileUri, String filename) {
        StorageReference ref = storage.getReference().child("resumes/" + uid + "/" + filename);
        return ref.putFile(fileUri);
    }

    public UploadTask uploadJobImage(String filename, Uri fileUri) {
        StorageReference ref = storage.getReference().child("job_images/" + filename);
        return ref.putFile(fileUri);
    }

    // This method will now compile correctly because 'Task' is imported.
    public Task<Uri> getDownloadUrl(String path) {
        return storage.getReference().child(path).getDownloadUrl();
    }
}
