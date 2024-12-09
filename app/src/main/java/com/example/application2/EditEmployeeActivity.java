package com.example.application2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditEmployeeActivity extends Activity {

    private EditText editName, editPosition, editID, editPhoneNumber, editEmailAddress;
    private Button btnCancel, btnConfirm;
    private DatabaseHelper dbHelper;
    private int employeeId; // The ID of the employee to be edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        // Initialize views
        editName = findViewById(R.id.editName);
        editPosition = findViewById(R.id.editPosition);
        editID = findViewById(R.id.editID);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editEmailAddress = findViewById(R.id.editEmailAddress);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Get employee details from the Intent
        employeeId = getIntent().getIntExtra("EMPLOYEE_ID", -1);

        if (employeeId != -1) {
            loadEmployeeDetails(employeeId);
        } else {
            Toast.makeText(this, "Invalid employee ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Cancel button click listener
        btnCancel.setOnClickListener(v -> finish());

        // Confirm button click listener
        btnConfirm.setOnClickListener(v -> updateEmployee());
    }

    private void loadEmployeeDetails(int id) {
        Employee employee = dbHelper.getEmployeeById(id);
        if (employee != null) {
            editName.setText(employee.getName());
            editPosition.setText(employee.getPosition());
            editID.setText(String.valueOf(employee.getId()));
            editPhoneNumber.setText(employee.getPhone());
            editEmailAddress.setText(employee.getEmail());
        } else {
            Toast.makeText(this, "Employee not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateEmployee() {
        String name = editName.getText().toString().trim();
        String position = editPosition.getText().toString().trim();
        String phone = editPhoneNumber.getText().toString().trim();
        String email = editEmailAddress.getText().toString().trim();

        if (name.isEmpty() || position.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Employee updatedEmployee = new Employee(employeeId, name, position, email, phone, 0.0, "");

        int rowsAffected = dbHelper.updateEmployee(updatedEmployee);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Employee updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, EditEmployeeConfirmActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error updating employee", Toast.LENGTH_SHORT).show();
        }
    }
}
