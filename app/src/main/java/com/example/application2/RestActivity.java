package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        // Initialize UI elements
        EditText etNewPassword = findViewById(R.id.eNewpassword);
        EditText etConfirmPassword = findViewById(R.id.etPassword_confirm);
        Button btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Save changes button functionality
        btnSaveChanges.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RestActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(RestActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // Successfully changed password (replace with actual logic to save the password)
                Toast.makeText(RestActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                // Navigate to RestConfirmActivity
                Intent intent = new Intent(RestActivity.this, RestConfirmActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
