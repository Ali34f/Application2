[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/1oOjCPDs)
# 📋 Employee Management System App

This project is a mobile application designed to help administrators manage employee data and holiday requests efficiently. Built for Android, the app offers a clean, professional interface with key functionalities such as employee record management, holiday request approvals, and notification customization. This README provides an overview of the app, installation instructions, key features, and API details.

---

## 📜 Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Technologies Used](#technologies-used)
- [API Endpoints](#api-endpoints)
- [Database Details](#database-details)
- [Screenshots](#screenshots)
- [GitHub Repository](#github-repository)
- [Acknowledgments](#acknowledgments)

---

## 🌟 Features

### Admin Functionalities:
- 👤 **Login and Authentication**: 
  - Secure login with username/password or Google sign-in.
- 📂 **Employee Management**:
  - Add, view, edit, and delete employee records.
  - Fetch employee details by ID.
- 📅 **Holiday Requests**:
  - Approve or reject holiday requests.
  - View pending and processed requests.
- 🔔 **Notifications**:
  - Notify users of request statuses and updates.
- ⚙️ **Settings**:
  - Manage notification preferences and account settings.

### Offline Capabilities:
- 🗂️ **SQLite Database**:
  - Local storage for login credentials and holiday requests.

---

## 🛠️ Installation

### Prerequisites:
1. **Android Studio** installed.
2. **Java Development Kit (JDK)** 8 or higher.

### Steps:
1. Clone the repository:
   ```bash
   git clone https://github.com/Ali34f/Application2.git

2. Open the project in Android Studio.
3. Run the app on an emulator or physical device.

## 💻 Technologies Used

- Android Studio: IDE for app development.
- Java: Programming language for backend logic.
- SQLite: Local database for offline functionality.
- Postman: API testing and validation.

## 🌐 API Endpoints

| **Method** | **Endpoint**           | **Description**                     |
|------------|------------------------|-------------------------------------|
| `GET`      | `/employees`           | Fetch all employee records.         |
| `GET`      | `/employees/{id}`      | Fetch employee details by ID.       |
| `POST`     | `/employees`           | Add a new employee record.          |
| `PUT`      | `/employees/{id}`      | Update an employee's details.       |
| `DELETE`   | `/employees/{id}`      | Delete an employee record.          |

All endpoints were tested using Postman to ensure functionality and data integrity.

## 💾 Database Details

### SQLite Schema:
| **Table**       | **Column**       | **Type**      |
|------------------|------------------|---------------|
| `Login`         | `username`       | `TEXT`        |
|                 | `password`       | `TEXT`        |
| `HolidayRequests`| `id`            | `INTEGER`     |
|                 | `employee_id`    | `INTEGER`     |
|                 | `start_date`     | `DATE`        |
|                 | `end_date`       | `DATE`        |

## 📸 Screenshots

Login Screen:

Admin Dashboard:

Employee Management:

## 🔗 GitHub Repository
The complete project is hosted on GitHub and can be accessed using the link below:

🔗 [Github Repository](https://github.com/Ali34f/Application2)

## 🤝 Acknowledgments
Special thanks to:

University Team for their guidance.
Peers for collaboration and feedback.
