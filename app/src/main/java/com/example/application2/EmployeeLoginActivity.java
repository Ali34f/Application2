package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application2.model.Employee;

/**
 * Activity for employee login.
 */
public class EmployeeLoginActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Button signInButton;
    private TextView forgotPasswordText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);

        // Initialize UI elements
        initializeViews();

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Set sign-in button click listener
        signInButton.setOnClickListener(v -> attemptLogin());

        // Set forgot password click listener
        forgotPasswordText.setOnClickListener(v -> navigateToResetPassword());
    }

    /**
     * Initialize views from the layout.
     */
    private void initializeViews() {
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        signInButton = findViewById(R.id.signInButton);
        forgotPasswordText = findViewById(R.id.forgotPassword);
    }

    /**
     * Attempt to login the employee with the provided credentials.
     */
    private void attemptLogin() {
        String email = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please enter both email and password");
            return;
        }

        // Validate the credentials
        if (validateCredentials(email, password)) {
            // Navigate to EmployeeDashboardActivity upon successful login
            Intent intent = new Intent(EmployeeLoginActivity.this, EmployeeDashboardActivity.class);
            intent.putExtra("EMPLOYEE_EMAIL", email);
            startActivity(intent);
            finish();
        } else {
            // Show an error message for invalid credentials
            showToast("Invalid email or password");
        }
    }

    /**
     * Validate user credentials using the database.
     */
    private boolean validateCredentials(String email, String password) {
        Employee employee = dbHelper.getEmployeeByEmail(email);

        if (employee == null) {
            showToast("Employee not found");
            return false;
        }

        if (employee.getPassword() != null && employee.getPassword().equals(password)) {
            return true;
        } else {
            showToast("Incorrect password");
            return false;
        }
    }

    /**
     * Navigate to the password reset activity.
     */
    private void navigateToResetPassword() {
        Intent intent = new Intent(EmployeeLoginActivity.this, EmployeeRestActivity.class);
        startActivity(intent);
    }

    /**
     * Utility method to show toast messages.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
