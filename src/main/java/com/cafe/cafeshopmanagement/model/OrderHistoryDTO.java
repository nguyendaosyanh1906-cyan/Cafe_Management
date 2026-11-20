package com.cafe.cafeshopmanagement.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class OrderHistoryDTO {
    private int orderId;
    private String staffName;
    private String customerPhone;
    private int totalAmount;
    private String paymentMethod;
    private String status;
    private Timestamp createdAt;

    public OrderHistoryDTO(int orderId, String staffName, String customerPhone, int totalAmount, String paymentMethod, String status, Timestamp createdAt) {
        this.orderId = orderId;
        this.staffName = staffName;
        this.customerPhone = (customerPhone != null) ? customerPhone : "Khách vãng lai";
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getOrderId() { return orderId; }
    public String getStaffName() { return staffName; }
    public String getCustomerPhone() { return customerPhone; }

    public int getTotalAmount() { return totalAmount; }
    public String getFormattedTotal() { return String.format("%,d VNĐ", totalAmount); }

    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }

    public String getFormattedDate() {
        if (createdAt == null) return "";
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(createdAt);
    }
}