package com.example.application2.service;

import com.example.application2.model.Employee;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;

public interface EmployeeApiService {

    @GET("/employees")
    Call<List<Employee>> getAllEmployees();

    @GET("/employees/get/{id}")
    Call<Employee> getEmployeeById(@Path("id") int id);

    @POST("/employees/add")
    Call<Void> addEmployee(@Body Employee employee);

    @PUT("/employees/edit/{id}")
    Call<Void> updateEmployee(@Path("id") int id, @Body Employee employee);

    @DELETE("/employees/delete/{id}")
    Call<Void> deleteEmployee(@Path("id") int id);
}
