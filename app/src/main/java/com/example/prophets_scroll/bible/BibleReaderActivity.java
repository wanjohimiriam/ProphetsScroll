package com.example.prophets_scroll.bible;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.example.prophets_scroll.R;
import com.example.prophets_scroll.retrofit.ApiClient;
import com.example.prophets_scroll.models.bible.SimpleBibleChapterVersesResponse;
import com.example.prophets_scroll.models.bible.VerseDetail;
import com.example.prophets_scroll.utils.BiblePreferences;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BibleReaderActivity extends AppCompatActivity {

    private static final String TAG = "BibleReaderActivity";
    private static final int SCROLL_THRESHOLD = 10; // px, avoids jitter on tiny scrolls
    private static final int TOP_ZONE = 50; // px, always show button near top

    private ImageButton btnBack;
    private MaterialButton btnTranslation, btnSelectBook;
    private TextView tvBookName, tvChapterNumber, tvToolbarTitle;
    private LinearLayout layoutVerses;
    private NestedScrollView scrollView;

    private BiblePreferences prefs;
    private String currentBookId = "GEN";   // Book ID for API
    private String currentBookName = "Genesis"; // Display name
    private int currentChapter = 1;

    private boolean isBookButtonVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_reader);

        prefs = new BiblePreferences(this);
        
        initViews();
        setupClickListeners();
        setupScrollListener();
        loadChapter();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnTranslation = findViewById(R.id.btnTranslation);
        btnSelectBook = findViewById(R.id.btnSelectBook);
        tvBookName = findViewById(R.id.tvBookName);
        tvChapterNumber = findViewById(R.id.tvChapterNumber);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        layoutVerses = findViewById(R.id.layoutVerses);
        scrollView = findViewById(R.id.scrollView);
        
        // Set translation button text
        String translation = prefs.getSelectedTranslation().toUpperCase();
        btnTranslation.setText(translation);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());
        
        btnTranslation.setOnClickListener(v -> {
            // TODO: Open translation selector
            Toast.makeText(this, "Translation selector coming soon", Toast.LENGTH_SHORT).show();
        });
        
        btnSelectBook.setOnClickListener(v -> openBookSelector());
    }

    private void setupScrollListener() {
        scrollView.setOnScrollChangeListener(
            (NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                int delta = scrollY - oldScrollY;

                if (scrollY < TOP_ZONE) {
                    showBookButton();
                    return;
                }

                if (delta > SCROLL_THRESHOLD) {
                    hideBookButton(); // scrolling down
                } else if (delta < -SCROLL_THRESHOLD) {
                    showBookButton(); // scrolling up
                }
            });
    }

    private void hideBookButton() {
        if (!isBookButtonVisible) return;
        isBookButtonVisible = false;
        btnSelectBook.animate()
                .translationY(btnSelectBook.getHeight() + 64)
                .alpha(0f)
                .setDuration(200)
                .start();
        tvToolbarTitle.setText(currentBookName + " " + currentChapter);
        tvToolbarTitle.animate().alpha(1f).setDuration(200).start();
    }

    private void showBookButton() {
        if (isBookButtonVisible) return;
        isBookButtonVisible = true;
        btnSelectBook.animate()
                .translationY(0)
                .alpha(1f)
                .setDuration(200)
                .start();
        tvToolbarTitle.animate().alpha(0f).setDuration(150).start();
    }

    private void openBookSelector() {
        Intent intent = new Intent(this, BibleActivity.class);
        startActivity(intent);
    }

    private void loadChapter() {
        String translation = prefs.getSelectedTranslation();
        
        tvBookName.setText(currentBookName);
        tvChapterNumber.setText(String.valueOf(currentChapter));
        btnSelectBook.setText(currentBookName);
        
        ApiClient.getBibleApiService()
                .getSimpleBibleChapterVerses(translation, currentBookId, currentChapter)
                .enqueue(new Callback<SimpleBibleChapterVersesResponse>() {
                    @Override
                    public void onResponse(Call<SimpleBibleChapterVersesResponse> call, Response<SimpleBibleChapterVersesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            displayVerses(response.body().getVerses());
                        } else {
                            Log.e(TAG, "Failed: code=" + response.code());
                            Toast.makeText(BibleReaderActivity.this,
                                "Failed to load chapter (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SimpleBibleChapterVersesResponse> call, Throwable t) {
                        Log.e(TAG, "API Error", t);
                        Toast.makeText(BibleReaderActivity.this,
                            "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayVerses(List<VerseDetail> verses) {
        layoutVerses.removeAllViews();
        
        for (VerseDetail verse : verses) {
            TextView tvVerse = new TextView(this);
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 20);
            tvVerse.setLayoutParams(params);
            
            String verseNum = String.valueOf(verse.getVerse());
            String fullText = verseNum + "  " + verse.getText();
            
            SpannableString spannable = new SpannableString(fullText);
            spannable.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(this, R.color.bible_accent)),
                    0, verseNum.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new StyleSpan(Typeface.BOLD),
                    0, verseNum.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new RelativeSizeSpan(0.75f),
                    0, verseNum.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            
            tvVerse.setText(spannable);
            tvVerse.setTextColor(getColor(R.color.text_primary));
            tvVerse.setTextSize(16);
            tvVerse.setLineSpacing(10, 1.05f);
            
            layoutVerses.addView(tvVerse);
        }
    }
}
