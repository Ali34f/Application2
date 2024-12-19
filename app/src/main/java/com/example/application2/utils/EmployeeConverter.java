package com.example.application2.utils;

import com.example.application2.model.Employee;
import com.example.application2.model.EmployeeApiModel;

/**
 * Utility class for converting EmployeeApiModel to Employee.
 */
public class EmployeeConverter {

    /**
     * Converts an EmployeeApiModel (from API) to an Employee (local model).
     *
     * @param apiModel The EmployeeApiModel object to convert.
     * @return The converted Employee object.
     */
    public static Employee toEmployee(EmployeeApiModel apiModel) {
        if (apiModel == null) {
            return null;
        }

        // Combine first name and last name to form the full name
        String fullName = apiModel.getFirstName() + " " + apiModel.getLastName();

        // Create and return the Employee object
        return new Employee(
                apiModel.getId(),                      // ID
                fullName,                              // Name (Full Name)
                apiModel.getDepartment(),              // Department mapped to Position
                apiModel.getEmail(),                   // Email
                "000-000-0000",                        // Default placeholder for Phone
                apiModel.getSalary(),                  // Salary
                apiModel.getJoiningDate(),             // Start Date (Joining Date)
                "defaultPassword",                     // Default placeholder for Password
                apiModel.getLeaves()                   // Leaves
        );
    }
}
