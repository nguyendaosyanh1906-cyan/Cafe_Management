package com.cafe.cafeshopmanagement.controller;

import com.cafe.cafeshopmanagement.dao.ReportDAO;
import com.cafe.cafeshopmanagement.dao.impl.ReportDAOImpl; // Đã thêm import cho ReportDAOImpl
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class StatisticsDashboardController {

    @FXML private Label lblTodayRevenue;
    @FXML private BarChart<String, Number> barChartRevenue;
    @FXML private PieChart pieChartTopProducts;

    // SỬA LỖI TẠI ĐÂY: Sử dụng class Impl để khởi tạo interface
    private ReportDAO reportDAO = new ReportDAOImpl();

    @FXML
    public void initialize() {
        // Sử dụng CompletableFuture để chạy ngầm các truy vấn DB, tránh làm đơ giao diện
        CompletableFuture.runAsync(() -> {
            int todayRev = reportDAO.getTodayRevenue();
            Map<String, Integer> revenueData = reportDAO.getRevenueLast7Days();
            Map<String, Integer> topProducts = reportDAO.getTopSellingProducts();

            // Chỉ cập nhật giao diện trên JavaFX Application Thread
            Platform.runLater(() -> {
                updateTodayRevenue(todayRev);
                updateRevenueChart(revenueData);
                updateTopProductsChart(topProducts);
            });
        }).exceptionally(ex -> {
            ex.printStackTrace(); // In lỗi ra console nếu truy vấn thất bại
            return null;
        });
    }

    private void updateTodayRevenue(int todayRev) {
        lblTodayRevenue.setText(String.format("%,d VNĐ", todayRev));
    }

    private void updateRevenueChart(Map<String, Integer> revenueData) {
        barChartRevenue.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu (VNĐ)");

        for (Map.Entry<String, Integer> entry : revenueData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChartRevenue.getData().add(series);
    }

    private void updateTopProductsChart(Map<String, Integer> topProducts) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : topProducts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + " ly)", entry.getValue()));
        }

        pieChartTopProducts.setData(pieChartData);
    }
}