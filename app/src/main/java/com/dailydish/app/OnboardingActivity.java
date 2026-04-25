package com.dailydish.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "DailyDishPrefs";
    private static final String USERNAME_KEY = "user_name";
    private static final String ONBOARDING_COMPLETE_KEY = "onboarding_complete";
    private static final String REMINDERS_ENABLED_KEY = "reminders_enabled";
    private static final String COLOR_SCHEME_KEY = "color_scheme";

    private EditText etOnboardingName;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        etOnboardingName = findViewById(R.id.etOnboardingName);
        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(v -> {
            String userName = etOnboardingName.getText().toString().trim();

            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
                return;
            }

            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putString(USERNAME_KEY, userName)
                    .putBoolean(ONBOARDING_COMPLETE_KEY, true)
                    .putBoolean(REMINDERS_ENABLED_KEY, true)
                    .putString(COLOR_SCHEME_KEY, "purple")
                    .apply();

            Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}