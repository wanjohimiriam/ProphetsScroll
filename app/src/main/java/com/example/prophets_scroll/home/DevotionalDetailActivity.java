package com.example.prophets_scroll.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.prophets_scroll.R;
import com.google.android.material.imageview.ShapeableImageView;

public class DevotionalDetailActivity extends AppCompatActivity {

    // ── Intent keys (whoever starts this Activity must pass these) ──
    public static final String EXTRA_TITLE     = "extra_title";
    public static final String EXTRA_DATE      = "extra_date";
    public static final String EXTRA_CATEGORY  = "extra_category";
    public static final String EXTRA_VERSE     = "extra_verse";
    public static final String EXTRA_REFERENCE = "extra_reference";
    public static final String EXTRA_BODY      = "extra_body";

    // ── Views ──
    private Toolbar toolbar;
    private ShapeableImageView ivDevotionalThumb;
    private TextView tvToolbarTitle, tvToolbarSubtitle;
    private ImageButton btnBack, btnFavourite, btnShare, btnMore;

    private TextView tvCategory, tvDate;
    private TextView tvTitle, tvKeyVerse, tvVerseReference, tvBody;

    // ── State ──
    private boolean isFavourited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devotional_detail);

        initViews();
        setupToolbar();
        populateContent();
        setupClickListeners();
    }

    // ─────────────────────────────────────────────
    //  1. INIT
    // ─────────────────────────────────────────────
    private void initViews() {
        toolbar            = findViewById(R.id.toolbar);
        ivDevotionalThumb  = findViewById(R.id.ivDevotionalThumb);
        tvToolbarTitle     = findViewById(R.id.tvToolbarTitle);
        tvToolbarSubtitle  = findViewById(R.id.tvToolbarSubtitle);

        btnBack            = findViewById(R.id.btnBack);
        btnFavourite       = findViewById(R.id.btnFavourite);
        btnShare           = findViewById(R.id.btnShare);
        btnMore            = findViewById(R.id.btnMore);

        tvCategory         = findViewById(R.id.tvCategory);
        tvDate             = findViewById(R.id.tvDate);
        tvTitle            = findViewById(R.id.tvTitle);
        tvKeyVerse         = findViewById(R.id.tvKeyVerse);
        tvVerseReference   = findViewById(R.id.tvVerseReference);
        tvBody             = findViewById(R.id.tvBody);
    }

    // ─────────────────────────────────────────────
    //  2. TOOLBAR
    // ─────────────────────────────────────────────
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        // We handle the back button manually so hide the default one
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    // ─────────────────────────────────────────────
    //  3. POPULATE from Intent extras
    //     Falls back to placeholder text if nothing passed
    // ─────────────────────────────────────────────
    private void populateContent() {
        Intent intent = getIntent();

        String title     = intent.getStringExtra(EXTRA_TITLE);
        String date      = intent.getStringExtra(EXTRA_DATE);
        String category  = intent.getStringExtra(EXTRA_CATEGORY);
        String verse     = intent.getStringExtra(EXTRA_VERSE);
        String reference = intent.getStringExtra(EXTRA_REFERENCE);
        String body      = intent.getStringExtra(EXTRA_BODY);

        // Fallbacks so screen never looks empty during development
        if (title     == null) title     = "Walking in Faith Through Uncertain Times";
        if (date      == null) date      = "May 25, 2026";
        if (category  == null) category  = "Faith";
        if (verse     == null) verse     = "\u201cFor we walk by faith, not by sight.\u201d";
        if (reference == null) reference = "2 Corinthians 5:7";
        if (body      == null) body      = "In our daily walk with God, we often find ourselves "
                + "at crossroads where the path ahead seems unclear. The natural human response "
                + "is to seek certainty, to demand visible proof before taking the next step. "
                + "Yet, God calls us to something deeper\u2014a trust that transcends what our "
                + "eyes can see.\n\nFaith is not the absence of doubt, nor is it blind optimism "
                + "in the face of difficulty. Rather, it is the courageous choice to trust in "
                + "God\u2019s character when His plan is not yet visible.";

        // Toolbar
        tvToolbarTitle.setText(title);
        tvToolbarSubtitle.setText(date);

        // Body
        tvCategory.setText(category);
        tvDate.setText(date);
        tvTitle.setText(title);
        tvKeyVerse.setText(verse);
        tvVerseReference.setText(reference);
        tvBody.setText(body);
    }

    // ─────────────────────────────────────────────
    //  4. CLICK LISTENERS
    // ─────────────────────────────────────────────
    private void setupClickListeners() {

        // Back
        btnBack.setOnClickListener(v -> onBackPressed());

        // Favourite toggle
        btnFavourite.setOnClickListener(v -> toggleFavourite());

        // Share
        btnShare.setOnClickListener(v -> shareDevotional());

        // More options (popup menu or bottom sheet — Toast for now)
        btnMore.setOnClickListener(v ->
                Toast.makeText(this, "More options", Toast.LENGTH_SHORT).show());
    }

    // ─────────────────────────────────────────────
    //  5. FAVOURITE TOGGLE
    // ─────────────────────────────────────────────
    private void toggleFavourite() {
        isFavourited = !isFavourited;

        if (isFavourited) {
            btnFavourite.setImageResource(R.drawable.ic_favorite);
            btnFavourite.setColorFilter(
                    getResources().getColor(R.color.error_red, getTheme()));
            Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
        } else {
            btnFavourite.setImageResource(R.drawable.ic_favorite);
            btnFavourite.setColorFilter(
                    getResources().getColor(R.color.text_hint, getTheme()));
            Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show();
        }
    }

    // ─────────────────────────────────────────────
    //  6. SHARE
    // ─────────────────────────────────────────────
    private void shareDevotional() {
        String title     = tvTitle.getText().toString();
        String verse     = tvKeyVerse.getText().toString();
        String reference = tvVerseReference.getText().toString();

        String shareText = title + "\n\n" + verse + "\n\u2014 " + reference
                + "\n\nShared via Prophets Scroll";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share devotional via"));
    }

    // ─────────────────────────────────────────────
    //  7. BACK PRESS
    // ─────────────────────────────────────────────
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}