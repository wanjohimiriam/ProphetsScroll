package com.example.prophets_scroll.onboard;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prophets_scroll.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class PersonalizationStep2Activity extends AppCompatActivity {

    private ImageButton btnBack;
    private MaterialButton btnSkip;
    private MaterialButton btnContinue;
    private SwitchMaterial switchReminders;
    private LinearLayout layoutTimePicker;
    private TextView tvSelectedTime;

    private int selectedHour = 8;
    private int selectedMinute = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard_two);

        // Initialize views
        initViews();

        // Setup click listeners
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSkip = findViewById(R.id.btnSkip);
        btnContinue = findViewById(R.id.btnContinue);
        switchReminders = findViewById(R.id.switchReminders);
        layoutTimePicker = findViewById(R.id.layoutTimePicker);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSkip.setOnClickListener(v -> {
            // Skip to completion
            startActivity(new Intent(this, SetupCompleteActivity.class));
            finish();
        });

        btnContinue.setOnClickListener(v -> {
            // Save reminder settings (TODO: Implement actual saving)
            boolean remindersEnabled = switchReminders.isChecked();

            // Navigate to Setup Complete
            startActivity(new Intent(this, SetupCompleteActivity.class));
            finish();
        });

        // Time picker click listener
        layoutTimePicker.setOnClickListener(v -> showTimePicker());
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    updateTimeDisplay();
                },
                selectedHour,
                selectedMinute,
                false // 12-hour format
        );

        timePickerDialog.show();
    }

    private void updateTimeDisplay() {
        // Convert to 12-hour format with AM/PM
        String amPm = selectedHour >= 12 ? "PM" : "AM";
        int displayHour = selectedHour % 12;
        if (displayHour == 0) displayHour = 12;

        String timeString = String.format("%d:%02d %s", displayHour, selectedMinute, amPm);
        tvSelectedTime.setText(timeString);
    }
}