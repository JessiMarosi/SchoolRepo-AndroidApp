package com.dailydish.app;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "DailyDishPrefs";
    private static final String USERNAME_KEY = "user_name";
    private static final String REMINDERS_ENABLED_KEY = "reminders_enabled";
    private static final String COLOR_SCHEME_KEY = "color_scheme";

    private ScrollView settingsRoot;

    private TextView tvSettingsTitle;
    private TextView tvNameLabel;
    private TextView tvColorSchemeLabel;

    private EditText etSettingsName;
    private Switch switchReminders;

    private RadioGroup rgColorScheme;
    private RadioButton rbPurple;
    private RadioButton rbBlue;
    private RadioButton rbGreen;
    private RadioButton rbRose;

    private Button btnSaveSettings;
    private Button btnBackHome;

    private String currentPreviewScheme = "purple";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsRoot = findViewById(R.id.settingsRoot);

        tvSettingsTitle = findViewById(R.id.tvSettingsTitle);
        tvNameLabel = findViewById(R.id.tvNameLabel);
        tvColorSchemeLabel = findViewById(R.id.tvColorSchemeLabel);

        etSettingsName = findViewById(R.id.etSettingsName);
        switchReminders = findViewById(R.id.switchReminders);

        rgColorScheme = findViewById(R.id.rgColorScheme);
        rbPurple = findViewById(R.id.rbPurple);
        rbBlue = findViewById(R.id.rbBlue);
        rbGreen = findViewById(R.id.rbGreen);
        rbRose = findViewById(R.id.rbRose);

        btnSaveSettings = findViewById(R.id.btnSaveSettings);
        btnBackHome = findViewById(R.id.btnBackHome);

        loadSettings();

        rgColorScheme.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbBlue) {
                currentPreviewScheme = "blue";
            } else if (checkedId == R.id.rbGreen) {
                currentPreviewScheme = "green";
            } else if (checkedId == R.id.rbRose) {
                currentPreviewScheme = "rose";
            } else {
                currentPreviewScheme = "purple";
            }

            applyColorPreview(currentPreviewScheme);
        });

        btnSaveSettings.setOnClickListener(v -> saveSettings());

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadSettings() {
        String userName = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(USERNAME_KEY, "");

        boolean remindersEnabled = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getBoolean(REMINDERS_ENABLED_KEY, true);

        String colorScheme = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(COLOR_SCHEME_KEY, "purple");

        currentPreviewScheme = colorScheme;

        etSettingsName.setText(userName);
        switchReminders.setChecked(remindersEnabled);

        if ("blue".equals(colorScheme)) {
            rbBlue.setChecked(true);
        } else if ("green".equals(colorScheme)) {
            rbGreen.setChecked(true);
        } else if ("rose".equals(colorScheme)) {
            rbRose.setChecked(true);
        } else {
            rbPurple.setChecked(true);
        }

        applyColorPreview(colorScheme);
    }

    private void applyColorPreview(String colorScheme) {
        int backgroundColor;
        int primaryButtonColor;
        int secondaryButtonColor;
        int textColor;
        int secondaryTextColor;
        int hintColor;

        if ("blue".equals(colorScheme)) {
            backgroundColor = Color.parseColor("#F2F7FC");
            primaryButtonColor = Color.parseColor("#3F6FA6");
            secondaryButtonColor = Color.parseColor("#2F5D8C");
            textColor = Color.parseColor("#17324D");
            secondaryTextColor = Color.parseColor("#315B7D");
            hintColor = Color.parseColor("#6F91BF");
        } else if ("green".equals(colorScheme)) {
            backgroundColor = Color.parseColor("#F3FAF5");
            primaryButtonColor = Color.parseColor("#4F8A5B");
            secondaryButtonColor = Color.parseColor("#3D6E49");
            textColor = Color.parseColor("#1F3D28");
            secondaryTextColor = Color.parseColor("#4C7455");
            hintColor = Color.parseColor("#75A87E");
        } else if ("rose".equals(colorScheme)) {
            backgroundColor = Color.parseColor("#FFF4F6");
            primaryButtonColor = Color.parseColor("#B24C63");
            secondaryButtonColor = Color.parseColor("#7D5A8C");
            textColor = Color.parseColor("#4A1F2B");
            secondaryTextColor = Color.parseColor("#7D4454");
            hintColor = Color.parseColor("#C7798A");
        } else {
            backgroundColor = Color.parseColor("#F7F4FB");
            primaryButtonColor = Color.parseColor("#6E53A3");
            secondaryButtonColor = Color.parseColor("#4F7CAC");
            textColor = Color.parseColor("#2E1A47");
            secondaryTextColor = Color.parseColor("#3F3154");
            hintColor = Color.parseColor("#8A74B9");
        }

        settingsRoot.setBackgroundColor(backgroundColor);

        tvSettingsTitle.setTextColor(textColor);
        tvNameLabel.setTextColor(textColor);
        tvColorSchemeLabel.setTextColor(textColor);

        etSettingsName.setTextColor(textColor);
        etSettingsName.setHintTextColor(hintColor);

        switchReminders.setTextColor(textColor);

        rbPurple.setTextColor(secondaryTextColor);
        rbBlue.setTextColor(secondaryTextColor);
        rbGreen.setTextColor(secondaryTextColor);
        rbRose.setTextColor(secondaryTextColor);

        btnSaveSettings.setBackgroundTintList(ColorStateList.valueOf(primaryButtonColor));
        btnBackHome.setBackgroundTintList(ColorStateList.valueOf(secondaryButtonColor));
    }

    private void saveSettings() {
        String userName = etSettingsName.getText().toString().trim();

        if (userName.isEmpty()) {
            Toast.makeText(this, "Name cannot be blank.", Toast.LENGTH_SHORT).show();
            return;
        }

        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(USERNAME_KEY, userName)
                .putBoolean(REMINDERS_ENABLED_KEY, switchReminders.isChecked())
                .putString(COLOR_SCHEME_KEY, currentPreviewScheme)
                .apply();

        Toast.makeText(this, "Settings saved.", Toast.LENGTH_SHORT).show();
    }
}