package com.cafe.cafeshopmanagement.model;

public class Topping {
    private int toppingId;
    private String name;
    private int price;
    private boolean active;

    public Topping() {}

    public Topping(int toppingId, String name, int price, boolean active) {
        this.toppingId = toppingId;
        this.name = name;
        this.price = price;
        this.active = active;
    }

    public int getToppingId() { return toppingId; }
    public void setToppingId(int toppingId) { this.toppingId = toppingId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // Hỗ trợ bí danh cho các Controller gọi getToppingName / setToppingName
    public String getToppingName() { return name; }
    public void setToppingName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}