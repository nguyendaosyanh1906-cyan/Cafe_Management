package com.cafe.cafeshopmanagement.controller;

import com.cafe.cafeshopmanagement.dao.OrderDAO;
import com.cafe.cafeshopmanagement.model.OrderHistoryDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminOrderController {

    @FXML private TableView<OrderHistoryDTO> tableOrders;
    @FXML private TableColumn<OrderHistoryDTO, Integer> colOrderId;
    @FXML private TableColumn<OrderHistoryDTO, String> colDate;
    @FXML private TableColumn<OrderHistoryDTO, String> colStaff;
    @FXML private TableColumn<OrderHistoryDTO, String> colCustomer;
    @FXML private TableColumn<OrderHistoryDTO, String> colTotal;
    @FXML private TableColumn<OrderHistoryDTO, String> colPayment;
    @FXML private TableColumn<OrderHistoryDTO, String> colStatus;

    private OrderDAO orderDAO = new OrderDAO();
    private ObservableList<OrderHistoryDTO> orderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Map cột với các phương thức get trong OrderHistoryDTO
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        colStaff.setCellValueFactory(new PropertyValueFactory<>("staffName"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("formattedTotal"));
        colPayment.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableOrders.setItems(orderList);
        loadOrderData();
    }

    private void loadOrderData() {
        orderList.clear();
        orderList.addAll(orderDAO.getAllOrderHistory());
    }

    @FXML
    void handleRefresh() {
        loadOrderData();
    }
}