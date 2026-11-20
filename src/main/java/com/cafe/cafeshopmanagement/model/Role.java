package com.cafe.cafeshopmanagement.model;

public abstract class Role {
    public abstract String getRoleName();
    public abstract String getDashboardFxml();

    // Khai báo các phương thức phân quyền
    public abstract boolean canManageProducts();
    public abstract boolean canManageStaff();

    @Override
    public String toString() {
        return getRoleName();
    }
}