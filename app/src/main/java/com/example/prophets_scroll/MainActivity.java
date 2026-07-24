package com.example.prophets_scroll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prophets_scroll.models.devotionals.Devotional;
import com.example.prophets_scroll.home.DevotionalRepository;
import com.example.prophets_scroll.home.RawTextReader;
import com.example.prophets_scroll.home.DevotionalDetailActivity;
import com.example.prophets_scroll.home.DevotionalsActivity;
import com.example.prophets_scroll.home.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnMenu, btnNotification;
    private MaterialCardView cardDevotion, cardDevotionals, cardBible, cardDevotionalSongs, cardNotes;
    private MaterialCardView cardVerseOfDay, cardFavorites, cardSearch, cardMorningCloudTV, cardApostolicConcordance;
    private TextView tvDate, tvDevotionalTitle, tvDevotionalVerse;
    private TextView tvVerseText, tvVerseReference;
    private MaterialButton btnReadNow;
    private BottomNavigationView bottomNavigation;

    // Resolved once in onCreate so both the devotional card AND the
    // "Verse of the Day" card read from the SAME source -- no more two
    // independently hardcoded copies of the same verse to keep in sync.
    private Devotional todaysDevotional;
    private RawTextReader.ScripturePreview todaysScripture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setTodayDate();
        resolveTodaysDevotional();
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

    /**
     * TEMPORARY: pinned to July 5's devotional specifically.
     *
     * july_1 (your own hand-pasted file) doesn't follow the
     * "TODAY'S DEVOTIONAL (date)" + quoted-verse structure
     * RawTextReader.parseOpeningScripture() looks for, so it wasn't
     * producing a usable verse. july_2 onward use that structure and
     * parse cleanly -- picking july_5 here just needed ONE working example
     * to show on the home screen while more real daily entries get added.
     *
     * Once there's real day-by-day coverage (and july_1 either gets
     * reformatted or its own verse set explicitly), swap this back to
     * matching today's actual date -- see the dated-match version this
     * replaced, in git history / earlier in this conversation.
     */
    private void resolveTodaysDevotional() {
        todaysDevotional = DevotionalRepository.getById("devotional_july_5_2026");
        if (todaysDevotional == null) {
            List<Devotional> all = DevotionalRepository.getAll();
            if (!all.isEmpty()) todaysDevotional = all.get(0);
        }

        if (todaysDevotional != null) {
            String body = RawTextReader.read(this, todaysDevotional.getContentRawRes());
            todaysScripture = RawTextReader.parseOpeningScripture(body);
        }
    }

    private void loadTodaysDevotional() {
        if (todaysDevotional == null || todaysScripture == null) {
            // No devotionals in the repository at all -- shouldn't happen
            // once DevotionalRepository is populated, but don't crash.
            tvDevotionalTitle.setText("");
            tvDevotionalVerse.setText("");
            tvVerseText.setText("");
            tvVerseReference.setText("");
            return;
        }

        // tvDevotionalTitle shows CATEGORY, not a title -- see
        // DevotionalRepository's class comment for why there's no title
        // anywhere in this pipeline.
        tvDevotionalTitle.setText(todaysDevotional.getCategory());
        tvDevotionalVerse.setText(todaysScripture.verseText);

        // "Verse of the Day" card -- same extraction, same source, so it's
        // never out of sync with what the devotional itself actually says.
        tvVerseText.setText(todaysScripture.verseText);
        tvVerseReference.setText(
                todaysScripture.reference != null ? todaysScripture.reference : "");
    }

    private void setupClickListeners() {
        btnMenu.setOnClickListener(v ->
                Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show());

        btnNotification.setOnClickListener(v ->
                Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show());

        cardDevotion.setOnClickListener(v -> openDevotionalDetail());
        btnReadNow.setOnClickListener(v -> openDevotionalDetail());

        cardVerseOfDay.setOnClickListener(v -> openBible());

        cardSearch.setOnClickListener(v -> openSearch());
        cardFavorites.setOnClickListener(v ->
                startActivity(new Intent(this, com.example.prophets_scroll.favorites.FavoritesActivity.class)));

        cardDevotionals.setOnClickListener(v -> openDevotionals());

        cardBible.setOnClickListener(v -> openBible());

        cardDevotionalSongs.setOnClickListener(v ->
                Toast.makeText(this, "Devotional Songs \u2014 coming soon!", Toast.LENGTH_SHORT).show());

        cardNotes.setOnClickListener(v ->
                startActivity(new Intent(this, com.example.prophets_scroll.notes.NotesListActivity.class)));

        cardMorningCloudTV.setOnClickListener(v ->
                startActivity(new Intent(this, com.example.prophets_scroll.streaming.VideoPlayerActivity.class)));

        cardApostolicConcordance.setOnClickListener(v ->
                Toast.makeText(this, "Apostolic Concordance \u2014 coming soon!", Toast.LENGTH_SHORT).show());
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
                Toast.makeText(this, "Profile \u2014 coming soon", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void openDevotionalDetail() {
        if (todaysDevotional == null || todaysScripture == null) return;

        Intent intent = new Intent(this, DevotionalDetailActivity.class);
        // FIX: was EXTRA_TITLE, which no longer exists on DevotionalDetailActivity
        // (renamed to EXTRA_ID when the title field was dropped project-wide).
        intent.putExtra(DevotionalDetailActivity.EXTRA_ID, todaysDevotional.getId());
        intent.putExtra(DevotionalDetailActivity.EXTRA_DATE, todaysDevotional.getDayLabel());
        intent.putExtra(DevotionalDetailActivity.EXTRA_CATEGORY, todaysDevotional.getCategory());
        intent.putExtra(DevotionalDetailActivity.EXTRA_VERSE, todaysScripture.verseText);
        intent.putExtra(DevotionalDetailActivity.EXTRA_REFERENCE,
                todaysScripture.reference != null ? todaysScripture.reference : "");
        intent.putExtra(DevotionalDetailActivity.EXTRA_BODY,
                RawTextReader.read(this, todaysDevotional.getContentRawRes()));

        startActivity(intent);
    }
}