package com.example.find_job.services;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;

public class AuthManager {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public Task<AuthResult> signIn(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> register(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public void signOut() {
        auth.signOut();
    }

    public Task<Void> sendPasswordReset(String email) {
        return auth.sendPasswordResetEmail(email);
    }

    public Task<GetTokenResult> refreshToken() {
        FirebaseUser u = auth.getCurrentUser();
        if (u == null) return null;
        return u.getIdToken(true);
    }
}
