package com.cafe.cafeshopmanagement.model;

public class Product {
    private int productId;
    private int categoryId;
    private String name;
    private String description;
    private String imageUrl;
    private int basePrice;
    private boolean isActive;

    public Product() {}
    public Product(int productId, int categoryId, String name, String description, String imageUrl, int basePrice, boolean isActive) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.basePrice = basePrice;
        this.isActive = isActive;
    }

    // Getters and Setters (Bạn dùng Alt+Insert hoặc tính năng Generate để sinh nốt nhé)
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getBasePrice() { return basePrice; }
    public void setBasePrice(int basePrice) { this.basePrice = basePrice; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}