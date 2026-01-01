package com.example.find_job.ui.admin.users;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;

import java.util.List;

public class AdminUserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminUserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_list);

        // ✅ INIT RECYCLER VIEW
        recyclerView = findViewById(R.id.rv_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ INIT VIEWMODEL
        viewModel = new ViewModelProvider(this)
                .get(AdminUserViewModel.class);

        // ✅ OBSERVE USERS
        viewModel.getUsers().observe(this, users -> {
            recyclerView.setAdapter(
                    new AdminUserAdapter(users, viewModel)
            );
        });
    }
}
