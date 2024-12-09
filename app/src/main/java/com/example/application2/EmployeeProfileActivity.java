package com.example.application2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeProfileActivity extends AppCompatActivity {

    private EditText nameField, positionField, idField, phoneField, emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        // Initialize UI elements
        initializeUI();

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
    }

    /**
     * Loads employee details from SharedPreferences and displays them.
     */
    private void loadEmployeeDetails() {
        SharedPreferences prefs = getSharedPreferences("EmployeeDetails", MODE_PRIVATE);

        String name = prefs.getString("Name", getString(R.string.michael_brown)); // Default name
        String position = prefs.getString("Position", getString(R.string.project_manager)); // Default position
        String id = prefs.getString("ID Number", getString(R.string.id_number)); // Default ID
        String phone = prefs.getString("Phone", getString(R.string.number4)); // Default phone
        String email = prefs.getString("Email", getString(R.string.michaelbrown_company_com)); // Default email

        // Set values to fields
        nameField.setText(name);
        positionField.setText(position);
        idField.setText(id);
        phoneField.setText(phone);
        emailField.setText(email);
    }

    /**
     * Sets up click listeners for buttons.
     */
    private void setupButtonListeners() {
        // Back button
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeProfileActivity.this, EmployeeDashboardActivity.class);
            startActivity(intent);
            finish();
        });

        // Edit details button
        Button btnEditDetails = findViewById(R.id.btnEditDetails);
        btnEditDetails.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeProfileActivity.this, EmployeeEditActivity.class);
            startActivity(intent);
        });
    }
}
