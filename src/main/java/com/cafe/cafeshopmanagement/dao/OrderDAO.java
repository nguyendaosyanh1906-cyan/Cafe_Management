package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.model.Order;
import com.cafe.cafeshopmanagement.model.OrderHistoryDTO;
import java.util.List;

public interface OrderDAO {
    int createOrder(Order order);
    List<OrderHistoryDTO> getAllOrderHistory();
}