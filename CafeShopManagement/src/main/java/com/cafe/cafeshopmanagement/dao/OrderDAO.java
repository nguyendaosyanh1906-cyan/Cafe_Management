package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.model.Order;
import com.cafe.cafeshopmanagement.model.OrderHistoryDTO; // Import thêm DTO
import com.cafe.cafeshopmanagement.utils.DBConnection;
import com.cafe.cafeshopmanagement.utils.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList; // Import thêm ArrayList
import java.util.List;      // Import thêm List

public class OrderDAO {

    public int createOrder(Order order) {
        String sql = "INSERT INTO Orders (staff_id, customer_id, total_amount, points_used, payment_method, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Lấy staff_id từ UserSession nếu chưa có
            int currentStaffId = order.getStaffId() > 0 ? order.getStaffId() : UserSession.getUserId();
            // Nếu không lấy được id staff thì để mặc định ID staff = 1
            ps.setInt(1, currentStaffId > 0 ? currentStaffId : 1);

            // Customer ID
            if (order.getCustomerId() != null && order.getCustomerId() > 0) {
                ps.setInt(2, order.getCustomerId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setInt(3, order.getTotalAmount());
            ps.setInt(4, order.getPointsUsed());
            ps.setString(5, order.getPaymentMethod() != null ? order.getPaymentMethod() : "CASH");
            ps.setString(6, order.getStatus() != null ? order.getStatus() : "COMPLETED");

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Trả về order_id tự tăng vừa tạo
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm Order: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public List<OrderHistoryDTO> getAllOrderHistory() {
        List<OrderHistoryDTO> list = new ArrayList<>();
        // Dùng LEFT JOIN để lấy tên nhân viên và SĐT khách hàng
        String sql = "SELECT o.order_id, u.full_name AS staff_name, c.phone AS customer_phone, " +
                "o.total_amount, o.payment_method, o.status, o.created_at " +
                "FROM Orders o " +
                "LEFT JOIN Users u ON o.staff_id = u.user_id " +
                "LEFT JOIN Customers c ON o.customer_id = c.customer_id " +
                "ORDER BY o.created_at DESC"; // Sắp xếp hóa đơn mới nhất lên đầu

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new OrderHistoryDTO(
                        rs.getInt("order_id"),
                        rs.getString("staff_name"),
                        rs.getString("customer_phone"),
                        rs.getInt("total_amount"),
                        rs.getString("payment_method"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy lịch sử hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}