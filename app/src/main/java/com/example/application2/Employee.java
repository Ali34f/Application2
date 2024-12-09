package com.example.application2;

public class Employee {
    private int id;
    private String name;
    private String position;
    private String email;
    private String phone;
    private double salary;
    private String startDate;

    // Constructor with ID (for existing employees)
    public Employee(int id, String name, String position, String email, String phone, double salary, String startDate) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.salary = salary;
        this.startDate = startDate;
    }

    // Constructor without ID (for new employees)
    public Employee(String name, String position, String email, String phone, double salary, String startDate) {
        this.name = name;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.salary = salary;
        this.startDate = startDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public double getSalary() { return salary; }
    public String getStartDate() { return startDate; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPosition(String position) { this.position = position; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
}
