package com.example.find_job.ui.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.find_job.R;

public class Home extends Fragment {

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);

        // Find the title TextView that we added to home.xml
        TextView title = view.findViewById(R.id.fragment_title);
        if (title != null) {
            title.setText("Home");
        } else {
            // Optional: log to help debugging
            // Log.w("Home", "fragment_title not found in layout");
        }

        return view;
    }
}
