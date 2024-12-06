package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeRestActivity extends AppCompatActivity {

    private EditText newPasswordField;
    private EditText confirmPasswordField;
    private Button saveChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_reset);

        // Initialize UI elements
        newPasswordField = findViewById(R.id.txtNewPasswordField);
        confirmPasswordField = findViewById(R.id.passwordConfirmField);
        saveChangesButton = findViewById(R.id.btnChangSave);

        // Set save changes button click listener
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordField.getText().toString().trim();
                String confirmPassword = confirmPasswordField.getText().toString().trim();

                if (validatePasswords(newPassword, confirmPassword)) {
                    // Navigate to EmployeeRestConfirmActivity
                    Intent intent = new Intent(EmployeeRestActivity.this, EmployeeRestConfirmActivity.class);
                    startActivity(intent);
                } else {
                    // Show an error message
                    Toast.makeText(EmployeeRestActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Validate new and confirm password fields.
     */
    private boolean validatePasswords(String newPassword, String confirmPassword) {
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
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
}
