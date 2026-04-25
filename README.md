# SchoolRepo-AndroidApp: Daily Dish

Spring 2 - 2026 Android App Project

---

## Project Overview

This repository contains the full development documentation, source code, and planning materials for **DailyDish**, a native Android meal tracking application developed for COM-437 Mobile Application Development.

DailyDish is designed to help users track meals, monitor nutritional intake, and review short-term dietary trends through a simple and user-friendly interface.

See the Wiki for full project documentation and development updates.

---

## Project Description

DailyDish is a native Android application that allows users to:

* Track meals throughout the day  
* Monitor nutrition and beverage intake  
* View calculated dietary statistics  
* Manage meals through full CRUD functionality  
* Review weekly, monthly, and annual summaries  
* Personalize their experience with themes and user preferences  

The app was built with a focus on usability, modular design, and privacy-conscious local data storage.

---

## Problem Addressed

Many meal-tracking apps become overly complex, cluttered, or difficult to maintain consistently. DailyDish addresses this issue by providing:

* A clean and intuitive interface  
* Fast meal entry and editing  
* Simple nutritional tracking  
* Automated statistics generation  
* Limited data retention for improved privacy  
* Lightweight personalization without complexity  

---

## Platform / Development Environment

* Native Android Application  
* Developed using Android Studio  
* Programming Language: Java  
* User Interface: XML Layouts  

---

## Core Functionality

### Meal Management (CRUD)

Users can:

* Add meals  
* Edit existing meals  
* Delete meals (with confirmation dialog for safety)  

---

### Nutrition Tracking

Each meal records:

* Calories  
* Protein (g)  
* Carbohydrates (g)  
* Sugar (g)  
* Fat (g)  
* Water Intake (oz)  
* Soda Intake (oz)  

---

### Daily Dashboard

Main screen displays:

* Current date  
* Personalized greeting  
* Daily meal list (newest first)  
* Daily nutritional totals  

---

### Statistics Tracking

Statistics page calculates:

* Weekly totals  
* Monthly totals  
* Annual totals  

---

## New Enhancements (Version 6.0 – Personalization & UX Refinement)

Recent improvements significantly enhanced usability, personalization, and UI polish:

### User Personalization

* One-time onboarding to capture user name  
* Personalized greeting on home screen  
* Personalized push notifications  

---

### Settings System

* Dedicated Settings screen accessible via gear icon  
* Edit username at any time  
* Toggle daily notifications (on/off)  
* Select color theme  

---

### Color Themes

Users can dynamically select:

* Purple Classic  
* Blue Calm  
* Green Fresh  
* Rose Warm  

Themes apply instantly and persist across sessions.

---

### Notification System

* Daily reminder notifications  
* Runtime permission handling (Android 13+)  
* Personalized notification messages  

---

### UX Improvements

* Delete confirmation dialog added  
* Automatic meal deselection  
* Improved meal list ordering (newest first)  
* Consistent font sizing and alignment in Daily Totals  
* Standard Android settings icon for familiarity  

---

## Application Structure

### MainActivity

Responsible for:

* Displaying current day meal totals  
* Displaying saved meals  
* Showing personalized greeting  
* Navigation to Add/Edit, Statistics, and Settings  

---

### AddMealActivity

Responsible for:

* Adding new meals  
* Editing existing meals  
* Canceling entry and returning home  

---

### StatisticsActivity

Responsible for:

* Displaying aggregated statistics  
* Returning user to home screen  

---

### OnboardingActivity

Responsible for:

* First-time user setup  
* Capturing and storing username  

---

### SettingsActivity

Responsible for:

* Managing user preferences  
* Updating username  
* Toggling notifications  
* Selecting color themes  

---

### ReminderReceiver

Responsible for:

* Handling scheduled notifications  
* Personalizing notification messages  

---

## Data Model

Each meal entry contains:

* Meal Name  
* Category  
* Calories  
* Protein  
* Carbs  
* Sugar  
* Fat  
* Water  
* Soda  
* Date  

---

## Data Storage Approach

DailyDish currently uses:

* **SharedPreferences local storage**

Benefits:

* Lightweight implementation  
* Fast read/write performance  
* No internet/server dependency  
* Supports user personalization settings  

---

## Data Retention Policy

* Meal data is retained for **30 days only**  
* Older records are automatically removed  
* Reduces unnecessary long-term storage of user behavior  

---

## UI / UX Design Philosophy

The app was designed around:

* Simplicity  
* Readability  
* Minimal user friction  
* Clear navigation  
* Fast interaction flow  

Design includes:

* Card-based layouts  
* Scrollable forms  
* Clearly labeled nutritional fields  
* Consistent button styling  
* Dynamic theming  

---

## Version Changelog

### Version 1.0 – Initial Concept

* Defined app purpose  
* Planned core features  

---

### Version 2.0 – Design

* Created wireframes  
* Planned navigation and structure  

---

### Version 3.0 – Core Development

* Implemented CRUD operations  
* Added nutrition tracking  
* Added local storage  

---

### Version 4.0 – UI Enhancements

* Improved layouts and navigation  
* Added cancel/home controls  

---

### Version 5.0 – Privacy Improvements

* Implemented 30-day data retention  
* Reduced long-term data storage  

---

### Version 6.0 – Personalization & UX Refinement

* Added onboarding system  
* Introduced settings screen  
* Implemented color themes  
* Added notification system  
* Improved usability and UI consistency  

---

## Future Enhancements

Potential future improvements:

* SQLite database migration  
* Cloud synchronization  
* User authentication  
* Graph/chart visualization  
* Recipe integration  
* Grocery list support  

---

## GitHub Wiki

https://github.com/JessiMarosi/SchoolRepo-AndroidApp/wiki

---

## Summary

DailyDish demonstrates real-world mobile development practices including:

* UI/UX design  
* CRUD implementation  
* Data persistence  
* Modular architecture  
* Personalization systems  
* Notification handling  
* Privacy-conscious design  

The project evolved into a polished, user-focused Android application that balances functionality, simplicity, and thoughtful design.
