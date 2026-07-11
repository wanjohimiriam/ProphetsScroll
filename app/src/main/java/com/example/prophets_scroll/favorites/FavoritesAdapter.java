package com.example.prophets_scroll.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prophets_scroll.R;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_DEVOTIONAL = 0;
    private static final int VIEW_TYPE_SONG = 1;

    private List<FavoriteItem> items;
    private OnFavoriteActionListener listener;
    private FavoriteItem.Type filterType;

    public interface OnFavoriteActionListener {
        void onItemClick(FavoriteItem item);
        void onRemoveFavorite(FavoriteItem item);
        void onPlaySong(FavoriteItem item);
    }

    public FavoritesAdapter(List<FavoriteItem> items, OnFavoriteActionListener listener,
                            FavoriteItem.Type filterType) {
        this.items = items;
        this.listener = listener;
        this.filterType = filterType;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType() == FavoriteItem.Type.DEVOTIONAL
                ? VIEW_TYPE_DEVOTIONAL : VIEW_TYPE_SONG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DEVOTIONAL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_favorite_devotional, parent, false);
            return new DevotionalViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_favorite_song, parent, false);
            return new SongViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FavoriteItem item = items.get(position);
        if (holder instanceof DevotionalViewHolder) {
            ((DevotionalViewHolder) holder).bind(item, listener);
        } else if (holder instanceof SongViewHolder) {
            ((SongViewHolder) holder).bind(item, listener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class DevotionalViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvTitle, tvDate;
        ImageView btnFavorite;

        public DevotionalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }

        public void bind(FavoriteItem item, OnFavoriteActionListener listener) {
            tvTitle.setText(item.getTitle());
            tvDate.setText(item.getSubtitle());
            tvCategory.setText(item.getExtra());

            itemView.setOnClickListener(v -> listener.onItemClick(item));
            btnFavorite.setOnClickListener(v -> listener.onRemoveFavorite(item));
        }
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView tvSongTitle, tvArtist, tvDuration;
        ImageView btnPlay, btnFavorite;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSongTitle = itemView.findViewById(R.id.tvSongTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            btnPlay = itemView.findViewById(R.id.btnPlay);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }

        public void bind(FavoriteItem item, OnFavoriteActionListener listener) {
            tvSongTitle.setText(item.getTitle());
            tvArtist.setText(item.getSubtitle());
            tvDuration.setText(item.getExtra());

            itemView.setOnClickListener(v -> listener.onItemClick(item));
            btnPlay.setOnClickListener(v -> listener.onPlaySong(item));
            btnFavorite.setOnClickListener(v -> listener.onRemoveFavorite(item));
        }
    }
}
