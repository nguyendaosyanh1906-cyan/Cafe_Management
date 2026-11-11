package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.model.CartItem;
import com.cafe.cafeshopmanagement.model.Topping;
import com.cafe.cafeshopmanagement.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class OrderDetailDAO {

    public boolean saveOrderDetailsAndToppings(int orderId, List<CartItem> cartItems) {
        String sqlDetail = "INSERT INTO Order_Details (order_id, product_id, size, sugar_level, ice_level, quantity, price_at_purchase) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlTopping = "INSERT INTO Order_Detail_Toppings (order_detail_id, topping_id, topping_price) " +
                "VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bật Transaction để đảm bảo toàn vẹn dữ liệu

            for (CartItem item : cartItems) {
                // 1. Insert vào bảng Order_Details
                try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail, Statement.RETURN_GENERATED_KEYS)) {
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, item.getProduct().getProductId());
                    psDetail.setString(3, item.getSize() != null ? item.getSize() : "M");
                    psDetail.setInt(4, item.getSugarPercent());
                    psDetail.setInt(5, item.getIcePercent());
                    psDetail.setInt(6, item.getQuantity());
                    psDetail.setInt(7, (int) item.getUnitPrice());

                    psDetail.executeUpdate();

                    ResultSet rs = psDetail.getGeneratedKeys();
                    if (rs.next()) {
                        int orderDetailId = rs.getInt(1);

                        // 2. Insert các topping đi kèm vào bảng Order_Detail_Toppings
                        if (item.getToppings() != null && !item.getToppings().isEmpty()) {
                            try (PreparedStatement psTopping = conn.prepareStatement(sqlTopping)) {
                                for (Topping t : item.getToppings()) {
                                    psTopping.setInt(1, orderDetailId);
                                    psTopping.setInt(2, t.getToppingId());
                                    psTopping.setInt(3, t.getPrice());
                                    psTopping.addBatch();
                                }
                                psTopping.executeBatch();
                            }
                        }
                    }
                }
            }

            conn.commit(); // Hoàn tất Transaction
            return true;
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            }
            System.err.println("Lỗi khi lưu OrderDetails & Toppings: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }
    }
}