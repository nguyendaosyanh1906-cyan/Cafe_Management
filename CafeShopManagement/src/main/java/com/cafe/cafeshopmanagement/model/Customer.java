package com.cafe.cafeshopmanagement.model;

public class Customer {
    private int customerId;
    private String phone;
    private String fullName;
    private int points;

    public Customer() {}

    public Customer(int customerId, String phone, String fullName, int points) {
        this.customerId = customerId;
        this.phone = phone;
        this.fullName = fullName;
        this.points = points;
    }

    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
}