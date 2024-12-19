package com.example.application2.repository;

import com.example.application2.model.Employee;
import com.example.application2.service.EmployeeApiService;
import com.example.application2.utils.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRepository {
    private final EmployeeApiService apiService;

    public EmployeeRepository() {
        apiService = RetrofitClient.getClient().create(EmployeeApiService.class);
    }

    public void getAllEmployees(Callback<List<Employee>> callback) {
        Call<List<Employee>> call = apiService.getAllEmployees();
        call.enqueue(callback);
    }

    public void getEmployeeById(int id, Callback<Employee> callback) {
        Call<Employee> call = apiService.getEmployeeById(id);
        call.enqueue(callback);
    }

    public void addEmployee(Employee employee, Callback<Void> callback) {
        Call<Void> call = apiService.addEmployee(employee);
        call.enqueue(callback);
    }

    public void updateEmployee(int id, Employee employee, Callback<Void> callback) {
        Call<Void> call = apiService.updateEmployee(id, employee);
        call.enqueue(callback);
    }

    public void deleteEmployee(int id, Callback<Void> callback) {
        Call<Void> call = apiService.deleteEmployee(id);
        call.enqueue(callback);
    }
}
