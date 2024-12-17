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

    private EditText etHolidayStartDate, etEndDate, etReasonForLeave, etAdditionalComments;
    private String employeeEmail;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_holiday_request);

        // Retrieve the logged-in employee's email from the intent
        employeeEmail = getIntent().getStringExtra("EMPLOYEE_EMAIL");

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Initialize views
        initializeUI();

        // Set up back button functionality
        ImageView btnBack = findViewById(R.id.btnBack2);
        btnBack.setOnClickListener(v -> navigateBackToDashboard());

        // Set up date pickers for Beginning Date and End Date
        etHolidayStartDate.setOnClickListener(v -> showDatePickerDialog(etHolidayStartDate));
        etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate));
    }

    /**
     * Initialize UI components.
     */
    private void initializeUI() {
        etHolidayStartDate = findViewById(R.id.etHolidayStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etReasonForLeave = findViewById(R.id.etReasonForLeave);
        etAdditionalComments = findViewById(R.id.etAdditionalComments);
        Button btnSubmitRequest = findViewById(R.id.btnSubmitRequest);

        // Submit request button functionality
        btnSubmitRequest.setOnClickListener(v -> submitHolidayRequest());
    }

    /**
     * Handle the submission of the holiday request.
     */
    private void submitHolidayRequest() {
        String reasonForLeave = etReasonForLeave.getText().toString().trim();
        String holidayStartDate = etHolidayStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();
        String additionalComments = etAdditionalComments.getText().toString().trim();

        if (reasonForLeave.isEmpty() || holidayStartDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate that end date is not before the beginning date
        if (!isEndDateValid(holidayStartDate, endDate)) {
            Toast.makeText(this, "End date cannot be before the beginning date.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a HolidayRequest object
        HolidayRequest holidayRequest = new HolidayRequest(
                0, // Auto-increment ID
                employeeEmail,
                holidayStartDate,
                endDate,
                reasonForLeave,
                additionalComments,
                "PENDING"
        );

        // Save the holiday request to the database
        long result = databaseHelper.addHolidayRequest(holidayRequest);
        if (result != -1) {
            Toast.makeText(this, "Holiday request submitted successfully.", Toast.LENGTH_SHORT).show();
            navigateToConfirmationScreen();
        } else {
            Toast.makeText(this, "Error submitting holiday request.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Navigate back to the Employee Dashboard and pass the employee email.
     */
    private void navigateBackToDashboard() {
        Intent intent = new Intent(EmployeeHolidayRequestActivity.this, EmployeeDashboardActivity.class);
        intent.putExtra("EMPLOYEE_EMAIL", employeeEmail);
        startActivity(intent);
        finish();
    }

    /**
     * Navigate to the confirmation screen.
     */
    private void navigateToConfirmationScreen() {
        Intent intent = new Intent(EmployeeHolidayRequestActivity.this, EmployeeHolidayConfirmActivity.class);
        intent.putExtra("EMPLOYEE_EMAIL", employeeEmail);
        startActivity(intent);
        finish();
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
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editText.setText(selectedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    /**
     * Validates that the end date is not before the beginning date.
     *
     * @param beginningDate The beginning date in DD/MM/YYYY format.
     * @param endDate       The end date in DD/MM/YYYY format.
     * @return True if the end date is valid, otherwise false.
     */
    private boolean isEndDateValid(String beginningDate, String endDate) {
        try {
            String[] start = beginningDate.split("/");
            String[] end = endDate.split("/");

            Calendar startCal = Calendar.getInstance();
            startCal.set(Integer.parseInt(start[2]), Integer.parseInt(start[1]) - 1, Integer.parseInt(start[0]));

            Calendar endCal = Calendar.getInstance();
            endCal.set(Integer.parseInt(end[2]), Integer.parseInt(end[1]) - 1, Integer.parseInt(end[0]));

            return !endCal.before(startCal);
        } catch (Exception e) {
            return false;
        }
    }
}
