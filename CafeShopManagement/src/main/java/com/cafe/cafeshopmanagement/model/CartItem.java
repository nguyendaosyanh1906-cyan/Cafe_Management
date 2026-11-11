package com.cafe.cafeshopmanagement.model;

import java.util.ArrayList;
import java.util.List;

public class CartItem {
    private Product product;
    private String size; // S, M, L
    private int sugarPercent; // 0, 30, 50, 70, 100
    private int icePercent;   // 0, 30, 50, 70, 100
    private List<Topping> toppings;
    private int quantity;
    private double unitPrice;

    public CartItem(Product product, String size, int sugarPercent, int icePercent, List<Topping> toppings, int quantity) {
        this.product = product;
        this.size = size;
        this.sugarPercent = sugarPercent;
        this.icePercent = icePercent;
        this.toppings = toppings != null ? toppings : new ArrayList<>();
        this.quantity = quantity;
        this.unitPrice = calculateUnitPrice();
    }

    private double calculateUnitPrice() {
        double basePrice = product.getBasePrice();
        double sizeMultiplier = 1.0;

        if ("S".equalsIgnoreCase(size)) {
            sizeMultiplier = 0.7; // 70% giá base
        } else if ("L".equalsIgnoreCase(size)) {
            sizeMultiplier = 1.3; // 130% giá base
        } else {
            sizeMultiplier = 1.0; // 100% giá base (Size M)
        }

        double price = basePrice * sizeMultiplier;

        for (Topping t : toppings) {
            price += t.getPrice();
        }
        return Math.max(price, 0);
    }

    public Product getProduct() { return product; }
    public String getSize() { return size; }
    public int getSugarPercent() { return sugarPercent; }
    public int getIcePercent() { return icePercent; }
    public List<Topping> getToppings() { return toppings; }

    public String getProductName() {
        StringBuilder sb = new StringBuilder(product.getName());
        sb.append(" (Size ").append(size).append(")");
        if (sugarPercent != 100 || icePercent != 100) {
            sb.append("\n• ").append(sugarPercent).append("% Đường, ").append(icePercent).append("% Đá");
        }
        if (!toppings.isEmpty()) {
            sb.append("\n• Topping: ");
            for (int i = 0; i < toppings.size(); i++) {
                sb.append(toppings.get(i).getName());
                if (i < toppings.size() - 1) sb.append(", ");
            }
        }
        return sb.toString();
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotalPrice() { return unitPrice * quantity; }

    public String getFormattedUnitPrice() { return String.format("%,.0f VNĐ", unitPrice); }
    public String getFormattedTotalPrice() { return String.format("%,.0f VNĐ", getTotalPrice()); }
}