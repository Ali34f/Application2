package com.example.application2.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the Employee model used for API interactions.
 */
public class EmployeeApiModel {

    @SerializedName("id")
    private int id;

    @SerializedName("firstname")
    private String firstName;

    @SerializedName("lastname")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("department")
    private String department;

    @SerializedName("salary")
    private double salary;

    @SerializedName("joiningdate")
    private String joiningDate;

    @SerializedName("leaves")
    private int leaves;

    /**
     * Default Constructor
     */
    public EmployeeApiModel() {
    }

    /**
     * Constructor for creating an EmployeeApiModel instance.
     *
     * @param firstName   The first name of the employee.
     * @param lastName    The last name of the employee.
     * @param email       The email of the employee.
     * @param department  The department of the employee.
     * @param salary      The salary of the employee.
     * @param joiningDate The joining date of the employee.
     * @param leaves      The number of leaves for the employee.
     */
    public EmployeeApiModel(String firstName, String lastName, String email, String department, double salary, String joiningDate, int leaves) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.leaves = leaves;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName != null ? firstName : "";
    }

    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    public String getEmail() {
        return email != null ? email : "unknown@example.com";
    }

    public String getDepartment() {
        return department != null ? department : "Unknown Department";
    }

    public double getSalary() {
        return salary;
    }

    public String getJoiningDate() {
        return joiningDate != null ? joiningDate : "Unknown Date";
    }

    public int getLeaves() {
        return leaves;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public void setLeaves(int leaves) {
        this.leaves = leaves;
    }

    @Override
    public String toString() {
        return "EmployeeApiModel{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                ", joiningDate='" + joiningDate + '\'' +
                ", leaves=" + leaves +
                '}';
    }
}
