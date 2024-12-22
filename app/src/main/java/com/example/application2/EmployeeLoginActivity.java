package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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

        // Validate input fields
        if (email.isEmpty()) {
            showToast("Email field cannot be empty");
            return;
        }

        if (password.isEmpty()) {
            showToast("Password field cannot be empty");
            return;
        }

        // Validate the credentials
        if (validateCredentials(email, password)) {
            // Navigate to EmployeeDashboardActivity upon successful login
            navigateToDashboard(email);
        } else {
            // Show an error message for invalid credentials
            showToast("Invalid email or password");
        }
    }

    /**
     * Validate user credentials using the database.
     *
     * @param email    The email entered by the user.
     * @param password The password entered by the user.
     * @return True if the credentials are valid, false otherwise.
     */
    private boolean validateCredentials(String email, String password) {
        return dbHelper.validateEmployeeLogin(email, password);
    }

    /**
     * Navigate to the Employee Dashboard Activity.
     *
     * @param email The email of the logged-in employee.
     */
    private void navigateToDashboard(String email) {
        Intent intent = new Intent(EmployeeLoginActivity.this, EmployeeDashboardActivity.class);
        intent.putExtra("EMPLOYEE_EMAIL", email);
        startActivity(intent);
        finish();
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
     *
     * @param message The message to be displayed.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
