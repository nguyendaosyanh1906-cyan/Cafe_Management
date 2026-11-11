package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.model.Category;
import com.cafe.cafeshopmanagement.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Categories";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category cat = new Category();
                cat.setCategoryId(rs.getInt("category_id"));
                cat.setName(rs.getString("name"));
                list.add(cat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}