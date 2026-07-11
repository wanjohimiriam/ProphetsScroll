package com.example.prophets_scroll.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prophets_scroll.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListFragment extends Fragment {

    private static final String ARG_TYPE = "type";

    private FavoriteItem.Type type;
    private RecyclerView rvFavorites;
    private LinearLayout llEmptyState;
    private TextView tvEmptySubtitle;
    private List<FavoriteItem> favoritesList;
    private FavoritesAdapter adapter;

    public static FavoriteListFragment newInstance(FavoriteItem.Type type) {
        FavoriteListFragment fragment = new FavoriteListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = FavoriteItem.Type.valueOf(getArguments().getString(ARG_TYPE));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        llEmptyState = view.findViewById(R.id.llEmptyState);
        tvEmptySubtitle = view.findViewById(R.id.tvEmptySubtitle);

        setupRecyclerView();
        loadFavorites();
    }

    private void setupRecyclerView() {
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        favoritesList = new ArrayList<>();
        adapter = new FavoritesAdapter(favoritesList, new FavoritesAdapter.OnFavoriteActionListener() {
            @Override
            public void onItemClick(FavoriteItem item) {
                Toast.makeText(getContext(), "Open: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRemoveFavorite(FavoriteItem item) {
                FavoritesManager.getInstance(requireContext()).removeFavorite(item.getId());
                loadFavorites();
                Toast.makeText(getContext(), R.string.toast_removed_from_favorites,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlaySong(FavoriteItem item) {
                Toast.makeText(getContext(), "Playing: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        }, type);
        rvFavorites.setAdapter(adapter);
    }

    private void loadFavorites() {
        favoritesList.clear();
        favoritesList.addAll(FavoritesManager.getInstance(requireContext()).getFavoritesByType(type));
        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (favoritesList.isEmpty()) {
            llEmptyState.setVisibility(View.VISIBLE);
            rvFavorites.setVisibility(View.GONE);
            
            if (type == FavoriteItem.Type.DEVOTIONAL) {
                tvEmptySubtitle.setText(R.string.favorites_devotionals_empty);
            } else {
                tvEmptySubtitle.setText(R.string.favorites_songs_empty);
            }
        } else {
            llEmptyState.setVisibility(View.GONE);
            rvFavorites.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }
}
