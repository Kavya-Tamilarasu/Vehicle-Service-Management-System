# Getting Started (Windows & IntelliJ IDEA)

This guide will help you set up and run the **Car Service User Management System** on your local machine using **IntelliJ IDEA**.

---

## 1. Prerequisites (The Basics)

Before you start, make sure you have these "Big Three" installed:

1.  **JDK 21**: The brain of the application. [Download here](https://adoptium.net/temurin/releases/?version=21).
2.  **MySQL Community Server**: Where your data lives. [Download here](https://dev.mysql.com/downloads/installer/).
3.  **IntelliJ IDEA (Community or Ultimate)**: Your workspace. [Download here](https://www.jetbrains.com/idea/download/).

> **Note on pgAdmin:** You might have heard of pgAdmin, but that is for *PostgreSQL*. This project uses **MySQL**, so we will use **MySQL Workbench** or IntelliJ's built-in tools instead.

---

## 2. Setting Up the Database

### Method A: Using MySQL Workbench (Easiest for Beginners)
1.  Open **MySQL Workbench**.
2.  Click on your **Local Instance** (usually named `Local instance MySQL80`).
3.  In the "Query" tab, type this exactly:
    ```sql
    CREATE DATABASE car_service_db;
    ```
4.  Click the **Lightning Bolt icon** ⚡ to run it. If you see a green checkmark at the bottom, you are good!

---

## 3. Opening the Project in IntelliJ

1.  Open **IntelliJ IDEA**.
2.  Click **Open** and select the folder where you downloaded this project.
3.  If IntelliJ asks, click **"Trust Project"**.
4.  Wait a minute! Look at the bottom right corner—let IntelliJ "Load Maven Project" and download all the necessary files.

---

## 4. Connecting Your Database to the Code

You have two ways to set your MySQL password. **DO NOT** change `application.properties` directly, or your password might accidentally be pushed to GitHub!

### Option A: Using a `.env` file (Recommended)
1.  In the project root, find the file named `.env.example`.
2.  Duplicate it and rename the copy to just `.env`.
3.  Open `.env` and put your password there:
    ```properties
    DB_PASSWORD=your_actual_password
    ```
    *The `.env` file is hidden from GitHub, so your password stays safe on your machine.*

### Option B: Using IntelliJ Settings
1.  In IntelliJ, click on the **Run Configuration** dropdown (near the Play button) and select **Edit Configurations...**.
2.  Find **Environment variables** and click the folder icon.
3.  Add a new variable:
    *   **Name:** `DB_PASSWORD`
    *   **Value:** `your_actual_password`
4.  Click **OK**.

---

## 5. Running the Application

You don't need to type commands in the terminal if you use IntelliJ!

1.  In the Project sidebar, navigate to:
    `src` -> `main` -> `java` -> `com.example.usermanagement` -> **`UsermanagementApplication.java`**.
2.  Look for the **Green Play Button** ▶️ next to `public class UsermanagementApplication`.
3.  Click it and select **Run 'UsermanagementApplication'**.
4.  Watch the **"Run" window** at the bottom. When you see `Started UsermanagementApplication in X seconds`, the app is alive!

---

## 6. How to Use It

1.  Open your browser (Chrome, Edge, etc.).
2.  Go to: **[http://localhost:8080](http://localhost:8080)**.
3.  **To Register:** Click the "Register" link, fill in your details, and hit submit.
4.  **To Login:** Use the email and password you just created.

---

## 🛠️ Common Problems (Don't Panic!)

*   **"Table not found":** You don't need to create tables! The app does it for you. Just make sure you created the `car_service_db` database in Step 2.
*   **"Access Denied for user 'root'":** This means your password in `application.properties` is wrong. Double-check it!
*   **"Port 8080 is already in use":** Another program is using that "door." To fix:
    *   Add `server.port=9090` to `application.properties`.
    *   Then go to `http://localhost:9090` instead.
