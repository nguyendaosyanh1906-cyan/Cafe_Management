package com.cafe.cafeshopmanagement.model;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private Role role;
    private String fullName;
    private String phone;
    private int salary;
    private boolean active;

    public User() {}

    public User(int userId, String username, String passwordHash, String roleStr, String fullName, String phone, int salary, boolean active) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.setRoleFromString(roleStr);
        this.fullName = fullName;
        this.phone = phone;
        this.salary = salary;
        this.active = active;
    }

    public void setRoleFromString(String roleStr) {
        if ("ADMIN".equalsIgnoreCase(roleStr)) {
            this.role = new AdminRole();
        } else {
            this.role = new StaffRole();
        }
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public int getSalary() { return salary; }
    public void setSalary(int salary) { this.salary = salary; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}