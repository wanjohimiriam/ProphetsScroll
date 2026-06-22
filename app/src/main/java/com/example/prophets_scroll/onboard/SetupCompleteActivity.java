package com.example.prophets_scroll.onboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prophets_scroll.MainActivity;
import com.example.prophets_scroll.R;
import com.example.prophets_scroll.auth.SignInActivity;
import com.example.prophets_scroll.auth.SignUpActivity;
import com.google.android.material.button.MaterialButton;

public class SetupCompleteActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "prophets_scroll_prefs";
    private static final String KEY_ONBOARDING_DONE = "onboarding_complete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard_complete);

        ImageButton btnBack = findViewById(R.id.btnBack);
        MaterialButton btnCreateAccount = findViewById(R.id.btnCreateAccount);
        MaterialButton btnStartExploring = findViewById(R.id.btnStartExploring);
        TextView tvSignInLink = findViewById(R.id.tvSignInLink);

        // Mark onboarding complete regardless of path taken
        markOnboardingComplete();

        btnBack.setOnClickListener(v -> finish());

        // Primary CTA — go create an account
        btnCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        // Guest path — go explore without account
        btnStartExploring.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Already have account — go sign in
        tvSignInLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        });
    }

    private void markOnboardingComplete() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_ONBOARDING_DONE, true).apply();
    }
}