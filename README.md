# SchoolRepo-AndroidApp

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
* Android Studio  
* Java  
* XML Layouts  

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

### User Personalization

* One-time onboarding to capture user name  
* Personalized greeting on home screen  
* Personalized notifications  

---

### Settings System

* Dedicated Settings screen accessible via gear icon  
* Edit username at any time  
* Toggle notifications (on/off)  
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

### Push Notification System

* Daily push notification scheduled for **7:00 PM (user’s local time)**  
* Runtime permission handling for Android 13+  
* Personalized notification messages using stored username  

The notification is intentionally scheduled for the end of the day to align with natural user behavior. This encourages users to log any remaining meals before the day concludes, improving consistency without being intrusive.

---

### UX Improvements

* Delete confirmation dialog added  
* Automatic meal deselection  
* Improved meal list ordering (newest first)  
* Consistent font sizing and alignment in Daily Totals  
* Standard Android settings icon  

---

## Application Structure

### MainActivity

* Displays daily totals and meals  
* Shows personalized greeting  
* Handles navigation  

---

### AddMealActivity

* Adds and edits meals  
* Supports cancel functionality  

---

### StatisticsActivity

* Displays aggregated data  

---

### OnboardingActivity

* First-time user setup  

---

### SettingsActivity

* Manages user preferences  
* Handles themes, name, and notifications  

---

### ReminderReceiver

* Handles scheduled push notifications  
* Personalizes reminder messages  

---

## Data Storage Approach

DailyDish uses:

* SharedPreferences (local storage)

Benefits:

* Lightweight  
* Fast  
* No server dependency  
* Supports user personalization  

---

## Data Retention Policy

* Meal data retained for **30 days only**  
* Older records automatically removed  
* Supports privacy-focused design  

---

## UI / UX Design Philosophy

The app was designed around:

* Simplicity  
* Readability  
* Minimal friction  
* Fast interaction  
* Real-world user behavior  

---

## Version Changelog

### Version 1.0 – Concept
Initial planning and idea

### Version 2.0 – Design
Wireframes and structure

### Version 3.0 – Core Build
CRUD + tracking

### Version 4.0 – UI Improvements
Navigation + layout

### Version 5.0 – Privacy
30-day retention system

### Version 6.0 – Personalization & UX Refinement

* Onboarding system  
* Settings system  
* Color themes  
* Push notification system (7 PM reminder)  
* UX and UI improvements  

---

## Future Enhancements

* SQLite integration  
* Cloud sync  
* Graph visualizations  
* Recipe tracking  

---

## GitHub Wiki

https://github.com/JessiMarosi/SchoolRepo-AndroidApp/wiki

---

## Summary

DailyDish demonstrates:

* Android development fundamentals  
* UI/UX design principles  
* Data persistence  
* Personalization systems  
* Push notification implementation  
* Privacy-conscious design  

The application evolved into a polished, user-focused mobile solution balancing simplicity, functionality, and real-world usability.
