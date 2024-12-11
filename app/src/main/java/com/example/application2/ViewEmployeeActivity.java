package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity to view, edit, and delete an employee's details.
 */
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
        initializeViews();

        // Initialize database helper
        db = new DatabaseHelper(this);

        // Get the employee ID from the intent
        employeeId = getIntent().getIntExtra("EMPLOYEE_ID", -1);

        if (employeeId != -1) {
            loadEmployeeData(employeeId);
        } else {
            showToast("Error loading employee data");
            finish();
        }

        // Set button actions
        setButtonActions();
    }

    /**
     * Initialize views from the layout.
     */
    private void initializeViews() {
        tvEmployeeName = findViewById(R.id.employeeName);
        tvEmployeePosition = findViewById(R.id.employeePosition);
        tvEmployeeId = findViewById(R.id.employeeid);
        tvEmployeeContactInfo = findViewById(R.id.employeeContactInfo);
        tvEmployeeSalary = findViewById(R.id.employeeSalary);
        tvEmployeeStartDate = findViewById(R.id.employeeStartDate);
        btnEdit = findViewById(R.id.editButton);
        btnDelete = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);
    }

    /**
     * Set button click actions for back, edit, and delete.
     */
    private void setButtonActions() {
        // Back button action
        backButton.setOnClickListener(v -> finish());

        // Edit button action
        btnEdit.setOnClickListener(v -> navigateToEditEmployee());

        // Delete button action
        btnDelete.setOnClickListener(v -> navigateToConfirmDeleteEmployee());
    }

    /**
     * Load employee data from the database and populate views.
     *
     * @param id The ID of the employee to load.
     */
    private void loadEmployeeData(int id) {
        currentEmployee = db.getEmployeeById(id);

        if (currentEmployee != null) {
            tvEmployeeName.setText(currentEmployee.getName());
            tvEmployeePosition.setText(currentEmployee.getPosition());
            tvEmployeeId.setText(String.format("ID: %d", currentEmployee.getId()));
            tvEmployeeContactInfo.setText(String.format("Email: %s\nPhone: %s", currentEmployee.getEmail(), currentEmployee.getPhone()));
            tvEmployeeSalary.setText(String.format("Salary: $%.2f", currentEmployee.getSalary()));
            tvEmployeeStartDate.setText(String.format("Start Date: %s", currentEmployee.getStartDate()));
        } else {
            showToast("Employee not found");
            finish();
        }
    }

    /**
     * Navigate to ConfirmDeleteEmployeeActivity for delete confirmation.
     */
    private void navigateToConfirmDeleteEmployee() {
        Intent intent = new Intent(this, ConfirmDeleteEmployeeActivity.class);
        intent.putExtra("EMPLOYEE_ID", employeeId);
        startActivity(intent);
    }

    /**
     * Navigate to EditEmployeeActivity to edit the current employee's details.
     */
    private void navigateToEditEmployee() {
        Intent intent = new Intent(this, EditEmployeeActivity.class);
        intent.putExtra("EMPLOYEE_ID", employeeId);
        startActivity(intent);
    }

    /**
     * Utility method to show toast messages.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh employee data when returning to this activity
        loadEmployeeData(employeeId);
    }
}
