package com.example.prophets_scroll.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prophets_scroll.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity {

    private RecyclerView rvNotes;
    private LinearLayout llEmptyState;
    private FloatingActionButton fabAddNote;
    private NotesAdapter adapter;
    private List<Note> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        initViews();
        setupToolbar();
        loadNotes();
        updateEmptyState();
    }

    private void initViews() {
        rvNotes = findViewById(R.id.rvNotes);
        llEmptyState = findViewById(R.id.llEmptyState);
        fabAddNote = findViewById(R.id.fabAddNote);

        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        notesList = new ArrayList<>();
        adapter = new NotesAdapter(notesList, new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                openNoteEditor(note);
            }

            @Override
            public void onDeleteClick(Note note) {
                deleteNote(note);
            }
        });
        rvNotes.setAdapter(adapter);

        fabAddNote.setOnClickListener(v -> openNoteEditor(null));
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadNotes() {
        // TODO: Load from database/SharedPreferences
        // For now, using mock data
        notesList.clear();
        notesList.addAll(NotesManager.getInstance(this).getAllNotes());
        adapter.notifyDataSetChanged();
    }

    private void updateEmptyState() {
        if (notesList.isEmpty()) {
            llEmptyState.setVisibility(View.VISIBLE);
            rvNotes.setVisibility(View.GONE);
        } else {
            llEmptyState.setVisibility(View.GONE);
            rvNotes.setVisibility(View.VISIBLE);
        }
    }

    private void openNoteEditor(Note note) {
        Intent intent = new Intent(this, NoteEditorActivity.class);
        if (note != null) {
            intent.putExtra("note_id", note.getId());
        }
        startActivity(intent);
    }

    private void deleteNote(Note note) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_note_title)
                .setMessage(R.string.dialog_delete_note_message)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    NotesManager.getInstance(this).deleteNote(note.getId());
                    loadNotes();
                    updateEmptyState();
                    android.widget.Toast.makeText(this, R.string.toast_note_deleted,
                            android.widget.Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
        updateEmptyState();
    }
}
