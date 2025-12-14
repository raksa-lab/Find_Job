package com.example.find_job.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.ui.job_detail.JobDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class AppliedJobAdapter extends RecyclerView.Adapter<AppliedJobAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, String>> list;

    public AppliedJobAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_applied_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        HashMap<String, String> job = list.get(position);

        h.title.setText(job.get("title"));
        h.company.setText(job.get("company"));

        String remarkValue = job.get("remark");
        if (remarkValue == null || remarkValue.isEmpty()) {
            h.remark.setText("Remark: (none)");
        } else {
            h.remark.setText("Remark: " + remarkValue);
        }

        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, JobDetailActivity.class);

            // Required to hide Apply button
            i.putExtra("fromApplied", true);

            // Send stored job data
            i.putExtra("jobId", job.get("jobId"));
            i.putExtra("title", job.get("title"));
            i.putExtra("company", job.get("company"));
            i.putExtra("location", job.get("location"));
            i.putExtra("description", job.get("description"));

            try {
                i.putExtra("salary", Integer.parseInt(job.get("salary")));
            } catch (Exception e) {
                i.putExtra("salary", 0);
            }

            // Requirements (string, because SharedPreferences cannot store list)
            ArrayList<String> reqList = new ArrayList<>();
            String reqString = job.get("requirements");
            if (reqString != null && !reqString.isEmpty()) {
                for (String r : reqString.split(";;")) {
                    reqList.add(r);
                }
            }
            i.putStringArrayListExtra("requirements", reqList);

            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, company, remark;

        public ViewHolder(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.tvJobTitle);
            company = v.findViewById(R.id.tvCompany);
            remark = v.findViewById(R.id.tvRemark);
        }
    }
}