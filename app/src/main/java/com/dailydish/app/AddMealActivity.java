package com.dailydish.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddMealActivity extends AppCompatActivity {

    private EditText etMealName;
    private EditText etCategory;
    private EditText etCalories;
    private EditText etProtein;
    private EditText etCarbs;
    private EditText etSugar;
    private EditText etFat;
    private EditText etWaterOz;
    private EditText etSodaOz;
    private Button btnSaveMeal;
    private Button btnCancel;

    private boolean editMode = false;
    private int mealIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        etMealName = findViewById(R.id.etMealName);
        etCategory = findViewById(R.id.etCategory);
        etCalories = findViewById(R.id.etCalories);
        etProtein = findViewById(R.id.etProtein);
        etCarbs = findViewById(R.id.etCarbs);
        etSugar = findViewById(R.id.etSugar);
        etFat = findViewById(R.id.etFat);
        etWaterOz = findViewById(R.id.etWaterOz);
        etSodaOz = findViewById(R.id.etSodaOz);
        btnSaveMeal = findViewById(R.id.btnSaveMeal);
        btnCancel = findViewById(R.id.btnCancel);

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editMode", false);
        mealIndex = intent.getIntExtra("mealIndex", -1);

        if (editMode) {
            Meal mealData = (Meal) intent.getSerializableExtra("mealData");
            if (mealData != null) {
                etMealName.setText(mealData.getName());
                etCategory.setText(mealData.getCategory());
                etCalories.setText(String.valueOf(mealData.getCalories()));
                etProtein.setText(String.valueOf(mealData.getProtein()));
                etCarbs.setText(String.valueOf(mealData.getCarbs()));
                etSugar.setText(String.valueOf(mealData.getSugar()));
                etFat.setText(String.valueOf(mealData.getFat()));
                etWaterOz.setText(String.valueOf(mealData.getWaterOz()));
                etSodaOz.setText(String.valueOf(mealData.getSodaOz()));
                btnSaveMeal.setText("Update Meal");
            }
        }

        btnSaveMeal.setOnClickListener(v -> {
            String name = etMealName.getText().toString().trim();
            String category = etCategory.getText().toString().trim();
            String calories = etCalories.getText().toString().trim();
            String protein = etProtein.getText().toString().trim();
            String carbs = etCarbs.getText().toString().trim();
            String sugar = etSugar.getText().toString().trim();
            String fat = etFat.getText().toString().trim();
            String waterOz = etWaterOz.getText().toString().trim();
            String sodaOz = etSodaOz.getText().toString().trim();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("category", category);
            resultIntent.putExtra("calories", calories);
            resultIntent.putExtra("protein", protein);
            resultIntent.putExtra("carbs", carbs);
            resultIntent.putExtra("sugar", sugar);
            resultIntent.putExtra("fat", fat);
            resultIntent.putExtra("waterOz", waterOz);
            resultIntent.putExtra("sodaOz", sodaOz);

            if (editMode) {
                resultIntent.putExtra("mealIndex", mealIndex);
            }

            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}