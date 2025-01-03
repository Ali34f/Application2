package com.example.application2.model;

import java.util.Objects;

/**
 * Represents an Employee entity with details such as name, position, email, phone, salary, start date, password, and leaves.
 */
public class Employee {

    // Fields
    private int id;
    private String name;
    private String position;
    private String email;
    private String phone;
    private double salary;
    private String startDate;
    private String password;
    private int leaves;

    /**
     * Constructor for existing employee (with ID and password).
     *
     * @param id        The unique identifier of the employee.
     * @param name      The full name of the employee.
     * @param position  The position or department of the employee.
     * @param email     The email address of the employee.
     * @param phone     The phone number of the employee.
     * @param salary    The salary of the employee.
     * @param startDate The start date of employment.
     * @param password  The password for the employee's account.
     * @param leaves    The number of leaves available.
     */
    public Employee(int id, String name, String position, String email, String phone, double salary, String startDate, String password, int leaves) {
        this.id = id;
        this.name = validateName(name);
        this.position = validatePosition(position);
        this.email = validateEmail(email);
        this.phone = validatePhone(phone);
        this.salary = validateSalary(salary);
        this.startDate = validateStartDate(startDate);
        this.password = password;
        this.leaves = validateLeaves(leaves);
    }

    /**
     * Constructor for new employee (without ID).
     *
     * @param name      The full name of the employee.
     * @param position  The position or department of the employee.
     * @param email     The email address of the employee.
     * @param phone     The phone number of the employee.
     * @param salary    The salary of the employee.
     * @param startDate The start date of employment.
     * @param password  The password for the employee's account.
     * @param leaves    The number of leaves available.
     */
    public Employee(String name, String position, String email, String phone, double salary, String startDate, String password, int leaves) {
        this.name = validateName(name);
        this.position = validatePosition(position);
        this.email = validateEmail(email);
        this.phone = validatePhone(phone);
        this.salary = validateSalary(salary);
        this.startDate = validateStartDate(startDate);
        this.password = password;
        this.leaves = validateLeaves(leaves);
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

    public String getPassword() {
        return password;
    }

    public int getLeaves() {
        return leaves;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLeaves(int leaves) {
        this.leaves = validateLeaves(leaves);
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
        return "000-000-0000";
    }

    private double validateSalary(double salary) {
        return Math.max(salary, 0);
    }

    private String validateStartDate(String startDate) {
        return (startDate != null && !startDate.trim().isEmpty()) ? startDate.trim() : "Unknown Start Date";
    }

    private int validateLeaves(int leaves) {
        return Math.max(leaves, 0);
    }

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
                ", leaves=" + leaves +
                ", password='" + (password != null ? "****" : null) + '\'' +
                '}';
    }

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
