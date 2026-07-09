package com.example.prophets_scroll.bible;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prophets_scroll.R;
import com.example.prophets_scroll.models.bible.BibleBook;

import java.util.ArrayList;
import java.util.List;

public class BibleBooksAdapter extends RecyclerView.Adapter<BibleBooksAdapter.BookViewHolder> {

    private List<BibleBook> books;
    private List<BibleBook> booksFiltered;
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(BibleBook book);
    }

    public BibleBooksAdapter(OnBookClickListener listener) {
        this.books = new ArrayList<>();
        this.booksFiltered = new ArrayList<>();
        this.listener = listener;
    }

    public void setBooks(List<BibleBook> books) {
        this.books = books;
        this.booksFiltered = new ArrayList<>(books);
        notifyDataSetChanged();
    }

    public void filter(String testament) {
        booksFiltered.clear();
        
        if (testament.equals("All")) {
            booksFiltered.addAll(books);
        } else if (testament.equals("OT")) {
            // Old Testament: First 39 books
            for (int i = 0; i < Math.min(39, books.size()); i++) {
                booksFiltered.add(books.get(i));
            }
        } else if (testament.equals("NT")) {
            // New Testament: Books from index 39 onwards
            for (int i = 39; i < books.size(); i++) {
                booksFiltered.add(books.get(i));
            }
        }
        
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bible_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BibleBook book = booksFiltered.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return booksFiltered.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookName;
        TextView tvChapterCount;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvChapterCount = itemView.findViewById(R.id.tvChapterCount);
        }

        public void bind(BibleBook book) {
            tvBookName.setText(book.getName());
            tvChapterCount.setText(book.getChapters() + " chapters");
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(book);
                }
            });
        }
    }
}
