package com.example.find_job;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Job extends Fragment {

    public Job() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.job, container, false);

        // Example: Set a title for the fragment
        TextView title = view.findViewById(R.id.fragment_title);
        title.setText("Jobs");

        return view;
    }
}
