package com.example.prophets_scroll.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prophets_scroll.MainActivity;
import com.example.prophets_scroll.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView btnSkip, tvSignInLink, tvPasswordRequirement;
    private MaterialButton btnGoogleSignUp, btnCreateAccount;
    private TextInputLayout tilFullName, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etFullName, etEmail, etPassword, etConfirmPassword;
    private MaterialCheckBox cbTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();
        setupClickListeners();
        setupPasswordValidation();

        // If coming from "Continue with Google" on onboarding — auto-trigger Google flow
        if (getIntent().getBooleanExtra("google_flow", false)) {
            triggerGoogleSignUp();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSkip = findViewById(R.id.btnSkip);
        btnGoogleSignUp = findViewById(R.id.btnGoogleSignUp);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tilFullName = findViewById(R.id.tilFullName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cbTerms = findViewById(R.id.cbTerms);
        tvSignInLink = findViewById(R.id.tvSignInLink);
        tvPasswordRequirement = findViewById(R.id.tvPasswordRequirement);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Skip → go to MainActivity as guest
        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnGoogleSignUp.setOnClickListener(v -> triggerGoogleSignUp());

        btnCreateAccount.setOnClickListener(v -> {
            if (validateInputs()) {
                Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
                // TODO: Call auth API — for now go home
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        tvSignInLink.setOnClickListener(v ->
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class)));
    }

    private void triggerGoogleSignUp() {
        // TODO: Implement Google Sign-In SDK
        Toast.makeText(this, "Google Sign Up — coming soon", Toast.LENGTH_SHORT).show();
    }

    private void setupPasswordValidation() {
        etPassword.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(android.text.Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePasswordStrength(s.toString());
            }
        });
    }

    private void updatePasswordStrength(String password) {
        if (TextUtils.isEmpty(password)) {
            tvPasswordRequirement.setText("Must be at least 8 characters");
            tvPasswordRequirement.setTextColor(getResources().getColor(android.R.color.darker_gray, getTheme()));
        } else if (password.length() < 8) {
            tvPasswordRequirement.setText("Too short (" + password.length() + "/8)");
            tvPasswordRequirement.setTextColor(getResources().getColor(android.R.color.holo_red_dark, getTheme()));
        } else {
            tvPasswordRequirement.setText("✓ Good");
            tvPasswordRequirement.setTextColor(getResources().getColor(android.R.color.holo_green_dark, getTheme()));
        }
    }

    private boolean validateInputs() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        tilFullName.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);

        boolean isValid = true;

        if (TextUtils.isEmpty(fullName) || fullName.length() < 2) {
            tilFullName.setError("Enter a valid name");
            isValid = false;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Enter a valid email");
            isValid = false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 8) {
            tilPassword.setError("Password must be at least 8 characters");
            isValid = false;
        }
        if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Passwords do not match");
            isValid = false;
        }
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept the Terms of Service", Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }
}