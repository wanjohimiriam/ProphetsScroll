package com.example.prophets_scroll.home;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prophets_scroll.R;

public class DevotionalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devotionals);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            // TODO: focus search bar
        });

        // TODO: Setup RecyclerView with DevotionalsAdapter
        // TODO: Setup TabLayout filter listener
    }
}