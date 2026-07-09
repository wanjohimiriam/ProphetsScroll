package com.example.prophets_scroll.bible;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prophets_scroll.R;
import com.example.prophets_scroll.retrofit.ApiClient;
import com.example.prophets_scroll.models.bible.BibleBook;
import com.example.prophets_scroll.utils.BiblePreferences;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BibleActivity extends AppCompatActivity implements BibleBooksAdapter.OnBookClickListener {

    private static final String TAG = "BibleActivity";
    
    private ImageButton btnBack;
    private RecyclerView recyclerBooks;
    private MaterialButton btnFilterAll, btnFilterOT, btnFilterNT;
    
    private BibleBooksAdapter adapter;
    private BiblePreferences prefs;
    private String currentFilter = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible);

        prefs = new BiblePreferences(this);
        
        initViews();
        setupClickListeners();
        loadBibleBooks();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        recyclerBooks = findViewById(R.id.recyclerBooks);
        btnFilterAll = findViewById(R.id.btnFilterAll);
        btnFilterOT = findViewById(R.id.btnFilterOT);
        btnFilterNT = findViewById(R.id.btnFilterNT);
        
        // Setup grid layout with 3 columns
        recyclerBooks.setLayoutManager(new GridLayoutManager(this, 3));
        
        // Setup adapter
        adapter = new BibleBooksAdapter(this);
        recyclerBooks.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());
        
        // Filter buttons
        btnFilterAll.setOnClickListener(v -> applyFilter("All"));
        btnFilterOT.setOnClickListener(v -> applyFilter("OT"));
        btnFilterNT.setOnClickListener(v -> applyFilter("NT"));
    }

    private void applyFilter(String filter) {
        currentFilter = filter;
        adapter.filter(filter);
        
        // Update button states
        resetFilterButtons();
        
        if (filter.equals("All")) {
            btnFilterAll.setBackgroundTintList(getColorStateList(R.color.gold_primary));
            btnFilterAll.setTextColor(getColor(android.R.color.white));
        } else if (filter.equals("OT")) {
            btnFilterOT.setBackgroundTintList(getColorStateList(R.color.gold_primary));
            btnFilterOT.setTextColor(getColor(android.R.color.white));
        } else if (filter.equals("NT")) {
            btnFilterNT.setBackgroundTintList(getColorStateList(R.color.gold_primary));
            btnFilterNT.setTextColor(getColor(android.R.color.white));
        }
    }

    private void resetFilterButtons() {
        // Reset all to default style
        btnFilterAll.setBackgroundTintList(getColorStateList(R.color.feature_card_bg));
        btnFilterAll.setTextColor(getColor(R.color.text_secondary));
        
        btnFilterOT.setBackgroundTintList(getColorStateList(R.color.feature_card_bg));
        btnFilterOT.setTextColor(getColor(R.color.text_secondary));
        
        btnFilterNT.setBackgroundTintList(getColorStateList(R.color.feature_card_bg));
        btnFilterNT.setTextColor(getColor(R.color.text_secondary));
    }

    private void loadBibleBooks() {
        String translation = prefs.getSelectedTranslation();
        
        ApiClient.getBibleApiService()
                .getBooks(translation)
                .enqueue(new Callback<List<BibleBook>>() {
                    @Override
                    public void onResponse(Call<List<BibleBook>> call, Response<List<BibleBook>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<BibleBook> books = response.body();
                            adapter.setBooks(books);
                        } else {
                            Toast.makeText(BibleActivity.this, 
                                "Failed to load books", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BibleBook>> call, Throwable t) {
                        Log.e(TAG, "API Error", t);
                        Toast.makeText(BibleActivity.this, 
                            "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBookClick(BibleBook book) {
        openBook(book);
    }
    
    private void openBook(BibleBook book) {
        // TODO: Open chapter selection screen
        Toast.makeText(this, "Opening " + book.getName(), Toast.LENGTH_SHORT).show();
    }
}
