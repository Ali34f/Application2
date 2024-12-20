package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application2.model.Employee;
import com.example.application2.repository.EmployeeRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity to view, edit, and delete an employee's details.
 */
public class ViewEmployeeActivity extends AppCompatActivity {

    private TextView tvEmployeeName, tvEmployeePosition, tvEmployeeId, tvEmployeeContactInfo, tvEmployeeSalary, tvEmployeeStartDate;
    private Button btnEdit, btnDelete;
    private ImageView backButton;
    private int employeeId;
    private EmployeeRepository employeeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);

        // Initialize views
        initializeViews();

        // Initialize EmployeeRepository
        employeeRepository = new EmployeeRepository();

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
     * Load employee data from the API and populate views.
     *
     * @param id The ID of the employee to load.
     */
    private void loadEmployeeData(int id) {
        employeeRepository.getEmployeeById(id, new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateEmployeeDetails(response.body());
                } else {
                    showToast("Employee not found or failed to load from API");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                showToast("API Error: " + t.getMessage());
                finish();
            }
        });
    }

    /**
     * Populate the views with the employee details.
     *
     * @param employee The Employee object containing the details.
     */
    private void populateEmployeeDetails(Employee employee) {
        tvEmployeeName.setText(employee.getName());
        tvEmployeePosition.setText(employee.getPosition());
        tvEmployeeId.setText(String.format("ID: %d", employee.getId()));
        tvEmployeeContactInfo.setText(String.format("Email: %s\nPhone: %s", employee.getEmail(), employee.getPhone()));
        tvEmployeeSalary.setText(String.format("Salary: $%.2f", employee.getSalary()));
        tvEmployeeStartDate.setText(String.format("Start Date: %s", employee.getStartDate()));
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
