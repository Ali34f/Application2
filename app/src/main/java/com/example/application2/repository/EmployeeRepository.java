package com.example.application2.repository;

import com.example.application2.model.Employee;
import com.example.application2.model.EmployeeApiModel;
import com.example.application2.service.EmployeeApiService;
import com.example.application2.utils.EmployeeConverter;
import com.example.application2.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for handling API interactions related to Employee data.
 */
public class EmployeeRepository {
    private final EmployeeApiService apiService;

    /**
     * Constructor initializes the API service using Retrofit.
     */
    public EmployeeRepository() {
        apiService = RetrofitClient.getClient().create(EmployeeApiService.class);
    }

    /**
     * Fetch all employees from the API and convert them to Employee objects.
     *
     * @param callback Callback for handling the response or failure.
     */
    public void getAllEmployees(Callback<List<Employee>> callback) {
        Call<List<EmployeeApiModel>> call = apiService.getAllEmployees();
        call.enqueue(new Callback<List<EmployeeApiModel>>() {
            @Override
            public void onResponse(Call<List<EmployeeApiModel>> call, Response<List<EmployeeApiModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Employee> employees = new ArrayList<>();
                    for (EmployeeApiModel apiModel : response.body()) {
                        employees.add(EmployeeConverter.toEmployee(apiModel));
                    }
                    callback.onResponse(null, Response.success(employees));
                } else {
                    callback.onFailure(null, new Throwable("Failed to load employees from API"));
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeApiModel>> call, Throwable t) {
                callback.onFailure(null, t);
            }
        });
    }

    /**
     * Fetch a single employee by ID from the API and convert it to an Employee object.
     *
     * @param id       The ID of the employee to fetch.
     * @param callback Callback for handling the response or failure.
     */
    public void getEmployeeById(int id, Callback<Employee> callback) {
        Call<EmployeeApiModel> call = apiService.getEmployeeById(id);
        call.enqueue(new Callback<EmployeeApiModel>() {
            @Override
            public void onResponse(Call<EmployeeApiModel> call, Response<EmployeeApiModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Employee employee = EmployeeConverter.toEmployee(response.body());
                    callback.onResponse(null, Response.success(employee));
                } else {
                    callback.onFailure(null, new Throwable("Failed to load employee from API"));
                }
            }

            @Override
            public void onFailure(Call<EmployeeApiModel> call, Throwable t) {
                callback.onFailure(null, t);
            }
        });
    }

    /**
     * Add a new employee to the API.
     *
     * @param apiModel The EmployeeApiModel object representing the new employee.
     * @param callback Callback for handling the response or failure.
     */
    public void addEmployee(EmployeeApiModel apiModel, Callback<Void> callback) {
        Call<Void> call = apiService.addEmployee(apiModel);
        call.enqueue(callback);
    }

    /**
     * Update an existing employee in the API.
     *
     * @param id       The ID of the employee to update.
     * @param apiModel The updated EmployeeApiModel object.
     * @param callback Callback for handling the response or failure.
     */
    public void updateEmployee(int id, EmployeeApiModel apiModel, Callback<Void> callback) {
        Call<Void> call = apiService.updateEmployee(id, apiModel);
        call.enqueue(callback);
    }

    /**
     * Delete an employee from the API by ID.
     *
     * @param id       The ID of the employee to delete.
     * @param callback Callback for handling the response or failure.
     */
    public void deleteEmployee(int id, Callback<Void> callback) {
        Call<Void> call = apiService.deleteEmployee(id);
        call.enqueue(callback);
    }
}
