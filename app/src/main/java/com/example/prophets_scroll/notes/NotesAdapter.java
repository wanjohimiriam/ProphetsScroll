package com.example.prophets_scroll.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prophets_scroll.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private OnNoteClickListener listener;

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onDeleteClick(Note note);
    }

    public NotesAdapter(List<Note> notes, OnNoteClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note, listener);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoteTitle;
        TextView tvNoteContent;
        TextView tvNoteDate;
        ImageView btnDeleteNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoteTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvNoteContent = itemView.findViewById(R.id.tvNoteContent);
            tvNoteDate = itemView.findViewById(R.id.tvNoteDate);
            btnDeleteNote = itemView.findViewById(R.id.btnDeleteNote);
        }

        public void bind(Note note, OnNoteClickListener listener) {
            // Title
            if (note.getTitle() != null && !note.getTitle().trim().isEmpty()) {
                tvNoteTitle.setText(note.getTitle());
            } else {
                tvNoteTitle.setText(R.string.note_title_placeholder);
            }

            // Content preview
            if (note.getContent() != null && !note.getContent().trim().isEmpty()) {
                tvNoteContent.setText(note.getContent());
                tvNoteContent.setVisibility(View.VISIBLE);
            } else {
                tvNoteContent.setVisibility(View.GONE);
            }

            // Date
            tvNoteDate.setText(formatDate(note.getTimestamp()));

            // Click listeners
            itemView.setOnClickListener(v -> listener.onNoteClick(note));
            btnDeleteNote.setOnClickListener(v -> listener.onDeleteClick(note));
        }

        private String formatDate(long timestamp) {
            long now = System.currentTimeMillis();
            long diff = now - timestamp;

            // Just now (less than 1 minute)
            if (diff < 60000) {
                return "Just now";
            }

            // Minutes ago
            if (diff < 3600000) {
                int minutes = (int) (diff / 60000);
                return minutes + " min ago";
            }

            // Hours ago (today)
            if (diff < 86400000) {
                int hours = (int) (diff / 3600000);
                return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
            }

            // Yesterday
            if (diff < 172800000) {
                return "Yesterday";
            }

            // Date format
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }
}
