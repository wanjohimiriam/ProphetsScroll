package com.example.prophets_scroll.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prophets_scroll.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class DevotionalsActivity extends AppCompatActivity {

    // ── Views ──
    private Toolbar toolbar;
    private ImageButton btnClose;
    private TextView tvResultCount;

    private MaterialCardView cardFilterSince, cardFilterLanguage, cardFilterCategory;
    private MaterialCardView cardFilterMonth, cardFilterDay;
    private TextView tvFilterSince, tvFilterLanguage, tvFilterCategory;
    private TextView tvFilterMonth, tvFilterDay;
    private LinearLayout layoutMonthDayRow;

    private RecyclerView recyclerDevotionals;
    private LinearLayout layoutEmpty;

    // ── Filter state ──
    private String selectedYear     = null;
    private String selectedMonth    = null;
    private String selectedDay      = null;
    private String selectedLanguage = null;
    private String selectedCategory = null;

//    private ImageButton btnClose;

    // ── Dropdown options ──
    private final String[] YEARS     = {"2020","2021","2022","2023","2024","2025","2026"};
    private final String[] MONTHS    = {"January","February","March","April","May","June",
            "July","August","September","October","November","December"};
    private final String[] DAYS;
    private final String[] LANGUAGES = {"English","Italian","Kiswahili","French","German"};
    private final String[] CATEGORIES_DEFAULT = {"Faith","Prophecy","Marriage","Health","Maturity","Destiny"};
    private String[] categories = CATEGORIES_DEFAULT;

    // ── Adapter ──
    private DevotionalsAdapter adapter;

    public DevotionalsActivity() {
        DAYS = new String[31];
        for (int i = 0; i < 31; i++) DAYS[i] = String.valueOf(i + 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devotionals);
        initViews();
        setupToolbar();
        setupRecyclerView();
        setupFilterListeners();
        loadDevotionals();
    }

    // ─────────────────────────────────────────────
    //  1. INIT
    // ─────────────────────────────────────────────
    private void initViews() {
        toolbar             = findViewById(R.id.toolbar);
        btnClose            = findViewById(R.id.btnClose);
        tvResultCount       = findViewById(R.id.tvResultCount);
        cardFilterSince     = findViewById(R.id.cardFilterSince);
        cardFilterLanguage  = findViewById(R.id.cardFilterLanguage);
        cardFilterCategory  = findViewById(R.id.cardFilterCategory);
        cardFilterMonth     = findViewById(R.id.cardFilterMonth);
        cardFilterDay       = findViewById(R.id.cardFilterDay);
        tvFilterSince       = findViewById(R.id.tvFilterSince);
        tvFilterLanguage    = findViewById(R.id.tvFilterLanguage);
        tvFilterCategory    = findViewById(R.id.tvFilterCategory);
        tvFilterMonth       = findViewById(R.id.tvFilterMonth);
        tvFilterDay         = findViewById(R.id.tvFilterDay);
        layoutMonthDayRow   = findViewById(R.id.layoutMonthDayRow);
        recyclerDevotionals = findViewById(R.id.recyclerDevotionals);
        layoutEmpty         = findViewById(R.id.layoutEmpty);
    }

    // ─────────────────────────────────────────────
    //  2. TOOLBAR
    // ─────────────────────────────────────────────
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        btnClose.setOnClickListener(v -> finish());
    }

    // ─────────────────────────────────────────────
    //  3. RECYCLERVIEW — GridLayoutManager 2 columns
    //     On item click → opens DevotionalDetailActivity
    //     same flow as MainActivity btnReadNow
    // ─────────────────────────────────────────────
    private void setupRecyclerView() {
        adapter = new DevotionalsAdapter(new ArrayList<>(), item -> {
            // ── Same intent as MainActivity.openDevotionalDetail() ──
            Intent intent = new Intent(this, DevotionalDetailActivity.class);
            intent.putExtra(DevotionalDetailActivity.EXTRA_TITLE,     item.title);
            intent.putExtra(DevotionalDetailActivity.EXTRA_DATE,      item.date);
            intent.putExtra(DevotionalDetailActivity.EXTRA_CATEGORY,  item.category);
            intent.putExtra(DevotionalDetailActivity.EXTRA_VERSE,     item.verse);
            intent.putExtra(DevotionalDetailActivity.EXTRA_REFERENCE, item.reference);
            intent.putExtra(DevotionalDetailActivity.EXTRA_BODY,      item.body);
            startActivity(intent);
        });

        recyclerDevotionals.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerDevotionals.setHasFixedSize(true);
        recyclerDevotionals.setAdapter(adapter);
    }

    // ─────────────────────────────────────────────
    //  4. FILTER LISTENERS
    // ─────────────────────────────────────────────
    private void setupFilterListeners() {

        // Since (year) — reveals Month/Day row when selected
        cardFilterSince.setOnClickListener(v ->
                showDropdown(v, YEARS, selected -> {
                    selectedYear  = selected;
                    selectedMonth = null;
                    selectedDay   = null;
                    tvFilterSince.setText(selectedYear);
                    tvFilterSince.setTextColor(getColor(R.color.text_primary));
                    tvFilterMonth.setText("Month");
                    tvFilterMonth.setTextColor(getColor(R.color.text_secondary));
                    tvFilterDay.setText("Day");
                    tvFilterDay.setTextColor(getColor(R.color.text_secondary));
                    layoutMonthDayRow.setVisibility(View.VISIBLE);
                    applyFilters();
                }));

        // Language
        cardFilterLanguage.setOnClickListener(v ->
                showDropdown(v, LANGUAGES, selected -> {
                    selectedLanguage = selected;
                    tvFilterLanguage.setText(selectedLanguage);
                    tvFilterLanguage.setTextColor(getColor(R.color.text_primary));
                    applyFilters();
                }));

        // Category
        cardFilterCategory.setOnClickListener(v ->
                showDropdown(v, categories, selected -> {
                    selectedCategory = selected;
                    tvFilterCategory.setText(selectedCategory);
                    tvFilterCategory.setTextColor(getColor(R.color.text_primary));
                    applyFilters();
                }));

        // Month (only visible after year picked)
        cardFilterMonth.setOnClickListener(v ->
                showDropdown(v, MONTHS, selected -> {
                    selectedMonth = selected;
                    tvFilterMonth.setText(selected.substring(0, 3));
                    tvFilterMonth.setTextColor(getColor(R.color.text_primary));
                    applyFilters();
                }));

        // Day
        cardFilterDay.setOnClickListener(v ->
                showDropdown(v, DAYS, selected -> {
                    selectedDay = selected;
                    tvFilterDay.setText(selectedDay);
                    tvFilterDay.setTextColor(getColor(R.color.text_primary));
                    applyFilters();
                }));
    }

    // ─────────────────────────────────────────────
    //  5. POPUP DROPDOWN
    // ─────────────────────────────────────────────
    private void showDropdown(View anchor, String[] options, OnOptionSelected callback) {
        PopupMenu popup = new PopupMenu(this, anchor);
        for (int i = 0; i < options.length; i++) {
            popup.getMenu().add(0, i, i, options[i]);
        }
        popup.setOnMenuItemClickListener(item -> {
            callback.onSelected(options[item.getItemId()]);
            return true;
        });
        popup.show();
    }

    // ─────────────────────────────────────────────
    //  6. APPLY FILTERS
    //     TODO: replace loadDevotionals() with your
    //     real API call passing the selected filters
    // ─────────────────────────────────────────────
    private void applyFilters() {
        loadDevotionals();
    }

    // ─────────────────────────────────────────────
    //  7. LOAD DATA  (dummy — swap with API)
    // ─────────────────────────────────────────────
    private void loadDevotionals() {
        List<DevotionalItem> items = new ArrayList<>();

        String[] titles = {
                "Walking in Faith",
                "The Power of Prayer",
                "Living in Abundance",
                "Grace and Mercy",
                "Strength in Weakness",
                "His Perfect Plan",
                "Love Endures All",
                "A New Beginning"
        };
        String[] dates  = {
                "Jun 23","Jun 22","Jun 21","Jun 20",
                "Jun 19","Jun 18","Jun 17","Jun 16"
        };
        String[] cats   = {
                "Faith","Prayer","Health","Mercy",
                "Maturity","Destiny","Marriage","Faith"
        };

        for (int i = 0; i < titles.length; i++) {
            DevotionalItem d  = new DevotionalItem();
            d.title     = titles[i];
            d.date      = dates[i] + ", 2026";
            d.category  = cats[i];
            d.verse     = "\u201cFor we walk by faith, not by sight.\u201d";
            d.reference = "2 Corinthians 5:7";
            d.body      = "In our daily walk with God, we often find ourselves at crossroads "
                    + "where the path ahead seems unclear. " + titles[i] + " reminds us to trust.";
            items.add(d);
        }

        adapter.updateData(items);
        tvResultCount.setText(items.size() + "+");
        recyclerDevotionals.setVisibility(items.isEmpty() ? View.GONE   : View.VISIBLE);
        layoutEmpty.setVisibility(items.isEmpty()         ? View.VISIBLE : View.GONE);
    }

    // ─────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────
    public void updateCategories(String[] beCategories) {
        this.categories = beCategories;
    }

    interface OnOptionSelected {
        void onSelected(String value);
    }

    public static class DevotionalItem {
        public String title, date, category, verse, reference, body;
    }
}