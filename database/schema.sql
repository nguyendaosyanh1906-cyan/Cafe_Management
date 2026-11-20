-- ==============================================================================
-- 1. KHỞI TẠO DATABASE
-- ==============================================================================
CREATE SCHEMA `cafe_management_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cafe_management_db;
-- ==============================================================================
-- 2. TẠO CÁC BẢNG (CẤU TRÚC DATABASE)
-- ==============================================================================

-- 2.1 BẢNG NGƯỜI DÙNG (Auth & HR)
CREATE TABLE Users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role ENUM('ADMIN', 'STAFF') NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       phone VARCHAR(15),
                       salary INT DEFAULT 0,
                       is_active BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2.2 BẢNG KHÁCH HÀNG (Customer & Loyalty)
CREATE TABLE Customers (
                           customer_id INT AUTO_INCREMENT PRIMARY KEY,
                           phone VARCHAR(15) NOT NULL UNIQUE,
                           full_name VARCHAR(100) NOT NULL,
                           points INT DEFAULT 0,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2.3 BẢNG DANH MỤC SẢN PHẨM (Menu)
CREATE TABLE Categories (
                            category_id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL
);

-- 2.4 BẢNG TOPPING (Customize)
CREATE TABLE Toppings (
                          topping_id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          price INT NOT NULL,
                          is_active BOOLEAN DEFAULT TRUE
);

-- 2.5 BẢNG SẢN PHẨM (Menu)
CREATE TABLE Products (
                          product_id INT AUTO_INCREMENT PRIMARY KEY,
                          category_id INT NOT NULL,
                          name VARCHAR(150) NOT NULL,
                          description TEXT,
                          image_url VARCHAR(255),
                          base_price INT NOT NULL,
                          is_active BOOLEAN DEFAULT TRUE,
                          FOREIGN KEY (category_id) REFERENCES Categories(category_id) ON DELETE RESTRICT
);

-- 2.6 BẢNG HÓA ĐƠN (Cart, Order & Payment)
CREATE TABLE Orders (
                        order_id INT AUTO_INCREMENT PRIMARY KEY,
                        staff_id INT NOT NULL,
                        customer_id INT,
                        total_amount INT NOT NULL DEFAULT 0,
                        points_used INT DEFAULT 0,
                        payment_method ENUM('CASH', 'QR') NOT NULL,
                        status ENUM('PENDING', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (staff_id) REFERENCES Users(user_id) ON DELETE RESTRICT,
                        FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE SET NULL
);

-- 2.7 BẢNG CHI TIẾT HÓA ĐƠN (Customize)
CREATE TABLE Order_Details (
                               order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
                               order_id INT NOT NULL,
                               product_id INT NOT NULL,
                               size ENUM('S', 'M', 'L') NOT NULL DEFAULT 'S',
                               sugar_level INT DEFAULT 100,
                               ice_level INT DEFAULT 100,
                               quantity INT NOT NULL DEFAULT 1,
                               price_at_purchase INT NOT NULL,
                               FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
                               FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE RESTRICT
);

-- 2.8 BẢNG TRUNG GIAN CHI TIẾT TOPPING (Customize)
CREATE TABLE Order_Detail_Toppings (
                                       order_detail_id INT NOT NULL,
                                       topping_id INT NOT NULL,
                                       topping_price INT NOT NULL,
                                       PRIMARY KEY (order_detail_id, topping_id),
                                       FOREIGN KEY (order_detail_id) REFERENCES Order_Details(order_detail_id) ON DELETE CASCADE,
                                       FOREIGN KEY (topping_id) REFERENCES Toppings(topping_id) ON DELETE RESTRICT
);