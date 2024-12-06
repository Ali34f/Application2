package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeDashboardActivity extends AppCompatActivity {

    private Button btnProfile, btnRequestHoliday, btnNotifications, btnSettings;
    private TextView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        // Initialize UI elements
        btnProfile = findViewById(R.id.btnProfile);
        btnRequestHoliday = findViewById(R.id.btnRequestHoliday);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnSettings = findViewById(R.id.btnSettings);
        backToLogin = findViewById(R.id.backToLogin);

        // Set button click listeners
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(EmployeeProfileActivity.class);
            }
        });

        btnRequestHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(EmployeeHolidayRequestActivity.class);
            }
        });

        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(EmployeeNotificationsActivity.class);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(EmployeeSettingsActivity.class);
            }
        });

        // Back to login
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(EmployeeLoginActivity.class);
            }
        });
    }

    /**
     * Navigate to the specified activity.
     *
     * @param targetActivity The activity to navigate to.
     */
    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(EmployeeDashboardActivity.this, targetActivity);
        startActivity(intent);
    }
}
