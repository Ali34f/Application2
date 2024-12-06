package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EmployeeLoginActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Button signInButton;
    private TextView forgotPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);

        // Initialize UI elements
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        signInButton = findViewById(R.id.signInButton);
        forgotPasswordText = findViewById(R.id.forgotPassword);

        // Set sign-in button click listener
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (validateCredentials(username, password)) {
                    // Navigate to EmployeeDashboardActivity
                    Intent intent = new Intent(EmployeeLoginActivity.this, EmployeeDashboardActivity.class);
                    startActivity(intent);
                } else {
                    // Show an error message
                    Toast.makeText(EmployeeLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set forgot password click listener
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to EmployeeRestActivity
                Intent intent = new Intent(EmployeeLoginActivity.this, EmployeeRestActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Dummy method to validate user credentials.
     * Replace this with actual authentication logic.
     */
    private boolean validateCredentials(String username, String password) {
        // Replace with real validation logic
        return username.equals("employee") && password.equals("password");
    }
}
