# Portal Admin Ditmawa - Student Registration System

[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Room Database](https://img.shields.io/badge/Room_Database-4CAF50?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/training/data-storage/room)
[![Material Design](https://img.shields.io/badge/Material_Design-757575?style=for-the-badge&logo=material-design&logoColor=white)](https://m3.material.io/)

> A native Android application designed for campus administration to manage new student registrations efficiently, featuring a robust local database and real-time data visualization.

## Project Overview
The **Portal Admin Ditmawa** is a role-based administration system developed to streamline the management of student registration data. Built with **Kotlin and Jetpack Compose**, the application leverages a strict **MVVM architecture** coupled with **Room Database (SQLite)** to ensure secure, non-volatile local data persistence.

This project focuses on data integrity, implementing rigorous client-side validation rules, and transforming raw database entries into insightful visual analytics for administrators.

## Technical Feature Highlights & UI Walkthrough

### 1. Role-Based Authentication
Secure entry point restricted to authorized personnel.

| Feature | Technical Implementation | Preview |
| :--- | :--- | :--- |
| **Static RBAC Login** | Access is heavily restricted via hardcoded credentials (`admin.ditmawa@its.ac.id` / `its123`). Unauthorized attempts are automatically rejected, ensuring only designated administrators can access the dashboard. | <img src="https://github.com/user-attachments/assets/bcd72217-7782-4179-8010-2537eb1f8915" width="250"><br><img src="https://github.com/user-attachments/assets/e0a22bfc-0ae6-469a-88d6-2c2509e69c72" width="250"> |

### 2. Data Visualization & Analytics Dashboard
Transforming raw registration data into actionable insights.

| Feature | Technical Implementation | Preview |
| :--- | :--- | :--- |
| **Real-Time Analytics** | The `ViewModel` calculates and processes data from the local database to render interactive Pie Charts representing Gender Demographics, Faculty Distribution, Top 10 Study Programs, and Origin Province maps. | <img src="https://github.com/user-attachments/assets/89672ee0-3ddc-4646-8b38-66a15b96b358" width="250"><br><img src="https://github.com/user-attachments/assets/767defad-0717-4417-a1a8-e5bf4472b87c" width="250"> |

### 3. Data Integrity & Input Validation (Create)
Ensuring all incoming data is standardized and error-free before database insertion.

| Feature | Technical Implementation | Preview |
| :--- | :--- | :--- |
| **Strict Form Validation** | Implements mandatory field checks, domain-specific email validation (must end in `@student.its.ac.id`), and automatic data trimming/lowercasing to prevent duplication and search errors. | <img src="https://github.com/user-attachments/assets/0bb8b805-845f-49f4-b6ce-0bdc331de76e" width="250"> |
| **Controlled Input Methods** | Utilizes Searchable Dropdowns for Province, Faculty, and Study Program to minimize typos, alongside a binary toggle for Gender selection to maintain consistent data formats. | *(Refer to the Add Data image above)* |

### 4. Full CRUD Implementation (Read, Update, Delete)
Comprehensive management of the local SQLite database.

| Feature | Technical Implementation | Preview |
| :--- | :--- | :--- |
| **Read & Real-Time Search** | Renders student data efficiently using `LazyColumn`. Features a real-time search function that filters the list dynamically based on character matches in the student's name. | <img src="https://github.com/user-attachments/assets/97ea292b-8b52-44fc-8a8c-1bbff20343f6" width="250"><br><img src="https://github.com/user-attachments/assets/28783e11-c802-4760-b4be-668f45dac44b" width="250"> |
| **Update (Pre-filled Forms)** | Modifying an entry triggers a dialog pre-filled with the existing record. The system executes an `UPDATE` query based on the student's Primary Key upon saving. | <img src="https://github.com/user-attachments/assets/b06ade93-ab45-4f5e-a840-42982a911f56" width="250"> |
| **Safe Delete** | Implements a multi-layered safety mechanism. Deletion requests are halted by a confirmation `AlertDialog` to prevent accidental loss of crucial administrative data. | <img src="https://github.com/user-attachments/assets/b779812b-52ed-4d93-a57d-eb7612903b34" width="250"> |

## System Architecture & Data Persistence
This application demonstrates high reliability in local data storage.

*   **Local Backend:** Powered by **Room Database**, ensuring data is non-volatile. Records remain securely saved as a `.db` file in the device's isolated internal directory, surviving app force-closures, memory clears, or device reboots (verified via Android Studio Database Inspector).
*   **Architecture:** **Model-View-ViewModel (MVVM)** separates business logic from the UI, resulting in a clean, scalable codebase that handles configuration changes safely.
*   **UI Toolkit:** Built entirely with **Jetpack Compose** for a modern, declarative, and responsive user interface.

---
