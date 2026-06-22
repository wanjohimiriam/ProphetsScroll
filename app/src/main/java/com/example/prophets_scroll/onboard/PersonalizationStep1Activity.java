package com.example.prophets_scroll.onboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prophets_scroll.R;
import com.example.prophets_scroll.adapter.CategoryAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class PersonalizationStep1Activity extends AppCompatActivity {

     private RecyclerView rvCategories;
    private MaterialButton btnContinue;
    private MaterialButton btnSkip;
    private ImageButton btnBack;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories;
    private int selectedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard_one);

        // Initialize views
        initViews();

        // Setup categories
        setupCategories();

        // Setup click listeners
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSkip = findViewById(R.id.btnSkip);
        btnContinue = findViewById(R.id.btnContinue);
        rvCategories = findViewById(R.id.rvCategories);

        // Setup RecyclerView
        rvCategories.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void setupCategories() {
        categories = new ArrayList<>();
        categories.add(new Category("1", "Faith", false));
        categories.add(new Category("2", "Hope", false));
        categories.add(new Category("3", "Love", false));
        categories.add(new Category("4", "Prayer", false));
        categories.add(new Category("5", "Wisdom", false));
        categories.add(new Category("6", "Family", false));
        categories.add(new Category("7", "Youth", false));
        categories.add(new Category("8", "Marriage", false));
        categories.add(new Category("9", "Finance", false));
        categories.add(new Category("10", "Worship", false));
        categories.add(new Category("11", "Healing", false));
        categories.add(new Category("12", "Leadership", false));

        categoryAdapter = new CategoryAdapter(categories, this::onCategoryClicked);
        rvCategories.setAdapter(categoryAdapter);
    }

    private void onCategoryClicked(Category category) {
        // Toggle selection
        category.setSelected(!category.isSelected());

        // Count selected categories
        selectedCount = 0;
        for (Category cat : categories) {
            if (cat.isSelected()) {
                selectedCount++;
            }
        }

        // Enable/disable continue button (minimum 3 selections)
        btnContinue.setEnabled(selectedCount >= 3);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSkip.setOnClickListener(v -> {
            // Skip to Step 2
            startActivity(new Intent(this, PersonalizationStep2Activity.class));
            finish();
        });

        btnContinue.setOnClickListener(v -> {
            if (selectedCount >= 3) {
                // Save selected categories (TODO: Implement actual saving)
                Toast.makeText(this, selectedCount + " categories selected", Toast.LENGTH_SHORT).show();

                // Navigate to Step 2
                startActivity(new Intent(this, PersonalizationStep2Activity.class));
                finish();
            } else {
                Toast.makeText(this, "Please select at least 3 categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Simple Category model
    public static class Category {
        private String id;
        private String name;
        private boolean selected;

        public Category(String id, String name, boolean selected) {
            this.id = id;
            this.name = name;
            this.selected = selected;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public boolean isSelected() { return selected; }
        public void setSelected(boolean selected) { this.selected = selected; }
    }
}