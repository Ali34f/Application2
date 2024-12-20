package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application2.repository.EmployeeRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for confirming the deletion of an employee.
 */
public class ConfirmDeleteEmployeeActivity extends AppCompatActivity {

    private int employeeId;
    private EmployeeRepository employeeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_confirm_employee);

        // Initialize the EmployeeRepository
        employeeRepository = new EmployeeRepository();

        // Get the employee ID from the intent
        employeeId = getIntent().getIntExtra("EMPLOYEE_ID", -1);

        // Initialize buttons
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnConfirm = findViewById(R.id.btnConfirm);

        // Set button click listeners
        setButtonListeners(btnCancel, btnConfirm);
    }

    /**
     * Set button click listeners for cancel and confirm actions.
     *
     * @param btnCancel  The cancel button.
     * @param btnConfirm The confirm button.
     */
    private void setButtonListeners(Button btnCancel, Button btnConfirm) {
        // Cancel button to return to the previous activity
        btnCancel.setOnClickListener(v -> finish());

        // Confirm button to delete the employee and navigate to DeleteConfirmActivity
        btnConfirm.setOnClickListener(v -> confirmDeletion());
    }

    /**
     * Confirm the deletion of the employee via API call.
     */
    private void confirmDeletion() {
        if (employeeId == -1) {
            showToast("Invalid employee ID");
            return;
        }

        employeeRepository.deleteEmployee(employeeId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showToast("Employee deleted successfully");
                    navigateToDeleteConfirmActivity();
                } else {
                    showToast("Error deleting employee: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("API Error: " + t.getMessage());
            }
        });
    }

    /**
     * Navigate to the DeleteConfirmActivity after successful deletion.
     */
    private void navigateToDeleteConfirmActivity() {
        Intent intent = new Intent(this, DeleteConfirmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Utility method to show toast messages.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
