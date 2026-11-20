package com.cafe.cafeshopmanagement.model;

import java.util.ArrayList;
import java.util.List;

public class OrderDetail {
    private int orderDetailId;
    private int orderId;
    private int productId;
    private String size; // "S", "M", "L"
    private int sugarLevel;
    private int iceLevel;
    private int quantity;
    private int priceAtPurchase;

    // Thuộc tính để lưu chuỗi mô tả gộp (dùng cho database nếu bảng OrderDetails chỉ có 1 cột notes)
    private String notes;

    // Thuộc tính mở rộng để hỗ trợ hiển thị giỏ hàng trên JavaFX dễ dàng hơn
    // Không map trực tiếp với 1 cột trong database nhưng rất hữu ích cho code xử lý
    private List<Topping> toppings = new ArrayList<>();

    public OrderDetail() {}

    // Constructor đầy đủ ban đầu của bạn
    public OrderDetail(int orderDetailId, int orderId, int productId, String size, int sugarLevel, int iceLevel, int quantity, int priceAtPurchase) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.productId = productId;
        this.size = size;
        this.sugarLevel = sugarLevel;
        this.iceLevel = iceLevel;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    // BỔ SUNG: Constructor mới dùng trong StaffPOSController (không cần orderDetailId vì DB tự tăng)
    public OrderDetail(int orderId, int productId, int quantity, int priceAtPurchase, String notes) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
        this.notes = notes;
    }

    public int getOrderDetailId() { return orderDetailId; }
    public void setOrderDetailId(int orderDetailId) { this.orderDetailId = orderDetailId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public int getSugarLevel() { return sugarLevel; }
    public void setSugarLevel(int sugarLevel) { this.sugarLevel = sugarLevel; }

    public int getIceLevel() { return iceLevel; }
    public void setIceLevel(int iceLevel) { this.iceLevel = iceLevel; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(int priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }

    public List<Topping> getToppings() { return toppings; }
    public void setToppings(List<Topping> toppings) { this.toppings = toppings; }

    // BỔ SUNG: Các Getter/Setter đồng bộ tên gọi với DAO ở bước trước
    // Trả về priceAtPurchase khi DAO gọi getUnitPrice()
    public int getUnitPrice() {
        return priceAtPurchase;
    }

    public String getNotes() {
        // Nếu biến notes chưa có giá trị, tự động tạo chuỗi từ size, đường, đá
        if (notes == null || notes.isEmpty()) {
            return String.format("Size: %s, Đường: %d%%, Đá: %d%%", size, sugarLevel, iceLevel);
        }
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}