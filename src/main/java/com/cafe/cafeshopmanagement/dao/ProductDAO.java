package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.model.Product;
import java.util.List;

public interface ProductDAO {
    List<Product> getAllProducts();
    List<Product> getActiveProducts();
    List<Product> getProductsByCategory(int categoryId);
    boolean addProduct(Product p);
    boolean updateProduct(Product p);
    boolean deleteProduct(int productId);
}