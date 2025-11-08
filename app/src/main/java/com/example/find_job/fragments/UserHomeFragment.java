package com.example.find_job.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.find_job.R;

public class UserHomeFragment extends Fragment {
    public UserHomeFragment() { /* empty constructor */ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate a simple layout (create fragment_user_home.xml in res/layout)
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }
}
