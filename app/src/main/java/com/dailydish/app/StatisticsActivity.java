package com.dailydish.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "DailyDishPrefs";
    private static final String MEALS_KEY = "meals_json";

    private TextView tvStatsDate;
    private TextView tvWeeklyTotals;
    private TextView tvMonthlyTotals;
    private TextView tvAnnualTotals;
    private Button btnHome;

    private final ArrayList<Meal> mealsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        tvStatsDate = findViewById(R.id.tvStatsDate);
        tvWeeklyTotals = findViewById(R.id.tvWeeklyTotals);
        tvMonthlyTotals = findViewById(R.id.tvMonthlyTotals);
        tvAnnualTotals = findViewById(R.id.tvAnnualTotals);
        btnHome = findViewById(R.id.btnHome);

        tvStatsDate.setText(getTodayDisplayDate());

        btnHome.setOnClickListener(v -> finish());

        loadMeals();
        populateStatistics();
    }

    private String getTodayDisplayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US);
        return sdf.format(Calendar.getInstance().getTime());
    }

    private void populateStatistics() {
        Calendar today = Calendar.getInstance();

        int weeklyCalories = 0, weeklyProtein = 0, weeklyCarbs = 0, weeklySugar = 0, weeklyFat = 0, weeklyWater = 0, weeklySoda = 0;
        int monthlyCalories = 0, monthlyProtein = 0, monthlyCarbs = 0, monthlySugar = 0, monthlyFat = 0, monthlyWater = 0, monthlySoda = 0;
        int annualCalories = 0, annualProtein = 0, annualCarbs = 0, annualSugar = 0, annualFat = 0, annualWater = 0, annualSoda = 0;

        for (Meal meal : mealsList) {
            Calendar mealDate = parseMealDate(meal.getDate());
            if (mealDate == null) {
                continue;
            }

            boolean sameYear = today.get(Calendar.YEAR) == mealDate.get(Calendar.YEAR);
            boolean sameMonth = sameYear && today.get(Calendar.MONTH) == mealDate.get(Calendar.MONTH);
            boolean sameWeek = sameYear && today.get(Calendar.WEEK_OF_YEAR) == mealDate.get(Calendar.WEEK_OF_YEAR);

            if (sameWeek) {
                weeklyCalories += meal.getCalories();
                weeklyProtein += meal.getProtein();
                weeklyCarbs += meal.getCarbs();
                weeklySugar += meal.getSugar();
                weeklyFat += meal.getFat();
                weeklyWater += meal.getWaterOz();
                weeklySoda += meal.getSodaOz();
            }

            if (sameMonth) {
                monthlyCalories += meal.getCalories();
                monthlyProtein += meal.getProtein();
                monthlyCarbs += meal.getCarbs();
                monthlySugar += meal.getSugar();
                monthlyFat += meal.getFat();
                monthlyWater += meal.getWaterOz();
                monthlySoda += meal.getSodaOz();
            }

            if (sameYear) {
                annualCalories += meal.getCalories();
                annualProtein += meal.getProtein();
                annualCarbs += meal.getCarbs();
                annualSugar += meal.getSugar();
                annualFat += meal.getFat();
                annualWater += meal.getWaterOz();
                annualSoda += meal.getSodaOz();
            }
        }

        tvWeeklyTotals.setText(formatTotals("Weekly Totals", weeklyCalories, weeklyProtein, weeklyCarbs, weeklySugar, weeklyFat, weeklyWater, weeklySoda));
        tvMonthlyTotals.setText(formatTotals("Monthly Totals", monthlyCalories, monthlyProtein, monthlyCarbs, monthlySugar, monthlyFat, monthlyWater, monthlySoda));
        tvAnnualTotals.setText(formatTotals("Annual Totals", annualCalories, annualProtein, annualCarbs, annualSugar, annualFat, annualWater, annualSoda));
    }

    private String formatTotals(String title, int calories, int protein, int carbs, int sugar, int fat, int water, int soda) {
        return title
                + "\nCalories: " + calories
                + "\nProtein: " + protein + " g"
                + "\nCarbs: " + carbs + " g"
                + "\nSugar: " + sugar + " g"
                + "\nFat: " + fat + " g"
                + "\nWater: " + water + " oz"
                + "\nSoda: " + soda + " oz";
    }

    private Calendar parseMealDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(dateString));
            return cal;
        } catch (Exception e) {
            return null;
        }
    }

    private void loadMeals() {
        try {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String mealsJson = prefs.getString(MEALS_KEY, "");

            mealsList.clear();

            if (mealsJson == null || mealsJson.isEmpty()) {
                return;
            }

            JSONArray jsonArray = new JSONArray(mealsJson);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject mealObject = jsonArray.getJSONObject(i);

                String name = mealObject.optString("name", "");
                String category = mealObject.optString("category", "");
                int calories = mealObject.optInt("calories", 0);
                int protein = mealObject.optInt("protein", 0);
                int carbs = mealObject.optInt("carbs", 0);
                int sugar = mealObject.optInt("sugar", 0);
                int fat = mealObject.optInt("fat", 0);
                int waterOz = mealObject.optInt("waterOz", 0);
                int sodaOz = mealObject.optInt("sodaOz", 0);
                String date = mealObject.optString("date", "");

                mealsList.add(new Meal(name, category, calories, protein, carbs, sugar, fat, waterOz, sodaOz, date));
            }

        } catch (Exception e) {
            // keep app from crashing if no saved data exists yet
        }
    }
}