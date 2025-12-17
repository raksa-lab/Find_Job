package com.example.find_job.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.BlogItem;

import java.util.List;

public class BlogSliderAdapter extends RecyclerView.Adapter<BlogSliderAdapter.ViewHolder> {

    private final List<BlogItem> blogs;

    public BlogSliderAdapter(List<BlogItem> blogs) {
        this.blogs = blogs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blog_slide, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        BlogItem blog = blogs.get(position);
        holder.tvTitle.setText(blog.title);
        holder.tvSubtitle.setText(blog.subtitle);
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubtitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
        }
    }
}
