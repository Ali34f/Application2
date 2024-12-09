package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class EmployeeManagementActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnAddEmployee, btnSortFilter;
    private EditText etSearchEmployee;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_management);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        btnAddEmployee = findViewById(R.id.btnAddEmployee);
        btnSortFilter = findViewById(R.id.btnSortFilter);
        etSearchEmployee = findViewById(R.id.etSearchEmployee);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Back button functionality
        btnBack.setOnClickListener(v -> navigateToDashboard());

        // Add Employee button functionality
        btnAddEmployee.setOnClickListener(v -> navigateToAddEmployee());

        // Sort/Filter button functionality
        btnSortFilter.setOnClickListener(v -> displayAllEmployees());

        // Search functionality
        etSearchEmployee.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    // Navigate back to DashboardActivity
    private void navigateToDashboard() {
        Intent intent = new Intent(EmployeeManagementActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    // Navigate to NewEmployeeDetailsActivity
    private void navigateToAddEmployee() {
        Intent intent = new Intent(EmployeeManagementActivity.this, NewEmployeeDetailsActivity.class);
        startActivity(intent);
    }

    // Display all employees in the console (can be modified to show in UI)
    private void displayAllEmployees() {
        List<Employee> employees = dbHelper.getAllEmployees();
        if (employees.isEmpty()) {
            Toast.makeText(this, "No employees found", Toast.LENGTH_SHORT).show();
        } else {
            for (Employee employee : employees) {
                System.out.println(employee.getName());
            }
            Toast.makeText(this, employees.size() + " employees found", Toast.LENGTH_SHORT).show();
        }
    }

    // Perform search and display results
    private void performSearch() {
        String query = etSearchEmployee.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Employee> filteredEmployees = dbHelper.searchEmployees(query);
        if (filteredEmployees.isEmpty()) {
            Toast.makeText(this, "No matching employees found", Toast.LENGTH_SHORT).show();
        } else {
            for (Employee employee : filteredEmployees) {
                System.out.println(employee.getName());
            }
            Toast.makeText(this, filteredEmployees.size() + " employees found", Toast.LENGTH_SHORT).show();
        }
    }
}
