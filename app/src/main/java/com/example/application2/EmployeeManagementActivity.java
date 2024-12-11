package com.example.application2;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to manage employee-related operations such as viewing, adding, and searching employees.
 */
public class EmployeeManagementActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnAddEmployee, btnSortFilter;
    private EditText etSearchEmployee;
    private RecyclerView recyclerViewEmployees;
    private EmployeeAdapter employeeAdapter;
    private DatabaseHelper dbHelper;
    private List<Employee> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_management);

        // Initialize views
        initializeViews();

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Set up RecyclerView
        setupRecyclerView();

        // Load all employees
        loadEmployeeData();

        // Set button click listeners
        setButtonListeners();

        // Set search functionality
        setSearchFunctionality();
    }

    /**
     * Initialize views from the layout.
     */
    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnAddEmployee = findViewById(R.id.btnAddEmployee);
        btnSortFilter = findViewById(R.id.btnSortFilter);
        etSearchEmployee = findViewById(R.id.etSearchEmployee);
        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);
    }

    /**
     * Set up the RecyclerView with a LinearLayoutManager and EmployeeAdapter.
     */
    private void setupRecyclerView() {
        recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(this));
        employeeList = new ArrayList<>();
        employeeAdapter = new EmployeeAdapter(this, employeeList);
        recyclerViewEmployees.setAdapter(employeeAdapter);
    }

    /**
     * Set button click listeners for navigation and sorting/filtering.
     */
    private void setButtonListeners() {
        // Back button functionality
        btnBack.setOnClickListener(v -> navigateToDashboard());

        // Add Employee button functionality
        btnAddEmployee.setOnClickListener(v -> navigateToAddEmployee());

        // Sort/Filter button functionality (reload employees)
        btnSortFilter.setOnClickListener(v -> loadEmployeeData());
    }

    /**
     * Set search functionality for the search EditText.
     */
    private void setSearchFunctionality() {
        etSearchEmployee.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getAction() == KeyEvent.ACTION_DOWN)) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    /**
     * Load all employees from the database and display them in the RecyclerView.
     */
    private void loadEmployeeData() {
        try {
            employeeList = dbHelper.getAllEmployees();
            if (employeeList.isEmpty()) {
                showToast("No employees found");
            }
            employeeAdapter.updateEmployeeList(employeeList);
        } catch (Exception e) {
            showToast("Error loading employees: " + e.getMessage());
        }
    }

    /**
     * Perform a search based on the input from the search EditText.
     */
    private void performSearch() {
        String query = etSearchEmployee.getText().toString().trim();
        if (query.isEmpty()) {
            showToast("Please enter a search term");
            return;
        }

        try {
            List<Employee> filteredEmployees = dbHelper.searchEmployees(query);
            if (filteredEmployees.isEmpty()) {
                showToast("No matching employees found");
            } else {
                employeeAdapter.updateEmployeeList(filteredEmployees);
                showToast(filteredEmployees.size() + " employees found");
            }
        } catch (Exception e) {
            showToast("Error searching employees: " + e.getMessage());
        }
    }

    /**
     * Navigate back to DashboardActivity.
     */
    private void navigateToDashboard() {
        startActivity(new Intent(EmployeeManagementActivity.this, DashboardActivity.class));
        finish();
    }

    /**
     * Navigate to NewEmployeeDetailsActivity to add a new employee.
     */
    private void navigateToAddEmployee() {
        startActivity(new Intent(EmployeeManagementActivity.this, NewEmployeeDetailsActivity.class));
    }

    /**
     * Utility method to show toast messages.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh employee list when returning to this activity
        loadEmployeeData();
    }
}
