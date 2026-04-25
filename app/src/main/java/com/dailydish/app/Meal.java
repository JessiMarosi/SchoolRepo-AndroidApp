package com.dailydish.app;

import java.io.Serializable;

public class Meal implements Serializable {
    private String name;
    private String category;
    private int calories;
    private int protein;
    private int carbs;
    private int sugar;
    private int fat;
    private int waterOz;
    private int sodaOz;
    private String date;

    public Meal(String name, String category, int calories, int protein, int carbs, int sugar, int fat, int waterOz, int sodaOz, String date) {
        this.name = name;
        this.category = category;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.sugar = sugar;
        this.fat = fat;
        this.waterOz = waterOz;
        this.sodaOz = sodaOz;
        this.date = date;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getCalories() { return calories; }
    public int getProtein() { return protein; }
    public int getCarbs() { return carbs; }
    public int getSugar() { return sugar; }
    public int getFat() { return fat; }
    public int getWaterOz() { return waterOz; }
    public int getSodaOz() { return sodaOz; }
    public String getDate() { return date; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setCalories(int calories) { this.calories = calories; }
    public void setProtein(int protein) { this.protein = protein; }
    public void setCarbs(int carbs) { this.carbs = carbs; }
    public void setSugar(int sugar) { this.sugar = sugar; }
    public void setFat(int fat) { this.fat = fat; }
    public void setWaterOz(int waterOz) { this.waterOz = waterOz; }
    public void setSodaOz(int sodaOz) { this.sodaOz = sodaOz; }
    public void setDate(String date) { this.date = date; }

    private String formatText(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "Unspecified";
        }

        String clean = value.trim();
        return clean.substring(0, 1).toUpperCase() + clean.substring(1);
    }

    @Override
    public String toString() {
        return "[" + formatText(category) + "] " + formatText(name)
                + "\nCalories: " + calories + " | Protein: " + protein + "g | Carbs: " + carbs + "g"
                + "\nSugar: " + sugar + "g | Fat: " + fat + "g"
                + "\nWater: " + waterOz + " oz | Soda: " + sodaOz + " oz";
    }
}