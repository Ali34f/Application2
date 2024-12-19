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

import com.example.application2.model.Employee;
import com.example.application2.model.EmployeeApiModel;
import com.example.application2.repository.EmployeeRepository;
import com.example.application2.utils.EmployeeConverter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private EmployeeRepository employeeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_management);

        // Initialize views and helpers
        initializeViews();
        dbHelper = new DatabaseHelper(this);
        employeeRepository = new EmployeeRepository();

        // Set up RecyclerView
        setupRecyclerView();

        // Load employees from API and local database
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
        btnBack.setOnClickListener(v -> navigateToDashboard());
        btnAddEmployee.setOnClickListener(v -> navigateToAddEmployee());
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
     * Load employees from API and local database.
     */
    private void loadEmployeeData() {
        // Load from API first
        employeeRepository.getAllEmployees(new Callback<List<EmployeeApiModel>>() {
            @Override
            public void onResponse(Call<List<EmployeeApiModel>> call, Response<List<EmployeeApiModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Employee> apiEmployees = new ArrayList<>();
                    for (EmployeeApiModel apiModel : response.body()) {
                        apiEmployees.add(EmployeeConverter.toEmployee(apiModel));
                    }
                    employeeList = apiEmployees;
                    employeeAdapter.updateEmployeeList(employeeList);
                    showToast("Loaded employees from API");
                } else {
                    showToast("Failed to load employees from API. Loading local data...");
                    loadLocalEmployeeData();
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeApiModel>> call, Throwable t) {
                showToast("API Error: " + t.getMessage() + ". Loading local data...");
                loadLocalEmployeeData();
            }
        });
    }

    /**
     * Load employees from the local database if API call fails.
     */
    private void loadLocalEmployeeData() {
        employeeList = dbHelper.getAllEmployees();
        if (employeeList == null || employeeList.isEmpty()) {
            showToast("No employees found in the local database");
            employeeList = new ArrayList<>();
        }
        employeeAdapter.updateEmployeeList(employeeList);
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

        List<Employee> filteredEmployees = dbHelper.searchEmployees(query);
        if (filteredEmployees == null || filteredEmployees.isEmpty()) {
            showToast("No matching employees found");
            employeeAdapter.updateEmployeeList(new ArrayList<>());
        } else {
            employeeAdapter.updateEmployeeList(filteredEmployees);
            showToast(filteredEmployees.size() + " employees found");
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
