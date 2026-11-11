package com.cafe.cafeshopmanagement.model;

import java.sql.Timestamp;

public class Order {
    private int orderId;
    private int staffId;
    private Integer customerId; // Dùng Integer để cho phép null
    private int totalAmount;
    private int pointsUsed;
    private String paymentMethod; // "CASH", "QR"
    private String status; // "PENDING", "COMPLETED", "CANCELLED"
    private Timestamp createdAt;

    // BỔ SUNG: Hỗ trợ lưu trữ tạm thời SĐT khách vãng lai khi chưa map được customerId
    private String customerPhone;

    public Order() {}

    // Constructor đầy đủ ban đầu của bạn
    public Order(int orderId, int staffId, Integer customerId, int totalAmount, int pointsUsed, String paymentMethod, String status, Timestamp createdAt) {
        this.orderId = orderId;
        this.staffId = staffId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.pointsUsed = pointsUsed;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.createdAt = createdAt;
    }

    // BỔ SUNG: Constructor dùng cho lúc tạo hóa đơn nhanh ở StaffPOSController
    public Order(String customerPhone, int totalAmount) {
        this.customerPhone = customerPhone;
        this.totalAmount = totalAmount;
        this.status = "COMPLETED";     // Gán giá trị mặc định khi thanh toán
        this.paymentMethod = "CASH";   // Gán giá trị mặc định
        this.pointsUsed = 0;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }

    public int getPointsUsed() { return pointsUsed; }
    public void setPointsUsed(int pointsUsed) { this.pointsUsed = pointsUsed; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    // BỔ SUNG: Getter & Setter cho customerPhone
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
}