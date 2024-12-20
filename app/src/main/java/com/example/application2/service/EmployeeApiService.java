package com.example.application2.service;

import com.example.application2.model.EmployeeApiModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;

/**
 * Service interface for Employee API interactions using Retrofit.
 */
public interface EmployeeApiService {

    /**
     * Retrieve a list of all employees from the API.
     *
     * @return A Call object containing a list of EmployeeApiModel.
     */
    @GET("employees")
    Call<List<EmployeeApiModel>> getAllEmployees();

    /**
     * Retrieve a specific employee by their ID from the API.
     *
     * @param id The ID of the employee.
     * @return A Call object containing the EmployeeApiModel.
     */
    @GET("employees/get/{id}")
    Call<EmployeeApiModel> getEmployeeById(@Path("id") int id);

    /**
     * Add a new employee to the API.
     *
     * @param employee The EmployeeApiModel representing the new employee.
     * @return A Call object for the operation result (Void).
     */
    @POST("employees/add")
    Call<Void> addEmployee(@Body EmployeeApiModel employee);

    /**
     * Update an existing employee by their ID in the API.
     *
     * @param id       The ID of the employee to update.
     * @param employee The EmployeeApiModel with updated employee details.
     * @return A Call object for the operation result (Void).
     */
    @PUT("employees/edit/{id}")
    Call<Void> updateEmployee(@Path("id") int id, @Body EmployeeApiModel employee);

    /**
     * Delete an employee by their ID in the API.
     *
     * @param id The ID of the employee to delete.
     * @return A Call object for the operation result (Void).
     */
    @DELETE("employees/delete/{id}")
    Call<Void> deleteEmployee(@Path("id") int id);
}
