package com.example.application2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditEmployeeActivity extends Activity {

    private EditText editName, editPosition, editID, editPhoneNumber, editEmailAddress;
    private Button btnCancel, btnConfirm;
    private ImageView btnBack;
    private DatabaseHelper dbHelper;
    private int employeeId; // The ID of the employee to be edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        // Initialize views
        initializeViews();

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Get employee details from the Intent
        employeeId = getIntent().getIntExtra("EMPLOYEE_ID", -1);

        if (employeeId != -1) {
            loadEmployeeDetails(employeeId);
        } else {
            showToast("Invalid employee ID");
            finish();
        }

        // Set button click listeners
        setButtonActions();
    }

    /**
     * Initialize views from the layout.
     */
    private void initializeViews() {
        editName = findViewById(R.id.editName);
        editPosition = findViewById(R.id.editPosition);
        editID = findViewById(R.id.editID);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editEmailAddress = findViewById(R.id.editEmailAddress);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);
    }

    /**
     * Set button click actions for back, cancel, and confirm.
     */
    private void setButtonActions() {
        // Back button action
        btnBack.setOnClickListener(v -> navigateBackToViewEmployee());

        // Cancel button action
        btnCancel.setOnClickListener(v -> finish());

        // Confirm button action
        btnConfirm.setOnClickListener(v -> updateEmployee());
    }

    /**
     * Load employee details into the form fields.
     *
     * @param id The ID of the employee to load.
     */
    private void loadEmployeeDetails(int id) {
        Employee employee = dbHelper.getEmployeeById(id);
        if (employee != null) {
            editName.setText(employee.getName());
            editPosition.setText(employee.getPosition());
            editID.setText(String.valueOf(employee.getId()));
            editPhoneNumber.setText(employee.getPhone());
            editEmailAddress.setText(employee.getEmail());
        } else {
            showToast("Employee not found");
            finish();
        }
    }

    /**
     * Update the employee's details in the database.
     */
    private void updateEmployee() {
        String name = editName.getText().toString().trim();
        String position = editPosition.getText().toString().trim();
        String phone = editPhoneNumber.getText().toString().trim();
        String email = editEmailAddress.getText().toString().trim();

        if (name.isEmpty() || position.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            showToast("Please fill in all fields");
            return;
        }

        Employee updatedEmployee = new Employee(employeeId, name, position, email, phone, 0.0, "");

        int rowsAffected = dbHelper.updateEmployee(updatedEmployee);

        if (rowsAffected > 0) {
            showToast("Employee updated successfully");
            Intent intent = new Intent(this, ViewEmployeeActivity.class);
            intent.putExtra("EMPLOYEE_ID", employeeId);
            startActivity(intent);
            finish();
        } else {
            showToast("Error updating employee");
        }
    }

    /**
     * Navigate back to the ViewEmployeeActivity.
     */
    private void navigateBackToViewEmployee() {
        Intent intent = new Intent(this, ViewEmployeeActivity.class);
        intent.putExtra("EMPLOYEE_ID", employeeId);
        startActivity(intent);
        finish();
    }

    /**
     * Utility method to show toast messages.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
