package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application2.model.Employee;
import com.example.application2.model.EmployeeApiModel;
import com.example.application2.repository.EmployeeRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditEmployeeActivity extends AppCompatActivity {

    private EditText editName, editPosition, editPhoneNumber, editEmailAddress, editLeaves;
    private Button btnCancel, btnConfirm;
    private ImageView btnBack;
    private int employeeId;
    private EmployeeRepository employeeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        initializeViews();
        employeeRepository = new EmployeeRepository();
        employeeId = getIntent().getIntExtra("EMPLOYEE_ID", -1);

        if (employeeId != -1) {
            loadEmployeeDetails(employeeId);
        } else {
            showToast("Invalid employee ID");
            finish();
        }

        setButtonActions();
    }

    private void initializeViews() {
        editName = findViewById(R.id.editName);
        editPosition = findViewById(R.id.editPosition);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editEmailAddress = findViewById(R.id.editEmailAddress);
        editLeaves = findViewById(R.id.editLeaves);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setButtonActions() {
        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
        btnConfirm.setOnClickListener(v -> updateEmployee());
    }

    private void loadEmployeeDetails(int id) {
        employeeRepository.getEmployeeById(id, new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateEmployeeDetails(response.body());
                } else {
                    showToast("Failed to load employee details: " + response.message());
                    Log.e("API Error", "Response: " + response.errorBody());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                showToast("Error loading employee: " + t.getMessage());
                Log.e("API Error", "Throwable: ", t);
                finish();
            }
        });
    }

    private void populateEmployeeDetails(Employee employee) {
        editName.setText(employee.getName());
        editPosition.setText(employee.getPosition());
        editPhoneNumber.setText(employee.getPhone());
        editEmailAddress.setText(employee.getEmail());
        editLeaves.setText(String.valueOf(employee.getLeaves()));
    }

    private void updateEmployee() {
        String name = editName.getText().toString().trim();
        String position = editPosition.getText().toString().trim();
        String phone = editPhoneNumber.getText().toString().trim();
        String email = editEmailAddress.getText().toString().trim();
        String leavesText = editLeaves.getText().toString().trim();

        if (name.isEmpty() || position.isEmpty() || phone.isEmpty() || email.isEmpty() || leavesText.isEmpty()) {
            showToast("Please fill in all fields");
            return;
        }

        int leaves;
        try {
            leaves = Integer.parseInt(leavesText);
        } catch (NumberFormatException e) {
            showToast("Invalid number of leaves");
            return;
        }

        EmployeeApiModel updatedEmployee = new EmployeeApiModel(
                name.split(" ")[0],
                name.contains(" ") ? name.split(" ")[1] : "",
                email,
                position,
                0.0, // Default salary for now
                "",  // Empty joining date for now
                leaves
        );

        Log.d("Update Request", "Updating Employee: " + updatedEmployee);

        employeeRepository.updateEmployee(employeeId, updatedEmployee, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showToast("Employee updated successfully");
                    Log.d("Update Success", "Employee updated with ID: " + employeeId);
                    navigateToEditEmployeeConfirm();
                } else {
                    showToast("Failed to update employee: " + response.message());
                    Log.e("API Error", "Response: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("Error updating employee: " + t.getMessage());
                Log.e("API Error", "Throwable: ", t);
            }
        });
    }

    private void navigateToEditEmployeeConfirm() {
        Intent intent = new Intent(EditEmployeeActivity.this, EditEmployeeConfirmActivity.class);
        intent.putExtra("EMPLOYEE_ID", employeeId);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
