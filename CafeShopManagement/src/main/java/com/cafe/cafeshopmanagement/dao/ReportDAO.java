package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportDAO {

    // Lấy doanh thu 7 ngày gần nhất (Dùng cho BarChart)
    public Map<String, Integer> getRevenueLast7Days() {
        Map<String, Integer> data = new LinkedHashMap<>();
        // Giả sử bảng orders có cột created_at (DATETIME) và total_amount (INT)
        String sql = "SELECT DATE(created_at) as order_date, SUM(total_amount) as revenue " +
                "FROM orders " +
                "WHERE status = 'COMPLETED' " +
                "GROUP BY DATE(created_at) " +
                "ORDER BY DATE(created_at) ASC LIMIT 7";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("order_date"), rs.getInt("revenue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    // Lấy Top 5 món bán chạy nhất (Dùng cho PieChart)
    public Map<String, Integer> getTopSellingProducts() {
        Map<String, Integer> data = new LinkedHashMap<>();
        // Kết hợp order_details và products để đếm số lượng
        String sql = "SELECT p.name, SUM(od.quantity) as total_sold " +
                "FROM order_details od " +
                "JOIN products p ON od.product_id = p.product_id " +
                "GROUP BY p.product_id, p.name " +
                "ORDER BY total_sold DESC LIMIT 5";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("name"), rs.getInt("total_sold"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    // Tổng doanh thu hôm nay (Dùng cho Label hiển thị nhanh)
    public int getTodayRevenue() {
        String sql = "SELECT SUM(total_amount) as today_revenue FROM orders " +
                "WHERE DATE(created_at) = CURDATE() AND status = 'COMPLETED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("today_revenue");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}