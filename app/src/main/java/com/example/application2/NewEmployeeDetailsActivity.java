package com.example.application2;

import android.app.DatePickerDialog;
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
import com.example.application2.utils.PasswordGenerator;
import com.example.application2.utils.EmailSender;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity to add new employee details.
 */
public class NewEmployeeDetailsActivity extends AppCompatActivity {

    private EditText etName, etPosition, etPhoneNumber, etEmailAddress, etSalary, etStartDate, etLeaves;
    private Button btnCancel, btnConfirm;
    private ImageView btnBack;
    private EmployeeRepository employeeRepository;
    private DatabaseHelper databaseHelper;
    private static final String TAG = "NewEmployeeDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_employee);

        // Initialize views
        initializeViews();

        // Initialize EmployeeRepository and DatabaseHelper
        employeeRepository = new EmployeeRepository();
        databaseHelper = new DatabaseHelper(this);

        // Set listeners
        setupListeners();
    }

    /**
     * Initialize views from the layout.
     */
    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etPosition = findViewById(R.id.etPosition);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etSalary = findViewById(R.id.etSalary);
        etStartDate = findViewById(R.id.etStartDate);
        etLeaves = findViewById(R.id.etLeaves);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);
    }

    /**
     * Setup click listeners for buttons and date picker.
     */
    private void setupListeners() {
        // Back Button Click Listener
        btnBack.setOnClickListener(v -> navigateToEmployeeManagement());

        // Start Date Picker
        etStartDate.setOnClickListener(v -> showDatePicker());

        // Confirm Button Click Listener
        btnConfirm.setOnClickListener(v -> saveEmployee());

        // Cancel Button Click Listener
        btnCancel.setOnClickListener(v -> finish());
    }

    /**
     * Navigate back to EmployeeManagementActivity.
     */
    private void navigateToEmployeeManagement() {
        Intent intent = new Intent(NewEmployeeDetailsActivity.this, EmployeeManagementActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Show Date Picker Dialog for selecting the start date.
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, day1) -> {
            String selectedDate = year1 + "-" + String.format("%02d", (month1 + 1)) + "-" + String.format("%02d", day1);
            etStartDate.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * Save Employee Data and Navigate to Confirmation Screen.
     */
    private void saveEmployee() {
        String fullName = etName.getText().toString().trim();
        String position = etPosition.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String email = etEmailAddress.getText().toString().trim();
        String salaryText = etSalary.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String leavesText = etLeaves.getText().toString().trim();

        // Validate inputs
        if (fullName.isEmpty() || position.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || salaryText.isEmpty() || startDate.isEmpty() || leavesText.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double salary;
        int leaves;
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid salary format", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            leaves = Integer.parseInt(leavesText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid leaves format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Split full name into first name and last name
        String[] nameParts = fullName.split("\\s+", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        // Generate a random password
        String generatedPassword = PasswordGenerator.generatePassword();
        Log.d(TAG, "Generated Password: " + generatedPassword);

        // Create new EmployeeApiModel object for API
        EmployeeApiModel apiModel = new EmployeeApiModel(firstName, lastName, email, position, salary, startDate, leaves);

        // Call API to add employee
        employeeRepository.addEmployee(apiModel, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Store employee in the local database
                    Employee localEmployee = new Employee(
                            0, // ID will be auto-incremented locally
                            fullName,
                            position,
                            email,
                            phoneNumber,
                            salary,
                            startDate,
                            generatedPassword,
                            leaves
                    );

                    long result = databaseHelper.addEmployee(localEmployee);
                    if (result != -1) {
                        // Send email with login credentials
                        EmailSender.sendEmail(email, "Your Account Credentials",
                                "Dear " + fullName + ",\n\nYour account has been created.\n\n" +
                                        "Username: " + email + "\n" +
                                        "Password: " + generatedPassword + "\n\n" +
                                        "Please change your password upon first login.");

                        Toast.makeText(NewEmployeeDetailsActivity.this, "Employee added successfully", Toast.LENGTH_SHORT).show();
                        navigateToConfirmation();
                    } else {
                        Toast.makeText(NewEmployeeDetailsActivity.this, "Failed to save employee locally", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NewEmployeeDetailsActivity.this, "Failed to add employee via API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(NewEmployeeDetailsActivity.this, "API Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Navigate to NewEmployeeConfirmActivity.
     */
    private void navigateToConfirmation() {
        Intent intent = new Intent(NewEmployeeDetailsActivity.this, NewEmployeeConfirmActivity.class);
        startActivity(intent);
        finish();
    }
}
