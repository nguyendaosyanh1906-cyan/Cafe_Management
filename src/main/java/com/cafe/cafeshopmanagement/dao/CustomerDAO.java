package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.model.Customer;
import java.util.List;

public interface CustomerDAO {
    // Các hàm CRUD cơ bản (Dành cho Admin)
    List<Customer> getAllCustomers();
    boolean addCustomer(Customer c);
    boolean updateCustomer(Customer c);
    boolean deleteCustomer(int customerId);

    // Các hàm nghiệp vụ (Dành cho POS)
    Customer getCustomerByPhone(String phone);
    boolean updateCustomerPoints(int customerId, int pointsToAdd, int pointsUsed);
    Customer registerCustomer(String phone, String fullName);
}