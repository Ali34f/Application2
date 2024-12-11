package com.example.application2;

import java.util.Objects;

/**
 * Represents an Employee entity with details such as name, position, email, phone, salary, and start date.
 */
public class Employee {

    private int id;           // Unique identifier for the employee
    private String name;      // Employee's full name
    private String position;  // Job position/title
    private String email;     // Employee's email address
    private String phone;     // Employee's phone number
    private double salary;    // Employee's salary
    private String startDate; // Start date of employment

    /**
     * Constructor for an existing employee (with ID).
     *
     * @param id        Unique identifier for the employee.
     * @param name      Employee's full name.
     * @param position  Job position/title.
     * @param email     Employee's email address.
     * @param phone     Employee's phone number.
     * @param salary    Employee's salary.
     * @param startDate Start date of employment.
     */
    public Employee(int id, String name, String position, String email, String phone, double salary, String startDate) {
        this.id = id;
        this.name = validateName(name);
        this.position = validatePosition(position);
        this.email = validateEmail(email);
        this.phone = validatePhone(phone);
        this.salary = validateSalary(salary);
        this.startDate = validateStartDate(startDate);
    }

    /**
     * Constructor for a new employee (without ID).
     *
     * @param name      Employee's full name.
     * @param position  Job position/title.
     * @param email     Employee's email address.
     * @param phone     Employee's phone number.
     * @param salary    Employee's salary.
     * @param startDate Start date of employment.
     */
    public Employee(String name, String position, String email, String phone, double salary, String startDate) {
        this.name = validateName(name);
        this.position = validatePosition(position);
        this.email = validateEmail(email);
        this.phone = validatePhone(phone);
        this.salary = validateSalary(salary);
        this.startDate = validateStartDate(startDate);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public double getSalary() {
        return salary;
    }

    public String getStartDate() {
        return startDate;
    }

    // Validation Methods

    private String validateName(String name) {
        return (name != null && !name.trim().isEmpty()) ? name.trim() : "Unknown Name";
    }

    private String validatePosition(String position) {
        return (position != null && !position.trim().isEmpty()) ? position.trim() : "Unknown Position";
    }

    private String validateEmail(String email) {
        if (email != null && email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return email.trim();
        }
        return "unknown@example.com";
    }

    private String validatePhone(String phone) {
        if (phone != null && phone.matches("^[+]?[0-9\\s\\-()]{10,15}$")) {
            return phone.trim();
        }
        return "000-000-0000"; // Fallback default phone number
    }

    private double validateSalary(double salary) {
        return Math.max(salary, 0); // Ensure salary is not negative
    }

    private String validateStartDate(String startDate) {
        return (startDate != null && !startDate.trim().isEmpty()) ? startDate.trim() : "Unknown Start Date";
    }

    // toString method for debugging and logging
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", salary=" + salary +
                ", startDate='" + startDate + '\'' +
                '}';
    }

    // Equals and hashCode methods for proper comparison and hashing

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
