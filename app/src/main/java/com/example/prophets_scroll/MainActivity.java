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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnMenu, btnNotification;
    private MaterialCardView cardDevotion, cardDevotionals, cardBible, cardDevotionalSongs, cardNotes;
    private MaterialCardView cardVerseOfDay, cardFavorites, cardSearch, cardMorningCloudTV, cardApostolicConcordance;
    private TextView tvDate, tvDevotionalTitle, tvDevotionalVerse;
    private TextView tvVerseText, tvVerseReference;
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
        tvVerseText = findViewById(R.id.tvVerseText);
        tvVerseReference = findViewById(R.id.tvVerseReference);
        cardFavorites = findViewById(R.id.cardFavorites);
        cardSearch = findViewById(R.id.cardSearch);
        cardDevotionals = findViewById(R.id.cardDevotionals);
        cardBible = findViewById(R.id.cardBible);
        cardDevotionalSongs = findViewById(R.id.cardDevotionalSongs);
        cardNotes = findViewById(R.id.cardNotes);
        cardMorningCloudTV = findViewById(R.id.cardMorningCloudTV);
        cardApostolicConcordance = findViewById(R.id.cardApostolicConcordance);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(new Date()));
    }

    private void loadTodaysDevotional() {
        tvDevotionalTitle.setText("Pressing Forward Into New Dimensions");
        tvDevotionalVerse.setText("\"Not that I have already attained, or am already perfected; but I press on...\"");
        
        // Populate verse of the day
        tvVerseText.setText("\"Not that I have already attained, or am already perfected; but I press on, that I may lay hold of that for which Christ Jesus has also laid hold of me.\" \"Brethren, I do not count myself to have apprehended; but one thing I do, forgetting those things which are behind and reaching forward to those things which are ahead, I press toward the goal for the prize of the upward call of God in Christ Jesus.\"");
        tvVerseReference.setText("Philippians 3:12-14 NKJV");
    }

    private void setupClickListeners() {
        btnMenu.setOnClickListener(v ->
                Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show());

        btnNotification.setOnClickListener(v ->
                Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show());

        // Devotional card → Devotionals list
        cardDevotion.setOnClickListener(v -> openDevotionalDetail());
        btnReadNow.setOnClickListener(v -> openDevotionalDetail());

        // Verse of day → Bible
        cardVerseOfDay.setOnClickListener(v -> openBible());

        // Quick access
        cardSearch.setOnClickListener(v -> openSearch());
        cardFavorites.setOnClickListener(v ->
                startActivity(new Intent(this, com.example.prophets_scroll.favorites.FavoritesActivity.class)));

        // Feature cards
        cardDevotionals.setOnClickListener(v -> openDevotionals());

        cardBible.setOnClickListener(v -> openBible());

        cardDevotionalSongs.setOnClickListener(v ->
                Toast.makeText(this, "Devotional Songs — coming soon!", Toast.LENGTH_SHORT).show());

        cardNotes.setOnClickListener(v ->
                startActivity(new Intent(this, com.example.prophets_scroll.notes.NotesListActivity.class)));

        cardMorningCloudTV.setOnClickListener(v ->
                startActivity(new Intent(this, com.example.prophets_scroll.streaming.VideoPlayerActivity.class)));

        cardApostolicConcordance.setOnClickListener(v ->
                Toast.makeText(this, "Apostolic Concordance — coming soon!", Toast.LENGTH_SHORT).show());
    }

    private void openDevotionals() {
        startActivity(new Intent(this, DevotionalsActivity.class));
    }

    private void openBible() {
        startActivity(new Intent(this, com.example.prophets_scroll.bible.BibleReaderActivity.class));
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
                startActivity(new Intent(this, com.example.prophets_scroll.favorites.FavoritesActivity.class));
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
        intent.putExtra(DevotionalDetailActivity.EXTRA_TITLE, "Pressing Forward Into New Dimensions");
        intent.putExtra(DevotionalDetailActivity.EXTRA_DATE, "Wednesday 1st July 2026");
        intent.putExtra(DevotionalDetailActivity.EXTRA_CATEGORY, "Faith");
        intent.putExtra(DevotionalDetailActivity.EXTRA_VERSE,
                "\"Not that I have already attained, or am already perfected; but I press on, that I may lay hold of that for which Christ Jesus has also laid hold of me.\" \"Brethren, I do not count myself to have apprehended; but one thing I do, forgetting those things which are behind and reaching forward to those things which are ahead, I press toward the goal for the prize of the upward call of God in Christ Jesus.\"");
        intent.putExtra(DevotionalDetailActivity.EXTRA_REFERENCE, "Philippians 3:12-14 NKJV");
        // Body will be loaded from raw file in DevotionalDetailActivity

        startActivity(intent);
    }

    private String loadDevotionalFromRaw() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.devotional_july_1_2026);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
            reader.close();
            return text.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to load devotional content.";
        }
    }

}