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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {

    // UI Components
    private ImageButton btnBack;
    private MaterialButton btnGoogleSignIn, btnSignIn;
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private TextView tvForgotPassword, tvSignUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize views
        initViews();

        // Setup click listeners
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUpLink = findViewById(R.id.tvSignUpLink);
    }

    private void setupClickListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Google Sign In
        btnGoogleSignIn.setOnClickListener(v -> {
            Toast.makeText(this, "Google Sign In clicked", Toast.LENGTH_SHORT).show();
            // TODO: Implement Google Sign In later
        });

        // Email/Password Sign In
        btnSignIn.setOnClickListener(v -> {
            if (validateInputs()) {
                // Show success and navigate to MainActivity
                Toast.makeText(this, "Sign In successful!", Toast.LENGTH_SHORT).show();
                navigateToMainActivity();
            }
        });

        // Forgot Password
        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Forgot Password screen later
        });

        // Navigate to Sign Up
        tvSignUpLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInputs() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Clear previous errors
        tilEmail.setError(null);
        tilPassword.setError(null);

        boolean isValid = true;

        // Validate email
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Enter a valid email");
            isValid = false;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 8) {
            tilPassword.setError("Password must be at least 8 characters");
            isValid = false;
        }

        return isValid;
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}