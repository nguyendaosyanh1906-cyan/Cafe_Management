package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.model.Product;
import com.cafe.cafeshopmanagement.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Alias hỗ trợ cho các Controller gọi getAllProducts()
    public List<Product> getAllProducts() {
        return getActiveProducts();
    }

    // 1. Lấy tất cả sản phẩm đang kinh doanh (is_active = TRUE)
    public List<Product> getActiveProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE is_active = TRUE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setImageUrl(rs.getString("image_url"));
                p.setBasePrice(rs.getInt("base_price"));
                p.setActive(rs.getBoolean("is_active"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy sản phẩm theo Category (dùng khi bấm vào nút lọc trên UI)
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE category_id = ? AND is_active = TRUE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("product_id"),
                            rs.getInt("category_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("image_url"),
                            rs.getInt("base_price"),
                            rs.getBoolean("is_active")
                    );
                    list.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Thêm mới sản phẩm
    public boolean addProduct(Product p) {
        String sql = "INSERT INTO Products (category_id, name, description, image_url, base_price, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getDescription() != null ? p.getDescription() : "");
            ps.setString(4, p.getImageUrl() != null ? p.getImageUrl() : "");
            ps.setInt(5, p.getBasePrice());
            ps.setBoolean(6, p.isActive());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Cập nhật sản phẩm
    public boolean updateProduct(Product p) {
        String sql = "UPDATE Products SET category_id=?, name=?, description=?, image_url=?, base_price=?, is_active=? WHERE product_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getDescription() != null ? p.getDescription() : "");
            ps.setString(4, p.getImageUrl() != null ? p.getImageUrl() : "");
            ps.setInt(5, p.getBasePrice());
            ps.setBoolean(6, p.isActive());
            ps.setInt(7, p.getProductId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Xóa mềm sản phẩm (Ngừng bán - ẩn khỏi UI nhưng vẫn giữ data cũ để đối soát hóa đơn)
    public boolean deleteProduct(int productId) {
        String sql = "UPDATE Products SET is_active=FALSE WHERE product_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}