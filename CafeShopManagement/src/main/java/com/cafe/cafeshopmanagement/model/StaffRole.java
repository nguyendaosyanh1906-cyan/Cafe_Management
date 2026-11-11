package com.cafe.cafeshopmanagement.model;

public class StaffRole extends Role {
    @Override
    public String getRoleName() {
        return "STAFF";
    }

    @Override
    public String getDashboardFxml() {
        return "/com/cafe/cafeshopmanagement/StaffPOS.fxml";
    }

    @Override
    public boolean canManageProducts() {
        return false; // Nhân viên không được quản lý sản phẩm
    }

    @Override
    public boolean canManageStaff() {
        return false; // Nhân viên không được quản lý nhân sự
    }
}