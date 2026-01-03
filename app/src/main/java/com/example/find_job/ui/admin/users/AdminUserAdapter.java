package com.example.find_job.ui.admin.users;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.AdminUser;

import java.util.List;

public class AdminUserAdapter
        extends RecyclerView.Adapter<AdminUserAdapter.VH> {

    private final List<AdminUser> users;
    private final AdminUserViewModel viewModel;

    public AdminUserAdapter(List<AdminUser> users, AdminUserViewModel vm) {
        this.users = users;
        this.viewModel = vm;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_user, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {

        AdminUser user = users.get(position);

        h.name.setText(user.getFullName());
        h.email.setText(user.getEmail());


        // ===============================
        // USER STATUS + BUTTON VISIBILITY
        // ===============================
        if (user.isDeleted()) {
            h.status.setText("Disabled");
            h.status.setTextColor(Color.RED);

            h.btnEnable.setVisibility(View.VISIBLE);
            h.btnDisable.setVisibility(View.GONE);
        } else {
            h.status.setText("Active");
            h.status.setTextColor(Color.GREEN);

            h.btnDisable.setVisibility(View.VISIBLE);
            h.btnEnable.setVisibility(View.GONE);
        }

        // ===============================
        // DISABLE USER
        // ===============================
        h.btnDisable.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Disable User")
                    .setMessage("Are you sure you want to disable this user?")
                    .setPositiveButton("Disable", (d, w) -> {
                        viewModel.disableUser(user.getId());
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // ===============================
        // ENABLE USER
        // ===============================
        h.btnEnable.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Enable User")
                    .setMessage("Do you want to re-enable this user?")
                    .setPositiveButton("Enable", (d, w) -> {
                        viewModel.enableUser(user.getId());
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // ===============================
    // VIEW HOLDER
    // ===============================
    static class VH extends RecyclerView.ViewHolder {

        TextView name, email, status;
        Button btnDisable, btnEnable;

        VH(View v) {
            super(v);
            name = v.findViewById(R.id.tv_name);
            email = v.findViewById(R.id.tv_email);
            status = v.findViewById(R.id.tv_status);
            btnDisable = v.findViewById(R.id.btn_disable);
            btnEnable = v.findViewById(R.id.btn_enable);
        }
    }

}
