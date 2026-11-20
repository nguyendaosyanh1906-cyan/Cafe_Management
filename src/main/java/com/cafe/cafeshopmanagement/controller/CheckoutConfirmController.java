package com.cafe.cafeshopmanagement.controller;

import com.cafe.cafeshopmanagement.model.CartItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CheckoutConfirmController {

    @FXML private TextArea txtInvoiceDetail;
    @FXML private Label lblTotalAmount;

    private boolean isConfirmed = false;

    // Hàm nhận dữ liệu từ màn hình POS truyền sang
    public void setInvoiceData(List<CartItem> cart, String customerInfo, int totalAmount) {
        lblTotalAmount.setText(String.format("%,d VNĐ", totalAmount));

        StringBuilder sb = new StringBuilder();
        sb.append("---------- CAFE POS ----------\n");
        sb.append("Ngày: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        sb.append("Khách hàng: ").append(customerInfo).append("\n");
        sb.append("------------------------------\n");

        for (CartItem item : cart) {
            sb.append(item.getProduct().getName()).append(" (Size ").append(item.getSize()).append(")\n");
            sb.append("   SL: ").append(item.getQuantity())
                    .append(" x ").append(String.format("%,.0f", item.getUnitPrice())).append(" VNĐ\n");

            if (item.getSugarPercent() != 100 || item.getIcePercent() != 100) {
                sb.append("   - ").append(item.getSugarPercent()).append("% Đường, ")
                        .append(item.getIcePercent()).append("% Đá\n");
            }
            if (!item.getToppings().isEmpty()) {
                sb.append("   - Topping: ");
                item.getToppings().forEach(t -> sb.append(t.getName()).append(", "));
                sb.setLength(sb.length() - 2); // Xóa dấu phẩy cuối
                sb.append("\n");
            }
        }
        sb.append("------------------------------\n");
        txtInvoiceDetail.setText(sb.toString());
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    @FXML
    void handleConfirm(ActionEvent event) {
        isConfirmed = true;
        closeWindow(event);
    }

    @FXML
    void handleCancel(ActionEvent event) {
        isConfirmed = false;
        closeWindow(event);
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}