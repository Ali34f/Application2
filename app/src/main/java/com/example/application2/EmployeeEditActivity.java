package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeEditActivity extends AppCompatActivity {

    private EditText nameField, positionField, idField, phoneField, emailField;
    private TextView employeeNameTextView; // TextView to display the employee's name under the profile picture
    private DatabaseHelper dbHelper;
    private String employeeEmail;
    private Employee loggedInEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit);

        // Initialize fields and Database Helper
        initializeFields();
        dbHelper = new DatabaseHelper(this);

        // Retrieve the logged-in employee's email from the intent
        employeeEmail = getIntent().getStringExtra("EMPLOYEE_EMAIL");

        // Load existing details from the database
        loadEmployeeDetails();

        // Back button functionality
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> navigateToProfile());

        // Confirm edits button functionality
        Button btnConfirmEdits = findViewById(R.id.btnConfirmEdits);
        btnConfirmEdits.setOnClickListener(v -> {
            if (saveUpdatedDetails()) {
                navigateToConfirmActivity();
            }
        });

        // Cancel edits button functionality
        Button btnCancelEdits = findViewById(R.id.btnCancelEdits);
        btnCancelEdits.setOnClickListener(v -> navigateToProfile());
    }

    /**
     * Initialize the fields from the layout.
     */
    private void initializeFields() {
        nameField = findViewById(R.id.editTextName);
        positionField = findViewById(R.id.editTextPosition);
        idField = findViewById(R.id.editTextId);
        phoneField = findViewById(R.id.editTextPhoneNumber);
        emailField = findViewById(R.id.editTextEmailAddress);
        employeeNameTextView = findViewById(R.id.employeeName); // For displaying the employee's name
    }

    /**
     * Load the employee's existing details from the database.
     */
    private void loadEmployeeDetails() {
        if (employeeEmail != null) {
            loggedInEmployee = dbHelper.getEmployeeByEmail(employeeEmail);

            if (loggedInEmployee != null) {
                // Set fields with existing details
                nameField.setText(loggedInEmployee.getName());
                positionField.setText(loggedInEmployee.getPosition());
                idField.setText(String.valueOf(loggedInEmployee.getId()));
                phoneField.setText(loggedInEmployee.getPhone());
                emailField.setText(loggedInEmployee.getEmail());

                // Set employee's name under the profile picture
                employeeNameTextView.setText(loggedInEmployee.getName());
            } else {
                Toast.makeText(this, "Error: Employee details not found.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Error: Missing employee email.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Save the updated employee details to the database.
     *
     * @return true if the update is successful, false otherwise
     */
    private boolean saveUpdatedDetails() {
        String name = nameField.getText().toString().trim();
        String position = positionField.getText().toString().trim();
        String id = idField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String email = emailField.getText().toString().trim();

        if (validateInputs(name, position, id, phone, email)) {
            // Create an updated Employee object
            Employee updatedEmployee = new Employee(
                    Integer.parseInt(id),
                    name,
                    position,
                    email,
                    phone,
                    loggedInEmployee.getSalary(),    // Keep the existing salary
                    loggedInEmployee.getStartDate(), // Keep the existing start date
                    loggedInEmployee.getPassword()   // Keep the existing password
            );

            boolean isSuccess = dbHelper.updateEmployeeProfile(updatedEmployee);

            if (isSuccess) {
                Toast.makeText(this, "Details updated successfully!", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(this, "Failed to update details. Please try again.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Validate inputs to ensure all fields are filled.
     */
    private boolean validateInputs(String name, String position, String id, String phone, String email) {
        return !name.isEmpty() && !position.isEmpty() && !id.isEmpty() && !phone.isEmpty() && !email.isEmpty();
    }

    /**
     * Navigate back to the EmployeeProfileActivity.
     */
    private void navigateToProfile() {
        Intent intent = new Intent(this, EmployeeProfileActivity.class);
        intent.putExtra("EMPLOYEE_EMAIL", employeeEmail);
        startActivity(intent);
        finish();
    }

    /**
     * Navigate to the EmployeeEditConfirmActivity.
     */
    private void navigateToConfirmActivity() {
        Intent intent = new Intent(this, EmployeeEditConfirmActivity.class);
        intent.putExtra("EMPLOYEE_EMAIL", employeeEmail);
        startActivity(intent);
        finish();
    }
}
