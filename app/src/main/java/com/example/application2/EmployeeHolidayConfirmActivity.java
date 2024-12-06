package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EmployeeHolidayConfirmActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_holiday_confirm);

        // Done button functionality
        Button btnDone = findViewById(R.id.btnDone4);
        btnDone.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeHolidayConfirmActivity.this, EmployeeDashboardActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
