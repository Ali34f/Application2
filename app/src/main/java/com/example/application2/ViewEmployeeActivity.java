package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ViewEmployeeActivity extends AppCompatActivity {

    private TextView tvEmployeeName, tvEmployeePosition, tvEmployeeId, tvEmployeeContactInfo, tvEmployeeSalary, tvEmployeeStartDate;
    private Button btnEdit, btnDelete;
    private ImageView backButton;
    private DatabaseHelper db;
    private int employeeId;
    private Employee currentEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);

        // Initialize views
        tvEmployeeName = findViewById(R.id.employeeName);
        tvEmployeePosition = findViewById(R.id.employeePosition);
        tvEmployeeId = findViewById(R.id.employeeid);
        tvEmployeeContactInfo = findViewById(R.id.employeeContactInfo);
        tvEmployeeSalary = findViewById(R.id.employeeSalary);
        tvEmployeeStartDate = findViewById(R.id.employeeStartDate);
        btnEdit = findViewById(R.id.editButton);
        btnDelete = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);

        // Initialize the database helper
        db = new DatabaseHelper(this);

        // Get the employee ID from the intent
        employeeId = getIntent().getIntExtra("EMPLOYEE_ID", -1);

        if (employeeId != -1) {
            loadEmployeeData(employeeId);
        } else {
            Toast.makeText(this, "Error loading employee data", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Back button action
        backButton.setOnClickListener(v -> finish());

        // Edit button action
        btnEdit.setOnClickListener(v -> navigateToEditEmployee());

        // Delete button action
        btnDelete.setOnClickListener(v -> confirmDeleteEmployee());
    }

    // Load employee data from the database
    private void loadEmployeeData(int id) {
        currentEmployee = db.getEmployeeById(id);

        if (currentEmployee != null) {
            tvEmployeeName.setText(currentEmployee.getName());
            tvEmployeePosition.setText(currentEmployee.getPosition());
            tvEmployeeId.setText("ID: " + currentEmployee.getId());
            tvEmployeeContactInfo.setText("Email: " + currentEmployee.getEmail() + "\nPhone: " + currentEmployee.getPhone());
            tvEmployeeSalary.setText("Salary: $" + currentEmployee.getSalary());
            tvEmployeeStartDate.setText("Start Date: " + currentEmployee.getStartDate());
        } else {
            Toast.makeText(this, "Employee not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // Navigate to EditEmployeeActivity
    private void navigateToEditEmployee() {
        Intent intent = new Intent(ViewEmployeeActivity.this, EditEmployeeActivity.class);
        intent.putExtra("EMPLOYEE_ID", employeeId);
        startActivity(intent);
    }

    // Confirm delete employee
    private void confirmDeleteEmployee() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Employee");
        builder.setMessage("Are you sure you want to delete this employee?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            int rowsDeleted = db.deleteEmployee(employeeId);
            if (rowsDeleted > 0) {
                Toast.makeText(ViewEmployeeActivity.this, "Employee deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ViewEmployeeActivity.this, "Error deleting employee", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEmployeeData(employeeId);
    }
}
