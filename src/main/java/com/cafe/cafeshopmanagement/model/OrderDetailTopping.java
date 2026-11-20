package com.cafe.cafeshopmanagement.model;

public class OrderDetailTopping {
    private int orderDetailId;
    private int toppingId;
    private int toppingPrice;

    public OrderDetailTopping() {}

    public OrderDetailTopping(int orderDetailId, int toppingId, int toppingPrice) {
        this.orderDetailId = orderDetailId;
        this.toppingId = toppingId;
        this.toppingPrice = toppingPrice;
    }

    public int getOrderDetailId() { return orderDetailId; }
    public void setOrderDetailId(int orderDetailId) { this.orderDetailId = orderDetailId; }
    public int getToppingId() { return toppingId; }
    public void setToppingId(int toppingId) { this.toppingId = toppingId; }
    public int getToppingPrice() { return toppingPrice; }
    public void setToppingPrice(int toppingPrice) { this.toppingPrice = toppingPrice; }
}