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
    // DISABLE USER (soft delete)
    // ===============================
    public LiveData<Boolean> disableUser(String userId) {
        return repository.disableUser(userId);
    }

    // ===============================
    // ENABLE USER (undo delete)
    // ===============================
    public LiveData<Boolean> enableUser(String userId) {
        return repository.enableUser(userId);
    }
}
