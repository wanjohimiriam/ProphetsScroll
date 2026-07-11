package com.example.prophets_scroll.notes;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NotesManager {
    private static NotesManager instance;
    private static final String PREFS_NAME = "NotesPrefs";
    private static final String KEY_NOTES = "notes";
    
    private SharedPreferences prefs;
    private Gson gson;

    private NotesManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static NotesManager getInstance(Context context) {
        if (instance == null) {
            instance = new NotesManager(context);
        }
        return instance;
    }

    public List<Note> getAllNotes() {
        String json = prefs.getString(KEY_NOTES, null);
        if (json == null) {
            return new ArrayList<>();
        }
        
        Type type = new TypeToken<List<Note>>(){}.getType();
        List<Note> notes = gson.fromJson(json, type);
        
        // Sort by timestamp (newest first)
        if (notes != null) {
            notes.sort((n1, n2) -> Long.compare(n2.getTimestamp(), n1.getTimestamp()));
            return notes;
        }
        
        return new ArrayList<>();
    }

    public Note getNoteById(String id) {
        List<Note> notes = getAllNotes();
        for (Note note : notes) {
            if (note.getId().equals(id)) {
                return note;
            }
        }
        return null;
    }

    public void saveNote(Note note) {
        List<Note> notes = getAllNotes();
        
        // Update existing or add new
        boolean found = false;
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId().equals(note.getId())) {
                notes.set(i, note);
                found = true;
                break;
            }
        }
        
        if (!found) {
            notes.add(note);
        }
        
        saveNotes(notes);
    }

    public void deleteNote(String id) {
        List<Note> notes = getAllNotes();
        notes.removeIf(note -> note.getId().equals(id));
        saveNotes(notes);
    }

    private void saveNotes(List<Note> notes) {
        String json = gson.toJson(notes);
        prefs.edit().putString(KEY_NOTES, json).apply();
    }
}
