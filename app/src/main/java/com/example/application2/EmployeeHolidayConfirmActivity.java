package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeHolidayConfirmActivity extends AppCompatActivity {

    private String employeeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_holiday_confirm);

        // Retrieve the logged-in employee's email from the intent
        employeeEmail = getIntent().getStringExtra("EMPLOYEE_EMAIL");

        // Initialize Done button
        Button btnDone = findViewById(R.id.btnDone4);

        // Done button functionality
        btnDone.setOnClickListener(v -> navigateToDashboard());
    }

    /**
     * Navigate back to the Employee Dashboard .
     */
    private void navigateToDashboard() {
        Intent intent = new Intent(EmployeeHolidayConfirmActivity.this, EmployeeDashboardActivity.class);
        intent.putExtra("EMPLOYEE_EMAIL", employeeEmail);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
