package com.example.application2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class NewEmployeeDetailsActivity extends AppCompatActivity {

    private EditText etName, etPosition, etEmployeeID, etPhoneNumber, etEmailAddress, etSalary, etStartDate;
    private Button btnCancel, btnConfirm;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_employee);

        // Initialize views
        etName = findViewById(R.id.etName);
        etPosition = findViewById(R.id.etPosition);
        etEmployeeID = findViewById(R.id.etid);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etSalary = findViewById(R.id.etSalary);
        etStartDate = findViewById(R.id.etStartDate);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);

        dbHelper = new DatabaseHelper(this);

        // Start Date Picker
        etStartDate.setOnClickListener(v -> showDatePicker());

        // Confirm Button Click Listener
        btnConfirm.setOnClickListener(v -> saveEmployee());

        // Cancel Button Click Listener
        btnCancel.setOnClickListener(v -> finish());
    }

    // Show Date Picker Dialog
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, day1) -> {
            String selectedDate = year1 + "-" + (month1 + 1) + "-" + day1;
            etStartDate.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    // Save Employee Data
    private void saveEmployee() {
        String name = etName.getText().toString().trim();
        String position = etPosition.getText().toString().trim();
        String employeeID = etEmployeeID.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String email = etEmailAddress.getText().toString().trim();
        String salaryText = etSalary.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();

        if (name.isEmpty() || position.isEmpty() || employeeID.isEmpty() || phoneNumber.isEmpty() ||
                email.isEmpty() || salaryText.isEmpty() || startDate.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid salary format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new Employee object (ID is auto-generated)
        Employee employee = new Employee(name, position, email, phoneNumber, salary, startDate);

        // Add employee to the database
        long result = dbHelper.addEmployee(employee);

        if (result != -1) {
            Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error adding employee", Toast.LENGTH_SHORT).show();
        }
    }
}
