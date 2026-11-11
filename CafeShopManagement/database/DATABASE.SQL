//Cách tạo database trong mySQL

CREATE SCHEMA `cafe_management_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;

//Cách tạo các table trong mySQL

USE cafe_management_db;
-- 1. BẢNG NGƯỜI DÙNG (Auth & HR)
CREATE TABLE Users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role ENUM('ADMIN', 'STAFF') NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       phone VARCHAR(15),
                       salary INT DEFAULT 0, -- Dùng INT cho tiền VNĐ
                       is_active BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. BẢNG KHÁCH HÀNG (Customer & Loyalty)
CREATE TABLE Customers (
                           customer_id INT AUTO_INCREMENT PRIMARY KEY,
                           phone VARCHAR(15) NOT NULL UNIQUE,
                           full_name VARCHAR(100) NOT NULL,
                           points INT DEFAULT 0,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. BẢNG DANH MỤC SẢN PHẨM (Menu)
CREATE TABLE Categories (
                            category_id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL
);

-- 4. BẢNG SẢN PHẨM (Menu)
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

-- 5. BẢNG TOPPING (Customize)
CREATE TABLE Toppings (
                          topping_id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          price INT NOT NULL,
                          is_active BOOLEAN DEFAULT TRUE
);

-- 6. BẢNG HÓA ĐƠN (Cart, Order & Payment)
CREATE TABLE Orders (
                        order_id INT AUTO_INCREMENT PRIMARY KEY,
                        staff_id INT NOT NULL,
                        customer_id INT, -- Có thể NULL nếu khách vãng lai không lưu thông tin
                        total_amount INT NOT NULL DEFAULT 0,
                        points_used INT DEFAULT 0,
                        payment_method ENUM('CASH', 'QR') NOT NULL,
                        status ENUM('PENDING', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (staff_id) REFERENCES Users(user_id) ON DELETE RESTRICT,
                        FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE SET NULL
);

-- 7. BẢNG CHI TIẾT HÓA ĐƠN (Customize)
CREATE TABLE Order_Details (
                               order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
                               order_id INT NOT NULL,
                               product_id INT NOT NULL,
                               size ENUM('S', 'M', 'L') NOT NULL DEFAULT 'S',
                               sugar_level INT DEFAULT 100, -- Ví dụ: 0, 30, 50, 70, 100
                               ice_level INT DEFAULT 100,
                               quantity INT NOT NULL DEFAULT 1,
                               price_at_purchase INT NOT NULL, -- Lưu giá chốt tại thời điểm mua (Base price + Size surcharge)
                               FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
                               FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE RESTRICT
);

-- 8. BẢNG TRUNG GIAN CHI TIẾT TOPPING (Customize)
-- Giải quyết bài toán 1 ly nước có nhiều topping
CREATE TABLE Order_Detail_Toppings (
                                       order_detail_id INT NOT NULL,
                                       topping_id INT NOT NULL,
                                       topping_price INT NOT NULL, -- Lưu lại giá topping tại thời điểm mua tránh lỗi thống kê sau này nếu giá thay đổi
                                       PRIMARY KEY (order_detail_id, topping_id),
                                       FOREIGN KEY (order_detail_id) REFERENCES Order_Details(order_detail_id) ON DELETE CASCADE,
                                       FOREIGN KEY (topping_id) REFERENCES Toppings(topping_id) ON DELETE RESTRICT
);





//Cách thêm giá trị cho từng bảng

USE cafe_management_db;

-- 1. Thêm dữ liệu vào bảng Users[cite: 5]
INSERT INTO Users (user_id, username, password_hash, role, full_name, phone, salary, is_active) VALUES
                                                                                                    (1, 'admin', '$2a$12$Oxv1pgXcaokh5PfCgYSisu6VPTXIrOK5Tr3XD.MzKp2akWZkj5Nk.', 'ADMIN', 'Quản trị viên hệ thống', '104240750', 15000000, 1),//123
                                                                                                    (2, 'staff1', '$2a$12$5kRvwhTRDo.2ql8R8n383u5NxWmZwTQ3Ob2LjMK52/4VylqGzTTJe', 'STAFF', 'Nhân viên quầy số 1', '104240022', 6000000, 1);

-- 2. Thêm dữ liệu vào bảng Categories[cite: 6]
INSERT INTO Categories (category_id, name) VALUES
                                               (1, 'Cà Phê Truyền Thống'),
                                               (2, 'Cà Phê Pha Máy'),
                                               (3, 'Phindi');

-- 3. Thêm dữ liệu vào bảng Products[cite: 7]
INSERT INTO Products (product_id, category_id, name, base_price, is_active) VALUES
                                                                                (1, 1, 'Cà Phê Đen', 15000, 1),
                                                                                (2, 1, 'Cà Phê Sữa', 15000, 1),
                                                                                (3, 1, 'Cà Phê Muối', 20000, 1),
                                                                                (4, 1, 'Cà Phê Trứng', 20000, 1),
                                                                                (5, 1, 'Cà Phê Cốt Dừa', 20000, 1),
                                                                                (6, 1, 'Bạc Xỉu', 15000, 1),
                                                                                (7, 2, 'Espresso', 35000, 1),
                                                                                (8, 2, 'Americano', 35000, 1),
                                                                                (9, 2, 'Cappuccino', 45000, 1),
                                                                                (10, 2, 'Latte', 45000, 1),
                                                                                (11, 2, 'Caramel Macchiato', 55000, 1),
                                                                                (12, 2, 'Mocha', 55000, 1),
                                                                                (13, 3, 'Phindi Kem Sữa', 40000, 1),
                                                                                (14, 3, 'Phindi Hạnh Nhân', 40000, 1),
                                                                                (15, 3, 'Phindi Choco', 40000, 1);

INSERT INTO Customers (customer_id, phone, full_name, points, created_at) VALUES (1, '104240047', 'Nguyễn Phúc Vinh', 1000, NULL), (2, '123456789', 'Alexander đệ tam', 0, NULL);
