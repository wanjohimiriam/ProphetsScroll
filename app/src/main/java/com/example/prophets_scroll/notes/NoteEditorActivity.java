package com.example.prophets_scroll.notes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prophets_scroll.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteEditorActivity extends AppCompatActivity {

    private TextInputEditText etTitle;
    private TextInputEditText etContent;
    private TextView tvDate;
    private ImageView btnSave;
    private ImageView btnDelete;

    private Note currentNote;
    private boolean isNewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        initViews();
        setupToolbar();
        loadNote();
        setupListeners();
    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        tvDate = findViewById(R.id.tvDate);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadNote() {
        String noteId = getIntent().getStringExtra("note_id");
        
        if (noteId != null) {
            // Editing existing note
            currentNote = NotesManager.getInstance(this).getNoteById(noteId);
            isNewNote = false;
            
            if (currentNote != null) {
                etTitle.setText(currentNote.getTitle());
                etContent.setText(currentNote.getContent());
                updateDateDisplay(currentNote.getTimestamp());
                btnDelete.setVisibility(View.VISIBLE);
            }
        } else {
            // New note
            currentNote = new Note();
            isNewNote = true;
            updateDateDisplay(currentNote.getTimestamp());
            btnDelete.setVisibility(View.GONE);
        }

        // Focus on content field for new notes
        if (isNewNote) {
            etContent.requestFocus();
        }
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveNote());
        
        btnDelete.setOnClickListener(v -> deleteNote());

        // Auto-update date on text change
        TextWatcher dateUpdater = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateDateDisplay(System.currentTimeMillis());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etTitle.addTextChangedListener(dateUpdater);
        etContent.addTextChangedListener(dateUpdater);
    }

    private void saveNote() {
        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String content = etContent.getText() != null ? etContent.getText().toString().trim() : "";

        // Don't save empty notes
        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, "Note is empty", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentNote.setTitle(title);
        currentNote.setContent(content);
        currentNote.setTimestamp(System.currentTimeMillis());

        NotesManager.getInstance(this).saveNote(currentNote);
        
        Toast.makeText(this, R.string.toast_note_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteNote() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_note_title)
                .setMessage(R.string.dialog_delete_note_message)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    NotesManager.getInstance(this).deleteNote(currentNote.getId());
                    Toast.makeText(this, R.string.toast_note_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void updateDateDisplay(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault());
        tvDate.setText(sdf.format(new Date(timestamp)));
    }

    @Override
    public void onBackPressed() {
        // Auto-save on back press
        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String content = etContent.getText() != null ? etContent.getText().toString().trim() : "";
        
        if (!title.isEmpty() || !content.isEmpty()) {
            saveNote();
        } else {
            super.onBackPressed();
        }
    }
}
