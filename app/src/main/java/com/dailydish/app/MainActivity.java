package com.dailydish.app;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private static final String USERNAME_KEY = "user_name";
    private static final String ONBOARDING_COMPLETE_KEY = "onboarding_complete";
    private static final String COLOR_SCHEME_KEY = "color_scheme";
    private static final int RETENTION_DAYS = 30;

    private LinearLayout mainRoot;
    private LinearLayout totalsCard;

    private TextView tvCurrentDate;
    private TextView tvUserGreeting;
    private TextView tvDailyTotalsTitle;
    private TextView tvTodaysMealsTitle;
    private TextView tvTotalCalories;
    private TextView tvTotalProtein;
    private TextView tvTotalCarbs;
    private TextView tvTotalSugar;
    private TextView tvTotalFat;
    private TextView tvTotalWater;
    private TextView tvTotalSoda;

    private ImageButton btnSettingsGear;
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

        boolean onboardingComplete = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getBoolean(ONBOARDING_COMPLETE_KEY, false);

        if (!onboardingComplete) {
            startActivity(new Intent(MainActivity.this, OnboardingActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }

        mainRoot = findViewById(R.id.mainRoot);
        totalsCard = findViewById(R.id.totalsCard);

        tvCurrentDate = findViewById(R.id.tvCurrentDate);
        tvUserGreeting = findViewById(R.id.tvUserGreeting);
        tvDailyTotalsTitle = findViewById(R.id.tvDailyTotalsTitle);
        tvTodaysMealsTitle = findViewById(R.id.tvTodaysMealsTitle);
        tvTotalCalories = findViewById(R.id.tvTotalCalories);
        tvTotalProtein = findViewById(R.id.tvTotalProtein);
        tvTotalCarbs = findViewById(R.id.tvTotalCarbs);
        tvTotalSugar = findViewById(R.id.tvTotalSugar);
        tvTotalFat = findViewById(R.id.tvTotalFat);
        tvTotalWater = findViewById(R.id.tvTotalWater);
        tvTotalSoda = findViewById(R.id.tvTotalSoda);

        btnSettingsGear = findViewById(R.id.btnSettingsGear);
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
            if (visibleMealIndexes.isEmpty()) {
                selectedVisibleIndex = -1;
                lvMeals.clearChoices();
                adapter.notifyDataSetChanged();
                return;
            }

            if (selectedVisibleIndex == position) {
                selectedVisibleIndex = -1;
                lvMeals.clearChoices();
            } else {
                selectedVisibleIndex = position;
                lvMeals.setItemChecked(position, true);
            }

            adapter.notifyDataSetChanged();
        });

        btnSettingsGear.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
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

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Meal")
                    .setMessage("Are you sure you want to delete this meal?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        int actualIndex = visibleMealIndexes.get(selectedVisibleIndex);
                        mealsList.remove(actualIndex);

                        selectedVisibleIndex = -1;
                        lvMeals.clearChoices();
                        saveMeals();
                        updateUI();

                        Toast.makeText(MainActivity.this, "Meal deleted.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        btnStatistics.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
        });

        scheduleDailyReminder();

        loadMeals();
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyUserSettings();
        loadMeals();
        updateUI();
    }

    private void applyUserSettings() {
        String userName = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(USERNAME_KEY, "");

        if (userName != null && !userName.trim().isEmpty()) {
            tvUserGreeting.setText("Welcome, " + userName.trim() + "!");
        } else {
            tvUserGreeting.setText("Welcome!");
        }

        String colorScheme = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getString(COLOR_SCHEME_KEY, "purple");

        int backgroundColor;
        int cardColor;
        int primaryButtonColor;
        int editButtonColor;
        int deleteButtonColor;
        int statsButtonColor;
        int textColor;
        int secondaryTextColor;

        if ("blue".equals(colorScheme)) {
            backgroundColor = Color.parseColor("#F2F7FC");
            cardColor = Color.parseColor("#E5F0FA");
            primaryButtonColor = Color.parseColor("#3F6FA6");
            editButtonColor = Color.parseColor("#6F91BF");
            deleteButtonColor = Color.parseColor("#B24C63");
            statsButtonColor = Color.parseColor("#2F5D8C");
            textColor = Color.parseColor("#17324D");
            secondaryTextColor = Color.parseColor("#315B7D");
        } else if ("green".equals(colorScheme)) {
            backgroundColor = Color.parseColor("#F3FAF5");
            cardColor = Color.parseColor("#E2F3E7");
            primaryButtonColor = Color.parseColor("#4F8A5B");
            editButtonColor = Color.parseColor("#75A87E");
            deleteButtonColor = Color.parseColor("#B24C63");
            statsButtonColor = Color.parseColor("#3D6E49");
            textColor = Color.parseColor("#1F3D28");
            secondaryTextColor = Color.parseColor("#4C7455");
        } else if ("rose".equals(colorScheme)) {
            backgroundColor = Color.parseColor("#FFF4F6");
            cardColor = Color.parseColor("#F8E2E7");
            primaryButtonColor = Color.parseColor("#B24C63");
            editButtonColor = Color.parseColor("#C7798A");
            deleteButtonColor = Color.parseColor("#9F3F54");
            statsButtonColor = Color.parseColor("#7D5A8C");
            textColor = Color.parseColor("#4A1F2B");
            secondaryTextColor = Color.parseColor("#7D4454");
        } else {
            backgroundColor = Color.parseColor("#F7F4FB");
            cardColor = Color.parseColor("#EEE8F8");
            primaryButtonColor = Color.parseColor("#6E53A3");
            editButtonColor = Color.parseColor("#8A74B9");
            deleteButtonColor = Color.parseColor("#B24C63");
            statsButtonColor = Color.parseColor("#4F7CAC");
            textColor = Color.parseColor("#2E1A47");
            secondaryTextColor = Color.parseColor("#3F3154");
        }

        mainRoot.setBackgroundColor(backgroundColor);
        totalsCard.setBackgroundColor(cardColor);

        tvUserGreeting.setTextColor(textColor);
        tvDailyTotalsTitle.setTextColor(textColor);
        tvTodaysMealsTitle.setTextColor(textColor);
        tvCurrentDate.setTextColor(secondaryTextColor);

        tvTotalCalories.setTextColor(secondaryTextColor);
        tvTotalProtein.setTextColor(secondaryTextColor);
        tvTotalCarbs.setTextColor(secondaryTextColor);
        tvTotalSugar.setTextColor(secondaryTextColor);
        tvTotalFat.setTextColor(secondaryTextColor);
        tvTotalWater.setTextColor(secondaryTextColor);
        tvTotalSoda.setTextColor(secondaryTextColor);

        btnSettingsGear.setColorFilter(textColor);
        btnAddMeal.setBackgroundTintList(ColorStateList.valueOf(primaryButtonColor));
        btnEditMeal.setBackgroundTintList(ColorStateList.valueOf(editButtonColor));
        btnDeleteMeal.setBackgroundTintList(ColorStateList.valueOf(deleteButtonColor));
        btnStatistics.setBackgroundTintList(ColorStateList.valueOf(statsButtonColor));
    }

    private void scheduleDailyReminder() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(this, ReminderReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
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
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

    private String getTodayDisplayDate() {
        return new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US).format(new Date());
    }

    private boolean isWithinRetentionWindow(String dateString) {
        try {
            Date mealDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateString);

            Calendar cutoff = Calendar.getInstance();
            cutoff.set(Calendar.HOUR_OF_DAY, 0);
            cutoff.set(Calendar.MINUTE, 0);
            cutoff.set(Calendar.SECOND, 0);
            cutoff.set(Calendar.MILLISECOND, 0);
            cutoff.add(Calendar.DAY_OF_YEAR, -RETENTION_DAYS);

            return mealDate != null && !mealDate.before(cutoff.getTime());

        } catch (Exception e) {
            return false;
        }
    }

    private boolean pruneExpiredMeals() {
        boolean removed = false;

        for (int i = mealsList.size() - 1; i >= 0; i--) {
            if (!isWithinRetentionWindow(mealsList.get(i).getDate())) {
                mealsList.remove(i);
                removed = true;
            }
        }

        return removed;
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

        for (int i = mealsList.size() - 1; i >= 0; i--) {
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
            selectedVisibleIndex = -1;
            lvMeals.clearChoices();
            displayList.add("No meals logged yet for today.\nTap \"Add Meal\" to get started!");
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
                JSONObject obj = new JSONObject();

                obj.put("name", meal.getName());
                obj.put("category", meal.getCategory());
                obj.put("calories", meal.getCalories());
                obj.put("protein", meal.getProtein());
                obj.put("carbs", meal.getCarbs());
                obj.put("sugar", meal.getSugar());
                obj.put("fat", meal.getFat());
                obj.put("waterOz", meal.getWaterOz());
                obj.put("sodaOz", meal.getSodaOz());
                obj.put("date", meal.getDate());

                jsonArray.put(obj);
            }

            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putString(MEALS_KEY, jsonArray.toString())
                    .apply();

        } catch (Exception e) {
            Toast.makeText(this, "Error saving meals.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMeals() {
        try {
            String mealsJson = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .getString(MEALS_KEY, "");

            mealsList.clear();

            if (mealsJson != null && !mealsJson.isEmpty()) {
                JSONArray jsonArray = new JSONArray(mealsJson);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);

                    mealsList.add(new Meal(
                            o.optString("name"),
                            o.optString("category"),
                            o.optInt("calories"),
                            o.optInt("protein"),
                            o.optInt("carbs"),
                            o.optInt("sugar"),
                            o.optInt("fat"),
                            o.optInt("waterOz"),
                            o.optInt("sodaOz"),
                            o.optString("date", getTodayStorageDate())
                    ));
                }
            }

            if (pruneExpiredMeals()) {
                saveMeals();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error loading meals.", Toast.LENGTH_SHORT).show();
        }
    }
}