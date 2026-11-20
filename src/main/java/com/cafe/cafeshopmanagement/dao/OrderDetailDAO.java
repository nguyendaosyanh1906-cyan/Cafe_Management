package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.model.CartItem;
import java.util.List;

public interface OrderDetailDAO {
    boolean saveOrderDetailsAndToppings(int orderId, List<CartItem> cartItems);
}