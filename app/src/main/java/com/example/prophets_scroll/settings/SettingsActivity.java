package com.example.prophets_scroll.settings;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.prophets_scroll.R;
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

    // Order matters — index doubles as the "selected item" index for the dialog
    private static final String[] FONT_SIZE_KEYS = {"small", "medium", "large"};
    private static final String[] FONT_SIZE_LABELS = {"Small", "Medium", "Large"};

    private ImageButton btnBack;
    private SwitchMaterial switchDailyReminders, switchDarkMode;
    private LinearLayout itemReminderTime;
    private LinearLayout itemFontSize;
    private TextView tvReminderTime;
    private TextView tvFontSize;

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

        // These match the ids that actually exist in activity_settings.xml
        itemReminderTime = findViewById(R.id.itemReminderTime);
        tvReminderTime = findViewById(R.id.tvReminderTime);

        itemFontSize = findViewById(R.id.itemFontSize);
        tvFontSize = findViewById(R.id.tvFontSize);
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
        updateFontSizeLabel();

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
        itemReminderTime.setOnClickListener(v -> showTimePickerDialog());

        // Font size picker
        itemFontSize.setOnClickListener(v -> showFontSizeDialog());

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

        String timeStr = String.format(Locale.getDefault(), "%d:%02d %s",
                displayHour, reminderMinute, amPm);
        tvReminderTime.setText(timeStr);
    }

    /**
     * Shows a single-choice picker dialog — Small / Medium / Large — with the
     * current size pre-selected, matching the "phone-style" settings pattern
     * (pick one of a fixed set of sizes) rather than an inline button row.
     */
    private void showFontSizeDialog() {
        int checkedIndex = indexOfFontSize(currentFontSize);

        new AlertDialog.Builder(this)
                .setTitle(R.string.font_size_dialog_title)
                .setSingleChoiceItems(FONT_SIZE_LABELS, checkedIndex, (DialogInterface dialog, int which) -> {
                    setFontSize(FONT_SIZE_KEYS[which]);
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private int indexOfFontSize(String key) {
        for (int i = 0; i < FONT_SIZE_KEYS.length; i++) {
            if (FONT_SIZE_KEYS[i].equals(key)) return i;
        }
        return 1; // default to Medium
    }

    private void setFontSize(String size) {
        currentFontSize = size;
        prefs.edit().putString(KEY_FONT_SIZE, size).apply();
        updateFontSizeLabel();
        // TODO: Apply font size to reading views (e.g. via a scale factor
        // read from these same prefs wherever devotional text is rendered)
    }

    private void updateFontSizeLabel() {
        int index = indexOfFontSize(currentFontSize);
        tvFontSize.setText(FONT_SIZE_LABELS[index]);
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