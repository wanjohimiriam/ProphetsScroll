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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    private com.google.android.material.button.MaterialButton btnSave, btnComments;

    // ── State ──
    private boolean isFavourited = false;
    private boolean isSaved = false;

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

        btnSave            = findViewById(R.id.btnSave);
        btnComments        = findViewById(R.id.btnComments);
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

        // Fallbacks with realistic devotional content
        if (title     == null) title     = "Pressing Forward Into New Dimensions";
        if (date      == null) date      = "Wednesday 1st July 2026";
        if (category  == null) category  = "Faith";
        if (verse     == null) verse     = "\"Not that I have already attained, or am already perfected; but I press on, that I may lay hold of that for which Christ Jesus has also laid hold of me.\" \"Brethren, I do not count myself to have apprehended; but one thing I do, forgetting those things which are behind and reaching forward to those things which are ahead, I press toward the goal for the prize of the upward call of God in Christ Jesus.\"";
        if (reference == null) reference = "Philippians 3:12-14 NKJV";
        if (body      == null) body      = loadDevotionalFromRaw();

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

        // Save button
        btnSave.setOnClickListener(v -> toggleSave());

        // Comments button
        btnComments.setOnClickListener(v -> openComments());
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
    //  7. SAVE TOGGLE
    // ─────────────────────────────────────────────
    private void toggleSave() {
        isSaved = !isSaved;
        if (isSaved) {
            btnSave.setText("Saved");
            btnSave.setIconResource(R.drawable.ic_favorite);
            btnSave.setIconTint(android.content.res.ColorStateList.valueOf(getColor(R.color.error_red)));
        } else {
            btnSave.setText("Save");
            btnSave.setIconResource(R.drawable.ic_favorite);
            btnSave.setIconTint(android.content.res.ColorStateList.valueOf(getColor(R.color.text_hint)));
        }
    }

    // ─────────────────────────────────────────────
    //  8. OPEN COMMENTS
    // ─────────────────────────────────────────────
    private void openComments() {
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra(CommentsActivity.EXTRA_DEVOTIONAL_TITLE,
                tvTitle.getText().toString());
        intent.putExtra(CommentsActivity.EXTRA_DEVOTIONAL_DATE,
                tvDate.getText().toString());
        intent.putExtra(CommentsActivity.EXTRA_DEVOTIONAL_CATEGORY,
                tvCategory.getText().toString());
        startActivity(intent);
    }

    // ─────────────────────────────────────────────
    //  9. BACK PRESS
    // ─────────────────────────────────────────────
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ─────────────────────────────────────────────
    //  10. LOAD DEVOTIONAL FROM RAW TEXT FILE
    // ─────────────────────────────────────────────
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