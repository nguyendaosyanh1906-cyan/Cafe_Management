package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.model.Topping;
import com.cafe.cafeshopmanagement.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ToppingDAO {

    public List<Topping> getAllToppings() {
        return getActiveToppings();
    }

    public List<Topping> getActiveToppings() {
        List<Topping> list = new ArrayList<>();
        String sql = "SELECT * FROM Toppings WHERE is_active = TRUE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Topping t = new Topping();
                t.setToppingId(rs.getInt("topping_id"));
                // Đã sửa từ "topping_name" thành "name" cho khớp với DB
                t.setName(rs.getString("name"));
                t.setPrice(rs.getInt("price"));
                t.setActive(rs.getBoolean("is_active"));
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addTopping(Topping t) {
        // Đã sửa cột topping_name -> name
        String sql = "INSERT INTO Toppings (name, price, is_active) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getName());
            ps.setInt(2, t.getPrice());
            ps.setBoolean(3, t.isActive());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTopping(Topping t) {
        // Đã sửa cột topping_name -> name
        String sql = "UPDATE Toppings SET name=?, price=?, is_active=? WHERE topping_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getName());
            ps.setInt(2, t.getPrice());
            ps.setBoolean(3, t.isActive());
            ps.setInt(4, t.getToppingId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTopping(int toppingId) {
        String sql = "UPDATE Toppings SET is_active=FALSE WHERE topping_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, toppingId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}