package com.example.application2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeEditActivity extends AppCompatActivity {

    private EditText nameField, positionField, idField, phoneField, emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit);

        // Initialize fields
        nameField = findViewById(R.id.editTextName);
        positionField = findViewById(R.id.editTextPosition);
        idField = findViewById(R.id.editTextId);
        phoneField = findViewById(R.id.editTextPhoneNumber);
        emailField = findViewById(R.id.editTextEmailAddress);

        // Load existing details from SharedPreferences
        loadExistingDetails();

        // Back button functionality
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeEditActivity.this, EmployeeProfileActivity.class);
            startActivity(intent);
            finish();
        });

        // Confirm edits button functionality
        Button btnConfirmEdits = findViewById(R.id.btnConfirmEdits);
        btnConfirmEdits.setOnClickListener(v -> {
            if (saveUpdatedDetails()) {
                // Navigate to EmployeeEditConfirmActivity
                Intent intent = new Intent(EmployeeEditActivity.this, EmployeeEditConfirmActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Cancel edits button functionality
        Button btnCancelEdits = findViewById(R.id.btnCancelEdits);
        btnCancelEdits.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeEditActivity.this, EmployeeProfileActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Load existing details from SharedPreferences into fields.
     */
    private void loadExistingDetails() {
        SharedPreferences prefs = getSharedPreferences("EmployeeDetails", MODE_PRIVATE);
        nameField.setText(prefs.getString("Name", getString(R.string.michael_brown)));
        positionField.setText(prefs.getString("Position", getString(R.string.project_manager)));
        idField.setText(prefs.getString("ID Number", getString(R.string.id_number)));  // Updated to use ID field
        phoneField.setText(prefs.getString("Phone", getString(R.string.number4)));
        emailField.setText(prefs.getString("Email", getString(R.string.michaelbrown_company_com)));
    }

    /**
     * Save updated details into SharedPreferences and validate inputs.
     *
     * @return boolean indicating success or failure
     */
    private boolean saveUpdatedDetails() {
        String name = nameField.getText().toString().trim();
        String position = positionField.getText().toString().trim();
        String id = idField.getText().toString().trim();
        String phone = phoneField.getText().toString().trim();
        String email = emailField.getText().toString().trim();

        if (validateInputs(name, position, id, phone, email)) {
            SharedPreferences.Editor editor = getSharedPreferences("EmployeeDetails", MODE_PRIVATE).edit();
            editor.putString("Name", name);
            editor.putString("Position", position);
            editor.putString("ID Number", id);  // Save the ID number
            editor.putString("Phone", phone);
            editor.putString("Email", email);
            editor.apply();

            Toast.makeText(this, "Details updated successfully!", Toast.LENGTH_SHORT).show();
            return true;
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
}
