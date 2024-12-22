package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeRestActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText newPasswordField;
    private EditText confirmPasswordField;
    private Button saveChangesButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_reset);

        // Initialize UI elements
        emailField = findViewById(R.id.txtEmailField);
        newPasswordField = findViewById(R.id.txtNewPasswordField);
        confirmPasswordField = findViewById(R.id.passwordConfirmField);
        saveChangesButton = findViewById(R.id.btnChangSave);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Set save changes button click listener
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String newPassword = newPasswordField.getText().toString().trim();
                String confirmPassword = confirmPasswordField.getText().toString().trim();

                if (validateInputs(email, newPassword, confirmPassword)) {
                    resetPassword(email, newPassword);
                }
            }
        });
    }

    /**
     * Validate inputs for resetting the password.
     */
    private boolean validateInputs(String email, String newPassword, String confirmPassword) {
        if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Reset the password for the specified email.
     */
    private void resetPassword(String email, String newPassword) {
        boolean isUpdated = dbHelper.updatePasswordByEmail(email, newPassword);

        if (isUpdated) {
            // Navigate to EmployeeRestConfirmActivity
            Intent intent = new Intent(EmployeeRestActivity.this, EmployeeRestConfirmActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error: Email not found", Toast.LENGTH_SHORT).show();
        }
    }
}
