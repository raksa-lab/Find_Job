package com.example.find_job.ui.admin.users;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        h.name.setText(user.fullName);
        h.email.setText(user.email);
        h.role.setText(user.role.toUpperCase());

        // DELETE USER
        h.delete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete User")
                    .setMessage("Are you sure you want to delete this user?")
                    .setPositiveButton("Delete", (d, w) -> {

                        viewModel.deleteUser(user.id)
                                .observeForever(success -> {
                                    if (success) {
                                        users.remove(position);
                                        notifyItemRemoved(position);
                                    }
                                });

                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, email, role;
        ImageView delete;

        VH(View v) {
            super(v);
            name = v.findViewById(R.id.tv_name);
            email = v.findViewById(R.id.tv_email);
            role = v.findViewById(R.id.tv_role);
            delete = v.findViewById(R.id.btn_delete);
        }
    }
}
