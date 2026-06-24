package com.example.prophets_scroll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prophets_scroll.home.DevotionalDetailActivity;
import com.example.prophets_scroll.home.DevotionalsActivity;
import com.example.prophets_scroll.home.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnMenu, btnNotification;
    private MaterialCardView cardDevotion, cardDevotionals, cardBible, cardWorship, cardNotes;
    private MaterialCardView cardVerseOfDay, cardFavorites, cardSearch;
    private TextView tvDate, tvDevotionalTitle, tvDevotionalVerse;
    private MaterialButton btnReadNow;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setTodayDate();
        loadTodaysDevotional();
        setupClickListeners();
        setupBottomNavigation();
    }

    private void initViews() {
        btnMenu = findViewById(R.id.btnMenu);
        btnNotification = findViewById(R.id.btnNotification);
        cardDevotion = findViewById(R.id.cardDevotion);
        tvDate = findViewById(R.id.tvDate);
        tvDevotionalTitle = findViewById(R.id.tvDevotionalTitle);
        tvDevotionalVerse = findViewById(R.id.tvDevotionalVerse);
        btnReadNow = findViewById(R.id.btnReadNow);
        cardVerseOfDay = findViewById(R.id.cardVerseOfDay);
        cardFavorites = findViewById(R.id.cardFavorites);
        cardSearch = findViewById(R.id.cardSearch);
        cardDevotionals = findViewById(R.id.cardDevotionals);
        cardBible = findViewById(R.id.cardBible);
        cardWorship = findViewById(R.id.cardWorship);
        cardNotes = findViewById(R.id.cardNotes);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(new Date()));
    }

    private void loadTodaysDevotional() {
        tvDevotionalTitle.setText("Walking in Faith");
        tvDevotionalVerse.setText("For we walk by faith, not by sight...");
    }

    private void setupClickListeners() {
        btnMenu.setOnClickListener(v ->
                Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show());

        btnNotification.setOnClickListener(v ->
                Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show());

        // Devotional card → Devotionals list
        cardDevotion.setOnClickListener(v -> openDevotionalDetail());
        btnReadNow.setOnClickListener(v -> openDevotionalDetail());

        // Verse of day → Bible (TODO)
        cardVerseOfDay.setOnClickListener(v ->
                Toast.makeText(this, "Opening Bible...", Toast.LENGTH_SHORT).show());

        // Quick access
        cardSearch.setOnClickListener(v -> openSearch());
        cardFavorites.setOnClickListener(v ->
                Toast.makeText(this, "Favorites — coming soon", Toast.LENGTH_SHORT).show());

        // Feature cards
        cardDevotionals.setOnClickListener(v -> openDevotionals());

        cardBible.setOnClickListener(v ->
                Toast.makeText(this, "Opening Bible...", Toast.LENGTH_SHORT).show());

        cardWorship.setOnClickListener(v ->
                Toast.makeText(this, "Worship Music — coming soon!", Toast.LENGTH_SHORT).show());

        cardNotes.setOnClickListener(v ->
                Toast.makeText(this, "Notes — coming soon!", Toast.LENGTH_SHORT).show());
    }

    private void openDevotionals() {
        startActivity(new Intent(this, DevotionalsActivity.class));
    }

    private void openSearch() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    private void setupBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_library) {
                openDevotionals();
                return true;
            } else if (id == R.id.nav_favorites) {
                Toast.makeText(this, "Favorites — coming soon", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_profile) {
                Toast.makeText(this, "Profile — coming soon", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void openDevotionalDetail() {
        Intent intent = new Intent(this, DevotionalDetailActivity.class);

        // Pass today's devotional data as extras
        intent.putExtra(DevotionalDetailActivity.EXTRA_TITLE,
                tvDevotionalTitle.getText().toString());

        intent.putExtra(DevotionalDetailActivity.EXTRA_DATE,
                tvDate.getText().toString());

        // Hardcoded for now — swap with your real data model later
        intent.putExtra(DevotionalDetailActivity.EXTRA_CATEGORY,  "Faith");
        intent.putExtra(DevotionalDetailActivity.EXTRA_VERSE,
                "\u201cFor we walk by faith, not by sight.\u201d");
        intent.putExtra(DevotionalDetailActivity.EXTRA_REFERENCE, "2 Corinthians 5:7");
        intent.putExtra(DevotionalDetailActivity.EXTRA_BODY,
                "In our daily walk with God, we often find ourselves at crossroads "
                        + "where the path ahead seems unclear...");

        startActivity(intent);
    }

}