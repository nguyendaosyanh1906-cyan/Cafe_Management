package com.cafe.cafeshopmanagement.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Đường dẫn đến database cafe_management_db của bạn
    private static final String URL = "jdbc:mysql://localhost:3306/cafe_management_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Syanh1906@"; // <-- Đổi dòng này

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Kết nối Database thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối Database: " + e.getMessage());
        }
        return connection;
    }
}