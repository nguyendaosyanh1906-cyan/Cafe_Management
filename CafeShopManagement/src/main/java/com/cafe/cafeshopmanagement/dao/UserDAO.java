package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.model.User;
import com.cafe.cafeshopmanagement.utils.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User login(String username, String password) {
        // CHỈ tìm bằng username, KHÔNG so sánh password_hash bằng SQL
        String sql = "SELECT * FROM users WHERE username = ? AND is_active = TRUE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");

                // Sử dụng BCrypt để so sánh mật khẩu gốc với mã băm
                if (BCrypt.checkpw(password, storedHash)) {
                    return new User(
                            rs.getInt("user_id"), rs.getString("username"), storedHash,
                            rs.getString("role"), rs.getString("full_name"), rs.getString("phone"),
                            rs.getInt("salary"), rs.getBoolean("is_active")
                    );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // Hàm lấy danh sách tài khoản đang hoạt động
    public List<User> getActiveUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE is_active = TRUE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"),
                        rs.getString("role"), rs.getString("full_name"), rs.getString("phone"),
                        rs.getInt("salary"), rs.getBoolean("is_active")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // HÀM MỚI BỔ SUNG: Lấy TOÀN BỘ tài khoản (gồm cả đang hoạt động và bị khóa)
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("user_id"), rs.getString("username"), rs.getString("password_hash"),
                        rs.getString("role"), rs.getString("full_name"), rs.getString("phone"),
                        rs.getInt("salary"), rs.getBoolean("is_active")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean addUser(User u) {
        String sql = "INSERT INTO users (username, password_hash, role, full_name, phone, salary, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());

            // TỰ ĐỘNG BĂM MẬT KHẨU TRƯỚC KHI LƯU
            String hashedPassword = BCrypt.hashpw(u.getPasswordHash(), BCrypt.gensalt());
            ps.setString(2, hashedPassword);

            ps.setString(3, u.getRole().getRoleName());
            ps.setString(4, u.getFullName()); ps.setString(5, u.getPhone());
            ps.setInt(6, u.getSalary()); ps.setBoolean(7, u.isActive());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateUser(User u) {
        String sql = "UPDATE users SET username=?, password_hash=?, role=?, full_name=?, phone=?, salary=?, is_active=? WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());

            // Kiểm tra: Nếu password chưa bị băm (không bắt đầu bằng "$2a$") thì băm nó
            String pwd = u.getPasswordHash();
            if (pwd != null && !pwd.startsWith("$2a$")) {
                pwd = BCrypt.hashpw(pwd, BCrypt.gensalt());
            }
            ps.setString(2, pwd);

            ps.setString(3, u.getRole().getRoleName());
            ps.setString(4, u.getFullName()); ps.setString(5, u.getPhone());
            ps.setInt(6, u.getSalary()); ps.setBoolean(7, u.isActive());
            ps.setInt(8, u.getUserId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ĐÃ SỬA: Xóa vĩnh viễn dòng khỏi Database (DELETE vĩnh viễn thay vì chỉ chuyển is_active = FALSE)
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}