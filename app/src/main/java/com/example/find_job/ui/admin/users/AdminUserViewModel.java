package com.example.find_job.ui.admin.users;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.find_job.data.models.AdminUser;
import com.example.find_job.data.repository.AdminUserRepository;

import java.util.List;

public class AdminUserViewModel extends AndroidViewModel {

    private final AdminUserRepository repository;
    private final LiveData<List<AdminUser>> users;

    public AdminUserViewModel(@NonNull Application application) {
        super(application);

        // ✅ SINGLE repository instance
        repository = new AdminUserRepository(application);

        // ✅ Load users
        users = repository.fetchUsers();
    }

    // ===============================
    // USERS LIST
    // ===============================
    public LiveData<List<AdminUser>> getUsers() {
        return users;
    }

    // ===============================
    // TOTAL USERS (FROM pagination.totalItems)
    // ===============================


    // ===============================
    // DELETE USER
    // ===============================
    public LiveData<Boolean> deleteUser(String userId) {
        return repository.deleteUser(userId);
    }
}
