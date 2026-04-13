package com.dailydish.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_MEAL = 1;
    private static final int REQUEST_CODE_EDIT_MEAL = 2;
    private static final String PREFS_NAME = "DailyDishPrefs";
    private static final String MEALS_KEY = "meals_json";
    private static final int RETENTION_DAYS = 30;

    private TextView tvCurrentDate;
    private TextView tvTotalCalories;
    private TextView tvTotalProtein;
    private TextView tvTotalCarbs;
    private TextView tvTotalSugar;
    private TextView tvTotalFat;
    private TextView tvTotalWater;
    private TextView tvTotalSoda;

    private Button btnAddMeal;
    private Button btnEditMeal;
    private Button btnDeleteMeal;
    private Button btnStatistics;
    private ListView lvMeals;

    private final ArrayList<Meal> mealsList = new ArrayList<>();
    private final ArrayList<String> displayList = new ArrayList<>();
    private final ArrayList<Integer> visibleMealIndexes = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private int selectedVisibleIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCurrentDate = findViewById(R.id.tvCurrentDate);
        tvTotalCalories = findViewById(R.id.tvTotalCalories);
        tvTotalProtein = findViewById(R.id.tvTotalProtein);
        tvTotalCarbs = findViewById(R.id.tvTotalCarbs);
        tvTotalSugar = findViewById(R.id.tvTotalSugar);
        tvTotalFat = findViewById(R.id.tvTotalFat);
        tvTotalWater = findViewById(R.id.tvTotalWater);
        tvTotalSoda = findViewById(R.id.tvTotalSoda);

        btnAddMeal = findViewById(R.id.btnAddMeal);
        btnEditMeal = findViewById(R.id.btnEditMeal);
        btnDeleteMeal = findViewById(R.id.btnDeleteMeal);
        btnStatistics = findViewById(R.id.btnStatistics);
        lvMeals = findViewById(R.id.lvMeals);

        tvCurrentDate.setText(getTodayDisplayDate());

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, displayList);
        lvMeals.setAdapter(adapter);
        lvMeals.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lvMeals.setOnItemClickListener((parent, view, position, id) -> {
            selectedVisibleIndex = position;
            lvMeals.setItemChecked(position, true);
        });

        btnAddMeal.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMealActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_MEAL);
        });

        btnEditMeal.setOnClickListener(v -> {
            if (selectedVisibleIndex < 0 || selectedVisibleIndex >= visibleMealIndexes.size()) {
                Toast.makeText(MainActivity.this, "Select a meal to edit.", Toast.LENGTH_SHORT).show();
                return;
            }

            int actualIndex = visibleMealIndexes.get(selectedVisibleIndex);
            Meal selectedMeal = mealsList.get(actualIndex);

            Intent intent = new Intent(MainActivity.this, AddMealActivity.class);
            intent.putExtra("editMode", true);
            intent.putExtra("mealIndex", actualIndex);
            intent.putExtra("mealData", selectedMeal);
            startActivityForResult(intent, REQUEST_CODE_EDIT_MEAL);
        });

        btnDeleteMeal.setOnClickListener(v -> {
            if (selectedVisibleIndex < 0 || selectedVisibleIndex >= visibleMealIndexes.size()) {
                Toast.makeText(MainActivity.this, "Select a meal to delete.", Toast.LENGTH_SHORT).show();
                return;
            }

            int actualIndex = visibleMealIndexes.get(selectedVisibleIndex);
            mealsList.remove(actualIndex);

            selectedVisibleIndex = -1;
            lvMeals.clearChoices();
            saveMeals();
            updateUI();
            Toast.makeText(MainActivity.this, "Meal deleted.", Toast.LENGTH_SHORT).show();
        });

        btnStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        loadMeals();
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMeals();
        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        String name = data.getStringExtra("name");
        String category = data.getStringExtra("category");

        int calories = parseNumber(data.getStringExtra("calories"));
        int protein = parseNumber(data.getStringExtra("protein"));
        int carbs = parseNumber(data.getStringExtra("carbs"));
        int sugar = parseNumber(data.getStringExtra("sugar"));
        int fat = parseNumber(data.getStringExtra("fat"));
        int waterOz = parseNumber(data.getStringExtra("waterOz"));
        int sodaOz = parseNumber(data.getStringExtra("sodaOz"));

        if (requestCode == REQUEST_CODE_ADD_MEAL) {
            Meal meal = new Meal(
                    name,
                    category,
                    calories,
                    protein,
                    carbs,
                    sugar,
                    fat,
                    waterOz,
                    sodaOz,
                    getTodayStorageDate()
            );
            mealsList.add(meal);
            Toast.makeText(this, "Meal added.", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_CODE_EDIT_MEAL) {
            int mealIndex = data.getIntExtra("mealIndex", -1);
            if (mealIndex >= 0 && mealIndex < mealsList.size()) {
                Meal existingMeal = mealsList.get(mealIndex);

                Meal updatedMeal = new Meal(
                        name,
                        category,
                        calories,
                        protein,
                        carbs,
                        sugar,
                        fat,
                        waterOz,
                        sodaOz,
                        existingMeal.getDate()
                );

                mealsList.set(mealIndex, updatedMeal);
                Toast.makeText(this, "Meal updated.", Toast.LENGTH_SHORT).show();
            }
        }

        selectedVisibleIndex = -1;
        lvMeals.clearChoices();
        saveMeals();
        updateUI();
    }

    private int parseNumber(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return 0;
            }
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private String getTodayStorageDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(new Date());
    }

    private String getTodayDisplayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US);
        return sdf.format(new Date());
    }

    private Date parseStorageDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            sdf.setLenient(false);
            return sdf.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isWithinRetentionWindow(String dateString) {
        Date mealDate = parseStorageDate(dateString);
        if (mealDate == null) {
            return false;
        }

        Calendar cutoff = Calendar.getInstance();
        cutoff.set(Calendar.HOUR_OF_DAY, 0);
        cutoff.set(Calendar.MINUTE, 0);
        cutoff.set(Calendar.SECOND, 0);
        cutoff.set(Calendar.MILLISECOND, 0);
        cutoff.add(Calendar.DAY_OF_YEAR, -RETENTION_DAYS);

        return !mealDate.before(cutoff.getTime());
    }

    private boolean pruneExpiredMeals() {
        boolean removedAny = false;

        for (int i = mealsList.size() - 1; i >= 0; i--) {
            Meal meal = mealsList.get(i);
            if (!isWithinRetentionWindow(meal.getDate())) {
                mealsList.remove(i);
                removedAny = true;
            }
        }

        return removedAny;
    }

    private void updateUI() {
        int totalCalories = 0;
        int totalProtein = 0;
        int totalCarbs = 0;
        int totalSugar = 0;
        int totalFat = 0;
        int totalWater = 0;
        int totalSoda = 0;

        String today = getTodayStorageDate();

        displayList.clear();
        visibleMealIndexes.clear();

        for (int i = 0; i < mealsList.size(); i++) {
            Meal meal = mealsList.get(i);

            if (today.equals(meal.getDate())) {
                totalCalories += meal.getCalories();
                totalProtein += meal.getProtein();
                totalCarbs += meal.getCarbs();
                totalSugar += meal.getSugar();
                totalFat += meal.getFat();
                totalWater += meal.getWaterOz();
                totalSoda += meal.getSodaOz();

                displayList.add(meal.toString());
                visibleMealIndexes.add(i);
            }
        }

        if (displayList.isEmpty()) {
            displayList.add("No meals logged for today.");
        }

        tvCurrentDate.setText(getTodayDisplayDate());
        tvTotalCalories.setText("Calories: " + totalCalories);
        tvTotalProtein.setText("Protein: " + totalProtein + " g");
        tvTotalCarbs.setText("Carbs: " + totalCarbs + " g");
        tvTotalSugar.setText("Sugar: " + totalSugar + " g");
        tvTotalFat.setText("Fat: " + totalFat + " g");
        tvTotalWater.setText("Water: " + totalWater + " oz");
        tvTotalSoda.setText("Soda: " + totalSoda + " oz");

        adapter.notifyDataSetChanged();
    }

    private void saveMeals() {
        try {
            JSONArray jsonArray = new JSONArray();

            for (Meal meal : mealsList) {
                JSONObject mealObject = new JSONObject();
                mealObject.put("name", meal.getName());
                mealObject.put("category", meal.getCategory());
                mealObject.put("calories", meal.getCalories());
                mealObject.put("protein", meal.getProtein());
                mealObject.put("carbs", meal.getCarbs());
                mealObject.put("sugar", meal.getSugar());
                mealObject.put("fat", meal.getFat());
                mealObject.put("waterOz", meal.getWaterOz());
                mealObject.put("sodaOz", meal.getSodaOz());
                mealObject.put("date", meal.getDate());
                jsonArray.put(mealObject);
            }

            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            prefs.edit().putString(MEALS_KEY, jsonArray.toString()).apply();

        } catch (Exception e) {
            Toast.makeText(this, "Error saving meals.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMeals() {
        try {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String mealsJson = prefs.getString(MEALS_KEY, "");

            mealsList.clear();

            if (mealsJson != null && !mealsJson.isEmpty()) {
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
                    String date = mealObject.optString("date", getTodayStorageDate());

                    mealsList.add(new Meal(name, category, calories, protein, carbs, sugar, fat, waterOz, sodaOz, date));
                }
            }

            boolean removedAny = pruneExpiredMeals();
            if (removedAny) {
                saveMeals();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error loading meals.", Toast.LENGTH_SHORT).show();
        }
    }
}