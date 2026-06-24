package com.example.prophets_scroll.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prophets_scroll.R;

import java.util.List;

public class DevotionalsAdapter extends RecyclerView.Adapter<DevotionalsAdapter.ViewHolder> {

    public interface OnItemClick {
        void onClick(DevotionalsActivity.DevotionalItem item);
    }

    private List<DevotionalsActivity.DevotionalItem> items;
    private final OnItemClick listener;

    public DevotionalsAdapter(List<DevotionalsActivity.DevotionalItem> items, OnItemClick listener) {
        this.items    = items;
        this.listener = listener;
    }

    public void updateData(List<DevotionalsActivity.DevotionalItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_devotional_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DevotionalsActivity.DevotionalItem item = items.get(position);
        holder.tvCategory.setText(item.category);
        holder.tvDate.setText(item.date);
        // TODO: load real thumbnail via Glide/Picasso using item.imageUrl
        // Glide.with(holder.ivThumbnail).load(item.imageUrl).into(holder.ivThumbnail);
        holder.itemView.setOnClickListener(v -> listener.onClick(item));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvCategory, tvDate;

        ViewHolder(View v) {
            super(v);
            ivThumbnail  = v.findViewById(R.id.ivThumbnail);
            tvCategory   = v.findViewById(R.id.tvCardCategory);
            tvDate       = v.findViewById(R.id.tvCardDate);
        }
    }
}