package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Back to Login functionality
        TextView tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class); // Redirect to LoginActivity
                startActivity(intent);
                finish(); // Optional: Close the DashboardActivity
            }
        });

        // Employee Management Button
        LinearLayout btnEmployeeManagement = findViewById(R.id.btnEmployeeManagement);
        btnEmployeeManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EmployeeManagementActivity.class);
                startActivity(intent);
            }
        });

        // Holiday Requests Button
        LinearLayout btnHolidayRequests = findViewById(R.id.btnHolidayRequests);
        btnHolidayRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, HolidayRequestActivity.class);
                startActivity(intent);
            }
        });

        // Notifications Button
        LinearLayout btnNotifications = findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });

        // Settings Button
        LinearLayout btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
