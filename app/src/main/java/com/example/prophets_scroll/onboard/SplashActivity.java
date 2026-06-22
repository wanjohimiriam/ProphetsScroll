package com.example.prophets_scroll.onboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prophets_scroll.MainActivity;
import com.example.prophets_scroll.R;
import com.example.prophets_scroll.onboard.OnboardingActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds
    private static final String PREFS_NAME = "prophets_scroll_prefs";
    private static final String KEY_ONBOARDING_DONE = "onboarding_complete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean onboardingDone = prefs.getBoolean(KEY_ONBOARDING_DONE, false);

            Intent intent;
            if (onboardingDone) {
                // Returning user → go straight to home
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // First time → go through onboarding
                intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}