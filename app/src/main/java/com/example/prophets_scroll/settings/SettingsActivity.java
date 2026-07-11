package com.example.prophets_scroll.settings;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.prophets_scroll.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_DAILY_REMINDERS = "daily_reminders";
    private static final String KEY_REMINDER_HOUR = "reminder_hour";
    private static final String KEY_REMINDER_MINUTE = "reminder_minute";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_DARK_MODE = "dark_mode";

    private ImageButton btnBack;
    private SwitchMaterial switchDailyReminders, switchDarkMode;
    private MaterialCardView cardReminderTime;
    private TextView tvReminderTime;
    private MaterialButton btnFontSmall, btnFontMedium, btnFontLarge;

    private SharedPreferences prefs;
    private int reminderHour = 8;
    private int reminderMinute = 0;
    private String currentFontSize = "medium";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        initViews();
        loadSettings();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        switchDailyReminders = findViewById(R.id.switchDailyReminders);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        cardReminderTime = findViewById(R.id.cardReminderTime);
        tvReminderTime = findViewById(R.id.tvReminderTime);
        btnFontSmall = findViewById(R.id.btnFontSmall);
        btnFontMedium = findViewById(R.id.btnFontMedium);
        btnFontLarge = findViewById(R.id.btnFontLarge);
    }

    private void loadSettings() {
        // Load daily reminders
        boolean dailyReminders = prefs.getBoolean(KEY_DAILY_REMINDERS, true);
        switchDailyReminders.setChecked(dailyReminders);

        // Load reminder time
        reminderHour = prefs.getInt(KEY_REMINDER_HOUR, 8);
        reminderMinute = prefs.getInt(KEY_REMINDER_MINUTE, 0);
        updateReminderTimeDisplay();

        // Load font size
        currentFontSize = prefs.getString(KEY_FONT_SIZE, "medium");
        updateFontSizeButtons();

        // Load dark mode
        boolean darkMode = prefs.getBoolean(KEY_DARK_MODE, false);
        switchDarkMode.setChecked(darkMode);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        // Daily reminders switch
        switchDailyReminders.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_DAILY_REMINDERS, isChecked).apply();
            // TODO: Schedule or cancel daily reminder notification
        });

        // Reminder time picker
        cardReminderTime.setOnClickListener(v -> showTimePickerDialog());

        // Font size buttons
        btnFontSmall.setOnClickListener(v -> setFontSize("small"));
        btnFontMedium.setOnClickListener(v -> setFontSize("medium"));
        btnFontLarge.setOnClickListener(v -> setFontSize("large"));

        // Dark mode switch
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_DARK_MODE, isChecked).apply();
            applyDarkMode(isChecked);
        });
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    reminderHour = hourOfDay;
                    reminderMinute = minute;
                    prefs.edit()
                            .putInt(KEY_REMINDER_HOUR, reminderHour)
                            .putInt(KEY_REMINDER_MINUTE, reminderMinute)
                            .apply();
                    updateReminderTimeDisplay();
                    // TODO: Reschedule daily reminder notification
                },
                reminderHour,
                reminderMinute,
                false // 12-hour format
        );
        timePickerDialog.show();
    }

    private void updateReminderTimeDisplay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, reminderHour);
        calendar.set(Calendar.MINUTE, reminderMinute);

        int displayHour = reminderHour;
        String amPm = "AM";

        if (reminderHour >= 12) {
            amPm = "PM";
            if (reminderHour > 12) {
                displayHour = reminderHour - 12;
            }
        }
        if (displayHour == 0) {
            displayHour = 12;
        }

        String timeStr = String.format(Locale.getDefault(), "%02d:%02d %s", 
                displayHour, reminderMinute, amPm);
        tvReminderTime.setText(timeStr);
    }

    private void setFontSize(String size) {
        currentFontSize = size;
        prefs.edit().putString(KEY_FONT_SIZE, size).apply();
        updateFontSizeButtons();
        // TODO: Apply font size to reading views
    }

    private void updateFontSizeButtons() {
        // Reset all buttons to default style
        resetFontButton(btnFontSmall);
        resetFontButton(btnFontMedium);
        resetFontButton(btnFontLarge);

        // Highlight selected button
        MaterialButton selectedButton;
        switch (currentFontSize) {
            case "small":
                selectedButton = btnFontSmall;
                break;
            case "large":
                selectedButton = btnFontLarge;
                break;
            default:
                selectedButton = btnFontMedium;
                break;
        }

        selectedButton.setBackgroundTintList(getColorStateList(R.color.gold_primary));
        selectedButton.setTextColor(getColor(android.R.color.white));
        selectedButton.setStrokeColor(getColorStateList(R.color.gold_primary));
        selectedButton.setStrokeWidth(4);
    }

    private void resetFontButton(MaterialButton button) {
        button.setBackgroundTintList(getColorStateList(R.color.feature_card_bg));
        button.setTextColor(getColor(R.color.text_primary));
        button.setStrokeColor(getColorStateList(R.color.card_border));
        button.setStrokeWidth(2);
    }

    private void applyDarkMode(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        // Activity will be recreated automatically
    }
}
