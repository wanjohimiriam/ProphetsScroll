package com.example.prophets_scroll.favorites;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavoritesManager {
    private static FavoritesManager instance;
    private static final String PREFS_NAME = "FavoritesPrefs";
    private static final String KEY_FAVORITES = "favorites";
    
    private SharedPreferences prefs;
    private Gson gson;

    private FavoritesManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static FavoritesManager getInstance(Context context) {
        if (instance == null) {
            instance = new FavoritesManager(context);
        }
        return instance;
    }

    public List<FavoriteItem> getAllFavorites() {
        String json = prefs.getString(KEY_FAVORITES, null);
        if (json == null) {
            return new ArrayList<>();
        }
        
        Type type = new TypeToken<List<FavoriteItem>>(){}.getType();
        List<FavoriteItem> favorites = gson.fromJson(json, type);
        
        if (favorites != null) {
            favorites.sort((f1, f2) -> Long.compare(f2.getTimestamp(), f1.getTimestamp()));
            return favorites;
        }
        
        return new ArrayList<>();
    }

    public List<FavoriteItem> getFavoritesByType(FavoriteItem.Type type) {
        return getAllFavorites().stream()
                .filter(item -> item.getType() == type)
                .collect(Collectors.toList());
    }

    public void addFavorite(FavoriteItem item) {
        List<FavoriteItem> favorites = getAllFavorites();
        
        // Don't add duplicate
        for (FavoriteItem fav : favorites) {
            if (fav.getId().equals(item.getId())) {
                return;
            }
        }
        
        favorites.add(item);
        saveFavorites(favorites);
    }

    public void removeFavorite(String id) {
        List<FavoriteItem> favorites = getAllFavorites();
        favorites.removeIf(item -> item.getId().equals(id));
        saveFavorites(favorites);
    }

    public boolean isFavorite(String id) {
        List<FavoriteItem> favorites = getAllFavorites();
        for (FavoriteItem item : favorites) {
            if (item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private void saveFavorites(List<FavoriteItem> favorites) {
        String json = gson.toJson(favorites);
        prefs.edit().putString(KEY_FAVORITES, json).apply();
    }

    public int getFavoritesCount() {
        return getAllFavorites().size();
    }

    public int getDevotionalsCount() {
        return getFavoritesByType(FavoriteItem.Type.DEVOTIONAL).size();
    }

    public int getSongsCount() {
        return getFavoritesByType(FavoriteItem.Type.SONG).size();
    }
}
