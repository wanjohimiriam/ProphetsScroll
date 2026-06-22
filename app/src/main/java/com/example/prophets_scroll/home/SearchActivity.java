package com.example.prophets_scroll.home;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prophets_scroll.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // TODO: Setup TabLayout (By Category / By Date / By Language)
        // TODO: Setup RecyclerView with search results
        // TODO: Wire up search input with text watcher
        // TODO: Wire up btnShowResults
    }
}