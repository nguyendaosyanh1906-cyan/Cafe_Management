package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.model.Customer;
import com.cafe.cafeshopmanagement.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // --------------------------------------------------------
    // CÁC HÀM QUẢN LÝ DÀNH CHO GIAO DIỆN ADMIN (CRUD CƠ BẢN)
    // --------------------------------------------------------

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("phone"),
                        rs.getString("full_name"),
                        rs.getInt("points")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addCustomer(Customer c) {
        String sql = "INSERT INTO customers (phone, full_name, points) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getPhone());
            ps.setString(2, c.getFullName());
            ps.setInt(3, c.getPoints());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCustomer(Customer c) {
        String sql = "UPDATE customers SET phone=?, full_name=?, points=? WHERE customer_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getPhone());
            ps.setString(2, c.getFullName());
            ps.setInt(3, c.getPoints());
            ps.setInt(4, c.getCustomerId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE customer_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------------------------------------------------------
    // CÁC HÀM NGHIỆP VỤ DÀNH CHO GIAO DIỆN BÁN HÀNG (POS)
    // --------------------------------------------------------

    // 1. Tìm khách hàng theo Số điện thoại (Dùng khi check-in khách mua hàng)
    public Customer getCustomerByPhone(String phone) {
        String sql = "SELECT * FROM customers WHERE phone = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("customer_id"),
                            rs.getString("phone"),
                            rs.getString("full_name"),
                            rs.getInt("points")
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi tìm khách hàng theo SĐT: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy khách
    }

    // 2. Cập nhật số điểm của khách hàng (Cộng thêm điểm mới và trừ đi điểm đã dùng)
    public boolean updateCustomerPoints(int customerId, int pointsToAdd, int pointsUsed) {
        String sql = "UPDATE customers SET points = points + ? - ? WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pointsToAdd);
            ps.setInt(2, pointsUsed);
            ps.setInt(3, customerId);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Lỗi cập nhật điểm khách hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Thêm khách hàng mới từ POS và trả về thông tin khách hàng đó
    public Customer registerCustomer(String phone, String fullName) {
        String sql = "INSERT INTO customers (phone, full_name, points) VALUES (?, ?, 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, phone);
            ps.setString(2, fullName);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        return new Customer(newId, phone, fullName, 0);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi thêm khách hàng mới: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}