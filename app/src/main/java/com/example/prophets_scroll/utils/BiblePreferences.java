package com.example.prophets_scroll.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class BiblePreferences {

    private static final String PREF_NAME = "BiblePreferences";
    private static final String KEY_SELECTED_TRANSLATION = "selected_translation";
    private static final String KEY_LAST_BOOK = "last_book";
    private static final String KEY_LAST_CHAPTER = "last_chapter";
    
    // Default to King James Version
    private static final String DEFAULT_TRANSLATION = "kjv";

    private final SharedPreferences prefs;

    public BiblePreferences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Translation
    public void setSelectedTranslation(String translation) {
        prefs.edit().putString(KEY_SELECTED_TRANSLATION, translation).apply();
    }

    public String getSelectedTranslation() {
        return prefs.getString(KEY_SELECTED_TRANSLATION, DEFAULT_TRANSLATION);
    }

    // Last read position
    public void setLastReadPosition(String book, int chapter) {
        prefs.edit()
                .putString(KEY_LAST_BOOK, book)
                .putInt(KEY_LAST_CHAPTER, chapter)
                .apply();
    }

    public String getLastBook() {
        return prefs.getString(KEY_LAST_BOOK, "Genesis");
    }

    public int getLastChapter() {
        return prefs.getInt(KEY_LAST_CHAPTER, 1);
    }

    // Clear all
    public void clear() {
        prefs.edit().clear().apply();
    }
}
