package com.example.application2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EmployeeHolidayRequestActivity extends AppCompatActivity {

    private EditText etStartDate, etEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_holiday_request);

        // Back button functionality
        ImageView btnBack = findViewById(R.id.btnBack2);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeHolidayRequestActivity.this, EmployeeDashboardActivity.class);
            startActivity(intent);
            finish();
        });

        // Initialize views
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        EditText etReasonForLeave = findViewById(R.id.etReasonForLeave);
        EditText etAdditionalComments = findViewById(R.id.etAdditionalComments);
        Button btnSubmitRequest = findViewById(R.id.btnSubmitRequest);

        // Set up date picker for Start Date
        etStartDate.setOnClickListener(v -> showDatePickerDialog(etStartDate));

        // Set up date picker for End Date
        etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate));

        // Enter Details functionality
        btnSubmitRequest.setOnClickListener(v -> {
            String reasonForLeave = etReasonForLeave.getText().toString().trim();
            String startDate = etStartDate.getText().toString().trim();
            String endDate = etEndDate.getText().toString().trim();
            String additionalComments = etAdditionalComments.getText().toString().trim();

            if (reasonForLeave.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            } else {
                // Simulate saving data and navigate to the confirmation screen
                Intent intent = new Intent(EmployeeHolidayRequestActivity.this, EmployeeHolidayConfirmActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Shows a DatePickerDialog for selecting a date.
     *
     * @param editText The EditText to set the selected date.
     */
    private void showDatePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Format the selected date and set it to the EditText
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    editText.setText(selectedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }
}
