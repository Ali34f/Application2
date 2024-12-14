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

import com.example.application2.utils.PasswordGenerator;
import com.example.application2.utils.EmailSender;

import java.util.Calendar;

/**
 * Activity to add new employee details.
 */
public class NewEmployeeDetailsActivity extends AppCompatActivity {

    private EditText etName, etPosition, etPhoneNumber, etEmailAddress, etSalary, etStartDate;
    private Button btnCancel, btnConfirm;
    private ImageView btnBack;
    private DatabaseHelper dbHelper;
    private static final String TAG = "NewEmployeeDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_employee);

        // Initialize views
        initializeViews();

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

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
     * Initialize views from the layout.
     */
    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etPosition = findViewById(R.id.etPosition);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etSalary = findViewById(R.id.etSalary);
        etStartDate = findViewById(R.id.etStartDate);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);
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
            String selectedDate = year1 + "-" + (month1 + 1) + "-" + day1;
            etStartDate.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * Save Employee Data and Navigate to Confirmation Screen.
     */
    private void saveEmployee() {
        String name = etName.getText().toString().trim();
        String position = etPosition.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String email = etEmailAddress.getText().toString().trim();
        String salaryText = etSalary.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || position.isEmpty() || phoneNumber.isEmpty() ||
                email.isEmpty() || salaryText.isEmpty() || startDate.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid salary format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a random password
        String generatedPassword = PasswordGenerator.generatePassword();
        Log.d(TAG, "Generated Password: " + generatedPassword);

        // Create new Employee object
        Employee employee = new Employee(name, position, email, phoneNumber, salary, startDate, generatedPassword);

        // Add employee to the database
        long result = dbHelper.addEmployee(employee);

        if (result != -1) {
            // Send the generated password via email
            EmailSender.sendEmail(email, "Your Account Credentials",
                    "Dear " + name + ",\n\nYour account has been created.\n\n" +
                            "Username: " + email + "\n" +
                            "Password: " + generatedPassword + "\n\n" +
                            "Please change your password upon first login.");

            Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show();
            navigateToConfirmation();
        } else {
            Toast.makeText(this, "Error adding employee", Toast.LENGTH_SHORT).show();
        }
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
