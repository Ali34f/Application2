package com.example.application2.model;

import com.google.gson.annotations.SerializedName;

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

    // Constructor
    public EmployeeApiModel(String firstName, String lastName, String email, String department, double salary, String joiningDate, int leaves) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.leaves = leaves;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }
    public String getJoiningDate() { return joiningDate; }
    public int getLeaves() { return leaves; }

    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setDepartment(String department) { this.department = department; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setJoiningDate(String joiningDate) { this.joiningDate = joiningDate; }

    public void setLeaves(int leaves) { this.leaves = leaves; }
}
