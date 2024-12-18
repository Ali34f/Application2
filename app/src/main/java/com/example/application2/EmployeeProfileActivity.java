package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity to display the employee's profile details.
 */
public class EmployeeProfileActivity extends AppCompatActivity {

    private EditText nameField, positionField, idField, phoneField, emailField, leavesField;
    private TextView employeeNameTextView;
    private ImageView btnBack;
    private Button btnEditDetails;
    private DatabaseHelper dbHelper;
    private String employeeEmail;
    private Employee loggedInEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        // Initialize UI elements
        initializeUI();

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Get the logged-in employee's email from the intent
        employeeEmail = getIntent().getStringExtra("EMPLOYEE_EMAIL");

        // Load and display employee details
        loadEmployeeDetails();

        // Set up button listeners
        setupButtonListeners();
    }

    /**
     * Initializes UI components for the activity.
     */
    private void initializeUI() {
        nameField = findViewById(R.id.editTextName);
        positionField = findViewById(R.id.editTextPosition);
        idField = findViewById(R.id.editTextId);
        phoneField = findViewById(R.id.editTextPhoneNumber);
        emailField = findViewById(R.id.editTextEmailAddress);
        leavesField = findViewById(R.id.editTextLeaves); // Added leaves field
        employeeNameTextView = findViewById(R.id.employeeName);
        btnBack = findViewById(R.id.btnBack);
        btnEditDetails = findViewById(R.id.btnEditDetails);

        // Disable editing for all fields
        disableEditing();
    }

    /**
     * Disables editing for all profile fields.
     */
    private void disableEditing() {
        nameField.setEnabled(false);
        positionField.setEnabled(false);
        idField.setEnabled(false);
        phoneField.setEnabled(false);
        emailField.setEnabled(false);
        leavesField.setEnabled(false); // Disable leaves field editing
    }

    /**
     * Loads employee details from the database and displays them.
     */
    private void loadEmployeeDetails() {
        if (employeeEmail == null) {
            Toast.makeText(this, "No employee email provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch employee data from the database
        loggedInEmployee = dbHelper.getEmployeeByEmail(employeeEmail);

        if (loggedInEmployee != null) {
            // Populate the fields with employee details
            nameField.setText(loggedInEmployee.getName());
            positionField.setText(loggedInEmployee.getPosition());
            idField.setText(String.valueOf(loggedInEmployee.getId()));
            phoneField.setText(loggedInEmployee.getPhone());
            emailField.setText(loggedInEmployee.getEmail());
            leavesField.setText(String.valueOf(loggedInEmployee.getLeaves())); // Set leaves field

            // Set the employee name under the profile picture
            employeeNameTextView.setText(loggedInEmployee.getName());
        } else {
            Toast.makeText(this, "Employee details not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Sets up click listeners for buttons.
     */
    private void setupButtonListeners() {
        // Back button
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeProfileActivity.this, EmployeeDashboardActivity.class);
            intent.putExtra("EMPLOYEE_EMAIL", employeeEmail);
            startActivity(intent);
            finish();
        });

        // Edit details button
        btnEditDetails.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeProfileActivity.this, EmployeeEditActivity.class);
            intent.putExtra("EMPLOYEE_EMAIL", employeeEmail);
            startActivity(intent);
        });
    }
}
