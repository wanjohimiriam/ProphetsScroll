package com.example.prophets_scroll.profile; // TODO: adjust to your actual package

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prophets_scroll.R; // TODO: adjust to your actual R import

/**
 * Profile screen: shows the user's avatar/name/email header, reading stats,
 * editable account info (name / email / password), achievement badges,
 * and account actions (sign out / delete account).
 */
public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_AVATAR = 101;

    // Header
    private TextView tvAvatarInitials;
    private ImageButton btnChangeAvatar;
    private TextView tvName;
    private TextView tvEmailHeader;
    private TextView tvMemberSince;

    // Stats
    private TextView tvStreakValue;
    private TextView tvReadValue;
    private TextView tvFavoritesValue;

    // Account info rows
    private TextView tvFullNameValue;
    private TextView tvEmailValue;
    private TextView tvPasswordValue;
    private ImageButton btnEditName;
    private ImageButton btnEditEmail;
    private ImageButton btnEditPassword;

    // Achievements
    private android.widget.LinearLayout cardStreak;
    private android.widget.LinearLayout cardBookworm;
    private android.widget.LinearLayout cardYearJourney;

    // Actions
    private android.widget.Button btnSignOut;
    private android.widget.Button btnDeleteAccount;
    private ImageButton btnBack;

    // In-memory model of the currently-stored (unmasked) password, so the
    // edit dialog can prefill it. Replace with a real secure source.
    private String currentPasswordPlaceholder = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bindViews();
        loadProfileData();
        setClickListeners();
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btnBack);

        tvAvatarInitials = findViewById(R.id.tvAvatarInitials);
        btnChangeAvatar = findViewById(R.id.btnChangeAvatar);
        tvName = findViewById(R.id.tvName);
        tvEmailHeader = findViewById(R.id.tvEmailHeader);
        tvMemberSince = findViewById(R.id.tvMemberSince);

        tvStreakValue = findViewById(R.id.tvStreakValue);
        tvReadValue = findViewById(R.id.tvReadValue);
        tvFavoritesValue = findViewById(R.id.tvFavoritesValue);

        tvFullNameValue = findViewById(R.id.tvFullNameValue);
        tvEmailValue = findViewById(R.id.tvEmailValue);
        tvPasswordValue = findViewById(R.id.tvPasswordValue);
        btnEditName = findViewById(R.id.btnEditName);
        btnEditEmail = findViewById(R.id.btnEditEmail);
        btnEditPassword = findViewById(R.id.btnEditPassword);

        cardStreak = findViewById(R.id.cardStreak);
        cardBookworm = findViewById(R.id.cardBookworm);
        cardYearJourney = findViewById(R.id.cardYearJourney);

        btnSignOut = findViewById(R.id.btnSignOut);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
    }

    /**
     * Populate the screen from your actual user/session source
     * (e.g. Firebase Auth, Room DB, ViewModel, etc). Values below are
     * placeholders matching the mock.
     */
    private void loadProfileData() {
        String fullName = "Miriam Wanjohi";
        String email = "miriam@example.com";
        String memberSince = "Member since May 2026";

        int streakDays = 25;
        int readCount = 142;
        int favoritesCount = 18;

        tvName.setText(fullName);
        tvEmailHeader.setText(email);
        tvMemberSince.setText(memberSince);
        tvAvatarInitials.setText(getInitials(fullName));

        tvStreakValue.setText(String.valueOf(streakDays));
        tvReadValue.setText(String.valueOf(readCount));
        tvFavoritesValue.setText(String.valueOf(favoritesCount));

        tvFullNameValue.setText(fullName);
        tvEmailValue.setText(email);
        tvPasswordValue.setText("••••••••");

        // Example: unlock/lock achievement cards based on real progress
        boolean yearJourneyUnlocked = readCount >= 365;
        setAchievementLocked(cardYearJourney, !yearJourneyUnlocked);
    }

    private void setClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnChangeAvatar.setOnClickListener(v -> openAvatarPicker());

        btnEditName.setOnClickListener(v -> showEditDialog(
                getString(R.string.edit_full_name),
                tvFullNameValue.getText().toString(),
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS,
                newValue -> {
                    tvFullNameValue.setText(newValue);
                    tvName.setText(newValue);
                    tvAvatarInitials.setText(getInitials(newValue));
                    // TODO: persist to backend
                }));

        btnEditEmail.setOnClickListener(v -> showEditDialog(
                getString(R.string.edit_email),
                tvEmailValue.getText().toString(),
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
                newValue -> {
                    tvEmailValue.setText(newValue);
                    tvEmailHeader.setText(newValue);
                    // TODO: persist to backend, may require re-verification
                }));

        btnEditPassword.setOnClickListener(v -> showEditDialog(
                getString(R.string.edit_password),
                currentPasswordPlaceholder,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD,
                newValue -> {
                    currentPasswordPlaceholder = newValue;
                    tvPasswordValue.setText("••••••••");
                    // TODO: persist to backend (re-auth may be required)
                }));

        cardStreak.setOnClickListener(v ->
                Toast.makeText(this, R.string.achievement_streak_title, Toast.LENGTH_SHORT).show());
        cardBookworm.setOnClickListener(v ->
                Toast.makeText(this, R.string.achievement_bookworm_title, Toast.LENGTH_SHORT).show());
        cardYearJourney.setOnClickListener(v ->
                Toast.makeText(this, R.string.achievement_year_title, Toast.LENGTH_SHORT).show());

        btnSignOut.setOnClickListener(v -> confirmSignOut());
        btnDeleteAccount.setOnClickListener(v -> confirmDeleteAccount());
    }

    // ─────────────────────────── Helpers ───────────────────────────

    private String getInitials(String fullName) {
        if (TextUtils.isEmpty(fullName)) return "?";
        String[] parts = fullName.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < Math.min(2, parts.length); i++) {
            if (!parts[i].isEmpty()) {
                initials.append(Character.toUpperCase(parts[i].charAt(0)));
            }
        }
        return initials.length() > 0 ? initials.toString() : "?";
    }

    private void setAchievementLocked(android.widget.LinearLayout card, boolean locked) {
        // Swap the icon well background/tint and dim the card if locked.
        // Assumes the icon well is the first child (index 0) of the card.
        ImageView iconWell = (ImageView) card.getChildAt(0);
        if (locked) {
            iconWell.setBackgroundResource(R.drawable.bg_achievement_locked);
            iconWell.setImageResource(R.drawable.ic_baseline_lock_24);
        }
        card.setAlpha(locked ? 0.85f : 1f);
    }

    private interface OnValueSaved {
        void onSaved(String newValue);
    }

    private void showEditDialog(String title, String currentValue, int inputType, OnValueSaved callback) {
        EditText input = new EditText(this);
        input.setInputType(inputType);
        input.setText(currentValue);
        input.setSelection(input.getText().length());

        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        input.setPadding(padding, padding, padding, padding);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(input)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String newValue = input.getText().toString().trim();
                    if (TextUtils.isEmpty(newValue)) {
                        Toast.makeText(this, "Value can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    callback.onSaved(newValue);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void confirmSignOut() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_sign_out_title)
                .setMessage(R.string.dialog_sign_out_message)
                .setPositiveButton(R.string.sign_out, (dialog, which) -> performSignOut())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void confirmDeleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
                .setPositiveButton(R.string.delete_account, (dialog, which) -> performDeleteAccount())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void performSignOut() {
        // TODO: clear session / Firebase Auth signOut() / navigate to Login
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        // Example:
        // FirebaseAuth.getInstance().signOut();
        // startActivity(new Intent(this, LoginActivity.class));
        // finishAffinity();
    }

    private void performDeleteAccount() {
        // TODO: call backend delete-account endpoint, then clear session and navigate away
        Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show();
    }

    private void openAvatarPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_AVATAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_AVATAR && resultCode == Activity.RESULT_OK && data != null) {
            // TODO: load the picked image (e.g. with Glide/Coil into an ImageView
            // that replaces tvAvatarInitials), then upload to storage.
            // Uri imageUri = data.getData();
        }
    }
}