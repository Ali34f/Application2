package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application2.model.Employee;

/**
 * Activity for the employee dashboard.
 */
public class EmployeeDashboardActivity extends AppCompatActivity {

    private Button btnProfile, btnRequestHoliday, btnNotifications, btnSettings;
    private TextView welcomeText, backToLogin;
    private String employeeEmail;
    private DatabaseHelper dbHelper;
    private Employee loggedInEmployee;

    private static final String TAG = "EmployeeDashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        // Initialize UI elements
        initializeViews();

        // Retrieve the logged-in employee's email from the intent
        employeeEmail = getIntent().getStringExtra("EMPLOYEE_EMAIL");

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Fetch and display the employee's name
        displayWelcomeMessage();

        // Set button click listeners
        setButtonListeners();
    }

    /**
     * Initialize views from the layout.
     */
    private void initializeViews() {
        welcomeText = findViewById(R.id.welcomeMessage);
        btnProfile = findViewById(R.id.btnProfile);
        btnRequestHoliday = findViewById(R.id.btnRequestHoliday);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnSettings = findViewById(R.id.btnSettings);
        backToLogin = findViewById(R.id.backToLogin);
    }

    /**
     * Fetch the employee's details from the database and display a welcome message.
     */
    private void displayWelcomeMessage() {
        if (employeeEmail == null || employeeEmail.isEmpty()) {
            Log.e(TAG, "Employee email is null or empty.");
            showToast("Error: Missing employee email.");
            welcomeText.setText("Welcome Back!");
            return;
        }

        loggedInEmployee = dbHelper.getEmployeeByEmail(employeeEmail);

        if (loggedInEmployee != null && welcomeText != null) {
            welcomeText.setText("Welcome Back, " + loggedInEmployee.getName() + "!");
        } else {
            Log.e(TAG, "Employee not found in database.");
            showToast("Error: Employee not found.");
            if (welcomeText != null) {
                welcomeText.setText("Welcome Back!");
            }
        }
    }

    /**
     * Set button click listeners for navigation.
     */
    private void setButtonListeners() {
        btnProfile.setOnClickListener(v -> navigateTo(EmployeeProfileActivity.class));
        btnRequestHoliday.setOnClickListener(v -> navigateTo(EmployeeHolidayRequestActivity.class));
        btnNotifications.setOnClickListener(v -> navigateTo(EmployeeNotificationsActivity.class));
        btnSettings.setOnClickListener(v -> navigateTo(EmployeeSettingsActivity.class));

        backToLogin.setOnClickListener(v -> navigateTo(MainActivity.class));
    }

    /**
     * Navigate to the specified activity and pass the employee email.
     *
     * @param targetActivity The activity to navigate to.
     */
    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(EmployeeDashboardActivity.this, targetActivity);
        intent.putExtra("EMPLOYEE_EMAIL", employeeEmail);
        startActivity(intent);
    }

    /**
     * Show a toast message.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
