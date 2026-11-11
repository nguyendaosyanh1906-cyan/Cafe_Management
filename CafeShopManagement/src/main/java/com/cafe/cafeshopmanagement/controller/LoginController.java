package com.cafe.cafeshopmanagement.controller;

import com.cafe.cafeshopmanagement.dao.UserDAO;
import com.cafe.cafeshopmanagement.model.User;
import com.cafe.cafeshopmanagement.utils.SceneManager;
import com.cafe.cafeshopmanagement.utils.UserSession; // Import class UserSession bạn vừa tạo
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblError;

    @FXML
    private Button btnLogin;

    private UserDAO userDAO = new UserDAO();

    @FXML
    void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setStyle("-fx-text-fill: red;");
            lblError.setText("Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
            return;
        }

        // Gọi DAO kiểm tra thông tin đăng nhập
        User loginUser = userDAO.login(username, password);

        if (loginUser != null) {
            lblError.setStyle("-fx-text-fill: green;");
            lblError.setText("Đăng nhập thành công!");

            // LƯU PHIÊN ĐĂNG NHẬP (Chuyển tên Role thành IN HOA, vd: "ADMIN" hoặc "STAFF")
            String roleName = loginUser.getRole().getRoleName().toUpperCase();
            UserSession.setRole(roleName);

            // Lấy đường dẫn FXML từ Strategy Pattern (Abstract Class Role)
            String targetFxml = loginUser.getRole().getDashboardFxml();
            String title = "Hệ thống Bán hàng - Quyền: " + loginUser.getRole().getRoleName();

            // Chuyển màn hình tự động
            SceneManager.switchScene(targetFxml, title);
        } else {
            lblError.setStyle("-fx-text-fill: red;");
            lblError.setText("Sai tài khoản hoặc mật khẩu!");
        }
    }
}