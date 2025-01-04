package com.example.application2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 100;

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

        // Check and request notification permission
        checkAndRequestNotificationPermission();
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

    /**
     * Check and request notification permission (for Android 13+).
     */
    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );
            } else {
                Log.d(TAG, "Notification permission already granted.");
            }
        } else {
            Log.d(TAG, "Notification permission not required for this Android version.");
        }
    }

    /**
     * Handle the result of the notification permission request.
     *
     * @param requestCode  The request code for the permission.
     * @param permissions  The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission granted.");
                showToast("Notification permission granted.");
            } else {
                Log.e(TAG, "Notification permission denied.");
                showToast("Notification permission denied.");
            }
        }
    }
}
