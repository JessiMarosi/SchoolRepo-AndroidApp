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

The app was built with a focus on usability, modular design, and privacy-conscious local data storage.

---

## Problem Addressed

Many meal-tracking apps become overly complex, cluttered, or difficult to maintain consistently. DailyDish addresses this issue by providing:

* A clean and intuitive interface
* Fast meal entry and editing
* Simple nutritional tracking
* Automated statistics generation
* Limited data retention for improved privacy

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
* Delete meals

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
* Daily meal list
* Daily nutritional totals

---

### Statistics Tracking

Statistics page calculates:

* Weekly totals
* Monthly totals
* Annual totals

---

## Application Structure

### MainActivity

Responsible for:

* Displaying current day meal totals
* Displaying saved meals
* Navigation to Add/Edit Meal and Statistics

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

Benefits of this approach:

* Lightweight implementation
* Fast read/write performance
* Ideal for smaller applications
* No internet/server dependency

---

## Data Retention Policy

To support privacy-conscious development practices:

* Meal data is retained for **30 days only**
* Records older than 30 days are automatically removed
* This minimizes unnecessary long-term storage of user behavioral data

---

## UI / UX Design Philosophy

The app was designed around the following principles:

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

---

## Architecture / Design Concepts Applied

DailyDish follows Android development best practices including:

* Separation of concerns
* Modular activity-based architecture
* Lifecycle-aware design principles
* Structured UI-to-data flow

---

## Version Changelog

### Version 1.0 – Initial Concept

* Created project concept
* Defined app purpose/problem
* Planned core features

---

### Version 2.0 – Design / Wireframes

* Built wireframes
* Designed UI structure
* Planned navigation and data model

---

### Version 3.0 – Core Development

* Built functional Android application
* Implemented CRUD operations
* Added nutritional tracking
* Added persistent local storage

---

### Version 4.0 – UI/UX Enhancements

* Improved design consistency
* Added cancel/home navigation buttons
* Refined layouts and scrolling

---

### Version 5.0 – Privacy/Data Improvements

* Added 30-day retention model
* Improved privacy-conscious data handling
* Limited unnecessary long-term data storage

---

## Future Enhancements

Potential future improvements include:

* SQLite database migration
* Cloud synchronization
* User authentication
* Graph/chart visualization
* Recipe integration
* Grocery list support

---

## GitHub Wiki

Project documentation and instructional guides can be found here:

**Wiki:**
https://github.com/JessiMarosi/SchoolRepo-AndroidApp/wiki

---

## Summary

DailyDish demonstrates the practical application of Android mobile development principles including:

* UI/UX design
* CRUD implementation
* Data persistence
* Modular architecture
* Documentation/version control
* Privacy-conscious application design

The project evolved from concept and wireframes into a fully functioning Android application that reflects real-world software development practices.
