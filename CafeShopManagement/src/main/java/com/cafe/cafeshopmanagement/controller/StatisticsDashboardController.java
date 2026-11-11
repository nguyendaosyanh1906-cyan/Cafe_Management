package com.cafe.cafeshopmanagement.controller;

import com.cafe.cafeshopmanagement.dao.ReportDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.util.Map;

public class StatisticsDashboardController {

    @FXML private Label lblTodayRevenue;
    @FXML private BarChart<String, Number> barChartRevenue;
    @FXML private PieChart pieChartTopProducts;

    private ReportDAO reportDAO = new ReportDAO();

    @FXML
    public void initialize() {
        loadTodayRevenue();
        loadRevenueChart();
        loadTopProductsChart();
    }

    private void loadTodayRevenue() {
        int todayRev = reportDAO.getTodayRevenue();
        lblTodayRevenue.setText(String.format("%,d VNĐ", todayRev));
    }

    private void loadRevenueChart() {
        barChartRevenue.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu (VNĐ)");

        Map<String, Integer> revenueData = reportDAO.getRevenueLast7Days();
        for (Map.Entry<String, Integer> entry : revenueData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChartRevenue.getData().add(series);
    }

    private void loadTopProductsChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Map<String, Integer> topProducts = reportDAO.getTopSellingProducts();

        for (Map.Entry<String, Integer> entry : topProducts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + " ly)", entry.getValue()));
        }

        pieChartTopProducts.setData(pieChartData);
    }
}