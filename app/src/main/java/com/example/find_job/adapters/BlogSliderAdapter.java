package com.example.find_job.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.BlogItem;

import java.util.List;

public class BlogSliderAdapter extends RecyclerView.Adapter<BlogSliderAdapter.SliderViewHolder> {

    private final List<BlogItem> items;

    public BlogSliderAdapter(List<BlogItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blog_slide, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        BlogItem item = items.get(position);

        holder.imgBanner.setImageResource(item.imageRes);
        holder.tvTitle.setText(item.title);
        holder.tvSubtitle.setText(item.subtitle);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBanner;
        TextView tvTitle, tvSubtitle;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBanner = itemView.findViewById(R.id.imgBanner);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
        }
    }
}
