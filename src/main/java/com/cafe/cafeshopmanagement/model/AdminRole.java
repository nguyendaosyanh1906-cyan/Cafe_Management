package com.cafe.cafeshopmanagement.model;

public class AdminRole extends Role {
    @Override
    public String getRoleName() {
        return "ADMIN";
    }

    @Override
    public String getDashboardFxml() {
        return "/com/cafe/cafeshopmanagement/AdminDashboard.fxml";
    }

    @Override
    public boolean canManageProducts() {
        return true;
    }

    @Override
    public boolean canManageStaff() {
        return true;
    }
}