package com.example.prophets_scroll.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prophets_scroll.R;
import com.example.prophets_scroll.onboard.PersonalizationStep1Activity.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(List<Category> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        CardView cardCategory;
        TextView tvCategoryLabel;
        ImageView ivCheckmark;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardCategory = itemView.findViewById(R.id.cardCategory);
            tvCategoryLabel = itemView.findViewById(R.id.tvCategoryLabel);
            ivCheckmark = itemView.findViewById(R.id.ivCheckmark);
        }

        void bind(Category category) {
            tvCategoryLabel.setText(category.getName());

            // Show/hide checkmark
            ivCheckmark.setVisibility(category.isSelected() ? View.VISIBLE : View.GONE);

            // Change card appearance based on selection
            if (category.isSelected()) {
                cardCategory.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.primary_purple_start)
                );
                tvCategoryLabel.setTextColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.text_white)
                );
            } else {
                cardCategory.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.background_secondary)
                );
                tvCategoryLabel.setTextColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.text_primary)
                );
            }

            // Click listener
            cardCategory.setOnClickListener(v -> {
                listener.onCategoryClick(category);
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}