package com.example.prophets_scroll.onboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prophets_scroll.MainActivity;
import com.example.prophets_scroll.R;
import com.example.prophets_scroll.auth.SignInActivity;
import com.google.android.material.button.MaterialButton;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Skip for now → MainActivity as guest
        TextView btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Begin Your Journey → Personalization Step 1
        MaterialButton btnGetStarted = findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(v ->
                startActivity(new Intent(this, PersonalizationStep1Activity.class)));

        // Already have account → Sign In
        TextView tvSignInLink = findViewById(R.id.tvSignInLink);
        tvSignInLink.setOnClickListener(v ->
                startActivity(new Intent(this, SignInActivity.class)));
    }
}