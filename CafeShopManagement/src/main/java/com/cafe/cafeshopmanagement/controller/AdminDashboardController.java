package com.cafe.cafeshopmanagement.controller;

import com.cafe.cafeshopmanagement.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AdminDashboardController {

    @FXML private Button btnManageProducts;
    @FXML private Button btnManageStaff;
    @FXML private Button btnOrderHistory;
    @FXML private Button btnDashboard;
    @FXML private Button btnPOS;
    @FXML private Button btnLogout;
    @FXML private VBox mainContent;

    @FXML
    public void initialize() {
        // Tự động tải trang Thống kê làm màn hình mặc định khi Admin đăng nhập thành công
        loadSubView("StatisticsDashboard.fxml");
    }

    // Phương thức dùng chung để tải 1 file FXML nhúng vào vùng giữa (mainContent)
    private void loadSubView(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cafe/cafeshopmanagement/" + fxmlFileName));
            Node view = loader.load();
            mainContent.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi load view: " + fxmlFileName);
        }
    }

    @FXML
    void handleDashboard(ActionEvent event) {
        System.out.println("Mở Thống kê & Báo cáo");
        loadSubView("StatisticsDashboard.fxml"); // Bổ sung đường dẫn file thống kê
    }

    @FXML
    void handleOrderHistory(ActionEvent event) {
        System.out.println("Mở Lịch sử hóa đơn");
        loadSubView("AdminOrder.fxml");
    }

    @FXML
    void handleManageProducts(ActionEvent event) {
        System.out.println("Mở Quản lý sản phẩm");
        loadSubView("ProductManagement.fxml");
    }

    @FXML
    void handleManageStaff(ActionEvent event) {
        System.out.println("Mở Quản lý nhân viên");
        loadSubView("UserCustomerManagement.fxml");
    }

    @FXML
    void handleGoToPOS(ActionEvent event) {
        SceneManager.switchScene("/com/cafe/cafeshopmanagement/StaffPOS.fxml", "Trang bán hàng - POS");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        SceneManager.switchScene("/com/cafe/cafeshopmanagement/Login.fxml", "Cafe Shop Management - Đăng nhập");
    }
}