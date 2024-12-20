package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application2.model.Employee;
import com.example.application2.model.EmployeeApiModel;
import com.example.application2.repository.EmployeeRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for editing an employee's details.
 */
public class EditEmployeeActivity extends AppCompatActivity {

    private EditText editName, editPosition, editPhoneNumber, editEmailAddress, editLeaves;
    private Button btnCancel, btnConfirm;
    private ImageView btnBack;
    private int employeeId;
    private EmployeeRepository employeeRepository;
    private EmployeeApiModel currentEmployeeApiModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        // Initialize views
        initializeViews();

        // Initialize EmployeeRepository
        employeeRepository = new EmployeeRepository();

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
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editEmailAddress = findViewById(R.id.editEmailAddress);
        editLeaves = findViewById(R.id.editLeaves);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);
    }

    /**
     * Set button click actions for back, cancel, and confirm.
     */
    private void setButtonActions() {
        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
        btnConfirm.setOnClickListener(v -> updateEmployee());
    }

    /**
     * Load employee details from the API and populate form fields.
     *
     * @param id The ID of the employee to load.
     */
    private void loadEmployeeDetails(int id) {
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
     * Populate the form fields with the employee's details.
     *
     * @param employee The Employee object containing the details.
     */
    private void populateEmployeeDetails(Employee employee) {
        editName.setText(employee.getName());
        editPosition.setText(employee.getPosition());
        editPhoneNumber.setText(employee.getPhone());
        editEmailAddress.setText(employee.getEmail());
        editLeaves.setText(String.valueOf(employee.getLeaves()));
    }

    /**
     * Update the employee's details using the API.
     */
    private void updateEmployee() {
        String name = editName.getText().toString().trim();
        String position = editPosition.getText().toString().trim();
        String phone = editPhoneNumber.getText().toString().trim();
        String email = editEmailAddress.getText().toString().trim();
        String leavesText = editLeaves.getText().toString().trim();

        if (name.isEmpty() || position.isEmpty() || phone.isEmpty() || email.isEmpty() || leavesText.isEmpty()) {
            showToast("Please fill in all fields");
            return;
        }

        int leaves;
        try {
            leaves = Integer.parseInt(leavesText);
        } catch (NumberFormatException e) {
            showToast("Invalid number of leaves");
            return;
        }

        // Create an updated EmployeeApiModel object
        EmployeeApiModel updatedEmployee = new EmployeeApiModel(
                name.split(" ")[0],                    // First name
                name.contains(" ") ? name.split(" ")[1] : "",  // Last name
                email,                                 // Email
                position,                              // Position (department)
                0.0,                                   // Placeholder salary (could be updated separately)
                "",                                    // Placeholder joining date
                leaves                                 // Leaves
        );

        // Call API to update employee
        employeeRepository.updateEmployee(employeeId, updatedEmployee, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showToast("Employee updated successfully");
                    navigateToEditEmployeeConfirm();
                } else {
                    showToast("Failed to update employee");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("API Error: " + t.getMessage());
            }
        });
    }

    /**
     * Navigate to EditEmployeeConfirmActivity after a successful update.
     */
    private void navigateToEditEmployeeConfirm() {
        Intent intent = new Intent(EditEmployeeActivity.this, EditEmployeeConfirmActivity.class);
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
